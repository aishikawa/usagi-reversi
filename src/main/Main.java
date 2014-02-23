/*
 * Copyright 2014 A.Ishikawa
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import player.ManualPlayer;
import player.MctsPlayer;
import player.Player;
import player.RandomPlayer;
import reversi.ReversiAction;
import reversi.ReversiState;
import reversi.Stone;


public class Main {
	
	public static void main(String[] args) {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		Stone[] stone = {Stone.BLACK, Stone.WHITE};
		
		Map<Stone, Player> player = new HashMap<Stone, Player>();
		for (int i=0; i<2; i++) {
			System.out.println("select "+ReversiState.getChar(stone[i])+" player");
			System.out.println("man:0  random:1  1sec:2  3sec:3" );
			try {
				int p = Integer.parseInt(stdin.readLine());
				if (p == 0) {
					player.put(stone[i], new ManualPlayer(stdin));
				} else if (p == 1) {
					player.put(stone[i], new RandomPlayer(new Random(1)));
				} else if (p == 2) {
					MctsPlayer pl = new MctsPlayer(1000);
					player.put(stone[i], pl);
				} else if (p == 3) {
					MctsPlayer pl = new MctsPlayer(3000);
					player.put(stone[i], pl);
				} else {
					i -= 1;
					continue;
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			} catch (NumberFormatException e) {
				i -= 1;
				continue;
			}			
		}

		//initialize a game
		ReversiState current = null;
		try {
			current = ReversiState.getInitialReversiState("initial.board");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		int numMove = 0;
		ArrayList<ReversiState> game = new ArrayList<ReversiState>();
		game.add(current.getDeepCopy());
		boolean resigned = false;
		
		//play a game
		try {
			while (current.isTerminal() == false) {
				Stone turn = Stone.getStone(current.getTurn());
				Player p = player.get(turn);

				System.out.println(current);
				System.out.println(ReversiState.getChar(turn)+"'s turn");

				ReversiAction move = p.getAction(current);
				if (move.isResign()) {
					resigned = true;
					break;
				} else if (move.isUndo()) {
					if (numMove >= 1) {
						do {
							game.remove(numMove);
							numMove -= 1;
						} while (numMove >= 1 && Stone.getStone(game.get(numMove).getTurn()) == Stone.opposite(turn));
							
						current = game.get(numMove).getDeepCopy();
					}
				} else {
					current.process(move);
					System.out.println(move);
					game.add(current.getDeepCopy());
					numMove += 1;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(current);
		
		// game is finished
		if (resigned) {
			if (numMove % 2 == 0) {
				System.out.println(ReversiState.getChar(stone[1])+" won");
			} else {
				System.out.println(ReversiState.getChar(stone[0])+" won");
			}				
		} else {
			int blackNum = current.getNum(Stone.BLACK);
			int whiteNum = current.getNum(Stone.WHITE);
			System.out.println(ReversiState.getChar(stone[0])+":"+blackNum+" "+ReversiState.getChar(stone[1])+":"+whiteNum);
			if (blackNum > whiteNum) {
				System.out.println(ReversiState.getChar(stone[0])+" won");
			} else if (whiteNum > blackNum) {
				System.out.println(ReversiState.getChar(stone[1])+" won");
			} else {
				System.out.println("draw");
			}
		}
	}
}

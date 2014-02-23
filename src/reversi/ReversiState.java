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

package reversi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mctslib.game.State;

public class ReversiState extends State<ReversiAction>{
	public static final Map<Stone, Character> character;
		
	private final int BOARD_HEIGHT;
	private final int BOARD_WIDTH;	

	private Stone[][] board;
	private Stone turn;
	private boolean terminal;
	
	static {
		character = new HashMap<Stone, Character>();
		character.put(Stone.BLACK, 'x');
		character.put(Stone.WHITE, 'o');
		character.put(Stone.EMPTY, '.');
		character.put(Stone.OUT, '*');
	}
	
	public static char getChar(Stone stone) {
		return character.get(stone);
	}
	
	private ReversiState(int height, int width, String[] initial) {
		BOARD_HEIGHT = height;
		BOARD_WIDTH = width;
		
		Set<Map.Entry<Stone, Character>> entrySet = character.entrySet();		
		board = new Stone[BOARD_HEIGHT][];		
		for (int y=0; y<height; y++) {
			board[y] = new Stone[BOARD_WIDTH]; 
			for (int x=0; x<width; x++) {
				char c = initial[y].charAt(x);
				for (Map.Entry<Stone, Character> entry : entrySet) {
					if (entry.getValue().equals(c)) {
						board[y][x] = entry.getKey();
					}
				}
			}
		}
		turn = Stone.BLACK;
		terminal = false;
	}
	
	/**
	 * copy constructor
	 * @param b
	 */
	private ReversiState(ReversiState b) {
		BOARD_HEIGHT = b.BOARD_HEIGHT;
		BOARD_WIDTH = b.BOARD_WIDTH;
		board = new Stone[b.board.length][];
		for (int i=0; i<b.board.length; i++) {
			board[i] = new Stone[b.board[i].length];
			for (int j=0; j<b.board[i].length; j++) {
				board[i][j] = b.board[i][j];
			}
		}
		turn = b.turn;
		terminal = b.terminal;
	}
	
	public static ReversiState getInitialReversiState(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = br.readLine();
		String[] a = line.split("\\s+");
		int height = Integer.parseInt(a[0]);
		int width = Integer.parseInt(a[1]);
		
		String[] initial = new String[height];
		for (int i=0; i<height; i++) {
			initial[i] = br.readLine();
		}
		br.close();
		
		ReversiState board = new ReversiState(height, width, initial);
		return board;
	}
	
	public ReversiState getDeepCopy() {
		return new ReversiState(this);
	}

	public void process(ReversiAction action/*, Stone stone*/) {		
		int x = action.getX();
		int y = action.getY();
		board[y][x] = turn;//stone;
		
		Stone opposite = Stone.opposite(turn);
		for (int yd=-1; yd<=1; yd++) {
			for (int xd=-1; xd<=1; xd++) {
				if (board[y+yd][x+xd] == opposite) {
					int i = 2;
					while (board[y+yd*i][x+xd*i] == opposite) {
						i += 1;
					}
					if (board[y+yd*i][x+xd*i] == turn) {
						for ( ; i>=1; i--) {
							board[y+yd*i][x+xd*i] = turn;
						}
					}
				}
			}
		}
		
		if (getLegalActions(opposite).size() == 0) {
			if (getLegalActions(turn).size() == 0) {
				terminal = true;
			}
		} else {
			turn = opposite;
		}
	}

	public List<ReversiAction> getLegalActions() {
		return getLegalActions(turn);
	}
	
	private List<ReversiAction> getLegalActions(Stone stone) {
		List<ReversiAction> ret = new LinkedList<ReversiAction>();
		for (int i=0; i<board.length; i++) {
			for (int j=0; j<board[i].length; j++) {
				ReversiAction move = new ReversiAction(i, j);
				if (canPut(move, stone)) {
					ret.add(move);
				}
			}
		}
		return ret;
	}

	public Map<Integer, Double> getScore() {
		Map<Integer, Double> score = new HashMap<Integer, Double>();
		if (isTerminal()) {
			int numB = getNum(Stone.BLACK);
			int numW = getNum(Stone.WHITE);
			if (numB > numW) {
				score.put(Stone.BLACK.toInt(), 1.0);
				score.put(Stone.WHITE.toInt(), 0.0);
			} else if (numB < numW) {
				score.put(Stone.BLACK.toInt(), 0.0);
				score.put(Stone.WHITE.toInt(), 1.0);
			} else {
				score.put(Stone.BLACK.toInt(), 0.5);
				score.put(Stone.WHITE.toInt(), 0.5);
			}
		} else {
			score.put(Stone.BLACK.toInt(), 0.5);
			score.put(Stone.WHITE.toInt(), 0.5);
		}
		return score;
	}

	private boolean canPut(ReversiAction pos, Stone stone) {
		int x = pos.getX();
		int y = pos.getY();
		if (x < 0 || board.length <= x || y < 0 || board[x].length <= y) {
			return false;
		}
		if (board[y][x] != Stone.EMPTY) {
			return false;
		}
		
		Stone opposite = Stone.opposite(stone);	
		for (int yd=-1; yd<=1; yd++) {
			for (int xd=-1; xd<=1; xd++) {
				if (board[y+yd][x+xd] == opposite) {
					int i = 2;
					while (board[y+yd*i][x+xd*i] == opposite) {
						i += 1;
					}
					if (board[y+yd*i][x+xd*i] == stone) {
						return true;
					}
				}
			}
		}		
		return false;
	}
	
	public boolean isTerminal() {
		//return (getLegalActions(Stone.BLACK).size() == 0) && (getLegalActions(Stone.WHITE).size() == 0);
		return terminal;
	}
	
	public int getTurn() {
		return turn.toInt();
	}	

	public int getNum(Stone stone) {
		int ret = 0;
		for (int i=0; i<board.length; i++) {
			for (int j=0; j<board[i].length; j++) {
				if (board[i][j] == stone) {
					ret += 1;
				}
			}
		}
		return ret;
	}
	
	
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<BOARD_HEIGHT; i++) {
			for (int j=0; j<BOARD_WIDTH; j++) {
				if (i==0 && 0 < j && j <= BOARD_WIDTH-2) {
					buff.append((char)('a'+j-1));
				} else if (j == 0 && 0 < i && i <= BOARD_HEIGHT-2) {
					buff.append(i);
				} else {
					buff.append(character.get(board[i][j]));
				}
			}
			buff.append("\n");
		}
		return buff.toString();
	}

}

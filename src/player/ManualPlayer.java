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

package player;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import reversi.ReversiAction;
import reversi.ReversiState;

public class ManualPlayer extends Player {
	private BufferedReader br;
	
	public ManualPlayer(BufferedReader b) {
		br = b;
	}
	
	@Override
	public ReversiAction getAction(ReversiState board) throws IOException {
		ReversiAction ret = null;
		while (true) {
			System.out.print("input move (\"undo\", \"resign\" or like \"c5\"): ");			
			String line = br.readLine();
			
			if (line.equals("undo")) {
				ret = new ReversiAction(-1, -1);
				ret.setUndo(true);
				break;
			} else if (line.equals("resign")) {
				ret = new ReversiAction(-1, -1);
				ret.setResign(true);
				break;
			} else if (line.length() == 2){
				int col = line.charAt(0) - 'a' + 1;
				int row = line.charAt(1) - '1' + 1;
				ret = new ReversiAction(row, col);

				List<ReversiAction> legalList = board.getLegalActions();
				if (legalList.contains(ret)) {
					break;
				}
			}
			System.out.println("\""+line+"\""+" is illegal.");
		}
		return ret;
	}
}

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

import mctslib.game.Action;


public class ReversiAction extends Action {
	private int y;
	private int x;
	private boolean undo;
	private boolean resign;
	
	public ReversiAction(int yy, int xx) {
		y = yy;
		x = xx;
	}
	
	public int getY() {
		return y;
	}
	
	public int getX() {
		return x;
	}
	
	public boolean isUndo() {
		return undo;
	}
	
	public boolean isResign() {
		return resign;
	}
	
	public void setUndo(boolean u) {
		undo = u;
	}
	
	public void setResign(boolean r) {
		resign = r;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof ReversiAction) {
			ReversiAction action = (ReversiAction)o;
			return undo == action.undo && resign == action.resign && x == action.x && y == action.y;
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuffer bf  = new StringBuffer();
		bf.append((char)(x+'a'-1));
		bf.append(y);
		return bf.toString();
	}
}

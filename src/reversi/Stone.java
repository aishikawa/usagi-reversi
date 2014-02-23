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

public enum Stone {
	BLACK,
	WHITE,
	EMPTY,
	OUT;
	
	public static Stone opposite(Stone stone) {
		if (stone == BLACK) {
			return WHITE;
		} else if (stone == WHITE) {
			return BLACK;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public int toInt() {
		if (this == BLACK) {
			return 0;
		} else if (this == WHITE) {
			return 1;
		} else if (this == EMPTY) {
			return 2;
		}
		return 3;
	}
	
	public static Stone getStone(int integer) {
		if (integer == 0) {
			return BLACK;
		} else if (integer == 1) {
			return WHITE;
		} else if (integer == 2) {
			return EMPTY;
		}
		return OUT;
	}
}

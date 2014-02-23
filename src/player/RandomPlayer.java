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

import java.io.IOException;
import java.util.List;
import java.util.Random;

import reversi.ReversiAction;
import reversi.ReversiState;

public class RandomPlayer extends Player {
	private Random rand;
	
	public RandomPlayer(Random r) {
		rand = r;
	}

	@Override
	public ReversiAction getAction(ReversiState board) throws IOException {
		List<ReversiAction> legalMoves = board.getLegalActions();
		int index = rand.nextInt(legalMoves.size());
		return legalMoves.get(index);
	}
}

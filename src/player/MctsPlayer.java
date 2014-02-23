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

import java.util.Random;

import mctslib.mcts.Mcts;
import reversi.ReversiAction;
import reversi.ReversiState;

public class MctsPlayer extends Player {
	private Mcts<ReversiAction> mcts;
	
	public MctsPlayer(int timelimit) {
		mcts = new Mcts<ReversiAction>();
		mcts.setTimeLimit(timelimit);
		mcts.setExpandThreshold(2);
	}
	
	@Override
	public ReversiAction getAction(ReversiState state) {
		return mcts.getAction(state);
	}
	
	//
	// setters
	//
	public void setExpandThreshold(int threshold) {
		mcts.setExpandThreshold(threshold);
	}
	
	public void setTimeLimit(long l) {
		mcts.setTimeLimit(l);
	}
	
	public void setC(double c) {
		mcts.setC(c);
	}
	
	public void setRand(Random r) {
		mcts.setRand(r);
	}
	
	public void setVerbose(boolean v) {
		mcts.setVerbose(v);
	}
}

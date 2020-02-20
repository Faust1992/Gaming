/****************************************************************
 * studPlayer.java
 * Implements MiniMax search with A-B pruning and iterative deepening search (IDS). The static board
 * evaluator (SBE) function is simple: the # of stones in studPlayer's
 * mancala minue the # in opponent's mancala.
 * -----------------------------------------------------------------------------------------------------------------
 * Licensing Information: You are free to use or extend these projects for educational purposes provided that
 * (1) you do not distribute or publish solutions, (2) you retain the notice, and (3) you provide clear attribution to UW-Madison
 *
 * Attribute Information: The Mancala Game was developed at UW-Madison.
 *
 * The initial project was developed by Chuck Dyer(dyer@cs.wisc.edu) and his TAs.
 *
 * Current Version with GUI was developed by Fengan Li(fengan@cs.wisc.edu).
 * Some GUI componets are from Mancala Project in Google code.
 */




//################################################################
// studPlayer class
//################################################################

public class botPlayer extends Player {

	public static final int infinte = 1000;
    /*Use IDS search to find the best move. The step starts from 1 and keeps incrementing by step 1 until
	 * interrupted by the time limit. The best move found in each step should be stored in the
	 * protected variable move of class Player.
     */
    public void move(GameState state)
    {
    	GameState currentState = new GameState(state);
    	for (int maxDepth = 1; ; maxDepth++){
    		move = maxAction(state, maxDepth);
    		state = new GameState(currentState);
    	}
    		
    	
    }

    // Return best move for max player. Note that this is a wrapper function created for ease to use.
	// In this function, you may do one step of search. Thus you can decide the best move by comparing the 
	// sbe values returned by maxSBE. This function should call minAction with 5 parameters.
    public int maxAction(GameState state, int maxDepth)
    {
    	int bestMove = -1;
    	int alpha = -infinte;
    	int beta = infinte;
		int bestValue = -infinte;
    	int currentDepth = 2;
    	int value;
		GameState currentState = new GameState(state);
    	for (int i = 0; i < 6; i++){
    		if (state.stoneCount(i) == 0)
    			continue;
    		else if (bestMove == -1)
    			bestMove = i;
    		if (!state.applyMove(i)){
    			if (maxDepth == 1)
    				value = sbe(state);
    			else
    			value = minAction(state, currentDepth, maxDepth, alpha, beta);
    			if (value > bestValue){
    				bestValue = value;
    				bestMove = i;
    			}
    		}
    			else{
    				value = maxAction(state, currentDepth, maxDepth, alpha, beta);
    				if (value > bestValue){
    					bestValue = value;
        				bestMove = i;
        			}
    		}
    		state = new GameState(currentState);
    	}
    	return bestMove;
    }
    
    public int minAction(GameState state, int maxDepth)
    {
    	int bestMove = -1;
    	int alpha = -infinte;
    	int beta = infinte;
		int bestValue = infinte;
    	int currentDepth = 2;
    	int value;
    	GameState currentState = new GameState(state);
    	for (int i = 0; i < 6; i++){
    		if (state.stoneCount(i) == 0)
    			continue;
    		else if (bestMove == -1)
    			bestMove = i;
    		if (!state.applyMove(i)){
    			if (maxDepth == 1)
    				value = sbe(state);
    			else
    			value = maxAction(state, currentDepth, maxDepth, alpha, beta);
    			if (value < bestValue){
    				bestValue = value;
    				bestMove = i;
    			}
    		}
    			else{
    				value = minAction(state, currentDepth, maxDepth, alpha, beta);
    				if (value < bestValue){
    					bestValue = value;
        				bestMove = i;
        			}
    		}
    		state = new GameState(currentState);
    	}
    	return bestMove;
    }
	//return sbe value related to the best move for max player
    public int maxAction(GameState state, int currentDepth, int maxDepth, int alpha, int beta)
    {
    	if (currentDepth == maxDepth)
    		return sbe(state);
    	int v = -infinte;
		GameState currentState = new GameState(state);
    	for (int i = 0; i < 6; i++){
    		if (state.stoneCount(i) == 0)
    			continue;
    		if(!state.applyMove(i))
    		v = Math.max(v, minAction(state, currentDepth + 1, maxDepth, alpha, beta));
    		else v = Math.max(v, maxAction(state, currentDepth, maxDepth, alpha, beta));
    		if (v >= beta)
    			return v;
    		alpha = Math.max(alpha, v);
    		state = new GameState(currentState);
    	}
    	return v;
    }
    //return sbe value related to the best move for min player
    public int minAction(GameState state, int currentDepth, int maxDepth, int alpha, int beta)
    {
    	if (currentDepth == maxDepth)
    		return sbe(state);
    	int v = infinte;
		GameState currentState = new GameState(state);
    	for (int i = 0; i < 6; i++){
    		if (state.stoneCount(i) == 0)
    			continue;
    		if(!state.applyMove(i))
    		v = Math.min(v, maxAction(state, currentDepth + 1, maxDepth, alpha, beta));
    		else v = Math.min(v, minAction(state, currentDepth, maxDepth, alpha, beta));
    		if (alpha >= v)
    			return v;
    		beta = Math.min(beta, v);
    		state = new GameState(currentState);
    	}
    	return v;
    }

    //the sbe function for game state. Note that in the game state, the bins for current player are always in the bottom row.
    private int sbe(GameState state)
    {
    	int sbevalue;
    	if (state.status() == -1)
    		sbevalue = -infinte+1;
    	else if (state.status() == 1)
    		sbevalue = infinte-1;
    	else if (state.status() == 0)
    		sbevalue = 0;
    	else
    	sbevalue = state.stoneCount(6) - state.stoneCount(13);
    	return sbevalue;
    }
}


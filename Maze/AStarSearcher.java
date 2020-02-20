import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * A* algorithm search
 * 
 * You should fill the search() method of this class.
 */
public class AStarSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public AStarSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main a-star search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {

		// FILL THIS METHOD
		// explored list is a Boolean array that indicates if a state associated with a given position in the maze has already been explored. 
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];
		// ...

		PriorityQueue<StateFValuePair> frontier = new PriorityQueue<StateFValuePair>();

		// TODO initialize the root state and add
		// to frontier list
		// ...
		State stateS = new State(maze.getPlayerSquare(), null, 0, 0);
		Square goal = maze.getGoalSquare();
		double pair = Math.sqrt(Math.pow((stateS.getSquare().X-goal.X),2)+Math.pow((stateS.getSquare().Y-goal.Y),2))+stateS.getGValue();
        StateFValuePair Spair = new StateFValuePair(stateS, pair);
		frontier.add(Spair);
		while (!frontier.isEmpty()) {
			StateFValuePair a = frontier.poll();
			explored[a.getState().getSquare().X][a.getState().getSquare().Y] = true;
			if (this.maxDepthSearched < a.getState().getDepth())
				this.maxDepthSearched = a.getState().getDepth();
			if (a.getState().getSquare().X == goal.X && a.getState().getSquare().Y == goal.Y)
			{
				State b = a.getState().getParent();
				this.cost=a.getState().getDepth();
				while (b.getParent() != null){
					maze.setOneSquare(b.getSquare(), '.');
					b = b.getParent();
				}
				return true;
			}
			this.noOfNodesExpanded++;
			// TODO return true if a solution has been found
			ArrayList<State> array = a.getState().getSuccessors(explored, maze);
			for (State successor : array)
			{
				pair = Math.sqrt(Math.pow((successor.getSquare().X-goal.X),2)+Math.pow((successor.getSquare().Y-goal.Y),2))+successor.getGValue();
				frontier.add(new StateFValuePair(successor,pair));
			}
			// TODO maintain the cost, noOfNodesExpanded (a.k.a. noOfNodesExplored),
			// maxDepthSearched, maxSizeOfFrontier during
			// the search
			// TODO update the maze if a solution found
            if (this.maxSizeOfFrontier < frontier.size())
            	this.maxSizeOfFrontier = frontier.size();
			// use frontier.poll() to extract the minimum stateFValuePair.
			// use frontier.add(...) to add stateFValue pairs
		}
		return false;
		// TODO return false if no solution
	}

}

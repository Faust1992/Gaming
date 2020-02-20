import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Depth-First Search (DFS)
 * 
 * You should fill the search() method of this class.
 */
public class DepthFirstSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public DepthFirstSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main depth first search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {
		// FILL THIS METHOD
		// explored list is a 2D Boolean array that indicates if a state associated with a given position in the maze has already been explored.
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];

		// ...

		// Stack implementing the Frontier list
		LinkedList<State> stack = new LinkedList<State>();
		State stateS = new State(maze.getPlayerSquare(), null, 0, 0);
		Square goal = maze.getGoalSquare();
        stack.push(stateS);
		while (!stack.isEmpty()) {
			State a = stack.pop();
			explored[a.getSquare().X][a.getSquare().Y] = true;
			if (this.maxDepthSearched < a.getDepth())
				this.maxDepthSearched = a.getDepth();
			if (a.getSquare().X == goal.X && a.getSquare().Y == goal.Y)
			{
				State b = a.getParent();
				this.cost=a.getDepth();
				while (b.getParent() != null){
					maze.setOneSquare(b.getSquare(), '.');
					b = b.getParent();
				}
				return true;
			}
			this.noOfNodesExpanded++;
			// TODO return true if find a solution
			ArrayList<State> array = a.getSuccessors(explored, maze);
			for (State successor : array)
				stack.push(successor);
			// TODO maintain the cost, noOfNodesExpanded (a.k.a. noOfNodesExplored),
            if (this.maxSizeOfFrontier < stack.size())
            	this.maxSizeOfFrontier = stack.size();
			// maxDepthSearched, maxSizeOfFrontier during
			// the search
			// TODO update the maze if a solution found

			// use stack.pop() to pop the stack.
			// use stack.push(...) to elements to stack
		}
        	 return false;
		// TODO return false if no solution
	}
}

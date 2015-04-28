/*
 * Solver.java
 * 
 * The Solver class reads input from file and constructs a tile 
 * puzzle with a single empty space denoted by the value 0. It then proceeds to 
 * solve the puzzle using the A* algorithm. For each board that is created from
 * an operator, up, down, left, right, a twin is created and checked. If a solution to a twin is found 
 * then the initial puzzle is unsolvable. http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html.
 * The Solver class uses the Board class that consists of a 2d array representing the tiles in
 * the puzzle. 
 * 
 * Link to test cases : http://coursera.cs.princeton.edu/algs4/testing/8puzzle/ 
 * 
 * @author David Morton K00179391
 * 
 * Artificial Intelligence Programming Assignment 
 * 8 Puzzle Problem
 * 
 */
public class Solver{
	
	// The goalStateNode is the Goal State search node when a solution is found
	private Node goalStateNode;	
	
	// Flag to indicate an unsolvable puzzle
	private boolean isSolvable; 
	
	/*
	 * The Node class contains information about the board, number of moves
	 * taken to get to this node, its parent node, and its heuristic value.
	 * The node becomes part of a tree structure that is used by the A* algorithm 
	 * to record the path from the initial state to the goal state. It implements
	 * the Comparable interface that requires the implementation of the compareTo() function.
	 */
	private static class Node implements Comparable<Node> 
	{        
        private final Board board; 
        private final int moves;       
        private final Node previousNode;        
        private final int priority;

        /*
         * Constructor for a Node that stores a board state, its parent
         * Node, the number of moves taken to get to this node, and the 
         * heuristic value of this Node
         * 
         * @param Board board for this node
         * @param Node previousNode is the parent search node
         */
        private Node(Board board, Node previousNode)
        {
        	// Board state
            this.board = board;
            
            // Parent search node
            this.previousNode = previousNode;
            
            // The number of moves taken to get to this Node
            this.moves = (this.previousNode == null) ? 0 : this.previousNode.moves + 1;
            
            // The heuristic used to order the search nodes in the priority queue
            this.priority = this.board.manhattan() + this.moves;
        }

        /*
         * compareTo function from Comparable interface(non-Javadoc)
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(Node that) 
        {
        	//StdOut.println("Re-arranging boards based on priority:\n" + this.board + "with\n" + that.board);
        	StdOut.println("Re-arranging boards based on priority...");
            return this.priority - that.priority;
        }

    }
	
	/*
	 * Constructor for the Solver. 
	 * 
	 * @param Board initial the starting state of the board
	 */
    public Solver(Board initial)
    {
        StdOut.println("Solver constructor called"); 
        
        // Check to see if the initial board has already been solved
        if(initial.isGoal())
        {
        	/*
        	 * If the initial board is solved it is in 
        	 * the goal state so there are no previousNode boards 
        	 * in the solution. 
        	 * 
        	 * Set the final goalStateNode to a new search node 
        	 * and set it's previousNode search node to null.        	 
        	 */
        	StdOut.println("Initial board is in goal state");
        	goalStateNode = new Node(initial, null);
        }
        else
        {
        	/*
        	 * If the initial board is not in its goal state
        	 * start solving the board
        	 */
        	StdOut.println("Initial board is not in goal state, Solving...");
        	goalStateNode = solve(initial, initial.twin());
        }
    }
    
    private Node solve(Board initial, Board twin) 
    {    	
    	StdOut.println("Creating Minimum Priority Queues for Initial and Twin boards");
    	
    	/*
    	 * mainPQ queue used to store the nodes in the tree that have not yet been 
    	 * expanded. The nodes with the smallest hueristic value will be checked 
    	 * first.
    	 */
        MinPQ<Node> mainPQ = new MinPQ<Node>();
        
        /*
         * twinPQ used in the same way as the mainPQ but starts with a twin 
         * of the initial node. A twin is the initial with two adjecent tiles 
         * swapped. 
         */
        MinPQ<Node> twinPQ = new MinPQ<Node>();
        
        // Add root Nodes to the priority queues
        mainPQ.insert(new Node(initial, null));
        twinPQ.insert(new Node(twin, null));
               
        StdOut.println("Root board in mainPQ is\n" + initial);
        StdOut.println("Root board in TwinPQ is\n" + twin);
        StdOut.println("Start loop until we find the goal board....");
               
        /*
         * Loop until the initial board has been solved or the twin board
         * takes the glory, meaning the puzzle cannot be solved.                  
         */
        while (true) 
        {      
        	StdOut.println("-----Start of mainQP check");
        	// Check the board that has the smallest heuristic value in the main queue
            Node node = nodeWithSmallestHeuristic(mainPQ);
            
            /*
             *	Check that the node dequeued from the priority queue is the 
             *	goal node. If it is we have solved the puzzle!. 
             */
            if (node.board.isGoal())
            {            	
            	StdOut.println("Found the board in the mainPQ!!");
            	isSolvable = true;
                return node;
            }   
                    
            StdOut.println("-----End of mainQP check");                
           
            /*
             * 	As described here 
             * 	http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html
             * 
             *  If the twin of the initial puzzle is solvable it means that the 
             *  initial puzzle is unsolvable, due to the fact that it is impossible 
             *  to swap tiles directly without using the empty tile to do
             *  so. If the twin of each board with one tile  swapped 
             *  is solvable it means that the other board cannot be solved.   
             */
            StdOut.println("-----Start of twinPQ check");
            if(nodeWithSmallestHeuristic(twinPQ).board.isGoal()) {
            	StdOut.println("The puzzle is not solvable because we solved the twin :( ");
                isSolvable = false;
                return null;
            }            
            StdOut.println("-----End of twinPQ check!!");
        }        
    }
    
    private Node nodeWithSmallestHeuristic(MinPQ<Node> priorityQueue)
    {
    	StdOut.println("Finding the board in the priorityQueue with minimum priority...");
    	Node least = priorityQueue.delMin();
    	StdOut.println("Least priority board in queue found and removed from priorityQueue is\n" + least.board + "priorityQueue size: " + priorityQueue.size());
    	
    	/*
    	 * For each board that is a neighbour of the least priority board
    	 * get its neighbours and move to the next node with least priority.
    	 * Expand the node an pick the next node that has the least heuristic cost
    	 * so that we are moving closer to the goal state.  
    	 */
        for (Board neighbour : least.board.neighbours()) 
        {            
        	//StdOut.println("Neighbour found...");
        	/*
        	 * If this is the root board or the neighbour is not the same as the parent board,
        	 * add it to the main priority queue. This is to avoid looping back
        	 * to a board state that was previousNodely checked.
        	 */
            if (least.previousNode == null || !neighbour.equals(least.previousNode.board)) 
            {
            	StdOut.println(">Adding NEW board to priorityQueue \n" + neighbour);
            	priorityQueue.insert(new Node(neighbour, least));
            }
        }
        StdOut.println("priorityQueue size: " + priorityQueue.size());
        
        return least;
    }
    
    public boolean isSolvable()
    {
    	//StdOut.println("isSolvable called");
    	return isSolvable;
    }
    
    public int moves()
    {
    	//StdOut.println("Solver moves called");
        return goalStateNode.moves;
    }
    
    public Iterable<Board> solution()
    {    	
    	//StdOut.println("Solver iterable solution called");
    	
    	/*
    	 *  Create a stack of board states that lead to the goal state. 
    	 *  The solution is all of the board states that the initial 
    	 *  board goes through to get to the goal state
    	 *  
    	 *  initial -> move one tile -> move one tile -> .... -> goal state
    	 */
    	Stack<Board> solution = new Stack<Board>();
    	
    	/*
    	 * Start at the goal board and push onto the stack 
    	 * each of the previousNode nodes leading up to the 
    	 * goal board. 
    	 */
        for (Node sn = goalStateNode; sn != null; sn = sn.previousNode) 
        {
            solution.push(sn.board);
        }
        
        // Return the stack of boards so they can printed from initial to goal 
        return solution;        
    }
    
    public static void main(String [] args){
    	System.out.printf("Solver start\n");
    	int N = StdIn.readInt();
        int [][] tiles = new int [N][N];      
        
        // Create the 2d array from the values stored on file
        for (int i = 0; i < N; i++){
            for (int j = 0; j < N; j++){
                tiles[i][j] = StdIn.readInt();                
            }            
        }
        
        // Create the initial board
        Board initial = new Board(tiles);
        
        // Solve the initial board
        Solver solver = new Solver(initial);
        
        /*
         *  Loop through the boards in the stack and print
         *  each board configuration. This will show which tile was moved
         *  at each solution state from the initial board to the 
         *  goal board. 
         */        
        for (Board board : solver.solution()){
            System.out.println(board);
        }
        
        if(!solver.isSolvable())
            System.out.println("No solution possible");
        else
            System.out.println("Mininimum number of moves = " + solver.moves());
    }
}
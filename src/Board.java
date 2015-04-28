/*
 * Board.java
 * 
 * The board class contains an 2d array of integers that represent
 * the tiles in a (*)Puzzle game. There are N - 1 tiles and the empty space is 
 * denoted by the value 0. Board takes in a 2D array of integers and populates
 * its array, makes a copy of the parameter array. It also has function that manipulate the
 * board like swapping tiles or applying operators.
 * 
 * @author David Morton K00179391
 * 
 * Artificial Intelligence Programming Assignment 
 * 8 Puzzle Problem
 * 
 */
public class Board
{
	
	private int N; // N x N tiles
	private int[][] boardTiles; // tiles on the board
	
	/*
	 * Create a board containing N x N tiles and 
	 * populate it with the values stored in inputTiles.
	 * Create a copy of the parameter board.
	 * 
	 * @param int [][] inputTiles is the input board from file
	 */
    public Board(int [][] inputTiles)
    {
        // YOUR CODE HERE
    	N = inputTiles.length; // N x N tiles
    	boardTiles = new int[N][N]; // 2d array of tiles
        
        // create the N x N board of tiles
        for (int i = 0; i < N; i++) 
        {
            for (int j = 0; j < N; j++) 
            {
            	boardTiles[i][j] = inputTiles[i][j];
                //System.out.printf("%d", boardTiles[i][j]);
            }
            //System.out.printf("\n");
        }           
    }
    
    /*
     * How many tiles are not in the correct position
     * in this board state?. Loop through the board and calculate
     * the number of tiles that are not in their goal states.
     * 
     * @return int hamming heuristic for this board
     */
    public int hamming()
    {
    	// Number of tiles that are not in their correct positions
    	int inCorrectTilePositions = 0;

    	/* 
    	 * Loop through the puzzle and check each tiles position
    	 * against its correct position 
    	 */
		for (int i = 0; i < N; i++) 
		{
			for (int j = 0; j < N; j++) 
			{
				// check if the tile is in the correct position
				if (boardTiles[i][j] != correctTileAt(i, j)) 
				{
					// increment the counter
					inCorrectTilePositions++;					
				}
			}
		}
		// subtract 1 for the blank square
		return inCorrectTilePositions - 1;
    }
    
    /*
     * In the 2d array, the position (i, j) should contain
     * the value i * N + j + 1
     * e.g 	
     * 		for position (0, 0) is 1
     * 		0 * 3 + 0 + 1 = 1
     * 
     * 		for position (2, 1) = 8
     * 		2 * 3 + 1 + 1 = 8
     * 
     * @param int i row position of tile in array
     * @param int j column position of tile in array
     * 
     * @return int the goal position of the tile at the position (i, j)
     */
    private int correctTileAt(int i, int j) 
    {    	
        return i * N + j + 1;
    }
    
    /*
     * Calculate the Manhattan distance heuristic for this board.
     * Sum the distance for each tile to its goal tile and use this
     * to choose from the priority queue.
     * 
     * @return int sum of the Manhattan distances for this board
     */
    public int manhattan()
    {    	  	
		int sum = 0;
		
		// where the stored value should be in the board
		int correctRow;
		int correctColumm;
		
		// For each tile in this board
		for (int i = 0; i < N; i++)
		{
		    for (int j = 0; j < N; j++) 
		    {		    			    	
		        int tileNumber = boardTiles[i][j];
		        if (tileNumber == 0) 
		        {
		        	// skip the blank square
		        	continue;
		        }
		
		        /*
		         * Use the value stored in the array to caluclate
		         * where it should be in the board
		         * 
		         * e.g 
		         * 		value = 8
		         * 		N = 3
		         * 		
		         * 		row = 8 - 1 / 3
		         * 		row = 7 / 3
		         * 		row = 2 (integer division)
		         * 
		         * 		column = 8 - 1 % 3
		         * 		column = 7 % 3
		         * 		column = 1 
		         * 
		         * 		tile 8 should be in position (2, 1)
		         */
		        correctRow = (tileNumber - 1) / N;
		        correctColumm = (tileNumber - 1) % N;
		        
		        /*
		         *  Calculate the Manhattan distance from the tile at 
		         *  (i, j) to its goal position (correctRow, goalCol). Use the 
		         *  absolute values to get distance.
		         *  
		         *  e.g tile number 8
		         *  	tile position (0, 0)
		         *  	goal position for tile (2, 1)
		         *  
		         *  	manhattan = (0 - 2) + (0 - 1)
		         *  	manhattan = -3
		         *  	manhattan = absoluteValue(-3) = 3
		         *  
		         *  	if negative change to positive, if positive, do nothing.		
		         */		        
		        sum += Math.abs(i - correctRow) + Math.abs(j - correctColumm);		        
		    }
	    }
		return sum;        
    }
    
    /* Check to see if this board is the goal board. Iterate the 2D
     * array and check all tiles except the empty one.
     * 
     * @return boolean true if this is the goal board
     */
    public boolean isGoal() 
    {
    	//StdOut.println("Checking if board is in the goal state...");
    	    	
        for (int i = 0; i < N; i++) 
        {
            for (int j = 0; j < N; j++) 
            {
                
            	/*
            	 * Check that all of the tiles in the board are in their correct positions, 
            	 * except the empty tile that has a value of 0. Not checking the bottom right
            	 * tile avoids the calculation error to find the empty tiles correct position
            	 * 
            	 * e.g	N = 3 
            	 * 		find goal position of (2, 2) this should be the empty square 
            	 * 		with value 0 in the bottom right of the goal state
            	 * 
            	 *  	equation : i * N + j + 1
            	 *  
            	 *  	2 * 3 + 2 + 1 = 9
            	 *  
            	 *  	The value at position 2,2 should be 0 not 9 in 
            	 *  	the final goal state
            	 */
                if ((boardTiles[i][j] != correctTileAt(i, j)) && ((i != N - 1) || (j != N - 1))) 
                {
                    
                	/*
                	 * There is at least 1 tile wrong so the board is not in
                	 * the goal state 
                	 */
                    return false;
                }
            }
        }
        // This board is complete! quick before the twin gets solved!......
        return true;
    }
    
    /*
     * Compare this object with the board Object by checking
     * common member variables. (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object board)
    {
        // YOUR CODE HERE
    	//StdOut.println("Checking if this board is equal to the parameter board..");
    	
    	/*
    	 * Cast the object to a board data type and check its tiles
    	 * against this board to see if they are identical
    	 */
    	Board that = (Board) board;
    	for (int i = 0; i < N; i++) 
    	{
    		for (int j = 0; j < N; j++) 
        	{
        		if (this.boardTiles[i] != that.boardTiles[j]) 
        		{
        			return false;
        		}
        	}    		
    	}
    	
    	// If all tiles in this board are the same as the parameter board, then they are equal
    	return true;        
    }
    
    public Iterable<Board> neighbours(){
        // YOUR CODE HERE
    	for (int i = 0; i < N; i++) 
    	{
            for (int j = 0; j < N; j++) 
            {
            	
            	/*
            	 * Walk through each tile in the board and if we hit
            	 * the 0 tile we can move tiles into this space.
            	 */
                if (boardTiles[i][j] == 0) 
                {
                	//StdOut.println("Creating queue of boards that have moved one tile into the empty tile space...");
                	
                	// Queue of boards that are derived from this board
                	Queue<Board> q = new Queue<Board>();

                	/*
                	 * From this board use an operator to move a tile and create 
                	 * a new board. Each new board is added to a queue and returned to the 
                	 * solver. 
                	 * 
                	 * The operators are the available moves for this board, 
                	 * Up, Down, Left, Right.
                	 * 
                	 * Depending on where the empty tile is on the board, certain
                	 * operators will be unavailable. 
                	 * 
                	 * e.g. if the empty tile is in the top left corner of the puzzle, only
                	 * the Up and Left operators can be used on tiles adjacent to the empty tile.
                	 * If the empty tile is bottom right only the Down and Right Operators can be 
                	 * used on adjacent tiles.
                	 *  
                	 */
                	
                	// Move tile down in to empty space
                    if (i > 0) 
                    {
                    	//StdOut.println("Operator Down");
                        q.enqueue(new Board(swap(i, j, i - 1, j)));
                    }
                    
                    // Move tile up into empty space
                    if (i < N - 1) 
                    {
                    	//StdOut.println("Operator Up");
                        q.enqueue(new Board(swap(i, j, i + 1, j)));
                    }
                    
                    // Move tile right into empty space
                    if (j > 0) 
                    {
                    	//StdOut.println("Operator Right");
                        q.enqueue(new Board(swap(i, j, i, j - 1)));
                    }
                    
                    // Move tile left into empty space
                    if (j < N - 1) 
                    {
                    	//StdOut.println("Operator Left");
                        q.enqueue(new Board(swap(i, j, i, j + 1)));
                    }
                    //StdOut.println("Returning queue of neightbour boards");
                    return q;                    
                }
            }
        }
    	//StdOut.println("There are no available moves for this board");
        return null;
    }
    
    /*
     * Create a twin of this board by swapping the first two adjacent tiles
     * possible and returning that board. The twin is used to find out if the initial board
     * is solvable. If the twin board at any stage is solvable then the initial board is not solvable. 
     * 
     * e.g 
     * 		Initial board
     * 		3
     *		0  1  3 
     *		4  2  5 
     *		7  8  6
     *
     *      Twin board
     * 		3
     *		0  3  1 
     *		4  2  5 
     *		7  8  6
     *
     *	The values 3 and 1 are swapped in the twin board, the value 0 is 
     *	ignored.
     *
     *	If the twin board that is created from each board being processed is solved, 
     *	it means that the initial board is unsolvable because there is no way to swap 
     *	tiles in the puzzle without removing the, thus if the twin is solvable it means the 
     *	initial boards is in a state that cannot be solved.
     */
    public Board twin() 
    {
    	//StdOut.println("Creating twin of board\n" + this);
    	
    	// Loop through this board and populate the twin
    	int N = boardTiles.length;
        int[][] twinBoardTiles = new int[N][N];
        for (int i = 0; i < N; i++) 
    	{
    		for (int j = 0; j < N; j++) 
        	{
    			twinBoardTiles[i][j] = boardTiles[i][j];
        	}
        }
                
        // Error checking the size of the board
        if (N <= 1) 
        {
            return new Board(twinBoardTiles);
        }
       
        /*
         * Loop through this boards tile and swap
         * the first two tiles that are not the empty
         * tile space
         */
        for (int i = 0; i < N; i++) 
        {
            int lastVal = boardTiles[i][0];

            for (int j = 1; j < N; j++) 
            {
                int currentVal = boardTiles[i][j];

                // If currentVal nor lastVal is the empty space
                if (currentVal != 0 && lastVal != 0) 
                {
                	
                	// Swap adjacent tiles
                	twinBoardTiles[i][j] = lastVal;
                	twinBoardTiles[i][j - 1] = currentVal;
                	//StdOut.println("Swapping tiles " + lastVal + " with " + currentVal);
                	
                	// Create the twin board from the new tile set
                    Board twinBoard = new Board(twinBoardTiles);
                    //StdOut.println("TWIN\n" + twinBoard);
                    return twinBoard;
                }
                
                /*
                 *  Move to the next adjacent tile because one of the 
                 *  previous two were empty tiles 
                 */                
                lastVal = currentVal;
            }
        }        
    	return null;
    }    
    
    /*
     * Given an origin and destination index in a 2D array of two tiles, swap
     * them and return the updated tile set. 
     * 
     * @param int originRow row index of value
     * @param int originColumn column index of value 
     * @param int destRow row index of destination position
     * @param int destColumn column index of destination position
     * 
     * @return int[][] copy is the updated 2D array of tile values
     */
    private int[][] swap(int originRow, int originColumn, int destRow, int destColumn) 
    {
    	
    	// Create a temporary tile set and populate it
    	int N = boardTiles.length;
        int[][] copy = new int[N][N];
        for (int i = 0; i < N; i++) 
    	{
    		for (int j = 0; j < N; j++) 
        	{
    			copy[i][j] = boardTiles[i][j];
        	}
        }
                
        // Store the value at the target position
        int tempValue = copy[destRow][destColumn];
        
        // Store the value to the target position
        copy[destRow][destColumn] = copy[originRow][originColumn];
        
        // Store the old target position value in its new position
        copy[originRow][originColumn] = tempValue;
        
        // Return the 2D array tile set with the swapped values.
        return copy;
    }
    
    /*
     * Format the board to s string so it be printed to StdOut(non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        // YOUR CODE HERE    	
    	StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) 
        {
            for (int j = 0; j < N; j++) 
            {
                s.append(String.format("%2d ", boardTiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();       
    }
}
        
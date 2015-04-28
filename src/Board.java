import java.util.Arrays;

public class Board{
	
	private int N; // N x N tiles
	private int[][] boardTiles; // tiles on the board
	
	/*
	 * Create a board containing N x N tiles and 
	 * populate it with the values stored in inputTiles.
	 * Create a copy of the parameter board.
	 * 
	 * @param int [][] inputTiles is the input board from file
	 * 
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
     * in this board state. Loop through the board and calculate
     * the number of tiles that are not in their goal states.
     * 
     * @return int hamming heuristic for this board
     */
    public int hamming()
    {    	
		int wrongPosCounter = 0;

		for (int i = 0; i < N; i++) 
		{
			for (int j = 0; j < N; j++) 
			{
				// check if the tile is in the correct position
				if (boardTiles[i][j] != correctTileAt(i, j)) 
				{
					// increment the counter
					wrongPosCounter++;					
				}
			}
		}
		// subtract 1 for the blank square
		return wrongPosCounter - 1;
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
		int goalRow, goalCol;
		
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
		        goalRow = (tileNumber - 1) / N;
		        goalCol = (tileNumber - 1) % N;
		        
		        /*
		         *  Calculate the Manhattan distance from the tile at 
		         *  (i, j) to its goal position (goalRow, goalCol). Use the 
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
		        sum += Math.abs(i - goalRow) + Math.abs(j - goalCol);		        
		    }
	    }
		return sum;        
    }
    
    /**
     * @return is this board the goal board?
     */
    public boolean isGoal() {
    	StdOut.println("Checking if board is in the goal state...");
    	    	
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                
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
                if ((boardTiles[i][j] != correctTileAt(i, j)) && ((i != N - 1) || (j != N - 1))) {
                    
                	/*
                	 * There is at least 1 tile wrong so the board is not in
                	 * the goal state 
                	 */
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean equals( Object o)
    {
        // YOUR CODE HERE
    	if (o == this) 
    	{
    		return true;
    	}

    	if (o == null) {
    		return false;
    	}

    	if (o.getClass() != this.getClass()) {
    		return false;
    	}

    	Board that = (Board) o;
    	if (this.N != that.N) 
    	{
    		return false;
    	}

    	for (int i = 0; i < N; i++) 
    	{
    		if (!Arrays.equals(this.boardTiles[i], that.boardTiles[i])) 
    		{
    			return false;
    		}
    	}
    	return true;        
    }
    
    public Iterable<Board> neighbours(){
        // YOUR CODE HERE
    	for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
            	
            	/*
            	 * Walk through each tile in the board and if we hit
            	 * the 0 tile we can move tiles into this space.
            	 */
                if (boardTiles[i][j] == 0) {
                	StdOut.println("Creating queue of boards that have moved one tile into the empty tile space...");
                	
                	Queue<Board> q = new Queue<Board>();

                	/*
                	 * 
                	 */
                    if (i > 0) {
                    	StdOut.println("Operator Down");
                        q.enqueue(new Board(swap(i, j, i - 1, j)));
                    }

                    if (i < N - 1) {
                    	StdOut.println("Operator Up");
                        q.enqueue(new Board(swap(i, j, i + 1, j)));
                    }

                    if (j > 0) {
                    	StdOut.println("Operator Right");
                        q.enqueue(new Board(swap(i, j, i, j - 1)));
                    }

                    if (j < N - 1) {
                    	StdOut.println("Operator Left");
                        q.enqueue(new Board(swap(i, j, i, j + 1)));
                    }
                    StdOut.println("Returning queue of neightbour boards");
                    return q;                    
                }
            }
        }
    	StdOut.println("Neighbours has returned null");
        return null;
    }
    
    /*
     * Create a twin of this board by swapping the first two adjacent tiles
     * possible and returning that board. 
     * 
     * e.g 
     * 		
     */
    public Board twin() {
    	StdOut.println("Creating twin of board\n" + this);
    	int N = boardTiles.length;
        int[][] copy = new int[N][];
        for (int i = 0; i < N; i++) {
            copy[i] = Arrays.copyOf(boardTiles[i], N);
        }
                
        // if N is either 0 or 1
        if (N <= 1) {
            return new Board(copy);
        }

        for (int i = 0; i < N; i++) {
            int lastVal = boardTiles[i][0];

            for (int j = 1; j < N; j++) {
                int currentVal = boardTiles[i][j];

                // if neither lastVal nor val is the blank square
                if (currentVal != 0 && lastVal != 0) {
                    copy[i][j] = lastVal;
                    copy[i][j - 1] = currentVal;
                    
                    Board twinBoard = new Board(copy);
                    StdOut.println("TWIN\n" + twinBoard);
                    return twinBoard;
                }

                lastVal = currentVal;
            }
        }
        
    	return null;
    }    
    
    /*
     * 
     */
    private int[][] swap(int fromRow, int fromCol, int toRow, int toCol) {
    	
    	int N = boardTiles.length;
        int[][] copy = new int[N][];
        for (int i = 0; i < N; i++) {
            copy[i] = Arrays.copyOf(boardTiles[i], N);
        }
        
        
        int tmp = copy[toRow][toCol];
        copy[toRow][toCol] = copy[fromRow][fromCol];
        copy[fromRow][fromCol] = tmp;
        return copy;
    }
    
    public String toString(){
        // YOUR CODE HERE    	
    	StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", boardTiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();       
    }
}
        
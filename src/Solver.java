public class Solver{
    public Solver(Board initial){
        StdOut.println("Solver constructor called");    	
    }
    
    public boolean isSolvable(){
    	StdOut.println("isSolvable called");
        return false;
    }
    
    public int moves(){
    	StdOut.println("Solver moves called");
        return 0;
    }
    
    public Iterable<Board> solution(){
    	StdOut.println("Solver iterable solution called");
        return null;
    }
    
    public static void main(String [] args){
        int N = StdIn.readInt();
        int [][] tiles = new int [N][N];
        
        for (int i = 0; i < N; i++){
            for (int j = 0; j < N; j++){
                tiles[i][j] = StdIn.readInt();
            }
        }
        
        Board initial = new Board(tiles);
        
        Solver solver = new Solver(initial);
        
        for (Board board : solver.solution()){
            System.out.println(board);
        }
        
        if(!solver.isSolvable())
            System.out.println("No solution possible");
        else
            System.out.println("Mininimum number of moves = " + solver.moves());
    }
}
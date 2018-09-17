import java.io.*;

/**
 * This is the Gameboard class.  It implements a two dimension array that
 * represents a connect four gameboard. It keeps track of the player making
 * the next play based on the number of pieces on the game board. It provides
 * all of the methods needed to implement the playing of a max connect four
 * game.
 * 
 * @author James Spargo
 *
 */

public class GameBoard 
{
    // class fields
    private int[][] playBoard;
    private int pieceCount;
    private int currentTurn;
    private maxconnect4.PLAYER_TYPE f_turn;

    /**
     * This constructor creates a GameBoard object based on the input file
     * given as an argument. It reads data from the input file and provides
     * lines that, when uncommented, will display exactly what has been read
     * in from the input file.  You can find these lines by looking for 
     * 
     * @param inputFile the path of the input file for the game
     */
    public GameBoard( String inputFile ) 
    {
	this.playBoard = new int[6][7];
	this.pieceCount = 0;
	int counter = 0;
	BufferedReader input = null;
	String gameData = null;

	// open the input file
	try 
	{
	    input = new BufferedReader( new FileReader( inputFile ) );
	} 
        catch( IOException e ) 
	{
	    System.out.println("\nProblem opening the input file!\nTry again." +
			       "\n");
	    e.printStackTrace();
	}

	//read the game data from the input file
	for(int i = 0; i < 6; i++) 
	{
	    try 
	    {
		gameData = input.readLine();

		for( int j = 0; j < 7; j++ ) 
		{
		    this.playBoard[ i ][ j ] = gameData.charAt( counter++ ) - 48;
		    
		    if( !( ( this.playBoard[ i ][ j ] == 0 ) ||
			   ( this.playBoard[ i ][ j ] == 1 ) ||
			   ( this.playBoard[ i ][ j ] == 2 ) ) ) 
                    {
			System.out.println("\nProblems!\n--The piece read " +
					   "from the input file was not a 1, a 2 or a 0" );
			this.exit_function( 0 );
		    }
		    
		    if( this.playBoard[ i ][ j ] > 0 )
		    {
			this.pieceCount++;
		    }
		}
	    } 
	    catch( Exception e ) 
	    {
		System.out.println("\nProblem reading the input file!\n" +
				   "Try again.\n");
		e.printStackTrace();
		this.exit_function( 0 );
	    }

	    //reset the counter
	    counter = 0;
	    
	} // end for loop

	try 
        {
	    gameData = input.readLine();
	} 
	catch( Exception e ) 
	{
	    System.out.println("\nProblem reading the next turn!\n" +
			       "--Try again.\n");
	    e.printStackTrace();
	}

	this.currentTurn = gameData.charAt( 0 ) - 48;
	
	if(!( ( this.currentTurn == 1) || ( this.currentTurn == 2 ) ) ) 
	{
	    System.out.println("Problems!\n the current turn read is not a " +
			       "1 or a 2!");
	    this.exit_function( 0 );
	} 
	else if ( this.getCurrentTurn() != this.currentTurn ) 
	{
	    System.out.println("Problems!\n the current turn read does not " +
			       "correspond to the number of pieces played!");
	    this.exit_function( 0 );			
	}
    } // end GameBoard( String )

	
    /**
     * This constructor creates a GameBoard object from another double
     * indexed array.
     */
    public GameBoard( int masterGame[][] ) 
    {
	
	this.playBoard = new int[6][7];
	this.pieceCount = 0;

	for( int i = 0; i < 6; i++ ) 
	{
	    for( int j = 0; j < 7; j++) 
	    {
		this.playBoard[ i ][ j ] = masterGame[ i ][ j ];
		
		if( this.playBoard[i][j] > 0 )
		{
		    this.pieceCount++;
		}
	    }
	}
    } 

    /**
     * Player Score check:
     * it checks horizontally, vertically, and each direction diagonally.
     * currently, it uses for loops.
     * 
     */
    public int getScore( int player ) 
    {
	//reset the scores
	int playerScore = 0;
	//check horizontally
	for( int i = 0; i < 6; i++ ) 
        {
	    for( int j = 0; j < 4; j++ ) 
	    {
		if( ( this.playBoard[ i ][j] == player) &&
		    ( this.playBoard[ i ][ j+1 ] == player) &&
		    ( this.playBoard[ i ][ j+2 ] == player) &&
		    ( this.playBoard[ i ][ j+3 ] == player) ) 
		{
		    playerScore++;
		   
		}
	    }
	}

	//check vertically
	for( int i = 0; i < 3; i++ ) {
	    for( int j = 0; j < 7; j++ ) {
		if( ( this.playBoard[ i ][ j ] == player) &&
		    ( this.playBoard[ i+1 ][ j ] == player) &&
		    ( this.playBoard[ i+2 ][ j ] == player) &&
		    ( this.playBoard[ i+3 ][ j ] == player) ) {
		    playerScore++;
		  
		}
	    }
	} 
	
	//check diagonally
	    for( int i = 0; i < 3; i++ ){
		for( int j = 0; j < 4; j++ ) {
		    if( ( this.playBoard[ i ][ j ] == player) &&
			( this.playBoard[ i+1 ][ j+1 ] == player ) &&
			( this.playBoard[ i+2 ][ j+2 ] == player ) &&
			( this.playBoard[ i+3 ][ j+3 ] == player ) ) {
			playerScore++;
		    }
		}
	    }
	    
	    //check diagonally - forward
	    for( int i = 0; i < 3; i++ ){
		for( int j = 0; j < 4; j++ ) {
		    if( ( this.playBoard[ i+3 ][ j ] == player ) &&
			( this.playBoard[ i+2 ][ j+1 ] == player ) &&
			( this.playBoard[ i+1 ][ j+2 ] == player ) &&
			( this.playBoard[ i ][ j+3 ] == player ) ) {
			playerScore++;
		    }
		}
	    }// end player score check
	    
	    return playerScore;
    } // end getScore

    /**
     * the method gets the current turn
     * @return an int value representing whose turn it is.  either a 1 or a 2
     */
    public int getCurrentTurn() 
    {
	return ( this.pieceCount % 2 ) + 1 ;
    } // end getCurrentTurn


    public int getPieceCount() 
    {
	return this.pieceCount;
    }


    public int[][] getGameBoard() 
    {
	return this.playBoard;
    }
    
    boolean IsBoardFull() {
        return (this.getPieceCount() >= 42);
    }

    
    public boolean isValidPlay( int column ) {
	
	if ( !( column >= 0 && column <= 7 ) ) {
	    // check the column bounds
	    return false;
	} else if( this.playBoard[0][ column ] > 0 ) {
	    // check if column is full
	    return false;
	} else {
	    // column is NOT full and the column is within bounds
	    return true;
	}
    }

    /**
     * This method plays a piece on the game board.
     */
    public boolean playPiece( int column ) {
		if( !this.isValidPlay( column ) ) {
	    return false;
	} else {

	    for( int i = 5; i >= 0; i-- ) {
		if( this.playBoard[i][column] == 0 ) {
		    if( this.pieceCount % 2 == 0 ){
			this.playBoard[i][column] = 1;
			this.pieceCount++;
			
		    } else { 
			this.playBoard[i][column] = 2;
			this.pieceCount++;
		    }
		    
		    return true;
		}
	    }
	    //the pgm shouldn't get here
	    System.out.println("Something went wrong with playPiece()");
	    
	    return false;
	}
    } //end playPiece
    
    public void removePiece( int column ) {
		for( int i = 0; i < 6; i++ ) {
	    if( this.playBoard[ i ][ column ] > 0 ) {
		this.playBoard[ i ][ column ] = 0;
		this.pieceCount--;
		
		break;
	    }
	}
	
    } // end remove piece	
    
    
    public void printGameBoard() 
    {
	System.out.println(" -----------------");
	for( int i = 0; i < 6; i++ ) 
        {
	    System.out.print(" | ");
	    for( int j = 0; j < 7; j++ ) 
            {      System.out.print( this.playBoard[i][j] + " " );   }
	    System.out.println("| ");
	}
	System.out.println(" -----------------");
    } // end printGameBoard
    
    /**
     * this method prints the GameBoard to an output file to be used
     */
    public void printGameBoardToFile( String outputFile ) {
	try {
	    BufferedWriter output = new BufferedWriter(
						       new FileWriter( outputFile ) );
	    
	    for( int i = 0; i < 6; i++ ) {
		for( int j = 0; j < 7; j++ ) {
		    output.write( this.playBoard[i][j] + 48 );
		}
		output.write("\r\n");
	    }
	    output.write( this.getCurrentTurn() + "\r\n");
	    output.close();
	    
	} catch( IOException e ) {
	    System.out.println("\nProblem writing to the output file!\n" +
			       "Try again.");
	    e.printStackTrace();
	}
    } 
    
    private void exit_function( int value ){
	System.out.println("exiting from GameBoard.java!\n\n");
	System.exit( value );
    }
    
    public void setFirstTurn(maxconnect4.PLAYER_TYPE turn) {
        f_turn = turn;
        setPiece();
    }
    
    public void setPiece() {
        if ((this.currentTurn == 1 && f_turn == maxconnect4.PLAYER_TYPE.computer)
            || (this.currentTurn == 2 && f_turn == maxconnect4.PLAYER_TYPE.human)) {
            maxconnect4.computer_piece = 1;
            maxconnect4.human_piece = 2;
        } else {
            maxconnect4.human_piece = 1;
            maxconnect4.computer_piece = 2;
        }
        
        System.out.println("Human plays as : " + maxconnect4.human_piece + " , Computer plays as : " + maxconnect4.computer_piece);
        
    }
    
}  
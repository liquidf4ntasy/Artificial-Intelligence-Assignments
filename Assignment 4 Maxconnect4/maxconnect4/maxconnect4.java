import java.io.*;
import java.util.Scanner;

/**
 *   Additions/ Modifications by: Satyajit Deshmukh
 *   UTA ID: 1001417727
 *   CSE 5360 Artificial Intelligence-1
 *   
 * @author James Spargo
 * This class controls the game play for the Max Connect-Four game. 
 * To compile the program, use the following command from the maxConnectFour directory:
 * javac *.java
 *
 * the usage to run the program is as follows:
 * ( again, from the maxConnectFour directory )
 *
 *  -- for interactive mode:
 * java MaxConnectFour interactive [ input_file ] [ computer-next / human-next ] [ search depth]
 *
 * -- for one move mode
 * java maxConnectFour.MaxConnectFour one-move [ input_file ] [ output_file ] [ search depth]
 * 
 * description of arguments: 
 *  [ input_file ]
 *  -- the path and filename of the input file for the game
 *  
 *  [ computer-next / human-next ]
 *  -- the entity to make the next move. either computer or human. can be abbreviated to either C or H. This is only used in interactive mode
 *  
 *  [ output_file ]
 *  -- the path and filename of the output file for the game.  this is only used in one-move mode
 *  
 *  [ search depth ]
 *  -- the depth of the minimax search algorithm
 *
 *   
 */

public class maxconnect4
{
	 public static GameBoard currentGame = null;
	 public static AiPlayer calculon = null;
	 public static Scanner userinput = null;
	  public static int computer_piece;
	  public static int human_piece;
	  public static final String computer_output_file="computer.txt";
	  public static final String human_output_file="human.txt";
	  
	  public enum PLAYER_TYPE {
	        human,
	        computer
	    };
  public static void main(String[] args) 
  {
    // check for the correct number of arguments
    if( args.length != 4 ) 
    {
      System.out.println("Four command-line arguments are needed:\n"
                         + "Usage: java [program name] interactive [input_file] [computer-next / human-next] [depth]\n"
                         + " or:  java [program name] one-move [input_file] [output_file] [depth]\n");

      exit_function( 0 );
     }
		
    // parse the input arguments
    String game_mode = args[0].toString();				// the game mode
    String input = args[1].toString();					// the input game file
    int depthLevel = Integer.parseInt( args[3] );  		// the depth level
		
    // create and initialize the game board
   currentGame = new GameBoard( input );
    
    // create the Ai Player
     calculon = new AiPlayer(depthLevel,currentGame);
		
    int playColumn = 99;				//  the players choice of column to play
    boolean playMade = false;			//  set to true once a play has been made

    if( game_mode.equalsIgnoreCase( "interactive" ) ) 
    {
        
        if (args[2].toString().equalsIgnoreCase("computer-next") || args[2].toString().equalsIgnoreCase("C")) {
            // if it is computer next, make the computer make a move
            currentGame.setFirstTurn(PLAYER_TYPE.computer);
            ComplayInteractive();
        } else if (args[2].toString().equalsIgnoreCase("human-next") || args[2].toString().equalsIgnoreCase("H")){
            currentGame.setFirstTurn(PLAYER_TYPE.human);
            HumanPlay();
        } else {
            System.out.println("\n" + "value for 'next turn' not recognized.  \n try again. \n");
            exit_function(0);
        }

        if (currentGame.IsBoardFull()) {
            System.out.println("\nI can't play.\nThe Board is Full\n\nGame Over.");
            exit_function(0);
        }

    } else if (!game_mode.equalsIgnoreCase("one-move")) {
        System.out.println("\n" + game_mode + " is an unrecognized game mode \n try again. \n");
        exit_function(0);
    }else if(!game_mode.equalsIgnoreCase( "one-move" ) ) 
    {
      System.out.println( "\n" + game_mode + " is an unrecognized game mode \n try again. \n" );
      return;
    }
    else{
    	String output = args[2].toString();
    	Complayonemove(output);
    	
    }
    return;
    
} // end of main()
    
    private static void HumanPlay() {
        printCurrentBoardAndScore();

        System.out.println("\n Human's turn:\nKindly play your move here(1-7):");

        userinput = new Scanner(System.in);

        int playColumn = 99;

        do {
            playColumn = userinput.nextInt();
        } while (!isValidPlay(playColumn));

        // play the piece
        currentGame.playPiece(playColumn - 1);

        System.out.println("move: " + currentGame.getPieceCount() + " , Player: Human , Column: " + playColumn);
        
        currentGame.printGameBoardToFile(human_output_file);

        if (currentGame.IsBoardFull()) {
            printCurrentBoardAndScore();
            printGameResult();
        } else {
            ComplayInteractive();
        }
	
}

	private static void ComplayInteractive() {
		   printCurrentBoardAndScore();

	        System.out.println("\n Computer's turn:\n");

	        int playColumn = 99; // the players choice of column to play

	        if(currentGame.getPieceCount()<42||playColumn!=99){
	    	  playColumn = calculon.findBestPlay(currentGame);

	        // play the piece
	        currentGame.playPiece(playColumn);

	        System.out.println("move: " + currentGame.getPieceCount() + " , Player: Computer , Column: " + (playColumn + 1));

	        currentGame.printGameBoardToFile(computer_output_file);

	        if (currentGame.IsBoardFull()) {
	            printCurrentBoardAndScore();
	            printGameResult();
	        } else {
	            HumanPlay();
	        }
	}else{
		System.out.println("\nI can't play.\nThe Board is Full.\n\nGame Over.");
		
	}
	
}

	private static void printGameResult() {
	    int human_score = currentGame.getScore(maxconnect4.human_piece);
        int comp_score = currentGame.getScore(maxconnect4.computer_piece);
        
        System.out.println("\n Final Result:");
        if(human_score > comp_score){
            System.out.println("\n Congratulations! You won this game."); 
        } else if (human_score < comp_score) {
            System.out.println("\n You lost! Good luck for next game.");
        } else {
            System.out.println("\n Game is tie!!");
        }
		
	}

	public static void printCurrentBoardAndScore() {
		 System.out.print("Game state :\n");

	        // print the current game board
	        currentGame.printGameBoard();

	        // print the current scores
	        System.out.println("Score: Player-1 = " + currentGame.getScore(1) + ", Player-2 = " + currentGame.getScore(2)
	            + "\n ");
		
	}
	 private static boolean isValidPlay(int playColumn) {
	        if (currentGame.isValidPlay(playColumn - 1)) {
	            return true;
	        }
	        System.out.println("Opps!!...Invalid column , Kindly enter column value between 1 to 7.");
	        return false;
	    }

	private static void Complayonemove(String outputFile){
    /////////////   one-move mode ///////////
    		
    int playColumn;
    System.out.print("\nMaxConnect-4 game\n");
    System.out.print("game state before move:\n");
    
    //print the current game board
    currentGame.printGameBoard();
    // print the current scores
    System.out.println( "Score: Player 1 = " + currentGame.getScore( 1 ) +
			", Player2 = " + currentGame.getScore( 2 ) + "\n " );
    
    // *computer play
    if( currentGame.getPieceCount() < 42 ) 
    {
        int current_player = currentGame.getCurrentTurn();
	// AI play - random play
	playColumn = calculon.findBestPlay( currentGame );
	
	// play the piece
	currentGame.playPiece( playColumn );
        	
        // display the current game board
        System.out.println("move " + currentGame.getPieceCount() 
                           + ": Player " + current_player
                           + ", column " + playColumn);
        System.out.print("game state after move:\n");
        currentGame.printGameBoard();
    
        // print the current scores
        System.out.println( "Score: Player 1 = " + currentGame.getScore( 1 ) +
                            ", Player2 = " + currentGame.getScore( 2 ) + "\n " );
        
        currentGame.printGameBoardToFile( outputFile );
    } 
    else 
    {
	System.out.println("\nI can't play.\nThe Board is Full\n\nGame Over");
    }			
    }

	
  private static void exit_function( int value )
  {
      System.out.println("Exiting from maxconnect4.java!\n\n");
      System.exit( value );
  }
}

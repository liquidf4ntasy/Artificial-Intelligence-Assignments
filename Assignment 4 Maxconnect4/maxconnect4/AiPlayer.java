import java.util.*;

/**
 * This is the AiPlayer class.  It simulates a minimax player for the maxconnect4 game
 * @author james spargo
 *
 */

public class AiPlayer 
{
	 public int depth;
	 public GameBoard mygameBoard;
    /**
     * The constructor essentially does nothing except instantiate an
     * AiPlayer object.
     *
     */
    public AiPlayer(int depth, GameBoard currentGame) 
    {
    	 this.depth = depth;
         this.mygameBoard = currentGame;
	
    }

    /**
     * This method plays a piece randomly on the board
     */
    
    public int findBestPlay(GameBoard currentGame) {
        int AIplay = 99;
        if (currentGame.getCurrentTurn() == 1) {
            int v = Integer.MAX_VALUE;
            for (int i = 0; i < 7; i++) {
                if (currentGame.isValidPlay(i)) {
                    GameBoard nextMoveBoard = new GameBoard(currentGame.getGameBoard());
                    nextMoveBoard.playPiece(i);
                    int value = Calculate_Max_Utility(nextMoveBoard, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);

                    if (v > value) {
                    	AIplay = i;
                        v = value;
                    }
                }
              }

        } else {
            int v = Integer.MIN_VALUE;
            for (int i = 0; i < 7; i++) {
                if (currentGame.isValidPlay(i)) {
                    GameBoard nextMoveBoard = new GameBoard(currentGame.getGameBoard());
                    nextMoveBoard.playPiece(i);
                    int value = Calculate_Min_Utility(nextMoveBoard, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);

                    if (v < value) {
                    	AIplay = i;
                        v = value;
                    }
                }
            }

        }
       
        return AIplay;
    }
    
    private int Calculate_Min_Utility(GameBoard gameBoard, int depth_level, int alpha_value, int beta_value) {
            if (gameBoard.IsBoardFull() || depth_level == 0) {
            	 return gameBoard.getScore(2) - gameBoard.getScore(1);
            }else{
                int v = Integer.MAX_VALUE;
                for (int i = 0; i < 7; i++) {
                    if (gameBoard.isValidPlay(i)) {
                        GameBoard board4NextMove = new GameBoard(gameBoard.getGameBoard());
                        board4NextMove.playPiece(i);
                        int value = Calculate_Max_Utility(board4NextMove, depth_level - 1, alpha_value, beta_value);
                        if (v > value) {
                            v = value;
                        }
                        if (v <= alpha_value) {
                            return v;
                        }
                        if (beta_value > v) {
                            beta_value = v;
                        }
                    }
                }
                return v;
            } 
        }
    private int Calculate_Max_Utility(GameBoard gameBoard, int depth_level, int alpha_value, int beta_value) {
            
    	if (gameBoard.IsBoardFull() || depth_level == 0) {
       	 return gameBoard.getScore(2) - gameBoard.getScore(1);
       } else{
    	   int v = Integer.MIN_VALUE;
                for (int i = 0; i < 7; i++) {
                    if (gameBoard.isValidPlay(i)) {
                        GameBoard board4NextMove = new GameBoard(gameBoard.getGameBoard());
                        board4NextMove.playPiece(i);
                        int value = Calculate_Min_Utility(board4NextMove, depth_level - 1, alpha_value, beta_value);
                        if (v < value) {
                            v = value;
                        }
                        if (v >= beta_value) {
                            return v;
                        }
                        if (alpha_value < v) {
                            alpha_value = v;
                        }
                    }
                }
                return v;
            } 
    }
}

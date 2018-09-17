Name: Satyajit Deshmukh
Id: 1001417727
-----------------------------

Assignment #4
-----------------------------
Task: Programming Assignment - Game Playing Algorithms - MaxConnect4
Course Name: Artificial Intelligence
Course Number: 5360
Programming Language: Java 


Extract the zip containing the code:
-----------------------------
Unzip the contents of the zip file.
Put the folder "maxconnect4" in Omega server.
use cd to go into the directory "maxconnect4".

How to Compile & Run :
-----------------------------
[sxd7727@omega maxconnect4]$ javac maxconnect4.java

-- for interactive mode:
 java maxConnect4 interactive [ input_file ] [ computer-next / human-next ] [ search depth]

-- for one move mode
java maxConnect4  one-move [ input_file ] [ output_file ] [ search depth]

example:
java maxconnect4 one-move input1.txt output.txt 1


Code Structure:
-----------------------------
Reads a txt file from the same directory only. (sample files included)
Please add the input file in txt format and in the same directory.

2D arrays are used to structure the gameboard, make a move & print the state of the game.
maxconnect4.java, AiPlayer.java and GameBoard.java are the 3 classes in the java code.

maxconnect4.java - This class controls the game play for the Max Connect-Four game. 
AiPlayer.java -    This is the AiPlayer class.  It simulates a minimax player for the maxconnect4 game. (depth limited minimax algorithm)
GameBoard.java -   It implements a two dimension array that represents a connect four gameboard. 
				   It keeps track of the player making the next play based on the number of pieces on the game board. 



Warning:
-----------------------------
Please do not re-format/edit sample txt files in notepad(windows only). Then you should be good to go!
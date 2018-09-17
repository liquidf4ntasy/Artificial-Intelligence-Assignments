Name: Satyajit Deshmukh
Id: 1001417727
-----------------------------

Assignment #1
-----------------------------
Task: Implement Uniform Cost Search (UCS)
Course Name: Artificial Intelligence
Course Number: 5360


Programming Language: Java 
-----------------------------

How to Run the Code:
-----------------------------
Unzip the contents of the zip file.
Put the folder "code1" in Omega server.
use cd to go into the directory.

How to Compile & Run :
-----------------------------
[sxd7727@omega Code1]$ javac Main.java
[sxd7727@omega Code1]$ java Main
 input_filename origin_city destination_city

Compile --- > javac Main.java
Run the Code --- > java Main
Enter 3 arguments in the format below
--- > <input_file.txt> <Source> <Destination>


Code Structure:
-----------------------------
Reads a txt file from the same directory only.
Please add the input file in txt format and in the same directory, also, the string "END OF INPUT" should be at the end of the file.
TreeMap and Hashset is used to traverse to store and traverse through the graph and nodes.
"Fringe" and "explored" are the Treemap and Hashset data structures used to implement Uniform Cost Search.
Gives infinity as output if connection does not exist between source and destination.
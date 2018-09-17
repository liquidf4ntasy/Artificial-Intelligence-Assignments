copy Folder decision_trees to omega.
cd to folder -> decision_trees

Copy Training and Testing file to omega /decision_trees/ folder.


Compile code
---------------------
javac dtree.java

Run the code
----------------------
>java dtree pendigits_training.txt pendigits_test.txt optimized 50

 Usage: java dtree <training_file> <test_file> <option> <pruning_thr>

example inputs:
java dtree pendigits_training.txt pendigits_test.txt randomized 50
java dtree pendigits_training.txt pendigits_test.txt forest3 50

-----------------------
3 text files included which has all the output of training and testing phase and accuracy.
randomized 50.txt
optimized 50.txt
forest3 50.txt
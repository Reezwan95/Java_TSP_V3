Version 3 of the program to solve the Traveling Salesman Problem using Java, that builds upon V2

This program asks the user to enter specific menu options to load a graph from a set of graphs, display the graph, run the nearest neighbor, nearest neighbor first-last and node insertion greedy algorithm.

The program also contains error handling for inccorectly loading graphs, non-existent files and results, in addition to the error handling in V2

Implemented using 8 (5 class files and 3 sub classes) class files:

-Main class file: contains the main function, the global buffered reader object and helper functions

-Node class file: gets and prints the node data to the console

-Graph class file: makes and prints the graph data to the console

-TSPSolver class file: runs one of the three nearest neighbor algorithm on the graphs, calculates the computation time required for each of them, the statistical summary (average, standard deviation, minimum and maximum) of the path cost, the overal performance of each algorithm and the winning algorithm

-NNSolver class file: subclass of the TSPSolver object class that runs the nearest neighbor algorithm

-NNFLSolver class file: subclass of the TSPSolver object class that runs the nearest neighbor first-last algorithm

-NISolver class file: subclass of the TSPSolver object class that runs the node insertion algorithm

-GraphLoader class file: loads the graph files from the command line using the global buffered reader file reader object


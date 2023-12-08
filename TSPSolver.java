import java.util.ArrayList;
import java.util.Arrays;

public abstract class TSPSolver {
	

	private static ArrayList<Graph> graph;
	private static ArrayList<int[]> solnPath;
	private static double[] solnCost;
	private static double[] compTime;
	private static boolean[] solnFound;
	private static boolean resultsExist;
	
	//constructors
	public TSPSolver() {
		
	}
	
	public TSPSolver(ArrayList<Graph> G) {
		solnPath = new ArrayList<>();
	    solnCost = new double[G.size()];
	    compTime = new double[G.size()];
	    solnFound = new boolean[G.size()];
	    Arrays.fill(solnCost, -1); //initialize with -1 to indicate no solution
	    Arrays.fill(compTime, -1); //initialize with -1 to indicate no computation
	    Arrays.fill(solnFound, false); //initialize with false to indicate no solution found
	    init(G);
	}
	
	//getters
	public int[] getSolnPath(int i) {
	    // Check if the index is within the bounds of the ArrayList
	    if (i >= 0 && i < this.solnPath.size()) {
	        // Return the solution path at the given index
	        return this.solnPath.get(i);
	    } else {
	        // Return null or an appropriate default value if the index is out of bounds
	        return null;
	    }
	}

	
	public double getSolnCost(int i) {
		//to check index validity
		if(i >= 0 && i < solnCost.length) {
			//returning soln cost for the index i
			return solnCost[i];
		} else {
			return -1;
		}
		
	}
	
	public double[] getSolnCosts() {
		return solnCost;
	}
	
	public double[] getCompTimes() {
		return compTime;
	}
	
	public double getCompTime(int i) {
		//to check index validity
		if(i >= 0 && i < compTime.length) {
			//returning computation time for index i
			return compTime[i];
		} else {
			return 0.0;
		}
		
	}
	public boolean getSolnFound(int i) {
		//to check index validity
		if(i >= 0 && i < solnFound.length) {
			//returning soln for index i
			return solnFound[i];
		} else {
			return false;
		}
		
	}
	public boolean hasResults(){
		//to check whether the ArrayList is empty or not
		return resultsExist;
	}
	
	//setters
	public void setSolnPath(int i, int[] solnPath) {
	    //solnPath is initialized if null
	    if (this.solnPath == null) {
	        this.solnPath = new ArrayList<>();
	    }

	    //add new entries dynamically based on index
	    while (i >= this.solnPath.size()) {
	        this.solnPath.add(null); //fill with nulls or empty arrays up to index i
	    }

	    //set or update the path at index i
	    this.solnPath.set(i, solnPath);
	}

	public void setSolnCost(int i, double solnCost){
		if(i >= 0 && i < this.solnCost.length) {
	        this.solnCost[i] = solnCost; //set solution cost for graph i
	    }
	}
	
	public void setCompTime(int i, double compTime){
		if(i >= 0 && i < this.compTime.length) {
	        this.compTime[i] = compTime; //set computation time for graph i
	    }
	}
	
	public void setSolnFound(int i, boolean solnFound){
		if(i >= 0 && i < this.solnFound.length) {
	        this.solnFound[i] = solnFound; //set solution found flag for graph i
	    }
	}
	
	public void setHasResults(boolean b){
		//setting results
		resultsExist = b;
	}
	
	public int getSolnFoundLength() {
	    return solnFound.length;
	}
	
	public void init(ArrayList<Graph> G){
		//initialize the ArrayList of Graphs
		 graph = new ArrayList<>(G);
	     solnPath = new ArrayList<>();
	     solnCost = new double[G.size()];
	     compTime = new double[G.size()]; 
	     solnFound = new boolean[G.size()]; 
	     
	    }
	
	
	public static void reset(ArrayList<Graph> graphList) {
	    // Clearing the graph data and resetting all arrays
	    graph.clear();
	    solnPath.clear();
	    Arrays.fill(solnCost, -1);
	    Arrays.fill(compTime, -1);
	    Arrays.fill(solnFound, false);
	    resultsExist = false;

	    // Clear the external graph list as well
	    if (graphList != null) {
	        graphList.clear();
	    }
	}
	
	public void run(ArrayList<Graph> G, int i, String algoName) {
	    // Check if the index i is within the bounds of the list of graphs
	    if (i >= 0 && i < G.size()) {
	        Graph graph = G.get(i); // Retrieve the graph at index i

	        long start = System.currentTimeMillis(); // Start timing the TSP solution
	        int[] solutionPath = solveTSP(graph); // Solve the TSP for the given graph
	        long elapsedTime = System.currentTimeMillis() - start; // Calculate elapsed time

	        // Check if a solution path was found
	        if (solutionPath != null) {
	            // Ensure the solution path list is large enough and set the solution at index i
	            if (i < solnPath.size()) {
	                solnPath.set(i, solutionPath);
	            } else {
	                solnPath.add(solutionPath);
	            }

	            // Store the cost and computation time of the solution
	            solnCost[i] = graph.pathCost(solutionPath);
	            compTime[i] = elapsedTime;
	            solnFound[i] = true; // Mark solution as found

	            // Optional: Print statement to indicate completion
	            // System.out.println("Graph " + (i + 1) + " done in " + compTime[i] + "ms.");

	        } else {
	            // Mark solution as not found if no path was returned
	            solnFound[i] = false;
	            System.out.println("ERROR: " + algoName + " did not find a TSP route for Graph " + (i + 1) + "!");
	        }
	    } else {
	        // Print error if the index i is out of bounds
	        System.out.printf("ERROR: Input must be an integer in [0,%d]%n", G.size() - 1);
	        System.out.println();
	    }

	    // Optional: Print a newline for formatting
	    // System.out.println();
	}

	
	public void runSingleGraph(ArrayList<Graph> G, Graph graph, String algoName) {
	    int index = 0; // Set index to 0, assuming the graph to solve is at the first position
	    run(G, index, algoName); // Call the run method with the specified algorithm and graph
	}
	
	public abstract int[] nextNode(Graph G, ArrayList<Integer> visited); // Abstract method to be implemented in subclasses for specific TSP algorithms

	private int[] solveTSP(Graph graph) {
	    ArrayList<Integer> visited = new ArrayList<>(); // List to keep track of visited nodes
	    visited.add(0); // Start from the first node (0 index)

	    // Loop until all nodes are visited
	    while (!allNodesVisited(visited, graph)) {
	        int[] nextNodeInfo = nextNode(graph, visited); // Determine next node to visit
	        if (nextNodeInfo == null || nextNodeInfo.length == 0) {
	            break; // Exit if no next node is found
	        }
	        int positionToAdd = nextNodeInfo.length > 1 ? nextNodeInfo[1] : visited.size(); // Determine position to add the node
	        visited.add(positionToAdd, nextNodeInfo[0]); // Add node to visited list
	    }

	    // Check if cycle is complete (start node = end node)
	    if (!visited.get(visited.size() - 1).equals(visited.get(0))) {
	        visited.add(visited.get(0));
	    }

	    // Convert visited list to array and return it
	    int[] path = new int[visited.size()];
	    for (int i = 0; i < visited.size(); i++) {
	        path[i] = visited.get(i);
	    }
	    return path;
	}

	private boolean allNodesVisited(ArrayList<Integer> visited, Graph G) {
	    return visited.size() >= G.getN(); // Return true if all nodes are visited
	}




	private boolean isValidTSPPath(ArrayList<Integer> path, Graph graph) {
	    if (path == null || path.isEmpty() || graph == null) {
	        return false; //invalid path or graph
	    }

	    int n = graph.getN(); //total number of nodes in the graph
	    boolean[] visited = new boolean[n]; //track visited nodes

	    //check if all nodes are visited exactly once
	    for (int i = 0; i < path.size(); i++) {
	        int currentNode = path.get(i);

	        //check for invalid node or repeated visit
	        if (currentNode < 0 || currentNode >= n || visited[currentNode]) {
	            return false;
	        }

	        visited[currentNode] = true; //mark node as visited
	    }

	    //check if all nodes are visited
	    for (boolean visit : visited) {
	        if (!visit) {
	            return false; //some nodes are not visited
	        }
	    }

	    //check if there is a return path to the starting node
	    int lastNode = path.get(path.size() - 1);
	    int firstNode = path.get(0);
	    if (!graph.getArc(lastNode, firstNode)) {
	        return false; //no return path to the starting node
	    }

	    return true; //all conditions satisfied
	}

	
	
	public int nearestNeighbor(Graph G, int city, boolean[] visited) {
	    // Get the total number of nodes in the graph
	    int n = G.getN(); 

	    // Initialize variables for tracking the nearest node and its distance
	    int nearestNode = -1;
	    double minDist = Double.MAX_VALUE;
	    
	    // Iterate through all nodes in the graph
	    for (int i = 0; i < n; i++) {
	        // Check if the node has not been visited and is not the current city
	        if (!visited[i] && i != city) {
	            // Calculate the distance from the current city to node i
	            double dist = G.getCost(city, i);
	            
	            // If this distance is the smallest so far, update nearestNode and minDist
	            if (dist < minDist) {
	                minDist = dist;
	                nearestNode = i;
	            }
	        }
	    }
	    
	    // Return the index of the nearest unvisited node or -1 if none found
	    return nearestNode;
	}
	
	
	
	public void printSingleResult(int i, boolean rowOnly) {
	    // Check if the index is within the bounds of the graph list
	    if (i >= 0 && i < graph.size()) {
	        // Print the graph number (index + 1 for user-friendly numbering)
	        System.out.printf("%3d", i + 1);

	        // Check if a solution was found for this graph
	        if (i < solnFound.length && solnFound[i]) {
	            // Format and print the solution cost, or "-" if not available
	            String costStr = (i < solnCost.length) ? String.format("%15.2f", solnCost[i]) : String.format("%15s", "-");
	            // Format and print the computation time, or "-" if not available
	            String timeStr = (i < compTime.length) ? String.format("%15.3f", compTime[i]) : String.format("%15s", "-");
	            System.out.print(costStr + timeStr);

	            // If rowOnly is false and a solution path exists, format and print the path
	            if (!rowOnly && i < solnPath.size() && solnPath.get(i) != null) {
	                String formattedPath = formatSolutionPath(solnPath.get(i));
	                System.out.format(" %25s", formattedPath);
	            } else if (!rowOnly) {
	                // If rowOnly is false but no path exists, print "-"
	                System.out.format("%25s", "-");
	            }
	        } else {
	            // If no solution found, print "-" for cost, time, and path
	            System.out.format("%15s%15s", "-", "-");
	            if (!rowOnly) {
	                System.out.format("%25s", "-");
	            }
	        }
	        System.out.println();
	    } else {
	        // Print an error message if the index is out of bounds
	        System.out.println("Invalid graph index: " + i);
	    }
	}

	private String formatSolutionPath(int[] solutionPath) {
	    // If the solution path is null or empty, return "-"
	    if (solutionPath == null || solutionPath.length == 0) {
	        return "-";
	    }

	    // Use StringBuilder to build a formatted string of the solution path
	    StringBuilder pathBuilder = new StringBuilder();
	    for (int j = 0; j < solutionPath.length; j++) {
	        // Add each city in the path, converting index to city number (index + 1)
	        pathBuilder.append(solutionPath[j] + 1); // Convert city index to city number
	        if (j < solutionPath.length - 1) {
	            pathBuilder.append("-"); // Add a separator between cities
	        }
	    }
	    return pathBuilder.toString();
	}




	
	public void printAll(ArrayList<Graph> graph){
		for(Graph graphPrintAll : graph){
			graphPrintAll.print();
			System.out.println();
		}
	}
	public void printStats() {
	    //check if the arrays have been initialized and have data
	    if (solnCost.length == 0 || compTime.length == 0) {
	        System.out.println("No data available to display stats.");
	        return;
	    }

	    //System.out.println("\nStatistical summary:");
	    System.out.println("--------------------------------------------");
	    System.out.format("%-15s%-15s%-15s%n", " ", "Cost (km)", "Comp time (ms)");
	    System.out.println("--------------------------------------------");

	    double minCost = Double.MAX_VALUE;
	    double maxCost = 0;
	    double minTime = Double.MAX_VALUE;
	    double maxTime = 0;
	    int successfulSolutions = 0;

	    for (int i = 0; i < solnCost.length; i++) {
	        if (solnFound[i]) {
	           
	            minCost = Math.min(minCost, solnCost[i]);
	            maxCost = Math.max(maxCost, solnCost[i]);
	            minTime = Math.min(minTime, compTime[i]);
	            maxTime = Math.max(maxTime, compTime[i]);
	            successfulSolutions++;
	        }
	    }

	    double avgCost = calcAvg(solnCost);
	    double avgTime = calcAvg(compTime);
	    double stDevCost = calculateStandardDeviation(solnCost, avgCost, successfulSolutions);
	    double stDevTime = calculateStandardDeviation(compTime, avgTime, successfulSolutions);
	    double successRate = successRate();
	    System.out.format("%-15s%15.2f%15.2f%n", "Average", avgCost, avgTime);
	    System.out.format("%-15s%15.2f%15.2f%n",  "St Dev", stDevCost, stDevTime);
	    System.out.format("%-15s%15.2f%15.2f%n",  "Min", minCost, minTime);
	    System.out.format("%-15s%15.2f%15.2f%n",  "Max", maxCost, maxTime);


	    System.out.println();
	    System.out.printf("Success rate: %.1f%%%n", successRate);
	    System.out.println();
	}



	private double calculateStandardDeviation(double[] values, double average, int count) {
	    if (count < 2) {
	    	return Double.NaN;
	    }

	    double sumSquareDiff = 0.0;
	    for (int i = 0; i < values.length; i++) {
	        if (solnFound[i]) {
	            sumSquareDiff += Math.pow(values[i] - average, 2);
	        }
	    }

	    return Math.sqrt(sumSquareDiff / (count - 1));
	}

	
	
	public static double calcAvg(double[] values) {
	    double sum = 0.0;
	    int count = 0;
	    for (double value : values) {
	        if (value >= 0) {
	            sum += value;
	            count++;
	        }
	    }
	    return count > 0 ? sum / count : 0.0;
	}
	
	public String formatRoute(int[] path) {
	    if (path == null || path.length == 0) {
	        return "-";
	    }
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < path.length; i++) {
	        sb.append(path[i] + 1); //add 1 to index to match the city number
	        if (i < path.length - 1) {
	            sb.append("-"); //separator between cities
	        }
	    }
	    return sb.toString();
	}

	
	public double successRate() {
	    if (solnFound.length == 0) {
	        return 0.0;
	    }

	    int totalGraphs = solnFound.length;
	    int successfulGraphs = 0;

	    for (boolean found : solnFound) {
	        if (found) {
	            successfulGraphs++;
	        }
	    }

	    return ((double) successfulGraphs / totalGraphs) * 100;
	}
	

}
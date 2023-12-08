import java.io.*;
import java.util.ArrayList;

/*
 * FIX:
 * 
 * Formatting of table in option "X"
 * Try all corner cases
 * Make it seem cleaner
 * Make it more efficient
 * Fix the calculation of the average; shows the same avg for NN-FL and NI, which is the cost for NN; NN is calculated faster, so it is announces as the winner
 * load graph "S_full.txt" to show the above error in the program
 * Fix data not added to the solution path array; it is showing size as 1 and i = 1
 * 
 * `
 * 
 * 
 * 
 * */

public class Pro5_samireez {

public static BufferedReader jIn = new BufferedReader(new InputStreamReader(System.in));
	
	public static Graph G = new Graph();
	public static Node node = new Node();
	public static TSPSolver nnSolver;
	public static TSPSolver nnflSolver;
	public static TSPSolver niSolver;
	
	public static void main(String[] args) throws NumberFormatException, IOException{
		
		ArrayList<Graph> graphList = new ArrayList<>();
		
		//menu options using a while loop and a series of switch-breaks
				while(true) {
					//shows menu options
					displayMenu();
					String choice = jIn.readLine().toUpperCase();
					
					switch (choice) {
					  
					//loads the graph
					case "L":
						System.out.println("");
						loadGraph(graphList);
						initSolvers(graphList);
						break;
					
					//display graph info
					case "I":
						System.out.println("");
						graphInfo(graphList);
						break;
					
						//clears all graphs
					case "C":
						System.out.println("");
						clearGraph(graphList);
						System.out.println();
						break;
					
						//run all NN algo
					case "R":
						System.out.println("");
						runAlgo(graphList, nnSolver, "NN", "Nearest neighbor");
						runAlgo(graphList, nnflSolver, "NN-FL", "Nearest neighbor first-last");
						runAlgo(graphList, niSolver, "NI", "Node insertion");
						break;
						
					case "X": //should be X, to compare algo performance
						System.out.println("");
						compareAlgoPerformance(graphList);
						break;
						
					//display algo performance
					case "D":
						System.out.println("");
						algoPerformance(graphList);
						break;
					
					//quits the program
					case "Q":
						System.out.println();
						System.out.print("Ciao!");
						System.exit(0);
					
					default:
						System.out.println();
						System.out.print("ERROR: Invalid menu choice!");
						System.out.println();
						System.out.println();
					}			
				}	

	}
	
	
	public static void displayMenu() {

		System.out.println(" " + " " + " " + "JAVA TRAVELING SALESMAN PROBLEM V3");
		System.out.println("L - Load graphs from file");
		System.out.println("I - Display graph info");
		System.out.println("C - Clear all graphs");
		System.out.println("R - Run nearest neighbor algorithm");
		System.out.println("D - Display algorithm performance");
		System.out.println("X - Compare algorithm performance");
		System.out.println("Q - Quit");
		System.out.println();
		System.out.printf("Enter choice: ");
	}
	
    // Method to load graphs from a file into graphList
	public static void loadGraph(ArrayList<Graph> G) throws IOException, NumberFormatException{
			
				System.out.print("Enter file name (0 to cancel): ");
				String fileName = jIn.readLine();
				System.out.println();
				
				if("0".equals(fileName)) {
					
					System.out.println("File loading process canceled.");
					System.out.println();
					return;
				}
				
			try{
			
				BufferedReader fIn = new BufferedReader(new FileReader(fileName));
				ArrayList<Graph> graph = GraphLoader.loadGraph(fIn);
				
				if (!graph.isEmpty()){
					G.clear();
					G.addAll(graph);
					System.out.println();
				} else {
					System.out.println("ERROR: No graphs found in the file!");
					System.out.println();
				}
			} catch (FileNotFoundException e) {
				System.out.println("ERROR: File not found!");
				System.out.println();
			}
		}
	
    // Method to initialize solvers with the graph list
	public static void initSolvers(ArrayList<Graph> graphList) {
		nnSolver = new NNSolver();
		nnflSolver = new NNFLSolver();
		niSolver = new NISolver();
		
		//initialize each solver with the graph list
		nnSolver.init(graphList);
		nnflSolver.init(graphList);
		niSolver.init(graphList);
		
	}
	
    // Method to run a specified TSP algorithm on all graphs
	private static void runAlgo(ArrayList<Graph> graphList, TSPSolver solver, String algoName, String algoNameFull) {
		if(graphList.isEmpty()) {
			System.out.println("ERROR: No graphs have been loaded!");
			return;
		}
		
		for(int i = 0; i < graphList.size(); i++) {
			Graph graph = graphList.get(i);
			solver.run(graphList, i, algoName); //run the solver on graph i
		}
		
		System.out.println(algoNameFull + " algorithm done.");
		System.out.println();
	}
	
    // Method to display detailed information of each graph
	public static void graphInfo(ArrayList<Graph> graphList) throws IOException, NumberFormatException {
		printGraphSummary(graphList);

        while (true) {
            System.out.print("Enter graph to see details (0 to quit): ");
            String input = jIn.readLine().trim().toUpperCase();

            if (input.equals("0")) {
            	System.out.println();
                break;
            }

            try {
                int graphIndex = Integer.parseInt(input);

                if (graphIndex >= 1 && graphIndex <= graphList.size()) {
                    printDetails(graphList.get(graphIndex - 1), graphList);
                } else {
                	
                    System.out.println("ERROR: Input must be an integer in [0, " + graphList.size() + "]!");
                    System.out.println();
                }
            } catch (NumberFormatException e) {
            	
                System.out.println("ERROR: Input must be an integer in [0, " + graphList.size() + "]!");
                System.out.println();
            }
        }
	}
	
    // Method to print a summary of all graphs
	private static void printGraphSummary(ArrayList<Graph> graphList) {
	    System.out.println("GRAPH SUMMARY");
	    System.out.printf("%-3s%10s%10s","No.","# nodes","# arcs");
	    System.out.println();
	    System.out.println("------------------------");

	    //iterate over each graph in the list and print its summary
	    for (int i = 0; i < graphList.size(); i++) {
	        Graph graph = graphList.get(i);
	        System.out.printf("%3d%10d%10d%n", i + 1, graph.getN(), graph.getM());
	    }
	    
	    System.out.println();
	}
	
    // Method to print a summary of all graphs
	public static void printDetails(Graph graph, ArrayList<Graph> graphList) {
	    System.out.println();
        System.out.println("Number of nodes: " + graph.getN());
        System.out.println("Number of arcs: " + graph.getM());

	    System.out.println();
        graph.printNodesAndArcs();
        System.out.println();
        printGraphSummary(graphList);
    }

    // Method to clear all graph data
	public static void clearGraph(ArrayList<Graph> graphList) {
	    TSPSolver.reset(graphList); // Pass the graphList to reset method
	    System.out.print("All graphs cleared.");
	    System.out.println();
	}
	
    // Method to run nearest neighbor algorithm
	public static void nearestNeighborAlgo(ArrayList<Graph> graphList, String algoType) {
	    if (graphList.isEmpty()) {
	        System.out.println("ERROR: No graphs have been loaded!");
	        return;
	    }

	    TSPSolver selectedSolver;
	    
	    switch (algoType) {
		    case "NN":
		    	selectedSolver = nnSolver;
		    	break;
		    
		    case "NNFL":
		    	selectedSolver = nnflSolver;
		    	break;
		    
		    case "NI":
		    	selectedSolver = niSolver;
		    	break;
		    	
		    default:
		    	System.out.println("ERROR: Unknown algorithm type!");
		    	return;
	    }
	    
	    for(int i = 0; i < graphList.size(); i++) {
	    	selectedSolver.run(graphList,  i, algoType); //run the chosen algo on graph i
	    }
	    
	    System.out.println();
	    System.out.println("All " + algoType + " completed for all graphs.");
	    System.out.println();
	}
	
    // Method to compare and print performance of all TSP algorithms
	public static void compareAlgoPerformance(ArrayList<Graph> graphList) {
		//check for solver
		if(graphList.isEmpty()) {
			System.out.println("ERROR: Not all algorithms have been run. Please run all algorithms before comparing.");
			return;
		}
		
		double avgCostNN = TSPSolver.calcAvg(nnSolver.getSolnCosts());
		double avgCompTimeNN = TSPSolver.calcAvg(nnSolver.getCompTimes());
		
		double avgCostNNFL = TSPSolver.calcAvg(nnflSolver.getSolnCosts());
		double avgCompTimeNNFL = TSPSolver.calcAvg(nnflSolver.getCompTimes());
		
		double avgCostNI = TSPSolver.calcAvg(niSolver.getSolnCosts());
		double avgCompTimeNI = TSPSolver.calcAvg(niSolver.getCompTimes());

		//print the headers
		System.out.println("---------------------------------------------------------------");
		System.out.printf("%-10s %-15s %-20s %-15s\n", "", "Cost (km)", "Comp time (ms)", "Success rate (%)");
		System.out.println("---------------------------------------------------------------");
	
		//display results
		printSolverPerformance("NN", nnSolver);
		printSolverPerformance("NN-FL", nnflSolver);
		printSolverPerformance("NI", niSolver);
		
		//print winners for each category
		System.out.println("---------------------------------------------------------------");
		printCategoryWinners(nnSolver, nnflSolver, niSolver);
		
		//print overall winner 
		String bestPerformer = overallBest(new double[] {avgCostNN, avgCostNI, avgCostNNFL}, new double[]{avgCompTimeNN, avgCompTimeNNFL, avgCompTimeNI}, new double[]{nnSolver.successRate(), nnflSolver.successRate(), niSolver.successRate()});
		System.out.println("Overall winner: " + bestPerformer);
		System.out.println();

	}
	
	private static String overallBest(double[] costs, double[] times, double[] rates) {
	    int[] points = new int[costs.length]; // Initialize points array.

	    // Assign points for cost, time, and success rate.
	    assignPoints(points, costs, true); // Lower cost is better.
	    assignPoints(points, times, true); // Lower time is better.
	    assignPoints(points, rates, false); // Higher success rate is better.

	    // Check for a tie.
	    if (isTie(points)) {
	        return "Unclear";
	    }

	    // Find the solver with the highest points.
	    int bestSolverIndex = getIndexOfHighestInt(points);
	    return getSolverName(bestSolverIndex);
	}

	private static void assignPoints(int[] points, double[] metrics, boolean lowerIsBetter) {
	    int bestIndex = lowerIsBetter ? getIndexOfLowest(metrics) : getIndexOfHighest(metrics);
	    points[bestIndex]++;
	}

	private static boolean isTie(int[] points) {
	    int firstValue = points[0];
	    for (int i = 1; i < points.length; i++) {
	        if (points[i] != firstValue) {
	            return false;
	        }
	    }
	    return true;
	}


	private static int getIndexOfLowest(double[] values) {
	    int index = 0;
	    for (int i = 1; i < values.length; i++) {
	        if (values[i] < values[index]) {
	            index = i;
	        }
	    }
	    return index;
	}

	private static int getIndexOfHighestInt(int[] values) {
	    int index = 0;
	    for (int i = 1; i < values.length; i++) {
	        if (values[i] > values[index]) {
	            index = i;
	        }
	    }
	    return index;
	}
	
	private static int getIndexOfHighest(double[] values) {
	    int index = 0;
	    for (int i = 1; i < values.length; i++) {
	        if (values[i] > values[index]) {
	            index = i;
	        }
	    }
	    return index;
	}

	
	private static void printSolverPerformance(String name, TSPSolver solver) {
		System.out.printf("%-10s %-15.2f %-20.3f %-15.1f\n", name, solver.calcAvg(solver.getSolnCosts()), solver.calcAvg(solver.getCompTimes()), solver.successRate());
		solver.successRate();
	}
	
	
	
	private static void printCategoryWinners(TSPSolver nnsolver, TSPSolver nnflsolver, TSPSolver niSolver) {
		double[] avgCosts = {
				TSPSolver.calcAvg(nnSolver.getSolnCosts()), 
				TSPSolver.calcAvg(nnflSolver.getSolnCosts()), 
				TSPSolver.calcAvg(niSolver.getSolnCosts())
				};
		    
		    double[] avgTimes = {
		    		TSPSolver.calcAvg(nnSolver.getCompTimes()),
		    		TSPSolver.calcAvg(nnflSolver.getCompTimes()),
		    		TSPSolver.calcAvg(niSolver.getCompTimes())		    
		    		};
		    
		    double[] successRates = {
		        nnSolver.successRate(),
		        nnflSolver.successRate(),
		        niSolver.successRate()
		    };

		    //determine the best solver for each category
		    String bestCostSolver = getWinnerByMetric(avgCosts, true); // Lower cost is better
		    String bestTimeSolver = getWinnerByMetric(avgTimes, true); // Lower time is better
		    String bestRateSolver = getWinnerByMetric(successRates, false); // Higher success rate is better

		    //print formatted output
		    //System.out.println("-----------------------------------------------------------------");
		    System.out.format("Winner %-15s %-20s %-15s\n", bestCostSolver, bestTimeSolver, bestRateSolver);
		    System.out.println("-----------------------------------------------------------------");
	}
	
	private static String getWinnerByMetric(double[] metrics, boolean lowerIsBetter) {
	    int bestIndx = 0;
	    boolean tie = false; //flag to detect a tie
	    for (int i = 1; i < metrics.length; i++) {
	        if ((lowerIsBetter && metrics[i] < metrics[bestIndx]) || (!lowerIsBetter && metrics[i] > metrics[bestIndx])) {
	            bestIndx = i;
	            tie = false; //reset the tie flag because we have a new best
	        } else if (metrics[i] == metrics[bestIndx]) {
	            tie = true; //set the tie flag because we have found a tie
	        }
	    }

	    if (tie) {
	        return "Unclear"; //return "Unclear" if there was a tie
	    } else {
	        return getSolverName(bestIndx); //return the name of the solver with the best performance
	    }
	}
	
	private static String getSolverName(int index) {
	    switch (index) {
	        case 0: return "NN";
	        case 1: return "NN-FL";
	        case 2: return "NI";
	        default: return "Unclear";
	    }
	}
		
	public static void algoPerformance(ArrayList<Graph> graphList) {
		
		//check if the solvers have a result
		boolean nnResultsExist = checkResultsExist(nnSolver);
		boolean nnflResultsExist = checkResultsExist(nnflSolver);
		boolean niResultsExist = checkResultsExist(niSolver);
		
		

	    //display error message if no results exist
	    if (!nnResultsExist && !nnflResultsExist && !niResultsExist) {
	        System.out.println("ERROR: No results exist!");
	        System.out.println();  //adding a new line for cleaner output
	        return;  //return to exit the method
	    }

	    //display detailed results for each algorithm
	    if(nnResultsExist) {
	    	System.out.println("Detailed results for nearest neighbor:");
		    System.out.println("--------------------------------------------");
		    System.out.format("%s%15s%15s%15s","No.","Cost (Km)","Comp time (ms)","Route");
		    System.out.println();
		    System.out.println("--------------------------------------------");
		    for (int i = 0; i < graphList.size(); i++) {
		        nnSolver.printSingleResult(i, false); //true for rowOnly, if you want to display the route in a single row
		    }
		    
		    //display statistical summary
		    System.out.println("\nStatistical summary for nearest neighbor:");
		    nnSolver.printStats(); //this will calculate and print the stats
	    } 
	    
	    if (nnflResultsExist) {
	    	System.out.println("Detailed results for nearest neighbor first-last:");
		    System.out.println("--------------------------------------------");
		    System.out.format("%s%15s%15s%15s","No.","Cost (Km)","Comp time (ms)","Route");
		    System.out.println();
		    System.out.println("--------------------------------------------");
		    for (int i = 0; i < graphList.size(); i++) {
		        nnflSolver.printSingleResult(i, false); //true for rowOnly, if you want to display the route in a single row
		    }
		    
		    //display statistical summary
		    System.out.println("\nStatistical summary for nearest neighbor first-last:");
		    nnflSolver.printStats(); //this will calculate and print the stats
	    }
	    
	    if (niResultsExist) {
	    	System.out.println("Detailed results for node insertion:");
		    System.out.println("--------------------------------------------");
		    System.out.format("%s%15s%15s%15s","No.","Cost (Km)","Comp time (ms)","Route");
		    System.out.println();
		    System.out.println("--------------------------------------------");
		    for (int i = 0; i < graphList.size(); i++) {
		        niSolver.printSingleResult(i, false); //true for rowOnly, if you want to display the route in a single row
		    }
		    
		    //display statistical summary
		    System.out.println("\nStatistical summary for node insertion:");
		    niSolver.printStats(); //this will calculate and print the stats
	    }
	}
	
	private static boolean checkResultsExist(TSPSolver solver) {
		for(int i = 0; i < solver.getSolnFoundLength(); i++) {
			if(solver.getSolnFound(i)) {
				return true;
			}
		}
		return false;
	}
	
	
    // Method to get a valid double input from user
	public static double getDouble(String prompt, double LB, double UB) throws IOException{
		
		double value = 0;
		boolean valid;
		
		do {
			try {
				valid = true;
				System.out.print(prompt);
				String input = jIn.readLine();
				value = Double.parseDouble(input);
				
				if (value >= LB && value <= UB) {
					break;
				} else {
	                throw new NumberFormatException();
	            }
			}
			catch (NumberFormatException e) {
				valid = false;
				System.out.printf("ERROR: Input must be a real number in [%.1f, %.1f]!", LB, UB);
                System.out.println("");
			}
		} while (!valid);
		
		return value;	
	}
	
    // Method to get a valid integer input from user
	public static int getInteger(String prompt, int LB, int UB) throws IOException{
		
		int value = 0;
		boolean valid;
		
		do {
			try {
				valid = true;
				System.out.print(prompt);
				String input = jIn.readLine();	
				value = Integer.parseInt(input);
				
				if (value >= LB && value <= UB) {
					break;				
				} else {
	                throw new NumberFormatException();
	            }
			}catch (NumberFormatException e){
				valid = false;
				System.out.printf("ERROR: Input must be an integer in [%d, %d]!\n", LB, UB);
				System.out.println("");
			}
		} while (!valid);
		
		return value;

	}

}

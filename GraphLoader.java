import java.io.*;
import java.util.ArrayList;

public class GraphLoader {

	public static ArrayList<Graph> loadGraph(BufferedReader reader) throws IOException {
	    ArrayList<Graph> graphs = new ArrayList<>();
	    String line;
	    int totalGraphs = 0;
	    int validGraphs = 0;

	    while ((line = reader.readLine()) != null) {
	        if (line.trim().isEmpty()) {
	            continue; //skip empty lines between graphs
	        }
	        
	        totalGraphs++;
	        int numNodes = Integer.parseInt(line.trim());
	        Graph graph = new Graph(numNodes);
	        boolean validGraph = true;

	        //read and create nodes
	        for (int i = 0; i < numNodes; i++) {
	            line = reader.readLine();
	            if (line == null || line.trim().isEmpty()) {
	                validGraph = false; //node info line expected
	                break;
	            }
	            String[] parts = line.trim().split(",");
	            String nodeName = parts[0].trim();
	            double lat = Double.parseDouble(parts[1].trim());
	            double lon = Double.parseDouble(parts[2].trim());

	            //check for invalid coordinates or duplicate nodes
	            if (lat < -90 || lat > 90 || lon < -180 || lon > 180 || graph.hasDuplicate(nodeName, lat, lon)) {
	                validGraph = false; //invalid coordinates or duplicate node detected
	                break;
	            }
	            graph.addNode(new Node(nodeName, lat, lon));
	        }

	        if (!validGraph) {
	            //skip to the next graph section if the current graph is invalid
	            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
	                //skip all lines until the next empty line
	            }
	            continue; //continue to the next graph
	        }

	        //read and create arcs
	        for (int i = 0; i < numNodes - 1; i++) {
	            line = reader.readLine();
	            if (line == null || line.trim().isEmpty()) {
	                //this should not happen as per the file structure
	                //but if it does, skip to the next graph
	                validGraph = false;
	                break;
	            }
	            String[] arcIndices = line.trim().split(","); // Splitting the line into individual indices of connected nodes, trimming any leading or trailing whitespace
	            for (int j = 0; j < arcIndices.length; j++) { // Iterating through each index in the array of connected nodes
	                int connectedNodeIndex = Integer.parseInt(arcIndices[j].trim()) - 1; // Parsing the string to an integer and adjusting for zero-based indexing
	                
	                // Checking if an arc doesn't already exist and that it's not a self-loop (node connected to itself)
	                if (!graph.isArcPresent(i, connectedNodeIndex) && i != connectedNodeIndex) {
	                    // Adding an arc with the cost equal to the distance between the nodes
	                    graph.addArcWithCost(i, connectedNodeIndex, Node.distance(graph.getNode(i), graph.getNode(connectedNodeIndex)));
	                }
	            }
	            }
	        

	        if (validGraph) {
	            graphs.add(graph);
	            validGraphs++;
	        } else {
	            //skip any remaining lines until an empty line is found, signaling the end of the current graph
	            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
	                //intentionally empty to skip the remaining lines of the invalid graph
	            }
	        }
	    }
	    
	    System.out.println(validGraphs + " of " + totalGraphs + " graphs loaded!");
	    return graphs;
	}

}
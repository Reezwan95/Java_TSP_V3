import java.io.*;
import java.util.ArrayList;

public class NNFLSolver extends TSPSolver {

    public NNFLSolver() {
        super();
    }

    @Override
    
    public int[] nextNode(Graph G, ArrayList<Integer> visited) {
    	
    	if (allNodesVisited(visited, G)) {
            return new int[]{-1, -1}; // Indicate that all nodes have been visited
        }
        
        int nearestToFirst = -1; // Initialize with -1
        int nearestToLast = -1; // Initialize with -1
        double costToFirst = Double.MAX_VALUE; // Initialize with max value
        double costToLast = Double.MAX_VALUE; // Initialize with max value

        nearestToFirst = nearestNeighbor(G, visited.get(0), visited);
        nearestToLast = nearestNeighbor(G, visited.get(visited.size() - 1), visited);
        
     // Decide where to add the next node based on cost comparison
        if (nearestToFirst != -1 && (nearestToLast == -1 || costToFirst <= costToLast)) {
            // Add to the beginning of the path
            visited.add(0, nearestToFirst);
            return new int[]{nearestToFirst, 0}; // Adding at the start
        } else if (nearestToLast != -1) {
            // Add to the end of the path
            visited.add(nearestToLast);
            return new int[]{nearestToLast, visited.size() - 1}; // Adding at the end
        }
        return new int[]{-1, -1}; // No nearest neighbor found
    }
    
    private boolean allNodesVisited(ArrayList<Integer> visited, Graph G) {
        // Check if the number of visited nodes is at least as large as the total number of nodes in the graph
        return visited.size() >= G.getN();
    }
    
    private int nearestNeighbor(Graph G, int currentNode, ArrayList<Integer> visited) {
        int nearestNeighbor = -1;
        double nearestDistance = Double.MAX_VALUE;

        for (int i = 0; i < G.getN(); i++) {
            if (!visited.contains(i)) {
                double distance = G.getCost(currentNode, i);
                if (distance < nearestDistance) {
                    nearestNeighbor = i;
                    nearestDistance = distance;
                }
            }
        }

        return nearestNeighbor;
    }
}

import java.util.ArrayList;

public class NISolver extends TSPSolver {

    public NISolver() {
        super();
    }

    @Override
    public int[] nextNode(Graph G, ArrayList<Integer> visited) {
        if (visited.size() == G.getN()) {
            return new int[] { visited.get(0) }; // Close the cycle
        }

        int bestNode = -1;
        int bestPosition = -1;
        double bestIncrease = Double.MAX_VALUE;

        // Iterate through each possible insertion point
        for (int i = 0; i <= visited.size(); i++) {
            for (int k = 0; k < G.getN(); k++) {
                if (!visited.contains(k)) {
                    double costIncrease = calculateInsertionCost(G, visited, i, k);
                    if (costIncrease < bestIncrease) {
                        bestNode = k;
                        bestPosition = i;
                        bestIncrease = costIncrease;
                    }
                }
            }
        }

        if (bestNode == -1) {
            return null; // No valid node found
        }

        return new int[] { bestNode, bestPosition };
    }


    public boolean canBeInserted(Graph G, ArrayList<Integer> visited, int i, int k) {
        // Check if k is a valid node and not already visited
        if (k < 0 || k >= G.getN() || visited.contains(k)) {
            return false;
        }

        // Check if there are valid arcs from node i to k and from k to the next node in the path
        int nextNodeIndex = (i + 1) % visited.size(); // Ensures wrap-around at the end of the list
        return G.getArc(visited.get(i), k) && G.getArc(k, visited.get(nextNodeIndex));
    }
    
    private boolean[] createVisitedArray(ArrayList<Integer> visited, int numNodes) {
        boolean[] visitedArray = new boolean[numNodes];
        for (Integer node : visited) {
            visitedArray[node] = true;
        }
        return visitedArray;
    }

    
    private void checkInsertionAtStartOrEnd(Graph G, ArrayList<Integer> visited, int[] bestNodeAndPosition, double bestIncrease) {
    	boolean[] visitedArray = createVisitedArray(visited, G.getN());
    	int nearestToFirst = nearestNeighbor(G, visited.get(0), visitedArray);
    	int nearestToLast = nearestNeighbor(G, visited.get(visited.size() - 1), visitedArray);

        // Calculate cost increase for insertion at the start
        double startIncrease = G.getCost(nearestToFirst, visited.get(0));
        if (!visited.contains(nearestToFirst) && startIncrease < bestIncrease) {
            bestNodeAndPosition[0] = nearestToFirst;
            bestNodeAndPosition[1] = 0; // Position to insert at start
        }

        // Calculate cost increase for insertion at the end
        double endIncrease = G.getCost(visited.get(visited.size() - 1), nearestToLast);
        if (!visited.contains(nearestToLast) && endIncrease < bestIncrease) {
            bestNodeAndPosition[0] = nearestToLast;
            bestNodeAndPosition[1] = visited.size(); // Position to insert at end
        }
    }


    private double calculateInsertionCost(Graph G, ArrayList<Integer> visited, int insertPosition, int newNode) {
        double costBeforeInsertion = 0;
        double costAfterInsertion = 0;

        if (insertPosition == 0 && visited.size() > 1) {
            // Inserting at the start of the list
            costBeforeInsertion = G.getCost(visited.get(0), visited.get(1));
            costAfterInsertion = G.getCost(newNode, visited.get(0)) + G.getCost(newNode, visited.get(1));
        } else if (insertPosition == visited.size() && !visited.isEmpty()) {
            // Inserting at the end of the list
            int lastNode = visited.get(visited.size() - 1);
            costBeforeInsertion = G.getCost(lastNode, visited.get(0));
            costAfterInsertion = G.getCost(lastNode, newNode) + G.getCost(newNode, visited.get(0));
        } else if (insertPosition > 0 && insertPosition < visited.size()) {
            // Inserting in between nodes
            costBeforeInsertion = G.getCost(visited.get(insertPosition - 1), visited.get(insertPosition));
            costAfterInsertion = G.getCost(visited.get(insertPosition - 1), newNode) + G.getCost(newNode, visited.get(insertPosition));
        }

        return costAfterInsertion - costBeforeInsertion;
    }



}
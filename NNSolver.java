import java.io.*;
import java.util.ArrayList;

public class NNSolver extends TSPSolver{

	public NNSolver () {
		super();
	}
	
	@Override
	public int [] nextNode (Graph G, ArrayList<Integer> visited) {
		if(visited.isEmpty()) {
			//if no nodes have been visited yet, start with the first node
			return new int[] {0};
		}
		
		int lastNode = visited.get(visited.size() - 1);
		int nN = nearestNeighbor(G, lastNode, visited);
		
		if(nN == -1) {
			return null; //no more nodes to add
		}
		
		//return the index of the NN
		return new int[] {nN};
	}
	
	private int nearestNeighbor(Graph G, int currNode, ArrayList<Integer> visited) {
		int nN = -1;
		double nearestDistance = Double.MAX_VALUE;
		
		for(int i = 0; i < G.getN(); i++) {
			if(!visited.contains(i) && G.getArc(currNode, i)) {
				double distance = G.getCost(currNode, i);
				if(distance < nearestDistance) {
					nN = i;
					nearestDistance = distance;
				}
			}
		}
		
		return nN;
	}
}

import java.util.ArrayList;

public class NISolver extends TSPSolver {	
	public NISolver() {
		
	}
	
	@Override
	public int[] nextNode (Graph G, ArrayList<Integer> visited){
		
		int[] nxtNode = new int[2];
		
		int firstNode = visited.get(0);
		int lastNode = visited.get(visited.size() - 1);
		double cheapest = Double.MAX_VALUE;
		int cheapestNode = 0;
		int cheapestNodeNearestNeighbour = -1;
		
		for (Integer i : visited){
		int i_NearestNeighbour = nearestInsertibleNeighbor(G, visited, i);
			if (i_NearestNeighbour != -1){
				double i_NearestNeighbourCost = G.getCost(i, i_NearestNeighbour);
				if (i_NearestNeighbourCost < cheapest ){
					cheapest = i_NearestNeighbourCost;
					cheapestNode = i;
					cheapestNodeNearestNeighbour = i_NearestNeighbour;
				}
			}
		}
		
	
		if (cheapestNodeNearestNeighbour == -1){
			nxtNode [0] = -1;
			nxtNode [1] = 0;
		}
		
		else if (cheapestNode == firstNode){
			nxtNode [0] = cheapestNodeNearestNeighbour;
			nxtNode [1] = 0;
		}
		
		
		else if (cheapestNode == lastNode){
			nxtNode [0] = cheapestNodeNearestNeighbour;
			nxtNode [1] = visited.size();
		}
		
		else {
			nxtNode [0] = cheapestNodeNearestNeighbour;
			nxtNode [1] = visited.indexOf(cheapestNode) + 1;
		}
		

		return nxtNode;
	}

	
	public boolean canBeInserted(Graph G, ArrayList <Integer> visited , int i,	int k){
		// check if node k can be inserted at position i
		//DB___System.out.println("--K is " + k + "--i is " + visited.get(i) + "--i+1 is " + visited.get(i+1));
		
		if (i == 0 || i == visited.size()-1)
			return true;
		
		if (G.existsArc(k, visited.get(i+1))){
			return true;
		}
				
		return false;
	}
	
	
	public int nearestInsertibleNeighbor(Graph G, ArrayList <Integer> visited, int k){
		// find node k’s nearest unvisited (and 'insertible') neighbor
		// return -1 if no neighbor available 
		
		int nearestNeighbor = -1;
		double leastCost = Double.MAX_VALUE;
		
		for( int j = 1; j <= G.getN(); j++ ){
			if (G.existsArc(k, j) && !(visited.contains(j)) && canBeInserted(G, visited, visited.indexOf(k), j) ) {
				if(G.getCost(k, j) < leastCost){
					leastCost = G.getCost(k, j);
					nearestNeighbor = j;
				}
				
			}
		}
		
		return nearestNeighbor;
	}
	
	@Override
	public String detailedResultsHeader(){
		return "Detailed results for node insertion:";
	}
	@Override
	public String statsSummaryHeader(){
		return "Statistical summary for node insertion:";
	}
	

	
}
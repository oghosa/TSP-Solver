import java.util.ArrayList;


public class NNFLSolver extends TSPSolver {	
	public NNFLSolver() {
		
	}
	
	@Override
	public int[] nextNode (Graph G, ArrayList<Integer> visited){
		
		int[] nxtNode = new int[2];

		int lastNode = visited.get(visited.size() -1);
		int firstNode = visited.get(0);
		
		int lastNodeNearestNeighbour = nearestNeighbor(G, visited, lastNode);
		int fistNodeNearestNeighbour = nearestNeighbor(G, visited, firstNode);
		
		//no nearest for both
		if (lastNodeNearestNeighbour == -1 && fistNodeNearestNeighbour == -1){
			nxtNode [0] = -1;
			nxtNode [1] = 0;
		}
		
		//no nearest for last, nearest for first
		else if (lastNodeNearestNeighbour == -1 && fistNodeNearestNeighbour != -1){
			nxtNode [0] = fistNodeNearestNeighbour;
			nxtNode [1] = 0;
		}
		
		//no nearest for first, nearest for last
		else if (lastNodeNearestNeighbour != -1 && fistNodeNearestNeighbour == -1){
			nxtNode [0] = lastNodeNearestNeighbour;
			nxtNode [1] = visited.size();
		}
		
		//nearest for both
		else if (G.getCost(lastNode, lastNodeNearestNeighbour) <= G.getCost(firstNode, fistNodeNearestNeighbour)){
			nxtNode [0] = lastNodeNearestNeighbour;
			nxtNode [1] = visited.size();;
		}
		else{
			nxtNode [0] = fistNodeNearestNeighbour;
			nxtNode [1] = 0;
		}
		
			
		return nxtNode;
	}

	
	@Override
	public String detailedResultsHeader(){
		return "Detailed results for nearest neighbor first-last:";
	}	
	@Override
	public String statsSummaryHeader(){
		return "Statistical summary for nearest neighbor first-last:";
	}
}
import java.util.ArrayList;

public class NNSolver extends TSPSolver {	
	public NNSolver() {
		
	}
	
	@Override
	public int[] nextNode (Graph G, ArrayList<Integer> visited){
		
		int[] nxtNode = new int[2];
		
		int lastVisitedNode = visited.get(visited.size() - 1);
		
		nxtNode [0] = nearestNeighbor(G, visited, lastVisitedNode);
		nxtNode [1] = visited.size();
		
		return nxtNode;
	}

	@Override
	public String detailedResultsHeader(){
		return "Detailed results for nearest neighbor:";
	}
	@Override
	public String statsSummaryHeader(){
		return "Statistical summary for nearest neighbor:";
	}
}

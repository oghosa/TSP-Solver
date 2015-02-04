import java.util.ArrayList;


public class TSPSolver {
	private ArrayList<int[]> solnPath; 		// ArrayList *or* array of solution paths
	private double[] solnCost; 				// ArrayList *or* array of solution costs
	private double[] compTime;				// ArrayList *or* array of computation times
	private boolean[] solnFound;			// ArrayList *or* array of T/F solns found
	private boolean resultsExist; 			// whether or not results exist
	private String name;
	
	// constructors
	public TSPSolver() {
		this.solnPath = new ArrayList<int[]>();
		this.solnCost = null;
		this.compTime = null;
		this.solnFound = null;
		this.resultsExist = false;
		
	}
	public TSPSolver(ArrayList<Graph> G) {
		this.init(G);
	}
	
	// getters
	public int[] getSolnPath(int i) {	return this.solnPath.get(i); 	}
	public double getSolnCost (int i) {		return this.solnCost[i];	}
	public double getCompTime (int i) {		return this.compTime[i];	}
	public boolean getSolnFound(int i) {	return this.solnFound[i];	}
	public boolean hasResults(){	return this.resultsExist;	}
	public String getName() { return this.name; }
	
	// setters
	public void setSolnPath(int i, int[] solnPath) {
		this.solnPath.set(i, solnPath);
	}
	public void setSolnCost(int i, double solnCost) {
		this.solnCost[i] = solnCost;
	}
	public void setCompTime(int i, double compTime) {
		this.compTime[i] = compTime;
	}
	public void setSolnFound(int i, boolean solnFound) {
		this.solnFound[i] = solnFound;
	}
	public void setHasResults(boolean b) {
		this.resultsExist = b;
	}
	public void setName (String name){
		this.name = name;
	}
	
	public void init(ArrayList<Graph> G){
		// Initialize variables and arrays
		int initialCapacity = G.size();
		
		this.solnPath = new ArrayList<int[]>(initialCapacity);
		this.solnPath.ensureCapacity(initialCapacity);
		
		//FOR SOME ANNOYING REASON INITIALCAPACITY ISNT WORKING FOR SOLNPATH
		for( int i = 0; i <G.size() ; i++){
			this.solnPath.add(new int[0]);
		}
		
		this.solnCost = new double[initialCapacity];
		this.compTime = new double[initialCapacity];
		this.solnFound = new boolean[initialCapacity];
		this.resultsExist = false;
		//DB---System.out.println("SIZE OF SOLUTION PATH IS " +this.solnPath.size());
		
	}
	public void reset(){
		// reset variables and arrays
		this.solnPath = new ArrayList<int[]>();
		this.solnCost = null;
		this.compTime = null;
		this.solnFound = null;
		this.resultsExist = false;
	}
	
	public void run(ArrayList<Graph> G, int i, boolean suppressOutput){
		// run nearest neighbor on Graph i
		
		Graph graph_i = G.get(i);
		
		ArrayList<Integer> solnPath = new ArrayList<Integer>();
		double solnCost = 0;
		double compTime = 0;
		boolean solnFound = false;
		
		//start from 1st city
		solnPath.add(1);
		
		//get nearest neighbor until all nodes are selected 
		int [] nextNodeToAdd = new int [2]; 
		
		long start = System.currentTimeMillis();
		
		for (int j = 0; j < graph_i.getN(); j++){
			
			nextNodeToAdd = nextNode(graph_i, solnPath);
			
			//check if there there is no unused node to select
			if (nextNodeToAdd[0] != -1){
				
				//check if to add at end of array i.e nextnode returns (x, solnPath.size()) 
				if (nextNodeToAdd[1] == solnPath.size())
					solnPath.add(nextNodeToAdd[0]);
				else
					solnPath.add(nextNodeToAdd[1], nextNodeToAdd[0]);
				
				//DB---checking path
				if (!suppressOutput){
					System.out.print(solnPath);
					System.out.println();
				}
			}
			else if ( (nextNodeToAdd[0] == -1)  && (j < graph_i.getN() - 1) ){
				//Algorithm Failed
				return;
			}
		}
		
		long elapsedTime = System.currentTimeMillis()-start;
		compTime = elapsedTime;
		
		//check if arc exist between last node and first node
		int lastNode = solnPath.get(solnPath.size() - 1);
		int fistNode = solnPath.get(0);
				
		if (graph_i.existsArc(lastNode, fistNode)){
			//Algorithm Succeeded
			solnPath.add(fistNode);
			
			solnFound = true;
			this.setSolnPath(i, BasicFunctions.convertIntegersToPrimitive(solnPath));
			solnCost = graph_i.pathCost(BasicFunctions.convertIntegersToPrimitive(solnPath));
			this.setSolnCost(i, solnCost);
			this.setCompTime(i, compTime);
			this.setSolnFound(i, solnFound);
			this.setHasResults(solnFound);
		}
		else{
			//Algorithm Failed
			return;
		}
		
	}
	
	public int[] nextNode(Graph G, ArrayList <Integer> visited){
		//TODO
		// find next node (return 2D array with [0]=next node, [1]=insertion position)
		int[] nxtNode = new int[2];
		
		return nxtNode;
	}
	
	public int nearestNeighbor(Graph G, ArrayList <Integer> visited, int k){
		// find node k’s nearest unvisited neighbor
		// return -1 if no neighbor available 
		
		int nearestNeighbor = -1;
		double leastCost = Double.MAX_VALUE;
		
		for( int j = 1; j <= G.getN(); j++ ){
			if (G.existsArc(k, j) && !(visited.contains(j))){
				if(G.getCost(k, j) < leastCost){
					leastCost = G.getCost(k, j);
					nearestNeighbor = j;
				}
				
			}
		}
		
		return nearestNeighbor;
	}
	
	public double successRate() {
		// calculate success rate
		double total = this.solnCost.length;
		double success_count = 0;
		double rate = 0;
		
		for (int i = 0 ; i < this.solnCost.length; i++){
			if(this.getSolnFound(i)){
				success_count += 1;
			}
		}
		
		rate = success_count/total;
		
		return rate;
	}
	public double avgCost() {
		// calculate average cost of successful routes
		int success_count = 0;
		
		for (int i = 0 ; i < this.solnCost.length; i++){
			if(this.getSolnFound(i)){
				success_count += 1;
			}
		}
		
		//Arrays for successful graphs
		double[] avgCost = new double[success_count]; 
		for (int i = 0, j = 0; i < this.solnCost.length; i++){
			if(this.getSolnFound(i)){
				avgCost[j] = this.getSolnCost(i);
				j++;
			}
		}
		
		return BasicFunctions.average(avgCost);
	}
	public double avgTime() {
		// calculate average comp time of successful routes
		int success_count = 0;
		
		for (int i = 0 ; i < this.solnCost.length; i++){
			if(this.getSolnFound(i)){
				success_count += 1;
			}
		}
		
		
		double[] avgCompTime = new double[success_count];
		for (int i = 0, j = 0 ; i < this.solnCost.length; i++){
			if(this.getSolnFound(i)){
				avgCompTime[j] = this.getCompTime(i);
				j++;
			}
		}
		return BasicFunctions.average(avgCompTime);
	}
	

	public void printSingleResult(int i, boolean rowOnly) {
		// print results for a single graph
		if (rowOnly == false){
			System.out.printf("%3d                -                  -   -\n", (i+1));
		}
		else{
			System.out.printf("%3d%17.2f%19.3f   ", (i+1), this.getSolnCost(i), this.getCompTime(i));
			
			for (int j=0; j<this.getSolnPath(i).length; j++){
				if(j==0){
					System.out.printf("%d", this.getSolnPath(i)[j]);
				}
				else{
					System.out.printf("-%d", this.getSolnPath(i)[j]);
				}
			}
			
			System.out.println();
		}
	}
	public void printAll() { 
		// print results for all graphs
		System.out.println(detailedResultsHeader());
		System.out.println("-----------------------------------------------");
		System.out.println("No.        Cost (km)     Comp time (ms)   Route");
		System.out.println("-----------------------------------------------");
		
		for (int i = 0; i < this.solnCost.length; i++){
			printSingleResult(i, this.getSolnFound(i));
		}
		System.out.println();
		
	}
	public void printStats () {
		// print statistics
		System.out.println(statsSummaryHeader());
		System.out.println("---------------------------------------");
		System.out.println("           Cost (km)     Comp time (ms)");
		System.out.println("---------------------------------------");
		
		int success_count = 0;
		
		for (int i = 0 ; i < this.solnCost.length; i++){
			if(this.getSolnFound(i)){
				success_count += 1;
			}
		}
		
		//Arrays for successful graphs
		double[] avgCost = new double[success_count]; 
		for (int i = 0, j = 0; i < this.solnCost.length; i++){
			if(this.getSolnFound(i)){
				avgCost[j] = this.getSolnCost(i);
				j++;
			}
		}
		
		double[] avgCompTime = new double[success_count];
		for (int i = 0, j = 0 ; i < this.solnCost.length; i++){
			if(this.getSolnFound(i)){
				avgCompTime[j] = this.getCompTime(i);
				j++;
			}
		}
		
		
		System.out.printf("Average%13.2f%19.3f\n", BasicFunctions.average(avgCost), BasicFunctions.average(avgCompTime));
		System.out.printf("St Dev%14.2f%19.3f\n", BasicFunctions.stDev(avgCost), BasicFunctions.stDev(avgCompTime));
		System.out.printf("Min%17.2f%19.3f\n", BasicFunctions.min(avgCost), BasicFunctions.min(avgCompTime));
		System.out.printf("Max%17.2f%19.3f\n", BasicFunctions.max(avgCost), BasicFunctions.max(avgCompTime));
		
		System.out.println();
	}

	public String detailedResultsHeader(){
		return "Detailed results:";
	}
	public String statsSummaryHeader(){
		return "Statistical summary:";
	}
}

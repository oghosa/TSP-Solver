import java.util.ArrayList;


public class Graph {
	private int n;					// number of nodes
	private int m;					// number of arcs
	private ArrayList<Node> node;	// ArrayList *or* array of nodes
	private boolean [] [] A;		// adjacency matrix
	private double [] [] C;			// cost matrix
	
	// constructors
	public Graph(){
		init(0);
	}
	
	public Graph(int n){
		init(n);
	}
	
	//setters
	public void setN(int n) {
		this.n = n;
	}
	public void setM(int m) {
		this.m = m;
	}
	public void setArc(int i, int j, boolean b) {
		this.A [i][j] = b;
		this.A[j][i] = b;
	}
	public void setCost(int i, int j, double c) {
		this.C [i][j] = c;
	}
	
	//getters
	public int getN() {
		return this.n;
	}
	public int getM() {
		return this.m;
	}
	public boolean getArc(int i, int j) {
		return this.A [i][j];
	}
	public double getCost(int i, int j) {
		return this.C [i-1][j-1];
	}
	public Node getNode(int i) {
		return this.node.get(i);
	}
	
	
	
	//Graph methods
	public void init(int n) {
		// initialize values and arrays
		
		setN(n);
		setM(0);
		this.node = new ArrayList<Node>(n);
		
		//set adjacency matrix to size n  & set values to 0(false)
		this.A = new boolean[n][n];
			//every element is false by default;
		
		//set cost matrix to size n & set values to 0
		this.C = new double[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				this.C[i][j] = 0.0;
			}
		}
		
	}
	public void reset() {
		// reset the graph
		this.n = 0;
		this.m = 0;
		this.node.clear();
		this.A = null;
		this.C = null;
	}
	public boolean existsArc(int i, int j) {
		//check if arc exists
		return this.A [i-1][j-1];
	}
	public boolean existsNode(Node t) {
		//check if node exists
		
		for (Node i : node) {
			if (i.getName().equals(t.getName())){
				return true;
			}
			else if ((i.getLat() == t.getLat()) && (i.getLon() == t.getLon())){
				return true;
			}
		}
		return false;
	}
	public boolean addArc(int i, int j) {
		// add an arc, return T/F success
		
		this.A[i-1][j-1] = true;
		return true;
	}
	public void removeArc(int k) {
		// remove an arc
		int index = 0;
		
		for (int i = 1; i <= this.n; i++){
			for (int j = i; j <= n; j++){
				if (existsArc(i, j)){
					index++;
					if(index == k) {
						//remove arc
						this.setArc(i-1, j-1, false);
						this.setArc(j-1, i-1, false);
						this.setCost(i-1, j-1, 0);
						this.setCost(j-1, 1-1, 0);
						this.updateArcsCount();
					}
				}
			}
		}
	}
	public boolean addNode(Node t) {
		// add a node to node array
		this.node.add(t);
		return true;
	}
	
	public void print() {
		// print all graph info
				
		System.out.println();
		System.out.printf("Number of nodes: %d\n", node.size());
		System.out.printf("Number of arcs: %d\n", this.getM());
		
		System.out.println();
		printNodes();
		System.out.println();
		printArcs();
	}
	public void printNodes() {
		//print node list
		
		System.out.println("NODE LIST");
		System.out.println("No.               Name        Coordinates");
		System.out.println("-----------------------------------------");
		
		for(Node city : this.node){	
			int index = this.node.indexOf(city) + 1;
			System.out.printf("%3d", index);
			city.print();
		}
	}
	public void printArcs() {
		// print arc list
		int index = 0;
		String cities;
		
		System.out.println("ARC LIST");
		System.out.println("No.    Cities       Distance");
		System.out.println("----------------------------");
		
		for (int i = 1; i <= this.n; i++){
			for (int j = i; j <= n; j++){
				if (existsArc(i, j)){
					index++;
					cities = Integer.toString(i) + "-" + Integer.toString(j);
					System.out.printf("%3d%10s%15.2f\n", index, cities, getCost(i, j));
				}
			}
		}
	}
	
	public boolean checkPath(int[] P) {
		// check feasibility of path p
		int maxCities = P.length;
		
		// Start and end cities do not match
		if (P[0] != P[maxCities-1]){
			System.out.println();
			System.out.printf("ERROR: Start and end cities must be the same!");
			System.out.println();
			return false;
		}
		
		//Cities (other than the home city, the first city in the path) are visited more than once
		//Not all cities are visited
		for (int i = 0; i <= P.length-2; i++){
			for (int j = i+1; j < P.length-1; j++){
				if (P[i] == P[j]){
					System.out.println();
					System.out.printf("ERROR: Cities cannot be visited more than once!\n");
					System.out.printf("ERROR: Not all cities are visited!");
					System.out.println();
					return false;
				}
			}
		}
				
				
		// Sequential cities are not connected by an arc
		for (int i = 0; i < P.length-1; i++){
			int j = i + 1;
			if (existsArc(P[i], P[j]) == false){
				System.out.println();
				System.out.printf("ERROR: Arc %d-%d does not exist!",P[i], P[j] );
				System.out.println();
				return false;
			}
		}
		
		return true;
	}
	public double pathCost(int[] P) {
		// calculate cost of path P
		double cost = 0;
		
		for (int i = 0; i <= P.length-2; i++ ){
			cost += getCost(P[i], P[i+1]);
		}
	
		return cost;
	}

	public void updateArcsCount (){
		//Counts number of arcs in graph
		int numArcs = 0;
		
		for (int i = 0; i < this.A.length; i++){
			for(int j = 0; j < this.A.length; j++){
				if (this.A[i][j]){
					numArcs++;
				}
			}
		}
		
		numArcs /= 2; //because the matrix is symmetric (Arcs counted twice)

		this.setM(numArcs);
	}

	public void updateArcsCost() {
		
		for (int i = 0; i < this.A.length; i++){
			for(int j = 0; j < this.A.length; j++){
				if (this.A[i][j]){
					Double arcDistsance  = Node.distance(this.getNode(j), this.getNode(i));
					this.setCost(i, j, arcDistsance);
					this.setCost(j, i, arcDistsance);
				}
			}
		}
	}

	public boolean isValid() {
		
		Node node1 = new Node();
		Node node2 = new Node();

		
		//check if node is duplicated
		for (int i = 0; i < this.getN() - 1 ; i++ ){
			node1 = this.getNode(i);
			for (int j = i+1; j < this.getN(); j++){
				node2 = this.getNode(j);
				
				if (node1.getName().equals(node2.getName())){
					return false;
				}
				else if ((node1.getLat() == node2.getLat()) && (node1.getLon() == node2.getLon())){
					return false;
				}
			}
		}
				
		//check if coordinates are valid
		for (Node t : this.node){
			if (t.getLat() < -90 || t.getLat() > 90){
				return false;
			}
			if (t.getLon() < -180 || t.getLon() > 180){
				return false;
			}
		}
		
		
		return true;
	}
	
}

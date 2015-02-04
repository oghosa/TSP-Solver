import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Pro5_igbinake {

	
	
	public static void main(String[] args) {	
		ArrayList<Graph> G = new ArrayList<Graph>();
		NNSolver NN = new NNSolver();
		NNFLSolver FL = new NNFLSolver();
		NISolver NI = new NISolver();
		boolean quit = false;
		
		do{
			String menuChoice = getMenuChoice();
			performMenuSelection(menuChoice, G, NN, FL, NI);
		}while (!quit);
		
	}
	
	
	public static void displayMenu(){
		//Display the menu.
		System.out.println("   JAVA TRAVELING SALESMAN PROBLEM V2");
		System.out.println("L - Load graphs from file");
		System.out.println("I - Display graph info");
		System.out.println("C - Clear all graphs");
		System.out.println("R - Run all algorithms");
		System.out.println("D - Display algorithm performance");
		System.out.println("X - Compare average algorithm performance");
		System.out.println("Q - Quit");
		System.out.println("");
		System.out.print("Enter choice: ");
	}
	
	public static String getMenuChoice(){
		String menuChoice = "";
		boolean valid;
		do {
			valid = true;
			
			displayMenu();
			menuChoice = BasicFunctions.getString();
			
			if  (valid && (menuChoice.equalsIgnoreCase("L") )) {
				menuChoice = "L";
			}
			else if (valid && (menuChoice.equalsIgnoreCase("I") )) {
				menuChoice = "I";
			}
			else if (valid && (menuChoice.equalsIgnoreCase("C") )) {
				menuChoice = "C";
			}
			else if (valid && (menuChoice.equalsIgnoreCase("R") )) {
				menuChoice = "R";
			}
			else if (valid && (menuChoice.equalsIgnoreCase("D") )) {
				menuChoice = "D";
			}
			else if (valid && (menuChoice.equalsIgnoreCase("X") )) {
				menuChoice = "X";
			}
			else if (valid && (menuChoice.equalsIgnoreCase("Q") )) {
				menuChoice = "Q";
			}
			else {
				valid = false;
				System.out.printf("\nERROR: Invalid menu choice!\n\n", menuChoice);
			}
		} while (!valid);
		
		return menuChoice;
	}
	
	public static void performMenuSelection(String menuChoice, ArrayList<Graph> G, NNSolver NN, NNFLSolver FL, NISolver NI) {
		//L---Load graphs from file
		if (menuChoice.equalsIgnoreCase("L")){
			if (loadFile(G) == true){
				loadSolvers(G, NN, FL, NI);
			}
		}
		
		//I---Display graph info if graph is loaded
		else if (menuChoice.equalsIgnoreCase("I")){
			if(isGraphLoaded(G)){
				displayGraphs(G);
			}

		}
		
		//C---Clear all graphs if graph is loaded
		else if (menuChoice.equalsIgnoreCase("C")){
			if(isGraphLoaded(G)){
				clearAllGraphs(G, NN, FL, NI);
			}
		}
		
		//R---Run nearest neighbor algorithm if graph is loaded
		else if (menuChoice.equalsIgnoreCase("R")){
			if(isGraphLoaded(G)){
				runAll(G, NN, FL, NI);
			}
		}
		
		//D---Display algorithm performance results exist
		else if (menuChoice.equalsIgnoreCase("D")){
			if(NN.hasResults() || FL.hasResults() || NI.hasResults()){
				printAll(NN, FL, NI);
			}
			else{
				System.out.print("\nERROR: Results do not exist for all algorithms!\n\n");
			}
			
		}
		
		//C---Compare average algorithm performance
		else if (menuChoice.equalsIgnoreCase("X")){
			if(NN.hasResults() || FL.hasResults() || NI.hasResults()){
				compare(NN, FL, NI);
			}
			else{
				System.out.print("\nERROR: Results do not exist for all algorithms!\n\n");
			}
			
		}
		
		//Q---Quit program
		else {
			quitProgram();
		}
	}
	
	
	public static boolean loadFile(ArrayList<Graph> G) {
		//Read in graphs from a user-specified file.
		
		int num_graphsToLoad = 0;
		int num_graphsLoaded = 0;
		
		//get filename from user
		System.out.println();
		System.out.print("Enter file name (0 to cancel): ");
		String filename = BasicFunctions.getString();
		
		//Exit if '0' is entered
		if ( filename.equals("0")){
			System.out.print("\nFile loading process canceled.\n\n");
			return false;
		}
		else {
			//Try to load graphs from file
			try {
				BufferedReader fin = new BufferedReader(new FileReader(filename));
				String line;
				
				//File Loading process begins
				do{
					line = fin.readLine();
					if (line != null){
						
						//Increase count of graphs in file at start of every graph
						num_graphsToLoad++;
						
						//number of cities
						int n = Integer.parseInt(line);
						Graph graph = new Graph(n);
						
						//Read Node lines
						for(int i = 0; i < n; i++){
							line = fin.readLine();
							String [] nodeLine  = line.split(",");
							String name = nodeLine[0];
							Double lat = Double.parseDouble(nodeLine[1]);
							Double lon = Double.parseDouble(nodeLine[2]);
							Node city = new Node(name, lat, lon);
							graph.addNode(city);
						}
						
						//Read Arc Lines
						for (int i = 0; i < n-1; i++){
							line = fin.readLine();
							String [] arcLine1 = line.split(",");
							int [] arcLine = BasicFunctions.convertStringToIntArray(arcLine1);
							
							for (int j = 0; j < arcLine.length; j++){
								//DB---System.out.printf("i - %d   j - %d\n", i, arcLine[j]);
								graph.setArc(i, arcLine[j]-1, true);
							}
							
						}
						
						
						//If graph is valid add to arrayList
						if (graph.isValid()){
							
							//DB---System.out.printf("Graph %d is valid\n", num_graphsToLoad);
							graph.updateArcsCost();
							graph.updateArcsCount();
							
							
							G.add(graph);
							
							//Increase count of number of graphs loaded
							num_graphsLoaded++;
						}
						else{
							//DB---System.out.printf("Graph %d is NOT valid\n", num_graphsToLoad);
						}
						
						
						//Read empty line
						line = fin.readLine();	
						
					}
				}while(line != null);
				fin.close();
				
			} catch (FileNotFoundException e) {
				System.out.print("\nERROR: File not found!\n\n");
				return false;
			} catch(IOException e){
				System.out.printf("\nERROR: IO exception!\n\n");
				return false;
			}
		}
		
		//Print number of graphs loaded
		System.out.printf("\n%d of %d graphs loaded!\n", num_graphsLoaded, num_graphsToLoad);
		System.out.println();
		
		return true;
	}
	
	public static void loadSolvers(ArrayList<Graph> G, NNSolver NN, NNFLSolver FL, NISolver NI) {
		//Initialize Solvers with ArrayList of graphs
		NN.init(G);
		FL.init(G);
		NI.init(G);
		
	}
	
	
	public static void displayGraphs(ArrayList<Graph> G) {
		//Display summary info for each graph, and allow user to select graphs to see detailed info.
		int graphChoice;
		
		do {
			System.out.println();
			printGraphSummary(G);
			
			graphChoice = BasicFunctions.getInteger("Enter graph to see details (0 to quit): ", 0, G.size());
			
			if (graphChoice != 0){
				G.get(graphChoice-1).print();
				System.out.println();
			}
			
		}while (graphChoice != 0);
	
		System.out.println();
	
	}
	
	public static void clearAllGraphs(ArrayList<Graph> G, NNSolver NN, NNFLSolver FL, NISolver NI) {
		//Clears all graphs in graph array and resets solver
		
		G.clear();
		
		resetAll(NN, FL, NI);
		
		System.out.println("\nAll graphs cleared.\n");
	}
	
	public static void resetAll(NNSolver NN, NNFLSolver FL, NISolver NI){
		// 	Reset all solvers.
		NN.reset();
		FL.reset();
		NI.reset();
	}
	
	public static void runAll(ArrayList<Graph> G, NNSolver NN, NNFLSolver FL, NISolver NI){
		//Run all solvers on all graphs.
		
		
		boolean suppressOutput = true;
		
		System.out.println();
		
		
		for (int i = 0; i < G.size(); i++ ){
			//System.out.println("NN__" + (i+1));
			NN.run(G, i, suppressOutput);
			if(NN.getSolnFound(i) == false){
				System.out.printf("ERROR: NN did not find a TSP route for Graph %d!\n", (i+1));
			}
		}
		System.out.println("Nearest neighbor algorithm done.\n");
		
		for (int i = 0; i < G.size(); i++ ){
			//System.out.println("FL__" + (i+1));
			FL.run(G, i, suppressOutput);
			if(FL.getSolnFound(i) == false){
				System.out.printf("ERROR: NN-FL did not find a TSP route for Graph %d!\n", (i+1));
			}
		}
		System.out.println("Nearest neighbor first-last algorithm done.\n");
		
		
		for (int i = 0; i < G.size(); i++ ){
			//System.out.println("NI__" + (i+1));
			NI.run(G, i, suppressOutput);
			if(NI.getSolnFound(i) == false){
				System.out.printf("ERROR: NI did not find a TSP route for Graph %d!\n", (i+1));
			}
		}
		System.out.println("Node insertion algorithm done.\n");
		
		
	}
	
	public static void printAll(NNSolver NN, NNFLSolver FL, NISolver NI){
		// Print the detailed results and statistics summaries for all algorithms.
		System.out.println();
		
		NN.printAll();
		NN.printStats();
		System.out.printf("Success rate: %.1f%%\n\n", (100 * NN.successRate()));
		
		System.out.println();
		FL.printAll();
		FL.printStats();
		System.out.printf("Success rate: %.1f%%\n\n", (100 * FL.successRate()));
		
		System.out.println();
		NI.printAll();
		NI.printStats();
		System.out.printf("Success rate: %.1f%%\n\n", (100 * NI.successRate()));
		
	}
	
	public static void compare(NNSolver NN, NNFLSolver FL, NISolver NI){
		// Compare the performance of the algorithms and pick winners.
		
		double NNavgCost = NN.avgCost();
		double NNavgTime = NN.avgTime();
		double NNsuccessRate = 100*NN.successRate();
		
		double FLavgCost = FL.avgCost();
		double FLavgTime = FL.avgTime();
		double FLsuccessRate = 100*FL.successRate();
		
		double NIavgCost = NI.avgCost();
		double NIavgTime = NI.avgTime();
		double NIsuccessRate = 100*NI.successRate();
		
		String solvers[] = {"NN", "NN-FL", "NI"};
		String winners[] = new String[3]; 
		
		double costs[] = {NNavgCost, FLavgCost, NIavgCost};
		double minCost = BasicFunctions.min(costs);
		String costWinnerStr = null;
		
		double times[] = {NNavgTime, FLavgTime, NIavgTime};
		double minTime = BasicFunctions.min(times);
		String timesWinnerStr = null;
		
		double successs[] = {NNsuccessRate, FLsuccessRate, NIsuccessRate};
		double maxSuccess = BasicFunctions.max(successs);
		String SuccessWinnerStr = null;
		
		String overallWinner = "Unclear";
		
		//Finding cost Winner
		for (int i = 2; i >= 0; i-- ){
			if (costs[i] == minCost){
				if (i == 0)
					costWinnerStr = "NN";
				else if (i == 1)
					costWinnerStr = "NN-FL";
				else if (i == 2)
					costWinnerStr = "NI";
				
			}
		}
		
		
		
		//Finding time Winner
		for (int i = 2; i >= 0; i-- ){
			if (times[i] == minTime){
				if (i == 0)
					timesWinnerStr = "NN";
				else if (i == 1)
					timesWinnerStr = "NN-FL";
				else if (i == 2)
					timesWinnerStr = "NI";
				
			}
		}
		
		
		//Finding Success Winner
		for (int i = 2; i >= 0; i-- ){
			if (successs[i] == maxSuccess){
				if (i == 0)
					SuccessWinnerStr = "NN";
				else if (i == 1)
					SuccessWinnerStr = "NN-FL";
				else if (i == 2)
					SuccessWinnerStr = "NI";
				
			}
		}
		
		//Setting Winners
		for (int i = 0; i < winners.length; i++ ){
				if (i == 0)
					winners[i] = costWinnerStr;
				else if (i == 1)
					winners[i] = timesWinnerStr;
				else if (i == 2)
					winners[i] = SuccessWinnerStr;
		}
				
		//Finding Overall Winner
		for (String solver : solvers){
			int count = 0;
			for (String winner : winners){
				if (solver.equals(winner)){
					count ++;
					if (count == 3){
						overallWinner = solver;
					}
				}
			}
		}
		
		
		System.out.println();
		System.out.println("------------------------------------------------------------");
		System.out.println("           Cost (km)     Comp time (ms)     Success rate (%)");
		System.out.println("------------------------------------------------------------");
		
		System.out.printf("NN%18.2f%19.3f%21.1f\n", NNavgCost, NNavgTime, NNsuccessRate);
		System.out.printf("NN-FL%15.2f%19.3f%21.1f\n", FLavgCost, FLavgTime, FLsuccessRate);
		System.out.printf("NI%18.2f%19.3f%21.1f\n", NIavgCost, NIavgTime, NIsuccessRate);
		
		System.out.println("------------------------------------------------------------");
		System.out.printf("Winner%14s%19s%21s\n", winners[0], winners[1], winners[2]);
		System.out.println("------------------------------------------------------------");
		
		
		System.out.printf("Overall winner: %s\n", overallWinner);
		System.out.println();
		
		
	}
	
	public static void quitProgram() {
		//Quit Program Method
			System.out.println("\nCiao!");
			System.exit(0);
		}

	
	
	public static void printGraphSummary(ArrayList<Graph> G) {
		//Prints summary of graph array
		System.out.println("GRAPH SUMMARY");
		System.out.println("No.    # nodes    # arcs");
		System.out.println("------------------------");
		
		for(Graph graph : G){	
			int index = G.indexOf(graph) + 1;
			System.out.printf("%3d%11d%10d\n", index, graph.getN(), graph.getM());
		}
		
		System.out.println();

		
	}

	public static boolean isGraphLoaded (ArrayList<Graph> G) {
		//Check if graph is loaded
		if (G.isEmpty()){
			System.out.print("\nERROR: No graphs have been loaded!\n\n");
			return false;
		}
		
		return true;
	}
	
	
	
}

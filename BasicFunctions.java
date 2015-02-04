import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class BasicFunctions {

	static BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));

	//getting user input
	public static int getInteger(String prompt, int LB, int UB) {
			//Get an integer in the range [LB, UB] from the user. Prompt the user repeatedly until a valid value is entered.
			int x = 0;
			boolean valid;
			do {
				valid = true;
				System.out.print(prompt);
				try{
					x = Integer.parseInt(cin.readLine());
				}
				catch (NumberFormatException e) {
					System.out.printf("ERROR: Input must be an integer in [%d, %d]!\n\n", LB, UB);
					valid = false;
				}
				catch (IOException e) {
					System.out.printf("ERROR: IO exception!\n");
					valid = false;
				}
				if (valid && (x < LB || x > UB)) {
					valid = false;
					System.out.printf("ERROR: Input must be an integer in [%d, %d]!\n\n", LB, UB);
				}
			} while (!valid);
			return x;
		}
	public static double getDouble(String prompt, double LB, double UB) {
			//Get a real number in the range [LB, UB] from the user. Prompt the user repeatedly until a valid value is entered.
			double x = 0;
			boolean valid;
			do {
				valid = true;
				System.out.print(prompt);
				try{
					x = Double.parseDouble(cin.readLine());
				}
				catch (NumberFormatException e) {
					System.out.printf("ERROR: Input must be a real number in [%.2f, %.2f]!\n\n", LB, UB);
					valid = false;
				}
				catch (IOException e) {
					System.out.printf("ERROR: IO exception!\n");
					valid = false;
				}
				if (valid && (x < LB || x > UB)) {
					valid = false;
					System.out.printf("ERROR: Input must be a real number in [%.2f, %.2f]!\n\n", LB, UB);
				}
			} while (!valid);
			return x;
		}
	public static String getString(){
			
			String str = "";
			try{
				str = cin.readLine();
			}
			catch(IOException e){
				System.out.printf("ERROR: IO exception!\n");
			}
			return str;
		}

	//array conversions
	public static int[] convertStringToIntArray(String[] strings) {
		//Converts array of strings to array of integers
		//Assuming all strings can be parsed to ints
		
		int[] ints = new int[strings.length];
		
		for (int i=0; i < strings.length; i++) {
	        ints[i] = Integer.parseInt(strings[i]);
	    }
		return ints;
	}
	public static int[] convertIntegersToPrimitive(ArrayList<Integer> integers)
	{
	    int[] ints = new int[integers.size()];
	    for (int i=0; i < ints.length; i++)
	    {
	        ints[i] = integers.get(i).intValue();
	    }
	    return ints;
	}

	//Statistics
	public static double average(double[] array){
		double average = 0;
		double sum = 0;
				
		for (int i = 0; i < array.length; i++){
			sum += array[i];
		}	
		average = sum / array.length;
		return average;
	}
	public static double min(double[] array) {
		double min = Double.MAX_VALUE;
		
		for ( int i = 0; i < array.length; i++) {
		    if (array[i] < min) {
		      min = array[i];
		    }
		}
		
		return min;
	}	
	public static double max(double[] array) {
		double max = Double.MIN_VALUE;
		
		for ( int i = 0; i < array.length; i++) {
		    if ( array[i] > max ) {
		      max = array[i];
		    }
		}
		return max;
	}
	public static double stDev(double[] array) {
		double stDev = 0;
				
				
		double mean = BasicFunctions.average(array);
		double squareSum = 0;
		
		for (int i = 0; i < array.length; i++){
			squareSum += Math.pow(array[i] - mean, 2);

		}
		
		stDev = Math.sqrt((squareSum) / (array.length - 1));
		
		if(stDev == 0){
			return Double.NaN;
		}
		else{
			return stDev;
		}
	}

}

package au.edu.unsw.cse.cs9318;

import java.io.IOException;
import java.util.List;

public class Proj1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 6 ) {
			try {
				List<String> inputFile = FileLoadder.loadFile(args[0]);
				List<String> queryFile = FileLoadder.loadFile(args[1]);
				
				List<List<String[]>> input = FileLoadder.processInputFile(inputFile);
				List<String[]> query = FileLoadder.processQueryFile(inputFile);
				int width = Integer.parseInt(args[3]);
				int depth = Integer.parseInt(args[4]);
				int seed = Integer.parseInt(args[5]);
				
                                                                

				
			} catch (NumberFormatException e) {
				handleExceptions(e);
			} catch (IOException e) {
				handleExceptions(e);
			}

		} else {
			System.err.println("===================================================================================");
			System.err.println("** Input arguments error, please input the six arguments in the following order: **");
			System.err.println("** DATAFILE:  The file that contains the data stream items.                      **");
			System.err.println("** QUERYFILE: The file that contains the queries.                                **");
			System.err.println("** METHOD:    It is a string of either \"time\" or \"item\".                         **");
			System.err.println("** WIDTH:     It is the width of each cm-sketch.                                 **");
			System.err.println("** DEPTH:     It is the depth of each cm-sketch.                                 **");
			System.err.println("** SEED:      It is an integer that initiates the random generator in Java.      **");
			System.err.println("===================================================================================");
			System.exit(1);
		}

	}
	

	
	private static void handleExceptions (Exception e) {
		e.printStackTrace();
//		System.err.println(e.getStackTrace());
		System.exit(1);
	}
}
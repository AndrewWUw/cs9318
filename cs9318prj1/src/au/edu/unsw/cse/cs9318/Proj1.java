package au.edu.unsw.cse.cs9318;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Proj1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 6 ) {
			try {
				
				loadFile(args[0]);
				System.out.println();
				loadFile(args[1]);
				String method = args[2];
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
	
	
	private static void loadFile(String fileName) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(new File(
				"SampleData/" + fileName)));
		StringBuffer buffer = new StringBuffer();
		while (true) {
			String str = br.readLine();
			if (str == null) {
				break;
			}
			buffer.append(str);
		}
		br.close();
	}
	
	private static void handleExceptions (Exception e) {
		e.printStackTrace();
//		System.err.println(e.getStackTrace());
		System.exit(1);
	}
}
package au.edu.unsw.cse.cs9318;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FileLoadder {

	/**
	 * 
	 * @param fileName
	 * @return list
	 * @throws IOException
	 */
	public static List<String> loadFile(String fileName) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(new File(
				"SampleData/" + fileName)));
		List<String> list = new ArrayList<String>();
		while (true) {
			String str = br.readLine();
			if (str == null) {
				break;
			}
			list.add(str);
		}
		br.close();
		return list;
	}

	/**
	 * 
	 * @param list
	 * @return resultList
	 */
	public static List<List<String[]>> processInputFile(List<String> list) {
		List<List<String[]>> resultList = new ArrayList<List<String[]>>();
		for (String str : list) {
			List<String[]> l = new ArrayList<String[]>();
			String[] items = str.split(" ");
			for (String s : items) {
				String[] temp = s.split(":");
				l.add(temp);
			}
			resultList.add(l);
		}
		return resultList;
	}

	/**
	 * 
	 * @param list
	 * @return resultList
	 */
	public static List<String[]> processQueryFile(List<String> list) {
		List<String[]> resultList = new ArrayList<String[]>();
		for (String str : list) {
			resultList.add(str.split(" "));
		}
		return resultList;
	}

	// public static void main(String[] args) {
	// try {
	// List<String> l = loadFile("query-a.txt");
	// List<String[]> resultList = new ArrayList<String[]>();
	// resultList = processQueryFile(l);
	// for (String[] s : resultList) {
	// System.out
	// .print("(" + s[0] + ", " + s[1] + ", " + s[2] + ")  ");
	// }
	// System.out.println();
	//
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
}

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
import java.util.Map.Entry;

public class FileLoadder {

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

	public static void processQueryFile(List<String> list) {
		List<Map<String, Integer>> resultList = new ArrayList<Map<String, Integer>>();
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {

		}
	}

	public static void main(String[] args) {
		// String str = "ccacca:1 abbabb:3 bbcbbc:3 bcabca:1 abaaba:5 bbabba:4";
		String str = "k:4 f:1 h:5 b:3 k:1 c:3";
		try {
			List<String> l = loadFile("input-a.txt");
			List<List<String[]>> resultList = new ArrayList<List<String[]>>();
			resultList = processInputFile(l);
			for (List<String[]> list : resultList) {
				for (String[] s : list) {
					System.out.print("(" + s[0] + ", " + s[1] + ")  ");
				}
				System.out.println();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		// String[] s = str.split(" ");
		// for (String ss : s) {
		// System.out.println(ss);
		// System.out.println(ss.split(":")[0]);
		// System.out.println(ss.split(":")[1]);
		// }
	}
}

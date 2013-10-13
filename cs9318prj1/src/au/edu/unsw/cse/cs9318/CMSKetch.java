package au.edu.unsw.cse.cs9318;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CMSKetch {
	private static final int p = 2147483647;

	 public static void main(String[] args) {
		 System.out.println(1 % 2);
	// List<int[]> list = hashFunction(2, 1234);
	// for (int[] s : list) {
	//
	// }
	// System.out.println("k".hashCode());
	// System.out.println((1388524629 * ("k".hashCode()) + 557894633) % p +
	// p);
	// System.out.println(Integer.MAX_VALUE);
	// System.out.println(Float.MAX_VALUE);
	// System.out.println(Long.MAX_VALUE);
	// System.out.println((long) 1388524629 * ("c".hashCode() + 557894633) %
	// p
	// % 32);
	 }

	/**
	 * 
	 * @param input
	 * @param width
	 * @param depth
	 * @param seed
	 * @return
	 */
	private static int[][] countMinSketch(List<String[]> input, int width,
			int depth, int seed) {
		int[][] array = new int[depth][width];
		for (String[] strs : input) {
			List<Integer> l = hashFunction(strs[0], width, depth, seed);
			for (int i = 0; i < depth; i++) {
				int b = l.get(i);
				array[i][b] += Integer.parseInt(strs[1]);
			}
		}
		// for (int i = 0; i < depth; i++) {
		// for (int j = 0; j < width; j++) {
		// System.out.print(array[i][j] + "   ");
		// }
		// System.out.println();
		// }
		return array;
	}

	private static void timeCMSketchs(List<List<String[]>> input, int width,
			int depth, int seed) {

		List<int[][]> list = new ArrayList<int[][]>();
		// Calculate how many arrays Mi requires
		int l = input.size();
		double t1 = Math.log(l) / Math.log(2);
		int t = (int) t1;
		// Init t CMSketches
		for (int i = 0; i < t; i++) {
			list.add(new int[depth][width]);
		}
		
		for (int i = 0; i < l; i++) {
			// for (List<String[]> list : input) {
			int[][] arr = countMinSketch(input.get(i), width, depth, seed);
			// list.set(0, arr);
			while ((i % Math.pow(2, i)) == 0) {
				
				for (int j = 0; j < Math.pow(2, i); j++) {
					int[][] temp = list.get(i);
					list.set(i, arr);
					arr = merge(arr, temp);
					
				}
			}
		}
	}

	/**
	 * @param data
	 * @param wa
	 * @param d
	 * @param seed
	 * @return
	 */
	private static List<Integer> hashFunction(String data, int w, int d,
			int seed) {
		List<Integer> result = new ArrayList<Integer>();
		List<int[]> aAndB = prepareHash(d, seed);
		for (int i = 0; i < d; i++) {
			int x = data.hashCode();
			// System.out.print(i + ": " + x + " ");
			// System.out.print("a" + i + "=" + aAndB.get(0)[i] + "   ");
			// System.out.println("b" + i + "=" +aAndB.get(1)[i]);
			long r = (((long) aAndB.get(0)[i] * x) + aAndB.get(1)[i]) % p;
			// System.out.print(r + "  ");
			if (r >= 0) {
				r = r % w;
			} else {
				r = (r + p) % w;
			}
			System.out.println(r);
			result.add((int) r);
		}
		return result;
	}

	/**
	 * @param depth
	 * @param seed
	 * @return
	 */
	private static List<int[]> prepareHash(int depth, int seed) {
		Random r = new Random(seed);
		int[] a = new int[depth];
		int[] b = new int[depth];
		for (int i = 0; i < depth; ++i) {
			a[i] = 1 + r.nextInt(p - 1);
			b[i] = r.nextInt(p);
			// System.out.println(a[i]);
			// System.out.println(b[i]);
		}
		List<int[]> list = new ArrayList<int[]>();
		list.add(a);
		list.add(b);
		return list;
	}

	public static void timeAggregation(List<List<String[]>> input,
			List<String[]> query, int w, int d, int seed) {
		List<int[][]> arrays = new ArrayList<int[][]>();
		timeCMSketchs(input, w, d, seed);

		for (String[] strs : query) {
			String item = strs[0];
			int startTime = Integer.parseInt(strs[1]);
			int endTime = Integer.parseInt(strs[2]);
			double t1 = Math.log(endTime - startTime + 1) / Math.log(2);
			int t = (int) t1;
			for (int i = startTime; i < endTime; i++) {

			}

		}

	}

	public static void itemAggregration(List<List<String[]>> input,
			List<String[]> query, int w, int d, int seed) {

	}

	private static int[][] merge(int[][] arr1, int[][] arr2) {
		int[][] arr = new int[arr1.length][arr1[0].length];
		for (int i = 0; i < arr1.length; i++) {
			for (int j = 0; j < arr1[0].length; j++) {
				arr[i][j] = arr1[i][j] + arr2[i][j];
			}
		}
		return arr;
	}

	private static void add(int[][] arr1, int[][] arr2) {

	}

}

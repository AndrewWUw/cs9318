package au.edu.unsw.cse.cs9318;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.text.html.parser.Entity;

public class CMSKetch {
	private static final int p = 2147483647;

	public static void main(String[] args) {
		// System.out.println(Math.log(3) / Math.log(2));
		// System.out.println(Math.log(5) / Math.log(2));
		// System.out.println(Math.log(6) / Math.log(2));
		// System.out.println(3 % 4);
		// System.out.println(Math.pow(2, 5));
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
	 * Generate CM array based on input
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
		// printArray(array);
		return array;
	}

	private static List<int[][]> timeCMSketches(List<List<String[]>> input,
			int width, int depth, int seed) {

		List<int[][]> list = new ArrayList<int[][]>();
		// Calculate how many sketches Mi requires
		int l = input.size();
		double t1 = Math.log(l) / Math.log(2);
		int j = (int) t1;
		// System.out.println("j= " + j);
		// Init j Sketches
		for (int i = 0; i <= j; i++) {
			list.add(new int[depth][width]);
		}

		for (int t = 0; t < l; t++) {
			int[][] newArr = countMinSketch(input.get(t), width, depth, seed);

			System.out.println("t= " + t);

			for (int m = 0; m <= j; m++) {

				// System.out.print("  m=" + m + " ");
				// System.out.println("    " + (t + 1) % Math.pow(2, m));

				if ((t + 1) % Math.pow(2, m) == 0) {
					// Update Mi form 0 to m
					for (int k = 0; k <= m; k++) {
						int[][] temp = copyArray(list.get(k));
						System.out.println("temp");
						printArray(temp);
						int[][] arr = copyArray(newArr);
						list.set(k, arr);
						System.out.println("seted value, k=" + k);
						printArray(arr);
						newArr = merge(newArr, temp);
						System.out.println("after merge");
						printArray(newArr);
					}
				}
			}
			// System.out.println();
		}

		for (int[][] arr : list) {
			printArray(arr);
			System.out.println();
		}
		return list;
	}

	/**
	 * Generate CM-Sketch arrays for input data
	 * 
	 * @param input
	 * @param width
	 * @param depth
	 * @param seed
	 * @return
	 */
	private static List<int[][]> itemCMSketches(List<List<String[]>> input,
			int width, int depth, int seed) {
		List<int[][]> list = new ArrayList<int[][]>();

		for (int t = 0; t < input.size(); t++) {
			int[][] array = countMinSketch(input.get(t), width, depth, seed);
			list.add(t, array);

			double t1 = Math.log(t + 1) / Math.log(2);
			int m = (int) t1;

			for (int k = 1; k <= m; k++) {
				int index = (int) (t - Math.pow(2, k));
				if (index >= 0) {
					list.set(index, fold(list.get(index)));
				}
			}
		}
		// for (int[][] l : list) {
		// printArray(l);
		// System.out.println();
		// }
		return list;
	}

	/**
	 * The hash function used is (a*x + b) % p % w
	 * 
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
			long r = (((long) aAndB.get(0)[i] * x) + aAndB.get(1)[i]) % p;
			if (r >= 0) {
				r = r % w;
			} else {
				r = (r + p) % w;
			}
			result.add((int) r);
		}
		return result;
	}

	/**
	 * Output the a,b needs in the hash function, based on depth of the array,
	 * and seed
	 * 
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
		List<int[][]> sketches = timeCMSketches(input, w, d, seed);

		int size = input.size();
		double t1 = Math.log(size) / Math.log(2);
		int t = (int) t1;

		for (String[] strs : query) {
			String item = strs[0];
			int startTime = Integer.parseInt(strs[1]);
			int endTime = Integer.parseInt(strs[2]);
			chooseSketch(sketches.size(), size, startTime, endTime);

			// int[] period = new int[endTime - startTime + 1];
			// for (int i = 0; i <= endTime - startTime; i++) {
			// period[i] = startTime + i;
			// }
			//
			// // calculate the timestamps stored in each array
			// List<int[]> items = new ArrayList<int[]>();
			// List<List<Integer>> itema = new ArrayList<List<Integer>>();
			// for (int j = 0; j < t; j++) {
			// int res = (int) Math.pow(2, j);
			// // int[] range = new int[res];
			// List<Integer> ranges = new ArrayList<Integer>();
			// int span = size / res;
			//
			// for (int i = 0; i < res; i++) {
			// // range[i] = i + span * res;
			// ranges.add(i + span * res);
			// }
			// // items.add(range);
			// itema.add(ranges);
			// }
			//
			// // int[] count = new int[t];
			// List<Integer> count = new ArrayList<Integer>();
			// // Find the location of starttime & endtime
			// for (List<Integer> l : itema) {
			// int cnt = 0;
			// for (int i = 0; i < period.length; i++) {
			// if (l.contains(period[i])) {
			// cnt++;
			// }
			// }
			// count.add(cnt);
			// }

		}

	}

	/**
	 * Perfoem item aggregration on input and query
	 * 
	 * @param input
	 * @param query
	 * @param w
	 * @param d
	 * @param seed
	 */
	public static void itemAggregration(List<List<String[]>> input,
			List<String[]> query, int w, int d, int seed) {
		List<int[][]> sketches = itemCMSketches(input, w, d, seed);
		for (String[] strs : query) {
			String item = strs[0];
			int startTime = Integer.parseInt(strs[1]);
			int endTime = Integer.parseInt(strs[2]);

			List<Integer> location = findMin(sketches, item, w, d, seed);

			for (int i = startTime; i <= endTime; i++) {
				System.out.print(location.get(i) + ",");
			}
			System.out.println();
		}
	}

	/**
	 * Merge two input arrays into one array
	 * 
	 * @param arr1
	 * @param arr2
	 * @return
	 */
	private static int[][] merge(int[][] arr1, int[][] arr2) {
		int[][] arr = new int[arr1.length][arr1[0].length];
		for (int i = 0; i < arr1.length; i++) {
			for (int j = 0; j < arr1[0].length; j++) {
				arr[i][j] = arr1[i][j] + arr2[i][j];
			}
		}
		return arr;
	}

	/**
	 * Fold the input array by half
	 * 
	 * @param arr1
	 * @return
	 */
	private static int[][] fold(int[][] arr1) {
		int depth = arr1.length;
		int width = arr1[0].length / 2;
		int[][] res = new int[depth][width];

		for (int i = 0; i < res.length; i++) {
			for (int j = 0; j < res[i].length; j++) {
				res[i][j] = arr1[i][j] + arr1[i][j + (width)];
			}
		}
		return res;
	}

	/**
	 * Find the min value from input CM-Sketch array
	 * 
	 * @param sketches
	 * @param str
	 * @param w
	 * @param d
	 * @param seed
	 * @return
	 */
	private static List<Integer> findMin(List<int[][]> sketches, String str,
			int w, int d, int seed) {
		List<Integer> res = new ArrayList<Integer>();

		for (int[][] sketch : sketches) {
			List<Integer> l = hashFunction(str, sketch[0].length, d, seed);
			int min = Integer.MAX_VALUE;
			for (int i = 0; i < sketch.length; i++) {
				if (min > sketch[i][l.get(i)]) {
					min = sketch[i][l.get(i)];
				}
			}
			res.add(min);
		}
		return res;
	}

	private static List<Double> chooseSketch(int sketchCount,
			int timeStampCount, int startTime, int endTime) {
		int[] period = new int[endTime - startTime + 1];
		for (int i = 0; i <= endTime - startTime; i++) {
			period[i] = startTime + i;
		}

		// calculate the timestamps stored in each sketch
		List<List<Integer>> item = new ArrayList<List<Integer>>();
		for (int j = 0; j < timeStampCount; j++) {
			int res = (int) Math.pow(2, j);
			List<Integer> range = new ArrayList<Integer>();
			int span = timeStampCount / res;

			for (int i = 0; i < res; i++) {
				range.add(i + span * res);
			}
			// items.add(range);
			item.add(range);
		}

		// Decide which sketch to be use for output,
		// based on the proportion of queried time stamps in each sketch
		List<Double> count = new ArrayList<Double>();
		for (List<Integer> l : item) {
			int cnt = 0;
			for (int i = 0; i < period.length; i++) {
				if (l.contains(period[i])) {
					cnt++;
				}
			}
			count.add((cnt * 1.0) / (l.size() * 1.0));
		}

		return count;
	}

	private static int[][] copyArray(int[][] arr) {
		int[][] res = new int[arr.length][arr[0].length];

		for (int i = 0; i < res.length; i++) {
			for (int j = 0; j < res[0].length; j++) {
				res[i][j] = arr[i][j];
			}
		}
		// System.out.println("new arr");
		// printArray(res);
		return res;
	}

	private static void printArray(int[][] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				if (arr[i][j] < 10) {
					System.out.print(arr[i][j] + "   ");
				} else {
					System.out.print(arr[i][j] + "  ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
}

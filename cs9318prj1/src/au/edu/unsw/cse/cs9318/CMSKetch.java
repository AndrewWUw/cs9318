package au.edu.unsw.cse.cs9318;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.text.html.parser.Entity;

public class CMSKetch {
	private static final int p = 2147483647;

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
		return array;
	}

	/**
	 * Generate CM-Sketch arrays from time aggregation
	 * 
	 * @param input
	 * @param width
	 * @param depth
	 * @param seed
	 * @return
	 */
	private static List<int[][]> timeCMSketches(List<List<String[]>> input,
			int width, int depth, int seed) {

		List<int[][]> list = new ArrayList<int[][]>();
		// Calculate how many sketches Mi requires
		int l = input.size();
		double t1 = Math.log(l) / Math.log(2);
		int j = (int) t1;
		// Init j Sketches
		for (int i = 0; i <= j; i++) {
			list.add(new int[depth][width]);
		}

		for (int t = 0; t < l; t++) {
			int[][] newArr = countMinSketch(input.get(t), width, depth, seed);
			for (int m = 0; m <= j; m++) {
				if ((t + 1) % Math.pow(2, m) == 0) {
					// Update Mi form 0 to m
					int[][] temp = copyArray(list.get(m));
					int[][] arr = copyArray(newArr);
					list.set(m, arr);
					newArr = merge(newArr, temp);
				}
			}
		}
		return list;
	}

	private static List<Sketch> timeCMSketchess(List<List<String[]>> input,
			int width, int depth, int seed) throws CloneNotSupportedException {

		List<int[][]> list = new ArrayList<int[][]>();

		// Calculate how many sketches Mi requires
		int l = input.size();
		double t1 = Math.log(l) / Math.log(2);
		int j = (int) t1;
		List<Sketch> lists = new ArrayList<Sketch>();

		for (int t = 0; t < l; t++) {
			// System.out.println("t=" + t);
			int[][] newArr = countMinSketch(input.get(t), width, depth, seed);
			Sketch sketch = new Sketch(countMinSketch(input.get(t), width,
					depth, seed));

			for (int m = 0; m <= j; m++) {
				if ((t + 1) % Math.pow(2, m) == 0) {
					// Update Mi form 0 to m
					int[][] temp = copyArray(list.get(m));
					int[][] arr = copyArray(newArr);
					list.set(m, arr);

					Sketch sketch2 = new Sketch(arr);
					sketch2.setStartTime(0);
					sketch2.setEndTime(0);
					sketch2.setPeriod(0);
					lists.set(m, sketch2);
					sketch.setSketch(merge(newArr, temp));

					newArr = merge(newArr, temp);
				}
			}
		}
		return lists;
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
		}
		List<int[]> list = new ArrayList<int[]>();
		list.add(a);
		list.add(b);
		return list;
	}

	public static void timeAggregation(List<List<String[]>> input,
			List<String[]> query, int w, int d, int seed) {
		int size = input.size();
		double t1 = Math.log(size) / Math.log(2);
		int t = (int) t1;
		List<int[][]> sketches = timeCMSketches(input, w, d, seed);

		for (String[] strs : query) {
			String item = strs[0];
			int startTime = Integer.parseInt(strs[1]);
			int endTime = Integer.parseInt(strs[2]);
			List<Integer> count = chooseSketch(sketches.size(), size,
					startTime, endTime);
			List<Integer> value = findMin(sketches, item, w, d, seed);

			for (int i = 0; i <= t; i++) {
				double v = value.get(i) / Math.pow(2, i);
				value.set(i, (int) v);
			}

			List<Integer> output = prepareOutput(count, value, endTime
					- startTime + 1);

			for (int i = output.size() - 1; i >= 0; i--) {
				System.out.print(output.get(i) + ",");
			}
			System.out.println();
		}
	}

	/**
	 * Perform item aggregation on input and query
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

			List<Integer> value = findMin(sketches, item, w, d, seed);

			for (int i = startTime; i <= endTime; i++) {
				System.out.print(value.get(i) + ",");
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

	private static List<Integer> chooseSketch(int sketchCount, int totalTimes,
			int startTime, int endTime) {
		// Find the time stamps stored in each sketch
		List<List<Integer>> item = new ArrayList<List<Integer>>();

		for (int j = 0; j < sketchCount; j++) {
			int res = (int) Math.pow(2, j);
			int span = totalTimes / res - 1;
			int count = 0;
			List<Integer> range = new ArrayList<Integer>();
			for (int s = 0; s < res; s++) {
				range.add(s + span * res);
			}
			item.add(range);
		}

		int[] period = new int[endTime - startTime + 1];
		for (int i = 0; i <= endTime - startTime; i++) {
			period[i] = startTime + i;
		}

		List<Integer> count = new ArrayList<Integer>();
		for (List<Integer> l : item) {
			int cnt = 0;
			for (int i = 0; i < period.length; i++) {
				for (int a : l) {
					if (a == period[i]) {
						cnt++;
					}
				}
			}
			count.add(cnt);
		}
		return count;
	}

	private static List<Integer> prepareOutput(List<Integer> count,
			List<Integer> value, int length) {
		List<Integer> output = new ArrayList<Integer>();
		int cnt = 0;
		if (count.get(0) == 1) {
			output.add(value.get(0));
			cnt++;
		} else {
			if (count.get(1) == 2) {
				output.add(value.get(1));
				cnt++;
			}
		}
		if (count.get(1) > 0) {
			output.add(value.get(1));
			cnt++;
		}
		for (int k = 2; k < count.size(); k++) {
			if (count.get(k) > 0) {
				for (int j = 0; j < count.get(k); j++) {
					if (cnt < length) {
						output.add(value.get(k));
						cnt++;
					}
				}
			}
		}
		return output;
	}

	private static int[][] copyArray(int[][] arr) {
		int[][] res = new int[arr.length][arr[0].length];

		for (int i = 0; i < res.length; i++) {
			for (int j = 0; j < res[0].length; j++) {
				res[i][j] = arr[i][j];
			}
		}
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

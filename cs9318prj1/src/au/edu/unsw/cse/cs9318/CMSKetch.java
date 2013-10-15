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

	private static List<int[][]> timeCMSketchs(List<List<String[]>> input,
			int width, int depth, int seed) {

		List<int[][]> list = new ArrayList<int[][]>();
		// List<Double> update = new ArrayList<Double>();
		// Calculate how many arrays Mi requires
		int l = input.size();
		double t1 = Math.log(l) / Math.log(2);
		int j = (int) t1;
		// Init j CMSketches
		for (int i = 0; i <= j; i++) {
			list.add(new int[depth][width]);
			// update.add(Math.pow(2, i));
		}

		for (int t = 0; t < l; t++) {
			int[][] newArr = countMinSketch(input.get(t), width, depth, seed);
			double ts = Math.pow(2, t + 1);
			// list.set(0, newArr);

			for (int m = 0; m <= j; m++) {
				// if (Math.pow(2, m) == ts) {
				if ((t + 1) % Math.pow(2, m) == 0) {
					for (int k = 0; k <= m; k++) {
						int[][] temp = list.get(k);
						int[][] arr = newArr;
						list.set(k, arr);
						newArr = merge(newArr, temp);
					}
				}
			}
		}

		for (int[][] arr : list) {
			for (int i = 0; i < arr.length; i++) {
				for (int f = 0; f < arr[0].length; f++) {
					System.out.print(arr[i][f] + "   ");
				}
				System.out.println();
			}
			System.out.println();
		}
		return list;
	}

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

		for (int[][] l : list) {
			for (int i = 0; i < l.length; i++) {
				for (int j = 0; j < l[i].length; j++) {
					System.out.print(l[i][j] + "   ");
				}
				System.out.println();
			}
			System.out.println();
		}
		return list;
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
			// System.out.println(r);
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
		List<int[][]> sketches = timeCMSketchs(input, w, d, seed);

		int size = input.size();
		double t1 = Math.log(size) / Math.log(2);
		int t = (int) t1;

		for (String[] strs : query) {
			String item = strs[0];
			int startTime = Integer.parseInt(strs[1]);
			int endTime = Integer.parseInt(strs[2]);
			int[] period = new int[endTime - startTime + 1];
			for (int i = 0; i <= endTime - startTime; i++) {
				period[i] = startTime + i;
			}

			// calculate the timestamps stored in each array
			List<int[]> items = new ArrayList<int[]>();
			List<List<Integer>> itema = new ArrayList<List<Integer>>();
			for (int j = 0; j < t; j++) {
				int res = (int) Math.pow(2, j);
				// int[] range = new int[res];
				List<Integer> ranges = new ArrayList<Integer>();
				int span = size / res;

				for (int i = 0; i < res; i++) {
					// range[i] = i + span * res;
					ranges.add(i + span * res);
				}
				// items.add(range);
				itema.add(ranges);
			}

			// int[] count = new int[t];
			List<Integer> count = new ArrayList<Integer>();
			// Find the location of starttime & endtime
			for (List<Integer> l : itema) {
				int cnt = 0;
				for (int i = 0; i < period.length; i++) {
					if (l.contains(period[i])) {
						cnt++;
					}
				}
				count.add(cnt);
			}

		}

	}

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

	private static int[][] merge(int[][] arr1, int[][] arr2) {
		int[][] arr = new int[arr1.length][arr1[0].length];
		for (int i = 0; i < arr1.length; i++) {
			for (int j = 0; j < arr1[0].length; j++) {
				arr[i][j] = arr1[i][j] + arr2[i][j];
			}
		}
		return arr;
	}

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
	 * 
	 * @param sketches
	 * @param str
	 * @param w
	 * @param d
	 * @param seed
	 * @return
	 */
	private static List<Integer> findMin(List<int[][]> sketches, String str, int w,
			int d, int seed) {
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
			min = Integer.MAX_VALUE;
		}
		return res;
	}

	private static void chooseSketch(int sketchCount, int timeStampCount,
			int startTime, int endTime) {
		int[] period = new int[endTime - startTime + 1];
		for (int i = 0; i <= endTime - startTime; i++) {
			period[i] = startTime + i;
		}

		// calculate the timestamps stored in each sketch
		List<int[]> items = new ArrayList<int[]>();
		List<List<Integer>> item = new ArrayList<List<Integer>>();
		for (int j = 0; j < timeStampCount; j++) {
			int res = (int) Math.pow(2, j);
			// int[] range = new int[res];
			List<Integer> ranges = new ArrayList<Integer>();
			int span = size / res;

			for (int i = 0; i < res; i++) {
				// range[i] = i + span * res;
				ranges.add(i + span * res);
			}
			// items.add(range);
			item.add(ranges);
		}

		// int[] count = new int[t];
		List<Integer> count = new ArrayList<Integer>();
		// Find the location of starttime & endtime
		for (List<Integer> l : item) {
			int cnt = 0;
			for (int i = 0; i < period.length; i++) {
				if (l.contains(period[i])) {
					cnt++;
				}
			}
			count.add(cnt);
		}
	}
}

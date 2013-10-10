package au.edu.unsw.cse.cs9318;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CMSKetche {
	private static final int p = 2147483647;

	public static void main(String[] args) {
		// List<int[]> list = hashFunction(2, 1234);
		// for (int[] s : list) {
		//
		// }
		// System.out.println("k".hashCode());
		System.out.println((1388524629 * ("k".hashCode()) + 557894633) % p + p);
		System.out.println(Integer.MAX_VALUE);
		System.out.println(Float.MAX_VALUE);
		System.out.println(Long.MAX_VALUE);
		System.out.println((long) 1388524629 * ("c".hashCode() + 557894633) % p
				% 32);
	}

	public static void countMinSketch(List<List<String[]>> input, int width,
			int depth, int seed) {
		int[][] array = new int[width][depth];
		for (List<String[]> list : input) {
			for (String[] strs : list) {
				
			}
		}
	}

	/**
	 * 
	 * @param data
	 * @param w
	 * @param d
	 * @param seed
	 * @return
	 */
	public static List<Integer> hashFunction(String data, int w, int d, int seed) {
		List<Integer> result = new ArrayList<Integer>();
		List<int[]> aAndB = prepareHash(d, seed);
		for (int i = 0; i < d; i++) {
			int x = data.hashCode();
			int[] arr = aAndB.get(i);
			long r = (((long) arr[0] * x) + arr[1]) % p;
			if (r >= 0) {
				r = r % w;
			} else {

			}
			result.add((int) r);
		}

		return result;
	}

	private static List<int[]> prepareHash(int depth, int seed) {
		Random r = new Random(seed);
		int[] a = new int[depth], b = new int[depth];
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
}

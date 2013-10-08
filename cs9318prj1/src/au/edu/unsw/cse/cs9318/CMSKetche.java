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
	}

	public static void countMinSketch() {
	}

	public static List<int[]> hashFunction(List<String[]> data,
			List<int[]> aAndB, int w) {
		List<int[]> result = new ArrayList<int[]>();
		for (String[] list : data) {
			int[] arr = new int[data.size()];
			for (int i = 0; i < list.length; i++) {
				int x = list[i].hashCode();
				int a = ((aAndB.get(0)[i] * x) + aAndB.get(1)[i]) % p;
				if (a >= 0) {

				} else {

				}
			}

			result.add(arr);

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

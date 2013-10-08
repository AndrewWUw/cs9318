package au.edu.unsw.cse.cs9318;

import java.util.Random;

public class CMSKetche {

	public static void hashFunction(int depth, int seed) {

		Random r = new Random(seed);
		int a[] = null, b[] = null;
		for (int i = 0; i < depth; ++i) {
			a[i] = 1 + r.nextInt();
			b[i] = r.nextInt();

		}
	}

	public static void main(String[] args) {

	}
}

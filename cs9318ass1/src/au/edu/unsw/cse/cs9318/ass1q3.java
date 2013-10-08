package au.edu.unsw.cse.cs9318;

import java.lang.Math;

public class ass1q3 {
	private static final double[] input = { 13, 15, 16, 19, 20, 21, 22, 25, 33,
			35, 36, 40, 45, 46, 52, 70 };

	// private static final double[] input = { 1, 3, 9, 13, 17 };

	public static void main(String[] args) {
//		int[] a = { 1, 1, 2, 2, 0, 0, 0, 1, 2, 2, 1, 1, 2, 2, 0, 0, 0, 0, 1, 2, 0 };
//		int[] b = { 1, 2, 1, 2, 1, 2, 2, 0, 0, 0, 1, 2, 1, 2, 0, 0, 1, 2, 0, 0, 0 };
//		int[] c = { 1, 1, 1, 2, 1, 1, 2, 1, 1, 2, 0, 0, 0, 0, 1, 2, 0, 0, 0, 0, 0 };
//		int[] d = null;
//		for (int i = 0; i < a.length; i++) {
//			System.out.println(9 * a[i] + 3 * b[i] + 2 * c[i]);
//		}
//		
//		double[][] opt = new double[4][16];
//		// double[][] opt = new double[4][5];
//		double avg = 0;
//
//		for (int j = 0; j < input.length; j++) {
//			avg = calculateAvg(input, j);
//			opt[0][j] = calculateSSE(avg, j);
//		}
//
//		for (int i = 1; i < 4; i++) {
//			for (int j = 0; j < 16 - i; j++) {
//				opt[i][j] = calculateOpt(opt, i, j);
//			}
//		}
//		double sum = 0;
//		for (int i = 0; i < 4; i++) {
//			for (int j = 0; j < 16; j++) {
//				System.out.print(opt[i][j] + ", ");
//			}
//			System.out.println();
//		}
//		for (int j = 0; j < 16; j++) {
//			sum += opt[3][j];
//		}
//		System.out.println(sum);
		
		double[] r = { 13, 15, 16, 19, 20, 21, 22, 25 };
//		double[] r = { 33,	35,	36,	40,	45,	46 };
		double avg = calculateAvg(r, 0);
		System.out.println(avg); 
//		System.out.print(calculateSSE(calculateAvg(r, 0), 0)); 
		
		double sse = 0;
		for (int i = 0; i < r.length; i++) {
			sse += Math.pow(r[i] - avg, 2);
		}
		System.out.println(sse);
	}

	public static double calculateAvg(double[] in, int i) {
		double sum = 0;
		for (int j = i; j < in.length; j++) {
			sum += in[j];
		}
		return sum / (in.length - i);
	}

	public static double calculateAvgBackward(double[] in, int i) {
		double sum = 0;
		for (int j = 0; j <= i; j++) {
			sum += in[j];
		}
		return sum / (i + 1);
	}

	public static double calculateSSE(double avg, int position) {
		double sse = 0;
		for (int i = position; i < input.length; i++) {
			sse += Math.pow(input[i] - avg, 2);
		}
		return sse;
	}

	public static double calculateSSEBackward(double[] in, double avg,
			int position) {
		double sse = 0;
		for (int i = 0; i <= position; i++) {
			sse += Math.pow(in[i] - avg, 2);
		}
		return sse;
	}

	public static double calculateOpt(double[][] opt, int bin, int position) {
		double[] temp = new double[16 - position - 1];
		double[] arr = new double[16 - position];
		for (int i = 0; i < 16 - position; i++) {
			arr[i] = input[position + i];
		}

		for (int i = 0; i < arr.length - 1; i++) {
			double avg = calculateAvgBackward(arr, i);
			if (position + i + 1 < 16) {
				double o = opt[bin - 1][position + i + 1];
				double sse = calculateSSEBackward(arr, avg, i);
				temp[i] = sse + o;
			} else {
				temp[i] = calculateSSEBackward(arr, avg, i);
			}
		}

		double value = temp[0];
		for (int i = 0; i < temp.length; i++) {
			if (value > temp[i]) {
				value = temp[i];
			}
		}

		return value;
	}
}
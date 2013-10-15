/***************************************************************
 * 
 * 
 * 
 ***************************************************************/
package au.edu.unsw.cse.cs9318;

import java.util.Arrays;

public class Sketch {

	private int[][] sketch;
	private int startTime;
	private int endTime;
	private int period;

	public int[][] getSketch() {
		return sketch;
	}

	public void setSketch(int[][] sketch) {
		this.sketch = sketch;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public Sketch(int[][] sketch, int startTime, int endTime, int period) {
		super();
		this.sketch = sketch;
		this.startTime = startTime;
		this.endTime = endTime;
		this.period = period;
	}

	public Sketch(int[][] sketch, int startTime, int endTime) {
		super();
		this.sketch = sketch;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Sketch(int[][] sketch) {
		super();
		this.sketch = sketch;
		this.startTime = 0;
		this.endTime = 0;
		this.period = 0;
	}

	public Sketch() {
		super();
	}

	@Override
	public String toString() {
		return ", startTime=" + startTime + ", endTime=" + endTime
				+ ", period=" + period + "\n" + Arrays.toString(sketch);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		int[][] arr = new int[this.sketch.length][this.sketch[0].length];
		for (int i = 0; i < this.sketch.length; i++) {
			for (int j = 0; j < this.sketch[0].length; j++) {
				arr[i][j] = this.sketch[i][j];
			}
		}
		return new Sketch(arr, this.startTime, this.endTime, this.period);
	}
}

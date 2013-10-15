/***************************************************************
 * 
 * 
 * 
 ***************************************************************/
package au.edu.unsw.cse.cs9318;

public class Sketch {

	private int[][] sketch;
	private int startTime;
	private int endTime;

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
	}
}

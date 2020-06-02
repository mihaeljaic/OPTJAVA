package hr.fer.zemris.optjava.dz3.tempSchedule;

public class GeometricTempSchedule implements ITempSchedule {
	private double alpha;
	private double tCurrent;
	private int innerLimit;
	private int outerLimit;
	private int k;
	
	public GeometricTempSchedule(double alpha, double tInitial, int innerLimit, int outerLimit) {
		super();
		this.alpha = alpha;
		this.innerLimit = innerLimit;
		this.outerLimit = outerLimit;
		tCurrent = tInitial;
	}

	public double getNextTemperature() {	
		return tCurrent *= Math.pow(alpha, k++);
	}

	public int getInnerLoopCounter() {
		return innerLimit;
	}

	public int getOuterLoopCounter() {
		return outerLimit;
	}

}

package hr.fer.zemris.optjava.dz2.funkcije;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class PrijenosnaFunkcija implements IHFunction {
	private double[][] values;
	private int variableCount = 6;
	private static final double epsilon = 1e-9;
	
	public PrijenosnaFunkcija(double[][] values) {
		super();
		this.values = values;
	}

	@Override
	public int getVariableCount() {
		return variableCount;
	}

	@Override
	public double valueInPoint(RealVector point) {
		double result = 0;
		double a = point.getEntry(0);
		double b = point.getEntry(1);
		double c = point.getEntry(2);
		double d = point.getEntry(3);
		double e = point.getEntry(4);
		double f = point.getEntry(5);
		
		for (int i = 0; i < values.length; i++) {
			double x1 = values[i][0];
			double x2 = values[i][1];
			double x3 = values[i][2];
			double x4 = values[i][3];
			double x5 = values[i][4];
			double y = values[i][5];
			result += Math.pow(a * x1 + b * x1 * x1 * x1 * x2 + c * Math.exp(d * x3) * (1 + Math.cos(e * x4)) + f * x4 * x5 * x5 - y, 2) / values.length;
		}
		
		return result;
	}
	
	@Override
	public RealVector gradientValueInPoint(RealVector point) {
		double[] gradient = new double[variableCount];
		
		for (int i = 0; i < variableCount; i++) {
			RealVector temp = new ArrayRealVector(point);
			temp.setEntry(i, temp.getEntry(i) + epsilon);
			gradient[i] = (valueInPoint(temp) - valueInPoint(point)) / epsilon;
		}
		
		return new ArrayRealVector(gradient);
	}

	@Override
	public RealMatrix hesseMatrixInPoint(RealVector point) {
		double[][] h = new double[variableCount][variableCount];
		
		RealVector gradient = gradientValueInPoint(point);
		for (int i = 0; i < variableCount; i++) {
			RealVector temp = new ArrayRealVector(point);
			temp.setEntry(i, point.getEntry(i) + epsilon);
			RealVector d = gradientValueInPoint(temp);
			for (int j = 0; j < variableCount; j++) {
				h[i][j] = (d.getEntry(j) - gradient.getEntry(j)) / epsilon;
			}
		}
		
		return MatrixUtils.createRealMatrix(h);
	}

}

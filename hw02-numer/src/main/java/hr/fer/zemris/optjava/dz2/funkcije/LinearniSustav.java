package hr.fer.zemris.optjava.dz2.funkcije;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class LinearniSustav implements IHFunction {
	private double[][] values;
	private RealMatrix hesseMatrix;
	private int variablesCount;
	
	public LinearniSustav(double[][] values) {
		super();
		this.values = values;
		variablesCount = values[0].length - 1;
		calculateHesseMatrix();
	}
	
	private void calculateHesseMatrix() {
		double[][] data = new double[variablesCount][variablesCount];
		for (int i = 0; i < variablesCount; i++) {
			for (int j = 0; j < variablesCount; j++) {
				for (int k = 0; k < values.length; k++) {
					data[i][j] += 2 * values[k][i] * values[k][j];
				}
			}
		}
		
		hesseMatrix = MatrixUtils.createRealMatrix(data);
	}

	@Override
	public int getVariableCount() {
		return variablesCount;
	}

	@Override
	public double valueInPoint(RealVector point) {
		double value = 0;
		for (int i = 0; i < values.length; i++) {
			double temp = 0;
			for (int j = 0; j < variablesCount; j++) {
				temp += values[i][j] * point.getEntry(j);
			}
			temp -= values[i][variablesCount];
			value += temp * temp;
		}
		
		return value / variablesCount;
	}

	@Override
	public RealVector gradientValueInPoint(RealVector point) {
		double[] gradient = new double[variablesCount];
		for (int i = 0; i < variablesCount; i++) {
			for (int j = 0; j < values.length; j++) {
				double bracketValue = 0;
				for (int k = 0; k < variablesCount; k++) {
					bracketValue += values[j][k] * point.getEntry(k);
				}
				bracketValue -= values[j][variablesCount];
				
				gradient[i] += 2 * values[j][i] * bracketValue / variablesCount;
			}
		}
		
		return new ArrayRealVector(gradient);
	}

	@Override
	public RealMatrix hesseMatrixInPoint(RealVector point) {
		return hesseMatrix;
	}
	
}

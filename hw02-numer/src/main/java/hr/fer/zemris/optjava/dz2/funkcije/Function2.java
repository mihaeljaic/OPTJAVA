package hr.fer.zemris.optjava.dz2.funkcije;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class Function2 implements IHFunction {

	public int getVariableCount() {
		return 2;
	}

	public double valueInPoint(RealVector point) {
		double x1 = point.getEntry(0);
		double x2 = point.getEntry(1);
		
		return (x1 - 1) * (x1 - 1) + 10 * (x2 - 2) * (x2 - 2);
	}

	public RealVector gradientValueInPoint(RealVector point) {
		double x1Partial = 2 * (point.getEntry(0) - 1);
		double x2Partial = 20 * (point.getEntry(1) - 2);
		
		return new ArrayRealVector(new double[]{x1Partial, x2Partial});
	}

	public RealMatrix hesseMatrixInPoint(RealVector point) {
		double[][] hMatrix = new double[2][2];
		hMatrix[0][0] = 2;
		hMatrix[0][1] = 0;
		hMatrix[1][0] = 0;
		hMatrix[1][1] = 20;
		
		return MatrixUtils.createRealMatrix(hMatrix);
	}

}

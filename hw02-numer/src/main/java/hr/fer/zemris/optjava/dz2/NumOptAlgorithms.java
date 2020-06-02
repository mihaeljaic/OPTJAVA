package hr.fer.zemris.optjava.dz2;

import java.util.List;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import hr.fer.zemris.optjava.dz2.funkcije.IFunction;
import hr.fer.zemris.optjava.dz2.funkcije.IHFunction;


public class NumOptAlgorithms {
	public static final int BISECTION_MAX_ITERATIONS = 1000;
	
	private interface DirectionVectorGetter {
		public RealVector calculateDirectionVector(IFunction function, RealVector point);
	}
	
	public static RealVector gradientDescent(IFunction function, int maxIterations, RealVector x0, List<RealVector> solutions) {
		return getMinimum(function, maxIterations, x0, (f, p) -> {
			RealVector gradient = f.gradientValueInPoint(p);
			RealVector d = new ArrayRealVector(f.getVariableCount());
			for (int i = 0; i < f.getVariableCount(); i++) {
				d.setEntry(i, -gradient.getEntry(i));
			}
			
			return d;
		}, solutions);
	}
	
	public static RealVector newtonMethod(IHFunction function, int maxIterations, RealVector x0, List<RealVector> solutions) {
		return getMinimum(function, maxIterations, x0, (f, point) -> {
			RealMatrix hInverse = MatrixUtils.inverse(((IHFunction)f).hesseMatrixInPoint(point));
			RealVector gradient = f.gradientValueInPoint(point);
			
			RealVector d = hInverse.operate(gradient);
			for (int i = 0; i < d.getDimension(); i++) {
				d.setEntry(i, -d.getEntry(i));
			}
			
			return d;
		}, solutions);
	}
	
	private static RealVector getMinimum(IFunction function, int maxIterations, RealVector x0, DirectionVectorGetter dGetter, List<RealVector> solutions) {
		for (int k = 0; k < maxIterations; k++) {
			solutions.add(x0);
			if (isFunctionMinium(function, x0)) {
				return x0;
			}
			
			RealVector d = dGetter.calculateDirectionVector(function, x0);

			double lambda = calculateLambda(function, x0, d);
			RealVector x1 = new ArrayRealVector(function.getVariableCount());
			for (int i = 0; i < x1.getDimension(); i++) {
				x1.setEntry(i, x0.getEntry(i) + lambda * d.getEntry(i));
			}
			
			System.out.println("Iteracija " + (k + 1) + ", rješenje: " + x1);
			x0 = x1;
		}
		
		return x0;
	}
	
	private static boolean isFunctionMinium(IFunction function, RealVector point) {
		RealVector gradient = function.gradientValueInPoint(point);
		for (int i = 0; i < gradient.getDimension(); i++) {
			if (Math.abs(gradient.getEntry(i)) > 1e-4) {
				return false;
			}
		}
		
		return true;
	}
	
	private static double calculateLambda(IFunction function, RealVector x0, RealVector d) {
		double lambdaLower = 0;
		double lambdaUpper = 1;
		
		while (thetaFunctionDifferentiationInPoint(function, x0, d, lambdaUpper) <= 0) {
			lambdaUpper *= 2;
		}
		
		double lambda = 0;
		for (int k = 0; k < BISECTION_MAX_ITERATIONS; k++) {
			lambda = (lambdaLower + lambdaUpper) / 2;
			double result = thetaFunctionDifferentiationInPoint(function, x0, d, lambda);
			if (Math.abs(result) < 1e-3) {
				return lambda;
			} 
			
			if (result > 0) {
				lambdaUpper = lambda;
			} else {
				lambdaLower = lambda;
			}
		}
		
		return lambda;
	}
	
	private static double thetaFunctionDifferentiationInPoint(IFunction function, RealVector x0, RealVector d, double lambda) {
		RealVector point = new ArrayRealVector(function.getVariableCount());
		for (int i = 0; i < point.getDimension(); i++) {
			point.setEntry(i, x0.getEntry(i) + lambda * d.getEntry(i));
		}
		RealVector gradient = function.gradientValueInPoint(point);
		
		double result = 0;
		for (int i = 0, len = function.getVariableCount(); i < len; i++) {
			result += gradient.getEntry(i) * d.getEntry(i);
		}
		
		return result;
	}
	
}

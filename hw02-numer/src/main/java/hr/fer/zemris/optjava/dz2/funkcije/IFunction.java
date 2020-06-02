package hr.fer.zemris.optjava.dz2.funkcije;

import org.apache.commons.math3.linear.RealVector;

public interface IFunction {

	public int getVariableCount();
	
	public double valueInPoint(RealVector point);
	
	public RealVector gradientValueInPoint(RealVector point);
	
}

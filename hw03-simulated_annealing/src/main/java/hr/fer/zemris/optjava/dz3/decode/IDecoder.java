package hr.fer.zemris.optjava.dz3.decode;

public interface IDecoder<T> {

	double[] decode(T data);
	
	void decode(T data, double[] point);
	
}

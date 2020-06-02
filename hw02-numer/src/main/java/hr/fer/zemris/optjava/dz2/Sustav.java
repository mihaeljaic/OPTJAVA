package hr.fer.zemris.optjava.dz2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import hr.fer.zemris.optjava.dz2.funkcije.IHFunction;
import hr.fer.zemris.optjava.dz2.funkcije.LinearniSustav;

public class Sustav {
	
	public static void main(String[] args) throws IOException {
		if (args.length != 3) {
			System.out.println("Argumenti...");
			return;
		}
		
		int maxIterations = Integer.parseInt(args[1]);
		List<String> lines = Files.readAllLines(Paths.get(args[2]), StandardCharsets.UTF_8);
		IHFunction function = parseFunction(lines);
		
		double[] random = new double[function.getVariableCount()];
		for (int i = 0; i < random.length; i++) {
			random[i] = nextDouble(-5, 5);
		}
		RealVector x0 = new ArrayRealVector(random);
		
		RealVector result;
		if (args[0].toLowerCase().equals("grad")) {
			result = NumOptAlgorithms.gradientDescent(function, maxIterations, x0, new ArrayList<>());
		} else {
			result = NumOptAlgorithms.newtonMethod(function, maxIterations, x0, new ArrayList<>());
		}
		
		System.out.println("Pronađeno rješenje: " + result);
		System.out.println("Pogreška: " + function.valueInPoint(result));
	}
	
	private static IHFunction parseFunction(List<String> lines) {
		List<String[]> temp = new ArrayList<>();
		for (String line : lines) {
			if (line.trim().isEmpty() || line.startsWith("#")) {
				continue;
			}
			
			temp.add(line.substring(1, line.length() - 1).split(",\\s*"));
		}
		
		int rows = temp.size();
		int columns = temp.get(0).length;
		double[][] values = new double[rows][columns];
		for (int i = 0; i < rows; i++) {
			int j = 0;
			for (String s : temp.get(i)) {
				values[i][j++] = Double.parseDouble(s);
			}
		}
		
		return new LinearniSustav(values);
	}
	
	private static double nextDouble(double from, double to) {
		return from + (to - from) * Math.random(); 
	}
	
}

package hr.fer.zemris.optjava.dz2;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import hr.fer.zemris.optjava.dz2.funkcije.Function1;
import hr.fer.zemris.optjava.dz2.funkcije.Function2;
import hr.fer.zemris.optjava.dz2.funkcije.IHFunction;

public class Jednostavno {

	public static void main(String[] args) throws IOException {
		if (args.length != 2 && args.length != 4) {
			System.out.println("Očekujem 2 ili 4 argumenta...");
			return;
		}
		
		int maxIterations = Integer.parseInt(args[1]);
		
		IHFunction function;
		if (args[0].startsWith("1")) {
			function = new Function1();
		} else {
			function = new Function2();
		}
		
		RealVector x0;
		if (args.length == 4) {
			double d1 = Double.parseDouble(args[2]);
			double d2 = Double.parseDouble(args[3]);
			
			x0 = new ArrayRealVector(new double[]{d1, d2});
		} else {
			double d1 = nextDouble(-5, 5);
			double d2 = nextDouble(-5, 5);
			
			x0 = new ArrayRealVector(new double[]{d1, d2});
		}
		
		RealVector result;
		List<RealVector> solutions = new ArrayList<>();
		if (args[0].matches("[0-9]a")) {
			result = NumOptAlgorithms.gradientDescent(function, maxIterations, x0, solutions);
		} else {
			result = NumOptAlgorithms.newtonMethod(function, maxIterations, x0, solutions);
		}
		
		System.out.println("Konačno rješenje: " + result);
		drawSolution(function, solutions, args);
	}
	
	private static void drawSolution(IHFunction function, List<RealVector> solutions, String[] args) throws IOException {
		BufferedImage image = new BufferedImage(440, 440, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = (Graphics2D) image.getGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, image.getWidth() - 1, image.getHeight() - 1);
		
		g.setColor(Color.BLACK);
		g.drawLine(220, 10, 220, 430);
		g.drawLine(10, 220, 430, 220);
		
		int[] xAxisXArrowCoordinates = new int[] {425, 435, 425};
		int[] xAxisYArrowCoordinates = new int[] {215, 220, 225};
		drawArrow(xAxisXArrowCoordinates, xAxisYArrowCoordinates, g);
		
		int[] yAxisXArrowCoordinates = new int[] {215, 220, 225};
		int[] yAxisYArrowCoordinates = new int[] {15, 5, 15};
		drawArrow(yAxisXArrowCoordinates, yAxisYArrowCoordinates, g);
		
		g.drawString("x", 425, 235);
		g.drawString("y", 210, 25);
		
		drawPoints(g, solutions);
		
		ImageIO.write(image, "png", new File("./" + args[0] + args[1] + ".png"));
	}
	
	private static void drawArrow(int[] xPoints, int[] yPoints, Graphics2D g2d) {
		Polygon p = new Polygon(xPoints, yPoints, 3);

		g2d.fillPolygon(p);
	}
	
	private static void drawPoints(Graphics2D g, List<RealVector> solutions) {
		g.setColor(Color.RED);
		
		int n = solutions.size();
		int[] xPoints = new int[n];
		int[] yPoints = new int[n];
		
		for (int i = 0; i < n; i++) {
			RealVector v = solutions.get(i);
			xPoints[i] = 220 + (int) Math.round(40 * v.getEntry(0));
			yPoints[i] = 220 - (int) Math.round(40 * v.getEntry(1));
		}
		
		g.drawPolyline(xPoints, yPoints, n);
		
		g.setColor(Color.BLUE);
		g.drawString("start", xPoints[0], yPoints[0] + 10);
		g.drawString("end", xPoints[n-1], yPoints[n-1] + 10);
		
		g.setColor(Color.BLACK);
		g.drawLine(xPoints[0], 220, xPoints[0], 225);
		g.drawLine(xPoints[n-1], 220, xPoints[n-1], 225);
		g.drawLine(220, yPoints[0], 215, yPoints[0]);
		g.drawLine(220, yPoints[n-1], 215, yPoints[n-1]);
		
		RealVector startV = solutions.get(0);
		RealVector endV = solutions.get(n - 1);
		String startX = Math.abs(startV.getEntry(0)) < 1e-4 ? "" : String.format("%.1f", startV.getEntry(0));
		String startY = Math.abs(startV.getEntry(1)) < 1e-4 ? "" : String.format("%.1f", startV.getEntry(1));
		String endX = Math.abs(endV.getEntry(0)) < 1e-4 ? "" : String.format("%.1f", endV.getEntry(0));
		String endY = Math.abs(endV.getEntry(1)) < 1e-4 ? "" : String.format("%.1f", endV.getEntry(1));
		FontMetrics fm = g.getFontMetrics();
		
		g.drawString(startX, xPoints[0] - fm.stringWidth(startX) / 2, 240);
		g.drawString(startY, 210 - fm.stringWidth(startY), yPoints[0] + 5);
		g.drawString(endX, xPoints[n-1] - fm.stringWidth(endX) / 2, 240);
		g.drawString(endY, 210 - fm.stringWidth(endY), yPoints[n-1] + 5);
	}
	
	private static double nextDouble(double from, double to) {
		return from + (to - from) * Math.random(); 
	}
	
}

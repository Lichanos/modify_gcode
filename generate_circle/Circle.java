package generate_circle;

import Jama.*;
import java.util.Arrays;
import java.util.List;

public class Circle {
	
	public static double[] getCircle(List<Double> coord1, List<Double> coord2, List<Double> coord3) {
		double x1 = coord1.get(0);
		double x2 = coord2.get(0);
		double x3 = coord3.get(0);
		double y1 = coord1.get(1);
		double y2 = coord2.get(1);
		double y3 = coord3.get(1);
		int[] one = {1,2,3};
		int[] two = {0,2,3};
		int[] three = {0,1,3};
		int[] four = {0,1,2};
		double[][] array = {{1, 1, 1, 1}, {Math.pow(x1, 2) + Math.pow(y1, 2), x1, y1, 1}, 
			{Math.pow(x2, 2) + Math.pow(y2, 2), x2, y2, 1}, {Math.pow(x3, 2)+Math.pow(y3, 2), x3, y3, 1}};
		System.out.println(Arrays.deepToString(array));
		Matrix full = new Matrix(array);
		double centerx = .5*full.getMatrix(one, two).det()/full.getMatrix(one, one).det();
		double centery = -.5*full.getMatrix(one, three).det()/full.getMatrix(one, one).det();
		double radius = Math.pow(centerx, 2) + Math.pow(centery, 2) + full.getMatrix(one, four).det()/full.getMatrix(one, one).det();
		double[] circle = {centerx, centery, radius};
		System.out.println(Arrays.toString(circle));
		return circle;		
	}
	
	
}

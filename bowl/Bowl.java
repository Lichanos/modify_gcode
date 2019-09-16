package bowl;
import gcode_editor.Editor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import generate_circle.Circle;

public class Bowl {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		List<Double> coord1 = new ArrayList<>();
		List<Double> coord2 = new ArrayList<>();
		List<Double> coord3 = new ArrayList<>();
		System.out.println("Point 1"); coord1 = getCoordinates(input);
		System.out.println("Point 2"); coord2 = getCoordinates(input);
		System.out.println("Point 3"); coord3 = getCoordinates(input);
		double[] circle = Circle.getCircle(coord1, coord2, coord3);
		print(circle, coord1);
	}
		
	public static List<Double> getCoordinates(Scanner input){
		List<Double> coord = new ArrayList<>();
		coord.add(Editor.getDouble("x coordinate of point on circle", input));
		coord.add(Editor.getDouble("y coordinate of point on circle", input));
		return coord;
	}
	
	public static void print(double[] circle, List<Double> point) {
		Scanner input = new Scanner(System.in);
		String filename = Editor.getString("Name of output file:", input);
		try {
			File newFile = new File(filename + ".gcode");
			PrintWriter printWriter = new PrintWriter (newFile);  
			double xpos = circle[0];
			double ypos = circle[1];
			double radius = Math.sqrt(circle[2]);
			double arc = 2*Math.PI/36;
			double startArc;
			double[] x = new double[36];
			double[] y = new double[36];
			System.out.println(point.get(0) + ", " + point.get(1));
			System.out.println(xpos + ", " + ypos);
			if(point.get(0) >= 0) {
				startArc = Math.atan((point.get(1)-ypos)/(point.get(0)-xpos));
				//System.out.println((point.get(1)-ypos)/(point.get(0)-xpos));
				//System.out.println(startArc);
			}
			else {
				startArc = Math.atan(((point.get(1)-ypos)/(point.get(0)-xpos)))+Math.PI;
				//System.out.println((point.get(1)-ypos)/(point.get(0)-xpos)+Math.PI);
				//System.out.println(startArc);
			}
			double e = 0;
			double startingZ = Editor.getDouble("Starting z value:", input);
			double z = startingZ;
			while(radius > 5) {
				printWriter.print(";LAYER\n");
				printWriter.print("F7200\nG1 Z" + z + " F3600\n");
				for(int ii = 0; ii < 36; ii++) {
					x[ii] = (Math.cos(startArc+arc*ii))*radius;
					y[ii] = (Math.sin(startArc+arc*ii))*radius;
					printWriter.print("G1 X" + (x[ii] + xpos) + " Y" + (y[ii] + ypos) + " E" + e + "\n");
					e += 5*radius/18; 
				}
				printWriter.print("G1 X" + (x[0] + xpos) + " Y" + (y[0] + ypos) + " E" + e + "\n");
				z += .8;
				radius -= 1.3;//= Math.abs(radius)/Math.pow(Math.E, z-startingZ);
				if(z > 50)
					break;
			}
			//standards for the last loop, shortcuts radius to 5
			radius = 5;
			printWriter.print(";LAYER\n");
			printWriter.print("F7200\nG1 Z" + z + " F3600\n");
			for(int ii = 0; ii < 36; ii++) {
				x[ii] = (Math.cos(startArc+arc*ii))*radius;
				y[ii] = (Math.sin(startArc+arc*ii))*radius;
				printWriter.print("G1 X" + (x[ii] + xpos) + " Y" + (y[ii] + ypos) + " E" + e + "\n");
				e += 5*radius/18; 
			}
			printWriter.print("G1 X" + (x[0] + xpos) + " Y" + (y[0] + ypos) + " E" + e + "\n");
			printWriter.close();
		} catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	}
}

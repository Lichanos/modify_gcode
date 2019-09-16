package gcode_editor;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;

public class Editor {
	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String filepath = getString("Input full file path", input);
		String filename = getString("Input file name", input);
		File file = new File(filepath + "\\" + filename + ".gcode");
		double zReduce = getDouble("Reduce Z value by: ", input);
		double eReduce = getDouble("Reduce E value by: ", input);
		boolean genBase = getBoolean("Generate a base from the file? (If \"no\", will run as expected", input);
		double scaleX = 1;
		double scaleY = 1;
		boolean scale = getBoolean("Scale the piece horizontally?", input);
		if(scale) {
			scaleX = getDouble("How much to scale in the x direction? (1.00 is no change)", input);
			scaleY = getDouble("How much to scale in the y direction?", input);
		}
		if(!genBase) {
		    try {
		    	
		    	File editFile = new File (filepath + "\\" +  filename + "_EDIT.gcode");
		        PrintWriter printWriter = new PrintWriter (editFile);  
		        
		        Scanner sc = new Scanner(file);
		        String clear;
		        String temp;
		        String edit;
		        double zPlaceholder;
		        double ePlaceholder;
		        double xVal;
		        double xVal2;
		        double yVal;
		        double yVal2;
		        while (sc.hasNext()) {
		            clear = sc.next();
		            if(clear.startsWith("Z")) {
		            	if(clear.startsWith("Z0"))
		            		printWriter.print(clear + " ");
		            	else {
		            		temp = clear.substring(1);
		            		zPlaceholder = Double.parseDouble(temp);
		            		edit = "Z" + (zPlaceholder - zReduce);
		            		printWriter.print(edit + " ");
		            	}
		            }
		            else if(clear.startsWith("E")) {
		            	if(clear.startsWith("E0"))
		            		printWriter.print(clear + " ");
		            	else {
		            		temp = clear.substring(1);
		            		ePlaceholder = Double.parseDouble(temp);
		            		edit = "E" + (ePlaceholder - eReduce);
		            		printWriter.print(edit + " ");
		            	}
		            }
		            else if(clear.startsWith("S"))
		            	printWriter.print(clear + " ");
		            else if(clear.startsWith("G") || clear.startsWith("M") || clear.startsWith(";")) {
		            	printWriter.print("\n" + clear + " ");
		            }
		            else if(clear.startsWith("X")) {
		            	xVal = Double.parseDouble(clear.substring(1));
		            	xVal2 = scale(scaleX, scaleY, xVal, "x");
		            	printWriter.print("X" + xVal2 + " ");
		            }
		            else if(clear.startsWith("Y")) {
		            	yVal = Double.parseDouble(clear.substring(1));
		            	yVal2 = scale(scaleX, scaleY, yVal, "y");
		            	printWriter.print("Y" + yVal2 + " ");
		            }
		            else {
		            	printWriter.print(clear + " ");
		            }
		            
		        }
		        sc.close();
		        printWriter.close ();
		    } 
		    catch (FileNotFoundException e) {
		        e.printStackTrace();
		    }
		}
		else {
			genBase(zReduce, eReduce, scaleX, scaleY, file, filename, filepath);
		}
	}
	
	public static void genBase(double zReduce, double eReduce, double scaleX, double scaleY, File file, String filename, String filepath) {
		try {
	    	Scanner input = new Scanner(System.in);
	    	File editFile = new File (filepath + "\\" +  filename + "_EDIT.gcode");
	    	double eStep = getDouble("What's the total extrusion value of the base layer?", input);
	        PrintWriter printWriter = new PrintWriter (editFile);  
	        String clear;
	        String temp;
	        String edit;
	        double zPlaceholder;
	        double ePlaceholder;
	        double xVal;
	        double yVal;
	        double xVal2;
	        double yVal2;
	        for(int ii = 0; ii < 4; ii++) {
		        Scanner sc = new Scanner(file);
		        while (sc.hasNext()) {
		            clear = sc.next();
		            if(clear.startsWith("Z")) {
		            	if(clear.startsWith("Z0"))
		            		printWriter.print(clear + " ");
		            	else {
		            		temp = clear.substring(1);
		            		zPlaceholder = Double.parseDouble(temp);
		            		edit = "Z" + (zPlaceholder - zReduce);
		            		printWriter.print(edit + " ");
		            	}
		            }
		            else if(clear.startsWith("E")) {
		            	if(clear.startsWith("E0"))
		            		printWriter.print(clear + " ");
		            	else {
		            		temp = clear.substring(1);
		            		ePlaceholder = Double.parseDouble(temp);
		            		edit = "E" + (ePlaceholder - eReduce);
		            		printWriter.print(edit + " ");
		            	}
		            }
		            else if(clear.startsWith("S"))
		            	printWriter.print(clear + " ");
		            else if(clear.startsWith("G") || clear.startsWith("M") || clear.startsWith(";")) {
		            	printWriter.print("\n" + clear + " ");
		            }
		            else if(clear.startsWith("X")) {
		            	xVal = Double.parseDouble(clear.substring(1));
		            	xVal2 = scale(scaleX, scaleY, xVal, "x");
		            	printWriter.print("X" + xVal2 + " ");
		            }
		            else if(clear.startsWith("Y")) {
		            	yVal = Double.parseDouble(clear.substring(1));
		            	yVal2 = scale(scaleX, scaleY, yVal, "y");
		            	printWriter.print("Y" + yVal2 + " ");
		            }
		            else {
		            	printWriter.print(clear + " ");
		            }
		        }
		        sc.close();
		        if(ii != 0) {
		        	zReduce -= 1.5;
		        }
		        eReduce -= eStep;
			}
	        printWriter.close ();
	    } 
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	}
	
	public static double scale(double scaleX, double scaleY, double xy, String xory) {
		if(xory.equals("x"))
			xy = xy*scaleX;
		else if(xory.equals("y"))
			xy = xy*scaleY;
		return xy;
	}
	
	public static double getDouble(String prompt, Scanner input){
		System.out.println(prompt);
		return input.nextDouble();
	}
   
	public static String getString(String prompt, Scanner input){
		System.out.println(prompt);
		return input.nextLine();
	}
	
	public static boolean getBoolean(String prompt, Scanner input){
		input.reset();
		System.out.println(prompt);
		String boolResponse = input.next();
		if(boolResponse.toLowerCase().startsWith("y")){
			return true;
		}
		else{
			return false;
		}
	}

}

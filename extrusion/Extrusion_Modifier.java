package extrusion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import gcode_editor.Editor;

enum Operation{
	ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION;
}

public class Extrusion_Modifier {
	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String filepath = Editor.getString("Filepath:", input);
		String filename = Editor.getString("Filename:", input);
		String trueFile = filepath + "\\" + filename;
		File file = new File(trueFile + ".gcode");
		String clearOperation = Editor.getString("Operation: (\"addition\", "
				+ "\"subtraction\", \"multiplication\", \"division\")", input);
		Operation o = null;
		if(clearOperation.toLowerCase().startsWith("a"))
			 o = Operation.ADDITION;
		else if(clearOperation.toLowerCase().startsWith("s"))
			o = Operation.SUBTRACTION;
		else if(clearOperation.toLowerCase().startsWith("m"))
			o = Operation.MULTIPLICATION;
		else if(clearOperation.toLowerCase().startsWith("d"))
			o = Operation.DIVISION;
		else {
			System.out.println("Something went wrong. Exiting.");
			System.exit(0);
		}
		double value = Editor.getDouble("Value:", input);
		
		mod(file, trueFile, o, value);
	}
	
	public static void mod(File file, String trueFile, Operation o, double value){
		File editFile = new File(trueFile + "_mod.gcode");
		try {
			PrintWriter printWriter = new PrintWriter (editFile);  
	        Scanner sc = new Scanner(file);
	        
	        String clearLine;
	        String checkedLine;
	        String subLine;
	        String eTerm = "";
	        String startLine = "";
	        String endLine = "";
	        boolean isE = false;
	        double eValue = 0;
	        while(sc.hasNextLine()) {
	        	clearLine = sc.nextLine();
	        	for(int ii = 0; ii < clearLine.length(); ii++) {
	        		subLine = clearLine.substring(ii);
	        		if(subLine.startsWith("E")) {
	        			if(subLine.length() < 5) {
	        				eTerm = subLine;
	        			}
	        			else {
	        				eTerm = subLine.substring(0, 5);
	        				endLine = clearLine.substring(ii+5);
	        			}
	        			startLine = clearLine.substring(0, ii);
	        			isE = true;
	        			break;
	        		}
	        	}
	        	if(isE) {
	        		if(eTerm.substring(1,2).equals("1") || eTerm.substring(1,2).equals("2") 
	        				|| eTerm.substring(1,2).equals("3") || eTerm.substring(1,2).equals("4") 
	        				|| eTerm.substring(1,2).equals("5") || eTerm.substring(1,2).equals("6") 
	        				|| eTerm.substring(1,2).equals("7") || eTerm.substring(1,2).equals("8") 
	        				|| eTerm.substring(1,2).equals("9")) {
	        			eValue = Double.parseDouble(eTerm.substring(1));
			        	if(o == Operation.ADDITION) 
			        		eValue += value;
			        	else if(o == Operation.MULTIPLICATION)
			        		eValue *= value;
			        	else if(o == Operation.SUBTRACTION)
			        		eValue -= value;
			        	else
			        		eValue /= value;
			        	checkedLine = startLine + "E" + eValue + endLine;
			        	printWriter.print(checkedLine + "\n");
	        		}
	        		else {
	        			printWriter.print(clearLine + "\n");
	        		}
	        		isE = false;
	        	}
	        	else {
	        		printWriter.print(clearLine + "\n");
	        	}
	        	
	        }
	        printWriter.close();
	        sc.close();
	        
		} catch(FileNotFoundException e) {
	        e.printStackTrace(); 
		}
	}
	
}

package duplicator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import gcode_editor.Editor;

public class Duplicator {
	static String runningLog = "";
	public static void main(String[] args){
		Scanner input = new Scanner(System.in);
		String filepath = Editor.getString("Filepath:", input);
		String filename = Editor.getString("Filename:", input);
		String trueFile = filepath + "\\" + filename;
		File file = new File(trueFile + ".gcode");
		int numPieces = getNumDupes("How many pieces? (Either 4 or 9, arranged in a grid)", input);
		if(numPieces > 6.5) {numPieces = 9;}
		else { numPieces = 4;}
		double maxX = 0;
		double minX = 0;
		double minY = 0;
		double maxY = 0;
		double maxZ = 0;
		String temp = "";
		double tempNum = 0;
		try {
			Scanner sc = new Scanner(file);
			while(sc.hasNext()) {
				temp = sc.next();
				if(temp.startsWith("X")){
					tempNum = Double.parseDouble(temp.substring(1));
					if(tempNum > maxX) {
						maxX = tempNum;
					}
					else if(tempNum < minX) {
						minX = tempNum;
					}
				}
				else if(temp.startsWith("Y")) {
					tempNum = Double.parseDouble(temp.substring(1));
					if(tempNum > maxY) {
						maxY = tempNum;
					}
					else if(tempNum < minY) {
						minY = tempNum;
					}
				}
				else if(temp.startsWith("Z")) {
					tempNum = Double.parseDouble(temp.substring(1));
					if(tempNum > maxZ) {
						maxZ = tempNum;
					}
				}
			}		
			double xRange = maxX-minX;
			double yRange = maxY-minY;
			double xValue =0;
			double yValue =0;
			for(int ii = 0; ii < (int)Math.sqrt(numPieces); ii++) {
				for(int jj = 0; jj < (int)Math.sqrt(numPieces); jj++) {
					//System.out.println(numPieces);
					if(numPieces == 4) {
						xValue = (-xRange*1.5)*.5 + (xRange*1.5*ii);
						yValue = (-yRange*1.5)*.5 + (yRange*1.5*jj);
						duplicate(file, xValue, yValue);
						runningLog = runningLog + ("Z" + maxZ*1.2 + "\n");
					}
					else if(numPieces == 9) {
						xValue = -xRange*1.5 + (xRange*1.5*ii);
						yValue = -yRange*1.5 + (yRange*1.5*jj);
						duplicate(file, xValue, yValue);
						runningLog = runningLog + ("Z" + maxZ*1.2 + "\n");
					}
					else {
						System.out.println("????");
					}
				}
			}
			File editFile = (numPieces == 4) ? new File(trueFile + "_2x2.gcode"): new File(trueFile + "_3x3.gcode");
			PrintWriter printWriter = new PrintWriter (editFile);
			printWriter.print("M140 S60\nM104 T0 S0\nM190 S60\nG28\nG92 E0\nG1 Z0 F2000\n"
					+ "G1 X1.87806531250001 Y0.0 Z0.928125 E4.325094131323642\nF1500\n");
			printWriter.print(runningLog);
			printWriter.print("M84\nG28\nM84\nM140 S0\nM104 T0 S0");
			printWriter.close();
			sc.close();
		} catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
	public static int getNumDupes(String prompt, Scanner input) {
		System.out.println(prompt);
		return (int)input.nextDouble();
	}
	
	public static void duplicate(File file, double dX, double dY) throws FileNotFoundException{
		Scanner sc = new Scanner(file);
		//System.out.println(dX + ", " + dY);
		String clearLine;
		String checkedLine;
		String xTerm = "";
		String yTerm = "";
		double xValue = 0;
		double yValue = 0;
		boolean layer = false;
		while(sc.hasNext()) {
			clearLine = sc.next();
			if(clearLine.startsWith(";")) {
				runningLog = runningLog + (String)(sc.nextLine());
			}
			else if(clearLine.startsWith("X")) {
				xTerm = clearLine.substring(1);
				xValue = Double.parseDouble(xTerm);
		    	checkedLine = "X" + (xValue + dX);
		    	runningLog = runningLog + (String)(checkedLine + " ");
			}
			else if(clearLine.startsWith("Y")) {
				if(clearLine.startsWith("YE")) {layer = true;}
				if(!layer) {
					yTerm = clearLine.substring(1);
					//System.out.println(yTerm);
					yValue = Double.parseDouble(yTerm);
		    		checkedLine = "Y" + (yValue + dY);
		    		//System.out.println(yValue);
		    		//System.out.println(dY);
		    		//System.out.println(checkedLine);
		    		//System.out.println(yValue + dY);
		    		runningLog = runningLog + (String)(checkedLine + " ");
				}
				
			}
			else if(clearLine.startsWith("G") || clearLine.startsWith("Z")){
				runningLog = runningLog + (String)(clearLine + " ");
			}
			else {
				runningLog = runningLog + (String)(clearLine + "\n");
				//System.out.println(clearLine);
			}
		}
		runningLog = runningLog + ";new\n";
		sc.close();
	}
}

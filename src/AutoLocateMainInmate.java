/*
 * This project will read in a csv file (file path is input as a runtime arg)
 * and will return the California representatives for the districts of the input address
 * 
 * Needs Selenium as a dependency
 * 
 * TO DO:
 * test time: idea: create a new driver because maybe the cache gets really big and slows down the program?
 * 
 * Created because I got frustrated at my internship locating hundreds of people... (Bohr-ingggg)
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class AutoLocateMainInmate {
	final String inputFilePath;
	final String outputFilePath;
	
	public AutoLocateMainInmate(String inputFile, String outputFile) throws IOException
	{
		inputFilePath = inputFile;
		outputFilePath = outputFile;
		
	}
	
	public void run() throws IOException
	{
		sudoMain(inputFilePath, outputFilePath);
	}
	
	public static void sudoMain(String inputFile, String outputFile) throws IOException 
	{
		
		//construct object to locate rep info
		AutoLocateInmateDriver autoLocateInmateDriver = new AutoLocateInmateDriver();
		
		//maintain list for output
		LinkedList<LinkedList<String>> outputInmateInfo = new LinkedList<LinkedList<String>>();
		
		//set file form file path (the argument
		File file = new File(inputFile);
		
		//read file one line at a time
		try{ 
			//holds information for a single query
			LinkedList<String> singleInmateInfo;
			
			BufferedReader br = new BufferedReader(new FileReader(file));
			//int counter = 0;
			for (String line;(line = br.readLine()) != null; ) {
				//skip empty strings, fixes a bug
				if(line.isEmpty()) continue;
				
				//new ll for each inmate
				singleInmateInfo = new LinkedList<String>();
				
				singleInmateInfo.add(line);
				
				
				String[] inmateInfo = autoLocateInmateDriver.getInmateInfo(line);
				
				//the null case else it worked
				if(inmateInfo == null) inmateError(singleInmateInfo);
				else inmateWorked(inmateInfo, singleInmateInfo);
				
				//add info to main LL
				outputInmateInfo.add(singleInmateInfo);
				
				//System.out.print(++counter+ " ");
				
				/*if(counter%500 == 0){
					System.out.println("printing: " + counter);
					print(outputRepInfo,counter);
					outputRepInfo.clear();
				}*/
			}
			//close the buffered reader
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		
		//close the WebDriver
		autoLocateInmateDriver.quitDriver();
		
		
		//print out information
		print(outputInmateInfo, outputFile);
      
		// System.out.println("Done");
	}
	
	//print out information
	private static void print(LinkedList<LinkedList<String>> LL, String filePath) throws IOException
	{
		FileWriter output = new FileWriter(filePath+"/InmateLocatorOutput.csv"); 
    	BufferedWriter out = new BufferedWriter(output);
    	
    	//title for document
    	out.write("CDCR Number, LastName, FirstName, age, admission date, PrisonName");
    	out.newLine();
    	 //for each string in each linked list
    	for(LinkedList<String> ll: LL)
    	{
    		for(String s: ll)
    		{
    			out.write(s + ",");
    		}
    		out.newLine();
    	}
    	
    	out.flush();
        out.close();
	}
	
	private static void inmateError(LinkedList<String> singleInmateInfo)
	{
		singleInmateInfo.add("the inmate could not be located");
	}
	
	private static void inmateWorked(String[] repInfo, LinkedList<String> singleRepInfo)
	{
		for(String s: repInfo)
		{
			singleRepInfo.add(s);
		}
	}
}

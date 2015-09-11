/*
 * This project will read in a csv file (file path is input as a runtime arg)
 * and will return the California representatives for the districts of the input address
 * 
 * Needs Selenium as a dependency
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

public class AutoLocateMainRep {
	final String inputFilePath;
	final String outputFilePath;
	
	public AutoLocateMainRep(String inputFile, String outputFile) throws IOException
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
		AutoLocateRepDriver autoLocateDriver = new AutoLocateRepDriver();
		
		//maintain list for output
		LinkedList<LinkedList<String>> outputRepInfo = new LinkedList<LinkedList<String>>();
		
		//set file form file path (the argument
		File file = new File(inputFile);
		try{ //read file one line at a time
			//holds information for a single query
			LinkedList<String> singleRepInfo;
			
			BufferedReader br = new BufferedReader(new FileReader(file));
			for (String line;(line = br.readLine()) != null; ) {
				//new ll for each address
				singleRepInfo = new LinkedList<String>();
				
				String[] strArray = line.split(",");
				String street = strArray[0]; 
				String city = strArray[1];
				String zip = strArray[2];

				//add information to LL 
				singleRepInfo.add(street);
				singleRepInfo.add(city);
				singleRepInfo.add(zip);
				
				//use info from line to get representative information, an array is returned
				//the array is either a null pointer or has 4 indices (district, assembly woman, distrcit, senator)
				String[] repInfo = autoLocateDriver.getRepInfo(street, city, zip);
				
				//the null case else it worked
				if(repInfo == null) addressError(singleRepInfo);
				else addressWorked(repInfo, singleRepInfo);
				
				//add info to main LL
				outputRepInfo.add(singleRepInfo);
			}
			//close the buffered reader
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		
		//close the WebDriver
		autoLocateDriver.quitDriver();
		
		
		//print out information
		FileWriter output = new FileWriter(outputFile + "/RepresentativeLocatorOutput.csv"); 
    	BufferedWriter out = new BufferedWriter(output);
    	
    	//title for document
    	out.write("Address, city, zip code, District, Assemblyman , District, Senator");
    	out.newLine();
    	 //for each string in each linked list
    	for(LinkedList<String> ll: outputRepInfo)
    	{
    		for(String s: ll)
    		{
    			out.write(s + ",");
    		}
    		out.newLine();
    	}
    	
    	out.flush();
        out.close();
        //System.out.println("Done");
	}
	
	//if no rep located, this is output
	private static void addressError(LinkedList<String> singleRepInfo)
	{
		singleRepInfo.add("The given address did not work");
	}
	
	//if rep located this is the output
	private static void addressWorked(String[] repInfo, LinkedList<String> singleRepInfo)
	{
		String string; //place holder 
		//print out the information, cutting the strings to right specifications
		//first string -- cut out part of begining and end
		string = repInfo[0]; 
		System.out.println(string.length());
		//if district is one digit (i.e. 7)
		if(string.toCharArray().length == 27){
			string = string.substring(25,26);
		//if district is two digits (23)
		} else if (string.toCharArray().length == 28){
			string = string.substring(25,27);
		}
		
		repInfo[0] = string;
		
		//second string -- cut out parts
		string = repInfo[1];
		string = string.substring(15, (string.length() - 4));
		
		repInfo[1] = string;
		
		//third string -- more of the same, need to account for digits in districts again
		string  = repInfo[2];
		System.out.println(string.length());
		if(string.toCharArray().length == 25){
			string = string.substring(23, 24);
		} else if (string.toCharArray().length == 26){
			string = string.substring(23, 25);
		}
		repInfo[2] = string;	
		
		//fourth string -- ""
		string = repInfo[3];
		string = string.substring(7, string.length()-4);
		repInfo[3] = string;
		
		//add strings to ll
		for(String s: repInfo)
		{
			singleRepInfo.add(s);
		}
	}
}

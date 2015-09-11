import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;


public class AutoLocateInmateDriver {
	private WebDriver driver;
	
	
	public AutoLocateInmateDriver()
	{
		driver = new FirefoxDriver();
		driver.get("http://inmatelocator.cdcr.ca.gov/");
		//accept user agreement
		(driver.findElement(By.name("ctl00$LocatorPublicPageContent$btnAccept"))).click();
		
		
	}
	
	public String[] getInmateInfo(String cdcr)
	{
		//is page the accept conditions page? if so need to accept (sometimes reloads if process 1000s of cdcr #'s
		if(isElementExistAgree(driver)){
			(driver.findElement(By.name("ctl00$LocatorPublicPageContent$btnAccept"))).click();
			System.out.println("Had to accept user agreement");
		}
		
		//input cdcr number
		WebElement inmateNumber = driver.findElement(By.name("ctl00$LocatorPublicPageContent$txtCDCNumber")); 
		inmateNumber.sendKeys(cdcr);
		
		//send query
		WebElement search = driver.findElement(By.name("ctl00$LocatorPublicPageContent$btnSearch"));
		search.click();
		
		//is page the accept conditions page? if so need to accept (sometimes reloads if process 1000s of cdcr #'s
		if(isElementExistAgree(driver)){
			(driver.findElement(By.name("ctl00$LocatorPublicPageContent$btnAccept"))).click();
			System.out.println("Had to accept user agreement ##2");
			inmateNumber.sendKeys(cdcr);
			search.click();
		}
		
		//if cdcr number exists, then element exists, else new search
		if(isElementExistInmateInfo(driver)){
			WebElement inmateInfo = driver.findElement(By.id("ctl00_LocatorPublicPageContent_gvGridView"));
			
			//get information: return 3 lines, only need middle line
			String stringToCut = inmateInfo.getText();
			String[] stringsToReturn = cutString(stringToCut);
			
			//return to search page
			WebElement newSearch = driver.findElement(By.id("NewSearch"));
			newSearch.click();
			
			
			return stringsToReturn;
		}
			//return to search page if cdcr does not exist
			WebElement newSearch = driver.findElement(By.id("NewSearch"));
			newSearch.click();
			
			return null;
	}
	
	//inspiration from Flo Bayer: http://goo.gl/qb6rfq
	//does element exist?
	private static boolean isElementExistInmateInfo(WebDriver driver) {
	    if(driver.findElements(By.id("ctl00_LocatorPublicPageContent_gvGridView")).size() != 0){
	        return true;
		} else {
	        return false;
	    }
	}
	
	//is the webpage to accept agreement loaded?
	private static boolean isElementExistAgree(WebDriver driver){
		if(driver.findElements(By.name("ctl00$LocatorPublicPageContent$btnAccept")).size() != 0){
			return true;
		} else {
			return false;
		}
		
	}
	
	//the string pulled from the web is too much info
	private static String[] cutString(String string)
	{
		//get middle string from output
		String[] strings = string.split("\n");
		string = strings[1];
		strings = null;
		
		//...then split that string by spaces each token stored in seperate index of array
		strings = string.split("\\s+");
		
		//i will represent the current index of the string
		int i = 0;
		
		//need to find the last word that is in the first name since some first names
		//contain more than one word i.i Mc donald -- this was a bug
		//should be using stringBuilder but performance of this part is negligible wrt Selenium speed
		String lastName = "";
		
		//while word does not contain comma, add to lastName
		while(!containsComma(strings[i])) 
		{
			lastName += " " + strings[i++]; 
		}
		
		//add the last word to last name (has comma so need to substring	
		lastName += " "+ strings[i].substring(0, (strings[i].length()-1));i++;

		String firstName = strings[i++];
		
		//I know that the next thing after name has numbers, so this will add all names
		while(!containsNumbers(strings[i]))
		{
			//bad performance but shouldn't occur often so not a big issue -- Stringbuilder would be better
			firstName += " " + strings[i++];
		}
		
		//increment past the CDCR number in string
		i++;
		
		//get given information
		String age = strings[i++];
		String admissionDate = strings[i++];
	
		
		//the prison name can be different lengths so taking that into acccount here
		int lastIndexForPrisonName = strings.length - 1; //want the last index b/c of how substring works
		
		String prisonName = "";
		
		while( i < lastIndexForPrisonName)
		{
			prisonName += strings[i] + " ";
			i++;
		}
		
		String[] stringsToReturn =  new String[5];
		stringsToReturn[0] = lastName;
		stringsToReturn[1] = firstName;
		stringsToReturn[2] = age;
		stringsToReturn[3] = admissionDate;
		stringsToReturn[4] = prisonName;
		
		return stringsToReturn;
	}
	
	
	//returns true if a string contains a number in it
	private static boolean containsNumbers(String string)
	{
		char[] chars = string.toCharArray();
		for(char c: chars){
			int ascii = (int)c;
			if(ascii < 58 && ascii > 47){
				return true;
			}
		}
		return false;
	}
	//returns true if a string has a comma
	//this is used because sometimes last names are more than one word
	//the last word in the last name will always have a comma
	private static boolean containsComma(String string)
	{
		char[] chars = string.toCharArray();
		for(char c: chars){
			if(c == ','){
				return true;
			}
		}
		return false;
	}
	
	//call at end of session
	public void quitDriver()
	{
		driver.quit();
	}
}




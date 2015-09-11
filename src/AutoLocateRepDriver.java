import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class AutoLocateRepDriver {
	private WebDriver driver;
	
	//constructor
	public AutoLocateRepDriver(){
		driver = new FirefoxDriver();
		driver.get("http://findyourrep.legislature.ca.gov/");
	}
	
	public String[] getRepInfo(String street, String city, String zip)
	{
		//the three text elements that need to be input
		WebElement streetElement = driver.findElement(By.id("street"));
		WebElement cityElement = driver.findElement(By.id("city"));
		WebElement zipElement = driver.findElement(By.id("ZIP"));
		
		//the button to press to locate
		WebElement button = driver.findElement(By.id("locate"));
		
		//input information
		streetElement.sendKeys(street);
		cityElement.sendKeys(city);
		zipElement.sendKeys(zip);
		
		//query information
		button.click();
		
		//need to wait here or else error will occur
		waitasec();
		
		//find element that holds result
		WebElement repInfo = driver.findElement(By.id("tabResults"));
		
		//convert result to string
		String string = repInfo.getText();
		//convert result into array[4] district/assembly.man/district/senator
		String[] stringArr = string.split("[\r\n]+");
		
		// if stringArr length = 1 this means that an error has occurred and no info was found, thus the array should return null
		//this will be dealt with in the main class
		if(stringArr.length == 1) stringArr = null;
		
		//clears the data entry fields in preparation for next query
		streetElement.clear();
		cityElement.clear();
		zipElement.clear();
		
		return stringArr;
	}
	
	//call at end of session
	public void quitDriver()
	{
		driver.quit();
	}
	
	private void waitasec()
	{
		try{Thread.sleep(1500);
		} catch(InterruptedException ex) {
		Thread.currentThread().interrupt();
		}
	}
}

//not using anymore

//moves to middle of button to un hide.
//Actions action = new Actions(driver);
//action.moveToElement(hiddenButtonElement).click();
//WebElement unhiddenButtonElement = driver.findElement(By.cssSelector("input[type='button']"));
//WebElement hiddenButtonElement = driver.findElement(By.className("dijit"));
//hiddenButtonElement.click();

/*try {
Thread.sleep(10000);
} catch(InterruptedException ex) {
Thread.currentThread().interrupt();
}



(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
public Boolean apply(WebDriver d) {
    return d.getTitle().toLowerCase().startsWith("f");
}
});*/
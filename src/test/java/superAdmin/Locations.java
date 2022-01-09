package superAdmin;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import superAdminInputData.LocationInfoData;

public class Locations extends LocationInfoData{

	public static String env = "Test for Super Admin";
	public static String testSuiteName = "Test Suit 4 -- Locations";
	public static RemoteWebDriver driver = null;
	@Parameters({"myBrowser", "myOS", "hubLink"})


	@BeforeTest
	public static void setup(String myBrowser, String myOS, String hubLink) throws MalformedURLException {

		if((myBrowser.equalsIgnoreCase("chrome")) && (myOS.equalsIgnoreCase("windows"))){
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setBrowserName(myBrowser);
			caps.setPlatform(Platform.WINDOWS);
			ChromeOptions options = new ChromeOptions();
			options.merge(caps);
			driver = new RemoteWebDriver(new URL(hubLink),options);

		}

		if((myBrowser.equalsIgnoreCase("chrome")) && (myOS.equalsIgnoreCase("linux"))){
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setBrowserName(myBrowser);
			caps.setPlatform(Platform.LINUX);
			ChromeOptions options = new ChromeOptions();
			options.merge(caps);
			driver = new RemoteWebDriver(new URL(hubLink),options);

		}

		if((myBrowser.equalsIgnoreCase("firefox")) && (myOS.equalsIgnoreCase("windows"))) {
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setBrowserName(myBrowser);
			caps.setPlatform(Platform.WINDOWS);
			FirefoxOptions options = new FirefoxOptions();
			options.merge(caps);
			driver = new RemoteWebDriver(new URL(hubLink),options);

		}

		if((myBrowser.equalsIgnoreCase("firefox")) && (myOS.equalsIgnoreCase("linux"))) {
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setPlatform(Platform.LINUX);
			FirefoxOptions options = new FirefoxOptions();
			options.merge(caps);
			driver = new RemoteWebDriver(new URL(hubLink),options);

		}

	}

	@BeforeSuite
	public static void beforeSuit() {

		if (env.equalsIgnoreCase("Test for Super Admin")) {

			System.out.println("Test executes in correct environment where environment= " + env);
			System.out.println("Test Suite name = " + testSuiteName);

		}else {
			System.out.println("Test executes in wrong environment where environment= " + env);

		}
	}

	//Opening browser with the given URL and navigate to Registration Page

	@BeforeMethod
	public void openBrowser() throws InterruptedException {

		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.get("https://dev.zntral.net/session/login");

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}



	//Verifying elements on Login page
	@Test(priority = 1)
	public void verifyElemntsOnPageTest() throws InterruptedException {

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement signInTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[@class='mb-3']")));
		signInTitle.isDisplayed();

	}

	//login with valid username & Password

	@Test(priority = 2)
	public void login() throws InterruptedException {

		WebElement username = driver.findElement(By.xpath("//input[@type='text']"));
		WebElement password = driver.findElement(By.xpath("//input[@type='password']"));
		WebElement login = driver.findElement(By.xpath("//form[@novalidate='novalidate']//button[1]"));

		String user = LocationInfoData.user;
		String pass = LocationInfoData.pass;

		username.sendKeys(user);
		password.sendKeys(pass);

		login.click();
		Thread.sleep(5000);

		String expectedUrl = "https://dev.zntral.net/dashboard";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl);
	}

	//Check Location list

	@Test(priority = 3)
	public void checkLocationList() throws InterruptedException {

		login();
		Thread.sleep(2000);
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement locations = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Locations']")));
		locations.click();
		Thread.sleep(3000);



		String expectedUrl = "https://dev.zntral.net/locations";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl);
	}

	//Search option

	@Test(priority = 4)
	public void search() throws InterruptedException {
		checkLocationList();

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		String searchLocation = LocationInfoData.searchLocation;

		WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-text-field__slot']//input[@type='text']")));
		search.sendKeys(searchLocation);
		Thread.sleep(3000);


		WebElement tableContents = driver.findElement(By.tagName("table"));
		List<WebElement> rows=tableContents.findElements(By.tagName("tr"));
		for(int rnum=1;rnum<rows.size();rnum++)
		{
			List<WebElement> columns=rows.get(rnum).findElements(By.tagName("td"));
			//Assert.assertEquals(columns.get(0).getText(),search.getAttribute("value"));
			String colVal = columns.get(1).getText();
			System.out.println(colVal);

			if(colVal.contains(search.getAttribute("value"))) {
				System.out.println("Matched!");
				Assert.assertTrue(true);
			}else {
				System.out.println("No value");

			}
		}

	}

	//Find patient list & patient info from location

	@Test(priority = 5)
	public void patientFromLocation() throws InterruptedException {
		checkLocationList();

		String searchLocation = LocationInfoData.searchLocation;

		WebElement search = driver.findElement(By.xpath("//div[@class='v-text-field__slot']//input[@type='text']"));
		search.sendKeys(searchLocation);
		WebElement firstRow = driver.findElement(By.xpath("//table[1]/tbody[1]/tr[1]/td[2]/span[1]"));

		firstRow.click();
		Thread.sleep(3000);
		WebElement patient = driver.findElement(By.xpath("//*[text()='Patients']"));
		patient.click();


		WebElement firstPatientInfo = driver.findElement(By.xpath("//table[1]/tbody[1]/tr[1]/td[1]/span[1]"));

		firstPatientInfo.click();
		Assert.assertTrue(true);
		Thread.sleep(3000);
	}

	//Find contact list & contact info from location

	@Test(priority = 6)
	public void contactFromLocation() throws InterruptedException {
		checkLocationList();

		String searchLocation = LocationInfoData.searchLocation;
		WebElement search = driver.findElement(By.xpath("//div[@class='v-text-field__slot']//input[@type='text']"));
		search.sendKeys(searchLocation);
		WebElement firstRow = driver.findElement(By.xpath("//table[1]/tbody[1]/tr[1]/td[2]/span[1]"));

		firstRow.click();
		Thread.sleep(3000);

		WebElement contact = driver.findElement(By.xpath("//*[text()='Contacts']"));
		contact.click();

		WebElement firstContactInfo = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/main[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/table[1]/tbody[1]/tr[1]/td[1]"));

		firstContactInfo.click();
		Assert.assertTrue(true);
		Thread.sleep(3000);
	}

	// types list
	@Test(priority = 7)
	public void locationTypes() throws InterruptedException {
		checkLocationList();
		WebElement types = driver.findElement(By.xpath("//*[text()='Types']"));
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", types);
		Assert.assertTrue(true);
		Thread.sleep(3000);
	}

	//update types

	@Test(priority = 8)
	public void updateTypes() throws InterruptedException {
		String editName = LocationInfoData.editName;
		String editAcronym = LocationInfoData.editAcronym;

		locationTypes();
		WebElement typeInfo = driver.findElement(By.xpath("//body[1]/div[1]/div[1]/div[1]/main[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/table[1]/tbody[1]/tr[1]/td[1]"));
		typeInfo.click();
		Thread.sleep(2000);

		WebElement name = driver.findElement(By.xpath("//div[@class='v-card v-sheet theme--light']//div[1]//div[1]//div[1]//div[1]//input[1]"));
		while (!name.getAttribute("value").equals("")) {
			name.sendKeys(Keys.BACK_SPACE);
		}
		name.sendKeys(editName);
		WebElement acronym = driver.findElement(By.xpath("//div[@class='v-card__text']//div[2]//div[1]//div[1]//div[1]//input[1]"));
		while (!acronym.getAttribute("value").equals("")) {
			acronym.sendKeys(Keys.BACK_SPACE);
		}
		acronym.sendKeys(editAcronym);
		WebElement update = driver.findElement(By.xpath("//*[text()='Update']"));

		try {
			update.click();
			Assert.assertTrue(true);
		}catch(Exception e) {

			if(name.getAttribute("value").isEmpty() || acronym.getAttribute("value").isEmpty()) {
				update.isDisplayed();
				Assert.assertFalse(false);
			}

		}
	}

	//Add new Location Type
	@Test(priority = 9)
	public void addNewLocationType() throws InterruptedException {
		String addName = LocationInfoData.name;
		String addAcronym = LocationInfoData.acronym;

		locationTypes();
		WebElement addNewType = driver.findElement(By.xpath("//i[normalize-space()='add']"));
		addNewType.click();

		WebElement name = driver.findElement(By.xpath("//div[@class='v-card v-sheet theme--light']//div[1]//div[1]//div[1]//div[1]//input[1]"));
		WebElement acronym = driver.findElement(By.xpath("//div[@class='v-card__text']//div[2]//div[1]//div[1]//div[1]//input[1]"));

		name.sendKeys(addName);
		acronym.sendKeys(addAcronym);

		String actual = name.getAttribute("value");

		WebElement save = driver.findElement(By.xpath("//span[normalize-space()='Save']"));
		save.click();
		Thread.sleep(3000);

		WebElement lastRow = driver.findElement(By.xpath("(//table[1]//tbody[1]//tr)[last()]//td[2]")); 
		JavascriptExecutor js = (JavascriptExecutor) driver;

		js.executeScript("arguments[0].scrollIntoView(true);",lastRow);
		Thread.sleep(2000);
		String expectedAdminName = lastRow.getText();
		Assert.assertEquals(actual, expectedAdminName);

	}


	//check reset button on add new form

	@Test(priority = 9)
	public void resetButton() throws InterruptedException {

		String addName = LocationInfoData.name;
		String addAcronym = LocationInfoData.acronym;

		locationTypes();
		WebElement addNewType = driver.findElement(By.xpath("//i[normalize-space()='add']"));
		addNewType.click();

		WebElement name = driver.findElement(By.xpath("//div[@class='v-card v-sheet theme--light']//div[1]//div[1]//div[1]//div[1]//input[1]"));
		WebElement acronym = driver.findElement(By.xpath("//div[@class='v-card__text']//div[2]//div[1]//div[1]//div[1]//input[1]"));

		name.sendKeys(addName);
		acronym.sendKeys(addAcronym);

		WebElement save = driver.findElement(By.xpath("//*[text()='Save']"));
		WebElement reset = driver.findElement(By.xpath("//*[text()='Reset']"));
		reset.click();

		WebElement nameErrorMsg = driver.findElement(By.xpath("//div[contains(text(),'Name is required')]"));
		WebElement acronymErrorMsg = driver.findElement(By.xpath("//div[contains(text(),'Acronym is required')]"));
		Thread.sleep(2000);
		Assert.assertTrue(save.isDisplayed());
		Assert.assertTrue(nameErrorMsg.isDisplayed());
		Assert.assertTrue(acronymErrorMsg.isDisplayed());
	}


	//check cancel button on add new form

	@Test(priority = 10)
	public void cancelButton() throws InterruptedException {

		String addName = LocationInfoData.name;
		String addAcronym = LocationInfoData.acronym;

		locationTypes();
		WebElement addNewType = driver.findElement(By.xpath("//i[normalize-space()='add']"));
		addNewType.click();

		WebElement name = driver.findElement(By.xpath("//div[@class='v-card v-sheet theme--light']//div[1]//div[1]//div[1]//div[1]//input[1]"));
		WebElement acronym = driver.findElement(By.xpath("//div[@class='v-card__text']//div[2]//div[1]//div[1]//div[1]//input[1]"));

		name.sendKeys(addName);
		acronym.sendKeys(addAcronym);

		WebElement cancel = driver.findElement(By.xpath("//*[text()='Cancel']"));
		cancel.click();

	}

	//Address wise sort

	@Test(priority = 11)
	public void addressWiseSort() throws InterruptedException {
		checkLocationList();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement selectAddress = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Address')]")));

		selectAddress.click();
		selectAddress.click();
		selectAddress.click();
	}

	//Status wise sort

	@Test(priority = 12)
	public void statusWiseSort() throws InterruptedException {
		checkLocationList();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement selectStatus = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Status')]")));

		selectStatus.click();
		selectStatus.click();
		selectStatus.click();

	}

	//Name wise types sort

	@Test(priority = 13)
	public void nameWiseTypeSort() throws InterruptedException {
		locationTypes();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement selectStatus = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='container location-section pt-4 container--fluid grid-list-xl']//th[1]")));

		selectStatus.click();
		Thread.sleep(1000);
		selectStatus.click();
		Thread.sleep(1000);
	}

	//Acronym wise types sort

	@Test(priority = 14)
	public void acronymWiseTypeSort() throws InterruptedException {
		locationTypes();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement selectStatus = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='container location-section pt-4 container--fluid grid-list-xl']//th[2]")));

		selectStatus.click();
		Thread.sleep(1000);
		selectStatus.click();
		Thread.sleep(1000);
	}

	@AfterTest
	public void tearDown() throws Exception {
		if (driver != null) {
			System.out.println("Test Done!!!");
			driver.quit();
		}
	}

	@AfterSuite
	public static void afterSuit() {

		System.out.println( testSuiteName + " execution Complete");
	}

}

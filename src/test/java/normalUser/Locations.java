package normalUser;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import normalUserInputData.LocationInfoData;

public class Locations  {
	public static String env = "Test";
	public static String testSuiteName = "Test Suit 4 -- Locations";


	public static RemoteWebDriver driver = null;
	WebDriver driver2;
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

		if (env.equalsIgnoreCase("Test")) {

			System.out.println("Test executes in correct environment where environment= " + env);
			System.out.println("Test Suite name = " + testSuiteName);

		}else {
			System.out.println("Test executes in wrong environment where environment= " + env);

		}
	}

	@BeforeMethod
	public void openBrowser() throws InterruptedException {

		driver.manage().deleteAllCookies();
		driver.get("https://dev.zntral.net/session/login");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		Thread.sleep(3000);
	}

		//login

	@Test(priority = 1)
	public void loginUser() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement username = driver.findElement(By.xpath("//input[@type='text']"));
		WebElement password = driver.findElement(By.xpath("//input[@type='password']"));
		WebElement login = driver.findElement(By.xpath("//form[@novalidate='novalidate']//button[1]"));

		String user = LocationInfoData.user;
		String pass = LocationInfoData.pass;

		username.sendKeys(user);
		password.sendKeys(pass);

		login.click();
		WebElement loginAs = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Counsellors']")));
		loginAs.click();
		Thread.sleep(5000);

		String expectedUrl = "https://dev.zntral.net/dashboard";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl);


	}


	//View the location list

	@Test(priority = 2)
	public void locationList() throws InterruptedException {

		loginUser();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Locations']")));
		driver.findElement(By.xpath("//span[normalize-space()='Locations']")).click();
		String expectedUrl = "https://dev.zntral.net/locations";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl);
	}

	//Search option

	@Test(priority = 3)
	public void search() throws InterruptedException {
		locationList();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-text-field__slot']//input[@type='text']")));
		driver.findElement(By.xpath("//div[@class='v-text-field__slot']//input[@type='text']")).sendKeys(LocationInfoData.searchLocation);

		WebElement firstRow = driver.findElement(By.xpath("//tbody/tr[1]/td[1]"));
		String expectedText = LocationInfoData.searchLocation;
		String actualText = firstRow.getText();
		Assert.assertEquals(actualText, expectedText);
		Thread.sleep(3000);
	}

	//Add new Location -- Click "+" button

	@Test(priority = 4)
	public void addLocation() throws InterruptedException{
		loginUser();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Locations']")));
		driver.findElement(By.xpath("//span[normalize-space()='Locations']")).click();

		WebElement add = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//i[normalize-space()='add']")));
		add.click();

		WebElement selectResidentType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@id='inspire']/div[@role='document']/div/div/div/div/div/div/div/div/div/div/div[@role='radiogroup']/div[3]/div[1]")));
		selectResidentType.click();
		Thread.sleep(2000);
		WebElement selectContinue = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Continue']")));
		selectContinue.click();

		Assert.assertTrue(selectResidentType.isEnabled());

	}

	//Adding location with valid info

	@Test(priority = 5)
	public void validLocationData() throws InterruptedException {
		addLocation();
		Thread.sleep(2000);
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement locationName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@data-app='true']/div[@role='document']/div/div/div/div/div/div/div/div/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/input[1]")));
		WebElement licenceNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@data-app='true']/div[@role='document']/div/div/div/div/div/div/div/div/div/div[2]/div[1]/div[2]/div[1]/div[1]/input[1]")));
		WebElement capacity = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@data-app='true']/div[@role='document']/div/div/div/div/div/div/div/div/div[1]/div[3]/div[1]/div[2]/div[1]/div[1]/input[1]")));
		WebElement address = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div[2]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement suiteUnit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div[2]//div[2]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		WebElement city = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='document']//div[3]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement state = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='document']//div[3]//div[2]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		WebElement zip = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@data-app='true']/div[@role='document']/div/div/div/div/div/div/div/div/div/div[3]/div[1]/div[1]/div[1]/div[1]/input[1]")));
		WebElement phoneNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[3]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[4]/div[1]/div[1]/div[2]/div[1]/div[1]/input[1]")));
		WebElement type = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-input theme--light v-text-field v-text-field--is-booted v-select']//div[@class='v-select__selections']")));
		//WebElement selectType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[4]/div[1]/div[1]/div[1]/input[1]")));
		WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='email']")));
		WebElement note = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[3]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[5]/div[1]/div[1]/div[2]/div[1]/div[1]/input[1]")));

		WebElement save = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Save']")));

		locationName.sendKeys(LocationInfoData.locationName);
		licenceNumber.sendKeys(LocationInfoData.licenceNumber);
		capacity.sendKeys(LocationInfoData.capacity);
		address.sendKeys(LocationInfoData.address);
		suiteUnit.sendKeys(LocationInfoData.suiteUnit);
		city.sendKeys(LocationInfoData.city);
		state.sendKeys(LocationInfoData.state);
		zip.sendKeys(LocationInfoData.zip);
		phoneNumber.sendKeys(LocationInfoData.phoneNumber);
		type.click();
		//selectType.click();
		email.sendKeys(LocationInfoData.email);
		note.sendKeys(LocationInfoData.note);
		save.click();
		Thread.sleep(5000);


		String URL = driver.getCurrentUrl();
		Assert.assertTrue(URL.contains("https://dev.zntral.net/locations/"));

	}

	//Adding location without any info

	@Test(priority = 6)
	public void invalidLocationData() throws InterruptedException {
		addLocation();
		Thread.sleep(2000);
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement locationName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@data-app='true']/div[@role='document']/div/div/div/div/div/div/div/div/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/input[1]")));
		WebElement licenceNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@data-app='true']/div[@role='document']/div/div/div/div/div/div/div/div/div/div[2]/div[1]/div[2]/div[1]/div[1]/input[1]")));
		WebElement capacity = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@data-app='true']/div[@role='document']/div/div/div/div/div/div/div/div/div[1]/div[3]/div[1]/div[2]/div[1]/div[1]/input[1]")));
		WebElement address = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div[2]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement suiteUnit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div[2]//div[2]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		WebElement city = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='document']//div[3]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement state = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='document']//div[3]//div[2]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		WebElement zip = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@data-app='true']/div[@role='document']/div/div/div/div/div/div/div/div/div/div[3]/div[1]/div[1]/div[1]/div[1]/input[1]")));
		WebElement phoneNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[3]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[4]/div[1]/div[1]/div[2]/div[1]/div[1]/input[1]")));
		WebElement type = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-input theme--light v-text-field v-text-field--is-booted v-select']//div[@class='v-select__selections']")));
		//WebElement selectType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[4]/div[1]/div[1]/div[1]/input[1]")));
		WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='email']")));
		WebElement note = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[3]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[5]/div[1]/div[1]/div[2]/div[1]/div[1]/input[1]")));

		WebElement save = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Save']")));

		locationName.sendKeys(LocationInfoData.locationName2);
		licenceNumber.sendKeys(LocationInfoData.licenceNumber2);
		capacity.sendKeys(LocationInfoData.capacity2);
		address.sendKeys(LocationInfoData.address2);
		suiteUnit.sendKeys(LocationInfoData.suiteUnit2);
		city.sendKeys(LocationInfoData.city2);
		state.sendKeys(LocationInfoData.state2);
		zip.sendKeys(LocationInfoData.zip2);
		phoneNumber.sendKeys(LocationInfoData.phoneNumber2);
		type.click();
		//selectType.click();
		email.sendKeys(LocationInfoData.email2);
		note.sendKeys(LocationInfoData.note2);
		save.isEnabled();
		Thread.sleep(5000);

		WebElement addressMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Address is required')]")));
		WebElement cityMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'City is required')]")));
		WebElement stateMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'State is required')]")));
		WebElement phoneMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Phone is required')]")));
		WebElement emailMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'E-mail must be valid')]")));

		String expectedText1 = "Address is required";
		String actualText1 = addressMsg.getText();
		Assert.assertEquals(actualText1, expectedText1);

		String expectedText2 = "City is required";
		String actualText2 = cityMsg.getText();
		Assert.assertEquals(actualText2, expectedText2);

		String expectedText3 = "State is required";
		String actualText3 = stateMsg.getText();
		Assert.assertEquals(actualText3, expectedText3);

		String expectedText4 = "Phone is required";
		String actualText4 = phoneMsg.getText();
		Assert.assertEquals(actualText4, expectedText4);

		String expectedText5 = "E-mail must be valid";
		String actualText5 = emailMsg.getText();
		Assert.assertEquals(actualText5, expectedText5);

	}

	//Adding location form --- Phone number, Email validation check

	@Test(priority = 7)
	public void validatePhoneEmailData() throws InterruptedException {
		addLocation();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement locationName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@data-app='true']/div[@role='document']/div/div/div/div/div/div/div/div/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/input[1]")));
		WebElement licenceNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@data-app='true']/div[@role='document']/div/div/div/div/div/div/div/div/div/div[2]/div[1]/div[2]/div[1]/div[1]/input[1]")));
		WebElement capacity = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@data-app='true']/div[@role='document']/div/div/div/div/div/div/div/div/div[1]/div[3]/div[1]/div[2]/div[1]/div[1]/input[1]")));
		WebElement address = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div[2]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement suiteUnit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div[2]//div[2]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		WebElement city = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='document']//div[3]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement state = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='document']//div[3]//div[2]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		WebElement zip = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@data-app='true']/div[@role='document']/div/div/div/div/div/div/div/div/div/div[3]/div[1]/div[1]/div[1]/div[1]/input[1]")));
		WebElement phoneNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[3]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[4]/div[1]/div[1]/div[2]/div[1]/div[1]/input[1]")));
		//WebElement type = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-input theme--light v-text-field v-text-field--is-booted v-select']//div[@class='v-select__selections']")));
		//WebElement selectType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[4]/div[1]/div[1]/div[1]/input[1]")));
		WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='email']")));
		WebElement note = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[3]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[5]/div[1]/div[1]/div[2]/div[1]/div[1]/input[1]")));

		WebElement save = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Save']")));

		locationName.sendKeys(LocationInfoData.locationName);
		licenceNumber.sendKeys(LocationInfoData.licenceNumber);
		capacity.sendKeys(LocationInfoData.capacity);
		address.sendKeys(LocationInfoData.address);
		suiteUnit.sendKeys(LocationInfoData.suiteUnit);
		city.sendKeys(LocationInfoData.city);
		state.sendKeys(LocationInfoData.state);
		zip.sendKeys(LocationInfoData.zip);
		phoneNumber.sendKeys(LocationInfoData.phoneNumber3);
		//type.click();
		//Thread.sleep(1000);
		//selectType.click();
		//Thread.sleep(1000);
		email.sendKeys(LocationInfoData.mail3);
		note.sendKeys(LocationInfoData.note);
		save.isEnabled();
		Thread.sleep(5000);


		WebElement phoneMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Phone Number minimum 10 digit required')]")));
		WebElement emailMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'E-mail must be valid')]")));

		String expectedText4 = "Phone Number minimum 10 digit required";
		String actualText4 = phoneMsg.getText();
		Assert.assertEquals(actualText4, expectedText4);

		String expectedText5 = "E-mail must be valid";
		String actualText5 = emailMsg.getText();
		Assert.assertEquals(actualText5, expectedText5);

	}

	//Adding location form --- back button check

	@Test(priority = 8)
	public void validateBackButton() throws InterruptedException {
		addLocation();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement back = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Back']")));
		back.click();

		Assert.assertTrue(true);
	}


	//Adding location form --- cancel button check

	@Test(priority = 9)
	public void validateCancelButton() throws InterruptedException {
		addLocation();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement cancel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Cancel']")));
		cancel.click();

		Thread.sleep(2000);
		Assert.assertTrue(true);
	}

	//Add patient info after adding new location info

	@Test(priority = 10)
	public void addPatientfromLocation() throws InterruptedException {

		validLocationData();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement addButton1 = driver.findElement(By.xpath("//i[normalize-space()='add']"));
		addButton1.click();
		Thread.sleep(1000);
		WebElement addPatientButton = driver.findElement(By.xpath("//strong[normalize-space()='Patient']"));
		addPatientButton.click();
		Thread.sleep(1000);
		WebElement FirstName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div[1]//div[2]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		WebElement LastName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='row no-gutters']//div[4]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		WebElement ssn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div[2]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement phoneNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='document']//div[3]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='email']")));
		WebElement submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Submit']")));

		FirstName.sendKeys(LocationInfoData.patientfirstName);
		LastName.sendKeys(LocationInfoData.patientlastName);
		ssn.sendKeys(LocationInfoData.ssn);
		phoneNumber.sendKeys(LocationInfoData.phone);
		email.sendKeys(LocationInfoData.patientemail);
		submit.click();
		Thread.sleep(5000);

		String URL = driver.getCurrentUrl();
		Assert.assertTrue(URL.contains("https://dev.zntral.net/patients/"));

	}

	//Add patient info after adding new location info -- Empty fields

	@Test(priority = 11)
	public void emptyPatientInfofromLocation() throws InterruptedException {

		validLocationData();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement addButton1 = driver.findElement(By.xpath("//i[normalize-space()='add']"));
		addButton1.click();
		Thread.sleep(1000);
		WebElement addPatientButton = driver.findElement(By.xpath("//strong[normalize-space()='Patient']"));
		addPatientButton.click();
		Thread.sleep(1000);
		WebElement FirstName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div[1]//div[2]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		WebElement LastName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='row no-gutters']//div[4]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		WebElement ssn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div[2]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement phoneNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='document']//div[3]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='email']")));
		WebElement submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Submit']")));

		FirstName.sendKeys(LocationInfoData.patientfirstName2);
		LastName.sendKeys(LocationInfoData.patientlastName2);
		ssn.sendKeys(LocationInfoData.ssn2);
		phoneNumber.sendKeys(LocationInfoData.phone2);
		email.sendKeys(LocationInfoData.patientemail2);

		submit.isEnabled();
		Thread.sleep(5000);

		WebElement firstNameMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'First name is required')]")));
		WebElement lastNameMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Last name is required')]")));

		String expectedText4 = "First name is required";
		String actualText4 = firstNameMsg.getText();
		Assert.assertEquals(actualText4, expectedText4);

		String expectedText5 = "Last name is required";
		String actualText5 = lastNameMsg.getText();
		Assert.assertEquals(actualText5, expectedText5);

		Thread.sleep(1000);

	}

	//Add contact info after adding new location info

	@Test(priority = 12)
	public void addContactfromLocation() throws InterruptedException {

		validLocationData();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement addButton1 = driver.findElement(By.xpath("//i[normalize-space()='add']"));
		addButton1.click();
		Thread.sleep(1000);
		WebElement addContactButton = driver.findElement(By.xpath("//strong[normalize-space()='Contact']"));
		addContactButton.click();
		Thread.sleep(1000);

		WebElement FirstName = driver.findElement(By.xpath("//input[@required='required']"));
		FirstName.sendKeys(LocationInfoData.contactfirstName);
		WebElement phoneNumber = driver.findElement(By.xpath("//div[@role='document']//div[3]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]"));
		phoneNumber.sendKeys(LocationInfoData.contactPhone);
		WebElement email = driver.findElement(By.xpath("//input[@type='email']"));
		email.sendKeys(LocationInfoData.contactEmail);
		WebElement save = driver.findElement(By.xpath("//span[normalize-space()='Save']"));

		save.click();
		Thread.sleep(5000);

		WebElement contactButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Contacts')]")));
		contactButton.click();

		WebElement firstRow = driver.findElement(By.xpath("//tbody/tr[1]/td[1]"));
		String expectedText = LocationInfoData.contactfirstName;
		String actualText = firstRow.getText();
		Assert.assertEquals(actualText, expectedText);

		String expectedText1 = firstRow.getText();
		firstRow.click();
		WebElement firstRowInfo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-card__title headline font-weight-bold']")));
		String actualText1 = firstRowInfo.getText();
		Assert.assertEquals(actualText1, expectedText1);
		Thread.sleep(2000);

		WebElement closeInfo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='text-capitalize v-btn v-btn--flat v-btn--text theme--light v-size--default primary--text']")));
		closeInfo.click();

	}

	//Add contact info after adding new location info with empty fields

	@Test(priority = 13)
	public void addContactEmptyLocation() throws InterruptedException {

		validLocationData();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement addButton1 = driver.findElement(By.xpath("//i[normalize-space()='add']"));
		addButton1.click();
		Thread.sleep(1000);
		WebElement addContactButton = driver.findElement(By.xpath("//strong[normalize-space()='Contact']"));
		addContactButton.click();
		Thread.sleep(1000);

		WebElement FirstName = driver.findElement(By.xpath("//input[@required='required']"));
		FirstName.sendKeys(LocationInfoData.contactfirstName2);
		WebElement phoneNumber = driver.findElement(By.xpath("//div[@role='document']//div[3]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]"));
		phoneNumber.sendKeys(LocationInfoData.contactPhone2);
		WebElement email = driver.findElement(By.xpath("//input[@type='email']"));
		email.sendKeys(LocationInfoData.contactEmail2);
		WebElement save = driver.findElement(By.xpath("//span[normalize-space()='Save']"));

		save.isEnabled();
		Thread.sleep(5000);

		WebElement firstNameMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-messages__message']")));
		String expectedText4 = "First name is required";
		String actualText4 = firstNameMsg.getText();
		Assert.assertEquals(actualText4, expectedText4);

	}

	//Edit location info

	@Test(priority = 14)
	public void editLocationInfo() throws InterruptedException {
		locationList();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement locationSelect = driver.findElement(By.xpath("//tbody/tr[1]/td[1]"));
		locationSelect.click();
		Thread.sleep(2000);

		WebElement editButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//i[@class='v-icon notranslate material-icons theme--light'][normalize-space()='more_vert']")));
		editButton.click();

		WebElement editOptionSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Edit')]")));
		editOptionSelect.click();

		WebElement editLicenceNumber = driver.findElement(By.xpath("//div[5]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]"));
		editLicenceNumber.clear();
		Thread.sleep(1000);

		while (!editLicenceNumber.getAttribute("value").equals("")) {
			editLicenceNumber.sendKeys(Keys.BACK_SPACE);
		}

		editLicenceNumber.sendKeys(LocationInfoData.editLicenceNumber);

		WebElement editCapacity = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[8]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		editCapacity.clear();
		Thread.sleep(1000);

		while (!editCapacity.getAttribute("value").equals("")) {
			editCapacity.sendKeys(Keys.BACK_SPACE);
		}
		editCapacity.sendKeys(LocationInfoData.editCapacity);

		WebElement update = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='update']")));
		update.click();
		Thread.sleep(3000);

		Assert.assertTrue(true);

	}

	//Delete location info

	@Test(priority = 15)
	public void deleteLocationInfo() throws InterruptedException {
		locationList();
		Thread.sleep(2000);
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement locationSelect = driver.findElement(By.xpath("//tbody/tr[1]/td[1]"));
		locationSelect.click();
		Thread.sleep(2000);

		WebElement editButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//i[@class='v-icon notranslate material-icons theme--light'][normalize-space()='more_vert']")));
		editButton.click();

		WebElement deleteButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Delete')]")));
		deleteButton.click();
		Thread.sleep(1000);

		WebElement deletePopUpYes = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='yes']")));
		deletePopUpYes.click();
		Thread.sleep(3000);

		String URL = driver.getCurrentUrl();
		Assert.assertTrue(URL.contains("https://dev.zntral.net/locations"));
		Thread.sleep(3000);
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

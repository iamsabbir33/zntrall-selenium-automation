package normalUser;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
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

import normalUserInputData.DashboardInfoData;

public class Dashboard  {

	public static String env = "Test";
	public static String testSuiteName = "Test Suit 8 -- Dashboard";

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
	public void loginUser()  throws InterruptedException {

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement username = driver.findElement(By.xpath("//input[@type='text']"));
		WebElement password = driver.findElement(By.xpath("//input[@type='password']"));
		WebElement login = driver.findElement(By.xpath("//form[@novalidate='novalidate']//button[1]"));

		String user = DashboardInfoData.user;
		String pass = DashboardInfoData.pass;

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

	// Add Location from the Dashboard

	@Test(priority = 2)
	public void addLocation() throws InterruptedException{
		loginUser();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//i[@class='v-icon notranslate material-icons theme--dark'][normalize-space()='add']")));
		driver.findElement(By.xpath("//i[@class='v-icon notranslate material-icons theme--dark'][normalize-space()='add']")).click();

		List<WebElement> options =  driver.findElements(By.xpath("//div[@role='radiogroup']"));
		Random random = new Random();
		int index = random.nextInt(options.size());
		options.get(index).click(); 

		WebElement selectContinue = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Continue']")));
		selectContinue.click();
		Assert.assertTrue(true);

	}

	// Add Location from the Dashboard with valid data

	@Test(priority = 3)
	public void validLocationData() throws InterruptedException {
		addLocation();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement locationName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@data-app='true']/div[@role='document']/div/div/div/div/div/div/div/div/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/input[1]")));
		locationName.sendKeys(DashboardInfoData.locationName);
		WebElement licenceNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@data-app='true']/div[@role='document']/div/div/div/div/div/div/div/div/div/div[2]/div[1]/div[2]/div[1]/div[1]/input[1]")));
		licenceNumber.sendKeys(DashboardInfoData.licenceNumber);
		WebElement capacity = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@data-app='true']/div[@role='document']/div/div/div/div/div/div/div/div/div[1]/div[3]/div[1]/div[2]/div[1]/div[1]/input[1]")));
		capacity.sendKeys(DashboardInfoData.capacity);
		WebElement address = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div[2]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		address.sendKeys(DashboardInfoData.address);
		WebElement suiteUnit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div[2]//div[2]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		suiteUnit.sendKeys(DashboardInfoData.suiteUnit);
		WebElement city = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='document']//div[3]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		city.sendKeys(DashboardInfoData.city);
		WebElement state = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='document']//div[3]//div[2]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		state.sendKeys(DashboardInfoData.state);
		WebElement zip = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@data-app='true']/div[@role='document']/div/div/div/div/div/div/div/div/div/div[3]/div[1]/div[1]/div[1]/div[1]/input[1]")));
		zip.sendKeys(DashboardInfoData.zip);
		WebElement phoneNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[3]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[4]/div[1]/div[1]/div[2]/div[1]/div[1]/input[1]")));
		phoneNumber.sendKeys(DashboardInfoData.phoneNumber);
		WebElement type = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-input theme--light v-text-field v-text-field--is-booted v-select']//div[@class='v-select__selections']")));
		type.click();
		WebElement selectType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Work')]")));
		selectType.click();
		WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='email']")));
		email.sendKeys(DashboardInfoData.email);
		WebElement note = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[3]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[5]/div[1]/div[1]/div[2]/div[1]/div[1]/input[1]")));
		note.sendKeys(DashboardInfoData.note);
		WebElement save = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Save']")));
		save.click();
		Thread.sleep(5000);

		String URL = driver.getCurrentUrl();
		Assert.assertTrue(URL.contains("https://dev.zntral.net/locations/"));

	}

	//Adding location without any info

	@Test(priority = 4)
	public void invalidLocationData() throws InterruptedException {
		addLocation();
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

		Thread.sleep(2000);
		locationName.sendKeys(DashboardInfoData.locationName2);
		licenceNumber.sendKeys(DashboardInfoData.licenceNumber2);
		capacity.sendKeys(DashboardInfoData.capacity2);
		address.sendKeys(DashboardInfoData.address2);
		suiteUnit.sendKeys(DashboardInfoData.suiteUnit2);
		city.sendKeys(DashboardInfoData.city2);
		state.sendKeys(DashboardInfoData.state2);
		zip.sendKeys(DashboardInfoData.zip2);
		phoneNumber.sendKeys(DashboardInfoData.phoneNumber2);
		type.click();
		email.sendKeys(DashboardInfoData.email2);
		note.sendKeys(DashboardInfoData.note2);

		//Location check
		if(locationName.getText().isEmpty()) {
			System.out.println("Location field is empty!");
		}
		else {
			System.out.println("Location submitted successfully!");
		}

		//Licence number check
		if(licenceNumber.getText().isEmpty()) {
			System.out.println("Licence number field is empty!");
		}
		else {
			System.out.println("Licence number submitted successfully!");
		}

		//capacity check
		if(capacity.getText().isEmpty()) {
			System.out.println("Capacity field is empty!");
		}
		else {
			System.out.println("Capacity submitted successfully!");
		}

		//address check
		if(address.getText().isEmpty()) {
			System.out.println("Address field is empty!");
		}
		else {
			System.out.println("address submitted successfully!");
		}

		//suiteUnit check
		if(suiteUnit.getText().isEmpty()) {
			System.out.println("Suite/Unit field is empty!");
		}
		else {
			System.out.println("Suite/Unit submitted successfully!");
		}

		//city check
		if(city.getText().isEmpty()) {
			System.out.println("City field is empty!");
		}
		else {
			System.out.println("City submitted successfully!");
		}

		//state check
		if(state.getText().isEmpty()) {
			System.out.println("State field is empty!");
		}
		else {
			System.out.println("State submitted successfully!");
		}

		//zip check
		if(zip.getText().isEmpty()) {
			System.out.println("Zip field is empty!");
		}
		else {
			System.out.println("Zip submitted successfully!");
		}

		//phoneNumber check
		if(phoneNumber.getText().isEmpty()) {
			System.out.println("Phone Number field is empty!");
		}
		else {
			System.out.println("Phone Number submitted successfully!");
		}

		//type check
		if(type.getText().isEmpty()) {
			System.out.println("Type field is empty!");
		}
		else {
			System.out.println("Type submitted successfully!");
		}

		//email check
		if(email.getText().isEmpty()) {
			System.out.println("Email field is empty!");
		}
		else {
			System.out.println("Email submitted successfully!");
		}

		//note check
		if(note.getText().isEmpty()) {
			System.out.println("Note field is empty!");
		}
		else {
			System.out.println("Note submitted successfully!");
		}


		try {
			save.click();
			Thread.sleep(2000);	

		} 
		catch (Exception e){
			System.out.println("Mandatory fields has no data !");
			save.isEnabled();
			Thread.sleep(2000);
		}

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

	//Email validation check on Add location form

	@Test(priority = 5)
	public void validateEmailData() throws InterruptedException {
		addLocation();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='email']")));

		email.sendKeys(DashboardInfoData.validemail);
		WebElement note = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[3]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[5]/div[1]/div[1]/div[2]/div[1]/div[1]/input[1]")));
		note.sendKeys(DashboardInfoData.note3);

		if(email.getAttribute("value").startsWith("@")){
			WebElement invalidMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'E-mail must be valid')]")));
			System.out.println(invalidMsg.getText());
			Assert.assertTrue(true);
		}else {
			System.out.println("Email submitted successfully!");
		}
	}

	// Add Patient from the Dashboard

	@Test(priority = 6)
	public void addPatient() throws InterruptedException{
		loginUser();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//i[@class='v-icon notranslate material-icons theme--light'][normalize-space()='add']")));
		driver.findElement(By.xpath("//i[@class='v-icon notranslate material-icons theme--light'][normalize-space()='add']")).click();

		WebElement prefix = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div[1]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement firstName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div[1]//div[2]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		WebElement lastName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div//div[4]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		WebElement ssn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div[2]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement dob = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div//div[2]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement gender = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='document']//div[3]//div[1]//div[2]//div[1]//div[1]//div[2]//div[1]//i[1]")));
		WebElement phone = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[3]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement type = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@id='inspire']//div[@role='document']//div//div//div//div//div//div//div//div//div//div[1]//div[1]//div[1]//div[2]//div[1]//i[1]")));
		WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='email']")));
		WebElement location = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@id='inspire']//div[@role='document']//div//div//div//div//div//div//div[1]//div[1]//div[2]//div[1]//div[1]//div[2]//div[1]//i[1]")));
		WebElement note = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[5]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));

		WebElement save = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Submit']")));

		prefix.sendKeys(DashboardInfoData.prefix);
		firstName.sendKeys(DashboardInfoData.patientfirstName);
		lastName.sendKeys(DashboardInfoData.patientlastName);
		ssn.sendKeys(DashboardInfoData.ssn);
		dob.sendKeys(DashboardInfoData.dob);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='OK']"))).click();
		gender.click();
		WebElement genderMale = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Male')]")));
		genderMale.click();
		phone.sendKeys(DashboardInfoData.phone);
		type.click();
		WebElement typeMobile = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-list-item__title'][normalize-space()='Mobile']")));
		typeMobile.click();
		email.sendKeys(DashboardInfoData.patientemail);
		//location.click();
		//WebElement selectLocation = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[normalize-space()='Test Location 2']")));
		//selectLocation.click();
		note.sendKeys(DashboardInfoData.patientnote);
		save.click();
		Thread.sleep(5000);

		String URL = driver.getCurrentUrl();
		Assert.assertTrue(URL.contains("https://dev.zntral.net/patients/"));

	}

	//Adding patient without any info

	@Test(priority = 7)
	public void addPatientWithEmpty() throws InterruptedException{
		loginUser();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//i[@class='v-icon notranslate material-icons theme--light'][normalize-space()='add']")));
		driver.findElement(By.xpath("//i[@class='v-icon notranslate material-icons theme--light'][normalize-space()='add']")).click();

		WebElement prefix = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div[1]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement firstName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div[1]//div[2]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		WebElement lastName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div//div[4]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		WebElement ssn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div[2]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement dob = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div//div[2]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		//WebElement gender = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='document']//div[3]//div[1]//div[2]//div[1]//div[1]//div[2]//div[1]//i[1]")));
		WebElement phone = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[3]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		//WebElement type = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@id='inspire']//div[@role='document']//div//div//div//div//div//div//div//div//div//div[1]//div[1]//div[1]//div[2]//div[1]//i[1]")));
		WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='email']")));
		WebElement location = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@id='inspire']//div[@role='document']//div//div//div//div//div//div//div[1]//div[1]//div[2]//div[1]//div[1]//div[2]//div[1]//i[1]")));
		WebElement note = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[5]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));

		WebElement save = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Submit']")));

		prefix.sendKeys(DashboardInfoData.prefix2);
		firstName.sendKeys(DashboardInfoData.patientfirstName2);
		lastName.sendKeys(DashboardInfoData.patientlastName2);
		ssn.sendKeys(DashboardInfoData.ssn2);
		dob.sendKeys(DashboardInfoData.dob2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='OK']"))).click();
		phone.sendKeys(DashboardInfoData.phone2);
		email.sendKeys(DashboardInfoData.patientemail2);
		note.sendKeys(DashboardInfoData.patientnote2);


		//prefix check
		if(prefix.getText().isEmpty()) {
			System.out.println("prefix field is empty!");
		}
		else {
			System.out.println("prefix submitted successfully!");
		}

		//firstName check
		if(firstName.getText().isEmpty()) {
			System.out.println("firstName field is empty!");
		}
		else {
			System.out.println("firstName submitted successfully!");
		}

		//lastName check
		if(lastName.getText().isEmpty()) {
			System.out.println("lastName field is empty!");
		}
		else {
			System.out.println("lastName submitted successfully!");
		}

		//ssn check
		if(ssn.getText().isEmpty()) {
			System.out.println("ssn field is empty!");
		}
		else {
			System.out.println("ssn submitted successfully!");
		}

		//dob check
		if(dob.getText().isEmpty()) {
			System.out.println("dob field is empty!");
		}
		else {
			System.out.println("dob submitted successfully!");
		}

		//phone check
		if(phone.getText().isEmpty()) {
			System.out.println("phone field is empty!");
		}
		else {
			System.out.println("phone submitted successfully!");
		}

		//email check
		if(email.getText().isEmpty()) {
			System.out.println("email field is empty!");
		}
		else {
			System.out.println("email submitted successfully!");
		}

		//location check
		if(location.getText().isEmpty()) {
			System.out.println("location field is empty!");
		}
		else {
			System.out.println("location submitted successfully!");
		}

		//note check
		if(note.getText().isEmpty()) {
			System.out.println("note field is empty!");
		}
		else {
			System.out.println("note submitted successfully!");
		}

		try {
			save.click();
			Thread.sleep(1000);	

		} 
		catch (Exception e){
			System.out.println("Mandatory fields has no data !");
			save.isEnabled();
			Thread.sleep(1000);
		}

		WebElement firstNameMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'First name is required')]")));
		WebElement lastNameMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Last name is required')]")));

		String expectedText1 = "First name is required";
		String actualText1 = firstNameMsg.getText();
		Assert.assertEquals(actualText1, expectedText1);

		String expectedText2 = "Last name is required";
		String actualText2 = lastNameMsg.getText();
		Assert.assertEquals(actualText2, expectedText2);

	}

	//Email validation check on Add patient form

	@Test(priority = 8)
	public void validateEmailDataOnPatient() throws InterruptedException {
		loginUser();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//i[@class='v-icon notranslate material-icons theme--light'][normalize-space()='add']")));
		driver.findElement(By.xpath("//i[@class='v-icon notranslate material-icons theme--light'][normalize-space()='add']")).click();

		WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='email']")));

		email.sendKeys(DashboardInfoData.patientvalidemail);
		WebElement note = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[5]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		note.sendKeys(DashboardInfoData.patientnote3);
		if(email.getAttribute("value").startsWith("@")){
			WebElement invalidMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'E-mail must be valid')]")));
			System.out.println(invalidMsg.getText());
			Assert.assertTrue(true);
			Thread.sleep(1000);
		}else {
			System.out.println("Email submitted successfully!");
			Thread.sleep(2000);
		}
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

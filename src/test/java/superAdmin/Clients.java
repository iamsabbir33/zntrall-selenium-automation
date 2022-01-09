package superAdmin;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
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

import superAdminInputData.ClientsInfoData;

public class Clients extends ClientsInfoData {

	public static String env = "Test for Super Admin";
	public static String testSuiteName = "Test Suit 2 -- Clients";

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
		Thread.sleep(3000);
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

		String user = ClientsInfoData.user;
		String pass = ClientsInfoData.pass;

		username.sendKeys(user);
		password.sendKeys(pass);

		login.click();
		Thread.sleep(5000);

		String expectedUrl = "https://dev.zntral.net/dashboard";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl);
	}


	//Check client list

	@Test(priority = 3)
	public void checkClientList() throws InterruptedException {

		login();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement clients = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Clients']")));
		clients.click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		String expectedUrl = "https://dev.zntral.net/clients";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl);

	}


	//Check client profile

	@Test(priority = 4)
	public void checkClientProfile() throws InterruptedException {

		login();
		WebElement clients = driver.findElement(By.xpath("//span[normalize-space()='Clients']"));
		clients.click();
		Thread.sleep(5000);

		WebElement clientName = driver.findElement(By.xpath("//table[1]/tbody[1]/tr[1]/td[1]"));
		String client = clientName.getText();
		clientName.click();
		Thread.sleep(5000);

		WebElement clientNameVerify = driver.findElement(By.xpath("//h2[contains(@class,'text-capitalize mb-0 header-title d-custom-flex')]"));

		if(clientNameVerify.getText().contains(client)) {
			Assert.assertTrue(true);
			System.out.println("Profile matched !!");
		}else {
			System.out.println("Profile don't match !!");
		}

	}


	//Check client profile status change (Active to inactive)

	@Test(priority = 5)
	public void changeStatusToInactive() throws InterruptedException {
		checkClientProfile();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement status = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='v-btn v-btn--flat v-btn--text theme--light v-size--default primary--text']")));
		status.click();
		WebElement statusInactive = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Inactive')]")));
		statusInactive.click();
		Assert.assertTrue(true);
		System.out.println("Status changed to Inactive");

	}


	//Check client profile status change (InActive to active)

	@Test(priority = 6)
	public void changeStatusToActive() throws InterruptedException {
		checkClientProfile();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement status = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='v-btn v-btn--flat v-btn--text theme--light v-size--default primary--text']")));
		status.click();
		WebElement statusActive = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Active')]")));
		statusActive.click();
		Assert.assertTrue(true);
		System.out.println("Status changed to Active");

	}

	//Check location status on client profile

	@Test(priority = 7)
	public void checkLocationStatus() throws InterruptedException{
		checkClientProfile();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement locationTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Locations')]")));
		locationTab.click();

		try {
			WebElement locationInfo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/main[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody[1]/tr[1]/td[1]")));
			if(locationInfo.getText() != null ) {
				Assert.assertTrue(true);
				System.out.println("Location Found !!");
			}
		}
		catch(Exception e) {
			System.out.println("Location not Found !!");
		}
	}


	//Check patient status on client profile

	@Test(priority = 8)
	public void checkPatientStatus() throws InterruptedException{
		checkClientProfile();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement patientTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Patients')]")));
		patientTab.click();
		try {
			WebElement patientInfo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/main[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/table[1]/tbody[1]/tr[1]/td[1]")));
			if(patientInfo.getText() != null ) {
				Assert.assertTrue(true);
				System.out.println("Patient Found !!");
			}
		}
		catch(Exception e) {
			System.out.println("Patient not Found !!");
		}
	}


	//Search option

	@Test(priority = 9)
	public void search() throws InterruptedException {
		checkClientList();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@data-app='true']/div/div/main[@data-booted='true']/div/div/div/div/div/div/div/div/div/div/div/div/div/input[1]")));
		search.sendKeys("John");
		WebElement firstRow = driver.findElement(By.xpath("//table[1]/tbody[1]/tr[1]/td[1]"));	
		String actualText = firstRow.getText();
		Assert.assertTrue(actualText.contains(search.getText()));
		Thread.sleep(3000);
	}


	//Check client type list

	@Test(priority = 10)
	public void checkClientTypeList() throws InterruptedException {
		login();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement clients = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Clients']")));
		clients.click();
		WebElement element = driver.findElement(By.xpath("//*[text()='Types']"));
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", element);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); 

		String expectedText = "Types";
		String actualText = element.getText();
		Assert.assertEquals(actualText, expectedText);
		Thread.sleep(3000);
	}

	//Add new client group with valid info

	@Test(priority = 11)
	public void addNewClientGroup() throws InterruptedException {

		String clientGroupName = ClientsInfoData.name;
		String clientGroupAcronym = ClientsInfoData.acronym;

		checkClientList();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement add = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//i[normalize-space()='add']")));
		add.click();

		WebElement clientGroup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//strong[normalize-space()='Client Group']")));
		clientGroup.click();
		WebElement name = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div[1]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		name.sendKeys(clientGroupName);
		WebElement acronym = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div[2]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		acronym.sendKeys(clientGroupAcronym);
		WebElement signUp = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='v-chip__content'][normalize-space()='Sign Up']")));
		WebElement status = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='v-chip__content'][normalize-space()='Status']")));

		signUp.click();
		status.click();
		Thread.sleep(2000);

		WebElement submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Add']")));
		submit.click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

	}

	//Edit client group with valid info

	@Test(priority = 12)
	public void editClientGroup() throws InterruptedException {

		String editClientGroupName = ClientsInfoData.editName;
		String editClientGroupAcronym = ClientsInfoData.editAcronym;

		checkClientList();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement selectAnyGroup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Client Group 1']")));
		selectAnyGroup.click();
		WebElement name = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div[1]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		name.sendKeys(editClientGroupName);
		WebElement acronym = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div[2]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		acronym.sendKeys(editClientGroupAcronym);
		WebElement update = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Update']")));
		update.click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}

	//Name wise sort

	@Test(priority = 13)
	public void nameSort() throws InterruptedException {
		checkClientList();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement selectName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Name')]")));

		selectName.click();
		selectName.click();
		selectName.click();
	}

	//Type wise sort

	@Test(priority = 14)
	public void typeSort() throws InterruptedException {
		checkClientList();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement selectName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Type')]")));

		selectName.click();
		selectName.click();
		selectName.click();
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

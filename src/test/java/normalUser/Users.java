package normalUser;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
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

import normalUserInputData.UsersInfoData;

public class Users {

	public static String env = "Test";
	public static String testSuiteName = "Test Suit 3 -- Users";

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

		WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='text']")));
		WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='password']")));
		WebElement login = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@novalidate='novalidate']//button[1]")));

		username.sendKeys(UsersInfoData.user);
		password.sendKeys(UsersInfoData.pass);

		login.click();
		WebElement loginAs = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Counsellors']")));
		loginAs.click();
		Thread.sleep(5000);

		String expectedUrl = "https://dev.zntral.net/dashboard";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl);


	}

	//View the user list

	@Test(priority = 2)
	public void userList() throws InterruptedException {

		loginUser();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement userlist = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Users']")));
		userlist.click();
		Thread.sleep(3000);

		String expectedUrl = "https://dev.zntral.net/users";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl);
	}

	//Add new user form

	@Test(priority = 3)
	public void addUser() throws InterruptedException{
		userList();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement add = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//i[normalize-space()='add']")));
		add.click();
		Thread.sleep(2000);

		WebElement addUser = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//strong[normalize-space()='Add new user']")));
		Thread.sleep(2000);
		addUser.click();

		Assert.assertTrue(true);
		Thread.sleep(3000);

	}

	//Adding user with valid info

	@Test(priority = 4)
	public void validUserData() throws InterruptedException {
		addUser();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement FirstName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div[1]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement LastName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@data-app='true']/div[@role='document']/div/div/div/div/div/div/form[@novalidate='novalidate']/div/div/div/div[1]/div[1]/div[1]/input[1]")));
		WebElement emailId = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='email']")));
		WebElement groupDrpDown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@novalidate='novalidate']//div//div//div//div//div[@role='button']//div//div//div//i[@aria-hidden='true']")));
		//WebElement groupSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='save']")));
		WebElement save = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='save']")));

		FirstName.sendKeys(UsersInfoData.FirstName);
		LastName.sendKeys(UsersInfoData.LastName);
		emailId.sendKeys(UsersInfoData.emailId);
		groupDrpDown.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Hospice Registered Nurse')]"))).click();


		try {
			save.click();
			Thread.sleep(5000);
			WebElement firstRow = driver.findElement(By.xpath("//tbody/tr[1]/td[3]"));
			if(firstRow.getAttribute("value") == emailId.getText()) {
				System.out.println("Successfully added");
				Assert.assertTrue(true);
			}

		}catch(Exception e) {
			System.out.println("Failed !!!");
			Assert.assertTrue(false);
		}
	}

	//Already existed user

	@Test(priority = 5)
	public void existedUser() throws InterruptedException {
		addUser();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement FirstName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div[1]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement LastName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@data-app='true']/div[@role='document']/div/div/div/div/div/div/form[@novalidate='novalidate']/div/div/div/div[1]/div[1]/div[1]/input[1]")));
		WebElement emailId = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='email']")));
		//WebElement groupDrpDown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@novalidate='novalidate']//div//div//div//div//div[@role='button']//div//div//div//i[@aria-hidden='true']")));
		//WebElement groupSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='save']")));
		WebElement save = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='save']")));

		FirstName.sendKeys(UsersInfoData.FirstName);
		LastName.sendKeys(UsersInfoData.LastName);
		emailId.sendKeys(UsersInfoData.emailId);
		save.click();
		Thread.sleep(2000);

		try {
			WebElement status = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='status']")));
			status.isDisplayed();

		} catch (Exception e){
			System.out.println("New User");
		}

	}
	//Add new user form -- validation
	@Test(priority = 6)
	public void validateNewUser() throws InterruptedException {
		addUser();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement FirstName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div[1]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement LastName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@data-app='true']/div[@role='document']/div/div/div/div/div/div/form[@novalidate='novalidate']/div/div/div/div[1]/div[1]/div[1]/input[1]")));
		WebElement emailId = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='email']")));
		WebElement groupDrpDown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@novalidate='novalidate']//div//div//div//div//div[@role='button']//div//div//div//i[@aria-hidden='true']")));
		//WebElement groupSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='save']")));
		WebElement save = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='save']")));

		FirstName.sendKeys(UsersInfoData.invalidFirstName);
		LastName.sendKeys(UsersInfoData.invalidLastName);
		emailId.sendKeys(UsersInfoData.invalidemailId);
		groupDrpDown.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Hospice Registered Nurse')]"))).click();

		try {
			save.click();
			Thread.sleep(1000);	

		} 
		catch (Exception e){
			if(FirstName.getAttribute("value").isEmpty()) {
				save.isDisplayed();
				WebElement invalidMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-messages__message']")));
				System.out.println(invalidMsg.getText());

				Assert.assertTrue(true);

			}
			else if(LastName.getAttribute("value").isEmpty()) {
				save.isDisplayed();
				WebElement invalidMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-messages__message']")));
				System.out.println(invalidMsg.getText());

				Assert.assertTrue(true);

			}
			else if(emailId.getAttribute("value").isEmpty()) {
				save.isDisplayed();
				WebElement invalidMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-messages__message']")));
				System.out.println(invalidMsg.getText());

				Assert.assertTrue(true);
			}
			else if(emailId.getAttribute("value").startsWith("@")) {
				save.isDisplayed();
				WebElement invalidMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-messages__message']")));
				System.out.println(invalidMsg.getText());

				Assert.assertTrue(true);
			}
			else {
				save.isDisplayed();
				System.out.println("Mandatory field has no value");
				Assert.assertTrue(true);
			}
		}
	}

	// Check Reset button on add new user form

	@Test(priority = 7)
	public void resetButton() throws InterruptedException {

		addUser();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement FirstName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div[1]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement LastName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@data-app='true']/div[@role='document']/div/div/div/div/div/div/form[@novalidate='novalidate']/div/div/div/div[1]/div[1]/div[1]/input[1]")));
		WebElement emailId = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='email']")));
		WebElement groupDrpDown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@novalidate='novalidate']//div//div//div//div//div[@role='button']//div//div//div//i[@aria-hidden='true']")));
		//WebElement groupSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='save']")));
		WebElement reset = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Reset']")));

		FirstName.sendKeys(UsersInfoData.FirstName);
		LastName.sendKeys(UsersInfoData.LastName);
		emailId.sendKeys(UsersInfoData.emailId);
		groupDrpDown.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Hospice Registered Nurse')]"))).click();

		//WebElement resetConfirm = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Yes']")));


		try {

			reset.click();
			Thread.sleep(2000);

			String expectedText1 = "Name is required";
			String actualText1 = driver.findElement(By.xpath("//div[@class='col-sm-12 col-md-6 col']//div[@class='v-messages__message']")).getText();
			Assert.assertEquals(actualText1, expectedText1);

			String expectedText2 = "Name is required";
			String actualText2 = driver.findElement(By.xpath("//div[@class='col-md-6 col']//div[@class='v-messages__message']")).getText();
			Assert.assertEquals(actualText2, expectedText2);

			String expectedText3 = "E-mail must be valid";
			String actualText3 = driver.findElement(By.xpath("//div[contains(text(),'E-mail must be valid')]")).getText();
			Assert.assertEquals(actualText3, expectedText3);

		} catch(Exception e) {
			System.out.println("Failed !!");
		}


	}

	// Check Cancel button on add new user form

	@Test(priority = 8)
	public void cancelButton() throws InterruptedException {
		addUser();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement cancel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Cancel']")));
		cancel.click();

	}

	//Check close button on add new user form

	@Test(priority = 9)
	public void closeButton() throws InterruptedException {
		addUser();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement close = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[normalize-space()='close']")));
		close.click();

	}

	//Search option

	@Test(priority = 10)
	public void search() throws InterruptedException {
		userList();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='text']")));
		search.sendKeys(UsersInfoData.search);

		WebElement firstRow = driver.findElement(By.xpath("//tbody/tr[1]/td[3]"));
		String expectedText = UsersInfoData.search;
		String actualText = firstRow.getText();
		Assert.assertEquals(actualText, expectedText);
		Thread.sleep(3000);
	}

	//User profile 

	@Test(priority = 11)
	public void profileOfUser() throws InterruptedException {
		userList();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement firstRowInfo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody[1]/tr[1]/td[1]")));
		firstRowInfo.click();

		String actualText = "Users";

		WebElement user =driver.findElement(By.xpath("//a[@class='router-link-active']"));
		String ExpectedText =user.getText();	
		Assert.assertTrue(ExpectedText.contains(actualText));
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

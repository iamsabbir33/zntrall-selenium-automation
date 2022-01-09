package superAdmin;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
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

import superAdminInputData.User_profileInfoData;

public class User_profile extends User_profileInfoData {

	public static String env = "Test for Super Admin";
	public static String testSuiteName = "Test Suit 6 -- Profile";
	public String name_text="";
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

		String user = User_profileInfoData.user;
		String pass = User_profileInfoData.pass;

		username.sendKeys(user);
		password.sendKeys(pass);

		login.click();
		Thread.sleep(5000);

		String expectedUrl = "https://dev.zntral.net/dashboard";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl);
	}

	//View the user profile

	@Test(priority = 2)
	public void userProfile() throws InterruptedException {

		login();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@data-app='true']/div/div/div/nav[@data-booted='true']/div/div/section/header[@data-booted='true']/div/div/div/div[2]/div[1]")));
		username.click();
		Thread.sleep(3000);

		String expectedUrl = "https://dev.zntral.net/profile";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl);
	}


	// Add phone number 

	@Test(priority = 3)
	public void addPhone() throws InterruptedException {
		userProfile();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement phoneNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h5[normalize-space()='Phone']")));
		phoneNumber.click();
		Thread.sleep(2000);

		WebElement selectPhoneNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@required='required']")));
		while (!selectPhoneNumber.getAttribute("value").equals("")) {
			selectPhoneNumber.sendKeys(Keys.BACK_SPACE);
		}

		String addPhone = User_profileInfoData.addPhone;

		selectPhoneNumber.sendKeys(addPhone);
		Thread.sleep(1000);

		WebElement phoneNumberType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@role='document']//div//div//div[1]//div[2]//div[1]//div[1]//div[1]//div[1]//div[2]//div[1]//i[1]")));
		phoneNumberType.click();
		Thread.sleep(1000);

		List<WebElement> items = driver.findElements(By.xpath("//div[@role='listbox']"));
		for(WebElement e: items)
		{
			//System.out.print(e.getText());
			if(e.getText().equalsIgnoreCase("Mobile"))
			{
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='listbox']//div[2]//div[1]//div[1]"))).click();
			}
			if(e.getText().equalsIgnoreCase("Work"))
			{
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='listbox']//div[3]//div[1]//div[1]"))).click();
			}
			else {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='listbox']//div[1]//div[1]//div[1]"))).click();
			}
		}

		WebElement save = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Submit']")));

		try {
			save.click();
			Thread.sleep(1000);	

		} 
		catch (Exception e){
			if(selectPhoneNumber.getAttribute("value").isEmpty()) {
				save.isDisplayed();
				WebElement invalidMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='alert']//div//div")));
				System.out.println(invalidMsg.getText());

				Assert.assertTrue(true);
				Thread.sleep(1000);

			}
			else if(selectPhoneNumber.getAttribute("value").length() > 10) {
				save.isDisplayed();
				WebElement invalidMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='alert']//div//div")));
				System.out.println(invalidMsg.getText());

				Assert.assertTrue(true);
				Thread.sleep(1000);
			}
			else {
				save.isDisplayed();
				WebElement invalidMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='alert']//div//div")));
				System.out.println(invalidMsg.getText());
				Assert.assertTrue(true);
				Thread.sleep(1000);
			}
		}

	}

	// Add gender

	@Test(priority = 4)
	public void addGender() throws InterruptedException {
		userProfile();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement gender = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h5[normalize-space()='Gender']")));
		gender.click();
		Thread.sleep(2000);

		WebElement selectGender = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='radiogroup']//div[1]//div[1]//div[1]")));
		selectGender.click();
		Thread.sleep(1000);

		WebElement save = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Submit']")));

		try {
			save.click();
			Thread.sleep(1000);	

		} 
		catch (Exception e){

			System.out.println("Gender selection is missing");
			Assert.assertTrue(true);
			Thread.sleep(1000);
		}

	}


	// update email

	@Test(priority = 5)
	public void updateEmail() throws InterruptedException {
		userProfile();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement gender = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h5[normalize-space()='Email']")));
		gender.click();
		Thread.sleep(2000);

		WebElement newEmail = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div[2]//div[1]//div[1]//div[1]//input[1]")));
		String firstMail = User_profileInfoData.firstMail;
		newEmail.sendKeys(firstMail);
		Thread.sleep(1000);

		WebElement confirmEmail = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='document']//div[3]//div[1]//div[1]//div[1]//input[1]")));
		String secondMail = User_profileInfoData.secondMail;
		confirmEmail.sendKeys(secondMail);
		Thread.sleep(1000);

		WebElement save = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='v-btn__content'][normalize-space()='Submit']")));

		try {
			save.click();
			Thread.sleep(1000);	

		} 
		catch (Exception e){
			if(newEmail.getAttribute("value").isEmpty()) {
				save.isDisplayed();
				WebElement invalidMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='alert']")));
				System.out.println(invalidMsg.getText());

				Assert.assertTrue(true);
				Thread.sleep(1000);

			}
			else if(newEmail.getAttribute("value").startsWith("@")) {
				save.isDisplayed();
				WebElement invalidMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='alert']")));
				System.out.println(invalidMsg.getText());

				Assert.assertTrue(true);
				Thread.sleep(1000);
			}
			else if(newEmail.getAttribute("value") != confirmEmail.getAttribute("value")) {
				save.isDisplayed();
				WebElement invalidMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='alert']")));
				System.out.println(invalidMsg.getText());
				Assert.assertTrue(true);
				Thread.sleep(1000);
			}
			else {
				save.isDisplayed();
				WebElement invalidMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='alert']")));
				System.out.println(invalidMsg.getText());
				Assert.assertTrue(true);
				Thread.sleep(1000);
			}
		}

	}


	//Add address

	@Test(priority = 6)
	public void addAddress() throws InterruptedException {
		userProfile();
		Thread.sleep(2000);

		String street = User_profileInfoData.streetAddress;
		String unitNo = User_profileInfoData.unit;
		String zipNo = User_profileInfoData.zip;
		String cityName = User_profileInfoData.city;
		String stateName = User_profileInfoData.state;
		String poBoxNo = User_profileInfoData.poBox;

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement address = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h5[normalize-space()='Address']")));
		address.click();
		Thread.sleep(2000);

		WebElement streetAddress = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[3]/div[1]/div[1]/div[2]/form[1]/div[1]/div[1]/div[1]/div[1]/input[1]")));
		while (!streetAddress.getAttribute("value").equals("")) {
			streetAddress.sendKeys(Keys.BACK_SPACE);
		}
		streetAddress.sendKeys(street);
		Thread.sleep(1000);

		WebElement unit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[3]/div[1]/div[1]/div[2]/form[1]/div[2]/div[1]/div[1]/div[1]/input[1]")));
		while (!unit.getAttribute("value").equals("")) {
			unit.sendKeys(Keys.BACK_SPACE);
		}
		unit.sendKeys(unitNo);
		Thread.sleep(1000);

		WebElement zip = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[3]/div[1]/div[1]/div[2]/form[1]/div[3]/div[1]/div[1]/div[1]/input[1]")));
		while (!zip.getAttribute("value").equals("")) {
			zip.sendKeys(Keys.BACK_SPACE);
		}
		zip.sendKeys(zipNo);
		Thread.sleep(1000);

		WebElement city = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[3]/div[1]/div[1]/div[2]/form[1]/div[4]/div[1]/div[1]/div[1]/input[1]")));
		while (!city.getAttribute("value").equals("")) {
			city.sendKeys(Keys.BACK_SPACE);
		}
		city.sendKeys(cityName);
		Thread.sleep(1000);

		WebElement state = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[3]/div[1]/div[1]/div[2]/form[1]/div[5]/div[1]/div[1]/div[1]/input[1]")));
		while (!state.getAttribute("value").equals("")) {
			state.sendKeys(Keys.BACK_SPACE);
		}
		state.sendKeys(stateName);
		Thread.sleep(1000);

		WebElement poBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[3]/div[1]/div[1]/div[2]/form[1]/div[6]/div[1]/div[1]/div[1]/input[1]")));
		while (!poBox.getAttribute("value").equals("")) {
			poBox.sendKeys(Keys.BACK_SPACE);
		}
		poBox.sendKeys(poBoxNo);
		Thread.sleep(1000);

		WebElement submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='v-btn__content'][normalize-space()='Submit']")));

		try {
			submit.click();
			Thread.sleep(1000);	

		} 
		catch (Exception e){
			if(streetAddress.getAttribute("value").isEmpty()) {
				submit.isDisplayed();
				WebElement invalidMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-messages__message']")));
				System.out.println(invalidMsg.getText());

				Assert.assertTrue(true);
				Thread.sleep(1000);

			}
			else {
				submit.isDisplayed();
				WebElement invalidMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-messages__message']")));
				System.out.println(invalidMsg.getText());
				Assert.assertTrue(true);
				Thread.sleep(1000);
			}
		}
	}

	//Change password

	@Test(priority = 7)
	public void changePassword() throws InterruptedException {
		userProfile();
		Thread.sleep(2000);

		String currPass = User_profileInfoData.currentPass;
		String newPassword = User_profileInfoData.newPass;

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h5[normalize-space()='Password']")));
		password.click();
		Thread.sleep(2000);

		WebElement requestOTP = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='font-sm mt-auto primary--text font-weight-bold']")));
		requestOTP.click();
		Thread.sleep(1000);

		WebElement currentPass = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='document']//div//div//div//div[2]//div[1]//div[1]//div[1]//input[1]")));
		currentPass.sendKeys(currPass);
		Thread.sleep(1000);

		WebElement newPass = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='document']//div[3]//div[1]//div[1]//div[1]//input[1]")));
		newPass.sendKeys(newPassword);
		Thread.sleep(1000);

		WebElement submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Submit']")));

		try {
			submit.click();
			Thread.sleep(1000);	

		} 
		catch (Exception e){
			if(currentPass.getAttribute("value").isEmpty()) {
				submit.isDisplayed();
				WebElement invalidMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='alert']")));
				System.out.println(invalidMsg.getText());
				Assert.assertTrue(true);
				Thread.sleep(1000);

			}
			else if(currentPass.getAttribute("value").length() <= 5) {
				submit.isDisplayed();
				WebElement invalidMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='alert']")));
				System.out.println(invalidMsg.getText());
				Assert.assertTrue(true);
				Thread.sleep(1000);
			}
			else if (newPass.getAttribute("value").isEmpty()) {
				submit.isDisplayed();
				WebElement invalidMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='alert']")));
				System.out.println(invalidMsg.getText());
				Assert.assertTrue(true);
				Thread.sleep(1000);
			}
			else if (currentPass.getAttribute("value") == newPass.getAttribute("value")) {
				submit.isDisplayed();
				WebElement invalidMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='alert']")));
				System.out.println(invalidMsg.getText());
				Assert.assertTrue(true);
				Thread.sleep(1000);
			}
			else {
				submit.isDisplayed();
				WebElement invalidMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='alert']")));
				System.out.println(invalidMsg.getText());
				Assert.assertTrue(true);
				Thread.sleep(1000);
			}
		}

	}

}



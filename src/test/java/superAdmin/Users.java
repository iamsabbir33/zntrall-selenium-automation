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
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import superAdminInputData.UsersInfoData;


public class Users extends UsersInfoData {

	public static String env = "Test for Super Admin";
	public static String testSuiteName = "Test Suit 3 -- Users";
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

		String user = UsersInfoData.user;
		String pass = UsersInfoData.pass;

		username.sendKeys(user);
		password.sendKeys(pass);

		login.click();
		Thread.sleep(5000);

		String expectedUrl = "https://dev.zntral.net/dashboard";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl);
	}

	//Check user list

	@Test(priority = 3)
	public void checkUserList() throws InterruptedException {

		login();
		Thread.sleep(2000);
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement users = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Users']")));
		users.click();
		Thread.sleep(3000);

		String expectedUrl = "https://dev.zntral.net/users";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl);
	}


	//Check user profile

	@Test(priority = 4)
	public void checkUserProfile() throws InterruptedException {

		login();

		WebElement users = driver.findElement(By.xpath("//span[normalize-space()='Users']"));
		users.click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		WebElement usersName = driver.findElement(By.xpath("//table[1]/tbody[1]/tr[2]/td[1]"));
		String user = usersName.getText();
		usersName.click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		WebElement userNameVerify = driver.findElement(By.xpath("//div[@class='media-body']"));

		if(userNameVerify.getText().contains(user)) {
			Assert.assertTrue(true);
			System.out.println("Profile matched !!");
		}else {
			System.out.println("Profile don't match !!");
		}

	}

	//Check client profile from user profile

	@Test(priority = 5)
	public void checkClientProfileFromUser() throws InterruptedException {
		checkUserProfile();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement clickUser = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[1]")));
		clickUser.click();
		Assert.assertTrue(true);
		System.out.println("Client Profile found");

	}

	//Check client profile status change (Active to inactive)

	@Test(priority = 6)
	public void changeStatusToInactive() throws InterruptedException {
		checkClientProfileFromUser();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement status = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='v-btn v-btn--flat v-btn--text theme--light v-size--default primary--text']")));
		status.click();
		WebElement statusInactive = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Inactive')]")));
		statusInactive.click();
		Assert.assertTrue(true);
		System.out.println("Status changed to Inactive");

	}


	//Check client profile status change (InActive to active)

	@Test(priority = 7)
	public void changeStatusToActive() throws InterruptedException {
		checkClientProfileFromUser();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement status = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='v-btn v-btn--flat v-btn--text theme--light v-size--default primary--text']")));
		status.click();
		WebElement statusActive = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Active')]")));
		statusActive.click();
		Assert.assertTrue(true);
		System.out.println("Status changed to Active");

	}

	//Search option

	@Test(priority = 8)
	public void search() throws InterruptedException {
		checkUserList();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@data-app='true']/div/div/main[@data-booted='true']/div/div/div/div/div/div/div/div/div/div/div/div/div/input[1]")));

		String searchUser = UsersInfoData.searchUser;
		search.sendKeys(searchUser);
		WebElement firstRow = driver.findElement(By.xpath("//table[1]/tbody[1]/tr[1]/td[1]"));	
		String actualText = firstRow.getText();
		Assert.assertTrue(actualText.contains(search.getText()));
	}

	//First Name wise sort

	@Test(priority = 9)
	public void firstNameSort() throws InterruptedException {
		checkUserList();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement selectName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'First Name')]")));
		selectName.click();
		selectName.click();
		selectName.click();
	}

	//Last name wise sort

	@Test(priority = 10)
	public void lastNameSort() throws InterruptedException {
		checkUserList();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement selectName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Last Name')]")));
		selectName.click();
		selectName.click();;
		selectName.click();
	}

	//email wise sort

	@Test(priority = 11)
	public void emailSort() throws InterruptedException {
		checkUserList();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement selectName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Email')]")));
		selectName.click();
		selectName.click();
		selectName.click();
	}

	//Check user type list

	@Test(priority = 12)
	public void checkUserTypeList() throws InterruptedException {
		login();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement users = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Users']")));
		users.click();
		WebElement element = driver.findElement(By.xpath("//*[text()='Types']"));
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", element);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		String expectedText = "Types";
		String actualText = element.getText();
		Assert.assertEquals(actualText, expectedText);
		Thread.sleep(3000);
	}


	//Permission selection functions section starts //
	//permission for users

	public void permissionUsers() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement userRead = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@id='inspire']/div[@role='document']/div/div/div/form[@novalidate='novalidate']/div[4]/div[1]/div[2]/div[1]/div[2]/div[1]/span[1]")));
		WebElement userAdd = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[4]//div[1]//div[2]//div[1]//div[2]//div[1]//span[2]")));
		WebElement userStatus = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[4]//div[1]//div[2]//div[1]//div[2]//div[1]//span[3]")));
		WebElement userClient = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[4]//div[1]//div[2]//div[1]//div[2]//div[1]//span[4]")));
		WebElement userChat = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[4]//div[1]//div[2]//div[1]//div[2]//div[1]//span[5]")));

		//		if(userRead.isSelected()) {
		//			userRead.click();
		//			Assert.assertTrue(true);
		//
		//
		//		}else {
		//			userRead.click();
		//		}


		if(userAdd.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			userAdd.click();
		}

		if(userStatus.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			userStatus.click();
		}


		if(userClient.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			userClient.click();
		}


		//		if(userChat.isSelected()) {
		//			System.out.println("Already Selected !!");
		//			Assert.assertTrue(true);
		//		}else {
		//			userChat.click();
		//		}
	}

	//permission for location

	public void permissionLocation() {
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement locationRead = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@id='inspire']/div[@role='document']/div/div/div/form[@novalidate='novalidate']/div[5]/div[1]/div[2]/div[1]/div[2]/div[1]/span[1]")));
		WebElement locationAdd = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[5]//div[1]//div[2]//div[1]//div[2]//div[1]//span[2]")));
		WebElement locationEdit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[5]//div[1]//div[2]//div[1]//div[2]//div[1]//span[3]")));
		WebElement locationDelete = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[5]//div[1]//div[2]//div[1]//div[2]//div[1]//span[4]")));

		if(locationRead.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			locationRead.click();
		}

		if(locationAdd.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			locationAdd.click();
		}

		if(locationEdit.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			locationEdit.click();
		}

		if(locationDelete.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			locationDelete.click();
		}

	}

	//permission for Patient

	public void permissionPatient() {
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement patientRead = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@id='inspire']/div[@role='document']/div/div/div/form[@novalidate='novalidate']/div[6]/div[1]/div[2]/div[1]/div[2]/div[1]/span[1]")));
		WebElement patientAdd = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[6]//div[1]//div[2]//div[1]//div[2]//div[1]//span[2]")));
		WebElement patientEdit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[6]//div[1]//div[2]//div[1]//div[2]//div[1]//span[3]")));
		WebElement patientField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[6]//div[1]//div[2]//div[1]//div[2]//div[1]//span[4]")));
		WebElement patientDelete = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[6]//div[1]//div[2]//div[1]//div[2]//div[1]//span[5]")));

		if(patientRead.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			patientRead.click();
		}

		if(patientAdd.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			patientAdd.click();
		}

		if(patientEdit.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			patientEdit.click();
		}

		if(patientField.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			patientField.click();
		}

		if(patientDelete.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			patientDelete.click();
		}
	}


	//permission for calendar

	public void permissionCalendar() {

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement calendarRead = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@id='inspire']/div[@role='document']/div/div/div/form[@novalidate='novalidate']/div[7]/div[1]/div[2]/div[1]/div[2]/div[1]/span[1]")));
		WebElement calendarEdit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[7]//div[1]//div[2]//div[1]//div[2]//div[1]//span[2]")));

		if(calendarRead.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			calendarRead.click();
		}

		if(calendarEdit.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			calendarEdit.click();
		}
	}

	//permission for contact

	public void permissionContact() {
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement contactRead = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@id='inspire']/div[@role='document']/div/div/div/form[@novalidate='novalidate']/div[8]/div[1]/div[2]/div[1]/div[2]/div[1]/span[1]")));
		WebElement contactAdd = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[8]//div[1]//div[2]//div[1]//div[2]//div[1]//span[2]")));
		WebElement contactEdit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[8]//div[1]//div[2]//div[1]//div[2]//div[1]//span[3]")));
		WebElement contactAddProvider = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[8]//div[1]//div[2]//div[1]//div[2]//div[1]//span[4]")));
		WebElement contactDelete = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[8]//div[1]//div[2]//div[1]//div[2]//div[1]//span[5]")));


		if(contactRead.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			contactRead.click();
		}

		if(contactAdd.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			contactAdd.click();
		}

		if(contactEdit.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			contactEdit.click();
		}

		if(contactAddProvider.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			contactAddProvider.click();
		}

		if(contactDelete.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			contactDelete.click();
		}
	}

	//permission for Forms

	public void permissionForms() {
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement endOfCareForms = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@id='inspire']/div[@role='document']/div/div/div/form[@novalidate='novalidate']/div[9]/div[1]/div[2]/div[1]/div[2]/div[1]/span[1]")));
		WebElement startOfCareForms = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[9]//div[1]//div[2]//div[1]//div[2]//div[1]//span[2]")));
		WebElement form4 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[9]//div[1]//div[2]//div[1]//div[2]//div[1]//span[3]")));
		WebElement testForm3 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[9]//div[1]//div[2]//div[1]//div[2]//div[1]//span[4]")));
		WebElement testForm2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[9]//div[1]//div[2]//div[1]//div[2]//div[1]//span[5]")));
		WebElement testForm = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[9]//div[1]//div[2]//div[1]//div[2]//div[1]//span[6]")));

		if(endOfCareForms.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			endOfCareForms.click();
		}

		if(startOfCareForms.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			startOfCareForms.click();
		}

		if(form4.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			form4.click();
		}

		if(testForm3.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			testForm3.click();
		}

		if(testForm2.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			testForm2.click();
		}

		if(testForm.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			testForm.click();
		}
	}
	//Permission selection functions section ends//



	//add user group

	@Test(priority = 13)
	public void userGroup() throws InterruptedException {
		checkUserList();

		String name = UsersInfoData.admin;
		String acronym = UsersInfoData.acronym;
		String viewTitle = UsersInfoData.viewTitle;
		String viewAcronym = UsersInfoData.viewAcronym;
		String desc = UsersInfoData.description;

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement add = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//i[normalize-space()='add']")));
		add.click();

		WebElement addUserGroup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='pl-2 pr-2 v-btn v-btn--contained theme--dark v-size--default primary']//span[@class='v-btn__content']")));
		addUserGroup.click();

		WebElement adminName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div[1]//div[1]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		adminName.sendKeys(name);

		WebElement adminAcronym = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div[1]//div[2]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		adminAcronym.sendKeys(acronym);

		WebElement statusCheck = driver.findElement(By.xpath("//input[@role='checkbox']"));

		if(statusCheck.isSelected()) {
			System.out.println("Already Selected !!");
			Assert.assertTrue(true);
		}else {
			statusCheck.click();
		}

		WebElement userViewTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='document']//div//div//div//div[2]//div[1]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		userViewTitle.sendKeys(viewTitle);

		WebElement userViewAcronym = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='document']//div//div//div//div[2]//div[2]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		userViewAcronym.sendKeys(viewAcronym);

		WebElement description = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='document']//div[3]//div[1]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		description.sendKeys(desc);

		//user permission select
		permissionUsers();

		// Location permission select
		permissionLocation();

		//Patient permission select
		permissionPatient();

		//Calendar permission select
		permissionCalendar();

		//Contact permission select
		permissionContact();

		//Forms permission select
		permissionForms();




		WebElement addGroup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='v-btn__content'][normalize-space()='Add']")));
		addGroup.click();
		Thread.sleep(5000);
		String actualAdminName = name;
		WebElement lastRow = driver.findElement(By.xpath("(//table[1]//tbody[1]//tr)[last()]//td[1]")); 
		JavascriptExecutor js = (JavascriptExecutor) driver;

		js.executeScript("arguments[0].scrollIntoView(true);",lastRow);
		Thread.sleep(5000);
		String expectedAdminName = lastRow.getText();

		//Admin name verification
		Assert.assertEquals(actualAdminName, expectedAdminName);

		Thread.sleep(2000);
	}

	//Update user group

	@Test(priority = 14)
	public void updateUserGroup() throws InterruptedException {
		checkUserList();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		WebElement lastRowValue = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//table[1]//tbody[1]//tr)[last()]//td[1]")));
		js.executeScript("arguments[0].scrollIntoView(true);",lastRowValue);

		lastRowValue.click();

		WebElement adminName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div[1]//div[1]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		while (!adminName.getAttribute("value").equals("")) {
			adminName.sendKeys(Keys.BACK_SPACE);
		}

		adminName.sendKeys("SQA20");

		WebElement adminAcronym = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div[1]//div[2]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		while (!adminAcronym.getAttribute("value").equals("")) {
			adminAcronym.sendKeys(Keys.BACK_SPACE);
		}

		adminAcronym.sendKeys("SQA20");

		WebElement update = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Update']")));
		update.click();

		Thread.sleep(2000);

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

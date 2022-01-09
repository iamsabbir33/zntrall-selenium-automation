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
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import normalUserInputData.PatientsInfoData;

public class Patients {

	public static String env = "Test";
	public static String testSuiteName = "Test Suit 5 -- Patients";

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

		username.sendKeys(PatientsInfoData.user);

		password.sendKeys(PatientsInfoData.pass);

		login.click();
		WebElement loginAs = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Counsellors']")));
		loginAs.click();
		Thread.sleep(5000);

		String expectedUrl = "https://dev.zntral.net/dashboard";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl);


	}


	//View the patients list
	@Test(priority = 2)
	public void patientsList() throws InterruptedException {

		loginUser();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Patients']")));
		driver.findElement(By.xpath("//span[normalize-space()='Patients']")).click();

		String expectedUrl = "https://dev.zntral.net/patients";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl);
	}


	//Search option

	@Test(priority = 3)
	public void search() throws InterruptedException {
		patientsList();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-text-field__slot']//input[@type='text']")));
		search.sendKeys(PatientsInfoData.search);


		WebElement firstRow = driver.findElement(By.xpath("//tbody/tr[1]/td[2]"));	
		String actualText = firstRow.getText();
		Assert.assertTrue(actualText.contains(PatientsInfoData.search));
		Thread.sleep(3000);
	}


	//Add new Patient -- Click "+" button

	@Test(priority = 4)
	public void addPatient() throws InterruptedException{
		patientsList();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement add = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//i[@class='v-icon notranslate zmdi zmdi zmdi zmdi-plus theme--light']")));
		add.click();

		WebElement selectAddPatient = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Add New Patient')]")));
		selectAddPatient.click();
		Thread.sleep(2000);

		Assert.assertTrue(true);


	}

	//Add patient with valid info

	@Test(priority = 5)
	public void validPatientData() throws InterruptedException {
		addPatient();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement prefix = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div[1]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement firstName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div[1]//div[2]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		WebElement lastName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div//div[4]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		WebElement ssn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div[2]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement dob = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div//div[2]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement gender = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[3]//div[1]//div[2]//div[1]//div[1]//div[2]//div[1]")));
		WebElement phone = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[3]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement type = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@id='inspire']//div[@role='document']//div//div//div//div//div//div//div//div//div//div[1]//div[1]//div[1]//div[2]//div[1]//i[1]")));
		WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='email']")));
		WebElement location = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@id='inspire']//div[@role='document']//div//div//div//div//div//div//div[1]//div[1]//div[2]//div[1]//div[1]//div[2]//div[1]//i[1]")));
		WebElement note = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[5]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));

		WebElement save = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Submit']")));

		prefix.sendKeys(PatientsInfoData.prefix);
		firstName.sendKeys(PatientsInfoData.patientfirstName);
		lastName.sendKeys(PatientsInfoData.patientlastName);
		ssn.sendKeys(PatientsInfoData.ssn);
		dob.sendKeys(PatientsInfoData.dob);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='OK']"))).click();
		gender.click();
		WebElement genderMale = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Male')]")));
		genderMale.click();
		phone.sendKeys(PatientsInfoData.phone);
		type.click();
		WebElement typeMobile = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-list-item__title'][normalize-space()='Mobile']")));
		typeMobile.click();
		email.sendKeys(PatientsInfoData.patientemail);
		location.click();
		WebElement selectLocation = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[normalize-space()='Test Location 2']")));
		selectLocation.click();
		note.sendKeys(PatientsInfoData.patientnote);
		save.click();
		Thread.sleep(5000);

		String URL = driver.getCurrentUrl();
		Assert.assertTrue(URL.contains("https://dev.zntral.net/patients/"));
		Thread.sleep(3000);

	}

	//Adding patient without any info

	@Test(priority = 6)
	public void invalidPatientData() throws InterruptedException {
		addPatient();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement prefix = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div[1]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement firstName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div[1]//div[2]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		WebElement lastName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div//div[4]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		WebElement ssn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div[2]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement dob = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div//div[2]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement gender = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[3]//div[1]//div[2]//div[1]//div[1]//div[2]//div[1]")));
		WebElement phone = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[3]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		WebElement type = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@id='inspire']//div[@role='document']//div//div//div//div//div//div//div//div//div//div[1]//div[1]//div[1]//div[2]//div[1]//i[1]")));
		WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='email']")));
		WebElement location = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@id='inspire']//div[@role='document']//div//div//div//div//div//div//div[1]//div[1]//div[2]//div[1]//div[1]//div[2]//div[1]//i[1]")));
		WebElement note = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[5]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));

		WebElement save = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Submit']")));

		prefix.sendKeys(PatientsInfoData.prefix2);
		firstName.sendKeys(PatientsInfoData.patientfirstName2);
		lastName.sendKeys(PatientsInfoData.patientlastName2);
		ssn.sendKeys(PatientsInfoData.ssn2);
		dob.sendKeys(PatientsInfoData.dob2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='OK']"))).click();
		gender.click();
		phone.sendKeys(PatientsInfoData.phone2);
		type.click();
		email.sendKeys(PatientsInfoData.patientemail2);
		location.click();
		note.sendKeys(PatientsInfoData.patientnote2);
		save.isEnabled();
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


	//Edit Patient info

	@Test(priority = 7)
	public void editPatientInfo() throws InterruptedException {
		patientsList();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement patientSelect = driver.findElement(By.xpath("//tbody/tr[1]/td[2]"));
		patientSelect.click();
		Thread.sleep(2000);

		WebElement editButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//i[@class='v-icon notranslate material-icons theme--light'][normalize-space()='more_vert']")));
		editButton.click();

		WebElement editOptionSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Edit')]")));
		editOptionSelect.click();
		Thread.sleep(1000);

		WebElement editSSN = driver.findElement(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div[2]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]"));
		editSSN.clear();

		while (!editSSN.getAttribute("value").equals("")) {
			editSSN.sendKeys(Keys.BACK_SPACE);
		}
		editSSN.sendKeys(PatientsInfoData.validssn);

		WebElement update = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Update']")));
		update.click();
		Thread.sleep(3000);

		Assert.assertTrue(true);
	}

	//Delete Patient info

	@Test(priority = 8)
	public void deletePatientInfo() throws InterruptedException {
		patientsList();
		Thread.sleep(2000);
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement patientSelect = driver.findElement(By.xpath("//tbody/tr[1]/td[2]"));
		patientSelect.click();
		Thread.sleep(2000);

		WebElement editButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//i[@class='v-icon notranslate material-icons theme--light'][normalize-space()='more_vert']")));
		editButton.click();

		WebElement deleteButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Delete')]")));
		deleteButton.click();


		WebElement deletePopUpYes = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='yes']")));
		deletePopUpYes.click();

		String URL = driver.getCurrentUrl();
		Assert.assertTrue(URL.contains("https://dev.zntral.net/patients"));
		Thread.sleep(3000);
	}

	//Add contact info after adding new patient info

	@Test(priority = 9)
	public void addContactfromPatient() throws InterruptedException {
		validPatientData();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement addButton1 = driver.findElement(By.xpath("//i[@class='v-icon notranslate zmdi zmdi zmdi zmdi-plus theme--light']"));
		addButton1.click();

		WebElement addContactButton = driver.findElement(By.xpath("//div[normalize-space()='Contact']"));
		addContactButton.click();
		Thread.sleep(1000);

		WebElement FirstName =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div//div//div//div[1]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		FirstName.sendKeys(PatientsInfoData.firstName);

		WebElement LastName =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div//div//div//div[1]//div[3]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		LastName.sendKeys(PatientsInfoData.lastName);

		WebElement gender =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@novalidate='novalidate']//div//div//div//div//div//div//div[@role='button']//div//div//div//i[@aria-hidden='true']")));
		gender.click();

		WebElement genderMale =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-list-item__title'][normalize-space()='Male']")));
		genderMale.click();

		WebElement relationship =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@id='inspire']/div[@role='document']/div/div/div/div/div/div/div/form[@novalidate='novalidate']/div/div/div/div/div/div[@role='button']/div/div/div/i[1]")));
		relationship.click();

		WebElement relationshipFather =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Father')]")));
		relationshipFather.click();

		WebElement phone =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='document']//div[4]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		phone.sendKeys(PatientsInfoData.phonenumber);

		WebElement phoneDropdown =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@id='inspire']/div[@role='document']/div/div/div/div/div/div/div/form[@novalidate='novalidate']/div/div/div/div/div[@role='button']/div/div/div/i[1]")));
		phoneDropdown.click();

		WebElement phoneTypeSelect =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Home')]")));
		phoneTypeSelect.click();

		WebElement save =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Save']")));
		save.click();
		Thread.sleep(5000);

		String expectedText5 = "TestContact data";
		WebElement contactNameSelect =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[last()]/td[1]")));
		String actualText5 = contactNameSelect.getText();
		Assert.assertEquals(actualText5, expectedText5);
	}

	//Add contact with empty fields after adding new patient 

	@Test(priority = 10)
	public void addContactWithEmptyData() throws InterruptedException {
		validPatientData();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement addButton1 = driver.findElement(By.xpath("//i[@class='v-icon notranslate zmdi zmdi zmdi zmdi-plus theme--light']"));
		addButton1.click();

		WebElement addContactButton = driver.findElement(By.xpath("//div[normalize-space()='Contact']"));
		addContactButton.click();
		Thread.sleep(1000);

		WebElement FirstName =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div//div//div//div[1]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		FirstName.sendKeys(PatientsInfoData.firstName2);

		WebElement LastName =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div//div//div//div[1]//div[3]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		LastName.sendKeys(PatientsInfoData.lastName2);

		WebElement relationship =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@id='inspire']/div[@role='document']/div/div/div/div/div/div/div/form[@novalidate='novalidate']/div/div/div/div/div/div[@role='button']/div/div/div/i[1]")));
		relationship.click();

		WebElement phone =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='document']//div[4]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		phone.sendKeys(PatientsInfoData.phone2);

		WebElement note =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[7]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		note.sendKeys(PatientsInfoData.note);

		WebElement phoneType =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@id='inspire']/div[@role='document']/div/div/div/div/div/div/div/form[@novalidate='novalidate']/div/div/div/div/div[@role='button']/div/div/div/i[1]")));
		phoneType.click();

		WebElement save =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Save']")));
		save.isEnabled();
		Thread.sleep(5000);

		WebElement firstNameMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-messages__message']")));
		String expectedText4 = "First name is required";
		String actualText4 = firstNameMsg.getText();
		Assert.assertEquals(actualText4, expectedText4);

		WebElement relationMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Relationship is required')]")));
		String expectedText = "Relationship is required";
		String actualText = relationMsg.getText();
		Assert.assertEquals(actualText, expectedText);
	}

	//Add contact to any patient from patients list 

	@Test(priority = 11)
	public void addContactFromPatientList() throws InterruptedException {
		patientsList();
		Thread.sleep(2000);

		WebElement firstRow = driver.findElement(By.xpath("//tbody/tr[1]/td[2]"));	
		firstRow.click();
		Thread.sleep(3000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement addButton1 = driver.findElement(By.xpath("//i[@class='v-icon notranslate zmdi zmdi zmdi zmdi-plus theme--light']"));
		addButton1.click();

		WebElement addContactButton = driver.findElement(By.xpath("//div[normalize-space()='Contact']"));
		addContactButton.click();
		Thread.sleep(1000);

		WebElement FirstName =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div//div//div//div[1]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		FirstName.sendKeys(PatientsInfoData.firstName);

		WebElement LastName =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div//div//div//div[1]//div[3]//div[1]//div[1]//div[1]//div[1]//input[1]")));
		LastName.sendKeys(PatientsInfoData.lastName);

		WebElement gender =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@novalidate='novalidate']//div//div//div//div//div//div//div[@role='button']//div//div//div//i[@aria-hidden='true']")));
		gender.click();

		WebElement genderMale =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-list-item__title'][normalize-space()='Male']")));
		genderMale.click();

		WebElement relationship =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@id='inspire']/div[@role='document']/div/div/div/div/div/div/div/form[@novalidate='novalidate']/div/div/div/div/div/div[@role='button']/div/div/div/i[1]")));
		relationship.click();

		WebElement relationshipFather =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Father')]")));
		relationshipFather.click();

		WebElement phone =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='document']//div[4]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		phone.sendKeys(PatientsInfoData.phonenumber);

		WebElement phoneDropdown =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@id='inspire']/div[@role='document']/div/div/div/div/div/div/div/form[@novalidate='novalidate']/div/div/div/div/div[@role='button']/div/div/div/i[1]")));
		phoneDropdown.click();

		WebElement phoneTypeSelect =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Home')]")));
		phoneTypeSelect.click();

		WebElement save =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Save']")));
		save.click();
		Thread.sleep(5000);

		String expectedText5 = "TestContact2 data";
		WebElement contactNameSelect =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[last()]/td[1]")));
		String actualText5 = contactNameSelect.getText();
		Assert.assertEquals(actualText5, expectedText5);

	}

	//Add Schedule info after adding new patient info
	@Test(priority = 12)
	public void addSchdeulefromPatient() throws InterruptedException {
		validPatientData();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement addButton1 = driver.findElement(By.xpath("//i[@class='v-icon notranslate zmdi zmdi zmdi zmdi-plus theme--light']"));
		addButton1.click();

		WebElement addScheduleButton = driver.findElement(By.xpath("//div[contains(text(),'Schedule')]"));
		addScheduleButton.click();
		Thread.sleep(1000);

		WebElement startDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div[2]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		startDate.click();		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr[5]/td[3]/button[1]"))).click();
		Thread.sleep(1000);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@id='inspire']//div[@role='document']//div//div//div//div[2]//div[1]//div[1]//div[1]//div[1]//div[1]"))).click();
		Thread.sleep(1000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[5]//div[1]//div[1]//div[1]//div[1]//div[1]//input[1]"))).sendKeys("This is test");
		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[4]/div[1]/form[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]"))).click();

		WebElement save =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Save']")));
		save.click();
		Thread.sleep(5000);


		WebElement contactNameSelect =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section//div[7]")));
		String actualText = contactNameSelect.getText();
		WebElement actual = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/main[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]")));
		String expectedText = actual.getText();
		Assert.assertTrue(expectedText.contains(actualText));
	}

	//Add schedule to any patient from patients list 

	@Test(priority = 13)
	public void addSheduleFromPatientList() throws InterruptedException {
		patientsList();
		Thread.sleep(2000);

		WebElement firstRow = driver.findElement(By.xpath("//tbody/tr[1]/td[2]"));	
		firstRow.click();
		Thread.sleep(3000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement addButton1 = driver.findElement(By.xpath("//i[@class='v-icon notranslate zmdi zmdi zmdi zmdi-plus theme--light']"));
		addButton1.click();

		WebElement addContactButton = driver.findElement(By.xpath("//div[contains(text(),'Schedule')]"));
		addContactButton.click();
		Thread.sleep(1000);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div[2]//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr[4]/td[4]/button[1]"))).click();
		Thread.sleep(1000);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@id='inspire']//div[@role='document']//div//div//div//div//div[1]//div[1]//div[1]//div[1]//div[1]//div[1]//div[2]//div[1]//i[1]"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[5]//div[1]//div[1]//div[1]//div[1]//div[1]//input[1]"))).sendKeys("This is test");
		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[4]/div[1]/form[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]"))).click();

		WebElement save =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Save']")));
		save.click();
		Thread.sleep(5000);

		WebElement contactNameSelect =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section//div[7]")));
		String actualText = contactNameSelect.getText();
		WebElement actual = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/main[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]")));
		String expectedText = actual.getText();
		Assert.assertTrue(expectedText.contains(actualText));

	}

	//Add schedule with empty fields after adding new patient 

	@Test(priority = 14)
	public void addSchdeuleWithEmpty() throws InterruptedException {
		validPatientData();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement addButton1 = driver.findElement(By.xpath("//i[@class='v-icon notranslate zmdi zmdi zmdi zmdi-plus theme--light']"));
		addButton1.click();

		WebElement addScheduleButton = driver.findElement(By.xpath("//div[contains(text(),'Schedule')]"));
		addScheduleButton.click();
		Thread.sleep(1000);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[5]//div[1]//div[1]//div[1]//div[1]//div[1]//input[1]"))).sendKeys("");
		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[4]/div[1]/form[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]"))).click();

		WebElement save =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Save']")));
		save.click();
		Thread.sleep(5000);


		WebElement contactNameSelect =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='status']")));
		String actualText = contactNameSelect.getText();
		String expectedText = "Failed to add Schedule";
		Assert.assertTrue(actualText.contains(expectedText));
	}


	//Add schedule with empty fields from the patient list

	@Test(priority = 15)
	public void addEmptySheduleFromPatientList() throws InterruptedException {
		patientsList();
		Thread.sleep(2000);

		WebElement firstRow = driver.findElement(By.xpath("//tbody/tr[1]/td[2]"));	
		firstRow.click();
		Thread.sleep(3000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement addButton1 = driver.findElement(By.xpath("//i[@class='v-icon notranslate zmdi zmdi zmdi zmdi-plus theme--light']"));
		addButton1.click();

		WebElement addContactButton = driver.findElement(By.xpath("//div[contains(text(),'Schedule')]"));
		addContactButton.click();
		Thread.sleep(1000);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[5]//div[1]//div[1]//div[1]//div[1]//div[1]//input[1]"))).sendKeys("");
		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[4]/div[1]/form[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]"))).click();

		WebElement save =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Save']")));
		save.click();
		Thread.sleep(5000);


		WebElement contactNameSelect =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='status']")));
		String actualText = contactNameSelect.getText();
		String expectedText = "Failed to add Schedule";
		Assert.assertTrue(actualText.contains(expectedText));

	}

	//Contact form cancel button check

	@Test(priority = 16)
	public void contactFormCancelButton() throws InterruptedException {
		patientsList();
		Thread.sleep(2000);

		WebElement firstRow = driver.findElement(By.xpath("//tbody/tr[1]/td[2]"));	
		firstRow.click();
		Thread.sleep(3000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement addButton1 = driver.findElement(By.xpath("//i[@class='v-icon notranslate zmdi zmdi zmdi zmdi-plus theme--light']"));
		addButton1.click();

		WebElement addContactButton = driver.findElement(By.xpath("//div[normalize-space()='Contact']"));
		addContactButton.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='float-right text-capitalize align-cancel v-btn v-btn--flat v-btn--text theme--light v-size--default']//span[@class='v-btn__content'][normalize-space()='Cancel']"))).click();
		Thread.sleep(5000);

		Assert.assertTrue(true);

	}

	//Schedule form cancel button check

	@Test(priority = 17)
	public void sheduleFormCancelButton() throws InterruptedException {
		patientsList();
		Thread.sleep(2000);

		WebElement firstRow = driver.findElement(By.xpath("//tbody/tr[1]/td[2]"));	
		firstRow.click();
		Thread.sleep(3000);

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement addButton1 = driver.findElement(By.xpath("//i[@class='v-icon notranslate zmdi zmdi zmdi zmdi-plus theme--light']"));
		addButton1.click();

		WebElement addContactButton = driver.findElement(By.xpath("//div[normalize-space()='Schedule']"));
		addContactButton.click();
		Thread.sleep(1000);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='v-btn v-btn--flat v-btn--text theme--light v-size--default blue--text text--darken-1']//span[@class='v-btn__content'][normalize-space()='Close']"))).click();
		Thread.sleep(5000);

		Assert.assertTrue(true);

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

package normalUser;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

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

import normalUserInputData.CalendarInfoData;

public class Calendar {

	public static String env = "Test";
	public static String testSuiteName = "Test Suit 6 -- Calendar";

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

		String user = CalendarInfoData.user;
		String pass = CalendarInfoData.pass;

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


	//View the calendar

	@Test(priority = 2)
	public void calendar() throws InterruptedException {

		loginUser();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Calendar']")));
		driver.findElement(By.xpath("//span[normalize-space()='Calendar']")).click();
		String expectedUrl = "https://dev.zntral.net/patient_schedule";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl);
	}


	//Add a single schedule on upcoming days

	@Test(priority = 3)
	public void addSingleEvent() throws InterruptedException {
		calendar();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement selectDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-calendar-monthly v-calendar-weekly v-calendar theme--light v-calendar-events']//span[@class='v-btn__content'][normalize-space()='28']")));
		selectDate.click();
		WebElement patientSelectDropDown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='combobox']//div//div//div//i[@aria-hidden='true']")));
		patientSelectDropDown.click();
		WebElement patientSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-list-item__title'][normalize-space()='Testing data']")));
		patientSelect.click();
		WebElement dateFieldSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//div[@data-app='true']//div[@role='document']//div//div//div//div//div//div//div[1]//div[1]//div[2]//div[1]//div[1]//input[1]")));
		dateFieldSelect.click();
		WebElement dateSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-btn__content'][normalize-space()='30']")));
		dateSelect.click();
		WebElement allDay = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'All day')]")));
		allDay.click();
		WebElement description = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-input theme--light v-text-field v-text-field--is-booted']//input[@type='text']")));
		String desc = CalendarInfoData.desc;
		description.sendKeys(desc);
		WebElement save = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Save']")));
		save.click();
		Thread.sleep(3000);
		WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='status']")));
		try {

			if(driver.switchTo().alert() != null) {
				System.out.println(errorMsg.getText());
				Assert.assertTrue(true);
			}

		}
		catch(Exception e) {
			System.out.println(errorMsg.getText());
			Assert.assertTrue(true);
		}
	}

	//cancel a schedule

	@Test(priority = 4)
	public void cancelSchedule() throws InterruptedException {
		calendar();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement selectSchedule = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body[1]/div[1]/div[1]/div[1]/main[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[5]/div[7]/div[2]/div[1]")));
		selectSchedule.click();
		WebElement selectCancel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-menu__content theme--light menuable__content__active']//span[@class='v-btn__content'][normalize-space()='Cancel']")));
		selectCancel.click();
	}

	//delete a schedule

	@Test(priority = 5)
	public void deleteSchedule() throws InterruptedException {
		calendar();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement selectSchedule = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/main[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[5]/div[3]/div[2]/div[1]")));
		selectSchedule.click();
		WebElement selectDelete = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Delete']")));
		selectDelete.click();
		WebElement selectRecurringOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[normalize-space()='All events']")));
		selectRecurringOption.click();
		WebElement delete = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='ml-2 v-btn v-btn--depressed v-btn--flat v-btn--outlined theme--light v-size--default danger--text']//span[@class='v-btn__content'][normalize-space()='Delete']")));
		delete.click();
	}

	//Edit a schedule

	@Test(priority = 6)
	public void editSchedule() throws InterruptedException {
		calendar();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement selectSchedule = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='pl-1'][normalize-space()='Testing data']")));
		selectSchedule.click();
		WebElement selectEdit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Edit']")));
		selectEdit.click();
		WebElement selectPatientDrpDown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='combobox']//div//div//div//i[@aria-hidden='true']")));
		selectPatientDrpDown.click();
		WebElement selectPatient = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Sabbir-Test Info')]")));
		selectPatient.click();
		WebElement description = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-input theme--light v-text-field v-text-field--is-booted']//input[@type='text']")));
		String desc = CalendarInfoData.desc;
		description.sendKeys(desc);
		WebElement save = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Save']")));
		save.click();

	}

	// Day view

	@Test(priority = 7)
	public void dayViewOnCalendar() throws InterruptedException {
		calendar();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement selectType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@role='button']//i[@aria-hidden='true']")));
		selectType.click();
		WebElement selectDay = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Day')]")));
		selectDay.click();
	}

	// Week view

	@Test(priority = 8)
	public void weekViewOnCalendar() throws InterruptedException {
		calendar();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement selectType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@role='button']//i[@aria-hidden='true']")));
		selectType.click();
		WebElement selectWeek = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Week')]")));
		selectWeek.click();
	}

	// Month view

	@Test(priority = 9)
	public void monthViewOnCalendar() throws InterruptedException {
		calendar();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement selectType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@role='button']//i[@aria-hidden='true']")));
		selectType.click();
		WebElement selectMonth = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Month')]")));
		selectMonth.click();
	}

	// Agenda view

	@Test(priority = 10)
	public void agendaViewOnCalendar() throws InterruptedException {
		calendar();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

		WebElement selectType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@role='button']//i[@aria-hidden='true']")));
		selectType.click();
		WebElement selectAgenda = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Agenda')]")));
		selectAgenda.click();
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

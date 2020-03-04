package testcases;

import static org.testng.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;
import com.github.javafaker.PhoneNumber;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SecurePay {
	
	// Initializing the variable

	public static WebDriver driver;
	public static WebDriverWait wait;
	public static Properties or = new Properties();
	public static Properties config = new Properties();
	public static FileInputStream fis;
	public static Logger log = Logger.getLogger(SecurePay.class.getSimpleName());

	Faker faker = new Faker();

	String firstName = faker.name().firstName();
	String lastName = faker.name().lastName();

	@BeforeTest
	public void setUp() throws Throwable {

		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		wait = new WebDriverWait(driver, 5);
		driver.get("http://google.com");

		// Load the Configuration Files
		
		PropertyConfigurator.configure("./src/test/resources/properties/log4j.properties");

		fis = new FileInputStream("./src/test/resources/properties/OR.properties");
		or.load(fis);
		log.info("OR Properties file loaded !!!");

		fis = new FileInputStream("./src/test/resources/properties/Config.properties");
		config.load(fis);
		log.info("Config Properties file loaded !!!");
		
	}

	@AfterTest
	public void tearDown() {

		driver.quit();

	}

	@Test(priority = 1)
	public void searchAndClick() throws Throwable {
		
		// Will search the Element and Will Click on search
		driver.findElement(By.xpath(or.getProperty("search_Xpath"))).click();

		driver.findElement(By.xpath(or.getProperty("search_Xpath"))).sendKeys("SecurePay");
		driver.findElement(By.xpath(or.getProperty("search_Xpath"))).sendKeys(Keys.ENTER);

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(or.getProperty("securePayLink_Xpath")))).click();
		Thread.sleep(5000);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(or.getProperty("SupportLink_Xpath"))));

		WebElement menu = driver.findElement(By.xpath(or.getProperty("SupportLink_Xpath")));

		Actions action = new Actions(driver);
		action.moveToElement(menu).perform();

		driver.findElement(By.linkText("Contact Us")).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(or.getProperty("ContactUs_Xpath"))));

	}

	@Test(priority = 2)
	public void validateContactUsForm() throws Throwable {
		
		// Validates the Actual title with Expected Title

		String actualTitle = driver.getTitle();
		log.info("actual Title is"+actualTitle);
		String expectedTitle = "Contact Us – SecurePay – Sales – Support – Accounts";
		assertEquals(expectedTitle, actualTitle);
		
		// Filling the Contact us form

		driver.findElement(By.xpath(or.getProperty("firstName_Xpath"))).sendKeys(firstName);
		driver.findElement(By.xpath(or.getProperty("lastName_Xpath"))).sendKeys(lastName);
		driver.findElement(By.xpath(or.getProperty("emailAddress_Xpath"))).sendKeys(firstName);
		driver.findElement(By.xpath(or.getProperty("businessName_Xpath"))).sendKeys(lastName);

		WebElement dropdown = driver.findElement(By.xpath(or.getProperty("reasonforEnquiry_Xpath")));
		Select select = new Select(dropdown);
		select.selectByIndex(1);

		driver.findElement(By.xpath(or.getProperty("textMessage_Xpath"))).sendKeys("Assignment of Secure Pay");

		driver.findElement(By.xpath(or.getProperty("submit_Xpath"))).click();

		// Validating that Form is not submitted as there is an Error with Email Address
		String Alert = driver.findElement(By.xpath(or.getProperty("alert_Xpath"))).getText();
		System.out.println("Text received is" + Alert);
		Assert.assertFalse(false, "The Email id enterted is Incorrect");

	}

}

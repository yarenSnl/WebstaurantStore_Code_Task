package Utilities;

import Pages.ActionHelper;
import Pages.CartPage;
import Pages.SearchPage;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.WebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;

public class TestSetup {
    protected WebDriver driver;
    protected SearchPage searchPage;
    protected CartPage cartPage;
    protected ActionHelper actionHelper;
    protected ExtentReports extent;
    protected ExtentTest test;

    /**
     * Initializes the test environment before any tests are run.
     * This includes setting up the ExtentReports, WebDriver, and navigating to the specified URL.
     * It also prepares the page objects needed for the tests.
     */
    @BeforeClass
    @Parameters({"browserType", "baseUrl"})
    public void setUp(String browserType, String baseUrl) {

        // Initialize ExtentReports
        extent = new ExtentReports();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss").format(Calendar.getInstance().getTime());
        ExtentSparkReporter spark = new ExtentSparkReporter("Test Reports/Report " + timeStamp + ".html");
        extent.attachReporter(spark);

        // Initialize WebDriver based on the provided browser type
        test = extent.createTest("Webstaurant Store Test");
        if (browserType.equalsIgnoreCase("Firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else if (browserType.equalsIgnoreCase("Chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else {
            throw new IllegalArgumentException("Browser type " + browserType + " is not supported.");
        }

        // Configure WebDriver with default settings
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

        // Navigate to the base URL
        driver.get(baseUrl);
        test.info("Navigate to '" + baseUrl + "'.");


        // Initialize page objects and helpers
        actionHelper = new ActionHelper(driver, WebDriverManagerClass.getWebDriverWait());
        searchPage = new SearchPage(driver, actionHelper, test);
        cartPage = new CartPage(driver, actionHelper, searchPage, test);
    }

    /**
     * Cleans up after each test method.
     * This method logs the test result, quits the WebDriver, and flushes the Extent Reports data.
     */
    @AfterMethod
    public void methodTearDown(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                test.fail(result.getThrowable());
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                test.pass("Test performed successfully.");
            } else {
                test.skip("Test skipped");
            }
        } finally {
            WebDriverManagerClass.quitDriver();
            extent.flush();
        }
    }

    /**
     * Performs final cleanup after all tests in this class have been run.
     * This includes quitting any remaining instances of the WebDriver.
     */
    @AfterClass
    public void tearDown() {
        try {
            WebDriverManagerClass.quitDriver();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
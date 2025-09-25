// Specifies the package where this class belongs.
package base;

// Import necessary classes from different libraries.
import driver.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;

import utils.ConfigReader;
import io.qameta.allure.testng.AllureTestNg;
import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * This annotation tells TestNG to use the AllureTestNg listener with this test class.
 * AllureTestNg listens for test events (start, stop, pass, fail) and creates the data needed for Allure reports.
 */
@Listeners({AllureTestNg.class})
public class BaseTest {

    // A protected variable to hold the WebDriver instance, accessible by child test classes.
    protected WebDriver driver;

    // A protected variable for the configuration reader, accessible by child classes.
    protected ConfigReader config;

    // A static final logger instance for this class, used to log information and errors.
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    /**
     * Set Allure results directory for Maven-compatible reporting.
     * This runs once before the suite starts.
     */
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        System.setProperty("allure.results.directory", "target/allure-results");
        logger.info("Set Allure results directory to target/allure-results");
    }

    /**
     * This method is marked with @BeforeMethod, so TestNG will run it before each @Test method.
     * It's responsible for setting up the test environment.
     */
    @BeforeMethod
    public void setUp() {
        logger.info("========== Test Setup Started ==========");

        // Initialize ConfigReader to load config properties
        config = new ConfigReader("src/test/resources/config.properties");

        // Read browser name from config
        String browser = config.getProperty("browser");
        logger.info("Loaded browser from config: {}", browser);

        // Setup Chrome options
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-logging"});
        logger.debug("ChromeOptions configured to suppress extra logs.");

        // Initialize WebDriver via DriverFactory
        driver = DriverFactory.initDriver(browser, options);
        logger.info("WebDriver initialized for browser: {}", browser);
    }

    /**
     * This method is marked with @AfterMethod, so TestNG will run it after each @Test method.
     * It's responsible for cleaning up the environment after the test is complete.
     */
    @AfterMethod
    public void tearDown() {
        logger.info("========== Test Teardown Started ==========");
        DriverFactory.quitDriver();
        logger.info("WebDriver closed successfully.");
    }

    /**
     * This method runs once after all test methods in the suite have completed.
     * It attaches a summary to Allure only. Report generation is handled by Maven.
     */
    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        logger.info("========== Test Suite Completed ==========");

        // Attach a simple test summary to the Allure report
        String summary = "Test Suite execution finished.\n"
                + "Environment: " + config.getProperty("env") + "\n"
                + "Browser: " + config.getProperty("browser");

        Allure.addAttachment("Test Suite Summary",
                new ByteArrayInputStream(summary.getBytes(StandardCharsets.UTF_8)));
    }
}

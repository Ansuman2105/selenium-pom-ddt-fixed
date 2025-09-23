package base;

import driver.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import utils.ConfigReader;
import io.qameta.allure.testng.AllureTestNg; // ✅ Allure import

@Listeners({AllureTestNg.class}) // ✅ Register Allure listener
public class BaseTest {
    protected WebDriver driver;
    protected ConfigReader config;
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    @BeforeMethod
    public void setUp() {
        logger.info("========== Test Setup Started ==========");

        // Initialize config
        config = new ConfigReader("src/test/resources/config.properties");
        String browser = config.getProperty("browser");
        logger.info("Loaded browser from config: {}", browser);

        // Prepare ChromeOptions to suppress ChromeDriver/CDP logs if browser is Chrome
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-logging"});
        logger.debug("ChromeOptions configured to suppress extra logs.");

        // Initialize driver via DriverFactory
        driver = DriverFactory.initDriver(browser, options);
        logger.info("WebDriver initialized for browser: {}", browser);
    }

    @AfterMethod
    public void tearDown() {
        logger.info("========== Test Teardown Started ==========");
        DriverFactory.quitDriver();
        logger.info("WebDriver closed successfully.");
    }
}

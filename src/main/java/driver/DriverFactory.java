// Specifies the package where this class belongs.
package driver;

// Import necessary classes from different libraries.
import org.apache.logging.log4j.LogManager; // For logging using Log4j2.
import org.apache.logging.log4j.Logger; // The Logger interface for Log4j2.
import org.openqa.selenium.WebDriver; // The main interface for browser automation.
import org.openqa.selenium.chrome.ChromeDriver; // Class to create a new Chrome browser session.
import org.openqa.selenium.chrome.ChromeOptions; // To configure Chrome browser options.
import org.openqa.selenium.firefox.FirefoxDriver; // Class to create a new Firefox browser session.
import io.github.bonigarcia.wdm.WebDriverManager; // A library that automatically manages WebDriver binaries.

import java.nio.file.Files; // For creating temporary directories
import java.nio.file.Path;

public class DriverFactory {

    // A private static variable to hold the single WebDriver instance (singleton pattern).
    private static WebDriver driver;

    // A static final logger instance for this class, used to log information and errors.
    private static final Logger logger = LogManager.getLogger(DriverFactory.class);

    /**
     * Initializes the WebDriver for a given browser name.
     * This method is 'static', so it can be called directly on the class (e.g., DriverFactory.initDriver("chrome")).
     * @param browser The name of the browser to initialize (e.g., "chrome", "firefox").
     * @return The initialized WebDriver instance.
     */
    public static WebDriver initDriver(String browser) {
        logger.info("Initializing driver for browser: {}", browser);

        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();

            // Always use ChromeOptions (even for local runs).
            ChromeOptions options = new ChromeOptions();

            // Detect if running inside GitHub Actions (CI environment).
            boolean isCI = "true".equalsIgnoreCase(System.getenv("CI"));

            if (isCI) {
                logger.info("Running in GitHub Actions (headless mode). Applying CI-specific Chrome options.");
                options.addArguments("--headless=new");                // Headless mode
                options.addArguments("--disable-gpu");                 // Disable GPU (not available in CI)
                options.addArguments("--window-size=1920,1080");       // Ensure full HD resolution
                options.addArguments("--no-sandbox");                  // Required in CI to run Chrome
                options.addArguments("--disable-dev-shm-usage");       // Prevents memory issues in containers
                options.addArguments("--remote-allow-origins=*");      // Required for Chrome 111+

                try {
                    // ✅ Create a unique temporary Chrome profile directory
                    Path tempDir = Files.createTempDirectory("chrome-profile");
                    options.addArguments("--user-data-dir=" + tempDir.toString());
                    logger.info("Using temporary Chrome profile at: {}", tempDir.toString());
                } catch (Exception e) {
                    logger.error("Failed to create temp Chrome profile directory: {}", e.getMessage());
                }

            } else {
                logger.info("Running locally. Using default Chrome options.");
            }

            driver = new ChromeDriver(options);
            logger.info("ChromeDriver initialized successfully.");

        } else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
            logger.info("FirefoxDriver initialized successfully.");

        } else {
            logger.error("Browser not supported: {}", browser);
            throw new IllegalArgumentException("Browser not supported: " + browser);
        }

        driver.manage().window().maximize();
        logger.info("Browser window maximized.");
        return driver;
    }

    /**
     * An overloaded version of initDriver that accepts ChromeOptions.
     * This allows for initializing Chrome with custom configurations.
     * @param browser The name of the browser (should be "chrome").
     * @param options The ChromeOptions object with custom settings.
     * @return The initialized WebDriver instance.
     */
    public static WebDriver initDriver(String browser, ChromeOptions options) {
        logger.info("Initializing driver with options for browser: {}", browser);

        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver(options);
            logger.info("ChromeDriver (with options) initialized successfully.");
        } else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
            logger.info("FirefoxDriver initialized successfully.");
        } else {
            logger.error("Browser not supported: {}", browser);
            throw new IllegalArgumentException("Browser not supported: " + browser);
        }

        driver.manage().window().maximize();
        logger.info("Browser window maximized.");
        return driver;
    }

    /**
     * A "getter" method to provide access to the currently active WebDriver instance.
     * @return The current WebDriver instance.
     */
    public static WebDriver getDriver() {
        return driver;
    }

    /**
     * Closes the browser and terminates the WebDriver session.
     */
    public static void quitDriver() {
        if (driver != null) {
            logger.info("Quitting the driver...");
            driver.quit();
            driver = null;
            logger.info("Driver quit successfully.");
        } else {
            logger.warn("quitDriver() called but driver was already null.");
        }
    }
}

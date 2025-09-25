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

public class DriverFactory {

    // A private static variable to hold the single WebDriver instance (singleton pattern).
    // 'static' means this variable belongs to the class, not to any specific instance.
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
        // Log the start of the driver initialization.
        logger.info("Initializing driver for browser: {}", browser);

        // Check if the requested browser is "chrome" (ignoring case).
        if (browser.equalsIgnoreCase("chrome")) {
            // Use WebDriverManager to automatically download and set up the correct ChromeDriver binary.
            WebDriverManager.chromedriver().setup();
            // Create a new instance of the ChromeDriver.
            driver = new ChromeDriver();
            // Log that the ChromeDriver was initialized successfully.
            logger.info("ChromeDriver initialized successfully.");
        // Check if the requested browser is "firefox" (ignoring case).
        } else if (browser.equalsIgnoreCase("firefox")) {
            // Use WebDriverManager to automatically download and set up the correct GeckoDriver binary for Firefox.
            WebDriverManager.firefoxdriver().setup();
            // Create a new instance of the FirefoxDriver.
            driver = new FirefoxDriver();
            // Log that the FirefoxDriver was initialized successfully.
            logger.info("FirefoxDriver initialized successfully.");
        // If the browser name doesn't match any supported browser.
        } else {
            // Log an error message indicating the browser is not supported.
            logger.error("Browser not supported: {}", browser);
            // Throw an exception to stop the execution, as the test cannot proceed without a valid browser.
            throw new IllegalArgumentException("Browser not supported: " + browser);
        }

        // Maximize the browser window to ensure all elements are visible.
        driver.manage().window().maximize();
        // Log that the window has been maximized.
        logger.info("Browser window maximized.");
        // Return the created WebDriver instance.
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
        // Log the start of the driver initialization with custom options.
        logger.info("Initializing driver with options for browser: {}", browser);

        // Check if the requested browser is "chrome".
        if (browser.equalsIgnoreCase("chrome")) {
            // Set up the ChromeDriver binary.
            WebDriverManager.chromedriver().setup();
            // Create a new ChromeDriver instance, passing the custom options to it.
            driver = new ChromeDriver(options);
            // Log the successful initialization.
            logger.info("ChromeDriver (with options) initialized successfully.");
        // As before, handle Firefox initialization. Note: Firefox options would need a separate overloaded method.
        } else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
            logger.info("FirefoxDriver initialized successfully.");
        // Handle unsupported browsers.
        } else {
            logger.error("Browser not supported: {}", browser);
            throw new IllegalArgumentException("Browser not supported: " + browser);
        }

        // Maximize the browser window.
        driver.manage().window().maximize();
        // Log that the window has been maximized.
        logger.info("Browser window maximized.");
        // Return the created WebDriver instance.
        return driver;
    }

    /**
     * A "getter" method to provide access to the currently active WebDriver instance.
     * This is useful for other parts of the framework (like page objects) to interact with the browser.
     * @return The current WebDriver instance.
     */
    public static WebDriver getDriver() {
        return driver;
    }

    /**
     * Closes the browser and terminates the WebDriver session.
     * This is crucial for cleaning up resources after tests are finished.
     */
    public static void quitDriver() {
        // Check if the driver instance actually exists to avoid a NullPointerException.
        if (driver != null) {
            // Log that the driver is being closed.
            logger.info("Quitting the driver...");
            // The quit() method closes all browser windows and safely ends the session.
            driver.quit();
            // Set the driver instance to null to indicate it's no longer active.
            driver = null;
            // Log that the cleanup was successful.
            logger.info("Driver quit successfully.");
        } else {
            // Log a warning if this method is called when the driver is already null.
            logger.warn("quitDriver() called but driver was already null.");
        }
    }
}
// Specifies the package where this class belongs.
package driver;

// Import necessary classes from different libraries.
import org.apache.logging.log4j.LogManager; // For logging using Log4j2.
import org.apache.logging.log4j.Logger; // The Logger interface for Log4j2.
import org.openqa.selenium.WebDriver; // The main interface for browser automation.
import org.openqa.selenium.chrome.ChromeDriver; // Class to create a new Chrome browser session.
import org.openqa.selenium.chrome.ChromeOptions; // To configure Chrome browser options.
import org.openqa.selenium.firefox.FirefoxDriver; // Class to create a new Firefox browser session.
import org.openqa.selenium.firefox.FirefoxOptions; // To configure Firefox browser options.
import io.github.bonigarcia.wdm.WebDriverManager; // A library that automatically manages WebDriver binaries;

public class DriverFactory {

    // A private static variable to hold the single WebDriver instance (singleton pattern).
    private static WebDriver driver;

    // A static final logger instance for this class, used to log information and errors.
    private static final Logger logger = LogManager.getLogger(DriverFactory.class);

    /**
     * Initializes the WebDriver for a given browser name.
     * @param browser The name of the browser to initialize (e.g., "chrome", "firefox").
     * @return The initialized WebDriver instance.
     */
    public static WebDriver initDriver(String browser) {
        logger.info("Initializing driver for browser: {}", browser);

        try {
            if (browser.equalsIgnoreCase("chrome")) {
                WebDriverManager.chromedriver().setup();

                // Create ChromeOptions to configure browser
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--disable-gpu");
                options.addArguments("--remote-allow-origins=*");

                // Detect if running inside CI (GitHub Actions)
                boolean isCI = System.getenv("GITHUB_ACTIONS") != null;
                if (isCI) {
                    options.addArguments("--headless");
                    options.addArguments("--window-size=1920,1080");
                    logger.info("Running in CI environment → Enabled headless mode for Chrome.");
                } else {
                    logger.info("Running locally → Launching Chrome in normal mode.");
                }

                driver = new ChromeDriver(options);
                logger.info("ChromeDriver initialized successfully.");
            } else if (browser.equalsIgnoreCase("firefox")) {
                WebDriverManager.firefoxdriver().setup();

                // Configure Firefox options
                FirefoxOptions options = new FirefoxOptions();
                boolean isCI = System.getenv("GITHUB_ACTIONS") != null;
                if (isCI) {
                    options.addArguments("--headless");
                    options.addArguments("--width=1920");
                    options.addArguments("--height=1080");
                    logger.info("Running in CI environment → Enabled headless mode for Firefox.");
                }

                driver = new FirefoxDriver(options);
                logger.info("FirefoxDriver initialized successfully.");
            } else {
                logger.error("Browser not supported: {}", browser);
                throw new IllegalArgumentException("Browser not supported: " + browser);
            }
        } catch (Exception e) {
            logger.error("Failed to initialize {} driver. Error: {}", browser, e.getMessage());
            logger.warn("Falling back to Firefox...");

            // Fallback to Firefox if Chrome fails
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            boolean isCI = System.getenv("GITHUB_ACTIONS") != null;
            if (isCI) {
                options.addArguments("--headless");
                options.addArguments("--width=1920");
                options.addArguments("--height=1080");
                logger.info("Running in CI environment → Enabled headless mode for Firefox (fallback).");
            }
            driver = new FirefoxDriver(options);
            logger.info("Fallback FirefoxDriver initialized successfully.");
        }

        // Maximize the browser window (for local runs; headless respects size args).
        try {
            driver.manage().window().maximize();
            logger.info("Browser window maximized.");
        } catch (Exception e) {
            logger.warn("Unable to maximize window (headless mode likely). Continuing...");
        }

        return driver;
    }

    /**
     * An overloaded version of initDriver that accepts ChromeOptions.
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

        try {
            driver.manage().window().maximize();
            logger.info("Browser window maximized.");
        } catch (Exception e) {
            logger.warn("Unable to maximize window (headless mode likely). Continuing...");
        }

        return driver;
    }

    /**
     * Getter method for the active WebDriver instance.
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

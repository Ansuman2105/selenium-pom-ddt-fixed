package driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {
    private static WebDriver driver;
    private static final Logger logger = LogManager.getLogger(DriverFactory.class);

    // Existing method
    public static WebDriver initDriver(String browser) {
        logger.info("Initializing driver for browser: {}", browser);

        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
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

    // Overloaded method for ChromeOptions
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

    // Return current driver instance
    public static WebDriver getDriver() {
        return driver;
    }

    // Quit driver after tests
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

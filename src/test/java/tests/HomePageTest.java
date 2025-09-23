package tests;

import java.time.Duration;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import base.BaseTest;
import pages.HomePage;
import pages.LoginPage;

public class HomePageTest extends BaseTest {

    @Test(priority = 1)
    public void loginTest() {
        driver.get(config.getProperty("baseUrl"));

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(config.getProperty("validUsername"), config.getProperty("validPassword"));

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isMyAccountVisible(), "Login failed!");
    }

    @Test(priority = 2, dependsOnMethods = "loginTest")
    public void verifyHomePageTitle() {
    	LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.titleIs("My Account"));

        HomePage homePage = new HomePage(driver);
        String actualTitle = homePage.getPageTitle();
        String expectedTitle = "My Account";

        Assert.assertEquals(actualTitle, expectedTitle, "Expected title: " + expectedTitle + ", but got: " + actualTitle);
    }

    @Test(priority = 3, dependsOnMethods = "loginTest")
    public void verifyLogoutButtonVisible() {
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isLogoutVisible(), "Logout button should be visible!");
    }
}

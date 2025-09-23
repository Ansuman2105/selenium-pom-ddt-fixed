package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.HomePage;
import utils.ExcelUtil;

public class LoginTest extends BaseTest {

    @DataProvider(name = "loginData")
    public Object[][] getData() {
        return ExcelUtil.getSheetData("login");
    }

    @Test(dataProvider = "loginData", groups = "login")
    public void validLoginTest(String username, String password) {
        driver.get(config.getProperty("baseUrl"));

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isMyAccountVisible(), "Login failed!");
    }
    

}

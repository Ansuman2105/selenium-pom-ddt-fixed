package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitHelper;

public class HomePage {
    private WebDriver driver;
    private WaitHelper wait;

    private By accountText = By.xpath("//h2[text()='My Account']");
    private By logoutBtn   = By.xpath("//a[text()='Logout']");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitHelper(driver, 10);
    }

    public boolean isMyAccountVisible() {
        return wait.waitForElementVisible(accountText).isDisplayed();
    }

    public boolean isLogoutVisible() {
        return wait.waitForElementVisible(logoutBtn).isDisplayed();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}

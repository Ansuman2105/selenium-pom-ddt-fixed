package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitHelper;

public class LoginPage {
    @SuppressWarnings("unused")
	private WebDriver driver;
    private WaitHelper wait;

    private By emailField = By.name("email");  
    private By passwordField = By.name("password");  
    private By loginBtn = By.xpath("//input[@value='Login']");  
    private By errorMsg = By.xpath("//*[@id=\"account-login\"]/div[1]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitHelper(driver, 10); // 10 seconds wait
    }

    public void enterEmail(String email) {
        wait.waitForElementVisible(emailField).sendKeys(email);
    }

    public void enterPassword(String password) {
        wait.waitForElementVisible(passwordField).sendKeys(password);
    }

    public void clickLogin() {
        wait.waitForElementClickable(loginBtn).click();
    }

    public String getErrorMessage() {
        try {
            return wait.waitForElementVisible(errorMsg).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLogin();
    }
}

package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitHelper;

import java.util.List;

public class SearchPage {
    private WebDriver driver;
    private WaitHelper waitHelper;

    private By searchBox = By.name("search");
    private By searchButton = By.cssSelector("button.btn.btn-default");
    private By productTitles = By.cssSelector(".product-thumb h4 a"); // Adjust to actual product link selector

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        this.waitHelper = new WaitHelper(driver, 10);  // use 10 sec timeout
    }

    public void searchForProduct(String productName) {
        waitHelper.waitForElementClickable(searchBox).clear();
        driver.findElement(searchBox).sendKeys(productName);
        driver.findElement(searchButton).click();
    }

    public boolean isProductDisplayed(String productName) {
        try {
            waitHelper.waitForElementVisible(productTitles);
            List<WebElement> products = driver.findElements(productTitles);
            for (WebElement product : products) {
                if (product.getText().trim().equalsIgnoreCase(productName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            // Optional: add logging here
        }
        return false;
    }
}

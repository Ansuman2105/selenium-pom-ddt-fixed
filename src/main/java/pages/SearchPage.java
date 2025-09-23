package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SearchPage {
    private WebDriver driver;

    private By searchBox = By.name("search");
    private By searchButton = By.cssSelector("button.btn.btn-default");

    public SearchPage(WebDriver driver) {
        this.driver = driver;
    }

    public void searchForProduct(String productName) {
        driver.findElement(searchBox).clear();
        driver.findElement(searchBox).sendKeys(productName);
        driver.findElement(searchButton).click();
    }

    public boolean isProductDisplayed(String productName) {
        return driver.getPageSource().contains(productName);
    }
}

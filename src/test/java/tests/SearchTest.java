package tests;

import io.qameta.allure.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.SearchPage;
import utils.ExcelUtil;
import base.BaseTest;

@Epic("E-Commerce")
@Feature("Product Search")
public class SearchTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(SearchTest.class);

    @DataProvider(name = "productData")
    public Object[][] getProductData() {
        return ExcelUtil.getSheetData("Products");
    }

    @Story("User searches for products")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that products from Excel data are displayed correctly in search results")
    @Test(dataProvider = "productData", description = "Validate product search functionality for multiple inputs")
    public void testSearchProduct(String productName, String expectedResult) {
        logger.info("Navigating to base URL: {}", config.getProperty("baseUrl"));
        driver.get(config.getProperty("baseUrl"));

        SearchPage searchPage = new SearchPage(driver);
        logger.info("Searching for product: {}", productName);

        searchPage.searchForProduct(productName);
        boolean isDisplayed = searchPage.isProductDisplayed(productName);

        Allure.step("Product searched: " + productName);
        Allure.addAttachment("Search Keyword", productName);
        Allure.addAttachment("Expected Presence", expectedResult);

        if (isDisplayed) {
            logger.info("✅ PASS: {} is displayed in search results", productName);
        } else {
            logger.error("❌ FAIL: {} not displayed in search results", productName);
        }

        Assert.assertEquals(isDisplayed, Boolean.parseBoolean(expectedResult),
                "❌ FAIL: " + productName + " should be displayed in search results");
    }
}

package tests;


import io.qameta.allure.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.SearchPage;
import utils.ExcelUtil;
import base.BaseTest;

@Epic("E-Commerce")
@Feature("Product Search")
// @Listeners({io.qameta.allure.testng.AllureTestNg.class})    // ✅ Ensures Allure captures results [removed it as it's added globally in testng.xml] 
public class SearchTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(SearchTest.class);

    @DataProvider(name = "productData")
    public Object[][] getProductData() {
        return ExcelUtil.getSheetData("Products");
    }

    @Story("User searches for products")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that products from Excel data are displayed correctly in search results")
    @Test(dataProvider = "productData")
    public void testSearchProduct(String productName, String expectedResult) {
        logger.info("Navigating to base URL: {}", config.getProperty("baseUrl"));
        driver.get(config.getProperty("baseUrl"));

        SearchPage searchPage = new SearchPage(driver);
        logger.info("Searching for product: {}", productName);

        searchPage.searchForProduct(productName);
        boolean isDisplayed = searchPage.isProductDisplayed(productName);

        if (isDisplayed) {
            logger.info("✅ PASS: {} is displayed in search results", productName);
            Allure.step("Product found: " + productName);   // ✅ adds step in report
            Allure.addAttachment("Search Result", productName + " found in results ✅");
        } else {
            logger.error("❌ FAIL: {} not displayed in search results", productName);
            Allure.step("Product not found: " + productName);   // ✅ adds step in report
            Allure.addAttachment("Search Result", productName + " not found ❌");
        }

        Assert.assertEquals(isDisplayed, Boolean.parseBoolean(expectedResult),
                "❌ FAIL: " + productName + " should be displayed in search results");
    }
}

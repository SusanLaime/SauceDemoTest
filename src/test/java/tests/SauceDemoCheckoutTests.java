package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.CartPage;
import pages.CheckoutStepOnePage;
import pages.CheckoutStepTwoPage;
import pages.InventoryPage;
import pages.LoginPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SauceDemoCheckoutTests {

    private WebDriver driver;
    private InventoryPage inventoryPage;

    private static final String USERNAME = "standard_user";
    private static final String PASSWORD = "secret_sauce";

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        inventoryPage = new LoginPage(driver)
                .open()
                .loginAs(USERNAME, PASSWORD);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void test01_VerifyProductDetails() {
        String expectedName = "Sauce Labs Backpack";
        String expectedPrice = "$29.99";

        assertEquals(expectedName, inventoryPage.getProductName(expectedName),
                "Product name should match");
        assertEquals(expectedPrice, inventoryPage.getProductPriceText(expectedName),
                "Product price should match");
    }

    @Test
    public void test02_VerifyCartItemDetails() {
        String productName = "Sauce Labs Backpack";

        String nameOnInventoryPage = inventoryPage.getProductName(productName);
        String priceOnInventoryPage = inventoryPage.getProductPriceText(productName);

        CartPage cartPage = inventoryPage
                .addProductToCart(productName)
                .goToCart();

        assertEquals(nameOnInventoryPage, cartPage.getCartItemName(),
                "Product name in cart should match inventory page");
        assertEquals(priceOnInventoryPage, cartPage.getCartItemPriceText(),
                "Product price in cart should match inventory page");
    }

    @Test
    public void test03_VerifyCheckoutItemTotal() {
        String[] productsToBuy = {"Sauce Labs Backpack", "Sauce Labs Bike Light"};

        double expectedSum = 0.0;
        for (String product : productsToBuy) {
            expectedSum += inventoryPage.getProductPrice(product);
            inventoryPage.addProductToCart(product);
        }

        CheckoutStepTwoPage overviewPage = inventoryPage.goToCart()
                .checkout()
                .fillInfo("John", "Doe", "12345")
                .continueToOverview();

        assertEquals(expectedSum, overviewPage.getItemTotal(), 0.001,
                "Item Total should equal the sum of product prices");
    }

    @Test
    public void test04_VerifyCheckoutTotalWithTax() {
        inventoryPage.addProductToCart("Sauce Labs Backpack");
        inventoryPage.addProductToCart("Sauce Labs Bike Light");

        CheckoutStepTwoPage overviewPage = inventoryPage.goToCart()
                .checkout()
                .fillInfo("John", "Doe", "12345")
                .continueToOverview();

        double expectedTotal = Math.round((overviewPage.getItemTotal() + overviewPage.getTax()) * 100.0) / 100.0;

        assertEquals(expectedTotal, overviewPage.getTotal(), 0.01,
                "Total should equal Item Total plus Tax");
    }

    @Test
    public void test05_VerifyCheckoutRequiredFieldValidation() {
        inventoryPage.addProductToCart("Sauce Labs Backpack");

        // Intentionally leave the Postal Code field empty (required field)
        CheckoutStepOnePage checkoutStepOnePage = inventoryPage.goToCart()
                .checkout()
                .fillInfo("John", "Doe", null)
                .continueExpectingError();

        assertTrue(checkoutStepOnePage.getErrorMessage().toLowerCase().contains("postal code"),
                "An error message about the missing Postal Code should be displayed");
        assertTrue(checkoutStepOnePage.isOnCheckoutStepOne(),
                "User should remain on the checkout information page when a required field is empty");
    }
}

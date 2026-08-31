package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class InventoryPage extends BasePage {

    private final By inventoryItems = By.className("inventory_item");
    private final By itemName = By.className("inventory_item_name");
    private final By itemPrice = By.className("inventory_item_price");
    private final By cartLink = By.className("shopping_cart_link");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    private WebElement findProduct(String productName) {
        return driver.findElements(inventoryItems).stream()
                .filter(item -> item.findElement(itemName).getText().equals(productName))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Product not found: " + productName));
    }

    public String getProductName(String productName) {
        return findProduct(productName).findElement(itemName).getText();
    }

    public String getProductPriceText(String productName) {
        return findProduct(productName).findElement(itemPrice).getText();
    }

    public double getProductPrice(String productName) {
        return parsePrice(getProductPriceText(productName));
    }

    public InventoryPage addProductToCart(String productName) {
        String testId = productName.toLowerCase().replace(" ", "-");
        driver.findElement(By.id("add-to-cart-" + testId)).click();
        return this;
    }

    public List<WebElement> getAllItems() {
        return driver.findElements(inventoryItems);
    }

    public CartPage goToCart() {
        driver.findElement(cartLink).click();
        wait.until(ExpectedConditions.urlContains("cart.html"));
        return new CartPage(driver);
    }
}

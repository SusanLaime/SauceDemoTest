package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CartPage extends BasePage {

    private final By cartItem = By.className("cart_item");
    private final By itemName = By.className("inventory_item_name");
    private final By itemPrice = By.className("inventory_item_price");
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public String getCartItemName() {
        WebElement item = driver.findElement(cartItem);
        return item.findElement(itemName).getText();
    }

    public String getCartItemPriceText() {
        WebElement item = driver.findElement(cartItem);
        return item.findElement(itemPrice).getText();
    }

    public double getCartItemPrice() {
        return parsePrice(getCartItemPriceText());
    }

    public CheckoutStepOnePage checkout() {
        driver.findElement(checkoutButton).click();
        wait.until(ExpectedConditions.urlContains("checkout-step-one.html"));
        return new CheckoutStepOnePage(driver);
    }
}

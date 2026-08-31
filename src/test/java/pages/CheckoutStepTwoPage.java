package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutStepTwoPage extends BasePage {

    private final By subtotalLabel = By.className("summary_subtotal_label");
    private final By taxLabel = By.className("summary_tax_label");
    private final By totalLabel = By.className("summary_total_label");

    public CheckoutStepTwoPage(WebDriver driver) {
        super(driver);
    }

    public double getItemTotal() {
        String text = driver.findElement(subtotalLabel).getText().replace("Item total:", "").trim();
        return parsePrice(text);
    }

    public double getTax() {
        String text = driver.findElement(taxLabel).getText().replace("Tax:", "").trim();
        return parsePrice(text);
    }

    public double getTotal() {
        String text = driver.findElement(totalLabel).getText().replace("Total:", "").trim();
        return parsePrice(text);
    }
}

package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Base class for all Page Objects.
 * Holds the shared WebDriver/WebDriverWait so every page doesn't repeat this setup.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected double parsePrice(String priceText) {
        // e.g. "$29.99" -> 29.99
        return Double.parseDouble(priceText.replace("$", "").trim());
    }
}

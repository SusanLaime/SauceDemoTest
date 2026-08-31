package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutStepOnePage extends BasePage {

    private final By firstNameField = By.id("first-name");
    private final By lastNameField = By.id("last-name");
    private final By postalCodeField = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public CheckoutStepOnePage(WebDriver driver) {
        super(driver);
    }

    public CheckoutStepOnePage fillInfo(String firstName, String lastName, String postalCode) {
        if (firstName != null) {
            driver.findElement(firstNameField).sendKeys(firstName);
        }
        if (lastName != null) {
            driver.findElement(lastNameField).sendKeys(lastName);
        }
        if (postalCode != null) {
            driver.findElement(postalCodeField).sendKeys(postalCode);
        }
        return this;
    }

    /** Use when the info is expected to be valid and checkout should advance to step two. */
    public CheckoutStepTwoPage continueToOverview() {
        driver.findElement(continueButton).click();
        wait.until(ExpectedConditions.urlContains("checkout-step-two.html"));
        return new CheckoutStepTwoPage(driver);
    }

    /** Use when a validation error is expected (e.g. a required field was left empty). */
    public CheckoutStepOnePage continueExpectingError() {
        driver.findElement(continueButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
        return this;
    }

    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }

    public boolean isOnCheckoutStepOne() {
        return driver.getCurrentUrl().contains("checkout-step-one.html");
    }
}

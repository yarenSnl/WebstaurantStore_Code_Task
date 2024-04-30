package Pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class ActionHelper {
    private WebDriver driver;
    private WebDriverWait wait;

    /**
     * Initializes the WebDriver and WebDriverWait for use in all methods.
     */
    public ActionHelper(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    /**
     * Clicks on the specified web element.
     */
    public void click(WebElement element) {
        prepareElement(element);
        element.click();
    }

    /**
     * Clears the content of the web element and types into it.
     */
    public void sendKeys(WebElement element, String text) {
        prepareElement(element);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Scrolls the web page until the specified element is in view.
     */
    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Waits until the specified element is visible on the page.
     */
    public void waitUntilVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Waits for the element to be visible on the page and scrolls to it.
     */
    private void prepareElement(WebElement element) {
        waitUntilVisible(element);
        scrollToElement(element);
    }
}


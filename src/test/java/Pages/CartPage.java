package Pages;


import com.aventstack.extentreports.ExtentTest;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class CartPage {
    private WebDriver driver;
    private ActionHelper actionHelper;
    private SearchPage searchPage;
    private ExtentTest extentTest;

    /**
     * Constructor for CartPage.
     * Initializes WebDriver, ActionHelper, SearchPage, and ExtentTest instances, and sets up page elements.
     */
    public CartPage(WebDriver driver, ActionHelper actionHelper, SearchPage searchPage, ExtentTest test) {
        this.driver = driver;
        this.actionHelper = actionHelper;
        this.searchPage = searchPage;
        this.extentTest = test;
        PageFactory.initElements(driver, this);

    }

    /**
     * Locators
     */
    @FindBy(xpath = "//p[@class='pl-1 itemNumber sub-text']")
    public WebElement itemInTheCart;
    @FindBy(xpath = "//button[.='Empty Cart']")
    public WebElement emptyCartButton;
    @FindBy(xpath = "//footer/button[.='Empty Cart']")
    public WebElement confirmationEmptyCartButton;
    @FindBy(xpath = "//p[.='Your cart is empty.']")
    public WebElement yourCartIsEmptyMessage;

    /**
     * Clicks on the 'View Cart' button using JavaScript Executor to ensure the click is executed.
     */
    public void clickOnViewTheCartButton() {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        WebElement viewCartButtonElement = searchPage.viewCartButton;
        executor.executeScript("arguments[0].click();", viewCartButtonElement);
        extentTest.info("Click on 'View Cart' button.");
    }

    /**
     * Verifies that the product code in the cart matches the expected product code stored in SearchPage.
     * Asserts that the product is correct.
     */
    public void verifyProductInCartMatchesSelectedProduct() {
        try {
            String codeInCart = itemInTheCart.getText().trim();
            actionHelper.waitUntilVisible(itemInTheCart);
            boolean isCorrectProduct = codeInCart.contains(SearchPage.expectedProductCode);
            Assert.assertTrue(isCorrectProduct, "The product in the cart does not match the expected product code.");
            if (isCorrectProduct) {
                extentTest.pass("Product in cart matches the expected product code: " + codeInCart);
            }
        } catch (AssertionError ae) {
            extentTest.fail("Product verification failed: " + ae.getMessage());
            throw ae;
        } catch (Exception e) {
            extentTest.fail("Error during cart verification: " + e.getMessage());
            throw new RuntimeException("Error verifying cart: " + e.getMessage(), e);
        }
    }

    /**
     * Empties the cart by clicking the 'Empty Cart' button and confirming the action.
     */
    public void emptyCart() {
        try {
            actionHelper.click(emptyCartButton);
            extentTest.info("Click on 'Empty Cart' button.");

            actionHelper.click(confirmationEmptyCartButton);
            extentTest.info("Confirm to empty the cart.");
        } catch (Exception e) {
            extentTest.fail("Failed to empty cart: " + e.getMessage());
            throw new RuntimeException("Error emptying the cart: " + e.getMessage(), e);
        }

    }

    /**
     * Verifies that the cart is empty by checking the presence of the expected empty cart message.
     */
    public void verifyTheCartIsEmpty(String expectedEmptyMessage) {
        try {
            boolean isCartEmpty = yourCartIsEmptyMessage.isDisplayed();
            Assert.assertTrue(isCartEmpty, "Expected empty cart message was not displayed. Expected: '" + expectedEmptyMessage + "'");
            if (isCartEmpty) {
                extentTest.pass("Verified cart is empty with expected message: " + expectedEmptyMessage);
            } else {
                extentTest.fail("Cart is not empty as expected. Expected empty cart message not displayed.");
            }
        } catch (Exception e) {
            extentTest.fail("Failed to verify empty cart: " + e.getMessage());
            throw new RuntimeException("Error verifying empty cart: " + e.getMessage(), e);
        }

    }
}

package Pages;

import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class SearchPage {
    private WebDriver driver;
    private ActionHelper actionHelper;
    private ExtentTest extentTest;

    /**
     * Initializes the WebDriver, ActionHelper, and ExtentTest instances, and sets up page elements.
     */
    public SearchPage(WebDriver driver, ActionHelper actionHelper, ExtentTest test) {
        this.driver = driver;
        this.actionHelper = actionHelper;
        this.extentTest = test;
        PageFactory.initElements(driver, this);
    }

    /**
     * Locators
     */
    @FindBy(xpath = "//input[@id='searchval']")
    public WebElement searchBox;
    @FindBy(xpath = "//button[@value='Search']")
    public WebElement searchButton;
    @FindBy(xpath = "//span[@data-testid='itemDescription']")
    public List<WebElement> itemsList;
    @FindBy(xpath = "//li[@class='inline-block leading-4 align-top rounded-r-md']/a[contains(@aria-label,'go to page')]")
    public WebElement nextPageButton;
    @FindBy(xpath = "//a[contains(@aria-label,'last page, page')]")
    public WebElement lastPagesNumber;
    @FindBy(xpath = "(//input[@name='addToCartButton'])[last()]")
    public WebElement lastItemAddToCartButton;
    @FindBy(xpath = "(//span[@class='whitespace-nowrap bg-white py-0 px-1 absolute text-gray-800'])[last()]")
    public WebElement lastItemsCode;
    @FindBy(xpath = "//a[.='View Cart']")
    public WebElement viewCartButton;


    /**
     * Performs a search with the specified query.
     */
    public void performSearch(String searchQuery) {
        actionHelper.sendKeys(searchBox, searchQuery);
        actionHelper.click(searchButton);
        extentTest.info("Perform search for: '" + searchQuery + "'.");
    }

    /**
     * Verifies that each item's title on all pages contain the specified keyword.
     * Logs the result and counts items that do not contain the keyword.
     */
    public void verifyEachItemsTitleContains(String keyword) {
        int lastPage = Integer.parseInt(lastPagesNumber.getText());
        int totalItems = 0;
        int countNotContainingKeyword = 0;
        extentTest.info("Starting to verify items across " + lastPage + " pages.");
        for (int i = 1; i <= lastPage; i++) {
            List<WebElement> currentItemsList = itemsList;
            totalItems += currentItemsList.size();
            for (WebElement eachItem : currentItemsList) {
                String itemTitle = eachItem.getText();
                if (!itemTitle.contains(keyword)) {
                    countNotContainingKeyword++;
                    extentTest.fail("Item: " + itemTitle + " does not contain \"" + keyword + "\" in its title.");
                }
            }
            if (i < lastPage) {
                actionHelper.click(nextPageButton);
                actionHelper.waitUntilVisible(driver.findElement(By.xpath("//*[@aria-label='current page, page " + (i + 1) + "']")));
            }
        }
        extentTest.info("Verification completed across " + lastPage + " pages: " + totalItems + " items reviewed, with " + countNotContainingKeyword + " items do not contain the keyword '" + keyword + "'.");
    }

    /**
     * This static variable is used to store the product code of the last item added to the cart.
     */
    public static String expectedProductCode;

    /**
     * Adds the last product on the page to the cart and retrieves its product code.
     */
    public String addLastProductToCart() {
        try {
            actionHelper.click(lastItemAddToCartButton);
            String fullProductCode = lastItemsCode.getText().trim();
            expectedProductCode = fullProductCode.substring(fullProductCode.indexOf('#'));
            extentTest.info("Product added to cart with code: " + expectedProductCode);
            return expectedProductCode;
        } catch (Exception e) {
            extentTest.fail("Failed to add product to cart and retrieve code: " + e.getMessage());
            throw new RuntimeException("Failed operation: " + e.getMessage(), e);
        }
    }

}

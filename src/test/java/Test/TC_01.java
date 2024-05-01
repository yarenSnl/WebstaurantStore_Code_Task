package Test;

import Utilities.TestSetup;
import org.testng.annotations.Test;


public class TC_01 extends TestSetup {
    @Test(priority = 1)
    public void webstaurantStoreTest() {

        /** Step 1: Search for a query and verify results contain the keyword "Table" */
        searchPage.performSearch("stainless work table");
        searchPage.verifyEachItemsTitleContains("Table");

        /** Step 2: Add to cart and verify the product code matches the expected product code. */
        searchPage.addLastProductToCart();
        cartPage.clickOnViewTheCartButton();
        cartPage.verifyProductInCartMatchesSelectedProduct();

        /** Step 3: Empty the cart and verify it is empty. */
        cartPage.emptyCart();
        cartPage.verifyTheCartIsEmpty("Your cart is empty.");
    }
}


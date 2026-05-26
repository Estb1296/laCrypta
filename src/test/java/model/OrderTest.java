package model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void AddItemToCart_ValidSandwich_ReturnsOneItem() {
        //Arrange
        Order order = new Order();
        Sandwich sandwich = new Sandwich();
        //Act
        order.addItem(sandwich);
        //Assert
        assertEquals(1, order.getItems().size());
    }

    @Test
    void getItems_ValidOrder_ReturnsArrayList() {
        // arrange
        Order order = new Order();
        Sandwich sandwich = new Sandwich();
        order.addItem(sandwich);

        // act
        ArrayList<MenuItem> items = order.getItems();

        // assert
        assertEquals(1, items.size());
        assertEquals(sandwich, items.get(0));
    }

    @Test
    void getPrice_ParameterizedConstructor_ReturnsSizePrice() {
        // arrange
        Order order = new Order();
        Sandwich sandwich = new Sandwich("Turkey", Sandwich.SandwichSize.TWELVE, "White", true);
        order.addItem(sandwich);

        // act
        double price = order.getPrice();

        // assert
        assertTrue(price > 0);
        assertEquals(Sandwich.SandwichSize.TWELVE.getPrice(), price, 0.01);
    }

    @Test
    void getPrice_NoParameterConstructor_ReturnsZero() {
        // arrange
        Order order = new Order();
        Sandwich sandwich = new Sandwich();
        order.addItem(sandwich);

        // act
        double price = order.getPrice();

        // assert
        assertEquals(0.0, price, 0.01);
    }

    @Test
    void removeItem_ValidIndex_ItemRemoved() {
        // arrange
        Order order = new Order();
        order.addItem(new Sandwich());
        order.addItem(new Drink("Coca-Cola"));

        // act
        order.removeItem(0);

        // assert
        assertEquals(1, order.getItems().size());
    }

    @Test
    void removeItem_InvalidIndex_NoExceptionThrown() {
        // arrange
        Order order = new Order();
        order.addItem(new Sandwich());

        // act & assert
        assertDoesNotThrow(() -> order.removeItem(10));
    }

    @Test
    void clearCart_NonEmptyCart_CartBecomesEmpty() {
        // arrange
        Order order = new Order();
        order.addItem(new Sandwich());
        order.addItem(new Drink("Fanta"));

        // act
        order.clearCart();

        // assert
        assertEquals(0, order.getItems().size());
    }


    @Test
    void isValidPromoCode_ValidCode_ReturnsTrue() {
        // arrange
        Order order = new Order();

        // act
        boolean isValid = order.isValidPromoCode("Craig26@");

        // assert
        assertTrue(isValid);
    }

    @Test
    void isValidPromoCode_InvalidCode_ReturnsFalse() {
        // arrange
        Order order = new Order();

        // act
        boolean isValid = order.isValidPromoCode("INVALID");

        // assert
        assertFalse(isValid);
    }

    @Test
    void setDiscountAmount_ValidDiscount_Coupon_DiscountApplied() {
        // arrange
        Order order = new Order();
        double discountAmount = 5.00;

        // act
        order.setCouponDiscountAmount(discountAmount);

        // assert
        assertEquals(discountAmount, order.getCouponDiscount(), 0.01);

    }

    @Test
    void getCouponDiscount_AppliedDiscount_ReturnsDiscount() {
        // arrange
        Order order = new Order();
        double expectedDiscount = 10.00;

        // act
        order.setCouponDiscountAmount(expectedDiscount);
        double discount = order.getCouponDiscount();

        // assert
        assertEquals(expectedDiscount, discount, 0.01);
    }


    @Test
    void calculatePrice_EmptyCart_ReturnsZero() {
        // arrange
        Order order = new Order();

        // act
        double price = order.calculatePrice();

        // assert
        assertEquals(0.0, price, 0.01);
    }

    @Test
    void calculatePrice_SingleItem_ReturnsCorrectPrice() {
        // arrange
        Order order = new Order();
        Sandwich sandwich = new Sandwich("Turkey", Sandwich.SandwichSize.TWELVE, "White", true);
        order.addItem(sandwich);

        // act
        double price = order.calculatePrice();

        // assert
        assertTrue(price > 0);
    }

    @Test
    void calculatePrice_WithDiscount_ReturnsReducedPrice() {
        // arrange
        Order order = new Order();
        Sandwich sandwich = new Sandwich("Turkey", Sandwich.SandwichSize.TWELVE, "White", true);
        order.addItem(sandwich);
        double originalPrice = order.calculatePrice();
        order.setCouponDiscountAmount(5.00);
        // act
        double discountedPrice = order.calculatePrice();

        // assert
        assertTrue(discountedPrice < originalPrice);
    }

    @Test
    void toReceiptLine_ToastedSandwich_IncludesToastedLabel() {
        // arrange
        Sandwich sandwich = new Sandwich("Turkey", Sandwich.SandwichSize.TWELVE, "White", true);

        // act
        String receiptLine = sandwich.toReceiptLine();

        // assert
        assertTrue(receiptLine.contains(" - TOASTED"));
    }
    @Test
    void toReceiptLine_NotToastedSandwich_OmitsToastedLabel() {
        // arrange
        Sandwich sandwich = new Sandwich("Turkey", Sandwich.SandwichSize.TWELVE, "White", false); // false here

        // act
        String receiptLine = sandwich.toReceiptLine();

        // assert
        assertFalse(receiptLine.contains(" - TOASTED"));
    }

    @Test
    void toReceiptLine_Drink_ReturnsFormattedString() {
        // arrange
        Drink drink = new Drink("Coke");

        // act
        String receiptLine = drink.toReceiptLine();

        // assert
        assertNotNull(receiptLine);
        assertTrue(receiptLine.contains("Coke"));
    }

    @Test
    void getCategory_Sandwich_ReturnsSandwich() {
        // arrange
        Sandwich sandwich = new Sandwich();

        // act
        String category = sandwich.getCategory();

        // assert
        assertEquals("Sandwich", category);
    }

    @Test
    void getCategory_Drink_ReturnsDrink() {
        // arrange
        Drink drink = new Drink("Coke");

        // act
        String category = drink.getCategory();

        // assert
        assertEquals("Drink", category);
    }

    @Test
    void getCategory_Chips_ReturnsChips() {
        // arrange
        Chips chips = new Chips("Doritos Cool Ranch", 1.49, "Crispy and delicious");

        // act
        String category = chips.getCategory();

        // assert
        assertEquals("Chips", category);
    }

}
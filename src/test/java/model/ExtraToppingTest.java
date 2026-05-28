package model;

import com.pluralsight.model.ExtraTopping;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExtraToppingTest {

    // ==================== CONSTRUCTOR TESTS ====================

    @Test
    void constructor_MeatTopping_InitializesCorrectly() {
        // arrange & act
        ExtraTopping extraTopping = new ExtraTopping("Extra Steak", false, ExtraTopping.ExtraToppingSize.TWELVE);

        // assert
        assertEquals("Extra Steak", extraTopping.getName());
        assertEquals(ExtraTopping.ExtraToppingSize.TWELVE, extraTopping.getSize());
        assertEquals("Extra Topping", extraTopping.getCategory());
    }

    @Test
    void constructor_CheeseTopping_InitializesCorrectly() {
        // arrange & act
        ExtraTopping extraTopping = new ExtraTopping("Extra Cheddar", true, ExtraTopping.ExtraToppingSize.EIGHT);

        // assert
        assertEquals("Extra Cheddar", extraTopping.getName());
        assertEquals(ExtraTopping.ExtraToppingSize.EIGHT, extraTopping.getSize());
    }

    // ==================== SIZE TESTS ====================

    @Test
    void getSize_FourInch_ReturnsFour() {
        // arrange & act
        ExtraTopping extraTopping = new ExtraTopping("Extra Meat", false, ExtraTopping.ExtraToppingSize.FOUR);

        // assert
        assertEquals(ExtraTopping.ExtraToppingSize.FOUR, extraTopping.getSize());
    }

    @Test
    void getSize_EightInch_ReturnsEight() {
        // arrange & act
        ExtraTopping extraTopping = new ExtraTopping("Extra Meat", false, ExtraTopping.ExtraToppingSize.EIGHT);

        // assert
        assertEquals(ExtraTopping.ExtraToppingSize.EIGHT, extraTopping.getSize());
    }

    @Test
    void getSize_TwelveInch_ReturnsTwelve() {
        // arrange & act
        ExtraTopping extraTopping = new ExtraTopping("Extra Meat", false, ExtraTopping.ExtraToppingSize.TWELVE);

        // assert
        assertEquals(ExtraTopping.ExtraToppingSize.TWELVE, extraTopping.getSize());
    }

    // ==================== PRICE TESTS (MEAT) ====================

    @Test
    void calculatePrice_FourInchMeat_ReturnsFullFlatRate() {
        // arrange
        ExtraTopping extraTopping = new ExtraTopping("Extra Ham", false, ExtraTopping.ExtraToppingSize.FOUR);

        // act
        double price = extraTopping.calculatePrice();

        // assert
        assertEquals(0.50, price, 0.01,
                "A 4\" extra meat topping should cost the full flat rate of $0.50");
    }

    @Test
    void calculatePrice_EightInchMeat_ReturnsFullFlatRate() {
        // arrange
        ExtraTopping extraTopping = new ExtraTopping("Extra Turkey", false, ExtraTopping.ExtraToppingSize.EIGHT);

        // act
        double price = extraTopping.calculatePrice();

        // assert
        assertEquals(1.00, price, 0.01,
                "An 8\" extra meat topping should cost the full flat rate of $1.00");
    }

    @Test
    void calculatePrice_TwelveInchMeat_ReturnsFullFlatRate() {
        // arrange
        ExtraTopping extraTopping = new ExtraTopping("Extra Steak", false, ExtraTopping.ExtraToppingSize.TWELVE);

        // act
        double price = extraTopping.calculatePrice();

        // assert
        assertEquals(1.50, price, 0.01,
                "A 12\" extra meat topping should cost the full flat rate of $1.50");
    }

    // ==================== PRICE TESTS (CHEESE) ====================

    @Test
    void calculatePrice_FourInchCheese_ReturnsHalfFlatRate() {
        // arrange
        ExtraTopping extraTopping = new ExtraTopping("Extra Cheddar", true, ExtraTopping.ExtraToppingSize.FOUR);

        // act
        double price = extraTopping.calculatePrice();

        // assert
        assertEquals(0.25, price, 0.01,
                "A 4\" extra cheese topping should cost 50% of the flat rate: $0.25");
    }

    @Test
    void calculatePrice_EightInchCheese_ReturnsHalfFlatRate() {
        // arrange
        ExtraTopping extraTopping = new ExtraTopping("Extra Swiss", true, ExtraTopping.ExtraToppingSize.EIGHT);

        // act
        double price = extraTopping.calculatePrice();

        // assert
        assertEquals(0.50, price, 0.01,
                "An 8\" extra cheese topping should cost 50% of the flat rate: $0.50");
    }

    @Test
    void calculatePrice_TwelveInchCheese_ReturnsHalfFlatRate() {
        // arrange
        ExtraTopping extraTopping = new ExtraTopping("Extra Provolone", true, ExtraTopping.ExtraToppingSize.TWELVE);

        // act
        double price = extraTopping.calculatePrice();

        // assert
        assertEquals(0.75, price, 0.01,
                "A 12\" extra cheese topping should cost 50% of the flat rate: $0.75");
    }

    @Test
    void calculatePrice_CheeseIsAlwaysCheaperThanMeatAtSameSize_PriceComparison() {
        // arrange
        ExtraTopping meat = new ExtraTopping("Extra Chicken", false, ExtraTopping.ExtraToppingSize.TWELVE);
        ExtraTopping cheese = new ExtraTopping("Extra Mozzarella", true, ExtraTopping.ExtraToppingSize.TWELVE);

        // act & assert
        assertTrue(cheese.calculatePrice() < meat.calculatePrice(),
                "Extra cheese should always cost less than extra meat at the same size");
    }

    // ==================== getPrice() TESTS ====================

    @Test
    void getPrice_MeatTopping_MatchesCalculatePrice() {
        // arrange
        ExtraTopping extraTopping = new ExtraTopping("Extra Ham", false, ExtraTopping.ExtraToppingSize.EIGHT);

        // act & assert
        assertEquals(extraTopping.calculatePrice(), extraTopping.getPrice(), 0.01,
                "getPrice() should delegate to calculatePrice() and return the same value");
    }

    @Test
    void getPrice_CheeseTopping_MatchesCalculatePrice() {
        // arrange
        ExtraTopping extraTopping = new ExtraTopping("Extra Swiss", true, ExtraTopping.ExtraToppingSize.EIGHT);

        // act & assert
        assertEquals(extraTopping.calculatePrice(), extraTopping.getPrice(), 0.01,
                "getPrice() should delegate to calculatePrice() and return the same value");
    }

    // ==================== RECEIPT LINE TESTS ====================

    @Test
    void toReceiptLine_MeatTopping_ContainsNameAndPrice() {
        // arrange
        ExtraTopping extraTopping = new ExtraTopping("Extra Steak", false, ExtraTopping.ExtraToppingSize.TWELVE);

        // act
        String receiptLine = extraTopping.toReceiptLine();

        // assert
        assertNotNull(receiptLine);
        assertTrue(receiptLine.contains("Extra Steak"),
                "The receipt line should include the topping name");
        assertTrue(receiptLine.contains("$1.50"),
                "The receipt line should include the formatted price");
    }

    @Test
    void toReceiptLine_CheeseTopping_ContainsNameAndDiscountedPrice() {
        // arrange
        ExtraTopping extraTopping = new ExtraTopping("Extra Cheddar", true, ExtraTopping.ExtraToppingSize.TWELVE);

        // act
        String receiptLine = extraTopping.toReceiptLine();

        // assert
        assertTrue(receiptLine.contains("Extra Cheddar"),
                "The receipt line should include the topping name");
        assertTrue(receiptLine.contains("$0.75"),
                "The receipt line should reflect the cheese discount price");
    }

    @Test
    void toReceiptLine_PriceFormat_MatchesDollarSignPattern() {
        // arrange
        ExtraTopping extraTopping = new ExtraTopping("Extra Turkey", false, ExtraTopping.ExtraToppingSize.EIGHT);

        // act
        String receiptLine = extraTopping.toReceiptLine();

        // assert
        assertTrue(receiptLine.matches(".*\\$\\d+\\.\\d{2}.*"),
                "The receipt line price should follow the $XX.XX format");
    }

    // ==================== CATEGORY TESTS ====================

    @Test
    void getCategory_MeatTopping_ReturnsExtraTopping() {
        // arrange
        ExtraTopping extraTopping = new ExtraTopping("Extra Ham", false, ExtraTopping.ExtraToppingSize.FOUR);

        // act
        String category = extraTopping.getCategory();

        // assert
        assertEquals("Extra Topping", category);
    }

    @Test
    void getCategory_CheeseTopping_ReturnsExtraTopping() {
        // arrange
        ExtraTopping extraTopping = new ExtraTopping("Extra Swiss", true, ExtraTopping.ExtraToppingSize.EIGHT);

        // act
        String category = extraTopping.getCategory();

        // assert
        assertEquals("Extra Topping", category);
    }

    // ==================== ENUM TESTS ====================

    @Test
    void extraToppingSize_FourPrice_ReturnsCorrectBasePrice() {
        // arrange & act
        double price = ExtraTopping.ExtraToppingSize.FOUR.getPrice();

        // assert
        assertEquals(0.50, price, 0.01);
    }

    @Test
    void extraToppingSize_EightPrice_ReturnsCorrectBasePrice() {
        // arrange & act
        double price = ExtraTopping.ExtraToppingSize.EIGHT.getPrice();

        // assert
        assertEquals(1.00, price, 0.01);
    }

    @Test
    void extraToppingSize_TwelvePrice_ReturnsCorrectBasePrice() {
        // arrange & act
        double price = ExtraTopping.ExtraToppingSize.TWELVE.getPrice();

        // assert
        assertEquals(1.50, price, 0.01);
    }
}
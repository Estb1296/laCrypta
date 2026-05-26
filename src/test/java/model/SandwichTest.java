package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SandwichTest {
    private Sandwich sandwich;

    @BeforeEach
    void setUp() {
        sandwich = new Sandwich();
    }
// ==================== BREAD TESTS ====================

    @Test
    void setBread_ValidBread_BreadSet() {
        // arrange
        String expectedBread = "Wheat";

        // act
        sandwich.setBread(expectedBread);

        // assert
        assertEquals(expectedBread, sandwich.getBread());
    }

    @Test
    void setBread_DifferentBreads_BreadUpdated() {
        // arrange
        sandwich.setBread("White");

        // act
        sandwich.setBread("Sourdough");

        // assert
        assertEquals("Sourdough", sandwich.getBread());
    }

    @Test
    void getBread_NoBreadSet_ReturnsNull() {
        // arrange & act
        String bread = sandwich.getBread();

        // assert
        assertNull(bread);
    }

    @Test
    void getBread_BreadSet_ReturnsBread() {
        // arrange
        sandwich.setBread("Rye");

        // act
        String bread = sandwich.getBread();

        // assert
        assertEquals("Rye", bread);
    }

    // ==================== SIZE TESTS ====================

    @Test
    void setSize_FourInch_SizeSetAndPriceUpdated() {
        // arrange
        double expectedPrice = 5.50;

        // act
        sandwich.setSize(Sandwich.SandwichSize.FOUR);

        // assert
        assertEquals(Sandwich.SandwichSize.FOUR, sandwich.getSize());
        assertEquals(expectedPrice, sandwich.getPrice(), 0.01);
    }

    @Test
    void setSize_EightInch_SizeSetAndPriceUpdated() {
        // arrange
        double expectedPrice = 7.00;

        // act
        sandwich.setSize(Sandwich.SandwichSize.EIGHT);

        // assert
        assertEquals(Sandwich.SandwichSize.EIGHT, sandwich.getSize());
        assertEquals(expectedPrice, sandwich.getPrice(), 0.01);
    }

    @Test
    void setSize_TwelveInch_SizeSetAndPriceUpdated() {
        // arrange
        double expectedPrice = 8.50;

        // act
        sandwich.setSize(Sandwich.SandwichSize.TWELVE);

        // assert
        assertEquals(Sandwich.SandwichSize.TWELVE, sandwich.getSize());
        assertEquals(expectedPrice, sandwich.getPrice(), 0.01);
    }

    @Test
    void setSize_ChangeSizeMultipleTimes_SizeUpdates() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.FOUR);

        // act
        sandwich.setSize(Sandwich.SandwichSize.TWELVE);

        // assert
        assertEquals(Sandwich.SandwichSize.TWELVE, sandwich.getSize());
    }

    @Test
    void getSize_NoSizeSet_ReturnsNull() {
        // arrange & act
        Sandwich.SandwichSize size = sandwich.getSize();

        // assert
        assertNull(size);
    }

    @Test
    void getSize_SizeSet_ReturnsSize() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.EIGHT);

        // act
        Sandwich.SandwichSize size = sandwich.getSize();

        // assert
        assertEquals(Sandwich.SandwichSize.EIGHT, size);
    }

    // ==================== MEAT TESTS ====================

    @Test
    void setMeat_ValidMeat_MeatSet() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.TWELVE);
        double basePriceBeforeMeat = sandwich.getPrice();

        // act
        sandwich.setMeat("Turkey");

        // assert
        // 1. Check that the description text contains our meat selection
        String description = sandwich.getDescription();
        assertTrue(description.contains("Turkey"), "The sandwich description should contain 'Turkey'");

        // 2. Check that the 12" meat premium ($3.00) was added to the price pool
        assertEquals(basePriceBeforeMeat + 3.00, sandwich.getPrice(), 0.01,
                "Adding meat to a 12\" sandwich should increase the price by exactly $3.00");
    }

    @Test
    void setMeat_DifferentMeats_MeatUpdatedAndPriceAdjusted() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.TWELVE);
        sandwich.setMeat("Chicken");
        double priceWithChicken = sandwich.getPrice();

        // act
        sandwich.setMeat("Steak");

        // assert
        //Verify that "Steak" successfully overwrote "Chicken" in the summary text
        String description = sandwich.getDescription();
        assertTrue(description.contains("Steak"), "The sandwich description should now contain 'Steak'");
        assertFalse(description.contains("Chicken"), "The old meat 'Chicken' should have been removed");

        //Verify that the price remained identical (proves old price was subtracted before adding new price)
        assertEquals(priceWithChicken, sandwich.getPrice(), 0.01,
                "Swapping meats of equal tier should result in the same total price");
    }

    @Test
    void setMeat_MeatToNone_MeatRemovedAndPriceReduced() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.TWELVE);
        sandwich.setMeat("Ham");
        double priceWithMeat = sandwich.getPrice();

        // act
        sandwich.setMeat("None");

        // assert
        // Verify "Ham" is gone and description reflects "None"
        String description = sandwich.getDescription();
        assertTrue(description.contains("None") || !description.contains("Ham"),
                "The description should show no meat is selected.");

        // Verify the 12" meat price ($3.00) was successfully subtracted
        assertEquals(priceWithMeat - 3.00, sandwich.getPrice(), 0.01,
                "Removing meat from a 12\" sandwich should reduce the price by $3.00");
    }

    @Test
    void getMeat_NoMeatSet_DescriptionShowsNone() {
        // arrange & act
        String description = sandwich.getDescription();

        // assert
        // Since there's no getter, we assert that a default sandwich lists Meat as None
        assertTrue(description.contains("Meat: None") || description.contains("None"),
                "A fresh sandwich should default to having no meat.");
    }

// ==================== CHEESE TESTS ====================

    @Test
    void setCheese_ValidCheese_CheeseSet() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.TWELVE);
        double basePriceBeforeCheese = sandwich.getPrice();

        // act
        sandwich.setCheese("Cheddar");

        // assert
        // Verify description updated
        String description = sandwich.getDescription();
        assertTrue(description.contains("Cheddar"), "The description should contain 'Cheddar'");

        // Verify price increased by the 12" cheese premium ($3.00)
        assertEquals(basePriceBeforeCheese + 3.00, sandwich.getPrice(), 0.01,
                "Adding cheese to a 12\" sandwich should increase total price by $3.00");
    }

    @Test
    void setCheese_DifferentCheeses_CheeseUpdatedAndPriceAdjusted() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.TWELVE);
        sandwich.setCheese("Swiss");
        double priceWithSwiss = sandwich.getPrice();

        // act
        sandwich.setCheese("Provolone");

        // assert
        // Verify text swapped cleanly
        String description = sandwich.getDescription();
        assertTrue(description.contains("Provolone"), "The description should now contain 'Provolone'");
        assertFalse(description.contains("Swiss"), "The old cheese 'Swiss' should be removed");

        // Verify the price remains balanced since both are 12" cheeses ($3.00)
        assertEquals(priceWithSwiss, sandwich.getPrice(), 0.01,
                "Swapping identical tier cheeses should leave the final price unchanged");
    }

    @Test
    void setCheese_CheeseToNone_CheeseRemovedAndPriceReduced() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.TWELVE);
        sandwich.setCheese("Mozzarella");
        double priceWithCheese = sandwich.getPrice();

        // act
        sandwich.setCheese("None");

        // assert
        //  Verify text updates to reflect no cheese
        String description = sandwich.getDescription();
        assertTrue(description.contains("None") || !description.contains("Mozzarella"),
                "The description should show no cheese is selected.");

        // Verify the 12" cheese price ($3.00) was successfully subtracted
        assertEquals(priceWithCheese - 3.00, sandwich.getPrice(), 0.01,
                "Removing cheese from a 12\" sandwich should reduce the price by $3.00");
    }

    @Test
    void getCheese_NoCheeseSet_DescriptionShowsNone() {
        // arrange & act
        String description = sandwich.getDescription();

        // assert
        assertTrue(description.contains("Cheese: None") || description.contains("None"),
                "A fresh sandwich should default to having no cheese.");
    }

    // ==================== TOPPING TESTS ====================

    @Test
    void addTopping_ValidTopping_ToppingAdded() {
        // arrange
        Topping topping = new Topping("Lettuce", 0.00, false);
        double basePriceBeforeTopping = sandwich.getPrice();

        // act
        sandwich.addTopping(topping);

        // assert
        assertEquals(1, sandwich.getToppings().size());
        assertEquals("Lettuce", sandwich.getToppings().get(0).name());
        // to check that price hasn't changed
        assertEquals(basePriceBeforeTopping, sandwich.getPrice(), 0.01);
    }

    @Test
    void addTopping_MultipleToppings_AllToppingsAdded() {
        // arrange
        Topping topping1 = new Topping("Lettuce", 0.00, false);
        Topping topping2 = new Topping("Tomato", 0.00, false);
        Topping topping3 = new Topping("Bacon", 0.50, true);

        // act
        sandwich.addTopping(topping1);
        sandwich.addTopping(topping2);
        sandwich.addTopping(topping3);

        // assert
        assertEquals(3, sandwich.getToppings().size());
    }

    @Test
    void addTopping_PremiumTopping_PriceIncreases() {
        // arrange
        Topping premiumTopping = new Topping("Bacon", 0.50, true);
        double basePriceBeforeTopping = sandwich.getPrice();

        // act
        sandwich.addTopping(premiumTopping);

        // assert
        assertEquals(basePriceBeforeTopping + 0.50, sandwich.getPrice(), 0.01);
    }

    @Test
    void removeTopping_ValidIndex_ToppingRemovedAndPriceAdjusted() {
        // arrange
        Topping topping = new Topping("Lettuce", 0.00, false);
        sandwich.addTopping(topping);
        double priceWithTopping = sandwich.getPrice();

        // act
        sandwich.removeTopping(0);

        // assert
        assertEquals(0, sandwich.getToppings().size());
        assertEquals(priceWithTopping, sandwich.getPrice(), 0.01);
    }

    @Test
    void removeTopping_InvalidIndex_NoExceptionThrown() {
        // arrange
        Topping topping = new Topping("Lettuce", 0.00, false);
        sandwich.addTopping(topping);

        // act & assert
        assertDoesNotThrow(() -> sandwich.removeTopping(10));
        assertEquals(1, sandwich.getToppings().size());
    }

    @Test
    void removeTopping_NegativeIndex_NoExceptionThrown() {
        // arrange
        Topping topping = new Topping("Lettuce", 0.00, false);
        sandwich.addTopping(topping);

        // act & assert
        assertDoesNotThrow(() -> sandwich.removeTopping(-1));
        assertEquals(1, sandwich.getToppings().size());
    }

    @Test
    void getToppings_NoToppingsAdded_ReturnsEmptyList() {
        // arrange & act
        ArrayList<Topping> toppings = sandwich.getToppings();

        // assert
        assertNotNull(toppings);
        assertEquals(0, toppings.size());
    }

    @Test
    void getToppings_ToppingsAdded_ReturnsToppings() {
        // arrange
        Topping topping1 = new Topping("Lettuce", 0.00, false);
        Topping topping2 = new Topping("Tomato", 0.00, false);
        sandwich.addTopping(topping1);
        sandwich.addTopping(topping2);

        // act
        ArrayList<Topping> toppings = sandwich.getToppings();

        // assert
        assertEquals(2, toppings.size());
    }

    // ==================== TOASTING TESTS ====================

    @Test
    void setToasted_ToastTrue_ToastingEnabled() {
        // arrange & act
        sandwich.setToasted(true);

        // assert
        assertTrue(sandwich.isToasted());
    }

    @Test
    void setToasted_ToastFalse_ToastingDisabled() {
        // arrange
        sandwich.setToasted(true);

        // act
        sandwich.setToasted(false);

        // assert
        assertFalse(sandwich.isToasted());
    }

    @Test
    void isToasted_DefaultValue_ReturnsFalse() {
        // arrange & act
        boolean toasted = sandwich.isToasted();

        // assert
        assertFalse(toasted);
    }

    @Test
    void isToasted_AfterSettingTrue_ReturnsTrue() {
        // arrange
        sandwich.setToasted(true);

        // act
        boolean toasted = sandwich.isToasted();

        // assert
        assertTrue(toasted);
    }

    // ==================== SAUCE TESTS ====================

    @Test
    void addSauce_ValidSauce_SauceAdded() {
        // arrange & act
        sandwich.addSauce("Mayo");

        // assert
        assertTrue(sandwich.getSauces().contains("Mayo"));
        assertEquals(1, sandwich.getSauces().size());
    }

    @Test
    void addSauce_MultipleSauces_AllSaucesAdded() {
        // arrange & act
        sandwich.addSauce("Mayo");
        sandwich.addSauce("Mustard");
        sandwich.addSauce("Ketchup");

        // assert
        assertEquals(3, sandwich.getSauces().size());
        assertTrue(sandwich.getSauces().contains("Mayo"));
        assertTrue(sandwich.getSauces().contains("Mustard"));
        assertTrue(sandwich.getSauces().contains("Ketchup"));
    }

    @Test
    void addSauce_DuplicateSauce_SauceNotAddedTwice() {
        // arrange & act
        sandwich.addSauce("Ranch");
        sandwich.addSauce("Ranch");

        // assert
        assertEquals(1, sandwich.getSauces().size());
    }

    @Test
    void removeSauce_ValidSauce_SauceRemoved() {
        // arrange
        sandwich.addSauce("Vinaigrette");

        // act
        sandwich.removeSauce("Vinaigrette");

        // assert
        assertEquals(0, sandwich.getSauces().size());
        assertFalse(sandwich.getSauces().contains("Vinaigrette"));
    }

    @Test
    void removeSauce_NonexistentSauce_NoExceptionThrown() {
        // arrange
        sandwich.addSauce("Mayo");

        // act & assert
        assertDoesNotThrow(() -> sandwich.removeSauce("Mustard"));
        assertEquals(1, sandwich.getSauces().size());
    }

    @Test
    void getSauces_NoSaucesAdded_ReturnsEmptyList() {
        // arrange & act
        ArrayList<String> sauces = sandwich.getSauces();

        // assert
        assertNotNull(sauces);
        assertEquals(0, sauces.size());
    }

    @Test
    void getSauces_SaucesAdded_ReturnsSauces() {
        // arrange
        sandwich.addSauce("Mayo");
        sandwich.addSauce("Mustard");

        // act
        ArrayList<String> sauces = sandwich.getSauces();

        // assert
        assertEquals(2, sauces.size());
    }

    // ==================== EXTRA TOPPING TESTS ====================

    @Test
    void addExtraTopping_ValidExtraTopping_ExtraToppingAdded() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.EIGHT);
        ExtraTopping extraTopping = new ExtraTopping("Extra Meat",  ExtraTopping.ExtraToppingSize.TWELVE);
        double basePriceBeforeExtra = sandwich.getPrice();

        // act
        sandwich.addExtraTopping(extraTopping);

        // assert
        assertEquals(1, sandwich.getExtraTopping().size());
        assertEquals(basePriceBeforeExtra + 1.50, sandwich.getPrice(), 0.01,
                "Adding a 12\" extra meat portion should add exactly $1.50 to the price pool");
    }

    @Test
    void addExtraTopping_MultipleExtraToppings_AllAdded() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.TWELVE);
        ExtraTopping extra1 = new ExtraTopping("Extra Meat", ExtraTopping.ExtraToppingSize.TWELVE);
        ExtraTopping extra2 = new ExtraTopping("Extra Cheese", ExtraTopping.ExtraToppingSize.TWELVE);

        // act
        sandwich.addExtraTopping(extra1);
        sandwich.addExtraTopping(extra2);

        // assert
        assertEquals(2, sandwich.getExtraTopping().size());
    }

    @Test
    void removeExtraTopping_ValidIndex_ExtraToppingRemoved() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.TWELVE);
        double basePrice = sandwich.getPrice(); // Capture base price before adding anything

        ExtraTopping extraTopping = new ExtraTopping("Extra Meat", ExtraTopping.ExtraToppingSize.TWELVE);
        sandwich.addExtraTopping(extraTopping);

        // act
        sandwich.removeExtraTopping(0);

        // assert
        assertEquals(0, sandwich.getExtraTopping().size(), "The extra topping list should be empty.");
        assertEquals(basePrice, sandwich.getPrice(), 0.01, "Sandwich price should return to its base price.");
    }
    @Test
    void removeExtraTopping_ValidIndex_ToppingRemovedAndPriceReverted() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.TWELVE);
        ExtraTopping extraTopping = new ExtraTopping("Extra Meat", ExtraTopping.ExtraToppingSize.TWELVE);
        sandwich.addExtraTopping(extraTopping);

        // Dynamically get the price of the topping instead of hardcoding 2.00
        double toppingPrice = extraTopping.getPrice();
        double priceWithExtra = sandwich.getPrice();

        // act
        sandwich.removeExtraTopping(0);

        // assert
        assertEquals(0, sandwich.getExtraTopping().size(), "The extra topping list should be empty.");
        assertEquals(priceWithExtra - toppingPrice, sandwich.getPrice(), 0.01, "Sandwich price did not revert correctly.");
    }

    @Test
    void removeExtraTopping_InvalidIndex_NoExceptionThrown() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.TWELVE);
        ExtraTopping extraTopping = new ExtraTopping("Extra Meat", ExtraTopping.ExtraToppingSize.TWELVE);
        sandwich.addExtraTopping(extraTopping);

        // act & assert
        assertDoesNotThrow(() -> sandwich.removeExtraTopping(10));
        assertEquals(1, sandwich.getExtraTopping().size());
    }

    @Test
    void getExtraTopping_NoExtraToppingsAdded_ReturnsEmptyList() {
        // arrange & act
        ArrayList<ExtraTopping> extraToppings = sandwich.getExtraTopping();

        // assert
        assertNotNull(extraToppings);
        assertEquals(0, extraToppings.size());
    }

    @Test
    void getExtraTopping_ExtraToppingsAdded_ReturnsExtraToppings() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.TWELVE);
        ExtraTopping extraTopping = new ExtraTopping("Extra Meat", ExtraTopping.ExtraToppingSize.TWELVE);
        sandwich.addExtraTopping(extraTopping);

        // act
        ArrayList<ExtraTopping> extraToppings = sandwich.getExtraTopping();

        // assert
        assertEquals(1, extraToppings.size());
    }

    // ==================== PRICE TESTS ====================

    @Test
    void calculatePrice_NoCustomizationNoParam_ReturnsBasePrice() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.TWELVE);

        // act
        double price = sandwich.calculatePrice();

        // assert
        assertEquals(8.50, price, 0.01);
    }

    @Test
    void calculatePrice_WithMeatAndCheese_ReturnsCorrectPrice() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.TWELVE);
        sandwich.setMeat("Turkey");
        sandwich.setCheese("Cheddar");

        // act
        double price = sandwich.calculatePrice();

        // assert
        // Base: 8.50 + Meat: 3.00 + Cheese: 3.00 = 14.50
        assertEquals(14.50, price, 0.01);
    }

    @Test
    void calculatePrice_WithToppings_ReturnsCorrectPrice() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.TWELVE);
        Topping topping = new Topping("Bacon", 0.50, true);
        sandwich.addTopping(topping);

        // act
        double price = sandwich.calculatePrice();

        // assert
        assertEquals(9.00, price, 0.01);
    }

    @Test
    void calculatePrice_FullCustomization_ReturnsCorrectPrice() {
        // arrange
        Sandwich cleanSandwich = new Sandwich();
        cleanSandwich.setSize(Sandwich.SandwichSize.TWELVE);
        cleanSandwich.setMeat("Steak");
        cleanSandwich.setCheese("Swiss");

        cleanSandwich.addTopping(new Topping("Lettuce", 0.00, false));
        cleanSandwich.addTopping(new Topping("Bacon", 0.50, true));
        ExtraTopping extraTopping = new ExtraTopping("Extra Meat", ExtraTopping.ExtraToppingSize.TWELVE);
        cleanSandwich.addExtraTopping(extraTopping);

        // act
        double baseTwelveInchPrice = 8.50;
        double premiumMeatPrice = 3.00;
        double premiumCheesePrice = 3.00;
        double baconPrice = 0.00;

        double extraMeatPrice = 2.00;

        double expectedPrice = baseTwelveInchPrice + premiumMeatPrice + premiumCheesePrice + baconPrice + extraMeatPrice;
        double price = cleanSandwich.calculatePrice();

        // assert
        assertEquals(expectedPrice, price, 0.01,
                String.format("Price mismatch! Expected: $%.2f but Production Code calculated: $%.2f", expectedPrice, price));
    }

    @Test
    void getPrice_NoCustomization_ReturnsBasePrice() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.EIGHT);

        // act
        double price = sandwich.getPrice();

        // assert
        assertEquals(7.00, price, 0.01);
    }

    @Test
    void getPrice_WithCustomization_ReturnsTotalPrice() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.FOUR);
        sandwich.setMeat("Chicken");

        // act
        double price = sandwich.getPrice();

        // assert
        // Base: 5.50 + Meat: 1.00 = 6.50
        assertEquals(6.50, price, 0.01);
    }

    // ==================== NAME TESTS ====================

    @Test
    void getName_NoCustomization_ReturnsCustomSandwich() {
        // arrange & act
        String name = sandwich.getName();

        // assert
        assertEquals("Custom Sandwich", name);
    }

    @Test
    void getName_WithSizeAndBread_ReturnsFormattedName() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.TWELVE);
        sandwich.setBread("Wheat");

        // act
        String name = sandwich.getName();

        // assert
        assertEquals("12\" Wheat Sandwich", name);
    }

    @Test
    void getName_WithSizeAndBreadAndToasted_ReturnsFormattedNameWithToasted() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.EIGHT);
        sandwich.setBread("Sourdough");
        sandwich.setToasted(true);

        // act
        String name = sandwich.getName();

        // assert
        assertEquals("8\" Sourdough Sandwich (Toasted)", name);
    }

    @Test
    void getName_WithSizeOnlyNoToasting_ReturnsNameWithoutToasted() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.FOUR);
        sandwich.setBread("Rye");
        sandwich.setToasted(false);

        // act
        String name = sandwich.getName();

        // assert
        assertEquals("4\" Rye Sandwich", name);
    }

    // ==================== DESCRIPTION TESTS ====================

    @Test
    void getDescription_NoCustomization_ReturnsValidDescription() {
        // arrange & act
        String description = sandwich.getDescription();

        // assert
        assertNotNull(description);
        assertTrue(description.contains("Sandwich Specifications"));
    }

    @Test
    void getDescription_WithAllCustomizations_ReturnsCompleteDescription() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.TWELVE);
        sandwich.setBread("Wheat");
        sandwich.setMeat("Turkey");
        sandwich.setCheese("Swiss");
        sandwich.setToasted(true);
        sandwich.addTopping(new Topping("Lettuce", 0.00, false));
        sandwich.addTopping(new Topping("Bacon", 0.50, true));
        sandwich.addSauce("Mayo");
        sandwich.addSauce("Mustard");

        // act
        String description = sandwich.getDescription();

        // assert
        assertTrue(description.contains("12\""));
        assertTrue(description.contains("Wheat"));
        assertTrue(description.contains("Turkey"));
        assertTrue(description.contains("Swiss"));
        assertTrue(description.contains("Yes"));
        assertTrue(description.contains("Lettuce"));
        assertTrue(description.contains("Bacon"));
        assertTrue(description.contains("Mayo"));
        assertTrue(description.contains("Mustard"));
    }

    @Test
    void getDescription_WithoutSauces_DescriptionShowsNoneSauces() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.EIGHT);
        sandwich.setBread("White");

        // act
        String description = sandwich.getDescription();

        // assert
        assertTrue(description.contains("Sauces"));
    }

    // ==================== RECEIPT LINE TESTS ====================

    @Test
    void toReceiptLine_BasicSandwich_ReturnsFormattedString() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.TWELVE);
        sandwich.setBread("Wheat");

        // act
        String receiptLine = sandwich.toReceiptLine();

        // assert
        assertNotNull(receiptLine);
        assertTrue(receiptLine.contains("12\""));
        assertTrue(receiptLine.contains("Wheat"));
        assertTrue(receiptLine.contains("$"));
    }

    @Test
    void toReceiptLine_ToastedSandwich_IncludesToastedLabel() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.EIGHT);
        sandwich.setBread("Sourdough");
        sandwich.setToasted(true);

        // act
        String receiptLine = sandwich.toReceiptLine();

        // assert
        assertTrue(receiptLine.contains("TOASTED"));
    }

    @Test
    void toReceiptLine_NonToastedSandwich_DoesNotIncludeToastedLabel() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.FOUR);
        sandwich.setBread("Rye");
        sandwich.setToasted(false);

        // act
        String receiptLine = sandwich.toReceiptLine();

        // assert
        assertFalse(receiptLine.contains("TOASTED"));
    }

    @Test
    void toReceiptLine_WithPrice_IncludesPriceFormatted() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.TWELVE);
        sandwich.setBread("White");
        sandwich.setMeat("Ham");

        // act
        String receiptLine = sandwich.toReceiptLine();

        // assert
        assertTrue(receiptLine.matches(".*\\$\\d+\\.\\d{2}.*"));  // Matches $XX.XX format
    }

    // ==================== CATEGORY TESTS ====================

    @Test
    void getCategory_AnySandwich_ReturnsSandwich() {
        // arrange
        sandwich.setSize(Sandwich.SandwichSize.TWELVE);
        sandwich.setBread("Wheat");

        // act
        String category = sandwich.getCategory();

        // assert
        assertEquals("Sandwich", category);
    }

    @Test
    void getCategory_DefaultSandwich_ReturnsSandwich() {
        // arrange & act
        String category = sandwich.getCategory();

        // assert
        assertEquals("Sandwich", category);
    }

    // ==================== CONSTRUCTOR TESTS ====================

    @Test
    void constructor_NoParameters_InitializesDefaultValues() {
        // arrange & act
        Sandwich defaultSandwich = new Sandwich();

        // assert
        assertEquals("Custom Sandwich", defaultSandwich.getName());
        assertEquals(0.0, defaultSandwich.getPrice(), 0.01);
        assertEquals(0, defaultSandwich.getToppings().size());
        assertFalse(defaultSandwich.isToasted());
    }

    @Test
    void constructor_WithThreeParameters_GeneratesCorrectDynamicName() {
        // arrange & act - Testing your brand new 3-parameter overloaded constructor
        Sandwich dynamicSandwich = new Sandwich(Sandwich.SandwichSize.TWELVE, "Wheat", true);

        // assert
        assertEquals("12\" Wheat Sandwich (Toasted)", dynamicSandwich.getName(),
                "The name should be dynamically generated based on size, bread, and toast status.");
        assertEquals(8.50, dynamicSandwich.getPrice(), 0.01);
        assertEquals(Sandwich.SandwichSize.TWELVE, dynamicSandwich.getSize());
        assertEquals("Wheat", dynamicSandwich.getBread());
        assertTrue(dynamicSandwich.isToasted());
    }

    @Test
    void constructor_WithFourParameters_InitializesCorrectly() {
        // arrange & act - Testing your legacy 4-parameter constructor
        Sandwich legacySandwich = new Sandwich("Turkey Club", Sandwich.SandwichSize.TWELVE, "Wheat", true);

        // assert
        // the passed string "Turkey Club" is ignored, and the dynamic string is returned.
        assertEquals("12\" Wheat Sandwich (Toasted)", legacySandwich.getName(),
                "Even with a custom name passed, getName() overrides it with the dynamic specifications.");
        assertEquals(8.50, legacySandwich.getPrice(), 0.01);
        assertEquals(Sandwich.SandwichSize.TWELVE, legacySandwich.getSize());
        assertEquals("Wheat", legacySandwich.getBread());
        assertTrue(legacySandwich.isToasted());
    }

    @Test
    void constructor_WithParameters_ToppingsListInitialized() {
        // arrange & act
        Sandwich parameterizedSandwich = new Sandwich("Turkey Club", Sandwich.SandwichSize.TWELVE, "Wheat", true);

        // assert
        assertNotNull(parameterizedSandwich.getToppings());
        assertEquals(0, parameterizedSandwich.getToppings().size());
    }
}

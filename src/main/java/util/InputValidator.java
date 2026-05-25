package util;

import java.util.InputMismatchException;
import java.util.Scanner;

    public class InputValidator {

        /**
         * Get valid menu choice (integer input)
         * Handles nonInteger inputs and out-of-range values gracefully
         * @param input Scanner object
         * @param minOption Minimum valid option
         * @param maxOption Maximum valid option
         * @return Valid integer choice
         */
        public static int getValidMenuChoice(Scanner input, int minOption, int maxOption) {
            while (true) {
                try {
                    int choice = input.nextInt();
                    input.nextLine();  // Clear buffer

                    if (choice < minOption || choice > maxOption) {
                        System.out.println("❌ Invalid choice. Please select " + minOption + "-" + maxOption);
                        continue;
                    }
                    return choice;
                } catch (InputMismatchException e) {
                    input.nextLine();  // Clear invalid input
                    System.out.println("❌ Invalid input. Please enter a number.");
                }
            }
        }

        /**
         * Get valid single character choice (A-Z, 0-9)
         * Handles invalid inputs and case-insensitivity gracefully
         * @param input Scanner object
         * @param validChars String of valid characters (e.g., "ABC0")
         * @return Valid character choice in uppercase
         */
        public static String getValidCharChoice(Scanner input, String validChars) {
            while (true) {
                try {
                    String inputString = input.nextLine().trim().toUpperCase();

                    if (inputString.isEmpty()) {
                        System.out.println("❌ Input cannot be empty. Please try again.");
                        continue;
                    }

                    if (inputString.length() != 1) {
                        System.out.println("❌ Please enter a single character only.");
                        continue;
                    }

                    if (!validChars.contains(inputString)) {
                        System.out.println("❌ Invalid choice. Valid options: " + validChars);
                        continue;
                    }

                    return inputString;
                } catch (Exception e) {
                    System.out.println("❌ Invalid input. Please try again.");
                }
            }
        }

        /**
         * Get valid string input (non-empty)
         * Handles empty inputs and special characters
         * @param input Scanner object
         * @param prompt Prompt to display to user
         * @return Valid non-empty string
         */
        public static String getValidStringInput(Scanner input, String prompt) {
            while (true) {
                try {
                    System.out.print(prompt);
                    String inputString = input.nextLine().trim();

                    if (inputString.isEmpty()) {
                        System.out.println("❌ Input cannot be empty. Please try again.");
                        continue;
                    }

                    return inputString;
                } catch (Exception e) {
                    System.out.println("❌ Invalid input. Please try again.");
                }
            }
        }

        /**
         * Get valid integer input within range
         * Handles nonInteger inputs and out-of-range values
         * @param input Scanner object
         * @param prompt Prompt to display to user
         * @param minValue Minimum valid value
         * @param maxValue Maximum valid value
         * @return Valid integer within range
         */
        public static int getValidIntegerInput(Scanner input, String prompt, int minValue, int maxValue) {
            while (true) {
                try {
                    System.out.print(prompt);
                    int value = input.nextInt();
                    input.nextLine();  // Clear buffer

                    if (value < minValue || value > maxValue) {
                        System.out.println("❌ Please enter a number between " + minValue + " and " + maxValue);
                        continue;
                    }

                    return value;
                } catch (InputMismatchException e) {
                    input.nextLine();  // Clear invalid input
                    System.out.println("❌ Invalid input. Please enter a whole number.");
                }
            }
        }

        /**
         * Get valid double input (for prices, amounts)
         * Handles non-numeric inputs and negative values
         * @param input Scanner object
         * @param prompt Prompt to display to user
         * @return Valid positive double value
         */
        public static double getValidDoubleInput(Scanner input, String prompt) {
            while (true) {
                try {
                    System.out.print(prompt);
                    double value = input.nextDouble();
                    input.nextLine();  // Clear buffer

                    if (value < 0) {
                        System.out.println("❌ Please enter a positive number.");
                        continue;
                    }

                    return value;
                } catch (InputMismatchException e) {
                    input.nextLine();  // Clear invalid input
                    System.out.println("❌ Invalid input. Please enter a valid number.");
                }
            }
        }

        /**
         * Get yes/no confirmation from user
         * @param input Scanner object
         * @param prompt Prompt to display to user
         * @return true for "Y", false for "N"
         */
        public static boolean getConfirmation(Scanner input, String prompt) {
            while (true) {
                System.out.print(prompt + " (Y/N): ");
                String response = input.nextLine().trim().toUpperCase();

                if (response.equals("Y")) {
                    return true;
                } else if (response.equals("N")) {
                    return false;
                } else {
                    System.out.println("❌ Please enter 'Y' for yes or 'N' for no.");
                }
            }
        }

        /**
         * Get valid list index from user (for removing items)
         * @param input Scanner object
         * @param listSize Size of the list
         * @return Valid index (0-based), or -1 if user wants to cancel
         */
        public static int getValidListIndex(Scanner input, int listSize) {
            while (true) {
                try {
                    System.out.print("Enter item number (0 to cancel): ");
                    int choice = input.nextInt();
                    input.nextLine();  // Clear buffer

                    if (choice == 0) {
                        return -1;  // User canceled
                    }

                    if (choice > 0 && choice <= listSize) {
                        return choice - 1;  // Convert to 0-based index
                    } else {
                        System.out.println("❌ Invalid item number. Please select 1-" + listSize);
                    }
                } catch (InputMismatchException e) {
                    input.nextLine();  // Clear invalid input
                    System.out.println("❌ Invalid input. Please enter a number.");
                }
            }
        }
    }

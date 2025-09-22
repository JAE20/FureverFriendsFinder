/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.furever.utils;

/**
 *
 * @author jerimiahtongco
 */
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Utility class for input validation and exception handling
 */
public class InputValidator {
    
    private static final Scanner scanner = new Scanner(System.in);
    
    // Email pattern for validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    // Phone pattern for validation (Philippine mobile numbers)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^09\\d{9}$");
    
    /**
     * Gets integer input with validation
     * @param prompt Message to display to user
     * @param min Minimum allowed value
     * @param max Maximum allowed value
     * @return Valid integer input
     */
    public static int getIntInput(String prompt, int min, int max) {
        int input;
        while (true) {
            try {
                System.out.print(prompt);
                input = Integer.parseInt(scanner.nextLine().trim());
                
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
    
    /**
     * Gets integer input with validation (no range restriction)
     * @param prompt Message to display to user
     * @return Valid integer input
     */
    public static int getIntInput(String prompt) {
        int input;
        while (true) {
            try {
                System.out.print(prompt);
                input = Integer.parseInt(scanner.nextLine().trim());
                return input;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
    
    /**
     * Gets string input with validation
     * @param prompt Message to display to user
     * @param allowEmpty Whether empty strings are allowed
     * @return Valid string input
     */
    public static String getStringInput(String prompt, boolean allowEmpty) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            
            if (!input.isEmpty() || allowEmpty) {
                return input;
            } else {
                System.out.println("Input cannot be empty. Please try again.");
            }
        }
    }
    
    /**
     * Gets string input with length validation
     * @param prompt Message to display to user
     * @param minLength Minimum length required
     * @param maxLength Maximum length allowed
     * @return Valid string input
     */
    public static String getStringInput(String prompt, int minLength, int maxLength) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            
            if (input.length() >= minLength && input.length() <= maxLength) {
                return input;
            } else {
                System.out.println("Input must be between " + minLength + " and " + maxLength + " characters.");
            }
        }
    }
    
    /**
     * Gets email input with validation
     * @param prompt Message to display to user
     * @return Valid email input
     */
    public static String getEmailInput(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            
            if (EMAIL_PATTERN.matcher(input).matches()) {
                return input;
            } else {
                System.out.println("Invalid email format. Please enter a valid email address.");
            }
        }
    }
    
    /**
     * Gets phone number input with validation
     * @param prompt Message to display to user
     * @return Valid phone number input
     */
    public static String getPhoneInput(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            
            if (PHONE_PATTERN.matcher(input).matches()) {
                return input;
            } else {
                System.out.println("Invalid phone format. Please enter a valid Philippine mobile number (09xxxxxxxxx).");
            }
        }
    }
    
    /**
     * Gets choice input for menu options
     * @param prompt Message to display to user
     * @param validChoices Array of valid choice characters
     * @return Valid choice character
     */
    public static char getChoiceInput(String prompt, char[] validChoices) {
        char input;
        while (true) {
            System.out.print(prompt);
            String inputStr = scanner.nextLine().trim().toLowerCase();
            
            if (inputStr.length() == 1) {
                input = inputStr.charAt(0);
                
                for (char validChoice : validChoices) {
                    if (Character.toLowerCase(validChoice) == input) {
                        return input;
                    }
                }
            }
            
            System.out.print("Invalid choice. Valid options are: ");
            for (int i = 0; i < validChoices.length; i++) {
                System.out.print(validChoices[i]);
                if (i < validChoices.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
    }
    
    /**
     * Gets confirmation input (Y/N)
     * @param prompt Message to display to user
     * @return true for yes, false for no
     */
    public static boolean getConfirmation(String prompt) {
        char choice = getChoiceInput(prompt + " (Y/N): ", new char[]{'y', 'n'});
        return choice == 'y';
    }
    
    /**
     * Validates and formats date input
     * @param prompt Message to display to user
     * @return Valid date string in YYYY-MM-DD format
     */
    public static String getDateInput(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt + " (YYYY-MM-DD): ");
            input = scanner.nextLine().trim();
            
            // Basic date format validation
            if (input.matches("\\d{4}-\\d{2}-\\d{2}")) {
                try {
                    java.sql.Date.valueOf(input); // This will throw exception if invalid date
                    return input;
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid date. Please enter a valid date in YYYY-MM-DD format.");
                }
            } else {
                System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
            }
        }
    }
    
    /**
     * Waits for user to press Enter to continue
     */
    public static void waitForEnter() {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Clears the console (works on most terminals)
     */
    public static void clearScreen() {
        try {
            // For Windows
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // For Unix/Linux/Mac
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            // If clearing doesn't work, just print some newlines
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    
    /**
     * Displays a formatted header
     * @param title Header title
     */
    public static void displayHeader(String title) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println(centerText(title, 60));
        System.out.println("=".repeat(60));
    }
    
    /**
     * Centers text within a given width
     * @param text Text to center
     * @param width Total width
     * @return Centered text
     */
    private static String centerText(String text, int width) {
        if (text.length() >= width) {
            return text;
        }
        
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(width - text.length() - padding);
    }
    
    /**
     * Displays error message in a formatted way
     * @param message Error message to display
     */
    public static void displayError(String message) {
        System.out.println("\n❌ ERROR: " + message + "\n");
    }
    
    /**
     * Displays success message in a formatted way
     * @param message Success message to display
     */
    public static void displaySuccess(String message) {
        System.out.println("\n✅ SUCCESS: " + message + "\n");
    }
    
    /**
     * Displays warning message in a formatted way
     * @param message Warning message to display
     */
    public static void displayWarning(String message) {
        System.out.println("\n⚠️ WARNING: " + message + "\n");
    }
}
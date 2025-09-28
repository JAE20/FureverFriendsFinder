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

public class InputValidator {

    private static final String INDENT = "\t\t\t\t\t";
    private static final String INDENT1 = "\t\t\t";
    
    public static final String RESET = "\u001B[0m";
    
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m"; // Used for Headers
    public static final String CYAN = "\u001B[36m";

    // Text Styles (Can be combined with colors, e.g., BOLD + PURPLE)
    public static final String BOLD = "\u001B[1m";
    private static final Scanner scanner = new Scanner(System.in);

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile("^09\\d{9}$");

    public static int getIntInput(String prompt, int min, int max) {
        int input;
        while (true) {
            try {
                System.out.print(prompt);
                input = Integer.parseInt(scanner.nextLine().trim());

                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.println(INDENT + "Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println(INDENT + "Invalid input. Please enter a valid number.");
            }
        }
    }

    public static int getIntInput(String prompt) {
        int input;
        while (true) {
            try {
                System.out.print(prompt);
                input = Integer.parseInt(scanner.nextLine().trim());
                return input;
            } catch (NumberFormatException e) {
                System.out.println(INDENT + "Invalid input. Please enter a valid number.");
            }
        }
    }

    public static String getStringInput(String prompt, boolean allowEmpty) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();

            if (!input.isEmpty() || allowEmpty) {
                return input;
            } else {
                System.out.println(INDENT + "Input cannot be empty. Please try again.");
            }
        }
    }

    public static String getStringInput(String prompt, int minLength, int maxLength) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();

            if (input.length() >= minLength && input.length() <= maxLength) {
                return input;
            } else {
                System.out.println(INDENT + "Input must be between " + minLength + " and " + maxLength + " characters.");
            }
        }
    }

    public static String getEmailInput(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();

            if (EMAIL_PATTERN.matcher(input).matches()) {
                return input;
            } else {
                System.out.println(INDENT + "Invalid email format. Please enter a valid email address.");
            }
        }
    }

    /**
     * Gets phone number input with validation
     *
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
                System.out.println(INDENT + "Invalid phone format. Please enter a valid Philippine mobile number (09xxxxxxxxx).");
            }
        }
    }

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

            System.out.print(INDENT + "Invalid choice. Valid options are: ");
            for (int i = 0; i < validChoices.length; i++) {
                System.out.print(validChoices[i]);
                if (i < validChoices.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
    }

    public static boolean getConfirmation(String prompt) {
        char choice = getChoiceInput(prompt + " (Y/N): ", new char[]{'y', 'n'});
        return choice == 'y';
    }

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
                    System.out.println(INDENT + "Invalid date. Please enter a valid date in YYYY-MM-DD format.");
                }
            } else {
                System.out.println(INDENT + "Invalid date format. Please use YYYY-MM-DD format.");
            }
        }
    }

    /**
     * Waits for user to press Enter to continue
     */
    public static void waitForEnter() {
        System.out.print(INDENT + "Press Enter to continue...");
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
     *
     * @param title Header title
     */
    public static void displayHeader(String title) {
        System.out.println(INDENT + "=".repeat(65));
        System.out.println(centerText(title, 65));
        System.out.println(INDENT + "=".repeat(65));
    }

    /**
     * Centers text within a given width
     *
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

    public static void displayError(String message) {
        System.out.println(InputValidator.RED + INDENT + "ERROR: " + message + "\n" + InputValidator.RESET);
    }

    public static void displaySuccess(String message) {
        System.out.println(InputValidator.GREEN + INDENT + "SUCCESS: " + message + "\n" + InputValidator.RESET);
    }

    public static void displayWarning(String message) {
        System.out.println(InputValidator.YELLOW + INDENT + "WARNING: " + message + "\n" + InputValidator.RESET);
    }
}

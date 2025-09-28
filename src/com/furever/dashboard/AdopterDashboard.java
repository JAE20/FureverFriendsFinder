/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.furever.dashboard;

/**
 *
 * @author jerimiahtongco
 */
import com.furever.crud.AdopterCRUD;
import com.furever.models.Adopter;
import com.furever.utils.InputValidator;

import java.util.List;


public class AdopterDashboard {

    private static final String INDENT = "\t\t\t\t\t";

    private final AdopterCRUD adopterCRUD;

    public AdopterDashboard() {
        this.adopterCRUD = new AdopterCRUD();
    }

 
    public void showAdopterMenu() {
        while (true) {
            System.out.println(INDENT + "=================================================================");
            System.out.println(InputValidator.BLUE + INDENT + "                 ADOPTER MANAGEMENT DASHBOARD" + InputValidator.RESET);
            System.out.println(INDENT + "=================================================================");
            System.out.println(INDENT + "1. Add New Adopter");
            System.out.println(INDENT + "2. View All Adopters");
            System.out.println(INDENT + "3. Search Adopter by ID");
            System.out.println(INDENT + "4. Search Adopter by Name");
            System.out.println(INDENT + "5. Update Adopter");
            System.out.println(INDENT + "6. Delete Adopter");
            System.out.println(INDENT + "7. View Adopter Statistics");
            System.out.println(INDENT + "8. Return to Main Menu");
            System.out.println(INDENT + "-".repeat(60));

            int choice = InputValidator.getIntInput(INDENT + "Enter your choice (1-8): ", 1, 8);

            switch (choice) {
                case 1:
                    addAdopter();
                    break;
                case 2:
                    viewAllAdopters();
                    break;
                case 3:
                    searchAdopterById();
                    break;
                case 4:
                    searchAdopterByName();
                    break;
                case 5:
                    updateAdopter();
                    break;
                case 6:
                    deleteAdopter();
                    break;
                case 7:
                    viewAdopterStatistics();
                    break;
                case 8:
                    return;
                default:
                    InputValidator.displayError("Invalid choice. Please try again.");
            }

            InputValidator.waitForEnter();
        }
    }

    /**
     * Adds a new adopter
     */
    private void addAdopter() {
        InputValidator.displayHeader(InputValidator.BLUE + INDENT + "ADD NEW ADOPTER" + InputValidator.RESET );

        try {
            String name = InputValidator.getStringInput(INDENT + "Enter adopter name: ", false);
            String contact = InputValidator.getPhoneInput(INDENT + "Enter contact number: ");
            String email = InputValidator.getEmailInput(INDENT + "Enter email: ");
            String address = InputValidator.getStringInput(INDENT + "Enter address: ", false);
            String profile = InputValidator.getStringInput(INDENT + "Enter profile/bio (optional): ", true);

            String username = InputValidator.getStringInput(INDENT + "Enter username (optional): ", true);
            if (!username.isEmpty()) {
                // Check if username already exists
                if (adopterCRUD.getAdopterByUsername(username) != null) {
                    InputValidator.displayError("Username already exists. Please choose a different username.");
                    return;
                }
            }

            String password = "";
            if (!username.isEmpty()) {
                password = InputValidator.getStringInput(INDENT + "Enter password: ", 6, 255);
            }

            Adopter adopter = new Adopter();
            adopter.setAdopterName(name);
            adopter.setAdopterContact(contact);
            adopter.setAdopterEmail(email);
            adopter.setAdopterAddress(address);
            adopter.setAdopterProfile(profile.isEmpty() ? null : profile);
            adopter.setAdopterUsername(username.isEmpty() ? null : username);
            adopter.setAdopterPassword(password.isEmpty() ? null : password);

            if (adopterCRUD.createAdopter(adopter)) {
                InputValidator.displaySuccess("Adopter created successfully!");
                System.out.println(INDENT + "Adopter ID: " + adopter.getAdopterId());
            } else {
                InputValidator.displayError("Failed to create adopter.");
            }

        } catch (Exception e) {
            InputValidator.displayError("An error occurred while adding adopter: " + e.getMessage());
        }
    }

    /**
     * Views all adopters
     */
    private void viewAllAdopters() {
        InputValidator.displayHeader(InputValidator.BLUE + INDENT + "ALL ADOPTERS" + InputValidator.RESET);

        try {
            List<Adopter> adopters = adopterCRUD.getAllAdopters();

            if (adopters.isEmpty()) {
                System.out.println(INDENT + "No adopters found.");
                return;
            }

            System.out.printf(INDENT + "%-5s %-25s %-15s %-30s%n",
                    "ID", "Name", "Contact", "Email");
            System.out.println("-".repeat(75));

            for (Adopter adopter : adopters) {
                System.out.printf(INDENT + "%-5d %-25s %-15s %-30s%n",
                        adopter.getAdopterId(),
                        adopter.getAdopterName(),
                        adopter.getAdopterContact(),
                        adopter.getAdopterEmail());
            }

        } catch (Exception e) {
            InputValidator.displayError("An error occurred while retrieving adopters: " + e.getMessage());
        }
    }

    /**
     * Searches for an adopter by ID
     */
    private void searchAdopterById() {
        InputValidator.displayHeader(InputValidator.BLUE + INDENT + "SEARCH ADOPTER BY ID" + InputValidator.RESET);

        try {
            int adopterId = InputValidator.getIntInput(INDENT + "Enter adopter ID: ");
            Adopter adopter = adopterCRUD.getAdopterById(adopterId);

            if (adopter != null) {
                displayAdopterDetails(adopter);
            } else {
                InputValidator.displayWarning("No adopter found with ID: " + adopterId);
            }

        } catch (Exception e) {
            InputValidator.displayError("An error occurred while searching adopter: " + e.getMessage());
        }
    }

    /**
     * Searches for adopters by name
     */
    private void searchAdopterByName() {
        InputValidator.displayHeader(InputValidator.BLUE + INDENT + "SEARCH ADOPTER BY NAME" + InputValidator.RESET);

        try {
            String searchTerm = InputValidator.getStringInput(INDENT + "Enter name to search: ", false);
            List<Adopter> adopters = adopterCRUD.searchAdoptersByName(searchTerm);

            if (adopters.isEmpty()) {
                InputValidator.displayWarning("No adopters found matching: " + searchTerm);
                return;
            }

            System.out.println(INDENT + "Search Results:");
            System.out.printf(INDENT + "%-5s %-25s %-15s %-30s%n",
                    "ID", "Name", "Contact", "Email");
            System.out.println(INDENT + "-".repeat(65));

            for (Adopter adopter : adopters) {
                System.out.printf(INDENT + "%-5d %-25s %-15s %-30s%n",
                        adopter.getAdopterId(),
                        adopter.getAdopterName(),
                        adopter.getAdopterContact(),
                        adopter.getAdopterEmail());
            }

        } catch (Exception e) {
            InputValidator.displayError("An error occurred while searching adopters: " + e.getMessage());
        }
    }

    /**
     * Updates an existing adopter
     */
    private void updateAdopter() {
        InputValidator.displayHeader(InputValidator.BLUE + INDENT + "UPDATE ADOPTER" + InputValidator.RESET);

        try {
            int adopterId = InputValidator.getIntInput(INDENT + "Enter adopter ID to update: ");
            Adopter adopter = adopterCRUD.getAdopterById(adopterId);

            if (adopter == null) {
                InputValidator.displayWarning("No adopter found with ID: " + adopterId);
                return;
            }

            System.out.println(INDENT + "Current adopter details:");
            displayAdopterDetails(adopter);

            System.out.println(INDENT + "\nEnter new information (press Enter to keep current value):");

            String name = InputValidator.getStringInput(INDENT + "Name [" + adopter.getAdopterName() + "]: ", true);
            if (!name.isEmpty()) {
                adopter.setAdopterName(name);
            }

            String contact = InputValidator.getStringInput(INDENT + "Contact [" + adopter.getAdopterContact() + "]: ", true);
            if (!contact.isEmpty()) {
                adopter.setAdopterContact(contact);
            }

            String email = InputValidator.getStringInput(INDENT + "Email [" + adopter.getAdopterEmail() + "]: ", true);
            if (!email.isEmpty()) {
                adopter.setAdopterEmail(email);
            }

            String address = InputValidator.getStringInput(INDENT + "Address [" + adopter.getAdopterAddress() + "]: ", true);
            if (!address.isEmpty()) {
                adopter.setAdopterAddress(address);
            }

            String profile = InputValidator.getStringInput(INDENT + "Profile [" + (adopter.getAdopterProfile() != null ? adopter.getAdopterProfile() : "N/A") + "]: ", true);
            if (!profile.isEmpty()) {
                adopter.setAdopterProfile(profile);
            }

            if (adopterCRUD.updateAdopter(adopter)) {
                InputValidator.displaySuccess("Adopter updated successfully!");
            } else {
                InputValidator.displayError("Failed to update adopter.");
            }

        } catch (Exception e) {
            InputValidator.displayError("An error occurred while updating adopter: " + e.getMessage());
        }
    }

    /**
     * Deletes an adopter
     */
    private void deleteAdopter() {
        InputValidator.displayHeader(InputValidator.BLUE + INDENT + "DELETE ADOPTER" + InputValidator.RESET);

        try {
            int adopterId = InputValidator.getIntInput(INDENT + "Enter adopter ID to delete: ");
            Adopter adopter = adopterCRUD.getAdopterById(adopterId);

            if (adopter == null) {
                InputValidator.displayWarning("No adopter found with ID: " + adopterId);
                return;
            }

            System.out.println(INDENT + "Adopter to be deleted:");
            displayAdopterDetails(adopter);

            if (InputValidator.getConfirmation(INDENT + "Are you sure you want to delete this adopter?")) {
                if (adopterCRUD.deleteAdopter(adopterId)) {
                    InputValidator.displaySuccess("Adopter deleted successfully!");
                } else {
                    InputValidator.displayError("Failed to delete adopter.");
                }
            } else {
                System.out.println(INDENT + "Deletion cancelled.");
            }

        } catch (Exception e) {
            InputValidator.displayError("An error occurred while deleting adopter: " + e.getMessage());
        }
    }

    /**
     * Views adopter statistics
     */
    private void viewAdopterStatistics() {
        InputValidator.displayHeader(InputValidator.BLUE + INDENT + "ADOPTER STATISTICS" + InputValidator.RESET);

        try {
            int totalAdopters = adopterCRUD.getAdopterCount();
            System.out.println(INDENT + "Total Adopters: " + totalAdopters);

            List<Adopter> allAdopters = adopterCRUD.getAllAdopters();

            long adoptersWithUsername = allAdopters.stream()
                    .filter(a -> a.getAdopterUsername() != null && !a.getAdopterUsername().isEmpty())
                    .count();

            System.out.println(INDENT + "Adopters with Account: " + adoptersWithUsername);
            System.out.println(INDENT + "Adopters without Account: " + (totalAdopters - adoptersWithUsername));

        } catch (Exception e) {
            InputValidator.displayError("An error occurred while retrieving statistics: " + e.getMessage());
        }
    }

    /**
     * Displays detailed information about an adopter
     *
     * @param adopter Adopter object to display
     */
    private void displayAdopterDetails(Adopter adopter) {
        System.out.println(INDENT + "\n" + "-".repeat(50));
        System.out.println(INDENT + "Adopter Details:");
        System.out.println(INDENT + "-".repeat(65));
        System.out.println(INDENT + "ID: " + adopter.getAdopterId());
        System.out.println(INDENT + "Name: " + adopter.getAdopterName());
        System.out.println(INDENT + "Contact: " + adopter.getAdopterContact());
        System.out.println(INDENT + "Email: " + adopter.getAdopterEmail());
        System.out.println(INDENT + "Address: " + adopter.getAdopterAddress());
        System.out.println(INDENT + "Profile: " + (adopter.getAdopterProfile() != null ? adopter.getAdopterProfile() : "N/A"));
        System.out.println(INDENT + "Username: " + (adopter.getAdopterUsername() != null ? adopter.getAdopterUsername() : "N/A"));
        System.out.println(INDENT + "-".repeat(65));
    }
}

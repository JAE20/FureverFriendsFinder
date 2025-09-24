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

/**
 * Dashboard for managing Adopter operations
 */
public class AdopterDashboard {
    
    private final AdopterCRUD adopterCRUD;
    
    public AdopterDashboard() {
        this.adopterCRUD = new AdopterCRUD();
    }
    
    /**
     * Displays the main adopter management menu
     */
    public void showAdopterMenu() {
        while (true) {
            InputValidator.displayHeader("ADOPTER MANAGEMENT DASHBOARD");
            System.out.println("1. Add New Adopter");
            System.out.println("2. View All Adopters");
            System.out.println("3. Search Adopter by ID");
            System.out.println("4. Search Adopter by Name");
            System.out.println("5. Update Adopter");
            System.out.println("6. Delete Adopter");
            System.out.println("7. View Adopter Statistics");
            System.out.println("8. Return to Main Menu");
            System.out.println("-".repeat(60));
            
            int choice = InputValidator.getIntInput("Enter your choice (1-8): ", 1, 8);
            
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
        InputValidator.displayHeader("ADD NEW ADOPTER");
        
        try {
            String name = InputValidator.getStringInput("Enter adopter name: ", false);
            String contact = InputValidator.getPhoneInput("Enter contact number: ");
            String email = InputValidator.getEmailInput("Enter email: ");
            String address = InputValidator.getStringInput("Enter address: ", false);
            String profile = InputValidator.getStringInput("Enter profile/bio (optional): ", true);
            
            String username = InputValidator.getStringInput("Enter username (optional): ", true);
            if (!username.isEmpty()) {
                // Check if username already exists
                if (adopterCRUD.getAdopterByUsername(username) != null) {
                    InputValidator.displayError("Username already exists. Please choose a different username.");
                    return;
                }
            }
            
            String password = "";
            if (!username.isEmpty()) {
                password = InputValidator.getStringInput("Enter password: ", 6, 255);
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
                System.out.println("Adopter ID: " + adopter.getAdopterId());
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
        InputValidator.displayHeader("ALL ADOPTERS");
        
        try {
            List<Adopter> adopters = adopterCRUD.getAllAdopters();
            
            if (adopters.isEmpty()) {
                System.out.println("No adopters found.");
                return;
            }
            
            System.out.printf("%-5s %-25s %-15s %-30s%n", 
                "ID", "Name", "Contact", "Email");
            System.out.println("-".repeat(75));
            
            for (Adopter adopter : adopters) {
                System.out.printf("%-5d %-25s %-15s %-30s%n",
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
        InputValidator.displayHeader("SEARCH ADOPTER BY ID");
        
        try {
            int adopterId = InputValidator.getIntInput("Enter adopter ID: ");
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
        InputValidator.displayHeader("SEARCH ADOPTER BY NAME");
        
        try {
            String searchTerm = InputValidator.getStringInput("Enter name to search: ", false);
            List<Adopter> adopters = adopterCRUD.searchAdoptersByName(searchTerm);
            
            if (adopters.isEmpty()) {
                InputValidator.displayWarning("No adopters found matching: " + searchTerm);
                return;
            }
            
            System.out.println("Search Results:");
            System.out.printf("%-5s %-25s %-15s %-30s%n", 
                "ID", "Name", "Contact", "Email");
            System.out.println("-".repeat(75));
            
            for (Adopter adopter : adopters) {
                System.out.printf("%-5d %-25s %-15s %-30s%n",
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
        InputValidator.displayHeader("UPDATE ADOPTER");
        
        try {
            int adopterId = InputValidator.getIntInput("Enter adopter ID to update: ");
            Adopter adopter = adopterCRUD.getAdopterById(adopterId);
            
            if (adopter == null) {
                InputValidator.displayWarning("No adopter found with ID: " + adopterId);
                return;
            }
            
            System.out.println("Current adopter details:");
            displayAdopterDetails(adopter);
            
            System.out.println("\nEnter new information (press Enter to keep current value):");
            
            String name = InputValidator.getStringInput("Name [" + adopter.getAdopterName() + "]: ", true);
            if (!name.isEmpty()) {
                adopter.setAdopterName(name);
            }
            
            String contact = InputValidator.getStringInput("Contact [" + adopter.getAdopterContact() + "]: ", true);
            if (!contact.isEmpty()) {
                adopter.setAdopterContact(contact);
            }
            
            String email = InputValidator.getStringInput("Email [" + adopter.getAdopterEmail() + "]: ", true);
            if (!email.isEmpty()) {
                adopter.setAdopterEmail(email);
            }
            
            String address = InputValidator.getStringInput("Address [" + adopter.getAdopterAddress() + "]: ", true);
            if (!address.isEmpty()) {
                adopter.setAdopterAddress(address);
            }
            
            String profile = InputValidator.getStringInput("Profile [" + (adopter.getAdopterProfile() != null ? adopter.getAdopterProfile() : "N/A") + "]: ", true);
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
        InputValidator.displayHeader("DELETE ADOPTER");
        
        try {
            int adopterId = InputValidator.getIntInput("Enter adopter ID to delete: ");
            Adopter adopter = adopterCRUD.getAdopterById(adopterId);
            
            if (adopter == null) {
                InputValidator.displayWarning("No adopter found with ID: " + adopterId);
                return;
            }
            
            System.out.println("Adopter to be deleted:");
            displayAdopterDetails(adopter);
            
            if (InputValidator.getConfirmation("Are you sure you want to delete this adopter?")) {
                if (adopterCRUD.deleteAdopter(adopterId)) {
                    InputValidator.displaySuccess("Adopter deleted successfully!");
                } else {
                    InputValidator.displayError("Failed to delete adopter.");
                }
            } else {
                System.out.println("Deletion cancelled.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while deleting adopter: " + e.getMessage());
        }
    }
    
    /**
     * Views adopter statistics
     */
    private void viewAdopterStatistics() {
        InputValidator.displayHeader("ADOPTER STATISTICS");
        
        try {
            int totalAdopters = adopterCRUD.getAdopterCount();
            System.out.println("Total Adopters: " + totalAdopters);
            
            List<Adopter> allAdopters = adopterCRUD.getAllAdopters();
            
            long adoptersWithUsername = allAdopters.stream()
                .filter(a -> a.getAdopterUsername() != null && !a.getAdopterUsername().isEmpty())
                .count();
            
            System.out.println("Adopters with Account: " + adoptersWithUsername);
            System.out.println("Adopters without Account: " + (totalAdopters - adoptersWithUsername));
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while retrieving statistics: " + e.getMessage());
        }
    }
    
    /**
     * Displays detailed information about an adopter
     * @param adopter Adopter object to display
     */
    private void displayAdopterDetails(Adopter adopter) {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("Adopter Details:");
        System.out.println("-".repeat(50));
        System.out.println("ID: " + adopter.getAdopterId());
        System.out.println("Name: " + adopter.getAdopterName());
        System.out.println("Contact: " + adopter.getAdopterContact());
        System.out.println("Email: " + adopter.getAdopterEmail());
        System.out.println("Address: " + adopter.getAdopterAddress());
        System.out.println("Profile: " + (adopter.getAdopterProfile() != null ? adopter.getAdopterProfile() : "N/A"));
        System.out.println("Username: " + (adopter.getAdopterUsername() != null ? adopter.getAdopterUsername() : "N/A"));
        System.out.println("-".repeat(50));
    }
}
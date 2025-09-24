/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.furever.dashboard;

/**
 *
 * @author jerimiahtongco
 */
import java.util.List;

import com.furever.crud.PetOwnerCRUD;
import com.furever.crud.UserCRUD;
import com.furever.models.PetOwner;
import com.furever.models.User;
import com.furever.utils.InputValidator;

/**
 * Dashboard for managing Pet Owner operations (Admin perspective)
 */
public class PetOwnerDashboard {
    
    private final PetOwnerCRUD petOwnerCRUD;
    private final UserCRUD userCRUD;
    
    public PetOwnerDashboard() {
        this.petOwnerCRUD = new PetOwnerCRUD();
        this.userCRUD = new UserCRUD();
    }
    
    /**
     * Displays the main pet owner management menu
     */
    public void showPetOwnerMenu() {
        while (true) {
            InputValidator.displayHeader("PET OWNER MANAGEMENT DASHBOARD");
            System.out.println("1. Add New Pet Owner");
            System.out.println("2. View All Pet Owners");
            System.out.println("3. Search Pet Owner by ID");
            System.out.println("4. Search Pet Owner by Name");
            System.out.println("5. Update Pet Owner");
            System.out.println("6. Delete Pet Owner");
            System.out.println("7. View Pet Owner Statistics");
            System.out.println("8. Return to Main Menu");
            System.out.println("-".repeat(60));
            
            int choice = InputValidator.getIntInput("Enter your choice (1-8): ", 1, 8);
            
            switch (choice) {
                case 1:
                    addPetOwner();
                    break;
                case 2:
                    viewAllPetOwners();
                    break;
                case 3:
                    searchPetOwnerById();
                    break;
                case 4:
                    searchPetOwnerByName();
                    break;
                case 5:
                    updatePetOwner();
                    break;
                case 6:
                    deletePetOwner();
                    break;
                case 7:
                    viewPetOwnerStatistics();
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
     * Adds a new pet owner
     */
    private void addPetOwner() {
        InputValidator.displayHeader("ADD NEW PET OWNER");
        
        try {
            System.out.println("Enter pet owner details:");
            
            String name = InputValidator.getStringInput("Name: ", false);
            String contact = InputValidator.getStringInput("Contact: ", true);
            String email = InputValidator.getStringInput("Email: ", false);
            String address = InputValidator.getStringInput("Address: ", true);
            String profile = InputValidator.getStringInput("Profile/Bio (optional): ", true);
            String username = InputValidator.getStringInput("Username: ", false);
            String password = InputValidator.getStringInput("Password: ", false);
            
            // Step 1: Create user account first (required for foreign key constraint)
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole("pet_owner");
            
            System.out.println("Creating user account...");
            boolean userCreated = userCRUD.createUser(user);
            
            if (!userCreated) {
                InputValidator.displayError("Failed to create user account. Username or email might already exist.");
                return;
            }
            
            // Step 2: Create pet owner profile linked to the user
            System.out.println("Creating pet owner profile...");
            boolean profileCreated = petOwnerCRUD.createPetOwnerProfileForUser(username, name, email);
            
            if (profileCreated) {
                // Update additional pet owner details if needed
                PetOwner petOwner = petOwnerCRUD.getPetOwnerByUsername(username);
                if (petOwner != null) {
                    petOwner.setPetOwnerContact(contact);
                    petOwner.setPetOwnerAddress(address);
                    petOwner.setPetOwnerProfile(profile);
                    petOwnerCRUD.updatePetOwner(petOwner);
                }
                
                InputValidator.displaySuccess("Pet owner added successfully!");
                System.out.println("User account created: " + username);
                System.out.println("Pet owner profile created for: " + name);
            } else {
                // Rollback: Delete the user if pet owner creation failed
                System.out.println("Pet owner profile creation failed. Rolling back user creation...");
                // Note: UserCRUD would need a deleteUser method for complete rollback
                InputValidator.displayError("Failed to create pet owner profile. User account may have been created but profile failed.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error adding pet owner: " + e.getMessage());
        }
    }
    
    /**
     * Views all pet owners
     */
    private void viewAllPetOwners() {
        InputValidator.displayHeader("ALL PET OWNERS");
        
        try {
            List<PetOwner> petOwners = petOwnerCRUD.getAllPetOwners();
            
            if (petOwners.isEmpty()) {
                System.out.println("No pet owners found.");
                return;
            }
            
            System.out.printf("%-5s %-25s %-15s %-30s %-25s%n", 
                "ID", "Name", "Contact", "Email", "Address");
            System.out.println("-".repeat(105));
            
            for (PetOwner petOwner : petOwners) {
                System.out.printf("%-5d %-25s %-15s %-30s %-25s%n",
                    petOwner.getPetOwnerId(),
                    truncateString(petOwner.getPetOwnerName(), 25),
                    truncateString(petOwner.getPetOwnerContact(), 15),
                    truncateString(petOwner.getPetOwnerEmail(), 30),
                    truncateString(petOwner.getPetOwnerAddress(), 25));
            }
            
            System.out.println("\nTotal pet owners: " + petOwners.size());
            
        } catch (Exception e) {
            InputValidator.displayError("Error retrieving pet owners: " + e.getMessage());
        }
    }
    
    /**
     * Searches for a pet owner by ID
     */
    private void searchPetOwnerById() {
        InputValidator.displayHeader("SEARCH PET OWNER BY ID");
        
        try {
            int petOwnerId = InputValidator.getIntInput("Enter Pet Owner ID: ", 1, Integer.MAX_VALUE);
            
            PetOwner petOwner = petOwnerCRUD.getPetOwnerById(petOwnerId);
            
            if (petOwner != null) {
                displayPetOwnerDetails(petOwner);
            } else {
                InputValidator.displayWarning("Pet owner with ID " + petOwnerId + " not found.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error searching pet owner: " + e.getMessage());
        }
    }
    
    /**
     * Searches for pet owners by name
     */
    private void searchPetOwnerByName() {
        InputValidator.displayHeader("SEARCH PET OWNERS BY NAME");
        
        try {
            String name = InputValidator.getStringInput("Enter name to search: ", false);
            
            List<PetOwner> petOwners = petOwnerCRUD.searchPetOwnersByName(name);
            
            if (petOwners.isEmpty()) {
                InputValidator.displayWarning("No pet owners found with name containing: " + name);
                return;
            }
            
            System.out.printf("%-5s %-25s %-15s %-30s%n", 
                "ID", "Name", "Contact", "Email");
            System.out.println("-".repeat(80));
            
            for (PetOwner petOwner : petOwners) {
                System.out.printf("%-5d %-25s %-15s %-30s%n",
                    petOwner.getPetOwnerId(),
                    truncateString(petOwner.getPetOwnerName(), 25),
                    truncateString(petOwner.getPetOwnerContact(), 15),
                    truncateString(petOwner.getPetOwnerEmail(), 30));
            }
            
            System.out.println("\nFound " + petOwners.size() + " pet owner(s).");
            
        } catch (Exception e) {
            InputValidator.displayError("Error searching pet owners: " + e.getMessage());
        }
    }
    
    /**
     * Updates an existing pet owner
     */
    private void updatePetOwner() {
        InputValidator.displayHeader("UPDATE PET OWNER");
        
        try {
            int petOwnerId = InputValidator.getIntInput("Enter Pet Owner ID to update: ", 1, Integer.MAX_VALUE);
            
            PetOwner petOwner = petOwnerCRUD.getPetOwnerById(petOwnerId);
            
            if (petOwner == null) {
                InputValidator.displayWarning("Pet owner with ID " + petOwnerId + " not found.");
                return;
            }
            
            System.out.println("Current Pet Owner Information:");
            displayPetOwnerDetails(petOwner);
            
            System.out.println("\nEnter new information (press Enter to keep current value):");
            
            String name = InputValidator.getStringInput("Name [" + petOwner.getPetOwnerName() + "]: ", true);
            if (!name.isEmpty()) {
                petOwner.setPetOwnerName(name);
            }
            
            String contact = InputValidator.getStringInput("Contact [" + petOwner.getPetOwnerContact() + "]: ", true);
            if (!contact.isEmpty()) {
                petOwner.setPetOwnerContact(contact);
            }
            
            String email = InputValidator.getStringInput("Email [" + petOwner.getPetOwnerEmail() + "]: ", true);
            if (!email.isEmpty()) {
                petOwner.setPetOwnerEmail(email);
            }
            
            String address = InputValidator.getStringInput("Address [" + petOwner.getPetOwnerAddress() + "]: ", true);
            if (!address.isEmpty()) {
                petOwner.setPetOwnerAddress(address);
            }
            
            String profile = InputValidator.getStringInput("Profile [" + petOwner.getPetOwnerProfile() + "]: ", true);
            if (!profile.isEmpty()) {
                petOwner.setPetOwnerProfile(profile);
            }
            
            if (petOwnerCRUD.updatePetOwner(petOwner)) {
                InputValidator.displaySuccess("Pet owner updated successfully!");
            } else {
                InputValidator.displayError("Failed to update pet owner.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error updating pet owner: " + e.getMessage());
        }
    }
    
    /**
     * Deletes a pet owner
     */
    private void deletePetOwner() {
        InputValidator.displayHeader("DELETE PET OWNER");
        
        try {
            int petOwnerId = InputValidator.getIntInput("Enter Pet Owner ID to delete: ", 1, Integer.MAX_VALUE);
            
            PetOwner petOwner = petOwnerCRUD.getPetOwnerById(petOwnerId);
            
            if (petOwner == null) {
                InputValidator.displayWarning("Pet owner with ID " + petOwnerId + " not found.");
                return;
            }
            
            System.out.println("Pet Owner to be deleted:");
            displayPetOwnerDetails(petOwner);
            
            String confirm = InputValidator.getStringInput("\nAre you sure you want to delete this pet owner? (yes/no): ", false);
            
            if ("yes".equalsIgnoreCase(confirm)) {
                if (petOwnerCRUD.deletePetOwner(petOwnerId)) {
                    InputValidator.displaySuccess("Pet owner deleted successfully!");
                } else {
                    InputValidator.displayError("Failed to delete pet owner.");
                }
            } else {
                System.out.println("Deletion cancelled.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error deleting pet owner: " + e.getMessage());
        }
    }
    
    /**
     * Views pet owner statistics
     */
    private void viewPetOwnerStatistics() {
        InputValidator.displayHeader("PET OWNER STATISTICS");
        
        try {
            int totalPetOwners = petOwnerCRUD.getPetOwnerCount();
            
            System.out.println("=== PET OWNER STATISTICS ===");
            System.out.println("Total Pet Owners: " + totalPetOwners);
            
            // TODO: Add more statistics like:
            // - Pet owners with most pets
            // - Pet owners with successful adoptions
            // - Active vs inactive pet owners
            
        } catch (Exception e) {
            InputValidator.displayError("Error retrieving statistics: " + e.getMessage());
        }
    }
    
    /**
     * Displays detailed information about a pet owner
     */
    private void displayPetOwnerDetails(PetOwner petOwner) {
        System.out.println("\n=== PET OWNER DETAILS ===");
        System.out.println("ID: " + petOwner.getPetOwnerId());
        System.out.println("Name: " + petOwner.getPetOwnerName());
        System.out.println("Contact: " + petOwner.getPetOwnerContact());
        System.out.println("Email: " + petOwner.getPetOwnerEmail());
        System.out.println("Address: " + petOwner.getPetOwnerAddress());
        System.out.println("Profile: " + petOwner.getPetOwnerProfile());
        System.out.println("Username: " + petOwner.getPetOwnerUsername());
    }
    
    /**
     * Helper method to truncate strings for display
     */
    private String truncateString(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
}
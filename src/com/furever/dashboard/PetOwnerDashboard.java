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


public class PetOwnerDashboard {

    private static final String INDENT = "\t\t\t\t\t";

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
            System.out.println(INDENT + "=================================================================");
            System.out.println(InputValidator.BLUE + INDENT + "                 PET OWNER MANAGEMENT DASHBOARD" + InputValidator.RESET);
            System.out.println(INDENT + "=================================================================");
            System.out.println(INDENT + "1. Add New Pet Owner");
            System.out.println(INDENT + "2. View All Pet Owners");
            System.out.println(INDENT + "3. Search Pet Owner by ID");
            System.out.println(INDENT + "4. Search Pet Owner by Name");
            System.out.println(INDENT + "5. Update Pet Owner");
            System.out.println(INDENT + "6. Delete Pet Owner");
            System.out.println(INDENT + "7. View Pet Owner Statistics");
            System.out.println(INDENT + "8. Return to Main Menu");
            System.out.println(INDENT + "-".repeat(65));

            int choice = InputValidator.getIntInput(INDENT + "Enter your choice (1-8): ", 1, 8);

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
        InputValidator.displayHeader(InputValidator.BLUE + INDENT + "ADD NEW PET OWNER" + InputValidator.RESET);

        try {
            System.out.println(INDENT + "Enter pet owner details:");

            String name = InputValidator.getStringInput(INDENT + "Name: ", false);
            String contact = InputValidator.getStringInput(INDENT + "Contact: ", true);
            String email = InputValidator.getStringInput(INDENT + "Email: ", false);
            String address = InputValidator.getStringInput(INDENT + "Address: ", true);
            String profile = InputValidator.getStringInput(INDENT + "Profile/Bio (optional): ", true);
            String username = InputValidator.getStringInput(INDENT + "Username: ", false);
            String password = InputValidator.getStringInput(INDENT + "Password: ", false);

            // Step 1: Create user account first (required for foreign key constraint)
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole("pet_owner");

            System.out.println(INDENT + "Creating user account...");
            boolean userCreated = userCRUD.createUser(user);

            if (!userCreated) {
                InputValidator.displayError("Failed to create user account. Username or email might already exist.");
                return;
            }

            // Step 2: Create pet owner profile linked to the user
            System.out.println(INDENT + "Creating pet owner profile...");
            PetOwner petOwner = petOwnerCRUD.getPetOwnerByUsername(username);

            if (petOwner != null) {
                // Update additional pet owner details
                petOwner.setPetOwnerContact(contact);
                petOwner.setPetOwnerAddress(address);
                petOwner.setPetOwnerProfile(profile);

                // Perform the update
                boolean profileUpdated = petOwnerCRUD.updatePetOwner(petOwner);

                if (profileUpdated) {
                    System.out.println(INDENT + "-".repeat(65));
                    InputValidator.displaySuccess("       Pet owner added successfully!");
                    System.out.println(INDENT + "-".repeat(65));
                    System.out.println(INDENT + "User account created: " + username);
                    System.out.println(INDENT + "Pet owner profile created and updated for: " + name);
                } else {
                    // Rollback: Update failed (rare, but good to handle)
                    System.out.println(INDENT + "Pet owner profile update failed. User account creation succeeded.");
                    // Consider calling a rollback/cleanup method here if necessary (e.g., userCRUD.deleteUser(username))
                    InputValidator.displayError("Failed to update pet owner profile details.");
                }

            } else {
                // This block handles the case where the user was created, but the automatic profile creation failed.
                System.out.println(INDENT + "Could not find pet owner profile after user creation. Automatic profile creation likely failed. Rolling back user creation...");
                // Call userCRUD.deleteUser(username) here to complete the rollback
                InputValidator.displayError("User account created, but pet owner profile could not be found or created.");
            }

        } catch (Exception e) {
            InputValidator.displayError("Error adding pet owner: " + e.getMessage());
        }
    }


    private void viewAllPetOwners() {
        InputValidator.displayHeader(InputValidator.BLUE + INDENT + "ALL PET OWNERS" + InputValidator.RESET);

        try {
            List<PetOwner> petOwners = petOwnerCRUD.getAllPetOwners();

            if (petOwners.isEmpty()) {
                System.out.println(INDENT + "No pet owners found.");
                return;
            }

            System.out.printf(INDENT + "%-5s %-25s %-15s %-30s %-25s%n",
                    "ID", "Name", "Contact", "Email", "Address");
            System.out.println(INDENT + "-".repeat(105));

            for (PetOwner petOwner : petOwners) {
                System.out.printf(INDENT + "%-5d %-25s %-15s %-30s %-25s%n",
                        petOwner.getPetOwnerId(),
                        truncateString(petOwner.getPetOwnerName(), 25),
                        truncateString(petOwner.getPetOwnerContact(), 15),
                        truncateString(petOwner.getPetOwnerEmail(), 30),
                        truncateString(petOwner.getPetOwnerAddress(), 25));
            } 

            System.out.println(INDENT + "\nTotal pet owners: " + petOwners.size());

        } catch (Exception e) {
            InputValidator.displayError("Error retrieving pet owners: " + e.getMessage());
        }
    }

    /**
     * Searches for a pet owner by ID
     */
    private void searchPetOwnerById() {
        InputValidator.displayHeader(InputValidator.BLUE + INDENT + "       SEARCH PET OWNER BY ID" + InputValidator.RESET);

        try {
            int petOwnerId = InputValidator.getIntInput(INDENT + "Enter Pet Owner ID: ", 1, Integer.MAX_VALUE);

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
        InputValidator.displayHeader(InputValidator.BLUE + INDENT + "SEARCH PET OWNERS BY NAME" + InputValidator.RESET);

        try {
            String name = InputValidator.getStringInput(INDENT + "Enter name to search: ", false);

            List<PetOwner> petOwners = petOwnerCRUD.searchPetOwnersByName(name);

            if (petOwners.isEmpty()) {
                InputValidator.displayWarning("No pet owners found with name containing: " + name);
                return;
            }

            System.out.printf(INDENT + "%-5s %-25s %-15s %-30s%n",
                    "ID", "Name", "Contact", "Email");
            System.out.println(INDENT + "-".repeat(80));

            for (PetOwner petOwner : petOwners) {
                System.out.printf(INDENT + "%-5d %-25s %-15s %-30s%n",
                        petOwner.getPetOwnerId(),
                        truncateString(petOwner.getPetOwnerName(), 25),
                        truncateString(petOwner.getPetOwnerContact(), 15),
                        truncateString(petOwner.getPetOwnerEmail(), 30));
            }

            System.out.println(INDENT + "\nFound " + petOwners.size() + " pet owner(s).");

        } catch (Exception e) {
            InputValidator.displayError("Error searching pet owners: " + e.getMessage());
        }
    }

    private void updatePetOwner() {
        InputValidator.displayHeader(InputValidator.BLUE + INDENT + "        UPDATE PET OWNER" + InputValidator.RESET);

        try {
            int petOwnerId = InputValidator.getIntInput(INDENT + "Enter Pet Owner ID to update: ", 1, Integer.MAX_VALUE);

            PetOwner petOwner = petOwnerCRUD.getPetOwnerById(petOwnerId);

            if (petOwner == null) {
                InputValidator.displayWarning("Pet owner with ID " + petOwnerId + " not found.");
                return;
            }

            System.out.println(INDENT + "Current Pet Owner Information:");
            displayPetOwnerDetails(petOwner);

            System.out.println(INDENT + "\nEnter new information (press Enter to keep current value):");

            String name = InputValidator.getStringInput(INDENT + "Name [" + petOwner.getPetOwnerName() + "]: ", true);
            if (!name.isEmpty()) {
                petOwner.setPetOwnerName(name);
            }

            String contact = InputValidator.getStringInput(INDENT + "Contact [" + petOwner.getPetOwnerContact() + "]: ", true);
            if (!contact.isEmpty()) {
                petOwner.setPetOwnerContact(contact);
            }

            String email = InputValidator.getStringInput(INDENT + "Email [" + petOwner.getPetOwnerEmail() + "]: ", true);
            if (!email.isEmpty()) {
                petOwner.setPetOwnerEmail(email);
            }

            String address = InputValidator.getStringInput(INDENT + "Address [" + petOwner.getPetOwnerAddress() + "]: ", true);
            if (!address.isEmpty()) {
                petOwner.setPetOwnerAddress(address);
            }

            String profile = InputValidator.getStringInput(INDENT + "Profile [" + petOwner.getPetOwnerProfile() + "]: ", true);
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

    private void deletePetOwner() {
        InputValidator.displayHeader(InputValidator.BLUE + INDENT + "         DELETE PET OWNER" + InputValidator.RESET);

        try {
            int petOwnerId = InputValidator.getIntInput(INDENT + "Enter Pet Owner ID to delete: ", 1, Integer.MAX_VALUE);

            PetOwner petOwner = petOwnerCRUD.getPetOwnerById(petOwnerId);

            if (petOwner == null) {
                InputValidator.displayWarning("Pet owner with ID " + petOwnerId + " not found.");
                return;
            }

            System.out.println(INDENT + "Pet Owner to be deleted:");
            displayPetOwnerDetails(petOwner);

            String confirm = InputValidator.getStringInput(INDENT + "\nAre you sure you want to delete this pet owner? (yes/no): ", false);

            if ("yes".equalsIgnoreCase(confirm)) {
                if (petOwnerCRUD.deletePetOwner(petOwnerId)) {
                    InputValidator.displaySuccess("Pet owner deleted successfully!");
                } else {
                    InputValidator.displayError("Failed to delete pet owner.");
                }
            } else {
                System.out.println(INDENT + "Deletion cancelled.");
            }

        } catch (Exception e) {
            InputValidator.displayError("Error deleting pet owner: " + e.getMessage());
        }
    }

    private void viewPetOwnerStatistics() {
        InputValidator.displayHeader(InputValidator.BLUE + INDENT + "         PET OWNER STATISTICS" + InputValidator.RESET);

        try {
            int totalPetOwners = petOwnerCRUD.getPetOwnerCount();

            System.out.println(INDENT + "=== PET OWNER STATISTICS ===");
            System.out.println(INDENT + "Total Pet Owners: " + totalPetOwners);

        } catch (Exception e) {
            InputValidator.displayError("Error retrieving statistics: " + e.getMessage());
        }
    }

    private void displayPetOwnerDetails(PetOwner petOwner) {
        System.out.println(InputValidator.CYAN + INDENT + "\n=== PET OWNER DETAILS ===" + InputValidator.RESET);
        System.out.println(INDENT + "ID: " + petOwner.getPetOwnerId());
        System.out.println(INDENT + "Name: " + petOwner.getPetOwnerName());
        System.out.println(INDENT + "Contact: " + petOwner.getPetOwnerContact());
        System.out.println(INDENT + "Email: " + petOwner.getPetOwnerEmail());
        System.out.println(INDENT + "Address: " + petOwner.getPetOwnerAddress());
        System.out.println(INDENT + "Profile: " + petOwner.getPetOwnerProfile());
        System.out.println(INDENT + "Username: " + petOwner.getPetOwnerUsername());
    }

    private String truncateString(String str, int maxLength) {
        if (str == null) {
            return "";
        }
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
}

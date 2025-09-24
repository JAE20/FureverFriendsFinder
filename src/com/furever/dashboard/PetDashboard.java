/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.furever.dashboard;

/**
 *
 * @author jerimiahtongco
 */
import com.furever.crud.PetCRUD;
import com.furever.utils.InputValidator;

public class PetDashboard {
    
    private final PetCRUD petCRUD;
    
    public PetDashboard() {
        this.petCRUD = new PetCRUD();
    }
    
    public void showPetMenu() {
        while (true) {
            InputValidator.displayHeader("PET MANAGEMENT DASHBOARD");
            System.out.println("1. Add New Pet");
            System.out.println("2. View All Pets");
            System.out.println("3. Search Pet by ID");
            System.out.println("4. Search Pet by Name");
            System.out.println("5. View Pets by Status");
            System.out.println("6. Archive Pet");
            System.out.println("7. View Archived Pets");
            System.out.println("8. Restore Archived Pet");
            System.out.println("9. Return to Main Menu");
            System.out.println("-".repeat(60));
            
            int choice = InputValidator.getIntInput("Enter your choice (1-9): ", 1, 9);
            
            switch (choice) {
                case 1:
                    InputValidator.displayWarning("Add Pet functionality - to be implemented");
                    break;
                case 2:
                    viewAllPets();
                    break;
                case 3:
                    searchPetById();
                    break;
                case 4:
                    searchPetByName();
                    break;
                case 5:
                    InputValidator.displayWarning("View Pets by Status functionality - to be implemented");
                    break;
                case 6:
                    archivePet();
                    break;
                case 7:
                    viewArchivedPets();
                    break;
                case 8:
                    restoreArchivedPet();
                    break;
                case 9:
                    return;
                default:
                    InputValidator.displayError("Invalid choice. Please try again.");
            }
            
            InputValidator.waitForEnter();
        }
    }
    
    private void viewAllPets() {
        InputValidator.displayHeader("ALL PETS");
        
        try {
            var pets = petCRUD.getAllPets();
            if (pets.isEmpty()) {
                InputValidator.displayWarning("No pets found in the system.");
            } else {
                petCRUD.displayPetsTable(pets);
            }
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while retrieving pets: " + e.getMessage());
        }
    }
    
    private void searchPetById() {
        InputValidator.displayHeader("SEARCH PET BY ID");
        
        try {
            int petId = InputValidator.getIntInput("Enter pet ID: ");
            petCRUD.displayPetDetails(petId);
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while searching pet: " + e.getMessage());
        }
    }
    
    private void searchPetByName() {
        InputValidator.displayHeader("SEARCH PET BY NAME");
        
        try {
            String searchTerm = InputValidator.getStringInput("Enter name to search: ", false);
            var pets = petCRUD.searchPetsByName(searchTerm);
            
            if (pets.isEmpty()) {
                InputValidator.displayWarning("No pets found matching: " + searchTerm);
            } else {
                petCRUD.displayPetsTable(pets);
            }
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while searching pets: " + e.getMessage());
        }
    }
    
    private void archivePet() {
        InputValidator.displayHeader("ARCHIVE PET");
        
        try {
            int petId = InputValidator.getIntInput("Enter pet ID to archive: ");
            
            if (petCRUD.archivePet(petId)) {
                InputValidator.displaySuccess("Pet archived successfully!");
            } else {
                InputValidator.displayError("Failed to archive pet or pet not found.");
            }
        } catch (Exception e) {
            InputValidator.displayError("Error archiving pet: " + e.getMessage());
        }
    }
    
    private void viewArchivedPets() {
        InputValidator.displayHeader("ARCHIVED PETS");
        
        try {
            var archivedPets = petCRUD.getArchivedPets();
            if (archivedPets.isEmpty()) {
                InputValidator.displayWarning("No archived pets found.");
            } else {
                petCRUD.displayPetsTable(archivedPets);
            }
        } catch (Exception e) {
            InputValidator.displayError("Error retrieving archived pets: " + e.getMessage());
        }
    }
    
    private void restoreArchivedPet() {
        InputValidator.displayHeader("RESTORE ARCHIVED PET");
        
        try {
            int petId = InputValidator.getIntInput("Enter pet ID to restore: ");
            
            if (petCRUD.restorePet(petId)) {
                InputValidator.displaySuccess("Pet restored successfully!");
            } else {
                InputValidator.displayError("Failed to restore pet or pet not found in archives.");
            }
        } catch (Exception e) {
            InputValidator.displayError("Error restoring pet: " + e.getMessage());
        }
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.furever.dashboard;

import com.furever.crud.PetCRUD;
import com.furever.crud.PetTypeCRUD;
import com.furever.models.Pet;
import com.furever.utils.InputValidator;

public class PetDashboard {

    private static final String INDENT = "\t\t\t\t\t";

    private final PetCRUD petCRUD;

    public PetDashboard() {
        this.petCRUD = new PetCRUD();
    }

    public void showPetMenu() {
        while (true) {
            System.out.println(INDENT + "=================================================================");
            System.out.println(InputValidator.BLUE + INDENT + "                 PET MANAGEMENT DASHBOARD" + InputValidator.RESET);
            System.out.println(INDENT + "=================================================================");
            System.out.println(INDENT + "1. Add New Pet");
            System.out.println(INDENT + "2. View All Pets");
            System.out.println(INDENT + "3. Search Pet by ID");
            System.out.println(INDENT + "4. Search Pet by Name");
            System.out.println(INDENT + "5. View Pets by Status");
            System.out.println(INDENT + "6. Archive Pet");
            System.out.println(INDENT + "7. View Archived Pets");
            System.out.println(INDENT + "8. Restore Archived Pet");
            System.out.println(INDENT + "9. Return to Main Menu");
            System.out.println(INDENT + "-".repeat(65));

            int choice = InputValidator.getIntInput(INDENT + "Enter your choice (1-9): ", 1, 9);

            switch (choice) {
                case 1:
                    addNewPet();
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
                    viewAllPets();
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

    private void addNewPet() {
        InputValidator.displayHeader(InputValidator.BLUE + INDENT + "ADD NEW PET" + InputValidator.RESET);

        try {
            System.out.println(INDENT + "Enter new pet details:");
            
            // Get Pet Name
            String petName = InputValidator.getStringInput(INDENT + "Pet Name: ", false);
            
            // Get Age
            int age = InputValidator.getIntInput(INDENT + "Age (years): ", 0, 30);
            
            // Get Gender
            String gender = InputValidator.getStringInput(INDENT + "Gender (Male/Female): ", false);
            
            // Health Status selection
            System.out.println(INDENT + "\nHealth Status Options:");
            System.out.println(INDENT + "1. Healthy");
            System.out.println(INDENT + "2. Needs Treatment");
            int healthChoice = InputValidator.getIntInput(INDENT + "Select health status (1-2): ", 1, 2);
            String healthStatus = healthChoice == 1 ? "Healthy" : "Needs Treatment";
            
            // Get Description
            String description = InputValidator.getStringInput(INDENT + "Description: ", true);
            
            // Get available pet types
            var petTypeCRUD = new PetTypeCRUD();
            var petTypes = petTypeCRUD.getAllPetTypes();
            
            if (petTypes.isEmpty()) {
                InputValidator.displayError("No pet types available. Please contact an administrator.");
                return;
            }
            
            System.out.println(INDENT + "\nAvailable Pet Types:");
            for (int i = 0; i < petTypes.size(); i++) {
                System.out.println(INDENT + (i + 1) + ". " + petTypes.get(i).getPetTypeName());
            }
            
            int typeChoice = InputValidator.getIntInput(INDENT + "Select pet type (1-" + petTypes.size() + "): ", 1, petTypes.size());
            int petTypeId = petTypes.get(typeChoice - 1).getPetTypeId();
            
            // Get Pet Owner ID (you might want to get this from a logged-in user session)
            System.out.println(INDENT + "\nOwner Information:");
            int petOwnerId = InputValidator.getIntInput(INDENT + "Pet Owner ID: ", 1, Integer.MAX_VALUE);
            
            // Vaccination Status
            System.out.println(INDENT + "\nVaccination Status:");
            System.out.println(INDENT + "1. Vaccinated");
            System.out.println(INDENT + "2. Not Vaccinated");
            int vaccinationChoice = InputValidator.getIntInput(INDENT + "Select vaccination status (1-2): ", 1, 3);
            String vaccinationStatus;
            switch (vaccinationChoice) {
                case 1:
                    vaccinationStatus = "Vaccinated";
                    break;
                case 2:
                    vaccinationStatus = "Not Vaccinated";
                    break;
                default:
                    vaccinationStatus = "Not Vaccinated";
            }
            
            // Optional: Upload health history
            String healthHistory = InputValidator.getStringInput(INDENT + "Health History Document (path/file or press Enter to skip): ", true);
            if (healthHistory.isEmpty()) {
                healthHistory = "Not Uploaded";
            }
            
            // Optional: Proof of vaccination
            String proofOfVaccination = InputValidator.getStringInput(INDENT + "Proof of Vaccination (path/file or press Enter to skip): ", true);
            if (proofOfVaccination.isEmpty()) {
                proofOfVaccination = "Not Uploaded";
            }
            
            // Create new pet object
            Pet newPet = new Pet();
            newPet.setPetName(petName);
            newPet.setAge(age);
            newPet.setGender(gender);
            newPet.setHealthStatus(healthStatus);
            newPet.setDescription(description);
            newPet.setPetTypeId(petTypeId);
            newPet.setPetOwnerId(petOwnerId);
            newPet.setVaccinationStatus(vaccinationStatus);
            newPet.setUploadHealthHistory(healthHistory);
            newPet.setProofOfVaccination(proofOfVaccination);
            newPet.setAdoptionStatus("Available"); // Default status for new pets
            
            // Save to database
            if (petCRUD.createPet(newPet)) {
                InputValidator.displaySuccess("Pet registered successfully!");
                System.out.println(INDENT + "Your pet '" + petName + "' has been added to the system and is available for adoption.");
                System.out.println(INDENT + "Pet ID: " + newPet.getPetId());
            } else {
                InputValidator.displayError("Failed to register pet. Please try again.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error registering pet: " + e.getMessage());
        }
    }

    
    private void viewAllPets() {
        InputValidator.displayHeader(InputValidator.BLUE + INDENT + "ALL PETS" + InputValidator.RESET);

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
        InputValidator.displayHeader(InputValidator.BLUE + INDENT + "SEARCH PET BY ID" + InputValidator.RESET);

        try {
            int petId = InputValidator.getIntInput(INDENT + "Enter pet ID: ");
            petCRUD.displayPetDetails(petId);
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while searching pet: " + e.getMessage());
        }
    }

    private void searchPetByName() {
        InputValidator.displayHeader(InputValidator.BLUE + INDENT + "SEARCH PET BY NAME" + InputValidator.RESET);

        try {
            String searchTerm = InputValidator.getStringInput(INDENT + "Enter name to search: ", false);
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
        InputValidator.displayHeader(InputValidator.BLUE + INDENT + "ARCHIVE PET" + InputValidator.RESET);

        try {
            int petId = InputValidator.getIntInput(INDENT + "Enter pet ID to archive: ");

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
        InputValidator.displayHeader(InputValidator.BLUE + "ARCHIVED PETS" + InputValidator.RESET);

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
        InputValidator.displayHeader(InputValidator.BLUE + INDENT + "RESTORE ARCHIVED PET" + InputValidator.RESET);

        try {
            int petId = InputValidator.getIntInput(INDENT + "Enter pet ID to restore: ");

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

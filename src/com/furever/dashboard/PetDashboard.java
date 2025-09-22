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
import com.furever.crud.PetTypeCRUD;
import com.furever.models.Pet;
import com.furever.models.PetType;
import com.furever.utils.InputValidator;
import java.sql.Date;
import java.util.List;

/**
 * Dashboard for managing Pet operations
 */
public class PetDashboard {
    
    private final PetCRUD petCRUD;
    private final PetTypeCRUD petTypeCRUD;
    
    public PetDashboard() {
        this.petCRUD = new PetCRUD();
        this.petTypeCRUD = new PetTypeCRUD();
    }
    
    /**
     * Displays the main pet management menu
     */
    public void showPetMenu() {
        while (true) {
            InputValidator.displayHeader("PET MANAGEMENT DASHBOARD");
            System.out.println("1. Add New Pet");
            System.out.println("2. View All Pets");
            System.out.println("3. Search Pet by ID");
            System.out.println("4. Search Pet by Name");
            System.out.println("5. View Pets by Status");
            System.out.println("6. View Pets by Type");
            System.out.println("7. Update Pet");
            System.out.println("8. Delete Pet");
            System.out.println("9. Update Pet Status");
            System.out.println("10. View Pet Statistics");
            System.out.println("11. Return to Main Menu");
            System.out.println("-".repeat(60));
            
            int choice = InputValidator.getIntInput("Enter your choice (1-11): ", 1, 11);
            
            switch (choice) {
                case 1:
                    addPet();
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
                    viewPetsByStatus();
                    break;
                case 6:
                    viewPetsByType();
                    break;
                case 7:
                    updatePet();
                    break;
                case 8:
                    deletePet();
                    break;
                case 9:
                    updatePetStatus();
                    break;
                case 10:
                    viewPetStatistics();
                    break;
                case 11:
                    return;
                default:
                    InputValidator.displayError("Invalid choice. Please try again.");
            }
            
            InputValidator.waitForEnter();
        }
    }
    
    /**
     * Adds a new pet
     */
    private void addPet() {
        InputValidator.displayHeader("ADD NEW PET");
        
        try {
            int ownerId = InputValidator.getIntInput("Enter pet owner ID: ");
            String name = InputValidator.getStringInput("Enter pet name: ", false);
            
            // Display available pet types
            List<PetType> petTypes = petTypeCRUD.getAllPetTypes();
            if (petTypes.isEmpty()) {
                InputValidator.displayError("No pet types available. Please add pet types first.");
                return;
            }
            
            System.out.println("\nAvailable Pet Types:");
            for (PetType type : petTypes) {
                System.out.println(type.getPetTypeId() + ". " + type.getPetTypeName());
            }
            
            int petTypeId = InputValidator.getIntInput("Enter pet type ID: ");
            String description = InputValidator.getStringInput("Enter description: ", false);
            int age = InputValidator.getIntInput("Enter age: ", 0, 30);
            
            System.out.println("\nSelect gender:");
            System.out.println("1. Male");
            System.out.println("2. Female");
            int genderChoice = InputValidator.getIntInput("Enter gender choice (1-2): ", 1, 2);
            String gender = (genderChoice == 1) ? "Male" : "Female";
            
            System.out.println("\nSelect health status:");
            System.out.println("1. Healthy");
            System.out.println("2. Needs Treatment");
            int healthChoice = InputValidator.getIntInput("Enter health status choice (1-2): ", 1, 2);
            String healthStatus = (healthChoice == 1) ? "Healthy" : "Needs Treatment";
            
            System.out.println("\nSelect vaccination status:");
            System.out.println("1. Vaccinated");
            System.out.println("2. Not Vaccinated");
            int vaccinationChoice = InputValidator.getIntInput("Enter vaccination status choice (1-2): ", 1, 2);
            String vaccinationStatus = (vaccinationChoice == 1) ? "Vaccinated" : "Not Vaccinated";
            
            String registrationDate = InputValidator.getDateInput("Enter registration date");
            
            Pet pet = new Pet();
            pet.setPetOwnerId(ownerId);
            pet.setPetName(name);
            pet.setPetTypeId(petTypeId);
            pet.setDescription(description);
            pet.setAge(age);
            pet.setGender(gender);
            pet.setHealthStatus(healthStatus);
            pet.setVaccinationStatus(vaccinationStatus);
            pet.setAdoptionStatus("Available");
            pet.setDateRegistered(Date.valueOf(registrationDate));
            
            if (petCRUD.createPet(pet)) {
                InputValidator.displaySuccess("Pet created successfully!");
                System.out.println("Pet ID: " + pet.getPetId());
            } else {
                InputValidator.displayError("Failed to create pet.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while adding pet: " + e.getMessage());
        }
    }
    
    /**
     * Views all pets
     */
    private void viewAllPets() {
        InputValidator.displayHeader("ALL PETS");
        
        try {
            List<Pet> pets = petCRUD.getAllPets();
            
            if (pets.isEmpty()) {
                System.out.println("No pets found.");
                return;
            }
            
            System.out.printf("%-5s %-20s %-8s %-8s %-10s %-12s %-15s%n", 
                "ID", "Name", "Type ID", "Age", "Gender", "Health", "Status");
            System.out.println("-".repeat(80));
            
            for (Pet pet : pets) {
                System.out.printf("%-5d %-20s %-8d %-8d %-10s %-12s %-15s%n",
                    pet.getPetId(),
                    pet.getPetName(),
                    pet.getPetTypeId(),
                    pet.getAge(),
                    pet.getGender(),
                    pet.getHealthStatus(),
                    pet.getAdoptionStatus());
            }
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while retrieving pets: " + e.getMessage());
        }
    }
    
    /**
     * Searches for a pet by ID
     */
    private void searchPetById() {
        InputValidator.displayHeader("SEARCH PET BY ID");
        
        try {
            int petId = InputValidator.getIntInput("Enter pet ID: ");
            Pet pet = petCRUD.getPetById(petId);
            
            if (pet != null) {
                displayPetDetails(pet);
            } else {
                InputValidator.displayWarning("No pet found with ID: " + petId);
            }
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while searching pet: " + e.getMessage());
        }
    }
    
    /**
     * Searches for pets by name
     */
    private void searchPetByName() {
        InputValidator.displayHeader("SEARCH PET BY NAME");
        
        try {
            String searchTerm = InputValidator.getStringInput("Enter name to search: ", false);
            List<Pet> pets = petCRUD.searchPetsByName(searchTerm);
            
            if (pets.isEmpty()) {
                InputValidator.displayWarning("No pets found matching: " + searchTerm);
                return;
            }
            
            System.out.println("Search Results:");
            System.out.printf("%-5s %-20s %-8s %-8s %-10s %-15s%n", 
                "ID", "Name", "Type ID", "Age", "Gender", "Status");
            System.out.println("-".repeat(70));
            
            for (Pet pet : pets) {
                System.out.printf("%-5d %-20s %-8d %-8d %-10s %-15s%n",
                    pet.getPetId(),
                    pet.getPetName(),
                    pet.getPetTypeId(),
                    pet.getAge(),
                    pet.getGender(),
                    pet.getAdoptionStatus());
            }
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while searching pets: " + e.getMessage());
        }
    }
    
    /**
     * Views pets by adoption status
     */
    private void viewPetsByStatus() {
        InputValidator.displayHeader("PETS BY ADOPTION STATUS");
        
        try {
            System.out.println("Select status:");
            System.out.println("1. Available");
            System.out.println("2. Pending");
            System.out.println("3. Adopted");
            
            int statusChoice = InputValidator.getIntInput("Enter status choice (1-3): ", 1, 3);
            String status;
            
            switch (statusChoice) {
                case 1:
                    status = "Available";
                    break;
                case 2:
                    status = "Pending";
                    break;
                case 3:
                    status = "Adopted";
                    break;
                default:
                    return;
            }
            
            List<Pet> pets = petCRUD.getPetsByAdoptionStatus(status);
            
            if (pets.isEmpty()) {
                System.out.println("No pets found with status: " + status);
                return;
            }
            
            System.out.println("Pets with status: " + status);
            System.out.printf("%-5s %-20s %-8s %-8s %-10s %-12s%n", 
                "ID", "Name", "Type ID", "Age", "Gender", "Health");
            System.out.println("-".repeat(65));
            
            for (Pet pet : pets) {
                System.out.printf("%-5d %-20s %-8d %-8d %-10s %-12s%n",
                    pet.getPetId(),
                    pet.getPetName(),
                    pet.getPetTypeId(),
                    pet.getAge(),
                    pet.getGender(),
                    pet.getHealthStatus());
            }
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while retrieving pets by status: " + e.getMessage());
        }
    }
    
    /**
     * Views pets by type
     */
    private void viewPetsByType() {
        InputValidator.displayHeader("PETS BY TYPE");
        
        try {
            List<PetType> petTypes = petTypeCRUD.getAllPetTypes();
            if (petTypes.isEmpty()) {
                InputValidator.displayError("No pet types available.");
                return;
            }
            
            System.out.println("Available Pet Types:");
            for (PetType type : petTypes) {
                System.out.println(type.getPetTypeId() + ". " + type.getPetTypeName());
            }
            
            int petTypeId = InputValidator.getIntInput("Enter pet type ID: ");
            List<Pet> pets = petCRUD.getPetsByType(petTypeId);
            
            if (pets.isEmpty()) {
                System.out.println("No pets found for the selected type.");
                return;
            }
            
            PetType selectedType = petTypeCRUD.getPetTypeById(petTypeId);
            System.out.println("Pets of type: " + (selectedType != null ? selectedType.getPetTypeName() : "Unknown"));
            System.out.printf("%-5s %-20s %-8s %-10s %-15s%n", 
                "ID", "Name", "Age", "Gender", "Status");
            System.out.println("-".repeat(60));
            
            for (Pet pet : pets) {
                System.out.printf("%-5d %-20s %-8d %-10s %-15s%n",
                    pet.getPetId(),
                    pet.getPetName(),
                    pet.getAge(),
                    pet.getGender(),
                    pet.getAdoptionStatus());
            }
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while retrieving pets by type: " + e.getMessage());
        }
    }
    
    /**
     * Updates an existing pet
     */
    private void updatePet() {
        InputValidator.displayHeader("UPDATE PET");
        
        try {
            int petId = InputValidator.getIntInput("Enter pet ID to update: ");
            Pet pet = petCRUD.getPetById(petId);
            
            if (pet == null) {
                InputValidator.displayWarning("No pet found with ID: " + petId);
                return;
            }
            
            System.out.println("Current pet details:");
            displayPetDetails(pet);
            
            System.out.println("\nEnter new information (press Enter to keep current value):");
            
            String name = InputValidator.getStringInput("Name [" + pet.getPetName() + "]: ", true);
            if (!name.isEmpty()) {
                pet.setPetName(name);
            }
            
            String description = InputValidator.getStringInput("Description [" + pet.getDescription() + "]: ", true);
            if (!description.isEmpty()) {
                pet.setDescription(description);
            }
            
            String ageStr = InputValidator.getStringInput("Age [" + pet.getAge() + "]: ", true);
            if (!ageStr.isEmpty()) {
                try {
                    pet.setAge(Integer.parseInt(ageStr));
                } catch (NumberFormatException e) {
                    InputValidator.displayWarning("Invalid age format. Keeping current age.");
                }
            }
            
            if (petCRUD.updatePet(pet)) {
                InputValidator.displaySuccess("Pet updated successfully!");
            } else {
                InputValidator.displayError("Failed to update pet.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while updating pet: " + e.getMessage());
        }
    }
    
    /**
     * Updates pet adoption status
     */
    private void updatePetStatus() {
        InputValidator.displayHeader("UPDATE PET STATUS");
        
        try {
            int petId = InputValidator.getIntInput("Enter pet ID: ");
            Pet pet = petCRUD.getPetById(petId);
            
            if (pet == null) {
                InputValidator.displayWarning("No pet found with ID: " + petId);
                return;
            }
            
            System.out.println("Current pet: " + pet.getPetName());
            System.out.println("Current status: " + pet.getAdoptionStatus());
            
            System.out.println("\nSelect new status:");
            System.out.println("1. Available");
            System.out.println("2. Pending");
            System.out.println("3. Adopted");
            
            int statusChoice = InputValidator.getIntInput("Enter status choice (1-3): ", 1, 3);
            String newStatus;
            
            switch (statusChoice) {
                case 1:
                    newStatus = "Available";
                    break;
                case 2:
                    newStatus = "Pending";
                    break;
                case 3:
                    newStatus = "Adopted";
                    break;
                default:
                    return;
            }
            
            pet.setAdoptionStatus(newStatus);
            
            if (petCRUD.updatePet(pet)) {
                InputValidator.displaySuccess("Pet status updated successfully!");
            } else {
                InputValidator.displayError("Failed to update pet status.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while updating pet status: " + e.getMessage());
        }
    }
    
    /**
     * Deletes a pet
     */
    private void deletePet() {
        InputValidator.displayHeader("DELETE PET");
        
        try {
            int petId = InputValidator.getIntInput("Enter pet ID to delete: ");
            Pet pet = petCRUD.getPetById(petId);
            
            if (pet == null) {
                InputValidator.displayWarning("No pet found with ID: " + petId);
                return;
            }
            
            System.out.println("Pet to be deleted:");
            displayPetDetails(pet);
            
            if (InputValidator.getConfirmation("Are you sure you want to delete this pet?")) {
                if (petCRUD.deletePet(petId)) {
                    InputValidator.displaySuccess("Pet deleted successfully!");
                } else {
                    InputValidator.displayError("Failed to delete pet.");
                }
            } else {
                System.out.println("Deletion cancelled.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while deleting pet: " + e.getMessage());
        }
    }
    
    /**
     * Views pet statistics
     */
    private void viewPetStatistics() {
        InputValidator.displayHeader("PET STATISTICS");
        
        try {
            int totalPets = petCRUD.getPetCount();
            System.out.println("Total Pets: " + totalPets);
            
            int availablePets = petCRUD.getPetCountByStatus("Available");
            int pendingPets = petCRUD.getPetCountByStatus("Pending");
            int adoptedPets = petCRUD.getPetCountByStatus("Adopted");
            
            System.out.println("Available Pets: " + availablePets);
            System.out.println("Pending Adoption: " + pendingPets);
            System.out.println("Adopted Pets: " + adoptedPets);
            
            // Pet type statistics
            List<PetType> petTypes = petTypeCRUD.getAllPetTypes();
            System.out.println("\nBy Pet Type:");
            for (PetType type : petTypes) {
                List<Pet> petsOfType = petCRUD.getPetsByType(type.getPetTypeId());
                System.out.println(type.getPetTypeName() + ": " + petsOfType.size());
            }
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while retrieving statistics: " + e.getMessage());
        }
    }
    
    /**
     * Displays detailed information about a pet
     * @param pet Pet object to display
     */
    private void displayPetDetails(Pet pet) {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("Pet Details:");
        System.out.println("-".repeat(50));
        System.out.println("ID: " + pet.getPetId());
        System.out.println("Name: " + pet.getPetName());
        System.out.println("Owner ID: " + pet.getPetOwnerId());
        System.out.println("Type ID: " + pet.getPetTypeId());
        System.out.println("Description: " + pet.getDescription());
        System.out.println("Age: " + pet.getAge());
        System.out.println("Gender: " + pet.getGender());
        System.out.println("Health Status: " + pet.getHealthStatus());
        System.out.println("Vaccination Status: " + pet.getVaccinationStatus());
        System.out.println("Adoption Status: " + pet.getAdoptionStatus());
        System.out.println("Date Registered: " + pet.getDateRegistered());
        System.out.println("-".repeat(50));
    }
}
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
import java.util.Map;

import com.furever.crud.AdopterCRUD;
import com.furever.crud.AdoptionRequestCRUD;
import com.furever.crud.ArchiveCRUD;
import com.furever.crud.PetCRUD;
import com.furever.crud.PetOwnerCRUD;
import com.furever.models.Adopter;
import com.furever.models.AdoptionRequest;
import com.furever.models.Pet;
import com.furever.models.PetOwner;
import com.furever.utils.InputValidator;

/**
 * Admin Dashboard for comprehensive archive management
 * Allows administrators to view, restore, and permanently delete archived records
 */
public class AdminArchiveDashboard {
    
    private final ArchiveCRUD archiveCRUD;
    private final AdoptionRequestCRUD adoptionRequestCRUD;
    private final PetCRUD petCRUD;
    private final AdopterCRUD adopterCRUD;
    private final PetOwnerCRUD petOwnerCRUD;
    
    public AdminArchiveDashboard() {
        this.archiveCRUD = new ArchiveCRUD();
        this.adoptionRequestCRUD = new AdoptionRequestCRUD();
        this.petCRUD = new PetCRUD();
        this.adopterCRUD = new AdopterCRUD();
        this.petOwnerCRUD = new PetOwnerCRUD();
    }
    
    /**
     * Displays the main archive management menu
     */
    public void showArchiveManagementMenu() {
        while (true) {
            InputValidator.displayHeader("ARCHIVE MANAGEMENT DASHBOARD");
            System.out.println("1. View Archive Statistics");
            System.out.println("2. View Archive Summary");
            System.out.println("3. Manage Archived Pets");
            System.out.println("4. Manage Archived Adoption Requests");
            System.out.println("5. Manage Archived Adopters");
            System.out.println("6. Manage Archived Pet Owners");
            System.out.println("7. View Recent Archive Operations");
            System.out.println("8. Auto-Archive Completed Records");
            System.out.println("9. Cleanup Archive Log");
            System.out.println("10. Return to Admin Menu");
            System.out.println("-".repeat(60));
            
            int choice = InputValidator.getIntInput("Enter your choice (1-10): ", 1, 10);
            
            switch (choice) {
                case 1:
                    viewArchiveStatistics();
                    break;
                case 2:
                    viewArchiveSummary();
                    break;
                case 3:
                    manageArchivedPets();
                    break;
                case 4:
                    manageArchivedAdoptionRequests();
                    break;
                case 5:
                    manageArchivedAdopters();
                    break;
                case 6:
                    manageArchivedPetOwners();
                    break;
                case 7:
                    viewRecentArchiveOperations();
                    break;
                case 8:
                    autoArchiveCompletedRecords();
                    break;
                case 9:
                    cleanupArchiveLog();
                    break;
                case 10:
                    return;
                default:
                    InputValidator.displayError("Invalid choice. Please try again.");
            }
            
            InputValidator.waitForEnter();
        }
    }
    
    /**
     * Displays archive statistics
     */
    private void viewArchiveStatistics() {
        InputValidator.displayHeader("ARCHIVE STATISTICS");
        
        try {
            Map<String, Integer> stats = archiveCRUD.getArchiveStatistics();
            
            System.out.println("Archive Record Counts:");
            System.out.println("-".repeat(40));
            System.out.printf("%-30s %10s%n", "Archive Table", "Count");
            System.out.println("-".repeat(40));
            
            System.out.printf("%-30s %10d%n", "Archived Pets", stats.get("tbl_pet_archive"));
            System.out.printf("%-30s %10d%n", "Archived Adoption Requests", stats.get("tbl_adoption_request_archive"));
            System.out.printf("%-30s %10d%n", "Archived Adopters", stats.get("tbl_adopter_archive"));
            System.out.printf("%-30s %10d%n", "Archived Pet Owners", stats.get("tbl_pet_owner_archive"));
            System.out.printf("%-30s %10d%n", "Archived Adoptions", stats.get("tbl_adoption_archive"));
            System.out.printf("%-30s %10d%n", "Archived Users", stats.get("users_archive"));
            
            System.out.println("-".repeat(40));
            int total = stats.values().stream().mapToInt(Integer::intValue).sum();
            System.out.printf("%-30s %10d%n", "Total Archived Records", total);
            
        } catch (Exception e) {
            InputValidator.displayError("Error retrieving archive statistics: " + e.getMessage());
        }
    }
    
    /**
     * Displays archive summary with date information
     */
    private void viewArchiveSummary() {
        InputValidator.displayHeader("ARCHIVE SUMMARY");
        
        try {
            List<ArchiveCRUD.ArchiveSummary> summaries = archiveCRUD.getArchiveSummary();
            
            System.out.println("Archive Summary with Date Information:");
            System.out.println("-".repeat(80));
            System.out.printf("%-20s %10s %-15s %-15s%n", 
                "Entity Type", "Count", "Oldest Record", "Newest Record");
            System.out.println("-".repeat(80));
            
            for (ArchiveCRUD.ArchiveSummary summary : summaries) {
                String oldest = summary.getOldestDate() != null ? 
                    summary.getOldestDate().toString().substring(0, 10) : "N/A";
                String newest = summary.getNewestDate() != null ? 
                    summary.getNewestDate().toString().substring(0, 10) : "N/A";
                    
                System.out.printf("%-20s %10d %-15s %-15s%n",
                    summary.getEntityName(), summary.getCount(), oldest, newest);
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error retrieving archive summary: " + e.getMessage());
        }
    }
    
    /**
     * Manages archived pets - view, restore, permanently delete
     */
    private void manageArchivedPets() {
        while (true) {
            InputValidator.displayHeader("MANAGE ARCHIVED PETS");
            System.out.println("1. View All Archived Pets");
            System.out.println("2. Restore Archived Pet");
            System.out.println("3. Permanently Delete Archived Pet");
            System.out.println("4. Return to Archive Management");
            System.out.println("-".repeat(50));
            
            int choice = InputValidator.getIntInput("Enter your choice (1-4): ", 1, 4);
            
            switch (choice) {
                case 1:
                    viewArchivedPets();
                    break;
                case 2:
                    restoreArchivedPet();
                    break;
                case 3:
                    permanentDeleteArchivedPet();
                    break;
                case 4:
                    return;
            }
            
            if (choice != 4) {
                InputValidator.waitForEnter();
            }
        }
    }
    
    /**
     * Manages archived adoption requests
     */
    private void manageArchivedAdoptionRequests() {
        while (true) {
            InputValidator.displayHeader("MANAGE ARCHIVED ADOPTION REQUESTS");
            System.out.println("1. View All Archived Requests");
            System.out.println("2. Restore Archived Request");
            System.out.println("3. Permanently Delete Archived Request");
            System.out.println("4. Return to Archive Management");
            System.out.println("-".repeat(50));
            
            int choice = InputValidator.getIntInput("Enter your choice (1-4): ", 1, 4);
            
            switch (choice) {
                case 1:
                    viewArchivedAdoptionRequests();
                    break;
                case 2:
                    restoreArchivedAdoptionRequest();
                    break;
                case 3:
                    permanentDeleteArchivedAdoptionRequest();
                    break;
                case 4:
                    return;
            }
            
            if (choice != 4) {
                InputValidator.waitForEnter();
            }
        }
    }
    
    /**
     * Views all archived pets
     */
    private void viewArchivedPets() {
        InputValidator.displayHeader("ARCHIVED PETS");
        
        try {
            List<Pet> archivedPets = petCRUD.getAllArchivedPets();
            
            if (archivedPets.isEmpty()) {
                InputValidator.displayWarning("No archived pets found.");
                return;
            }
            
            System.out.println("Found " + archivedPets.size() + " archived pets:");
            System.out.println("-".repeat(100));
            System.out.printf("%-5s %-20s %-10s %-10s %-15s %-20s%n", 
                "ID", "Name", "Age", "Gender", "Status", "Owner ID");
            System.out.println("-".repeat(100));
            
            for (Pet pet : archivedPets) {
                System.out.printf("%-5d %-20s %-10s %-10s %-15s %-20d%n",
                    pet.getPetId(),
                    pet.getPetName(),
                    pet.getAge(),
                    pet.getGender(),
                    pet.getAdoptionStatus(),
                    pet.getPetOwnerId()
                );
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error retrieving archived pets: " + e.getMessage());
        }
    }
    
    /**
     * Views all archived adoption requests
     */
    private void viewArchivedAdoptionRequests() {
        InputValidator.displayHeader("ARCHIVED ADOPTION REQUESTS");
        
        try {
            List<AdoptionRequest> archivedRequests = adoptionRequestCRUD.getAllArchivedAdoptionRequests();
            
            if (archivedRequests.isEmpty()) {
                InputValidator.displayWarning("No archived adoption requests found.");
                return;
            }
            
            System.out.println("Found " + archivedRequests.size() + " archived adoption requests:");
            System.out.println("-".repeat(100));
            System.out.printf("%-5s %-10s %-12s %-15s %-10s %-20s%n", 
                "ID", "Pet ID", "Adopter ID", "Status", "Date", "Remarks");
            System.out.println("-".repeat(100));
            
            for (AdoptionRequest request : archivedRequests) {
                System.out.printf("%-5d %-10d %-12d %-15s %-10s %-20s%n",
                    request.getAdoptionRequestId(),
                    request.getPetId(),
                    request.getAdopterId(),
                    request.getStatus(),
                    request.getRequestDate() != null ? request.getRequestDate().toString() : "N/A",
                    request.getRemarks() != null ? 
                        (request.getRemarks().length() > 18 ? 
                            request.getRemarks().substring(0, 18) + ".." : 
                            request.getRemarks()) : "N/A"
                );
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error retrieving archived adoption requests: " + e.getMessage());
        }
    }
    
    /**
     * Restores an archived pet
     */
    private void restoreArchivedPet() {
        InputValidator.displayHeader("RESTORE ARCHIVED PET");
        
        try {
            int petId = InputValidator.getIntInput("Enter pet ID to restore: ");
            String reason = InputValidator.getStringInput("Enter reason for restoration: ", false);
            
            if (petCRUD.restorePet(petId, 1, reason)) { // Using admin user ID 1
                InputValidator.displaySuccess("Pet restored successfully!");
            } else {
                InputValidator.displayError("Failed to restore pet. Pet may not exist in archives.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error restoring pet: " + e.getMessage());
        }
    }
    
    /**
     * Restores an archived adoption request
     */
    private void restoreArchivedAdoptionRequest() {
        InputValidator.displayHeader("RESTORE ARCHIVED ADOPTION REQUEST");
        
        try {
            int requestId = InputValidator.getIntInput("Enter adoption request ID to restore: ");
            String reason = InputValidator.getStringInput("Enter reason for restoration: ", false);
            
            if (adoptionRequestCRUD.restoreAdoptionRequest(requestId, 1, reason)) { // Using admin user ID 1
                InputValidator.displaySuccess("Adoption request restored successfully!");
            } else {
                InputValidator.displayError("Failed to restore adoption request. Request may not exist in archives.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error restoring adoption request: " + e.getMessage());
        }
    }
    
    /**
     * Permanently deletes an archived pet
     */
    private void permanentDeleteArchivedPet() {
        InputValidator.displayHeader("PERMANENTLY DELETE ARCHIVED PET");
        
        System.out.println("WARNING: This action cannot be undone!");
        System.out.println("The pet record will be completely removed from the system.");
        
        try {
            int petId = InputValidator.getIntInput("Enter pet ID to permanently delete: ");
            
            if (InputValidator.getConfirmation("Are you absolutely sure you want to permanently delete this pet record?")) {
                String reason = InputValidator.getStringInput("Enter reason for permanent deletion: ", false);
                
                if (petCRUD.permanentDeletePet(petId, 1, reason)) { // Using admin user ID 1
                    InputValidator.displaySuccess("Pet permanently deleted from archives.");
                } else {
                    InputValidator.displayError("Failed to permanently delete pet. Pet may not exist in archives.");
                }
            } else {
                System.out.println("Permanent deletion cancelled.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error permanently deleting pet: " + e.getMessage());
        }
    }
    
    /**
     * Permanently deletes an archived adoption request
     */
    private void permanentDeleteArchivedAdoptionRequest() {
        InputValidator.displayHeader("PERMANENTLY DELETE ARCHIVED ADOPTION REQUEST");
        
        System.out.println("WARNING: This action cannot be undone!");
        System.out.println("The adoption request record will be completely removed from the system.");
        
        try {
            int requestId = InputValidator.getIntInput("Enter adoption request ID to permanently delete: ");
            
            if (InputValidator.getConfirmation("Are you absolutely sure you want to permanently delete this adoption request record?")) {
                String reason = InputValidator.getStringInput("Enter reason for permanent deletion: ", false);
                
                if (adoptionRequestCRUD.permanentDeleteAdoptionRequest(requestId, 1, reason)) { // Using admin user ID 1
                    InputValidator.displaySuccess("Adoption request permanently deleted from archives.");
                } else {
                    InputValidator.displayError("Failed to permanently delete adoption request. Request may not exist in archives.");
                }
            } else {
                System.out.println("Permanent deletion cancelled.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error permanently deleting adoption request: " + e.getMessage());
        }
    }
    
    /**
     * Views recent archive operations
     */
    private void viewRecentArchiveOperations() {
        InputValidator.displayHeader("RECENT ARCHIVE OPERATIONS");
        
        try {
            int limit = InputValidator.getIntInput("Enter number of recent operations to view (1-50): ", 1, 50);
            List<ArchiveCRUD.ArchiveLogEntry> operations = archiveCRUD.getRecentArchiveOperations(limit);
            
            if (operations.isEmpty()) {
                InputValidator.displayWarning("No archive operations found.");
                return;
            }
            
            System.out.println("Recent Archive Operations:");
            System.out.println("-".repeat(100));
            System.out.printf("%-5s %-25s %-10s %-15s %-15s %-20s%n", 
                "ID", "Table", "Record ID", "Operation", "User ID", "Date");
            System.out.println("-".repeat(100));
            
            for (ArchiveCRUD.ArchiveLogEntry entry : operations) {
                System.out.printf("%-5d %-25s %-10d %-15s %-15d %-20s%n",
                    entry.getLogId(),
                    entry.getTableName(),
                    entry.getRecordId(),
                    entry.getOperation(),
                    entry.getPerformedByUserId(),
                    entry.getOperationDate().toString().substring(0, 16)
                );
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error retrieving archive operations: " + e.getMessage());
        }
    }
    
    /**
     * Auto-archives completed records
     */
    private void autoArchiveCompletedRecords() {
        InputValidator.displayHeader("AUTO-ARCHIVE COMPLETED RECORDS");
        
        try {
            System.out.println("This will automatically archive:");
            System.out.println("- Approved/Rejected adoption requests older than 30 days");
            System.out.println("- Adopted pets older than 60 days");
            
            if (InputValidator.getConfirmation("Do you want to proceed with auto-archiving?")) {
                int requestsArchived = archiveCRUD.autoArchiveCompletedAdoptionRequests(1); // Using admin user ID 1
                int petsArchived = archiveCRUD.autoArchiveAdoptedPets(1); // Using admin user ID 1
                
                InputValidator.displaySuccess("Auto-archiving completed!");
                System.out.println("Archived " + requestsArchived + " adoption requests");
                System.out.println("Archived " + petsArchived + " adopted pets");
            } else {
                System.out.println("Auto-archiving cancelled.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error during auto-archiving: " + e.getMessage());
        }
    }
    
    /**
     * Cleans up old archive log entries
     */
    private void cleanupArchiveLog() {
        InputValidator.displayHeader("CLEANUP ARCHIVE LOG");
        
        try {
            int daysToKeep = InputValidator.getIntInput("Enter number of days to keep log entries (30-365): ", 30, 365);
            
            if (InputValidator.getConfirmation("This will delete archive log entries older than " + daysToKeep + " days. Continue?")) {
                int deletedCount = archiveCRUD.cleanupArchiveLog(daysToKeep);
                InputValidator.displaySuccess("Cleaned up " + deletedCount + " old archive log entries.");
            } else {
                System.out.println("Archive log cleanup cancelled.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error cleaning up archive log: " + e.getMessage());
        }
    }
    
    /**
     * Manages archived adopters with view, restore, and delete options
     */
    private void manageArchivedAdopters() {
        while (true) {
            InputValidator.displayHeader("MANAGE ARCHIVED ADOPTERS");
            System.out.println("1. View All Archived Adopters");
            System.out.println("2. Restore Archived Adopter");
            System.out.println("3. Permanently Delete Archived Adopter");
            System.out.println("4. Return to Archive Management");
            System.out.println("-".repeat(50));
            
            int choice = InputValidator.getIntInput("Enter your choice (1-4): ", 1, 4);
            
            switch (choice) {
                case 1:
                    viewArchivedAdopters();
                    break;
                case 2:
                    restoreArchivedAdopter();
                    break;
                case 3:
                    permanentDeleteArchivedAdopter();
                    break;
                case 4:
                    return; // Exit the loop
                default:
                    InputValidator.displayError("Invalid choice. Please try again.");
            }
            
            InputValidator.waitForEnter();
        }
    }
    
    /**
     * Views all archived adopters
     */
    private void viewArchivedAdopters() {
        InputValidator.displayHeader("ARCHIVED ADOPTERS");
        
        try {
            List<Adopter> archivedAdopters = adopterCRUD.getAllArchivedAdopters();
            
            if (archivedAdopters.isEmpty()) {
                InputValidator.displayWarning("No archived adopters found.");
                return;
            }
            
            System.out.println("Found " + archivedAdopters.size() + " archived adopters:");
            System.out.println("-".repeat(120));
            System.out.printf("%-5s %-20s %-20s %-25s %-15s %-15s%n", 
                "ID", "Username", "Name", "Email", "Contact", "Address");
            System.out.println("-".repeat(120));
            
            for (Adopter adopter : archivedAdopters) {
                System.out.printf("%-5d %-20s %-20s %-25s %-15s %-15s%n",
                    adopter.getAdopterId(),
                    adopter.getAdopterUsername() != null ? adopter.getAdopterUsername() : "N/A",
                    adopter.getAdopterName() != null ? adopter.getAdopterName() : "N/A",
                    adopter.getAdopterEmail() != null ? adopter.getAdopterEmail() : "N/A",
                    adopter.getAdopterContact() != null ? adopter.getAdopterContact() : "N/A",
                    adopter.getAdopterAddress() != null ? 
                        (adopter.getAdopterAddress().length() > 13 ? 
                            adopter.getAdopterAddress().substring(0, 13) + ".." : 
                            adopter.getAdopterAddress()) : "N/A"
                );
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error retrieving archived adopters: " + e.getMessage());
        }
    }
    
    /**
     * Restores an archived adopter
     */
    private void restoreArchivedAdopter() {
        InputValidator.displayHeader("RESTORE ARCHIVED ADOPTER");
        
        try {
            int adopterId = InputValidator.getIntInput("Enter adopter ID to restore: ");
            String reason = InputValidator.getStringInput("Enter reason for restoration: ", false);
            
            if (adopterCRUD.restoreAdopter(adopterId, 1, reason)) { // Using admin user ID 1
                InputValidator.displaySuccess("Adopter restored successfully!");
            } else {
                InputValidator.displayError("Failed to restore adopter. Adopter may not exist in archives.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error restoring adopter: " + e.getMessage());
        }
    }
    
    /**
     * Permanently deletes an archived adopter
     */
    private void permanentDeleteArchivedAdopter() {
        InputValidator.displayHeader("PERMANENTLY DELETE ARCHIVED ADOPTER");
        
        System.out.println("WARNING: This action cannot be undone!");
        System.out.println("The adopter record will be completely removed from the system.");
        
        try {
            int adopterId = InputValidator.getIntInput("Enter adopter ID to permanently delete: ");
            
            if (InputValidator.getConfirmation("Are you absolutely sure you want to permanently delete this adopter record?")) {
                String reason = InputValidator.getStringInput("Enter reason for permanent deletion: ", false);
                
                if (adopterCRUD.permanentDeleteAdopter(adopterId, 1, reason)) { // Using admin user ID 1
                    InputValidator.displaySuccess("Adopter permanently deleted from archives.");
                } else {
                    InputValidator.displayError("Failed to permanently delete adopter. Adopter may not exist in archives.");
                }
            } else {
                System.out.println("Permanent deletion cancelled.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error permanently deleting adopter: " + e.getMessage());
        }
    }
    
    /**
     * Manages archived pet owners with view, restore, and delete options
     */
    private void manageArchivedPetOwners() {
        while (true) {
            InputValidator.displayHeader("MANAGE ARCHIVED PET OWNERS");
            System.out.println("1. View All Archived Pet Owners");
            System.out.println("2. Restore Archived Pet Owner");
            System.out.println("3. Permanently Delete Archived Pet Owner");
            System.out.println("4. Return to Archive Management");
            System.out.println("-".repeat(50));
            
            int choice = InputValidator.getIntInput("Enter your choice (1-4): ", 1, 4);
            
            switch (choice) {
                case 1:
                    viewArchivedPetOwners();
                    break;
                case 2:
                    restoreArchivedPetOwner();
                    break;
                case 3:
                    permanentDeleteArchivedPetOwner();
                    break;
                case 4:
                    return; // Exit the loop
                default:
                    InputValidator.displayError("Invalid choice. Please try again.");
            }
            
            InputValidator.waitForEnter();
        }
    }
    
    /**
     * Views all archived pet owners
     */
    private void viewArchivedPetOwners() {
        InputValidator.displayHeader("ARCHIVED PET OWNERS");
        
        try {
            List<PetOwner> archivedPetOwners = petOwnerCRUD.getAllArchivedPetOwners();
            
            if (archivedPetOwners.isEmpty()) {
                InputValidator.displayWarning("No archived pet owners found.");
                return;
            }
            
            System.out.println("Found " + archivedPetOwners.size() + " archived pet owners:");
            System.out.println("-".repeat(120));
            System.out.printf("%-5s %-20s %-20s %-25s %-15s %-15s%n", 
                "ID", "Username", "Name", "Email", "Contact", "Address");
            System.out.println("-".repeat(120));
            
            for (PetOwner petOwner : archivedPetOwners) {
                System.out.printf("%-5d %-20s %-20s %-25s %-15s %-15s%n",
                    petOwner.getPetOwnerId(),
                    petOwner.getPetOwnerUsername() != null ? petOwner.getPetOwnerUsername() : "N/A",
                    petOwner.getPetOwnerName() != null ? petOwner.getPetOwnerName() : "N/A",
                    petOwner.getPetOwnerEmail() != null ? petOwner.getPetOwnerEmail() : "N/A",
                    petOwner.getPetOwnerContact() != null ? petOwner.getPetOwnerContact() : "N/A",
                    petOwner.getPetOwnerAddress() != null ? 
                        (petOwner.getPetOwnerAddress().length() > 13 ? 
                            petOwner.getPetOwnerAddress().substring(0, 13) + ".." : 
                            petOwner.getPetOwnerAddress()) : "N/A"
                );
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error retrieving archived pet owners: " + e.getMessage());
        }
    }
    
    /**
     * Restores an archived pet owner
     */
    private void restoreArchivedPetOwner() {
        InputValidator.displayHeader("RESTORE ARCHIVED PET OWNER");
        
        try {
            int petOwnerId = InputValidator.getIntInput("Enter pet owner ID to restore: ");
            String reason = InputValidator.getStringInput("Enter reason for restoration: ", false);
            
            if (petOwnerCRUD.restorePetOwner(petOwnerId, 1, reason)) { // Using admin user ID 1
                InputValidator.displaySuccess("Pet owner restored successfully!");
            } else {
                InputValidator.displayError("Failed to restore pet owner. Pet owner may not exist in archives.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error restoring pet owner: " + e.getMessage());
        }
    }
    
    /**
     * Permanently deletes an archived pet owner
     */
    private void permanentDeleteArchivedPetOwner() {
        InputValidator.displayHeader("PERMANENTLY DELETE ARCHIVED PET OWNER");
        
        System.out.println("WARNING: This action cannot be undone!");
        System.out.println("The pet owner record will be completely removed from the system.");
        
        try {
            int petOwnerId = InputValidator.getIntInput("Enter pet owner ID to permanently delete: ");
            
            if (InputValidator.getConfirmation("Are you absolutely sure you want to permanently delete this pet owner record?")) {
                String reason = InputValidator.getStringInput("Enter reason for permanent deletion: ", false);
                
                if (petOwnerCRUD.permanentDeletePetOwner(petOwnerId, 1, reason)) { // Using admin user ID 1
                    InputValidator.displaySuccess("Pet owner permanently deleted from archives.");
                } else {
                    InputValidator.displayError("Failed to permanently delete pet owner. Pet owner may not exist in archives.");
                }
            } else {
                System.out.println("Permanent deletion cancelled.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error permanently deleting pet owner: " + e.getMessage());
        }
    }
}
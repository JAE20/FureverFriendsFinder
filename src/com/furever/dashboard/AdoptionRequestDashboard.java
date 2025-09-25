/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.furever.dashboard;

/**
 *
 * @author jerimiahtongco
 */
import java.sql.Date;
import java.util.List;

import com.furever.crud.AdopterCRUD;
import com.furever.crud.AdoptionRequestCRUD;
import com.furever.crud.PetCRUD;
import com.furever.models.Adopter;
import com.furever.models.AdoptionRequest;
import com.furever.models.Pet;
import com.furever.utils.InputValidator;

/**
 * Dashboard for managing AdoptionRequest operations
 */
public class AdoptionRequestDashboard {
    
    private final AdoptionRequestCRUD adoptionRequestCRUD;
    private final PetCRUD petCRUD;
    private final AdopterCRUD adopterCRUD;
    
    public AdoptionRequestDashboard() {
        this.adoptionRequestCRUD = new AdoptionRequestCRUD();
        this.petCRUD = new PetCRUD();
        this.adopterCRUD = new AdopterCRUD();
    }
    
    /**
     * Displays the main adoption request management menu
     */
    public void showAdoptionRequestMenu() {
        while (true) {
            InputValidator.displayHeader("ADOPTION REQUEST MANAGEMENT DASHBOARD");
            System.out.println("1. Create New Adoption Request");
            System.out.println("2. View All Adoption Requests");
            System.out.println("3. View Pending Requests");
            System.out.println("4. View Approved Requests");
            System.out.println("5. View Rejected Requests");
            System.out.println("6. Search Request by ID");
            System.out.println("7. View Requests by Adopter");
            System.out.println("8. View Requests for Pet");
            System.out.println("9. Approve Request");
            System.out.println("10. Reject Request");
            System.out.println("11. Update Request");
            System.out.println("12. Archive Request");
            System.out.println("13. View Statistics");
            System.out.println("14. Return to Main Menu");
            System.out.println("-".repeat(60));
            
            int choice = InputValidator.getIntInput("Enter your choice (1-14): ", 1, 14);
            
            switch (choice) {
                case 1:
                    createAdoptionRequest();
                    break;
                case 2:
                    viewAllAdoptionRequests();
                    break;
                case 3:
                    viewRequestsByStatus("Pending");
                    break;
                case 4:
                    viewRequestsByStatus("Approved");
                    break;
                case 5:
                    viewRequestsByStatus("Rejected");
                    break;
                case 6:
                    searchRequestById();
                    break;
                case 7:
                    viewRequestsByAdopter();
                    break;
                case 8:
                    viewRequestsForPet();
                    break;
                case 9:
                    approveRequest();
                    break;
                case 10:
                    rejectRequest();
                    break;
                case 11:
                    updateRequest();
                    break;
                case 12:
                    deleteRequest();
                    break;
                case 13:
                    viewStatistics();
                    break;
                case 14:
                    return;
                default:
                    InputValidator.displayError("Invalid choice. Please try again.");
            }
            
            InputValidator.waitForEnter();
        }
    }
    
    /**
     * Creates a new adoption request
     */
    private void createAdoptionRequest() {
        InputValidator.displayHeader("CREATE NEW ADOPTION REQUEST");
        
        try {
            int petId = InputValidator.getIntInput("Enter pet ID: ");
            Pet pet = petCRUD.getPetById(petId);
            
            if (pet == null) {
                InputValidator.displayError("Pet not found with ID: " + petId);
                return;
            }
            
            if (!"Available".equals(pet.getAdoptionStatus())) {
                InputValidator.displayWarning("Pet is not available for adoption. Current status: " + pet.getAdoptionStatus());
                return;
            }
            
            System.out.println("Pet: " + pet.getPetName() + " (ID: " + pet.getPetId() + ")");
            
            int adopterId = InputValidator.getIntInput("Enter adopter ID: ");
            Adopter adopter = adopterCRUD.getAdopterById(adopterId);
            
            if (adopter == null) {
                InputValidator.displayError("Adopter not found with ID: " + adopterId);
                return;
            }
            
            System.out.println("Adopter: " + adopter.getAdopterName() + " (ID: " + adopter.getAdopterId() + ")");
            
            String requestDate = InputValidator.getDateInput("Enter request date");
            String remarks = InputValidator.getStringInput("Enter remarks (optional): ", true);
            
            AdoptionRequest request = new AdoptionRequest();
            request.setPetId(petId);
            request.setAdopterId(adopterId);
            request.setRequestDate(Date.valueOf(requestDate));
            request.setStatus("Pending");
            request.setRemarks(remarks.isEmpty() ? null : remarks);
            
            if (adoptionRequestCRUD.createAdoptionRequest(request)) {
                InputValidator.displaySuccess("Adoption request created successfully!");
                System.out.println("Request ID: " + request.getAdoptionRequestId());
                
                // Update pet status to Pending
                pet.setAdoptionStatus("Pending");
                petCRUD.updatePet(pet);
                System.out.println("Pet status updated to Pending.");
            } else {
                InputValidator.displayError("Failed to create adoption request.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while creating adoption request: " + e.getMessage());
        }
    }
    
    /**
     * Views all adoption requests
     */
    private void viewAllAdoptionRequests() {
        InputValidator.displayHeader("ALL ADOPTION REQUESTS");
        
        try {
            List<AdoptionRequest> requests = adoptionRequestCRUD.getAllAdoptionRequests();
            
            if (requests.isEmpty()) {
                System.out.println("No adoption requests found.");
                return;
            }
            
            displayRequestsTable(requests);
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while retrieving adoption requests: " + e.getMessage());
        }
    }
    
    /**
     * Views adoption requests by status
     */
    private void viewRequestsByStatus(String status) {
        InputValidator.displayHeader("ADOPTION REQUESTS - " + status.toUpperCase());
        
        try {
            List<AdoptionRequest> requests = adoptionRequestCRUD.getAdoptionRequestsByStatus(status);
            
            if (requests.isEmpty()) {
                System.out.println("No " + status.toLowerCase() + " requests found.");
                return;
            }
            
            displayRequestsTable(requests);
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while retrieving requests: " + e.getMessage());
        }
    }
    
    /**
     * Searches for an adoption request by ID
     */
    private void searchRequestById() {
        InputValidator.displayHeader("SEARCH ADOPTION REQUEST BY ID");
        
        try {
            int requestId = InputValidator.getIntInput("Enter request ID: ");
            AdoptionRequest request = adoptionRequestCRUD.getAdoptionRequestById(requestId);
            
            if (request != null) {
                displayRequestDetails(request);
            } else {
                InputValidator.displayWarning("No adoption request found with ID: " + requestId);
            }
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while searching request: " + e.getMessage());
        }
    }
    
    /**
     * Views adoption requests by adopter
     */
    private void viewRequestsByAdopter() {
        InputValidator.displayHeader("ADOPTION REQUESTS BY ADOPTER");
        
        try {
            int adopterId = InputValidator.getIntInput("Enter adopter ID: ");
            Adopter adopter = adopterCRUD.getAdopterById(adopterId);
            
            if (adopter == null) {
                InputValidator.displayError("Adopter not found with ID: " + adopterId);
                return;
            }
            
            System.out.println("Requests by: " + adopter.getAdopterName());
            
            List<AdoptionRequest> requests = adoptionRequestCRUD.getAdoptionRequestsByAdopter(adopterId);
            
            if (requests.isEmpty()) {
                System.out.println("No requests found for this adopter.");
                return;
            }
            
            displayRequestsTable(requests);
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while retrieving requests: " + e.getMessage());
        }
    }
    
    /**
     * Views adoption requests for a specific pet
     */
    private void viewRequestsForPet() {
        InputValidator.displayHeader("ADOPTION REQUESTS FOR PET");
        
        try {
            int petId = InputValidator.getIntInput("Enter pet ID: ");
            Pet pet = petCRUD.getPetById(petId);
            
            if (pet == null) {
                InputValidator.displayError("Pet not found with ID: " + petId);
                return;
            }
            
            System.out.println("Requests for: " + pet.getPetName());
            
            List<AdoptionRequest> requests = adoptionRequestCRUD.getAdoptionRequestsByPet(petId);
            
            if (requests.isEmpty()) {
                System.out.println("No requests found for this pet.");
                return;
            }
            
            displayRequestsTable(requests);
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while retrieving requests: " + e.getMessage());
        }
    }
    
    /**
     * Approves an adoption request
     */
    private void approveRequest() {
        InputValidator.displayHeader("APPROVE ADOPTION REQUEST");
        
        try {
            int requestId = InputValidator.getIntInput("Enter request ID to approve: ");
            AdoptionRequest request = adoptionRequestCRUD.getAdoptionRequestById(requestId);
            
            if (request == null) {
                InputValidator.displayWarning("No adoption request found with ID: " + requestId);
                return;
            }
            
            if (!"Pending".equals(request.getStatus())) {
                InputValidator.displayWarning("Request is not pending. Current status: " + request.getStatus());
                return;
            }
            
            // Check if there's already an approved request for the same adopter and pet
            List<AdoptionRequest> existingApproved = adoptionRequestCRUD.getAdoptionRequestsByPet(request.getPetId());
            boolean hasApprovedRequest = existingApproved.stream()
                .anyMatch(r -> r.getAdopterId() == request.getAdopterId() && "Approved".equals(r.getStatus()));
            
            if (hasApprovedRequest) {
                InputValidator.displayWarning("This adopter already has an approved request for this pet.");
                System.out.println("Cannot approve multiple requests from the same adopter for the same pet.");
                System.out.println("You may want to reject this request instead.");
                return;
            }
            
            displayRequestDetails(request);
            
            if (!InputValidator.getConfirmation("Do you want to approve this request?")) {
                System.out.println("Approval cancelled.");
                return;
            }
            
            String approvalDate = InputValidator.getDateInput("Enter approval date");
            String remarks = InputValidator.getStringInput("Enter approval remarks: ", false);
            
            if (adoptionRequestCRUD.approveAdoptionRequest(requestId, Date.valueOf(approvalDate), remarks)) {
                InputValidator.displaySuccess("Adoption request approved successfully!");
                
                // Update pet status to Adopted
                Pet pet = petCRUD.getPetById(request.getPetId());
                if (pet != null) {
                    pet.setAdoptionStatus("Adopted");
                    petCRUD.updatePet(pet);
                    System.out.println("Pet status updated to Adopted.");
                }
                
                // Auto-reject other pending requests for the same pet
                List<AdoptionRequest> otherPendingRequests = adoptionRequestCRUD.getAdoptionRequestsByPet(request.getPetId());
                for (AdoptionRequest otherRequest : otherPendingRequests) {
                    if (otherRequest.getAdoptionRequestId() != requestId && "Pending".equals(otherRequest.getStatus())) {
                        adoptionRequestCRUD.rejectAdoptionRequest(otherRequest.getAdoptionRequestId(), 
                            "Automatically rejected - pet adopted by another adopter");
                    }
                }
                System.out.println("Other pending requests for this pet have been automatically rejected.");
                
            } else {
                InputValidator.displayError("Failed to approve adoption request.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while approving request: " + e.getMessage());
        }
    }
    
    /**
     * Rejects an adoption request
     */
    private void rejectRequest() {
        InputValidator.displayHeader("REJECT ADOPTION REQUEST");
        
        try {
            int requestId = InputValidator.getIntInput("Enter request ID to reject: ");
            AdoptionRequest request = adoptionRequestCRUD.getAdoptionRequestById(requestId);
            
            if (request == null) {
                InputValidator.displayWarning("No adoption request found with ID: " + requestId);
                return;
            }
            
            if (!"Pending".equals(request.getStatus())) {
                InputValidator.displayWarning("Request is not pending. Current status: " + request.getStatus());
                return;
            }
            
            displayRequestDetails(request);
            
            if (!InputValidator.getConfirmation("Do you want to reject this request?")) {
                System.out.println("Rejection cancelled.");
                return;
            }
            
            String remarks = InputValidator.getStringInput("Enter rejection reason: ", false);
            
            if (adoptionRequestCRUD.rejectAdoptionRequest(requestId, remarks)) {
                InputValidator.displaySuccess("Adoption request rejected successfully!");
                
                // Update pet status back to Available
                Pet pet = petCRUD.getPetById(request.getPetId());
                if (pet != null) {
                    pet.setAdoptionStatus("Available");
                    petCRUD.updatePet(pet);
                    System.out.println("Pet status updated to Available.");
                }
            } else {
                InputValidator.displayError("Failed to reject adoption request.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while rejecting request: " + e.getMessage());
        }
    }
    
    /**
     * Updates an adoption request
     */
    private void updateRequest() {
        InputValidator.displayHeader("UPDATE ADOPTION REQUEST");
        
        try {
            int requestId = InputValidator.getIntInput("Enter request ID to update: ");
            AdoptionRequest request = adoptionRequestCRUD.getAdoptionRequestById(requestId);
            
            if (request == null) {
                InputValidator.displayWarning("No adoption request found with ID: " + requestId);
                return;
            }
            
            System.out.println("Current request details:");
            displayRequestDetails(request);
            
            System.out.println("\nEnter new information (press Enter to keep current value):");
            
            String remarks = InputValidator.getStringInput("Remarks [" + (request.getRemarks() != null ? request.getRemarks() : "N/A") + "]: ", true);
            if (!remarks.isEmpty()) {
                request.setRemarks(remarks);
            }
            
            if (adoptionRequestCRUD.updateAdoptionRequest(request)) {
                InputValidator.displaySuccess("Adoption request updated successfully!");
            } else {
                InputValidator.displayError("Failed to update adoption request.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while updating request: " + e.getMessage());
        }
    }
    
    /**
     * Deletes an adoption request
     */
    private void deleteRequest() {
        InputValidator.displayHeader("ARCHIVE ADOPTION REQUEST");
        
        try {
            int requestId = InputValidator.getIntInput("Enter request ID to archive: ");
            AdoptionRequest request = adoptionRequestCRUD.getAdoptionRequestById(requestId);
            
            if (request == null) {
                InputValidator.displayWarning("No adoption request found with ID: " + requestId);
                return;
            }
            
            System.out.println("Request to be archived:");
            displayRequestDetails(request);
            
            System.out.println("\nNote: Archiving will move this request to archive storage where it can be restored later if needed.");
            
            if (InputValidator.getConfirmation("Are you sure you want to archive this request?")) {
                String reason = InputValidator.getStringInput("Enter reason for archiving (optional): ", true);
                if (reason.isEmpty()) {
                    reason = "Manual archive via dashboard";
                }
                
                // For now, we'll use null for user ID - in a full implementation, 
                // this would come from the current logged-in user
                if (adoptionRequestCRUD.archiveAdoptionRequest(requestId, null, reason)) {
                    InputValidator.displaySuccess("Adoption request archived successfully!");
                    System.out.println("The request has been moved to archive storage and can be restored by an administrator if needed.");
                } else {
                    InputValidator.displayError("Failed to archive adoption request.");
                }
            } else {
                System.out.println("Archive operation cancelled.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while archiving request: " + e.getMessage());
        }
    }
    
    /**
     * Views adoption request statistics
     */
    private void viewStatistics() {
        InputValidator.displayHeader("ADOPTION REQUEST STATISTICS");
        
        try {
            int totalRequests = adoptionRequestCRUD.getAdoptionRequestCount();
            int pendingRequests = adoptionRequestCRUD.getAdoptionRequestCountByStatus("Pending");
            int approvedRequests = adoptionRequestCRUD.getAdoptionRequestCountByStatus("Approved");
            int rejectedRequests = adoptionRequestCRUD.getAdoptionRequestCountByStatus("Rejected");
            
            System.out.println("Total Requests: " + totalRequests);
            System.out.println("Pending Requests: " + pendingRequests);
            System.out.println("Approved Requests: " + approvedRequests);
            System.out.println("Rejected Requests: " + rejectedRequests);
            
            if (totalRequests > 0) {
                double approvalRate = (double) approvedRequests / totalRequests * 100;
                double rejectionRate = (double) rejectedRequests / totalRequests * 100;
                
                System.out.printf("Approval Rate: %.1f%%%n", approvalRate);
                System.out.printf("Rejection Rate: %.1f%%%n", rejectionRate);
            }
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while retrieving statistics: " + e.getMessage());
        }
    }
    
    /**
     * Displays adoption requests in table format
     */
    private void displayRequestsTable(List<AdoptionRequest> requests) {
        System.out.printf("%-5s %-20s %-20s %-12s %-10s %-12s%n", 
            "ID", "Pet Name", "Adopter Name", "Date", "Status", "Approval");
        System.out.println("-".repeat(85));
        
        for (AdoptionRequest request : requests) {
            // Fetch pet and adopter details for better display
            String petName = "Unknown";
            String adopterName = "Unknown";
            
            try {
                Pet pet = petCRUD.getPetById(request.getPetId());
                if (pet != null) {
                    petName = pet.getPetName();
                }
                
                Adopter adopter = adopterCRUD.getAdopterById(request.getAdopterId());
                if (adopter != null) {
                    adopterName = adopter.getAdopterName();
                }
            } catch (Exception e) {
                // If there's an error fetching names, we'll use "Unknown"
                System.err.println("Error fetching pet/adopter details: " + e.getMessage());
            }
            
            System.out.printf("%-5d %-20s %-20s %-12s %-10s %-12s%n",
                request.getAdoptionRequestId(),
                petName.length() > 19 ? petName.substring(0, 16) + "..." : petName,
                adopterName.length() > 19 ? adopterName.substring(0, 16) + "..." : adopterName,
                request.getRequestDate() != null ? request.getRequestDate().toString() : "N/A",
                request.getStatus(),
                request.getApprovalDate() != null ? request.getApprovalDate().toString() : "N/A");
        }
    }
    
    /**
     * Displays detailed information about an adoption request
     */
    private void displayRequestDetails(AdoptionRequest request) {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("Adoption Request Details:");
        System.out.println("-".repeat(50));
        System.out.println("Request ID: " + request.getAdoptionRequestId());
        System.out.println("Pet ID: " + request.getPetId());
        System.out.println("Adopter ID: " + request.getAdopterId());
        System.out.println("Request Date: " + request.getRequestDate());
        System.out.println("Status: " + request.getStatus());
        System.out.println("Approval Date: " + (request.getApprovalDate() != null ? request.getApprovalDate().toString() : "N/A"));
        System.out.println("Remarks: " + (request.getRemarks() != null ? request.getRemarks() : "N/A"));
        System.out.println("User ID: " + (request.getUserId() != null ? request.getUserId() : "N/A"));
        
        // Display pet and adopter details
        try {
            Pet pet = petCRUD.getPetById(request.getPetId());
            if (pet != null) {
                System.out.println("\nPet: " + pet.getPetName() + " (" + pet.getAdoptionStatus() + ")");
            }
            
            Adopter adopter = adopterCRUD.getAdopterById(request.getAdopterId());
            if (adopter != null) {
                System.out.println("Adopter: " + adopter.getAdopterName() + " (" + adopter.getAdopterEmail() + ")");
            }
        } catch (Exception e) {
            // Ignore errors in fetching additional details
        }
        
        System.out.println("-".repeat(50));
    }
}
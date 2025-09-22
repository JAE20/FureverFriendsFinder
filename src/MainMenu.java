/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jerimiahtongco
 */
import com.furever.crud.UserCRUD;
import com.furever.dashboard.AdopterDashboard;
import com.furever.dashboard.AdoptionRequestDashboard;
import com.furever.dashboard.PetDashboard;
import com.furever.dashboard.UserDashboard;
import com.furever.database.DbConnection;
import com.furever.models.User;
import com.furever.utils.InputValidator;

/**
 * Main Menu class - Entry point for the Pet Adoption System
 */
public class MainMenu {
    
    private final UserCRUD userCRUD;
    private final UserDashboard userDashboard;
    private final AdopterDashboard adopterDashboard;
    private final PetDashboard petDashboard;
    private final AdoptionRequestDashboard adoptionRequestDashboard;
    
    private User currentUser;
    
    public MainMenu() {
        this.userCRUD = new UserCRUD();
        this.userDashboard = new UserDashboard();
        this.adopterDashboard = new AdopterDashboard();
        this.petDashboard = new PetDashboard();
        this.adoptionRequestDashboard = new AdoptionRequestDashboard();
    }
    
    /**
     * Main entry point of the application
     */
    public static void main(String[] args) {
        MainMenu mainMenu = new MainMenu();
        mainMenu.startApplication();
    }
    
    /**
     * Starts the Pet Adoption System application
     */
    public void startApplication() {
        try {
            // Test database connection
            if (!DbConnection.testConnection()) {
                InputValidator.displayError("Failed to connect to database. Please check your database configuration.");
                return;
            }
            
            InputValidator.displayHeader("WELCOME TO FUREVER PET ADOPTION SYSTEM");
            System.out.println("System initialized successfully!");
            System.out.println("Database connection established.");
            
            // Show login screen
            showLoginMenu();
            
        } catch (Exception e) {
            InputValidator.displayError("Failed to start application: " + e.getMessage());
        } finally {
            // Close database connection
            DbConnection.closeConnection();
            System.out.println("Thank you for using Furever Pet Adoption System!");
        }
    }
    
    /**
     * Displays the login menu
     */
    private void showLoginMenu() {
        while (true) {
            InputValidator.displayHeader("LOGIN TO FUREVER SYSTEM");
            System.out.println("1. Admin Login");
            System.out.println("2. Adopter Login");
            System.out.println("3. Guest Access (Limited Features)");
            System.out.println("4. Exit System");
            System.out.println("-".repeat(60));
            
            int choice = InputValidator.getIntInput("Enter your choice (1-4): ", 1, 4);
            
            switch (choice) {
                case 1:
                    if (adminLogin()) {
                        showMainMenu();
                    }
                    break;
                case 2:
                    if (adopterLogin()) {
                        showAdopterMenu();
                    }
                    break;
                case 3:
                    showGuestMenu();
                    break;
                case 4:
                    return;
                default:
                    InputValidator.displayError("Invalid choice. Please try again.");
            }
        }
    }
    
    /**
     * Handles admin login
     */
    private boolean adminLogin() {
        InputValidator.displayHeader("ADMIN LOGIN");
        
        try {
            String username = InputValidator.getStringInput("Username: ", false);
            String password = InputValidator.getStringInput("Password: ", false);
            
            User user = userCRUD.authenticateUser(username, password);
            
            if (user != null && "admin".equals(user.getRole())) {
                currentUser = user;
                InputValidator.displaySuccess("Login successful! Welcome, " + user.getUsername());
                return true;
            } else {
                InputValidator.displayError("Invalid credentials or insufficient privileges.");
                return false;
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Login error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Handles adopter login
     */
    private boolean adopterLogin() {
        InputValidator.displayHeader("ADOPTER LOGIN");
        
        try {
            String username = InputValidator.getStringInput("Username: ", false);
            String password = InputValidator.getStringInput("Password: ", false);
            
            User user = userCRUD.authenticateUser(username, password);
            
            if (user != null && "adopter".equals(user.getRole())) {
                currentUser = user;
                InputValidator.displaySuccess("Login successful! Welcome, " + user.getUsername());
                return true;
            } else {
                InputValidator.displayError("Invalid credentials or insufficient privileges.");
                return false;
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Login error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Displays the main menu for authenticated admin users
     */
    private void showMainMenu() {
        while (true) {
            InputValidator.displayHeader("FUREVER PET ADOPTION SYSTEM - MAIN MENU");
            System.out.println("Logged in as: " + currentUser.getUsername() + " (Admin)");
            System.out.println();
            System.out.println("1. User Management");
            System.out.println("2. Adopter Management");
            System.out.println("3. Pet Management");
            System.out.println("4. Adoption Request Management");
            System.out.println("5. System Statistics");
            System.out.println("6. Database Status");
            System.out.println("7. Logout");
            System.out.println("-".repeat(60));
            
            int choice = InputValidator.getIntInput("Enter your choice (1-7): ", 1, 7);
            
            switch (choice) {
                case 1:
                    userDashboard.showUserMenu();
                    break;
                case 2:
                    adopterDashboard.showAdopterMenu();
                    break;
                case 3:
                    petDashboard.showPetMenu();
                    break;
                case 4:
                    adoptionRequestDashboard.showAdoptionRequestMenu();
                    break;
                case 5:
                    showSystemStatistics();
                    break;
                case 6:
                    showDatabaseStatus();
                    break;
                case 7:
                    currentUser = null;
                    InputValidator.displaySuccess("Logged out successfully.");
                    return;
                default:
                    InputValidator.displayError("Invalid choice. Please try again.");
            }
        }
    }
    
    /**
     * Displays the main menu for authenticated adopter users
     */
    private void showAdopterMenu() {
        while (true) {
            InputValidator.displayHeader("FUREVER PET ADOPTION SYSTEM - ADOPTER MENU");
            System.out.println("Logged in as: " + currentUser.getUsername() + " (Adopter)");
            System.out.println();
            System.out.println("1. View Available Pets");
            System.out.println("2. Submit Adoption Request");
            System.out.println("3. View My Adoption Requests");
            System.out.println("4. Update My Profile");
            System.out.println("5. View Pet Statistics");
            System.out.println("6. Logout");
            System.out.println("-".repeat(60));
            
            int choice = InputValidator.getIntInput("Enter your choice (1-6): ", 1, 6);
            
            switch (choice) {
                case 1:
                    showAvailablePets();
                    break;
                case 2:
                    submitAdoptionRequest();
                    break;
                case 3:
                    viewMyAdoptionRequests();
                    break;
                case 4:
                    updateMyProfile();
                    break;
                case 5:
                    showGuestPetStatistics();
                    break;
                case 6:
                    currentUser = null;
                    InputValidator.displaySuccess("Logged out successfully.");
                    return;
                default:
                    InputValidator.displayError("Invalid choice. Please try again.");
            }
            
            InputValidator.waitForEnter();
        }
    }
    
    /**
     * Displays the guest menu with limited features
     */
    private void showGuestMenu() {
        while (true) {
            InputValidator.displayHeader("FUREVER PET ADOPTION SYSTEM - GUEST ACCESS");
            System.out.println("Limited access - View only features");
            System.out.println();
            System.out.println("1. View Available Pets");
            System.out.println("2. View Pet Statistics");
            System.out.println("3. View Adopters");
            System.out.println("4. Return to Login Menu");
            System.out.println("-".repeat(60));
            
            int choice = InputValidator.getIntInput("Enter your choice (1-4): ", 1, 4);
            
            switch (choice) {
                case 1:
                    showAvailablePets();
                    break;
                case 2:
                    showGuestPetStatistics();
                    break;
                case 3:
                    showGuestAdopters();
                    break;
                case 4:
                    return;
                default:
                    InputValidator.displayError("Invalid choice. Please try again.");
            }
            
            InputValidator.waitForEnter();
        }
    }
    
    /**
     * Shows available pets for guest users
     */
    private void showAvailablePets() {
        InputValidator.displayHeader("AVAILABLE PETS FOR ADOPTION");
        
        try {
            var petCRUD = new com.furever.crud.PetCRUD();
            var availablePets = petCRUD.getPetsByAdoptionStatus("Available");
            
            if (availablePets.isEmpty()) {
                System.out.println("No pets currently available for adoption.");
                return;
            }
            
            System.out.printf("%-5s %-20s %-8s %-10s %-12s%n", 
                "ID", "Name", "Age", "Gender", "Health");
            System.out.println("-".repeat(60));
            
            for (var pet : availablePets) {
                System.out.printf("%-5d %-20s %-8d %-10s %-12s%n",
                    pet.getPetId(),
                    pet.getPetName(),
                    pet.getAge(),
                    pet.getGender(),
                    pet.getHealthStatus());
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error retrieving pets: " + e.getMessage());
        }
    }
    
    /**
     * Shows pet statistics for guest users
     */
    private void showGuestPetStatistics() {
        InputValidator.displayHeader("PET ADOPTION STATISTICS");
        
        try {
            var petCRUD = new com.furever.crud.PetCRUD();
            
            int totalPets = petCRUD.getPetCount();
            int availablePets = petCRUD.getPetCountByStatus("Available");
            int adoptedPets = petCRUD.getPetCountByStatus("Adopted");
            
            System.out.println("Total Pets in System: " + totalPets);
            System.out.println("Available for Adoption: " + availablePets);
            System.out.println("Successfully Adopted: " + adoptedPets);
            
            if (totalPets > 0) {
                double adoptionRate = (double) adoptedPets / totalPets * 100;
                System.out.printf("Adoption Success Rate: %.1f%%%n", adoptionRate);
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error retrieving statistics: " + e.getMessage());
        }
    }
    
    /**
     * Shows adopters for guest users
     */
    private void showGuestAdopters() {
        InputValidator.displayHeader("REGISTERED ADOPTERS");
        
        try {
            var adopterCRUD = new com.furever.crud.AdopterCRUD();
            var adopters = adopterCRUD.getAllAdopters();
            
            if (adopters.isEmpty()) {
                System.out.println("No adopters registered yet.");
                return;
            }
            
            System.out.printf("%-5s %-25s %-30s%n", "ID", "Name", "Email");
            System.out.println("-".repeat(65));
            
            for (var adopter : adopters) {
                System.out.printf("%-5d %-25s %-30s%n",
                    adopter.getAdopterId(),
                    adopter.getAdopterName(),
                    adopter.getAdopterEmail());
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error retrieving adopters: " + e.getMessage());
        }
    }
    
    /**
     * Shows system-wide statistics
     */
    private void showSystemStatistics() {
        InputValidator.displayHeader("SYSTEM STATISTICS");
        
        try {
            var petCRUD = new com.furever.crud.PetCRUD();
            var adopterCRUD = new com.furever.crud.AdopterCRUD();
            var requestCRUD = new com.furever.crud.AdoptionRequestCRUD();
            
            System.out.println("=== PETS ===");
            System.out.println("Total Pets: " + petCRUD.getPetCount());
            System.out.println("Available: " + petCRUD.getPetCountByStatus("Available"));
            System.out.println("Pending: " + petCRUD.getPetCountByStatus("Pending"));
            System.out.println("Adopted: " + petCRUD.getPetCountByStatus("Adopted"));
            
            System.out.println("\n=== ADOPTERS ===");
            System.out.println("Total Adopters: " + adopterCRUD.getAdopterCount());
            
            System.out.println("\n=== USERS ===");
            System.out.println("Total Users: " + userCRUD.getUserCount());
            
            System.out.println("\n=== ADOPTION REQUESTS ===");
            System.out.println("Total Requests: " + requestCRUD.getAdoptionRequestCount());
            System.out.println("Pending: " + requestCRUD.getAdoptionRequestCountByStatus("Pending"));
            System.out.println("Approved: " + requestCRUD.getAdoptionRequestCountByStatus("Approved"));
            System.out.println("Rejected: " + requestCRUD.getAdoptionRequestCountByStatus("Rejected"));
            
        } catch (Exception e) {
            InputValidator.displayError("Error retrieving system statistics: " + e.getMessage());
        }
        
        InputValidator.waitForEnter();
    }
    
    /**
     * Shows database connection status
     */
    private void showDatabaseStatus() {
        InputValidator.displayHeader("DATABASE STATUS");
        
        try {
            if (DbConnection.testConnection()) {
                InputValidator.displaySuccess("Database connection is active.");
                System.out.println("Database URL: " + DbConnection.getDatabaseUrl());
                System.out.println("Database User: " + DbConnection.getDatabaseUsername());
            } else {
                InputValidator.displayError("Database connection failed.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error checking database status: " + e.getMessage());
        }
        
        InputValidator.waitForEnter();
    }
    
    /**
     * Allows adopter to submit an adoption request
     */
    private void submitAdoptionRequest() {
        InputValidator.displayHeader("SUBMIT ADOPTION REQUEST");
        
        try {
            // First check if adopter profile exists
            var adopterCRUD = new com.furever.crud.AdopterCRUD();
            var adopter = adopterCRUD.getAdopterByUsername(currentUser.getUsername());
            
            if (adopter == null) {
                InputValidator.displayWarning("No adopter profile found for your account.");
                System.out.println("Please contact an administrator to create your adopter profile.");
                return;
            }
            
            // Show available pets
            var petCRUD = new com.furever.crud.PetCRUD();
            var availablePets = petCRUD.getPetsByAdoptionStatus("Available");
            
            if (availablePets.isEmpty()) {
                InputValidator.displayWarning("No pets currently available for adoption.");
                return;
            }
            
            System.out.println("Available Pets:");
            System.out.printf("%-5s %-20s %-8s %-10s %-12s%n", 
                "ID", "Name", "Age", "Gender", "Health");
            System.out.println("-".repeat(60));
            
            for (var pet : availablePets) {
                System.out.printf("%-5d %-20s %-8d %-10s %-12s%n",
                    pet.getPetId(),
                    pet.getPetName(),
                    pet.getAge(),
                    pet.getGender(),
                    pet.getHealthStatus());
            }
            
            // Get pet selection from user
            System.out.println();
            int petId = InputValidator.getIntInput("Enter the Pet ID you want to adopt (0 to cancel): ", 0, Integer.MAX_VALUE);
            
            if (petId == 0) {
                System.out.println("Adoption request cancelled.");
                return;
            }
            
            // Verify the pet exists and is available
            var selectedPet = petCRUD.getPetById(petId);
            if (selectedPet == null) {
                InputValidator.displayError("Pet with ID " + petId + " not found.");
                return;
            }
            
            if (!"Available".equals(selectedPet.getAdoptionStatus())) {
                InputValidator.displayError("Pet is not available for adoption.");
                return;
            }
            
            // Create adoption request
            var requestCRUD = new com.furever.crud.AdoptionRequestCRUD();
            var adoptionRequest = new com.furever.models.AdoptionRequest();
            
            adoptionRequest.setAdopterId(adopter.getAdopterId());
            adoptionRequest.setPetId(petId);
            adoptionRequest.setStatus("Pending");
            adoptionRequest.setRequestDate(new java.sql.Date(System.currentTimeMillis()));
            
            // Get additional information from adopter
            System.out.println("\nPlease provide additional information for your adoption request:");
            String reason = InputValidator.getStringInput("Why do you want to adopt " + selectedPet.getPetName() + "? ", false);
            String experience = InputValidator.getStringInput("Do you have experience with pets? ", false);
            String livingCondition = InputValidator.getStringInput("Describe your living conditions: ", false);
            
            // Combine additional info (you might want to add these fields to the AdoptionRequest model)
            String additionalInfo = String.format("Reason: %s\nExperience: %s\nLiving Conditions: %s", 
                reason, experience, livingCondition);
            
            // For now, we'll just show confirmation since the model might not have additional fields
            if (requestCRUD.createAdoptionRequest(adoptionRequest)) {
                InputValidator.displaySuccess("Adoption request submitted successfully!");
                System.out.println("Pet: " + selectedPet.getPetName() + " (ID: " + petId + ")");
                System.out.println("Status: Pending");
                System.out.println("Your request will be reviewed by an administrator.");
                System.out.println("\nAdditional Information Provided:");
                System.out.println(additionalInfo);
            } else {
                InputValidator.displayError("Failed to submit adoption request. Please try again.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error submitting adoption request: " + e.getMessage());
        }
    }
    
    /**
     * Shows adopter's adoption requests
     */
    private void viewMyAdoptionRequests() {
        InputValidator.displayHeader("MY ADOPTION REQUESTS");
        
        try {
            // First need to find the adopter record associated with this user
            var adopterCRUD = new com.furever.crud.AdopterCRUD();
            var adopter = adopterCRUD.getAdopterByUsername(currentUser.getUsername());
            
            if (adopter == null) {
                InputValidator.displayWarning("No adopter profile found for your account.");
                System.out.println("Please contact an administrator to create your adopter profile.");
                return;
            }
            
            var requestCRUD = new com.furever.crud.AdoptionRequestCRUD();
            var requests = requestCRUD.getAdoptionRequestsByAdopter(adopter.getAdopterId());
            
            if (requests.isEmpty()) {
                System.out.println("You have no adoption requests yet.");
                return;
            }
            
            System.out.printf("%-5s %-8s %-12s %-10s %-12s%n", 
                "ID", "Pet ID", "Date", "Status", "Approval");
            System.out.println("-".repeat(50));
            
            for (var request : requests) {
                System.out.printf("%-5d %-8d %-12s %-10s %-12s%n",
                    request.getAdoptionRequestId(),
                    request.getPetId(),
                    request.getRequestDate() != null ? request.getRequestDate().toString() : "N/A",
                    request.getStatus(),
                    request.getApprovalDate() != null ? request.getApprovalDate().toString() : "N/A");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error retrieving your adoption requests: " + e.getMessage());
        }
    }
    
    /**
     * Allows adopter to update their profile
     */
    private void updateMyProfile() {
        InputValidator.displayHeader("UPDATE MY PROFILE");
        
        try {
            // First need to find the adopter record associated with this user
            var adopterCRUD = new com.furever.crud.AdopterCRUD();
            var adopter = adopterCRUD.getAdopterByUsername(currentUser.getUsername());
            
            if (adopter == null) {
                InputValidator.displayWarning("No adopter profile found for your account.");
                System.out.println("Please contact an administrator to create your adopter profile.");
                return;
            }
            
            System.out.println("Current Profile Information:");
            System.out.println("Name: " + adopter.getAdopterName());
            System.out.println("Contact: " + adopter.getAdopterContact());
            System.out.println("Email: " + adopter.getAdopterEmail());
            System.out.println("Address: " + adopter.getAdopterAddress());
            
            System.out.println("\nEnter new information (press Enter to keep current value):");
            
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
            
            if (adopterCRUD.updateAdopter(adopter)) {
                InputValidator.displaySuccess("Profile updated successfully!");
            } else {
                InputValidator.displayError("Failed to update profile.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error updating profile: " + e.getMessage());
        }
    }
}
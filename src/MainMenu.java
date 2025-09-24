/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jerimiahtongco
 */
import com.furever.crud.PetOwnerCRUD;
import com.furever.crud.UserCRUD;
import com.furever.dashboard.AdopterDashboard;
import com.furever.dashboard.AdoptionRequestDashboard;
import com.furever.dashboard.PetDashboard;
import com.furever.dashboard.PetOwnerDashboard;
import com.furever.dashboard.UserDashboard;
import com.furever.database.DbConnection;
import com.furever.models.PetOwner;
import com.furever.models.User;
import com.furever.utils.InputValidator;

/**
 * Main Menu class - Entry point for the Pet Adoption System
 */
public class MainMenu {
    
    private final UserCRUD userCRUD;
    private final PetOwnerCRUD petOwnerCRUD;
    private final UserDashboard userDashboard;
    private final AdopterDashboard adopterDashboard;
    private final PetDashboard petDashboard;
    private final PetOwnerDashboard petOwnerDashboard;
    private final AdoptionRequestDashboard adoptionRequestDashboard;
    
    private User currentUser;
    
    public MainMenu() {
        this.userCRUD = new UserCRUD();
        this.petOwnerCRUD = new PetOwnerCRUD();
        this.userDashboard = new UserDashboard();
        this.adopterDashboard = new AdopterDashboard();
        this.petDashboard = new PetDashboard();
        this.petOwnerDashboard = new PetOwnerDashboard();
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
            System.out.println("3. Pet Owner Login");
            System.out.println("4. Guest Access (Limited Features)");
            System.out.println("5. Exit System");
            System.out.println("-".repeat(60));
            
            int choice = InputValidator.getIntInput("Enter your choice (1-5): ", 1, 5);
            
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
                    if (petOwnerLogin()) {
                        showPetOwnerMenu();
                    }
                    break;
                case 4:
                    showGuestMenu();
                    break;
                case 5:
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
            
            // Debug: Check if user exists
            User userCheck = userCRUD.getUserByUsername(username);
            if (userCheck == null) {
                InputValidator.displayError("User '" + username + "' not found.");
                return false;
            }
            
            // Debug: Check role
            if (!"adopter".equals(userCheck.getRole())) {
                InputValidator.displayError("User '" + username + "' is not an adopter. Role: " + userCheck.getRole());
                return false;
            }
            
            User user = userCRUD.authenticateUser(username, password);
            
            if (user != null && "adopter".equals(user.getRole())) {
                currentUser = user;
                
                // Check if adopter profile exists, create if not
                var adopterCRUD = new com.furever.crud.AdopterCRUD();
                var adopter = adopterCRUD.getAdopterByLinkedUsername(username);
                
                if (adopter == null) {
                    InputValidator.displayWarning("No adopter profile found for your account.");
                    System.out.println("Creating adopter profile...");
                    
                    // Create adopter profile automatically
                    boolean profileCreated = adopterCRUD.createAdopterProfileForUser(
                        username, 
                        username, // Use username as display name initially
                        "Not provided", 
                        user.getEmail(), 
                        "Not provided"
                    );
                    
                    if (profileCreated) {
                        InputValidator.displaySuccess("Adopter profile created successfully!");
                    } else {
                        InputValidator.displayError("Failed to create adopter profile. Please contact an administrator.");
                        return false;
                    }
                }
                
                InputValidator.displaySuccess("Login successful! Welcome, " + user.getUsername());
                return true;
            } else if (user != null) {
                InputValidator.displayError("Authentication successful but role verification failed. Expected 'adopter', got '" + user.getRole() + "'.");
                return false;
            } else {
                InputValidator.displayError("Invalid password for user '" + username + "'.");
                return false;
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Login error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Handles pet owner login
     */
    private boolean petOwnerLogin() {
        InputValidator.displayHeader("PET OWNER LOGIN");
        
        try {
            String username = InputValidator.getStringInput("Username: ", false);
            String password = InputValidator.getStringInput("Password: ", false);
            
            User user = userCRUD.authenticateUser(username, password);
            
            if (user != null && "pet_owner".equals(user.getRole())) {
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
            System.out.println("3. Pet Owner Management");
            System.out.println("4. Pet Management");
            System.out.println("5. Adoption Request Management");
            System.out.println("6. System Statistics");
            System.out.println("7. Database Status");
            System.out.println("8. Logout");
            System.out.println("-".repeat(60));
            
            int choice = InputValidator.getIntInput("Enter your choice (1-8): ", 1, 8);
            
            switch (choice) {
                case 1:
                    userDashboard.showUserMenu();
                    break;
                case 2:
                    adopterDashboard.showAdopterMenu();
                    break;
                case 3:
                    petOwnerDashboard.showPetOwnerMenu();
                    break;
                case 4:
                    petDashboard.showPetMenu();
                    break;
                case 5:
                    adoptionRequestDashboard.showAdoptionRequestMenu();
                    break;
                case 6:
                    showSystemStatistics();
                    break;
                case 7:
                    showDatabaseStatus();
                    break;
                case 8:
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
     * Displays the main menu for authenticated pet owner users
     */
    private void showPetOwnerMenu() {
        while (true) {
            InputValidator.displayHeader("FUREVER PET ADOPTION SYSTEM - PET OWNER MENU");
            System.out.println("Logged in as: " + currentUser.getUsername() + " (Pet Owner)");
            System.out.println();
            System.out.println("1. Manage My Pets");
            System.out.println("2. Register New Pet");
            System.out.println("3. View Adoption Requests for My Pets");
            System.out.println("4. Update My Profile");
            System.out.println("5. View Pet Statistics");
            System.out.println("6. Logout");
            System.out.println("-".repeat(60));
            
            int choice = InputValidator.getIntInput("Enter your choice (1-6): ", 1, 6);
            
            switch (choice) {
                case 1:
                    manageMyPets();
                    break;
                case 2:
                    registerNewPet();
                    break;
                case 3:
                    viewAdoptionRequestsForMyPets();
                    break;
                case 4:
                    updateMyPetOwnerProfile();
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
            System.out.println("Choose your guest access type:");
            System.out.println("1. General Public (View pets and statistics)");
            System.out.println("2. Pet Owner/Rescuer (View adoption process info)");
            System.out.println("3. Potential Adopter (View available pets and adopters)");
            System.out.println("4. Return to Login Menu");
            System.out.println("-".repeat(60));
            
            int choice = InputValidator.getIntInput("Enter your choice (1-4): ", 1, 4);
            
            switch (choice) {
                case 1:
                    showGeneralPublicMenu();
                    break;
                case 2:
                    showPetOwnerGuestMenu();
                    break;
                case 3:
                    showPotentialAdopterMenu();
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
     * General public guest menu
     */
    private void showGeneralPublicMenu() {
        while (true) {
            InputValidator.displayHeader("GENERAL PUBLIC ACCESS");
            System.out.println("1. View Available Pets");
            System.out.println("2. View Pet Statistics");
            System.out.println("3. Return to Guest Menu");
            System.out.println("-".repeat(60));
            
            int choice = InputValidator.getIntInput("Enter your choice (1-3): ", 1, 3);
            
            switch (choice) {
                case 1:
                    showAvailablePets();
                    break;
                case 2:
                    showGuestPetStatistics();
                    break;
                case 3:
                    return;
                default:
                    InputValidator.displayError("Invalid choice. Please try again.");
            }
            
            InputValidator.waitForEnter();
        }
    }
    
    /**
     * Pet Owner/Rescuer guest menu
     */
    private void showPetOwnerGuestMenu() {
        while (true) {
            InputValidator.displayHeader("PET OWNER/RESCUER GUEST ACCESS");
            System.out.println("Information for those who want to register pets for adoption");
            System.out.println();
            System.out.println("1. View Available Pets (Examples)");
            System.out.println("2. View Adoption Process Information");
            System.out.println("3. View Registration Requirements");
            System.out.println("4. Return to Guest Menu");
            System.out.println("-".repeat(60));
            
            int choice = InputValidator.getIntInput("Enter your choice (1-4): ", 1, 4);
            
            switch (choice) {
                case 1:
                    showAvailablePets();
                    break;
                case 2:
                    showAdoptionProcessInfo();
                    break;
                case 3:
                    showRegistrationRequirements();
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
     * Potential Adopter guest menu
     */
    private void showPotentialAdopterMenu() {
        while (true) {
            InputValidator.displayHeader("POTENTIAL ADOPTER GUEST ACCESS");
            System.out.println("Information for those interested in adopting pets");
            System.out.println();
            System.out.println("1. View Available Pets");
            System.out.println("2. View Successful Adopters");
            System.out.println("3. View Adoption Requirements");
            System.out.println("4. Return to Guest Menu");
            System.out.println("-".repeat(60));
            
            int choice = InputValidator.getIntInput("Enter your choice (1-4): ", 1, 4);
            
            switch (choice) {
                case 1:
                    showAvailablePets();
                    break;
                case 2:
                    showGuestAdopters();
                    break;
                case 3:
                    showAdoptionRequirements();
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
     * Shows adoption process information for pet owners/rescuers
     */
    private void showAdoptionProcessInfo() {
        InputValidator.displayHeader("ADOPTION PROCESS INFORMATION");
        
        System.out.println("How the Pet Adoption Process Works:");
        System.out.println();
        System.out.println("1. PET REGISTRATION:");
        System.out.println("   - Create an account and log in as Pet Owner");
        System.out.println("   - Register your pet with complete information");
        System.out.println("   - Upload health records and vaccination proof");
        System.out.println();
        System.out.println("2. ADOPTION REVIEW:");
        System.out.println("   - Potential adopters browse available pets");
        System.out.println("   - Adopters submit adoption requests");
        System.out.println("   - You review and approve/reject requests");
        System.out.println();
        System.out.println("3. ADOPTION COMPLETION:");
        System.out.println("   - Meet with approved adopter");
        System.out.println("   - Complete adoption paperwork");
        System.out.println("   - Pet status updated to 'Adopted'");
        System.out.println();
        System.out.println("For more information, please create an account!");
    }
    
    /**
     * Shows registration requirements for pet owners
     */
    private void showRegistrationRequirements() {
        InputValidator.displayHeader("PET REGISTRATION REQUIREMENTS");
        
        System.out.println("Required Information for Pet Registration:");
        System.out.println();
        System.out.println("BASIC INFORMATION:");
        System.out.println("• Pet name and type (dog, cat, etc.)");
        System.out.println("• Age and gender");
        System.out.println("• Physical description");
        System.out.println();
        System.out.println("HEALTH INFORMATION:");
        System.out.println("• Current health status");
        System.out.println("• Vaccination records");
        System.out.println("• Medical history (if any)");
        System.out.println();
        System.out.println("OWNER INFORMATION:");
        System.out.println("• Valid contact information");
        System.out.println("• Reason for rehoming");
        System.out.println("• Pet care history");
        System.out.println();
        System.out.println("Please have all this information ready when you register!");
    }
    
    /**
     * Shows adoption requirements for potential adopters
     */
    private void showAdoptionRequirements() {
        InputValidator.displayHeader("ADOPTION REQUIREMENTS");
        
        System.out.println("Requirements to Adopt a Pet:");
        System.out.println();
        System.out.println("ADOPTER QUALIFICATIONS:");
        System.out.println("• Must be 18+ years old");
        System.out.println("• Stable living situation");
        System.out.println("• Financial ability to care for pet");
        System.out.println("• Previous pet experience (preferred)");
        System.out.println();
        System.out.println("APPLICATION PROCESS:");
        System.out.println("• Create adopter account");
        System.out.println("• Browse available pets");
        System.out.println("• Submit adoption request");
        System.out.println("• Wait for pet owner approval");
        System.out.println();
        System.out.println("ADOPTION MEETING:");
        System.out.println("• Meet the pet in person");
        System.out.println("• Complete adoption paperwork");
        System.out.println("• Provide ongoing care commitment");
        System.out.println();
        System.out.println("Ready to adopt? Create your account today!");
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
                System.out.println("You need an adopter profile to submit adoption requests.");
                System.out.println("Please go to 'Update My Profile' first to create your profile.");
                return;
            }
            
            // Show available pets with enhanced display
            var petCRUD = new com.furever.crud.PetCRUD();
            var availablePets = petCRUD.getPetsByAdoptionStatus("Available");
            
            if (availablePets.isEmpty()) {
                InputValidator.displayWarning("No pets currently available for adoption.");
                return;
            }
            
            System.out.println("Available Pets for Adoption:");
            System.out.println("Please review the complete pet information below to make your selection:");
            // Use the enhanced display method for comprehensive pet information
            petCRUD.displayPetsTable(availablePets);
            
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
                System.out.println("You need an adopter profile to view adoption requests.");
                System.out.println("Please go to 'Update My Profile' first to create your profile.");
                return;
            }
            
            var requestCRUD = new com.furever.crud.AdoptionRequestCRUD();
            var requests = requestCRUD.getAdoptionRequestsByAdopter(adopter.getAdopterId());
            
            if (requests.isEmpty()) {
                System.out.println("You have no adoption requests yet.");
                return;
            }
            
            System.out.printf("%-5s %-20s %-12s %-10s %-12s%n", 
                "ID", "Pet Name", "Date", "Status", "Approval");
            System.out.println("-".repeat(65));
            
            var petCRUD = new com.furever.crud.PetCRUD();
            
            for (var request : requests) {
                // Get pet name for better display
                String petName = "Unknown";
                try {
                    var pet = petCRUD.getPetById(request.getPetId());
                    if (pet != null) {
                        petName = pet.getPetName();
                    }
                } catch (Exception e) {
                    // Keep default "Unknown" if error occurs
                }
                
                System.out.printf("%-5d %-20s %-12s %-10s %-12s%n",
                    request.getAdoptionRequestId(),
                    petName.length() > 19 ? petName.substring(0, 16) + "..." : petName,
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
                System.out.println("Would you like to create your adopter profile now?");
                
                String createProfile = InputValidator.getStringInput("Create profile? (y/n): ", false);
                if ("y".equalsIgnoreCase(createProfile) || "yes".equalsIgnoreCase(createProfile)) {
                    // Create new adopter profile
                    adopter = new com.furever.models.Adopter();
                    
                    System.out.println("\nCreating your adopter profile:");
                    String name = InputValidator.getStringInput("Full Name: ", false);
                    String contact = InputValidator.getStringInput("Contact Number: ", false);
                    String email = InputValidator.getStringInput("Email Address: ", false);
                    String address = InputValidator.getStringInput("Home Address: ", false);
                    
                    adopter.setAdopterName(name);
                    adopter.setAdopterContact(contact);
                    adopter.setAdopterEmail(email);
                    adopter.setAdopterAddress(address);
                    adopter.setAdopterUsername(currentUser.getUsername());
                    
                    if (adopterCRUD.createAdopter(adopter)) {
                        InputValidator.displaySuccess("Adopter profile created successfully!");
                        System.out.println("You can now update your profile anytime.");
                    } else {
                        InputValidator.displayError("Failed to create adopter profile.");
                        return;
                    }
                } else {
                    System.out.println("Profile creation cancelled. Please contact an administrator.");
                    return;
                }
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
    
    /**
     * Pet Owner functionality - Manage existing pets
     */
    private void manageMyPets() {
        InputValidator.displayHeader("MANAGE MY PETS");
        
        try {
            // Get current pet owner profile
            PetOwner petOwner = petOwnerCRUD.getPetOwnerByUsername(currentUser.getUsername());
            
            if (petOwner == null) {
                InputValidator.displayWarning("No pet owner profile found for your account.");
                System.out.println("Creating pet owner profile...");
                
                // Create automatic profile
                if (petOwnerCRUD.createPetOwnerProfileForUser(
                        currentUser.getUsername(), 
                        currentUser.getUsername(), 
                        currentUser.getEmail())) {
                    InputValidator.displaySuccess("Pet owner profile created successfully!");
                    petOwner = petOwnerCRUD.getPetOwnerByUsername(currentUser.getUsername());
                } else {
                    InputValidator.displayError("Failed to create pet owner profile. Please contact an administrator.");
                    return;
                }
            }
            
            // Get pets owned by this user
            var petCRUD = new com.furever.crud.PetCRUD();
            var myPets = petCRUD.getPetsByOwner(petOwner.getPetOwnerId());
            
            if (myPets.isEmpty()) {
                System.out.println("You don't have any registered pets yet.");
                System.out.println("Use 'Register New Pet' to add pets to the system.");
                return;
            }
            
            System.out.println("Your Registered Pets:");
            System.out.printf("%-5s %-20s %-8s %-10s %-12s %-15s%n", 
                "ID", "Name", "Age", "Gender", "Health", "Status");
            System.out.println("-".repeat(75));
            
            for (var pet : myPets) {
                System.out.printf("%-5d %-20s %-8d %-10s %-12s %-15s%n",
                    pet.getPetId(),
                    pet.getPetName(),
                    pet.getAge(),
                    pet.getGender(),
                    pet.getHealthStatus(),
                    pet.getAdoptionStatus());
            }
            
            System.out.println("\nPet Management Options:");
            System.out.println("1. View detailed pet information");
            System.out.println("2. Update pet information");
            System.out.println("3. Change adoption status");
            System.out.println("4. Return to main menu");
            
            int choice = InputValidator.getIntInput("Choose an option (1-4): ", 1, 4);
            
            switch (choice) {
                case 1:
                    viewPetDetails(myPets);
                    break;
                case 2:
                    updatePetInformation(myPets);
                    break;
                case 3:
                    changeAdoptionStatus(myPets);
                    break;
                case 4:
                    return;
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error managing pets: " + e.getMessage());
        }
    }
    
    /**
     * Pet Owner functionality - Register new pet
     */
    private void registerNewPet() {
        InputValidator.displayHeader("REGISTER NEW PET");
        
        try {
            // Get current pet owner profile
            PetOwner petOwner = petOwnerCRUD.getPetOwnerByUsername(currentUser.getUsername());
            
            if (petOwner == null) {
                InputValidator.displayWarning("No pet owner profile found for your account.");
                System.out.println("Creating pet owner profile...");
                
                // Create automatic profile
                if (petOwnerCRUD.createPetOwnerProfileForUser(
                        currentUser.getUsername(), 
                        currentUser.getUsername(), 
                        currentUser.getEmail())) {
                    InputValidator.displaySuccess("Pet owner profile created successfully!");
                    petOwner = petOwnerCRUD.getPetOwnerByUsername(currentUser.getUsername());
                } else {
                    InputValidator.displayError("Failed to create pet owner profile. Please contact an administrator.");
                    return;
                }
            }
            
            System.out.println("Enter new pet details:");
            
            String petName = InputValidator.getStringInput("Pet Name: ", false);
            int age = InputValidator.getIntInput("Age (years): ", 0, 30);
            String gender = InputValidator.getStringInput("Gender (Male/Female): ", false);
            
            // Health Status selection
            System.out.println("\nHealth Status Options:");
            System.out.println("1. Healthy");
            System.out.println("2. Needs Treatment");
            int healthChoice = InputValidator.getIntInput("Select health status (1-2): ", 1, 2);
            String healthStatus = healthChoice == 1 ? "Healthy" : "Needs Treatment";
            
            String description = InputValidator.getStringInput("Description: ", true);
            
            // Get available pet types
            var petTypeCRUD = new com.furever.crud.PetTypeCRUD();
            var petTypes = petTypeCRUD.getAllPetTypes();
            
            if (petTypes.isEmpty()) {
                InputValidator.displayError("No pet types available. Please contact an administrator.");
                return;
            }
            
            System.out.println("\nAvailable Pet Types:");
            for (int i = 0; i < petTypes.size(); i++) {
                System.out.println((i + 1) + ". " + petTypes.get(i).getPetTypeName());
            }
            
            int typeChoice = InputValidator.getIntInput("Select pet type (1-" + petTypes.size() + "): ", 1, petTypes.size());
            int petTypeId = petTypes.get(typeChoice - 1).getPetTypeId();
            
            // Create new pet
            var petCRUD = new com.furever.crud.PetCRUD();
            var newPet = new com.furever.models.Pet();
            newPet.setPetName(petName);
            newPet.setAge(age);
            newPet.setGender(gender);
            newPet.setHealthStatus(healthStatus);
            newPet.setDescription(description);
            newPet.setPetTypeId(petTypeId);
            newPet.setPetOwnerId(petOwner.getPetOwnerId());
            newPet.setAdoptionStatus("Available"); // Default status
            
            if (petCRUD.createPet(newPet)) {
                InputValidator.displaySuccess("Pet registered successfully!");
                System.out.println("Your pet '" + petName + "' has been added to the system and is available for adoption.");
            } else {
                InputValidator.displayError("Failed to register pet. Please try again.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error registering pet: " + e.getMessage());
        }
    }
    
    /**
     * Pet Owner functionality - View adoption requests
     */
    private void viewAdoptionRequestsForMyPets() {
        InputValidator.displayHeader("ADOPTION REQUESTS FOR MY PETS");
        
        try {
            // Get current pet owner profile
            PetOwner petOwner = petOwnerCRUD.getPetOwnerByUsername(currentUser.getUsername());
            
            if (petOwner == null) {
                InputValidator.displayWarning("No pet owner profile found for your account.");
                return;
            }
            
            // Get all pets owned by this user
            var petCRUD = new com.furever.crud.PetCRUD();
            var myPets = petCRUD.getPetsByOwner(petOwner.getPetOwnerId());
            
            if (myPets.isEmpty()) {
                System.out.println("You don't have any registered pets yet.");
                return;
            }
            
            // Get adoption requests for all owned pets
            var requestCRUD = new com.furever.crud.AdoptionRequestCRUD();
            boolean hasRequests = false;
            
            for (var pet : myPets) {
                var requests = requestCRUD.getAdoptionRequestsByPet(pet.getPetId());
                
                if (!requests.isEmpty()) {
                    if (!hasRequests) {
                        System.out.println("Adoption Requests for Your Pets:");
                        System.out.printf("%-10s %-20s %-20s %-15s %-12s%n", 
                            "Request ID", "Pet Name", "Adopter", "Status", "Date");
                        System.out.println("-".repeat(85));
                        hasRequests = true;
                    }
                    
                    for (var request : requests) {
                        // Get adopter information
                        var adopterCRUD = new com.furever.crud.AdopterCRUD();
                        var adopter = adopterCRUD.getAdopterById(request.getAdopterId());
                        
                        System.out.printf("%-10d %-20s %-20s %-15s %-12s%n",
                            request.getAdoptionRequestId(),
                            pet.getPetName(),
                            adopter != null ? adopter.getAdopterName() : "Unknown",
                            request.getStatus(),
                            request.getRequestDate());
                    }
                }
            }
            
            if (!hasRequests) {
                System.out.println("No adoption requests found for your pets.");
                return;
            }
            
            System.out.println("\nOptions:");
            System.out.println("1. View detailed request information");
            System.out.println("2. Approve/Reject requests");
            System.out.println("3. Return to main menu");
            
            int choice = InputValidator.getIntInput("Choose an option (1-3): ", 1, 3);
            
            switch (choice) {
                case 1:
                    viewDetailedRequestInfo(myPets);
                    break;
                case 2:
                    processAdoptionRequests(myPets);
                    break;
                case 3:
                    return;
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error viewing adoption requests: " + e.getMessage());
        }
    }
    
    /**
     * Pet Owner functionality - Update profile
     */
    private void updateMyPetOwnerProfile() {
        InputValidator.displayHeader("UPDATE PET OWNER PROFILE");
        
        try {
            // Get current pet owner profile
            PetOwner petOwner = petOwnerCRUD.getPetOwnerByUsername(currentUser.getUsername());
            
            if (petOwner == null) {
                InputValidator.displayWarning("No pet owner profile found for your account.");
                System.out.println("Creating pet owner profile...");
                
                // Create automatic profile
                if (petOwnerCRUD.createPetOwnerProfileForUser(
                        currentUser.getUsername(), 
                        currentUser.getUsername(), 
                        currentUser.getEmail())) {
                    InputValidator.displaySuccess("Pet owner profile created successfully!");
                    petOwner = petOwnerCRUD.getPetOwnerByUsername(currentUser.getUsername());
                } else {
                    InputValidator.displayError("Failed to create pet owner profile. Please contact an administrator.");
                    return;
                }
            }
            
            System.out.println("Current Profile Information:");
            System.out.println("Name: " + petOwner.getPetOwnerName());
            System.out.println("Contact: " + petOwner.getPetOwnerContact());
            System.out.println("Email: " + petOwner.getPetOwnerEmail());
            System.out.println("Address: " + petOwner.getPetOwnerAddress());
            System.out.println("Profile/Bio: " + petOwner.getPetOwnerProfile());
            
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
            
            String profile = InputValidator.getStringInput("Profile/Bio [" + petOwner.getPetOwnerProfile() + "]: ", true);
            if (!profile.isEmpty()) {
                petOwner.setPetOwnerProfile(profile);
            }
            
            if (petOwnerCRUD.updatePetOwner(petOwner)) {
                InputValidator.displaySuccess("Profile updated successfully!");
            } else {
                InputValidator.displayError("Failed to update profile.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error updating profile: " + e.getMessage());
        }
    }
    
    /**
     * Helper method to view detailed pet information
     */
    private void viewPetDetails(java.util.List<com.furever.models.Pet> pets) {
        int petId = InputValidator.getIntInput("Enter Pet ID to view details: ", 1, Integer.MAX_VALUE);
        
        var selectedPet = pets.stream()
            .filter(pet -> pet.getPetId() == petId)
            .findFirst()
            .orElse(null);
        
        if (selectedPet == null) {
            InputValidator.displayError("Pet not found or you don't own this pet.");
            return;
        }
        
        System.out.println("\n=== PET DETAILS ===");
        System.out.println("ID: " + selectedPet.getPetId());
        System.out.println("Name: " + selectedPet.getPetName());
        System.out.println("Age: " + selectedPet.getAge() + " years");
        System.out.println("Gender: " + selectedPet.getGender());
        System.out.println("Health Status: " + selectedPet.getHealthStatus());
        System.out.println("Description: " + selectedPet.getDescription());
        System.out.println("Adoption Status: " + selectedPet.getAdoptionStatus());
        System.out.println("Registration Date: " + selectedPet.getDateRegistered());
    }
    
    /**
     * Helper method to update pet information
     */
    private void updatePetInformation(java.util.List<com.furever.models.Pet> pets) {
        int petId = InputValidator.getIntInput("Enter Pet ID to update: ", 1, Integer.MAX_VALUE);
        
        var selectedPet = pets.stream()
            .filter(pet -> pet.getPetId() == petId)
            .findFirst()
            .orElse(null);
        
        if (selectedPet == null) {
            InputValidator.displayError("Pet not found or you don't own this pet.");
            return;
        }
        
        System.out.println("Current Pet Information:");
        System.out.println("Name: " + selectedPet.getPetName());
        System.out.println("Age: " + selectedPet.getAge());
        System.out.println("Health Status: " + selectedPet.getHealthStatus());
        System.out.println("Description: " + selectedPet.getDescription());
        
        System.out.println("\nEnter new information (press Enter to keep current value):");
        
        String name = InputValidator.getStringInput("Name [" + selectedPet.getPetName() + "]: ", true);
        if (!name.isEmpty()) {
            selectedPet.setPetName(name);
        }
        
        String ageStr = InputValidator.getStringInput("Age [" + selectedPet.getAge() + "]: ", true);
        if (!ageStr.isEmpty()) {
            try {
                int age = Integer.parseInt(ageStr);
                selectedPet.setAge(age);
            } catch (NumberFormatException e) {
                InputValidator.displayError("Invalid age format.");
                return;
            }
        }
        
        String health = InputValidator.getStringInput("Health Status [" + selectedPet.getHealthStatus() + "] - Enter to keep current, or type new: ", true);
        if (!health.isEmpty()) {
            // Validate health status
            if ("Healthy".equalsIgnoreCase(health) || "Needs Treatment".equalsIgnoreCase(health)) {
                selectedPet.setHealthStatus(health);
            } else {
                System.out.println("Invalid health status. Valid options are: 'Healthy' or 'Needs Treatment'");
                System.out.println("Health status not updated.");
            }
        }
        
        String description = InputValidator.getStringInput("Description [" + selectedPet.getDescription() + "]: ", true);
        if (!description.isEmpty()) {
            selectedPet.setDescription(description);
        }
        
        try {
            var petCRUD = new com.furever.crud.PetCRUD();
            if (petCRUD.updatePet(selectedPet)) {
                InputValidator.displaySuccess("Pet information updated successfully!");
            } else {
                InputValidator.displayError("Failed to update pet information.");
            }
        } catch (Exception e) {
            InputValidator.displayError("Error updating pet: " + e.getMessage());
        }
    }
    
    /**
     * Helper method to change pet adoption status
     */
    private void changeAdoptionStatus(java.util.List<com.furever.models.Pet> pets) {
        int petId = InputValidator.getIntInput("Enter Pet ID to change status: ", 1, Integer.MAX_VALUE);
        
        var selectedPet = pets.stream()
            .filter(pet -> pet.getPetId() == petId)
            .findFirst()
            .orElse(null);
        
        if (selectedPet == null) {
            InputValidator.displayError("Pet not found or you don't own this pet.");
            return;
        }
        
        System.out.println("Current Status: " + selectedPet.getAdoptionStatus());
        System.out.println("\nAvailable statuses:");
        System.out.println("1. Available");
        System.out.println("2. Pending");
        System.out.println("3. Adopted");
        
        int choice = InputValidator.getIntInput("Select new status (1-3): ", 1, 3);
        
        String newStatus = switch (choice) {
            case 1 -> "Available";
            case 2 -> "Pending";
            case 3 -> "Adopted";
            default -> selectedPet.getAdoptionStatus();
        };
        
        selectedPet.setAdoptionStatus(newStatus);
        
        try {
            var petCRUD = new com.furever.crud.PetCRUD();
            if (petCRUD.updatePet(selectedPet)) {
                InputValidator.displaySuccess("Pet adoption status updated to: " + newStatus);
            } else {
                InputValidator.displayError("Failed to update adoption status.");
            }
        } catch (Exception e) {
            InputValidator.displayError("Error updating status: " + e.getMessage());
        }
    }
    
    /**
     * Helper method to view detailed adoption request information
     */
    private void viewDetailedRequestInfo(java.util.List<com.furever.models.Pet> pets) {
        int requestId = InputValidator.getIntInput("Enter Request ID to view details: ", 1, Integer.MAX_VALUE);
        
        try {
            var requestCRUD = new com.furever.crud.AdoptionRequestCRUD();
            var request = requestCRUD.getAdoptionRequestById(requestId);
            
            if (request == null) {
                InputValidator.displayError("Request not found.");
                return;
            }
            
            // Verify this request is for one of the user's pets
            boolean isMyPet = pets.stream().anyMatch(pet -> pet.getPetId() == request.getPetId());
            
            if (!isMyPet) {
                InputValidator.displayError("This request is not for your pet.");
                return;
            }
            
            // Get pet and adopter details
            var petCRUD = new com.furever.crud.PetCRUD();
            var adopterCRUD = new com.furever.crud.AdopterCRUD();
            
            var pet = petCRUD.getPetById(request.getPetId());
            var adopter = adopterCRUD.getAdopterById(request.getAdopterId());
            
            System.out.println("\n=== ADOPTION REQUEST DETAILS ===");
            System.out.println("Request ID: " + request.getAdoptionRequestId());
            System.out.println("Pet: " + (pet != null ? pet.getPetName() : "Unknown"));
            System.out.println("Adopter: " + (adopter != null ? adopter.getAdopterName() : "Unknown"));
            System.out.println("Adopter Contact: " + (adopter != null ? adopter.getAdopterContact() : "Unknown"));
            System.out.println("Adopter Email: " + (adopter != null ? adopter.getAdopterEmail() : "Unknown"));
            System.out.println("Request Date: " + request.getRequestDate());
            System.out.println("Status: " + request.getStatus());
            System.out.println("Remarks: " + request.getRemarks());
            
        } catch (Exception e) {
            InputValidator.displayError("Error viewing request details: " + e.getMessage());
        }
    }
    
    /**
     * Helper method to process (approve/reject) adoption requests
     */
    private void processAdoptionRequests(java.util.List<com.furever.models.Pet> pets) {
        int requestId = InputValidator.getIntInput("Enter Request ID to process: ", 1, Integer.MAX_VALUE);
        
        try {
            var requestCRUD = new com.furever.crud.AdoptionRequestCRUD();
            var request = requestCRUD.getAdoptionRequestById(requestId);
            
            if (request == null) {
                InputValidator.displayError("Request not found.");
                return;
            }
            
            // Verify this request is for one of the user's pets
            boolean isMyPet = pets.stream().anyMatch(pet -> pet.getPetId() == request.getPetId());
            
            if (!isMyPet) {
                InputValidator.displayError("This request is not for your pet.");
                return;
            }
            
            if (!"Pending".equals(request.getStatus())) {
                InputValidator.displayWarning("This request has already been processed. Current status: " + request.getStatus());
                return;
            }
            
            System.out.println("Current Status: " + request.getStatus());
            System.out.println("\nOptions:");
            System.out.println("1. Approve Request");
            System.out.println("2. Reject Request");
            System.out.println("3. Cancel");
            
            int choice = InputValidator.getIntInput("Choose action (1-3): ", 1, 3);
            
            if (choice == 3) {
                return;
            }
            
            String newStatus = choice == 1 ? "Approved" : "Rejected";
            String remarks = InputValidator.getStringInput("Add remarks (optional): ", true);
            
            boolean success = false;
            
            if (choice == 1) { // Approve Request
                java.sql.Date approvalDate = new java.sql.Date(System.currentTimeMillis());
                success = requestCRUD.approveAdoptionRequestSafely(requestId, approvalDate, remarks);
            } else { // Reject Request
                success = requestCRUD.rejectAdoptionRequest(requestId, remarks);
            }
            
            if (success) {
                InputValidator.displaySuccess("Request " + newStatus.toLowerCase() + " successfully!");
                
                // If approved, update pet status
                if ("Approved".equals(newStatus)) {
                    var petCRUD = new com.furever.crud.PetCRUD();
                    var pet = petCRUD.getPetById(request.getPetId());
                    if (pet != null) {
                        pet.setAdoptionStatus("Adopted");
                        petCRUD.updatePet(pet);
                        System.out.println("Pet status updated to 'Adopted'.");
                    }
                }
            } else {
                InputValidator.displayError("Failed to update request.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("Error processing request: " + e.getMessage());
        }
    }
}
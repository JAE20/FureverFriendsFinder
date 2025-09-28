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

    private static final String INDENT = "\t\t\t\t\t";
    private static final String INDENT1 = "\t\t\t\t\t\t\t";

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
                InputValidator.displayError(INDENT + "Failed to connect to database. Please check your database configuration.");
                return;
            }

            System.out.println(InputValidator.PURPLE + INDENT + "=================================================================");
            System.out.println(InputValidator.BLUE + INDENT + " ########  ##   ## ########  ######## ##   ## ######## ########");
            System.out.println(InputValidator.BLUE + INDENT + " ##        ##   ## ##    ##  ##       ##   ## ##       ##    ##");
            System.out.println(InputValidator.BLUE + INDENT + " ######    ##   ## ########  ######    ## ##  ######   ########");
            System.out.println(InputValidator.BLUE + INDENT + " ##        ##   ## ##   ##   ##         ###   ##       ##   ## ");
            System.out.println(InputValidator.BLUE + INDENT + " ##        ####### ##    ##  #######     #    ######## ##    ##");
            System.out.println(InputValidator.PURPLE + INDENT + "=================================================================");
            System.out.println(InputValidator.BOLD + INDENT + "                      PET ADOPTION SYSTEM ");
            System.out.println(InputValidator.PURPLE + INDENT + "=================================================================");
            System.out.println();
            System.out.println(InputValidator.GREEN + INDENT + "                System initialized successfully!" + InputValidator.RESET);

            // Show login screen Select displayError:
            showLoginMenu();

        } catch (Exception e) {
            InputValidator.displayError(INDENT + "Failed to start application: " + e.getMessage());
        } finally {
            // Close database connection
            DbConnection.closeConnection();
            System.out.println(INDENT + "Thank you for using Furever Pet Adoption System!");
        }
    }

    
    private void showLoginMenu() { 
        while (true) {
            System.out.println(INDENT + "=================================================================");
            System.out.println(InputValidator.PURPLE + INDENT1 + "    LOGIN TO FUREVER SYSTEM" + InputValidator.RESET);
            System.out.println(INDENT + "=================================================================");
            System.out.println(INDENT + "1. Admin Login");
            System.out.println(INDENT + "2. Adopter Login");
            System.out.println(INDENT + "3. Pet Owner Login");
            System.out.println(INDENT + "4. Guest Access (Limited Features)");
            System.out.println(INDENT + "5. Exit System");
            System.out.println(INDENT + "-".repeat(65));

            int choice = InputValidator.getIntInput(INDENT + "Enter your choice (1-5): ", 1, 5);

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
                    InputValidator.displayError(INDENT + "Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Handles admin login displayError
     */
    private boolean adminLogin() {
        System.out.println(INDENT + "=================================================================");
        System.out.println(InputValidator.PURPLE + INDENT + "                         ADMIN LOGIN" + InputValidator.RESET);
        System.out.println(INDENT + "=================================================================");

        try {
            String username = InputValidator.getStringInput(INDENT + "Username: ", false);
            String password = InputValidator.getStringInput(INDENT + "Password: ", false);

            User user = userCRUD.authenticateUser(username, password);

            if (user != null && "admin".equals(user.getRole())) { //taga check if tama yung role sa log in 
                currentUser = user;
                InputValidator.displaySuccess("       Login successful! Welcome, " + user.getUsername());
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

    private boolean adopterLogin() {
        System.out.println(INDENT + "=================================================================");
        System.out.println(InputValidator.PURPLE + INDENT + "                       ADOPTER LOGIN" + InputValidator.RESET);
        System.out.println(INDENT + "=================================================================");

        try {
            String username = InputValidator.getStringInput(INDENT + "Username: ", false);
            String password = InputValidator.getStringInput(INDENT + "Password: ", false);

            // taga check if nag eexist yung user
            User userCheck = userCRUD.getUserByUsername(username);
            if (userCheck == null) {
                InputValidator.displayError("User '" + username + "' not found.");
                return false;
            }

            // taga check ng proper role
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
                    System.out.println(INDENT + "Creating adopter profile...");

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

                InputValidator.displaySuccess("       Login successful! Welcome, " + user.getUsername());
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
        System.out.println(INDENT + "=================================================================");
        System.out.println(InputValidator.PURPLE + INDENT + "                      PET OWNER LOGIN" + InputValidator.RESET);
        System.out.println(INDENT + "=================================================================");

        try {
            String username = InputValidator.getStringInput(INDENT + "Username: ", false);
            String password = InputValidator.getStringInput(INDENT + "Password: ", false);

            User user = userCRUD.authenticateUser(username, password);

            if (user != null && "pet_owner".equals(user.getRole())) {
                currentUser = user;
                InputValidator.displaySuccess("       Login successful! Welcome, " + user.getUsername());
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
            System.out.println(INDENT + "=================================================================");
            System.out.println(InputValidator.PURPLE + INDENT + "              FUREVER PET ADOPTION SYSTEM - MAIN MENU" + InputValidator.RESET);
            System.out.println(INDENT + "=================================================================");
            System.out.println(INDENT + "Logged in as: " + currentUser.getUsername() + " (Admin)");
            System.out.println();
            System.out.println(INDENT + "1. User Management");
            System.out.println(INDENT + "2. Adopter Management");
            System.out.println(INDENT + "3. Pet Owner Management");
            System.out.println(INDENT + "4. Pet Management");
            System.out.println(INDENT + "5. Adoption Request Management");
            System.out.println(INDENT + "6. System Statistics");
            System.out.println(INDENT + "7. Database Status");
            System.out.println(INDENT + "8. Logout");
            System.out.println(INDENT + "-".repeat(65));

            int choice = InputValidator.getIntInput(INDENT + "Enter your choice (1-8): ", 1, 8);

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
            System.out.println(INDENT + "=================================================================");
            System.out.println(InputValidator.PURPLE + INDENT + "             FUREVER PET ADOPTION SYSTEM - ADOPTER MENU" + InputValidator.RESET);
            System.out.println(INDENT + "=================================================================");
            System.out.println(INDENT + "Logged in as: " + currentUser.getUsername() + " (Adopter)");
            System.out.println();
            System.out.println(INDENT + "1. View Available Pets");
            System.out.println(INDENT + "2. Submit Adoption Request");
            System.out.println(INDENT + "3. View My Adoption Requests");
            System.out.println(INDENT + "4. Update My Profile");
            System.out.println(INDENT + "5. View Pet Statistics");
            System.out.println(INDENT + "6. Logout");
            System.out.println(INDENT + "-".repeat(65));

            int choice = InputValidator.getIntInput(INDENT + "Enter your choice (1-6): ", 1, 6);

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
            System.out.println(INDENT + "=================================================================");
            System.out.println(InputValidator.PURPLE + INDENT + "          FUREVER PET ADOPTION SYSTEM - PET OWNER MENU" + InputValidator.RESET);
            System.out.println(INDENT + "=================================================================");
            System.out.println(INDENT + "Logged in as: " + currentUser.getUsername() + " (Pet Owner)");
            System.out.println();
            System.out.println(INDENT + "1. Manage My Pets");
            System.out.println(INDENT + "2. Register New Pet");
            System.out.println(INDENT + "3. View Adoption Requests for My Pets");
            System.out.println(INDENT + "4. Update My Profile");
            System.out.println(INDENT + "5. View Pet Statistics");
            System.out.println(INDENT + "6. Logout");
            System.out.println(INDENT + "-".repeat(65));

            int choice = InputValidator.getIntInput(INDENT + "Enter your choice (1-6): ", 1, 6);

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
            System.out.println(INDENT + "=================================================================");
            System.out.println(InputValidator.PURPLE + INDENT + "          FUREVER PET ADOPTION SYSTEM - GUEST ACCESS" + InputValidator.RESET);
            System.out.println(INDENT + "=================================================================");
            System.out.println(INDENT + "Limited access - View only features");
            System.out.println();
            System.out.println(INDENT + "Choose your guest access type:");
            System.out.println(INDENT + "1. General Public (View pets and statistics)");
            System.out.println(INDENT + "2. Pet Owner/Rescuer (View adoption process info)");
            System.out.println(INDENT + "3. Potential Adopter (View available pets and adopters)");
            System.out.println(INDENT + "4. Return to Login Menu");
            System.out.println(INDENT + "-".repeat(65));

            int choice = InputValidator.getIntInput(INDENT + "Enter your choice (1-4): ", 1, 4);  

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
            System.out.println(INDENT + "=================================================================");
            System.out.println(InputValidator.PURPLE + INDENT + "                    GENERAL PUBLIC ACCESS" + InputValidator.RESET);
            System.out.println(INDENT + "=================================================================");
            System.out.println(INDENT + "1. View Available Pets");
            System.out.println(INDENT + "2. View Pet Statistics");
            System.out.println(INDENT + "3. Return to Guest Menu");
            System.out.println(INDENT + "-".repeat(65));

            int choice = InputValidator.getIntInput(INDENT + "Enter your choice (1-3): ", 1, 3);

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
            System.out.println(INDENT + "=================================================================");
            System.out.println(InputValidator.PURPLE + INDENT + "                  PET OWNER/RESCUER GUEST ACCESS" + InputValidator.RESET);
            System.out.println(INDENT + "=================================================================");
            System.out.println(INDENT + "Information for those who want to register pets for adoption");
            System.out.println();
            System.out.println(INDENT + "1. View Available Pets (Examples)");
            System.out.println(INDENT + "2. View Adoption Process Information");
            System.out.println(INDENT + "3. View Registration Requirements");
            System.out.println(INDENT + "4. Return to Guest Menu");
            System.out.println(INDENT + "-".repeat(65));

            int choice = InputValidator.getIntInput(INDENT + "Enter your choice (1-4): ", 1, 4);

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
                    InputValidator.displayError( "Invalid choice. Please try again.");
            }

            InputValidator.waitForEnter();
        }
    }

    /**
     * Potential Adopter guest menu
     */
    private void showPotentialAdopterMenu() {
        while (true) {
            System.out.println(INDENT + "=================================================================");
            System.out.println(InputValidator.PURPLE + INDENT + "                 POTENTIAL ADOPTER GUEST ACCESS" + InputValidator.RESET);
            System.out.println(INDENT + "=================================================================");
            System.out.println(INDENT + "Information for those interested in adopting pets");
            System.out.println();
            System.out.println(INDENT + "1. View Available Pets");
            System.out.println(INDENT + "2. View Successful Adopters");
            System.out.println(INDENT + "3. View Adoption Requirements");
            System.out.println(INDENT + "4. Return to Guest Menu");
            System.out.println(INDENT + "-".repeat(65));

            int choice = InputValidator.getIntInput(INDENT + "Enter your choice (1-4): ", 1, 4);

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
        System.out.println(INDENT + "=================================================================");
        System.out.println(InputValidator.PURPLE + INDENT + "                  AVAILABLE PETS FOR ADOPTION" + InputValidator.RESET);
        System.out.println(INDENT + "=================================================================");

        try {
            var petCRUD = new com.furever.crud.PetCRUD();
            var availablePets = petCRUD.getPetsByAdoptionStatus("Available");

            if (availablePets.isEmpty()) {
                System.out.println(INDENT + "No pets currently available for adoption.");
                return;
            }

            System.out.printf(INDENT + "%-5s %-20s %-8s %-10s %-12s%n",
                    "ID", "Name", "Age", "Gender", "Health");
            System.out.println(INDENT + "-".repeat(65));

            for (var pet : availablePets) {
                System.out.printf(INDENT + "%-5d %-20s %-8d %-10s %-12s%n",
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
        System.out.println(INDENT + "=================================================================");
        System.out.println(InputValidator.PURPLE + INDENT + "                   PET ADOPTION STATISTICS" + InputValidator.RESET);
        System.out.println(INDENT + "=================================================================");

        try {
            var petCRUD = new com.furever.crud.PetCRUD();

            int totalPets = petCRUD.getPetCount();
            int availablePets = petCRUD.getPetCountByStatus("Available");
            int adoptedPets = petCRUD.getPetCountByStatus("Adopted");

            System.out.println(INDENT + "Total Pets in System: " + totalPets);
            System.out.println(INDENT + "Available for Adoption: " + availablePets);
            System.out.println(INDENT + "Successfully Adopted: " + adoptedPets);

            if (totalPets > 0) { 
                double adoptionRate = (double) adoptedPets / totalPets * 100;
                System.out.printf(INDENT + "Adoption Success Rate: %.1f%%%n", adoptionRate);
            }

        } catch (Exception e) {
            InputValidator.displayError("Error retrieving statistics: " + e.getMessage());
        }
    }
    /**
     * Shows adoption process information for pet owners/rescuers
     */
    private void showAdoptionProcessInfo() {
        System.out.println(INDENT + "=================================================================");
        System.out.println(InputValidator.PURPLE + INDENT + "                   ADOPTION PROCESS INFORMATION" + InputValidator.RESET);
        System.out.println(INDENT + "=================================================================");

        System.out.println(INDENT + "How the Pet Adoption Process Works:");
        System.out.println();
        System.out.println(INDENT + "\t1. PET REGISTRATION:");
        System.out.println(INDENT + "    - Create an account and log in as Pet Owner");
        System.out.println(INDENT + "   - Register your pet with complete information");
        System.out.println(INDENT + "   - Upload health records and vaccination proof");
        System.out.println();
        System.out.println(INDENT + "\t2. ADOPTION REVIEW:");
        System.out.println(INDENT + "   - Potential adopters browse available pets");
        System.out.println(INDENT + "   - Adopters submit adoption requests");
        System.out.println(INDENT + "   - You review and approve/reject requests");
        System.out.println();
        System.out.println(INDENT + "\t3. ADOPTION COMPLETION:");
        System.out.println(INDENT + "   - Meet with approved adopter");
        System.out.println(INDENT + "   - Complete adoption paperwork");
        System.out.println(INDENT + "   - Pet status updated to 'Adopted'");
        System.out.println();
        System.out.println(INDENT + "For more information, please create an account!");
    }

    /**
     * Shows registration requirements for pet owners
     */
    private void showRegistrationRequirements() {
        System.out.println(INDENT + "=================================================================");
        System.out.println(InputValidator.PURPLE + INDENT + "                  PET REGISTRATION REQUIREMENTS" + InputValidator.RESET);
        System.out.println(INDENT + "=================================================================");

        System.out.println(INDENT + "Required Information for Pet Registration:");
        System.out.println();
        System.out.println(INDENT + "BASIC INFORMATION:");
        System.out.println(INDENT + "• Pet name and type (dog, cat, etc.)");
        System.out.println(INDENT + "• Age and gender");
        System.out.println(INDENT + "• Physical description");
        System.out.println();
        System.out.println(INDENT + "HEALTH INFORMATION:");
        System.out.println(INDENT + "• Current health status");
        System.out.println(INDENT + "• Vaccination records");
        System.out.println(INDENT + "• Medical history (if any)");
        System.out.println();
        System.out.println(INDENT + "OWNER INFORMATION:");
        System.out.println(INDENT + "• Valid contact information");
        System.out.println(INDENT + "• Reason for rehoming");
        System.out.println(INDENT + "• Pet care history");
        System.out.println();
        System.out.println(INDENT + "Please have all this information ready when you register!");
    }

    /**
     * Shows adoption requirements for potential adopters
     */
    private void showAdoptionRequirements() {
        System.out.println(INDENT + "=================================================================");
        System.out.println(InputValidator.PURPLE + INDENT + "                      ADOPTION REQUIREMENTS" + InputValidator.RESET);
        System.out.println(INDENT + "=================================================================");

        System.out.println(INDENT + "Requirements to Adopt a Pet:");
        System.out.println();
        System.out.println(INDENT + "ADOPTER QUALIFICATIONS:");
        System.out.println(INDENT + "• Must be 18+ years old");
        System.out.println(INDENT + "• Stable living situation");
        System.out.println(INDENT + "• Financial ability to care for pet");
        System.out.println(INDENT + "• Previous pet experience (preferred)");
        System.out.println();
        System.out.println(INDENT + "APPLICATION PROCESS:");
        System.out.println(INDENT + "• Create adopter account");
        System.out.println(INDENT + "• Browse available pets");
        System.out.println(INDENT + "• Submit adoption request");
        System.out.println(INDENT + "• Wait for pet owner approval");
        System.out.println();
        System.out.println(INDENT + "ADOPTION MEETING:");
        System.out.println(INDENT + "• Meet the pet in person");
        System.out.println(INDENT + "• Complete adoption paperwork");
        System.out.println(INDENT + "• Provide ongoing care commitment");
        System.out.println();
        System.out.println(INDENT + "Ready to adopt? Create your account today!");
    }

    /**
     * Shows adopters for guest users
     */
    private void showGuestAdopters() {
        System.out.println(INDENT + "=================================================================");
        System.out.println(InputValidator.PURPLE + InputValidator.PURPLE + INDENT + "                       REGISTERED ADOPTERS" + InputValidator.RESET);
        System.out.println(INDENT + "=================================================================");

        try {
            var adopterCRUD = new com.furever.crud.AdopterCRUD();
            var adopters = adopterCRUD.getAllAdopters();

            if (adopters.isEmpty()) {
                System.out.println(INDENT + "No adopters registered yet.");
                return;
            }

            System.out.printf(INDENT + "%-5s %-25s %-30s%n", "ID", "Name", "Email");
            System.out.println(INDENT + "-".repeat(65));

            for (var adopter : adopters) {
                System.out.printf(INDENT + "%-5d %-25s %-30s%n",
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
        System.out.println(INDENT + "=================================================================");
        System.out.println(InputValidator.PURPLE + INDENT + "                        SYSTEM STATISTICS" + InputValidator.RESET);
        System.out.println(INDENT + "=================================================================");

        try {
            var petCRUD = new com.furever.crud.PetCRUD();
            var adopterCRUD = new com.furever.crud.AdopterCRUD();
            var requestCRUD = new com.furever.crud.AdoptionRequestCRUD();

            System.out.println(INDENT + "                        === PETS ===");
            System.out.println(INDENT + "Total Pets: " + petCRUD.getPetCount());
            System.out.println(INDENT + "Available: " + petCRUD.getPetCountByStatus("Available"));
            System.out.println(INDENT + "Pending: " + petCRUD.getPetCountByStatus("Pending"));
            System.out.println(INDENT + "Adopted: " + petCRUD.getPetCountByStatus("Adopted"));

            System.out.println(INDENT + "\n                    === ADOPTERS ===");
            System.out.println(INDENT + "Total Adopters: " + adopterCRUD.getAdopterCount());

            System.out.println(INDENT + "\n                     === USERS ===");
            System.out.println(INDENT + "Total Users: " + userCRUD.getUserCount());

            System.out.println(INDENT + "\n                === ADOPTION REQUESTS ===");
            System.out.println(INDENT + "Total Requests: " + requestCRUD.getAdoptionRequestCount());
            System.out.println(INDENT + "Pending: " + requestCRUD.getAdoptionRequestCountByStatus("Pending"));
            System.out.println(INDENT + "Approved: " + requestCRUD.getAdoptionRequestCountByStatus("Approved"));
            System.out.println(INDENT + "Rejected: " + requestCRUD.getAdoptionRequestCountByStatus("Rejected"));

        } catch (Exception e) {
            InputValidator.displayError("Error retrieving system statistics: " + e.getMessage());
        }

        InputValidator.waitForEnter();
    }

    /**
     * Shows database connection status
     */
    private void showDatabaseStatus() {
        System.out.println(INDENT + "=================================================================");
        System.out.println(InputValidator.PURPLE + INDENT + "                         DATABASE STATUS" + InputValidator.RESET);
        System.out.println(INDENT + "=================================================================");

        try {
            if (DbConnection.testConnection()) {
                InputValidator.displaySuccess("Database connection is active.");
                System.out.println(INDENT + "Database URL: " + DbConnection.getDatabaseUrl());
                System.out.println(INDENT + "Database User: " + DbConnection.getDatabaseUsername());
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
        System.out.println(INDENT + "=================================================================");
        System.out.println(InputValidator.PURPLE + INDENT + "                     SUBMIT ADOPTION REQUEST" + InputValidator.RESET);
        System.out.println(INDENT + "=================================================================");

        try {
            // First check if adopter profile exists
            var adopterCRUD = new com.furever.crud.AdopterCRUD();
            var adopter = adopterCRUD.getAdopterByUsername(currentUser.getUsername());

            if (adopter == null) {
                InputValidator.displayWarning("No adopter profile found for your account.");
                System.out.println(INDENT + "You need an adopter profile to submit adoption requests.");
                System.out.println(INDENT + "Please go to 'Update My Profile' first to create your profile.");
                return;
            }

            // Show available pets with enhanced display
            var petCRUD = new com.furever.crud.PetCRUD();
            var availablePets = petCRUD.getPetsByAdoptionStatus("Available");

            if (availablePets.isEmpty()) {
                InputValidator.displayWarning("No pets currently available for adoption.");
                return;
            }

            System.out.println(INDENT + "Available Pets for Adoption:");
            System.out.println(INDENT + "Please review the complete pet information below to make your selection:");
            // Use the enhanced display method for comprehensive pet information
            petCRUD.displayPetsTable(availablePets);

            // Get pet selection from user
            System.out.println();
            int petId = InputValidator.getIntInput("\tEnter the Pet ID you want to adopt (0 to cancel): ", 0, Integer.MAX_VALUE);

            if (petId == 0) {
                System.out.println(INDENT + "Adoption request cancelled.");
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
            System.out.println(INDENT + "\nPlease provide additional information for your adoption request:");
            String reason = InputValidator.getStringInput(INDENT + "Why do you want to adopt " + selectedPet.getPetName() + "? ", false);
            String experience = InputValidator.getStringInput(INDENT + "Do you have experience with pets? ", false);
            String livingCondition = InputValidator.getStringInput(INDENT + "Describe your living conditions: ", false);

            // Combine additional info (you might want to add these fields to the AdoptionRequest model)
            String additionalInfo = String.format(INDENT + "Reason: %s\nExperience: %s\nLiving Conditions: %s",
                    reason, experience, livingCondition);

            // For now, we'll just show confirmation since the model might not have additional fields
            if (requestCRUD.createAdoptionRequest(adoptionRequest)) {
                InputValidator.displaySuccess("Adoption request submitted successfully!");
                System.out.println(INDENT + "Pet: " + selectedPet.getPetName() + " (ID: " + petId + ")");
                System.out.println(INDENT + "Status: Pending");
                System.out.println(INDENT + "Your request will be reviewed by an administrator.");
                System.out.println(INDENT + "\nAdditional Information Provided:");
                System.out.println(INDENT + additionalInfo);
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
        System.out.println(INDENT + "=================================================================");
        System.out.println(InputValidator.PURPLE + INDENT + "                     MY ADOPTION REQUESTS" + InputValidator.RESET);
        System.out.println(INDENT + "=================================================================");

        try {
            // First need to find the adopter record associated with this user
            var adopterCRUD = new com.furever.crud.AdopterCRUD();
            var adopter = adopterCRUD.getAdopterByUsername(currentUser.getUsername());

            if (adopter == null) {
                InputValidator.displayWarning("No adopter profile found for your account.");
                System.out.println(INDENT + "You need an adopter profile to view adoption requests.");
                System.out.println(INDENT + "Please go to 'Update My Profile' first to create your profile.");
                return;
            }

            var requestCRUD = new com.furever.crud.AdoptionRequestCRUD();
            var requests = requestCRUD.getAdoptionRequestsByAdopter(adopter.getAdopterId());

            if (requests.isEmpty()) {
                System.out.println(INDENT + "You have no adoption requests yet.");
                return;
            }

            System.out.printf(INDENT + "%-5s %-20s %-12s %-10s %-12s%n",
                    "ID", "Pet Name", "Date", "Status", "Approval");
            System.out.println(INDENT + "-".repeat(65));

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

                System.out.printf(INDENT + "%-5d %-20s %-12s %-10s %-12s%n",
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
        System.out.println(INDENT + "=================================================================");
        System.out.println(InputValidator.PURPLE + INDENT + "                       UPDATE MY PROFILE" + InputValidator.RESET);
        System.out.println(INDENT + "=================================================================");

        try {
            // First need to find the adopter record associated with this user
            var adopterCRUD = new com.furever.crud.AdopterCRUD();
            var adopter = adopterCRUD.getAdopterByUsername(currentUser.getUsername());

            if (adopter == null) {
                InputValidator.displayWarning("No adopter profile found for your account.");
                System.out.println(INDENT + "Would you like to create your adopter profile now?");

                String createProfile = InputValidator.getStringInput(INDENT + "Create profile? (y/n): ", false);
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
                        System.out.println(INDENT + "You can now update your profile anytime.");
                    } else {
                        InputValidator.displayError("Failed to create adopter profile.");
                        return;
                    }
                } else {
                    System.out.println(INDENT + "Profile creation cancelled. Please contact an administrator.");
                    return;
                }
            }

            System.out.println(INDENT + "Current Profile Information:");
            System.out.println(INDENT + "Name: " + adopter.getAdopterName());
            System.out.println(INDENT + "Contact: " + adopter.getAdopterContact());
            System.out.println(INDENT + "Email: " + adopter.getAdopterEmail());
            System.out.println(INDENT + "Address: " + adopter.getAdopterAddress());

            System.out.println(INDENT + "\nEnter new information (press Enter to keep current value):");

            String contact = InputValidator.getStringInput(INDENT + "Contact [" + adopter.getAdopterContact() + "]: ", true);
            if (!contact.isEmpty()) {
                adopter.setAdopterContact(contact);
            }

            String email = InputValidator.getStringInput(INDENT + "Email [" + adopter.getAdopterEmail() + "]: ", true);
            if (!email.isEmpty()) {
                adopter.setAdopterEmail(email);
            }

            String address = InputValidator.getStringInput(INDENT + "Address [" + adopter.getAdopterAddress() + "]: ", true);
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
        System.out.println(INDENT + "=================================================================");
        System.out.println(InputValidator.PURPLE + INDENT + "                        MANAGE MY PETS" + InputValidator.RESET);
        System.out.println(INDENT + "=================================================================");

        try {
            // Get current pet owner profile
            PetOwner petOwner = petOwnerCRUD.getPetOwnerByUsername(currentUser.getUsername());

            if (petOwner == null) {
                InputValidator.displayWarning("No pet owner profile found for your account.");
                System.out.println(INDENT + "Creating pet owner profile...");

                // Create automatic profile
                if (petOwnerCRUD.createPetOwnerProfileForUser(
                        currentUser.getUsername(), 
                        currentUser.getUsername(),
                        currentUser.getEmail())) {
                    InputValidator.displaySuccess( "Pet owner profile created successfully!");
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
                System.out.println(INDENT + "You don't have any registered pets yet.");
                System.out.println(INDENT + "Use 'Register New Pet' to add pets to the system.");
                return;
            }

            System.out.println(INDENT + "Your Registered Pets:");
            System.out.printf(INDENT + "%-5s %-20s %-8s %-10s %-12s %-15s%n",
                    "ID", "Name", "Age", "Gender", "Health", "Status");
            System.out.println(INDENT + "-".repeat(65));

            for (var pet : myPets) {
                System.out.printf(INDENT + "%-5d %-20s %-8d %-10s %-12s %-15s%n",
                        pet.getPetId(),
                        pet.getPetName(),
                        pet.getAge(),
                        pet.getGender(),
                        pet.getHealthStatus(),
                        pet.getAdoptionStatus()); 
            }

            System.out.println(INDENT + "\nPet Management Options:");
            System.out.println(INDENT + "1. View detailed pet information");
            System.out.println(INDENT + "2. Update pet information");
            System.out.println(INDENT + "3. Change adoption status");
            System.out.println(INDENT + "4. Return to main menu");

            int choice = InputValidator.getIntInput(INDENT + "Choose an option (1-4): ", 1, 4);

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
        InputValidator.displayHeader(INDENT + "REGISTER NEW PET");

        try {
            // Get current pet owner profile
            PetOwner petOwner = petOwnerCRUD.getPetOwnerByUsername(currentUser.getUsername());

            if (petOwner == null) {
                InputValidator.displayWarning("No pet owner profile found for your account.");
                System.out.println(INDENT + "Creating pet owner profile...");

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

            System.out.println(INDENT + "Enter new pet details:");

            String petName = InputValidator.getStringInput(INDENT + "Pet Name: ", false);
            int age = InputValidator.getIntInput(INDENT + "Age (years): ", 0, 30);
            String gender = InputValidator.getStringInput(INDENT + "Gender (Male/Female): ", false);

            // Health Status selection
            System.out.println(INDENT + "\nHealth Status Options:");
            System.out.println(INDENT + "1. Healthy");
            System.out.println(INDENT + "2. Needs Treatment");
            int healthChoice = InputValidator.getIntInput(INDENT + "Select health status (1-2): ", 1, 2);
            String healthStatus = healthChoice == 1 ? "Healthy" : "Needs Treatment";

            String description = InputValidator.getStringInput("Description: ", true);

            // Get available pet types
            var petTypeCRUD = new com.furever.crud.PetTypeCRUD();
            var petTypes = petTypeCRUD.getAllPetTypes();

            if (petTypes.isEmpty()) {
                InputValidator.displayError("No pet types available. Please contact an administrator.");
                return;
            }

            System.out.println(INDENT + "\nAvailable Pet Types:");
            for (int i = 0; i < petTypes.size(); i++) {
                System.out.println((i + 1) + ". " + petTypes.get(i).getPetTypeName());
            }

            int typeChoice = InputValidator.getIntInput(INDENT + "Select pet type (1-" + petTypes.size() + "): ", 1, petTypes.size());
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
                System.out.println(INDENT + "Your pet '" + petName + "' has been added to the system and is available for adoption.");
            } else {
                InputValidator.displayError("Failed to register pet. Please try again.");
            }

        } catch (Exception e) {
            InputValidator.displayError("Error registering pet: " + e.getMessage());
        }
    }

    /**
     * Pet Owner functionality - View adoption requests Adoption Request Management
     */
    private void viewAdoptionRequestsForMyPets() {
        System.out.println(INDENT + "=================================================================");
        System.out.println(InputValidator.PURPLE + INDENT + "                 ADOPTION REQUESTS FOR MY PETS" + InputValidator.RESET);
        System.out.println(INDENT + "=================================================================");

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
                System.out.println(INDENT + "You don't have any registered pets yet.");
                return;
            }

            // Get adoption requests for all owned pets
            var requestCRUD = new com.furever.crud.AdoptionRequestCRUD();
            boolean hasRequests = false;

            for (var pet : myPets) {
                var requests = requestCRUD.getAdoptionRequestsByPet(pet.getPetId());

                if (!requests.isEmpty()) {
                    if (!hasRequests) {
                        System.out.println(INDENT + "Adoption Requests for Your Pets:");
                        System.out.printf(INDENT + "%-10s %-20s %-20s %-15s %-12s%n",
                                "Request ID", "Pet Name", "Adopter", "Status", "Date");
                        System.out.println(INDENT + "-".repeat(65));
                        hasRequests = true;
                    }

                    for (var request : requests) {
                        // Get adopter information
                        var adopterCRUD = new com.furever.crud.AdopterCRUD();
                        var adopter = adopterCRUD.getAdopterById(request.getAdopterId());

                        System.out.printf(INDENT + "%-10d %-20s %-20s %-15s %-12s%n",
                                request.getAdoptionRequestId(),
                                pet.getPetName(),
                                adopter != null ? adopter.getAdopterName() : INDENT + "Unknown",
                                request.getStatus(),
                                request.getRequestDate());
                    }
                }
            }

            if (!hasRequests) {
                System.out.println(INDENT + "No adoption requests found for your pets.");
                return;
            }

            System.out.println(INDENT + "\nOptions:");
            System.out.println(INDENT + "1. View detailed request information");
            System.out.println(INDENT + "2. Approve/Reject requests");
            System.out.println(INDENT + "3. Return to main menu");

            int choice = InputValidator.getIntInput(INDENT + "Choose an option (1-3): ", 1, 3);

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
        System.out.println(INDENT + "=================================================================");
        System.out.println(InputValidator.PURPLE + INDENT + "                   UPDATE PET OWNER PROFILE" + InputValidator.RESET);
        System.out.println(INDENT + "=================================================================");

        try {
            // Get current pet owner profile
            PetOwner petOwner = petOwnerCRUD.getPetOwnerByUsername(currentUser.getUsername());

            if (petOwner == null) {
                InputValidator.displayWarning("No pet owner profile found for your account.");
                System.out.println(INDENT + "Creating pet owner profile...");

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

            System.out.println(INDENT + "Current Profile Information:");
            System.out.println(INDENT + "Name: " + petOwner.getPetOwnerName());
            System.out.println(INDENT + "Contact: " + petOwner.getPetOwnerContact());
            System.out.println(INDENT + "Email: " + petOwner.getPetOwnerEmail());
            System.out.println(INDENT + "Address: " + petOwner.getPetOwnerAddress());
            System.out.println(INDENT + "Profile/Bio: " + petOwner.getPetOwnerProfile());

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

            String profile = InputValidator.getStringInput(INDENT + "Profile/Bio [" + petOwner.getPetOwnerProfile() + "]: ", true);
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
        int petId = InputValidator.getIntInput(INDENT + "Enter Pet ID to view details: ", 1, Integer.MAX_VALUE);

        var selectedPet = pets.stream()
                .filter(pet -> pet.getPetId() == petId)
                .findFirst()
                .orElse(null);

        if (selectedPet == null) {
            InputValidator.displayError("Pet not found or you don't own this pet.");
            return;
        }

        System.out.println(INDENT + "\n=== PET DETAILS ===");
        System.out.println(INDENT + "ID: " + selectedPet.getPetId());
        System.out.println(INDENT + "Name: " + selectedPet.getPetName());
        System.out.println(INDENT + "Age: " + selectedPet.getAge() + " years");
        System.out.println(INDENT + "Gender: " + selectedPet.getGender());
        System.out.println(INDENT + "Health Status: " + selectedPet.getHealthStatus());
        System.out.println(INDENT + "Description: " + selectedPet.getDescription());
        System.out.println(INDENT + "Adoption Status: " + selectedPet.getAdoptionStatus());
        System.out.println(INDENT + "Registration Date: " + selectedPet.getDateRegistered());
    }

    /**
     * Helper method to update pet information
     */
    private void updatePetInformation(java.util.List<com.furever.models.Pet> pets) {
        int petId = InputValidator.getIntInput(INDENT + "Enter Pet ID to update: ", 1, Integer.MAX_VALUE);

        var selectedPet = pets.stream()
                .filter(pet -> pet.getPetId() == petId)
                .findFirst()
                .orElse(null);

        if (selectedPet == null) {
            InputValidator.displayError("Pet not found or you don't own this pet.");
            return;
        }

        System.out.println(INDENT + "Current Pet Information:");
        System.out.println(INDENT + "Name: " + selectedPet.getPetName());
        System.out.println(INDENT + "Age: " + selectedPet.getAge());
        System.out.println(INDENT + "Health Status: " + selectedPet.getHealthStatus());
        System.out.println(INDENT + "Description: " + selectedPet.getDescription());

        System.out.println(INDENT + "\nEnter new information (press Enter to keep current value):");

        String name = InputValidator.getStringInput(INDENT + "Name [" + selectedPet.getPetName() + "]: ", true);
        if (!name.isEmpty()) {
            selectedPet.setPetName(name);
        }

        String ageStr = InputValidator.getStringInput(INDENT + "Age [" + selectedPet.getAge() + "]: ", true);
        if (!ageStr.isEmpty()) {
            try {
                int age = Integer.parseInt(ageStr);
                selectedPet.setAge(age);
            } catch (NumberFormatException e) {
                InputValidator.displayError("Invalid age format.");
                return;
            }
        }

        String health = InputValidator.getStringInput(INDENT + "Health Status [" + selectedPet.getHealthStatus() + "] - Enter to keep current, or type new: ", true);
        if (!health.isEmpty()) {
            // Validate health status
            if ("Healthy".equalsIgnoreCase(health) || "Needs Treatment".equalsIgnoreCase(health)) {
                selectedPet.setHealthStatus(health);
            } else {
                System.out.println(INDENT + "Invalid health status. Valid options are: 'Healthy' or 'Needs Treatment'");
                System.out.println(INDENT + "Health status not updated.");
            }
        }

        String description = InputValidator.getStringInput(INDENT + "Description [" + selectedPet.getDescription() + "]: ", true);
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
        int petId = InputValidator.getIntInput(INDENT + "Enter Pet ID to change status: ", 1, Integer.MAX_VALUE);

        var selectedPet = pets.stream()
                .filter(pet -> pet.getPetId() == petId)
                .findFirst()
                .orElse(null);

        if (selectedPet == null) {
            InputValidator.displayError("Pet not found or you don't own this pet.");
            return;
        }

        System.out.println(INDENT + "Current Status: " + selectedPet.getAdoptionStatus());
        System.out.println(INDENT + "\nAvailable statuses:");
        System.out.println(INDENT + "1. Available");
        System.out.println(INDENT + "2. Pending");
        System.out.println(INDENT + "3. Adopted");

        int choice = InputValidator.getIntInput("Select new status (1-3): ", 1, 3);

        String newStatus = switch (choice) {
            case 1 ->
                "Available";
            case 2 ->
                "Pending";
            case 3 ->
                "Adopted";
            default ->
                selectedPet.getAdoptionStatus();
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
        int requestId = InputValidator.getIntInput(INDENT + "Enter Request ID to view details: ", 1, Integer.MAX_VALUE);

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

            System.out.println(INDENT + "\n=== ADOPTION REQUEST DETAILS ===");
            System.out.println(INDENT + "Request ID: " + request.getAdoptionRequestId());
            System.out.println(INDENT + "Pet: " + (pet != null ? pet.getPetName() : "Unknown"));
            System.out.println(INDENT + "Adopter: " + (adopter != null ? adopter.getAdopterName() : "Unknown"));
            System.out.println(INDENT + "Adopter Contact: " + (adopter != null ? adopter.getAdopterContact() : "Unknown"));
            System.out.println(INDENT + "Adopter Email: " + (adopter != null ? adopter.getAdopterEmail() : "Unknown"));
            System.out.println(INDENT + "Request Date: " + request.getRequestDate());
            System.out.println(INDENT + "Status: " + request.getStatus());
            System.out.println(INDENT + "Remarks: " + request.getRemarks());

        } catch (Exception e) {
            InputValidator.displayError("Error viewing request details: " + e.getMessage());
        }
    }

    /**
     * Helper method to process (approve/reject) adoption requests
     */
    private void processAdoptionRequests(java.util.List<com.furever.models.Pet> pets) {
        int requestId = InputValidator.getIntInput(INDENT + "Enter Request ID to process: ", 1, Integer.MAX_VALUE);

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

            System.out.println(INDENT + "Current Status: " + request.getStatus());
            System.out.println(INDENT + "\nOptions:");
            System.out.println(INDENT + "1. Approve Request");
            System.out.println(INDENT + "2. Reject Request");
            System.out.println(INDENT + "3. Cancel");

            int choice = InputValidator.getIntInput(INDENT + "Choose action (1-3): ", 1, 3);

            if (choice == 3) {
                return;
            }

            String newStatus = choice == 1 ? INDENT + "Approved" : "Rejected";
            String remarks = InputValidator.getStringInput(INDENT + "Add remarks (optional): ", true);

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
                        System.out.println(INDENT + "Pet status updated to 'Adopted'.");
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

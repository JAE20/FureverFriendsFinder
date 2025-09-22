/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.furever.dashboard;

/**
 *
 * @author jerimiahtongco
 */
import com.furever.crud.UserCRUD;
import com.furever.models.User;
import com.furever.utils.InputValidator;
import java.util.List;

/**
 * Dashboard for managing User operations
 */
public class UserDashboard {
    
    private final UserCRUD userCRUD;
    
    public UserDashboard() {
        this.userCRUD = new UserCRUD();
    }
    
    /**
     * Displays the main user management menu
     */
    public void showUserMenu() {
        while (true) {
            InputValidator.displayHeader("USER MANAGEMENT DASHBOARD");
            System.out.println("1. Add New User");
            System.out.println("2. View All Users");
            System.out.println("3. Search User by ID");
            System.out.println("4. Search User by Username");
            System.out.println("5. Update User");
            System.out.println("6. Delete User");
            System.out.println("7. View User Statistics");
            System.out.println("8. Return to Main Menu");
            System.out.println("-".repeat(60));
            
            int choice = InputValidator.getIntInput("Enter your choice (1-8): ", 1, 8);
            
            switch (choice) {
                case 1:
                    addUser();
                    break;
                case 2:
                    viewAllUsers();
                    break;
                case 3:
                    searchUserById();
                    break;
                case 4:
                    searchUserByUsername();
                    break;
                case 5:
                    updateUser();
                    break;
                case 6:
                    deleteUser();
                    break;
                case 7:
                    viewUserStatistics();
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
     * Adds a new user
     */
    private void addUser() {
        InputValidator.displayHeader("ADD NEW USER");
        
        try {
            String username = InputValidator.getStringInput("Enter username: ", 3, 50);
            
            // Check if username already exists
            if (userCRUD.getUserByUsername(username) != null) {
                InputValidator.displayError("Username already exists. Please choose a different username.");
                return;
            }
            
            String email = InputValidator.getEmailInput("Enter email: ");
            String password = InputValidator.getStringInput("Enter password: ", 6, 255);
            
            System.out.println("\nSelect user role:");
            System.out.println("1. Admin");
            System.out.println("2. Adopter");
            int roleChoice = InputValidator.getIntInput("Enter role choice (1-2): ", 1, 2);
            String role = (roleChoice == 1) ? "admin" : "adopter";
            
            User user = new User(username, email, password, role);
            
            if (userCRUD.createUser(user)) {
                InputValidator.displaySuccess("User created successfully!");
                System.out.println("User ID: " + user.getId());
            } else {
                InputValidator.displayError("Failed to create user.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while adding user: " + e.getMessage());
        }
    }
    
    /**
     * Views all users
     */
    private void viewAllUsers() {
        InputValidator.displayHeader("ALL USERS");
        
        try {
            List<User> users = userCRUD.getAllUsers();
            
            if (users.isEmpty()) {
                System.out.println("No users found.");
                return;
            }
            
            System.out.printf("%-5s %-20s %-30s %-10s %-20s%n", 
                "ID", "Username", "Email", "Role", "Created At");
            System.out.println("-".repeat(85));
            
            for (User user : users) {
                System.out.printf("%-5d %-20s %-30s %-10s %-20s%n",
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole(),
                    user.getCreatedAt() != null ? user.getCreatedAt().toString() : "N/A");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while retrieving users: " + e.getMessage());
        }
    }
    
    /**
     * Searches for a user by ID
     */
    private void searchUserById() {
        InputValidator.displayHeader("SEARCH USER BY ID");
        
        try {
            int userId = InputValidator.getIntInput("Enter user ID: ");
            User user = userCRUD.getUserById(userId);
            
            if (user != null) {
                displayUserDetails(user);
            } else {
                InputValidator.displayWarning("No user found with ID: " + userId);
            }
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while searching user: " + e.getMessage());
        }
    }
    
    /**
     * Searches for users by username using LIKE pattern matching
     */
    private void searchUserByUsername() {
        InputValidator.displayHeader("SEARCH USER BY USERNAME");
        
        try {
            String usernamePattern = InputValidator.getStringInput("Enter username pattern to search (partial match supported): ", false);
            List<User> users = userCRUD.searchUsersByUsername(usernamePattern);
            
            if (users.isEmpty()) {
                InputValidator.displayWarning("No users found matching: " + usernamePattern);
                return;
            }
            
            System.out.println("Search Results:");
            System.out.printf("%-5s %-20s %-30s %-10s%n", 
                "ID", "Username", "Email", "Role");
            System.out.println("-".repeat(65));
            
            for (User user : users) {
                System.out.printf("%-5d %-20s %-30s %-10s%n",
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole());
            }
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while searching users: " + e.getMessage());
        }
    }
    
    /**
     * Updates an existing user
     */
    private void updateUser() {
        InputValidator.displayHeader("UPDATE USER");
        
        try {
            int userId = InputValidator.getIntInput("Enter user ID to update: ");
            User user = userCRUD.getUserById(userId);
            
            if (user == null) {
                InputValidator.displayWarning("No user found with ID: " + userId);
                return;
            }
            
            System.out.println("Current user details:");
            displayUserDetails(user);
            
            System.out.println("\nEnter new information (press Enter to keep current value):");
            
            String username = InputValidator.getStringInput("Username [" + user.getUsername() + "]: ", true);
            if (!username.isEmpty() && !username.equals(user.getUsername())) {
                // Check if new username already exists
                if (userCRUD.getUserByUsername(username) != null) {
                    InputValidator.displayError("Username already exists. Please choose a different username.");
                    return;
                }
                user.setUsername(username);
            }
            
            String email = InputValidator.getStringInput("Email [" + user.getEmail() + "]: ", true);
            if (!email.isEmpty()) {
                user.setEmail(email);
            }
            
            String password = InputValidator.getStringInput("Password [Hidden]: ", true);
            if (!password.isEmpty()) {
                user.setPassword(password);
            }
            
            String currentRole = user.getRole();
            System.out.println("Current role: " + currentRole);
            System.out.println("1. Admin");
            System.out.println("2. Adopter");
            System.out.println("3. Keep current role");
            int roleChoice = InputValidator.getIntInput("Enter role choice (1-3): ", 1, 3);
            
            if (roleChoice == 1) {
                user.setRole("admin");
            } else if (roleChoice == 2) {
                user.setRole("adopter");
            }
            
            if (userCRUD.updateUser(user)) {
                InputValidator.displaySuccess("User updated successfully!");
            } else {
                InputValidator.displayError("Failed to update user.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while updating user: " + e.getMessage());
        }
    }
    
    /**
     * Deletes a user
     */
    private void deleteUser() {
        InputValidator.displayHeader("DELETE USER");
        
        try {
            int userId = InputValidator.getIntInput("Enter user ID to delete: ");
            User user = userCRUD.getUserById(userId);
            
            if (user == null) {
                InputValidator.displayWarning("No user found with ID: " + userId);
                return;
            }
            
            System.out.println("User to be deleted:");
            displayUserDetails(user);
            
            if (InputValidator.getConfirmation("Are you sure you want to delete this user?")) {
                if (userCRUD.deleteUser(userId)) {
                    InputValidator.displaySuccess("User deleted successfully!");
                } else {
                    InputValidator.displayError("Failed to delete user.");
                }
            } else {
                System.out.println("Deletion cancelled.");
            }
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while deleting user: " + e.getMessage());
        }
    }
    
    /**
     * Views user statistics
     */
    private void viewUserStatistics() {
        InputValidator.displayHeader("USER STATISTICS");
        
        try {
            int totalUsers = userCRUD.getUserCount();
            System.out.println("Total Users: " + totalUsers);
            
            // Additional statistics can be added here
            List<User> allUsers = userCRUD.getAllUsers();
            
            long adminCount = allUsers.stream().filter(u -> "admin".equals(u.getRole())).count();
            long adopterCount = allUsers.stream().filter(u -> "adopter".equals(u.getRole())).count();
            
            System.out.println("Admin Users: " + adminCount);
            System.out.println("Adopter Users: " + adopterCount);
            
        } catch (Exception e) {
            InputValidator.displayError("An error occurred while retrieving statistics: " + e.getMessage());
        }
    }
    
    /**
     * Displays detailed information about a user
     * @param user User object to display
     */
    private void displayUserDetails(User user) {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("User Details:");
        System.out.println("-".repeat(40));
        System.out.println("ID: " + user.getId());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Role: " + user.getRole());
        System.out.println("Created At: " + (user.getCreatedAt() != null ? user.getCreatedAt().toString() : "N/A"));
        System.out.println("-".repeat(40));
    }
}
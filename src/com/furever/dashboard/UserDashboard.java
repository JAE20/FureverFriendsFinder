/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.furever.dashboard;

/**
 *
 * @author jerimiahtongcouser role: Select user role:
 */
import com.furever.crud.UserCRUD;
import com.furever.models.User;
import com.furever.utils.InputValidator;
import java.util.List;


public class UserDashboard {

    private static final String INDENT = "\t\t\t\t\t";
    private static final String INDENT1 = "\t\t\t\t\t\t";
    private final UserCRUD userCRUD;

    public UserDashboard() {
        this.userCRUD = new UserCRUD();
    }

    /**
     * Displays the main user management menu
     */
    public void showUserMenu() {
        while (true) {
            System.out.println(INDENT + "=================================================================");
            System.out.println(InputValidator.BLUE + INDENT + "                    USER MANAGEMENT DASHBOARD" + InputValidator.RESET);
            System.out.println(INDENT + "=================================================================");
            System.out.println(INDENT + "1. Add New User");
            System.out.println(INDENT + "2. View All Users");
            System.out.println(INDENT + "3. Search User by ID");
            System.out.println(INDENT + "4. Search User by Username");
            System.out.println(INDENT + "5. Update User");
            System.out.println(INDENT + "6. Delete User");
            System.out.println(INDENT + "7. View User Statistics");
            System.out.println(INDENT + "8. Return to Main Menu");
            System.out.println(INDENT + "-".repeat(65));

            int choice = InputValidator.getIntInput(INDENT + "Enter your choice (1-8): ", 1, 8);

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
        InputValidator.displayHeader(InputValidator.BLUE + INDENT1 + "ADD NEW USER" + InputValidator.RESET);

        try {
            String username = InputValidator.getStringInput(INDENT + "Enter username: ", 3, 50);

            // Check if username already exists
            if (userCRUD.getUserByUsername(username) != null) {
                InputValidator.displayError("Username already exists. Please choose a different username.");
                return;
            }

            String email = InputValidator.getEmailInput(INDENT + "Enter email: ");
            String password = InputValidator.getStringInput(INDENT + "Enter password: ", 6, 255);

            System.out.println(INDENT + "Select user role:");
            System.out.println(INDENT + "1. Admin");
            System.out.println(INDENT + "2. Adopter");
            int roleChoice = InputValidator.getIntInput(INDENT + "Enter role choice (1-2): ", 1, 2);
            String role = (roleChoice == 1) ? "admin" : "adopter";

            User user = new User(username, email, password, role);

            if (userCRUD.createUser(user)) {
                InputValidator.displaySuccess("User created successfully!");
                System.out.println(INDENT + "User ID: " + user.getId());
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
        InputValidator.displayHeader(InputValidator.BLUE + INDENT1 + "ALL USERS" + InputValidator.RESET);

        try {
            List<User> users = userCRUD.getAllUsers();

            if (users.isEmpty()) {
                System.out.println(INDENT + "No users found.");
                return;
            }

            System.out.printf(INDENT + "%-5s %-20s %-30s %-10s %-20s%n",
                    "ID", "Username", "Email", "Role", "Created At");
            System.out.println(INDENT + "-".repeat(65));

            for (User user : users) {
                System.out.printf(INDENT + "%-5d %-20s %-30s %-10s %-20s%n",
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
        InputValidator.displayHeader(InputValidator.BLUE + INDENT1 + "SEARCH USER BY ID" + InputValidator.RESET);

        try {
            int userId = InputValidator.getIntInput(INDENT + "Enter user ID: ");
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
     * search ng mga users by username using LIKE
     */
    private void searchUserByUsername() {
        InputValidator.displayHeader(InputValidator.BLUE + INDENT + "SEARCH USER BY USERNAME" + InputValidator.RESET);

        try {
            String usernamePattern = InputValidator.getStringInput(INDENT + "Enter username pattern to search (partial match supported): ", false);
            List<User> users = userCRUD.searchUsersByUsername(usernamePattern);

            if (users.isEmpty()) {
                InputValidator.displayWarning("No users found matching: " + usernamePattern);
                return;
            }

            System.out.println(INDENT + "Search Results:");
            System.out.printf(INDENT + "%-5s %-20s %-30s %-10s%n",
                    "ID", "Username", "Email", "Role");
            System.out.println(INDENT + "-".repeat(65));

            for (User user : users) {
                System.out.printf(INDENT + "%-5d %-20s %-30s %-10s%n",
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
        InputValidator.displayHeader(InputValidator.BLUE + INDENT + "UPDATE USER" + InputValidator.RESET);

        try {
            int userId = InputValidator.getIntInput(INDENT + "Enter user ID to update: ");
            User user = userCRUD.getUserById(userId);

            if (user == null) {
                InputValidator.displayWarning("No user found with ID: " + userId);
                return;
            }

            System.out.println(INDENT + "Current user details:");
            displayUserDetails(user);

            System.out.println(INDENT + "\nEnter new information (press Enter to keep current value):");

            String username = InputValidator.getStringInput(INDENT + "Username [" + user.getUsername() + "]: ", true);
            if (!username.isEmpty() && !username.equals(user.getUsername())) {
                // Check if new username already exists
                if (userCRUD.getUserByUsername(username) != null) {
                    InputValidator.displayError("Username already exists. Please choose a different username.");
                    return;
                }
                user.setUsername(username);
            }

            String email = InputValidator.getStringInput(INDENT + "Email [" + user.getEmail() + "]: ", true);
            if (!email.isEmpty()) {
                user.setEmail(email);
            }

            String password = InputValidator.getStringInput(INDENT + "Password [Hidden]: ", true);
            if (!password.isEmpty()) {
                user.setPassword(password);
            }

            String currentRole = user.getRole();
            System.out.println(INDENT + "Current role: " + currentRole);
            System.out.println(INDENT + "1. Admin");
            System.out.println(INDENT + "2. Adopter");
            System.out.println(INDENT + "3. Keep current role");
            int roleChoice = InputValidator.getIntInput(INDENT + "Enter role choice (1-3): ", 1, 3);

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
        InputValidator.displayHeader(InputValidator.BLUE + INDENT1 + "DELETE USER" + InputValidator.RESET);

        try {
            int userId = InputValidator.getIntInput(INDENT + "Enter user ID to delete: ");
            User user = userCRUD.getUserById(userId);

            if (user == null) {
                InputValidator.displayWarning("No user found with ID: " + userId);
                return;
            }

            System.out.println(INDENT + "User to be deleted:");
            displayUserDetails(user);

            if (InputValidator.getConfirmation(INDENT + "Are you sure you want to delete this user?")) {
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
        InputValidator.displayHeader(InputValidator.BLUE + INDENT1 + "USER STATISTICS" + InputValidator.RESET);

        try {
            int totalUsers = userCRUD.getUserCount();
            System.out.println(INDENT + "Total Users: " + totalUsers);

            // Additional statistics can be added here
            List<User> allUsers = userCRUD.getAllUsers();

            long adminCount = allUsers.stream().filter(u -> "admin".equals(u.getRole())).count();
            long adopterCount = allUsers.stream().filter(u -> "adopter".equals(u.getRole())).count();

            System.out.println(INDENT + "Admin Users: " + adminCount);
            System.out.println(INDENT + "Adopter Users: " + adopterCount);

        } catch (Exception e) {
            InputValidator.displayError("An error occurred while retrieving statistics: " + e.getMessage());
        }
    }

    /**
     * Displays detailed information about a user
     *
     * @param user User object to display
     */
    private void displayUserDetails(User user) {
        System.out.println(INDENT + "\n" + "-".repeat(40));
        System.out.println(INDENT + "User Details:");
        System.out.println(INDENT + "-".repeat(40));
        System.out.println(INDENT + "ID: " + user.getId());
        System.out.println(INDENT + "Username: " + user.getUsername());
        System.out.println(INDENT + "Email: " + user.getEmail());
        System.out.println(INDENT + "Role: " + user.getRole());
        System.out.println(INDENT + "Created At: " + (user.getCreatedAt() != null ? user.getCreatedAt().toString() : "N/A"));
        System.out.println(INDENT + "-".repeat(40));
    }
}

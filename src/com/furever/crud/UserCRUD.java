/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.furever.crud;

/**
 *
 * @author jerimiahtongco
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.furever.database.DbConnection;
import com.furever.models.User;

/**
 * CRUD operations for User entity
 */
public class UserCRUD {
    
    /**
     * Creates a new user in the database and automatically creates corresponding profile
     * @param user User object to create
     * @return true if user was created successfully, false otherwise
     */
    public boolean createUser(User user) {
        String sql = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        
        try {
            conn = DbConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
                
                // Automatically create corresponding profile based on role
                boolean profileCreated = true;
                if ("adopter".equals(user.getRole())) {
                    profileCreated = createAdopterProfile(conn, user);
                } else if ("pet_owner".equals(user.getRole())) {
                    profileCreated = createPetOwnerProfile(conn, user);
                }
                
                if (profileCreated) {
                    conn.commit(); // Commit transaction
                    System.out.println("User created successfully with ID: " + user.getId());
                    if (!"admin".equals(user.getRole())) {
                        System.out.println("Corresponding " + user.getRole() + " profile created automatically.");
                    }
                    return true;
                } else {
                    conn.rollback(); // Rollback if profile creation failed
                    System.err.println("User creation rolled back due to profile creation failure.");
                }
            }
            
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }
            System.err.println("Error creating user: " + e.getMessage());
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException closeEx) {
                System.err.println("Error closing resources: " + closeEx.getMessage());
            }
        }
        
        return false;
    }
    
    /**
     * Creates an adopter profile for a user
     * @param conn Database connection (should be in transaction)
     * @param user User for whom to create adopter profile
     * @return true if profile was created successfully, false otherwise
     */
    private boolean createAdopterProfile(Connection conn, User user) throws SQLException {
        String sql = "INSERT INTO tbl_adopter (username, adopter_name, adopter_contact, adopter_email, adopter_address, adopter_username, adopter_password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername()); // Link to users.username
            pstmt.setString(2, user.getUsername()); // Use username as display name initially
            pstmt.setString(3, "09000000000"); // Default contact (user can update later)
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, "Address not provided"); // Default address
            pstmt.setString(6, user.getUsername()); // Legacy username field
            pstmt.setString(7, user.getPassword()); // Legacy password field
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    /**
     * Creates a pet owner profile for a user
     * @param conn Database connection (should be in transaction)
     * @param user User for whom to create pet owner profile
     * @return true if profile was created successfully, false otherwise
     */
    private boolean createPetOwnerProfile(Connection conn, User user) throws SQLException {
        String sql = "INSERT INTO tbl_pet_owner (username, pet_owner_name, pet_owner_contact, pet_owner_email, pet_owner_address, pet_owner_username, pet_owner_password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername()); // Link to users.username
            pstmt.setString(2, user.getUsername()); // Use username as display name initially
            pstmt.setString(3, "09000000000"); // Default contact (user can update later)
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, "Address not provided"); // Default address
            pstmt.setString(6, user.getUsername()); // Legacy username field
            pstmt.setString(7, user.getPassword()); // Legacy password field
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    /**
     * Retrieves a user by ID
     * @param userId User ID to search for
     * @return User object if found, null otherwise
     */
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving user: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Retrieves a user by username
     * @param username Username to search for
     * @return User object if found, null otherwise
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving user by username: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Searches users by username using LIKE pattern matching
     * @param usernamePattern Username pattern to search for (supports wildcards)
     * @return List of users matching the pattern
     */
    public List<User> searchUsersByUsername(String usernamePattern) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE username LIKE ? ORDER BY username";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Add wildcards for partial matching
            pstmt.setString(1, "%" + usernamePattern + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    users.add(extractUserFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching users by username: " + e.getMessage());
        }
        
        return users;
    }
    
    /**
     * Retrieves all users from the database
     * @return List of all users
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY id";
        
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving all users: " + e.getMessage());
        }
        
        return users;
    }
    
    /**
     * Updates an existing user and manages corresponding profile changes
     * @param user User object with updated information
     * @return true if user was updated successfully, false otherwise
     */
    public boolean updateUser(User user) {
        // First get the current user to check for role changes
        User currentUser = getUserById(user.getId());
        if (currentUser == null) {
            System.out.println("No user found with ID: " + user.getId());
            return false;
        }
        
        String sql = "UPDATE users SET username = ?, email = ?, password = ?, role = ? WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DbConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole());
            pstmt.setInt(5, user.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                boolean profileHandled = true;
                
                // Handle role changes
                if (!currentUser.getRole().equals(user.getRole())) {
                    System.out.println("Role changed from " + currentUser.getRole() + " to " + user.getRole());
                    
                    // Archive old profile if role changed away from adopter/pet_owner
                    if ("adopter".equals(currentUser.getRole()) && !"adopter".equals(user.getRole())) {
                        profileHandled = archiveAdopterProfile(conn, currentUser.getUsername());
                    } else if ("pet_owner".equals(currentUser.getRole()) && !"pet_owner".equals(user.getRole())) {
                        profileHandled = archivePetOwnerProfile(conn, currentUser.getUsername());
                    }
                    
                    // Create new profile if role changed to adopter/pet_owner
                    if (profileHandled) {
                        if ("adopter".equals(user.getRole()) && !"adopter".equals(currentUser.getRole())) {
                            profileHandled = createAdopterProfile(conn, user);
                        } else if ("pet_owner".equals(user.getRole()) && !"pet_owner".equals(currentUser.getRole())) {
                            profileHandled = createPetOwnerProfile(conn, user);
                        }
                    }
                } else {
                    // Same role, update profile information if needed
                    if ("adopter".equals(user.getRole())) {
                        profileHandled = updateAdopterProfile(conn, user);
                    } else if ("pet_owner".equals(user.getRole())) {
                        profileHandled = updatePetOwnerProfile(conn, user);
                    }
                }
                
                if (profileHandled) {
                    conn.commit(); // Commit transaction
                    System.out.println("User updated successfully.");
                    return true;
                } else {
                    conn.rollback(); // Rollback if profile handling failed
                    System.err.println("User update rolled back due to profile handling failure.");
                }
            } else {
                System.out.println("No user found with ID: " + user.getId());
            }
            
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }
            System.err.println("Error updating user: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException closeEx) {
                System.err.println("Error closing resources: " + closeEx.getMessage());
            }
        }
        
        return false;
    }
    
    /**
     * Archives an adopter profile (sets archived flag)
     * @param conn Database connection (should be in transaction)
     * @param username Username of the adopter to archive
     * @return true if profile was archived successfully, false otherwise
     */
    private boolean archiveAdopterProfile(Connection conn, String username) throws SQLException {
        String sql = "UPDATE tbl_adopter SET archived = 1, archived_date = NOW() WHERE username = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
            System.out.println("Adopter profile archived for username: " + username);
            return true;
        }
    }
    
    /**
     * Archives a pet owner profile (sets archived flag)
     * @param conn Database connection (should be in transaction)
     * @param username Username of the pet owner to archive
     * @return true if profile was archived successfully, false otherwise
     */
    private boolean archivePetOwnerProfile(Connection conn, String username) throws SQLException {
        String sql = "UPDATE tbl_pet_owner SET archived = 1, archived_date = NOW() WHERE username = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
            System.out.println("Pet owner profile archived for username: " + username);
            return true;
        }
    }
    
    /**
     * Updates adopter profile information based on user changes
     * @param conn Database connection (should be in transaction)
     * @param user User with updated information
     * @return true if profile was updated successfully, false otherwise
     */
    private boolean updateAdopterProfile(Connection conn, User user) throws SQLException {
        String sql = "UPDATE tbl_adopter SET adopter_email = ? WHERE username = ? AND archived = 0";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getUsername());
            pstmt.executeUpdate();
            return true;
        }
    }
    
    /**
     * Updates pet owner profile information based on user changes
     * @param conn Database connection (should be in transaction)
     * @param user User with updated information
     * @return true if profile was updated successfully, false otherwise
     */
    private boolean updatePetOwnerProfile(Connection conn, User user) throws SQLException {
        String sql = "UPDATE tbl_pet_owner SET pet_owner_email = ? WHERE username = ? AND archived = 0";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getUsername());
            pstmt.executeUpdate();
            return true;
        }
    }
    
    /**
     * Deletes a user by ID
     * @param userId User ID to delete
     * @return true if user was deleted successfully, false otherwise
     */
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("User deleted successfully.");
                return true;
            } else {
                System.out.println("No user found with ID: " + userId);
            }
            
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Authenticates a user with username and password
     * @param username Username
     * @param password Password
     * @return User object if authentication successful, null otherwise
     */
    public User authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Counts total number of users
     * @return Total count of users
     */
    public int getUserCount() {
        String sql = "SELECT COUNT(*) FROM users";
        
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error counting users: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Extracts User object from ResultSet
     * @param rs ResultSet containing user data
     * @return User object
     * @throws SQLException if database access error occurs
     */
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        return user;
    }
}
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
     * Deletes a user by ID (now archives instead of permanent deletion)
     * @param userId User ID to delete
     * @return true if user was archived successfully, false otherwise
     */
    public boolean deleteUser(int userId) {
        return archiveUser(userId, null, "User deleted via admin dashboard");
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
    
    /**
     * Archives a user by moving them to the archive table
     * @param userId ID of the user to archive
     * @param archivedByUserId ID of the user performing the archive operation
     * @param reason Reason for archiving
     * @return true if archiving was successful, false otherwise
     */
    public boolean archiveUser(int userId, Integer archivedByUserId, String reason) {
        String selectUserSql = "SELECT * FROM users WHERE id = ?";
        String insertArchiveSql = "INSERT INTO users_archive (id, username, email, password, role, created_at, archived, archived_date, archived_by_user_id, archive_reason) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String deleteUserSql = "DELETE FROM users WHERE id = ?";
        String logSql = "INSERT INTO tbl_archive_log (table_name, record_id, operation, performed_by_user_id, reason) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DbConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Get the user record to archive
            User user = null;
            try (PreparedStatement selectStmt = conn.prepareStatement(selectUserSql)) {
                selectStmt.setInt(1, userId);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        user = new User();
                        user.setId(rs.getInt("id"));
                        user.setUsername(rs.getString("username"));
                        user.setEmail(rs.getString("email"));
                        user.setPassword(rs.getString("password"));
                        user.setRole(rs.getString("role"));
                        user.setCreatedAt(rs.getTimestamp("created_at"));
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }
            
            // Insert into archive table
            try (PreparedStatement insertStmt = conn.prepareStatement(insertArchiveSql)) {
                insertStmt.setInt(1, user.getId());
                insertStmt.setString(2, user.getUsername());
                insertStmt.setString(3, user.getEmail());
                insertStmt.setString(4, user.getPassword());
                insertStmt.setString(5, user.getRole());
                insertStmt.setTimestamp(6, user.getCreatedAt());
                insertStmt.setBoolean(7, true);
                insertStmt.setTimestamp(8, new java.sql.Timestamp(System.currentTimeMillis()));
                if (archivedByUserId != null) {
                    insertStmt.setInt(9, archivedByUserId);
                } else {
                    insertStmt.setNull(9, java.sql.Types.INTEGER);
                }
                insertStmt.setString(10, reason);
                
                insertStmt.executeUpdate();
            }
            
            // Delete from main table
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteUserSql)) {
                deleteStmt.setInt(1, userId);
                deleteStmt.executeUpdate();
            }
            
            // Log the operation
            try (PreparedStatement logStmt = conn.prepareStatement(logSql)) {
                logStmt.setString(1, "users");
                logStmt.setInt(2, userId);
                logStmt.setString(3, "ARCHIVE");
                if (archivedByUserId != null) {
                    logStmt.setInt(4, archivedByUserId);
                } else {
                    logStmt.setNull(4, java.sql.Types.INTEGER);
                }
                logStmt.setString(5, reason);
                
                logStmt.executeUpdate();
            }
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error archiving user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Restores a user from archive back to the main table
     * @param userId ID of the user to restore
     * @param restoredByUserId ID of the user performing the restore operation
     * @param reason Reason for restoring
     * @return true if restoration was successful, false otherwise
     */
    public boolean restoreUser(int userId, Integer restoredByUserId, String reason) {
        String selectArchiveSql = "SELECT * FROM users_archive WHERE id = ?";
        String insertMainSql = "INSERT INTO users (id, username, email, password, role, created_at, archived, archived_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String deleteArchiveSql = "DELETE FROM users_archive WHERE id = ?";
        String logSql = "INSERT INTO tbl_archive_log (table_name, record_id, operation, performed_by_user_id, reason) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DbConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Get the user record from archive
            User user = null;
            try (PreparedStatement selectStmt = conn.prepareStatement(selectArchiveSql)) {
                selectStmt.setInt(1, userId);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        user = new User();
                        user.setId(rs.getInt("id"));
                        user.setUsername(rs.getString("username"));
                        user.setEmail(rs.getString("email"));
                        user.setPassword(rs.getString("password"));
                        user.setRole(rs.getString("role"));
                        user.setCreatedAt(rs.getTimestamp("created_at"));
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }
            
            // Insert user back into main table
            try (PreparedStatement insertStmt = conn.prepareStatement(insertMainSql)) {
                insertStmt.setInt(1, user.getId());
                insertStmt.setString(2, user.getUsername());
                insertStmt.setString(3, user.getEmail());
                insertStmt.setString(4, user.getPassword());
                insertStmt.setString(5, user.getRole());
                insertStmt.setTimestamp(6, user.getCreatedAt());
                insertStmt.setBoolean(7, false);
                insertStmt.setNull(8, java.sql.Types.TIMESTAMP);
                
                insertStmt.executeUpdate();
            }
            
            // Delete from archive table
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteArchiveSql)) {
                deleteStmt.setInt(1, userId);
                deleteStmt.executeUpdate();
            }
            
            // Log the operation
            try (PreparedStatement logStmt = conn.prepareStatement(logSql)) {
                logStmt.setString(1, "users");
                logStmt.setInt(2, userId);
                logStmt.setString(3, "RESTORE");
                if (restoredByUserId != null) {
                    logStmt.setInt(4, restoredByUserId);
                } else {
                    logStmt.setNull(4, java.sql.Types.INTEGER);
                }
                logStmt.setString(5, reason);
                
                logStmt.executeUpdate();
            }
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error restoring user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Permanently deletes a user from the archive table
     * @param userId ID of the user to permanently delete
     * @param deletedByUserId ID of the user performing the permanent deletion
     * @param reason Reason for permanent deletion
     * @return true if permanent deletion was successful, false otherwise
     */
    public boolean permanentDeleteUser(int userId, Integer deletedByUserId, String reason) {
        String deleteUserSql = "DELETE FROM users_archive WHERE id = ?";
        String logSql = "INSERT INTO tbl_archive_log (table_name, record_id, operation, performed_by_user_id, reason) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DbConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Delete user from archive table
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteUserSql)) {
                deleteStmt.setInt(1, userId);
                int rowsAffected = deleteStmt.executeUpdate();
                
                if (rowsAffected == 0) {
                    conn.rollback();
                    return false;
                }
            }
            
            // Log the operation
            try (PreparedStatement logStmt = conn.prepareStatement(logSql)) {
                logStmt.setString(1, "users");
                logStmt.setInt(2, userId);
                logStmt.setString(3, "PERMANENT_DELETE");
                if (deletedByUserId != null) {
                    logStmt.setInt(4, deletedByUserId);
                } else {
                    logStmt.setNull(4, java.sql.Types.INTEGER);
                }
                logStmt.setString(5, reason);
                
                logStmt.executeUpdate();
            }
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error permanently deleting user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Gets all archived users
     * @return List of archived users
     */
    public List<User> getAllArchivedUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users_archive ORDER BY archived_date DESC";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                users.add(user);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving archived users: " + e.getMessage());
        }
        
        return users;
    }
}
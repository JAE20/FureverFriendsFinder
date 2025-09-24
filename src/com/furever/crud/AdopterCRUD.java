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
import com.furever.models.Adopter;

/**
 * CRUD operations for Adopter entity
 */
public class AdopterCRUD {
    
    /**
     * Creates a new adopter in the database
     * @param adopter Adopter object to create
     * @return true if adopter was created successfully, false otherwise
     */
    public boolean createAdopter(Adopter adopter) {
        String sql = "INSERT INTO tbl_adopter (adopter_name, adopter_contact, adopter_email, adopter_address, adopter_profile, adopter_username, adopter_password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, adopter.getAdopterName());
            pstmt.setString(2, adopter.getAdopterContact());
            pstmt.setString(3, adopter.getAdopterEmail());
            pstmt.setString(4, adopter.getAdopterAddress());
            pstmt.setString(5, adopter.getAdopterProfile());
            pstmt.setString(6, adopter.getAdopterUsername());
            pstmt.setString(7, adopter.getAdopterPassword());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        adopter.setAdopterId(generatedKeys.getInt(1));
                    }
                }
                System.out.println("Adopter created successfully with ID: " + adopter.getAdopterId());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating adopter: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Retrieves an adopter by ID
     * @param adopterId Adopter ID to search for
     * @return Adopter object if found, null otherwise
     */
    public Adopter getAdopterById(int adopterId) {
        String sql = "SELECT * FROM tbl_adopter WHERE adopter_id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, adopterId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractAdopterFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving adopter: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Retrieves an adopter by username
     * @param username Username to search for
     * @return Adopter object if found, null otherwise
     */
    public Adopter getAdopterByUsername(String username) {
        String sql = "SELECT * FROM tbl_adopter WHERE adopter_username = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractAdopterFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving adopter by username: " + e.getMessage());
        }
        
        return null;
    }
    
    
    
    /**
     * Searches adopters by username using LIKE pattern matching
     * @param usernamePattern Username pattern to search for (supports wildcards)
     * @return List of adopters matching the pattern
     */
    public List<Adopter> searchAdoptersByUsername(String usernamePattern) {
        List<Adopter> adopters = new ArrayList<>();
        String sql = "SELECT * FROM tbl_adopter WHERE adopter_username LIKE ? ORDER BY adopter_username";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Add wildcards for partial matching
            pstmt.setString(1, "%" + usernamePattern + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    adopters.add(extractAdopterFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching adopters by username: " + e.getMessage());
        }
        
        return adopters;
    }
    
    
    /**
     * Retrieves all adopters from the database
     * @return List of all adopters
     */
    public List<Adopter> getAllAdopters() {
        List<Adopter> adopters = new ArrayList<>();
        String sql = "SELECT * FROM tbl_adopter ORDER BY adopter_id";
        
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                adopters.add(extractAdopterFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving all adopters: " + e.getMessage());
        }
        
        return adopters;
    }
    
    /**
     * Updates an existing adopter
     * @param adopter Adopter object with updated information
     * @return true if adopter was updated successfully, false otherwise
     */
    public boolean updateAdopter(Adopter adopter) {
        String sql = "UPDATE tbl_adopter SET adopter_name = ?, adopter_contact = ?, adopter_email = ?, adopter_address = ?, adopter_profile = ?, adopter_username = ?, adopter_password = ? WHERE adopter_id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, adopter.getAdopterName());
            pstmt.setString(2, adopter.getAdopterContact());
            pstmt.setString(3, adopter.getAdopterEmail());
            pstmt.setString(4, adopter.getAdopterAddress());
            pstmt.setString(5, adopter.getAdopterProfile());
            pstmt.setString(6, adopter.getAdopterUsername());
            pstmt.setString(7, adopter.getAdopterPassword());
            pstmt.setInt(8, adopter.getAdopterId());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Adopter updated successfully.");
                return true;
            } else {
                System.out.println("No adopter found with ID: " + adopter.getAdopterId());
            }
            
        } catch (SQLException e) {
            System.err.println("Error updating adopter: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Deletes an adopter by ID
     * @param adopterId Adopter ID to delete
     * @return true if adopter was deleted successfully, false otherwise
     */
    public boolean deleteAdopter(int adopterId) {
        String sql = "DELETE FROM tbl_adopter WHERE adopter_id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, adopterId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Adopter deleted successfully.");
                return true;
            } else {
                System.out.println("No adopter found with ID: " + adopterId);
            }
            
        } catch (SQLException e) {
            System.err.println("Error deleting adopter: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Searches adopters by name
     * @param searchTerm Search term to match against adopter names
     * @return List of adopters matching the search term
     */
    public List<Adopter> searchAdoptersByName(String searchTerm) {
        List<Adopter> adopters = new ArrayList<>();
        String sql = "SELECT * FROM tbl_adopter WHERE adopter_name LIKE ? ORDER BY adopter_name";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + searchTerm + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    adopters.add(extractAdopterFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching adopters: " + e.getMessage());
        }
        
        return adopters;
    }
    
    /**
     * Counts total number of adopters
     * @return Total count of adopters
     */
    public int getAdopterCount() {
        String sql = "SELECT COUNT(*) FROM tbl_adopter";
        
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error counting adopters: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Extracts Adopter object from ResultSet
     * @param rs ResultSet containing adopter data
     * @return Adopter object
     * @throws SQLException if database access error occurs
     */
    private Adopter extractAdopterFromResultSet(ResultSet rs) throws SQLException {
        Adopter adopter = new Adopter();
        adopter.setAdopterId(rs.getInt("adopter_id"));
        adopter.setAdopterName(rs.getString("adopter_name"));
        adopter.setAdopterContact(rs.getString("adopter_contact"));
        adopter.setAdopterEmail(rs.getString("adopter_email"));
        adopter.setAdopterAddress(rs.getString("adopter_address"));
        adopter.setAdopterProfile(rs.getString("adopter_profile"));
        adopter.setAdopterUsername(rs.getString("adopter_username"));
        adopter.setAdopterPassword(rs.getString("adopter_password"));
        return adopter;
    }
    
    /**
     * Creates an adopter profile automatically linked to a user account
     * @param username Username from users table to link to
     * @param name Display name for the adopter
     * @param contact Contact number
     * @param email Email address (should match user's email)
     * @param address Physical address
     * @return true if adopter profile was created successfully, false otherwise
     */
    public boolean createAdopterProfileForUser(String username, String name, String contact, String email, String address) {
        String sql = "INSERT INTO tbl_adopter (username, adopter_name, adopter_contact, adopter_email, adopter_address, adopter_username, adopter_password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, username);      // Link to users.username
            pstmt.setString(2, name);          // Display name
            pstmt.setString(3, contact);       // Contact number
            pstmt.setString(4, email);         // Email address
            pstmt.setString(5, address);       // Address
            pstmt.setString(6, username);      // Legacy adopter_username field
            pstmt.setString(7, "legacy");      // Legacy adopter_password field
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Adopter profile created successfully for user: " + username);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating adopter profile for user: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Gets adopter by linked username (from users table)
     * @param username Username from users table
     * @return Adopter object if found, null otherwise
     */
    public Adopter getAdopterByLinkedUsername(String username) {
        String sql = "SELECT * FROM tbl_adopter WHERE username = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractAdopterFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving adopter by linked username: " + e.getMessage());
        }
        
        return null;
    }
}
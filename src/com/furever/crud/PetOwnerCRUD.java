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
import com.furever.models.PetOwner;

/**
 * CRUD operations for PetOwner entity
 */
public class PetOwnerCRUD {
    
    /**
     * Creates a new pet owner in the database
     */
    public boolean createPetOwner(PetOwner petOwner) {
        String sql = "INSERT INTO tbl_pet_owner (pet_owner_name, pet_owner_contact, pet_owner_email, " +
                     "pet_owner_address, pet_owner_profile, pet_owner_username, pet_owner_password, username) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, petOwner.getPetOwnerName());
            pstmt.setString(2, petOwner.getPetOwnerContact());
            pstmt.setString(3, petOwner.getPetOwnerEmail());
            pstmt.setString(4, petOwner.getPetOwnerAddress());
            pstmt.setString(5, petOwner.getPetOwnerProfile());
            pstmt.setString(6, petOwner.getPetOwnerUsername());
            pstmt.setString(7, petOwner.getPetOwnerPassword());
            pstmt.setString(8, petOwner.getPetOwnerUsername()); // username field for FK
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating pet owner: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Retrieves a pet owner by ID
     */
    public PetOwner getPetOwnerById(int petOwnerId) {
        String sql = "SELECT * FROM tbl_pet_owner WHERE pet_owner_id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, petOwnerId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractPetOwnerFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving pet owner by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Retrieves a pet owner by username
     */
    public PetOwner getPetOwnerByUsername(String username) {
        String sql = "SELECT * FROM tbl_pet_owner WHERE pet_owner_username = ? OR username = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractPetOwnerFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving pet owner by username: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Retrieves all pet owners
     */
    public List<PetOwner> getAllPetOwners() {
        String sql = "SELECT * FROM tbl_pet_owner ORDER BY pet_owner_name";
        List<PetOwner> petOwners = new ArrayList<>();
        
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                petOwners.add(extractPetOwnerFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving all pet owners: " + e.getMessage());
        }
        
        return petOwners;
    }
    
    /**
     * Updates an existing pet owner
     */
    public boolean updatePetOwner(PetOwner petOwner) {
        String sql = "UPDATE tbl_pet_owner SET pet_owner_name = ?, pet_owner_contact = ?, " +
                     "pet_owner_email = ?, pet_owner_address = ?, pet_owner_profile = ? " +
                     "WHERE pet_owner_id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, petOwner.getPetOwnerName());
            pstmt.setString(2, petOwner.getPetOwnerContact());
            pstmt.setString(3, petOwner.getPetOwnerEmail());
            pstmt.setString(4, petOwner.getPetOwnerAddress());
            pstmt.setString(5, petOwner.getPetOwnerProfile());
            pstmt.setInt(6, petOwner.getPetOwnerId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating pet owner: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Deletes a pet owner by ID
     */
    public boolean deletePetOwner(int petOwnerId) {
        String sql = "DELETE FROM tbl_pet_owner WHERE pet_owner_id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, petOwnerId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting pet owner: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Searches pet owners by name (partial match)
     */
    public List<PetOwner> searchPetOwnersByName(String name) {
        String sql = "SELECT * FROM tbl_pet_owner WHERE pet_owner_name LIKE ? ORDER BY pet_owner_name";
        List<PetOwner> petOwners = new ArrayList<>();
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                petOwners.add(extractPetOwnerFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching pet owners by name: " + e.getMessage());
        }
        
        return petOwners;
    }
    
    /**
     * Gets the total count of pet owners
     */
    public int getPetOwnerCount() {
        String sql = "SELECT COUNT(*) FROM tbl_pet_owner";
        
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting pet owner count: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Creates a pet owner profile automatically when a user with pet_owner role is created
     */
    public boolean createPetOwnerProfileForUser(String username, String name, String email) {
        String sql = "INSERT INTO tbl_pet_owner (pet_owner_name, pet_owner_email, pet_owner_username, username) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, username);
            pstmt.setString(4, username);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating pet owner profile for user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Updates pet owner username reference
     */
    public boolean updatePetOwnerUsername(int petOwnerId, String username) {
        String sql = "UPDATE tbl_pet_owner SET username = ? WHERE pet_owner_id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setInt(2, petOwnerId);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating pet owner username: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Checks if a pet owner profile exists for a given username
     */
    public boolean petOwnerProfileExists(String username) {
        String sql = "SELECT COUNT(*) FROM tbl_pet_owner WHERE pet_owner_username = ? OR username = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking pet owner profile existence: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Helper method to extract PetOwner object from ResultSet
     */
    private PetOwner extractPetOwnerFromResultSet(ResultSet rs) throws SQLException {
        PetOwner petOwner = new PetOwner();
        petOwner.setPetOwnerId(rs.getInt("pet_owner_id"));
        petOwner.setPetOwnerName(rs.getString("pet_owner_name"));
        petOwner.setPetOwnerContact(rs.getString("pet_owner_contact"));
        petOwner.setPetOwnerEmail(rs.getString("pet_owner_email"));
        petOwner.setPetOwnerAddress(rs.getString("pet_owner_address"));
        petOwner.setPetOwnerProfile(rs.getString("pet_owner_profile"));
        petOwner.setPetOwnerUsername(rs.getString("pet_owner_username"));
        petOwner.setPetOwnerPassword(rs.getString("pet_owner_password"));
        return petOwner;
    }
}

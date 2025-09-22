/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.furever.crud;

/**
 *
 * @author jerimiahtongco
 */
import com.furever.database.DbConnection;
import com.furever.models.PetType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD operations for PetType entity
 */
public class PetTypeCRUD {
    
    /**
     * Creates a new pet type in the database
     * @param petType PetType object to create
     * @return true if pet type was created successfully, false otherwise
     */
    public boolean createPetType(PetType petType) {
        String sql = "INSERT INTO tbl_pet_type (pet_type_name) VALUES (?)";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, petType.getPetTypeName());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        petType.setPetTypeId(generatedKeys.getInt(1));
                    }
                }
                System.out.println("Pet type created successfully with ID: " + petType.getPetTypeId());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating pet type: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Retrieves a pet type by ID
     * @param petTypeId Pet type ID to search for
     * @return PetType object if found, null otherwise
     */
    public PetType getPetTypeById(int petTypeId) {
        String sql = "SELECT * FROM tbl_pet_type WHERE pet_type_id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, petTypeId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractPetTypeFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving pet type: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Retrieves all pet types from the database
     * @return List of all pet types
     */
    public List<PetType> getAllPetTypes() {
        List<PetType> petTypes = new ArrayList<>();
        String sql = "SELECT * FROM tbl_pet_type ORDER BY pet_type_name";
        
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                petTypes.add(extractPetTypeFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving all pet types: " + e.getMessage());
        }
        
        return petTypes;
    }
    
    /**
     * Updates an existing pet type
     * @param petType PetType object with updated information
     * @return true if pet type was updated successfully, false otherwise
     */
    public boolean updatePetType(PetType petType) {
        String sql = "UPDATE tbl_pet_type SET pet_type_name = ? WHERE pet_type_id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, petType.getPetTypeName());
            pstmt.setInt(2, petType.getPetTypeId());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Pet type updated successfully.");
                return true;
            } else {
                System.out.println("No pet type found with ID: " + petType.getPetTypeId());
            }
            
        } catch (SQLException e) {
            System.err.println("Error updating pet type: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Deletes a pet type by ID
     * @param petTypeId Pet type ID to delete
     * @return true if pet type was deleted successfully, false otherwise
     */
    public boolean deletePetType(int petTypeId) {
        String sql = "DELETE FROM tbl_pet_type WHERE pet_type_id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, petTypeId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Pet type deleted successfully.");
                return true;
            } else {
                System.out.println("No pet type found with ID: " + petTypeId);
            }
            
        } catch (SQLException e) {
            System.err.println("Error deleting pet type: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Extracts PetType object from ResultSet
     * @param rs ResultSet containing pet type data
     * @return PetType object
     * @throws SQLException if database access error occurs
     */
    private PetType extractPetTypeFromResultSet(ResultSet rs) throws SQLException {
        PetType petType = new PetType();
        petType.setPetTypeId(rs.getInt("pet_type_id"));
        petType.setPetTypeName(rs.getString("pet_type_name"));
        return petType;
    }
}
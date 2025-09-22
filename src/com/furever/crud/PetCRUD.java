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
import com.furever.models.Pet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD operations for Pet entity
 */
public class PetCRUD {
    
    /**
     * Creates a new pet in the database
     * @param pet Pet object to create
     * @return true if pet was created successfully, false otherwise
     */
    public boolean createPet(Pet pet) {
        String sql = "INSERT INTO tbl_pet (pet_owner_id, pet_name, pet_type_id, description, age, gender, health_status, upload_health_history, vaccination_status, proof_of_vaccination, adoption_status, date_registered) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, pet.getPetOwnerId());
            pstmt.setString(2, pet.getPetName());
            pstmt.setInt(3, pet.getPetTypeId());
            pstmt.setString(4, pet.getDescription());
            pstmt.setInt(5, pet.getAge());
            pstmt.setString(6, pet.getGender());
            pstmt.setString(7, pet.getHealthStatus());
            pstmt.setString(8, pet.getUploadHealthHistory());
            pstmt.setString(9, pet.getVaccinationStatus());
            pstmt.setString(10, pet.getProofOfVaccination());
            pstmt.setString(11, pet.getAdoptionStatus());
            pstmt.setDate(12, pet.getDateRegistered());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pet.setPetId(generatedKeys.getInt(1));
                    }
                }
                System.out.println("Pet created successfully with ID: " + pet.getPetId());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating pet: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Retrieves a pet by ID
     * @param petId Pet ID to search for
     * @return Pet object if found, null otherwise
     */
    public Pet getPetById(int petId) {
        String sql = "SELECT * FROM tbl_pet WHERE pet_id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, petId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractPetFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving pet: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Retrieves all pets from the database
     * @return List of all pets
     */
    public List<Pet> getAllPets() {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM tbl_pet ORDER BY pet_id";
        
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                pets.add(extractPetFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving all pets: " + e.getMessage());
        }
        
        return pets;
    }
    
    /**
     * Retrieves pets by adoption status
     * @param status Adoption status to filter by
     * @return List of pets with the specified adoption status
     */
    public List<Pet> getPetsByAdoptionStatus(String status) {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM tbl_pet WHERE adoption_status = ? ORDER BY pet_id";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    pets.add(extractPetFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving pets by adoption status: " + e.getMessage());
        }
        
        return pets;
    }
    
    /**
     * Retrieves pets by pet type
     * @param petTypeId Pet type ID to filter by
     * @return List of pets with the specified type
     */
    public List<Pet> getPetsByType(int petTypeId) {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM tbl_pet WHERE pet_type_id = ? ORDER BY pet_id";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, petTypeId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    pets.add(extractPetFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving pets by type: " + e.getMessage());
        }
        
        return pets;
    }
    
    /**
     * Retrieves pets by owner
     * @param ownerId Pet owner ID to filter by
     * @return List of pets owned by the specified owner
     */
    public List<Pet> getPetsByOwner(int ownerId) {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM tbl_pet WHERE pet_owner_id = ? ORDER BY pet_id";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, ownerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    pets.add(extractPetFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving pets by owner: " + e.getMessage());
        }
        
        return pets;
    }
    
    /**
     * Updates an existing pet
     * @param pet Pet object with updated information
     * @return true if pet was updated successfully, false otherwise
     */
    public boolean updatePet(Pet pet) {
        String sql = "UPDATE tbl_pet SET pet_owner_id = ?, pet_name = ?, pet_type_id = ?, description = ?, age = ?, gender = ?, health_status = ?, upload_health_history = ?, vaccination_status = ?, proof_of_vaccination = ?, adoption_status = ?, date_registered = ? WHERE pet_id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, pet.getPetOwnerId());
            pstmt.setString(2, pet.getPetName());
            pstmt.setInt(3, pet.getPetTypeId());
            pstmt.setString(4, pet.getDescription());
            pstmt.setInt(5, pet.getAge());
            pstmt.setString(6, pet.getGender());
            pstmt.setString(7, pet.getHealthStatus());
            pstmt.setString(8, pet.getUploadHealthHistory());
            pstmt.setString(9, pet.getVaccinationStatus());
            pstmt.setString(10, pet.getProofOfVaccination());
            pstmt.setString(11, pet.getAdoptionStatus());
            pstmt.setDate(12, pet.getDateRegistered());
            pstmt.setInt(13, pet.getPetId());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Pet updated successfully.");
                return true;
            } else {
                System.out.println("No pet found with ID: " + pet.getPetId());
            }
            
        } catch (SQLException e) {
            System.err.println("Error updating pet: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Deletes a pet by ID
     * @param petId Pet ID to delete
     * @return true if pet was deleted successfully, false otherwise
     */
    public boolean deletePet(int petId) {
        String sql = "DELETE FROM tbl_pet WHERE pet_id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, petId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Pet deleted successfully.");
                return true;
            } else {
                System.out.println("No pet found with ID: " + petId);
            }
            
        } catch (SQLException e) {
            System.err.println("Error deleting pet: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Searches pets by name
     * @param searchTerm Search term to match against pet names
     * @return List of pets matching the search term
     */
    public List<Pet> searchPetsByName(String searchTerm) {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM tbl_pet WHERE pet_name LIKE ? ORDER BY pet_name";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + searchTerm + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    pets.add(extractPetFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching pets: " + e.getMessage());
        }
        
        return pets;
    }
    
    /**
     * Counts total number of pets
     * @return Total count of pets
     */
    public int getPetCount() {
        String sql = "SELECT COUNT(*) FROM tbl_pet";
        
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error counting pets: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Counts pets by adoption status
     * @param status Adoption status to count
     * @return Count of pets with the specified status
     */
    public int getPetCountByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM tbl_pet WHERE adoption_status = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error counting pets by status: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Extracts Pet object from ResultSet
     * @param rs ResultSet containing pet data
     * @return Pet object
     * @throws SQLException if database access error occurs
     */
    private Pet extractPetFromResultSet(ResultSet rs) throws SQLException {
        Pet pet = new Pet();
        pet.setPetId(rs.getInt("pet_id"));
        pet.setPetOwnerId(rs.getInt("pet_owner_id"));
        pet.setPetName(rs.getString("pet_name"));
        pet.setPetTypeId(rs.getInt("pet_type_id"));
        pet.setDescription(rs.getString("description"));
        pet.setAge(rs.getInt("age"));
        pet.setGender(rs.getString("gender"));
        pet.setHealthStatus(rs.getString("health_status"));
        pet.setUploadHealthHistory(rs.getString("upload_health_history"));
        pet.setVaccinationStatus(rs.getString("vaccination_status"));
        pet.setProofOfVaccination(rs.getString("proof_of_vaccination"));
        pet.setAdoptionStatus(rs.getString("adoption_status"));
        pet.setDateRegistered(rs.getDate("date_registered"));
        return pet;
    }
}

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
import com.furever.models.Pet;

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
            
            // Set current date for registration
            java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
            
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
            pstmt.setDate(12, currentDate);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pet.setPetId(generatedKeys.getInt(1));
                        // Set the registration date in the pet object
                        pet.setDateRegistered(currentDate);
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
        String sql = "SELECT * FROM tbl_pet WHERE archived = false ORDER BY pet_id";
        
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
        String sql = "SELECT * FROM tbl_pet WHERE adoption_status = ? AND archived = false ORDER BY pet_id";
        
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
        String sql = "SELECT * FROM tbl_pet WHERE pet_owner_id = ? AND archived = false ORDER BY pet_id";
        
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
        String sql = "UPDATE tbl_pet SET pet_owner_id = ?, pet_name = ?, pet_type_id = ?, description = ?, age = ?, gender = ?, health_status = ?, upload_health_history = ?, vaccination_status = ?, proof_of_vaccination = ?, adoption_status = ? WHERE pet_id = ?";
        
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
            pstmt.setInt(12, pet.getPetId());
            
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
    
    /**
     * Archives a pet by ID (soft delete)
     * @param petId Pet ID to archive
     * @return true if pet was archived successfully, false otherwise
     */
    public boolean archivePet(int petId) {
        String sql = "UPDATE tbl_pet SET archived = true, archived_date = NOW() WHERE pet_id = ? AND archived = false";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, petId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Pet archived successfully.");
                return true;
            } else {
                System.out.println("No active pet found with ID: " + petId);
            }
            
        } catch (SQLException e) {
            System.err.println("Error archiving pet: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Restores an archived pet by ID
     * @param petId Pet ID to restore
     * @return true if pet was restored successfully, false otherwise
     */
    public boolean restorePet(int petId) {
        String sql = "UPDATE tbl_pet SET archived = false, archived_date = NULL WHERE pet_id = ? AND archived = true";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, petId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Pet restored successfully.");
                return true;
            } else {
                System.out.println("No archived pet found with ID: " + petId);
            }
            
        } catch (SQLException e) {
            System.err.println("Error restoring pet: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Retrieves all archived pets from the database
     * @return List of archived pets
     */
    public List<Pet> getArchivedPets() {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM tbl_pet WHERE archived = true ORDER BY archived_date DESC";
        
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                pets.add(extractPetFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving archived pets: " + e.getMessage());
        }
        
        return pets;
    }
    
    /**
     * Retrieves all active (non-archived) pets from the database
     * @return List of active pets
     */
    public List<Pet> getActivePets() {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM tbl_pet WHERE archived = false ORDER BY pet_id";
        
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                pets.add(extractPetFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving active pets: " + e.getMessage());
        }
        
        return pets;
    }
    
    /**
     * Enhanced view method - displays complete pet information in table format
     * @param pets List of pets to display
     */
    public void displayPetsTable(List<Pet> pets) {
        if (pets.isEmpty()) {
            System.out.println("No pets found.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(120));
        System.out.println("                                    PETS INFORMATION TABLE");
        System.out.println("=".repeat(120));
        System.out.printf("%-4s %-15s %-8s %-30s %-4s %-8s %-12s %-12s %-12s%n",
                "ID", "Name", "Type", "Description", "Age", "Gender", "Health", "Adoption", "Registered");
        System.out.println("-".repeat(120));
        
        for (Pet pet : pets) {
            System.out.printf("%-4d %-15s %-8d %-30s %-4d %-8s %-12s %-12s %-12s%n",
                    pet.getPetId(),
                    truncateString(pet.getPetName(), 15),
                    pet.getPetTypeId(),
                    truncateString(pet.getDescription(), 30),
                    pet.getAge(),
                    pet.getGender(),
                    pet.getHealthStatus(),
                    pet.getAdoptionStatus(),
                    pet.getDateRegistered() != null ? pet.getDateRegistered().toString() : "N/A");
        }
        System.out.println("=".repeat(120));
    }
    
    /**
     * Helper method to truncate strings for display
     * @param str String to truncate
     * @param maxLength Maximum length
     * @return Truncated string
     */
    private String truncateString(String str, int maxLength) {
        if (str == null) return "N/A";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
    
    /**
     * Enhanced read method - displays complete pet details
     * @param petId Pet ID to display details for
     */
    public void displayPetDetails(int petId) {
        Pet pet = getPetById(petId);
        if (pet == null) {
            System.out.println("Pet not found with ID: " + petId);
            return;
        }
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                        PET DETAILED INFORMATION");
        System.out.println("=".repeat(80));
        System.out.printf("%-25s: %s%n", "Pet ID", pet.getPetId());
        System.out.printf("%-25s: %s%n", "Pet Name", pet.getPetName());
        System.out.printf("%-25s: %s%n", "Pet Type ID", pet.getPetTypeId());
        System.out.printf("%-25s: %s%n", "Pet Owner ID", pet.getPetOwnerId());
        System.out.printf("%-25s: %s%n", "Description", pet.getDescription());
        System.out.printf("%-25s: %d years%n", "Age", pet.getAge());
        System.out.printf("%-25s: %s%n", "Gender", pet.getGender());
        System.out.printf("%-25s: %s%n", "Health Status", pet.getHealthStatus());
        System.out.printf("%-25s: %s%n", "Health History", pet.getUploadHealthHistory());
        System.out.printf("%-25s: %s%n", "Vaccination Status", pet.getVaccinationStatus());
        System.out.printf("%-25s: %s%n", "Vaccination Proof", pet.getProofOfVaccination());
        System.out.printf("%-25s: %s%n", "Adoption Status", pet.getAdoptionStatus());
        System.out.printf("%-25s: %s%n", "Date Registered", pet.getDateRegistered());
        System.out.println("=".repeat(80));
    }
    
    // Enhanced Update Methods for better functionality
    
    /**
     * Updates only the pet's basic information (name, description, age)
     * @param petId Pet ID to update
     * @param name New pet name
     * @param description New description
     * @param age New age
     * @return true if pet was updated successfully, false otherwise
     */
    public boolean updatePetBasicInfo(int petId, String name, String description, int age) {
        String sql = "UPDATE tbl_pet SET pet_name = ?, description = ?, age = ? WHERE pet_id = ? AND archived = FALSE";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setInt(3, age);
            pstmt.setInt(4, petId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating pet basic info: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Updates only the pet's health-related information
     * @param petId Pet ID to update
     * @param healthStatus New health status
     * @param vaccinationStatus New vaccination status
     * @return true if pet was updated successfully, false otherwise
     */
    public boolean updatePetHealthInfo(int petId, String healthStatus, String vaccinationStatus) {
        String sql = "UPDATE tbl_pet SET health_status = ?, vaccination_status = ? WHERE pet_id = ? AND archived = FALSE";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, healthStatus);
            pstmt.setString(2, vaccinationStatus);
            pstmt.setInt(3, petId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating pet health info: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Updates only the pet's adoption status
     * @param petId Pet ID to update
     * @param adoptionStatus New adoption status
     * @return true if pet was updated successfully, false otherwise
     */
    public boolean updatePetAdoptionStatus(int petId, String adoptionStatus) {
        String sql = "UPDATE tbl_pet SET adoption_status = ? WHERE pet_id = ? AND archived = FALSE";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, adoptionStatus);
            pstmt.setInt(2, petId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating pet adoption status: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Batch update multiple pets' adoption status
     * @param petIds List of pet IDs to update
     * @param adoptionStatus New adoption status for all pets
     * @return number of pets successfully updated
     */
    public int batchUpdateAdoptionStatus(List<Integer> petIds, String adoptionStatus) {
        String sql = "UPDATE tbl_pet SET adoption_status = ? WHERE pet_id = ? AND archived = FALSE";
        int updatedCount = 0;
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (Integer petId : petIds) {
                pstmt.setString(1, adoptionStatus);
                pstmt.setInt(2, petId);
                pstmt.addBatch();
            }
            
            int[] results = pstmt.executeBatch();
            for (int result : results) {
                if (result > 0) {
                    updatedCount++;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error in batch update adoption status: " + e.getMessage());
        }
        
        return updatedCount;
    }
}
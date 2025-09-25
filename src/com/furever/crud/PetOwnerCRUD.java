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
     * Deletes a pet owner by ID (now archives instead of permanent deletion)
     */
    public boolean deletePetOwner(int petOwnerId) {
        return archivePetOwner(petOwnerId, null, "Pet owner deleted via admin dashboard");
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
    
    /**
     * Archives a pet owner by moving them to the archive table
     * Note: This will fail if the pet owner has pets that are not archived
     * @param petOwnerId ID of the pet owner to archive
     * @param archivedByUserId ID of the user performing the archive operation
     * @param reason Reason for archiving
     * @return true if archiving was successful, false otherwise
     */
    public boolean archivePetOwner(int petOwnerId, Integer archivedByUserId, String reason) {
        String checkPetsSql = "SELECT COUNT(*) as pet_count FROM tbl_pet WHERE pet_owner_id = ?";
        String selectPetOwnerSql = "SELECT * FROM tbl_pet_owner WHERE pet_owner_id = ?";
        String insertArchiveSql = "INSERT INTO tbl_pet_owner_archive (pet_owner_id, pet_owner_name, pet_owner_contact, pet_owner_email, pet_owner_address, pet_owner_profile, pet_owner_username, pet_owner_password, username, archived, archived_date, archived_by_user_id, archive_reason) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String deletePetOwnerSql = "DELETE FROM tbl_pet_owner WHERE pet_owner_id = ?";
        String logSql = "INSERT INTO tbl_archive_log (table_name, record_id, operation, performed_by_user_id, reason) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DbConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Check if pet owner has any active pets
            try (PreparedStatement checkStmt = conn.prepareStatement(checkPetsSql)) {
                checkStmt.setInt(1, petOwnerId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt("pet_count") > 0) {
                        System.err.println("Cannot archive pet owner: Pet owner has active pets. Please archive or transfer the pets first.");
                        conn.rollback();
                        return false;
                    }
                }
            }
            
            // Get the pet owner record to archive
            PetOwner petOwner = null;
            String linkedUsername = null;
            try (PreparedStatement selectStmt = conn.prepareStatement(selectPetOwnerSql)) {
                selectStmt.setInt(1, petOwnerId);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        petOwner = new PetOwner();
                        petOwner.setPetOwnerId(rs.getInt("pet_owner_id"));
                        petOwner.setPetOwnerName(rs.getString("pet_owner_name"));
                        petOwner.setPetOwnerContact(rs.getString("pet_owner_contact"));
                        petOwner.setPetOwnerEmail(rs.getString("pet_owner_email"));
                        petOwner.setPetOwnerAddress(rs.getString("pet_owner_address"));
                        petOwner.setPetOwnerProfile(rs.getString("pet_owner_profile"));
                        petOwner.setPetOwnerUsername(rs.getString("pet_owner_username"));
                        petOwner.setPetOwnerPassword(rs.getString("pet_owner_password"));
                        linkedUsername = rs.getString("username");
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }
            
            // Insert into archive table
            try (PreparedStatement insertStmt = conn.prepareStatement(insertArchiveSql)) {
                insertStmt.setInt(1, petOwner.getPetOwnerId());
                insertStmt.setString(2, petOwner.getPetOwnerName());
                insertStmt.setString(3, petOwner.getPetOwnerContact());
                insertStmt.setString(4, petOwner.getPetOwnerEmail());
                insertStmt.setString(5, petOwner.getPetOwnerAddress());
                insertStmt.setString(6, petOwner.getPetOwnerProfile());
                insertStmt.setString(7, petOwner.getPetOwnerUsername());
                insertStmt.setString(8, petOwner.getPetOwnerPassword());
                insertStmt.setString(9, linkedUsername);
                insertStmt.setBoolean(10, true);
                insertStmt.setTimestamp(11, new java.sql.Timestamp(System.currentTimeMillis()));
                if (archivedByUserId != null) {
                    insertStmt.setInt(12, archivedByUserId);
                } else {
                    insertStmt.setNull(12, java.sql.Types.INTEGER);
                }
                insertStmt.setString(13, reason);
                
                insertStmt.executeUpdate();
            }
            
            // Delete from main table
            try (PreparedStatement deleteStmt = conn.prepareStatement(deletePetOwnerSql)) {
                deleteStmt.setInt(1, petOwnerId);
                deleteStmt.executeUpdate();
            }
            
            // Log the operation
            try (PreparedStatement logStmt = conn.prepareStatement(logSql)) {
                logStmt.setString(1, "tbl_pet_owner");
                logStmt.setInt(2, petOwnerId);
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
            System.err.println("Error archiving pet owner: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Restores a pet owner from archive back to the main table
     * @param petOwnerId ID of the pet owner to restore
     * @param restoredByUserId ID of the user performing the restore operation
     * @param reason Reason for restoring
     * @return true if restoration was successful, false otherwise
     */
    public boolean restorePetOwner(int petOwnerId, Integer restoredByUserId, String reason) {
        String selectArchiveSql = "SELECT * FROM tbl_pet_owner_archive WHERE pet_owner_id = ?";
        String insertMainSql = "INSERT INTO tbl_pet_owner (pet_owner_id, pet_owner_name, pet_owner_contact, pet_owner_email, pet_owner_address, pet_owner_profile, pet_owner_username, pet_owner_password, username, archived, archived_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String deleteArchiveSql = "DELETE FROM tbl_pet_owner_archive WHERE pet_owner_id = ?";
        String logSql = "INSERT INTO tbl_archive_log (table_name, record_id, operation, performed_by_user_id, reason) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DbConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Get the pet owner record from archive
            PetOwner petOwner = null;
            String linkedUsername = null;
            try (PreparedStatement selectStmt = conn.prepareStatement(selectArchiveSql)) {
                selectStmt.setInt(1, petOwnerId);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        petOwner = new PetOwner();
                        petOwner.setPetOwnerId(rs.getInt("pet_owner_id"));
                        petOwner.setPetOwnerName(rs.getString("pet_owner_name"));
                        petOwner.setPetOwnerContact(rs.getString("pet_owner_contact"));
                        petOwner.setPetOwnerEmail(rs.getString("pet_owner_email"));
                        petOwner.setPetOwnerAddress(rs.getString("pet_owner_address"));
                        petOwner.setPetOwnerProfile(rs.getString("pet_owner_profile"));
                        petOwner.setPetOwnerUsername(rs.getString("pet_owner_username"));
                        petOwner.setPetOwnerPassword(rs.getString("pet_owner_password"));
                        linkedUsername = rs.getString("username");
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }
            
            // Insert pet owner back into main table
            try (PreparedStatement insertStmt = conn.prepareStatement(insertMainSql)) {
                insertStmt.setInt(1, petOwner.getPetOwnerId());
                insertStmt.setString(2, petOwner.getPetOwnerName());
                insertStmt.setString(3, petOwner.getPetOwnerContact());
                insertStmt.setString(4, petOwner.getPetOwnerEmail());
                insertStmt.setString(5, petOwner.getPetOwnerAddress());
                insertStmt.setString(6, petOwner.getPetOwnerProfile());
                insertStmt.setString(7, petOwner.getPetOwnerUsername());
                insertStmt.setString(8, petOwner.getPetOwnerPassword());
                insertStmt.setString(9, linkedUsername);
                insertStmt.setBoolean(10, false);
                insertStmt.setNull(11, java.sql.Types.TIMESTAMP);
                
                insertStmt.executeUpdate();
            }
            
            // Delete from archive table
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteArchiveSql)) {
                deleteStmt.setInt(1, petOwnerId);
                deleteStmt.executeUpdate();
            }
            
            // Log the operation
            try (PreparedStatement logStmt = conn.prepareStatement(logSql)) {
                logStmt.setString(1, "tbl_pet_owner");
                logStmt.setInt(2, petOwnerId);
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
            System.err.println("Error restoring pet owner: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Permanently deletes a pet owner from the archive table
     * @param petOwnerId ID of the pet owner to permanently delete
     * @param deletedByUserId ID of the user performing the permanent deletion
     * @param reason Reason for permanent deletion
     * @return true if permanent deletion was successful, false otherwise
     */
    public boolean permanentDeletePetOwner(int petOwnerId, Integer deletedByUserId, String reason) {
        String deletePetOwnerSql = "DELETE FROM tbl_pet_owner_archive WHERE pet_owner_id = ?";
        String logSql = "INSERT INTO tbl_archive_log (table_name, record_id, operation, performed_by_user_id, reason) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DbConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Delete pet owner from archive table
            try (PreparedStatement deleteStmt = conn.prepareStatement(deletePetOwnerSql)) {
                deleteStmt.setInt(1, petOwnerId);
                int rowsAffected = deleteStmt.executeUpdate();
                
                if (rowsAffected == 0) {
                    conn.rollback();
                    return false;
                }
            }
            
            // Log the operation
            try (PreparedStatement logStmt = conn.prepareStatement(logSql)) {
                logStmt.setString(1, "tbl_pet_owner");
                logStmt.setInt(2, petOwnerId);
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
            System.err.println("Error permanently deleting pet owner: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Gets all archived pet owners
     * @return List of archived pet owners
     */
    public List<PetOwner> getAllArchivedPetOwners() {
        List<PetOwner> petOwners = new ArrayList<>();
        String sql = "SELECT * FROM tbl_pet_owner_archive ORDER BY archived_date DESC";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                PetOwner petOwner = new PetOwner();
                petOwner.setPetOwnerId(rs.getInt("pet_owner_id"));
                petOwner.setPetOwnerName(rs.getString("pet_owner_name"));
                petOwner.setPetOwnerContact(rs.getString("pet_owner_contact"));
                petOwner.setPetOwnerEmail(rs.getString("pet_owner_email"));
                petOwner.setPetOwnerAddress(rs.getString("pet_owner_address"));
                petOwner.setPetOwnerProfile(rs.getString("pet_owner_profile"));
                petOwner.setPetOwnerUsername(rs.getString("pet_owner_username"));
                petOwner.setPetOwnerPassword(rs.getString("pet_owner_password"));
                // Note: username field stored separately in archive table
                petOwners.add(petOwner);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving archived pet owners: " + e.getMessage());
        }
        
        return petOwners;
    }
}
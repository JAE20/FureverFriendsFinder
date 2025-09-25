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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.furever.database.DbConnection;
import com.furever.models.AdoptionRequest;

/**
 * CRUD operations for AdoptionRequest entity
 */
public class AdoptionRequestCRUD {
    
    /**
     * Creates a new adoption request in the database
     * @param adoptionRequest AdoptionRequest object to create
     * @return true if adoption request was created successfully, false otherwise
     */
    public boolean createAdoptionRequest(AdoptionRequest adoptionRequest) {
        String sql = "INSERT INTO tbl_adoption_request (pet_id, adopter_id, request_date, status, approval_date, remarks, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, adoptionRequest.getPetId());
            pstmt.setInt(2, adoptionRequest.getAdopterId());
            pstmt.setDate(3, adoptionRequest.getRequestDate());
            pstmt.setString(4, adoptionRequest.getStatus());
            pstmt.setDate(5, adoptionRequest.getApprovalDate());
            pstmt.setString(6, adoptionRequest.getRemarks());
            if (adoptionRequest.getUserId() != null) {
                pstmt.setInt(7, adoptionRequest.getUserId());
            } else {
                pstmt.setNull(7, Types.INTEGER);
            }
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        adoptionRequest.setAdoptionRequestId(generatedKeys.getInt(1));
                    }
                }
                System.out.println("Adoption request created successfully with ID: " + adoptionRequest.getAdoptionRequestId());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating adoption request: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Retrieves an adoption request by ID
     * @param requestId Adoption request ID to search for
     * @return AdoptionRequest object if found, null otherwise
     */
    public AdoptionRequest getAdoptionRequestById(int requestId) {
        String sql = "SELECT * FROM tbl_adoption_request WHERE adoption_request_id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, requestId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractAdoptionRequestFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving adoption request: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Retrieves all adoption requests from the database
     * @return List of all adoption requests
     */
    public List<AdoptionRequest> getAllAdoptionRequests() {
        List<AdoptionRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM tbl_adoption_request ORDER BY adoption_request_id";
        
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                requests.add(extractAdoptionRequestFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving all adoption requests: " + e.getMessage());
        }
        
        return requests;
    }
    
    /**
     * Retrieves adoption requests by status
     * @param status Status to filter by
     * @return List of adoption requests with the specified status
     */
    public List<AdoptionRequest> getAdoptionRequestsByStatus(String status) {
        List<AdoptionRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM tbl_adoption_request WHERE status = ? ORDER BY request_date DESC";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    requests.add(extractAdoptionRequestFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving adoption requests by status: " + e.getMessage());
        }
        
        return requests;
    }
    
    /**
     * Retrieves adoption requests by adopter
     * @param adopterId Adopter ID to filter by
     * @return List of adoption requests from the specified adopter
     */
    public List<AdoptionRequest> getAdoptionRequestsByAdopter(int adopterId) {
        List<AdoptionRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM tbl_adoption_request WHERE adopter_id = ? ORDER BY request_date DESC";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, adopterId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    requests.add(extractAdoptionRequestFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving adoption requests by adopter: " + e.getMessage());
        }
        
        return requests;
    }
    
    /**
     * Retrieves adoption requests for a specific pet
     * @param petId Pet ID to filter by
     * @return List of adoption requests for the specified pet
     */
    public List<AdoptionRequest> getAdoptionRequestsByPet(int petId) {
        List<AdoptionRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM tbl_adoption_request WHERE pet_id = ? ORDER BY request_date DESC";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, petId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    requests.add(extractAdoptionRequestFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving adoption requests by pet: " + e.getMessage());
        }
        
        return requests;
    }
    
    /**
     * Updates an existing adoption request
     * @param adoptionRequest AdoptionRequest object with updated information
     * @return true if adoption request was updated successfully, false otherwise
     */
    public boolean updateAdoptionRequest(AdoptionRequest adoptionRequest) {
        String sql = "UPDATE tbl_adoption_request SET pet_id = ?, adopter_id = ?, request_date = ?, status = ?, approval_date = ?, remarks = ?, user_id = ? WHERE adoption_request_id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, adoptionRequest.getPetId());
            pstmt.setInt(2, adoptionRequest.getAdopterId());
            pstmt.setDate(3, adoptionRequest.getRequestDate());
            pstmt.setString(4, adoptionRequest.getStatus());
            pstmt.setDate(5, adoptionRequest.getApprovalDate());
            pstmt.setString(6, adoptionRequest.getRemarks());
            if (adoptionRequest.getUserId() != null) {
                pstmt.setInt(7, adoptionRequest.getUserId());
            } else {
                pstmt.setNull(7, Types.INTEGER);
            }
            pstmt.setInt(8, adoptionRequest.getAdoptionRequestId());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Adoption request updated successfully.");
                return true;
            } else {
                System.out.println("No adoption request found with ID: " + adoptionRequest.getAdoptionRequestId());
            }
            
        } catch (SQLException e) {
            System.err.println("Error updating adoption request: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Checks if an adopter already has an approved request for a specific pet
     * @param adopterId Adopter ID to check
     * @param petId Pet ID to check
     * @return true if adopter already has an approved request for this pet, false otherwise
     */
    public boolean hasApprovedRequestForPet(int adopterId, int petId) {
        String sql = "SELECT COUNT(*) FROM tbl_adoption_request WHERE adopter_id = ? AND pet_id = ? AND status = 'Approved'";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, adopterId);
            pstmt.setInt(2, petId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking for existing approved requests: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Approves an adoption request with constraint checking
     * @param requestId Adoption request ID to approve
     * @param approvalDate Date of approval
     * @param remarks Approval remarks
     * @return true if request was approved successfully, false otherwise
     */
    public boolean approveAdoptionRequestSafely(int requestId, Date approvalDate, String remarks) {
        // First, get the adoption request details
        AdoptionRequest request = getAdoptionRequestById(requestId);
        if (request == null) {
            System.out.println("❌ ERROR: Adoption request not found.");
            return false;
        }
        
        // Check if adopter already has an approved request for this pet
        if (hasApprovedRequestForPet(request.getAdopterId(), request.getPetId())) {
            System.out.println("❌ ERROR: This adopter already has an approved request for this pet.");
            System.out.println("   Only one approved request per adopter per pet is allowed.");
            System.out.println("   Please reject the existing approved request first if needed.");
            return false;
        }
        
        // If no conflict, proceed with approval
        return approveAdoptionRequest(requestId, approvalDate, remarks);
    }
    
    /**
     * Approves an adoption request
     * @param requestId Adoption request ID to approve
     * @param approvalDate Date of approval
     * @param remarks Approval remarks
     * @return true if request was approved successfully, false otherwise
     */
    public boolean approveAdoptionRequest(int requestId, Date approvalDate, String remarks) {
        String sql = "UPDATE tbl_adoption_request SET status = 'Approved', approval_date = ?, remarks = ? WHERE adoption_request_id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, approvalDate);
            pstmt.setString(2, remarks);
            pstmt.setInt(3, requestId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Adoption request approved successfully.");
                return true;
            } else {
                System.out.println("No adoption request found with ID: " + requestId);
            }
            
        } catch (SQLException e) {
            System.err.println("Error approving adoption request: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Rejects an adoption request
     * @param requestId Adoption request ID to reject
     * @param remarks Rejection remarks
     * @return true if request was rejected successfully, false otherwise
     */
    public boolean rejectAdoptionRequest(int requestId, String remarks) {
        String sql = "UPDATE tbl_adoption_request SET status = 'Rejected', remarks = ? WHERE adoption_request_id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, remarks);
            pstmt.setInt(2, requestId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Adoption request rejected successfully.");
                return true;
            } else {
                System.out.println("No adoption request found with ID: " + requestId);
            }
            
        } catch (SQLException e) {
            System.err.println("Error rejecting adoption request: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Deletes an adoption request by ID
     * @param requestId Adoption request ID to delete
     * @return true if adoption request was deleted successfully, false otherwise
     */
    public boolean deleteAdoptionRequest(int requestId) {
        String sql = "DELETE FROM tbl_adoption_request WHERE adoption_request_id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, requestId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Adoption request deleted successfully.");
                return true;
            } else {
                System.out.println("No adoption request found with ID: " + requestId);
            }
            
        } catch (SQLException e) {
            System.err.println("Error deleting adoption request: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Counts total number of adoption requests
     * @return Total count of adoption requests
     */
    public int getAdoptionRequestCount() {
        String sql = "SELECT COUNT(*) FROM tbl_adoption_request";
        
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error counting adoption requests: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Counts adoption requests by status
     * @param status Status to count
     * @return Count of adoption requests with the specified status
     */
    public int getAdoptionRequestCountByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM tbl_adoption_request WHERE status = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error counting adoption requests by status: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Extracts AdoptionRequest object from ResultSet
     * @param rs ResultSet containing adoption request data
     * @return AdoptionRequest object
     * @throws SQLException if database access error occurs
     */
    private AdoptionRequest extractAdoptionRequestFromResultSet(ResultSet rs) throws SQLException {
        AdoptionRequest request = new AdoptionRequest();
        request.setAdoptionRequestId(rs.getInt("adoption_request_id"));
        request.setPetId(rs.getInt("pet_id"));
        request.setAdopterId(rs.getInt("adopter_id"));
        request.setRequestDate(rs.getDate("request_date"));
        request.setStatus(rs.getString("status"));
        request.setApprovalDate(rs.getDate("approval_date"));
        request.setRemarks(rs.getString("remarks"));
        
        int userId = rs.getInt("user_id");
        if (!rs.wasNull()) {
            request.setUserId(userId);
        }
        
        return request;
    }
    
    /**
     * Archives an adoption request by moving it to the archive table and removing from main table
     * @param adoptionRequestId ID of the adoption request to archive
     * @param archivedByUserId ID of the user performing the archive operation
     * @param reason Reason for archiving
     * @return true if archiving was successful, false otherwise
     */
    public boolean archiveAdoptionRequest(int adoptionRequestId, Integer archivedByUserId, String reason) {
        String selectSql = "SELECT * FROM tbl_adoption_request WHERE adoption_request_id = ?";
        String insertArchiveSql = "INSERT INTO tbl_adoption_request_archive (adoption_request_id, pet_id, adopter_id, request_date, status, approval_date, remarks, user_id, archived, archived_date, archived_by_user_id, archive_reason) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String deleteSql = "DELETE FROM tbl_adoption_request WHERE adoption_request_id = ?";
        String logSql = "INSERT INTO tbl_archive_log (table_name, record_id, operation, performed_by_user_id, reason) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DbConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Get the record to archive
            AdoptionRequest request = null;
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                selectStmt.setInt(1, adoptionRequestId);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        request = extractAdoptionRequestFromResultSet(rs);
                    }
                }
            }
            
            if (request == null) {
                conn.rollback();
                return false;
            }
            
            // Insert into archive table
            try (PreparedStatement insertStmt = conn.prepareStatement(insertArchiveSql)) {
                insertStmt.setInt(1, request.getAdoptionRequestId());
                insertStmt.setInt(2, request.getPetId());
                insertStmt.setInt(3, request.getAdopterId());
                insertStmt.setDate(4, request.getRequestDate());
                insertStmt.setString(5, request.getStatus());
                insertStmt.setDate(6, request.getApprovalDate());
                insertStmt.setString(7, request.getRemarks());
                if (request.getUserId() != null) {
                    insertStmt.setInt(8, request.getUserId());
                } else {
                    insertStmt.setNull(8, java.sql.Types.INTEGER);
                }
                insertStmt.setBoolean(9, true);
                insertStmt.setTimestamp(10, new java.sql.Timestamp(System.currentTimeMillis()));
                if (archivedByUserId != null) {
                    insertStmt.setInt(11, archivedByUserId);
                } else {
                    insertStmt.setNull(11, java.sql.Types.INTEGER);
                }
                insertStmt.setString(12, reason);
                
                insertStmt.executeUpdate();
            }
            
            // Delete from main table
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, adoptionRequestId);
                deleteStmt.executeUpdate();
            }
            
            // Log the operation
            try (PreparedStatement logStmt = conn.prepareStatement(logSql)) {
                logStmt.setString(1, "tbl_adoption_request");
                logStmt.setInt(2, adoptionRequestId);
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
            System.err.println("Error archiving adoption request: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Restores an adoption request from archive back to the main table
     * @param adoptionRequestId ID of the adoption request to restore
     * @param restoredByUserId ID of the user performing the restore operation
     * @param reason Reason for restoring
     * @return true if restoration was successful, false otherwise
     */
    public boolean restoreAdoptionRequest(int adoptionRequestId, Integer restoredByUserId, String reason) {
        String selectArchiveSql = "SELECT * FROM tbl_adoption_request_archive WHERE adoption_request_id = ?";
        String insertMainSql = "INSERT INTO tbl_adoption_request (adoption_request_id, pet_id, adopter_id, request_date, status, approval_date, remarks, user_id, archived, archived_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String deleteArchiveSql = "DELETE FROM tbl_adoption_request_archive WHERE adoption_request_id = ?";
        String logSql = "INSERT INTO tbl_archive_log (table_name, record_id, operation, performed_by_user_id, reason) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DbConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Get the archived record
            AdoptionRequest request = null;
            try (PreparedStatement selectStmt = conn.prepareStatement(selectArchiveSql)) {
                selectStmt.setInt(1, adoptionRequestId);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        request = extractAdoptionRequestFromResultSet(rs);
                    }
                }
            }
            
            if (request == null) {
                conn.rollback();
                return false;
            }
            
            // Insert back into main table
            try (PreparedStatement insertStmt = conn.prepareStatement(insertMainSql)) {
                insertStmt.setInt(1, request.getAdoptionRequestId());
                insertStmt.setInt(2, request.getPetId());
                insertStmt.setInt(3, request.getAdopterId());
                insertStmt.setDate(4, request.getRequestDate());
                insertStmt.setString(5, request.getStatus());
                insertStmt.setDate(6, request.getApprovalDate());
                insertStmt.setString(7, request.getRemarks());
                if (request.getUserId() != null) {
                    insertStmt.setInt(8, request.getUserId());
                } else {
                    insertStmt.setNull(8, java.sql.Types.INTEGER);
                }
                insertStmt.setBoolean(9, false);
                insertStmt.setNull(10, java.sql.Types.TIMESTAMP);
                
                insertStmt.executeUpdate();
            }
            
            // Delete from archive table
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteArchiveSql)) {
                deleteStmt.setInt(1, adoptionRequestId);
                deleteStmt.executeUpdate();
            }
            
            // Log the operation
            try (PreparedStatement logStmt = conn.prepareStatement(logSql)) {
                logStmt.setString(1, "tbl_adoption_request");
                logStmt.setInt(2, adoptionRequestId);
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
            System.err.println("Error restoring adoption request: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Permanently deletes an adoption request from the archive table
     * @param adoptionRequestId ID of the adoption request to permanently delete
     * @param deletedByUserId ID of the user performing the permanent delete operation
     * @param reason Reason for permanent deletion
     * @return true if permanent deletion was successful, false otherwise
     */
    public boolean permanentDeleteAdoptionRequest(int adoptionRequestId, Integer deletedByUserId, String reason) {
        String deleteSql = "DELETE FROM tbl_adoption_request_archive WHERE adoption_request_id = ?";
        String logSql = "INSERT INTO tbl_archive_log (table_name, record_id, operation, performed_by_user_id, reason) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DbConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Delete from archive table
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, adoptionRequestId);
                int rowsAffected = deleteStmt.executeUpdate();
                
                if (rowsAffected == 0) {
                    conn.rollback();
                    return false;
                }
            }
            
            // Log the operation
            try (PreparedStatement logStmt = conn.prepareStatement(logSql)) {
                logStmt.setString(1, "tbl_adoption_request");
                logStmt.setInt(2, adoptionRequestId);
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
            System.err.println("Error permanently deleting adoption request: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Gets all archived adoption requests
     * @return List of archived adoption requests
     */
    public List<AdoptionRequest> getAllArchivedAdoptionRequests() {
        String sql = "SELECT * FROM tbl_adoption_request_archive ORDER BY archived_date DESC";
        List<AdoptionRequest> requests = new ArrayList<>();
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                requests.add(extractAdoptionRequestFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving archived adoption requests: " + e.getMessage());
        }
        
        return requests;
    }
    
    /**
     * Gets archived adoption requests by adopter ID
     * @param adopterId ID of the adopter
     * @return List of archived adoption requests for the adopter
     */
    public List<AdoptionRequest> getArchivedAdoptionRequestsByAdopter(int adopterId) {
        String sql = "SELECT * FROM tbl_adoption_request_archive WHERE adopter_id = ? ORDER BY archived_date DESC";
        List<AdoptionRequest> requests = new ArrayList<>();
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, adopterId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    requests.add(extractAdoptionRequestFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving archived adoption requests by adopter: " + e.getMessage());
        }
        
        return requests;
    }
}
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
import com.furever.models.AdoptionRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
}

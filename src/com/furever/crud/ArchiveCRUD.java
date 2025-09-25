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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.furever.database.DbConnection;
import com.furever.models.AdoptionRequest;
import com.furever.models.Pet;
import com.furever.models.Adopter;
import com.furever.models.PetOwner;

/**
 * CRUD operations for Archive Management
 * Provides unified archive management functionality across all tables
 */
public class ArchiveCRUD {
    
    /**
     * Gets archive statistics for dashboard display
     * @return Map containing counts for each archived table
     */
    public Map<String, Integer> getArchiveStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        
        String[] tables = {
            "tbl_adopter_archive",
            "tbl_pet_owner_archive", 
            "tbl_pet_archive",
            "tbl_adoption_request_archive",
            "tbl_adoption_archive",
            "users_archive"
        };
        
        for (String table : tables) {
            String sql = "SELECT COUNT(*) FROM " + table;
            
            try (Connection conn = DbConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                
                if (rs.next()) {
                    stats.put(table, rs.getInt(1));
                } else {
                    stats.put(table, 0);
                }
                
            } catch (SQLException e) {
                System.err.println("Error getting statistics for " + table + ": " + e.getMessage());
                stats.put(table, 0);
            }
        }
        
        return stats;
    }
    
    /**
     * Gets recent archive operations for audit log
     * @param limit Number of recent operations to retrieve
     * @return List of archive log entries
     */
    public List<ArchiveLogEntry> getRecentArchiveOperations(int limit) {
        String sql = "SELECT * FROM tbl_archive_log ORDER BY operation_date DESC LIMIT ?";
        List<ArchiveLogEntry> logEntries = new ArrayList<>();
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ArchiveLogEntry entry = new ArchiveLogEntry();
                    entry.setLogId(rs.getInt("log_id"));
                    entry.setTableName(rs.getString("table_name"));
                    entry.setRecordId(rs.getInt("record_id"));
                    entry.setOperation(rs.getString("operation"));
                    entry.setPerformedByUserId(rs.getInt("performed_by_user_id"));
                    entry.setOperationDate(rs.getTimestamp("operation_date"));
                    entry.setReason(rs.getString("reason"));
                    
                    logEntries.add(entry);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving archive operations: " + e.getMessage());
        }
        
        return logEntries;
    }
    
    /**
     * Gets archive log entries for a specific table
     * @param tableName Name of the table to get log entries for
     * @param limit Number of entries to retrieve
     * @return List of archive log entries for the specified table
     */
    public List<ArchiveLogEntry> getArchiveOperationsByTable(String tableName, int limit) {
        String sql = "SELECT * FROM tbl_archive_log WHERE table_name = ? ORDER BY operation_date DESC LIMIT ?";
        List<ArchiveLogEntry> logEntries = new ArrayList<>();
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, tableName);
            pstmt.setInt(2, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ArchiveLogEntry entry = new ArchiveLogEntry();
                    entry.setLogId(rs.getInt("log_id"));
                    entry.setTableName(rs.getString("table_name"));
                    entry.setRecordId(rs.getInt("record_id"));
                    entry.setOperation(rs.getString("operation"));
                    entry.setPerformedByUserId(rs.getInt("performed_by_user_id"));
                    entry.setOperationDate(rs.getTimestamp("operation_date"));
                    entry.setReason(rs.getString("reason"));
                    
                    logEntries.add(entry);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving archive operations by table: " + e.getMessage());
        }
        
        return logEntries;
    }
    
    /**
     * Archives adoption requests with status "Approved" or "Rejected" after 30 days
     * @param archivedByUserId ID of the user performing the archive operation
     * @return number of records archived
     */
    public int autoArchiveCompletedAdoptionRequests(Integer archivedByUserId) {
        AdoptionRequestCRUD requestCRUD = new AdoptionRequestCRUD();
        String selectSql = "SELECT adoption_request_id FROM tbl_adoption_request " +
                          "WHERE status IN ('Approved', 'Rejected') " +
                          "AND (approval_date IS NULL OR approval_date < DATE_SUB(CURDATE(), INTERVAL 30 DAY))";
        
        int archivedCount = 0;
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                int requestId = rs.getInt("adoption_request_id");
                if (requestCRUD.archiveAdoptionRequest(requestId, archivedByUserId, "Auto-archived: Completed request older than 30 days")) {
                    archivedCount++;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error auto-archiving adoption requests: " + e.getMessage());
        }
        
        return archivedCount;
    }
    
    /**
     * Archives pets with status "Adopted" after 60 days
     * @param archivedByUserId ID of the user performing the archive operation
     * @return number of records archived
     */
    public int autoArchiveAdoptedPets(Integer archivedByUserId) {
        PetCRUD petCRUD = new PetCRUD();
        String selectSql = "SELECT p.pet_id FROM tbl_pet p " +
                          "JOIN tbl_adoption a ON p.pet_id = a.pet_id " +
                          "WHERE p.adoption_status = 'Adopted' " +
                          "AND a.adoption_date < DATE_SUB(CURDATE(), INTERVAL 60 DAY)";
        
        int archivedCount = 0;
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                int petId = rs.getInt("pet_id");
                if (petCRUD.archivePet(petId, archivedByUserId, "Auto-archived: Pet adopted over 60 days ago")) {
                    archivedCount++;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error auto-archiving adopted pets: " + e.getMessage());
        }
        
        return archivedCount;
    }
    
    /**
     * Clears archive log entries older than specified days
     * @param daysToKeep Number of days to keep log entries
     * @return number of log entries deleted
     */
    public int cleanupArchiveLog(int daysToKeep) {
        String sql = "DELETE FROM tbl_archive_log WHERE operation_date < DATE_SUB(NOW(), INTERVAL ? DAY)";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, daysToKeep);
            
            int deletedCount = pstmt.executeUpdate();
            return deletedCount;
            
        } catch (SQLException e) {
            System.err.println("Error cleaning up archive log: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Gets archived records summary with details for admin dashboard
     * @return List of archive summaries
     */
    public List<ArchiveSummary> getArchiveSummary() {
        List<ArchiveSummary> summaries = new ArrayList<>();
        
        // Pets archive summary
        String petsSql = "SELECT COUNT(*) as count, MIN(archived_date) as oldest, MAX(archived_date) as newest " +
                        "FROM tbl_pet_archive";
        summaries.add(getArchiveSummaryForTable("Pets", petsSql));
        
        // Adoption Requests archive summary
        String requestsSql = "SELECT COUNT(*) as count, MIN(archived_date) as oldest, MAX(archived_date) as newest " +
                            "FROM tbl_adoption_request_archive";
        summaries.add(getArchiveSummaryForTable("Adoption Requests", requestsSql));
        
        // Adopters archive summary
        String adoptersSql = "SELECT COUNT(*) as count, MIN(archived_date) as oldest, MAX(archived_date) as newest " +
                            "FROM tbl_adopter_archive";
        summaries.add(getArchiveSummaryForTable("Adopters", adoptersSql));
        
        // Pet Owners archive summary
        String ownersSql = "SELECT COUNT(*) as count, MIN(archived_date) as oldest, MAX(archived_date) as newest " +
                          "FROM tbl_pet_owner_archive";
        summaries.add(getArchiveSummaryForTable("Pet Owners", ownersSql));
        
        // Adoptions archive summary
        String adoptionsSql = "SELECT COUNT(*) as count, MIN(archived_date) as oldest, MAX(archived_date) as newest " +
                             "FROM tbl_adoption_archive";
        summaries.add(getArchiveSummaryForTable("Adoptions", adoptionsSql));
        
        return summaries;
    }
    
    /**
     * Helper method to get archive summary for a specific table
     * @param entityName Name of the entity type
     * @param sql SQL query to get summary data
     * @return ArchiveSummary object
     */
    private ArchiveSummary getArchiveSummaryForTable(String entityName, String sql) {
        ArchiveSummary summary = new ArchiveSummary();
        summary.setEntityName(entityName);
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                summary.setCount(rs.getInt("count"));
                summary.setOldestDate(rs.getTimestamp("oldest"));
                summary.setNewestDate(rs.getTimestamp("newest"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting archive summary for " + entityName + ": " + e.getMessage());
            summary.setCount(0);
        }
        
        return summary;
    }
    
    /**
     * Validates archive integrity by checking for orphaned records
     * @return List of integrity issues found
     */
    public List<String> validateArchiveIntegrity() {
        List<String> issues = new ArrayList<>();
        
        // Check for archived pets without corresponding owners
        String orphanedPetsSql = "SELECT COUNT(*) FROM tbl_pet_archive pa " +
                               "WHERE NOT EXISTS (SELECT 1 FROM tbl_pet_owner po WHERE po.pet_owner_id = pa.pet_owner_id) " +
                               "AND NOT EXISTS (SELECT 1 FROM tbl_pet_owner_archive poa WHERE poa.pet_owner_id = pa.pet_owner_id)";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(orphanedPetsSql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next() && rs.getInt(1) > 0) {
                issues.add("Found " + rs.getInt(1) + " archived pets without corresponding owners");
            }
            
        } catch (SQLException e) {
            issues.add("Error checking pet archive integrity: " + e.getMessage());
        }
        
        // Check for archived adoption requests without corresponding pets or adopters
        String orphanedRequestsSql = "SELECT COUNT(*) FROM tbl_adoption_request_archive ara " +
                                   "WHERE (NOT EXISTS (SELECT 1 FROM tbl_pet p WHERE p.pet_id = ara.pet_id) " +
                                   "AND NOT EXISTS (SELECT 1 FROM tbl_pet_archive pa WHERE pa.pet_id = ara.pet_id)) " +
                                   "OR (NOT EXISTS (SELECT 1 FROM tbl_adopter a WHERE a.adopter_id = ara.adopter_id) " +
                                   "AND NOT EXISTS (SELECT 1 FROM tbl_adopter_archive aa WHERE aa.adopter_id = ara.adopter_id))";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(orphanedRequestsSql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next() && rs.getInt(1) > 0) {
                issues.add("Found " + rs.getInt(1) + " archived adoption requests with missing references");
            }
            
        } catch (SQLException e) {
            issues.add("Error checking adoption request archive integrity: " + e.getMessage());
        }
        
        return issues;
    }
    
    /**
     * Inner class to represent archive log entries
     */
    public static class ArchiveLogEntry {
        private int logId;
        private String tableName;
        private int recordId;
        private String operation;
        private int performedByUserId;
        private java.sql.Timestamp operationDate;
        private String reason;
        
        // Getters and setters
        public int getLogId() { return logId; }
        public void setLogId(int logId) { this.logId = logId; }
        
        public String getTableName() { return tableName; }
        public void setTableName(String tableName) { this.tableName = tableName; }
        
        public int getRecordId() { return recordId; }
        public void setRecordId(int recordId) { this.recordId = recordId; }
        
        public String getOperation() { return operation; }
        public void setOperation(String operation) { this.operation = operation; }
        
        public int getPerformedByUserId() { return performedByUserId; }
        public void setPerformedByUserId(int performedByUserId) { this.performedByUserId = performedByUserId; }
        
        public java.sql.Timestamp getOperationDate() { return operationDate; }
        public void setOperationDate(java.sql.Timestamp operationDate) { this.operationDate = operationDate; }
        
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }
    
    /**
     * Inner class to represent archive summaries
     */
    public static class ArchiveSummary {
        private String entityName;
        private int count;
        private java.sql.Timestamp oldestDate;
        private java.sql.Timestamp newestDate;
        
        // Getters and setters
        public String getEntityName() { return entityName; }
        public void setEntityName(String entityName) { this.entityName = entityName; }
        
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
        
        public java.sql.Timestamp getOldestDate() { return oldestDate; }
        public void setOldestDate(java.sql.Timestamp oldestDate) { this.oldestDate = oldestDate; }
        
        public java.sql.Timestamp getNewestDate() { return newestDate; }
        public void setNewestDate(java.sql.Timestamp newestDate) { this.newestDate = newestDate; }
    }
}

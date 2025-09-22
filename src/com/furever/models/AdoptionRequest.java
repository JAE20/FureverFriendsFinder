/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.furever.models;

/**
 *
 * @author jerimiahtongco
 */
import java.sql.Date;

/**
 * AdoptionRequest model class representing the tbl_adoption_request table
 */
public class AdoptionRequest {
    private int adoptionRequestId;
    private int petId;
    private int adopterId;
    private Date requestDate;
    private String status;
    private Date approvalDate;
    private String remarks;
    private Integer userId;
    
    // Default constructor
    public AdoptionRequest() {}
    
    // Constructor with basic parameters
    public AdoptionRequest(int petId, int adopterId, String status) {
        this.petId = petId;
        this.adopterId = adopterId;
        this.status = status;
    }
    
    // Constructor with all parameters
    public AdoptionRequest(int adoptionRequestId, int petId, int adopterId, Date requestDate, 
                          String status, Date approvalDate, String remarks, Integer userId) {
        this.adoptionRequestId = adoptionRequestId;
        this.petId = petId;
        this.adopterId = adopterId;
        this.requestDate = requestDate;
        this.status = status;
        this.approvalDate = approvalDate;
        this.remarks = remarks;
        this.userId = userId;
    }
    
    // Getters and Setters
    public int getAdoptionRequestId() {
        return adoptionRequestId;
    }
    
    public void setAdoptionRequestId(int adoptionRequestId) {
        this.adoptionRequestId = adoptionRequestId;
    }
    
    public int getPetId() {
        return petId;
    }
    
    public void setPetId(int petId) {
        this.petId = petId;
    }
    
    public int getAdopterId() {
        return adopterId;
    }
    
    public void setAdopterId(int adopterId) {
        this.adopterId = adopterId;
    }
    
    public Date getRequestDate() {
        return requestDate;
    }
    
    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Date getApprovalDate() {
        return approvalDate;
    }
    
    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    @Override
    public String toString() {
        return "AdoptionRequest{" +
                "adoptionRequestId=" + adoptionRequestId +
                ", petId=" + petId +
                ", adopterId=" + adopterId +
                ", requestDate=" + requestDate +
                ", status='" + status + '\'' +
                ", approvalDate=" + approvalDate +
                ", remarks='" + remarks + '\'' +
                ", userId=" + userId +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AdoptionRequest that = (AdoptionRequest) obj;
        return adoptionRequestId == that.adoptionRequestId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(adoptionRequestId);
    }
}
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
 * Adoption model class representing the tbl_adoption table
 */
public class Adoption {
    private int adoptionId;
    private int petId;
    private int adopterId;
    private Date adoptionDate;
    private String uploadAdoptionDocument;
    private String remarks;
    private Integer userId;
    
    // Default constructor
    public Adoption() {}
    
    // Constructor with basic parameters
    public Adoption(int petId, int adopterId, Date adoptionDate) {
        this.petId = petId;
        this.adopterId = adopterId;
        this.adoptionDate = adoptionDate;
    }
    
    // Constructor with all parameters
    public Adoption(int adoptionId, int petId, int adopterId, Date adoptionDate, 
                   String uploadAdoptionDocument, String remarks, Integer userId) {
        this.adoptionId = adoptionId;
        this.petId = petId;
        this.adopterId = adopterId;
        this.adoptionDate = adoptionDate;
        this.uploadAdoptionDocument = uploadAdoptionDocument;
        this.remarks = remarks;
        this.userId = userId;
    }
    
    // Getters and Setters
    public int getAdoptionId() {
        return adoptionId;
    }
    
    public void setAdoptionId(int adoptionId) {
        this.adoptionId = adoptionId;
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
    
    public Date getAdoptionDate() {
        return adoptionDate;
    }
    
    public void setAdoptionDate(Date adoptionDate) {
        this.adoptionDate = adoptionDate;
    }
    
    public String getUploadAdoptionDocument() {
        return uploadAdoptionDocument;
    }
    
    public void setUploadAdoptionDocument(String uploadAdoptionDocument) {
        this.uploadAdoptionDocument = uploadAdoptionDocument;
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
        return "Adoption{" +
                "adoptionId=" + adoptionId +
                ", petId=" + petId +
                ", adopterId=" + adopterId +
                ", adoptionDate=" + adoptionDate +
                ", uploadAdoptionDocument='" + uploadAdoptionDocument + '\'' +
                ", remarks='" + remarks + '\'' +
                ", userId=" + userId +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Adoption adoption = (Adoption) obj;
        return adoptionId == adoption.adoptionId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(adoptionId);
    }
}

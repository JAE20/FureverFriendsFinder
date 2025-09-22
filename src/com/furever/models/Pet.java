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
 * Pet model class representing the tbl_pet table
 */
public class Pet {
    private int petId;
    private int petOwnerId;
    private String petName;
    private int petTypeId;
    private String description;
    private int age;
    private String gender;
    private String healthStatus;
    private String uploadHealthHistory;
    private String vaccinationStatus;
    private String proofOfVaccination;
    private String adoptionStatus;
    private Date dateRegistered;
    
    // Default constructor
    public Pet() {}
    
    // Constructor with basic parameters
    public Pet(int petOwnerId, String petName, int petTypeId, String description, int age, String gender) {
        this.petOwnerId = petOwnerId;
        this.petName = petName;
        this.petTypeId = petTypeId;
        this.description = description;
        this.age = age;
        this.gender = gender;
    }
    
    // Constructor with all parameters
    public Pet(int petId, int petOwnerId, String petName, int petTypeId, String description, int age, 
               String gender, String healthStatus, String uploadHealthHistory, String vaccinationStatus, 
               String proofOfVaccination, String adoptionStatus, Date dateRegistered) {
        this.petId = petId;
        this.petOwnerId = petOwnerId;
        this.petName = petName;
        this.petTypeId = petTypeId;
        this.description = description;
        this.age = age;
        this.gender = gender;
        this.healthStatus = healthStatus;
        this.uploadHealthHistory = uploadHealthHistory;
        this.vaccinationStatus = vaccinationStatus;
        this.proofOfVaccination = proofOfVaccination;
        this.adoptionStatus = adoptionStatus;
        this.dateRegistered = dateRegistered;
    }
    
    // Getters and Setters
    public int getPetId() {
        return petId;
    }
    
    public void setPetId(int petId) {
        this.petId = petId;
    }
    
    public int getPetOwnerId() {
        return petOwnerId;
    }
    
    public void setPetOwnerId(int petOwnerId) {
        this.petOwnerId = petOwnerId;
    }
    
    public String getPetName() {
        return petName;
    }
    
    public void setPetName(String petName) {
        this.petName = petName;
    }
    
    public int getPetTypeId() {
        return petTypeId;
    }
    
    public void setPetTypeId(int petTypeId) {
        this.petTypeId = petTypeId;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getHealthStatus() {
        return healthStatus;
    }
    
    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }
    
    public String getUploadHealthHistory() {
        return uploadHealthHistory;
    }
    
    public void setUploadHealthHistory(String uploadHealthHistory) {
        this.uploadHealthHistory = uploadHealthHistory;
    }
    
    public String getVaccinationStatus() {
        return vaccinationStatus;
    }
    
    public void setVaccinationStatus(String vaccinationStatus) {
        this.vaccinationStatus = vaccinationStatus;
    }
    
    public String getProofOfVaccination() {
        return proofOfVaccination;
    }
    
    public void setProofOfVaccination(String proofOfVaccination) {
        this.proofOfVaccination = proofOfVaccination;
    }
    
    public String getAdoptionStatus() {
        return adoptionStatus;
    }
    
    public void setAdoptionStatus(String adoptionStatus) {
        this.adoptionStatus = adoptionStatus;
    }
    
    public Date getDateRegistered() {
        return dateRegistered;
    }
    
    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }
    
    @Override
    public String toString() {
        return "Pet{" +
                "petId=" + petId +
                ", petOwnerId=" + petOwnerId +
                ", petName='" + petName + '\'' +
                ", petTypeId=" + petTypeId +
                ", description='" + description + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", healthStatus='" + healthStatus + '\'' +
                ", vaccinationStatus='" + vaccinationStatus + '\'' +
                ", adoptionStatus='" + adoptionStatus + '\'' +
                ", dateRegistered=" + dateRegistered +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pet pet = (Pet) obj;
        return petId == pet.petId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(petId);
    }
}

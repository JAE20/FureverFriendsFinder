/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.furever.models;

/**
 *
 * @author jerimiahtongco
 */

/**
 * PetOwner model class representing the tbl_pet_owner table
 */
public class PetOwner {
    private int petOwnerId;
    private String petOwnerName;
    private String petOwnerContact;
    private String petOwnerEmail;
    private String petOwnerAddress;
    private String petOwnerProfile;
    private String petOwnerUsername;
    private String petOwnerPassword;
    
    // Default constructor
    public PetOwner() {}
    
    // Constructor with basic parameters
    public PetOwner(String petOwnerName, String petOwnerContact, String petOwnerEmail, String petOwnerAddress) {
        this.petOwnerName = petOwnerName;
        this.petOwnerContact = petOwnerContact;
        this.petOwnerEmail = petOwnerEmail;
        this.petOwnerAddress = petOwnerAddress;
    }
    
    // Constructor with all parameters
    public PetOwner(int petOwnerId, String petOwnerName, String petOwnerContact, String petOwnerEmail, 
                    String petOwnerAddress, String petOwnerProfile, String petOwnerUsername, String petOwnerPassword) {
        this.petOwnerId = petOwnerId;
        this.petOwnerName = petOwnerName;
        this.petOwnerContact = petOwnerContact;
        this.petOwnerEmail = petOwnerEmail;
        this.petOwnerAddress = petOwnerAddress;
        this.petOwnerProfile = petOwnerProfile;
        this.petOwnerUsername = petOwnerUsername;
        this.petOwnerPassword = petOwnerPassword;
    }
    
    // Getters and Setters
    public int getPetOwnerId() {
        return petOwnerId;
    }
    
    public void setPetOwnerId(int petOwnerId) {
        this.petOwnerId = petOwnerId;
    }
    
    public String getPetOwnerName() {
        return petOwnerName;
    }
    
    public void setPetOwnerName(String petOwnerName) {
        this.petOwnerName = petOwnerName;
    }
    
    public String getPetOwnerContact() {
        return petOwnerContact;
    }
    
    public void setPetOwnerContact(String petOwnerContact) {
        this.petOwnerContact = petOwnerContact;
    }
    
    public String getPetOwnerEmail() {
        return petOwnerEmail;
    }
    
    public void setPetOwnerEmail(String petOwnerEmail) {
        this.petOwnerEmail = petOwnerEmail;
    }
    
    public String getPetOwnerAddress() {
        return petOwnerAddress;
    }
    
    public void setPetOwnerAddress(String petOwnerAddress) {
        this.petOwnerAddress = petOwnerAddress;
    }
    
    public String getPetOwnerProfile() {
        return petOwnerProfile;
    }
    
    public void setPetOwnerProfile(String petOwnerProfile) {
        this.petOwnerProfile = petOwnerProfile;
    }
    
    public String getPetOwnerUsername() {
        return petOwnerUsername;
    }
    
    public void setPetOwnerUsername(String petOwnerUsername) {
        this.petOwnerUsername = petOwnerUsername;
    }
    
    public String getPetOwnerPassword() {
        return petOwnerPassword;
    }
    
    public void setPetOwnerPassword(String petOwnerPassword) {
        this.petOwnerPassword = petOwnerPassword;
    }
    
    @Override
    public String toString() {
        return "PetOwner{" +
                "petOwnerId=" + petOwnerId +
                ", petOwnerName='" + petOwnerName + '\'' +
                ", petOwnerContact='" + petOwnerContact + '\'' +
                ", petOwnerEmail='" + petOwnerEmail + '\'' +
                ", petOwnerAddress='" + petOwnerAddress + '\'' +
                ", petOwnerUsername='" + petOwnerUsername + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PetOwner petOwner = (PetOwner) obj;
        return petOwnerId == petOwner.petOwnerId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(petOwnerId);
    }
}

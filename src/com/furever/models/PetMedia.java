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
 * PetMedia model class representing the tbl_pet_media table
 */
public class PetMedia {
    private int petMediaId;
    private int petId;
    private String petMediaName;
    private String petMediaUrl;
    
    // Default constructor
    public PetMedia() {}
    
    // Constructor with basic parameters
    public PetMedia(int petId, String petMediaName, String petMediaUrl) {
        this.petId = petId;
        this.petMediaName = petMediaName;
        this.petMediaUrl = petMediaUrl;
    }
    
    // Constructor with all parameters
    public PetMedia(int petMediaId, int petId, String petMediaName, String petMediaUrl) {
        this.petMediaId = petMediaId;
        this.petId = petId;
        this.petMediaName = petMediaName;
        this.petMediaUrl = petMediaUrl;
    }
    
    // Getters and Setters
    public int getPetMediaId() {
        return petMediaId;
    }
    
    public void setPetMediaId(int petMediaId) {
        this.petMediaId = petMediaId;
    }
    
    public int getPetId() {
        return petId;
    }
    
    public void setPetId(int petId) {
        this.petId = petId;
    }
    
    public String getPetMediaName() {
        return petMediaName;
    }
    
    public void setPetMediaName(String petMediaName) {
        this.petMediaName = petMediaName;
    }
    
    public String getPetMediaUrl() {
        return petMediaUrl;
    }
    
    public void setPetMediaUrl(String petMediaUrl) {
        this.petMediaUrl = petMediaUrl;
    }
    
    @Override
    public String toString() {
        return "PetMedia{" +
                "petMediaId=" + petMediaId +
                ", petId=" + petId +
                ", petMediaName='" + petMediaName + '\'' +
                ", petMediaUrl='" + petMediaUrl + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PetMedia petMedia = (PetMedia) obj;
        return petMediaId == petMedia.petMediaId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(petMediaId);
    }
}
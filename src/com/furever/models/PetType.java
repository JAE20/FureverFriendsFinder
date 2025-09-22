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
 * PetType model class representing the tbl_pet_type table
 */
public class PetType {
    private int petTypeId;
    private String petTypeName;
    
    // Default constructor
    public PetType() {}
    
    // Constructor with parameters
    public PetType(String petTypeName) {
        this.petTypeName = petTypeName;
    }
    
    // Constructor with all parameters
    public PetType(int petTypeId, String petTypeName) {
        this.petTypeId = petTypeId;
        this.petTypeName = petTypeName;
    }
    
    // Getters and Setters
    public int getPetTypeId() {
        return petTypeId;
    }
    
    public void setPetTypeId(int petTypeId) {
        this.petTypeId = petTypeId;
    }
    
    public String getPetTypeName() {
        return petTypeName;
    }
    
    public void setPetTypeName(String petTypeName) {
        this.petTypeName = petTypeName;
    }
    
    @Override
    public String toString() {
        return "PetType{" +
                "petTypeId=" + petTypeId +
                ", petTypeName='" + petTypeName + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PetType petType = (PetType) obj;
        return petTypeId == petType.petTypeId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(petTypeId);
    }
}
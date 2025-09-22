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
 * Adopter model class representing the tbl_adopter table
 */
public class Adopter {
    private int adopterId;
    private String adopterName;
    private String adopterContact;
    private String adopterEmail;
    private String adopterAddress;
    private String adopterProfile;
    private String adopterUsername;
    private String adopterPassword;
    
    // Default constructor
    public Adopter() {}
    
    // Constructor with basic parameters
    public Adopter(String adopterName, String adopterContact, String adopterEmail, String adopterAddress) {
        this.adopterName = adopterName;
        this.adopterContact = adopterContact;
        this.adopterEmail = adopterEmail;
        this.adopterAddress = adopterAddress;
    }
    
    // Constructor with all parameters
    public Adopter(int adopterId, String adopterName, String adopterContact, String adopterEmail, 
                   String adopterAddress, String adopterProfile, String adopterUsername, String adopterPassword) {
        this.adopterId = adopterId;
        this.adopterName = adopterName;
        this.adopterContact = adopterContact;
        this.adopterEmail = adopterEmail;
        this.adopterAddress = adopterAddress;
        this.adopterProfile = adopterProfile;
        this.adopterUsername = adopterUsername;
        this.adopterPassword = adopterPassword;
    }
    
    // Getters and Setters
    public int getAdopterId() {
        return adopterId;
    }
    
    public void setAdopterId(int adopterId) {
        this.adopterId = adopterId;
    }
    
    public String getAdopterName() {
        return adopterName;
    }
    
    public void setAdopterName(String adopterName) {
        this.adopterName = adopterName;
    }
    
    public String getAdopterContact() {
        return adopterContact;
    }
    
    public void setAdopterContact(String adopterContact) {
        this.adopterContact = adopterContact;
    }
    
    public String getAdopterEmail() {
        return adopterEmail;
    }
    
    public void setAdopterEmail(String adopterEmail) {
        this.adopterEmail = adopterEmail;
    }
    
    public String getAdopterAddress() {
        return adopterAddress;
    }
    
    public void setAdopterAddress(String adopterAddress) {
        this.adopterAddress = adopterAddress;
    }
    
    public String getAdopterProfile() {
        return adopterProfile;
    }
    
    public void setAdopterProfile(String adopterProfile) {
        this.adopterProfile = adopterProfile;
    }
    
    public String getAdopterUsername() {
        return adopterUsername;
    }
    
    public void setAdopterUsername(String adopterUsername) {
        this.adopterUsername = adopterUsername;
    }
    
    public String getAdopterPassword() {
        return adopterPassword;
    }
    
    public void setAdopterPassword(String adopterPassword) {
        this.adopterPassword = adopterPassword;
    }
    
    @Override
    public String toString() {
        return "Adopter{" +
                "adopterId=" + adopterId +
                ", adopterName='" + adopterName + '\'' +
                ", adopterContact='" + adopterContact + '\'' +
                ", adopterEmail='" + adopterEmail + '\'' +
                ", adopterAddress='" + adopterAddress + '\'' +
                ", adopterUsername='" + adopterUsername + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Adopter adopter = (Adopter) obj;
        return adopterId == adopter.adopterId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(adopterId);
    }
}

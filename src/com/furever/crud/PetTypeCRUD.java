/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.furever.crud;


import com.furever.database.DbConnection;
import com.furever.models.PetType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PetTypeCRUD {

    private static final String INDENT = "\t\t\t\t\t";

    public boolean createPetType(PetType petType) {
        String sql = "INSERT INTO tbl_pet_type (pet_type_name) VALUES (?)";

        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, petType.getPetTypeName());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        petType.setPetTypeId(generatedKeys.getInt(1));
                    }
                }
                System.out.println(INDENT + "Pet type created successfully with ID: " + petType.getPetTypeId());
                return true;
            }

        } catch (SQLException e) {
            System.err.println(INDENT + "Error creating pet type: " + e.getMessage());
        }

        return false;
    }

 
    public PetType getPetTypeById(int petTypeId) {
        String sql = "SELECT * FROM tbl_pet_type WHERE pet_type_id = ?";

        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, petTypeId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractPetTypeFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println(INDENT + "Error retrieving pet type: " + e.getMessage());
        }

        return null;
    }

    public List<PetType> getAllPetTypes() {
        List<PetType> petTypes = new ArrayList<>();
        String sql = "SELECT * FROM tbl_pet_type ORDER BY pet_type_name";

        try (Connection conn = DbConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                petTypes.add(extractPetTypeFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println(INDENT + "Error retrieving all pet types: " + e.getMessage());
        }

        return petTypes;
    }

 
    public boolean updatePetType(PetType petType) {
        String sql = "UPDATE tbl_pet_type SET pet_type_name = ? WHERE pet_type_id = ?";

        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, petType.getPetTypeName());
            pstmt.setInt(2, petType.getPetTypeId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(INDENT + "Pet type updated successfully.");
                return true;
            } else {
                System.out.println(INDENT + "No pet type found with ID: " + petType.getPetTypeId());
            }

        } catch (SQLException e) {
            System.err.println(INDENT + "Error updating pet type: " + e.getMessage());
        }

        return false;
    }


    public boolean deletePetType(int petTypeId) {
        String sql = "DELETE FROM tbl_pet_type WHERE pet_type_id = ?";

        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, petTypeId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(INDENT + "Pet type deleted successfully.");
                return true;
            } else {
                System.out.println(INDENT + "No pet type found with ID: " + petTypeId);
            }

        } catch (SQLException e) {
            System.err.println(INDENT + "Error deleting pet type: " + e.getMessage());
        }

        return false;
    }

    private PetType extractPetTypeFromResultSet(ResultSet rs) throws SQLException {
        PetType petType = new PetType();
        petType.setPetTypeId(rs.getInt("pet_type_id"));
        petType.setPetTypeName(rs.getString("pet_type_name"));
        return petType;
    }
}

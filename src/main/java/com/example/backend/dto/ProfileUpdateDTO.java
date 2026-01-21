package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la mise à jour du profil utilisateur
 * @author Wulfrid MBONGO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateDTO {
    private String nom;
    private String prenom;
    private String email;
    private String numeroTelephone;
    private String photoProfil;
}

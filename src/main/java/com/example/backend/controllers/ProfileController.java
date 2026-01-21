package com.example.backend.controllers;

import com.example.backend.dto.ProfileUpdateDTO;
import com.example.backend.dto.UtilisateurDTO;
import com.example.backend.services.implementations.UtilisateurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour la gestion des profils utilisateurs
 * @author Wulfrid MBONGO
 */
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    private final UtilisateurService utilisateurService;

    public ProfileController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    /**
     * Récupère le profil d'un utilisateur
     */
    @GetMapping("/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UtilisateurDTO> getProfile(@PathVariable Long userId) {
        logger.info("Récupération du profil userId={}", userId);
        try {
            UtilisateurDTO profile = utilisateurService.getUtilisateurById(userId);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du profil userId={}", userId, e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Met à jour le profil d'un utilisateur
     */
    @PutMapping("/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UtilisateurDTO> updateProfile(
            @PathVariable Long userId,
            @RequestBody ProfileUpdateDTO profileDTO) {
        logger.info("Mise à jour du profil userId={}", userId);
        try {
            UtilisateurDTO updatedProfile = utilisateurService.updateProfile(userId, profileDTO);
            return ResponseEntity.ok(updatedProfile);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du profil userId={}", userId, e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Met à jour uniquement la photo de profil
     */
    @PutMapping("/{userId}/photo")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UtilisateurDTO> updateProfilePhoto(
            @PathVariable Long userId,
            @RequestBody String photoUrl) {
        logger.info("Mise à jour de la photo de profil userId={}", userId);
        try {
            UtilisateurDTO updatedProfile = utilisateurService.updateProfilePhoto(userId, photoUrl);
            return ResponseEntity.ok(updatedProfile);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de la photo userId={}", userId, e);
            return ResponseEntity.badRequest().build();
        }
    }
}

package com.example.backend.controllers;

import com.example.backend.entities.Proprietaire;
import com.example.backend.repositories.ProprietaireRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Contrôleur pour les opérations sur les propriétaires
 * @author Wulfrid MBONGO
 */
@RestController
@RequestMapping("/api/proprietaires")
@CrossOrigin(origins = "*")
public class ProprietaireController {

    private static final Logger logger = LoggerFactory.getLogger(ProprietaireController.class);

    private final ProprietaireRepository proprietaireRepository;

    public ProprietaireController(ProprietaireRepository proprietaireRepository) {
        this.proprietaireRepository = proprietaireRepository;
    }

    /**
     * Récupère un propriétaire par son userId
     * GET /api/proprietaires/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getProprietaireByUserId(@PathVariable Long userId) {
        logger.info("Requête pour récupérer le propriétaire avec userId={}", userId);
        
        try {
            Proprietaire proprietaire = proprietaireRepository.findByIdUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Propriétaire non trouvé pour l'utilisateur ID: " + userId));
            
            // Retourner seulement l'ID pour éviter la boucle de sérialisation JSON
            return ResponseEntity.ok(Map.of("id", proprietaire.getId()));
        } catch (RuntimeException e) {
            logger.error("Erreur lors de la récupération du propriétaire userId={}", userId, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la récupération du propriétaire userId={}", userId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Récupère un propriétaire par son ID
     * GET /api/proprietaires/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PROPRIETAIRE', 'ADMIN')")
    public ResponseEntity<?> getProprietaireById(@PathVariable Long id) {
        logger.info("Requête pour récupérer le propriétaire avec id={}", id);
        
        try {
            Proprietaire proprietaire = proprietaireRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Propriétaire non trouvé avec ID: " + id));
            
            return ResponseEntity.ok(proprietaire);
        } catch (RuntimeException e) {
            logger.error("Erreur lors de la récupération du propriétaire id={}", id, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la récupération du propriétaire id={}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}

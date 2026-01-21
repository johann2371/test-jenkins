package com.example.backend.controllers;

import com.example.backend.dto.FavoriDTO;
import com.example.backend.services.implementations.FavoriService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favori")
@CrossOrigin(origins = "*")
public class FavoriController {

    private static final Logger logger = LoggerFactory.getLogger(FavoriController.class);
    private final FavoriService favoriService;

    public FavoriController(FavoriService favoriService) {
        this.favoriService = favoriService;
    }

    /**
     * Récupère tous les favoris d'un voyageur
     * GET /api/favori/voyageur/{voyageurId}
     */
    @GetMapping("/voyageur/{voyageurId}")
    public ResponseEntity<?> getFavorisByVoyageur(@PathVariable Long voyageurId) {
        logger.info("Récupération des favoris du voyageur ID={}", voyageurId);
        try {
            List<FavoriDTO> favoris = favoriService.listerFavorisByVoyageur(voyageurId);
            return ResponseEntity.ok(favoris);
        } catch (Exception e) {
            logger.error("Erreur récupération favoris voyageur ID={}", voyageurId, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Récupère un favori par ID
     * GET /api/favori/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getFavoriById(@PathVariable Long id) {
        logger.info("Récupération favori ID={}", id);
        try {
            FavoriDTO favori = favoriService.obtenirFavoriParId(id);
            return ResponseEntity.ok(favori);
        } catch (Exception e) {
            logger.error("Erreur récupération favori ID={}", id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Crée un nouveau favori
     * POST /api/favori
     */
    @PostMapping
    public ResponseEntity<?> createFavori(@RequestBody FavoriDTO favoriDTO) {
        logger.info("Création favori pour voyageur ID={}, annonce ID={}",
                favoriDTO.getIdVoyageur(), favoriDTO.getIdAnnonce());
        logger.info("Requête brute reçue: {}", favoriDTO);
        logger.info("Type de requestBody: {}", favoriDTO.getClass().getName());
        try {

            FavoriDTO createdFavori = favoriService.ajouterFavori(favoriDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFavori);
        } catch (Exception e) {
            logger.error("Erreur création favori", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Met à jour un favori
     * PUT /api/favori/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFavori(@PathVariable Long id, @RequestBody FavoriDTO favoriDTO) {
        logger.info("Mise à jour favori ID={}", id);
        try {
            FavoriDTO updatedFavori = favoriService.mettreAJourFavori(id, favoriDTO);
            return ResponseEntity.ok(updatedFavori);
        } catch (Exception e) {
            logger.error("Erreur mise à jour favori ID={}", id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Supprime un favori
     * DELETE /api/favori/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFavori(@PathVariable Long id) {
        logger.info("Suppression favori ID={}", id);
        try {
            favoriService.supprimerFavori(id);
            return ResponseEntity.ok("Favori supprimé avec succès");
        } catch (Exception e) {
            logger.error("Erreur suppression favori ID={}", id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
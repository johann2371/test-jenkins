package com.example.backend.controllers;

import com.example.backend.dto.AdminStatsDTO;
import com.example.backend.dto.OwnerStatsDTO;
import com.example.backend.services.implementations.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour les statistiques de l'application
 * @author Wulfrid MBONGO
 */
@RestController
@RequestMapping("api/statistics")
public class StatisticsController {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * Récupère les statistiques globales pour l'administrateur
     */
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AdminStatsDTO> getAdminStatistics() {
        logger.info("Requête pour les statistiques admin");
        try {
            AdminStatsDTO stats = statisticsService.calculateAdminStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Erreur lors du calcul des statistiques admin", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Récupère les statistiques pour un propriétaire spécifique
     */
    @GetMapping("/owner/{userId}")
    @PreAuthorize("hasAuthority('PROPRIETAIRE')")
    public ResponseEntity<OwnerStatsDTO> getOwnerStatistics(@PathVariable Long userId) {
        logger.info("Requête pour les statistiques du propriétaire userId={}", userId);
        try {
            OwnerStatsDTO stats = statisticsService.calculateOwnerStats(userId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Erreur lors du calcul des statistiques propriétaire userId={}", userId, e);
            return ResponseEntity.badRequest().build();
        }
    }
}
//bonjour les dev
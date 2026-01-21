package com.example.backend.services.implementations;

import com.example.backend.dto.AdminStatsDTO;
import com.example.backend.dto.OwnerStatsDTO;
import com.example.backend.entities.Proprietaire;
import com.example.backend.repositories.*;
import com.example.backend.roles.RoleUtilisateur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service pour calculer les statistiques de l'application
 * @author Wulfrid MBONGO
 */
@Service
public class StatisticsService {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsService.class);

    private final UtilisateurRepository utilisateurRepository;
    private final AnnoncesRepository annoncesRepository;
    private final ReservationRepository reservationRepository;
    private final PaiementRepository paiementRepository;
    private final ProprietaireRepository proprietaireRepository;

    public StatisticsService(
            UtilisateurRepository utilisateurRepository,
            AnnoncesRepository annoncesRepository,
            ReservationRepository reservationRepository,
            PaiementRepository paiementRepository,
            ProprietaireRepository proprietaireRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.annoncesRepository = annoncesRepository;
        this.reservationRepository = reservationRepository;
        this.paiementRepository = paiementRepository;
        this.proprietaireRepository = proprietaireRepository;
    }

    /**
     * Calcule les statistiques globales pour l'administrateur
     */
    public AdminStatsDTO calculateAdminStats() {
        logger.info("Calcul des statistiques admin");

        // Total utilisateurs
        Long totalUsers = utilisateurRepository.count();

        // Total annonces actives
        Long totalAnnonces = annoncesRepository.countAnnoncesActives();

        // Total réservations
        Long totalReservations = reservationRepository.count();

        // Revenu total (paiements validés)
        Double revenuTotal = paiementRepository.sumMontantByStatut("REUSSI");
        if (revenuTotal == null) {
            revenuTotal = 0.0;
        }

        // Calcul du taux d'occupation moyen
        Double tauxOccupation = calculateTauxOccupationGlobal();

        // Villes populaires
        List<AdminStatsDTO.VilleStatDTO> villesPopulaires = getVillesPopulaires();

        logger.info("Statistiques admin calculées: {} utilisateurs, {} annonces, {} réservations",
                totalUsers, totalAnnonces, totalReservations);

        return new AdminStatsDTO(
                totalUsers,
                totalAnnonces,
                totalReservations,
                revenuTotal,
                tauxOccupation,
                villesPopulaires
        );
    }

    /**
     * Calcule les statistiques pour un propriétaire spécifique
     */
    public OwnerStatsDTO calculateOwnerStats(Long userId) {
        logger.info("Calcul des statistiques pour le propriétaire userId={}", userId);

        // Trouver le propriétaire
        Proprietaire proprietaire = proprietaireRepository.findByIdUserId(userId)
                .orElseThrow(() -> new RuntimeException("Propriétaire non trouvé pour l'utilisateur ID: " + userId));

        // Total annonces du propriétaire
        Long totalAnnonces = (long) annoncesRepository.findByIdProprietaire_Id(proprietaire.getId()).size();

        // Total réservations sur ses annonces
        Long totalReservations = (long) reservationRepository.findByProprietaireUserId(userId).size();

        // Revenu total généré
        Double revenuTotal = paiementRepository.sumMontantByProprietaireUserId(userId);
        if (revenuTotal == null) {
            revenuTotal = 0.0;
        }

        // Taux d'occupation des annonces du propriétaire
        Double tauxOccupation = calculateTauxOccupationProprietaire(proprietaire.getId());

        // Évaluation moyenne (depuis l'entité Proprietaire)
        Double evaluationMoyenne = proprietaire.getEvaluationMoyenne();
        if (evaluationMoyenne == null) {
            evaluationMoyenne = 0.0;
        }

        logger.info("Statistiques propriétaire calculées: {} annonces, {} réservations, revenu={}",
                totalAnnonces, totalReservations, revenuTotal);

        return new OwnerStatsDTO(
                totalAnnonces,
                totalReservations,
                revenuTotal,
                tauxOccupation,
                evaluationMoyenne
        );
    }

    /**
     * Calcule le taux d'occupation global de toutes les annonces actives
     */
    private Double calculateTauxOccupationGlobal() {
        // Simplified calculation: ratio of confirmed reservations to total annonces
        Long totalAnnonces = annoncesRepository.countAnnoncesActives();
        if (totalAnnonces == 0) {
            return 0.0;
        }

        Long reservationsConfirmees = reservationRepository.countByStatut("CONFIRMEE");
        if (reservationsConfirmees == null) {
            reservationsConfirmees = 0L;
        }

        // Simple percentage: (confirmées / annonces) * 100, capped at 100
        double taux = (reservationsConfirmees.doubleValue() / totalAnnonces.doubleValue()) * 100;
        return Math.min(taux, 100.0);
    }

    /**
     * Calcule le taux d'occupation pour les annonces d'un propriétaire
     */
    private Double calculateTauxOccupationProprietaire(Long proprietaireId) {
        List<com.example.backend.entities.Annonces> annonces = annoncesRepository.findByIdProprietaire_Id(proprietaireId);
        
        if (annonces.isEmpty()) {
            return 0.0;
        }

        // Compter les réservations confirmées pour ce propriétaire
        Long reservationsConfirmees = reservationRepository.findByProprietaireUserId(
                annonces.get(0).getIdProprietaire().getIdUser().getId()
        ).stream()
        .filter(r -> "CONFIRMEE".equals(r.getStatut()))
        .count();

        // Simple percentage
        double taux = (reservationsConfirmees.doubleValue() / annonces.size()) * 100;
        return Math.min(taux, 100.0);
    }

    /**
     * Récupère les villes les plus populaires par nombre d'annonces
     */
    private List<AdminStatsDTO.VilleStatDTO> getVillesPopulaires() {
        List<Object[]> results = annoncesRepository.countAnnoncesByVille();
        List<AdminStatsDTO.VilleStatDTO> villesPopulaires = new ArrayList<>();

        for (Object[] result : results) {
            String ville = (String) result[0];
            Long count = (Long) result[1];
            villesPopulaires.add(new AdminStatsDTO.VilleStatDTO(ville, count));
        }

        // Trier par count décroissant et prendre les 5 premières
        villesPopulaires.sort((a, b) -> b.getCount().compareTo(a.getCount()));
        return villesPopulaires.stream().limit(5).toList();
    }
}

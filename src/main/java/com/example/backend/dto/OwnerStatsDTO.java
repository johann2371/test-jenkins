package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les statistiques du propriétaire
 * @author Wulfrid MBONGO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerStatsDTO {
    private Long totalAnnonces;
    private Long totalReservations;
    private Double revenuTotal;
    private Double tauxOccupation;
    private Double evaluationMoyenne;
}

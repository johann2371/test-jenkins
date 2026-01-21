package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO pour les statistiques de l'administrateur
 * @author Wulfrid MBONGO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatsDTO {
    private Long totalUsers;
    private Long totalAnnonces;
    private Long totalReservations;
    private Double revenuTotal;
    private Double tauxOccupation;
    private List<VilleStatDTO> villesPopulaires;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VilleStatDTO {
        private String ville;
        private Long count;
    }
}

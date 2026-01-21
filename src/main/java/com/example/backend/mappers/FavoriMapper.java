package com.example.backend.mappers;

import com.example.backend.dto.FavoriDTO;
import com.example.backend.entities.Annonces;
import com.example.backend.entities.Favori;
import com.example.backend.entities.Voyageur;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

@Component
public class FavoriMapper {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    /**
     * Convertit un FavoriDTO en entité Favori
     * Utilise EntityManager.getReference() pour créer des proxies JPA
     * au lieu de nouvelles instances, permettant à Hibernate de gérer
     * correctement les relations @ManyToOne
     */
    public Favori toEntity(FavoriDTO favoriDTO) {
        if (favoriDTO == null) {
            return null;
        }
        
        Favori favori = new Favori();
        favori.setId(favoriDTO.getId());
        
        // Utiliser EntityManager.getReference() pour créer des références JPA
        // Cela crée un proxy sans charger l'entité complète de la DB
        if (favoriDTO.getIdVoyageur() != null) {
            Voyageur voyageur = entityManager.getReference(Voyageur.class, favoriDTO.getIdVoyageur());
            favori.setIdVoyageur(voyageur);
        }
        
        if (favoriDTO.getIdAnnonce() != null) {
            Annonces annonce = entityManager.getReference(Annonces.class, favoriDTO.getIdAnnonce());
            favori.setIdAnnonce(annonce);
        }
        
        return favori;
    }
    
    /**
     * Convertit une entité Favori en FavoriDTO
     * Les références d'entités sont converties en IDs Long
     */
    public FavoriDTO toDTO(Favori favori) {
        if (favori == null) {
            return null;
        }
        
        FavoriDTO favoriDTO = new FavoriDTO();
        favoriDTO.setId(favori.getId());
        
        // Extraire les IDs des références d'entités
        if (favori.getIdVoyageur() != null) {
            favoriDTO.setIdVoyageur(favori.getIdVoyageur().getId());
        }
        
        if (favori.getIdAnnonce() != null) {
            favoriDTO.setIdAnnonce(favori.getIdAnnonce().getId());
        }
        
        return favoriDTO;
    }
}

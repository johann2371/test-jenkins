package com.example.backend.repositories;


import com.example.backend.entities.Favori;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriRepos extends JpaRepository<Favori, Long> {
    /**
     * Récupère les favoris d'un voyageur
     */
    @Query("SELECT f FROM Favori f WHERE f.idVoyageur.id = :voyageurId")
    List<Favori> findByIdVoyageur_Id(@Param("voyageurId") Long voyageurId);

    /**
     * Vérifie si un favori existe déjà
     */
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Favori f WHERE f.idVoyageur.id = :voyageurId AND f.idAnnonce.id = :annonceId")
    boolean existsByIdVoyageur_IdAndIdAnnonce_Id(@Param("voyageurId") Long voyageurId, @Param("annonceId") Long annonceId);
}

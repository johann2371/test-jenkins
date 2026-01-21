package com.example.backend.repositories;

import com.example.backend.entities.Voyageur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoyageurRepository extends JpaRepository<Voyageur,Long> {
    
    /**
     * Recherche un voyageur par l'ID de son utilisateur associé
     * @param userId L'ID de l'utilisateur
     * @return Optional contenant le voyageur si trouvé
     */
    Optional<Voyageur> findByIdUser_Id(Long userId);
}

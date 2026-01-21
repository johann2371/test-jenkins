package com.example.backend.repositories;

import com.example.backend.entities.Avis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvisRepos extends JpaRepository<Avis, Long> {
    List<Avis> findByIdAnnonce_Id(Long annonceId);
    List<Avis> findByIdAnnonce_IdProprietaire_Id(Long proprietaireId);
}

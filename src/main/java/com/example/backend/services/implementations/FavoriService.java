package com.example.backend.services.implementations;

import com.example.backend.dto.FavoriDTO;
import com.example.backend.entities.Favori;
import com.example.backend.entities.Annonces;
import com.example.backend.entities.Voyageur;
import com.example.backend.mappers.FavoriMapper;
import com.example.backend.repositories.AnnoncesRepos;
import com.example.backend.repositories.FavoriRepos;
import com.example.backend.repositories.VoyageurRepos;
import com.example.backend.services.interfaces.FavoriInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriService implements FavoriInterface {

    private static final Logger logger = LoggerFactory.getLogger(FavoriService.class);

    private final FavoriMapper favoriMapper;
    private final FavoriRepos favoriRepos;
    private final VoyageurRepos voyageurRepos;
    private final AnnoncesRepos annoncesRepos;

    public FavoriService(FavoriMapper favoriMapper,
                         FavoriRepos favoriRepos,
                         VoyageurRepos voyageurRepos,
                         AnnoncesRepos annoncesRepos) {
        this.favoriMapper = favoriMapper;
        this.favoriRepos = favoriRepos;
        this.voyageurRepos = voyageurRepos;
        this.annoncesRepos = annoncesRepos;
    }

    /**
     * Liste les favoris d'un voyageur spécifique
     */
    public List<FavoriDTO> listerFavorisByVoyageur(Long voyageurId) {
        logger.info("Récupération favoris voyageur ID={}", voyageurId);

        if (!voyageurRepos.existsById(voyageurId)) {
            logger.warn("Voyageur inexistant ID={}", voyageurId);
            throw new IllegalArgumentException("Voyageur inexistant");
        }

        return favoriRepos.findByIdVoyageur_Id(voyageurId)
                .stream()
                .map(favoriMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FavoriDTO ajouterFavori(FavoriDTO favoriDTO) {
        logger.info("Ajout favori voyageur ID={}, annonce ID={}",
                favoriDTO.getIdVoyageur(), favoriDTO.getIdAnnonce());

        if (!annoncesRepos.existsById(favoriDTO.getIdAnnonce())
                || !voyageurRepos.existsById(favoriDTO.getIdVoyageur())) {

            logger.warn("Ajout favori refusé : annonce ou voyageur inexistant");
            throw new IllegalArgumentException(
                    "L'ID de l'annonce et l'ID du voyageur sont requis et doivent exister.");
        }

        // Vérifier si le favori existe déjà
        boolean exists = favoriRepos.existsByIdVoyageur_IdAndIdAnnonce_Id(
                favoriDTO.getIdVoyageur(),
                favoriDTO.getIdAnnonce()
        );

        if (exists) {
            logger.warn("Favori déjà existant");
            throw new IllegalArgumentException("Cette annonce est déjà dans vos favoris");
        }

        try {
            Favori favori = favoriMapper.toEntity(favoriDTO);
            Favori savedFavori = favoriRepos.save(favori);

            logger.info("Favori ajouté avec succès ID={}", savedFavori.getId());
            return favoriMapper.toDTO(savedFavori);

        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout du favori", e);
            throw new RuntimeException("Erreur lors de l'ajout du favori: " + e.getMessage());
        }
    }

    @Override
    public List<FavoriDTO> listerFavoris() {
        logger.info("Récupération de tous les favoris");

        return favoriRepos.findAll()
                .stream()
                .map(favoriMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FavoriDTO mettreAJourFavori(Long id, FavoriDTO favoriDTO) {
        logger.info("Mise à jour favori ID={}", id);

        Favori favori1 = favoriRepos.findById(id).orElse(null);

        if (favori1 != null) {
            try {
                // Créer les références d'entités à partir des IDs
                if (favoriDTO.getIdAnnonce() != null) {
                    Annonces annonce = new Annonces(favoriDTO.getIdAnnonce());
                    favori1.setIdAnnonce(annonce);
                }
                if (favoriDTO.getIdVoyageur() != null) {
                    Voyageur voyageur = new Voyageur(favoriDTO.getIdVoyageur());
                    favori1.setIdVoyageur(voyageur);
                }

                Favori updatedFavori = favoriRepos.save(favori1);
                logger.info("Favori mis à jour avec succès ID={}", id);

                return favoriMapper.toDTO(updatedFavori);

            } catch (Exception e) {
                logger.error("Erreur mise à jour favori ID={}", id, e);
                throw new RuntimeException("Erreur lors de la mise à jour: " + e.getMessage());
            }

        } else {
            logger.warn("Favori non trouvé pour mise à jour ID={}", id);
            throw new RuntimeException("Favori non trouvé avec l'id: " + id);
        }
    }

    @Override
    public void supprimerFavori(Long id) {
        logger.info("Suppression favori ID={}", id);

        if (favoriRepos.existsById(id)) {
            favoriRepos.deleteById(id);
            logger.info("Favori supprimé avec succès ID={}", id);
        } else {
            logger.warn("Suppression impossible : favori non trouvé ID={}", id);
            throw new RuntimeException("Favori non trouvé avec l'id: " + id);
        }
    }

    @Override
    public FavoriDTO obtenirFavoriParId(Long id) {
        logger.info("Récupération favori ID={}", id);

        Favori favori1 = favoriRepos.findById(id).orElse(null);

        if (favori1 != null) {
            try {
                logger.debug("Favori récupéré : {}", favori1);
                return favoriMapper.toDTO(favori1);

            } catch (Exception e) {
                logger.error("Erreur récupération favori ID={}", id, e);
                throw new RuntimeException("Erreur: " + e.getMessage());
            }

        } else {
            logger.warn("Favori non trouvé ID={}", id);
            throw new RuntimeException("Favori non trouvé avec l'id: " + id);
        }
    }
}
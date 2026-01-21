package com.example.backend.dto;

import java.util.Date;

public class ReservationResponseDTO {
    private Long id;
    private String codeConfirmation;
    private Integer nombreInvites;
    private Double prixTotal;
    private Date dateDebut;
    private Date dateFin;
    private String statut;
    
    // Objets imbriqués pour correspondre au frontend
    private AnnonceInfo idAnnonces;
    private VoyageurInfo idVoyageur;

    // Classes internes pour structurer les données
    public static class AnnonceInfo {
        private Long id;
        private String titre;
        private String ville;

        public AnnonceInfo() {}

        public AnnonceInfo(Long id, String titre, String ville) {
            this.id = id;
            this.titre = titre;
            this.ville = ville;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitre() {
            return titre;
        }

        public void setTitre(String titre) {
            this.titre = titre;
        }

        public String getVille() {
            return ville;
        }

        public void setVille(String ville) {
            this.ville = ville;
        }
    }

    public static class VoyageurInfo {
        private Long id;
        private UserInfo idUser;

        public VoyageurInfo() {}

        public VoyageurInfo(Long id, UserInfo idUser) {
            this.id = id;
            this.idUser = idUser;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public UserInfo getIdUser() {
            return idUser;
        }

        public void setIdUser(UserInfo idUser) {
            this.idUser = idUser;
        }
    }

    public static class UserInfo {
        private String nom;
        private String prenom;
        private String email;
        private String numeroTelephone;

        public UserInfo() {}

        public UserInfo(String nom, String prenom, String email, String numeroTelephone) {
            this.nom = nom;
            this.prenom = prenom;
            this.email = email;
            this.numeroTelephone = numeroTelephone;
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String getPrenom() {
            return prenom;
        }

        public void setPrenom(String prenom) {
            this.prenom = prenom;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getNumeroTelephone() {
            return numeroTelephone;
        }

        public void setNumeroTelephone(String numeroTelephone) {
            this.numeroTelephone = numeroTelephone;
        }
    }

    // Constructeurs
    public ReservationResponseDTO() {
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeConfirmation() {
        return codeConfirmation;
    }

    public void setCodeConfirmation(String codeConfirmation) {
        this.codeConfirmation = codeConfirmation;
    }

    public Integer getNombreInvites() {
        return nombreInvites;
    }

    public void setNombreInvites(Integer nombreInvites) {
        this.nombreInvites = nombreInvites;
    }

    public Double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(Double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public AnnonceInfo getIdAnnonces() {
        return idAnnonces;
    }

    public void setIdAnnonces(AnnonceInfo idAnnonces) {
        this.idAnnonces = idAnnonces;
    }

    public VoyageurInfo getIdVoyageur() {
        return idVoyageur;
    }

    public void setIdVoyageur(VoyageurInfo idVoyageur) {
        this.idVoyageur = idVoyageur;
    }
}


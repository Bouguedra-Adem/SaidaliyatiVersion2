package io.pharmacie.models;


public class pharmacy {
    private String nomPrenomPharmacien;
    private String heure;
    private String adresse;
    private String numeroTelephone;
    private String facebookUrl;
    private String caisseConventionnee;
    private String dateGarde;

    public pharmacy(String nomPrenomPharmacien, String heure, String adresse, String numeroTelephone,
                    String facebookUrl, String caisseConventionnee, String dateGarde) {
        this.nomPrenomPharmacien = nomPrenomPharmacien;
        this.heure = heure;
        this.adresse = adresse;
        this.numeroTelephone = numeroTelephone;
        this.facebookUrl = facebookUrl;
        this.caisseConventionnee = caisseConventionnee;
        this.dateGarde = dateGarde;
    }

    public String toString() {
        return nomPrenomPharmacien;
    }

    public String getHeure() {
        return heure;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getNumeroTelephone() {
        return numeroTelephone;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public String getCaisseConventionnee() {
        return caisseConventionnee;
    }

    public String getDateGarde() {
        return dateGarde;
    }
}

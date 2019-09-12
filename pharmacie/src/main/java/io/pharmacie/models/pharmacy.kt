package io.pharmacie.models


class pharmacy(
     val nomPrenomPharmacien: String, val heure: String, val adresse: String, val numeroTelephone: String,
    val facebookUrl: String, val caisseConventionnee: String, val dateGarde: String
) {

    override fun toString(): String {
        return nomPrenomPharmacien
    }
}

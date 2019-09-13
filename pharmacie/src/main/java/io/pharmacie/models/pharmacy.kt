package io.pharmacie.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class pharmacy(
    @PrimaryKey val nomPrenomPharmacien: String, val heure: String, val adresse: String, val numeroTelephone: String,
    val facebookUrl: String, val caisseConventionnee: String, val dateGarde: String
) {

    override fun toString(): String {
        return nomPrenomPharmacien
    }
}

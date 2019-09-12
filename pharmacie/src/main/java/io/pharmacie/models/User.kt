package io.pharmacie.models

class User(
    private val lastname: String,
    private val firstname: String,
    private val email: String,
    private val phone_number: String,
    private val NSS: String,
    private val password: String
) {

    override fun toString(): String {
        return this.phone_number
    }
}
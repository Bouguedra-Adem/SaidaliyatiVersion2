package io.pharmacie.models;
public class User {
    private String lastname;
    private String firstname;
    private String email;
    private String phone_number;
    private String NSS;
    private String password;


    public User(String lastname, String firstname, String email, String phoneNumber, String NSS, String password) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.phone_number = phoneNumber;
        this.NSS = NSS;
        this.password = password;
    }

    public String toString(){
        return this.phone_number;
    }
}
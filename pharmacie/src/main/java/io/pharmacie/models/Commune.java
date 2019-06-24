package io.pharmacie.models;

public class Commune {
    private String nomCommune;
    private int codeWilaya;

    public int getWiyalaCode(){
        return this.codeWilaya;
    }
    public String toString() {
        return nomCommune;
    }

    public Commune(String name,int wilayacode){
        this.nomCommune = name;
        this.codeWilaya = wilayacode;
    }

    public String getNomCommune() {
        return nomCommune;
    }

    public int getCodeWilaya() {
        return codeWilaya;
    }

    public void setNomCommune(String nomCommune) {
        this.nomCommune = nomCommune;
    }

    public void setCodeWilaya(int codeWilaya) {
        this.codeWilaya = codeWilaya;
    }
}

package io.pharmacie.models;

public class Camand {

    private String nomCmd;
    private String userEmail ;
    private String cmdState;
    private String imgNamr;
    private String dateLancement;

    public Camand(String nomCmd, String userEmail, String cmdState, String imgNamr, String dateLancement) {
        this.nomCmd = nomCmd;
        this.userEmail = userEmail;
        this.cmdState = cmdState;
        this.imgNamr = imgNamr;
        this.dateLancement = dateLancement;
    }

    public String getNomCmd() {
        return nomCmd;
    }

    public void setNomCmd(String nomCmd) {
        this.nomCmd = nomCmd;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getCmdState() {
        return cmdState;
    }

    public void setCmdState(String cmdState) {
        this.cmdState = cmdState;
    }

    public String getImgNamr() {
        return imgNamr;
    }

    public void setImgNamr(String imgNamr) {
        this.imgNamr = imgNamr;
    }

    public String getDateLancement() {
        return dateLancement;
    }

    public void setDateLancement(String dateLancement) {
        this.dateLancement = dateLancement;
    }
}

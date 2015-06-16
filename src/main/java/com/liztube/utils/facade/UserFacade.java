package com.liztube.utils.facade;

import java.sql.Timestamp;

/**
 * Created by maxime on 07/04/2015.
 */
public class UserFacade {
    //region attributes
    private String firstname;
    private String lastname;
    private Timestamp birthdate;
    private String email;
    private Boolean isfemale;
    private String pseudo;
    //endregion


    public String getFirstname() {
        return firstname;
    }

    public UserFacade setFirstname(String firstname) {
        this.firstname = firstname;return this;
    }

    public String getLastname() {
        return lastname;
    }

    public UserFacade setLastname(String lastname) {
        this.lastname = lastname;return this;
    }

    public Timestamp getBirthdate() {
        return birthdate;
    }

    public UserFacade setBirthdate(Timestamp birthdate) {
        this.birthdate = birthdate;return this;
    }

    public String getEmail() {
        return email;
    }

    public UserFacade setEmail(String email) {
        this.email = email;return this;
    }

    public Boolean getIsfemale() {
        return isfemale;
    }

    public UserFacade setIsfemale(Boolean isfemale) {
        this.isfemale = isfemale;return this;
    }

    public String getPseudo() {
        return pseudo;
    }

    public UserFacade setPseudo(String pseudo) {
        this.pseudo = pseudo;return this;
    }
}

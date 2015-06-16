package com.liztube.utils.facade;

import java.sql.Timestamp;

/**
 * Created by laurent on 22/02/15.
 */
public class UserForRegistration {

    //region attributes
    private String firstname;
    private String lastname;
    private String pseudo;
    private String password;
    private Timestamp birthdate;
    private String email;
    private Boolean isfemale;
    //endregion

    //region getter/setter

    public String getFirstname() {
        return firstname;
    }

    public UserForRegistration setFirstname(String firstname) {
        this.firstname = firstname; return this;
    }

    public String getLastname() {
        return lastname;
    }

    public UserForRegistration setLastname(String lastname) {
        this.lastname = lastname; return this;
    }

    public String getPseudo() {
        return pseudo;
    }

    public UserForRegistration setPseudo(String pseudo) {
        this.pseudo = pseudo; return this;
    }

    public String getPassword() {
        return password;
    }

    public UserForRegistration setPassword(String password) {
        this.password = password; return this;
    }

    public Timestamp getBirthdate() {
        return birthdate;
    }

    public UserForRegistration setBirthdate(Timestamp birthdate) {
        this.birthdate = birthdate; return this;
    }

    public String getEmail() {
        return email;
    }

    public UserForRegistration setEmail(String email) {
        this.email = email; return this;
    }

    public Boolean getIsfemale() {
        return isfemale;
    }

    public UserForRegistration setIsfemale(Boolean isfemale) {
        this.isfemale = isfemale; return this;
    }

    //endregion

}

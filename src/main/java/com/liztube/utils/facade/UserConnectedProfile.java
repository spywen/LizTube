package com.liztube.utils.facade;

import java.util.List;

/**
 * Facade with connected user data
 */
public class UserConnectedProfile {

    //region attributs
    private long id;
    private String email;
    private String pseudo;
    private List<String> roles;
    //endregion

    //region constructor
    public UserConnectedProfile(long id, String email, String pseudo, List<String> roles){
        this.id = id;
        this.email = email;
        this.pseudo = pseudo;
        this.roles = roles;
    }
    //endregion

    //region getter/setter
    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    //endregion

}

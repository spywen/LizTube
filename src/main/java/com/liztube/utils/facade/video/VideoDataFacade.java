package com.liztube.utils.facade.video;

import java.sql.Timestamp;

/**
 * Video data to send to the client when searching for example
 */
public class VideoDataFacade {

    //region attributes
    private String title;
    private String description;
    private int views;
    private String key;
    private String ownerPseudo;
    private String ownerEmail;
    private long ownerId;
    private boolean isPublic;
    private boolean isPublicLink;
    private Timestamp creationDate;
    private long duration;
    //endregion

    //region getter/setter

    public String getKey() {
        return key;
    }

    public VideoDataFacade setKey(String key) {
        this.key = key; return this;
    }

    public String getTitle() {
        return title;
    }

    public VideoDataFacade setTitle(String title) {
        this.title = title; return this;
    }

    public String getDescription() {
        return description;
    }

    public VideoDataFacade setDescription(String description) {
        this.description = description; return this;
    }

    public int getViews() {
        return views;
    }

    public VideoDataFacade setViews(int views) {
        this.views = views; return this;
    }

    public String getOwnerPseudo() {
        return ownerPseudo;
    }

    public VideoDataFacade setOwnerPseudo(String ownerPseudo) {
        this.ownerPseudo = ownerPseudo; return this;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public VideoDataFacade setOwnerId(long ownerId) {
        this.ownerId = ownerId; return this;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public VideoDataFacade setPublic(boolean isPublic) {
        this.isPublic = isPublic; return this;
    }

    public boolean isPublicLink() {
        return isPublicLink;
    }

    public VideoDataFacade setPublicLink(boolean isPublicLink) {
        this.isPublicLink = isPublicLink; return this;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public VideoDataFacade setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate; return this;
    }

    public long getDuration() {
        return duration;
    }

    public VideoDataFacade setDuration(long duration) {
        this.duration = duration; return this;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public VideoDataFacade setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
        return this;
    }

    //endregion

}

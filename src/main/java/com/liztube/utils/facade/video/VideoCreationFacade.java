package com.liztube.utils.facade.video;

/**
 * Facade which will be associated to the video uploaded which contains title, description and sharing attributes
 */
public class VideoCreationFacade {

    //region attributes
    private String title;
    private String description;
    private boolean isPublic;
    private boolean isPublicLink;
    //endregion

    //region getter/setter
    public String getTitle() {
        return title;
    }

    public VideoCreationFacade setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public VideoCreationFacade setDescription(String description) {
        this.description = description;
        return this;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public VideoCreationFacade setPublic(boolean isPublic) {
        this.isPublic = isPublic;
        return this;
    }

    public boolean isPublicLink() {
        return isPublicLink;
    }

    public VideoCreationFacade setPublicLink(boolean isPublicLink) {
        this.isPublicLink = isPublicLink;
        return this;
    }
    //endregion

}

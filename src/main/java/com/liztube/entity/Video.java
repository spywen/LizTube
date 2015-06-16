package com.liztube.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liztube.utils.EnumError;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

/**
 * Video class
 */
@Entity
@Table(name = "VIDEO")
public class Video {

    //region attributes
    private String key;
    private String title;
    private String description;
    private Timestamp creationdate;
    private UserLiztube owner;
    private Boolean ispublic;
    private Boolean ispubliclink;
    private long videoRankAsMostViewed;
    private long videoRankAsMostRecent;
    private long videoRankAsMostShared;
    private List<View> views;
    private long duration;//milliseconds

    //Transcient values
    private long viewsCount;
    private long privateViewsCount;
    //endregion

    //region getter/setter
    @Id
    @Column(name = "KEYID")
    public String getKey() {
        return key;
    }

    public Video setKey(String key) {
        this.key = key; return this;
    }

    @Lob
    @Column(name = "TITLE", nullable = false, insertable = true, updatable = true, length = 300)
    @Size(min = 1, max = 300, message = EnumError.VIDEO_TITLE_SIZE)
    public String getTitle() {
        return title;
    }

    public Video setTitle(String title) {
        this.title = title; return this;
    }

    @Lob
    @Column(name = "DESCRIPTION", nullable = true, insertable = true, updatable = true, length = 1000)
    @Size(min = 0, max = 1000, message = EnumError.VIDEO_DESCRIPTION_SIZE)
    public String getDescription() {
        return description;
    }

    public Video setDescription(String description) {
        this.description = description; return this;
    }

    @Basic
    @Column(name = "CREATIONDATE", nullable = false, insertable = true, updatable = false)
    public Timestamp getCreationdate() {
        return creationdate;
    }

    public Video setCreationdate(Timestamp creationdate) {
        this.creationdate = creationdate; return this;
    }

    @ManyToOne
    @JoinColumn(name="USER", nullable=false, referencedColumnName = "ID")
    @NotNull
    public UserLiztube getOwner() {
        return owner;
    }

    public Video setOwner(UserLiztube owner) {
        this.owner = owner; return this;
    }

    @Basic
    @Column(name = "ISPUBLIC", nullable = false, insertable = true, updatable = true)
    public Boolean getIspublic() {
        return ispublic;
    }

    public Video setIspublic(Boolean ispublic) {
        this.ispublic = ispublic; return this;
    }

    @Basic
    @Column(name = "ISPUBLICLINK", nullable = false, insertable = true, updatable = true)
    public Boolean getIspubliclink() {
        return ispubliclink;
    }

    public Video setIspubliclink(Boolean ispubliclink) {
        this.ispubliclink = ispubliclink; return this;
    }

    @Basic
    @Column(name = "VIDEORANKASMOSTVIEWED", nullable = false, insertable = true, updatable = true)
    public long getVideoRankAsMostViewed() {
        return videoRankAsMostViewed;
    }

    public Video setVideoRankAsMostViewed(long videoRankAsMostViewed) {
        this.videoRankAsMostViewed = videoRankAsMostViewed; return this;
    }

    @Basic
    @Column(name = "VIDEORANKASMOSTRECENT", nullable = false, insertable = true, updatable = true)
    public long getVideoRankAsMostRecent() {
        return videoRankAsMostRecent;
    }

    public Video setVideoRankAsMostRecent(long videoRankAsMostRecent) {
        this.videoRankAsMostRecent = videoRankAsMostRecent; return this;
    }

    @Basic
    @Column(name = "VIDEORANKASMOSTSHARED", nullable = false, insertable = true, updatable = true)
    public long getVideoRankAsMostShared() {
        return videoRankAsMostShared;
    }

    public Video setVideoRankAsMostShared(long videoRankAsMostShared) {
        this.videoRankAsMostShared = videoRankAsMostShared; return this;
    }

    @Basic
    @Column(name = "DURATION", nullable = false, insertable = true, updatable = true)
    public long getDuration() {
        return duration;
    }

    public Video setDuration(long duration) {
        this.duration = duration; return this;
    }

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "video", cascade = CascadeType.ALL)
    public List<View> getViews() {
        return views;
    }

    public void setViews(List<View> views) {
        this.views = views;
    }

    @Transient
    public long getViewsCount() {
        return getViews().size();
    }

    public void setViewsCount(long viewsCount) {}

    @Transient
    public long getPrivateViewsCount() {
        long privateViewsCount = 0;
        for(View v : getViews()){
            if(v.isAsShared()){
                privateViewsCount++;
            }
        }
        return privateViewsCount;
    }

    public void setPrivateViewsCount(long privateViewsCount) {}

    //endregion

    //region override methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Video video = (Video) o;

        if (description != null ? !description.equals(video.description) : video.description != null) return false;
        if (ispublic != null ? !ispublic.equals(video.ispublic) : video.ispublic != null) return false;
        if (ispubliclink != null ? !ispubliclink.equals(video.ispubliclink) : video.ispubliclink != null) return false;
        if (key != null ? !key.equals(video.key) : video.key != null) return false;
        if (owner != null ? !owner.equals(video.owner) : video.owner != null) return false;
        if (title != null ? !title.equals(video.title) : video.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (ispublic != null ? ispublic.hashCode() : 0);
        result = 31 * result + (ispubliclink != null ? ispubliclink.hashCode() : 0);
        return result;
    }
    //endregion

}

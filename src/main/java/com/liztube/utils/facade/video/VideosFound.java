package com.liztube.utils.facade.video;

import com.liztube.entity.Video;

import java.util.List;

/**
 * Videos found with total count of videos
 */
public class VideosFound {

    private List<Video> videos;
    private Long totalCount;

    public VideosFound(List<Video> videos, Long totalCount){
        this.videos = videos;
        this.totalCount = totalCount;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}

package com.liztube.utils.facade.video;

import java.util.List;

/**
 * Facade to send videos found when searching for it
 */
public class GetVideosFacade {

    //region attributes
    /**
     * List of videos found
     */
    private List<VideoDataFacade> videos;
    /**
     * Current page number
     */
    private long currentPage;
    /**
     * Total count of videos found
     */
    private long videosTotalCount;
    /**
     * Total count of pages according to the pagination attribute value
     */
    private long totalPage;
    //endregion

    //region getter/setter

    public List<VideoDataFacade> getVideos() {
        return videos;
    }

    public GetVideosFacade setVideos(List<VideoDataFacade> videos) {
        this.videos = videos; return this;
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public GetVideosFacade setCurrentPage(long currentPage) {
        this.currentPage = currentPage; return this;
    }

    public long getVideosTotalCount() {
        return videosTotalCount;
    }

    public GetVideosFacade setVideosTotalCount(long videosTotalCount) {
        this.videosTotalCount = videosTotalCount; return this;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public GetVideosFacade setTotalPage(long totalPage) {
        this.totalPage = totalPage; return this;
    }

    //endregion
}

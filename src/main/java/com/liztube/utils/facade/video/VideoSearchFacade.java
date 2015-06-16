package com.liztube.utils.facade.video;

import com.liztube.utils.EnumVideoOrderBy;

/**
 * Facade which contains all the options to search for videos
 */
public class VideoSearchFacade {

    //region attributes
    /**
     * Keyword from search bar
     */
    private String keyword;
    /**
     * Order by
     */
    private EnumVideoOrderBy orderBy;
    /**
     * Page asked for
     */
    private int page;
    /**
     * Number of videos by page asked for
     * Default value set in the application properties if = 0
     */
    private int pagination;
    /**
     * Search video for a specific user
     */
    private int userId;
    //endregion

    //region getter/setter

    public String getKeyword() {
        return keyword;
    }

    public VideoSearchFacade setKeyword(String keyword) {
        this.keyword = keyword; return this;
    }

    public EnumVideoOrderBy getOrderBy() {
        return orderBy;
    }

    public VideoSearchFacade setOrderBy(EnumVideoOrderBy orderBy) {
        this.orderBy = orderBy; return this;
    }

    public int getPage() {
        return page;
    }

    public VideoSearchFacade setPage(int page) {
        this.page = page; return this;
    }

    public int getPagination() {
        return pagination;
    }

    public VideoSearchFacade setPagination(int pagination) {
        this.pagination = pagination; return this;
    }

    public int getUserId() {
        return userId;
    }

    public VideoSearchFacade setUserId(int userId) {
        this.userId = userId; return this;
    }

    //endregion
}

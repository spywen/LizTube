package com.liztube.utils.facade.video;

import com.liztube.utils.EnumVideoOrderBy;

/**
 * Video search facade for repository
 */
public class VideoSearchFacadeForRepository {

    //region attributes
    /**
     * true <=> get only videos public, false <=> get all videos
     */
    private boolean onlyPublic;
    /**
     * search for videos which belong to this user
     */
    private int userId;
    /**
     * search for videos which match to these keywords (take in consideration title, description, and owner pseudo)
     */
    private String keywords;
    /**
     * ordering result by this field
     */
    private EnumVideoOrderBy orderBy;
    /**
     * number of video by page (should not be 0)
     */
    private int pagination;
    /**
     * page asked for ([0;infinite])
     */
    private int page;
    //endregion

    //region getter/setter

    public boolean isOnlyPublic() {
        return onlyPublic;
    }

    public VideoSearchFacadeForRepository setOnlyPublic(boolean onlyPublic) {
        this.onlyPublic = onlyPublic; return this;
    }

    public int getUserId() {
        return userId;
    }

    public VideoSearchFacadeForRepository setUserId(int userId) {
        this.userId = userId; return this;
    }

    public String getKeywords() {
        return keywords;
    }

    public VideoSearchFacadeForRepository setKeywords(String keywords) {
        this.keywords = keywords; return this;
    }

    public EnumVideoOrderBy getOrderBy() {
        return orderBy;
    }

    public VideoSearchFacadeForRepository setOrderBy(EnumVideoOrderBy orderBy) {
        this.orderBy = orderBy; return this;
    }

    public int getPagination() {
        return pagination;
    }

    public VideoSearchFacadeForRepository setPagination(int pagination) {
        this.pagination = pagination; return this;
    }

    public int getPage() {
        return page;
    }

    public VideoSearchFacadeForRepository setPage(int page) {
        this.page = page; return this;
    }

    //endregion
}

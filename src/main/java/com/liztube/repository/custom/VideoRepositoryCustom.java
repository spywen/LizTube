package com.liztube.repository.custom;

import com.liztube.utils.facade.video.VideoSearchFacadeForRepository;
import com.liztube.utils.facade.video.VideosFound;

/**
 * Created by laurent on 05/04/15.
 */
public interface VideoRepositoryCustom {
    /**
     * Get videos according to a set of params
     * @param vFacade
     * @return list of videos found for the page asked for and total count of videos found
     */
    VideosFound findVideosByCriteria(VideoSearchFacadeForRepository vFacade);
}

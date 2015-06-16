package com.liztube.utils.comparators;

import com.liztube.entity.Video;

import java.util.Comparator;

/**
 * Compare shared videos (views as shared (not in public mode))
 */
public class VideoSharedCountComparator implements Comparator<Video> {
    @Override
    public int compare(Video video1, Video video2) {
        return (video1.getPrivateViewsCount() < video2.getPrivateViewsCount() ) ? -1: (video1.getPrivateViewsCount() > video2.getPrivateViewsCount() ) ? 1 : 0;
    }
}

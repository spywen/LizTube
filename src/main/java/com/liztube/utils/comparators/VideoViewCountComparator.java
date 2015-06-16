package com.liztube.utils.comparators;

import com.liztube.entity.Video;

import java.util.Comparator;

/**
 * Compare views between videos
 */
public class VideoViewCountComparator implements Comparator<Video> {
    @Override
    public int compare(Video video1, Video video2) {
        return (video1.getViewsCount() < video2.getViewsCount() ) ? -1: (video1.getViewsCount() > video2.getViewsCount() ) ? 1 : 0;
    }
}

package com.liztube.utils.comparators;

import com.liztube.entity.Video;

import java.util.Comparator;

/**
 * Comprae creation date between videos
 */
public class VideoDateComparator implements Comparator<Video> {
    @Override
    public int compare(Video video1, Video video2) {
        return video1.getCreationdate().compareTo(video2.getCreationdate());
    }
}

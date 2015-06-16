package com.liztube.business;

import com.liztube.entity.Video;
import com.liztube.repository.VideoRepository;
import com.liztube.utils.comparators.VideoDateComparator;
import com.liztube.utils.comparators.VideoSharedCountComparator;
import com.liztube.utils.comparators.VideoViewCountComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Video rank business (call by dedicated scheduler)
 */
@Component
public class VideoRankBusiness {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    VideoRepository videoRepository;

    /**
     * Analyse videos and put the new rank to all videos
     * Rank  : 1 <=> Best, first one
     * Rank : n <=> Bad, last one
     * with n : total videos count
     */
    public void runVideoRankProcedure(){
        logger.info("--- VIDEO RANK PROCEDURE - START ---");
        logger.info("Get all videos...");
        List<Video> videos = videoRepository.findAll();
        long totalVideo = videos.size();
        logger.info("Total videos count = " + totalVideo + ".");

        //Most recent rank
        logger.info("@ Set MOST RECENT RANK... @");
        Collections.sort(videos, new VideoDateComparator());//Order from the oldest one to the newest one
        long mostRecentRank = totalVideo;
        for(Video video : videos){//Decrement mostRecentRank count each time and set the Most recent field
            video.setVideoRankAsMostRecent(mostRecentRank);
            mostRecentRank--;
        }

        //Most viewed rank
        logger.info("@ Set MOST VIEWED RANK... @");
        Collections.sort(videos, new VideoViewCountComparator());
        long mostViewedRank = totalVideo;
        for(Video video : videos){//Decrement the mostViewedRank count each time and set the Most viewed field
            video.setVideoRankAsMostViewed(mostViewedRank);
            mostViewedRank--;
        }

        //Most shared rank
        logger.info("@ Set MOST SHARED RANK... @");
        Collections.sort(videos, new VideoSharedCountComparator());
        long mostSharedRank = totalVideo;
        for(Video video : videos){//Decrement the mostSharedRank count each time and set the Most viewed field
            video.setVideoRankAsMostShared(mostSharedRank);
            mostSharedRank--;
        }

        //Save videos
        logger.info("Save videos...");
        for(Video video : videos){
            videoRepository.saveAndFlush(video);
        }
        /* With JDK 8
        videos.forEach(videoRepository::saveAndFlush);
         */
        logger.info("--- VIDEO RANK PROCEDURE - END ---");
    }
}

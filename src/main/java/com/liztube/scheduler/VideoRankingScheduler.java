package com.liztube.scheduler;

import com.liztube.business.VideoRankBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Scheduler (CRON) to analyse all videos and assign to it a rank for each type of filter
 */
@Component
public class VideoRankingScheduler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    VideoRankBusiness videoRankBusiness;

    /**
     * Cron to execute each day at 4 O'Clock
     * ([etoile]/10 * * * * *) to test each 10 seconds
     */
    @Scheduled(cron="0 0 4 * * *")//Each day at 4 O'Clock
    public void runVideoRankProcedure() {
        logger.info("/// CRON START");
        try{
            videoRankBusiness.runVideoRankProcedure();
        }catch (Exception e){
            logger.error("!!! An unexpected error occured during the rank procedure. Please check the db integrity. !!!", e);
        }
        logger.info("/// CRON END");
    }
}

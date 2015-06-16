package com.liztube.business;

import com.liztube.entity.UserLiztube;
import com.liztube.entity.Video;
import com.liztube.exception.UserNotFoundException;
import com.liztube.repository.VideoRepository;
import com.liztube.utils.EnumVideoOrderBy;
import com.liztube.utils.facade.video.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Business to search for videos
 */
@Component
public class SearchForVideosBusiness {

    @Autowired
    AuthBusiness authBusiness;
    @Autowired
    VideoRepository videoRepository;

    @Autowired
    Environment environment;

    /**
     * Get videos according to a set of options
     * page attribute : [1;infinite]
     * @return
     */
    public GetVideosFacade GetVideos(VideoSearchFacade videoSearchFacade) {

        VideoSearchFacadeForRepository vFacade = convertVideoSearchFacadeAsVideoSearchFacadeForRepository(videoSearchFacade);

        VideosFound response;
        if(videoSearchFacade.getUserId() != 0){
            UserLiztube user = null;
            try {
                user = authBusiness.getConnectedUser(false);
            } catch (UserNotFoundException e) {}
            if(user != null && user.getId() == videoSearchFacade.getUserId()){
                response = videoRepository.findVideosByCriteria(vFacade.setOnlyPublic(false));
            }else{
                response = videoRepository.findVideosByCriteria(vFacade.setOnlyPublic(true));
            }
        }else{
            response = videoRepository.findVideosByCriteria(vFacade.setOnlyPublic(true));
        }

        //Get pagination data
        long totalItem = response.getTotalCount();
        double pages = totalItem / vFacade.getPagination();
        double pageSup = totalItem % vFacade.getPagination();
        int totalPage = (int)Math.round(pages) + ((pageSup != 0) ? 1 : 0);

        //Get video list as facade objects
        List<VideoDataFacade> videosFound = new ArrayList<>();
        for(Video v : response.getVideos()){
            videosFound.add(new VideoDataFacade()
                            .setKey(v.getKey())
                            .setTitle(v.getTitle())
                            .setDescription(v.getDescription())
                            .setViews(v.getViews().size())
                            .setOwnerId(v.getOwner().getId())
                            .setOwnerPseudo(v.getOwner().getPseudo())
                            .setPublic(v.getIspublic())
                            .setPublicLink(v.getIspubliclink())
                            .setCreationDate(v.getCreationdate())
                            .setDuration(v.getDuration())
            );
        }
        /* With JDK 8
        response.getKey().forEach((v) -> videosFound.add(new VideoDataFacade()
                        .setKey(v.getKey())
                        .setTitle(v.getTitle())
                        .setDescription(v.getDescription())
                        .setViews(v.getViews().size())
                        .setOwnerId(v.getOwner().getId())
                        .setOwnerPseudo(v.getOwner().getPseudo())
                        .setPublic(v.getIspublic())
                        .setPublicLink(v.getIspubliclink())
                        .setCreationDate(v.getCreationdate())
                        .setDuration(v.getDuration())
        ));*/

        //Build response
        return new GetVideosFacade()
                .setVideos(videosFound)
                .setVideosTotalCount(totalItem)
                .setTotalPage(totalPage)
                .setCurrentPage(videoSearchFacade.getPage());
    }

    /**
     * Analyse criteria, prepare them and add it to a adapted object for repository
     * @param videoSearchFacade
     * @return
     */
    private VideoSearchFacadeForRepository convertVideoSearchFacadeAsVideoSearchFacadeForRepository(VideoSearchFacade videoSearchFacade) {
        //Get number of videos to return (get value send or get default value)
        int pagination = (videoSearchFacade.getPagination() == 0) ? Integer.parseInt(environment.getProperty("video.default.pagination")) : videoSearchFacade.getPagination();
        //Get page asked for (we substract 1 because page go from 1 to infinite and pagination engine need to start to 0)
        int page = videoSearchFacade.getPage() == 0 ? 0 : videoSearchFacade.getPage() - 1;
        //Get asked for user
        int userId = videoSearchFacade.getUserId();
        //Get asked for keywords
        String unEncodedKeywords = com.liztube.utils.StringUtils.UrlDecoder(videoSearchFacade.getKeyword() != null ? videoSearchFacade.getKeyword().trim() : "");
        String keywords = !StringUtils.isEmpty(unEncodedKeywords) ? unEncodedKeywords : null;
        //Get order by attribute
        EnumVideoOrderBy enumVideoOrderBy = videoSearchFacade.getOrderBy() == null ? EnumVideoOrderBy.DEFAULT : videoSearchFacade.getOrderBy();

        return new VideoSearchFacadeForRepository()
                .setPagination(pagination)
                .setPage(page)
                .setUserId(userId)
                .setKeywords(keywords)
                .setOrderBy(enumVideoOrderBy);

    }
}

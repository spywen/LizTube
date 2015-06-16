package com.liztube.repository.impl;

import com.liztube.entity.UserLiztube;
import com.liztube.entity.UserLiztube_;
import com.liztube.entity.Video;
import com.liztube.entity.Video_;
import com.liztube.repository.custom.VideoRepositoryCustom;
import com.liztube.utils.EnumVideoOrderBy;
import com.liztube.utils.facade.video.VideoSearchFacadeForRepository;
import com.liztube.utils.facade.video.VideosFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Video repository implementation
 */
public class VideoRepositoryImpl implements VideoRepositoryCustom {

    @PersistenceContext
    EntityManager em;

    @Autowired
    Environment environment;

    /**
     * See interface
     * @param vFacade
     * @return
     */
    @Override
    public VideosFound findVideosByCriteria(VideoSearchFacadeForRepository vFacade) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Video> query = cb.createQuery(Video.class);
        Root<Video> video = query.from(Video.class);

        final List<Predicate> andPredicates = new ArrayList<>();

        //Build predicate
        onlyPublic(cb, video, vFacade.isOnlyPublic(), andPredicates);
        forUser(cb, video, vFacade.getUserId(), andPredicates);
        searchByKeywords(cb, video, vFacade.getKeywords(), andPredicates);

        query.where(cb.and(andPredicates.toArray(new Predicate[andPredicates.size()])));

        //Order by predicate
        orderBy(cb, query, video, vFacade.getOrderBy());

        //Return videos found and total count
        return new VideosFound(
                em.createQuery(query).setFirstResult(vFacade.getPage() * vFacade.getPagination()).setMaxResults(vFacade.getPagination()).getResultList(),
                getTotalVideosFoundByCriteria(vFacade.isOnlyPublic(), vFacade.getUserId(), vFacade.getKeywords())
        );
    }

    /**
     * Get total of videos found
     * @param onlyPublic
     * @param userId
     * @param keywords
     * @return
     */
    private long getTotalVideosFoundByCriteria(boolean onlyPublic, int userId, String keywords){
        CriteriaBuilder cbForCount = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cbForCount.createQuery(Long.class);
        Root<Video> videoForCount = countQuery.from(Video.class);
        countQuery.select(cbForCount.count(videoForCount));

        final List<Predicate> andPredicates = new ArrayList<>();

        //Build predicate
        onlyPublic(cbForCount, videoForCount, onlyPublic, andPredicates);
        forUser(cbForCount, videoForCount, userId, andPredicates);
        searchByKeywords(cbForCount, videoForCount, keywords, andPredicates);

        countQuery.where(cbForCount.and(andPredicates.toArray(new Predicate[andPredicates.size()])));

        return em.createQuery(countQuery).getSingleResult();
    }

    /**
     * Order by EnumVideoOrderBy value
     * @param cb
     * @param query
     * @param video
     * @param enumVideoOrderBy
     * @return
     */
    private CriteriaQuery<Video> orderBy(CriteriaBuilder cb, CriteriaQuery<Video> query, Root<Video> video, EnumVideoOrderBy enumVideoOrderBy){
        if(enumVideoOrderBy.equals(EnumVideoOrderBy.MOSTRECENT)) {
            return query.orderBy(cb.asc(video.get(Video_.videoRankAsMostRecent)));
        }else if(enumVideoOrderBy.equals(EnumVideoOrderBy.MOSTVIEWED)) {
            return query.orderBy(cb.asc(video.get(Video_.videoRankAsMostViewed)));
        }else if (enumVideoOrderBy.equals(EnumVideoOrderBy.MOSTSHARED)){
            return query.orderBy(cb.asc(video.get(Video_.videoRankAsMostShared)));
        }else if(enumVideoOrderBy.equals(EnumVideoOrderBy.HOMESUGGESTION)){
            int mostRecentRate = Integer.parseInt(environment.getProperty("video.home.suggestion.most.recent.rate"));
            int mostViewedRate = Integer.parseInt(environment.getProperty("video.home.suggestion.most.viewed.rate"));
            int mostSharedRate = Integer.parseInt(environment.getProperty("video.home.suggestion.most.shared.rate"));
            return query.orderBy(
                    cb.asc(
                            cb.sum(
                                    cb.sum(
                                            cb.prod(mostViewedRate, video.get(Video_.videoRankAsMostViewed)),
                                            cb.prod(mostSharedRate, video.get(Video_.videoRankAsMostShared))
                                    ),
                                    cb.prod(mostRecentRate, video.get(Video_.videoRankAsMostRecent))
                            )
                    )
            );
        }
        return query.orderBy(cb.desc(video.get(Video_.creationdate)));
    }

    /**
     * Keywords predicate
     * @param cb
     * @param video
     * @param keywords
     * @param predicates
     * @return
     */
    private List<Predicate> searchByKeywords(CriteriaBuilder cb, Root<Video> video, String keywords, List<Predicate> predicates){
        Join<Video, UserLiztube> ownerJoin = video.join(Video_.owner);
        if(!StringUtils.isEmpty(keywords)){
            Predicate titleKeywordsFilter = cb.like(cb.lower(video.get(Video_.title)), "%"+keywords.toLowerCase()+"%");
            Predicate descriptionKeywordsFilter = cb.like(cb.lower(video.get(Video_.description)), "%"+keywords.toLowerCase()+"%");
            Predicate ownerPseudoKeywordsFilter = cb.like(cb.lower(ownerJoin.get(UserLiztube_.pseudo)), "%"+keywords.toLowerCase()+"%");
            Predicate keywordsFilter = cb.or(titleKeywordsFilter, descriptionKeywordsFilter, ownerPseudoKeywordsFilter);
            predicates.add(keywordsFilter);
        }
        return predicates;
    }

    /**
     * Only public video predicate
     * @param cb
     * @param video
     * @param onlyPublic
     * @param predicates
     * @return
     */
    private List<Predicate> onlyPublic(CriteriaBuilder cb, Root<Video> video, boolean onlyPublic, List<Predicate> predicates){
        if(onlyPublic){
            predicates.add(cb.equal(video.get(Video_.ispublic),true));
        }
        return predicates;
    }

    /**
     * Get user's videos predicate
     * @param cb
     * @param video
     * @param userId
     * @param predicates
     * @return
     */
    private List<Predicate> forUser(CriteriaBuilder cb, Root<Video> video, int userId, List<Predicate> predicates){
        if(userId != 0){
            predicates.add(cb.equal(video.get(Video_.owner), userId));
        }
        return predicates;
    }
}

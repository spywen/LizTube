package com.liztube.repository;

import com.liztube.entity.Video;
import com.liztube.repository.custom.VideoRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data access layer to videos
 */
public interface VideoRepository extends JpaRepository<Video, Long>, VideoRepositoryCustom {
    Video findByKey(String key);
}

package com.liztube.repository;

import com.liztube.entity.View;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * View repository
 */
public interface ViewRepository extends JpaRepository<View, Long> {
    List<View> findByUserIdAndVideoKey(long userId, String key);
}

package com.liztube.repository.custom;

import com.liztube.entity.UserLiztube;

/**
 * Repository interface for Liztube user to be able to create complex queries usable by the default JpaRepository
 */
public interface UserLiztubeRepositoryCustom {
    UserLiztube findByEmailOrPseudo(String login);
}

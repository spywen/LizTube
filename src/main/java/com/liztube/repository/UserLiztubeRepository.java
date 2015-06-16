package com.liztube.repository;

import com.liztube.entity.UserLiztube;
import com.liztube.repository.custom.UserLiztubeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * User repository : data access layer to the users
 *  QueryDslPredicateExecutor<UserLiztube>
 */
public interface UserLiztubeRepository extends JpaRepository<UserLiztube, Long>, UserLiztubeRepositoryCustom{
    /**
     * Find a user according to his pseudo
     * @param pseudo
     * @return
     */
    public UserLiztube findByPseudo(String pseudo);

    /**
     * Return the count of user(s) with the same email
     * @param email
     * @return
     */
    long countByEmail(String email);

    /**
     * Return the count of user(s) with the same pseudo
     * @param pseudo
     * @return
     */
    long countByPseudo(String pseudo);
}
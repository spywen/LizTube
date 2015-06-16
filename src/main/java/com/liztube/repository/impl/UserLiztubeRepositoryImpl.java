package com.liztube.repository.impl;

import com.liztube.entity.UserLiztube;
import com.liztube.entity.UserLiztube_;
import com.liztube.repository.custom.UserLiztubeRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Repository implementation of complex queries for Liztube user.
 */
public class UserLiztubeRepositoryImpl implements UserLiztubeRepositoryCustom{

    @PersistenceContext
    EntityManager em;


    @Override
    public UserLiztube findByEmailOrPseudo(String login) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserLiztube> query = cb.createQuery(UserLiztube.class);
        Root<UserLiztube> user = query.from(UserLiztube.class);

        Predicate emailFilter = cb.equal(user.get(UserLiztube_.email),login);
        Predicate pseudoFilter = cb.equal(user.get(UserLiztube_.pseudo), login);
        Predicate loginFilter = cb.or(emailFilter, pseudoFilter);

        query.where(loginFilter);
        return em.createQuery(query).getSingleResult();
    }
}

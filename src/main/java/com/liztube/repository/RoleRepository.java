package com.liztube.repository;

import com.liztube.entity.Role;
import com.liztube.entity.UserLiztube;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * Role repository : data access layer to the roles
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Find a role according to his name
     * @param name
     * @return
     */
    Role findByName(String name);
}

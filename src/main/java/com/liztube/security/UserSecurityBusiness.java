package com.liztube.security;

import com.liztube.entity.UserLiztube;
import com.liztube.repository.UserLiztubeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class used by Spring to identified a user according to its login informations
 */
@Service("userDetailsService")
@Transactional(readOnly = true)
public class UserSecurityBusiness implements UserDetailsService{
    @Autowired
    UserLiztubeRepository userLiztubeRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserLiztube domainUser = userLiztubeRepository.findByEmailOrPseudo(username);

            return new User(
                    domainUser.getPseudo(),
                    domainUser.getPassword(),
                    domainUser.getIsactive(),
                    true,//domainUser.isAccountNonExpired(),
                    true,//domainUser.isCredentialsNonExpired(),
                    true,//domainUser.isAccountNonLocked(),
                    domainUser.getRolesAutorithies()
            );

        } catch (Exception e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}

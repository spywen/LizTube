package com.liztube.service;

import com.liztube.business.AuthBusiness;
import com.liztube.exception.ServiceException;
import com.liztube.exception.SigninException;
import com.liztube.exception.UserNotFoundException;
import com.liztube.exception.exceptionType.PublicException;
import com.liztube.utils.facade.TestExistFacade;
import com.liztube.utils.facade.UserConnectedProfile;
import com.liztube.utils.facade.UserForRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Service which provide all the method concerning the authentication of an user (login, signin, get user profile...)
 */
@RestController
@RequestMapping("/api/auth")
public class AuthService {
    @Autowired
    AuthBusiness authBusiness;

    /**
     * Get current user profile
     * @return
     * @throws UserNotFoundException
     */
    @RequestMapping(value = "/currentProfil",method = RequestMethod.GET)
    public UserConnectedProfile getUserProfil() throws ServiceException {
        try{
            return authBusiness.getUserConnectedProfile(true);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("Get connected user profile");
        }
    }

    /**
     * Register to the liztube service as a user
     * @param userForRegistration
     * @return
     * @throws SigninException
     */
    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public long signIn(@RequestBody UserForRegistration userForRegistration) throws SigninException, ServiceException {
        try{
            return authBusiness.signIn(userForRegistration).getId();
        }catch (PublicException e){
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("Sign in");
        }
    }

    /**
     * Determine if a pseudo is already used
     * @param testExistFacade
     * @return
     */
    @RequestMapping(value = "/pseudo", method = RequestMethod.POST)
    public Boolean existPseudo(@RequestBody TestExistFacade testExistFacade) throws ServiceException {
        try{
            return authBusiness.existPseudo(testExistFacade);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("Pseudo exists");
        }
    }

    /**
     * Determine if an email is already used
     * @param testExistFacade
     * @return
     */
    @RequestMapping(value = "/email", method = RequestMethod.POST)
    public Boolean existEmail(@RequestBody TestExistFacade testExistFacade) throws ServiceException {
        try{
            return authBusiness.existEmail(testExistFacade);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("Email exists");
        }
    }


}

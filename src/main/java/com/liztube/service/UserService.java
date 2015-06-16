package com.liztube.service;

import com.liztube.business.UserBusiness;
import com.liztube.entity.UserLiztube;
import com.liztube.exception.ServiceException;
import com.liztube.exception.UserException;
import com.liztube.exception.UserNotFoundException;
import com.liztube.exception.exceptionType.PublicException;
import com.liztube.utils.facade.TestExistFacade;
import com.liztube.utils.facade.UserAccountDeletionFacade;
import com.liztube.utils.facade.UserFacade;
import com.liztube.utils.facade.UserPasswordFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Service which provide all the method concerning the user  (infos user, modification...)
 */
@RestController
@RequestMapping("/api/user")
public class UserService {

    @Autowired
    UserBusiness userBusiness;

    /**
     * Get current user profile infos
     * @return
     * @throws com.liztube.exception.UserNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET)
    public UserFacade getUserInfoProfile() throws UserNotFoundException, ServiceException {
        try{
            return userBusiness.getUserInfo();
        }catch (PublicException e){
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("Get user profile");
        }
    }

    /**
     * Update user data
     * @return
     * @throws UserNotFoundException
     */
    @RequestMapping(method = RequestMethod.PUT)
    public UserLiztube updateUserInfo(@RequestBody UserFacade userInfo) throws UserNotFoundException, UserException, ServiceException {
        try{
            return userBusiness.updateUserInfo(userInfo);
        }catch (PublicException e){
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("Update user");
        }
    }

    /**
     * Update password
     * @param userPasswordFacade
     * @return
     * @throws UserNotFoundException
     * @throws UserException
     */
    @RequestMapping(value = "/password", method = RequestMethod.PATCH)
    public boolean changeUserPassword(@RequestBody UserPasswordFacade userPasswordFacade) throws UserNotFoundException, UserException, ServiceException {
        try{
            return userBusiness.changeUserPassword(userPasswordFacade);
        }catch (PublicException e){
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("Update user password");
        }
    }

    /**
     * Delete user account
     * @param userAccountDeletionFacade
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void deleteUserAccount(@RequestBody UserAccountDeletionFacade userAccountDeletionFacade) throws UserNotFoundException, UserException, ServiceException {
        try{
            userBusiness.delete(userAccountDeletionFacade);
        }catch (PublicException e){
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("Delete user account");
        }
    }
}

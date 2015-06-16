package com.liztube.business;

import com.liztube.entity.UserLiztube;
import com.liztube.exception.UserException;
import com.liztube.exception.UserNotFoundException;
import com.liztube.repository.UserLiztubeRepository;
import com.liztube.utils.EnumError;
import com.liztube.utils.Regex;
import com.liztube.utils.facade.TestExistFacade;
import com.liztube.utils.facade.UserAccountDeletionFacade;
import com.liztube.utils.facade.UserFacade;
import com.liztube.utils.facade.UserPasswordFacade;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Component;


import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Class which manage all the method concerning the  user
 */
@Component
public class UserBusiness {

    @Autowired
    public UserLiztubeRepository userLiztubeRepository;

    @Autowired
    public AuthBusiness authBusiness;

    private ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);

    /**
     * Get user information
     * @return
     * @throws UserNotFoundException
     */
    public UserFacade getUserInfo() throws UserNotFoundException {
        UserLiztube userLiztube = authBusiness.getConnectedUser(true);

        UserFacade userInfo = new UserFacade()
                .setEmail(userLiztube.getEmail())
                .setFirstname(userLiztube.getFirstname())
                .setLastname(userLiztube.getLastname())
                .setBirthdate(userLiztube.getBirthdate())
                .setPseudo(userLiztube.getPseudo())
                .setIsfemale(userLiztube.getIsfemale());

        return userInfo;
    }

    /**
     * Update user information
     * @param userInfo
     * @return
     * @throws UserNotFoundException
     * @throws UserException
     */
    public UserLiztube updateUserInfo(UserFacade userInfo) throws UserNotFoundException, UserException {
        UserLiztube userLiztube = authBusiness.getConnectedUser(true);

        //Update user persistent
        userLiztube
                .setFirstname(userInfo.getFirstname())
                .setLastname(userInfo.getLastname())
                .setBirthdate(userInfo.getBirthdate())
                .setIsfemale(userInfo.getIsfemale())
                .setModificationdate(new Timestamp(new DateTime().getMillis()));


        //Verify email if modify
        if (!userInfo.getEmail().equals(userLiztube.getEmail())){
            if(authBusiness.existEmail(new TestExistFacade().setValue(userInfo.getEmail()))){
                throw new UserException("Update user - Email already used", Arrays.asList(EnumError.USER_EMAIL_OR_PSEUDO_ALREADY_USED));
            }
            userLiztube.setEmail(userInfo.getEmail());
        }
        //Verify pseudo
        if (!userInfo.getPseudo().equals(userLiztube.getPseudo())){
            if(authBusiness.existPseudo(new TestExistFacade().setValue(userInfo.getPseudo()))){
                throw new UserException("Update user - Pseudo already used", Arrays.asList(EnumError.USER_EMAIL_OR_PSEUDO_ALREADY_USED));
            }
            userLiztube.setPseudo(userInfo.getPseudo());
        }

        //Entity validations Validations
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<UserLiztube>> constraintViolations = validator.validate(userLiztube);
        if(constraintViolations.size() > 0){
            List<String> errorMessages = new ArrayList<>();
            for(ConstraintViolation<UserLiztube> constraintViolation : constraintViolations){
                errorMessages.add(constraintViolation.getMessage());
            }
            throw new UserException("Update user - Entity format not valid", errorMessages);
        }

        userLiztubeRepository.saveAndFlush(userLiztube);

        return userLiztube;
    }

    /**
     * Change password user
     * @param userPassword
     * @return
     * @throws UserNotFoundException
     * @throws UserException
     */
    public boolean changeUserPassword(UserPasswordFacade userPassword) throws UserNotFoundException, UserException{
        UserLiztube userLiztube = authBusiness.getConnectedUser(true);

        //check old password
        if (userLiztube.getPassword().equals(encoder.encodePassword(userPassword.getOldPassword(), null))){
            //Password well formatted
            if(!userPassword.getNewPassword().matches(Regex.PASSWORD_REGEX)){
                throw new UserException("Change password - Not valid format for password", EnumError.USER_PASSWORD_FORMAT);
            }
            userLiztube.setPassword(encoder.encodePassword(userPassword.getNewPassword(), null));
        }else{
            throw new UserException("Change password - Old password not corresponding ", EnumError.USER_OLD_PASSWORD_INVALID);
        }

        userLiztubeRepository.saveAndFlush(userLiztube);

        return true;
    }

    /**
     * Delete user account method
     * @param userAccountDeletionFacade
     */
    public void delete(UserAccountDeletionFacade userAccountDeletionFacade) throws UserNotFoundException, UserException {
        UserLiztube userLiztube = authBusiness.getConnectedUser(true);
        if(!userLiztube.getPassword().equals(encoder.encodePassword(userAccountDeletionFacade.getPassword(), null))){
            throw new UserException("Delete user account - incorrect password", EnumError.DELETE_ACCOUNT_BAD_PASSWORD);
        }
        userLiztubeRepository.delete(userLiztube);
    }

}

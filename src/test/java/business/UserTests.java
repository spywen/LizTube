package business;

import com.excilys.ebi.spring.dbunit.config.DBOperation;
import com.excilys.ebi.spring.dbunit.test.DataSet;
import com.excilys.ebi.spring.dbunit.test.DataSetTestExecutionListener;
import com.liztube.business.AuthBusiness;
import com.liztube.business.UserBusiness;
import com.liztube.config.JpaConfigs;
import com.liztube.entity.UserLiztube;
import com.liztube.exception.UserException;
import com.liztube.exception.UserNotFoundException;
import com.liztube.exception.exceptionType.PublicException;
import com.liztube.repository.UserLiztubeRepository;
import com.liztube.repository.VideoRepository;
import com.liztube.repository.ViewRepository;
import com.liztube.utils.EnumError;
import com.liztube.utils.EnumRole;
import com.liztube.utils.facade.UserAccountDeletionFacade;
import com.liztube.utils.facade.UserFacade;
import com.liztube.utils.facade.UserPasswordFacade;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaConfigs.class}, loader = AnnotationConfigContextLoader.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DataSetTestExecutionListener.class })
@DataSet(value = "/data/UserDataset.xml", tearDownOperation = DBOperation.DELETE_ALL)
public class UserTests {

    //region preparation
    @Autowired
    AuthBusiness authBusiness;
    @Autowired
    UserBusiness userBusiness;
    @Autowired
    UserLiztubeRepository userLiztubeRepository;
    @Autowired
    ViewRepository viewRepository;
    @Autowired
    VideoRepository videoRepository;

    public UserLiztube userLiztube;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    public UserFacade userToUpdate;

    public UserPasswordFacade userPasswordFacade;

    @Before
    public void SetUp(){
        Timestamp birthday = Timestamp.valueOf(LocalDateTime.of(1991, Month.FEBRUARY, 1, 0, 0));
        userToUpdate = new UserFacade().setPseudo("userUpdate")
                .setBirthdate(birthday)
                .setEmail("userInfos@hotmail.fr")
                .setFirstname("user")
                .setLastname("user")
                .setIsfemale(false);

        userPasswordFacade = new UserPasswordFacade()
                .setNewPassword("liztube")
                .setOldPassword("cisco");

        userLiztube = userLiztubeRepository.findByPseudo("spywen");

        //User connected
        List<GrantedAuthority> userAuthorities=new ArrayList<GrantedAuthority>(2);
        userAuthorities.add(new SimpleGrantedAuthority(EnumRole.AUTHENTICATED.toString()));
        userAuthorities.add(new SimpleGrantedAuthority(EnumRole.USER.toString()));
        User adminSpringUser = new User("spywen","cisco", userAuthorities);
        Authentication auth = new UsernamePasswordAuthenticationToken(adminSpringUser,null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
    //endregion

    //region get user profile
    @Test
    public void user_should_get_user_infos_successfully() throws UserNotFoundException, UserException{
        UserFacade userForRegistration = userBusiness.getUserInfo();

        assertThat(userForRegistration).isNotNull();
        assertThat(userForRegistration.getBirthdate()).isEqualTo(userLiztube.getBirthdate());
        assertThat(userForRegistration.getEmail()).isEqualTo(userLiztube.getEmail());
        assertThat(userForRegistration.getLastname()).isEqualTo(userLiztube.getLastname());
        assertThat(userForRegistration.getFirstname()).isEqualTo(userLiztube.getFirstname());
    }
    //endregion

    //region update user
    @Test
    public void firstname_should_be_changed_successfully() throws UserNotFoundException, UserException{
        assertThat(userLiztube.getFirstname()).isEqualTo("Laurent");
        userToUpdate.setFirstname("Youcef");
        userLiztube = userBusiness.updateUserInfo(userToUpdate);
        assertThat(userLiztube.getFirstname()).isEqualTo("Youcef");

    }

    @Test
    public void lastname_should_be_changed_successfully() throws UserNotFoundException, UserException{
        assertThat(userLiztube.getLastname()).isEqualTo("Babin");
        userToUpdate.setLastname("BenZ");
        userLiztube = userBusiness.updateUserInfo(userToUpdate);
        assertThat(userLiztube.getLastname()).isEqualTo("BenZ");
    }

    @Test
    public void birthdate_should_be_changed_successfully() throws UserNotFoundException, UserException{
        assertThat(userLiztube.getBirthdate()).isEqualTo(Timestamp.valueOf(LocalDateTime.of(2013, Month.OCTOBER, 5, 10, 15, 26)));
        userToUpdate.setBirthdate(Timestamp.valueOf(LocalDateTime.of(2005, Month.MAY, 6, 0, 0)));
        userLiztube = userBusiness.updateUserInfo(userToUpdate);
        assertThat(userLiztube.getBirthdate()).isEqualTo(Timestamp.valueOf(LocalDateTime.of(2005, Month.MAY, 6, 0, 0)));
    }

    @Test
    public void date_modification_changed_should_be_filled() throws UserException, UserNotFoundException {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        userLiztube = userBusiness.updateUserInfo(userToUpdate);
        assertThat(userLiztube.getModificationdate()).isEqualToIgnoringSeconds(now);
    }

    @Test
    public void should_raise_an_error_if_user_not_connected() throws UserException {
        SecurityContextHolder.getContext().setAuthentication(null);
        try{
            userBusiness.updateUserInfo(userToUpdate);
            fail("Should throw exception");
        }catch (UserNotFoundException e){
            assertThat(e.getMessages()).contains(authBusiness.USER_NOT_FOUND_EXCEPTION);
        }
    }

    @Test
    public void should_raise_an_error_if_email_already_used(){
        userToUpdate = userToUpdate.setEmail("kmille@hotmail.fr");
        try{
            userBusiness.updateUserInfo(userToUpdate);
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(EnumError.USER_EMAIL_OR_PSEUDO_ALREADY_USED);
        }
    }

    @Test
    public void should_raise_an_error_if_pseudo_already_used(){
        userToUpdate = userToUpdate.setPseudo("kmille");
        try{
            userBusiness.updateUserInfo(userToUpdate);
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(EnumError.USER_EMAIL_OR_PSEUDO_ALREADY_USED);
        }
    }

    @Test
    public void should_raise_an_error_if_firstname_empty() throws UserNotFoundException, UserException {
        userToUpdate = userToUpdate.setFirstname("");
        try{
            userBusiness.updateUserInfo(userToUpdate);
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(EnumError.USER_FIRSTNAME_SIZE);
        }
    }

    @Test
    public void should_raise_an_error_if_lastname_empty() throws UserNotFoundException, UserException {
        userToUpdate = userToUpdate.setLastname("");
        try{
            userBusiness.updateUserInfo(userToUpdate);
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(EnumError.USER_LASTNAME_SIZE);
        }
    }

    @Test
    public void should_raise_an_error_if_birthday_is_not_a_past_date() throws UserNotFoundException, UserException {
        userToUpdate = userToUpdate.setBirthdate(Timestamp.valueOf(LocalDateTime.of(2016, Month.JANUARY, 1, 0, 0)));
        try{
            userBusiness.updateUserInfo(userToUpdate);
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(EnumError.USER_BIRTHDAY_PAST_DATE);
        }
    }
    //endregion

    //region change password
    @Test
    public void change_password_should_raise_an_error_if_user_not_connected() throws UserException {
        SecurityContextHolder.getContext().setAuthentication(null);
        try{
            userBusiness.changeUserPassword(userPasswordFacade);
            fail("Should throw exception");
        }catch (UserNotFoundException e){
            assertThat(e.getMessages()).contains(authBusiness.USER_NOT_FOUND_EXCEPTION);
        }
    }

    @Test
    public void should_failed_because_old_password_false() throws UserNotFoundException {
        try{
            userBusiness.changeUserPassword(userPasswordFacade.setOldPassword("OLDPWDINVALID"));
            fail("Should throw exception");
        }catch (UserException e){
            assertThat(e.getMessages()).contains(EnumError.USER_OLD_PASSWORD_INVALID);
        }
    }

    @Test
    public void should_successfully_change_password() throws UserNotFoundException, UserException {
        ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
        userBusiness.changeUserPassword(userPasswordFacade);
        userLiztube = userLiztubeRepository.findByPseudo("spywen");
        assertThat(userLiztube.getPassword()).isEqualTo(encoder.encodePassword(userPasswordFacade.getNewPassword(),null));
    }

    @Test
    public void should_raise_an_error_if_password_too_short() throws UserNotFoundException, UserException {
        userPasswordFacade = userPasswordFacade.setNewPassword("cisc");//4 characters
        try{
            userBusiness.changeUserPassword(userPasswordFacade);
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(EnumError.USER_PASSWORD_FORMAT);
        }
    }

    @Test
    public void should_raise_an_error_if_password_too_long() throws UserNotFoundException, UserException {
        userPasswordFacade = userPasswordFacade.setNewPassword("ciscociscociscociscociscociscociscociscociscociscoc");//51 characters
        try{
            userBusiness.changeUserPassword(userPasswordFacade);
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(EnumError.USER_PASSWORD_FORMAT);
        }
    }
    //endregion

    //region delete user account
    @Test
    public void should_remove_user_successfully() throws UserNotFoundException, UserException {
        UserAccountDeletionFacade userAccountDeletionFacade = new UserAccountDeletionFacade();
        userAccountDeletionFacade.setPassword("cisco");
        userBusiness.delete(userAccountDeletionFacade);
        assertThat(userLiztubeRepository.findOne((long)1)).isNull();
    }

    @Test
    public void should_remove_user_videos_and_relatives_views() throws UserNotFoundException, UserException {
        assertThat(videoRepository.findAll().size()).isEqualTo(1);
        assertThat(viewRepository.findAll().size()).isEqualTo(1);
        UserAccountDeletionFacade userAccountDeletionFacade = new UserAccountDeletionFacade();
        userAccountDeletionFacade.setPassword("cisco");
        userBusiness.delete(userAccountDeletionFacade);
        assertThat(videoRepository.findAll().size()).isEqualTo(0);
        assertThat(viewRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    public void delete_user_account_should_raise_an_error_if_user_not_connected() throws UserException {
        UserAccountDeletionFacade userAccountDeletionFacade = new UserAccountDeletionFacade();
        userAccountDeletionFacade.setPassword("cisco");
        SecurityContextHolder.getContext().setAuthentication(null);
        try{
            userBusiness.delete(userAccountDeletionFacade);
            fail("Should throw exception");
        }catch (UserNotFoundException e){
            assertThat(e.getMessages()).contains(authBusiness.USER_NOT_FOUND_EXCEPTION);
        }
    }

    @Test
    public void delete_user_account_should_raise_an_error_if_bad_password_sent() throws UserNotFoundException {
        UserAccountDeletionFacade userAccountDeletionFacade = new UserAccountDeletionFacade();
        userAccountDeletionFacade.setPassword("invalidPassword");
        try{
            userBusiness.delete(userAccountDeletionFacade);
            fail("Should throw exception");
        }catch (UserException e){
            assertThat(e.getMessages()).contains(EnumError.DELETE_ACCOUNT_BAD_PASSWORD);
        }
    }
    //endregion

}

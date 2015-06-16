package business;

import com.excilys.ebi.spring.dbunit.test.DataSet;
import com.excilys.ebi.spring.dbunit.test.DataSetTestExecutionListener;
import com.liztube.business.AuthBusiness;
import com.liztube.config.JpaConfigs;
import com.liztube.entity.UserLiztube;
import com.liztube.exception.SigninException;
import com.liztube.exception.exceptionType.PublicException;
import com.liztube.repository.RoleRepository;
import com.liztube.repository.UserLiztubeRepository;
import com.liztube.utils.EnumError;
import com.liztube.utils.EnumRole;
import com.liztube.utils.facade.UserForRegistration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaConfigs.class}, loader = AnnotationConfigContextLoader.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DataSetTestExecutionListener.class })
@DataSet(value = "/data/UserDataset.xml")
public class SignInTests {
    @Autowired
    AuthBusiness authBusiness;

    @Autowired
    UserLiztubeRepository userLiztubeRepository;
    @Autowired
    RoleRepository roleRepository;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    public UserForRegistration newUser;

    @Before
    public void SetUp(){
        Timestamp birthday = Timestamp.valueOf(LocalDateTime.of(1991, Month.FEBRUARY, 1, 0, 0));
        newUser = new UserForRegistration().setPseudo("newUser")
                .setBirthdate(birthday)
                .setEmail("newUser@hotmail.fr")
                .setFirstname("user")
                .setLastname("user")
                .setIsfemale(false)
                .setPassword("cisco");
    }

    @Test
    public void nominal_case() throws SigninException {
        assertThat(userLiztubeRepository.findAll().size()).isEqualTo(2);
        UserLiztube userLiztube = authBusiness.signIn(newUser);
        assertThat(userLiztubeRepository.findAll().size()).isEqualTo(3);
        assertThat(userLiztube.getId()).isGreaterThan(2);
        assertThat(userLiztube.getIsactive()).isTrue();
    }

    @Test
    public void password_should_be_encrypted() throws SigninException {
        assertThat(newUser.getPassword()).isEqualTo("cisco");
        UserLiztube userLiztube = authBusiness.signIn(newUser);

        ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
        assertThat(userLiztube.getPassword()).isEqualTo(encoder.encodePassword(newUser.getPassword(),null));
    }

    @Test
    public void dates_should_be_filled() throws SigninException {
        Timestamp before = Timestamp.valueOf(LocalDateTime.now());
        UserLiztube userLiztube = authBusiness.signIn(newUser);
        Timestamp after = Timestamp.valueOf(LocalDateTime.now());

        assertThat(userLiztube.getRegisterdate()).isBetween(before, after);
        assertThat(userLiztube.getModificationdate()).isBetween(before, after);
    }

    //Role assignement
    @Test
    public void authenticated_and_user_role_should_be_assigned_to_the_new_user() throws SigninException {
        UserLiztube userLiztube = authBusiness.signIn(newUser);
        assertThat(userLiztube.getRoles().size()).isEqualTo(2);
        assertThat(userLiztube.getRoles()).contains(roleRepository.findByName(EnumRole.USER.toString()));
        assertThat(userLiztube.getRoles()).contains(roleRepository.findByName(EnumRole.AUTHENTICATED.toString()));
        assertThat(userLiztube.getRoles()).doesNotContain(roleRepository.findByName(EnumRole.ADMIN.toString()));
    }


    //Other errors manage by entity validations

    @Test
    public void should_raise_an_error_if_password_too_short() throws SigninException {
        newUser = newUser.setPassword("cisc");//4 characters
        try{
            authBusiness.signIn(newUser);
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(EnumError.USER_PASSWORD_FORMAT);
        }
    }

    @Test
    public void should_raise_an_error_if_password_too_long() throws SigninException {
        newUser = newUser.setPassword("ciscociscociscociscociscociscociscociscociscociscoc");//51 characters
        try{
            authBusiness.signIn(newUser);
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(EnumError.USER_PASSWORD_FORMAT);
        }
    }

    @Test
    public void should_raise_an_error_if_firstname_empty() throws SigninException {
        newUser = newUser.setFirstname("");
        try{
            authBusiness.signIn(newUser);
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(EnumError.USER_FIRSTNAME_SIZE);
        }
    }

    @Test
    public void should_raise_an_error_if_lastname_empty() throws SigninException {
        newUser = newUser.setLastname("");
        try{
            authBusiness.signIn(newUser);
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(EnumError.USER_LASTNAME_SIZE);
        }
    }

    @Test
    public void should_raise_an_error_if_pseudo_empty() throws SigninException {
        newUser = newUser.setPseudo("");
        try{
            authBusiness.signIn(newUser);
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(EnumError.USER_PSEUDO_SIZE);
        }
    }

    @Test
    public void should_raise_an_error_if_pseudo_contains_less_than_3_characters() throws SigninException {
        newUser = newUser.setPseudo("az");
        try{
            authBusiness.signIn(newUser);
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(EnumError.USER_PSEUDO_SIZE);
        }
    }

    @Test
    public void should_raise_an_error_if_birthday_is_null() throws SigninException {
        newUser = newUser.setBirthdate(null);
        try{
            authBusiness.signIn(newUser);
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(EnumError.USER_BIRTHDAY_NOTNULL);
        }
    }

    @Test
    public void should_raise_an_error_if_birthday_is_not_a_past_date() throws SigninException {
        newUser = newUser.setBirthdate(Timestamp.valueOf(LocalDateTime.of(2016, Month.JANUARY, 1, 0, 0)));
        try{
            authBusiness.signIn(newUser);
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(EnumError.USER_BIRTHDAY_PAST_DATE);
        }
    }

    @Test
    public void should_raise_an_error_if_email_not_well_formatted() throws SigninException {
        newUser = newUser.setEmail("bademail");
        try{
            authBusiness.signIn(newUser);
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(EnumError.USER_EMAIL_FORMAT);
        }
    }

    @Test
    public void should_raise_an_error_if_email_is_not_well_sized() throws SigninException {
        newUser = newUser.setEmail("");
        try{
            authBusiness.signIn(newUser);
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(EnumError.USER_EMAIL_SIZE);
        }
    }

    @Test
    public void should_raise_an_error_if_isfemale_is_null() throws SigninException {
        newUser = newUser.setIsfemale(null);
        try{
            authBusiness.signIn(newUser);
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(EnumError.USER_ISFEMALE_NOTNULL);
        }
    }

    @Test
    public void should_raise_an_error_if_email_already_used(){
        newUser = newUser.setEmail("spywen@hotmail.fr");
        try{
            authBusiness.signIn(newUser);
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(EnumError.USER_EMAIL_OR_PSEUDO_ALREADY_USED);
        }
    }

    @Test
    public void should_raise_an_error_if_pseudo_already_used(){
        newUser = newUser.setPseudo("spywen");
        try{
            authBusiness.signIn(newUser);
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(EnumError.USER_EMAIL_OR_PSEUDO_ALREADY_USED);
        }
    }

}

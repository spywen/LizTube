package business;

import com.excilys.ebi.spring.dbunit.config.DBOperation;
import com.excilys.ebi.spring.dbunit.test.DataSet;
import com.excilys.ebi.spring.dbunit.test.DataSetTestExecutionListener;
import com.liztube.business.AuthBusiness;
import com.liztube.config.JpaConfigs;
import com.liztube.entity.UserLiztube;
import com.liztube.exception.UserNotFoundException;
import com.liztube.repository.UserLiztubeRepository;
import com.liztube.utils.EnumRole;
import com.liztube.utils.facade.UserConnectedProfile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaConfigs.class}, loader = AnnotationConfigContextLoader.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DataSetTestExecutionListener.class })
@DataSet(value = "/data/UserDataset.xml", tearDownOperation = DBOperation.DELETE_ALL)
public class AuthConnectedTests {
    @Autowired
    AuthBusiness authBusiness;

    @Autowired
    UserLiztubeRepository userLiztubeRepository;


    @Before
    public void setUp(){
        //Admin connected
        List<GrantedAuthority> adminAuthorities=new ArrayList<GrantedAuthority>(2);
        adminAuthorities.add(new SimpleGrantedAuthority(EnumRole.AUTHENTICATED.toString()));
        adminAuthorities.add(new SimpleGrantedAuthority(EnumRole.ADMIN.toString()));
        User adminSpringUser = new User("spywen","cisco", adminAuthorities);
        Authentication auth = new UsernamePasswordAuthenticationToken(adminSpringUser,null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @org.junit.After
    public void tearDown(){
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Test
    public void getUserConnectedProfile_should_get_profile_of_connected_user(){
        UserConnectedProfile userConnectedProfile = authBusiness.getUserConnectedProfile(true);
        assertThat(userConnectedProfile.getPseudo()).isEqualTo("spywen");
        assertThat(userConnectedProfile.getRoles()).isNotEmpty();
        assertThat(userConnectedProfile.getRoles().size()).isEqualTo(2);
        assertThat(userConnectedProfile.getRoles()).contains("ADMIN","AUTHENTICATED");
        assertThat(userConnectedProfile.getId()).isEqualTo(1);
        assertThat(userConnectedProfile.getEmail()).isEqualTo("spywen@hotmail.fr");
    }

    @Test
    public void getConnectedUser_should_get_connected_user_complete_profile() throws UserNotFoundException {
        UserLiztube userLiztubeToFound = userLiztubeRepository.findByPseudo("spywen");
        assertThat(authBusiness.getConnectedUser(true).getId()).isEqualTo(userLiztubeToFound.getId());
    }
}

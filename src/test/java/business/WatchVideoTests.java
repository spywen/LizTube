package business;

import com.excilys.ebi.spring.dbunit.test.DataSet;
import com.excilys.ebi.spring.dbunit.test.DataSetTestExecutionListener;
import com.liztube.business.PathBusiness;
import com.liztube.business.VideoBusiness;
import com.liztube.config.JpaConfigs;
import com.liztube.entity.View;
import com.liztube.exception.PathException;
import com.liztube.exception.UserNotFoundException;
import com.liztube.exception.VideoException;
import com.liztube.exception.exceptionType.PublicException;
import com.liztube.repository.ViewRepository;
import com.liztube.utils.EnumRole;
import com.liztube.utils.facade.video.VideoCreationFacade;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaConfigs.class}, loader = AnnotationConfigContextLoader.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DataSetTestExecutionListener.class })
@DataSet(value = "/data/VideoDataset.xml")
@TestPropertySource("/application.testing.properties")
public class WatchVideoTests {
    @Autowired
    VideoBusiness videoBusiness;
    @Autowired
    PathBusiness pathBusiness;

    @Autowired
    ViewRepository viewRepository;

    @Before
    public void setUp() throws IOException, PathException {
        File fakeVideoA = new File(pathBusiness.getVideoLibraryPath() + File.separator + "a");
        File fakeVideoD = new File(pathBusiness.getVideoLibraryPath() + File.separator + "d");
        File fakeVideoE = new File(pathBusiness.getVideoLibraryPath() + File.separator + "e");
        if(!fakeVideoA.exists()){
            fakeVideoA.createNewFile();
        }
        if(!fakeVideoD.exists()){
            fakeVideoD.createNewFile();
        }
        if(!fakeVideoE.exists()){
            fakeVideoE.createNewFile();
        }

        //User connected
        List<GrantedAuthority> userAuthorities=new ArrayList<GrantedAuthority>(2);
        userAuthorities.add(new SimpleGrantedAuthority(EnumRole.AUTHENTICATED.toString()));
        userAuthorities.add(new SimpleGrantedAuthority(EnumRole.USER.toString()));
        User adminSpringUser = new User("spywen","cisco", userAuthorities);
        Authentication auth = new UsernamePasswordAuthenticationToken(adminSpringUser,null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    public void should_raise_an_error_if_video_not_exist() throws VideoException, UserNotFoundException, IOException {
        try{
            videoBusiness.watch("abcd");
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(videoBusiness.VIDEO_NOT_FOUND);
        }
    }

    @Test
    public void should_raise_an_error_if_video_private_and_user_not_owner() throws VideoException, UserNotFoundException, IOException {
        try{
            videoBusiness.watch("f");
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(videoBusiness.VIDEO_NOT_AVAILABLE);
        }
    }

    @Test
    public void should_be_successfull_if_user_is_owner_of_private_video() throws VideoException, UserNotFoundException, IOException, PathException {
        videoBusiness.watch("d");
    }

    @Test
    public void should_log_view_if_user_never_seen_video() throws VideoException, UserNotFoundException, IOException, PathException {
        assertThat(viewRepository.findByUserIdAndVideoKey(1, "a").size()).isEqualTo(0);
        videoBusiness.watch("a");
        List<View> views = viewRepository.findByUserIdAndVideoKey(1, "a");
        assertThat(views.size()).isEqualTo(1);
        View view = views.get(0);
        assertThat(view.getUser().getId()).isEqualTo(1);
        assertThat(view.getVideo().getKey()).isEqualTo("a");
        assertThat(view.isAsShared()).isEqualTo(false);
    }

    @Test
    public void should_log_view_as_shared() throws VideoException, UserNotFoundException, IOException, PathException {
        assertThat(viewRepository.findByUserIdAndVideoKey(1, "e").size()).isEqualTo(0);
        videoBusiness.watch("e");
        List<View> views = viewRepository.findByUserIdAndVideoKey(1, "e");
        assertThat(views.size()).isEqualTo(1);
        View view = views.get(0);
        assertThat(view.getUser().getId()).isEqualTo(1);
        assertThat(view.getVideo().getKey()).isEqualTo("e");
        assertThat(view.isAsShared()).isEqualTo(true);
    }

    @Test
    public void should_not_log_view_if_user_already_seen_video() throws VideoException, UserNotFoundException, IOException, PathException {
        SecurityContextHolder.getContext().setAuthentication(null);

        List<GrantedAuthority> userAuthorities=new ArrayList<GrantedAuthority>(2);
        userAuthorities.add(new SimpleGrantedAuthority(EnumRole.AUTHENTICATED.toString()));
        userAuthorities.add(new SimpleGrantedAuthority(EnumRole.USER.toString()));
        User adminSpringUser = new User("kmille","cisco", userAuthorities);
        Authentication auth = new UsernamePasswordAuthenticationToken(adminSpringUser,null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThat(viewRepository.findByUserIdAndVideoKey(2, "a").size()).isEqualTo(2);
        videoBusiness.watch("a");
        assertThat(viewRepository.findByUserIdAndVideoKey(2, "a").size()).isEqualTo(2);
    }

    @Test
    public void should_not_log_view_if_user_disconnected() throws VideoException, UserNotFoundException, IOException, PathException {
        SecurityContextHolder.getContext().setAuthentication(null);
        assertThat(viewRepository.findAll().size()).isEqualTo(2);
        videoBusiness.watch("a");
        assertThat(viewRepository.findAll().size()).isEqualTo(2);
    }
}

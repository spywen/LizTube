package business;

import com.excilys.ebi.spring.dbunit.test.DataSet;
import com.excilys.ebi.spring.dbunit.test.DataSetTestExecutionListener;
import com.liztube.business.SearchForVideosBusiness;
import com.liztube.config.JpaConfigs;
import com.liztube.utils.EnumRole;
import com.liztube.utils.EnumVideoOrderBy;
import com.liztube.utils.facade.video.GetVideosFacade;
import com.liztube.utils.facade.video.VideoSearchFacade;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaConfigs.class}, loader = AnnotationConfigContextLoader.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DataSetTestExecutionListener.class })
@DataSet(value = "/data/SearchVideosDataset.xml")
@TestPropertySource("/application.testing.properties")
public class SearchForVideosBusinessTests {

    @Autowired
    SearchForVideosBusiness searchForVideosBusiness;

    Authentication userAuth;
    Authentication ownerAuth;

    //region before/after
    @Before
    public void setUp(){
        List<GrantedAuthority> userAuthorities=new ArrayList<GrantedAuthority>(2);
        userAuthorities.add(new SimpleGrantedAuthority(EnumRole.AUTHENTICATED.toString()));
        userAuthorities.add(new SimpleGrantedAuthority(EnumRole.USER.toString()));
        User userSpringUser = new User("test","cisco", userAuthorities);
        userAuth = new UsernamePasswordAuthenticationToken(userSpringUser,null);

        List<GrantedAuthority> ownerAuthorities=new ArrayList<GrantedAuthority>(2);
        ownerAuthorities.add(new SimpleGrantedAuthority(EnumRole.AUTHENTICATED.toString()));
        ownerAuthorities.add(new SimpleGrantedAuthority(EnumRole.USER.toString()));
        User ownerSpringUser = new User("kmille","cisco", ownerAuthorities);
        ownerAuth = new UsernamePasswordAuthenticationToken(ownerSpringUser,null);
    }

    @org.junit.After
    public void tearDown(){
        SecurityContextHolder.getContext().setAuthentication(null);
    }
    //endregion

    //region default search with pagination
    @Test
    public void defaultSearch_should_return_5_latest_videos(){
        VideoSearchFacade videoSearchFacade = new VideoSearchFacade().setPage(1);
        GetVideosFacade videosFound = searchForVideosBusiness.GetVideos(videoSearchFacade);
        assertThat(videosFound.getVideos().size()).isEqualTo(5);
        assertThat(videosFound.getVideos().get(0).getTitle()).isEqualTo("K video");
        assertThat(videosFound.getVideos().get(4).getTitle()).isEqualTo("F video");
    }

    @Test
    public void defaultSearch_should_take_in_consideration_pagination_asked_for(){
        VideoSearchFacade videoSearchFacade = new VideoSearchFacade().setPage(1).setPagination(2);
        GetVideosFacade videosFound = searchForVideosBusiness.GetVideos(videoSearchFacade);
        assertThat(videosFound.getVideos().size()).isEqualTo(2);
        assertThat(videosFound.getTotalPage()).isEqualTo(5);
        assertThat(videosFound.getVideosTotalCount()).isEqualTo(9);
        assertThat(videosFound.getCurrentPage()).isEqualTo(1);
        assertThat(videosFound.getVideos().get(0).getTitle()).isEqualTo("K video");
        assertThat(videosFound.getVideos().get(1).getTitle()).isEqualTo("J video");
    }

    @Test
    public void defaultSearch_should_return_correct_additonnal_data(){
        VideoSearchFacade videoSearchFacade = new VideoSearchFacade().setPage(1);
        GetVideosFacade videosFound = searchForVideosBusiness.GetVideos(videoSearchFacade);
        assertThat(videosFound.getCurrentPage()).isEqualTo(1);
        assertThat(videosFound.getVideosTotalCount()).isEqualTo(9);
        assertThat(videosFound.getTotalPage()).isEqualTo(2);
    }

    @Test
    public void defaultSearch_should_return_4_latest_videos_on_page_2(){
        VideoSearchFacade videoSearchFacade = new VideoSearchFacade().setPage(2);
        GetVideosFacade videosFound = searchForVideosBusiness.GetVideos(videoSearchFacade);
        assertThat(videosFound.getVideos().size()).isEqualTo(4);
        assertThat(videosFound.getVideos().get(0).getTitle()).isEqualTo("E video");
        assertThat(videosFound.getVideos().get(3).getTitle()).isEqualTo("A video");
    }

    @Test
    public void defaultSearch_should_return_videos_data(){
        VideoSearchFacade videoSearchFacade = new VideoSearchFacade().setPage(2);
        GetVideosFacade videosFound = searchForVideosBusiness.GetVideos(videoSearchFacade);
        assertThat(videosFound.getVideos().size()).isEqualTo(4);
        assertThat(videosFound.getVideos().get(3).getTitle()).isEqualTo("A video");
        assertThat(videosFound.getVideos().get(3).getDescription()).isEqualTo("desc of a");
        assertThat(videosFound.getVideos().get(3).getKey()).isEqualTo("a");
        assertThat(videosFound.getVideos().get(3).getViews()).isEqualTo(2);
        assertThat(videosFound.getVideos().get(3).getOwnerId()).isEqualTo(1);
        assertThat(videosFound.getVideos().get(3).getOwnerPseudo()).isEqualTo("spywen");
        assertThat(videosFound.getVideos().get(3).isPublic()).isTrue();
        assertThat(videosFound.getVideos().get(3).isPublicLink()).isTrue();
        assertThat(videosFound.getVideos().get(3).getCreationDate()).isEqualTo(Timestamp.valueOf(LocalDateTime.of(2013, Month.OCTOBER, 5, 10, 15, 26)));
        assertThat(videosFound.getVideos().get(3).getDuration()).isEqualTo(25000);
    }

    @Test
    public void defaultSearch_should_return_specified_count_of_videos(){
        VideoSearchFacade videoSearchFacade = new VideoSearchFacade().setPagination(2);
        GetVideosFacade videosFound = searchForVideosBusiness.GetVideos(videoSearchFacade);
        assertThat(videosFound.getVideos().size()).isEqualTo(2);
    }
    //endregion

    //region search by user
    @Test
    public void searchByUser_for_unconnected_user_should_return_public_video_of_user(){
        VideoSearchFacade videoSearchFacade = new VideoSearchFacade().setPage(1).setUserId(2);
        GetVideosFacade videosFound = searchForVideosBusiness.GetVideos(videoSearchFacade);
        assertThat(videosFound.getVideos().size()).isEqualTo(3);
        assertThat(videosFound.getTotalPage()).isEqualTo(1);
    }

    @Test
    public void searchByUser_for_connected_user_should_return_public_video_of_another_user(){
        SecurityContextHolder.getContext().setAuthentication(userAuth);

        VideoSearchFacade videoSearchFacade = new VideoSearchFacade().setPage(1).setUserId(2);
        GetVideosFacade videosFound = searchForVideosBusiness.GetVideos(videoSearchFacade);
        assertThat(videosFound.getVideos().size()).isEqualTo(3);
        assertThat(videosFound.getTotalPage()).isEqualTo(1);
    }

    @Test
    public void searchByUser_for_connected_user_should_return_all_his_videos(){
        SecurityContextHolder.getContext().setAuthentication(ownerAuth);

        VideoSearchFacade videoSearchFacade = new VideoSearchFacade().setPage(1).setUserId(2);
        GetVideosFacade videosFound = searchForVideosBusiness.GetVideos(videoSearchFacade);
        assertThat(videosFound.getVideos().size()).isEqualTo(4);
        assertThat(videosFound.getTotalPage()).isEqualTo(1);
    }
    //endregion

    //region search by order by
    @Test
    public void should_return_videos_ordered_by_most_viewed(){
        VideoSearchFacade videoSearchFacade = new VideoSearchFacade().setOrderBy(EnumVideoOrderBy.MOSTVIEWED);
        GetVideosFacade videosFound = searchForVideosBusiness.GetVideos(videoSearchFacade);
        assertThat(videosFound.getVideosTotalCount()).isEqualTo(9);
        assertThat(videosFound.getVideos().get(0).getKey()).isEqualTo("f");
    }

    @Test
    public void should_return_videos_ordered_by_most_recent(){
        VideoSearchFacade videoSearchFacade = new VideoSearchFacade().setOrderBy(EnumVideoOrderBy.MOSTRECENT);
        GetVideosFacade videosFound = searchForVideosBusiness.GetVideos(videoSearchFacade);
        assertThat(videosFound.getVideosTotalCount()).isEqualTo(9);
        assertThat(videosFound.getVideos().get(0).getKey()).isEqualTo("e");
    }

    @Test
    public void should_return_videos_ordered_by_most_shared(){
        VideoSearchFacade videoSearchFacade = new VideoSearchFacade().setOrderBy(EnumVideoOrderBy.MOSTSHARED);
        GetVideosFacade videosFound = searchForVideosBusiness.GetVideos(videoSearchFacade);
        assertThat(videosFound.getVideosTotalCount()).isEqualTo(9);
        assertThat(videosFound.getVideos().get(0).getKey()).isEqualTo("a");
    }
    //endregion

    //region search by keyword
    @Test
    public void defaultSearch_should_return_video_with_title_in_there_title(){
        VideoSearchFacade videoSearchFacade = new VideoSearchFacade().setKeyword("TiTle");
        GetVideosFacade videosFound = searchForVideosBusiness.GetVideos(videoSearchFacade);
        assertThat(videosFound.getVideos().size()).isEqualTo(1);
        assertThat(videosFound.getVideos().get(0).getKey()).isEqualTo("b");
    }

    @Test
    public void defaultSearch_should_return_video_with_description_in_their_description(){
        VideoSearchFacade videoSearchFacade = new VideoSearchFacade().setKeyword("descriptIon");
        GetVideosFacade videosFound = searchForVideosBusiness.GetVideos(videoSearchFacade);
        assertThat(videosFound.getVideos().size()).isEqualTo(1);
        assertThat(videosFound.getVideos().get(0).getKey()).isEqualTo("g");
    }

    @Test
    public void defaultSearch_should_return_video_with_description_with_spaces(){
        VideoSearchFacade videoSearchFacade = new VideoSearchFacade().setKeyword("desCRip+ok");
        GetVideosFacade videosFound = searchForVideosBusiness.GetVideos(videoSearchFacade);
        assertThat(videosFound.getVideos().size()).isEqualTo(1);
        assertThat(videosFound.getVideos().get(0).getKey()).isEqualTo("j");
    }

    @Test
    public void defaultSearch_should_return_video_with_owner_pseudo(){
        VideoSearchFacade videoSearchFacade = new VideoSearchFacade().setKeyword("kMille");
        GetVideosFacade videosFound = searchForVideosBusiness.GetVideos(videoSearchFacade);
        assertThat(videosFound.getVideos().size()).isEqualTo(3);
    }
    //endregion

}

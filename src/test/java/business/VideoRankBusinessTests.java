package business;

import com.excilys.ebi.spring.dbunit.test.DataSet;
import com.excilys.ebi.spring.dbunit.test.DataSetTestExecutionListener;
import com.liztube.business.SearchForVideosBusiness;
import com.liztube.business.VideoBusiness;
import com.liztube.business.VideoRankBusiness;
import com.liztube.config.JpaConfigs;
import com.liztube.utils.EnumVideoOrderBy;
import com.liztube.utils.facade.video.GetVideosFacade;
import com.liztube.utils.facade.video.VideoSearchFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaConfigs.class}, loader = AnnotationConfigContextLoader.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DataSetTestExecutionListener.class })
@DataSet(value = "/data/VideoRankDataset.xml")
@TestPropertySource("/application.testing.properties")
public class VideoRankBusinessTests {
    @Autowired
    VideoRankBusiness videoRankBusiness;
    @Autowired
    SearchForVideosBusiness searchForVideosBusiness;

    @Test
    public void should_rank_videos_by_most_recent(){
        GetVideosFacade videosFound = searchForVideosBusiness.GetVideos(new VideoSearchFacade().setOrderBy(EnumVideoOrderBy.MOSTRECENT));
        assertThat(videosFound.getVideos().get(0).getKey()).isEqualTo("e");

        videoRankBusiness.runVideoRankProcedure();

        GetVideosFacade videosFoundAfterRankProcedure = searchForVideosBusiness.GetVideos(new VideoSearchFacade().setOrderBy(EnumVideoOrderBy.MOSTRECENT));
        assertThat(videosFoundAfterRankProcedure.getVideos().get(0).getKey()).isEqualTo("k");
    }

    @Test
    public void should_rank_videos_by_most_viewed(){
        GetVideosFacade videosFound = searchForVideosBusiness.GetVideos(new VideoSearchFacade().setOrderBy(EnumVideoOrderBy.MOSTVIEWED));
        assertThat(videosFound.getVideos().get(0).getKey()).isEqualTo("f");

        videoRankBusiness.runVideoRankProcedure();

        GetVideosFacade videosFoundAfterRankProcedure = searchForVideosBusiness.GetVideos(new VideoSearchFacade().setOrderBy(EnumVideoOrderBy.MOSTVIEWED));
        assertThat(videosFoundAfterRankProcedure.getVideos().get(0).getKey()).isEqualTo("a");
    }

    @Test
    public void should_rank_videos_by_most_shared(){
        GetVideosFacade videosFound = searchForVideosBusiness.GetVideos(new VideoSearchFacade().setOrderBy(EnumVideoOrderBy.MOSTSHARED));
        assertThat(videosFound.getVideos().get(0).getKey()).isEqualTo("h");

        videoRankBusiness.runVideoRankProcedure();

        GetVideosFacade videosFoundAfterRankProcedure = searchForVideosBusiness.GetVideos(new VideoSearchFacade().setOrderBy(EnumVideoOrderBy.MOSTSHARED));
        assertThat(videosFoundAfterRankProcedure.getVideos().get(0).getKey()).isEqualTo("b");
    }
}

package business.homeSuggestion;

import com.excilys.ebi.spring.dbunit.test.DataSet;
import com.excilys.ebi.spring.dbunit.test.DataSetTestExecutionListener;
import com.liztube.business.SearchForVideosBusiness;
import com.liztube.config.JpaConfigs;
import com.liztube.utils.EnumVideoOrderBy;
import com.liztube.utils.facade.video.GetVideosFacade;
import com.liztube.utils.facade.video.VideoSearchFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
@DataSet(value = "/data/SearchVideosHomeSuggestionDataset.xml")
@TestPropertySource("/application.testing.properties")
public class SearchForVideoBusinessHomeSuggestionTests {

    @Autowired
    SearchForVideosBusiness searchForVideosBusiness;
    @Autowired
    Environment environment;

    @Test
    public void should_return_videos_ordered_by_home_suggestion(){
        VideoSearchFacade videoSearchFacade = new VideoSearchFacade().setOrderBy(EnumVideoOrderBy.HOMESUGGESTION);
        GetVideosFacade videosFound = searchForVideosBusiness.GetVideos(videoSearchFacade);
        assertThat(videosFound.getVideosTotalCount()).isEqualTo(4);
        assertThat(videosFound.getVideos().get(0).getKey()).isEqualTo("c");
        assertThat(videosFound.getVideos().get(1).getKey()).isEqualTo("b");
        assertThat(videosFound.getVideos().get(2).getKey()).isEqualTo("d");
        assertThat(videosFound.getVideos().get(3).getKey()).isEqualTo("a");
    }

}

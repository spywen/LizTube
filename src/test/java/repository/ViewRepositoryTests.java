package repository;

import com.excilys.ebi.spring.dbunit.test.DataSet;
import com.excilys.ebi.spring.dbunit.test.DataSetTestExecutionListener;
import com.liztube.config.JpaConfigs;
import com.liztube.entity.View;
import com.liztube.repository.ViewRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by laurent on 05/06/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaConfigs.class}, loader = AnnotationConfigContextLoader.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DataSetTestExecutionListener.class })
@DataSet(value = "/data/VideoRankDataset.xml")
public class ViewRepositoryTests {
    @Autowired
    ViewRepository viewRepository;

    @Test
    public void should_not_found_user_associated_to_this_video(){
        List<View> views = viewRepository.findByUserIdAndVideoKey(1, "a");
        assertThat(views.size()).isEqualTo(0);
    }

    @Test
    public void should_not_found_video_associated_to_this_user(){
        List<View> views = viewRepository.findByUserIdAndVideoKey(2, "f");
        assertThat(views.size()).isEqualTo(0);
    }

    @Test
    public void should_found_a_view(){
        List<View> views = viewRepository.findByUserIdAndVideoKey(2, "c");
        assertThat(views.size()).isEqualTo(1);
    }
}

package repository;

import com.excilys.ebi.spring.dbunit.test.DataSet;
import com.excilys.ebi.spring.dbunit.test.DataSetTestExecutionListener;
import com.liztube.config.JpaConfigs;
import com.liztube.entity.Video;
import com.liztube.repository.VideoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaConfigs.class}, loader = AnnotationConfigContextLoader.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DataSetTestExecutionListener.class })
@DataSet(value = "/data/VideoDataset.xml")
public class VideoRepositoryTests {
    @Autowired
    VideoRepository videoRepository;

    @Test
    public void should_return_videos_orderbydesc(){
        final Pageable pageRequest = new PageRequest(
                0, 100, new Sort(
                new Sort.Order(Sort.Direction.DESC, "creationdate")
        ));
        List<Video> videos = videoRepository.findAll(pageRequest).getContent();
        assertThat(videos.get(0).getKey()).isEqualTo("f");
        assertThat(videos.size()).isEqualTo(6);
    }

    @Test
    public void should_return_videos_orderbyasc(){
        final Pageable pageRequest = new PageRequest(
                0, 100, new Sort(
                new Sort.Order(Sort.Direction.ASC, "creationdate")
        ));
        List<Video> videos = videoRepository.findAll(pageRequest).getContent();
        assertThat(videos.get(0).getKey()).isEqualTo("a");
        assertThat(videos.size()).isEqualTo(6);
    }

    @Test
    public void should_return_onlytwo_videos(){
        final Pageable pageRequest = new PageRequest(
                0, 2
        );
        List<Video> videos = videoRepository.findAll(pageRequest).getContent();
        assertThat(videos.size()).isEqualTo(2);
    }
}

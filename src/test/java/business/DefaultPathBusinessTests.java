package business;

import com.excilys.ebi.spring.dbunit.test.DataSetTestExecutionListener;
import com.liztube.business.PathBusiness;
import com.liztube.config.JpaConfigs;
import com.liztube.exception.PathException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaConfigs.class}, loader = AnnotationConfigContextLoader.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DataSetTestExecutionListener.class })
@TestPropertySource("/application.testing.properties")
public class DefaultPathBusinessTests {
    @Autowired
    PathBusiness pathBusiness;

    public ClassPathResource videoLibrary = new ClassPathResource("VideoLibrary/");
    public ClassPathResource videoThumbnailsLibrary = new ClassPathResource("VideoThumbnailsLibrary/");

    @Test
    public void getVideoLibraryPath() throws PathException, IOException {
        assertThat(pathBusiness.getVideoLibraryPath()).isEqualTo(videoLibrary.getFile().getAbsolutePath());
    }

    @Test
    public void getVideoThumbnailsLibraryPath() throws PathException, IOException {
        assertThat(pathBusiness.getVideoThumbnailsLibraryPath()).isEqualTo(videoThumbnailsLibrary.getFile().getAbsolutePath());
    }
}


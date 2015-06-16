package business;

import com.excilys.ebi.spring.dbunit.test.DataSetTestExecutionListener;
import com.liztube.business.PathBusiness;
import com.liztube.config.JpaConfigs;
import com.liztube.exception.PathException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaConfigs.class}, loader = AnnotationConfigContextLoader.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DataSetTestExecutionListener.class })
@TestPropertySource("/application.testing.path.exist.properties")
public class PathBusinessTests {
    @Autowired
    PathBusiness pathBusiness;

    @Test
    public void getVideoLibraryPath() throws PathException {
        assertThat(pathBusiness.getVideoLibraryPath()).isEqualTo("/var/liztube/videos");
    }

    @Test
    public void getVideoThumbnailsLibraryPath() throws PathException {
        assertThat(pathBusiness.getVideoThumbnailsLibraryPath()).isEqualTo("/var/liztube/thumbnails");
    }
}

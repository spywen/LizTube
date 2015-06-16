package config;

import com.liztube.config.JpaConfigs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaConfigs.class}, loader = AnnotationConfigContextLoader.class)
@TestPropertySource("/application.testing.properties")
public class ApplicationPropertiesTests {

    @Autowired
    Environment environment;

    @Test
    public void should_return_real_property(){
        assertThat(environment.getProperty("test")).isEqualTo("testing");
    }


}

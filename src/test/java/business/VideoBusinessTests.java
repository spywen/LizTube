package business;

import com.excilys.ebi.spring.dbunit.test.DataSet;
import com.excilys.ebi.spring.dbunit.test.DataSetTestExecutionListener;
import com.liztube.business.PathBusiness;
import com.liztube.business.ThumbnailBusiness;
import com.liztube.business.VideoBusiness;
import com.liztube.config.JpaConfigs;
import com.liztube.entity.Video;
import com.liztube.exception.PathException;
import com.liztube.exception.ThumbnailException;
import com.liztube.exception.UserNotFoundException;
import com.liztube.exception.VideoException;
import com.liztube.exception.exceptionType.PublicException;
import com.liztube.repository.VideoRepository;
import com.liztube.utils.EnumError;
import com.liztube.utils.EnumRole;
import com.liztube.utils.facade.video.VideoCreationFacade;
import com.liztube.utils.facade.video.VideoDataFacade;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaConfigs.class}, loader = AnnotationConfigContextLoader.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DataSetTestExecutionListener.class })
@DataSet(value = "/data/VideoDataset.xml")
@TestPropertySource("/application.testing.properties")
public class VideoBusinessTests {

    //region preparation
    @Autowired
    VideoBusiness videoBusiness;
    @Autowired
    ThumbnailBusiness thumbnailBusiness;
    @Autowired
    VideoRepository videoRepository;
    @Autowired
    PathBusiness pathBusiness;

    @Autowired
    Environment environment;

    private ClassPathResource files = new ClassPathResource("files/");
    private VideoCreationFacade videoCreationFacade;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp(){
        videoCreationFacade = new VideoCreationFacade().setTitle("title").setDescription("description").setPublic(false).setPublicLink(false);

        //User connected
        List<GrantedAuthority> userAuthorities=new ArrayList<GrantedAuthority>(2);
        userAuthorities.add(new SimpleGrantedAuthority(EnumRole.AUTHENTICATED.toString()));
        userAuthorities.add(new SimpleGrantedAuthority(EnumRole.USER.toString()));
        User adminSpringUser = new User("spywen","cisco", userAuthorities);
        Authentication auth = new UsernamePasswordAuthenticationToken(adminSpringUser,null);
        SecurityContextHolder.getContext().setAuthentication(auth);

    }

    @After
    public void setDown() throws IOException, PathException {
        //Remove all files inside the video library AND video thumbnails library after each tests
        File videoLibraryFolder = new File(pathBusiness.getVideoLibraryPath() + File.separator);
        File videoThumbnailLibraryFolder = new File(pathBusiness.getVideoThumbnailsLibraryPath() + File.separator);
        if(videoLibraryFolder.exists()){
            for(File file : videoLibraryFolder.listFiles()){
                file.delete();
            }
            for(File file : videoThumbnailLibraryFolder.listFiles()){
                file.delete();
            }
        }
    }
    //endregion

    //region upload
    @Test
    public void uploadVideo_should_raise_exception_if_not_mp4() throws IOException, UserNotFoundException, VideoException {
        FileInputStream inputFile = new FileInputStream(files.getFile().getAbsolutePath() + File.separator +"video.m4a");
        MockMultipartFile file = new MockMultipartFile("file", "video.m4a", "multipart/form-data", inputFile);
        try{
            videoBusiness.uploadVideo(file, videoCreationFacade);
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(videoBusiness.VIDEO_UPLOAD_NO_VALID_TYPE);
        }
    }

    @Test
    public void uploadVideo_should_raise_exception_if_exceed_max_file_size() throws IOException, UserNotFoundException, VideoException {
        FileInputStream inputFile = new FileInputStream(files.getFile().getAbsolutePath() + File.separator +"heavyVideo.mp4");
        MockMultipartFile file = new MockMultipartFile("file", "video.mp4", "multipart/form-data", inputFile);
        try{
            videoBusiness.uploadVideo(file, videoCreationFacade);
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(String.format(videoBusiness.VIDEO_UPLOAD_TOO_HEAVY, FileUtils.byteCountToDisplaySize(Integer.parseInt(environment.getProperty("upload.maxFileSize")))));
        }
    }

    @Test
    public void uploadVideo_should_return_key() throws IOException, UserNotFoundException, VideoException, ThumbnailException {
        FileInputStream inputFile = new FileInputStream(files.getFile().getAbsolutePath() + File.separator +"video.mp4");
        MockMultipartFile file = new MockMultipartFile("file", "video.mp4", "multipart/form-data", inputFile);

        String result = videoBusiness.uploadVideo(file, videoCreationFacade);
        assertThat(result.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")).isTrue();
    }

    @Test
    public void uploadVideo_should_raise_error_if_title_size_incorrect() throws IOException, UserNotFoundException {
        FileInputStream inputFile = new FileInputStream(files.getFile().getAbsolutePath() + File.separator +"video.mp4");
        MockMultipartFile file = new MockMultipartFile("file", "video.mp4", "multipart/form-data", inputFile);
        videoCreationFacade.setTitle("");
        try{
            videoBusiness.uploadVideo(file, videoCreationFacade);
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(EnumError.VIDEO_TITLE_SIZE);
        }
    }

    @Test
    public void uploadVideo_should_save_file_on_server() throws IOException, UserNotFoundException, VideoException, ThumbnailException, PathException {
        FileInputStream inputFile = new FileInputStream(files.getFile().getAbsolutePath() + File.separator +"video.mp4");
        MockMultipartFile file = new MockMultipartFile("file", "video.mp4", "multipart/form-data", inputFile);

        File videoLibraryFolder = new File(pathBusiness.getVideoLibraryPath() + File.separator);
        assertThat(videoLibraryFolder.list().length).isEqualTo(0);

        String key = videoBusiness.uploadVideo(file, videoCreationFacade);
        File fileFound = new File(pathBusiness.getVideoLibraryPath() + File.separator + key);
        assertThat(videoLibraryFolder.list().length).isEqualTo(1);
        assertThat(fileFound.exists()).isTrue();
    }

    @Test
    public void uploadVideo_should_save_default_thumbnail_on_server() throws IOException, UserNotFoundException, VideoException, ThumbnailException, PathException {
        FileInputStream inputFile = new FileInputStream(files.getFile().getAbsolutePath() + File.separator +"video.mp4");
        MockMultipartFile file = new MockMultipartFile("file", "video.mp4", "multipart/form-data", inputFile);

        File videoLibraryFolder = new File(pathBusiness.getVideoThumbnailsLibraryPath() + File.separator);
        assertThat(videoLibraryFolder.list().length).isEqualTo(0);

        String key = videoBusiness.uploadVideo(file, videoCreationFacade);
        String videoPath = pathBusiness.getVideoThumbnailsLibraryPath() + File.separator + key + thumbnailBusiness.VIDEO_DEFAULT_THUMBNAIL_DEFAULT_IMAGE_SUFFIX;
        File fileFound = new File(videoPath);
        assertThat(videoLibraryFolder.list().length).isEqualTo(1);
        assertThat(fileFound.exists()).isTrue();
        BufferedImage thumbnail = ImageIO.read(new File(videoPath));
        assertThat(thumbnail.getWidth()).isEqualTo(1280);
        assertThat(thumbnail.getHeight()).isEqualTo(720);
    }

    @Test
    public void uploadVideo_should_persist_video_if_all_tests_passed_successfully() throws IOException, UserNotFoundException, VideoException, ThumbnailException {
        assertThat(videoRepository.findAll().size()).isEqualTo(6);
        FileInputStream inputFile = new FileInputStream(files.getFile().getAbsolutePath() + File.separator +"video.mp4");
        MockMultipartFile file = new MockMultipartFile("file", "video.mp4", "multipart/form-data", inputFile);

        String key = videoBusiness.uploadVideo(file, videoCreationFacade);
        Video videoPersist = videoRepository.findByKey(key);
        assertThat(videoPersist).isNotNull();
        assertThat(videoPersist.getTitle()).isEqualTo(videoCreationFacade.getTitle());
        assertThat(videoPersist.getDescription()).isEqualTo(videoCreationFacade.getDescription());
        assertThat(videoPersist.getIspublic()).isEqualTo(videoCreationFacade.isPublic());
        assertThat(videoPersist.getIspubliclink()).isEqualTo(videoCreationFacade.isPublicLink());
        assertThat(videoPersist.getViews().size()).isEqualTo(0);
        assertThat(videoPersist.getCreationdate()).isEqualToIgnoringMinutes(Timestamp.valueOf(LocalDateTime.now()));
        assertThat(videoRepository.findAll().size()).isEqualTo(7);
        assertThat(videoPersist.getDuration()).isBetween((long) 4200, (long) 4300);
    }

    @Test
    public void uploadVideo_should_persist_video_and_if_public_is_true_public_link_should_be_true() throws IOException, UserNotFoundException, VideoException, ThumbnailException {
        assertThat(videoRepository.findAll().size()).isEqualTo(6);
        FileInputStream inputFile = new FileInputStream(files.getFile().getAbsolutePath() + File.separator +"video.mp4");
        MockMultipartFile file = new MockMultipartFile("file", "video.mp4", "multipart/form-data", inputFile);
        videoCreationFacade = videoCreationFacade.setPublic(true).setPublicLink(false);

        String key = videoBusiness.uploadVideo(file, videoCreationFacade);
        Video videoPersist = videoRepository.findByKey(key);
        assertThat(videoPersist.getIspublic()).isEqualTo(videoCreationFacade.isPublic());
        assertThat(videoPersist.getIspubliclink()).isEqualTo(!videoCreationFacade.isPublicLink());
    }

    @Test
    public void uploadVideo_should_persist_video_and_if_public_is_false_public_link_could_be_true() throws IOException, UserNotFoundException, VideoException, ThumbnailException {
        assertThat(videoRepository.findAll().size()).isEqualTo(6);
        FileInputStream inputFile = new FileInputStream(files.getFile().getAbsolutePath() + File.separator +"video.mp4");
        MockMultipartFile file = new MockMultipartFile("file", "video.mp4", "multipart/form-data", inputFile);
        videoCreationFacade = videoCreationFacade.setPublic(false).setPublicLink(true);

        String key = videoBusiness.uploadVideo(file, videoCreationFacade);
        Video videoPersist = videoRepository.findByKey(key);
        assertThat(videoPersist.getIspublic()).isEqualTo(videoCreationFacade.isPublic());
        assertThat(videoPersist.getIspubliclink()).isEqualTo(videoCreationFacade.isPublicLink());
    }

    @Test
    public void uploadVideo_should_persist_video_and_if_public_is_false_public_link_could_be_false() throws IOException, UserNotFoundException, VideoException, ThumbnailException {
        assertThat(videoRepository.findAll().size()).isEqualTo(6);
        FileInputStream inputFile = new FileInputStream(files.getFile().getAbsolutePath() + File.separator +"video.mp4");
        MockMultipartFile file = new MockMultipartFile("file", "video.mp4", "multipart/form-data", inputFile);
        videoCreationFacade = videoCreationFacade.setPublic(false).setPublicLink(false);

        String key = videoBusiness.uploadVideo(file, videoCreationFacade);
        Video videoPersist = videoRepository.findByKey(key);
        assertThat(videoPersist.getIspublic()).isEqualTo(videoCreationFacade.isPublic());
        assertThat(videoPersist.getIspubliclink()).isEqualTo(videoCreationFacade.isPublicLink());
    }
    //endregion

    //region get
    @Test
    public void getVideo_should_return_video_data() throws VideoException, UserNotFoundException {
        Video videoInDb = videoRepository.findByKey("a");
        VideoDataFacade videoFound = videoBusiness.get("a");
        assertThat(videoFound.getKey()).isEqualTo("a");
        assertThat(videoFound.getTitle()).isEqualTo(videoInDb.getTitle());
        assertThat(videoFound.getDescription()).isEqualTo(videoInDb.getDescription());
        assertThat(videoFound.getCreationDate()).isEqualTo(videoInDb.getCreationdate());
        assertThat(videoFound.getOwnerId()).isEqualTo(videoInDb.getOwner().getId());
        assertThat(videoFound.getOwnerPseudo()).isEqualTo(videoInDb.getOwner().getPseudo());
        assertThat(videoFound.getOwnerEmail()).isEqualTo(videoInDb.getOwner().getEmail());
        assertThat(videoFound.isPublic()).isTrue();
        assertThat(videoFound.isPublicLink()).isTrue();
        assertThat(videoFound.getViews()).isEqualTo(2);
        assertThat(videoFound.getDuration()).isEqualTo(25000);
    }

    @Test
    public void getVideo_should_return_an_error_if_video_not_found() {
        try{
            videoBusiness.get("KEYWHICHNOTEXIST");
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(videoBusiness.VIDEO_NOT_FOUND);
        }
    }

    @Test
    public void getVideo_should_return_an_error_if_video_is_private() {
        try{
            videoBusiness.get("f");
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(videoBusiness.VIDEO_NOT_AVAILABLE);
        }
    }

    @Test
    public void getVideo_should_return_video_if_private_but_asked_by_owner() throws VideoException, UserNotFoundException {
        VideoDataFacade videoFound = videoBusiness.get("d");
        assertThat(videoFound.getKey()).isEqualTo("d");
        assertThat(videoFound.isPublic()).isFalse();
        assertThat(videoFound.isPublicLink()).isFalse();
    }

    @Test
    public void getVideo_should_return_raised_an_error_if_video_private_and_user_not_connected() throws VideoException, UserNotFoundException {
        SecurityContextHolder.getContext().setAuthentication(null);
        try{
            videoBusiness.get("f");
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(videoBusiness.VIDEO_NOT_AVAILABLE);
        }
    }

    @Test
    public void getVideo_should_return_video_if_private_but_have_public_link() throws VideoException, UserNotFoundException {
        VideoDataFacade videoFound = videoBusiness.get("e");
        assertThat(videoFound.getKey()).isEqualTo("e");
        assertThat(videoFound.isPublic()).isFalse();
        assertThat(videoFound.isPublicLink()).isTrue();
    }
    //endregion

    //region update
    @Test
    public void updateVideo_should_update_video() throws VideoException, UserNotFoundException {
        String key = videoBusiness.update(new VideoDataFacade()
                .setKey("a")
                .setTitle("z")
                .setDescription("desc of z")
                .setPublic(false)
                .setPublicLink(false));
        Video videoUpdated = videoRepository.findByKey("a");
        assertThat(key).isEqualTo("a");
        assertThat(videoUpdated.getTitle()).isEqualTo("z");
        assertThat(videoUpdated.getDescription()).isEqualTo("desc of z");
        assertThat(videoUpdated.getIspublic()).isFalse();
        assertThat(videoUpdated.getIspubliclink()).isFalse();
    }

    @Test
    public void updateVideo_should_update_video_as_private_then_publicLink_should_be_set_to_false() throws VideoException, UserNotFoundException {
        String key = videoBusiness.update(new VideoDataFacade()
                .setKey("a")
                .setTitle("z")
                .setDescription("desc of z")
                .setPublic(false)
                .setPublicLink(true));
        Video videoUpdated = videoRepository.findByKey("a");
        assertThat(key).isEqualTo("a");
        assertThat(videoUpdated.getTitle()).isEqualTo("z");
        assertThat(videoUpdated.getDescription()).isEqualTo("desc of z");
        assertThat(videoUpdated.getIspublic()).isFalse();
        assertThat(videoUpdated.getIspubliclink()).isFalse();
    }

    @Test
    public void updateVideo_should_raise_an_error_when_video_not_found() throws VideoException, UserNotFoundException {
        try{
            videoBusiness.update(new VideoDataFacade()
                    .setKey("DONTEXISTKEY"));
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(videoBusiness.VIDEO_NOT_FOUND);
        }
    }

    @Test
    public void updateVideo_should_raise_an_error_when_user_not_connected() throws VideoException, UserNotFoundException {
        SecurityContextHolder.getContext().setAuthentication(null);
        try{
            videoBusiness.update(new VideoDataFacade()
                    .setKey("a"));
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(videoBusiness.VIDEO_UPDATE_USER_IS_NOT_VIDEO_OWNER);
        }
    }

    @Test
    public void updateVideo_should_raise_an_error_when_user_is_not_video_owner() throws VideoException, UserNotFoundException {
        try{
            videoBusiness.update(new VideoDataFacade()
                    .setKey("f"));
            fail("Should throw exception");
        }catch (PublicException e){
            assertThat(e.getMessages()).contains(videoBusiness.VIDEO_UPDATE_USER_IS_NOT_VIDEO_OWNER);
        }
    }
    //endregion
}

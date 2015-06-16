package com.liztube.business;

import com.liztube.exception.PathException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

/**
 * Business to get paths
 */
@Component
public class PathBusiness {

    public static final String VIDEO_LIBRARY_PATH_ERROR = "An error occured when trying to get video library path";
    public static final String VIDEO_THUMBNAILS_LIBRARY_PATH_ERROR = "An error occured when trying to get video thumbnails library path";

    @Autowired
    Environment environment;

    public ClassPathResource videoLibrary = new ClassPathResource("VideoLibrary/");
    public ClassPathResource videoThumbnailsLibrary = new ClassPathResource("VideoThumbnailsLibrary/");

    /**
     * Get VideoLibrary path : where are stored all videos
     * @return
     */
    public String getVideoLibraryPath() throws PathException {
        try {
            String path = environment.getProperty("absolute.path.video.library");
            if(path.isEmpty()){
                return videoLibrary.getFile().getAbsolutePath();
            }else{
                return path;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new PathException("Path exception", Arrays.asList(VIDEO_LIBRARY_PATH_ERROR));
        }
    }

    /**
     * Get VideoThumbnailsLibrary path : where are strored all videos thumbnails
     * @return
     */
    public String getVideoThumbnailsLibraryPath() throws PathException {
        try {
            String path = environment.getProperty("absolute.path.video.thumbnails.library");
            if(path.isEmpty()){
                return videoThumbnailsLibrary.getFile().getAbsolutePath();
            }else{
                return path;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new PathException("Path exception", Arrays.asList(VIDEO_THUMBNAILS_LIBRARY_PATH_ERROR));
        }
    }
}

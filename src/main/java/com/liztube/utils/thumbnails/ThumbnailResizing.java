package com.liztube.utils.thumbnails;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Image resizing class
 */
public class ThumbnailResizing {

    private static final double WIDTH_HEIGHT_RAPPORT = 1.7778;// 16/9
    private static final double HEIGHT_WIDTH_RAPPORT = 0.5625;// 9/16
    private static final int DEFAULT_THUMBNAIL_WIDTH = 1280;// 16
    private static final int DEFAULT_THUMBNAIL_HEIGHT = 720;// 9

    /**
     * Adapt thumbnail size :
     * 1) Crop
     * 2) Resize to default thumbnail size
     * @param originalImage
     * @return
     */
    public static BufferedImage adaptThumbnailSize(BufferedImage originalImage){
        return resizeImageWithHint(cropThumbnail(originalImage), 0, 0);
    }

    /**
     * Crop image according to defined rapport
     * @param originalImage
     * @return
     */
    private static BufferedImage cropThumbnail(BufferedImage originalImage){
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        long computedWidth = Math.round(originalHeight * WIDTH_HEIGHT_RAPPORT);
        if(computedWidth > originalWidth){
            long computedHeight = Math.round(originalWidth * HEIGHT_WIDTH_RAPPORT);
            long heightDecal = (originalHeight - computedHeight) / 2;
            return originalImage.getSubimage(0, (int)heightDecal, originalWidth, (int)computedHeight);
        }else{
            long widthDecal = (originalWidth - computedWidth) / 2;
            return originalImage.getSubimage((int)widthDecal, 0, (int)computedWidth, originalHeight);
        }
    }

    /**
     * Resize image by increasing quality
     * Manage the image dimension :
     * - if width = 0 and height = 0 : use default dimension
     * - if one of them is 0 : adapt this one to have a 16/9 dimensions
     * - if two of them are different of 0 : check 16/9 dimensions ...
     *  - if don't have 16/9 dimensions : adapt height
     *  - else keep asked values
     * @param originalImage
     * @return
     */
    public static BufferedImage resizeImageWithHint(BufferedImage originalImage, int width, int height){
        //If no defined : get default values
        if(width == 0 && height == 0){
            width = DEFAULT_THUMBNAIL_WIDTH;
            height = DEFAULT_THUMBNAIL_HEIGHT;
        }
        //If width not defined : define width for 16/9 dimensions
        if(width == 0 && height != 0){
            width = (int) Math.round(height * WIDTH_HEIGHT_RAPPORT);
        }
        //If height not defined : define height for 16/9 dimensions
        //OR
        //if height and width defined and image don't have 16/9 dimensions : we adapt the height
        if ((width != 0 && height == 0) || (width != 0 && height != 0 && (float)width/(float)height != (float)16/(float)9)){
            height = (int)Math.round(width * HEIGHT_WIDTH_RAPPORT);
        }

        int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        g.setComposite(AlphaComposite.Src);

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        return resizedImage;
    }

}

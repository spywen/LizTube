package com.liztube.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides helper methods for working with ffmpeg
 */
public class FfMpegUtils
{
    private String ffmpegCommand;
    public FfMpegUtils(String ffmpegCommand)
    {
        this.ffmpegCommand = ffmpegCommand;
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Helper method that executes ffmpeg against the specified file, parses the
     * results, and returns a map that contains key metrics about the file
     * @param filename      The name of the file to query
     * @return              A map that contains interesting fields
     */
    protected Map<String,String> executeInquiry( String filename )
    {
        Map<String,String> fieldMap = new HashMap<String,String>();

        try
        {
            ProcessBuilder pb = new ProcessBuilder(ffmpegCommand,
                    "-i",
                    filename);

            pb.directory(new File("/tmp"));
            Process p = pb.start();

            BufferedReader input = new BufferedReader( new InputStreamReader( p.getInputStream() ) );
            BufferedReader error = new BufferedReader( new InputStreamReader( p.getErrorStream() ) );
            p.waitFor();

            // Parse the input stream
            String line = input.readLine();
            while( line != null )
            {
                line = input.readLine();
            }

            // Parse the error stream
            line = error.readLine();
            while( line != null )
            {
                // Handle the line
                if( line.startsWith( "FFmpeg version" ) )
                {
                    // Handle the version line:
                    //    FFmpeg version 0.6.2-4:0.6.2-1ubuntu1, Copyright (c) 2000-2010 the Libav developers
                    String version = line.substring( 15, line.indexOf( ", Copyright", 16  ) );
                    fieldMap.put( "version", version );
                }
                else if( line.indexOf( "Duration:" ) != -1 )
                {
                    // Handle Duration line:
                    //    Duration: 00:42:53.59, start: 0.000000, bitrate: 1136 kb/s
                    String duration = line.substring( line.indexOf( "Duration: " ) + 10, line.indexOf( ", start:" ) );
                    fieldMap.put( "duration", duration );

                    String bitrate = line.substring( line.indexOf( "bitrate: " ) + 9 );
                    fieldMap.put( "bitrate", bitrate );
                }

                // Read the next line
                line = error.readLine();
            }
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        /*for( String field : fieldMap.keySet() )
        {
            System.out.println( "\t" + field + " = " + fieldMap.get( field ) );
        }*/

        return fieldMap;
    }

    /**
     * Returns a String containing the duration of the specified file
     * @param filepath      The name of the file for which to retrieve the duration
     * @return              A String that contains the duration of the file
     */
    public String getDuration( String filepath )
    {
        Map<String,String> fieldMap = executeInquiry( filepath );
        if( fieldMap.containsKey( "duration" ) )
        {
            return fieldMap.get( "duration" );
        }
        return "00:00:00.00";
    }

    /**
     * Get video duration in milliseconds
     * @param filepath
     * @return
     */
    public int getDurationInMilliSeconds(String filepath){
        String duration = getDuration(filepath).trim();
        int hours = Integer.parseInt(duration.substring(0, 2).trim());
        int minutes = Integer.parseInt(duration.substring( 3, 5 ).trim());
        int seconds = Integer.parseInt(duration.substring( 6, 8 ).trim());
        int milliseconds = Integer.parseInt(duration.substring(9, 11).trim());
        return hours * 3600000 + minutes * 60000 + seconds * 1000 + milliseconds * 10;
    }


    /**
     * Generates thumbnails for the specified video file to the specified directory
     * @param filename      The name of the file from which to create the thumbnails
     * @param dir           The directory to which to create the thumbnails
     * @param width         The width of the thumbnail
     * @param height        The height of the thumbnail
     * @param count         The number of thumbnails to generate
     */
    /*
    public void generateThumbnails( String filename, File dir, int width, int height, int count )
    {
        // The following example shows how to generate thumbnails at seconds 4, 8, 12, and 16
        // ffmpeg  -itsoffset -4  -i test.avi -vcodec mjpeg -vframes 1 -an -f rawvideo -s 320x240 test.jpg
        // ffmpeg  -itsoffset -8  -i test.avi -vcodec mjpeg -vframes 1 -an -f rawvideo -s 320x240 test.jpg
        // ffmpeg  -itsoffset -12  -i test.avi -vcodec mjpeg -vframes 1 -an -f rawvideo -s 320x240 test.jpg
        // ffmpeg  -itsoffset -16  -i test.avi -vcodec mjpeg -vframes 1 -an -f rawvideo -s 320x240 test.jpg

        // Have to compute seconds, format is: 00:42:53.59
        String duration = getDuration( filename );
        int hours = Integer.parseInt( duration.substring( 0, 2 ) );
        int minutes = Integer.parseInt( duration.substring( 3, 5 ) );
        int seconds = Integer.parseInt( duration.substring( 6, 8 ) );
        int totalSeconds = hours * 3600 + minutes * 60 + seconds;
        System.out.println( "Total Seconds: " + totalSeconds );

        // Create the thumbnails
        String shortFilename = filename;
        if( filename.indexOf( File.separator ) != -1 )
        {
            // Strip the path
            shortFilename = filename.substring(filename.lastIndexOf(File.separator) + 1);
        }
        if( shortFilename.indexOf( "." ) != -1 )
        {
            // Strip extension
            shortFilename = shortFilename.substring( 0, shortFilename.lastIndexOf( "." ) );
        }

        // Define a shift in seconds
        int shift = 4;

        // The step is the number of seconds to step between thumbnails
        int step = totalSeconds / count;
        for( int index = 0; index < count; index++ )
        {
            // Build the command
            StringBuilder sb = new StringBuilder();
            sb.append( ffmpegCommand );
            sb.append( " -itsoffset -" );
            sb.append( Integer.toString( shift + ( index * step ) ) );
            sb.append( " -i " );
            sb.append( filename );
            sb.append( " -vcodec mjpeg -vframes 1 -an -f rawvideo -s " );
            sb.append( Integer.toString( width ) );
            sb.append( "x" );
            sb.append( Integer.toString( height ) );
            sb.append( " " );
            sb.append( dir.getAbsolutePath() );
            sb.append( File.separator );
            sb.append( shortFilename );
            sb.append( "-" );
            sb.append( Integer.toString( index + 1 ) );
            sb.append( ".jpg" );
            System.out.println( "Thumbnail command: " + sb );

            try
            {
                // Execute the command
                Process p = Runtime.getRuntime().exec( sb.toString() );

                // Detach from the process
                p.getOutputStream().close();
                consumeStream( p.getInputStream() );
                consumeStream( p.getErrorStream() );
            }
            catch( Exception e )
            {
                e.printStackTrace();
            }
        }
    }*/

    /**
     * Helper method that reads a stream until it is complete and returns its contents
     * as a String
     * @param is        The input stream to read from
     * @return          The response of the input stream as a String
     */
    /*
    protected String consumeStream( InputStream is )
    {
        StringBuilder sb = new StringBuilder();
        try
        {
            BufferedReader br = new BufferedReader( new InputStreamReader( is ) );
            String line = br.readLine();
            while( line != null )
            {
                sb.append( line );
                line = br.readLine();
            }
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        return sb.toString();
    }
    */
}


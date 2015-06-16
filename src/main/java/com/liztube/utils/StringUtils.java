package com.liztube.utils;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * String Utils
 */
@Component
public class StringUtils {
    /**
     * Decode string url encoded
     * See : http://www.url-encode-decode.com/
     * @param str
     * @return
     */
    public static String UrlDecoder(String str){
        try {
            return URLDecoder.decode(str, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
}

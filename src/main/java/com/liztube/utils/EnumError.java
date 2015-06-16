package com.liztube.utils;

/**
 * Created by laurent on 15/02/15.
 */
public class EnumError {
    //SIGNIN
    public static final String USER_FIRSTNAME_SIZE = "#1001";
    public static final String USER_LASTNAME_SIZE = "#1002";
    public static final String USER_PSEUDO_SIZE = "#1003";
    public static final String USER_BIRTHDAY_NOTNULL = "#1004";
    public static final String USER_BIRTHDAY_PAST_DATE = "#1005";
    public static final String USER_EMAIL_FORMAT = "#1006";
    public static final String USER_EMAIL_SIZE = "#1007";
    public static final String USER_REGISTER_NOTNULL = "#1008";
    public static final String USER_REGISTER_PAST_DATE = "#1009";
    public static final String USER_MODIFICATION_NOTNULL = "#1010";
    public static final String USER_ISFEMALE_NOTNULL = "#1011";
    public static final String USER_ISACTIVE_NOTNULL = "#1012";
    public static final String USER_EMAIL_OR_PSEUDO_ALREADY_USED = "#1013";
    public static final String USER_PASSWORD_FORMAT = "#1014";
    public static final String USER_OLD_PASSWORD_INVALID = "#1015";

    //VIDEO
    public static final String VIDEO_TITLE_SIZE            = "#1051";
    public static final String VIDEO_DESCRIPTION_SIZE      = "#1052";
    public static final String VIDEO_UPLOAD_SAVE_FILE_ON_SERVER     = "#1053";

    //USER DELETE ACCOUNT
    public static final String DELETE_ACCOUNT_BAD_PASSWORD = "#1080";
}

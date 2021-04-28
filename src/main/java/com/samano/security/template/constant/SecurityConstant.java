package com.samano.security.template.constant;

/*
This class provide all the constants for Security Configuration and JWT
 */
public class SecurityConstant {
    public static final String JWT_SECRET = "3234EE*F#Z5**#$%DJK*ASD324Z#$%$#fgdff**ee*d##$%34";
    public static final long EXPIRATION_TIME = 432_000_000; // 5 DAYS IN MILLISECONDS
    public static final String TOKEN_HEADER = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String LLC = "API-REST SECURITY TEMPLATE";
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = {"/auth/login","/auth/register"};
    public static final String LOGIN_URL = "/auth/login";

}

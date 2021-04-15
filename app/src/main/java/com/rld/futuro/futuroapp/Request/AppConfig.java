package com.rld.futuro.futuroapp.Request;

public class AppConfig {

    private static final String HOST = "192.168.1.64";
    private static final String PORT = "3000";
    private static final String URL_SERVER = "http://" + HOST + ":" + PORT + "/";

    public static final String GET_SECTIONS = URL_SERVER + "api/v1/section/appSetup";

    public static final int MUNICIPALITIES_SIZE = 125;
    public static final int LOCAL_DISTRICTS_SIZE = 20;
    public static final int SECTIONS_SIZE = 3602;

    public static boolean isInternetConnected;
}

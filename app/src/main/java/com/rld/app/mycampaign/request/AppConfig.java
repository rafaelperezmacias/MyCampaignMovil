package com.rld.app.mycampaign.request;

public class AppConfig {

    private static final String HOST = "192.168.1.64";
    private static final String PORT = "3000";
    private static final String URL_SERVER = "http://" + HOST + ":" + PORT + "/";

    public static final String GET_SECTIONS = URL_SERVER + "api/v1/section/appSetup/";
    public static final String INSERT_VOLUNTEER = URL_SERVER + "api/v1/volunteer/";

}

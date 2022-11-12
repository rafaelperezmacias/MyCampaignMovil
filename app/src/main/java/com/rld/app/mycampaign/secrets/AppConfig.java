package com.rld.app.mycampaign.secrets;

public class AppConfig {

    private static final String HOST = "192.168.100.6";
    private static final String PORT = "80";
    private static final String URL_SERVER = "http://" + HOST + ":" + PORT + "/";

    public static final String GET_SECTIONS = URL_SERVER + "/mycampaignweb/server.php/api/sections";
    public static final String INSERT_VOLUNTEER = URL_SERVER + "api/v1/volunteer/";

}

# Uso

El proyecto necesita del archivo AppConfig.java para trabajar ubicado en _secrets/AppConfig.java_

## Ejemplo de Archivo AppConfig

```java
public class AppConfig {

    private static final String HOST = "192.168.100.6";
    private static final String PORT = "80";
    private static final String URL_SERVER = "http://" + HOST + ":" + PORT + "/";

    public static final String LOGIN = URL_SERVER + "api/login";
    public static final String GET_SECTIONS = URL_SERVER + "api/sections";
    public static final String INSERT_VOLUNTEER = URL_SERVER + "api/volunteer";

}
```

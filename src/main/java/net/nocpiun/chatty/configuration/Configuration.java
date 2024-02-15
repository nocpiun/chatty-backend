package net.nocpiun.chatty.configuration;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;

public class Configuration {
    private static Configuration instance;
    private static String configPath = System.getProperty("user.dir") +"/config.yml";
    private Yaml yaml;
    private LinkedHashMap config;

    private Configuration() throws Exception {
        yaml = new Yaml();

        InputStream stream = new FileInputStream(configPath);
        config = yaml.load(stream);
    }

    public String getSQLServer() {
        return (String) config.get("sqlServer");
    }

    public String getSQLUserName() {
        return (String) config.get("sqlUserName");
    }

    public String getSQLPassword() {
        return (String) config.get("sqlPassword");
    }

    public int getServerPort() {
        return (int) config.get("serverPort");
    }

    public String getSalt() {
        return (String) config.get("salt");
    }

    public static Configuration get() throws Exception {
        if(instance == null) instance = new Configuration();
        return instance;
    }
}

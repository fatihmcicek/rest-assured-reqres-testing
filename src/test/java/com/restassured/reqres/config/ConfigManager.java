package com.restassured.reqres.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    private static final Properties properties = new Properties();
    private static ConfigManager instance;

    private ConfigManager() {
        try {
            FileInputStream file = new FileInputStream("src/test/resources/config.properties");
            properties.load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
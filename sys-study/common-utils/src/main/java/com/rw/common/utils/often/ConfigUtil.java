/**
 *
 * Copyright (c) 2015 Chutong Technologies All rights reserved.
 *
 */
package com.rw.common.utils.often;

import java.io.InputStream;
import java.util.Properties;

/**
 * 配置服务
 * @author Mr. Li
 * @version 0.0.1
 * @since
 */
public class ConfigUtil {
    private static Properties config = new Properties();

    public static Properties getConfig() {
        if (0 == config.size()) {
            synchronized (ConfigUtil.class) {
                if (0 == config.size()) {
                    InputStream resourceAsStream = ConfigUtil.class.getResourceAsStream("/config.properties");
                    try {
                        config.load(resourceAsStream);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return config;
    }

    public static String getConfigValue(String key) {
        getConfig();
        String env = config.getProperty("env");
        return config.getProperty(env + "." + key);
    }
    
    public static String getEnv() {
        getConfig();
        return config.getProperty("env");
    }
    
    public static String getProperty(String key){
        return config.getProperty(key);
    }
    
    public static void main(String[] args) {
    	
     System.out.println(getEnv());
		
	}
    
}

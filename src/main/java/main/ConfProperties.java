package main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfProperties {
    static Properties PROPERTIES = new Properties();

    static {
        try (InputStream inputStream = ConfProperties.class.getClassLoader().getResourceAsStream("conf.properties")) {
            if (inputStream != null) {
                PROPERTIES.load(inputStream);
            } else {
                throw new RuntimeException("Properties file not found");
            }
        } catch (IOException e) {
            System.out.println("Error loading properties file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

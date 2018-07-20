package org.sqltomongo.mongo;

import java.io.IOException;
import java.util.Properties;

public class MongoProperties {

    public static final String HOST_KEY = "mongo.host";
    public static final String COLLECTION_KEY = "mongo.collection";
    public static final String PORT_KEY = "mongo.port";
    public static final String PROPERTIES_FILE_NAME = "config.properties";

    Properties properties = new Properties();

    public String getHost(){

        return properties.getProperty(HOST_KEY);

    }

    public String getCollection(){

        return properties.getProperty(COLLECTION_KEY);
    }

    public int getPort(){

        return Integer.parseInt(properties.getProperty(PORT_KEY));

    }

    public MongoProperties() {

        try {
            properties.load(ClassLoader.getSystemResourceAsStream(PROPERTIES_FILE_NAME));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}

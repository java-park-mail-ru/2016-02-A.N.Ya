package main.cfg;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private static final String SERVER_CONFIG_FILE = "configuration/cfg/server.properties";

    @SuppressWarnings("ConstantNamingConvention")
    private static final Logger logger = LogManager.getLogger(Config.class);

    private static int port;

    public static void loadConfig() {
        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream(SERVER_CONFIG_FILE)) {
            properties.load(fileInputStream);
            fileInputStream.close();
            port = Integer.valueOf(properties.getProperty("port"));
            /* host = properties.getProperty("host"); */
        } catch (FileNotFoundException e) {
            logger.error("File not found: " + SERVER_CONFIG_FILE);
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static int getPort() {
        return port;
    }
}

package project;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

class ReadProperties {

    ArrayList<String> getPropertyValues() {

        final String randomKey = "1KcyOOtF2E";
        final String propFileName= "MemeStockMarket.properties";

        InputStream inputStream = null;
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(randomKey); //random key gen from randomkeygen.com

        ArrayList<String> result = new ArrayList<>();
        try {
            Properties prop = new EncryptableProperties(encryptor);
            //  Properties prop = new Properties();

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            // get the property value and print it out
            String serverUserName = prop.getProperty("serverUsername");
            result.add(serverUserName);
            String serverPassword = prop.getProperty("serverPassword");
            result.add(serverPassword);
            String databaseName = prop.getProperty("databaseName");
            result.add(databaseName);
            String serverName = prop.getProperty("serverName");

            result.add(serverName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}

package hr.fer.zemris.optjava.rng;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

public class RNG {
    private static IRNGProvider rngProvider;
    private static final String providerConfiguration = "config.properties";

    static {
        Properties properties = new Properties();
        InputStream inputStream = RNG.class.getResourceAsStream(providerConfiguration);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
        }

        String className = properties.getProperty("rng-provider");
        try {
            inputStream.close();
        } catch (IOException e) {
        }

        Class<?> rngProviderClass = null;
        try {
            rngProviderClass = RNG.class.getClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
        }

        try {
            rngProvider = (IRNGProvider) rngProviderClass.newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
    }

    public static IRNG getRNG() {
        return rngProvider.getRNG();
    }
}

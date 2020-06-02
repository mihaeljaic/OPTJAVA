package hr.fer.zemris.generic.ga.evaluator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Evaluator {
    private static IEvaluatorProvider evaluatorProvider;
    private static final String providerConfiguration = "config.properties";

    static {
        Properties properties = new Properties();
        InputStream inputStream = Evaluator.class.getResourceAsStream(providerConfiguration);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
        }

        String className = properties.getProperty("evaluator-provider");
        try {
            inputStream.close();
        } catch (IOException e) {
        }

        Class<?> evaluatorProviderClass = null;
        try {
            evaluatorProviderClass = Evaluator.class.getClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
        }

        try {
            evaluatorProvider = (IEvaluatorProvider) evaluatorProviderClass.newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
    }

    public static EvaluatorImplementation getEvaluator() throws IOException {
        return evaluatorProvider.getEvaluator();
    }
}

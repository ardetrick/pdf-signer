package com.ardetrick.pdfsigner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PropertiesLoader {

    private static final String EXAMPLE_PROPERTIES_PATH = "files/example/example.properties";

    public static Properties loadCustomProperties(String pathToConfigProperties) {
        try (var input = new FileInputStream(pathToConfigProperties)) {
            return loadProperties(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Properties loadingExampleProperties() {
        // Example properties files are packaged in the jbang jar to support invoking the script via GitHub link.
        try (InputStream is = PropertiesLoader.class.getClassLoader().getResourceAsStream(EXAMPLE_PROPERTIES_PATH)) {
            return loadProperties(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Properties loadProperties(InputStream input) throws IOException {
        var properties = new java.util.Properties();

        properties.load(input);

        return new Properties(DocumentSigner.Properties.fromProperties(properties),
                              ScanEmulator.Properties.fromProperties(properties),
                              PdfLoader.Properties.fromProperties(properties));
    }

    public record Properties(DocumentSigner.Properties documentSignerProperties,
                             ScanEmulator.Properties scanEmulatorProperties,
                             PdfLoader.Properties pdfLoaderProperties) {}

}

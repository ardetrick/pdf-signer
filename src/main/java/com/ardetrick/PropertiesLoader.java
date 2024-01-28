package com.ardetrick;

import java.io.FileInputStream;
import java.io.IOException;

public class PropertiesLoader {

    public static Properties loadProperties() {
        // TODO make this overridable by CLI arg
        var propertiesPath = "config.properties";

        var properties = new java.util.Properties();

        try (var input = new FileInputStream(propertiesPath)) {
            properties.load(input);

            return new Properties(DocumentSigner.Properties.fromProperties(properties),
                                  ScanEmulator.Properties.fromProperties(properties),
                                  PdfLoader.Properties.fromProperties(properties));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public record Properties(DocumentSigner.Properties documentSignerProperties,
                             ScanEmulator.Properties scanEmulatorProperties,
                             PdfLoader.Properties pdfLoaderProperties) {}

}

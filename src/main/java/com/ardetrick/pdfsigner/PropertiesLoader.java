package com.ardetrick.pdfsigner;

import java.io.FileInputStream;
import java.io.IOException;

public class PropertiesLoader {

    public static Properties loadProperties(String pathToConfigProperties) {
        var properties = new java.util.Properties();

        try (var input = new FileInputStream(pathToConfigProperties)) {
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

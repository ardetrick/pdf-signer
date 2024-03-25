package com.ardetrick.pdfsigner;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

public class PdfLoader {

    public static PDDocument loadPdf(Properties properties) {
        var fileInputName = properties.fileInputName();

        return getPdfFromFilePath(fileInputName);
    }

    private static PDDocument getPdfFromFilePath(String fileInputName) {
        try {
            return Loader.loadPDF(new File(fileInputName));
        } catch (NoSuchFileException e) {
            // Fall back on looking in the jar - applicable only for example usage.
            var inputStream = PdfLoader.class.getClassLoader().getResourceAsStream(fileInputName);
            if (inputStream == null) {
                throw new RuntimeException(e);
            }
            try {
                return Loader.loadPDF(inputStream.readAllBytes());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public record Properties(String fileInputName) {

        public static Properties fromProperties(java.util.Properties properties) {
            return new Properties(properties.getProperty("pdf-loader.file-input-name"));
        }

    }
}

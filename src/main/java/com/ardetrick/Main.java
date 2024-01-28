package com.ardetrick;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        var config = PropertiesLoader.loadProperties();

        try (var document = PdfLoader.loadPdf(config.pdfLoaderProperties())) {
            DocumentSigner.signAndDate(document,
                                       config.documentSignerProperties());

            ScanEmulator.emulateScanAndWriteFile(document, config.scanEmulatorProperties());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

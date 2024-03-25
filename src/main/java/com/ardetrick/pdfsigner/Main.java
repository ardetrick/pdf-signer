///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 21+
//SOURCES DocumentSigner.java
//SOURCES PdfLoader.java
//SOURCES PropertiesLoader.java
//SOURCES RandomUtils.java
//SOURCES ScanEmulator.java,
//DEPS org.apache.pdfbox:pdfbox:3.0.1

// Only useful for the example case, does not have use otherwise.
//FILES files/example/example.properties=../../../../../../files/example/example.properties
//FILES files/example/document/example-document.pdf=../../../../../../files/example/document/example-document.pdf
//FILES files/example/signatures/signature-1.png=../../../../../../files/example/signatures/signature-1.png
//FILES files/example/signatures/signature-2.png=../../../../../../files/example/signatures/signature-2.png

package com.ardetrick.pdfsigner;

import java.io.IOException;

import static java.util.logging.Level.OFF;
import static java.util.logging.Logger.getLogger;

public class Main {

    public static void main(String[] args) {
        disableNoisyAndSeeminglyBenignLogs();

        var config = loadConfigurationProperties(args);

        run(config);
    }

    private static void run(PropertiesLoader.Properties config) {
        try (var document = PdfLoader.loadPdf(config.pdfLoaderProperties())) {
            DocumentSigner.signAndDate(document, config.documentSignerProperties());

            ScanEmulator.emulateScanAndWriteFile(document, config.scanEmulatorProperties());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static PropertiesLoader.Properties loadConfigurationProperties(String[] args) {
        return args.length > 0 ?
                PropertiesLoader.loadCustomProperties(args[0]) :
                PropertiesLoader.loadingExampleProperties();
    }

    private static void disableNoisyAndSeeminglyBenignLogs() {
        getLogger("org.apache.pdfbox").setLevel(OFF);
        getLogger("org.apache.fontbox").setLevel(OFF);
    }

}

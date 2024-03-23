///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 21+
//SOURCES DocumentSigner.java
//SOURCES PdfLoader.java
//SOURCES PropertiesLoader.java
//SOURCES RandomUtils.java
//SOURCES ScanEmulator.java,
//DEPS org.apache.pdfbox:pdfbox:3.0.1

package com.ardetrick.pdfsigner;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        var propertiesPath = args.length > 0 ?
                args[0] :
                "config.properties";

        var config = PropertiesLoader.loadProperties(propertiesPath);

        try (var document = PdfLoader.loadPdf(config.pdfLoaderProperties())) {
            DocumentSigner.signAndDate(document,
                                       config.documentSignerProperties());

            ScanEmulator.emulateScanAndWriteFile(document, config.scanEmulatorProperties());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

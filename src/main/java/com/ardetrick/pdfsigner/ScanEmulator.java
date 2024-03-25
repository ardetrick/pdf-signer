package com.ardetrick.pdfsigner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

import static com.ardetrick.pdfsigner.RandomUtils.getRandomDouble;
import static com.ardetrick.pdfsigner.RandomUtils.getRandomFloatBetween;
import static java.awt.geom.AffineTransform.getRotateInstance;
import static java.awt.image.AffineTransformOp.TYPE_BILINEAR;
import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;

/**
 * Introduces some entropy to the quality and position of the entire document.
 * The intention is to help make each output look unique and as if it was scanned manually.
 * This extra entropy reduces the probability of the document getting flagged as identical
 * to prior submissions, which can delay document processing by the receiving agency in some
 * contexts.
 */
public class ScanEmulator {

    public static void emulateScanAndWriteFile(PDDocument document,
                                               ScanEmulator.Properties properties) throws IOException {
        try (PDDocument modifiedDocument = new PDDocument()) {
            emulateScan(document, modifiedDocument, properties);

            String formatted = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
            modifiedDocument.save(String.join(".", properties.scanEmulationFileOutputName, formatted, "pdf"));
        }
    }

    private static void emulateScan(PDDocument document,
                                    PDDocument modifiedDocument,
                                    Properties config) throws IOException {
        var renderer = new PDFRenderer(document);

        // Use the same DPI for all pages for consistency
        // Otherwise pages are rendered with different width and heights
        // This property changes the "quality" of the saved PDF
        var randomizedDPI = getRandomFloatBetween(config.randomDPIMin, config.randomDPIMax);

        for (var pageNumber = 0; pageNumber < document.getNumberOfPages(); pageNumber++) {
            var image = renderer.renderImageWithDPI(pageNumber, randomizedDPI);

            var rotatedImage = getRotatedImage(image, config);

            var modifiedPage = drawImageOnNewPDPage(modifiedDocument,
                                                    rotatedImage,
                                                    // Write image with the same width and height as read
                                                    image.getWidth(),
                                                    image.getHeight());

            modifiedDocument.addPage(modifiedPage);
        }
    }

    private static PDPage drawImageOnNewPDPage(PDDocument modifiedDocument,
                                               BufferedImage rotatedImage,
                                               float originalWidth,
                                               float originalHeight) throws IOException {
        PDPage page = new PDPage(new PDRectangle(originalWidth,
                                                 originalHeight));

        PDPageContentStream contentStream = new PDPageContentStream(modifiedDocument,
                                                                    page);

        // Draw the rotated image onto the PDF
        PDImageXObject pdImage = LosslessFactory.createFromImage(modifiedDocument, rotatedImage);
        contentStream.drawImage(pdImage, 0, 0, originalWidth, originalHeight);
        contentStream.close();
        return page;
    }

    private static BufferedImage getRotatedImage(BufferedImage image,
                                                 Properties config) {
        var rotationDegrees = getRandomDouble(config.randomRotateMin, config.randomRotateMax);
        var rotationRequired = Math.toRadians(rotationDegrees);

        // Find middle point of document to use as rotation point
        // TODO: add a randomization to rotation point
        var locationX = (double) image.getWidth() / 2;
        var locationY = (double) image.getHeight() / 2;

        // TODO: add getTranslateInstance()
        AffineTransform affineTransform = getRotateInstance(rotationRequired, locationX, locationY);
        AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, TYPE_BILINEAR);

        return affineTransformOp.filter(image, null);
    }

    public record Properties(String scanEmulationFileOutputName,
                             double randomRotateMin,
                             double randomRotateMax,
                             float randomDPIMin,
                             float randomDPIMax) {
        public static Properties fromProperties(java.util.Properties properties) {
            return new Properties(properties.getProperty("scan-emulator.file-output-name"),
                                  parseDouble(properties.getProperty("scan-emulator.rotation-radians-random-min")),
                                  parseDouble(properties.getProperty("scan-emulator.rotation-radians-random-max")),
                                  parseFloat(properties.getProperty("scan-emulator.dpi-random-min")),
                                  parseFloat(properties.getProperty("scan-emulator.dpi-random-max")));
        }
    }

}

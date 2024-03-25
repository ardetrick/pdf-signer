package com.ardetrick.pdfsigner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.ardetrick.pdfsigner.RandomUtils.getRandomFloatBetween;
import static java.lang.Integer.parseInt;
import static java.util.Objects.requireNonNull;
import static org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode.APPEND;
import static org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName.HELVETICA;

public class DocumentSigner {

    public static void signAndDate(PDDocument document,
                                   DocumentSigner.Properties properties) {
        addDateToDocument(document, properties);
        addSignatureToDocument(document, properties);
    }

    private static void addDateToDocument(PDDocument document,
                                          DocumentSigner.Properties properties) {
        PDPage page = document.getPage(properties.zeroBasedPageIndex());

        try (PDPageContentStream contentStream = new PDPageContentStream(document,
                                                                         page,
                                                                         APPEND,
                                                                         true,
                                                                         true)) {
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(HELVETICA), 12);
            contentStream.newLineAtOffset(properties.datePositionX, properties.datePositionY);
            String text = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            contentStream.showText(text);

            contentStream.endText();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addSignatureToDocument(PDDocument document,
                                              DocumentSigner.Properties properties) {
        PDPage page = document.getPage(properties.zeroBasedPageIndex());
        try (PDPageContentStream contentStream = new PDPageContentStream(document,
                                                                         page,
                                                                         APPEND,
                                                                         true,
                                                                         true)) {
            PDImageXObject pdImage = randomlySelectSignature(document, properties);

            // Get the original dimensions of the image
            float imageWidth = pdImage.getWidth();
            float imageHeight = pdImage.getHeight();

            float desiredHeight = getRandomFloatBetween(properties.signatureHeightMin,
                                                        properties.signatureHeightMax);

            // Calculate the aspect ratio
            float aspectRatio = imageWidth / imageHeight;

            // Calculate the new width to maintain the aspect ratio
            float newWidth = desiredHeight * aspectRatio;

            contentStream.drawImage(pdImage,
                                    getRandomFloatBetween(properties.signaturePositionXMin, properties.signaturePositionXMax),
                                    properties.signaturePositionY,
                                    newWidth,
                                    desiredHeight);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static PDImageXObject randomlySelectSignature(PDDocument document,
                                                          DocumentSigner.Properties properties) {
        var signatureImagesDirectory = properties.signatureImagesDirectory;

        return getSignatureFromFileSystem(document, signatureImagesDirectory);
    }

    private static PDImageXObject getSignatureFromFileSystem(PDDocument document, String signatureImagesDirectory) {
        File randomFile;

        // Not ideal, but this is easier than trying to list resources from a jar, which is required
        // when running the example for a GitHub link.
        // TODO: Revisit this approach.
        if (signatureImagesDirectory.equals("files/example/signatures")) {
            // This list must be kept in sync with what is added in Main.java.
            var path = List.of("files/example/signatures/signature-1.png",
                               "files/example/signatures/signature-1.png")
                    .get(RandomUtils.nextIntExclusive(2));

            InputStream resourceAsStream = DocumentSigner.class.getClassLoader().getResourceAsStream(path);

            if (resourceAsStream == null) {
                throw new RuntimeException("Could not find file at path: " + path);
            }
            try {
                return PDImageXObject.createFromByteArray(document, resourceAsStream.readAllBytes(), path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        File folder = new File(signatureImagesDirectory);
        File[] files = requireNonNull(folder.listFiles());
        randomFile = files[RandomUtils.nextIntExclusive(files.length)];
        try {
            return PDImageXObject.createFromFileByExtension(randomFile, document);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public record Properties(int zeroBasedPageIndex,
                             int datePositionX,
                             int datePositionY,
                             int signaturePositionXMin,
                             int signaturePositionXMax,
                             int signaturePositionY,
                             int signatureHeightMin,
                             int signatureHeightMax,
                             String signatureImagesDirectory) {
        public static Properties fromProperties(java.util.Properties properties) {
            return new DocumentSigner.Properties(parseInt(properties.getProperty("signer.page-index-zero-based")),
                                                 parseInt(properties.getProperty("signer.date.position-x")),
                                                 parseInt(properties.getProperty("signer.date.position-y")),
                                                 parseInt(properties.getProperty("signer.signature.position-x-min")),
                                                 parseInt(properties.getProperty("signer.signature.position-x-max")),
                                                 parseInt(properties.getProperty("signer.signature.position-y")),
                                                 parseInt(properties.getProperty("signer.signature.height.min")),
                                                 parseInt(properties.getProperty("signer.signature.height.max")),
                                                 properties.getProperty("signer.signatures-images-directory"));
        }
    }

}

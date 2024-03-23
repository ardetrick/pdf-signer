# PDF Signer

Automates the signing and dating of PDF documents. Includes configuration to introduce
varying aspects of entropy which makes each execution unique, including using different
signatures.

## How To Run

The easiest way to run this script is though JBang.

```bash
jbang https://github.com/ardetrick/pdf-signer/blob/main/src/main/java/com/ardetrick/pdfsigner/Main.java
```

Alternatively you can download the code and run Main directly.

## Configurations

By default, the `config.properties` file in the root of the repository will be used. To use a different
configuration file, provide the file path as the first argument.

``
jbang https://github.com/ardetrick/pdf-signer/blob/main/src/main/java/com/ardetrick/pdfsigner/Main.java ./different/path/custom.properties
``

### PDF File Locations

Modify these properties to change the location of the input/output file location.s 

```properties
# Location of unsigned PDF file
pdf-loader.file-input-name=files/pdf-document.pdf
# Location to write signed PDF file
scan-emulator.file-output-name=files/pdf-document.edited.pdf
# Location of directory containing signatures, if more than one is present one will be randomly selected.
signer.signatures-images-directory=files/signatures
```

### Location Of Signature On Document

Use these properties to configure the location where the signature should be placed on the PDF.

```properties
# Location of the signature
signer.date.position-x=500
signer.date.position-y=642
# Page of the document to sign
signer.page-index-zero-based=3
signer.signature.position-y=640
```

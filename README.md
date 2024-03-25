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

The script accepts one argument, a relative path to a properties file. When no file is provided, the example file properties
are used to demo the feature without any setup.

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

## Creating Signature Files

Using multiple signature files can add an extra level of entropy, reducing the possibility of automated checks
rejecting a signature as a duplicate.

### MacOS

1. Create a copy of: `files/images/empty-signature.png`
2. Open it in Preview.
3. From the menu bar, click `Tools` -> `Annotate` -> `Signature` -> `Manage Signatures`.
4. Click `Create Signature`.
5. Follow the instructions to create a signature. 
6. Add the signature to the png.
7. Expand the signature and crop it to fit.
8. Save.


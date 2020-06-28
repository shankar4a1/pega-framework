

package com.pega.util;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.encryption.*;
import org.apache.pdfbox.text.*;
import org.testng.*;

import java.io.*;

public class PDFUtil {
    public static String readDatafromPDF(final String path) {
        String pdfFileInText = null;
        try {
            Throwable t = null;
            try {
                final PDDocument document = PDDocument.load(new File(path));
                try {
                    Reporter.log("Reading PDF file: " + path, true);
                    document.getClass();
                    if (!document.isEncrypted()) {
                        final PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                        stripper.setSortByPosition(true);
                        final PDFTextStripper tStripper = new PDFTextStripper();
                        pdfFileInText = tStripper.getText(document);
                    }
                } finally {
                    if (document != null) {
                        document.close();
                    }
                }
            } finally {
                if (t == null) {
                    final Throwable exception = new Throwable();
                    t = exception;
                } else {
                    final Throwable exception = new Throwable();
                    if (t != exception) {
                        t.addSuppressed(exception);
                    }
                }
            }
        } catch (InvalidPasswordException pe) {
            Reporter.log("Invalid password exception::", true);
            pe.printStackTrace();
        } catch (FileNotFoundException fe) {
            Reporter.log("File not found exception::", true);
            fe.printStackTrace();
        } catch (IOException ie) {
            Reporter.log("IO exception::", true);
            ie.printStackTrace();
        }
        return pdfFileInText;
    }
}

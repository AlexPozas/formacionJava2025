package com.edisa.formacion.mayo2025;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

public class QRCodeGenerator {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java QRCodeGenerator \"<text-to-encode>\" \"<output-path>.jpg\"");
            return;
        }

        String text = args[0];
        String filePath = args[1];

        int width = 300;
        int height = 300;

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            Path path = Paths.get(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "JPG", path);
            System.out.println("QR Code image generated successfully: " + filePath);
        } catch (WriterException e) {
            System.err.println("Error generating QR code: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error saving the image: " + e.getMessage());
        }
    }
}

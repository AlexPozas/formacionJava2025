package com.edisa.formacion.mayo2025;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BarcodeGenerator {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Example formats: QR_CODE, EAN_13, CODE_128");
            return;
        }

        String text = args[0];
        String outputPath = args[1];
        String formatInput = args[2].toUpperCase();

        try {
            BarcodeFormat format = BarcodeFormat.valueOf(formatInput);

            int width = format == BarcodeFormat.QR_CODE ? 300 : 400;
            int height = format == BarcodeFormat.QR_CODE ? 300 : 150;

            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    text, format, width, height, new HashMap<>());

            Path path = Paths.get(outputPath);

            // Validación de directorio de salida
            if (!path.getParent().toFile().exists()) {
                throw new IOException("The directory does not exist: " + path.getParent());
            }

            MatrixToImageWriter.writeToPath(bitMatrix, "JPG", path);
            System.out.println("Barcode image generated successfully at: " + outputPath);

        } catch (IllegalArgumentException e) {
            System.err.println("Invalid barcode format: " + formatInput);
            System.err.println("Available formats: QR_CODE, EAN_13, CODE_128, etc.");
        } catch (WriterException | IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}


package com.example.legal_meterology.certificate;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

public class QRCodeGenerator {

    public String generate(
            String verificationUrl,
            String outputPath) throws Exception {

        int width = 300;
        int height = 300;

        Map<EncodeHintType, Object> hints = new HashMap<>();

        hints.put(
                EncodeHintType.MARGIN,
                1
        );

        BitMatrix matrix = new MultiFormatWriter().encode(
                verificationUrl,
                BarcodeFormat.QR_CODE,
                width,
                height,
                hints
        );

        Path path = Path.of(outputPath);

        MatrixToImageWriter.writeToPath(
                matrix,
                "PNG",
                path
        );

        return path.toString();
    }
}
package com.sougata.form_engine.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public final class CompressionUtil {

    private CompressionUtil() {
    }

    public static String compress(String input) {
        if (input == null) {
            return null;
        }

        if (input.isEmpty()) {
            return "";
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             DeflaterOutputStream deflater = new DeflaterOutputStream(
                     outputStream,
                     new Deflater(Deflater.BEST_COMPRESSION)
             )) {

            deflater.write(input.getBytes(StandardCharsets.UTF_8));
            deflater.finish();

            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Failed to compress string.", e);
        }
    }

    public static String decompress(String compressed) {
        if (compressed == null) {
            return null;
        }

        if (compressed.isEmpty()) {
            return "";
        }

        byte[] compressedBytes = Base64.getUrlDecoder().decode(compressed);

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedBytes);
             InflaterInputStream inflater = new InflaterInputStream(inputStream);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = inflater.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return outputStream.toString(StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException("Failed to decompress string.", e);
        }
    }
}
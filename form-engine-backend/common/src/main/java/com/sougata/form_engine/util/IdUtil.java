package com.sougata.form_engine.util;

import java.util.*;

public class IdUtil {

    private static final String partValueDelimiter = "\u001F";
    private static final String partsDelimiter = "\u001E";
    private static final String keyValueDelimiter = "\u001D";

    public static String generateCompressedEncodedId(Map<String, List<String>> inputs) {
        var parts = new ArrayList<String>();

        inputs.forEach((k, v) -> {
            var part = k + keyValueDelimiter + String.join(partValueDelimiter, v);
            parts.add(part);
        });

        var originalId = String.join(partsDelimiter, parts);

        return CompressionUtil.compress(originalId);
    }

    public static Map<String, List<String>> reconstructCompressedEncodedId(String id) {
        var decompressedId = CompressionUtil.decompress(id);

        var parts = decompressedId.split(partsDelimiter);

        var result = new HashMap<String, List<String>>();

        for (var part : parts) {
            var kv = part.split(keyValueDelimiter);

            if (kv.length < 1 || kv.length > 2) {
                throw new IllegalArgumentException("Invalid compressed and encoded ID. ID: " + id);
            }

            var k = kv[0];
            String[] v = {null};

            if (kv.length == 2) {
                v = kv[1].split(partValueDelimiter);
            }

            result.put(k, Arrays.asList(v));
        }

        return result;
    }

}

/*
 * Copyright 2013 (c) MuleSoft, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.xbib.raml.internal.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StreamUtils {

    private static final String RAML_PARSER_ENCODING = "raml.parser.encoding";

    private static String getDefaultEncoding() {
        return System.getProperty(RAML_PARSER_ENCODING, "UTF-8");
    }

    public static Reader reader(InputStream stream) {
        try {
            byte[] content = IOUtils.toByteArray(stream);
            return new InputStreamReader(new ByteArrayInputStream(content), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    public static String toString(InputStream stream) {
        try {
            byte[] content = IOUtils.toByteArray(stream);
            Charset encoding = StandardCharsets.UTF_8;
            return new String(trimBom(content), encoding);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    public static String trimBom(String content) {
        String trimmed = content;
        trimmed = new String(trimBom(content.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        return trimmed;
    }

    public static byte[] trimBom(byte[] content) {
        int bomSize = 0;
        if (content.length > 4) {
            // check for UTF_32BE and UTF_32LE BOMs
            if (content[0] == 0x00 && content[1] == 0x00 && content[2] == (byte) 0xFE && content[3] == (byte) 0xFF ||
                    content[0] == (byte) 0xFF && content[1] == (byte) 0xFE && content[2] == 0x00 && content[3] == 0x00) {
                bomSize = 4;
            }
        }
        if (content.length > 3 && bomSize == 0) {
            // check for UTF-8 BOM
            if (content[0] == (byte) 0xEF && content[1] == (byte) 0xBB && content[2] == (byte) 0xBF) {
                bomSize = 3;
            }
        }
        if (content.length > 2 && bomSize == 0) {
            // check for UTF_16BE and UTF_16LE BOMs
            if (content[0] == (byte) 0xFE && content[1] == (byte) 0xFF || content[0] == (byte) 0xFF && content[1] == (byte) 0xFE) {
                bomSize = 2;
            }
        }

        if (bomSize > 0) {
            int trimmedSize = content.length - bomSize;
            byte[] trimmedArray = new byte[trimmedSize];
            System.arraycopy(content, bomSize, trimmedArray, 0, trimmedSize);
            return trimmedArray;
        }
        return content;
    }

}

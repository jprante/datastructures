package org.xbib.datastructures.charset;

/*
 * #%L
 * ch-commons-charset
 * %%
 * Copyright (C) 2012 Cloudhopper by Twitter
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for encoding and decoding between Strings and byte arrays.
 *
 * @author joelauer (twitter: @jjlauer or <a href="http://twitter.com/jjlauer" target=window>http://twitter.com/jjlauer</a>)
 */
public class CharsetUtil {

    public static final HashMap<String,Charset> charsets;

    public static final String NAME_ISO_8859_1 = "ISO-8859-1";
    public static final String NAME_ISO_8859_15 = "ISO-8859-15";
    public static final String NAME_GSM = "GSM";
    public static final String NAME_PACKED_GSM = "PACKED-GSM";
    public static final String NAME_UCS_2 = "UCS-2";
    public static final String NAME_UCS_2LE = "UCS-2LE";
    public static final String NAME_UTF_8 = "UTF-8";
    /** Modified version of UTF-8 designed mostly for serialization and speed.
     The byte arrays it produces is NOT identical to UTF-8 but is safe to use
     for Java-to-Java serialization. */
    public static final String NAME_MODIFIED_UTF8 = "MODIFIED-UTF8";
    // special charset for "Airwide SMSCs" that have a unique GSM mapping
    public static final String NAME_AIRWIDE_IA5 = "AIRWIDE-IA5";
    // special charset for "Vodafone M2" SMSC that has a unique GSM mapping
    public static final String NAME_VFD2_GSM = "VFD2-GSM";
    /** Alias for "PACKED-GSM" */
    public static final String NAME_GSM7 = "GSM7";
    /** Alias for "GSM" */
    public static final String NAME_GSM8 = "GSM8";
    /** Alias for "AIRWIDE-IA5" */
    public static final String NAME_AIRWIDE_GSM = "AIRWIDE-GSM";
    // special charset for "T-Mobile Netherlands" SMSC that has a unique GSM mapping
    public static final String NAME_TMOBILENL_GSM = "TMOBILE-NL-GSM";

    public static final Charset CHARSET_ISO_8859_1 = new ISO88591Charset();
    public static final Charset CHARSET_ISO_8859_15 = new ISO885915Charset();
    public static final Charset CHARSET_GSM = new GSMCharset();
    public static final Charset CHARSET_PACKED_GSM = new PackedGSMCharset();
    public static final Charset CHARSET_UCS_2 = new UCS2Charset();
    public static final Charset CHARSET_UCS_2LE = new UCS2LECharset();
    public static final Charset CHARSET_UTF_8 = new UTF8Charset();
    /** Modified version of UTF-8 designed mostly for serialization and speed.
     The byte arrays it produces is NOT identical to UTF-8 but is safe to use
     for Java-to-Java serialization. */
    public static final Charset CHARSET_MODIFIED_UTF8 = new ModifiedUTF8Charset();
    public static final Charset CHARSET_AIRWIDE_IA5 = new AirwideIA5Charset();
    public static final Charset CHARSET_VFD2_GSM = new VFD2GSMCharset();
    /** Alias for "PACKED-GSM" */
    public static final Charset CHARSET_GSM7 = CHARSET_PACKED_GSM;
    /** Alias for "GSM" */
    public static final Charset CHARSET_GSM8 = CHARSET_GSM;
    /** Alias for "AIRWIDE-IA5" */
    public static final Charset CHARSET_AIRWIDE_GSM = CHARSET_AIRWIDE_IA5;
    public static final Charset CHARSET_TMOBILENL_GSM = new TMobileNlGSMCharset();

    static {
        charsets = new HashMap<String,Charset>();
        charsets.put(NAME_ISO_8859_1, CHARSET_ISO_8859_1);
        charsets.put(NAME_ISO_8859_15, CHARSET_ISO_8859_15);
        charsets.put(NAME_GSM, CHARSET_GSM);
        charsets.put(NAME_MODIFIED_UTF8, CHARSET_MODIFIED_UTF8);
        charsets.put(NAME_PACKED_GSM, CHARSET_PACKED_GSM);
        charsets.put(NAME_UCS_2, CHARSET_UCS_2);
        charsets.put(NAME_UCS_2LE, CHARSET_UCS_2LE);
        charsets.put(NAME_UTF_8, CHARSET_UTF_8);
        charsets.put(NAME_AIRWIDE_IA5, CHARSET_AIRWIDE_IA5);
        charsets.put(NAME_VFD2_GSM, CHARSET_VFD2_GSM);
        charsets.put(NAME_GSM7, CHARSET_GSM7);
        charsets.put(NAME_GSM8, CHARSET_GSM8);
        charsets.put(NAME_AIRWIDE_GSM, CHARSET_AIRWIDE_GSM);
        charsets.put(NAME_TMOBILENL_GSM, CHARSET_TMOBILENL_GSM);
    }

    public static Map<String, Charset> getCharsetMap() {
        return charsets;
    }

    public static Charset map(String charsetName) {
        String upperCharsetName = charsetName.toUpperCase();
        return charsets.get(upperCharsetName);
    }

    public static byte[] encode(StringBuilder stringBuilder, String charsetName) {
        Charset charset = map(charsetName);
        if (charset == null) {
            return null;
        }
        return encode(stringBuilder, charset);
    }

    static public byte[] encode(StringBuilder stringBuilder, Charset charset) {
        return charset.encode(stringBuilder);
    }

    static public void decode(byte[] bytes, StringBuilder buffer, String charsetName) {
        Charset charset = map(charsetName);
        if (charset == null) {
            // do nothing
            return;
        }
        decode(bytes, buffer, charset);
    }

    static public void decode(byte[] bytes, StringBuilder buffer, Charset charset) {
        charset.decode(bytes, buffer);
    }

    public static String decode(byte[] bytes, String charsetName) {
        Charset charset = map(charsetName);
        if (charset == null) {
            return null;
        }
        return charset.decode(bytes);
    }

    public static String decode(byte[] bytes, Charset charset) {
        return charset.decode(bytes);
    }

    public static String normalize(StringBuilder stringBuilder, String charsetName) {
        Charset charset = map(charsetName);
        if (charset == null) {
            throw new IllegalArgumentException("Unsupported charset [" + charsetName + "]");
        }
        return normalize(stringBuilder, charset);
    }

    public static String normalize(StringBuilder stringBuilder, Charset charset) {
        return charset.normalize(stringBuilder);
    }
}

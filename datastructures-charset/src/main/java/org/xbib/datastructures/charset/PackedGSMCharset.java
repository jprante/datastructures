package org.xbib.datastructures.charset;

/**
 * The <b>PackedGSMCharset</b> class handles the encoding and decoding of the
 * GSM default encoding charset, with packing as per GSM 03.38 spec.
 *
 * The encoding and decoding are based on the mapping at
 * http://www.unicode.org/Public/MAPPINGS/ETSI/GSM0338.TXT
 */
public class PackedGSMCharset extends GSMCharset {

    @Override
    public byte[] encode(StringBuilder stringBuilder) {
        byte[] unpacked = super.encode(stringBuilder);
        return GSMBitPacker.pack(unpacked);
    }

    @Override
    public void decode(byte[] bytes, StringBuilder buffer) {
        byte[] unpacked = GSMBitPacker.unpack(bytes);
        super.decode(unpacked, buffer);
    }
}

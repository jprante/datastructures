package org.xbib.datastructures.charset;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.xbib.datastructures.charset.util.Hex;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class GSMBitPackerTest {

    private static final Logger logger = Logger.getLogger(GSMBitPackerTest.class.getName());

    @Test
    public void unpackAndPack() throws Exception {
        byte[] packed = null;
        byte[] unpacked = null;
        assertArrayEquals(unpacked, GSMBitPacker.unpack(packed));
        assertArrayEquals(packed, GSMBitPacker.pack(unpacked));

        // zero length returns zero length array
        packed = new byte[0];
        unpacked = new byte[0];
        assertArrayEquals(unpacked, GSMBitPacker.unpack(packed));
        assertArrayEquals(packed, GSMBitPacker.pack(unpacked));

        // 7-bit single byte is always the single byte
        packed = Hex.hexToByteArray("7F");
        unpacked = Hex.hexToByteArray("7F");
        assertArrayEquals(unpacked, GSMBitPacker.unpack(packed));
        assertArrayEquals(packed, GSMBitPacker.pack(unpacked));

        // 8-bit single byte ends up as 1 byte since the MSB of 1
        packed = Hex.hexToByteArray("FF");
        unpacked = Hex.hexToByteArray("7F");
        assertArrayEquals(unpacked, GSMBitPacker.unpack(packed));
        // NOT A 2-WAY TEST
        //Assert.assertArrayEquals(packed, GSMBitPacker.pack(unpacked));
        assertArrayEquals(Hex.hexToByteArray("7F"), GSMBitPacker.pack(unpacked));

        // these 2 bytes decoded
        packed = Hex.hexToByteArray("9B32");
        unpacked = Hex.hexToByteArray("1B65");
        assertArrayEquals(unpacked, GSMBitPacker.unpack(packed));
        assertArrayEquals(packed, GSMBitPacker.pack(unpacked));
        
        // "JOE" packed into 
        packed = Hex.hexToByteArray("CA7719");
        unpacked = "Joe".getBytes();
        assertArrayEquals(unpacked, GSMBitPacker.unpack(packed));
        assertArrayEquals(packed, GSMBitPacker.pack(unpacked));

        // "JOEY" packed into
        packed = Hex.hexToByteArray("CA77390F");
        unpacked = "Joey".getBytes();
        assertArrayEquals(unpacked, GSMBitPacker.unpack(packed));
        assertArrayEquals(packed, GSMBitPacker.pack(unpacked));
        
        // packed into 00c1285432bd74
        packed = Hex.hexToByteArray("00c1285432bd74");
        unpacked = Hex.hexToByteArray("0002232125262f3a");
        assertArrayEquals(unpacked, GSMBitPacker.unpack(packed));
        assertArrayEquals(packed, GSMBitPacker.pack(unpacked));

        // 8 bytes packed into 7 bytes
        packed = Hex.hexToByteArray("CA77392F64D7CB");
        unpacked = "JoeyBlue".getBytes();
        assertArrayEquals(unpacked, GSMBitPacker.unpack(packed));
        assertArrayEquals(packed, GSMBitPacker.pack(unpacked));

        // 160 character message in 140 bytes
        packed = Hex.hexToByteArray("d3b29b0c0abb414d2a68da9c82e8e8301d347ebbe9e1b47b0e9297cfe9b71b348797c769737a0c1aa3c3f239685e1fa341e139c8282fbbc76850783c2ebbe97316680a0fbbd37334283c1e97ddf4390b54a68f5d2072195e7693d3ee33e8ed06b1dfe3301b347ed7dd7479de050219df7250191f6ec3d96516286d060dc3ee3039cc02cdcb6e3268fe6e9763");
        unpacked = Hex.hexToByteArray("53656e6420616e204d5420534d53207468617420636f6e7461696e7320726567696f6e2073706563696669632063686172732073756368206173204672656e636820616363656e74732c205370616e69736820616363656e74732c206574632e20646570656e64696e67206f6e206c6f63616c20636f756e7472792e2020466f72206578616d706c652c2069662043616e6164612c2073656e6420736f6d6531");
        assertArrayEquals(unpacked, GSMBitPacker.unpack(packed));
        assertArrayEquals(packed, GSMBitPacker.pack(unpacked));

        // SPECIAL CASE -- last octet is zeros (AT character)
        // in 7-bit packed form, we don't know if its an AT char OR if its padding
        // basically 8 chars in 7 bytes with the last char being an @ character
        packed = Hex.hexToByteArray("CA77392F64D701");
        //unpacked = HexUtil.toByteArray("0002232125262f3a");
        //unpacked = "JoeyBlu\u0000".getBytes("ISO-8859-1");
        // a choice was made to strip off the trailing @ char
        unpacked = "JoeyBlu".getBytes(StandardCharsets.ISO_8859_1);
        assertArrayEquals(unpacked, GSMBitPacker.unpack(packed));
        assertArrayEquals(packed, GSMBitPacker.pack(unpacked));
    }
}

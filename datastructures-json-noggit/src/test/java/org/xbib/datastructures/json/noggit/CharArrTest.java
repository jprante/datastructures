package org.xbib.datastructures.json.noggit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CharArrTest {

    @Test
    public void testFixWriteCharArrToCharArr() throws Exception {
        CharArr target = new CharArr(10);
        target.append("1");
        assertEquals(1, target.size());
        CharArr source = new CharArr(10);
        source.append("23");
        assertEquals(2, source.size());
        target.write(source);
        assertEquals(3, target.size());
    }
}
package org.noggit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Created by eudoden on 07/01/2016.
 */
public class CharArrTest {

    @Test
    public void testFixWriteCharArrToCharArr() throws Exception {
        CharArr target = new CharArr(10);
        target.append("1");
        assertEquals(1,target.size());
        CharArr source = new CharArr(10);
        source.append("23");
        assertEquals(2, source.size());
        target.write(source);
        assertEquals(3, target.size());
    }
}
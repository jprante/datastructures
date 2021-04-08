package net.openhft.chronicle.bytes;

import net.openhft.chronicle.core.time.LongTime;
import net.openhft.chronicle.core.time.TimeProvider;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MappedUniqueMicroTimeProviderTest extends BytesTestCommon {

    @Test
    public void currentTimeMicros() {
        TimeProvider tp = MappedUniqueMicroTimeProvider.INSTANCE;
        long last = 0;
        for (int i = 0; i < 100_000; i++) {
            long time = tp.currentTimeMicros();
            assertTrue(time > last);
            assertEquals(LongTime.toMicros(time), time);
            last = time;
        }
    }

    @Test
    public void currentTimeNanos() {
        TimeProvider tp = MappedUniqueMicroTimeProvider.INSTANCE;
        long last = 0;
        for (int i = 0; i < 100_000; i++) {
            long time = tp.currentTimeNanos();
            assertTrue(time > last);
            assertEquals(LongTime.toNanos(time), time);
            last = time;
        }
    }
}
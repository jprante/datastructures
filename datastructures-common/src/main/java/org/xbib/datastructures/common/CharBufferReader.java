package org.xbib.datastructures.common;

import java.io.Reader;
import java.nio.CharBuffer;

public class CharBufferReader extends Reader {

	private final CharBuffer charBuffer;

	public CharBufferReader(CharBuffer buffer) {
		buffer.mark();
		charBuffer = buffer;
	}

	@Override
	public int read() {
		if (!charBuffer.hasRemaining()) {
			return -1;
		}
		return charBuffer.get();
	}

	@Override
	public int read(char[] cbuf, int off, int len) {
		int remaining = charBuffer.remaining();
		if (remaining <= 0) {
			return -1;
		}
		int length = Math.min(len, remaining);
		charBuffer.get(cbuf, off, length);
		return length;
	}

	@Override
	public long skip(long n) {
		if (n < 0L) {
			return 0L;
		}
		int skipped = Math.min((int) n, charBuffer.remaining());
		charBuffer.position(charBuffer.position() + skipped);
		return skipped;
	}

	@Override
	public boolean ready() {
		return true;
	}

	@Override
	public boolean markSupported() {
		return true;
	}

	@Override
	public void mark(int readAheadLimit) {
		charBuffer.mark();
	}

	@Override
	public void reset() {
		charBuffer.reset();
	}

	@Override
	public void close() {
		charBuffer.position(charBuffer.limit());
	}
}

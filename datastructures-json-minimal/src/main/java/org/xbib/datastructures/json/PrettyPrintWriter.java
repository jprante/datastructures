package org.xbib.datastructures.json;

import java.io.IOException;
import java.io.Writer;

public class PrettyPrintWriter extends JsonWriter {

    private final char[] indentChars;
    private int indent;

    public PrettyPrintWriter(Writer writer, char[] indentChars) {
        super(writer);
        this.indentChars = indentChars;
    }

    @Override
    public void writeArrayOpen() throws IOException {
        indent++;
        writer.write('[');
        writeNewLine();
    }

    @Override
    public void writeArrayClose() throws IOException {
        indent--;
        writeNewLine();
        writer.write(']');
    }

    @Override
    public void writeArraySeparator() throws IOException {
        writer.write(',');
        if (!writeNewLine()) {
            writer.write(' ');
        }
    }

    @Override
    public void writeObjectOpen() throws IOException {
        indent++;
        writer.write('{');
        writeNewLine();
    }

    @Override
    public void writeObjectClose() throws IOException {
        indent--;
        writeNewLine();
        writer.write('}');
    }

    @Override
    public void writeMemberSeparator() throws IOException {
        writer.write(':');
        writer.write(' ');
    }

    @Override
    public void writeObjectSeparator() throws IOException {
        writer.write(',');
        if (!writeNewLine()) {
            writer.write(' ');
        }
    }

    private boolean writeNewLine() throws IOException {
        if (indentChars == null) {
            return false;
        }
        writer.write('\n');
        for (int i = 0; i < indent; i++) {
            writer.write(indentChars);
        }
        return true;
    }

}

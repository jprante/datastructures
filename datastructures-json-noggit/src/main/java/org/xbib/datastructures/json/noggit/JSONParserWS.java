package org.xbib.datastructures.json.noggit;

import java.io.IOException;
import java.io.Reader;

public class JSONParserWS extends JSONParser {

    public static abstract class WhitespaceHandler {
        public abstract void whitespaceNotification(int state, CharArr whitespace, boolean containsComment);
    }

    private final CharArr outWS = new CharArr(64);
    private WhitespaceHandler wsHandler = new WhitespaceHandler() {
        @Override
        public void whitespaceNotification(int state, CharArr whitespace, boolean containsComment) {
            System.out.println("state=" + state + " comment=" + containsComment + " ws=" + whitespace.toString());
        }
    };

    public JSONParserWS(Reader in) {
        super(in);
    }

    public JSONParserWS(Reader in, char[] buffer) {
        super(in, buffer);
    }

    public JSONParserWS(char[] data, int start, int end) {
        super(data, start, end);
    }

    public JSONParserWS(String data) {
        super(data);
    }

    public JSONParserWS(String data, int start, int end) {
        super(data, start, end);
    }

    public void setWhitespaceHandler(WhitespaceHandler wsHandler) {
        this.wsHandler = wsHandler;
    }


    // TODO: use subclassing if handling comments is sufficiently slower?
    protected int getCharNWS() throws IOException {
        outWS.reset();

        outer:
        for (; ; ) {
            int ch = getChar();
            switch (ch) {
                case ' ':
                case '\t':
                case '\r':
                case '\n':
                    outWS.write(ch);
                    continue outer;
                case '#':
                    getNewlineComment();
                    continue outer;
                case '/':
                    getSlashComment();
                    continue outer;
                default:
                    return ch;
            }
        }
    }

    protected void getNewlineComment() throws IOException {
        // read a # or a //, so go until newline
        for (; ; ) {
            int ch = getChar();
          if (ch != -1) {
            outWS.write(ch);
          }
            // don't worry about DOS /r/n... we'll stop on the \r and let the rest of the whitespace
            // eater consume the \n
            if (ch == '\n' || ch == '\r' || ch == -1) {
                return;
            }


        }
    }

    protected void getSlashComment() throws IOException {
        int ch = getChar();
      if (ch != -1) {
        outWS.write(ch);
      }

        if (ch == '/') {
            getNewlineComment();
            return;
        }

        if (ch != '*') {
            throw err("Invalid comment: expected //, /*, or #");
        }

        ch = getChar();
      if (ch != -1) {
        outWS.write(ch);
      }

        for (; ; ) {
            if (ch == '*') {
                ch = getChar();
              if (ch != -1) {
                outWS.write(ch);
              }

                if (ch == '/') {
                    return;
                } else if (ch == '*') {
                    // handle cases of *******/
                    continue;
                }
            }
            if (ch == -1) {
                return;
            }
            ch = getChar();
          if (ch != -1) {
            outWS.write(ch);
          }
        }
    }


}

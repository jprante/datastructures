package org.xbib.datastructures.json.noggit;

import java.io.StringReader;

// handling numbers... either put a limit of buffersize on the number
// while reading it all into another buffer, or ???

/*
ISSUE: repeated keys in a map... is it an issue?
Leave it to a higher level to validate this?

The JSON standard says that keys *SHOULD* be unique, not *MUST*!!!
That means we should support this when parsing.
*/
public class MyParse {
    public static void main(String[] args) throws Exception {
        StringReader sr;
        sr = new StringReader("\"hello\"");
        sr = new StringReader("[\"a\",\"b\"]");
        sr = new StringReader("  [  \"a\"  ,  \"b\"  ] ");
        sr = new StringReader("  [  1  ,  23, 456,7890 ,999]  ");
        sr = new StringReader("{}");
        sr = new StringReader("{ \"a\" : 1}");
        sr = new StringReader("[[1]]");

        JSONParser js = new JSONParser(sr);
        int maxEv = 100;
        for (int i = 0; i < maxEv; i++) {
            System.out.println("  Parser State:" + js);
            int ev = js.nextEvent();
            System.out.println("  Parser State After nextEvent:" + js);
            switch (ev) {
                case JSONParser.STRING:
                    System.out.println("STRING:");
                    System.out.println(js.getString());
                    break;
                case JSONParser.NUMBER:
                    System.out.println("NUMBER:");
                    // System.out.println(js.getString());
                    break;
                case JSONParser.LONG:
                    System.out.println("LONG:");
                    //System.out.println(js.getLong());
                    System.out.println(js.getNumberChars());
                    break;
                case JSONParser.NULL:
                    System.out.println("NULL");
                    break;
                case JSONParser.BOOLEAN:
                    System.out.println("BOOLEAN:");
                    // System.out.println(js.getString());
                    break;
                case JSONParser.OBJECT_START:
                    System.out.println("OBJECT_START");
                    break;
                case JSONParser.OBJECT_END:
                    System.out.println("OBJECT_END");
                    break;
                case JSONParser.ARRAY_START:
                    System.out.println("ARRAY_START");
                    break;
                case JSONParser.ARRAY_END:
                    System.out.println("ARRAY_END");
                    break;
                case JSONParser.EOF:
                    System.out.println("EOF");
                    break;
                default:
                    System.out.println("UNKNOWN_EVENT_ID:" + ev);
                    break;
            }

          if (ev == JSONParser.EOF) {
            break;
          }
        }

    }

    // Could also have a cursor that contained information about the last entity
    // read... string, number (that would allow to pass back more specific info
    // about the number easier.

}

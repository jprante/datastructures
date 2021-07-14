package org.xbib.datastructures.json.noggit;

public class CharUtil {

    // belongs in number utils or charutil?
    public long parseLong(char[] arr, int start, int end) {
        long x = 0;
        boolean negative = arr[start] == '-';
        for (int i = negative ? start + 1 : start; i < end; i++) {
            // If constructing the largest negative number, this will overflow
            // to the largest negative number.  This is OK since the negation of
            // the largest negative number is itself in two's complement.
            x = x * 10 + (arr[i] - '0');
        }
        // could replace conditional-move with multiplication of sign... not sure
        // which is faster.
        return negative ? -x : x;
    }


    public int compare(char[] a, int a_start, int a_end, char[] b, int b_start, int b_end) {
        int a_len = a_end - a_start;
        int b_len = b_end - b_start;
        int len = Math.min(a_len, b_len);
        while (--len >= 0) {
            int c = a[a_start] - b[b_start];
          if (c != 0) {
            return c;
          }
            a_start++;
            b_start++;
        }
        return a_len - b_len;
    }

}

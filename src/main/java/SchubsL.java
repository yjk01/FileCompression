/*
 * Program     : LZW Encoding
 * Description : This program compresses a file using LZW encoding.
 * Author      : Jun Kim
 * Date        : 05/07/2024
 * Course      : CS375
 * Compile     : javac SchubsL.java
 * Execute     : java SchubsL uncompressed-file-name
 */

import java.io.File;

public class SchubsL {
    private static final int R = 128; // number of input chars
    private static final int L = 256; // number of codewords = 2^W
    private static final int W = 8; // codeword width

    // LZW compression
    public static void compress(String fin) {
        System.out.println("Compressing " + fin + "...");

        // read the input
        BinaryIn in = new BinaryIn(fin);
        String input = in.readString();

        // tabulate frequency counts
        TST<Integer> st = new TST<Integer>();

        for (int i = 0; i < R; i++) {
            st.put("" + (char) i, i);
        }

        // build codebook
        int code = R + 1;

        // write the output
        BinaryOut out = new BinaryOut(fin + ".ll");

        // compress the input string using LZW compression
        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);
            out.write(st.get(s), W);
            int t = s.length();
            if (t < input.length() && code < L) {
                st.put(input.substring(0, t + 1), code++);
            }
            input = input.substring(t);
        }

        out.write(R, W);
        out.close();
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println(
                    "Wrong Number of arguments! Try java SchubsL <filename>");
            return;
        }

        for (int i = 0; i < args.length; i++) {
            // check if file exists and if exists, skip compression
            File file = new File(args[i] + ".ll");

            if (file.exists()) {
                System.out.println("File " + args[i] + ".ll already exists. Skipping compression.");
                continue;
            }
            compress(args[i]);
        }
    }

}

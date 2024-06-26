/*
 * Program     : Decompress / Extract
 * Description : This program decompresses a file using Huffman encoding / LZW or extracts an archive file.
 * Author      : Jun Kim
 * Date        : 05/07/2024
 * Course      : CS375
 * Compile     : javac Deschubs.java
 * Execute     : java Deschubs filename.hh|ll|zh
 */

import java.io.File;
import java.io.IOException;

public class Deschubs {

    private static int R = 128;
    private static int L = 256;
    private static int W = 8;

    private static class Node implements Comparable<Node> {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        // is the node a leaf node?
        private boolean isLeaf() {
            assert (left == null && right == null) || (left != null && right != null);
            return (left == null && right == null);
        }

        // compare, based on frequency
        public int compareTo(Node that) {
            int freqComparison = this.freq - that.freq;
            if (freqComparison != 0) {
                return freqComparison;
            } else {
                return this.ch - that.ch;
            }
        }
    }

    public static void expandH(String fin) {

        // read in Huffman trie from input stream
        BinaryIn in = new BinaryIn(fin);
        String fout = fin.substring(0, fin.lastIndexOf(".hh"));
        // write the uncompressed file to output stream
        BinaryOut out = new BinaryOut(fout);
        Node root = readTrie(in, out);

        // number of bytes to write
        int length = in.readInt();

        // decode using the Huffman trie
        for (int i = 0; i < length; i++) {
            Node x = root;
            while (!x.isLeaf()) {
                boolean bit = in.readBoolean();
                if (bit)
                    x = x.right;
                else
                    x = x.left;
            }
            out.write(x.ch);
        }
        out.flush();
    }

    // read in Huffman trie from input stream
    private static Node readTrie(BinaryIn in, BinaryOut out) {
        boolean isLeaf = in.readBoolean();
        if (isLeaf) {
            return new Node(in.readChar(), -1, null, null);
        } else {
            return new Node('\0', -1, readTrie(in, out), readTrie(in, out));
        }
    }

    // decompress LZW-compressed file
    public static void expandL(String fin) {

        R = 256;
        L = 4096;
        W = 12;

        // read in the LZW dictionary from input stream
        BinaryIn in = new BinaryIn(fin);
        String fout = fin.substring(0, fin.lastIndexOf(".ll"));
        BinaryOut out = new BinaryOut(fout);

        // number of codewords = 2^W
        String[] st = new String[L];
        int i;

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";

        // read in codeword, output corresponding string, and add new string to the
        int codeword = in.readInt(W);
        String val = st[codeword];

        // loop until we reach end of file
        while (true) {
            out.write(val);
            codeword = in.readInt(W);
            if (codeword == R)
                break;
            String s = st[codeword];
            if (i == codeword)
                s = val + val.charAt(0);
            if (i < L)
                st[i++] = val + s.charAt(0);
            val = s;
        }
        out.flush();
    }

    // uncompress the archive file using Huffman and extract content
    public static void expandA(String fin) throws IOException {
        BinaryIn in = new BinaryIn(fin);
        String fout = fin.substring(0, fin.lastIndexOf(".zh"));
        BinaryOut out = new BinaryOut(fout);
        Node root = readTrie(in, out);

        // number of bytes to write
        int length = in.readInt();

        // decode using the Huffman trie
        for (int i = 0; i < length; i++) {
            Node x = root;
            while (!x.isLeaf()) {
                boolean bit = in.readBoolean();
                if (bit)
                    x = x.right;
                else
                    x = x.left;
            }
            out.write(x.ch);
        }
        out.flush();

        in = null;

        File file = new File(fout);

        if (!file.exists()) {
            throw new IOException("File not found: " + fout);
        }

        in = new BinaryIn(fout);

        while (!in.isEmpty()) {
            // Read filename size
            int filenamesize = in.readInt();

            // Read separator
            char sep = in.readChar();

            // Read filename
            StringBuilder filenameBuilder = new StringBuilder();
            for (int i = 0; i < filenamesize; i++) {
                filenameBuilder.append(in.readChar());
            }
            String filename = filenameBuilder.toString();

            // Read separator
            sep = in.readChar();

            // Read file length
            long filesize = in.readLong();

            // Read separator
            sep = in.readChar();

            // Create BinaryOut for writing the extracted file
            out = new BinaryOut(filename);

            // Write file content to BinaryOut
            for (int i = 0; i < filesize; i++) {
                out.write(in.readChar());
            }

            System.out.println("Extracted file: " + filename);
            // Close BinaryOut
            out.close();
        }

        File fileToDelete = new File(fout);
        if (fileToDelete.exists()) {
            fileToDelete.delete();
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 1 || args.length > 1) {
            System.out.println(
                    "Wrong Number of arguments! Try java Deschubs <filename>.hh|ll");
            return;
        }

        String input = args[0];
        String extension = "";

        int i = input.lastIndexOf('.');

        if (i > 0) {
            extension = input.substring(i + 1);
        }

        // Huffman
        if (extension.equals("hh")) {
            File file = new File(input.substring(0, input.lastIndexOf(".hh")));
            if (file.exists()) {
                System.out.println(
                        "File " + input.substring(0, input.lastIndexOf(".hh")) + " already exists. Skipping...");
                return;
            }
            expandH(input);
        }

        // LZW
        if (extension.equals("ll")) {
            File file = new File(input.substring(0, input.lastIndexOf(".ll")));
            if (file.exists()) {
                System.out.println(
                        "File " + input.substring(0, input.lastIndexOf(".ll")) + " already exists. Skipping...");
                return;
            }

            expandL(input);
        }

        // Archive
        if (extension.equals("zh")) {
            expandA(input);
        }

        // if extension does not equal to hh, ll, or zh say that file type is not
        // supported
        if (!extension.equals("hh") && !extension.equals("ll") && !extension.equals("zh")) {
            System.err.println("This file type is not supported.");
        }
    }

}

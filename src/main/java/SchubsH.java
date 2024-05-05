/*
 * Program     : Huffman Encoding
 * Description : This program compresses a file using Huffman encoding.
 * Author      : Jun Kim
 * Date        : 03/08/2024
 * Course      : CS375
 * Compile     : javac HuffmanSE2.java
 * Execute     : java HuffmanSE2 uncompressed-file-name compressed-file-name (l)
 */

import java.io.File;

public class SchubsH {

    // alphabet size of extended ASCII
    private static final int R = 256;

    // Huffman trie node
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
            return this.freq - that.freq;
        }
    }

    // compress bytes from standard input and write to standard output
    public static void compress(String fin) {
        System.err.println("Compressing " + fin + "...");
        // read the input
        BinaryIn in = new BinaryIn(fin);
        String s = in.readString();
        char[] input = s.toCharArray();

        // tabulate frequency counts
        int[] freq = new int[R];
        for (int i = 0; i < input.length; i++) {
            char c = input[i];
            freq[c]++;
        }

        // build Huffman trie
        Node root = buildTrie(freq);

        // build code table
        String[] st = new String[R];
        buildCode(st, root, "");

        BinaryOut out = new BinaryOut(fin + ".hh");
        // print trie for decoder
        writeTrie(root, out);

        // print number of bytes in original uncompressed message
        out.write(input.length);

        // use Huffman code to encode input
        for (int i = 0; i < input.length; i++) {
            String code = st[input[i]];
            for (int j = 0; j < code.length(); j++) {
                if (code.charAt(j) == '0') {
                    out.write(false);
                } else if (code.charAt(j) == '1') {
                    out.write(true);
                } else {
                    throw new RuntimeException("Illegal state");
                }
            }
        }

        // flush output stream
        // out.flush();
        out.close();
    }

    // build the Huffman trie given frequencies
    private static Node buildTrie(int[] freq) {

        // initialze priority queue with singleton trees
        MinPQ<Node> pq = new MinPQ<Node>();
        for (char i = 0; i < R; i++) {
            if (freq[i] > 0) {
                pq.insert(new Node(i, freq[i], null, null));
            }
        }

        if (pq.size() == 1) {
            return pq.delMin();
        }
        // merge two smallest trees
        while (pq.size() > 1) {
            if (!pq.isEmpty()) {
                Node left = pq.delMin();
                if (pq.isEmpty()) {
                    throw new RuntimeException("Priority queue underflow: not enough nodes in the priority queue.");
                }
                Node right = pq.delMin();
                Node parent = new Node('\0', left.freq + right.freq, left, right);
                pq.insert(parent);
            }
        }
        return pq.delMin();
    }

    // write bitstring-encoded trie to standard output
    private static void writeTrie(Node x, BinaryOut out) {
        if (x.isLeaf()) {
            out.write(true);
            out.write(x.ch);
            return;
        }
        out.write(false);
        writeTrie(x.left, out);
        writeTrie(x.right, out);
    }

    // make a lookup table from symbols and their encodings
    private static void buildCode(String[] st, Node x, String s) {
        if (!x.isLeaf()) {
            buildCode(st, x.left, s + '0');
            buildCode(st, x.right, s + '1');
        } else {
            st[x.ch] = s;
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println(
                    "Wrong Number of arguments! Try java SchubsH <filename>");
            return;
        }

        for (int i = 0; i < args.length; i++) {
            File file = new File(args[i] + ".hh");

            if (file.exists()) {
                System.out.println("File " + args[i] + ".hh already exists. Skipping...");
                continue;
            }

            compress(args[i]);
        }
    }
}

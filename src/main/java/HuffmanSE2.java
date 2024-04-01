/*
 * Program     : Huffman Encoding
 * Description : This program compresses a file using Huffman encoding.
 * Author      : Jun Kim
 * Date        : 03/08/2024
 * Course      : CS375
 * Compile     : javac HuffmanSE2.java
 * Execute     : java HuffmanSE2 uncompressed-file-name compressed-file-name (l)
 */

public class HuffmanSE2 {

    // alphabet size of extended ASCII
    private static final int R = 256;
    private static boolean log = false;

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
    public static void compress(String fin, String fout, boolean log) {
        // read the input
        BinaryIn in = new BinaryIn(fin);
        String s = in.readString();
        char[] input = s.toCharArray();

        // tabulate frequency counts
        int[] freq = new int[R];
        if (log) {
            System.out.println(
                    "----------------------------------------------log----------------------------------------------");
            System.out.println("Input: " + new String(input));
        }
        for (int i = 0; i < input.length; i++) {
            char c = input[i];
            freq[c]++;
        }

        if (log)
            System.out.println("Input size: " + input.length + " bytes \n");
        // build Huffman trie
        Node root = buildTrie(freq, log);

        // build code table
        String[] st = new String[R];
        buildCode(st, root, "", log);

        BinaryOut out = new BinaryOut(fout + ".hh");
        // print trie for decoder
        writeTrie(root, out);
        if (log)
            System.out.println("\nencoding...\n");

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
            if (log)
                System.out.println("Char " + input[i] + ": " + code);
        }

        // flush output stream
        out.flush();
        out.close();
    }

    // build the Huffman trie given frequencies
    private static Node buildTrie(int[] freq, boolean log) {

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
                if (log)
                    System.out.println(
                            "buildTrie: new parent: " + " Parent: " + parent.freq + " Frequencies:  " + left.freq
                                    + " and " + right.freq);
            }
        }

        System.out.println("\n");
        return pq.delMin();
    }

    // write bitstring-encoded trie to standard output
    private static void writeTrie(Node x, BinaryOut out) {
        if (x.isLeaf()) {
            out.write(true);
            out.write(x.ch, 8);
            return;
        }
        BinaryStdOut.write(false);
        writeTrie(x.left, out);
        writeTrie(x.right, out);
    }

    // make a lookup table from symbols and their encodings
    private static void buildCode(String[] st, Node x, String s, boolean log) {
        if (!x.isLeaf()) {
            buildCode(st, x.left, s + '0', log);
            buildCode(st, x.right, s + '1', log);
        } else {
            st[x.ch] = s;
            if (log)
                System.out.println("buildCode " + x.ch + ": " + s);
        }
    }

    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) {
            System.out.println(
                    "Wrong Number of arguments! Try java HuffmanSE2 uncompressed-file-name compressed-file-name (l)");
            return;
        }

        String input = args[0];
        String output = args[1];
        boolean printLogs = args.length == 3 && args[2].equals("l"); // if args has 3 elements and the third element is
                                                                     // "l" set printLogs to true

        // if printLogs is true, set log to true, else set log to false and run compress
        if (printLogs) {
            log = true;
            compress(input, output, log);
        } else {
            log = false;
            compress(input, output, log);
        }
    }
}

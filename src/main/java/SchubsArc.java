import java.io.File;
import java.io.IOException;

public class SchubsArc {
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

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            throw new IllegalArgumentException("No filles");
        }

        String tarFileName = "archive";

        Tarsn.main(tarFileName, args);

        BinaryIn in = new BinaryIn(tarFileName);
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

        BinaryOut out = new BinaryOut(tarFileName + ".zh");
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

        File file = new File(tarFileName);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
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

}
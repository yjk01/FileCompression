import java.io.File;
import java.io.IOException;

public class Deschubs {

    private static final int R = 128;
    private static final int L = 256;
    private static final int W = 8;

    private static boolean log = false;

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

    private static Node readTrie(BinaryIn in, BinaryOut out) {
        boolean isLeaf = in.readBoolean();
        if (isLeaf) {
            return new Node(in.readChar(), -1, null, null);
        } else {
            return new Node('\0', -1, readTrie(in, out), readTrie(in, out));
        }
    }

    public static void expandL(String fin) {

        BinaryIn in = new BinaryIn(fin);
        String fout = fin.substring(0, fin.lastIndexOf(".ll"));
        BinaryOut out = new BinaryOut(fout);

        String[] st = new String[L];
        int i;

        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";

        int codeword = in.readInt(W);
        String val = st[codeword];

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

        if (extension.equals("hh")) {
            expandH(input);
        }

        if (extension.equals("ll")) {
            expandL(input);
        }

        if (extension.equals("zh")) {
            expandA(input);
        }

        if (extension.equals("zl")) {
            System.err.println("RIP LOL");
        }

        System.out.println("DECompression complete.");
    }

}

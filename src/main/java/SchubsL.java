public class SchubsL {
    private static final int R = 256;
    private static final int L = 4096;
    private static final int W = 12;

    private static boolean log = false;

    public static void compress(String fin, boolean log) {
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++) {
            st.put("" + (char) i, i);
        }

        int code = R + 1;

        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);
            BinaryStdOut.write(st.get(s), W);
            int t = s.length();
            if (t < input.length() && code < L) {
                st.put(input.substring(0, t + 1), code++);
            }
            input = input.substring(t);
        }

        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    }

    public static void expand() {
        String[] st = new String[L];
        int i;

        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";

        int codeword = BinaryStdIn.readInt(W);
        String val = st[codeword];

        while (true) {
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R)
                break;
            String s = st[codeword];
            if (i == codeword)
                s = val + val.charAt(0);
            if (i < L)
                st[i++] = val + s.charAt(0);
            val = s;
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args.length < 1 || args.length > 2) {
            System.out.println(
                    "Wrong Number of arguments! Try java HuffmanSE2 uncompressed-file-name compressed-file-name (l)");
            return;
        }

        String input = args[0];
        boolean printLogs = args.length == 2 && args[1].equals("l"); // if args has 3 elements and the third element is
                                                                     // "l" set printLogs to true

        // if printLogs is true, set log to true, else set log to false and run compress
        if (printLogs) {
            log = true;
            compress(input, log);
        } else {
            log = false;
            compress(input, log);
        }
        System.out.println("Compression complete.");
    }

}

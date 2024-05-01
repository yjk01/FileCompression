public class SchubsL {
    private static final int R = 128;
    private static final int L = 256;
    private static final int W = 8;

    public static void compress(String fin) {
        System.out.println("Compressing " + fin + "...");
        BinaryIn in = new BinaryIn(fin);
        String input = in.readString();
        TST<Integer> st = new TST<Integer>();

        for (int i = 0; i < R; i++) {
            st.put("" + (char) i, i);
        }

        int code = R + 1;
        BinaryOut out = new BinaryOut(fin + ".ll");

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
                    "Wrong Number of arguments! Try java SchubsL <filename>");
            return;
        }

        String input = args[0];

        // if printLogs is true, set log to true, else set log to false and run compress
        compress(input);
        System.out.println("Compression complete.");
    }

}

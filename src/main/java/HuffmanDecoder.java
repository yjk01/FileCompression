import java.io.*;

class HuffmanNode {
    char data;
    HuffmanNode left, right;

    public HuffmanNode(char data, HuffmanNode left2, HuffmanNode right2) {
        this.data = data;
        this.left = this.right = null;
    }
}

public class HuffmanDecoder {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java HuffmanDecoder compressed-file-name decompressed-file-name");
            return;
        }

        String inputFile = args[0];
        String outputFile = args[1];

        try {
            decompress(inputFile, outputFile);
            System.out.println("Decompression completed successfully!");
        } catch (IOException e) {
            System.out.println("Error during decompression: " + e.getMessage());
        }
    }

    private static void decompress(String inputFile, String outputFile) throws IOException {
        try (BitInputStream inputStream = new BitInputStream(new FileInputStream(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            HuffmanNode root = readHuffmanTree(inputStream);
            int totalBits = 0; // Initialize totalBits variable
            totalBits = inputStream.readInt(totalBits); // Read the total number of bits in the encoded message

            HuffmanNode current = root;
            for (int i = 0; i < totalBits; i++) {
                int bit = inputStream.read();
                if (bit == -1) {
                    throw new IOException("Unexpected end of file");
                }

                current = (bit == 0) ? current.left : current.right;

                if (current.data != '\0') {
                    writer.write(current.data);
                    current = root;
                }
            }
        }
    }

    private static HuffmanNode readHuffmanTree(BitInputStream inputStream) throws IOException {
        int bit = inputStream.read();
        if (bit == -1) {
            throw new IOException("Unexpected end of file");
        }

        if (bit == 0) {
            HuffmanNode left = readHuffmanTree(inputStream);
            HuffmanNode right = readHuffmanTree(inputStream);
            return new HuffmanNode('\0', left, right);
        } else {
            char data = (char) inputStream.readInt(8);
            return new HuffmanNode(data, null, null);
        }
    }
}

class BitInputStream implements Closeable {
    private final InputStream input;
    private int currentByte;
    private int numBitsFilled;

    public BitInputStream(InputStream input) {
        if (input == null) {
            throw new NullPointerException("Input stream is null");
        }
        this.input = input;
        this.currentByte = 0;
        this.numBitsFilled = 0;
    }

    public int read() throws IOException {
        if (currentByte == -1) {
            return -1; // Signal end of file
        }
        if (numBitsFilled == 8) {
            currentByte = input.read();
            if (currentByte == -1) {
                return -1; // Signal end of file
            }
            numBitsFilled = 0;
        }
        int bit = (currentByte >> (7 - numBitsFilled)) & 1;
        numBitsFilled++;
        return bit;
    }

    public int readInt(int numBits) throws IOException {
        int value = 0;
        for (int i = 0; i < numBits; i++) {
            int bit = read();
            if (bit == -1) {
                // End of file, return -1
                return -1;
            }
            value = (value << 1) | bit;
        }
        return value;
    }

    @Override
    public void close() throws IOException {
        input.close();
    }
}

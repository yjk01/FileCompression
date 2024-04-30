/*
 * Program     : Tarsn.java
 * Description : This program archives files into a tar file.
 * Author      : Jun Kim
 * Date        : 04/24/2024
 * Course      : CS375
 * Compile     : javac Tarsn.java
 * Execute     : java Tarsn archive-name file1 file2...
 */

import java.io.IOException;
import java.io.File;

public class Tarsn {
    public static void main(String name, String[] args) throws IOException {
        BinaryOut out = null;
        char separator = (char) 255; // all ones 11111111

        try {
            // case when number of arguments is wrong
            if (args.length < 1) {
                System.err.println("Wrong number of arguments! Try java Tars2 archive-name file1 file2 ...");
                return;
            }

            // archive file
            out = new BinaryOut(name);

            // Loop through input files and put them in archive
            for (int i = 0; i < args.length; i++) {
                File inFile = new File(args[i]);

                // Check if input file exists and is a regular file
                if (!inFile.exists() || !inFile.isFile()) {
                    System.err.println("Input file does not exist or is not a regular file: " + args[i]);
                    continue; // Skip to the next file
                }

                BinaryIn bin = new BinaryIn(args[i]);
                long fileSize = inFile.length();
                int fileNameSize = args[i].length();

                // Write info to archive
                out.write(fileNameSize);
                out.write(separator);
                out.write(args[i]);
                out.write(separator);
                out.write(fileSize);
                out.write(separator);
                out.write(bin.readString());
            }
        } finally {
            if (out != null)
                out.close();
        }
    }
}
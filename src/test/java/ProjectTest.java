/*
* Program : Huffman Encoding Test
* Description : This program tests the program that compresses a file using
Huffman encoding.
* Author : Jun Kim
* Date : 03/08/2024
* Course : CS375
* Execute : mvn clean // mvn compile // mvn test
*/

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProjectTest {
    private String fin;

    @Before
    public void setUp() {
        fin = "test.txt";
    }

    @After
    public void tearDown() {
        File file = new File(fin);
        if (file.exists()) {
            file.delete();
        }

        File file2 = new File(fin + ".hh");
        if (file2.exists()) {
            file2.delete();
        }

        File file3 = new File(fin + ".ll");
        if (file3.exists()) {
            file3.delete();
        }
    }

    @Test
    public void testEmptyH() throws IOException {
        System.err.println("Testing Empty file for Huffman");

        try {
            new File(fin).createNewFile();
        } catch (Exception e) {
            fail("Failed to create a file");
        }

        assertTrue(new File(fin).length() == 0);

        if (new File(fin).length() > 0) {
            String[] args = { fin };
            SchubsH.main(args);

        } else {
            System.err.println("Uncompressed file is empty. Skipping compression");
            assertTrue(true);
        }
    }

    @Test
    public void testMissingH() throws IOException {
        System.err.println("Testing missing file for Huffman");

        String mFile = "missing.txt";

        try {
            SchubsH.main(new String[] { mFile });
            fail("Failed to catch missing file");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testContainManyH() throws IOException {
        System.err.println("Testing file with many characters for Huffman");

        String testcontent = "This is a test file with many characters. This file is used to test the Huffman encoding program! 123!@#%%^";

        try {
            Files.write(Paths.get(fin), testcontent.getBytes());
        } catch (Exception e) {
            fail("Failed to write to a file");
        }

        assertTrue(new File(fin).length() > 0);

        String[] args = { fin };
        SchubsH.main(args);

        assertTrue(new File(fin + ".hh").length() > 0);

    }

    @Test
    public void testWrongNumberofArgsH() throws IOException {
        System.err.println("Testing wrong number of arguments for Huffman");

        ByteArrayOutputStream newOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(newOut));

        ByteArrayInputStream in = new ByteArrayInputStream(fin.getBytes());
        System.setIn(in);

        boolean status = false;

        try {
            SchubsH.main(new String[] {});

            String output = newOut.toString().trim();

            System.err.println(output);

            if (output.contains("Wrong Number of arguments! Try java SchubsH <filename>")) {
                status = true;
            }
            assertTrue(status);
        } finally {
            System.setOut(System.out);
            System.setIn(System.in);
        }
    }

    @Test
    public void testReallyLongH() throws IOException {
        System.err.println("Testing really long file for Huffman");

        String testcontent = "Thisisatestfilewithmanycharacters.ThisfileisusedtotesttheHuffmanencodingprogram!123!@#%%^";

        try {
            Files.write(Paths.get(fin), testcontent.getBytes());
        } catch (Exception e) {
            fail("Failed to write to a file");
        }

        assertTrue(new File(fin).length() > 0);

        String[] args = { fin };
        SchubsH.main(args);

        assertTrue(new File(fin + ".hh").length() > 0);

    }

    @Test
    public void testOnlyLowerCaseH() throws IOException {
        System.err.println("Testing file with only lowercase characters for Huffman");

        String testcontent = "thisisatestfilewithonlylowercasecharacters";

        try {
            Files.write(Paths.get(fin), testcontent.getBytes());
        } catch (Exception e) {
            fail("Failed to write to a file");
        }

        assertTrue(new File(fin).length() > 0);

        String[] args = { fin };
        SchubsH.main(args);

        assertTrue(new File(fin + ".hh").length() > 0);

    }

    @Test
    public void testOnlyUpperCaseH() throws IOException {
        System.err.println("Testing file with only uppercase characters for Huffman");

        String testcontent = "THISISATESTFILEWITHONLYUPPERCASECHARACTERS";

        try {
            Files.write(Paths.get(fin), testcontent.getBytes());
        } catch (Exception e) {
            fail("Failed to write to a file");
        }

        assertTrue(new File(fin).length() > 0);

        String[] args = { fin };
        SchubsH.main(args);

        assertTrue(new File(fin + ".hh").length() > 0);
    }
}
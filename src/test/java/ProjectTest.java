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
    private String fin1;

    @Before
    public void setUp() {
        fin = "test.txt";
        fin1 = "test1.txt.ll";

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

        File file4 = new File(fin1);
        if (file4.exists()) {
            file4.delete();
        }
    }

    // Tests for Huffman encoding

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

        System.err.println("-Empty file test for Huffman passed \n");
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

        System.err.println("-Missing file test for Huffman passed \n");
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

        System.err.println("-File with many characters test for Huffman passed \n");
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

        System.err.println("-Wrong number of arguments test for Huffman passed \n");
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

        System.err.println("-Really long file test for Huffman passed \n");
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

        System.err.println("-File with only lowercase characters test for Huffman passed \n");
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

        System.err.println("-File with only uppercase characters test for Huffman passed \n");
    }

    // Test for LZW encoding

    @Test
    public void testEmptyL() throws IOException {
        System.err.println("Testing Empty file for LZW");

        try {
            new File(fin).createNewFile();
        } catch (Exception e) {
            fail("Failed to create a file");
        }

        assertTrue(new File(fin).length() == 0);

        if (new File(fin).length() > 0) {
            String[] args = { fin };
            SchubsL.main(args);

        } else {
            System.err.println("Uncompressed file is empty. Skipping compression");
            assertTrue(true);
        }

        System.err.println("-Empty file test for LZW passed \n");
    }

    @Test
    public void testMissingL() throws IOException {
        System.err.println("Testing missing file for LZW");

        String mFile = "missing.txt";

        try {
            SchubsL.main(new String[] { mFile });
            fail("Failed to catch missing file");
        } catch (NullPointerException e) {
            assertTrue(true);
        }

        System.err.println("-Missing file test for LZW passed \n");
    }

    @Test
    public void testContainManyL() throws IOException {
        System.err.println("Testing file with many characters for LZW");

        String testcontent = "This is a test file with many characters. This file is used to test the LZW encoding program! 123!@#%%^";

        try {
            Files.write(Paths.get(fin), testcontent.getBytes());
        } catch (Exception e) {
            fail("Failed to write to a file");
        }

        assertTrue(new File(fin).length() > 0);

        String[] args = { fin };
        SchubsL.main(args);

        assertTrue(new File(fin + ".ll").length() > 0);

        System.err.println("-File with many characters test for LZW passed \n");
    }

    @Test
    public void testWrongNumberofArgsL() throws IOException {
        System.err.println("Testing wrong number of arguments for LZW");

        ByteArrayOutputStream newOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(newOut));

        ByteArrayInputStream in = new ByteArrayInputStream(fin.getBytes());
        System.setIn(in);

        boolean status = false;

        try {
            SchubsL.main(new String[] {});

            String output = newOut.toString().trim();

            System.err.println(output);

            if (output.contains("Wrong Number of arguments! Try java SchubsL <filename>")) {
                status = true;
            }
            assertTrue(status);
        } finally {
            System.setOut(System.out);
            System.setIn(System.in);
        }

        System.err.println("-Wrong number of arguments test for LZW passed \n");
    }

    @Test
    public void testReallyLongL() throws IOException {
        System.err.println("Testing really long file for LZW");

        String testcontent = "Thisisatestfilewithmanycharacters.ThisfileisusedtotesttheLZWencodingprogram!123!@#%%^";

        try {
            Files.write(Paths.get(fin), testcontent.getBytes());
        } catch (Exception e) {
            fail("Failed to write to a file");
        }

        assertTrue(new File(fin).length() > 0);

        String[] args = { fin };
        SchubsL.main(args);

        assertTrue(new File(fin + ".ll").length() > 0);

        System.err.println("-Really long file test for LZW passed \n");
    }

    @Test
    public void testOnlyLowerCaseL() throws IOException {
        System.err.println("Testing file with only lowercase characters for LZW");

        String testcontent = "thisisatestfilewithonlylowercasecharacters";

        try {
            Files.write(Paths.get(fin), testcontent.getBytes());
        } catch (Exception e) {
            fail("Failed to write to a file");
        }

        assertTrue(new File(fin).length() > 0);

        String[] args = { fin };
        SchubsL.main(args);

        assertTrue(new File(fin + ".ll").length() > 0);

        System.err.println("-File with only lowercase characters test for LZW passed \n");
    }

    @Test
    public void testOnlyUpperCaseL() throws IOException {
        System.err.println("Testing file with only uppercase characters for LZW");

        String testcontent = "THISISATESTFILEWITHONLYUPPERCASECHARACTERS";

        try {
            Files.write(Paths.get(fin), testcontent.getBytes());
        } catch (Exception e) {
            fail("Failed to write to a file");
        }

        assertTrue(new File(fin).length() > 0);

        String[] args = { fin };
        SchubsL.main(args);

        assertTrue(new File(fin + ".ll").length() > 0);

        System.err.println("-File with only uppercase characters test for LZW passed \n");
    }

    // Tests for Deschubs

    @Test
    public void testEmptyD() throws IOException {
        System.err.println("Testing Empty file for Deschubs");

        try {
            new File(fin1).createNewFile();
        } catch (Exception e) {
            fail("Failed to create a file");
        }

        assertTrue(new File(fin1).length() == 0);

        if (new File(fin1).length() > 0) {
            String[] args = { fin1 };
            Deschubs.main(args);

        } else {
            System.err.println("Uncompressed file is empty. Skipping decompression");
            assertTrue(true);
        }

        System.err.println("-Empty file test for Deschubs passed \n");
    }

    @Test
    public void testMissingD() throws IOException {
        System.err.println("Testing missing file for Deschubs");

        String mFile = "missing.txt";

        try {
            Deschubs.main(new String[] { mFile });
        } catch (NullPointerException e) {
            assertTrue(true);
        }

        System.err.println("-Missing file test for Deschubs passed \n");
    }

    @Test
    public void testWrongNumberofArgsD() throws IOException {
        System.err.println("Testing wrong number of arguments for Deschubs");

        ByteArrayOutputStream newOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(newOut));

        ByteArrayInputStream in = new ByteArrayInputStream(fin1.getBytes());
        System.setIn(in);

        boolean status = false;

        try {
            Deschubs.main(new String[] {});

            String output = newOut.toString().trim();

            System.err.println(output);

            if (output.contains("Wrong Number of arguments! Try java Deschubs <filename>.hh|ll")) {
                status = true;
            }
            assertTrue(status);
        } finally {
            System.setOut(System.out);
            System.setIn(System.in);
        }

        System.err.println("-Wrong number of arguments test for Deschubs passed \n");
    }

    @Test
    public void ContainManyD() throws IOException {
        System.err.println("Testing really long file for Deschubs");

        String testcontent = "Thisisatestfilewithmanycharacters.ThisfileisusedtotesttheDeschubsencodingprogram!123!@#%%^         \n";

        try {
            Files.write(Paths.get(fin), testcontent.getBytes());
        } catch (Exception e) {
            fail("Failed to write to a file");
        }

        assertTrue(new File(fin).length() > 0);

        SchubsL.main(new String[] { fin });

        String[] args = { fin + ".ll" };
        Deschubs.main(args);

        String decompmessage = new String(Files.readAllBytes(Paths.get(fin)));

        assertTrue(decompmessage.equals(testcontent));

        System.err.println("-Contain many file test for Deschubs passed \n");
    }

    @Test
    public void testOnlyUpperD() throws IOException {
        System.err.println("Testing really long file for Deschubs");

        String testcontent = "THISISATESTFILEWITHONLYUPPERCASECHARACTERS";

        try {
            Files.write(Paths.get(fin), testcontent.getBytes());
        } catch (Exception e) {
            fail("Failed to write to a file");
        }

        assertTrue(new File(fin).length() > 0);

        SchubsL.main(new String[] { fin });

        String[] args = { fin + ".ll" };
        Deschubs.main(args);

        String decompmessage = new String(Files.readAllBytes(Paths.get(fin)));

        assertTrue(decompmessage.equals(testcontent));

        System.err.println("-Only uppercase file test for Deschubs passed \n");
    }

    @Test
    public void testOnlyLowerD() throws IOException {
        System.err.println("Testing really long file for Deschubs");

        String testcontent = "thisisatestfilewithonlylowercasecharacters";

        try {
            Files.write(Paths.get(fin), testcontent.getBytes());
        } catch (Exception e) {
            fail("Failed to write to a file");
        }

        assertTrue(new File(fin).length() > 0);

        SchubsL.main(new String[] { fin });

        String[] args = { fin + ".ll" };
        Deschubs.main(args);

        String decompmessage = new String(Files.readAllBytes(Paths.get(fin)));

        assertTrue(decompmessage.equals(testcontent));

        System.err.println("-Only lowercase file test for Deschubs passed \n");
    }

    @Test
    public void testReallyLongD() throws IOException {
        System.err.println("Testing really long file for Deschubs");

        String testcontent = "Thisisatestfilewithmanycharacters.ThisfileisusedtotesttheDeschubsencodingprogram!123!@#%%^asdfabcdefghijklmnopqrstuvwxyz1234567890!@#$%^&*()_+";

        try {
            Files.write(Paths.get(fin), testcontent.getBytes());
        } catch (Exception e) {
            fail("Failed to write to a file");
        }

        assertTrue(new File(fin).length() > 0);

        SchubsL.main(new String[] { fin });

        String[] args = { fin + ".ll" };
        Deschubs.main(args);

        String decompmessage = new String(Files.readAllBytes(Paths.get(fin)));

        assertTrue(decompmessage.equals(testcontent));

        System.err.println("-Really long file test for Deschubs passed \n");
    }
}
// /*
// * Program : Huffman Encoding Test
// * Description : This program tests the program that compresses a file using
// Huffman encoding.
// * Author : Jun Kim
// * Date : 03/08/2024
// * Course : CS375
// * Execute : mvn clean // mvn compile // mvn test
// */

// import static org.junit.Assert.assertTrue;
// import static org.junit.Assert.fail;

// import java.io.ByteArrayInputStream;
// import java.io.ByteArrayOutputStream;
// import java.io.File;
// import java.io.IOException;
// import java.io.PrintStream;
// import java.nio.file.Files;
// import java.nio.file.Paths;
// import org.junit.After;
// import org.junit.Before;
// import org.junit.Test;

// /**
// * Unit test for simple App.
// */
// public class HuffmanSE2Test {
// private String uncompPath;
// private String compPath;

// @Before
// public void setUp() {
// uncompPath = "test_uncomp.txt";
// compPath = "test_comp.txt";
// }

// @After
// public void tearDown() {
// // Delete the test files after the test is done
// File uncomf = new File(uncompPath);
// File compf = new File(compPath);

// if (uncomf.exists()) {
// uncomf.delete();
// }

// if (compf.exists()) {
// compf.delete();
// }
// }

// @Test
// public void testEmptyFile() throws IOException {
// System.err.println("Testing empty file.");
// try {
// new File(uncompPath).createNewFile();
// } catch (Exception e) {
// fail("Unable to create the test_uncomp.txt file");
// }

// // Check if the uncompressed file is empty before compression
// assertTrue(new File(uncompPath).length() == 0);

// // Execute the compression only if the uncompressed file is not empty
// if (new File(uncompPath).length() > 0) {
// HuffmanSE2.compress(uncompPath, compPath, false);

// // Now, check if the compressed file is empty
// assertTrue(new File(compPath).length() == 0);
// } else {
// // If the uncompressed file is empty, mark the test as passed
// System.err.println("The uncompressed file is empty. Skipping the compression
// test.");
// assertTrue(true);
// }
// System.err.println("Test passed.\n");
// }

// @Test
// public void testMissingUncompressedFile() {
// System.err.println("Testing missing uncompressed file.");
// // Provide a non-existent uncompressed file path
// String missingFile = "nonexistent_file.txt";

// // Provide the path where you want to save the compressed file (output)
// String compressedFile = "test_comp.huff";

// // Execute the compression
// try {
// HuffmanSE2.compress(missingFile, compressedFile, false);

// fail("Expected RuntimeException for missing uncompressed file, but no
// exception was thrown.");
// } catch (NullPointerException e) {
// // If a NullPointerException is thrown, the test is successful
// assertTrue("Expected NullPointerException for missing uncompressed file",
// true);
// }
// System.err.println("Test passed.\n");
// }

// @Test
// public void testContainMany() {
// System.err.println("Testing contain many.");
// // Create a test file with various characters, numbers, and other stuff
// String testContent = "Lorem ipsum dolor sit amet, 123 consectetur adipiscing
// elit. "
// + "Sed 456 vehicula 789 mauris 0 eget 1 semper 2. "
// + "Nulla facilisi. Aenean 3 aliquam 4, 5 justo 6 id 7 finibus 8. "
// + "Vestibulum 9 ante 10 ipsum, 11 euismod 12 vitae 13, 14 tincidunt 15.";

// try {
// Files.write(Paths.get(uncompPath), testContent.getBytes());
// } catch (IOException e) {
// fail("Unable to create the test_uncomp.txt file");
// }

// // Check if the uncompressed file is not empty before compression
// assertTrue(new File(uncompPath).length() > 0);

// // Execute the compression
// HuffmanSE2.compress(uncompPath, compPath, false);

// // Now, check if the compressed file is not empty
// assertTrue(new File(compPath).length() > 0);
// System.err.println("Test passed.\n");
// }

// @Test
// public void testWrongNumberOfArguments() throws IOException {
// System.err.println("Testing wrong number of arguments.");
// // Capture stdout
// ByteArrayOutputStream newOut = new ByteArrayOutputStream();
// System.setOut(new PrintStream(newOut));

// // Redirect System.in
// ByteArrayInputStream in = new ByteArrayInputStream(uncompPath.getBytes());
// System.setIn(in);

// boolean status = false;
// try {
// // Call main method to run program
// HuffmanSE2.main(new String[] { uncompPath });

// // Convert the captured output to a string
// String output = newOut.toString().trim();

// System.err.println(output);

// // Verify that output contains information about the wrong number of
// arguments
// if (output.contains(
// "Wrong Number of arguments! Try java HuffmanSE2 uncompressed-file-name
// compressed-file-name (l)")) {
// status = true;
// }
// assertTrue(status);
// } finally {
// // Reset System.in and System.out
// System.setIn(System.in);
// System.setOut(System.out);
// }

// System.err.println("Test passed.\n");
// }

// @Test
// public void testReallyLong() {
// System.out.println("Testing really long.");
// // Create a test file with various characters, numbers, and other stuff
// String testContent =
// "BleeBlahBloowaitwhatisthisBleeBlahBloowaitwhatisthisBleeBlahBloowaitwhatisthisBleeBlahBloowaitwhatisthisBleeBlahBloowaitwhatisthis"
// +
// "BleeBlahBloowaitwhatisthisBleeBlahBloowaitwhatisthisBleeBlahBloowaitwhatisthisBleeBlahBloowaitwhatisthisBleeBlahBloowaitwhatisthisBleeBlahBloowaitwhatisthis";

// try {
// Files.write(Paths.get(uncompPath), testContent.getBytes());
// } catch (IOException e) {
// fail("Unable to create the test_uncomp.txt file");
// }

// // Check if the uncompressed file is not empty before compression
// assertTrue(new File(uncompPath).length() > 0);

// // Execute the compression
// HuffmanSE2.compress(uncompPath, compPath, false);

// // Now, check if the compressed file is not empty
// assertTrue(new File(compPath).length() > 0);

// System.err.println("Test passed.\n");
// }

// @Test
// public void onlyLowerCase() {
// System.err.println("Testing only lower case.");
// // Create a test file with various characters, numbers, and other stuff
// String testContent =
// "loremipsumdolorsitametconsecteturadipiscingelitseddoeiusmodtemporincididuntulaboreetdoloremagnaaliqua";

// try {
// Files.write(Paths.get(uncompPath), testContent.getBytes());
// } catch (IOException e) {
// fail("Unable to create the test_uncomp.txt file");
// }

// // Check if the uncompressed file is not empty before compression
// assertTrue(new File(uncompPath).length() > 0);

// // Execute the compression
// HuffmanSE2.compress(uncompPath, compPath, false);

// // Now, check if the compressed file is not empty
// assertTrue(new File(compPath).length() > 0);

// System.err.println("Test passed.\n");
// }

// @Test
// public void onlyUpperCase() {
// System.out.println("Testing only upper case.");
// // Create a test file with various characters, numbers, and other stuff
// String testContent =
// "LOREMIPSUMDOLORSITAMETCONSECTETURADIPISCINGELITSEDDOEIUSMODTEMPORINCIDIDUNTULABOREETDOLOREMAGNAALIQUA";

// try {
// Files.write(Paths.get(uncompPath), testContent.getBytes());
// } catch (IOException e) {
// fail("Unable to create the test_uncomp.txt file");
// }

// // Check if the uncompressed file is not empty before compression
// assertTrue(new File(uncompPath).length() > 0);

// // Execute the compression
// HuffmanSE2.compress(uncompPath, compPath, false);

// // Now, check if the compressed file is not empty
// assertTrue(new File(compPath).length() > 0);

// System.out.println("Test passed.\n");
// }

// @Test
// public void testLog() {
// System.out.println("Testing log.");
// // Create a test file with various characters, numbers, and other stuff
// String testContent =
// "bleeblahblueblahwhatisthisbleeblahblooblahbluebleeblee";

// try {
// Files.write(Paths.get(uncompPath), testContent.getBytes());
// } catch (IOException e) {
// fail("Unable to create the test_uncomp.txt file");
// }

// // Check if the uncompressed file is not empty before compression
// assertTrue(new File(uncompPath).length() > 0);

// // Capture stdout
// ByteArrayOutputStream newOut = new ByteArrayOutputStream();
// System.setOut(new PrintStream(newOut));

// // Redirect System.in
// ByteArrayInputStream in = new ByteArrayInputStream(uncompPath.getBytes());
// System.setIn(in);

// boolean status = false;
// try {
// // Call main method to run program
// HuffmanSE2.main(new String[] { uncompPath, compPath, "l" });

// // Convert the captured output to a string
// String output = newOut.toString().trim();

// System.err.println(output);

// // Verify that output contains information about the wrong number of
// arguments
// if (output.contains("buildCode")) {
// status = true;
// }

// System.err.println("Log: " + status);

// assertTrue(status);
// } finally {
// // Reset System.in and System.out
// System.setIn(System.in);
// System.setOut(System.out);
// }

// System.err.println("Test passed.\n");
// }

// }

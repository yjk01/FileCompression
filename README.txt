I. Design
    A. Huffman Encoding
        1. Algorithm Theory:
            a. Frequency Analysis: Begin by counting the frequency of characters in the input data.
            b. Tree Construction: Create a binary tree where each leaf node represents a character and the path from the root to the leaf corresponds to the binary code of the character.
            c. 
        2. Trade-offs:
            a. Compression process can get computationally intensive for large datasets.
            b. Has to read the whole file for a complete compression process.
    B. LZW Encoding
        1. Algorithm Theory:
            a. Dictionary-Based: Build a dictionary of strings encountered in the input data and replace recurring strings with shorter codes.
            b. Initialization: The dictionary begins with single-character data for all possible characters. (i.e All ASCII characters)
            c. 
    C. Tar
        1.

II. Installation
    A. Clone Repository: git clone
    B. Navigate into project directory: cd Project_File_Compression_yxk19a
    C. Make sure you are in the root project directory: pwd
        1. "pwd" should result in something like /Users/blee/Desktop/Project_File_Compression

III. Test Instructions
    A. Make sure you are in the same level as the "pom.xml" file
    B. run: mvn compile // mvn Test

IV. Run Examples
    A. Make sure you are in the root project directory
    B. Run Programs
        1. Huffman Compression: java -cp target/classes SchubsH <filename>
        2. LZW Compression: java -cp target/classes SchubsL <filename>
        3. Archive using Tar: java -cp target/classes SchubsArc archive-name <file1name> <file2name> ...
        4. Decompress files: java -cp target/classes Deschubs <filename.ll|hh|hz>
    

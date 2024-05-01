I. Design
    A. Huffman Encoding
        1. 
    B. LZW Encoding
        1.
    C. Tar
        1.

II. Installation
    A. Clone Repository: git Clone
    B. Navigate into project directory: cd Project_File_Compression
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
    

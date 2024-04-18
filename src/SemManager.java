import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
/**
 * On my honor:
 * - I have not used source code obtained from another student,
 * or any other unauthorized source, either modified or
 * unmodified.
 * 
 * - All source code and documentation used in my program is
 * either my original work, or was derived by me from the
 * source code published in the textbook for this course.
 *
 * - I have not discussed coding details about this project with
 * anyone other than my partner (in the case of a joint
 * submission), instructor, ACM/UPE tutors or the TAs assigned
 * to this course. I understand that I may discuss the concepts
 * of this program with other students, and that another student
 * may help me debug my program so long as neither of us writes
 * anything during the discussion or modifies any computer file
 * during the discussion. I have violated neither the spirit nor
 * letter of this restriction.
 */

import java.io.FileNotFoundException;

/**
 * This is the Handle class the MemoryManager returns
 * 
 * @author {Ibrahim Khalilov ibrahimk}
 * @version 2023-09-04
 */

public class SemManager {
    private static CommandProcessor cp;

    /**
     * 
     * Read contents of a file into a string
     * 
     * @param path
     *            File name
     * 
     * @return the string
     * 
     * @throws IOException
     * 
     */

    static String readFile(String path) throws IOException {

        byte[] encoded = Files.readAllBytes(Paths.get(path));

        return new String(encoded);

    }


    private static Parser createParser(String[] args) {
        Parser parser = null;
        try {
            parser = new Parser(args[2]);
        }
        catch (FileNotFoundException e) {
            Util.print("Error: File Not Found! Check syntax");
            return null;
        }
        return parser;
    }


    /**
     * @param args
     *            - initial sizes of Memory pool, Hash table and name of the
     *            input file
     * @throws Exception
     */

    public static void main(String[] args) throws Exception {
        Parser parser = createParser(args);
        if (parser == null) {
            return;
        }
        // DEFAULT VALUES
        int initialMemorySize = 16;
        int initialHashSize = 16;

        try {
            initialMemorySize = Integer.parseInt(args[0]);
            initialHashSize = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e) {
            return;
        }

        cp = new CommandProcessor(initialMemorySize, initialHashSize);

        String[][] nextChunk = parser.readNextChunk();

        while (nextChunk != null) {
            if ((nextChunk[0][0]).equals("")) {
                nextChunk = parser.readNextChunk();
                continue;
            }
            try {
                cp.command(nextChunk);
            }
            catch (NoSuchFieldException e) {
                Util.print("Syntax error in input file.");
            }
            nextChunk = parser.readNextChunk();
        }
        parser.closeScan();
    }
}


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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * CommandProcessor class process
 * commands from input file, read each line and
 * execute commands
 * 
 * @author {Ibrahim Khalilov ibrahimk}
 * @version 2023-09-04
 * 
 */

public class Parser {
    private Scanner scan;

    /**
     * We are constructing the Parser based on the name of file
     * 
     * @param str
     *            - name of file
     */
    public Parser(String str) throws FileNotFoundException {
        scan = new Scanner(new File(str));
    }


    /**
     * Reading 5 lines at a time of the command file
     * 
     * @return insertChunck
     */

    public String[][] readNextChunk() {
        if (!scan.hasNextLine()) {
// End of the file.
            return null;
        }
        String[] commands = getNextLine(true);

        if (commands[0].toLowerCase().equals("insert")) {
            String[][] insertChunck = new String[5][];
            insertChunck[0] = commands;
            int i = 1;
            while (i < 5) {
                insertChunck[i] = getNextLine(i == 1 || i == 4 ? false : true);
                i++;
            }
            return insertChunck;
        }
        else {
            return new String[][] { commands };
        }
    }


    private String[] getNextLine(boolean shouldSplit) {
        String currentLine = scan.nextLine().trim();

        if (shouldSplit) {
            while (currentLine.contains("  ")) {
                currentLine = currentLine.replace("  ", " ");
            }
            return currentLine.split(" ");
        }
        else {
            return new String[] { currentLine };
        }
    }


    /**
     * Done Scanning the file
     */
    public void closeScan() {
        scan.close();
    }
}

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

/**
 * CommandProcessor class process commands from input file, read each line and
 * execute commands
 * 
 * @author Ibrahim Khalilov ibrahimk
 * @version 2023-09-04
 * 
 */
public class CommandProcessor {

    private Database db;

    /**
     * This method will initialize MemManager and HashTable
     * 
     * @param initialMemorySize
     *            the size in bytes of memory manager
     * @param initialHashSize
     *            the number of slot in the initial hash
     */
    public CommandProcessor(int initialMemorySize, int initialHashSize) {
        db = new Database(initialMemorySize, initialHashSize);
    }


    /**
     * Processing the commands that will be inserted into the Hashtable and the
     * MemoryManager
     * 
     * @param lines
     *            from the command file
     * @throws Exception
     */

    public void command(String[][] lines) throws Exception {
        String action = lines[0][0];
        switch (action) {
            case "insert":
                Seminar sem = createSeminar(lines);
                db.insert(sem);
                break;
            case "delete":
                int deleteKey = Integer.parseInt(lines[0][1]);
                db.delete(deleteKey);
                break;
            case "search":
                int searchKey = Integer.parseInt(lines[0][1]);
                db.find(searchKey);
                break;
            case "print":
                db.print(lines[0][1]);
                break;
            default:
                /** Falls through */
                throw new NoSuchFieldException();
        }
    }


    private Seminar createSeminar(String[][] lines) {
        int idin = Integer.parseInt(lines[0][1]);
        String tin = lines[1][0];
        String datein = lines[2][0];
        int lin = Integer.parseInt(lines[2][1]);
        short xin = Short.parseShort(lines[2][2]);
        short yin = Short.parseShort(lines[2][3]);
        int cin = Integer.parseInt(lines[2][4]);
        String[] kin = lines[3];
        String descin = lines[4][0];

        Seminar sem = new Seminar(idin, tin, datein, lin, xin, yin, cin, kin,
            descin);
        return sem;
    }


    /**
     * Getter for the private method above - to use in unit tests
     * 
     * @param lines
     *            - 2D array of Strings
     * @return createSeminar
     */
    public Seminar getCreateSeminar(String[][] lines) {
        return createSeminar(lines);
    }

}

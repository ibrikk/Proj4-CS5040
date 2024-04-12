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
    private static MemManager mm;
    /** Using a hash table to store data */
    private static MyHashTable hash;

    /**
     * This method will initialize MemManager and HashTable
     * 
     * @param initialMemorySize
     *            the size in bytes of memory manager
     * @param initialHashSize
     *            the number of slot in the initial hash
     */
    public CommandProcessor(int initialMemorySize, int initialHashSize) {
        mm = new MemManager(initialMemorySize);
        hash = new MyHashTable(initialHashSize);
    }


    /**
     * Processing the commands that will be inserted into the Hashtable and the
     * MemoryManager
     * 
     * @param lines
     *            from the command file
     */

    public void command(String[][] lines) throws NoSuchFieldException {
        String action = lines[0][0];
        switch (action) {
            case "insert":
// Pretend getting Handle from mm
                Seminar sem = createSeminar(lines);
                int mockId = 0;
                Handle mockFromMM = new Handle(7, 2345);
                Record mockFromMMRec = new Record(mockId, mockFromMM);
                int insertKey = Integer.parseInt(lines[0][1]);
                boolean didInsert = hash.insert(insertKey, mockFromMMRec);
                if (didInsert) {
                    Util.print(sem.toString());
                    try {
                        byte[] serializedSem = sem.serialize();
                        Util.print("Size: " + serializedSem.length);
                    }
                    catch (Exception e) {
                        Util.print("Could not serialize Seminar");
                    }
                }
                break;
            case "delete":
                int deleteKey = Integer.parseInt(lines[0][1]);
                hash.delete(deleteKey);
                break;
            case "search":
                int searchKey = Integer.parseInt(lines[0][1]);
                int foundIndex = hash.search(searchKey, true);
                if (foundIndex > -1) {
                    //
// Get record from Memory and print.
                }
                break;
            case "print":
                print(lines[0][1]);
                break;
            default:
                /** Falls through */
                throw new NoSuchFieldException();
        }
    }


    /** Custom Printing method */

    private void print(String location) throws NoSuchFieldException {
        switch (location) {
            case "hashtable":
                System.out.println("Hashtable:");
                hash.printHashTable();
                break;
            case "blocks":
                System.out.println("Freeblock List:");
                // Code here
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

/**
 * This is the Tombstone class for the hash table
 * 
 * @author {Ibrahim Khalilov ibrahimk}
 * @version 2024-04-25
 */
public class Tombstone implements HashableEntry {

    private static final Tombstone INSTANCE = new Tombstone();
    private final static int SEMINARID = -1;
    private final static Handle HANDLE = null;

    /**
     * Private constructor to prevent instantiation from outside the class.
     */
    public Tombstone() {
    }


    /**
     * Returns the singleton instance of EmptyNode.
     *
     * @return the singleton instance of EmptyNode
     */
    public static Tombstone getInstance() {
        return INSTANCE;
    }


    @Override
    public int getSeminarId() {
        return SEMINARID;
    }


    @Override
    public Handle getHandle() {
        return HANDLE;
    }

}

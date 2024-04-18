
public class Tombstone implements HashableEntry {

    private static final Tombstone INSTANCE = new Tombstone();
    private final static int seminarId = -1;
    private final static Handle handle = null;

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
        return seminarId;
    }


    @Override
    public Handle getHandle() {
        return handle;
    }

}

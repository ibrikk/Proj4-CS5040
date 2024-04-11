
public class Tombstone implements HashableEntry {

    private static final Tombstone INSTANCE = new Tombstone();

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
    public boolean isTombstone() {
        return true;
    }

}


public class Database {
    private static MemManager mm;
    private static MyHashTable hash;

    /**
     * This method will initialize MemManager and HashTable
     * 
     * @param initialMemorySize
     *            the size in bytes of memory manager
     * @param initialHashSize
     *            the number of slot in the initial hash
     */
    public Database(int initialMemorySize, int initialHashSize) {
        mm = new MemManager(initialMemorySize);
        hash = new MyHashTable(initialHashSize);
    }


    public void insert(Seminar sem) {
        int seminarId = sem.getId();
        try {
            byte[] serializedSem = sem.serialize();
            Handle handle = mm.insert(serializedSem);
            Record record = new Record(seminarId, handle);
            boolean didInsert = hash.insert(record);
            if (didInsert) {
                Util.print(sem.toString());
                Util.print("Size: " + serializedSem.length);
            }
        }
        catch (Exception e) {
            Util.print("Could not serialize Seminar");
        }
    }


    public void delete(int deleteKey) {
        hash.delete(deleteKey);
    }


    public void find(int searchKey) throws Exception {
        HashableEntry foundEntry = hash.find(searchKey, true);
        if (foundEntry instanceof Record) {
            byte[] serializedSem = mm.readBytes(foundEntry.getHandle());
            Seminar sem = Seminar.deserialize(serializedSem);
            Util.print(sem);
        }
    }


    /** Custom Printing method */

    public void print(String location) throws NoSuchFieldException {
        switch (location) {
            case "hashtable":
                System.out.println("Hashtable:");
                hash.printHashTable();
                break;
            case "blocks":
                System.out.println("Freeblock List:");
                mm.dump();
                break;
            default:
                /** Falls through */
                throw new NoSuchFieldException();
        }
    }

}

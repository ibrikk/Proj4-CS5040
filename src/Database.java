/**
 * Database class for operations
 */

/**
 * This is the HashableEntry class for the hash table
 * 
 * @author {Ibrahim Khalilov ibrahimk}
 * @version 2024-04-25
 */
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


    /**
     * This method will serialize the seminar and insert
     * 
     * @param sem
     *            seminar that was early created
     */
    public void insert(Seminar sem) {
        int seminarId = sem.getId();
        try {
            HashableEntry foundDuplicate = hash.find(seminarId);
            if (foundDuplicate instanceof Record) {
                Util.print("Insert FAILED - There is already a record with ID "
                    + seminarId);

                return;
            }
            byte[] serializedSem = sem.serialize();
            Handle handle = mm.insert(serializedSem);
            if (handle == null) {
                return;
            }
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


    /**
     * This method will delete the seminar based on key
     * 
     * @param deleteKey
     *            key to delete
     */
    public void delete(int deleteKey) {
        Record deletedRecord = hash.delete(deleteKey);
        if (deletedRecord == null) {
            return;
        }
        mm.remove(deletedRecord.getHandle());
        Util.print("Record with ID " + deletedRecord.getSeminarId()
            + " successfully deleted from the database");
    }


    /**
     * This method will find the seminar based on key
     * 
     * @param searchKey
     *            key to delete
     */
    public void find(int searchKey) throws Exception {
        HashableEntry foundEntry = hash.find(searchKey, true);
        if (foundEntry instanceof Record) {
            byte[] serializedSem = mm.readBytes(foundEntry.getHandle());
            Seminar sem = Seminar.deserialize(serializedSem);
            Util.print(sem.toString());
        }
    }


    /**
     * @param location
     *            print based on a location
     * 
     *            Custom Printing method
     */
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

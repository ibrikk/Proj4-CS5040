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
 * This is the Handle class the MemoryManager returns
 * 
 * @author {Ibrahim Khalilov ibrahimk}
 * @version 2023-09-04
 */
public class MyHashTable {

    private HashableEntry[] hashTable;

    private int size;

    private int usedSpaceCount;

    private Tombstone tombstone = Tombstone.getInstance();

    /**
     * 
     * @return array of Handles
     */
    public HashableEntry[] getHashTable() {
        return hashTable;
    }


    /**
     * 
     * @return size
     */
    public int getSize() {
        return size;
    }


    /**
     * 
     * @return filled hashTable slots
     */
    public int getUsedSpaceCount() {
        return usedSpaceCount;
    }


    /**
     * We are constructing the Hashtable based on the initial
     * size
     * 
     * @param initialSize
     *            - of the hash table
     */

    public MyHashTable(int initialSize) {

        if (!Util.isPowerOfTwo(initialSize)) {
            throw new IllegalArgumentException("Size must be a power of 2");
        }
        hashTable = new HashableEntry[initialSize];
        size = initialSize;
        usedSpaceCount = 0;
    }


    /**
     * @param key
     *            - calculating the remainder for the first hash
     * 
     * @return true or false
     */

    public int hash1(int key) {
        return key % size;
    }


    /**
     * @param key
     *            - calculating the the double-hashing for he second hash
     * 
     * @return true or false
     */
    public int hash2(int key) {
        return (((key / size) % (size / 2)) * 2) + 1;
    }


    /**
     * @param key
     *            - calculating the remainder for the first hash
     * 
     * @param newSize
     *            - calculating the remainder for the first hash
     *            on a given size
     * 
     * @return true or false
     */

    public int hash1(int key, int newSize) {
        return key % newSize;
    }


    /**
     * @param key
     *            - calculating the the double-hashing for he second hash
     * 
     * @param newSize
     *            - calculating the the double-hashing on a given size
     * 
     * @return true or false
     */
    public int hash2(int key, int newSize) {
        return (((key / newSize) % (newSize / 2)) * 2) + 1;
    }


    /**
     * @param key
     *            - Seminar ID
     * @param value
     *            - to be inserted in the other array that indicates
     *            startPos of the byte and length
     * @return true if was able to insert.
     */
    public boolean insert(Record record) {
        int seminarId = record.getSeminarId();
        // Check for inserting a negative key
        if (seminarId < 0) {
            Util.print(seminarId + " key cannot be a negative value");
            return false;
        }
        HashableEntry foundDuplicate = find(seminarId);
        if (foundDuplicate instanceof Record) {
            Util.print("Insert FAILED - There is already a record with ID "
                + seminarId);

            return false;
        }
        usedSpaceCount++;
        if (usedSpaceCount > (size / 2)) {
            doubleSize();
        }

        int hash1 = hash1(seminarId);
        int hash2 = hash2(seminarId);

        while (hashTable[hash1] instanceof Record) {
            hash1 += hash2;
            hash1 %= size;
        }
        hashTable[hash1] = record;

        Util.print("Successfully inserted record with ID " + seminarId);
        return true;
    }


    private void doubleSize() {
        int newSize = hashTable.length * 2;
        HashableEntry[] newArr = new HashableEntry[newSize];
        for (int i = 0; i < hashTable.length; i++) {
            if (hashTable[i] instanceof Record) {
                int seminarId = hashTable[i].getSeminarId();

                int hash1 = hash1(seminarId, newSize);
                int hash2 = hash2(seminarId, newSize);

                while (newArr[hash1] instanceof Record) {
                    hash1 += hash2;
                    hash1 %= size;
                }
                newArr[hash1] = hashTable[i];
            }
        }
        hashTable = newArr;
        size = newSize;
        Util.print("Hash table expanded to " + size + " records");
    }


    /**
     * @param key
     *            - What slot to delete and make tombstone in the hashtable
     *            array
     * @return true if found and deleted.
     */
    public Record delete(int key) {

        // Check for deleting a negative key
        if (key < 0) {
            Util.print(key + " key cannot be a negative value");
            return null;
        }

        int hash1 = hash1(key);
        int hash2 = hash2(key);
        // Keep track of search attempts.
        int tries = 0;
        while ((hashTable[hash1] == null || hashTable[hash1]
            .getSeminarId() != (Integer)key) && tries < size) {
            hash1 += hash2;
            hash1 %= size;
            tries++;
        }
        if (hashTable[hash1] == null || hashTable[hash1] instanceof Tombstone) {
            Util.print("Delete FAILED -- There is no record with ID " + key);
            return null;
        }
        if (hashTable[hash1].getSeminarId() == (Integer)key) {
            Record removed = (Record)hashTable[hash1];
            hashTable[hash1] = tombstone;
            return removed;
        }
        return null;
    }


    /**
     * @param key
     *            - if one parameter is given then set ShouldPrint to
     *            false
     * 
     * @return search method itself, which will use the false boolean as default
     */

    public HashableEntry find(int key) {

        return find(key, false);
    }


    /**
     * @param key
     *            - what slot of the hash table array to search
     * @param shouldPrint
     *            - if both parameters are given then set ShouldPrint can be
     *            set to true
     * 
     * @return the hash of the key or -1 if the ID was not found
     */

    public HashableEntry find(int key, boolean shouldPrint) {

        int hash1 = hash1(key);
        int hash2 = hash2(key);
        // Keep track of search attempts.
        int tries = 0;
        /// Casting int key to Integer so that the comparison runs
        while (hashTable[hash1] != null && hashTable[hash1]
            .getSeminarId() != (Integer)key && tries < size) {
            hash1 += hash2;
            hash1 %= size;
            tries++;
        }
        if (hashTable[hash1] != null && hashTable[hash1]
            .getSeminarId() == (Integer)key) {
            if (shouldPrint) {
                Util.print("Found record with ID " + hashTable[hash1]
                    .getSeminarId() + ":");
            }
            return hashTable[hash1];
        }
        if (shouldPrint) {
            Util.print("Search FAILED -- There is no record with ID " + key);
        }
        return null;
    }


    /** prints the whole hasTable */
    /** print the Hashtable is the value is in there */
    public void printHashTable() {
        int i = 0;
        int itemCount = 0;
        while (i < size) {
            if (hashTable[i] != null) {
                if (hashTable[i] instanceof Tombstone) {
                    Util.print(i + ": TOMBSTONE");
                }
                else {
                    Util.print(i + ": " + hashTable[i].getSeminarId());
                    itemCount++;
                }
            }
            i++;
        }
        Util.print("total records: " + itemCount);
    }
}

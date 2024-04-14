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

/// Using Integer type because it supports null
    private Integer[] keyTable;

    private Record[] values;

    private int size;

    private int usedSpaceCount;

    /**
     * 
     * @return array of keys
     */
    public Integer[] getKeyTable() {
        return keyTable;
    }


    /**
     * 
     * @return array of Handles
     */
    public Record[] getValues() {
        return values;
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
        keyTable = new Integer[initialSize];
        values = new Record[initialSize];
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
        int foundDuplicate = search(seminarId);
        if (foundDuplicate > -1) {
            Util.print("Insert FAILED - There is already a record with ID "
                + seminarId);

            return false;
        }
        usedSpaceCount++;
        if (usedSpaceCount > (size / 2)) {
            keyTable = Util.doubleSizeHashTableKeysArray(keyTable);
            values = Util.doubleSize(values);
            size = keyTable.length;
        }

        int hash1 = hash1(seminarId);
        int hash2 = hash2(seminarId);

        while (keyTable[hash1] != null && keyTable[hash1] != -1) {
            hash1 += hash2;
            hash1 %= size;
        }

        keyTable[hash1] = (Integer)seminarId;
        values[hash1] = record;

        Util.print("Successfully inserted record with ID " + seminarId);
        return true;
    }


    /**
     * @param key
     *            - What slot to delete and make tombstone in the hashtable
     *            array
     * @return true if found and deleted.
     */
    public boolean delete(int key) {

        // Check for deleting a negative key
        if (key < 0) {
            Util.print(key + " key cannot be a negative value");
            return false;
        }

        int hash1 = hash1(key);
        int hash2 = hash2(key);
        // Keep track of search attempts.
        int tries = 0;
        while ((keyTable[hash1] == null || keyTable[hash1] != (Integer)key)
            && tries < size) {
            hash1 += hash2;
            hash1 %= size;
            tries++;
        }
        if (keyTable[hash1] == null || keyTable[hash1] == -1) {
            Util.print("Delete FAILED -- There is no record with ID " + key);
            return false;
        }
        if (keyTable[hash1] == (Integer)key) {
            keyTable[hash1] = -1;
            values[hash1] = null;
// printHashTable();
// Util.printDiv();
            Util.print("Record with ID " + key
                + " successfully deleted from the database");
            usedSpaceCount--;
            return true;
        }
        return false;
    }


    /**
     * @param key
     *            - if one parameter is given then set ShouldPrint to
     *            false
     * 
     * @return search method itself, which will use the false boolean as default
     */

    public int search(int key) {

        return search(key, false);
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

    public int search(int key, boolean shouldPrint) {

        int hash1 = hash1(key);
        int hash2 = hash2(key);
        // Keep track of search attempts.
        int tries = 0;
        /// Casting int key to Integer so that the comparison runs
        while (keyTable[hash1] != (Integer)key && tries < size) {
            hash1 += hash2;
            hash1 %= size;
            tries++;
        }
        if (keyTable[hash1] == (Integer)key) {
            if (shouldPrint) {
                Util.print("Found record with ID " + keyTable[hash1].toString()
                    + ":");
            }
            return hash1;
        }
        if (shouldPrint) {
            Util.print("Search FAILED -- There is no record with ID " + key);
        }
        return -1;
    }


    /** prints the whole hasTable */
    /** print the Hashtable is the value is in there */
    public void printHashTable() {
        int i = 0;
        int itemCount = 0;
        while (i < size) {
            if (keyTable[i] != null) {
                if (keyTable[i] != -1) {
                    Util.print(i + ": " + keyTable[i]);
                    itemCount++;
                }
                else {
                    Util.print(i + ": TOMBSTONE");
                }
            }
            i++;
        }
        Util.print("total records: " + itemCount);
    }
}

/**
 * Provides an interface for hashable entries, defining methods necessary
 * for managing entries within a hash table. This interface is intended to
 * support handling generic entries which contain a seminar ID and a handle
 * to a location in memory. The interface allows retrieval of seminar IDs
 * and their corresponding memory handles, facilitating operations like
 * insertion, deletion, and searches within hash-based data structures.
 */

/**
 * This is the HashableEntry class for the hash table
 * 
 * @author {Ibrahim Khalilov ibrahimk}
 * @version 2024-04-25
 */
public interface HashableEntry {

    /**
     * Retrieves the seminar ID associated with this entry. The seminar ID
     * is used as a unique identifier for hash table operations.
     *
     * @return the integer ID of the seminar, used as a key in hash operations
     */
    public int getSeminarId();


    /**
     * Retrieves the handle associated with this entry. The handle typically
     * points to a location in memory where the seminar's data is stored,
     * allowing direct access to the data in a memory management system.
     *
     * @return the Handle object pointing to the memory location of the
     *         seminar's data
     */
    public Handle getHandle();
}

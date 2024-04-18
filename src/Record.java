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
 * This is the Record class the hash table returns
 * 
 * @author {Ibrahim Khalilov ibrahimk}
 * @version 2023-09-04
 */
public class Record implements HashableEntry {
    Handle handle;
    int seminarId;

    public Record(int seminarId, Handle handle) {
        this.seminarId = seminarId;
        this.handle = handle;
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




/**
 * This is the Handle class the MemoryManager returns
 * 
 * @author {Ibrahim Khalilov ibrahimk}
 * @version 2023-09-04
 */
class Handle {
    /**
     * Position that has the start of the memory block.
     */
    private int startingPos;
    private int length;

    /**
     * This method will initialize startPos and length
     * 
     * @param startPos
     *            Starting position in memory
     * @param length
     *            length of the block in memory
     */
    Handle(int startPos, int length) {
        /** constructing */
        this.startingPos = startPos;
        this.length = length;
    }


    /**
     * 
     * @return length of the memory block (the size on disk). this will have the
     *         space occupied if filled; otherwise the maximum size this block
     *         can hold
     */
    public int getLength() {
        return length;
    }


    /**
     * Sets the length of a block of memory
     * 
     * @param length
     *            is the new length of this handle
     */
    protected void setLength(int length) {
        this.length = length;
    }


    /**
     * This method will get the start of the memory block
     * 
     * @return start of the memory block
     */
    public int getStartingPos() {
        return startingPos;
    }


    /**
     * This method will set the start of the location
     * 
     * @param startingPos
     *            start of memory block
     */
    public void setStartingPos(int startingPos) {
        this.startingPos = startingPos;
    }
}

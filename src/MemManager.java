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
public class MemManager {
    private byte[] memoryPool;
    private LinkedList[] freeLists;
    private int maxPower;

    /**
     * @param initialMemorySize
     *            - the size of the initialized array
     *            class definition for the Memory Manager
     */
    public MemManager(int initialSize) {
        this.memoryPool = new byte[initialSize];
        this.maxPower = (int)(Math.log(initialSize) / Math.log(2));
        this.freeLists = new LinkedList[maxPower + 1];

        for (int i = 0; i <= maxPower; i++) {
            freeLists[i] = new LinkedList();
        }

        // Initially, the whole memory is a free block
        freeLists[maxPower].add(0, initialSize);
    }


    public Handle allocate(int size) {
        int requiredPower = 0;
        while ((1 << requiredPower) < size)
            requiredPower++;
        int blockSize = 1 << requiredPower;

        for (int i = requiredPower; i <= maxPower; i++) {
            if (!freeLists[i].isEmpty()) {
                // Find and remove the first block of this size
                ListNode block = freeLists[i].findAndRemove(1 << i);
                int currentSize = 1 << i;
                while (currentSize > blockSize) {
                    currentSize >>= 1;
                    freeLists[requiredPower].add(block.start + currentSize,
                        currentSize);
                }
                return new Handle(block.start, blockSize);
            }
        }
        return null; // Not enough memory
    }


    public void free(Handle handle) {
        int blockPower = 0;
        while ((1 << blockPower) != handle.getLength())
            blockPower++;
        int buddyStart = handle.getStartingPos() ^ (1 << blockPower);

        // Try to find and merge with buddy
        ListNode buddy = null;
        for (ListNode node = freeLists[blockPower].head; node != null; node =
            node.next) {
            if (node.start == buddyStart) {
                buddy = node;
                break;
            }
        }

        if (buddy != null) {
            freeLists[blockPower].remove(buddy.start, buddy.size);
            // Merge and free at a higher level
            free(new Handle(Math.min(handle.getStartingPos(), buddy.start),
                handle.getLength() * 2));
        }
        else {
            freeLists[blockPower].add(handle.getStartingPos(), handle
                .getLength());
        }
    }
}

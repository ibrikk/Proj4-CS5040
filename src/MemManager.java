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
    public MemManager(int poolSize) {
        this.memoryPool = new byte[poolSize];
        this.maxPower = (int)(Math.log(poolSize) / Math.log(2));
        this.freeLists = new LinkedList[maxPower + 1];
        for (int i = 0; i <= maxPower; i++) {
            freeLists[i] = new LinkedList();
        }
        freeLists[maxPower].add(0, poolSize);
    }


    public Handle insert(byte[] space, int size) {
        int requiredPower = 0;
        while ((1 << requiredPower) < size)
            requiredPower++;
        int blockSize = 1 << requiredPower;

        for (int i = requiredPower; i <= maxPower; i++) {
            if (!freeLists[i].isEmpty()) {
                // Split blocks until the correct size is achieved
                while (i > requiredPower) {
                    ListNode block = freeLists[i].findAndRemove(1 << i);
                    int currentSize = 1 << i;
                    currentSize >>= 1;
                    freeLists[i - 1].add(block.getStart(), currentSize);
                    freeLists[i - 1].add(block.getStart() + currentSize,
                        currentSize);
                    i--;
                }
                ListNode block = freeLists[requiredPower].findAndRemove(
                    1 << requiredPower);
                System.arraycopy(space, 0, memoryPool, block.getStart(), size);
                return new Handle(block.getStart(), size);
            }
        }
        return null; // Not enough memory
    }


    public int length(Handle theHandle) {
        return theHandle.getLength();
    }


    public void remove(Handle theHandle) {
        int blockPower = (int)(Math.log(theHandle.getLength()) / Math.log(2));
        int buddyStart = theHandle.getStartingPos() ^ (1 << blockPower);

        ListNode buddy = null;
        for (ListNode node = freeLists[blockPower].head; node != null; node =
            node.getNext()) {
            if (node.getStart() == buddyStart) {
                buddy = node;
                break;
            }
        }

        if (buddy != null) {
            freeLists[blockPower].remove(buddy.getStart(), buddy.getSize());
            // Merge and free at a higher level
            remove(new Handle(Math.min(theHandle.getStartingPos(), buddy
                .getStart()), theHandle.getLength() * 2));
        }
        else {
            freeLists[blockPower].add(theHandle.getStartingPos(), theHandle
                .getLength());
        }
    }


    public int get(byte[] space, Handle theHandle, int size) {
        int bytesToCopy = Math.min(size, theHandle.getLength());
        System.arraycopy(memoryPool, theHandle.getStartingPos(), space, 0,
            bytesToCopy);
        return bytesToCopy;
    }


    public void dump() {
        for (int i = 0; i <= maxPower; i++) {
            System.out.print("Size " + (1 << i) + ": ");
            ListNode current = freeLists[i].head;
            while (current != null) {
                System.out.print("(" + current.getStart() + ", " + current
                    .getSize() + ") ");
                current = current.getNext();
            }
            System.out.println();
        }
    }

// // Writes data to the allocated block
// public void writeData(Handle handle, byte[] data) {
// if (data.length > handle.getLength()) {
// throw new IllegalArgumentException("Data exceeds block size.");
// }
// System.arraycopy(data, 0, memoryPool, handle.getStartingPos(),
// data.length);
// }
//
//
// // Reads data from the block
// public byte[] readData(Handle handle) {
// byte[] data = new byte[handle.getLength()];
// System.arraycopy(memoryPool, handle.getStartingPos(), data, 0, handle
// .getLength());
// return data;
// }
}

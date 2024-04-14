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
        if (!Util.isPowerOfTwo(poolSize)) {
            Util.print("Pool size is not power of two!");
            return;
        }
        this.memoryPool = new byte[poolSize];
        this.maxPower = (int)(Math.log(poolSize) / Math.log(2));
        this.freeLists = new LinkedList[maxPower];
        for (int i = 0; i < maxPower; i++) {
            freeLists[i] = new LinkedList();
        }
        freeLists[maxPower - 1].add(0, poolSize);
    }


    public Handle insert(byte[] space) {
        int requiredPower = 0;
        while ((1 << requiredPower) < space.length) {
            requiredPower++;
        }
        int requiredSize = 1 << requiredPower;

        // Attempt to find or split a block recursively starting from the
        // required power.
        Handle handle = findOrSplit(requiredPower - 1, maxPower - 1);
        if (handle != null) {
            System.arraycopy(space, 0, memoryPool, handle.getStartingPos(),
                space.length);
        }
        return handle;
    }


    private Handle findOrSplit(int requiredPowerIndex, int currentPowerIndex) {
        if (currentPowerIndex < requiredPowerIndex) {
            // Base case: No block small enough is available.
            return null;
        }

        if (!freeLists[currentPowerIndex].isEmpty()) {
            int currentPower = currentPowerIndex + 1;
            int currBlockSize = 1 << currentPower;
            ListNode block = freeLists[currentPowerIndex].findAndRemove(
                currBlockSize);
            if (block != null) {
                if (currentPowerIndex == requiredPowerIndex) {
                    // Correct size block is found, return a new handle.
                    return new Handle(block.getStart(), 1 << currentPower);
                }

                // Split the block into two halves
                int newSize = 1 << currentPowerIndex;
                int newStart = block.getStart() + newSize;
                freeLists[currentPowerIndex - 1].add(newStart, newSize);
                freeLists[currentPowerIndex - 1].add(block.getStart(), newSize);

                // Recursively try to find or split the block in the smaller
                // size list
                return findOrSplit(requiredPowerIndex, currentPowerIndex - 1);
            }
        }

        // Recursively check the next larger block
        return findOrSplit(requiredPowerIndex, currentPowerIndex - 1);
    }


// TODO: Better testing required
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
        for (int i = 1; i <= maxPower; i++) {
            System.out.print((1 << i) + ": ");
            ListNode current = freeLists[i].head;
            while (current != null) {
                System.out.print(current.getStart() + " " + current.getSize());
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

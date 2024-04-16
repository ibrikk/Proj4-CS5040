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


    public int getMemoryPoolLength() {
        return memoryPool.length;
    }


    public Handle insert(byte[] space) {
        int requiredPower = 0;
        while ((1 << requiredPower) < space.length) {
            requiredPower++;
        }
        int requiredSize = 1 << requiredPower;

        // Attempt to find or split a block recursively starting from the
        // required power.
        Handle handle = findOrSplit(requiredPower - 1);
        if (handle != null) {
            System.arraycopy(space, 0, memoryPool, handle.getStartingPos(),
                space.length);
        }
        else {
            expandMemoryPool();
            // After expanding, try to insert again
            handle = findOrSplit(requiredPower - 1);
            Util.print("Memory pool expanded to " + memoryPool.length
                + " bytes");
            if (handle == null) {
                Util.print(
                    "Failed to allocate memory even after expanding the memory pool.");
            }
        }
        return handle;
    }


    private Handle findOrSplit(int requiredPowerIndex) {
        // Attempt to find a suitable block from the smallest necessary size
        // upwards
        for (int currentPowerIndex =
            requiredPowerIndex; currentPowerIndex < maxPower; currentPowerIndex++) {
            if (!freeLists[currentPowerIndex].isEmpty()) {
                ListNode block = freeLists[currentPowerIndex].findAndRemove(
                    1 << (currentPowerIndex + 1));
                if (block != null) {
                    // If found the exact needed block size, return the handle
                    if (currentPowerIndex == requiredPowerIndex) {
                        return new Handle(block.getStart(),
                            1 << (currentPowerIndex + 1));
                    }

                    int targetSize = 1 << (requiredPowerIndex + 1);
                    int start = block.getStart();
                    int size = 1 << (currentPowerIndex + 1);

                    // Split the block iteratively until the block size matches
                    // the target size
                    while (size > targetSize) {
                        size >>= 1; // Halve the block size
                        int nextStart = start + size;

                        // Add the second half to the free list
                        freeLists[currentPowerIndex - 1].add(nextStart, size);
                        currentPowerIndex--;
                    }

                    // Now the block size equals the target size, return this
                    // newly sized block
                    return new Handle(start, size);
                }
            }
        }

        // If no suitable block is found after checking all levels up to
        // maxPower, return null
        return null;
    }


    private void expandMemoryPool() {
        int newSize = memoryPool.length * 2;
        byte[] newMemoryPool = new byte[newSize];
        System.arraycopy(memoryPool, 0, newMemoryPool, 0, memoryPool.length);
        memoryPool = newMemoryPool; // Replace old memory pool with the new one

        // Update maxPower and adjust freeLists for the new size
        maxPower++;
        LinkedList[] newFreeLists = new LinkedList[maxPower];
        System.arraycopy(freeLists, 0, newFreeLists, 0, freeLists.length);
        newFreeLists[maxPower - 1] = new LinkedList();

        freeLists = newFreeLists;

        // Add the new additional memory as a free block
        // TODO: Fix this index issue
        freeLists[maxPower - 2].add(memoryPool.length / 2, memoryPool.length
            / 2);
    }

// TODO: Better testing required
// public void remove(Handle theHandle) {
// int blockPower = (int)(Math.log(theHandle.getLength()) / Math.log(2));
// int buddyStart = theHandle.getStartingPos() ^ (1 << blockPower);
//
// ListNode buddy = null;
// for (ListNode node = freeLists[blockPower].head; node != null; node =
// node.getNext()) {
// if (node.getStart() == buddyStart) {
// buddy = node;
// break;
// }
// }
//
// if (buddy != null) {
// freeLists[blockPower].remove(buddy.getStart(), buddy.getSize());
// // Merge and free at a higher level
// remove(new Handle(Math.min(theHandle.getStartingPos(), buddy
// .getStart()), theHandle.getLength() * 2));
// }
// else {
// freeLists[blockPower].add(theHandle.getStartingPos(), theHandle
// .getLength());
// }
// }
//
//
// public int get(byte[] space, Handle theHandle, int size) {
// int bytesToCopy = Math.min(size, theHandle.getLength());
// System.arraycopy(memoryPool, theHandle.getStartingPos(), space, 0,
// bytesToCopy);
// return bytesToCopy;
// }


    public void dump() {
        for (int i = 1; i < maxPower; i++) {
            ListNode current = freeLists[i].head;
            if (current != null) {
                System.out.print((1 << (i + 1)) + ": ");
            }
            while (current != null) {
                System.out.print(current.getStart());
                current = current.getNext();
                if (current == null) {
                    System.out.println();
                }
            }
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

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
        Handle handle = findOrSplit(requiredPower - 1, space.length);
        if (handle != null) {
            System.arraycopy(space, 0, memoryPool, handle.getStartingPos(),
                space.length);
        }
        else {
            expandMemoryPool();
            // After expanding, try to insert again
            handle = findOrSplit(requiredPower - 1, space.length);
            Util.print("Memory pool expanded to " + memoryPool.length
                + " bytes");
            if (handle == null) {
                Util.print(
                    "Failed to allocate memory even after expanding the memory pool.");
            }
            else {
                System.arraycopy(space, 0, memoryPool, handle.getStartingPos(),
                    space.length);
            }

        }
        return handle;
    }


    private Handle findOrSplit(int requiredPowerIndex, int spaceLength) {
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
                        return new Handle(block.getStart(), spaceLength);
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
                    return new Handle(start, spaceLength);
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
        freeLists[maxPower - 2].add(memoryPool.length / 2, memoryPool.length
            / 2);
    }


    public byte[] readBytes(Handle handle) {
        byte[] data = new byte[handle.getLength()];
        System.arraycopy(memoryPool, handle.getStartingPos(), data, 0, handle
            .getLength());
        return data;
    }


// TODO: Better testing required
    public void remove(Handle theHandle) {
        int blockPower = (int)(Math.log(theHandle.getLength()) / Math.log(2));
        int buddySize = (1 << (blockPower + 1));
        int buddyStart = theHandle.getStartingPos() ^ buddySize;

        ListNode buddy = findAndRemoveFromList(buddyStart, buddySize,
            blockPower);
        if (buddy != null) {
            // Buddy found and removed, merge them
            int mergedStart = Math.min(theHandle.getStartingPos(), buddyStart);
            int mergedSize = theHandle.getLength() * 2;

            // Create a new handle for the merged block and attempt further
            // merging
            Handle mergedHandle = new Handle(mergedStart, mergedSize);
            remove(mergedHandle); // Recursive call to try further merging
        }
        else {
            // No buddy found, add the current block to the free list
            addToFreeList(theHandle.getStartingPos(), theHandle.getLength(),
                blockPower);

            clearMemory(theHandle.getStartingPos(), theHandle.getLength());
        }
    }


    private ListNode findAndRemoveFromList(
        int start,
        int size,
        int powerIndex) {
        ListNode current = freeLists[powerIndex].head;
        ListNode prev = null;
        while (current != null) {
            if (current.getStart() == start && current.getSize() == size) {
                if (prev == null) {
                    freeLists[powerIndex].head = current.getNext();
                }
                else {
                    prev.setNext(current.getNext());
                }
                return current;
            }
            prev = current;
            current = current.getNext();
        }
        return null; // Buddy not found
    }


    private void addToFreeList(int start, int size, int powerIndex) {
        freeLists[powerIndex].add(start, size);
    }


    private void clearMemory(int start, int length) {
        for (int i = start; i < start + length; i++) {
            memoryPool[i] = 0;
        }
    }


    public void dump() {
        boolean isFreeListEmpty = true;
        for (int i = 1; i < maxPower; i++) {
            ListNode current = freeLists[i].head;
            if (current != null) {
                if (isFreeListEmpty) {
                    isFreeListEmpty = false;
                }
                System.out.print((1 << (i + 1)) + ":");
            }
            while (current != null) {
                System.out.print(" " + current.getStart());
                current = current.getNext();
                if (current == null) {
                    Util.print();
                }
            }
        }
        if (isFreeListEmpty) {
            Util.print("There are no freeblocks in the memory pool");
        }
    }


    public int getLargestFreeBlockSize() {
        int largestSize = 0;
        for (LinkedList list : freeLists) {
            ListNode node = list.head;
            while (node != null) {
                if (node.getSize() > largestSize) {
                    largestSize = node.getSize();
                }
                node = node.getNext();
            }
        }
        return largestSize;
    }

}

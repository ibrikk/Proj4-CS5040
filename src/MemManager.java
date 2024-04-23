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
        this.maxPower = calculateMaxPower(poolSize);
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
        int requiredPower = calculateMaxPower(space.length);
        // Attempt to find or split a block recursively starting from the
        // required power.
        Handle handle = findOrSplit(requiredPower - 1, space.length);
        if (handle != null) {
            System.arraycopy(space, 0, memoryPool, handle.getStartingPos(),
                space.length);
        }
        else {
            while (handle == null) {
                expandMemoryPool();
                // After expanding, try to insert again
                handle = findOrSplit(requiredPower - 1, space.length);
            }
            Util.print("Memory pool expanded to " + memoryPool.length
                + " bytes");

            System.arraycopy(space, 0, memoryPool, handle.getStartingPos(),
                space.length);

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
        int oldSize = memoryPool.length;
        int newSize = oldSize * 2;
        byte[] newMemoryPool = new byte[newSize];
        System.arraycopy(memoryPool, 0, newMemoryPool, 0, oldSize);
        memoryPool = newMemoryPool; // Replace old memory pool with the new one

        // Update maxPower and adjust freeLists for the new size
        maxPower++;
        LinkedList[] newFreeLists = new LinkedList[maxPower];
        System.arraycopy(freeLists, 0, newFreeLists, 0, freeLists.length);
        newFreeLists[maxPower - 1] = new LinkedList();
        newFreeLists[maxPower - 2].add(oldSize, oldSize);

        freeLists = newFreeLists;
    }


    public byte[] readBytes(Handle handle) {
        byte[] data = new byte[handle.getLength()];
        System.arraycopy(memoryPool, handle.getStartingPos(), data, 0, handle
            .getLength());
        return data;
    }


    public void remove(Handle theHandle) {
        int length = theHandle.getLength();
        int blockPower = calculateBlockSize(length);

        // Convert blockPower from size to the exponent
        int power = Integer.numberOfTrailingZeros(blockPower);
        // Buddy size should be exactly the block size
        int buddySize = blockPower;
        int buddyStart = theHandle.getStartingPos() ^ buddySize;

        ListNode buddy = findAndRemoveFromList(buddyStart, buddySize, power
            - 1);
        if (buddy != null) {
            int mergedStart = Math.min(theHandle.getStartingPos(), buddyStart);
            int mergedSize = theHandle.getLength() * 2;
            Handle mergedHandle = new Handle(mergedStart, mergedSize);
            remove(mergedHandle);
        }
        else {
            addToFreeList(theHandle.getStartingPos(), theHandle.getLength(),
                power - 1);
            clearMemory(theHandle.getStartingPos(), theHandle.getLength());
        }
    }


    private ListNode findAndRemoveFromList(
        int start,
        int size,
        int powerIndex) {
        ListNode current = freeLists[powerIndex].getHead();
        ListNode prev = null;
        while (current != null) {
            if (current.getStart() == start && current.getSize() == size) {
                if (prev == null) {
                    freeLists[powerIndex].setHead(current.getNext());
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
        ListNode newNode = new ListNode(start, size);
        ListNode current = freeLists[powerIndex].getHead();
        ListNode prev = null;

        // Traverse the list to find the insertion point
        while (current != null && current.getStart() < start) {
            prev = current;
            current = current.getNext();
        }

        // Insert the new node at the found position
        newNode.setNext(current);
        if (prev == null) {
            // Inserting at the head of the list
            freeLists[powerIndex].setHead(newNode);
        }
        else {
            // Inserting somewhere after the head
            prev.setNext(newNode);
        }
    }


    private void clearMemory(int start, int length) {
        for (int i = start; i < start + length; i++) {
            memoryPool[i] = 0;
        }
    }


    private int calculateBlockSize(int length) {
        if (length <= 0) {
            return 0;
        }
        int highestOneBit = Integer.highestOneBit(length);
        return (highestOneBit == length) ? highestOneBit : highestOneBit << 1;
    }


    public int calculateMaxPower(int size) {
        int power = 0;
        while ((1 << power) < size) {
            power++;
        }
        return power; // This returns the index, not the size.
    }


    public void dump() {
        boolean isFreeListEmpty = true;
        for (int i = 1; i < maxPower; i++) {
            ListNode current = freeLists[i].getHead();
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
            ListNode node = list.getHead();
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

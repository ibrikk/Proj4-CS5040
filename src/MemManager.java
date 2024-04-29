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
 * @version 2024-04-25
 */
public class MemManager {
    private byte[] memoryPool;
    private LinkedList[] freeLists;
    private int maxPower;

    /**
     * @param poolSize
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


    /**
     * Retrieves the length of the memory pool.
     *
     * @return the length of the memory pool
     */
    public int getMemoryPoolLength() {
        return memoryPool.length;
    }


    /**
     * Inserts a byte array into the memory pool and returns a handle to the
     * allocated memory.
     *
     * @param space
     *            the byte array to be inserted
     * @return a handle to the allocated memory, or null if allocation fails
     *         after multiple expansions
     */
    public Handle insert(byte[] space) {
        int requiredPower = calculateMaxPower(space.length);
        Handle handle = insertRecursive(requiredPower - 1, space);
        if (handle != null) {
            System.arraycopy(space, 0, memoryPool, handle.getStartingPos(),
                space.length);
        }
        else {
            Util.print("Failed to allocate memory after multiple expansions.");
        }
        return handle;
    }


    /**
     * Recursively attempts to insert a byte array into the memory pool.
     *
     * @param requiredPowerIndex
     *            the index of the required power for the allocation
     * @param space
     *            the byte array to be inserted
     * @return a handle to the allocated memory, or null if allocation fails
     */
    private Handle insertRecursive(int requiredPowerIndex, byte[] space) {
        Handle handle = findOrSplit(requiredPowerIndex, space.length);
        if (handle == null) {
            // Always attempt to expand if needed
            expandMemoryPool(space.length);
            // Retry after expanding
            return insertRecursive(requiredPowerIndex, space);
        }
        return handle;
    }


    /**
     * Expands the memory pool by doubling its size and adjusting the free lists
     * accordingly.
     */
    private void expandMemoryPool(int spaceLength) {
        int oldSize = memoryPool.length;
        int newSize = oldSize * 2;
        byte[] newMemoryPool = new byte[newSize];
        System.arraycopy(memoryPool, 0, newMemoryPool, 0, oldSize);
        // Replace old memory pool with the new one
        memoryPool = newMemoryPool;

        // Update maxPower and adjust freeLists for the new size
        maxPower++;
        LinkedList[] newFreeLists = new LinkedList[maxPower];
        System.arraycopy(freeLists, 0, newFreeLists, 0, freeLists.length);
        newFreeLists[maxPower - 1] = new LinkedList();
        // Add the new large block at the end
        int p = 1 << (maxPower - 1);

        if (spaceLength > p) {
            ListNode removedNode = newFreeLists[maxPower - 2].findAndRemove(
                oldSize);
            newFreeLists[maxPower - 1].add(removedNode.getStart(), newSize);
        }
        else {
            newFreeLists[maxPower - 2].add(oldSize, oldSize);
        }

        freeLists = newFreeLists;
        Util.print("Memory pool expanded to " + memoryPool.length + " bytes");
    }


    /**
     * Finds or splits a block of the required size from the free lists.
     *
     * @param requiredPowerIndex
     *            the index of the required power for the allocation
     * @param spaceLength
     *            the length of the space to be allocated
     * @return a handle to the allocated memory, or null if no suitable block is
     *         found
     */
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


    /**
     * Reads the bytes from the memory pool based on the provided handle.
     *
     * @param handle
     *            the handle to the allocated memory
     * @return the byte array containing the data from the memory pool
     */
    public byte[] readBytes(Handle handle) {
        byte[] data = new byte[handle.getLength()];
        System.arraycopy(memoryPool, handle.getStartingPos(), data, 0, handle
            .getLength());
        return data;
    }


    /**
     * Removes (deallocates) the memory block associated with the provided
     * handle.
     *
     * @param theHandle
     *            the handle to the memory block to be removed
     */
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
            int mergedSize = buddySize * 2;
            Handle mergedHandle = new Handle(mergedStart, mergedSize);
            remove(mergedHandle);
        }
        else {
            addToFreeList(theHandle.getStartingPos(), buddySize, power - 1);
            clearMemory(theHandle.getStartingPos(), theHandle.getLength());
        }
    }


    /**
     * Finds and removes a specific block from the free list at the given power
     * index.
     *
     * @param start
     *            the starting position of the block
     * @param size
     *            the size of the block
     * @param powerIndex
     *            the index of the power for the free list
     * @return the removed list node, or null if the block is not found
     */
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


    /**
     * Adds a block to the free list at the given power index.
     *
     * @param start
     *            the starting position of the block
     * @param size
     *            the size of the block
     * @param powerIndex
     *            the index of the power for the free list
     */
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


    /**
     * Clears the memory block by setting all its bytes to zero.
     *
     * @param start
     *            the starting position of the block
     * @param length
     *            the length of the block
     */
    private void clearMemory(int start, int length) {
        for (int i = start; i < start + length; i++) {
            memoryPool[i] = 0;
        }
    }


    /**
     * Calculates the block size based on the given length.
     *
     * @param length
     *            the length of the block
     * @return the calculated block size
     */
    private int calculateBlockSize(int length) {
        if (length <= 0) {
            return 0;
        }
        int highestOneBit = Integer.highestOneBit(length);
        return (highestOneBit == length) ? highestOneBit : highestOneBit << 1;
    }


    /**
     * Calculates the maximum power (index) for the given size.
     *
     * @param size
     *            the size for which to calculate the maximum power
     * @return the maximum power (index)
     */
    public int calculateMaxPower(int size) {
        int power = 0;
        while ((1 << power) < size) {
            power++;
        }
        return power; // This returns the index, not the size.
    }


    /**
     * Dumps the free lists, displaying the sizes and starting positions of the
     * free blocks.
     */
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


    /**
     * Retrieves the size of the largest free block in the memory pool.
     *
     * @return the size of the largest free block
     */
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

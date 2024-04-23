import org.junit.Before;
import org.junit.Test;
import student.TestCase;

/**
 * Test the MemManager class
 *
 * @author Ibrahim Khalilov - ibrahimk
 * @version 2023-09-06
 */

public class MemManagerTest extends TestCase {

    private MemManager manager;

    /**
     * Before annotation runs before each test
     */
    @Before
    public void setUp() {
        manager = new MemManager(1024);
    }


    /**
     * Test that a simple insertion is handled correctly.
     */
    @Test
    public void testSimpleInsert() {
        byte[] data = new byte[256]; // Less than 1024 and a power of two
        Handle handle = manager.insert(data);
        assertNotNull("Handle should not be null after insertion", handle);
        assertEquals("The starting position should be 0", 0, handle
            .getStartingPos());
        assertEquals("The size of the handle should be 256", 256, handle
            .getLength());
    }


    /**
     * Test that the memory pool expands when necessary.
     */
    @Test
    public void testMemoryExpansion() {
        byte[] data = new byte[1024]; // Exactly fill the initial pool
        manager.insert(data);
        byte[] moreData = new byte[512]; // Requires expansion
        Handle handle = manager.insert(moreData);
        assertNotNull("Handle should not be null after expansion", handle);
        assertTrue("Memory pool should expand to at least 2048 bytes", manager
            .getMemoryPoolLength() >= 2048);
    }


    /**
     * Test the correct handling and splitting of blocks.
     */
    @Test
    public void testBlockSplitting() {
        byte[] firstInsert = new byte[512]; // Use half the pool
        byte[] secondInsert = new byte[256]; // Use a quarter of the pool
        manager.insert(firstInsert);
        Handle secondHandle = manager.insert(secondInsert);

        assertNotNull("Second handle should not be null", secondHandle);
        assertEquals("Second handle should have correct size", 256, secondHandle
            .getLength());
        // This assumes block placement logic; adjust according to actual
        // strategy
        assertEquals("Second handle should start at 512", 512, secondHandle
            .getStartingPos());
    }


    /**
     * Test inserting more than the initial capacity to check multiple
     * expansions.
     */
    @Test
    public void testMultipleExpansions() {
        for (int i = 0; i < 10; i++) { // Ten inserts, each requiring more
                                       // memory
            byte[] data = new byte[1024]; // Each insert is the size of the
                                          // initial pool
            Handle handle = manager.insert(data);
            assertNotNull("Handle should not be null on insert " + (i + 1),
                handle);
        }
        assertTrue(
            "Memory pool should expand to accommodate multiple large inserts",
            manager.getMemoryPoolLength() >= 1024 * 11);
    }


    /**
     * Test inserting a block and then removing it.
     */
    @Test
    public void testInsertAndRemove() {
        byte[] data = new byte[128]; // Smaller than the total size
        for (int i = 0; i < data.length; i++) {
            data[i] = 1;
        }
        Handle handle = manager.insert(data);
        assertNotNull("Handle should not be null after insertion", handle);

        manager.remove(handle);
        // Check if memory is cleared
        byte[] clearedMemory = manager.readBytes(handle);
        assertTrue("Memory should be cleared after removal", Util
            .isMemoryCleared(clearedMemory));
    }


    /**
     * Test removing a block and verifying if it merges with its buddy
     * correctly.
     */
    @Test
    public void testMergeWithBuddy() {
        byte[] data = new byte[256];
        for (int i = 0; i < data.length; i++) {
            data[i] = 1;
        }
        Handle handle1 = manager.insert(data);
        Handle handle2 = manager.insert(data);

        manager.remove(handle1);

        assertEquals("Blocks should merge to form a larger free block", 512,
            manager.getLargestFreeBlockSize());

        manager.remove(handle2); // Should trigger merging

        assertEquals("Blocks should merge to form a larger free block", 1024,
            manager.getLargestFreeBlockSize());
    }


    /**
     * Test that inserting more than the available memory triggers an expansion.
     */
    @Test
    public void testMemoryExpansion2() {
        byte[] largeData = new byte[2048]; // Larger than the initial size
        Handle handle = manager.insert(largeData);
        assertNull("Memory should expand to accommodate large blocks", handle);
        assertTrue("Memory pool should expand", manager
            .getMemoryPoolLength() > 1024);
    }

    /**
     * Test and use empty memory pool
     */
    @Test
    public void testUseAndEmptyMemoryPool() {
        int blockSize = 64;
        int numBlocks = 1024 / blockSize;

        // Allocate all memory
        Handle[] handles = new Handle[numBlocks];
        for (int i = 0; i < numBlocks; i++) {
            handles[i] = manager.insert(new byte[blockSize]);
            assertNotNull("Handle should not be null after insertion",
                handles[i]);
        }

        // Free all memory
        for (Handle handle : handles) {
            manager.remove(handle);
        }

        // Check if the entire memory is available again
        assertEquals("All memory should be free after removals", 1024, manager
            .getLargestFreeBlockSize());
    }

}

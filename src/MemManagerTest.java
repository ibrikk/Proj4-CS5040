import org.junit.Before;
import org.junit.Test;
import student.TestCase;
import java.util.Random;

/**
 * Test the MemManager class
 *
 * @author Ibrahim Khalilov - ibrahimk
 * @version 2024-04-20
 */

public class MemManagerTest extends TestCase {

    private MemManager manager;

    /**
     * Sets up the environment before each test, initializing a MemManager with
     * a specific
     * memory pool size.
     */
    @Before
    public void setUp() {
        manager = new MemManager(1024);
    }


    /**
     * Tests simple insertion into the memory manager to ensure a block can be
     * allocated
     * without the need for splitting.
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
        assertTrue("Memory pool should expand", manager
            .getMemoryPoolLength() > 1024);
    }


    /**
     * Test and use empty memory pool
     */
    @Test
    public void testUseAndEmptyMemoryPool() {
        int blockSize = 64;
        byte[] data = new byte[blockSize];
        for (int i = 0; i < data.length; i++) {
            data[i] = 1; // Initialize all bytes in the block to 1.
        }
        int numBlocks = 1024 / blockSize;

        // Allocate all memory
        Handle[] handles = new Handle[numBlocks];
        for (int i = 0; i < numBlocks; i++) {
            handles[i] = manager.insert(data);
            assertNotNull("Handle should not be null after insertion",
                handles[i]);
        }

        // Randomize the order of handles using a
        // simple shuffle algorithm
        Random rand = new Random();
        for (int i = 0; i < handles.length; i++) {
            // Generate a random index
            int randomIndex = rand.nextInt(handles.length);
            // Swap handles[i] and handles[randomIndex]
            Handle temp = handles[i];
            handles[i] = handles[randomIndex];
            handles[randomIndex] = temp;
        }
        // Free all memory in random order
        for (Handle handle : handles) {
            manager.remove(handle);
        }

        // Check if the entire memory is available again
        assertEquals("All memory should be free after removals", 1024, manager
            .getLargestFreeBlockSize());
    }


    /**
     * Test that uses all of the memory pool then empties it.
     */
    @Test
    public void testMemManagerFullEmpty() {
        byte[] data = new byte[1024];
        for (int i = 0; i < data.length; i++) {
            data[i] = 1;
        }
        Handle h = manager.insert(data);
        assertEquals(manager.getLargestFreeBlockSize(), 0);
        assertEquals(manager.getMemoryPoolLength(), 1024);

        manager.remove(h);
        assertEquals(manager.getLargestFreeBlockSize(), 1024);
    }


    /**
     * Test another insert.
     */
    @Test
    public void testSimpleInsert2() {
        byte[] block = new byte[128];
        Handle handle = manager.insert(block);
        assertNotNull(handle);
        assertEquals(128, handle.getLength());
    }


    /**
     * Tests the behavior of the memory manager when a block needs to be split
     * to fit a
     * smaller request after a larger block has been previously inserted.
     */
    @Test
    public void testSplittingBehavior() {
        byte[] block = new byte[512];
        manager.insert(block); // This should fit directly
        byte[] smallerBlock = new byte[256];
        manager.insert(smallerBlock); // This will require splitting the
                                      // remaining 512 block
        Handle handle = manager.insert(smallerBlock);
        assertNotNull(handle);
        assertEquals(256, handle.getLength());
    }


    /**
     * Tests non-sequential insertion to ensure the memory manager correctly
     * handles
     * allocations of various sizes in an unpredictable order.
     */
    @Test
    public void testNonSequentialInsertion() {
        manager.insert(new byte[256]);
        manager.insert(new byte[512]);
        Handle handleSmall = manager.insert(new byte[64]);
        Handle handleLarge = manager.insert(new byte[128]);

        assertNotNull(handleSmall);
        assertNotNull(handleLarge);
        assertFalse(handleSmall.getStartingPos() == handleLarge
            .getStartingPos());
    }


    /**
     * Tests the memory manager's ability to handle filling the memory to
     * capacity and
     * then correctly expanding the memory pool when needed.
     */
    @Test
    public void testFullCapacityAndExpansion() {
        // Completely fill the memory
        for (int i = 0; i < 8; i++) {
            manager.insert(new byte[128]);
        }
        // Force an expansion
        Handle expansionHandle = manager.insert(new byte[128]);
        assertNotNull(expansionHandle);
        assertTrue(manager.getMemoryPoolLength() > 1024);
    }


    /**
     * Tests randomized insertion and deletion to simulate a complex real-world
     * scenario
     * and ensure that the memory manager can handle dynamic changes in memory
     * usage.
     */
    @Test
    public void testRandomizedInsertionAndDeletion() {
        Handle[] handles = new Handle[10];
        for (int i = 0; i < handles.length; i++) {
            handles[i] = manager.insert(new byte[(int)(Math.random() * 100)
                + 50]);
        }

        for (int i = 0; i < handles.length; i++) {
            if (Math.random() > 0.5) {
                manager.remove(handles[i]);
            }
        }

        int largestFreeBlock = manager.getLargestFreeBlockSize();
        assertTrue(largestFreeBlock >= 50);
    }

}

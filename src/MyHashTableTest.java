import student.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the CommandProcessor class
 *
 * @author Ibrahim Khalilov - ibrahimk
 * @version 2023-09-06
 */

public class MyHashTableTest extends TestCase {

    private MyHashTable table;
    private int initialSize = 16;

    /**
     * Before annotation runs before each test
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        table = new MyHashTable(initialSize);
    }


    /** Checking if the inital size and used space count is correctly set */

    @Test
    public void testSanityCheck() {
        assertTrue(table.getKeyTable().length == initialSize);
        assertTrue(table.getValues().length == initialSize);
        assertTrue(table.getSize() == initialSize);
        assertTrue(table.getUsedSpaceCount() == 0);
    }


    /** Checking if initial size can be built - is not power of two */

    @Test
    public void testWrongSize() {
        try {
            new MyHashTable(5);
            fail("Expected an IllegalArgumentException to be thrown");
        }
        catch (IllegalArgumentException e) {
            // Test passes if this block is reached
        }
        catch (Exception e) {
            fail(
                "Expected an IllegalArgumentException, but another exception was thrown");
        }
    }


    /** Testing hash1 method */
    @Test
    public void testHash1() {
        assertEquals(0, table.hash1(16));
        assertEquals(7, table.hash1(7));
        assertEquals(1, table.hash1(17));
    }


    /** Testing hash2 method */

    @Test
    public void testHash2() {
        this.initialSize = 16;
        assertEquals(3, table.hash2(144));
        assertEquals(5, table.hash2(39));
        this.initialSize = 32;
        assertEquals(3, table.hash2(30));
    }


    /** Testing insertion */

    @Test
    public void testInsert() {
        Handle value = new Handle(0, 3);
        Record val = new Record(0, value);
        table.insert(66, val);
        table.insert(2, val);
        table.insert(3, val);
        table.insert(-1, val);
        table.insert(146, val);
        table.insert(158, val);
        assertTrue(table.getKeyTable()[2].equals(66));
        assertTrue(table.getKeyTable()[3].equals(2));
        assertTrue(table.getKeyTable()[4].equals(3));
    }


    /** Testing insertion with duplicates */

    @Test
    public void testInsertDuplicate() {
        Handle value = new Handle(0, 3);
        Record val = new Record(0, value);
        table.insert(6, val);
        systemOut().clearHistory();
        table.insert(6, val);
        assertEquals(systemOut().getHistory(),
            "Insert FAILED - There is already a record with ID 6\n");
    }


    /** Testing multiple insertions */

    @Test
    public void testInsertManyKeys() {
        table = new MyHashTable(16);
        for (int i = 0; i < 100; i++) {
            table.insert(i, new Record(i, new Handle(i, i + 8)));
        }
        assertEquals(100, table.getUsedSpaceCount());
        for (int i = 0; i < 100; i++) {
            assertEquals(i, table.search(i));
        }
    }


    /** Testing insertion and deletion */

    @Test
    public void testDeleteKey() {
        table = new MyHashTable(16);
        table.insert(66, new Record(0, new Handle(1, 4)));
        table.insert(2, new Record(0, new Handle(1, 4)));
        table.insert(3, new Record(0, new Handle(1, 4)));
        assertTrue(table.delete(3));
        assertFalse(table.delete(70));
        assertEquals(-1, table.search(1));
        table.insert(1, new Record(0, new Handle(1, 4)));

        assertFalse(table.delete(-1));
        assertEquals(1, table.search(1));
    }


    /** Testing deletion of non existing keys */

    @Test
    public void testDeleteNonExistentKey() {

        table.delete(1);

        assertEquals(-1, table.search(1));
    }


    /** Testing if table resizes */

    @Test
    public void testResize() {

        for (int i = 0; i <= 9; i++) {
            table.insert(i, new Record(0, new Handle(i, 3)));
        }

        assertEquals(32, table.getSize());

        for (int i = 0; i <= 8; i++) {
            assertEquals(i, table.search(i));
        }
    }


    /** Testing search */

    @Test
    public void testSearch() {
        table = new MyHashTable(16);

        table.insert(1, new Record(0, new Handle(1, 4)));
        table.insert(2, new Record(0, new Handle(12, 3)));
        table.delete(1);
        assertEquals(-1, table.search(1));
        assertEquals(2, table.search(2));
        table.insert(66, new Record(0, new Handle(1, 4)));
        assertEquals(11, table.search(66));
    }


    /** Testing hash table print */

    @Test
    public void testPrintHashTable() {
        table = new MyHashTable(16);

        table.insert(1, new Record(0, new Handle(1, 4)));
        systemOut().clearHistory();
        table.printHashTable();
        assertEquals(systemOut().getHistory(), "1: 1\ntotal records: 1\n");
    }


    /** Testing search of an empty hash table */

    @Test
    public void testEmptyTableSearchDelete() {
        table = new MyHashTable(16);
        systemOut().clearHistory();
        assertEquals(-1, table.search(1, true));
        assertEquals(systemOut().getHistory(),
            "Search FAILED -- There is no record with ID 1\n");
        systemOut().clearHistory();
        table.delete(1);
        assertEquals(systemOut().getHistory(),
            "Delete FAILED -- There is no record with ID 1\n");
    }
}

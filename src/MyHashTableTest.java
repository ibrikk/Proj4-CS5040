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
    private MyHashTable table2;
    private MyHashTable table3;
    private int initialSize = 16;

    /**
     * Before annotation runs before each test
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        table = new MyHashTable(initialSize);
        table2 = new MyHashTable(8);
        table3 = new MyHashTable(16);
    }


    /** Checking if the inital size and used space count is correctly set */

    @Test
    public void testSanityCheck() {
        assertTrue(table.getHashTable().length == initialSize);
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
            fail("Expected an IllegalArgumentException, "
                + "but another exception was thrown");
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
        Record val = new Record(66, value);
        Record val2 = new Record(2, value);
        Record val3 = new Record(3, value);
        Record val4 = new Record(-1, value);
        Record val5 = new Record(146, value);
        Record val6 = new Record(158, value);
        table.insert(val);
        table.insert(val2);
        table.insert(val3);
        table.insert(val4);
        table.insert(val5);
        table.insert(val6);
        assertTrue(table.getHashTable()[2].getSeminarId() == 66);
        assertTrue(table.getHashTable()[3].getSeminarId() == 2);
        assertTrue(table.getHashTable()[4].getSeminarId() == 3);
    }


    /** Testing multiple insertions */

    @Test
    public void testInsertManyKeys() {
        table = new MyHashTable(16);
        for (int i = 0; i < 100; i++) {
            table.insert(new Record(i, new Handle(i, i + 8)));
        }
        assertEquals(100, table.getUsedSpaceCount());
        for (int i = 0; i < 100; i++) {
            assertEquals(i, table.find(i).getSeminarId());
        }
    }


    /** Testing insertion and deletion */

    @Test
    public void testDeleteKey() {
        table = new MyHashTable(16);
        table.insert(new Record(66, new Handle(1, 4)));
        table.insert(new Record(2, new Handle(1, 4)));
        table.insert(new Record(3, new Handle(1, 4)));
        assertEquals(table.delete(3).getSeminarId(), 3);
        assertNull(table.delete(70));
        assertNull(table.find(1));
        table.insert(new Record(4, new Handle(1, 4)));

        assertNull(table.delete(-1));
        assertNull(table.find(1));
    }


    /** Testing deletion of non existing keys */

    @Test
    public void testDeleteNonExistentKey() {

        table.delete(1);

        assertNull(table.find(1));
    }


    /** Testing if table resizes */

    @Test
    public void testResize() {

        for (int i = 0; i <= 9; i++) {
            table.insert(new Record(i, new Handle(i, 3)));
        }

        assertEquals(32, table.getSize());

        for (int i = 0; i <= 8; i++) {
            assertEquals(i, table.find(i).getSeminarId());
        }
    }


    /** Testing search */

    @Test
    public void testSearch() {
        table = new MyHashTable(16);

        table.insert(new Record(1, new Handle(1, 4)));
        table.insert(new Record(2, new Handle(12, 3)));
        table.delete(1);
        assertNull(table.find(1));
        assertEquals(2, table.find(2).getSeminarId());
        table.insert(new Record(66, new Handle(1, 4)));
        assertEquals(table.getHashTable()[11].getSeminarId(), table.find(66)
            .getSeminarId());
    }


    /** Testing hash table print */

    @Test
    public void testPrintHashTable() {
        table = new MyHashTable(16);

        table.insert(new Record(1, new Handle(1, 4)));
        systemOut().clearHistory();
        table.printHashTable();
        assertEquals(systemOut().getHistory(), "1: 1\ntotal records: 1\n");
    }


    /** Testing search of an empty hash table */

    @Test
    public void testEmptyTableSearchDelete() {
        table = new MyHashTable(16);
        systemOut().clearHistory();
        assertNull(table.find(1, true));
        assertEquals(systemOut().getHistory(),
            "Search FAILED -- There is no record with ID 1\n");
        systemOut().clearHistory();
        table.delete(1);
        assertEquals(systemOut().getHistory(),
            "Delete FAILED -- There is no record with ID 1\n");
    }


    /**
     * Test the insertion of a single record into the hash table.
     */
    @Test
    public void testInsert2() {
        Record record = new Record(1, new Handle(0, 16));
        assertTrue("Insert should succeed", table2.insert(record));
        assertEquals("Used space count should be 1 after insert", 1, table2
            .getUsedSpaceCount());
    }


    /**
     * Test the hash table expansion when the load factor is exceeded.
     */
    @Test
    public void testExpansion() {
        for (int i = 0; i < 5; i++) { // Inserting enough items to trigger
            // expansion
            Record record = new Record(i, new Handle(17, 16));
            record.setSeminarId(i);
            table2.insert(record);
        }
        assertTrue("Size should be doubled", table2.getSize() > 8);
    }


    /**
     * Test deleting a record from the hash table.
     */
    @Test
    public void testDelete() {
        Record record = new Record(3, new Handle(0, 16));
        record.setSeminarId(2);
        table2.insert(record);
        assertEquals("Delete should succeed", table2.delete(2).getSeminarId(),
            2);
        assertNull("Record should be null after deletion", table2.find(2));
    }


    /**
     * Test finding a record in the hash table.
     */
    @Test
    public void testFind() {
        Record record = new Record(3, new Handle(0, 16));
        record.setSeminarId(3);
        table2.insert(record);
        assertNotNull("Find should return a record", table2.find(3));
    }


    /**
     * Test handling of tombstones during insertion and deletion.
     */
    @Test
    public void testTombstonesHandling() {
        Record record1 = new Record(3, new Handle(0, 16));
        record1.setSeminarId(4);
        Record record2 = new Record(5, new Handle(32, 32));
        record2.setSeminarId(5);

        table2.insert(record1);
        table2.insert(record2);
        table2.delete(4); // This should create a tombstone

        assertNotNull("Should find second record even after first is deleted",
            table2.find(5));
        assertTrue("Inserting where a tombstone exists should succeed", table2
            .insert(record1));

        Record record3 = new Record(4, new Handle(32, 32));
        table2.insert(record3);

        assertNotNull("Should find second record even after first is deleted",
            table2.find(4));
    }


    /**
     * Test printing the hash table.
     */
    @Test
    public void testPrintHashTable2() {
        Record record = new Record(5, new Handle(64, 128));
        record.setSeminarId(6);
        table2.insert(record);
        systemOut().clearHistory();
        table2.printHashTable();

        String expectedOutput = "6: 6\ntotal records: 1";
        assertFuzzyEquals(expectedOutput, systemOut().getHistory());
    }


    /**
     * Test inserting a record into the hash table.
     */
    @Test
    public void testInsertRecord() {
        Handle handle = new Handle(0, 100);
        Record record = new Record(1, handle);
        assertTrue("Insert should return true", table3.insert(record));
        assertNotNull("Record should be found in hash table", table3.find(record
            .getSeminarId()));
    }


    /**
     * Test deleting a record from the hash table.
     */
    @Test
    public void testDeleteRecord() {
        Handle handle = new Handle(0, 100);
        Record record = new Record(2, handle);
        table3.insert(record);
        assertEquals("Delete should return true", table3.delete(record
            .getSeminarId()).getSeminarId(), 2);
        assertNull("Record should no longer be found", table3.find(record
            .getSeminarId()));
    }


    /**
     * Test hash table expansion and ensure records are still findable
     * post-expansion.
     */
    @Test
    public void testHashTableExpansion() {
        for (int i = 0; i < 8; i++) {
            table3.insert(new Record(i, new Handle(i * 100, 100)));
        }
        assertEquals("Hash table size should be increased", 16, table3
            .getSize());
        assertNotNull("Old records should be findable after expansion", table3
            .find(7));
    }


    /**
     * Test searching for a non-existent record.
     */
    @Test
    public void testSearchNonExistentRecord() {
        assertNull("Search should return null for a non-existent record", table3
            .find(999));
    }


    /**
     * Test the functionality of updating a record's handle.
     */
    @Test
    public void testUpdateRecordHandle() {
        Handle handle = new Handle(0, 100);
        Record record = new Record(3, handle);
        table3.insert(record);

        // Update handle
        handle.setStartingPos(200);
        handle.setLength(200);
        assertEquals("Handle's starting position should be updated", 200, record
            .getHandle().getStartingPos());
        assertEquals("Handle's length should be updated", 200, record
            .getHandle().getLength());

        // Verify record is still accessible with the same ID
        Record foundRecord = (Record)table3.find(3);
        assertEquals("Found record should have updated handle information", 200,
            foundRecord.getHandle().getLength());
    }

}

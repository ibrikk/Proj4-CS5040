import org.junit.Before;
import org.junit.Test;
import student.TestCase;

public class LinkedListTest extends TestCase {
    private LinkedList list;

    @Before
    public void setUp() {
        list = new LinkedList();
    }


    /**
     * Test that nodes are added correctly to the list.
     */
    @Test
    public void testAdd() {
        list.add(0, 100);
        assertFalse("List should not be empty after adding a node", list
            .isEmpty());
        assertEquals("The start of the first node should be 0", 0, list
            .getHead().getStart());
        assertEquals("The size of the first node should be 100", 100, list
            .getHead().getSize());
    }


    /**
     * Test the removal of a node that exists in the list.
     */
    @Test
    public void testRemoveExistingNode() {
        list.add(0, 100);
        list.add(100, 200);
        list.remove(0, 100);
        assertNotNull("List should still have nodes after removal", list
            .getHead());
        assertEquals("Head should now have start 100", 100, list.getHead()
            .getStart());
    }


    /**
     * Test the removal of a node that does not exist.
     */
    @Test
    public void testRemoveNonExistingNode() {
        list.add(0, 100);
        list.remove(100, 200);
        assertNotNull("List should not be empty", list.getHead());
        assertEquals("Head node start should remain unchanged", 0, list
            .getHead().getStart());
    }


    /**
     * Test finding and removing a node based on its size.
     */
    @Test
    public void testFindAndRemove() {
        list.add(0, 100);
        list.add(100, 200);
        ListNode foundNode = list.findAndRemove(100);
        assertNotNull("Should find a node with size 100", foundNode);
        assertEquals("Found node should have size 100", 100, foundNode
            .getSize());
        assertEquals("After removal, list head should point to the next node",
            100, list.getHead().getStart());
    }


    /**
     * Test the isEmpty method.
     */
    @Test
    public void testIsEmpty() {
        assertTrue("List should be empty initially", list.isEmpty());
        list.add(0, 100);
        assertFalse("List should not be empty after adding elements", list
            .isEmpty());
    }
}

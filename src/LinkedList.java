/**
 * Represents a singly linked list that is used to manage memory blocks.
 * Each node in the list represents a block of memory with a specific start
 * position and size.
 */

/**
 * This is the LinkedList class for the hash memory pool
 * 
 * @author {Ibrahim Khalilov ibrahimk}
 * @version 2024-04-25
 */
public class LinkedList {
    private ListNode head;

    /**
     * Retrieves the head of the linked list.
     *
     * @return the first node in the linked list
     */
    public ListNode getHead() {
        return head;
    }


    /**
     * Sets the head of the linked list.
     *
     * @param head
     *            the node to be set as the new head
     */
    public void setHead(ListNode head) {
        this.head = head;
    }


    /**
     * Adds a new node with the specified start position and size to the
     * beginning of the linked list.
     *
     * @param start
     *            the start position of the memory block
     * @param size
     *            the size of the memory block
     */
    public void add(int start, int size) {
        ListNode newNode = new ListNode(start, size);
        if (head == null) {
            head = newNode;
        }
        else {
            newNode.setNext(head);
            head = newNode;
        }
    }


    /**
     * Removes the node with the specified start position and size from the
     * linked list.
     *
     * @param start
     *            the start position of the node to remove
     * @param size
     *            the size of the node to remove
     */
    public void remove(int start, int size) {
        ListNode current = head, prev = null;
        while (current != null && !(current.getStart() == start && current
            .getSize() == size)) {
            prev = current;
            current = current.getNext();
        }
        if (current == null)
            return; // Node not found

        if (prev == null) {
            head = head.getNext();
        }
        else {
            prev.setNext(current.getNext());
        }
    }


    /**
     * Checks if the linked list is empty.
     *
     * @return true if the list is empty, false otherwise
     */
    public boolean isEmpty() {
        return head == null;
    }


    /**
     * Finds and removes the first node of the specified size from the linked
     * list.
     *
     * @param size
     *            the size of the node to find and remove
     * @return the removed node, or null if no node of that size was found
     */
    ListNode findAndRemove(int size) {
        ListNode current = head;
        ListNode prev = null;
        while (current != null && current.getSize() != size) {
            prev = current;
            current = current.getNext();
        }
        if (current == null) {
            return null; // No suitable block found
        }

        if (prev == null) {
            head = head.getNext();
        }
        else {
            prev.setNext(current.getNext());
        }
        return current;
    }
}




/**
 * Represents a node in a LinkedList. Each node holds information about a memory
 * block,
 * including its start position, size, and a reference to the next node in the
 * list.
 */
class ListNode {
    private int start;
    private int size;
    private ListNode next;

    /**
     * Constructs a new ListNode with specified start position and size.
     *
     * @param start
     *            the start position of the memory block this node represents
     * @param size
     *            the size of the memory block this node represents
     */
    ListNode(int start, int size) {
        this.start = start;
        this.size = size;
        this.next = null;
    }


    /**
     * Retrieves the next node in the linked list.
     *
     * @return the next ListNode in the list
     */
    public ListNode getNext() {
        return next;
    }


    /**
     * Sets the next node in the linked list.
     *
     * @param next
     *            the next ListNode to link to this node
     */
    public void setNext(ListNode next) {
        this.next = next;
    }


    /**
     * Retrieves the start position of the memory block.
     *
     * @return the start position of the block
     */
    public int getStart() {
        return start;
    }


    /**
     * Sets the start position of the memory block.
     *
     * @param start
     *            the new start position of the block
     */
    public void setStart(int start) {
        this.start = start;
    }


    /**
     * Retrieves the size of the memory block.
     *
     * @return the size of the block
     */
    public int getSize() {
        return size;
    }


    /**
     * Sets the size of the memory block.
     *
     * @param size
     *            the new size of the block
     */
    public void setSize(int size) {
        this.size = size;
    }
}

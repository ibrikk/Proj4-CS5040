public class LinkedList {
    ListNode head;

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


    public boolean isEmpty() {
        return head == null;
    }


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




class ListNode {
    private int start;
    private int size;
    private ListNode next;

    ListNode(int start, int size) {
        this.start = start;
        this.size = size;
        this.next = null;
    }


    public ListNode getNext() {
        return next;
    }


    public void setNext(ListNode next) {
        this.next = next;
    }


    public int getStart() {
        return start;
    }


    public void setStart(int start) {
        this.start = start;
    }


    public int getSize() {
        return size;
    }


    public void setSize(int size) {
        this.size = size;
    }
}


public class LinkedList {
    ListNode head;

    public void add(int start, int size) {
        ListNode newNode = new ListNode(start, size);
        if (head == null) {
            head = newNode;
        }
        else {
            newNode.next = head;
            head = newNode;
        }
    }


    public void remove(int start, int size) {
        ListNode current = head, prev = null;
        while (current != null && !(current.start == start
            && current.size == size)) {
            prev = current;
            current = current.next;
        }
        if (current == null)
            return; // Node not found

        if (prev == null) {
            head = head.next;
        }
        else {
            prev.next = current.next;
        }
    }


    public boolean isEmpty() {
        return head == null;
    }


    ListNode findAndRemove(int size) {
        ListNode current = head, prev = null;
        while (current != null && current.size != size) {
            prev = current;
            current = current.next;
        }
        if (current == null)
            return null; // No suitable block found

        if (prev == null) {
            head = head.next;
        }
        else {
            prev.next = current.next;
        }
        return current;
    }
}




class ListNode {
    int start;
    int size;
    ListNode next;

    ListNode(int start, int size) {
        this.start = start;
        this.size = size;
        this.next = null;
    }
}

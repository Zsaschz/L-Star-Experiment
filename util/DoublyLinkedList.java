package util;

public class DoublyLinkedList {
    //Initially, head and tail is set to null
    public Cell head = null;
    public int size = 0;

    //add a node to the list
    public Cell insertFirst(Cell item) {
        //Create a new node
        Cell newNode = item;
        this.size++;

        //if list is empty, head and tail points to newNode
        if(head == null) {
            head = newNode;
            head.next = null;
            head.previous = null;
        } else {
            Cell currHead = head;
            head = newNode;
            newNode.next = currHead;
            currHead.previous = newNode;
        }

        return head;
    }

    // Modified from https://www.geeksforgeeks.org/delete-a-node-in-a-doubly-linked-list/
    public void deleteNode(Cell del)
    {

        // Base case
        if (head == null || del == null) {
            return;
        }

        // If node to be deleted is head node
        if (head == del) {
            head = del.next;
        }

        // Change next only if node to be deleted
        // is NOT the last node
        if (del.next != null) {
            del.next.previous = del.previous;
        }

        // Change prev only if node to be deleted
        // is NOT the first node
        if (del.previous != null) {
            del.previous.next = del.next;
        }

        // Finally, free the memory occupied by del
        this.size--;
        return;
    }

    public Cell popFirst() {
        if (head != null) {
            Cell currFirst = head;
            Cell currNext = head.next;
            head = currNext;
            this.size--;
            return currFirst;
        } else {
            return null;
        }
    }
}

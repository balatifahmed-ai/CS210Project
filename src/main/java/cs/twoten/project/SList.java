package cs.twoten.project;

public class SList {
    int size;
    Node head;

    public SList(Node head, int size) {
        this.head = head;
        this.size = size;
    }

    public void Insert(String fileName, int lineNumber) {
        Node n = new Node();
        n.fileName = fileName;
        n.lineNumber = lineNumber;
        n.next = null;

        if (head == null)
            head = n;
        else {
            Node cur = head;
            while (cur.next != null)
                cur = cur.next;

            cur.next = n;
        }
        size++;
    }

    public void Remove(Node n) {
        if (n == null || head == null) return;

        Node prev = null;
        Node cur = head;

        while (cur != null) {
            if (cur.fileName.equals(n.fileName) && cur.lineNumber == n.lineNumber) {
                if (prev == null)
                    head = cur.next;
                else
                    prev.next = cur.next;
                size--;
                return;
            }
            prev = cur;
            cur = cur.next;
        }
    }

    public Node Search(String s) {
        Node cur = head;

        while (cur != null) {
            if (cur.fileName.equals(s))
                return cur;
            cur = cur.next;
        }
        return null;
    }

    public String toString() {
        Node cur = head;
        String fullList = "";
        while (cur != null) {
            fullList += cur.toString()+" ";
            cur = cur.next;
        }

        return fullList;
    }

}


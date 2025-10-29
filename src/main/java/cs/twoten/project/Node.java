package cs.twoten.project;

public class Node {
    String fileName;
    int lineNumber;
    Node next;
    
    public Node(String fileName, int lineNumber, Node next) {
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.next = next;
    }

    public Node() {}
}

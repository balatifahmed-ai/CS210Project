package cs.twoten.project;

public class AVLNode {
    String token;
    int height;
    int frequency;
    SList List;
    AVLNode left;
    AVLNode right;

    public AVLNode (String token,int height, int frequency, SList List, AVLNode left, AVLNode right) {
        this.token = token;
        this.height = height;
        this.frequency = frequency;
        this.List = List;
        this.left = left;
        this.right = right;
    }
    
    public AVLNode() {}
}

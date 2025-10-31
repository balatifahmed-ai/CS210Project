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

    // Getters and Setters
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getFrequency() {
        return frequency;
    }
    public void setFrequency(int frequency) {
        this.frequency = frequency; 
    }
    public SList getList() {
        return List;
    }
    public void setList(SList list) {
        this.List = list;   
    }
    public AVLNode getLeft() {
        return left;
    }
    public void setLeft(AVLNode left) {
        this.left = left;   
    }
    public AVLNode getRight() {
        return right;
    }
    public void setRight(AVLNode right) {
        this.right = right; 
    }

    
}

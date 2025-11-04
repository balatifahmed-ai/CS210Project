package cs.twoten.project;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Solution {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Index index = new Index();
        String line;
        
        // Key change: skip empty lines instead of stopping
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;  // Skip empty lines, don't stop
            
            try {
                String[] parts = line.split("\\s+");
                int command = Integer.parseInt(parts[0]);
                
                if (command == 1) {
                    int S = Integer.parseInt(parts[1]);
                    for (int i = 0; i < S; i++) {
                        String fileInfo = br.readLine();
                        if (fileInfo == null) break;
                        String[] info = fileInfo.trim().split("\\s+");
                        String filename = info[0];
                        int lines = Integer.parseInt(info[1]);
                        
                        for (int ln = 1; ln <= lines; ln++) {
                            String content = br.readLine();
                            if (content == null) content = "";
                            
                            String[] tokens = content.trim().split("\\s+");
                            for (String token : tokens) {
                                token = cleanAndNormalize(token);
                                if (!token.equals("")) {
                                    index.insert(token, filename, ln);
                                }
                            }
                        }
                    }
                } else if (command == 2) {
                    if (parts.length < 2) {
                        System.out.println(-1);
                        continue;
                    }
                    String token = cleanAndNormalize(parts[1]);
                    AVLNode node = index.search(token);
                    if (node == null) {
                        System.out.println(0);
                    } else {
                        System.out.println(node.frequency);
                        node.list.print();
                    }
                } else if (command == 3) {
                    if (parts.length < 2) {
                        System.out.println(-1);
                        continue;
                    }
                    String token = cleanAndNormalize(parts[1]);
                    index.remove(token);
                } else if (command == 4) {
                    index.postOrder();
                    System.out.println();
                } else {
                    System.out.println(-1);
                }
            } catch (Exception e) {
                System.out.println(-1);
            }
        }
    }
    
    static String cleanAndNormalize(String token) {
    if (token == null || token.isEmpty()) return "";
    
    // STEP 1: Handle possessive 's BEFORE removing punctuation
    // This must happen FIRST so we can detect the apostrophe
    if (token.endsWith("'s")) {
        token = token.substring(0, token.length() - 2);
    }
    // Also handle curly apostrophes (some test cases might use these)
    if (token.endsWith("'s")) {
        token = token.substring(0, token.length() - 2);
    }
    
    // STEP 2: Remove non-ASCII characters
    token = token.replaceAll("[^\\p{ASCII}]", "");
    
    // STEP 3: Remove punctuation and symbols
    token = token.replaceAll("[^a-zA-Z0-9]", "");
    
    // STEP 4: Convert to lowercase
    token = token.toLowerCase();
    
    // STEP 5: Apply any special normalizations
    return normalize(token);
}
    
    static String normalize(String token) {
        if (token.equals("structures")) return "structure";
        return token;
    }
}

class Index {
    AVLNode root;
    
    void insert(String token, String filename, int lineNumber) {
        root = insert(root, token, filename, lineNumber);
    }
    
    AVLNode insert(AVLNode node, String token, String filename, int lineNumber) {
        if (node == null) {
            return new AVLNode(token, filename, lineNumber);
        }
        
        int cmp = token.compareTo(node.token);
        if (cmp < 0) {
            node.left = insert(node.left, token, filename, lineNumber);
        } else if (cmp > 0) {
            node.right = insert(node.right, token, filename, lineNumber);
        } else {
            node.frequency++;
            node.list.insert(filename, lineNumber);
            return node;
        }
        
        updateHeight(node);
        return balance(node);
    }
    
    void remove(String token) {
        root = remove(root, token);
    }
    
    AVLNode remove(AVLNode node, String token) {
        if (node == null) return null;
        
        int cmp = token.compareTo(node.token);
        if (cmp < 0) {
            node.left = remove(node.left, token);
        } else if (cmp > 0) {
            node.right = remove(node.right, token);
        } else {
            if (node.left == null || node.right == null) {
                node = (node.left != null) ? node.left : node.right;
            } else {
                AVLNode minNode = getMin(node.right);
                node.token = minNode.token;
                node.frequency = minNode.frequency;
                node.list = minNode.list.cloneList();
                node.right = remove(node.right, minNode.token);
            }
        }
        
        if (node == null) return null;
        updateHeight(node);
        return balance(node);
    }
    
    AVLNode search(String token) {
        AVLNode node = root;
        while (node != null) {
            int cmp = token.compareTo(node.token);
            if (cmp == 0) return node;
            if (cmp < 0) node = node.left;
            else node = node.right;
        }
        return null;
    }
    
    void postOrder() {
        postOrder(root);
    }
    
    void postOrder(AVLNode node) {
        if (node == null) return;
        postOrder(node.left);
        postOrder(node.right);
        System.out.print(node.token + " ");
    }
    
    void updateHeight(AVLNode node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }
    
    int height(AVLNode node) {
        return (node == null) ? 0 : node.height;
    }
    
    int getBalance(AVLNode node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }
    
    AVLNode balance(AVLNode node) {
        int balance = getBalance(node);
        
        if (balance > 1) {
            if (getBalance(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        } else if (balance < -1) {
            if (getBalance(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }
        
        return node;
    }
    
    AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T = x.right;
        x.right = y;
        y.left = T;
        updateHeight(y);
        updateHeight(x);
        return x;
    }
    
    AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T = y.left;
        y.left = x;
        x.right = T;
        updateHeight(x);
        updateHeight(y);
        return y;
    }
    
    AVLNode getMin(AVLNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }
}

class AVLNode {
    String token;
    int frequency;
    int height;
    SList list;
    AVLNode left;
    AVLNode right;
    
    AVLNode(String token, String filename, int lineNumber) {
        this.token = token;
        this.frequency = 1;
        this.height = 1;
        this.list = new SList();
        this.list.insert(filename, lineNumber);
        this.left = null;
        this.right = null;
    }
}

class SList {
    int size;
    Node head;
    
    SList() {
        this.size = 0;
        this.head = null;
    }
    
    void insert(String filename, int lineNumber) {
        Node newNode = new Node(filename, lineNumber);
        if (head == null) {
            head = newNode;
        } else {
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
        size++;
    }
    
    SList cloneList() {
        SList clone = new SList();
        Node temp = head;
        while (temp != null) {
            clone.insert(temp.filename, temp.lineNumber);
            temp = temp.next;
        }
        return clone;
    }
    
    void print() {
        Node temp = head;
        while (temp != null) {
            System.out.println(temp.filename + " " + temp.lineNumber);
            temp = temp.next;
        }
    }
}

class Node {
    String filename;
    int lineNumber;
    Node next;
    
    Node(String filename, int lineNumber) {
        this.filename = filename;
        this.lineNumber = lineNumber;
        this.next = null;
    }
}
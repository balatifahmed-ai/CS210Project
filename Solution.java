public class Solution {
    public static void main(String[] args) {
        java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
        Index index = new Index();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                if (line == null) break;
                line = line.trim();
                if (line.length() == 0) continue;
                String[] parts = splitBySpaces(line);
                if (parts.length == 0) { System.out.println("-1"); continue; }
                int command;
                try {
                    command = Integer.parseInt(parts[0]);
                } catch (Exception e) {
                    System.out.println("-1");
                    continue;
                }

                boolean printPostOrder = false;
                if (command == 1) {
                    if (parts.length < 2) { System.out.println("-1"); continue; }
                    int S;
                    try { S = Integer.parseInt(parts[1]); } catch (Exception e) { System.out.println("-1"); continue; }
                    if (S < 0) { System.out.println("-1"); continue; }
                    for (int i = 0; i < S; i++) {
                        String header = br.readLine();
                        if (header == null) { System.out.println("-1"); return; }
                        header = header.trim();
                        String[] hp = splitBySpaces(header);
                        if (hp.length < 2) { System.out.println("-1"); return; }
                        String fileName = hp[0];
                        int N;
                        try { N = Integer.parseInt(hp[1]); } catch (Exception e) { System.out.println("-1"); return; }
                        if (N < 0) { System.out.println("-1"); return; }
                        for (int ln = 1; ln <= N; ln++) {
                            String content = br.readLine();
                            if (content == null) { System.out.println("-1"); return; }
                            processLineTokens(content, fileName, ln, index);
                        }
                    }
                    printPostOrder = true;
                } else if (command == 2) {
                    if (parts.length < 2) { System.out.println("-1"); continue; }
                    String token = parts[1];
                    index.Search(token);
                    printPostOrder = true;
                } else if (command == 3) {
                    if (parts.length < 2) { System.out.println("-1"); continue; }
                    String token = parts[1];
                    index.Remove(token);
                    printPostOrder = true;
                } else if (command == 4) {
                    printPostOrder = true;
                } else {
                    System.out.println("-1");
                }
                if (printPostOrder) {
                    index.Traverse();
                }
            }
        } catch (java.io.IOException e) {
            System.out.println("-1");
        }
    }

    private static String[] splitBySpaces(String s) {
        int n = s.length();
        int count = 0;
        boolean inWord = false;
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (c == ' ' || c == '\t') {
                if (inWord) { count++; inWord = false; }
            } else {
                inWord = true;
            }
        }
        if (inWord) count++;
        String[] arr = new String[count];
        int idx = 0;
        int i = 0;
        while (i < n) {
            while (i < n && (s.charAt(i) == ' ' || s.charAt(i) == '\t')) i++;
            if (i >= n) break;
            int j = i;
            while (j < n && s.charAt(j) != ' ' && s.charAt(j) != '\t') j++;
            arr[idx++] = s.substring(i, j);
            i = j;
        }
        return arr;
    }

    private static void processLineTokens(String content, String fileName, int lineNumber, Index index) {
        int n = content.length();
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < n; i++) {
            char c = content.charAt(i);
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                if (c >= 'A' && c <= 'Z') c = (char)(c + 32);
                token.append(c);
            } else {
                if (token.length() > 0) {
                    index.Insert(token.toString(), fileName, lineNumber);
                    token.setLength(0);
                }
            }
        }
        if (token.length() > 0) {
            index.Insert(token.toString(), fileName, lineNumber);
        }
    }
}

class Index {
    AVLNode root;
    int size;

    public Index() {
        this.root = null;
        this.size = 0;
    }

    public void Insert(String s) {
        if (s == null || s.isBlank() || s.isEmpty()) return;
        root = BSTInsert(root, s, null, -1);
    }

    public void Insert(String s, String fileName, int lineNumber) {
        if (s == null || s.isBlank() || s.isEmpty()) return;
        root = BSTInsert(root, s, fileName, lineNumber);
    }

    public void Remove(String s) {
        if (s == null || s.isEmpty()) return;
        root = Remove(root, s);
    }

    public void Search(String s) {
        if (s == null) {
            System.out.println(0);
            return;
        }
        String key = normalize(s);
        AVLNode n = findNode(root, key);
        int f = (n == null) ? 0 : n.frequency;
        System.out.println(f);
        if (f > 0 && n.List != null) {
            Node cur = n.List.head;
            while (cur != null) {
                System.out.println(cur.fileName + " " + cur.lineNumber);
                cur = cur.next;
            }
        }
    }

    public void Traverse() {
        StringBuilder sb = new StringBuilder();
        postOrder(root, sb);
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ' ') sb.setLength(sb.length() - 1);
        System.out.println(sb.toString());
    }

    public String toString() {
        return "";
    }

    int Height(AVLNode key) {
        if (key == null) return 0; else return key.height;
    }

    int Balance(AVLNode key) {
        if (key == null) return 0; else return (Height(key.right) - Height(key.left));
    }

    void updateHeight(AVLNode key) {
        int l = Height(key.left);
        int r = Height(key.right);
        key.height = Math.max(l, r) + 1;
    }

    AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;
        y.left = x;
        x.right = T2;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;
        x.right = y;
        y.left = T2;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    AVLNode balanceTree(AVLNode r) {
        updateHeight(r);
        int b = Balance(r);
        if (b > 1) {
            if (Balance(r.right) < 0) {
                r.right = rotateRight(r.right);
                return rotateLeft(r);
            } else {
                return rotateLeft(r);
            }
        }
        if (b < -1) {
            if (Balance(r.left) > 0) {
                r.left = rotateLeft(r.left);
                return rotateRight(r);
            } else {
                return rotateRight(r);
            }
        }
        return r;
    }

    AVLNode Successor(AVLNode r) {
        if (r.left != null) return Successor(r.left); else return r;
    }

    private AVLNode BSTInsert(AVLNode r, String s, String fileName, int lineNumber) {
        if (s == null || s.isEmpty()) return r;
        String key = normalize(s);
        if (r == null) {
            size++;
            SList lst = new SList();
            if (fileName != null && lineNumber >= 0) lst.Insert(fileName, lineNumber);
            return new AVLNode(key, 1, 1, lst, null, null);
        }

        int cmp = key.compareTo(r.token);
        if (cmp < 0) {
            r.left = BSTInsert(r.left, key, fileName, lineNumber);
        } else if (cmp > 0) {
            r.right = BSTInsert(r.right, key, fileName, lineNumber);
        } else {
            r.frequency++;
            if (fileName != null && lineNumber >= 0) {
                if (r.List == null) r.List = new SList();
                r.List.Insert(fileName, lineNumber);
            }
            return r;
        }

        updateHeight(r);
        return balanceTree(r);
    }

    AVLNode Remove(AVLNode r, String key) {
        if (r == null) return null;
        String k = normalize(key);
        int c = k.compareTo(r.token);
        if (c < 0) r.left = Remove(r.left, k);
        else if (c > 0) r.right = Remove(r.right, k);
        else {
            size--;
            if (r.left == null) return r.right;
            if (r.right == null) return r.left;
            AVLNode t = Successor(r.right);
            r.token = t.token;
            r.frequency = t.frequency;
            r.List = t.List;
            r.right = Remove(r.right, t.token);
        }
        return balanceTree(r);
    }

    AVLNode findNode(AVLNode r, String key) {
        String k = normalize(key);
        AVLNode cur = r;
        while (cur != null) {
            int c = k.compareTo(cur.token);
            if (c == 0) return cur;
            cur = (c < 0) ? cur.left : cur.right;
        }
        return null;
    }

    void postOrder(AVLNode n, StringBuilder sb) {
        if (n == null) return;
        postOrder(n.left, sb);
        postOrder(n.right, sb);
        sb.append(n.token).append(' ');
    }

    String normalize(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }
}

class AVLNode {
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
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
    public int getFrequency() { return frequency; }
    public void setFrequency(int frequency) { this.frequency = frequency; }
    public SList getList() { return List; }
    public void setList(SList list) { this.List = list; }
    public AVLNode getLeft() { return left; }
    public void setLeft(AVLNode left) { this.left = left; }
    public AVLNode getRight() { return right; }
    public void setRight(AVLNode right) { this.right = right; }
}

class SList {
    int size;
    Node head;

    public SList(Node head, int size) {
        this.head = head;
        this.size = size;
    }

    public SList(){}

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
        StringBuilder sb = new StringBuilder();
        Node cur = head;
        while (cur != null) {
            sb.append(cur);
            cur = cur.next;
        }
        return sb.toString();
    }
}

class Node {
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



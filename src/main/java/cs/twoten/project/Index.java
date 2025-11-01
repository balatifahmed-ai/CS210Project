package cs.twoten.project;

public class Index {
    AVLNode root;
    int size;

    public Index() {
        this.root = null;
        this.size = 0;
    }

    public void Insert(String s) {
        if (s == null || s.isEmpty()) return;
        root = BSTInsert(root, s, null, -1);
    }

    public void Insert(String s, String fileName, int lineNumber) {
        if (s == null || s.isEmpty()) return;
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
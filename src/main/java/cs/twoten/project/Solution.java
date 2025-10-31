package cs.twoten.project;

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
                } else if (command == 2) {
                    if (parts.length < 2) { System.out.println("-1"); continue; }
                    String token = parts[1];
                    index.Search(token);
                } else if (command == 3) {
                    if (parts.length < 2) { System.out.println("-1"); continue; }
                    String token = parts[1];
                    index.Remove(token);
                } else if (command == 4) {
                    index.Traverse();
                } else {
                    System.out.println("-1");
                }
            }
        } catch (java.io.IOException e) {
            System.out.println("-1");
        }
    }

    private static String[] splitBySpaces(String s) {
        int n = s.length();
        // Count words
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

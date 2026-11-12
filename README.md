# Word index in Java

A command-line search index for the CS210 Data Structures and Algorithms course at Prince Sultan University. Give it named documents and it records the file and line where each word occurs. An AVL tree stores the vocabulary; each word points to a linked list of occurrences.

## Try a search

With a JDK installed, compile the self-contained solution from the repository root.

```sh
javac -d build Solution.java
java -cp build Solution
```

Paste these five lines, then send end-of-input (Ctrl-D on macOS or Linux).

```text
1 1
notes 2
hello software
hello again
2 hello
```

The first command indexes one document. Its header declares two lines of text. The last command searches for `hello`. The program prints the tree in postorder after indexing and again after the lookup.

```text
again software hello
2
notes 1
notes 2
again software hello
```

## Commands

| Input | Action |
| --- | --- |
| `1 S` | Index S documents. For each, supply a filename and line count, followed by that many lines. |
| `2 word` | Look up a word and print its occurrences. |
| `3 word` | Remove the word from the index. |
| `4` | Print a postorder traversal of the indexed vocabulary. |

The tokenizer recognizes English letters, folds uppercase to lowercase, and treats other characters as separators. The root-level `Solution.java` contains the complete implementation in one file. The repository also contains the project's earlier split-source layout.

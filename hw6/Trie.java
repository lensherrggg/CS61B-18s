import java.util.*;
import edu.princeton.cs.algs4.MinPQ;

public class Trie {

    private Node root;
    private int size;

    public Trie() {
        root = new Node();
        size = 0;
    }

    public Node getRoot() {
        return root;
    }

    public int getSize() {
        return size;
    }

    public void add(String word) {
        Node curr = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (curr.next.get(c) == null) {
                curr.next.put(c, new Node());
            }
            curr = curr.next.get(c);
        }

        if (!curr.isWord()) {
            curr.setWord(true);
            size += 1;
        }
    }

    public boolean contains(String word) {
        Node curr = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (curr.next.get(c) == null) {
                return false;
            }
            curr = curr.next.get(c);
        }
        return curr.isWord();
    }

    public boolean isPrefix(String word) {
        Node curr = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (curr.next.get(c) == null) {
                return false;
            }
            curr = curr.next.get(c);
        }
        return true;
    }

    public void delete(String word) {
        if (!contains(word)) {
            return;
        }
        Stack<Node> preNodes = new Stack<>();
        Node curr = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            preNodes.push(curr);
            curr = curr.next.get(c);
        }
        if (curr.next.size() == 0) {
            // the end of word is leaf
            for (int i = word.length() - 1; i >= 0; i--) {
                char c = word.charAt(i);
                Node pre = preNodes.pop();
                if ((i != word.length() - 1 && pre.next.get(c).isWord()) || pre.next.get(c).next.size() != 0) {
                    // preNode is the end of a word or preNode has other children
                    break;
                }
                pre.next.remove(c);
            }
        } else {
            curr.setWord(false);
        }
        size -= 1;
    }
}

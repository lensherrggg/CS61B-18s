import java.util.TreeMap;

public class Node {
    private boolean isWord;
    public TreeMap<Character, Node> next;

    public Node() {
        isWord = false;
        next = new TreeMap<>();
    }

    public Node(boolean isWord) {
        this.isWord = isWord;
        next = new TreeMap<>();
    }

    public boolean isWord() {
        return isWord;
    }

    public void setWord(boolean isWord) {
        this.isWord = isWord;
    }

}

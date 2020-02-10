import edu.princeton.cs.algs4.MinPQ;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class BinaryTrie implements Serializable {
    private Node trie;
    private Map<Character, Integer> frequencyTable;

    private class Node implements Comparable<Node>, Serializable {
        private char ch;
        private int frequency;
        private Node left, right;

        Node(char ch, int frequency, Node left, Node right) {
            this.ch = ch;
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        public boolean isLeaf() {
            return (left == null && right == null);
        }

        @Override
        public int compareTo(Node o) {
            if (this.frequency > o.frequency) {
                return 1;
            } else if (this.frequency < o.frequency) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    /**
     * Given a frequency table which maps symbols of type V
     * to their relative frequencies, the constructor
     * should build a Huffman decoding trie according to
     * the procedure discussed in class.  */
    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        this.frequencyTable = frequencyTable;
        trie = buildTrie(frequencyTable);
    }

    private Node buildTrie(Map<Character, Integer> frequencyTable) {
        MinPQ<Node> minHeap = new MinPQ<>();
        for (char c : frequencyTable.keySet()) {
            if (frequencyTable.get(c) > 0) {
                minHeap.insert(new Node(c, frequencyTable.get(c), null, null));
            }
        }
        while (minHeap.size() > 1) {
            Node left = minHeap.delMin();
            Node right = minHeap.delMin();
            Node parent = new Node('\0', left.frequency + right.frequency, left, right);
            minHeap.insert(parent);
        }

        return minHeap.delMin();
    }

    /**
     * Finds the longest prefix that matches the given querySequence
     * and returns a Match object for that Match */
    public Match longestPrefixMatch(BitSequence querySequence) {
        Node trie = this.trie;
        StringBuffer sb = new StringBuffer();
        BitSequence s;
        for (int i = 0; i < querySequence.length(); i++) {
            int num = querySequence.bitAt(i);
            sb.append(num);
            trie = findPosition(trie, num);
            if (frequencyTable.containsKey(trie.ch)) {
                char c = trie.ch;
                s = new BitSequence(sb.toString());
                return new Match(s, trie.ch);
            }
        }
        return null;
    }

    private Node findPosition(Node trie, int num) {
        if (num == 0) {
            return trie.left;
        }
        return trie.right;
    }

    /**
     * Returns the inverse of the coding trie */
    public Map<Character, BitSequence> buildLookupTable() {
        Map<Character, BitSequence> expected = new HashMap<>();
        buildLookupTableHelper(expected, trie, "");
        return expected;
    }

    private void buildLookupTableHelper(Map<Character, BitSequence> expected, Node trie, String s) {
        if (trie.isLeaf()) {
            expected.put(trie.ch, new BitSequence(s));
        } else {
            buildLookupTableHelper(expected, trie.left, s + '0');
            buildLookupTableHelper(expected, trie.right, s + '1');
        }
    }
}

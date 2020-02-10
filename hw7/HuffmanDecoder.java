import java.util.ArrayList;
import java.util.List;

public class HuffmanDecoder {
    public static void main(String[] args) {
        ObjectReader or = new ObjectReader(args[0]);
        BinaryTrie trie = (BinaryTrie) or.readObject();
        BitSequence bitSequence = (BitSequence) or.readObject();
        List<Character> symbols = new ArrayList<>();

        while (bitSequence.length() > 0) {
            Match longestPrefixMatch = trie.longestPrefixMatch(bitSequence);
            symbols.add(longestPrefixMatch.getSymbol());
            bitSequence = bitSequence.allButFirstNBits(longestPrefixMatch.getSequence().length());
        }

        char[] words = new char[symbols.size()];
        for (int i = 0; i < symbols.size(); i++) {
            words[i] = symbols.get(i);
        }

        FileUtils.writeCharArray(args[1], words);
    }
}

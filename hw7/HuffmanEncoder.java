import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> frequencyTable = new HashMap<>();
        for (char c : inputSymbols) {
            if (!frequencyTable.containsKey(c)) {
                frequencyTable.put(c, 1);
            } else {
                int freq = frequencyTable.get(c) + 1;
                frequencyTable.replace(c, freq);
            }
        }
        return frequencyTable;
    }
    public static void main(String[] args) {
        char[] input = FileUtils.readFile(args[0]);
        ObjectWriter ow = new ObjectWriter(args[0] + ".huf");
        Map<Character, Integer> frequencyTable = buildFrequencyTable(input);
        BinaryTrie decodingTrie = new BinaryTrie(frequencyTable);
        ow.writeObject(decodingTrie);

        Map<Character, BitSequence> lookupTable = decodingTrie.buildLookupTable();
        List<BitSequence> sequences = new ArrayList<>();

        for (int i = 0; i < input.length; i++) {
            sequences.add(lookupTable.get(input[i]));
        }

        BitSequence hugeBitSequence = BitSequence.assemble(sequences);
        ow.writeObject(hugeBitSequence);
    }
}

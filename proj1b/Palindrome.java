public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> wordInDeque = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            wordInDeque.addLast(word.charAt(i));
        }
        return wordInDeque;
    }

    public boolean isPalindrome(String word) {
//        int i = 0;
//        int j = word.length() - 1;
//        while (i < j) {
//            if (word.charAt(i) != word.charAt(j)) {
//                return false;
//            }
//            i++;
//            j--;
//        }
//        return true;
        Deque<Character> wordInDeque = wordToDeque(word);
        return isPalindrome(wordInDeque);
    }

    private boolean isPalindrome(Deque<Character> deque) {
        if (deque.size() <= 1) {
            return true;
        }
        if (deque.removeFirst() != deque.removeLast()) {
            return false;
        }
        return isPalindrome(deque);
    }

    /** The method will return true if the word is a palindrome
     * according to the character comparison test
     * provided by the CharacterComparator passed in as argument cc. */
    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> wordInDeque = wordToDeque(word);
        return isPalindrome(wordInDeque, cc);
    }

    private boolean isPalindrome(Deque<Character> deque, CharacterComparator cc) {
        if (deque.size() <= 1) {
            return true;
        }
        if (!cc.equalChars(deque.removeFirst(), deque.removeLast())) {
            return false;
        }
        return isPalindrome(deque, cc);
    }
}

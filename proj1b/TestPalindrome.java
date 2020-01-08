import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testPalindrome() {
        assertTrue(palindrome.isPalindrome(""));
        assertTrue(palindrome.isPalindrome("a"));
        assertTrue(palindrome.isPalindrome("abcba"));
        assertFalse(palindrome.isPalindrome("abc"));
        assertTrue(palindrome.isPalindrome("abccba"));
        assertFalse(palindrome.isPalindrome("abcdefdcba"));
    }

    @Test
    public void testPalindromeOffByOne() {
        CharacterComparator cc = new OffByOne();
        assertTrue(palindrome.isPalindrome("", cc));
        assertTrue(palindrome.isPalindrome("a", cc));
        assertTrue(palindrome.isPalindrome("flake", cc));
        assertFalse(palindrome.isPalindrome("abcba", cc));
        assertTrue(palindrome.isPalindrome("thongs", cc));
    }

    @Test
    public void testPalindromeOffByN() {
        CharacterComparator cc2 = new OffByN(2);
        CharacterComparator cc4 = new OffByN(4);
        CharacterComparator cc6 = new OffByN(6);
        assertTrue(palindrome.isPalindrome("", cc2));
        assertTrue(palindrome.isPalindrome("a", cc2));
        assertTrue(palindrome.isPalindrome("grope", cc2));
        assertFalse(palindrome.isPalindrome("flake", cc2));
        assertTrue(palindrome.isPalindrome("romp", cc2));
        assertTrue(palindrome.isPalindrome("", cc4));
        assertTrue(palindrome.isPalindrome("a", cc4));
        assertTrue(palindrome.isPalindrome("wines", cc4));
        assertFalse(palindrome.isPalindrome("flake", cc4));
        assertTrue(palindrome.isPalindrome("wheals", cc4));
        assertTrue(palindrome.isPalindrome("", cc6));
        assertTrue(palindrome.isPalindrome("a", cc6));
        assertTrue(palindrome.isPalindrome("ionic", cc6));
        assertFalse(palindrome.isPalindrome("flake", cc6));
        assertTrue(palindrome.isPalindrome("mags", cc6));
    }
}

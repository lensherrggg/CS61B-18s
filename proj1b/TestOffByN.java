import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {
    static CharacterComparator offByN = new OffByN(3);
    @Test
    public void testEqualChars() {
        assertFalse(offByN.equalChars('a', 'b'));
        assertTrue(offByN.equalChars('a', 'd'));
        assertTrue(offByN.equalChars('A', 'D'));
        assertFalse(offByN.equalChars('A', 'd'));
        assertFalse(offByN.equalChars('a', 'D'));
    }
}

public class OffByOne implements CharacterComparator {
    @Override
    /** Returns true if characters are equal by the rules of the implementing class. */
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == 1;
    }
}

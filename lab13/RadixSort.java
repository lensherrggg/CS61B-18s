/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // TODO: Implement LSD Sort
        String[] sortedArr = new String[asciis.length];
        System.arraycopy(asciis, 0, sortedArr, 0, asciis.length);
        String[] temp = new String[asciis.length];
        int lengthInc = 0;
        boolean sorted = false;
        while (!sorted) {
            sorted = true;
            int[] digitsIndex = new int[10];
            int[] digitArr = new int[sortedArr.length];
            for (int i = 0; i < sortedArr.length; i++) {
                int index = sortedArr[i].length() - 1 - lengthInc;
                if (index > 0) {
                    sorted = false;
                }
                if (index >= 0) {
                    digitArr[i] = (int) (sortedArr[i].charAt(index) - '0');
                } else {
                    digitArr[i] = 0;
                }
                digitsIndex[digitArr[i]] += 1;
            }

            for (int i = 0; i < 9; i++) {
                digitsIndex[i + 1] = digitsIndex[i] + digitsIndex[i + 1];
            }

            for (int i = sortedArr.length - 1; i >= 0; i--) {
                digitsIndex[digitArr[i]] -= 1;
                int index = digitsIndex[digitArr[i]];
                temp[index] = sortedArr[i];
            }

            lengthInc += 1;
            System.arraycopy(temp, 0, sortedArr, 0, sortedArr.length);
        }
        return sortedArr;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        return;
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }
}

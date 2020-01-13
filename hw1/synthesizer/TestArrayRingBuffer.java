package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        //ArrayRingBuffer arb = new ArrayRingBuffer(10);
        BoundedQueue<Integer> arb = new ArrayRingBuffer<>(5);
        assertEquals(arb.capacity(), 5);
        assertTrue(arb.isEmpty());
        arb.enqueue(1);
        arb.enqueue(2);
        assertFalse(arb.isEmpty());
        assertEquals(arb.peek(), Integer.valueOf(1));

        Integer ret = arb.dequeue();
        assertEquals(ret, Integer.valueOf(1));
        assertEquals(arb.peek(), Integer.valueOf(2));
        ret = arb.dequeue();
        assertEquals(ret, Integer.valueOf(2));

        for (int i = 5; i >= 1; i--) {
            arb.enqueue(i);
        }
        assertTrue(arb.isFull());
        assertEquals(arb.peek(), Integer.valueOf(5));
        ret = arb.dequeue();
        assertFalse(arb.isFull());
        assertEquals(ret, Integer.valueOf(5));
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 

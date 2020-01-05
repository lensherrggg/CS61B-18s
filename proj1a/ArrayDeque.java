import java.sql.ClientInfoStatus;

public class ArrayDeque<T> {
    private T[] items;
    private int first;
    private int last;
    private int size;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        first = 0;
        last = 1;
    }

    /* add and must take constant time, except during resizing operations. */

    private void resize(int capacity) {
        T[] newItems = (T[]) new Object[capacity];
        int oldIndex = (first + 1) % items.length;
        int newIndex;
        for (newIndex = 1; newIndex <= size; newIndex++) {
            newItems[newIndex] = items[oldIndex];
            oldIndex = (oldIndex + 1) % items.length;
        }
        first = 0;
        last = size + 1;
        items = newItems;
    }

    /* Adds an item of type T to the front of the deque. */
    public void addFirst(T item) {
        if (size == items.length) {
            resize(2 * size);
        }
        items[first] = item;
        // In Java -5 % 6 = -5
        first = (first - 1 + items.length) % items.length;
        size++;
    }

    /* Adds an item of type T to the back of the deque. */
    public void addLast(T item) {
        if (size == items.length) {
            resize(2 * size);
        }
        items[last] = item;
        last = (last + 1) % items.length;
        size++;
    }

    /* Returns true if deque is empty, false otherwise. */
    public boolean isEmpty() {
        return size == 0;
    }

    /* Returns the number of items in the deque.
     * must take constant time */
    public int size() {
        return size;
    }

    /* Prints the items in the deque from first to last, separated by a space. */
    public void printDeque() {
        int count = 0;
        int toPrint = (first + 1) % items.length;
        while (count < size - 1) {
            System.out.print(items[toPrint] + " ");
            toPrint = (toPrint + 1) % items.length;
            count++;
        }
        System.out.print(toPrint);
        System.out.println();
    }

    /* Removes and returns the item at the front of the deque.
     * If no such item exists, returns null. */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        first = (first + 1) % items.length;
        T removedItem = items[first];
        items[first] = null;
        size--;
        if ((4 * size < items.length) && items.length >= 16) {
            resize(items.length / 2);
        }
        return removedItem;
    }

    /* Removes and returns the item at the back of the deque.
     * If no such item exists, returns null. */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        last = (last - 1 + items.length) % items.length;
        T removedItem = items[last];
        items[last] = null;
        size--;
        if ((4 * size < items.length) && items.length >= 16) {
            resize(items.length / 2);
        }
        return removedItem;
    }

    /* Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     * Must use iteration, not recursion.
     * Must take constant time. */
    public T get(int index) {
        if (index > size) {
            return null;
        }
        return items[(first + index) % items.length];
    }

    public static void main(String[] args) {
        ArrayDeque<Integer> List = new ArrayDeque<>();
        for(int i = 0; i < 15; i++) {
            List.addLast(i);
        }
        List.addFirst(15);
        List.addLast(16);
        List.printDeque();
        System.out.println(List.get(6));
        System.out.println(List.get(8));
    }
}

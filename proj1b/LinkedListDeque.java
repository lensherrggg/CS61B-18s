public class LinkedListDeque<T> implements Deque<T>{
    public class TNode {
        private T item;
        private TNode prev;
        private TNode next;

        public TNode(T i, TNode p, TNode n) {
            item = i;
            prev = p;
            next = n;
        }
    }

    private TNode sentinel;
    private int size;

    /* Creates an empty linked list deque */
    public LinkedListDeque() {
        sentinel = new TNode(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    /* add and remove must not use any looping or recursion,
     * and take constant time */

    /* Adds an item of type T to the front of the deque. */
    @Override
    public void addFirst(T item) {
        sentinel.next = new TNode(item, sentinel, sentinel.next);
        sentinel.next.next.prev = sentinel.next;
        size++;
    }

    /* Adds an item of type T to the back of the deque. */
    @Override
    public void addLast(T item) {
        sentinel.prev = new TNode(item, sentinel.prev, sentinel);
        sentinel.prev.prev.next = sentinel.prev;
        size++;
    }

    /* Returns true if deque is empty, false otherwise. */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /* Returns the number of items in the deque.
     * must take constant time */
    @Override
    public int size() {
        return size;
    }

    /* Prints the items in the deque from first to last, separated by a space. */
    @Override
    public void printDeque() {
        TNode toPrintNode = sentinel.next;
        for (int i = 0; i < size - 1; i++) {
            System.out.print(toPrintNode.item + " ");
            toPrintNode = toPrintNode.next;
        }
        System.out.print(toPrintNode.item);
        System.out.println();
    }

    /* Removes and returns the item at the front of the deque.
     * If no such item exists, returns null. */
    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T removedItem = sentinel.next.item;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        size--;
        return removedItem;
    }

    /* Removes and returns the item at the back of the deque.
     * If no such item exists, returns null. */
    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T removedItem = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        size--;
        return removedItem;

    }

    /* Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     * must use iteration but not recursion */
    @Override
    public T get(int index) {
        if (index > size - 1) {
            return null;
        }
        TNode toGetNode = sentinel.next;
        for (int i = 0; i < index; i++) {
            toGetNode = toGetNode.next;
        }
        return toGetNode.item;
    }

    /* Same as get, but uses recursion */
    private T getRecursiveHelper(int index, TNode t) {
        if (index == 0) {
            return t.item;
        } else {
            return getRecursiveHelper(index - 1, t.next);
        }
    }

    public T getRecursive(int index) {
        return getRecursiveHelper(index, sentinel.next);
    }
}

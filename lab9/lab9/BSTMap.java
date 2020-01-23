package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }
        if (p.key.compareTo(key) == 0) {
            return p.value;
        } else if (p.key.compareTo(key) > 0) {
            return getHelper(key, p.left);
        } else {
            return getHelper(key, p.right);
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            return new Node(key, value);
        }
        if (p.key.compareTo(key) > 0) {
            p.left = putHelper(key, value, p.left);
        } else if (p.key.compareTo(key) < 0){
            p.right = putHelper(key, value, p.right);
        } else {
            p.value = value;
        }
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
        size += 1;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    private Set<K> keySet = new HashSet<>();
    @Override
    public Set<K> keySet() {
        pushSet(root, keySet);
        return keySet;
    }

    private void pushSet(Node p, Set<K> keySet) {
        if (p.left != null) {
            pushSet(p.left, keySet);
        }
        keySet.add(p.key);
        if (p.right != null) {
            pushSet(p.right, keySet);
        }
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        V t = get(key);
        root = remove(root, key);
        return t;
    }

    private Node findMin(Node p) {
        if (p.left == null) {
            return p;
        } else {
            return findMin(p.left);
        }
    }

    private Node findMax(Node p) {
        if (p.right == null) {
            return p;
        } else {
            return findMax(p.right);
        }
    }

    private Node removeMin(Node p) {
        if (p.left == null) {
            return p.right;
        }
        p.left = removeMin(p.left);
        return p;
    }

    private Node remove(Node p, K key) {
        if (p == null) {
            return null;
        }
        if (p.key.compareTo(key) > 0) {
            p.left = remove(p.left, key);
            return p;
        } else if (p.key.compareTo(key) < 0) {
            p.right = remove(p.right, key);
            return p;
        } else {
            if (p.left == null) {
                Node rightNode = p.right;
                p.right = null;
                size -= 1;
                return rightNode;
            }
            if (p.right == null) {
                Node leftNode = p.left;
                p.left = null;
                size -= 1;
                return leftNode;
            }
            Node successor = findMin(p.right);
            successor.right = removeMin(p.right);
            successor.left = p.left;

            p.left = null;
            p.right = null;
            return successor;
        }
    }




    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        if (get(key) == value) {
            return null;
        }
        return remove(key);
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
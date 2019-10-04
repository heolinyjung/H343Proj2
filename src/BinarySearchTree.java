//////////////////////////////////////////////////////////////////////
//
// H343
// Prof. Siek
// Project 2: Segment Intersection
// Group Members: Heoliny, Joe, Rafael
//
//////////////////////////////////////////////////////////////////////

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * TODO: This is your first major task.
 * This class implements a generic unbalanced binary search tree (BST).
 */

public class BinarySearchTree <K> implements Tree <K> {

    /**
     * A Node is a Location (defined in Tree.java), which means that it can be the return value
     * of a search on the tree.
     */

    class Node implements Location<K> {

        protected K data;
        protected Node left, right;
        protected Node parent;
        protected int height;

        /**
         * Constructs a leaf node with the given key.
         */
        public Node(K key) {
            this(key, null, null);
            this.height = 0;
        }

        /**
         * Constructs a new node with the given values for fields.
         */
        public Node(K data, Node left, Node right) {
            this.data = data;
            this.left = left;
            this.right = right;
            updateHeight();
        }

        public Node(K data, Node left, Node right, Node parent) {
            this.data = data;
            this.left = left;
            this.right = right;
            this.parent = parent;
            updateHeight();
        }

        /*
         * Provide the get() method required by the Location interface.
         */
        @Override
        public K get() {
            return data;
        }

        /**
         * Return true iff this node is a leaf in the tree.
         */
        protected boolean isLeaf() {
            return left == null && right == null;
        }

        /**
         * Performs a local update on the height of this node. Assumes that the
         * heights in the child nodes are correct. Returns true iff the height
         * actually changed. This function *must* run in O(1) time.
         */
        protected boolean updateHeight() {
            int origH = this.height;
            if(left == null && right == null){
                height = 0;
            } else if (left == null){
                height = right.height + 1;
            } else if (right == null) {
                height = left.height + 1;
            } else {
                if (left.height >= right.height){
                    height = left.height + 1;
                } else {
                    height = right.height + 1;
                }
            }
            if (origH == this.height){
                return false;
            } else {
                return true;
            }
        }

        /**
         * Helper for insert method in tree class
         * @param k
         * @return
         */
        public boolean insert(K k) {
            boolean inserted;
            if (lessThan.test(k,data)) {
                if (left == null) {
                    left = new Node(k, null, null, this);
                    inserted = true;
                } else
                    inserted = left.insert(k);
            } else if (lessThan.test(data,k)) {
                if (right == null) {
                    right = new Node(k, null, null, this);
                    inserted = true;
                } else
                    inserted = right.insert(k);
            } else
                inserted = false;
            updateHeight();
            return inserted;
        }

        /**
         * Returns the location of the node containing the inorder predecessor
         * of this node.
         */
        public Node next() {
            if (right != null){
                return right.first();
            } else {
                return nextAncestor();
            }
        }

        /**
         * Returns the location of the node containing the inorder successor
         * of this node.
         */
        public Node previous() {
            if (left != null){
                return left.last();
            } else {
                return prevAncestor();
            }
        }

        /**
         * This method should return the closest ancestor of node q
         * whose key is less than q's key. It is not necessary to
         * to perform key comparisons to implement this method.
         */
        public Node prevAncestor() {
            if (parent != null && this == parent.left){
                return parent.prevAncestor();
            } else {
                return parent;
            }
        }

        /**
         * This method should return the closest ancestor of node q
         * whose key is greater than q's key. It is not necessary to
         * to perform key comparisons to implement this method.
         */
        public Node nextAncestor() {
            if (parent != null && this == parent.right){
                return parent.nextAncestor();
            } else {
                return parent;
            }
        }

        /**
         * This method should return the node in the subtree rooted at 'this'
         * that has the smallest key.
         */
        public Node first() {
            if (this.left == null){
                return this;
            } else {
                return left.first();
            }
        }

        /**
         * This method should return the node in the subtree rooted at 'this'
         * that has the largest key.
         */
        public Node last() {
            if (this.right == null){
                return this;
            } else {
                return right.last();
            }
        }

        public void inorder(List<K> out) {
            if(left != null)
                left.inorder(out);

            out.add(data);

            if(right != null)
                right.inorder(out);
        }

        public boolean isAVL() {
            int h1, h2;
            h1 = get_height(left);
            h2 = get_height(right);
            return Math.abs(h2 - h1) < 2;
        }

        public String toString() {
            return toStringPreorder(this);
        }

    }

    protected Node root;
    protected int numNodes;
    protected BiPredicate<K, K> lessThan;

    /**
     * Constructs an empty BST, where the data is to be organized according to
     * the lessThan relation.
     */
    public BinarySearchTree(BiPredicate<K, K> lessThan) {
        this.lessThan = lessThan;
    }

    /**
     * Looks up the key in this tree and, if found, returns the
     * location containing the key.
     */
    public Node search(K key) {
        return searchFind(root, key);
    }

    //Search helper
    private Node searchFind(Node n, K key) {
        if (n == null) {
            return null;
        } else if (lessThan.test(key,n.data)) {
            return searchFind(n.left, key);
        } else if (lessThan.test(n.data,key)) {
            return searchFind(n.right, key);
        } else {
            return n;
        }
    }

    /**
     * Returns the height of this tree. Runs in O(1) time!
     */
    public int height() {
        return root.height;
    }

    /**
     * Clears all the keys from this tree. Runs in O(1) time!
     */
    public void clear() {
        root = null;
        numNodes = 0;
    }

    /**
     * Returns the number of keys in this tree.
     */
    public int size() {
        return numNodes;
    }

    /**
     * Inserts the given key into this BST, as a leaf, where the path
     * to the leaf is determined by the predicate provided to the tree
     * at construction time. The parent pointer of the new node and
     * the heights in all node along the path to the root are adjusted
     * accordingly.
     * Note: we assume that all keys are unique. Thus, if the given
     * key is already present in the tree, nothing happens.
     * Returns the location where the insert occurred (i.e., the leaf
     * node containing the key), or null if the key is already present.
     */
    public Node insert(K key) {
        if (root == null) {
            root = new Node(key, null, null);
        } else {
            root.insert(key);
        }
        numNodes++;
        return search(key);
    }

    /**
     * Returns a textual representation of this BST.
     */
    public String toString() {
        return toStringPreorder(root);
    }

    /**
     * Returns true iff the given key is in this BST.
     */
    public boolean contains(K key) {
        Node p = search(key);
        return p != null;
    }

    /**
     * Removes the key from this BST. If the key is not in the tree,
     * nothing happens.
     */
    public void remove(K key) {
        remove_helper(root,key);
    }

    private Node remove_helper(Node n, K key) {
        if (n == null) {
            return null;
        } else if (lessThan.test(key,n.data)) { // remove in left subtree
            n.left = remove_helper(n.left, key);
            n.updateHeight();
            return n;
        } else if (lessThan.test(n.data,key)) { // remove in right subtree
            n.right = remove_helper(n.right, key);
            n.updateHeight();
            return n;
        } else { // remove this node
            --numNodes;
            if (n.left == null) {
                return n.right;
            } else if (n.right == null) {
                return n.left;
            } else { // two children, replace this with min of right subtree
                Node min = get_min(n.right);
                n.data = min.data;
                n.right = delete_min(n.right);
                return n;
            }
        }
    }

    private Node delete_min(Node n) {
        if (n.left == null) {
            return n.right;
        } else {
            n.left = delete_min(n.left);
            n.updateHeight();
            return n;
        }
    }

    private Node get_min(Node n) {
        if (n.left == null) return n;
        else return get_min(n.left);
    }

    /**
     * Returns a sorted list of all the keys in this tree.
     */
    public List<K> keys() {
        List<K> list = new LinkedList<>();
        if(root != null)
            root.inorder(list);

        return list;
    }

    /**
     * Finds the node with the specified key, or if there is none, the parent of
     * where such a node would be.
     * @param key
     * @param curr  The current node.
     * @param parent  The parent of the current node.
     * @return A node whose data == key or else the parent of where the node would be.
     */
    protected Node find(K key, Node curr, Node parent) {
        if (curr == null)
            return parent;
        else if (lessThan.test(key, curr.data))
            return find(key, curr.left, curr);
        else if (lessThan.test(curr.data, key))
            return find(key, curr.right, curr);
        else
            return curr;
    }

    private String toStringInorder(Node p) {
        if (p == null)
            return ".";
        String left = toStringInorder(p.left);
        if (left.length() != 0) left = left + " ";
        String right = toStringInorder(p.right);
        if (right.length() != 0) right = " " + right;
        String data = p.data.toString();
        return "(" + left + data + right + ")";
    }

    private String toStringPreorder(Node p) {
        if (p == null)
            return ".";
        String left = toStringPreorder(p.left);
        if (left.length() != 0) left = " " + left;
        String right = toStringPreorder(p.right);
        if (right.length() != 0) right = " " + right;
        String data = p.data.toString();
        return "(" + data + "[" + p.height + "]" + left + right + ")";
    }

    /**
     * The get_height method returns the height of the Node n, which may be null.
     */
    protected int get_height(Node n) {
        if (n == null) return -1;
        else return n.height;
    }
}

//////////////////////////////////////////////////////////////////////
//
// H343
// Prof. Siek
// Project 2: Segment Intersection
// Group Members: Heoliny, Joe, Rafael
//
//////////////////////////////////////////////////////////////////////

import java.util.function.BiPredicate; //what is this?

/**
 * TODO: This is your second major task.
 * <p>
 * This class implements a height-balanced binary search tree,
 * using the AVL algorithm. Beyond the constructor, only the insert()
 * and remove() methods need to be implemented. All other methods are unchanged.
 */

public class AVLTree<K> extends BinarySearchTree<K> {

    /**
     * Creates an empty AVL tree as a BST organized according to the
     * lessThan predicate.
     */
    public AVLTree(BiPredicate<K, K> lessThan) {
        super(lessThan);
    }

    /**
     * TODO
     * Inserts the given key into this AVL tree such that the ordering
     * property for a BST and the balancing property for an AVL tree are
     * maintained.
     */
    public Node insert(K key) {
        Node insertedNode = super.insert(key);
        Node curr = insertedNode.parent;

        while(curr != null && !curr.isAVL()) {
            curr.updateHeight();

            if(!curr.isAVL())
                rebalance(curr);

            curr = curr.parent;
        }

        return insertedNode;
    }

    public void remove(K key) {
        super.remove(key);
        Node curr = find(key,root,null);

        while(curr != null && !curr.isAVL()) {
            curr.updateHeight();

            if(!curr.isAVL())
                rebalance(curr);

            curr = curr.parent;
        }
    }

    public void rebalance(Node curr) {
        if (get_height(curr.left) < get_height(curr.right)) {
            if (get_height(curr.right.left) <= get_height(curr.right.right)) {
                leftRotate(curr.left);
            } else if (get_height(curr.right.left) > get_height(curr.right.right)) {
                rightRotate(curr.right);
                leftRotate(curr);
            }
        } else if (get_height(curr.left) > get_height(curr.right)) {
            if (get_height(curr.left.left) < get_height(curr.left.right)) {
                leftRotate(curr.right);
                rightRotate(curr);
            } else if (get_height(curr.left.left) < get_height(curr.left.right)) {
                rightRotate(curr);
            }
        }
    }

    public Node rightRotate(Node x){
        if (x != null) {
            Node y = x.left;
            Node B = null;
            if (y != null) {
                B = y.right;
            }
            Node p = x.parent;
            if (x.data.equals(p.left.data)) {
                p.left = y;
                y.parent = p;
            } else {
                p.right = y;
                y.parent = p;
            }
            x.left = B;
            y.right = x;
            return x;
        }
        else {
            return null;
        }
    }

    public Node leftRotate(Node x){
        Node y = x.right;
        Node T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        //  Update heights
        x.height = max(get_height(x.left), get_height(x.right)) + 1;
        y.height = max(get_height(y.left), get_height(y.right)) + 1;

        // Return new root
        return y;
    }

    private int max(int a, int b) {
        return (a > b) ? a : b;
    }
}

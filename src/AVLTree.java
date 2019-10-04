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

        while(curr != null) {
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
                leftRotate(curr);
            } else {
                rightRotate(curr.right);
                leftRotate(curr);
            }
        } else if (get_height(curr.left) > get_height(curr.right)) {
            if (get_height(curr.left.left) < get_height(curr.left.right)) {
                leftRotate(curr.right);
                rightRotate(curr);
            } else {
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
            Node p = null;
            if (x.parent != null)
                p = x.parent;
            if (p != null) {
                if (p.left != null && x.data.equals(p.left.data)) {
                    p.left = y;
                } else {
                    p.right = y;
                }
            }
            if (y != null) {
                y.parent = p;
                x.parent = y;
                x.left = B;
                y.right = x;
                y.updateHeight();
            } else
                x.parent = null;
            if (B != null)
                B.parent = x;
            x.updateHeight();
            root = y;
            return x;
        }
        else {
            return null;
        }
    }

    public Node leftRotate(Node x){
        if (x != null) {
            Node y = x.right;
            Node B = null;
            if (y != null) {
                B = y.left;
            }
            Node p = null;
            if (x.parent != null)
                p = x.parent;
            if (p != null) {
                if (p.right != null && x.data.equals(p.right.data)) {
                    p.right = y;
                } else {
                    p.left = y;
                }
            }
            if (y != null) {
                y.parent = p;
                x.parent = y;
                x.right = B;
                y.left = x;
                y.updateHeight();
            } else
                x.parent = null;
            if (B != null)
                B.parent = x;
            x.updateHeight();
            root = y;
            return x;
        }
        else {
            return null;
        }
    }
}

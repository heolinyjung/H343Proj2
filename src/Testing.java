//////////////////////////////////////////////////////////////////////
//
// H343
// Prof. Siek
// Project 2: Segment Intersection
// Group Members: Heoliny, Joe, Rafael
//
//////////////////////////////////////////////////////////////////////

import org.junit.Assert.*;
import org.junit.Test;

import java.util.*;
import java.util.function.BiPredicate;

import static org.junit.Assert.*;

public class Testing {

    @Test
    /**
     * Start with this test. Make appropriate changes to BinarySearchTree.Node.
     */
    public void nodeProperties() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        assertTrue(bst.isEmpty());
        BinarySearchTree<Integer>.Node p;
        p = bst.new Node(5);
        assertNull(p.left);
        assertNull(p.right);
        assertNull(p.parent);
        assertTrue(5 == p.data);
        assertTrue(5 == p.get());
        assertEquals(0, p.height);
        assertTrue(p.isLeaf());
        p = bst.new Node(6, p, null);
        assertTrue(6 == p.data);
        assertTrue(6 == p.get());
        p.updateHeight();
        assertEquals(1, p.height);
        p = bst.new Node(7, null, p);
        assertTrue(7 == p.data);
        assertTrue(7 == p.get());
        p.updateHeight();
        assertEquals(2, p.height);
        p.left = bst.new Node(8);
        assertTrue(8 == p.left.data);
        assertTrue(8 == p.left.get());
        p.updateHeight();
        assertEquals(2, p.height);
        p.left.left = p.right;
        p.left.updateHeight();
        assertEquals(2, p.height);
        p.updateHeight();
        assertEquals(3, p.height);
    }

    @Test
    /**
     * When you're ready to run this test, remove the // from the above line.
     */
    public void insertSmallBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        assertTrue(bst.isEmpty());
        int[] a = new int[]{4, 8, 0, 2, 6, 10};
        int n = 0;
        for (Integer key : a) {
            bst.insert(key);
            n++;
            assertEquals(n, bst.size());
        }
        /**
         *       4
         *     /  \
         *    /    \
         *   0      8
         *    \    / \
         *     2  6   10
         */
        assertTrue(4 == bst.root.data);
        assertTrue(0 == bst.root.left.data);
        assertTrue(2 == bst.root.left.right.data);
        assertTrue(8 == bst.root.right.data);
        assertTrue(6 == bst.root.right.left.data);
        assertTrue(10 == bst.root.right.right.data);

        assertTrue(verifyParentPointers(bst.root));
    }

    @Test
    public void clearSmallBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        assertTrue(bst.isEmpty());
        int[] a = new int[]{4, 8, 0, 2, 6, 10};
        for (Integer key : a)
            bst.insert(key);
        bst.clear();
        assertEquals(0, bst.size());
        assertNull(bst.root);
        for (Integer key : a)
            bst.insert(key);
        assertEquals(a.length, bst.size());
        assertNotNull(bst.root);
    }

    @Test
    public void insertReturnBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        assertTrue(bst.isEmpty());
        int[] a = new int[]{4, 8, 0, 2, 6, 10};
        for (Integer key : a)
            assertEquals(key, bst.insert(key).get());
    }

    @Test
    public void skinnyBST() {
        BinarySearchTree<String> bst =
                new BinarySearchTree<>((String x, String y) -> x.compareTo(y) < 0);
        String[] a = new String[]{"ape", "boa", "cat", "dog", "emu", "fox", "gnu", "hog"};
        for (String key : a)
            bst.insert(key);
        assertEquals("ape", bst.root.data);
        assertEquals("boa", bst.root.right.data);
        assertEquals("cat", bst.root.right.right.data);
        assertEquals("dog", bst.root.right.right.right.data);
        assertEquals("emu", bst.root.right.right.right.right.data);
        assertEquals("fox", bst.root.right.right.right.right.right.data);
        assertEquals("gnu", bst.root.right.right.right.right.right.right.data);
        assertEquals("hog", bst.root.right.right.right.right.right.right.right.data);
    }

    @Test
    public void parentsSmallBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{4, 8, 0, 2, 6, 10};
        for (Integer key : a)
            bst.insert(key);
        assertTrue(null == bst.root.parent);
        assertTrue(bst.root == bst.root.left.parent);
        assertTrue(bst.root.left == bst.root.left.right.parent);
        assertTrue(bst.root == bst.root.right.parent);
        assertTrue(bst.root.right == bst.root.right.left.parent);
        assertTrue(bst.root.right == bst.root.right.right.parent);
    }

    @Test
    public void searchSmallBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{4, 8, 0, 2, 6, 10};
        for (Integer key : a)
            bst.insert(key);
        Location<Integer> loc;
        for (Integer key : a) {
            assertTrue(bst.contains(key));
            assertFalse(bst.contains(key + 1));
            loc = bst.search(key);
            assertNotNull(loc);
            assertEquals(key, loc.get());
            loc = bst.search(key + 1);
            assertNull(bst.search(key + 1));
        }
    }

    @Test
    public void heightSmallBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{4, 8, 0, 2, 6, 10};
        for (Integer key : a) {
            bst.insert(key);
            assertTrue(verifyHeights(bst.root));
        }
        bst.insert(7);
        assertTrue(verifyHeights(bst.root));
    }

    @Test
    public void removeSmallBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        int[] a = {4, 8, 0, 2, 6, 10};
        for (Integer key : a)
            bst.insert(key);
        int size = a.length;
        for (Integer key : a) {
            bst.remove(key);
            size--;
            assertEquals(size, bst.size());
            assertFalse(bst.contains(key));
            // Removing a key not in the tree should do nothing.
            bst.remove(key + 1);
            assertEquals(size, bst.size());
            assertFalse(bst.contains(key));
            bst.remove(key);
            assertEquals(size, bst.size());
            assertFalse(bst.contains(key));
        }
        assertTrue(bst.isEmpty());
        assertTrue(verifyParentPointers(bst.root));
    }

    @Test
    public void dupsSmallBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{4, 8, 0, 2, 6, 10};
        for (Integer key : a)
            bst.insert(key);
        // Attempts to insert a duplicate key should be ignored.
        for (Integer key : a) {
            bst.insert(key);
            assertEquals(6, bst.size());
        }
        // Attempts to insert a previously removed key should reuse the dirty node.
        int size = a.length, dirtyCount = 0;
        for (Integer key : a) {
            bst.remove(key);
            size--;
            dirtyCount++;
            assertEquals(size, bst.size());
            assertFalse(bst.contains(key));
        }
        assertTrue(bst.isEmpty());
        size = 0;
        for (Integer key : a) {
            bst.insert(key);
            dirtyCount--;
            size++;
            assertEquals(size, bst.size());
        }
    }

    @Test
    public void keysSmallBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{4, 8, 0, 2, 6, 10};
        for (Integer key : a)
            bst.insert(key);
        List<Integer> ks = bst.keys();
        assertEquals(a.length, ks.size());
        for (int i = 1; i < ks.size(); i++)
            assertTrue(ks.get(i - 1) <= ks.get(i));
        bst.clear();
        assertTrue(bst.keys().isEmpty());
    }

    @Test
    public void beforeBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        assertTrue(bst.isEmpty());
        int[] a = new int[]{4, 5, 8, 9, 3, 2, 7, 6, 1};
        int n = a.length;
        for (Integer key : a)
            bst.insert(key);
        assertNull(bst.search(1).previous());
        for (int i = 2; i <= n; i++)
            assertTrue(i - 1 == bst.search(i).previous().get());
    }

    @Test
    public void afterBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        assertTrue(bst.isEmpty());
        int[] a = new int[]{4, 5, 8, 9, 3, 2, 7, 6, 1};
        int n = a.length;
        for (Integer key : a)
            bst.insert(key);
        for (int i = 1; i < n; i++)
            assertTrue(i + 1 == bst.search(i).next().get());
        assertNull(bst.search(n).next());
    }

    /**********************************************************************************
     * Add test cases for insert and remove on a tree with 100 randomly generated keys.
     */

    /**********************************************************************************
     * When you've reached this point, run the Driver to see the Line Sweep Algorithm
     * in action.
     **********************************************************************************/

    /*
    @Test
    public void insertAVL() {
        BinarySearchTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{3, 8, 1, 2, 7, 9};
        for (Integer key : a)
            avl.insert(key);
        assertEquals(2, avl.height());
        for (Integer key : a) {
            assertNotNull(avl.search(key));
            assertEquals(key, avl.search(key).get());
        }
        for (Integer key : a) {
            avl.remove(key);
            assertFalse(avl.contains(key));
        }
        assertTrue(avl.isEmpty());
        assertEquals(0, avl.size());
        assertEquals(-1, avl.height());
        assertTrue(verifyParentPointers(avl.root));
    }



    @Test
    public void LLtinyAVL() {
        BinarySearchTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{3, 2, 1};
        for (Integer key : a)
            avl.insert(key);
        // Check size
        assertEquals(3, avl.size());
        // Check keys
        assertTrue(2 == avl.root.data);
        assertTrue(1 == avl.root.left.data);
        assertTrue(3 == avl.root.right.data);
        // Check parents
        assertNull(avl.root.parent);
        assertEquals(avl.root, avl.root.left.parent);
        assertEquals(avl.root, avl.root.right.parent);
        // Check heights
        assertEquals(1, avl.height());
        assertEquals(1, avl.root.height);
        assertEquals(0, avl.root.left.height);
        assertEquals(0, avl.root.right.height);
    }

    @Test
    public void RRtinyAVL() {
        BinarySearchTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{1, 2, 3};
        for (Integer key : a)
            avl.insert(key);
        // Check size
        assertEquals(3, avl.size());
        // Check keys
        assertTrue(2 == avl.root.data);
        assertTrue(1 == avl.root.left.data);
        assertTrue(3 == avl.root.right.data);
        // Check parents
        assertNull(avl.root.parent);
        assertEquals(avl.root, avl.root.left.parent);
        assertEquals(avl.root, avl.root.right.parent);
        // Check heights
        assertEquals(1, avl.height());
        assertEquals(1, avl.root.height);
        assertEquals(0, avl.root.left.height);
        assertEquals(0, avl.root.right.height);
    }

    @Test
    public void LRtinyAVL() {
        BinarySearchTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{3, 1, 2};
        for (Integer key : a)
            avl.insert(key);
        // Check size
        assertEquals(3, avl.size());
        // Check keys
        assertTrue(2 == avl.root.data);
        assertTrue(1 == avl.root.left.data);
        assertTrue(3 == avl.root.right.data);
        // Check parents
        assertNull(avl.root.parent);
        assertEquals(avl.root, avl.root.left.parent);
        assertEquals(avl.root, avl.root.right.parent);
        // Check heights
        assertEquals(1, avl.height());
        assertEquals(1, avl.root.height);
        assertEquals(0, avl.root.left.height);
        assertEquals(0, avl.root.right.height);
    }

    @Test
    public void RLtinyAVL() {
        BinarySearchTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{1, 3, 2};
        for (Integer key : a)
            avl.insert(key);
        // Check size
        assertEquals(3, avl.size());
        // Check keys
        assertTrue(2 == avl.root.data);
        assertTrue(1 == avl.root.left.data);
        assertTrue(3 == avl.root.right.data);
        // Check parents
        assertNull(avl.root.parent);
        assertEquals(avl.root, avl.root.left.parent);
        assertEquals(avl.root, avl.root.right.parent);
        // Check heights
        assertEquals(1, avl.height());
        assertEquals(1, avl.root.height);
        assertEquals(0, avl.root.left.height);
        assertEquals(0, avl.root.right.height);
    }

    @Test
    public void LLsmallAVL() {
        BinarySearchTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{5, 6, 3, 2, 4, 1};
        for (Integer key : a)
            avl.insert(key);
        assertEquals(6, avl.size());
        assertTrue(verifyAVL(avl.root));
        assertTrue(verifyParentPointers(avl.root));
        assertTrue(verifyHeights(avl.root));
    }

    @Test
    public void RRsmallAVL() {
        BinarySearchTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{2, 1, 4, 3, 5, 6};
        for (Integer key : a)
            avl.insert(key);
        assertEquals(6, avl.size());
        assertTrue(verifyAVL(avl.root));
        assertTrue(verifyParentPointers(avl.root));
        assertTrue(verifyHeights(avl.root));
    }

    @Test
    public void LRsmallAVL() {
        BinarySearchTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{5, 2, 6, 1, 3};
        for (Integer key : a)
            avl.insert(key);
        assertTrue(verifyAVL(avl.root));
        avl.insert(4);
        assertTrue(verifyAVL(avl.root));
        assertTrue(verifyParentPointers(avl.root));
        assertTrue(verifyHeights(avl.root));
    }

    @Test
    public void RLsmallAVL() {
        BinarySearchTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{2, 1, 5, 3, 6};
        for (Integer key : a)
            avl.insert(key);
        assertTrue(verifyAVL(avl.root));
        avl.insert(4);
        assertTrue(verifyAVL(avl.root));
        assertTrue(verifyParentPointers(avl.root));
        assertTrue(verifyHeights(avl.root));
    }

    @Test
    public void keysSmallAVL() {
        AVLTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{4, 8, 0, 2, 6, 10};
        for (Integer key : a)
            avl.insert(key);
        List<Integer> ks = avl.keys();
        assertEquals(a.length, ks.size());
        for (int i = 1; i < ks.size(); i++)
            assertTrue(ks.get(i - 1) <= ks.get(i));
    }



    @Test
    public void rebuildSmallAVL() {
        AVLTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{4, 8, 0, 2, 6, 10};
        for (Integer key : a) {
            avl.insert(key);
            assertTrue(verifyAVL(avl.root));
        }
        int n = a.length;
        for (Integer key : a) {
            avl.remove(key);
            n--;
            assertEquals(n, avl.size());
            assertNull(avl.search(key));
            assertFalse(avl.contains(key));
            assertTrue(verifyParentPointers(avl.root));
            assertTrue(verifyOrderingProperty(avl.root, avl.lessThan));
            assertTrue(verifyHeights(avl.root));
            assertTrue(verifyAVL(avl.root));
        }
        assertNull(avl.root);
        assertEquals(-1, avl.height());
    }

    /**********************************************************************************
     * When you've reached this point, you're ready to replace the BST with AVL in
     * the Driver.
     **********************************************************************************/

    private <K> int get_height(BinarySearchTree<K>.Node p) {
        if (p == null) return -1;
        else return p.height;
    }

    private <K> boolean verifyHeights(BinarySearchTree<K>.Node p) {
        if (p == null)
            return true;
        int h1 = get_height(p.left);
        int h2 = get_height(p.right);
        return p.height == 1 + Math.max(h1, h2) &&
                verifyHeights(p.left) && verifyHeights(p.right);
    }

    private <K> boolean verifyAVL(BinarySearchTree<K>.Node p) {
        if (p == null)
            return true;
        int h1 = get_height(p.left);
        int h2 = get_height(p.right);
        if (Math.abs(h1 - h2) > 1) {
            System.out.println("Not AVL");
            System.out.println(p);
            return false;
        } else
            return verifyAVL(p.left) && verifyAVL(p.right);
    }

    private <K> boolean verifyParentPointers(BinarySearchTree<K>.Node root) {
        if (root == null)
            return true;
        else if (root.parent != null)
            return false;
        else
            return verifyParentPointersHelper(root, root.left) && verifyParentPointersHelper(root, root.right);
    }

    private <K> boolean verifyParentPointersHelper(BinarySearchTree<K>.Node p, BinarySearchTree<K>.Node q) {
        if (q == null)
            return true;
        else if (q.parent != p)
            return false;
        else
            return verifyParentPointersHelper(q, q.left) && verifyParentPointersHelper(q, q.right);
    }

    private <K>
    boolean verifyOrderingProperty(BinarySearchTree<K>.Node p, BiPredicate<K, K> lessThan) {
        if (p == null) return true;
        K key = p.data;
        return allLessThan(p.left, key, lessThan) && allGreaterThan(p.right, key, lessThan)
                && verifyOrderingProperty(p.left, lessThan) && verifyOrderingProperty(p.right, lessThan);
    }

    private <K>
    boolean allGreaterThan(BinarySearchTree<K>.Node p, K x, BiPredicate<K, K> lessThan) {
        if (p == null) return true;
        return !lessThan.test(p.data, x)
                && allGreaterThan(p.left, x, lessThan) && allGreaterThan(p.right, x, lessThan);
    }

    private <K>
    boolean allLessThan(BinarySearchTree<K>.Node p, K x, BiPredicate<K, K> lessThan) {
        if (p == null) return true;
        return lessThan.test(p.data, x)
                && allLessThan(p.left, x, lessThan) && allLessThan(p.right, x, lessThan);
    }


}
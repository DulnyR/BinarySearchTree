/*************************************************************************
 *  Binary Search Tree class.
 *  Adapted from Sedgewick and Wayne.
 *
 *  @version 14/11/23 07:33:39
 *
 *  @author Radek Dulny
 *
 *************************************************************************/
package csu22011_a3;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class BST<Key extends Comparable<Key>, Value> {
    private Node root;             // root of BST

    /**
     * Private node class.
     */
    private class Node {
        private Key key;           // sorted by key
        private Value val;         // associated data
        private Node left, right;  // left and right subtrees
        private int N;             // number of nodes in subtree

        public Node(Key key, Value val, int N) {
            this.key = key;
            this.val = val;
            this.N = N;
        }
    }

    public Node getRoot() { return root; }

    // is the symbol table empty?
    public boolean isEmpty() { return size() == 0; }

    // return number of key-value pairs in BST
    public int size() { return size(root); }

    // return number of key-value pairs in BST rooted at x
    private int size(Node x) {
        if (x == null) return 0;
        else return x.N;
    }

    /**
     *  Search BST for given key.
     *  Does there exist a key-value pair with given key?
     *
     *  @param key the search key
     *  @return true if key is found and false otherwise
     */
    public boolean contains(Key key) {
        return get(key) != null;
    }

    /**
     *  Search BST for given key.
     *  What is the value associated with given key?
     *
     *  @param key the search key
     *  @return value associated with the given key if found, or null if no such key exists.
     */
    public Value get(Key key) { return get(root, key); }

    private Value get(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else              return x.val;
    }

    /**
     *  Insert key-value pair into BST.
     *  If key already exists, update with new value.
     *
     *  @param key the key to insert
     *  @param val the value associated with key
     */
    public void put(Key key, Value val) {
        if (val == null) { delete(key); return; }
        root = put(root, key, val);
    }

    private Node put(Node x, Key key, Value val) {
        if (x == null) return new Node(key, val, 1);
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x.left  = put(x.left,  key, val);
        else if (cmp > 0) x.right = put(x.right, key, val);
        else              x.val   = val;
        x.N = 1 + size(x.left) + size(x.right);
        return x;
    }

    /**
     * Tree height.
     *
     * Asymptotic worst-case running time using Theta notation: TODO
     *
     * @return the number of links from the root to the deepest leaf.
     *
     * Example 1: for an empty tree this should return -1.
     * Example 2: for a tree with only one node it should return 0.
     * Example 3: for the following tree it should return 2.
     *   B
     *  / \
     * A   C
     *      \
     *       D
     */
    
     
    public int height() {
      if(isEmpty()) return -1; // (1)
      if(size() == 1) return 0; // (1)
      return heightRecursive(root) - 1;
    }
    
    public int heightRecursive(Node currentNode)
    {
      if(currentNode == null) return 0; // (1)
      int goLeft = heightRecursive(currentNode.left);
      int goRight = heightRecursive(currentNode.right);

      if(goLeft > goRight) return goLeft + 1; //(1)
      else return goRight + 1; //(1)
    }

    //As the heightRecursive function will go down a node with every iteration, its worst case running time is (h)

    /**
     * Median key.
     * If the tree has N keys k1 < k2 < k3 < ... < kN, then their median key 
     * is the element at position (N+1)/2 (where "/" here is integer division)
     *
     * @return the median key, or null if the tree is empty.
     */
    public Key median() {
      Node currentNode = root;
      int median = (Integer) size()/2;
      while(currentNode != null)
      {
        Node leftNode = currentNode.left;
        Node rightNode = currentNode.right;
        int leftKeys, rightKeys;
        if(leftNode != null) leftKeys = leftNode.N - 1;
        else leftKeys = 0;
        if(rightNode != null) rightKeys = rightNode.N - 1;
        else rightKeys = 0;
        if (leftKeys == rightKeys) return currentNode.key;
        if(leftKeys >= median)
        {
          currentNode = leftNode;
        }
        else 
        {
          int keysToLeft = leftKeys + 1; 
          if(keysToLeft == median) return leftNode.key;
          currentNode = rightNode; 
          median -= keysToLeft; 
        } 
      }
      return null;
    }

    /**
     * Print all keys of the tree in a sequence, in-order.
     * That is, for each node, the keys in the left subtree should appear before the key in the node.
     * Also, for each node, the keys in the right subtree should appear before the key in the node.
     * For each subtree, its keys should appear within a parenthesis.
     *
     * Example 1: Empty tree -- output: "()"
     * Example 2: Tree containing only "A" -- output: "(()A())"
     * Example 3: Tree:
     *   B
     *  / \
     * A   C
     *      \
     *       D
     *
     * output: "((()A())B(()C(()D())))"
     *
     * output of example in the assignment: (((()A(()C()))E((()H(()M()))R()))S(()X()))
     *
     * @return a String with all keys in the tree, in order, parenthesized.
     */
    public String printKeysInOrder() {
      if (isEmpty()) return "()";
      return '(' + inOrderRecursive(root) + ')';
    }
    

    public String inOrderRecursive(Node currentNode)
    {
      String inOrder = "(";
      if(currentNode.left != null) inOrder += inOrderRecursive(currentNode.left);
      inOrder += ")" + currentNode.key + "(";
      if(currentNode.right != null) inOrder += inOrderRecursive(currentNode.right);
      return inOrder + ")";
    }
    
    /**
     * Pretty Printing the tree. Each node is on one line -- see assignment for details.
     *
     * @return a multi-line string with the pretty ascii picture of the tree.
     */
    public String prettyPrintKeys() {
      String answer = prettyPrint(root, "");
      System.out.println(answer);
      return answer;
    }

    private String prettyPrint(Node node, String prefix) {
      if(node == null) {
        return prefix + "-null\n";
      }
      String prettyString = prefix + "-" + node.key + "\n";
      String myPrefix = prefix + " ";
      prettyString += prettyPrint(node.left, myPrefix + "|");
      prettyString += prettyPrint(node.right, myPrefix + " ");
      return prettyString;
    }

    /**
     * Deletes a key from a tree (if the key is in the tree).
     * Note that this method works symmetrically from the Hibbard deletion:
     * If the node to be deleted has two child nodes, then it needs to be
     * replaced with its predecessor (not its successor) node.
     *
     * @param key the key to delete
     */
    public void delete(Key key) {
      if(isEmpty()) return;
      if(!contains(key)) return;
      Node parent = getParentNode(root, null, key);
      Node deleteNode;
      boolean right = true;
      boolean isParent = false;
      if(key == root.key) {
        isParent = true;
        deleteNode = root;
      }
      else {
        int compare = 1;
        if(parent.left != null) compare = key.compareTo(parent.left.key);
        if(compare <= 0) 
        { 
          deleteNode = parent.left;
          right = false;
        }
        else deleteNode = parent.right;
      }
      //0 Children
      if(deleteNode.left == null && deleteNode.right == null) 
      {
        if (isParent) root = null;
        else if (right) parent.right = null;
        else parent.left = null;
      }
      //Right Child
      else if(deleteNode.left == null) 
      {
        if (isParent) root = deleteNode.right;
        else if (right) parent.right = deleteNode.right;
        else parent.left = deleteNode.right;
      }
      //Left Child
      else if(deleteNode.right == null) 
      {
        if (isParent) root = deleteNode.left;
        else if (right) parent.right = deleteNode.left;
        else parent.left = deleteNode.left;
      }
      //Both Children
      else 
      {
        Node preNode = deleteNode.left;
        while(preNode.right != null)
        {
          preNode = preNode.right;
        }
        Key tempKey = preNode.key;
        Value tempVal = preNode.val;
        delete(preNode.key);
        if (isParent)
        {
          root.key = tempKey;
          root.val = tempVal;
        }
        else
        {
          deleteNode.key = tempKey;
          deleteNode.val = tempVal;
        }
      }
    }

    private Node getParentNode(Node x, Node parent, Key key) {
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) return getParentNode(x.left, x, key);
        else if (cmp > 0) return getParentNode(x.right, x, key);
        else              return parent;
    }


     /**
     * This method takes an array of Key objects and determines whether the
     * given sequence can represent the pre-order traversal of a BST.
     *
     * @param keys an array of keys
     */
    public boolean isBSTPreOrder(Key[] keys){
      BST<Key, Integer> bst = new BST<Key, Integer>();
      for(int i = 0; i < keys.length; i++)
      {
        bst.put(keys[i], (Integer) keys[i]);
      }
      if(bst.isEmpty()) return true;
      List<Key> myTraversal = preOrderRec(bst.getRoot(), bst);
      for(int i = 0; i < keys.length; i++)
      {
        if(keys[i] != myTraversal.get(i)) return false;
      }
      return true;
    }

    private List<Key> preOrderRec(BST<Key,Integer>.Node node, BST<Key, Integer> bst){
      List<Key> keyList = new ArrayList<>();
      keyList.add(node.key);
      if(node.left != null) keyList.addAll(preOrderRec(node.left, bst));
      if(node.right != null) keyList.addAll(preOrderRec(node.right, bst));
      return keyList;
    }

}

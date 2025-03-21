/**
 * Reusable node for creating linked linear data structures
 * @author GMunt
 */
public class Node<E> {
    private E element; 
    private Node<E> nextNode;

    /**
     * Basic constructor to make Node objects from a known element
     * @param element the known element to assign Node to
     */
    public Node(E element) {
        this.element = element;
        nextNode = null;
    }

    /**
     * Additional constructor to make Node objects if you know both the 
     * element and the next Node
     * @param element the known element to assign Node to 
     * @param nextNode the known Node that should come after
     */
    public Node(E element, Node nextNode) { // Not totally necessary 
        this.element = element;
        this.nextNode = nextNode;
    }

    /**
     * Return the current element
     * @return current element
     */
    public E getElement() {
        return element;
    }

    /**
     * Set the Node element 
     * @param element value to set to
     */
    public void setElement(E element) {
        this.element = element;
    }

    /**
     * Return the next Node object
     * @return the next Node object
     */
    public Node<E> getNextNode() {
        return nextNode;
    }

    /**
     * Set the next Node object
     * @param nextNode the next Node object to set 
     */
    public void setNextNode(Node<E> nextNode) {
        this.nextNode = nextNode;
    }
}

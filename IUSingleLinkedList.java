import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Single-linked-Node-based implementation of IndexedUnsortedList
 * @author GMunt
 * @version Spring 2025
 */
public class IUSingleLinkedList<E> implements IndexedUnsortedList<E> {
    private Node<E> head;
    private Node<E> tail;
    private int size;
    private int modCount;

    public IUSingleLinkedList() {
        head = tail = null;
        size = 0;
        modCount = 0;
    }

    @Override
    public void addToFront(E element) {
        Node<E> newNode = new Node<E>(element); //Could also do (element, head) due to other constructor 
        if (isEmpty()) {
            tail = newNode;
        }
        newNode.setNextNode(head);
        head = newNode;
        size++;
        modCount++;
    }

    @Override
    public void addToRear(E element) {
        Node<E> newNode = new Node<E>(element);
        if (isEmpty()) {
            head = newNode;
        }
        else {
            tail.setNextNode(newNode);
        }
        tail = newNode;
        modCount++;
        size++;
    }

    @Override
    public void add(E element) {
        addToRear(element);
    }

    @Override
    public void addAfter(E element, E target) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node<E> nodeToAdd = new Node<E>(element);
        Node<E> currentNode = head;
        // Traverse through the list until target is found
        while (currentNode != null && !currentNode.getElement().equals(target)) {
            currentNode = currentNode.getNextNode();
        }

        if (currentNode == null) {
            throw new NoSuchElementException();
        }

        // Update node reference values
        nodeToAdd.setNextNode(currentNode.getNextNode());
        currentNode.setNextNode(nodeToAdd);
        // Update tail if Node is added to the end
        if (nodeToAdd.getNextNode() == null) {
            tail = nodeToAdd;
        }
        size++;
        modCount++;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        
        Node<E> nodeToAdd = new Node<E>(element);

        // Check if a value is being added to the front
        if (index == 0) {
            nodeToAdd.setNextNode(head); // Place new Node in front
            head = nodeToAdd; // Update head
            
            if (tail == null) {
                tail = nodeToAdd;
            }
        }
        else {
            Node<E> currentNode = head;

            // Traverse to the index (starting at 1 since if statement above checked index = 0)
            for (int i = 1; i < index; i++) {
                currentNode = currentNode.getNextNode();
            }

            // Update node reference values
            nodeToAdd.setNextNode(currentNode.getNextNode());
            currentNode.setNextNode(nodeToAdd);
            // Update tail if Node is added to the end
            if (nodeToAdd.getNextNode() == null) {
                tail = nodeToAdd;
            }
        }
        size++;
        modCount++;
    }

    @Override
    public E removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        E returnElement = head.getElement();
        head = head.getNextNode();
        if (head == null) { //Check for single element case, use this or size == 1 
            tail = null;
        }
        size--;
        modCount++;
        return returnElement;
    }

    @Override
    public E removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        E retVal = tail.getElement();
        if (head == tail) { // or size == 1
            head = tail = null;
        }
        else {
            Node<E> newTailNode = head;
            while (newTailNode.getNextNode() != tail) {
                newTailNode = newTailNode.getNextNode();
            }
            tail = newTailNode;
            newTailNode.setNextNode(null);
        }
        size--;
        modCount++;
        return retVal;
    }

    @Override
    public E remove(E element) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        E retVal;
        // Check if the element is in the head Node
        if (head.getElement().equals(element)) {
            retVal = head.getElement();
            head = head.getNextNode(); 
            // Check for one element lists
            if (head == null) { // or size == 1
                tail = null;
            }
        } 
        else {
            Node<E> previousNode = null;
            Node<E> nodeToRemove = head;

            // Traverse through each Node until the Node containing the element is found
            while (nodeToRemove != null && !nodeToRemove.getElement().equals(element)) {
                previousNode = nodeToRemove; 
                nodeToRemove = nodeToRemove.getNextNode();
            }

            // if element isn't found, throw an error
            if (nodeToRemove == null) { 
                throw new NoSuchElementException();
            }

            // Grab the element before it is removed
            retVal = nodeToRemove.getElement(); 

            // If head is removed
            if (previousNode == null) {
                head = nodeToRemove.getNextNode();
                // If list is now empty
                if (head == null) {
                    tail = null;
                }
            }
            // If element is in a Node in the middle
            else {
                previousNode.setNextNode(nodeToRemove.getNextNode());
                // Adjust tail if it was removed
                if (nodeToRemove == tail) { 
                    tail = previousNode;
                }
            }
        }
        size--;
        modCount++;
        return retVal;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        E retVal;
        // Check if index is based off of first index
        if (index == 0) {
            retVal = head.getElement();
            head = head.getNextNode();
            // Check if list is empty now
            if (head == null) {
                tail = null;
            }
        }
        else {
            Node<E> previousNode = head;
            Node<E> nodeToRemove = previousNode.getNextNode();

            // Traverse to the index (starting at 1 because previous if check index = 1)
            for (int i = 1; i < index; i++) {
                previousNode = nodeToRemove;
                nodeToRemove = nodeToRemove.getNextNode();
            }

            // Store element before Node is removed
            retVal = nodeToRemove.getElement();
            
            // Remove the Node
            previousNode.setNextNode(nodeToRemove.getNextNode());

            if (nodeToRemove == tail) {
                tail = previousNode;
            }
            }
        size--;
        modCount++;
        return retVal;
    }

    @Override
    public void set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node<E> currentNode = head;
        // Traverse to the index 
        for (int i = 0; i < index; i++) {
            currentNode.getNextNode();
        }
        // Update element
        currentNode.setElement(element);
        modCount++;
    }

    @Override
    public E get(int index) { // n factor now in SLL vs AL single factor
        if (index < 0 || index >=  size) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> currentNode = head;
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.getNextNode();
        }
        return currentNode.getElement();
    }

    @Override
    public int indexOf(E element) {
        Node<E> currentNode = head;
        int currentIndex = 0;
        boolean foundElement = false;
        while (currentNode != null && !foundElement) {
            if (currentNode.getElement().equals(element)) {
                foundElement = true;
            }
            else {
                currentNode = currentNode.getNextNode();
                currentIndex++;
            }    
        }
        if (!foundElement) {
            currentIndex = -1;
        }
        return currentIndex;
    }

    @Override
    public E first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return head.getElement();
    }

    @Override
    public E last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return tail.getElement();
    }

    @Override
    public boolean contains(E target) {
        return indexOf(target) > -1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0; //best clarity 
        // return head == null; //paranoia way 
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder resString = new StringBuilder();
        resString.append("[");
        for (E element : this) {
            resString.append(element.toString());
            resString.append(", ");
        }
        if (size > 0) {
            resString.delete(resString.length() - 2, resString.length());
        }
        resString.append("]");

        return resString.toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new SLLIterator();
    }

    @Override
    public ListIterator<E> listIterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    @Override
    public ListIterator<E> listIterator(int startingIndex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }
    
    /**
     * Basic Iterator for IUSingleLinkedList
     */
    private class SLLIterator implements Iterator<E> {
        private Node<E> nextNode;
        private int iterModCount;
        private boolean canRemove; 

        /**
         * Initialize Iterator before first element
         */
        public SLLIterator() {
            nextNode = head;
            iterModCount = modCount;
            canRemove = false;
        }
        @Override
        public boolean hasNext() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return nextNode != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E retVal = nextNode.getElement();
            nextNode = nextNode.getNextNode();
            canRemove = true;
            return retVal;
        }

        @Override
        public void remove() {
            // Checks before modification 
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (!canRemove) {
                throw new IllegalStateException();
            }
            
            canRemove = false;
            if (head.getNextNode() == nextNode) { // Removing head (B,^ D)
                head = nextNode;
                if (nextNode == null) { // Removing ONLY node: or size==1, head==null 
                    tail = null;
                }
            }
            else { // General case: removing something deep into the list (A, B,^ C)
                Node<E> prevPrevNode = head;
                // Locate the node 2 behind nextNode
                while (prevPrevNode.getNextNode().getNextNode() != nextNode) {
                    prevPrevNode = prevPrevNode.getNextNode();
                }
                prevPrevNode.setNextNode(nextNode); // Reassign to remove   
                
                if (nextNode == null) { // Check to ensure 
                    tail = prevPrevNode;
                }             
            }
            size--;
            modCount++;
            iterModCount++;
        }
    }
}

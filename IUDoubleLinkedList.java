import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Double-linked-node-based implementation of IndexedUnsortedList
 * @author GMunt
 * @version Spring 2025
 */
public class IUDoubleLinkedList<E> implements IndexedUnsortedList<E> {
    private Node<E> head;
    private Node<E> tail;
    private int size;
    private int modCount;

    public IUDoubleLinkedList() {
        head = tail = null;
        size = 0;
        modCount = 0;
    }

    @Override
    public void addToFront(E element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addToFront'");
    }

    @Override
    public void addToRear(E element) {
        Node<E> newNode = new Node<E>(element);
        newNode.setPreviousNode(tail);
        if (isEmpty()) {
            head = newNode;
        }
        else {
            tail.setNextNode(newNode);
        }
        tail = newNode;
        size++;
        modCount++;
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
        // Traverse
        while (currentNode != null && !currentNode.getElement().equals(target)) {
            currentNode = currentNode.getNextNode();
        }
        // if the target isn't found
        if (currentNode == null) { 
            throw new NoSuchElementException();
        }
        // Update Nodes 
        nodeToAdd.setPreviousNode(currentNode);
        nodeToAdd.setNextNode(currentNode.getNextNode());
        currentNode.setNextNode(nodeToAdd);
        if (nodeToAdd.getNextNode() == null) { // or currentNode == tail
            tail = nodeToAdd;
        }
        else {
            nodeToAdd.getNextNode().setPreviousNode(nodeToAdd);
        }
        size++;
        modCount++;
    }

    @Override
    public void add(int index, E element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public E removeFirst() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeFirst'");
    }

    @Override
    public E removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        E retVal = tail.getElement();
        if (head == tail) { // size == 1
            head = null;
        } 
        else {
            tail.getPreviousNode().setNextNode(null);
        }
        tail = tail.getPreviousNode();
        size--;
        modCount++;
        return retVal;
    }

    @Override
    public E remove(E element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) { // because remove's valid index range != ListIterator's range
            throw new IndexOutOfBoundsException();
        }
        ListIterator<E> lit = listIterator(index);
        E retVal = lit.next();
        lit.remove();
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
    public E get(int index) {
        if (index < 0 || index >=  size) {
            throw new IndexOutOfBoundsException();
        }
        // Node<E> currentNode = head;
        // for (int i = 0; i < index; i++) {
        //     currentNode = currentNode.getNextNode();
        // }
        // return currentNode.getElement();
        ListIterator<E> lit = listIterator(index);
        return lit.next();
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
    public Iterator iterator() {
        return new IUDLLIterator();
    }

    @Override
    public ListIterator listIterator() {
        return new IUDLLIterator();
    }

    @Override
    public ListIterator listIterator(int startingIndex) {
        return new IUDLLIterator(startingIndex);
    }
    
    /**
     * ListIterator (and basic Iterator) for IUDLL.
     */
    private class IUDLLIterator implements ListIterator<E> {
        private Node<E> nextNode;
        private Node<E> lastReturnedNode;
        private int nextIndex;
        private int iterModCount;

        /**
         * Initialize iterator before first element
         */
        public IUDLLIterator() {
            nextNode = head;
            lastReturnedNode = null;
            nextIndex = 0;
            iterModCount = modCount;
        }

        /**
         * Initialize iterator before startingIndex
         * @param startingIndex index of element that would be next
         */
        public IUDLLIterator(int startingIndex) {
            if (startingIndex < 0 || startingIndex > size) {
                throw new IndexOutOfBoundsException();
            }
            nextNode = head;
            // TODO optimize to start at tail if past halfway point
            for (int i = 0; i < startingIndex; i++) {
                nextNode = nextNode.getNextNode();
            }
            nextIndex = startingIndex;
            lastReturnedNode = null;
            iterModCount = modCount;
        }

        @Override
        public boolean hasNext() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return (nextNode != null);
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E retVal = nextNode.getElement();
            lastReturnedNode = nextNode; // Set returned node to next node before it moves
            nextNode = nextNode.getNextNode();
            nextIndex++;
            return retVal;
        }

        @Override
        public boolean hasPrevious() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return (nextNode != head);
        }

        @Override
        public E previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            if (nextNode != null) {
                nextNode = nextNode.getPreviousNode();
            }
            else {
                nextNode = tail;
            }
            lastReturnedNode = nextNode; // Store returned node after moving 
            nextIndex--;
            return nextNode.getElement();
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        @Override
        public void remove() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (lastReturnedNode == null) { // Can't remove
                throw new IllegalStateException();
            }
            // Check if head is removed
            if (lastReturnedNode == head) {
                head = head.getNextNode();
            }
            else {
                lastReturnedNode.getPreviousNode().setNextNode(lastReturnedNode.getNextNode()); // Bypass the last returned node (left side)
            }
            // Check if tail is removed
            if (lastReturnedNode == tail) {
                tail = tail.getPreviousNode();
            }
            else {
                lastReturnedNode.getNextNode().setPreviousNode(lastReturnedNode.getPreviousNode()); // Bypass the last returned node (right side)
            }
            // Need to know if last move was next or previous to update list/iterator correctly
            if (lastReturnedNode == nextNode) { // Last move was previous
                nextNode = nextNode.getNextNode();
            }
            else { // Last move was next
                nextIndex--;
            }
            lastReturnedNode = null;
            size--;
            modCount++;
            iterModCount++;
        }

        @Override
        public void set(E e) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'set'");
        }

        @Override
        public void add(E e) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'add'");
            // Can always call add unless concurrent error. Always adds to the left of iterator 
            // Set lastReturned = null
        }

    }
}

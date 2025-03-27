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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
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
    public Iterator iterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }

    @Override
    public ListIterator listIterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    @Override
    public ListIterator listIterator(int startingIndex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

}

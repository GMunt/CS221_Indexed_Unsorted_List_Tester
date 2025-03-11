import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Single-linked-Node-based implementation of IndexedUnsortedList
 * @author GMunt
 */
public class IUSingleLinkedList<E> implements IndexedUnsortedList<E> {
    private Node<E> head;
    private Node<E> tail;
    private int size;

    public IUSingleLinkedList() {
        head = tail = null;
        size = 0;
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
        size++;
    }

    @Override
    public void add(E element) {
        addToRear(element);
    }

    @Override
    public void addAfter(E element, E target) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAfter'");
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeLast'");
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'set'");
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
        while(currentNode != null && !foundElement) {
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
    public Iterator<E> iterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
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
    
}

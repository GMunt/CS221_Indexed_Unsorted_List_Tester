import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * 
 * @author GMunt
 */
public class IUArrayList<E> implements IndexedUnsortedList<E> {

    E[] array;
    int rear;
    final static int DEFAULT_CAPACITY = 10;

    /**
     * Constructor method for default settings
     */
    public IUArrayList() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Constructor method with user configured size
     * @param size
     */
    public IUArrayList(int size) {
        array = (E[])(new Object[size]);
        rear = 0;
    }

    /**
     * 
     */
    private void expandIfNecessary() {
        if (rear >= array.length) {
            Arrays.copyOf(array, array.length * 2);
        }
    }

    @Override
    public void addToFront(E element) {
        expandIfNecessary();
        // Must SHIFT all current elements back by 1 index position 
        for (int i = rear; i > 0; i--) { // important to move backwards for saving data
            array[i] = array[i - 1];
        }
        array[0] = element;
        rear++;
    }

    @Override
    public void addToRear(E element) {
        expandIfNecessary();
        array[rear] = element;
        rear++;
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
        if (index <= rear && index > -1) {
            expandIfNecessary();
            for (int i = rear; i > index; i--) { 
                array[i] = array[i - 1];
            }
            array[index] = element;
            rear++;
        }
        else {
            throw new IndexOutOfBoundsException();
        }
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
        int index = indexOf(element);
        if (index < 0) {
            throw new NoSuchElementException();
        }
        // don't forget the value you're deleting 
        E retVal = array[index];
        // Shift everything from index on to the left
        for (int i = index; i < rear - 1; i++) {
            array[i] = array[i + 1];
        }
        array[rear - 1] = null;
        rear--;
        return retVal;
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
    public E get(int index) {
        if (index < 0 || index >= rear) {
            throw new IndexOutOfBoundsException();
        }
        return array[index];
    }

    @Override
    public int indexOf(E element) {
        int currentIndex = 0;
        int returnIndex = -1;
        while (returnIndex < 0 && currentIndex < rear) { // rear or size()
            if (array[currentIndex].equals(element)) {
                returnIndex = currentIndex;
            }
            else {
                currentIndex++;
            }
        }
        return returnIndex;
    }

    @Override
    public E first() {
        return array[0];
    }

    @Override
    public E last() {
        return array[rear - 1];
    }

    @Override
    public boolean contains(E target) {
        return indexOf(target) > -1;
    }

    @Override
    public boolean isEmpty() {
        return rear == 0;
    }

    @Override
    public int size() {
        return rear;
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

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * This class will create a custom indexed unsorted array list
 * allowing for all of the required methods
 * @author GMunt
 * @version Spring 2025
 * @param <E> type to store
 */
public class IUArrayList<E> implements IndexedUnsortedList<E> {

    private E[] array;
    private int rear;
    private int modCount;
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
    @SuppressWarnings("unchecked")
    public IUArrayList(int size) {
        array = (E[])(new Object[size]);
        rear = 0;
        modCount = 0;
    }

    /**
     * Helper method to double the size of array whenever called
     */
    private void expandIfNecessary() {
        if (rear >= array.length) {
            array = Arrays.copyOf(array, array.length * 2);
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
        modCount++;
    }

    @Override
    public void addToRear(E element) {
        expandIfNecessary();
        array[rear] = element;
        rear++;
        modCount++;
    }

    @Override
    public void add(E element) {
        addToRear(element);
        modCount++;
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
            modCount++;
        }
        else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void addAfter(E element, E target) {
        if (indexOf(target) < 0) {
			throw new NoSuchElementException();
		} 
        add(indexOf(target) + 1, element);
    }

    @Override
    public E removeFirst() {
        if (rear - 1 < 0) {
            throw new NoSuchElementException();
        }
        return remove(0);
    }

    @Override
    public E removeLast() {
        if (rear - 1 < 0) {
            throw new NoSuchElementException();
        }
        return remove(rear - 1);
    }

    @Override
    public E remove(E element) {
        int index = indexOf(element);
        if (index < 0 || index >= rear) {
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
        modCount++;
        return retVal;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= rear) { 
            throw new IndexOutOfBoundsException(); // Must be IOOBE and not NSEE for index error 
        }
        E retVal = array[index];
        for (int i = index; i < rear - 1; i++) {
            array[i] = array[i + 1];
        }
        array[rear - 1] = null;
        rear--;
        modCount++;
        return retVal;
    }

    @Override
    public void set(int index, E element) {
        // Check if index is out of bounds
        if (index < 0 || index >= rear) {
            throw new IndexOutOfBoundsException();
        }
        array[index] = element;
        modCount++;
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
        if (array[0] == null) {
			throw new NoSuchElementException();
		} 
        return array[0];
    }

    @Override
    public E last() {
        if (rear - 1 < 0) {
			throw new NoSuchElementException();
		} 
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
        return new ALIterator();
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

    // Added to pass toString() tests
    @Override
    public String toString() {
        StringBuilder resString = new StringBuilder();
        resString.append("[");
        for (E element : this) {
            resString.append(element.toString());
            resString.append(", ");
        }
        if (rear > 0) {
            resString.delete(resString.length() - 2, resString.length());
        }
        resString.append("]");

        return resString.toString();
    }

    /**
     * Basic iterator for IUArrayList
     */
    private class ALIterator implements Iterator<E> {
        private int nextIndex;
        private int iterModCount;
        private boolean canRemove;

        /**
         * Create a new Iterator in front of the first element 
         */
        public ALIterator() {
            nextIndex = 0;
            iterModCount = modCount;
            canRemove = false;
        }

        @Override
        public boolean hasNext() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return nextIndex < rear;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            nextIndex++; // Must increment and then return
            canRemove = true;
            return array[nextIndex - 1];
        }

        // Doesn't autofill method because of sad history :(
        @Override 
        public void remove() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (!canRemove) {
                throw new IllegalStateException();
            }
            canRemove = false;
            // Shift everything from index on to the left
            for (int i = nextIndex - 1; i < rear - 1; i++) {
                array[i] = array[i + 1];
            }
            array[rear - 1] = null;
            rear--;
            nextIndex--;
            iterModCount++;
            modCount++;
            // TODO Go over in class and fill this in (might need a boolean for if the next() has been called)
        }
    }
}

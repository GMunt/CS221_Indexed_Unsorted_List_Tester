import java.util.Comparator;

/**
 * Class for sorting lists that implement the IndexedUnsortedList interface,
 * using ordering defined by class of objects in list or a Comparator.
 * As written uses Mergesort algorithm.
 *
 * @author CS221
 */
public class Sort
{	
	/**
	 * Returns a new list that implements the IndexedUnsortedList interface. 
	 * As configured, uses WrappedDLL. Must be changed if using 
	 * your own IUDoubleLinkedList class. 
	 * 
	 * @return a new list that implements the IndexedUnsortedList interface
	 */
	private static <T> IndexedUnsortedList<T> newList() 
	{
		return new IUDoubleLinkedList<T>(); //TODO: replace with your IUDoubleLinkedList for extra-credit
	}
	
	/**
	 * Sorts a list that implements the IndexedUnsortedList interface 
	 * using compareTo() method defined by class of objects in list.
	 * DO NOT MODIFY THIS METHOD
	 * 
	 * @param <T>
	 *            The class of elements in the list, must extend Comparable
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface 
	 * @see IndexedUnsortedList 
	 */
	public static <T extends Comparable<T>> void sort(IndexedUnsortedList<T> list) 
	{
		mergesort(list);
	}

	/**
	 * Sorts a list that implements the IndexedUnsortedList interface 
	 * using given Comparator.
	 * DO NOT MODIFY THIS METHOD
	 * 
	 * @param <T>
	 *            The class of elements in the list
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface 
	 * @param c
	 *            The Comparator used
	 * @see IndexedUnsortedList 
	 */
	public static <T> void sort(IndexedUnsortedList <T> list, Comparator<T> c) 
	{
		mergesort(list, c);
	}
	
	/**
	 * Mergesort algorithm to sort objects in a list 
	 * that implements the IndexedUnsortedList interface, 
	 * using compareTo() method defined by class of objects in list.
	 * DO NOT MODIFY THIS METHOD SIGNATURE
	 * 
	 * @param <T>
	 *            The class of elements in the list, must extend Comparable
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface 
	 */
	private static <T extends Comparable<T>> void mergesort(IndexedUnsortedList<T> list)
	{
		if (list.size() <= 1) {
			return; // Base case
		}

		// Split the list into two halves
		IndexedUnsortedList<T> leftHalf = newList(); // Create a new list for the left half
		IndexedUnsortedList<T> rightHalf = newList(); // Create a new list for the right half

		// Divide the original list into two halves
		int originalSize = list.size();
		int mid = list.size() / 2; 
		
		// Fill left half of list
		for (int i = 0; i < mid; i++) {
			leftHalf.add(list.removeFirst());
		}
		// Fill right half of list
		for (int i = mid; i < originalSize; i++) {
			rightHalf.add(list.removeFirst());
		}

		// Recursively apply the Merge Sort algorithm to each half
		mergesort(leftHalf);
		mergesort(rightHalf);

		// Merge the sorted half lists back into the original list by repeatedly taking the smaller of the two first elements of the halves and adding it back into the original list until both halves are emptied
		while (!leftHalf.isEmpty() && !rightHalf.isEmpty()) {
			if (leftHalf.first().compareTo(rightHalf.first()) < 0) {
				list.add(leftHalf.removeFirst());
			} else {
				list.add(rightHalf.removeFirst());
			}
		}

		// Add any remaining elements from leftHalf
		while (!leftHalf.isEmpty()) {
			list.add(leftHalf.removeFirst());
		}

		// Add any remaining elements from rightHalf
		while (!rightHalf.isEmpty()) {
			list.add(rightHalf.removeFirst());
		}
	}
	/**
	 * Mergesort algorithm to sort objects in a list 
	 * that implements the IndexedUnsortedList interface,
	 * using the given Comparator.
	 * DO NOT MODIFY THIS METHOD SIGNATURE
	 * 
	 * @param <T>
	 *            The class of elements in the list
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface 
	 * @param c
	 *            The Comparator used
	 */
	private static <T> void mergesort(IndexedUnsortedList<T> list, Comparator<T> c)
	{
		if (list.size() <= 1) {
			return; // Base case
		}

		// Split the list into two halves
		IndexedUnsortedList<T> leftHalf = newList(); // Create a new list for the left half
		IndexedUnsortedList<T> rightHalf = newList(); // Create a new list for the right half

		// Divide the original list into two halves
		int originalSize = list.size();
		int mid = list.size() / 2; 
		
		// Fill left half of list
		for (int i = 0; i < mid; i++) {
			leftHalf.add(list.removeFirst());
		}
		// Fill right half of list
		for (int i = mid; i < originalSize; i++) {
			rightHalf.add(list.removeFirst());
		}

		// Recursively apply the Merge Sort algorithm to each half
		mergesort(leftHalf, c);
		mergesort(rightHalf, c);

		// Merge the sorted half lists back into the original list by repeatedly taking the smaller of the two first elements of the halves and adding it back into the original list until both halves are emptied
		while (!leftHalf.isEmpty() && !rightHalf.isEmpty()) {
			if (c.compare(leftHalf.first(), rightHalf.first()) < 0) {
				list.add(leftHalf.removeFirst());
			} else {
				list.add(rightHalf.removeFirst());
			}
		}

		// Add any remaining elements from leftHalf
		while (!leftHalf.isEmpty()) {
			list.add(leftHalf.removeFirst());
		}

		// Add any remaining elements from rightHalf
		while (!rightHalf.isEmpty()) {
			list.add(rightHalf.removeFirst());
		}

	}
	
}

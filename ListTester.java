import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * A unit test class for lists that implement IndexedUnsortedList. 
 * This is a set of black box tests that should work for any implementation
 * of this interface.
 * 
 * NOTE: One example test is given for each interface method using a new list to
 * get you started.
 * 
 * @author mvail, mhthomas, awinters
 */
@SuppressWarnings("deprecated")
public class ListTester {
	//possible lists that could be tested
	private static enum ListToUse {
		goodList, badList, arrayList, singleLinkedList, doubleLinkedList
	};
	// TODO: THIS IS WHERE YOU CHOOSE WHICH LIST TO TEST
	private final static ListToUse LIST_TO_USE = ListToUse.doubleLinkedList;

	// possible results expected in tests
	private enum Result {
		IndexOutOfBounds, IllegalState, NoSuchElement, 
		ConcurrentModification, UnsupportedOperation, 
		NoException, UnexpectedException,
		True, False, Pass, Fail, 
		MatchingValue,
		ValidString
	};

	// named elements for use in tests
	private static final Integer ELEMENT_A = 1;
	private static final Integer ELEMENT_B = 2;
	private static final Integer ELEMENT_C = 3;
	private static final Integer ELEMENT_D = 4;
	private static final Integer ELEMENT_X = -1;//element that should appear in no lists
	private static final Integer ELEMENT_Z = -2;//element that should appear in no lists

	// determine whether to include ListIterator functionality tests
	private final boolean SUPPORTS_LIST_ITERATOR; //initialized in constructor
	
	//tracking number of tests and test results
	private int passes = 0;
	private int failures = 0;
	private int totalRun = 0;

	private int secTotal = 0;
	private int secPasses = 0;
	private int secFails = 0;

	//control output - modified by command-line args
	private boolean printFailuresOnly = true;
	private boolean showToString = true;
	private boolean printSectionSummaries = true;

	/**
	 * Valid command line args include:
	 *  -a : print results from all tests (default is to print failed tests, only)
	 *  -s : hide Strings from toString() tests
	 *  -m : hide section summaries in output
	 * @param args not used
	 */
	public static void main(String[] args) {
		// to avoid every method being static
		ListTester tester = new ListTester(args);
		tester.runTests();
	}

	/** tester constructor
	 * @param args command line args
	 */
	public ListTester(String[] args) {
		for (String arg : args) {
			if (arg.equalsIgnoreCase("-a"))
				printFailuresOnly = false;
			if (arg.equalsIgnoreCase("-s"))
				showToString = false;
			if (arg.equalsIgnoreCase("-m"))
				printSectionSummaries = false;
		}
		switch (LIST_TO_USE) {
		case doubleLinkedList:
			SUPPORTS_LIST_ITERATOR = true;
			break;
		default:
			SUPPORTS_LIST_ITERATOR = false;
			break;
		}
	}

	/** Print test results in a consistent format
	 * @param testDesc description of the test
	 * @param result indicates if the test passed or failed
	 */
	private void printTest(String testDesc, boolean result) {
		totalRun++;
		if (result) { passes++; }
		else { failures++; }
		if (!result || !printFailuresOnly) {
			System.out.printf("%-46s\t%s\n", testDesc, (result ? "   PASS" : "***FAIL***"));
		}
	}

	/** Print a final summary */
	private void printFinalSummary() {
		String verdict = String.format("\nTotal Tests Run: %d,  Passed: %d (%.1f%%),  Failed: %d\n",
				totalRun, passes, passes*100.0/totalRun, failures);
		String line = "";
		for (int i = 0; i < verdict.length(); i++) {
			line += "-";
		}
		System.out.println(line);
		System.out.println(verdict);
	}

	/** Print a section summary */
	private void printSectionSummary() {
		secTotal = totalRun - secTotal;
		secPasses = passes - secPasses;
		secFails = failures - secFails;
		System.out.printf("\nSection Tests: %d,  Passed: %d,  Failed: %d\n", secTotal, secPasses, secFails);
		secTotal = totalRun; //reset for next section
		secPasses = passes;
		secFails = failures;		
		System.out.printf("Tests Run So Far: %d,  Passed: %d (%.1f%%),  Failed: %d\n",
				totalRun, passes, passes*100.0/totalRun, failures);
	}
	
	/////////////////////
	// XXX runTests()
	/////////////////////

	/** Run tests to confirm required functionality from list constructors and methods */
	private void runTests() {
		//Possible list contents after a scenario has been set up
		Integer[] LIST_A = {ELEMENT_A};
		Integer[] LIST_B = {ELEMENT_B};
		String STRING_A = "A";
		String STRING_B = "B";
		Integer[] LIST_AB = {ELEMENT_A, ELEMENT_B};
		Integer[] LIST_AC = {ELEMENT_A, ELEMENT_C};
		Integer[] LIST_BC = {ELEMENT_B, ELEMENT_C};
		Integer[] LIST_BA = {ELEMENT_B, ELEMENT_A};
		Integer[] LIST_CB = {ELEMENT_C, ELEMENT_B};
		Integer[] LIST_ABC = {ELEMENT_A, ELEMENT_B, ELEMENT_C};
		Integer[] LIST_CAB = {ELEMENT_C, ELEMENT_A, ELEMENT_B};
		Integer[] LIST_ACB = {ELEMENT_A, ELEMENT_C, ELEMENT_B};
		Integer[] LIST_ABD = {ELEMENT_A, ELEMENT_B, ELEMENT_D};
		Integer[] LIST_ADC = {ELEMENT_A, ELEMENT_D, ELEMENT_C};
		String STRING_AB = "AB";
		String STRING_AC = "AC";
		String STRING_BC = "BC";
		String STRING_BA = "BA";
		String STRING_CB = "CB";
		String STRING_ABC = "ABC";
		String STRING_CAB = "CAB";
		String STRING_ACB = "ACB";
		String STRING_ABD = "ABD";
		String STRING_ADC = "ADC";

		//newly constructed empty list
		testEmptyList(newList, "newList");
		//empty to 1-element list
		testSingleElementList(emptyList_addToFrontA_A, "emptyList_addToFrontA_A", LIST_A, STRING_A);
		testSingleElementList(emptyList_addToRearA_A, "emptyList_addToRearA_A", LIST_A, STRING_A); 
		testSingleElementList(emptyList_addA_A, "emptyList_addA_A", LIST_A, STRING_A); 
		testSingleElementList(emptyList_addAtIndexZeroA_A, "emptyList_addAtIndexZeroA_A", LIST_A, STRING_A);
		testSingleElementList(emptyList_listIterAddA_A, "emptyList_listIterAddA_A", LIST_A, STRING_A);
		//1-element to empty list
		testEmptyList(A_removeFirst_emptyList, "A_removeFirst_emptyList");
		testEmptyList(A_removeLast_emptyList, "A_removeLast_emptyList");
		testEmptyList(A_removeA_emptyList, "A_removeA_emptyList");
		testEmptyList(A_removeIndexZero_emptyList, "A_removeIndexZero_emptyList");
		testEmptyList(A_iterRemoveAfterNextA_emptyList, "A_iterRemoveAfterNextA_emptyList");
		testEmptyList(A_listIterRemoveAfterPreviousA_emptyList, "A_listIterRemoveAfterPreviousA_emptyList");
		//1-element to 2-element
		testTwoElementList(A_addToFrontB_BA, "A_addToFrontB_BA", LIST_BA, STRING_BA);
		testTwoElementList(A_addToRearB_AB, "A_addToRearB_AB", LIST_AB, STRING_AB);
		testTwoElementList(A_addBAfterA_AB, "A_addBAfterA_AB", LIST_AB, STRING_AB);
		testTwoElementList(A_addB_AB, "A_addB_AB", LIST_AB, STRING_AB);
		testTwoElementList(A_addAtIndexZeroB_BA, "A_addAtIndexZeroB_BA", LIST_BA, STRING_BA);
		testTwoElementList(A_addAtIndexOneB_AB, "A_addAtIndexOneB_AB", LIST_AB, STRING_AB);
		testTwoElementList(A_newListIterAddB_BA, "A_newListIterAddB_BA", LIST_BA, STRING_BA);
		testTwoElementList(A_listIterAddAfterPreviousB_BA, "A_listIterAddAfterPreviousB_BA", LIST_BA, STRING_BA);
		//1-element to changed 1-element via set()
		testSingleElementList(A_setatIndexZeroB_B, "A_setatIndexZeroB_B", LIST_B, STRING_B);
		testSingleElementList(A_listIterSetAfterNextB_B, "A_listIterSetAfterNextB_B", LIST_B, STRING_B);
		//2-element to 1-element
		testSingleElementList(AB_removeFirst_B, "AB_removeFirst_B", LIST_B, STRING_B);
		testSingleElementList(AB_removeLast_A, "AB_removeLast_A", LIST_A, STRING_A);
		testSingleElementList(AB_removeAtIndexZero_B, "AB_removeAtIndexZero_B", LIST_B, STRING_B);
		testSingleElementList(AB_removeAtIndexOne_A, "AB_removeAtIndexOne_A", LIST_A, STRING_A);
		testSingleElementList(AB_removeA_B, "AB_removeA_B", LIST_B, STRING_B);
		testSingleElementList(AB_removeB_A, "AB_removeB_A", LIST_A, STRING_A);
		testSingleElementList(AB_iterRemoveAfterNextA_B, "AB_iterRemoveAfterNextA_B", LIST_B, STRING_B);
		testSingleElementList(AB_IterRemoveAfterNextNextB_A, "AB_IterRemoveAfterNextNextB_A", LIST_A, STRING_A);
		testSingleElementList(AB_listIterRemoveAfterPrevious_B, "AB_listIterRemoveAfterPrevious_B", LIST_B, STRING_B);
		//2-element to 3-element
		testThreeElementList(AB_addToFrontC_CAB, "AB_addToFrontC_CAB", LIST_CAB, STRING_CAB);
		testThreeElementList(AB_addToRearC_ABC, "AB_addToRearC_ABC", LIST_ABC, STRING_ABC);
		testThreeElementList(AB_addCAfterA_ACB, "AB_addCAfterA_ACB", LIST_ACB, STRING_ACB);
		testThreeElementList(AB_addCAfterB_ABC, "AB_addCAfterB_ABC", LIST_ABC, STRING_ABC);
		testThreeElementList(AB_addC_ABC, "AB_addC_ABC", LIST_ABC, STRING_ABC);
		testThreeElementList(AB_addAtIndexZeroC_CAB, "AB_addAtIndexZeroC_CAB", LIST_CAB, STRING_CAB);
		testThreeElementList(AB_addAtIndexOneC_ACB, "AB_addAtIndexOneC_ACB", LIST_ACB, STRING_ACB);
		testThreeElementList(AB_addAtIndexTwoC_ABC, "AB_addAtIndexTwoC_ABC", LIST_ABC, STRING_ABC);
		testThreeElementList(AB_listIterAddAfterNextC_ABC, "AB_listIterAddAfterNextC_ABC", LIST_ABC, STRING_ABC);
		//2-element to changed 2-element via set()
		testTwoElementList(AB_listIterSetAfterPreviousC_CB, "AB_listIterSetAfterPreviousC_CB", LIST_CB, STRING_CB);
		//3-element to 2-element
		testTwoElementList(ABC_removeLast_AB, "ABC_removeLast_AB", LIST_AB, STRING_AB);
		testTwoElementList(ABC_removeA_BC, "ABC_removeA_BC", LIST_BC, STRING_BC);
		testTwoElementList(ABC_removeB_AC, "ABC_removeB_AC", LIST_AC, STRING_AC);
		testTwoElementList(ABC_removeC_AB, "ABC_removeC_AB", LIST_AB, STRING_AB);
		testTwoElementList(ABC_removeAtIndexZero_BC, "ABC_removeAtIndexZero_BC", LIST_BC, STRING_BC);
		testTwoElementList(ABC_removeAtIndexOne_AC, "ABC_removeAtIndexOne_AC", LIST_AC, STRING_AC);
		testTwoElementList(ABC_removeAtIndexTwo_AB, "ABC_removeAtIndexTwo_AB", LIST_AB, STRING_AB);
		testTwoElementList(ABC_iterRemoveAfterNextA_BC, "ABC_iterRemoveAfterNextA_BC", LIST_BC, STRING_BC);
		testTwoElementList(ABC_iterRemoveAfterNextNextB_AC, "ABC_iterRemoveAfterNextNextB_AC", LIST_AC, STRING_AC);
		testTwoElementList(ABC_iterRemoveAfterNextNextNextC_AB, "ABC_iterRemoveAfterNextNextNextC_AB", LIST_AB, STRING_AB);
		testTwoElementList(ABC_listIterRemoveAfterPreviousB_AC, "ABC_iterRemoveAfterNextNextNextC_AB", LIST_AC, STRING_AC);
		//3-element to changed 3-element via set()
		testThreeElementList(ABC_listIterSetAfterNextD_ABD, "ABC_listIterSetAfterNextD_ABD", LIST_ABD, STRING_ABD);
		testThreeElementList(ABC_listIterSetAfterPreviousD_ADC, "ABC_listIterSetAfterPreviousD_ADC", LIST_ADC, STRING_ADC);
		testThreeElementList(ABC_listIterSetAfterPreviousD_ABD, "ABC_listIterSetAfterPreviousD_ABD", LIST_ABD, STRING_ABD);
		//Iterator concurrency tests
		if (SUPPORTS_LIST_ITERATOR) {
			test_ListIterConcurrency();
		}

		// report final verdict
		printFinalSummary();
	}

	//////////////////////////////////////
	// XXX SCENARIO BUILDERS
	//////////////////////////////////////

	/**
	 * Returns a IndexedUnsortedList for the "new empty list" scenario.
	 * Scenario: no list -> constructor -> [ ]
	 * 
	 * NOTE: Comment out cases for any implementations not currently available
	 *
	 * @return a new, empty IndexedUnsortedList
	 */
	private IndexedUnsortedList<Integer> newList() {
		IndexedUnsortedList<Integer> listToUse;
		switch (LIST_TO_USE) {
		case goodList:
			listToUse = new GoodList<Integer>();
			break;
		case badList:
			listToUse = new BadList<Integer>();
			break;
		case arrayList:
			listToUse = new IUArrayList<Integer>();
			break;
		case singleLinkedList:
			listToUse = new IUSingleLinkedList<Integer>();
			break;
		case doubleLinkedList:
			listToUse = new IUDoubleLinkedList<Integer>();
			break;
		default:
			listToUse = null;
		}
		return listToUse;
	}
	// The following creates a "lambda" reference that allows us to pass a scenario
	//  builder method as an argument. You don't need to worry about how it works -
	//  just make sure each scenario building method has a corresponding Scenario 
	//  assignment statement as in these examples. 
	private Scenario<Integer> newList = () -> newList();
	
	/** Scenario: [ ] -> listIterAddA -> [A]
	 * @return [A] after listIterAddA
	 */
	private IndexedUnsortedList<Integer> emptyList_listIterAddA_A() {
		IndexedUnsortedList<Integer> list = newList(); 
		ListIterator<Integer> it = list.listIterator();
		it.add(ELEMENT_A);
		return list;
	}
	private Scenario<Integer> emptyList_listIterAddA_A = () -> emptyList_listIterAddA_A();

	/** Scenario: [A] -> listIterRemoveAfterPrevious -> [ ]
	 * @return [ ] after listIterRemoveAfterPrevious
	 */
	private IndexedUnsortedList<Integer> A_listIterRemoveAfterPreviousA_emptyList() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A(); 
		ListIterator<Integer> it = list.listIterator(1);
		it.previous();
		it.remove();
		return list;
	}
	private Scenario<Integer> A_listIterRemoveAfterPreviousA_emptyList = () -> A_listIterRemoveAfterPreviousA_emptyList();

	/** Scenario: [A] -> removeFirst() -> [] 
	 * @return [] after removeFirst()
	 */
	private IndexedUnsortedList<Integer> A_removeFirst_emptyList() {
		IndexedUnsortedList<Integer> list = emptyList_addA_A(); 
		list.removeFirst();
		return list;
	}
	private Scenario<Integer> A_removeFirst_emptyList = () -> A_removeFirst_emptyList();

	/** Scenario: [A] -> removeLast() -> [] 
	 * @return [] after removeLast()
	 */
	private IndexedUnsortedList<Integer> A_removeLast_emptyList() {
		IndexedUnsortedList<Integer> list = emptyList_addAtIndexZeroA_A(); 
		list.removeLast();
		return list;
	}
	private Scenario<Integer> A_removeLast_emptyList = () -> A_removeLast_emptyList();

	/** Scenario: [A] -> remove(A) -> [] 
	 * @return [] after remove(A)
	 */
	private IndexedUnsortedList<Integer> A_removeA_emptyList() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A(); 
		list.remove(ELEMENT_A);
		return list;
	}
	private Scenario<Integer> A_removeA_emptyList = () -> A_removeA_emptyList();

	/** Scenario: [A] -> remove(A) -> [] 
	 * @return [] after remove(A)
	 */
	private IndexedUnsortedList<Integer> A_removeIndexZero_emptyList() {
		IndexedUnsortedList<Integer> list = emptyList_addToRearA_A(); 
		list.remove(0);
		return list;
	}
	private Scenario<Integer> A_removeIndexZero_emptyList = () -> A_removeIndexZero_emptyList();

	/** Scenario: empty list -> addToFront(A) -> [A] 
	 * @return [A] after addToFront(A)
	 */
	private IndexedUnsortedList<Integer> emptyList_addToFrontA_A() {
		IndexedUnsortedList<Integer> list = newList(); 
		list.addToFront(ELEMENT_A);
		return list;
	}
	private Scenario<Integer> emptyList_addToFrontA_A = () -> emptyList_addToFrontA_A();
	
	/**
	 * Scenario: empty list -> addToRear(A) -> [A]
	 * @return [A] after addToRear(A)
	 */
	private IndexedUnsortedList<Integer> emptyList_addToRearA_A() {
		IndexedUnsortedList<Integer> list = newList(); 
		list.addToRear(ELEMENT_A);
		return list;
	}
	private Scenario<Integer> emptyList_addToRearA_A = () -> emptyList_addToRearA_A();
	
	/**
	 * Scenario: empty list -> add(A) -> [A]
	 * @return [A] after add(A)
	 */
	private IndexedUnsortedList<Integer> emptyList_addA_A() {
		IndexedUnsortedList<Integer> list = newList(); 
		list.add(ELEMENT_A);
		return list;
	}
	private Scenario<Integer> emptyList_addA_A = () -> emptyList_addA_A();
	
	/**
	 * Scenario: empty list -> add(0,A) -> [A]
	 * @return [A] after add(0,A)
	 */
	private IndexedUnsortedList<Integer> emptyList_addAtIndexZeroA_A() {
		IndexedUnsortedList<Integer> list = newList(); 
		list.add(0,ELEMENT_A);
		return list;
	}
	private Scenario<Integer> emptyList_addAtIndexZeroA_A = () -> emptyList_addAtIndexZeroA_A();

	/**
	 * Scenario: [A] -> set(0,B) -> [B]
	 * @return [B] after set(0,B)
	 */
	private IndexedUnsortedList<Integer> A_setatIndexZeroB_B() {
		IndexedUnsortedList<Integer> list = emptyList_addA_A(); 
		list.set(0,ELEMENT_B);
		return list;
	}
	private Scenario<Integer> A_setatIndexZeroB_B = () -> A_setatIndexZeroB_B();

	/**
	 * Scenario: [A] -> listIterSetAfterNextB -> [B]
	 * @return [B] after listIterSetAfterNextB
	 */
	private IndexedUnsortedList<Integer> A_listIterSetAfterNextB_B() {
		IndexedUnsortedList<Integer> list = emptyList_listIterAddA_A(); 
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.set(ELEMENT_B);
		return list;
	}
	private Scenario<Integer> A_listIterSetAfterNextB_B = () -> A_listIterSetAfterNextB_B();

	/** Scenario: [A] -> addToFront(B) -> [B,A] 
	 * @return [B,A] after addToFront(B)
	 */
	private IndexedUnsortedList<Integer> A_addToFrontB_BA() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A(); 
		list.addToFront(ELEMENT_B);
		return list;
	}
	private Scenario<Integer> A_addToFrontB_BA = () -> A_addToFrontB_BA();

	/** Scenario: [A] -> addToRear(B) -> [A,B] 
	 * @return [A,B] after addToRear(B)
	 */
	private IndexedUnsortedList<Integer> A_addToRearB_AB() {
		IndexedUnsortedList<Integer> list = emptyList_addToRearA_A(); 
		list.addToRear(ELEMENT_B);
		return list;
	}
	private Scenario<Integer> A_addToRearB_AB = () -> A_addToRearB_AB();

	/** Scenario: [A] -> addAfter(B,A) -> [A,B] 
	 * @return [A,B] after addAfter(B,A)
	 */
	private IndexedUnsortedList<Integer> A_addBAfterA_AB() {
		IndexedUnsortedList<Integer> list = emptyList_addA_A(); 
		list.addAfter(ELEMENT_B,ELEMENT_A);
		return list;
	}
	private Scenario<Integer> A_addBAfterA_AB = () -> A_addBAfterA_AB();

	/** Scenario: [A] -> add(B) -> [A,B] 
	 * @return [A,B] after add(B)
	 */
	private IndexedUnsortedList<Integer> A_addB_AB() {
		IndexedUnsortedList<Integer> list = emptyList_addAtIndexZeroA_A(); 
		list.add(ELEMENT_B);
		return list;
	}
	private Scenario<Integer> A_addB_AB = () -> A_addB_AB();

	/** Scenario: [A] -> add(0,B) -> [B,A] 
	 * @return [A,B] after add(0,B)
	 */
	private IndexedUnsortedList<Integer> A_addAtIndexZeroB_BA() {
		IndexedUnsortedList<Integer> list = emptyList_addAtIndexZeroA_A(); 
		list.add(0,ELEMENT_B);
		return list;
	}
	private Scenario<Integer> A_addAtIndexZeroB_BA = () -> A_addAtIndexZeroB_BA();

	/** Scenario: [A] -> add(1,B) -> [A,B] 
	 * @return [A,B] after add(1,B)
	 */
	private IndexedUnsortedList<Integer> A_addAtIndexOneB_AB() {
		IndexedUnsortedList<Integer> list = emptyList_addA_A(); 
		list.add(1,ELEMENT_B);
		return list;
	}
	private Scenario<Integer> A_addAtIndexOneB_AB = () -> A_addAtIndexOneB_AB();

	/** Scenario: [A] -> newListIterAddB -> [B,A]
	 * @return [B,A] after newListIterAddB
	 */
	private IndexedUnsortedList<Integer> A_newListIterAddB_BA() {
		IndexedUnsortedList<Integer> list = newList(); 
		ListIterator<Integer> it1 = list.listIterator();
		it1.add(ELEMENT_A);
		ListIterator<Integer> it2 = list.listIterator();
		it2.add(ELEMENT_B);
		return list;
	}
	private Scenario<Integer> A_newListIterAddB_BA = () -> A_newListIterAddB_BA();

	/** Scenario: [A] -> listIterAddAfterPreviousB -> [B,A]
	 * @return [B,A] after listIterAddAfterPreviousB
	 */
	private IndexedUnsortedList<Integer> A_listIterAddAfterPreviousB_BA() {
		IndexedUnsortedList<Integer> list = AB_removeB_A(); 
		ListIterator<Integer> it = list.listIterator(1);
		it.previous();
		it.add(ELEMENT_B);
		return list;
	}
	private Scenario<Integer> A_listIterAddAfterPreviousB_BA = () -> A_listIterAddAfterPreviousB_BA();

	
	/** Scenario: [A,B] -> addToFront(C) -> [C,A,B] 
	 * @return [C,A,B] after addToFront(C)
	 */
	private IndexedUnsortedList<Integer> AB_addToFrontC_CAB() {
		IndexedUnsortedList<Integer> list = A_addAtIndexOneB_AB(); 
		list.addToFront(ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_addToFrontC_CAB = () -> AB_addToFrontC_CAB();

	/** Scenario: [A,B] -> addToRear(C) -> [A,B,C] 
	 * @return [A,B,C] after addToRear(C)
	 */
	private IndexedUnsortedList<Integer> AB_addToRearC_ABC() {
		IndexedUnsortedList<Integer> list = A_addBAfterA_AB(); 
		list.addToRear(ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_addToRearC_ABC = () -> AB_addToRearC_ABC();

	/** Scenario: [A,B] -> addAfter(C,A) -> [A,C,B] 
	 * @return [A,C,B] after addAfter(C,A)
	 */
	private IndexedUnsortedList<Integer> AB_addCAfterA_ACB() {
		IndexedUnsortedList<Integer> list = A_addB_AB(); 
		list.addAfter(ELEMENT_C, ELEMENT_A);
		return list;
	}
	private Scenario<Integer> AB_addCAfterA_ACB = () -> AB_addCAfterA_ACB();

	/** Scenario: [A,B] -> addAfter(C,B) -> [A,B,C] 
	 * @return [A,B,C] after addAfter(C,B)
	 */
	private IndexedUnsortedList<Integer> AB_addCAfterB_ABC() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB(); 
		list.addAfter(ELEMENT_C, ELEMENT_B);
		return list;
	}
	private Scenario<Integer> AB_addCAfterB_ABC = () -> AB_addCAfterB_ABC();

	/** Scenario: [A,B] -> add(C) -> [A,B,C] 
	 * @return [A,B,C] after add(C)
	 */
	private IndexedUnsortedList<Integer> AB_addC_ABC() {
		IndexedUnsortedList<Integer> list = A_addAtIndexOneB_AB(); 
		list.add(ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_addC_ABC = () -> AB_addC_ABC();

	/** Scenario: [A,B] -> add(0,C) -> [C,A,B] 
	 * @return [C,A,B] after add(0,C)
	 */
	private IndexedUnsortedList<Integer> AB_addAtIndexZeroC_CAB() {
		IndexedUnsortedList<Integer> list = A_addBAfterA_AB(); 
		list.add(0,ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_addAtIndexZeroC_CAB = () -> AB_addAtIndexZeroC_CAB();

	/** Scenario: [A,B] -> add(1,C) -> [A,C,B] 
	 * @return [A,C,B] after add(1,C)
	 */
	private IndexedUnsortedList<Integer> AB_addAtIndexOneC_ACB() {
		IndexedUnsortedList<Integer> list = A_addB_AB(); 
		list.add(1,ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_addAtIndexOneC_ACB = () -> AB_addAtIndexOneC_ACB();

	/** Scenario: [A,B] -> add(2,C) -> [A,B,C] 
	 * @return [A,B,C] after add(2,C)
	 */
	private IndexedUnsortedList<Integer> AB_addAtIndexTwoC_ABC() {
		IndexedUnsortedList<Integer> list = A_addB_AB(); 
		list.add(2,ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_addAtIndexTwoC_ABC = () -> AB_addAtIndexTwoC_ABC();

	/** Scenario: [A] -> iteratorRemoveAfterNextA -> [ ] 
	 * @return [ ] after iteratorRemoveAfterNextA
	 */
	private IndexedUnsortedList<Integer> A_iterRemoveAfterNextA_emptyList() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A(); 
		Iterator<Integer> it = list.iterator();
		it.next();
		it.remove();
		return list;
	}
	private Scenario<Integer> A_iterRemoveAfterNextA_emptyList = () -> A_iterRemoveAfterNextA_emptyList();

	/** Scenario: [A,B] -> iteratorRemoveAfterNextA -> [B] 
	 * @return [B] after iteratorRemoveAfterNextA
	 */
	private IndexedUnsortedList<Integer> AB_iterRemoveAfterNextA_B() {
		IndexedUnsortedList<Integer> list = A_addAtIndexOneB_AB(); 
		Iterator<Integer> it = list.iterator();
		it.next();
		it.remove();
		return list;
	}
	private Scenario<Integer> AB_iterRemoveAfterNextA_B = () -> AB_iterRemoveAfterNextA_B();
	
	/** Scenario: [A,B] -> iteratorRemoveAfterNextNextB -> [A] 
	 * @return [A] after iteratorRemoveAfterNextB
	 */
	private IndexedUnsortedList<Integer> AB_IterRemoveAfterNextNextB_A() {
		IndexedUnsortedList<Integer> list = A_addBAfterA_AB(); 
		Iterator<Integer> it = list.iterator();
		it.next();
		it.next();
		it.remove();
		return list;
	}
	private Scenario<Integer> AB_IterRemoveAfterNextNextB_A = () -> AB_IterRemoveAfterNextNextB_A();

	/** Scenario: [A,B] -> listIterRemoveAfterPrevious -> [B]
	 * @return [B] after listIterRemoveAfterPrevious
	 */
	private IndexedUnsortedList<Integer> AB_listIterRemoveAfterPrevious_B() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB(); 
		ListIterator<Integer> it = list.listIterator(1);
		it.previous();
		it.remove();
		return list;
	}
	private Scenario<Integer> AB_listIterRemoveAfterPrevious_B = () -> AB_listIterRemoveAfterPrevious_B();

	/** Scenario: [A,B] -> listIterAddAfterNextC -> [A,B,C]
	 * @return [A,B,C] after listIterRemoveAfterPrevious
	 */
	private IndexedUnsortedList<Integer> AB_listIterAddAfterNextC_ABC() {
		IndexedUnsortedList<Integer> list = ABC_iterRemoveAfterNextNextNextC_AB(); 
		ListIterator<Integer> it = list.listIterator(1);
		it.next();
		it.add(ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_listIterAddAfterNextC_ABC = () -> AB_listIterAddAfterNextC_ABC();

	/** Scenario: [A,B] -> listIterSetAfterPreviousC -> [C,B]
	 * @return [C,B] after listIterSetAfterPreviousC
	 */
	private IndexedUnsortedList<Integer> AB_listIterSetAfterPreviousC_CB() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB(); 
		ListIterator<Integer> it = list.listIterator(1);
		it.previous();
		it.set(ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_listIterSetAfterPreviousC_CB = () -> AB_listIterSetAfterPreviousC_CB();

	/** Scenario: [A,B] -> removeFirst() -> [B] 
	 * @return [B] after removeFirst()
	 */
	private IndexedUnsortedList<Integer> AB_removeFirst_B() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB(); 
		list.removeFirst();
		return list;
	}
	private Scenario<Integer> AB_removeFirst_B = () -> AB_removeFirst_B();

	/** Scenario: [A,B] -> removeLast() -> [A] 
	 * @return [A] after removeLast()
	 */
	private IndexedUnsortedList<Integer> AB_removeLast_A() {
		IndexedUnsortedList<Integer> list = ABC_removeLast_AB(); 
		list.removeLast();
		return list;
	}
	private Scenario<Integer> AB_removeLast_A = () -> AB_removeLast_A();

	/** Scenario: [A,B] -> remove(0) -> [B] 
	 * @return [B] after remove(0)
	 */
	private IndexedUnsortedList<Integer> AB_removeAtIndexZero_B() {
		IndexedUnsortedList<Integer> list = A_addB_AB(); 
		list.remove(0);
		return list;
	}
	private Scenario<Integer> AB_removeAtIndexZero_B = () -> AB_removeAtIndexZero_B();

	/** Scenario: [A,B] -> remove(1) -> [A] 
	 * @return [A] after remove(1)
	 */
	private IndexedUnsortedList<Integer> AB_removeAtIndexOne_A() {
		IndexedUnsortedList<Integer> list = A_addB_AB(); 
		list.remove(1);
		return list;
	}
	private Scenario<Integer> AB_removeAtIndexOne_A = () -> AB_removeAtIndexOne_A();

	/** Scenario: [A,B] -> remove(A) -> [B] 
	 * @return [B] after remove(A)
	 */
	private IndexedUnsortedList<Integer> AB_removeA_B() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB(); 
		list.remove(ELEMENT_A);
		return list;
	}
	private Scenario<Integer> AB_removeA_B = () -> AB_removeA_B();

	/** Scenario: [A,B] -> remove(B) -> [A] 
	 * @return [A] after remove(B)
	 */
	private IndexedUnsortedList<Integer> AB_removeB_A() {
		IndexedUnsortedList<Integer> list = A_addBAfterA_AB(); 
		list.remove(ELEMENT_B);
		return list;
	}
	private Scenario<Integer> AB_removeB_A = () -> AB_removeB_A();

	/** Scenario: [A,B,C] -> removeLast() -> [A,B] 
	 * @return [A,B] after removeLast()
	 */
	private IndexedUnsortedList<Integer> ABC_removeLast_AB() {
		IndexedUnsortedList<Integer> list = AB_addAtIndexTwoC_ABC(); 
		list.removeLast();
		return list;
	}
	private Scenario<Integer> ABC_removeLast_AB = () -> ABC_removeLast_AB();

	/** Scenario: [A,B,C] -> remove(A) -> [B,C] 
	 * @return [B,C] after remove(A)
	 */
	private IndexedUnsortedList<Integer> ABC_removeA_BC() {
		IndexedUnsortedList<Integer> list = AB_addC_ABC(); 
		list.remove(ELEMENT_A);
		return list;
	}
	private Scenario<Integer> ABC_removeA_BC = () -> ABC_removeA_BC();

	/** Scenario: [A,B,C] -> remove(B) -> [A,C] 
	 * @return [A,C] after remove(B)
	 */
	private IndexedUnsortedList<Integer> ABC_removeB_AC() {
		IndexedUnsortedList<Integer> list = AB_addCAfterB_ABC(); 
		list.remove(ELEMENT_B);
		return list;
	}
	private Scenario<Integer> ABC_removeB_AC = () -> ABC_removeB_AC();

	/** Scenario: [A,B,C] -> remove(C) -> [A,B] 
	 * @return [A,B] after remove(C)
	 */
	private IndexedUnsortedList<Integer> ABC_removeC_AB() {
		IndexedUnsortedList<Integer> list = AB_addAtIndexTwoC_ABC(); 
		list.remove(ELEMENT_C);
		return list;
	}
	private Scenario<Integer> ABC_removeC_AB = () -> ABC_removeC_AB();

	/** Scenario: [A,B,C] -> remove(0) -> [B,C] 
	 * @return [B,C] after remove(0)
	 */
	private IndexedUnsortedList<Integer> ABC_removeAtIndexZero_BC() {
		IndexedUnsortedList<Integer> list = AB_addCAfterB_ABC(); 
		list.remove(0);
		return list;
	}
	private Scenario<Integer> ABC_removeAtIndexZero_BC = () -> ABC_removeAtIndexZero_BC();

	/** Scenario: [A,B,C] -> remove(1) -> [A,C] 
	 * @return [A,C] after remove(1)
	 */
	private IndexedUnsortedList<Integer> ABC_removeAtIndexOne_AC() {
		IndexedUnsortedList<Integer> list = AB_addAtIndexTwoC_ABC(); 
		list.remove(1);
		return list;
	}
	private Scenario<Integer> ABC_removeAtIndexOne_AC = () -> ABC_removeAtIndexOne_AC();

	/** Scenario: [A,B,C] -> remove(2) -> [A,B] 
	 * @return [A,B] after remove(2)
	 */
	private IndexedUnsortedList<Integer> ABC_removeAtIndexTwo_AB() {
		IndexedUnsortedList<Integer> list = AB_addToRearC_ABC(); 
		list.remove(2);
		return list;
	}
	private Scenario<Integer> ABC_removeAtIndexTwo_AB = () -> ABC_removeAtIndexTwo_AB();

	/** Scenario: [A,B,C] -> iteratorRemoveAfterNextA -> [A,B] 
	 * @return [A,B] after iteratorRemoveAfterNextA
	 */
	private IndexedUnsortedList<Integer> ABC_iterRemoveAfterNextA_BC() {
		IndexedUnsortedList<Integer> list = AB_addAtIndexTwoC_ABC(); 
		Iterator<Integer> it = list.iterator();
		it.next();
		it.remove();
		return list;
	}
	private Scenario<Integer> ABC_iterRemoveAfterNextA_BC = () -> ABC_iterRemoveAfterNextA_BC();

	/** Scenario: [A,B,C] -> iteratorRemoveAfterNextNextB -> [A,C] 
	 * @return [A,C] after iteratorRemoveAfterNextNextB
	 */
	private IndexedUnsortedList<Integer> ABC_iterRemoveAfterNextNextB_AC() {
		IndexedUnsortedList<Integer> list = AB_addCAfterB_ABC(); 
		Iterator<Integer> it = list.iterator();
		it.next();
		it.next();
		it.remove();
		return list;
	}
	private Scenario<Integer> ABC_iterRemoveAfterNextNextB_AC = () -> ABC_iterRemoveAfterNextNextB_AC();

	/** Scenario: [A,B,C] -> iteratorRemoveAfterNextNextNextB -> [A,B] 
	 * @return [A,B] after iteratorRemoveAfterNextNextNextB
	 */
	private IndexedUnsortedList<Integer> ABC_iterRemoveAfterNextNextNextC_AB() {
		IndexedUnsortedList<Integer> list = AB_addToRearC_ABC();
		Iterator<Integer> it = list.iterator();
		it.next();
		it.next();
		it.next();
		it.remove();
		return list;
	}
	private Scenario<Integer> ABC_iterRemoveAfterNextNextNextC_AB = () -> ABC_iterRemoveAfterNextNextNextC_AB();

	/** Scenario: [A,B,C] -> listIterRemoveAfterPreviousB -> [A,C] 
	 * @return [A,C] after listIterRemoveAfterPreviousB
	 */
	private IndexedUnsortedList<Integer> ABC_listIterRemoveAfterPreviousB_AC() {
		IndexedUnsortedList<Integer> list = AB_addToRearC_ABC();
		ListIterator<Integer> it = list.listIterator(2);
		it.previous();
		it.remove();
		return list;
	}
	private Scenario<Integer> ABC_listIterRemoveAfterPreviousB_AC = () -> ABC_listIterRemoveAfterPreviousB_AC();

	/** Scenario: [A,B,C] -> listIterSetAfterNextD -> [A,B,D]
	 * @return [A,B,D] after listIterSetAfterNextD
	 */
	private IndexedUnsortedList<Integer> ABC_listIterSetAfterNextD_ABD() {
		IndexedUnsortedList<Integer> list = AB_addAtIndexTwoC_ABC(); 
		ListIterator<Integer> it = list.listIterator(2);
		it.next();
		it.set(ELEMENT_D);
		return list;
	}
	private Scenario<Integer> ABC_listIterSetAfterNextD_ABD = () -> ABC_listIterSetAfterNextD_ABD();

	/** Scenario: [A,B,C] -> listIterSetAfterPreviousD -> [A,D,C]
	 * @return [A,D,C] after listIterSetAfterPreviousD
	 */
	private IndexedUnsortedList<Integer> ABC_listIterSetAfterPreviousD_ADC() {
		IndexedUnsortedList<Integer> list = AB_addToRearC_ABC(); 
		ListIterator<Integer> it = list.listIterator(2);
		it.previous();
		it.set(ELEMENT_D);
		return list;
	}
	private Scenario<Integer> ABC_listIterSetAfterPreviousD_ADC = () -> ABC_listIterSetAfterPreviousD_ADC();

	/** Scenario: [A,B,C] -> listIterSetAfterPreviousD -> [A,B,D]
	 * @return [A,B,D] after listIterSetAfterPreviousD
	 */
	private IndexedUnsortedList<Integer> ABC_listIterSetAfterPreviousD_ABD() {
		IndexedUnsortedList<Integer> list = AB_listIterAddAfterNextC_ABC(); 
		ListIterator<Integer> it = list.listIterator(3);
		it.previous();
		it.set(ELEMENT_D);
		return list;
	}
	private Scenario<Integer> ABC_listIterSetAfterPreviousD_ABD = () -> ABC_listIterSetAfterPreviousD_ABD();

	/////////////////////////////////
	//XXX Tests for 0-element list
	/////////////////////////////////
	
	/** Run all tests on scenarios resulting in an empty list
	 * @param scenario lambda reference to scenario builder method
	 * @param scenarioName name of the scenario being tested
	 */
	private void testEmptyList(Scenario<Integer> scenario, String scenarioName) {
		System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
		try {
			// IndexedUnsortedList
			printTest(scenarioName + "_testRemoveFirst", testRemoveFirst(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testRemoveLast", testRemoveLast(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testRemoveX", testRemoveElement(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testFirst", testFirst(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testLast", testLast(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testContainsX", testContains(scenario.build(), ELEMENT_X, Result.False));
			printTest(scenarioName + "_testIsEmpty", testIsEmpty(scenario.build(), Result.True));
			printTest(scenarioName + "_testSize", testSize(scenario.build(), 0));
			printTest(scenarioName + "_testToString", testToString(scenario.build(), Result.ValidString));
			printTest(scenarioName + "_testAddToFront", testAddToFront(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddToRear", testAddToRear(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfterX", testAddAfter(scenario.build(), ELEMENT_X, ELEMENT_Z, Result.NoSuchElement));
			printTest(scenarioName + "_testAddAtIndexNeg1", testAddAtIndex(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddAtIndex0", testAddAtIndex(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex1", testAddAtIndex(scenario.build(), 1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSetNeg1", testSet(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSet0", testSet(scenario.build(), 0, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddX", testAdd(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testGetNeg1", testGet(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testGet0", testGet(scenario.build(), 0, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testIndexOfX", testIndexOf(scenario.build(), ELEMENT_X, -1));
			printTest(scenarioName + "_testRemoveNeg1", testRemoveIndex(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testRemove0", testRemoveIndex(scenario.build(), 0, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testContains", testContains(scenario.build(), ELEMENT_A, Result.False));
			// Iterator
			printTest(scenarioName + "_testIter", testIter(scenario.build(), Result.NoException));
			printTest(scenarioName + "_testIterHasNext", testIterHasNext(scenario.build().iterator(), Result.False));
			printTest(scenarioName + "_testIterNext", testIterNext(scenario.build().iterator(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testIterRemove", testIterRemove(scenario.build().iterator(), Result.IllegalState));
			// ListIterator
			if (SUPPORTS_LIST_ITERATOR) {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.NoException));
				printTest(scenarioName + "_testListIterNeg1", testListIter(scenario.build(), -1, Result.IndexOutOfBounds));
				printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.NoException));
				printTest(scenarioName + "_testListIter1", testListIter(scenario.build(), 1, Result.IndexOutOfBounds));
				printTest(scenarioName + "_testListIterHasNext", testIterHasNext(scenario.build().listIterator(), Result.False));
				printTest(scenarioName + "_testListIterNext", testIterNext(scenario.build().listIterator(), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIterRemove", testIterRemove(scenario.build().listIterator(), Result.IllegalState));
				printTest(scenarioName + "_testListIterHasPrevious", testListIterHasPrevious(scenario.build().listIterator(), Result.False));
				printTest(scenarioName + "_testListIterPrevious", testListIterPrevious(scenario.build().listIterator(), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIterAdd", testListIterAdd(scenario.build().listIterator(), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIterSet", testListIterSet(scenario.build().listIterator(), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIterNextIndex", testListIterNextIndex(scenario.build().listIterator(), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIter0NextIndex", testListIterNextIndex(scenario.build().listIterator(0), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIterPreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(), -1, Result.MatchingValue));
				printTest(scenarioName + "_testListIter0PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(0), -1, Result.MatchingValue));
			} else {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.UnsupportedOperation));
				printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.UnsupportedOperation));
			}
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}
	
	//////////////////////////////////
	//XXX Tests for 1-element list
	//////////////////////////////////
	
	/** Run all tests on scenarios resulting in a single element list
	 * @param scenario lambda reference to scenario builder method
	 * @param scenarioName name of the scenario being tested
	 * @param contents elements expected in the list after scenario has been set up
	 * @param contentsString contains character labels corresponding to values in contents
	 */
	private void testSingleElementList(Scenario<Integer> scenario, String scenarioName, Integer[] contents, String contentsString) {
		System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
		try {
			// IndexedUnsortedList
			printTest(scenarioName + "_testRemoveFirst", testRemoveFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveLast", testRemoveLast(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(0), testRemoveElement(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveX", testRemoveElement(scenario.build(), ELEMENT_X, Result.NoSuchElement));
			printTest(scenarioName + "_testFirst", testFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testLast", testLast(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testContains" + contentsString.charAt(0), testContains(scenario.build(), contents[0], Result.True));
			printTest(scenarioName + "_testContainsX", testContains(scenario.build(), ELEMENT_X, Result.False));
			printTest(scenarioName + "_testIsEmpty", testIsEmpty(scenario.build(), Result.False));
			printTest(scenarioName + "_testSize", testSize(scenario.build(), 1));
			printTest(scenarioName + "_testToString", testToString(scenario.build(), Result.ValidString));
			printTest(scenarioName + "_testAddToFront", testAddToFront(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddToRear", testAddToRear(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(0), testAddAfter(scenario.build(), contents[0], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfterX", testAddAfter(scenario.build(), ELEMENT_X, ELEMENT_Z, Result.NoSuchElement));
			printTest(scenarioName + "_testAddAtIndexNeg1", testAddAtIndex(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddAtIndex0", testAddAtIndex(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex1", testAddAtIndex(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex2", testAddAtIndex(scenario.build(), 2, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSetNeg1", testSet(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSet0", testSet(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet1", testSet(scenario.build(), 1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAdd", testAdd(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testGetNeg1", testGet(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testGet0", testGet(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testGet1", testGet(scenario.build(), 1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(0), testIndexOf(scenario.build(), contents[0], 0));
			printTest(scenarioName + "_testIndexOfX", testIndexOf(scenario.build(), ELEMENT_X, -1));
			printTest(scenarioName + "_testRemoveNeg1", testRemoveIndex(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testRemove0", testRemoveIndex(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove1", testRemoveIndex(scenario.build(), 1, null, Result.IndexOutOfBounds));
			// Iterator
			printTest(scenarioName + "_testIter", testIter(scenario.build(), Result.NoException));
			printTest(scenarioName + "_testIterHasNext", testIterHasNext(scenario.build().iterator(), Result.True));
			printTest(scenarioName + "_testIterNext", testIterNext(scenario.build().iterator(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testIterRemove", testIterRemove(scenario.build().iterator(), Result.IllegalState));
			printTest(scenarioName + "_iterNext_testIterHasNext", testIterHasNext(iterAfterNext(scenario.build(), 1), Result.False));
			printTest(scenarioName + "_iterNext_testIterNext", testIterNext(iterAfterNext(scenario.build(), 1), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNext_testIterRemove", testIterRemove(iterAfterNext(scenario.build(), 1), Result.NoException));
			printTest(scenarioName + "_iterNextRemove_testIterHasNext", testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.False));
			printTest(scenarioName + "_iterNextRemove_testIterNext", testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextRemove_testIterRemove", testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.IllegalState));
			// ListIterator
			if (SUPPORTS_LIST_ITERATOR) {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.NoException));
				printTest(scenarioName + "_testListIterNeg1", testListIter(scenario.build(), -1, Result.IndexOutOfBounds));
				printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.NoException));
				printTest(scenarioName + "_testListIter1", testListIter(scenario.build(), 1, Result.NoException));
				printTest(scenarioName + "_testListIter2", testListIter(scenario.build(), 2, Result.IndexOutOfBounds));
				printTest(scenarioName + "_testListIterHasNext", testIterHasNext(scenario.build().listIterator(), Result.True));
				printTest(scenarioName + "_testListIterNext", testIterNext(scenario.build().listIterator(), contents[0], Result.MatchingValue));
				printTest(scenarioName + "_testListIterNextIndex", testListIterNextIndex(scenario.build().listIterator(), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIterHasPrevious", testListIterHasPrevious(scenario.build().listIterator(), Result.False));
				printTest(scenarioName + "_testListIterPrevious", testListIterPrevious(scenario.build().listIterator(), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIterPreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(), -1, Result.MatchingValue));
				printTest(scenarioName + "_testListIterRemove", testIterRemove(scenario.build().listIterator(), Result.IllegalState));
				printTest(scenarioName + "_testListIterAdd", testListIterAdd(scenario.build().listIterator(), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIterSet", testListIterSet(scenario.build().listIterator(), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIterNextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(), 1), Result.NoException));
				printTest(scenarioName + "_testListIterNextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIterNextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNextRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterNext(scenario.build().listIterator(), 1)), Result.IllegalState));
				printTest(scenarioName + "_testListIterNextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), Result.NoException));
				printTest(scenarioName + "_testListIterNextPreviousRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1)), Result.IllegalState));
				printTest(scenarioName + "_testListIterNextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIterNextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0HasNext", testIterHasNext(scenario.build().listIterator(0), Result.True));
				printTest(scenarioName + "_testListIter0Next", testIterNext(scenario.build().listIterator(0), contents[0], Result.MatchingValue));
				printTest(scenarioName + "_testListIter0NextIndex", testListIterNextIndex(scenario.build().listIterator(0), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIter0HasPrevious", testListIterHasPrevious(scenario.build().listIterator(0), Result.False));
				printTest(scenarioName + "_testListIter0Previous", testListIterPrevious(scenario.build().listIterator(0), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIter0PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(0), -1, Result.MatchingValue));
				printTest(scenarioName + "_testListIter0Remove", testIterRemove(scenario.build().listIterator(0), Result.IllegalState));
				printTest(scenarioName + "_testListIter0Add", testListIterAdd(scenario.build().listIterator(0), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter0Set", testListIterSet(scenario.build().listIterator(0), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIter0NextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(0), 1), Result.NoException));
				printTest(scenarioName + "_testListIter0NextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter0NextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), Result.NoException));
				printTest(scenarioName + "_testListIter0NextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter0NextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1HasNext", testIterHasNext(scenario.build().listIterator(1), Result.False));
				printTest(scenarioName + "_testListIter1Next", testIterNext(scenario.build().listIterator(1), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIter1NextIndex", testListIterNextIndex(scenario.build().listIterator(1), 1, Result.MatchingValue));
				printTest(scenarioName + "_testListIter1HasPrevious", testListIterHasPrevious(scenario.build().listIterator(1), Result.True));
				printTest(scenarioName + "_testListIter1Previous", testListIterPrevious(scenario.build().listIterator(1), contents[0], Result.MatchingValue));
				printTest(scenarioName + "_testListIter1PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(1), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIter1Remove", testIterRemove(scenario.build().listIterator(1), Result.IllegalState));
				printTest(scenarioName + "_testListIter1Add", testListIterAdd(scenario.build().listIterator(1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter1Set", testListIterSet(scenario.build().listIterator(1), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIter1PreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(1), 1), Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousAdd", testListIterAdd(listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter1PreviousSet", testListIterSet(listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter1PreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), ELEMENT_X, Result.NoException));
		  	} else {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.UnsupportedOperation));
				printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.UnsupportedOperation));
		  	}
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

	/////////////////////////////////
	//XXX Tests for 2-element list
	/////////////////////////////////
	
	/** Run all tests on scenarios resulting in a two-element list
	 * @param scenario lambda reference to scenario builder method
	 * @param scenarioName name of the scenario being tested
	 * @param contents elements expected in the list after scenario has been set up
	 * @param contentsString contains character labels corresponding to values in contents 
	 */
	private void testTwoElementList(Scenario<Integer> scenario, String scenarioName, Integer[] contents, String contentsString) {
		System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
		try {
			printTest(scenarioName + "_testRemoveFirst", testRemoveFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveLast", testRemoveLast(scenario.build(), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(0), testRemoveElement(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(1), testRemoveElement(scenario.build(), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveX", testRemoveElement(scenario.build(), ELEMENT_X, Result.NoSuchElement));
			printTest(scenarioName + "_testFirst", testFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testLast", testLast(scenario.build(), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testContains" + contentsString.charAt(0), testContains(scenario.build(), contents[0], Result.True));
			printTest(scenarioName + "_testContains" + contentsString.charAt(1), testContains(scenario.build(), contents[1], Result.True));
			printTest(scenarioName + "_testContainsX", testContains(scenario.build(), ELEMENT_X, Result.False));
			printTest(scenarioName + "_testIsEmpty", testIsEmpty(scenario.build(), Result.False));
			printTest(scenarioName + "_testSize", testSize(scenario.build(), 2));
			printTest(scenarioName + "_testToString", testToString(scenario.build(), Result.ValidString));
			printTest(scenarioName + "_testAddToFront", testAddToFront(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddToRear", testAddToRear(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(0), testAddAfter(scenario.build(), contents[0], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(1), testAddAfter(scenario.build(), contents[1], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfterX", testAddAfter(scenario.build(), ELEMENT_X, ELEMENT_Z, Result.NoSuchElement));
			printTest(scenarioName + "_testAddAtIndexNeg1", testAddAtIndex(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddAtIndex0", testAddAtIndex(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex1", testAddAtIndex(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex2", testAddAtIndex(scenario.build(), 2, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex3", testAddAtIndex(scenario.build(), 3, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSetNeg1", testSet(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSet0", testSet(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet1", testSet(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet2", testSet(scenario.build(), 2, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAdd", testAdd(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testGetNeg1", testGet(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testGet0", testGet(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testGet1", testGet(scenario.build(), 1, contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testGet2", testGet(scenario.build(), 2, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(0), testIndexOf(scenario.build(), contents[0], 0));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(1), testIndexOf(scenario.build(), contents[1], 1));
			printTest(scenarioName + "_testIndexOfX", testIndexOf(scenario.build(), ELEMENT_X, -1));
			printTest(scenarioName + "_testRemoveNeg1", testRemoveIndex(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testRemove0", testRemoveIndex(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove1", testRemoveIndex(scenario.build(), 1, contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testRemove2", testRemoveIndex(scenario.build(), 2, null, Result.IndexOutOfBounds));
			// Iterator
			printTest(scenarioName + "_testIter", testIter(scenario.build(), Result.NoException));
			printTest(scenarioName + "_testIterHasNext", testIterHasNext(scenario.build().iterator(), Result.True));
			printTest(scenarioName + "_testIterNext", testIterNext(scenario.build().iterator(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testIterRemove", testIterRemove(scenario.build().iterator(), Result.IllegalState));
			printTest(scenarioName + "_iterNext_testIterHasNext", testIterHasNext(iterAfterNext(scenario.build(), 1), Result.True));
			printTest(scenarioName + "_iterNext_testIterNext", testIterNext(iterAfterNext(scenario.build(), 1), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_iterNext_testIterRemove", testIterRemove(iterAfterNext(scenario.build(), 1), Result.NoException));
			printTest(scenarioName + "_iterNextRemove_testIterHasNext", testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.True));
			printTest(scenarioName + "_iterNextRemove_testIterNext", testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_iterNextRemove_testIterRemove", testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.IllegalState));
			printTest(scenarioName + "_iterNextNext_testIterHasNext", testIterHasNext(iterAfterNext(scenario.build(), 2), Result.False));
			printTest(scenarioName + "_iterNextNext_testIterNext", testIterNext(iterAfterNext(scenario.build(), 2), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextNext_testIterRemove", testIterRemove(iterAfterNext(scenario.build(), 2), Result.NoException));
			printTest(scenarioName + "_iterNextNextRemove_testIterHasNext", testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)), Result.False));
			printTest(scenarioName + "_iterNextNextRemove_testIterNext", testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextNextRemove_testIterRemove", testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 2)), Result.IllegalState));
			if (SUPPORTS_LIST_ITERATOR) {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.NoException));
				printTest(scenarioName + "_testListIterNeg1", testListIter(scenario.build(), -1, Result.IndexOutOfBounds));
				printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.NoException));
				printTest(scenarioName + "_testListIter1", testListIter(scenario.build(), 1, Result.NoException));
				printTest(scenarioName + "_testListIter2", testListIter(scenario.build(), 2, Result.NoException));
				printTest(scenarioName + "_testListIter3", testListIter(scenario.build(), 3, Result.IndexOutOfBounds));
				printTest(scenarioName + "_testListIterHasNext", testIterHasNext(scenario.build().listIterator(), Result.True));
				printTest(scenarioName + "_testListIterNext", testIterNext(scenario.build().listIterator(), contents[0], Result.MatchingValue));
				printTest(scenarioName + "_testListIterNextIndex", testListIterNextIndex(scenario.build().listIterator(), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIterHasPrevious", testListIterHasPrevious(scenario.build().listIterator(), Result.False));
				printTest(scenarioName + "_testListIterPrevious", testListIterPrevious(scenario.build().listIterator(), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIterPreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(), -1, Result.MatchingValue));
				printTest(scenarioName + "_testListIterRemove", testIterRemove(scenario.build().listIterator(), Result.IllegalState));
				printTest(scenarioName + "_testListIterAdd", testListIterAdd(scenario.build().listIterator(), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIterSet", testListIterSet(scenario.build().listIterator(), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIterNextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(), 1), Result.NoException));
				printTest(scenarioName + "_testListIterNextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIterNextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNextRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterNext(scenario.build().listIterator(), 1)), Result.IllegalState));
				printTest(scenarioName + "_testListIterNextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), Result.NoException));
				printTest(scenarioName + "_testListIterNextPreviousRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1)), Result.IllegalState));
				printTest(scenarioName + "_testListIterNextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIterNextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0HasNext", testIterHasNext(scenario.build().listIterator(0), Result.True));
				printTest(scenarioName + "_testListIter0Next", testIterNext(scenario.build().listIterator(0), contents[0], Result.MatchingValue));
				printTest(scenarioName + "_testListIter0NextIndex", testListIterNextIndex(scenario.build().listIterator(0), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIter0HasPrevious", testListIterHasPrevious(scenario.build().listIterator(0), Result.False));
				printTest(scenarioName + "_testListIter0Previous", testListIterPrevious(scenario.build().listIterator(0), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIter0PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(0), -1, Result.MatchingValue));
				printTest(scenarioName + "_testListIter0Remove", testIterRemove(scenario.build().listIterator(0), Result.IllegalState));
				printTest(scenarioName + "_testListIter0Add", testListIterAdd(scenario.build().listIterator(0), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter0Set", testListIterSet(scenario.build().listIterator(0), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIter0NextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(0), 1), Result.NoException));
				printTest(scenarioName + "_testListIter0NextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));	
				printTest(scenarioName + "_testListIter0NextNextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(0), 2), Result.NoException));
				printTest(scenarioName + "_testListIter0NextNextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(0), 2), ELEMENT_X, Result.NoException));	
				printTest(scenarioName + "_testListIter0NextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(0), 2), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0NextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), Result.NoException));
				printTest(scenarioName + "_testListIter0NextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter0NextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1HasNext", testIterHasNext(scenario.build().listIterator(1), Result.True));
				printTest(scenarioName + "_testListIter1Next", testIterNext(scenario.build().listIterator(1), contents[1], Result.MatchingValue));
				printTest(scenarioName + "_testListIter1NextIndex", testListIterNextIndex(scenario.build().listIterator(1), 1, Result.MatchingValue));
				printTest(scenarioName + "_testListIter1HasPrevious", testListIterHasPrevious(scenario.build().listIterator(1), Result.True));
				printTest(scenarioName + "_testListIter1Previous", testListIterPrevious(scenario.build().listIterator(1), contents[0], Result.MatchingValue));
				printTest(scenarioName + "_testListIter1PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(1), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIter1Remove", testIterRemove(scenario.build().listIterator(1), Result.IllegalState));
				printTest(scenarioName + "_testListIter1Add", testListIterAdd(scenario.build().listIterator(1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1Set", testListIterSet(scenario.build().listIterator(1), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIter1PreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(1), 1), Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousAdd", testListIterAdd(listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter1PreviousSet", testListIterSet(listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter1PreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter2HasNext", testIterHasNext(scenario.build().listIterator(2), Result.False));
				printTest(scenarioName + "_testListIter2Next", testIterNext(scenario.build().listIterator(2), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIter2NextIndex", testListIterNextIndex(scenario.build().listIterator(2), 2, Result.MatchingValue));
				printTest(scenarioName + "_testListIter2HasPrevious", testListIterHasPrevious(scenario.build().listIterator(2), Result.True));
				printTest(scenarioName + "_testListIter2Previous", testListIterPrevious(scenario.build().listIterator(2), contents[1], Result.MatchingValue));
				printTest(scenarioName + "_testListIter2PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(2), 1, Result.MatchingValue));
				printTest(scenarioName + "_testListIter2Remove", testIterRemove(scenario.build().listIterator(2), Result.IllegalState));
				printTest(scenarioName + "_testListIter2Add", testListIterAdd(scenario.build().listIterator(2), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter2Set", testListIterSet(scenario.build().listIterator(2), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIter2PreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(2), 1), Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousAdd", testListIterAdd(listIterAfterPrevious(scenario.build().listIterator(2), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter2PreviousSet", testListIterSet(listIterAfterPrevious(scenario.build().listIterator(2), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1), Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter2PreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousPreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(2), 2), Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousPreviousAdd", testListIterAdd(listIterAfterPrevious(scenario.build().listIterator(2), 2), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter2PreviousPreviousSet", testListIterSet(listIterAfterPrevious(scenario.build().listIterator(2), 2), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousPreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 2), 1), Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousPreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 2), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter2PreviousPreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 2), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousPreviousHasPrevious", testListIterHasPrevious(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 2), 1), Result.True));
			} else {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.UnsupportedOperation));
				printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.UnsupportedOperation));
			}
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

	//////////////////////////////////
	//XXX Tests for 3-element list
	//////////////////////////////////
	
	/** Run all tests on scenarios resulting in a three-element list
	 * @param scenario lambda reference to scenario builder method
	 * @param scenarioName name of the scenario being tested
	 * @param contents elements expected in the list after scenario has been set up
	 * @param contentsString contains character labels corresponding to values in contents 
	 */
	private void testThreeElementList(Scenario<Integer> scenario, String scenarioName, Integer[] contents, String contentsString) {
		System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
		try {
			printTest(scenarioName + "_testRemoveFirst", testRemoveFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveLast", testRemoveLast(scenario.build(), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(0), testRemoveElement(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(1), testRemoveElement(scenario.build(), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(2), testRemoveElement(scenario.build(), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveX", testRemoveElement(scenario.build(), ELEMENT_X, Result.NoSuchElement));
			printTest(scenarioName + "_testFirst", testFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testLast", testLast(scenario.build(), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testContains" + contentsString.charAt(0), testContains(scenario.build(), contents[0], Result.True));
			printTest(scenarioName + "_testContains" + contentsString.charAt(1), testContains(scenario.build(), contents[1], Result.True));
			printTest(scenarioName + "_testContains" + contentsString.charAt(2), testContains(scenario.build(), contents[2], Result.True));
			printTest(scenarioName + "_testContainsX", testContains(scenario.build(), ELEMENT_X, Result.False));
			printTest(scenarioName + "_testIsEmpty", testIsEmpty(scenario.build(), Result.False));
			printTest(scenarioName + "_testSize", testSize(scenario.build(), 3));
			printTest(scenarioName + "_testToString", testToString(scenario.build(), Result.ValidString));
			printTest(scenarioName + "_testAddToFront", testAddToFront(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddToRear", testAddToRear(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(0), testAddAfter(scenario.build(), contents[0], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(1), testAddAfter(scenario.build(), contents[1], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(2), testAddAfter(scenario.build(), contents[2], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfterX", testAddAfter(scenario.build(), ELEMENT_X, ELEMENT_Z, Result.NoSuchElement));
			printTest(scenarioName + "_testAddAtIndexNeg1", testAddAtIndex(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddAtIndex0", testAddAtIndex(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex1", testAddAtIndex(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex2", testAddAtIndex(scenario.build(), 2, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex3", testAddAtIndex(scenario.build(), 3, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex4", testAddAtIndex(scenario.build(), 4, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSetNeg1", testSet(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSet0", testSet(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet1", testSet(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet2", testSet(scenario.build(), 2, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet3", testSet(scenario.build(), 3, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAdd", testAdd(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testGetNeg1", testGet(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testGet0", testGet(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testGet1", testGet(scenario.build(), 1, contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testGet2", testGet(scenario.build(), 2, contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testGet3", testGet(scenario.build(), 3, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(0), testIndexOf(scenario.build(), contents[0], 0));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(1), testIndexOf(scenario.build(), contents[1], 1));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(2), testIndexOf(scenario.build(), contents[2], 2));
			printTest(scenarioName + "_testIndexOfX", testIndexOf(scenario.build(), ELEMENT_X, -1));
			printTest(scenarioName + "_testRemoveNeg1", testRemoveIndex(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testRemove0", testRemoveIndex(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove1", testRemoveIndex(scenario.build(), 1, contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testRemove2", testRemoveIndex(scenario.build(), 2, contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testRemove2", testRemoveIndex(scenario.build(), 3, null, Result.IndexOutOfBounds));
			// Iterator
			printTest(scenarioName + "_testIter", testIter(scenario.build(), Result.NoException));
			printTest(scenarioName + "_testIterHasNext", testIterHasNext(scenario.build().iterator(), Result.True));
			printTest(scenarioName + "_testIterNext", testIterNext(scenario.build().iterator(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testIterRemove", testIterRemove(scenario.build().iterator(), Result.IllegalState));
			printTest(scenarioName + "_iterNext_testIterHasNext", testIterHasNext(iterAfterNext(scenario.build(), 1), Result.True));
			printTest(scenarioName + "_iterNext_testIterNext", testIterNext(iterAfterNext(scenario.build(), 1), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_iterNext_testIterRemove", testIterRemove(iterAfterNext(scenario.build(), 1), Result.NoException));
			printTest(scenarioName + "_iterNextRemove_testIterHasNext", testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.True));
			printTest(scenarioName + "_iterNextRemove_testIterNext", testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_iterNextRemove_testIterRemove", testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.IllegalState));
			printTest(scenarioName + "_iterNextNext_testIterHasNext", testIterHasNext(iterAfterNext(scenario.build(), 2), Result.True));
			printTest(scenarioName + "_iterNextNext_testIterNext", testIterNext(iterAfterNext(scenario.build(), 2), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_iterNextNext_testIterRemove", testIterRemove(iterAfterNext(scenario.build(), 2), Result.NoException));
			printTest(scenarioName + "_iterNextNextRemove_testIterHasNext", testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)), Result.True));
			printTest(scenarioName + "_iterNextNextRemove_testIterNext", testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_iterNextNextRemove_testIterRemove", testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 2)), Result.IllegalState));
			printTest(scenarioName + "_iterNextNextNext_testIterHasNext", testIterHasNext(iterAfterNext(scenario.build(), 3), Result.False));
			printTest(scenarioName + "_iterNextNextNext_testIterNext", testIterNext(iterAfterNext(scenario.build(), 3), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextNextNext_testIterRemove", testIterRemove(iterAfterNext(scenario.build(), 3), Result.NoException));
			printTest(scenarioName + "_iterNextNextNextRemove_testIterHasNext", testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 3)), Result.False));
			printTest(scenarioName + "_iterNextNextNextRemove_testIterNext", testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 3)), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextNextNextRemove_testIterRemove", testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 3)), Result.IllegalState));
			if (SUPPORTS_LIST_ITERATOR) {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.NoException));
				printTest(scenarioName + "_testListIterNeg1", testListIter(scenario.build(), -1, Result.IndexOutOfBounds));
				printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.NoException));
				printTest(scenarioName + "_testListIter1", testListIter(scenario.build(), 1, Result.NoException));
				printTest(scenarioName + "_testListIter2", testListIter(scenario.build(), 2, Result.NoException));
				printTest(scenarioName + "_testListIter3", testListIter(scenario.build(), 3, Result.NoException));
				printTest(scenarioName + "_testListIter4", testListIter(scenario.build(), 4, Result.IndexOutOfBounds));
				printTest(scenarioName + "_testListIterHasNext", testIterHasNext(scenario.build().listIterator(), Result.True));
				printTest(scenarioName + "_testListIterNext", testIterNext(scenario.build().listIterator(), contents[0], Result.MatchingValue));
				printTest(scenarioName + "_testListIterNextIndex", testListIterNextIndex(scenario.build().listIterator(), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIterHasPrevious", testListIterHasPrevious(scenario.build().listIterator(), Result.False));
				printTest(scenarioName + "_testListIterPrevious", testListIterPrevious(scenario.build().listIterator(), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIterPreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(), -1, Result.MatchingValue));
				printTest(scenarioName + "_testListIterRemove", testIterRemove(scenario.build().listIterator(), Result.IllegalState));
				printTest(scenarioName + "_testListIterAdd", testListIterAdd(scenario.build().listIterator(), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIterSet", testListIterSet(scenario.build().listIterator(), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIterNextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(), 1), Result.NoException));
				printTest(scenarioName + "_testListIterNextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIterNextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNextRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterNext(scenario.build().listIterator(), 1)), Result.IllegalState));
				printTest(scenarioName + "_testListIterNextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), Result.NoException));
				printTest(scenarioName + "_testListIterNextPreviousRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1)), Result.IllegalState));
				printTest(scenarioName + "_testListIterNextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIterNextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0HasNext", testIterHasNext(scenario.build().listIterator(0), Result.True));
				printTest(scenarioName + "_testListIter0Next", testIterNext(scenario.build().listIterator(0), contents[0], Result.MatchingValue));
				printTest(scenarioName + "_testListIter0NextIndex", testListIterNextIndex(scenario.build().listIterator(0), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIter0HasPrevious", testListIterHasPrevious(scenario.build().listIterator(0), Result.False));
				printTest(scenarioName + "_testListIter0Previous", testListIterPrevious(scenario.build().listIterator(0), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIter0PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(0), -1, Result.MatchingValue));
				printTest(scenarioName + "_testListIter0Remove", testIterRemove(scenario.build().listIterator(0), Result.IllegalState));
				printTest(scenarioName + "_testListIter0Add", testListIterAdd(scenario.build().listIterator(0), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter0Set", testListIterSet(scenario.build().listIterator(0), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIter0NextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(0), 1), Result.NoException));
				printTest(scenarioName + "_testListIter0NextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));	
				printTest(scenarioName + "_testListIter0NextNextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(0), 2), Result.NoException));
				printTest(scenarioName + "_testListIter0NextNextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(0), 2), ELEMENT_X, Result.NoException));	
				printTest(scenarioName + "_testListIter0NextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(0), 2), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0NextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), Result.NoException));
				printTest(scenarioName + "_testListIter0NextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter0NextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1HasNext", testIterHasNext(scenario.build().listIterator(1), Result.True));
				printTest(scenarioName + "_testListIter1Next", testIterNext(scenario.build().listIterator(1), contents[1], Result.MatchingValue));
				printTest(scenarioName + "_testListIter1NextIndex", testListIterNextIndex(scenario.build().listIterator(1), 1, Result.MatchingValue));
				printTest(scenarioName + "_testListIter1HasPrevious", testListIterHasPrevious(scenario.build().listIterator(1), Result.True));
				printTest(scenarioName + "_testListIter1Previous", testListIterPrevious(scenario.build().listIterator(1), contents[0], Result.MatchingValue));
				printTest(scenarioName + "_testListIter1PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(1), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIter1Remove", testIterRemove(scenario.build().listIterator(1), Result.IllegalState));
				printTest(scenarioName + "_testListIter1Add", testListIterAdd(scenario.build().listIterator(1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1Set", testListIterSet(scenario.build().listIterator(1), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIter1PreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(1), 1), Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousAdd", testListIterAdd(listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter1PreviousSet", testListIterSet(listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter1PreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter2HasNext", testIterHasNext(scenario.build().listIterator(2), Result.True));
				printTest(scenarioName + "_testListIter2Next", testIterNext(scenario.build().listIterator(2), contents[2], Result.MatchingValue));
				printTest(scenarioName + "_testListIter2NextIndex", testListIterNextIndex(scenario.build().listIterator(2), 2, Result.MatchingValue));
				printTest(scenarioName + "_testListIter2HasPrevious", testListIterHasPrevious(scenario.build().listIterator(2), Result.True));
				printTest(scenarioName + "_testListIter2Previous", testListIterPrevious(scenario.build().listIterator(2), contents[1], Result.MatchingValue));
				printTest(scenarioName + "_testListIter2PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(2), 1, Result.MatchingValue));
				printTest(scenarioName + "_testListIter2Remove", testIterRemove(scenario.build().listIterator(2), Result.IllegalState));
				printTest(scenarioName + "_testListIter2Add", testListIterAdd(scenario.build().listIterator(2), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter2Set", testListIterSet(scenario.build().listIterator(2), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIter2PreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(2), 1), Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousAdd", testListIterAdd(listIterAfterPrevious(scenario.build().listIterator(2), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter2PreviousSet", testListIterSet(listIterAfterPrevious(scenario.build().listIterator(2), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1), Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter2PreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousPreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(2), 2), Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousPreviousAdd", testListIterAdd(listIterAfterPrevious(scenario.build().listIterator(2), 2), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter2PreviousPreviousSet", testListIterSet(listIterAfterPrevious(scenario.build().listIterator(2), 2), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousPreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 2), 1), Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousPreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 2), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter2PreviousPreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 2), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousPreviousHasPrevious", testListIterHasPrevious(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 2), 1), Result.True));
				printTest(scenarioName + "_testListIter3HasNext", testIterHasNext(scenario.build().listIterator(3), Result.False));
				printTest(scenarioName + "_testListIter3Next", testIterNext(scenario.build().listIterator(3), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIter3NextIndex", testListIterNextIndex(scenario.build().listIterator(3), 3, Result.MatchingValue));
				printTest(scenarioName + "_testListIter3HasPrevious", testListIterHasPrevious(scenario.build().listIterator(3), Result.True));
				printTest(scenarioName + "_testListIter3Previous", testListIterPrevious(scenario.build().listIterator(3), contents[2], Result.MatchingValue));
				printTest(scenarioName + "_testListIter3PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(3), 2, Result.MatchingValue));
				printTest(scenarioName + "_testListIter3Remove", testIterRemove(scenario.build().listIterator(3), Result.IllegalState));
				printTest(scenarioName + "_testListIter3Add", testListIterAdd(scenario.build().listIterator(3), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter3Set", testListIterSet(scenario.build().listIterator(3), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIter3PreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(3), 1), Result.NoException));
				printTest(scenarioName + "_testListIter3PreviousAdd", testListIterAdd(listIterAfterPrevious(scenario.build().listIterator(3), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter3PreviousSet", testListIterSet(listIterAfterPrevious(scenario.build().listIterator(3), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter3PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 1), 1), Result.NoException));
				printTest(scenarioName + "_testListIter3PreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 1), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter3PreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter3PreviousPreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(3), 2), Result.NoException));
				printTest(scenarioName + "_testListIter3PreviousPreviousAdd", testListIterAdd(listIterAfterPrevious(scenario.build().listIterator(3), 2), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter3PreviousPreviousSet", testListIterSet(listIterAfterPrevious(scenario.build().listIterator(3), 2), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter3PreviousPreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 2), 1), Result.NoException));
				printTest(scenarioName + "_testListIter3PreviousPreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 2), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter3PreviousPreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 2), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter3PreviousPreviousHasPrevious", testListIterHasPrevious(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 2), 1), Result.True));
				printTest(scenarioName + "_testListIter3PreviousPreviousPreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(3), 3), Result.NoException));
				printTest(scenarioName + "_testListIter3PreviousPreviousPreviousAdd", testListIterAdd(listIterAfterPrevious(scenario.build().listIterator(3), 3), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter3PreviousPreviousPreviousSet", testListIterSet(listIterAfterPrevious(scenario.build().listIterator(3), 3), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter3PreviousPreviousPreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 3), 1), Result.NoException));
				printTest(scenarioName + "_testListIter3PreviousPreviousPreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 3), 1), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testListIter3PreviousPreviousPreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 3), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter3PreviousPreviousPreviousHasPrevious", testListIterHasPrevious(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 3), 1), Result.True));


			} else {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.UnsupportedOperation));
				printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.UnsupportedOperation));
			}
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

	////////////////////////////
	// XXX LIST TEST METHODS
	////////////////////////////

	/** Runs removeFirst() method on given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedElement element or null if expectedResult is an Exception
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testRemoveFirst(IndexedUnsortedList<Integer> list, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.removeFirst();
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testRemoveFirst", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs removeLast() method on given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedElement element or null if expectedResult is an Exception
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testRemoveLast(IndexedUnsortedList<Integer> list, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.removeLast();
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testRemoveLast", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs removeLast() method on given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param element element to remove
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testRemoveElement(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.remove(element);
			if (retVal.equals(element)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testRemoveElement", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs first() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedElement element or null if expectedResult is an Exception
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testFirst(IndexedUnsortedList<Integer> list, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.first();
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testFirst", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs last() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedElement element or null if expectedResult is an Exception
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testLast(IndexedUnsortedList<Integer> list, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.last();
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testLast", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs contains() method on a given list and element and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testContains(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			if (list.contains(element)) {
				result = Result.True;
			} else {
				result = Result.False;
			}
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testContains", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs isEmpty() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIsEmpty(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			if (list.isEmpty()) {
				result = Result.True;
			} else {
				result = Result.False;
			}
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIsEmpty", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs size() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedSize
	 * @return test success
	 */
	private boolean testSize(IndexedUnsortedList<Integer> list, int expectedSize) {
		try {
			return (list.size() == expectedSize);
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testSize", e.toString());
			e.printStackTrace();
			return false;
		}
	}

	/** Runs toString() method on given list and attempts to confirm non-default or empty String
	 * difficult to test - just confirm that default address output has been overridden
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testToString(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			String str = list.toString().trim();
			if (showToString) {
				System.out.println("toString() output: " + str);
			}
			if (str.length() < (list.size() + list.size()/2 + 2)) { //elements + commas + '[' + ']'
				result = Result.Fail;
			} else {
				char lastChar = str.charAt(str.length() - 1);
				char firstChar = str.charAt(0);
				if (firstChar != '[' || lastChar != ']') {
					result = Result.Fail;
				} else if (str.contains("@")
						&& !str.contains(" ")
						&& Character.isLetter(str.charAt(0))
						&& (Character.isDigit(lastChar) || (lastChar >= 'a' && lastChar <= 'f'))) {
					result = Result.Fail; // looks like default toString()
				} else {
					result = Result.ValidString;
				}
			}
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testToString", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs addToFront() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAddToFront(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			list.addToFront(element);
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddToFront",  e.toString());
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs addToRear() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAddToRear(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			list.addToRear(element);
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddToRear", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs addAfter() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param target
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAddAfter(IndexedUnsortedList<Integer> list, Integer target, Integer element, Result expectedResult) {
		Result result;
		try {
			list.addAfter(element, target);
			result = Result.NoException;
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddAfter", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs add(int, T) method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param index
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAddAtIndex(IndexedUnsortedList<Integer> list, int index, Integer element, Result expectedResult) {
		Result result;
		try {
			list.add(index, element);
			result = Result.NoException;
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddAtIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs add(T) method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAdd(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			list.add(element);
			result = Result.NoException;
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddAtIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs set(int, T) method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param index
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testSet(IndexedUnsortedList<Integer> list, int index, Integer element, Result expectedResult) {
		Result result;
		try {
			list.set(index, element);
			result = Result.NoException;
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testSet", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs get() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param index
	 * @param expectedElement
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testGet(IndexedUnsortedList<Integer> list, int index, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.get(index);
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testGet", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs remove(index) method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param index
	 * @param expectedElement
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testRemoveIndex(IndexedUnsortedList<Integer> list, int index, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.remove(index);
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testRemoveIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs indexOf() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param element
	 * @param expectedIndex
	 * @return test success
	 */
	private boolean testIndexOf(IndexedUnsortedList<Integer> list, Integer element, int expectedIndex) {
		try {
			return list.indexOf(element) == expectedIndex;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIndexOf", e.toString());
			e.printStackTrace();
			return false;
		}
	}

	////////////////////////////
	// XXX ITERATOR TESTS
	////////////////////////////

	/** Runs iterator() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIter(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			Iterator<Integer> it = list.iterator();
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIter", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs list's iterator hasNext() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to hasNext()
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIterHasNext(Iterator<Integer> iterator, Result expectedResult) {
		Result result;
		try {
			if (iterator.hasNext()) {
				result = Result.True;
			} else {
				result = Result.False;
			}
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIterHasNext", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs list's iterator next() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to hasNext()
	 * @param expectedValue the Integer expected from next() or null if an exception is expected
	 * @param expectedResult MatchingValue or expected exception
	 * @return test success
	 */
	private boolean testIterNext(Iterator<Integer> iterator, Integer expectedValue, Result expectedResult) {
		Result result;
		try {
			Integer retVal = iterator.next();
			if (retVal.equals(expectedValue)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIterNext", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs list's iterator remove() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to remove()
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIterRemove(Iterator<Integer> iterator, Result expectedResult) {
		Result result;
		try {
			iterator.remove();
			result = Result.NoException;
		} catch (IllegalStateException e) {
			result = Result.IllegalState;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIterRemove", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs iterator() method twice on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIterConcurrent(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			Iterator<Integer> it1 = list.iterator();
			@SuppressWarnings("unused")
			Iterator<Integer> it2 = list.iterator();
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIterConcurrent", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	//////////////////////////////////////////////////////////
	//XXX HELPER METHODS FOR TESTING ITERATORS
	//Note: You can create other similar helpers if you want
	//something slightly different.
	//////////////////////////////////////////////////////////
	
	/** Helper for testing iterators. Return an Iterator that has been advanced numCallsToNext times.
	 * @param list
	 * @param numCallsToNext
	 * @return Iterator for given list, after numCallsToNext
	 */
	private Iterator<Integer> iterAfterNext(IndexedUnsortedList<Integer> list, int numCallsToNext) {
		Iterator<Integer> it = list.iterator();
		for (int i = 0; i < numCallsToNext; i++) {
			it.next();
		}
		return it;
	}

/** Helper for testing iterators. Return an Iterator that has been advanced numCallsToNext times.
	 * @param previousInterator 
	 * @param numCallsToNext
	 * @return Iterator for given list, after numCallsToNext
	 */
	private Iterator<Integer> iterAfterNext(Iterator<Integer> previousIterator, int numCallsToNext) {
		for (int i = 0; i < numCallsToNext; i++) {
			previousIterator.next();
		}
		return previousIterator;
	}

	/** Helper for testing iterators. Return an Iterator that has had remove() called once.
	 * @param iterator
	 * @return same Iterator following a call to remove()
	 */
	private Iterator<Integer> iterAfterRemove(Iterator<Integer> iterator) {
		iterator.remove();
		return iterator;
	}

	////////////////////////////////////////////////////////////////////////
	// XXX LISTITERATOR TESTS
	// Note: can use Iterator tests for hasNext(), next(), and remove()
	////////////////////////////////////////////////////////////////////////

	/** Runs listIterator() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIter(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			Iterator<Integer> it = list.listIterator();
			result = Result.NoException;
		} catch (UnsupportedOperationException e) {
			result = Result.UnsupportedOperation;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIter", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs listIterator(index) method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @param startingIndex
	 * @return test success
	 */
	private boolean testListIter(IndexedUnsortedList<Integer> list, int startingIndex, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			Iterator<Integer> it = list.listIterator(startingIndex);
			result = Result.NoException;
		} catch (UnsupportedOperationException e) {
			result = Result.UnsupportedOperation;
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIter", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs ListIterator's hasPrevious() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to hasPrevious()
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterHasPrevious(ListIterator<Integer> iterator, Result expectedResult) {
		Result result;
		try {
			if (iterator.hasPrevious()) {
				result = Result.True;
			} else {
				result = Result.False;
			}
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterHasPrevious", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs ListIterator previous() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to hasPrevious()
	 * @param expectedValue the Integer expected from next() or null if an exception is expected
	 * @param expectedResult MatchingValue or expected exception
	 * @return test success
	 */
	private boolean testListIterPrevious(ListIterator<Integer> iterator, Integer expectedValue, Result expectedResult) {
		Result result;
		try {
			Integer retVal = iterator.previous();
			if (retVal.equals(expectedValue)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterPrevious", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs ListIterator add() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to add()
	 * @param element new Integer for insertion
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterAdd(ListIterator<Integer> iterator, Integer element, Result expectedResult) {
		Result result;
		try {
			iterator.add(element);
			result = Result.NoException;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterAdd", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs ListIterator set() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to set()
	 * @param element replacement Integer for last returned element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterSet(ListIterator<Integer> iterator, Integer element, Result expectedResult) {
		Result result;
		try {
			iterator.set(element);
			result = Result.NoException;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (IllegalStateException e) {
			result = Result.IllegalState;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterSet", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs ListIterator nextIndex() and checks result against expected Result
	 * @param iterator already positioned for the call to nextIndex()
	 * @param expectedIndex
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterNextIndex(ListIterator<Integer> iterator, int expectedIndex, Result expectedResult) {
		Result result;
		try {
			int idx = iterator.nextIndex();
			if (idx == expectedIndex) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterNextIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs ListIterator previousIndex() and checks result against expected Result
	 * @param iterator already positioned for the call to previousIndex()
	 * @param expectedIndex
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterPreviousIndex(ListIterator<Integer> iterator, int expectedIndex, Result expectedResult) {
		Result result;
		try {
			int idx = iterator.previousIndex();
			if (idx == expectedIndex) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterPreviousIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs listIterator() method twice on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterConcurrent(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			ListIterator<Integer> it1 = list.listIterator();
			@SuppressWarnings("unused")
			ListIterator<Integer> it2 = list.listIterator();
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterConcurrent", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs listIterator(index) method twice on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param index1
	 * @param index2
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterConcurrent(IndexedUnsortedList<Integer> list, int index1, int index2, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			ListIterator<Integer> it1 = list.listIterator(index1);
			@SuppressWarnings("unused")
			ListIterator<Integer> it2 = list.listIterator(index2);
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterConcurrent", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	//////////////////////////////////////////////////////////
	//XXX HELPER METHODS FOR TESTING LISTITERATORS
	//Note: You can create other similar helpers if you want
	//  something slightly different.
	//////////////////////////////////////////////////////////
	
	/** Helper for testing ListIterators. Return a ListIterator that has been advanced numCallsToNext times.
	 * @param iterator
	 * @param numCallsToNext
	 * @return same iterator after numCallsToNext
	 */
	private ListIterator<Integer> listIterAfterNext(ListIterator<Integer> iterator, int numCallsToNext) {
		for (int i = 0; i < numCallsToNext; i++) {
			iterator.next();
		}
		return iterator;
	}

	/** Helper for testing ListIterators. Return a ListIterator that has been backed up numCallsToPrevious times.
	 * @param iterator
	 * @param numCallsToPrevious
	 * @return same iterator after numCallsToPrevious
	 */
	private ListIterator<Integer> listIterAfterPrevious(ListIterator<Integer> iterator, int numCallsToPrevious) {
		for (int i = 0; i < numCallsToPrevious; i++) {
			iterator.previous();
		}
		return iterator;
	}

	/** Helper for testing ListIterators. Return a ListIterator that has had remove() called once.
	 * @param iterator
	 * @return same Iterator following a call to remove()
	 */
	private ListIterator<Integer> listIterAfterRemove(ListIterator<Integer> iterator) {
		iterator.remove();
		return iterator;
	}

	////////////////////////////////////////////////////////
	// XXX Iterator Concurrency Tests
	// Can simply use as given. Don't need to add more.
	////////////////////////////////////////////////////////

	/** run Iterator concurrency tests */
	private void test_IterConcurrency() {
		System.out.println("\nIterator Concurrency Tests\n");		
		try {
			printTest("emptyList_testConcurrentIter", testIterConcurrent(newList(), Result.NoException));
			IndexedUnsortedList<Integer> list = newList();
			Iterator<Integer> it1 = list.iterator();
			Iterator<Integer> it2 = list.iterator();
			it1.hasNext();
			printTest("emptyList_iter1HasNext_testIter2HasNext", testIterHasNext(it2, Result.False));
			list = newList();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.hasNext();
			printTest("emptyList_iter1HasNext_testIter2Next", testIterNext(it2, null, Result.NoSuchElement));
			list = newList();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.hasNext();
			printTest("emptyList_iter1HasNext_testIter2Remove", testIterRemove(it2, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.hasNext();
			printTest("A_iter1HasNext_testIter2HasNext", testIterHasNext(it2, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.hasNext();
			printTest("A_iter1HasNext_testIter2Next", testIterNext(it2, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.hasNext();
			printTest("A_iter1HasNext_testIter2Remove", testIterRemove(it2, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			printTest("A_iter1Next_testIter2HasNext", testIterHasNext(it2, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			printTest("A_iter1Next_testIter2Next", testIterNext(it2, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			printTest("A_iter1Next_testIter2Remove", testIterRemove(it2, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			it1.remove();
			printTest("A_iter1NextRemove_testIter2HasNext", testIterHasNext(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			it1.remove();
			printTest("A_iter1NextRemove_testIter2Next", testIterNext(it2, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			it1.remove();
			printTest("A_iter1NextRemove_testIter2Remove", testIterRemove(it2, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeFirst();
			printTest("A_removeFirst_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeFirst();
			printTest("A_removeFirst_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeFirst();
			printTest("A_removeLast_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeLast();
			printTest("A_removeLast_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeLast();
			printTest("A_removeLast_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeLast();
			printTest("A_removeLast_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));			

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(ELEMENT_A);
			printTest("A_remove_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(ELEMENT_A);
			printTest("A_remove_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.first();
			printTest("A_first_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.first();
			printTest("A_first_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.first();
			printTest("A_first_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.last();
			printTest("A_last_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.last();
			printTest("A_last_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.last();
			printTest("A_last_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.isEmpty();
			printTest("A_isEmpty_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.isEmpty();
			printTest("A_isEmpty_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.isEmpty();
			printTest("A_isEmpty_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.size();
			printTest("A_size_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.size();
			printTest("A_size_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.size();
			printTest("A_size_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.toString();
			printTest("A_toString_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.toString();
			printTest("A_toString_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.toString();
			printTest("A_toString_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testIterNextConcurrent", testIterNext(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(0,ELEMENT_B);
			printTest("A_add0B_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(0,ELEMENT_B);
			printTest("A_add0B_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(0,ELEMENT_B);
			printTest("A_add0B_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.set(0,ELEMENT_B);
			printTest("A_set0B_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.set(0,ELEMENT_B);
			printTest("A_set0B_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.set(0,ELEMENT_B);
			printTest("A_set0B_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.get(0);
			printTest("A_get0_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.get(0);
			printTest("A_get0_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.get(0);
			printTest("A_get_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(0);
			printTest("A_remove0_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(0);
			printTest("A_remove0_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(0);
			printTest("A_remove0_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", "test_IteratorConcurrency");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

		////////////////////////////////////////////////////////
	// XXX ListIterator Concurrency Tests
	////////////////////////////////////////////////////////

	/** run ListIterator concurrency tests */
	private void test_ListIterConcurrency() {
		System.out.println("\nListIterator Concurrency Tests\n");
		try {
			printTest("emptyList_testConcurrentListIter", testListIterConcurrent(newList(), Result.NoException));
			printTest("emptyList_testConcurrentListIter00", testListIterConcurrent(newList(), 0, 0, Result.NoException));

			IndexedUnsortedList<Integer> list = newList();
			ListIterator<Integer> it1 = list.listIterator();
			ListIterator<Integer> it2 = list.listIterator();
			it1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2HasNext", testIterHasNext(it2, Result.False));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2Next", testIterNext(it2, null, Result.NoSuchElement));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2Remove", testIterRemove(it2, Result.IllegalState));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2HasPrevious", testListIterHasPrevious(it2, Result.False));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2Previous", testListIterPrevious(it2, null, Result.NoSuchElement));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2AddA", testListIterAdd(it2, ELEMENT_A, Result.NoException));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2SetA", testListIterSet(it2, ELEMENT_A, Result.IllegalState));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2NextIndex", testListIterNextIndex(it2, 0, Result.MatchingValue));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2PreviousIndex", testListIterPreviousIndex(it2, -1, Result.MatchingValue));

			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2HasNext", testIterHasNext(it2, Result.False));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2Next", testIterNext(it2, null, Result.NoSuchElement));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2Remove", testIterRemove(it2, Result.IllegalState));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2HasPrevious", testListIterHasPrevious(it2, Result.False));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2Previous", testListIterPrevious(it2, null, Result.NoSuchElement));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2AddA", testListIterAdd(it2, ELEMENT_A, Result.NoException));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2SetA", testListIterSet(it2, ELEMENT_A, Result.IllegalState));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2NextIndex", testListIterNextIndex(it2, 0, Result.MatchingValue));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2PreviousIndex", testListIterPreviousIndex(it2, -1, Result.MatchingValue));

			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2HasNext", testIterHasNext(it2, Result.False));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2Next", testIterNext(it2, null, Result.NoSuchElement));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2Remove", testIterRemove(it2, Result.IllegalState));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2HasPrevious", testListIterHasPrevious(it2, Result.False));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2Previous", testListIterPrevious(it2, null, Result.NoSuchElement));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2AddA", testListIterAdd(it2, ELEMENT_A, Result.NoException));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2SetA", testListIterSet(it2, ELEMENT_A, Result.IllegalState));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2NextIndex", testListIterNextIndex(it2, 0, Result.MatchingValue));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2PreviousIndex", testListIterPreviousIndex(it2, -1, Result.MatchingValue));

			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2HasNext", testIterHasNext(it2, Result.False));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2Next", testIterNext(it2, null, Result.NoSuchElement));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2Remove", testIterRemove(it2, Result.IllegalState));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2HasPrevious", testListIterHasPrevious(it2, Result.False));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2Previous", testListIterPrevious(it2, null, Result.NoSuchElement));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2AddA", testListIterAdd(it2, ELEMENT_A, Result.NoException));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2SetA", testListIterSet(it2, ELEMENT_A, Result.IllegalState));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2NextIndex", testListIterNextIndex(it2, 0, Result.MatchingValue));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2PreviousIndex", testListIterPreviousIndex(it2, -1, Result.MatchingValue));

			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2HasNext", testIterHasNext(it2, Result.ConcurrentModification));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2Next", testIterNext(it2, null, Result.ConcurrentModification));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2Remove", testIterRemove(it2, Result.ConcurrentModification));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2HasPrevious", testListIterHasPrevious(it2, Result.ConcurrentModification));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2Previous", testListIterPrevious(it2, null, Result.ConcurrentModification));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2AddB", testListIterAdd(it2, ELEMENT_B, Result.ConcurrentModification));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2SetA", testListIterSet(it2, ELEMENT_A, Result.ConcurrentModification));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2NextIndex", testListIterNextIndex(it2, 0, Result.ConcurrentModification));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2PreviousIndex", testListIterPreviousIndex(it2, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			printTest("A_ListIter1Next_testListIter2HasNext", testIterHasNext(it2, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			printTest("A_ListIter1Next_testListIter2Next", testIterNext(it2, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			printTest("A_ListIter1Next_testListIter2Remove", testIterRemove(it2, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			printTest("A_ListIter1Next_testListIter2HasPrevious", testListIterHasPrevious(it2, Result.False));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			printTest("A_ListIter1Next_testListIter2Previous", testListIterPrevious(it2, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			printTest("A_ListIter1Next_testListIter2AddB", testListIterAdd(it2, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			printTest("A_ListIter1Next_testListIter2SetB", testListIterSet(it2, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			printTest("A_ListIter1Next_testListIter2NextIndex", testListIterNextIndex(it2, 0, Result.MatchingValue));
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			printTest("A_ListIter1Next_testListIter2PreviousIndex", testListIterPreviousIndex(it2, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			printTest("A_ListIter1Previous_testListIter2HasNext", testIterHasNext(it2, Result.True));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			printTest("A_ListIter1Previous_testListIter2Next", testIterNext(it2, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			printTest("A_ListIter1Previous_testListIter2Remove", testIterRemove(it2, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			printTest("A_ListIter1Previous_testListIter2HasPrevious", testListIterHasPrevious(it2, Result.False));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			printTest("A_ListIter1Previous_testListIter2Previous", testListIterPrevious(it2, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			printTest("A_ListIter1Previous_testListIter2AddB", testListIterAdd(it2, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			printTest("A_ListIter1Previous_testListIter2SetB", testListIterSet(it2, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			printTest("A_ListIter1Previous_testListIter2NextIndex", testListIterNextIndex(it2, 0, Result.MatchingValue));
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			printTest("A_ListIter1Previous_testListIter2PreviousIndex", testListIterPreviousIndex(it2, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.remove();
			printTest("A_ListIter1NextRemove_testListIter2HasNext", testIterHasNext(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.remove();
			printTest("A_ListIter1NextRemove_testListIter2Next", testIterNext(it2, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.remove();
			printTest("A_ListIter1NextRemove_testListIter2Remove", testIterRemove(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.remove();
			printTest("A_ListIter1NextRemove_testListIter2HasPrevious", testListIterHasPrevious(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.remove();
			printTest("A_ListIter1NextRemove_testListIter2Previous", testListIterPrevious(it2, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.remove();
			printTest("A_ListIter1NextRemove_testListIter2AddB", testListIterAdd(it2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.remove();
			printTest("A_ListIter1NextRemove_testListIter2SetB", testListIterSet(it2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.remove();
			printTest("A_ListIter1NextRemove_testListIter2NextIndex", testListIterNextIndex(it2, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.remove();
			printTest("A_ListIter1NextRemove_testListIter2PreviousIndex", testListIterPreviousIndex(it2, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2HasNext", testIterHasNext(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2Next", testIterNext(it2, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2Remove", testIterRemove(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2HasPrevious", testListIterHasPrevious(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2Previous", testListIterPrevious(it2, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2AddB", testListIterAdd(it2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2SetB", testListIterSet(it2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2NextIndex", testListIterNextIndex(it2, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2PreviousIndex", testListIterPreviousIndex(it2, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2HasNext", testIterHasNext(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2Next", testIterNext(it2, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2Remove", testIterRemove(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2HasPrevious", testListIterHasPrevious(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2Previous", testListIterPrevious(it2, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2AddB", testListIterAdd(it2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2SetB", testListIterSet(it2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2NextIndex", testListIterNextIndex(it2, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2PreviousIndex", testListIterPreviousIndex(it2, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2HasNext", testIterHasNext(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2Next", testIterNext(it2, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2Remove", testIterRemove(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2HasPrevious", testListIterHasPrevious(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2Previous", testListIterPrevious(it2, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2AddB", testListIterAdd(it2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2SetB", testListIterSet(it2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2NextIndex", testListIterNextIndex(it2, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2PreviousIndex", testListIterPreviousIndex(it2, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2HasNext", testIterHasNext(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2Next", testIterNext(it2, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2Remove", testIterRemove(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2HasPrevious", testListIterHasPrevious(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2Previous", testListIterPrevious(it2, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2AddB", testListIterAdd(it2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2SetB", testListIterSet(it2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2NextIndex", testListIterNextIndex(it2, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2PreviousIndex", testListIterPreviousIndex(it2, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testListIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeLast_testListIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testListIterPreviousConcurrent", testListIterPrevious(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterPreviousConcurrent", testListIterPrevious(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testListIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeLast_testListIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testListIterPreviousConcurrent", testListIterPrevious(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.False));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.False));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testListIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testListIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.False));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.False));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.False));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.False));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterNextConcurrent", testIterNext(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterNextConcurrent", testIterNext(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(0,ELEMENT_B);
			printTest("A_add0B_testListIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(0,ELEMENT_B);
			printTest("A_add0B_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(0,ELEMENT_B);
			printTest("A_add0B_testListIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.set(0,ELEMENT_B);
			printTest("A_set0B_testListIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.set(0,ELEMENT_B);
			printTest("A_set0B_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.set(0,ELEMENT_B);
			printTest("A_set0B_testListIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.set(0, ELEMENT_B);
			printTest("A_set0B_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.set(0, ELEMENT_B);
			printTest("A_set0B_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.set(0, ELEMENT_B);
			printTest("A_set0B_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.set(0, ELEMENT_B);
			printTest("A_set0B_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.set(0, ELEMENT_B);
			printTest("A_set0B_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.set(0, ELEMENT_B);
			printTest("A_set0B_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testListIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testListIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testListIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.get(0);
			printTest("A_get_testListIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.False));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testListIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testListIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.False));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testListIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testListIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.ConcurrentModification));
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", "test_ListIterConcurrency");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}
}// end class IndexedUnsortedListTester

/** Interface for builder method Lambda references used above */
interface Scenario<T> {
	IndexedUnsortedList<T> build();
}

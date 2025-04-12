****************
* Double-Linked List (DLL)
* CS221-003
* 4/11/2025
* Gage Munt
**************** 

**OVERVIEW:**

 This program implements a doubly linked list `IUDoubleLinkedList` that stores generic objects and supports indexed operations and iteration. A separate `ListTester` verifies that the list behaves correctly by running a suite of functional tests.


**INCLUDED FILES:**

 * IUDoubleLinkedList.java - source file: creates double-linked lists using nodes with iterator support
 * Node.java - source file: creates node objects for single and double-linked lists to implement
 * IndexedUnsortedList.java - interface to base all lists off of (including IUDoubleLinkedList)
 * ListTester.java - driver/test class to verify list functionality
 * README.md - to document the purpose behind the program and its creation


**COMPILING AND RUNNING:**

 From the directory containing all source files, compile the
 driver/test class (and all dependencies) with the command:

 `$ javac ListTester.java`

 Run the compiled class file with the command:

 `$ java ListTester [-a] [-s] [-m]`
  
  * `-a` shows output of passed tests as well as failures 
  * `-s` hides toString() output
  * `-m` hides section summaries after each case

 *Console output will give the results after the program finishes.*


**PROGRAM DESIGN AND IMPORTANT CONCEPTS:**

 The IUDoubleLinkedList class is built upon both the Node class and the IndexedUnsortedList interface. The Node class provides the fundamental building block for the list, where each node stores an element and maintains references to both its previous and next neighbors. This double-linked structure allows for efficient forward and backward traversal of the list. Meanwhile, the IndexedUnsortedList interface defines a contract for expected behaviors such as add, remove, get, set, isEmpty, and more. By implementing this interface, IUDoubleLinkedList guarantees that it supports all required operations.

 A key component of the design is the internal list iterator provided by IUDoubleLinkedList, which supports bidirectional traversal and core modification operations (`add`, `remove`, `set`). This iterator plays a central role in my implementation. After writing the basic methods to pass early tests, I focused on building a complete and robust iterator. Once all iterator tests passed, I refactored most of the list’s core methods to delegate functionality to the iterator. This reduced code duplication, improved maintainability, and ensured that list behavior is consistent across different entry points.

 The primary benefit of using a double-linked list is the ability to traverse in both directions, improving performance on operations such as `removeLast()`, which becomes O(1) instead of O(n) as in a single-linked list. In my implementation, I further optimized traversal by dynamically deciding whether to begin iteration from the head or tail based on the provided starting index.

 Clarity was a central focus throughout my design. I gave meaningful names to test cases in ListTester to make debugging and test tracing straightforward. In the implementation itself, I preferred conditions and logic that would be immediately understandable to another developer. For example, in the `isEmpty()` method, I chose to check `size == 0` instead of `head == null`, as it better reflects the external interface’s understanding and intention. I also incorpate a helper method `checkForConcurrentModification()` to further reduce code duplication in my listIterator implementation. 

 While this design brings several advantages, including: modularity, clarity, and efficient traversal, it also introduces a trade-off. Because many core list methods rely on the iterator’s behavior, any bug or change in IUDLLIterator can spread widely, affecting seemingly unrelated functionality. 

 Overall, I prioritized code reuse, readability, and adherence to the interface's contract while still maintaining performance and avoiding index-based operations altogether.

**TESTING:**

 Testing was a critical part of this assignment, especially since we were responsible for building our own testing class, `ListTester.java`. Before implementing any part of the list, I first created a core set of fundamental test cases. These served as a baseline to verify basic functionality. I then developed my list class incrementally, starting with the constructor and gradually working up to more complex methods. This allowed me to employ a fail-fast development strategy; I could immediately test and validate each method right after implementation.

 In total, I achieved a total of 6,845 tests, covering roughly 58 out of the 82 provided testing scenarios. When deciding which scenarios to include, I considered overlap in method coverage to ensure efficient and complete testing. I also layered tests by calling scenarios within others to verify complex interactions between list operations.

 This approach was especially helpful when working with the IUDLLIterator. It helped me identify that my `previous()` and `next()` methods were missing concurrent modification checks, leading to subtle bugs when multiple iterators interacted with the list. I also caught edge cases, like failing to handle empty lists in the `add()` method, and corrected them immediately based on test results.

 One particularly tricky issue arose when optimizing the iterator constructor to dynamically traverse from either the head or the tail based on the starting index. I initially struggled with an off-by-one error and loop misconfiguration when starting from the tail. Focused tests helped pinpoint the problem and verify the final fix.

 My program is designed to handle bad input robustly. For example, methods that access specific indexes include bounds checking, and the iterator enforces fail-fast behavior by detecting concurrent modifications. Additionally, the program will throw no such element exceptions if the requested element cannot be found in the list. These guardrails ensure the program is not easily broken by invalid usage.

 At this time, there are no known bugs or issues remaining in the program. All methods behave as expected under both normal and edge-case conditions.


**DISCUSSION:**
 
 Overall, I really enjoyed this project and the challenges it brought. At first, testing was one of the harder skills to develop, especially writing effective tests that could catch subtle bugs. But over time, it really clicked for me. I started to see how building a strong test suite was just as important as building the list itself. It was especially rewarding to see tests pass in real time as I completed new methods.

 One of the biggest challenges for me was working with nodes and iterators. While I feel much more confident with nodes now, especially how to insert between two existing nodes and handle all the next and previous node adjustments, the iterator still trips me up a bit. I kept forgetting that the iterator sits just to the *left* of the current index and doesn't explicitly point to an element, which caused confusion when implementing methods like `previous()` or when moving backward from the tail. In fact, one bug in my reverse traversal logic came from this exact misunderstanding; my loop would overshoot or undershoot the intended node because I misunderstood the starting position.

 I also ran into a weird infinite loop in my `add(index, item)` method. It happened when I tried to replicate some of the logic from my second list iterator constructor. After stepping back and re-reading my own code, I realized I was overcomplicating it, I didn’t need to recreate the traversal logic when I already had a working iterator constructor. Sometimes, the best fix is to simplify.

 For research, I leaned on the JavaDocs for LinkedList and ListIterator to get a better idea of how the methods should behave. I also found that most online resources didn’t go deep enough into custom iterators, so I relied a lot on in-class explanations and sometimes AI-based tools to explain subtle edge cases (especially those involving the tail or concurrent modification).

 All in all, I'm proud of how much I learned, even if some parts still feel fuzzy. I'm definitely leaving this project with a better grasp on linked data structures and the power of iterators, and a much greater appreciation for thorough testing.
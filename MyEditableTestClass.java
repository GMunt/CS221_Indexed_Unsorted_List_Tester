public class MyEditableTestClass {
    public static void main(String args[]) {
        IUArrayList testArray = new IUArrayList();

        testArray.add("a");
        testArray.add("b");
        testArray.add("c");

        System.out.println(testArray.first());
        System.out.println(testArray.last());

        System.out.println(testArray.toString());

        testArray.addAfter("d", "b");

        System.out.println(testArray.toString());

        testArray.remove("b");
        System.out.println(testArray.toString());

        testArray.remove(1);
        System.out.println(testArray.toString());

    }
}

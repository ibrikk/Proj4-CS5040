import student.TestCase;

/**
 * Test the CommandProcessor class
 *
 * @author Ibrahim Khalilov - ibrahimk
 * @version 2024-04-25
 */
public class CommandProcessorTest extends TestCase {

    /**
     * Initializes the CommandProcessor
     */

    private CommandProcessor cp = new CommandProcessor(16, 16);
    private Database db = new Database(128, 16);

    /**
     * Sets up the tests that follow. In general, used for initialization
     */

    public void setUp() {
// Nothing here
    }


    /**
     * Check the createSeminar method
     */
    public void testCreateSeminar() {
        String[][] arrayOfLinesArray = {
            /** Line 1 */
            { "insert", "1" }, {
                /** Line 2 */
                "Overview of HCI Research at VT" },
            /** Line 3 */
            { "0610051600", "90", "10", "10", "45" },
            /** Line 4 */
            { "HCI", "Computer_Science", "VT", "Virginia_Tech" },
            /** Line 5 */
            { "This seminar will present an overview of HCI research at VT" } };

        String[] keywords = { "HCI", "Computer_Science", "VT",
            "Virginia_Tech" };

        Seminar actual = cp.getCreateSeminar(arrayOfLinesArray);

        assertEquals(1, actual.getId());
        assertEquals("Overview of HCI Research at VT", actual.getTitle());
        assertEquals("0610051600", actual.getDate());
        assertEquals(90, actual.getLength());
        assertEquals(10, actual.getX());
        assertEquals(10, actual.getY());
        assertEquals(45, actual.getCost());
        assertEquals(keywords[0], actual.getKeywords()[0]);
        assertEquals(keywords[1], actual.getKeywords()[1]);
        assertEquals(keywords[2], actual.getKeywords()[2]);
        assertEquals(keywords[3], actual.getKeywords()[3]);
        assertEquals(
            "This seminar will present an overview of HCI research at VT",
            actual.getDescription());

    }

// /**
// * Check the createSeminar method
// */
//
// public void testCreateSeminar2() throws NoSuchFieldException {
// String[][] arrayOfLinesArray = {
// /** Line 1 */
// { "insert", "1" }, {
// /** Line 2 */
// "T1" },
// /** Line 3 */
// { "0610051600", "90", "10", "10", "45" },
// /** Line 4 */
// { "HCI" },
// /** Line 5 */
// { "VT" } };
//
// Seminar actual = cp.getCreateSeminar(arrayOfLinesArray);
//
// String[][] arrayOfLinesArray2 = {
// /** Line 1 */
// { "insert", "2" }, {
// /** Line 2 */
// "T1" },
// /** Line 3 */
// { "0610051600", "90", "10", "10", "45" },
// /** Line 4 */
// { "HCI" },
// /** Line 5 */
// { "VT" } };
//
// Seminar actual2 = cp.getCreateSeminar(arrayOfLinesArray2);
//
// String[][] arrayOfLinesArray3 = {
// /** Line 1 */
// { "insert", "3" }, {
// /** Line 2 */
// "T1" },
// /** Line 3 */
// { "0610051600", "90", "10", "10", "45" },
// /** Line 4 */
// { "HCI" },
// /** Line 5 */
// { "VT" } };
//
// Seminar actual3 = cp.getCreateSeminar(arrayOfLinesArray3);
//
// db.insert(actual);
// db.print("blocks");
// db.insert(actual2);
// db.print("blocks");
// db.insert(actual3);
// db.print("blocks");
//
// db.delete(3);
// db.delete(2);
// db.delete(1);
// db.print("blocks");
//
// }
}

// -----TEST 7.2 -- memManagerMerge -----
//
// Successfully inserted record with ID 1
// ID: 1, Title: T1
// Date: 0610051600, Length: 90, X: 10, Y: 10, Cost: 45
// Description: D1
// Keywords: K1
// Size: 50
// Successfully inserted record with ID 2
// ID: 2, Title: T1
// Date: 0610051600, Length: 90, X: 10, Y: 10, Cost: 45
// Description: D1
// Keywords: K1
// Size: 50
// Successfully inserted record with ID 3
// ID: 3, Title: T1
// Date: 0610051600, Length: 90, X: 10, Y: 10, Cost: 45
// Description: D1
// Keywords: K1
// Size: 50
// Freeblock List:
// 64: 192
// Record with ID 3 successfully deleted from the database
// Freeblock List:
// 128: 128
// Record with ID 2 successfully deleted from the database
// Freeblock List:
// 64: 64
// 128: 128

// -----TEST 7.3 -- memManagerFullEmpty -----
//
// Successfully inserted record with ID 1
// ID: 1, Title: T1
// Date: 0610051600, Length: 90, X: 10, Y: 10, Cost: 45
// Description: D1
// Keywords: K1
// Size: 50
// Successfully inserted record with ID 2
// ID: 2, Title: T1
// Date: 0610051600, Length: 90, X: 10, Y: 10, Cost: 45
// Description: D1
// Keywords: K1
// Size: 50
// Freeblock List:
// There are no freeblocks in the memory pool
// Memory pool expanded to 256 bytes
// Successfully inserted record with ID 3
// ID: 3, Title: T1
// Date: 0610051600, Length: 90, X: 10, Y: 10, Cost: 45
// Description: D1
// Keywords: K1
// Size: 50
// Freeblock List:
// 64: 192
// Record with ID 3 successfully deleted from the database
// Record with ID 2 successfully deleted from the database
// Record with ID 1 successfully deleted from the database
// Freeblock List:
// 64: 0 64
// 128: 128

// -----TEST 9.0--memManagerCascadeSimple-----

//
// Successfully inserted
// record with ID 1 ID:1, Title: T1
// Date:0610051600,Length:90,X:10,Y:10,Cost:45 Description:
// D1
// Keywords:
// K1
// Size:50
// Freeblock List:
// 64:64
// 128:128
// 256:256
// 512: 512

// My output
// init 128 size

// Testcase: test_memManagerFullEmpty took 0.043 sec
// FAILED
// junit.framework.ComparisonFailure: expected:<...base
// freeblock list
// [256 0]
// > but was:<...base
// freeblock list
// [64 0 64
// 128 128]
// >
// at junit.framework.Assert.assertEquals(Assert.java:100)
// at student.TestCase.assertEquals(TestCase.java:799)
// at student.TestCase.assertFuzzyEquals(TestCase.java:530)
// at student.TestCase.assertFuzzyEquals(TestCase.java:502)
// at wc_SemManagerTest.checkOutputString(wc_SemManagerTest.java:385)
// at wc_SemManagerTest.runTestMethod(wc_SemManagerTest.java:345)
// at wc_SemManagerTest.test_memManagerFullEmpty(wc_SemManagerTest.java:204)
// at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native
// Method)
// at
// java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
// at
// java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)

// The exoected outpur
// Successfully inserted record with ID 1
// ID: 1, Title: T1
// Date: 0610051600, Length: 90, X: 10, Y: 10, Cost: 45
// Description: D1
// Keywords: K1
// Size: 50
// Successfully inserted record with ID 2
// ID: 2, Title: T1
// Date: 0610051600, Length: 90, X: 10, Y: 10, Cost: 45
// Description: D1
// Keywords: K1
// Size: 50
// Freeblock List:
// There are no freeblocks in the memory pool
// Memory pool expanded to 256 bytes
// Successfully inserted record with ID 3
// ID: 3, Title: T1
// Date: 0610051600, Length: 90, X: 10, Y: 10, Cost: 45
// Description: D1
// Keywords: K1
// Size: 50
// Freeblock List:
// 64: 192
// Record with ID 3 successfully deleted from the database
// Record with ID 2 successfully deleted from the database
// Record with ID 1 successfully deleted from the database
// Freeblock List:
// 256: 0

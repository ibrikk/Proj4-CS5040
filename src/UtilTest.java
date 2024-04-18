import student.TestCase;
import org.junit.Test;

/**
 * Test the CommandProcessor class
 *
 * @author Ibrahim Khalilov - ibrahimk
 * @version 2023-09-06
 */

public class UtilTest extends TestCase {

    /**
     * Checks if the array doubled in size
     */

    @Test
    public void testDoubleSize() {
        Integer[] doubleSizeArr = Util.doubleSize(new Integer[2]);
        assertEquals(doubleSizeArr.length, 4);
        Integer[] doubleSizeArr2 = Util.doubleSize(new Integer[1000]);
        assertEquals(doubleSizeArr2.length, 2000);
    }


    /**
     * Checks if the output was printed to the console
     */

    @Test
    public void testPrint() {
        String[] arr = { "foo", "bar", "baz" };

        String expected = "foo, bar, baz" + System.lineSeparator();
        systemOut().clearHistory();
        Util.print(arr);

        assertEquals(systemOut().getHistory(), expected);

    }


    /** Checking if initial size is power of two */

    @Test
    public void testPowerOfTwo() {
        int powerOfTwoSixteen = 16;
        int powerOfTwoTwo = 2;
        int powerOfTwoFive = 5;
        int powerOfTwoSix = 6;
        int powerOfTwoNegativeSix = -6;
        int powerOfTwoZero = 0;
        assertTrue(Util.isPowerOfTwo(powerOfTwoSixteen));
        assertTrue(Util.isPowerOfTwo(powerOfTwoTwo));
        assertFalse(Util.isPowerOfTwo(powerOfTwoFive));
        assertFalse(Util.isPowerOfTwo(powerOfTwoSix));
        assertFalse(Util.isPowerOfTwo(powerOfTwoNegativeSix));
        assertFalse(Util.isPowerOfTwo(powerOfTwoZero));
    }


    /**
     * Test the print method to ensure it correctly formats and prints all
     * details of a seminar.
     */
    @Test
    public void testPrintSeminar() {
        String[] keywords = { "AI", "Machine Learning", "Deep Learning" };
        short x = 50;
        short y = 20;
        Seminar seminar = new Seminar(1, "Intro to AI", "2021-04-15", 120, x, y,
            200, keywords,
            "A comprehensive introduction to artificial intelligence.");

        String expectedOutput = "ID: 1, Title: Intro to AI\n"
            + "Date: 2021-04-15, Length: 120, X: 50, Y: 20, Cost: 200\n"
            + "Description: A comprehensive introduction to artificial intelligence.\n"
            + "Keywords: AI, Machine Learning, Deep Learning";

        systemOut().clearHistory();
        Util.print(seminar);
        assertFuzzyEquals(expectedOutput, systemOut().getHistory());
    }
}

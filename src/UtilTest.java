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
}

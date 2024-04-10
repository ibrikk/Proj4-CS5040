import student.TestCase;

/**
 * Test the CommandProcessor class
 *
 * @author Ibrahim Khalilov - ibrahimk
 * @version 2023-09-06
 */
public class CommandProcessorTest extends TestCase {

    /**
     * Initializes the CommandProcessor
     */

    private CommandProcessor cp = new CommandProcessor(16, 16);

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
}

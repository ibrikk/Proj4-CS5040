import org.junit.Before;
import student.TestCase;

/**
 * Test the Database class
 *
 * @author Ibrahim Khalilov - ibrahimk
 * @version 2024-04-25
 */
public class DatabaseTest extends TestCase {

    private Database db;

    /**
     * Sets up the tests that follow. In general, used for initialization
     */
    @Before
    public void setUp() {
        db = new Database(128, 16);
    }


    /**
     * Test delete
     */
    public void testDelete() {
        db.delete(-1);
    }


    /**
     * Test find
     * 
     * @throws Exception
     */
    public void testFind() throws Exception {
        String[] keywords = { "Good", "Bad", "Ugly" };
        Seminar mysem = new Seminar(1729, "Seminar Title", "2405231000", 75,
            (short)15, (short)33, 125, keywords, "This is a great seminar");
        db.insert(mysem);
        systemOut().clearHistory();
        db.find(1729);
        String expected = "Found record with ID 1729:\n"
            + "ID: 1729, Title: Seminar Title\n"
            + "Date: 2405231000, Length: 75, X: 15, Y: 33, Cost: 125\n"
            + "Description: This is a great seminar\n"
            + "Keywords: Good, Bad, Ugly";
        assertFuzzyEquals(expected, systemOut().getHistory());
    }

}

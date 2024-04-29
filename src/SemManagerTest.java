import org.junit.Before;
import student.TestCase;

/**
 * Test the CommandProcessor class
 *
 * @author Ibrahim Khalilov - ibrahimk
 * @version 2024-04-20
 */

public class SemManagerTest extends TestCase {

    /**
     * Before annotation runs before each test
     * 
     * @throws Exception
     */

    @Before
    public void setUp() throws Exception {
    }


    /**
     * 
     * Full parser test
     * 
     * @throws Exception
     * 
     */

    public void testparserfull() throws Exception {

        String[] args = new String[3];

        args[0] = "512";

        args[1] = "4";

        args[2] = "P1Sample_inputX.txt";

        systemOut().clearHistory();

        SemManager.main(args);

        String output = systemOut().getHistory();

        String referenceOutput = SemManager.readFile("P1Sample_outputX.txt");

        assertFuzzyEquals(referenceOutput, output);

    }

}

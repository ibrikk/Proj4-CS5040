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

// args[0] = "1024";
// Should not be expanding the second time
        args[0] = "512";

        args[1] = "4";

        args[2] = "P1Sample_inputX.txt";
// args[2] = "Simple_insert_in.txt";

        systemOut().clearHistory();

        SemManager.main(args);

        String output = systemOut().getHistory();

        String referenceOutput = SemManager.readFile("P1Sample_outputX.txt");
// String referenceOutput = SemManager.readFile("Simple_insert_out.txt");

        assertFuzzyEquals(referenceOutput, output);

    }

// pool 32
// table 4
// expansion-decreases the memory pool

    // expanded earlier than supposed to
// 128 -> 512 -> 256

}

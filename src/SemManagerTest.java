import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Before;
import student.TestCase;

/**
 * Test the CommandProcessor class
 *
 * @author Ibrahim Khalilov - ibrahimk
 * @version 2023-09-06
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
     * @throws IOException
     * 
     */

    public void testparserfull() throws IOException {

        String[] args = new String[3];

        args[0] = "256";

        args[1] = "16";

        args[2] = "P1Hash_input.txt";

        systemOut().clearHistory();

        SemManager.main(args);

        String output = systemOut().getHistory();

        String referenceOutput = SemManager.readFile("P1Hash_output.txt");

        assertFuzzyEquals(referenceOutput, output);

    }

}

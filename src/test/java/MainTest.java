import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MainTest {

    public MainClass mainClass;

    @Before
    public void setUp() {

    }

    @Test
    public void enqueueOperation() {

    }


    public static int getRamdomNo() {
        return ThreadLocalRandom.current().nextInt(1,6);
    }
    public static char getRandomChar(){
        Random r = new Random();
        String alphabet = "ABCDE";
        return alphabet.charAt(r.nextInt(alphabet.length()));
    }

}

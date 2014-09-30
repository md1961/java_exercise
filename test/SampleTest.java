package test;

import static org.junit.Assert.assertEquals;
 
import org.junit.Test;
import org.junit.runner.JUnitCore;
 

public class SampleTest {
 
    public static void main(String[] args) {
        JUnitCore.main(SampleTest.class.getName());
    }
 
    @Test
    public void testOne() {
        String str = "りんご";
        assertEquals(str.charAt(0), 'り');
    }
}

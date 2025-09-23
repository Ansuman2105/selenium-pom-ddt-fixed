package tests;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SanityTest {

    @Test
    public void testOne() {
        System.out.println("Hello, TestNG is running fine!");
        Assert.assertTrue(true); // should PASS
    }
}

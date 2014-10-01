package test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;
import org.junit.runner.JUnitCore;

import finished.GarapagosMobileTyper;


public class GarapagosMobileTyperTest {

    public static void main(String[] args) {
        JUnitCore.main(GarapagosMobileTyperTest.class.getName());
    }

	private final GarapagosMobileTyper gmTyper = new GarapagosMobileTyper();

	@Test
	public void helloWorld() {
		assertThat(gmTyper.type("4444433555E5556661111999996667775553E"), is("Hello_World"));
	}

	@Test
	public void paizaSrank() {
		assertThat(gmTyper.type("7244499992222222277777777E77726655E"), is("paizaSrank"));
	}
}

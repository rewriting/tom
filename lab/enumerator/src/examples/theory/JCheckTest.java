package examples.theory;

import org.jcheck.annotations.Configuration;
import org.jcheck.annotations.Generator;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(org.jcheck.runners.JCheckRunner.class)
public class JCheckTest {

	@Test
	@Configuration(tests=10, size=20)
	public void testWithIntegers(int i) {
		System.out.println("Random " + i);
	}

	@Generator(klass = Integer.class, generator = JCheckGenerator.class)
	@Test
	@Configuration(tests=10)
	public void testWithEnumeratedIntegers(Integer i) {
		System.out.println("Random enumerated " + i);
	}

}

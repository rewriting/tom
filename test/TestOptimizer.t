import static org.junit.Assert.*;
import org.junit.Test;

public class TestOptimizer {

	%include { string.tom }
	
	public static void main(String[] args) {
    org.junit.runner.JUnitCore.runClasses(TestOptimizer.class);
	}

  @Test
	public void test1() {
		String s = "";
		// used to make the optimizer fail
		// with a stackoverflow exception
		%match(String s) {
			a@x	-> {
				// nothing
			}
			_a@_x	-> {
				// nothing
			}
		}
	}

}

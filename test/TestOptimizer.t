import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestOptimizer extends TestCase {

	%include { string.tom }
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(new TestSuite(TestOptimizer.class));
	}

	public void test1() {
		String s = "";
		// used to make the optimizer fail
		// with a stackoverflow exception
		%match(String s){
			a@x	->{
				// nothing
			}
		}
	}

}

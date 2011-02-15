package ppeditor.test;

import junit.framework.Test;

import junit.framework.TestSuite;


public class AllTests {


public static Test suite() {

TestSuite suite =

new TestSuite("Suite de teste pour les outils de calcul");

//$JUnit-BEGIN$

suite.addTest(new TestSuite(Test.class));

suite.addTest(new TestSuite(ExtendedCalculatorTest.class));

//$JUnit-END$

return suite;

}

} 	

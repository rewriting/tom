package ppeditor.test;

import junit.framework.Test;

import junit.framework.TestSuite;


public class AllTests {


public static Test suite() {

TestSuite suite =

new TestSuite("Suite de test pour les outils de calcul");

//$JUnit-BEGIN$

suite.addTest(new TestSuite(TestPPCursor.class));

suite.addTest(new TestSuite(TestPPEditor.class));

//$JUnit-END$

return suite;

}

} 	

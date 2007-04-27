/*
 * Copyright (c) 2004-2007, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the INRIA nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package sl;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestSL extends TestCase {	  

  %include { testsl/testsl.tom }

  private NewBehaviour nb = new NewBehaviour();
  private OldBehaviour ob = new OldBehaviour();

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestSL.class));
  }

  public void setUp() {

  }

  public void testSL1() throws jjtraveler.VisitFailure {		
    assertTrue( ob.test1() == nb.test1() );
  }
  public void testSL2() throws jjtraveler.VisitFailure {		
    assertTrue( ob.test2() == nb.test2() );
  }
  public void testSL3() throws jjtraveler.VisitFailure {		
    assertTrue( ob.test3() == nb.test3() );
  }
  public void testSL4() throws jjtraveler.VisitFailure {		
    assertTrue( ob.test4() == nb.test4() );
  }
  public void testSL5() throws jjtraveler.VisitFailure {		
    assertTrue( ob.test5() == nb.test5() );
  }
  public void testSLChoice() throws jjtraveler.VisitFailure {		
    assertTrue( ob.testChoice() == nb.testChoice() );
  }
  public void testSLChoiceSideEffect() throws jjtraveler.VisitFailure {		
    assertTrue( ob.testChoiceSideEffect() == nb.testChoiceSideEffect() );
  } 
  public void testSLNot() throws jjtraveler.VisitFailure {		
    assertTrue( ob.testNot() == nb.testNot() );
  }
  public void testSLNotSideEffect() throws jjtraveler.VisitFailure {		
    assertTrue( ob.testNotSideEffect() == nb.testNotSideEffect() );
  }
  public void testSL6() throws jjtraveler.VisitFailure {		
    assertTrue( ob.test6() == nb.test6() );
  }
  public void testSL7() throws jjtraveler.VisitFailure {		
    assertTrue( ob.test7() == nb.test7() );
  }
  public void testSL8() throws jjtraveler.VisitFailure {		
    assertTrue( ob.test8() == nb.test8() );
  }
  public void testSL9() throws jjtraveler.VisitFailure {		
    assertTrue( ob.test9() == nb.test9() );
  }
  public void testITESideEffect() throws jjtraveler.VisitFailure {		
    assertTrue( ob.testITESideEffect() == nb.testITESideEffect() );
  }
  public void testSL10() throws jjtraveler.VisitFailure {		
    assertTrue( ob.test10() == nb.test10() );
  }
  public void testSL11() throws jjtraveler.VisitFailure {		
    assertTrue( ob.test11() == nb.test11() );
  }
  public void testSL12() throws jjtraveler.VisitFailure {		
    assertTrue( ob.test12() == nb.test12() );
  }
  public void testSL13() throws jjtraveler.VisitFailure {		
    assertTrue( ob.test13() == nb.test13() );
  }
  public void testSLWhen1() throws jjtraveler.VisitFailure {		
    assertTrue( ob.testWhen1() == nb.testWhen1() );
  }
  public void testSLCongruence1() throws jjtraveler.VisitFailure {		
    assertTrue( ob.testCongruence1() == nb.testCongruence1() );
  }
  public void testSLCongruence2() throws jjtraveler.VisitFailure {	
    try {
      ob.testCongruence2();
      fail();
    } catch (RuntimeException e) {
      try {
        nb.testCongruence2();
        fail();
      } catch (tom.library.sl.FireException ee) {/*success*/}
    }
  }
  public void testSLCongruenceList() throws jjtraveler.VisitFailure {
    assertTrue( ob.testCongruenceList() == nb.testCongruenceList() );
  }

}

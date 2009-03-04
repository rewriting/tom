/*
 * Copyright (c) 2004-2009, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *   - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.  
 *   - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
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

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class TestSL {

  %include { testsl/testsl.tom }

  private NewBehaviour nb = new NewBehaviour();
  private OldBehaviour ob = new OldBehaviour();

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.runClasses(TestSL.class);
  }

  @Before
  public void setUp() {

  }

  @Test
  public void testSL1() throws tom.library.sl.VisitFailure {    
    assertTrue( ob.test1() == nb.test1() );
  }
  @Test
  public void testSL2() throws tom.library.sl.VisitFailure {    
    assertTrue( ob.test2() == nb.test2() );
  }
  @Test
  public void testSL3() throws tom.library.sl.VisitFailure {    
    assertTrue( ob.test3() == nb.test3() );
  }
  @Test
  public void testSL4() throws tom.library.sl.VisitFailure {    
    assertTrue( ob.test4() == nb.test4() );
  }
  @Test
  public void testSL5() throws tom.library.sl.VisitFailure {    
    assertTrue( ob.test5() == nb.test5() );
  }
  @Test
  public void testSLChoice() throws tom.library.sl.VisitFailure {   
    assertTrue( ob.testChoice() == nb.testChoice() );
  }
  @Test
  public void testSLChoiceSideEffect() throws tom.library.sl.VisitFailure {   
    assertTrue( ob.testChoiceSideEffect() == nb.testChoiceSideEffect() );
  } 
  @Test
  public void testSLNot() throws tom.library.sl.VisitFailure {    
    assertTrue( ob.testNot() == nb.testNot() );
  }
  @Test
  public void testSLNotSideEffect() throws tom.library.sl.VisitFailure {    
    assertTrue( ob.testNotSideEffect() == nb.testNotSideEffect() );
  }
  @Test
  public void testSL6() throws tom.library.sl.VisitFailure {    
    assertTrue( ob.test6() == nb.test6() );
  }
  @Test
  public void testSL7() throws tom.library.sl.VisitFailure {    
    assertTrue( ob.test7() == nb.test7() );
  }
  @Test
  public void testSL8() throws tom.library.sl.VisitFailure {    
    assertTrue( ob.test8() == nb.test8() );
  }
  @Test
  public void testSL9() throws tom.library.sl.VisitFailure {    
    assertTrue( ob.test9() == nb.test9() );
  }
  @Test
  public void testITESideEffect() throws tom.library.sl.VisitFailure {    
    assertTrue( ob.testITESideEffect() == nb.testITESideEffect() );
  }
  @Test
  public void testSL10() throws tom.library.sl.VisitFailure {   
    assertTrue( ob.test10() == nb.test10() );
  }
  @Test
  public void testSL11() throws tom.library.sl.VisitFailure {   
    assertTrue( ob.test11() == nb.test11() );
  }
  @Test
  public void testSL12() throws tom.library.sl.VisitFailure {   
    assertTrue( ob.test12() == nb.test12() );
  }
  @Test
  public void testSL13() throws tom.library.sl.VisitFailure {   
    assertTrue( ob.test13() == nb.test13() );
  }
  @Test
  public void testSLWhen1() throws tom.library.sl.VisitFailure {    
    assertTrue( ob.testWhen1() == nb.testWhen1() );
  }
  @Test
  public void testSLCongruence1() throws tom.library.sl.VisitFailure {    
    assertTrue( ob.testCongruence1() == nb.testCongruence1() );
  }
  @Test
  public void testSLCongruence2() throws tom.library.sl.VisitFailure {  
    try {
      ob.testCongruence2();
      fail();
    } catch (RuntimeException e) {
      try {
        nb.testCongruence2();
        fail();
      } catch (tom.library.sl.VisitFailure ee) {/*success*/}
    }
  }
  @Test
  public void testSLCongruenceList() throws tom.library.sl.VisitFailure {
    assertTrue( ob.testCongruenceList() == nb.testCongruenceList() );
  }

}

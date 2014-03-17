/*
 * Copyright (c) 2004-2014, Universite de Lorraine, Inria
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
 *  - Neither the name of the Inria nor the names of its
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
import sl.testsl.types.*;

public class TestSL {

  %include { testsl/testsl.tom }

  private NewBehaviour nb = new NewBehaviour();
  private OldBehaviour ob = new OldBehaviour();
  private IntrospectorBehaviour ib = new IntrospectorBehaviour();

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestSL.class.getName());
  }

  @Test
  public void testSL1() throws tom.library.sl.VisitFailure {    
    Term result = `f(a());
    assertEquals( result, ob.test1() );
    assertEquals( result, nb.test1() );
    assertEquals( result, ib.test1() );
  }
  @Test
  public void testSL2() throws tom.library.sl.VisitFailure {    
    Term result = `f(b());
    assertEquals( result, ob.test2() );
    assertEquals( result, nb.test2() );
    assertEquals( result, ib.test2() );
  }
  @Test
  public void testSL3() throws tom.library.sl.VisitFailure {    
    Term result = `f(a());
    assertEquals( result, ob.test3() );
    assertEquals( result, nb.test3() );
    assertEquals( result, ib.test3() );
  }
  @Test
  public void testSL4() throws tom.library.sl.VisitFailure {    
    Term result = `g(f(c()),c());
    assertEquals( result, ob.test4() );
    assertEquals( result, nb.test4() );
    assertEquals( result, ib.test4() );
  }
  @Test
  public void testSL5() throws tom.library.sl.VisitFailure {    
    Term result = `g(f(b()),c()); 
    assertEquals( result, ob.test5() );
    assertEquals( result, nb.test5() );
    assertEquals( result, ib.test5() );
  }
  @Test
  public void testSLChoice() throws tom.library.sl.VisitFailure {   
    Term result = `f(b()); //'Choice' does not fail even if both choices fail.
    assertEquals( result, ob.testChoice() );
    assertEquals( result, nb.testChoice() );
    assertEquals( result, ib.testChoice() );
  }
  @Test
  public void testSLChoiceSideEffect() throws tom.library.sl.VisitFailure {   
    Term result = `f(b()); //'Choice' does not fail even if both choices fail, but the side effect of first R1() remains.
    assertEquals( result, ob.testChoiceSideEffect() );
    assertEquals( result, nb.testChoiceSideEffect() );
    assertEquals( result, ib.testChoiceSideEffect() );
  }
  @Test
  public void testSLNot() throws tom.library.sl.VisitFailure {    
    Term result = `f(b());
    assertEquals( result, ob.testNot() );
    assertEquals( result, nb.testNot() );
    assertEquals( result, ib.testNot() );
  }
  @Test
  public void testSLNotSideEffect() throws tom.library.sl.VisitFailure {    
    Term result = `f(a());
    assertEquals( result, ob.testNotSideEffect() );
    assertEquals( result, nb.testNotSideEffect() );
    assertEquals( result, ib.testNotSideEffect() );
  }
  @Test
  public void testSL6() throws tom.library.sl.VisitFailure {
    Term result = `g(f(c()),c());
    assertEquals( result, ob.test6() );
    assertEquals( result, nb.test6() );
    assertEquals( result, ib.test6() );
  }
  @Test
  public void testSL7() throws tom.library.sl.VisitFailure {    
    Term result = `g(f(b()),b());
    assertEquals( result, ob.test7() );
    assertEquals( result, nb.test7() );
    assertEquals( result, ib.test7() );
  }
  @Test
  public void testSL8() throws tom.library.sl.VisitFailure {    
    Term result = `g(f(a()),c()); 
    assertEquals( result, ob.test8() );
    assertEquals( result, nb.test8() );
    assertEquals( result, ib.test8() );
  }
  @Test
  public void testSL9() throws tom.library.sl.VisitFailure {    
    Term result = `b(); 
    assertEquals( result, ob.test9() );
    assertEquals( result, nb.test9() );
    assertEquals( result, ib.test9() );
  }
  @Test
  public void testITESideEffect() throws tom.library.sl.VisitFailure {    
    Term result = `f(a()); //todo?? should this not produce side effect?
    assertEquals( result, ob.testITESideEffect() );
    assertEquals( result, nb.testITESideEffect() );
    assertEquals( result, ib.testITESideEffect() );
  }
  @Test
  public void testSL10() throws tom.library.sl.VisitFailure {   
    Term result = `a();
    assertEquals( result, ob.test10() );
    assertEquals( result, nb.test10() );
    assertEquals( result, ib.test10() );
  }
  @Test
  public void testSL11() throws tom.library.sl.VisitFailure {   
    Term result = `g(f(c()),c());
    assertEquals( result, ob.test11() );
    assertEquals( result, nb.test11() );
    assertEquals( result, ib.test11() );
  }
  @Test
  public void testSL12() throws tom.library.sl.VisitFailure {   
    Term result = `g(f(c()),c()); //'Innermost' applies until no more success. (3 applications as in previous)
    assertEquals( result, ob.test12() );
    assertEquals( result, nb.test12() );
    assertEquals( result, ib.test12() );
  }
  @Test
  public void testSL13() throws tom.library.sl.VisitFailure {   
    Term result = `g(f(c()),c()); //'Outermost' applies until no more success. (3 applications as in previous)
    assertEquals( result, ob.test13() );
    assertEquals( result, nb.test13() );
    assertEquals( result, ib.test13() );
  }
  @Test
  public void testSLWhen1() throws tom.library.sl.VisitFailure {    
    Term result = `f(b());
    assertEquals( result, ob.testWhen1() );
    assertEquals( result, nb.testWhen1() );
    assertEquals( result, ib.testWhen1() );
  }
  @Test
  public void testSLCongruence1() throws tom.library.sl.VisitFailure {    
    Term result = `g(f(b()),c());
    assertEquals( result, ob.testCongruence1() );
    assertEquals( result, nb.testCongruence1() );
    assertEquals( result, ib.testCongruence1() );
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
    try {
      ib.testCongruence2();
      fail();
    } catch (tom.library.sl.VisitFailure ee) {/*success*/}
  }
  @Test
  public void testSLCongruenceList() throws tom.library.sl.VisitFailure {
    Term result = `l(a(),c()); 
    assertEquals( result, ob.testCongruenceList() );
    assertEquals( result, nb.testCongruenceList() );
    assertEquals( result, ib.testCongruenceList() );
  }

}

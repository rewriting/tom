/*
 * Copyright (c) 2004, INRIA
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

package minirho;

 import aterm.*;
 import aterm.pure.*;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import minirho.rho.rhoterm.*;
import minirho.rho.rhoterm.types.*;

public class TestRho extends TestCase {
  private Rho interpreteur;
  private static rhotermFactory factory;
	private static int compteur = 0;
	public static void main(String[] args) {
		junit.textui.TestRunner.run(new TestSuite(TestRho.class));
	}

  public void setUp() {
		if (factory == null) {
			factory = rhotermFactory.getInstance(new PureFactory());
		}
		interpreteur = new Rho(factory);
  }

  public rhotermFactory getSeqFactory() {
    return factory;
  }

  public void mytestBasic(String term, String resultat){
		compteur++;
		assertEquals(term,resultat, interpreteur.test(term));
	}
	public void testBasic1(){
		mytestBasic("var(\"X\")","var(\"X\")");
	}	public void testBasic2(){
		mytestBasic("const(\"a\")","const(\"a\")");
	}	public void testBasic3(){
		mytestBasic("app(abs(const(\"a\"),const(\"b\")),const(\"a\"))", "const(\"b\")");
	}	public void testBasic4(){
		mytestBasic("app(abs(var(\"X\"),var(\"X\")),const(\"a\"))","const(\"a\")");
	}	public void testBasic5(){
		mytestBasic("app(abs(app(app(const(\"f\"),var(\"X\")),var(\"Y\")),var(\"X\")),app(app(const(\"f\"),const(\"a\")),const(\"b\")))","const(\"a\")");	
	}	public void testBasic5bis(){
		mytestBasic("app(abs(app(const(\"f\"),var(\"X\")),var(\"X\")),app(const(\"f\"),const(\"a\")))","const(\"a\")");
	}	public void testBasic6(){
		//(((f X) Y) Z -> Y) f a b c
		mytestBasic("app(abs(app(app(app(const(\"f\"),var(\"X\")),var(\"Y\")),var(\"Z\")),var(\"Y\")),app(app(app(const(\"f\"),const(\"a\")),const(\"b\")),const(\"c\")))","const(\"b\")");
	}	public void testBasic7(){
		//Z -> ((f X Y -> X) f ab)
		mytestBasic("abs(var(\"Z\"),app(abs(app(app(const(\"f\"),var(\"X\")),var(\"Y\")),var(\"X\")),app(app(const(\"f\"),const(\"a\")),const(\"b\"))))","abs(var(\"Z\"),app(abs(app(app(const(\"f\"),var(\"X\")),var(\"Y\")),var(\"X\")),app(app(const(\"f\"),const(\"a\")),const(\"b\"))))");
	}	public void testBasic8(){
//(Z -> ((f X Y -> X) f ab) ) 3
		mytestBasic("app(abs(var(\"Z\"),app(abs(app(app(const(\"f\"),var(\"X\")),var(\"Y\")),var(\"X\")),app(app(const(\"f\"),const(\"a\")),const(\"b\")))),const(\"3\"))","const(\"a\")");
	}	public void testBasic9(){
		//(Z -> ((f X Y -> X,Y,Z) f ab) ) 3
		mytestBasic("app(abs(var(\"Z\"),app(abs(app(app(const(\"f\"),var(\"X\")),var(\"Y\")),struct(struct(var(\"X\"),var(\"Y\")),var(\"Z\"))),app(app(const(\"f\"),const(\"a\")),const(\"b\")))),const(\"3\"))","struct(struct(const(\"a\"),const(\"b\")),const(\"3\"))");
	}	public void testBasic10(){
		//	(f X Y -> X,Y) f ab
		mytestBasic("app(abs(app(app(const(\"f\"),var(\"X\")),var(\"Y\")),struct(var(\"X\"),var(\"Y\"))),app(app(const(\"f\"),const(\"a\")),const(\"b\")))","struct(const(\"a\"),const(\"b\"))");
	}	public void testBasic11(){
//(f X Y -> Y,Y) f ab
		mytestBasic("app(abs(app(app(const(\"f\"),var(\"X\")),var(\"Y\")),struct(var(\"Y\"),var(\"Y\"))),app(app(const(\"f\"),const(\"a\")),const(\"b\")))","struct(const(\"b\"),const(\"b\"))");
	}	public void testBasic12(){
//(f X Y -> Y,Y,Y,X,X,X) f ab
		mytestBasic("app(abs(app(app(const(\"f\"),var(\"X\")),var(\"Y\")),struct(struct(struct(struct(struct(var(\"Y\"),var(\"Y\")),var(\"Y\")),var(\"X\")),var(\"X\")),var(\"X\"))),app(app(const(\"f\"),const(\"a\")),const(\"b\")))",
								"struct(struct(struct(struct(struct(const(\"b\"),const(\"b\")),const(\"b\")),const(\"a\")),const(\"a\")),const(\"a\"))");

/*//(f X Y -> X) g a b
	mytestBasic("app(abs(app(app(const(\"f\"),var(\"X\")),var(\"Y\")),var(\"X\")),app(app(const(\"g\"),const(\"a\")),const(\"b\")))","appC(matchKO(app(app(const(\"f\"),var(\"X\")),var(\"Y\")),app(app(const(\"g\"),const(\"a\")),const(\"b\"))),var(\"X\"))");*/

	}	public void testBasic13(){
//(f X Y -> X, f X Y -> Y) f ab
		mytestBasic("app(struct(abs(app(app(const(\"f\"),var(\"X\")),var(\"Y\")),var(\"X\")),abs(app(app(const(\"f\"),var(\"X\")),var(\"Y\")),var(\"Y\"))),app(app(const(\"f\"),const(\"a\")),const(\"b\")))","struct(const(\"a\"),const(\"b\"))");
			}	public void testBasic14(){
//((f X Y -> X, f X Y -> Y) f Z H)
		mytestBasic("app(struct(abs(app(app(const(\"f\"),var(\"X\")),var(\"Y\")),var(\"X\")),abs(app(app(const(\"f\"),var(\"X\")),var(\"Y\")),var(\"Y\"))),app(app(const(\"f\"),var(\"Z\")),var(\"H\")))","struct(var(\"Z\"),var(\"H\"))");
	}	public void testBasic15(){
//(h Z H h(g(I))  -> ((f X Y -> X, f X Y -> Y) f Z H)) h(h(a),h(b),h(g(b)))
		mytestBasic("app(abs(app(app(app(const(\"h\"),var(\"Z\")),var(\"H\")),app(const(\"h\"),app(const(\"g\"),var(\"I\")))),app(struct(abs(app(app(const(\"f\"),var(\"X\")),var(\"Y\")),var(\"X\")),abs(app(app(const(\"f\"),var(\"X\")),var(\"Y\")),var(\"Y\"))),app(app(const(\"f\"),var(\"Z\")),var(\"H\")))),app(app(app(const(\"h\"),app(const(\"h\"),const(\"a\"))),app(const(\"h\"),const(\"b\"))),app(const(\"h\"),app(const(\"g\"),const(\"b\")))))","struct(app(const(\"h\"),const(\"a\")),app(const(\"h\"),const(\"b\")))");
			}	public void testBasic16(){
//(f X h(Y) g(h(Z)) -> (h(T) -> T)Y, (U,V -> U)X,Z) f a h(h(b)) g(h(a)
		mytestBasic("app(abs(app(app(app(const(\"f\"),var(\"X\")),app(const(\"h\"),var(\"Y\"))),app(const(\"g\"), app(const(\"h\"),var(\"Z\")))),struct(app(abs(app(const(\"h\"),var(\"T\")),var(\"T\")),var(\"Y\")),app(abs(struct(var(\"U\"),var(\"V\")),var(\"U\")),struct(var(\"X\"),var(\"Z\"))))), app(app(app(const(\"f\"),const(\"a\")),app(const(\"h\"),app(const(\"h\"),const(\"b\")))),app(const(\"g\"),app(const(\"h\"),const(\"a\")))))","struct(const(\"b\"),const(\"a\"))");
			}	public void testBasic17(){
//(f X h(Y) g(h(Z)) -> (h(T) -> T)Y, (U,V -> i(U,V,Y))X,Z) f a h(h(b)) g(h(a)
			mytestBasic("app(abs(app(app(app(const(\"f\"),var(\"X\")),app(const(\"h\"),var(\"Y\"))),app(const(\"g\"), app(const(\"h\"),var(\"Z\")))),struct(app(abs(app(const(\"h\"),var(\"T\")),var(\"T\")),var(\"Y\")),app(abs(struct(var(\"U\"),var(\"V\")),app(app(app(const(\"i\"),var(\"U\")),var(\"V\")),var(\"Y\"))),struct(var(\"X\"),var(\"Z\"))))), app(app(app(const(\"f\"),const(\"a\")),app(const(\"h\"),app(const(\"h\"),const(\"b\")))),app(const(\"g\"),app(const(\"h\"),const(\"a\")))))","struct(const(\"b\"),app(app(app(const(\"i\"),const(\"a\")),const(\"a\")),app(const(\"h\"),const(\"b\"))))");
			}
//  	public void testAddition(){
//  		assertEquals("o + o",
//  								 interpreteur.test("app(app(app(abs(app(const(\"fix\"),var(\"Z\")),struct(abs(const(\"0\"),abs(var(\"Y\"),var(\"Y\"))),abs(app(const(\"s\"),var(\"X\")),abs(var(\"Y\"),app(const(\"s\"),app(app(app(var(\"Z\"),app(const(\"fix\"),var(\"Z\"))),var(\"X\")),var(\"Y\"))))))),app(const(\"fix\"),abs(app(const(\"fix\"),var(\"Z\")),struct(abs(const(\"0\"),abs(var(\"Y\"),var(\"Y\"))),abs(app(const(\"s\"),var(\"X\")),abs(var(\"Y\"),app(const(\"s\"),app(app(app(var(\"Z\"),app(const(\"fix\"),var(\"Z\"))),var(\"X\")),var(\"Y\"))))))))),const(\"0\")),const(\"0\"))") ,"const(\"0\")");

//  		assertEquals("one + o",
//  								 interpreteur.test("app(app(app(abs(app(const(\"fix\"),var(\"Z\")),struct(abs(const(\"0\"),abs(var(\"Y\"),var(\"Y\"))),abs(app(const(\"s\"),var(\"X\")),abs(var(\"Y\"),app(const(\"s\"),app(app(app(var(\"Z\"),app(const(\"fix\"),var(\"Z\"))),var(\"X\")),var(\"Y\"))))))),app(const(\"fix\"),abs(app(const(\"fix\"),var(\"Z\")),struct(abs(const(\"0\"),abs(var(\"Y\"),var(\"Y\"))),abs(app(const(\"s\"),var(\"X\")),abs(var(\"Y\"),app(const(\"s\"),app(app(app(var(\"Z\"),app(const(\"fix\"),var(\"Z\"))),var(\"X\")),var(\"Y\"))))))))),app(const(\"s\"),const(\"0\"))),const(\"0\"))")
//  								 ,"app(const(\"s\"),const(\"0\"))");

//  		assertEquals("o + one",
//  								 interpreteur.test("app(app(app(abs(app(const(\"fix\"),var(\"Z\")),struct(abs(const(\"0\"),abs(var(\"Y\"),var(\"Y\"))),abs(app(const(\"s\"),var(\"X\")),abs(var(\"Y\"),app(const(\"s\"),app(app(app(var(\"Z\"),app(const(\"fix\"),var(\"Z\"))),var(\"X\")),var(\"Y\"))))))),app(const(\"fix\"),abs(app(const(\"fix\"),var(\"Z\")),struct(abs(const(\"0\"),abs(var(\"Y\"),var(\"Y\"))),abs(app(const(\"s\"),var(\"X\")),abs(var(\"Y\"),app(const(\"s\"),app(app(app(var(\"Z\"),app(const(\"fix\"),var(\"Z\"))),var(\"X\")),var(\"Y\"))))))))),const(\"0\")),app(const(\"s\"),const(\"0\")))"),
//  								 "app(const(\"s\"),const(\"0\"))");
//  		assertEquals("one + one",
//  								 interpreteur.test("app(app(app(abs(app(const(\"fix\"),var(\"Z\")),struct(abs(const(\"0\"),abs(var(\"Y\"),var(\"Y\"))),abs(app(const(\"s\"),var(\"X\")),abs(var(\"Y\"),app(const(\"s\"),app(app(app(var(\"Z\"),app(const(\"fix\"),var(\"Z\"))),var(\"X\")),var(\"Y\"))))))),app(const(\"fix\"),abs(app(const(\"fix\"),var(\"Z\")),struct(abs(const(\"0\"),abs(var(\"Y\"),var(\"Y\"))),abs(app(const(\"s\"),var(\"X\")),abs(var(\"Y\"),app(const(\"s\"),app(app(app(var(\"Z\"),app(const(\"fix\"),var(\"Z\"))),var(\"X\")),var(\"Y\"))))))))),app(const(\"s\"),const(\"0\"))),app(const(\"s\"),const(\"0\")))") ,"app(const(\"s\"),app(const(\"s\"),const(\"0\")))");
//  		assertEquals("o + two",
//  								 interpreteur.test("app(app(app(abs(app(const(\"fix\"),var(\"Z\")),struct(abs(const(\"0\"),abs(var(\"Y\"),var(\"Y\"))),abs(app(const(\"s\"),var(\"X\")),abs(var(\"Y\"),app(const(\"s\"),app(app(app(var(\"Z\"),app(const(\"fix\"),var(\"Z\"))),var(\"X\")),var(\"Y\"))))))),app(const(\"fix\"),abs(app(const(\"fix\"),var(\"Z\")),struct(abs(const(\"0\"),abs(var(\"Y\"),var(\"Y\"))),abs(app(const(\"s\"),var(\"X\")),abs(var(\"Y\"),app(const(\"s\"),app(app(app(var(\"Z\"),app(const(\"fix\"),var(\"Z\"))),var(\"X\")),var(\"Y\"))))))))),const(\"0\")),app(const(\"s\"),app(const(\"s\"),const(\"0\"))))")
//  ,"app(const(\"s\"),app(const(\"s\"),const(\"0\")))");

//  			assertEquals("two + o",
//  									 interpreteur.test("app(app(app(abs(app(const(\"fix\"),var(\"Z\")),struct(abs(const(\"0\"),abs(var(\"Y\"),var(\"Y\"))),abs(app(const(\"s\"),var(\"X\")),abs(var(\"Y\"),app(const(\"s\"),app(app(app(var(\"Z\"),app(const(\"fix\"),var(\"Z\"))),var(\"X\")),var(\"Y\"))))))),app(const(\"fix\"),abs(app(const(\"fix\"),var(\"Z\")),struct(abs(const(\"0\"),abs(var(\"Y\"),var(\"Y\"))),abs(app(const(\"s\"),var(\"X\")),abs(var(\"Y\"),app(const(\"s\"),app(app(app(var(\"Z\"),app(const(\"fix\"),var(\"Z\"))),var(\"X\")),var(\"Y\"))))))))),app(const(\"s\"),app(const(\"s\"),const(\"0\")))),const(\"0\"))")
//  ,"app(const(\"s\"),app(const(\"s\"),const(\"0\")))");

// 			assertEquals("two + two",
// 									 interpreteur.test("app(app(app(abs(app(const(\"fix\"),var(\"Z\")),struct(abs(const(\"0\"),abs(var(\"Y\"),var(\"Y\"))),abs(app(const(\"s\"),var(\"X\")),abs(var(\"Y\"),app(const(\"s\"),app(app(app(var(\"Z\"),app(const(\"fix\"),var(\"Z\"))),var(\"X\")),var(\"Y\"))))))),app(const(\"fix\"),abs(app(const(\"fix\"),var(\"Z\")),struct(abs(const(\"0\"),abs(var(\"Y\"),var(\"Y\"))),abs(app(const(\"s\"),var(\"X\")),abs(var(\"Y\"),app(const(\"s\"),app(app(app(var(\"Z\"),app(const(\"fix\"),var(\"Z\"))),var(\"X\")),var(\"Y\"))))))))),app(const(\"s\"),app(const(\"s\"),const(\"0\")))),app(const(\"s\"),app(const(\"s\"),const(\"0\"))))"),
// 									 "app(const(\"s\"),app(const(\"s\"),app(const(\"s\"),app(const(\"s\"),const(\"0\")))))");



			//	}
}

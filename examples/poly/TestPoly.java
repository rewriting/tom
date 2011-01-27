/*
 * Copyright (c) 2004-2011, INPL, INRIA
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

package poly;

import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  PolyAdvanced1.class,
  PolyAdvanced2.class,
  PolyApigen1.class,
  PolyApigen2.class,
  PolySimple2.class,
  PolyTraveler1.class,
  PolyTraveler2.class
})

public class TestPoly {

	public static void main(String[] args) {
		//junit.textui.TestRunner.run(suite());
    org.junit.runner.JUnitCore.main(TestPoly.class.getName());
	}

  /*public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTest(new TestSuite(PolyAdvanced1.class));
    suite.addTest(new TestSuite(PolyAdvanced2.class));
    suite.addTest(new TestSuite(PolyApigen1.class));
    suite.addTest(new TestSuite(PolyApigen2.class));
    suite.addTest(new TestSuite(PolySimple2.class));
    suite.addTest(new TestSuite(PolyTraveler1.class));
    suite.addTest(new TestSuite(PolyTraveler2.class));
    return suite;
  }*/

}

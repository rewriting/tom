/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
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

import static org.junit.Assert.*;
import org.junit.Test;

import testsublists.m.types.*;

public class TestSublists {
  %gom {
    module M
      abstract syntax
      Term =
      | a()
      | b()
      | c()
      | list( Term* )
      | f( l:TermList)

      TermList = termList(Term*)
  }

  @Test
  public void test1() {
    Term res = `list(a(),b(),c());
    %match(res) {
      list(_*,list(a(),b(),c()),_*) -> {
        return;
      }
    }
    fail();
  }

  @Test
  public void test2() {
    Term res = `list(a(),b(),c());
    %match(res) {
      list(_*,_p@list(a(),b(),c()),_*) -> {
        return;
      }
    }
    fail();
  }

/*
  @Test
  public void test3() {
    Term res = `list(a(),b(),b(),c());
    %match(res) {
      list(X*,p@list(b(),b()),Y*) -> {
        if (`p == `list(b(),b()) && `X == `list(a()) && `Y == `list(c())) {
          return;
        } else {
          fail();
        }
      }
    }
    fail();
  }


  @Test
  public void test4() {
    Term res = `list(a(),b(),b(),c());
    %match(res) {
      list(_X*,p@!list(_*,!b(),_*),_Y*) -> {
        if (`p == `list(b(),b()) || `p == `list(b()) || `p == `list() ) {
          return;
        } else {
          fail();
        }
      }
    }
    fail();
  }

  //////// domain != codomain ///////////

  @Test
  public void test5() {
    TermList res = `termList(a(),b(),b(),c());
    %match(res) {
      termList(_X*,_p@list(_*,b(),_*),_Y*) -> {
        fail();
      }
    }
  }

  @Test
  public void test6() {
    TermList res = `termList(a(),list(b(),b()),c());
    %match(res) {
      termList(_X*,list(_*,b(),_*),_Y*) -> {
        return;
      }
    }
    fail();
  }

  @Test
  public void test7() {
    TermList res = `termList(a(),b(),b(),c());
    %match(res) {
      termList(_X*,termList(_*,b(),_*),_Y*) -> {
        return;
      }
    }
    fail();
  }

  @Test
  public void test8() {
    TermList res = `termList(a(),b(),b(),c());
    %match(res) {
      termList(_X*,p@!termList(_*,!b(),_*),_Y*) -> {
        if( `p == `termList(b()) || `p == `termList(b(),b()) || `p == `termList()) {
          return;
        } else {
          fail();
        }
      }
    }
    fail();
  }

  @Test
  public void test9() {
    TermList res = `termList(a(),b(),b(),c());
    %match(res) {
      termList(_X*,p@termList(b(),b()),_Y*) -> {
        if( `p == `termList(b(),b()) ) {
          return;
        } else {
          fail();
        }
      }
    }
    fail();
  }

  @Test
  public void test10() {
    TermList res = `termList(a(),b(),b(),c());
    %match(res) {
      termList(_X*,p@(b(),!b()),_Y*) -> {
        if( `p == `list(b(),b()) ) {
          fail();
        }
      }
    }
  }

  @Test
  public void test11() {
    TermList t = `termList(termList(a(),b()));
    %match(t) {
      _z@termList(termList(a(),b())) -> {
        return;
      }
    }
    fail();
  }
*/
  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestSublists.class.getName());
  }

}

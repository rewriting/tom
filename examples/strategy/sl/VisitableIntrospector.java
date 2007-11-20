/*
*
* Copyright (c) 2000-2007, Pierre-Etienne Moreau
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
*
**/

package strategy.sl;

public class VisitableIntrospector implements Introspector {

  //singleton
  private static Introspector mapping = new VisitableIntrospector();

  private VisitableIntrospector() {
  }

  public static Introspector getInstance() {
    return mapping;
  } 

  public Object setChildren(Object o, Object[] children) {
    if (o instanceof Visitable) {
      return ((Visitable)o).setChildren((Visitable[])children); }
    else {
      throw new RuntimeException();
    }
  }

  public Object[] getChildren(Object o){
    if (o instanceof Visitable) {
      return ((Visitable)o).getChildren();
    } else {
      throw new RuntimeException();
    }
  }

  public Object setChildAt(Object o, int i, Object child) {
    if (o instanceof Visitable) {
      return ((Visitable)o).setChildAt(i,(Visitable)child);
    } else {
      throw new RuntimeException();
    } 
  }

  public Object getChildAt(Object o, int i) {
    if (o instanceof Visitable) {
      return ((Visitable)o).getChildAt(i);
    } else {
      throw new RuntimeException();
    } 
  }

  public int getChildCount(Object o) {
    if (o instanceof Visitable) {
      return ((Visitable)o).getChildCount();
    } else {
      throw new RuntimeException();
    }
  }

}

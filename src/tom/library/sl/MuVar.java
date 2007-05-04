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
package tom.library.sl;

/**
 * <code>MuVar(v)</code> always raises a VisitFailure exception. 
 * <p>
 * Basic visitor combinator used to build recursive visitors
 * <p>
 */

public class MuVar extends AbstractStrategy {
  private Strategy instance = null;
  protected String name;
  
  public MuVar(String name) {
    // make sure the MuVar is seen as a leaf for all visitors
    initSubterm();
    this.name = name;
  }

  public boolean equals(Object o) {
    if(o instanceof MuVar) {
      MuVar muVar = (MuVar)o;
      if(muVar != null) {
        if(name != null) {
          return name.equals(muVar.name);
        } else {
          return name==muVar.name && instance==muVar.instance;
        }
      }
    }
    return false;
  }

  public int hashCode() {
    if(name!=null) {
      return name.hashCode();
    } else {
      return instance.hashCode();
    }
  }
  
  public final Visitable visitLight(Visitable any) throws VisitFailure {
    if(instance != null) {
      return instance.visitLight(any);
    } else {
      throw new VisitFailure();
    }
  }

  public int visit() {
    if(instance != null) {
      return instance.visit();
    } else {
      //setStatus(Environment.FAILURE);
      return Environment.FAILURE;
    }
  }

  public Strategy getInstance() {
    return instance;
  }
  
  protected void setInstance(Strategy v) {
    this.instance = v;
  }
  
  protected void setName(String name) {
    this.name = name;
  }

  protected final boolean isExpanded() {
    return instance != null;
  }

  public String getName() {
    return name;
  }

  public String toString() {
    return "[" + name + "," + instance + "]";
  }
}

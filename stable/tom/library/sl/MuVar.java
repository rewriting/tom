/*
 *
 * Copyright (c) 2000-2014, Universite de Lorraine, Inria
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
 * 	- Neither the name of the Inria nor the names of its
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
 * <p>
 * Basic strategy combinator with one argument <code>name</code> 
 * which denotes the name of the mu-variable
 * This basic argument combinator used to build recursive arguments
 * After the mu-expansion, the private variable <code>instance</code>
 * is a pointer to the strategy to execute
 * <p>
 */

public class MuVar extends AbstractStrategyCombinator {
  private Strategy instance = null;
  protected String name;
  
  public MuVar(String name) {
    // make sure the MuVar is seen as a leaf for all arguments
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
  
  public final <T> T visitLight(T any,Introspector i) throws VisitFailure {
    if(instance != null) {
      return instance.visitLight(any,i);
    } else {
      throw new VisitFailure();
    }
  }

  public int visit(Introspector i) {
    if(instance != null) {
      return instance.visit(i);
    } else {
      //setStatus(Environment.FAILURE);
      return Environment.FAILURE;
    }
  }

  public Strategy getInstance() {
    return instance;
  }
  
  public void setInstance(Strategy v) {
    this.instance = v;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  public final boolean isExpanded() {
    return instance != null;
  }

  public String getName() {
    return name;
  }

  public String toString() {
    return "[" + name + "," + instance + "]";
  }
}

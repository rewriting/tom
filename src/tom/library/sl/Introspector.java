/*
 *
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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

public interface Introspector {

  /**
   * Replace all children of a given object, and return the new object
   * @param o the object whose children are replace
   * @param children the new subterm of the object
   */
  public <T> T setChildren(T o, Object[] children);

  /**
   * Return the children of a given object
   * @param o the subject
   */
  public Object[] getChildren(Object o);

  /**
   * Replace the i-th subterm of the object o by child
   * @param o the subject
   * @param i the index of the subterm
   * @param child the new subterm 
   */
  public <T> T setChildAt(T o, int i, Object child);

  /**
   * Return the i-th subterm of the object o
   * @param o the subject
   * @param i the index of the subterm (0<=i<=childCount)
   */
  public Object getChildAt(Object o, int i);

  /**
   * Return the number of subterms of the object o
   * @param o the subject
   * @return the number of children
   */
  public int getChildCount(Object o);

}

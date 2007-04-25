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
 * Represent a path between two locations in a term.
 * The implementations of this class must define several algebraic operations                                      
 * on these paths (i.e. addition, substraction, inverse).                                                          
 */                                                                                                               

public interface Path {

  /**
   * Computes the path from the source of this to the target of the parameter p.
   * The addition of two paths must respect the following equations:
   * <p>
   * <code>t1.add(t2) = (t1.getTail().add(t2)).conc(t1.getHead()) if t1.length()>0,</code> 
   * <p>
   * <code>t1.add(t2) = t2</code>  otherwise.
   * @return the path corresponding to the addition of this and the parameter p.
   * @param p the path to add. 
   */
  public Path add(Path p);

  /**
   * Computes the path from the target of this to the target of the parameter p.
   * The substraction of two paths must respect the following equation:
   * <p>
   * <code>t1.sub(t2) = t2.inv().add(t1),</code> 
   * @return the path corresponding to the substraction of this and the parameter p.
   * @param p the path to substract. 
   */
  public Path sub(Path p);

  /**
   * Computes the path from the target of this to the source of this.
   * The inverse operation  must respect the following equations:
   * <p>
    * <code>t.inv() = t.getTail().inv().add(-t.getHead()) if t1.length()>0,</code> 
   * <p>
   * <code>t.inv() = t</code> otherwise 
   * @return the path corresponding to the inverse path of this.
   */
  public Path inv();
  
  public int length();

  public int getHead();

  public Path getTail();

  /**
   * Gives a canonical form of a path 
   * @return its canonical form 
   */
  public Path normalize();

  public Path conc(int i);

  public int compare(Path path);

}

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
 * Represent a path between two locations in a term.
 * The implementations of this class must define several algebraic operations
 * on these paths (i.e. addition, subtraction, inverse).
 */

public interface Path {

  /**
   * Computes the path from the source of this to the target of the parameter p.
   * The addition of two paths must respect the following equations:
   * <p>
   * <code>t1.add(t2) = (t1.getTail().add(t2)).conc(t1.getHead()) if t1.length()>0,</code>
   * <p>
   * <code>t1.add(t2) = t2</code>  otherwise.
   * @param p the path to add.
   * @return the path corresponding to the addition of this and the parameter p.
   */
  public Path add(Path p);

  /**
   * Computes the path from the target of this to the target of the parameter p.
   * The subtraction of two paths must respect the following equation:
   * <p>
   * <code>t1.sub(t2) = t2.inverse().add(t1),</code>
   * @return the path corresponding to the subtraction of this and the parameter p.
   * @param p the path to subtract.
   */
  public Path sub(Path p);

  /**
   * Computes the path from the target of this to the source of this.
   * The inverse operation  must respect the following equations:
   * <p>
    * <code>t.inverse() = t.getTail().inverse().add(-t.getHead()) if t.length()>0,</code>
   * <p>
   * <code>t.inverse() = t</code> otherwise
   * @return the path corresponding to the inverse path of this.
   */
  public Path inverse();

  /**
   * Gives the canonical form of a path.
   * Two paths are equivalent if for every source the corresponding targets are
   * the same. In an equivalence class, the canonical form is the smallest path.
   * @return its canonical form
   */
  public Path getCanonicalPath();

  /**
   * Computes the length which corresponds to the number of moves
   * needed to go from the source to the target.
   * @return the length of the path
   */
  public int length();

  /**
   * Gives the first move of the path which is represented by an integer i.
   * When i is positive, it corresponds to a move from the parent to th i-th
   * child of the current location. Otherwise, it signifies a move from the
   * i-th child to the parent.
   * @return the first move.
   */
  public int getHead();

  /**
   * Gives the tail of the path which is also a path.
   * @return the path after the first move.
   */
  public Path getTail();

  /**
   * Add the move i to the end of the current path
   * @param i the move to insert.
   * @return the path corresponding to the insersion of i into this.
   */
  public Path conc(int i);

  /**
   * @return an array containing all of the elements in this path in proper sequence (from first to last element).
   */
  public int[] toIntArray();
}

/*
 *
 * Copyright (c) 2000-2007, Pierre-Etienne Moreau All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.  - Redistributions in
 * binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution.  - Neither the name of the INRIA
 * nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 **/

package tom.library.sl;

public class DeRef extends AbstractStrategy {
  public final static int ARG = 0;
  // is it an absolute or a relative reference
  private boolean relative = false;
  // strict means that it fails when it is not a Ref
  private boolean strict = false;

  private DeRef(Strategy s, boolean relative,  boolean strict) {
    initSubterm(s);
    this.relative=relative;
    this.strict = strict;
  }

  public static DeRef make(Strategy s) {
    return new DeRef(s,false,false);
  }

  public static DeRef makeRelative(Strategy s) {
    return new DeRef(s,true,false);
  }

  public static DeRef makeStrict(Strategy s) {
    return new DeRef(s,false,true);
  }

  public static DeRef makeRelativeStrict(Strategy s) {
    return new DeRef(s,true,true);
  }

  public boolean isRelative() { return relative; }
  public boolean isStrict() { return strict; }

  public jjtraveler.Visitable visit(jjtraveler.Visitable x) throws jjtraveler.VisitFailure {
    throw new RuntimeException("The strategy operator DeRef can be used only with the methods visit() and fire()");
  }

  public void visit() {
    if (environment.getSubject() instanceof Path){
      visitPath((Path)environment.getSubject());
    } else {
      if(strict) {
        // does nothing when it is not a Ref
      } else {
        visitors[ARG].visit();
      }
    }
  }

  private void visitPath(Path path) {
    if(relative) {
      Position current = environment.getPosition();
      environment.followPath(path);
      visitors[ARG].visit();
      if (environment.getStatus() != Environment.SUCCESS) {
        environment.followPath(current.sub(environment.getPosition()));
        return;
      }
      environment.followPath(current.sub(environment.getPosition()));
    }
  }

}

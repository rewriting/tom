/*
 *
 * Copyright (c) 2000-2006, Pierre-Etienne Moreau All rights reserved.
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

public class Ref extends AbstractStrategy {
  public final static int ARG = 0;
  // is it an absolute or a relative reference
  private boolean relative = false;
  // strict means that it fails when it is not a Ref
  private boolean strict = false;

  private Ref(Strategy s, boolean relative,  boolean strict) {
    initSubterm(s);
    this.relative=relative;
    this.strict = strict;
  }

  public static Ref make(Strategy s) {
    return new Ref(s,false,false);
  }

  public static Ref makeRelative(Strategy s) {
    return new Ref(s,true,false);
  }

  public static Ref makeStrict(Strategy s) {
    return new Ref(s,false,true);
  }

  public static Ref makeRelativeStrict(Strategy s) {
    return new Ref(s,true,true);
  }

  public boolean isRelative() { return relative; }
  public boolean isStrict() { return strict; }

  public jjtraveler.Visitable visit(jjtraveler.Visitable x) throws jjtraveler.VisitFailure {
    throw new RuntimeException("The strategy operator Ref can be used only with the methods visit() and fire()");
  }

  public void visit() {
    if (getSubject() instanceof Reference){
      visitReference((Reference)getSubject());
    } else {
      if(strict) {
        // does nothing when it is not a Ref
      } else {
        visitors[ARG].visit();
      }
    }
  }

  private void visitReference(Reference ref) {
    int[] pos;
    int[] omega =environment.getOmega();
    int size =environment.depth()+1;
    int[] reducedOmega = new int[size];
    for(int i=0;i<size;i++){
      reducedOmega[i]=omega[i];
    }
    if(relative) {
      pos = getAbsolutePosition(reducedOmega,ref.toArray());
    } else {
      pos = ref.toArray();
    }
    int[] oldToNew = getRelativePosition(reducedOmega,pos);
    int[] newToOld = getRelativePosition(pos,reducedOmega);
    goToPosition(oldToNew);
    visitors[ARG].visit();
    if (getStatus() != Environment.SUCCESS) {
      goToPosition(newToOld);
      return;
    }
    goToPosition(newToOld);
  }

  private void goToPosition(int[] pos) {
    int pos_back = pos[0];
    int pos_length = pos.length;
    for(int i=0;i<pos_back;i++){
      environment.up();
    }
    if(pos_length>1){
      for(int i=1;i<pos_length;i++){
        environment.down(pos[i]);
      }
    }
  }

  private int[] getRelativePosition(int[] source,int[] target) {
    int min_length =Math.min(source.length,target.length);
    int commonPrefixLength=0;
    while(commonPrefixLength<min_length && source[commonPrefixLength]==target[commonPrefixLength]){
      commonPrefixLength++;
    }
    int[] relative = new int[target.length-commonPrefixLength+1];
    relative[0]=source.length-commonPrefixLength;
    for(int j=1;j<relative.length;j++){
      relative[j] = target[commonPrefixLength+j-1];
    }
    return relative;
  }

  private int[] getAbsolutePosition(int[] current,int[] relative) {
    int prefix = current.length-relative[0];
    int absoluteLength = prefix+relative.length-1;
    int[] absolute = new int[absoluteLength];
    for(int i=0 ; i<prefix ; i++) {
      absolute[i]=current[i];
    }
    for(int i=prefix ; i<absoluteLength ; i++){
      absolute[i]=relative[i-prefix+1];
    }
    return absolute;
  }

}

/*
 *
 * Copyright (c) 2000-2006, Pierre-Etienne Moreau
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
package tom.library.strategy.mutraveler;

/**
 * Object that represents a relative position in a term
 * the first element in the list represents the number of backtracking
 * steps and the others classicals steps in the term
 */

public class RelativePosition extends Position {

  public RelativePosition(int[] data) {
    super(data);
  }

  public static RelativePosition make(Position source,Position target) {
    int[] sourceData = source.toArray();
    int[] targetData = target.toArray();
    int min_length =Math.min(sourceData.length,targetData.length); 
    int commonPrefixLength=0;
    while(commonPrefixLength<min_length && sourceData[commonPrefixLength]==targetData[commonPrefixLength]){
      commonPrefixLength++;
    }
    int[] relativeData = new int[target.depth()-commonPrefixLength+1];
    relativeData[0]=source.depth()-commonPrefixLength;
    for(int j=1;j<relativeData.length;j++){
      relativeData[j] = targetData[commonPrefixLength+j-1];
    }
    RelativePosition res = new RelativePosition(relativeData);
    return res;

  }

  public Position getAbsolutePosition(Position currentPosition) {
    int[] currentData = currentPosition.toArray();
    int prefix = currentPosition.depth()-data[0];
    int absoluteDataLength = prefix+depth()-1;
    int[] absoluteData = new int[absoluteDataLength];
    for(int i=0 ; i<prefix ; i++) {
      absoluteData[i]=currentData[i];
    }
    for(int i=prefix ; i<absoluteDataLength ; i++){
      absoluteData[i]=data[i-prefix+1];
    }
    Position absolutePos = new Position(absoluteData);
    return absolutePos;
  }

}

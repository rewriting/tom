/*
 * Copyright (c) 2006, INRIA
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
 */
package builtin;

import java.util.ArrayList;
import java.util.LinkedList;

public class Collections {

  public final static void main(String[] args) {
    Collections test = new Collections();

    ArrayList res = test.testArrayList();
    System.out.println("ArrayList = " + res);

    LinkedList resLinked = test.testLinkedList();
    System.out.println("LinkedList = " + resLinked);
  }

  %include { java/util/ArrayList.tom }
  public ArrayList testArrayList() {
    ArrayList list = `concArrayList();
    ArrayList res = `concArrayList();
    list = `concArrayList("one","two","three","four");
    %match(ArrayList list) {
      (_*,x,_*)   -> { res = `concArrayList((String)x+" thing",res*); }
    }
    return res;
  }

  %include { java/util/LinkedList.tom }
  public LinkedList testLinkedList() {
    LinkedList list = `concLinkedList();
    LinkedList res = `concLinkedList();
    list = `concLinkedList("one","two","three","four");
    %match(LinkedList list) {
      (_*,x,_*)   -> { res = `concLinkedList((String)x+" thing",res*); }
    }
    return res;
  }
}

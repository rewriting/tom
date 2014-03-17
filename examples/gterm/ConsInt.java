/*
 * Copyright (c) 2004-2014, Universite de Lorraine, Inria
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the Inria nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
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
package gterm;

public class ConsInt extends List {
  private static ConsInt proto = new ConsInt();
  private int hashCode;
  private ConsInt() {}

	private int headint;
	private List tail;

	public static ConsInt make(int headint, List tail) {
      proto.initHashCode(headint,tail);
      return (ConsInt) shared.SingletonSharedObjectFactory.getInstance().build(proto);
	}

  private void init(int headint, List tail,int hashCode) {
		this.headint = headint;
		this.tail = tail;
    this.hashCode = hashCode;
  }

  private void initHashCode(int headint, List tail) {
		this.headint = headint;
		this.tail = tail;
    this.hashCode = this.hashFunction();
  }


  private String getName() {
    return "ConsInt";
  }

  private int getArity() {
    return 2;
  }

  /* shared.SharedObject */
	public int hashCode() {
		return this.hashCode;
	}
	
  public shared.SharedObject duplicate() {
    ConsInt clone = new ConsInt();
    clone.init(headint,tail,hashCode);
    return clone;
  }
  
	public boolean equivalent(shared.SharedObject obj) {
		if(obj instanceof ConsInt) {
			ConsInt peer = (ConsInt) obj;
			return headint==peer.getHeadInt() && tail==peer.getTail();
		}
		return false;
  }

  /* List */
	public boolean isConsInt() {
		return true;
	}
	public int getHeadInt() {
		return headint;
	}

	public List getTail() {
		return tail;
	}
	
  /* AbstractType */
	public aterm.ATerm toATerm() {
		return aterm.pure.SingletonFactory.getInstance().makeAppl(
				aterm.pure.SingletonFactory.getInstance().makeAFun(getName(),getArity(),false), 
				new aterm.ATerm[] {
					aterm.pure.SingletonFactory.getInstance().makeInt(getHeadInt()), // special case for builtin argument
					 getTail().toATerm()});
	}

	public static List fromTerm(aterm.ATerm trm) {
		if(trm instanceof aterm.ATermAppl) {
			aterm.ATermAppl appl = (aterm.ATermAppl) trm;
			if(proto.getName().equals(appl.getName())) {
				return make(
						((aterm.ATermInt)appl.getArgument(0)).getInt(), // special case for builtin argument
						List.fromTerm(appl.getArgument(1))
						);
			}
		}
		return null;
  }

  /* Strategy */
  public int getChildCount() {
		return 1; // skip arg0:builtin
  }

	public Strategy getChildAt(int index) {
		switch(index) {
			// skip arg0:builtin
			case 0: return getTail();
			default: throw new IndexOutOfBoundsException();
		}
  }

  public Strategy setChildAt(int index, Strategy v) {
		switch(index) {
			// skip arg0:builtin
			case 0: return make(getHeadInt(),(List)v);
			default: throw new IndexOutOfBoundsException();
		}
  }

  /* internal use */
  protected int hashFunction() {
    int a, b, c;

    /* Set up the internal state */
    a = b = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    c = getArity();
    /*---------------------------------------- handle most of the key */

    /*------------------------------------- handle the last 11 bytes */
    //b += (stringHashFunction(getName(),getArity()) << 8);

		a += (headint << 8); // special case for builtin argument
		a += (tail.hashCode());
    
		/* case 0: nothing left to add */
    a -= b;
    a -= c;
    a ^= (c >> 13);
    b -= c;
    b -= a;
    b ^= (a << 8);
    c -= a;
    c -= b;
    c ^= (b >> 13);
    a -= b;
    a -= c;
    a ^= (c >> 12);
    b -= c;
    b -= a;
    b ^= (a << 16);
    c -= a;
    c -= b;
    c ^= (b >> 5);
    a -= b;
    a -= c;
    a ^= (c >> 3);
    b -= c;
    b -= a;
    b ^= (a << 10);
    c -= a;
    c -= b;
    c ^= (b >> 15);

    /*-------------------------------------------- report the result */
    return c;
  }

}

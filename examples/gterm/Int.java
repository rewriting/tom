/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
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

public class Int extends Element {
  private static Int proto = new Int();
	private int hashCode;

	private int value;

  private Int() {}

	public boolean isInt() {
		return true;
	}

	public static Int make(int value) {
      proto.initHashCode(value);
      return (Int) shared.SingletonSharedObjectFactory.getInstance().build(proto);
	}

  private void init(int value,int hashCode) {
		this.value = value;
    this.hashCode = hashCode;
  }

  private void initHashCode(int value) {
		this.value = value;
    this.hashCode = this.hashFunction();
  }

	public int hashCode() {
		return this.hashCode;
	}
	
  public shared.SharedObject duplicate() {
    Int clone = new Int();
    clone.init(value,hashCode);
    return clone;
  }
  
	public boolean equivalent(shared.SharedObject obj) {
		if(obj instanceof Int) {
			Int peer = (Int) obj;
			return value == peer.getValue();
		}
		return false;
  }

	public int getValue() {
		return value;
	}

  public String getName() {
    return "Int";
  }

  public int getArity() {
    return 1;
  }

  public int getChildCount() {
    return 0;
  }

	public aterm.ATerm toATerm() {
		return aterm.pure.SingletonFactory.getInstance().makeAppl(
				aterm.pure.SingletonFactory.getInstance().makeAFun(getName(),getArity(),false), 
				new aterm.ATerm[] {
					aterm.pure.SingletonFactory.getInstance().makeInt(getValue()) // special case for builtin argument
        });
	}
	
	public static Element fromTerm(aterm.ATerm trm) {
		if(trm instanceof aterm.ATermAppl) {
			aterm.ATermAppl appl = (aterm.ATermAppl) trm;
			if(proto.getName().equals(appl.getName())) {
				return make(
						((aterm.ATermInt)appl.getArgument(0)).getInt() // special case for builtin argument
						);
			}
		}
		return null;
  }

	public Strategy getChildAt(int index) {
		switch(index) {
			// skip arg0:builtin
			default: throw new IndexOutOfBoundsException();
		}
  }

  public Strategy setChildAt(int index, Strategy v) {
		switch(index) {
			// skip arg0:builtin
			default: throw new IndexOutOfBoundsException();
		}
  }

  protected int hashFunction() {
    int a, b, c;

    /* Set up the internal state */
    a = b = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    c = getArity();
    /*---------------------------------------- handle most of the key */

    /*------------------------------------- handle the last 11 bytes */
    //b += (stringHashFunction(getName(),getArity()) << 8);
    
		a += value; // special case for builtin argument

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

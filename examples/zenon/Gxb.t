
package zenon;

import aterm.*;
import aterm.pure.*;
import zenon.gxb.abg.*;
import zenon.gxb.abg.types.*;

import java.io.*;

class Gxb {
	%vas{
		module Abg	
	
		public sorts Hop

		abstract syntax
			a -> Hop
			b -> Hop
			g(left:Hop,right:Hop) -> Hop
	}

	private zenon.gxb.abg.AbgFactory factory;

	Gxb() {
		factory = AbgFactory.getInstance(SingletonFactory.getInstance());
	}

	protected final AbgFactory getAbgFactory() {
		return factory;
	}

	public static void main(String[] args) {
		Gxb essai = new Gxb();
		essai.test();
	}

	void test() {
		Hop test = `g(a(),b());
		%match(Hop test) {
			g(x,b()) -> { System.out.println(x); }
		}

}

}

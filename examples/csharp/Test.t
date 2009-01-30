using tom.library.sl;
using term.types;
using System;

public class Test {

  %include { term/term.tom }  
  %include { sl.tom }

  %strategy Replace() extends Identity() {
    visit Term {
      a() -> { return `b(); }
    }
  }

	public static int Main (string[] args) {
		Term t = `f(a(),b());
		Console.WriteLine(t.toString());
    %match (t) {
      f(x,y) -> { Console.WriteLine("x = {0}, y = {1}",`x,`y); }
    }
    Console.WriteLine("After Replace: " + ((Term) `TopDown(Replace()).visit(t)).toString());
		return 0;
	}
}




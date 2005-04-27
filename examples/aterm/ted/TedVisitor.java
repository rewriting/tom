import java.io.*;
import aterm.*;
import tom.library.strategy.mutraveler.*;


public class TedVisitor extends aterm.Visitor {

    Ted ted = new Ted();
    ATerm tomatch;

    public TedVisitor(ATerm tomatch) {
	super();
	this.tomatch = tomatch;
    }

    public void visitATerm(ATerm arg) {
	if (ted.match(tomatch, arg))
	    System.out.println("matched !");
    }
}


  /*  * Copyright (c) 2005-2014, Universite de Lorraine, Inria
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
  * by Germain Faure
  */


package lambdaparallel;

import lambdaparallel.parallellamterm.types.*;
import tom.library.sl.Strategy;
import jjtraveler.VisitFailure;
import java.io.*;

public class Parallellamcal{
	private static int comptVariable = 0;	
	%include { sl.tom }
	%include { parallellamterm/Parallellamterm.tom }
	

	public final static void main(String[] args) {
		Parallellamterm subject = `var("undefined");
		Strategy beta = `reductionRules();
		Strategy print = `print();
		String s;
		System.out.println(" ******************************************************************\n lambda Parallel\n By Germain Faure\n  It is under development and is definitevely not stable nor deliverable. \n ******************************************************************");
     ParallelllamcalLexer lexer = new ParallelllamcalLexer(System.in); // Create parser attached to lexer
     ParallellamcalParser parser = new ParallellamcalParser(lexer);
		while(true){
			System.out.print("laP>");
      try {
				subject = parser.parallellamterm();
				System.out.println(prettyPrinter(subject));
			} catch (Exception e) {
				System.out.println(e);
				
			}
			try{
				System.out.println("After beta-normalisation: "+`Sequence(Innermost(beta),print).visit(subject));
			} 
			catch(tom.library.sl.VisitFailure e) {
				System.out.println("reduction failed on: " + subject);
			}
		}
	}
	
	%strategy print() extends `Identity() {
		visit Parallellamterm {
			X -> {System.out.println(prettyPrinter(`X));}
		}
	}
	%strategy reductionRules() extends `Fail() {
		visit Parallellamterm {
			app(abs(X@var[],M),N) -> {return `substitute(X,N,M);}
		}
	}
	//[subject/X]t
	public static Parallellamterm substitute(Parallellamterm X, Parallellamterm subject, Parallellamterm t){
		%match(Parallellamterm t){
			var(name) -> {
				%match(Parallellamterm X){
					var(nameSubject)-> {
						if (`name.equals(`nameSubject)){
							return `subject;
						}
					}

				}
				return `t;
			}
			app(A1,A2) -> {return `app(substitute(X,subject,A1),substitute(X,subject,A2));}
			abs(x,A1) -> {
				Parallellamterm newx=`var("_x"+(++comptVariable));
				return `abs(newx,substitute(X,subject,(substitute(x,newx,A1))));
			}
		}
		return `subject;
	}
	public static String prettyPrinter(Parallellamterm t){
		%match(Parallellamterm t){
			app(term1,term2) -> {return "("+prettyPrinter(`term1)+"."+prettyPrinter(`term2)+")";}
			abs(term1,term2) -> {return "("+prettyPrinter(`term1)+"->"+prettyPrinter(`term2)+")";}
			parallel(x,xs*) -> {return "{"+prettyPrinter(`x)+prettyPrinterBis(`parallel(xs*))+"}";//si vide je renvoie ""
			}
			var(s) -> {return `s;}
		}
    return "";
	}
	public static String prettyPrinterBis(Parallellamterm t){
		%match(Parallellamterm t){
			app(term1,term2) -> {return "("+prettyPrinter(`term1)+"."+prettyPrinter(`term2)+")";}
			abs(term1,term2) -> {return "("+prettyPrinter(`term1)+"->"+prettyPrinter(`term2)+")";}
			parallel(x,xs*) -> {return ","+prettyPrinter(`x)+prettyPrinterBis(`parallel(xs*));}
			var(s) -> {return `s;}
		}
    return "";
	}

}

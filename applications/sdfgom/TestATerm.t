import testaterm.term.types.*;

import aterm.*;
import aterm.pure.*;

public class TestATerm {
 %gom{
   module Term
     abstract syntax
     Term = f(t1:TermList,t2:TermList)
          | g(tl:TermList)
     A = a()
     B = b()

     TermList = conc1( A* )
              | conc2( B* )
              | conc3( Term* )
              | conc4( A* )

 }        

 public static void main(String[] args) {
   PureFactory factory = SingletonFactory.getInstance();
   ATerm at = factory.parse("g([f([a()],[b()])])");
   System.out.println("at = " + at);

   Term t = Term.fromTerm(at);
   System.out.println("t = " + t);
   System.out.println("t.toATerm = " + t.toATerm());
 } 

} 



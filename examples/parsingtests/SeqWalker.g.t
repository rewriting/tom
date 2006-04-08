header {
package parsingtests;

}
{
	import parsingtests.seq.*;
	import parsingtests.seq.types.*;
}

//{{{ class SeqTreeWalker extends TreeParser;
class SeqTreeParser extends TreeParser;

options {
	importVocab=SeqParser;
}

{
  %include { seq/Seq.tom }
}

seq returns [Sequent s]
{
  ListPred a,b;
  s = null;  
}
  : #(SEQ a=list_pred b=list_pred) 
    {
		s = `seq(a,b); 
    }
  ;

list_pred returns [ListPred l]
{
	l = `concPred();
	ListPred left,right;
	
	Pred a,b;
}
  : a = pred 
  	{
		l = `concPred(a,l*);		
	}	  
    | #(LIST a=pred right=list_pred) 
    {
    	l = `concPred(a,right*);
    }    
 
  ; 
  
pred returns [Pred p]
    { 
      Pred a,b;
      p = null;
    }
      : i:ID {p = Pred.fromTerm( aterm.pure.SingletonFactory.getInstance().parse(
                      (String)String.valueOf(i.getText()))); } 
      | #(IMPL a=pred b=pred )  {p = `impl(a,b);}
      | #(OR a=pred b=pred ) {p = `vee(a,b);}
      | #(AND a=pred b=pred ) {p = `wedge(a,b);}
      | #(NOT a=pred ) {p = `neg(a);}
      
      ;  


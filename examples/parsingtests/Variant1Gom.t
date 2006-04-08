package parsingtests;

import java.io.DataInputStream;
import java.io.FileInputStream;

import antlr.collections.AST;

import aterm.*;
import aterm.pure.*;

import parsingtests.seq.*;
import parsingtests.seq.types.*;

public class Variant1Gom {	
	
	%include { Mapping.tom }	
	%include { seq/Seq.tom }	
	
	public Sequent genFinalTree(ATermAST t){
		return getFinalGomTree(t.genATermFromAST(TokenTable.getTokenMap()));		
	}
	
	public Sequent getFinalGomTree(ATerm n){
		
		%match(ATerm n){
			SEQ(_,(N1,N2)) ->{				
				return `seq(getListGom(N1),getListGom(N2));
			}			
		}		
		return null; //should never reach here
	}
	
	private ListPred getListGom(ATerm n){
		
		%match(ATerm n){
			LIST(_,(N1,N2)) -> {
				ListPred l = getListGom(`N2);
				return `concPred(getNodeGom(N1),l*);
			}
			_ -> {				
					return `concPred(getNodeGom(n));
			}
			
		}
		
		return null; //should never reach here
	}
	
	private Pred getNodeGom(ATerm n){
		
		%match(ATerm n){
			IMPL(_,(N1,N2)) ->{
				return `impl(getNodeGom(N1),getNodeGom(N2));
			}
			AND(_,(N1,N2)) ->{
				return `wedge(getNodeGom(N1),getNodeGom(N2));
			}
			OR(_,(N1,N2)) ->{
				return `vee(getNodeGom(N1),getNodeGom(N2));
			}
			NOT(_,(N1,_*)) ->{
				return `neg(getNodeGom(N1));
			}
			ID(NodeInfo[text=text],_) ->{
				return Pred.fromTerm(
            aterm.pure.SingletonFactory.getInstance().parse(`text));
			}
		}
    throw new RuntimeException("Unable to translate "+n);
	}
}

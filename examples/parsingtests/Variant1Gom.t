package parsingtests;
//
//import java.io.DataInputStream;
//import java.io.FileInputStream;
//
//import antlr.collections.AST;
//
//import parsingtests.seq.*;
//import parsingtests.seq.types.*;
//
//public class Variant1Gom {	
//	
//	%include { seq/Seq.tom }	
//	
//	public Sequent genFinalTree(AST t){
//		return getFinalGomTree(genGomMappingFromAST(t));		
//	}
//	
//	public Sequent getFinalGomTree(Node n){
//		
//		%match(Node n){
//			node(4,_,concNode(N1,N2)) ->{				
//				return `seq(getListGom(N1),getListGom(N2));
//			}			
//		}		
//		return null; //should never reach here
//	}
//	
//	private ListPred getListGom(Node n){
//		
//		%match(Node n){
//			node(6,_,concNode(N1,N2)) -> {
//				ListPred l = getListGom(`N2);
//				return `concPred(getNodeGom(N1),l*);
//			}
//			_ -> {				
//					return `concPred(getNodeGom(n));
//			}
//			
//		}
//		
//		return null; //should never reach here
//	}
//	
//	private Pred getNodeGom(Node n){
//		
//		%match(Node n){
//			node(7,_,concNode(N1,N2)) ->{
//				return `impl(getNodeGom(N1),getNodeGom(N2));
//			}
//			node(8,_,concNode(N1,N2)) ->{
//				return `wedge(getNodeGom(N1),getNodeGom(N2));
//			}
//			node(9,_,concNode(N1,N2)) ->{
//				return `vee(getNodeGom(N1),getNodeGom(N2));
//			}
//			node(12,_,concNode(N1,_*)) ->{
//				return `neg(getNodeGom(N1));
//			}
//			node(13,text,_) ->{
//				return Pred.fromTerm( aterm.pure.SingletonFactory.getInstance().parse(
//	                      (String)String.valueOf(`text)));
//			}
//		}
//		
//		return null; //should never reach here
//	}
//	
//	
//	public Node genGomMappingFromAST(AST t){
//	
//		return `node(t.getType(),t.getText(),getGomChildrenForAST(t));
//	}
//
//	// returns a list with the children for this node
//	private ListNode getGomChildrenForAST(AST t){
//		
//		ListNode children = `concNode();	
//		
//		if (t == null ){
//			return children;	
//		}
//
//		AST tmp = t.getFirstChild();
//		// only if the node has children
//		if (tmp != null) {			
//			
//			children = `concNode(children*,node(tmp.getType(),tmp.getText(),getGomChildrenForAST(tmp)));
//			
//			while((tmp = tmp.getNextSibling())!=null){				
//				children = `concNode(children*,node(tmp.getType(),tmp.getText(),getGomChildrenForAST(tmp)));
//			}
//		}
//		
//		return children;		
//	}
//}

package pom;

import tom.pom.ATermAST;

import aterm.*;
import aterm.pure.*;

import pom.seq.*;
import pom.seq.types.*;

public class AST2Gom {

  %include { seq/Seq.tom }
  %include { Mapping.tom }

  public static Sequent genFinalTree(ATermAST t){
    return getFinalGomTree(t.genATermFromAST(TokenTable.getTokenMap()));
  }

  public static Sequent getFinalGomTree(ATerm n) {
    %match(ATerm n){
      SEQ(_,(N1,N2)) ->{
        return `seq(getListGom(N1),getListGom(N2));
      }
    }
    throw new RuntimeException("Unable to translate SEQ " + n);
  }

  private static ListPred getListGom(ATerm n){
    %match(ATerm n){
      LIST(_,(N1,N2)) -> {
        ListPred l = getListGom(`N2);
        return `concPred(getNodeGom(N1),l*);
      }
      _ -> {
        return `concPred(getNodeGom(n));
      }
    }
    throw new RuntimeException("Unable to translate LIST " + n);
  }

  private static Pred getNodeGom(ATerm n){
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
      System.out.println("aterm: " + aterm.pure.SingletonFactory.getInstance().parse(`text));
        return Pred.fromTerm(
            aterm.pure.SingletonFactory.getInstance().parse(`text));
      }
    }
    throw new RuntimeException("Unable to translate " + n);
  }
}

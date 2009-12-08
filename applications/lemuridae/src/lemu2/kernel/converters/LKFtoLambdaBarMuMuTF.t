package lemu2.kernel.converters;

import lemu2.kernel.proofterms.types.*;
import lemu2.kernel.*;
import lemu2.util.*;

public class LKFtoLambdaBarMuMuTF {

  %include { kernel/proofterms/proofterms.tom } 

  public static LMMTerm convert(ProofTerm pt) {
    %match(pt) {
      rootR(RootRPrem1(a,pa,M)) -> {
        return `lmmMu(LmmMu(a,pa,conv(M)));
      }
    }
    return null;
  }

  private static LMMCommand conv(ProofTerm pt) {
    %match(pt) {
      ax(x,a) -> { return `lmmCommand(lmmVar(x),lmmCoVar(a)); }
      implyR(ImplyRPrem1(x,px,a,pa,M),b) -> { 
        return `lmmCommand(
                  lmmLam(LmmLam(x,px,lmmMu(LmmMu(a,pa,conv(M))))),
                  lmmCoVar(b));
      }
      implyL(ImplyLPrem1(x,px,M),ImplyLPrem2(a,pa,N),y) -> {
        return `lmmCommand(
                  lmmVar(y),
                  lmmDot(lmmMu(LmmMu(a,pa,conv(N))),
                         lmmMuT(LmmMuT(x,px,conv(M)))));
      }
      forallR(ForallRPrem1(a,pa,fx,M),b) -> {
        return `lmmCommand(
                  lmmFLam(LmmFLam(fx,lmmMu(LmmMu(a,pa,conv(M))))),
                  lmmCoVar(b));
      }
      forallL(ForallLPrem1(x,px,M),t,y) -> {
        return `lmmCommand(
                  lmmVar(y),
                  lmmFDot(t,lmmMuT(LmmMuT(x,px,conv(M)))));
      }
      cut(CutPrem1(a,pa,M),CutPrem2(x,px,N)) -> {
        return `lmmCommand(
                  lmmMu(LmmMu(a,pa,conv(M))),
                  lmmMuT(LmmMuT(x,px,conv(N))));
      }
      foldR(rule,FoldRPrem1(a,pa,M),b) -> {
        return `lmmCommand(
                  lmmFoldR(rule,lmmMu(LmmMu(a,pa,conv(M)))),
                  lmmCoVar(b));
      }
      foldL(rule,FoldLPrem1(x,px,M),y) -> {
        return `lmmCommand(
                  lmmVar(y),
                  lmmFoldL(rule,lmmMuT(LmmMuT(x,px,conv(M)))));
      }
    }
    return null;
  }
}

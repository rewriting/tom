package lemu2.kernel.converters;

import lemu2.kernel.proofterms.types.*;
import lemu2.kernel.*;
import lemu2.util.*;

//import java.util.Collection;
//import tom.library.freshgom.*;
//import tom.library.sl.*;

public class LKMToLKF {
  
  %include { kernel/proofterms/proofterms.tom } 
  //%include { sl.tom } 

  private static ProofTerm 
    eta(NProp left, CNProp right, PropRewriteRules prs, FoVarList free) {
      %match(left, right) {
        nprop(x,A@relApp[]), cnprop(a,B@relApp[]) -> {
          if (U.`alpha(A,B,free)) return `ax(x,a);
        }
        nprop(n,A@relApp[]), _ -> {
          PropRewriteRule rule = Rewriting.rewrites(`A,prs);
          if (rule != null) {
            String ruleid = rule.getid();
            Prop AA = Rewriting.rewrite(`A,rule);
            Name fresh = Name.freshName(`n);
            ProofTerm prem = `eta(nprop(fresh,AA),right,prs,free);
            return `foldL(ruleid,FoldLPrem1(fresh,AA,prem),n);
          }
        }
        _, cnprop(cn,B@relApp[]) -> {
          PropRewriteRule rule = Rewriting.rewrites(`B,prs);
          if (rule != null) {
            String ruleid = rule.getid();
            Prop BB = Rewriting.rewrite(`B,rule);
            CoName fresh = CoName.freshCoName(`cn);
            ProofTerm prem = `eta(left,cnprop(fresh,BB),prs,free);
            return `foldR(ruleid,FoldRPrem1(fresh,BB,prem),cn);
          }
        }
        nprop(x,and(A1,B1)), cnprop(a,and(A2,B2)) -> {
          Name y = Name.freshName(`x);
          Name z = Name.freshName(`x);
          CoName b = CoName.freshCoName(`a);
          CoName c = CoName.freshCoName(`a);
          ProofTerm prem1 = `eta(nprop(y,A1),cnprop(b,A2),prs,free);
          ProofTerm prem2 = `eta(nprop(z,B1),cnprop(c,B2),prs,free);
          return `andL(AndLPrem1(
                    y,A1,z,B1,andR(
                      AndRPrem1(b,A2,prem1),
                      AndRPrem2(c,B2,prem2),a)),x);
        }
        nprop(x,or(A1,B1)), cnprop(a,or(A2,B2)) -> {
          Name y = Name.freshName(`x);
          Name z = Name.freshName(`x);
          CoName b = CoName.freshCoName(`a);
          CoName c = CoName.freshCoName(`a);
          ProofTerm prem1 = `eta(nprop(y,A1),cnprop(b,A2),prs,free);
          ProofTerm prem2 = `eta(nprop(z,B1),cnprop(c,B2),prs,free);
          return `orR(OrRPrem1(
                    b,A2,c,B2,orL(
                      OrLPrem1(y,A2,prem1),
                      OrLPrem2(z,B2,prem2),x)),a);
        }
        nprop(x,l@implies(A1,B1)), cnprop(a,r@implies(A2,B2)) -> {
          System.out.println("eta on : " + Pretty.pretty(`l) + " |- " + Pretty.pretty(`r));
          Name y = Name.freshName(`x);
          Name z = Name.freshName(`x);
          CoName b = CoName.freshCoName(`a);
          CoName c = CoName.freshCoName(`a);
          ProofTerm prem1 = `eta(nprop(z,B1),cnprop(b,B2),prs,free);
          ProofTerm prem2 = `eta(nprop(y,A2),cnprop(c,A1),prs,free);
          return `implyR(ImplyRPrem1(
                    y,A2,b,B2,implyL(
                      ImplyLPrem1(z,B1,prem1),
                      ImplyLPrem2(c,A1,prem2),x)),a);
        }
      }
      return null;
  }
  public static ProofTerm 
    convert(ProofTerm pt, PropRewriteRules prs) {
      return convert(pt,`seq(fovarList(),lctx(),rctx()),prs);
    }

  private static ProofTerm 
    convert(ProofTerm pt, Sequent se, PropRewriteRules prs) {
      %match(se) {
        seq(free,gamma,delta) -> {
          %match(pt) {
            rootR(RootRPrem1(a,A,M)) -> {
              Sequent se1 = `seq(free,gamma,rctx(cnprop(a,A),delta*));
              return `rootR(RootRPrem1(a,A,convert(M,se1,prs)));
            }
            ax(x,a) -> { 
              return `eta(nprop(x,U.lookup(gamma,x)),cnprop(a,U.lookup(delta,a)),prs,free); 
            }
            implyR(ImplyRPrem1(x,A,a,B,M),cn) -> {
              Prop P = U.`lookup(delta,cn);
              %match(P) {
                relApp[] -> {
                  PropRewriteRule rule = Rewriting.rewrites(`P,prs);
                  if (rule != null) {
                    String ruleid = rule.getid();
                    Prop PP = Rewriting.rewrite(`P,rule);
                    CoName fresh = CoName.freshCoName(`cn);
                    Sequent se1 = `seq(free,gamma,rctx(cnprop(fresh,PP),delta*));
                    ProofTerm prem = `convert(U.reconame(M,cn,fresh),se1,prs);
                    return `foldR(ruleid,FoldRPrem1(fresh,PP,prem),cn);
                  }
                }
                implies(A1,B1) -> {
                  Sequent se1 = 
                    `seq(free,lctx(nprop(x,A1),gamma*),rctx(cnprop(a,B1),delta*));
                  ProofTerm prem = `convert(M,se1,prs);
                  return `implyR(ImplyRPrem1(x,A1,a,B1,prem),cn);
                }
              } return null;
            }
          }
        }
      }
      return null;
  }

}

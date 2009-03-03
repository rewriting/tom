package lemu2.kernel.converters;

import lemu2.kernel.proofterms.types.*;
import lemu2.kernel.*;
import lemu2.util.*;

public class LKMtoLKF {
  
  %include { kernel/proofterms/proofterms.tom } 

  private static ProofTerm 
    eta(NProp left, CNProp right, PropRewriteRules prs, FoVarList free) {
      //System.out.println("eta on " + left.getn() + " " + Pretty.pretty(left.getp()) + " " + right.getcn() + " " + Pretty.pretty(right.getp()));
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
        nprop(x,forall(Fa(fx,A))), cnprop(a,forall(Fa(fy,B))) -> {
          Name y = Name.freshName(`x);
          CoName b = CoName.freshCoName(`a);
          FoVarList free1 = `fovarList(fy,free*);
          Prop A1 = U.`substFoVar(A,fx,var(fy));
          ProofTerm prem = `eta(nprop(y,A1),cnprop(b,B),prs,free1);
          return `forallR(ForallRPrem1(
                    b,B,fy,forallL(ForallLPrem1(
                      y,A1,prem),var(fy),x)),a);
        }
      }
      return null;
  }

  public static ProofTerm 
    convert(ProofTerm pt, PropRewriteRules prs) {
      return convert(pt,`seq(fovarList(),lctx(),rctx()),prs);
    }

  private static ProofTerm unfold(Prop P, ProofTerm M, CoName cn, FoVarList free, LCtx gamma, RCtx delta, PropRewriteRules prs) {
      PropRewriteRule rule = Rewriting.rewrites(P,prs);
      if (rule != null) {
        String ruleid = rule.getid();
        Prop PP = Rewriting.rewrite(P,rule);
        CoName fresh = CoName.freshCoName(cn);
        Sequent se1 = `seq(free,gamma,rctx(cnprop(fresh,PP),delta*));
        ProofTerm prem = convert(U.reconame(M,cn,fresh),se1,prs);
        return `foldR(ruleid,FoldRPrem1(fresh,PP,prem),cn);
      } else return null;
  }

  private static ProofTerm unfold(Prop P, ProofTerm M, Name n, FoVarList free, LCtx gamma, RCtx delta, PropRewriteRules prs) {
      PropRewriteRule rule = Rewriting.rewrites(P,prs);
      if (rule != null) {
        String ruleid = rule.getid();
        Prop PP = Rewriting.rewrite(P,rule);
        Name fresh = Name.freshName(n);
        Sequent se1 = `seq(free,lctx(nprop(fresh,PP),gamma*),delta);
        ProofTerm prem = convert(U.rename(M,n,fresh),se1,prs);
        return `foldL(ruleid,FoldLPrem1(fresh,PP,prem),n);
      } else return null;
  }

  private static ProofTerm 
    convert(ProofTerm pt, Sequent se, PropRewriteRules prs) {
      /*
      System.out.println("--------------------------------------------");
      System.out.println();
      System.out.println(Pretty.pretty(pt));
      System.out.println();
      System.out.println(Pretty.pretty(se));
      */
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
            cut(CutPrem1(a,A,M1),CutPrem2(x,B,M2)) -> {
              Sequent se1 = `seq(free,gamma,rctx(cnprop(a,A),delta*));
              ProofTerm prem1 = `convert(M1,se1,prs);
              Sequent se2 = `seq(free,lctx(nprop(x,A),gamma*),delta);
              ProofTerm prem2 = `convert(M2,se2,prs);
              return `cut(CutPrem1(a,A,prem1),CutPrem2(x,A,prem2));
            }
            trueR(cn) -> {
              Prop P = U.`lookup(delta,cn);
              %match(P) {
                relApp[] -> { return `unfold(P,pt,cn,free,gamma,delta,prs); }
                top() -> { return `trueR(cn); }
              } 
            }
            falseL(n) -> {
              Prop P = U.`lookup(gamma,n);
              %match(P) {
                relApp[] -> { return `unfold(P,pt,n,free,gamma,delta,prs); }
                bottom() -> { return `falseL(n); }
              } 
            }
            implyR(ImplyRPrem1(x,A,a,B,M),cn) -> {
              Prop P = U.`lookup(delta,cn);
              %match(P) {
                relApp[] -> { return `unfold(P,pt,cn,free,gamma,delta,prs); }
                implies(A1,B1) -> {
                  Sequent se1 = `seq(free,lctx(nprop(x,A1),gamma*),rctx(cnprop(a,B1),delta*));
                  ProofTerm prem = `convert(M,se1,prs);
                  return `implyR(ImplyRPrem1(x,A1,a,B1,prem),cn);
                }
              } 
            }
            implyL(ImplyLPrem1(x,B,M1),ImplyLPrem2(a,A,M2),n) -> {
              Prop P = U.`lookup(gamma,n);
              %match(P) {
                relApp[] -> { return `unfold(P,pt,n,free,gamma,delta,prs); }
                implies(A1,B1) -> {
                  Sequent se1 = `seq(free,lctx(nprop(x,B1),gamma*),delta);
                  Sequent se2 = `seq(free,gamma,rctx(cnprop(a,A1),delta*));
                  ProofTerm prem1 = `convert(M1,se1,prs);
                  ProofTerm prem2 = `convert(M2,se2,prs);
                  return `implyL(ImplyLPrem1(x,B1,prem1),ImplyLPrem2(a,A1,prem2),n);
                }
              } 
            }
            andR(AndRPrem1(a,A,M1),AndRPrem2(b,B,M2),cn) -> {
              Prop P = U.`lookup(delta,cn);
              %match(P) {
                relApp[] -> { return `unfold(P,pt,cn,free,gamma,delta,prs); }
                and(A1,B1) -> {
                  Sequent se1 = `seq(free,gamma,rctx(cnprop(a,A1),delta*));
                  Sequent se2 = `seq(free,gamma,rctx(cnprop(b,B1),delta*));
                  ProofTerm prem1 = `convert(M1,se1,prs);
                  ProofTerm prem2 = `convert(M2,se2,prs);
                  return `andR(AndRPrem1(a,A1,prem1),AndRPrem2(b,B1,prem2),cn);
                }
              } 
            }
            andL(AndLPrem1(x,A,y,B,M),n) -> {
              Prop P = U.`lookup(gamma,n);
              %match(P) {
                relApp[] -> { return `unfold(P,pt,n,free,gamma,delta,prs); }
                and(A1,B1) -> {
                  Sequent se1 = `seq(free,lctx(nprop(x,A1),nprop(y,B1),gamma*),delta);
                  ProofTerm prem1 = `convert(M,se1,prs);
                  return `andL(AndLPrem1(x,A1,y,B1,prem1),n);
                }
              } 
            }
            orR(OrRPrem1(a,A,b,B,M),cn) -> {
              Prop P = U.`lookup(delta,cn);
              %match(P) {
                relApp[] -> { return `unfold(P,pt,cn,free,gamma,delta,prs); }
                or(A1,B1) -> {
                  Sequent se1 = `seq(free,gamma,rctx(cnprop(a,A1),cnprop(b,B1),delta*));
                  ProofTerm prem1 = `convert(M,se1,prs);
                  return `orR(OrRPrem1(a,A1,b,B1,prem1),cn);
                }
              } 
            }
            orL(OrLPrem1(x,A,M1),OrLPrem2(y,B,M2),n) -> {
              Prop P = U.`lookup(gamma,n);
              %match(P) {
                relApp[] -> { return `unfold(P,pt,n,free,gamma,delta,prs); }
                or(A1,B1) -> {
                  Sequent se1 = `seq(free,lctx(nprop(x,A1),gamma*),delta);
                  Sequent se2 = `seq(free,lctx(nprop(y,B1),gamma*),delta);
                  ProofTerm prem1 = `convert(M1,se1,prs);
                  ProofTerm prem2 = `convert(M2,se2,prs);
                  return `orL(OrLPrem1(x,A1,prem1),OrLPrem2(y,B1,prem2),n);
                }
              }
            }
            forallR(ForallRPrem1(a,A,fx,M),cn) -> {
              Prop P = U.`lookup(delta,cn);
              %match(P) {
                relApp[] -> { return `unfold(P,pt,cn,free,gamma,delta,prs); }
                forall(Fa(fy,A1)) -> {
                  Sequent se1 = `seq(fovarList(fy,free),gamma,rctx(cnprop(a,A1),delta*));
                  ProofTerm prem1 = `convert(U.substFoVar(M,fx,var(fy)),se1,prs);
                  return `forallR(ForallRPrem1(a,A1,fy,prem1),cn);
                }
              } 
            }
            forallL(ForallLPrem1(x,A,M),ft,n) -> {
              Prop P = U.`lookup(gamma,n);
              %match(P) {
                relApp[] -> { return `unfold(P,pt,n,free,gamma,delta,prs); }
                forall(Fa(fx,A1)) -> {
                  Sequent se1 = `seq(free,lctx(nprop(x,U.substFoVar(A1,fx,ft)),gamma*),delta);
                  ProofTerm prem1 = `convert(U.substFoVar(M,fx,ft),se1,prs);
                  return `forallL(ForallLPrem1(x,U.substFoVar(A1,fx,ft),prem1),ft,n);
                }
              } 
            }
            existsR(ExistsRPrem1(a,A,M),ft,cn) -> {
              Prop P = U.`lookup(delta,cn);
              %match(P) {
                relApp[] -> { return `unfold(P,pt,cn,free,gamma,delta,prs); }
                exists(Ex(fx,A1)) -> {
                  Sequent se1 = `seq(free,gamma,rctx(cnprop(a,U.substFoVar(A1,fx,ft)),delta*));
                  ProofTerm prem1 = `convert(U.substFoVar(M,fx,ft),se1,prs);
                  return `existsR(ExistsRPrem1(a,U.substFoVar(A1,fx,ft),prem1),ft,cn);
                }
              } 
            }
            existsL(ExistsLPrem1(x,A,fx,M),n) -> {
              Prop P = U.`lookup(gamma,n);
              %match(P) {
                relApp[] -> { return `unfold(P,pt,n,free,gamma,delta,prs); }
                exists(Ex(fy,A1)) -> {
                  Sequent se1 = `seq(fovarList(fx,free*),lctx(nprop(x,A1),gamma*),delta);
                  ProofTerm prem1 = `convert(U.substFoVar(M,fx,var(fy)),se1,prs);
                  return `existsL(ExistsLPrem1(x,A1,fy,prem1),n);
                }
              } 
            }
          }
        }
      }
      return null;
    }

}

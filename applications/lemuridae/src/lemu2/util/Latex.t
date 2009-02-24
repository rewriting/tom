package lemu2.util;

import java.io.*;
import lemu2.kernel.proofterms.types.*;
import lemu2.kernel.proofterms.types.rawnamelist.RawnameList;
import lemu2.kernel.proofterms.types.rawconamelist.RawconameList;


public class Latex {

  %include { kernel/proofterms/proofterms.tom } 

  private static RawconameList getFreeCoNames(RawconameList c, RawProofTerm pt) {
    %match (pt) {
      Rawax(_,cn) -> {
        return (RawconameList) (c.contains(`cn) ? `RawconameList() : `RawconameList(cn));
      }
      Rawcut(RawCutPrem1(a,_,M1),RawCutPrem2(_,_,M2)) -> {
        RawconameList M1conames = `getFreeCoNames(RawconameList(a,c*),M1);
        RawconameList M2conames = `getFreeCoNames(c,M2);
        return (RawconameList) `RawconameList(M1conames*,M2conames*); 
      }
      // left rules
      RawandL(RawAndLPrem1(_,_,_,_,M),n) -> {
        return `getFreeCoNames(c,M);
      }
      RaworL(RawOrLPrem1(_,_,M1),RawOrLPrem2(_,_,M2),n) -> {
        RawconameList M1conames = `getFreeCoNames(c,M1);
        RawconameList M2conames = `getFreeCoNames(c,M2);
        return (RawconameList) `RawconameList(M1conames*,M2conames*); 
      }
      RawimplyL(RawImplyLPrem1(_,_,M1),RawImplyLPrem2(a,_,M2),n) -> {
        RawconameList M1names = `getFreeCoNames(c,M1);
        RawconameList M2names = `getFreeCoNames(RawconameList(a,c*),M2);
        return (RawconameList) `RawconameList(M1names*,M2names*); 
      }
      RawforallL(RawForallLPrem1(_,_,M),_,n) -> {
        return `getFreeCoNames(c,M);
      }
      RawexistsL(RawExistsLPrem1(_,_,_,M),n) -> {
        return `getFreeCoNames(c,M);
      }
      RawrootL(RawRootLPrem1(_,_,M)) -> {
        return `getFreeCoNames(c,M);
      }
      RawfoldL(_,RawFoldLPrem1(_,_,M),n) -> {
        return `getFreeCoNames(c,M);
      }
      // right rules
      RaworR(RawOrRPrem1(a,_,b,_,M),cn) -> {
        RawconameList Mconames = `getFreeCoNames(RawconameList(a,b,c*),M);
        return (RawconameList) (c.contains(`cn) ? Mconames : `RawconameList(cn,Mconames*)); 
      }
      RawandR(RawAndRPrem1(a,_,M1),RawAndRPrem2(b,_,M2),cn) -> {
        RawconameList M1conames = `getFreeCoNames(RawconameList(a,c*),M1);
        RawconameList M2conames = `getFreeCoNames(RawconameList(b,c*),M2);
        return (RawconameList) (c.contains(`cn)  ? `RawconameList(M1conames*,M2conames*) : `RawconameList(cn,M1conames*,M2conames*)); 
      }
      RawimplyR(RawImplyRPrem1(_,_,a,_,M),cn) -> {
        RawconameList Mconames = `getFreeCoNames(RawconameList(a,c*),M);
        return (RawconameList) (c.contains(`cn) ? Mconames : `RawconameList(cn,Mconames*)); 
      }
      RawexistsR(RawExistsRPrem1(a,_,M),_,cn) -> {
        RawconameList Mconames = `getFreeCoNames(RawconameList(a,c*),M);
        return (RawconameList) (c.contains(`cn) ? Mconames : `RawconameList(cn,Mconames*)); 
      }
      RawforallR(RawForallRPrem1(a,_,_,M),cn) -> {
        RawconameList Mconames = `getFreeCoNames(RawconameList(a,c*),M);
        return (RawconameList) (c.contains(`cn) ? Mconames : `RawconameList(cn,Mconames*)); 
      }
      RawrootR(RawRootRPrem1(a,_,M)) -> {
        return `getFreeCoNames(RawconameList(a,c*),M);
      }
      RawfoldR(_,RawFoldRPrem1(a,_,M),cn) -> {
        RawconameList Mconames = `getFreeCoNames(RawconameList(a,c*),M);
        return (RawconameList) (c.contains(`cn) ? Mconames : `RawconameList(cn,Mconames*)); 
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static RawnameList getFreeNames(RawnameList c, RawProofTerm pt) {
    %match (pt) {
      Rawax(n,_) -> {
        return (RawnameList) (c.contains(`n) ? `RawnameList() : `RawnameList(n));
      }
      Rawcut(RawCutPrem1(_,_,M1),RawCutPrem2(x,_,M2)) -> {
        RawnameList M1names = `getFreeNames(c,M1);
        RawnameList M2names = `getFreeNames(RawnameList(x,c*),M2);
        return (RawnameList) `RawnameList(M1names*,M2names*); 
      }
      // left rules
      RawandL(RawAndLPrem1(x,_,y,_,M),n) -> {
        RawnameList Mnames = `getFreeNames(RawnameList(x,y,c*),M);
        return (RawnameList) (c.contains(`n) ? Mnames : `RawnameList(n,Mnames*)); 
      }
      RaworL(RawOrLPrem1(x,_,M1),RawOrLPrem2(y,_,M2),n) -> {
        RawnameList M1names = `getFreeNames(RawnameList(x,c*),M1);
        RawnameList M2names = `getFreeNames(RawnameList(y,c*),M2);
        return (RawnameList) (c.contains(`n) ? `RawnameList(M1names*,M2names*) : `RawnameList(n,M1names*,M2names*));
      }
      RawimplyL(RawImplyLPrem1(x,_,M1),RawImplyLPrem2(_,_,M2),n) -> {
        RawnameList M1names = `getFreeNames(RawnameList(x,c*),M1);
        RawnameList M2names = `getFreeNames(c,M2);
        return (RawnameList) (c.contains(`n) ? `RawnameList(M1names*,M2names*) : `RawnameList(n,M1names*,M2names*)); 
      }
      RawforallL(RawForallLPrem1(x,_,M),_,n) -> {
        RawnameList Mnames = `getFreeNames(RawnameList(x,c*),M);
        return (RawnameList) (c.contains(`n) ? Mnames : `RawnameList(n,Mnames*)); 
      }
      RawexistsL(RawExistsLPrem1(x,_,_,M),n) -> {
        RawnameList Mnames = `getFreeNames(RawnameList(x,c*),M);
        return (RawnameList) (c.contains(`n) ? Mnames : `RawnameList(n,Mnames*)); 
      }
      RawrootL(RawRootLPrem1(x,_,M)) -> {
        return `getFreeNames(RawnameList(x,c*),M);
      }
      RawfoldL(_,RawFoldLPrem1(x,_,M),n) -> {
        RawnameList Mnames = `getFreeNames(RawnameList(x,c*),M);
        return (RawnameList) (c.contains(`n) ? Mnames : `RawnameList(n,Mnames*)); 
      }
      // right rules
      RaworR(RawOrRPrem1(_,_,_,_,M),cn) -> {
        return `getFreeNames(c,M);
      }
      RawandR(RawAndRPrem1(_,_,M1),RawAndRPrem2(_,_,M2),cn) -> {
        RawnameList M1names = `getFreeNames(c,M1);
        RawnameList M2names = `getFreeNames(c,M2);
        return (RawnameList) `RawnameList(M1names*,M2names*); 
      }
      RawimplyR(RawImplyRPrem1(x,_,_,_,M),cn) -> {
        return `getFreeNames(RawnameList(x,c*),M);
      }
      RawexistsR(RawExistsRPrem1(_,_,M),_,cn) -> {
        return `getFreeNames(c,M);
      }
      RawforallR(RawForallRPrem1(_,_,_,M),cn) -> {
        return `getFreeNames(c,M);
      }
      RawrootR(RawRootRPrem1(_,_,M)) -> {
        return `getFreeNames(c,M);
      }
      RawfoldR(_,RawFoldRPrem1(_,_,M),cn) -> {
        return `getFreeNames(c,M);
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static RawconameList getFreeCoNames(RawProofTerm pt) {
    return `getFreeCoNames((RawconameList) RawconameList(),pt);
  }

  private static RawnameList getFreeNames(RawProofTerm pt) {
    return `getFreeNames((RawnameList) RawnameList(),pt);
  }

  private static RawRCtx clean(RawProofTerm pt, RawRCtx ctx) {
		RawconameList cnl = getFreeCoNames(pt);
		RawRCtx res = `Rawrctx();
		%match(ctx) {
			Rawrctx(_*,Rawcnprop(cn,p),_*) -> {
				if(cnl.contains(`cn)) res = `Rawrctx(res*,Rawcnprop(cn,p));
			}
		}
    return res;
  }

  private static RawLCtx clean(RawProofTerm pt, RawLCtx ctx) {
		RawnameList nl = getFreeNames(pt);
		RawLCtx res = `Rawlctx();
		%match(ctx) {
			Rawlctx(_*,Rawnprop(n,p),_*) -> {
				if(nl.contains(`n)) res = `Rawlctx(res*,Rawnprop(n,p));
			}
		}
    return res;
  }

  private static RawSequent clean(RawProofTerm pt, RawSequent seq) {
		%match(seq) {
			Rawseq(free,gamma,delta) -> {
				return `Rawseq(free,clean(pt,gamma),clean(pt,delta));
			}
		}
		throw new RuntimeException("non exhaustive patterns");
  }

  private static class ConversionError extends Exception { }

  private static int peanoToInt(RawTerm t) throws ConversionError {
    %match(t) {
      RawfunApp("z",()) -> { return 0; }
      RawfunApp("s",(n)) -> { return 1+`peanoToInt(n); }
    }
    throw new ConversionError();
  }

	private static String toTex(RawTerm t) {
		%match(t) {
      Rawvar(x) -> { return `x;}
      // arithmetic toTex print
      RawfunApp("z",()) -> { return "0"; }
      i@RawfunApp("s",_) -> {
        try { return Integer.toString(peanoToInt(`i));}
        catch (ConversionError e) { }
      }
      // normal case
      RawfunApp(name,x) -> { return %[@`name@(@`toTex(x)@)]%; }
    }
    throw new RuntimeException("non exhaustive patterns"); 
	}

	private static String toTex(RawTermList tl) {
    %match(tl) {
      RawtermList() -> { return ""; }
      RawtermList(x) -> { return `toTex(x); }
      RawtermList(h,t*) -> { return %[@`toTex(h)@,@toTex(`t)@]%; }
    }
    throw new RuntimeException("non exhaustive patterns"); 
	}

	private static String toTex(RawProp p) {
		%match(p) {
      Rawimplies(p1@RawrelApp[],p2) -> { return %[@`toTex(p1)@ \Rightarrow @`toTex(p2)@]% ; }
      Rawimplies(p1,p2) -> { return %[(@`toTex(p1)@) \Rightarrow @`toTex(p2)@]% ; }
      Rawor(p1@RawrelApp[],p2) -> { return %[@`toTex(p1)@ \lor @`toTex(p2)@]%; }
      Rawor(p1,p2) -> { return %[(@`toTex(p1)@) \lor @`toTex(p2)@]%; }
      Rawand(p1@RawrelApp[],p2) -> { return %[@`toTex(p1)@ \land @`toTex(p2)@]%; }
      Rawand(p1,p2) -> { return %[(@`toTex(p1)@) \land @`toTex(p2)@]%; }
      Rawforall(RawFa(x,p1@RawrelApp[])) -> { return %[\forall @`x@. @`toTex(p1)@]%; }
      Rawforall(RawFa(x,p1)) -> { return %[\forall @`x@. (@`toTex(p1)@)]%; }
      Rawexists(RawEx(x,p1@RawrelApp[])) -> { return %[\exists @`x@. @`toTex(p1)@]%; }
      Rawexists(RawEx(x,p1)) -> { return %[\exists @`x@. (@`toTex(p1)@)]%; }
      RawrelApp(r,()) -> { return `r; }
      // arithmetic toTex print
      RawrelApp("eq",(x,y)) -> { return %[@`toTex(x)@ = @`toTex(y)@]%; }
      // set theory toTexprint
      RawrelApp("in",(x,y)) -> { return %[@`toTex(x)@ \in @`toTex(y)@]%; }
      RawrelApp(r,x) -> { return %[@`r@(@`toTex(x)@)]%; }
      Rawbottom() -> { return "\\bot"; }
      Rawtop() -> { return "\\top"; }
    }
    throw new RuntimeException("non exhaustive patterns"); 
	}

	private static String toTex(RawLCtx ctx) {
    %match(ctx) {
      Rawlctx() -> { return ""; }
      Rawlctx(x) -> { return `toTex(x); }
      Rawlctx(h,t*) -> { return %[@`toTex(h)@,@toTex(`t)@]%; }
    }
    throw new RuntimeException("non exhaustive patterns"); 
	}

	private static String toTex(RawRCtx ctx) {
    %match(ctx) {
      Rawrctx() -> { return ""; }
      Rawrctx(x) -> { return `toTex(x); }
      Rawrctx(h,t*) -> { return %[@`toTex(h)@,@toTex(`t)@]%; }
    }
    throw new RuntimeException("non exhaustive patterns"); 
	}

	private static String toTex(RawNProp np) {
		%match(np) {
			Rawnprop(_,p) -> { return `toTex(p); }
		}
		throw new RuntimeException("non exhaustive patterns"); 
	}

	private static String toTex(RawCNProp cnp) {
		%match(cnp) {
			Rawcnprop(_,p) -> { return `toTex(p); }
		}
		throw new RuntimeException("non exhaustive patterns"); 
	}

	private static String toTex(RawSequent se) {
		%match(se) {
			Rawseq(_,gamma,delta) -> { return %[@`toTex(gamma)@ \vdash @`toTex(delta)@]%; }
		}
		throw new RuntimeException("non exhaustive patterns"); 
	}

	private static String toTex(RawProofTerm pt, RawSequent se) {
		%match(se) {
			Rawseq(free,gamma,delta) -> {
				%match(pt) {
					RawrootR(RawRootRPrem1(a,pa,M)) -> {
						RawSequent se1 = `Rawseq(free,Rawlctx(gamma),Rawrctx(Rawcnprop(a,pa),delta*));
						se1 = `clean(M,se1);
						return `toTex(M,se1);
					}
					Rawax[] -> { return %[\infer[ax]{@`toTex(se)@}{}]%; }
					RawtrueR[] -> { return %[\infer[\top_R]{@`toTex(se)@}{}]%; }
					RawfalseL[] -> { return %[\infer[\bot_R]{@`toTex(se)@}{}]%; }
					Rawcut(RawCutPrem1(a,pa,M1),RawCutPrem2(x,px,M2)) -> {
						RawSequent se1 = `Rawseq(free,gamma,Rawrctx(Rawcnprop(a,pa),delta*));
						se1 = `clean(M1,se1);
						RawSequent se2 = `Rawseq(free,Rawlctx(Rawnprop(x,px),gamma*),delta);
						se2 = `clean(M2,se2);
						return %[\infer[cut]{@`toTex(se)@}{@`toTex(M1,se1)@ & @`toTex(M2,se2)@}]%;
					}
					RawimplyR(RawImplyRPrem1(x,px,a,pa,M),cn) -> { 
						RawSequent se1 = 
							`Rawseq(free,Rawlctx(Rawnprop(x,px),gamma*),Rawrctx(Rawcnprop(a,pa),delta*));
						se1 = `clean(M,se1);
						return %[\infer[\Rightarrow_R]{@`toTex(se)@}{@`toTex(M,se1)@}]%;
					}
					RawimplyL(RawImplyLPrem1(x,px,M1),RawImplyLPrem2(a,pa,M2),n) -> {
						RawSequent se1 = `Rawseq(free,Rawlctx(Rawnprop(x,px),gamma*),delta);
						se1 = `clean(M1,se1);
						RawSequent se2 = `Rawseq(free,gamma,Rawrctx(Rawcnprop(a,pa),delta*));
						se2 = `clean(M2,se2);
						return 
							%[\infer[\Rightarrow_L]{@`toTex(se)@}{@`toTex(M1,se1)@ & @`toTex(M2,se2)@}]%;
					}
					RawandR(RawAndRPrem1(a,pa,M1),RawAndRPrem2(b,pb,M2),cn) -> {
						RawSequent se1 = `Rawseq(free,gamma,Rawrctx(Rawcnprop(a,pa),delta*));
						se1 = `clean(M1,se1);
						RawSequent se2 = `Rawseq(free,gamma,Rawrctx(Rawcnprop(b,pb),delta*));
						se2 = `clean(M2,se2);
						return 
							%[\infer[\land_R]{@`toTex(se)@}{@`toTex(M1,se1)@ & @`toTex(M2,se2)@}]%;
					}
					RawandL(RawAndLPrem1(x,px,y,py,M),n) -> {
						RawSequent se1 = `Rawseq(free,Rawlctx(Rawnprop(x,px),Rawnprop(y,py),gamma*),delta);
						se1 = `clean(M,se1);
						return %[\infer[\land_L]{@`toTex(se)@}{@`toTex(M,se1)@}]%;
					}
					RaworR(RawOrRPrem1(a,pa,b,pb,M),n) -> {
						RawSequent se1 = 
							`Rawseq(free,gamma,Rawrctx(Rawcnprop(a,pa),Rawcnprop(b,pb),delta*));
						se1 = `clean(M,se1);
						return %[\infer[\lor_R]{@`toTex(se)@}{@`toTex(M,se1)@}]%;
					}
					RaworL(RawOrLPrem1(x,px,M1),RawOrLPrem2(y,py,M2),cn) -> {
						RawSequent se1 = `Rawseq(free,Rawlctx(Rawnprop(x,px),gamma*),delta);
						se1 = `clean(M1,se1);
						RawSequent se2 = `Rawseq(free,Rawlctx(Rawnprop(y,py),gamma*),delta);
						se2 = `clean(M2,se2);
						return 
							%[\infer[\lor_L]{@`toTex(se)@}{@`toTex(M1,se1)@ & @`toTex(M2,se2)@}]%;
					}
					RawexistsR(RawExistsRPrem1(a,pa,M),t,cn) -> {
						RawSequent se1 = `Rawseq(free,gamma,Rawrctx(Rawcnprop(a,pa),delta*));
						se1 = `clean(M,se1);
						return %[\infer[\exists_R]{@`toTex(se)@}{@`toTex(M,se1)@}]%;
					}
					RawexistsL(RawExistsLPrem1(x,px,fx,M),n) -> {
						RawSequent se1 = 
							`Rawseq(RawfovarList(fx,free*),Rawlctx(Rawnprop(x,px),gamma*),delta);
						se1 = `clean(M,se1);
						return %[\infer[\exists_L]{@`toTex(se)@}{@`toTex(M,se1)@}]%;
					}
					RawforallR(RawForallRPrem1(a,pa,fx,M),cn) -> {
						RawSequent se1 = 
							`Rawseq(RawfovarList(fx,free*),gamma,Rawrctx(Rawcnprop(a,pa),delta*));
						se1 = `clean(M,se1);
						return %[\infer[\forall_R]{@`toTex(se)@}{@`toTex(M,se1)@}]%;
					}
					RawforallL(RawForallLPrem1(x,px,M),t,n) -> {
						RawSequent se1 = 
							`Rawseq(free,Rawlctx(Rawnprop(x,px),gamma*),delta);
						se1 = `clean(M,se1);
						return %[\infer[\forall_L]{@`toTex(se)@}{@`toTex(M,se1)@}]%;
					}
					RawfoldR(r,RawFoldRPrem1(a,pa,M),cn) -> {
						RawSequent se1 = `Rawseq(free,gamma,Rawrctx(Rawcnprop(a,pa),delta*));
						se1 = `clean(M,se1);
						return %[\infer[fold_R(@`r@)]{@`toTex(se)@}{@`toTex(M,se1)@}]%;						
					}
					RawfoldL(r,RawFoldLPrem1(x,px,M),n) -> {
						RawSequent se1 = `Rawseq(free,Rawlctx(Rawnprop(x,px),gamma*),delta);
						se1 = `clean(M,se1);
						return %[\infer[fold_L(@`r@)]{@`toTex(se)@}{@`toTex(M,se1)@}]%;						
					}
				}
			}
		}
		throw new RuntimeException("non exhaustive patterns");
	}

	public static String toTex(RawProofTerm pt) {
		return `toTex(pt,Rawseq(RawfovarList(),Rawlctx(),Rawrctx()));
	}

  // displays a latex output in xdvi
  public static void display(RawProofTerm p) 
		throws java.io.IOException, java.lang.InterruptedException {
    File tmp = File.createTempFile("output",".tex");
    FileWriter writer = new FileWriter(tmp);
    String path = tmp.getAbsolutePath();
    String name = tmp.getName();
    String basename = "/tmp/"+name;
    basename = basename.substring(0,basename.length()-4);

    writer.write("\\documentclass{article}\n\\usepa"+
								 "ckage{proof}\n\\usepa"+
								 "ckage{amssymb}\n\\begin{document}\n\\pagestyle{empty}\n\\[\n");
    writer.write(toTex(p));
    writer.write("\n\\]\n");
    writer.write("\\end{document}\n");
    writer.flush();

    System.out.println(path);
    Runtime rt = Runtime.getRuntime();
    Process pr = rt.exec("latex -output-directory=/tmp " + path);

    int ret = pr.waitFor(); 
    if (ret == 0) {
      pr = rt.exec("dvips " + basename +".dvi -X 300 -Y 300 -E -o "+ basename +".ps");
      ret = pr.waitFor();
    } if (ret ==0) {
      pr = rt.exec("evince "+ basename +".ps");
      ret = pr.waitFor();
    } else
      System.err.println("An error occurred during the LaTeX compilation.");
  }
}

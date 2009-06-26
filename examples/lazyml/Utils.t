package lazyml;

import tom.library.sl.*;
import lazyml.lambda.types.*;
import org.antlr.runtime.*;
import java.util.*;

public class Utils {

  %include { lambda/lambda.tom }

  private static TVarList getFreeTVars(LType ty, TVarList free) {
    %match(ty) {
      Atom(_) -> { return `TVarList(); }
      Arrow(a,b) -> {
        TVarList fa = `getFreeTVars(a,free);
        TVarList fb = `getFreeTVars(b,free);
        return `TVarList(fa*,fb*);
      }
      TypeVar(X) -> { 
        if (((Collection)free).`contains(X)) return `TVarList();
        else return `TVarList(X);
      }
      Forall(Fa(X,a)) -> { 
        return `getFreeTVars(a,TVarList(X,free*)); 
      }
      TyConstr(_,tys) -> { 
        return `getFreeTVars(tys,free); 
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static TVarList getFreeTVars(TyList tys, TVarList free) {
    %match(tys) {
      TyList() -> { return `TVarList(); }
      TyList(x,xs*) -> { 
        TVarList fx = `getFreeTVars(x,free);
        TVarList fxs = `getFreeTVars(xs,free);
        return `TVarList(fx*,fxs*);
      }
    }
    throw new RuntimeException("non exhaustive patterns");
  }

  private static TVarList getFreeTVars(FTerm t, TVarList free) {
    %match(t) {
      FApp(a,b) -> { 
        TVarList fa = `getFreeTVars(a,free);
        TVarList fb = `getFreeTVars(b,free);
        return `TVarList(fa*,fb*);
      }
      FTApp(u,ty) -> {
        TVarList fa = `getFreeTVars(u,free);
        TVarList fb = `getFreeTVars(ty,free);
        return `TVarList(fa*,fb*);
      }
      FAbs(FLam(_,ty,t1)) -> { 
        TVarList fa = `getFreeTVars(ty,free);
        TVarList fb = `getFreeTVars(t1,free);
        return `TVarList(fa*,fb*);
      }
      FTAbs(FTLam(X,u)) -> {
        return `getFreeTVars(u,TVarList(X,free*));
      }
      FLet(FLetin(_,ty,t1,t2)) -> {
        TVarList fa = `getFreeTVars(ty,free);
        TVarList fb = `getFreeTVars(t1,free);
        TVarList fc = `getFreeTVars(t2,free);
        return `TVarList(fa*,fb*,fc*);
      }
      FFix(FFixpoint(_,ty,t1)) -> { 
        TVarList fa = `getFreeTVars(ty,free);
        TVarList fb = `getFreeTVars(t1,free);
        return `TVarList(fa*,fb*);
      }
      FVar(_) -> { return `TVarList(); }
      FConstr(_,ty,c) -> { 
        TVarList fa = `getFreeTVars(ty,free);
        TVarList fb = `getFreeTVars(c,free);
        return `TVarList(fa*,fb*);
			}
      FPrimFun(_,c) -> { return `getFreeTVars(c,free); }
      FCase(s,r) -> { 
        TVarList fa = `getFreeTVars(s,free);
        TVarList fb = `getFreeTVars(r,free);
        return `TVarList(fa*,fb*);
      }
      FLit(_) -> { return `TVarList(); }
      FChr(_) -> { return `TVarList(); }
      FStr(_) -> { return `TVarList(); }
      FError(_) -> { return `TVarList(); }
    }
    throw new RuntimeException();
  }

  private static TVarList getFreeTVars(FRules r, TVarList free) {
    %match(r) {
      FRList() -> { return `TVarList(); }
      FRList(t,ts*) -> {
        TVarList fa = `getFreeTVars(t,free);
        TVarList fb = `getFreeTVars(ts,free);
        return `TVarList(fa*,fb*);
      }
    }
    throw new RuntimeException();
  }

  private static TVarList getFreeTVars(FClause c, TVarList free) {
    %match(c) {
      FRule(p,t) -> { 
        TVarList fa = `getFreeTVars(p,free);
        TVarList fb = `getFreeTVars(t,free);
        return `TVarList(fa*,fb*);
      }
    }
    throw new RuntimeException();
  }

  private static TVarList getFreeTVars(FTermList l, TVarList free) {
    %match(l) {
      FTermList() -> { return `TVarList(); }
      FTermList(t,ts*) -> {
        TVarList fa = `getFreeTVars(t,free);
        TVarList fb = `getFreeTVars(ts,free);
        return `TVarList(fa*,fb*);
      }
    }
    throw new RuntimeException();
  }
  
  private static TVarList getFreeTVars(FPattern p, TVarList free) {
    %match(p) {
      FPFun(_,pl) -> { return `getFreeTVars(pl,free); }
      FDefault() -> { return `TVarList(); }
    }
    throw new RuntimeException();
  }

  private static TVarList getFreeTVars(FPVarList l, TVarList free) {
    %match(l) {
      FPVarList() -> { return `TVarList(); }
      FPVarList(p,ps*) -> { 
        TVarList fp = `getFreeTVars(p,free);
        TVarList fps = `getFreeTVars(ps,free);
        return `TVarList(fp*,fps*);
      }
    }
    throw new RuntimeException();
  }

  private static TVarList getFreeTVars(FPVar v, TVarList free) {
    %match(v) {
			FPVar(_,T) -> { return `getFreeTVars(T,free); }
		}
		throw new RuntimeException("pattern matching non exhaustive");
	}

  private static TVarList nub(TVarList l) {
    %match(l) {
      TVarList(X*,x,Y*,x,Z*) -> {
        return `nub(TVarList(X*,x,Y*,Z*));
      }
      ok -> { return `ok; }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }

  public static TVarList getFreeTVars(FTerm t) {
    return `nub(getFreeTVars(t,TVarList()));
  }
      
  private static LVarList getFreeVars(FTerm t, LVarList free) {
    %match(t) {
      FApp(a,b) -> { 
        LVarList fa = `getFreeVars(a,free);
        LVarList fb = `getFreeVars(b,free);
        return `LVarList(fa*,fb*);
      }
      FTApp(u,_) -> {
        return `getFreeVars(u,free);
      }
      FAbs(FLam(x,_,t1)) -> { 
        return `getFreeVars(t1,LVarList(x,free*));
      }
      FTAbs(FTLam(_,u)) -> {
        return `getFreeVars(u,free);
      }
      FLet(FLetin(f,_,t1,t2)) -> {
        LVarList fa = `getFreeVars(t1,free);
        LVarList fb = `getFreeVars(t2,LVarList(f,free*));
        return `LVarList(fa*,fb*);
      }
      FFix(FFixpoint(f,_,t1)) -> { 
        return `getFreeVars(t1,LVarList(f,free*));
      }
      FVar(x) -> {
        if (((Collection)free).contains(`x)) return `LVarList();
        else return `LVarList(x);
      }
      FConstr(_,_,c) -> { return `getFreeVars(c,free); }
      FPrimFun(_,c) -> { return `getFreeVars(c,free); }
      FCase(s,r) -> { 
        LVarList fa = `getFreeVars(s,free);
        LVarList fb = `getFreeVars(r,free);
        return `LVarList(fa*,fb*);
      }
      FLit(_) -> { return `LVarList(); }
      FChr(_) -> { return `LVarList(); }
      FStr(_) -> { return `LVarList(); }
      FError(_) -> { return `LVarList(); }
    }
    throw new RuntimeException();
  }

  private static LVarList getFreeVars(FRules r, LVarList free) {
    %match(r) {
      FRList() -> { return `LVarList(); }
      FRList(t,ts*) -> {
        LVarList fa = `getFreeVars(t,free);
        LVarList fb = `getFreeVars(ts,free);
        return `LVarList(fa*,fb*);
      }
    }
    throw new RuntimeException();
  }

  private static LVarList getFreeVars(FClause c, LVarList free) {
    %match(c) {
      FRule(p,t) -> { 
        LVarList fa = `getVars(p);
        return `getFreeVars(t,LVarList(fa*,free*));
      }
    }
    throw new RuntimeException();
  }

  private static LVarList getFreeVars(FTermList l, LVarList free) {
    %match(l) {
      FTermList() -> { return `LVarList(); }
      FTermList(t,ts*) -> {
        LVarList fa = `getFreeVars(t,free);
        LVarList fb = `getFreeVars(ts,free);
        return `LVarList(fa*,fb*);
      }
    }
    throw new RuntimeException();
  }
  
  private static LVarList getVars(FPattern p) {
    %match(p) {
      FPFun(_,pl) -> { return `getVars(pl); }
      FDefault() -> { return `LVarList(); }
    }
    throw new RuntimeException();
  }

  private static LVarList getVars(FPVarList l) {
    %match(l) {
      FPVarList() -> { return `LVarList(); }
      FPVarList(FPVar(v,_),vs*) -> {
				LVarList vl = `getVars(vs); 
        return `LVarList(v,vl*);
      }
    }
    throw new RuntimeException();
  }

  private static LVarList nub(LVarList l) {
    %match(l) {
      LVarList(X*,x,Y*,x,Z*) -> {
        return `nub(LVarList(X*,x,Y*,Z*));
      }
      ok -> { return `ok; }
    }
    throw new RuntimeException("non exhaustive patterns"); 
  }


  public static LVarList getFreeVars(FTerm t) {
    return `nub(getFreeVars(t,LVarList()));
  }

	public static LType getType(Context c, FRules rs) {
		%match(FRules rs) {
		  EmptyFRList()   -> { 
				throw new RuntimeException("malformed term: empty case");
			} 
			ConsFRList(cl,_) -> { 
				return `getType(c,cl);
			}
		}
		throw new RuntimeException("non exhaustive patterns");
	}

	public static LType getType(Context c, FClause r) {
		%match(FClause r) {
		  FRule(FPFun(_,vs),t) -> { 
				%match(FPVarList vs) {
				  FPVarList(_*,FPVar(x,ty),_*) -> { 
						c = `Context(Jugement(x,ty),c*);
						return `getType(c,t);
					}
				}
			}
			FRule(FDefault[],_) -> {
				throw new RuntimeException("something got wrong during case unfolding: case with first clause of the form var -> rhs");
			}
		}
		throw new RuntimeException("non exhaustive patterns");
	}

	public static LType getType(Context c, FTerm t) {
		%match(FTerm t) {
			FTApp(u,ty) -> {
				%match(getType(c,u)) {
					Forall(Fa(X,T)) -> {
						return Typer.`substType(X,ty,T);
					}
				}
			}
			FTAbs(FTLam(X,u)) -> {
				return `Forall(Fa(X,getType(c,u)));
			}
		  FApp(t1,_) -> {
				LType ty1 = `getType(c,t1);
				%match(ty1) {
					Arrow(_,res) -> { return `res; } 
				}
			}
			FAbs(FLam(x,ty1,u)) -> {
				return `Arrow(ty1,getType(Context(Jugement(x,ty1),c*),u));
			}
			FLet(FLetin(x,ty1,_,u)) -> { 
				return `getType(Context(Jugement(x,ty1),c*),u);
			}
			FFix(FFixpoint(x,ty1,u)) -> {
				return `getType(Context(Jugement(x,ty1),c*),u);
			} 
			FVar(x) -> {
				return Typer.`assoc(c,x);
			}
			FConstr(f,tys,_) -> {
				%match(Typer.assoc(c,f)) {
				  Range(Ra(_,_,a@Atom[])) -> { 
						return `a;
					}
					Range(Ra(_,_,TyConstr(C,_))) -> { 
						return `TyConstr(C,tys);
					}
				}
			}
			FCase(_,rules) -> { 
				return `getType(c,rules);
			} 
			FLit(_) -> { 
				return `Atom("Int");
			} 
			FStr(_) -> {
				return `Atom("String");
			} 
			FChr(_) -> {
				return `Atom("Character");
			} 
			FPrimFun(f,_) -> {
				return Typer.`assoc(c,f).getra().getcodom();
			}
			FError(_) -> {
				return null;
			}
		}
		throw new RuntimeException("not yet implemented " + t);
	}
}

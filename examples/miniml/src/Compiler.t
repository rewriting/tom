import org.antlr.runtime.tree.*;
import org.antlr.runtime.*;
import mlsig.types.*;
import tom.library.sl.*;
import java.util.*;

public class Compiler {

  %include { sl.tom }
  %include { mlsig/mlsig.tom }
  %include { mlsig/_mlsig.tom }

  %typeterm StringCollection { implement {Collection<String>} }
  %typeterm SymbolTable { implement {SymbolTable} }

  private static int freshCounter = 0;
  private static String freshFunSymb() {
    return "v_" + (freshCounter++);
  }

  private static class SymbolTable {
    private LinkedList<Scope> scopes = new LinkedList();  
    
    public void newScope(String name) {
      scopes.addFirst(`scope(name,typedvarlist()));
    }

    public void quitScope() {
      scopes.removeFirst();
    }

    public void addSymbol(String sym, Type t) {
      Scope s = scopes.removeFirst();
      %match (s) {
        scope(n,l) -> { s = `scope(n,typedvarlist(l*,typedvar(sym,t))); }
      }
      scopes.addFirst(s);
    }

    public ScopeSearchResult lookForSymb(String symb) {
        for (Scope s: scopes) {
          %match(s, String symb) {
            scope(n,(_*,typedvar(sy,t),_*)), sy -> {
              return `scopeAndType(n,t);
            }
          }
        }
        return null;
      }
  }


  %strategy DecorateAux() extends Fail() {
    visit Expr { 
      e@!typed[] -> { return `typed(e,undef()); } 
    }
  }

  // turns every expr into typed(expr,undef)
  private static Strategy Decorate() {
    return `mu(MuVar("x"),Choice(
          Sequence(DecorateAux(),_typed(All(MuVar("x")),Identity())),
          All(MuVar("x")))
        );
  }

  %strategy Propagate() extends Identity() {
    visit Affect {
      affect(var,t,typed(val,undef())) -> { 
        return `affect(var,t,typed(val,t)); 
      }
    } 
    visit Expr {
      typed(let(al,typed(in,undef())),t) -> { 
        return `typed(let(al,typed(in,t)),t); 
      }
      typed(function(arg,typed(e,undef())),abs(t1,t2)) -> { 
        return `typed(function(arg,typed(e,t2)),abs(t1,t2)); 
      }
      typed(typed(match(p,rl),undef()),t) -> { 
        return `typed(typed(match(p,rl),t),t); 
      }
      typed(match(p,(RL1*,rule(lhs,typed(e,undef())),RL2*)),t) -> {
        return `typed(match(p,rulelist(RL1*,rule(lhs,typed(e,t)),RL2*)),t);
      }
      typed(skip(),undef()) -> { 
        return `typed(skip(),unit()); 
      }
    } 
  }

  %strategy NameAnonymousVals() extends Identity() {
    visit Expr {
      typed(function(arg,f@typed(!let[],t1)),t2) -> {
        String fresh = freshFunSymb();
        return `typed(function(arg,typed(let(
                        affect(fresh,t1,f),
                        typed(var(fresh),t1)),t1)),t2);
      }
   }
  }

  private static Strategy PropagateVarTypes(SymbolTable st) {
    return `mu(MuVar("x"),
        Choice(SpecialCase(st),Sequence(OtherCases(st),All(MuVar("x"))))); 
  }

  %strategy SpecialCase(st: SymbolTable) extends Fail() {
    visit Affect {
      affect(name,t@abs(t1,_),f@typed(function(arg,_),_)) -> { 
        st.newScope(`name);
        st.addSymbol(`name,`t);
        st.addSymbol(`arg,`t1);
        Expr res = (Expr) PropagateVarTypes(st).visit(`f);
        st.quitScope();
        return `affect(name,t,res);
      }
    }
  }

  %strategy OtherCases(st: SymbolTable) extends Identity() {
    visit Affect {
      affect(var,t,_) -> {
        st.addSymbol(`var,`t);
      }
    }
    visit Expr {
      typed(var(n),undef()) -> {
        ScopeSearchResult res = st.lookForSymb(`n);
        if (res != null) { 
          %match(res) {
            scopeAndType(sn,t) -> { return `typed(scopedvar(sn,n),t); }
          }
        }
      }
    }
  }

  %strategy TypeApplications() extends Identity() {
    visit Expr {
      typed(appl(typed(f,t@abs(_,t1)),e),undef()) -> {
        return `typed(appl(typed(f,t),e),t1);
      }
      /*
      typed(appl(typed(f,t@abs(_,t1)),e),undef()) -> {
        return `typed(appl(typed(f,t),e),t1);
      }*/
      // en dernier recours
      appl(typed(e1,undef()),e2) -> {
        return `appl(typed(e1,basetype("Strategy")),e2);
      } 
    }
  }

  %strategy toLetlist() extends Identity() {
    visit Expr {
      typed(let(a,in),t) -> { return `typed(letlist(alist(a),in),t); }
      typed(letlist(l,typed(let(a,in),_)),t) -> { 
        return `typed(letlist(alist(l*,a),in),t); 
      }
    }
  }

  public static Expr prepare(Expr e) throws VisitFailure {
    //System.out.println("before typing:");
    //ted.VisitableViewer.toTreeStdout(e);
    //System.out.println(pp(e));
    e = (Expr) Decorate().visit(e);
    e = (Expr) `TopDown(RepeatId(Propagate())).visit(e);
    e = (Expr) `TopDown(NameAnonymousVals()).visit(e);

    //ted.VisitableViewer.toTreeStdout(e);
    SymbolTable st = new SymbolTable();
    e = (Expr) PropagateVarTypes(st).visit(e);
    e = (Expr) `BottomUp(TypeApplications()).visit(e);
    e = (Expr) `TopDown(RepeatId(toLetlist())).visit(e);
    //ted.VisitableViewer.toTreeStdout(e);

    return e;
  }



  private static 
    String mToJava(mlsig.mlsigAbstractType e, String res) {
    %match(Expr e) {
      typed(match(var,rl),_) -> {
        return "%match (" + `var + ") {\n" + `mToJava(rl,res) + "}\n";
      }
    }
    %match(Rule e) {
      rule(lhs,rhs@typed(_,ty)) -> {
        return `toJava(lhs)+" -> { " + res + " = " + 
               `toJava(rhs)+"; }\n";
      }
    }
    %match(RuleList e) {
      (r) -> { return `mToJava(r,res); }
      (r,rl) -> { return `mToJava(r,res) + `mToJava(rl,res); }
    }
    return null;
  }


  public static String toJava(mlsig.mlsigAbstractType e) {
    return toJava(e,null,null);
  }

  // ugly : change for a parameter 
  private static boolean inbackquote = false;
  private static String 
    toJava(mlsig.mlsigAbstractType ee, String arg, Type argtype) {
      String res = "";
      %match(Type ee) {
        abs[] -> { res += "Strategy"; }
        basetype(n) -> { res += `n; }
      }
      %match(Expr ee) {
        typed[e=letlist(alist(affect(v,_,fun)),typed[e=skip()])] -> {
          res += "public static class " + `v + " extends AbstractStrategy {\n";
          res += "public " + `v + "() { initSubterm(); }\n";
          res += "public Visitable visitLight(Visitable v) "
                +"throws VisitFailure { return v; }\n";
          res += "public "+`v+" make"+`v+"() { return new "+`v+"(); }\n";
          res += `toJava(fun);
          res += "}\n";
          res += "%op Strategy " + `v + " () { "; 
          res += "make() { new "+`v+"() } }\n"; 
        }
        typed[e=letlist(al,in@typed(!skip(),t))] -> {
          %match(AffectList al) {
            (_*,affect(var,tt,exp@typed(!function[],_)),_*) -> {
              res += `toJava(tt) + " " + `var + ";\n";
              /* when generating a call to a function, there is
                 no way to kmow if it is a generated class or some
                 parameter, thus we generate makevar methods for
                 everything with a functional type */
              %match(tt) { 
                abs[] -> { 
                  res += "public Strategy make"+`var + "() "
                      +  "{ return "+`var+"; }\n"; 
                }
              }
            }
          }
          res += "public int visit() {\n";
          res += "try {\n";
          res += "`Identity().visit((Visitable) null);\n";
          if(arg != null) { 
            res += "this." + arg + " = (" + toJava(argtype) 
                +  ") environment.getSubject();\n"; 
          }
          %match(AffectList al) {
            (_*,affect(var,type,exp@typed(!function[],_)),_*) -> {
              %match(exp) {
                typed(!match[],_) -> { 
                  res += "this." + `var + " = " + `toJava(exp) + ";\n"; 
                }
                typed(match[],_) -> {
                  res += `mToJava(exp,var); 
                }
              }
            }
          }
          %match(in) {
            typed(!match[],_) -> { 
              res += toJava(`t)+" _result = ("+toJava(`t)+") "+
                     toJava(`in)+";\n";
            }
            typed(match[],_) -> {
              res += toJava(`t)+" _result;\n";
              res += `mToJava(in,"_result");
            }
          }
          res += "environment.setSubject(_result);\n";
          res += "return Environment.SUCCESS;\n";
          res += "} catch (VisitFailure ex) {";
          res += "return Environment.FAILURE; }\n\n";
          res += "}";

          %match(AffectList al) {
            (_*,affect(v,type,exp@typed(function[],_)),_*) -> {
              res += "public "+`v+" make"+`v+"() { return new "+`v+"(); }\n";
              res += "public class " + `v + " extends AbstractStrategy {\n";
              res += "public " + `v + "() { initSubterm(); }\n";
              res += "public Visitable visitLight(Visitable v) "
                    +"throws VisitFailure { return v; }\n";
              res += `toJava(exp);
              res += "}\n";
            }
          }
        }
        typed(function(arg,e),abs(t,_)) -> {
          res += `toJava(t) + " " + `arg + ";\n";
          %match(t) { 
            abs[] -> { res += "public Strategy make"+`arg+ "() "
                           +  "{ return "+`arg+"; }\n"; 
            }
          }
          res += `toJava(e,arg,t);
        }
        typed(constr(f,l),_) -> {
          if(!inbackquote) {
            inbackquote = true;
            res += "`" + `f + "(" + `toJava(l) + ")";
            inbackquote = false;
          } else {
            res += `f + "(" + `toJava(l) + ")";
          }
        }
        typed(var(n),abs[]) -> { res += "make"+`n+"()"; }
        typed(scopedvar(s,n),abs[]) -> { res += `s+".this.make"+`n+"()"; }
        typed(var(n),!abs[]) -> {
          if(! inbackquote) res += "`";
            res += `n; 
        }
        typed(scopedvar(s,n),!abs[]) -> { res += `s+".this."+`n; }
        typed(appl(typed(var(n),_),e2),t) -> { 
          return "(("+`toJava(t)+") `"+`n +"().visit("+`toJava(e2)+"))";
        }
        typed(appl(e1,e2),t) -> { 
          return "(("+`toJava(t)+") "+`toJava(e1)+".visit("+`toJava(e2)+"))";
        }
      }
      %match (ExprList ee) {
        (x) -> { return `toJava(x); }
        (h,tail*) -> { 
          return `toJava(h) + "," + `toJava(tail); 
        }
      }
      %match(Pattern ee) {
        pvar(n) -> { return `n; }
        pconstr(f,l) -> { return `f + "(" + `toJava(l) + ")"; } 
      }
      %match (PatternList ee) {
        (x) -> { return `toJava(x); }
        (h,tail*) -> { return `toJava(h) + "," + `toJava(tail); }
      }
       return res;
    }

  public static String pp(mlsig.mlsigAbstractType e) {
    String res = "";
    %match (ExprList e) {
      (x) -> { return `pp(x); }
      (h,tail*) -> { return `pp(h) + "," + `pp(tail); }
    }
    %match (Expr e) { 
      var(n) -> { return `n; }
      scopedvar(scope,n) -> { return `scope + "." + `n; }
      constr(f,l) -> { return `f + "(" + `pp(l) + ")"; } 
      appl(e1,e2) -> { return "(" + `pp(e1) + " " + `pp(e2) + ")"; }
      typed(exp,t) -> { return "[" + `pp(exp) + "]:" + `pp(t) ; }
      match(id,rl) -> { return "match " + `id + " with\n" + `pp(rl); }
      function(arg,exp) -> { return "function " + `arg + " ->\n" + `pp(exp); }
      letlist(affects,in) -> { return `pp(affects) + `pp(in); }
      let(affect,in) -> { return "let "+`pp(affect)+" in\n"+`pp(in); }
      skip() -> { return "()"; }
    }
    %match(Affect e) {
      affect(var,t,val) -> { return `var + ":" + `pp(t) + " = " + `pp(val); }
    }
    %match(AffectList e) {
      (_*,x,_*) -> { res += "let " + `pp(x) + " in\n"; }
    }
    %match(Type e) {
      abs(t1@basetype[],t2) -> { return `pp(t1) + "->" + `pp(t2); }
      abs(t1,t2) -> { return "(" + `pp(t1) + ")->" + `pp(t2); }
      basetype(n) -> { return `n; }
      unit() -> { return "unit"; }
      undef() -> { return "UNDEF"; }
    }
    %match(Rule e) {
      rule(lhs,rhs) -> { return `pp(lhs) + " -> " + `pp(rhs); }
    }
    %match(RuleList e) {
      (_*,x,_*) -> { res += "| " + `pp(x) + "\n"; }
    }
    %match(Pattern e) {
      pvar(n) -> { return `n; }
      pconstr(f,l) -> { return `f + "(" + `pp(l) + ")"; } 
    }
    %match (PatternList e) {
      (x) -> { return `pp(x); }
      (h,tail*) -> { return `pp(h) + "," + `pp(tail); }
    }

    return res;
  }

  public static void main(String[] args) {
    try {
      CharStream input = new ANTLRFileStream(args[0]);
      MlLexer lex = new MlLexer(input);

      CommonTokenStream tokens = new CommonTokenStream(lex);
      MlParser parser = new MlParser(tokens);
      MlParser.prog_return root = parser.prog();
      CommonTreeNodeStream nodes = new CommonTreeNodeStream((Tree)root.tree);
      MlWalker walker = new MlWalker(nodes);
      Expr e = walker.prog();
      e = prepare(e);
      //System.out.println("\nat the end:");
      //System.out.println(pp(e));
      //System.out.println("\njava output:");
      System.out.println(toJava(e)); 
    } catch(Throwable t) {
      System.out.println("exception: "+t);
      t.printStackTrace();
    }
  }
}

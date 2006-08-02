import sequents.*;
import sequents.types.*;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.Collection;

import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.MuStrategy;
import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

import java.io.*;
import antlr.*;
import antlr.collections.*;

class Utils {
 
  %include { sequents/sequents.tom }

  // pour convenance

  public static Tree axiom(Sequent s, Prop active) {
    return `rule("axiom", premisses(), s, active);
  }

  public static Tree impliesL(Tree s1, Tree s2, Sequent c, Prop active) {
    return `rule("implies L", premisses(s1,s2), c, active);
  }

  public static Tree impliesR(Tree s, Sequent c, Prop active) {
    return `rule("implies R", premisses(s), c, active);
  }

  public static Tree andL(Tree s, Sequent c, Prop active) {
    return `rule("and L", premisses(s), c, active);
  }

  public static Tree andR(Tree s1, Tree s2, Sequent c, Prop active) {
    return `rule("and R", premisses(s1,s2), c, active);
  }

  public static Tree orR(Tree s, Sequent c, Prop active) {
    return `rule("or R", premisses(s), c, active);
  }

  public static Tree orL(Tree s1, Tree s2, Sequent c, Prop active) {
    return `rule("or L", premisses(s1,s2), c, active);
  }

  public static Prop prop(String name) {  // relation d'arite nulle, pour convenance
    return `relationAppl(relation(name), concTerm());
  }


  %include { mutraveler.tom }

  %strategy ReplaceTerm(old_term: Term, new_term: Term) extends `Identity() {
    visit Term {
      x -> { 
        if (`x == old_term) return new_term;
      }
    }
  }

  %strategy ReplaceProp(old_prop: Prop, new_prop: Prop) extends `Identity() {
    visit Prop {
      x -> {
        if (`x == old_prop) return new_prop;
      }
    }
  }

  public static sequentsAbstractType replaceTerm(sequentsAbstractType subject, 
      Term old_term, Term new_term) 
  {
    ReplaceTerm v = new ReplaceTerm(old_term, new_term);
    sequentsAbstractType res = null;
    try { res = (sequentsAbstractType) MuTraveler.init(`TopDown(v)).visit(subject); }
    catch (VisitFailure e ) { e.printStackTrace(); }
    return res;
  }


  %strategy ReplaceFreeVars(old_term: Term, new_term: Term) extends `Identity() {
    visit Prop {
      
      relationAppl(r, tl) -> {
       TermList res = (TermList) replaceTerm(`tl, old_term, new_term);
        return `relationAppl(r, res);
      }

      r@forAll(x,_) -> { 
        if  (`x != old_term.getname()) {
          return `r; 
        }
        else
          throw new VisitFailure();
      }

      r@exists(x,_) -> {
        if  (`x != old_term.getname())
          return `r; 
        else
          throw new VisitFailure();
      }
    }
  }

  public static sequentsAbstractType 
    replaceFreeVars(sequentsAbstractType p, Term old_term, Term new_term) 
    {
      VisitableVisitor r = `ReplaceFreeVars(old_term, new_term);
      VisitableVisitor v = `mu(MuVar("x"),Try(Sequence(r,All(MuVar("x")))));
      try {
        `p = (sequentsAbstractType) MuTraveler.init(v).visit(`p); 
      } catch ( VisitFailure e) { e.printStackTrace(); }
      return `p;
    }

  public static HashSet<Term> collectFreeVars(Prop p) {
    HashSet<Term> res = new HashSet<Term>();
    collectFreeVars(p, res, new Stack<Term>());
    return res;
  }

  private static void collectFreeVars(Prop p, HashSet<Term> set, Stack<Term> bounded) {
    %match(Prop p) {
      (forAll|exists)(n,p1) -> {
        bounded.push(`Var(n));
        collectFreeVars(`p1,set,bounded);
        bounded.pop();
      }
      relationAppl(r,t) -> {
        HashSet<Term> vars = collectVars(`t);
        for(Term var: vars) {
          if (!bounded.contains(var))
            set.add(var);
        }
      }
      (and|or)(p1,p2) -> {
        collectFreeVars(`p1,set,bounded);
        collectFreeVars(`p2,set,bounded);
      }
      implies(p1,p2) -> {
        collectFreeVars(`p1,set,bounded);
        collectFreeVars(`p2,set,bounded);
      }
    }
  }

  %typeterm Collection { implement {Collection}}

  %strategy VarCollector(vars:Collection) extends `Identity() {
    visit Term {
      v@Var(_) -> { vars.add(`v); }
    }
  }

  public static HashSet<Term> collectVars(sequentsAbstractType t) {
    HashSet set = new HashSet();
    MuStrategy v = (MuStrategy) `TopDown(VarCollector(set));
    v.apply(t);
    return set;
  }

  public static Term freshVar(String x, sequentsAbstractType term) {

    HashSet set = collectVars(term);

    int i = 0;
    while(true) {
      String s = x + i;
      Term res = `Var(s);
      if (!set.contains(res))
        return res;
      else
        i++;
    }
  }


  %strategy CollectConstraints(set: Collection) extends `Identity() {
    visit Term {
      x@FreshVar[] -> { set.add(`x); }
    }
  }

  public static HashSet getSideConstraints(SeqList list) {
    HashSet set = new HashSet();
    try {
      MuTraveler.init(`TopDown(CollectConstraints(set))).visit(list);
    } catch ( VisitFailure e) {
      e.printStackTrace();
    }
    return set;
  }

  %strategy CollectNewVars(set: Collection) extends `Identity() {
    visit Term {
      x@NewVar[] -> { set.add(`x); }
    }
  }

  public static HashSet getNewVars(SeqList list) {
    HashSet set = new HashSet();
    try {
      MuTraveler.init(`TopDown(CollectNewVars(set))).visit(list);
    } catch ( VisitFailure e) {
      e.printStackTrace();
    }
    return set;
  }

  // handling user input
  public static String getInput() {
    String res = null;
    BufferedReader clav = new BufferedReader(new InputStreamReader(System.in));
    try { res = clav.readLine(); }
    catch (IOException e) { System.out.println(e); }
    return res.trim();
  }

  public static Prop getProp() throws RecognitionException, TokenStreamException {
    SeqLexer lexer = new SeqLexer(new DataInputStream(System.in));
    SeqParser parser = new SeqParser(lexer);
    SeqTreeParser walker = new SeqTreeParser();

    parser.start1();
    AST t = parser.getAST();
    Prop p  = walker.pred(t);
    return p;
  }

  public static Term getTerm() throws RecognitionException, TokenStreamException {
    SeqLexer lexer = new SeqLexer(new DataInputStream(System.in));
    SeqParser parser = new SeqParser(lexer);
    SeqTreeParser walker = new SeqTreeParser();

    parser.start2();
    AST t = parser.getAST();
    Term res  = walker.term(t);
    return res;
  }

  public static Command getCommand()
    throws RecognitionException, TokenStreamException {
      SeqLexer lexer = new SeqLexer(new DataInputStream(System.in));
      SeqParser parser = new SeqParser(lexer);
      SeqTreeParser walker = new SeqTreeParser();

      parser.command();
      AST t = parser.getAST();

      Command res = walker.command(t);
      return res;
    }

  public static ProofCommand getProofCommand()
    throws RecognitionException, TokenStreamException {
      SeqLexer lexer = new SeqLexer(new DataInputStream(System.in));
      SeqParser parser = new SeqParser(lexer);
      SeqTreeParser walker = new SeqTreeParser();

      parser.proofcommand();
      AST t = parser.getAST();
      ProofCommand res = walker.proofcommand(t);
      return res;
    }

  // FIXME : get rid of "ident" in parser and use lexer directly
  public static String getIdent()
    throws RecognitionException, TokenStreamException {
      SeqLexer lexer = new SeqLexer(new DataInputStream(System.in));
      SeqParser parser = new SeqParser(lexer);
      SeqTreeParser walker = new SeqTreeParser();

      parser.ident();
      AST t = parser.getAST();
      String res = walker.ident(t);
      return res;
    }
}


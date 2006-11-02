import junit.framework.TestCase;
import junit.framework.TestSuite;
import testlistnonlinear.test.types.*;

public class TestListNonLinear extends TestCase {
  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestListNonLinear.class));
  }
  
%gom {
  module test
  imports String int
  abstract syntax
    IntList = intlist(int*)
    Toto = f(l1:IntList,l2:IntList)


Term = Var(name:String)
     | funAppl(fun:Fun, p:TermList)
     | NewVar(name:String, base_name: String) // asks for a new var
     | FreshVar(name:String, base_name: String) // asks for a fresh var

Fun  = fun(name:String)

TermList = concTerm(Term*)

Relation = relation(name:String)

Prop = relationAppl(r:Relation, t:TermList)
     | and(p1:Prop, p2:Prop)
     | or(p1:Prop, p2:Prop)
     | implies(left:Prop, right:Prop)
     | forAll(var:String, p:Prop)
     | exists(var:String, p:Prop)
     | bottom()
     | top()

Context = context(Prop*)

Sequent = sequent(h:Context,c:Context)

Premisses = premisses(Tree*)

// st == symbol table
Tree = rule(name:String, p:Premisses, c:Sequent, active:Prop)

//--- new rules ---
SeqList = concSeq(Sequent*)

VarList = varlist(String*)

// hs == hand-side : 0 left, 1 right
Rule = ruledesc(hs:int, concl:Prop, prem:SeqList)

TermRule = termrule(lhs:Term,rh:Term)

TermRuleList = termrulelist(TermRule*)



}


  public void test1() {
    IntList l1 = `intlist(1,2,4,5);
    IntList l2 = `intlist(1,2,3,4,5);
    %match(IntList l1, IntList l2) {
      (x*,y*),(x*,3,y*) -> {
        return;
      }
    }
    fail("ca marche pas");
  }

  public void test2() {
    IntList l1 = `intlist(1,2,4,5);
    IntList l2 = `intlist(1,2,3,4,5);
    Toto t = `f(l1,l2);
    %match(t) {
      f((x*,y*),(x*,3,y*)) -> {
        return;
      }
    }
    fail("ca marche pas");
  }

  public void test4() {
    IntList l1 = `intlist(1,2,4,5);
    IntList l2 = `intlist(1,2,3,4,5);
    IntList l3 = `intlist(1,2,6,4,5);
    %match(IntList l1, IntList l2, IntList l3) {
      (x*,y*),(x*,a,y*),(x*,b,y*) -> {
        return;
      }
    }
    fail("ca marche pas");
  }



  public void test3() {
Tree t = `rule("implies L",Conspremisses(rule("axiom",Emptypremisses(),sequent(
Conscontext(relationAppl(relation("G"),EmptyconcTerm()), /* g1* */
Conscontext(relationAppl(relation("A"),EmptyconcTerm()),Emptycontext())), /* g2* */
Conscontext(relationAppl(relation("A"),EmptyconcTerm()), /* A */
Conscontext(relationAppl(relation("B"),EmptyconcTerm()),Emptycontext()))), /* d* */
relationAppl(relation("A"),EmptyconcTerm())), /* */
Conspremisses(rule("axiom",Emptypremisses(),sequent(
Conscontext(relationAppl(relation("G"),EmptyconcTerm()), /* g1* */
Conscontext(relationAppl(relation("B"),EmptyconcTerm()), /* B */
Conscontext(relationAppl(relation("A"),EmptyconcTerm()),Emptycontext()))), /* g2* */
Conscontext(relationAppl(relation("B"),EmptyconcTerm()),Emptycontext())), /* d */
relationAppl(relation("B"),EmptyconcTerm())),Emptypremisses())), /* */
sequent(
Conscontext(relationAppl(relation("G"),EmptyconcTerm()), /* g1* */
Conscontext(implies(relationAppl(relation("A"),EmptyconcTerm()),relationAppl(relation("B"),EmptyconcTerm())), /* a */
Conscontext(relationAppl(relation("A"),EmptyconcTerm()),Emptycontext()))), /* g2* */
Conscontext(relationAppl(relation("B"),EmptyconcTerm()),Emptycontext())), /* d */
implies(
relationAppl(relation("A"),EmptyconcTerm()), /* A */
relationAppl(relation("B"),EmptyconcTerm()))); /* B */

    %match(t) {
      rule(
          "implies L",
          (rule(_,_,sequent((g1*,g2*),(A,d*)),_), rule(_,_,sequent((g1*,B,g2*),d),_)),
          sequent((g1*,a,g2*),d),
          a@implies(A,B)
          )
/*
      rule(
          "implies L",
          (p1@rule(_,_,sequent((g1*,g2*),(A,d*)),_), p2@rule(_,_,sequent((g1*,B,g2*),d),_)),
          sequent(y@(g1*,a,g2*),d),
          a@implies(A,B)
          )
*/
        -> { return; }
    }
    fail("ca marche pas");
  }
}


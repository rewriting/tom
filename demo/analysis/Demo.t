import demo.term.types.*;
import java.util.*;
import tom.library.sl.*;

public class Demo {

  %gom {
    module Term
      imports int String
      abstract syntax

      Instruction = Seq(Instruction*)
      | Declare(v:Expression,e:Expression,i:Instruction)
      | Assign(v:Expression,e:Expression)
      | Print(e:Expression)

      Expression = Plus(e1:Expression,e2:Expression)
      | Mult(e1:Expression,e2:Expression)
      | Var(name:String)
      | Cst(n:int)

      Seq : FL() {}
  }

  %include { java/util/types/Collection.tom }
  %include { java/util/HashMap.tom }
  %include { sl.tom }
  %include { demo/term/_Term.tom }

  public final static void main(String[] args) {
    Instruction p1 = `Declare(Var("x"),Plus(Cst(1),Cst(2)), Seq(
          Declare(Var("y"),Mult(Var("x"),Cst(10)),
            Print(Var("y"))),
          Print(Var("x"))));
    try {
      System.out.println("Program p1:");
      System.out.println(pretty(p1));

      testRenaming(p1);

      testCollectVariables(p1);

      Expression e = `Mult(Cst(2),Plus(Cst(3),Cst(2)));
      testConstantEvaluation(e);

      testCfgTraversal(p1);
      //example with a more complex cfg
      Instruction p2 = `Seq(Declare(Var("x"),Cst(0),
            Declare(Var("y"),Cst(0),
              Seq(Print(Cst(1)),Print(Cst(2))))),
          Print(Cst(3)),
          Print(Cst(4)));
      System.out.println("\nProgram p2:");
      System.out.println(pretty(p2));
      testCfgTraversal(p2);

      testRandomGeneration();

    } catch (VisitFailure e ) {
      System.out.println("Strategy failure!!!");
    }
  }

  //Renaming
  public static void testRenaming(Instruction p) throws VisitFailure {
    System.out.println("\nRenaming x to z:");
    System.out.println(pretty(`TopDown(RenameVar("x","z")).visit(p)));
  }

  %strategy RenameVar(n1:String,n2:String) extends Identity() {
    visit Expression {
      Var(n) && n==n1 -> Var(n2)
    }
  }

  // Collecting variables
  public static void testCollectVariables(Instruction p) throws VisitFailure {
    System.out.println("\nCollecting variables:");
    HashSet set = new HashSet();
    `TopDown(CollectVar(set)).visit(p);
    System.out.println(set);
  }

  %strategy CollectVar(c:Collection) extends Identity() {
    visit Expression {
      Var(n) -> { c.add(`n); }
    }
  }

  // Constant evaluation
  public static void testConstantEvaluation(Expression e) throws VisitFailure {
    System.out.println("\nConstant evaluation:");
    System.out.println(pretty(e)+"="+pretty(`BottomUp(EvalConst()).visit(e)));
  }

  %strategy EvalConst() extends Identity() {
    visit Expression {
      Plus(Cst(c1),Cst(c2)) -> { return `Cst(c1 + c2); }
      Mult(Cst(c1),Cst(c2)) -> { return `Cst(c1 * c2); }
    }
  }


  // Print the Cfg by traversing the Ast
  public static void testCfgTraversal(Instruction p) throws VisitFailure {
    System.out.println("\nPrint the cfg:");
    `TopDownCfg(PrintInst()).visit(p);
  }

  %op Strategy TopDownCfg(s:Strategy) {
    make(s) { `Mu(MuVar("x"),Sequence(s,AllCfg(MuVar("x")))) }
  }

  %op Strategy AllCfg(s:Strategy) {
    make(s) {`Mu(MuVar("begin"), Choice(
          _Declare(Identity(), Identity(), s),
          _ConsSeq(s, Identity()),
          Sequence( Is_EmptySeq(), ApplyAtTopSeq(ApplyAtOuterSeq(MuVar("begin"))) ),
          Sequence( Is_Print(), ApplyAtOuterSeq(MuVar("begin"))),
          Sequence( Is_Assign(), ApplyAtOuterSeq(MuVar("begin")))))
    }
  }

  %op Strategy ApplyAtOuterSeq(s:Strategy) {
    make(s) { `Mu(MuVar("x"),Up(Choice(_ConsSeq(Identity(),s),MuVar("x")))) }
  }


  %op Strategy ApplyAtTopSeq(s:Strategy) {
    make(s) { `Mu(MuVar("x"),Up(Choice(Sequence(Is_ConsSeq(),MuVar("x")),s))) }
  }

  %strategy PrintInst() extends Identity() {
    visit Instruction {
      i -> { System.out.println(`i); }
    }
  }

  // Expression generation
  public static void testRandomGeneration() throws VisitFailure {
    System.out.println("\nExpression generation:");
    Depth max = new Depth(20);
    Depth current = new Depth(0);
    Strategy ExprGenerator = `Mu(MuVar("x"),
        Choice(ControlDepth(max,current), ChoiceUndet(
            GenerateRandomCst(),
            Sequence(IncreaseDepth(current),Make_Plus(MuVar("x"),MuVar("x")),DecreaseDepth(current)),
            Sequence(IncreaseDepth(current),Make_Mult(MuVar("x"),MuVar("x")),DecreaseDepth(current))
            )));
    System.out.println(pretty(ExprGenerator.visit(`Cst(0))));
  }

  static Random random = new Random();

  %strategy GenerateRandomCst() extends Identity() {
    visit Expression {
      _ -> { return `Cst(random.nextInt(101)); }
    }
  }

  static class Depth {
    public int value;

    public Depth(int value) {
      this.value = value;
    }
  }

  %typeterm Depth {
    implement { Depth }
  }

  %strategy PrintExpr() extends Identity() {
    visit Expression {
      x -> {
        System.out.println(getEnvironment());
      }
    }
  }

  %strategy ControlDepth(max:Depth,current:Depth) extends Fail() {
    visit Expression {
      x -> {
        if (current.value >= max.value) {
          return (Expression) `GenerateRandomCst().visit(`x);
        }
      }
    }
  }

  %strategy IncreaseDepth(current:Depth) extends Identity() {
    visit Expression {
      _ -> {
        current.value++;
      }
    }
  }

  %strategy DecreaseDepth(current:Depth) extends Identity() {
    visit Expression {
      _ -> {
        current.value--;
      }
    }
  }

  public static String pretty(Object e) { return pretty(0,e); }

  public static String pretty(int indent, Object e) {
    String w = "";
    for(int i=0;i<indent;i++) w+="  ";
    %match(e) { 
      Declare(var,expr,body) -> { return w+"declare " + pretty(`var) + " <- " + pretty(`expr) + " in\n" + pretty(indent+1,`body); }
      Assign(var,expr) -> { return w+pretty(`var)+" = "+pretty(`expr); }
      Print(expr) -> { return w+"print("+pretty(`expr)+")"; }
      Seq(x*) -> { String s=""; while(!`x.isEmptySeq()) { s+=pretty(indent,`x.getHeadSeq())+";\n"; `x=`x.getTailSeq(); } return s;  }
    }
    %match(e) {
      Plus(e1,e2) -> { return "("+pretty(`e1)+" + "+pretty(`e2)+")"; }
      Mult(e1,e2) -> { return "("+pretty(`e1)+" * "+pretty(`e2)+")"; }
      Var(name) -> { return `name; }
      Cst(n) -> { return ""+`n; }
    } 
    return w+e.toString();
  }

}

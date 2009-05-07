import org.jboss.el.parser.*;

import el.types.Expr;
import el.types.expr.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Assert;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class TestGomMapping extends TestCase {

  %include { el/EL.tom }
  %include { sl.tom }
  %include { string.tom }
  %include { java/util/types/Collection.tom }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestGomMapping.class));
  }

  public void testIdentifier() {
    Node node = ELParser.parse("#{contactSearchContext}");
    Expr expr = Utils.convertElExpr(node);

    System.out.println("Expr = " + expr);

    final List<Expr> list = collectTerms(expr, Identifier.class);

    Assert.assertTrue(list.size() == 1 && list.get(0) instanceof Identifier);
  }

  public void testIdentifierAndProperties() {
    Node node = ELParser.parse("#{contactSearchContext.positionGroup.syncUpdateDt}");
    Expr expr = Utils.convertElExpr(node);

    System.out.println("Expr = " + expr);

    final List<Expr> list = collectTerms(expr, Identifier.class, PropertySuffix.class);

    Assert.assertTrue(list.size() == 3 && list.get(0) instanceof Identifier &&
      list.get(1) instanceof PropertySuffix && list.get(2) instanceof PropertySuffix);
  }

  public void testEquals() {
    Node node = ELParser.parse("#{communicationContext.communication.id == null}");
    Expr expr = Utils.convertElExpr(node);

    System.out.println("Expr = " + expr);

    final List<Expr> list = collectTerms(expr);

    Assert.assertTrue(list.size() == 4 && list.get(0) instanceof Equal);
  }

  public void testArithmetic() {
    Node node = ELParser.parse("#{a + 2 / 3}");
    Expr expr = Utils.convertElExpr(node);

    System.out.println("Expr = " + expr);

    final List<Expr> list = collectTerms(expr);

    Assert.assertTrue(list.size() == 5 && list.get(0) instanceof Plus);
  }

  public void testConditional() {
    Node node = ELParser.parse("#{a != 3  ? 2 : 3}");
    Expr expr = Utils.convertElExpr(node);

    System.out.println("Expr = " + expr);

    final List<Expr> list = collectTerms(expr);

    Assert.assertTrue(list.size() == 6 && list.get(0) instanceof If);
  }

  public void testFunctionCall() {
    Node node = ELParser.parse("#{communicationContext.abc.search(searchEntityId,searchEntityCode)}");
    Expr expr = Utils.convertElExpr(node);

    System.out.println("Expr = " + expr);

    final List<Expr> list = collectTerms(expr);

    Assert.assertTrue(list.size() == 5 && list.get(0) instanceof FunctionCall);
  }

  public void testFunctionCallConst() {
    Node node = ELParser.parse("#{common.selectItems('Jurisdiction')}");
    Expr expr = Utils.convertElExpr(node);

    System.out.println("Expr = " + expr);

    final List<Expr> list = collectTerms(expr);

    Assert.assertTrue(list.size() == 3 && list.get(0) instanceof FunctionCall);
  }

  public void testNotEqual() {
    Node node = ELParser.parse("#{'query'!=contactSearchContext.contactFindType}");
    Expr expr = Utils.convertElExpr(node);

    System.out.println("Expr = " + expr);

    final List<Expr> list = collectTerms(expr);

    Assert.assertTrue(list.size() == 4 && list.get(0) instanceof NotEqual);
  }

  public void testNotEmpty() {
    Node node = ELParser.parse("#{not empty search.results}");
    Expr expr = Utils.convertElExpr(node);

    System.out.println("Expr = " + expr);

    final List<Expr> list = collectTerms(expr);

    Assert.assertTrue(list.size() == 4 && list.get(0) instanceof Not_);
  }

  public void testRelGt() {
    Node node = ELParser.parse("#{(numberGuess.biggest-numberGuess.smallest) gt 20}");
    Expr expr = Utils.convertElExpr(node);

    System.out.println("Expr = " + expr);

    final List<Expr> list = collectTerms(expr);

    //Assert.assertTrue(list.size() == 4 && list.get(0) instanceof koko......);
  }

  public void testMixedLiteral() {
    Node node = ELParser.parse("#{contactSearchList.getRowCount()} Clients Total Assets = $ #{contactSearchContext.getTotalAsset()}");
    Expr expr = Utils.convertElExpr(node);

    System.out.println("Expr = " + expr);

    final List<Expr> list = collectTerms(expr);

    Assert.assertTrue(list.size() == 5 && list.get(2) instanceof Literal);
  }

  public void testGreaterThan() {
    Node node = ELParser.parse("#{contacts.rowCount>0}");
    Expr expr = Utils.convertElExpr(node);

    System.out.println("Expr = " + expr);

    final List<Expr> list = collectTerms(expr);

    Assert.assertTrue(list.size() == 4 && list.get(0) instanceof GreaterThan);
  }

  public void testArrayAccess() {
    Node node = ELParser.parse("/images/icons/security/check_#{fsiContext.codeMap[fsiContext.getKey(fs.id, accessType)]}.PNG");
    Expr expr = Utils.convertElExpr(node);

    System.out.println("Expr = " + expr);

    final List<Expr> list = collectTerms(expr);

    Assert.assertTrue(list.size() == 10 && list.get(1) instanceof ArrayAccess);
  }

  %strategy FindTerms(terms:Collection, cls:Collection) extends Identity() {
	visit Expr {
     e@Literal[] -> {
       if(cls.isEmpty() || cls.contains(`e.getClass())) {
    	   terms.add(`e);
       }
     }
     e@Identifier[] -> {
       if(cls.isEmpty() || cls.contains(`e.getClass())) {
    	   terms.add(`e);
       }
     }
     e@Integer[] -> {
       if(cls.isEmpty() || cls.contains(`e.getClass())) {
    	   terms.add(`e);
       }
     }
     e@Str[] -> {
       if(cls.isEmpty() || cls.contains(`e.getClass())) {
    	   terms.add(`e);
       }
     }
     e@Plus[] -> {
       if(cls.isEmpty() || cls.contains(`e.getClass())) {
    	   terms.add(`e);
       }
     }
     e@Minus[] -> {
       if(cls.isEmpty() || cls.contains(`e.getClass())) {
    	   terms.add(`e);
       }
     }
     e@Mult[] -> {
       if(cls.isEmpty() || cls.contains(`e.getClass())) {
    	   terms.add(`e);
       }
     }
     e@Div[] -> {
       if(cls.isEmpty() || cls.contains(`e.getClass())) {
    	   terms.add(`e);
       }
     }
     e@PropertySuffix[] -> {
       if(cls.isEmpty() || cls.contains(`e.getClass())) {
    	   terms.add(`e);
       }
     }
     e@Equal[] -> {
       if(cls.isEmpty() || cls.contains(`e.getClass())) {
    	   terms.add(`e);
       }
     }
     e@NotEqual[] -> {
       if(cls.isEmpty() || cls.contains(`e.getClass())) {
    	   terms.add(`e);
       }
     }
     e@Not_[] -> {
       if(cls.isEmpty() || cls.contains(`e.getClass())) {
    	   terms.add(`e);
       }
     }
     e@Empty[] -> {
       if(cls.isEmpty() || cls.contains(`e.getClass())) {
    	   terms.add(`e);
       }
     }
     e@If[] -> {
       if(cls.isEmpty() || cls.contains(`e.getClass())) {
    	   terms.add(`e);
       }
     }
     e@GreaterThan[] -> {
       if(cls.isEmpty() || cls.contains(`e.getClass())) {
    	   terms.add(`e);
       }
     }
     e@GreaterThanEqual[] -> {
       if(cls.isEmpty() || cls.contains(`e.getClass())) {
    	   terms.add(`e);
       }
     }
     e@LessThan[] -> {
       if(cls.isEmpty() || cls.contains(`e.getClass())) {
    	   terms.add(`e);
       }
     }
     e@LessThanEqual[] -> {
       if(cls.isEmpty() || cls.contains(`e.getClass())) {
    	   terms.add(`e);
       }
     }
     e@FunctionCall[] -> {
       if(cls.isEmpty() || cls.contains(`e.getClass())) {
    	   terms.add(`e);
       }
     }
     e@ArrayAccess[] -> {
       if(cls.isEmpty() || cls.contains(`e.getClass())) {
    	   terms.add(`e);
       }
     }
    }
  }

  public static List<Expr> collectTerms(Expr source, Class... cls) {
    try {
    	List<Expr> exprs = new ArrayList<Expr>();
    	`TopDown(FindTerms(exprs, Arrays.asList(cls))).visitLight(source);
    	return exprs;
    }catch(tom.library.sl.VisitFailure e){
      throw new RuntimeException(e);
    }
  }

}

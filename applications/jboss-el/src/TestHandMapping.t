import org.jboss.el.parser.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Assert;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class TestHandMapping extends TestCase {

  %include { EL.tom }
  %include { sl.tom }
  %include { string.tom }
  %include { java/util/types/Collection.tom }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestHandMapping.class));
  }

  public void testIdentifier() {
    Node node = ELParser.parse("#{contactSearchContext}");

    final List<Node> list = collectTerms(node, AstIdentifier.class);

    Assert.assertTrue(list.size() == 1 && list.get(0) instanceof AstIdentifier);
  }

  public void testIdentifierAndProperties() {
    Node node = ELParser.parse("#{contactSearchContext.positionGroup.syncUpdateDt}");
    final List<Node> list = collectTerms(node, AstIdentifier.class, AstPropertySuffix.class);


    Assert.assertTrue(list.size() == 3 && list.get(0) instanceof AstIdentifier &&
        list.get(1) instanceof AstPropertySuffix && list.get(2) instanceof AstPropertySuffix);
  }

  public void testEquals() {
    Node node = ELParser.parse("#{communicationContext.communication.id == null}");
    final List<Node> list = collectTerms(node);
    Assert.assertTrue(list.size() == 4 && list.get(0) instanceof AstEqual);
  }

  public void testArithmetic() {
    Node node = ELParser.parse("#{a + 2 / 3}");
    final List<Node> list = collectTerms(node);
    Assert.assertTrue(list.size() == 5 && list.get(0) instanceof AstPlus);
  }

  public void testConditional() {
    Node node = ELParser.parse("#{a != 3  ? 2 : 3}");
    final List<Node> list = collectTerms(node);
    Assert.assertTrue(list.size() == 6 && list.get(0) instanceof AstChoice);
  }

  public void testFunctionCall() {
    Node node = ELParser.parse("#{communicationContext.abc.search(searchEntityId,searchEntityCode)}");
    final List<Node> list = collectTerms(node);
    Assert.assertTrue(list.size() == 5 && list.get(0) instanceof AstValue);
  }

  public void testFunctionCallConst() {
    Node node = ELParser.parse("#{common.selectItems('Jurisdiction')}");
    final List<Node> list = collectTerms(node);
    Assert.assertTrue(list.size() == 3 && list.get(0) instanceof AstValue);
  }

  public void testNotEqual() {
    Node node = ELParser.parse("#{'query'!=contactSearchContext.contactFindType}");
    final List<Node> list = collectTerms(node);

    Assert.assertTrue(list.size() == 4 && list.get(0) instanceof AstNotEqual);
  }

  public void testNotEmpty() {
    Node node = ELParser.parse("#{not empty search.results}");
    final List<Node> list = collectTerms(node);

    Assert.assertTrue(list.size() == 4 && list.get(0) instanceof AstNot);
  }

  public void testMixedLiteral() {
    Node node = ELParser.parse("#{contactSearchList.getRowCount()} Clients Total Assets = $ #{contactSearchContext.getTotalAsset()}");
    final List<Node> list = collectTerms(node);

    Assert.assertTrue(list.size() == 5 && list.get(2) instanceof AstLiteralExpression);
  }

  public void testGreaterThan() {
    Node node = ELParser.parse("#{contacts.rowCount>0}");
    final List<Node> list = collectTerms(node);

    Assert.assertTrue(list.size() == 4 && list.get(0) instanceof AstGreaterThan);
  }

  public void testArrayAccess() {
    Node node = ELParser.parse("/images/icons/security/check_#{fsiContext.codeMap[fsiContext.getKey(fs.id, accessType)]}.PNG");
    final List<Node> list = collectTerms(node);

    Assert.assertTrue(list.size() == 10 && list.get(1) instanceof AstValue);
  }

  public void testReplaceLiteral() {
    Node l = `FunctionCall(concExpr(Identifier("bundle")),"get",concExpr(Literal("toto")));
    //Node node = ELParser.parse("#{contactSearchList.getRowCount()} Clients Total Assets = $ #{contactSearchContext.getTotalAsset()}");
    //List<Node> l = `concExpr(Value(concExpr(Literal("a"),Literal("c"))),Literal("b"));
    //deepClone(l);
    try {
      `TopDown(Debug()).visit(l, new LocalIntrospector());
    } catch(tom.library.sl.VisitFailure e ) {}
  }

  %strategy ReplaceLiteral() extends Identity() {
    visit Expr {
      Literal(s) -> {
        System.out.println("s value "+`s);
        
        return `FunctionCall(concExpr(),"get",concExpr(Literal(s)));
        //return `Literal(s);
      }

        
        //FunctionCall(concExpr(Identifier("bundle")),"get",concExpr(Literal(s)))
      /**
      { 
        List<Node> l1 = new ArrayList<Node>();
        l1.add(`Identifier("bundle"));
        List<Node> l2 = new ArrayList<Node>();
        l2.add(`Literal(s));
        return `FunctionCall(l1,"get",l2);
      }
      */
    }
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

  %strategy Debug() extends Identity() {
    visit Expr {
      x -> {
        System.out.println(getPosition());
        System.out.println(`x+"\n");
      }
    }
    visit ExprList {
      x -> {
        System.out.println(getPosition());
        System.out.println(`x+"\n");
      }
    }
  }

  public static List<Node> collectTerms(Node source, Class... cls) {
    try {
      List<Node> exprs = new ArrayList<Node>();
      //`TopDown(Sequence(Debug(),FindTerms(exprs, Arrays.asList(cls)))).visit(source, new LocalIntrospector());
      `TopDown(FindTerms(exprs, Arrays.asList(cls))).visitLight(source, new LocalIntrospector());
      return exprs;
    }catch(tom.library.sl.VisitFailure e){
      throw new RuntimeException(e);
    }
  }

}

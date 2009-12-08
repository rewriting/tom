import java.util.Collection;

import el.types.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.ArrayList;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import tom.library.sl.VisitFailure;

import org.jboss.el.parser.*;

public class Utils {

  %include { el/EL.tom }
  %include { sl.tom }
  %include { string.tom }
  %include { java/util/types/Collection.tom }
  
  public static ExprList convertElExpr(List<org.jboss.el.parser.Node> nodes, boolean tryConvert) {
    ExprList exprList = `concExpr();
//    Names stringList = `concName();
    
//    boolean allIds = true;
    for(org.jboss.el.parser.Node node : nodes) {
      Expr expr = convertElExpr(node);
      exprList = `concExpr(exprList*, expr);
//      if(expr instanceof Identifier || expr instanceof PropertySuffix) {
//        stringList = `concName(stringList*, node.getImage());
//      } else {
//        allIds = false;
//      }
    }
    //if(allIds) {
    //  return `concExpr(IdPath(stringList));
    //}
	return exprList;
  }

  /**
   *
   */
  public static List<org.jboss.el.parser.Node> getChildren(org.jboss.el.parser.Node node, int start, int end) {
    List<org.jboss.el.parser.Node> nodes = new ArrayList<org.jboss.el.parser.Node>();
    if(end == -1) {
      end = node.jjtGetNumChildren();
    }
    for(; start < end; start++) {
      nodes.add(node.jjtGetChild(start));
    }
    return nodes;
  }

  /**
   *
   */
  public static Expr convertElExpr(org.jboss.el.parser.Node node) {
	  if(node instanceof org.jboss.el.parser.AstCompositeExpression) {

      ExprList nested = `concExpr();
      for(int c = 0; c < node.jjtGetNumChildren(); c++) {
        Expr child = (Expr)convertElExpr(node.jjtGetChild(c));
        nested = `concExpr(nested*, child);
      }
		  return `CompositeExpression(nested);
	  }
	  if(node instanceof org.jboss.el.parser.AstDeferredExpression) {
		  assert node.jjtGetNumChildren() == 1;
		  return `DeferredExpression(convertElExpr(((AstDeferredExpression)node).jjtGetChild(0)));
	  }
	  if(node instanceof org.jboss.el.parser.AstValue) {
          // Check if FunctionCall
          org.jboss.el.parser.Node suffix = node.jjtGetChild(node.jjtGetNumChildren() - 1);
          if(suffix instanceof AstMethodSuffix) {
            ExprList ids = convertElExpr(getChildren(node, 0, node.jjtGetNumChildren() - 1), true);
            ExprList args = convertElExpr(getChildren(suffix, 0, -1), false);
            return `FunctionCall(ids, suffix.getImage(), args);
          }
          if(suffix instanceof AstBracketSuffix) {
            ExprList ids = convertElExpr(getChildren(node, 0, node.jjtGetNumChildren() - 1), true);
            Expr arg = convertElExpr(suffix.jjtGetChild(0));
            return `ArrayAccess(ids, arg);
          }

          ExprList values = `concExpr();
          for(int c = 0; c < node.jjtGetNumChildren(); c++) {
            Expr child = (Expr)convertElExpr(node.jjtGetChild(c));
            values = `concExpr(values*, child);
          }

          return `Value(values);
	  }
	  if(node instanceof org.jboss.el.parser.AstIdentifier) {
		  assert node.jjtGetNumChildren() == 0;
		  return `Identifier(node.getImage());
	  }
	  if(node instanceof org.jboss.el.parser.AstNull) {
		  assert node.jjtGetNumChildren() == 0;
		  return `Null();
	  }
	  if(node instanceof org.jboss.el.parser.AstPropertySuffix) {
		  assert node.jjtGetNumChildren() == 0;
		  return `PropertySuffix(node.getImage());
	  }
	  if(node instanceof org.jboss.el.parser.AstChoice) {
		  assert node.jjtGetNumChildren() == 3;
		  return `If(convertElExpr(node.jjtGetChild(0)), 
				  convertElExpr(node.jjtGetChild(1)), 
				  convertElExpr(node.jjtGetChild(2)));
	  }


	  // Boolean expressions
	  {
	    Expr expr = convertElBooleanExpr(node);
	    if(expr != null) {
	      return expr;
	    }
	  }

	  // Arithmetic expressions
	  {
	    Expr expr = convertElArithmeticExpr(node);
	    if(expr != null) {
	      return expr;
	    }
	  }

      throw new RuntimeException("Could not handle " + node);
  }

  /**
   *
   */
  public static Expr convertElBooleanExpr(org.jboss.el.parser.Node node) {
	  
	  if(node instanceof org.jboss.el.parser.AstEqual) {
		  assert node.jjtGetNumChildren() == 2;
		  return `Equal(convertElExpr(node.jjtGetChild(0)), convertElExpr(node.jjtGetChild(1)));
	  }
	  if(node instanceof org.jboss.el.parser.AstNotEqual) {
		  assert node.jjtGetNumChildren() == 2;
		  return `NotEqual(convertElExpr(node.jjtGetChild(0)), convertElExpr(node.jjtGetChild(1)));
	  }
	  if(node instanceof org.jboss.el.parser.AstNot) {
		  assert node.jjtGetNumChildren() == 1;
		  return `Not_(convertElExpr(node.jjtGetChild(0)));
	  }
	  if(node instanceof org.jboss.el.parser.AstEmpty) {
		  assert node.jjtGetNumChildren() == 1;
		  return `Empty(convertElExpr(node.jjtGetChild(0)));
	  }

	  return null;
  }

  /**
   *
   */
  public static Expr convertElArithmeticExpr(org.jboss.el.parser.Node node) {
	  // Arithmetic expressions
	  if(node instanceof org.jboss.el.parser.AstPlus) {
		  assert node.jjtGetNumChildren() == 2;
		  return `Plus(convertElExpr(node.jjtGetChild(0)), convertElExpr(node.jjtGetChild(1)));
	  }
	  if(node instanceof org.jboss.el.parser.AstMinus) {
		  assert node.jjtGetNumChildren() == 2;
		  return `Minus(convertElExpr(node.jjtGetChild(0)), convertElExpr(node.jjtGetChild(1)));
	  }
	  if(node instanceof org.jboss.el.parser.AstMult) {
		  assert node.jjtGetNumChildren() == 2;
		  return `Mult(convertElExpr(node.jjtGetChild(0)), convertElExpr(node.jjtGetChild(1)));
	  }
	  if(node instanceof org.jboss.el.parser.AstDiv) {
		  assert node.jjtGetNumChildren() == 2;
		  return `Div(convertElExpr(node.jjtGetChild(0)), convertElExpr(node.jjtGetChild(1)));
	  }

	  // Relations expressions
	  if(node instanceof org.jboss.el.parser.AstGreaterThan) {
		  assert node.jjtGetNumChildren() == 2;
		  return `GreaterThan(convertElExpr(node.jjtGetChild(0)), convertElExpr(node.jjtGetChild(1)));
	  }
    // 
	  if(node instanceof org.jboss.el.parser.AstLiteralExpression) {
		  assert node.jjtGetNumChildren() == 0;
		  return `Literal(node.getImage());
	  }
	  if(node instanceof org.jboss.el.parser.AstInteger) {
		  assert node.jjtGetNumChildren() == 0;
		  return `Integer(node.getImage());
	  }
	  if(node instanceof org.jboss.el.parser.AstString) {
		  assert node.jjtGetNumChildren() == 0;
		  return `Str(node.getImage());
	  }

	  return null;
  }
}

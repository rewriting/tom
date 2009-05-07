import java.util.Collection;

import el.types.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.ArrayList;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import tom.library.sl.VisitFailure;
import xml.*;
import tnode.types.*;

import org.apache.jasper.compiler.Node;
import org.jboss.el.parser.*;

public class Utils {

  %include { tnode/TNode.tom }
  %include { el/EL.tom }
  //%include { tnode/_TNode.tom }
  %include { sl.tom }
  %include { string.tom }
  %include { java/util/types/Collection.tom }
  
  public static HTMLDocument xmlStringToDocument(String htmlText)
		throws SAXException, IOException {
    DOMParser parser = new DOMParser();
    parser.parse(new InputSource(new StringReader(htmlText)));
    final HTMLDocument document = (HTMLDocument)parser.getDocument();
    return document;
  }

  public static TNode xmlStringToTNode(final String xmlString) 
  	throws VisitFailure {

    try {	
      InputStream is = new ByteArrayInputStream(xmlString.getBytes("utf-8"));
      TNode tnodeDoc = (TNode)new XmlTools().convertXMLToTNode(is);        
      return tnodeDoc;
    } catch(UnsupportedEncodingException e) {
    	throw new VisitFailure(e.getMessage());
    } 
  }

  public static TNode getTagLibVersion(TNode source, Collection<String> tagLibNames){
    TNode result = null;
    try{
      result = (TNode)`InnermostId(ConvertToTagLibs(tagLibNames)).visitLight(source);
    }catch(tom.library.sl.VisitFailure e){
      throw new RuntimeException("getTagLibVersion failed:" + e.getMessage());
    }
    return result;
  }
  
  %strategy ConvertToTagLibs(tagLibNames:Collection) extends Identity(){
    visit TNode{
      // Taglib fromXML form.
      ElementNode(name,attrList,childList) -> {

         // Match elements with only one attribute named "hashcode".
         %match(attrList) {
    	   concTNode(attr@AttributeNode[Name="hashcode", Value=hashCode]) -> {

    		   //System.out.println("Convert from XML form " + `name + "/" + `attrList);
    		   Node.CustomTag tag = com.redline.codebraces.jsptools.extractor.jasper.JasperCompiler.getTag(`hashCode);
    		   // Build the attrList from the data in the Jasper taglib object
    		   TNodeList attrListNew = `concTNode();
    		   for (int a = 0; a < tag.getAttributes().getLength(); a++) {

    			   TNode tnode = `AttributeNode(tag.getAttributes().getLocalName(a), "true", 
    					   tag.getAttributes().getValue(a));
    			   attrListNew = `concTNode(attrListNew*, tnode);
    		   }
		       // Transform name to the same format as the names in 'tagLibNames'
		       `name = `name.replace(":", "_").toUpperCase();
		       
    		   return `TagLib(name,attrListNew,childList);
    	   }
         }
         //if (tagLibNames.contains(`name)) {
//           return `TagLib(name,attrList,childList);
         //} 
      }
      // Taglib(s) from placeholder form (@{...}@) 
      tx@TextNode(str) -> {        
        TNode tagList = `concTagLib();
        %match(str) {
        	concString(_*,'@','{',X*,'}','@',_*) && !concString(_*,'@',_*) << X   -> { 
        		//System.out.println("Convert from placeholder form " + `X*);
        		TNode tnode = xmlStringToTNode(`concString('<', X*, '/', '>')); 
        		tagList = `concTagLib(tagList*,tnode); 
        	}
		}
        // If there are no taglibs embedded then leave it unchanged
        return tagList == `concTagLib() ? `tx : tagList;  
      }      
    }
  } 
  
  public static void printTagLibsForValue(TNode source,String value) {    
    try {
      `TopDown(PrintTagLibsForValue(value)).visitLight(source);
    }catch(tom.library.sl.VisitFailure e){
      System.out.println("printTagLibsForValue failed:" + e.getMessage());
    }
  }
  
  %strategy PrintTagLibsForValue(value:String) extends Identity() {
    visit TNode {
      tl@TagLib[AttrList=concTNode(_*,AttributeNode[Value=TextNode(str)],_*)] -> {        
        if (`str.contains(value)) {
          System.out.println(`tl);
        }
      }
    }
  }
   
  public static List<TNode> collectTaglibCallsByName(TNode source,String name) {    
    try {
       List<TNode> taglibs = new ArrayList<TNode>();
       //System.out.println("collectTaglibCallsByName " + name);
      `TopDown(FindTagLibsWithName(name, taglibs)).visitLight(source);
       return taglibs;
    }catch(tom.library.sl.VisitFailure e){
      System.out.println("collectTagLibsByName failed:" + e.getMessage());
      return null;
    }
  }

  %strategy FindTagLibsWithName(name:String, taglibs:Collection) extends Identity() {
    visit TNode {
      tl@TagLib[Name=name_] && name_ << String name -> {        
//	      System.out.println("Found by name " + name + "-" + `tl.getName());
	      taglibs.add(`tl);
      } 
    }
  }

  public static List<TNode> findTagLibsInChunk(TNode source) {
    try {
    	List<TNode> taglibs = new ArrayList<TNode>();
    	`TopDown(FindTagLibsInChunk(taglibs)).visitLight(source);
    	return taglibs;
    }catch(tom.library.sl.VisitFailure e){
      throw new RuntimeException(e);
    }
  }

  %strategy FindTagLibsInChunk(taglibs:Collection) extends Identity() {
    visit TNode {
      ElementNode[ChildList=concTNode(X*,
         ElementNode[Name="SCRIPTCHUNK",ChildList=concTNode(_*,TextNode(codeChunk),_*)], Y*)] -> {
         if(`codeChunk.contains("help()") ) {
        	 `TopDown(FindTagLibHelp(taglibs)).visitLight(`ElementNode("",concTNode(),concTNode(X*,Y*)));
         }
      }
    }
  }

  %strategy FindTagLibHelp(taglibs:Collection) extends Identity() {
	visit TNode {
     tl@TagLib[] -> { 
    	 System.out.println("Found taglib in help() code :" + `tl); 
    	 taglibs.add(`tl);
     }
    } 
  }

  /**
   *
   */
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

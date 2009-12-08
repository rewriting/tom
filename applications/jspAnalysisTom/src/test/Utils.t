package test;

import java.util.Collection;

import test.tnode.*;
import test.tnode.types.*;
import tom.library.sl.*;

public class Utils{

  %include { tnode/TNode.tom }
  %include { sl.tom }
  %include { string.tom }
  %include { Collection.tom }
  
  public static TNode getTagLibVersion(TNode source, Collection<String> tagLibNames){
    TNode result = null;
    try{
      result = (TNode)`TopDown(ConvertToTagLibs(tagLibNames)).visitLight(source);
    }catch(tom.library.sl.VisitFailure e){
      throw new RuntimeException("getTagLibVersion failed:" + e.getMessage());
    }
    return result;
  }
  
  %strategy ConvertToTagLibs(tagLibNames:Collection) extends Identity(){
    visit TNode{
      en@ElementNode(name,attrList,childList) -> {
        if (tagLibNames.contains(`name)) {
          return `TagLib(name,attrList,childList);
        }        
      }
      TextNode(concString(_*,'@','{',X*,'}','@',_*)) -> {        
        return getTagLibFromStr(`X*);
      }      
    }
  } 
  
  private static TNode getTagLibFromStr(String str) {
    String[] strArray = str.split(" ");    
    TNodeList attrList = `concTNode();
    for(int i = 1; i < strArray.length; i ++){
      String[] attrNameValue = strArray[i].split("=");
      if ( attrNameValue.length < 2 ) { continue; }
      attrList = `concTNode(attrList*,AttributeNode(attrNameValue[0], "true", TextNode(attrNameValue[1])));
    }
    String tagLibName = strArray[0].replace(':','_');
    return `TagLib(tagLibName,attrList,concTNode());
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
  
  public static void findTagLibsInChunk(TNode source) {    
    try {
      `TopDown(FindTagLibsInChunk()).visitLight(source);
    }catch(tom.library.sl.VisitFailure e){
      System.out.println("findTagLibsInChunk failed:" + e.getMessage());
    }
  }
  
  %strategy FindTagLibsInChunk() extends Identity() {
    visit TNode {
      ElementNode[ChildList=concTNode(X*,el,Y*)] 
                  && ElementNode[Name="SCRIPTCHUNK",ChildList=concTNode(_*,TextNode(concString(_*,'help()',_*)),_*)] << el -> {
        `TopDown(PrintTagLibHelp()).visitLight(`ElementNode("",concTNode(),concTNode(X*,Y*)));
      }
    }    
  } 
  
  %strategy PrintTagLibHelp() extends Identity() {
    visit TNode {
      tl@TagLib[] -> {        
        System.out.println("Found :" + `tl);
      }
    }    
  }
}
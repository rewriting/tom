package parsingtests;

import java.io.*;
import java.util.*;

public class Gen {

  %include { string.tom }

  public static void main(String[] args) {
    Gen gen = new Gen();
    gen.gen(args[0]);
  }

  public void gen(String fileName) {
    // use the first argument as input file
    Map tokenMap = new HashMap();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(fileName));
      String line = "";
      while(line != null) {
        line = reader.readLine();
        %match(String line) {
          ('//',_*) -> { /* comment */}
          (name*,'=',value*) -> {
            Integer val = Integer.valueOf(`value);
            tokenMap.put(val,`name);
          }
        }
      }
    } catch (Exception e) {
      System.out.println("Exception "+e);
    }
    System.out.println(tokenMap);
    generateTable(tokenMap);
    generateTomMapping(tokenMap);
  }

  void generateTable(Map tokenMap) {
    StringBuffer out = new StringBuffer();

    out.append(%[
package parsingtests;

public class TokenTable {
  private static java.util.HashMap tokenMap = null;

  private static java.util.HashMap initTokenMap() {
    tokenMap = new java.util.HashMap();
@initMap("tokenMap",tokenMap)@
    return tokenMap;
  }
  public static java.util.Map getTokenMap() {
    if (tokenMap == null) {
      tokenMap = initTokenMap();
    }
    return (java.util.Map)tokenMap.clone();
  }

}

]%);
    try {
      Writer writer = new BufferedWriter(
          new FileWriter("parsingtests/TokenTable.java"));
      writer.write(out.toString());
      writer.close();
    } catch (Exception e) {
      System.out.println("Write failed "+e);
      e.printStackTrace();
    }
  }

  private String initMap(String mapName, Map tokMap) {
    StringBuffer out = new StringBuffer();
    Iterator it = tokMap.keySet().iterator();
    while(it.hasNext()) {
      Integer key = (Integer)it.next();
      String value = (String)tokMap.get(key);
      out.append(%[
    @mapName@.put(new Integer(@key.intValue()@),"@value@");]%);
    }
    return out.toString();
  }

  void generateTomMapping(Map tokMap) {
    StringBuffer out = new StringBuffer();

    out.append(%[
  %include{ int.tom }
  %include{ string.tom }
  %include{ aterm.tom }
  %include{ atermlist.tom }

  %oplist ATermList concATerm (ATerm*){
    is_fsym(t) { t instanceof ATermList } 
    make_empty() { aterm.pure.SingletonFactory.getInstance().makeList() }
    make_insert(e,l) { l.insert(e) }
    get_head(t) { t.getFirst() }
    get_tail(t) { t.getNext() }
    is_empty(t) { t.isEmpty() } 
  }
  
  %op ATerm NodeInfo(text:String,line:int,column:int) {
    is_fsym(t) { (t != null) && ((ATermAppl)t).getAFun() == SingletonFactory.getInstance().makeAFun("NodeInfo",3,false) }
    get_slot(text, t) { ((ATermAppl)((ATermAppl)t).getArgument(0)).getAFun().getName() }
    get_slot(line, t) { ((ATermInt)((ATermAppl)t).getArgument(1)).getInt() }
    get_slot(column, t) { ((ATermInt)((ATermAppl)t).getArgument(2)).getInt() }
    make(t,l,c) { SingletonFactory.getInstance().makeAppl(SingletonFactory.getInstance().makeAFun("NodeInfo",3,false),SingletonFactory.getInstance().makeAppl(SingletonFactory.getInstance().makeAFun(t,0,true)),SingletonFactory.getInstance().makeInt(l),SingletonFactory.getInstance().makeInt(c)) }
  }
  
  ]%);

    Iterator it = tokMap.keySet().iterator();
    while(it.hasNext()) {
      Integer key = (Integer)it.next();
      String value = (String)tokMap.get(key);
      out.append(%[
  %op ATerm @value@(info:ATerm,childs:ATermList) {
    is_fsym(t) { (t != null) && ((ATermAppl)t).getAFun() == SingletonFactory.getInstance().makeAFun("@value@",2,false) }
    get_slot(info, t) { ((ATermAppl)t).getArgument(0) }
    get_slot(childs, t) { (ATermList)((ATermAppl)t).getArgument(1) }
    make(i,c) {SingletonFactory.getInstance().makeAppl(SingletonFactory.getInstance().makeAFun("@value@",2,false),i,c) }
  }]%);
    }

    try {
      Writer writer = new BufferedWriter(
          new FileWriter("parsingtests/Mapping.tom"));
      writer.write(out.toString());
      writer.close();
    } catch (Exception e) {
      System.out.println("Write failed "+e);
      e.printStackTrace();
    }
  }
}

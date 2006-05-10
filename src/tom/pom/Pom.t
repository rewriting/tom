/*
 * Pom
 * 
 * Copyright (c) 2005-2006, INRIA
 * Nancy, France.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 * 
 * Yoann Toussaint    e-mail: Yoann.Toussaint@loria.fr
 * 
 **/

package tom.pom;

import java.io.*;
import java.util.*;

public class Pom {

  private String fileName;
  private String destDir;
  private String packagePrefix;

  %include { string.tom }

  public static void main(String[] args) {
    exec(args);
  }
  public static int exec(String[] args) {
    String srcfile= null;
    String  destdir= null;
    String packageP= null;

    for (int i=0;i<java.lang.reflect.Array.getLength(args);i++) {
      if (args[i].equals("--srcfile")) {
        srcfile = args[i+1];
      }
      if (args[i].equals("--destdir")) {
        destdir = args[i+1];
      }
      if (args[i].equals("--package")) {
        packageP = args[i+1];
      }
    }
    Pom pom = new Pom(srcfile,destdir,packageP);
    pom.gen();
    return 0;//no errors
  }

  public Pom(String fileN, String destD,String packageP){
    this.fileName = fileN;
    this.destDir = destD;
    this.packagePrefix = packageP;
  }

  public void gen() {
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
package @packagePrefix@;

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
          new FileWriter(packagePrefix + "/TokenTable.java"));
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
          new FileWriter(packagePrefix + "/Mapping.tom"));
      writer.write(out.toString());
      writer.close();
    } catch (Exception e) {
      System.out.println("Write failed "+e);
      e.printStackTrace();
    }
  }
}

/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2003  LORIA (CNRS, INPL, INRIA, UHP, U-Nancy 2)
			     Nancy, France.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr

*/

package jtom;
  
import java.util.*;
import java.io.*;

import aterm.*;
import aterm.pure.*;

import jtom.tools.*;
import jtom.exception.*;
import jtom.verifier.*;
import jtom.adt.*;

public class TomBase {
  private TomEnvironment tomEnvironment;
  private TomList empty;
  
  private static List emptyList = new ArrayList();
  protected final static boolean debug = false;

  public TomBase(TomEnvironment tomEnvironment) {
    this.tomEnvironment = tomEnvironment;
    this.empty = tsf().makeTomList_Empty();
  }

  protected ASTFactory ast() {
    return tomEnvironment.getASTFactory();
  }

  protected TomSignatureFactory tsf() {
    return tomEnvironment.getTomSignatureFactory();
  }

  protected TomSignatureFactory getTomSignatureFactory() {
    return tsf();
  }
  
  protected Statistics statistics() {
    return tomEnvironment.getStatistics();
  }

  protected SymbolTable symbolTable() {
    return tomEnvironment.getSymbolTable();
  }

  protected boolean isIntType(String type) {
    return type.equals("int");
  }

  protected boolean isBoolType(String type) {
    return type.equals("bool");
  }

  protected TomType getIntType() {
    return symbolTable().getType("int");
  }

  protected TomType getBoolType() {
    return symbolTable().getType("bool");
  }
  
  protected TomType getUniversalType() {
    return symbolTable().getType("universal");
  }

  protected TomTerm makeNumber(int n) {
    return tsf().makeTomTerm_Number(new Integer(n));
  }
  
  protected TomList empty() {
    return empty;
  }

  protected TomList cons(TomTerm t, TomList l) {
    if(t!=null) {
      return tsf().makeTomList_Cons(t,l);
    } else {
      return l;
    }
  }

  protected TomList append(TomTerm t, TomList l) {
    if(l.isEmpty()) {
      return cons(t,l);
    } else {
      return cons(l.getHead(), append(t,l.getTail()));
    }
  }

  protected TomList concat(TomList l1, TomList l2) {
    if(l1.isEmpty()) {
      return l2;
    } else {
      return cons(l1.getHead(), concat(l1.getTail(),l2));
    }
  }

  protected TomList reverse(TomList l) {
    TomList reverse = empty();
    while(!l.isEmpty()){
      reverse = cons(l.getHead(),reverse);
      l = l.getTail();
    }
    return reverse;
  }

  protected int length(TomList l) {
    if(l.isEmpty()) {
      return 0;
    } else {
      return 1 + length(l.getTail());
    }
  }
  
// ------------------------------------------------------------
  %include { Tom.signature }
// ------------------------------------------------------------

  protected void debugPrintln(String s) {
    if(debug) {
      System.out.println(s);
    }
  }
  
  protected String getTomType(TomType type) {
    return type.getTomType().getString();
  }

  protected String getTLType(TomType type) {
    return getTLCode(type.getTlType());
  }

  protected String getTLCode(TomType type) {
    %match(TomType type) {
      TLType(TL[code=tlType])  -> { return tlType; }
      TLType(ITL[code=tlType]) -> { return tlType; }
      _ -> {
        System.out.println("getTLType error on term: " + type);
        System.exit(1);
      }
    }
    return null;
  }

  
  protected TomType getSymbolCodomain(TomSymbol symbol) {
    return symbol.getTypesToType().getCodomain();
  }

  protected TomList getSymbolDomain(TomSymbol symbol) {
    if(symbol!=null) {
      return symbol.getTypesToType().getList();
    } else {
      System.out.println("getSymbolDomain: symbol = " + symbol);
      return empty();
    }
  }

  protected String getSymbolCode(TomSymbol symbol) {
      //%variable
    %match(TomSymbol symbol) {
      Symbol[tlCode=TL[code=tlCode]]  -> { return tlCode; }
      Symbol[tlCode=ITL[code=tlCode]] -> { return tlCode; }
      _ -> {
        System.out.println("getSymbolCode error on term: " + symbol);
        System.exit(1);
      }
    }
    return null;
  }

  protected TomType getTermType(TomTerm t) {
      //%variable
    %match(TomTerm t) {
      Appl(option, Name(tomName),subterms) -> {
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        return tomSymbol.getTypesToType().getCodomain();
      }
      
      Variable(option,name,type) -> { return type; }
      
      VariableStar(option,name,type) -> { return type; }
      
      UnamedVariable(option,type) -> { return type; }
      
      _ -> {
        System.out.println("getTermType error on term: " + t);
        System.exit(1);
      }
    }
    return null;
  }

  private HashMap numberListToIdentifierMap = new HashMap();

  private String elementToIdentifier(TomTerm subject) {
    %match(TomTerm subject) {
      Begin(Number(i)) -> { return "begin" + i; }
      End(Number(i)) -> { return "end" + i; }
      MatchNumber(Number(i)) -> { return "match" + i; }
      PatternNumber(Number(i)) -> { return "pattern" + i; }
      ListNumber(Number(i)) -> { return "list" + i; }
      IndexNumber(Number(i)) -> { return "index" + i; }
      AbsVar(Number(i)) -> { return "absvar" + i; }
      RenamedVar(Name(name)) -> { return "renamedvar_" + name; }
      RuleVar() -> { return "rulevar"; }
      Number(i) -> { return "" + i; }
      _ -> { return subject.toString(); }
    }
  }
  
  protected String numberListToIdentifier(TomList l) {
    String res = (String)numberListToIdentifierMap.get(l);
    if(res == null) {
      TomList key = l;
      StringBuffer buf = new StringBuffer(30);
      while(!l.isEmpty()) {
        buf.append("_" + elementToIdentifier(l.getHead()));
        l = l.getTail();
      }
      res = buf.toString();
      numberListToIdentifierMap.put(key,res);
    }
    return res;
  }

  protected boolean isListOperator(TomSymbol subject) {
    //%variable
    if(subject==null) {
      return false;
    }
    %match(TomSymbol subject) {
      Symbol[option=Option(optionList)] -> {
        while(!optionList.isEmptyOptionList()) {
          Option opt = optionList.getHead();
          %match(Option opt) {
            DeclarationToOption(MakeEmptyList[]) -> { return true; }
            DeclarationToOption(MakeAddList[])   -> { return true; }
          }
          optionList = optionList.getTail();
        }
        return false;
      }
      
      _ -> {
        System.out.println("isListOperator: strange case: '" + subject + "'");
        System.exit(1);
      }
    }
    return false;
  }

  protected boolean isArrayOperator(TomSymbol subject) {
    //%variable
    if(subject==null) {
      return false;
    }
    %match(TomSymbol subject) {
      Symbol[option=Option(optionList)] -> {
        while(!optionList.isEmptyOptionList()) {
          Option opt = optionList.getHead();
          %match(Option opt) {
            DeclarationToOption(MakeEmptyArray[]) -> { return true; }
            DeclarationToOption(MakeAddArray[])   -> { return true; }
          }
          optionList = optionList.getTail();
        }
        return false;
      }
      
      _ -> {
        System.out.println("isArrayOperator: strange case: '" + subject + "'");
        System.exit(1);
      }
    }
    return false;
  }

    /*
     * collects something in table
     * returns false if no more traversal is needed
     * returns true  if traversal has to be continued
     */
  protected interface Collect {
    boolean apply(ATerm t);
  }

  protected interface Replace {
    ATerm apply(ATerm t) throws TomException;
  }

  protected TomList tomListMap(TomList subject, Replace replace) {
    TomList res = subject;
    try {
      if(!subject.isEmpty()) {
        TomTerm term = (TomTerm) replace.apply(subject.getHead());
        TomList list = tomListMap(subject.getTail(),replace);
        res = cons(term,list);
      }
    } catch(Exception e) {
      System.out.println("tomListMap error: " + e);
      e.printStackTrace();
      System.exit(0);
    }
    return res;
  }
  
    /*
     * Apply a function to each element of a list
     */
  protected ATermList genericMap(ATermList subject, Replace replace) {
      /*
        %match(TomList subject) {
        conc()      -> { return empty(); }
        conc(t,l*)  -> { return cons(replace.apply(t), map(l,replace)); }
        _ -> {
        System.out.println("TomBase.map error on term: " + subject);
        System.exit(1);
        }
        }
        return null;
      */
    ATermList res = subject;
    try {
      if(!subject.isEmpty()) {
        ATerm term = replace.apply(subject.getFirst());
        ATermList list = genericMap(subject.getNext(),replace);
        res = list.insert(term);
      }
    } catch(Exception e) {
      System.out.println("genericMap error: " + e);
      e.printStackTrace();
      System.exit(0);
    }
    return res;
  }

    /*
     * Apply a function to each subterm of a term
     */
  protected ATermAppl genericMapterm(ATermAppl subject, Replace replace) {
    try {
      ATerm newSubterm;
      for(int i=0 ; i<subject.getArity() ; i++) {
        newSubterm = replace.apply(subject.getArgument(i));
        if(newSubterm != subject.getArgument(i)) {
          subject = subject.setArgument(newSubterm,i);
        }
      }
    } catch(Exception e) {
      System.out.println("genericMapterm error: " + e);
      e.printStackTrace();
      System.exit(0);
    }
    return subject;
  }
  
    /*
     * Traverse a subject and collect
     * %all(subject, collect(vTable,subject,f)); 
     */
  protected void genericCollect(ATerm subject, Collect collect) { 
    if(collect.apply(subject)) { 
      if(subject instanceof ATermAppl) { 
        ATermAppl subjectAppl = (ATermAppl) subject; 
        for(int i=0 ; i<subjectAppl.getArity() ; i++) {
          ATerm term = subjectAppl.getArgument(i);
          genericCollect(term,collect); 
        } 
      } else if(subject instanceof ATermList) { 
        ATermList subjectList = (ATermList) subject; 
        while(!subjectList.isEmpty()) { 
          genericCollect(subjectList.getFirst(),collect); 
          subjectList = subjectList.getNext(); 
        } 
      } else { 
          //System.out.println("genericCollect(subject) with subject instanceof: " + subject.getClass()); 
          //System.exit(1); 
      } 
    } 
  } 

    /*
     * Traverse a subject and replace
     */
  protected ATerm genericTraversal(ATerm subject, Replace replace) {
    ATerm res = subject;
    try {
      if(subject instanceof ATermAppl) { 
        res = genericMapterm((ATermAppl) subject,replace);
      } else if(subject instanceof ATermList) {
        res = genericMap((ATermList) subject,replace);
      }
    } catch(Exception e) {
      System.out.println("traversal error: " + e);
      System.exit(0);
    }
    return res;
  } 

  protected Declaration getIsFsymDecl(OptionList optionList) {
    //%variable
    while(!optionList.isEmptyOptionList()) {
      Option subject = optionList.getHead();
      %match(Option subject) {
        DeclarationToOption(decl@IsFsymDecl[]) -> { return decl; }
      }
      optionList = optionList.getTail();
    }
    return null;
  }

  protected Option getLRParen(OptionList optionList) {
    //%variable
    while(!optionList.isEmptyOptionList()) {
      Option subject = optionList.getHead();
      %match(Option subject) {
        LRParen[] -> { return subject; }
      }
      optionList = optionList.getTail();
    }
    return null;
  }

  protected TomList getSlotList(OptionList optionList) {
    //%variable
    while(!optionList.isEmptyOptionList()) {
      Option subject = optionList.getHead();
      %match(Option subject) {
        SlotList(l) -> { return l; }
      }
      optionList = optionList.getTail();
    }
    return null;
  }


  protected TomTerm getSlotName(OptionList optionList, int number) {
    //%variable
    TomList slotList = getSlotList(optionList);
      /*
    if(slotList == null) {
      System.out.println("slotList == null");
    } else {
      System.out.println("number = " + number);
        //System.out.println("length = " + slotList.getLength());
    }
      */

    if(slotList == null) {
      return null;
    }
      //System.out.println("slotList = " + slotList);
    for(int index = 0; !slotList.isEmpty() && index<number ; index++) {
      slotList = slotList.getTail();
    }
    if(slotList.isEmpty()) {
      return null;
    }
    
    TomTerm slotNameAST = slotList.getHead();    
    
      //System.out.println("slotNameAST = " + slotNameAST);

    while(!optionList.isEmptyOptionList()) {
      Option subject = optionList.getHead();
      %match(Option subject) {
        DeclarationToOption(GetSlotDecl[slotName=name]) -> {
            //System.out.println("name = " + name);
          if(slotNameAST.equals(name)) {
            return name; 
          }
        }
      }
      optionList = optionList.getTail();
    }
    return null;
  }
  
  protected int getSlotIndex(OptionList optionList, String slotName) {
    //%variable
    int index = -1;
    TomList slotList = getSlotList(optionList);

      //System.out.println("slotList = " + slotList);
      //System.out.println("slotName = " + slotName);
    
    if(slotList == null) {
      return index;
    }


    if(slotList.isEmpty()) {
      System.out.println("getSlotIndex: strange");
    }
    index = 0;
    while(!slotList.isEmpty()) {
      String name = slotList.getHead().getString();
        // System.out.println("index = " + index + " name = " + name);
      if(slotName.equals(name)) {
        return index; 
      }
      slotList = slotList.getTail();
      index++;
    }
    System.out.println("Warning: slot '" + slotName + "' not found");
    return index;
  }


}
 

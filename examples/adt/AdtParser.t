package adt;

import aterm.*;
import aterm.pure.*;
import adt.adt.*;
import adt.adt.types.*;
import adt.vas.*;
import adt.vas.types.*;

import java.util.*;
import java.io.*;

public class AdtParser {
 private AdtFactory adtFactory;
 private VasFactory vasFactory;

 %include {adt.tom}
 %include {vas.tom}

 public final static void main(String[] args) {
   AdtParser parser = new AdtParser(AdtFactory.getInstance(SingletonFactory.getInstance()),
                                    VasFactory.getInstance(SingletonFactory.getInstance()));
   parser.test();
 }

  public AdtParser(AdtFactory adtFactory, VasFactory vasFactory) {
   this.adtFactory = adtFactory;
   this.vasFactory = vasFactory;
 }

  private final AdtFactory getAdtFactory() {
    return adtFactory;
  }

  private final VasFactory getVasFactory() {
    return vasFactory;
  }

 private void test() {
   Entry entry = adtFactory.EntryFromString("constructor(Stuff,myfun,myfun(<s(T)>,<t(T)>,<u(T)>))"); //Quelle syntaxe ?
   %match(Entry entry) {
     constructor(sort,opname,syntax) -> { 
       String stringName = ((ATermAppl)opname).getName();
       String codomainString = ((ATermAppl)sort).getName();
       FieldList fieldList = buildFieldList(((ATermAppl)syntax).getArguments());
       Production prod =  `Production(stringName, fieldList, VasType(codomainString));

       System.out.println(prod);
     }
   }
 }
  
  private FieldList buildFieldList(ATermList subtermList) {
    if(subtermList.isEmpty()) {
      return `concField();
    } else {
      ATermPlaceholder arg = (ATermPlaceholder) subtermList.getFirst();
      String argname = ((ATermAppl)arg.getPlaceholder()).getName();
      String argtype = ((ATermAppl)((ATermAppl)arg.getPlaceholder()).getArgument(0)).getName();
      
      return `manyFieldList(NamedField(argname,VasType(argtype)), buildFieldList(subtermList.getNext()));
    }
  }

  /*

   FieldList res = `concField();
   for(int i = 0 ; i<expression.getArity() ; i++) {
     ATermPlaceholder arg = (ATermPlaceholder)expression.getArgument(i);
     String argname = ((ATermAppl)arg.getPlaceholder()).getName();
     String argtype = ((ATermAppl)((ATermAppl)arg.getPlaceholder()).getArgument(0)).getName();
     res = res.append(`NamedField(argname,VasType(argtype)));
   }

   return res;
  */

}

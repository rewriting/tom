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
  private VasTypeList typeList;
  private ProductionList prodList; 

  %include {Adt.tom}
  %include {Vas.tom}

  public final static void main(String[] args) {
    AdtParser parser = new AdtParser(AdtFactory.getInstance(SingletonFactory.getInstance()),
                                     VasFactory.getInstance(SingletonFactory.getInstance()));
    parser.test();
  }
  
  public AdtParser(AdtFactory adtFactory, VasFactory vasFactory) {
    this.adtFactory = adtFactory;
    this.vasFactory = vasFactory;
    typeList = `concVasType();
    prodList = `concProduction();
  }
  
  private final AdtFactory getAdtFactory() {
    return adtFactory;
  }

  private final VasFactory getVasFactory() {
    return vasFactory;
  }
  
  private void test() {
    toModularAdt("[constructor(Nat,zero,zero),constructor(Nat,suc,suc(<pred(Nat)>))]","Peano");
  }

  private void toModularAdt(String input, String inputName) {
    Entries entries = adtFactory.EntriesFromString(input);
    %match(Entries entries) {
      concEntry(_*,entry,_*) -> {
        Production prod =  buildEntry(`entry);
        prodList = prodList.append(prod);
      }
    }
    VasModule vm = `VasModule(VasModuleName(inputName),
                              concSection(Imports(concImportedVasModule()),
                                          Public(concGrammar(Sorts(typeList),Grammar(prodList)))));
    System.out.println(vm);   
  }
  
  private Production buildEntry(Entry entry) {
    %match(Entry entry) {
      constructor(sort,opname,syntax) -> { 
       String stringName = ((ATermAppl)opname).getName();
       String codomainString = ((ATermAppl)sort).getName();
       FieldList fieldList = buildFieldList(((ATermAppl)syntax).getArguments());
       appendType(codomainString);
       return `Production(stringName, fieldList, VasType(codomainString));
      }
    }
   return null;
  }

  private void appendType(String typeString) {
    %match(VasTypeList typeList) {
      concVasType() -> {
        typeList = typeList.append(`VasType(typeString));
      }
      concVasType(_*,VasType(typename),_*) -> {
        if(`typename != typeString) {
          typeList = typeList.append(`VasType(typeString));
        }
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
  
}

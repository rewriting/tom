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
  }
  
  private final AdtFactory getAdtFactory() {
    return adtFactory;
  }

  private final VasFactory getVasFactory() {
    return vasFactory;
  }
  
  private void test() {
    parse("[constructor(Nat,zero,zero),constructor(Nat,suc,suc(<pred(Nat)>))]","Peano");
    parse("[modulentry(name(\"Peano\"),[],[type(\"Nat\")],[constructor(Nat,zero,zero),constructor(Nat,suc,suc(<pred(Nat)>))])]");
  }

  public void parse(String input) {
    if(!input.startsWith("[modulentry")) {
      // TODO EXCEPTION
    } else {
      modularAdtToVas(input);
    }
  }

  public void parse(String input, String inputName) {
    if(!input.startsWith("[modulentry")) {
      modularAdtToVas(toModular(input, inputName));
    } else {
      // TODO EXCEPTION
    }
  }

  public void modularAdtToVas(String input) {
    VasTypeList typeList = `concVasType();
    ProductionList prodList = `concProduction();
    ImportList importList = `concImportedVasModule();
    AdtModules modules = adtFactory.AdtModulesFromString(input);
    %match(AdtModules modules) {
      concAdtModule(_*,module,_*) -> {
        System.out.println(buildModule(module, typeList, prodList, importList));
      }
    }
  }

  private void appendType(String typeString, VasTypeList typeList) {
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

  private Production buildEntry(Entry entry, VasTypeList typeList) {
    %match(Entry entry) {
      constructor(sort,opname,syntax) -> { 
        String stringName = ((ATermAppl)opname).getName();
        String codomainString = ((ATermAppl)sort).getName();
        FieldList fieldList = buildFieldList(((ATermAppl)syntax).getArguments());
        appendType(codomainString, typeList);
        return `Production(stringName, fieldList, VasType(codomainString));
      }
      list(sort,elemsort) -> {
        String codomainString = ((ATermAppl)sort).getName();
        appendType(codomainString, typeList);
        String elemCodomainString = ((ATermAppl)elemsort).getName();
        return `Production("conc"+elemCodomainString,
                           concField(StaredField(VasType(elemCodomainString))),
                           VasType(codomainString));
      }
      // TODO NEXT
    }
   return null;
  }

  private void buildImports(Imports imports, ImportList importList) {
    %match(Imports imports) {
      concAdtModuleName(_*,name(importName),_*) -> {
        importList.append(`Import(VasModuleName(importName)));
      }
    }
    // TODO fermeture transitive
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

  private VasModule buildModule(AdtModule module, VasTypeList typeList, ProductionList prodList, ImportList importList) {
    String inputName = "";
    %match(AdtModule module) {
      modulentry[modulename=moduleName] -> {
        inputName = `moduleName.getName();
      }
      modulentry[importedModule=imports] -> {
        buildImports(imports, importList);
      }
      modulentry[definedSorts=sorts] -> {
        // Redondant ?
      }
      modulentry(_,_,_,concEntry(_*,entry,_*)) -> {
        Production prod =  buildEntry(`entry, typeList);
        prodList = prodList.append(prod);
      }
    }
    return `VasModule(VasModuleName(inputName),
                      concSection(Imports(importList),
                                  Public(concGrammar(Sorts(typeList),Grammar(prodList)))));
  };

  private String toModular(String input, String inputName) {
    return ("[modulentry(name(\""+inputName+"\"),[],[],"+input+")]");
  }

}

package adt;

import aterm.*;
import aterm.pure.*;
import adt.adt.*;
import adt.adt.types.*;
import adt.vas.*;
import adt.vas.types.*;

import java.util.*;
import java.io.*;

import tom.library.traversal.*;

public class AdtParser {
  private AdtFactory adtFactory;
  private VasFactory vasFactory;
  private GenericTraversal traversal;

  %include {Adt.tom}
  %include {Vas.tom}

  public final static void main(String[] args) {
    AdtParser parser = new AdtParser(AdtFactory.getInstance(SingletonFactory.getInstance()),
                                     VasFactory.getInstance(SingletonFactory.getInstance()),
                                     new GenericTraversal());
    parser.test();
  }
  
  public AdtParser(AdtFactory adtFactory, VasFactory vasFactory, GenericTraversal traversal) {
    this.adtFactory = adtFactory;
    this.vasFactory = vasFactory;
    this.traversal = traversal;
  }
  
  private final AdtFactory getAdtFactory() {
    return adtFactory;
  }

  private final VasFactory getVasFactory() {
    return vasFactory;
  }
  
  private void test() {
    String adt = "["+"\n"+
      "constructor(Date,date,date(<year(int)>,<month(int)>,<day(int)>)),"+"\n"+
      "constructor(Date,date2,date2(<year(int)>,<month(int)>,<day(int)>)),"+"\n"+
      "constructor(Person,person,person(<firstname(str)>,<lastname(str)>,<birthdate(Date)>)),"+"\n"+
      "named-list(\"concPerson\", PersonList, Person)"+"\n"+
      "]";
    System.out.println(adt);
    parse(adt,"AddresseBook");
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

  public void prettyPrint(VasModule module) {
    VasPrettyPrinter printer = new VasPrettyPrinter(VasFactory.getInstance(SingletonFactory.getInstance()));
    printer.print(module);
  }

  public void  modularAdtToVas(String input) {
    ProductionList prodList = `concProduction();
    ImportList importList = `concImportedVasModule();
    VasTypeList sortList = `concVasType();
    AdtModules modules = adtFactory.AdtModulesFromString(input);
    %match(AdtModules modules) {
      concAdtModule(_*,module,_*) -> {
        prettyPrint(`buildModule(module, prodList, importList, sortList));
      }
    }
  }

  private void appendType(String typeString, VasTypeList sortList) {
    %match(VasTypeList sortList) {
      concVasType() -> {
        sortList = sortList.append(`VasType(typeString));
        // il en manquait un
      }
      concVasType(_*,VasType(typename),_*) -> {
        if(`typename != typeString) {
          sortList = sortList.append(`VasType(typeString));
        }
      }
    }
  }

  private Production buildEntry(Entry entry, VasTypeList sortList) {
    %match(Entry entry) {
      constructor(sort,opname,syntax) -> { 
        String stringName = ((ATermAppl)`opname).getName();
        String codomainString = ((ATermAppl)`sort).getName();
        FieldList fieldList = buildFieldList(((ATermAppl)`syntax).getArguments());
        appendType(codomainString, sortList);
        return `Production(stringName, fieldList, VasType(codomainString));
      }
      list(sort,elemsort) -> {
        String codomainString = ((ATermAppl)`sort).getName();
        appendType(codomainString, sortList);
        String elemCodomainString = ((ATermAppl)`elemsort).getName();
        return `Production("conc"+elemCodomainString,
                           concField(StaredField(VasType(elemCodomainString))),
                           VasType(codomainString));
      }
      namedList(opname,sort,elemsort) -> {
        String codomainString = ((ATermAppl)`sort).getName();
        appendType(codomainString, sortList);
        String elemCodomainString = ((ATermAppl)`elemsort).getName();
        return `Production(opname,
                           concField(StaredField(VasType(elemCodomainString))),
                           VasType(codomainString));
      }
      _ -> {
        // TODO EXCEPTION
      }
    }
   return null;
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

  private VasModule buildModule(AdtModule module, ProductionList prodList, ImportList importList, VasTypeList sortList) {
    String inputName = "";
    %match(AdtModule module) {
      modulentry[modulename=moduleName] -> {
        inputName = `moduleName.getName();
      }
      modulentry[importedModule=imports] -> {
        importList = buildImports(`imports);
      }
      modulentry[definedSorts=sorts] -> {
        sortList = buildSorts(`sorts);
      }
      modulentry(_,_,_,concEntry(_*,entry,_*)) -> {
        Production prod =  buildEntry(`entry, sortList);
        prodList = prodList.append(prod);
      }
    }
    return `VasModule(VasModuleName(inputName),
                      concSection(Imports(importList),
                                  Public(concGrammar(Sorts(sortList),Grammar(prodList)))));
  };  

  private ImportList buildImports(ATermList imports) {
    if(imports.isEmpty()) {
      return `concImportedVasModule();
    } else {
      AdtModuleName name = (AdtModuleName)imports.getFirst();
      String nameName = name.getName();
      return buildImports(imports.getNext()).append(`Import(VasModuleName(nameName)));
    }
  }

  private VasTypeList buildSorts(ATermList sorts) {
    if(sorts.isEmpty()) {
      return `concVasType();
    } else {
      AdtType type = (AdtType)sorts.getFirst();
      String typeName = type.getName();
      return buildSorts(sorts.getNext()).append(`VasType(typeName));
    }
  }

  private String toModular(String input, String inputName) {
    Entries entries = adtFactory.EntriesFromString(input);
    HashMap map = new HashMap();
    collectSort(entries, map);
    //System.out.println("[modulentry(name(\""+inputName+"\"),[],["+hashSetToStr(set)+"],"+input+")]");
    return ("[modulentry(name(\""+inputName+"\"),[],["+hashMapToStr(map)+"],"+input+")]");
  }

  private void collectSort(ATerm subject, final HashMap set) {
    Collect1 collect = new Collect1() { 
        public boolean apply(ATerm t) {
          if(t instanceof Entry) {
            %match(Entry t) {
              list[sort=sort] -> { 
                set.put(`sort,"module");
                return false; 
              }
              constructor[sort=sort] -> { 
                set.put(`sort,"module");
                return false; 
              }
            }
          }
          return true;
        } // end apply
      }; // end new
    traversal.genericCollect(subject, collect);
  }

  private String hashSetToStr(HashMap map) {
    String result = "";
    Set set = map.keySet();
    Iterator i = set.iterator();
    if(i.hasNext()) {
      result = result+"type(\""+i.next()+"\")";
    }
    while(i.hasNext()) {
      result = result+",type(\""+i.next()+"\")";
    }
    return result;
  }

}

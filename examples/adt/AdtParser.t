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
    if (args.length < 1) {
      System.err.println("Usage: java adt.AdtParser <file_path>");
      System.exit(1);
    }
    AdtParser parser = new AdtParser(AdtFactory.getInstance(SingletonFactory.getInstance()),
                                     VasFactory.getInstance(SingletonFactory.getInstance()),
                                     new GenericTraversal());
    parser.run(args[0]);
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
  
  private void run(String path) {
    File file = new File(path);
    String adt = "";
    try {
      BufferedReader bufferReader = new BufferedReader(new FileReader(file));
      String line = "";
      while (line != null){
        adt += line;
        line = bufferReader.readLine();
      } //end while
      bufferReader.close();
      String name = file.getName();
      parse(adt,name.substring(0,name.lastIndexOf('.')),path.substring(0,path.length()-name.length()));
    } catch(IOException e) {
      System.out.println("io erreur : " + e);
    } catch(Exception ee) {
      System.out.println("erreur : " + ee);
    }
  }

  private void appendType(String typeString, VasTypeList sortList) {
    %match(VasTypeList sortList) {
      concVasType() -> {
        // Warning !
        sortList = sortList.append(`VasType(typeString));
      }
      concVasType(_*,VasType(typename),_*) -> {
        if(`typename != typeString) {
          sortList = sortList.append(`VasType(typeString));
        }
      }
    }
  }

  private Production buildEntry(Entry entry, VasTypeList sortList) throws Exception {
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
        throw new Exception("bad entry");
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

  private ImportList buildImports(ATermList imports) {
    if(imports.isEmpty()) {
      return `concImportedVasModule();
    } else {
      AdtModuleName name = (AdtModuleName)imports.getFirst();
      String nameName = name.getName();
      return buildImports(imports.getNext()).append(`Import(VasModuleName(nameName)));
    }
  }

  private VasModule buildModule(AdtModule module, ProductionList prodList,
                                ImportList importList, VasTypeList sortList) throws Exception {
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

  private VasTypeList buildSorts(ATermList sorts) {
    if(sorts.isEmpty()) {
      return `concVasType();
    } else {
      AdtType type = (AdtType)sorts.getFirst();
      String typeName = type.getName();
      return buildSorts(sorts.getNext()).append(`VasType(typeName));
    }
  }

  private void collectImport(ATerm subject, final HashMap map) {
    Collect1 collect = new Collect1() { 
        public boolean apply(ATerm t) {
          if(t instanceof AdtModules) {
            %match(AdtModules t) {
              concAdtModule(_*,module@modulentry[modulename=moduleName],_*) -> { 
                HashSet set = new HashSet();
                listSort(`module,set);
                map.put(`moduleName,set);
                return false; 
              }

            }
          }
          return true;
        } // end apply
      }; // end new
    traversal.genericCollect(subject, collect);
  }

  private void collectSort(ATerm subject, final HashMap map, final String modulename) {
    Collect1 collect = new Collect1() { 
        public boolean apply(ATerm t) {
          if(t instanceof Entry) {

            %match(Entry t) {
              list[sort=sort] -> { 
                map.put(`sort,modulename);
                return false; 
              }
              constructor[sort=sort] -> { 
                map.put(`sort,modulename);
                return false; 
              }
            }
          }
          return true;
        } // end apply
      }; // end new
    traversal.genericCollect(subject, collect);
  }  

  private void listSort(ATerm subject, final HashSet set) {
    Collect1 collect = new Collect1() { 
        public boolean apply(ATerm t) {
          if(t instanceof Entry) {
            %match(Entry t) {
              list[sort=sort] -> { 
                set.add(`sort);
                return false;
              }
              constructor[sort=sort] -> { 
                set.add(`sort);
                return false; 
              }
            }
          }
          return true;
        } // end apply
      }; // end new
    traversal.genericCollect(subject, collect);
  }

  private void  modularAdtToVas(String input, String path) throws Exception {
    /*************************************************************************************************************/
    ProductionList prodList = `concProduction();
    ImportList importList = `concImportedVasModule();
    VasTypeList sortList = `concVasType();
    AdtModules modules = adtFactory.AdtModulesFromString(input);

    HashMap sortMap = new HashMap();
    HashMap importMap = new HashMap();
    collectImport(modules, importMap);

    %match(AdtModules modules) {
      concAdtModule(_*,module@modulentry[modulename=moduleName],_*) -> {
        collectSort(`module, sortMap, `moduleName.getName());
        prettyPrint(`buildModule(module, prodList, importList, sortList), path);
      }
    }
  }

  private void parse(String input, String inputName, String path) throws Exception {
    if(!input.startsWith("[modulentry")) {
      modularAdtToVas(toModular(input, inputName), path);
    } else {
      modularAdtToVas(input, path);
    }
  }

  private void prettyPrint(VasModule module, String path) {
    VasPrettyPrinter printer = new VasPrettyPrinter(vasFactory);
    printer.print(module, path);
  }

  private String setToStr(HashSet set) {
    String result = "";
    Iterator i = set.iterator();
    if(i.hasNext()) {
      result = result+"type(\""+i.next()+"\")";
    }
    while(i.hasNext()) {
      result = result+",type(\""+i.next()+"\")";
    }
    return result;
  }

  private String toModular(String input, String inputName) {
    Entries entries = adtFactory.EntriesFromString(input);
    HashSet set = new HashSet();
    listSort(entries, set);
    return ("[modulentry(name(\""+inputName+"\"),[],["+setToStr(set)+"],"+input+")]");
  }

}

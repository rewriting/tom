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
  private HashMap moduleToDefinedSorts;
  private HashMap moduleToImportedModules;
  //private HashMap sortToModules;
  private ArrayList builtinSortsList;
  
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
    moduleToDefinedSorts = new HashMap();
    moduleToImportedModules = new HashMap();
    //sortToModules = new HashMap();
    builtinSortsList = new ArrayList();
    builtinSortsList.add("str");
    builtinSortsList.add("term");
    builtinSortsList.add("int");
    builtinSortsList.add("real");
    builtinSortsList.add("char");
    builtinSortsList.add("list");
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
      System.err.println("IO erreur : " + e);
    } catch(Exception ee) {
      System.err.println("Erreur : " + ee);
    }
  }

  private void appendType(String typeString, VasTypeList sortList) {
    %match(VasTypeList sortList) {
      concVasType(_*,VasType(typename),_*) -> {
        if(`typename == typeString) {
          return;
        }
      }
    }
    // Warning !
    System.err.println("Warning : Missing sort "+typeString+" in definition");
    sortList = sortList.append(`VasType(typeString));
  }

  private Production buildEntry(Entry entry, VasTypeList sortList, String moduleName) throws Exception {
    %match(Entry entry) {
      constructor(sort,opname,syntax) -> { 
        String stringName = ((ATermAppl)`opname).getName();
        String codomainString = ((ATermAppl)`sort).getName();
        FieldList fieldList = buildFieldList(((ATermAppl)`syntax).getArguments(),moduleName);
        appendType(codomainString, sortList);
        return `Production(stringName, fieldList, VasType(typeToVasConverter(codomainString)));
      }
      list(sort,elemsort) -> {
        String codomainString = ((ATermAppl)`sort).getName();
        appendType(codomainString, sortList);
        String elemCodomainString = ((ATermAppl)`elemsort).getName();
        if(!sortIsDefined(elemCodomainString,moduleName)) {
          throw new Exception("Unknown argument sort "+elemCodomainString);
        }
        return `Production("conc"+elemCodomainString,
                           concField(StaredField(VasType(typeToVasConverter(elemCodomainString)))),
                           VasType(codomainString));
      }
      namedList(opname,sort,elemsort) -> {
        String codomainString = ((ATermAppl)`sort).getName();
        appendType(codomainString, sortList);
        String elemCodomainString = ((ATermAppl)`elemsort).getName();
        if(!sortIsDefined(elemCodomainString,moduleName)) {
          throw new Exception("Unknown argument sort "+elemCodomainString);
        }
        return `Production(opname,
                           concField(StaredField(VasType(elemCodomainString))),
                           VasType(typeToVasConverter(codomainString)));
      }
      _ -> {
        throw new Exception("Bad Entry");
      }
    }
  }

  private FieldList buildFieldList(ATermList subtermList, String moduleName) throws Exception {
    if(subtermList.isEmpty()) {
      return `concField();
    } else {
      ATermPlaceholder arg = (ATermPlaceholder) subtermList.getFirst();
      String argname = ((ATermAppl)arg.getPlaceholder()).getName();
      String argtype = ((ATermAppl)((ATermAppl)arg.getPlaceholder()).getArgument(0)).getName();
      if(!sortIsDefined(argtype,moduleName)) {
        throw new Exception("Unknown argument sort "+argtype);
      }
      return `manyFieldList(NamedField(argname,VasType(typeToVasConverter(argtype))),
                            buildFieldList(subtermList.getNext(),moduleName));
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

  private ImportList buildImports(ATermList imports) throws Exception {
    if(imports.isEmpty()) {
      return `concImportedVasModule();
    } else {
      AdtModuleName name = (AdtModuleName)imports.getFirst();
      String nameName = name.getName();
      if(!moduleToDefinedSorts.containsKey(nameName)) {
        throw new Exception("Unknown module "+nameName);
      }
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
      modulentry[modulename=moduleName,definedSorts=sorts] -> {
        sortList = buildSorts(`sorts,`moduleName.getName());
      }
      modulentry(moduleName,_,_,concEntry(_*,entry,_*)) -> {
        Production prod =  buildEntry(`entry, sortList,`moduleName.getName());
        prodList = prodList.append(prod);
      }
    }
    return `VasModule(VasModuleName(inputName),
                      concSection(Imports(importList),
                                  Public(concGrammar(Sorts(sortList),Grammar(prodList)))));
  };  

  private VasTypeList buildSorts(ATermList sorts, String moduleName) throws Exception {
    if(sorts.isEmpty()) {
      return `concVasType();
    } else {
      AdtType type = (AdtType)sorts.getFirst();
      String typeName = type.getName();
      ArrayList moduleSorts = (ArrayList)moduleToDefinedSorts.get(moduleName);
      if((moduleSorts.contains(typeName))||(builtinSortsList.contains(typeName))) {
        return buildSorts(sorts.getNext(),moduleName).append(`VasType(typeToVasConverter(typeName)));
      } else {
        throw new Exception("Unknown type "+typeName);
      }
    }
  }

  private void collectImport(ATerm subject, final HashMap map, final String modulename) {
    Collect1 collect = new Collect1() { 
        public boolean apply(ATerm t) {
          if(t instanceof Imports) {
            %match(Imports t) {
              concAdtModuleName(_*,name[name=moduleName],_*) -> { 
                if(map.containsKey(modulename)) {
                  ((ArrayList)map.get(modulename)).add(`moduleName.toString());
                } else {
                  ArrayList list = new ArrayList();
                  list.add(`moduleName.toString());
                  map.put(modulename,list);
                }
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
              namedList[sort=sort] -> { 
                if(map.containsKey(modulename)) {
                  ((ArrayList)map.get(modulename)).add(`sort.toString());
                } else {
                  ArrayList list = new ArrayList();
                  list.add(`sort.toString());
                  map.put(modulename,list);
                }
                return false;
              }
              list[sort=sort] -> { 
                if(map.containsKey(modulename)) {
                  ((ArrayList)map.get(modulename)).add(`sort.toString());
                } else {
                  ArrayList list = new ArrayList();
                  list.add(`sort.toString());
                  map.put(modulename,list);
                }
                return false;
              }
              constructor[sort=sort] -> {
                if(map.containsKey(modulename)) {
                  ((ArrayList)map.get(modulename)).add(`sort.toString());
                } else {
                  ArrayList list = new ArrayList();
                  list.add(`sort.toString());
                  map.put(modulename,list);
                }
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
              namedList[sort=sort] -> { 
                set.add(`sort);
                return false;
              }
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
    System.out.println(input);
    ProductionList prodList = `concProduction();
    ImportList importList = `concImportedVasModule();
    VasTypeList sortList = `concVasType();
    AdtModules modules = adtFactory.AdtModulesFromString(input);
    %match(AdtModules modules) {
      concAdtModule(_*,module@modulentry[modulename=moduleName],_*) -> {
        collectImport(modules, moduleToImportedModules, `moduleName.getName());
        collectSort(`module, moduleToDefinedSorts, `moduleName.getName());
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

  private boolean sortIsDefined(String sort, String moduleName) {
    if((((ArrayList)moduleToDefinedSorts.get(moduleName)).contains(sort))||(builtinSortsList.contains(sort))) {
      return true;
    } else {
      Iterator i = ((ArrayList)moduleToImportedModules.get(moduleName)).iterator();
      while(i.hasNext()) {
        if(((ArrayList)i.next()).contains(sort)) {
          return true;
        }
      }
      return false;
    }
  }

  private String toModular(String input, String inputName) {
    Entries entries = adtFactory.EntriesFromString(input);
    HashSet set = new HashSet();
    listSort(entries, set);
    return ("[modulentry(name(\""+inputName+"\"),[],["+setToStr(set)+"],"+input+")]");
  }

  private String typeToVasConverter(String type) {
    %match(String type) {
      "str"  -> { return "String"; }
      "term" -> { return "ATerm"; }
      "int"  -> { return "Int"; }
      "real" -> { return "Real"; }
      "char" -> { return "Char"; }
      _      -> { return type; }
    }
  }
}

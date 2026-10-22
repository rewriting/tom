























package tom.gom.tools;



import tom.gom.GomStreamManager;
import tom.gom.adt.gom.types.*;
import tom.gom.adt.objects.types.*;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.SymbolTable;
import java.util.*;



public class GomEnvironment {

  

  private GomStreamManager streamManager;
  private String lastGeneratedMapping;
  private SymbolTable symbolTable;
  
  private Map<ModuleDecl,ModuleDeclList> importedModules;
  private Map<String,ClassName> usedBuiltinSorts;

  private static final Map<String,ClassName> builtinSorts;
  static {
    Map<String,ClassName> localBuiltinSorts = new HashMap<String,ClassName>();
    localBuiltinSorts.put("boolean", tom.gom.adt.objects.types.classname.ClassName.make("", "boolean") );
    localBuiltinSorts.put("int", tom.gom.adt.objects.types.classname.ClassName.make("", "int") );
    localBuiltinSorts.put("String", tom.gom.adt.objects.types.classname.ClassName.make("", "String") );
    localBuiltinSorts.put("char", tom.gom.adt.objects.types.classname.ClassName.make("", "char") );
    localBuiltinSorts.put("double", tom.gom.adt.objects.types.classname.ClassName.make("", "double") );
    localBuiltinSorts.put("long", tom.gom.adt.objects.types.classname.ClassName.make("", "long") );
    localBuiltinSorts.put("float", tom.gom.adt.objects.types.classname.ClassName.make("", "float") );
    localBuiltinSorts.put("ATerm", tom.gom.adt.objects.types.classname.ClassName.make("aterm", "ATerm") );
    localBuiltinSorts.put("ATermList", tom.gom.adt.objects.types.classname.ClassName.make("aterm", "ATermList") );
    builtinSorts = Collections.unmodifiableMap(localBuiltinSorts);
  }

  
  public GomEnvironment() {
    streamManager = new GomStreamManager();
    importedModules = new HashMap<ModuleDecl,ModuleDeclList>();
    usedBuiltinSorts = new HashMap<String,ClassName>();
    symbolTable = new SymbolTable(this);
  }

  public SymbolTable getSymbolTable() {
    return symbolTable;
  }

  public void initSymbolTable(GomModuleList l) {
    symbolTable.clear();
    symbolTable.fill(l);
  }

  public ModuleDeclList getModuleDependency(ModuleDecl module) {
    return importedModules.get(module);
  }
  public void addModuleDependency(ModuleDecl module, ModuleDeclList imported) {
    importedModules.put(module,imported);
  }
  public void setStreamManager(GomStreamManager stream) {
    this.streamManager = stream;
  }
  public GomStreamManager getStreamManager() {
    return streamManager;
  }

  
  public void markUsedBuiltin(String moduleName) {
    if (builtinSorts.containsKey(moduleName)) {
      usedBuiltinSorts.put(moduleName,builtinSorts.get(moduleName));
    } else {
      throw new GomRuntimeException("Not a builtin module: "+moduleName);
    }
  }
  public boolean isBuiltin(String moduleName) {
    return builtinSorts.containsKey(moduleName);
  }
  public boolean isBuiltinSort(String sortName) {
    return usedBuiltinSorts.containsKey(sortName);
  }
  public boolean isBuiltinClass(ClassName className) {
    return usedBuiltinSorts.containsValue(className);
  }
  public SortDecl builtinSort(String sortname) {
    if (isBuiltin(sortname)) {
      return  tom.gom.adt.gom.types.sortdecl.BuiltinSortDecl.make(sortname) ;
    } else {
      throw new GomRuntimeException("Not a builtin sort: "+sortname);
    }
  }

  public Map<SortDecl,ClassName> builtinSortClassMap() {
    Map<SortDecl,ClassName> sortClass = new HashMap<SortDecl,ClassName>();
    for (String name : usedBuiltinSorts.keySet()) {
      sortClass.put( tom.gom.adt.gom.types.sortdecl.BuiltinSortDecl.make(name) ,usedBuiltinSorts.get(name));
    }
    return sortClass;
  }

  
  public String getLastGeneratedMapping() {
    return lastGeneratedMapping;
  }

  public void setLastGeneratedMapping(String fileName) {
    lastGeneratedMapping = fileName;
  }
}

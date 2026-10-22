























package tom.gom.backend.strategy;



import java.io.*;
import java.util.logging.*;
import tom.gom.backend.TemplateClass;
import tom.gom.tools.GomEnvironment;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.adt.objects.types.*;



public class SOpTemplate extends TemplateClass {
  ClassName operator;
  SlotFieldList slotList;

  

  
  public SOpTemplate(GomClass gomClass, GomEnvironment gomEnvironment) {
    super(gomClass,gomEnvironment);
    ClassName clsName = this.className;
    { /* unamed block */{ /* unamed block */if ( (clsName instanceof tom.gom.adt.objects.types.ClassName) ) {if ( ((( tom.gom.adt.objects.types.ClassName )clsName) instanceof tom.gom.adt.objects.types.classname.ClassName) ) {

        String newpkg =  (( tom.gom.adt.objects.types.ClassName )clsName).getPkg() .replaceFirst(".types.",".strategy.");
        String newname = "_"+ (( tom.gom.adt.objects.types.ClassName )clsName).getName() ;
        this.className =  tom.gom.adt.objects.types.classname.ClassName.make(newpkg, newname) ;
      }}}}{ /* unamed block */{ /* unamed block */if ( (gomClass instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )gomClass) instanceof tom.gom.adt.objects.types.gomclass.OperatorClass) ) {



        this.operator =  (( tom.gom.adt.objects.types.GomClass )gomClass).getClassName() ;
        this.slotList =  (( tom.gom.adt.objects.types.GomClass )gomClass).getSlotFields() ;
        return;
      }}}}

    throw new GomRuntimeException(
        "Wrong argument for SOpTemplate: " + gomClass);
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {
writer.write("\npackage "+getPackage()+";\n\npublic class "+className()+" implements tom.library.sl.Strategy {\n  private static final String msg = \"Not an "+className(operator)+"\";\n  /* Manage an internal environment */\n  protected tom.library.sl.Environment environment;\n\n  public void setEnvironment(tom.library.sl.Environment env) {\n    this.environment = env;\n  }\n\n  public tom.library.sl.Environment getEnvironment() {\n    if(environment!=null) {\n      return environment;\n    } else {\n      throw new RuntimeException(\"environment not initialized\");\n    }\n  }\n\n  private tom.library.sl.Strategy[] args;\n\n  public int getChildCount() {\n    return args.length;\n  }\n  public tom.library.sl.Visitable getChildAt(int i) {\n      return args[i];\n  }\n  public tom.library.sl.Visitable setChildAt(int i, tom.library.sl.Visitable child) {\n    args[i]= (tom.library.sl.Strategy) child;\n    return this;\n  }\n\n  public tom.library.sl.Visitable[] getChildren() {\n    return args.clone();\n  }\n\n  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {\n    tom.library.sl.Strategy[] newArgs = new tom.library.sl.Strategy[children.length];\n    for(int i = 0; i < children.length; i++) {\n      newArgs[i] = (tom.library.sl.Strategy) children[i];\n    }\n    args = newArgs;\n    return this;\n  }\n\n  @SuppressWarnings(\"unchecked\")\n  public <T extends tom.library.sl.Visitable> T visit(tom.library.sl.Environment envt) throws tom.library.sl.VisitFailure {\n    return (T) visit(envt,tom.library.sl.VisitableIntrospector.getInstance());\n  }\n\n  public <T extends tom.library.sl.Visitable> T visit(T any) throws tom.library.sl.VisitFailure {\n    return visit(any,tom.library.sl.VisitableIntrospector.getInstance());\n  }\n\n  public <T extends tom.library.sl.Visitable> T visitLight(T any) throws tom.library.sl.VisitFailure {\n    return visitLight(any,tom.library.sl.VisitableIntrospector.getInstance());\n  }\n\n  @SuppressWarnings(\"unchecked\")\n  public <T> T visit(T any, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {\n    tom.library.sl.AbstractStrategy.init(this,new tom.library.sl.Environment());\n    environment.setRoot(any);\n    int status = visit(i);\n    if(status == tom.library.sl.Environment.SUCCESS) {\n      return (T) environment.getRoot();\n    } else {\n      throw new tom.library.sl.VisitFailure();\n    }\n  }\n\n  public "+className()+"("+genConstrArgs(slotList.length(),"tom.library.sl.Strategy arg",false)+") {\n    args = new tom.library.sl.Strategy[] {"+genConstrArgs(slotList.length(),"arg",false)+"};\n  }\n\n  public Object visit(tom.library.sl.Environment envt, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {\n    tom.library.sl.AbstractStrategy.init(this,envt);\n    int status = visit(i);\n    if(status == tom.library.sl.Environment.SUCCESS) {\n      return environment.getSubject();\n    } else {\n      throw new tom.library.sl.VisitFailure();\n    }\n  }\n\n  public <T> T visitLight(T any, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {\n    if(any instanceof "+fullClassName(operator)+") {\n      T result = any;\n      Object[] children = null;\n      for (int i = 0, nbi = 0; i < "+slotList.length()+"; i++) {\n          Object oldChild = introspector.getChildAt(any,nbi);\n          Object newChild = args[i].visitLight(oldChild,introspector);\n          if(children != null) {\n            children[nbi] = newChild;\n          } else if(newChild != oldChild) {\n            // allocate the array, and fill it\n            children = introspector.getChildren(any);\n            children[nbi] = newChild;\n          }\n          nbi++;\n      }\n      if(children!=null) {\n        result = introspector.setChildren(any,children);\n      }\n      return result;\n    } else {\n      throw new tom.library.sl.VisitFailure(msg);\n    }\n  }\n\n  public int visit(tom.library.sl.Introspector introspector) {\n    Object any = environment.getSubject();\n    if(any instanceof "+fullClassName(operator)+") {\n      Object[] children = null;\n      for(int i = 0, nbi = 0; i < "+slotList.length()+"; i++) {\n          Object oldChild = introspector.getChildAt(any,nbi);\n          environment.down(nbi+1);\n          int status = args[i].visit(introspector);\n          if(status != tom.library.sl.Environment.SUCCESS) {\n            environment.upLocal();\n            return status;\n          }\n          Object newChild = environment.getSubject();\n          if(children != null) {\n            children[nbi] = newChild;\n          } else if(newChild != oldChild) {\n            children = introspector.getChildren(any);\n            children[nbi] = newChild;\n          }\n          environment.upLocal();\n          nbi++;\n      }\n      if(children!=null) {\n        environment.setSubject(introspector.setChildren(any,children));\n      }\n      return tom.library.sl.Environment.SUCCESS;\n    } else {\n      return tom.library.sl.Environment.FAILURE;\n    }\n  }\n}\n"












































































































































);
}



private String genConstrArgs(int count, String arg, boolean withDollar) {
  StringBuilder args = new StringBuilder();
  for(int i = 0; i < count; ++i) {
    args.append((i==0?"":", "));
    if(withDollar) {
      args.append("$");
    }
    args.append(arg);
    args.append(i);
  }
  return args.toString();
}



private String genIdArgs(int count) {
  StringBuilder args = new StringBuilder();
  for(int i = 0; i < count; ++i) {
    args.append((i==0?"":", "));
    args.append("Identity()");
  }
  return args.toString();
}



public String generateMapping() {

  return "\n  %op Strategy "+className()+"("+genStratArgs(slotList.length(),"arg")+") {\n    is_fsym(t) { (($t!=null) && ($t instanceof "+fullClassName()+"))}\n    "+genGetSlot(slotList.length(),"arg")+"\n    make("+genConstrArgs(slotList.length(),"arg",false)+") { new "+fullClassName()+"("+genConstrArgs(slotList.length(),"arg",true)+") }\n  }\n  "





;
}



private String genGetSlot(int count, String arg) {
  StringBuilder out = new StringBuilder();
  for (int i = 0; i < count; ++i) {
    out.append("\n        get_slot("+arg+i+", t) { (tom.library.sl.Strategy)(("+fullClassName()+")$t).getChildAt("+i+") }"
);
  }
  return out.toString();
}



private String genStratArgs(int count, String arg) {
  StringBuilder args = new StringBuilder();
  for(int i = 0; i < count; ++i) {
    args.append((i==0?"":", "));
    args.append(arg);
    args.append(i);
    args.append(":Strategy");
  }
  return args.toString();
}




private Logger getLogger() {
  return Logger.getLogger(getClass().getName());
}
}

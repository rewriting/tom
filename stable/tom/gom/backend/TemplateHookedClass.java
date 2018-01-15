























package tom.gom.backend;



import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import tom.gom.Gom;
import tom.gom.GomMessage;
import tom.gom.backend.CodeGen;
import tom.gom.adt.objects.*;
import tom.gom.adt.objects.types.*;
import tom.platform.OptionManager;
import tom.gom.tools.GomEnvironment;



public abstract class TemplateHookedClass extends TemplateClass {
  protected HookList hooks;
  protected File tomHomePath;
  protected List importList;
  protected TemplateClass mapping;
  protected OptionManager optionManager;

  public TemplateHookedClass(GomClass gomClass,
                             OptionManager manager,
                             File tomHomePath,
                             List importList,
                             TemplateClass mapping,
                             GomEnvironment gomEnvironment) {
    super(gomClass,gomEnvironment);
    this.optionManager = manager;
    this.hooks = gomClass.getHooks();
    this.tomHomePath = tomHomePath;
    this.importList = importList;
    this.mapping = mapping;
  }

     private static   tom.gom.adt.objects.types.HookList  tom_append_list_ConcHook( tom.gom.adt.objects.types.HookList l1,  tom.gom.adt.objects.types.HookList  l2) {     if( l1.isEmptyConcHook() ) {       return l2;     } else if( l2.isEmptyConcHook() ) {       return l1;     } else if(  l1.getTailConcHook() .isEmptyConcHook() ) {       return  tom.gom.adt.objects.types.hooklist.ConsConcHook.make( l1.getHeadConcHook() ,l2) ;     } else {       return  tom.gom.adt.objects.types.hooklist.ConsConcHook.make( l1.getHeadConcHook() ,tom_append_list_ConcHook( l1.getTailConcHook() ,l2)) ;     }   }   private static   tom.gom.adt.objects.types.HookList  tom_get_slice_ConcHook( tom.gom.adt.objects.types.HookList  begin,  tom.gom.adt.objects.types.HookList  end, tom.gom.adt.objects.types.HookList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcHook()  ||  (end== tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.objects.types.hooklist.ConsConcHook.make( begin.getHeadConcHook() ,( tom.gom.adt.objects.types.HookList )tom_get_slice_ConcHook( begin.getTailConcHook() ,end,tail)) ;   }   


  protected String generateBlock() {
    StringBuilder res = new StringBuilder();
    HookList h = tom_append_list_ConcHook(hooks, tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() );
    { /* unamed block */{ /* unamed block */if ( (h instanceof tom.gom.adt.objects.types.HookList) ) {if ( (((( tom.gom.adt.objects.types.HookList )h) instanceof tom.gom.adt.objects.types.hooklist.ConsConcHook) || ((( tom.gom.adt.objects.types.HookList )h) instanceof tom.gom.adt.objects.types.hooklist.EmptyConcHook)) ) { tom.gom.adt.objects.types.HookList  tomMatch627_end_4=(( tom.gom.adt.objects.types.HookList )h);do {{ /* unamed block */if (!( tomMatch627_end_4.isEmptyConcHook() )) { tom.gom.adt.objects.types.Hook  tomMatch627_8= tomMatch627_end_4.getHeadConcHook() ;if ( ((( tom.gom.adt.objects.types.Hook )tomMatch627_8) instanceof tom.gom.adt.objects.types.hook.BlockHook) ) {

        res.append(CodeGen.generateCode( tomMatch627_8.getCode() ));
        res.append("\n");
      }}if ( tomMatch627_end_4.isEmptyConcHook() ) {tomMatch627_end_4=(( tom.gom.adt.objects.types.HookList )h);} else {tomMatch627_end_4= tomMatch627_end_4.getTailConcHook() ;}}} while(!( (tomMatch627_end_4==(( tom.gom.adt.objects.types.HookList )h)) ));}}}}

    return res.toString();
  }

  protected String generateImport() {
    StringBuilder res = new StringBuilder();
    HookList h = tom_append_list_ConcHook(hooks, tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() );
    { /* unamed block */{ /* unamed block */if ( (h instanceof tom.gom.adt.objects.types.HookList) ) {if ( (((( tom.gom.adt.objects.types.HookList )h) instanceof tom.gom.adt.objects.types.hooklist.ConsConcHook) || ((( tom.gom.adt.objects.types.HookList )h) instanceof tom.gom.adt.objects.types.hooklist.EmptyConcHook)) ) { tom.gom.adt.objects.types.HookList  tomMatch628_end_4=(( tom.gom.adt.objects.types.HookList )h);do {{ /* unamed block */if (!( tomMatch628_end_4.isEmptyConcHook() )) { tom.gom.adt.objects.types.Hook  tomMatch628_8= tomMatch628_end_4.getHeadConcHook() ;if ( ((( tom.gom.adt.objects.types.Hook )tomMatch628_8) instanceof tom.gom.adt.objects.types.hook.ImportHook) ) {

        res.append(CodeGen.generateCode( tomMatch628_8.getCode() ));
        res.append("\n");
      }}if ( tomMatch628_end_4.isEmptyConcHook() ) {tomMatch628_end_4=(( tom.gom.adt.objects.types.HookList )h);} else {tomMatch628_end_4= tomMatch628_end_4.getTailConcHook() ;}}} while(!( (tomMatch628_end_4==(( tom.gom.adt.objects.types.HookList )h)) ));}}}}

    return res.toString();
  }

  protected String generateInterface() {
    StringBuilder res = new StringBuilder();
    HookList h = tom_append_list_ConcHook(hooks, tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() );
    { /* unamed block */{ /* unamed block */if ( (h instanceof tom.gom.adt.objects.types.HookList) ) {if ( (((( tom.gom.adt.objects.types.HookList )h) instanceof tom.gom.adt.objects.types.hooklist.ConsConcHook) || ((( tom.gom.adt.objects.types.HookList )h) instanceof tom.gom.adt.objects.types.hooklist.EmptyConcHook)) ) { tom.gom.adt.objects.types.HookList  tomMatch629_end_4=(( tom.gom.adt.objects.types.HookList )h);do {{ /* unamed block */if (!( tomMatch629_end_4.isEmptyConcHook() )) { tom.gom.adt.objects.types.Hook  tomMatch629_8= tomMatch629_end_4.getHeadConcHook() ;if ( ((( tom.gom.adt.objects.types.Hook )tomMatch629_8) instanceof tom.gom.adt.objects.types.hook.InterfaceHook) ) {

        res.append(",");
        res.append(CodeGen.generateCode( tomMatch629_8.getCode() ));
        res.append("\n");
      }}if ( tomMatch629_end_4.isEmptyConcHook() ) {tomMatch629_end_4=(( tom.gom.adt.objects.types.HookList )h);} else {tomMatch629_end_4= tomMatch629_end_4.getTailConcHook() ;}}} while(!( (tomMatch629_end_4==(( tom.gom.adt.objects.types.HookList )h)) ));}}}}

    return res.toString();
  }

  
  public int generateFile() {
    if (hooks.containsTomCode()) {
      
      
      File configFile = new File(tomHomePath,"Tom.config");
      if(!configFile.exists()) {
        GomMessage.finer(getLogger(),null,0,
            GomMessage.getCanonicalPathFailure, configFile.getPath());
      }
      String file_path = null;
      try {
        File output = fileToGenerate();
        file_path = output.getCanonicalPath();
      } catch (IOException e) {
        GomMessage.finer(getLogger(),null,0,
            GomMessage.getCanonicalPathFailure, fileName());
      }

      ArrayList<String> tomParams = new ArrayList<String>();

      try {
        Iterator it = importList.iterator();
        while(it.hasNext()){
          String importPath = ((File)it.next()).getCanonicalPath();
          tomParams.add("--import");
          tomParams.add(importPath);
        }
      } catch (IOException e) {
        GomMessage.error(getLogger(),null,0,
            GomMessage.importListComputationFailure, e.getMessage());
      }

      tomParams.add("-X");
      tomParams.add(configFile.getPath());
      if(Boolean.TRUE == optionManager.getOptionValue("newtyper")) {
        tomParams.add("--newtyper");
      }
      if(Boolean.TRUE == optionManager.getOptionValue("newparser")) {
        tomParams.add("--newparser");
      }
      if(Boolean.TRUE == optionManager.getOptionValue("tomjava")) {
        tomParams.add("--tomjava");
      }
      if(Boolean.TRUE == optionManager.getOptionValue("optimize")) {
        tomParams.add("--optimize");
      }
      if(Boolean.TRUE == optionManager.getOptionValue("optimize2")) {
        tomParams.add("--optimize2");
      }
      if(Boolean.TRUE == optionManager.getOptionValue("inlineplus")) {
        tomParams.add("--inlineplus");
      }
      if(optionManager.getOptionValue("wall")==Boolean.TRUE) {
        tomParams.add("--wall");
      }
      if(optionManager.getOptionValue("intermediate")==Boolean.TRUE) {
        tomParams.add("--intermediate");
      }
      if(optionManager.getOptionValue("verbose")==Boolean.TRUE) {
        tomParams.add("--verbose");
      }
      tomParams.add("--output");
      tomParams.add(file_path);
    
      final File tmpFile;
      try {
        tmpFile = File.createTempFile("tmp", ".t", null).getCanonicalFile();
      } catch (IOException e) {
        System.out.println("IO Exception when computing importList");
        e.printStackTrace();
        return 1;
      }
      tomParams.add(tmpFile.getPath());
      
      

      try {
        StringWriter gen = new StringWriter();
        generate(gen);

        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmpFile)));
        String s = new String(gen.toString().getBytes("UTF-8"));
        
        writer.write(s); 
        writer.flush();
        writer.close();

        int res = tom.engine.Tom.exec(tomParams.toArray(new String[0]));
        tmpFile.deleteOnExit();

        
        if (res != 0 ) {
          GomMessage.error(getLogger(),null,0,
              tom.gom.GomMessage.tomFailure, file_path);
          return res;
        }
      } catch (IOException e) {
        GomMessage.error(getLogger(),null,0,
            GomMessage.tomCodeGenerationFailure, e.getMessage());
      }

    } else {
      return super.generateFile();
    }
    return 0;
  }

  
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }



}

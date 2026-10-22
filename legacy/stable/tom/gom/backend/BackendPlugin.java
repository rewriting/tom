























package tom.gom.backend;



import java.util.Map;
import java.io.File;
import java.io.IOException;



import tom.gom.GomMessage;
import tom.gom.tools.GomGenericPlugin;
import tom.gom.tools.GomEnvironment;
import tom.platform.adt.platformoption.types.PlatformOptionList;



import tom.gom.adt.objects.types.*;






public class BackendPlugin extends GomGenericPlugin {
     private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_append_list_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList l1,  tom.platform.adt.platformoption.types.PlatformOptionList  l2) {     if( l1.isEmptyconcPlatformOption() ) {       return l2;     } else if( l2.isEmptyconcPlatformOption() ) {       return l1;     } else if(  l1.getTailconcPlatformOption() .isEmptyconcPlatformOption() ) {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,l2) ;     } else {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,tom_append_list_concPlatformOption( l1.getTailconcPlatformOption() ,l2)) ;     }   }   private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_get_slice_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList  begin,  tom.platform.adt.platformoption.types.PlatformOptionList  end, tom.platform.adt.platformoption.types.PlatformOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcPlatformOption()  ||  (end== tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( begin.getHeadconcPlatformOption() ,( tom.platform.adt.platformoption.types.PlatformOptionList )tom_get_slice_concPlatformOption( begin.getTailconcPlatformOption() ,end,tail)) ;   }   

  
  private GomClassList classList;
  private TemplateFactory templateFactory;

  
  public BackendPlugin() {
    super("GomBackend");
    templateFactory = new SharedTemplateFactory(getOptionManager(),getGomEnvironment());
  }

  
  public static final PlatformOptionList PLATFORM_OPTIONS =
     tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("generator", "g", "Select Generator. Possible value: \"shared\"",  tom.platform.adt.platformoption.types.platformvalue.StringValue.make("shared") , "type") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("newtyper", "nt", "New TyperPlugin (activated by default since Tom-2.10)",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.True.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("newparser", "np", "New parser plugin (not activated by default)",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("tomjava", "tj", "Parser tailored for Tom+Java (activated by default)",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.True.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("optimize", "O", "Optimize generated code: perform inlining",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.True.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("optimize2", "O2", "Optimize generated code: discrimination tree",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("inlineplus", "", "Make inlining active",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("withCongruenceStrategies", "wcs", "Include the definition of congruence strategies in the generate file.tom file",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("withSeparateCongruenceStrategies", "wscs", "Generate the definition of congruence strategies in _file.tom file",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("multithread", "mt", "Generate code compatible with multi-threading",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("nosharing", "ns", "Generate code without maximal sharing",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("jmicompatible", "jmi", "Generate code whose syntax is compatible with JMI standards (capitalize getters and setters)",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) ) ) ) ) ) ) ) ) ) ) ) 












;

  
  public PlatformOptionList getDeclaredOptionList() {
    return PLATFORM_OPTIONS;
  }

  
  public void setArgs(Object arg[]) {
    if (arg[0] instanceof GomClassList) {
      classList = (GomClassList)arg[0];
      setGomEnvironment((GomEnvironment)arg[1]);
    } else {
      GomMessage.error(getLogger(),null,0,
          GomMessage.invalidPluginArgument,
          "GomBackend", "[GomClassList,GomEnvironment]",
          getArgumentArrayString(arg));
    }
  }

  
  public void run(Map<String,String> informationTracker) {
    long startChrono = System.currentTimeMillis();
    
    getGomEnvironment().setStreamManager(getStreamManager());
    
    File tomHomePath = null;
    String tomHome = System.getProperty("tom.home");
    try {
      if (null == tomHome) {
        String configFilename = getOptionStringValue("X");
        tomHome = new File(configFilename).getParent();
      }
      tomHomePath = new File(tomHome).getCanonicalFile();
    } catch (IOException e) {
      GomMessage.finer(getLogger(),null,0, GomMessage.getCanonicalPathFailure,
          tomHome);
    }
    int generateStratMapping = 0;
    if (getOptionBooleanValue("withCongruenceStrategies")) {
      generateStratMapping = 1;
    }
    if (getOptionBooleanValue("withSeparateCongruenceStrategies")) {
      generateStratMapping = 2;
    }
    boolean multithread = getOptionBooleanValue("multithread");
    boolean nosharing = getOptionBooleanValue("nosharing");
    boolean jmicompatible = getOptionBooleanValue("jmicompatible");
    Backend backend =
      new Backend(templateFactory.getFactory(getOptionManager()),
          tomHomePath, generateStratMapping, multithread, nosharing, jmicompatible,
          getStreamManager().getImportList(),getGomEnvironment());
    backend.generate(classList);
    if (null == classList) {
      GomMessage.error(getLogger(), getStreamManager().getInputFileName(),0,
          GomMessage.generationIssue,
          getStreamManager().getInputFileName());
    } else {
      GomMessage.info(getLogger(), getStreamManager().getInputFileName(), 0,
          GomMessage.gomGenerationPhase,
          (System.currentTimeMillis()-startChrono));
    }
    informationTracker.put(KEY_LAST_GEN_MAPPING,getGomEnvironment().getLastGeneratedMapping());
  }

  
  public Object[] getArgs() {
    return new Object[]{getGomEnvironment()};
  }
}

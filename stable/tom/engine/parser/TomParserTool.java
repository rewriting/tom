
























package tom.engine.parser;



import java.io.*;
import java.util.*;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;
import tom.platform.OptionManager;
import tom.engine.TomStreamManager;
import tom.engine.exception.TomIncludeException;
import tom.engine.TomMessage;



import tom.library.sl.*;



import tom.engine.adt.code.types.*;
import tom.engine.adt.cst.types.*;



public class TomParserTool {
     private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;   }   private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ));}private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return (  tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) )  );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_BottomUp( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), tom.library.sl.Sequence.make(v, null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}


  private static Logger logger = Logger.getLogger("tom.engine.parser.TomParserTool");
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

  private TomStreamManager streamManager;
  private OptionManager optionManager;

  public TomParserTool(TomStreamManager streamManager, OptionManager optionManager) {
    this.streamManager = streamManager;
    this.optionManager = optionManager;
  }

  public TomStreamManager getStreamManager() {
    return this.streamManager;
  }
  public OptionManager getOptionManager() {
    return this.optionManager;
  }

  
  public String searchIncludeFile(String currentFileName, String filename, int lineNumber) throws TomIncludeException {

    String includeName = filename.trim();
    includeName = includeName.replace('/',File.separatorChar);
    includeName = includeName.replace('\\',File.separatorChar);
    if(includeName.equals("")) {
      throw new TomIncludeException(TomMessage.missingIncludedFile,
        new Object[]{currentFileName, lineNumber});
    }

    File file = new File(includeName);
    if(file.isAbsolute()) {
      try {
        file = file.getCanonicalFile();
      } catch (IOException e) {
        System.out.println("IO Exception when computing included file");
        e.printStackTrace();
      }

      if(!file.exists()) {
        file = null;
      }
    } else {
      

      
      
      file = getStreamManager().findFile(new File(currentFileName).getParentFile(), includeName);
    }
    if(file == null) {
      throw new TomIncludeException(TomMessage.includedFileNotFound,
          new Object[]{
            filename, 
            currentFileName, 
            lineNumber,
            currentFileName});
    }

    String res = "";
    try {
      res = file.getCanonicalPath();
    } catch (IOException e) {
      TomMessage.finer(getLogger(), null, 0, TomMessage.failGetCanonicalPath,file.getPath());
    }
    return res;
  }

  
  public String parseGomFile(String gomCode, int initialGomLine, String[] userOpts) throws TomIncludeException {
    String res = "";
    
    String currentFileName = getStreamManager().getInputFileName();

    File configFile = null;
    ArrayList<String> parameters = new ArrayList<String>();
    try {
      String tom_home = System.getProperty("tom.home");
      if(tom_home != null) {
        configFile = new File(tom_home,"Gom.config");
      } else {
        
        String tom_config_filename = ((String)getOptionManager().getOptionValue("X"));
        configFile = new File(new File(tom_config_filename).getParentFile(),"Gom.config");
        
        java.util.List<File> imp = getStreamManager().getUserImportList();
        for(File f:imp) {
          parameters.add("--import");
          parameters.add(f.getCanonicalPath());
        }
      }
      configFile = configFile.getCanonicalFile();
    } catch (IOException e) {
      TomMessage.finer(getLogger(), null, 0, TomMessage.failGetCanonicalPath,configFile.getPath());
    }

    String destDir = getStreamManager().getDestDir().getPath();
    String packageName = getStreamManager().getPackagePath().replace(File.separatorChar, '.');
    String inputFileNameWithoutExtension = getStreamManager().getRawFileName().toLowerCase();
    String subPackageName = "";
    if(packageName.equals("")) {
      subPackageName = inputFileNameWithoutExtension;
    } else {
      subPackageName = packageName + "." + inputFileNameWithoutExtension;
    }

    parameters.add("-X");
    parameters.add(configFile.getPath());
    parameters.add("--destdir");
    parameters.add(destDir);
    parameters.add("--package");
    parameters.add(subPackageName);
    if(getOptionManager().getOptionValue("wall")==Boolean.TRUE) {
      parameters.add("--wall");
    }
    if(getOptionManager().getOptionValue("intermediate")==Boolean.TRUE) {
      parameters.add("--intermediate");
    }
    if(Boolean.TRUE == getOptionManager().getOptionValue("optimize")) {
      parameters.add("--optimize");
    }
    if(Boolean.TRUE == getOptionManager().getOptionValue("optimize2")) {
      parameters.add("--optimize2");
    }
    if(Boolean.TRUE == getOptionManager().getOptionValue("newtyper")) {
      parameters.add("--newtyper");
    }
    if(Boolean.TRUE == getOptionManager().getOptionValue("newparser")) {
      parameters.add("--newparser");
    }
    parameters.add("--intermediateName");
    parameters.add(getStreamManager().getRawFileName()+".t.gom");
    if(getOptionManager().getOptionValue("verbose")==Boolean.TRUE) {
      parameters.add("--verbose");
    }
    
    
    
    
      for(int i=0; i < userOpts.length; i++) {
        parameters.add(userOpts[i]);
      }
    

    final File tmpFile;
    try {
      tmpFile = File.createTempFile("tmp", ".gom", null).getCanonicalFile();
      parameters.add(tmpFile.getPath());
    } catch (IOException e) {
      TomMessage.error(getLogger(), null, 0, TomMessage.ioExceptionTempGom,e.getMessage());
      e.printStackTrace();
      return res;
    }

    TomMessage.fine(getLogger(), null, 0, TomMessage.writingExceptionTempGom,tmpFile.getPath());

    try {
      Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmpFile)));
      writer.write(new String(gomCode.getBytes("UTF-8")));
      writer.flush();
      writer.close();
    } catch (IOException e) {
      TomMessage.error(getLogger(), null, 0, TomMessage.writingFailureTempGom,e.getMessage());
      return res;
    }

    
    Object[] preparams = parameters.toArray();
    String[] params = new String[preparams.length];
    for (int i = 0; i < preparams.length; i++) {
      params[i] = (String)preparams[i];
    }

    int errorCode = 1;
    Map<String,String> informationTracker = new HashMap<String,String>();
    informationTracker.put(tom.engine.tools.TomGenericPlugin.KEY_LAST_GEN_MAPPING,null);

    informationTracker.put("gomBegin",""+initialGomLine);
    informationTracker.put("inputFileName",getStreamManager().getInputFileName());

    try {
      
      
      errorCode = ((Integer) Class.forName("tom.gom.Gom")
          .getMethod("exec", new Class[] {params.getClass(), Map.class})
          .invoke(null, new Object[] {params, informationTracker}))
        .intValue();
    } catch (ClassNotFoundException cnfe) {
      TomMessage.error(getLogger(), currentFileName, initialGomLine, TomMessage.gomInitFailure,currentFileName,Integer.valueOf(initialGomLine), cnfe);
    } catch (NoSuchMethodException nsme) {
      TomMessage.error(getLogger(), currentFileName, initialGomLine, TomMessage.gomInitFailure,currentFileName,Integer.valueOf(initialGomLine), nsme);
    } catch (InvocationTargetException ite) {
      TomMessage.error(getLogger(), currentFileName, initialGomLine, TomMessage.gomInitFailure,currentFileName,Integer.valueOf(initialGomLine), ite);
    } catch (IllegalAccessException iae) {
      TomMessage.error(getLogger(), currentFileName, initialGomLine, TomMessage.gomInitFailure,currentFileName,Integer.valueOf(initialGomLine), iae);
    }
    tmpFile.deleteOnExit();
    if(errorCode != 0) {
      TomMessage.error(getLogger(), currentFileName, initialGomLine, TomMessage.gomFailure,currentFileName,Integer.valueOf(initialGomLine));
      return res;
    }
    String generatedMapping = (String)informationTracker.get(tom.engine.tools.TomGenericPlugin.KEY_LAST_GEN_MAPPING);

    
    
    if(generatedMapping != null) {
      res = generatedMapping;
    }

    return res;
  }

  public void printTree(Visitable tree) {
    if(tree!=null) {
      try {
        tree = tom_make_BottomUp( new ToSingleLineTargetLanguage() ).visit(tree);
        tom.library.utils.Viewer.toTree(tree);
      } catch (tom.library.sl.VisitFailure e) {
        System.err.println("VisitFailure Exception"); 
      }
    } else {
      System.out.println("Nothing to print (tree is null)");
    }
  }

  
  public static class ToSingleLineTargetLanguage extends tom.library.sl.AbstractStrategyBasic {public ToSingleLineTargetLanguage() {super(( new tom.library.sl.Identity() ));}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.code.types.TargetLanguage) ) {return ((T)visit_TargetLanguage((( tom.engine.adt.code.types.TargetLanguage )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.TargetLanguage  _visit_TargetLanguage( tom.engine.adt.code.types.TargetLanguage  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.TargetLanguage )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.TargetLanguage  visit_TargetLanguage( tom.engine.adt.code.types.TargetLanguage  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.code.types.TargetLanguage) ) {if ( ((( tom.engine.adt.code.types.TargetLanguage )tom__arg) instanceof tom.engine.adt.code.types.targetlanguage.TL) ) {


        return  tom.engine.adt.code.types.targetlanguage.TL.make(formatTargetLanguageString( (( tom.engine.adt.code.types.TargetLanguage )tom__arg).getCode() ),  (( tom.engine.adt.code.types.TargetLanguage )tom__arg).getStart() ,  (( tom.engine.adt.code.types.TargetLanguage )tom__arg).getEnd() ) ;
      }}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.code.types.TargetLanguage) ) {if ( ((( tom.engine.adt.code.types.TargetLanguage )tom__arg) instanceof tom.engine.adt.code.types.targetlanguage.ITL) ) {

        return  tom.engine.adt.code.types.targetlanguage.ITL.make(formatTargetLanguageString( (( tom.engine.adt.code.types.TargetLanguage )tom__arg).getCode() )) ;
      }}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.code.types.TargetLanguage) ) {if ( ((( tom.engine.adt.code.types.TargetLanguage )tom__arg) instanceof tom.engine.adt.code.types.targetlanguage.Comment) ) {

        return  tom.engine.adt.code.types.targetlanguage.Comment.make(formatTargetLanguageString( (( tom.engine.adt.code.types.TargetLanguage )tom__arg).getCode() )) ;
      }}}}return _visit_TargetLanguage(tom__arg,introspector);}}



  
  private static String formatTargetLanguageString(String s) {
    s = s.replaceAll("\n", "\\\\n");
    s = s.replaceAll("\r", "\\\\r");
    s = s.replaceAll("\t", "\\\\t");
    return "["+s+"]";
  }


  public String metaEncodeCode(String code) {
		
		char bs = '\\';
		StringBuilder sb = new StringBuilder((int)1.5*code.length());
		for(int i=0 ; i<code.length() ; i++) {
			char c = code.charAt(i);
			switch(c) {
				case '\n':
					sb.append(bs);
					sb.append('n');
					break;
				case '\r':
					sb.append(bs);
					sb.append('r');
					break;
				case '\t':
					sb.append(bs);
					sb.append('t');
					break;
				case '\"':
				case '\\':
					sb.append(bs);
					sb.append(c);
					break;
				default:
					sb.append(c);
			}
		}
    
		sb.insert(0,'\"');
		sb.append('\"');
		return sb.toString();
  }





}



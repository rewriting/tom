/*
 * Gom
 *
 * Copyright (c) 2006-2012, INPL, INRIA
 * Nancy, France.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 * Antoine Reilles  e-mail: Antoine.Reilles@loria.fr
 *
 **/

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
    {{if ( (((Object)h) instanceof tom.gom.adt.objects.types.HookList) ) {if ( (((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )((Object)h))) instanceof tom.gom.adt.objects.types.hooklist.ConsConcHook) || ((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )((Object)h))) instanceof tom.gom.adt.objects.types.hooklist.EmptyConcHook)) ) { tom.gom.adt.objects.types.HookList  tomMatch546__end__4=(( tom.gom.adt.objects.types.HookList )((Object)h));do {{if (!( tomMatch546__end__4.isEmptyConcHook() )) { tom.gom.adt.objects.types.Hook  tomMatch546_8= tomMatch546__end__4.getHeadConcHook() ;if ( (tomMatch546_8 instanceof tom.gom.adt.objects.types.Hook) ) {if ( ((( tom.gom.adt.objects.types.Hook )tomMatch546_8) instanceof tom.gom.adt.objects.types.hook.BlockHook) ) {

        res.append(CodeGen.generateCode( tomMatch546_8.getCode() ));
        res.append("\n");
      }}}if ( tomMatch546__end__4.isEmptyConcHook() ) {tomMatch546__end__4=(( tom.gom.adt.objects.types.HookList )((Object)h));} else {tomMatch546__end__4= tomMatch546__end__4.getTailConcHook() ;}}} while(!( (tomMatch546__end__4==(( tom.gom.adt.objects.types.HookList )((Object)h))) ));}}}}

    return res.toString();
  }

  protected String generateImport() {
    StringBuilder res = new StringBuilder();
    HookList h = tom_append_list_ConcHook(hooks, tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() );
    {{if ( (((Object)h) instanceof tom.gom.adt.objects.types.HookList) ) {if ( (((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )((Object)h))) instanceof tom.gom.adt.objects.types.hooklist.ConsConcHook) || ((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )((Object)h))) instanceof tom.gom.adt.objects.types.hooklist.EmptyConcHook)) ) { tom.gom.adt.objects.types.HookList  tomMatch547__end__4=(( tom.gom.adt.objects.types.HookList )((Object)h));do {{if (!( tomMatch547__end__4.isEmptyConcHook() )) { tom.gom.adt.objects.types.Hook  tomMatch547_8= tomMatch547__end__4.getHeadConcHook() ;if ( (tomMatch547_8 instanceof tom.gom.adt.objects.types.Hook) ) {if ( ((( tom.gom.adt.objects.types.Hook )tomMatch547_8) instanceof tom.gom.adt.objects.types.hook.ImportHook) ) {

        res.append(CodeGen.generateCode( tomMatch547_8.getCode() ));
        res.append("\n");
      }}}if ( tomMatch547__end__4.isEmptyConcHook() ) {tomMatch547__end__4=(( tom.gom.adt.objects.types.HookList )((Object)h));} else {tomMatch547__end__4= tomMatch547__end__4.getTailConcHook() ;}}} while(!( (tomMatch547__end__4==(( tom.gom.adt.objects.types.HookList )((Object)h))) ));}}}}

    return res.toString();
  }

  protected String generateInterface() {
    StringBuilder res = new StringBuilder();
    HookList h = tom_append_list_ConcHook(hooks, tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() );
    {{if ( (((Object)h) instanceof tom.gom.adt.objects.types.HookList) ) {if ( (((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )((Object)h))) instanceof tom.gom.adt.objects.types.hooklist.ConsConcHook) || ((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )((Object)h))) instanceof tom.gom.adt.objects.types.hooklist.EmptyConcHook)) ) { tom.gom.adt.objects.types.HookList  tomMatch548__end__4=(( tom.gom.adt.objects.types.HookList )((Object)h));do {{if (!( tomMatch548__end__4.isEmptyConcHook() )) { tom.gom.adt.objects.types.Hook  tomMatch548_8= tomMatch548__end__4.getHeadConcHook() ;if ( (tomMatch548_8 instanceof tom.gom.adt.objects.types.Hook) ) {if ( ((( tom.gom.adt.objects.types.Hook )tomMatch548_8) instanceof tom.gom.adt.objects.types.hook.InterfaceHook) ) {

        res.append(",");
        res.append(CodeGen.generateCode( tomMatch548_8.getCode() ));
        res.append("\n");
      }}}if ( tomMatch548__end__4.isEmptyConcHook() ) {tomMatch548__end__4=(( tom.gom.adt.objects.types.HookList )((Object)h));} else {tomMatch548__end__4= tomMatch548__end__4.getTailConcHook() ;}}} while(!( (tomMatch548__end__4==(( tom.gom.adt.objects.types.HookList )((Object)h))) ));}}}}

    return res.toString();
  }

  /*
   * The function for generating the file is extended, to be able to call Tom if
   * necessary (i.e. if there are user defined hooks)
   */
  public int generateFile() {
    if (hooks.containsTomCode()) {
      /* We need to call Tom to generate the file */
      File xmlFile = new File(tomHomePath,"Tom.xml");
      if(!xmlFile.exists()) {
        GomMessage.finer(getLogger(),null,0,
            GomMessage.getCanonicalPathFailure, xmlFile.getPath());
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
      tomParams.add(xmlFile.getPath());
      if(Boolean.TRUE == optionManager.getOptionValue("newtyper")) {
        tomParams.add("--newtyper");
      }
      if(Boolean.TRUE == optionManager.getOptionValue("newparser")) {
        tomParams.add("--newparser");
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

      //String[] params = {"-X",xmlFile.getPath(),"--optimize","--optimize2","--output",file_path,"-"};
      //String[] params = {"-X",config_xml,"--output",file_path,"-"};

      //System.out.println("params: " + tomParams);

      try {
        StringWriter gen = new StringWriter();
        generate(gen);

        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmpFile)));
        writer.write(new String(gen.toString().getBytes("UTF-8"))); 
        writer.flush();
        writer.close();

        int res = tom.engine.Tom.exec(tomParams.toArray(new String[0]));
        tmpFile.deleteOnExit();

        //int res = tom.engine.Tom.exec(tomParams.toArray(new String[0]),informationTracker);
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

  /** the class logger instance*/
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

}

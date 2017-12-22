/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2017, Universite de Lorraine, Inria
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
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.engine.backend;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.*;

import tom.engine.TomMessage;
import tom.engine.TomBase;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.code.types.*;

import tom.engine.tools.*;
import tom.engine.exception.TomRuntimeException;
import tom.platform.PluginPlatformMessage;
import tom.platform.PlatformException;
import tom.platform.adt.platformoption.types.PlatformOptionList;

import tom.library.sl.*;
import tom.library.sl.VisitFailure;


/**
 * The TomBackend "plugin".
 * Has to create the generator depending on OptionManager, create the output 
 * writer and generting the output code.
 */
public class BackendPlugin extends TomGenericPlugin {

     private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_append_list_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList l1,  tom.platform.adt.platformoption.types.PlatformOptionList  l2) {     if( l1.isEmptyconcPlatformOption() ) {       return l2;     } else if( l2.isEmptyconcPlatformOption() ) {       return l1;     } else if(  l1.getTailconcPlatformOption() .isEmptyconcPlatformOption() ) {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,l2) ;     } else {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,tom_append_list_concPlatformOption( l1.getTailconcPlatformOption() ,l2)) ;     }   }   private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_get_slice_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList  begin,  tom.platform.adt.platformoption.types.PlatformOptionList  end, tom.platform.adt.platformoption.types.PlatformOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcPlatformOption()  ||  (end== tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( begin.getHeadconcPlatformOption() ,( tom.platform.adt.platformoption.types.PlatformOptionList )tom_get_slice_concPlatformOption( begin.getTailconcPlatformOption() ,end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;   }   private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ));}private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return (  tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) )  );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDownCollect( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),tom_make_Try( tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) )) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}





  /** the tabulation starting value */
  public final static int defaultDeep = 1;

  /** the declared options string */

  public static final PlatformOptionList PLATFORM_OPTIONS =
     tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("noOutput", "", "Do not generate code",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("aCode", "a", "Generate Ada code",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("jCode", "j", "Generate Java code",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.True.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("csCode", "", "Generate C# code",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("cCode", "c", "Generate C code",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("camlCode", "", "Generate Caml code",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("pCode", "", "Generate Python code",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("inline", "", "Inline mapping",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("inlineplus", "", "Inline mapping",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) ) ) ) ) ) ) ) ) 










;

  public static final String CCODE = "cCode"; 
  public static final String CAMLCODE = "camlCode"; 
  public static final String PCODE = "pCode"; 
  public static final String JCODE = "jCode"; 
  public static final String ACODE = "aCode"; 

  /** the generated file name */
  private String generatedFileName = null;

  /** Constructor*/
  public BackendPlugin() {
    super("BackendPlugin");
  }

  /**
   *
   */
  public void run(Map informationTracker) {
    //System.out.println("(debug) I'm in the Tom backend : TSM"+getStreamManager().toString());
    try {
      if(isActivated() == true) {
        AbstractGenerator generator = null;
        Writer writer;
        long startChrono = System.currentTimeMillis();
        try {
          String encoding = getOptionStringValue("encoding");
          writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getStreamManager().getOutputFile()),encoding));
          OutputCode output = new OutputCode(writer, getOptionManager());
          if(getOptionBooleanValue("noOutput")) {
            throw new TomRuntimeException("Backend activated, but noOutput is set");
          } else if(getOptionBooleanValue(BackendPlugin.CCODE)) {
            generator = new CGenerator(output, getOptionManager(), getSymbolTable());
          } else if(getOptionBooleanValue(BackendPlugin.CAMLCODE)) {
            generator = new CamlGenerator(output, getOptionManager(), getSymbolTable());
          } else if(getOptionBooleanValue(BackendPlugin.PCODE)) {
            generator = new PythonGenerator(output, getOptionManager(), getSymbolTable());
          } else if(getOptionBooleanValue("aCode")) {
            generator = new AdaGenerator(output, getOptionManager(), getSymbolTable());
          } else if(getOptionBooleanValue("csCode")) {
            generator = new CSharpGenerator(output, getOptionManager(), getSymbolTable());
          } else if(getOptionBooleanValue(BackendPlugin.JCODE)) {
            generator = new JavaGenerator(output, getOptionManager(), getSymbolTable());
          } else {
            throw new TomRuntimeException("no selected language for the Backend");
          }

          Code pilCode = (Code) getWorkingTerm();

          //System.out.println("pil: " + pilCode);

          markUsedConstructorDestructor(pilCode);

          generator.generate(defaultDeep, pilCode,TomBase.DEFAULT_MODULE_NAME);
          // verbose
          TomMessage.info(getLogger(), getStreamManager().getInputFileName(),
              0, TomMessage.tomGenerationPhase,
              Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));
          output.close();
        } catch (IOException e) {
          TomMessage.error(getLogger(), getStreamManager().getInputFileName(),
              0, TomMessage.backendIOException, e.getMessage());
          return;
        } catch (Exception e) {
          String fileName = getStreamManager().getInputFileName();
          //int line = -1;
          // set line number to -1 instead of 0 in order to keep the old
          // behavior. It will probably be modified soon.
          TomMessage.error(getLogger(),fileName,-1,TomMessage.exceptionMessage, fileName);
          e.printStackTrace();
          return;
        }
        // set the generated File Name
        try {
          generatedFileName = getStreamManager().getOutputFile().getCanonicalPath();
        } catch (IOException e) {
          System.out.println("IO Exception when computing generatedFileName");
          e.printStackTrace();
        }
      } else {
        // backend is desactivated
        TomMessage.info(getLogger(),getStreamManager().getInputFileName(),0,TomMessage.backendInactivated);
      }
    } catch(PlatformException e) {
      TomMessage.error(getLogger(),null,0,PluginPlatformMessage.platformStopped);
      return;
    }
  }

  public void optionChanged(String optionName, Object optionValue) {
    //System.out.println("optionChanged: " + optionName + " --> " + optionValue);
    if(optionName.equals(BackendPlugin.CAMLCODE) && ((Boolean)optionValue).booleanValue() ) {
      setOptionValue(BackendPlugin.JCODE, Boolean.FALSE);
      setOptionValue(BackendPlugin.CCODE, Boolean.FALSE);
      setOptionValue(BackendPlugin.PCODE, Boolean.FALSE);
      setOptionValue(BackendPlugin.ACODE, Boolean.FALSE);        
    } else if(optionName.equals(BackendPlugin.CCODE) && ((Boolean)optionValue).booleanValue() ) {
      setOptionValue(BackendPlugin.JCODE, Boolean.FALSE);
      setOptionValue(BackendPlugin.CAMLCODE, Boolean.FALSE);
      setOptionValue(BackendPlugin.PCODE, Boolean.FALSE);
      setOptionValue(BackendPlugin.ACODE, Boolean.FALSE);        
    } else if(optionName.equals(BackendPlugin.JCODE) && ((Boolean)optionValue).booleanValue() ) {
      setOptionValue(BackendPlugin.CCODE, Boolean.FALSE);
      setOptionValue(BackendPlugin.CAMLCODE, Boolean.FALSE);
      setOptionValue(BackendPlugin.PCODE, Boolean.FALSE);
      setOptionValue(BackendPlugin.ACODE, Boolean.FALSE);        
    } else if(optionName.equals(BackendPlugin.PCODE) && ((Boolean)optionValue).booleanValue() ) {
      setOptionValue(BackendPlugin.CCODE, Boolean.FALSE);
      setOptionValue(BackendPlugin.CAMLCODE, Boolean.FALSE);
      setOptionValue(BackendPlugin.JCODE, Boolean.FALSE);
      setOptionValue(BackendPlugin.ACODE, Boolean.FALSE);        
    } else if(optionName.equals(BackendPlugin.ACODE) && ((Boolean)optionValue).booleanValue() ) { 
      setOptionValue(BackendPlugin.CCODE, Boolean.FALSE);
      setOptionValue(BackendPlugin.CAMLCODE, Boolean.FALSE);
      setOptionValue(BackendPlugin.JCODE, Boolean.FALSE);
      setOptionValue(BackendPlugin.PCODE, Boolean.FALSE);
    }
  }

  /**
   * inherited from OptionOwner interface (plugin) 
   */
  public PlatformOptionList getDeclaredOptionList() {
    return PLATFORM_OPTIONS; 
  }

  private boolean isActivated() {
    return !getOptionBooleanValue("noOutput");
  }

  protected SymbolTable getSymbolTable(String moduleName) {
    //TODO//
    //Using of the moduleName
    ////////

    //System.out.println(getSymbolTable().toTerm());

    return getSymbolTable();
  }
  /**
   * inherited from plugin interface
   * returns the generated file name
   */
  public Object[] getArgs() {
    return new Object[]{generatedFileName};
  }

  











  private void markUsedConstructorDestructor(Code pilCode) {
    Stack<String> stack = new Stack<String>();
    stack.push(TomBase.DEFAULT_MODULE_NAME);
    try {
      ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("markStrategy") ),tom_make_TopDownCollect( new Collector(( new tom.library.sl.MuVar("markStrategy") ),this,stack) )) )
.visitLight(pilCode);
    } catch(VisitFailure e) { /* Ignored */ }
  }

  private void setUsedSymbolConstructor(String moduleName, TomSymbol tomSymbol, Strategy markStrategy) {
    SymbolTable st = getSymbolTable(moduleName);
    if(!st.isUsedSymbolConstructor(tomSymbol) && !st.isUsedSymbolDestructor(tomSymbol)) {
      try {
        markStrategy.visitLight(tomSymbol);
      } catch(VisitFailure e) { /* Ignored */ }
    }
    getSymbolTable(moduleName).setUsedSymbolConstructor(tomSymbol);
  }

  private void setUsedSymbolDestructor(String moduleName, TomSymbol tomSymbol, Strategy markStrategy) {    
    SymbolTable st = getSymbolTable(moduleName);
    if(!st.isUsedSymbolConstructor(tomSymbol) && !st.isUsedSymbolDestructor(tomSymbol)) {
      try {
        markStrategy.visitLight(tomSymbol);
      } catch(VisitFailure e) { /* Ignored */ }
    }
    getSymbolTable(moduleName).setUsedSymbolDestructor(tomSymbol);
  }

  private void setUsedType(String moduleName, String tomTypeName, Strategy markStrategy) {
    getSymbolTable(moduleName).setUsedType(tomTypeName);
  }

  /*
   * the strategy Collector is used collect the part of the mapping that is really used
   * this strategy also collect the declarations (IsFsymDecl, GetSlotDecl, etc)
   * to fill the mapInliner used by the backend to inline calls to IsFsym, GetSlot, etc.
   */
  public static class Collector extends tom.library.sl.AbstractStrategyBasic {private  tom.library.sl.Strategy  markStrategy;private  BackendPlugin  bp;private  Stack<String>  stack;public Collector( tom.library.sl.Strategy  markStrategy,  BackendPlugin  bp,  Stack<String>  stack) {super(( new tom.library.sl.Identity() ));this.markStrategy=markStrategy;this.bp=bp;this.stack=stack;}public  tom.library.sl.Strategy  getmarkStrategy() {return markStrategy;}public  BackendPlugin  getbp() {return bp;}public  Stack<String>  getstack() {return stack;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);stratChildren[1] = getmarkStrategy();return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);markStrategy = ( tom.library.sl.Strategy ) children[1];return this;}public int getChildCount() {return 2;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);case 1: return getmarkStrategy();default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);case 1: markStrategy = ( tom.library.sl.Strategy )child; return this;default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomexpression.types.Expression) ) {return ((T)visit_Expression((( tom.engine.adt.tomexpression.types.Expression )v),introspector));}if ( (v instanceof tom.engine.adt.tomtype.types.TomType) ) {return ((T)visit_TomType((( tom.engine.adt.tomtype.types.TomType )v),introspector));}if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if ( (v instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {return ((T)visit_Declaration((( tom.engine.adt.tomdeclaration.types.Declaration )v),introspector));}if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomdeclaration.types.Declaration  _visit_Declaration( tom.engine.adt.tomdeclaration.types.Declaration  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomdeclaration.types.Declaration )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomtype.types.TomType  _visit_TomType( tom.engine.adt.tomtype.types.TomType  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomtype.types.TomType )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomexpression.types.Expression  _visit_Expression( tom.engine.adt.tomexpression.types.Expression  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomexpression.types.Expression )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomdeclaration.types.Declaration  visit_Declaration( tom.engine.adt.tomdeclaration.types.Declaration  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsFsymDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch80_1= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ; tom.engine.adt.tomexpression.types.Expression  tomMatch80_2= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getExpr() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch80_1) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch80_2) instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {













































































































































        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putIsFsym( tomMatch80_1.getString() , tomMatch80_2.getCode() );
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsSortDecl) ) { tom.engine.adt.code.types.BQTerm  tomMatch80_12= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getTermArg() ; tom.engine.adt.tomexpression.types.Expression  tomMatch80_13= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getExpr() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch80_12) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomtype.types.TomType  tomMatch80_16= tomMatch80_12.getAstType() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch80_16) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch80_13) instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {












        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putIsSort( tomMatch80_16.getTomType() , tomMatch80_13.getCode() );
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.EqualTermDecl) ) { tom.engine.adt.code.types.BQTerm  tomMatch80_26= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getTermArg1() ; tom.engine.adt.tomexpression.types.Expression  tomMatch80_27= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getExpr() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch80_26) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomtype.types.TomType  tomMatch80_30= tomMatch80_26.getAstType() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch80_30) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch80_27) instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {



        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putEqualTerm( tomMatch80_30.getTomType() , tomMatch80_27.getCode() );
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetSlotDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch80_40= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ; tom.engine.adt.tomname.types.TomName  tomMatch80_41= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getSlotName() ; tom.engine.adt.tomexpression.types.Expression  tomMatch80_42= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getExpr() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch80_40) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch80_41) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch80_42) instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {



        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putGetSlot( tomMatch80_40.getString() , tomMatch80_41.getString() , tomMatch80_42.getCode() );
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetHeadDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch80_55= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getOpname() ; tom.engine.adt.tomexpression.types.Expression  tomMatch80_56= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getExpr() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch80_55) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch80_56) instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {



        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putGetHead( tomMatch80_55.getString() , tomMatch80_56.getCode() );
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetTailDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch80_66= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getOpname() ; tom.engine.adt.tomexpression.types.Expression  tomMatch80_67= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getExpr() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch80_66) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch80_67) instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {



        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putGetTail( tomMatch80_66.getString() , tomMatch80_67.getCode() );
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch80_77= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ; tom.engine.adt.tominstruction.types.Instruction  tomMatch80_78= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getInstr() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch80_77) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tomMatch80_78) instanceof tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction) ) { tom.engine.adt.tomexpression.types.Expression  tomMatch80_84= tomMatch80_78.getExpr() ;if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch80_84) instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {



        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putMake( tomMatch80_77.getString() , tomMatch80_84.getCode() );
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsEmptyDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch80_91= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getOpname() ; tom.engine.adt.tomexpression.types.Expression  tomMatch80_92= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getExpr() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch80_91) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch80_92) instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {



        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putIsEmptyList( tomMatch80_91.getString() , tomMatch80_92.getCode() );
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyList) ) { tom.engine.adt.tomname.types.TomName  tomMatch80_102= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ; tom.engine.adt.tominstruction.types.Instruction  tomMatch80_103= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getInstr() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch80_102) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tomMatch80_103) instanceof tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction) ) { tom.engine.adt.tomexpression.types.Expression  tomMatch80_109= tomMatch80_103.getExpr() ;if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch80_109) instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {



        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putMakeEmptyList( tomMatch80_102.getString() , tomMatch80_109.getCode() );
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddList) ) { tom.engine.adt.tomname.types.TomName  tomMatch80_116= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ; tom.engine.adt.tominstruction.types.Instruction  tomMatch80_117= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getInstr() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch80_116) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tomMatch80_117) instanceof tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction) ) { tom.engine.adt.tomexpression.types.Expression  tomMatch80_123= tomMatch80_117.getExpr() ;if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch80_123) instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {



        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putMakeAddList( tomMatch80_116.getString() , tomMatch80_123.getCode() );
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyArray) ) { tom.engine.adt.tomname.types.TomName  tomMatch80_130= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ; tom.engine.adt.tominstruction.types.Instruction  tomMatch80_131= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getInstr() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch80_130) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tomMatch80_131) instanceof tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction) ) { tom.engine.adt.tomexpression.types.Expression  tomMatch80_137= tomMatch80_131.getExpr() ;if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch80_137) instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {



        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putMakeEmptyArray( tomMatch80_130.getString() , tomMatch80_137.getCode() );
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddArray) ) { tom.engine.adt.tomname.types.TomName  tomMatch80_144= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ; tom.engine.adt.tominstruction.types.Instruction  tomMatch80_145= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getInstr() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch80_144) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tomMatch80_145) instanceof tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction) ) { tom.engine.adt.tomexpression.types.Expression  tomMatch80_151= tomMatch80_145.getExpr() ;if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch80_151) instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {



        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putMakeAddArray( tomMatch80_144.getString() , tomMatch80_151.getCode() );
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetElementDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch80_158= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getOpname() ; tom.engine.adt.tomexpression.types.Expression  tomMatch80_159= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getExpr() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch80_158) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch80_159) instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {



        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putGetElementArray( tomMatch80_158.getString() , tomMatch80_159.getCode() );
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetSizeDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch80_169= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getOpname() ; tom.engine.adt.tomexpression.types.Expression  tomMatch80_170= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getExpr() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch80_169) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch80_170) instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {



        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putGetSizeArray( tomMatch80_169.getString() , tomMatch80_170.getCode() );
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {boolean tomMatch80_188= false ; tom.engine.adt.tomdeclaration.types.Declaration  tomMatch80_183= null ; tom.engine.adt.tomname.types.TomName  tomMatch80_180= null ; tom.engine.adt.tomdeclaration.types.Declaration  tomMatch80_184= null ; tom.engine.adt.tomdeclaration.types.Declaration  tomMatch80_182= null ;if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.SymbolDecl) ) {{ /* unamed block */tomMatch80_188= true ;tomMatch80_182=(( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg);tomMatch80_180= tomMatch80_182.getAstName() ;}} else {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.ListSymbolDecl) ) {{ /* unamed block */tomMatch80_188= true ;tomMatch80_183=(( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg);tomMatch80_180= tomMatch80_183.getAstName() ;}} else {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.ArraySymbolDecl) ) {{ /* unamed block */tomMatch80_188= true ;tomMatch80_184=(( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg);tomMatch80_180= tomMatch80_184.getAstName() ;}}}}if (tomMatch80_188) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch80_180) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {



        try {
          String moduleName = stack.peek();
          TomSymbol tomSymbol = TomBase.getSymbolFromName( tomMatch80_180.getString() ,bp.getSymbolTable(moduleName));
          markStrategy.visitLight(tomSymbol);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.ResolveGetSlotDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch80_190= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ; tom.engine.adt.tomname.types.TomName  tomMatch80_191= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getSlotName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch80_190) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch80_191) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {






        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putResolveGetSlot( tomMatch80_190.getString() , tomMatch80_191.getString() );
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}}return _visit_Declaration(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch81_8= false ; tom.engine.adt.code.types.BQTerm  tomMatch81_3= null ; tom.engine.adt.tomname.types.TomName  tomMatch81_1= null ; tom.engine.adt.code.types.BQTerm  tomMatch81_4= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BuildTerm) ) {{ /* unamed block */tomMatch81_8= true ;tomMatch81_3=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch81_1= tomMatch81_3.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyArray) ) {{ /* unamed block */tomMatch81_8= true ;tomMatch81_4=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch81_1= tomMatch81_4.getAstName() ;}}}if (tomMatch81_8) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch81_1) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {         try {           /*System.out.println("build: " + `name);*/           String moduleName = stack.peek();           /*System.out.println("moduleName: " + moduleName);*/           TomSymbol tomSymbol = TomBase.getSymbolFromName( tomMatch81_1.getString() ,bp.getSymbolTable(moduleName));            bp.setUsedSymbolConstructor(moduleName,tomSymbol,markStrategy);         } catch (EmptyStackException e) {           System.out.println("No moduleName in stack");         }       }}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch81_20= false ; tom.engine.adt.code.types.BQTerm  tomMatch81_14= null ; tom.engine.adt.code.types.BQTerm  tomMatch81_12= null ; tom.engine.adt.code.types.BQTerm  tomMatch81_15= null ; tom.engine.adt.tomname.types.TomName  tomMatch81_10= null ; tom.engine.adt.code.types.BQTerm  tomMatch81_13= null ; tom.engine.adt.code.types.BQTerm  tomMatch81_16= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BuildConsList) ) {{ /* unamed block */tomMatch81_20= true ;tomMatch81_12=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch81_10= tomMatch81_12.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyList) ) {{ /* unamed block */tomMatch81_20= true ;tomMatch81_13=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch81_10= tomMatch81_13.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BuildAppendList) ) {{ /* unamed block */tomMatch81_20= true ;tomMatch81_14=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch81_10= tomMatch81_14.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BuildConsArray) ) {{ /* unamed block */tomMatch81_20= true ;tomMatch81_15=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch81_10= tomMatch81_15.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BuildAppendArray) ) {{ /* unamed block */tomMatch81_20= true ;tomMatch81_16=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch81_10= tomMatch81_16.getAstName() ;}}}}}}if (tomMatch81_20) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch81_10) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {         try {           /*System.out.println("build: " + `name);*/           String moduleName = stack.peek();           /*System.out.println("moduleName: " + moduleName);*/           TomSymbol tomSymbol = TomBase.getSymbolFromName( tomMatch81_10.getString() ,bp.getSymbolTable(moduleName));            bp.setUsedSymbolConstructor(moduleName,tomSymbol,markStrategy);           /* XXX: Also mark the destructors as used, since some generated            * functions will use them */           bp.setUsedSymbolDestructor(moduleName,tomSymbol,markStrategy);           /*            * resolve uses in the symbol declaration            */         } catch (EmptyStackException e) {           System.out.println("No moduleName in stack");         }       }}}}}return _visit_BQTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {         TomNameList l =  (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getNameList() ;         /*System.out.println("dest " + `l);*/         while(!l.isEmptyconcTomName()) {           try {             /*System.out.println("op: " + l.getHead());*/             String moduleName = stack.peek();             /*System.out.println("moduleName: " + moduleName);*/             TomSymbol tomSymbol = TomBase.getSymbolFromName(l.getHeadconcTomName().getString(),bp.getSymbolTable(moduleName));              /*System.out.println("mark: " + tomSymbol);*/             /*              * if it comes from java              */             if (tomSymbol != null) { bp.setUsedSymbolDestructor(moduleName,tomSymbol,markStrategy);}           } catch (EmptyStackException e) {             System.out.println("No moduleName in stack");           }           l = l.getTailconcTomName();         }         /*          * here we can fail because the subterms appear in isFsym tests          * therefore, they are marked when traversing the compiledAutomata          */         throw new tom.library.sl.VisitFailure();       }}}}return _visit_TomTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomtype.types.TomType  visit_TomType( tom.engine.adt.tomtype.types.TomType  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tom__arg) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {         try {           String moduleName = stack.peek();           bp.setUsedType(moduleName, (( tom.engine.adt.tomtype.types.TomType )tom__arg).getTomType() ,markStrategy);         } catch (EmptyStackException e) {           System.out.println("No moduleName in stack");         }       }}}}return _visit_TomType(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomexpression.types.Expression  visit_Expression( tom.engine.adt.tomexpression.types.Expression  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {boolean tomMatch84_12= false ; tom.engine.adt.tomexpression.types.Expression  tomMatch84_5= null ; tom.engine.adt.tomexpression.types.Expression  tomMatch84_4= null ; tom.engine.adt.tomexpression.types.Expression  tomMatch84_8= null ; tom.engine.adt.tomexpression.types.Expression  tomMatch84_3= null ; tom.engine.adt.tomname.types.TomName  tomMatch84_1= null ; tom.engine.adt.tomexpression.types.Expression  tomMatch84_7= null ; tom.engine.adt.tomexpression.types.Expression  tomMatch84_6= null ;if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.IsEmptyList) ) {{ /* unamed block */tomMatch84_12= true ;tomMatch84_3=(( tom.engine.adt.tomexpression.types.Expression )tom__arg);tomMatch84_1= tomMatch84_3.getOpname() ;}} else {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.IsEmptyArray) ) {{ /* unamed block */tomMatch84_12= true ;tomMatch84_4=(( tom.engine.adt.tomexpression.types.Expression )tom__arg);tomMatch84_1= tomMatch84_4.getOpname() ;}} else {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.GetHead) ) {{ /* unamed block */tomMatch84_12= true ;tomMatch84_5=(( tom.engine.adt.tomexpression.types.Expression )tom__arg);tomMatch84_1= tomMatch84_5.getOpname() ;}} else {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.GetTail) ) {{ /* unamed block */tomMatch84_12= true ;tomMatch84_6=(( tom.engine.adt.tomexpression.types.Expression )tom__arg);tomMatch84_1= tomMatch84_6.getOpname() ;}} else {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.GetSize) ) {{ /* unamed block */tomMatch84_12= true ;tomMatch84_7=(( tom.engine.adt.tomexpression.types.Expression )tom__arg);tomMatch84_1= tomMatch84_7.getOpname() ;}} else {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.GetElement) ) {{ /* unamed block */tomMatch84_12= true ;tomMatch84_8=(( tom.engine.adt.tomexpression.types.Expression )tom__arg);tomMatch84_1= tomMatch84_8.getOpname() ;}}}}}}}if (tomMatch84_12) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch84_1) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {         try {           /*System.out.println("list check: " + `name);*/           String moduleName = stack.peek();           /*System.out.println("moduleName: " + moduleName);*/           TomSymbol tomSymbol = TomBase.getSymbolFromName( tomMatch84_1.getString() ,bp.getSymbolTable(moduleName));            bp.setUsedSymbolConstructor(moduleName,tomSymbol,markStrategy);         } catch (EmptyStackException e) {           System.out.println("No moduleName in stack");         }       }}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.IsFsym) ) { tom.engine.adt.tomname.types.TomName  tomMatch84_14= (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch84_14) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {         try {           /*System.out.println("list check: " + `name);*/           String moduleName = stack.peek();           /*System.out.println("moduleName: " + moduleName);*/           TomSymbol tomSymbol = TomBase.getSymbolFromName( tomMatch84_14.getString() ,bp.getSymbolTable(moduleName));            bp.setUsedSymbolDestructor(moduleName,tomSymbol,markStrategy);         } catch (EmptyStackException e) {           System.out.println("No moduleName in stack");         }       }}}}}return _visit_Expression(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.CompiledMatch) ) {          String moduleName = TomBase.getModuleName( (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getOptions() );         /*          * push the modulename          * or the wrapping modulename if the current one          * (nested match for example) does not have one          */         if (moduleName==null) {           try {             moduleName = stack.peek();             stack.push(moduleName);             /*System.out.println("push2: " + moduleName);*/           } catch (EmptyStackException e) {             System.out.println("No moduleName in stack");           }         } else {           stack.push(moduleName);           /*System.out.println("push1: " + moduleName);*/         }         /*System.out.println("match -> moduleName = " + moduleName);*/         markStrategy.visitLight( (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getAutomataInst() );         /*String pop = (String) stack.pop();           System.out.println("pop: " + pop);*/         throw new tom.library.sl.VisitFailure();        }}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.RawAction) ) {         markStrategy.visitLight( (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getAstInstruction() );         throw new tom.library.sl.VisitFailure();       }}}}return _visit_Instruction(tom__arg,introspector);}}




}

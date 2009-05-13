/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2009, INRIA
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

import tom.engine.tools.*;
import tom.engine.exception.TomRuntimeException;
import tom.platform.OptionParser;
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
public class TomBackend extends TomGenericPlugin {

            private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Sequence) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) );       } else {         return ( (tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Sequence(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin):new tom.library.sl.Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)) );   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Choice) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):new tom.library.sl.Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) );       } else {         return ( (tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):new tom.library.sl.Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Choice(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin):new tom.library.sl.Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):( null )),end,tail)) );   }     private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  v) { return ( ( (( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )==null)?v:new tom.library.sl.Choice(v,( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )) ) );}private static  tom.library.sl.Strategy  tom_make_TopDownCollect( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),tom_make_Try(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) ))) ) );}   



  /** the tabulation starting value */
  private final static int defaultDeep = 2;

  /** the declared options string */
  public static final String DECLARED_OPTIONS = 
    "<options>" +
    "<boolean name='noOutput' altName=''  description='Do not generate code' value='false'/>" +
    "<boolean name='jCode'    altName='j' description='Generate Java code' value='true'/>" + 
    "<boolean name='csCode'   altName=''  description='Generate C# code' value='false'/>" + 
    "<boolean name='cCode'    altName='c' description='Generate C code' value='false'/>" +
    "<boolean name='camlCode' altName=''  description='Generate Caml code' value='false'/>" + 
    "<boolean name='pCode'    altName=''  description='Generate Python code' value='false'/>" + 
    "<boolean name='inline'   altName=''  description='Inline mapping' value='false'/>" +
    "<boolean name='inlineplus'   altName=''  description='Inline mapping' value='false'/>" +
    "</options>";

  /** the generated file name */
  private String generatedFileName = null;

  /** Constructor*/
  public TomBackend() {
    super("TomBackend");
  }

  /**
   *
   */
  public void run(Map informationTracker) {
    //System.out.println("(debug) I'm in the Tom backend : TSM"+getStreamManager().toString());
    try {
      if(isActivated() == true) {
        TomAbstractGenerator generator = null;
        Writer writer;
        long startChrono = System.currentTimeMillis();
        try {
          String encoding = getOptionStringValue("encoding");
          writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getStreamManager().getOutputFile()),encoding));
          OutputCode output = new OutputCode(writer, getOptionManager());
          if(getOptionBooleanValue("noOutput")) {
            throw new TomRuntimeException("Backend activated, but noOutput is set");
          } else if(getOptionBooleanValue("cCode")) {
            generator = new TomCGenerator(output, getOptionManager(), symbolTable());
          } else if(getOptionBooleanValue("camlCode")) {
            generator = new TomCamlGenerator(output, getOptionManager(), symbolTable());
          } else if(getOptionBooleanValue("pCode")) {
            generator = new TomPythonGenerator(output, getOptionManager(), symbolTable());
          } else if(getOptionBooleanValue("csCode")) {
            generator = new TomCSharpGenerator(output, getOptionManager(), symbolTable());
          } else if(getOptionBooleanValue("jCode")) {
            generator = new TomJavaGenerator(output, getOptionManager(), symbolTable());
          } else {
            throw new TomRuntimeException("no selected language for the Backend");
          }

          TomTerm pilCode = (TomTerm) getWorkingTerm();

          markUsedConstructorDestructor(pilCode);

          generator.generate(defaultDeep, generator.operatorsTogenerate(pilCode),TomBase.DEFAULT_MODULE_NAME);
          // verbose
          getLogger().log(Level.INFO,
              TomMessage.tomGenerationPhase.getMessage(),
              Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));
          output.close();
        } catch (IOException e) {
          getLogger().log(Level.SEVERE,
              TomMessage.backendIOException.getMessage(),
              new Object[]{getStreamManager().getOutputFile().getName(), e.getMessage()} );
          return;
        } catch (Exception e) {
          String fileName = getStreamManager().getInputFileName();
          int line = -1;
          TomMessage.error(getLogger(),fileName,line,TomMessage.exceptionMessage, new Object[]{fileName});
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
        getLogger().log(Level.INFO,TomMessage.backendInactivated.getMessage());
      }
    } catch(PlatformException e) {
      getLogger().log( Level.SEVERE, PluginPlatformMessage.platformStopped.getMessage());
      return;
    }
  }

  public void optionChanged(String optionName, Object optionValue) {
    //System.out.println("optionChanged: " + optionName + " --> " + optionValue);
    if(optionName.equals("camlCode") && ((Boolean)optionValue).booleanValue() ) { 
      setOptionValue("jCode", Boolean.FALSE);        
      setOptionValue("cCode", Boolean.FALSE);        
      setOptionValue("pCode", Boolean.FALSE);        
    } else if(optionName.equals("cCode") && ((Boolean)optionValue).booleanValue() ) { 
      setOptionValue("jCode", Boolean.FALSE);        
      setOptionValue("camlCode", Boolean.FALSE);        
      setOptionValue("pCode", Boolean.FALSE);        
    } else if(optionName.equals("jCode") && ((Boolean)optionValue).booleanValue() ) { 
      setOptionValue("cCode", Boolean.FALSE);        
      setOptionValue("camlCode", Boolean.FALSE);        
      setOptionValue("pCode", Boolean.FALSE);        
    } else if(optionName.equals("pCode") && ((Boolean)optionValue).booleanValue() ) { 
      setOptionValue("cCode", Boolean.FALSE);        
      setOptionValue("camlCode", Boolean.FALSE);        
      setOptionValue("jCode", Boolean.FALSE);        
    }
  }

  /**
   * inherited from OptionOwner interface (plugin) 
   */
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TomBackend.DECLARED_OPTIONS);
  }

  private boolean isActivated() {
    return !getOptionBooleanValue("noOutput");
  }

  protected SymbolTable getSymbolTable(String moduleName) {
    //TODO//
    //Using of the moduleName
    ////////

    //System.out.println(symbolTable().toTerm());

    return symbolTable();
  }
  /**
   * inherited from plugin interface
   * returns the generated file name
   */
  public Object[] getArgs() {
    return new Object[]{generatedFileName};
  }

  









  private void markUsedConstructorDestructor(TomTerm pilCode) {
    Stack<String> stack = new Stack<String>();
    stack.push(TomBase.DEFAULT_MODULE_NAME);
    try {
      ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("markStrategy") ),tom_make_TopDownCollect(tom_make_Collector(( new tom.library.sl.MuVar("markStrategy") ),this,stack))) ).visitLight(pilCode);
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
   * this strategy also collect the declarations (IsFsymDecl, GetSLotDecl, etc)
   * to fill the mapInliner used by the backend to inline calls to IsFsym, GetSlot, etc.
   */
  public static class Collector extends tom.library.sl.AbstractBasicStrategy {private  tom.library.sl.Strategy  markStrategy;private  TomBackend  tb;private  Stack<String>  stack;public Collector( tom.library.sl.Strategy  markStrategy,  TomBackend  tb,  Stack<String>  stack) {super(( new tom.library.sl.Identity() ));this.markStrategy=markStrategy;this.tb=tb;this.stack=stack;}public  tom.library.sl.Strategy  getmarkStrategy() {return markStrategy;}public  TomBackend  gettb() {return tb;}public  Stack<String>  getstack() {return stack;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);stratChilds[1] = getmarkStrategy();return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);markStrategy = ( tom.library.sl.Strategy ) children[1];return this;}public int getChildCount() {return 2;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);case 1: return getmarkStrategy();default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);case 1: markStrategy = ( tom.library.sl.Strategy )child; return this;default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.CompiledMatch) ) {



        String moduleName = TomBase.getModuleName( (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getOption() );
        /*
         * push the modulename
         * or the wrapping modulename if the current one
         * (nested match for example) does not have one
         */
        if (moduleName==null) {
          try {
            moduleName = stack.peek();
            stack.push(moduleName);
            //System.out.println("push2: " + moduleName);
          } catch (EmptyStackException e) {
            System.out.println("No moduleName in stack");
          }
        } else {
          stack.push(moduleName);
          //System.out.println("push1: " + moduleName);
        }
        //System.out.println("match -> moduleName = " + moduleName);
        markStrategy.visitLight( (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getAutomataInst() );
        //String pop = (String) stack.pop();
        //System.out.println("pop: " + pop);
        throw new tom.library.sl.VisitFailure();

      }}}{if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.TypedAction) ) {


        markStrategy.visitLight( (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getAstInstruction() );
        throw new tom.library.sl.VisitFailure();
      }}}}return _visit_Instruction(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tomexpression.types.Expression  visit_Expression( tom.engine.adt.tomexpression.types.Expression  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {boolean tomMatch47NameNumber_freshVar_5= false ; tom.engine.adt.tomname.types.TomName  tomMatch47NameNumber_freshVar_1= null ;if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.IsEmptyList) ) {{tomMatch47NameNumber_freshVar_5= true ;tomMatch47NameNumber_freshVar_1= (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getOpname() ;}} else {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.IsEmptyArray) ) {{tomMatch47NameNumber_freshVar_5= true ;tomMatch47NameNumber_freshVar_1= (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getOpname() ;}} else {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.GetHead) ) {{tomMatch47NameNumber_freshVar_5= true ;tomMatch47NameNumber_freshVar_1= (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getOpname() ;}} else {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.GetTail) ) {{tomMatch47NameNumber_freshVar_5= true ;tomMatch47NameNumber_freshVar_1= (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getOpname() ;}}}}}if ((tomMatch47NameNumber_freshVar_5 ==  true )) {if ( (tomMatch47NameNumber_freshVar_1 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {




        try {
          // System.out.println("list check: " + `name);
          String moduleName = stack.peek();
          //System.out.println("moduleName: " + moduleName);
          TomSymbol tomSymbol = TomBase.getSymbolFromName( tomMatch47NameNumber_freshVar_1.getString() ,tb.getSymbolTable(moduleName)); 
          tb.setUsedSymbolConstructor(moduleName,tomSymbol,markStrategy);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}{if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.IsFsym) ) { tom.engine.adt.tomname.types.TomName  tomMatch47NameNumber_freshVar_7= (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getAstName() ;if ( (tomMatch47NameNumber_freshVar_7 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        try {
          // System.out.println("list check: " + `name);
          String moduleName = stack.peek();
          //System.out.println("moduleName: " + moduleName);
          TomSymbol tomSymbol = TomBase.getSymbolFromName( tomMatch47NameNumber_freshVar_7.getString() ,tb.getSymbolTable(moduleName)); 
          tb.setUsedSymbolDestructor(moduleName,tomSymbol,markStrategy);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}return _visit_Expression(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tomtype.types.TomType  visit_TomType( tom.engine.adt.tomtype.types.TomType  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tom__arg) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TomType  tomMatch48NameNumber_freshVar_1= (( tom.engine.adt.tomtype.types.TomType )tom__arg).getTomType() ;if ( (tomMatch48NameNumber_freshVar_1 instanceof tom.engine.adt.tomtype.types.tomtype.ASTTomType) ) {




        try {
          String moduleName = stack.peek();
          tb.setUsedType(moduleName, tomMatch48NameNumber_freshVar_1.getString() ,markStrategy);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}return _visit_TomType(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch49NameNumber_freshVar_3= false ; tom.engine.adt.tomname.types.TomNameList  tomMatch49NameNumber_freshVar_1= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {{tomMatch49NameNumber_freshVar_3= true ;tomMatch49NameNumber_freshVar_1= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getNameList() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {{tomMatch49NameNumber_freshVar_3= true ;tomMatch49NameNumber_freshVar_1= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getNameList() ;}}}if ((tomMatch49NameNumber_freshVar_3 ==  true )) {




        TomNameList l = tomMatch49NameNumber_freshVar_1;
        // System.out.println("dest " + `l);
        while(!l.isEmptyconcTomName()) {
          try {
            //System.out.println("op: " + l.getHead());
            String moduleName = stack.peek();
            //System.out.println("moduleName: " + moduleName);
            TomSymbol tomSymbol = TomBase.getSymbolFromName(l.getHeadconcTomName().getString(),tb.getSymbolTable(moduleName)); 
            //System.out.println("mark: " + tomSymbol);
            // if it comes from java
            if (tomSymbol != null) { tb.setUsedSymbolDestructor(moduleName,tomSymbol,markStrategy);}
          } catch (EmptyStackException e) {
            System.out.println("No moduleName in stack");
          }
          l = l.getTailconcTomName();
        }
        /*
         * here we can fail because the subterms appear in isFsym tests
         * therefore, they are marked when traversing the compiledAutomata
         */
        throw new tom.library.sl.VisitFailure();
      }}}{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch49NameNumber_freshVar_9= false ; tom.engine.adt.tomname.types.TomName  tomMatch49NameNumber_freshVar_5= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.BuildTerm) ) {{tomMatch49NameNumber_freshVar_9= true ;tomMatch49NameNumber_freshVar_5= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstName() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.BuildEmptyArray) ) {{tomMatch49NameNumber_freshVar_9= true ;tomMatch49NameNumber_freshVar_5= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstName() ;}}}if ((tomMatch49NameNumber_freshVar_9 ==  true )) {if ( (tomMatch49NameNumber_freshVar_5 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

        try {
          // System.out.println("build: " + `name);
          String moduleName = stack.peek();
          //System.out.println("moduleName: " + moduleName);
          TomSymbol tomSymbol = TomBase.getSymbolFromName( tomMatch49NameNumber_freshVar_5.getString() ,tb.getSymbolTable(moduleName)); 
          tb.setUsedSymbolConstructor(moduleName,tomSymbol,markStrategy);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch49NameNumber_freshVar_15= false ; tom.engine.adt.tomname.types.TomName  tomMatch49NameNumber_freshVar_11= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.BuildConsList) ) {{tomMatch49NameNumber_freshVar_15= true ;tomMatch49NameNumber_freshVar_11= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstName() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.BuildEmptyList) ) {{tomMatch49NameNumber_freshVar_15= true ;tomMatch49NameNumber_freshVar_11= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstName() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.BuildAppendList) ) {{tomMatch49NameNumber_freshVar_15= true ;tomMatch49NameNumber_freshVar_11= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstName() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.BuildConsArray) ) {{tomMatch49NameNumber_freshVar_15= true ;tomMatch49NameNumber_freshVar_11= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstName() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.BuildAppendArray) ) {{tomMatch49NameNumber_freshVar_15= true ;tomMatch49NameNumber_freshVar_11= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstName() ;}}}}}}if ((tomMatch49NameNumber_freshVar_15 ==  true )) {if ( (tomMatch49NameNumber_freshVar_11 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

        try {
          // System.out.println("build: " + `name);
          String moduleName = stack.peek();
          //System.out.println("moduleName: " + moduleName);
          TomSymbol tomSymbol = TomBase.getSymbolFromName( tomMatch49NameNumber_freshVar_11.getString() ,tb.getSymbolTable(moduleName)); 
          tb.setUsedSymbolConstructor(moduleName,tomSymbol,markStrategy);
          /* XXX: Also mark the destructors as used, since some generated
           * functions will use them */
          tb.setUsedSymbolDestructor(moduleName,tomSymbol,markStrategy);
          // resolve uses in the symbol declaration
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}return _visit_TomTerm(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tomdeclaration.types.Declaration  visit_Declaration( tom.engine.adt.tomdeclaration.types.Declaration  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsFsymDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch50NameNumber_freshVar_1= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ; tom.engine.adt.tomexpression.types.Expression  tomMatch50NameNumber_freshVar_2= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getExpr() ;if ( (tomMatch50NameNumber_freshVar_1 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch50NameNumber_freshVar_2 instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {














        try {
          String moduleName = stack.peek();
          tb.getSymbolTable(moduleName).putIsFsym( tomMatch50NameNumber_freshVar_1.getString() , tomMatch50NameNumber_freshVar_2.getCode() );
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}{if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsSortDecl) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch50NameNumber_freshVar_9= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getTermArg() ; tom.engine.adt.tomexpression.types.Expression  tomMatch50NameNumber_freshVar_10= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getExpr() ;if ( (tomMatch50NameNumber_freshVar_9 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomtype.types.TomType  tomMatch50NameNumber_freshVar_12= tomMatch50NameNumber_freshVar_9.getAstType() ;if ( (tomMatch50NameNumber_freshVar_12 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TomType  tomMatch50NameNumber_freshVar_14= tomMatch50NameNumber_freshVar_12.getTomType() ;if ( (tomMatch50NameNumber_freshVar_14 instanceof tom.engine.adt.tomtype.types.tomtype.ASTTomType) ) {if ( (tomMatch50NameNumber_freshVar_10 instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {


        try {
          String moduleName = stack.peek();
          tb.getSymbolTable(moduleName).putIsSort( tomMatch50NameNumber_freshVar_14.getString() , tomMatch50NameNumber_freshVar_10.getCode() );
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}}}{if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.EqualTermDecl) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch50NameNumber_freshVar_22= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getTermArg1() ; tom.engine.adt.tomexpression.types.Expression  tomMatch50NameNumber_freshVar_23= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getExpr() ;if ( (tomMatch50NameNumber_freshVar_22 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomtype.types.TomType  tomMatch50NameNumber_freshVar_25= tomMatch50NameNumber_freshVar_22.getAstType() ;if ( (tomMatch50NameNumber_freshVar_25 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TomType  tomMatch50NameNumber_freshVar_27= tomMatch50NameNumber_freshVar_25.getTomType() ;if ( (tomMatch50NameNumber_freshVar_27 instanceof tom.engine.adt.tomtype.types.tomtype.ASTTomType) ) {if ( (tomMatch50NameNumber_freshVar_23 instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {


        try {
          String moduleName = stack.peek();
          tb.getSymbolTable(moduleName).putEqualTerm( tomMatch50NameNumber_freshVar_27.getString() , tomMatch50NameNumber_freshVar_23.getCode() );
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}}}{if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetSlotDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch50NameNumber_freshVar_35= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ; tom.engine.adt.tomname.types.TomName  tomMatch50NameNumber_freshVar_36= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getSlotName() ; tom.engine.adt.tomexpression.types.Expression  tomMatch50NameNumber_freshVar_37= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getExpr() ;if ( (tomMatch50NameNumber_freshVar_35 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch50NameNumber_freshVar_36 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch50NameNumber_freshVar_37 instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {


        try {
          String moduleName = stack.peek();
          tb.getSymbolTable(moduleName).putGetSlot( tomMatch50NameNumber_freshVar_35.getString() , tomMatch50NameNumber_freshVar_36.getString() , tomMatch50NameNumber_freshVar_37.getCode() );
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}}{if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch50NameNumber_freshVar_46= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ; tom.engine.adt.tominstruction.types.Instruction  tomMatch50NameNumber_freshVar_47= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getInstr() ;if ( (tomMatch50NameNumber_freshVar_46 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch50NameNumber_freshVar_47 instanceof tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction) ) { tom.engine.adt.tomexpression.types.Expression  tomMatch50NameNumber_freshVar_51= tomMatch50NameNumber_freshVar_47.getExpr() ;if ( (tomMatch50NameNumber_freshVar_51 instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {


        try {
          String moduleName = stack.peek();
          tb.getSymbolTable(moduleName).putMake( tomMatch50NameNumber_freshVar_46.getString() , tomMatch50NameNumber_freshVar_51.getCode() );
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}}{if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyList) ) { tom.engine.adt.tomname.types.TomName  tomMatch50NameNumber_freshVar_56= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ; tom.engine.adt.tominstruction.types.Instruction  tomMatch50NameNumber_freshVar_57= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getInstr() ;if ( (tomMatch50NameNumber_freshVar_56 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch50NameNumber_freshVar_57 instanceof tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction) ) { tom.engine.adt.tomexpression.types.Expression  tomMatch50NameNumber_freshVar_61= tomMatch50NameNumber_freshVar_57.getExpr() ;if ( (tomMatch50NameNumber_freshVar_61 instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {


        try {
          String moduleName = stack.peek();
          tb.getSymbolTable(moduleName).putMakeEmptyList( tomMatch50NameNumber_freshVar_56.getString() , tomMatch50NameNumber_freshVar_61.getCode() );
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}}{if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddList) ) { tom.engine.adt.tomname.types.TomName  tomMatch50NameNumber_freshVar_66= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ; tom.engine.adt.tominstruction.types.Instruction  tomMatch50NameNumber_freshVar_67= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getInstr() ;if ( (tomMatch50NameNumber_freshVar_66 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch50NameNumber_freshVar_67 instanceof tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction) ) { tom.engine.adt.tomexpression.types.Expression  tomMatch50NameNumber_freshVar_71= tomMatch50NameNumber_freshVar_67.getExpr() ;if ( (tomMatch50NameNumber_freshVar_71 instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {


        try {
          String moduleName = stack.peek();
          tb.getSymbolTable(moduleName).putMakeAddList( tomMatch50NameNumber_freshVar_66.getString() , tomMatch50NameNumber_freshVar_71.getCode() );
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}}{if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyArray) ) { tom.engine.adt.tomname.types.TomName  tomMatch50NameNumber_freshVar_76= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ; tom.engine.adt.tominstruction.types.Instruction  tomMatch50NameNumber_freshVar_77= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getInstr() ;if ( (tomMatch50NameNumber_freshVar_76 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch50NameNumber_freshVar_77 instanceof tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction) ) { tom.engine.adt.tomexpression.types.Expression  tomMatch50NameNumber_freshVar_81= tomMatch50NameNumber_freshVar_77.getExpr() ;if ( (tomMatch50NameNumber_freshVar_81 instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {


        try {
          String moduleName = stack.peek();
          tb.getSymbolTable(moduleName).putMakeEmptyArray( tomMatch50NameNumber_freshVar_76.getString() , tomMatch50NameNumber_freshVar_81.getCode() );
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}}{if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddArray) ) { tom.engine.adt.tomname.types.TomName  tomMatch50NameNumber_freshVar_86= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ; tom.engine.adt.tominstruction.types.Instruction  tomMatch50NameNumber_freshVar_87= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getInstr() ;if ( (tomMatch50NameNumber_freshVar_86 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch50NameNumber_freshVar_87 instanceof tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction) ) { tom.engine.adt.tomexpression.types.Expression  tomMatch50NameNumber_freshVar_91= tomMatch50NameNumber_freshVar_87.getExpr() ;if ( (tomMatch50NameNumber_freshVar_91 instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {


        try {
          String moduleName = stack.peek();
          tb.getSymbolTable(moduleName).putMakeAddArray( tomMatch50NameNumber_freshVar_86.getString() , tomMatch50NameNumber_freshVar_91.getCode() );
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}}{if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetSizeDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch50NameNumber_freshVar_96= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getOpname() ; tom.engine.adt.tomexpression.types.Expression  tomMatch50NameNumber_freshVar_97= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getExpr() ;if ( (tomMatch50NameNumber_freshVar_96 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch50NameNumber_freshVar_97 instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {


        try {
          String moduleName = stack.peek();
          tb.getSymbolTable(moduleName).putGetSizeArray( tomMatch50NameNumber_freshVar_96.getString() , tomMatch50NameNumber_freshVar_97.getCode() );
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }}}}}}return _visit_Declaration(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tomdeclaration.types.Declaration  _visit_Declaration( tom.engine.adt.tomdeclaration.types.Declaration  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomdeclaration.types.Declaration )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public  tom.engine.adt.tomexpression.types.Expression  _visit_Expression( tom.engine.adt.tomexpression.types.Expression  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomexpression.types.Expression )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public  tom.engine.adt.tomtype.types.TomType  _visit_TomType( tom.engine.adt.tomtype.types.TomType  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomtype.types.TomType )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {return ((T)visit_Declaration((( tom.engine.adt.tomdeclaration.types.Declaration )v),introspector));}if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if ( (v instanceof tom.engine.adt.tomexpression.types.Expression) ) {return ((T)visit_Expression((( tom.engine.adt.tomexpression.types.Expression )v),introspector));}if ( (v instanceof tom.engine.adt.tomtype.types.TomType) ) {return ((T)visit_TomType((( tom.engine.adt.tomtype.types.TomType )v),introspector));}if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!((environment ==  null ))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_Collector( tom.library.sl.Strategy  t0,  TomBackend  t1,  Stack<String>  t2) { return new Collector(t0,t1,t2);}



} // class TomBackend

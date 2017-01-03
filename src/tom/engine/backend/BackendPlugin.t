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
public class BackendPlugin extends TomGenericPlugin {

  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../platform/adt/platformoption/PlatformOption.tom }
  %include { ../../library/mapping/java/sl.tom }

  /** the tabulation starting value */
  public final static int defaultDeep = 1;

  /** the declared options string */
  public static final String DECLARED_OPTIONS = 
    "<options>" +
    "<boolean name='noOutput' altName=''  description='Do not generate code' value='false'/>" +
    "<boolean name='aCode'    altName='a'  description='Generate Ada code' value='false'/>" +
    "<boolean name='jCode'    altName='j' description='Generate Java code' value='true'/>" + 
    "<boolean name='csCode'   altName=''  description='Generate C# code' value='false'/>" + 
    "<boolean name='cCode'    altName='c' description='Generate C code' value='false'/>" +
    "<boolean name='camlCode' altName=''  description='Generate Caml code' value='false'/>" + 
    "<boolean name='pCode'    altName=''  description='Generate Python code' value='false'/>" + 
    "<boolean name='inline'   altName=''  description='Inline mapping' value='false'/>" +
    "<boolean name='inlineplus'   altName=''  description='Inline mapping' value='false'/>" +
    "</options>";

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
    return OptionParser.xmlToOptionList(BackendPlugin.DECLARED_OPTIONS);
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

  %typeterm StringStack {
    implement { Stack<String> }
    is_sort(t) { ($t instanceof Stack) }
  }

  %typeterm BackendPlugin {
    implement { BackendPlugin }
    is_sort(t) { ($t instanceof BackendPlugin) }
  }

  private void markUsedConstructorDestructor(Code pilCode) {
    Stack<String> stack = new Stack<String>();
    stack.push(TomBase.DEFAULT_MODULE_NAME);
    try {
      `mu(MuVar("markStrategy"),TopDownCollect(Collector(MuVar("markStrategy"),this,stack))).visitLight(pilCode);
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
  %strategy Collector(markStrategy:Strategy,bp:BackendPlugin,stack:StringStack) extends Identity() {
    visit Instruction {
      CompiledMatch[AutomataInst=inst, Options=optionList] -> {

        String moduleName = TomBase.getModuleName(`optionList);
        /*
         * push the modulename
         * or the wrapping modulename if the current one
         * (nested match for example) does not have one
         */
        if (moduleName==null) {
          try {
            moduleName = stack.peek();
            stack.push(moduleName);
            /*System.out.println("push2: " + moduleName);*/
          } catch (EmptyStackException e) {
            System.out.println("No moduleName in stack");
          }
        } else {
          stack.push(moduleName);
          /*System.out.println("push1: " + moduleName);*/
        }
        /*System.out.println("match -> moduleName = " + moduleName);*/
        markStrategy.visitLight(`inst);
        /*String pop = (String) stack.pop();
          System.out.println("pop: " + pop);*/
        throw new tom.library.sl.VisitFailure();

      }

      RawAction[AstInstruction=inst] -> {
        markStrategy.visitLight(`inst);
        throw new tom.library.sl.VisitFailure();
      }
    }

    visit Expression {
      (IsEmptyList|IsEmptyArray|GetHead|GetTail|GetSize|GetElement)[Opname=Name(name)] -> {
        try {
          /*System.out.println("list check: " + `name);*/
          String moduleName = stack.peek();
          /*System.out.println("moduleName: " + moduleName);*/
          TomSymbol tomSymbol = TomBase.getSymbolFromName(`name,bp.getSymbolTable(moduleName)); 
          bp.setUsedSymbolConstructor(moduleName,tomSymbol,markStrategy);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }
      
      IsFsym[AstName=Name(name)] -> {
        try {
          /*System.out.println("list check: " + `name);*/
          String moduleName = stack.peek();
          /*System.out.println("moduleName: " + moduleName);*/
          TomSymbol tomSymbol = TomBase.getSymbolFromName(`name,bp.getSymbolTable(moduleName)); 
          bp.setUsedSymbolDestructor(moduleName,tomSymbol,markStrategy);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }
    }

    visit TomType {
      Type[TomType=type] -> {
        try {
          String moduleName = stack.peek();
          bp.setUsedType(moduleName,`type,markStrategy);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }
    }

    visit TomTerm {
      /* TermAppl does not exists after Desugarer phase */
      //(TermAppl|RecordAppl)[NameList=nameList] -> {
      RecordAppl[NameList=nameList] -> {
        TomNameList l = `nameList;
        /*System.out.println("dest " + `l);*/
        while(!l.isEmptyconcTomName()) {
          try {
            /*System.out.println("op: " + l.getHead());*/
            String moduleName = stack.peek();
            /*System.out.println("moduleName: " + moduleName);*/
            TomSymbol tomSymbol = TomBase.getSymbolFromName(l.getHeadconcTomName().getString(),bp.getSymbolTable(moduleName)); 
            /*System.out.println("mark: " + tomSymbol);*/
            /*
             * if it comes from java
             */
            if (tomSymbol != null) { bp.setUsedSymbolDestructor(moduleName,tomSymbol,markStrategy);}
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
      }
    }

    visit BQTerm {
      (BuildTerm|BuildEmptyArray)[AstName=Name(name)] -> {
        try {
          /*System.out.println("build: " + `name);*/
          String moduleName = stack.peek();
          /*System.out.println("moduleName: " + moduleName);*/
          TomSymbol tomSymbol = TomBase.getSymbolFromName(`name,bp.getSymbolTable(moduleName)); 
          bp.setUsedSymbolConstructor(moduleName,tomSymbol,markStrategy);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }
      (BuildConsList|BuildEmptyList|BuildAppendList|BuildConsArray|BuildAppendArray)[AstName=Name(name)] -> {
        try {
          /*System.out.println("build: " + `name);*/
          String moduleName = stack.peek();
          /*System.out.println("moduleName: " + moduleName);*/
          TomSymbol tomSymbol = TomBase.getSymbolFromName(`name,bp.getSymbolTable(moduleName)); 
          bp.setUsedSymbolConstructor(moduleName,tomSymbol,markStrategy);
          /* XXX: Also mark the destructors as used, since some generated
           * functions will use them */
          bp.setUsedSymbolDestructor(moduleName,tomSymbol,markStrategy);
          /*
           * resolve uses in the symbol declaration
           */
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }
    }

    visit Declaration {
      /*
       * collect all declarations and add them in the mapInliner
       * this is needed because a %op or %typeterm may be defined
       * after the usage of the sort or the operator
       */
      IsFsymDecl[AstName=Name(opname),Expr=Code(code)] -> {
        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putIsFsym(`opname,`code);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }

/*      ImplementDecl[AstName=Name(opname),Expr=Code(code)] ->{
        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putIsFsym(`opname,`code);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }*/

      IsSortDecl[TermArg=BQVariable[AstType=Type[TomType=type]],Expr=Code(code)] -> {
        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putIsSort(`type,`code);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }

      EqualTermDecl[TermArg1=BQVariable[AstType=Type[TomType=type]],Expr=Code(code)] -> {
        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putEqualTerm(`type,`code);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }

      GetSlotDecl[AstName=Name(opname),SlotName=Name(slotName),Expr=Code(code)] -> {
        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putGetSlot(`opname,`slotName,`code);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }

      GetHeadDecl[Opname=Name(opname),Expr=Code(code)] -> {
        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putGetHead(`opname,`code);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }

      GetTailDecl[Opname=Name(opname),Expr=Code(code)] -> {
        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putGetTail(`opname,`code);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }

      MakeDecl[AstName=Name(opname),Instr=ExpressionToInstruction(Code(code))] -> {
        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putMake(`opname,`code);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }

      IsEmptyDecl[Opname=Name(opname),Expr=Code(code)] -> {
        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putIsEmptyList(`opname,`code);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }

      MakeEmptyList[AstName=Name(opname),Instr=ExpressionToInstruction(Code(code))] -> {
        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putMakeEmptyList(`opname,`code);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }

      MakeAddList[AstName=Name(opname),Instr=ExpressionToInstruction(Code(code))] -> {
        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putMakeAddList(`opname,`code);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }

      MakeEmptyArray[AstName=Name(opname),Instr=ExpressionToInstruction(Code(code))] -> {
        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putMakeEmptyArray(`opname,`code);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }

      MakeAddArray[AstName=Name(opname),Instr=ExpressionToInstruction(Code(code))] -> {
        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putMakeAddArray(`opname,`code);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }

      GetElementDecl[Opname=Name(opname),Expr=Code(code)] -> {
        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putGetElementArray(`opname,`code);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }

      GetSizeDecl[Opname=Name(opname),Expr=Code(code)] -> {
        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putGetSizeArray(`opname,`code);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }

      (SymbolDecl|ListSymbolDecl|ArraySymbolDecl)[AstName=Name(opname)] -> {
        try {
          String moduleName = stack.peek();
          TomSymbol tomSymbol = TomBase.getSymbolFromName(`opname,bp.getSymbolTable(moduleName));
          markStrategy.visitLight(tomSymbol);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }

      /**
        * Resolve declarations
        */
      ResolveGetSlotDecl[AstName=Name(opname),SlotName=Name(slotName)] -> {
        try {
          String moduleName = stack.peek();
          bp.getSymbolTable(moduleName).putResolveGetSlot(`opname,`slotName);
        } catch (EmptyStackException e) {
          System.out.println("No moduleName in stack");
        }
      }

    }
  }
}

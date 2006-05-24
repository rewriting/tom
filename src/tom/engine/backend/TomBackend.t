/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2006, INRIA
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
import tom.engine.adt.tomsignature.types.*;
import tom.engine.tools.OutputCode;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.exception.TomRuntimeException;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;

import tom.library.strategy.mutraveler.MuTraveler;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;


/**
 * The TomBackend "plugin".
 * Has to create the generator depending on OptionManager, create the output 
 * writer and generting the output code.
 */
public class TomBackend extends TomGenericPlugin {
  
  %include { adt/tomsignature/TomSignature.tom }
  %include { adt/platformoption/PlatformOption.tom }
  %include { mutraveler.tom }

  /** the tabulation starting value */
  private final static int defaultDeep = 2;

  /** the declared options string */
  public static final String DECLARED_OPTIONS = 
    "<options>" +
    "<boolean name='noOutput' altName=''  description='Do not generate code' value='false'/>" +
    "<boolean name='jCode'    altName='j' description='Generate Java code' value='true'/>" + 
    "<boolean name='cCode'    altName='c' description='Generate C code' value='false'/>" +
    "<boolean name='camlCode' altName=''  description='Generate Caml code' value='false'/>" + 
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
  public void run() {
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
        } else if(getOptionBooleanValue("jCode")) {
          generator = new TomJavaGenerator(output, getOptionManager(), symbolTable());
        } else {
          throw new TomRuntimeException("no selected language for the Backend");
        }
        //TODO//
        ////////
        String defaultModule = "default";
        ////////
        ////////
				TomTerm pilCode = (TomTerm) getWorkingTerm();

				//markUsedConstructorDestructor(pilCode);

        generator.generate(defaultDeep, generator.operatorsTogenerate(pilCode),defaultModule);
        // verbose
        getLogger().log(Level.INFO, TomMessage.tomGenerationPhase.getMessage(),
                        new Integer((int)(System.currentTimeMillis()-startChrono)));
        writer.close();
      } catch (IOException e) {
        getLogger().log( Level.SEVERE, TomMessage.backendIOException.getMessage(),
                         new Object[]{getStreamManager().getOutputFile().getName(), e.getMessage()} );
        return;
      } catch (Exception e) {
        getLogger().log( Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
                         new Object[]{getStreamManager().getInputFileName(), "TomBackend", e.getMessage()} );
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
  }

  public void optionChanged(String optionName, Object optionValue) {
    //System.out.println("optionChanged: " + optionName + " --> " + optionValue);
    if(optionName.equals("camlCode") && ((Boolean)optionValue).booleanValue() ) { 
      setOptionValue("jCode", Boolean.FALSE);        
      setOptionValue("cCode", Boolean.FALSE);        
    } else if(optionName.equals("cCode") && ((Boolean)optionValue).booleanValue() ) { 
      setOptionValue("jCode", Boolean.FALSE);        
      setOptionValue("camlCode", Boolean.FALSE);        
    } else if(optionName.equals("jCode") && ((Boolean)optionValue).booleanValue() ) { 
      setOptionValue("cCode", Boolean.FALSE);        
      setOptionValue("camlCode", Boolean.FALSE);        
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
  
  /**
   * inherited from plugin interface
   * returns the generated file name
   */
  public Object[] getArgs() {
    return new Object[]{generatedFileName};
  }

	%typeterm Stack {
		implement { Stack }
	}

  %op VisitableVisitor TopDownCollector(s:Stack) {
    make(s) { `mu(MuVar("x"),Try(Sequence(Collector(s),All(MuVar("x"))))) }
  }

	private void markUsedConstructorDestructor(TomTerm pilCode) {
		Stack stack = new Stack();
		try {
			VisitableVisitor v = `TopDownCollector(stack);
			v = MuTraveler.init(v);
			v.visit(pilCode);
		} catch (VisitFailure e) {
      System.out.println("reduction failed on: " + pilCode);
		}
	}

	%strategy Collector(stack:Stack) extends `Identity() {
    visit Instruction {
			CompiledMatch[automataInst=inst, option=optionList] -> {
				String moduleName = getModuleName(`optionList);

				if(moduleName==null && hasGeneratedMatch(`optionList)) {
					try {
						moduleName = (String) stack.peek();
            stack.push(moduleName);
            System.out.println("push2: " + moduleName);
					} catch (EmptyStackException e) {
						System.out.println("No moduleName in stack");
					}
        } else {
          stack.push(moduleName);
          System.out.println("push1: " + moduleName);
        }
				//System.out.println("match -> moduleName = " + moduleName);
				try {
					MuTraveler.init(`TopDownCollector(stack)).visit(`inst);
				} catch (jjtraveler.VisitFailure e) {
					System.out.println("visit failure");
					`Fail().visit(null);
				}
				String pop = (String) stack.pop();
				System.out.println("pop: " + pop);
				`Fail().visit(null);
			}
		}

		visit TomTerm {
			(TermAppl|RecordAppl)[nameList=nameList] -> {
				NameList l = `nameList;
        System.out.println(`l);
				while(!l.isEmpty()) {
					try {
						System.out.println("op: " + l.getHead());
						String moduleName = (String) stack.peek();
						System.out.println("moduleName: " + moduleName);
					} catch (EmptyStackException e) {
						System.out.println("No moduleName in stack");
					}
					l = l.getTail();
				}
			}
			BuildTerm[astName=astName] -> {
				try {
					System.out.println("build: " + `astName);
					String moduleName = (String) stack.peek();
					System.out.println("moduleName: " + moduleName);
				} catch (EmptyStackException e) {
					System.out.println("No moduleName in stack");
				}
			}
		}
		

	}

} // class TomBackend

/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2004 INRIA
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

package jtom.backend;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Level;

import jtom.adt.tomsignature.types.TomTerm;
import jtom.tools.OutputCode;
import jtom.tools.TomGenericPlugin;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;


/**
 * The TomBackend "plugin".
 * Has to create the generator depending on OptionManager, create the output 
 * writer and generting the output code.
 */
public class TomBackend extends TomGenericPlugin {
  
  %include { adt/TomSignature.tom }
  %include { adt/PlatformOption.tom }

  /** the tabulation starting value */
  private final static int defaultDeep = 2;

  /** the declared options string */
  public static final String DECLARED_OPTIONS = 
    "<options>" +
    "<boolean name='noOutput' altName='' description='Do not generate code' value='false'/>" +
    "<boolean name='jCode' altName='j' description='Generate Java code' value='true'/>" + 
    "<boolean name='cCode' altName='c' description='Generate C code' value='false'/>" +
    "<boolean name='camlCode' altName='' description='Generate Caml code' value='false'/>" + 
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
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getStreamManager().getOutputFile())));
        OutputCode output = new OutputCode(writer, getOptionManager());
        if(getOptionBooleanValue("jCode")) {
          generator = new TomJavaGenerator(output, getOptionManager(), symbolTable());
        } else if(getOptionBooleanValue("cCode")) {
          generator = new TomCGenerator(output, getOptionManager(), symbolTable());
        } else if(getOptionBooleanValue("camlCode")) {
          generator = new TomCamlGenerator(output, getOptionManager(), symbolTable());
        }
        generator.generate(defaultDeep, (TomTerm)getWorkingTerm());
        // verbose
        getLogger().log(Level.INFO, "TomGenerationPhase",
                        new Integer((int)(System.currentTimeMillis()-startChrono)));
        writer.close();
      } catch (IOException e) {
        getLogger().log( Level.SEVERE, "BackendIOException",
                         new Object[]{getStreamManager().getOutputFile().getName(), e.getMessage()} );
        return;
      } catch (Exception e) {
        getLogger().log( Level.SEVERE, "ExceptionMessage",
                         new Object[]{getStreamManager().getInputFile().getName(), "TomBackend", e.getMessage()} );
        e.printStackTrace();
        return;
      }
      // set the generated File Name
      generatedFileName = getStreamManager().getOutputFile().getAbsolutePath();
    } else {
      // backend is desactivated
      getLogger().log(Level.INFO,"BackendInactivated");
    }
  }
  
  /**
   * inherited from OptionOwner interface (plugin) 
   */
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TomBackend.DECLARED_OPTIONS);
  }
  
  public void setOption(String optionName, Object optionValue) {
    setOptionValue(optionName, optionValue);
    
    if(optionValue.equals(Boolean.TRUE)) {// no more than 1 type of code can be activated at a time
	    if(optionName.equals("jCode") || optionName.equals("j")) { 
		    //System.out.println("Java code activated, other codes desactivated");
		    setOptionValue("cCode", Boolean.FALSE);
		    setOptionValue("camlCode", Boolean.FALSE); 
      } else if(optionName.equals("cCode") || optionName.equals("c")) { 
		    //System.out.println("C code activated, other codes desactivated");
		    setOptionValue("jCode", Boolean.FALSE);
		    setOptionValue("camlCode", Boolean.FALSE); 
      } else if(optionName.equals("camlCode")) { 
		    //System.out.println("Caml code activated, other codes desactivated");
		    setOptionValue("jCode", Boolean.FALSE);
		    setOptionValue("cCode", Boolean.FALSE);
      }
    }
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

} // class TomBackend

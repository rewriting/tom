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

package jtom.parser;

import jtom.TomEnvironment;
import jtom.adt.tomsignature.types.*;
import jtom.exception.*;
import jtom.tools.*;
import jtom.TomMessage;

public class TomTaskParser extends TomTask {
  TomParser parser;
  String fileName;
  
  public TomTaskParser(String fileName) {
    super("Tom Task Parser");
    this.fileName = fileName;
  }
  
  public void initProcess() {
  }
  
  public void process() {
    try {
      long startChrono = System.currentTimeMillis();
      if(getInput().isJCode()) {
        TomJavaParser javaParser = TomJavaParser.createParser(fileName);
        String packageName = javaParser.JavaPackageDeclaration();
        // Update taskInput
        getInput().setPackagePath(packageName);
        getInput().updateOutputFile();
        //System.out.println("package = '" + getInput().getPackagePath() + "'");
      } else {
        getInput().setPackagePath("");
      }
      this.parser = TomParser.createParser(fileName);
      TomTerm parsedTerm = parser.startParsing();
      
      if(getInput().isVerbose()) {
        System.out.println("TOM parsing phase (" + (System.currentTimeMillis()-startChrono)+ " ms)");
      }
      if(getInput().isEclipseMode()) {
        String fileName = getInput().getOutputFileNameWithoutSuffix() + TomTaskInput.parsedTableSuffix;
        Tools.generateOutput(fileName, symbolTable().toTerm());
      }
      if(getInput().isIntermediate()) {
        Tools.generateOutput(getInput().getOutputFileNameWithoutSuffix() + TomTaskInput.parsedSuffix, parsedTerm);
        Tools.generateOutput(getInput().getOutputFileNameWithoutSuffix() + TomTaskInput.parsedTableSuffix, symbolTable().toTerm());
      }
        
      if(getInput().isDebugMode()) {
        Tools.generateOutput(getInput().getInputFileNameWithoutSuffix() + TomTaskInput.debugTableSuffix, parser.getStructTable());  
      }
        
      // Update environment
      environment().setTerm(parsedTerm);
      
    } catch (TokenMgrError e) {
      TomEnvironment.getInstance().addError(TomMessage.getString("TokenMgrError"), new Object[]{fileName, e.getMessage()}, fileName,  parser.getLine(), TomMessage.TOM_ERROR);
    } catch (TomIncludeException e) {
    	System.out.println("TomIncludeException"+e.getMessage());
      TomEnvironment.getInstance().addError(e.getMessage(), fileName,  parser.getLine(), TomMessage.TOM_ERROR);
    } catch (TomException e) {
      TomEnvironment.getInstance().addError(e.getMessage(), fileName,  parser.getLine(), TomMessage.TOM_ERROR);
    } catch (ParseException e) {
      TomEnvironment.getInstance().addError(TomMessage.getString("ParseException"), new Object[]{fileName, e.getMessage()}, fileName, parser.getLine(), TomMessage.TOM_ERROR);
    } catch (Exception e) {
      e.printStackTrace();
      TomEnvironment.getInstance().addError(TomMessage.getString("UnhandledException"), new Object[]{fileName, e.getMessage()}, fileName, TomMessage.DEFAULT_ERROR_LINE_NUMBER, TomMessage.TOM_ERROR);
    }
  }


}

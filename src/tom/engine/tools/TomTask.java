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
 * Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package jtom.tools;

import java.text.MessageFormat;

import jtom.*;
import jtom.adt.tomsignature.types.*;
import jtom.TomMessage;

public abstract class TomTask extends TomBase {
	
  private TomTask nextTask;
  private String name;
	
  public TomTask() {
  }
  public TomTask(String name) {
    super();
    this.name = name;
  }
	
  public void addTask(TomTask task){
      //System.out.println("Connecting " +name+ " to task"+task.getName());
    this.nextTask = task;
  }
	
  public void startProcess() {
    initProcess();
    process();
    closeProcess();
  }
	
  protected void initProcess() {
  }
	
  protected void process(){
  }
	
  protected void closeProcess() {
    if ( checkNoErrors() ) {
      finishProcess();
    }
  }
	
  public boolean checkNoErrors() {
    return environment().checkNoErrors(name,
    																	 getInput().isEclipseMode(),
																			 getInput().isWarningAll(),
																			 getInput().isNoWarning());
  }
	
  public void finishProcess() {
      // Start next task
    if(nextTask != null) {
      if(!getInput().isEclipseMode()) {
        environment().setErrors(tsf().makeTomErrorList()); // but remove all warning also so possible and usefull only in command line
      } 
      nextTask.startProcess();
    }
  }
	
  public TomTask getTask(){
    return nextTask;
  }
	
  public String getName() {
    return name;
  }
	
  public void messageError(int errorLine, String fileName,String structInfo, int structInfoLine, String msg, Object[] msgArg, int level) {
    String s;
    msg = MessageFormat.format(msg, msgArg);
    if (level == TomMessage.TOM_ERROR) {
      s = MessageFormat.format(TomMessage.getString("MainErrorMessage"), new Object[]{new Integer(errorLine), structInfo, new Integer(structInfoLine), fileName, msg});
    } else {
      s = MessageFormat.format(TomMessage.getString("MainWarningMessage"), new Object[]{new Integer(errorLine), structInfo, new Integer(structInfoLine), fileName, msg});
    }
		
    if (getInput().isEclipseMode()) {
      addError(msg,fileName, errorLine, level);
    } else {
      addError(s,fileName, errorLine, level);
    }
  }
				
  public void addError(String msg, String file, int line, int level) {
    TomError err = tsf().makeTomError_Error(msg,file,line,level);
    environment().setErrors(tsf().makeTomErrorList(err, environment().getErrors()));
  }
  
  public void addError(String msg, Object[] args, String file, int line, int level) {
    TomError err = tsf().makeTomError_Error(MessageFormat.format(msg, args), file, line, level);
    environment().setErrors(tsf().makeTomErrorList(err, environment().getErrors()));
  }
		
}

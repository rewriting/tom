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

package jtom.tools;

import jtom.*;

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
    if(!environment().hasError()) {
      finishProcess();
    } else {
			environment().printAlertMessage(name);
		}
  }
  
  public void finishProcess() {
      // Start next task
    if(nextTask != null) {
      if(!getInput().isEclipseMode()) {
        // remove all warning (in command line only)
        environment().setWarnings(tsf().makeTomAlertList());
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

  public void messageError(int errorLine,
                           String fileName,
                           String structInfo,
                           int line,
                           String msg,
                           Object[] msgArg) {
    environment().messageError(errorLine, fileName, structInfo, line, msg, msgArg);
  }
         
  public void messageWarning(int warningLine,
                             String fileName,
                             String structInfo,
                             int line,
                             String msg,
                             Object[] msgArg) {
    environment().messageWarning(warningLine, fileName, structInfo, line, msg, msgArg);
  }

  public void messageError(String msg, String fileName, int line) {
    environment().messageError(msg,fileName,line);
  }
  
  public void messageWarning(String msg, String fileName, int line) {
    environment().messageWarning(msg,fileName,line);
  }

}

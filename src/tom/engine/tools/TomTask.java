/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2003  LORIA (CNRS, INPL, INRIA, UHP, U-Nancy 2)
			     Nancy, France.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr

*/

package jtom.tools;

import java.text.MessageFormat;

import jtom.*;
import jtom.adt.*;
import jtom.checker.TomCheckerMessage;;

public abstract class TomTask extends TomBase {
	
	private TomTaskInput input;
	private TomTask nextTask;
	private String name;
	
	public TomTask() {
	}
	public TomTask(String name, TomEnvironment tomEnvironment) {
			super(tomEnvironment);
			this.name = name;
		}
	
	public void addTask(TomTask task){
		//System.out.println("Connecting " +name+ " to task"+task.getName());
		this.nextTask = task;
	}
	
	public void startProcess(TomTaskInput input) {
		this.input = input;
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
		boolean res = true; 
		TomErrorList errors = input.getErrors();
		//System.out.println(errors);
		int nbTotalError = errors.getLength();
		int nbWarning = 0, nbError=0;
		if(nbTotalError > 0 ) {
			while(!errors.isEmpty()) {
				TomError error = errors.getHead();
				if (error.getLevel() == 1) {
					nbWarning++;
					if (/*!input.isNoWarning() || */input.isWarningAll() && !input.isEclipseMode()) {
						System.out.println(error.getMessage());
					}
				} else if (error.getLevel() == 0) {
					if(!input.isEclipseMode()){
						System.out.println(error.getMessage());
					}
					res = false;
					nbError++;
				}
				errors= errors.getTail();
			}
			if (nbError>0 && !input.isEclipseMode()) {
				String msg = name+":  Encountered " + nbError + " errors and "+ nbWarning+" warnings.\nNo file generated.";
				System.out.println(msg);
			} else if (nbWarning>0 && !input.isEclipseMode() && !input.isNoWarning()) {
				String msg = name+":  Encountered "+ nbWarning+" warnings.";
				System.out.println(msg);
			}
		}
		return res;
	}
	
	public void finishProcess() {
			//	Start next task
		if(nextTask != null) {
			if(!input.isEclipseMode()) {
				input.setErrors(tsf().makeTomErrorList()); // but remove all warning also so possible and usefull only in command line
			} 
			nextTask.startProcess(input);
		} /*else { System.out.println("No more tasks"); }*/
	}
	
	public TomTaskInput getInput() {
		return input;
	}
	
	public TomTask getTask(){
		return nextTask;
	}
	
	public String getName() {
		return name;
	}
	
	public void messageError(int errorLine, String fileName,String structInfo, int structInfoLine, String msg, Object[] msgArg, int level) {
		msg = MessageFormat.format(msg, msgArg);
		String s = MessageFormat.format(TomCheckerMessage.MainErrorMessage, new Object[]{new Integer(errorLine), structInfo, new Integer(structInfoLine), fileName, msg});
		if (input.isEclipseMode()) {
			addError(msg,fileName, errorLine, level);
		} else {
			addError(s,fileName, errorLine, level);
		}
	}
				
	public void addError(String msg, String file, int line, int level) {
		if(input != null) {
			TomError err = tsf().makeTomError_Error(msg,file,line,level);
			input.setErrors(tsf().makeTomErrorList(err, input.getErrors()));
		}
	}
		
}

/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2003  LORIA (CNRST, INPL, INRIA, UHP, U-Nancy 2)
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
    Julien Guyon

*/

package jtom.debug;
  
import java.util.*;
import java.io.*;

import  jtom.debug.TomDebugStructure;

public class TomDebugEnvironment {
  private TomDebugStructure debugStructure;
  private BufferedReader in;
  private ArrayList subjects;
  private Map substitutions;
  private int step = 0;
  private String lastPatternResult = null;
  private String totalMatchResult = null;
  private boolean failureLookup = false;
  private boolean resultLookup = false;
  private boolean nextLookup = false;

  private static String FAILURE = "Failure";
  private static String SUCCESS = "Success";
  
  public TomDebugEnvironment(TomDebugStructure struct, boolean failureLookup ) {
    this.debugStructure = struct;
    this.failureLookup = failureLookup;
    this.subjects = new ArrayList();
    this.substitutions = new HashMap();
    this.in = new BufferedReader(new InputStreamReader(System.in));
    totalMatchResult = FAILURE;
    if(!failureLookup) {
      System.out.println("\n\tEntering "+struct.type+" declared in "+struct.fileName+" at line "+struct.line);
    }
  }

  public String toString() {
    return debugStructure+debugStructure.type+debugStructure.fileName+debugStructure.line+debugStructure.watchPatternList;
  }
  
  public int leaving() {
    int result = -1;// FAILURE
    if(failureLookup) {
      if(totalMatchResult == SUCCESS) {
        result = 0;
      }
      if(result == -1) {
         System.out.println(debugStructure.type+" declared in "+debugStructure.fileName+" at line "+debugStructure.line+" completely fails with subject(s)");
         showSubjects();
      }
      return result;
    }
    System.out.println("\tLeaving "+debugStructure.type+" declared in "+debugStructure.fileName+" at line "+debugStructure.line);
    try {
      String str = "";
      System.out.print(">:(Press enter to contine....");
      str = in.readLine();
    } catch (IOException e) {
    }

    if(lastPatternResult == SUCCESS) {
      result = 0;
    }
    return result;
  }

  public void enteringPattern() {
    incrementStep();
    nextLookup = false;
    if(failureLookup) {return;}
    if(debugStructure.watchPatternList.contains(new Integer(getStep()))) {      
      System.out.println("\t\tEntering Pattern number "+ getStep()+ " evaluation (declared line "+debugStructure.patternLine[getStep()-1]+" in "+debugStructure.fileName+")");
      lastPatternResult = FAILURE;
      substitutions.clear();
      debugBreak();
    }
  }

  public void leavingPattern() {
    if(failureLookup) {return;}
    if(nextLookup) {return;}
    if(debugStructure.watchPatternList.contains(new Integer(getStep()))) {
      System.out.println("\t\tLeaving  Pattern number "+ getStep()+" evaluation with "+lastPatternResult);
      debugBreak();
    }
  }
  
  public void linearizationFail() {
    if(failureLookup) {return;}
    if(nextLookup) {return;}
    if(debugStructure.watchPatternList.contains(new Integer(getStep()))) {
      lastPatternResult = FAILURE;
      System.out.println("\t\tPattern fails because of linearization issue");
    }
  }
  
  public void patternSuccess() {
    if(failureLookup) {
      totalMatchResult = SUCCESS;
      return;
    }
    if(nextLookup) {return;}
    if(debugStructure.watchPatternList.contains(new Integer(getStep()))) {
      lastPatternResult = SUCCESS;
      System.out.println("\t\tPattern number "+getStep()+" succeeds");
      debugBreak();
    }
  }
  
  public String getKey() {
    return debugStructure.key;
  }

  public void addSubject(String name, Object trm) {
    subjects.add(new Substitution(name, trm));
    if(subjects.size() == debugStructure.nbSubjects.intValue()) {
      System.out.println("Here is(are) the subject(s):");
      showSubjects();
      System.out.println();
        //debugBreak();
    }
  }

  public void showSubjects() {
    Iterator it = subjects.iterator();
    Substitution s;
    while(it.hasNext()) {
      s = (Substitution)it.next();
      System.out.println(s.name+":\t"+s.object);
    }
  }
  
  public void addSubstitution(String name, Object trm) {
    substitutions.put(name, new Substitution(name, trm));
  }
  
  public void showSubsts() {
    Collection c = substitutions.values();
    Iterator it = c.iterator();
    Substitution s;
    while(it.hasNext()) {
      s = (Substitution)it.next();
      System.out.println(s.name+":\t"+s.object);
    }
  }

  public void showPatterns() {
    try {
      System.out.println("Pattern declared line "+debugStructure.patternLine[getStep()-1]);
      System.out.println(debugStructure.patternText[getStep()-1]);
    } catch (Exception e) {
    }
  }
  
  private void debugBreak() {
    if(TomDebugger.testingMode) {
      return;
    }
    try {
      String str = "";
      System.out.print("? for command list>:");
      str = in.readLine();
      processBreak(str);
    } catch (IOException e) {
      System.out.println("Catching exception:"+e.getStackTrace());
    }
  }  

  private void processBreak(String str) {
    if(str.equals("?")) {
      System.out.println("\nFollowing commands are allowed:");
      System.out.println("\t?\t\t:Show this help");
      System.out.println("\tnext      | n\t:Jump to next pattern evaluation");
      System.out.println("\tgetResult | r\t:Jump to pattern evaluation result");
      System.out.println("\tnextFail  | f\t:Jump to pattern evaluation result");
      System.out.println("\tsubject   | s\t:Show the current subject list");
      System.out.println("\tpattern   | p\t:Show the current pattern list");
      System.out.println("\tsubst     | S\t:Show the realized substitution(s)");
    } else if (str.equals("")) {
      return;
    } else if (str.equals("next") || str.equals("n")) {
      nextLookup = true;
      return;
    } else if (str.equals("getResult") || str.equals("r")) {
      resultLookup = true;
      return;
    } else if (str.equals("nextFail") || str.equals("f")) {
      failureLookup = true;
    } else if (str.equals("subject") || str.equals("s")) {
      showSubjects();
    } else if (str.equals("pattern") || str.equals("p")) {
      showPatterns();
    } else if (str.equals("subst") || str.equals("S")) {
      showSubsts();
    } else {
      System.out.println("Unknow command: please enter `?` to list available commands");
    }
    debugBreak();
  }

  private int getStep() {
    return step;
  }

  private void incrementStep() {
    substitutions.clear();
    step++;
  }
  
  class Substitution {
    String name;
    Object object;

    Substitution(String name, Object object) {
      this.name = name;
      this.object = object;
    }
  } 
}

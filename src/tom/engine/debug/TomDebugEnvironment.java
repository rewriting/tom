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

public class TomDebugEnvironment {
  private String key;
  private String type;
  private String fileName = null;
  private Integer line = null;
  private BufferedReader in;
  private ArrayList subjects;
  private ArrayList substitutions;
  private HashSet patternList;
  private String patternText[];
  private int step = 0;
  private String lastPatternResult = null;
  private String totalMatchResult = null;
  private static String FAILURE = "Failure";
  private static String SUCCESS = "Success";
  
  public TomDebugEnvironment(TomDebugStructure struct) {
    this.patternList = struct.watchPatternList;
    this.patternText = struct.patternText;
    this.key = struct.key;
    this.type = struct.type;
    this.fileName = struct.fileName;
    this.line = struct.line;
    
    this.subjects = new ArrayList();
    this.substitutions = new ArrayList();
    this.in = new BufferedReader(new InputStreamReader(System.in));
    totalMatchResult = FAILURE;
    System.out.println("\tEntering "+struct.type+" declared in "+fileName+" at line "+line);

  }

  public void leaving() {
    System.out.println("\tLeaving "+type+" declared in "+fileName+" at line "+line);
    try {
      String str = "";
      System.out.print(">:(Press enter to contine....");
      str = in.readLine();
    } catch (IOException e) {
    }
  }
  
  public String getKey() {
    return key;
  }
  
  private int getStep() {
    return step;
  }

  private void incrementStep() {
    substitutions.clear();
    step++;
  }

  public void addSubject(String name, Object trm) {
    subjects.add(new Substitution(name, trm));
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
    substitutions.add(new Substitution(name, trm));
  }
  
  public void showSubsts() {
    Iterator it = substitutions.iterator();
    Substitution s;
    while(it.hasNext()) {
      s = (Substitution)it.next();
      System.out.println(s.name+":\t"+s.object);
    }
  }

  public void showPatterns() {
    System.out.println(patternText[getStep()-1]);
  }

  public void enteringPattern() {
    incrementStep();
    if(patternList.contains(new Integer(getStep()))) {
      
      System.out.println("\t\tEntering Pattern number "+ getStep()+ " evaluation");
      lastPatternResult = FAILURE;
      substitutions.clear();
      debugBreak();
    }
  }
  
  public void leavingPattern() {
    if(patternList.contains(new Integer(getStep()))) {
      System.out.println("\t\t Leaving Pattern number "+ getStep()+" evaluation with "+lastPatternResult);
      debugBreak();
    }
  }
  
  public void linearizationFail() {
    if(patternList.contains(new Integer(getStep()))) {
      lastPatternResult = FAILURE;
      System.out.println("\t\tPattern fails because of linearization issue");
    }
  }
  
  public void patternSuccess() {
    if(patternList.contains(new Integer(getStep()))) {
      lastPatternResult = SUCCESS;
      System.out.println("\t\tPattern number "+getStep()+" succeeds");
      debugBreak();
    }
  }

    private void debugBreak() {
    try {
      String str = "";
      System.out.print("? to see the available command list>:");
      str = in.readLine();
      processBreak(str);
    } catch (IOException e) {
    }
  }  

  private void processBreak(String str) {
    if(str.equals("?")) {
      System.out.println("\nFollowing commands are allowed:");
      System.out.println("\t?: show this help");
      System.out.println("\tnext: jump to next breakpoint");
      System.out.println("\tsubject: show the current subject list");
      System.out.println("\tpattern: show the current pattern list");
       System.out.println("\tsubst: show the realized substitution(s)");
    } else if (str.equals("n") || str.equals("")) {
      return;
    } else if (str.equals("subject")) {
      showSubjects();
    } else if (str.equals("pattern")) {
      showPatterns();
    } else if (str.equals("subst")) {
      showSubsts();
    } else {
      System.out.println("Unknow command: please enter `?` to know the available commands");
    }
    debugBreak();
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

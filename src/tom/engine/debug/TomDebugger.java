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

*/

package jtom.debug;
  
import java.util.*;
import java.io.*;
import java.lang.Integer;

import aterm.*;
import aterm.pure.*;

import jtom.*;
import jtom.tools.*;
import jtom.adt.*;

public class TomDebugger {
  int matchNumber, patternNumber;
  BufferedReader in;
  public static TomDebugger debug = null;
  HashSet patternSet;
  Map mapMatchPattern;
  ArrayList currentSubject, currentPattern, substitutions;
  boolean echoMode = false;
  boolean subst = true;
  String[] patterns;
  int nbPatterns = 0;
  
  public TomDebugger() {
    matchNumber=0;
    patternNumber=0;
    this.debug = this;
    in = new BufferedReader(new InputStreamReader(System.in));
    mapMatchPattern = new HashMap();
    patternSet = new HashSet();
    patterns = new String[100];
    currentSubject = new ArrayList();
    currentPattern = new ArrayList();
    substitutions = new ArrayList();
    start();
  }
  
  private void start() {
    try {
      String str = "";
      System.out.print(">:");
      str = in.readLine();
      process(str);
    } catch (IOException e) {
    }
  }
  
  private void debugBreak() {
    try {
      String str = "";
      System.out.print(">:");
      str = in.readLine();
      processBreak(str);
    } catch (IOException e) {
    }
  }  

  private void processBreak(String str) {
    if(str.equals("?")) {
      System.out.println("\nFollowing commands are allowed:\n\t?: show this help\n\tnext: jump to next breakpoint\n\tsubject: show the current subject list\n\tpattern: show the current subject list\n\tsubst: show ths substitution done");
    } else if (str.equals("n") || str.equals("")) {
      return;
    } else if (str.equals("subject")) {
      showSubjectList();
    } else if (str.equals("pattern")) {
      showPatternList();
    } else {
      System.out.println("Unknow command: please enter `?` to know the available commands");
    }
    debugBreak();
  }
  
  private void defineMatchWatcher() {
    try {
      String str = "";
      int input = 0;
      while (str != null) {
        System.out.print(">Enter Match Number:");
        str = in.readLine();
        try {
          input = Integer.parseInt(str, 10);
        }catch (NumberFormatException e) {
          System.out.println("Not a valid number");
          continue;
        }
        if (input == 0) {
          System.out.println();
          return;
        } else {
          HashSet list = definePatternWatcher();
          mapMatchPattern.put(new Integer(input), list);
        }
      }
    } catch (IOException e) {
    }
  }
  
  private HashSet definePatternWatcher() {
    HashSet result = new HashSet();
    try {
      String str = "";
      int input = 0;
      while (str != null) {
        System.out.print(">Enter Pattern Number:");
        str = in.readLine();
        try {
          input = Integer.parseInt(str, 10);
        }catch (NumberFormatException e) {
          System.out.println("Not a valid number");
          continue;
        }
        if (input == 0) {
          return result;
        } else {
          result.add(new Integer(input));
        }
      }
    } catch (IOException e) {
    }
    return result;
  }

  private void defineTermWatcher() {
    try {
      String str = "";
      while (str != null) {
        System.out.print(">Enter term Pattern:");
        str = in.readLine();
        if(str.equals("exit"))
          return;
        else {
          patterns[nbPatterns] = str;
          nbPatterns++;
        }
      }
    } catch (IOException e) {
    }
  }
  
  private void process(String str) {
    if(str.equals("?")) {
      System.out.println("\nFollowing commands are allowed:\n\t?:show this help\n\trun: exit debugger configuration and run the program\n\ts: save configuration\n\tl: load configuration from file\n\tm: define breakpoint for Match structure\n\tt: define Term pattern to be watch\n\tsubstOff: do not show substitution");
    } else if (str.equals("run")) {
      System.out.println();
      return;
    } else if (str.equals("s")) {
      System.out.println("Function not yet defined");
    } else if (str.equals("l")) {
      System.out.println("Function not yet defined");
    } else if (str.equals("m")) {
      defineMatchWatcher();
    } else if (str.equals("t")) {
      defineTermWatcher();
    } else if (str.equals("substOff")) {
      subst = false;
    } else {
      System.out.println("Unknow command: please enter `?` to know the available commands");
    }
    start();
    return ;
  }

  public void enteringMatch(int mNumber) {
    Integer key = new Integer(mNumber);
    if(mapMatchPattern.containsKey(key)) {
      currentSubject.clear();
      currentPattern.clear(); // Add automatically or by the hand??
      matchNumber = mNumber;
      patternSet = (HashSet)mapMatchPattern.get(key);
      System.out.println("\tEntering Match number "+ matchNumber);
        //debugBreak();
      echoMode = true;
    } else {echoMode = false;}
  }
  
  public void enteringPattern(int pNumber) {
    if(patternSet.contains(new Integer(pNumber))) {
      substitutions.clear();
      patternNumber = pNumber;
      System.out.println("\t\tEntering Pattern number "+ patternNumber);
      echoMode = true;
      debugBreak();
    } else {echoMode = false;}
  }

  public void patternFail() {
    if (echoMode) {
      System.out.println("\t\tPattern number "+ patternNumber+" fails");
      debugBreak();
    }
  }
  
  public void patternSuccess() {
    if (echoMode) {
      System.out.println("\t\tPattern number "+ patternNumber+" succeeds");
      debugBreak();
    }
  }

  public void termCreation(ATerm trm) {
    java.util.List children;
      //System.out.println("\t\tTested Term: "+trm);
    for(int i = 0; i <nbPatterns; i++ ) {
        //System.out.println("\t\t\tvs pattern: "+patterns[i]);
      children = null;
      children = trm.match(patterns[i]);
      if (children != null) {
        System.out.println("A term corresponding to patterm "+patterns[i]+" has been created");
        debugBreak();
      }
    }
  }

  public void specifySubject(ATerm trm) {
    currentSubject.add(trm);
  }

  public void specifyPattern(ATerm trm) {
    currentPattern.add(trm);
  }

  public void substitution(ATerm trm) {
    substitutions.add(trm);
  }
  
  private void showSubjectList() {
    Iterator it = currentSubject.iterator();
    while(it.hasNext()) {
      System.out.println(it.next());
    }
  }

  private void showPatternList() {
    Iterator it = currentSubject.iterator();
    while(it.hasNext()) {
      System.out.println(it.next());
    }
  }
  
  private void showSubst() {
    Iterator it = substitutions.iterator();
    while(it.hasNext()) {
      System.out.println(it.next());
    }
  }
} //Class TomDebugger

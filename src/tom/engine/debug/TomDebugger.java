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
import java.lang.Integer;

import aterm.*;
import aterm.pure.*;

import jtom.runtime.*;

import jtom.*;
import jtom.tools.*;
import jtom.adt.*;

import jtom.debug.TomDebugEnvironment;

public class TomDebugger {
  BufferedReader in;
  public static TomDebugger debug = null;

  Map mapKeyDebugStructure;
  Set debugStructureKeySet;
  
  boolean subst = true;
  String[] watchPatterns;
  int nbPatterns = 0;

  String baseFileName;

  Stack environment;
  
  public TomDebugger(String structureFileName) {
    TomStructureTable table = null;
    InputStream input = null;
    try {
      input = new FileInputStream(structureFileName+".tfix.debug.table");
      TomSignatureFactory tsf = new TomSignatureFactory(10);
      table = TomStructureTable.fromTextFile(input);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.out.println("Fail to create debugger: File " + structureFileName + ".tfix.debug.table not found.");
      System.exit(1);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Exception during reading "+structureFileName+".tfix.debug.table");
      System.exit(1);
    }

    this.debug = this;
    this.baseFileName = structureFileName;
    this.in = new BufferedReader(new InputStreamReader(System.in));
    
    mapKeyDebugStructure = new HashMap();
    analyseStructure(table);

    debugStructureKeySet = new HashSet();

    watchPatterns = new String[100];
    environment = new Stack();

    start();
  }
  
    //////////////////////////////
    // User Interface procedure //
    //////////////////////////////
  private void start() {
    try {
      String str = "";
      System.out.print(">:");
      str = in.readLine();
      process(str);
    } catch (IOException e) {
    }
  }

    private void process(String str) {
    if(str.equals("?")) {
      System.out.println("\nFollowing commands are allowed:\n\t?:show this help\n\tinfo: Show information on structure that can be debugged\n\trun: exit debugger configuration and run the program\n\ts: save configuration\n\tl: load configuration from file\n\tm: define breakpoint for Match structure\n\tt: define Term pattern to be watch\n\tsubstOff: do not show substitution");
    } else if (str.equals("run")) {
      System.out.println(debugStructureKeySet);
      return;
    } else if (str.equals("info")) {
      TomDebugStructure struct;
      System.out.println("Valid structures to be debugged:");
      Collection structList = mapKeyDebugStructure.values();
      Iterator it = structList.iterator();
      while(it.hasNext()) {
        struct = (TomDebugStructure)it.next();
        System.out.println("\t- "+struct.type+" declared in "+struct.fileName+" at line "+struct.line+" with "+struct.nbPatterns+" patterns");
      }
      System.out.println("\nHere are the corresponding maximum allowed pattern numbers (0 is not a valid entry)");
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

    private void defineMatchWatcher() {
    try {
      String str = "";
      int input = 0;
      while (str != null) {
        System.out.print(">Enter structure line number:");
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
          String key = baseFileName+".t"+input;
          if (mapKeyDebugStructure.keySet().contains(key)) {
            HashSet list = definePatternWatcher(input);
            ((TomDebugStructure)mapKeyDebugStructure.get(key)).watchPatternList = list;
            debugStructureKeySet.add(key);
          } else {
            System.out.println("Not a valid line number, please use info command to ensure valid entry");
          }
        }
      }
    } catch (IOException e) {
    }
  }
  
  private HashSet definePatternWatcher(int line) {
    HashSet result = new HashSet();
    String key = baseFileName+".t"+line;
    TomDebugStructure struct = (TomDebugStructure)mapKeyDebugStructure.get(key);
    int nbPatterns = struct.nbPatterns.intValue();
    try {
      String str = "";
      int input = 0;
      while (str != null) {
        System.out.print(">Enter Pattern Number:");
        str = in.readLine();
        if (str.equals("all")) {
          
          for (int i=1; i<=nbPatterns;i++) {
            result.add(new Integer(i));
          }
          return result;
        }
        else {
          try {
            input = Integer.parseInt(str, 10);
          }catch (NumberFormatException e) {
            System.out.println("Not a valid number");
            continue;
          }
          if (input == 0) {
            return result;
          } else {
            if (input <= nbPatterns) {
              result.add(new Integer(input));
            } else {
              System.out.println("Not a valid pattern number: this structure has only "+nbPatterns+" patterns");
            }
          }
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
          watchPatterns[nbPatterns] = str;
          nbPatterns++;
        }
      }
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
      System.out.println("\nFollowing commands are allowed:\n\t?: show this help\n\tnext: jump to next breakpoint\n\tsubject: show the current subject list\n\tpattern: show the current pattern list\n\tsubst: show the already done substitution(s)");
    } else if (str.equals("n") || str.equals("")) {
      return;
    } else if (str.equals("subject")) {
      showSubjectList();
    } else if (str.equals("pattern")) {
      showPattern();
    } else {
      System.out.println("Unknow command: please enter `?` to know the available commands");
    }
    debugBreak();
  }

    //////////////////////
    // Debug Primitives //
    //////////////////////
  
  public void enteringMatch(String key) {
    if(debugStructureKeySet.contains(key)) {
      environment.push(new TomDebugEnvironment((TomDebugStructure)mapKeyDebugStructure.get(key))); 
    }
  }
  
  public void enteringRule(String key) {
    if(debugStructureKeySet.contains(key)) {
      environment.push(new TomDebugEnvironment((TomDebugStructure)mapKeyDebugStructure.get(key)));
    }
  }
  
  public void leavingMatch(String key) {
    if(!environment.empty() && ((TomDebugEnvironment)(environment.peek())).getKey().equals(key)) {
      environment.pop();
      System.out.println("\tLeaving Match");
    }
  }

  public void leavingRule(String key) {
    if(!environment.empty() && ((TomDebugEnvironment)(environment.peek())).getKey().equals(key)) {
      environment.pop();
      System.out.println("\tLeaving Match");
    }
  }
  
  public void enteringPattern(String key) {
    if(!environment.empty() && ((TomDebugEnvironment)(environment.peek())).getKey().equals(key)) {
      ((TomDebugEnvironment)(environment.peek())).enteringPattern();
    }
  }
  
  public void patternFail(String key) {
    if(!environment.empty() && ((TomDebugEnvironment)(environment.peek())).getKey().equals(key)) {
      ((TomDebugEnvironment)(environment.peek())).patternFail();
    }
  }
  
  public void patternSuccess(String key) {
    if(!environment.empty() && ((TomDebugEnvironment)(environment.peek())).getKey().equals(key)) {
      ((TomDebugEnvironment)(environment.peek())).patternSuccess();
    }
  }

  public void termCreation(Object trm) {
    java.util.List children;
      //System.out.println("\t\tTested Term: "+trm);
    for(int i = 0; i <nbPatterns; i++ ) {
      if (trm instanceof ATerm) {
          //System.out.println("\t\t\tvs pattern: "+patterns[i]);
        children = null;
        children = ((ATerm)trm).match(watchPatterns[i]);
        if (children != null) {
          System.out.println("A term corresponding to patterm "+watchPatterns[i]+" has been created");
          debugBreak();
        }
      } else {
        System.out.println("No able to compare with pattern: Not an ATerm");
      }
    }
  }
  
  public void specifySubject(String key, ATerm trm) {
    if (!environment.empty())
    ((TomDebugEnvironment)environment.peek()).addSubject(trm);
  }

  public void addSubstitution(ATerm trm) {
    if (!environment.empty())
      ((TomDebugEnvironment)environment.peek()).addSubstitution(trm);
  }
  
  private void showSubjectList() {
    if (!environment.empty())
      ((TomDebugEnvironment)environment.peek()).showSubjectList();
  }

  private void showPattern() {
    if (!environment.empty())
      ((TomDebugEnvironment)environment.peek()).showPattern();
  }
  
  private void showSubst() {
    if (!environment.empty())
      ((TomDebugEnvironment)environment.peek()).showSubst();
  }
  
  private void analyseStructure(TomStructureTable trm) {
    TomList list = trm.getStructList();
    while (!list.isEmpty()) {
      TomTerm struct = list.getHead();
      if (struct.isMatch()) {
        String fileName = struct.getOrgTrack().getFileName().getString();
        Integer line = struct.getOrgTrack().getLine();
        String key = fileName+line;
        TomList paList = struct.getPatternList().getList();
        Integer nbPatterns =  evalPatternNumber(paList);
        String[] patternText = new String[nbPatterns.intValue()];
        int i=0;
        while(!paList.isEmpty()) {
          TomTerm pa = paList.getHead();
          patternText[i++] = pa.getOrgText().getString();
          paList = paList.getTail();
        }

        mapKeyDebugStructure.put(key, new TomDebugStructure(key, "Match", fileName, line, nbPatterns, patternText));
      } else if (struct.isRuleSet()) {
        String fileName = struct.getOrgTrack().getFileName().getString();
        Integer line = struct.getOrgTrack().getLine();
        String key = fileName+line;
        TomList paList = struct.getList();
        Integer nbPatterns =  evalPatternNumber(paList);
        String[] patternText = new String[nbPatterns.intValue()];
        int i=0;
        while(!paList.isEmpty()) {
          TomTerm pa = paList.getHead();
          patternText[i++] = pa.getOrgText().getString();
          paList = paList.getTail();
        }
        mapKeyDebugStructure.put(key, new TomDebugStructure(key, "Rule", fileName, line, nbPatterns, patternText));
      } else {
        System.out.println("Corrupt debug term");
        System.exit(1);
      } 
      list = list.getTail();
    }
  }
  
  private Integer evalPatternNumber(TomList list) {
    int nbPattern = 0;
    while(!list.isEmpty()) {
      nbPattern++;
      list = list.getTail();
    }
    return new Integer(nbPattern);
  }
} //Class TomDebugger

class TomDebugStructure {
  String[] patternText;
  HashSet watchPatternList;
  Integer nbPatterns;
  String fileName;
  Integer line;
  String key;
  String type;

  TomDebugStructure(String key, String type, String fileName, Integer line, Integer nbPatterns, String[] patternText){
    this.key = key;
    this.type = type;
    this.fileName = fileName;
    this.line = line;
    this.nbPatterns = nbPatterns;
    this.patternText = patternText;
  }

  public String toString() {
    return type+fileName+line+watchPatternList;
  }
}

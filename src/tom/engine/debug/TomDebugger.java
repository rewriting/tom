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
  Set debuggedStructureKeySet;
  String[] watchPatterns;
  int nbPatterns = 0;
  String[] baseFileName;
  Stack environment;
  boolean nextFailure = false;
  int nextFailureStatus;
  
  public TomDebugger(String[] fileName) {
    this.debug = this;
    this.baseFileName = new String[fileName.length];
    this.in = new BufferedReader(new InputStreamReader(System.in));
    this.debuggedStructureKeySet = new HashSet();
    this.watchPatterns = new String[100];
    this.environment = new Stack();
    this.mapKeyDebugStructure = new HashMap();
    
    TomStructureTable table = null;
    File file = null;
    InputStream input = null;
    String name =null, debugSuffix = ".tfix.debug.table";
    for(int i=0;i<fileName.length;i++) {
      try {
        file = new File(fileName[i]+debugSuffix);
        name = file.getName();
        baseFileName[i] = name.substring(0, name.length() - (debugSuffix.length()))+".t";
        input = new FileInputStream(file);
        TomSignatureFactory tsf = new TomSignatureFactory(10);
        table = TomStructureTable.fromTextFile(input);
        analyseStructure(table);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
        System.out.println("Fail to create debugger: File " + baseFileName[i]+debugSuffix+" not found.");
        System.exit(1);
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Exception during reading "+baseFileName[i]+debugSuffix);
        System.exit(1);
      }
    }
     
    showAvailableStructure();
    start();
  }
  
    //////////////////////////////
    // User Interface procedure //
    //////////////////////////////
  private void start() {
    try {
      String str = "";
      System.out.print("? to see the available command list>:");
      str = in.readLine();
      configure(str);
    } catch (IOException e) {
    }
  }

    private void configure(String str) {
    if(str.equals("?")) {
      System.out.println("\nFollowing commands are allowed:");
      System.out.println("\t ?\t\t:Show this help");
      System.out.println("\t info    | i\t:Show information on structure that can be debugged");
      System.out.println("\t run     | r\t:Exit debugger configuration, jump to next breakpoint");
      System.out.println("\t failure | f\t:Exit debugger configuration and run the program");
      System.out.println("\t save    | s\t:Save configuration");
      System.out.println("\t load    | l\t:Load configuration from file");
      System.out.println("\t breakpt | b\t:Define breakpoint for Match-Rule structures");
      System.out.println("\t clear   | c\t:Clear breakpoint list");
      System.out.println("\t term    | t\t:Define Term pattern to be watch");
      System.out.println("\t more    | m\t:Show patterns of a specific available structure");
    } else if (str.equals("run")) {
      System.out.println(debuggedStructureKeySet);
      return;
    } else if (str.equals("failure") || str.equals("f")) {
      nextFailure = true;
      return;
    } else if (str.equals("info") || str.equals("i")) {
      showAvailableStructure();
    } else if (str.equals("save") || str.equals("s")) {
      System.out.println("Function not yet defined");
    } else if (str.equals("load") || str.equals("l")) {
      System.out.println("Function not yet defined");
    } else if (str.equals("breakpt") || str.equals("b")) {
      defineStructWatcher();
    } else if (str.equals("clear") || str.equals("c")) {
      debuggedStructureKeySet.clear();
    } else if (str.equals("term") || str.equals("t")) {
      defineTermWatcher();
    } else if (str.equals("more") || str.equals("m")) {
      showStructureDetails();
    } else {
      System.out.println("Unknow command: please enter `?` to know the available commands");
    }
    start();
    return ;
  }

  private void showAvailableStructure() {
    TomDebugStructure struct;
    System.out.println("Valid structures to be debugged and their corresponding maximum allowed pattern numbers:");
    Collection structList = mapKeyDebugStructure.values();
    Iterator it = structList.iterator();
    while(it.hasNext()) {
      struct = (TomDebugStructure)it.next();
      System.out.println("\t- "+struct.type+" declared in "+struct.fileName+" at line "+struct.line+" with "+struct.nbPatterns+" patterns");
    }
  }
  
  private void showStructureDetails() {
     try {
      String str = "";
      int input = 0;
      String fileName = getFileName();
      if (fileName == null) {
        return;
      }
      while (str != null) {
        System.out.print(">Enter structure line number (0 to exit):");
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
          String key = fileName+input;
          if (mapKeyDebugStructure.keySet().contains(key)) {
            showPatterns(key);
          } else {
            System.out.println("Not a valid line number, please use first `info` command to ensure valid entry");
          }
        }
      }
    } catch (IOException e) {
    }
  }
  private String getFileName() {
    String str = "";
    int input = 0;
    if(baseFileName.length == 1) {
      return baseFileName[0];
    }
    try {
      while (true) {
        System.out.println(">Choose base original file name:(0 to exit)");
        for(int i=0;i<baseFileName.length;i++) {
          System.out.println((i+1)+")\t"+baseFileName[i]);
        }
        str = in.readLine();
        try {
          input = Integer.parseInt(str, 10);
        }catch (NumberFormatException e) {
          System.out.println("Not a valid number");
          continue;
        }
        if(input == 0) {
          return null;
        } else if ((input-1) >= baseFileName.length) {
          System.out.println("Not a valid number");
          continue;
        } else {
          return baseFileName[input-1];
        }
      }
    } catch (IOException e) {
      return null;
    }
  }
  
  private void showPatterns(String key) {
    TomDebugStructure struct = (TomDebugStructure)mapKeyDebugStructure.get(key);
    for(int i=0;i<struct.nbPatterns.intValue();i++){
      System.out.println("Pattern at line: "+struct.patternLine[i]);
      System.out.println(struct.patternText[i]);
      System.out.println("--------------------------------------------------------------------------------");
    }
  }
  
  private void defineStructWatcher() {
    try {
      String str = "";
      int input = 0;
      String fileName = getFileName();
      if (fileName == null) {
        return;
      }
      while (str != null) {
        System.out.print(">Enter structure line number (0 to exit):");
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
          String key = fileName+input;
          if (mapKeyDebugStructure.keySet().contains(key)) {
            HashSet list = definePatternWatcher(input, fileName);
            ((TomDebugStructure)mapKeyDebugStructure.get(key)).watchPatternList = list;
            debuggedStructureKeySet.add(key);
          } else {
            System.out.println("Not a valid line number, please use info command to ensure valid entry");
          }
        }
      }
    } catch (IOException e) {
    }
  }
  
  private HashSet definePatternWatcher(int line, String fileName) {
    HashSet result = new HashSet();
    String key = fileName+line;
    TomDebugStructure struct = (TomDebugStructure)mapKeyDebugStructure.get(key);
    int nbPatterns = struct.nbPatterns.intValue();
    try {
      String str = "";
      int input = 0;
      while (str != null) {
        System.out.print(">Enter Pattern Number (0 to exit, `all` to have them all):");
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
      System.out.print("? to see the available command list>:");
      str = in.readLine();
      processBreak(str);
    } catch (IOException e) {
    }
  }  

  private void processBreak(String str) {
    if(str.equals("?")) {
      System.out.println("\nFollowing commands are allowed:");
      System.out.println("\t ?: show this help");
      System.out.println("\t next: jump to next breakpoint");
      System.out.println("\t subject: show the current subject list");
      System.out.println("\t pattern: show the current pattern list");
      System.out.println("\t subst: show the already done substitution(s)");
    } else if (str.equals("next")) {
      System.out.println("Function not yet defined");
    } else if (str.equals("subst")) {
      System.out.println("Function not yet defined");
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

  private boolean evalCondition(String key) {
    return (debuggedStructureKeySet.contains(key) || nextFailure);
  }
  
  public void enteringStructure(String key) {
    if(evalCondition(key) == true) {
      environment.push(new TomDebugEnvironment((TomDebugStructure)mapKeyDebugStructure.get(key), nextFailure)); 
    }
  }
  
  public void leavingStructure(String key) {
    if(!environment.empty() && ((TomDebugEnvironment)(environment.peek())).getKey().equals(key)) {
      int status = ((TomDebugEnvironment)(environment.peek())).leaving();
      environment.pop();
      if (status == -1) {
        nextFailure = false;
        start();
      }
    }
  }
  
  public void enteringPattern(String key) {
    if(!environment.empty() && ((TomDebugEnvironment)(environment.peek())).getKey().equals(key)) {
      ((TomDebugEnvironment)(environment.peek())).enteringPattern();
    }
  }
  
  public void leavingPattern(String key) {
    if(!environment.empty() && ((TomDebugEnvironment)(environment.peek())).getKey().equals(key)) {
      ((TomDebugEnvironment)(environment.peek())).leavingPattern();
    }
  }
  
  public void linearizationFail(String key) {
    if(!environment.empty() && ((TomDebugEnvironment)(environment.peek())).getKey().equals(key)) {
      ((TomDebugEnvironment)(environment.peek())).linearizationFail();
    }
  }
  
  public void patternSuccess(String key) {
    if(!environment.empty() && ((TomDebugEnvironment)(environment.peek())).getKey().equals(key)) {
      ((TomDebugEnvironment)(environment.peek())).patternSuccess();
    }
  }

  public void termCreation(Object trm) {
    java.util.List children;
    for(int i = 0; i <nbPatterns; i++ ) {
      if (trm instanceof ATerm) {
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
  
  public void specifySubject(String key, String name, ATerm trm) {
    if (!environment.empty())
    ((TomDebugEnvironment)environment.peek()).addSubject(name, trm);
  }

  public void addSubstitution(String key, String name, ATerm trm) {
    if (!environment.empty())
      ((TomDebugEnvironment)environment.peek()).addSubstitution(name, trm);
  }
  
  private void showSubjectList() {
    if (!environment.empty())
      ((TomDebugEnvironment)environment.peek()).showSubjects();
  }

  private void showPattern() {
    if (!environment.empty())
      ((TomDebugEnvironment)environment.peek()).showPatterns();
  }
  
  private void showSubst() {
    if (!environment.empty())
      ((TomDebugEnvironment)environment.peek()).showSubsts();
  }
  
  private void analyseStructure(TomStructureTable trm) {
    TomList list = trm.getStructList();
    while (!list.isEmpty()) {
      TomTerm struct = list.getHead();
      if (struct.isMatch()) {
          // The only present option in the list is the orgTack
        Option orgTrack = struct.getOption().getOptionList().getHead();
        String fileName = orgTrack.getFileName().getString();
        Integer line = orgTrack.getLine();
        String key = fileName+line;
        TomList paList = struct.getPatternList().getList();
        Integer nbPatterns =  evalPatternNumber(paList);
        String[] patternText = new String[nbPatterns.intValue()];
        Integer[] patternLine = new Integer[nbPatterns.intValue()];
        int i=0;
        TomTerm pa;
        OptionList opList;
        while(!paList.isEmpty()) {
          pa = paList.getHead();
          opList = pa.getOption().getOptionList();
          patternText[i] = extractPatternText(opList);
          patternLine[i++] = extractPatternLine(opList);
          paList = paList.getTail();
        }
        mapKeyDebugStructure.put(key, new TomDebugStructure(key, "Match", fileName, line, nbPatterns, patternText, patternLine));
        
      } else if (struct.isRuleSet()) {
        String fileName = struct.getOrgTrack().getFileName().getString();
        Integer line = struct.getOrgTrack().getLine();
        String key = fileName+line;
        TomList paList = struct.getList();
        Integer nbPatterns =  evalPatternNumber(paList);
        String[] patternText = new String[nbPatterns.intValue()];
        Integer[] patternLine = new Integer[nbPatterns.intValue()];
        int i=0;
        TomTerm pa;
        OptionList opList;
        while(!paList.isEmpty()) {
          pa = paList.getHead();
          opList = pa.getOption().getOptionList();
          patternText[i] = extractPatternText(opList);
          patternLine[i++] = extractPatternLine(opList);
          paList = paList.getTail();
        }
        mapKeyDebugStructure.put(key, new TomDebugStructure(key, "Rule", fileName, line, nbPatterns, patternText, patternLine));
        
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
  
  private Integer extractPatternLine(OptionList list) {
    Option op;
    while(!list.isEmptyOptionList()) {
      op = list.getHead();
      if(op.isOriginTracking()) {
        return op.getLine();
      }
      list = list.getTail();
    }
    return null;
  }

  private String extractPatternText(OptionList list) {
    Option op;
    while(!list.isEmptyOptionList()) {
      op = list.getHead();
      if(op.isTomNameToOption()) {
        return op.getAstName().getString();
      }
      list = list.getTail();
    }
    return null;
  }
  
} //Class TomDebugger

class TomDebugStructure {
  String[] patternText;
  HashSet watchPatternList;
  Integer nbPatterns;
  Integer[] patternLine;
  String fileName;
  Integer line;
  String key;
  String type;

  TomDebugStructure(String key, String type, String fileName, Integer line, Integer nbPatterns, String[] patternText, Integer[] patternLine){
    this.key = key;
    this.type = type;
    this.fileName = fileName;
    this.line = line;
    this.nbPatterns = nbPatterns;
    this.patternText = patternText;
    this.patternLine = patternLine;
  }

  public String toString() {
    return type+fileName+line+watchPatternList;
  }
} // Class TomDebugStructure

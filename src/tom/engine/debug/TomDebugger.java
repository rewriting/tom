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

import jtom.exception.TomRuntimeException;
  
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;

import jtom.adt.Option;
import jtom.adt.OptionList;
import jtom.adt.TomList;
import jtom.adt.TomSignatureFactory;
import jtom.adt.TomStructureTable;
import jtom.adt.TomTerm;

import aterm.pure.PureFactory;

public class TomDebugger {
  public static TomDebugger debugger = null;
  static boolean testingMode = false;
  static String debugFileExtension = ".jdbg";
  static String debugTableSuffix = ".tfix.debug.table";

  BufferedReader in;
  Map mapKeyDebugStructure;
  Set debuggedStructureKeySet;
  String[] baseFileName;
  Stack environment;
  boolean nextFailure = false;
  ArrayList termHandlerList;
  ArrayList activeTermHandlerList;
  
  public TomDebugger(String[] fileName) {
    this(fileName, false);
  }

  public TomDebugger(String fileName) {
    this(new String[]{fileName}, false);
  }
  
  public TomDebugger(String[] fileName, boolean testingModeArg) {
    testingMode = testingModeArg;
    debugger = this;
    this.baseFileName = new String[fileName.length];
    this.in = new BufferedReader(new InputStreamReader(System.in));
    this.debuggedStructureKeySet = new HashSet();
    this.environment = new Stack();
    this.mapKeyDebugStructure = new HashMap();
    this.termHandlerList = new ArrayList();
    this.activeTermHandlerList = new ArrayList();
    TomStructureTable table = null;
    File file = null;
    InputStream input = null;
    String name =null;
    for(int i=0;i<fileName.length;i++) {
      try {
        file = new File(fileName[i]+debugTableSuffix);
        name = file.getName();
        baseFileName[i] = name.substring(0, name.length() - (debugTableSuffix.length()))+".t";
        input = new FileInputStream(file);
        TomSignatureFactory tsf = new TomSignatureFactory(new PureFactory(10));
        table = tsf.TomStructureTableFromFile(input);
        System.out.println("Analysing "+baseFileName[i]+"...");
        analyseStructure(table);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
        System.out.println("Fail to create debugger: File " + baseFileName[i]+debugTableSuffix+" not found.");
		    throw new TomRuntimeException(new Throwable("Fail to create debugger: File " + baseFileName[i]+debugTableSuffix+" not found."));
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Exception during reading "+baseFileName[i]+debugTableSuffix);
				try {
  				input.close();
				} catch(IOException e2) {
  				throw new RuntimeException("in.close() failed");
				}
      }
    }
  }

  public void start() {
    System.out.println("Starting debugger!!!!\n");
    showAvailableStructure();
    showMainMenu();
  }
  
    //////////////////////////////
    // User Interface procedure //
    //////////////////////////////
  private void showMainMenu() {
    try {
      String str = "";
      System.out.print("? for command list>:");
      str = in.readLine();
      configure(str);
    } catch (IOException e) {
    }
  }
  
  private void configure(String str) {
    if(str.equals("?")) {
      System.out.println("\nFollowing commands are allowed:");
      System.out.println("\t ?\t\t:Show this help");
      System.out.println("\t save    | s\t:Save configuration");
      System.out.println("\t load    | L\t:Load configuration from file");
      System.out.println("\t info    | i\t:Show information on structure that can be debugged");
      System.out.println("\t more    | m\t:Show patterns of a specific available structure");
      System.out.println("\t breakpt | b\t:Define breakpoint for Match-Rule structures");
      System.out.println("\t handler | h\t:Show activable Term pattern handler");
      System.out.println("\t term    | t\t:Define Term pattern handler to be activated");
      System.out.println("\t listb   | lb\t:List defined breakpoint");
      System.out.println("\t listh   | lh\t:List defined breakpoint");
      System.out.println("\t clear   | c\t:Clear breakpoint and active term handler lists");
      System.out.println("\t removeb | RB\t:Remove a defined breakpoint");
      System.out.println("\t removeh | RH\t:Remove an activated Handler");
      System.out.println("\t run     | r\t:Exit debugger configuration, jump to next defined breakpoint");
      System.out.println("\t failure | f\t:Exit debugger configuration, jump to next Match failure");
      System.out.println("\t stack   | S\t:Show Environment Stack");
      System.out.println("\t cl\t\t:Clear screen (unix system)");
    } else if (str.equals("run")     || str.equals("r")) {
        //System.out.println(debuggedStructureKeySet);
      return;
    } else if (str.equals("failure") || str.equals("f")) {
      nextFailure = true;
      return;
    } else if (str.equals("info")    || str.equals("i")) {
      showAvailableStructure();
    } else if (str.equals("save")    || str.equals("s")) {
      saveConfigurationToFile();
    } else if (str.equals("load")    || str.equals("L")) {
      loadConfigurationFromFile();
    } else if (str.equals("breakpt") || str.equals("b")) {
      defineBreakpoint();
    } else if (str.equals("handler") || str.equals("h")) {
      showHandlers();
    } else if (str.equals("clear")   || str.equals("c")) {
      System.out.println("...Clearing all defined breakpoints and terms handlers\n");
      debuggedStructureKeySet.clear();
      activeTermHandlerList.clear();
    } else if (str.equals("term")    || str.equals("t")) {
      defineActineTermHandler();
    } else if (str.equals("more")    || str.equals("m")) {
      showStructureDetails();
    } else if (str.equals("listb")    || str.equals("lb")) {
      showBreakpoint();
    } else if (str.equals("listh")    || str.equals("lh")) {
      showActiveHandlers();
    } else if (str.equals("removeb")  || str.equals("RB")) {
      removeBreakpoint();
    } else if (str.equals("removeh")  || str.equals("RH")) {
      removeActiveHandler();
    } else if (str.equals("stack")   || str.equals("S")) {
      showStack();
    } else if (str.equals("cl")) {
      clscrn();
    } else {
      System.out.println("Unknow command: please enter `?` to list available commands");
    }
    showMainMenu();
    return;
  }
  
  private void showMenu2() {
    if(TomDebugger.testingMode) {
      return;
    }
    try {
      String str = "";
      System.out.print("? for command list>:");
      str = in.readLine();
      processMenu2(str);
    } catch (IOException e) {
      System.out.println("Catching exception:"+e.getStackTrace());
    }
  }  
  
  private void processMenu2(String str) {
    if(str.equals("?")) {
      System.out.println("\nFollowing commands are allowed:");
      System.out.println("\t?\t\t:Show this help");
      System.out.println("\tsubject   | s\t:Show the current subject list");
      System.out.println("\tpattern   | p\t:Show the current pattern list");
      System.out.println("\tsubst     | S\t:Show the realized substitution(s)");
    } else if (str.equals("")) {
      return;
    }  else if (str.equals("subject") || str.equals("s")) {
      showSubjects();
    } else if (str.equals("pattern") || str.equals("p")) {
      showPatterns();
    } else if (str.equals("subst") || str.equals("S")) {
      showSubsts();
    } else {
      System.out.println("Unknow command: please enter `?` to list available commands");
    }
    showMenu2();
  }

  private void clscrn(){
    for (int i=0; i<100; i++) {
      System.out.println("");
    }
  }
  
  private void saveConfigurationToFile() {
    String str ="";
    try {
      while(true) {
        System.out.print("Enter file name ("+debugFileExtension+"):");
        str = in.readLine();
        if (str.equals("")) {
          continue;
        }
        if(str.indexOf(debugFileExtension) == -1) {
          str += debugFileExtension;
        }
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(str)));
        TomDebugStructure struct;
        Collection structList = mapKeyDebugStructure.values();
        Iterator it = structList.iterator();
        while(it.hasNext()) {
          struct = (TomDebugStructure)it.next();
          if (struct.watchPatternList != null) {
            out.print(struct.key);
            Integer[] tab = (Integer[])((struct.watchPatternList).toArray(new Integer[]{}));
            Arrays.sort(tab);
            for(int i=0;i<tab.length;i++) {
              out.print("\t"+tab[i]);
            }
            out.println();
          }
        }
        out.close();
        break;
      }
    } catch (IOException e) {
    }  
  }

  private void loadConfigurationFromFile() {
    System.out.println("Choose configuration file name ("+debugFileExtension+") in:");
    try {
      File path = new File(".");
      String[] list = path.list(filter(debugFileExtension));
      Arrays.sort(list, new AlphabeticComparator());
      for(int i=0;i<list.length;i++) {
        System.out.println(list[i]);
      }
      System.out.print(">:");
      while(true) {
        String str = in.readLine();
        if(str.indexOf(debugFileExtension) == -1) {
          str += debugFileExtension;
        }
        File file = new File(str);
        if(! file.exists()) {
          System.out.println("Not a valid filename: "+str);
          System.out.print("Configuration file name ("+debugFileExtension+"):");
          continue;
        }
        BufferedReader fileIn = new BufferedReader(new FileReader(str));
        String line, token = new String();
        StringTokenizer st;
        TomDebugStructure struct;
        while( (line = fileIn.readLine()) != null) {
          HashSet set = new HashSet();
          st = new StringTokenizer(line);
          token = st.nextToken();
          System.out.println(token);
          debuggedStructureKeySet.add(token);
          struct = (TomDebugStructure)mapKeyDebugStructure.get(token);
          while(st.hasMoreTokens()) {
            token = st.nextToken();
            System.out.println(token);
            set.add( new Integer(Integer.parseInt(token, 10)));
          }
          struct.watchPatternList = set;
        }
        fileIn.close();
        break;
      }
    } catch (IOException e) {
    }  
  }
  
  private static FilenameFilter filter(final String afn) {
     // Creation de la classe anonyme interne :
    return new FilenameFilter() {
        String fn = afn;
        public boolean accept(File dir, String n) {
            // Strip path information:
          String f = new File(n).getName();
          return f.indexOf(fn) != -1;
        }
      }; // Fin de la classe anonyme interne.
  }
  
/*
  class DirFilter implements FilenameFilter {
  String afn;
  DirFilter(String afn) { this.afn = afn; }
  public boolean accept(File dir, String name) {
    // Information du chemin de repertoire :
    String f = new File(name).getName();
    return f.indexOf(afn) != -1;
    }
    }
*/
  class AlphabeticComparator implements Comparator{
    public int compare(Object o1, Object o2) {
      String s1 = (String)o1;
      String s2 = (String)o2;
      return s1.toLowerCase().compareTo(
        s2.toLowerCase());
    }
  }

  public void showStack() {
    if(environment.isEmpty()) {
      System.out.println("Empty environment");
      return;
    }
    int max =  (10 > environment.size()) ? environment.size() : 10;
    for(int i=0;i<max;i++) {
      System.out.println(i+": "+environment.elementAt(i));
    }
  }
  
  private void showAvailableStructure() {
    TomDebugStructure struct;
    Collection structList = mapKeyDebugStructure.values();
    Iterator it = structList.iterator();
    if (it.hasNext()) {
      System.out.println("Valid structures to be debugged and their corresponding maximum allowed pattern numbers:");
    } else {
      System.out.println("No structure to debug");
      return;
    }
    while(it.hasNext()) {
      struct = (TomDebugStructure)it.next();
      System.out.println("\t- `"+struct.type+"` declared in `"+struct.fileName+"` at line "+struct.line+" with "+struct.nbPatterns+" patterns");
    }
  }
    
  private void showStructureDetails() {
     try {
      String str = "";
      int input = 0;
      String fileName = getFileName("");
      if (fileName == null) {
        return;
      }
      while (str != null) {
        System.out.print("\t>Enter structure line number in "+fileName+" (0:exit):");
        str = in.readLine();
        try {
          input = Integer.parseInt(str, 10);
        }catch (NumberFormatException e) {
          System.out.println("Not a number");
          continue;
        }
        if (input == 0) {
          System.out.println();
          return;
        } else {
          String key = fileName+input;
          if (mapKeyDebugStructure.keySet().contains(key)) {
            clscrn();
            System.out.println("Details of structure declared line "+input+" in file "+fileName+"\n");
            showPatterns(key);
          } else {
            System.out.println("Not a valid line number, please use first `info` command to ensure valid entry");
          }
        }
      }
    } catch (IOException e) {
    }
  }

  private void defineBreakpoint() {
    System.out.println("\nA breakpoint is defined by: the file name, the structure line number and the pattern number (optional)");
    try {
      String str = "";
      int input = 0;
        // getting a file Name
      String fileName = getFileName("All files means defining all possible breakpoints");
      if (fileName == null) {
        return;
      }
      if (str.equals("AllFiles")) {
          // All files means all possible breakpoints
        System.out.println("...Defining breakpoints for all couple of Structure/Pattern in all files ");
        Collection structList = mapKeyDebugStructure.values();
        TomDebugStructure struct;
        Iterator it = structList.iterator();
        while(it.hasNext()) {
          struct = (TomDebugStructure)it.next();
          HashSet set = new HashSet();
          for (int i=1; i<=struct.nbPatterns;i++) {
            set.add(new Integer(i));
          }
          struct.watchPatternList = set;
          debuggedStructureKeySet.add(struct.key);
        }
        return;
      }
        // we get a file name
      while (str != null) {
        System.out.print("\n\t>Enter a structure line number in "+fileName+" (0:exit, `all`:all):");
        str = in.readLine();
        if (str.equals("all")) {
          System.out.println("... Adding all possible breakpoints for structures in file "+fileName+"\n");
          Collection structList = mapKeyDebugStructure.values();
          TomDebugStructure struct;
          Iterator it = structList.iterator();
          while(it.hasNext()) {
            struct = (TomDebugStructure)it.next();
            if(!(struct.key).startsWith(fileName)) {
              continue;
            }
            HashSet set = new HashSet();
            for (int i=1; i<=struct.nbPatterns;i++) {
              set.add(new Integer(i));
            }
            struct.watchPatternList = set;
            debuggedStructureKeySet.add(struct.key);
          }
          return;
        }
        try {
          input = Integer.parseInt(str, 10);
        }catch (NumberFormatException e) {
          System.out.println("Not a number");
          continue;
        }
        if (input == 0) {
          System.out.println();
          return;
        } else {
          String key = fileName+input;
          if (mapKeyDebugStructure.keySet().contains(key)) {
            HashSet set = definePatternWatcher(input, fileName);
            ((TomDebugStructure)mapKeyDebugStructure.get(key)).watchPatternList = set;
            debuggedStructureKeySet.add(key);
          } else {
            System.out.println("Not a valid line number, please exit and use `info` command to see valid entry");
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
    int nbPatterns = struct.nbPatterns;
    try {
      String str = "";
      int input = 0;
      while (str != null) {
        System.out.print("\t\t>Complete breakpoint with pattern number (0:exit, `all`:all):");
        str = in.readLine();
        if (str.equals("all")) {
          System.out.println("... Adding breakpoints for all patterns of structure line "+line+" in file "+fileName);
          for (int i=1; i<=nbPatterns;i++) {
            result.add(new Integer(i));
          }
          return result;
        }
        else {
          try {
            input = Integer.parseInt(str, 10);
          }catch (NumberFormatException e) {
            System.out.println("Not a number");
            continue;
          }
          if (input == 0) {
            return result;
          } else {
            if ( input <= nbPatterns && input > 0) {
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
  
  private void showBreakpoint() {
    TomDebugStructure struct;
    if(debuggedStructureKeySet.isEmpty()) {
      System.out.println("No breakpoints");
      return;
    } else {
      clscrn();
      System.out.println("\nList of yet defined breakpoint(s):\n");
      Iterator it = debuggedStructureKeySet.iterator();
      while(it.hasNext()) {
        struct = (TomDebugStructure)mapKeyDebugStructure.get((String)it.next());
        Integer[] tab = (Integer[])((struct.watchPatternList).toArray(new Integer[]{}));
        Arrays.sort(tab);
        System.out.println("- "+struct.type+" declared in "+struct.fileName+" at line "+struct.line+" with "+struct.nbPatterns+" patterns");
        if(tab.length == 0) {
          System.out.println("\t- No patterns are used for this breakpoint");
        }
        for(int i=0;i<tab.length;i++) {
          System.out.println("\t-Pattern "+tab[i]+" at line "+struct.patternLine[tab[i].intValue()-1]+":");
          System.out.println("\t=>\t "+struct.patternText[tab[i].intValue()-1]);
        }
        System.out.println();
      }
    }
  }
  
  private void removeBreakpoint() {
    try {
      String str = "", str2 = "";
      int input = 0;
      String fileName = getFileName("All files means removing all breakpoints");
      if (fileName == null) {
        return;
      }
      if (str.equals("AllFiles")) {
        System.out.println("...Clearing all breakpoints in all files");
        debuggedStructureKeySet.clear();
        return;
      }        
      while (str != null) {
        System.out.print("\t>Enter structure line number (0:exit, `all`:all):");
        str = in.readLine();
        if (str.equals("all")) {
          System.out.println("...Clearing all breakpoints in file "+fileName+"\n");
          TomDebugStructure struct;
          String key;
          Iterator it = debuggedStructureKeySet.iterator();
          ArrayList listOfKey = new ArrayList();
          while(it.hasNext()) {
            key = (String)it.next();
            if(key.startsWith(fileName)) {
              listOfKey.add(key);
                //struct = (TomDebugStructure)mapKeyDebugStructure.get(key);
                //struct.watchPatternList = null;
            }
          }
          for(int i=0;i<listOfKey.size();i++) {
            debuggedStructureKeySet.remove(listOfKey.get(i));
          }
          return;
        }
        try {
          input = Integer.parseInt(str, 10);
        }catch (NumberFormatException e) {
          System.out.println("Not a number");
          continue;
        }
        if (input == 0) {
          System.out.println();
          return;
        } else {
          String key = fileName+input;
          
          if (mapKeyDebugStructure.keySet().contains(key)) {
            TomDebugStructure struct = (TomDebugStructure)mapKeyDebugStructure.get(key);
              // obtain pattern number
            if(struct.watchPatternList.isEmpty()) {
              System.out.println("Removing breakpoint on structure (No pattern were associated to this breakpoint)\n");
              debuggedStructureKeySet.remove(key);
              continue;
            }
            while (str2 != null) {
              System.out.print("\t>Pattern  number (0:exit, `all`:all):");
              str2 = in.readLine();
              if(str2.equals("all")) {
                struct.watchPatternList = null;
                debuggedStructureKeySet.remove(key);
              }
              try {
                input = Integer.parseInt(str2, 10);
              }catch (NumberFormatException e) {
                System.out.println("Not a number");
                continue;
              }
              if (input == 0) {
                System.out.println();
                break;
              } else {
                if (true == struct.watchPatternList.remove(new Integer(input))) {
                  if (struct.watchPatternList.isEmpty()) {
                    struct.watchPatternList = null;
                    debuggedStructureKeySet.remove(key);
                  }
                } else {
                   System.out.println("No breakpoint on pattern number "+input);
                }
              }
            }
          } else {
            System.out.println("Not a valid line number, please use first `list` command to see breakpoint list");
          }
        }
      }
     } catch (IOException e) {
     }
  }
  
  private String getFileName(String message) {
    if(baseFileName.length == 1) {
      return baseFileName[0];
    }
    String str = "";
    int input = 0;
    boolean emptyMessage = message.equals("");
      
      try {
        while (true) {
        System.out.println(">Choose base original file name to be used:(0:exit)");
        int i = 0;
        for(;i<baseFileName.length;i++) {
          System.out.println((i+1)+")\t"+baseFileName[i]);
        }
        if (!emptyMessage) {
          System.out.println((i+1)+")\t All files ("+message+")");
        }
        str = in.readLine();
        try {
          input = Integer.parseInt(str, 10);
        }catch (NumberFormatException e) {
          System.out.println("Not a number");
          continue;
        }
        if(input == 0) {
          return null;
        } else if ((input-1) == baseFileName.length && !emptyMessage) {
          return new String("AllFiles");
        } else if ((input-1) > baseFileName.length || input < 0) {
          System.out.println("Number out of range");
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
    for(int i=0;i<struct.nbPatterns;i++){
      System.out.println("Pattern declared line: "+struct.patternLine[i]);
      System.out.println("=>\t"+struct.patternText[i]);
      System.out.println("--------------------------------------------------------------------------------");
    }
  }
  
  private void showActiveHandlers() {
    int nbHandler = activeTermHandlerList.size();
    if(nbHandler>0) {
      System.out.println("Here is the list of active term handler");
    } else {
      System.out.println("No active term handler");
      return;
    }
    for(int i=0;i<nbHandler;i++) {
      System.out.println((i+1)+") "+ ((TermHandler)activeTermHandlerList.get(i)).getHandlerName());
    }
  }
  
  private void showHandlers() {
    int nbHandler = termHandlerList.size();
    if(nbHandler>0) {
      System.out.println("Here is the list of registered term handler");
    } else {
      System.out.println("No registered term handler");
      return;
    }
    for(int i=0;i<nbHandler;i++) {
      System.out.println((i+1)+") "+ ((TermHandler)termHandlerList.get(i)).getHandlerName());
    }
  }
  
  private void removeActiveHandler() {
    try {
      String str = "";
      int input = 0;
      int nbHandler = 0;
      while (str != null) {
        showActiveHandlers();
        nbHandler = activeTermHandlerList.size();
        if (nbHandler == 0) {
          System.out.println("No more term handler to desactivate...\n");
          return;
        }
        System.out.print(">Term handler to desactivate (0:exit, `all`:all):");
        str = in.readLine();
        if (str.equals("all")){
          activeTermHandlerList.clear();
          return;
        } else {
            // TODO: Segregate activated Handler
          try {
            input = Integer.parseInt(str, 10);
          }catch (NumberFormatException e) {
            System.out.println("Not a number");
            continue;
          }
          if(input == 0) {
            return ;
          } else if ((input-1) >= nbHandler || input<0) {
            System.out.println("Not a valid number");
            continue;
          } else {
            activeTermHandlerList.remove(input-1);
          }
        }
      }
    } catch (IOException e) {
    }
  }
  
  private void defineActineTermHandler() {
    showHandlers();
    int nbHandler = termHandlerList.size();
    if (nbHandler == 0) {
      return;
    }
    try {
      String str = "";
      int input = 0;
      while (str != null) {
        System.out.print(">Term handler to activate (0:exit, `all`:all):");
        str = in.readLine();
        if (str.equals("all")){
          //for(int i=0;i<termHandlerList.size();i++) {
          //  activeTermHandlerList.add(termHandlerList.get(i));            
          //}
          activeTermHandlerList = (ArrayList) termHandlerList.clone();
          return;
        } else {
            // TODO: Segregate activated Handler
          try {
            input = Integer.parseInt(str, 10);
          }catch (NumberFormatException e) {
            System.out.println("Not a number");
            continue;
          }
          if(input == 0) {
            return ;
          } else if ((input-1) >= nbHandler || input<0) {
            System.out.println("Not a valid number");
            continue;
          } else {
            activeTermHandlerList.add(termHandlerList.get(input-1));
          }
        }
      }
    } catch (IOException e) {
    }
  }
  
  private void showSubjects() {
    if (!environment.empty()) {
      ((TomDebugEnvironment)environment.peek()).showSubjects();
    } else {
      System.out.println("Empty environment");
    }
  }
  
  private void showPatterns() {
    if (!environment.empty()) {
      ((TomDebugEnvironment)environment.peek()).showPatterns();
    } else {
      System.out.println("Empty environment");
    }
  }
  
  private void showSubsts() {
    if (!environment.empty()) {
      ((TomDebugEnvironment)environment.peek()).showSubsts();
    } else {
      System.out.println("Empty environment");
    }
  }
  
    //////////////////////
    // Debug Primitives //
    //////////////////////
  public void emptyStack() {
    if(!environment.empty()) {
      environment.pop();
    }
  }
  
  public void registerHandler(TermHandler th) {
    System.out.println("Registering handler: "+th.getHandlerName());
    termHandlerList.add(th);
  }

  private boolean evalCondition(String key) {
    return (debuggedStructureKeySet.contains(key) /*|| nextFailure*/);
  }
  
  public void enteringStructure(String key) {
    if(evalCondition(key) == true) {
      environment.push(new TomDebugEnvironment((TomDebugStructure)mapKeyDebugStructure.get(key), nextFailure));
    }
  }
  
  public void enteringDefaultPattern(String key) {
    if(!environment.empty() && ((TomDebugEnvironment)(environment.peek())).getKey().equals(key)) {
      ((TomDebugEnvironment)(environment.peek())).enteringDefaultPattern();
    }
  }
  
  public void enteringPattern(String key) {
    if(!environment.empty() && ((TomDebugEnvironment)(environment.peek())).getKey().equals(key)) {
      ((TomDebugEnvironment)(environment.peek())).enteringPattern();
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
  
  public void leavingPattern(String key) {
    if(!environment.empty() && ((TomDebugEnvironment)(environment.peek())).getKey().equals(key)) {
      ((TomDebugEnvironment)(environment.peek())).leavingPattern();
    }
  }

  public void leavingStructure(String key) {
    if(!environment.empty() && ((TomDebugEnvironment)(environment.peek())).getKey().equals(key)) {
      int status = ((TomDebugEnvironment)(environment.peek())).leaving();
      environment.pop();
      if (status == -1 && nextFailure) {
        nextFailure = false;
        showMainMenu();
      }
    }
  }
  
  public void termCreation(Object trm) {
    for(int i=0;i<activeTermHandlerList.size();i++) {
      ((TermHandler)(activeTermHandlerList.get(i))).testTerm(trm);
    }
  }

  public void breakOnPattern(String id) {
    System.out.println("The breakpoint link to pattern `"+id+"` has been reached");
    showMenu2();
  }
  
  public void specifySubject(String key, String name, Object trm) {
    if (!environment.empty())
    ((TomDebugEnvironment)environment.peek()).addSubject(name, trm);
  }

  public void addSubstitution(String key, String name, Object trm) {
    if (!environment.empty())
      ((TomDebugEnvironment)environment.peek()).addSubstitution(name, trm);
  }

    ////////////////////////
    // Structure Analyses //
    //////////////////////// 
  private void analyseStructure(TomStructureTable trm) {
    TomList list = trm.getStructList();
    while (!list.isEmpty()) {
      TomTerm struct = list.getHead();
      if (struct.isMatch()) {
          // The only present option in the list is the orgTack
        Option orgTrack = struct.getOption().getOptionList().getHead();
        String fileName = orgTrack.getFileName().getString();
        int line = orgTrack.getLine();
        String key = fileName+line;
        TomList paList = struct.getPatternList().getTomList();
        int nbPatterns =  paList.getLength();
        int nbSubjects = struct.getSubjectList().getTomList().getLength();
        String[] patternText = new String[nbPatterns];
        int[] patternLine = new int[nbPatterns];
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
        mapKeyDebugStructure.put(key, new TomDebugStructure(key, "Match", fileName, line, nbPatterns, nbSubjects, patternText, patternLine));
        
      } else if (struct.isRuleSet()) {
        String fileName = struct.getOrgTrack().getFileName().getString();
        int line = struct.getOrgTrack().getLine();
        String key = fileName+line;
        TomList paList = struct.getTomList();
        int nbPatterns =  paList.getLength();
        int nbSubjects = struct.getTomList().getHead().getLhs().getTomTerm().getArgs().getLength();
        String[] patternText = new String[nbPatterns];
        int[] patternLine = new int[nbPatterns];
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
        mapKeyDebugStructure.put(key, new TomDebugStructure(key, "Rule", fileName, line, nbPatterns, nbSubjects,  patternText, patternLine));
        
      } else {
        System.out.println("Corrupt debug term");
		    throw new TomRuntimeException(new Throwable("Corrupt debug term"));
      } 
      list = list.getTail();
    }
  }
  
  private int extractPatternLine(OptionList list) {
    Option op;
    while(!list.isEmpty()) {
      op = list.getHead();
      if(op.isOriginTracking()) {
        return op.getLine();
      }
      list = list.getTail();
    }
    return -1;
  }

  private String extractPatternText(OptionList list) {
    Option op;
    while(!list.isEmpty()) {
      op = list.getHead();
      if(op.isOriginalText()) {
        return op.getAstName().getString();
      }
      list = list.getTail();
    }
    return null;
  }
  
} //Class TomDebugger

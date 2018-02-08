/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2016-2017, Universite de Lorraine
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

package tom.engine.parser.tomjava;

import java.util.logging.Logger;
import java.util.*;
import java.io.*;

import java.lang.reflect.InvocationTargetException;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import tom.engine.adt.cst.types.*;
import tom.engine.adt.code.types.*;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.exception.TomRuntimeException;
import tom.engine.exception.TomIncludeException;
import tom.engine.exception.TomException;
import tom.engine.parser.TomParserTool;

import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;
import tom.engine.TomStreamManager;
import tom.platform.OptionManager;

import tom.library.sl.*;

public class CstConverter {
  %include { ../../../library/mapping/java/sl.tom}
  %include { ../../adt/cst/CST.tom }
  %typeterm CstConverter { implement { CstConverter }}

  private static Logger logger = Logger.getLogger("tom.engine.parser.CstConverter");
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

  private TomParserTool parserTool;
  private static HashMap<String,List<String>> includedFiles = new HashMap<String,List<String>>();

  public CstConverter(TomParserTool parserTool) {
    this.parserTool = parserTool;
  }

  public TomParserTool getParserTool() {
    return this.parserTool;
  }

  public TomStreamManager getStreamManager() {
    return this.getParserTool().getStreamManager();
  }

  public CstBlockList convert(CstBlockList t) {
    try {
      t = `BottomUp(SimplifyCST(this)).visitLight(t);
    } catch(VisitFailure e) {
      // do nothing
    }
    return t;
  }

  /*
   * replace IncludeFile by IncludeConstruct corresponding to the content of the file
   * parse text corresponding to GomConstruct and include the generated *.tom file
   * generate declarations for Cst_StrategyConstruct
   * flatten BQComposite(...,Cst_BQComposite(...),...) -> Cst_BQComposite(...)
   * merge HOSTBLOCK: ConcCstBlock(...,HOSTBLOCK,HOSTBLOCK,...) ->  ConcCstBlock(...,HOSTBLOCK,...)
   * merge ITL: ConcCstBQTerm(...,Cst_ITL,Cst_ITL,...) -> ConcCstBQTerm(...,Cst_ITL,...)
   * Cst_Anti(Cst_Anti(t)) -> t
   */
  %strategy SimplifyCST(cc:CstConverter) extends Identity() {

    visit CstBlock {
      Cst_IncludeFile(ConcCstOption(Cst_OriginTracking(currentFileName,l1,c1,l2,c2)),filename) -> {
        try {
          return cc.includeFile(`currentFileName,`filename,`l1);
        } catch(TomIncludeException e) {
          e.printStackTrace();
        }
      }

      Cst_GomConstruct(ConcCstOption(ot@Cst_OriginTracking(currentFileName,l1,c1,l2,c2)),nameList,text) -> {
        try {
          /*System.out.println("GomConstruct: " + `text);*/
          return cc.gomFile(`currentFileName,`nameList,`text,`l1);
        } catch(TomIncludeException e) {
          e.printStackTrace();
        }
      }

      Cst_Metaquote(ol@ConcCstOption(ot@Cst_OriginTracking(currentFileName,l1,c1,l2,c2)), ConcCstBlock(HOSTBLOCK(optionList, code))) -> {
        String metacode = `code.substring(2,`code.length()-2);
        /*System.out.println("CstConverter1: " + metacode);*/
        CstBlockList bl = cc.tomSplitter(`optionList, metacode);
        /*System.out.println("CstConverter2: " + `Cst_Metaquote(ol,bl));*/
        return `Cst_Metaquote(ol,bl);
      }

      s@Cst_StrategyConstruct(ConcCstOption(Cst_OriginTracking(currentFileName,l1,c1,l2,c2)), Cst_Name(stratName), stratArgs, extendsTerm, visitList) -> {
        String opargs = "";
        String getslots = "";
        String makeargs = "";
        String newargs = "";
        int index = 1;
        %match(stratArgs) {
          ConcCstSlot(_*,Cst_Slot(Cst_Name(name),Cst_Type(type)),_*) -> {
            opargs += (`name + ":" + `type + ",");
            makeargs += ("t" + index + ",");
            newargs += ("$t" + index + ",");
            index++;
            getslots += %[get_slot(@`name@,t) { ((@`stratName@)$t).get@`name@() }]%;
          }
        }
        index = opargs.lastIndexOf(',');
        if(index > 0) { opargs = opargs.substring(0,index); }
        index = makeargs.lastIndexOf(',');
        if(index > 0) { makeargs = makeargs.substring(0,index); }
        index = newargs.lastIndexOf(',');
        if(index > 0) { newargs = newargs.substring(0,index); }

        String opdecl = %[
          %op Strategy @`stratName@(@opargs@) {
            is_fsym(t) { ($t instanceof @`stratName@) }
            @getslots@
            make(@makeargs@) { new @`stratName@(@newargs@) }
          }
        ]%;

        /* call the parser to build the corresponding CST */
        ANTLRInputStream tomInput = new ANTLRInputStream(opdecl);
        CstBlock block = cc.parseStream(tomInput,`currentFileName, tom.engine.parser.tomjava.TomParser.JAVA_DECLARATIONS_LEVEL);
        return `Cst_AbstractBlock(ConcCstBlock(s,block));
      }
    }

    visit CstBQTerm {
      Cst_BQComposite(option,ConcCstBQTerm(C1*,Cst_BQComposite(_,args),C2*)) -> { 
        /* flatten Cst_BQComposite */
        return `Cst_BQComposite(option,simplifyCstBQTermList(ConcCstBQTerm(C1*,args*,C2*)));
      }
    }

    visit CstPattern {
      Cst_Anti(Cst_Anti(t)) -> { 
        return `t; 
      }
    }

    visit CstBlockList {
      s@ConcCstBlock(_*) -> {
        /* merge HOSTBLOCK */
        /*return addSpace(simplifyCstBlockList(`s));*/
        return simplifyCstBlockList(`s);
      }
    }

    visit CstBQTermList {
      s@ConcCstBQTerm(_*) -> {
        /* merge Cst_ITL */
        return simplifyCstBQTermList(`s);
      }
    }

  }


  /*
   * include a file and return the corresponding CST
   */
  private CstBlock includeFile(String currentFileName, String filename, int lineNumber) throws TomIncludeException {
    //System.out.println("include: " + `filename);
    String canonicalPath = getParserTool().searchIncludeFile(currentFileName,filename,lineNumber);

    List<String> listOfIncludedFiles = includedFiles.get(getStreamManager().getInputFileName());
    if(listOfIncludedFiles == null) {
      listOfIncludedFiles = new ArrayList<String>();
      includedFiles.put(getStreamManager().getInputFileName(),listOfIncludedFiles);
    }

    //System.out.println("*** Try include: " + canonicalPath);
    //System.out.println("\tcurrentFileName: " + getStreamManager().getInputFileName());
    //System.out.println("\tlistOfIncludedFiles: " + listOfIncludedFiles);
    if(listOfIncludedFiles.contains(canonicalPath)) {
      //System.out.println("\tdo not include: " + canonicalPath);
      if(!getStreamManager().isSilentDiscardImport(filename)) {
        TomMessage.info(logger, currentFileName, lineNumber, TomMessage.includedFileAlreadyParsed,filename);
      }
      return `Cst_IncludeConstruct(ConcCstBlock());
    } else {
      listOfIncludedFiles.add(canonicalPath);
      //System.out.println("\tdo include: " + canonicalPath);
    }
    
    // parse the file
    try {
      ANTLRInputStream tomInput = new ANTLRFileStream(canonicalPath);
      CstBlock block = parseStream(tomInput,canonicalPath,tom.engine.parser.tomjava.TomParser.JAVA_DECLARATIONS_LEVEL);
      return `Cst_IncludeConstruct(ConcCstBlock(block));
    } catch (IOException e) {
      throw new RuntimeException(e); //XXX
    }
  }

  private CstBlock parseStream(ANTLRInputStream tomInput, String canonicalPath, int parseLevel) {
    // parse the file
    try {
      tom.engine.parser.tomjava.TomParser parser = new tom.engine.parser.tomjava.TomParser(canonicalPath, getLogger());
      //System.out.println("\tCstConverter.parseStream: " + canonicalPath);
      CstBlockList include = parser.parse(tomInput, parseLevel, getParserTool());
      return `Cst_AbstractBlock(include);
    } catch (Exception e) {
      throw new RuntimeException(e); //XXX
    }

    //return `Cst_AbstractBlock(ConcCstBlock());
  }


  /*
   * parse a Gom construct, include the resulting *.tom file, and return the corresponding CST
   */
  private CstBlock gomFile(String currentFileName, CstNameList nameList, String gomCode, int initialGomLine) throws TomIncludeException {
    //System.out.println("gomCode: " + gomCode);
    //System.out.println("initialGomLine: " + initialGomLine);
    //System.out.println("gomCode: " + nameList);

    // add blank lines to synchronize line numbers
    String newline = System.getProperty("line.separator");
    for(int i=0 ; i< initialGomLine ; i++) {
      gomCode = newline + gomCode;
    }


    int nbOpts = nameList.length();
    String[] userOpts = new String[nbOpts];
    int i = 0;
    %match(nameList) {
      ConcCstName(_*,Cst_Name(name),_*) -> {
        userOpts[i++] = `name;
      }
    }
    String generatedMapping = getParserTool().parseGomFile(gomCode,initialGomLine, userOpts);
    if(generatedMapping != null && generatedMapping.length() > 0) {
      return includeFile(currentFileName, generatedMapping, initialGomLine);
    }
    return `Cst_AbstractBlock(ConcCstBlock());
  }

  /*
   * merge HOSTBLOCK: iterative version to avoid stack overflow
   */
  private static CstBlockList simplifyCstBlockList(CstBlockList l) {
    CstBlockList res = `ConcCstBlock(); 
    CstBlock last = null;
    int size = 0;
    while(!l.isEmptyConcCstBlock()) {
      size++;
      CstBlock head = l.getHeadConcCstBlock();
      if(!head.isHOSTBLOCK()) {
        if(last != null) {
          res = `ConcCstBlock(last,res*);
          last = null;
        }
        res = `ConcCstBlock(head,res*);
      } else {
        if(last == null) {
          last = head;
        } else {
          %match(last,head) {
            HOSTBLOCK(ConcCstOption(Cst_OriginTracking(name,lmin1,cmin1,lmax1,cmax1)),text1),
            HOSTBLOCK(ConcCstOption(Cst_OriginTracking(name,lmin2,cmin2,lmax2,cmax2)),text2) -> {
              String s = `mergeString(text1,text2,lmax1,cmax1,lmin2,cmin2);
              last = `HOSTBLOCK(ConcCstOption(Cst_OriginTracking(name,lmin1,cmin1,lmax2,cmax2)),s);
            }
          }
        }
      }
      l = l.getTailConcCstBlock();
    }


    if(last != null) {
      res = `ConcCstBlock(last,res*);
      last = null;
    }
    return res.reverse();
  }

  /*
   * add a space between each HOSTBLOCK (which could has been lost by the parser)
   */
  /*
  private static CstBlockList addSpace(CstBlockList l) {
    %match(l) {
      ConcCstBlock(HOSTBLOCK(ConcCstOption(Cst_OriginTracking(name,lmin,cmin,lmax,cmax)),text),tail*) -> {
        CstBlock hb = `HOSTBLOCK(ConcCstOption(Cst_OriginTracking(name,lmin,cmin,lmax,cmax+1)),text+" ");
        CstBlockList newTail = `addSpace(tail*);
        return `ConcCstBlock(hb,newTail*);
      }

      ConcCstBlock(other,tail*) -> {
        CstBlockList newTail = `addSpace(tail*);
        return `ConcCstBlock(other,newTail*);
      }
    }
    return l;
  }
  */

  /*
   * merge ITL
   */
  private static CstBQTermList simplifyCstBQTermList(CstBQTermList l) {
    %match(l) {
      ConcCstBQTerm(
          head*,
          Cst_ITL(ConcCstOption(Cst_OriginTracking(name,lmin1,cmin1,lmax1,cmax1)),text1),
          Cst_ITL(ConcCstOption(Cst_OriginTracking(name,lmin2,cmin2,lmax2,cmax2)),text2),tail*) -> {
        String s = `mergeString(text1,text2,lmax1,cmax1,lmin2,cmin2);
        if(s != null) {
          return `simplifyCstBQTermList(ConcCstBQTerm(head*,Cst_ITL(ConcCstOption(Cst_OriginTracking(name,lmin1,cmin1,lmax2,cmax2)),s),tail*));
        }
      }
    }
    return l;
  }

  /*
   * add missing spaces/newlines between two strings
   */
  private static String mergeString(String s1, String s2, int lmax1, int cmax1, int lmin2, int cmin2) {
    String newline = System.getProperty("line.separator");
    if(lmax1 < lmin2) {
      //System.out.println("mergeString: '" + s1 + "' --- '" + s2 + "'");
      //System.out.println("lmax1 = " + lmax1 + " lmin2 = " + lmin2);
    }
    while(lmax1 < lmin2) {
      s1 += newline;
      lmax1++;
      cmax1 = 1;
    }
    if(cmax1 < cmin2) {
      //System.out.println("mergeString: '" + s1 + "' --- '" + s2 + "'");
      //System.out.println("cmax1 = " + cmax1 + " cmin2 = " + cmin2);
    }
    while(cmax1 < cmin2) {
      s1 += " ";
      cmax1++;
    }
    s1 += s2;
    return s1;
  }


  /*
   * this function receives a string that comes from %[ ... ]%
   * @@ corresponds to the char '@', so it is encoded into ]% (which cannot
   * appear in the string)
   * then, the string is split around the delimiter @
   * alternatively, each string correspond either to a metaString, or a string
   * to parse the @@ encoded by ]%, it is put back as a single '@' in the metaString
   */
  
  private CstBlockList tomSplitter(CstOptionList optionList,String subject) {

    CstBlockList bl = `ConcCstBlock();
    String metaChar = "]%";
    String escapeChar = "@";

    //System.out.println("initial subject: '" + subject + "'");
    subject = subject.replace(escapeChar+escapeChar,metaChar);
    //System.out.println("subject: '" + subject + "'");
    String split[] = subject.split(escapeChar);
    boolean last = subject.endsWith(escapeChar);
    int numSeparator = split.length + 1 + (last ? 1 : 0);
    if(numSeparator%2==1) {
      //TomMessage.error(logger, currentFile, getLine(), TomMessage.badNumberOfAt);
    }
    //System.out.println("split.length: " + split.length);
    boolean metaMode = true;
    for(int i=0 ; i<split.length ; i++) {
      if(metaMode) {
        // put back escapeChar instead of metaChar
        String code = getParserTool().metaEncodeCode(split[i].replace(metaChar,escapeChar));
        metaMode = false;
        //System.out.println("metaString: '" + code + "'");

        bl = `ConcCstBlock(HOSTBLOCK(optionList,code),bl*);
      } else {
        String code = split[i];
        metaMode = true;
        //System.out.println("prg to parse: '" + code + "'");
        try {
          ANTLRInputStream tomInput = new ANTLRInputStream(code.toCharArray(), code.length());
          CstBlock block = parseStream(tomInput,"metaquote",tom.engine.parser.tomjava.TomParser.JAVA_EXPRESSION_LEVEL);
          //System.out.println("block: " + block);
          CstBlock bPlus = `HOSTBLOCK(optionList,"+");
          bl = `ConcCstBlock(bPlus,block,bPlus,bl*);
          //System.out.println("bl: " + bl);
        } catch (Exception e) {
          throw new TomRuntimeException("Exception catched in tomSplitter: " + e);
        }
      }
    }
    if(subject.endsWith(escapeChar)) {
      // add an empty string when %[...@...@]%
      bl = `ConcCstBlock(HOSTBLOCK(optionList,"\"\""),bl*);
    }
    return bl.reverse();
  }
 
}

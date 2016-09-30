/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2016-2016, Universite de Lorraine
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

package tom.engine.parser.antlr4;

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

  public CstConverter(TomParserTool parserTool) {
    this.parserTool = parserTool;
  }

  public TomParserTool getParserTool() {
    return this.parserTool;
  }

  public TomStreamManager getStreamManager() {
    return this.getParserTool().getStreamManager();
  }

  public CstProgram convert(CstProgram t) {
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

      Cst_GomConstruct(ConcCstOption(Cst_OriginTracking(currentFileName,l1,c1,l2,c2)),blocks) -> {
        try {
          String text = cstBlockListToString(`blocks);
          return cc.gomFile(`currentFileName,text,`l1);
        } catch(TomIncludeException e) {
          e.printStackTrace();
        }
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
        CstBlock block = cc.parseStream(tomInput,`currentFileName);
        return `Cst_AbstractBlock(ConcCstBlock(s,block));
      }
    }

    visit CstBQTerm {
      Cst_BQComposite(option,ConcCstBQTerm(C1*,Cst_BQComposite(_,args),C2*)) -> { 
        /* flatten Cst_BQComposite */
        return `Cst_BQComposite(option,ConcCstBQTerm(C1*,args*,C2*));
      }
    }

    visit CstBlockList {
      s@ConcCstBlock(_*) -> {
        /* merge HOSTBLOCK */
        return addSpace(simplifyCstBlockList(`s));
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
    String canonicalPath = getParserTool().searchIncludeFile(currentFileName, filename,lineNumber);

    // parse the file
    try {
      ANTLRInputStream tomInput = new ANTLRFileStream(canonicalPath);
      CstBlock block = parseStream(tomInput,filename);
      return `Cst_IncludeConstruct(ConcCstBlock(block));
    } catch (IOException e) {
      throw new RuntimeException(e); //XXX
    }
  }

  private CstBlock parseStream(ANTLRInputStream tomInput, String filename) {
    // parse the file
    try {
      tom.engine.parser.antlr4.TomParser parser = new tom.engine.parser.antlr4.TomParser(filename, getParserTool(), getStreamManager().getSymbolTable());
      CstProgram include = parser.parse(tomInput);
      %match(include) {
        Cst_Program(blocks) -> { 
          return `Cst_AbstractBlock(blocks);
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e); //XXX
    }

    return `Cst_AbstractBlock(ConcCstBlock());
  }


  /*
   * parse a Gom construct, include the resulting *.tom file, and return the corresponding CST
   */
  private CstBlock gomFile(String currentFileName, String gomCode, int initialGomLine) throws TomIncludeException {
    //System.out.println("gomCode: " + gomCode);
    String[] userOpts = new String[0];
    String generatedMapping = getParserTool().parseGomFile(gomCode,initialGomLine, userOpts);
    if(generatedMapping != null && generatedMapping.length() > 0) {
      return includeFile(currentFileName, generatedMapping, initialGomLine);
    }
    return `Cst_AbstractBlock(ConcCstBlock());
  }

  /*
   * merge HOSTBLOCK
   */
  private static CstBlockList simplifyCstBlockList(CstBlockList l) {
    %match(l) {
      ConcCstBlock(
          head*,
          HOSTBLOCK(ConcCstOption(Cst_OriginTracking(name,lmin1,cmin1,lmax1,cmax1)),text1),
          HOSTBLOCK(ConcCstOption(Cst_OriginTracking(name,lmin2,cmin2,lmax2,cmax2)),text2),tail*) -> {
        String s = `mergeString(text1,text2,lmax1,cmax1,lmin2,cmin2);
        if(s != null) {
          return `simplifyCstBlockList(ConcCstBlock(head*,HOSTBLOCK(ConcCstOption(Cst_OriginTracking(name,lmin1,cmin1,lmax2,cmax2)),s),tail*));
        }

      }
    }
    return l;
  }

  /*
   * add a space between each HOSTBLOCK (which could has been lost by the parser)
   */
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
    //System.out.println("mergeString: " + s1 + " --- " + s2);
    String newline = System.getProperty("line.separator");
    while(lmax1 < lmin2) {
      s1 += newline;
      lmax1++;
      cmax1 = 1;
    }
    while(cmax1 < cmin2) {
      s1 += " ";
      cmax1++;
    }
    s1 += s2;
    return s1;
  }

  private static String cstBlockListToString(CstBlockList blocks) {
    StringBuffer sb = new StringBuffer();
    %match(blocks) {
      ConcCstBlock(_*,x,_*) -> {
        %match(x) {
          HOSTBLOCK(options,text) -> {
            sb.append(`text);
          } 

          Cst_UnamedBlock(bl) -> {
            sb.append("{");
            sb.append(cstBlockListToString(`bl));
            sb.append("}");
          }

        }
      }

    }
    return sb.toString();
  }

}

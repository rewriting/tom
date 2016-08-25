/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import tom.engine.adt.cst.types.*;
import tom.engine.adt.code.types.*;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.exception.TomRuntimeException;
import tom.engine.exception.TomIncludeException;
import tom.engine.exception.TomException;

import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;
import tom.engine.TomStreamManager;

import tom.library.sl.*;

public class CstConverter {
  %include { ../../../library/mapping/java/sl.tom}
  %include { ../../adt/cst/CST.tom }
  %typeterm CstConverter { implement { CstConverter }}

  private static Logger logger = Logger.getLogger("tom.engine.typer.CstConverter");
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

  private TomStreamManager streamManager;

  public CstConverter(TomStreamManager streamManager) {
    this.streamManager = streamManager;
  }

  public TomStreamManager getStreamManager() {
    return this.streamManager;
  }

  public CstProgram convert(CstProgram t) {
    try {
      t = `BottomUp(SimplifyCST(this)).visitLight(t);
    } catch(VisitFailure e) {
      // do nothing
    }
    return t;
  }

  private CstBlock includeFile(String filename, int lineNumber) throws TomIncludeException {
    System.out.println("include: " + `filename);
    String currentFileName = getStreamManager().getInputFileName();  

    String includeName = `filename.trim();
    includeName = includeName.replace('/',File.separatorChar);
    includeName = includeName.replace('\\',File.separatorChar);
    if(includeName.equals("")) {
      throw new TomIncludeException(TomMessage.missingIncludedFile,
        new Object[]{currentFileName, lineNumber});
    }

    File file = new File(includeName);
    if(file.isAbsolute()) {
      try {
        file = file.getCanonicalFile();
      } catch (IOException e) {
        System.out.println("IO Exception when computing included file");
        e.printStackTrace();
      }

      if(!file.exists()) {
        file = null;
      }
    } else {
      /* StreamManager shall find it */
      file = getStreamManager().findFile(new File(currentFileName).getParentFile(), includeName);
    }
    if(file == null) {
      throw new TomIncludeException(TomMessage.includedFileNotFound,
          new Object[]{
            filename, 
            currentFileName, 
            lineNumber,
            currentFileName});
    }

    // parse the file
    ANTLRFileStream tomInput;
    try {
      tomInput = new ANTLRFileStream(file.getCanonicalPath());
      tom.engine.parser.antlr4.TomParser parser = new tom.engine.parser.antlr4.TomParser(filename, getStreamManager(), getStreamManager().getSymbolTable());

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

  %strategy SimplifyCST(cc:CstConverter) extends Identity() {

    visit CstBlock {
      Cst_IncludeConstruct(ConcCstOption(Cst_OriginTracking(_,l1,c1,l2,c2)),filename) -> {
        try {
          return cc.includeFile(`filename,`l1);
        } catch(TomIncludeException e) {
          e.printStackTrace();
        }
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

}

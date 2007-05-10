/*
 * Gom
 * 
 * Copyright (c) 2006-2007, INRIA
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
 * Yoann Toussaint    e-mail: Yoann.Toussaint@loria.fr
 * 
 **/

package tom.gom.parser;

import java.util.logging.Logger;
import java.util.logging.Level;

import tom.gom.GomStreamManager;
import tom.antlrmapper.ATermAST;
import aterm.*;
import aterm.pure.*;

import tom.gom.GomMessage;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.adt.gom.*;
import tom.gom.adt.gom.types.*;

public class AST2Gom {

  %include { Mapping.tom }
  %include { ../adt/gom/Gom.tom }

  public static GomModule getGomModule(ATermAST t, GomStreamManager stream) {
    return getGomModule(t.genATermFromAST(TokenTable.getTokenMap()),stream);
  }

  private static GomModule getGomModule(ATerm t, GomStreamManager stream) {
    /*
     * The first sequence of (ID DOT)* ID nodes composes the module name
     */
    GomModuleName moduleName = null;
    ATermList list = null;
    %match(t) {
      MODULE(_,l) -> {
        list = `l;
        ATermList modname = `concATerm();
        while (true) {
          %match(ATermList list) {
            (a@(ID|DOT)[],_*) -> {
              modname = `concATerm(modname*,a);
              list = list.getNext();
              continue;
            }
            _ -> { break; }
          }
        }
        moduleName = getGomModuleName(modname, stream);
      }
    }
    if(moduleName == null) {
      throw new GomRuntimeException("No module name here");
    }
    %match(ATermList list) {
      (imports,section*) -> {
        %match(ATerm imports) {//checks that imports is a real import, not a section
          IMPORTS(_,_) -> {
            return `GomModule(moduleName,concSection(getImports(imports),getSection(section*)));
          }
        }
      }
      (section*) -> {
        return `GomModule(moduleName,concSection(getSection(section*)));
      }
    }
    throw new GomRuntimeException("Unable to translate: " + t);
  }

  private static GomModuleName getGomModuleName(ATerm t) {
    %match(t) {
      ID(NodeInfo[text=text],_) -> {
        return `GomModuleName(text);
      }
    }
    throw new GomRuntimeException("Unable to translate: " + t);
  }

  private static GomModuleName getGomModuleName(ATermList l, GomStreamManager stream) {
    %match(ATermList l) {
      (prefix*,ID(NodeInfo[text=text],_)) -> {
        if (!`prefix.isEmpty()) {
          setPkgPath(`prefix,stream);
        }
        return `GomModuleName(text);
      }
    }
    throw new GomRuntimeException("Unable to translate: " + l);
  }

  private static void setPkgPath(ATermList l, GomStreamManager stream) {
    StringBuffer pkgPrefix = new StringBuffer("");
    while (!l.isEmpty()) {
      ATerm t = l.getFirst();
      %match(t) {
        ID(NodeInfo[text=text],_) -> { pkgPrefix.append(`text); }
        DOT[] -> { pkgPrefix.append("."); }
      }
      l = l.getNext();
    }
    /* Take care to remove the last dot */
    pkgPrefix.deleteCharAt(pkgPrefix.length()-1);
    stream.appendToPackagePath(pkgPrefix.toString());
  }

  private static Section getImports(ATerm t) {
    %match(t) {
      IMPORTS(_,importList) -> { return `Imports(getImportList(importList)); }
    }
    throw new GomRuntimeException("Unable to translate: " + t);
  }

  private static ImportList getImportList(ATermList l) {
    %match(ATermList l) {
      (importM,tail*) -> {
        ImportList tmpL = getImportList(`tail);
        return `concImportedModule(getImportedModule(importM),tmpL*);
      }
      concATerm() -> {
        return `concImportedModule();
      }
    }
    throw new GomRuntimeException("Unable to translate: " + l);
  }

  private static ImportedModule getImportedModule(ATerm t) {
    %match(ATerm t) {
      module -> {
        /*
         * Add a list operator, as imports are unqualified names
         */
        return `Import(getGomModuleName(module));
      }
    }
    throw new GomRuntimeException("Unable to translate: " + t);
  }

  private static Section getSection(ATermList l) {
    %match(ATermList l) {
      (PUBLIC[],grammar*) -> { return `Public(getGrammarList(grammar*)); }
      (grammar*)             -> { return `Public(getGrammarList(grammar*)); }
    }
    throw new GomRuntimeException("Unable to translate: " + l);
  }

  private static GrammarList getGrammarList(ATermList l) {
    %match(ATermList l) {
      (g,tail*) -> {
        GrammarList tmpL = getGrammarList(`tail);
        %match (ATerm g) {
          SORTS[]  -> { return `concGrammar(getGrammar(g),tmpL*); }
          SYNTAX[] -> { return `concGrammar(getSorts(g),getGrammar(g),tmpL*); }
        }
      }
      concATerm() -> {
        return `concGrammar();
      }
    }
    throw new GomRuntimeException("Unable to translate: " + l);
  }
  //when sorts are declared with using 'sorts ...'
  private static Grammar getSorts(ATerm t) {
    %match(ATerm t) {
      SYNTAX(_,productions) -> {
        return `Sorts(getSortsList(productions));
      }
    }
    throw new GomRuntimeException("Unable to translate: " + t);
  }
  private static Grammar getGrammar(ATerm t) {
    %match(t) {
      SORTS(_,types)        -> { return `Sorts(getGomTypeList(types)); }
      SYNTAX(_,productions) -> { return `Grammar(getProductionList(productions*)); }
    }
    throw new GomRuntimeException("Unable to translate: " + t);
  }

  private static GomTypeList getSortsList(ATermList l) {
    %match(ATermList l) {
      (g,tail*) -> {
        GomTypeList tmpL = getSortsList(`tail);
        %match(g) {
          EQUALS(_,(type,_*)) -> { return `concGomType(getGomType(type),tmpL*); }
          COLON[]             -> { return `concGomType(tmpL*); }
          ARROW[]             -> { return `concGomType(tmpL*); }
        }
      }
      concATerm() -> {
        return `concGomType();
      }
    }
    throw new GomRuntimeException("Unable to translate: " + l);
  }

  private static Production getProduction(ATerm t) {
    %match(ATerm t) {
      ARROW(NodeInfo[line=line],(name,fieldlist*, type)) -> {
        return `Production(
            getId(name),
            getFieldList(fieldlist*),
            getGomType(type),
            Origin(line));
      }
      COLON(NodeInfo[text=code,line=line],(idType,id,hook,args*)) -> {
        return `Hook(getIdkind(idType),getId(id),
                     getHookKind(hook),getArgList(args),
                     code,Origin(line));
      }

    }
    throw new GomRuntimeException("Unable to translate: " + t);
  }

  private static ProductionList getProductionList(ATermList l) {
    %match(ATermList l) {
      (g,tail*) -> {
        ProductionList tmpL = getProductionList(`tail);
        %match(ATerm g) {
          ARROW[] -> {
            return `concProduction(getProduction(g),tmpL*);
          }
          EQUALS(_,(type,alternatives*)) -> {
            ProductionList alter = getAlternatives(`type,`alternatives*);
            return `concProduction(alter*,tmpL*);
          }
          //hook
          COLON[] -> {
            return `concProduction(getProduction(g),tmpL*);
          }
        }
      }
      concATerm() -> {
        return `concProduction();
      }
    }
    throw new GomRuntimeException("Unable to translate: " + l);
  }

  private static ProductionList getAlternatives(ATerm type, ATermList altL) {
    %match(ATermList altL) {
      (ALT(_,_),id,fieldlist*, ALT(_,_), tail*) -> {
        ProductionList tmpL = getAlternatives(type,`tail);
        return `concProduction(Production(getId(id),getFieldList(fieldlist*),getGomType(type),getIdLine(id)),tmpL*);
      }
      (id,fieldlist*, ALT(_,_), tail*) -> {
        ProductionList tmpL = getAlternatives(type,`tail);
        return `concProduction(Production(getId(id),getFieldList(fieldlist*),getGomType(type),getIdLine(id)),tmpL*);
      }
      (id,fieldlist*) -> {
        return `concProduction(Production(getId(id),getFieldList(fieldlist*),getGomType(type),getIdLine(id)));
      }
      concATerm() -> {
        return `concProduction();
      }
    }
    throw new GomRuntimeException("Unable to translate: " + altL);
  }

  private static GomTypeList getGomTypeList(ATermList l) {
    %match(ATermList l) {
      (g,tail*) -> {
        GomTypeList tmpL = getGomTypeList(`tail);
        return `concGomType(getGomType(g),tmpL*);
      }
      concATerm() -> {
        return `concGomType();
      }
    }
    throw new GomRuntimeException("Unable to translate: " + l);
  }
  private static GomType getGomType(ATerm t) {
    %match(ATerm t) {
      ID(NodeInfo[text=text],_) -> {
        return `GomType(text);
      }
    }
    throw new GomRuntimeException("Unable to translate: " + t);
  }

  private static String getId(ATerm t) {
    %match(t) {
      ID(NodeInfo[text=text],_) -> { return `text; }
    }
    throw new GomRuntimeException("Unable to translate: " + t);
  }

  private static Option getIdLine(ATerm t) {
    %match(t) {
      ID(NodeInfo[line=line],_) -> { return `Origin(line); }
    }
    throw new GomRuntimeException("Unable to get line for id: " + t);
  }

  private static IdKind getIdkind(ATerm t) {
    %match(t) {
      SORT[]     -> { return `KindSort(); }
      MODULE[]   -> { return `KindModule(); }
      OPERATOR[] -> { return `KindOperator(); }
    }
    throw new GomRuntimeException("Unable to translate: " + t);
  }

  private static HookKind getHookKind(ATerm t) {
    %match(t) {
      ID(NodeInfo[text=text],_) -> { return `HookKind(text);}
    }
    throw new GomRuntimeException("Unable to translate: " + t);
  }

  private static ArgList getArgList(ATermList l) {
    %match(ATermList l) {
      (a,COMMA(_,_),tail*) -> {
        ArgList tmpL = getArgList(`tail);
        return `concArg(Arg(getId(a)),tmpL*);
      }
      (a) -> {
        return `concArg(Arg(getId(a)));
      }
      concATerm() -> {
        return `concArg();
      }
    }
    throw new GomRuntimeException("Unable to translate: " + l);
  }
  private static FieldList getFieldList(ATermList l) {
    %match(ATermList l) {
      (f,COMMA(_,_),tail*) -> {
        FieldList tmpL = getFieldList(`tail);
        return `concField(getField(f),tmpL*);
      }
      (f) -> {
        return `concField(getField(f));
      }
      concATerm() -> {
        return `concField();
      }
    }
    throw new GomRuntimeException("Unable to translate: " + l);
  }

  private static Field getField(ATerm t) {
    %match(ATerm t) {
      COLON(_,(id, type)) -> {
        return `NamedField(getId(id),getGomType(type));
      }
      STAR(_,(type)) -> {
        return `StarredField(getGomType(type));
      }
      _ -> {
        Logger.getLogger("AST2Gom.class").log(Level.SEVERE,
            GomMessage.noSlotDeclaration.getMessage(),
            new Object[]{});
        throw new GomRuntimeException("parsing problem");
      }
    }
    throw new GomRuntimeException("Unable to translate: " + t);
  }
}

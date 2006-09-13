/*
 * Gom
 * 
 * Copyright (c) 2006, INRIA
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

import tom.antlrmapper.ATermAST;
import aterm.*;
import aterm.pure.*;

import tom.gom.GomMessage;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.adt.gom.*;
import tom.gom.adt.gom.types.*;

public class AST2Gom{

  %include { Mapping.tom }
  %include { ../adt/gom/Gom.tom }

  public static GomModule getGomModule(ATermAST t){
    return getGomModule(t.genATermFromAST(TokenTable.getTokenMap()));
  }

  private static GomModule getGomModule(ATerm t) {
    %match(ATerm t){
      MODULE(_,(name,imports,section*)) ->{
        %match(ATerm imports){//checks that imports is a real import, not a section
          IMPORTS(_,_) -> {
            return `GomModule(getGomModuleName(name),concSection(getImports(imports),getSection(section*)));
          }
        }
      }
      MODULE(_,(name,section*)) ->{
        return `GomModule(getGomModuleName(name),concSection(getSection(section*)));
      }
    }
    throw new GomRuntimeException("Unable to translate: " + t);
  }

  private static GomModuleName getGomModuleName(ATerm t) {
    %match(ATerm t){
      ID(NodeInfo[text=text],_) ->{
        return `GomModuleName(text);
      }
    }
    throw new GomRuntimeException("Unable to translate: " + t);
  }

  private static Section getImports(ATerm t) {
    %match(ATerm t){
      IMPORTS(_,importList) -> {
        return `Imports(getImportList(importList));
      }
    }
    throw new GomRuntimeException("Unable to translate: " + t);
  }

  private static ImportList getImportList(ATermList l) {
    %match(ATermList l){
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
    %match(ATerm t){
      module -> {
        return `Import(getGomModuleName(module));
      }
    }
    throw new GomRuntimeException("Unable to translate: " + t);
  }
  private static Section getSection(ATermList l) {
    %match(ATermList l){
      (PUBLIC(_,_),grammar*) -> {
        return `Public(getGrammarList(grammar*));
      }
      (grammar*) -> {
        return `Public(getGrammarList(grammar*));
      }
    }
    throw new GomRuntimeException("Unable to translate: " + l);
  }
  private static GrammarList getGrammarList(ATermList l) {
    %match(ATermList l){
      (g,tail*) -> {
        GrammarList tmpL = getGrammarList(`tail);
        %match (ATerm g){
          SORTS(_,_) -> {
            return `concGrammar(getGrammar(g),tmpL*);
          }
          SYNTAX(_,_) -> {
            return `concGrammar(getSorts(g),getGrammar(g),tmpL*);
          }
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
    %match(ATerm t){
      SYNTAX(_,productions) -> {
        return `Sorts(getSortsList(productions));
      }
    }
    throw new GomRuntimeException("Unable to translate: " + t);
  }
  private static Grammar getGrammar(ATerm t) {
    %match(ATerm t){
      SORTS(_,types) -> {
        return `Sorts(getGomTypeList(types));
      }
      SYNTAX(_,productions) -> {
        return `Grammar(getProductionList(productions*));
      }
    }
    throw new GomRuntimeException("Unable to translate: " + t);
  }

  private static GomTypeList getSortsList(ATermList l) {
    %match(ATermList l){
      (g,tail*) -> {
        GomTypeList tmpL = getSortsList(`tail);
        %match(ATerm g){
          EQUALS(_,(type,_*)) -> {
            return `concGomType(getGomType(type),tmpL*);
          }
          COLON(_,_) -> {
            return `concGomType(tmpL*);
          }
          ARROW(_,_) -> {
            return `concGomType(tmpL*);
          }
        }
      }
      concATerm() -> {
        return `concGomType();
      }
    }
    throw new GomRuntimeException("Unable to translate: " + l);
  }

  private static Production getProduction(ATerm t) {
    %match(ATerm t){
      ARROW(_,(name,fieldlist*, type)) -> {
        return `Production(getId(name),getFieldList(fieldlist*),getGomType(type));
      }
      COLON(NodeInfo(code,_,_),(id,hook)) -> {
        return `Hook(KindOperator(),getId(id),getHookKind(hook),getHookarg(hook),code);
      }
      COLON(NodeInfo(code,_,_),(idType,id,hook)) -> {
        return `Hook(getIdkind(idType),getId(id),getHookKind(hook),concArg(),code);
      }

    }
    throw new GomRuntimeException("Unable to translate: " + t);
  }

  private static ProductionList getProductionList(ATermList l) {
    %match(ATermList l){
      (g,tail*) -> {
        ProductionList tmpL = getProductionList(`tail);
        %match(ATerm g){
          ARROW(_,_) -> {
            return `concProduction(getProduction(g),tmpL*);
          }
          EQUALS(_,_) -> {
            ProductionList alter = getAlternatives(`g);
            return `concProduction(alter*,tmpL*);
          }
          //hook
          COLON(_,_) -> {
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
    %match(ATermList altL){
      (ALT(_,_),id,fieldlist*, ALT(_,_), tail*) -> {
        ProductionList tmpL = getAlternatives(type,`tail);
        return `concProduction(Production(getId(id),getFieldList(fieldlist*),getGomType(type)),tmpL*);
      }
      (id,fieldlist*, ALT(_,_), tail*) -> {
        ProductionList tmpL = getAlternatives(type,`tail);
        return `concProduction(Production(getId(id),getFieldList(fieldlist*),getGomType(type)),tmpL*);
      }
      (id,fieldlist*) -> {
        return `concProduction(Production(getId(id),getFieldList(fieldlist*),getGomType(type)));
      }
      concATerm() -> {
        return `concProduction();
      }
    }
    throw new GomRuntimeException("Unable to translate: " + altL);
  }

  private static ProductionList getAlternatives(ATerm t) {
    %match(ATerm t){
      EQUALS(_,(type,alternatives*)) -> {
        return getAlternatives(`type,`alternatives*);
      }
    }
    throw new GomRuntimeException("Unable to translate: " + t);
  }

  private static GomTypeList getGomTypeList(ATermList l) {
    %match(ATermList l){
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
    %match(ATerm t){
      ID(NodeInfo[text=text],_) ->{
        return `GomType(text);
      }
    }
    throw new GomRuntimeException("Unable to translate: " + t);
  }

  private static String getId(ATerm t) {
    %match(ATerm t){
      ID(NodeInfo[text=text],_) ->{
        return `text;
      }
    }
    throw new GomRuntimeException("Unable to translate: " + t);
  }

  private static IdKind getIdkind(ATerm t) {
    %match(ATerm t){
      SORT[] ->{
        return `KindSort();
      }
      MODULE[] ->{
        return `KindModule();
      }
      OPERATOR[] ->{
        return `KindOperator();
      }
    }
    throw new GomRuntimeException("Unable to translate: " + t);
  }

  private static HookKind getHookKind(ATerm t) {
    %match(ATerm t){
      MAKE[] ->{
        return `KindMakeHook();
      }
      MAKEINSERT[] ->{
        return `KindMakeinsertHook();
      }
      BLOCK[] ->{
        return `KindBlockHook();
      }
      IMPORT[] ->{
        return `KindImportHook();
      }
      INTERFACE[] ->{
        return `KindInterfaceHook();
      }
    }
    throw new GomRuntimeException("Unable to translate: " + t);
  }

  private static ArgList getHookarg(ATerm t) {
    %match(ATerm t){
      MAKE(_,args) -> {
        return getArgList(`args);
      }
      MAKEINSERT(_,args) -> {
        return getArgList(`args);
      }
      _ -> {
        return `concArg();
      }
    }
    throw new GomRuntimeException("Unable to translate: " + t);
  }

  private static ArgList getArgList(ATermList l){
    %match(ATermList l){
      (a,COMMA(_,_),tail*) -> {
        ArgList tmpL = getArgList(`tail);
        return `concArg(getArg(a),tmpL*);
      }
      (a) -> {
        return `concArg(getArg(a));
      }
      concATerm() -> {
        return `concArg();
      }
    }
    throw new GomRuntimeException("Unable to translate: " + l);
  }
  private static FieldList getFieldList(ATermList l){
    %match(ATermList l){
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

  private static Arg getArg(ATerm s) {
    return `Arg(getId(s));
  }

  private static Field getField(ATerm t) {
    %match(ATerm t){
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

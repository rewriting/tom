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

import tom.pom.ATermAST;
import aterm.*;
import aterm.pure.*;

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
        return `GomModule(getGomModuleName(name),concSection(getImports(imports),getSection(section*)));
      }
      MODULE(_,(name,section*)) ->{
        return `GomModule(getGomModuleName(name),concSection(getSection(section*)));
      }
    }
    throw new RuntimeException("Unable to translate: " + t);
  }

  private static GomModuleName getGomModuleName(ATerm t) {
    %match(ATerm t){
      ID(NodeInfo[text=text],_) ->{
        return `GomModuleName(text);
      }
    }
    throw new RuntimeException("Unable to translate: " + t);
  }

  private static Section getImports(ATerm t) {
    %match(ATerm t){
      IMPORTS(_,importList) -> {
        return `Imports(getImportList(importList));
      }
    }
    throw new RuntimeException("Unable to translate: " + t);
  }

  private static ImportList getImportList(ATermList l) {
    %match(ATermList l){
      (importM,tail*) -> {
        ImportList tmpL = getImportList(`tail);
        return `concImportedModule(getImportedModule(importM),tmpL*);
      }
      _ -> {
        return `concImportedModule();
      }
    }
    throw new RuntimeException("Unable to translate: " + l);
  }

  private static ImportedModule getImportedModule(ATerm t) {
    %match(ATerm t){
      module -> {
        return `Import(getGomModuleName(module));
      }
    }
    throw new RuntimeException("Unable to translate: " + t);
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
    throw new RuntimeException("Unable to translate: " + l);
  }
  private static GrammarList getGrammarList(ATermList l) {
    %match(ATermList l){
      (g,tail*) -> {
        GrammarList tmpL = getGrammarList(`tail);
        return `concGrammar(getGrammar(g),tmpL*);
      }
      _ -> {
        return `concGrammar();
      }
    }
    throw new RuntimeException("Unable to translate: " + l);
  }
  private static Grammar getGrammar(ATerm t) {
    %match(ATerm t){
      SORTS(_,types) -> {
        return `Sorts(getGomTypeList(types));
      }
      SYNTAX(_,productions) -> {
        return `Grammar(getProductionList(productions));
      }
    }
    throw new RuntimeException("Unable to translate: " + t);
  }
  private static Production getProduction(ATerm t) {
    %match(ATerm t){
      ARROW(_,(name,fieldlist*, type)) -> {
        return `Production(getId(name),getFieldList(fieldlist*),getGomType(type));
      }
    }
    throw new RuntimeException("Unable to translate: " + t);
  }

  private static ProductionList getProductionList(ATermList l) {
    %match(ATermList l){
      (g,tail*) -> {
        ProductionList tmpL = getProductionList(`tail);
        return `concProduction(getProduction(g),tmpL*);
      }
      _ -> {
        return `concProduction();
      }
    }
    throw new RuntimeException("Unable to translate: " + l);
  }
  private static GomTypeList getGomTypeList(ATermList l) {
    %match(ATermList l){
      (g,tail*) -> {
        GomTypeList tmpL = getGomTypeList(`tail);
        return `concGomType(getGomType(g),tmpL*);
      }
      _ -> {
        return `concGomType();
      }
    }
    throw new RuntimeException("Unable to translate: " + l);
  }
  private static GomType getGomType(ATerm t) {
    %match(ATerm t){
      ID(NodeInfo[text=text],_) ->{
        return `GomType(text);
      }
    }
    throw new RuntimeException("Unable to translate: " + t);
  }

  private static String getId(ATerm t) {
    %match(ATerm t){
      ID(NodeInfo[text=text],_) ->{
        return `text;
      }
    }
    throw new RuntimeException("Unable to translate: " + t);
  }

  private static FieldList getFieldList(ATermList l){
    %match(ATermList l){
      (f,tail*) -> {
        FieldList tmpL = getFieldList(`tail);
        return `concField(getField(f),tmpL*);
      }
      _ -> {
        return `concField();
      }
    }
    throw new RuntimeException("Unable to translate: " + l);
  }
  private static Field getField(ATerm t) {
    %match(ATerm t){
      COLON(_,(id, type)) -> {
        return `NamedField(getId(id),getGomType(type));
      }
    }
    throw new RuntimeException("Unable to translate: " + t);
  }
}

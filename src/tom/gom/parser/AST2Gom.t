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
 * Antoine Reilles    e-mail: Antoine.Reilles@loria.fr
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

  public static GomModule getGomModule(ATerm t) {
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

  public static GomModuleName getGomModuleName(ATerm t) {
    %match(ATerm t){
      ID(NodeInfo[text=text],_) ->{
        return `GomModuleName(text);
      }
    }
    throw new RuntimeException("Unable to translate: " + t);
  }

  public static Section getImports(ATerm t) {
    %match(ATerm t){
      IMPORTS(_,importList) -> {
        return `Imports(getImportList(importList));
      }
    }
    throw new RuntimeException("Unable to translate: " + t);
  }

  public static ImportList getImportList(ATermList l) {
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

  public static ImportedModule getImportedModule(ATerm t) {
    %match(ATerm t){
      module -> {
        return `Import(getGomModuleName(module));
      }
    }
    throw new RuntimeException("Unable to translate: " + t);
  }
  public static Section getSection(ATermList l) {
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
  public static GrammarList getGrammarList(ATermList l) {
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
  public static Grammar getGrammar(ATerm t) {
    %match(ATerm t){
      SORTS(_,types) -> {
        return `Sorts(getTypeList(types));
      }
      SYNTAX(_,productions) -> {
        return `Grammar(getProductionList(productions));
      }
    }
    throw new RuntimeException("Unable to translate: " + t);
  }
  public static Production getProduction(ATerm t) {
    %match(ATerm t){
      EQUALS(_,_) -> {
        //return `Production(getTypeList(types));
      }
    }
    throw new RuntimeException("Unable to translate: " + t);
  }
  public static ProductionList getProductionList(ATermList l) {
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
  public static GomTypeList getTypeList(ATermList l) {
    %match(ATermList l){
      (g,tail*) -> {
        GomTypeList tmpL = getTypeList(`tail);
        return `concGomType(getType(g),tmpL*);
      }
      _ -> {
        return `concGomType();
      }
    }
    throw new RuntimeException("Unable to translate: " + l);
  }
  public static GomType getType(ATerm t) {
    %match(ATerm t){
      ID(NodeInfo[text=text],_) ->{
        return `GomType(text);
      }
    }
    throw new RuntimeException("Unable to translate: " + t);
  }
  /*public static GomModule getGomModule(ATerm t) {
    %match(ATerm t){
    MODULE(_,(name,imports,section)) ->{
    return `GomModule(getGomModuleName(name),getgetSectionList(bodyList*));
    }
    }
    throw new RuntimeException("Unable to translate: " + t);
    }
    public static SectionList getSectionList(ATermList l) {
    %match(ATermList l){
    (section,tail*) -> {
    SectionList tmpL = getSectionList(`tail);
    return `concSection(getSection(section),tmpL*);
    }
    _ ->{
    return `concSection();
    }
    }
    throw new RuntimeException("Unable to translate: " + l);
    }
    public static GomModuleName getGomModuleName(ATerm t) {
    %match(ATerm t){
    ID(NodeInfo[text=text],_) ->{
    return `GomModuleName(text);
    }
    }
    throw new RuntimeException("Unable to translate: " + t);
    }

    public static Grammar getGrammar(ATerm t) {
    %match(ATerm t){
    SORTS(_,types) -> {
    return `Sorts(getTypeList(types));
    }
    SYNTAX(_,productions) -> {
    return `Grammar(getProductionList(productions));
    }
    }
    throw new RuntimeException("Unable to translate: " + t);
    }
    public static GrammarList getGrammarList(ATermList l) {
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
    }*/
}

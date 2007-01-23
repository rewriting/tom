/*
 *
 * PrintGomModule
 *
 * Copyright (c) 2007, INRIA
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
 * Eric Deplagne <Eric.Deplagne@loria.fr>
 *
 **/

package utils;

import tom.gom.adt.gom.types.*;

public class PrintGomModule {

    %include { adt/gom/Gom.tom}
    
    public static void printGomModule(GomModule gomModule) {
        %match(gomModule) {
            GomModule(GomModuleName(name),sectionList) -> {
                System.out.println("module "+`name);
                printSectionList(`sectionList);
            }
        }
    }

    public static void printSectionList(SectionList sectionList) {
        // In fact, we should just have one Imports, then one Public.
        %match(sectionList) {
            concSection() -> {
            }
            concSection(Imports(x),y*) -> {
                printImportList(`x);
                printSectionList(`y);
            }
            concSection(Public(x),y*) -> {
                System.out.println("abstract syntax");
                printGrammarList(`x);
                printSectionList(`y);
            }
        }
    }

    public static void printImportList(ImportList importList) {
        printImportList(importList,null);
    }

    private static void printImportList(ImportList importList, String list) {
        %match(importList) {
            concImportedModule() -> {
                if(list!=null) {
                    System.out.println("imports "+list);
                }
            }
            concImportedModule(Import(GomModuleName(name)),y*) -> {
                if(list==null) {
                    list="";
                }

                list=list+" "+`name;
                
                printImportList(`y,list);
            }
        }
    }

    public static void printGrammarList(GrammarList grammarList) {
        // In fact, we should just have one Sorts, then one Grammar.
        %match(grammarList) {
            concGrammar() -> {
            }
            concGrammar(Sorts(x),y*) -> {
                printGomTypeList(`x);
                printGrammarList(`y);
            }
            concGrammar(Grammar(x),y*) -> {
                printProductionList(`x);
                printGrammarList(`y);
            }
        }
    }

    public static void printGomTypeList(GomTypeList gomTypeList) {
        // We don't need to print it for a .gom file.
    }

    public static void printProductionList(ProductionList productionList) {
        %match(productionList) {
            concProduction() -> {
            }
            concProduction(Production(name,domain,GomType(codomain)),y*) -> {
                System.out.println("");
                System.out.println(`codomain+" =");
                System.out.print("    "+`name+"(");
                printFieldList(`domain);
                System.out.println(")");
                printProductionList2(`codomain,`y);
            }
        }
    }

    private static void printProductionList2(String codomain,ProductionList productionList) {
        %match(productionList) {
            concProduction() -> {
            }
            concProduction(Production(name,domain,GomType(codomain1)),y*) -> {
                if(`codomain1.equals(`codomain)) {
                    System.out.print("  | "+`name+"(");
                    printFieldList(`domain);
                    System.out.println(")");
                    printProductionList2(codomain,`y);
                } else {
                    printProductionList(productionList);
                }
            }
        }
    }

    public static void printFieldList(FieldList fieldList) {
        %match(fieldList) {
            concField() -> {
            }
            concField(StarredField(GomType(type))) -> {
                System.out.print(`type+"*");
            }
            concField(NamedField(name,GomType(type))) -> {
                System.out.print(`name+":"+`type);
            }
            concField(NamedField(name,GomType(type)),x,y*) -> {
                System.out.print(`name+":"+`type+",");
                printFieldList(`concField(x,y*));
            }
        }
    }
}

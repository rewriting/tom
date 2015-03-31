/*
 *
 * PrintGomModule
 *
 * Copyright (c) 2007-2015, Universite de Lorraine, Inria
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

import java.io.PrintStream;

import tom.gom.adt.gom.types.*;

public class PrintGomModule {

    %include { adt/gom/Gom.tom}
    
    public static void printGomModule(GomModule gomModule) {
        printGomModule(gomModule,System.out);
    }

    public static void printGomModule(GomModule gomModule,PrintStream stream) {
        %match(gomModule) {
            GomModule(GomModuleName(name),sectionList) -> {
                stream.println("module "+`name);
                printSectionList(`sectionList,stream);
            }
        }
    }

    public static void printSectionList(SectionList sectionList) {
        printSectionList(sectionList,System.out);
    }

    public static void printSectionList(SectionList sectionList,PrintStream stream) {
        // In fact, we should just have one Imports, then one Public.
        %match(sectionList) {
            concSection() -> {
            }
            concSection(Imports(x),y*) -> {
                printImportList(`x,stream);
                printSectionList(`y,stream);
            }
            concSection(Public(x),y*) -> {
                stream.println("abstract syntax");
                printGrammarList(`x,stream);
                printSectionList(`y,stream);
            }
        }
    }

    public static void printImportList(ImportList importList) {
        printImportList(importList,System.out);
    }

    public static void printImportList(ImportList importList,PrintStream stream) {
        printImportList(importList,null,stream);
    }

    private static void printImportList(ImportList importList, String list,PrintStream stream) {
        %match(importList) {
            concImportedModule() -> {
                if(list!=null) {
                    stream.println("imports "+list);
                }
            }
            concImportedModule(Import(GomModuleName(name)),y*) -> {
                if(list==null) {
                    list="";
                }

                list=list+" "+`name;
                
                printImportList(`y,list,stream);
            }
        }
    }

    public static void printGrammarList(GrammarList grammarList) {
        printGrammarList(grammarList,System.out);
    }

    public static void printGrammarList(GrammarList grammarList,PrintStream stream) {
        // In fact, we should just have one Sorts, then one Grammar.
        %match(grammarList) {
            concGrammar() -> {
            }
            concGrammar(Sorts(x),y*) -> {
                printGomTypeList(`x,stream);
                printGrammarList(`y,stream);
            }
            concGrammar(Grammar(x),y*) -> {
                printProductionList(`x,stream);
                printGrammarList(`y,stream);
            }
        }
    }

    public static void printGomTypeList(GomTypeList gomTypeList) {
        printGomTypeList(gomTypeList,System.out);
    }

    public static void printGomTypeList(GomTypeList gomTypeList,PrintStream stream) {
        // We don't need to print it for a .gom file.
    }

    public static void printProductionList(ProductionList productionList) {
        printProductionList(productionList,System.out);
    }

    public static void printProductionList(ProductionList productionList,PrintStream stream) {
        %match(productionList) {
            concProduction() -> {
            }
            concProduction(Production(name,domain,GomType(codomain)),y*) -> {
                stream.println("");
                stream.println(`codomain+" =");
                stream.print("    "+`name+"(");
                printFieldList(`domain,stream);
                stream.println(")");
                printProductionList2(`codomain,`y,stream);
            }
        }
    }

    private static void printProductionList2(String codomain,ProductionList productionList,PrintStream stream) {
        %match(productionList) {
            concProduction() -> {
            }
            concProduction(Production(name,domain,GomType(codomain1)),y*) -> {
                if(`codomain1.equals(`codomain)) {
                    stream.print("  | "+`name+"(");
                    printFieldList(`domain,stream);
                    stream.println(")");
                    printProductionList2(codomain,`y,stream);
                } else {
                    printProductionList(productionList,stream);
                }
            }
        }
    }

    public static void printFieldList(FieldList fieldList) {
        printFieldList(fieldList,System.out);
    }

    public static void printFieldList(FieldList fieldList,PrintStream stream) {
        %match(fieldList) {
            concField() -> {
            }
            concField(StarredField(GomType(type))) -> {
                stream.print(`type+"*");
            }
            concField(NamedField(name,GomType(type))) -> {
                stream.print(`name+":"+`type);
            }
            concField(NamedField(name,GomType(type)),x,y*) -> {
                stream.print(`name+":"+`type+",");
                printFieldList(`concField(x,y*),stream);
            }
        }
    }
}

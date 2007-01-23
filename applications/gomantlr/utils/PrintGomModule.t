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
        //System.out.println("before sortProductionList, productionList="+productionList);
        GrammarList grammarList=sortProductionList(productionList);
        //System.out.println("after sortProductionList, grammarList="+grammarList);
        printSortedGrammarList(grammarList);
    }

    private static GrammarList sortProductionList(ProductionList productionList) {
        %match(productionList) {
            concProduction() -> {
                return `concGrammar();
            }
            concProduction(x,y*) -> {
                GrammarList grammarList=sortProductionList(`y);
                return addToGrammarList(`x,grammarList);
            }
        }
        return `concGrammar();
    }

    private static GrammarList addToGrammarList(Production production, GrammarList grammarList) {
        %match(production) {
            Production(name,domain,GomType(codomain1)) -> {
                %match(grammarList) {
                    concGrammar() -> {
                        return `concGrammar(Grammar(concProduction(production)));
                    }
                    // We don't have Sorts here,
                    // and each Grammar is non-empty
                    // and contains only productions of type Production.
                    concGrammar(z*,x@Grammar(y@concProduction(Production(_,_,GomType(codomain2)),_*))) -> {
                        if(`codomain1.equals(`codomain2)) {
                            Grammar grammar=`Grammar(concProduction(y*,production));
                            return `concGrammar(grammar,grammarList*);
                        } else {
                            GrammarList grammarList2=addToGrammarList(production,`z);
                            return `concGrammar(grammarList2*,x);
                        }
                    }
                }
            }
            // We don't handle hooks for now
            Hook[] -> {
            }
        }

        return `concGrammar();
    }

    private static void printSortedGrammarList(GrammarList grammarList) {
        %match(grammarList) {
            concGrammar() -> {
            }
            concGrammar(Grammar(x),y*) -> {
                printSortedProductionList(`x);
                printSortedGrammarList(`y);
            }
        }
    }

    private static void printSortedProductionList(ProductionList productionList) {
        %match(productionList) {
            concProduction(Production(_,_,GomType(name)),_*) -> {
                System.out.println("");
                System.out.println(`name+" =");
                System.out.print("   ");
                printSortedProductionList2(productionList);
            }
        }
    }
    
    private static void printSortedProductionList2(ProductionList productionList) {
        %match(productionList) {
            concProduction() -> {
            }
            concProduction(Production(name,domain,_)) -> {
                System.out.print(" "+`name+"(");
                printFieldList(`domain);
                System.out.println(")");
            }
            concProduction(Production(name,domain,GomType(codomain1)),x,y*) -> {
                System.out.print(" "+`name+"(");
                printFieldList(`domain);
                System.out.println(")");
                System.out.print("  |");
                printSortedProductionList2(`concProduction(x,y*));
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

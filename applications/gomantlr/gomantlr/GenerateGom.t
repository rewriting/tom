package gomantlr;

import tom.gom.adt.gom.types.*;

import antlrgrammar.types.*;

import antlrgrammar.antlrrules.types.*;
import antlrgrammar.antlrelement.types.*;

import antlrgrammar.antlrcommons.types.AntlrId;

public class GenerateGom {

    %include { ../antlrgrammar/AntlrGrammar.tom }
    %include { adt/gom/Gom.tom}

    private static class Container {
        public String classPrefix="Gomantlr";
        public String varPrefix="gomantlr";

        public String prefixSeparator="_";
        public String suffixSeparator="_";

        public String grammarName;

        public boolean charUsed=false;
        public boolean stringUsed=false;

        public GomTypeList typeList=`concGomType();
        public GomTypeList lexerTypeList=`concGomType();

        public ProductionList lexerProductionList=`concProduction();
    }
    
    public static GomModule generateGom(AntlrGrammar grammar) {
        Container container=new Container();

        // We're only interested in rules for now.
        %match(grammar) {
            AntlrGrammar(_,AntlrId(name),_,_,_,_,_,rules) -> {
                container.grammarName=`name;
                ProductionList productionList1=generateGom(`rules,container);
                ProductionList productionList2=container.lexerProductionList;

                ImportList importList=null;

                if(container.charUsed) {
                    importList=`concImportedModule(Import(GomModuleName("char")));
                }
                
                if(container.stringUsed) {
                    if(importList==null) {
                        importList=`concImportedModule(Import(GomModuleName("String")));
                    } else {
                        importList=`concImportedModule(importList*,Import(GomModuleName("String")));
                    }
                }
                
                SectionList sectionList=`concSection(Public(concGrammar(Sorts(container.typeList),Grammar(concProduction(productionList1*,productionList2*)))));

                if(importList!=null) {
                    sectionList=`concSection(Imports(importList),sectionList*);
                }

                return
                    `GomModule(
                        GomModuleName(container.classPrefix+container.prefixSeparator+container.grammarName),
                        sectionList);
            }
        }
        
        return null;
    }

    private static ProductionList generateGom(AntlrRules rules,Container container) {
        %match(rules) {
            AntlrRules(x,y*) -> {
                ProductionList productionList1=generateGom(`x,container);
                ProductionList productionList2=generateGom(`y,container);
                return `concProduction(productionList1*,productionList2*);
            }
        }

        return `concProduction();
    }

    private static ProductionList generateGom(AntlrRule rule,Container container) {
        %match(rule) {
            AntlrRule(AntlrId(name),_,_,_,_,_,_,element,_) -> {
                String radix=container.grammarName+container.suffixSeparator+`name;
                String type=container.classPrefix+container.prefixSeparator+radix;

                GomTypeList typeList=container.typeList;
                container.typeList=`concGomType(GomType(type),typeList*);

                return generateElementGom(`element,`name,type,container);
            }
        }

        return `concProduction();
    }

    private static ProductionList generateElementGom(AntlrElement element,String ruleName,String ruleType,Container container) {
        Production production;
        Field field;
        String radix;
        String type;

        %match(element) {
            AntlrBlock(_,_,x) -> {
                return generateElementGom(`x,ruleName,ruleType,container);
            }
            AntlrOptional[] -> {
                return generateOptionalGom(element,ruleName,ruleType,container);
            }
            AntlrClosure[] -> {
                return generateClosureGom(element,ruleName,ruleType,container);
            }
            AntlrPositiveClosure[] -> {
                return generatePositiveClosureGom(element,ruleName,ruleType,container);
            }
            AntlrOrElement(_*) -> {
		return generateOrElementGom(element,ruleName,ruleType,container);
            }
            AntlrAndElement(_*) -> {
		return generateAndElementGom(element,ruleName,ruleType,container);
            }
            AntlrNotElement[] -> {
                return generateNotElementGom(element,ruleName,ruleType,container);
            }
            AntlrRuleRef(name) -> {
                radix=container.grammarName+container.suffixSeparator+`name;
                type=container.classPrefix+container.prefixSeparator+radix;

                field=
                    `NamedField(
                         container.varPrefix+container.prefixSeparator+radix,
                         GomType(type));
                                  
                production=`Production(ruleType,concField(field),GomType(ruleType));

                return `concProduction(production);
            }
            AntlrToken(name) -> {
                radix=container.grammarName+container.suffixSeparator+`name;
                type=container.classPrefix+container.prefixSeparator+radix;

                field=
                    `NamedField(
                         container.varPrefix+container.prefixSeparator+radix,
                         GomType(type));
                                  
                addUsedToken(type,container);

                production=`Production(ruleType,concField(field),GomType(ruleType));

                return `concProduction(production);
            }
            AntlrStringElement[] -> {
                production=`Production(ruleType,concField(),GomType(ruleType));
                
                return `concProduction(production);
            }
            AntlrCharElement[] -> {
                production=`Production(ruleType,concField(),GomType(ruleType));

                return `concProduction(production);
            }
            AntlrCharRange[] -> {
                production=`Production(ruleType,concField(),GomType(ruleType));

                return `concProduction(production);
            }
            AntlrActionElement[] -> {
                // just ignore
                return `concProduction();
            }
            AntlrWildcard() -> {
                field=`NamedField("c",GomType("char"));

                container.charUsed=true;
                                  
                production=`Production(ruleType,concField(field),GomType(ruleType));

                return `concProduction(production);
            }
            AntlrEpsilon() -> {
                production=`Production(ruleType,concField(),GomType(ruleType));

                return `concProduction(production);
            }
        }

        System.out.println("Unexpected element: "+element);
        return `concProduction();
    }

    private static ProductionList generateAndElementGom(AntlrElement element,String ruleName,String ruleType,Container container) {
        return generateAndElementGom(element,ruleName,ruleType,`concField(),`concProduction(),container);
    }

    private static ProductionList generateAndElementGom(AntlrElement element,String ruleName,String ruleType,FieldList fieldList,ProductionList productionList,Container container) {
        %match(element) {
            AntlrAndElement() -> {
                Production production=`Production(ruleType,fieldList,GomType(ruleType));

                return `concProduction(production,productionList*);
            }
            AntlrAndElement(x,y*) -> {
                return generateAndSubElementGom(`x,`y,ruleName,ruleType,fieldList,productionList,container);
            }
        }

        System.out.println("Unexpected AndElement: "+element);
        return `concProduction();
    }

    private static ProductionList generateAndSubElementGom(AntlrElement subElement,AntlrElement rest,String ruleName,String ruleType,FieldList fieldList,ProductionList productionList,Container container) {
        int num=fieldList.length()+1;

        ProductionList productionList2;
        
        Field field;
        String radix;
        String type;
        
        %match(subElement) {
            AntlrBlock(_,_,x) -> {
                return generateAndSubElementGom(`x,rest,ruleName,ruleType,fieldList,productionList,container);
            }
            AntlrActionElement[] -> {
                return generateAndElementGom(rest,ruleName,ruleType,fieldList,productionList,container);
            }
            AntlrStringElement[] -> {
                return generateAndElementGom(rest,ruleName,ruleType,fieldList,productionList,container);
            }
            AntlrCharElement[] -> {
                return generateAndElementGom(rest,ruleName,ruleType,fieldList,productionList,container);
            }
            AntlrCharRange[] -> {
                return generateAndElementGom(rest,ruleName,ruleType,fieldList,productionList,container);
            }
            AntlrRuleRef(name) -> {
                String nameRadix=container.grammarName+container.suffixSeparator+ruleName;
                String typeRadix=container.grammarName+container.suffixSeparator+`name;
                type=container.classPrefix+container.prefixSeparator+typeRadix;
                
                field=
                    `NamedField(
                        container.varPrefix+container.prefixSeparator+nameRadix+container.suffixSeparator+num,
                        GomType(type));
                
                return generateAndElementGom(rest,ruleName,ruleType,`concField(fieldList*,field),productionList,container);
            }
            AntlrToken(name) -> {
                String nameRadix=container.grammarName+container.suffixSeparator+ruleName;
                String typeRadix=container.grammarName+container.suffixSeparator+`name;
                type=container.classPrefix+container.prefixSeparator+typeRadix;
            
                field=
                    `NamedField(
                        container.varPrefix+container.prefixSeparator+nameRadix+container.suffixSeparator+num,
                        GomType(type));
            
                addUsedToken(type,container);
                
                return generateAndElementGom(rest,ruleName,ruleType,`concField(fieldList*,field),productionList,container);
            }
            AntlrWildcard() -> {
                radix=container.grammarName+container.suffixSeparator+ruleName;

                field=
		    `NamedField(
		        container.varPrefix+container.prefixSeparator+radix+container.suffixSeparator+num,
		        GomType("char"));
                        
                container.charUsed=true;
                
                return generateAndElementGom(rest,ruleName,ruleType,`concField(fieldList*,field),productionList,container);
            }
        }
        
        // java is unhappy with "_ ->"

        radix=container.grammarName+container.suffixSeparator+ruleName+container.suffixSeparator+num;
        type=container.classPrefix+container.prefixSeparator+radix;
        
        GomTypeList typeList=container.typeList;
        container.typeList=`concGomType(GomType(type),typeList*);
                
        field=
            `NamedField(
                container.varPrefix+container.prefixSeparator+radix,
                GomType(type));
                
        productionList2=generateElementGom(subElement,ruleName+container.suffixSeparator+num,type,container);
        return generateAndElementGom(rest,ruleName,ruleType,`concField(fieldList*,field),`concProduction(productionList*,productionList2*),container);
    }

    private static ProductionList generateOrElementGom(AntlrElement element,String ruleName,String ruleType,Container container) {
        return generateOrElementGom(element,ruleName,ruleType,`concProduction(),1,container);
    }

    private static ProductionList generateOrElementGom(AntlrElement element,String ruleName,String ruleType,ProductionList productionList,int num,Container container) {
        %match(element) {
            AntlrOrElement() -> {
                return productionList;
            }
            AntlrOrElement(x,y*) -> {
                return generateOrSubElementGom(`x,`y,ruleName,ruleType,productionList,num,container);
            }
        }

        System.out.println("Unexpected OrElement: "+element);
        return `concProduction();
    }

    private static ProductionList generateOrSubElementGom(AntlrElement subElement,AntlrElement rest,String ruleName,String ruleType,ProductionList productionList,int num,Container container) {
        String radix;
        String type;
        Field field;
        Production production;
        ProductionList productionList2;

        %match(subElement) {
            AntlrBlock(_,_,x) -> {
                return generateOrSubElementGom(`x,rest,ruleName,ruleType,productionList,num,container);
            }
            AntlrActionElement[] -> {
                // just ignore
                return generateOrElementGom(rest,ruleName,ruleType,productionList,num,container);
            }
            AntlrStringElement[] -> {
                production=`Production(ruleType+container.suffixSeparator+num,concField(),GomType(ruleType));

                productionList2=generateOrElementGom(rest,ruleName,ruleType,productionList,num+1,container);

                return `concProduction(production,productionList2*);
            }
            AntlrCharElement[] -> {
                production=`Production(ruleType+container.suffixSeparator+num,concField(),GomType(ruleType));

                productionList2=generateOrElementGom(rest,ruleName,ruleType,productionList,num+1,container);

                return `concProduction(production,productionList2*);
            }
            AntlrCharRange[] -> {
                production=`Production(ruleType+container.suffixSeparator+num,concField(),GomType(ruleType));

                productionList2=generateOrElementGom(rest,ruleName,ruleType,productionList,num+1,container);

                return `concProduction(production,productionList2*);
            }
            AntlrEpsilon() -> {
                production=`Production(ruleType+container.suffixSeparator+num,concField(),GomType(ruleType));

                productionList2=generateOrElementGom(rest,ruleName,ruleType,productionList,num+1,container);

                return `concProduction(production,productionList2*);
            }
            AntlrRuleRef(name) -> {
                radix=container.grammarName+container.suffixSeparator+`name;
                type=container.classPrefix+container.prefixSeparator+radix;

                field=
                    `NamedField(
                         container.varPrefix+container.prefixSeparator+radix,
                         GomType(type));
                                  
                production=`Production(ruleType+container.suffixSeparator+num,concField(field),GomType(ruleType));

                productionList2=generateOrElementGom(rest,ruleName,ruleType,productionList,num+1,container);

                return `concProduction(production,productionList2*);
            }
            AntlrToken(name) -> {
                radix=container.grammarName+container.suffixSeparator+`name;
                type=container.classPrefix+container.prefixSeparator+radix;

                field=
                    `NamedField(
                         container.varPrefix+container.prefixSeparator+radix,
                         GomType(type));
                                  
                addUsedToken(type,container);

                production=`Production(ruleType+container.suffixSeparator+num,concField(field),GomType(ruleType));

                productionList2=generateOrElementGom(rest,ruleName,ruleType,productionList,num+1,container);

                return `concProduction(production,productionList2*);
            }
            AntlrWildcard() -> {
                field=`NamedField("c",GomType("char"));

                container.charUsed=true;
                                  
                production=`Production(ruleType+container.suffixSeparator+num,concField(field),GomType(ruleType));

                productionList2=generateOrElementGom(rest,ruleName,ruleType,productionList,num+1,container);

                return `concProduction(production,productionList2*);
            }
        }

        // java is unhappy with "_ ->"

        radix=container.grammarName+container.suffixSeparator+ruleName+container.suffixSeparator+num;

        type=container.classPrefix+container.prefixSeparator+radix+container.suffixSeparator+"1";

        GomTypeList typeList=container.typeList;
        container.typeList=`concGomType(GomType(type),typeList*);

        field=
            `NamedField(
                container.varPrefix+container.prefixSeparator+radix+container.suffixSeparator+"1",
                GomType(type));

        production=`Production(ruleType+container.suffixSeparator+num,concField(field),GomType(ruleType));
        
        ProductionList productionList1=generateElementGom(subElement,ruleName+container.suffixSeparator+num+container.suffixSeparator+"1",type,container);
        productionList2=generateOrElementGom(rest,ruleName,ruleType,`concProduction(productionList*,productionList1*),num+1,container);

        return `concProduction(production,productionList2*);
    }

    private static ProductionList generateOptionalGom(AntlrElement element,String ruleName,String ruleType,Container container) {
        %match(element) {
            AntlrOptional(x) -> {
                return generateOptionalElementGom(`x,ruleName,ruleType,container);
            }
        }

        System.out.println("Unexpected Optional: "+element);
        return `concProduction();
    }

    private static ProductionList generateOptionalElementGom(AntlrElement element,String ruleName,String ruleType,Container container) {
        String radix;
        String type;
        Field field;
        Production production1;
        Production production2;

        %match(element) {
            AntlrBlock(_,_,x) -> {
                return generateOptionalElementGom(`x,ruleName,ruleType,container);
            }
            AntlrActionElement[] -> {
                // Just ignore, but should not happen.
                return `concProduction();
            }
            AntlrStringElement[] -> {
                production1=`Production(ruleType+container.suffixSeparator+"1",concField(),GomType(ruleType));
                production2=`Production(ruleType+container.suffixSeparator+"2",concField(),GomType(ruleType));
        
                return `concProduction(production1,production2);
            }
            AntlrCharElement[] -> {
                production1=`Production(ruleType+container.suffixSeparator+"1",concField(),GomType(ruleType));
                production2=`Production(ruleType+container.suffixSeparator+"2",concField(),GomType(ruleType));
        
                return `concProduction(production1,production2);
            }
            AntlrCharRange[] -> {
                production1=`Production(ruleType+container.suffixSeparator+"1",concField(),GomType(ruleType));
                production2=`Production(ruleType+container.suffixSeparator+"2",concField(),GomType(ruleType));
        
                return `concProduction(production1,production2);
            }
            AntlrRuleRef(name) -> {
                radix=container.grammarName+container.suffixSeparator+`name;
                type=container.classPrefix+container.prefixSeparator+radix;

                field=
                    `NamedField(
                         container.varPrefix+container.prefixSeparator+radix,
                         GomType(type));
                                  
                production1=`Production(ruleType+container.suffixSeparator+"1",concField(field),GomType(ruleType));
                production2=`Production(ruleType+container.suffixSeparator+"2",concField(),GomType(ruleType));
        
                return `concProduction(production1,production2);
            }
            AntlrToken(name) -> {
                radix=container.grammarName+container.suffixSeparator+`name;
                type=container.classPrefix+container.prefixSeparator+radix;

                field=
                    `NamedField(
                         container.varPrefix+container.prefixSeparator+radix,
                         GomType(type));
                                  
                addUsedToken(type,container);

                production1=`Production(ruleType+container.suffixSeparator+"1",concField(field),GomType(ruleType));
                production2=`Production(ruleType+container.suffixSeparator+"2",concField(),GomType(ruleType));
        
                return `concProduction(production1,production2);
            }
            AntlrWildcard() -> {
                field=`NamedField("c",GomType("char"));

                container.charUsed=true;
                                  
                production1=`Production(ruleType+container.suffixSeparator+"1",concField(field),GomType(ruleType));
                production2=`Production(ruleType+container.suffixSeparator+"2",concField(),GomType(ruleType));
        
                return `concProduction(production1,production2);
            }
        }

        // java is unhappy with "_ ->"

        radix=container.grammarName+container.suffixSeparator+ruleName+container.suffixSeparator+"1";
        
        type=container.classPrefix+container.prefixSeparator+radix+container.suffixSeparator+"1";
        
        GomTypeList typeList=container.typeList;
        container.typeList=`concGomType(GomType(type),typeList*);
        
        field=
            `NamedField(
                container.varPrefix+container.prefixSeparator+radix+container.suffixSeparator+"1",
                GomType(type));
        
        production1=`Production(ruleType+container.suffixSeparator+"1",concField(field),GomType(ruleType));
        production2=`Production(ruleType+container.suffixSeparator+"2",concField(),GomType(ruleType));
        
        ProductionList productionList1=`concProduction(production1,production2);
        
        ProductionList productionList2=generateElementGom(element,ruleName+container.suffixSeparator+"1"+container.suffixSeparator+"1",type,container);
        return `concProduction(productionList1*,productionList2*);
    }

    private static ProductionList generateClosureGom(AntlrElement element,String ruleName,String ruleType,Container container) {
        %match(element) {
            AntlrClosure(x) -> {
                return generateClosureElementGom(`x,ruleName,ruleType,container);
            }
        }

        System.out.println("Unexpected Closure: "+element);
        return `concProduction();
    }

    private static ProductionList generateClosureElementGom(AntlrElement element,String ruleName,String ruleType,Container container) {
        String radix;
        String type;
        Field field;
        Production production;

        %match(element) {
            AntlrBlock(_,_,x) -> {
                return generateClosureElementGom(`x,ruleName,ruleType,container);
            }
            AntlrActionElement[] -> {
                // Just ignore, but should not happen.
                return `concProduction();
            }
            AntlrStringElement[] -> {
                production=`Production(ruleType+container.suffixSeparator+"1",concField(),GomType(ruleType));

                return `concProduction(production);
            }
            AntlrCharElement[] -> {
                production=`Production(ruleType+container.suffixSeparator+"1",concField(),GomType(ruleType));

                return `concProduction(production);
            }
            AntlrCharRange[] -> {
                production=`Production(ruleType+container.suffixSeparator+"1",concField(),GomType(ruleType));

                return `concProduction(production);
            }
            AntlrRuleRef(name) -> {
                radix=container.grammarName+container.suffixSeparator+`name;
                type=container.classPrefix+container.prefixSeparator+radix;

                field=
                    `StarredField(GomType(type));
                                  
                production=`Production(ruleType+container.suffixSeparator+"1",concField(field),GomType(ruleType));
        
                return `concProduction(production);
            }
            AntlrToken(name) -> {
                radix=container.grammarName+container.suffixSeparator+`name;
                type=container.classPrefix+container.prefixSeparator+radix;

                field=
                    `StarredField(GomType(type));

                addUsedToken(type,container);

                production=`Production(ruleType+container.suffixSeparator+"1",concField(field),GomType(ruleType));
                
                return `concProduction(production);
             }
            AntlrWildcard() -> {
                field=`StarredField(GomType("char"));

                container.charUsed=true;
                                  
                production=`Production(ruleType+container.suffixSeparator+"1",concField(field),GomType(ruleType));
        
                return `concProduction(production);
            }
        }

        // java is unhappy with "_ ->"

        radix=container.grammarName+container.suffixSeparator+ruleName+container.suffixSeparator+"1";

        type=container.classPrefix+container.prefixSeparator+radix+container.suffixSeparator+"1";

        GomTypeList typeList=container.typeList;
        container.typeList=`concGomType(GomType(type),typeList*);

        field=
            `StarredField(GomType(type));

        production=`Production(ruleType+container.suffixSeparator+"1",concField(field),GomType(ruleType));

        ProductionList productionList1=`concProduction(production);
        ProductionList productionList2=generateElementGom(element,ruleName+container.suffixSeparator+"1"+container.suffixSeparator+"1",type,container);

        return `concProduction(productionList1*,productionList2*);
    }

    private static ProductionList generatePositiveClosureGom(AntlrElement element,String ruleName,String ruleType,Container container) {
        // We don't have "foo+" in gom, thus we do "foo*"
        %match(element) {
            AntlrPositiveClosure(x) -> {
                return generateClosureElementGom(`x,ruleName,ruleType,container);
            }
        }

        System.out.println("Unexpected PositiveClosure: "+element);
        return `concProduction();
    }

    private static ProductionList generateNotElementGom(AntlrElement element,String ruleName,String ruleType,Container container) {
        %match(element) {
            AntlrNotElement(x) -> {
                return generateNotSubElementGom(`x,ruleName,ruleType,container);
            }
        }

        System.out.println("Unexpected NotElement: "+element);
        return `concProduction();
    }

    private static ProductionList generateNotSubElementGom(AntlrElement element,String ruleName,String ruleType,Container container) {
        String radix;
        String type;
        Field field;
        Production production;

        %match(element) {
            AntlrBlock(_,_,x) -> {
                return generateNotSubElementGom(`x,ruleName,ruleType,container);
            }
            AntlrActionElement[] -> {
                // Just ignore, but should not happen.
                return `concProduction();
            }
            AntlrStringElement[] -> {
                production=`Production(ruleType+container.suffixSeparator+"1",concField(),GomType(ruleType));

                return `concProduction(production);
            }
            AntlrCharElement[] -> {
                production=`Production(ruleType+container.suffixSeparator+"1",concField(),GomType(ruleType));

                return `concProduction(production);
            }
            AntlrCharRange[] -> {
                production=`Production(ruleType+container.suffixSeparator+"1",concField(),GomType(ruleType));

                return `concProduction(production);
            }
            AntlrRuleRef(name) -> {
                radix=container.grammarName+container.suffixSeparator+`name;
                type=container.classPrefix+container.prefixSeparator+radix;

                field=
                    `NamedField(
                         container.varPrefix+container.prefixSeparator+radix,
                         GomType(type));
                                  
                production=`Production(ruleType+container.suffixSeparator+"1",concField(field),GomType(ruleType));
        
                return `concProduction(production);
            }
            AntlrToken(name) -> {
                radix=container.grammarName+container.suffixSeparator+`name;
                type=container.classPrefix+container.prefixSeparator+radix;

                field=
                    `NamedField(
                         container.varPrefix+container.prefixSeparator+radix,
                         GomType(type));

                addUsedToken(type,container);

                production=`Production(ruleType+container.suffixSeparator+"1",concField(field),GomType(ruleType));
                
                return `concProduction(production);
             }
            AntlrWildcard() -> {
                field=`NamedField("c",GomType("char"));

                container.charUsed=true;
                                  
                production=`Production(ruleType+container.suffixSeparator+"1",concField(field),GomType(ruleType));
        
                return `concProduction(production);
            }
        }

        // java is unhappy with "_ ->"

        radix=container.grammarName+container.suffixSeparator+ruleName+container.suffixSeparator+"1";

        type=container.classPrefix+container.prefixSeparator+radix+container.suffixSeparator+"1";

        GomTypeList typeList=container.typeList;
        container.typeList=`concGomType(GomType(type),typeList*);

        field=
            `NamedField(
                container.varPrefix+container.prefixSeparator+radix+container.suffixSeparator+"1",
                GomType(type));

        production=`Production(ruleType+container.suffixSeparator+"1",concField(field),GomType(ruleType));
        
        ProductionList productionList2=generateElementGom(element,ruleName+container.suffixSeparator+"1"+container.suffixSeparator+"1",type,container);

        return `concProduction(production,productionList2*);
    }

    private static void addUsedToken(String type,Container container) {
        ProductionList productionList=container.lexerProductionList;

        %match(productionList) {
            concProduction(_*,Production(name,_,_),_*) -> {
                if(`name.equals(type)) {
                    return;
                }
            }
        }

        container.stringUsed=true;

        Field field=`NamedField("s",GomType("String"));
        Production production=`Production(type,concField(field),GomType(type));

        GomTypeList typeList=container.lexerTypeList;
        container.typeList=`concGomType(GomType(type),typeList*);

        container.lexerProductionList=`concProduction(productionList*,production);
    }
}

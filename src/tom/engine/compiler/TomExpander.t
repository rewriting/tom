package jtom.compiler;

import java.util.logging.*;

import jtom.*;
import jtom.adt.tomsignature.types.*;
import tom.platform.adt.platformoption.types.*;
import tom.library.traversal.Replace1;
import aterm.*;
import jtom.tools.*;
import jtom.xml.Constants;
import jtom.exception.TomRuntimeException;

/**
 * The TomExpander plugin.
 */
public class TomExpander extends TomGenericPlugin {

  %include { adt/TomSignature.tom }
  %include { adt/PlatformOption.tom }

  private TomKernelExpander tomKernelExpander;
  private TomFactory tomFactory;

  public static final String EXPANDED_SUFFIX       = ".tfix.expanded";
  public static final String EXPANDED_TABLE_SUFFIX = ".tfix.expanded.table";
  
  public TomExpander() {
    super("TomExpander");
    tomKernelExpander = new TomKernelExpander();
    tomFactory = new TomFactory();
  }

  public void run() {
    try {
      int errorsAtStart = getPluginPlatform().getStatusHandler().nbOfErrors();
      int warningsAtStart = getPluginPlatform().getStatusHandler().nbOfWarnings();

      long startChrono = System.currentTimeMillis();
      boolean intermediate = getPluginPlatform().getOptionBooleanValue("intermediate");

      TomTerm syntaxExpandedTerm   = expandTomSyntax( (TomTerm)getTerm() );
      
      tomKernelExpander.updateSymbolTable();
      TomTerm context = `emptyTerm();
      
      TomTerm variableExpandedTerm = expandVariable(context, syntaxExpandedTerm);
      TomTerm stringExpandedTerm   = expandString(variableExpandedTerm);
      TomTerm expandedTerm         = updateCodomain(stringExpandedTerm);

      setTerm(expandedTerm);

      getLogger().log( Level.INFO,
		       "TomExpandingPhase",
		       new Integer((int)(System.currentTimeMillis()-startChrono)) );
      
      if(intermediate) {
	      Tools.generateOutput(environment().getOutputFileNameWithoutSuffix()
                             + EXPANDED_SUFFIX, expandedTerm);
	      Tools.generateOutput(environment().getOutputFileNameWithoutSuffix()
                             + EXPANDED_TABLE_SUFFIX, symbolTable().toTerm());
      }

      printAlertMessage(errorsAtStart, warningsAtStart);
    } catch (Exception e) {
	getLogger().log( Level.SEVERE,
			 "ExceptionMessage",
			 new Object[]{environment().getInputFile().getName(), "TomExpander", e.getMessage()} );

	e.printStackTrace();
    }
  }

  public PlatformOptionList declaredOptions() {
    return `concPlatformOption(OptionBoolean("expand","","",True()) // activationFlag
			  );
  }



  private TomTerm expandVariable(TomTerm contextSubject, TomTerm subject) {
    return tomKernelExpander.expandVariable(contextSubject,subject); 
  }
 
  /*
   * The 'expandTomSyntax' phase replaces:
   * - each 'RecordAppl' by its expanded term form:
   *   (unused slots a replaced by placeholders)
   * - each BackQuoteTerm by its compiled form
   */
  
  public TomTerm expandTomSyntax(TomTerm subject) {
    Replace1 replace = new Replace1() { 
        public ATerm apply(ATerm subject) {
          if(subject instanceof TomTerm) {
            %match(TomTerm subject) {
              DoubleBackQuote(concTomTerm(context*,backQuoteTerm)) -> {
                if(!`context.isEmpty()) {
                  `context = aggregateContext(`context);
                  //System.out.println("context = " + `context);
                }
                TomTerm t = expandTomSyntax(`backQuoteTerm);
                t = expandBackQuoteXMLAppl(`context,t);
                return t;
              }

              backQuoteTerm@BackQuoteAppl[] -> {
                TomTerm t = expandBackQuoteAppl(`backQuoteTerm);
                  //System.out.println("t = " + t);
                return t;
              }
              
              RecordAppl[option=option,nameList=nameList,args=args,constraints=constraints] -> {
                return expandRecordAppl(`option,`nameList,`args,`constraints);
              }

              XMLAppl[option=optionList,nameList=nameList,attrList=list1,childList=list2,constraints=constraints] -> {
                //System.out.println("expandXML in:\n" + subject);
                return expandXMLAppl(`optionList, `nameList, `list1, `list2,`constraints);
              }
              
              _ -> {
                return traversal().genericTraversal(subject,this);
              }
            } // end match
          } else {
            return traversal().genericTraversal(subject,this);
          }
        } // end apply
      }; // end new

    return (TomTerm) replace.apply(subject);
  }

  /*
   * this post-processing phase replaces untyped (universalType) codomain
   * by their precise type (according to the symbolTable)
   */
  private TomTerm updateCodomain(TomTerm subject) {
    Replace1 replace = new Replace1() { 
        public ATerm apply(ATerm subject) {
          if(subject instanceof Declaration) {
            %match(Declaration subject) {
              GetHeadDecl[variable=Variable[astType=domain]] -> {
                TomSymbol tomSymbol = getSymbol(`domain);
                if(tomSymbol != null) {
                  TomTypeList codomain = getSymbolDomain(tomSymbol);
                  //System.out.println("tomSymbol = " + tomSymbol);
                  //System.out.println("domain    = " + domain);
                  //System.out.println("codomain  = " + codomain);
                  
                  if(codomain.isSingle()) {
                    Declaration t = (Declaration)subject;
                    t = t.setCodomain(codomain.getHead());
                    return t;
                  } else {
                    throw new TomRuntimeException("updateCodomain: bad codomain: " + codomain);
                  }
                }
              }

              // default rule
              _ -> {
                return traversal().genericTraversal(subject,this);
              }
            } // end match
          } else {
            // not instance of Declaration
            return traversal().genericTraversal(subject,this);
          }

        } // end apply
      }; // end new

    return (TomTerm) replace.apply(subject);
  }

  /*
   * replace 'abc' by conc('a','b','c')
   */
  private TomTerm expandString(TomTerm subject) {
    Replace1 replace = new Replace1() { 
        public ATerm apply(ATerm subject) {
          if(subject instanceof TomTerm) {
            %match(TomTerm subject) {
              appl@Appl[nameList=(Name(tomName),_*),args=args] -> {
                TomSymbol tomSymbol = getSymbol(`tomName);
                //System.out.println("appl = " + subject);
                if(tomSymbol != null) {
                  if(isListOperator(tomSymbol) || isArrayOperator(tomSymbol)) {
                    //System.out.println("appl = " + subject);
                    TomList newArgs = expandChar(`args);
                    if(newArgs!=`args) {
                      return `appl.setArgs(newArgs);
                    }
                  }
                }
              }

              // default rule
              _ -> {
                return traversal().genericTraversal(subject,this);
              }
            } // end match
          } else {
            // not instance of Declaration
            return traversal().genericTraversal(subject,this);
          }

        } // end apply
      }; // end new

    return (TomTerm) replace.apply(subject);
  }

  /*
   * detect ill-formed char: 'abc'
   * and expand it into a list of char: 'a','b','c'
   */
  private TomList expandChar(TomList args) {
    if(args.isEmpty()) {
      return args;
    } else {
      TomTerm head = args.getHead();
      TomList tail = expandChar(args.getTail());
      %match(TomTerm head) {
        Appl[nameList=(Name(tomName)),args=()] -> {
          /*
           * ensure that the argument contains at least 1 character and 2 single quotes
           */
          TomSymbol tomSymbol = getSymbol(`tomName);
          TomType termType = tomSymbol.getTypesToType().getCodomain();
          String type = termType.getTomType().getString();
          if(symbolTable().isCharType(type) && `tomName.length()>3) {
            if(`tomName.charAt(0)=='\'' && `tomName.charAt(`tomName.length()-1)=='\'') {
              TomList newArgs = tail;
              //System.out.println("bingo -> " + tomSymbol);
              for(int i=`tomName.length()-2 ; i>0 ;  i--) {
                char c = `tomName.charAt(i);
                String newName = "'" + c + "'";
                TomSymbol newSymbol = tomSymbol.setAstName(`Name(newName));
                newSymbol = newSymbol.setTlCode(`ITL(newName));
                symbolTable().putSymbol(newName,newSymbol);
                TomTerm newHead = head.setNameList(`concTomName(Name(newName)));
                newArgs = `manyTomList(newHead,newArgs);
                //System.out.println("newHead = " + newHead);
                //System.out.println("newSymb = " + getSymbol(newName));
              }
              return newArgs;
            } else {
              throw new TomRuntimeException("expandChar: strange char: " + `tomName);
            }
          }
        }

        _ -> {
          return `manyTomList(head,tail);
        }
      }

    }
  }

  protected TomTerm expandRecordAppl(OptionList option, NameList nameList, TomList args, ConstraintList constraints) {
    TomSymbol tomSymbol = getSymbol(nameList.getHead().getString());
    SlotList slotList = tomSymbol.getSlotList();
    TomList subtermList = empty();
      // For each slotName (from tomSymbol)
    while(!slotList.isEmpty()) {
      TomName slotName = slotList.getHead().getSlotName();
      TomList pairList = args;
      TomTerm newSubterm = null;
      if(!slotName.isEmptyName()) {
          // look for a same name (from record)
        whileBlock: {
          while(newSubterm==null && !pairList.isEmpty()) {
            TomTerm pairSlotName = pairList.getHead();
            %match(TomName slotName, TomTerm pairSlotName) {
              Name[string=name], PairSlotAppl(Name[string=name],slotSubterm) -> {
                  // bingo
                newSubterm = expandTomSyntax(`slotSubterm);
                break whileBlock;
              }
              _ , _ -> {pairList = pairList.getTail();}
            }
          }
        } // end whileBlock
      }
      
      if(newSubterm == null) {
        newSubterm = `Placeholder(emptyOption(),concConstraint());
      }
      subtermList = append(newSubterm,subtermList);
      slotList = slotList.getTail();
    }
    
    return `Appl(option,nameList,subtermList,constraints);
  }

  protected TomTerm expandBackQuoteAppl(TomTerm t) {
    Replace1 replaceSymbol = new Replace1() {
        public ATerm apply(ATerm t) {
          if(t instanceof TomTerm) {
            %match(TomTerm t) {
              BackQuoteAppl[option=optionList,astName=name@Name(tomName),args=l] -> {
                TomSymbol tomSymbol = getSymbol(`tomName);
                TomList args  = (TomList) traversal().genericTraversal(`l,this);
                
                //System.out.println("BackQuoteTerm: " + `tomName);
                //System.out.println("tomSymbol: " + tomSymbol);

                if(tomSymbol != null) {
                  if(isListOperator(tomSymbol)) {
                    return tomFactory.buildList(`name,args);
                  } else if(isArrayOperator(tomSymbol)) {
                    return tomFactory.buildArray(`name,args);
                  } else if(isStringOperator(tomSymbol)) {
                    return `BuildVariable(name,emptyTomList());
                  } else if(isDefinedSymbol(tomSymbol)) {
                    return `FunctionCall(name,args);
                  } else {
                    return `BuildTerm(name,args);
                  }
                } else if(args.isEmpty() && !hasConstructor(`optionList)) {
                  return `BuildVariable(name,emptyTomList());
                } else {
                  return `FunctionCall(name,args);
                }
              }
            } // end match 
          }
          return traversal().genericTraversal(t,this);
        } // end apply
      }; // end replaceSymbol
    return (TomTerm) replaceSymbol.apply(t);
  }

  private TomList sortAttributeList(TomList attrList) {
    %match(TomList attrList) {
      concTomTerm() -> { return attrList; }
      concTomTerm(X1*,e1,X2*,e2,X3*) -> {
        %match(TomTerm e1, TomTerm e2) {
          Appl[args=manyTomList(Appl[nameList=(Name(name1))],_)],
          Appl[args=manyTomList(Appl[nameList=(Name(name2))],_)] -> {
            if(`name1.compareTo(`name2) > 0) {
              return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
            }
          }

          RecordAppl[args=manyTomList(PairSlotAppl(slotName,Appl[nameList=(Name(name1))]),_)],
          RecordAppl[args=manyTomList(PairSlotAppl(slotName,Appl[nameList=(Name(name2))]),_)] -> {
            if(`name1.compareTo(`name2) > 0) {
              return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
            }
          }

          BackQuoteAppl[args=manyTomList(Appl[nameList=(Name(name1))],_)],
          BackQuoteAppl[args=manyTomList(Appl[nameList=(Name(name2))],_)] -> {
            if(`name1.compareTo(`name2) > 0) {
              return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
            }
          }

          BackQuoteAppl[args=manyTomList(BackQuoteAppl[astName=Name(name1)],_)],
          BackQuoteAppl[args=manyTomList(BackQuoteAppl[astName=Name(name2)],_)] -> {
            if(`name1.compareTo(`name2) > 0) {
              return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
            }
          }
        }
      }
    }
    return attrList;
  }

  private TomList aggregateContext(TomList context) {
    TomList newContext = `concTomTerm();
    %match(TomList context) {
      concTomTerm(_*,Composite(concTomTerm(arg@BackQuoteAppl[])),_*) -> {
        newContext = `concTomTerm(newContext*,arg);
      }
    }
    return newContext;
  }

  private OptionList convertOriginTracking(String name,OptionList optionList) {
    Option originTracking = findOriginTracking(optionList);
    %match(Option originTracking) {
      OriginTracking[line=line, fileName=fileName] -> {
        return `concOption(OriginTracking(Name(name),line,fileName));
      }
    }
    System.out.println("Warning: no OriginTracking information");
    return emptyOption();
  }

  
  protected TomTerm expandXMLAppl(OptionList optionList, NameList nameList,
                                  TomList attrList, TomList childList, ConstraintList constraints) {
    boolean implicitAttribute = hasImplicitXMLAttribut(optionList);
    boolean implicitChild     = hasImplicitXMLChild(optionList);
    
    TomList newAttrList  = `emptyTomList();
    TomList newChildList = `emptyTomList();

    TomTerm star = ast().makeUnamedVariableStar(convertOriginTracking("_*",optionList),"unknown type",`concConstraint());
    if(implicitAttribute) { newAttrList  = `manyTomList(star,newAttrList); }
    if(implicitChild)     { newChildList = `manyTomList(star,newChildList); }

    /*
     * the list of attributes should not be expanded before the sort
     * the sortAttribute is extended to compare RecordAppl
     */
    attrList = sortAttributeList(attrList);

    /*
     * Attributes: go from implicit notation to explicit notation
     */
    while(!attrList.isEmpty()) {
      TomTerm newPattern = expandTomSyntax(attrList.getHead());
      newAttrList = `manyTomList(newPattern,newAttrList);
      if(implicitAttribute) { 
        newAttrList = `manyTomList(star,newAttrList); 
      }
      attrList = attrList.getTail();
    }
    newAttrList = (TomList) newAttrList.reverse();
    
    /*
     * Childs: go from implicit notation to explicit notation
     */
    while(!childList.isEmpty()) {
      TomTerm newPattern = expandTomSyntax(childList.getHead());
      newChildList = `manyTomList(newPattern,newChildList);
      if(implicitChild) {
        if(newPattern.isVariableStar()) {
            // remove the previously inserted pattern
          newChildList = newChildList.getTail();
          if(newChildList.getHead().isUnamedVariableStar()) {
            // remove the previously inserted star
            newChildList = newChildList.getTail();
          }
            // re-insert the pattern
          newChildList = `manyTomList(newPattern,newChildList);
        } else {
          newChildList = `manyTomList(star,newChildList);
        }
      }
      childList = childList.getTail();
    }
    newChildList = (TomList) newChildList.reverse();

      /*
       * encode the name and put it into the table of symbols
       */
    NameList newNameList = `concTomName();
    matchBlock: {
      %match(NameList nameList) {
        (Name("_")) -> {
          break matchBlock;
        }

        (_*,Name(name),_*) -> {
          newNameList = (NameList)newNameList.append(`Name(tomFactory.encodeXMLString(symbolTable(),name)));
        }
      }
    }

      /*
       * a single "_" is converted into a Placeholder to match
       * any XML node
       */
    TomTerm xmlHead;

    if(newNameList.isEmpty()){
      xmlHead = `Placeholder(emptyOption(),concConstraint());
    } else { 
      xmlHead = `Appl(convertOriginTracking(newNameList.getHead().getString(),optionList),newNameList,empty(),concConstraint());
    }

    TomList newArgs = `concTomTerm(
      PairSlotAppl(Name(Constants.SLOT_NAME),xmlHead),
      PairSlotAppl(Name(Constants.SLOT_ATTRLIST),Appl(convertOriginTracking("CONC_TNODE",optionList),concTomName(Name(Constants.CONC_TNODE)), newAttrList,concConstraint())),
      PairSlotAppl(Name(Constants.SLOT_CHILDLIST),Appl(convertOriginTracking("CONC_TNODE",optionList),concTomName(Name(Constants.CONC_TNODE)), newChildList,concConstraint())));
    
    TomTerm result = `expandTomSyntax(RecordAppl(optionList,concTomName(Name(Constants.ELEMENT_NODE)),newArgs,constraints));


      //System.out.println("expandXML out:\n" + result);
    return result;
   
  }

  protected TomTerm expandBackQuoteXMLAppl(final TomList context,TomTerm subject) {
    Replace1 replaceSymbol = new Replace1() {
        public ATerm apply(ATerm t) {
          if(t instanceof TomTerm) {
            %match(TomTerm t) {
              Composite(list) -> {
                `list = parseBackQuoteXMLAppl(context,`list);
                `list = (TomList) traversal().genericTraversal(`list,this);
                return `Composite(list);
              }
            } // end match 
          } 
          return traversal().genericTraversal(t,this);
        } // end apply
      }; // end replaceSymbol
    return (TomTerm) replaceSymbol.apply(subject);
  }

  private TomList parseBackQuoteXMLAppl(TomList context,TomList list) {
    //System.out.println("parseBackQuoteXMLAppl: " + list);
    %match(TomList list) {
      concTomTerm(
        TargetLanguageToTomTerm(ITL("#TEXT")),
        TargetLanguageToTomTerm(ITL("(")),
        value*,
        TargetLanguageToTomTerm(ITL(")")),
        tail*
        ) -> {
        TomTerm newBackQuoteAppl = `BackQuoteAppl(emptyOption(),Name(Constants.TEXT_NODE),concTomTerm(context*,Composite(value*)));
        TomList newTail = parseBackQuoteXMLAppl(context,`tail);
        return `concTomTerm(newBackQuoteAppl,newTail*);
      }

      concTomTerm(
        (BuildVariable|BuildTerm)[astName=name],
        TargetLanguageToTomTerm(ITL("*")),
        tail*
        ) -> {
        TomTerm term = `VariableStar(emptyOption(),name,TomTypeAlone("unknown type"),concConstraint());
        TomList newTail = parseBackQuoteXMLAppl(context,`tail);
        return `concTomTerm(term,newTail*);
      }

      label2:concTomTerm(
        TargetLanguageToTomTerm(ITL("<")),
        (BuildVariable|BuildTerm)[astName=name],
        Attributes*,
        TargetLanguageToTomTerm(ITL(">")),
        Body*,
        TargetLanguageToTomTerm(ITL("<")),
        TargetLanguageToTomTerm(ITL("/")),
        (BuildVariable|BuildTerm)[astName=name],
        TargetLanguageToTomTerm(ITL(">")),
        tail*
        ) -> {
        if(containClosingBracket(`Attributes)) {
          break label2;
        }

          //System.out.println("Attributes = " + Attributes);
          //System.out.println("Body = " + Body);
        
        TomTerm newName = `BackQuoteAppl(emptyOption(),encodeName(name),empty());
        TomTerm newAttribute = metaEncodeTNodeList(aggregateXMLAttribute(context,`Attributes));
        //System.out.println("aggregate: " + newAttribute);

        TomTerm newBody = metaEncodeTNodeList(aggregateXMLBody(context,`Body));

        //System.out.println("newbody = " + newBody);
        //System.out.println("context = " + context);

        TomTerm newBackQuoteAppl = `BackQuoteAppl(emptyOption(),Name(Constants.ELEMENT_NODE),concTomTerm(context*,newName,newAttribute,newBody));
          //System.out.println("newBackQuoteAppl1 = " + newBackQuoteAppl);
        newBackQuoteAppl = expandTomSyntax(newBackQuoteAppl);
          //System.out.println("newBackQuoteAppl2 = " + newBackQuoteAppl);
          //TomList newTail = aggregateXMLBody(context,tail);
        TomList newTail = parseBackQuoteXMLAppl(context,`tail);
        return `concTomTerm(newBackQuoteAppl,newTail*);
      }
      
      label3:concTomTerm(
        TargetLanguageToTomTerm(ITL("<")),
        (BuildVariable|BuildTerm)[astName=name],
        Attributes*,
        TargetLanguageToTomTerm(ITL("/")),
        TargetLanguageToTomTerm(ITL(">")),
        tail*
        ) -> {
        if(containClosingBracket(`Attributes)) {
          break label3;
        }
	//System.out.println("SingleNode(" + name +")");
	//System.out.println("Attributes = " + Attributes);

        TomTerm newName = `BackQuoteAppl(emptyOption(),encodeName(name),empty());
        TomTerm newAttribute = metaEncodeTNodeList(aggregateXMLAttribute(context,`Attributes));
        //System.out.println("aggregate: " + newAttribute);

        TomTerm newBody = metaEncodeTNodeList(`concTomTerm());
        TomTerm newBackQuoteAppl = `BackQuoteAppl(emptyOption(),Name(Constants.ELEMENT_NODE),concTomTerm(context*,newName,newAttribute,newBody));
        //  System.out.println("newBackQuoteAppl1 = " + newBackQuoteAppl);
        newBackQuoteAppl = expandTomSyntax(newBackQuoteAppl);
        // System.out.println("newBackQuoteAppl2 = " + newBackQuoteAppl);
          //TomList newTail = aggregateXMLBody(context,tail);
        TomList newTail = parseBackQuoteXMLAppl(context,`tail);
        return `concTomTerm(newBackQuoteAppl,newTail*);

      }

      concTomTerm() -> {
        return `concTomTerm();
      }

      concTomTerm(head,tail*) -> {
        TomList newTail = parseBackQuoteXMLAppl(context,`tail);
        return `concTomTerm(head,newTail*);
      }

      
    }
    return list;
  }

  private boolean containClosingBracket(TomList list) {
    %match(TomList list) {
      concTomTerm(_*,
		  TargetLanguageToTomTerm(ITL(">")),
		  _*) -> {
        return true;
      }
      concTomTerm(_*,
		  TargetLanguageToTomTerm(ITL("/")),
		  TargetLanguageToTomTerm(ITL(">")),
		  _*) -> {
        return true;
      }
    }
    return false;
  }
  
  private TomTerm metaEncodeTNodeList(TomList list) {
    return `BackQuoteAppl(emptyOption(),Name(Constants.CONC_TNODE),list);
  }
  
  private TomList aggregateXMLBody(TomList context, TomList subjectList) {
    TomTerm composite = expandBackQuoteXMLAppl(context,`Composite(subjectList));
      //System.out.println("composite = " + composite);
    return composite.getArgs();
  }

  private TomName encodeName(TomName name) {
    return `Name("\"" + name.getString() + "\"");
  }
  
  private TomList aggregateXMLAttribute(TomList context,TomList subjectList) {
    TomList list = `concTomTerm();
    %match(TomList subjectList) {
      concTomTerm(
        _*,
        (BuildVariable|BuildTerm)[astName=name],TargetLanguageToTomTerm(ITL("=")),value,
        _*) -> {
          //TomTerm newValue = `BackQuoteAppl(emptyOption(),Name(Constants.TEXT_NODE),concTomTerm(value));
          // no longer necessary to encode string attributes
        TomTerm newValue = `value;
        
        TomList args = `concTomTerm(
          context*,
          BackQuoteAppl(emptyOption(),encodeName(name),concTomTerm()),
          BackQuoteAppl(emptyOption(),Name("\"true\""),concTomTerm()),
          newValue);
        TomTerm attributeNode = `BackQuoteAppl(emptyOption(),
                                               Name(Constants.ATTRIBUTE_NODE),
                                               args);
        list = `manyTomList(attributeNode,list);
      }

      concTomTerm(
        _*,
	      (BuildVariable|BuildTerm)[astName=name],
        TargetLanguageToTomTerm(ITL("*")),
        _*) -> {
        TomTerm attributeNode = `VariableStar(emptyOption(),name,TomTypeAlone("unknown type"),concConstraint());
        list = `manyTomList(attributeNode,list);
      }
    }

    //list = (TomList) sortAttributeList(list).reverse();
    list = sortAttributeList(list);
    return list;
  }

}

package jtom.parser;

import java.util.*;

import jtom.*;
import jtom.adt.tomsignature.types.*;

import aterm.*;

import antlr.*;

public class NewTomBackQuoteParser 
    extends TomBase 
    implements jtom.parser.NewBQParserTokenTypes, jtom.parser.NewTomParserTokenTypes {
 
    %include{../adt/TomSignature.tom}

    %typeterm AntlrToken {
	implement {antlr.Token}
	get_fun_sym(t) { null }
	cmp_fun_sym(s1,s2) { false }
	get_subterm(t,n) { null }
	equals(t1,t2) { t1.equals(t2) }
    }

    %op AntlrToken LPAREN(name:String,line:int) {
	fsym { }
	is_fsym(t) { (t!=null) && (t.getType() == LPAREN || t.getType() == BQ_LPAREN) }
	get_slot(name,t) { t.getText() }
	get_slot(line,t) { t.getLine() }
    }

    %op AntlrToken RPAREN(name:String,line:int) {
	fsym { }
	is_fsym(t) { (t!=null) && t.getType() == BQ_RPAREN}
	get_slot(name,t) { t.getText() }
	get_slot(line,t) { t.getLine() }
    }
    
    %op AntlrToken IDENT(name:String,line:int) {
	fsym { }
	is_fsym(t) { (t!=null) && (t.getType() == ID || t.getType() == BQ_ID) }
	get_slot(name,t) { t.getText() }
	get_slot(line,t) { t.getLine() }
    }
    
    %op AntlrToken COMMA(name:String,line:int) {
	fsym { }
	is_fsym(t) { (t!=null) && t.getType() == BQ_COMMA }
	get_slot(name,t) { t.getText() }
	get_slot(line,t) { t.getLine() }
    }
    
    private static NewTomBackQuoteParser instance = null;
   
    private NewTomBackQuoteParser() {
	super();
    }

    public static NewTomBackQuoteParser getInstance(){
	if(instance == null)
	    instance = new NewTomBackQuoteParser();
	return instance;
    }

    public TomTerm buildBackQuoteAppl(String symbolName, int line, String fileName) {
	OptionList option = `concOption(OriginTracking(Name(symbolName), line, Name(fileName)));
	return `BackQuoteAppl(option,Name(symbolName),concTomTerm());
    }

    public TomTerm buildBackQuoteAppl(LinkedList tokenList, String fileName) {
	antlr.Token t = (antlr.Token) tokenList.getFirst();
	return buildBackQuoteAppl(t.getText(),t.getLine(),fileName);
    }

    public TomTerm buildVariableStar(String symbolName, int line, String fileName) {
	OptionList option = `concOption(OriginTracking(Name(symbolName), line, Name(fileName)));
	return  `VariableStar(option,Name(symbolName),TomTypeAlone("unknown type"),concConstraint());
    }

    public TomTerm buildVariableStar(LinkedList tokenList, String fileName) {
	antlr.Token t = (antlr.Token) tokenList.getFirst();
	return buildVariableStar(t.getText(),t.getLine(),fileName);
    }

    private void addToLastComposite(Stack stackList, TomTerm term) {
	TomList list = (TomList) stackList.pop();
	
	%match(TomList list) {
	    concTomTerm(X1*,Composite(concTomTerm(L*))) -> {
		stackList.push(`concTomTerm(X1*,Composite(concTomTerm(L*,term))));
		return;
	    }
	    
	    _ -> {
		System.out.println("addToLastComposite: error");
	    }
	}
    }

    private void detectVariableStar(Stack stackList) {
	TomList list = (TomList) stackList.peek();
	%match(TomList list) {
	    concTomTerm(X1*,Composite(concTomTerm(L*,
						  BackQuoteAppl(option,
								Name(name),
								concTomTerm()),
						  TargetLanguageToTomTerm(ITL("*"))))) -> {
		TomTerm term = `VariableStar(option,Name(name),TomTypeAlone("unknown type"),concConstraint());
		stackList.pop();
		stackList.push(`concTomTerm(X1*,Composite(concTomTerm(L*,term))));
		return;
	    }
	}
    }
    
    private boolean isEmptyComposite(TomList args) {
	%match(TomList args) {
	    concTomTerm(Composite(concTomTerm())) -> {
		return true;
	    }
	}
	return false;
    }

    private TomTerm buildTermFromStack(Stack stackList, Stack stackName, Stack stackOption) {
	String name = (String) stackName.pop();
	TomList args = (TomList) stackList.pop();
	OptionList option;
	
	if(isEmptyComposite(args)) {
	    option = `concOption( Constructor(concTomName(Name(name))), (Option)stackOption.pop() );
	    args = `concTomTerm();
	} else {
	    option = `concOption((Option)stackOption.pop() );
	}
	
	return `BackQuoteAppl(option,Name(name),args);
    }

    private boolean isWellFormed(Stack stackList) {
	int open = 0;
	TomList listComposite = (TomList) stackList.peek();
	%match(TomList listComposite) {
	    concTomTerm(X1*,Composite(concTomTerm(list*))) -> {
		while(!`list.isEmpty()) {
		    TomTerm term = `list.getHead();
		    matchBlock: {
			%match(TomTerm term) {
			    TargetLanguageToTomTerm(ITL("(")) -> {
				open++;
				break matchBlock;
			    }
			    TargetLanguageToTomTerm(ITL(")")) -> {
				open--;
				break matchBlock;
			    }
			}
		    } // end matchBlock
		    `list = `list.getTail();
		}
		return open==0;
	    }
	    
	    _ -> {
		System.out.println("isWellFormed: error");
	    }
	}
	return open==0;
    }

    private void addNewComposite(Stack stackList) {
	TomList list = (TomList) stackList.pop();
	list = (TomList) list.append(`Composite(concTomTerm()));
	stackList.push(list);
    }

    public TomTerm buildBackQuoteTerm(LinkedList tokenList, String filename) {
	antlr.Token current = null, next = null;
	Stack stackName = new Stack();
	Stack stackList = new Stack();
	Stack stackOption = new Stack();

	stackList.push(`concTomTerm(Composite(concTomTerm())));
	
	Iterator it = tokenList.iterator();
	
	if(it.hasNext()) {
	    next = (antlr.Token)it.next();
	}
	boolean finish = false;
	while(! finish){
	    
	    current = next;
	    if(it.hasNext()) 
		next = (antlr.Token)it.next();
	    else 
		finish = true;

	    matchBlock: {
		%match(AntlrToken current, AntlrToken next) {
		    IDENT(name,line), LPAREN[] -> {
			stackOption.push(`OriginTracking(
							 Name(name), 
							 line, 
							 Name(filename))
					 );
			stackName.push(`name);
			stackList.push(`concTomTerm(Composite(concTomTerm())));
			current = next;
			if(it.hasNext()) 
			    next = (antlr.Token)it.next();
			else {
			    System.out.println("buildBackQuoteTerm: term not correct");
			    finish = true;
			}
			break matchBlock;
		    }

		    IDENT(name,line), _ -> {
			addToLastComposite(
					   stackList,
					   buildBackQuoteAppl(`name,
							      line,
							      filename)
					   );
			
			break matchBlock;
		    }
		    
		    COMMA[], _ -> {
			detectVariableStar(stackList);
			addNewComposite(stackList);
			break matchBlock;
		    }
		    
		    LPAREN(name,line), _ -> {
			addToLastComposite(stackList, `TargetLanguageToTomTerm(ITL(name)));
			break matchBlock;
		    }
		    
		    RPAREN(name,line), _ -> {
			if(isWellFormed(stackList)) {
			    detectVariableStar(stackList);
			    TomTerm term = buildTermFromStack(stackList, stackName, stackOption);
			    addToLastComposite(stackList, term);
			    break matchBlock;
			} else {
			    addToLastComposite(stackList, `TargetLanguageToTomTerm(ITL(name)));
			    break matchBlock;
			}
		    }

		    other, _ -> {
			addToLastComposite(stackList, `TargetLanguageToTomTerm(ITL(other.getText())));
			break matchBlock;
		    }
		}
	    }
	}
	TomTerm term = ((TomList)stackList.pop()).getHead();
	return term;
    }
}

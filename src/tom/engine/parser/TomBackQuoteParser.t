/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2004 INRIA
			    Nancy, France.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr

*/

package jtom.parser;
  
import java.util.*;
import jtom.TomBase;
import jtom.adt.tomsignature.types.*;
import aterm.*;

public class TomBackQuoteParser extends TomBase implements TomParserConstants {
  
  private HashMap nameToDecLineMap;
	
  public TomBackQuoteParser(jtom.TomEnvironment environment) {
    super(environment);
  }
  
// ------------------------------------------------------------
  %include { ../../adt/TomSignature.tom }
// ------------------------------------------------------------

  %typeterm Token {
    implement { Token }
    get_fun_sym(t) { null }
    cmp_fun_sym(s1,s2) { false }
    get_subterm(t,n) { null }
    equals(t1,t2) { t1.equals(t2) }
  }

  %op Token LPAREN(image:String) {
    fsym { }
    is_fsym(t) { (t!=null) && t.kind == TOM_LPAREN }
    get_slot(image,t) { t.image }
  }

  %op Token RPAREN(image:String) {
    fsym { }
    is_fsym(t) { (t!=null) && t.kind == TOM_RPAREN }
    get_slot(image,t) { t.image }
  }

  %op Token IDENT(image:String) {
    fsym { }
    is_fsym(t) { (t!=null) && t.kind == TOM_IDENTIFIER }
    get_slot(image,t) { t.image }
  }
  
  %op Token COMMA(image:String) {
    fsym { }
    is_fsym(t) { (t!=null) && t.kind == TOM_COMMA }
    get_slot(image,t) { t.image }
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

  private void addNewComposite(Stack stackList) {
    TomList list = (TomList) stackList.pop();
    list = (TomList) list.append(`Composite(concTomTerm()));
    stackList.push(list);
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

  private void detectVariableStar(Stack stackList) {
    TomList list = (TomList) stackList.peek();
    %match(TomList list) {
      concTomTerm(X1*,Composite(concTomTerm(L*,
                                            BackQuoteAppl(option,
                                                       Name(name),
                                                       concTomTerm()),
                                            TargetLanguageToTomTerm(ITL("*"))))) -> {
        TomTerm term = `VariableStar(option,Name(name),TomTypeAlone("unknown type"));
        stackList.pop();
        stackList.push(`concTomTerm(X1*,Composite(concTomTerm(L*,term))));
        return;
      }
    }
  }

  
  private boolean isWellFormed(Stack stackList) {
    int open = 0;
    TomList listComposite = (TomList) stackList.peek();
    %match(TomList listComposite) {
      concTomTerm(X1*,Composite(concTomTerm(list*))) -> {
        while(!list.isEmpty()) {
          TomTerm term = list.getHead();
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
          list = list.getTail();
        }
        return open==0;
      }

      _ -> {
        System.out.println("isWellFormed: error");
      }
    }
    return open==0;
  }
  
  public TomTerm buildBackQuoteTerm(List tokenList, HashMap nameToDecLineMap, String currentFileName) {
    this.nameToDecLineMap = nameToDecLineMap;
    Token token = null;
    Token next = null;
    LinkedList accu = new LinkedList();
    Stack stackName = new Stack();
    Stack stackList = new Stack();
		Stack stackOption = new Stack();

    stackList.push(`concTomTerm(Composite(concTomTerm())));

    Iterator it = tokenList.iterator();

    if(it.hasNext()) {
      next = (Token)it.next();
    }
      
    boolean finish = false;
    while(finish == false) {
      token = next;
      if(it.hasNext()) {
        next = (Token)it.next();
      } else {
        finish = true;
      }

        //System.out.println("token: " + token.image);
        //System.out.println("next: " + next.image);
      
      matchBlock: {
        %match(Token token, Token next) {
          IDENT(name), LPAREN[] -> {
            stackOption.push(`OriginTracking(Name(name), ((Integer)nameToDecLineMap.get(token)).intValue(), Name(currentFileName)));
            stackName.push(name);
            stackList.push(`concTomTerm(Composite(concTomTerm())));
            token = next;
            if(it.hasNext()) {
              next = (Token)it.next();
            } else {
              System.out.println("buildBackQuoteTerm: term not correct");
              finish = true;
            }
            break matchBlock;
          }
          
          IDENT(name), _ -> {
            OptionList option = `concOption(OriginTracking(Name(name), ((Integer)nameToDecLineMap.get(token)).intValue(), Name(currentFileName)));
            addToLastComposite(stackList, `BackQuoteAppl(option,Name(name),concTomTerm()));
              //concTomTerm(Composite(concTomTerm()))));
            break matchBlock;
          }
          
          COMMA[], _ -> {
            detectVariableStar(stackList);
            addNewComposite(stackList);
            break matchBlock;
          }

          LPAREN(image), _ -> {
            addToLastComposite(stackList, `TargetLanguageToTomTerm(ITL(image)));
            break matchBlock;
          }

          RPAREN(image), _ -> {
            if(isWellFormed(stackList)) {
              detectVariableStar(stackList);
              TomTerm term = buildTermFromStack(stackList, stackName, stackOption);
              addToLastComposite(stackList, term);
              break matchBlock;
            } else {
              addToLastComposite(stackList, `TargetLanguageToTomTerm(ITL(image)));
              break matchBlock;
            }
          }
          
          other, _ -> {
            addToLastComposite(stackList, `TargetLanguageToTomTerm(ITL(other.image)));
            break matchBlock;
          }
          
        }
      } // end matchBlock
      
    
    }

    TomTerm term = ((TomList)stackList.pop()).getHead();
      //System.out.println(term);
    
    return term;
  }

} //class TomBackQuoteParser

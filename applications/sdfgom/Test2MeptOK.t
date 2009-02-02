import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import aterm.ATerm;
import aterm.ATermAppl;
import aterm.ATermList;
import aterm.pure.PureFactory;
import aterm.pure.SingletonFactory;
import mept.types.*;

public class Test2MeptOK {
  %include { mept/Mept.tom }

  public static ATermAppl getMyCharATerm(ATerm value) {
    String sTemplate = "my_char(42)";
    return ((ATermAppl)SingletonFactory.getInstance().parse(sTemplate)).setArgument(value,0);
  }

  public static ATermAppl getCharacterATerm(ATerm value) {
    String sTemplate = "character(42)";
    return ((ATermAppl)SingletonFactory.getInstance().parse(sTemplate)).setArgument(value,0);
  }

  public static ATerm convert(ATerm at) {
    switch(at.getType()) {
      case ATerm.APPL:
        ATermAppl appl = (ATermAppl) at;
        String name = appl.getName();

        if(name.equals("char_class")) { // subcase : "char_class(CharRanges)"
          if(!(((ATermList)((ATermAppl)at).getArguments().getFirst()).isEmpty())) {
            for(int i=0;i<((ATermList)((ATermList)((ATermAppl)at).getArguments()).getFirst()).getLength();i++) {
              if(((ATermList)((ATermAppl)at).getArguments().getFirst()).elementAt(i).getType() == ATerm.INT) {
                at = ((ATermAppl)at).setArgument(((ATermList)(((ATermAppl)at).getArguments().getFirst())).replace((ATermAppl)getCharacterATerm(((ATermList)((ATermAppl)at).getArguments().getFirst()).elementAt(i)),i),0);
              } 
            }
          }
        
        } else if(name.equals("amb")) { // subcase : "amb(Args)"
            if(!(((ATermList)((ATermAppl)at).getArguments().getFirst()).isEmpty())) {
              for(int i=0;i<((ATermList)((ATermList)((ATermAppl)at).getArguments()).getFirst()).getLength();i++) {
                if(((ATermList)((ATermAppl)at).getArguments().getFirst()).elementAt(i).getType() == ATerm.INT) {
                  at = ((ATermAppl)at).setArgument(((ATermList)(((ATermAppl)at).getArguments().getFirst())).replace((ATermAppl)getMyCharATerm(((ATermList)((ATermAppl)at).getArguments().getFirst()).elementAt(i)),i),0);
                } else {
                  at = ((ATermAppl)at).setArgument(((ATermList)(((ATermAppl)at).getArguments().getFirst())).replace(convert(((ATermList)((ATermAppl)at).getArguments().getFirst()).elementAt(i)),i),0);
                }
              }
            }

        } else if(name.equals("appl")) { // subcase : "appl(Production,Args)"
            at = ((ATermAppl)at).setArgument(convert(((ATermAppl)at).getArguments().getFirst()),0);
            if(!(((ATermList)((ATermAppl)at).getArgument(1)).isEmpty())) {
              for(int i=0;i<((ATermList)((ATermAppl)at).getArgument(1)).getLength();i++) {
                if(((ATermList)((ATermAppl)at).getArgument(1)).elementAt(i).getType() == ATerm.INT) {
                  at = ((ATermAppl)at).setArgument(((ATermList)(((ATermAppl)at).getArgument(1))).replace((ATermAppl)getMyCharATerm(((ATermList)((ATermAppl)at).getArgument(1)).elementAt(i)),i),1);
                } else {
                  at = ((ATermAppl)at).setArgument(((ATermList)(((ATermAppl)at).getArgument(1))).replace(convert(((ATermList)((ATermAppl)at).getArgument(1)).elementAt(i)),i),1);
                }
              }
            }
        
        } else { // subcase : other APPL
          if(!(((ATermAppl)at).getArguments().isEmpty())) {
            if( ((ATermAppl)at).getArity()>1) {
              for(int k=0;k<((ATermAppl)at).getArity();k++) {
                at = ((ATermAppl)at).setArgument(convert(((ATermAppl)at).getArgument(k)),k);
              }
            } else {
              at = ((ATermAppl)at).setArgument(((ATermList)convert(((ATermAppl)at).getArguments())).getFirst(),0);
            }
          }
        }
        break;

      case ATerm.LIST:
        if(!(((ATermList)at).isEmpty())) {
          at = ((ATermList)convert(((ATermList)at).getNext())).insert(convert(((ATermList)at).getFirst()));
        }
        break;
    }
    return at;
  }

  public static void main(String[] args) {
    //PureFactory factory = SingletonFactory.getInstance();
    String s1 =
      "parsetree(appl(prod([cf(opt(layout)),cf(sort(\"Block\")),cf(opt(layout))],sort(\"<START>\"),no-attrs),[appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[])])]),appl(prod([lit(\"{\"),cf(opt(layout)),cf(sort(\"BoolList\")),cf(opt(layout)),lit(\"}\")],cf(sort(\"Block\")),attrs([term(cons(\"BoolBlock\"))])),[appl(prod([char-class([123])],lit(\"{\"),no-attrs),[123]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[])])]),appl(prod([cf(iter-star(sort(\"Bool\")))],cf(sort(\"BoolList\")),attrs([term(cons(\"list\"))])),[appl(list(cf(iter-star(sort(\"Bool\")))),[appl(prod([lit(\"true\")],cf(sort(\"Bool\")),attrs([term(cons(\"True\"))])),[appl(prod([char-class([116]),char-class([114]),char-class([117]),char-class([101])],lit(\"true\"),no-attrs),[116,114,117,101])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([lit(\"false\")],cf(sort(\"Bool\")),attrs([term(cons(\"False\"))])),[appl(prod([char-class([102]),char-class([97]),char-class([108]),char-class([115]),char-class([101])],lit(\"false\"),no-attrs),[102,97,108,115,101])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([cf(sort(\"Bool\")),cf(opt(layout)),lit(\"&\"),cf(opt(layout)),cf(sort(\"Bool\"))],cf(sort(\"Bool\")),attrs([term(cons(\"And\"))])),[appl(prod([lit(\"false\")],cf(sort(\"Bool\")),attrs([term(cons(\"False\"))])),[appl(prod([char-class([102]),char-class([97]),char-class([108]),char-class([115]),char-class([101])],lit(\"false\"),no-attrs),[102,97,108,115,101])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([char-class([38])],lit(\"&\"),no-attrs),[38]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([lit(\"true\")],cf(sort(\"Bool\")),attrs([term(cons(\"True\"))])),[appl(prod([char-class([116]),char-class([114]),char-class([117]),char-class([101])],lit(\"true\"),no-attrs),[116,114,117,101])])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([cf(sort(\"Id\"))],cf(sort(\"Bool\")),attrs([term(cons(\"Id\"))])),[appl(prod([lex(sort(\"Id\"))],cf(sort(\"Id\")),no-attrs),[appl(prod([lex(iter(char-class([range(48,57)])))],lex(sort(\"Id\")),attrs([term(cons(\"IdCons\"))])),[appl(list(lex(iter(char-class([range(48,57)])))),[48,48,55])])])])])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[])])]),appl(prod([char-class([125])],lit(\"}\"),no-attrs),[125])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[10])])])])]),0)";
    String s2 = 
      "parsetree(appl(prod([cf(opt(layout)),cf(sort(\"Block\")),cf(opt(layout))],sort(\"<START>\"),no-attrs),[appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[])])]),appl(prod([lit(\"{\"),cf(opt(layout)),cf(sort(\"BoolList\")),cf(opt(layout)),lit(\"}\")],cf(sort(\"Block\")),attrs([term(cons(\"BoolBlock\"))])),[appl(prod([char-class([123])],lit(\"{\"),no-attrs),[123,amb([999,998,997])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[])])]),appl(prod([cf(iter-star(sort(\"Bool\")))],cf(sort(\"BoolList\")),attrs([term(cons(\"list\"))])),[appl(list(cf(iter-star(sort(\"Bool\")))),[appl(prod([lit(\"true\")],cf(sort(\"Bool\")),attrs([term(cons(\"True\"))])),[appl(prod([char-class([116]),char-class([114]),char-class([117]),char-class([101])],lit(\"true\"),no-attrs),[116,114,117,101])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([lit(\"false\")],cf(sort(\"Bool\")),attrs([term(cons(\"False\"))])),[appl(prod([char-class([102]),char-class([97]),char-class([108]),char-class([115]),char-class([101])],lit(\"false\"),no-attrs),[102,97,108,115,101])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([cf(sort(\"Bool\")),cf(opt(layout)),lit(\"&\"),cf(opt(layout)),cf(sort(\"Bool\"))],cf(sort(\"Bool\")),attrs([term(cons(\"And\"))])),[appl(prod([lit(\"false\")],cf(sort(\"Bool\")),attrs([term(cons(\"False\"))])),[appl(prod([char-class([102]),char-class([97]),char-class([108]),char-class([115]),char-class([101])],lit(\"false\"),no-attrs),[102,97,108,115,101])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([char-class([38])],lit(\"&\"),no-attrs),[38]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([lit(\"true\")],cf(sort(\"Bool\")),attrs([term(cons(\"True\"))])),[appl(prod([char-class([116]),char-class([114]),char-class([117]),char-class([101])],lit(\"true\"),no-attrs),[116,114,117,101])])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([cf(sort(\"Id\"))],cf(sort(\"Bool\")),attrs([term(cons(\"Id\"))])),[appl(prod([lex(sort(\"Id\"))],cf(sort(\"Id\")),no-attrs),[appl(prod([lex(iter(char-class([range(48,57)])))],lex(sort(\"Id\")),attrs([term(cons(\"IdCons\"))])),[appl(list(lex(iter(char-class([range(48,57)])))),[48,48,55])])])])])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[])])]),appl(prod([char-class([125])],lit(\"}\"),no-attrs),[125])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[10])])])])]),0)";
    
    System.out.println("s1 before any substitution =\n" + s1);
    ATerm at = SingletonFactory.getInstance().parse(s1.replace('-','_').replaceAll("sort","my_sort"));
    //System.out.println("\ns2 before any substitution =\n" + s2);
    //ATerm at = SingletonFactory.getInstance().parse(s2.replace('-','_').replaceAll("sort","my_sort"));
    
    //System.out.println("\nat after the first substitution =\n" + at + "\n");

    ATerm convertedAT = convert(at);
    System.out.println("\nat after the 'semantic' conversion =\n" + convertedAT + "\n");

    ParseTree t = ParseTree.fromTerm(convertedAT);
    System.out.println("\nParseTree.fromTerm(convertedAT) = t =\n" + t + "\n");
    
    ATerm newATerm = t.toATerm();
    System.out.println("\nt.toATerm() = newATerm =\n" + newATerm + "\n");
    
    System.out.println("\nNew test : convert(newATerm) =\n" + convert(newATerm) + "\n");
  }
} 

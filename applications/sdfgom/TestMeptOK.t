import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import aterm.*;
import aterm.pure.*;
import mept.types.*;

public class TestMeptOK {
  %include { mept/Mept.tom }

  public static ATerm getMyCharATermTemplate(PureFactory factory) {
    String sTemplate = "my_char(42)";
    return factory.parse(sTemplate);
  }
  
  public static ATerm getCharacterATermTemplate(PureFactory factory) {
    String sTemplate = "character(42)";
    return factory.parse(sTemplate);
  }

  public static ATermAppl getMyCharATerm(PureFactory factory, ATerm value) {
    String sTemplate = "my_char(42)";
    return ((ATermAppl)factory.parse(sTemplate)).setArgument(value,0);
  }

  public static ATermAppl getCharacterATerm(PureFactory factory, ATerm value) {
    String sTemplate = "character(42)";
    return ((ATermAppl)factory.parse(sTemplate)).setArgument(value,0);
  }

  // Is it usefull to decompose the substitute method with this one ?
  /*public static ATerm processCharClassSubstitution(ATerm at, PureFactory factory) {
    if(!(((ATermListImpl)((ATermAppl)at).getArguments().getFirst()).isEmpty())) {
      for(int i=0;i<((ATermListImpl)((ATermListImpl)((ATermAppl)at).getArguments()).getFirst()).getLength();i++) {
        if(((ATermListImpl)((ATermAppl)at).getArguments().getFirst()).elementAt(i).getType() == ATerm.INT) {
          ATerm myAppl = (ATermAppl)getCharacterATerm(factory,((ATermListImpl)((ATermAppl)at).getArguments().getFirst()).elementAt(i));
          at = ((ATermAppl)at).setArgument(((ATermListImpl)(((ATermAppl)at).getArguments().getFirst())).replace(myAppl,i),0);
        }
      }
    }
    return at;
  }*/

  public static ATerm substitute(ATerm at, PureFactory factory) {
    switch(at.getType()) {
      case ATerm.APPL:
        //System.out.println("at.getType() = APPL ="+at.getType());
        ATermAppl appl = (ATermAppl) at;
        String name = appl.getName();

        if(name.equals("char_class")) { // subcase : "char_class(CharRanges)"
          //System.out.println("(in ATerm.APPL) in if name == char_class");
          if(!(((ATermListImpl)((ATermAppl)at).getArguments().getFirst()).isEmpty())) {
            for(int i=0;i<((ATermListImpl)((ATermListImpl)((ATermAppl)at).getArguments()).getFirst()).getLength();i++) {
              if(((ATermListImpl)((ATermAppl)at).getArguments().getFirst()).elementAt(i).getType() == ATerm.INT) {
                // Create a new ATerm by using the "to be remplaced" ATerm
                //ATerm myAppl = (ATermAppl)getCharacterATerm(factory,((ATermListImpl)((ATermAppl)at).getArguments().getFirst()).elementAt(i));
                //at = ((ATermAppl)at).setArgument(((ATermListImpl)(((ATermAppl)at).getArguments().getFirst())).replace(myAppl,i),0);
                at = ((ATermAppl)at).setArgument(((ATermListImpl)(((ATermAppl)at).getArguments().getFirst())).replace((ATermAppl)getCharacterATerm(factory,((ATermListImpl)((ATermAppl)at).getArguments().getFirst()).elementAt(i)),i),0);
              } //if getType() == ATerm.INT
            } // for
          } //if test getArguments().getFirst().isEmpty()
        
        } else if(name.equals("amb")) { // subcase : "amb(Args)"
            if(!(((ATermList)((ATermAppl)at).getArguments().getFirst()).isEmpty())) {
              for(int i=0;i<((ATermList)((ATermList)((ATermAppl)at).getArguments()).getFirst()).getLength();i++) {
                if(((ATermList)((ATermAppl)at).getArguments().getFirst()).elementAt(i).getType() == ATerm.INT) {
                  at = ((ATermAppl)at).setArgument(((ATermList)(((ATermAppl)at).getArguments().getFirst())).replace((ATermAppl)getMyCharATerm(factory,((ATermList)((ATermAppl)at).getArguments().getFirst()).elementAt(i)),i),0);
                } else {
                  at = ((ATermAppl)at).setArgument(((ATermList)(((ATermAppl)at).getArguments().getFirst())).replace(substitute(((ATermList)((ATermAppl)at).getArguments().getFirst()).elementAt(i),factory),i),0);
                }
              }
            }

        } else if(name.equals("appl")) { // subcase : "appl(Production,Args)"
            // In any case, the first argument has to be substituted before we substitute the second argument 
            at = ((ATermAppl)at).setArgument(substitute(((ATermAppl)at).getArguments().getFirst(),factory),0);
            if(!(((ATermList)((ATermAppl)at).getArgument(1)).isEmpty())) {
              for(int i=0;i<((ATermList)((ATermAppl)at).getArgument(1)).getLength();i++) {
                if(((ATermList)((ATermAppl)at).getArgument(1)).elementAt(i).getType() == ATerm.INT) {
                  at = ((ATermAppl)at).setArgument(((ATermList)(((ATermAppl)at).getArgument(1))).replace((ATermAppl)getMyCharATerm(factory,((ATermList)((ATermAppl)at).getArgument(1)).elementAt(i)),i),1);
                } else {
                  at = ((ATermAppl)at).setArgument(((ATermList)(((ATermAppl)at).getArgument(1))).replace(substitute(((ATermList)((ATermAppl)at).getArgument(1)).elementAt(i),factory),i),1);
                }
              }
            }
        
        } else { // subcase : other APPL
          if(!(((ATermAppl)at).getArguments().isEmpty())) {
            // Here we check the arity of the ATermAppl, since the needed behaviour is different 
            if( ((ATermAppl)at).getArity()>1) {
              //System.out.println("(in ATerm.APPL) in if arity > 1");
              for(int k=0;k<((ATermAppl)at).getArity();k++) {
                at = ((ATermAppl)at).setArgument(substitute( ((ATermAppl)at).getArgument(k),factory),k);
              }
            } else {
              //System.out.println("(in ATerm.APPL) in if arity == 0|1");
              at = ((ATermAppl)at).setArgument(((ATermList)substitute(((ATermAppl)at).getArguments(),factory)).getFirst(),0);
            }
          } // if "isEmpty()"
        }
        break;

      case ATerm.LIST:
        if(!(((ATermList)at).isEmpty())) {
          //System.out.println("(in ATerm.LIST) in if !isEmpty()");
          at = ((ATermList)substitute(((ATermList)at).getNext(),factory)).insert(substitute(((ATermList)at).getFirst(),factory));
        }
        break;
    }
    return at;
  }

  public static void main(String[] args) {
    PureFactory factory = SingletonFactory.getInstance();
    String s1 =
      "parsetree(appl(prod([cf(opt(layout)),cf(sort(\"Block\")),cf(opt(layout))],sort(\"<START>\"),no-attrs),[appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[])])]),appl(prod([lit(\"{\"),cf(opt(layout)),cf(sort(\"BoolList\")),cf(opt(layout)),lit(\"}\")],cf(sort(\"Block\")),attrs([term(cons(\"BoolBlock\"))])),[appl(prod([char-class([123])],lit(\"{\"),no-attrs),[123]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[])])]),appl(prod([cf(iter-star(sort(\"Bool\")))],cf(sort(\"BoolList\")),attrs([term(cons(\"list\"))])),[appl(list(cf(iter-star(sort(\"Bool\")))),[appl(prod([lit(\"true\")],cf(sort(\"Bool\")),attrs([term(cons(\"True\"))])),[appl(prod([char-class([116]),char-class([114]),char-class([117]),char-class([101])],lit(\"true\"),no-attrs),[116,114,117,101])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([lit(\"false\")],cf(sort(\"Bool\")),attrs([term(cons(\"False\"))])),[appl(prod([char-class([102]),char-class([97]),char-class([108]),char-class([115]),char-class([101])],lit(\"false\"),no-attrs),[102,97,108,115,101])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([cf(sort(\"Bool\")),cf(opt(layout)),lit(\"&\"),cf(opt(layout)),cf(sort(\"Bool\"))],cf(sort(\"Bool\")),attrs([term(cons(\"And\"))])),[appl(prod([lit(\"false\")],cf(sort(\"Bool\")),attrs([term(cons(\"False\"))])),[appl(prod([char-class([102]),char-class([97]),char-class([108]),char-class([115]),char-class([101])],lit(\"false\"),no-attrs),[102,97,108,115,101])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([char-class([38])],lit(\"&\"),no-attrs),[38]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([lit(\"true\")],cf(sort(\"Bool\")),attrs([term(cons(\"True\"))])),[appl(prod([char-class([116]),char-class([114]),char-class([117]),char-class([101])],lit(\"true\"),no-attrs),[116,114,117,101])])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([cf(sort(\"Id\"))],cf(sort(\"Bool\")),attrs([term(cons(\"Id\"))])),[appl(prod([lex(sort(\"Id\"))],cf(sort(\"Id\")),no-attrs),[appl(prod([lex(iter(char-class([range(48,57)])))],lex(sort(\"Id\")),attrs([term(cons(\"IdCons\"))])),[appl(list(lex(iter(char-class([range(48,57)])))),[48,48,55])])])])])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[])])]),appl(prod([char-class([125])],lit(\"}\"),no-attrs),[125])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[10])])])])]),0)";
    String s2 = 
      "parsetree(appl(prod([cf(opt(layout)),cf(sort(\"Block\")),cf(opt(layout))],sort(\"<START>\"),no-attrs),[appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[])])]),appl(prod([lit(\"{\"),cf(opt(layout)),cf(sort(\"BoolList\")),cf(opt(layout)),lit(\"}\")],cf(sort(\"Block\")),attrs([term(cons(\"BoolBlock\"))])),[appl(prod([char-class([123])],lit(\"{\"),no-attrs),[123,amb([999,998,997])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[])])]),appl(prod([cf(iter-star(sort(\"Bool\")))],cf(sort(\"BoolList\")),attrs([term(cons(\"list\"))])),[appl(list(cf(iter-star(sort(\"Bool\")))),[appl(prod([lit(\"true\")],cf(sort(\"Bool\")),attrs([term(cons(\"True\"))])),[appl(prod([char-class([116]),char-class([114]),char-class([117]),char-class([101])],lit(\"true\"),no-attrs),[116,114,117,101])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([lit(\"false\")],cf(sort(\"Bool\")),attrs([term(cons(\"False\"))])),[appl(prod([char-class([102]),char-class([97]),char-class([108]),char-class([115]),char-class([101])],lit(\"false\"),no-attrs),[102,97,108,115,101])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([cf(sort(\"Bool\")),cf(opt(layout)),lit(\"&\"),cf(opt(layout)),cf(sort(\"Bool\"))],cf(sort(\"Bool\")),attrs([term(cons(\"And\"))])),[appl(prod([lit(\"false\")],cf(sort(\"Bool\")),attrs([term(cons(\"False\"))])),[appl(prod([char-class([102]),char-class([97]),char-class([108]),char-class([115]),char-class([101])],lit(\"false\"),no-attrs),[102,97,108,115,101])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([char-class([38])],lit(\"&\"),no-attrs),[38]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([lit(\"true\")],cf(sort(\"Bool\")),attrs([term(cons(\"True\"))])),[appl(prod([char-class([116]),char-class([114]),char-class([117]),char-class([101])],lit(\"true\"),no-attrs),[116,114,117,101])])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([cf(sort(\"Id\"))],cf(sort(\"Bool\")),attrs([term(cons(\"Id\"))])),[appl(prod([lex(sort(\"Id\"))],cf(sort(\"Id\")),no-attrs),[appl(prod([lex(iter(char-class([range(48,57)])))],lex(sort(\"Id\")),attrs([term(cons(\"IdCons\"))])),[appl(list(lex(iter(char-class([range(48,57)])))),[48,48,55])])])])])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[])])]),appl(prod([char-class([125])],lit(\"}\"),no-attrs),[125])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[10])])])])]),0)";
    
    // Test remplacement :
   
    // Etape 1 : '-' -> '_'
    // Etape 2 : "sort" -> "my_sort"
    // ultra simple en utilisant les méthodes java suivantes :
    // System.out.println("s : - -> _ : \n"+s.replace('-','_').replaceAll("sort","my_sort"));

    // Etape 3 : char_class([,,,]) -> char_class([...,my_char(),...,...]) 
    // Etape 4 : appl(prod,[,,,,] & amb([]) -> blabla([...,character(..),...])

    /*StringBuffer temp=new StringBuffer(s);
    for(int i=0;i<s.length();i++) {
      if (temp.charAt(i)=='-') {
        temp.setCharAt(i,'_');
      }
    }
    System.out.println("temp.toString() - -> _ : \n"+temp.toString());*/
    //System.out.println("s = "+s);
    //ATerm at = factory.parse(s);
    ATerm at = factory.parse(s1.replace('-','_').replaceAll("sort","my_sort"));
    
    //////////////////////
    //test creation d ATerm sur test bidon
    //String sTemplate = "my_char(32)";
    //ATerm atTemplate = factory.parse(sTemplate);
    /*ATermAppl applTemplate = (ATermAppl) atTemplate;
    ATermList argsTemplate = applTemplate.getArguments();
    ATerm myNewATerm = atTemplate.make(new ArrayList(Arrays.asList(argsTemplate.getFirst())));
    System.out.println("myNewATerm ="+myNewATerm);
    if(argsTemplate.getFirst().getType() == ATerm.INT) {
      System.out.println("\nc'est un ATerm.INT\n");
    } else {
      System.out.println("\nCe n'est pas un ATerm.INT\n");
    }*/
    ///////////////////////
    // en prenant le test bidon :
    // AtermList replace(ATerm,int), autrement dit : 
    // arguments.replace(atTemplate.make(new ArrayList(Arrays.asList(argsTemplate.getFirst()))),index_courrant)
    

    //System.out.println("at =\n" + at);

    System.out.println("\nat after substitution =\n" + substitute(at,factory)+"\n");

    //ParseTree t = ParseTree.fromTerm(at);
    //System.out.println("t = " + t);
  }
} 

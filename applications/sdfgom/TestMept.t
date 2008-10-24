import aterm.*;
import aterm.pure.*;
import mept.types.*;

public class TestMept {
  %include { mept/mept.tom }
 public static void main(String[] args) {
   PureFactory factory = SingletonFactory.getInstance();
   String s =
   "parsetree(appl(prod([cf(opt(layout)),cf(sort(\"Block\")),cf(opt(layout))],sort(\"<START>\"),no-attrs),[appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[])])]),appl(prod([lit(\"{\"),cf(opt(layout)),cf(sort(\"BoolList\")),cf(opt(layout)),lit(\"}\")],cf(sort(\"Block\")),attrs([term(cons(\"BoolBlock\"))])),[appl(prod([char-class([123])],lit(\"{\"),no-attrs),[123]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[])])]),appl(prod([cf(iter-star(sort(\"Bool\")))],cf(sort(\"BoolList\")),attrs([term(cons(\"list\"))])),[appl(list(cf(iter-star(sort(\"Bool\")))),[appl(prod([lit(\"true\")],cf(sort(\"Bool\")),attrs([term(cons(\"True\"))])),[appl(prod([char-class([116]),char-class([114]),char-class([117]),char-class([101])],lit(\"true\"),no-attrs),[116,114,117,101])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([lit(\"false\")],cf(sort(\"Bool\")),attrs([term(cons(\"False\"))])),[appl(prod([char-class([102]),char-class([97]),char-class([108]),char-class([115]),char-class([101])],lit(\"false\"),no-attrs),[102,97,108,115,101])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([cf(sort(\"Bool\")),cf(opt(layout)),lit(\"&\"),cf(opt(layout)),cf(sort(\"Bool\"))],cf(sort(\"Bool\")),attrs([term(cons(\"And\"))])),[appl(prod([lit(\"false\")],cf(sort(\"Bool\")),attrs([term(cons(\"False\"))])),[appl(prod([char-class([102]),char-class([97]),char-class([108]),char-class([115]),char-class([101])],lit(\"false\"),no-attrs),[102,97,108,115,101])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([char-class([38])],lit(\"&\"),no-attrs),[38]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([lit(\"true\")],cf(sort(\"Bool\")),attrs([term(cons(\"True\"))])),[appl(prod([char-class([116]),char-class([114]),char-class([117]),char-class([101])],lit(\"true\"),no-attrs),[116,114,117,101])])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[32])])])]),appl(prod([cf(sort(\"Id\"))],cf(sort(\"Bool\")),attrs([term(cons(\"Id\"))])),[appl(prod([lex(sort(\"Id\"))],cf(sort(\"Id\")),no-attrs),[appl(prod([lex(iter(char-class([range(48,57)])))],lex(sort(\"Id\")),attrs([term(cons(\"IdCons\"))])),[appl(list(lex(iter(char-class([range(48,57)])))),[48,48,55])])])])])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[])])]),appl(prod([char-class([125])],lit(\"}\"),no-attrs),[125])]),appl(prod([cf(layout)],cf(opt(layout)),no-attrs),[appl(prod([lex(iter(layout))],cf(layout),no-attrs),[appl(list(lex(iter(layout))),[appl(prod([char-class([range(9,10),13,32])],lex(layout),no-attrs),[10])])])])]),0)";
   ATerm at = factory.parse(s);
   System.out.println("at = " + at);

   ParseTree t = ParseTree.fromTerm(at);
   System.out.println("t = " + t);
 } 

} 



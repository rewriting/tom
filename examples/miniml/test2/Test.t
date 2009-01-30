import test.sig.types.*;

interface Fun<A,B> { B apply(A x); }
interface Value<A> { A value(); }

public class Test {

    %gom {
      module sig
      abstract syntax

      Name = A() | B() | C() | D() | E() | F() | G()
      Term = Fun(f:Name,l:TermList)
      TermList = TList(Term*)
      TermRes = NoneTL() | SomeTL(l:TermList)
      TermListList = TLList(TermList*)
      TermListRes = NoneTLL() | SomeTLL(ll:TermListList)
      TermListListList = TLLList(TermListList*)
      TermListListRes = NoneTLLL() | SomeTLLL(lll:TermListListList);
    }


    /* to make tom generate tom_make_[...] functions */
    static {
      `A(); `B(); `C(); `D(); `E(); `F(); `G();
      `NoneTL(); 
      TermList tl = `ConsTList(Fun(A(),EmptyTList()),EmptyTList());
      `SomeTL(tl); `NoneTLL(); 
      TermListList tll = `ConsTLList(tl,EmptyTLList());
      `SomeTLL(tll); `NoneTLLL(); 
      `SomeTLLL(ConsTLLList(tll,EmptyTLLList()));
    }

    public static String pretty(Name n) {
      %match(n) {
        A() -> { return "a"; }
        B() -> { return "b"; }
        C() -> { return "c"; }
        D() -> { return "d"; }
        E() -> { return "e"; }
        F() -> { return "f"; }
        G() -> { return "f"; }
      }
      return null;
    }

    public static String pretty(Term t) {
      %match(t) {
        Fun(f,TList()) -> { return `pretty(f); }
        Fun(f,l) -> { return `pretty(f) + "(" + `pretty(l) + ")"; }
      }
      return null;
    }

    public static String pretty(TermList tl) {
      %match(tl) {
        TList(x) -> { return `pretty(x); }
        TList(x,xs*) -> { return `pretty(x) + "," + `pretty(xs); }
      }
      return null;
    }

    public static void main(String[] args) {
      TermRes res = %include{ minisl.tom };
      %match(res) { SomeTL(l) -> { System.out.println(`pretty(l)); } }
    }
}

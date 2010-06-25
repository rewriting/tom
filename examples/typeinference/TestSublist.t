import testsublist.testsublist.types.*;
public class TestSublist{
  %gom {
    module TestSublist
      imports int
      abstract syntax
      Term =
      | a()
      | b()
      | c()
      | list( Term* )
      | f( l:TermList)

      TermList = termList(Term*)
  }

  public static void main(String[] args) {
    TestSublist test = new TestSublist();
  }

  public void test() {
    TermList res = `termList(a(),list(b(),b()),c());
    %match(res) {
      termList(_X*,list(_*,b(),_*),_Y*) -> {
        return;
      }
    }
    //fail();
  }
}

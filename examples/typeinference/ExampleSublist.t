import examplesublist.examplesublist.types.*;

public class ExampleSublist{
  %gom {
    module ExampleSublist
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
    ExampleSublist test = new ExampleSublist();
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

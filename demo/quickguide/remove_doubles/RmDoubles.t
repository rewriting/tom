import rmdoubles.mylist.types.*; 
import tom.library.sl.*; 

public class RmDoubles {
  
  %include { sl.tom }

  %gom {
    module mylist
      imports  String
      abstract syntax
      
      StrList = strlist(String*)
  }

  %strategy Remove() extends `Identity() {
    visit StrList { (X*,i,Y*,i,Z*) -> { return `strlist(X*,i,Y*,Z*); } }
  }

  public static void main(String[] args) throws VisitFailure {
    StrList l = `strlist("framboisier","eric","framboisier","remi","remi","framboisier","rene","bernard");
    System.out.println(l);
    System.out.println(`RepeatId(Remove()).visit(l));
  }
}



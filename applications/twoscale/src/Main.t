
import formula.*;
import formula.types.*;
import java.util.*;

public class Main {

  %include { formula/formula.tom }
  %include { sl.tom }

	public static void main(String[] args) {
    Exp e = `Cst(5);
    System.out.println(e);

  }
}

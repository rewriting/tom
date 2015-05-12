package sa;

import sa.rule.types.*;
import java.util.*;
// import tom.library.sl.*;
// import aterm.*;
// import aterm.pure.*;

public class Signature {
//   %include { rule/Rule.tom }
//   %include { sl.tom }
//   %include { java/util/types/Map.tom }
//   %include { java/util/types/List.tom }

  Map<GomType,Map<String,List<GomType>>> signature;

  public Signature(){
    this.signature = new HashMap<GomType,Map<String,List<GomType>>>(); 
  }


  public Signature setSignature(Program program) {

//     %match(program) {
//       Program(ConcProduction(_,SortType(GomType(type),ConcAlternative(symbols)),_*) -> {
//             this.signatures.add(`sig);
//             this.strategies.put("strat"+index++,`strat);
//       }
//     }

    return this;
  }

  public String toString(){
    return ""+this.signature;
  }
}

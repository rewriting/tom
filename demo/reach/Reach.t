import java.util.*;
import term.types.*;

public class Reach {

  %include { mustrategy.tom }
  %include { util/types/Collection.tom }
  %include { term/term.tom }

  public static Collection<Nat> oneStep(Collection<Nat> subjects) {
    Iterator<Nat> it = subjects.iterator();
    Collection<Nat> set = new HashSet<Nat>();
    while (it.hasNext()) {
      Nat subject = it.next();
      `TopDown(Collect(subject, set)).apply(subject);
    }
    return set;
  }

  %strategy Collect(subject:Nat, c:Collection) extends Identity() {
    visit Nat {
      suc(x) -> {
        c.add(getPosition().getReplace(`suc(suc(x))).visit(subject));
      }
      suc(suc(suc(x))) -> {
        c.add(getPosition().getReplace(`x).visit(subject));
      }
    }
  }

  public static void main(String[] args) {
    Collection<Nat> q = new HashSet<Nat>();
    q.add(`suc(zero()));
    //q.add(`suc(suc(couple(suc(zero()),suc(suc(zero()))))));
    if (args.length > 0) {
      int count = Integer.parseInt(args[0]);
      for(int i = 0; i<count; i++) {
        q = oneStep(q);
      }
      System.out.println(q);
    } else {
      System.out.println(oneStep(q));
    }
  }
}

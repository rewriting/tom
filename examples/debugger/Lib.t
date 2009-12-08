package debugger;

import tom.library.sl.*;

public class Lib {

  %include{ sl.tom }

  public static boolean simulates(Strategy s, String n) {
    return (s instanceof Decorator 
        && ((Decorator)s).getName().equals(n));
  }

  private static boolean ellegibleMuVar(Environment e) {
    //System.out.println(e);
    //if (!(e.getSubject() instanceof MuVar)) return false;
    try { e = (Environment) e.clone(); } 
    catch (CloneNotSupportedException ex) {
      throw new RuntimeException(); 
    }
    e.up();
    Object parent = e.getSubject();
    if (parent instanceof Mu) return false;
    if (parent instanceof MuVarDecorator) return false;
    return true;
  }

  %strategy Decorate() extends Identity() {
    visit Strategy {
      x -> {
        Strategy v = `x; // ` space eating bug
        if (v instanceof MuVar) {
          if (ellegibleMuVar(getEnvironment()))
            return new MuVarDecorator(v);
          else return `x;
        }
        if (!(v instanceof ADecorator)) 
          return new BasicDecorator(v); 
      }
    }
  }

  public static Strategy weaveBasicDecorators(Strategy s) {
    try {
      return (Strategy) `BottomUp(Decorate()).visit(s);
    } catch(VisitFailure e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

}

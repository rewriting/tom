/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

/**
 *
 * @author hubert
 */
public class Hole {

  private Typable type;
  private Constructor cons;
  private Hole[] deps;
  @Deprecated
  private Request req;

  Hole(Typable type) {
    this.type = type;
  }

  Hole[] chooseConstructor() {
    if (type instanceof Algebraic) {
      cons = ((Algebraic) type).chooseConstructor();
      deps = cons.giveATermDeps();
    } else {
      deps = new Hole[0];
    }
    return deps;
  }

  Hole[] chooseMinimalConstructor() {
    if (type instanceof Algebraic) {
      cons = ((Algebraic) type).chooseMinimalConstructor();
      deps = cons.giveATermDeps();
    } else {
      deps = new Hole[0];
    }
    return deps;
  }

  @Deprecated
  Request getRequest() {
    return req;
  }

  @Deprecated
  void setRequest(Request req) {
    this.req = req;
  }

  int getDimention() {
    return type.getDimension();
  }

  int getDstToLeaf() {
    return type.dstToLeaf();
  }

  @Override
  public String toString() {
    String res = "";
    res += cons.getName() + "(";
    int i = 0;
    for (Hole aTerm : deps) {
      res += aTerm;
      if (i != deps.length - 1) {
        res += ", ";
      }
      i++;
    }
    res += ")";
    return res;
  }
}

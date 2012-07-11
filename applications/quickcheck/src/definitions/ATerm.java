/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

/**
 *
 * @author hubert
 */
public class ATerm {

  private Typable type;
  private Constructor cons;
  private ATerm[] deps;
  @Deprecated
  private Request req;

  ATerm(Typable type) {
    this.type = type;
  }

  ATerm[] chooseConstructor() {
    if (type instanceof Algebraic) {
      cons = ((Algebraic) type).chooseConstructor();
      deps = cons.giveATermDeps();
    } else {
      deps = new ATerm[0];
    }
    return deps;
  }

  ATerm[] chooseMinimalConstructor() {
    if (type instanceof Algebraic) {
      cons = ((Algebraic) type).chooseMinimalConstructor();
      deps = cons.giveATermDeps();
    } else {
      deps = new ATerm[0];
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
    return type.getDimention();
  }

  int getDstToLeaf() {
    return type.dstToLeaf();
  }

  @Override
  public String toString() {
    String res = "";
    res += cons.getName() + "(";
    int i = 0;
    for (ATerm aTerm : deps) {
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

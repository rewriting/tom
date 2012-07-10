/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

/**
 *
 * @author hubert
 */
class ATerm {

  private Typable type;
  private Constructor cons;
  private ATerm[] deps;
  Request req;

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

  int getDimention() {
    return type.getDimention();
  }
}

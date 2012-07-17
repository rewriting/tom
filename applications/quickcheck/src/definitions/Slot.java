/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

import aterm.AFun;
import aterm.ATerm;
import aterm.ATermAppl;
import aterm.pure.PureFactory;
import java.io.File;
import java.io.FileOutputStream;

/**
 *
 * @author hubert
 */
class Slot {

  private Buildable type;
  private Constructor cons;
  private Slot[] deps;

  Slot(Buildable type) {
    this.type = type;
  }

  Slot[] chooseFiniteConstructor() {
    if (type instanceof Algebraic) {
      cons = ((Algebraic) type).chooseFiniteConstructor();
      deps = cons.giveATermDeps();
    } else {
      deps = new Slot[0];
    }
    return deps;
  }

  Slot[] chooseMinimalConstructor() {
    if (type instanceof Algebraic) {
      cons = ((Algebraic) type).chooseMinDepthConstructor();
      deps = cons.giveATermDeps();
    } else {
      deps = new Slot[0];
    }
    return deps;
  }

  int getDimension() {
    return type.getDimension();
  }

  int getDstToLeaf() {
    return type.depthToLeaf();
  }

  @Override
  public String toString() {
    String res = "";
    res += cons.getName() + "(";
    int i = 0;
    for (Slot aTerm : deps) {
      res += aTerm;
      if (i != deps.length - 1) {
        res += ", ";
      }
      i++;
    }
    res += ")";
    return res;
  }

  public void toDot(String chemin) {
    String res = "digraph mon_graphe {\n";
    res += toDot_aux("8");
    res += "}\n";
    File fichier = new File(chemin);
    try {
      FileOutputStream graveur = new FileOutputStream(fichier);
      graveur.write(res.getBytes());
      graveur.close();
    } catch (java.io.IOException err) {
      System.err.println("ecriture fichier impossible");
    }
  }

  private String toDot_aux(String way) {
    String res = new String();
    int i = 0;
    for (Slot slot : deps) {
      res += "\"" + way + "\"" + "->" + "\"" + way + i + "\"\n";
      res += slot.toDot_aux(way + i);
      i++;
    }
    return res;
  }

  ATermAppl toATerm() {
    PureFactory factory = new PureFactory();
    ATerm[] listFields = new ATerm[deps.length];
    for (int i = 0; i < listFields.length; i++) {
      listFields[i] = deps[i].toATerm();
    }
    AFun fun = factory.makeAFun(cons.getName(), deps.length, false);
    return factory.makeAppl(fun, listFields);
  }

  String getName() {
    return type.getName();
  }
}

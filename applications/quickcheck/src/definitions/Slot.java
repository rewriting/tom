/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

import java.io.File;
import java.io.FileOutputStream;

/**
 *
 * @author hubert
 */
public class Slot {

  private Typable type;
  private Constructor cons;
  private Slot[] deps;

  Slot(Typable type) {
    this.type = type;
  }

  Slot[] chooseConstructor() {
    if (type instanceof Algebraic) {
      cons = ((Algebraic) type).chooseConstructor();
      deps = cons.giveATermDeps();
    } else {
      deps = new Slot[0];
    }
    return deps;
  }

  Slot[] chooseMinimalConstructor() {
    if (type instanceof Algebraic) {
      cons = ((Algebraic) type).chooseMinimalConstructor();
      deps = cons.giveATermDeps();
    } else {
      deps = new Slot[0];
    }
    return deps;
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
}

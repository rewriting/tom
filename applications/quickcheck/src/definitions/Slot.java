/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

import aterm.AFun;
import aterm.ATerm;
import aterm.ATermAppl;
import aterm.ATermFactory;
import aterm.pure.PureFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author hubert
 */
class Slot {

  private static ATermFactory factory;
  private Buildable type;
  private Constructor cons;
  private Slot[] deps;

  Slot(Buildable type) {
    this.type = type;
  }

  Slot[] chooseFiniteConstructor() {
    if(type instanceof Sort) {
      cons = ((Sort) type).chooseAnyFiniteConstructor();
      deps = cons.giveATermDeps();
    } else {
      deps = new Slot[0];
    }
    return deps;
  }

  Slot[] chooseMinSizeConstructor(StrategyParameters.DistStrategy strategy) {
    if(type instanceof Sort) {
      switch (strategy) {
        case DEPTH:
          cons = ((Sort) type).chooseMinDepthConstructor();
          break;
        case NODES:
          cons = ((Sort) type).chooseMinNodesConstructor();
          break;
        default:
          throw new RuntimeException("Strategy " + strategy + " is unknown.");
      }
      deps = cons.giveATermDeps();
    } else {
      deps = new Slot[0];
    }
    return deps;
  }

  Slot[] chooseMaxDimConstructor() {
    if(type instanceof Sort) {
      cons = ((Sort) type).chooseMaxDimConstructor();
      deps = cons.giveATermDeps();
    } else {
      deps = new Slot[0];
    }
    return deps;
  }

  int getDimension() {
    return type.getDimension();
  }

  int minimalSize(StrategyParameters.DistStrategy strategy) {
    return type.minimalSize(strategy);
  }

  @Override
  public String toString() {
    String res = "";
    res += cons.getName() + "(";
    int i = 0;
    for(Slot aTerm : deps) {
      res += aTerm;
      if(i != deps.length - 1) {
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
    for(Slot slot : deps) {
      res += "\"" + way + "\"" + "->" + "\"" + way + i + "\"\n";
      res += slot.toDot_aux(way + i);
      i++;
    }
    return res;
  }

  public int toDot2(String chemin) {
    String res = "digraph mon_graphe {\n";
    AtomicInteger n = new AtomicInteger(1);
    res += toDot_aux2(n);
    res += "}\n";
    File fichier = new File(chemin);
    try {
      FileOutputStream graveur = new FileOutputStream(fichier);
      graveur.write(res.getBytes());
      graveur.close();
    } catch (java.io.IOException err) {
      System.err.println("ecriture fichier impossible");
    }
    return n.intValue();
  }

  private String toDot_aux2(AtomicInteger n) {
    int root = n.intValue();
    String res = new String();
    int i = 0;
    for(Slot slot : deps) {
      res += "\"" + root + "\"" + "->" + "\"";
      n.incrementAndGet();
      res += n + "\"\n";
      res += slot.toDot_aux2(n);
      i++;
    }
    return res;
  }

  ATermAppl toATerm() {
    if(factory == null) {
      factory = new PureFactory();
    }
    //PureFactory factory = new PureFactory();
    ATerm[] listFields = new ATerm[deps.length];
    for(int i = 0; i < listFields.length; i++) {
      listFields[i] = deps[i].toATerm();
    }
    AFun fun = factory.makeAFun(cons.getName(), deps.length, false);
    return factory.makeAppl(fun, listFields);
  }

  String getName() {
    return type.getName();
  }
}

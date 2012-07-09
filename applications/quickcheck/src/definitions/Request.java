/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

import java.util.HashSet;

/**
 *
 * @author hubert
 */
public abstract class Request {

  int counter;

  public Request(int initialValue) {
    counter = initialValue;
  }
  
  public int getCounter(){
    return counter;
  }

  abstract Request getNewRequestWith(int d);

  /**
   * Cette methode permet de construire les requestes a passer lors dela
   * creation des champs du constructeur. La repartition des requetes doit tenir
   * compte des dimensions des champs, mais peut aussi infuer sur la forme du
   * graphe en choisissant par exemple 
   *
   * @param cons
   * @return
   */
  abstract Request[] getNewRequestWith(Constructor cons);

  abstract Constructor chooseConstructor(HashSet<Constructor> listConstructors);
}

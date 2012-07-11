/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hubert
 */
public abstract class Request {

  private int counter;

  public Request(int initialValue) {
    counter = initialValue;
  }
  
  public int getCounter(){
    return counter;
  }
  
  public void inc(){
    counter++;
  }
  
  public void setCounter(int n){
    this.counter = n;
  }
  
  public Request copy(){
    try {
      return (Request) this.clone();
    } catch (CloneNotSupportedException ex) {
      Logger.getLogger(Request.class.getName()).log(Level.SEVERE, null, ex);
    }
    throw new UnsupportedOperationException("clone method fails.");
  }
  
  abstract HashSet<ATerm> fillATerm(ATerm aTerm);
  
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

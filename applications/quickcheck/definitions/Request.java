/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

/**
 *
 * @author hubert
 */
public abstract class Request {
  int counter;
  
  public Request(int initialValue){
    counter = initialValue;
  }

  abstract Request getNewRequestWith(int d);

  abstract Request[] getNewRequestWith(Constructor cons);
}

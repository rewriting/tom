/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hubert
 */
class ConstructorExp extends Constructor {
  
  private Method make;

  ConstructorExp(String name, AlgebraicExp caller, Method make) {
    super(name, extractFields(make, caller));
    this.make = make;
  }

  static private Buildable[] extractFields(Method make, AlgebraicExp caller) {
    Class[] listClasses = make.getParameterTypes();
    Buildable[] res = new Buildable[listClasses.length];
    for (int i = 0; i < res.length; i++) {
      res[i] = caller.getScope().searchType(listClasses[i].getName());
    }
    return res;
  }
  
  Object make(Object[] args) {
    try {
      return make.invoke(null, args);
    } catch (IllegalAccessException ex) {
      System.err.println("Method " + make + " enforces Java language access control and the underlying method is inaccessible.");
      Logger.getLogger(Constructor.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalArgumentException ex) {
      System.err.println("Method " + make + " is not static.");
      Logger.getLogger(Constructor.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvocationTargetException ex) {
      System.err.println("Method " + make + " throws exception.");
      Logger.getLogger(Constructor.class.getName()).log(Level.SEVERE, null, ex);
    }
    throw new UnsupportedOperationException("ERROR");
  }
  
}

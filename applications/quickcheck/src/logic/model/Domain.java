/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.model;

import aterm.ATerm;
import definitions.Buildable;
import java.lang.reflect.Method;

/**
 *
 * @author hubert
 */
public class Domain {

  private Buildable model;
  private Method fromTerm;
  private Class type;

  @Deprecated
  public Domain(Class type, Buildable model) {
    this.model = model;
    this.type = type;
    fromTerm = retrieveFromTerm(type);
  }

  public Domain(Buildable model) {
    this.model = model;
    this.fromTerm = null;
    this.type = null;
  }

  @Deprecated
  private Method retrieveFromTerm(Class type) {
    String pattern = "fromTerm";
    Method[] listMethods = type.getDeclaredMethods();
    Method fun = null;
    for (int i = 0; i < listMethods.length; i++) {
      Method method = listMethods[i];
      if (method.getName().equals(pattern)) {
        Class[] params = method.getParameterTypes();
        if (params.length == 1 || params[0].getCanonicalName().equals("aterm.ATerm")) {
          fun = method;
          break;
        }
      }
      if (i == listMethods.length - 1) {
        throw new UnsupportedOperationException("Method " + pattern + "(aterm.ATerm) was not found in " + type);
      }
    }
    return fun;
  }

  public ATerm chooseATerm(int n) {
    return model.generate(n);
  }
}

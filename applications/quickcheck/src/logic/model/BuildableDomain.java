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
public class BuildableDomain implements Domain {

  private Buildable model;
  @Deprecated
  private Method fromTerm;
  @Deprecated
  private Class type;

  public BuildableDomain(Buildable model) {
    this.model = model;
  }

  @Override
  public ATerm chooseElement() {
    return model.generate(100);
  }

  /*
   * =================== DEPRECATED ==========================
   */
  @Deprecated
  public BuildableDomain(Class type, Buildable model) {
    this.model = model;
    this.type = type;
    fromTerm = retrieveFromTerm(type);
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
}

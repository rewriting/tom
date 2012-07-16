/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 *
 * @author hubert
 */
class AlgebraicExp extends Algebraic {

  private final Scope scope;

  public AlgebraicExp(Scope scope, Class type) {
    super(scope, type.getName());
    this.scope = scope;
  }
  
  Scope getScope() {
    return scope;
  }

  /**
   * This methode only work with Gom pattern classes. Indeed, method make()
   * constructed by using Gom is searched in order to build Constructor
   *
   * @param classe class following Gom pattern definition
   * @return
   * @deprecated
   */
  @Deprecated
  public Algebraic addConstructor(Class classe) {
    String pattern = "make";
    Method[] listMethods = classe.getDeclaredMethods();
    Method make = null;
    for (int i = 0; i < listMethods.length; i++) {
      Method method = listMethods[i];
      if (method.getName().equals(pattern)) {
        make = method;
        break;
      }
      if (i == listMethods.length - 1) {
        throw new UnsupportedOperationException("Method " + pattern + "() was not found in " + classe);
      }
    }
    ConstructorExp cons = new ConstructorExp(classe.getSimpleName(), this, make);
    this.getConstructors().add(cons);
    this.getDependences().addAll(Arrays.asList(cons.getFields()));
    return this;
  }

  /**
   * The method make it possible to add constructor by using java class of this
   * constructor.
   *
   * @param classe
   * @param pattern
   * @return
   * @deprecated
   */
  @Deprecated
  public Algebraic addConstructor(Class classe, String pattern) {
    Method[] listMethods = classe.getDeclaredMethods();
    Method make = null;
    for (int i = 0; i < listMethods.length; i++) {
      Method method = listMethods[i];
      if (method.getName().equals(pattern)) {
        make = method;
        break;
      }
      if (i == listMethods.length - 1) {
        throw new UnsupportedOperationException("Method " + pattern + "() was not found in " + classe);
      }
    }
    ConstructorExp cons = new ConstructorExp(classe.getSimpleName(), this, make);
    this.getConstructors().add(cons);
    this.getDependences().addAll(Arrays.asList(cons.getFields()));
    return this;
  }
}

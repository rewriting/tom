/**
 * <copyright>
 * </copyright>
 *
 */
package tom.tomDsl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Java Body</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link tom.tomDsl.JavaBody#getBody <em>Body</em>}</li>
 * </ul>
 * </p>
 *
 * @see tom.tomDsl.TomDslPackage#getJavaBody()
 * @model
 * @generated
 */
public interface JavaBody extends EObject
{
  /**
   * Returns the value of the '<em><b>Body</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Body</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Body</em>' attribute.
   * @see #setBody(String)
   * @see tom.tomDsl.TomDslPackage#getJavaBody_Body()
   * @model
   * @generated
   */
  String getBody();

  /**
   * Sets the value of the '{@link tom.tomDsl.JavaBody#getBody <em>Body</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Body</em>' attribute.
   * @see #getBody()
   * @generated
   */
  void setBody(String value);

} // JavaBody

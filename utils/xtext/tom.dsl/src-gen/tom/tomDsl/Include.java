/**
 * <copyright>
 * </copyright>
 *
 */
package tom.tomDsl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Include</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link tom.tomDsl.Include#getPath <em>Path</em>}</li>
 * </ul>
 * </p>
 *
 * @see tom.tomDsl.TomDslPackage#getInclude()
 * @model
 * @generated
 */
public interface Include extends EObject
{
  /**
   * Returns the value of the '<em><b>Path</b></em>' attribute list.
   * The list contents are of type {@link java.lang.String}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Path</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Path</em>' attribute list.
   * @see tom.tomDsl.TomDslPackage#getInclude_Path()
   * @model unique="false"
   * @generated
   */
  EList<String> getPath();

} // Include

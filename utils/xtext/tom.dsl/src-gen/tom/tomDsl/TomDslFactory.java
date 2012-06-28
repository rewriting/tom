/**
 * <copyright>
 * </copyright>
 *
 */
package tom.tomDsl;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see tom.tomDsl.TomDslPackage
 * @generated
 */
public interface TomDslFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  TomDslFactory eINSTANCE = tom.tomDsl.impl.TomDslFactoryImpl.init();

  /**
   * Returns a new object of class '<em>Tom File</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Tom File</em>'.
   * @generated
   */
  TomFile createTomFile();

  /**
   * Returns a new object of class '<em>Array Operation</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Array Operation</em>'.
   * @generated
   */
  ArrayOperation createArrayOperation();

  /**
   * Returns a new object of class '<em>Include</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Include</em>'.
   * @generated
   */
  Include createInclude();

  /**
   * Returns a new object of class '<em>Operation</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Operation</em>'.
   * @generated
   */
  Operation createOperation();

  /**
   * Returns a new object of class '<em>Operation Array</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Operation Array</em>'.
   * @generated
   */
  OperationArray createOperationArray();

  /**
   * Returns a new object of class '<em>Java Body</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Java Body</em>'.
   * @generated
   */
  JavaBody createJavaBody();

  /**
   * Returns a new object of class '<em>ARG</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>ARG</em>'.
   * @generated
   */
  ARG createARG();

  /**
   * Returns a new object of class '<em>Type Term</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Type Term</em>'.
   * @generated
   */
  TypeTerm createTypeTerm();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  TomDslPackage getTomDslPackage();

} //TomDslFactory

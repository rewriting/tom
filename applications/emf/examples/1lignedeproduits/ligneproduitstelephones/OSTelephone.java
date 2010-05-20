/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package ligneproduitstelephones;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>OS Telephone</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link ligneproduitstelephones.OSTelephone#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see ligneproduitstelephones.LigneproduitstelephonesPackage#getOSTelephone()
 * @model
 * @generated
 */
public interface OSTelephone extends EObject {

	/**
	 * Returns the value of the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Version</em>' attribute.
	 * @see #setVersion(int)
	 * @see ligneproduitstelephones.LigneproduitstelephonesPackage#getOSTelephone_Version()
	 * @model
	 * @generated
	 */
	int getVersion();

	/**
	 * Sets the value of the '{@link ligneproduitstelephones.OSTelephone#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version</em>' attribute.
	 * @see #getVersion()
	 * @generated
	 */
	void setVersion(int value);
} // OSTelephone

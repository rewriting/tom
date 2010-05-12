package ligneproduitstelephones;

import org.eclipse.emf.ecore.EObject;

/**
 * @model
 */
public interface Telephone extends EObject {
  /**
   * @model
   */
  String getName();

  /**
	 * Sets the value of the '{@link ligneproduitstelephones.Telephone#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

		/**
   * @model default="false"
   */
  boolean isTactile();

  /**
	 * Sets the value of the '{@link ligneproduitstelephones.Telephone#isTactile <em>Tactile</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Tactile</em>' attribute.
	 * @see #isTactile()
	 * @generated
	 */
	void setTactile(boolean value);

		/**
   * @model
   */
  OSTelephone getOSTelephone();

  /**
	 * Sets the value of the '{@link ligneproduitstelephones.Telephone#getOSTelephone <em>OS Telephone</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>OS Telephone</em>' attribute.
	 * @see ligneproduitstelephones.OSTelephone
	 * @see #getOSTelephone()
	 * @generated
	 */
	void setOSTelephone(OSTelephone value);

		/**
   * @model opposite="telephones"
   */
  Marque getMarque();

		/**
	 * Sets the value of the '{@link ligneproduitstelephones.Telephone#getMarque <em>Marque</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Marque</em>' reference.
	 * @see #getMarque()
	 * @generated
	 */
	void setMarque(Marque value);
}
package ligneproduitstelephones;

import org.eclipse.emf.ecore.EObject;

/**
 * @model
 */
public interface Forfait extends EObject {

	/**
	 *  @model
	 */
	String getName();
	
	/**
	 * Sets the value of the '{@link ligneproduitstelephones.Forfait#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * @model
	 */
	float getPrice();

	/**
	 * Sets the value of the '{@link ligneproduitstelephones.Forfait#getPrice <em>Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price</em>' attribute.
	 * @see #getPrice()
	 * @generated
	 */
	void setPrice(float value);

	/**
	 * @model
	 */
  Operateur getOperateur();

		/**
	 * Sets the value of the '{@link ligneproduitstelephones.Forfait#getOperateur <em>Operateur</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operateur</em>' reference.
	 * @see #getOperateur()
	 * @generated
	 */
	void setOperateur(Operateur value);

		/**
   * @model
   */
  int getHours();

		/**
	 * Sets the value of the '{@link ligneproduitstelephones.Forfait#getHours <em>Hours</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hours</em>' attribute.
	 * @see #getHours()
	 * @generated
	 */
	void setHours(int value);

		/**
   * @model
   */
  boolean hasInternet();

  /**
   * @model
   */
  int getSMS();

	/**
	 * Sets the value of the '{@link ligneproduitstelephones.Forfait#getSMS <em>SMS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>SMS</em>' attribute.
	 * @see #getSMS()
	 * @generated
	 */
	void setSMS(int value);

	/**
  * @model
  * null if the Forfait is not specific to one phone
  */
  Telephone getSpecificPhone();

	/**
	 * Sets the value of the '{@link ligneproduitstelephones.Forfait#getSpecificPhone <em>Specific Phone</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Specific Phone</em>' reference.
	 * @see #getSpecificPhone()
	 * @generated
	 */
	void setSpecificPhone(Telephone value);

}
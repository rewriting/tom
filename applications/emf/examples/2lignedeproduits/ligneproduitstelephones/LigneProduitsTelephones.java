package ligneproduitstelephones;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * @model
 */
public interface LigneProduitsTelephones extends EObject {
  /**
   * @model
   */
  String getName();

  /**
	 * Sets the value of the '{@link ligneproduitstelephones.LigneProduitsTelephones#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

		/**
   * @model containment="true"
   */
  EList<Marque> getMarque();

  /**
   * @model containment="true"
   */
  EList<Telephone> getTelephones();

}
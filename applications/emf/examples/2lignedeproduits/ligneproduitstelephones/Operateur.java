package ligneproduitstelephones;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 *  @model
 */
public interface Operateur extends EObject {

	/**
	 *  @model
	 */
	String getName();
	
	void setName(String value);
	
	/**
	 * @model
	 */
	boolean useOperatorOS();

	/**
	 * @model
	 */
	boolean hasCommercialAgreements();
	
	/**
	   * @model opposite="operateur"
	   */
	EList<Marque> getMarques();
}
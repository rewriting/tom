package ligneproduitstelephones;

/**
 * @model
 */
public interface Operateur {

	/**
	 *  @model
	 */
	String getName();
	
	/**
	 * @model
	 */
	boolean useOperatorOS();
	
	/**
	   * @model opposite="operateur"
	   */
	java.util.List<Marque> getMarques();

  //faire quelque chose sur les forfaits
}

package ligneproduitstelephones;

/**
 * @model
 */
public interface Forfait {

	/**
	 *  @model
	 */
	String getName();
	
	/**
	 * @model
	 */
	float getPrice();

	/**
	 * @model
	 */
  Operateur getOperateur();

  /**
   * @model
   */
  int getHours();

  /**
   * @model
   */
  boolean hasInternet();

  /**
   * @model
   */
  int getSMS();

 /**
  * @model
  * null if the Forfait is not specific to one phone
  */
  Telephone getSpecificPhone();
}

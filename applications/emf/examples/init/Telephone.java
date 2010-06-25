package ligneproduitstelephones;

/**
 * @model
 */
public interface Telephone {
  /**
   * @model
   */
  String getName();

  /**
   * @model default="false"
   */
  boolean isTactile();

  /**
   * @model
   */
  OSTelephone getOSTelephone();

  /**
   * @model opposite="telephones"
   */
  Marque getMarque();
}

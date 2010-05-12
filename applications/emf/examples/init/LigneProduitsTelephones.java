package ligneproduitstelephones;

/**
 * @model
 */
public interface LigneProduitsTelephones {
  /**
   * @model
   */
  String getName();

  /**
   * @model containment="true"
   */
  java.util.List<Marque> getMarque();

  /**
   * @model containment="true"
   */
  java.util.List<Telephone> getTelephones();
}

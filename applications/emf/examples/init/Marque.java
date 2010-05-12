package ligneproduitstelephones;

/**
 * @model
 */
public interface Marque {
  /**
   * @model
   */
  String getName();

  /**
   * @model opposite="marque"
   */
  java.util.List<Telephone> getTelephones();

  /**
   * @model opposite="marque"
   */
  java.util.List<Operateur> getOperateurs();
}

//Source file: C:\\document\\codegen\\xquery\\lib\\PathAxe.java

package xquery.lib;


public class PathAxe 
{
  public static final int CHILD = 0;
  public static final int ATTRIBUTE = 1;
  public static final int PARENT = 2;
  public static final int SELF = 3;
  public static final int DESCENDANT = 4;
  public static final int DESCENDANT_OR_SELF = 5;
  protected int axeType;
   
  /**
   * @param type
   * @roseuid 4118049D035F
   */
  public PathAxe(int type) 
  {
	this.axeType = type; 
  }
   
  /**
   * @roseuid 4110B63101E2
   */
  public PathAxe() 
  {
	this.axeType = CHILD; 
  }
   
  /**
   * @return int
   * @roseuid 410E0D9301E8
   */
  public int getAxeType() 
  {
    return axeType;
  }
   
  /**
   * @param type
   * @roseuid 410E0F26016F
   */
  public void setAxeType(int type) 
  {
	this.axeType = type; 
  }
   
  /**
   * @return PathAxe
   * @roseuid 410FA11002AC
   */
  public static PathAxe createChildPathAxe() 
  {
    return new PathAxe(CHILD);
  }
   
  /**
   * @return PathAxe
   * @roseuid 410FA12D029A
   */
  public static PathAxe createSelfPathAxe() 
  {
    return new PathAxe(SELF);
  }
   
  /**
   * @return PathAxe
   * @roseuid 410FA12E0147
   */
  public static PathAxe createParentPathAxe() 
  {
    return new PathAxe(PARENT);
  }
   
  /**
   * @return PathAxe
   * @roseuid 410FA12E03D2
   */
  public static PathAxe createAttributePathAxe() 
  {
    return new PathAxe(ATTRIBUTE);
  }
   
  /**
   * @return PathAxe
   * @roseuid 410FA12F0261
   */
  public static PathAxe createDescendantPathAxe() 
  {
    return new PathAxe(DESCENDANT);
  }
   
  /**
   * @return PathAxe
   * @roseuid 410FA13000DC
   */
  public static PathAxe createDescendantOrSelfPathAxe() 
  {
    return new PathAxe(DESCENDANT_OR_SELF);
  }
}

package tom.engine.prettyprinter.ppeditor;

/**An instance of the class PPTextPosition represents a position in a file.
  *@author 
  *@version 1.0 (2011.01.17)
*/
public class PPTextPosition {

/**The line represented by a PPTextPosition.
*/
  private int line;

/**The column represented by a PPTextPosition.
*/
  private int column;

/**Constructs a PPTextPosition at the given line and column.
*@param line line position of the PPTextPosition
*@param column column position of the PPTextPosition
*/
  public PPTextPosition(int line,int column) {
    this.line = line;
    this.column = column;
  }

/**Returns the line position of the PPTextPosition.
*@return value of the line attribute
*/
  public int getLine() {
		return this.line;
  }

/**Returns the column position of the PPTextPosition.
*@return value of the column attribute
*/
  public int getColumn() {
		return this.column;
	}

}

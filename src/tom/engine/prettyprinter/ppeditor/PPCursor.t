package tom.engine.prettyprinter.ppeditor;

import java.util.List;

/**The PPCursor class is used to organize the file in columns and lines 
*/
public class PPCursor {

/**The PPTextPosition which represents the current position in the file
*/
  private PPTextPosition position;
/**The way of writing.
*If true the writeText method insert the text, else the writeText method replaces the text.
*/
  private boolean insertion;
/**A list of StringBuffer used as a local copy of the file
*/
  private List<StringBuffer> fileBuffer;

/**Create a PPCursor
*/
  public PPCursor() {}

/**Move the PPCursor
*@param p the PPTextPosition to place the PPCursor
*/
  public void setPosition(PPTextPosition p) {

    this.position = p;
  }

/**Move the PPCursor
*@param p the PPTextPosition to add to the current PPTextPosition
*/
  public void move(PPTextPosition p) {}

/**Change the way of writing
*@param b the boolean to set the attribute insertion to
*/
  public void setInsertion(boolean b) {

    this.insertion = b;
  }

/**Get the position attribute 
*@return value of the position attribute
*/
  public PPTextPosition getPosition() {

    return position;
  }

/**Get the insertion attribute
*@return value of the insertion attribute
*/
  public boolean isInsertion() {

    return insertion;
  }

/**Read a sequence of the fileBuffer attribute, from the current position to a given PPTextPosition
*@param p the PPTextPosition to stop to
*@return the string between the current position and the given PPTextPosition 
*/
//  public String readFileBuffer(PPTextPosition p) {}

/**Write a string at the current position
*The way of writing (insertion or replacement) is given by the insertion attribute
*@param s the string to write
*/
  public void write(String s) {

    int currentLine = this.getPosition().getLine();
    for(int i = 0; i<s.length(); i++) {

      if(s.charAt(i)!='\n') {
        fileBuffer[currentLine].append(s.charAt(i));
      }else {
        fileBuffer.add(i, new StringBuffer());
        currentLine++;
      }
    }
  }

/**Erase one character
*@param n number of character to erase
*/
  public void erase() {}

/**Reinitialize the fileBuffer attribute to an empty List<StringBuffer>
*/
//  public void eraseAll() {}

/**Read all text
*@return a String which represents the content of the file
*/
  public StringBuffer dump() {}

}

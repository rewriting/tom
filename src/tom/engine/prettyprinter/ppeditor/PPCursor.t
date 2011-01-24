package tom.engine.prettyprinter.ppeditor;

import java.util.List;

/**The PPCursor class is used to organize the file in columns and lines 
*/
public class PPCursor{

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
    public void movePPCursorAbsolutely(PPTextPosition p) {}

/**Move the PPCursor
*@param p the PPTextPosition to add to the current PPTextPosition
*/
    public void movePPCursorRelatively(PPTextPosition p) {}

/**Change the way of writing
*@param b the boolean to set the attribute insertion to
*/
    public void setInsertion(boolean b) {}

/**Get the position attribute 
*@return value of the position attribute
*/
//    public PPTextPosition getPosition() {}

/**Get the insertion attribute
*@return value of the insertion attribute
*/
//    public boolean isInsertion() {}

/**Read a sequence of the fileBuffer attribute, from the current position to the next occurence of a given character
*@param c the character to stop to
*@return the string between the current position and the next occurence of the given character
*/
//    public String readFileBuffer(char c) {}

/**Read a sequence of the fileBuffer attribute, from the current position to a given PPTextPosition
*@param p the PPTextPosition to stop to
*@return the string between the current position and the given PPTextPosition 
*/
//    public String readFileBuffer(PPTextPosition p) {}

/**Write a string at the current position
*The way of writing (insertion or replacement) is given by the insertion attribute
*@param s the string to write
*/
    public void writeText(String s) {}

/**Erase a given number of character
*@param n number of character to erase
*/
    public void eraseText(int n) {}

/**Erase the first occurence of a given String from the current position
*@param s the String to erase
*@return true if a occurence of the given String has been deleted, false if no occurence of the given String has been found 
*/
//    public boolean eraseText(String s) {}

/**Reinitialize the fileBuffer attribute to an empty List<StringBuffer>
*/
    public void eraseAll() {}

}

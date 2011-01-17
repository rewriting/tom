package tom.engine.prettyprinter;

import java.io.*;

/**The PPOutputFormatter class is used to coordinate the different steps 
*in the processus of a text file modification/creation.
*@author
*@version 1.0 (2011.01.17)
*/
public class PPOutputFormatter {

/**The Cursor to move whithin the file.
*/
  private Cursor fileCursor;
/**The FileWriter to modify/create the file.
*/
  private FileWrite fileToModify;
/**A List of PPText which can be written in the file.
*/
  private List<PPText> pPTextList;
  
/**Creates a PPOutputFormatter to coordinate the modification/creation of a File.
*@param aCursor the Cursor which will move whithin the File
*@param aFileWriter the FileWriter which will modify/create the file
*@param aListOfPPText an intermediate position for text fragments before writing them into the file
*/
  public PPOutputFormatter(Cursor aCursor, FileWriter aFileWriter) {}

/**Put a new PPText in the pPTextList.
*@param s the text to stock in a PPText
*@param start the start position of the created PPText
*@param end the end position of the created PPText
*/ 
  public void put(String s, PPTextPosition start, PPTextPosition end) {}

/**All the text contained in the file is transferred to the Cursor, so that the Cursor can manipulate it.
*/
  public void giveFileToCursor(){}

/**All the text contained in the Cursor is written into the file.
*/
  public void writeFile(){}

/**The specified PPText is given to the Cursor.
*@param ppt the PPText to give
*/
  public void giveOnePPTextToPPCursor(PPText ppt){}

/**All the PPText stocked in the pPTextList are given to the Cursor.
*/
  public void giveAllPPTextToCursor(){}

}

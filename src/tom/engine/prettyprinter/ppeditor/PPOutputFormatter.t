package tom.engine.prettyprinter.ppeditor;

import java.io.*;
import java.util.List;

/**The PPOutputFormatter class is used to coordinate the different steps 
*in the processus of a text file's modification/creation.
*@author
*@version 1.0 (2011.01.17)
*/
public class PPOutputFormatter {

/**The PPCursor to move whithin the file.
*/
  private PPCursor fileCursor;
/**The FileWriter to modify/create the file.
*/
  private FileWriter fileToModify;
/**A List of PPText which can be written in the file.
*/
  private List<PPText> ppTextList;
  
/**Creates a PPOutputFormatter to coordinate the modification/creation of a File.
*@param aPPCursor the PPCursor which will move whithin the File
*@param aFileWriter the FileWriter which will modify/create the file
*@param aListOfPPText an intermediate position for text fragments before writing them into the file
*/
  public PPOutputFormatter(PPCursor aPPCursor, FileWriter aFileWriter) {}

/**Put a new PPText in the pPTextList.
*@param s the text to stock in a PPText
*/ 
  public void put(PPText pptext) {}

/**All the text contained in the file is transferred to the PPCursor, so that the Cursor can manipulate it.
*/
  public void giveFileToPPCursor(){}

/**All the text contained in the PPCursor is written into the file.
*/
  public void writeFile(){}

/**The specified PPText is given to the PPCursor.
*@param ppt the PPText to give
*/
  public void giveOnePPTextToPPCursor(PPText ppt){}

/**All the PPText stocked in the ppTextList are given to the PPCursor.
*/
  public void giveAllPPTextToPPCursor(){}

/**Returns the PPOutputFormatter's PPCursor.
*/
	/*public PPCursor getPPCursor(){
	}*/

}

package tom.engine.prettyprinter;

import java.io.*;

public class PPOutputFormatter {

  private Cursor fileCursor;
  private File fileToModify;
  private List<PPText> textToWrite;
  
  public void put(String s, PPTextPosition start, PPTextPosition end) {}

  public void put(String s) {}

  public void giveFileToCursor(){}

  public void writeFile(){}

  public void giveOnePPTextToPPCursor(PPText t){}

  public void giveAllPPTextToCursor(){}

}

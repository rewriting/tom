package ppeditor;

import java.util.*;
import java.lang.*;
import java.io.*;

/**The PPCursor class is used to organize the file in columns and lines 
*/
public class PPCursor {

/**The PPTextPosition which represents the current position in the file
*/
  private PPTextPosition position;

/**The way of writing.
*If true the writeText method inserts the new text inside the existing text, else the writeText method replaces the original text.
*/
  private boolean insertion;

/**A list of StringBuffer used as a local copy of the file
*/
  private ArrayList<StringBuffer> fileBuffer;

/**Creates a PPCursor
*/
  public PPCursor(int l, int c, String fileName) {
    
    this.position = new PPTextPosition(l,c);
    insertion=true;
    fileBuffer = new ArrayList<StringBuffer>();
    fileBuffer.add(new StringBuffer(""));
  }

/**Moves the PPCursor to an absolute position
*@param p the PPTextPosition to place the PPCursor
*/

  public void setPosition(PPTextPosition p) {

    this.position = p;
  }

/**Moves the PPCursor to a relative position from the current position
*@param delta the PPTextPosition to add to the current position
*/
  public void move(PPTextPosition delta) {

    position.add(delta);
  }

/**Changes the way of writing
*@param b the boolean to set the attribute insertion to
*/
  public void setInsertion(boolean b) {

    this.insertion = b;
  }

/**Gets the position attribute 
*@return value of the position attribute
*/
  public PPTextPosition getPosition() {

    return position;
  }

/**Gets the insertion attribute
*@return value of the insertion attribute
*/
  public boolean isInsertion() {

    return insertion;
  }

/**For debug purpose. Returns the fileBuffer of this PPCursor.
*@return the fileBuffer of this PPCursor
*/
  public ArrayList<StringBuffer> getFileBuffer() {

    return this.fileBuffer;
}

/**Reads a sequence of the fileBuffer attribute, from the current position to a given PPTextPosition
*@param p the PPTextPosition to stop to
*@return the string between the current position and the given PPTextPosition 
*/
//  public String readFileBuffer(PPTextPosition p) {}

/**Writes a string at the current position
*The way of writing (insertion or replacement) is given by the insertion attribute
*@param s the string to write
*/
  public void write(String s) {
    if(position.getLine()>fileBuffer.size()){
      for(int l=fileBuffer.size();l<position.getLine()+1;l++){
        fileBuffer.add(new StringBuffer(""));
      }
    }
    for(int i=0;i<s.length();i++){
          if(s.charAt(i)!='\\'){
          fileBuffer.get(position.getLine()).insert(position.getColumn(),new StringBuffer(s.charAt(i)));
        if(!insertion){
          fileBuffer.get(position.getLine()).deleteCharAt(position.getColumn()+1);
        }
        position.add(new PPTextPosition(1,0));
      }else if(s.charAt(i+1)=='n'){
        i++;
        if(insertion){
          fileBuffer.add(position.getLine()+1, new StringBuffer(fileBuffer.get(position.getLine()).subSequence(position.getColumn(), fileBuffer.get(position.getLine()).length()-1)));
        }else if(fileBuffer.size()==position.getLine()+1){
          fileBuffer.add(new StringBuffer(""));
        }
        position.set(position.getLine()+1,0);

      }
    }
  }

/**Erases one character
*/
  public void erase() {
		fileBuffer.get(position.getLine()).deleteCharAt(position.getColumn());
  }

/**Reinitializes the fileBuffer attribute to an empty List<StringBuffer>
*/
  public void eraseAll() {
		fileBuffer.clear();
  }

/**Creates a text file with the fileBuffer as a content.
*@param fileName the name of the created file
*@return a StringBuffer which represents the content of the created file
*/
  public StringBuffer dump(String fileName) {
    StringBuffer content = new StringBuffer("");
    for(int i=0;i<fileBuffer.size()-1;i++){
      content.append(fileBuffer.get(i)+"\n");
    }
    content.append(fileBuffer.get(fileBuffer.size()-1));
    try{
      FileWriter textFile = new FileWriter(fileName);
      textFile.write(content.toString());
    }catch(IOException ioe){
      System.out.println("Writing failed.");
    }
    return content;
  }

}

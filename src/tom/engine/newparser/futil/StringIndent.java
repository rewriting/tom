package tom.engine.newparser.futil;

/**
 * add a visible indentation to a String.
 */
public class StringIndent {

  private StringIndent(){;}
  
  /**
   * Return a String where each line from input is prefixed whith
   * "level" '|' chars, separated by "spaces" '_' chars.
   * 
   * @param input
   * @param level
   * @param spaces
   * @return
   */
  public static String indent(String input, int level, int spaces){
    String res = "";
    
    String lines[] = input.split("\n");
    for(String line : lines){
      for(int i=0; i<level; i++){
        for(int j=0; j<spaces; j++){
          res+=" ";
        }
      res+="|";
      }
      
      res+=line+"\n";
    }
    return res;
  }
  
}

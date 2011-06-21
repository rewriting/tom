package futil;

public class StringIndent {

  private StringIndent(){;}
  
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

package tom.engine.parser.antlr3;
import org.antlr.runtime.CharStream;

public class EOFdetector extends KeywordDetector {

  public EOFdetector(){
    super(""+(char)CharStream.EOF);
  }
  
}

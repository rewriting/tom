package tom.engine.parser.antlr3.streamanalysis;
import org.antlr.runtime.CharStream;

public class EOFdetector extends KeywordDetector {

  public EOFdetector(){
    super(""+(char)CharStream.EOF);
  }
  
}

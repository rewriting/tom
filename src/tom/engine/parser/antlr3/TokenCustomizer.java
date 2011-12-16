package tom.engine.parser.antlr3;
import org.antlr.runtime.Token;


public class TokenCustomizer {

  Object nextTokenPayload = null;
  
  public TokenCustomizer() {}
  
  public Token customize(Token oldToken){
    if(nextTokenPayload == null) {
      return oldToken;
    }else{
      Token customToken = new CustomToken(oldToken, nextTokenPayload);
      nextTokenPayload = null;
      return customToken;
    }
  }
  
  public <E> void prepareNextToken(E payload){
    if(nextTokenPayload!=null){
      throw new RuntimeException("NextTokenPayload already prepared");
    }
    nextTokenPayload = payload; 
  }
}

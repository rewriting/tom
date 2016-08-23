package tom.engine.parser.antlr3;

import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;

/**
 * Extends Antlr's Tokens to pass Object from Lexer
 * to Parser using Token
 */
@SuppressWarnings("serial")
public class CustomToken extends CommonToken{

  private Object payload = null;
  
  public <E> CustomToken(Token oldToken, E payload) {
    super(oldToken);
    this.payload = payload;
  }
  
  public <E> E getPayload(Class<E> payloadClass){
    return payloadClass.cast(payload); 
  }
}

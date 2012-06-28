package tom.ui;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.FailedPredicateException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;

import tom.ui.contentassist.antlr.internal.InternalTomDslLexer;

public class CustomTomMappingUILexer extends InternalTomDslLexer{

    public CustomTomMappingUILexer() {;} 
    public CustomTomMappingUILexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public CustomTomMappingUILexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    int depth= 0;
    public CustomTomMappingUILexer(ANTLRStringStream antlrStringStream) {
		super(antlrStringStream);
	}
	
    boolean lookAheadMatch(String s) {
    	int i=0;
		//System.out.println("lookAheadMatch("+s+":"+s.length()+")");
    	while (i<s.length()) {
    		int la = input.LA(i+1);
			//System.out.println("Comparing "+la+"!="+((int)s.charAt(i)));
    		if (la!=s.charAt(i) ) {
    			return false;
    		}
    		i++;
    	}
    	return true;
    }

    @Override
	public void mTokens() throws RecognitionException {
		if(lookAheadMatch("{") ) {
			//System.out.println("found "+input.LA(0)+" disambiguitating ");
			if(depth==0) {
				super.mRULE_FIRST_LEVEL_LBRACKET();
				depth++;
			} else {
				super.mRULE_BRCKTSTMT();
			}
		} else {
			if (lookAheadMatch("}") && depth==1) {
				depth=0;
			}
			super.mTokens();
		}
		
	}

}

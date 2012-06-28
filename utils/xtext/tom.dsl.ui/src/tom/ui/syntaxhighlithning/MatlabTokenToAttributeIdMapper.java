package tom.ui.syntaxhighlithning;

import java.util.regex.Pattern;

import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.AbstractAntlrTokenToAttributeIdMapper;

//import org.olive.xtext.lexer.ClobLexer;

public class MatlabTokenToAttributeIdMapper extends AbstractAntlrTokenToAttributeIdMapper {


	private static final Pattern KEYWORD= 
		Pattern.compile("\"for\"|\"if\"|\"else\"|\"while\"|\"switch\"|\"case\"|\"otherwise\"|\"end\"|\"function'\"");

//	@Override
//	protected String calculateId(String tokenName, int tokenType) {
//		if(PUNCTUATION.matcher(tokenName).matches()) {
//			return DefaultHighlightingConfiguration.PUNCTUATION_ID;
//		}
//		if(QUOTED.matcher(tokenName).matches()) {
//			return DefaultHighlightingConfiguration.KEYWORD_ID;
//		}
//		if("RULE_STRING".equals(tokenName)) {
//			return DefaultHighlightingConfiguration.STRING_ID;
//		}
//		if("RULE_INT".equals(tokenName)) {
//			return DefaultHighlightingConfiguration.NUMBER_ID;
//		}
//		if("RULE_ML_COMMENT".equals(tokenName) || "RULE_SL_COMMENT".equals(tokenName)) {
//			return DefaultHighlightingConfiguration.COMMENT_ID;
//		}
//		return DefaultHighlightingConfiguration.DEFAULT_ID;
//	}
	
	
		@Override
        protected String calculateId(String tokenName, int tokenType) {
			
                if (tokenName.startsWith("%pragma")) {
                        return MatlabHighlightingConfiguration.PRAGMA_ID;
                } else if (tokenName.startsWith("RULE_P_")) {
                        return DefaultHighlightingConfiguration.PUNCTUATION_ID;
                } else if (tokenName.equals("RULE_T_TEXT")) {
                        return DefaultHighlightingConfiguration.STRING_ID;
                } else if (tokenName.equals("RULE_S_BEGIN") || tokenName.equals("RULE_S_END")) {
                        return DefaultHighlightingConfiguration.STRING_ID;
                } else if (tokenName.startsWith("RULE_C_")) {
                        return DefaultHighlightingConfiguration.COMMENT_ID;
                } else {
                        return DefaultHighlightingConfiguration.INVALID_TOKEN_ID;
                } 
        }
}
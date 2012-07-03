package tom.mapping.dsl.parser.antlr.internal; 

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import tom.mapping.dsl.services.TomMappingGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalTomMappingParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'TomMapping'", "';'", "'prefix'", "'import'", "'terminals'", "'{'", "'define'", "','", "'}'", "'use'", "'operators'", "'module'", "':'", "'[]'", "'alias'", "'::'", "'('", "')'", "'op'", "'make'", "'='", "'is_fsym'", "'slot'", "'ignore'", "'.'"
    };
    public static final int RULE_ID=4;
    public static final int T__29=29;
    public static final int T__28=28;
    public static final int T__27=27;
    public static final int T__26=26;
    public static final int T__25=25;
    public static final int T__24=24;
    public static final int T__23=23;
    public static final int T__22=22;
    public static final int RULE_ANY_OTHER=10;
    public static final int T__21=21;
    public static final int T__20=20;
    public static final int RULE_SL_COMMENT=8;
    public static final int EOF=-1;
    public static final int RULE_ML_COMMENT=7;
    public static final int T__30=30;
    public static final int T__19=19;
    public static final int T__31=31;
    public static final int RULE_STRING=5;
    public static final int T__32=32;
    public static final int T__33=33;
    public static final int T__16=16;
    public static final int T__34=34;
    public static final int T__15=15;
    public static final int T__35=35;
    public static final int T__18=18;
    public static final int T__17=17;
    public static final int T__12=12;
    public static final int T__11=11;
    public static final int T__14=14;
    public static final int T__13=13;
    public static final int RULE_INT=6;
    public static final int RULE_WS=9;

    // delegates
    // delegators


        public InternalTomMappingParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalTomMappingParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalTomMappingParser.tokenNames; }
    public String getGrammarFileName() { return "../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g"; }



     	private TomMappingGrammarAccess grammarAccess;
     	
        public InternalTomMappingParser(TokenStream input, TomMappingGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }
        
        @Override
        protected String getFirstRuleName() {
        	return "Mapping";	
       	}
       	
       	@Override
       	protected TomMappingGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}



    // $ANTLR start "entryRuleMapping"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:67:1: entryRuleMapping returns [EObject current=null] : iv_ruleMapping= ruleMapping EOF ;
    public final EObject entryRuleMapping() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMapping = null;


        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:68:2: (iv_ruleMapping= ruleMapping EOF )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:69:2: iv_ruleMapping= ruleMapping EOF
            {
             newCompositeNode(grammarAccess.getMappingRule()); 
            pushFollow(FOLLOW_ruleMapping_in_entryRuleMapping75);
            iv_ruleMapping=ruleMapping();

            state._fsp--;

             current =iv_ruleMapping; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleMapping85); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleMapping"


    // $ANTLR start "ruleMapping"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:76:1: ruleMapping returns [EObject current=null] : (otherlv_0= 'TomMapping' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ';' (otherlv_3= 'prefix' ( (lv_prefix_4_0= RULE_STRING ) ) otherlv_5= ';' )? (otherlv_6= 'import' ( (lv_imports_7_0= ruleImport ) ) otherlv_8= ';' )* (otherlv_9= 'terminals' otherlv_10= '{' ( ( (otherlv_11= 'define' otherlv_12= '{' ( (lv_terminals_13_0= ruleTerminal ) ) (otherlv_14= ',' ( (lv_terminals_15_0= ruleTerminal ) ) )* otherlv_16= '}' )? (otherlv_17= 'use' otherlv_18= '{' ( (otherlv_19= RULE_ID ) ) (otherlv_20= ',' ( (otherlv_21= RULE_ID ) ) )* otherlv_22= '}' )? ) | ( ( (lv_terminals_23_0= ruleTerminal ) ) (otherlv_24= ',' ( (lv_terminals_25_0= ruleTerminal ) ) )* ) ) otherlv_26= '}' )? ( (otherlv_27= 'operators' otherlv_28= '{' ( (lv_operators_29_0= ruleOperator ) ) (otherlv_30= ';' ( (lv_operators_31_0= ruleOperator ) ) )* otherlv_32= ';' otherlv_33= '}' ) | ( (lv_modules_34_0= ruleModule ) ) )* ) ;
    public final EObject ruleMapping() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token lv_prefix_4_0=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        Token otherlv_8=null;
        Token otherlv_9=null;
        Token otherlv_10=null;
        Token otherlv_11=null;
        Token otherlv_12=null;
        Token otherlv_14=null;
        Token otherlv_16=null;
        Token otherlv_17=null;
        Token otherlv_18=null;
        Token otherlv_19=null;
        Token otherlv_20=null;
        Token otherlv_21=null;
        Token otherlv_22=null;
        Token otherlv_24=null;
        Token otherlv_26=null;
        Token otherlv_27=null;
        Token otherlv_28=null;
        Token otherlv_30=null;
        Token otherlv_32=null;
        Token otherlv_33=null;
        EObject lv_imports_7_0 = null;

        EObject lv_terminals_13_0 = null;

        EObject lv_terminals_15_0 = null;

        EObject lv_terminals_23_0 = null;

        EObject lv_terminals_25_0 = null;

        EObject lv_operators_29_0 = null;

        EObject lv_operators_31_0 = null;

        EObject lv_modules_34_0 = null;


         enterRule(); 
            
        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:79:28: ( (otherlv_0= 'TomMapping' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ';' (otherlv_3= 'prefix' ( (lv_prefix_4_0= RULE_STRING ) ) otherlv_5= ';' )? (otherlv_6= 'import' ( (lv_imports_7_0= ruleImport ) ) otherlv_8= ';' )* (otherlv_9= 'terminals' otherlv_10= '{' ( ( (otherlv_11= 'define' otherlv_12= '{' ( (lv_terminals_13_0= ruleTerminal ) ) (otherlv_14= ',' ( (lv_terminals_15_0= ruleTerminal ) ) )* otherlv_16= '}' )? (otherlv_17= 'use' otherlv_18= '{' ( (otherlv_19= RULE_ID ) ) (otherlv_20= ',' ( (otherlv_21= RULE_ID ) ) )* otherlv_22= '}' )? ) | ( ( (lv_terminals_23_0= ruleTerminal ) ) (otherlv_24= ',' ( (lv_terminals_25_0= ruleTerminal ) ) )* ) ) otherlv_26= '}' )? ( (otherlv_27= 'operators' otherlv_28= '{' ( (lv_operators_29_0= ruleOperator ) ) (otherlv_30= ';' ( (lv_operators_31_0= ruleOperator ) ) )* otherlv_32= ';' otherlv_33= '}' ) | ( (lv_modules_34_0= ruleModule ) ) )* ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:80:1: (otherlv_0= 'TomMapping' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ';' (otherlv_3= 'prefix' ( (lv_prefix_4_0= RULE_STRING ) ) otherlv_5= ';' )? (otherlv_6= 'import' ( (lv_imports_7_0= ruleImport ) ) otherlv_8= ';' )* (otherlv_9= 'terminals' otherlv_10= '{' ( ( (otherlv_11= 'define' otherlv_12= '{' ( (lv_terminals_13_0= ruleTerminal ) ) (otherlv_14= ',' ( (lv_terminals_15_0= ruleTerminal ) ) )* otherlv_16= '}' )? (otherlv_17= 'use' otherlv_18= '{' ( (otherlv_19= RULE_ID ) ) (otherlv_20= ',' ( (otherlv_21= RULE_ID ) ) )* otherlv_22= '}' )? ) | ( ( (lv_terminals_23_0= ruleTerminal ) ) (otherlv_24= ',' ( (lv_terminals_25_0= ruleTerminal ) ) )* ) ) otherlv_26= '}' )? ( (otherlv_27= 'operators' otherlv_28= '{' ( (lv_operators_29_0= ruleOperator ) ) (otherlv_30= ';' ( (lv_operators_31_0= ruleOperator ) ) )* otherlv_32= ';' otherlv_33= '}' ) | ( (lv_modules_34_0= ruleModule ) ) )* )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:80:1: (otherlv_0= 'TomMapping' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ';' (otherlv_3= 'prefix' ( (lv_prefix_4_0= RULE_STRING ) ) otherlv_5= ';' )? (otherlv_6= 'import' ( (lv_imports_7_0= ruleImport ) ) otherlv_8= ';' )* (otherlv_9= 'terminals' otherlv_10= '{' ( ( (otherlv_11= 'define' otherlv_12= '{' ( (lv_terminals_13_0= ruleTerminal ) ) (otherlv_14= ',' ( (lv_terminals_15_0= ruleTerminal ) ) )* otherlv_16= '}' )? (otherlv_17= 'use' otherlv_18= '{' ( (otherlv_19= RULE_ID ) ) (otherlv_20= ',' ( (otherlv_21= RULE_ID ) ) )* otherlv_22= '}' )? ) | ( ( (lv_terminals_23_0= ruleTerminal ) ) (otherlv_24= ',' ( (lv_terminals_25_0= ruleTerminal ) ) )* ) ) otherlv_26= '}' )? ( (otherlv_27= 'operators' otherlv_28= '{' ( (lv_operators_29_0= ruleOperator ) ) (otherlv_30= ';' ( (lv_operators_31_0= ruleOperator ) ) )* otherlv_32= ';' otherlv_33= '}' ) | ( (lv_modules_34_0= ruleModule ) ) )* )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:80:3: otherlv_0= 'TomMapping' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ';' (otherlv_3= 'prefix' ( (lv_prefix_4_0= RULE_STRING ) ) otherlv_5= ';' )? (otherlv_6= 'import' ( (lv_imports_7_0= ruleImport ) ) otherlv_8= ';' )* (otherlv_9= 'terminals' otherlv_10= '{' ( ( (otherlv_11= 'define' otherlv_12= '{' ( (lv_terminals_13_0= ruleTerminal ) ) (otherlv_14= ',' ( (lv_terminals_15_0= ruleTerminal ) ) )* otherlv_16= '}' )? (otherlv_17= 'use' otherlv_18= '{' ( (otherlv_19= RULE_ID ) ) (otherlv_20= ',' ( (otherlv_21= RULE_ID ) ) )* otherlv_22= '}' )? ) | ( ( (lv_terminals_23_0= ruleTerminal ) ) (otherlv_24= ',' ( (lv_terminals_25_0= ruleTerminal ) ) )* ) ) otherlv_26= '}' )? ( (otherlv_27= 'operators' otherlv_28= '{' ( (lv_operators_29_0= ruleOperator ) ) (otherlv_30= ';' ( (lv_operators_31_0= ruleOperator ) ) )* otherlv_32= ';' otherlv_33= '}' ) | ( (lv_modules_34_0= ruleModule ) ) )*
            {
            otherlv_0=(Token)match(input,11,FOLLOW_11_in_ruleMapping122); 

                	newLeafNode(otherlv_0, grammarAccess.getMappingAccess().getTomMappingKeyword_0());
                
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:84:1: ( (lv_name_1_0= RULE_ID ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:85:1: (lv_name_1_0= RULE_ID )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:85:1: (lv_name_1_0= RULE_ID )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:86:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleMapping139); 

            			newLeafNode(lv_name_1_0, grammarAccess.getMappingAccess().getNameIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getMappingRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_1_0, 
                    		"ID");
            	    

            }


            }

            otherlv_2=(Token)match(input,12,FOLLOW_12_in_ruleMapping156); 

                	newLeafNode(otherlv_2, grammarAccess.getMappingAccess().getSemicolonKeyword_2());
                
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:106:1: (otherlv_3= 'prefix' ( (lv_prefix_4_0= RULE_STRING ) ) otherlv_5= ';' )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==13) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:106:3: otherlv_3= 'prefix' ( (lv_prefix_4_0= RULE_STRING ) ) otherlv_5= ';'
                    {
                    otherlv_3=(Token)match(input,13,FOLLOW_13_in_ruleMapping169); 

                        	newLeafNode(otherlv_3, grammarAccess.getMappingAccess().getPrefixKeyword_3_0());
                        
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:110:1: ( (lv_prefix_4_0= RULE_STRING ) )
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:111:1: (lv_prefix_4_0= RULE_STRING )
                    {
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:111:1: (lv_prefix_4_0= RULE_STRING )
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:112:3: lv_prefix_4_0= RULE_STRING
                    {
                    lv_prefix_4_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleMapping186); 

                    			newLeafNode(lv_prefix_4_0, grammarAccess.getMappingAccess().getPrefixSTRINGTerminalRuleCall_3_1_0()); 
                    		

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getMappingRule());
                    	        }
                           		setWithLastConsumed(
                           			current, 
                           			"prefix",
                            		lv_prefix_4_0, 
                            		"STRING");
                    	    

                    }


                    }

                    otherlv_5=(Token)match(input,12,FOLLOW_12_in_ruleMapping203); 

                        	newLeafNode(otherlv_5, grammarAccess.getMappingAccess().getSemicolonKeyword_3_2());
                        

                    }
                    break;

            }

            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:132:3: (otherlv_6= 'import' ( (lv_imports_7_0= ruleImport ) ) otherlv_8= ';' )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==14) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:132:5: otherlv_6= 'import' ( (lv_imports_7_0= ruleImport ) ) otherlv_8= ';'
            	    {
            	    otherlv_6=(Token)match(input,14,FOLLOW_14_in_ruleMapping218); 

            	        	newLeafNode(otherlv_6, grammarAccess.getMappingAccess().getImportKeyword_4_0());
            	        
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:136:1: ( (lv_imports_7_0= ruleImport ) )
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:137:1: (lv_imports_7_0= ruleImport )
            	    {
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:137:1: (lv_imports_7_0= ruleImport )
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:138:3: lv_imports_7_0= ruleImport
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getMappingAccess().getImportsImportParserRuleCall_4_1_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleImport_in_ruleMapping239);
            	    lv_imports_7_0=ruleImport();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getMappingRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"imports",
            	            		lv_imports_7_0, 
            	            		"Import");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }

            	    otherlv_8=(Token)match(input,12,FOLLOW_12_in_ruleMapping251); 

            	        	newLeafNode(otherlv_8, grammarAccess.getMappingAccess().getSemicolonKeyword_4_2());
            	        

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:158:3: (otherlv_9= 'terminals' otherlv_10= '{' ( ( (otherlv_11= 'define' otherlv_12= '{' ( (lv_terminals_13_0= ruleTerminal ) ) (otherlv_14= ',' ( (lv_terminals_15_0= ruleTerminal ) ) )* otherlv_16= '}' )? (otherlv_17= 'use' otherlv_18= '{' ( (otherlv_19= RULE_ID ) ) (otherlv_20= ',' ( (otherlv_21= RULE_ID ) ) )* otherlv_22= '}' )? ) | ( ( (lv_terminals_23_0= ruleTerminal ) ) (otherlv_24= ',' ( (lv_terminals_25_0= ruleTerminal ) ) )* ) ) otherlv_26= '}' )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==15) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:158:5: otherlv_9= 'terminals' otherlv_10= '{' ( ( (otherlv_11= 'define' otherlv_12= '{' ( (lv_terminals_13_0= ruleTerminal ) ) (otherlv_14= ',' ( (lv_terminals_15_0= ruleTerminal ) ) )* otherlv_16= '}' )? (otherlv_17= 'use' otherlv_18= '{' ( (otherlv_19= RULE_ID ) ) (otherlv_20= ',' ( (otherlv_21= RULE_ID ) ) )* otherlv_22= '}' )? ) | ( ( (lv_terminals_23_0= ruleTerminal ) ) (otherlv_24= ',' ( (lv_terminals_25_0= ruleTerminal ) ) )* ) ) otherlv_26= '}'
                    {
                    otherlv_9=(Token)match(input,15,FOLLOW_15_in_ruleMapping266); 

                        	newLeafNode(otherlv_9, grammarAccess.getMappingAccess().getTerminalsKeyword_5_0());
                        
                    otherlv_10=(Token)match(input,16,FOLLOW_16_in_ruleMapping278); 

                        	newLeafNode(otherlv_10, grammarAccess.getMappingAccess().getLeftCurlyBracketKeyword_5_1());
                        
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:166:1: ( ( (otherlv_11= 'define' otherlv_12= '{' ( (lv_terminals_13_0= ruleTerminal ) ) (otherlv_14= ',' ( (lv_terminals_15_0= ruleTerminal ) ) )* otherlv_16= '}' )? (otherlv_17= 'use' otherlv_18= '{' ( (otherlv_19= RULE_ID ) ) (otherlv_20= ',' ( (otherlv_21= RULE_ID ) ) )* otherlv_22= '}' )? ) | ( ( (lv_terminals_23_0= ruleTerminal ) ) (otherlv_24= ',' ( (lv_terminals_25_0= ruleTerminal ) ) )* ) )
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==17||(LA8_0>=19 && LA8_0<=20)) ) {
                        alt8=1;
                    }
                    else if ( (LA8_0==RULE_ID) ) {
                        alt8=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 8, 0, input);

                        throw nvae;
                    }
                    switch (alt8) {
                        case 1 :
                            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:166:2: ( (otherlv_11= 'define' otherlv_12= '{' ( (lv_terminals_13_0= ruleTerminal ) ) (otherlv_14= ',' ( (lv_terminals_15_0= ruleTerminal ) ) )* otherlv_16= '}' )? (otherlv_17= 'use' otherlv_18= '{' ( (otherlv_19= RULE_ID ) ) (otherlv_20= ',' ( (otherlv_21= RULE_ID ) ) )* otherlv_22= '}' )? )
                            {
                            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:166:2: ( (otherlv_11= 'define' otherlv_12= '{' ( (lv_terminals_13_0= ruleTerminal ) ) (otherlv_14= ',' ( (lv_terminals_15_0= ruleTerminal ) ) )* otherlv_16= '}' )? (otherlv_17= 'use' otherlv_18= '{' ( (otherlv_19= RULE_ID ) ) (otherlv_20= ',' ( (otherlv_21= RULE_ID ) ) )* otherlv_22= '}' )? )
                            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:166:3: (otherlv_11= 'define' otherlv_12= '{' ( (lv_terminals_13_0= ruleTerminal ) ) (otherlv_14= ',' ( (lv_terminals_15_0= ruleTerminal ) ) )* otherlv_16= '}' )? (otherlv_17= 'use' otherlv_18= '{' ( (otherlv_19= RULE_ID ) ) (otherlv_20= ',' ( (otherlv_21= RULE_ID ) ) )* otherlv_22= '}' )?
                            {
                            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:166:3: (otherlv_11= 'define' otherlv_12= '{' ( (lv_terminals_13_0= ruleTerminal ) ) (otherlv_14= ',' ( (lv_terminals_15_0= ruleTerminal ) ) )* otherlv_16= '}' )?
                            int alt4=2;
                            int LA4_0 = input.LA(1);

                            if ( (LA4_0==17) ) {
                                alt4=1;
                            }
                            switch (alt4) {
                                case 1 :
                                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:166:5: otherlv_11= 'define' otherlv_12= '{' ( (lv_terminals_13_0= ruleTerminal ) ) (otherlv_14= ',' ( (lv_terminals_15_0= ruleTerminal ) ) )* otherlv_16= '}'
                                    {
                                    otherlv_11=(Token)match(input,17,FOLLOW_17_in_ruleMapping293); 

                                        	newLeafNode(otherlv_11, grammarAccess.getMappingAccess().getDefineKeyword_5_2_0_0_0());
                                        
                                    otherlv_12=(Token)match(input,16,FOLLOW_16_in_ruleMapping305); 

                                        	newLeafNode(otherlv_12, grammarAccess.getMappingAccess().getLeftCurlyBracketKeyword_5_2_0_0_1());
                                        
                                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:174:1: ( (lv_terminals_13_0= ruleTerminal ) )
                                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:175:1: (lv_terminals_13_0= ruleTerminal )
                                    {
                                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:175:1: (lv_terminals_13_0= ruleTerminal )
                                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:176:3: lv_terminals_13_0= ruleTerminal
                                    {
                                     
                                    	        newCompositeNode(grammarAccess.getMappingAccess().getTerminalsTerminalParserRuleCall_5_2_0_0_2_0()); 
                                    	    
                                    pushFollow(FOLLOW_ruleTerminal_in_ruleMapping326);
                                    lv_terminals_13_0=ruleTerminal();

                                    state._fsp--;


                                    	        if (current==null) {
                                    	            current = createModelElementForParent(grammarAccess.getMappingRule());
                                    	        }
                                           		add(
                                           			current, 
                                           			"terminals",
                                            		lv_terminals_13_0, 
                                            		"Terminal");
                                    	        afterParserOrEnumRuleCall();
                                    	    

                                    }


                                    }

                                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:192:2: (otherlv_14= ',' ( (lv_terminals_15_0= ruleTerminal ) ) )*
                                    loop3:
                                    do {
                                        int alt3=2;
                                        int LA3_0 = input.LA(1);

                                        if ( (LA3_0==18) ) {
                                            alt3=1;
                                        }


                                        switch (alt3) {
                                    	case 1 :
                                    	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:192:4: otherlv_14= ',' ( (lv_terminals_15_0= ruleTerminal ) )
                                    	    {
                                    	    otherlv_14=(Token)match(input,18,FOLLOW_18_in_ruleMapping339); 

                                    	        	newLeafNode(otherlv_14, grammarAccess.getMappingAccess().getCommaKeyword_5_2_0_0_3_0());
                                    	        
                                    	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:196:1: ( (lv_terminals_15_0= ruleTerminal ) )
                                    	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:197:1: (lv_terminals_15_0= ruleTerminal )
                                    	    {
                                    	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:197:1: (lv_terminals_15_0= ruleTerminal )
                                    	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:198:3: lv_terminals_15_0= ruleTerminal
                                    	    {
                                    	     
                                    	    	        newCompositeNode(grammarAccess.getMappingAccess().getTerminalsTerminalParserRuleCall_5_2_0_0_3_1_0()); 
                                    	    	    
                                    	    pushFollow(FOLLOW_ruleTerminal_in_ruleMapping360);
                                    	    lv_terminals_15_0=ruleTerminal();

                                    	    state._fsp--;


                                    	    	        if (current==null) {
                                    	    	            current = createModelElementForParent(grammarAccess.getMappingRule());
                                    	    	        }
                                    	           		add(
                                    	           			current, 
                                    	           			"terminals",
                                    	            		lv_terminals_15_0, 
                                    	            		"Terminal");
                                    	    	        afterParserOrEnumRuleCall();
                                    	    	    

                                    	    }


                                    	    }


                                    	    }
                                    	    break;

                                    	default :
                                    	    break loop3;
                                        }
                                    } while (true);

                                    otherlv_16=(Token)match(input,19,FOLLOW_19_in_ruleMapping374); 

                                        	newLeafNode(otherlv_16, grammarAccess.getMappingAccess().getRightCurlyBracketKeyword_5_2_0_0_4());
                                        

                                    }
                                    break;

                            }

                            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:218:3: (otherlv_17= 'use' otherlv_18= '{' ( (otherlv_19= RULE_ID ) ) (otherlv_20= ',' ( (otherlv_21= RULE_ID ) ) )* otherlv_22= '}' )?
                            int alt6=2;
                            int LA6_0 = input.LA(1);

                            if ( (LA6_0==20) ) {
                                alt6=1;
                            }
                            switch (alt6) {
                                case 1 :
                                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:218:5: otherlv_17= 'use' otherlv_18= '{' ( (otherlv_19= RULE_ID ) ) (otherlv_20= ',' ( (otherlv_21= RULE_ID ) ) )* otherlv_22= '}'
                                    {
                                    otherlv_17=(Token)match(input,20,FOLLOW_20_in_ruleMapping389); 

                                        	newLeafNode(otherlv_17, grammarAccess.getMappingAccess().getUseKeyword_5_2_0_1_0());
                                        
                                    otherlv_18=(Token)match(input,16,FOLLOW_16_in_ruleMapping401); 

                                        	newLeafNode(otherlv_18, grammarAccess.getMappingAccess().getLeftCurlyBracketKeyword_5_2_0_1_1());
                                        
                                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:226:1: ( (otherlv_19= RULE_ID ) )
                                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:227:1: (otherlv_19= RULE_ID )
                                    {
                                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:227:1: (otherlv_19= RULE_ID )
                                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:228:3: otherlv_19= RULE_ID
                                    {

                                    			if (current==null) {
                                    	            current = createModelElement(grammarAccess.getMappingRule());
                                    	        }
                                            
                                    otherlv_19=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleMapping421); 

                                    		newLeafNode(otherlv_19, grammarAccess.getMappingAccess().getExternalTerminalsTerminalCrossReference_5_2_0_1_2_0()); 
                                    	

                                    }


                                    }

                                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:239:2: (otherlv_20= ',' ( (otherlv_21= RULE_ID ) ) )*
                                    loop5:
                                    do {
                                        int alt5=2;
                                        int LA5_0 = input.LA(1);

                                        if ( (LA5_0==18) ) {
                                            alt5=1;
                                        }


                                        switch (alt5) {
                                    	case 1 :
                                    	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:239:4: otherlv_20= ',' ( (otherlv_21= RULE_ID ) )
                                    	    {
                                    	    otherlv_20=(Token)match(input,18,FOLLOW_18_in_ruleMapping434); 

                                    	        	newLeafNode(otherlv_20, grammarAccess.getMappingAccess().getCommaKeyword_5_2_0_1_3_0());
                                    	        
                                    	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:243:1: ( (otherlv_21= RULE_ID ) )
                                    	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:244:1: (otherlv_21= RULE_ID )
                                    	    {
                                    	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:244:1: (otherlv_21= RULE_ID )
                                    	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:245:3: otherlv_21= RULE_ID
                                    	    {

                                    	    			if (current==null) {
                                    	    	            current = createModelElement(grammarAccess.getMappingRule());
                                    	    	        }
                                    	            
                                    	    otherlv_21=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleMapping454); 

                                    	    		newLeafNode(otherlv_21, grammarAccess.getMappingAccess().getExternalTerminalsTerminalCrossReference_5_2_0_1_3_1_0()); 
                                    	    	

                                    	    }


                                    	    }


                                    	    }
                                    	    break;

                                    	default :
                                    	    break loop5;
                                        }
                                    } while (true);

                                    otherlv_22=(Token)match(input,19,FOLLOW_19_in_ruleMapping468); 

                                        	newLeafNode(otherlv_22, grammarAccess.getMappingAccess().getRightCurlyBracketKeyword_5_2_0_1_4());
                                        

                                    }
                                    break;

                            }


                            }


                            }
                            break;
                        case 2 :
                            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:261:6: ( ( (lv_terminals_23_0= ruleTerminal ) ) (otherlv_24= ',' ( (lv_terminals_25_0= ruleTerminal ) ) )* )
                            {
                            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:261:6: ( ( (lv_terminals_23_0= ruleTerminal ) ) (otherlv_24= ',' ( (lv_terminals_25_0= ruleTerminal ) ) )* )
                            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:261:7: ( (lv_terminals_23_0= ruleTerminal ) ) (otherlv_24= ',' ( (lv_terminals_25_0= ruleTerminal ) ) )*
                            {
                            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:261:7: ( (lv_terminals_23_0= ruleTerminal ) )
                            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:262:1: (lv_terminals_23_0= ruleTerminal )
                            {
                            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:262:1: (lv_terminals_23_0= ruleTerminal )
                            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:263:3: lv_terminals_23_0= ruleTerminal
                            {
                             
                            	        newCompositeNode(grammarAccess.getMappingAccess().getTerminalsTerminalParserRuleCall_5_2_1_0_0()); 
                            	    
                            pushFollow(FOLLOW_ruleTerminal_in_ruleMapping499);
                            lv_terminals_23_0=ruleTerminal();

                            state._fsp--;


                            	        if (current==null) {
                            	            current = createModelElementForParent(grammarAccess.getMappingRule());
                            	        }
                                   		add(
                                   			current, 
                                   			"terminals",
                                    		lv_terminals_23_0, 
                                    		"Terminal");
                            	        afterParserOrEnumRuleCall();
                            	    

                            }


                            }

                            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:279:2: (otherlv_24= ',' ( (lv_terminals_25_0= ruleTerminal ) ) )*
                            loop7:
                            do {
                                int alt7=2;
                                int LA7_0 = input.LA(1);

                                if ( (LA7_0==18) ) {
                                    alt7=1;
                                }


                                switch (alt7) {
                            	case 1 :
                            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:279:4: otherlv_24= ',' ( (lv_terminals_25_0= ruleTerminal ) )
                            	    {
                            	    otherlv_24=(Token)match(input,18,FOLLOW_18_in_ruleMapping512); 

                            	        	newLeafNode(otherlv_24, grammarAccess.getMappingAccess().getCommaKeyword_5_2_1_1_0());
                            	        
                            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:283:1: ( (lv_terminals_25_0= ruleTerminal ) )
                            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:284:1: (lv_terminals_25_0= ruleTerminal )
                            	    {
                            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:284:1: (lv_terminals_25_0= ruleTerminal )
                            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:285:3: lv_terminals_25_0= ruleTerminal
                            	    {
                            	     
                            	    	        newCompositeNode(grammarAccess.getMappingAccess().getTerminalsTerminalParserRuleCall_5_2_1_1_1_0()); 
                            	    	    
                            	    pushFollow(FOLLOW_ruleTerminal_in_ruleMapping533);
                            	    lv_terminals_25_0=ruleTerminal();

                            	    state._fsp--;


                            	    	        if (current==null) {
                            	    	            current = createModelElementForParent(grammarAccess.getMappingRule());
                            	    	        }
                            	           		add(
                            	           			current, 
                            	           			"terminals",
                            	            		lv_terminals_25_0, 
                            	            		"Terminal");
                            	    	        afterParserOrEnumRuleCall();
                            	    	    

                            	    }


                            	    }


                            	    }
                            	    break;

                            	default :
                            	    break loop7;
                                }
                            } while (true);


                            }


                            }
                            break;

                    }

                    otherlv_26=(Token)match(input,19,FOLLOW_19_in_ruleMapping549); 

                        	newLeafNode(otherlv_26, grammarAccess.getMappingAccess().getRightCurlyBracketKeyword_5_3());
                        

                    }
                    break;

            }

            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:305:3: ( (otherlv_27= 'operators' otherlv_28= '{' ( (lv_operators_29_0= ruleOperator ) ) (otherlv_30= ';' ( (lv_operators_31_0= ruleOperator ) ) )* otherlv_32= ';' otherlv_33= '}' ) | ( (lv_modules_34_0= ruleModule ) ) )*
            loop11:
            do {
                int alt11=3;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==21) ) {
                    alt11=1;
                }
                else if ( (LA11_0==22) ) {
                    alt11=2;
                }


                switch (alt11) {
            	case 1 :
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:305:4: (otherlv_27= 'operators' otherlv_28= '{' ( (lv_operators_29_0= ruleOperator ) ) (otherlv_30= ';' ( (lv_operators_31_0= ruleOperator ) ) )* otherlv_32= ';' otherlv_33= '}' )
            	    {
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:305:4: (otherlv_27= 'operators' otherlv_28= '{' ( (lv_operators_29_0= ruleOperator ) ) (otherlv_30= ';' ( (lv_operators_31_0= ruleOperator ) ) )* otherlv_32= ';' otherlv_33= '}' )
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:305:6: otherlv_27= 'operators' otherlv_28= '{' ( (lv_operators_29_0= ruleOperator ) ) (otherlv_30= ';' ( (lv_operators_31_0= ruleOperator ) ) )* otherlv_32= ';' otherlv_33= '}'
            	    {
            	    otherlv_27=(Token)match(input,21,FOLLOW_21_in_ruleMapping565); 

            	        	newLeafNode(otherlv_27, grammarAccess.getMappingAccess().getOperatorsKeyword_6_0_0());
            	        
            	    otherlv_28=(Token)match(input,16,FOLLOW_16_in_ruleMapping577); 

            	        	newLeafNode(otherlv_28, grammarAccess.getMappingAccess().getLeftCurlyBracketKeyword_6_0_1());
            	        
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:313:1: ( (lv_operators_29_0= ruleOperator ) )
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:314:1: (lv_operators_29_0= ruleOperator )
            	    {
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:314:1: (lv_operators_29_0= ruleOperator )
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:315:3: lv_operators_29_0= ruleOperator
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getMappingAccess().getOperatorsOperatorParserRuleCall_6_0_2_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleOperator_in_ruleMapping598);
            	    lv_operators_29_0=ruleOperator();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getMappingRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"operators",
            	            		lv_operators_29_0, 
            	            		"Operator");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }

            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:331:2: (otherlv_30= ';' ( (lv_operators_31_0= ruleOperator ) ) )*
            	    loop10:
            	    do {
            	        int alt10=2;
            	        int LA10_0 = input.LA(1);

            	        if ( (LA10_0==12) ) {
            	            int LA10_1 = input.LA(2);

            	            if ( (LA10_1==25||LA10_1==29) ) {
            	                alt10=1;
            	            }


            	        }


            	        switch (alt10) {
            	    	case 1 :
            	    	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:331:4: otherlv_30= ';' ( (lv_operators_31_0= ruleOperator ) )
            	    	    {
            	    	    otherlv_30=(Token)match(input,12,FOLLOW_12_in_ruleMapping611); 

            	    	        	newLeafNode(otherlv_30, grammarAccess.getMappingAccess().getSemicolonKeyword_6_0_3_0());
            	    	        
            	    	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:335:1: ( (lv_operators_31_0= ruleOperator ) )
            	    	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:336:1: (lv_operators_31_0= ruleOperator )
            	    	    {
            	    	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:336:1: (lv_operators_31_0= ruleOperator )
            	    	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:337:3: lv_operators_31_0= ruleOperator
            	    	    {
            	    	     
            	    	    	        newCompositeNode(grammarAccess.getMappingAccess().getOperatorsOperatorParserRuleCall_6_0_3_1_0()); 
            	    	    	    
            	    	    pushFollow(FOLLOW_ruleOperator_in_ruleMapping632);
            	    	    lv_operators_31_0=ruleOperator();

            	    	    state._fsp--;


            	    	    	        if (current==null) {
            	    	    	            current = createModelElementForParent(grammarAccess.getMappingRule());
            	    	    	        }
            	    	           		add(
            	    	           			current, 
            	    	           			"operators",
            	    	            		lv_operators_31_0, 
            	    	            		"Operator");
            	    	    	        afterParserOrEnumRuleCall();
            	    	    	    

            	    	    }


            	    	    }


            	    	    }
            	    	    break;

            	    	default :
            	    	    break loop10;
            	        }
            	    } while (true);

            	    otherlv_32=(Token)match(input,12,FOLLOW_12_in_ruleMapping646); 

            	        	newLeafNode(otherlv_32, grammarAccess.getMappingAccess().getSemicolonKeyword_6_0_4());
            	        
            	    otherlv_33=(Token)match(input,19,FOLLOW_19_in_ruleMapping658); 

            	        	newLeafNode(otherlv_33, grammarAccess.getMappingAccess().getRightCurlyBracketKeyword_6_0_5());
            	        

            	    }


            	    }
            	    break;
            	case 2 :
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:362:6: ( (lv_modules_34_0= ruleModule ) )
            	    {
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:362:6: ( (lv_modules_34_0= ruleModule ) )
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:363:1: (lv_modules_34_0= ruleModule )
            	    {
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:363:1: (lv_modules_34_0= ruleModule )
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:364:3: lv_modules_34_0= ruleModule
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getMappingAccess().getModulesModuleParserRuleCall_6_1_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleModule_in_ruleMapping686);
            	    lv_modules_34_0=ruleModule();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getMappingRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"modules",
            	            		lv_modules_34_0, 
            	            		"Module");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleMapping"


    // $ANTLR start "entryRuleModule"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:388:1: entryRuleModule returns [EObject current=null] : iv_ruleModule= ruleModule EOF ;
    public final EObject entryRuleModule() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModule = null;


        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:389:2: (iv_ruleModule= ruleModule EOF )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:390:2: iv_ruleModule= ruleModule EOF
            {
             newCompositeNode(grammarAccess.getModuleRule()); 
            pushFollow(FOLLOW_ruleModule_in_entryRuleModule724);
            iv_ruleModule=ruleModule();

            state._fsp--;

             current =iv_ruleModule; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleModule734); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleModule"


    // $ANTLR start "ruleModule"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:397:1: ruleModule returns [EObject current=null] : (otherlv_0= 'module' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' (otherlv_3= 'operators' otherlv_4= '{' ( (lv_operators_5_0= ruleOperator ) ) (otherlv_6= ';' ( (lv_operators_7_0= ruleOperator ) ) )* otherlv_8= ';' otherlv_9= '}' )? otherlv_10= '}' ) ;
    public final EObject ruleModule() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        Token otherlv_8=null;
        Token otherlv_9=null;
        Token otherlv_10=null;
        EObject lv_operators_5_0 = null;

        EObject lv_operators_7_0 = null;


         enterRule(); 
            
        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:400:28: ( (otherlv_0= 'module' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' (otherlv_3= 'operators' otherlv_4= '{' ( (lv_operators_5_0= ruleOperator ) ) (otherlv_6= ';' ( (lv_operators_7_0= ruleOperator ) ) )* otherlv_8= ';' otherlv_9= '}' )? otherlv_10= '}' ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:401:1: (otherlv_0= 'module' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' (otherlv_3= 'operators' otherlv_4= '{' ( (lv_operators_5_0= ruleOperator ) ) (otherlv_6= ';' ( (lv_operators_7_0= ruleOperator ) ) )* otherlv_8= ';' otherlv_9= '}' )? otherlv_10= '}' )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:401:1: (otherlv_0= 'module' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' (otherlv_3= 'operators' otherlv_4= '{' ( (lv_operators_5_0= ruleOperator ) ) (otherlv_6= ';' ( (lv_operators_7_0= ruleOperator ) ) )* otherlv_8= ';' otherlv_9= '}' )? otherlv_10= '}' )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:401:3: otherlv_0= 'module' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' (otherlv_3= 'operators' otherlv_4= '{' ( (lv_operators_5_0= ruleOperator ) ) (otherlv_6= ';' ( (lv_operators_7_0= ruleOperator ) ) )* otherlv_8= ';' otherlv_9= '}' )? otherlv_10= '}'
            {
            otherlv_0=(Token)match(input,22,FOLLOW_22_in_ruleModule771); 

                	newLeafNode(otherlv_0, grammarAccess.getModuleAccess().getModuleKeyword_0());
                
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:405:1: ( (lv_name_1_0= RULE_ID ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:406:1: (lv_name_1_0= RULE_ID )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:406:1: (lv_name_1_0= RULE_ID )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:407:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleModule788); 

            			newLeafNode(lv_name_1_0, grammarAccess.getModuleAccess().getNameIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getModuleRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_1_0, 
                    		"ID");
            	    

            }


            }

            otherlv_2=(Token)match(input,16,FOLLOW_16_in_ruleModule805); 

                	newLeafNode(otherlv_2, grammarAccess.getModuleAccess().getLeftCurlyBracketKeyword_2());
                
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:427:1: (otherlv_3= 'operators' otherlv_4= '{' ( (lv_operators_5_0= ruleOperator ) ) (otherlv_6= ';' ( (lv_operators_7_0= ruleOperator ) ) )* otherlv_8= ';' otherlv_9= '}' )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==21) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:427:3: otherlv_3= 'operators' otherlv_4= '{' ( (lv_operators_5_0= ruleOperator ) ) (otherlv_6= ';' ( (lv_operators_7_0= ruleOperator ) ) )* otherlv_8= ';' otherlv_9= '}'
                    {
                    otherlv_3=(Token)match(input,21,FOLLOW_21_in_ruleModule818); 

                        	newLeafNode(otherlv_3, grammarAccess.getModuleAccess().getOperatorsKeyword_3_0());
                        
                    otherlv_4=(Token)match(input,16,FOLLOW_16_in_ruleModule830); 

                        	newLeafNode(otherlv_4, grammarAccess.getModuleAccess().getLeftCurlyBracketKeyword_3_1());
                        
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:435:1: ( (lv_operators_5_0= ruleOperator ) )
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:436:1: (lv_operators_5_0= ruleOperator )
                    {
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:436:1: (lv_operators_5_0= ruleOperator )
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:437:3: lv_operators_5_0= ruleOperator
                    {
                     
                    	        newCompositeNode(grammarAccess.getModuleAccess().getOperatorsOperatorParserRuleCall_3_2_0()); 
                    	    
                    pushFollow(FOLLOW_ruleOperator_in_ruleModule851);
                    lv_operators_5_0=ruleOperator();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getModuleRule());
                    	        }
                           		add(
                           			current, 
                           			"operators",
                            		lv_operators_5_0, 
                            		"Operator");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }

                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:453:2: (otherlv_6= ';' ( (lv_operators_7_0= ruleOperator ) ) )*
                    loop12:
                    do {
                        int alt12=2;
                        int LA12_0 = input.LA(1);

                        if ( (LA12_0==12) ) {
                            int LA12_1 = input.LA(2);

                            if ( (LA12_1==25||LA12_1==29) ) {
                                alt12=1;
                            }


                        }


                        switch (alt12) {
                    	case 1 :
                    	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:453:4: otherlv_6= ';' ( (lv_operators_7_0= ruleOperator ) )
                    	    {
                    	    otherlv_6=(Token)match(input,12,FOLLOW_12_in_ruleModule864); 

                    	        	newLeafNode(otherlv_6, grammarAccess.getModuleAccess().getSemicolonKeyword_3_3_0());
                    	        
                    	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:457:1: ( (lv_operators_7_0= ruleOperator ) )
                    	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:458:1: (lv_operators_7_0= ruleOperator )
                    	    {
                    	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:458:1: (lv_operators_7_0= ruleOperator )
                    	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:459:3: lv_operators_7_0= ruleOperator
                    	    {
                    	     
                    	    	        newCompositeNode(grammarAccess.getModuleAccess().getOperatorsOperatorParserRuleCall_3_3_1_0()); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleOperator_in_ruleModule885);
                    	    lv_operators_7_0=ruleOperator();

                    	    state._fsp--;


                    	    	        if (current==null) {
                    	    	            current = createModelElementForParent(grammarAccess.getModuleRule());
                    	    	        }
                    	           		add(
                    	           			current, 
                    	           			"operators",
                    	            		lv_operators_7_0, 
                    	            		"Operator");
                    	    	        afterParserOrEnumRuleCall();
                    	    	    

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop12;
                        }
                    } while (true);

                    otherlv_8=(Token)match(input,12,FOLLOW_12_in_ruleModule899); 

                        	newLeafNode(otherlv_8, grammarAccess.getModuleAccess().getSemicolonKeyword_3_4());
                        
                    otherlv_9=(Token)match(input,19,FOLLOW_19_in_ruleModule911); 

                        	newLeafNode(otherlv_9, grammarAccess.getModuleAccess().getRightCurlyBracketKeyword_3_5());
                        

                    }
                    break;

            }

            otherlv_10=(Token)match(input,19,FOLLOW_19_in_ruleModule925); 

                	newLeafNode(otherlv_10, grammarAccess.getModuleAccess().getRightCurlyBracketKeyword_4());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleModule"


    // $ANTLR start "entryRuleOperator"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:495:1: entryRuleOperator returns [EObject current=null] : iv_ruleOperator= ruleOperator EOF ;
    public final EObject entryRuleOperator() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOperator = null;


        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:496:2: (iv_ruleOperator= ruleOperator EOF )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:497:2: iv_ruleOperator= ruleOperator EOF
            {
             newCompositeNode(grammarAccess.getOperatorRule()); 
            pushFollow(FOLLOW_ruleOperator_in_entryRuleOperator961);
            iv_ruleOperator=ruleOperator();

            state._fsp--;

             current =iv_ruleOperator; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleOperator971); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleOperator"


    // $ANTLR start "ruleOperator"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:504:1: ruleOperator returns [EObject current=null] : (this_ClassOperator_0= ruleClassOperator | this_ClassOperatorWithExceptions_1= ruleClassOperatorWithExceptions | this_UserOperator_2= ruleUserOperator | this_AliasOperator_3= ruleAliasOperator ) ;
    public final EObject ruleOperator() throws RecognitionException {
        EObject current = null;

        EObject this_ClassOperator_0 = null;

        EObject this_ClassOperatorWithExceptions_1 = null;

        EObject this_UserOperator_2 = null;

        EObject this_AliasOperator_3 = null;


         enterRule(); 
            
        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:507:28: ( (this_ClassOperator_0= ruleClassOperator | this_ClassOperatorWithExceptions_1= ruleClassOperatorWithExceptions | this_UserOperator_2= ruleUserOperator | this_AliasOperator_3= ruleAliasOperator ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:508:1: (this_ClassOperator_0= ruleClassOperator | this_ClassOperatorWithExceptions_1= ruleClassOperatorWithExceptions | this_UserOperator_2= ruleUserOperator | this_AliasOperator_3= ruleAliasOperator )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:508:1: (this_ClassOperator_0= ruleClassOperator | this_ClassOperatorWithExceptions_1= ruleClassOperatorWithExceptions | this_UserOperator_2= ruleUserOperator | this_AliasOperator_3= ruleAliasOperator )
            int alt14=4;
            alt14 = dfa14.predict(input);
            switch (alt14) {
                case 1 :
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:509:5: this_ClassOperator_0= ruleClassOperator
                    {
                     
                            newCompositeNode(grammarAccess.getOperatorAccess().getClassOperatorParserRuleCall_0()); 
                        
                    pushFollow(FOLLOW_ruleClassOperator_in_ruleOperator1018);
                    this_ClassOperator_0=ruleClassOperator();

                    state._fsp--;

                     
                            current = this_ClassOperator_0; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 2 :
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:519:5: this_ClassOperatorWithExceptions_1= ruleClassOperatorWithExceptions
                    {
                     
                            newCompositeNode(grammarAccess.getOperatorAccess().getClassOperatorWithExceptionsParserRuleCall_1()); 
                        
                    pushFollow(FOLLOW_ruleClassOperatorWithExceptions_in_ruleOperator1045);
                    this_ClassOperatorWithExceptions_1=ruleClassOperatorWithExceptions();

                    state._fsp--;

                     
                            current = this_ClassOperatorWithExceptions_1; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 3 :
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:529:5: this_UserOperator_2= ruleUserOperator
                    {
                     
                            newCompositeNode(grammarAccess.getOperatorAccess().getUserOperatorParserRuleCall_2()); 
                        
                    pushFollow(FOLLOW_ruleUserOperator_in_ruleOperator1072);
                    this_UserOperator_2=ruleUserOperator();

                    state._fsp--;

                     
                            current = this_UserOperator_2; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 4 :
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:539:5: this_AliasOperator_3= ruleAliasOperator
                    {
                     
                            newCompositeNode(grammarAccess.getOperatorAccess().getAliasOperatorParserRuleCall_3()); 
                        
                    pushFollow(FOLLOW_ruleAliasOperator_in_ruleOperator1099);
                    this_AliasOperator_3=ruleAliasOperator();

                    state._fsp--;

                     
                            current = this_AliasOperator_3; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleOperator"


    // $ANTLR start "entryRuleImport"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:555:1: entryRuleImport returns [EObject current=null] : iv_ruleImport= ruleImport EOF ;
    public final EObject entryRuleImport() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleImport = null;


        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:556:2: (iv_ruleImport= ruleImport EOF )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:557:2: iv_ruleImport= ruleImport EOF
            {
             newCompositeNode(grammarAccess.getImportRule()); 
            pushFollow(FOLLOW_ruleImport_in_entryRuleImport1134);
            iv_ruleImport=ruleImport();

            state._fsp--;

             current =iv_ruleImport; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleImport1144); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleImport"


    // $ANTLR start "ruleImport"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:564:1: ruleImport returns [EObject current=null] : ( (lv_importURI_0_0= RULE_STRING ) ) ;
    public final EObject ruleImport() throws RecognitionException {
        EObject current = null;

        Token lv_importURI_0_0=null;

         enterRule(); 
            
        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:567:28: ( ( (lv_importURI_0_0= RULE_STRING ) ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:568:1: ( (lv_importURI_0_0= RULE_STRING ) )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:568:1: ( (lv_importURI_0_0= RULE_STRING ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:569:1: (lv_importURI_0_0= RULE_STRING )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:569:1: (lv_importURI_0_0= RULE_STRING )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:570:3: lv_importURI_0_0= RULE_STRING
            {
            lv_importURI_0_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleImport1185); 

            			newLeafNode(lv_importURI_0_0, grammarAccess.getImportAccess().getImportURISTRINGTerminalRuleCall_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getImportRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"importURI",
                    		lv_importURI_0_0, 
                    		"STRING");
            	    

            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleImport"


    // $ANTLR start "entryRuleTerminal"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:594:1: entryRuleTerminal returns [EObject current=null] : iv_ruleTerminal= ruleTerminal EOF ;
    public final EObject entryRuleTerminal() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTerminal = null;


        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:595:2: (iv_ruleTerminal= ruleTerminal EOF )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:596:2: iv_ruleTerminal= ruleTerminal EOF
            {
             newCompositeNode(grammarAccess.getTerminalRule()); 
            pushFollow(FOLLOW_ruleTerminal_in_entryRuleTerminal1225);
            iv_ruleTerminal=ruleTerminal();

            state._fsp--;

             current =iv_ruleTerminal; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleTerminal1235); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleTerminal"


    // $ANTLR start "ruleTerminal"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:603:1: ruleTerminal returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' ( ( ruleFQN ) ) ( (lv_many_3_0= '[]' ) )? ) ;
    public final EObject ruleTerminal() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token lv_many_3_0=null;

         enterRule(); 
            
        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:606:28: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' ( ( ruleFQN ) ) ( (lv_many_3_0= '[]' ) )? ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:607:1: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' ( ( ruleFQN ) ) ( (lv_many_3_0= '[]' ) )? )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:607:1: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' ( ( ruleFQN ) ) ( (lv_many_3_0= '[]' ) )? )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:607:2: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' ( ( ruleFQN ) ) ( (lv_many_3_0= '[]' ) )?
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:607:2: ( (lv_name_0_0= RULE_ID ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:608:1: (lv_name_0_0= RULE_ID )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:608:1: (lv_name_0_0= RULE_ID )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:609:3: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleTerminal1277); 

            			newLeafNode(lv_name_0_0, grammarAccess.getTerminalAccess().getNameIDTerminalRuleCall_0_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getTerminalRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_0_0, 
                    		"ID");
            	    

            }


            }

            otherlv_1=(Token)match(input,23,FOLLOW_23_in_ruleTerminal1294); 

                	newLeafNode(otherlv_1, grammarAccess.getTerminalAccess().getColonKeyword_1());
                
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:629:1: ( ( ruleFQN ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:630:1: ( ruleFQN )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:630:1: ( ruleFQN )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:631:3: ruleFQN
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getTerminalRule());
            	        }
                    
             
            	        newCompositeNode(grammarAccess.getTerminalAccess().getClassEClassCrossReference_2_0()); 
            	    
            pushFollow(FOLLOW_ruleFQN_in_ruleTerminal1317);
            ruleFQN();

            state._fsp--;

             
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:644:2: ( (lv_many_3_0= '[]' ) )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==24) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:645:1: (lv_many_3_0= '[]' )
                    {
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:645:1: (lv_many_3_0= '[]' )
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:646:3: lv_many_3_0= '[]'
                    {
                    lv_many_3_0=(Token)match(input,24,FOLLOW_24_in_ruleTerminal1335); 

                            newLeafNode(lv_many_3_0, grammarAccess.getTerminalAccess().getManyLeftSquareBracketRightSquareBracketKeyword_3_0());
                        

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getTerminalRule());
                    	        }
                           		setWithLastConsumed(current, "many", true, "[]");
                    	    

                    }


                    }
                    break;

            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleTerminal"


    // $ANTLR start "entryRuleAliasOperator"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:667:1: entryRuleAliasOperator returns [EObject current=null] : iv_ruleAliasOperator= ruleAliasOperator EOF ;
    public final EObject entryRuleAliasOperator() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAliasOperator = null;


        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:668:2: (iv_ruleAliasOperator= ruleAliasOperator EOF )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:669:2: iv_ruleAliasOperator= ruleAliasOperator EOF
            {
             newCompositeNode(grammarAccess.getAliasOperatorRule()); 
            pushFollow(FOLLOW_ruleAliasOperator_in_entryRuleAliasOperator1385);
            iv_ruleAliasOperator=ruleAliasOperator();

            state._fsp--;

             current =iv_ruleAliasOperator; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleAliasOperator1395); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAliasOperator"


    // $ANTLR start "ruleAliasOperator"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:676:1: ruleAliasOperator returns [EObject current=null] : (otherlv_0= 'alias' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '::' ( (otherlv_3= RULE_ID ) ) otherlv_4= '(' ( (lv_nodes_5_0= ruleAliasNode ) ) (otherlv_6= ',' ( (lv_nodes_7_0= ruleAliasNode ) ) )* otherlv_8= ')' ) ;
    public final EObject ruleAliasOperator() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        Token otherlv_8=null;
        EObject lv_nodes_5_0 = null;

        EObject lv_nodes_7_0 = null;


         enterRule(); 
            
        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:679:28: ( (otherlv_0= 'alias' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '::' ( (otherlv_3= RULE_ID ) ) otherlv_4= '(' ( (lv_nodes_5_0= ruleAliasNode ) ) (otherlv_6= ',' ( (lv_nodes_7_0= ruleAliasNode ) ) )* otherlv_8= ')' ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:680:1: (otherlv_0= 'alias' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '::' ( (otherlv_3= RULE_ID ) ) otherlv_4= '(' ( (lv_nodes_5_0= ruleAliasNode ) ) (otherlv_6= ',' ( (lv_nodes_7_0= ruleAliasNode ) ) )* otherlv_8= ')' )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:680:1: (otherlv_0= 'alias' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '::' ( (otherlv_3= RULE_ID ) ) otherlv_4= '(' ( (lv_nodes_5_0= ruleAliasNode ) ) (otherlv_6= ',' ( (lv_nodes_7_0= ruleAliasNode ) ) )* otherlv_8= ')' )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:680:3: otherlv_0= 'alias' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '::' ( (otherlv_3= RULE_ID ) ) otherlv_4= '(' ( (lv_nodes_5_0= ruleAliasNode ) ) (otherlv_6= ',' ( (lv_nodes_7_0= ruleAliasNode ) ) )* otherlv_8= ')'
            {
            otherlv_0=(Token)match(input,25,FOLLOW_25_in_ruleAliasOperator1432); 

                	newLeafNode(otherlv_0, grammarAccess.getAliasOperatorAccess().getAliasKeyword_0());
                
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:684:1: ( (lv_name_1_0= RULE_ID ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:685:1: (lv_name_1_0= RULE_ID )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:685:1: (lv_name_1_0= RULE_ID )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:686:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleAliasOperator1449); 

            			newLeafNode(lv_name_1_0, grammarAccess.getAliasOperatorAccess().getNameIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getAliasOperatorRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_1_0, 
                    		"ID");
            	    

            }


            }

            otherlv_2=(Token)match(input,26,FOLLOW_26_in_ruleAliasOperator1466); 

                	newLeafNode(otherlv_2, grammarAccess.getAliasOperatorAccess().getColonColonKeyword_2());
                
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:706:1: ( (otherlv_3= RULE_ID ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:707:1: (otherlv_3= RULE_ID )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:707:1: (otherlv_3= RULE_ID )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:708:3: otherlv_3= RULE_ID
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getAliasOperatorRule());
            	        }
                    
            otherlv_3=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleAliasOperator1486); 

            		newLeafNode(otherlv_3, grammarAccess.getAliasOperatorAccess().getOpOperatorCrossReference_3_0()); 
            	

            }


            }

            otherlv_4=(Token)match(input,27,FOLLOW_27_in_ruleAliasOperator1498); 

                	newLeafNode(otherlv_4, grammarAccess.getAliasOperatorAccess().getLeftParenthesisKeyword_4());
                
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:723:1: ( (lv_nodes_5_0= ruleAliasNode ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:724:1: (lv_nodes_5_0= ruleAliasNode )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:724:1: (lv_nodes_5_0= ruleAliasNode )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:725:3: lv_nodes_5_0= ruleAliasNode
            {
             
            	        newCompositeNode(grammarAccess.getAliasOperatorAccess().getNodesAliasNodeParserRuleCall_5_0()); 
            	    
            pushFollow(FOLLOW_ruleAliasNode_in_ruleAliasOperator1519);
            lv_nodes_5_0=ruleAliasNode();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getAliasOperatorRule());
            	        }
                   		add(
                   			current, 
                   			"nodes",
                    		lv_nodes_5_0, 
                    		"AliasNode");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:741:2: (otherlv_6= ',' ( (lv_nodes_7_0= ruleAliasNode ) ) )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0==18) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:741:4: otherlv_6= ',' ( (lv_nodes_7_0= ruleAliasNode ) )
            	    {
            	    otherlv_6=(Token)match(input,18,FOLLOW_18_in_ruleAliasOperator1532); 

            	        	newLeafNode(otherlv_6, grammarAccess.getAliasOperatorAccess().getCommaKeyword_6_0());
            	        
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:745:1: ( (lv_nodes_7_0= ruleAliasNode ) )
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:746:1: (lv_nodes_7_0= ruleAliasNode )
            	    {
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:746:1: (lv_nodes_7_0= ruleAliasNode )
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:747:3: lv_nodes_7_0= ruleAliasNode
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getAliasOperatorAccess().getNodesAliasNodeParserRuleCall_6_1_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleAliasNode_in_ruleAliasOperator1553);
            	    lv_nodes_7_0=ruleAliasNode();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getAliasOperatorRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"nodes",
            	            		lv_nodes_7_0, 
            	            		"AliasNode");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop16;
                }
            } while (true);

            otherlv_8=(Token)match(input,28,FOLLOW_28_in_ruleAliasOperator1567); 

                	newLeafNode(otherlv_8, grammarAccess.getAliasOperatorAccess().getRightParenthesisKeyword_7());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAliasOperator"


    // $ANTLR start "entryRuleAliasNode"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:775:1: entryRuleAliasNode returns [EObject current=null] : iv_ruleAliasNode= ruleAliasNode EOF ;
    public final EObject entryRuleAliasNode() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAliasNode = null;


        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:776:2: (iv_ruleAliasNode= ruleAliasNode EOF )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:777:2: iv_ruleAliasNode= ruleAliasNode EOF
            {
             newCompositeNode(grammarAccess.getAliasNodeRule()); 
            pushFollow(FOLLOW_ruleAliasNode_in_entryRuleAliasNode1603);
            iv_ruleAliasNode=ruleAliasNode();

            state._fsp--;

             current =iv_ruleAliasNode; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleAliasNode1613); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAliasNode"


    // $ANTLR start "ruleAliasNode"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:784:1: ruleAliasNode returns [EObject current=null] : (this_FeatureNode_0= ruleFeatureNode | this_OperatorNode_1= ruleOperatorNode ) ;
    public final EObject ruleAliasNode() throws RecognitionException {
        EObject current = null;

        EObject this_FeatureNode_0 = null;

        EObject this_OperatorNode_1 = null;


         enterRule(); 
            
        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:787:28: ( (this_FeatureNode_0= ruleFeatureNode | this_OperatorNode_1= ruleOperatorNode ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:788:1: (this_FeatureNode_0= ruleFeatureNode | this_OperatorNode_1= ruleOperatorNode )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:788:1: (this_FeatureNode_0= ruleFeatureNode | this_OperatorNode_1= ruleOperatorNode )
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==RULE_ID) ) {
                int LA17_1 = input.LA(2);

                if ( (LA17_1==EOF||LA17_1==18||LA17_1==28) ) {
                    alt17=1;
                }
                else if ( (LA17_1==27) ) {
                    alt17=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 17, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;
            }
            switch (alt17) {
                case 1 :
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:789:5: this_FeatureNode_0= ruleFeatureNode
                    {
                     
                            newCompositeNode(grammarAccess.getAliasNodeAccess().getFeatureNodeParserRuleCall_0()); 
                        
                    pushFollow(FOLLOW_ruleFeatureNode_in_ruleAliasNode1660);
                    this_FeatureNode_0=ruleFeatureNode();

                    state._fsp--;

                     
                            current = this_FeatureNode_0; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 2 :
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:799:5: this_OperatorNode_1= ruleOperatorNode
                    {
                     
                            newCompositeNode(grammarAccess.getAliasNodeAccess().getOperatorNodeParserRuleCall_1()); 
                        
                    pushFollow(FOLLOW_ruleOperatorNode_in_ruleAliasNode1687);
                    this_OperatorNode_1=ruleOperatorNode();

                    state._fsp--;

                     
                            current = this_OperatorNode_1; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAliasNode"


    // $ANTLR start "entryRuleFeatureNode"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:815:1: entryRuleFeatureNode returns [EObject current=null] : iv_ruleFeatureNode= ruleFeatureNode EOF ;
    public final EObject entryRuleFeatureNode() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFeatureNode = null;


        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:816:2: (iv_ruleFeatureNode= ruleFeatureNode EOF )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:817:2: iv_ruleFeatureNode= ruleFeatureNode EOF
            {
             newCompositeNode(grammarAccess.getFeatureNodeRule()); 
            pushFollow(FOLLOW_ruleFeatureNode_in_entryRuleFeatureNode1722);
            iv_ruleFeatureNode=ruleFeatureNode();

            state._fsp--;

             current =iv_ruleFeatureNode; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleFeatureNode1732); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleFeatureNode"


    // $ANTLR start "ruleFeatureNode"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:824:1: ruleFeatureNode returns [EObject current=null] : ( (lv_feature_0_0= RULE_ID ) ) ;
    public final EObject ruleFeatureNode() throws RecognitionException {
        EObject current = null;

        Token lv_feature_0_0=null;

         enterRule(); 
            
        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:827:28: ( ( (lv_feature_0_0= RULE_ID ) ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:828:1: ( (lv_feature_0_0= RULE_ID ) )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:828:1: ( (lv_feature_0_0= RULE_ID ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:829:1: (lv_feature_0_0= RULE_ID )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:829:1: (lv_feature_0_0= RULE_ID )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:830:3: lv_feature_0_0= RULE_ID
            {
            lv_feature_0_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleFeatureNode1773); 

            			newLeafNode(lv_feature_0_0, grammarAccess.getFeatureNodeAccess().getFeatureIDTerminalRuleCall_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getFeatureNodeRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"feature",
                    		lv_feature_0_0, 
                    		"ID");
            	    

            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleFeatureNode"


    // $ANTLR start "entryRuleOperatorNode"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:854:1: entryRuleOperatorNode returns [EObject current=null] : iv_ruleOperatorNode= ruleOperatorNode EOF ;
    public final EObject entryRuleOperatorNode() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOperatorNode = null;


        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:855:2: (iv_ruleOperatorNode= ruleOperatorNode EOF )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:856:2: iv_ruleOperatorNode= ruleOperatorNode EOF
            {
             newCompositeNode(grammarAccess.getOperatorNodeRule()); 
            pushFollow(FOLLOW_ruleOperatorNode_in_entryRuleOperatorNode1813);
            iv_ruleOperatorNode=ruleOperatorNode();

            state._fsp--;

             current =iv_ruleOperatorNode; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleOperatorNode1823); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleOperatorNode"


    // $ANTLR start "ruleOperatorNode"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:863:1: ruleOperatorNode returns [EObject current=null] : ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '(' ( (lv_nodes_2_0= ruleAliasNode ) ) (otherlv_3= ',' ( (lv_nodes_4_0= ruleAliasNode ) ) )* otherlv_5= ')' ) ;
    public final EObject ruleOperatorNode() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_nodes_2_0 = null;

        EObject lv_nodes_4_0 = null;


         enterRule(); 
            
        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:866:28: ( ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '(' ( (lv_nodes_2_0= ruleAliasNode ) ) (otherlv_3= ',' ( (lv_nodes_4_0= ruleAliasNode ) ) )* otherlv_5= ')' ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:867:1: ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '(' ( (lv_nodes_2_0= ruleAliasNode ) ) (otherlv_3= ',' ( (lv_nodes_4_0= ruleAliasNode ) ) )* otherlv_5= ')' )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:867:1: ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '(' ( (lv_nodes_2_0= ruleAliasNode ) ) (otherlv_3= ',' ( (lv_nodes_4_0= ruleAliasNode ) ) )* otherlv_5= ')' )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:867:2: ( (otherlv_0= RULE_ID ) ) otherlv_1= '(' ( (lv_nodes_2_0= ruleAliasNode ) ) (otherlv_3= ',' ( (lv_nodes_4_0= ruleAliasNode ) ) )* otherlv_5= ')'
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:867:2: ( (otherlv_0= RULE_ID ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:868:1: (otherlv_0= RULE_ID )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:868:1: (otherlv_0= RULE_ID )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:869:3: otherlv_0= RULE_ID
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getOperatorNodeRule());
            	        }
                    
            otherlv_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleOperatorNode1868); 

            		newLeafNode(otherlv_0, grammarAccess.getOperatorNodeAccess().getOpOperatorCrossReference_0_0()); 
            	

            }


            }

            otherlv_1=(Token)match(input,27,FOLLOW_27_in_ruleOperatorNode1880); 

                	newLeafNode(otherlv_1, grammarAccess.getOperatorNodeAccess().getLeftParenthesisKeyword_1());
                
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:884:1: ( (lv_nodes_2_0= ruleAliasNode ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:885:1: (lv_nodes_2_0= ruleAliasNode )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:885:1: (lv_nodes_2_0= ruleAliasNode )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:886:3: lv_nodes_2_0= ruleAliasNode
            {
             
            	        newCompositeNode(grammarAccess.getOperatorNodeAccess().getNodesAliasNodeParserRuleCall_2_0()); 
            	    
            pushFollow(FOLLOW_ruleAliasNode_in_ruleOperatorNode1901);
            lv_nodes_2_0=ruleAliasNode();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getOperatorNodeRule());
            	        }
                   		add(
                   			current, 
                   			"nodes",
                    		lv_nodes_2_0, 
                    		"AliasNode");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:902:2: (otherlv_3= ',' ( (lv_nodes_4_0= ruleAliasNode ) ) )*
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( (LA18_0==18) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:902:4: otherlv_3= ',' ( (lv_nodes_4_0= ruleAliasNode ) )
            	    {
            	    otherlv_3=(Token)match(input,18,FOLLOW_18_in_ruleOperatorNode1914); 

            	        	newLeafNode(otherlv_3, grammarAccess.getOperatorNodeAccess().getCommaKeyword_3_0());
            	        
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:906:1: ( (lv_nodes_4_0= ruleAliasNode ) )
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:907:1: (lv_nodes_4_0= ruleAliasNode )
            	    {
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:907:1: (lv_nodes_4_0= ruleAliasNode )
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:908:3: lv_nodes_4_0= ruleAliasNode
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getOperatorNodeAccess().getNodesAliasNodeParserRuleCall_3_1_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleAliasNode_in_ruleOperatorNode1935);
            	    lv_nodes_4_0=ruleAliasNode();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getOperatorNodeRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"nodes",
            	            		lv_nodes_4_0, 
            	            		"AliasNode");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop18;
                }
            } while (true);

            otherlv_5=(Token)match(input,28,FOLLOW_28_in_ruleOperatorNode1949); 

                	newLeafNode(otherlv_5, grammarAccess.getOperatorNodeAccess().getRightParenthesisKeyword_4());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleOperatorNode"


    // $ANTLR start "entryRuleClassOperator"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:936:1: entryRuleClassOperator returns [EObject current=null] : iv_ruleClassOperator= ruleClassOperator EOF ;
    public final EObject entryRuleClassOperator() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleClassOperator = null;


        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:937:2: (iv_ruleClassOperator= ruleClassOperator EOF )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:938:2: iv_ruleClassOperator= ruleClassOperator EOF
            {
             newCompositeNode(grammarAccess.getClassOperatorRule()); 
            pushFollow(FOLLOW_ruleClassOperator_in_entryRuleClassOperator1985);
            iv_ruleClassOperator=ruleClassOperator();

            state._fsp--;

             current =iv_ruleClassOperator; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleClassOperator1995); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleClassOperator"


    // $ANTLR start "ruleClassOperator"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:945:1: ruleClassOperator returns [EObject current=null] : (otherlv_0= 'op' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '::' ( ( ruleFQN ) ) ) ;
    public final EObject ruleClassOperator() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;

         enterRule(); 
            
        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:948:28: ( (otherlv_0= 'op' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '::' ( ( ruleFQN ) ) ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:949:1: (otherlv_0= 'op' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '::' ( ( ruleFQN ) ) )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:949:1: (otherlv_0= 'op' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '::' ( ( ruleFQN ) ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:949:3: otherlv_0= 'op' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '::' ( ( ruleFQN ) )
            {
            otherlv_0=(Token)match(input,29,FOLLOW_29_in_ruleClassOperator2032); 

                	newLeafNode(otherlv_0, grammarAccess.getClassOperatorAccess().getOpKeyword_0());
                
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:953:1: ( (lv_name_1_0= RULE_ID ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:954:1: (lv_name_1_0= RULE_ID )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:954:1: (lv_name_1_0= RULE_ID )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:955:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleClassOperator2049); 

            			newLeafNode(lv_name_1_0, grammarAccess.getClassOperatorAccess().getNameIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getClassOperatorRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_1_0, 
                    		"ID");
            	    

            }


            }

            otherlv_2=(Token)match(input,26,FOLLOW_26_in_ruleClassOperator2066); 

                	newLeafNode(otherlv_2, grammarAccess.getClassOperatorAccess().getColonColonKeyword_2());
                
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:975:1: ( ( ruleFQN ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:976:1: ( ruleFQN )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:976:1: ( ruleFQN )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:977:3: ruleFQN
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getClassOperatorRule());
            	        }
                    
             
            	        newCompositeNode(grammarAccess.getClassOperatorAccess().getClassEClassCrossReference_3_0()); 
            	    
            pushFollow(FOLLOW_ruleFQN_in_ruleClassOperator2089);
            ruleFQN();

            state._fsp--;

             
            	        afterParserOrEnumRuleCall();
            	    

            }


            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleClassOperator"


    // $ANTLR start "entryRuleClassOperatorWithExceptions"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:998:1: entryRuleClassOperatorWithExceptions returns [EObject current=null] : iv_ruleClassOperatorWithExceptions= ruleClassOperatorWithExceptions EOF ;
    public final EObject entryRuleClassOperatorWithExceptions() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleClassOperatorWithExceptions = null;


        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:999:2: (iv_ruleClassOperatorWithExceptions= ruleClassOperatorWithExceptions EOF )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1000:2: iv_ruleClassOperatorWithExceptions= ruleClassOperatorWithExceptions EOF
            {
             newCompositeNode(grammarAccess.getClassOperatorWithExceptionsRule()); 
            pushFollow(FOLLOW_ruleClassOperatorWithExceptions_in_entryRuleClassOperatorWithExceptions2125);
            iv_ruleClassOperatorWithExceptions=ruleClassOperatorWithExceptions();

            state._fsp--;

             current =iv_ruleClassOperatorWithExceptions; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleClassOperatorWithExceptions2135); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleClassOperatorWithExceptions"


    // $ANTLR start "ruleClassOperatorWithExceptions"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1007:1: ruleClassOperatorWithExceptions returns [EObject current=null] : (otherlv_0= 'op' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '::' ( ( ruleFQN ) ) otherlv_4= '(' ( (lv_parameters_5_0= ruleFeatureParameter ) ) (otherlv_6= ',' ( (lv_parameters_7_0= ruleFeatureParameter ) ) )* otherlv_8= ')' ) ;
    public final EObject ruleClassOperatorWithExceptions() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        Token otherlv_8=null;
        EObject lv_parameters_5_0 = null;

        EObject lv_parameters_7_0 = null;


         enterRule(); 
            
        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1010:28: ( (otherlv_0= 'op' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '::' ( ( ruleFQN ) ) otherlv_4= '(' ( (lv_parameters_5_0= ruleFeatureParameter ) ) (otherlv_6= ',' ( (lv_parameters_7_0= ruleFeatureParameter ) ) )* otherlv_8= ')' ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1011:1: (otherlv_0= 'op' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '::' ( ( ruleFQN ) ) otherlv_4= '(' ( (lv_parameters_5_0= ruleFeatureParameter ) ) (otherlv_6= ',' ( (lv_parameters_7_0= ruleFeatureParameter ) ) )* otherlv_8= ')' )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1011:1: (otherlv_0= 'op' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '::' ( ( ruleFQN ) ) otherlv_4= '(' ( (lv_parameters_5_0= ruleFeatureParameter ) ) (otherlv_6= ',' ( (lv_parameters_7_0= ruleFeatureParameter ) ) )* otherlv_8= ')' )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1011:3: otherlv_0= 'op' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '::' ( ( ruleFQN ) ) otherlv_4= '(' ( (lv_parameters_5_0= ruleFeatureParameter ) ) (otherlv_6= ',' ( (lv_parameters_7_0= ruleFeatureParameter ) ) )* otherlv_8= ')'
            {
            otherlv_0=(Token)match(input,29,FOLLOW_29_in_ruleClassOperatorWithExceptions2172); 

                	newLeafNode(otherlv_0, grammarAccess.getClassOperatorWithExceptionsAccess().getOpKeyword_0());
                
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1015:1: ( (lv_name_1_0= RULE_ID ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1016:1: (lv_name_1_0= RULE_ID )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1016:1: (lv_name_1_0= RULE_ID )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1017:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleClassOperatorWithExceptions2189); 

            			newLeafNode(lv_name_1_0, grammarAccess.getClassOperatorWithExceptionsAccess().getNameIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getClassOperatorWithExceptionsRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_1_0, 
                    		"ID");
            	    

            }


            }

            otherlv_2=(Token)match(input,26,FOLLOW_26_in_ruleClassOperatorWithExceptions2206); 

                	newLeafNode(otherlv_2, grammarAccess.getClassOperatorWithExceptionsAccess().getColonColonKeyword_2());
                
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1037:1: ( ( ruleFQN ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1038:1: ( ruleFQN )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1038:1: ( ruleFQN )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1039:3: ruleFQN
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getClassOperatorWithExceptionsRule());
            	        }
                    
             
            	        newCompositeNode(grammarAccess.getClassOperatorWithExceptionsAccess().getClassEClassCrossReference_3_0()); 
            	    
            pushFollow(FOLLOW_ruleFQN_in_ruleClassOperatorWithExceptions2229);
            ruleFQN();

            state._fsp--;

             
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            otherlv_4=(Token)match(input,27,FOLLOW_27_in_ruleClassOperatorWithExceptions2241); 

                	newLeafNode(otherlv_4, grammarAccess.getClassOperatorWithExceptionsAccess().getLeftParenthesisKeyword_4());
                
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1056:1: ( (lv_parameters_5_0= ruleFeatureParameter ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1057:1: (lv_parameters_5_0= ruleFeatureParameter )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1057:1: (lv_parameters_5_0= ruleFeatureParameter )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1058:3: lv_parameters_5_0= ruleFeatureParameter
            {
             
            	        newCompositeNode(grammarAccess.getClassOperatorWithExceptionsAccess().getParametersFeatureParameterParserRuleCall_5_0()); 
            	    
            pushFollow(FOLLOW_ruleFeatureParameter_in_ruleClassOperatorWithExceptions2262);
            lv_parameters_5_0=ruleFeatureParameter();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getClassOperatorWithExceptionsRule());
            	        }
                   		add(
                   			current, 
                   			"parameters",
                    		lv_parameters_5_0, 
                    		"FeatureParameter");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1074:2: (otherlv_6= ',' ( (lv_parameters_7_0= ruleFeatureParameter ) ) )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( (LA19_0==18) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1074:4: otherlv_6= ',' ( (lv_parameters_7_0= ruleFeatureParameter ) )
            	    {
            	    otherlv_6=(Token)match(input,18,FOLLOW_18_in_ruleClassOperatorWithExceptions2275); 

            	        	newLeafNode(otherlv_6, grammarAccess.getClassOperatorWithExceptionsAccess().getCommaKeyword_6_0());
            	        
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1078:1: ( (lv_parameters_7_0= ruleFeatureParameter ) )
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1079:1: (lv_parameters_7_0= ruleFeatureParameter )
            	    {
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1079:1: (lv_parameters_7_0= ruleFeatureParameter )
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1080:3: lv_parameters_7_0= ruleFeatureParameter
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getClassOperatorWithExceptionsAccess().getParametersFeatureParameterParserRuleCall_6_1_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleFeatureParameter_in_ruleClassOperatorWithExceptions2296);
            	    lv_parameters_7_0=ruleFeatureParameter();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getClassOperatorWithExceptionsRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"parameters",
            	            		lv_parameters_7_0, 
            	            		"FeatureParameter");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop19;
                }
            } while (true);

            otherlv_8=(Token)match(input,28,FOLLOW_28_in_ruleClassOperatorWithExceptions2310); 

                	newLeafNode(otherlv_8, grammarAccess.getClassOperatorWithExceptionsAccess().getRightParenthesisKeyword_7());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleClassOperatorWithExceptions"


    // $ANTLR start "entryRuleUserOperator"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1108:1: entryRuleUserOperator returns [EObject current=null] : iv_ruleUserOperator= ruleUserOperator EOF ;
    public final EObject entryRuleUserOperator() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleUserOperator = null;


        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1109:2: (iv_ruleUserOperator= ruleUserOperator EOF )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1110:2: iv_ruleUserOperator= ruleUserOperator EOF
            {
             newCompositeNode(grammarAccess.getUserOperatorRule()); 
            pushFollow(FOLLOW_ruleUserOperator_in_entryRuleUserOperator2346);
            iv_ruleUserOperator=ruleUserOperator();

            state._fsp--;

             current =iv_ruleUserOperator; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleUserOperator2356); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleUserOperator"


    // $ANTLR start "ruleUserOperator"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1117:1: ruleUserOperator returns [EObject current=null] : (otherlv_0= 'op' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_parameters_3_0= ruleParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleParameter ) ) )* otherlv_6= ')' otherlv_7= '::' ( (otherlv_8= RULE_ID ) ) otherlv_9= '{' ( (lv_accessors_10_0= ruleAccessor ) ) (otherlv_11= ';' ( (lv_accessors_12_0= ruleAccessor ) ) )* otherlv_13= ';' otherlv_14= 'make' otherlv_15= '=' ( (lv_make_16_0= RULE_STRING ) ) otherlv_17= ';' otherlv_18= 'is_fsym' otherlv_19= '=' ( (lv_test_20_0= RULE_STRING ) ) otherlv_21= ';' otherlv_22= '}' ) ;
    public final EObject ruleUserOperator() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        Token otherlv_7=null;
        Token otherlv_8=null;
        Token otherlv_9=null;
        Token otherlv_11=null;
        Token otherlv_13=null;
        Token otherlv_14=null;
        Token otherlv_15=null;
        Token lv_make_16_0=null;
        Token otherlv_17=null;
        Token otherlv_18=null;
        Token otherlv_19=null;
        Token lv_test_20_0=null;
        Token otherlv_21=null;
        Token otherlv_22=null;
        EObject lv_parameters_3_0 = null;

        EObject lv_parameters_5_0 = null;

        EObject lv_accessors_10_0 = null;

        EObject lv_accessors_12_0 = null;


         enterRule(); 
            
        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1120:28: ( (otherlv_0= 'op' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_parameters_3_0= ruleParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleParameter ) ) )* otherlv_6= ')' otherlv_7= '::' ( (otherlv_8= RULE_ID ) ) otherlv_9= '{' ( (lv_accessors_10_0= ruleAccessor ) ) (otherlv_11= ';' ( (lv_accessors_12_0= ruleAccessor ) ) )* otherlv_13= ';' otherlv_14= 'make' otherlv_15= '=' ( (lv_make_16_0= RULE_STRING ) ) otherlv_17= ';' otherlv_18= 'is_fsym' otherlv_19= '=' ( (lv_test_20_0= RULE_STRING ) ) otherlv_21= ';' otherlv_22= '}' ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1121:1: (otherlv_0= 'op' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_parameters_3_0= ruleParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleParameter ) ) )* otherlv_6= ')' otherlv_7= '::' ( (otherlv_8= RULE_ID ) ) otherlv_9= '{' ( (lv_accessors_10_0= ruleAccessor ) ) (otherlv_11= ';' ( (lv_accessors_12_0= ruleAccessor ) ) )* otherlv_13= ';' otherlv_14= 'make' otherlv_15= '=' ( (lv_make_16_0= RULE_STRING ) ) otherlv_17= ';' otherlv_18= 'is_fsym' otherlv_19= '=' ( (lv_test_20_0= RULE_STRING ) ) otherlv_21= ';' otherlv_22= '}' )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1121:1: (otherlv_0= 'op' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_parameters_3_0= ruleParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleParameter ) ) )* otherlv_6= ')' otherlv_7= '::' ( (otherlv_8= RULE_ID ) ) otherlv_9= '{' ( (lv_accessors_10_0= ruleAccessor ) ) (otherlv_11= ';' ( (lv_accessors_12_0= ruleAccessor ) ) )* otherlv_13= ';' otherlv_14= 'make' otherlv_15= '=' ( (lv_make_16_0= RULE_STRING ) ) otherlv_17= ';' otherlv_18= 'is_fsym' otherlv_19= '=' ( (lv_test_20_0= RULE_STRING ) ) otherlv_21= ';' otherlv_22= '}' )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1121:3: otherlv_0= 'op' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '(' ( (lv_parameters_3_0= ruleParameter ) ) (otherlv_4= ',' ( (lv_parameters_5_0= ruleParameter ) ) )* otherlv_6= ')' otherlv_7= '::' ( (otherlv_8= RULE_ID ) ) otherlv_9= '{' ( (lv_accessors_10_0= ruleAccessor ) ) (otherlv_11= ';' ( (lv_accessors_12_0= ruleAccessor ) ) )* otherlv_13= ';' otherlv_14= 'make' otherlv_15= '=' ( (lv_make_16_0= RULE_STRING ) ) otherlv_17= ';' otherlv_18= 'is_fsym' otherlv_19= '=' ( (lv_test_20_0= RULE_STRING ) ) otherlv_21= ';' otherlv_22= '}'
            {
            otherlv_0=(Token)match(input,29,FOLLOW_29_in_ruleUserOperator2393); 

                	newLeafNode(otherlv_0, grammarAccess.getUserOperatorAccess().getOpKeyword_0());
                
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1125:1: ( (lv_name_1_0= RULE_ID ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1126:1: (lv_name_1_0= RULE_ID )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1126:1: (lv_name_1_0= RULE_ID )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1127:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleUserOperator2410); 

            			newLeafNode(lv_name_1_0, grammarAccess.getUserOperatorAccess().getNameIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getUserOperatorRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_1_0, 
                    		"ID");
            	    

            }


            }

            otherlv_2=(Token)match(input,27,FOLLOW_27_in_ruleUserOperator2427); 

                	newLeafNode(otherlv_2, grammarAccess.getUserOperatorAccess().getLeftParenthesisKeyword_2());
                
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1147:1: ( (lv_parameters_3_0= ruleParameter ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1148:1: (lv_parameters_3_0= ruleParameter )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1148:1: (lv_parameters_3_0= ruleParameter )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1149:3: lv_parameters_3_0= ruleParameter
            {
             
            	        newCompositeNode(grammarAccess.getUserOperatorAccess().getParametersParameterParserRuleCall_3_0()); 
            	    
            pushFollow(FOLLOW_ruleParameter_in_ruleUserOperator2448);
            lv_parameters_3_0=ruleParameter();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getUserOperatorRule());
            	        }
                   		add(
                   			current, 
                   			"parameters",
                    		lv_parameters_3_0, 
                    		"Parameter");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1165:2: (otherlv_4= ',' ( (lv_parameters_5_0= ruleParameter ) ) )*
            loop20:
            do {
                int alt20=2;
                int LA20_0 = input.LA(1);

                if ( (LA20_0==18) ) {
                    alt20=1;
                }


                switch (alt20) {
            	case 1 :
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1165:4: otherlv_4= ',' ( (lv_parameters_5_0= ruleParameter ) )
            	    {
            	    otherlv_4=(Token)match(input,18,FOLLOW_18_in_ruleUserOperator2461); 

            	        	newLeafNode(otherlv_4, grammarAccess.getUserOperatorAccess().getCommaKeyword_4_0());
            	        
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1169:1: ( (lv_parameters_5_0= ruleParameter ) )
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1170:1: (lv_parameters_5_0= ruleParameter )
            	    {
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1170:1: (lv_parameters_5_0= ruleParameter )
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1171:3: lv_parameters_5_0= ruleParameter
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getUserOperatorAccess().getParametersParameterParserRuleCall_4_1_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleParameter_in_ruleUserOperator2482);
            	    lv_parameters_5_0=ruleParameter();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getUserOperatorRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"parameters",
            	            		lv_parameters_5_0, 
            	            		"Parameter");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop20;
                }
            } while (true);

            otherlv_6=(Token)match(input,28,FOLLOW_28_in_ruleUserOperator2496); 

                	newLeafNode(otherlv_6, grammarAccess.getUserOperatorAccess().getRightParenthesisKeyword_5());
                
            otherlv_7=(Token)match(input,26,FOLLOW_26_in_ruleUserOperator2508); 

                	newLeafNode(otherlv_7, grammarAccess.getUserOperatorAccess().getColonColonKeyword_6());
                
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1195:1: ( (otherlv_8= RULE_ID ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1196:1: (otherlv_8= RULE_ID )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1196:1: (otherlv_8= RULE_ID )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1197:3: otherlv_8= RULE_ID
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getUserOperatorRule());
            	        }
                    
            otherlv_8=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleUserOperator2528); 

            		newLeafNode(otherlv_8, grammarAccess.getUserOperatorAccess().getTypeTerminalCrossReference_7_0()); 
            	

            }


            }

            otherlv_9=(Token)match(input,16,FOLLOW_16_in_ruleUserOperator2540); 

                	newLeafNode(otherlv_9, grammarAccess.getUserOperatorAccess().getLeftCurlyBracketKeyword_8());
                
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1212:1: ( (lv_accessors_10_0= ruleAccessor ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1213:1: (lv_accessors_10_0= ruleAccessor )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1213:1: (lv_accessors_10_0= ruleAccessor )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1214:3: lv_accessors_10_0= ruleAccessor
            {
             
            	        newCompositeNode(grammarAccess.getUserOperatorAccess().getAccessorsAccessorParserRuleCall_9_0()); 
            	    
            pushFollow(FOLLOW_ruleAccessor_in_ruleUserOperator2561);
            lv_accessors_10_0=ruleAccessor();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getUserOperatorRule());
            	        }
                   		add(
                   			current, 
                   			"accessors",
                    		lv_accessors_10_0, 
                    		"Accessor");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1230:2: (otherlv_11= ';' ( (lv_accessors_12_0= ruleAccessor ) ) )*
            loop21:
            do {
                int alt21=2;
                int LA21_0 = input.LA(1);

                if ( (LA21_0==12) ) {
                    int LA21_1 = input.LA(2);

                    if ( (LA21_1==33) ) {
                        alt21=1;
                    }


                }


                switch (alt21) {
            	case 1 :
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1230:4: otherlv_11= ';' ( (lv_accessors_12_0= ruleAccessor ) )
            	    {
            	    otherlv_11=(Token)match(input,12,FOLLOW_12_in_ruleUserOperator2574); 

            	        	newLeafNode(otherlv_11, grammarAccess.getUserOperatorAccess().getSemicolonKeyword_10_0());
            	        
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1234:1: ( (lv_accessors_12_0= ruleAccessor ) )
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1235:1: (lv_accessors_12_0= ruleAccessor )
            	    {
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1235:1: (lv_accessors_12_0= ruleAccessor )
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1236:3: lv_accessors_12_0= ruleAccessor
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getUserOperatorAccess().getAccessorsAccessorParserRuleCall_10_1_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleAccessor_in_ruleUserOperator2595);
            	    lv_accessors_12_0=ruleAccessor();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getUserOperatorRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"accessors",
            	            		lv_accessors_12_0, 
            	            		"Accessor");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop21;
                }
            } while (true);

            otherlv_13=(Token)match(input,12,FOLLOW_12_in_ruleUserOperator2609); 

                	newLeafNode(otherlv_13, grammarAccess.getUserOperatorAccess().getSemicolonKeyword_11());
                
            otherlv_14=(Token)match(input,30,FOLLOW_30_in_ruleUserOperator2621); 

                	newLeafNode(otherlv_14, grammarAccess.getUserOperatorAccess().getMakeKeyword_12());
                
            otherlv_15=(Token)match(input,31,FOLLOW_31_in_ruleUserOperator2633); 

                	newLeafNode(otherlv_15, grammarAccess.getUserOperatorAccess().getEqualsSignKeyword_13());
                
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1264:1: ( (lv_make_16_0= RULE_STRING ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1265:1: (lv_make_16_0= RULE_STRING )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1265:1: (lv_make_16_0= RULE_STRING )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1266:3: lv_make_16_0= RULE_STRING
            {
            lv_make_16_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleUserOperator2650); 

            			newLeafNode(lv_make_16_0, grammarAccess.getUserOperatorAccess().getMakeSTRINGTerminalRuleCall_14_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getUserOperatorRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"make",
                    		lv_make_16_0, 
                    		"STRING");
            	    

            }


            }

            otherlv_17=(Token)match(input,12,FOLLOW_12_in_ruleUserOperator2667); 

                	newLeafNode(otherlv_17, grammarAccess.getUserOperatorAccess().getSemicolonKeyword_15());
                
            otherlv_18=(Token)match(input,32,FOLLOW_32_in_ruleUserOperator2679); 

                	newLeafNode(otherlv_18, grammarAccess.getUserOperatorAccess().getIs_fsymKeyword_16());
                
            otherlv_19=(Token)match(input,31,FOLLOW_31_in_ruleUserOperator2691); 

                	newLeafNode(otherlv_19, grammarAccess.getUserOperatorAccess().getEqualsSignKeyword_17());
                
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1294:1: ( (lv_test_20_0= RULE_STRING ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1295:1: (lv_test_20_0= RULE_STRING )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1295:1: (lv_test_20_0= RULE_STRING )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1296:3: lv_test_20_0= RULE_STRING
            {
            lv_test_20_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleUserOperator2708); 

            			newLeafNode(lv_test_20_0, grammarAccess.getUserOperatorAccess().getTestSTRINGTerminalRuleCall_18_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getUserOperatorRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"test",
                    		lv_test_20_0, 
                    		"STRING");
            	    

            }


            }

            otherlv_21=(Token)match(input,12,FOLLOW_12_in_ruleUserOperator2725); 

                	newLeafNode(otherlv_21, grammarAccess.getUserOperatorAccess().getSemicolonKeyword_19());
                
            otherlv_22=(Token)match(input,19,FOLLOW_19_in_ruleUserOperator2737); 

                	newLeafNode(otherlv_22, grammarAccess.getUserOperatorAccess().getRightCurlyBracketKeyword_20());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleUserOperator"


    // $ANTLR start "entryRuleParameter"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1328:1: entryRuleParameter returns [EObject current=null] : iv_ruleParameter= ruleParameter EOF ;
    public final EObject entryRuleParameter() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleParameter = null;


        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1329:2: (iv_ruleParameter= ruleParameter EOF )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1330:2: iv_ruleParameter= ruleParameter EOF
            {
             newCompositeNode(grammarAccess.getParameterRule()); 
            pushFollow(FOLLOW_ruleParameter_in_entryRuleParameter2773);
            iv_ruleParameter=ruleParameter();

            state._fsp--;

             current =iv_ruleParameter; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleParameter2783); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleParameter"


    // $ANTLR start "ruleParameter"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1337:1: ruleParameter returns [EObject current=null] : ( ( (otherlv_0= RULE_ID ) ) ( (lv_name_1_0= ruleEString ) ) ) ;
    public final EObject ruleParameter() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        AntlrDatatypeRuleToken lv_name_1_0 = null;


         enterRule(); 
            
        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1340:28: ( ( ( (otherlv_0= RULE_ID ) ) ( (lv_name_1_0= ruleEString ) ) ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1341:1: ( ( (otherlv_0= RULE_ID ) ) ( (lv_name_1_0= ruleEString ) ) )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1341:1: ( ( (otherlv_0= RULE_ID ) ) ( (lv_name_1_0= ruleEString ) ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1341:2: ( (otherlv_0= RULE_ID ) ) ( (lv_name_1_0= ruleEString ) )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1341:2: ( (otherlv_0= RULE_ID ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1342:1: (otherlv_0= RULE_ID )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1342:1: (otherlv_0= RULE_ID )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1343:3: otherlv_0= RULE_ID
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getParameterRule());
            	        }
                    
            otherlv_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleParameter2828); 

            		newLeafNode(otherlv_0, grammarAccess.getParameterAccess().getTypeTerminalCrossReference_0_0()); 
            	

            }


            }

            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1354:2: ( (lv_name_1_0= ruleEString ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1355:1: (lv_name_1_0= ruleEString )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1355:1: (lv_name_1_0= ruleEString )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1356:3: lv_name_1_0= ruleEString
            {
             
            	        newCompositeNode(grammarAccess.getParameterAccess().getNameEStringParserRuleCall_1_0()); 
            	    
            pushFollow(FOLLOW_ruleEString_in_ruleParameter2849);
            lv_name_1_0=ruleEString();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getParameterRule());
            	        }
                   		set(
                   			current, 
                   			"name",
                    		lv_name_1_0, 
                    		"EString");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleParameter"


    // $ANTLR start "entryRuleAccessor"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1380:1: entryRuleAccessor returns [EObject current=null] : iv_ruleAccessor= ruleAccessor EOF ;
    public final EObject entryRuleAccessor() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAccessor = null;


        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1381:2: (iv_ruleAccessor= ruleAccessor EOF )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1382:2: iv_ruleAccessor= ruleAccessor EOF
            {
             newCompositeNode(grammarAccess.getAccessorRule()); 
            pushFollow(FOLLOW_ruleAccessor_in_entryRuleAccessor2885);
            iv_ruleAccessor=ruleAccessor();

            state._fsp--;

             current =iv_ruleAccessor; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleAccessor2895); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAccessor"


    // $ANTLR start "ruleAccessor"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1389:1: ruleAccessor returns [EObject current=null] : (otherlv_0= 'slot' ( (otherlv_1= RULE_ID ) ) otherlv_2= '=' ( (lv_java_3_0= RULE_STRING ) ) ) ;
    public final EObject ruleAccessor() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        Token lv_java_3_0=null;

         enterRule(); 
            
        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1392:28: ( (otherlv_0= 'slot' ( (otherlv_1= RULE_ID ) ) otherlv_2= '=' ( (lv_java_3_0= RULE_STRING ) ) ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1393:1: (otherlv_0= 'slot' ( (otherlv_1= RULE_ID ) ) otherlv_2= '=' ( (lv_java_3_0= RULE_STRING ) ) )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1393:1: (otherlv_0= 'slot' ( (otherlv_1= RULE_ID ) ) otherlv_2= '=' ( (lv_java_3_0= RULE_STRING ) ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1393:3: otherlv_0= 'slot' ( (otherlv_1= RULE_ID ) ) otherlv_2= '=' ( (lv_java_3_0= RULE_STRING ) )
            {
            otherlv_0=(Token)match(input,33,FOLLOW_33_in_ruleAccessor2932); 

                	newLeafNode(otherlv_0, grammarAccess.getAccessorAccess().getSlotKeyword_0());
                
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1397:1: ( (otherlv_1= RULE_ID ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1398:1: (otherlv_1= RULE_ID )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1398:1: (otherlv_1= RULE_ID )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1399:3: otherlv_1= RULE_ID
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getAccessorRule());
            	        }
                    
            otherlv_1=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleAccessor2952); 

            		newLeafNode(otherlv_1, grammarAccess.getAccessorAccess().getSlotParameterCrossReference_1_0()); 
            	

            }


            }

            otherlv_2=(Token)match(input,31,FOLLOW_31_in_ruleAccessor2964); 

                	newLeafNode(otherlv_2, grammarAccess.getAccessorAccess().getEqualsSignKeyword_2());
                
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1414:1: ( (lv_java_3_0= RULE_STRING ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1415:1: (lv_java_3_0= RULE_STRING )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1415:1: (lv_java_3_0= RULE_STRING )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1416:3: lv_java_3_0= RULE_STRING
            {
            lv_java_3_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleAccessor2981); 

            			newLeafNode(lv_java_3_0, grammarAccess.getAccessorAccess().getJavaSTRINGTerminalRuleCall_3_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getAccessorRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"java",
                    		lv_java_3_0, 
                    		"STRING");
            	    

            }


            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAccessor"


    // $ANTLR start "entryRuleFeatureException"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1440:1: entryRuleFeatureException returns [EObject current=null] : iv_ruleFeatureException= ruleFeatureException EOF ;
    public final EObject entryRuleFeatureException() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFeatureException = null;


        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1441:2: (iv_ruleFeatureException= ruleFeatureException EOF )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1442:2: iv_ruleFeatureException= ruleFeatureException EOF
            {
             newCompositeNode(grammarAccess.getFeatureExceptionRule()); 
            pushFollow(FOLLOW_ruleFeatureException_in_entryRuleFeatureException3022);
            iv_ruleFeatureException=ruleFeatureException();

            state._fsp--;

             current =iv_ruleFeatureException; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleFeatureException3032); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleFeatureException"


    // $ANTLR start "ruleFeatureException"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1449:1: ruleFeatureException returns [EObject current=null] : (otherlv_0= 'ignore' ( (otherlv_1= RULE_ID ) ) ) ;
    public final EObject ruleFeatureException() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;

         enterRule(); 
            
        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1452:28: ( (otherlv_0= 'ignore' ( (otherlv_1= RULE_ID ) ) ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1453:1: (otherlv_0= 'ignore' ( (otherlv_1= RULE_ID ) ) )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1453:1: (otherlv_0= 'ignore' ( (otherlv_1= RULE_ID ) ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1453:3: otherlv_0= 'ignore' ( (otherlv_1= RULE_ID ) )
            {
            otherlv_0=(Token)match(input,34,FOLLOW_34_in_ruleFeatureException3069); 

                	newLeafNode(otherlv_0, grammarAccess.getFeatureExceptionAccess().getIgnoreKeyword_0());
                
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1457:1: ( (otherlv_1= RULE_ID ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1458:1: (otherlv_1= RULE_ID )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1458:1: (otherlv_1= RULE_ID )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1459:3: otherlv_1= RULE_ID
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getFeatureExceptionRule());
            	        }
                    
            otherlv_1=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleFeatureException3089); 

            		newLeafNode(otherlv_1, grammarAccess.getFeatureExceptionAccess().getFeatureEStructuralFeatureCrossReference_1_0()); 
            	

            }


            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleFeatureException"


    // $ANTLR start "entryRuleFeatureParameter"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1478:1: entryRuleFeatureParameter returns [EObject current=null] : iv_ruleFeatureParameter= ruleFeatureParameter EOF ;
    public final EObject entryRuleFeatureParameter() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFeatureParameter = null;


        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1479:2: (iv_ruleFeatureParameter= ruleFeatureParameter EOF )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1480:2: iv_ruleFeatureParameter= ruleFeatureParameter EOF
            {
             newCompositeNode(grammarAccess.getFeatureParameterRule()); 
            pushFollow(FOLLOW_ruleFeatureParameter_in_entryRuleFeatureParameter3125);
            iv_ruleFeatureParameter=ruleFeatureParameter();

            state._fsp--;

             current =iv_ruleFeatureParameter; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleFeatureParameter3135); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleFeatureParameter"


    // $ANTLR start "ruleFeatureParameter"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1487:1: ruleFeatureParameter returns [EObject current=null] : ( ( (otherlv_0= RULE_ID ) ) | this_FeatureException_1= ruleFeatureException | this_SettedFeatureParameter_2= ruleSettedFeatureParameter ) ;
    public final EObject ruleFeatureParameter() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        EObject this_FeatureException_1 = null;

        EObject this_SettedFeatureParameter_2 = null;


         enterRule(); 
            
        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1490:28: ( ( ( (otherlv_0= RULE_ID ) ) | this_FeatureException_1= ruleFeatureException | this_SettedFeatureParameter_2= ruleSettedFeatureParameter ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1491:1: ( ( (otherlv_0= RULE_ID ) ) | this_FeatureException_1= ruleFeatureException | this_SettedFeatureParameter_2= ruleSettedFeatureParameter )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1491:1: ( ( (otherlv_0= RULE_ID ) ) | this_FeatureException_1= ruleFeatureException | this_SettedFeatureParameter_2= ruleSettedFeatureParameter )
            int alt22=3;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==RULE_ID) ) {
                int LA22_1 = input.LA(2);

                if ( (LA22_1==31) ) {
                    alt22=3;
                }
                else if ( (LA22_1==EOF||LA22_1==18||LA22_1==28) ) {
                    alt22=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 22, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA22_0==34) ) {
                alt22=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 22, 0, input);

                throw nvae;
            }
            switch (alt22) {
                case 1 :
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1491:2: ( (otherlv_0= RULE_ID ) )
                    {
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1491:2: ( (otherlv_0= RULE_ID ) )
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1492:1: (otherlv_0= RULE_ID )
                    {
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1492:1: (otherlv_0= RULE_ID )
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1493:3: otherlv_0= RULE_ID
                    {

                    			if (current==null) {
                    	            current = createModelElement(grammarAccess.getFeatureParameterRule());
                    	        }
                            
                    otherlv_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleFeatureParameter3180); 

                    		newLeafNode(otherlv_0, grammarAccess.getFeatureParameterAccess().getFeatureEStructuralFeatureCrossReference_0_0()); 
                    	

                    }


                    }


                    }
                    break;
                case 2 :
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1506:5: this_FeatureException_1= ruleFeatureException
                    {
                     
                            newCompositeNode(grammarAccess.getFeatureParameterAccess().getFeatureExceptionParserRuleCall_1()); 
                        
                    pushFollow(FOLLOW_ruleFeatureException_in_ruleFeatureParameter3208);
                    this_FeatureException_1=ruleFeatureException();

                    state._fsp--;

                     
                            current = this_FeatureException_1; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 3 :
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1516:5: this_SettedFeatureParameter_2= ruleSettedFeatureParameter
                    {
                     
                            newCompositeNode(grammarAccess.getFeatureParameterAccess().getSettedFeatureParameterParserRuleCall_2()); 
                        
                    pushFollow(FOLLOW_ruleSettedFeatureParameter_in_ruleFeatureParameter3235);
                    this_SettedFeatureParameter_2=ruleSettedFeatureParameter();

                    state._fsp--;

                     
                            current = this_SettedFeatureParameter_2; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleFeatureParameter"


    // $ANTLR start "entryRuleSettedFeatureParameter"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1532:1: entryRuleSettedFeatureParameter returns [EObject current=null] : iv_ruleSettedFeatureParameter= ruleSettedFeatureParameter EOF ;
    public final EObject entryRuleSettedFeatureParameter() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSettedFeatureParameter = null;


        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1533:2: (iv_ruleSettedFeatureParameter= ruleSettedFeatureParameter EOF )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1534:2: iv_ruleSettedFeatureParameter= ruleSettedFeatureParameter EOF
            {
             newCompositeNode(grammarAccess.getSettedFeatureParameterRule()); 
            pushFollow(FOLLOW_ruleSettedFeatureParameter_in_entryRuleSettedFeatureParameter3270);
            iv_ruleSettedFeatureParameter=ruleSettedFeatureParameter();

            state._fsp--;

             current =iv_ruleSettedFeatureParameter; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSettedFeatureParameter3280); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSettedFeatureParameter"


    // $ANTLR start "ruleSettedFeatureParameter"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1541:1: ruleSettedFeatureParameter returns [EObject current=null] : ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '=' ( (lv_value_2_0= ruleSettedValue ) ) ) ;
    public final EObject ruleSettedFeatureParameter() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        EObject lv_value_2_0 = null;


         enterRule(); 
            
        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1544:28: ( ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '=' ( (lv_value_2_0= ruleSettedValue ) ) ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1545:1: ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '=' ( (lv_value_2_0= ruleSettedValue ) ) )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1545:1: ( ( (otherlv_0= RULE_ID ) ) otherlv_1= '=' ( (lv_value_2_0= ruleSettedValue ) ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1545:2: ( (otherlv_0= RULE_ID ) ) otherlv_1= '=' ( (lv_value_2_0= ruleSettedValue ) )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1545:2: ( (otherlv_0= RULE_ID ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1546:1: (otherlv_0= RULE_ID )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1546:1: (otherlv_0= RULE_ID )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1547:3: otherlv_0= RULE_ID
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getSettedFeatureParameterRule());
            	        }
                    
            otherlv_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSettedFeatureParameter3325); 

            		newLeafNode(otherlv_0, grammarAccess.getSettedFeatureParameterAccess().getFeatureEStructuralFeatureCrossReference_0_0()); 
            	

            }


            }

            otherlv_1=(Token)match(input,31,FOLLOW_31_in_ruleSettedFeatureParameter3337); 

                	newLeafNode(otherlv_1, grammarAccess.getSettedFeatureParameterAccess().getEqualsSignKeyword_1());
                
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1562:1: ( (lv_value_2_0= ruleSettedValue ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1563:1: (lv_value_2_0= ruleSettedValue )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1563:1: (lv_value_2_0= ruleSettedValue )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1564:3: lv_value_2_0= ruleSettedValue
            {
             
            	        newCompositeNode(grammarAccess.getSettedFeatureParameterAccess().getValueSettedValueParserRuleCall_2_0()); 
            	    
            pushFollow(FOLLOW_ruleSettedValue_in_ruleSettedFeatureParameter3358);
            lv_value_2_0=ruleSettedValue();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getSettedFeatureParameterRule());
            	        }
                   		set(
                   			current, 
                   			"value",
                    		lv_value_2_0, 
                    		"SettedValue");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleSettedFeatureParameter"


    // $ANTLR start "entryRuleSettedValue"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1588:1: entryRuleSettedValue returns [EObject current=null] : iv_ruleSettedValue= ruleSettedValue EOF ;
    public final EObject entryRuleSettedValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSettedValue = null;


        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1589:2: (iv_ruleSettedValue= ruleSettedValue EOF )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1590:2: iv_ruleSettedValue= ruleSettedValue EOF
            {
             newCompositeNode(grammarAccess.getSettedValueRule()); 
            pushFollow(FOLLOW_ruleSettedValue_in_entryRuleSettedValue3394);
            iv_ruleSettedValue=ruleSettedValue();

            state._fsp--;

             current =iv_ruleSettedValue; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSettedValue3404); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSettedValue"


    // $ANTLR start "ruleSettedValue"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1597:1: ruleSettedValue returns [EObject current=null] : (this_JavaCodeValue_0= ruleJavaCodeValue | this_LiteralValue_1= ruleLiteralValue ) ;
    public final EObject ruleSettedValue() throws RecognitionException {
        EObject current = null;

        EObject this_JavaCodeValue_0 = null;

        EObject this_LiteralValue_1 = null;


         enterRule(); 
            
        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1600:28: ( (this_JavaCodeValue_0= ruleJavaCodeValue | this_LiteralValue_1= ruleLiteralValue ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1601:1: (this_JavaCodeValue_0= ruleJavaCodeValue | this_LiteralValue_1= ruleLiteralValue )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1601:1: (this_JavaCodeValue_0= ruleJavaCodeValue | this_LiteralValue_1= ruleLiteralValue )
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==RULE_STRING) ) {
                alt23=1;
            }
            else if ( (LA23_0==RULE_ID) ) {
                alt23=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 23, 0, input);

                throw nvae;
            }
            switch (alt23) {
                case 1 :
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1602:5: this_JavaCodeValue_0= ruleJavaCodeValue
                    {
                     
                            newCompositeNode(grammarAccess.getSettedValueAccess().getJavaCodeValueParserRuleCall_0()); 
                        
                    pushFollow(FOLLOW_ruleJavaCodeValue_in_ruleSettedValue3451);
                    this_JavaCodeValue_0=ruleJavaCodeValue();

                    state._fsp--;

                     
                            current = this_JavaCodeValue_0; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 2 :
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1612:5: this_LiteralValue_1= ruleLiteralValue
                    {
                     
                            newCompositeNode(grammarAccess.getSettedValueAccess().getLiteralValueParserRuleCall_1()); 
                        
                    pushFollow(FOLLOW_ruleLiteralValue_in_ruleSettedValue3478);
                    this_LiteralValue_1=ruleLiteralValue();

                    state._fsp--;

                     
                            current = this_LiteralValue_1; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleSettedValue"


    // $ANTLR start "entryRuleJavaCodeValue"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1628:1: entryRuleJavaCodeValue returns [EObject current=null] : iv_ruleJavaCodeValue= ruleJavaCodeValue EOF ;
    public final EObject entryRuleJavaCodeValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleJavaCodeValue = null;


        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1629:2: (iv_ruleJavaCodeValue= ruleJavaCodeValue EOF )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1630:2: iv_ruleJavaCodeValue= ruleJavaCodeValue EOF
            {
             newCompositeNode(grammarAccess.getJavaCodeValueRule()); 
            pushFollow(FOLLOW_ruleJavaCodeValue_in_entryRuleJavaCodeValue3513);
            iv_ruleJavaCodeValue=ruleJavaCodeValue();

            state._fsp--;

             current =iv_ruleJavaCodeValue; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleJavaCodeValue3523); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleJavaCodeValue"


    // $ANTLR start "ruleJavaCodeValue"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1637:1: ruleJavaCodeValue returns [EObject current=null] : ( (lv_java_0_0= RULE_STRING ) ) ;
    public final EObject ruleJavaCodeValue() throws RecognitionException {
        EObject current = null;

        Token lv_java_0_0=null;

         enterRule(); 
            
        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1640:28: ( ( (lv_java_0_0= RULE_STRING ) ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1641:1: ( (lv_java_0_0= RULE_STRING ) )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1641:1: ( (lv_java_0_0= RULE_STRING ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1642:1: (lv_java_0_0= RULE_STRING )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1642:1: (lv_java_0_0= RULE_STRING )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1643:3: lv_java_0_0= RULE_STRING
            {
            lv_java_0_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleJavaCodeValue3564); 

            			newLeafNode(lv_java_0_0, grammarAccess.getJavaCodeValueAccess().getJavaSTRINGTerminalRuleCall_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getJavaCodeValueRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"java",
                    		lv_java_0_0, 
                    		"STRING");
            	    

            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleJavaCodeValue"


    // $ANTLR start "entryRuleLiteralValue"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1667:1: entryRuleLiteralValue returns [EObject current=null] : iv_ruleLiteralValue= ruleLiteralValue EOF ;
    public final EObject entryRuleLiteralValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleLiteralValue = null;


        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1668:2: (iv_ruleLiteralValue= ruleLiteralValue EOF )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1669:2: iv_ruleLiteralValue= ruleLiteralValue EOF
            {
             newCompositeNode(grammarAccess.getLiteralValueRule()); 
            pushFollow(FOLLOW_ruleLiteralValue_in_entryRuleLiteralValue3604);
            iv_ruleLiteralValue=ruleLiteralValue();

            state._fsp--;

             current =iv_ruleLiteralValue; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleLiteralValue3614); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleLiteralValue"


    // $ANTLR start "ruleLiteralValue"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1676:1: ruleLiteralValue returns [EObject current=null] : ( ( ruleFQN ) ) ;
    public final EObject ruleLiteralValue() throws RecognitionException {
        EObject current = null;

         enterRule(); 
            
        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1679:28: ( ( ( ruleFQN ) ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1680:1: ( ( ruleFQN ) )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1680:1: ( ( ruleFQN ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1681:1: ( ruleFQN )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1681:1: ( ruleFQN )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1682:3: ruleFQN
            {

            			if (current==null) {
            	            current = createModelElement(grammarAccess.getLiteralValueRule());
            	        }
                    
             
            	        newCompositeNode(grammarAccess.getLiteralValueAccess().getLiteralEEnumLiteralCrossReference_0()); 
            	    
            pushFollow(FOLLOW_ruleFQN_in_ruleLiteralValue3661);
            ruleFQN();

            state._fsp--;

             
            	        afterParserOrEnumRuleCall();
            	    

            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleLiteralValue"


    // $ANTLR start "entryRuleEString"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1703:1: entryRuleEString returns [String current=null] : iv_ruleEString= ruleEString EOF ;
    public final String entryRuleEString() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleEString = null;


        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1704:2: (iv_ruleEString= ruleEString EOF )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1705:2: iv_ruleEString= ruleEString EOF
            {
             newCompositeNode(grammarAccess.getEStringRule()); 
            pushFollow(FOLLOW_ruleEString_in_entryRuleEString3697);
            iv_ruleEString=ruleEString();

            state._fsp--;

             current =iv_ruleEString.getText(); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleEString3708); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEString"


    // $ANTLR start "ruleEString"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1712:1: ruleEString returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_STRING_0= RULE_STRING | this_ID_1= RULE_ID ) ;
    public final AntlrDatatypeRuleToken ruleEString() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_STRING_0=null;
        Token this_ID_1=null;

         enterRule(); 
            
        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1715:28: ( (this_STRING_0= RULE_STRING | this_ID_1= RULE_ID ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1716:1: (this_STRING_0= RULE_STRING | this_ID_1= RULE_ID )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1716:1: (this_STRING_0= RULE_STRING | this_ID_1= RULE_ID )
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==RULE_STRING) ) {
                alt24=1;
            }
            else if ( (LA24_0==RULE_ID) ) {
                alt24=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 24, 0, input);

                throw nvae;
            }
            switch (alt24) {
                case 1 :
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1716:6: this_STRING_0= RULE_STRING
                    {
                    this_STRING_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleEString3748); 

                    		current.merge(this_STRING_0);
                        
                     
                        newLeafNode(this_STRING_0, grammarAccess.getEStringAccess().getSTRINGTerminalRuleCall_0()); 
                        

                    }
                    break;
                case 2 :
                    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1724:10: this_ID_1= RULE_ID
                    {
                    this_ID_1=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleEString3774); 

                    		current.merge(this_ID_1);
                        
                     
                        newLeafNode(this_ID_1, grammarAccess.getEStringAccess().getIDTerminalRuleCall_1()); 
                        

                    }
                    break;

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEString"


    // $ANTLR start "entryRuleFQN"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1743:1: entryRuleFQN returns [String current=null] : iv_ruleFQN= ruleFQN EOF ;
    public final String entryRuleFQN() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleFQN = null;


        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1744:2: (iv_ruleFQN= ruleFQN EOF )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1745:2: iv_ruleFQN= ruleFQN EOF
            {
             newCompositeNode(grammarAccess.getFQNRule()); 
            pushFollow(FOLLOW_ruleFQN_in_entryRuleFQN3824);
            iv_ruleFQN=ruleFQN();

            state._fsp--;

             current =iv_ruleFQN.getText(); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleFQN3835); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleFQN"


    // $ANTLR start "ruleFQN"
    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1752:1: ruleFQN returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* ) ;
    public final AntlrDatatypeRuleToken ruleFQN() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;
        Token kw=null;
        Token this_ID_2=null;

         enterRule(); 
            
        try {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1755:28: ( (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* ) )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1756:1: (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* )
            {
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1756:1: (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* )
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1756:6: this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )*
            {
            this_ID_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleFQN3875); 

            		current.merge(this_ID_0);
                
             
                newLeafNode(this_ID_0, grammarAccess.getFQNAccess().getIDTerminalRuleCall_0()); 
                
            // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1763:1: (kw= '.' this_ID_2= RULE_ID )*
            loop25:
            do {
                int alt25=2;
                int LA25_0 = input.LA(1);

                if ( (LA25_0==35) ) {
                    alt25=1;
                }


                switch (alt25) {
            	case 1 :
            	    // ../tom.mapping.dsl/src-gen/tom/mapping/dsl/parser/antlr/internal/InternalTomMapping.g:1764:2: kw= '.' this_ID_2= RULE_ID
            	    {
            	    kw=(Token)match(input,35,FOLLOW_35_in_ruleFQN3894); 

            	            current.merge(kw);
            	            newLeafNode(kw, grammarAccess.getFQNAccess().getFullStopKeyword_1_0()); 
            	        
            	    this_ID_2=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleFQN3909); 

            	    		current.merge(this_ID_2);
            	        
            	     
            	        newLeafNode(this_ID_2, grammarAccess.getFQNAccess().getIDTerminalRuleCall_1_1()); 
            	        

            	    }
            	    break;

            	default :
            	    break loop25;
                }
            } while (true);


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleFQN"

    // Delegated rules


    protected DFA14 dfa14 = new DFA14(this);
    static final String DFA14_eotS =
        "\13\uffff";
    static final String DFA14_eofS =
        "\6\uffff\1\11\3\uffff\1\11";
    static final String DFA14_minS =
        "\1\31\1\4\1\uffff\1\32\1\4\1\uffff\1\14\1\4\2\uffff\1\14";
    static final String DFA14_maxS =
        "\1\35\1\4\1\uffff\1\33\1\4\1\uffff\1\43\1\4\2\uffff\1\43";
    static final String DFA14_acceptS =
        "\2\uffff\1\4\2\uffff\1\3\2\uffff\1\2\1\1\1\uffff";
    static final String DFA14_specialS =
        "\13\uffff}>";
    static final String[] DFA14_transitionS = {
            "\1\2\3\uffff\1\1",
            "\1\3",
            "",
            "\1\4\1\5",
            "\1\6",
            "",
            "\1\11\16\uffff\1\10\7\uffff\1\7",
            "\1\12",
            "",
            "",
            "\1\11\16\uffff\1\10\7\uffff\1\7"
    };

    static final short[] DFA14_eot = DFA.unpackEncodedString(DFA14_eotS);
    static final short[] DFA14_eof = DFA.unpackEncodedString(DFA14_eofS);
    static final char[] DFA14_min = DFA.unpackEncodedStringToUnsignedChars(DFA14_minS);
    static final char[] DFA14_max = DFA.unpackEncodedStringToUnsignedChars(DFA14_maxS);
    static final short[] DFA14_accept = DFA.unpackEncodedString(DFA14_acceptS);
    static final short[] DFA14_special = DFA.unpackEncodedString(DFA14_specialS);
    static final short[][] DFA14_transition;

    static {
        int numStates = DFA14_transitionS.length;
        DFA14_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA14_transition[i] = DFA.unpackEncodedString(DFA14_transitionS[i]);
        }
    }

    class DFA14 extends DFA {

        public DFA14(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 14;
            this.eot = DFA14_eot;
            this.eof = DFA14_eof;
            this.min = DFA14_min;
            this.max = DFA14_max;
            this.accept = DFA14_accept;
            this.special = DFA14_special;
            this.transition = DFA14_transition;
        }
        public String getDescription() {
            return "508:1: (this_ClassOperator_0= ruleClassOperator | this_ClassOperatorWithExceptions_1= ruleClassOperatorWithExceptions | this_UserOperator_2= ruleUserOperator | this_AliasOperator_3= ruleAliasOperator )";
        }
    }
 

    public static final BitSet FOLLOW_ruleMapping_in_entryRuleMapping75 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleMapping85 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_11_in_ruleMapping122 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleMapping139 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleMapping156 = new BitSet(new long[]{0x000000000060E002L});
    public static final BitSet FOLLOW_13_in_ruleMapping169 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleMapping186 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleMapping203 = new BitSet(new long[]{0x000000000060C002L});
    public static final BitSet FOLLOW_14_in_ruleMapping218 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_ruleImport_in_ruleMapping239 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleMapping251 = new BitSet(new long[]{0x000000000060C002L});
    public static final BitSet FOLLOW_15_in_ruleMapping266 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_ruleMapping278 = new BitSet(new long[]{0x00000000001A0010L});
    public static final BitSet FOLLOW_17_in_ruleMapping293 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_ruleMapping305 = new BitSet(new long[]{0x00000000001A0010L});
    public static final BitSet FOLLOW_ruleTerminal_in_ruleMapping326 = new BitSet(new long[]{0x00000000000C0000L});
    public static final BitSet FOLLOW_18_in_ruleMapping339 = new BitSet(new long[]{0x00000000001A0010L});
    public static final BitSet FOLLOW_ruleTerminal_in_ruleMapping360 = new BitSet(new long[]{0x00000000000C0000L});
    public static final BitSet FOLLOW_19_in_ruleMapping374 = new BitSet(new long[]{0x0000000000180000L});
    public static final BitSet FOLLOW_20_in_ruleMapping389 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_ruleMapping401 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleMapping421 = new BitSet(new long[]{0x00000000000C0000L});
    public static final BitSet FOLLOW_18_in_ruleMapping434 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleMapping454 = new BitSet(new long[]{0x00000000000C0000L});
    public static final BitSet FOLLOW_19_in_ruleMapping468 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_ruleTerminal_in_ruleMapping499 = new BitSet(new long[]{0x00000000000C0000L});
    public static final BitSet FOLLOW_18_in_ruleMapping512 = new BitSet(new long[]{0x00000000001A0010L});
    public static final BitSet FOLLOW_ruleTerminal_in_ruleMapping533 = new BitSet(new long[]{0x00000000000C0000L});
    public static final BitSet FOLLOW_19_in_ruleMapping549 = new BitSet(new long[]{0x0000000000600002L});
    public static final BitSet FOLLOW_21_in_ruleMapping565 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_ruleMapping577 = new BitSet(new long[]{0x0000000022000000L});
    public static final BitSet FOLLOW_ruleOperator_in_ruleMapping598 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleMapping611 = new BitSet(new long[]{0x0000000022000000L});
    public static final BitSet FOLLOW_ruleOperator_in_ruleMapping632 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleMapping646 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_ruleMapping658 = new BitSet(new long[]{0x0000000000600002L});
    public static final BitSet FOLLOW_ruleModule_in_ruleMapping686 = new BitSet(new long[]{0x0000000000600002L});
    public static final BitSet FOLLOW_ruleModule_in_entryRuleModule724 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleModule734 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_ruleModule771 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleModule788 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_ruleModule805 = new BitSet(new long[]{0x0000000000280000L});
    public static final BitSet FOLLOW_21_in_ruleModule818 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_ruleModule830 = new BitSet(new long[]{0x0000000022000000L});
    public static final BitSet FOLLOW_ruleOperator_in_ruleModule851 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleModule864 = new BitSet(new long[]{0x0000000022000000L});
    public static final BitSet FOLLOW_ruleOperator_in_ruleModule885 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleModule899 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_ruleModule911 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_ruleModule925 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperator_in_entryRuleOperator961 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleOperator971 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleClassOperator_in_ruleOperator1018 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleClassOperatorWithExceptions_in_ruleOperator1045 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleUserOperator_in_ruleOperator1072 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAliasOperator_in_ruleOperator1099 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleImport_in_entryRuleImport1134 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleImport1144 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleImport1185 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleTerminal_in_entryRuleTerminal1225 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleTerminal1235 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleTerminal1277 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_ruleTerminal1294 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleFQN_in_ruleTerminal1317 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_24_in_ruleTerminal1335 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAliasOperator_in_entryRuleAliasOperator1385 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleAliasOperator1395 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_ruleAliasOperator1432 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleAliasOperator1449 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_ruleAliasOperator1466 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleAliasOperator1486 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_ruleAliasOperator1498 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleAliasNode_in_ruleAliasOperator1519 = new BitSet(new long[]{0x0000000010040000L});
    public static final BitSet FOLLOW_18_in_ruleAliasOperator1532 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleAliasNode_in_ruleAliasOperator1553 = new BitSet(new long[]{0x0000000010040000L});
    public static final BitSet FOLLOW_28_in_ruleAliasOperator1567 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAliasNode_in_entryRuleAliasNode1603 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleAliasNode1613 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFeatureNode_in_ruleAliasNode1660 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperatorNode_in_ruleAliasNode1687 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFeatureNode_in_entryRuleFeatureNode1722 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleFeatureNode1732 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleFeatureNode1773 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperatorNode_in_entryRuleOperatorNode1813 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleOperatorNode1823 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleOperatorNode1868 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_ruleOperatorNode1880 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleAliasNode_in_ruleOperatorNode1901 = new BitSet(new long[]{0x0000000010040000L});
    public static final BitSet FOLLOW_18_in_ruleOperatorNode1914 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleAliasNode_in_ruleOperatorNode1935 = new BitSet(new long[]{0x0000000010040000L});
    public static final BitSet FOLLOW_28_in_ruleOperatorNode1949 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleClassOperator_in_entryRuleClassOperator1985 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleClassOperator1995 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_ruleClassOperator2032 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleClassOperator2049 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_ruleClassOperator2066 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleFQN_in_ruleClassOperator2089 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleClassOperatorWithExceptions_in_entryRuleClassOperatorWithExceptions2125 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleClassOperatorWithExceptions2135 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_ruleClassOperatorWithExceptions2172 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleClassOperatorWithExceptions2189 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_ruleClassOperatorWithExceptions2206 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleFQN_in_ruleClassOperatorWithExceptions2229 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_ruleClassOperatorWithExceptions2241 = new BitSet(new long[]{0x0000000400000010L});
    public static final BitSet FOLLOW_ruleFeatureParameter_in_ruleClassOperatorWithExceptions2262 = new BitSet(new long[]{0x0000000010040000L});
    public static final BitSet FOLLOW_18_in_ruleClassOperatorWithExceptions2275 = new BitSet(new long[]{0x0000000400000010L});
    public static final BitSet FOLLOW_ruleFeatureParameter_in_ruleClassOperatorWithExceptions2296 = new BitSet(new long[]{0x0000000010040000L});
    public static final BitSet FOLLOW_28_in_ruleClassOperatorWithExceptions2310 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleUserOperator_in_entryRuleUserOperator2346 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleUserOperator2356 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_ruleUserOperator2393 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleUserOperator2410 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_ruleUserOperator2427 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleParameter_in_ruleUserOperator2448 = new BitSet(new long[]{0x0000000010040000L});
    public static final BitSet FOLLOW_18_in_ruleUserOperator2461 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ruleParameter_in_ruleUserOperator2482 = new BitSet(new long[]{0x0000000010040000L});
    public static final BitSet FOLLOW_28_in_ruleUserOperator2496 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_ruleUserOperator2508 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleUserOperator2528 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_ruleUserOperator2540 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_ruleAccessor_in_ruleUserOperator2561 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleUserOperator2574 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_ruleAccessor_in_ruleUserOperator2595 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleUserOperator2609 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_30_in_ruleUserOperator2621 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_31_in_ruleUserOperator2633 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleUserOperator2650 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleUserOperator2667 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_32_in_ruleUserOperator2679 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_31_in_ruleUserOperator2691 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleUserOperator2708 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_ruleUserOperator2725 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_ruleUserOperator2737 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleParameter_in_entryRuleParameter2773 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleParameter2783 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleParameter2828 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_ruleEString_in_ruleParameter2849 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAccessor_in_entryRuleAccessor2885 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleAccessor2895 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_ruleAccessor2932 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleAccessor2952 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_31_in_ruleAccessor2964 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleAccessor2981 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFeatureException_in_entryRuleFeatureException3022 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleFeatureException3032 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_ruleFeatureException3069 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleFeatureException3089 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFeatureParameter_in_entryRuleFeatureParameter3125 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleFeatureParameter3135 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleFeatureParameter3180 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFeatureException_in_ruleFeatureParameter3208 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSettedFeatureParameter_in_ruleFeatureParameter3235 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSettedFeatureParameter_in_entryRuleSettedFeatureParameter3270 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSettedFeatureParameter3280 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSettedFeatureParameter3325 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_31_in_ruleSettedFeatureParameter3337 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_ruleSettedValue_in_ruleSettedFeatureParameter3358 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSettedValue_in_entryRuleSettedValue3394 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSettedValue3404 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleJavaCodeValue_in_ruleSettedValue3451 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleLiteralValue_in_ruleSettedValue3478 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleJavaCodeValue_in_entryRuleJavaCodeValue3513 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleJavaCodeValue3523 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleJavaCodeValue3564 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleLiteralValue_in_entryRuleLiteralValue3604 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleLiteralValue3614 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFQN_in_ruleLiteralValue3661 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleEString_in_entryRuleEString3697 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleEString3708 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleEString3748 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleEString3774 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFQN_in_entryRuleFQN3824 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleFQN3835 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleFQN3875 = new BitSet(new long[]{0x0000000800000002L});
    public static final BitSet FOLLOW_35_in_ruleFQN3894 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleFQN3909 = new BitSet(new long[]{0x0000000800000002L});

}
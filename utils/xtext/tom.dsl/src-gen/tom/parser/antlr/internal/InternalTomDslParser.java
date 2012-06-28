package tom.parser.antlr.internal; 

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import tom.services.TomDslGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalTomDslParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_FIRST_LEVEL_LBRACKET", "RULE_ID", "RULE_FIRST_LEVEL_RBRACKET", "RULE_JAVAMETHOD", "RULE_BRCKTSTMT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'%include'", "'/'", "'.'", "'%op'", "'('", "','", "')'", "'is_fsym'", "'get_slot'", "'make'", "'%oparray'", "'*'", "'get_size'", "'get_element'", "'make_empty'", "'make_append'", "':'", "'%typeterm'", "'implement'", "'is_sort'", "'equals'"
    };
    public static final int RULE_ID=5;
    public static final int T__29=29;
    public static final int T__28=28;
    public static final int T__27=27;
    public static final int T__26=26;
    public static final int T__25=25;
    public static final int T__24=24;
    public static final int RULE_JAVAMETHOD=7;
    public static final int T__23=23;
    public static final int T__22=22;
    public static final int RULE_ANY_OTHER=12;
    public static final int T__21=21;
    public static final int T__20=20;
    public static final int RULE_SL_COMMENT=10;
    public static final int EOF=-1;
    public static final int RULE_ML_COMMENT=9;
    public static final int RULE_BRCKTSTMT=8;
    public static final int T__30=30;
    public static final int T__19=19;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int T__33=33;
    public static final int T__16=16;
    public static final int T__15=15;
    public static final int T__18=18;
    public static final int T__17=17;
    public static final int RULE_FIRST_LEVEL_LBRACKET=4;
    public static final int T__14=14;
    public static final int T__13=13;
    public static final int RULE_FIRST_LEVEL_RBRACKET=6;
    public static final int RULE_WS=11;

    // delegates
    // delegators


        public InternalTomDslParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalTomDslParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalTomDslParser.tokenNames; }
    public String getGrammarFileName() { return "../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g"; }



     	private TomDslGrammarAccess grammarAccess;
     	
        public InternalTomDslParser(TokenStream input, TomDslGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }
        
        @Override
        protected String getFirstRuleName() {
        	return "TomFile";	
       	}
       	
       	@Override
       	protected TomDslGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}



    // $ANTLR start "entryRuleTomFile"
    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:67:1: entryRuleTomFile returns [EObject current=null] : iv_ruleTomFile= ruleTomFile EOF ;
    public final EObject entryRuleTomFile() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTomFile = null;


        try {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:68:2: (iv_ruleTomFile= ruleTomFile EOF )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:69:2: iv_ruleTomFile= ruleTomFile EOF
            {
             newCompositeNode(grammarAccess.getTomFileRule()); 
            pushFollow(FOLLOW_ruleTomFile_in_entryRuleTomFile75);
            iv_ruleTomFile=ruleTomFile();

            state._fsp--;

             current =iv_ruleTomFile; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleTomFile85); 

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
    // $ANTLR end "entryRuleTomFile"


    // $ANTLR start "ruleTomFile"
    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:76:1: ruleTomFile returns [EObject current=null] : ( ( (lv_ops_0_0= ruleArrayOperation ) ) | ( (lv_terms_1_0= ruleTypeTerm ) ) | ( (lv_inc_2_0= ruleInclude ) ) | ( (lv_locals_3_0= ruleLocal ) ) )* ;
    public final EObject ruleTomFile() throws RecognitionException {
        EObject current = null;

        EObject lv_ops_0_0 = null;

        EObject lv_terms_1_0 = null;

        EObject lv_inc_2_0 = null;

        AntlrDatatypeRuleToken lv_locals_3_0 = null;


         enterRule(); 
            
        try {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:79:28: ( ( ( (lv_ops_0_0= ruleArrayOperation ) ) | ( (lv_terms_1_0= ruleTypeTerm ) ) | ( (lv_inc_2_0= ruleInclude ) ) | ( (lv_locals_3_0= ruleLocal ) ) )* )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:80:1: ( ( (lv_ops_0_0= ruleArrayOperation ) ) | ( (lv_terms_1_0= ruleTypeTerm ) ) | ( (lv_inc_2_0= ruleInclude ) ) | ( (lv_locals_3_0= ruleLocal ) ) )*
            {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:80:1: ( ( (lv_ops_0_0= ruleArrayOperation ) ) | ( (lv_terms_1_0= ruleTypeTerm ) ) | ( (lv_inc_2_0= ruleInclude ) ) | ( (lv_locals_3_0= ruleLocal ) ) )*
            loop1:
            do {
                int alt1=5;
                switch ( input.LA(1) ) {
                case 16:
                case 23:
                    {
                    alt1=1;
                    }
                    break;
                case 30:
                    {
                    alt1=2;
                    }
                    break;
                case 13:
                    {
                    alt1=3;
                    }
                    break;
                case RULE_JAVAMETHOD:
                    {
                    alt1=4;
                    }
                    break;

                }

                switch (alt1) {
            	case 1 :
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:80:2: ( (lv_ops_0_0= ruleArrayOperation ) )
            	    {
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:80:2: ( (lv_ops_0_0= ruleArrayOperation ) )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:81:1: (lv_ops_0_0= ruleArrayOperation )
            	    {
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:81:1: (lv_ops_0_0= ruleArrayOperation )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:82:3: lv_ops_0_0= ruleArrayOperation
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getTomFileAccess().getOpsArrayOperationParserRuleCall_0_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleArrayOperation_in_ruleTomFile131);
            	    lv_ops_0_0=ruleArrayOperation();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getTomFileRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"ops",
            	            		lv_ops_0_0, 
            	            		"ArrayOperation");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }


            	    }
            	    break;
            	case 2 :
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:99:6: ( (lv_terms_1_0= ruleTypeTerm ) )
            	    {
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:99:6: ( (lv_terms_1_0= ruleTypeTerm ) )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:100:1: (lv_terms_1_0= ruleTypeTerm )
            	    {
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:100:1: (lv_terms_1_0= ruleTypeTerm )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:101:3: lv_terms_1_0= ruleTypeTerm
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getTomFileAccess().getTermsTypeTermParserRuleCall_1_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleTypeTerm_in_ruleTomFile158);
            	    lv_terms_1_0=ruleTypeTerm();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getTomFileRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"terms",
            	            		lv_terms_1_0, 
            	            		"TypeTerm");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }


            	    }
            	    break;
            	case 3 :
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:118:6: ( (lv_inc_2_0= ruleInclude ) )
            	    {
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:118:6: ( (lv_inc_2_0= ruleInclude ) )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:119:1: (lv_inc_2_0= ruleInclude )
            	    {
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:119:1: (lv_inc_2_0= ruleInclude )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:120:3: lv_inc_2_0= ruleInclude
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getTomFileAccess().getIncIncludeParserRuleCall_2_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleInclude_in_ruleTomFile185);
            	    lv_inc_2_0=ruleInclude();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getTomFileRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"inc",
            	            		lv_inc_2_0, 
            	            		"Include");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }


            	    }
            	    break;
            	case 4 :
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:137:6: ( (lv_locals_3_0= ruleLocal ) )
            	    {
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:137:6: ( (lv_locals_3_0= ruleLocal ) )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:138:1: (lv_locals_3_0= ruleLocal )
            	    {
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:138:1: (lv_locals_3_0= ruleLocal )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:139:3: lv_locals_3_0= ruleLocal
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getTomFileAccess().getLocalsLocalParserRuleCall_3_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleLocal_in_ruleTomFile212);
            	    lv_locals_3_0=ruleLocal();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getTomFileRule());
            	    	        }
            	           		set(
            	           			current, 
            	           			"locals",
            	            		lv_locals_3_0, 
            	            		"Local");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


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
    // $ANTLR end "ruleTomFile"


    // $ANTLR start "entryRuleArrayOperation"
    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:163:1: entryRuleArrayOperation returns [EObject current=null] : iv_ruleArrayOperation= ruleArrayOperation EOF ;
    public final EObject entryRuleArrayOperation() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleArrayOperation = null;


        try {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:164:2: (iv_ruleArrayOperation= ruleArrayOperation EOF )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:165:2: iv_ruleArrayOperation= ruleArrayOperation EOF
            {
             newCompositeNode(grammarAccess.getArrayOperationRule()); 
            pushFollow(FOLLOW_ruleArrayOperation_in_entryRuleArrayOperation249);
            iv_ruleArrayOperation=ruleArrayOperation();

            state._fsp--;

             current =iv_ruleArrayOperation; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleArrayOperation259); 

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
    // $ANTLR end "entryRuleArrayOperation"


    // $ANTLR start "ruleArrayOperation"
    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:172:1: ruleArrayOperation returns [EObject current=null] : (this_Operation_0= ruleOperation | this_OperationArray_1= ruleOperationArray ) ;
    public final EObject ruleArrayOperation() throws RecognitionException {
        EObject current = null;

        EObject this_Operation_0 = null;

        EObject this_OperationArray_1 = null;


         enterRule(); 
            
        try {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:175:28: ( (this_Operation_0= ruleOperation | this_OperationArray_1= ruleOperationArray ) )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:176:1: (this_Operation_0= ruleOperation | this_OperationArray_1= ruleOperationArray )
            {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:176:1: (this_Operation_0= ruleOperation | this_OperationArray_1= ruleOperationArray )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==16) ) {
                alt2=1;
            }
            else if ( (LA2_0==23) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:177:5: this_Operation_0= ruleOperation
                    {
                     
                            newCompositeNode(grammarAccess.getArrayOperationAccess().getOperationParserRuleCall_0()); 
                        
                    pushFollow(FOLLOW_ruleOperation_in_ruleArrayOperation306);
                    this_Operation_0=ruleOperation();

                    state._fsp--;

                     
                            current = this_Operation_0; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 2 :
                    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:187:5: this_OperationArray_1= ruleOperationArray
                    {
                     
                            newCompositeNode(grammarAccess.getArrayOperationAccess().getOperationArrayParserRuleCall_1()); 
                        
                    pushFollow(FOLLOW_ruleOperationArray_in_ruleArrayOperation333);
                    this_OperationArray_1=ruleOperationArray();

                    state._fsp--;

                     
                            current = this_OperationArray_1; 
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
    // $ANTLR end "ruleArrayOperation"


    // $ANTLR start "entryRuleInclude"
    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:203:1: entryRuleInclude returns [EObject current=null] : iv_ruleInclude= ruleInclude EOF ;
    public final EObject entryRuleInclude() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleInclude = null;


        try {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:204:2: (iv_ruleInclude= ruleInclude EOF )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:205:2: iv_ruleInclude= ruleInclude EOF
            {
             newCompositeNode(grammarAccess.getIncludeRule()); 
            pushFollow(FOLLOW_ruleInclude_in_entryRuleInclude368);
            iv_ruleInclude=ruleInclude();

            state._fsp--;

             current =iv_ruleInclude; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleInclude378); 

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
    // $ANTLR end "entryRuleInclude"


    // $ANTLR start "ruleInclude"
    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:212:1: ruleInclude returns [EObject current=null] : (otherlv_0= '%include' this_FIRST_LEVEL_LBRACKET_1= RULE_FIRST_LEVEL_LBRACKET ( ( (lv_path_2_1= RULE_ID | lv_path_2_2= '/' | lv_path_2_3= '.' ) ) )+ this_FIRST_LEVEL_RBRACKET_3= RULE_FIRST_LEVEL_RBRACKET ) ;
    public final EObject ruleInclude() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token this_FIRST_LEVEL_LBRACKET_1=null;
        Token lv_path_2_1=null;
        Token lv_path_2_2=null;
        Token lv_path_2_3=null;
        Token this_FIRST_LEVEL_RBRACKET_3=null;

         enterRule(); 
            
        try {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:215:28: ( (otherlv_0= '%include' this_FIRST_LEVEL_LBRACKET_1= RULE_FIRST_LEVEL_LBRACKET ( ( (lv_path_2_1= RULE_ID | lv_path_2_2= '/' | lv_path_2_3= '.' ) ) )+ this_FIRST_LEVEL_RBRACKET_3= RULE_FIRST_LEVEL_RBRACKET ) )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:216:1: (otherlv_0= '%include' this_FIRST_LEVEL_LBRACKET_1= RULE_FIRST_LEVEL_LBRACKET ( ( (lv_path_2_1= RULE_ID | lv_path_2_2= '/' | lv_path_2_3= '.' ) ) )+ this_FIRST_LEVEL_RBRACKET_3= RULE_FIRST_LEVEL_RBRACKET )
            {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:216:1: (otherlv_0= '%include' this_FIRST_LEVEL_LBRACKET_1= RULE_FIRST_LEVEL_LBRACKET ( ( (lv_path_2_1= RULE_ID | lv_path_2_2= '/' | lv_path_2_3= '.' ) ) )+ this_FIRST_LEVEL_RBRACKET_3= RULE_FIRST_LEVEL_RBRACKET )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:216:3: otherlv_0= '%include' this_FIRST_LEVEL_LBRACKET_1= RULE_FIRST_LEVEL_LBRACKET ( ( (lv_path_2_1= RULE_ID | lv_path_2_2= '/' | lv_path_2_3= '.' ) ) )+ this_FIRST_LEVEL_RBRACKET_3= RULE_FIRST_LEVEL_RBRACKET
            {
            otherlv_0=(Token)match(input,13,FOLLOW_13_in_ruleInclude415); 

                	newLeafNode(otherlv_0, grammarAccess.getIncludeAccess().getIncludeKeyword_0());
                
            this_FIRST_LEVEL_LBRACKET_1=(Token)match(input,RULE_FIRST_LEVEL_LBRACKET,FOLLOW_RULE_FIRST_LEVEL_LBRACKET_in_ruleInclude426); 
             
                newLeafNode(this_FIRST_LEVEL_LBRACKET_1, grammarAccess.getIncludeAccess().getFIRST_LEVEL_LBRACKETTerminalRuleCall_1()); 
                
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:224:1: ( ( (lv_path_2_1= RULE_ID | lv_path_2_2= '/' | lv_path_2_3= '.' ) ) )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==RULE_ID||(LA4_0>=14 && LA4_0<=15)) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:225:1: ( (lv_path_2_1= RULE_ID | lv_path_2_2= '/' | lv_path_2_3= '.' ) )
            	    {
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:225:1: ( (lv_path_2_1= RULE_ID | lv_path_2_2= '/' | lv_path_2_3= '.' ) )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:226:1: (lv_path_2_1= RULE_ID | lv_path_2_2= '/' | lv_path_2_3= '.' )
            	    {
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:226:1: (lv_path_2_1= RULE_ID | lv_path_2_2= '/' | lv_path_2_3= '.' )
            	    int alt3=3;
            	    switch ( input.LA(1) ) {
            	    case RULE_ID:
            	        {
            	        alt3=1;
            	        }
            	        break;
            	    case 14:
            	        {
            	        alt3=2;
            	        }
            	        break;
            	    case 15:
            	        {
            	        alt3=3;
            	        }
            	        break;
            	    default:
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 3, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt3) {
            	        case 1 :
            	            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:227:3: lv_path_2_1= RULE_ID
            	            {
            	            lv_path_2_1=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleInclude444); 

            	            			newLeafNode(lv_path_2_1, grammarAccess.getIncludeAccess().getPathIDTerminalRuleCall_2_0_0()); 
            	            		

            	            	        if (current==null) {
            	            	            current = createModelElement(grammarAccess.getIncludeRule());
            	            	        }
            	                   		addWithLastConsumed(
            	                   			current, 
            	                   			"path",
            	                    		lv_path_2_1, 
            	                    		"ID");
            	            	    

            	            }
            	            break;
            	        case 2 :
            	            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:242:8: lv_path_2_2= '/'
            	            {
            	            lv_path_2_2=(Token)match(input,14,FOLLOW_14_in_ruleInclude465); 

            	                    newLeafNode(lv_path_2_2, grammarAccess.getIncludeAccess().getPathSolidusKeyword_2_0_1());
            	                

            	            	        if (current==null) {
            	            	            current = createModelElement(grammarAccess.getIncludeRule());
            	            	        }
            	                   		addWithLastConsumed(current, "path", lv_path_2_2, null);
            	            	    

            	            }
            	            break;
            	        case 3 :
            	            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:254:8: lv_path_2_3= '.'
            	            {
            	            lv_path_2_3=(Token)match(input,15,FOLLOW_15_in_ruleInclude494); 

            	                    newLeafNode(lv_path_2_3, grammarAccess.getIncludeAccess().getPathFullStopKeyword_2_0_2());
            	                

            	            	        if (current==null) {
            	            	            current = createModelElement(grammarAccess.getIncludeRule());
            	            	        }
            	                   		addWithLastConsumed(current, "path", lv_path_2_3, null);
            	            	    

            	            }
            	            break;

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);

            this_FIRST_LEVEL_RBRACKET_3=(Token)match(input,RULE_FIRST_LEVEL_RBRACKET,FOLLOW_RULE_FIRST_LEVEL_RBRACKET_in_ruleInclude522); 
             
                newLeafNode(this_FIRST_LEVEL_RBRACKET_3, grammarAccess.getIncludeAccess().getFIRST_LEVEL_RBRACKETTerminalRuleCall_3()); 
                

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
    // $ANTLR end "ruleInclude"


    // $ANTLR start "entryRuleLocal"
    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:281:1: entryRuleLocal returns [String current=null] : iv_ruleLocal= ruleLocal EOF ;
    public final String entryRuleLocal() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleLocal = null;


        try {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:282:2: (iv_ruleLocal= ruleLocal EOF )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:283:2: iv_ruleLocal= ruleLocal EOF
            {
             newCompositeNode(grammarAccess.getLocalRule()); 
            pushFollow(FOLLOW_ruleLocal_in_entryRuleLocal558);
            iv_ruleLocal=ruleLocal();

            state._fsp--;

             current =iv_ruleLocal.getText(); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleLocal569); 

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
    // $ANTLR end "entryRuleLocal"


    // $ANTLR start "ruleLocal"
    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:290:1: ruleLocal returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : this_JAVAMETHOD_0= RULE_JAVAMETHOD ;
    public final AntlrDatatypeRuleToken ruleLocal() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_JAVAMETHOD_0=null;

         enterRule(); 
            
        try {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:293:28: (this_JAVAMETHOD_0= RULE_JAVAMETHOD )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:294:5: this_JAVAMETHOD_0= RULE_JAVAMETHOD
            {
            this_JAVAMETHOD_0=(Token)match(input,RULE_JAVAMETHOD,FOLLOW_RULE_JAVAMETHOD_in_ruleLocal608); 

            		current.merge(this_JAVAMETHOD_0);
                
             
                newLeafNode(this_JAVAMETHOD_0, grammarAccess.getLocalAccess().getJAVAMETHODTerminalRuleCall()); 
                

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
    // $ANTLR end "ruleLocal"


    // $ANTLR start "entryRuleOperation"
    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:309:1: entryRuleOperation returns [EObject current=null] : iv_ruleOperation= ruleOperation EOF ;
    public final EObject entryRuleOperation() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOperation = null;


        try {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:310:2: (iv_ruleOperation= ruleOperation EOF )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:311:2: iv_ruleOperation= ruleOperation EOF
            {
             newCompositeNode(grammarAccess.getOperationRule()); 
            pushFollow(FOLLOW_ruleOperation_in_entryRuleOperation652);
            iv_ruleOperation=ruleOperation();

            state._fsp--;

             current =iv_ruleOperation; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleOperation662); 

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
    // $ANTLR end "entryRuleOperation"


    // $ANTLR start "ruleOperation"
    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:318:1: ruleOperation returns [EObject current=null] : (otherlv_0= '%op' ( (lv_term_1_0= RULE_ID ) ) ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '(' ( ( (lv_arg_4_0= ruleARG ) ) (otherlv_5= ',' ( (lv_arg_6_0= ruleARG ) ) )* )? otherlv_7= ')' this_FIRST_LEVEL_LBRACKET_8= RULE_FIRST_LEVEL_LBRACKET otherlv_9= 'is_fsym' ruleParID ( (lv_fsym_11_0= ruleJavaBody ) ) (otherlv_12= 'get_slot' ruleParIDList ( (lv_slot_14_0= ruleJavaBody ) ) )* otherlv_15= 'make' ruleParIDList ( (lv_make_17_0= ruleJavaBody ) ) this_FIRST_LEVEL_RBRACKET_18= RULE_FIRST_LEVEL_RBRACKET ) ;
    public final EObject ruleOperation() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_term_1_0=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_7=null;
        Token this_FIRST_LEVEL_LBRACKET_8=null;
        Token otherlv_9=null;
        Token otherlv_12=null;
        Token otherlv_15=null;
        Token this_FIRST_LEVEL_RBRACKET_18=null;
        EObject lv_arg_4_0 = null;

        EObject lv_arg_6_0 = null;

        EObject lv_fsym_11_0 = null;

        EObject lv_slot_14_0 = null;

        EObject lv_make_17_0 = null;


         enterRule(); 
            
        try {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:321:28: ( (otherlv_0= '%op' ( (lv_term_1_0= RULE_ID ) ) ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '(' ( ( (lv_arg_4_0= ruleARG ) ) (otherlv_5= ',' ( (lv_arg_6_0= ruleARG ) ) )* )? otherlv_7= ')' this_FIRST_LEVEL_LBRACKET_8= RULE_FIRST_LEVEL_LBRACKET otherlv_9= 'is_fsym' ruleParID ( (lv_fsym_11_0= ruleJavaBody ) ) (otherlv_12= 'get_slot' ruleParIDList ( (lv_slot_14_0= ruleJavaBody ) ) )* otherlv_15= 'make' ruleParIDList ( (lv_make_17_0= ruleJavaBody ) ) this_FIRST_LEVEL_RBRACKET_18= RULE_FIRST_LEVEL_RBRACKET ) )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:322:1: (otherlv_0= '%op' ( (lv_term_1_0= RULE_ID ) ) ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '(' ( ( (lv_arg_4_0= ruleARG ) ) (otherlv_5= ',' ( (lv_arg_6_0= ruleARG ) ) )* )? otherlv_7= ')' this_FIRST_LEVEL_LBRACKET_8= RULE_FIRST_LEVEL_LBRACKET otherlv_9= 'is_fsym' ruleParID ( (lv_fsym_11_0= ruleJavaBody ) ) (otherlv_12= 'get_slot' ruleParIDList ( (lv_slot_14_0= ruleJavaBody ) ) )* otherlv_15= 'make' ruleParIDList ( (lv_make_17_0= ruleJavaBody ) ) this_FIRST_LEVEL_RBRACKET_18= RULE_FIRST_LEVEL_RBRACKET )
            {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:322:1: (otherlv_0= '%op' ( (lv_term_1_0= RULE_ID ) ) ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '(' ( ( (lv_arg_4_0= ruleARG ) ) (otherlv_5= ',' ( (lv_arg_6_0= ruleARG ) ) )* )? otherlv_7= ')' this_FIRST_LEVEL_LBRACKET_8= RULE_FIRST_LEVEL_LBRACKET otherlv_9= 'is_fsym' ruleParID ( (lv_fsym_11_0= ruleJavaBody ) ) (otherlv_12= 'get_slot' ruleParIDList ( (lv_slot_14_0= ruleJavaBody ) ) )* otherlv_15= 'make' ruleParIDList ( (lv_make_17_0= ruleJavaBody ) ) this_FIRST_LEVEL_RBRACKET_18= RULE_FIRST_LEVEL_RBRACKET )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:322:3: otherlv_0= '%op' ( (lv_term_1_0= RULE_ID ) ) ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '(' ( ( (lv_arg_4_0= ruleARG ) ) (otherlv_5= ',' ( (lv_arg_6_0= ruleARG ) ) )* )? otherlv_7= ')' this_FIRST_LEVEL_LBRACKET_8= RULE_FIRST_LEVEL_LBRACKET otherlv_9= 'is_fsym' ruleParID ( (lv_fsym_11_0= ruleJavaBody ) ) (otherlv_12= 'get_slot' ruleParIDList ( (lv_slot_14_0= ruleJavaBody ) ) )* otherlv_15= 'make' ruleParIDList ( (lv_make_17_0= ruleJavaBody ) ) this_FIRST_LEVEL_RBRACKET_18= RULE_FIRST_LEVEL_RBRACKET
            {
            otherlv_0=(Token)match(input,16,FOLLOW_16_in_ruleOperation699); 

                	newLeafNode(otherlv_0, grammarAccess.getOperationAccess().getOpKeyword_0());
                
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:326:1: ( (lv_term_1_0= RULE_ID ) )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:327:1: (lv_term_1_0= RULE_ID )
            {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:327:1: (lv_term_1_0= RULE_ID )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:328:3: lv_term_1_0= RULE_ID
            {
            lv_term_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleOperation716); 

            			newLeafNode(lv_term_1_0, grammarAccess.getOperationAccess().getTermIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getOperationRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"term",
                    		lv_term_1_0, 
                    		"ID");
            	    

            }


            }

            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:344:2: ( (lv_name_2_0= RULE_ID ) )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:345:1: (lv_name_2_0= RULE_ID )
            {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:345:1: (lv_name_2_0= RULE_ID )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:346:3: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleOperation738); 

            			newLeafNode(lv_name_2_0, grammarAccess.getOperationAccess().getNameIDTerminalRuleCall_2_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getOperationRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_2_0, 
                    		"ID");
            	    

            }


            }

            otherlv_3=(Token)match(input,17,FOLLOW_17_in_ruleOperation755); 

                	newLeafNode(otherlv_3, grammarAccess.getOperationAccess().getLeftParenthesisKeyword_3());
                
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:366:1: ( ( (lv_arg_4_0= ruleARG ) ) (otherlv_5= ',' ( (lv_arg_6_0= ruleARG ) ) )* )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==RULE_ID) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:366:2: ( (lv_arg_4_0= ruleARG ) ) (otherlv_5= ',' ( (lv_arg_6_0= ruleARG ) ) )*
                    {
                    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:366:2: ( (lv_arg_4_0= ruleARG ) )
                    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:367:1: (lv_arg_4_0= ruleARG )
                    {
                    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:367:1: (lv_arg_4_0= ruleARG )
                    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:368:3: lv_arg_4_0= ruleARG
                    {
                     
                    	        newCompositeNode(grammarAccess.getOperationAccess().getArgARGParserRuleCall_4_0_0()); 
                    	    
                    pushFollow(FOLLOW_ruleARG_in_ruleOperation777);
                    lv_arg_4_0=ruleARG();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getOperationRule());
                    	        }
                           		add(
                           			current, 
                           			"arg",
                            		lv_arg_4_0, 
                            		"ARG");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }

                    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:384:2: (otherlv_5= ',' ( (lv_arg_6_0= ruleARG ) ) )*
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( (LA5_0==18) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:384:4: otherlv_5= ',' ( (lv_arg_6_0= ruleARG ) )
                    	    {
                    	    otherlv_5=(Token)match(input,18,FOLLOW_18_in_ruleOperation790); 

                    	        	newLeafNode(otherlv_5, grammarAccess.getOperationAccess().getCommaKeyword_4_1_0());
                    	        
                    	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:388:1: ( (lv_arg_6_0= ruleARG ) )
                    	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:389:1: (lv_arg_6_0= ruleARG )
                    	    {
                    	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:389:1: (lv_arg_6_0= ruleARG )
                    	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:390:3: lv_arg_6_0= ruleARG
                    	    {
                    	     
                    	    	        newCompositeNode(grammarAccess.getOperationAccess().getArgARGParserRuleCall_4_1_1_0()); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleARG_in_ruleOperation811);
                    	    lv_arg_6_0=ruleARG();

                    	    state._fsp--;


                    	    	        if (current==null) {
                    	    	            current = createModelElementForParent(grammarAccess.getOperationRule());
                    	    	        }
                    	           		add(
                    	           			current, 
                    	           			"arg",
                    	            		lv_arg_6_0, 
                    	            		"ARG");
                    	    	        afterParserOrEnumRuleCall();
                    	    	    

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop5;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_7=(Token)match(input,19,FOLLOW_19_in_ruleOperation827); 

                	newLeafNode(otherlv_7, grammarAccess.getOperationAccess().getRightParenthesisKeyword_5());
                
            this_FIRST_LEVEL_LBRACKET_8=(Token)match(input,RULE_FIRST_LEVEL_LBRACKET,FOLLOW_RULE_FIRST_LEVEL_LBRACKET_in_ruleOperation838); 
             
                newLeafNode(this_FIRST_LEVEL_LBRACKET_8, grammarAccess.getOperationAccess().getFIRST_LEVEL_LBRACKETTerminalRuleCall_6()); 
                
            otherlv_9=(Token)match(input,20,FOLLOW_20_in_ruleOperation849); 

                	newLeafNode(otherlv_9, grammarAccess.getOperationAccess().getIs_fsymKeyword_7());
                
             
                    newCompositeNode(grammarAccess.getOperationAccess().getParIDParserRuleCall_8()); 
                
            pushFollow(FOLLOW_ruleParID_in_ruleOperation865);
            ruleParID();

            state._fsp--;

             
                    afterParserOrEnumRuleCall();
                
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:426:1: ( (lv_fsym_11_0= ruleJavaBody ) )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:427:1: (lv_fsym_11_0= ruleJavaBody )
            {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:427:1: (lv_fsym_11_0= ruleJavaBody )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:428:3: lv_fsym_11_0= ruleJavaBody
            {
             
            	        newCompositeNode(grammarAccess.getOperationAccess().getFsymJavaBodyParserRuleCall_9_0()); 
            	    
            pushFollow(FOLLOW_ruleJavaBody_in_ruleOperation885);
            lv_fsym_11_0=ruleJavaBody();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getOperationRule());
            	        }
                   		set(
                   			current, 
                   			"fsym",
                    		lv_fsym_11_0, 
                    		"JavaBody");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:444:2: (otherlv_12= 'get_slot' ruleParIDList ( (lv_slot_14_0= ruleJavaBody ) ) )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==21) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:444:4: otherlv_12= 'get_slot' ruleParIDList ( (lv_slot_14_0= ruleJavaBody ) )
            	    {
            	    otherlv_12=(Token)match(input,21,FOLLOW_21_in_ruleOperation898); 

            	        	newLeafNode(otherlv_12, grammarAccess.getOperationAccess().getGet_slotKeyword_10_0());
            	        
            	     
            	            newCompositeNode(grammarAccess.getOperationAccess().getParIDListParserRuleCall_10_1()); 
            	        
            	    pushFollow(FOLLOW_ruleParIDList_in_ruleOperation914);
            	    ruleParIDList();

            	    state._fsp--;

            	     
            	            afterParserOrEnumRuleCall();
            	        
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:456:1: ( (lv_slot_14_0= ruleJavaBody ) )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:457:1: (lv_slot_14_0= ruleJavaBody )
            	    {
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:457:1: (lv_slot_14_0= ruleJavaBody )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:458:3: lv_slot_14_0= ruleJavaBody
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getOperationAccess().getSlotJavaBodyParserRuleCall_10_2_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleJavaBody_in_ruleOperation934);
            	    lv_slot_14_0=ruleJavaBody();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getOperationRule());
            	    	        }
            	           		add(
            	           			current, 
            	           			"slot",
            	            		lv_slot_14_0, 
            	            		"JavaBody");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);

            otherlv_15=(Token)match(input,22,FOLLOW_22_in_ruleOperation948); 

                	newLeafNode(otherlv_15, grammarAccess.getOperationAccess().getMakeKeyword_11());
                
             
                    newCompositeNode(grammarAccess.getOperationAccess().getParIDListParserRuleCall_12()); 
                
            pushFollow(FOLLOW_ruleParIDList_in_ruleOperation964);
            ruleParIDList();

            state._fsp--;

             
                    afterParserOrEnumRuleCall();
                
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:486:1: ( (lv_make_17_0= ruleJavaBody ) )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:487:1: (lv_make_17_0= ruleJavaBody )
            {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:487:1: (lv_make_17_0= ruleJavaBody )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:488:3: lv_make_17_0= ruleJavaBody
            {
             
            	        newCompositeNode(grammarAccess.getOperationAccess().getMakeJavaBodyParserRuleCall_13_0()); 
            	    
            pushFollow(FOLLOW_ruleJavaBody_in_ruleOperation984);
            lv_make_17_0=ruleJavaBody();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getOperationRule());
            	        }
                   		set(
                   			current, 
                   			"make",
                    		lv_make_17_0, 
                    		"JavaBody");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            this_FIRST_LEVEL_RBRACKET_18=(Token)match(input,RULE_FIRST_LEVEL_RBRACKET,FOLLOW_RULE_FIRST_LEVEL_RBRACKET_in_ruleOperation995); 
             
                newLeafNode(this_FIRST_LEVEL_RBRACKET_18, grammarAccess.getOperationAccess().getFIRST_LEVEL_RBRACKETTerminalRuleCall_14()); 
                

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
    // $ANTLR end "ruleOperation"


    // $ANTLR start "entryRuleOperationArray"
    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:516:1: entryRuleOperationArray returns [EObject current=null] : iv_ruleOperationArray= ruleOperationArray EOF ;
    public final EObject entryRuleOperationArray() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOperationArray = null;


        try {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:517:2: (iv_ruleOperationArray= ruleOperationArray EOF )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:518:2: iv_ruleOperationArray= ruleOperationArray EOF
            {
             newCompositeNode(grammarAccess.getOperationArrayRule()); 
            pushFollow(FOLLOW_ruleOperationArray_in_entryRuleOperationArray1030);
            iv_ruleOperationArray=ruleOperationArray();

            state._fsp--;

             current =iv_ruleOperationArray; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleOperationArray1040); 

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
    // $ANTLR end "entryRuleOperationArray"


    // $ANTLR start "ruleOperationArray"
    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:525:1: ruleOperationArray returns [EObject current=null] : (otherlv_0= '%oparray' ( (lv_term_1_0= RULE_ID ) ) ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '(' this_ID_4= RULE_ID otherlv_5= '*' otherlv_6= ')' this_FIRST_LEVEL_LBRACKET_7= RULE_FIRST_LEVEL_LBRACKET ( (otherlv_8= 'is_fsym' ruleParID ( (lv_fsym_10_0= ruleJavaBody ) ) ) | (otherlv_11= 'get_size' ruleParID ( (lv_size_13_0= ruleJavaBody ) ) ) | (otherlv_14= 'get_element' ruleParIDList ( (lv_element_16_0= ruleJavaBody ) ) ) | (otherlv_17= 'make_empty' ruleParID ( (lv_empty_19_0= ruleJavaBody ) ) ) | (otherlv_20= 'make_append' ruleParIDList ( (lv_append_22_0= ruleJavaBody ) ) ) )+ this_FIRST_LEVEL_RBRACKET_23= RULE_FIRST_LEVEL_RBRACKET ) ;
    public final EObject ruleOperationArray() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_term_1_0=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token this_ID_4=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        Token this_FIRST_LEVEL_LBRACKET_7=null;
        Token otherlv_8=null;
        Token otherlv_11=null;
        Token otherlv_14=null;
        Token otherlv_17=null;
        Token otherlv_20=null;
        Token this_FIRST_LEVEL_RBRACKET_23=null;
        EObject lv_fsym_10_0 = null;

        EObject lv_size_13_0 = null;

        EObject lv_element_16_0 = null;

        EObject lv_empty_19_0 = null;

        EObject lv_append_22_0 = null;


         enterRule(); 
            
        try {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:528:28: ( (otherlv_0= '%oparray' ( (lv_term_1_0= RULE_ID ) ) ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '(' this_ID_4= RULE_ID otherlv_5= '*' otherlv_6= ')' this_FIRST_LEVEL_LBRACKET_7= RULE_FIRST_LEVEL_LBRACKET ( (otherlv_8= 'is_fsym' ruleParID ( (lv_fsym_10_0= ruleJavaBody ) ) ) | (otherlv_11= 'get_size' ruleParID ( (lv_size_13_0= ruleJavaBody ) ) ) | (otherlv_14= 'get_element' ruleParIDList ( (lv_element_16_0= ruleJavaBody ) ) ) | (otherlv_17= 'make_empty' ruleParID ( (lv_empty_19_0= ruleJavaBody ) ) ) | (otherlv_20= 'make_append' ruleParIDList ( (lv_append_22_0= ruleJavaBody ) ) ) )+ this_FIRST_LEVEL_RBRACKET_23= RULE_FIRST_LEVEL_RBRACKET ) )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:529:1: (otherlv_0= '%oparray' ( (lv_term_1_0= RULE_ID ) ) ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '(' this_ID_4= RULE_ID otherlv_5= '*' otherlv_6= ')' this_FIRST_LEVEL_LBRACKET_7= RULE_FIRST_LEVEL_LBRACKET ( (otherlv_8= 'is_fsym' ruleParID ( (lv_fsym_10_0= ruleJavaBody ) ) ) | (otherlv_11= 'get_size' ruleParID ( (lv_size_13_0= ruleJavaBody ) ) ) | (otherlv_14= 'get_element' ruleParIDList ( (lv_element_16_0= ruleJavaBody ) ) ) | (otherlv_17= 'make_empty' ruleParID ( (lv_empty_19_0= ruleJavaBody ) ) ) | (otherlv_20= 'make_append' ruleParIDList ( (lv_append_22_0= ruleJavaBody ) ) ) )+ this_FIRST_LEVEL_RBRACKET_23= RULE_FIRST_LEVEL_RBRACKET )
            {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:529:1: (otherlv_0= '%oparray' ( (lv_term_1_0= RULE_ID ) ) ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '(' this_ID_4= RULE_ID otherlv_5= '*' otherlv_6= ')' this_FIRST_LEVEL_LBRACKET_7= RULE_FIRST_LEVEL_LBRACKET ( (otherlv_8= 'is_fsym' ruleParID ( (lv_fsym_10_0= ruleJavaBody ) ) ) | (otherlv_11= 'get_size' ruleParID ( (lv_size_13_0= ruleJavaBody ) ) ) | (otherlv_14= 'get_element' ruleParIDList ( (lv_element_16_0= ruleJavaBody ) ) ) | (otherlv_17= 'make_empty' ruleParID ( (lv_empty_19_0= ruleJavaBody ) ) ) | (otherlv_20= 'make_append' ruleParIDList ( (lv_append_22_0= ruleJavaBody ) ) ) )+ this_FIRST_LEVEL_RBRACKET_23= RULE_FIRST_LEVEL_RBRACKET )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:529:3: otherlv_0= '%oparray' ( (lv_term_1_0= RULE_ID ) ) ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '(' this_ID_4= RULE_ID otherlv_5= '*' otherlv_6= ')' this_FIRST_LEVEL_LBRACKET_7= RULE_FIRST_LEVEL_LBRACKET ( (otherlv_8= 'is_fsym' ruleParID ( (lv_fsym_10_0= ruleJavaBody ) ) ) | (otherlv_11= 'get_size' ruleParID ( (lv_size_13_0= ruleJavaBody ) ) ) | (otherlv_14= 'get_element' ruleParIDList ( (lv_element_16_0= ruleJavaBody ) ) ) | (otherlv_17= 'make_empty' ruleParID ( (lv_empty_19_0= ruleJavaBody ) ) ) | (otherlv_20= 'make_append' ruleParIDList ( (lv_append_22_0= ruleJavaBody ) ) ) )+ this_FIRST_LEVEL_RBRACKET_23= RULE_FIRST_LEVEL_RBRACKET
            {
            otherlv_0=(Token)match(input,23,FOLLOW_23_in_ruleOperationArray1077); 

                	newLeafNode(otherlv_0, grammarAccess.getOperationArrayAccess().getOparrayKeyword_0());
                
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:533:1: ( (lv_term_1_0= RULE_ID ) )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:534:1: (lv_term_1_0= RULE_ID )
            {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:534:1: (lv_term_1_0= RULE_ID )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:535:3: lv_term_1_0= RULE_ID
            {
            lv_term_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleOperationArray1094); 

            			newLeafNode(lv_term_1_0, grammarAccess.getOperationArrayAccess().getTermIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getOperationArrayRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"term",
                    		lv_term_1_0, 
                    		"ID");
            	    

            }


            }

            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:551:2: ( (lv_name_2_0= RULE_ID ) )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:552:1: (lv_name_2_0= RULE_ID )
            {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:552:1: (lv_name_2_0= RULE_ID )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:553:3: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleOperationArray1116); 

            			newLeafNode(lv_name_2_0, grammarAccess.getOperationArrayAccess().getNameIDTerminalRuleCall_2_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getOperationArrayRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_2_0, 
                    		"ID");
            	    

            }


            }

            otherlv_3=(Token)match(input,17,FOLLOW_17_in_ruleOperationArray1133); 

                	newLeafNode(otherlv_3, grammarAccess.getOperationArrayAccess().getLeftParenthesisKeyword_3());
                
            this_ID_4=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleOperationArray1144); 
             
                newLeafNode(this_ID_4, grammarAccess.getOperationArrayAccess().getIDTerminalRuleCall_4()); 
                
            otherlv_5=(Token)match(input,24,FOLLOW_24_in_ruleOperationArray1155); 

                	newLeafNode(otherlv_5, grammarAccess.getOperationArrayAccess().getAsteriskKeyword_5());
                
            otherlv_6=(Token)match(input,19,FOLLOW_19_in_ruleOperationArray1167); 

                	newLeafNode(otherlv_6, grammarAccess.getOperationArrayAccess().getRightParenthesisKeyword_6());
                
            this_FIRST_LEVEL_LBRACKET_7=(Token)match(input,RULE_FIRST_LEVEL_LBRACKET,FOLLOW_RULE_FIRST_LEVEL_LBRACKET_in_ruleOperationArray1178); 
             
                newLeafNode(this_FIRST_LEVEL_LBRACKET_7, grammarAccess.getOperationArrayAccess().getFIRST_LEVEL_LBRACKETTerminalRuleCall_7()); 
                
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:589:1: ( (otherlv_8= 'is_fsym' ruleParID ( (lv_fsym_10_0= ruleJavaBody ) ) ) | (otherlv_11= 'get_size' ruleParID ( (lv_size_13_0= ruleJavaBody ) ) ) | (otherlv_14= 'get_element' ruleParIDList ( (lv_element_16_0= ruleJavaBody ) ) ) | (otherlv_17= 'make_empty' ruleParID ( (lv_empty_19_0= ruleJavaBody ) ) ) | (otherlv_20= 'make_append' ruleParIDList ( (lv_append_22_0= ruleJavaBody ) ) ) )+
            int cnt8=0;
            loop8:
            do {
                int alt8=6;
                switch ( input.LA(1) ) {
                case 20:
                    {
                    alt8=1;
                    }
                    break;
                case 25:
                    {
                    alt8=2;
                    }
                    break;
                case 26:
                    {
                    alt8=3;
                    }
                    break;
                case 27:
                    {
                    alt8=4;
                    }
                    break;
                case 28:
                    {
                    alt8=5;
                    }
                    break;

                }

                switch (alt8) {
            	case 1 :
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:589:2: (otherlv_8= 'is_fsym' ruleParID ( (lv_fsym_10_0= ruleJavaBody ) ) )
            	    {
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:589:2: (otherlv_8= 'is_fsym' ruleParID ( (lv_fsym_10_0= ruleJavaBody ) ) )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:589:4: otherlv_8= 'is_fsym' ruleParID ( (lv_fsym_10_0= ruleJavaBody ) )
            	    {
            	    otherlv_8=(Token)match(input,20,FOLLOW_20_in_ruleOperationArray1191); 

            	        	newLeafNode(otherlv_8, grammarAccess.getOperationArrayAccess().getIs_fsymKeyword_8_0_0());
            	        
            	     
            	            newCompositeNode(grammarAccess.getOperationArrayAccess().getParIDParserRuleCall_8_0_1()); 
            	        
            	    pushFollow(FOLLOW_ruleParID_in_ruleOperationArray1207);
            	    ruleParID();

            	    state._fsp--;

            	     
            	            afterParserOrEnumRuleCall();
            	        
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:601:1: ( (lv_fsym_10_0= ruleJavaBody ) )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:602:1: (lv_fsym_10_0= ruleJavaBody )
            	    {
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:602:1: (lv_fsym_10_0= ruleJavaBody )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:603:3: lv_fsym_10_0= ruleJavaBody
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getOperationArrayAccess().getFsymJavaBodyParserRuleCall_8_0_2_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleJavaBody_in_ruleOperationArray1227);
            	    lv_fsym_10_0=ruleJavaBody();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getOperationArrayRule());
            	    	        }
            	           		set(
            	           			current, 
            	           			"fsym",
            	            		lv_fsym_10_0, 
            	            		"JavaBody");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }


            	    }


            	    }
            	    break;
            	case 2 :
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:620:6: (otherlv_11= 'get_size' ruleParID ( (lv_size_13_0= ruleJavaBody ) ) )
            	    {
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:620:6: (otherlv_11= 'get_size' ruleParID ( (lv_size_13_0= ruleJavaBody ) ) )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:620:8: otherlv_11= 'get_size' ruleParID ( (lv_size_13_0= ruleJavaBody ) )
            	    {
            	    otherlv_11=(Token)match(input,25,FOLLOW_25_in_ruleOperationArray1247); 

            	        	newLeafNode(otherlv_11, grammarAccess.getOperationArrayAccess().getGet_sizeKeyword_8_1_0());
            	        
            	     
            	            newCompositeNode(grammarAccess.getOperationArrayAccess().getParIDParserRuleCall_8_1_1()); 
            	        
            	    pushFollow(FOLLOW_ruleParID_in_ruleOperationArray1263);
            	    ruleParID();

            	    state._fsp--;

            	     
            	            afterParserOrEnumRuleCall();
            	        
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:632:1: ( (lv_size_13_0= ruleJavaBody ) )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:633:1: (lv_size_13_0= ruleJavaBody )
            	    {
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:633:1: (lv_size_13_0= ruleJavaBody )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:634:3: lv_size_13_0= ruleJavaBody
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getOperationArrayAccess().getSizeJavaBodyParserRuleCall_8_1_2_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleJavaBody_in_ruleOperationArray1283);
            	    lv_size_13_0=ruleJavaBody();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getOperationArrayRule());
            	    	        }
            	           		set(
            	           			current, 
            	           			"size",
            	            		lv_size_13_0, 
            	            		"JavaBody");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }


            	    }


            	    }
            	    break;
            	case 3 :
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:651:6: (otherlv_14= 'get_element' ruleParIDList ( (lv_element_16_0= ruleJavaBody ) ) )
            	    {
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:651:6: (otherlv_14= 'get_element' ruleParIDList ( (lv_element_16_0= ruleJavaBody ) ) )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:651:8: otherlv_14= 'get_element' ruleParIDList ( (lv_element_16_0= ruleJavaBody ) )
            	    {
            	    otherlv_14=(Token)match(input,26,FOLLOW_26_in_ruleOperationArray1303); 

            	        	newLeafNode(otherlv_14, grammarAccess.getOperationArrayAccess().getGet_elementKeyword_8_2_0());
            	        
            	     
            	            newCompositeNode(grammarAccess.getOperationArrayAccess().getParIDListParserRuleCall_8_2_1()); 
            	        
            	    pushFollow(FOLLOW_ruleParIDList_in_ruleOperationArray1319);
            	    ruleParIDList();

            	    state._fsp--;

            	     
            	            afterParserOrEnumRuleCall();
            	        
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:663:1: ( (lv_element_16_0= ruleJavaBody ) )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:664:1: (lv_element_16_0= ruleJavaBody )
            	    {
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:664:1: (lv_element_16_0= ruleJavaBody )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:665:3: lv_element_16_0= ruleJavaBody
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getOperationArrayAccess().getElementJavaBodyParserRuleCall_8_2_2_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleJavaBody_in_ruleOperationArray1339);
            	    lv_element_16_0=ruleJavaBody();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getOperationArrayRule());
            	    	        }
            	           		set(
            	           			current, 
            	           			"element",
            	            		lv_element_16_0, 
            	            		"JavaBody");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }


            	    }


            	    }
            	    break;
            	case 4 :
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:682:6: (otherlv_17= 'make_empty' ruleParID ( (lv_empty_19_0= ruleJavaBody ) ) )
            	    {
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:682:6: (otherlv_17= 'make_empty' ruleParID ( (lv_empty_19_0= ruleJavaBody ) ) )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:682:8: otherlv_17= 'make_empty' ruleParID ( (lv_empty_19_0= ruleJavaBody ) )
            	    {
            	    otherlv_17=(Token)match(input,27,FOLLOW_27_in_ruleOperationArray1359); 

            	        	newLeafNode(otherlv_17, grammarAccess.getOperationArrayAccess().getMake_emptyKeyword_8_3_0());
            	        
            	     
            	            newCompositeNode(grammarAccess.getOperationArrayAccess().getParIDParserRuleCall_8_3_1()); 
            	        
            	    pushFollow(FOLLOW_ruleParID_in_ruleOperationArray1375);
            	    ruleParID();

            	    state._fsp--;

            	     
            	            afterParserOrEnumRuleCall();
            	        
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:694:1: ( (lv_empty_19_0= ruleJavaBody ) )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:695:1: (lv_empty_19_0= ruleJavaBody )
            	    {
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:695:1: (lv_empty_19_0= ruleJavaBody )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:696:3: lv_empty_19_0= ruleJavaBody
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getOperationArrayAccess().getEmptyJavaBodyParserRuleCall_8_3_2_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleJavaBody_in_ruleOperationArray1395);
            	    lv_empty_19_0=ruleJavaBody();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getOperationArrayRule());
            	    	        }
            	           		set(
            	           			current, 
            	           			"empty",
            	            		lv_empty_19_0, 
            	            		"JavaBody");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }


            	    }


            	    }
            	    break;
            	case 5 :
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:713:6: (otherlv_20= 'make_append' ruleParIDList ( (lv_append_22_0= ruleJavaBody ) ) )
            	    {
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:713:6: (otherlv_20= 'make_append' ruleParIDList ( (lv_append_22_0= ruleJavaBody ) ) )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:713:8: otherlv_20= 'make_append' ruleParIDList ( (lv_append_22_0= ruleJavaBody ) )
            	    {
            	    otherlv_20=(Token)match(input,28,FOLLOW_28_in_ruleOperationArray1415); 

            	        	newLeafNode(otherlv_20, grammarAccess.getOperationArrayAccess().getMake_appendKeyword_8_4_0());
            	        
            	     
            	            newCompositeNode(grammarAccess.getOperationArrayAccess().getParIDListParserRuleCall_8_4_1()); 
            	        
            	    pushFollow(FOLLOW_ruleParIDList_in_ruleOperationArray1431);
            	    ruleParIDList();

            	    state._fsp--;

            	     
            	            afterParserOrEnumRuleCall();
            	        
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:725:1: ( (lv_append_22_0= ruleJavaBody ) )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:726:1: (lv_append_22_0= ruleJavaBody )
            	    {
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:726:1: (lv_append_22_0= ruleJavaBody )
            	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:727:3: lv_append_22_0= ruleJavaBody
            	    {
            	     
            	    	        newCompositeNode(grammarAccess.getOperationArrayAccess().getAppendJavaBodyParserRuleCall_8_4_2_0()); 
            	    	    
            	    pushFollow(FOLLOW_ruleJavaBody_in_ruleOperationArray1451);
            	    lv_append_22_0=ruleJavaBody();

            	    state._fsp--;


            	    	        if (current==null) {
            	    	            current = createModelElementForParent(grammarAccess.getOperationArrayRule());
            	    	        }
            	           		set(
            	           			current, 
            	           			"append",
            	            		lv_append_22_0, 
            	            		"JavaBody");
            	    	        afterParserOrEnumRuleCall();
            	    	    

            	    }


            	    }


            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt8 >= 1 ) break loop8;
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);

            this_FIRST_LEVEL_RBRACKET_23=(Token)match(input,RULE_FIRST_LEVEL_RBRACKET,FOLLOW_RULE_FIRST_LEVEL_RBRACKET_in_ruleOperationArray1465); 
             
                newLeafNode(this_FIRST_LEVEL_RBRACKET_23, grammarAccess.getOperationArrayAccess().getFIRST_LEVEL_RBRACKETTerminalRuleCall_9()); 
                

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
    // $ANTLR end "ruleOperationArray"


    // $ANTLR start "entryRuleJavaBody"
    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:755:1: entryRuleJavaBody returns [EObject current=null] : iv_ruleJavaBody= ruleJavaBody EOF ;
    public final EObject entryRuleJavaBody() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleJavaBody = null;


        try {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:756:2: (iv_ruleJavaBody= ruleJavaBody EOF )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:757:2: iv_ruleJavaBody= ruleJavaBody EOF
            {
             newCompositeNode(grammarAccess.getJavaBodyRule()); 
            pushFollow(FOLLOW_ruleJavaBody_in_entryRuleJavaBody1500);
            iv_ruleJavaBody=ruleJavaBody();

            state._fsp--;

             current =iv_ruleJavaBody; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleJavaBody1510); 

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
    // $ANTLR end "entryRuleJavaBody"


    // $ANTLR start "ruleJavaBody"
    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:764:1: ruleJavaBody returns [EObject current=null] : ( (lv_body_0_0= RULE_BRCKTSTMT ) ) ;
    public final EObject ruleJavaBody() throws RecognitionException {
        EObject current = null;

        Token lv_body_0_0=null;

         enterRule(); 
            
        try {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:767:28: ( ( (lv_body_0_0= RULE_BRCKTSTMT ) ) )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:768:1: ( (lv_body_0_0= RULE_BRCKTSTMT ) )
            {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:768:1: ( (lv_body_0_0= RULE_BRCKTSTMT ) )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:769:1: (lv_body_0_0= RULE_BRCKTSTMT )
            {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:769:1: (lv_body_0_0= RULE_BRCKTSTMT )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:770:3: lv_body_0_0= RULE_BRCKTSTMT
            {
            lv_body_0_0=(Token)match(input,RULE_BRCKTSTMT,FOLLOW_RULE_BRCKTSTMT_in_ruleJavaBody1551); 

            			newLeafNode(lv_body_0_0, grammarAccess.getJavaBodyAccess().getBodyBRCKTSTMTTerminalRuleCall_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getJavaBodyRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"body",
                    		lv_body_0_0, 
                    		"BRCKTSTMT");
            	    

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
    // $ANTLR end "ruleJavaBody"


    // $ANTLR start "entryRuleParID"
    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:794:1: entryRuleParID returns [String current=null] : iv_ruleParID= ruleParID EOF ;
    public final String entryRuleParID() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleParID = null;


        try {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:795:2: (iv_ruleParID= ruleParID EOF )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:796:2: iv_ruleParID= ruleParID EOF
            {
             newCompositeNode(grammarAccess.getParIDRule()); 
            pushFollow(FOLLOW_ruleParID_in_entryRuleParID1592);
            iv_ruleParID=ruleParID();

            state._fsp--;

             current =iv_ruleParID.getText(); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleParID1603); 

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
    // $ANTLR end "entryRuleParID"


    // $ANTLR start "ruleParID"
    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:803:1: ruleParID returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '(' (this_ID_1= RULE_ID )? kw= ')' ) ;
    public final AntlrDatatypeRuleToken ruleParID() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;
        Token this_ID_1=null;

         enterRule(); 
            
        try {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:806:28: ( (kw= '(' (this_ID_1= RULE_ID )? kw= ')' ) )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:807:1: (kw= '(' (this_ID_1= RULE_ID )? kw= ')' )
            {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:807:1: (kw= '(' (this_ID_1= RULE_ID )? kw= ')' )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:808:2: kw= '(' (this_ID_1= RULE_ID )? kw= ')'
            {
            kw=(Token)match(input,17,FOLLOW_17_in_ruleParID1641); 

                    current.merge(kw);
                    newLeafNode(kw, grammarAccess.getParIDAccess().getLeftParenthesisKeyword_0()); 
                
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:813:1: (this_ID_1= RULE_ID )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==RULE_ID) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:813:6: this_ID_1= RULE_ID
                    {
                    this_ID_1=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleParID1657); 

                    		current.merge(this_ID_1);
                        
                     
                        newLeafNode(this_ID_1, grammarAccess.getParIDAccess().getIDTerminalRuleCall_1()); 
                        

                    }
                    break;

            }

            kw=(Token)match(input,19,FOLLOW_19_in_ruleParID1677); 

                    current.merge(kw);
                    newLeafNode(kw, grammarAccess.getParIDAccess().getRightParenthesisKeyword_2()); 
                

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
    // $ANTLR end "ruleParID"


    // $ANTLR start "entryRuleParIDList"
    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:834:1: entryRuleParIDList returns [String current=null] : iv_ruleParIDList= ruleParIDList EOF ;
    public final String entryRuleParIDList() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleParIDList = null;


        try {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:835:2: (iv_ruleParIDList= ruleParIDList EOF )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:836:2: iv_ruleParIDList= ruleParIDList EOF
            {
             newCompositeNode(grammarAccess.getParIDListRule()); 
            pushFollow(FOLLOW_ruleParIDList_in_entryRuleParIDList1718);
            iv_ruleParIDList=ruleParIDList();

            state._fsp--;

             current =iv_ruleParIDList.getText(); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleParIDList1729); 

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
    // $ANTLR end "entryRuleParIDList"


    // $ANTLR start "ruleParIDList"
    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:843:1: ruleParIDList returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '(' (this_ID_1= RULE_ID (kw= ',' this_ID_3= RULE_ID )* )? kw= ')' ) ;
    public final AntlrDatatypeRuleToken ruleParIDList() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;
        Token this_ID_1=null;
        Token this_ID_3=null;

         enterRule(); 
            
        try {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:846:28: ( (kw= '(' (this_ID_1= RULE_ID (kw= ',' this_ID_3= RULE_ID )* )? kw= ')' ) )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:847:1: (kw= '(' (this_ID_1= RULE_ID (kw= ',' this_ID_3= RULE_ID )* )? kw= ')' )
            {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:847:1: (kw= '(' (this_ID_1= RULE_ID (kw= ',' this_ID_3= RULE_ID )* )? kw= ')' )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:848:2: kw= '(' (this_ID_1= RULE_ID (kw= ',' this_ID_3= RULE_ID )* )? kw= ')'
            {
            kw=(Token)match(input,17,FOLLOW_17_in_ruleParIDList1767); 

                    current.merge(kw);
                    newLeafNode(kw, grammarAccess.getParIDListAccess().getLeftParenthesisKeyword_0()); 
                
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:853:1: (this_ID_1= RULE_ID (kw= ',' this_ID_3= RULE_ID )* )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==RULE_ID) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:853:6: this_ID_1= RULE_ID (kw= ',' this_ID_3= RULE_ID )*
                    {
                    this_ID_1=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleParIDList1783); 

                    		current.merge(this_ID_1);
                        
                     
                        newLeafNode(this_ID_1, grammarAccess.getParIDListAccess().getIDTerminalRuleCall_1_0()); 
                        
                    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:860:1: (kw= ',' this_ID_3= RULE_ID )*
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( (LA10_0==18) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:861:2: kw= ',' this_ID_3= RULE_ID
                    	    {
                    	    kw=(Token)match(input,18,FOLLOW_18_in_ruleParIDList1802); 

                    	            current.merge(kw);
                    	            newLeafNode(kw, grammarAccess.getParIDListAccess().getCommaKeyword_1_1_0()); 
                    	        
                    	    this_ID_3=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleParIDList1817); 

                    	    		current.merge(this_ID_3);
                    	        
                    	     
                    	        newLeafNode(this_ID_3, grammarAccess.getParIDListAccess().getIDTerminalRuleCall_1_1_1()); 
                    	        

                    	    }
                    	    break;

                    	default :
                    	    break loop10;
                        }
                    } while (true);


                    }
                    break;

            }

            kw=(Token)match(input,19,FOLLOW_19_in_ruleParIDList1839); 

                    current.merge(kw);
                    newLeafNode(kw, grammarAccess.getParIDListAccess().getRightParenthesisKeyword_2()); 
                

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
    // $ANTLR end "ruleParIDList"


    // $ANTLR start "entryRuleARG"
    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:887:1: entryRuleARG returns [EObject current=null] : iv_ruleARG= ruleARG EOF ;
    public final EObject entryRuleARG() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleARG = null;


        try {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:888:2: (iv_ruleARG= ruleARG EOF )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:889:2: iv_ruleARG= ruleARG EOF
            {
             newCompositeNode(grammarAccess.getARGRule()); 
            pushFollow(FOLLOW_ruleARG_in_entryRuleARG1879);
            iv_ruleARG=ruleARG();

            state._fsp--;

             current =iv_ruleARG; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleARG1889); 

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
    // $ANTLR end "entryRuleARG"


    // $ANTLR start "ruleARG"
    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:896:1: ruleARG returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= ':' ( (lv_type_2_0= RULE_ID ) ) )? ) ;
    public final EObject ruleARG() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token lv_type_2_0=null;

         enterRule(); 
            
        try {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:899:28: ( ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= ':' ( (lv_type_2_0= RULE_ID ) ) )? ) )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:900:1: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= ':' ( (lv_type_2_0= RULE_ID ) ) )? )
            {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:900:1: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= ':' ( (lv_type_2_0= RULE_ID ) ) )? )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:900:2: ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= ':' ( (lv_type_2_0= RULE_ID ) ) )?
            {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:900:2: ( (lv_name_0_0= RULE_ID ) )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:901:1: (lv_name_0_0= RULE_ID )
            {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:901:1: (lv_name_0_0= RULE_ID )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:902:3: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleARG1931); 

            			newLeafNode(lv_name_0_0, grammarAccess.getARGAccess().getNameIDTerminalRuleCall_0_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getARGRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_0_0, 
                    		"ID");
            	    

            }


            }

            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:918:2: (otherlv_1= ':' ( (lv_type_2_0= RULE_ID ) ) )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==29) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:918:4: otherlv_1= ':' ( (lv_type_2_0= RULE_ID ) )
                    {
                    otherlv_1=(Token)match(input,29,FOLLOW_29_in_ruleARG1949); 

                        	newLeafNode(otherlv_1, grammarAccess.getARGAccess().getColonKeyword_1_0());
                        
                    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:922:1: ( (lv_type_2_0= RULE_ID ) )
                    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:923:1: (lv_type_2_0= RULE_ID )
                    {
                    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:923:1: (lv_type_2_0= RULE_ID )
                    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:924:3: lv_type_2_0= RULE_ID
                    {
                    lv_type_2_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleARG1966); 

                    			newLeafNode(lv_type_2_0, grammarAccess.getARGAccess().getTypeIDTerminalRuleCall_1_1_0()); 
                    		

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getARGRule());
                    	        }
                           		setWithLastConsumed(
                           			current, 
                           			"type",
                            		lv_type_2_0, 
                            		"ID");
                    	    

                    }


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
    // $ANTLR end "ruleARG"


    // $ANTLR start "entryRuleTypeTerm"
    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:948:1: entryRuleTypeTerm returns [EObject current=null] : iv_ruleTypeTerm= ruleTypeTerm EOF ;
    public final EObject entryRuleTypeTerm() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTypeTerm = null;


        try {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:949:2: (iv_ruleTypeTerm= ruleTypeTerm EOF )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:950:2: iv_ruleTypeTerm= ruleTypeTerm EOF
            {
             newCompositeNode(grammarAccess.getTypeTermRule()); 
            pushFollow(FOLLOW_ruleTypeTerm_in_entryRuleTypeTerm2009);
            iv_ruleTypeTerm=ruleTypeTerm();

            state._fsp--;

             current =iv_ruleTypeTerm; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleTypeTerm2019); 

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
    // $ANTLR end "entryRuleTypeTerm"


    // $ANTLR start "ruleTypeTerm"
    // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:957:1: ruleTypeTerm returns [EObject current=null] : (otherlv_0= '%typeterm' ( (lv_name_1_0= RULE_ID ) ) this_FIRST_LEVEL_LBRACKET_2= RULE_FIRST_LEVEL_LBRACKET otherlv_3= 'implement' ( (lv_implement_4_0= ruleJavaBody ) ) otherlv_5= 'is_sort' ruleParID ( (lv_sort_7_0= ruleJavaBody ) ) otherlv_8= 'equals' otherlv_9= '(' this_ID_10= RULE_ID otherlv_11= ',' this_ID_12= RULE_ID otherlv_13= ')' ( (lv_equals_14_0= ruleJavaBody ) ) this_FIRST_LEVEL_RBRACKET_15= RULE_FIRST_LEVEL_RBRACKET ) ;
    public final EObject ruleTypeTerm() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token this_FIRST_LEVEL_LBRACKET_2=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_8=null;
        Token otherlv_9=null;
        Token this_ID_10=null;
        Token otherlv_11=null;
        Token this_ID_12=null;
        Token otherlv_13=null;
        Token this_FIRST_LEVEL_RBRACKET_15=null;
        EObject lv_implement_4_0 = null;

        EObject lv_sort_7_0 = null;

        EObject lv_equals_14_0 = null;


         enterRule(); 
            
        try {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:960:28: ( (otherlv_0= '%typeterm' ( (lv_name_1_0= RULE_ID ) ) this_FIRST_LEVEL_LBRACKET_2= RULE_FIRST_LEVEL_LBRACKET otherlv_3= 'implement' ( (lv_implement_4_0= ruleJavaBody ) ) otherlv_5= 'is_sort' ruleParID ( (lv_sort_7_0= ruleJavaBody ) ) otherlv_8= 'equals' otherlv_9= '(' this_ID_10= RULE_ID otherlv_11= ',' this_ID_12= RULE_ID otherlv_13= ')' ( (lv_equals_14_0= ruleJavaBody ) ) this_FIRST_LEVEL_RBRACKET_15= RULE_FIRST_LEVEL_RBRACKET ) )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:961:1: (otherlv_0= '%typeterm' ( (lv_name_1_0= RULE_ID ) ) this_FIRST_LEVEL_LBRACKET_2= RULE_FIRST_LEVEL_LBRACKET otherlv_3= 'implement' ( (lv_implement_4_0= ruleJavaBody ) ) otherlv_5= 'is_sort' ruleParID ( (lv_sort_7_0= ruleJavaBody ) ) otherlv_8= 'equals' otherlv_9= '(' this_ID_10= RULE_ID otherlv_11= ',' this_ID_12= RULE_ID otherlv_13= ')' ( (lv_equals_14_0= ruleJavaBody ) ) this_FIRST_LEVEL_RBRACKET_15= RULE_FIRST_LEVEL_RBRACKET )
            {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:961:1: (otherlv_0= '%typeterm' ( (lv_name_1_0= RULE_ID ) ) this_FIRST_LEVEL_LBRACKET_2= RULE_FIRST_LEVEL_LBRACKET otherlv_3= 'implement' ( (lv_implement_4_0= ruleJavaBody ) ) otherlv_5= 'is_sort' ruleParID ( (lv_sort_7_0= ruleJavaBody ) ) otherlv_8= 'equals' otherlv_9= '(' this_ID_10= RULE_ID otherlv_11= ',' this_ID_12= RULE_ID otherlv_13= ')' ( (lv_equals_14_0= ruleJavaBody ) ) this_FIRST_LEVEL_RBRACKET_15= RULE_FIRST_LEVEL_RBRACKET )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:961:3: otherlv_0= '%typeterm' ( (lv_name_1_0= RULE_ID ) ) this_FIRST_LEVEL_LBRACKET_2= RULE_FIRST_LEVEL_LBRACKET otherlv_3= 'implement' ( (lv_implement_4_0= ruleJavaBody ) ) otherlv_5= 'is_sort' ruleParID ( (lv_sort_7_0= ruleJavaBody ) ) otherlv_8= 'equals' otherlv_9= '(' this_ID_10= RULE_ID otherlv_11= ',' this_ID_12= RULE_ID otherlv_13= ')' ( (lv_equals_14_0= ruleJavaBody ) ) this_FIRST_LEVEL_RBRACKET_15= RULE_FIRST_LEVEL_RBRACKET
            {
            otherlv_0=(Token)match(input,30,FOLLOW_30_in_ruleTypeTerm2056); 

                	newLeafNode(otherlv_0, grammarAccess.getTypeTermAccess().getTypetermKeyword_0());
                
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:965:1: ( (lv_name_1_0= RULE_ID ) )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:966:1: (lv_name_1_0= RULE_ID )
            {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:966:1: (lv_name_1_0= RULE_ID )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:967:3: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleTypeTerm2073); 

            			newLeafNode(lv_name_1_0, grammarAccess.getTypeTermAccess().getNameIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getTypeTermRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_1_0, 
                    		"ID");
            	    

            }


            }

            this_FIRST_LEVEL_LBRACKET_2=(Token)match(input,RULE_FIRST_LEVEL_LBRACKET,FOLLOW_RULE_FIRST_LEVEL_LBRACKET_in_ruleTypeTerm2089); 
             
                newLeafNode(this_FIRST_LEVEL_LBRACKET_2, grammarAccess.getTypeTermAccess().getFIRST_LEVEL_LBRACKETTerminalRuleCall_2()); 
                
            otherlv_3=(Token)match(input,31,FOLLOW_31_in_ruleTypeTerm2100); 

                	newLeafNode(otherlv_3, grammarAccess.getTypeTermAccess().getImplementKeyword_3());
                
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:991:1: ( (lv_implement_4_0= ruleJavaBody ) )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:992:1: (lv_implement_4_0= ruleJavaBody )
            {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:992:1: (lv_implement_4_0= ruleJavaBody )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:993:3: lv_implement_4_0= ruleJavaBody
            {
             
            	        newCompositeNode(grammarAccess.getTypeTermAccess().getImplementJavaBodyParserRuleCall_4_0()); 
            	    
            pushFollow(FOLLOW_ruleJavaBody_in_ruleTypeTerm2121);
            lv_implement_4_0=ruleJavaBody();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getTypeTermRule());
            	        }
                   		set(
                   			current, 
                   			"implement",
                    		lv_implement_4_0, 
                    		"JavaBody");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            otherlv_5=(Token)match(input,32,FOLLOW_32_in_ruleTypeTerm2133); 

                	newLeafNode(otherlv_5, grammarAccess.getTypeTermAccess().getIs_sortKeyword_5());
                
             
                    newCompositeNode(grammarAccess.getTypeTermAccess().getParIDParserRuleCall_6()); 
                
            pushFollow(FOLLOW_ruleParID_in_ruleTypeTerm2149);
            ruleParID();

            state._fsp--;

             
                    afterParserOrEnumRuleCall();
                
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:1021:1: ( (lv_sort_7_0= ruleJavaBody ) )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:1022:1: (lv_sort_7_0= ruleJavaBody )
            {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:1022:1: (lv_sort_7_0= ruleJavaBody )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:1023:3: lv_sort_7_0= ruleJavaBody
            {
             
            	        newCompositeNode(grammarAccess.getTypeTermAccess().getSortJavaBodyParserRuleCall_7_0()); 
            	    
            pushFollow(FOLLOW_ruleJavaBody_in_ruleTypeTerm2169);
            lv_sort_7_0=ruleJavaBody();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getTypeTermRule());
            	        }
                   		set(
                   			current, 
                   			"sort",
                    		lv_sort_7_0, 
                    		"JavaBody");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            otherlv_8=(Token)match(input,33,FOLLOW_33_in_ruleTypeTerm2181); 

                	newLeafNode(otherlv_8, grammarAccess.getTypeTermAccess().getEqualsKeyword_8());
                
            otherlv_9=(Token)match(input,17,FOLLOW_17_in_ruleTypeTerm2193); 

                	newLeafNode(otherlv_9, grammarAccess.getTypeTermAccess().getLeftParenthesisKeyword_9());
                
            this_ID_10=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleTypeTerm2204); 
             
                newLeafNode(this_ID_10, grammarAccess.getTypeTermAccess().getIDTerminalRuleCall_10()); 
                
            otherlv_11=(Token)match(input,18,FOLLOW_18_in_ruleTypeTerm2215); 

                	newLeafNode(otherlv_11, grammarAccess.getTypeTermAccess().getCommaKeyword_11());
                
            this_ID_12=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleTypeTerm2226); 
             
                newLeafNode(this_ID_12, grammarAccess.getTypeTermAccess().getIDTerminalRuleCall_12()); 
                
            otherlv_13=(Token)match(input,19,FOLLOW_19_in_ruleTypeTerm2237); 

                	newLeafNode(otherlv_13, grammarAccess.getTypeTermAccess().getRightParenthesisKeyword_13());
                
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:1063:1: ( (lv_equals_14_0= ruleJavaBody ) )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:1064:1: (lv_equals_14_0= ruleJavaBody )
            {
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:1064:1: (lv_equals_14_0= ruleJavaBody )
            // ../tom.dsl/src-gen/tom/parser/antlr/internal/InternalTomDsl.g:1065:3: lv_equals_14_0= ruleJavaBody
            {
             
            	        newCompositeNode(grammarAccess.getTypeTermAccess().getEqualsJavaBodyParserRuleCall_14_0()); 
            	    
            pushFollow(FOLLOW_ruleJavaBody_in_ruleTypeTerm2258);
            lv_equals_14_0=ruleJavaBody();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getTypeTermRule());
            	        }
                   		set(
                   			current, 
                   			"equals",
                    		lv_equals_14_0, 
                    		"JavaBody");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            this_FIRST_LEVEL_RBRACKET_15=(Token)match(input,RULE_FIRST_LEVEL_RBRACKET,FOLLOW_RULE_FIRST_LEVEL_RBRACKET_in_ruleTypeTerm2269); 
             
                newLeafNode(this_FIRST_LEVEL_RBRACKET_15, grammarAccess.getTypeTermAccess().getFIRST_LEVEL_RBRACKETTerminalRuleCall_15()); 
                

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
    // $ANTLR end "ruleTypeTerm"

    // Delegated rules


 

    public static final BitSet FOLLOW_ruleTomFile_in_entryRuleTomFile75 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleTomFile85 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleArrayOperation_in_ruleTomFile131 = new BitSet(new long[]{0x0000000040812082L});
    public static final BitSet FOLLOW_ruleTypeTerm_in_ruleTomFile158 = new BitSet(new long[]{0x0000000040812082L});
    public static final BitSet FOLLOW_ruleInclude_in_ruleTomFile185 = new BitSet(new long[]{0x0000000040812082L});
    public static final BitSet FOLLOW_ruleLocal_in_ruleTomFile212 = new BitSet(new long[]{0x0000000040812082L});
    public static final BitSet FOLLOW_ruleArrayOperation_in_entryRuleArrayOperation249 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleArrayOperation259 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperation_in_ruleArrayOperation306 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperationArray_in_ruleArrayOperation333 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleInclude_in_entryRuleInclude368 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleInclude378 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_ruleInclude415 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_FIRST_LEVEL_LBRACKET_in_ruleInclude426 = new BitSet(new long[]{0x000000000000C020L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleInclude444 = new BitSet(new long[]{0x000000000000C060L});
    public static final BitSet FOLLOW_14_in_ruleInclude465 = new BitSet(new long[]{0x000000000000C060L});
    public static final BitSet FOLLOW_15_in_ruleInclude494 = new BitSet(new long[]{0x000000000000C060L});
    public static final BitSet FOLLOW_RULE_FIRST_LEVEL_RBRACKET_in_ruleInclude522 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleLocal_in_entryRuleLocal558 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleLocal569 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_JAVAMETHOD_in_ruleLocal608 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperation_in_entryRuleOperation652 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleOperation662 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_ruleOperation699 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleOperation716 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleOperation738 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_ruleOperation755 = new BitSet(new long[]{0x0000000000080020L});
    public static final BitSet FOLLOW_ruleARG_in_ruleOperation777 = new BitSet(new long[]{0x00000000000C0000L});
    public static final BitSet FOLLOW_18_in_ruleOperation790 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_ruleARG_in_ruleOperation811 = new BitSet(new long[]{0x00000000000C0000L});
    public static final BitSet FOLLOW_19_in_ruleOperation827 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_FIRST_LEVEL_LBRACKET_in_ruleOperation838 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_ruleOperation849 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_ruleParID_in_ruleOperation865 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ruleJavaBody_in_ruleOperation885 = new BitSet(new long[]{0x0000000000600000L});
    public static final BitSet FOLLOW_21_in_ruleOperation898 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_ruleParIDList_in_ruleOperation914 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ruleJavaBody_in_ruleOperation934 = new BitSet(new long[]{0x0000000000600000L});
    public static final BitSet FOLLOW_22_in_ruleOperation948 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_ruleParIDList_in_ruleOperation964 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ruleJavaBody_in_ruleOperation984 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_RULE_FIRST_LEVEL_RBRACKET_in_ruleOperation995 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperationArray_in_entryRuleOperationArray1030 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleOperationArray1040 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_ruleOperationArray1077 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleOperationArray1094 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleOperationArray1116 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_ruleOperationArray1133 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleOperationArray1144 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_ruleOperationArray1155 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_ruleOperationArray1167 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_FIRST_LEVEL_LBRACKET_in_ruleOperationArray1178 = new BitSet(new long[]{0x000000001E100000L});
    public static final BitSet FOLLOW_20_in_ruleOperationArray1191 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_ruleParID_in_ruleOperationArray1207 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ruleJavaBody_in_ruleOperationArray1227 = new BitSet(new long[]{0x000000001E100040L});
    public static final BitSet FOLLOW_25_in_ruleOperationArray1247 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_ruleParID_in_ruleOperationArray1263 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ruleJavaBody_in_ruleOperationArray1283 = new BitSet(new long[]{0x000000001E100040L});
    public static final BitSet FOLLOW_26_in_ruleOperationArray1303 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_ruleParIDList_in_ruleOperationArray1319 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ruleJavaBody_in_ruleOperationArray1339 = new BitSet(new long[]{0x000000001E100040L});
    public static final BitSet FOLLOW_27_in_ruleOperationArray1359 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_ruleParID_in_ruleOperationArray1375 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ruleJavaBody_in_ruleOperationArray1395 = new BitSet(new long[]{0x000000001E100040L});
    public static final BitSet FOLLOW_28_in_ruleOperationArray1415 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_ruleParIDList_in_ruleOperationArray1431 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ruleJavaBody_in_ruleOperationArray1451 = new BitSet(new long[]{0x000000001E100040L});
    public static final BitSet FOLLOW_RULE_FIRST_LEVEL_RBRACKET_in_ruleOperationArray1465 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleJavaBody_in_entryRuleJavaBody1500 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleJavaBody1510 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_BRCKTSTMT_in_ruleJavaBody1551 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleParID_in_entryRuleParID1592 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleParID1603 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_ruleParID1641 = new BitSet(new long[]{0x0000000000080020L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleParID1657 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_ruleParID1677 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleParIDList_in_entryRuleParIDList1718 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleParIDList1729 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_ruleParIDList1767 = new BitSet(new long[]{0x0000000000080020L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleParIDList1783 = new BitSet(new long[]{0x00000000000C0000L});
    public static final BitSet FOLLOW_18_in_ruleParIDList1802 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleParIDList1817 = new BitSet(new long[]{0x00000000000C0000L});
    public static final BitSet FOLLOW_19_in_ruleParIDList1839 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleARG_in_entryRuleARG1879 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleARG1889 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleARG1931 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_29_in_ruleARG1949 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleARG1966 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleTypeTerm_in_entryRuleTypeTerm2009 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleTypeTerm2019 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_ruleTypeTerm2056 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleTypeTerm2073 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_FIRST_LEVEL_LBRACKET_in_ruleTypeTerm2089 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_31_in_ruleTypeTerm2100 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ruleJavaBody_in_ruleTypeTerm2121 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_32_in_ruleTypeTerm2133 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_ruleParID_in_ruleTypeTerm2149 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ruleJavaBody_in_ruleTypeTerm2169 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_ruleTypeTerm2181 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_ruleTypeTerm2193 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleTypeTerm2204 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_ruleTypeTerm2215 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleTypeTerm2226 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_ruleTypeTerm2237 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ruleJavaBody_in_ruleTypeTerm2258 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_RULE_FIRST_LEVEL_RBRACKET_in_ruleTypeTerm2269 = new BitSet(new long[]{0x0000000000000002L});

}
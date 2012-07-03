package tom.mapping.dsl.ui.contentassist.antlr.internal; 

import java.io.InputStream;
import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.AbstractInternalContentAssistParser;
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.DFA;
import tom.mapping.dsl.services.TomMappingGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalTomMappingParser extends AbstractInternalContentAssistParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_STRING", "RULE_ID", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'TomMapping'", "';'", "'prefix'", "'import'", "'terminals'", "'{'", "'}'", "'define'", "','", "'use'", "'operators'", "'module'", "':'", "'alias'", "'::'", "'('", "')'", "'op'", "'make'", "'='", "'is_fsym'", "'slot'", "'ignore'", "'.'", "'[]'"
    };
    public static final int RULE_ID=5;
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
    public static final int RULE_STRING=4;
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
    public String getGrammarFileName() { return "../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g"; }


     
     	private TomMappingGrammarAccess grammarAccess;
     	
        public void setGrammarAccess(TomMappingGrammarAccess grammarAccess) {
        	this.grammarAccess = grammarAccess;
        }
        
        @Override
        protected Grammar getGrammar() {
        	return grammarAccess.getGrammar();
        }
        
        @Override
        protected String getValueForTokenName(String tokenName) {
        	return tokenName;
        }




    // $ANTLR start "entryRuleMapping"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:60:1: entryRuleMapping : ruleMapping EOF ;
    public final void entryRuleMapping() throws RecognitionException {
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:61:1: ( ruleMapping EOF )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:62:1: ruleMapping EOF
            {
             before(grammarAccess.getMappingRule()); 
            pushFollow(FOLLOW_ruleMapping_in_entryRuleMapping61);
            ruleMapping();

            state._fsp--;

             after(grammarAccess.getMappingRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleMapping68); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleMapping"


    // $ANTLR start "ruleMapping"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:69:1: ruleMapping : ( ( rule__Mapping__Group__0 ) ) ;
    public final void ruleMapping() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:73:2: ( ( ( rule__Mapping__Group__0 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:74:1: ( ( rule__Mapping__Group__0 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:74:1: ( ( rule__Mapping__Group__0 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:75:1: ( rule__Mapping__Group__0 )
            {
             before(grammarAccess.getMappingAccess().getGroup()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:76:1: ( rule__Mapping__Group__0 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:76:2: rule__Mapping__Group__0
            {
            pushFollow(FOLLOW_rule__Mapping__Group__0_in_ruleMapping94);
            rule__Mapping__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getMappingAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleMapping"


    // $ANTLR start "entryRuleModule"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:88:1: entryRuleModule : ruleModule EOF ;
    public final void entryRuleModule() throws RecognitionException {
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:89:1: ( ruleModule EOF )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:90:1: ruleModule EOF
            {
             before(grammarAccess.getModuleRule()); 
            pushFollow(FOLLOW_ruleModule_in_entryRuleModule121);
            ruleModule();

            state._fsp--;

             after(grammarAccess.getModuleRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleModule128); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleModule"


    // $ANTLR start "ruleModule"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:97:1: ruleModule : ( ( rule__Module__Group__0 ) ) ;
    public final void ruleModule() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:101:2: ( ( ( rule__Module__Group__0 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:102:1: ( ( rule__Module__Group__0 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:102:1: ( ( rule__Module__Group__0 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:103:1: ( rule__Module__Group__0 )
            {
             before(grammarAccess.getModuleAccess().getGroup()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:104:1: ( rule__Module__Group__0 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:104:2: rule__Module__Group__0
            {
            pushFollow(FOLLOW_rule__Module__Group__0_in_ruleModule154);
            rule__Module__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getModuleAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleModule"


    // $ANTLR start "entryRuleOperator"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:116:1: entryRuleOperator : ruleOperator EOF ;
    public final void entryRuleOperator() throws RecognitionException {
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:117:1: ( ruleOperator EOF )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:118:1: ruleOperator EOF
            {
             before(grammarAccess.getOperatorRule()); 
            pushFollow(FOLLOW_ruleOperator_in_entryRuleOperator181);
            ruleOperator();

            state._fsp--;

             after(grammarAccess.getOperatorRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleOperator188); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleOperator"


    // $ANTLR start "ruleOperator"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:125:1: ruleOperator : ( ( rule__Operator__Alternatives ) ) ;
    public final void ruleOperator() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:129:2: ( ( ( rule__Operator__Alternatives ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:130:1: ( ( rule__Operator__Alternatives ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:130:1: ( ( rule__Operator__Alternatives ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:131:1: ( rule__Operator__Alternatives )
            {
             before(grammarAccess.getOperatorAccess().getAlternatives()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:132:1: ( rule__Operator__Alternatives )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:132:2: rule__Operator__Alternatives
            {
            pushFollow(FOLLOW_rule__Operator__Alternatives_in_ruleOperator214);
            rule__Operator__Alternatives();

            state._fsp--;


            }

             after(grammarAccess.getOperatorAccess().getAlternatives()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleOperator"


    // $ANTLR start "entryRuleImport"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:144:1: entryRuleImport : ruleImport EOF ;
    public final void entryRuleImport() throws RecognitionException {
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:145:1: ( ruleImport EOF )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:146:1: ruleImport EOF
            {
             before(grammarAccess.getImportRule()); 
            pushFollow(FOLLOW_ruleImport_in_entryRuleImport241);
            ruleImport();

            state._fsp--;

             after(grammarAccess.getImportRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleImport248); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleImport"


    // $ANTLR start "ruleImport"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:153:1: ruleImport : ( ( rule__Import__ImportURIAssignment ) ) ;
    public final void ruleImport() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:157:2: ( ( ( rule__Import__ImportURIAssignment ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:158:1: ( ( rule__Import__ImportURIAssignment ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:158:1: ( ( rule__Import__ImportURIAssignment ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:159:1: ( rule__Import__ImportURIAssignment )
            {
             before(grammarAccess.getImportAccess().getImportURIAssignment()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:160:1: ( rule__Import__ImportURIAssignment )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:160:2: rule__Import__ImportURIAssignment
            {
            pushFollow(FOLLOW_rule__Import__ImportURIAssignment_in_ruleImport274);
            rule__Import__ImportURIAssignment();

            state._fsp--;


            }

             after(grammarAccess.getImportAccess().getImportURIAssignment()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleImport"


    // $ANTLR start "entryRuleTerminal"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:172:1: entryRuleTerminal : ruleTerminal EOF ;
    public final void entryRuleTerminal() throws RecognitionException {
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:173:1: ( ruleTerminal EOF )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:174:1: ruleTerminal EOF
            {
             before(grammarAccess.getTerminalRule()); 
            pushFollow(FOLLOW_ruleTerminal_in_entryRuleTerminal301);
            ruleTerminal();

            state._fsp--;

             after(grammarAccess.getTerminalRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleTerminal308); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleTerminal"


    // $ANTLR start "ruleTerminal"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:181:1: ruleTerminal : ( ( rule__Terminal__Group__0 ) ) ;
    public final void ruleTerminal() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:185:2: ( ( ( rule__Terminal__Group__0 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:186:1: ( ( rule__Terminal__Group__0 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:186:1: ( ( rule__Terminal__Group__0 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:187:1: ( rule__Terminal__Group__0 )
            {
             before(grammarAccess.getTerminalAccess().getGroup()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:188:1: ( rule__Terminal__Group__0 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:188:2: rule__Terminal__Group__0
            {
            pushFollow(FOLLOW_rule__Terminal__Group__0_in_ruleTerminal334);
            rule__Terminal__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getTerminalAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleTerminal"


    // $ANTLR start "entryRuleAliasOperator"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:200:1: entryRuleAliasOperator : ruleAliasOperator EOF ;
    public final void entryRuleAliasOperator() throws RecognitionException {
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:201:1: ( ruleAliasOperator EOF )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:202:1: ruleAliasOperator EOF
            {
             before(grammarAccess.getAliasOperatorRule()); 
            pushFollow(FOLLOW_ruleAliasOperator_in_entryRuleAliasOperator361);
            ruleAliasOperator();

            state._fsp--;

             after(grammarAccess.getAliasOperatorRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleAliasOperator368); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleAliasOperator"


    // $ANTLR start "ruleAliasOperator"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:209:1: ruleAliasOperator : ( ( rule__AliasOperator__Group__0 ) ) ;
    public final void ruleAliasOperator() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:213:2: ( ( ( rule__AliasOperator__Group__0 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:214:1: ( ( rule__AliasOperator__Group__0 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:214:1: ( ( rule__AliasOperator__Group__0 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:215:1: ( rule__AliasOperator__Group__0 )
            {
             before(grammarAccess.getAliasOperatorAccess().getGroup()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:216:1: ( rule__AliasOperator__Group__0 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:216:2: rule__AliasOperator__Group__0
            {
            pushFollow(FOLLOW_rule__AliasOperator__Group__0_in_ruleAliasOperator394);
            rule__AliasOperator__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getAliasOperatorAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleAliasOperator"


    // $ANTLR start "entryRuleAliasNode"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:228:1: entryRuleAliasNode : ruleAliasNode EOF ;
    public final void entryRuleAliasNode() throws RecognitionException {
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:229:1: ( ruleAliasNode EOF )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:230:1: ruleAliasNode EOF
            {
             before(grammarAccess.getAliasNodeRule()); 
            pushFollow(FOLLOW_ruleAliasNode_in_entryRuleAliasNode421);
            ruleAliasNode();

            state._fsp--;

             after(grammarAccess.getAliasNodeRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleAliasNode428); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleAliasNode"


    // $ANTLR start "ruleAliasNode"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:237:1: ruleAliasNode : ( ( rule__AliasNode__Alternatives ) ) ;
    public final void ruleAliasNode() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:241:2: ( ( ( rule__AliasNode__Alternatives ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:242:1: ( ( rule__AliasNode__Alternatives ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:242:1: ( ( rule__AliasNode__Alternatives ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:243:1: ( rule__AliasNode__Alternatives )
            {
             before(grammarAccess.getAliasNodeAccess().getAlternatives()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:244:1: ( rule__AliasNode__Alternatives )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:244:2: rule__AliasNode__Alternatives
            {
            pushFollow(FOLLOW_rule__AliasNode__Alternatives_in_ruleAliasNode454);
            rule__AliasNode__Alternatives();

            state._fsp--;


            }

             after(grammarAccess.getAliasNodeAccess().getAlternatives()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleAliasNode"


    // $ANTLR start "entryRuleFeatureNode"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:256:1: entryRuleFeatureNode : ruleFeatureNode EOF ;
    public final void entryRuleFeatureNode() throws RecognitionException {
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:257:1: ( ruleFeatureNode EOF )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:258:1: ruleFeatureNode EOF
            {
             before(grammarAccess.getFeatureNodeRule()); 
            pushFollow(FOLLOW_ruleFeatureNode_in_entryRuleFeatureNode481);
            ruleFeatureNode();

            state._fsp--;

             after(grammarAccess.getFeatureNodeRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleFeatureNode488); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleFeatureNode"


    // $ANTLR start "ruleFeatureNode"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:265:1: ruleFeatureNode : ( ( rule__FeatureNode__FeatureAssignment ) ) ;
    public final void ruleFeatureNode() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:269:2: ( ( ( rule__FeatureNode__FeatureAssignment ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:270:1: ( ( rule__FeatureNode__FeatureAssignment ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:270:1: ( ( rule__FeatureNode__FeatureAssignment ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:271:1: ( rule__FeatureNode__FeatureAssignment )
            {
             before(grammarAccess.getFeatureNodeAccess().getFeatureAssignment()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:272:1: ( rule__FeatureNode__FeatureAssignment )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:272:2: rule__FeatureNode__FeatureAssignment
            {
            pushFollow(FOLLOW_rule__FeatureNode__FeatureAssignment_in_ruleFeatureNode514);
            rule__FeatureNode__FeatureAssignment();

            state._fsp--;


            }

             after(grammarAccess.getFeatureNodeAccess().getFeatureAssignment()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleFeatureNode"


    // $ANTLR start "entryRuleOperatorNode"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:284:1: entryRuleOperatorNode : ruleOperatorNode EOF ;
    public final void entryRuleOperatorNode() throws RecognitionException {
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:285:1: ( ruleOperatorNode EOF )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:286:1: ruleOperatorNode EOF
            {
             before(grammarAccess.getOperatorNodeRule()); 
            pushFollow(FOLLOW_ruleOperatorNode_in_entryRuleOperatorNode541);
            ruleOperatorNode();

            state._fsp--;

             after(grammarAccess.getOperatorNodeRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleOperatorNode548); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleOperatorNode"


    // $ANTLR start "ruleOperatorNode"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:293:1: ruleOperatorNode : ( ( rule__OperatorNode__Group__0 ) ) ;
    public final void ruleOperatorNode() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:297:2: ( ( ( rule__OperatorNode__Group__0 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:298:1: ( ( rule__OperatorNode__Group__0 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:298:1: ( ( rule__OperatorNode__Group__0 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:299:1: ( rule__OperatorNode__Group__0 )
            {
             before(grammarAccess.getOperatorNodeAccess().getGroup()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:300:1: ( rule__OperatorNode__Group__0 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:300:2: rule__OperatorNode__Group__0
            {
            pushFollow(FOLLOW_rule__OperatorNode__Group__0_in_ruleOperatorNode574);
            rule__OperatorNode__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getOperatorNodeAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleOperatorNode"


    // $ANTLR start "entryRuleClassOperator"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:312:1: entryRuleClassOperator : ruleClassOperator EOF ;
    public final void entryRuleClassOperator() throws RecognitionException {
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:313:1: ( ruleClassOperator EOF )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:314:1: ruleClassOperator EOF
            {
             before(grammarAccess.getClassOperatorRule()); 
            pushFollow(FOLLOW_ruleClassOperator_in_entryRuleClassOperator601);
            ruleClassOperator();

            state._fsp--;

             after(grammarAccess.getClassOperatorRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleClassOperator608); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleClassOperator"


    // $ANTLR start "ruleClassOperator"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:321:1: ruleClassOperator : ( ( rule__ClassOperator__Group__0 ) ) ;
    public final void ruleClassOperator() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:325:2: ( ( ( rule__ClassOperator__Group__0 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:326:1: ( ( rule__ClassOperator__Group__0 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:326:1: ( ( rule__ClassOperator__Group__0 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:327:1: ( rule__ClassOperator__Group__0 )
            {
             before(grammarAccess.getClassOperatorAccess().getGroup()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:328:1: ( rule__ClassOperator__Group__0 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:328:2: rule__ClassOperator__Group__0
            {
            pushFollow(FOLLOW_rule__ClassOperator__Group__0_in_ruleClassOperator634);
            rule__ClassOperator__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getClassOperatorAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleClassOperator"


    // $ANTLR start "entryRuleClassOperatorWithExceptions"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:340:1: entryRuleClassOperatorWithExceptions : ruleClassOperatorWithExceptions EOF ;
    public final void entryRuleClassOperatorWithExceptions() throws RecognitionException {
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:341:1: ( ruleClassOperatorWithExceptions EOF )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:342:1: ruleClassOperatorWithExceptions EOF
            {
             before(grammarAccess.getClassOperatorWithExceptionsRule()); 
            pushFollow(FOLLOW_ruleClassOperatorWithExceptions_in_entryRuleClassOperatorWithExceptions661);
            ruleClassOperatorWithExceptions();

            state._fsp--;

             after(grammarAccess.getClassOperatorWithExceptionsRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleClassOperatorWithExceptions668); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleClassOperatorWithExceptions"


    // $ANTLR start "ruleClassOperatorWithExceptions"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:349:1: ruleClassOperatorWithExceptions : ( ( rule__ClassOperatorWithExceptions__Group__0 ) ) ;
    public final void ruleClassOperatorWithExceptions() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:353:2: ( ( ( rule__ClassOperatorWithExceptions__Group__0 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:354:1: ( ( rule__ClassOperatorWithExceptions__Group__0 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:354:1: ( ( rule__ClassOperatorWithExceptions__Group__0 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:355:1: ( rule__ClassOperatorWithExceptions__Group__0 )
            {
             before(grammarAccess.getClassOperatorWithExceptionsAccess().getGroup()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:356:1: ( rule__ClassOperatorWithExceptions__Group__0 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:356:2: rule__ClassOperatorWithExceptions__Group__0
            {
            pushFollow(FOLLOW_rule__ClassOperatorWithExceptions__Group__0_in_ruleClassOperatorWithExceptions694);
            rule__ClassOperatorWithExceptions__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getClassOperatorWithExceptionsAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleClassOperatorWithExceptions"


    // $ANTLR start "entryRuleUserOperator"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:368:1: entryRuleUserOperator : ruleUserOperator EOF ;
    public final void entryRuleUserOperator() throws RecognitionException {
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:369:1: ( ruleUserOperator EOF )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:370:1: ruleUserOperator EOF
            {
             before(grammarAccess.getUserOperatorRule()); 
            pushFollow(FOLLOW_ruleUserOperator_in_entryRuleUserOperator721);
            ruleUserOperator();

            state._fsp--;

             after(grammarAccess.getUserOperatorRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleUserOperator728); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleUserOperator"


    // $ANTLR start "ruleUserOperator"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:377:1: ruleUserOperator : ( ( rule__UserOperator__Group__0 ) ) ;
    public final void ruleUserOperator() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:381:2: ( ( ( rule__UserOperator__Group__0 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:382:1: ( ( rule__UserOperator__Group__0 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:382:1: ( ( rule__UserOperator__Group__0 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:383:1: ( rule__UserOperator__Group__0 )
            {
             before(grammarAccess.getUserOperatorAccess().getGroup()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:384:1: ( rule__UserOperator__Group__0 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:384:2: rule__UserOperator__Group__0
            {
            pushFollow(FOLLOW_rule__UserOperator__Group__0_in_ruleUserOperator754);
            rule__UserOperator__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getUserOperatorAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleUserOperator"


    // $ANTLR start "entryRuleParameter"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:396:1: entryRuleParameter : ruleParameter EOF ;
    public final void entryRuleParameter() throws RecognitionException {
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:397:1: ( ruleParameter EOF )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:398:1: ruleParameter EOF
            {
             before(grammarAccess.getParameterRule()); 
            pushFollow(FOLLOW_ruleParameter_in_entryRuleParameter781);
            ruleParameter();

            state._fsp--;

             after(grammarAccess.getParameterRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleParameter788); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleParameter"


    // $ANTLR start "ruleParameter"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:405:1: ruleParameter : ( ( rule__Parameter__Group__0 ) ) ;
    public final void ruleParameter() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:409:2: ( ( ( rule__Parameter__Group__0 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:410:1: ( ( rule__Parameter__Group__0 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:410:1: ( ( rule__Parameter__Group__0 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:411:1: ( rule__Parameter__Group__0 )
            {
             before(grammarAccess.getParameterAccess().getGroup()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:412:1: ( rule__Parameter__Group__0 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:412:2: rule__Parameter__Group__0
            {
            pushFollow(FOLLOW_rule__Parameter__Group__0_in_ruleParameter814);
            rule__Parameter__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getParameterAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleParameter"


    // $ANTLR start "entryRuleAccessor"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:424:1: entryRuleAccessor : ruleAccessor EOF ;
    public final void entryRuleAccessor() throws RecognitionException {
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:425:1: ( ruleAccessor EOF )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:426:1: ruleAccessor EOF
            {
             before(grammarAccess.getAccessorRule()); 
            pushFollow(FOLLOW_ruleAccessor_in_entryRuleAccessor841);
            ruleAccessor();

            state._fsp--;

             after(grammarAccess.getAccessorRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleAccessor848); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleAccessor"


    // $ANTLR start "ruleAccessor"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:433:1: ruleAccessor : ( ( rule__Accessor__Group__0 ) ) ;
    public final void ruleAccessor() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:437:2: ( ( ( rule__Accessor__Group__0 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:438:1: ( ( rule__Accessor__Group__0 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:438:1: ( ( rule__Accessor__Group__0 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:439:1: ( rule__Accessor__Group__0 )
            {
             before(grammarAccess.getAccessorAccess().getGroup()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:440:1: ( rule__Accessor__Group__0 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:440:2: rule__Accessor__Group__0
            {
            pushFollow(FOLLOW_rule__Accessor__Group__0_in_ruleAccessor874);
            rule__Accessor__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getAccessorAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleAccessor"


    // $ANTLR start "entryRuleFeatureException"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:452:1: entryRuleFeatureException : ruleFeatureException EOF ;
    public final void entryRuleFeatureException() throws RecognitionException {
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:453:1: ( ruleFeatureException EOF )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:454:1: ruleFeatureException EOF
            {
             before(grammarAccess.getFeatureExceptionRule()); 
            pushFollow(FOLLOW_ruleFeatureException_in_entryRuleFeatureException901);
            ruleFeatureException();

            state._fsp--;

             after(grammarAccess.getFeatureExceptionRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleFeatureException908); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleFeatureException"


    // $ANTLR start "ruleFeatureException"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:461:1: ruleFeatureException : ( ( rule__FeatureException__Group__0 ) ) ;
    public final void ruleFeatureException() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:465:2: ( ( ( rule__FeatureException__Group__0 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:466:1: ( ( rule__FeatureException__Group__0 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:466:1: ( ( rule__FeatureException__Group__0 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:467:1: ( rule__FeatureException__Group__0 )
            {
             before(grammarAccess.getFeatureExceptionAccess().getGroup()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:468:1: ( rule__FeatureException__Group__0 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:468:2: rule__FeatureException__Group__0
            {
            pushFollow(FOLLOW_rule__FeatureException__Group__0_in_ruleFeatureException934);
            rule__FeatureException__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getFeatureExceptionAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleFeatureException"


    // $ANTLR start "entryRuleFeatureParameter"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:480:1: entryRuleFeatureParameter : ruleFeatureParameter EOF ;
    public final void entryRuleFeatureParameter() throws RecognitionException {
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:481:1: ( ruleFeatureParameter EOF )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:482:1: ruleFeatureParameter EOF
            {
             before(grammarAccess.getFeatureParameterRule()); 
            pushFollow(FOLLOW_ruleFeatureParameter_in_entryRuleFeatureParameter961);
            ruleFeatureParameter();

            state._fsp--;

             after(grammarAccess.getFeatureParameterRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleFeatureParameter968); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleFeatureParameter"


    // $ANTLR start "ruleFeatureParameter"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:489:1: ruleFeatureParameter : ( ( rule__FeatureParameter__Alternatives ) ) ;
    public final void ruleFeatureParameter() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:493:2: ( ( ( rule__FeatureParameter__Alternatives ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:494:1: ( ( rule__FeatureParameter__Alternatives ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:494:1: ( ( rule__FeatureParameter__Alternatives ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:495:1: ( rule__FeatureParameter__Alternatives )
            {
             before(grammarAccess.getFeatureParameterAccess().getAlternatives()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:496:1: ( rule__FeatureParameter__Alternatives )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:496:2: rule__FeatureParameter__Alternatives
            {
            pushFollow(FOLLOW_rule__FeatureParameter__Alternatives_in_ruleFeatureParameter994);
            rule__FeatureParameter__Alternatives();

            state._fsp--;


            }

             after(grammarAccess.getFeatureParameterAccess().getAlternatives()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleFeatureParameter"


    // $ANTLR start "entryRuleSettedFeatureParameter"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:508:1: entryRuleSettedFeatureParameter : ruleSettedFeatureParameter EOF ;
    public final void entryRuleSettedFeatureParameter() throws RecognitionException {
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:509:1: ( ruleSettedFeatureParameter EOF )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:510:1: ruleSettedFeatureParameter EOF
            {
             before(grammarAccess.getSettedFeatureParameterRule()); 
            pushFollow(FOLLOW_ruleSettedFeatureParameter_in_entryRuleSettedFeatureParameter1021);
            ruleSettedFeatureParameter();

            state._fsp--;

             after(grammarAccess.getSettedFeatureParameterRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSettedFeatureParameter1028); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleSettedFeatureParameter"


    // $ANTLR start "ruleSettedFeatureParameter"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:517:1: ruleSettedFeatureParameter : ( ( rule__SettedFeatureParameter__Group__0 ) ) ;
    public final void ruleSettedFeatureParameter() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:521:2: ( ( ( rule__SettedFeatureParameter__Group__0 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:522:1: ( ( rule__SettedFeatureParameter__Group__0 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:522:1: ( ( rule__SettedFeatureParameter__Group__0 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:523:1: ( rule__SettedFeatureParameter__Group__0 )
            {
             before(grammarAccess.getSettedFeatureParameterAccess().getGroup()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:524:1: ( rule__SettedFeatureParameter__Group__0 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:524:2: rule__SettedFeatureParameter__Group__0
            {
            pushFollow(FOLLOW_rule__SettedFeatureParameter__Group__0_in_ruleSettedFeatureParameter1054);
            rule__SettedFeatureParameter__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getSettedFeatureParameterAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleSettedFeatureParameter"


    // $ANTLR start "entryRuleSettedValue"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:536:1: entryRuleSettedValue : ruleSettedValue EOF ;
    public final void entryRuleSettedValue() throws RecognitionException {
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:537:1: ( ruleSettedValue EOF )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:538:1: ruleSettedValue EOF
            {
             before(grammarAccess.getSettedValueRule()); 
            pushFollow(FOLLOW_ruleSettedValue_in_entryRuleSettedValue1081);
            ruleSettedValue();

            state._fsp--;

             after(grammarAccess.getSettedValueRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSettedValue1088); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleSettedValue"


    // $ANTLR start "ruleSettedValue"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:545:1: ruleSettedValue : ( ( rule__SettedValue__Alternatives ) ) ;
    public final void ruleSettedValue() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:549:2: ( ( ( rule__SettedValue__Alternatives ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:550:1: ( ( rule__SettedValue__Alternatives ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:550:1: ( ( rule__SettedValue__Alternatives ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:551:1: ( rule__SettedValue__Alternatives )
            {
             before(grammarAccess.getSettedValueAccess().getAlternatives()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:552:1: ( rule__SettedValue__Alternatives )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:552:2: rule__SettedValue__Alternatives
            {
            pushFollow(FOLLOW_rule__SettedValue__Alternatives_in_ruleSettedValue1114);
            rule__SettedValue__Alternatives();

            state._fsp--;


            }

             after(grammarAccess.getSettedValueAccess().getAlternatives()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleSettedValue"


    // $ANTLR start "entryRuleJavaCodeValue"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:564:1: entryRuleJavaCodeValue : ruleJavaCodeValue EOF ;
    public final void entryRuleJavaCodeValue() throws RecognitionException {
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:565:1: ( ruleJavaCodeValue EOF )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:566:1: ruleJavaCodeValue EOF
            {
             before(grammarAccess.getJavaCodeValueRule()); 
            pushFollow(FOLLOW_ruleJavaCodeValue_in_entryRuleJavaCodeValue1141);
            ruleJavaCodeValue();

            state._fsp--;

             after(grammarAccess.getJavaCodeValueRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleJavaCodeValue1148); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleJavaCodeValue"


    // $ANTLR start "ruleJavaCodeValue"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:573:1: ruleJavaCodeValue : ( ( rule__JavaCodeValue__JavaAssignment ) ) ;
    public final void ruleJavaCodeValue() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:577:2: ( ( ( rule__JavaCodeValue__JavaAssignment ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:578:1: ( ( rule__JavaCodeValue__JavaAssignment ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:578:1: ( ( rule__JavaCodeValue__JavaAssignment ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:579:1: ( rule__JavaCodeValue__JavaAssignment )
            {
             before(grammarAccess.getJavaCodeValueAccess().getJavaAssignment()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:580:1: ( rule__JavaCodeValue__JavaAssignment )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:580:2: rule__JavaCodeValue__JavaAssignment
            {
            pushFollow(FOLLOW_rule__JavaCodeValue__JavaAssignment_in_ruleJavaCodeValue1174);
            rule__JavaCodeValue__JavaAssignment();

            state._fsp--;


            }

             after(grammarAccess.getJavaCodeValueAccess().getJavaAssignment()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleJavaCodeValue"


    // $ANTLR start "entryRuleLiteralValue"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:592:1: entryRuleLiteralValue : ruleLiteralValue EOF ;
    public final void entryRuleLiteralValue() throws RecognitionException {
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:593:1: ( ruleLiteralValue EOF )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:594:1: ruleLiteralValue EOF
            {
             before(grammarAccess.getLiteralValueRule()); 
            pushFollow(FOLLOW_ruleLiteralValue_in_entryRuleLiteralValue1201);
            ruleLiteralValue();

            state._fsp--;

             after(grammarAccess.getLiteralValueRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleLiteralValue1208); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleLiteralValue"


    // $ANTLR start "ruleLiteralValue"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:601:1: ruleLiteralValue : ( ( rule__LiteralValue__LiteralAssignment ) ) ;
    public final void ruleLiteralValue() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:605:2: ( ( ( rule__LiteralValue__LiteralAssignment ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:606:1: ( ( rule__LiteralValue__LiteralAssignment ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:606:1: ( ( rule__LiteralValue__LiteralAssignment ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:607:1: ( rule__LiteralValue__LiteralAssignment )
            {
             before(grammarAccess.getLiteralValueAccess().getLiteralAssignment()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:608:1: ( rule__LiteralValue__LiteralAssignment )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:608:2: rule__LiteralValue__LiteralAssignment
            {
            pushFollow(FOLLOW_rule__LiteralValue__LiteralAssignment_in_ruleLiteralValue1234);
            rule__LiteralValue__LiteralAssignment();

            state._fsp--;


            }

             after(grammarAccess.getLiteralValueAccess().getLiteralAssignment()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleLiteralValue"


    // $ANTLR start "entryRuleEString"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:620:1: entryRuleEString : ruleEString EOF ;
    public final void entryRuleEString() throws RecognitionException {
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:621:1: ( ruleEString EOF )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:622:1: ruleEString EOF
            {
             before(grammarAccess.getEStringRule()); 
            pushFollow(FOLLOW_ruleEString_in_entryRuleEString1261);
            ruleEString();

            state._fsp--;

             after(grammarAccess.getEStringRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleEString1268); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleEString"


    // $ANTLR start "ruleEString"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:629:1: ruleEString : ( ( rule__EString__Alternatives ) ) ;
    public final void ruleEString() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:633:2: ( ( ( rule__EString__Alternatives ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:634:1: ( ( rule__EString__Alternatives ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:634:1: ( ( rule__EString__Alternatives ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:635:1: ( rule__EString__Alternatives )
            {
             before(grammarAccess.getEStringAccess().getAlternatives()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:636:1: ( rule__EString__Alternatives )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:636:2: rule__EString__Alternatives
            {
            pushFollow(FOLLOW_rule__EString__Alternatives_in_ruleEString1294);
            rule__EString__Alternatives();

            state._fsp--;


            }

             after(grammarAccess.getEStringAccess().getAlternatives()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleEString"


    // $ANTLR start "entryRuleFQN"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:652:1: entryRuleFQN : ruleFQN EOF ;
    public final void entryRuleFQN() throws RecognitionException {
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:653:1: ( ruleFQN EOF )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:654:1: ruleFQN EOF
            {
             before(grammarAccess.getFQNRule()); 
            pushFollow(FOLLOW_ruleFQN_in_entryRuleFQN1325);
            ruleFQN();

            state._fsp--;

             after(grammarAccess.getFQNRule()); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleFQN1332); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleFQN"


    // $ANTLR start "ruleFQN"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:661:1: ruleFQN : ( ( rule__FQN__Group__0 ) ) ;
    public final void ruleFQN() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:665:2: ( ( ( rule__FQN__Group__0 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:666:1: ( ( rule__FQN__Group__0 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:666:1: ( ( rule__FQN__Group__0 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:667:1: ( rule__FQN__Group__0 )
            {
             before(grammarAccess.getFQNAccess().getGroup()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:668:1: ( rule__FQN__Group__0 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:668:2: rule__FQN__Group__0
            {
            pushFollow(FOLLOW_rule__FQN__Group__0_in_ruleFQN1358);
            rule__FQN__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getFQNAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleFQN"


    // $ANTLR start "rule__Mapping__Alternatives_5_2"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:680:1: rule__Mapping__Alternatives_5_2 : ( ( ( rule__Mapping__Group_5_2_0__0 ) ) | ( ( rule__Mapping__Group_5_2_1__0 ) ) );
    public final void rule__Mapping__Alternatives_5_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:684:1: ( ( ( rule__Mapping__Group_5_2_0__0 ) ) | ( ( rule__Mapping__Group_5_2_1__0 ) ) )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( ((LA1_0>=17 && LA1_0<=18)||LA1_0==20) ) {
                alt1=1;
            }
            else if ( (LA1_0==RULE_ID) ) {
                alt1=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:685:1: ( ( rule__Mapping__Group_5_2_0__0 ) )
                    {
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:685:1: ( ( rule__Mapping__Group_5_2_0__0 ) )
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:686:1: ( rule__Mapping__Group_5_2_0__0 )
                    {
                     before(grammarAccess.getMappingAccess().getGroup_5_2_0()); 
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:687:1: ( rule__Mapping__Group_5_2_0__0 )
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:687:2: rule__Mapping__Group_5_2_0__0
                    {
                    pushFollow(FOLLOW_rule__Mapping__Group_5_2_0__0_in_rule__Mapping__Alternatives_5_21394);
                    rule__Mapping__Group_5_2_0__0();

                    state._fsp--;


                    }

                     after(grammarAccess.getMappingAccess().getGroup_5_2_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:691:6: ( ( rule__Mapping__Group_5_2_1__0 ) )
                    {
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:691:6: ( ( rule__Mapping__Group_5_2_1__0 ) )
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:692:1: ( rule__Mapping__Group_5_2_1__0 )
                    {
                     before(grammarAccess.getMappingAccess().getGroup_5_2_1()); 
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:693:1: ( rule__Mapping__Group_5_2_1__0 )
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:693:2: rule__Mapping__Group_5_2_1__0
                    {
                    pushFollow(FOLLOW_rule__Mapping__Group_5_2_1__0_in_rule__Mapping__Alternatives_5_21412);
                    rule__Mapping__Group_5_2_1__0();

                    state._fsp--;


                    }

                     after(grammarAccess.getMappingAccess().getGroup_5_2_1()); 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Alternatives_5_2"


    // $ANTLR start "rule__Mapping__Alternatives_6"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:702:1: rule__Mapping__Alternatives_6 : ( ( ( rule__Mapping__Group_6_0__0 ) ) | ( ( rule__Mapping__ModulesAssignment_6_1 ) ) );
    public final void rule__Mapping__Alternatives_6() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:706:1: ( ( ( rule__Mapping__Group_6_0__0 ) ) | ( ( rule__Mapping__ModulesAssignment_6_1 ) ) )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==21) ) {
                alt2=1;
            }
            else if ( (LA2_0==22) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:707:1: ( ( rule__Mapping__Group_6_0__0 ) )
                    {
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:707:1: ( ( rule__Mapping__Group_6_0__0 ) )
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:708:1: ( rule__Mapping__Group_6_0__0 )
                    {
                     before(grammarAccess.getMappingAccess().getGroup_6_0()); 
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:709:1: ( rule__Mapping__Group_6_0__0 )
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:709:2: rule__Mapping__Group_6_0__0
                    {
                    pushFollow(FOLLOW_rule__Mapping__Group_6_0__0_in_rule__Mapping__Alternatives_61445);
                    rule__Mapping__Group_6_0__0();

                    state._fsp--;


                    }

                     after(grammarAccess.getMappingAccess().getGroup_6_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:713:6: ( ( rule__Mapping__ModulesAssignment_6_1 ) )
                    {
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:713:6: ( ( rule__Mapping__ModulesAssignment_6_1 ) )
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:714:1: ( rule__Mapping__ModulesAssignment_6_1 )
                    {
                     before(grammarAccess.getMappingAccess().getModulesAssignment_6_1()); 
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:715:1: ( rule__Mapping__ModulesAssignment_6_1 )
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:715:2: rule__Mapping__ModulesAssignment_6_1
                    {
                    pushFollow(FOLLOW_rule__Mapping__ModulesAssignment_6_1_in_rule__Mapping__Alternatives_61463);
                    rule__Mapping__ModulesAssignment_6_1();

                    state._fsp--;


                    }

                     after(grammarAccess.getMappingAccess().getModulesAssignment_6_1()); 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Alternatives_6"


    // $ANTLR start "rule__Operator__Alternatives"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:724:1: rule__Operator__Alternatives : ( ( ruleClassOperator ) | ( ruleClassOperatorWithExceptions ) | ( ruleUserOperator ) | ( ruleAliasOperator ) );
    public final void rule__Operator__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:728:1: ( ( ruleClassOperator ) | ( ruleClassOperatorWithExceptions ) | ( ruleUserOperator ) | ( ruleAliasOperator ) )
            int alt3=4;
            alt3 = dfa3.predict(input);
            switch (alt3) {
                case 1 :
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:729:1: ( ruleClassOperator )
                    {
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:729:1: ( ruleClassOperator )
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:730:1: ruleClassOperator
                    {
                     before(grammarAccess.getOperatorAccess().getClassOperatorParserRuleCall_0()); 
                    pushFollow(FOLLOW_ruleClassOperator_in_rule__Operator__Alternatives1496);
                    ruleClassOperator();

                    state._fsp--;

                     after(grammarAccess.getOperatorAccess().getClassOperatorParserRuleCall_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:735:6: ( ruleClassOperatorWithExceptions )
                    {
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:735:6: ( ruleClassOperatorWithExceptions )
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:736:1: ruleClassOperatorWithExceptions
                    {
                     before(grammarAccess.getOperatorAccess().getClassOperatorWithExceptionsParserRuleCall_1()); 
                    pushFollow(FOLLOW_ruleClassOperatorWithExceptions_in_rule__Operator__Alternatives1513);
                    ruleClassOperatorWithExceptions();

                    state._fsp--;

                     after(grammarAccess.getOperatorAccess().getClassOperatorWithExceptionsParserRuleCall_1()); 

                    }


                    }
                    break;
                case 3 :
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:741:6: ( ruleUserOperator )
                    {
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:741:6: ( ruleUserOperator )
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:742:1: ruleUserOperator
                    {
                     before(grammarAccess.getOperatorAccess().getUserOperatorParserRuleCall_2()); 
                    pushFollow(FOLLOW_ruleUserOperator_in_rule__Operator__Alternatives1530);
                    ruleUserOperator();

                    state._fsp--;

                     after(grammarAccess.getOperatorAccess().getUserOperatorParserRuleCall_2()); 

                    }


                    }
                    break;
                case 4 :
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:747:6: ( ruleAliasOperator )
                    {
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:747:6: ( ruleAliasOperator )
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:748:1: ruleAliasOperator
                    {
                     before(grammarAccess.getOperatorAccess().getAliasOperatorParserRuleCall_3()); 
                    pushFollow(FOLLOW_ruleAliasOperator_in_rule__Operator__Alternatives1547);
                    ruleAliasOperator();

                    state._fsp--;

                     after(grammarAccess.getOperatorAccess().getAliasOperatorParserRuleCall_3()); 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Operator__Alternatives"


    // $ANTLR start "rule__AliasNode__Alternatives"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:758:1: rule__AliasNode__Alternatives : ( ( ruleFeatureNode ) | ( ruleOperatorNode ) );
    public final void rule__AliasNode__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:762:1: ( ( ruleFeatureNode ) | ( ruleOperatorNode ) )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==RULE_ID) ) {
                int LA4_1 = input.LA(2);

                if ( (LA4_1==EOF||LA4_1==19||LA4_1==27) ) {
                    alt4=1;
                }
                else if ( (LA4_1==26) ) {
                    alt4=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:763:1: ( ruleFeatureNode )
                    {
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:763:1: ( ruleFeatureNode )
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:764:1: ruleFeatureNode
                    {
                     before(grammarAccess.getAliasNodeAccess().getFeatureNodeParserRuleCall_0()); 
                    pushFollow(FOLLOW_ruleFeatureNode_in_rule__AliasNode__Alternatives1579);
                    ruleFeatureNode();

                    state._fsp--;

                     after(grammarAccess.getAliasNodeAccess().getFeatureNodeParserRuleCall_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:769:6: ( ruleOperatorNode )
                    {
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:769:6: ( ruleOperatorNode )
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:770:1: ruleOperatorNode
                    {
                     before(grammarAccess.getAliasNodeAccess().getOperatorNodeParserRuleCall_1()); 
                    pushFollow(FOLLOW_ruleOperatorNode_in_rule__AliasNode__Alternatives1596);
                    ruleOperatorNode();

                    state._fsp--;

                     after(grammarAccess.getAliasNodeAccess().getOperatorNodeParserRuleCall_1()); 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AliasNode__Alternatives"


    // $ANTLR start "rule__FeatureParameter__Alternatives"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:780:1: rule__FeatureParameter__Alternatives : ( ( ( rule__FeatureParameter__FeatureAssignment_0 ) ) | ( ruleFeatureException ) | ( ruleSettedFeatureParameter ) );
    public final void rule__FeatureParameter__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:784:1: ( ( ( rule__FeatureParameter__FeatureAssignment_0 ) ) | ( ruleFeatureException ) | ( ruleSettedFeatureParameter ) )
            int alt5=3;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==RULE_ID) ) {
                int LA5_1 = input.LA(2);

                if ( (LA5_1==30) ) {
                    alt5=3;
                }
                else if ( (LA5_1==EOF||LA5_1==19||LA5_1==27) ) {
                    alt5=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 5, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA5_0==33) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:785:1: ( ( rule__FeatureParameter__FeatureAssignment_0 ) )
                    {
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:785:1: ( ( rule__FeatureParameter__FeatureAssignment_0 ) )
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:786:1: ( rule__FeatureParameter__FeatureAssignment_0 )
                    {
                     before(grammarAccess.getFeatureParameterAccess().getFeatureAssignment_0()); 
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:787:1: ( rule__FeatureParameter__FeatureAssignment_0 )
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:787:2: rule__FeatureParameter__FeatureAssignment_0
                    {
                    pushFollow(FOLLOW_rule__FeatureParameter__FeatureAssignment_0_in_rule__FeatureParameter__Alternatives1628);
                    rule__FeatureParameter__FeatureAssignment_0();

                    state._fsp--;


                    }

                     after(grammarAccess.getFeatureParameterAccess().getFeatureAssignment_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:791:6: ( ruleFeatureException )
                    {
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:791:6: ( ruleFeatureException )
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:792:1: ruleFeatureException
                    {
                     before(grammarAccess.getFeatureParameterAccess().getFeatureExceptionParserRuleCall_1()); 
                    pushFollow(FOLLOW_ruleFeatureException_in_rule__FeatureParameter__Alternatives1646);
                    ruleFeatureException();

                    state._fsp--;

                     after(grammarAccess.getFeatureParameterAccess().getFeatureExceptionParserRuleCall_1()); 

                    }


                    }
                    break;
                case 3 :
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:797:6: ( ruleSettedFeatureParameter )
                    {
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:797:6: ( ruleSettedFeatureParameter )
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:798:1: ruleSettedFeatureParameter
                    {
                     before(grammarAccess.getFeatureParameterAccess().getSettedFeatureParameterParserRuleCall_2()); 
                    pushFollow(FOLLOW_ruleSettedFeatureParameter_in_rule__FeatureParameter__Alternatives1663);
                    ruleSettedFeatureParameter();

                    state._fsp--;

                     after(grammarAccess.getFeatureParameterAccess().getSettedFeatureParameterParserRuleCall_2()); 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__FeatureParameter__Alternatives"


    // $ANTLR start "rule__SettedValue__Alternatives"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:808:1: rule__SettedValue__Alternatives : ( ( ruleJavaCodeValue ) | ( ruleLiteralValue ) );
    public final void rule__SettedValue__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:812:1: ( ( ruleJavaCodeValue ) | ( ruleLiteralValue ) )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==RULE_STRING) ) {
                alt6=1;
            }
            else if ( (LA6_0==RULE_ID) ) {
                alt6=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:813:1: ( ruleJavaCodeValue )
                    {
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:813:1: ( ruleJavaCodeValue )
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:814:1: ruleJavaCodeValue
                    {
                     before(grammarAccess.getSettedValueAccess().getJavaCodeValueParserRuleCall_0()); 
                    pushFollow(FOLLOW_ruleJavaCodeValue_in_rule__SettedValue__Alternatives1695);
                    ruleJavaCodeValue();

                    state._fsp--;

                     after(grammarAccess.getSettedValueAccess().getJavaCodeValueParserRuleCall_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:819:6: ( ruleLiteralValue )
                    {
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:819:6: ( ruleLiteralValue )
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:820:1: ruleLiteralValue
                    {
                     before(grammarAccess.getSettedValueAccess().getLiteralValueParserRuleCall_1()); 
                    pushFollow(FOLLOW_ruleLiteralValue_in_rule__SettedValue__Alternatives1712);
                    ruleLiteralValue();

                    state._fsp--;

                     after(grammarAccess.getSettedValueAccess().getLiteralValueParserRuleCall_1()); 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__SettedValue__Alternatives"


    // $ANTLR start "rule__EString__Alternatives"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:830:1: rule__EString__Alternatives : ( ( RULE_STRING ) | ( RULE_ID ) );
    public final void rule__EString__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:834:1: ( ( RULE_STRING ) | ( RULE_ID ) )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==RULE_STRING) ) {
                alt7=1;
            }
            else if ( (LA7_0==RULE_ID) ) {
                alt7=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:835:1: ( RULE_STRING )
                    {
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:835:1: ( RULE_STRING )
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:836:1: RULE_STRING
                    {
                     before(grammarAccess.getEStringAccess().getSTRINGTerminalRuleCall_0()); 
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__EString__Alternatives1744); 
                     after(grammarAccess.getEStringAccess().getSTRINGTerminalRuleCall_0()); 

                    }


                    }
                    break;
                case 2 :
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:841:6: ( RULE_ID )
                    {
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:841:6: ( RULE_ID )
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:842:1: RULE_ID
                    {
                     before(grammarAccess.getEStringAccess().getIDTerminalRuleCall_1()); 
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__EString__Alternatives1761); 
                     after(grammarAccess.getEStringAccess().getIDTerminalRuleCall_1()); 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__EString__Alternatives"


    // $ANTLR start "rule__Mapping__Group__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:855:1: rule__Mapping__Group__0 : rule__Mapping__Group__0__Impl rule__Mapping__Group__1 ;
    public final void rule__Mapping__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:859:1: ( rule__Mapping__Group__0__Impl rule__Mapping__Group__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:860:2: rule__Mapping__Group__0__Impl rule__Mapping__Group__1
            {
            pushFollow(FOLLOW_rule__Mapping__Group__0__Impl_in_rule__Mapping__Group__01792);
            rule__Mapping__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group__1_in_rule__Mapping__Group__01795);
            rule__Mapping__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group__0"


    // $ANTLR start "rule__Mapping__Group__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:867:1: rule__Mapping__Group__0__Impl : ( 'TomMapping' ) ;
    public final void rule__Mapping__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:871:1: ( ( 'TomMapping' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:872:1: ( 'TomMapping' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:872:1: ( 'TomMapping' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:873:1: 'TomMapping'
            {
             before(grammarAccess.getMappingAccess().getTomMappingKeyword_0()); 
            match(input,11,FOLLOW_11_in_rule__Mapping__Group__0__Impl1823); 
             after(grammarAccess.getMappingAccess().getTomMappingKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group__0__Impl"


    // $ANTLR start "rule__Mapping__Group__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:886:1: rule__Mapping__Group__1 : rule__Mapping__Group__1__Impl rule__Mapping__Group__2 ;
    public final void rule__Mapping__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:890:1: ( rule__Mapping__Group__1__Impl rule__Mapping__Group__2 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:891:2: rule__Mapping__Group__1__Impl rule__Mapping__Group__2
            {
            pushFollow(FOLLOW_rule__Mapping__Group__1__Impl_in_rule__Mapping__Group__11854);
            rule__Mapping__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group__2_in_rule__Mapping__Group__11857);
            rule__Mapping__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group__1"


    // $ANTLR start "rule__Mapping__Group__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:898:1: rule__Mapping__Group__1__Impl : ( ( rule__Mapping__NameAssignment_1 ) ) ;
    public final void rule__Mapping__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:902:1: ( ( ( rule__Mapping__NameAssignment_1 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:903:1: ( ( rule__Mapping__NameAssignment_1 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:903:1: ( ( rule__Mapping__NameAssignment_1 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:904:1: ( rule__Mapping__NameAssignment_1 )
            {
             before(grammarAccess.getMappingAccess().getNameAssignment_1()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:905:1: ( rule__Mapping__NameAssignment_1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:905:2: rule__Mapping__NameAssignment_1
            {
            pushFollow(FOLLOW_rule__Mapping__NameAssignment_1_in_rule__Mapping__Group__1__Impl1884);
            rule__Mapping__NameAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getMappingAccess().getNameAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group__1__Impl"


    // $ANTLR start "rule__Mapping__Group__2"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:915:1: rule__Mapping__Group__2 : rule__Mapping__Group__2__Impl rule__Mapping__Group__3 ;
    public final void rule__Mapping__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:919:1: ( rule__Mapping__Group__2__Impl rule__Mapping__Group__3 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:920:2: rule__Mapping__Group__2__Impl rule__Mapping__Group__3
            {
            pushFollow(FOLLOW_rule__Mapping__Group__2__Impl_in_rule__Mapping__Group__21914);
            rule__Mapping__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group__3_in_rule__Mapping__Group__21917);
            rule__Mapping__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group__2"


    // $ANTLR start "rule__Mapping__Group__2__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:927:1: rule__Mapping__Group__2__Impl : ( ';' ) ;
    public final void rule__Mapping__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:931:1: ( ( ';' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:932:1: ( ';' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:932:1: ( ';' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:933:1: ';'
            {
             before(grammarAccess.getMappingAccess().getSemicolonKeyword_2()); 
            match(input,12,FOLLOW_12_in_rule__Mapping__Group__2__Impl1945); 
             after(grammarAccess.getMappingAccess().getSemicolonKeyword_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group__2__Impl"


    // $ANTLR start "rule__Mapping__Group__3"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:946:1: rule__Mapping__Group__3 : rule__Mapping__Group__3__Impl rule__Mapping__Group__4 ;
    public final void rule__Mapping__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:950:1: ( rule__Mapping__Group__3__Impl rule__Mapping__Group__4 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:951:2: rule__Mapping__Group__3__Impl rule__Mapping__Group__4
            {
            pushFollow(FOLLOW_rule__Mapping__Group__3__Impl_in_rule__Mapping__Group__31976);
            rule__Mapping__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group__4_in_rule__Mapping__Group__31979);
            rule__Mapping__Group__4();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group__3"


    // $ANTLR start "rule__Mapping__Group__3__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:958:1: rule__Mapping__Group__3__Impl : ( ( rule__Mapping__Group_3__0 )? ) ;
    public final void rule__Mapping__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:962:1: ( ( ( rule__Mapping__Group_3__0 )? ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:963:1: ( ( rule__Mapping__Group_3__0 )? )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:963:1: ( ( rule__Mapping__Group_3__0 )? )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:964:1: ( rule__Mapping__Group_3__0 )?
            {
             before(grammarAccess.getMappingAccess().getGroup_3()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:965:1: ( rule__Mapping__Group_3__0 )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==13) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:965:2: rule__Mapping__Group_3__0
                    {
                    pushFollow(FOLLOW_rule__Mapping__Group_3__0_in_rule__Mapping__Group__3__Impl2006);
                    rule__Mapping__Group_3__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getMappingAccess().getGroup_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group__3__Impl"


    // $ANTLR start "rule__Mapping__Group__4"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:975:1: rule__Mapping__Group__4 : rule__Mapping__Group__4__Impl rule__Mapping__Group__5 ;
    public final void rule__Mapping__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:979:1: ( rule__Mapping__Group__4__Impl rule__Mapping__Group__5 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:980:2: rule__Mapping__Group__4__Impl rule__Mapping__Group__5
            {
            pushFollow(FOLLOW_rule__Mapping__Group__4__Impl_in_rule__Mapping__Group__42037);
            rule__Mapping__Group__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group__5_in_rule__Mapping__Group__42040);
            rule__Mapping__Group__5();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group__4"


    // $ANTLR start "rule__Mapping__Group__4__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:987:1: rule__Mapping__Group__4__Impl : ( ( rule__Mapping__Group_4__0 )* ) ;
    public final void rule__Mapping__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:991:1: ( ( ( rule__Mapping__Group_4__0 )* ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:992:1: ( ( rule__Mapping__Group_4__0 )* )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:992:1: ( ( rule__Mapping__Group_4__0 )* )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:993:1: ( rule__Mapping__Group_4__0 )*
            {
             before(grammarAccess.getMappingAccess().getGroup_4()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:994:1: ( rule__Mapping__Group_4__0 )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==14) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:994:2: rule__Mapping__Group_4__0
            	    {
            	    pushFollow(FOLLOW_rule__Mapping__Group_4__0_in_rule__Mapping__Group__4__Impl2067);
            	    rule__Mapping__Group_4__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

             after(grammarAccess.getMappingAccess().getGroup_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group__4__Impl"


    // $ANTLR start "rule__Mapping__Group__5"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1004:1: rule__Mapping__Group__5 : rule__Mapping__Group__5__Impl rule__Mapping__Group__6 ;
    public final void rule__Mapping__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1008:1: ( rule__Mapping__Group__5__Impl rule__Mapping__Group__6 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1009:2: rule__Mapping__Group__5__Impl rule__Mapping__Group__6
            {
            pushFollow(FOLLOW_rule__Mapping__Group__5__Impl_in_rule__Mapping__Group__52098);
            rule__Mapping__Group__5__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group__6_in_rule__Mapping__Group__52101);
            rule__Mapping__Group__6();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group__5"


    // $ANTLR start "rule__Mapping__Group__5__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1016:1: rule__Mapping__Group__5__Impl : ( ( rule__Mapping__Group_5__0 )? ) ;
    public final void rule__Mapping__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1020:1: ( ( ( rule__Mapping__Group_5__0 )? ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1021:1: ( ( rule__Mapping__Group_5__0 )? )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1021:1: ( ( rule__Mapping__Group_5__0 )? )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1022:1: ( rule__Mapping__Group_5__0 )?
            {
             before(grammarAccess.getMappingAccess().getGroup_5()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1023:1: ( rule__Mapping__Group_5__0 )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==15) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1023:2: rule__Mapping__Group_5__0
                    {
                    pushFollow(FOLLOW_rule__Mapping__Group_5__0_in_rule__Mapping__Group__5__Impl2128);
                    rule__Mapping__Group_5__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getMappingAccess().getGroup_5()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group__5__Impl"


    // $ANTLR start "rule__Mapping__Group__6"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1033:1: rule__Mapping__Group__6 : rule__Mapping__Group__6__Impl ;
    public final void rule__Mapping__Group__6() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1037:1: ( rule__Mapping__Group__6__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1038:2: rule__Mapping__Group__6__Impl
            {
            pushFollow(FOLLOW_rule__Mapping__Group__6__Impl_in_rule__Mapping__Group__62159);
            rule__Mapping__Group__6__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group__6"


    // $ANTLR start "rule__Mapping__Group__6__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1044:1: rule__Mapping__Group__6__Impl : ( ( rule__Mapping__Alternatives_6 )* ) ;
    public final void rule__Mapping__Group__6__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1048:1: ( ( ( rule__Mapping__Alternatives_6 )* ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1049:1: ( ( rule__Mapping__Alternatives_6 )* )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1049:1: ( ( rule__Mapping__Alternatives_6 )* )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1050:1: ( rule__Mapping__Alternatives_6 )*
            {
             before(grammarAccess.getMappingAccess().getAlternatives_6()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1051:1: ( rule__Mapping__Alternatives_6 )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( ((LA11_0>=21 && LA11_0<=22)) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1051:2: rule__Mapping__Alternatives_6
            	    {
            	    pushFollow(FOLLOW_rule__Mapping__Alternatives_6_in_rule__Mapping__Group__6__Impl2186);
            	    rule__Mapping__Alternatives_6();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);

             after(grammarAccess.getMappingAccess().getAlternatives_6()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group__6__Impl"


    // $ANTLR start "rule__Mapping__Group_3__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1075:1: rule__Mapping__Group_3__0 : rule__Mapping__Group_3__0__Impl rule__Mapping__Group_3__1 ;
    public final void rule__Mapping__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1079:1: ( rule__Mapping__Group_3__0__Impl rule__Mapping__Group_3__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1080:2: rule__Mapping__Group_3__0__Impl rule__Mapping__Group_3__1
            {
            pushFollow(FOLLOW_rule__Mapping__Group_3__0__Impl_in_rule__Mapping__Group_3__02231);
            rule__Mapping__Group_3__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_3__1_in_rule__Mapping__Group_3__02234);
            rule__Mapping__Group_3__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_3__0"


    // $ANTLR start "rule__Mapping__Group_3__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1087:1: rule__Mapping__Group_3__0__Impl : ( 'prefix' ) ;
    public final void rule__Mapping__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1091:1: ( ( 'prefix' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1092:1: ( 'prefix' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1092:1: ( 'prefix' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1093:1: 'prefix'
            {
             before(grammarAccess.getMappingAccess().getPrefixKeyword_3_0()); 
            match(input,13,FOLLOW_13_in_rule__Mapping__Group_3__0__Impl2262); 
             after(grammarAccess.getMappingAccess().getPrefixKeyword_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_3__0__Impl"


    // $ANTLR start "rule__Mapping__Group_3__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1106:1: rule__Mapping__Group_3__1 : rule__Mapping__Group_3__1__Impl rule__Mapping__Group_3__2 ;
    public final void rule__Mapping__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1110:1: ( rule__Mapping__Group_3__1__Impl rule__Mapping__Group_3__2 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1111:2: rule__Mapping__Group_3__1__Impl rule__Mapping__Group_3__2
            {
            pushFollow(FOLLOW_rule__Mapping__Group_3__1__Impl_in_rule__Mapping__Group_3__12293);
            rule__Mapping__Group_3__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_3__2_in_rule__Mapping__Group_3__12296);
            rule__Mapping__Group_3__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_3__1"


    // $ANTLR start "rule__Mapping__Group_3__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1118:1: rule__Mapping__Group_3__1__Impl : ( ( rule__Mapping__PrefixAssignment_3_1 ) ) ;
    public final void rule__Mapping__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1122:1: ( ( ( rule__Mapping__PrefixAssignment_3_1 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1123:1: ( ( rule__Mapping__PrefixAssignment_3_1 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1123:1: ( ( rule__Mapping__PrefixAssignment_3_1 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1124:1: ( rule__Mapping__PrefixAssignment_3_1 )
            {
             before(grammarAccess.getMappingAccess().getPrefixAssignment_3_1()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1125:1: ( rule__Mapping__PrefixAssignment_3_1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1125:2: rule__Mapping__PrefixAssignment_3_1
            {
            pushFollow(FOLLOW_rule__Mapping__PrefixAssignment_3_1_in_rule__Mapping__Group_3__1__Impl2323);
            rule__Mapping__PrefixAssignment_3_1();

            state._fsp--;


            }

             after(grammarAccess.getMappingAccess().getPrefixAssignment_3_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_3__1__Impl"


    // $ANTLR start "rule__Mapping__Group_3__2"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1135:1: rule__Mapping__Group_3__2 : rule__Mapping__Group_3__2__Impl ;
    public final void rule__Mapping__Group_3__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1139:1: ( rule__Mapping__Group_3__2__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1140:2: rule__Mapping__Group_3__2__Impl
            {
            pushFollow(FOLLOW_rule__Mapping__Group_3__2__Impl_in_rule__Mapping__Group_3__22353);
            rule__Mapping__Group_3__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_3__2"


    // $ANTLR start "rule__Mapping__Group_3__2__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1146:1: rule__Mapping__Group_3__2__Impl : ( ';' ) ;
    public final void rule__Mapping__Group_3__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1150:1: ( ( ';' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1151:1: ( ';' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1151:1: ( ';' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1152:1: ';'
            {
             before(grammarAccess.getMappingAccess().getSemicolonKeyword_3_2()); 
            match(input,12,FOLLOW_12_in_rule__Mapping__Group_3__2__Impl2381); 
             after(grammarAccess.getMappingAccess().getSemicolonKeyword_3_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_3__2__Impl"


    // $ANTLR start "rule__Mapping__Group_4__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1171:1: rule__Mapping__Group_4__0 : rule__Mapping__Group_4__0__Impl rule__Mapping__Group_4__1 ;
    public final void rule__Mapping__Group_4__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1175:1: ( rule__Mapping__Group_4__0__Impl rule__Mapping__Group_4__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1176:2: rule__Mapping__Group_4__0__Impl rule__Mapping__Group_4__1
            {
            pushFollow(FOLLOW_rule__Mapping__Group_4__0__Impl_in_rule__Mapping__Group_4__02418);
            rule__Mapping__Group_4__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_4__1_in_rule__Mapping__Group_4__02421);
            rule__Mapping__Group_4__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_4__0"


    // $ANTLR start "rule__Mapping__Group_4__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1183:1: rule__Mapping__Group_4__0__Impl : ( 'import' ) ;
    public final void rule__Mapping__Group_4__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1187:1: ( ( 'import' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1188:1: ( 'import' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1188:1: ( 'import' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1189:1: 'import'
            {
             before(grammarAccess.getMappingAccess().getImportKeyword_4_0()); 
            match(input,14,FOLLOW_14_in_rule__Mapping__Group_4__0__Impl2449); 
             after(grammarAccess.getMappingAccess().getImportKeyword_4_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_4__0__Impl"


    // $ANTLR start "rule__Mapping__Group_4__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1202:1: rule__Mapping__Group_4__1 : rule__Mapping__Group_4__1__Impl rule__Mapping__Group_4__2 ;
    public final void rule__Mapping__Group_4__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1206:1: ( rule__Mapping__Group_4__1__Impl rule__Mapping__Group_4__2 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1207:2: rule__Mapping__Group_4__1__Impl rule__Mapping__Group_4__2
            {
            pushFollow(FOLLOW_rule__Mapping__Group_4__1__Impl_in_rule__Mapping__Group_4__12480);
            rule__Mapping__Group_4__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_4__2_in_rule__Mapping__Group_4__12483);
            rule__Mapping__Group_4__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_4__1"


    // $ANTLR start "rule__Mapping__Group_4__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1214:1: rule__Mapping__Group_4__1__Impl : ( ( rule__Mapping__ImportsAssignment_4_1 ) ) ;
    public final void rule__Mapping__Group_4__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1218:1: ( ( ( rule__Mapping__ImportsAssignment_4_1 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1219:1: ( ( rule__Mapping__ImportsAssignment_4_1 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1219:1: ( ( rule__Mapping__ImportsAssignment_4_1 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1220:1: ( rule__Mapping__ImportsAssignment_4_1 )
            {
             before(grammarAccess.getMappingAccess().getImportsAssignment_4_1()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1221:1: ( rule__Mapping__ImportsAssignment_4_1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1221:2: rule__Mapping__ImportsAssignment_4_1
            {
            pushFollow(FOLLOW_rule__Mapping__ImportsAssignment_4_1_in_rule__Mapping__Group_4__1__Impl2510);
            rule__Mapping__ImportsAssignment_4_1();

            state._fsp--;


            }

             after(grammarAccess.getMappingAccess().getImportsAssignment_4_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_4__1__Impl"


    // $ANTLR start "rule__Mapping__Group_4__2"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1231:1: rule__Mapping__Group_4__2 : rule__Mapping__Group_4__2__Impl ;
    public final void rule__Mapping__Group_4__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1235:1: ( rule__Mapping__Group_4__2__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1236:2: rule__Mapping__Group_4__2__Impl
            {
            pushFollow(FOLLOW_rule__Mapping__Group_4__2__Impl_in_rule__Mapping__Group_4__22540);
            rule__Mapping__Group_4__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_4__2"


    // $ANTLR start "rule__Mapping__Group_4__2__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1242:1: rule__Mapping__Group_4__2__Impl : ( ';' ) ;
    public final void rule__Mapping__Group_4__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1246:1: ( ( ';' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1247:1: ( ';' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1247:1: ( ';' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1248:1: ';'
            {
             before(grammarAccess.getMappingAccess().getSemicolonKeyword_4_2()); 
            match(input,12,FOLLOW_12_in_rule__Mapping__Group_4__2__Impl2568); 
             after(grammarAccess.getMappingAccess().getSemicolonKeyword_4_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_4__2__Impl"


    // $ANTLR start "rule__Mapping__Group_5__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1267:1: rule__Mapping__Group_5__0 : rule__Mapping__Group_5__0__Impl rule__Mapping__Group_5__1 ;
    public final void rule__Mapping__Group_5__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1271:1: ( rule__Mapping__Group_5__0__Impl rule__Mapping__Group_5__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1272:2: rule__Mapping__Group_5__0__Impl rule__Mapping__Group_5__1
            {
            pushFollow(FOLLOW_rule__Mapping__Group_5__0__Impl_in_rule__Mapping__Group_5__02605);
            rule__Mapping__Group_5__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_5__1_in_rule__Mapping__Group_5__02608);
            rule__Mapping__Group_5__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5__0"


    // $ANTLR start "rule__Mapping__Group_5__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1279:1: rule__Mapping__Group_5__0__Impl : ( 'terminals' ) ;
    public final void rule__Mapping__Group_5__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1283:1: ( ( 'terminals' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1284:1: ( 'terminals' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1284:1: ( 'terminals' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1285:1: 'terminals'
            {
             before(grammarAccess.getMappingAccess().getTerminalsKeyword_5_0()); 
            match(input,15,FOLLOW_15_in_rule__Mapping__Group_5__0__Impl2636); 
             after(grammarAccess.getMappingAccess().getTerminalsKeyword_5_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5__0__Impl"


    // $ANTLR start "rule__Mapping__Group_5__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1298:1: rule__Mapping__Group_5__1 : rule__Mapping__Group_5__1__Impl rule__Mapping__Group_5__2 ;
    public final void rule__Mapping__Group_5__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1302:1: ( rule__Mapping__Group_5__1__Impl rule__Mapping__Group_5__2 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1303:2: rule__Mapping__Group_5__1__Impl rule__Mapping__Group_5__2
            {
            pushFollow(FOLLOW_rule__Mapping__Group_5__1__Impl_in_rule__Mapping__Group_5__12667);
            rule__Mapping__Group_5__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_5__2_in_rule__Mapping__Group_5__12670);
            rule__Mapping__Group_5__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5__1"


    // $ANTLR start "rule__Mapping__Group_5__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1310:1: rule__Mapping__Group_5__1__Impl : ( '{' ) ;
    public final void rule__Mapping__Group_5__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1314:1: ( ( '{' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1315:1: ( '{' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1315:1: ( '{' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1316:1: '{'
            {
             before(grammarAccess.getMappingAccess().getLeftCurlyBracketKeyword_5_1()); 
            match(input,16,FOLLOW_16_in_rule__Mapping__Group_5__1__Impl2698); 
             after(grammarAccess.getMappingAccess().getLeftCurlyBracketKeyword_5_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5__1__Impl"


    // $ANTLR start "rule__Mapping__Group_5__2"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1329:1: rule__Mapping__Group_5__2 : rule__Mapping__Group_5__2__Impl rule__Mapping__Group_5__3 ;
    public final void rule__Mapping__Group_5__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1333:1: ( rule__Mapping__Group_5__2__Impl rule__Mapping__Group_5__3 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1334:2: rule__Mapping__Group_5__2__Impl rule__Mapping__Group_5__3
            {
            pushFollow(FOLLOW_rule__Mapping__Group_5__2__Impl_in_rule__Mapping__Group_5__22729);
            rule__Mapping__Group_5__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_5__3_in_rule__Mapping__Group_5__22732);
            rule__Mapping__Group_5__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5__2"


    // $ANTLR start "rule__Mapping__Group_5__2__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1341:1: rule__Mapping__Group_5__2__Impl : ( ( rule__Mapping__Alternatives_5_2 ) ) ;
    public final void rule__Mapping__Group_5__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1345:1: ( ( ( rule__Mapping__Alternatives_5_2 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1346:1: ( ( rule__Mapping__Alternatives_5_2 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1346:1: ( ( rule__Mapping__Alternatives_5_2 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1347:1: ( rule__Mapping__Alternatives_5_2 )
            {
             before(grammarAccess.getMappingAccess().getAlternatives_5_2()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1348:1: ( rule__Mapping__Alternatives_5_2 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1348:2: rule__Mapping__Alternatives_5_2
            {
            pushFollow(FOLLOW_rule__Mapping__Alternatives_5_2_in_rule__Mapping__Group_5__2__Impl2759);
            rule__Mapping__Alternatives_5_2();

            state._fsp--;


            }

             after(grammarAccess.getMappingAccess().getAlternatives_5_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5__2__Impl"


    // $ANTLR start "rule__Mapping__Group_5__3"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1358:1: rule__Mapping__Group_5__3 : rule__Mapping__Group_5__3__Impl ;
    public final void rule__Mapping__Group_5__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1362:1: ( rule__Mapping__Group_5__3__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1363:2: rule__Mapping__Group_5__3__Impl
            {
            pushFollow(FOLLOW_rule__Mapping__Group_5__3__Impl_in_rule__Mapping__Group_5__32789);
            rule__Mapping__Group_5__3__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5__3"


    // $ANTLR start "rule__Mapping__Group_5__3__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1369:1: rule__Mapping__Group_5__3__Impl : ( '}' ) ;
    public final void rule__Mapping__Group_5__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1373:1: ( ( '}' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1374:1: ( '}' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1374:1: ( '}' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1375:1: '}'
            {
             before(grammarAccess.getMappingAccess().getRightCurlyBracketKeyword_5_3()); 
            match(input,17,FOLLOW_17_in_rule__Mapping__Group_5__3__Impl2817); 
             after(grammarAccess.getMappingAccess().getRightCurlyBracketKeyword_5_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5__3__Impl"


    // $ANTLR start "rule__Mapping__Group_5_2_0__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1396:1: rule__Mapping__Group_5_2_0__0 : rule__Mapping__Group_5_2_0__0__Impl rule__Mapping__Group_5_2_0__1 ;
    public final void rule__Mapping__Group_5_2_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1400:1: ( rule__Mapping__Group_5_2_0__0__Impl rule__Mapping__Group_5_2_0__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1401:2: rule__Mapping__Group_5_2_0__0__Impl rule__Mapping__Group_5_2_0__1
            {
            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0__0__Impl_in_rule__Mapping__Group_5_2_0__02856);
            rule__Mapping__Group_5_2_0__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0__1_in_rule__Mapping__Group_5_2_0__02859);
            rule__Mapping__Group_5_2_0__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0__0"


    // $ANTLR start "rule__Mapping__Group_5_2_0__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1408:1: rule__Mapping__Group_5_2_0__0__Impl : ( ( rule__Mapping__Group_5_2_0_0__0 )? ) ;
    public final void rule__Mapping__Group_5_2_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1412:1: ( ( ( rule__Mapping__Group_5_2_0_0__0 )? ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1413:1: ( ( rule__Mapping__Group_5_2_0_0__0 )? )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1413:1: ( ( rule__Mapping__Group_5_2_0_0__0 )? )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1414:1: ( rule__Mapping__Group_5_2_0_0__0 )?
            {
             before(grammarAccess.getMappingAccess().getGroup_5_2_0_0()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1415:1: ( rule__Mapping__Group_5_2_0_0__0 )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==18) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1415:2: rule__Mapping__Group_5_2_0_0__0
                    {
                    pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_0__0_in_rule__Mapping__Group_5_2_0__0__Impl2886);
                    rule__Mapping__Group_5_2_0_0__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getMappingAccess().getGroup_5_2_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0__0__Impl"


    // $ANTLR start "rule__Mapping__Group_5_2_0__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1425:1: rule__Mapping__Group_5_2_0__1 : rule__Mapping__Group_5_2_0__1__Impl ;
    public final void rule__Mapping__Group_5_2_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1429:1: ( rule__Mapping__Group_5_2_0__1__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1430:2: rule__Mapping__Group_5_2_0__1__Impl
            {
            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0__1__Impl_in_rule__Mapping__Group_5_2_0__12917);
            rule__Mapping__Group_5_2_0__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0__1"


    // $ANTLR start "rule__Mapping__Group_5_2_0__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1436:1: rule__Mapping__Group_5_2_0__1__Impl : ( ( rule__Mapping__Group_5_2_0_1__0 )? ) ;
    public final void rule__Mapping__Group_5_2_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1440:1: ( ( ( rule__Mapping__Group_5_2_0_1__0 )? ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1441:1: ( ( rule__Mapping__Group_5_2_0_1__0 )? )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1441:1: ( ( rule__Mapping__Group_5_2_0_1__0 )? )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1442:1: ( rule__Mapping__Group_5_2_0_1__0 )?
            {
             before(grammarAccess.getMappingAccess().getGroup_5_2_0_1()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1443:1: ( rule__Mapping__Group_5_2_0_1__0 )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==20) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1443:2: rule__Mapping__Group_5_2_0_1__0
                    {
                    pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_1__0_in_rule__Mapping__Group_5_2_0__1__Impl2944);
                    rule__Mapping__Group_5_2_0_1__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getMappingAccess().getGroup_5_2_0_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0__1__Impl"


    // $ANTLR start "rule__Mapping__Group_5_2_0_0__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1457:1: rule__Mapping__Group_5_2_0_0__0 : rule__Mapping__Group_5_2_0_0__0__Impl rule__Mapping__Group_5_2_0_0__1 ;
    public final void rule__Mapping__Group_5_2_0_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1461:1: ( rule__Mapping__Group_5_2_0_0__0__Impl rule__Mapping__Group_5_2_0_0__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1462:2: rule__Mapping__Group_5_2_0_0__0__Impl rule__Mapping__Group_5_2_0_0__1
            {
            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_0__0__Impl_in_rule__Mapping__Group_5_2_0_0__02979);
            rule__Mapping__Group_5_2_0_0__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_0__1_in_rule__Mapping__Group_5_2_0_0__02982);
            rule__Mapping__Group_5_2_0_0__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_0__0"


    // $ANTLR start "rule__Mapping__Group_5_2_0_0__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1469:1: rule__Mapping__Group_5_2_0_0__0__Impl : ( 'define' ) ;
    public final void rule__Mapping__Group_5_2_0_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1473:1: ( ( 'define' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1474:1: ( 'define' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1474:1: ( 'define' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1475:1: 'define'
            {
             before(grammarAccess.getMappingAccess().getDefineKeyword_5_2_0_0_0()); 
            match(input,18,FOLLOW_18_in_rule__Mapping__Group_5_2_0_0__0__Impl3010); 
             after(grammarAccess.getMappingAccess().getDefineKeyword_5_2_0_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_0__0__Impl"


    // $ANTLR start "rule__Mapping__Group_5_2_0_0__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1488:1: rule__Mapping__Group_5_2_0_0__1 : rule__Mapping__Group_5_2_0_0__1__Impl rule__Mapping__Group_5_2_0_0__2 ;
    public final void rule__Mapping__Group_5_2_0_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1492:1: ( rule__Mapping__Group_5_2_0_0__1__Impl rule__Mapping__Group_5_2_0_0__2 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1493:2: rule__Mapping__Group_5_2_0_0__1__Impl rule__Mapping__Group_5_2_0_0__2
            {
            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_0__1__Impl_in_rule__Mapping__Group_5_2_0_0__13041);
            rule__Mapping__Group_5_2_0_0__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_0__2_in_rule__Mapping__Group_5_2_0_0__13044);
            rule__Mapping__Group_5_2_0_0__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_0__1"


    // $ANTLR start "rule__Mapping__Group_5_2_0_0__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1500:1: rule__Mapping__Group_5_2_0_0__1__Impl : ( '{' ) ;
    public final void rule__Mapping__Group_5_2_0_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1504:1: ( ( '{' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1505:1: ( '{' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1505:1: ( '{' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1506:1: '{'
            {
             before(grammarAccess.getMappingAccess().getLeftCurlyBracketKeyword_5_2_0_0_1()); 
            match(input,16,FOLLOW_16_in_rule__Mapping__Group_5_2_0_0__1__Impl3072); 
             after(grammarAccess.getMappingAccess().getLeftCurlyBracketKeyword_5_2_0_0_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_0__1__Impl"


    // $ANTLR start "rule__Mapping__Group_5_2_0_0__2"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1519:1: rule__Mapping__Group_5_2_0_0__2 : rule__Mapping__Group_5_2_0_0__2__Impl rule__Mapping__Group_5_2_0_0__3 ;
    public final void rule__Mapping__Group_5_2_0_0__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1523:1: ( rule__Mapping__Group_5_2_0_0__2__Impl rule__Mapping__Group_5_2_0_0__3 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1524:2: rule__Mapping__Group_5_2_0_0__2__Impl rule__Mapping__Group_5_2_0_0__3
            {
            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_0__2__Impl_in_rule__Mapping__Group_5_2_0_0__23103);
            rule__Mapping__Group_5_2_0_0__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_0__3_in_rule__Mapping__Group_5_2_0_0__23106);
            rule__Mapping__Group_5_2_0_0__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_0__2"


    // $ANTLR start "rule__Mapping__Group_5_2_0_0__2__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1531:1: rule__Mapping__Group_5_2_0_0__2__Impl : ( ( rule__Mapping__TerminalsAssignment_5_2_0_0_2 ) ) ;
    public final void rule__Mapping__Group_5_2_0_0__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1535:1: ( ( ( rule__Mapping__TerminalsAssignment_5_2_0_0_2 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1536:1: ( ( rule__Mapping__TerminalsAssignment_5_2_0_0_2 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1536:1: ( ( rule__Mapping__TerminalsAssignment_5_2_0_0_2 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1537:1: ( rule__Mapping__TerminalsAssignment_5_2_0_0_2 )
            {
             before(grammarAccess.getMappingAccess().getTerminalsAssignment_5_2_0_0_2()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1538:1: ( rule__Mapping__TerminalsAssignment_5_2_0_0_2 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1538:2: rule__Mapping__TerminalsAssignment_5_2_0_0_2
            {
            pushFollow(FOLLOW_rule__Mapping__TerminalsAssignment_5_2_0_0_2_in_rule__Mapping__Group_5_2_0_0__2__Impl3133);
            rule__Mapping__TerminalsAssignment_5_2_0_0_2();

            state._fsp--;


            }

             after(grammarAccess.getMappingAccess().getTerminalsAssignment_5_2_0_0_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_0__2__Impl"


    // $ANTLR start "rule__Mapping__Group_5_2_0_0__3"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1548:1: rule__Mapping__Group_5_2_0_0__3 : rule__Mapping__Group_5_2_0_0__3__Impl rule__Mapping__Group_5_2_0_0__4 ;
    public final void rule__Mapping__Group_5_2_0_0__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1552:1: ( rule__Mapping__Group_5_2_0_0__3__Impl rule__Mapping__Group_5_2_0_0__4 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1553:2: rule__Mapping__Group_5_2_0_0__3__Impl rule__Mapping__Group_5_2_0_0__4
            {
            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_0__3__Impl_in_rule__Mapping__Group_5_2_0_0__33163);
            rule__Mapping__Group_5_2_0_0__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_0__4_in_rule__Mapping__Group_5_2_0_0__33166);
            rule__Mapping__Group_5_2_0_0__4();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_0__3"


    // $ANTLR start "rule__Mapping__Group_5_2_0_0__3__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1560:1: rule__Mapping__Group_5_2_0_0__3__Impl : ( ( rule__Mapping__Group_5_2_0_0_3__0 )* ) ;
    public final void rule__Mapping__Group_5_2_0_0__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1564:1: ( ( ( rule__Mapping__Group_5_2_0_0_3__0 )* ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1565:1: ( ( rule__Mapping__Group_5_2_0_0_3__0 )* )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1565:1: ( ( rule__Mapping__Group_5_2_0_0_3__0 )* )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1566:1: ( rule__Mapping__Group_5_2_0_0_3__0 )*
            {
             before(grammarAccess.getMappingAccess().getGroup_5_2_0_0_3()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1567:1: ( rule__Mapping__Group_5_2_0_0_3__0 )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==19) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1567:2: rule__Mapping__Group_5_2_0_0_3__0
            	    {
            	    pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_0_3__0_in_rule__Mapping__Group_5_2_0_0__3__Impl3193);
            	    rule__Mapping__Group_5_2_0_0_3__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);

             after(grammarAccess.getMappingAccess().getGroup_5_2_0_0_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_0__3__Impl"


    // $ANTLR start "rule__Mapping__Group_5_2_0_0__4"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1577:1: rule__Mapping__Group_5_2_0_0__4 : rule__Mapping__Group_5_2_0_0__4__Impl ;
    public final void rule__Mapping__Group_5_2_0_0__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1581:1: ( rule__Mapping__Group_5_2_0_0__4__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1582:2: rule__Mapping__Group_5_2_0_0__4__Impl
            {
            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_0__4__Impl_in_rule__Mapping__Group_5_2_0_0__43224);
            rule__Mapping__Group_5_2_0_0__4__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_0__4"


    // $ANTLR start "rule__Mapping__Group_5_2_0_0__4__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1588:1: rule__Mapping__Group_5_2_0_0__4__Impl : ( '}' ) ;
    public final void rule__Mapping__Group_5_2_0_0__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1592:1: ( ( '}' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1593:1: ( '}' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1593:1: ( '}' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1594:1: '}'
            {
             before(grammarAccess.getMappingAccess().getRightCurlyBracketKeyword_5_2_0_0_4()); 
            match(input,17,FOLLOW_17_in_rule__Mapping__Group_5_2_0_0__4__Impl3252); 
             after(grammarAccess.getMappingAccess().getRightCurlyBracketKeyword_5_2_0_0_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_0__4__Impl"


    // $ANTLR start "rule__Mapping__Group_5_2_0_0_3__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1617:1: rule__Mapping__Group_5_2_0_0_3__0 : rule__Mapping__Group_5_2_0_0_3__0__Impl rule__Mapping__Group_5_2_0_0_3__1 ;
    public final void rule__Mapping__Group_5_2_0_0_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1621:1: ( rule__Mapping__Group_5_2_0_0_3__0__Impl rule__Mapping__Group_5_2_0_0_3__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1622:2: rule__Mapping__Group_5_2_0_0_3__0__Impl rule__Mapping__Group_5_2_0_0_3__1
            {
            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_0_3__0__Impl_in_rule__Mapping__Group_5_2_0_0_3__03293);
            rule__Mapping__Group_5_2_0_0_3__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_0_3__1_in_rule__Mapping__Group_5_2_0_0_3__03296);
            rule__Mapping__Group_5_2_0_0_3__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_0_3__0"


    // $ANTLR start "rule__Mapping__Group_5_2_0_0_3__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1629:1: rule__Mapping__Group_5_2_0_0_3__0__Impl : ( ',' ) ;
    public final void rule__Mapping__Group_5_2_0_0_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1633:1: ( ( ',' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1634:1: ( ',' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1634:1: ( ',' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1635:1: ','
            {
             before(grammarAccess.getMappingAccess().getCommaKeyword_5_2_0_0_3_0()); 
            match(input,19,FOLLOW_19_in_rule__Mapping__Group_5_2_0_0_3__0__Impl3324); 
             after(grammarAccess.getMappingAccess().getCommaKeyword_5_2_0_0_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_0_3__0__Impl"


    // $ANTLR start "rule__Mapping__Group_5_2_0_0_3__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1648:1: rule__Mapping__Group_5_2_0_0_3__1 : rule__Mapping__Group_5_2_0_0_3__1__Impl ;
    public final void rule__Mapping__Group_5_2_0_0_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1652:1: ( rule__Mapping__Group_5_2_0_0_3__1__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1653:2: rule__Mapping__Group_5_2_0_0_3__1__Impl
            {
            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_0_3__1__Impl_in_rule__Mapping__Group_5_2_0_0_3__13355);
            rule__Mapping__Group_5_2_0_0_3__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_0_3__1"


    // $ANTLR start "rule__Mapping__Group_5_2_0_0_3__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1659:1: rule__Mapping__Group_5_2_0_0_3__1__Impl : ( ( rule__Mapping__TerminalsAssignment_5_2_0_0_3_1 ) ) ;
    public final void rule__Mapping__Group_5_2_0_0_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1663:1: ( ( ( rule__Mapping__TerminalsAssignment_5_2_0_0_3_1 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1664:1: ( ( rule__Mapping__TerminalsAssignment_5_2_0_0_3_1 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1664:1: ( ( rule__Mapping__TerminalsAssignment_5_2_0_0_3_1 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1665:1: ( rule__Mapping__TerminalsAssignment_5_2_0_0_3_1 )
            {
             before(grammarAccess.getMappingAccess().getTerminalsAssignment_5_2_0_0_3_1()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1666:1: ( rule__Mapping__TerminalsAssignment_5_2_0_0_3_1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1666:2: rule__Mapping__TerminalsAssignment_5_2_0_0_3_1
            {
            pushFollow(FOLLOW_rule__Mapping__TerminalsAssignment_5_2_0_0_3_1_in_rule__Mapping__Group_5_2_0_0_3__1__Impl3382);
            rule__Mapping__TerminalsAssignment_5_2_0_0_3_1();

            state._fsp--;


            }

             after(grammarAccess.getMappingAccess().getTerminalsAssignment_5_2_0_0_3_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_0_3__1__Impl"


    // $ANTLR start "rule__Mapping__Group_5_2_0_1__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1680:1: rule__Mapping__Group_5_2_0_1__0 : rule__Mapping__Group_5_2_0_1__0__Impl rule__Mapping__Group_5_2_0_1__1 ;
    public final void rule__Mapping__Group_5_2_0_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1684:1: ( rule__Mapping__Group_5_2_0_1__0__Impl rule__Mapping__Group_5_2_0_1__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1685:2: rule__Mapping__Group_5_2_0_1__0__Impl rule__Mapping__Group_5_2_0_1__1
            {
            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_1__0__Impl_in_rule__Mapping__Group_5_2_0_1__03416);
            rule__Mapping__Group_5_2_0_1__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_1__1_in_rule__Mapping__Group_5_2_0_1__03419);
            rule__Mapping__Group_5_2_0_1__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_1__0"


    // $ANTLR start "rule__Mapping__Group_5_2_0_1__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1692:1: rule__Mapping__Group_5_2_0_1__0__Impl : ( 'use' ) ;
    public final void rule__Mapping__Group_5_2_0_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1696:1: ( ( 'use' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1697:1: ( 'use' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1697:1: ( 'use' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1698:1: 'use'
            {
             before(grammarAccess.getMappingAccess().getUseKeyword_5_2_0_1_0()); 
            match(input,20,FOLLOW_20_in_rule__Mapping__Group_5_2_0_1__0__Impl3447); 
             after(grammarAccess.getMappingAccess().getUseKeyword_5_2_0_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_1__0__Impl"


    // $ANTLR start "rule__Mapping__Group_5_2_0_1__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1711:1: rule__Mapping__Group_5_2_0_1__1 : rule__Mapping__Group_5_2_0_1__1__Impl rule__Mapping__Group_5_2_0_1__2 ;
    public final void rule__Mapping__Group_5_2_0_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1715:1: ( rule__Mapping__Group_5_2_0_1__1__Impl rule__Mapping__Group_5_2_0_1__2 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1716:2: rule__Mapping__Group_5_2_0_1__1__Impl rule__Mapping__Group_5_2_0_1__2
            {
            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_1__1__Impl_in_rule__Mapping__Group_5_2_0_1__13478);
            rule__Mapping__Group_5_2_0_1__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_1__2_in_rule__Mapping__Group_5_2_0_1__13481);
            rule__Mapping__Group_5_2_0_1__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_1__1"


    // $ANTLR start "rule__Mapping__Group_5_2_0_1__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1723:1: rule__Mapping__Group_5_2_0_1__1__Impl : ( '{' ) ;
    public final void rule__Mapping__Group_5_2_0_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1727:1: ( ( '{' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1728:1: ( '{' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1728:1: ( '{' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1729:1: '{'
            {
             before(grammarAccess.getMappingAccess().getLeftCurlyBracketKeyword_5_2_0_1_1()); 
            match(input,16,FOLLOW_16_in_rule__Mapping__Group_5_2_0_1__1__Impl3509); 
             after(grammarAccess.getMappingAccess().getLeftCurlyBracketKeyword_5_2_0_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_1__1__Impl"


    // $ANTLR start "rule__Mapping__Group_5_2_0_1__2"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1742:1: rule__Mapping__Group_5_2_0_1__2 : rule__Mapping__Group_5_2_0_1__2__Impl rule__Mapping__Group_5_2_0_1__3 ;
    public final void rule__Mapping__Group_5_2_0_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1746:1: ( rule__Mapping__Group_5_2_0_1__2__Impl rule__Mapping__Group_5_2_0_1__3 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1747:2: rule__Mapping__Group_5_2_0_1__2__Impl rule__Mapping__Group_5_2_0_1__3
            {
            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_1__2__Impl_in_rule__Mapping__Group_5_2_0_1__23540);
            rule__Mapping__Group_5_2_0_1__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_1__3_in_rule__Mapping__Group_5_2_0_1__23543);
            rule__Mapping__Group_5_2_0_1__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_1__2"


    // $ANTLR start "rule__Mapping__Group_5_2_0_1__2__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1754:1: rule__Mapping__Group_5_2_0_1__2__Impl : ( ( rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_2 ) ) ;
    public final void rule__Mapping__Group_5_2_0_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1758:1: ( ( ( rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_2 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1759:1: ( ( rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_2 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1759:1: ( ( rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_2 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1760:1: ( rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_2 )
            {
             before(grammarAccess.getMappingAccess().getExternalTerminalsAssignment_5_2_0_1_2()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1761:1: ( rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_2 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1761:2: rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_2
            {
            pushFollow(FOLLOW_rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_2_in_rule__Mapping__Group_5_2_0_1__2__Impl3570);
            rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_2();

            state._fsp--;


            }

             after(grammarAccess.getMappingAccess().getExternalTerminalsAssignment_5_2_0_1_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_1__2__Impl"


    // $ANTLR start "rule__Mapping__Group_5_2_0_1__3"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1771:1: rule__Mapping__Group_5_2_0_1__3 : rule__Mapping__Group_5_2_0_1__3__Impl rule__Mapping__Group_5_2_0_1__4 ;
    public final void rule__Mapping__Group_5_2_0_1__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1775:1: ( rule__Mapping__Group_5_2_0_1__3__Impl rule__Mapping__Group_5_2_0_1__4 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1776:2: rule__Mapping__Group_5_2_0_1__3__Impl rule__Mapping__Group_5_2_0_1__4
            {
            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_1__3__Impl_in_rule__Mapping__Group_5_2_0_1__33600);
            rule__Mapping__Group_5_2_0_1__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_1__4_in_rule__Mapping__Group_5_2_0_1__33603);
            rule__Mapping__Group_5_2_0_1__4();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_1__3"


    // $ANTLR start "rule__Mapping__Group_5_2_0_1__3__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1783:1: rule__Mapping__Group_5_2_0_1__3__Impl : ( ( rule__Mapping__Group_5_2_0_1_3__0 )* ) ;
    public final void rule__Mapping__Group_5_2_0_1__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1787:1: ( ( ( rule__Mapping__Group_5_2_0_1_3__0 )* ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1788:1: ( ( rule__Mapping__Group_5_2_0_1_3__0 )* )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1788:1: ( ( rule__Mapping__Group_5_2_0_1_3__0 )* )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1789:1: ( rule__Mapping__Group_5_2_0_1_3__0 )*
            {
             before(grammarAccess.getMappingAccess().getGroup_5_2_0_1_3()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1790:1: ( rule__Mapping__Group_5_2_0_1_3__0 )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==19) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1790:2: rule__Mapping__Group_5_2_0_1_3__0
            	    {
            	    pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_1_3__0_in_rule__Mapping__Group_5_2_0_1__3__Impl3630);
            	    rule__Mapping__Group_5_2_0_1_3__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);

             after(grammarAccess.getMappingAccess().getGroup_5_2_0_1_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_1__3__Impl"


    // $ANTLR start "rule__Mapping__Group_5_2_0_1__4"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1800:1: rule__Mapping__Group_5_2_0_1__4 : rule__Mapping__Group_5_2_0_1__4__Impl ;
    public final void rule__Mapping__Group_5_2_0_1__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1804:1: ( rule__Mapping__Group_5_2_0_1__4__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1805:2: rule__Mapping__Group_5_2_0_1__4__Impl
            {
            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_1__4__Impl_in_rule__Mapping__Group_5_2_0_1__43661);
            rule__Mapping__Group_5_2_0_1__4__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_1__4"


    // $ANTLR start "rule__Mapping__Group_5_2_0_1__4__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1811:1: rule__Mapping__Group_5_2_0_1__4__Impl : ( '}' ) ;
    public final void rule__Mapping__Group_5_2_0_1__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1815:1: ( ( '}' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1816:1: ( '}' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1816:1: ( '}' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1817:1: '}'
            {
             before(grammarAccess.getMappingAccess().getRightCurlyBracketKeyword_5_2_0_1_4()); 
            match(input,17,FOLLOW_17_in_rule__Mapping__Group_5_2_0_1__4__Impl3689); 
             after(grammarAccess.getMappingAccess().getRightCurlyBracketKeyword_5_2_0_1_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_1__4__Impl"


    // $ANTLR start "rule__Mapping__Group_5_2_0_1_3__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1840:1: rule__Mapping__Group_5_2_0_1_3__0 : rule__Mapping__Group_5_2_0_1_3__0__Impl rule__Mapping__Group_5_2_0_1_3__1 ;
    public final void rule__Mapping__Group_5_2_0_1_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1844:1: ( rule__Mapping__Group_5_2_0_1_3__0__Impl rule__Mapping__Group_5_2_0_1_3__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1845:2: rule__Mapping__Group_5_2_0_1_3__0__Impl rule__Mapping__Group_5_2_0_1_3__1
            {
            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_1_3__0__Impl_in_rule__Mapping__Group_5_2_0_1_3__03730);
            rule__Mapping__Group_5_2_0_1_3__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_1_3__1_in_rule__Mapping__Group_5_2_0_1_3__03733);
            rule__Mapping__Group_5_2_0_1_3__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_1_3__0"


    // $ANTLR start "rule__Mapping__Group_5_2_0_1_3__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1852:1: rule__Mapping__Group_5_2_0_1_3__0__Impl : ( ',' ) ;
    public final void rule__Mapping__Group_5_2_0_1_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1856:1: ( ( ',' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1857:1: ( ',' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1857:1: ( ',' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1858:1: ','
            {
             before(grammarAccess.getMappingAccess().getCommaKeyword_5_2_0_1_3_0()); 
            match(input,19,FOLLOW_19_in_rule__Mapping__Group_5_2_0_1_3__0__Impl3761); 
             after(grammarAccess.getMappingAccess().getCommaKeyword_5_2_0_1_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_1_3__0__Impl"


    // $ANTLR start "rule__Mapping__Group_5_2_0_1_3__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1871:1: rule__Mapping__Group_5_2_0_1_3__1 : rule__Mapping__Group_5_2_0_1_3__1__Impl ;
    public final void rule__Mapping__Group_5_2_0_1_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1875:1: ( rule__Mapping__Group_5_2_0_1_3__1__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1876:2: rule__Mapping__Group_5_2_0_1_3__1__Impl
            {
            pushFollow(FOLLOW_rule__Mapping__Group_5_2_0_1_3__1__Impl_in_rule__Mapping__Group_5_2_0_1_3__13792);
            rule__Mapping__Group_5_2_0_1_3__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_1_3__1"


    // $ANTLR start "rule__Mapping__Group_5_2_0_1_3__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1882:1: rule__Mapping__Group_5_2_0_1_3__1__Impl : ( ( rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_3_1 ) ) ;
    public final void rule__Mapping__Group_5_2_0_1_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1886:1: ( ( ( rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_3_1 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1887:1: ( ( rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_3_1 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1887:1: ( ( rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_3_1 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1888:1: ( rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_3_1 )
            {
             before(grammarAccess.getMappingAccess().getExternalTerminalsAssignment_5_2_0_1_3_1()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1889:1: ( rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_3_1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1889:2: rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_3_1
            {
            pushFollow(FOLLOW_rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_3_1_in_rule__Mapping__Group_5_2_0_1_3__1__Impl3819);
            rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_3_1();

            state._fsp--;


            }

             after(grammarAccess.getMappingAccess().getExternalTerminalsAssignment_5_2_0_1_3_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_0_1_3__1__Impl"


    // $ANTLR start "rule__Mapping__Group_5_2_1__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1903:1: rule__Mapping__Group_5_2_1__0 : rule__Mapping__Group_5_2_1__0__Impl rule__Mapping__Group_5_2_1__1 ;
    public final void rule__Mapping__Group_5_2_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1907:1: ( rule__Mapping__Group_5_2_1__0__Impl rule__Mapping__Group_5_2_1__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1908:2: rule__Mapping__Group_5_2_1__0__Impl rule__Mapping__Group_5_2_1__1
            {
            pushFollow(FOLLOW_rule__Mapping__Group_5_2_1__0__Impl_in_rule__Mapping__Group_5_2_1__03853);
            rule__Mapping__Group_5_2_1__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_5_2_1__1_in_rule__Mapping__Group_5_2_1__03856);
            rule__Mapping__Group_5_2_1__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_1__0"


    // $ANTLR start "rule__Mapping__Group_5_2_1__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1915:1: rule__Mapping__Group_5_2_1__0__Impl : ( ( rule__Mapping__TerminalsAssignment_5_2_1_0 ) ) ;
    public final void rule__Mapping__Group_5_2_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1919:1: ( ( ( rule__Mapping__TerminalsAssignment_5_2_1_0 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1920:1: ( ( rule__Mapping__TerminalsAssignment_5_2_1_0 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1920:1: ( ( rule__Mapping__TerminalsAssignment_5_2_1_0 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1921:1: ( rule__Mapping__TerminalsAssignment_5_2_1_0 )
            {
             before(grammarAccess.getMappingAccess().getTerminalsAssignment_5_2_1_0()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1922:1: ( rule__Mapping__TerminalsAssignment_5_2_1_0 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1922:2: rule__Mapping__TerminalsAssignment_5_2_1_0
            {
            pushFollow(FOLLOW_rule__Mapping__TerminalsAssignment_5_2_1_0_in_rule__Mapping__Group_5_2_1__0__Impl3883);
            rule__Mapping__TerminalsAssignment_5_2_1_0();

            state._fsp--;


            }

             after(grammarAccess.getMappingAccess().getTerminalsAssignment_5_2_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_1__0__Impl"


    // $ANTLR start "rule__Mapping__Group_5_2_1__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1932:1: rule__Mapping__Group_5_2_1__1 : rule__Mapping__Group_5_2_1__1__Impl ;
    public final void rule__Mapping__Group_5_2_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1936:1: ( rule__Mapping__Group_5_2_1__1__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1937:2: rule__Mapping__Group_5_2_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Mapping__Group_5_2_1__1__Impl_in_rule__Mapping__Group_5_2_1__13913);
            rule__Mapping__Group_5_2_1__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_1__1"


    // $ANTLR start "rule__Mapping__Group_5_2_1__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1943:1: rule__Mapping__Group_5_2_1__1__Impl : ( ( rule__Mapping__Group_5_2_1_1__0 )* ) ;
    public final void rule__Mapping__Group_5_2_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1947:1: ( ( ( rule__Mapping__Group_5_2_1_1__0 )* ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1948:1: ( ( rule__Mapping__Group_5_2_1_1__0 )* )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1948:1: ( ( rule__Mapping__Group_5_2_1_1__0 )* )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1949:1: ( rule__Mapping__Group_5_2_1_1__0 )*
            {
             before(grammarAccess.getMappingAccess().getGroup_5_2_1_1()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1950:1: ( rule__Mapping__Group_5_2_1_1__0 )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0==19) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1950:2: rule__Mapping__Group_5_2_1_1__0
            	    {
            	    pushFollow(FOLLOW_rule__Mapping__Group_5_2_1_1__0_in_rule__Mapping__Group_5_2_1__1__Impl3940);
            	    rule__Mapping__Group_5_2_1_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop16;
                }
            } while (true);

             after(grammarAccess.getMappingAccess().getGroup_5_2_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_1__1__Impl"


    // $ANTLR start "rule__Mapping__Group_5_2_1_1__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1964:1: rule__Mapping__Group_5_2_1_1__0 : rule__Mapping__Group_5_2_1_1__0__Impl rule__Mapping__Group_5_2_1_1__1 ;
    public final void rule__Mapping__Group_5_2_1_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1968:1: ( rule__Mapping__Group_5_2_1_1__0__Impl rule__Mapping__Group_5_2_1_1__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1969:2: rule__Mapping__Group_5_2_1_1__0__Impl rule__Mapping__Group_5_2_1_1__1
            {
            pushFollow(FOLLOW_rule__Mapping__Group_5_2_1_1__0__Impl_in_rule__Mapping__Group_5_2_1_1__03975);
            rule__Mapping__Group_5_2_1_1__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_5_2_1_1__1_in_rule__Mapping__Group_5_2_1_1__03978);
            rule__Mapping__Group_5_2_1_1__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_1_1__0"


    // $ANTLR start "rule__Mapping__Group_5_2_1_1__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1976:1: rule__Mapping__Group_5_2_1_1__0__Impl : ( ',' ) ;
    public final void rule__Mapping__Group_5_2_1_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1980:1: ( ( ',' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1981:1: ( ',' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1981:1: ( ',' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1982:1: ','
            {
             before(grammarAccess.getMappingAccess().getCommaKeyword_5_2_1_1_0()); 
            match(input,19,FOLLOW_19_in_rule__Mapping__Group_5_2_1_1__0__Impl4006); 
             after(grammarAccess.getMappingAccess().getCommaKeyword_5_2_1_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_1_1__0__Impl"


    // $ANTLR start "rule__Mapping__Group_5_2_1_1__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1995:1: rule__Mapping__Group_5_2_1_1__1 : rule__Mapping__Group_5_2_1_1__1__Impl ;
    public final void rule__Mapping__Group_5_2_1_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:1999:1: ( rule__Mapping__Group_5_2_1_1__1__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2000:2: rule__Mapping__Group_5_2_1_1__1__Impl
            {
            pushFollow(FOLLOW_rule__Mapping__Group_5_2_1_1__1__Impl_in_rule__Mapping__Group_5_2_1_1__14037);
            rule__Mapping__Group_5_2_1_1__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_1_1__1"


    // $ANTLR start "rule__Mapping__Group_5_2_1_1__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2006:1: rule__Mapping__Group_5_2_1_1__1__Impl : ( ( rule__Mapping__TerminalsAssignment_5_2_1_1_1 ) ) ;
    public final void rule__Mapping__Group_5_2_1_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2010:1: ( ( ( rule__Mapping__TerminalsAssignment_5_2_1_1_1 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2011:1: ( ( rule__Mapping__TerminalsAssignment_5_2_1_1_1 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2011:1: ( ( rule__Mapping__TerminalsAssignment_5_2_1_1_1 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2012:1: ( rule__Mapping__TerminalsAssignment_5_2_1_1_1 )
            {
             before(grammarAccess.getMappingAccess().getTerminalsAssignment_5_2_1_1_1()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2013:1: ( rule__Mapping__TerminalsAssignment_5_2_1_1_1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2013:2: rule__Mapping__TerminalsAssignment_5_2_1_1_1
            {
            pushFollow(FOLLOW_rule__Mapping__TerminalsAssignment_5_2_1_1_1_in_rule__Mapping__Group_5_2_1_1__1__Impl4064);
            rule__Mapping__TerminalsAssignment_5_2_1_1_1();

            state._fsp--;


            }

             after(grammarAccess.getMappingAccess().getTerminalsAssignment_5_2_1_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_5_2_1_1__1__Impl"


    // $ANTLR start "rule__Mapping__Group_6_0__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2027:1: rule__Mapping__Group_6_0__0 : rule__Mapping__Group_6_0__0__Impl rule__Mapping__Group_6_0__1 ;
    public final void rule__Mapping__Group_6_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2031:1: ( rule__Mapping__Group_6_0__0__Impl rule__Mapping__Group_6_0__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2032:2: rule__Mapping__Group_6_0__0__Impl rule__Mapping__Group_6_0__1
            {
            pushFollow(FOLLOW_rule__Mapping__Group_6_0__0__Impl_in_rule__Mapping__Group_6_0__04098);
            rule__Mapping__Group_6_0__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_6_0__1_in_rule__Mapping__Group_6_0__04101);
            rule__Mapping__Group_6_0__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_6_0__0"


    // $ANTLR start "rule__Mapping__Group_6_0__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2039:1: rule__Mapping__Group_6_0__0__Impl : ( 'operators' ) ;
    public final void rule__Mapping__Group_6_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2043:1: ( ( 'operators' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2044:1: ( 'operators' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2044:1: ( 'operators' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2045:1: 'operators'
            {
             before(grammarAccess.getMappingAccess().getOperatorsKeyword_6_0_0()); 
            match(input,21,FOLLOW_21_in_rule__Mapping__Group_6_0__0__Impl4129); 
             after(grammarAccess.getMappingAccess().getOperatorsKeyword_6_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_6_0__0__Impl"


    // $ANTLR start "rule__Mapping__Group_6_0__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2058:1: rule__Mapping__Group_6_0__1 : rule__Mapping__Group_6_0__1__Impl rule__Mapping__Group_6_0__2 ;
    public final void rule__Mapping__Group_6_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2062:1: ( rule__Mapping__Group_6_0__1__Impl rule__Mapping__Group_6_0__2 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2063:2: rule__Mapping__Group_6_0__1__Impl rule__Mapping__Group_6_0__2
            {
            pushFollow(FOLLOW_rule__Mapping__Group_6_0__1__Impl_in_rule__Mapping__Group_6_0__14160);
            rule__Mapping__Group_6_0__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_6_0__2_in_rule__Mapping__Group_6_0__14163);
            rule__Mapping__Group_6_0__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_6_0__1"


    // $ANTLR start "rule__Mapping__Group_6_0__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2070:1: rule__Mapping__Group_6_0__1__Impl : ( '{' ) ;
    public final void rule__Mapping__Group_6_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2074:1: ( ( '{' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2075:1: ( '{' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2075:1: ( '{' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2076:1: '{'
            {
             before(grammarAccess.getMappingAccess().getLeftCurlyBracketKeyword_6_0_1()); 
            match(input,16,FOLLOW_16_in_rule__Mapping__Group_6_0__1__Impl4191); 
             after(grammarAccess.getMappingAccess().getLeftCurlyBracketKeyword_6_0_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_6_0__1__Impl"


    // $ANTLR start "rule__Mapping__Group_6_0__2"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2089:1: rule__Mapping__Group_6_0__2 : rule__Mapping__Group_6_0__2__Impl rule__Mapping__Group_6_0__3 ;
    public final void rule__Mapping__Group_6_0__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2093:1: ( rule__Mapping__Group_6_0__2__Impl rule__Mapping__Group_6_0__3 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2094:2: rule__Mapping__Group_6_0__2__Impl rule__Mapping__Group_6_0__3
            {
            pushFollow(FOLLOW_rule__Mapping__Group_6_0__2__Impl_in_rule__Mapping__Group_6_0__24222);
            rule__Mapping__Group_6_0__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_6_0__3_in_rule__Mapping__Group_6_0__24225);
            rule__Mapping__Group_6_0__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_6_0__2"


    // $ANTLR start "rule__Mapping__Group_6_0__2__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2101:1: rule__Mapping__Group_6_0__2__Impl : ( ( rule__Mapping__OperatorsAssignment_6_0_2 ) ) ;
    public final void rule__Mapping__Group_6_0__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2105:1: ( ( ( rule__Mapping__OperatorsAssignment_6_0_2 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2106:1: ( ( rule__Mapping__OperatorsAssignment_6_0_2 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2106:1: ( ( rule__Mapping__OperatorsAssignment_6_0_2 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2107:1: ( rule__Mapping__OperatorsAssignment_6_0_2 )
            {
             before(grammarAccess.getMappingAccess().getOperatorsAssignment_6_0_2()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2108:1: ( rule__Mapping__OperatorsAssignment_6_0_2 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2108:2: rule__Mapping__OperatorsAssignment_6_0_2
            {
            pushFollow(FOLLOW_rule__Mapping__OperatorsAssignment_6_0_2_in_rule__Mapping__Group_6_0__2__Impl4252);
            rule__Mapping__OperatorsAssignment_6_0_2();

            state._fsp--;


            }

             after(grammarAccess.getMappingAccess().getOperatorsAssignment_6_0_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_6_0__2__Impl"


    // $ANTLR start "rule__Mapping__Group_6_0__3"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2118:1: rule__Mapping__Group_6_0__3 : rule__Mapping__Group_6_0__3__Impl rule__Mapping__Group_6_0__4 ;
    public final void rule__Mapping__Group_6_0__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2122:1: ( rule__Mapping__Group_6_0__3__Impl rule__Mapping__Group_6_0__4 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2123:2: rule__Mapping__Group_6_0__3__Impl rule__Mapping__Group_6_0__4
            {
            pushFollow(FOLLOW_rule__Mapping__Group_6_0__3__Impl_in_rule__Mapping__Group_6_0__34282);
            rule__Mapping__Group_6_0__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_6_0__4_in_rule__Mapping__Group_6_0__34285);
            rule__Mapping__Group_6_0__4();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_6_0__3"


    // $ANTLR start "rule__Mapping__Group_6_0__3__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2130:1: rule__Mapping__Group_6_0__3__Impl : ( ( rule__Mapping__Group_6_0_3__0 )* ) ;
    public final void rule__Mapping__Group_6_0__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2134:1: ( ( ( rule__Mapping__Group_6_0_3__0 )* ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2135:1: ( ( rule__Mapping__Group_6_0_3__0 )* )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2135:1: ( ( rule__Mapping__Group_6_0_3__0 )* )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2136:1: ( rule__Mapping__Group_6_0_3__0 )*
            {
             before(grammarAccess.getMappingAccess().getGroup_6_0_3()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2137:1: ( rule__Mapping__Group_6_0_3__0 )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0==12) ) {
                    int LA17_1 = input.LA(2);

                    if ( (LA17_1==24||LA17_1==28) ) {
                        alt17=1;
                    }


                }


                switch (alt17) {
            	case 1 :
            	    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2137:2: rule__Mapping__Group_6_0_3__0
            	    {
            	    pushFollow(FOLLOW_rule__Mapping__Group_6_0_3__0_in_rule__Mapping__Group_6_0__3__Impl4312);
            	    rule__Mapping__Group_6_0_3__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);

             after(grammarAccess.getMappingAccess().getGroup_6_0_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_6_0__3__Impl"


    // $ANTLR start "rule__Mapping__Group_6_0__4"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2147:1: rule__Mapping__Group_6_0__4 : rule__Mapping__Group_6_0__4__Impl rule__Mapping__Group_6_0__5 ;
    public final void rule__Mapping__Group_6_0__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2151:1: ( rule__Mapping__Group_6_0__4__Impl rule__Mapping__Group_6_0__5 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2152:2: rule__Mapping__Group_6_0__4__Impl rule__Mapping__Group_6_0__5
            {
            pushFollow(FOLLOW_rule__Mapping__Group_6_0__4__Impl_in_rule__Mapping__Group_6_0__44343);
            rule__Mapping__Group_6_0__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_6_0__5_in_rule__Mapping__Group_6_0__44346);
            rule__Mapping__Group_6_0__5();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_6_0__4"


    // $ANTLR start "rule__Mapping__Group_6_0__4__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2159:1: rule__Mapping__Group_6_0__4__Impl : ( ';' ) ;
    public final void rule__Mapping__Group_6_0__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2163:1: ( ( ';' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2164:1: ( ';' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2164:1: ( ';' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2165:1: ';'
            {
             before(grammarAccess.getMappingAccess().getSemicolonKeyword_6_0_4()); 
            match(input,12,FOLLOW_12_in_rule__Mapping__Group_6_0__4__Impl4374); 
             after(grammarAccess.getMappingAccess().getSemicolonKeyword_6_0_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_6_0__4__Impl"


    // $ANTLR start "rule__Mapping__Group_6_0__5"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2178:1: rule__Mapping__Group_6_0__5 : rule__Mapping__Group_6_0__5__Impl ;
    public final void rule__Mapping__Group_6_0__5() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2182:1: ( rule__Mapping__Group_6_0__5__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2183:2: rule__Mapping__Group_6_0__5__Impl
            {
            pushFollow(FOLLOW_rule__Mapping__Group_6_0__5__Impl_in_rule__Mapping__Group_6_0__54405);
            rule__Mapping__Group_6_0__5__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_6_0__5"


    // $ANTLR start "rule__Mapping__Group_6_0__5__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2189:1: rule__Mapping__Group_6_0__5__Impl : ( '}' ) ;
    public final void rule__Mapping__Group_6_0__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2193:1: ( ( '}' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2194:1: ( '}' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2194:1: ( '}' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2195:1: '}'
            {
             before(grammarAccess.getMappingAccess().getRightCurlyBracketKeyword_6_0_5()); 
            match(input,17,FOLLOW_17_in_rule__Mapping__Group_6_0__5__Impl4433); 
             after(grammarAccess.getMappingAccess().getRightCurlyBracketKeyword_6_0_5()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_6_0__5__Impl"


    // $ANTLR start "rule__Mapping__Group_6_0_3__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2220:1: rule__Mapping__Group_6_0_3__0 : rule__Mapping__Group_6_0_3__0__Impl rule__Mapping__Group_6_0_3__1 ;
    public final void rule__Mapping__Group_6_0_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2224:1: ( rule__Mapping__Group_6_0_3__0__Impl rule__Mapping__Group_6_0_3__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2225:2: rule__Mapping__Group_6_0_3__0__Impl rule__Mapping__Group_6_0_3__1
            {
            pushFollow(FOLLOW_rule__Mapping__Group_6_0_3__0__Impl_in_rule__Mapping__Group_6_0_3__04476);
            rule__Mapping__Group_6_0_3__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Mapping__Group_6_0_3__1_in_rule__Mapping__Group_6_0_3__04479);
            rule__Mapping__Group_6_0_3__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_6_0_3__0"


    // $ANTLR start "rule__Mapping__Group_6_0_3__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2232:1: rule__Mapping__Group_6_0_3__0__Impl : ( ';' ) ;
    public final void rule__Mapping__Group_6_0_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2236:1: ( ( ';' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2237:1: ( ';' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2237:1: ( ';' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2238:1: ';'
            {
             before(grammarAccess.getMappingAccess().getSemicolonKeyword_6_0_3_0()); 
            match(input,12,FOLLOW_12_in_rule__Mapping__Group_6_0_3__0__Impl4507); 
             after(grammarAccess.getMappingAccess().getSemicolonKeyword_6_0_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_6_0_3__0__Impl"


    // $ANTLR start "rule__Mapping__Group_6_0_3__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2251:1: rule__Mapping__Group_6_0_3__1 : rule__Mapping__Group_6_0_3__1__Impl ;
    public final void rule__Mapping__Group_6_0_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2255:1: ( rule__Mapping__Group_6_0_3__1__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2256:2: rule__Mapping__Group_6_0_3__1__Impl
            {
            pushFollow(FOLLOW_rule__Mapping__Group_6_0_3__1__Impl_in_rule__Mapping__Group_6_0_3__14538);
            rule__Mapping__Group_6_0_3__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_6_0_3__1"


    // $ANTLR start "rule__Mapping__Group_6_0_3__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2262:1: rule__Mapping__Group_6_0_3__1__Impl : ( ( rule__Mapping__OperatorsAssignment_6_0_3_1 ) ) ;
    public final void rule__Mapping__Group_6_0_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2266:1: ( ( ( rule__Mapping__OperatorsAssignment_6_0_3_1 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2267:1: ( ( rule__Mapping__OperatorsAssignment_6_0_3_1 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2267:1: ( ( rule__Mapping__OperatorsAssignment_6_0_3_1 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2268:1: ( rule__Mapping__OperatorsAssignment_6_0_3_1 )
            {
             before(grammarAccess.getMappingAccess().getOperatorsAssignment_6_0_3_1()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2269:1: ( rule__Mapping__OperatorsAssignment_6_0_3_1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2269:2: rule__Mapping__OperatorsAssignment_6_0_3_1
            {
            pushFollow(FOLLOW_rule__Mapping__OperatorsAssignment_6_0_3_1_in_rule__Mapping__Group_6_0_3__1__Impl4565);
            rule__Mapping__OperatorsAssignment_6_0_3_1();

            state._fsp--;


            }

             after(grammarAccess.getMappingAccess().getOperatorsAssignment_6_0_3_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__Group_6_0_3__1__Impl"


    // $ANTLR start "rule__Module__Group__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2283:1: rule__Module__Group__0 : rule__Module__Group__0__Impl rule__Module__Group__1 ;
    public final void rule__Module__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2287:1: ( rule__Module__Group__0__Impl rule__Module__Group__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2288:2: rule__Module__Group__0__Impl rule__Module__Group__1
            {
            pushFollow(FOLLOW_rule__Module__Group__0__Impl_in_rule__Module__Group__04599);
            rule__Module__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Module__Group__1_in_rule__Module__Group__04602);
            rule__Module__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group__0"


    // $ANTLR start "rule__Module__Group__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2295:1: rule__Module__Group__0__Impl : ( 'module' ) ;
    public final void rule__Module__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2299:1: ( ( 'module' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2300:1: ( 'module' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2300:1: ( 'module' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2301:1: 'module'
            {
             before(grammarAccess.getModuleAccess().getModuleKeyword_0()); 
            match(input,22,FOLLOW_22_in_rule__Module__Group__0__Impl4630); 
             after(grammarAccess.getModuleAccess().getModuleKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group__0__Impl"


    // $ANTLR start "rule__Module__Group__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2314:1: rule__Module__Group__1 : rule__Module__Group__1__Impl rule__Module__Group__2 ;
    public final void rule__Module__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2318:1: ( rule__Module__Group__1__Impl rule__Module__Group__2 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2319:2: rule__Module__Group__1__Impl rule__Module__Group__2
            {
            pushFollow(FOLLOW_rule__Module__Group__1__Impl_in_rule__Module__Group__14661);
            rule__Module__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Module__Group__2_in_rule__Module__Group__14664);
            rule__Module__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group__1"


    // $ANTLR start "rule__Module__Group__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2326:1: rule__Module__Group__1__Impl : ( ( rule__Module__NameAssignment_1 ) ) ;
    public final void rule__Module__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2330:1: ( ( ( rule__Module__NameAssignment_1 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2331:1: ( ( rule__Module__NameAssignment_1 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2331:1: ( ( rule__Module__NameAssignment_1 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2332:1: ( rule__Module__NameAssignment_1 )
            {
             before(grammarAccess.getModuleAccess().getNameAssignment_1()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2333:1: ( rule__Module__NameAssignment_1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2333:2: rule__Module__NameAssignment_1
            {
            pushFollow(FOLLOW_rule__Module__NameAssignment_1_in_rule__Module__Group__1__Impl4691);
            rule__Module__NameAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getModuleAccess().getNameAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group__1__Impl"


    // $ANTLR start "rule__Module__Group__2"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2343:1: rule__Module__Group__2 : rule__Module__Group__2__Impl rule__Module__Group__3 ;
    public final void rule__Module__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2347:1: ( rule__Module__Group__2__Impl rule__Module__Group__3 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2348:2: rule__Module__Group__2__Impl rule__Module__Group__3
            {
            pushFollow(FOLLOW_rule__Module__Group__2__Impl_in_rule__Module__Group__24721);
            rule__Module__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Module__Group__3_in_rule__Module__Group__24724);
            rule__Module__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group__2"


    // $ANTLR start "rule__Module__Group__2__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2355:1: rule__Module__Group__2__Impl : ( '{' ) ;
    public final void rule__Module__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2359:1: ( ( '{' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2360:1: ( '{' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2360:1: ( '{' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2361:1: '{'
            {
             before(grammarAccess.getModuleAccess().getLeftCurlyBracketKeyword_2()); 
            match(input,16,FOLLOW_16_in_rule__Module__Group__2__Impl4752); 
             after(grammarAccess.getModuleAccess().getLeftCurlyBracketKeyword_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group__2__Impl"


    // $ANTLR start "rule__Module__Group__3"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2374:1: rule__Module__Group__3 : rule__Module__Group__3__Impl rule__Module__Group__4 ;
    public final void rule__Module__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2378:1: ( rule__Module__Group__3__Impl rule__Module__Group__4 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2379:2: rule__Module__Group__3__Impl rule__Module__Group__4
            {
            pushFollow(FOLLOW_rule__Module__Group__3__Impl_in_rule__Module__Group__34783);
            rule__Module__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Module__Group__4_in_rule__Module__Group__34786);
            rule__Module__Group__4();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group__3"


    // $ANTLR start "rule__Module__Group__3__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2386:1: rule__Module__Group__3__Impl : ( ( rule__Module__Group_3__0 )? ) ;
    public final void rule__Module__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2390:1: ( ( ( rule__Module__Group_3__0 )? ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2391:1: ( ( rule__Module__Group_3__0 )? )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2391:1: ( ( rule__Module__Group_3__0 )? )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2392:1: ( rule__Module__Group_3__0 )?
            {
             before(grammarAccess.getModuleAccess().getGroup_3()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2393:1: ( rule__Module__Group_3__0 )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==21) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2393:2: rule__Module__Group_3__0
                    {
                    pushFollow(FOLLOW_rule__Module__Group_3__0_in_rule__Module__Group__3__Impl4813);
                    rule__Module__Group_3__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getModuleAccess().getGroup_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group__3__Impl"


    // $ANTLR start "rule__Module__Group__4"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2403:1: rule__Module__Group__4 : rule__Module__Group__4__Impl ;
    public final void rule__Module__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2407:1: ( rule__Module__Group__4__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2408:2: rule__Module__Group__4__Impl
            {
            pushFollow(FOLLOW_rule__Module__Group__4__Impl_in_rule__Module__Group__44844);
            rule__Module__Group__4__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group__4"


    // $ANTLR start "rule__Module__Group__4__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2414:1: rule__Module__Group__4__Impl : ( '}' ) ;
    public final void rule__Module__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2418:1: ( ( '}' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2419:1: ( '}' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2419:1: ( '}' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2420:1: '}'
            {
             before(grammarAccess.getModuleAccess().getRightCurlyBracketKeyword_4()); 
            match(input,17,FOLLOW_17_in_rule__Module__Group__4__Impl4872); 
             after(grammarAccess.getModuleAccess().getRightCurlyBracketKeyword_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group__4__Impl"


    // $ANTLR start "rule__Module__Group_3__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2443:1: rule__Module__Group_3__0 : rule__Module__Group_3__0__Impl rule__Module__Group_3__1 ;
    public final void rule__Module__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2447:1: ( rule__Module__Group_3__0__Impl rule__Module__Group_3__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2448:2: rule__Module__Group_3__0__Impl rule__Module__Group_3__1
            {
            pushFollow(FOLLOW_rule__Module__Group_3__0__Impl_in_rule__Module__Group_3__04913);
            rule__Module__Group_3__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Module__Group_3__1_in_rule__Module__Group_3__04916);
            rule__Module__Group_3__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group_3__0"


    // $ANTLR start "rule__Module__Group_3__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2455:1: rule__Module__Group_3__0__Impl : ( 'operators' ) ;
    public final void rule__Module__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2459:1: ( ( 'operators' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2460:1: ( 'operators' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2460:1: ( 'operators' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2461:1: 'operators'
            {
             before(grammarAccess.getModuleAccess().getOperatorsKeyword_3_0()); 
            match(input,21,FOLLOW_21_in_rule__Module__Group_3__0__Impl4944); 
             after(grammarAccess.getModuleAccess().getOperatorsKeyword_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group_3__0__Impl"


    // $ANTLR start "rule__Module__Group_3__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2474:1: rule__Module__Group_3__1 : rule__Module__Group_3__1__Impl rule__Module__Group_3__2 ;
    public final void rule__Module__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2478:1: ( rule__Module__Group_3__1__Impl rule__Module__Group_3__2 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2479:2: rule__Module__Group_3__1__Impl rule__Module__Group_3__2
            {
            pushFollow(FOLLOW_rule__Module__Group_3__1__Impl_in_rule__Module__Group_3__14975);
            rule__Module__Group_3__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Module__Group_3__2_in_rule__Module__Group_3__14978);
            rule__Module__Group_3__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group_3__1"


    // $ANTLR start "rule__Module__Group_3__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2486:1: rule__Module__Group_3__1__Impl : ( '{' ) ;
    public final void rule__Module__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2490:1: ( ( '{' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2491:1: ( '{' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2491:1: ( '{' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2492:1: '{'
            {
             before(grammarAccess.getModuleAccess().getLeftCurlyBracketKeyword_3_1()); 
            match(input,16,FOLLOW_16_in_rule__Module__Group_3__1__Impl5006); 
             after(grammarAccess.getModuleAccess().getLeftCurlyBracketKeyword_3_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group_3__1__Impl"


    // $ANTLR start "rule__Module__Group_3__2"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2505:1: rule__Module__Group_3__2 : rule__Module__Group_3__2__Impl rule__Module__Group_3__3 ;
    public final void rule__Module__Group_3__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2509:1: ( rule__Module__Group_3__2__Impl rule__Module__Group_3__3 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2510:2: rule__Module__Group_3__2__Impl rule__Module__Group_3__3
            {
            pushFollow(FOLLOW_rule__Module__Group_3__2__Impl_in_rule__Module__Group_3__25037);
            rule__Module__Group_3__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Module__Group_3__3_in_rule__Module__Group_3__25040);
            rule__Module__Group_3__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group_3__2"


    // $ANTLR start "rule__Module__Group_3__2__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2517:1: rule__Module__Group_3__2__Impl : ( ( rule__Module__OperatorsAssignment_3_2 ) ) ;
    public final void rule__Module__Group_3__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2521:1: ( ( ( rule__Module__OperatorsAssignment_3_2 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2522:1: ( ( rule__Module__OperatorsAssignment_3_2 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2522:1: ( ( rule__Module__OperatorsAssignment_3_2 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2523:1: ( rule__Module__OperatorsAssignment_3_2 )
            {
             before(grammarAccess.getModuleAccess().getOperatorsAssignment_3_2()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2524:1: ( rule__Module__OperatorsAssignment_3_2 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2524:2: rule__Module__OperatorsAssignment_3_2
            {
            pushFollow(FOLLOW_rule__Module__OperatorsAssignment_3_2_in_rule__Module__Group_3__2__Impl5067);
            rule__Module__OperatorsAssignment_3_2();

            state._fsp--;


            }

             after(grammarAccess.getModuleAccess().getOperatorsAssignment_3_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group_3__2__Impl"


    // $ANTLR start "rule__Module__Group_3__3"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2534:1: rule__Module__Group_3__3 : rule__Module__Group_3__3__Impl rule__Module__Group_3__4 ;
    public final void rule__Module__Group_3__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2538:1: ( rule__Module__Group_3__3__Impl rule__Module__Group_3__4 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2539:2: rule__Module__Group_3__3__Impl rule__Module__Group_3__4
            {
            pushFollow(FOLLOW_rule__Module__Group_3__3__Impl_in_rule__Module__Group_3__35097);
            rule__Module__Group_3__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Module__Group_3__4_in_rule__Module__Group_3__35100);
            rule__Module__Group_3__4();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group_3__3"


    // $ANTLR start "rule__Module__Group_3__3__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2546:1: rule__Module__Group_3__3__Impl : ( ( rule__Module__Group_3_3__0 )* ) ;
    public final void rule__Module__Group_3__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2550:1: ( ( ( rule__Module__Group_3_3__0 )* ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2551:1: ( ( rule__Module__Group_3_3__0 )* )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2551:1: ( ( rule__Module__Group_3_3__0 )* )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2552:1: ( rule__Module__Group_3_3__0 )*
            {
             before(grammarAccess.getModuleAccess().getGroup_3_3()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2553:1: ( rule__Module__Group_3_3__0 )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( (LA19_0==12) ) {
                    int LA19_1 = input.LA(2);

                    if ( (LA19_1==24||LA19_1==28) ) {
                        alt19=1;
                    }


                }


                switch (alt19) {
            	case 1 :
            	    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2553:2: rule__Module__Group_3_3__0
            	    {
            	    pushFollow(FOLLOW_rule__Module__Group_3_3__0_in_rule__Module__Group_3__3__Impl5127);
            	    rule__Module__Group_3_3__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop19;
                }
            } while (true);

             after(grammarAccess.getModuleAccess().getGroup_3_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group_3__3__Impl"


    // $ANTLR start "rule__Module__Group_3__4"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2563:1: rule__Module__Group_3__4 : rule__Module__Group_3__4__Impl rule__Module__Group_3__5 ;
    public final void rule__Module__Group_3__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2567:1: ( rule__Module__Group_3__4__Impl rule__Module__Group_3__5 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2568:2: rule__Module__Group_3__4__Impl rule__Module__Group_3__5
            {
            pushFollow(FOLLOW_rule__Module__Group_3__4__Impl_in_rule__Module__Group_3__45158);
            rule__Module__Group_3__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Module__Group_3__5_in_rule__Module__Group_3__45161);
            rule__Module__Group_3__5();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group_3__4"


    // $ANTLR start "rule__Module__Group_3__4__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2575:1: rule__Module__Group_3__4__Impl : ( ';' ) ;
    public final void rule__Module__Group_3__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2579:1: ( ( ';' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2580:1: ( ';' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2580:1: ( ';' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2581:1: ';'
            {
             before(grammarAccess.getModuleAccess().getSemicolonKeyword_3_4()); 
            match(input,12,FOLLOW_12_in_rule__Module__Group_3__4__Impl5189); 
             after(grammarAccess.getModuleAccess().getSemicolonKeyword_3_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group_3__4__Impl"


    // $ANTLR start "rule__Module__Group_3__5"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2594:1: rule__Module__Group_3__5 : rule__Module__Group_3__5__Impl ;
    public final void rule__Module__Group_3__5() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2598:1: ( rule__Module__Group_3__5__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2599:2: rule__Module__Group_3__5__Impl
            {
            pushFollow(FOLLOW_rule__Module__Group_3__5__Impl_in_rule__Module__Group_3__55220);
            rule__Module__Group_3__5__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group_3__5"


    // $ANTLR start "rule__Module__Group_3__5__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2605:1: rule__Module__Group_3__5__Impl : ( '}' ) ;
    public final void rule__Module__Group_3__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2609:1: ( ( '}' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2610:1: ( '}' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2610:1: ( '}' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2611:1: '}'
            {
             before(grammarAccess.getModuleAccess().getRightCurlyBracketKeyword_3_5()); 
            match(input,17,FOLLOW_17_in_rule__Module__Group_3__5__Impl5248); 
             after(grammarAccess.getModuleAccess().getRightCurlyBracketKeyword_3_5()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group_3__5__Impl"


    // $ANTLR start "rule__Module__Group_3_3__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2636:1: rule__Module__Group_3_3__0 : rule__Module__Group_3_3__0__Impl rule__Module__Group_3_3__1 ;
    public final void rule__Module__Group_3_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2640:1: ( rule__Module__Group_3_3__0__Impl rule__Module__Group_3_3__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2641:2: rule__Module__Group_3_3__0__Impl rule__Module__Group_3_3__1
            {
            pushFollow(FOLLOW_rule__Module__Group_3_3__0__Impl_in_rule__Module__Group_3_3__05291);
            rule__Module__Group_3_3__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Module__Group_3_3__1_in_rule__Module__Group_3_3__05294);
            rule__Module__Group_3_3__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group_3_3__0"


    // $ANTLR start "rule__Module__Group_3_3__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2648:1: rule__Module__Group_3_3__0__Impl : ( ';' ) ;
    public final void rule__Module__Group_3_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2652:1: ( ( ';' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2653:1: ( ';' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2653:1: ( ';' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2654:1: ';'
            {
             before(grammarAccess.getModuleAccess().getSemicolonKeyword_3_3_0()); 
            match(input,12,FOLLOW_12_in_rule__Module__Group_3_3__0__Impl5322); 
             after(grammarAccess.getModuleAccess().getSemicolonKeyword_3_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group_3_3__0__Impl"


    // $ANTLR start "rule__Module__Group_3_3__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2667:1: rule__Module__Group_3_3__1 : rule__Module__Group_3_3__1__Impl ;
    public final void rule__Module__Group_3_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2671:1: ( rule__Module__Group_3_3__1__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2672:2: rule__Module__Group_3_3__1__Impl
            {
            pushFollow(FOLLOW_rule__Module__Group_3_3__1__Impl_in_rule__Module__Group_3_3__15353);
            rule__Module__Group_3_3__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group_3_3__1"


    // $ANTLR start "rule__Module__Group_3_3__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2678:1: rule__Module__Group_3_3__1__Impl : ( ( rule__Module__OperatorsAssignment_3_3_1 ) ) ;
    public final void rule__Module__Group_3_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2682:1: ( ( ( rule__Module__OperatorsAssignment_3_3_1 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2683:1: ( ( rule__Module__OperatorsAssignment_3_3_1 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2683:1: ( ( rule__Module__OperatorsAssignment_3_3_1 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2684:1: ( rule__Module__OperatorsAssignment_3_3_1 )
            {
             before(grammarAccess.getModuleAccess().getOperatorsAssignment_3_3_1()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2685:1: ( rule__Module__OperatorsAssignment_3_3_1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2685:2: rule__Module__OperatorsAssignment_3_3_1
            {
            pushFollow(FOLLOW_rule__Module__OperatorsAssignment_3_3_1_in_rule__Module__Group_3_3__1__Impl5380);
            rule__Module__OperatorsAssignment_3_3_1();

            state._fsp--;


            }

             after(grammarAccess.getModuleAccess().getOperatorsAssignment_3_3_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__Group_3_3__1__Impl"


    // $ANTLR start "rule__Terminal__Group__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2699:1: rule__Terminal__Group__0 : rule__Terminal__Group__0__Impl rule__Terminal__Group__1 ;
    public final void rule__Terminal__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2703:1: ( rule__Terminal__Group__0__Impl rule__Terminal__Group__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2704:2: rule__Terminal__Group__0__Impl rule__Terminal__Group__1
            {
            pushFollow(FOLLOW_rule__Terminal__Group__0__Impl_in_rule__Terminal__Group__05414);
            rule__Terminal__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Terminal__Group__1_in_rule__Terminal__Group__05417);
            rule__Terminal__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Terminal__Group__0"


    // $ANTLR start "rule__Terminal__Group__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2711:1: rule__Terminal__Group__0__Impl : ( ( rule__Terminal__NameAssignment_0 ) ) ;
    public final void rule__Terminal__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2715:1: ( ( ( rule__Terminal__NameAssignment_0 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2716:1: ( ( rule__Terminal__NameAssignment_0 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2716:1: ( ( rule__Terminal__NameAssignment_0 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2717:1: ( rule__Terminal__NameAssignment_0 )
            {
             before(grammarAccess.getTerminalAccess().getNameAssignment_0()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2718:1: ( rule__Terminal__NameAssignment_0 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2718:2: rule__Terminal__NameAssignment_0
            {
            pushFollow(FOLLOW_rule__Terminal__NameAssignment_0_in_rule__Terminal__Group__0__Impl5444);
            rule__Terminal__NameAssignment_0();

            state._fsp--;


            }

             after(grammarAccess.getTerminalAccess().getNameAssignment_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Terminal__Group__0__Impl"


    // $ANTLR start "rule__Terminal__Group__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2728:1: rule__Terminal__Group__1 : rule__Terminal__Group__1__Impl rule__Terminal__Group__2 ;
    public final void rule__Terminal__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2732:1: ( rule__Terminal__Group__1__Impl rule__Terminal__Group__2 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2733:2: rule__Terminal__Group__1__Impl rule__Terminal__Group__2
            {
            pushFollow(FOLLOW_rule__Terminal__Group__1__Impl_in_rule__Terminal__Group__15474);
            rule__Terminal__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Terminal__Group__2_in_rule__Terminal__Group__15477);
            rule__Terminal__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Terminal__Group__1"


    // $ANTLR start "rule__Terminal__Group__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2740:1: rule__Terminal__Group__1__Impl : ( ':' ) ;
    public final void rule__Terminal__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2744:1: ( ( ':' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2745:1: ( ':' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2745:1: ( ':' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2746:1: ':'
            {
             before(grammarAccess.getTerminalAccess().getColonKeyword_1()); 
            match(input,23,FOLLOW_23_in_rule__Terminal__Group__1__Impl5505); 
             after(grammarAccess.getTerminalAccess().getColonKeyword_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Terminal__Group__1__Impl"


    // $ANTLR start "rule__Terminal__Group__2"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2759:1: rule__Terminal__Group__2 : rule__Terminal__Group__2__Impl rule__Terminal__Group__3 ;
    public final void rule__Terminal__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2763:1: ( rule__Terminal__Group__2__Impl rule__Terminal__Group__3 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2764:2: rule__Terminal__Group__2__Impl rule__Terminal__Group__3
            {
            pushFollow(FOLLOW_rule__Terminal__Group__2__Impl_in_rule__Terminal__Group__25536);
            rule__Terminal__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Terminal__Group__3_in_rule__Terminal__Group__25539);
            rule__Terminal__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Terminal__Group__2"


    // $ANTLR start "rule__Terminal__Group__2__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2771:1: rule__Terminal__Group__2__Impl : ( ( rule__Terminal__ClassAssignment_2 ) ) ;
    public final void rule__Terminal__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2775:1: ( ( ( rule__Terminal__ClassAssignment_2 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2776:1: ( ( rule__Terminal__ClassAssignment_2 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2776:1: ( ( rule__Terminal__ClassAssignment_2 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2777:1: ( rule__Terminal__ClassAssignment_2 )
            {
             before(grammarAccess.getTerminalAccess().getClassAssignment_2()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2778:1: ( rule__Terminal__ClassAssignment_2 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2778:2: rule__Terminal__ClassAssignment_2
            {
            pushFollow(FOLLOW_rule__Terminal__ClassAssignment_2_in_rule__Terminal__Group__2__Impl5566);
            rule__Terminal__ClassAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getTerminalAccess().getClassAssignment_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Terminal__Group__2__Impl"


    // $ANTLR start "rule__Terminal__Group__3"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2788:1: rule__Terminal__Group__3 : rule__Terminal__Group__3__Impl ;
    public final void rule__Terminal__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2792:1: ( rule__Terminal__Group__3__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2793:2: rule__Terminal__Group__3__Impl
            {
            pushFollow(FOLLOW_rule__Terminal__Group__3__Impl_in_rule__Terminal__Group__35596);
            rule__Terminal__Group__3__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Terminal__Group__3"


    // $ANTLR start "rule__Terminal__Group__3__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2799:1: rule__Terminal__Group__3__Impl : ( ( rule__Terminal__ManyAssignment_3 )? ) ;
    public final void rule__Terminal__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2803:1: ( ( ( rule__Terminal__ManyAssignment_3 )? ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2804:1: ( ( rule__Terminal__ManyAssignment_3 )? )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2804:1: ( ( rule__Terminal__ManyAssignment_3 )? )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2805:1: ( rule__Terminal__ManyAssignment_3 )?
            {
             before(grammarAccess.getTerminalAccess().getManyAssignment_3()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2806:1: ( rule__Terminal__ManyAssignment_3 )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==35) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2806:2: rule__Terminal__ManyAssignment_3
                    {
                    pushFollow(FOLLOW_rule__Terminal__ManyAssignment_3_in_rule__Terminal__Group__3__Impl5623);
                    rule__Terminal__ManyAssignment_3();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getTerminalAccess().getManyAssignment_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Terminal__Group__3__Impl"


    // $ANTLR start "rule__AliasOperator__Group__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2824:1: rule__AliasOperator__Group__0 : rule__AliasOperator__Group__0__Impl rule__AliasOperator__Group__1 ;
    public final void rule__AliasOperator__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2828:1: ( rule__AliasOperator__Group__0__Impl rule__AliasOperator__Group__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2829:2: rule__AliasOperator__Group__0__Impl rule__AliasOperator__Group__1
            {
            pushFollow(FOLLOW_rule__AliasOperator__Group__0__Impl_in_rule__AliasOperator__Group__05662);
            rule__AliasOperator__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__AliasOperator__Group__1_in_rule__AliasOperator__Group__05665);
            rule__AliasOperator__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AliasOperator__Group__0"


    // $ANTLR start "rule__AliasOperator__Group__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2836:1: rule__AliasOperator__Group__0__Impl : ( 'alias' ) ;
    public final void rule__AliasOperator__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2840:1: ( ( 'alias' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2841:1: ( 'alias' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2841:1: ( 'alias' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2842:1: 'alias'
            {
             before(grammarAccess.getAliasOperatorAccess().getAliasKeyword_0()); 
            match(input,24,FOLLOW_24_in_rule__AliasOperator__Group__0__Impl5693); 
             after(grammarAccess.getAliasOperatorAccess().getAliasKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AliasOperator__Group__0__Impl"


    // $ANTLR start "rule__AliasOperator__Group__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2855:1: rule__AliasOperator__Group__1 : rule__AliasOperator__Group__1__Impl rule__AliasOperator__Group__2 ;
    public final void rule__AliasOperator__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2859:1: ( rule__AliasOperator__Group__1__Impl rule__AliasOperator__Group__2 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2860:2: rule__AliasOperator__Group__1__Impl rule__AliasOperator__Group__2
            {
            pushFollow(FOLLOW_rule__AliasOperator__Group__1__Impl_in_rule__AliasOperator__Group__15724);
            rule__AliasOperator__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__AliasOperator__Group__2_in_rule__AliasOperator__Group__15727);
            rule__AliasOperator__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AliasOperator__Group__1"


    // $ANTLR start "rule__AliasOperator__Group__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2867:1: rule__AliasOperator__Group__1__Impl : ( ( rule__AliasOperator__NameAssignment_1 ) ) ;
    public final void rule__AliasOperator__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2871:1: ( ( ( rule__AliasOperator__NameAssignment_1 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2872:1: ( ( rule__AliasOperator__NameAssignment_1 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2872:1: ( ( rule__AliasOperator__NameAssignment_1 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2873:1: ( rule__AliasOperator__NameAssignment_1 )
            {
             before(grammarAccess.getAliasOperatorAccess().getNameAssignment_1()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2874:1: ( rule__AliasOperator__NameAssignment_1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2874:2: rule__AliasOperator__NameAssignment_1
            {
            pushFollow(FOLLOW_rule__AliasOperator__NameAssignment_1_in_rule__AliasOperator__Group__1__Impl5754);
            rule__AliasOperator__NameAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getAliasOperatorAccess().getNameAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AliasOperator__Group__1__Impl"


    // $ANTLR start "rule__AliasOperator__Group__2"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2884:1: rule__AliasOperator__Group__2 : rule__AliasOperator__Group__2__Impl rule__AliasOperator__Group__3 ;
    public final void rule__AliasOperator__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2888:1: ( rule__AliasOperator__Group__2__Impl rule__AliasOperator__Group__3 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2889:2: rule__AliasOperator__Group__2__Impl rule__AliasOperator__Group__3
            {
            pushFollow(FOLLOW_rule__AliasOperator__Group__2__Impl_in_rule__AliasOperator__Group__25784);
            rule__AliasOperator__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__AliasOperator__Group__3_in_rule__AliasOperator__Group__25787);
            rule__AliasOperator__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AliasOperator__Group__2"


    // $ANTLR start "rule__AliasOperator__Group__2__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2896:1: rule__AliasOperator__Group__2__Impl : ( '::' ) ;
    public final void rule__AliasOperator__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2900:1: ( ( '::' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2901:1: ( '::' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2901:1: ( '::' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2902:1: '::'
            {
             before(grammarAccess.getAliasOperatorAccess().getColonColonKeyword_2()); 
            match(input,25,FOLLOW_25_in_rule__AliasOperator__Group__2__Impl5815); 
             after(grammarAccess.getAliasOperatorAccess().getColonColonKeyword_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AliasOperator__Group__2__Impl"


    // $ANTLR start "rule__AliasOperator__Group__3"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2915:1: rule__AliasOperator__Group__3 : rule__AliasOperator__Group__3__Impl rule__AliasOperator__Group__4 ;
    public final void rule__AliasOperator__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2919:1: ( rule__AliasOperator__Group__3__Impl rule__AliasOperator__Group__4 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2920:2: rule__AliasOperator__Group__3__Impl rule__AliasOperator__Group__4
            {
            pushFollow(FOLLOW_rule__AliasOperator__Group__3__Impl_in_rule__AliasOperator__Group__35846);
            rule__AliasOperator__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__AliasOperator__Group__4_in_rule__AliasOperator__Group__35849);
            rule__AliasOperator__Group__4();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AliasOperator__Group__3"


    // $ANTLR start "rule__AliasOperator__Group__3__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2927:1: rule__AliasOperator__Group__3__Impl : ( ( rule__AliasOperator__OpAssignment_3 ) ) ;
    public final void rule__AliasOperator__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2931:1: ( ( ( rule__AliasOperator__OpAssignment_3 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2932:1: ( ( rule__AliasOperator__OpAssignment_3 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2932:1: ( ( rule__AliasOperator__OpAssignment_3 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2933:1: ( rule__AliasOperator__OpAssignment_3 )
            {
             before(grammarAccess.getAliasOperatorAccess().getOpAssignment_3()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2934:1: ( rule__AliasOperator__OpAssignment_3 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2934:2: rule__AliasOperator__OpAssignment_3
            {
            pushFollow(FOLLOW_rule__AliasOperator__OpAssignment_3_in_rule__AliasOperator__Group__3__Impl5876);
            rule__AliasOperator__OpAssignment_3();

            state._fsp--;


            }

             after(grammarAccess.getAliasOperatorAccess().getOpAssignment_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AliasOperator__Group__3__Impl"


    // $ANTLR start "rule__AliasOperator__Group__4"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2944:1: rule__AliasOperator__Group__4 : rule__AliasOperator__Group__4__Impl rule__AliasOperator__Group__5 ;
    public final void rule__AliasOperator__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2948:1: ( rule__AliasOperator__Group__4__Impl rule__AliasOperator__Group__5 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2949:2: rule__AliasOperator__Group__4__Impl rule__AliasOperator__Group__5
            {
            pushFollow(FOLLOW_rule__AliasOperator__Group__4__Impl_in_rule__AliasOperator__Group__45906);
            rule__AliasOperator__Group__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__AliasOperator__Group__5_in_rule__AliasOperator__Group__45909);
            rule__AliasOperator__Group__5();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AliasOperator__Group__4"


    // $ANTLR start "rule__AliasOperator__Group__4__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2956:1: rule__AliasOperator__Group__4__Impl : ( '(' ) ;
    public final void rule__AliasOperator__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2960:1: ( ( '(' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2961:1: ( '(' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2961:1: ( '(' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2962:1: '('
            {
             before(grammarAccess.getAliasOperatorAccess().getLeftParenthesisKeyword_4()); 
            match(input,26,FOLLOW_26_in_rule__AliasOperator__Group__4__Impl5937); 
             after(grammarAccess.getAliasOperatorAccess().getLeftParenthesisKeyword_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AliasOperator__Group__4__Impl"


    // $ANTLR start "rule__AliasOperator__Group__5"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2975:1: rule__AliasOperator__Group__5 : rule__AliasOperator__Group__5__Impl rule__AliasOperator__Group__6 ;
    public final void rule__AliasOperator__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2979:1: ( rule__AliasOperator__Group__5__Impl rule__AliasOperator__Group__6 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2980:2: rule__AliasOperator__Group__5__Impl rule__AliasOperator__Group__6
            {
            pushFollow(FOLLOW_rule__AliasOperator__Group__5__Impl_in_rule__AliasOperator__Group__55968);
            rule__AliasOperator__Group__5__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__AliasOperator__Group__6_in_rule__AliasOperator__Group__55971);
            rule__AliasOperator__Group__6();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AliasOperator__Group__5"


    // $ANTLR start "rule__AliasOperator__Group__5__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2987:1: rule__AliasOperator__Group__5__Impl : ( ( rule__AliasOperator__NodesAssignment_5 ) ) ;
    public final void rule__AliasOperator__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2991:1: ( ( ( rule__AliasOperator__NodesAssignment_5 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2992:1: ( ( rule__AliasOperator__NodesAssignment_5 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2992:1: ( ( rule__AliasOperator__NodesAssignment_5 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2993:1: ( rule__AliasOperator__NodesAssignment_5 )
            {
             before(grammarAccess.getAliasOperatorAccess().getNodesAssignment_5()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2994:1: ( rule__AliasOperator__NodesAssignment_5 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:2994:2: rule__AliasOperator__NodesAssignment_5
            {
            pushFollow(FOLLOW_rule__AliasOperator__NodesAssignment_5_in_rule__AliasOperator__Group__5__Impl5998);
            rule__AliasOperator__NodesAssignment_5();

            state._fsp--;


            }

             after(grammarAccess.getAliasOperatorAccess().getNodesAssignment_5()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AliasOperator__Group__5__Impl"


    // $ANTLR start "rule__AliasOperator__Group__6"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3004:1: rule__AliasOperator__Group__6 : rule__AliasOperator__Group__6__Impl rule__AliasOperator__Group__7 ;
    public final void rule__AliasOperator__Group__6() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3008:1: ( rule__AliasOperator__Group__6__Impl rule__AliasOperator__Group__7 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3009:2: rule__AliasOperator__Group__6__Impl rule__AliasOperator__Group__7
            {
            pushFollow(FOLLOW_rule__AliasOperator__Group__6__Impl_in_rule__AliasOperator__Group__66028);
            rule__AliasOperator__Group__6__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__AliasOperator__Group__7_in_rule__AliasOperator__Group__66031);
            rule__AliasOperator__Group__7();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AliasOperator__Group__6"


    // $ANTLR start "rule__AliasOperator__Group__6__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3016:1: rule__AliasOperator__Group__6__Impl : ( ( rule__AliasOperator__Group_6__0 )* ) ;
    public final void rule__AliasOperator__Group__6__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3020:1: ( ( ( rule__AliasOperator__Group_6__0 )* ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3021:1: ( ( rule__AliasOperator__Group_6__0 )* )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3021:1: ( ( rule__AliasOperator__Group_6__0 )* )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3022:1: ( rule__AliasOperator__Group_6__0 )*
            {
             before(grammarAccess.getAliasOperatorAccess().getGroup_6()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3023:1: ( rule__AliasOperator__Group_6__0 )*
            loop21:
            do {
                int alt21=2;
                int LA21_0 = input.LA(1);

                if ( (LA21_0==19) ) {
                    alt21=1;
                }


                switch (alt21) {
            	case 1 :
            	    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3023:2: rule__AliasOperator__Group_6__0
            	    {
            	    pushFollow(FOLLOW_rule__AliasOperator__Group_6__0_in_rule__AliasOperator__Group__6__Impl6058);
            	    rule__AliasOperator__Group_6__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop21;
                }
            } while (true);

             after(grammarAccess.getAliasOperatorAccess().getGroup_6()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AliasOperator__Group__6__Impl"


    // $ANTLR start "rule__AliasOperator__Group__7"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3033:1: rule__AliasOperator__Group__7 : rule__AliasOperator__Group__7__Impl ;
    public final void rule__AliasOperator__Group__7() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3037:1: ( rule__AliasOperator__Group__7__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3038:2: rule__AliasOperator__Group__7__Impl
            {
            pushFollow(FOLLOW_rule__AliasOperator__Group__7__Impl_in_rule__AliasOperator__Group__76089);
            rule__AliasOperator__Group__7__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AliasOperator__Group__7"


    // $ANTLR start "rule__AliasOperator__Group__7__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3044:1: rule__AliasOperator__Group__7__Impl : ( ')' ) ;
    public final void rule__AliasOperator__Group__7__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3048:1: ( ( ')' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3049:1: ( ')' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3049:1: ( ')' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3050:1: ')'
            {
             before(grammarAccess.getAliasOperatorAccess().getRightParenthesisKeyword_7()); 
            match(input,27,FOLLOW_27_in_rule__AliasOperator__Group__7__Impl6117); 
             after(grammarAccess.getAliasOperatorAccess().getRightParenthesisKeyword_7()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AliasOperator__Group__7__Impl"


    // $ANTLR start "rule__AliasOperator__Group_6__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3079:1: rule__AliasOperator__Group_6__0 : rule__AliasOperator__Group_6__0__Impl rule__AliasOperator__Group_6__1 ;
    public final void rule__AliasOperator__Group_6__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3083:1: ( rule__AliasOperator__Group_6__0__Impl rule__AliasOperator__Group_6__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3084:2: rule__AliasOperator__Group_6__0__Impl rule__AliasOperator__Group_6__1
            {
            pushFollow(FOLLOW_rule__AliasOperator__Group_6__0__Impl_in_rule__AliasOperator__Group_6__06164);
            rule__AliasOperator__Group_6__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__AliasOperator__Group_6__1_in_rule__AliasOperator__Group_6__06167);
            rule__AliasOperator__Group_6__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AliasOperator__Group_6__0"


    // $ANTLR start "rule__AliasOperator__Group_6__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3091:1: rule__AliasOperator__Group_6__0__Impl : ( ',' ) ;
    public final void rule__AliasOperator__Group_6__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3095:1: ( ( ',' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3096:1: ( ',' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3096:1: ( ',' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3097:1: ','
            {
             before(grammarAccess.getAliasOperatorAccess().getCommaKeyword_6_0()); 
            match(input,19,FOLLOW_19_in_rule__AliasOperator__Group_6__0__Impl6195); 
             after(grammarAccess.getAliasOperatorAccess().getCommaKeyword_6_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AliasOperator__Group_6__0__Impl"


    // $ANTLR start "rule__AliasOperator__Group_6__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3110:1: rule__AliasOperator__Group_6__1 : rule__AliasOperator__Group_6__1__Impl ;
    public final void rule__AliasOperator__Group_6__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3114:1: ( rule__AliasOperator__Group_6__1__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3115:2: rule__AliasOperator__Group_6__1__Impl
            {
            pushFollow(FOLLOW_rule__AliasOperator__Group_6__1__Impl_in_rule__AliasOperator__Group_6__16226);
            rule__AliasOperator__Group_6__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AliasOperator__Group_6__1"


    // $ANTLR start "rule__AliasOperator__Group_6__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3121:1: rule__AliasOperator__Group_6__1__Impl : ( ( rule__AliasOperator__NodesAssignment_6_1 ) ) ;
    public final void rule__AliasOperator__Group_6__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3125:1: ( ( ( rule__AliasOperator__NodesAssignment_6_1 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3126:1: ( ( rule__AliasOperator__NodesAssignment_6_1 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3126:1: ( ( rule__AliasOperator__NodesAssignment_6_1 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3127:1: ( rule__AliasOperator__NodesAssignment_6_1 )
            {
             before(grammarAccess.getAliasOperatorAccess().getNodesAssignment_6_1()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3128:1: ( rule__AliasOperator__NodesAssignment_6_1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3128:2: rule__AliasOperator__NodesAssignment_6_1
            {
            pushFollow(FOLLOW_rule__AliasOperator__NodesAssignment_6_1_in_rule__AliasOperator__Group_6__1__Impl6253);
            rule__AliasOperator__NodesAssignment_6_1();

            state._fsp--;


            }

             after(grammarAccess.getAliasOperatorAccess().getNodesAssignment_6_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AliasOperator__Group_6__1__Impl"


    // $ANTLR start "rule__OperatorNode__Group__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3142:1: rule__OperatorNode__Group__0 : rule__OperatorNode__Group__0__Impl rule__OperatorNode__Group__1 ;
    public final void rule__OperatorNode__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3146:1: ( rule__OperatorNode__Group__0__Impl rule__OperatorNode__Group__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3147:2: rule__OperatorNode__Group__0__Impl rule__OperatorNode__Group__1
            {
            pushFollow(FOLLOW_rule__OperatorNode__Group__0__Impl_in_rule__OperatorNode__Group__06287);
            rule__OperatorNode__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__OperatorNode__Group__1_in_rule__OperatorNode__Group__06290);
            rule__OperatorNode__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorNode__Group__0"


    // $ANTLR start "rule__OperatorNode__Group__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3154:1: rule__OperatorNode__Group__0__Impl : ( ( rule__OperatorNode__OpAssignment_0 ) ) ;
    public final void rule__OperatorNode__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3158:1: ( ( ( rule__OperatorNode__OpAssignment_0 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3159:1: ( ( rule__OperatorNode__OpAssignment_0 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3159:1: ( ( rule__OperatorNode__OpAssignment_0 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3160:1: ( rule__OperatorNode__OpAssignment_0 )
            {
             before(grammarAccess.getOperatorNodeAccess().getOpAssignment_0()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3161:1: ( rule__OperatorNode__OpAssignment_0 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3161:2: rule__OperatorNode__OpAssignment_0
            {
            pushFollow(FOLLOW_rule__OperatorNode__OpAssignment_0_in_rule__OperatorNode__Group__0__Impl6317);
            rule__OperatorNode__OpAssignment_0();

            state._fsp--;


            }

             after(grammarAccess.getOperatorNodeAccess().getOpAssignment_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorNode__Group__0__Impl"


    // $ANTLR start "rule__OperatorNode__Group__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3171:1: rule__OperatorNode__Group__1 : rule__OperatorNode__Group__1__Impl rule__OperatorNode__Group__2 ;
    public final void rule__OperatorNode__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3175:1: ( rule__OperatorNode__Group__1__Impl rule__OperatorNode__Group__2 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3176:2: rule__OperatorNode__Group__1__Impl rule__OperatorNode__Group__2
            {
            pushFollow(FOLLOW_rule__OperatorNode__Group__1__Impl_in_rule__OperatorNode__Group__16347);
            rule__OperatorNode__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__OperatorNode__Group__2_in_rule__OperatorNode__Group__16350);
            rule__OperatorNode__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorNode__Group__1"


    // $ANTLR start "rule__OperatorNode__Group__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3183:1: rule__OperatorNode__Group__1__Impl : ( '(' ) ;
    public final void rule__OperatorNode__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3187:1: ( ( '(' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3188:1: ( '(' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3188:1: ( '(' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3189:1: '('
            {
             before(grammarAccess.getOperatorNodeAccess().getLeftParenthesisKeyword_1()); 
            match(input,26,FOLLOW_26_in_rule__OperatorNode__Group__1__Impl6378); 
             after(grammarAccess.getOperatorNodeAccess().getLeftParenthesisKeyword_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorNode__Group__1__Impl"


    // $ANTLR start "rule__OperatorNode__Group__2"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3202:1: rule__OperatorNode__Group__2 : rule__OperatorNode__Group__2__Impl rule__OperatorNode__Group__3 ;
    public final void rule__OperatorNode__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3206:1: ( rule__OperatorNode__Group__2__Impl rule__OperatorNode__Group__3 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3207:2: rule__OperatorNode__Group__2__Impl rule__OperatorNode__Group__3
            {
            pushFollow(FOLLOW_rule__OperatorNode__Group__2__Impl_in_rule__OperatorNode__Group__26409);
            rule__OperatorNode__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__OperatorNode__Group__3_in_rule__OperatorNode__Group__26412);
            rule__OperatorNode__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorNode__Group__2"


    // $ANTLR start "rule__OperatorNode__Group__2__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3214:1: rule__OperatorNode__Group__2__Impl : ( ( rule__OperatorNode__NodesAssignment_2 ) ) ;
    public final void rule__OperatorNode__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3218:1: ( ( ( rule__OperatorNode__NodesAssignment_2 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3219:1: ( ( rule__OperatorNode__NodesAssignment_2 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3219:1: ( ( rule__OperatorNode__NodesAssignment_2 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3220:1: ( rule__OperatorNode__NodesAssignment_2 )
            {
             before(grammarAccess.getOperatorNodeAccess().getNodesAssignment_2()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3221:1: ( rule__OperatorNode__NodesAssignment_2 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3221:2: rule__OperatorNode__NodesAssignment_2
            {
            pushFollow(FOLLOW_rule__OperatorNode__NodesAssignment_2_in_rule__OperatorNode__Group__2__Impl6439);
            rule__OperatorNode__NodesAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getOperatorNodeAccess().getNodesAssignment_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorNode__Group__2__Impl"


    // $ANTLR start "rule__OperatorNode__Group__3"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3231:1: rule__OperatorNode__Group__3 : rule__OperatorNode__Group__3__Impl rule__OperatorNode__Group__4 ;
    public final void rule__OperatorNode__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3235:1: ( rule__OperatorNode__Group__3__Impl rule__OperatorNode__Group__4 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3236:2: rule__OperatorNode__Group__3__Impl rule__OperatorNode__Group__4
            {
            pushFollow(FOLLOW_rule__OperatorNode__Group__3__Impl_in_rule__OperatorNode__Group__36469);
            rule__OperatorNode__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__OperatorNode__Group__4_in_rule__OperatorNode__Group__36472);
            rule__OperatorNode__Group__4();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorNode__Group__3"


    // $ANTLR start "rule__OperatorNode__Group__3__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3243:1: rule__OperatorNode__Group__3__Impl : ( ( rule__OperatorNode__Group_3__0 )* ) ;
    public final void rule__OperatorNode__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3247:1: ( ( ( rule__OperatorNode__Group_3__0 )* ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3248:1: ( ( rule__OperatorNode__Group_3__0 )* )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3248:1: ( ( rule__OperatorNode__Group_3__0 )* )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3249:1: ( rule__OperatorNode__Group_3__0 )*
            {
             before(grammarAccess.getOperatorNodeAccess().getGroup_3()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3250:1: ( rule__OperatorNode__Group_3__0 )*
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( (LA22_0==19) ) {
                    alt22=1;
                }


                switch (alt22) {
            	case 1 :
            	    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3250:2: rule__OperatorNode__Group_3__0
            	    {
            	    pushFollow(FOLLOW_rule__OperatorNode__Group_3__0_in_rule__OperatorNode__Group__3__Impl6499);
            	    rule__OperatorNode__Group_3__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);

             after(grammarAccess.getOperatorNodeAccess().getGroup_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorNode__Group__3__Impl"


    // $ANTLR start "rule__OperatorNode__Group__4"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3260:1: rule__OperatorNode__Group__4 : rule__OperatorNode__Group__4__Impl ;
    public final void rule__OperatorNode__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3264:1: ( rule__OperatorNode__Group__4__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3265:2: rule__OperatorNode__Group__4__Impl
            {
            pushFollow(FOLLOW_rule__OperatorNode__Group__4__Impl_in_rule__OperatorNode__Group__46530);
            rule__OperatorNode__Group__4__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorNode__Group__4"


    // $ANTLR start "rule__OperatorNode__Group__4__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3271:1: rule__OperatorNode__Group__4__Impl : ( ')' ) ;
    public final void rule__OperatorNode__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3275:1: ( ( ')' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3276:1: ( ')' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3276:1: ( ')' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3277:1: ')'
            {
             before(grammarAccess.getOperatorNodeAccess().getRightParenthesisKeyword_4()); 
            match(input,27,FOLLOW_27_in_rule__OperatorNode__Group__4__Impl6558); 
             after(grammarAccess.getOperatorNodeAccess().getRightParenthesisKeyword_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorNode__Group__4__Impl"


    // $ANTLR start "rule__OperatorNode__Group_3__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3300:1: rule__OperatorNode__Group_3__0 : rule__OperatorNode__Group_3__0__Impl rule__OperatorNode__Group_3__1 ;
    public final void rule__OperatorNode__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3304:1: ( rule__OperatorNode__Group_3__0__Impl rule__OperatorNode__Group_3__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3305:2: rule__OperatorNode__Group_3__0__Impl rule__OperatorNode__Group_3__1
            {
            pushFollow(FOLLOW_rule__OperatorNode__Group_3__0__Impl_in_rule__OperatorNode__Group_3__06599);
            rule__OperatorNode__Group_3__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__OperatorNode__Group_3__1_in_rule__OperatorNode__Group_3__06602);
            rule__OperatorNode__Group_3__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorNode__Group_3__0"


    // $ANTLR start "rule__OperatorNode__Group_3__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3312:1: rule__OperatorNode__Group_3__0__Impl : ( ',' ) ;
    public final void rule__OperatorNode__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3316:1: ( ( ',' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3317:1: ( ',' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3317:1: ( ',' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3318:1: ','
            {
             before(grammarAccess.getOperatorNodeAccess().getCommaKeyword_3_0()); 
            match(input,19,FOLLOW_19_in_rule__OperatorNode__Group_3__0__Impl6630); 
             after(grammarAccess.getOperatorNodeAccess().getCommaKeyword_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorNode__Group_3__0__Impl"


    // $ANTLR start "rule__OperatorNode__Group_3__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3331:1: rule__OperatorNode__Group_3__1 : rule__OperatorNode__Group_3__1__Impl ;
    public final void rule__OperatorNode__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3335:1: ( rule__OperatorNode__Group_3__1__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3336:2: rule__OperatorNode__Group_3__1__Impl
            {
            pushFollow(FOLLOW_rule__OperatorNode__Group_3__1__Impl_in_rule__OperatorNode__Group_3__16661);
            rule__OperatorNode__Group_3__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorNode__Group_3__1"


    // $ANTLR start "rule__OperatorNode__Group_3__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3342:1: rule__OperatorNode__Group_3__1__Impl : ( ( rule__OperatorNode__NodesAssignment_3_1 ) ) ;
    public final void rule__OperatorNode__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3346:1: ( ( ( rule__OperatorNode__NodesAssignment_3_1 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3347:1: ( ( rule__OperatorNode__NodesAssignment_3_1 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3347:1: ( ( rule__OperatorNode__NodesAssignment_3_1 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3348:1: ( rule__OperatorNode__NodesAssignment_3_1 )
            {
             before(grammarAccess.getOperatorNodeAccess().getNodesAssignment_3_1()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3349:1: ( rule__OperatorNode__NodesAssignment_3_1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3349:2: rule__OperatorNode__NodesAssignment_3_1
            {
            pushFollow(FOLLOW_rule__OperatorNode__NodesAssignment_3_1_in_rule__OperatorNode__Group_3__1__Impl6688);
            rule__OperatorNode__NodesAssignment_3_1();

            state._fsp--;


            }

             after(grammarAccess.getOperatorNodeAccess().getNodesAssignment_3_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorNode__Group_3__1__Impl"


    // $ANTLR start "rule__ClassOperator__Group__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3363:1: rule__ClassOperator__Group__0 : rule__ClassOperator__Group__0__Impl rule__ClassOperator__Group__1 ;
    public final void rule__ClassOperator__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3367:1: ( rule__ClassOperator__Group__0__Impl rule__ClassOperator__Group__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3368:2: rule__ClassOperator__Group__0__Impl rule__ClassOperator__Group__1
            {
            pushFollow(FOLLOW_rule__ClassOperator__Group__0__Impl_in_rule__ClassOperator__Group__06722);
            rule__ClassOperator__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__ClassOperator__Group__1_in_rule__ClassOperator__Group__06725);
            rule__ClassOperator__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperator__Group__0"


    // $ANTLR start "rule__ClassOperator__Group__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3375:1: rule__ClassOperator__Group__0__Impl : ( 'op' ) ;
    public final void rule__ClassOperator__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3379:1: ( ( 'op' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3380:1: ( 'op' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3380:1: ( 'op' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3381:1: 'op'
            {
             before(grammarAccess.getClassOperatorAccess().getOpKeyword_0()); 
            match(input,28,FOLLOW_28_in_rule__ClassOperator__Group__0__Impl6753); 
             after(grammarAccess.getClassOperatorAccess().getOpKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperator__Group__0__Impl"


    // $ANTLR start "rule__ClassOperator__Group__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3394:1: rule__ClassOperator__Group__1 : rule__ClassOperator__Group__1__Impl rule__ClassOperator__Group__2 ;
    public final void rule__ClassOperator__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3398:1: ( rule__ClassOperator__Group__1__Impl rule__ClassOperator__Group__2 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3399:2: rule__ClassOperator__Group__1__Impl rule__ClassOperator__Group__2
            {
            pushFollow(FOLLOW_rule__ClassOperator__Group__1__Impl_in_rule__ClassOperator__Group__16784);
            rule__ClassOperator__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__ClassOperator__Group__2_in_rule__ClassOperator__Group__16787);
            rule__ClassOperator__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperator__Group__1"


    // $ANTLR start "rule__ClassOperator__Group__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3406:1: rule__ClassOperator__Group__1__Impl : ( ( rule__ClassOperator__NameAssignment_1 ) ) ;
    public final void rule__ClassOperator__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3410:1: ( ( ( rule__ClassOperator__NameAssignment_1 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3411:1: ( ( rule__ClassOperator__NameAssignment_1 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3411:1: ( ( rule__ClassOperator__NameAssignment_1 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3412:1: ( rule__ClassOperator__NameAssignment_1 )
            {
             before(grammarAccess.getClassOperatorAccess().getNameAssignment_1()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3413:1: ( rule__ClassOperator__NameAssignment_1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3413:2: rule__ClassOperator__NameAssignment_1
            {
            pushFollow(FOLLOW_rule__ClassOperator__NameAssignment_1_in_rule__ClassOperator__Group__1__Impl6814);
            rule__ClassOperator__NameAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getClassOperatorAccess().getNameAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperator__Group__1__Impl"


    // $ANTLR start "rule__ClassOperator__Group__2"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3423:1: rule__ClassOperator__Group__2 : rule__ClassOperator__Group__2__Impl rule__ClassOperator__Group__3 ;
    public final void rule__ClassOperator__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3427:1: ( rule__ClassOperator__Group__2__Impl rule__ClassOperator__Group__3 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3428:2: rule__ClassOperator__Group__2__Impl rule__ClassOperator__Group__3
            {
            pushFollow(FOLLOW_rule__ClassOperator__Group__2__Impl_in_rule__ClassOperator__Group__26844);
            rule__ClassOperator__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__ClassOperator__Group__3_in_rule__ClassOperator__Group__26847);
            rule__ClassOperator__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperator__Group__2"


    // $ANTLR start "rule__ClassOperator__Group__2__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3435:1: rule__ClassOperator__Group__2__Impl : ( '::' ) ;
    public final void rule__ClassOperator__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3439:1: ( ( '::' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3440:1: ( '::' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3440:1: ( '::' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3441:1: '::'
            {
             before(grammarAccess.getClassOperatorAccess().getColonColonKeyword_2()); 
            match(input,25,FOLLOW_25_in_rule__ClassOperator__Group__2__Impl6875); 
             after(grammarAccess.getClassOperatorAccess().getColonColonKeyword_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperator__Group__2__Impl"


    // $ANTLR start "rule__ClassOperator__Group__3"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3454:1: rule__ClassOperator__Group__3 : rule__ClassOperator__Group__3__Impl ;
    public final void rule__ClassOperator__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3458:1: ( rule__ClassOperator__Group__3__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3459:2: rule__ClassOperator__Group__3__Impl
            {
            pushFollow(FOLLOW_rule__ClassOperator__Group__3__Impl_in_rule__ClassOperator__Group__36906);
            rule__ClassOperator__Group__3__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperator__Group__3"


    // $ANTLR start "rule__ClassOperator__Group__3__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3465:1: rule__ClassOperator__Group__3__Impl : ( ( rule__ClassOperator__ClassAssignment_3 ) ) ;
    public final void rule__ClassOperator__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3469:1: ( ( ( rule__ClassOperator__ClassAssignment_3 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3470:1: ( ( rule__ClassOperator__ClassAssignment_3 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3470:1: ( ( rule__ClassOperator__ClassAssignment_3 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3471:1: ( rule__ClassOperator__ClassAssignment_3 )
            {
             before(grammarAccess.getClassOperatorAccess().getClassAssignment_3()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3472:1: ( rule__ClassOperator__ClassAssignment_3 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3472:2: rule__ClassOperator__ClassAssignment_3
            {
            pushFollow(FOLLOW_rule__ClassOperator__ClassAssignment_3_in_rule__ClassOperator__Group__3__Impl6933);
            rule__ClassOperator__ClassAssignment_3();

            state._fsp--;


            }

             after(grammarAccess.getClassOperatorAccess().getClassAssignment_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperator__Group__3__Impl"


    // $ANTLR start "rule__ClassOperatorWithExceptions__Group__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3490:1: rule__ClassOperatorWithExceptions__Group__0 : rule__ClassOperatorWithExceptions__Group__0__Impl rule__ClassOperatorWithExceptions__Group__1 ;
    public final void rule__ClassOperatorWithExceptions__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3494:1: ( rule__ClassOperatorWithExceptions__Group__0__Impl rule__ClassOperatorWithExceptions__Group__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3495:2: rule__ClassOperatorWithExceptions__Group__0__Impl rule__ClassOperatorWithExceptions__Group__1
            {
            pushFollow(FOLLOW_rule__ClassOperatorWithExceptions__Group__0__Impl_in_rule__ClassOperatorWithExceptions__Group__06971);
            rule__ClassOperatorWithExceptions__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__ClassOperatorWithExceptions__Group__1_in_rule__ClassOperatorWithExceptions__Group__06974);
            rule__ClassOperatorWithExceptions__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperatorWithExceptions__Group__0"


    // $ANTLR start "rule__ClassOperatorWithExceptions__Group__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3502:1: rule__ClassOperatorWithExceptions__Group__0__Impl : ( 'op' ) ;
    public final void rule__ClassOperatorWithExceptions__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3506:1: ( ( 'op' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3507:1: ( 'op' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3507:1: ( 'op' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3508:1: 'op'
            {
             before(grammarAccess.getClassOperatorWithExceptionsAccess().getOpKeyword_0()); 
            match(input,28,FOLLOW_28_in_rule__ClassOperatorWithExceptions__Group__0__Impl7002); 
             after(grammarAccess.getClassOperatorWithExceptionsAccess().getOpKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperatorWithExceptions__Group__0__Impl"


    // $ANTLR start "rule__ClassOperatorWithExceptions__Group__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3521:1: rule__ClassOperatorWithExceptions__Group__1 : rule__ClassOperatorWithExceptions__Group__1__Impl rule__ClassOperatorWithExceptions__Group__2 ;
    public final void rule__ClassOperatorWithExceptions__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3525:1: ( rule__ClassOperatorWithExceptions__Group__1__Impl rule__ClassOperatorWithExceptions__Group__2 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3526:2: rule__ClassOperatorWithExceptions__Group__1__Impl rule__ClassOperatorWithExceptions__Group__2
            {
            pushFollow(FOLLOW_rule__ClassOperatorWithExceptions__Group__1__Impl_in_rule__ClassOperatorWithExceptions__Group__17033);
            rule__ClassOperatorWithExceptions__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__ClassOperatorWithExceptions__Group__2_in_rule__ClassOperatorWithExceptions__Group__17036);
            rule__ClassOperatorWithExceptions__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperatorWithExceptions__Group__1"


    // $ANTLR start "rule__ClassOperatorWithExceptions__Group__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3533:1: rule__ClassOperatorWithExceptions__Group__1__Impl : ( ( rule__ClassOperatorWithExceptions__NameAssignment_1 ) ) ;
    public final void rule__ClassOperatorWithExceptions__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3537:1: ( ( ( rule__ClassOperatorWithExceptions__NameAssignment_1 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3538:1: ( ( rule__ClassOperatorWithExceptions__NameAssignment_1 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3538:1: ( ( rule__ClassOperatorWithExceptions__NameAssignment_1 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3539:1: ( rule__ClassOperatorWithExceptions__NameAssignment_1 )
            {
             before(grammarAccess.getClassOperatorWithExceptionsAccess().getNameAssignment_1()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3540:1: ( rule__ClassOperatorWithExceptions__NameAssignment_1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3540:2: rule__ClassOperatorWithExceptions__NameAssignment_1
            {
            pushFollow(FOLLOW_rule__ClassOperatorWithExceptions__NameAssignment_1_in_rule__ClassOperatorWithExceptions__Group__1__Impl7063);
            rule__ClassOperatorWithExceptions__NameAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getClassOperatorWithExceptionsAccess().getNameAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperatorWithExceptions__Group__1__Impl"


    // $ANTLR start "rule__ClassOperatorWithExceptions__Group__2"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3550:1: rule__ClassOperatorWithExceptions__Group__2 : rule__ClassOperatorWithExceptions__Group__2__Impl rule__ClassOperatorWithExceptions__Group__3 ;
    public final void rule__ClassOperatorWithExceptions__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3554:1: ( rule__ClassOperatorWithExceptions__Group__2__Impl rule__ClassOperatorWithExceptions__Group__3 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3555:2: rule__ClassOperatorWithExceptions__Group__2__Impl rule__ClassOperatorWithExceptions__Group__3
            {
            pushFollow(FOLLOW_rule__ClassOperatorWithExceptions__Group__2__Impl_in_rule__ClassOperatorWithExceptions__Group__27093);
            rule__ClassOperatorWithExceptions__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__ClassOperatorWithExceptions__Group__3_in_rule__ClassOperatorWithExceptions__Group__27096);
            rule__ClassOperatorWithExceptions__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperatorWithExceptions__Group__2"


    // $ANTLR start "rule__ClassOperatorWithExceptions__Group__2__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3562:1: rule__ClassOperatorWithExceptions__Group__2__Impl : ( '::' ) ;
    public final void rule__ClassOperatorWithExceptions__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3566:1: ( ( '::' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3567:1: ( '::' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3567:1: ( '::' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3568:1: '::'
            {
             before(grammarAccess.getClassOperatorWithExceptionsAccess().getColonColonKeyword_2()); 
            match(input,25,FOLLOW_25_in_rule__ClassOperatorWithExceptions__Group__2__Impl7124); 
             after(grammarAccess.getClassOperatorWithExceptionsAccess().getColonColonKeyword_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperatorWithExceptions__Group__2__Impl"


    // $ANTLR start "rule__ClassOperatorWithExceptions__Group__3"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3581:1: rule__ClassOperatorWithExceptions__Group__3 : rule__ClassOperatorWithExceptions__Group__3__Impl rule__ClassOperatorWithExceptions__Group__4 ;
    public final void rule__ClassOperatorWithExceptions__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3585:1: ( rule__ClassOperatorWithExceptions__Group__3__Impl rule__ClassOperatorWithExceptions__Group__4 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3586:2: rule__ClassOperatorWithExceptions__Group__3__Impl rule__ClassOperatorWithExceptions__Group__4
            {
            pushFollow(FOLLOW_rule__ClassOperatorWithExceptions__Group__3__Impl_in_rule__ClassOperatorWithExceptions__Group__37155);
            rule__ClassOperatorWithExceptions__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__ClassOperatorWithExceptions__Group__4_in_rule__ClassOperatorWithExceptions__Group__37158);
            rule__ClassOperatorWithExceptions__Group__4();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperatorWithExceptions__Group__3"


    // $ANTLR start "rule__ClassOperatorWithExceptions__Group__3__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3593:1: rule__ClassOperatorWithExceptions__Group__3__Impl : ( ( rule__ClassOperatorWithExceptions__ClassAssignment_3 ) ) ;
    public final void rule__ClassOperatorWithExceptions__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3597:1: ( ( ( rule__ClassOperatorWithExceptions__ClassAssignment_3 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3598:1: ( ( rule__ClassOperatorWithExceptions__ClassAssignment_3 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3598:1: ( ( rule__ClassOperatorWithExceptions__ClassAssignment_3 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3599:1: ( rule__ClassOperatorWithExceptions__ClassAssignment_3 )
            {
             before(grammarAccess.getClassOperatorWithExceptionsAccess().getClassAssignment_3()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3600:1: ( rule__ClassOperatorWithExceptions__ClassAssignment_3 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3600:2: rule__ClassOperatorWithExceptions__ClassAssignment_3
            {
            pushFollow(FOLLOW_rule__ClassOperatorWithExceptions__ClassAssignment_3_in_rule__ClassOperatorWithExceptions__Group__3__Impl7185);
            rule__ClassOperatorWithExceptions__ClassAssignment_3();

            state._fsp--;


            }

             after(grammarAccess.getClassOperatorWithExceptionsAccess().getClassAssignment_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperatorWithExceptions__Group__3__Impl"


    // $ANTLR start "rule__ClassOperatorWithExceptions__Group__4"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3610:1: rule__ClassOperatorWithExceptions__Group__4 : rule__ClassOperatorWithExceptions__Group__4__Impl rule__ClassOperatorWithExceptions__Group__5 ;
    public final void rule__ClassOperatorWithExceptions__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3614:1: ( rule__ClassOperatorWithExceptions__Group__4__Impl rule__ClassOperatorWithExceptions__Group__5 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3615:2: rule__ClassOperatorWithExceptions__Group__4__Impl rule__ClassOperatorWithExceptions__Group__5
            {
            pushFollow(FOLLOW_rule__ClassOperatorWithExceptions__Group__4__Impl_in_rule__ClassOperatorWithExceptions__Group__47215);
            rule__ClassOperatorWithExceptions__Group__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__ClassOperatorWithExceptions__Group__5_in_rule__ClassOperatorWithExceptions__Group__47218);
            rule__ClassOperatorWithExceptions__Group__5();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperatorWithExceptions__Group__4"


    // $ANTLR start "rule__ClassOperatorWithExceptions__Group__4__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3622:1: rule__ClassOperatorWithExceptions__Group__4__Impl : ( '(' ) ;
    public final void rule__ClassOperatorWithExceptions__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3626:1: ( ( '(' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3627:1: ( '(' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3627:1: ( '(' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3628:1: '('
            {
             before(grammarAccess.getClassOperatorWithExceptionsAccess().getLeftParenthesisKeyword_4()); 
            match(input,26,FOLLOW_26_in_rule__ClassOperatorWithExceptions__Group__4__Impl7246); 
             after(grammarAccess.getClassOperatorWithExceptionsAccess().getLeftParenthesisKeyword_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperatorWithExceptions__Group__4__Impl"


    // $ANTLR start "rule__ClassOperatorWithExceptions__Group__5"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3641:1: rule__ClassOperatorWithExceptions__Group__5 : rule__ClassOperatorWithExceptions__Group__5__Impl rule__ClassOperatorWithExceptions__Group__6 ;
    public final void rule__ClassOperatorWithExceptions__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3645:1: ( rule__ClassOperatorWithExceptions__Group__5__Impl rule__ClassOperatorWithExceptions__Group__6 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3646:2: rule__ClassOperatorWithExceptions__Group__5__Impl rule__ClassOperatorWithExceptions__Group__6
            {
            pushFollow(FOLLOW_rule__ClassOperatorWithExceptions__Group__5__Impl_in_rule__ClassOperatorWithExceptions__Group__57277);
            rule__ClassOperatorWithExceptions__Group__5__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__ClassOperatorWithExceptions__Group__6_in_rule__ClassOperatorWithExceptions__Group__57280);
            rule__ClassOperatorWithExceptions__Group__6();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperatorWithExceptions__Group__5"


    // $ANTLR start "rule__ClassOperatorWithExceptions__Group__5__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3653:1: rule__ClassOperatorWithExceptions__Group__5__Impl : ( ( rule__ClassOperatorWithExceptions__ParametersAssignment_5 ) ) ;
    public final void rule__ClassOperatorWithExceptions__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3657:1: ( ( ( rule__ClassOperatorWithExceptions__ParametersAssignment_5 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3658:1: ( ( rule__ClassOperatorWithExceptions__ParametersAssignment_5 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3658:1: ( ( rule__ClassOperatorWithExceptions__ParametersAssignment_5 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3659:1: ( rule__ClassOperatorWithExceptions__ParametersAssignment_5 )
            {
             before(grammarAccess.getClassOperatorWithExceptionsAccess().getParametersAssignment_5()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3660:1: ( rule__ClassOperatorWithExceptions__ParametersAssignment_5 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3660:2: rule__ClassOperatorWithExceptions__ParametersAssignment_5
            {
            pushFollow(FOLLOW_rule__ClassOperatorWithExceptions__ParametersAssignment_5_in_rule__ClassOperatorWithExceptions__Group__5__Impl7307);
            rule__ClassOperatorWithExceptions__ParametersAssignment_5();

            state._fsp--;


            }

             after(grammarAccess.getClassOperatorWithExceptionsAccess().getParametersAssignment_5()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperatorWithExceptions__Group__5__Impl"


    // $ANTLR start "rule__ClassOperatorWithExceptions__Group__6"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3670:1: rule__ClassOperatorWithExceptions__Group__6 : rule__ClassOperatorWithExceptions__Group__6__Impl rule__ClassOperatorWithExceptions__Group__7 ;
    public final void rule__ClassOperatorWithExceptions__Group__6() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3674:1: ( rule__ClassOperatorWithExceptions__Group__6__Impl rule__ClassOperatorWithExceptions__Group__7 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3675:2: rule__ClassOperatorWithExceptions__Group__6__Impl rule__ClassOperatorWithExceptions__Group__7
            {
            pushFollow(FOLLOW_rule__ClassOperatorWithExceptions__Group__6__Impl_in_rule__ClassOperatorWithExceptions__Group__67337);
            rule__ClassOperatorWithExceptions__Group__6__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__ClassOperatorWithExceptions__Group__7_in_rule__ClassOperatorWithExceptions__Group__67340);
            rule__ClassOperatorWithExceptions__Group__7();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperatorWithExceptions__Group__6"


    // $ANTLR start "rule__ClassOperatorWithExceptions__Group__6__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3682:1: rule__ClassOperatorWithExceptions__Group__6__Impl : ( ( rule__ClassOperatorWithExceptions__Group_6__0 )* ) ;
    public final void rule__ClassOperatorWithExceptions__Group__6__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3686:1: ( ( ( rule__ClassOperatorWithExceptions__Group_6__0 )* ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3687:1: ( ( rule__ClassOperatorWithExceptions__Group_6__0 )* )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3687:1: ( ( rule__ClassOperatorWithExceptions__Group_6__0 )* )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3688:1: ( rule__ClassOperatorWithExceptions__Group_6__0 )*
            {
             before(grammarAccess.getClassOperatorWithExceptionsAccess().getGroup_6()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3689:1: ( rule__ClassOperatorWithExceptions__Group_6__0 )*
            loop23:
            do {
                int alt23=2;
                int LA23_0 = input.LA(1);

                if ( (LA23_0==19) ) {
                    alt23=1;
                }


                switch (alt23) {
            	case 1 :
            	    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3689:2: rule__ClassOperatorWithExceptions__Group_6__0
            	    {
            	    pushFollow(FOLLOW_rule__ClassOperatorWithExceptions__Group_6__0_in_rule__ClassOperatorWithExceptions__Group__6__Impl7367);
            	    rule__ClassOperatorWithExceptions__Group_6__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop23;
                }
            } while (true);

             after(grammarAccess.getClassOperatorWithExceptionsAccess().getGroup_6()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperatorWithExceptions__Group__6__Impl"


    // $ANTLR start "rule__ClassOperatorWithExceptions__Group__7"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3699:1: rule__ClassOperatorWithExceptions__Group__7 : rule__ClassOperatorWithExceptions__Group__7__Impl ;
    public final void rule__ClassOperatorWithExceptions__Group__7() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3703:1: ( rule__ClassOperatorWithExceptions__Group__7__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3704:2: rule__ClassOperatorWithExceptions__Group__7__Impl
            {
            pushFollow(FOLLOW_rule__ClassOperatorWithExceptions__Group__7__Impl_in_rule__ClassOperatorWithExceptions__Group__77398);
            rule__ClassOperatorWithExceptions__Group__7__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperatorWithExceptions__Group__7"


    // $ANTLR start "rule__ClassOperatorWithExceptions__Group__7__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3710:1: rule__ClassOperatorWithExceptions__Group__7__Impl : ( ')' ) ;
    public final void rule__ClassOperatorWithExceptions__Group__7__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3714:1: ( ( ')' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3715:1: ( ')' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3715:1: ( ')' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3716:1: ')'
            {
             before(grammarAccess.getClassOperatorWithExceptionsAccess().getRightParenthesisKeyword_7()); 
            match(input,27,FOLLOW_27_in_rule__ClassOperatorWithExceptions__Group__7__Impl7426); 
             after(grammarAccess.getClassOperatorWithExceptionsAccess().getRightParenthesisKeyword_7()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperatorWithExceptions__Group__7__Impl"


    // $ANTLR start "rule__ClassOperatorWithExceptions__Group_6__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3745:1: rule__ClassOperatorWithExceptions__Group_6__0 : rule__ClassOperatorWithExceptions__Group_6__0__Impl rule__ClassOperatorWithExceptions__Group_6__1 ;
    public final void rule__ClassOperatorWithExceptions__Group_6__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3749:1: ( rule__ClassOperatorWithExceptions__Group_6__0__Impl rule__ClassOperatorWithExceptions__Group_6__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3750:2: rule__ClassOperatorWithExceptions__Group_6__0__Impl rule__ClassOperatorWithExceptions__Group_6__1
            {
            pushFollow(FOLLOW_rule__ClassOperatorWithExceptions__Group_6__0__Impl_in_rule__ClassOperatorWithExceptions__Group_6__07473);
            rule__ClassOperatorWithExceptions__Group_6__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__ClassOperatorWithExceptions__Group_6__1_in_rule__ClassOperatorWithExceptions__Group_6__07476);
            rule__ClassOperatorWithExceptions__Group_6__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperatorWithExceptions__Group_6__0"


    // $ANTLR start "rule__ClassOperatorWithExceptions__Group_6__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3757:1: rule__ClassOperatorWithExceptions__Group_6__0__Impl : ( ',' ) ;
    public final void rule__ClassOperatorWithExceptions__Group_6__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3761:1: ( ( ',' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3762:1: ( ',' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3762:1: ( ',' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3763:1: ','
            {
             before(grammarAccess.getClassOperatorWithExceptionsAccess().getCommaKeyword_6_0()); 
            match(input,19,FOLLOW_19_in_rule__ClassOperatorWithExceptions__Group_6__0__Impl7504); 
             after(grammarAccess.getClassOperatorWithExceptionsAccess().getCommaKeyword_6_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperatorWithExceptions__Group_6__0__Impl"


    // $ANTLR start "rule__ClassOperatorWithExceptions__Group_6__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3776:1: rule__ClassOperatorWithExceptions__Group_6__1 : rule__ClassOperatorWithExceptions__Group_6__1__Impl ;
    public final void rule__ClassOperatorWithExceptions__Group_6__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3780:1: ( rule__ClassOperatorWithExceptions__Group_6__1__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3781:2: rule__ClassOperatorWithExceptions__Group_6__1__Impl
            {
            pushFollow(FOLLOW_rule__ClassOperatorWithExceptions__Group_6__1__Impl_in_rule__ClassOperatorWithExceptions__Group_6__17535);
            rule__ClassOperatorWithExceptions__Group_6__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperatorWithExceptions__Group_6__1"


    // $ANTLR start "rule__ClassOperatorWithExceptions__Group_6__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3787:1: rule__ClassOperatorWithExceptions__Group_6__1__Impl : ( ( rule__ClassOperatorWithExceptions__ParametersAssignment_6_1 ) ) ;
    public final void rule__ClassOperatorWithExceptions__Group_6__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3791:1: ( ( ( rule__ClassOperatorWithExceptions__ParametersAssignment_6_1 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3792:1: ( ( rule__ClassOperatorWithExceptions__ParametersAssignment_6_1 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3792:1: ( ( rule__ClassOperatorWithExceptions__ParametersAssignment_6_1 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3793:1: ( rule__ClassOperatorWithExceptions__ParametersAssignment_6_1 )
            {
             before(grammarAccess.getClassOperatorWithExceptionsAccess().getParametersAssignment_6_1()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3794:1: ( rule__ClassOperatorWithExceptions__ParametersAssignment_6_1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3794:2: rule__ClassOperatorWithExceptions__ParametersAssignment_6_1
            {
            pushFollow(FOLLOW_rule__ClassOperatorWithExceptions__ParametersAssignment_6_1_in_rule__ClassOperatorWithExceptions__Group_6__1__Impl7562);
            rule__ClassOperatorWithExceptions__ParametersAssignment_6_1();

            state._fsp--;


            }

             after(grammarAccess.getClassOperatorWithExceptionsAccess().getParametersAssignment_6_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperatorWithExceptions__Group_6__1__Impl"


    // $ANTLR start "rule__UserOperator__Group__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3808:1: rule__UserOperator__Group__0 : rule__UserOperator__Group__0__Impl rule__UserOperator__Group__1 ;
    public final void rule__UserOperator__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3812:1: ( rule__UserOperator__Group__0__Impl rule__UserOperator__Group__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3813:2: rule__UserOperator__Group__0__Impl rule__UserOperator__Group__1
            {
            pushFollow(FOLLOW_rule__UserOperator__Group__0__Impl_in_rule__UserOperator__Group__07596);
            rule__UserOperator__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__UserOperator__Group__1_in_rule__UserOperator__Group__07599);
            rule__UserOperator__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__0"


    // $ANTLR start "rule__UserOperator__Group__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3820:1: rule__UserOperator__Group__0__Impl : ( 'op' ) ;
    public final void rule__UserOperator__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3824:1: ( ( 'op' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3825:1: ( 'op' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3825:1: ( 'op' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3826:1: 'op'
            {
             before(grammarAccess.getUserOperatorAccess().getOpKeyword_0()); 
            match(input,28,FOLLOW_28_in_rule__UserOperator__Group__0__Impl7627); 
             after(grammarAccess.getUserOperatorAccess().getOpKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__0__Impl"


    // $ANTLR start "rule__UserOperator__Group__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3839:1: rule__UserOperator__Group__1 : rule__UserOperator__Group__1__Impl rule__UserOperator__Group__2 ;
    public final void rule__UserOperator__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3843:1: ( rule__UserOperator__Group__1__Impl rule__UserOperator__Group__2 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3844:2: rule__UserOperator__Group__1__Impl rule__UserOperator__Group__2
            {
            pushFollow(FOLLOW_rule__UserOperator__Group__1__Impl_in_rule__UserOperator__Group__17658);
            rule__UserOperator__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__UserOperator__Group__2_in_rule__UserOperator__Group__17661);
            rule__UserOperator__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__1"


    // $ANTLR start "rule__UserOperator__Group__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3851:1: rule__UserOperator__Group__1__Impl : ( ( rule__UserOperator__NameAssignment_1 ) ) ;
    public final void rule__UserOperator__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3855:1: ( ( ( rule__UserOperator__NameAssignment_1 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3856:1: ( ( rule__UserOperator__NameAssignment_1 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3856:1: ( ( rule__UserOperator__NameAssignment_1 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3857:1: ( rule__UserOperator__NameAssignment_1 )
            {
             before(grammarAccess.getUserOperatorAccess().getNameAssignment_1()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3858:1: ( rule__UserOperator__NameAssignment_1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3858:2: rule__UserOperator__NameAssignment_1
            {
            pushFollow(FOLLOW_rule__UserOperator__NameAssignment_1_in_rule__UserOperator__Group__1__Impl7688);
            rule__UserOperator__NameAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getUserOperatorAccess().getNameAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__1__Impl"


    // $ANTLR start "rule__UserOperator__Group__2"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3868:1: rule__UserOperator__Group__2 : rule__UserOperator__Group__2__Impl rule__UserOperator__Group__3 ;
    public final void rule__UserOperator__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3872:1: ( rule__UserOperator__Group__2__Impl rule__UserOperator__Group__3 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3873:2: rule__UserOperator__Group__2__Impl rule__UserOperator__Group__3
            {
            pushFollow(FOLLOW_rule__UserOperator__Group__2__Impl_in_rule__UserOperator__Group__27718);
            rule__UserOperator__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__UserOperator__Group__3_in_rule__UserOperator__Group__27721);
            rule__UserOperator__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__2"


    // $ANTLR start "rule__UserOperator__Group__2__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3880:1: rule__UserOperator__Group__2__Impl : ( '(' ) ;
    public final void rule__UserOperator__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3884:1: ( ( '(' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3885:1: ( '(' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3885:1: ( '(' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3886:1: '('
            {
             before(grammarAccess.getUserOperatorAccess().getLeftParenthesisKeyword_2()); 
            match(input,26,FOLLOW_26_in_rule__UserOperator__Group__2__Impl7749); 
             after(grammarAccess.getUserOperatorAccess().getLeftParenthesisKeyword_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__2__Impl"


    // $ANTLR start "rule__UserOperator__Group__3"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3899:1: rule__UserOperator__Group__3 : rule__UserOperator__Group__3__Impl rule__UserOperator__Group__4 ;
    public final void rule__UserOperator__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3903:1: ( rule__UserOperator__Group__3__Impl rule__UserOperator__Group__4 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3904:2: rule__UserOperator__Group__3__Impl rule__UserOperator__Group__4
            {
            pushFollow(FOLLOW_rule__UserOperator__Group__3__Impl_in_rule__UserOperator__Group__37780);
            rule__UserOperator__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__UserOperator__Group__4_in_rule__UserOperator__Group__37783);
            rule__UserOperator__Group__4();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__3"


    // $ANTLR start "rule__UserOperator__Group__3__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3911:1: rule__UserOperator__Group__3__Impl : ( ( rule__UserOperator__ParametersAssignment_3 ) ) ;
    public final void rule__UserOperator__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3915:1: ( ( ( rule__UserOperator__ParametersAssignment_3 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3916:1: ( ( rule__UserOperator__ParametersAssignment_3 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3916:1: ( ( rule__UserOperator__ParametersAssignment_3 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3917:1: ( rule__UserOperator__ParametersAssignment_3 )
            {
             before(grammarAccess.getUserOperatorAccess().getParametersAssignment_3()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3918:1: ( rule__UserOperator__ParametersAssignment_3 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3918:2: rule__UserOperator__ParametersAssignment_3
            {
            pushFollow(FOLLOW_rule__UserOperator__ParametersAssignment_3_in_rule__UserOperator__Group__3__Impl7810);
            rule__UserOperator__ParametersAssignment_3();

            state._fsp--;


            }

             after(grammarAccess.getUserOperatorAccess().getParametersAssignment_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__3__Impl"


    // $ANTLR start "rule__UserOperator__Group__4"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3928:1: rule__UserOperator__Group__4 : rule__UserOperator__Group__4__Impl rule__UserOperator__Group__5 ;
    public final void rule__UserOperator__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3932:1: ( rule__UserOperator__Group__4__Impl rule__UserOperator__Group__5 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3933:2: rule__UserOperator__Group__4__Impl rule__UserOperator__Group__5
            {
            pushFollow(FOLLOW_rule__UserOperator__Group__4__Impl_in_rule__UserOperator__Group__47840);
            rule__UserOperator__Group__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__UserOperator__Group__5_in_rule__UserOperator__Group__47843);
            rule__UserOperator__Group__5();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__4"


    // $ANTLR start "rule__UserOperator__Group__4__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3940:1: rule__UserOperator__Group__4__Impl : ( ( rule__UserOperator__Group_4__0 )* ) ;
    public final void rule__UserOperator__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3944:1: ( ( ( rule__UserOperator__Group_4__0 )* ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3945:1: ( ( rule__UserOperator__Group_4__0 )* )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3945:1: ( ( rule__UserOperator__Group_4__0 )* )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3946:1: ( rule__UserOperator__Group_4__0 )*
            {
             before(grammarAccess.getUserOperatorAccess().getGroup_4()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3947:1: ( rule__UserOperator__Group_4__0 )*
            loop24:
            do {
                int alt24=2;
                int LA24_0 = input.LA(1);

                if ( (LA24_0==19) ) {
                    alt24=1;
                }


                switch (alt24) {
            	case 1 :
            	    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3947:2: rule__UserOperator__Group_4__0
            	    {
            	    pushFollow(FOLLOW_rule__UserOperator__Group_4__0_in_rule__UserOperator__Group__4__Impl7870);
            	    rule__UserOperator__Group_4__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop24;
                }
            } while (true);

             after(grammarAccess.getUserOperatorAccess().getGroup_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__4__Impl"


    // $ANTLR start "rule__UserOperator__Group__5"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3957:1: rule__UserOperator__Group__5 : rule__UserOperator__Group__5__Impl rule__UserOperator__Group__6 ;
    public final void rule__UserOperator__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3961:1: ( rule__UserOperator__Group__5__Impl rule__UserOperator__Group__6 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3962:2: rule__UserOperator__Group__5__Impl rule__UserOperator__Group__6
            {
            pushFollow(FOLLOW_rule__UserOperator__Group__5__Impl_in_rule__UserOperator__Group__57901);
            rule__UserOperator__Group__5__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__UserOperator__Group__6_in_rule__UserOperator__Group__57904);
            rule__UserOperator__Group__6();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__5"


    // $ANTLR start "rule__UserOperator__Group__5__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3969:1: rule__UserOperator__Group__5__Impl : ( ')' ) ;
    public final void rule__UserOperator__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3973:1: ( ( ')' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3974:1: ( ')' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3974:1: ( ')' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3975:1: ')'
            {
             before(grammarAccess.getUserOperatorAccess().getRightParenthesisKeyword_5()); 
            match(input,27,FOLLOW_27_in_rule__UserOperator__Group__5__Impl7932); 
             after(grammarAccess.getUserOperatorAccess().getRightParenthesisKeyword_5()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__5__Impl"


    // $ANTLR start "rule__UserOperator__Group__6"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3988:1: rule__UserOperator__Group__6 : rule__UserOperator__Group__6__Impl rule__UserOperator__Group__7 ;
    public final void rule__UserOperator__Group__6() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3992:1: ( rule__UserOperator__Group__6__Impl rule__UserOperator__Group__7 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:3993:2: rule__UserOperator__Group__6__Impl rule__UserOperator__Group__7
            {
            pushFollow(FOLLOW_rule__UserOperator__Group__6__Impl_in_rule__UserOperator__Group__67963);
            rule__UserOperator__Group__6__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__UserOperator__Group__7_in_rule__UserOperator__Group__67966);
            rule__UserOperator__Group__7();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__6"


    // $ANTLR start "rule__UserOperator__Group__6__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4000:1: rule__UserOperator__Group__6__Impl : ( '::' ) ;
    public final void rule__UserOperator__Group__6__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4004:1: ( ( '::' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4005:1: ( '::' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4005:1: ( '::' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4006:1: '::'
            {
             before(grammarAccess.getUserOperatorAccess().getColonColonKeyword_6()); 
            match(input,25,FOLLOW_25_in_rule__UserOperator__Group__6__Impl7994); 
             after(grammarAccess.getUserOperatorAccess().getColonColonKeyword_6()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__6__Impl"


    // $ANTLR start "rule__UserOperator__Group__7"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4019:1: rule__UserOperator__Group__7 : rule__UserOperator__Group__7__Impl rule__UserOperator__Group__8 ;
    public final void rule__UserOperator__Group__7() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4023:1: ( rule__UserOperator__Group__7__Impl rule__UserOperator__Group__8 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4024:2: rule__UserOperator__Group__7__Impl rule__UserOperator__Group__8
            {
            pushFollow(FOLLOW_rule__UserOperator__Group__7__Impl_in_rule__UserOperator__Group__78025);
            rule__UserOperator__Group__7__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__UserOperator__Group__8_in_rule__UserOperator__Group__78028);
            rule__UserOperator__Group__8();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__7"


    // $ANTLR start "rule__UserOperator__Group__7__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4031:1: rule__UserOperator__Group__7__Impl : ( ( rule__UserOperator__TypeAssignment_7 ) ) ;
    public final void rule__UserOperator__Group__7__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4035:1: ( ( ( rule__UserOperator__TypeAssignment_7 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4036:1: ( ( rule__UserOperator__TypeAssignment_7 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4036:1: ( ( rule__UserOperator__TypeAssignment_7 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4037:1: ( rule__UserOperator__TypeAssignment_7 )
            {
             before(grammarAccess.getUserOperatorAccess().getTypeAssignment_7()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4038:1: ( rule__UserOperator__TypeAssignment_7 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4038:2: rule__UserOperator__TypeAssignment_7
            {
            pushFollow(FOLLOW_rule__UserOperator__TypeAssignment_7_in_rule__UserOperator__Group__7__Impl8055);
            rule__UserOperator__TypeAssignment_7();

            state._fsp--;


            }

             after(grammarAccess.getUserOperatorAccess().getTypeAssignment_7()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__7__Impl"


    // $ANTLR start "rule__UserOperator__Group__8"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4048:1: rule__UserOperator__Group__8 : rule__UserOperator__Group__8__Impl rule__UserOperator__Group__9 ;
    public final void rule__UserOperator__Group__8() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4052:1: ( rule__UserOperator__Group__8__Impl rule__UserOperator__Group__9 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4053:2: rule__UserOperator__Group__8__Impl rule__UserOperator__Group__9
            {
            pushFollow(FOLLOW_rule__UserOperator__Group__8__Impl_in_rule__UserOperator__Group__88085);
            rule__UserOperator__Group__8__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__UserOperator__Group__9_in_rule__UserOperator__Group__88088);
            rule__UserOperator__Group__9();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__8"


    // $ANTLR start "rule__UserOperator__Group__8__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4060:1: rule__UserOperator__Group__8__Impl : ( '{' ) ;
    public final void rule__UserOperator__Group__8__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4064:1: ( ( '{' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4065:1: ( '{' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4065:1: ( '{' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4066:1: '{'
            {
             before(grammarAccess.getUserOperatorAccess().getLeftCurlyBracketKeyword_8()); 
            match(input,16,FOLLOW_16_in_rule__UserOperator__Group__8__Impl8116); 
             after(grammarAccess.getUserOperatorAccess().getLeftCurlyBracketKeyword_8()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__8__Impl"


    // $ANTLR start "rule__UserOperator__Group__9"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4079:1: rule__UserOperator__Group__9 : rule__UserOperator__Group__9__Impl rule__UserOperator__Group__10 ;
    public final void rule__UserOperator__Group__9() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4083:1: ( rule__UserOperator__Group__9__Impl rule__UserOperator__Group__10 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4084:2: rule__UserOperator__Group__9__Impl rule__UserOperator__Group__10
            {
            pushFollow(FOLLOW_rule__UserOperator__Group__9__Impl_in_rule__UserOperator__Group__98147);
            rule__UserOperator__Group__9__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__UserOperator__Group__10_in_rule__UserOperator__Group__98150);
            rule__UserOperator__Group__10();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__9"


    // $ANTLR start "rule__UserOperator__Group__9__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4091:1: rule__UserOperator__Group__9__Impl : ( ( rule__UserOperator__AccessorsAssignment_9 ) ) ;
    public final void rule__UserOperator__Group__9__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4095:1: ( ( ( rule__UserOperator__AccessorsAssignment_9 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4096:1: ( ( rule__UserOperator__AccessorsAssignment_9 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4096:1: ( ( rule__UserOperator__AccessorsAssignment_9 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4097:1: ( rule__UserOperator__AccessorsAssignment_9 )
            {
             before(grammarAccess.getUserOperatorAccess().getAccessorsAssignment_9()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4098:1: ( rule__UserOperator__AccessorsAssignment_9 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4098:2: rule__UserOperator__AccessorsAssignment_9
            {
            pushFollow(FOLLOW_rule__UserOperator__AccessorsAssignment_9_in_rule__UserOperator__Group__9__Impl8177);
            rule__UserOperator__AccessorsAssignment_9();

            state._fsp--;


            }

             after(grammarAccess.getUserOperatorAccess().getAccessorsAssignment_9()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__9__Impl"


    // $ANTLR start "rule__UserOperator__Group__10"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4108:1: rule__UserOperator__Group__10 : rule__UserOperator__Group__10__Impl rule__UserOperator__Group__11 ;
    public final void rule__UserOperator__Group__10() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4112:1: ( rule__UserOperator__Group__10__Impl rule__UserOperator__Group__11 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4113:2: rule__UserOperator__Group__10__Impl rule__UserOperator__Group__11
            {
            pushFollow(FOLLOW_rule__UserOperator__Group__10__Impl_in_rule__UserOperator__Group__108207);
            rule__UserOperator__Group__10__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__UserOperator__Group__11_in_rule__UserOperator__Group__108210);
            rule__UserOperator__Group__11();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__10"


    // $ANTLR start "rule__UserOperator__Group__10__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4120:1: rule__UserOperator__Group__10__Impl : ( ( rule__UserOperator__Group_10__0 )* ) ;
    public final void rule__UserOperator__Group__10__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4124:1: ( ( ( rule__UserOperator__Group_10__0 )* ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4125:1: ( ( rule__UserOperator__Group_10__0 )* )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4125:1: ( ( rule__UserOperator__Group_10__0 )* )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4126:1: ( rule__UserOperator__Group_10__0 )*
            {
             before(grammarAccess.getUserOperatorAccess().getGroup_10()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4127:1: ( rule__UserOperator__Group_10__0 )*
            loop25:
            do {
                int alt25=2;
                int LA25_0 = input.LA(1);

                if ( (LA25_0==12) ) {
                    int LA25_1 = input.LA(2);

                    if ( (LA25_1==32) ) {
                        alt25=1;
                    }


                }


                switch (alt25) {
            	case 1 :
            	    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4127:2: rule__UserOperator__Group_10__0
            	    {
            	    pushFollow(FOLLOW_rule__UserOperator__Group_10__0_in_rule__UserOperator__Group__10__Impl8237);
            	    rule__UserOperator__Group_10__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop25;
                }
            } while (true);

             after(grammarAccess.getUserOperatorAccess().getGroup_10()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__10__Impl"


    // $ANTLR start "rule__UserOperator__Group__11"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4137:1: rule__UserOperator__Group__11 : rule__UserOperator__Group__11__Impl rule__UserOperator__Group__12 ;
    public final void rule__UserOperator__Group__11() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4141:1: ( rule__UserOperator__Group__11__Impl rule__UserOperator__Group__12 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4142:2: rule__UserOperator__Group__11__Impl rule__UserOperator__Group__12
            {
            pushFollow(FOLLOW_rule__UserOperator__Group__11__Impl_in_rule__UserOperator__Group__118268);
            rule__UserOperator__Group__11__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__UserOperator__Group__12_in_rule__UserOperator__Group__118271);
            rule__UserOperator__Group__12();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__11"


    // $ANTLR start "rule__UserOperator__Group__11__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4149:1: rule__UserOperator__Group__11__Impl : ( ';' ) ;
    public final void rule__UserOperator__Group__11__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4153:1: ( ( ';' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4154:1: ( ';' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4154:1: ( ';' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4155:1: ';'
            {
             before(grammarAccess.getUserOperatorAccess().getSemicolonKeyword_11()); 
            match(input,12,FOLLOW_12_in_rule__UserOperator__Group__11__Impl8299); 
             after(grammarAccess.getUserOperatorAccess().getSemicolonKeyword_11()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__11__Impl"


    // $ANTLR start "rule__UserOperator__Group__12"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4168:1: rule__UserOperator__Group__12 : rule__UserOperator__Group__12__Impl rule__UserOperator__Group__13 ;
    public final void rule__UserOperator__Group__12() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4172:1: ( rule__UserOperator__Group__12__Impl rule__UserOperator__Group__13 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4173:2: rule__UserOperator__Group__12__Impl rule__UserOperator__Group__13
            {
            pushFollow(FOLLOW_rule__UserOperator__Group__12__Impl_in_rule__UserOperator__Group__128330);
            rule__UserOperator__Group__12__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__UserOperator__Group__13_in_rule__UserOperator__Group__128333);
            rule__UserOperator__Group__13();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__12"


    // $ANTLR start "rule__UserOperator__Group__12__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4180:1: rule__UserOperator__Group__12__Impl : ( 'make' ) ;
    public final void rule__UserOperator__Group__12__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4184:1: ( ( 'make' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4185:1: ( 'make' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4185:1: ( 'make' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4186:1: 'make'
            {
             before(grammarAccess.getUserOperatorAccess().getMakeKeyword_12()); 
            match(input,29,FOLLOW_29_in_rule__UserOperator__Group__12__Impl8361); 
             after(grammarAccess.getUserOperatorAccess().getMakeKeyword_12()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__12__Impl"


    // $ANTLR start "rule__UserOperator__Group__13"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4199:1: rule__UserOperator__Group__13 : rule__UserOperator__Group__13__Impl rule__UserOperator__Group__14 ;
    public final void rule__UserOperator__Group__13() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4203:1: ( rule__UserOperator__Group__13__Impl rule__UserOperator__Group__14 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4204:2: rule__UserOperator__Group__13__Impl rule__UserOperator__Group__14
            {
            pushFollow(FOLLOW_rule__UserOperator__Group__13__Impl_in_rule__UserOperator__Group__138392);
            rule__UserOperator__Group__13__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__UserOperator__Group__14_in_rule__UserOperator__Group__138395);
            rule__UserOperator__Group__14();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__13"


    // $ANTLR start "rule__UserOperator__Group__13__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4211:1: rule__UserOperator__Group__13__Impl : ( '=' ) ;
    public final void rule__UserOperator__Group__13__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4215:1: ( ( '=' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4216:1: ( '=' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4216:1: ( '=' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4217:1: '='
            {
             before(grammarAccess.getUserOperatorAccess().getEqualsSignKeyword_13()); 
            match(input,30,FOLLOW_30_in_rule__UserOperator__Group__13__Impl8423); 
             after(grammarAccess.getUserOperatorAccess().getEqualsSignKeyword_13()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__13__Impl"


    // $ANTLR start "rule__UserOperator__Group__14"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4230:1: rule__UserOperator__Group__14 : rule__UserOperator__Group__14__Impl rule__UserOperator__Group__15 ;
    public final void rule__UserOperator__Group__14() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4234:1: ( rule__UserOperator__Group__14__Impl rule__UserOperator__Group__15 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4235:2: rule__UserOperator__Group__14__Impl rule__UserOperator__Group__15
            {
            pushFollow(FOLLOW_rule__UserOperator__Group__14__Impl_in_rule__UserOperator__Group__148454);
            rule__UserOperator__Group__14__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__UserOperator__Group__15_in_rule__UserOperator__Group__148457);
            rule__UserOperator__Group__15();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__14"


    // $ANTLR start "rule__UserOperator__Group__14__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4242:1: rule__UserOperator__Group__14__Impl : ( ( rule__UserOperator__MakeAssignment_14 ) ) ;
    public final void rule__UserOperator__Group__14__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4246:1: ( ( ( rule__UserOperator__MakeAssignment_14 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4247:1: ( ( rule__UserOperator__MakeAssignment_14 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4247:1: ( ( rule__UserOperator__MakeAssignment_14 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4248:1: ( rule__UserOperator__MakeAssignment_14 )
            {
             before(grammarAccess.getUserOperatorAccess().getMakeAssignment_14()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4249:1: ( rule__UserOperator__MakeAssignment_14 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4249:2: rule__UserOperator__MakeAssignment_14
            {
            pushFollow(FOLLOW_rule__UserOperator__MakeAssignment_14_in_rule__UserOperator__Group__14__Impl8484);
            rule__UserOperator__MakeAssignment_14();

            state._fsp--;


            }

             after(grammarAccess.getUserOperatorAccess().getMakeAssignment_14()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__14__Impl"


    // $ANTLR start "rule__UserOperator__Group__15"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4259:1: rule__UserOperator__Group__15 : rule__UserOperator__Group__15__Impl rule__UserOperator__Group__16 ;
    public final void rule__UserOperator__Group__15() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4263:1: ( rule__UserOperator__Group__15__Impl rule__UserOperator__Group__16 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4264:2: rule__UserOperator__Group__15__Impl rule__UserOperator__Group__16
            {
            pushFollow(FOLLOW_rule__UserOperator__Group__15__Impl_in_rule__UserOperator__Group__158514);
            rule__UserOperator__Group__15__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__UserOperator__Group__16_in_rule__UserOperator__Group__158517);
            rule__UserOperator__Group__16();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__15"


    // $ANTLR start "rule__UserOperator__Group__15__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4271:1: rule__UserOperator__Group__15__Impl : ( ';' ) ;
    public final void rule__UserOperator__Group__15__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4275:1: ( ( ';' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4276:1: ( ';' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4276:1: ( ';' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4277:1: ';'
            {
             before(grammarAccess.getUserOperatorAccess().getSemicolonKeyword_15()); 
            match(input,12,FOLLOW_12_in_rule__UserOperator__Group__15__Impl8545); 
             after(grammarAccess.getUserOperatorAccess().getSemicolonKeyword_15()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__15__Impl"


    // $ANTLR start "rule__UserOperator__Group__16"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4290:1: rule__UserOperator__Group__16 : rule__UserOperator__Group__16__Impl rule__UserOperator__Group__17 ;
    public final void rule__UserOperator__Group__16() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4294:1: ( rule__UserOperator__Group__16__Impl rule__UserOperator__Group__17 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4295:2: rule__UserOperator__Group__16__Impl rule__UserOperator__Group__17
            {
            pushFollow(FOLLOW_rule__UserOperator__Group__16__Impl_in_rule__UserOperator__Group__168576);
            rule__UserOperator__Group__16__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__UserOperator__Group__17_in_rule__UserOperator__Group__168579);
            rule__UserOperator__Group__17();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__16"


    // $ANTLR start "rule__UserOperator__Group__16__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4302:1: rule__UserOperator__Group__16__Impl : ( 'is_fsym' ) ;
    public final void rule__UserOperator__Group__16__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4306:1: ( ( 'is_fsym' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4307:1: ( 'is_fsym' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4307:1: ( 'is_fsym' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4308:1: 'is_fsym'
            {
             before(grammarAccess.getUserOperatorAccess().getIs_fsymKeyword_16()); 
            match(input,31,FOLLOW_31_in_rule__UserOperator__Group__16__Impl8607); 
             after(grammarAccess.getUserOperatorAccess().getIs_fsymKeyword_16()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__16__Impl"


    // $ANTLR start "rule__UserOperator__Group__17"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4321:1: rule__UserOperator__Group__17 : rule__UserOperator__Group__17__Impl rule__UserOperator__Group__18 ;
    public final void rule__UserOperator__Group__17() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4325:1: ( rule__UserOperator__Group__17__Impl rule__UserOperator__Group__18 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4326:2: rule__UserOperator__Group__17__Impl rule__UserOperator__Group__18
            {
            pushFollow(FOLLOW_rule__UserOperator__Group__17__Impl_in_rule__UserOperator__Group__178638);
            rule__UserOperator__Group__17__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__UserOperator__Group__18_in_rule__UserOperator__Group__178641);
            rule__UserOperator__Group__18();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__17"


    // $ANTLR start "rule__UserOperator__Group__17__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4333:1: rule__UserOperator__Group__17__Impl : ( '=' ) ;
    public final void rule__UserOperator__Group__17__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4337:1: ( ( '=' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4338:1: ( '=' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4338:1: ( '=' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4339:1: '='
            {
             before(grammarAccess.getUserOperatorAccess().getEqualsSignKeyword_17()); 
            match(input,30,FOLLOW_30_in_rule__UserOperator__Group__17__Impl8669); 
             after(grammarAccess.getUserOperatorAccess().getEqualsSignKeyword_17()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__17__Impl"


    // $ANTLR start "rule__UserOperator__Group__18"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4352:1: rule__UserOperator__Group__18 : rule__UserOperator__Group__18__Impl rule__UserOperator__Group__19 ;
    public final void rule__UserOperator__Group__18() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4356:1: ( rule__UserOperator__Group__18__Impl rule__UserOperator__Group__19 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4357:2: rule__UserOperator__Group__18__Impl rule__UserOperator__Group__19
            {
            pushFollow(FOLLOW_rule__UserOperator__Group__18__Impl_in_rule__UserOperator__Group__188700);
            rule__UserOperator__Group__18__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__UserOperator__Group__19_in_rule__UserOperator__Group__188703);
            rule__UserOperator__Group__19();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__18"


    // $ANTLR start "rule__UserOperator__Group__18__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4364:1: rule__UserOperator__Group__18__Impl : ( ( rule__UserOperator__TestAssignment_18 ) ) ;
    public final void rule__UserOperator__Group__18__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4368:1: ( ( ( rule__UserOperator__TestAssignment_18 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4369:1: ( ( rule__UserOperator__TestAssignment_18 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4369:1: ( ( rule__UserOperator__TestAssignment_18 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4370:1: ( rule__UserOperator__TestAssignment_18 )
            {
             before(grammarAccess.getUserOperatorAccess().getTestAssignment_18()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4371:1: ( rule__UserOperator__TestAssignment_18 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4371:2: rule__UserOperator__TestAssignment_18
            {
            pushFollow(FOLLOW_rule__UserOperator__TestAssignment_18_in_rule__UserOperator__Group__18__Impl8730);
            rule__UserOperator__TestAssignment_18();

            state._fsp--;


            }

             after(grammarAccess.getUserOperatorAccess().getTestAssignment_18()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__18__Impl"


    // $ANTLR start "rule__UserOperator__Group__19"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4381:1: rule__UserOperator__Group__19 : rule__UserOperator__Group__19__Impl rule__UserOperator__Group__20 ;
    public final void rule__UserOperator__Group__19() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4385:1: ( rule__UserOperator__Group__19__Impl rule__UserOperator__Group__20 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4386:2: rule__UserOperator__Group__19__Impl rule__UserOperator__Group__20
            {
            pushFollow(FOLLOW_rule__UserOperator__Group__19__Impl_in_rule__UserOperator__Group__198760);
            rule__UserOperator__Group__19__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__UserOperator__Group__20_in_rule__UserOperator__Group__198763);
            rule__UserOperator__Group__20();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__19"


    // $ANTLR start "rule__UserOperator__Group__19__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4393:1: rule__UserOperator__Group__19__Impl : ( ';' ) ;
    public final void rule__UserOperator__Group__19__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4397:1: ( ( ';' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4398:1: ( ';' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4398:1: ( ';' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4399:1: ';'
            {
             before(grammarAccess.getUserOperatorAccess().getSemicolonKeyword_19()); 
            match(input,12,FOLLOW_12_in_rule__UserOperator__Group__19__Impl8791); 
             after(grammarAccess.getUserOperatorAccess().getSemicolonKeyword_19()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__19__Impl"


    // $ANTLR start "rule__UserOperator__Group__20"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4412:1: rule__UserOperator__Group__20 : rule__UserOperator__Group__20__Impl ;
    public final void rule__UserOperator__Group__20() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4416:1: ( rule__UserOperator__Group__20__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4417:2: rule__UserOperator__Group__20__Impl
            {
            pushFollow(FOLLOW_rule__UserOperator__Group__20__Impl_in_rule__UserOperator__Group__208822);
            rule__UserOperator__Group__20__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__20"


    // $ANTLR start "rule__UserOperator__Group__20__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4423:1: rule__UserOperator__Group__20__Impl : ( '}' ) ;
    public final void rule__UserOperator__Group__20__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4427:1: ( ( '}' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4428:1: ( '}' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4428:1: ( '}' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4429:1: '}'
            {
             before(grammarAccess.getUserOperatorAccess().getRightCurlyBracketKeyword_20()); 
            match(input,17,FOLLOW_17_in_rule__UserOperator__Group__20__Impl8850); 
             after(grammarAccess.getUserOperatorAccess().getRightCurlyBracketKeyword_20()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group__20__Impl"


    // $ANTLR start "rule__UserOperator__Group_4__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4484:1: rule__UserOperator__Group_4__0 : rule__UserOperator__Group_4__0__Impl rule__UserOperator__Group_4__1 ;
    public final void rule__UserOperator__Group_4__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4488:1: ( rule__UserOperator__Group_4__0__Impl rule__UserOperator__Group_4__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4489:2: rule__UserOperator__Group_4__0__Impl rule__UserOperator__Group_4__1
            {
            pushFollow(FOLLOW_rule__UserOperator__Group_4__0__Impl_in_rule__UserOperator__Group_4__08923);
            rule__UserOperator__Group_4__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__UserOperator__Group_4__1_in_rule__UserOperator__Group_4__08926);
            rule__UserOperator__Group_4__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group_4__0"


    // $ANTLR start "rule__UserOperator__Group_4__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4496:1: rule__UserOperator__Group_4__0__Impl : ( ',' ) ;
    public final void rule__UserOperator__Group_4__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4500:1: ( ( ',' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4501:1: ( ',' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4501:1: ( ',' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4502:1: ','
            {
             before(grammarAccess.getUserOperatorAccess().getCommaKeyword_4_0()); 
            match(input,19,FOLLOW_19_in_rule__UserOperator__Group_4__0__Impl8954); 
             after(grammarAccess.getUserOperatorAccess().getCommaKeyword_4_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group_4__0__Impl"


    // $ANTLR start "rule__UserOperator__Group_4__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4515:1: rule__UserOperator__Group_4__1 : rule__UserOperator__Group_4__1__Impl ;
    public final void rule__UserOperator__Group_4__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4519:1: ( rule__UserOperator__Group_4__1__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4520:2: rule__UserOperator__Group_4__1__Impl
            {
            pushFollow(FOLLOW_rule__UserOperator__Group_4__1__Impl_in_rule__UserOperator__Group_4__18985);
            rule__UserOperator__Group_4__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group_4__1"


    // $ANTLR start "rule__UserOperator__Group_4__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4526:1: rule__UserOperator__Group_4__1__Impl : ( ( rule__UserOperator__ParametersAssignment_4_1 ) ) ;
    public final void rule__UserOperator__Group_4__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4530:1: ( ( ( rule__UserOperator__ParametersAssignment_4_1 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4531:1: ( ( rule__UserOperator__ParametersAssignment_4_1 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4531:1: ( ( rule__UserOperator__ParametersAssignment_4_1 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4532:1: ( rule__UserOperator__ParametersAssignment_4_1 )
            {
             before(grammarAccess.getUserOperatorAccess().getParametersAssignment_4_1()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4533:1: ( rule__UserOperator__ParametersAssignment_4_1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4533:2: rule__UserOperator__ParametersAssignment_4_1
            {
            pushFollow(FOLLOW_rule__UserOperator__ParametersAssignment_4_1_in_rule__UserOperator__Group_4__1__Impl9012);
            rule__UserOperator__ParametersAssignment_4_1();

            state._fsp--;


            }

             after(grammarAccess.getUserOperatorAccess().getParametersAssignment_4_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group_4__1__Impl"


    // $ANTLR start "rule__UserOperator__Group_10__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4547:1: rule__UserOperator__Group_10__0 : rule__UserOperator__Group_10__0__Impl rule__UserOperator__Group_10__1 ;
    public final void rule__UserOperator__Group_10__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4551:1: ( rule__UserOperator__Group_10__0__Impl rule__UserOperator__Group_10__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4552:2: rule__UserOperator__Group_10__0__Impl rule__UserOperator__Group_10__1
            {
            pushFollow(FOLLOW_rule__UserOperator__Group_10__0__Impl_in_rule__UserOperator__Group_10__09046);
            rule__UserOperator__Group_10__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__UserOperator__Group_10__1_in_rule__UserOperator__Group_10__09049);
            rule__UserOperator__Group_10__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group_10__0"


    // $ANTLR start "rule__UserOperator__Group_10__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4559:1: rule__UserOperator__Group_10__0__Impl : ( ';' ) ;
    public final void rule__UserOperator__Group_10__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4563:1: ( ( ';' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4564:1: ( ';' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4564:1: ( ';' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4565:1: ';'
            {
             before(grammarAccess.getUserOperatorAccess().getSemicolonKeyword_10_0()); 
            match(input,12,FOLLOW_12_in_rule__UserOperator__Group_10__0__Impl9077); 
             after(grammarAccess.getUserOperatorAccess().getSemicolonKeyword_10_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group_10__0__Impl"


    // $ANTLR start "rule__UserOperator__Group_10__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4578:1: rule__UserOperator__Group_10__1 : rule__UserOperator__Group_10__1__Impl ;
    public final void rule__UserOperator__Group_10__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4582:1: ( rule__UserOperator__Group_10__1__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4583:2: rule__UserOperator__Group_10__1__Impl
            {
            pushFollow(FOLLOW_rule__UserOperator__Group_10__1__Impl_in_rule__UserOperator__Group_10__19108);
            rule__UserOperator__Group_10__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group_10__1"


    // $ANTLR start "rule__UserOperator__Group_10__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4589:1: rule__UserOperator__Group_10__1__Impl : ( ( rule__UserOperator__AccessorsAssignment_10_1 ) ) ;
    public final void rule__UserOperator__Group_10__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4593:1: ( ( ( rule__UserOperator__AccessorsAssignment_10_1 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4594:1: ( ( rule__UserOperator__AccessorsAssignment_10_1 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4594:1: ( ( rule__UserOperator__AccessorsAssignment_10_1 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4595:1: ( rule__UserOperator__AccessorsAssignment_10_1 )
            {
             before(grammarAccess.getUserOperatorAccess().getAccessorsAssignment_10_1()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4596:1: ( rule__UserOperator__AccessorsAssignment_10_1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4596:2: rule__UserOperator__AccessorsAssignment_10_1
            {
            pushFollow(FOLLOW_rule__UserOperator__AccessorsAssignment_10_1_in_rule__UserOperator__Group_10__1__Impl9135);
            rule__UserOperator__AccessorsAssignment_10_1();

            state._fsp--;


            }

             after(grammarAccess.getUserOperatorAccess().getAccessorsAssignment_10_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__Group_10__1__Impl"


    // $ANTLR start "rule__Parameter__Group__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4610:1: rule__Parameter__Group__0 : rule__Parameter__Group__0__Impl rule__Parameter__Group__1 ;
    public final void rule__Parameter__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4614:1: ( rule__Parameter__Group__0__Impl rule__Parameter__Group__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4615:2: rule__Parameter__Group__0__Impl rule__Parameter__Group__1
            {
            pushFollow(FOLLOW_rule__Parameter__Group__0__Impl_in_rule__Parameter__Group__09169);
            rule__Parameter__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Parameter__Group__1_in_rule__Parameter__Group__09172);
            rule__Parameter__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Parameter__Group__0"


    // $ANTLR start "rule__Parameter__Group__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4622:1: rule__Parameter__Group__0__Impl : ( ( rule__Parameter__TypeAssignment_0 ) ) ;
    public final void rule__Parameter__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4626:1: ( ( ( rule__Parameter__TypeAssignment_0 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4627:1: ( ( rule__Parameter__TypeAssignment_0 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4627:1: ( ( rule__Parameter__TypeAssignment_0 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4628:1: ( rule__Parameter__TypeAssignment_0 )
            {
             before(grammarAccess.getParameterAccess().getTypeAssignment_0()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4629:1: ( rule__Parameter__TypeAssignment_0 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4629:2: rule__Parameter__TypeAssignment_0
            {
            pushFollow(FOLLOW_rule__Parameter__TypeAssignment_0_in_rule__Parameter__Group__0__Impl9199);
            rule__Parameter__TypeAssignment_0();

            state._fsp--;


            }

             after(grammarAccess.getParameterAccess().getTypeAssignment_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Parameter__Group__0__Impl"


    // $ANTLR start "rule__Parameter__Group__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4639:1: rule__Parameter__Group__1 : rule__Parameter__Group__1__Impl ;
    public final void rule__Parameter__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4643:1: ( rule__Parameter__Group__1__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4644:2: rule__Parameter__Group__1__Impl
            {
            pushFollow(FOLLOW_rule__Parameter__Group__1__Impl_in_rule__Parameter__Group__19229);
            rule__Parameter__Group__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Parameter__Group__1"


    // $ANTLR start "rule__Parameter__Group__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4650:1: rule__Parameter__Group__1__Impl : ( ( rule__Parameter__NameAssignment_1 ) ) ;
    public final void rule__Parameter__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4654:1: ( ( ( rule__Parameter__NameAssignment_1 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4655:1: ( ( rule__Parameter__NameAssignment_1 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4655:1: ( ( rule__Parameter__NameAssignment_1 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4656:1: ( rule__Parameter__NameAssignment_1 )
            {
             before(grammarAccess.getParameterAccess().getNameAssignment_1()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4657:1: ( rule__Parameter__NameAssignment_1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4657:2: rule__Parameter__NameAssignment_1
            {
            pushFollow(FOLLOW_rule__Parameter__NameAssignment_1_in_rule__Parameter__Group__1__Impl9256);
            rule__Parameter__NameAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getParameterAccess().getNameAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Parameter__Group__1__Impl"


    // $ANTLR start "rule__Accessor__Group__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4671:1: rule__Accessor__Group__0 : rule__Accessor__Group__0__Impl rule__Accessor__Group__1 ;
    public final void rule__Accessor__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4675:1: ( rule__Accessor__Group__0__Impl rule__Accessor__Group__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4676:2: rule__Accessor__Group__0__Impl rule__Accessor__Group__1
            {
            pushFollow(FOLLOW_rule__Accessor__Group__0__Impl_in_rule__Accessor__Group__09290);
            rule__Accessor__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Accessor__Group__1_in_rule__Accessor__Group__09293);
            rule__Accessor__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Accessor__Group__0"


    // $ANTLR start "rule__Accessor__Group__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4683:1: rule__Accessor__Group__0__Impl : ( 'slot' ) ;
    public final void rule__Accessor__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4687:1: ( ( 'slot' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4688:1: ( 'slot' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4688:1: ( 'slot' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4689:1: 'slot'
            {
             before(grammarAccess.getAccessorAccess().getSlotKeyword_0()); 
            match(input,32,FOLLOW_32_in_rule__Accessor__Group__0__Impl9321); 
             after(grammarAccess.getAccessorAccess().getSlotKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Accessor__Group__0__Impl"


    // $ANTLR start "rule__Accessor__Group__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4702:1: rule__Accessor__Group__1 : rule__Accessor__Group__1__Impl rule__Accessor__Group__2 ;
    public final void rule__Accessor__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4706:1: ( rule__Accessor__Group__1__Impl rule__Accessor__Group__2 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4707:2: rule__Accessor__Group__1__Impl rule__Accessor__Group__2
            {
            pushFollow(FOLLOW_rule__Accessor__Group__1__Impl_in_rule__Accessor__Group__19352);
            rule__Accessor__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Accessor__Group__2_in_rule__Accessor__Group__19355);
            rule__Accessor__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Accessor__Group__1"


    // $ANTLR start "rule__Accessor__Group__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4714:1: rule__Accessor__Group__1__Impl : ( ( rule__Accessor__SlotAssignment_1 ) ) ;
    public final void rule__Accessor__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4718:1: ( ( ( rule__Accessor__SlotAssignment_1 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4719:1: ( ( rule__Accessor__SlotAssignment_1 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4719:1: ( ( rule__Accessor__SlotAssignment_1 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4720:1: ( rule__Accessor__SlotAssignment_1 )
            {
             before(grammarAccess.getAccessorAccess().getSlotAssignment_1()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4721:1: ( rule__Accessor__SlotAssignment_1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4721:2: rule__Accessor__SlotAssignment_1
            {
            pushFollow(FOLLOW_rule__Accessor__SlotAssignment_1_in_rule__Accessor__Group__1__Impl9382);
            rule__Accessor__SlotAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getAccessorAccess().getSlotAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Accessor__Group__1__Impl"


    // $ANTLR start "rule__Accessor__Group__2"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4731:1: rule__Accessor__Group__2 : rule__Accessor__Group__2__Impl rule__Accessor__Group__3 ;
    public final void rule__Accessor__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4735:1: ( rule__Accessor__Group__2__Impl rule__Accessor__Group__3 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4736:2: rule__Accessor__Group__2__Impl rule__Accessor__Group__3
            {
            pushFollow(FOLLOW_rule__Accessor__Group__2__Impl_in_rule__Accessor__Group__29412);
            rule__Accessor__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__Accessor__Group__3_in_rule__Accessor__Group__29415);
            rule__Accessor__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Accessor__Group__2"


    // $ANTLR start "rule__Accessor__Group__2__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4743:1: rule__Accessor__Group__2__Impl : ( '=' ) ;
    public final void rule__Accessor__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4747:1: ( ( '=' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4748:1: ( '=' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4748:1: ( '=' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4749:1: '='
            {
             before(grammarAccess.getAccessorAccess().getEqualsSignKeyword_2()); 
            match(input,30,FOLLOW_30_in_rule__Accessor__Group__2__Impl9443); 
             after(grammarAccess.getAccessorAccess().getEqualsSignKeyword_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Accessor__Group__2__Impl"


    // $ANTLR start "rule__Accessor__Group__3"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4762:1: rule__Accessor__Group__3 : rule__Accessor__Group__3__Impl ;
    public final void rule__Accessor__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4766:1: ( rule__Accessor__Group__3__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4767:2: rule__Accessor__Group__3__Impl
            {
            pushFollow(FOLLOW_rule__Accessor__Group__3__Impl_in_rule__Accessor__Group__39474);
            rule__Accessor__Group__3__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Accessor__Group__3"


    // $ANTLR start "rule__Accessor__Group__3__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4773:1: rule__Accessor__Group__3__Impl : ( ( rule__Accessor__JavaAssignment_3 ) ) ;
    public final void rule__Accessor__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4777:1: ( ( ( rule__Accessor__JavaAssignment_3 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4778:1: ( ( rule__Accessor__JavaAssignment_3 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4778:1: ( ( rule__Accessor__JavaAssignment_3 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4779:1: ( rule__Accessor__JavaAssignment_3 )
            {
             before(grammarAccess.getAccessorAccess().getJavaAssignment_3()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4780:1: ( rule__Accessor__JavaAssignment_3 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4780:2: rule__Accessor__JavaAssignment_3
            {
            pushFollow(FOLLOW_rule__Accessor__JavaAssignment_3_in_rule__Accessor__Group__3__Impl9501);
            rule__Accessor__JavaAssignment_3();

            state._fsp--;


            }

             after(grammarAccess.getAccessorAccess().getJavaAssignment_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Accessor__Group__3__Impl"


    // $ANTLR start "rule__FeatureException__Group__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4798:1: rule__FeatureException__Group__0 : rule__FeatureException__Group__0__Impl rule__FeatureException__Group__1 ;
    public final void rule__FeatureException__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4802:1: ( rule__FeatureException__Group__0__Impl rule__FeatureException__Group__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4803:2: rule__FeatureException__Group__0__Impl rule__FeatureException__Group__1
            {
            pushFollow(FOLLOW_rule__FeatureException__Group__0__Impl_in_rule__FeatureException__Group__09539);
            rule__FeatureException__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__FeatureException__Group__1_in_rule__FeatureException__Group__09542);
            rule__FeatureException__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__FeatureException__Group__0"


    // $ANTLR start "rule__FeatureException__Group__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4810:1: rule__FeatureException__Group__0__Impl : ( 'ignore' ) ;
    public final void rule__FeatureException__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4814:1: ( ( 'ignore' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4815:1: ( 'ignore' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4815:1: ( 'ignore' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4816:1: 'ignore'
            {
             before(grammarAccess.getFeatureExceptionAccess().getIgnoreKeyword_0()); 
            match(input,33,FOLLOW_33_in_rule__FeatureException__Group__0__Impl9570); 
             after(grammarAccess.getFeatureExceptionAccess().getIgnoreKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__FeatureException__Group__0__Impl"


    // $ANTLR start "rule__FeatureException__Group__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4829:1: rule__FeatureException__Group__1 : rule__FeatureException__Group__1__Impl ;
    public final void rule__FeatureException__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4833:1: ( rule__FeatureException__Group__1__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4834:2: rule__FeatureException__Group__1__Impl
            {
            pushFollow(FOLLOW_rule__FeatureException__Group__1__Impl_in_rule__FeatureException__Group__19601);
            rule__FeatureException__Group__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__FeatureException__Group__1"


    // $ANTLR start "rule__FeatureException__Group__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4840:1: rule__FeatureException__Group__1__Impl : ( ( rule__FeatureException__FeatureAssignment_1 ) ) ;
    public final void rule__FeatureException__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4844:1: ( ( ( rule__FeatureException__FeatureAssignment_1 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4845:1: ( ( rule__FeatureException__FeatureAssignment_1 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4845:1: ( ( rule__FeatureException__FeatureAssignment_1 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4846:1: ( rule__FeatureException__FeatureAssignment_1 )
            {
             before(grammarAccess.getFeatureExceptionAccess().getFeatureAssignment_1()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4847:1: ( rule__FeatureException__FeatureAssignment_1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4847:2: rule__FeatureException__FeatureAssignment_1
            {
            pushFollow(FOLLOW_rule__FeatureException__FeatureAssignment_1_in_rule__FeatureException__Group__1__Impl9628);
            rule__FeatureException__FeatureAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getFeatureExceptionAccess().getFeatureAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__FeatureException__Group__1__Impl"


    // $ANTLR start "rule__SettedFeatureParameter__Group__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4861:1: rule__SettedFeatureParameter__Group__0 : rule__SettedFeatureParameter__Group__0__Impl rule__SettedFeatureParameter__Group__1 ;
    public final void rule__SettedFeatureParameter__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4865:1: ( rule__SettedFeatureParameter__Group__0__Impl rule__SettedFeatureParameter__Group__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4866:2: rule__SettedFeatureParameter__Group__0__Impl rule__SettedFeatureParameter__Group__1
            {
            pushFollow(FOLLOW_rule__SettedFeatureParameter__Group__0__Impl_in_rule__SettedFeatureParameter__Group__09662);
            rule__SettedFeatureParameter__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__SettedFeatureParameter__Group__1_in_rule__SettedFeatureParameter__Group__09665);
            rule__SettedFeatureParameter__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__SettedFeatureParameter__Group__0"


    // $ANTLR start "rule__SettedFeatureParameter__Group__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4873:1: rule__SettedFeatureParameter__Group__0__Impl : ( ( rule__SettedFeatureParameter__FeatureAssignment_0 ) ) ;
    public final void rule__SettedFeatureParameter__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4877:1: ( ( ( rule__SettedFeatureParameter__FeatureAssignment_0 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4878:1: ( ( rule__SettedFeatureParameter__FeatureAssignment_0 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4878:1: ( ( rule__SettedFeatureParameter__FeatureAssignment_0 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4879:1: ( rule__SettedFeatureParameter__FeatureAssignment_0 )
            {
             before(grammarAccess.getSettedFeatureParameterAccess().getFeatureAssignment_0()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4880:1: ( rule__SettedFeatureParameter__FeatureAssignment_0 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4880:2: rule__SettedFeatureParameter__FeatureAssignment_0
            {
            pushFollow(FOLLOW_rule__SettedFeatureParameter__FeatureAssignment_0_in_rule__SettedFeatureParameter__Group__0__Impl9692);
            rule__SettedFeatureParameter__FeatureAssignment_0();

            state._fsp--;


            }

             after(grammarAccess.getSettedFeatureParameterAccess().getFeatureAssignment_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__SettedFeatureParameter__Group__0__Impl"


    // $ANTLR start "rule__SettedFeatureParameter__Group__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4890:1: rule__SettedFeatureParameter__Group__1 : rule__SettedFeatureParameter__Group__1__Impl rule__SettedFeatureParameter__Group__2 ;
    public final void rule__SettedFeatureParameter__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4894:1: ( rule__SettedFeatureParameter__Group__1__Impl rule__SettedFeatureParameter__Group__2 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4895:2: rule__SettedFeatureParameter__Group__1__Impl rule__SettedFeatureParameter__Group__2
            {
            pushFollow(FOLLOW_rule__SettedFeatureParameter__Group__1__Impl_in_rule__SettedFeatureParameter__Group__19722);
            rule__SettedFeatureParameter__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__SettedFeatureParameter__Group__2_in_rule__SettedFeatureParameter__Group__19725);
            rule__SettedFeatureParameter__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__SettedFeatureParameter__Group__1"


    // $ANTLR start "rule__SettedFeatureParameter__Group__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4902:1: rule__SettedFeatureParameter__Group__1__Impl : ( '=' ) ;
    public final void rule__SettedFeatureParameter__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4906:1: ( ( '=' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4907:1: ( '=' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4907:1: ( '=' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4908:1: '='
            {
             before(grammarAccess.getSettedFeatureParameterAccess().getEqualsSignKeyword_1()); 
            match(input,30,FOLLOW_30_in_rule__SettedFeatureParameter__Group__1__Impl9753); 
             after(grammarAccess.getSettedFeatureParameterAccess().getEqualsSignKeyword_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__SettedFeatureParameter__Group__1__Impl"


    // $ANTLR start "rule__SettedFeatureParameter__Group__2"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4921:1: rule__SettedFeatureParameter__Group__2 : rule__SettedFeatureParameter__Group__2__Impl ;
    public final void rule__SettedFeatureParameter__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4925:1: ( rule__SettedFeatureParameter__Group__2__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4926:2: rule__SettedFeatureParameter__Group__2__Impl
            {
            pushFollow(FOLLOW_rule__SettedFeatureParameter__Group__2__Impl_in_rule__SettedFeatureParameter__Group__29784);
            rule__SettedFeatureParameter__Group__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__SettedFeatureParameter__Group__2"


    // $ANTLR start "rule__SettedFeatureParameter__Group__2__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4932:1: rule__SettedFeatureParameter__Group__2__Impl : ( ( rule__SettedFeatureParameter__ValueAssignment_2 ) ) ;
    public final void rule__SettedFeatureParameter__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4936:1: ( ( ( rule__SettedFeatureParameter__ValueAssignment_2 ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4937:1: ( ( rule__SettedFeatureParameter__ValueAssignment_2 ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4937:1: ( ( rule__SettedFeatureParameter__ValueAssignment_2 ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4938:1: ( rule__SettedFeatureParameter__ValueAssignment_2 )
            {
             before(grammarAccess.getSettedFeatureParameterAccess().getValueAssignment_2()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4939:1: ( rule__SettedFeatureParameter__ValueAssignment_2 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4939:2: rule__SettedFeatureParameter__ValueAssignment_2
            {
            pushFollow(FOLLOW_rule__SettedFeatureParameter__ValueAssignment_2_in_rule__SettedFeatureParameter__Group__2__Impl9811);
            rule__SettedFeatureParameter__ValueAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getSettedFeatureParameterAccess().getValueAssignment_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__SettedFeatureParameter__Group__2__Impl"


    // $ANTLR start "rule__FQN__Group__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4956:1: rule__FQN__Group__0 : rule__FQN__Group__0__Impl rule__FQN__Group__1 ;
    public final void rule__FQN__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4960:1: ( rule__FQN__Group__0__Impl rule__FQN__Group__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4961:2: rule__FQN__Group__0__Impl rule__FQN__Group__1
            {
            pushFollow(FOLLOW_rule__FQN__Group__0__Impl_in_rule__FQN__Group__09848);
            rule__FQN__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__FQN__Group__1_in_rule__FQN__Group__09851);
            rule__FQN__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__FQN__Group__0"


    // $ANTLR start "rule__FQN__Group__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4968:1: rule__FQN__Group__0__Impl : ( RULE_ID ) ;
    public final void rule__FQN__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4972:1: ( ( RULE_ID ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4973:1: ( RULE_ID )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4973:1: ( RULE_ID )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4974:1: RULE_ID
            {
             before(grammarAccess.getFQNAccess().getIDTerminalRuleCall_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__FQN__Group__0__Impl9878); 
             after(grammarAccess.getFQNAccess().getIDTerminalRuleCall_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__FQN__Group__0__Impl"


    // $ANTLR start "rule__FQN__Group__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4985:1: rule__FQN__Group__1 : rule__FQN__Group__1__Impl ;
    public final void rule__FQN__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4989:1: ( rule__FQN__Group__1__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4990:2: rule__FQN__Group__1__Impl
            {
            pushFollow(FOLLOW_rule__FQN__Group__1__Impl_in_rule__FQN__Group__19907);
            rule__FQN__Group__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__FQN__Group__1"


    // $ANTLR start "rule__FQN__Group__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:4996:1: rule__FQN__Group__1__Impl : ( ( rule__FQN__Group_1__0 )* ) ;
    public final void rule__FQN__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5000:1: ( ( ( rule__FQN__Group_1__0 )* ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5001:1: ( ( rule__FQN__Group_1__0 )* )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5001:1: ( ( rule__FQN__Group_1__0 )* )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5002:1: ( rule__FQN__Group_1__0 )*
            {
             before(grammarAccess.getFQNAccess().getGroup_1()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5003:1: ( rule__FQN__Group_1__0 )*
            loop26:
            do {
                int alt26=2;
                int LA26_0 = input.LA(1);

                if ( (LA26_0==34) ) {
                    alt26=1;
                }


                switch (alt26) {
            	case 1 :
            	    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5003:2: rule__FQN__Group_1__0
            	    {
            	    pushFollow(FOLLOW_rule__FQN__Group_1__0_in_rule__FQN__Group__1__Impl9934);
            	    rule__FQN__Group_1__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop26;
                }
            } while (true);

             after(grammarAccess.getFQNAccess().getGroup_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__FQN__Group__1__Impl"


    // $ANTLR start "rule__FQN__Group_1__0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5017:1: rule__FQN__Group_1__0 : rule__FQN__Group_1__0__Impl rule__FQN__Group_1__1 ;
    public final void rule__FQN__Group_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5021:1: ( rule__FQN__Group_1__0__Impl rule__FQN__Group_1__1 )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5022:2: rule__FQN__Group_1__0__Impl rule__FQN__Group_1__1
            {
            pushFollow(FOLLOW_rule__FQN__Group_1__0__Impl_in_rule__FQN__Group_1__09969);
            rule__FQN__Group_1__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_rule__FQN__Group_1__1_in_rule__FQN__Group_1__09972);
            rule__FQN__Group_1__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__FQN__Group_1__0"


    // $ANTLR start "rule__FQN__Group_1__0__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5029:1: rule__FQN__Group_1__0__Impl : ( '.' ) ;
    public final void rule__FQN__Group_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5033:1: ( ( '.' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5034:1: ( '.' )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5034:1: ( '.' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5035:1: '.'
            {
             before(grammarAccess.getFQNAccess().getFullStopKeyword_1_0()); 
            match(input,34,FOLLOW_34_in_rule__FQN__Group_1__0__Impl10000); 
             after(grammarAccess.getFQNAccess().getFullStopKeyword_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__FQN__Group_1__0__Impl"


    // $ANTLR start "rule__FQN__Group_1__1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5048:1: rule__FQN__Group_1__1 : rule__FQN__Group_1__1__Impl ;
    public final void rule__FQN__Group_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5052:1: ( rule__FQN__Group_1__1__Impl )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5053:2: rule__FQN__Group_1__1__Impl
            {
            pushFollow(FOLLOW_rule__FQN__Group_1__1__Impl_in_rule__FQN__Group_1__110031);
            rule__FQN__Group_1__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__FQN__Group_1__1"


    // $ANTLR start "rule__FQN__Group_1__1__Impl"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5059:1: rule__FQN__Group_1__1__Impl : ( RULE_ID ) ;
    public final void rule__FQN__Group_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5063:1: ( ( RULE_ID ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5064:1: ( RULE_ID )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5064:1: ( RULE_ID )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5065:1: RULE_ID
            {
             before(grammarAccess.getFQNAccess().getIDTerminalRuleCall_1_1()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__FQN__Group_1__1__Impl10058); 
             after(grammarAccess.getFQNAccess().getIDTerminalRuleCall_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__FQN__Group_1__1__Impl"


    // $ANTLR start "rule__Mapping__NameAssignment_1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5081:1: rule__Mapping__NameAssignment_1 : ( RULE_ID ) ;
    public final void rule__Mapping__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5085:1: ( ( RULE_ID ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5086:1: ( RULE_ID )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5086:1: ( RULE_ID )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5087:1: RULE_ID
            {
             before(grammarAccess.getMappingAccess().getNameIDTerminalRuleCall_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Mapping__NameAssignment_110096); 
             after(grammarAccess.getMappingAccess().getNameIDTerminalRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__NameAssignment_1"


    // $ANTLR start "rule__Mapping__PrefixAssignment_3_1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5096:1: rule__Mapping__PrefixAssignment_3_1 : ( RULE_STRING ) ;
    public final void rule__Mapping__PrefixAssignment_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5100:1: ( ( RULE_STRING ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5101:1: ( RULE_STRING )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5101:1: ( RULE_STRING )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5102:1: RULE_STRING
            {
             before(grammarAccess.getMappingAccess().getPrefixSTRINGTerminalRuleCall_3_1_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Mapping__PrefixAssignment_3_110127); 
             after(grammarAccess.getMappingAccess().getPrefixSTRINGTerminalRuleCall_3_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__PrefixAssignment_3_1"


    // $ANTLR start "rule__Mapping__ImportsAssignment_4_1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5111:1: rule__Mapping__ImportsAssignment_4_1 : ( ruleImport ) ;
    public final void rule__Mapping__ImportsAssignment_4_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5115:1: ( ( ruleImport ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5116:1: ( ruleImport )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5116:1: ( ruleImport )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5117:1: ruleImport
            {
             before(grammarAccess.getMappingAccess().getImportsImportParserRuleCall_4_1_0()); 
            pushFollow(FOLLOW_ruleImport_in_rule__Mapping__ImportsAssignment_4_110158);
            ruleImport();

            state._fsp--;

             after(grammarAccess.getMappingAccess().getImportsImportParserRuleCall_4_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__ImportsAssignment_4_1"


    // $ANTLR start "rule__Mapping__TerminalsAssignment_5_2_0_0_2"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5126:1: rule__Mapping__TerminalsAssignment_5_2_0_0_2 : ( ruleTerminal ) ;
    public final void rule__Mapping__TerminalsAssignment_5_2_0_0_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5130:1: ( ( ruleTerminal ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5131:1: ( ruleTerminal )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5131:1: ( ruleTerminal )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5132:1: ruleTerminal
            {
             before(grammarAccess.getMappingAccess().getTerminalsTerminalParserRuleCall_5_2_0_0_2_0()); 
            pushFollow(FOLLOW_ruleTerminal_in_rule__Mapping__TerminalsAssignment_5_2_0_0_210189);
            ruleTerminal();

            state._fsp--;

             after(grammarAccess.getMappingAccess().getTerminalsTerminalParserRuleCall_5_2_0_0_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__TerminalsAssignment_5_2_0_0_2"


    // $ANTLR start "rule__Mapping__TerminalsAssignment_5_2_0_0_3_1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5141:1: rule__Mapping__TerminalsAssignment_5_2_0_0_3_1 : ( ruleTerminal ) ;
    public final void rule__Mapping__TerminalsAssignment_5_2_0_0_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5145:1: ( ( ruleTerminal ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5146:1: ( ruleTerminal )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5146:1: ( ruleTerminal )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5147:1: ruleTerminal
            {
             before(grammarAccess.getMappingAccess().getTerminalsTerminalParserRuleCall_5_2_0_0_3_1_0()); 
            pushFollow(FOLLOW_ruleTerminal_in_rule__Mapping__TerminalsAssignment_5_2_0_0_3_110220);
            ruleTerminal();

            state._fsp--;

             after(grammarAccess.getMappingAccess().getTerminalsTerminalParserRuleCall_5_2_0_0_3_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__TerminalsAssignment_5_2_0_0_3_1"


    // $ANTLR start "rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_2"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5156:1: rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_2 : ( ( RULE_ID ) ) ;
    public final void rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5160:1: ( ( ( RULE_ID ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5161:1: ( ( RULE_ID ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5161:1: ( ( RULE_ID ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5162:1: ( RULE_ID )
            {
             before(grammarAccess.getMappingAccess().getExternalTerminalsTerminalCrossReference_5_2_0_1_2_0()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5163:1: ( RULE_ID )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5164:1: RULE_ID
            {
             before(grammarAccess.getMappingAccess().getExternalTerminalsTerminalIDTerminalRuleCall_5_2_0_1_2_0_1()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_210255); 
             after(grammarAccess.getMappingAccess().getExternalTerminalsTerminalIDTerminalRuleCall_5_2_0_1_2_0_1()); 

            }

             after(grammarAccess.getMappingAccess().getExternalTerminalsTerminalCrossReference_5_2_0_1_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_2"


    // $ANTLR start "rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_3_1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5175:1: rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_3_1 : ( ( RULE_ID ) ) ;
    public final void rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5179:1: ( ( ( RULE_ID ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5180:1: ( ( RULE_ID ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5180:1: ( ( RULE_ID ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5181:1: ( RULE_ID )
            {
             before(grammarAccess.getMappingAccess().getExternalTerminalsTerminalCrossReference_5_2_0_1_3_1_0()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5182:1: ( RULE_ID )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5183:1: RULE_ID
            {
             before(grammarAccess.getMappingAccess().getExternalTerminalsTerminalIDTerminalRuleCall_5_2_0_1_3_1_0_1()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_3_110294); 
             after(grammarAccess.getMappingAccess().getExternalTerminalsTerminalIDTerminalRuleCall_5_2_0_1_3_1_0_1()); 

            }

             after(grammarAccess.getMappingAccess().getExternalTerminalsTerminalCrossReference_5_2_0_1_3_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_3_1"


    // $ANTLR start "rule__Mapping__TerminalsAssignment_5_2_1_0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5194:1: rule__Mapping__TerminalsAssignment_5_2_1_0 : ( ruleTerminal ) ;
    public final void rule__Mapping__TerminalsAssignment_5_2_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5198:1: ( ( ruleTerminal ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5199:1: ( ruleTerminal )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5199:1: ( ruleTerminal )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5200:1: ruleTerminal
            {
             before(grammarAccess.getMappingAccess().getTerminalsTerminalParserRuleCall_5_2_1_0_0()); 
            pushFollow(FOLLOW_ruleTerminal_in_rule__Mapping__TerminalsAssignment_5_2_1_010329);
            ruleTerminal();

            state._fsp--;

             after(grammarAccess.getMappingAccess().getTerminalsTerminalParserRuleCall_5_2_1_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__TerminalsAssignment_5_2_1_0"


    // $ANTLR start "rule__Mapping__TerminalsAssignment_5_2_1_1_1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5209:1: rule__Mapping__TerminalsAssignment_5_2_1_1_1 : ( ruleTerminal ) ;
    public final void rule__Mapping__TerminalsAssignment_5_2_1_1_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5213:1: ( ( ruleTerminal ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5214:1: ( ruleTerminal )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5214:1: ( ruleTerminal )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5215:1: ruleTerminal
            {
             before(grammarAccess.getMappingAccess().getTerminalsTerminalParserRuleCall_5_2_1_1_1_0()); 
            pushFollow(FOLLOW_ruleTerminal_in_rule__Mapping__TerminalsAssignment_5_2_1_1_110360);
            ruleTerminal();

            state._fsp--;

             after(grammarAccess.getMappingAccess().getTerminalsTerminalParserRuleCall_5_2_1_1_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__TerminalsAssignment_5_2_1_1_1"


    // $ANTLR start "rule__Mapping__OperatorsAssignment_6_0_2"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5224:1: rule__Mapping__OperatorsAssignment_6_0_2 : ( ruleOperator ) ;
    public final void rule__Mapping__OperatorsAssignment_6_0_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5228:1: ( ( ruleOperator ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5229:1: ( ruleOperator )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5229:1: ( ruleOperator )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5230:1: ruleOperator
            {
             before(grammarAccess.getMappingAccess().getOperatorsOperatorParserRuleCall_6_0_2_0()); 
            pushFollow(FOLLOW_ruleOperator_in_rule__Mapping__OperatorsAssignment_6_0_210391);
            ruleOperator();

            state._fsp--;

             after(grammarAccess.getMappingAccess().getOperatorsOperatorParserRuleCall_6_0_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__OperatorsAssignment_6_0_2"


    // $ANTLR start "rule__Mapping__OperatorsAssignment_6_0_3_1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5239:1: rule__Mapping__OperatorsAssignment_6_0_3_1 : ( ruleOperator ) ;
    public final void rule__Mapping__OperatorsAssignment_6_0_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5243:1: ( ( ruleOperator ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5244:1: ( ruleOperator )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5244:1: ( ruleOperator )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5245:1: ruleOperator
            {
             before(grammarAccess.getMappingAccess().getOperatorsOperatorParserRuleCall_6_0_3_1_0()); 
            pushFollow(FOLLOW_ruleOperator_in_rule__Mapping__OperatorsAssignment_6_0_3_110422);
            ruleOperator();

            state._fsp--;

             after(grammarAccess.getMappingAccess().getOperatorsOperatorParserRuleCall_6_0_3_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__OperatorsAssignment_6_0_3_1"


    // $ANTLR start "rule__Mapping__ModulesAssignment_6_1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5254:1: rule__Mapping__ModulesAssignment_6_1 : ( ruleModule ) ;
    public final void rule__Mapping__ModulesAssignment_6_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5258:1: ( ( ruleModule ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5259:1: ( ruleModule )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5259:1: ( ruleModule )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5260:1: ruleModule
            {
             before(grammarAccess.getMappingAccess().getModulesModuleParserRuleCall_6_1_0()); 
            pushFollow(FOLLOW_ruleModule_in_rule__Mapping__ModulesAssignment_6_110453);
            ruleModule();

            state._fsp--;

             after(grammarAccess.getMappingAccess().getModulesModuleParserRuleCall_6_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Mapping__ModulesAssignment_6_1"


    // $ANTLR start "rule__Module__NameAssignment_1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5269:1: rule__Module__NameAssignment_1 : ( RULE_ID ) ;
    public final void rule__Module__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5273:1: ( ( RULE_ID ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5274:1: ( RULE_ID )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5274:1: ( RULE_ID )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5275:1: RULE_ID
            {
             before(grammarAccess.getModuleAccess().getNameIDTerminalRuleCall_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Module__NameAssignment_110484); 
             after(grammarAccess.getModuleAccess().getNameIDTerminalRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__NameAssignment_1"


    // $ANTLR start "rule__Module__OperatorsAssignment_3_2"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5284:1: rule__Module__OperatorsAssignment_3_2 : ( ruleOperator ) ;
    public final void rule__Module__OperatorsAssignment_3_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5288:1: ( ( ruleOperator ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5289:1: ( ruleOperator )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5289:1: ( ruleOperator )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5290:1: ruleOperator
            {
             before(grammarAccess.getModuleAccess().getOperatorsOperatorParserRuleCall_3_2_0()); 
            pushFollow(FOLLOW_ruleOperator_in_rule__Module__OperatorsAssignment_3_210515);
            ruleOperator();

            state._fsp--;

             after(grammarAccess.getModuleAccess().getOperatorsOperatorParserRuleCall_3_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__OperatorsAssignment_3_2"


    // $ANTLR start "rule__Module__OperatorsAssignment_3_3_1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5299:1: rule__Module__OperatorsAssignment_3_3_1 : ( ruleOperator ) ;
    public final void rule__Module__OperatorsAssignment_3_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5303:1: ( ( ruleOperator ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5304:1: ( ruleOperator )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5304:1: ( ruleOperator )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5305:1: ruleOperator
            {
             before(grammarAccess.getModuleAccess().getOperatorsOperatorParserRuleCall_3_3_1_0()); 
            pushFollow(FOLLOW_ruleOperator_in_rule__Module__OperatorsAssignment_3_3_110546);
            ruleOperator();

            state._fsp--;

             after(grammarAccess.getModuleAccess().getOperatorsOperatorParserRuleCall_3_3_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Module__OperatorsAssignment_3_3_1"


    // $ANTLR start "rule__Import__ImportURIAssignment"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5314:1: rule__Import__ImportURIAssignment : ( RULE_STRING ) ;
    public final void rule__Import__ImportURIAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5318:1: ( ( RULE_STRING ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5319:1: ( RULE_STRING )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5319:1: ( RULE_STRING )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5320:1: RULE_STRING
            {
             before(grammarAccess.getImportAccess().getImportURISTRINGTerminalRuleCall_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Import__ImportURIAssignment10577); 
             after(grammarAccess.getImportAccess().getImportURISTRINGTerminalRuleCall_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Import__ImportURIAssignment"


    // $ANTLR start "rule__Terminal__NameAssignment_0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5329:1: rule__Terminal__NameAssignment_0 : ( RULE_ID ) ;
    public final void rule__Terminal__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5333:1: ( ( RULE_ID ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5334:1: ( RULE_ID )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5334:1: ( RULE_ID )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5335:1: RULE_ID
            {
             before(grammarAccess.getTerminalAccess().getNameIDTerminalRuleCall_0_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Terminal__NameAssignment_010608); 
             after(grammarAccess.getTerminalAccess().getNameIDTerminalRuleCall_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Terminal__NameAssignment_0"


    // $ANTLR start "rule__Terminal__ClassAssignment_2"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5344:1: rule__Terminal__ClassAssignment_2 : ( ( ruleFQN ) ) ;
    public final void rule__Terminal__ClassAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5348:1: ( ( ( ruleFQN ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5349:1: ( ( ruleFQN ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5349:1: ( ( ruleFQN ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5350:1: ( ruleFQN )
            {
             before(grammarAccess.getTerminalAccess().getClassEClassCrossReference_2_0()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5351:1: ( ruleFQN )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5352:1: ruleFQN
            {
             before(grammarAccess.getTerminalAccess().getClassEClassFQNParserRuleCall_2_0_1()); 
            pushFollow(FOLLOW_ruleFQN_in_rule__Terminal__ClassAssignment_210643);
            ruleFQN();

            state._fsp--;

             after(grammarAccess.getTerminalAccess().getClassEClassFQNParserRuleCall_2_0_1()); 

            }

             after(grammarAccess.getTerminalAccess().getClassEClassCrossReference_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Terminal__ClassAssignment_2"


    // $ANTLR start "rule__Terminal__ManyAssignment_3"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5363:1: rule__Terminal__ManyAssignment_3 : ( ( '[]' ) ) ;
    public final void rule__Terminal__ManyAssignment_3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5367:1: ( ( ( '[]' ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5368:1: ( ( '[]' ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5368:1: ( ( '[]' ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5369:1: ( '[]' )
            {
             before(grammarAccess.getTerminalAccess().getManyLeftSquareBracketRightSquareBracketKeyword_3_0()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5370:1: ( '[]' )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5371:1: '[]'
            {
             before(grammarAccess.getTerminalAccess().getManyLeftSquareBracketRightSquareBracketKeyword_3_0()); 
            match(input,35,FOLLOW_35_in_rule__Terminal__ManyAssignment_310683); 
             after(grammarAccess.getTerminalAccess().getManyLeftSquareBracketRightSquareBracketKeyword_3_0()); 

            }

             after(grammarAccess.getTerminalAccess().getManyLeftSquareBracketRightSquareBracketKeyword_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Terminal__ManyAssignment_3"


    // $ANTLR start "rule__AliasOperator__NameAssignment_1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5386:1: rule__AliasOperator__NameAssignment_1 : ( RULE_ID ) ;
    public final void rule__AliasOperator__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5390:1: ( ( RULE_ID ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5391:1: ( RULE_ID )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5391:1: ( RULE_ID )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5392:1: RULE_ID
            {
             before(grammarAccess.getAliasOperatorAccess().getNameIDTerminalRuleCall_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__AliasOperator__NameAssignment_110722); 
             after(grammarAccess.getAliasOperatorAccess().getNameIDTerminalRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AliasOperator__NameAssignment_1"


    // $ANTLR start "rule__AliasOperator__OpAssignment_3"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5401:1: rule__AliasOperator__OpAssignment_3 : ( ( RULE_ID ) ) ;
    public final void rule__AliasOperator__OpAssignment_3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5405:1: ( ( ( RULE_ID ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5406:1: ( ( RULE_ID ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5406:1: ( ( RULE_ID ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5407:1: ( RULE_ID )
            {
             before(grammarAccess.getAliasOperatorAccess().getOpOperatorCrossReference_3_0()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5408:1: ( RULE_ID )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5409:1: RULE_ID
            {
             before(grammarAccess.getAliasOperatorAccess().getOpOperatorIDTerminalRuleCall_3_0_1()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__AliasOperator__OpAssignment_310757); 
             after(grammarAccess.getAliasOperatorAccess().getOpOperatorIDTerminalRuleCall_3_0_1()); 

            }

             after(grammarAccess.getAliasOperatorAccess().getOpOperatorCrossReference_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AliasOperator__OpAssignment_3"


    // $ANTLR start "rule__AliasOperator__NodesAssignment_5"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5420:1: rule__AliasOperator__NodesAssignment_5 : ( ruleAliasNode ) ;
    public final void rule__AliasOperator__NodesAssignment_5() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5424:1: ( ( ruleAliasNode ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5425:1: ( ruleAliasNode )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5425:1: ( ruleAliasNode )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5426:1: ruleAliasNode
            {
             before(grammarAccess.getAliasOperatorAccess().getNodesAliasNodeParserRuleCall_5_0()); 
            pushFollow(FOLLOW_ruleAliasNode_in_rule__AliasOperator__NodesAssignment_510792);
            ruleAliasNode();

            state._fsp--;

             after(grammarAccess.getAliasOperatorAccess().getNodesAliasNodeParserRuleCall_5_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AliasOperator__NodesAssignment_5"


    // $ANTLR start "rule__AliasOperator__NodesAssignment_6_1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5435:1: rule__AliasOperator__NodesAssignment_6_1 : ( ruleAliasNode ) ;
    public final void rule__AliasOperator__NodesAssignment_6_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5439:1: ( ( ruleAliasNode ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5440:1: ( ruleAliasNode )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5440:1: ( ruleAliasNode )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5441:1: ruleAliasNode
            {
             before(grammarAccess.getAliasOperatorAccess().getNodesAliasNodeParserRuleCall_6_1_0()); 
            pushFollow(FOLLOW_ruleAliasNode_in_rule__AliasOperator__NodesAssignment_6_110823);
            ruleAliasNode();

            state._fsp--;

             after(grammarAccess.getAliasOperatorAccess().getNodesAliasNodeParserRuleCall_6_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AliasOperator__NodesAssignment_6_1"


    // $ANTLR start "rule__FeatureNode__FeatureAssignment"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5450:1: rule__FeatureNode__FeatureAssignment : ( RULE_ID ) ;
    public final void rule__FeatureNode__FeatureAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5454:1: ( ( RULE_ID ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5455:1: ( RULE_ID )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5455:1: ( RULE_ID )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5456:1: RULE_ID
            {
             before(grammarAccess.getFeatureNodeAccess().getFeatureIDTerminalRuleCall_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__FeatureNode__FeatureAssignment10854); 
             after(grammarAccess.getFeatureNodeAccess().getFeatureIDTerminalRuleCall_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__FeatureNode__FeatureAssignment"


    // $ANTLR start "rule__OperatorNode__OpAssignment_0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5465:1: rule__OperatorNode__OpAssignment_0 : ( ( RULE_ID ) ) ;
    public final void rule__OperatorNode__OpAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5469:1: ( ( ( RULE_ID ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5470:1: ( ( RULE_ID ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5470:1: ( ( RULE_ID ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5471:1: ( RULE_ID )
            {
             before(grammarAccess.getOperatorNodeAccess().getOpOperatorCrossReference_0_0()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5472:1: ( RULE_ID )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5473:1: RULE_ID
            {
             before(grammarAccess.getOperatorNodeAccess().getOpOperatorIDTerminalRuleCall_0_0_1()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__OperatorNode__OpAssignment_010889); 
             after(grammarAccess.getOperatorNodeAccess().getOpOperatorIDTerminalRuleCall_0_0_1()); 

            }

             after(grammarAccess.getOperatorNodeAccess().getOpOperatorCrossReference_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorNode__OpAssignment_0"


    // $ANTLR start "rule__OperatorNode__NodesAssignment_2"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5484:1: rule__OperatorNode__NodesAssignment_2 : ( ruleAliasNode ) ;
    public final void rule__OperatorNode__NodesAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5488:1: ( ( ruleAliasNode ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5489:1: ( ruleAliasNode )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5489:1: ( ruleAliasNode )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5490:1: ruleAliasNode
            {
             before(grammarAccess.getOperatorNodeAccess().getNodesAliasNodeParserRuleCall_2_0()); 
            pushFollow(FOLLOW_ruleAliasNode_in_rule__OperatorNode__NodesAssignment_210924);
            ruleAliasNode();

            state._fsp--;

             after(grammarAccess.getOperatorNodeAccess().getNodesAliasNodeParserRuleCall_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorNode__NodesAssignment_2"


    // $ANTLR start "rule__OperatorNode__NodesAssignment_3_1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5499:1: rule__OperatorNode__NodesAssignment_3_1 : ( ruleAliasNode ) ;
    public final void rule__OperatorNode__NodesAssignment_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5503:1: ( ( ruleAliasNode ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5504:1: ( ruleAliasNode )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5504:1: ( ruleAliasNode )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5505:1: ruleAliasNode
            {
             before(grammarAccess.getOperatorNodeAccess().getNodesAliasNodeParserRuleCall_3_1_0()); 
            pushFollow(FOLLOW_ruleAliasNode_in_rule__OperatorNode__NodesAssignment_3_110955);
            ruleAliasNode();

            state._fsp--;

             after(grammarAccess.getOperatorNodeAccess().getNodesAliasNodeParserRuleCall_3_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__OperatorNode__NodesAssignment_3_1"


    // $ANTLR start "rule__ClassOperator__NameAssignment_1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5514:1: rule__ClassOperator__NameAssignment_1 : ( RULE_ID ) ;
    public final void rule__ClassOperator__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5518:1: ( ( RULE_ID ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5519:1: ( RULE_ID )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5519:1: ( RULE_ID )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5520:1: RULE_ID
            {
             before(grammarAccess.getClassOperatorAccess().getNameIDTerminalRuleCall_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__ClassOperator__NameAssignment_110986); 
             after(grammarAccess.getClassOperatorAccess().getNameIDTerminalRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperator__NameAssignment_1"


    // $ANTLR start "rule__ClassOperator__ClassAssignment_3"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5529:1: rule__ClassOperator__ClassAssignment_3 : ( ( ruleFQN ) ) ;
    public final void rule__ClassOperator__ClassAssignment_3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5533:1: ( ( ( ruleFQN ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5534:1: ( ( ruleFQN ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5534:1: ( ( ruleFQN ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5535:1: ( ruleFQN )
            {
             before(grammarAccess.getClassOperatorAccess().getClassEClassCrossReference_3_0()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5536:1: ( ruleFQN )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5537:1: ruleFQN
            {
             before(grammarAccess.getClassOperatorAccess().getClassEClassFQNParserRuleCall_3_0_1()); 
            pushFollow(FOLLOW_ruleFQN_in_rule__ClassOperator__ClassAssignment_311021);
            ruleFQN();

            state._fsp--;

             after(grammarAccess.getClassOperatorAccess().getClassEClassFQNParserRuleCall_3_0_1()); 

            }

             after(grammarAccess.getClassOperatorAccess().getClassEClassCrossReference_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperator__ClassAssignment_3"


    // $ANTLR start "rule__ClassOperatorWithExceptions__NameAssignment_1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5548:1: rule__ClassOperatorWithExceptions__NameAssignment_1 : ( RULE_ID ) ;
    public final void rule__ClassOperatorWithExceptions__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5552:1: ( ( RULE_ID ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5553:1: ( RULE_ID )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5553:1: ( RULE_ID )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5554:1: RULE_ID
            {
             before(grammarAccess.getClassOperatorWithExceptionsAccess().getNameIDTerminalRuleCall_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__ClassOperatorWithExceptions__NameAssignment_111056); 
             after(grammarAccess.getClassOperatorWithExceptionsAccess().getNameIDTerminalRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperatorWithExceptions__NameAssignment_1"


    // $ANTLR start "rule__ClassOperatorWithExceptions__ClassAssignment_3"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5563:1: rule__ClassOperatorWithExceptions__ClassAssignment_3 : ( ( ruleFQN ) ) ;
    public final void rule__ClassOperatorWithExceptions__ClassAssignment_3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5567:1: ( ( ( ruleFQN ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5568:1: ( ( ruleFQN ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5568:1: ( ( ruleFQN ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5569:1: ( ruleFQN )
            {
             before(grammarAccess.getClassOperatorWithExceptionsAccess().getClassEClassCrossReference_3_0()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5570:1: ( ruleFQN )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5571:1: ruleFQN
            {
             before(grammarAccess.getClassOperatorWithExceptionsAccess().getClassEClassFQNParserRuleCall_3_0_1()); 
            pushFollow(FOLLOW_ruleFQN_in_rule__ClassOperatorWithExceptions__ClassAssignment_311091);
            ruleFQN();

            state._fsp--;

             after(grammarAccess.getClassOperatorWithExceptionsAccess().getClassEClassFQNParserRuleCall_3_0_1()); 

            }

             after(grammarAccess.getClassOperatorWithExceptionsAccess().getClassEClassCrossReference_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperatorWithExceptions__ClassAssignment_3"


    // $ANTLR start "rule__ClassOperatorWithExceptions__ParametersAssignment_5"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5582:1: rule__ClassOperatorWithExceptions__ParametersAssignment_5 : ( ruleFeatureParameter ) ;
    public final void rule__ClassOperatorWithExceptions__ParametersAssignment_5() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5586:1: ( ( ruleFeatureParameter ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5587:1: ( ruleFeatureParameter )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5587:1: ( ruleFeatureParameter )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5588:1: ruleFeatureParameter
            {
             before(grammarAccess.getClassOperatorWithExceptionsAccess().getParametersFeatureParameterParserRuleCall_5_0()); 
            pushFollow(FOLLOW_ruleFeatureParameter_in_rule__ClassOperatorWithExceptions__ParametersAssignment_511126);
            ruleFeatureParameter();

            state._fsp--;

             after(grammarAccess.getClassOperatorWithExceptionsAccess().getParametersFeatureParameterParserRuleCall_5_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperatorWithExceptions__ParametersAssignment_5"


    // $ANTLR start "rule__ClassOperatorWithExceptions__ParametersAssignment_6_1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5597:1: rule__ClassOperatorWithExceptions__ParametersAssignment_6_1 : ( ruleFeatureParameter ) ;
    public final void rule__ClassOperatorWithExceptions__ParametersAssignment_6_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5601:1: ( ( ruleFeatureParameter ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5602:1: ( ruleFeatureParameter )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5602:1: ( ruleFeatureParameter )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5603:1: ruleFeatureParameter
            {
             before(grammarAccess.getClassOperatorWithExceptionsAccess().getParametersFeatureParameterParserRuleCall_6_1_0()); 
            pushFollow(FOLLOW_ruleFeatureParameter_in_rule__ClassOperatorWithExceptions__ParametersAssignment_6_111157);
            ruleFeatureParameter();

            state._fsp--;

             after(grammarAccess.getClassOperatorWithExceptionsAccess().getParametersFeatureParameterParserRuleCall_6_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__ClassOperatorWithExceptions__ParametersAssignment_6_1"


    // $ANTLR start "rule__UserOperator__NameAssignment_1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5612:1: rule__UserOperator__NameAssignment_1 : ( RULE_ID ) ;
    public final void rule__UserOperator__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5616:1: ( ( RULE_ID ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5617:1: ( RULE_ID )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5617:1: ( RULE_ID )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5618:1: RULE_ID
            {
             before(grammarAccess.getUserOperatorAccess().getNameIDTerminalRuleCall_1_0()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__UserOperator__NameAssignment_111188); 
             after(grammarAccess.getUserOperatorAccess().getNameIDTerminalRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__NameAssignment_1"


    // $ANTLR start "rule__UserOperator__ParametersAssignment_3"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5627:1: rule__UserOperator__ParametersAssignment_3 : ( ruleParameter ) ;
    public final void rule__UserOperator__ParametersAssignment_3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5631:1: ( ( ruleParameter ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5632:1: ( ruleParameter )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5632:1: ( ruleParameter )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5633:1: ruleParameter
            {
             before(grammarAccess.getUserOperatorAccess().getParametersParameterParserRuleCall_3_0()); 
            pushFollow(FOLLOW_ruleParameter_in_rule__UserOperator__ParametersAssignment_311219);
            ruleParameter();

            state._fsp--;

             after(grammarAccess.getUserOperatorAccess().getParametersParameterParserRuleCall_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__ParametersAssignment_3"


    // $ANTLR start "rule__UserOperator__ParametersAssignment_4_1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5642:1: rule__UserOperator__ParametersAssignment_4_1 : ( ruleParameter ) ;
    public final void rule__UserOperator__ParametersAssignment_4_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5646:1: ( ( ruleParameter ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5647:1: ( ruleParameter )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5647:1: ( ruleParameter )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5648:1: ruleParameter
            {
             before(grammarAccess.getUserOperatorAccess().getParametersParameterParserRuleCall_4_1_0()); 
            pushFollow(FOLLOW_ruleParameter_in_rule__UserOperator__ParametersAssignment_4_111250);
            ruleParameter();

            state._fsp--;

             after(grammarAccess.getUserOperatorAccess().getParametersParameterParserRuleCall_4_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__ParametersAssignment_4_1"


    // $ANTLR start "rule__UserOperator__TypeAssignment_7"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5657:1: rule__UserOperator__TypeAssignment_7 : ( ( RULE_ID ) ) ;
    public final void rule__UserOperator__TypeAssignment_7() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5661:1: ( ( ( RULE_ID ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5662:1: ( ( RULE_ID ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5662:1: ( ( RULE_ID ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5663:1: ( RULE_ID )
            {
             before(grammarAccess.getUserOperatorAccess().getTypeTerminalCrossReference_7_0()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5664:1: ( RULE_ID )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5665:1: RULE_ID
            {
             before(grammarAccess.getUserOperatorAccess().getTypeTerminalIDTerminalRuleCall_7_0_1()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__UserOperator__TypeAssignment_711285); 
             after(grammarAccess.getUserOperatorAccess().getTypeTerminalIDTerminalRuleCall_7_0_1()); 

            }

             after(grammarAccess.getUserOperatorAccess().getTypeTerminalCrossReference_7_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__TypeAssignment_7"


    // $ANTLR start "rule__UserOperator__AccessorsAssignment_9"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5676:1: rule__UserOperator__AccessorsAssignment_9 : ( ruleAccessor ) ;
    public final void rule__UserOperator__AccessorsAssignment_9() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5680:1: ( ( ruleAccessor ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5681:1: ( ruleAccessor )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5681:1: ( ruleAccessor )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5682:1: ruleAccessor
            {
             before(grammarAccess.getUserOperatorAccess().getAccessorsAccessorParserRuleCall_9_0()); 
            pushFollow(FOLLOW_ruleAccessor_in_rule__UserOperator__AccessorsAssignment_911320);
            ruleAccessor();

            state._fsp--;

             after(grammarAccess.getUserOperatorAccess().getAccessorsAccessorParserRuleCall_9_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__AccessorsAssignment_9"


    // $ANTLR start "rule__UserOperator__AccessorsAssignment_10_1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5691:1: rule__UserOperator__AccessorsAssignment_10_1 : ( ruleAccessor ) ;
    public final void rule__UserOperator__AccessorsAssignment_10_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5695:1: ( ( ruleAccessor ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5696:1: ( ruleAccessor )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5696:1: ( ruleAccessor )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5697:1: ruleAccessor
            {
             before(grammarAccess.getUserOperatorAccess().getAccessorsAccessorParserRuleCall_10_1_0()); 
            pushFollow(FOLLOW_ruleAccessor_in_rule__UserOperator__AccessorsAssignment_10_111351);
            ruleAccessor();

            state._fsp--;

             after(grammarAccess.getUserOperatorAccess().getAccessorsAccessorParserRuleCall_10_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__AccessorsAssignment_10_1"


    // $ANTLR start "rule__UserOperator__MakeAssignment_14"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5706:1: rule__UserOperator__MakeAssignment_14 : ( RULE_STRING ) ;
    public final void rule__UserOperator__MakeAssignment_14() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5710:1: ( ( RULE_STRING ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5711:1: ( RULE_STRING )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5711:1: ( RULE_STRING )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5712:1: RULE_STRING
            {
             before(grammarAccess.getUserOperatorAccess().getMakeSTRINGTerminalRuleCall_14_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__UserOperator__MakeAssignment_1411382); 
             after(grammarAccess.getUserOperatorAccess().getMakeSTRINGTerminalRuleCall_14_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__MakeAssignment_14"


    // $ANTLR start "rule__UserOperator__TestAssignment_18"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5721:1: rule__UserOperator__TestAssignment_18 : ( RULE_STRING ) ;
    public final void rule__UserOperator__TestAssignment_18() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5725:1: ( ( RULE_STRING ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5726:1: ( RULE_STRING )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5726:1: ( RULE_STRING )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5727:1: RULE_STRING
            {
             before(grammarAccess.getUserOperatorAccess().getTestSTRINGTerminalRuleCall_18_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__UserOperator__TestAssignment_1811413); 
             after(grammarAccess.getUserOperatorAccess().getTestSTRINGTerminalRuleCall_18_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__UserOperator__TestAssignment_18"


    // $ANTLR start "rule__Parameter__TypeAssignment_0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5736:1: rule__Parameter__TypeAssignment_0 : ( ( RULE_ID ) ) ;
    public final void rule__Parameter__TypeAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5740:1: ( ( ( RULE_ID ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5741:1: ( ( RULE_ID ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5741:1: ( ( RULE_ID ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5742:1: ( RULE_ID )
            {
             before(grammarAccess.getParameterAccess().getTypeTerminalCrossReference_0_0()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5743:1: ( RULE_ID )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5744:1: RULE_ID
            {
             before(grammarAccess.getParameterAccess().getTypeTerminalIDTerminalRuleCall_0_0_1()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Parameter__TypeAssignment_011448); 
             after(grammarAccess.getParameterAccess().getTypeTerminalIDTerminalRuleCall_0_0_1()); 

            }

             after(grammarAccess.getParameterAccess().getTypeTerminalCrossReference_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Parameter__TypeAssignment_0"


    // $ANTLR start "rule__Parameter__NameAssignment_1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5755:1: rule__Parameter__NameAssignment_1 : ( ruleEString ) ;
    public final void rule__Parameter__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5759:1: ( ( ruleEString ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5760:1: ( ruleEString )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5760:1: ( ruleEString )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5761:1: ruleEString
            {
             before(grammarAccess.getParameterAccess().getNameEStringParserRuleCall_1_0()); 
            pushFollow(FOLLOW_ruleEString_in_rule__Parameter__NameAssignment_111483);
            ruleEString();

            state._fsp--;

             after(grammarAccess.getParameterAccess().getNameEStringParserRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Parameter__NameAssignment_1"


    // $ANTLR start "rule__Accessor__SlotAssignment_1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5770:1: rule__Accessor__SlotAssignment_1 : ( ( RULE_ID ) ) ;
    public final void rule__Accessor__SlotAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5774:1: ( ( ( RULE_ID ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5775:1: ( ( RULE_ID ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5775:1: ( ( RULE_ID ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5776:1: ( RULE_ID )
            {
             before(grammarAccess.getAccessorAccess().getSlotParameterCrossReference_1_0()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5777:1: ( RULE_ID )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5778:1: RULE_ID
            {
             before(grammarAccess.getAccessorAccess().getSlotParameterIDTerminalRuleCall_1_0_1()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__Accessor__SlotAssignment_111518); 
             after(grammarAccess.getAccessorAccess().getSlotParameterIDTerminalRuleCall_1_0_1()); 

            }

             after(grammarAccess.getAccessorAccess().getSlotParameterCrossReference_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Accessor__SlotAssignment_1"


    // $ANTLR start "rule__Accessor__JavaAssignment_3"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5789:1: rule__Accessor__JavaAssignment_3 : ( RULE_STRING ) ;
    public final void rule__Accessor__JavaAssignment_3() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5793:1: ( ( RULE_STRING ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5794:1: ( RULE_STRING )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5794:1: ( RULE_STRING )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5795:1: RULE_STRING
            {
             before(grammarAccess.getAccessorAccess().getJavaSTRINGTerminalRuleCall_3_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__Accessor__JavaAssignment_311553); 
             after(grammarAccess.getAccessorAccess().getJavaSTRINGTerminalRuleCall_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Accessor__JavaAssignment_3"


    // $ANTLR start "rule__FeatureException__FeatureAssignment_1"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5804:1: rule__FeatureException__FeatureAssignment_1 : ( ( RULE_ID ) ) ;
    public final void rule__FeatureException__FeatureAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5808:1: ( ( ( RULE_ID ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5809:1: ( ( RULE_ID ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5809:1: ( ( RULE_ID ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5810:1: ( RULE_ID )
            {
             before(grammarAccess.getFeatureExceptionAccess().getFeatureEStructuralFeatureCrossReference_1_0()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5811:1: ( RULE_ID )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5812:1: RULE_ID
            {
             before(grammarAccess.getFeatureExceptionAccess().getFeatureEStructuralFeatureIDTerminalRuleCall_1_0_1()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__FeatureException__FeatureAssignment_111588); 
             after(grammarAccess.getFeatureExceptionAccess().getFeatureEStructuralFeatureIDTerminalRuleCall_1_0_1()); 

            }

             after(grammarAccess.getFeatureExceptionAccess().getFeatureEStructuralFeatureCrossReference_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__FeatureException__FeatureAssignment_1"


    // $ANTLR start "rule__FeatureParameter__FeatureAssignment_0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5823:1: rule__FeatureParameter__FeatureAssignment_0 : ( ( RULE_ID ) ) ;
    public final void rule__FeatureParameter__FeatureAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5827:1: ( ( ( RULE_ID ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5828:1: ( ( RULE_ID ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5828:1: ( ( RULE_ID ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5829:1: ( RULE_ID )
            {
             before(grammarAccess.getFeatureParameterAccess().getFeatureEStructuralFeatureCrossReference_0_0()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5830:1: ( RULE_ID )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5831:1: RULE_ID
            {
             before(grammarAccess.getFeatureParameterAccess().getFeatureEStructuralFeatureIDTerminalRuleCall_0_0_1()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__FeatureParameter__FeatureAssignment_011627); 
             after(grammarAccess.getFeatureParameterAccess().getFeatureEStructuralFeatureIDTerminalRuleCall_0_0_1()); 

            }

             after(grammarAccess.getFeatureParameterAccess().getFeatureEStructuralFeatureCrossReference_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__FeatureParameter__FeatureAssignment_0"


    // $ANTLR start "rule__SettedFeatureParameter__FeatureAssignment_0"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5842:1: rule__SettedFeatureParameter__FeatureAssignment_0 : ( ( RULE_ID ) ) ;
    public final void rule__SettedFeatureParameter__FeatureAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5846:1: ( ( ( RULE_ID ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5847:1: ( ( RULE_ID ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5847:1: ( ( RULE_ID ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5848:1: ( RULE_ID )
            {
             before(grammarAccess.getSettedFeatureParameterAccess().getFeatureEStructuralFeatureCrossReference_0_0()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5849:1: ( RULE_ID )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5850:1: RULE_ID
            {
             before(grammarAccess.getSettedFeatureParameterAccess().getFeatureEStructuralFeatureIDTerminalRuleCall_0_0_1()); 
            match(input,RULE_ID,FOLLOW_RULE_ID_in_rule__SettedFeatureParameter__FeatureAssignment_011666); 
             after(grammarAccess.getSettedFeatureParameterAccess().getFeatureEStructuralFeatureIDTerminalRuleCall_0_0_1()); 

            }

             after(grammarAccess.getSettedFeatureParameterAccess().getFeatureEStructuralFeatureCrossReference_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__SettedFeatureParameter__FeatureAssignment_0"


    // $ANTLR start "rule__SettedFeatureParameter__ValueAssignment_2"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5861:1: rule__SettedFeatureParameter__ValueAssignment_2 : ( ruleSettedValue ) ;
    public final void rule__SettedFeatureParameter__ValueAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5865:1: ( ( ruleSettedValue ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5866:1: ( ruleSettedValue )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5866:1: ( ruleSettedValue )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5867:1: ruleSettedValue
            {
             before(grammarAccess.getSettedFeatureParameterAccess().getValueSettedValueParserRuleCall_2_0()); 
            pushFollow(FOLLOW_ruleSettedValue_in_rule__SettedFeatureParameter__ValueAssignment_211701);
            ruleSettedValue();

            state._fsp--;

             after(grammarAccess.getSettedFeatureParameterAccess().getValueSettedValueParserRuleCall_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__SettedFeatureParameter__ValueAssignment_2"


    // $ANTLR start "rule__JavaCodeValue__JavaAssignment"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5876:1: rule__JavaCodeValue__JavaAssignment : ( RULE_STRING ) ;
    public final void rule__JavaCodeValue__JavaAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5880:1: ( ( RULE_STRING ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5881:1: ( RULE_STRING )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5881:1: ( RULE_STRING )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5882:1: RULE_STRING
            {
             before(grammarAccess.getJavaCodeValueAccess().getJavaSTRINGTerminalRuleCall_0()); 
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_rule__JavaCodeValue__JavaAssignment11732); 
             after(grammarAccess.getJavaCodeValueAccess().getJavaSTRINGTerminalRuleCall_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__JavaCodeValue__JavaAssignment"


    // $ANTLR start "rule__LiteralValue__LiteralAssignment"
    // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5891:1: rule__LiteralValue__LiteralAssignment : ( ( ruleFQN ) ) ;
    public final void rule__LiteralValue__LiteralAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
            
        try {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5895:1: ( ( ( ruleFQN ) ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5896:1: ( ( ruleFQN ) )
            {
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5896:1: ( ( ruleFQN ) )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5897:1: ( ruleFQN )
            {
             before(grammarAccess.getLiteralValueAccess().getLiteralEEnumLiteralCrossReference_0()); 
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5898:1: ( ruleFQN )
            // ../tom.mapping.dsl.ui/src-gen/tom/mapping/dsl/ui/contentassist/antlr/internal/InternalTomMapping.g:5899:1: ruleFQN
            {
             before(grammarAccess.getLiteralValueAccess().getLiteralEEnumLiteralFQNParserRuleCall_0_1()); 
            pushFollow(FOLLOW_ruleFQN_in_rule__LiteralValue__LiteralAssignment11767);
            ruleFQN();

            state._fsp--;

             after(grammarAccess.getLiteralValueAccess().getLiteralEEnumLiteralFQNParserRuleCall_0_1()); 

            }

             after(grammarAccess.getLiteralValueAccess().getLiteralEEnumLiteralCrossReference_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__LiteralValue__LiteralAssignment"

    // Delegated rules


    protected DFA3 dfa3 = new DFA3(this);
    static final String DFA3_eotS =
        "\13\uffff";
    static final String DFA3_eofS =
        "\6\uffff\1\11\3\uffff\1\11";
    static final String DFA3_minS =
        "\1\30\1\5\1\uffff\1\31\1\5\1\uffff\1\14\1\5\2\uffff\1\14";
    static final String DFA3_maxS =
        "\1\34\1\5\1\uffff\1\32\1\5\1\uffff\1\42\1\5\2\uffff\1\42";
    static final String DFA3_acceptS =
        "\2\uffff\1\4\2\uffff\1\3\2\uffff\1\2\1\1\1\uffff";
    static final String DFA3_specialS =
        "\13\uffff}>";
    static final String[] DFA3_transitionS = {
            "\1\2\3\uffff\1\1",
            "\1\3",
            "",
            "\1\4\1\5",
            "\1\6",
            "",
            "\1\11\15\uffff\1\10\7\uffff\1\7",
            "\1\12",
            "",
            "",
            "\1\11\15\uffff\1\10\7\uffff\1\7"
    };

    static final short[] DFA3_eot = DFA.unpackEncodedString(DFA3_eotS);
    static final short[] DFA3_eof = DFA.unpackEncodedString(DFA3_eofS);
    static final char[] DFA3_min = DFA.unpackEncodedStringToUnsignedChars(DFA3_minS);
    static final char[] DFA3_max = DFA.unpackEncodedStringToUnsignedChars(DFA3_maxS);
    static final short[] DFA3_accept = DFA.unpackEncodedString(DFA3_acceptS);
    static final short[] DFA3_special = DFA.unpackEncodedString(DFA3_specialS);
    static final short[][] DFA3_transition;

    static {
        int numStates = DFA3_transitionS.length;
        DFA3_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA3_transition[i] = DFA.unpackEncodedString(DFA3_transitionS[i]);
        }
    }

    class DFA3 extends DFA {

        public DFA3(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 3;
            this.eot = DFA3_eot;
            this.eof = DFA3_eof;
            this.min = DFA3_min;
            this.max = DFA3_max;
            this.accept = DFA3_accept;
            this.special = DFA3_special;
            this.transition = DFA3_transition;
        }
        public String getDescription() {
            return "724:1: rule__Operator__Alternatives : ( ( ruleClassOperator ) | ( ruleClassOperatorWithExceptions ) | ( ruleUserOperator ) | ( ruleAliasOperator ) );";
        }
    }
 

    public static final BitSet FOLLOW_ruleMapping_in_entryRuleMapping61 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleMapping68 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group__0_in_ruleMapping94 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModule_in_entryRuleModule121 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleModule128 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Module__Group__0_in_ruleModule154 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperator_in_entryRuleOperator181 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleOperator188 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Operator__Alternatives_in_ruleOperator214 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleImport_in_entryRuleImport241 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleImport248 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Import__ImportURIAssignment_in_ruleImport274 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleTerminal_in_entryRuleTerminal301 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleTerminal308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Terminal__Group__0_in_ruleTerminal334 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAliasOperator_in_entryRuleAliasOperator361 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleAliasOperator368 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__AliasOperator__Group__0_in_ruleAliasOperator394 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAliasNode_in_entryRuleAliasNode421 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleAliasNode428 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__AliasNode__Alternatives_in_ruleAliasNode454 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFeatureNode_in_entryRuleFeatureNode481 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleFeatureNode488 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__FeatureNode__FeatureAssignment_in_ruleFeatureNode514 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperatorNode_in_entryRuleOperatorNode541 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleOperatorNode548 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorNode__Group__0_in_ruleOperatorNode574 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleClassOperator_in_entryRuleClassOperator601 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleClassOperator608 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ClassOperator__Group__0_in_ruleClassOperator634 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleClassOperatorWithExceptions_in_entryRuleClassOperatorWithExceptions661 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleClassOperatorWithExceptions668 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ClassOperatorWithExceptions__Group__0_in_ruleClassOperatorWithExceptions694 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleUserOperator_in_entryRuleUserOperator721 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleUserOperator728 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__0_in_ruleUserOperator754 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleParameter_in_entryRuleParameter781 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleParameter788 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Parameter__Group__0_in_ruleParameter814 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAccessor_in_entryRuleAccessor841 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleAccessor848 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Accessor__Group__0_in_ruleAccessor874 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFeatureException_in_entryRuleFeatureException901 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleFeatureException908 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__FeatureException__Group__0_in_ruleFeatureException934 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFeatureParameter_in_entryRuleFeatureParameter961 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleFeatureParameter968 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__FeatureParameter__Alternatives_in_ruleFeatureParameter994 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSettedFeatureParameter_in_entryRuleSettedFeatureParameter1021 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSettedFeatureParameter1028 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SettedFeatureParameter__Group__0_in_ruleSettedFeatureParameter1054 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSettedValue_in_entryRuleSettedValue1081 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSettedValue1088 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SettedValue__Alternatives_in_ruleSettedValue1114 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleJavaCodeValue_in_entryRuleJavaCodeValue1141 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleJavaCodeValue1148 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__JavaCodeValue__JavaAssignment_in_ruleJavaCodeValue1174 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleLiteralValue_in_entryRuleLiteralValue1201 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleLiteralValue1208 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__LiteralValue__LiteralAssignment_in_ruleLiteralValue1234 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleEString_in_entryRuleEString1261 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleEString1268 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__EString__Alternatives_in_ruleEString1294 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFQN_in_entryRuleFQN1325 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleFQN1332 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__FQN__Group__0_in_ruleFQN1358 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0__0_in_rule__Mapping__Alternatives_5_21394 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_1__0_in_rule__Mapping__Alternatives_5_21412 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_6_0__0_in_rule__Mapping__Alternatives_61445 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__ModulesAssignment_6_1_in_rule__Mapping__Alternatives_61463 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleClassOperator_in_rule__Operator__Alternatives1496 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleClassOperatorWithExceptions_in_rule__Operator__Alternatives1513 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleUserOperator_in_rule__Operator__Alternatives1530 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAliasOperator_in_rule__Operator__Alternatives1547 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFeatureNode_in_rule__AliasNode__Alternatives1579 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperatorNode_in_rule__AliasNode__Alternatives1596 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__FeatureParameter__FeatureAssignment_0_in_rule__FeatureParameter__Alternatives1628 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFeatureException_in_rule__FeatureParameter__Alternatives1646 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSettedFeatureParameter_in_rule__FeatureParameter__Alternatives1663 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleJavaCodeValue_in_rule__SettedValue__Alternatives1695 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleLiteralValue_in_rule__SettedValue__Alternatives1712 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__EString__Alternatives1744 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__EString__Alternatives1761 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group__0__Impl_in_rule__Mapping__Group__01792 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__Mapping__Group__1_in_rule__Mapping__Group__01795 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_11_in_rule__Mapping__Group__0__Impl1823 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group__1__Impl_in_rule__Mapping__Group__11854 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_rule__Mapping__Group__2_in_rule__Mapping__Group__11857 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__NameAssignment_1_in_rule__Mapping__Group__1__Impl1884 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group__2__Impl_in_rule__Mapping__Group__21914 = new BitSet(new long[]{0x000000000060E000L});
    public static final BitSet FOLLOW_rule__Mapping__Group__3_in_rule__Mapping__Group__21917 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__Mapping__Group__2__Impl1945 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group__3__Impl_in_rule__Mapping__Group__31976 = new BitSet(new long[]{0x000000000060E000L});
    public static final BitSet FOLLOW_rule__Mapping__Group__4_in_rule__Mapping__Group__31979 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_3__0_in_rule__Mapping__Group__3__Impl2006 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group__4__Impl_in_rule__Mapping__Group__42037 = new BitSet(new long[]{0x000000000060E000L});
    public static final BitSet FOLLOW_rule__Mapping__Group__5_in_rule__Mapping__Group__42040 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_4__0_in_rule__Mapping__Group__4__Impl2067 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_rule__Mapping__Group__5__Impl_in_rule__Mapping__Group__52098 = new BitSet(new long[]{0x000000000060E000L});
    public static final BitSet FOLLOW_rule__Mapping__Group__6_in_rule__Mapping__Group__52101 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5__0_in_rule__Mapping__Group__5__Impl2128 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group__6__Impl_in_rule__Mapping__Group__62159 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Alternatives_6_in_rule__Mapping__Group__6__Impl2186 = new BitSet(new long[]{0x0000000000600002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_3__0__Impl_in_rule__Mapping__Group_3__02231 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Mapping__Group_3__1_in_rule__Mapping__Group_3__02234 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_rule__Mapping__Group_3__0__Impl2262 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_3__1__Impl_in_rule__Mapping__Group_3__12293 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_rule__Mapping__Group_3__2_in_rule__Mapping__Group_3__12296 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__PrefixAssignment_3_1_in_rule__Mapping__Group_3__1__Impl2323 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_3__2__Impl_in_rule__Mapping__Group_3__22353 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__Mapping__Group_3__2__Impl2381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_4__0__Impl_in_rule__Mapping__Group_4__02418 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Mapping__Group_4__1_in_rule__Mapping__Group_4__02421 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_rule__Mapping__Group_4__0__Impl2449 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_4__1__Impl_in_rule__Mapping__Group_4__12480 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_rule__Mapping__Group_4__2_in_rule__Mapping__Group_4__12483 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__ImportsAssignment_4_1_in_rule__Mapping__Group_4__1__Impl2510 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_4__2__Impl_in_rule__Mapping__Group_4__22540 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__Mapping__Group_4__2__Impl2568 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5__0__Impl_in_rule__Mapping__Group_5__02605 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5__1_in_rule__Mapping__Group_5__02608 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_rule__Mapping__Group_5__0__Impl2636 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5__1__Impl_in_rule__Mapping__Group_5__12667 = new BitSet(new long[]{0x0000000000140020L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5__2_in_rule__Mapping__Group_5__12670 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_rule__Mapping__Group_5__1__Impl2698 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5__2__Impl_in_rule__Mapping__Group_5__22729 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5__3_in_rule__Mapping__Group_5__22732 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Alternatives_5_2_in_rule__Mapping__Group_5__2__Impl2759 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5__3__Impl_in_rule__Mapping__Group_5__32789 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Mapping__Group_5__3__Impl2817 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0__0__Impl_in_rule__Mapping__Group_5_2_0__02856 = new BitSet(new long[]{0x0000000000140000L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0__1_in_rule__Mapping__Group_5_2_0__02859 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_0__0_in_rule__Mapping__Group_5_2_0__0__Impl2886 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0__1__Impl_in_rule__Mapping__Group_5_2_0__12917 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_1__0_in_rule__Mapping__Group_5_2_0__1__Impl2944 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_0__0__Impl_in_rule__Mapping__Group_5_2_0_0__02979 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_0__1_in_rule__Mapping__Group_5_2_0_0__02982 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_rule__Mapping__Group_5_2_0_0__0__Impl3010 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_0__1__Impl_in_rule__Mapping__Group_5_2_0_0__13041 = new BitSet(new long[]{0x0000000000140020L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_0__2_in_rule__Mapping__Group_5_2_0_0__13044 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_rule__Mapping__Group_5_2_0_0__1__Impl3072 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_0__2__Impl_in_rule__Mapping__Group_5_2_0_0__23103 = new BitSet(new long[]{0x00000000000A0000L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_0__3_in_rule__Mapping__Group_5_2_0_0__23106 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__TerminalsAssignment_5_2_0_0_2_in_rule__Mapping__Group_5_2_0_0__2__Impl3133 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_0__3__Impl_in_rule__Mapping__Group_5_2_0_0__33163 = new BitSet(new long[]{0x00000000000A0000L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_0__4_in_rule__Mapping__Group_5_2_0_0__33166 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_0_3__0_in_rule__Mapping__Group_5_2_0_0__3__Impl3193 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_0__4__Impl_in_rule__Mapping__Group_5_2_0_0__43224 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Mapping__Group_5_2_0_0__4__Impl3252 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_0_3__0__Impl_in_rule__Mapping__Group_5_2_0_0_3__03293 = new BitSet(new long[]{0x0000000000140020L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_0_3__1_in_rule__Mapping__Group_5_2_0_0_3__03296 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_rule__Mapping__Group_5_2_0_0_3__0__Impl3324 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_0_3__1__Impl_in_rule__Mapping__Group_5_2_0_0_3__13355 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__TerminalsAssignment_5_2_0_0_3_1_in_rule__Mapping__Group_5_2_0_0_3__1__Impl3382 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_1__0__Impl_in_rule__Mapping__Group_5_2_0_1__03416 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_1__1_in_rule__Mapping__Group_5_2_0_1__03419 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_rule__Mapping__Group_5_2_0_1__0__Impl3447 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_1__1__Impl_in_rule__Mapping__Group_5_2_0_1__13478 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_1__2_in_rule__Mapping__Group_5_2_0_1__13481 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_rule__Mapping__Group_5_2_0_1__1__Impl3509 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_1__2__Impl_in_rule__Mapping__Group_5_2_0_1__23540 = new BitSet(new long[]{0x00000000000A0000L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_1__3_in_rule__Mapping__Group_5_2_0_1__23543 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_2_in_rule__Mapping__Group_5_2_0_1__2__Impl3570 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_1__3__Impl_in_rule__Mapping__Group_5_2_0_1__33600 = new BitSet(new long[]{0x00000000000A0000L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_1__4_in_rule__Mapping__Group_5_2_0_1__33603 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_1_3__0_in_rule__Mapping__Group_5_2_0_1__3__Impl3630 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_1__4__Impl_in_rule__Mapping__Group_5_2_0_1__43661 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Mapping__Group_5_2_0_1__4__Impl3689 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_1_3__0__Impl_in_rule__Mapping__Group_5_2_0_1_3__03730 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_1_3__1_in_rule__Mapping__Group_5_2_0_1_3__03733 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_rule__Mapping__Group_5_2_0_1_3__0__Impl3761 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_0_1_3__1__Impl_in_rule__Mapping__Group_5_2_0_1_3__13792 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_3_1_in_rule__Mapping__Group_5_2_0_1_3__1__Impl3819 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_1__0__Impl_in_rule__Mapping__Group_5_2_1__03853 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_1__1_in_rule__Mapping__Group_5_2_1__03856 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__TerminalsAssignment_5_2_1_0_in_rule__Mapping__Group_5_2_1__0__Impl3883 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_1__1__Impl_in_rule__Mapping__Group_5_2_1__13913 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_1_1__0_in_rule__Mapping__Group_5_2_1__1__Impl3940 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_1_1__0__Impl_in_rule__Mapping__Group_5_2_1_1__03975 = new BitSet(new long[]{0x0000000000140020L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_1_1__1_in_rule__Mapping__Group_5_2_1_1__03978 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_rule__Mapping__Group_5_2_1_1__0__Impl4006 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_5_2_1_1__1__Impl_in_rule__Mapping__Group_5_2_1_1__14037 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__TerminalsAssignment_5_2_1_1_1_in_rule__Mapping__Group_5_2_1_1__1__Impl4064 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_6_0__0__Impl_in_rule__Mapping__Group_6_0__04098 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_rule__Mapping__Group_6_0__1_in_rule__Mapping__Group_6_0__04101 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_rule__Mapping__Group_6_0__0__Impl4129 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_6_0__1__Impl_in_rule__Mapping__Group_6_0__14160 = new BitSet(new long[]{0x0000000011000000L});
    public static final BitSet FOLLOW_rule__Mapping__Group_6_0__2_in_rule__Mapping__Group_6_0__14163 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_rule__Mapping__Group_6_0__1__Impl4191 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_6_0__2__Impl_in_rule__Mapping__Group_6_0__24222 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_rule__Mapping__Group_6_0__3_in_rule__Mapping__Group_6_0__24225 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__OperatorsAssignment_6_0_2_in_rule__Mapping__Group_6_0__2__Impl4252 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_6_0__3__Impl_in_rule__Mapping__Group_6_0__34282 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_rule__Mapping__Group_6_0__4_in_rule__Mapping__Group_6_0__34285 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_6_0_3__0_in_rule__Mapping__Group_6_0__3__Impl4312 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_6_0__4__Impl_in_rule__Mapping__Group_6_0__44343 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_rule__Mapping__Group_6_0__5_in_rule__Mapping__Group_6_0__44346 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__Mapping__Group_6_0__4__Impl4374 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_6_0__5__Impl_in_rule__Mapping__Group_6_0__54405 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Mapping__Group_6_0__5__Impl4433 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_6_0_3__0__Impl_in_rule__Mapping__Group_6_0_3__04476 = new BitSet(new long[]{0x0000000011000000L});
    public static final BitSet FOLLOW_rule__Mapping__Group_6_0_3__1_in_rule__Mapping__Group_6_0_3__04479 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__Mapping__Group_6_0_3__0__Impl4507 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__Group_6_0_3__1__Impl_in_rule__Mapping__Group_6_0_3__14538 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Mapping__OperatorsAssignment_6_0_3_1_in_rule__Mapping__Group_6_0_3__1__Impl4565 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Module__Group__0__Impl_in_rule__Module__Group__04599 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__Module__Group__1_in_rule__Module__Group__04602 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_rule__Module__Group__0__Impl4630 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Module__Group__1__Impl_in_rule__Module__Group__14661 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_rule__Module__Group__2_in_rule__Module__Group__14664 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Module__NameAssignment_1_in_rule__Module__Group__1__Impl4691 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Module__Group__2__Impl_in_rule__Module__Group__24721 = new BitSet(new long[]{0x0000000000220000L});
    public static final BitSet FOLLOW_rule__Module__Group__3_in_rule__Module__Group__24724 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_rule__Module__Group__2__Impl4752 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Module__Group__3__Impl_in_rule__Module__Group__34783 = new BitSet(new long[]{0x0000000000220000L});
    public static final BitSet FOLLOW_rule__Module__Group__4_in_rule__Module__Group__34786 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Module__Group_3__0_in_rule__Module__Group__3__Impl4813 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Module__Group__4__Impl_in_rule__Module__Group__44844 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Module__Group__4__Impl4872 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Module__Group_3__0__Impl_in_rule__Module__Group_3__04913 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_rule__Module__Group_3__1_in_rule__Module__Group_3__04916 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_rule__Module__Group_3__0__Impl4944 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Module__Group_3__1__Impl_in_rule__Module__Group_3__14975 = new BitSet(new long[]{0x0000000011000000L});
    public static final BitSet FOLLOW_rule__Module__Group_3__2_in_rule__Module__Group_3__14978 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_rule__Module__Group_3__1__Impl5006 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Module__Group_3__2__Impl_in_rule__Module__Group_3__25037 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_rule__Module__Group_3__3_in_rule__Module__Group_3__25040 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Module__OperatorsAssignment_3_2_in_rule__Module__Group_3__2__Impl5067 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Module__Group_3__3__Impl_in_rule__Module__Group_3__35097 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_rule__Module__Group_3__4_in_rule__Module__Group_3__35100 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Module__Group_3_3__0_in_rule__Module__Group_3__3__Impl5127 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_rule__Module__Group_3__4__Impl_in_rule__Module__Group_3__45158 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_rule__Module__Group_3__5_in_rule__Module__Group_3__45161 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__Module__Group_3__4__Impl5189 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Module__Group_3__5__Impl_in_rule__Module__Group_3__55220 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__Module__Group_3__5__Impl5248 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Module__Group_3_3__0__Impl_in_rule__Module__Group_3_3__05291 = new BitSet(new long[]{0x0000000011000000L});
    public static final BitSet FOLLOW_rule__Module__Group_3_3__1_in_rule__Module__Group_3_3__05294 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__Module__Group_3_3__0__Impl5322 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Module__Group_3_3__1__Impl_in_rule__Module__Group_3_3__15353 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Module__OperatorsAssignment_3_3_1_in_rule__Module__Group_3_3__1__Impl5380 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Terminal__Group__0__Impl_in_rule__Terminal__Group__05414 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_rule__Terminal__Group__1_in_rule__Terminal__Group__05417 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Terminal__NameAssignment_0_in_rule__Terminal__Group__0__Impl5444 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Terminal__Group__1__Impl_in_rule__Terminal__Group__15474 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__Terminal__Group__2_in_rule__Terminal__Group__15477 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_rule__Terminal__Group__1__Impl5505 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Terminal__Group__2__Impl_in_rule__Terminal__Group__25536 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_rule__Terminal__Group__3_in_rule__Terminal__Group__25539 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Terminal__ClassAssignment_2_in_rule__Terminal__Group__2__Impl5566 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Terminal__Group__3__Impl_in_rule__Terminal__Group__35596 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Terminal__ManyAssignment_3_in_rule__Terminal__Group__3__Impl5623 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__AliasOperator__Group__0__Impl_in_rule__AliasOperator__Group__05662 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__AliasOperator__Group__1_in_rule__AliasOperator__Group__05665 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_rule__AliasOperator__Group__0__Impl5693 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__AliasOperator__Group__1__Impl_in_rule__AliasOperator__Group__15724 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_rule__AliasOperator__Group__2_in_rule__AliasOperator__Group__15727 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__AliasOperator__NameAssignment_1_in_rule__AliasOperator__Group__1__Impl5754 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__AliasOperator__Group__2__Impl_in_rule__AliasOperator__Group__25784 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__AliasOperator__Group__3_in_rule__AliasOperator__Group__25787 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_rule__AliasOperator__Group__2__Impl5815 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__AliasOperator__Group__3__Impl_in_rule__AliasOperator__Group__35846 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_rule__AliasOperator__Group__4_in_rule__AliasOperator__Group__35849 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__AliasOperator__OpAssignment_3_in_rule__AliasOperator__Group__3__Impl5876 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__AliasOperator__Group__4__Impl_in_rule__AliasOperator__Group__45906 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__AliasOperator__Group__5_in_rule__AliasOperator__Group__45909 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_rule__AliasOperator__Group__4__Impl5937 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__AliasOperator__Group__5__Impl_in_rule__AliasOperator__Group__55968 = new BitSet(new long[]{0x0000000008080000L});
    public static final BitSet FOLLOW_rule__AliasOperator__Group__6_in_rule__AliasOperator__Group__55971 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__AliasOperator__NodesAssignment_5_in_rule__AliasOperator__Group__5__Impl5998 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__AliasOperator__Group__6__Impl_in_rule__AliasOperator__Group__66028 = new BitSet(new long[]{0x0000000008080000L});
    public static final BitSet FOLLOW_rule__AliasOperator__Group__7_in_rule__AliasOperator__Group__66031 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__AliasOperator__Group_6__0_in_rule__AliasOperator__Group__6__Impl6058 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_rule__AliasOperator__Group__7__Impl_in_rule__AliasOperator__Group__76089 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_rule__AliasOperator__Group__7__Impl6117 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__AliasOperator__Group_6__0__Impl_in_rule__AliasOperator__Group_6__06164 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__AliasOperator__Group_6__1_in_rule__AliasOperator__Group_6__06167 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_rule__AliasOperator__Group_6__0__Impl6195 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__AliasOperator__Group_6__1__Impl_in_rule__AliasOperator__Group_6__16226 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__AliasOperator__NodesAssignment_6_1_in_rule__AliasOperator__Group_6__1__Impl6253 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorNode__Group__0__Impl_in_rule__OperatorNode__Group__06287 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_rule__OperatorNode__Group__1_in_rule__OperatorNode__Group__06290 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorNode__OpAssignment_0_in_rule__OperatorNode__Group__0__Impl6317 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorNode__Group__1__Impl_in_rule__OperatorNode__Group__16347 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__OperatorNode__Group__2_in_rule__OperatorNode__Group__16350 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_rule__OperatorNode__Group__1__Impl6378 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorNode__Group__2__Impl_in_rule__OperatorNode__Group__26409 = new BitSet(new long[]{0x0000000008080000L});
    public static final BitSet FOLLOW_rule__OperatorNode__Group__3_in_rule__OperatorNode__Group__26412 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorNode__NodesAssignment_2_in_rule__OperatorNode__Group__2__Impl6439 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorNode__Group__3__Impl_in_rule__OperatorNode__Group__36469 = new BitSet(new long[]{0x0000000008080000L});
    public static final BitSet FOLLOW_rule__OperatorNode__Group__4_in_rule__OperatorNode__Group__36472 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorNode__Group_3__0_in_rule__OperatorNode__Group__3__Impl6499 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_rule__OperatorNode__Group__4__Impl_in_rule__OperatorNode__Group__46530 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_rule__OperatorNode__Group__4__Impl6558 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorNode__Group_3__0__Impl_in_rule__OperatorNode__Group_3__06599 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__OperatorNode__Group_3__1_in_rule__OperatorNode__Group_3__06602 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_rule__OperatorNode__Group_3__0__Impl6630 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorNode__Group_3__1__Impl_in_rule__OperatorNode__Group_3__16661 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__OperatorNode__NodesAssignment_3_1_in_rule__OperatorNode__Group_3__1__Impl6688 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ClassOperator__Group__0__Impl_in_rule__ClassOperator__Group__06722 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__ClassOperator__Group__1_in_rule__ClassOperator__Group__06725 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_rule__ClassOperator__Group__0__Impl6753 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ClassOperator__Group__1__Impl_in_rule__ClassOperator__Group__16784 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_rule__ClassOperator__Group__2_in_rule__ClassOperator__Group__16787 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ClassOperator__NameAssignment_1_in_rule__ClassOperator__Group__1__Impl6814 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ClassOperator__Group__2__Impl_in_rule__ClassOperator__Group__26844 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__ClassOperator__Group__3_in_rule__ClassOperator__Group__26847 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_rule__ClassOperator__Group__2__Impl6875 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ClassOperator__Group__3__Impl_in_rule__ClassOperator__Group__36906 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ClassOperator__ClassAssignment_3_in_rule__ClassOperator__Group__3__Impl6933 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ClassOperatorWithExceptions__Group__0__Impl_in_rule__ClassOperatorWithExceptions__Group__06971 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__ClassOperatorWithExceptions__Group__1_in_rule__ClassOperatorWithExceptions__Group__06974 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_rule__ClassOperatorWithExceptions__Group__0__Impl7002 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ClassOperatorWithExceptions__Group__1__Impl_in_rule__ClassOperatorWithExceptions__Group__17033 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_rule__ClassOperatorWithExceptions__Group__2_in_rule__ClassOperatorWithExceptions__Group__17036 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ClassOperatorWithExceptions__NameAssignment_1_in_rule__ClassOperatorWithExceptions__Group__1__Impl7063 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ClassOperatorWithExceptions__Group__2__Impl_in_rule__ClassOperatorWithExceptions__Group__27093 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__ClassOperatorWithExceptions__Group__3_in_rule__ClassOperatorWithExceptions__Group__27096 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_rule__ClassOperatorWithExceptions__Group__2__Impl7124 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ClassOperatorWithExceptions__Group__3__Impl_in_rule__ClassOperatorWithExceptions__Group__37155 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_rule__ClassOperatorWithExceptions__Group__4_in_rule__ClassOperatorWithExceptions__Group__37158 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ClassOperatorWithExceptions__ClassAssignment_3_in_rule__ClassOperatorWithExceptions__Group__3__Impl7185 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ClassOperatorWithExceptions__Group__4__Impl_in_rule__ClassOperatorWithExceptions__Group__47215 = new BitSet(new long[]{0x0000000200000020L});
    public static final BitSet FOLLOW_rule__ClassOperatorWithExceptions__Group__5_in_rule__ClassOperatorWithExceptions__Group__47218 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_rule__ClassOperatorWithExceptions__Group__4__Impl7246 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ClassOperatorWithExceptions__Group__5__Impl_in_rule__ClassOperatorWithExceptions__Group__57277 = new BitSet(new long[]{0x0000000008080000L});
    public static final BitSet FOLLOW_rule__ClassOperatorWithExceptions__Group__6_in_rule__ClassOperatorWithExceptions__Group__57280 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ClassOperatorWithExceptions__ParametersAssignment_5_in_rule__ClassOperatorWithExceptions__Group__5__Impl7307 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ClassOperatorWithExceptions__Group__6__Impl_in_rule__ClassOperatorWithExceptions__Group__67337 = new BitSet(new long[]{0x0000000008080000L});
    public static final BitSet FOLLOW_rule__ClassOperatorWithExceptions__Group__7_in_rule__ClassOperatorWithExceptions__Group__67340 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ClassOperatorWithExceptions__Group_6__0_in_rule__ClassOperatorWithExceptions__Group__6__Impl7367 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_rule__ClassOperatorWithExceptions__Group__7__Impl_in_rule__ClassOperatorWithExceptions__Group__77398 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_rule__ClassOperatorWithExceptions__Group__7__Impl7426 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ClassOperatorWithExceptions__Group_6__0__Impl_in_rule__ClassOperatorWithExceptions__Group_6__07473 = new BitSet(new long[]{0x0000000200000020L});
    public static final BitSet FOLLOW_rule__ClassOperatorWithExceptions__Group_6__1_in_rule__ClassOperatorWithExceptions__Group_6__07476 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_rule__ClassOperatorWithExceptions__Group_6__0__Impl7504 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ClassOperatorWithExceptions__Group_6__1__Impl_in_rule__ClassOperatorWithExceptions__Group_6__17535 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__ClassOperatorWithExceptions__ParametersAssignment_6_1_in_rule__ClassOperatorWithExceptions__Group_6__1__Impl7562 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__0__Impl_in_rule__UserOperator__Group__07596 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__1_in_rule__UserOperator__Group__07599 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_rule__UserOperator__Group__0__Impl7627 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__1__Impl_in_rule__UserOperator__Group__17658 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__2_in_rule__UserOperator__Group__17661 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__NameAssignment_1_in_rule__UserOperator__Group__1__Impl7688 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__2__Impl_in_rule__UserOperator__Group__27718 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__3_in_rule__UserOperator__Group__27721 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_rule__UserOperator__Group__2__Impl7749 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__3__Impl_in_rule__UserOperator__Group__37780 = new BitSet(new long[]{0x0000000008080000L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__4_in_rule__UserOperator__Group__37783 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__ParametersAssignment_3_in_rule__UserOperator__Group__3__Impl7810 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__4__Impl_in_rule__UserOperator__Group__47840 = new BitSet(new long[]{0x0000000008080000L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__5_in_rule__UserOperator__Group__47843 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group_4__0_in_rule__UserOperator__Group__4__Impl7870 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__5__Impl_in_rule__UserOperator__Group__57901 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__6_in_rule__UserOperator__Group__57904 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_rule__UserOperator__Group__5__Impl7932 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__6__Impl_in_rule__UserOperator__Group__67963 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__7_in_rule__UserOperator__Group__67966 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_rule__UserOperator__Group__6__Impl7994 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__7__Impl_in_rule__UserOperator__Group__78025 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__8_in_rule__UserOperator__Group__78028 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__TypeAssignment_7_in_rule__UserOperator__Group__7__Impl8055 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__8__Impl_in_rule__UserOperator__Group__88085 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__9_in_rule__UserOperator__Group__88088 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_rule__UserOperator__Group__8__Impl8116 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__9__Impl_in_rule__UserOperator__Group__98147 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__10_in_rule__UserOperator__Group__98150 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__AccessorsAssignment_9_in_rule__UserOperator__Group__9__Impl8177 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__10__Impl_in_rule__UserOperator__Group__108207 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__11_in_rule__UserOperator__Group__108210 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group_10__0_in_rule__UserOperator__Group__10__Impl8237 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__11__Impl_in_rule__UserOperator__Group__118268 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__12_in_rule__UserOperator__Group__118271 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__UserOperator__Group__11__Impl8299 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__12__Impl_in_rule__UserOperator__Group__128330 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__13_in_rule__UserOperator__Group__128333 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_rule__UserOperator__Group__12__Impl8361 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__13__Impl_in_rule__UserOperator__Group__138392 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__14_in_rule__UserOperator__Group__138395 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_rule__UserOperator__Group__13__Impl8423 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__14__Impl_in_rule__UserOperator__Group__148454 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__15_in_rule__UserOperator__Group__148457 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__MakeAssignment_14_in_rule__UserOperator__Group__14__Impl8484 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__15__Impl_in_rule__UserOperator__Group__158514 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__16_in_rule__UserOperator__Group__158517 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__UserOperator__Group__15__Impl8545 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__16__Impl_in_rule__UserOperator__Group__168576 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__17_in_rule__UserOperator__Group__168579 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_rule__UserOperator__Group__16__Impl8607 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__17__Impl_in_rule__UserOperator__Group__178638 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__18_in_rule__UserOperator__Group__178641 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_rule__UserOperator__Group__17__Impl8669 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__18__Impl_in_rule__UserOperator__Group__188700 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__19_in_rule__UserOperator__Group__188703 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__TestAssignment_18_in_rule__UserOperator__Group__18__Impl8730 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__19__Impl_in_rule__UserOperator__Group__198760 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__20_in_rule__UserOperator__Group__198763 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__UserOperator__Group__19__Impl8791 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group__20__Impl_in_rule__UserOperator__Group__208822 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_rule__UserOperator__Group__20__Impl8850 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group_4__0__Impl_in_rule__UserOperator__Group_4__08923 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__UserOperator__Group_4__1_in_rule__UserOperator__Group_4__08926 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_rule__UserOperator__Group_4__0__Impl8954 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group_4__1__Impl_in_rule__UserOperator__Group_4__18985 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__ParametersAssignment_4_1_in_rule__UserOperator__Group_4__1__Impl9012 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group_10__0__Impl_in_rule__UserOperator__Group_10__09046 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_rule__UserOperator__Group_10__1_in_rule__UserOperator__Group_10__09049 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_rule__UserOperator__Group_10__0__Impl9077 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__Group_10__1__Impl_in_rule__UserOperator__Group_10__19108 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__UserOperator__AccessorsAssignment_10_1_in_rule__UserOperator__Group_10__1__Impl9135 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Parameter__Group__0__Impl_in_rule__Parameter__Group__09169 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__Parameter__Group__1_in_rule__Parameter__Group__09172 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Parameter__TypeAssignment_0_in_rule__Parameter__Group__0__Impl9199 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Parameter__Group__1__Impl_in_rule__Parameter__Group__19229 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Parameter__NameAssignment_1_in_rule__Parameter__Group__1__Impl9256 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Accessor__Group__0__Impl_in_rule__Accessor__Group__09290 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__Accessor__Group__1_in_rule__Accessor__Group__09293 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_rule__Accessor__Group__0__Impl9321 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Accessor__Group__1__Impl_in_rule__Accessor__Group__19352 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_rule__Accessor__Group__2_in_rule__Accessor__Group__19355 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Accessor__SlotAssignment_1_in_rule__Accessor__Group__1__Impl9382 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Accessor__Group__2__Impl_in_rule__Accessor__Group__29412 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_rule__Accessor__Group__3_in_rule__Accessor__Group__29415 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_rule__Accessor__Group__2__Impl9443 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Accessor__Group__3__Impl_in_rule__Accessor__Group__39474 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__Accessor__JavaAssignment_3_in_rule__Accessor__Group__3__Impl9501 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__FeatureException__Group__0__Impl_in_rule__FeatureException__Group__09539 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__FeatureException__Group__1_in_rule__FeatureException__Group__09542 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_rule__FeatureException__Group__0__Impl9570 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__FeatureException__Group__1__Impl_in_rule__FeatureException__Group__19601 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__FeatureException__FeatureAssignment_1_in_rule__FeatureException__Group__1__Impl9628 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SettedFeatureParameter__Group__0__Impl_in_rule__SettedFeatureParameter__Group__09662 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_rule__SettedFeatureParameter__Group__1_in_rule__SettedFeatureParameter__Group__09665 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SettedFeatureParameter__FeatureAssignment_0_in_rule__SettedFeatureParameter__Group__0__Impl9692 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SettedFeatureParameter__Group__1__Impl_in_rule__SettedFeatureParameter__Group__19722 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_rule__SettedFeatureParameter__Group__2_in_rule__SettedFeatureParameter__Group__19725 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_rule__SettedFeatureParameter__Group__1__Impl9753 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SettedFeatureParameter__Group__2__Impl_in_rule__SettedFeatureParameter__Group__29784 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__SettedFeatureParameter__ValueAssignment_2_in_rule__SettedFeatureParameter__Group__2__Impl9811 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__FQN__Group__0__Impl_in_rule__FQN__Group__09848 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_rule__FQN__Group__1_in_rule__FQN__Group__09851 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__FQN__Group__0__Impl9878 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__FQN__Group__1__Impl_in_rule__FQN__Group__19907 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__FQN__Group_1__0_in_rule__FQN__Group__1__Impl9934 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_rule__FQN__Group_1__0__Impl_in_rule__FQN__Group_1__09969 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_rule__FQN__Group_1__1_in_rule__FQN__Group_1__09972 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_rule__FQN__Group_1__0__Impl10000 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule__FQN__Group_1__1__Impl_in_rule__FQN__Group_1__110031 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__FQN__Group_1__1__Impl10058 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Mapping__NameAssignment_110096 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Mapping__PrefixAssignment_3_110127 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleImport_in_rule__Mapping__ImportsAssignment_4_110158 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleTerminal_in_rule__Mapping__TerminalsAssignment_5_2_0_0_210189 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleTerminal_in_rule__Mapping__TerminalsAssignment_5_2_0_0_3_110220 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_210255 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Mapping__ExternalTerminalsAssignment_5_2_0_1_3_110294 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleTerminal_in_rule__Mapping__TerminalsAssignment_5_2_1_010329 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleTerminal_in_rule__Mapping__TerminalsAssignment_5_2_1_1_110360 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperator_in_rule__Mapping__OperatorsAssignment_6_0_210391 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperator_in_rule__Mapping__OperatorsAssignment_6_0_3_110422 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModule_in_rule__Mapping__ModulesAssignment_6_110453 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Module__NameAssignment_110484 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperator_in_rule__Module__OperatorsAssignment_3_210515 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleOperator_in_rule__Module__OperatorsAssignment_3_3_110546 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Import__ImportURIAssignment10577 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Terminal__NameAssignment_010608 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFQN_in_rule__Terminal__ClassAssignment_210643 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_rule__Terminal__ManyAssignment_310683 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__AliasOperator__NameAssignment_110722 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__AliasOperator__OpAssignment_310757 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAliasNode_in_rule__AliasOperator__NodesAssignment_510792 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAliasNode_in_rule__AliasOperator__NodesAssignment_6_110823 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__FeatureNode__FeatureAssignment10854 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__OperatorNode__OpAssignment_010889 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAliasNode_in_rule__OperatorNode__NodesAssignment_210924 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAliasNode_in_rule__OperatorNode__NodesAssignment_3_110955 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__ClassOperator__NameAssignment_110986 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFQN_in_rule__ClassOperator__ClassAssignment_311021 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__ClassOperatorWithExceptions__NameAssignment_111056 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFQN_in_rule__ClassOperatorWithExceptions__ClassAssignment_311091 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFeatureParameter_in_rule__ClassOperatorWithExceptions__ParametersAssignment_511126 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFeatureParameter_in_rule__ClassOperatorWithExceptions__ParametersAssignment_6_111157 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__UserOperator__NameAssignment_111188 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleParameter_in_rule__UserOperator__ParametersAssignment_311219 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleParameter_in_rule__UserOperator__ParametersAssignment_4_111250 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__UserOperator__TypeAssignment_711285 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAccessor_in_rule__UserOperator__AccessorsAssignment_911320 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAccessor_in_rule__UserOperator__AccessorsAssignment_10_111351 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__UserOperator__MakeAssignment_1411382 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__UserOperator__TestAssignment_1811413 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Parameter__TypeAssignment_011448 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleEString_in_rule__Parameter__NameAssignment_111483 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__Accessor__SlotAssignment_111518 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__Accessor__JavaAssignment_311553 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__FeatureException__FeatureAssignment_111588 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__FeatureParameter__FeatureAssignment_011627 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_rule__SettedFeatureParameter__FeatureAssignment_011666 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSettedValue_in_rule__SettedFeatureParameter__ValueAssignment_211701 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_rule__JavaCodeValue__JavaAssignment11732 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleFQN_in_rule__LiteralValue__LiteralAssignment11767 = new BitSet(new long[]{0x0000000000000002L});

}
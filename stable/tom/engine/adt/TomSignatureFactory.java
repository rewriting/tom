package jtom.adt;

import aterm.*;
import aterm.pure.PureFactory;
public class TomSignatureFactory extends PureFactory
{
  private aterm.AFun funDeclaration_TypeTermDecl;
  private Declaration protoDeclaration_TypeTermDecl;
  private aterm.ATerm patternDeclaration_TypeTermDecl;
  private aterm.AFun funDeclaration_TypeListDecl;
  private Declaration protoDeclaration_TypeListDecl;
  private aterm.ATerm patternDeclaration_TypeListDecl;
  private aterm.AFun funDeclaration_TypeArrayDecl;
  private Declaration protoDeclaration_TypeArrayDecl;
  private aterm.ATerm patternDeclaration_TypeArrayDecl;
  private aterm.AFun funDeclaration_GetFunctionSymbolDecl;
  private Declaration protoDeclaration_GetFunctionSymbolDecl;
  private aterm.ATerm patternDeclaration_GetFunctionSymbolDecl;
  private aterm.AFun funDeclaration_GetSubtermDecl;
  private Declaration protoDeclaration_GetSubtermDecl;
  private aterm.ATerm patternDeclaration_GetSubtermDecl;
  private aterm.AFun funDeclaration_IsFsymDecl;
  private Declaration protoDeclaration_IsFsymDecl;
  private aterm.ATerm patternDeclaration_IsFsymDecl;
  private aterm.AFun funDeclaration_GetSlotDecl;
  private Declaration protoDeclaration_GetSlotDecl;
  private aterm.ATerm patternDeclaration_GetSlotDecl;
  private aterm.AFun funDeclaration_CompareFunctionSymbolDecl;
  private Declaration protoDeclaration_CompareFunctionSymbolDecl;
  private aterm.ATerm patternDeclaration_CompareFunctionSymbolDecl;
  private aterm.AFun funDeclaration_TermsEqualDecl;
  private Declaration protoDeclaration_TermsEqualDecl;
  private aterm.ATerm patternDeclaration_TermsEqualDecl;
  private aterm.AFun funDeclaration_GetHeadDecl;
  private Declaration protoDeclaration_GetHeadDecl;
  private aterm.ATerm patternDeclaration_GetHeadDecl;
  private aterm.AFun funDeclaration_GetTailDecl;
  private Declaration protoDeclaration_GetTailDecl;
  private aterm.ATerm patternDeclaration_GetTailDecl;
  private aterm.AFun funDeclaration_IsEmptyDecl;
  private Declaration protoDeclaration_IsEmptyDecl;
  private aterm.ATerm patternDeclaration_IsEmptyDecl;
  private aterm.AFun funDeclaration_MakeEmptyList;
  private Declaration protoDeclaration_MakeEmptyList;
  private aterm.ATerm patternDeclaration_MakeEmptyList;
  private aterm.AFun funDeclaration_MakeAddList;
  private Declaration protoDeclaration_MakeAddList;
  private aterm.ATerm patternDeclaration_MakeAddList;
  private aterm.AFun funDeclaration_GetElementDecl;
  private Declaration protoDeclaration_GetElementDecl;
  private aterm.ATerm patternDeclaration_GetElementDecl;
  private aterm.AFun funDeclaration_GetSizeDecl;
  private Declaration protoDeclaration_GetSizeDecl;
  private aterm.ATerm patternDeclaration_GetSizeDecl;
  private aterm.AFun funDeclaration_MakeEmptyArray;
  private Declaration protoDeclaration_MakeEmptyArray;
  private aterm.ATerm patternDeclaration_MakeEmptyArray;
  private aterm.AFun funDeclaration_MakeAddArray;
  private Declaration protoDeclaration_MakeAddArray;
  private aterm.ATerm patternDeclaration_MakeAddArray;
  private aterm.AFun funDeclaration_MakeDecl;
  private Declaration protoDeclaration_MakeDecl;
  private aterm.ATerm patternDeclaration_MakeDecl;
  private aterm.AFun funDeclaration_SymbolDecl;
  private Declaration protoDeclaration_SymbolDecl;
  private aterm.ATerm patternDeclaration_SymbolDecl;
  private aterm.AFun funDeclaration_ListSymbolDecl;
  private Declaration protoDeclaration_ListSymbolDecl;
  private aterm.ATerm patternDeclaration_ListSymbolDecl;
  private aterm.AFun funDeclaration_ArraySymbolDecl;
  private Declaration protoDeclaration_ArraySymbolDecl;
  private aterm.ATerm patternDeclaration_ArraySymbolDecl;
  private aterm.AFun funDeclaration_EmptyDeclaration;
  private Declaration protoDeclaration_EmptyDeclaration;
  private aterm.ATerm patternDeclaration_EmptyDeclaration;
  private aterm.AFun funOption_DeclarationToOption;
  private Option protoOption_DeclarationToOption;
  private aterm.ATerm patternOption_DeclarationToOption;
  private aterm.AFun funOption_TomNameToOption;
  private Option protoOption_TomNameToOption;
  private aterm.ATerm patternOption_TomNameToOption;
  private aterm.AFun funOption_TomTermToOption;
  private Option protoOption_TomTermToOption;
  private aterm.ATerm patternOption_TomTermToOption;
  private aterm.AFun funOption_Option;
  private Option protoOption_Option;
  private aterm.ATerm patternOption_Option;
  private aterm.AFun funOption_DefinedSymbol;
  private Option protoOption_DefinedSymbol;
  private aterm.ATerm patternOption_DefinedSymbol;
  private aterm.AFun funOption_GeneratedMatch;
  private Option protoOption_GeneratedMatch;
  private aterm.ATerm patternOption_GeneratedMatch;
  private aterm.AFun funOption_WithDefaultProduction;
  private Option protoOption_WithDefaultProduction;
  private aterm.ATerm patternOption_WithDefaultProduction;
  private aterm.AFun funOption_OriginTracking;
  private Option protoOption_OriginTracking;
  private aterm.ATerm patternOption_OriginTracking;
  private aterm.AFun funOption_Constructor;
  private Option protoOption_Constructor;
  private aterm.ATerm patternOption_Constructor;
  private aterm.AFun funOption_OriginalText;
  private Option protoOption_OriginalText;
  private aterm.ATerm patternOption_OriginalText;
  private aterm.AFun funOption_XMLPosition;
  private Option protoOption_XMLPosition;
  private aterm.ATerm patternOption_XMLPosition;
  private aterm.AFun funExpression_TomTermToExpression;
  private Expression protoExpression_TomTermToExpression;
  private aterm.ATerm patternExpression_TomTermToExpression;
  private aterm.AFun funExpression_Not;
  private Expression protoExpression_Not;
  private aterm.ATerm patternExpression_Not;
  private aterm.AFun funExpression_And;
  private Expression protoExpression_And;
  private aterm.ATerm patternExpression_And;
  private aterm.AFun funExpression_TrueTL;
  private Expression protoExpression_TrueTL;
  private aterm.ATerm patternExpression_TrueTL;
  private aterm.AFun funExpression_FalseTL;
  private Expression protoExpression_FalseTL;
  private aterm.ATerm patternExpression_FalseTL;
  private aterm.AFun funExpression_IsEmptyList;
  private Expression protoExpression_IsEmptyList;
  private aterm.ATerm patternExpression_IsEmptyList;
  private aterm.AFun funExpression_IsEmptyArray;
  private Expression protoExpression_IsEmptyArray;
  private aterm.ATerm patternExpression_IsEmptyArray;
  private aterm.AFun funExpression_EqualFunctionSymbol;
  private Expression protoExpression_EqualFunctionSymbol;
  private aterm.ATerm patternExpression_EqualFunctionSymbol;
  private aterm.AFun funExpression_EqualTerm;
  private Expression protoExpression_EqualTerm;
  private aterm.ATerm patternExpression_EqualTerm;
  private aterm.AFun funExpression_GetSubterm;
  private Expression protoExpression_GetSubterm;
  private aterm.ATerm patternExpression_GetSubterm;
  private aterm.AFun funExpression_IsFsym;
  private Expression protoExpression_IsFsym;
  private aterm.ATerm patternExpression_IsFsym;
  private aterm.AFun funExpression_GetSlot;
  private Expression protoExpression_GetSlot;
  private aterm.ATerm patternExpression_GetSlot;
  private aterm.AFun funExpression_GetHead;
  private Expression protoExpression_GetHead;
  private aterm.ATerm patternExpression_GetHead;
  private aterm.AFun funExpression_GetTail;
  private Expression protoExpression_GetTail;
  private aterm.ATerm patternExpression_GetTail;
  private aterm.AFun funExpression_GetSize;
  private Expression protoExpression_GetSize;
  private aterm.ATerm patternExpression_GetSize;
  private aterm.AFun funExpression_GetElement;
  private Expression protoExpression_GetElement;
  private aterm.ATerm patternExpression_GetElement;
  private aterm.AFun funExpression_GetSliceList;
  private Expression protoExpression_GetSliceList;
  private aterm.ATerm patternExpression_GetSliceList;
  private aterm.AFun funExpression_GetSliceArray;
  private Expression protoExpression_GetSliceArray;
  private aterm.ATerm patternExpression_GetSliceArray;
  private aterm.AFun funTargetLanguage_TL;
  private TargetLanguage protoTargetLanguage_TL;
  private aterm.ATerm patternTargetLanguage_TL;
  private aterm.AFun funTargetLanguage_ITL;
  private TargetLanguage protoTargetLanguage_ITL;
  private aterm.ATerm patternTargetLanguage_ITL;
  private aterm.AFun funPosition_Position;
  private Position protoPosition_Position;
  private aterm.ATerm patternPosition_Position;
  private aterm.AFun funTomType_Type;
  private TomType protoTomType_Type;
  private aterm.ATerm patternTomType_Type;
  private aterm.AFun funTomType_TypesToType;
  private TomType protoTomType_TypesToType;
  private aterm.ATerm patternTomType_TypesToType;
  private aterm.AFun funTomType_TomType;
  private TomType protoTomType_TomType;
  private aterm.ATerm patternTomType_TomType;
  private aterm.AFun funTomType_TomTypeAlone;
  private TomType protoTomType_TomTypeAlone;
  private aterm.ATerm patternTomType_TomTypeAlone;
  private aterm.AFun funTomType_TLType;
  private TomType protoTomType_TLType;
  private aterm.ATerm patternTomType_TLType;
  private aterm.AFun funTomType_EmptyType;
  private TomType protoTomType_EmptyType;
  private aterm.ATerm patternTomType_EmptyType;
  private aterm.AFun funTomName_Name;
  private TomName protoTomName_Name;
  private aterm.ATerm patternTomName_Name;
  private aterm.AFun funTomName_PositionName;
  private TomName protoTomName_PositionName;
  private aterm.ATerm patternTomName_PositionName;
  private aterm.AFun funTomName_EmptyName;
  private TomName protoTomName_EmptyName;
  private aterm.ATerm patternTomName_EmptyName;
  private aterm.AFun funTomTerm_TargetLanguageToTomTerm;
  private TomTerm protoTomTerm_TargetLanguageToTomTerm;
  private aterm.ATerm patternTomTerm_TargetLanguageToTomTerm;
  private aterm.AFun funTomTerm_TomTypeToTomTerm;
  private TomTerm protoTomTerm_TomTypeToTomTerm;
  private aterm.ATerm patternTomTerm_TomTypeToTomTerm;
  private aterm.AFun funTomTerm_TomNameToTomTerm;
  private TomTerm protoTomTerm_TomNameToTomTerm;
  private aterm.ATerm patternTomTerm_TomNameToTomTerm;
  private aterm.AFun funTomTerm_TomSymbolToTomTerm;
  private TomTerm protoTomTerm_TomSymbolToTomTerm;
  private aterm.ATerm patternTomTerm_TomSymbolToTomTerm;
  private aterm.AFun funTomTerm_DeclarationToTomTerm;
  private TomTerm protoTomTerm_DeclarationToTomTerm;
  private aterm.ATerm patternTomTerm_DeclarationToTomTerm;
  private aterm.AFun funTomTerm_OptionToTomTerm;
  private TomTerm protoTomTerm_OptionToTomTerm;
  private aterm.ATerm patternTomTerm_OptionToTomTerm;
  private aterm.AFun funTomTerm_ExpressionToTomTerm;
  private TomTerm protoTomTerm_ExpressionToTomTerm;
  private aterm.ATerm patternTomTerm_ExpressionToTomTerm;
  private aterm.AFun funTomTerm_InstructionToTomTerm;
  private TomTerm protoTomTerm_InstructionToTomTerm;
  private aterm.ATerm patternTomTerm_InstructionToTomTerm;
  private aterm.AFun funTomTerm_TomRuleToTomTerm;
  private TomTerm protoTomTerm_TomRuleToTomTerm;
  private aterm.ATerm patternTomTerm_TomRuleToTomTerm;
  private aterm.AFun funTomTerm_Tom;
  private TomTerm protoTomTerm_Tom;
  private aterm.ATerm patternTomTerm_Tom;
  private aterm.AFun funTomTerm_TomInclude;
  private TomTerm protoTomTerm_TomInclude;
  private aterm.ATerm patternTomTerm_TomInclude;
  private aterm.AFun funTomTerm_MakeTerm;
  private TomTerm protoTomTerm_MakeTerm;
  private aterm.ATerm patternTomTerm_MakeTerm;
  private aterm.AFun funTomTerm_BackQuoteTerm;
  private TomTerm protoTomTerm_BackQuoteTerm;
  private aterm.ATerm patternTomTerm_BackQuoteTerm;
  private aterm.AFun funTomTerm_FunctionCall;
  private TomTerm protoTomTerm_FunctionCall;
  private aterm.ATerm patternTomTerm_FunctionCall;
  private aterm.AFun funTomTerm_MakeFunctionBegin;
  private TomTerm protoTomTerm_MakeFunctionBegin;
  private aterm.ATerm patternTomTerm_MakeFunctionBegin;
  private aterm.AFun funTomTerm_MakeFunctionEnd;
  private TomTerm protoTomTerm_MakeFunctionEnd;
  private aterm.ATerm patternTomTerm_MakeFunctionEnd;
  private aterm.AFun funTomTerm_Appl;
  private TomTerm protoTomTerm_Appl;
  private aterm.ATerm patternTomTerm_Appl;
  private aterm.AFun funTomTerm_RecordAppl;
  private TomTerm protoTomTerm_RecordAppl;
  private aterm.ATerm patternTomTerm_RecordAppl;
  private aterm.AFun funTomTerm_PairSlotAppl;
  private TomTerm protoTomTerm_PairSlotAppl;
  private aterm.ATerm patternTomTerm_PairSlotAppl;
  private aterm.AFun funTomTerm_Match;
  private TomTerm protoTomTerm_Match;
  private aterm.ATerm patternTomTerm_Match;
  private aterm.AFun funTomTerm_MatchingCondition;
  private TomTerm protoTomTerm_MatchingCondition;
  private aterm.ATerm patternTomTerm_MatchingCondition;
  private aterm.AFun funTomTerm_EqualityCondition;
  private TomTerm protoTomTerm_EqualityCondition;
  private aterm.ATerm patternTomTerm_EqualityCondition;
  private aterm.AFun funTomTerm_RuleSet;
  private TomTerm protoTomTerm_RuleSet;
  private aterm.ATerm patternTomTerm_RuleSet;
  private aterm.AFun funTomTerm_SubjectList;
  private TomTerm protoTomTerm_SubjectList;
  private aterm.ATerm patternTomTerm_SubjectList;
  private aterm.AFun funTomTerm_PatternList;
  private TomTerm protoTomTerm_PatternList;
  private aterm.ATerm patternTomTerm_PatternList;
  private aterm.AFun funTomTerm_TermList;
  private TomTerm protoTomTerm_TermList;
  private aterm.ATerm patternTomTerm_TermList;
  private aterm.AFun funTomTerm_Term;
  private TomTerm protoTomTerm_Term;
  private aterm.ATerm patternTomTerm_Term;
  private aterm.AFun funTomTerm_PatternAction;
  private TomTerm protoTomTerm_PatternAction;
  private aterm.ATerm patternTomTerm_PatternAction;
  private aterm.AFun funTomTerm_DefaultPatternAction;
  private TomTerm protoTomTerm_DefaultPatternAction;
  private aterm.ATerm patternTomTerm_DefaultPatternAction;
  private aterm.AFun funTomTerm_TLVar;
  private TomTerm protoTomTerm_TLVar;
  private aterm.ATerm patternTomTerm_TLVar;
  private aterm.AFun funTomTerm_Declaration;
  private TomTerm protoTomTerm_Declaration;
  private aterm.ATerm patternTomTerm_Declaration;
  private aterm.AFun funTomTerm_Variable;
  private TomTerm protoTomTerm_Variable;
  private aterm.ATerm patternTomTerm_Variable;
  private aterm.AFun funTomTerm_VariableStar;
  private TomTerm protoTomTerm_VariableStar;
  private aterm.ATerm patternTomTerm_VariableStar;
  private aterm.AFun funTomTerm_Placeholder;
  private TomTerm protoTomTerm_Placeholder;
  private aterm.ATerm patternTomTerm_Placeholder;
  private aterm.AFun funTomTerm_UnamedVariable;
  private TomTerm protoTomTerm_UnamedVariable;
  private aterm.ATerm patternTomTerm_UnamedVariable;
  private aterm.AFun funTomTerm_DotTerm;
  private TomTerm protoTomTerm_DotTerm;
  private aterm.ATerm patternTomTerm_DotTerm;
  private aterm.AFun funTomTerm_LocalVariable;
  private TomTerm protoTomTerm_LocalVariable;
  private aterm.ATerm patternTomTerm_LocalVariable;
  private aterm.AFun funTomTerm_EndLocalVariable;
  private TomTerm protoTomTerm_EndLocalVariable;
  private aterm.ATerm patternTomTerm_EndLocalVariable;
  private aterm.AFun funTomTerm_BuildVariable;
  private TomTerm protoTomTerm_BuildVariable;
  private aterm.ATerm patternTomTerm_BuildVariable;
  private aterm.AFun funTomTerm_BuildTerm;
  private TomTerm protoTomTerm_BuildTerm;
  private aterm.ATerm patternTomTerm_BuildTerm;
  private aterm.AFun funTomTerm_BuildList;
  private TomTerm protoTomTerm_BuildList;
  private aterm.ATerm patternTomTerm_BuildList;
  private aterm.AFun funTomTerm_BuildArray;
  private TomTerm protoTomTerm_BuildArray;
  private aterm.ATerm patternTomTerm_BuildArray;
  private aterm.AFun funTomTerm_BuildBuiltin;
  private TomTerm protoTomTerm_BuildBuiltin;
  private aterm.ATerm patternTomTerm_BuildBuiltin;
  private aterm.AFun funTomTerm_CompiledMatch;
  private TomTerm protoTomTerm_CompiledMatch;
  private aterm.ATerm patternTomTerm_CompiledMatch;
  private aterm.AFun funTomTerm_CompiledPattern;
  private TomTerm protoTomTerm_CompiledPattern;
  private aterm.ATerm patternTomTerm_CompiledPattern;
  private aterm.AFun funTomTerm_AssignedVariable;
  private TomTerm protoTomTerm_AssignedVariable;
  private aterm.ATerm patternTomTerm_AssignedVariable;
  private aterm.AFun funTomTerm_Automata;
  private TomTerm protoTomTerm_Automata;
  private aterm.ATerm patternTomTerm_Automata;
  private aterm.AFun funTomTerm_DefaultAutomata;
  private TomTerm protoTomTerm_DefaultAutomata;
  private aterm.ATerm patternTomTerm_DefaultAutomata;
  private aterm.AFun funTomTerm_MatchXML;
  private TomTerm protoTomTerm_MatchXML;
  private aterm.ATerm patternTomTerm_MatchXML;
  private aterm.AFun funTomTerm_BackQuoteXML;
  private TomTerm protoTomTerm_BackQuoteXML;
  private aterm.ATerm patternTomTerm_BackQuoteXML;
  private aterm.AFun funTomTerm_XMLTermToTomTerm;
  private TomTerm protoTomTerm_XMLTermToTomTerm;
  private aterm.ATerm patternTomTerm_XMLTermToTomTerm;
  private aterm.AFun funTomNumber_MatchNumber;
  private TomNumber protoTomNumber_MatchNumber;
  private aterm.ATerm patternTomNumber_MatchNumber;
  private aterm.AFun funTomNumber_PatternNumber;
  private TomNumber protoTomNumber_PatternNumber;
  private aterm.ATerm patternTomNumber_PatternNumber;
  private aterm.AFun funTomNumber_ListNumber;
  private TomNumber protoTomNumber_ListNumber;
  private aterm.ATerm patternTomNumber_ListNumber;
  private aterm.AFun funTomNumber_IndexNumber;
  private TomNumber protoTomNumber_IndexNumber;
  private aterm.ATerm patternTomNumber_IndexNumber;
  private aterm.AFun funTomNumber_Begin;
  private TomNumber protoTomNumber_Begin;
  private aterm.ATerm patternTomNumber_Begin;
  private aterm.AFun funTomNumber_End;
  private TomNumber protoTomNumber_End;
  private aterm.ATerm patternTomNumber_End;
  private aterm.AFun funTomNumber_Number;
  private TomNumber protoTomNumber_Number;
  private aterm.ATerm patternTomNumber_Number;
  private aterm.AFun funTomNumber_AbsVar;
  private TomNumber protoTomNumber_AbsVar;
  private aterm.ATerm patternTomNumber_AbsVar;
  private aterm.AFun funTomNumber_RenamedVar;
  private TomNumber protoTomNumber_RenamedVar;
  private aterm.ATerm patternTomNumber_RenamedVar;
  private aterm.AFun funTomNumber_RuleVar;
  private TomNumber protoTomNumber_RuleVar;
  private aterm.ATerm patternTomNumber_RuleVar;
  private aterm.AFun funTomRule_RewriteRule;
  private TomRule protoTomRule_RewriteRule;
  private aterm.ATerm patternTomRule_RewriteRule;
  private aterm.AFun funInstruction_IfThenElse;
  private Instruction protoInstruction_IfThenElse;
  private aterm.ATerm patternInstruction_IfThenElse;
  private aterm.AFun funInstruction_DoWhile;
  private Instruction protoInstruction_DoWhile;
  private aterm.ATerm patternInstruction_DoWhile;
  private aterm.AFun funInstruction_Assign;
  private Instruction protoInstruction_Assign;
  private aterm.ATerm patternInstruction_Assign;
  private aterm.AFun funInstruction_AssignMatchSubject;
  private Instruction protoInstruction_AssignMatchSubject;
  private aterm.ATerm patternInstruction_AssignMatchSubject;
  private aterm.AFun funInstruction_Increment;
  private Instruction protoInstruction_Increment;
  private aterm.ATerm patternInstruction_Increment;
  private aterm.AFun funInstruction_Action;
  private Instruction protoInstruction_Action;
  private aterm.ATerm patternInstruction_Action;
  private aterm.AFun funInstruction_ExitAction;
  private Instruction protoInstruction_ExitAction;
  private aterm.ATerm patternInstruction_ExitAction;
  private aterm.AFun funInstruction_Return;
  private Instruction protoInstruction_Return;
  private aterm.ATerm patternInstruction_Return;
  private aterm.AFun funInstruction_OpenBlock;
  private Instruction protoInstruction_OpenBlock;
  private aterm.ATerm patternInstruction_OpenBlock;
  private aterm.AFun funInstruction_CloseBlock;
  private Instruction protoInstruction_CloseBlock;
  private aterm.ATerm patternInstruction_CloseBlock;
  private aterm.AFun funInstruction_NamedBlock;
  private Instruction protoInstruction_NamedBlock;
  private aterm.ATerm patternInstruction_NamedBlock;
  private aterm.AFun funTomSymbol_Symbol;
  private TomSymbol protoTomSymbol_Symbol;
  private aterm.ATerm patternTomSymbol_Symbol;
  private aterm.AFun funPairNameDecl_Slot;
  private PairNameDecl protoPairNameDecl_Slot;
  private aterm.ATerm patternPairNameDecl_Slot;
  private aterm.AFun funTomSymbolTable_Table;
  private TomSymbolTable protoTomSymbolTable_Table;
  private aterm.ATerm patternTomSymbolTable_Table;
  private aterm.AFun funTomEntry_Entry;
  private TomEntry protoTomEntry_Entry;
  private aterm.ATerm patternTomEntry_Entry;
  private aterm.AFun funTomStructureTable_StructTable;
  private TomStructureTable protoTomStructureTable_StructTable;
  private aterm.ATerm patternTomStructureTable_StructTable;
  private aterm.AFun funXMLTerm_Element;
  private XMLTerm protoXMLTerm_Element;
  private aterm.ATerm patternXMLTerm_Element;
  private aterm.AFun funXMLTerm_Attribute;
  private XMLTerm protoXMLTerm_Attribute;
  private aterm.ATerm patternXMLTerm_Attribute;
  private aterm.AFun funXMLTerm_ReservedWord;
  private XMLTerm protoXMLTerm_ReservedWord;
  private aterm.ATerm patternXMLTerm_ReservedWord;
  private aterm.AFun funXMLTerm_XMLPlaceholder;
  private XMLTerm protoXMLTerm_XMLPlaceholder;
  private aterm.ATerm patternXMLTerm_XMLPlaceholder;
  private aterm.AFun funXMLTerm_XMLVariable;
  private XMLTerm protoXMLTerm_XMLVariable;
  private aterm.ATerm patternXMLTerm_XMLVariable;
  private TomList protoTomList;
  private TomNumberList protoTomNumberList;
  private TomRuleList protoTomRuleList;
  private TomTypeList protoTomTypeList;
  private OptionList protoOptionList;
  private SlotList protoSlotList;
  private TomEntryList protoTomEntryList;
  static protected TomList emptyTomList;
  static protected TomNumberList emptyTomNumberList;
  static protected TomRuleList emptyTomRuleList;
  static protected TomTypeList emptyTomTypeList;
  static protected OptionList emptyOptionList;
  static protected SlotList emptySlotList;
  static protected TomEntryList emptyTomEntryList;
  public TomSignatureFactory()
  {
     super();
     initialize();
  }
  public TomSignatureFactory(int logSize)
  {
     super(logSize);
     initialize();
  }
  private void initialize()
  {

    patternDeclaration_TypeTermDecl = parse("TypeTermDecl(<term>,<term>,<term>)");
    funDeclaration_TypeTermDecl = makeAFun("_Declaration_TypeTermDecl", 3, false);
    protoDeclaration_TypeTermDecl = new Declaration_TypeTermDecl(this);

    patternDeclaration_TypeListDecl = parse("TypeListDecl(<term>,<term>,<term>)");
    funDeclaration_TypeListDecl = makeAFun("_Declaration_TypeListDecl", 3, false);
    protoDeclaration_TypeListDecl = new Declaration_TypeListDecl(this);

    patternDeclaration_TypeArrayDecl = parse("TypeArrayDecl(<term>,<term>,<term>)");
    funDeclaration_TypeArrayDecl = makeAFun("_Declaration_TypeArrayDecl", 3, false);
    protoDeclaration_TypeArrayDecl = new Declaration_TypeArrayDecl(this);

    patternDeclaration_GetFunctionSymbolDecl = parse("GetFunctionSymbolDecl(<term>,<term>,<term>)");
    funDeclaration_GetFunctionSymbolDecl = makeAFun("_Declaration_GetFunctionSymbolDecl", 3, false);
    protoDeclaration_GetFunctionSymbolDecl = new Declaration_GetFunctionSymbolDecl(this);

    patternDeclaration_GetSubtermDecl = parse("GetSubtermDecl(<term>,<term>,<term>,<term>)");
    funDeclaration_GetSubtermDecl = makeAFun("_Declaration_GetSubtermDecl", 4, false);
    protoDeclaration_GetSubtermDecl = new Declaration_GetSubtermDecl(this);

    patternDeclaration_IsFsymDecl = parse("IsFsymDecl(<term>,<term>,<term>,<term>)");
    funDeclaration_IsFsymDecl = makeAFun("_Declaration_IsFsymDecl", 4, false);
    protoDeclaration_IsFsymDecl = new Declaration_IsFsymDecl(this);

    patternDeclaration_GetSlotDecl = parse("GetSlotDecl(<term>,<term>,<term>,<term>,<term>)");
    funDeclaration_GetSlotDecl = makeAFun("_Declaration_GetSlotDecl", 5, false);
    protoDeclaration_GetSlotDecl = new Declaration_GetSlotDecl(this);

    patternDeclaration_CompareFunctionSymbolDecl = parse("CompareFunctionSymbolDecl(<term>,<term>,<term>,<term>)");
    funDeclaration_CompareFunctionSymbolDecl = makeAFun("_Declaration_CompareFunctionSymbolDecl", 4, false);
    protoDeclaration_CompareFunctionSymbolDecl = new Declaration_CompareFunctionSymbolDecl(this);

    patternDeclaration_TermsEqualDecl = parse("TermsEqualDecl(<term>,<term>,<term>,<term>)");
    funDeclaration_TermsEqualDecl = makeAFun("_Declaration_TermsEqualDecl", 4, false);
    protoDeclaration_TermsEqualDecl = new Declaration_TermsEqualDecl(this);

    patternDeclaration_GetHeadDecl = parse("GetHeadDecl(<term>,<term>,<term>)");
    funDeclaration_GetHeadDecl = makeAFun("_Declaration_GetHeadDecl", 3, false);
    protoDeclaration_GetHeadDecl = new Declaration_GetHeadDecl(this);

    patternDeclaration_GetTailDecl = parse("GetTailDecl(<term>,<term>,<term>)");
    funDeclaration_GetTailDecl = makeAFun("_Declaration_GetTailDecl", 3, false);
    protoDeclaration_GetTailDecl = new Declaration_GetTailDecl(this);

    patternDeclaration_IsEmptyDecl = parse("IsEmptyDecl(<term>,<term>,<term>)");
    funDeclaration_IsEmptyDecl = makeAFun("_Declaration_IsEmptyDecl", 3, false);
    protoDeclaration_IsEmptyDecl = new Declaration_IsEmptyDecl(this);

    patternDeclaration_MakeEmptyList = parse("MakeEmptyList(<term>,<term>,<term>)");
    funDeclaration_MakeEmptyList = makeAFun("_Declaration_MakeEmptyList", 3, false);
    protoDeclaration_MakeEmptyList = new Declaration_MakeEmptyList(this);

    patternDeclaration_MakeAddList = parse("MakeAddList(<term>,<term>,<term>,<term>,<term>)");
    funDeclaration_MakeAddList = makeAFun("_Declaration_MakeAddList", 5, false);
    protoDeclaration_MakeAddList = new Declaration_MakeAddList(this);

    patternDeclaration_GetElementDecl = parse("GetElementDecl(<term>,<term>,<term>,<term>)");
    funDeclaration_GetElementDecl = makeAFun("_Declaration_GetElementDecl", 4, false);
    protoDeclaration_GetElementDecl = new Declaration_GetElementDecl(this);

    patternDeclaration_GetSizeDecl = parse("GetSizeDecl(<term>,<term>,<term>)");
    funDeclaration_GetSizeDecl = makeAFun("_Declaration_GetSizeDecl", 3, false);
    protoDeclaration_GetSizeDecl = new Declaration_GetSizeDecl(this);

    patternDeclaration_MakeEmptyArray = parse("MakeEmptyArray(<term>,<term>,<term>,<term>)");
    funDeclaration_MakeEmptyArray = makeAFun("_Declaration_MakeEmptyArray", 4, false);
    protoDeclaration_MakeEmptyArray = new Declaration_MakeEmptyArray(this);

    patternDeclaration_MakeAddArray = parse("MakeAddArray(<term>,<term>,<term>,<term>,<term>)");
    funDeclaration_MakeAddArray = makeAFun("_Declaration_MakeAddArray", 5, false);
    protoDeclaration_MakeAddArray = new Declaration_MakeAddArray(this);

    patternDeclaration_MakeDecl = parse("MakeDecl(<term>,<term>,<term>,<term>,<term>)");
    funDeclaration_MakeDecl = makeAFun("_Declaration_MakeDecl", 5, false);
    protoDeclaration_MakeDecl = new Declaration_MakeDecl(this);

    patternDeclaration_SymbolDecl = parse("SymbolDecl(<term>)");
    funDeclaration_SymbolDecl = makeAFun("_Declaration_SymbolDecl", 1, false);
    protoDeclaration_SymbolDecl = new Declaration_SymbolDecl(this);

    patternDeclaration_ListSymbolDecl = parse("ListSymbolDecl(<term>)");
    funDeclaration_ListSymbolDecl = makeAFun("_Declaration_ListSymbolDecl", 1, false);
    protoDeclaration_ListSymbolDecl = new Declaration_ListSymbolDecl(this);

    patternDeclaration_ArraySymbolDecl = parse("ArraySymbolDecl(<term>)");
    funDeclaration_ArraySymbolDecl = makeAFun("_Declaration_ArraySymbolDecl", 1, false);
    protoDeclaration_ArraySymbolDecl = new Declaration_ArraySymbolDecl(this);

    patternDeclaration_EmptyDeclaration = parse("EmptyDeclaration");
    funDeclaration_EmptyDeclaration = makeAFun("_Declaration_EmptyDeclaration", 0, false);
    protoDeclaration_EmptyDeclaration = new Declaration_EmptyDeclaration(this);


    patternOption_DeclarationToOption = parse("DeclarationToOption(<term>)");
    funOption_DeclarationToOption = makeAFun("_Option_DeclarationToOption", 1, false);
    protoOption_DeclarationToOption = new Option_DeclarationToOption(this);

    patternOption_TomNameToOption = parse("TomNameToOption(<term>)");
    funOption_TomNameToOption = makeAFun("_Option_TomNameToOption", 1, false);
    protoOption_TomNameToOption = new Option_TomNameToOption(this);

    patternOption_TomTermToOption = parse("TomTermToOption(<term>)");
    funOption_TomTermToOption = makeAFun("_Option_TomTermToOption", 1, false);
    protoOption_TomTermToOption = new Option_TomTermToOption(this);

    patternOption_Option = parse("Option(<term>)");
    funOption_Option = makeAFun("_Option_Option", 1, false);
    protoOption_Option = new Option_Option(this);

    patternOption_DefinedSymbol = parse("DefinedSymbol");
    funOption_DefinedSymbol = makeAFun("_Option_DefinedSymbol", 0, false);
    protoOption_DefinedSymbol = new Option_DefinedSymbol(this);

    patternOption_GeneratedMatch = parse("GeneratedMatch");
    funOption_GeneratedMatch = makeAFun("_Option_GeneratedMatch", 0, false);
    protoOption_GeneratedMatch = new Option_GeneratedMatch(this);

    patternOption_WithDefaultProduction = parse("WithDefaultProduction");
    funOption_WithDefaultProduction = makeAFun("_Option_WithDefaultProduction", 0, false);
    protoOption_WithDefaultProduction = new Option_WithDefaultProduction(this);

    patternOption_OriginTracking = parse("OriginTracking(<term>,<int>,<term>)");
    funOption_OriginTracking = makeAFun("_Option_OriginTracking", 3, false);
    protoOption_OriginTracking = new Option_OriginTracking(this);

    patternOption_Constructor = parse("Constructor(<term>)");
    funOption_Constructor = makeAFun("_Option_Constructor", 1, false);
    protoOption_Constructor = new Option_Constructor(this);

    patternOption_OriginalText = parse("OriginalText(<term>)");
    funOption_OriginalText = makeAFun("_Option_OriginalText", 1, false);
    protoOption_OriginalText = new Option_OriginalText(this);

    patternOption_XMLPosition = parse("XMLPosition(<str>)");
    funOption_XMLPosition = makeAFun("_Option_XMLPosition", 1, false);
    protoOption_XMLPosition = new Option_XMLPosition(this);


    patternExpression_TomTermToExpression = parse("TomTermToExpression(<term>)");
    funExpression_TomTermToExpression = makeAFun("_Expression_TomTermToExpression", 1, false);
    protoExpression_TomTermToExpression = new Expression_TomTermToExpression(this);

    patternExpression_Not = parse("Not(<term>)");
    funExpression_Not = makeAFun("_Expression_Not", 1, false);
    protoExpression_Not = new Expression_Not(this);

    patternExpression_And = parse("And(<term>,<term>)");
    funExpression_And = makeAFun("_Expression_And", 2, false);
    protoExpression_And = new Expression_And(this);

    patternExpression_TrueTL = parse("TrueTL");
    funExpression_TrueTL = makeAFun("_Expression_TrueTL", 0, false);
    protoExpression_TrueTL = new Expression_TrueTL(this);

    patternExpression_FalseTL = parse("FalseTL");
    funExpression_FalseTL = makeAFun("_Expression_FalseTL", 0, false);
    protoExpression_FalseTL = new Expression_FalseTL(this);

    patternExpression_IsEmptyList = parse("IsEmptyList(<term>)");
    funExpression_IsEmptyList = makeAFun("_Expression_IsEmptyList", 1, false);
    protoExpression_IsEmptyList = new Expression_IsEmptyList(this);

    patternExpression_IsEmptyArray = parse("IsEmptyArray(<term>,<term>)");
    funExpression_IsEmptyArray = makeAFun("_Expression_IsEmptyArray", 2, false);
    protoExpression_IsEmptyArray = new Expression_IsEmptyArray(this);

    patternExpression_EqualFunctionSymbol = parse("EqualFunctionSymbol(<term>,<term>)");
    funExpression_EqualFunctionSymbol = makeAFun("_Expression_EqualFunctionSymbol", 2, false);
    protoExpression_EqualFunctionSymbol = new Expression_EqualFunctionSymbol(this);

    patternExpression_EqualTerm = parse("EqualTerm(<term>,<term>)");
    funExpression_EqualTerm = makeAFun("_Expression_EqualTerm", 2, false);
    protoExpression_EqualTerm = new Expression_EqualTerm(this);

    patternExpression_GetSubterm = parse("GetSubterm(<term>,<term>)");
    funExpression_GetSubterm = makeAFun("_Expression_GetSubterm", 2, false);
    protoExpression_GetSubterm = new Expression_GetSubterm(this);

    patternExpression_IsFsym = parse("IsFsym(<term>,<term>)");
    funExpression_IsFsym = makeAFun("_Expression_IsFsym", 2, false);
    protoExpression_IsFsym = new Expression_IsFsym(this);

    patternExpression_GetSlot = parse("GetSlot(<term>,<str>,<term>)");
    funExpression_GetSlot = makeAFun("_Expression_GetSlot", 3, false);
    protoExpression_GetSlot = new Expression_GetSlot(this);

    patternExpression_GetHead = parse("GetHead(<term>)");
    funExpression_GetHead = makeAFun("_Expression_GetHead", 1, false);
    protoExpression_GetHead = new Expression_GetHead(this);

    patternExpression_GetTail = parse("GetTail(<term>)");
    funExpression_GetTail = makeAFun("_Expression_GetTail", 1, false);
    protoExpression_GetTail = new Expression_GetTail(this);

    patternExpression_GetSize = parse("GetSize(<term>)");
    funExpression_GetSize = makeAFun("_Expression_GetSize", 1, false);
    protoExpression_GetSize = new Expression_GetSize(this);

    patternExpression_GetElement = parse("GetElement(<term>,<term>)");
    funExpression_GetElement = makeAFun("_Expression_GetElement", 2, false);
    protoExpression_GetElement = new Expression_GetElement(this);

    patternExpression_GetSliceList = parse("GetSliceList(<term>,<term>,<term>)");
    funExpression_GetSliceList = makeAFun("_Expression_GetSliceList", 3, false);
    protoExpression_GetSliceList = new Expression_GetSliceList(this);

    patternExpression_GetSliceArray = parse("GetSliceArray(<term>,<term>,<term>,<term>)");
    funExpression_GetSliceArray = makeAFun("_Expression_GetSliceArray", 4, false);
    protoExpression_GetSliceArray = new Expression_GetSliceArray(this);


    patternTargetLanguage_TL = parse("TL(<str>,<term>,<term>)");
    funTargetLanguage_TL = makeAFun("_TargetLanguage_TL", 3, false);
    protoTargetLanguage_TL = new TargetLanguage_TL(this);

    patternTargetLanguage_ITL = parse("ITL(<str>)");
    funTargetLanguage_ITL = makeAFun("_TargetLanguage_ITL", 1, false);
    protoTargetLanguage_ITL = new TargetLanguage_ITL(this);


    patternPosition_Position = parse("Position(<int>,<int>)");
    funPosition_Position = makeAFun("_Position_Position", 2, false);
    protoPosition_Position = new Position_Position(this);


    patternTomType_Type = parse("Type(<term>,<term>)");
    funTomType_Type = makeAFun("_TomType_Type", 2, false);
    protoTomType_Type = new TomType_Type(this);

    patternTomType_TypesToType = parse("TypesToType(<term>,<term>)");
    funTomType_TypesToType = makeAFun("_TomType_TypesToType", 2, false);
    protoTomType_TypesToType = new TomType_TypesToType(this);

    patternTomType_TomType = parse("TomType(<str>)");
    funTomType_TomType = makeAFun("_TomType_TomType", 1, false);
    protoTomType_TomType = new TomType_TomType(this);

    patternTomType_TomTypeAlone = parse("TomTypeAlone(<str>)");
    funTomType_TomTypeAlone = makeAFun("_TomType_TomTypeAlone", 1, false);
    protoTomType_TomTypeAlone = new TomType_TomTypeAlone(this);

    patternTomType_TLType = parse("TLType(<term>)");
    funTomType_TLType = makeAFun("_TomType_TLType", 1, false);
    protoTomType_TLType = new TomType_TLType(this);

    patternTomType_EmptyType = parse("EmptyType");
    funTomType_EmptyType = makeAFun("_TomType_EmptyType", 0, false);
    protoTomType_EmptyType = new TomType_EmptyType(this);


    patternTomName_Name = parse("Name(<str>)");
    funTomName_Name = makeAFun("_TomName_Name", 1, false);
    protoTomName_Name = new TomName_Name(this);

    patternTomName_PositionName = parse("PositionName(<term>)");
    funTomName_PositionName = makeAFun("_TomName_PositionName", 1, false);
    protoTomName_PositionName = new TomName_PositionName(this);

    patternTomName_EmptyName = parse("EmptyName");
    funTomName_EmptyName = makeAFun("_TomName_EmptyName", 0, false);
    protoTomName_EmptyName = new TomName_EmptyName(this);


    patternTomTerm_TargetLanguageToTomTerm = parse("TargetLanguageToTomTerm(<term>)");
    funTomTerm_TargetLanguageToTomTerm = makeAFun("_TomTerm_TargetLanguageToTomTerm", 1, false);
    protoTomTerm_TargetLanguageToTomTerm = new TomTerm_TargetLanguageToTomTerm(this);

    patternTomTerm_TomTypeToTomTerm = parse("TomTypeToTomTerm(<term>)");
    funTomTerm_TomTypeToTomTerm = makeAFun("_TomTerm_TomTypeToTomTerm", 1, false);
    protoTomTerm_TomTypeToTomTerm = new TomTerm_TomTypeToTomTerm(this);

    patternTomTerm_TomNameToTomTerm = parse("TomNameToTomTerm(<term>)");
    funTomTerm_TomNameToTomTerm = makeAFun("_TomTerm_TomNameToTomTerm", 1, false);
    protoTomTerm_TomNameToTomTerm = new TomTerm_TomNameToTomTerm(this);

    patternTomTerm_TomSymbolToTomTerm = parse("TomSymbolToTomTerm(<term>)");
    funTomTerm_TomSymbolToTomTerm = makeAFun("_TomTerm_TomSymbolToTomTerm", 1, false);
    protoTomTerm_TomSymbolToTomTerm = new TomTerm_TomSymbolToTomTerm(this);

    patternTomTerm_DeclarationToTomTerm = parse("DeclarationToTomTerm(<term>)");
    funTomTerm_DeclarationToTomTerm = makeAFun("_TomTerm_DeclarationToTomTerm", 1, false);
    protoTomTerm_DeclarationToTomTerm = new TomTerm_DeclarationToTomTerm(this);

    patternTomTerm_OptionToTomTerm = parse("OptionToTomTerm(<term>)");
    funTomTerm_OptionToTomTerm = makeAFun("_TomTerm_OptionToTomTerm", 1, false);
    protoTomTerm_OptionToTomTerm = new TomTerm_OptionToTomTerm(this);

    patternTomTerm_ExpressionToTomTerm = parse("ExpressionToTomTerm(<term>)");
    funTomTerm_ExpressionToTomTerm = makeAFun("_TomTerm_ExpressionToTomTerm", 1, false);
    protoTomTerm_ExpressionToTomTerm = new TomTerm_ExpressionToTomTerm(this);

    patternTomTerm_InstructionToTomTerm = parse("InstructionToTomTerm(<term>)");
    funTomTerm_InstructionToTomTerm = makeAFun("_TomTerm_InstructionToTomTerm", 1, false);
    protoTomTerm_InstructionToTomTerm = new TomTerm_InstructionToTomTerm(this);

    patternTomTerm_TomRuleToTomTerm = parse("TomRuleToTomTerm(<term>)");
    funTomTerm_TomRuleToTomTerm = makeAFun("_TomTerm_TomRuleToTomTerm", 1, false);
    protoTomTerm_TomRuleToTomTerm = new TomTerm_TomRuleToTomTerm(this);

    patternTomTerm_Tom = parse("Tom(<term>)");
    funTomTerm_Tom = makeAFun("_TomTerm_Tom", 1, false);
    protoTomTerm_Tom = new TomTerm_Tom(this);

    patternTomTerm_TomInclude = parse("TomInclude(<term>)");
    funTomTerm_TomInclude = makeAFun("_TomTerm_TomInclude", 1, false);
    protoTomTerm_TomInclude = new TomTerm_TomInclude(this);

    patternTomTerm_MakeTerm = parse("MakeTerm(<term>)");
    funTomTerm_MakeTerm = makeAFun("_TomTerm_MakeTerm", 1, false);
    protoTomTerm_MakeTerm = new TomTerm_MakeTerm(this);

    patternTomTerm_BackQuoteTerm = parse("BackQuoteTerm(<term>,<term>)");
    funTomTerm_BackQuoteTerm = makeAFun("_TomTerm_BackQuoteTerm", 2, false);
    protoTomTerm_BackQuoteTerm = new TomTerm_BackQuoteTerm(this);

    patternTomTerm_FunctionCall = parse("FunctionCall(<term>,<term>)");
    funTomTerm_FunctionCall = makeAFun("_TomTerm_FunctionCall", 2, false);
    protoTomTerm_FunctionCall = new TomTerm_FunctionCall(this);

    patternTomTerm_MakeFunctionBegin = parse("MakeFunctionBegin(<term>,<term>)");
    funTomTerm_MakeFunctionBegin = makeAFun("_TomTerm_MakeFunctionBegin", 2, false);
    protoTomTerm_MakeFunctionBegin = new TomTerm_MakeFunctionBegin(this);

    patternTomTerm_MakeFunctionEnd = parse("MakeFunctionEnd");
    funTomTerm_MakeFunctionEnd = makeAFun("_TomTerm_MakeFunctionEnd", 0, false);
    protoTomTerm_MakeFunctionEnd = new TomTerm_MakeFunctionEnd(this);

    patternTomTerm_Appl = parse("Appl(<term>,<term>,<term>)");
    funTomTerm_Appl = makeAFun("_TomTerm_Appl", 3, false);
    protoTomTerm_Appl = new TomTerm_Appl(this);

    patternTomTerm_RecordAppl = parse("RecordAppl(<term>,<term>,<term>)");
    funTomTerm_RecordAppl = makeAFun("_TomTerm_RecordAppl", 3, false);
    protoTomTerm_RecordAppl = new TomTerm_RecordAppl(this);

    patternTomTerm_PairSlotAppl = parse("PairSlotAppl(<term>,<term>)");
    funTomTerm_PairSlotAppl = makeAFun("_TomTerm_PairSlotAppl", 2, false);
    protoTomTerm_PairSlotAppl = new TomTerm_PairSlotAppl(this);

    patternTomTerm_Match = parse("Match(<term>,<term>,<term>)");
    funTomTerm_Match = makeAFun("_TomTerm_Match", 3, false);
    protoTomTerm_Match = new TomTerm_Match(this);

    patternTomTerm_MatchingCondition = parse("MatchingCondition(<term>,<term>)");
    funTomTerm_MatchingCondition = makeAFun("_TomTerm_MatchingCondition", 2, false);
    protoTomTerm_MatchingCondition = new TomTerm_MatchingCondition(this);

    patternTomTerm_EqualityCondition = parse("EqualityCondition(<term>,<term>)");
    funTomTerm_EqualityCondition = makeAFun("_TomTerm_EqualityCondition", 2, false);
    protoTomTerm_EqualityCondition = new TomTerm_EqualityCondition(this);

    patternTomTerm_RuleSet = parse("RuleSet(<term>,<term>)");
    funTomTerm_RuleSet = makeAFun("_TomTerm_RuleSet", 2, false);
    protoTomTerm_RuleSet = new TomTerm_RuleSet(this);

    patternTomTerm_SubjectList = parse("SubjectList(<term>)");
    funTomTerm_SubjectList = makeAFun("_TomTerm_SubjectList", 1, false);
    protoTomTerm_SubjectList = new TomTerm_SubjectList(this);

    patternTomTerm_PatternList = parse("PatternList(<term>)");
    funTomTerm_PatternList = makeAFun("_TomTerm_PatternList", 1, false);
    protoTomTerm_PatternList = new TomTerm_PatternList(this);

    patternTomTerm_TermList = parse("TermList(<term>)");
    funTomTerm_TermList = makeAFun("_TomTerm_TermList", 1, false);
    protoTomTerm_TermList = new TomTerm_TermList(this);

    patternTomTerm_Term = parse("Term(<term>)");
    funTomTerm_Term = makeAFun("_TomTerm_Term", 1, false);
    protoTomTerm_Term = new TomTerm_Term(this);

    patternTomTerm_PatternAction = parse("PatternAction(<term>,<term>,<term>)");
    funTomTerm_PatternAction = makeAFun("_TomTerm_PatternAction", 3, false);
    protoTomTerm_PatternAction = new TomTerm_PatternAction(this);

    patternTomTerm_DefaultPatternAction = parse("DefaultPatternAction(<term>,<term>,<term>)");
    funTomTerm_DefaultPatternAction = makeAFun("_TomTerm_DefaultPatternAction", 3, false);
    protoTomTerm_DefaultPatternAction = new TomTerm_DefaultPatternAction(this);

    patternTomTerm_TLVar = parse("TLVar(<str>,<term>)");
    funTomTerm_TLVar = makeAFun("_TomTerm_TLVar", 2, false);
    protoTomTerm_TLVar = new TomTerm_TLVar(this);

    patternTomTerm_Declaration = parse("Declaration(<term>)");
    funTomTerm_Declaration = makeAFun("_TomTerm_Declaration", 1, false);
    protoTomTerm_Declaration = new TomTerm_Declaration(this);

    patternTomTerm_Variable = parse("Variable(<term>,<term>,<term>)");
    funTomTerm_Variable = makeAFun("_TomTerm_Variable", 3, false);
    protoTomTerm_Variable = new TomTerm_Variable(this);

    patternTomTerm_VariableStar = parse("VariableStar(<term>,<term>,<term>)");
    funTomTerm_VariableStar = makeAFun("_TomTerm_VariableStar", 3, false);
    protoTomTerm_VariableStar = new TomTerm_VariableStar(this);

    patternTomTerm_Placeholder = parse("Placeholder(<term>)");
    funTomTerm_Placeholder = makeAFun("_TomTerm_Placeholder", 1, false);
    protoTomTerm_Placeholder = new TomTerm_Placeholder(this);

    patternTomTerm_UnamedVariable = parse("UnamedVariable(<term>,<term>)");
    funTomTerm_UnamedVariable = makeAFun("_TomTerm_UnamedVariable", 2, false);
    protoTomTerm_UnamedVariable = new TomTerm_UnamedVariable(this);

    patternTomTerm_DotTerm = parse("DotTerm(<term>,<term>)");
    funTomTerm_DotTerm = makeAFun("_TomTerm_DotTerm", 2, false);
    protoTomTerm_DotTerm = new TomTerm_DotTerm(this);

    patternTomTerm_LocalVariable = parse("LocalVariable");
    funTomTerm_LocalVariable = makeAFun("_TomTerm_LocalVariable", 0, false);
    protoTomTerm_LocalVariable = new TomTerm_LocalVariable(this);

    patternTomTerm_EndLocalVariable = parse("EndLocalVariable");
    funTomTerm_EndLocalVariable = makeAFun("_TomTerm_EndLocalVariable", 0, false);
    protoTomTerm_EndLocalVariable = new TomTerm_EndLocalVariable(this);

    patternTomTerm_BuildVariable = parse("BuildVariable(<term>)");
    funTomTerm_BuildVariable = makeAFun("_TomTerm_BuildVariable", 1, false);
    protoTomTerm_BuildVariable = new TomTerm_BuildVariable(this);

    patternTomTerm_BuildTerm = parse("BuildTerm(<term>,<term>)");
    funTomTerm_BuildTerm = makeAFun("_TomTerm_BuildTerm", 2, false);
    protoTomTerm_BuildTerm = new TomTerm_BuildTerm(this);

    patternTomTerm_BuildList = parse("BuildList(<term>,<term>)");
    funTomTerm_BuildList = makeAFun("_TomTerm_BuildList", 2, false);
    protoTomTerm_BuildList = new TomTerm_BuildList(this);

    patternTomTerm_BuildArray = parse("BuildArray(<term>,<term>)");
    funTomTerm_BuildArray = makeAFun("_TomTerm_BuildArray", 2, false);
    protoTomTerm_BuildArray = new TomTerm_BuildArray(this);

    patternTomTerm_BuildBuiltin = parse("BuildBuiltin(<term>)");
    funTomTerm_BuildBuiltin = makeAFun("_TomTerm_BuildBuiltin", 1, false);
    protoTomTerm_BuildBuiltin = new TomTerm_BuildBuiltin(this);

    patternTomTerm_CompiledMatch = parse("CompiledMatch(<term>,<term>,<term>)");
    funTomTerm_CompiledMatch = makeAFun("_TomTerm_CompiledMatch", 3, false);
    protoTomTerm_CompiledMatch = new TomTerm_CompiledMatch(this);

    patternTomTerm_CompiledPattern = parse("CompiledPattern(<term>)");
    funTomTerm_CompiledPattern = makeAFun("_TomTerm_CompiledPattern", 1, false);
    protoTomTerm_CompiledPattern = new TomTerm_CompiledPattern(this);

    patternTomTerm_AssignedVariable = parse("AssignedVariable(<str>,<term>,<int>,<term>,<term>)");
    funTomTerm_AssignedVariable = makeAFun("_TomTerm_AssignedVariable", 5, false);
    protoTomTerm_AssignedVariable = new TomTerm_AssignedVariable(this);

    patternTomTerm_Automata = parse("Automata(<term>,<term>,<term>)");
    funTomTerm_Automata = makeAFun("_TomTerm_Automata", 3, false);
    protoTomTerm_Automata = new TomTerm_Automata(this);

    patternTomTerm_DefaultAutomata = parse("DefaultAutomata(<term>,<term>,<term>)");
    funTomTerm_DefaultAutomata = makeAFun("_TomTerm_DefaultAutomata", 3, false);
    protoTomTerm_DefaultAutomata = new TomTerm_DefaultAutomata(this);

    patternTomTerm_MatchXML = parse("MatchXML(<str>,<term>,<term>)");
    funTomTerm_MatchXML = makeAFun("_TomTerm_MatchXML", 3, false);
    protoTomTerm_MatchXML = new TomTerm_MatchXML(this);

    patternTomTerm_BackQuoteXML = parse("BackQuoteXML(<term>,<term>)");
    funTomTerm_BackQuoteXML = makeAFun("_TomTerm_BackQuoteXML", 2, false);
    protoTomTerm_BackQuoteXML = new TomTerm_BackQuoteXML(this);

    patternTomTerm_XMLTermToTomTerm = parse("XMLTermToTomTerm(<term>)");
    funTomTerm_XMLTermToTomTerm = makeAFun("_TomTerm_XMLTermToTomTerm", 1, false);
    protoTomTerm_XMLTermToTomTerm = new TomTerm_XMLTermToTomTerm(this);


    patternTomNumber_MatchNumber = parse("MatchNumber(<term>)");
    funTomNumber_MatchNumber = makeAFun("_TomNumber_MatchNumber", 1, false);
    protoTomNumber_MatchNumber = new TomNumber_MatchNumber(this);

    patternTomNumber_PatternNumber = parse("PatternNumber(<term>)");
    funTomNumber_PatternNumber = makeAFun("_TomNumber_PatternNumber", 1, false);
    protoTomNumber_PatternNumber = new TomNumber_PatternNumber(this);

    patternTomNumber_ListNumber = parse("ListNumber(<term>)");
    funTomNumber_ListNumber = makeAFun("_TomNumber_ListNumber", 1, false);
    protoTomNumber_ListNumber = new TomNumber_ListNumber(this);

    patternTomNumber_IndexNumber = parse("IndexNumber(<term>)");
    funTomNumber_IndexNumber = makeAFun("_TomNumber_IndexNumber", 1, false);
    protoTomNumber_IndexNumber = new TomNumber_IndexNumber(this);

    patternTomNumber_Begin = parse("Begin(<term>)");
    funTomNumber_Begin = makeAFun("_TomNumber_Begin", 1, false);
    protoTomNumber_Begin = new TomNumber_Begin(this);

    patternTomNumber_End = parse("End(<term>)");
    funTomNumber_End = makeAFun("_TomNumber_End", 1, false);
    protoTomNumber_End = new TomNumber_End(this);

    patternTomNumber_Number = parse("Number(<int>)");
    funTomNumber_Number = makeAFun("_TomNumber_Number", 1, false);
    protoTomNumber_Number = new TomNumber_Number(this);

    patternTomNumber_AbsVar = parse("AbsVar(<term>)");
    funTomNumber_AbsVar = makeAFun("_TomNumber_AbsVar", 1, false);
    protoTomNumber_AbsVar = new TomNumber_AbsVar(this);

    patternTomNumber_RenamedVar = parse("RenamedVar(<term>)");
    funTomNumber_RenamedVar = makeAFun("_TomNumber_RenamedVar", 1, false);
    protoTomNumber_RenamedVar = new TomNumber_RenamedVar(this);

    patternTomNumber_RuleVar = parse("RuleVar");
    funTomNumber_RuleVar = makeAFun("_TomNumber_RuleVar", 0, false);
    protoTomNumber_RuleVar = new TomNumber_RuleVar(this);


    patternTomRule_RewriteRule = parse("RewriteRule(<term>,<term>,<term>,<term>)");
    funTomRule_RewriteRule = makeAFun("_TomRule_RewriteRule", 4, false);
    protoTomRule_RewriteRule = new TomRule_RewriteRule(this);


    patternInstruction_IfThenElse = parse("IfThenElse(<term>,<term>,<term>)");
    funInstruction_IfThenElse = makeAFun("_Instruction_IfThenElse", 3, false);
    protoInstruction_IfThenElse = new Instruction_IfThenElse(this);

    patternInstruction_DoWhile = parse("DoWhile(<term>,<term>)");
    funInstruction_DoWhile = makeAFun("_Instruction_DoWhile", 2, false);
    protoInstruction_DoWhile = new Instruction_DoWhile(this);

    patternInstruction_Assign = parse("Assign(<term>,<term>)");
    funInstruction_Assign = makeAFun("_Instruction_Assign", 2, false);
    protoInstruction_Assign = new Instruction_Assign(this);

    patternInstruction_AssignMatchSubject = parse("AssignMatchSubject(<term>,<term>)");
    funInstruction_AssignMatchSubject = makeAFun("_Instruction_AssignMatchSubject", 2, false);
    protoInstruction_AssignMatchSubject = new Instruction_AssignMatchSubject(this);

    patternInstruction_Increment = parse("Increment(<term>)");
    funInstruction_Increment = makeAFun("_Instruction_Increment", 1, false);
    protoInstruction_Increment = new Instruction_Increment(this);

    patternInstruction_Action = parse("Action(<term>)");
    funInstruction_Action = makeAFun("_Instruction_Action", 1, false);
    protoInstruction_Action = new Instruction_Action(this);

    patternInstruction_ExitAction = parse("ExitAction(<term>)");
    funInstruction_ExitAction = makeAFun("_Instruction_ExitAction", 1, false);
    protoInstruction_ExitAction = new Instruction_ExitAction(this);

    patternInstruction_Return = parse("Return(<term>)");
    funInstruction_Return = makeAFun("_Instruction_Return", 1, false);
    protoInstruction_Return = new Instruction_Return(this);

    patternInstruction_OpenBlock = parse("OpenBlock");
    funInstruction_OpenBlock = makeAFun("_Instruction_OpenBlock", 0, false);
    protoInstruction_OpenBlock = new Instruction_OpenBlock(this);

    patternInstruction_CloseBlock = parse("CloseBlock");
    funInstruction_CloseBlock = makeAFun("_Instruction_CloseBlock", 0, false);
    protoInstruction_CloseBlock = new Instruction_CloseBlock(this);

    patternInstruction_NamedBlock = parse("NamedBlock(<str>,<term>)");
    funInstruction_NamedBlock = makeAFun("_Instruction_NamedBlock", 2, false);
    protoInstruction_NamedBlock = new Instruction_NamedBlock(this);


    patternTomSymbol_Symbol = parse("Symbol(<term>,<term>,<term>,<term>,<term>)");
    funTomSymbol_Symbol = makeAFun("_TomSymbol_Symbol", 5, false);
    protoTomSymbol_Symbol = new TomSymbol_Symbol(this);


    patternPairNameDecl_Slot = parse("Slot(<term>,<term>)");
    funPairNameDecl_Slot = makeAFun("_PairNameDecl_Slot", 2, false);
    protoPairNameDecl_Slot = new PairNameDecl_Slot(this);


    patternTomSymbolTable_Table = parse("Table(<term>)");
    funTomSymbolTable_Table = makeAFun("_TomSymbolTable_Table", 1, false);
    protoTomSymbolTable_Table = new TomSymbolTable_Table(this);


    patternTomEntry_Entry = parse("Entry(<str>,<term>)");
    funTomEntry_Entry = makeAFun("_TomEntry_Entry", 2, false);
    protoTomEntry_Entry = new TomEntry_Entry(this);


    patternTomStructureTable_StructTable = parse("StructTable(<term>)");
    funTomStructureTable_StructTable = makeAFun("_TomStructureTable_StructTable", 1, false);
    protoTomStructureTable_StructTable = new TomStructureTable_StructTable(this);


    patternXMLTerm_Element = parse("Element(<term>,<term>,<term>)");
    funXMLTerm_Element = makeAFun("_XMLTerm_Element", 3, false);
    protoXMLTerm_Element = new XMLTerm_Element(this);

    patternXMLTerm_Attribute = parse("Attribute(<term>,<term>,<term>)");
    funXMLTerm_Attribute = makeAFun("_XMLTerm_Attribute", 3, false);
    protoXMLTerm_Attribute = new XMLTerm_Attribute(this);

    patternXMLTerm_ReservedWord = parse("ReservedWord(<term>,<term>,<term>)");
    funXMLTerm_ReservedWord = makeAFun("_XMLTerm_ReservedWord", 3, false);
    protoXMLTerm_ReservedWord = new XMLTerm_ReservedWord(this);

    patternXMLTerm_XMLPlaceholder = parse("XMLPlaceholder(<term>)");
    funXMLTerm_XMLPlaceholder = makeAFun("_XMLTerm_XMLPlaceholder", 1, false);
    protoXMLTerm_XMLPlaceholder = new XMLTerm_XMLPlaceholder(this);

    patternXMLTerm_XMLVariable = parse("XMLVariable(<term>,<term>)");
    funXMLTerm_XMLVariable = makeAFun("_XMLTerm_XMLVariable", 2, false);
    protoXMLTerm_XMLVariable = new XMLTerm_XMLVariable(this);

    protoTomList = new TomList(this);
    protoTomList.init(84, null, null, null);
    emptyTomList = (TomList) build(protoTomList);
    emptyTomList.init(84, emptyTomList, null, null);

    protoTomNumberList = new TomNumberList(this);
    protoTomNumberList.init(126, null, null, null);
    emptyTomNumberList = (TomNumberList) build(protoTomNumberList);
    emptyTomNumberList.init(126, emptyTomNumberList, null, null);

    protoTomRuleList = new TomRuleList(this);
    protoTomRuleList.init(168, null, null, null);
    emptyTomRuleList = (TomRuleList) build(protoTomRuleList);
    emptyTomRuleList.init(168, emptyTomRuleList, null, null);

    protoTomTypeList = new TomTypeList(this);
    protoTomTypeList.init(210, null, null, null);
    emptyTomTypeList = (TomTypeList) build(protoTomTypeList);
    emptyTomTypeList.init(210, emptyTomTypeList, null, null);

    protoOptionList = new OptionList(this);
    protoOptionList.init(252, null, null, null);
    emptyOptionList = (OptionList) build(protoOptionList);
    emptyOptionList.init(252, emptyOptionList, null, null);

    protoSlotList = new SlotList(this);
    protoSlotList.init(294, null, null, null);
    emptySlotList = (SlotList) build(protoSlotList);
    emptySlotList.init(294, emptySlotList, null, null);

    protoTomEntryList = new TomEntryList(this);
    protoTomEntryList.init(336, null, null, null);
    emptyTomEntryList = (TomEntryList) build(protoTomEntryList);
    emptyTomEntryList.init(336, emptyTomEntryList, null, null);

  }
  protected Declaration_TypeTermDecl makeDeclaration_TypeTermDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_TypeTermDecl) {
      protoDeclaration_TypeTermDecl.initHashCode(annos,fun,args);
      return (Declaration_TypeTermDecl) build(protoDeclaration_TypeTermDecl);
    }
  }

  public Declaration_TypeTermDecl makeDeclaration_TypeTermDecl(TomName _astName, TomList _keywordList, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _keywordList, _orgTrack};
    return makeDeclaration_TypeTermDecl( funDeclaration_TypeTermDecl, args, empty);
  }

  public Declaration Declaration_TypeTermDeclFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternDeclaration_TypeTermDecl);

    if (children != null) {
      Declaration tmp = makeDeclaration_TypeTermDecl(TomNameFromTerm( (aterm.ATerm) children.get(0)), TomListFromTerm( (aterm.ATerm) children.get(1)), OptionFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Declaration_TypeTermDeclImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((TomList)arg.getArgument(1)).toTerm());
    args.add(((Option)arg.getArgument(2)).toTerm());
    return make(patternDeclaration_TypeTermDecl, args);
  }

  protected Declaration_TypeListDecl makeDeclaration_TypeListDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_TypeListDecl) {
      protoDeclaration_TypeListDecl.initHashCode(annos,fun,args);
      return (Declaration_TypeListDecl) build(protoDeclaration_TypeListDecl);
    }
  }

  public Declaration_TypeListDecl makeDeclaration_TypeListDecl(TomName _astName, TomList _keywordList, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _keywordList, _orgTrack};
    return makeDeclaration_TypeListDecl( funDeclaration_TypeListDecl, args, empty);
  }

  public Declaration Declaration_TypeListDeclFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternDeclaration_TypeListDecl);

    if (children != null) {
      Declaration tmp = makeDeclaration_TypeListDecl(TomNameFromTerm( (aterm.ATerm) children.get(0)), TomListFromTerm( (aterm.ATerm) children.get(1)), OptionFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Declaration_TypeListDeclImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((TomList)arg.getArgument(1)).toTerm());
    args.add(((Option)arg.getArgument(2)).toTerm());
    return make(patternDeclaration_TypeListDecl, args);
  }

  protected Declaration_TypeArrayDecl makeDeclaration_TypeArrayDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_TypeArrayDecl) {
      protoDeclaration_TypeArrayDecl.initHashCode(annos,fun,args);
      return (Declaration_TypeArrayDecl) build(protoDeclaration_TypeArrayDecl);
    }
  }

  public Declaration_TypeArrayDecl makeDeclaration_TypeArrayDecl(TomName _astName, TomList _keywordList, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _keywordList, _orgTrack};
    return makeDeclaration_TypeArrayDecl( funDeclaration_TypeArrayDecl, args, empty);
  }

  public Declaration Declaration_TypeArrayDeclFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternDeclaration_TypeArrayDecl);

    if (children != null) {
      Declaration tmp = makeDeclaration_TypeArrayDecl(TomNameFromTerm( (aterm.ATerm) children.get(0)), TomListFromTerm( (aterm.ATerm) children.get(1)), OptionFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Declaration_TypeArrayDeclImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((TomList)arg.getArgument(1)).toTerm());
    args.add(((Option)arg.getArgument(2)).toTerm());
    return make(patternDeclaration_TypeArrayDecl, args);
  }

  protected Declaration_GetFunctionSymbolDecl makeDeclaration_GetFunctionSymbolDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_GetFunctionSymbolDecl) {
      protoDeclaration_GetFunctionSymbolDecl.initHashCode(annos,fun,args);
      return (Declaration_GetFunctionSymbolDecl) build(protoDeclaration_GetFunctionSymbolDecl);
    }
  }

  public Declaration_GetFunctionSymbolDecl makeDeclaration_GetFunctionSymbolDecl(TomTerm _termArg, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_termArg, _tlCode, _orgTrack};
    return makeDeclaration_GetFunctionSymbolDecl( funDeclaration_GetFunctionSymbolDecl, args, empty);
  }

  public Declaration Declaration_GetFunctionSymbolDeclFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternDeclaration_GetFunctionSymbolDecl);

    if (children != null) {
      Declaration tmp = makeDeclaration_GetFunctionSymbolDecl(TomTermFromTerm( (aterm.ATerm) children.get(0)), TargetLanguageFromTerm( (aterm.ATerm) children.get(1)), OptionFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Declaration_GetFunctionSymbolDeclImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    args.add(((TargetLanguage)arg.getArgument(1)).toTerm());
    args.add(((Option)arg.getArgument(2)).toTerm());
    return make(patternDeclaration_GetFunctionSymbolDecl, args);
  }

  protected Declaration_GetSubtermDecl makeDeclaration_GetSubtermDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_GetSubtermDecl) {
      protoDeclaration_GetSubtermDecl.initHashCode(annos,fun,args);
      return (Declaration_GetSubtermDecl) build(protoDeclaration_GetSubtermDecl);
    }
  }

  public Declaration_GetSubtermDecl makeDeclaration_GetSubtermDecl(TomTerm _termArg, TomTerm _variable, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_termArg, _variable, _tlCode, _orgTrack};
    return makeDeclaration_GetSubtermDecl( funDeclaration_GetSubtermDecl, args, empty);
  }

  public Declaration Declaration_GetSubtermDeclFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternDeclaration_GetSubtermDecl);

    if (children != null) {
      Declaration tmp = makeDeclaration_GetSubtermDecl(TomTermFromTerm( (aterm.ATerm) children.get(0)), TomTermFromTerm( (aterm.ATerm) children.get(1)), TargetLanguageFromTerm( (aterm.ATerm) children.get(2)), OptionFromTerm( (aterm.ATerm) children.get(3)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Declaration_GetSubtermDeclImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    args.add(((TomTerm)arg.getArgument(1)).toTerm());
    args.add(((TargetLanguage)arg.getArgument(2)).toTerm());
    args.add(((Option)arg.getArgument(3)).toTerm());
    return make(patternDeclaration_GetSubtermDecl, args);
  }

  protected Declaration_IsFsymDecl makeDeclaration_IsFsymDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_IsFsymDecl) {
      protoDeclaration_IsFsymDecl.initHashCode(annos,fun,args);
      return (Declaration_IsFsymDecl) build(protoDeclaration_IsFsymDecl);
    }
  }

  public Declaration_IsFsymDecl makeDeclaration_IsFsymDecl(TomName _astName, TomTerm _variable, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _variable, _tlCode, _orgTrack};
    return makeDeclaration_IsFsymDecl( funDeclaration_IsFsymDecl, args, empty);
  }

  public Declaration Declaration_IsFsymDeclFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternDeclaration_IsFsymDecl);

    if (children != null) {
      Declaration tmp = makeDeclaration_IsFsymDecl(TomNameFromTerm( (aterm.ATerm) children.get(0)), TomTermFromTerm( (aterm.ATerm) children.get(1)), TargetLanguageFromTerm( (aterm.ATerm) children.get(2)), OptionFromTerm( (aterm.ATerm) children.get(3)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Declaration_IsFsymDeclImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((TomTerm)arg.getArgument(1)).toTerm());
    args.add(((TargetLanguage)arg.getArgument(2)).toTerm());
    args.add(((Option)arg.getArgument(3)).toTerm());
    return make(patternDeclaration_IsFsymDecl, args);
  }

  protected Declaration_GetSlotDecl makeDeclaration_GetSlotDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_GetSlotDecl) {
      protoDeclaration_GetSlotDecl.initHashCode(annos,fun,args);
      return (Declaration_GetSlotDecl) build(protoDeclaration_GetSlotDecl);
    }
  }

  public Declaration_GetSlotDecl makeDeclaration_GetSlotDecl(TomName _astName, TomName _slotName, TomTerm _variable, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _slotName, _variable, _tlCode, _orgTrack};
    return makeDeclaration_GetSlotDecl( funDeclaration_GetSlotDecl, args, empty);
  }

  public Declaration Declaration_GetSlotDeclFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternDeclaration_GetSlotDecl);

    if (children != null) {
      Declaration tmp = makeDeclaration_GetSlotDecl(TomNameFromTerm( (aterm.ATerm) children.get(0)), TomNameFromTerm( (aterm.ATerm) children.get(1)), TomTermFromTerm( (aterm.ATerm) children.get(2)), TargetLanguageFromTerm( (aterm.ATerm) children.get(3)), OptionFromTerm( (aterm.ATerm) children.get(4)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Declaration_GetSlotDeclImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((TomName)arg.getArgument(1)).toTerm());
    args.add(((TomTerm)arg.getArgument(2)).toTerm());
    args.add(((TargetLanguage)arg.getArgument(3)).toTerm());
    args.add(((Option)arg.getArgument(4)).toTerm());
    return make(patternDeclaration_GetSlotDecl, args);
  }

  protected Declaration_CompareFunctionSymbolDecl makeDeclaration_CompareFunctionSymbolDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_CompareFunctionSymbolDecl) {
      protoDeclaration_CompareFunctionSymbolDecl.initHashCode(annos,fun,args);
      return (Declaration_CompareFunctionSymbolDecl) build(protoDeclaration_CompareFunctionSymbolDecl);
    }
  }

  public Declaration_CompareFunctionSymbolDecl makeDeclaration_CompareFunctionSymbolDecl(TomTerm _symbolArg1, TomTerm _symbolArg2, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_symbolArg1, _symbolArg2, _tlCode, _orgTrack};
    return makeDeclaration_CompareFunctionSymbolDecl( funDeclaration_CompareFunctionSymbolDecl, args, empty);
  }

  public Declaration Declaration_CompareFunctionSymbolDeclFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternDeclaration_CompareFunctionSymbolDecl);

    if (children != null) {
      Declaration tmp = makeDeclaration_CompareFunctionSymbolDecl(TomTermFromTerm( (aterm.ATerm) children.get(0)), TomTermFromTerm( (aterm.ATerm) children.get(1)), TargetLanguageFromTerm( (aterm.ATerm) children.get(2)), OptionFromTerm( (aterm.ATerm) children.get(3)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Declaration_CompareFunctionSymbolDeclImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    args.add(((TomTerm)arg.getArgument(1)).toTerm());
    args.add(((TargetLanguage)arg.getArgument(2)).toTerm());
    args.add(((Option)arg.getArgument(3)).toTerm());
    return make(patternDeclaration_CompareFunctionSymbolDecl, args);
  }

  protected Declaration_TermsEqualDecl makeDeclaration_TermsEqualDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_TermsEqualDecl) {
      protoDeclaration_TermsEqualDecl.initHashCode(annos,fun,args);
      return (Declaration_TermsEqualDecl) build(protoDeclaration_TermsEqualDecl);
    }
  }

  public Declaration_TermsEqualDecl makeDeclaration_TermsEqualDecl(TomTerm _termArg1, TomTerm _termArg2, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_termArg1, _termArg2, _tlCode, _orgTrack};
    return makeDeclaration_TermsEqualDecl( funDeclaration_TermsEqualDecl, args, empty);
  }

  public Declaration Declaration_TermsEqualDeclFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternDeclaration_TermsEqualDecl);

    if (children != null) {
      Declaration tmp = makeDeclaration_TermsEqualDecl(TomTermFromTerm( (aterm.ATerm) children.get(0)), TomTermFromTerm( (aterm.ATerm) children.get(1)), TargetLanguageFromTerm( (aterm.ATerm) children.get(2)), OptionFromTerm( (aterm.ATerm) children.get(3)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Declaration_TermsEqualDeclImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    args.add(((TomTerm)arg.getArgument(1)).toTerm());
    args.add(((TargetLanguage)arg.getArgument(2)).toTerm());
    args.add(((Option)arg.getArgument(3)).toTerm());
    return make(patternDeclaration_TermsEqualDecl, args);
  }

  protected Declaration_GetHeadDecl makeDeclaration_GetHeadDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_GetHeadDecl) {
      protoDeclaration_GetHeadDecl.initHashCode(annos,fun,args);
      return (Declaration_GetHeadDecl) build(protoDeclaration_GetHeadDecl);
    }
  }

  public Declaration_GetHeadDecl makeDeclaration_GetHeadDecl(TomTerm _var, TargetLanguage _tlcode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_var, _tlcode, _orgTrack};
    return makeDeclaration_GetHeadDecl( funDeclaration_GetHeadDecl, args, empty);
  }

  public Declaration Declaration_GetHeadDeclFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternDeclaration_GetHeadDecl);

    if (children != null) {
      Declaration tmp = makeDeclaration_GetHeadDecl(TomTermFromTerm( (aterm.ATerm) children.get(0)), TargetLanguageFromTerm( (aterm.ATerm) children.get(1)), OptionFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Declaration_GetHeadDeclImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    args.add(((TargetLanguage)arg.getArgument(1)).toTerm());
    args.add(((Option)arg.getArgument(2)).toTerm());
    return make(patternDeclaration_GetHeadDecl, args);
  }

  protected Declaration_GetTailDecl makeDeclaration_GetTailDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_GetTailDecl) {
      protoDeclaration_GetTailDecl.initHashCode(annos,fun,args);
      return (Declaration_GetTailDecl) build(protoDeclaration_GetTailDecl);
    }
  }

  public Declaration_GetTailDecl makeDeclaration_GetTailDecl(TomTerm _var, TargetLanguage _tlcode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_var, _tlcode, _orgTrack};
    return makeDeclaration_GetTailDecl( funDeclaration_GetTailDecl, args, empty);
  }

  public Declaration Declaration_GetTailDeclFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternDeclaration_GetTailDecl);

    if (children != null) {
      Declaration tmp = makeDeclaration_GetTailDecl(TomTermFromTerm( (aterm.ATerm) children.get(0)), TargetLanguageFromTerm( (aterm.ATerm) children.get(1)), OptionFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Declaration_GetTailDeclImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    args.add(((TargetLanguage)arg.getArgument(1)).toTerm());
    args.add(((Option)arg.getArgument(2)).toTerm());
    return make(patternDeclaration_GetTailDecl, args);
  }

  protected Declaration_IsEmptyDecl makeDeclaration_IsEmptyDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_IsEmptyDecl) {
      protoDeclaration_IsEmptyDecl.initHashCode(annos,fun,args);
      return (Declaration_IsEmptyDecl) build(protoDeclaration_IsEmptyDecl);
    }
  }

  public Declaration_IsEmptyDecl makeDeclaration_IsEmptyDecl(TomTerm _var, TargetLanguage _tlcode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_var, _tlcode, _orgTrack};
    return makeDeclaration_IsEmptyDecl( funDeclaration_IsEmptyDecl, args, empty);
  }

  public Declaration Declaration_IsEmptyDeclFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternDeclaration_IsEmptyDecl);

    if (children != null) {
      Declaration tmp = makeDeclaration_IsEmptyDecl(TomTermFromTerm( (aterm.ATerm) children.get(0)), TargetLanguageFromTerm( (aterm.ATerm) children.get(1)), OptionFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Declaration_IsEmptyDeclImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    args.add(((TargetLanguage)arg.getArgument(1)).toTerm());
    args.add(((Option)arg.getArgument(2)).toTerm());
    return make(patternDeclaration_IsEmptyDecl, args);
  }

  protected Declaration_MakeEmptyList makeDeclaration_MakeEmptyList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_MakeEmptyList) {
      protoDeclaration_MakeEmptyList.initHashCode(annos,fun,args);
      return (Declaration_MakeEmptyList) build(protoDeclaration_MakeEmptyList);
    }
  }

  public Declaration_MakeEmptyList makeDeclaration_MakeEmptyList(TomName _astName, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _tlCode, _orgTrack};
    return makeDeclaration_MakeEmptyList( funDeclaration_MakeEmptyList, args, empty);
  }

  public Declaration Declaration_MakeEmptyListFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternDeclaration_MakeEmptyList);

    if (children != null) {
      Declaration tmp = makeDeclaration_MakeEmptyList(TomNameFromTerm( (aterm.ATerm) children.get(0)), TargetLanguageFromTerm( (aterm.ATerm) children.get(1)), OptionFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Declaration_MakeEmptyListImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((TargetLanguage)arg.getArgument(1)).toTerm());
    args.add(((Option)arg.getArgument(2)).toTerm());
    return make(patternDeclaration_MakeEmptyList, args);
  }

  protected Declaration_MakeAddList makeDeclaration_MakeAddList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_MakeAddList) {
      protoDeclaration_MakeAddList.initHashCode(annos,fun,args);
      return (Declaration_MakeAddList) build(protoDeclaration_MakeAddList);
    }
  }

  public Declaration_MakeAddList makeDeclaration_MakeAddList(TomName _astName, TomTerm _varElt, TomTerm _varList, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _varElt, _varList, _tlCode, _orgTrack};
    return makeDeclaration_MakeAddList( funDeclaration_MakeAddList, args, empty);
  }

  public Declaration Declaration_MakeAddListFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternDeclaration_MakeAddList);

    if (children != null) {
      Declaration tmp = makeDeclaration_MakeAddList(TomNameFromTerm( (aterm.ATerm) children.get(0)), TomTermFromTerm( (aterm.ATerm) children.get(1)), TomTermFromTerm( (aterm.ATerm) children.get(2)), TargetLanguageFromTerm( (aterm.ATerm) children.get(3)), OptionFromTerm( (aterm.ATerm) children.get(4)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Declaration_MakeAddListImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((TomTerm)arg.getArgument(1)).toTerm());
    args.add(((TomTerm)arg.getArgument(2)).toTerm());
    args.add(((TargetLanguage)arg.getArgument(3)).toTerm());
    args.add(((Option)arg.getArgument(4)).toTerm());
    return make(patternDeclaration_MakeAddList, args);
  }

  protected Declaration_GetElementDecl makeDeclaration_GetElementDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_GetElementDecl) {
      protoDeclaration_GetElementDecl.initHashCode(annos,fun,args);
      return (Declaration_GetElementDecl) build(protoDeclaration_GetElementDecl);
    }
  }

  public Declaration_GetElementDecl makeDeclaration_GetElementDecl(TomTerm _kid1, TomTerm _kid2, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1, _kid2, _tlCode, _orgTrack};
    return makeDeclaration_GetElementDecl( funDeclaration_GetElementDecl, args, empty);
  }

  public Declaration Declaration_GetElementDeclFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternDeclaration_GetElementDecl);

    if (children != null) {
      Declaration tmp = makeDeclaration_GetElementDecl(TomTermFromTerm( (aterm.ATerm) children.get(0)), TomTermFromTerm( (aterm.ATerm) children.get(1)), TargetLanguageFromTerm( (aterm.ATerm) children.get(2)), OptionFromTerm( (aterm.ATerm) children.get(3)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Declaration_GetElementDeclImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    args.add(((TomTerm)arg.getArgument(1)).toTerm());
    args.add(((TargetLanguage)arg.getArgument(2)).toTerm());
    args.add(((Option)arg.getArgument(3)).toTerm());
    return make(patternDeclaration_GetElementDecl, args);
  }

  protected Declaration_GetSizeDecl makeDeclaration_GetSizeDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_GetSizeDecl) {
      protoDeclaration_GetSizeDecl.initHashCode(annos,fun,args);
      return (Declaration_GetSizeDecl) build(protoDeclaration_GetSizeDecl);
    }
  }

  public Declaration_GetSizeDecl makeDeclaration_GetSizeDecl(TomTerm _kid1, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1, _tlCode, _orgTrack};
    return makeDeclaration_GetSizeDecl( funDeclaration_GetSizeDecl, args, empty);
  }

  public Declaration Declaration_GetSizeDeclFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternDeclaration_GetSizeDecl);

    if (children != null) {
      Declaration tmp = makeDeclaration_GetSizeDecl(TomTermFromTerm( (aterm.ATerm) children.get(0)), TargetLanguageFromTerm( (aterm.ATerm) children.get(1)), OptionFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Declaration_GetSizeDeclImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    args.add(((TargetLanguage)arg.getArgument(1)).toTerm());
    args.add(((Option)arg.getArgument(2)).toTerm());
    return make(patternDeclaration_GetSizeDecl, args);
  }

  protected Declaration_MakeEmptyArray makeDeclaration_MakeEmptyArray(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_MakeEmptyArray) {
      protoDeclaration_MakeEmptyArray.initHashCode(annos,fun,args);
      return (Declaration_MakeEmptyArray) build(protoDeclaration_MakeEmptyArray);
    }
  }

  public Declaration_MakeEmptyArray makeDeclaration_MakeEmptyArray(TomName _astName, TomTerm _varSize, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _varSize, _tlCode, _orgTrack};
    return makeDeclaration_MakeEmptyArray( funDeclaration_MakeEmptyArray, args, empty);
  }

  public Declaration Declaration_MakeEmptyArrayFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternDeclaration_MakeEmptyArray);

    if (children != null) {
      Declaration tmp = makeDeclaration_MakeEmptyArray(TomNameFromTerm( (aterm.ATerm) children.get(0)), TomTermFromTerm( (aterm.ATerm) children.get(1)), TargetLanguageFromTerm( (aterm.ATerm) children.get(2)), OptionFromTerm( (aterm.ATerm) children.get(3)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Declaration_MakeEmptyArrayImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((TomTerm)arg.getArgument(1)).toTerm());
    args.add(((TargetLanguage)arg.getArgument(2)).toTerm());
    args.add(((Option)arg.getArgument(3)).toTerm());
    return make(patternDeclaration_MakeEmptyArray, args);
  }

  protected Declaration_MakeAddArray makeDeclaration_MakeAddArray(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_MakeAddArray) {
      protoDeclaration_MakeAddArray.initHashCode(annos,fun,args);
      return (Declaration_MakeAddArray) build(protoDeclaration_MakeAddArray);
    }
  }

  public Declaration_MakeAddArray makeDeclaration_MakeAddArray(TomName _astName, TomTerm _varElt, TomTerm _varList, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _varElt, _varList, _tlCode, _orgTrack};
    return makeDeclaration_MakeAddArray( funDeclaration_MakeAddArray, args, empty);
  }

  public Declaration Declaration_MakeAddArrayFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternDeclaration_MakeAddArray);

    if (children != null) {
      Declaration tmp = makeDeclaration_MakeAddArray(TomNameFromTerm( (aterm.ATerm) children.get(0)), TomTermFromTerm( (aterm.ATerm) children.get(1)), TomTermFromTerm( (aterm.ATerm) children.get(2)), TargetLanguageFromTerm( (aterm.ATerm) children.get(3)), OptionFromTerm( (aterm.ATerm) children.get(4)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Declaration_MakeAddArrayImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((TomTerm)arg.getArgument(1)).toTerm());
    args.add(((TomTerm)arg.getArgument(2)).toTerm());
    args.add(((TargetLanguage)arg.getArgument(3)).toTerm());
    args.add(((Option)arg.getArgument(4)).toTerm());
    return make(patternDeclaration_MakeAddArray, args);
  }

  protected Declaration_MakeDecl makeDeclaration_MakeDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_MakeDecl) {
      protoDeclaration_MakeDecl.initHashCode(annos,fun,args);
      return (Declaration_MakeDecl) build(protoDeclaration_MakeDecl);
    }
  }

  public Declaration_MakeDecl makeDeclaration_MakeDecl(TomName _astName, TomType _astType, TomList _args, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _astType, _args, _tlCode, _orgTrack};
    return makeDeclaration_MakeDecl( funDeclaration_MakeDecl, args, empty);
  }

  public Declaration Declaration_MakeDeclFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternDeclaration_MakeDecl);

    if (children != null) {
      Declaration tmp = makeDeclaration_MakeDecl(TomNameFromTerm( (aterm.ATerm) children.get(0)), TomTypeFromTerm( (aterm.ATerm) children.get(1)), TomListFromTerm( (aterm.ATerm) children.get(2)), TargetLanguageFromTerm( (aterm.ATerm) children.get(3)), OptionFromTerm( (aterm.ATerm) children.get(4)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Declaration_MakeDeclImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((TomType)arg.getArgument(1)).toTerm());
    args.add(((TomList)arg.getArgument(2)).toTerm());
    args.add(((TargetLanguage)arg.getArgument(3)).toTerm());
    args.add(((Option)arg.getArgument(4)).toTerm());
    return make(patternDeclaration_MakeDecl, args);
  }

  protected Declaration_SymbolDecl makeDeclaration_SymbolDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_SymbolDecl) {
      protoDeclaration_SymbolDecl.initHashCode(annos,fun,args);
      return (Declaration_SymbolDecl) build(protoDeclaration_SymbolDecl);
    }
  }

  public Declaration_SymbolDecl makeDeclaration_SymbolDecl(TomName _astName) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName};
    return makeDeclaration_SymbolDecl( funDeclaration_SymbolDecl, args, empty);
  }

  public Declaration Declaration_SymbolDeclFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternDeclaration_SymbolDecl);

    if (children != null) {
      Declaration tmp = makeDeclaration_SymbolDecl(TomNameFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Declaration_SymbolDeclImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    return make(patternDeclaration_SymbolDecl, args);
  }

  protected Declaration_ListSymbolDecl makeDeclaration_ListSymbolDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_ListSymbolDecl) {
      protoDeclaration_ListSymbolDecl.initHashCode(annos,fun,args);
      return (Declaration_ListSymbolDecl) build(protoDeclaration_ListSymbolDecl);
    }
  }

  public Declaration_ListSymbolDecl makeDeclaration_ListSymbolDecl(TomName _astName) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName};
    return makeDeclaration_ListSymbolDecl( funDeclaration_ListSymbolDecl, args, empty);
  }

  public Declaration Declaration_ListSymbolDeclFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternDeclaration_ListSymbolDecl);

    if (children != null) {
      Declaration tmp = makeDeclaration_ListSymbolDecl(TomNameFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Declaration_ListSymbolDeclImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    return make(patternDeclaration_ListSymbolDecl, args);
  }

  protected Declaration_ArraySymbolDecl makeDeclaration_ArraySymbolDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_ArraySymbolDecl) {
      protoDeclaration_ArraySymbolDecl.initHashCode(annos,fun,args);
      return (Declaration_ArraySymbolDecl) build(protoDeclaration_ArraySymbolDecl);
    }
  }

  public Declaration_ArraySymbolDecl makeDeclaration_ArraySymbolDecl(TomName _astName) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName};
    return makeDeclaration_ArraySymbolDecl( funDeclaration_ArraySymbolDecl, args, empty);
  }

  public Declaration Declaration_ArraySymbolDeclFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternDeclaration_ArraySymbolDecl);

    if (children != null) {
      Declaration tmp = makeDeclaration_ArraySymbolDecl(TomNameFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Declaration_ArraySymbolDeclImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    return make(patternDeclaration_ArraySymbolDecl, args);
  }

  protected Declaration_EmptyDeclaration makeDeclaration_EmptyDeclaration(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_EmptyDeclaration) {
      protoDeclaration_EmptyDeclaration.initHashCode(annos,fun,args);
      return (Declaration_EmptyDeclaration) build(protoDeclaration_EmptyDeclaration);
    }
  }

  public Declaration_EmptyDeclaration makeDeclaration_EmptyDeclaration() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeDeclaration_EmptyDeclaration( funDeclaration_EmptyDeclaration, args, empty);
  }

  public Declaration Declaration_EmptyDeclarationFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternDeclaration_EmptyDeclaration);

    if (children != null) {
      Declaration tmp = makeDeclaration_EmptyDeclaration();
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Declaration_EmptyDeclarationImpl arg) {
    java.util.List args = new java.util.LinkedList();
    return make(patternDeclaration_EmptyDeclaration, args);
  }

  protected Option_DeclarationToOption makeOption_DeclarationToOption(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoOption_DeclarationToOption) {
      protoOption_DeclarationToOption.initHashCode(annos,fun,args);
      return (Option_DeclarationToOption) build(protoOption_DeclarationToOption);
    }
  }

  public Option_DeclarationToOption makeOption_DeclarationToOption(Declaration _astDeclaration) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astDeclaration};
    return makeOption_DeclarationToOption( funOption_DeclarationToOption, args, empty);
  }

  public Option Option_DeclarationToOptionFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternOption_DeclarationToOption);

    if (children != null) {
      Option tmp = makeOption_DeclarationToOption(DeclarationFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Option_DeclarationToOptionImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((Declaration)arg.getArgument(0)).toTerm());
    return make(patternOption_DeclarationToOption, args);
  }

  protected Option_TomNameToOption makeOption_TomNameToOption(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoOption_TomNameToOption) {
      protoOption_TomNameToOption.initHashCode(annos,fun,args);
      return (Option_TomNameToOption) build(protoOption_TomNameToOption);
    }
  }

  public Option_TomNameToOption makeOption_TomNameToOption(TomName _astName) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName};
    return makeOption_TomNameToOption( funOption_TomNameToOption, args, empty);
  }

  public Option Option_TomNameToOptionFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternOption_TomNameToOption);

    if (children != null) {
      Option tmp = makeOption_TomNameToOption(TomNameFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Option_TomNameToOptionImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    return make(patternOption_TomNameToOption, args);
  }

  protected Option_TomTermToOption makeOption_TomTermToOption(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoOption_TomTermToOption) {
      protoOption_TomTermToOption.initHashCode(annos,fun,args);
      return (Option_TomTermToOption) build(protoOption_TomTermToOption);
    }
  }

  public Option_TomTermToOption makeOption_TomTermToOption(TomTerm _astTerm) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astTerm};
    return makeOption_TomTermToOption( funOption_TomTermToOption, args, empty);
  }

  public Option Option_TomTermToOptionFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternOption_TomTermToOption);

    if (children != null) {
      Option tmp = makeOption_TomTermToOption(TomTermFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Option_TomTermToOptionImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    return make(patternOption_TomTermToOption, args);
  }

  protected Option_Option makeOption_Option(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoOption_Option) {
      protoOption_Option.initHashCode(annos,fun,args);
      return (Option_Option) build(protoOption_Option);
    }
  }

  public Option_Option makeOption_Option(OptionList _optionList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_optionList};
    return makeOption_Option( funOption_Option, args, empty);
  }

  public Option Option_OptionFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternOption_Option);

    if (children != null) {
      Option tmp = makeOption_Option(OptionListFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Option_OptionImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((OptionList)arg.getArgument(0)).toTerm());
    return make(patternOption_Option, args);
  }

  protected Option_DefinedSymbol makeOption_DefinedSymbol(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoOption_DefinedSymbol) {
      protoOption_DefinedSymbol.initHashCode(annos,fun,args);
      return (Option_DefinedSymbol) build(protoOption_DefinedSymbol);
    }
  }

  public Option_DefinedSymbol makeOption_DefinedSymbol() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeOption_DefinedSymbol( funOption_DefinedSymbol, args, empty);
  }

  public Option Option_DefinedSymbolFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternOption_DefinedSymbol);

    if (children != null) {
      Option tmp = makeOption_DefinedSymbol();
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Option_DefinedSymbolImpl arg) {
    java.util.List args = new java.util.LinkedList();
    return make(patternOption_DefinedSymbol, args);
  }

  protected Option_GeneratedMatch makeOption_GeneratedMatch(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoOption_GeneratedMatch) {
      protoOption_GeneratedMatch.initHashCode(annos,fun,args);
      return (Option_GeneratedMatch) build(protoOption_GeneratedMatch);
    }
  }

  public Option_GeneratedMatch makeOption_GeneratedMatch() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeOption_GeneratedMatch( funOption_GeneratedMatch, args, empty);
  }

  public Option Option_GeneratedMatchFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternOption_GeneratedMatch);

    if (children != null) {
      Option tmp = makeOption_GeneratedMatch();
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Option_GeneratedMatchImpl arg) {
    java.util.List args = new java.util.LinkedList();
    return make(patternOption_GeneratedMatch, args);
  }

  protected Option_WithDefaultProduction makeOption_WithDefaultProduction(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoOption_WithDefaultProduction) {
      protoOption_WithDefaultProduction.initHashCode(annos,fun,args);
      return (Option_WithDefaultProduction) build(protoOption_WithDefaultProduction);
    }
  }

  public Option_WithDefaultProduction makeOption_WithDefaultProduction() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeOption_WithDefaultProduction( funOption_WithDefaultProduction, args, empty);
  }

  public Option Option_WithDefaultProductionFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternOption_WithDefaultProduction);

    if (children != null) {
      Option tmp = makeOption_WithDefaultProduction();
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Option_WithDefaultProductionImpl arg) {
    java.util.List args = new java.util.LinkedList();
    return make(patternOption_WithDefaultProduction, args);
  }

  protected Option_OriginTracking makeOption_OriginTracking(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoOption_OriginTracking) {
      protoOption_OriginTracking.initHashCode(annos,fun,args);
      return (Option_OriginTracking) build(protoOption_OriginTracking);
    }
  }

  public Option_OriginTracking makeOption_OriginTracking(TomName _astName, Integer _line, TomName _fileName) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, makeInt(_line.intValue()), _fileName};
    return makeOption_OriginTracking( funOption_OriginTracking, args, empty);
  }

  public Option Option_OriginTrackingFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternOption_OriginTracking);

    if (children != null) {
      Option tmp = makeOption_OriginTracking(TomNameFromTerm( (aterm.ATerm) children.get(0)), (Integer) children.get(1), TomNameFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Option_OriginTrackingImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(new Integer(((aterm.ATermInt)arg.getArgument(1)).getInt()));
    args.add(((TomName)arg.getArgument(2)).toTerm());
    return make(patternOption_OriginTracking, args);
  }

  protected Option_Constructor makeOption_Constructor(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoOption_Constructor) {
      protoOption_Constructor.initHashCode(annos,fun,args);
      return (Option_Constructor) build(protoOption_Constructor);
    }
  }

  public Option_Constructor makeOption_Constructor(TomName _astName) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName};
    return makeOption_Constructor( funOption_Constructor, args, empty);
  }

  public Option Option_ConstructorFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternOption_Constructor);

    if (children != null) {
      Option tmp = makeOption_Constructor(TomNameFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Option_ConstructorImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    return make(patternOption_Constructor, args);
  }

  protected Option_OriginalText makeOption_OriginalText(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoOption_OriginalText) {
      protoOption_OriginalText.initHashCode(annos,fun,args);
      return (Option_OriginalText) build(protoOption_OriginalText);
    }
  }

  public Option_OriginalText makeOption_OriginalText(TomName _astName) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName};
    return makeOption_OriginalText( funOption_OriginalText, args, empty);
  }

  public Option Option_OriginalTextFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternOption_OriginalText);

    if (children != null) {
      Option tmp = makeOption_OriginalText(TomNameFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Option_OriginalTextImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    return make(patternOption_OriginalText, args);
  }

  protected Option_XMLPosition makeOption_XMLPosition(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoOption_XMLPosition) {
      protoOption_XMLPosition.initHashCode(annos,fun,args);
      return (Option_XMLPosition) build(protoOption_XMLPosition);
    }
  }

  public Option_XMLPosition makeOption_XMLPosition(String _place) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeAppl(makeAFun(_place, 0, true))};
    return makeOption_XMLPosition( funOption_XMLPosition, args, empty);
  }

  public Option Option_XMLPositionFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternOption_XMLPosition);

    if (children != null) {
      Option tmp = makeOption_XMLPosition((String) children.get(0));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Option_XMLPositionImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((aterm.ATermAppl)arg.getArgument(0)).getAFun().getName());
    return make(patternOption_XMLPosition, args);
  }

  protected Expression_TomTermToExpression makeExpression_TomTermToExpression(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_TomTermToExpression) {
      protoExpression_TomTermToExpression.initHashCode(annos,fun,args);
      return (Expression_TomTermToExpression) build(protoExpression_TomTermToExpression);
    }
  }

  public Expression_TomTermToExpression makeExpression_TomTermToExpression(TomTerm _astTerm) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astTerm};
    return makeExpression_TomTermToExpression( funExpression_TomTermToExpression, args, empty);
  }

  public Expression Expression_TomTermToExpressionFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternExpression_TomTermToExpression);

    if (children != null) {
      Expression tmp = makeExpression_TomTermToExpression(TomTermFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Expression_TomTermToExpressionImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    return make(patternExpression_TomTermToExpression, args);
  }

  protected Expression_Not makeExpression_Not(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_Not) {
      protoExpression_Not.initHashCode(annos,fun,args);
      return (Expression_Not) build(protoExpression_Not);
    }
  }

  public Expression_Not makeExpression_Not(Expression _arg) {
    aterm.ATerm[] args = new aterm.ATerm[] {_arg};
    return makeExpression_Not( funExpression_Not, args, empty);
  }

  public Expression Expression_NotFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternExpression_Not);

    if (children != null) {
      Expression tmp = makeExpression_Not(ExpressionFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Expression_NotImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((Expression)arg.getArgument(0)).toTerm());
    return make(patternExpression_Not, args);
  }

  protected Expression_And makeExpression_And(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_And) {
      protoExpression_And.initHashCode(annos,fun,args);
      return (Expression_And) build(protoExpression_And);
    }
  }

  public Expression_And makeExpression_And(Expression _arg1, Expression _arg2) {
    aterm.ATerm[] args = new aterm.ATerm[] {_arg1, _arg2};
    return makeExpression_And( funExpression_And, args, empty);
  }

  public Expression Expression_AndFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternExpression_And);

    if (children != null) {
      Expression tmp = makeExpression_And(ExpressionFromTerm( (aterm.ATerm) children.get(0)), ExpressionFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Expression_AndImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((Expression)arg.getArgument(0)).toTerm());
    args.add(((Expression)arg.getArgument(1)).toTerm());
    return make(patternExpression_And, args);
  }

  protected Expression_TrueTL makeExpression_TrueTL(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_TrueTL) {
      protoExpression_TrueTL.initHashCode(annos,fun,args);
      return (Expression_TrueTL) build(protoExpression_TrueTL);
    }
  }

  public Expression_TrueTL makeExpression_TrueTL() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeExpression_TrueTL( funExpression_TrueTL, args, empty);
  }

  public Expression Expression_TrueTLFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternExpression_TrueTL);

    if (children != null) {
      Expression tmp = makeExpression_TrueTL();
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Expression_TrueTLImpl arg) {
    java.util.List args = new java.util.LinkedList();
    return make(patternExpression_TrueTL, args);
  }

  protected Expression_FalseTL makeExpression_FalseTL(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_FalseTL) {
      protoExpression_FalseTL.initHashCode(annos,fun,args);
      return (Expression_FalseTL) build(protoExpression_FalseTL);
    }
  }

  public Expression_FalseTL makeExpression_FalseTL() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeExpression_FalseTL( funExpression_FalseTL, args, empty);
  }

  public Expression Expression_FalseTLFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternExpression_FalseTL);

    if (children != null) {
      Expression tmp = makeExpression_FalseTL();
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Expression_FalseTLImpl arg) {
    java.util.List args = new java.util.LinkedList();
    return make(patternExpression_FalseTL, args);
  }

  protected Expression_IsEmptyList makeExpression_IsEmptyList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_IsEmptyList) {
      protoExpression_IsEmptyList.initHashCode(annos,fun,args);
      return (Expression_IsEmptyList) build(protoExpression_IsEmptyList);
    }
  }

  public Expression_IsEmptyList makeExpression_IsEmptyList(TomTerm _kid1) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1};
    return makeExpression_IsEmptyList( funExpression_IsEmptyList, args, empty);
  }

  public Expression Expression_IsEmptyListFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternExpression_IsEmptyList);

    if (children != null) {
      Expression tmp = makeExpression_IsEmptyList(TomTermFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Expression_IsEmptyListImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    return make(patternExpression_IsEmptyList, args);
  }

  protected Expression_IsEmptyArray makeExpression_IsEmptyArray(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_IsEmptyArray) {
      protoExpression_IsEmptyArray.initHashCode(annos,fun,args);
      return (Expression_IsEmptyArray) build(protoExpression_IsEmptyArray);
    }
  }

  public Expression_IsEmptyArray makeExpression_IsEmptyArray(TomTerm _kid1, TomTerm _kid2) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1, _kid2};
    return makeExpression_IsEmptyArray( funExpression_IsEmptyArray, args, empty);
  }

  public Expression Expression_IsEmptyArrayFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternExpression_IsEmptyArray);

    if (children != null) {
      Expression tmp = makeExpression_IsEmptyArray(TomTermFromTerm( (aterm.ATerm) children.get(0)), TomTermFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Expression_IsEmptyArrayImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    args.add(((TomTerm)arg.getArgument(1)).toTerm());
    return make(patternExpression_IsEmptyArray, args);
  }

  protected Expression_EqualFunctionSymbol makeExpression_EqualFunctionSymbol(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_EqualFunctionSymbol) {
      protoExpression_EqualFunctionSymbol.initHashCode(annos,fun,args);
      return (Expression_EqualFunctionSymbol) build(protoExpression_EqualFunctionSymbol);
    }
  }

  public Expression_EqualFunctionSymbol makeExpression_EqualFunctionSymbol(TomTerm _kid1, TomTerm _kid2) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1, _kid2};
    return makeExpression_EqualFunctionSymbol( funExpression_EqualFunctionSymbol, args, empty);
  }

  public Expression Expression_EqualFunctionSymbolFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternExpression_EqualFunctionSymbol);

    if (children != null) {
      Expression tmp = makeExpression_EqualFunctionSymbol(TomTermFromTerm( (aterm.ATerm) children.get(0)), TomTermFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Expression_EqualFunctionSymbolImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    args.add(((TomTerm)arg.getArgument(1)).toTerm());
    return make(patternExpression_EqualFunctionSymbol, args);
  }

  protected Expression_EqualTerm makeExpression_EqualTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_EqualTerm) {
      protoExpression_EqualTerm.initHashCode(annos,fun,args);
      return (Expression_EqualTerm) build(protoExpression_EqualTerm);
    }
  }

  public Expression_EqualTerm makeExpression_EqualTerm(TomTerm _kid1, TomTerm _kid2) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1, _kid2};
    return makeExpression_EqualTerm( funExpression_EqualTerm, args, empty);
  }

  public Expression Expression_EqualTermFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternExpression_EqualTerm);

    if (children != null) {
      Expression tmp = makeExpression_EqualTerm(TomTermFromTerm( (aterm.ATerm) children.get(0)), TomTermFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Expression_EqualTermImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    args.add(((TomTerm)arg.getArgument(1)).toTerm());
    return make(patternExpression_EqualTerm, args);
  }

  protected Expression_GetSubterm makeExpression_GetSubterm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_GetSubterm) {
      protoExpression_GetSubterm.initHashCode(annos,fun,args);
      return (Expression_GetSubterm) build(protoExpression_GetSubterm);
    }
  }

  public Expression_GetSubterm makeExpression_GetSubterm(TomTerm _variable, TomNumber _number) {
    aterm.ATerm[] args = new aterm.ATerm[] {_variable, _number};
    return makeExpression_GetSubterm( funExpression_GetSubterm, args, empty);
  }

  public Expression Expression_GetSubtermFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternExpression_GetSubterm);

    if (children != null) {
      Expression tmp = makeExpression_GetSubterm(TomTermFromTerm( (aterm.ATerm) children.get(0)), TomNumberFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Expression_GetSubtermImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    args.add(((TomNumber)arg.getArgument(1)).toTerm());
    return make(patternExpression_GetSubterm, args);
  }

  protected Expression_IsFsym makeExpression_IsFsym(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_IsFsym) {
      protoExpression_IsFsym.initHashCode(annos,fun,args);
      return (Expression_IsFsym) build(protoExpression_IsFsym);
    }
  }

  public Expression_IsFsym makeExpression_IsFsym(TomName _astName, TomTerm _variable) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _variable};
    return makeExpression_IsFsym( funExpression_IsFsym, args, empty);
  }

  public Expression Expression_IsFsymFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternExpression_IsFsym);

    if (children != null) {
      Expression tmp = makeExpression_IsFsym(TomNameFromTerm( (aterm.ATerm) children.get(0)), TomTermFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Expression_IsFsymImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((TomTerm)arg.getArgument(1)).toTerm());
    return make(patternExpression_IsFsym, args);
  }

  protected Expression_GetSlot makeExpression_GetSlot(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_GetSlot) {
      protoExpression_GetSlot.initHashCode(annos,fun,args);
      return (Expression_GetSlot) build(protoExpression_GetSlot);
    }
  }

  public Expression_GetSlot makeExpression_GetSlot(TomName _astName, String _slotNameString, TomTerm _variable) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, makeAppl(makeAFun(_slotNameString, 0, true)), _variable};
    return makeExpression_GetSlot( funExpression_GetSlot, args, empty);
  }

  public Expression Expression_GetSlotFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternExpression_GetSlot);

    if (children != null) {
      Expression tmp = makeExpression_GetSlot(TomNameFromTerm( (aterm.ATerm) children.get(0)), (String) children.get(1), TomTermFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Expression_GetSlotImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((aterm.ATermAppl)arg.getArgument(1)).getAFun().getName());
    args.add(((TomTerm)arg.getArgument(2)).toTerm());
    return make(patternExpression_GetSlot, args);
  }

  protected Expression_GetHead makeExpression_GetHead(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_GetHead) {
      protoExpression_GetHead.initHashCode(annos,fun,args);
      return (Expression_GetHead) build(protoExpression_GetHead);
    }
  }

  public Expression_GetHead makeExpression_GetHead(TomTerm _kid1) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1};
    return makeExpression_GetHead( funExpression_GetHead, args, empty);
  }

  public Expression Expression_GetHeadFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternExpression_GetHead);

    if (children != null) {
      Expression tmp = makeExpression_GetHead(TomTermFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Expression_GetHeadImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    return make(patternExpression_GetHead, args);
  }

  protected Expression_GetTail makeExpression_GetTail(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_GetTail) {
      protoExpression_GetTail.initHashCode(annos,fun,args);
      return (Expression_GetTail) build(protoExpression_GetTail);
    }
  }

  public Expression_GetTail makeExpression_GetTail(TomTerm _kid1) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1};
    return makeExpression_GetTail( funExpression_GetTail, args, empty);
  }

  public Expression Expression_GetTailFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternExpression_GetTail);

    if (children != null) {
      Expression tmp = makeExpression_GetTail(TomTermFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Expression_GetTailImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    return make(patternExpression_GetTail, args);
  }

  protected Expression_GetSize makeExpression_GetSize(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_GetSize) {
      protoExpression_GetSize.initHashCode(annos,fun,args);
      return (Expression_GetSize) build(protoExpression_GetSize);
    }
  }

  public Expression_GetSize makeExpression_GetSize(TomTerm _kid1) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1};
    return makeExpression_GetSize( funExpression_GetSize, args, empty);
  }

  public Expression Expression_GetSizeFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternExpression_GetSize);

    if (children != null) {
      Expression tmp = makeExpression_GetSize(TomTermFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Expression_GetSizeImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    return make(patternExpression_GetSize, args);
  }

  protected Expression_GetElement makeExpression_GetElement(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_GetElement) {
      protoExpression_GetElement.initHashCode(annos,fun,args);
      return (Expression_GetElement) build(protoExpression_GetElement);
    }
  }

  public Expression_GetElement makeExpression_GetElement(TomTerm _kid1, TomTerm _kid2) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1, _kid2};
    return makeExpression_GetElement( funExpression_GetElement, args, empty);
  }

  public Expression Expression_GetElementFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternExpression_GetElement);

    if (children != null) {
      Expression tmp = makeExpression_GetElement(TomTermFromTerm( (aterm.ATerm) children.get(0)), TomTermFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Expression_GetElementImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    args.add(((TomTerm)arg.getArgument(1)).toTerm());
    return make(patternExpression_GetElement, args);
  }

  protected Expression_GetSliceList makeExpression_GetSliceList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_GetSliceList) {
      protoExpression_GetSliceList.initHashCode(annos,fun,args);
      return (Expression_GetSliceList) build(protoExpression_GetSliceList);
    }
  }

  public Expression_GetSliceList makeExpression_GetSliceList(TomName _astName, TomTerm _variableBeginAST, TomTerm _variableEndAST) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _variableBeginAST, _variableEndAST};
    return makeExpression_GetSliceList( funExpression_GetSliceList, args, empty);
  }

  public Expression Expression_GetSliceListFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternExpression_GetSliceList);

    if (children != null) {
      Expression tmp = makeExpression_GetSliceList(TomNameFromTerm( (aterm.ATerm) children.get(0)), TomTermFromTerm( (aterm.ATerm) children.get(1)), TomTermFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Expression_GetSliceListImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((TomTerm)arg.getArgument(1)).toTerm());
    args.add(((TomTerm)arg.getArgument(2)).toTerm());
    return make(patternExpression_GetSliceList, args);
  }

  protected Expression_GetSliceArray makeExpression_GetSliceArray(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_GetSliceArray) {
      protoExpression_GetSliceArray.initHashCode(annos,fun,args);
      return (Expression_GetSliceArray) build(protoExpression_GetSliceArray);
    }
  }

  public Expression_GetSliceArray makeExpression_GetSliceArray(TomName _astName, TomTerm _subjectListName, TomTerm _variableBeginAST, TomTerm _variableEndAST) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _subjectListName, _variableBeginAST, _variableEndAST};
    return makeExpression_GetSliceArray( funExpression_GetSliceArray, args, empty);
  }

  public Expression Expression_GetSliceArrayFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternExpression_GetSliceArray);

    if (children != null) {
      Expression tmp = makeExpression_GetSliceArray(TomNameFromTerm( (aterm.ATerm) children.get(0)), TomTermFromTerm( (aterm.ATerm) children.get(1)), TomTermFromTerm( (aterm.ATerm) children.get(2)), TomTermFromTerm( (aterm.ATerm) children.get(3)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Expression_GetSliceArrayImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((TomTerm)arg.getArgument(1)).toTerm());
    args.add(((TomTerm)arg.getArgument(2)).toTerm());
    args.add(((TomTerm)arg.getArgument(3)).toTerm());
    return make(patternExpression_GetSliceArray, args);
  }

  protected TargetLanguage_TL makeTargetLanguage_TL(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTargetLanguage_TL) {
      protoTargetLanguage_TL.initHashCode(annos,fun,args);
      return (TargetLanguage_TL) build(protoTargetLanguage_TL);
    }
  }

  public TargetLanguage_TL makeTargetLanguage_TL(String _code, Position _start, Position _end) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeAppl(makeAFun(_code, 0, true)), _start, _end};
    return makeTargetLanguage_TL( funTargetLanguage_TL, args, empty);
  }

  public TargetLanguage TargetLanguage_TLFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTargetLanguage_TL);

    if (children != null) {
      TargetLanguage tmp = makeTargetLanguage_TL((String) children.get(0), PositionFromTerm( (aterm.ATerm) children.get(1)), PositionFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TargetLanguage_TLImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((aterm.ATermAppl)arg.getArgument(0)).getAFun().getName());
    args.add(((Position)arg.getArgument(1)).toTerm());
    args.add(((Position)arg.getArgument(2)).toTerm());
    return make(patternTargetLanguage_TL, args);
  }

  protected TargetLanguage_ITL makeTargetLanguage_ITL(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTargetLanguage_ITL) {
      protoTargetLanguage_ITL.initHashCode(annos,fun,args);
      return (TargetLanguage_ITL) build(protoTargetLanguage_ITL);
    }
  }

  public TargetLanguage_ITL makeTargetLanguage_ITL(String _code) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeAppl(makeAFun(_code, 0, true))};
    return makeTargetLanguage_ITL( funTargetLanguage_ITL, args, empty);
  }

  public TargetLanguage TargetLanguage_ITLFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTargetLanguage_ITL);

    if (children != null) {
      TargetLanguage tmp = makeTargetLanguage_ITL((String) children.get(0));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TargetLanguage_ITLImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((aterm.ATermAppl)arg.getArgument(0)).getAFun().getName());
    return make(patternTargetLanguage_ITL, args);
  }

  protected Position_Position makePosition_Position(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoPosition_Position) {
      protoPosition_Position.initHashCode(annos,fun,args);
      return (Position_Position) build(protoPosition_Position);
    }
  }

  public Position_Position makePosition_Position(Integer _line, Integer _column) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeInt(_line.intValue()), makeInt(_column.intValue())};
    return makePosition_Position( funPosition_Position, args, empty);
  }

  public Position Position_PositionFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternPosition_Position);

    if (children != null) {
      Position tmp = makePosition_Position((Integer) children.get(0), (Integer) children.get(1));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Position_PositionImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(new Integer(((aterm.ATermInt)arg.getArgument(0)).getInt()));
    args.add(new Integer(((aterm.ATermInt)arg.getArgument(1)).getInt()));
    return make(patternPosition_Position, args);
  }

  protected TomType_Type makeTomType_Type(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomType_Type) {
      protoTomType_Type.initHashCode(annos,fun,args);
      return (TomType_Type) build(protoTomType_Type);
    }
  }

  public TomType_Type makeTomType_Type(TomType _tomType, TomType _tlType) {
    aterm.ATerm[] args = new aterm.ATerm[] {_tomType, _tlType};
    return makeTomType_Type( funTomType_Type, args, empty);
  }

  public TomType TomType_TypeFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomType_Type);

    if (children != null) {
      TomType tmp = makeTomType_Type(TomTypeFromTerm( (aterm.ATerm) children.get(0)), TomTypeFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomType_TypeImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomType)arg.getArgument(0)).toTerm());
    args.add(((TomType)arg.getArgument(1)).toTerm());
    return make(patternTomType_Type, args);
  }

  protected TomType_TypesToType makeTomType_TypesToType(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomType_TypesToType) {
      protoTomType_TypesToType.initHashCode(annos,fun,args);
      return (TomType_TypesToType) build(protoTomType_TypesToType);
    }
  }

  public TomType_TypesToType makeTomType_TypesToType(TomTypeList _domain, TomType _codomain) {
    aterm.ATerm[] args = new aterm.ATerm[] {_domain, _codomain};
    return makeTomType_TypesToType( funTomType_TypesToType, args, empty);
  }

  public TomType TomType_TypesToTypeFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomType_TypesToType);

    if (children != null) {
      TomType tmp = makeTomType_TypesToType(TomTypeListFromTerm( (aterm.ATerm) children.get(0)), TomTypeFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomType_TypesToTypeImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTypeList)arg.getArgument(0)).toTerm());
    args.add(((TomType)arg.getArgument(1)).toTerm());
    return make(patternTomType_TypesToType, args);
  }

  protected TomType_TomType makeTomType_TomType(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomType_TomType) {
      protoTomType_TomType.initHashCode(annos,fun,args);
      return (TomType_TomType) build(protoTomType_TomType);
    }
  }

  public TomType_TomType makeTomType_TomType(String _string) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeAppl(makeAFun(_string, 0, true))};
    return makeTomType_TomType( funTomType_TomType, args, empty);
  }

  public TomType TomType_TomTypeFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomType_TomType);

    if (children != null) {
      TomType tmp = makeTomType_TomType((String) children.get(0));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomType_TomTypeImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((aterm.ATermAppl)arg.getArgument(0)).getAFun().getName());
    return make(patternTomType_TomType, args);
  }

  protected TomType_TomTypeAlone makeTomType_TomTypeAlone(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomType_TomTypeAlone) {
      protoTomType_TomTypeAlone.initHashCode(annos,fun,args);
      return (TomType_TomTypeAlone) build(protoTomType_TomTypeAlone);
    }
  }

  public TomType_TomTypeAlone makeTomType_TomTypeAlone(String _string) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeAppl(makeAFun(_string, 0, true))};
    return makeTomType_TomTypeAlone( funTomType_TomTypeAlone, args, empty);
  }

  public TomType TomType_TomTypeAloneFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomType_TomTypeAlone);

    if (children != null) {
      TomType tmp = makeTomType_TomTypeAlone((String) children.get(0));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomType_TomTypeAloneImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((aterm.ATermAppl)arg.getArgument(0)).getAFun().getName());
    return make(patternTomType_TomTypeAlone, args);
  }

  protected TomType_TLType makeTomType_TLType(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomType_TLType) {
      protoTomType_TLType.initHashCode(annos,fun,args);
      return (TomType_TLType) build(protoTomType_TLType);
    }
  }

  public TomType_TLType makeTomType_TLType(TargetLanguage _tl) {
    aterm.ATerm[] args = new aterm.ATerm[] {_tl};
    return makeTomType_TLType( funTomType_TLType, args, empty);
  }

  public TomType TomType_TLTypeFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomType_TLType);

    if (children != null) {
      TomType tmp = makeTomType_TLType(TargetLanguageFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomType_TLTypeImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TargetLanguage)arg.getArgument(0)).toTerm());
    return make(patternTomType_TLType, args);
  }

  protected TomType_EmptyType makeTomType_EmptyType(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomType_EmptyType) {
      protoTomType_EmptyType.initHashCode(annos,fun,args);
      return (TomType_EmptyType) build(protoTomType_EmptyType);
    }
  }

  public TomType_EmptyType makeTomType_EmptyType() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeTomType_EmptyType( funTomType_EmptyType, args, empty);
  }

  public TomType TomType_EmptyTypeFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomType_EmptyType);

    if (children != null) {
      TomType tmp = makeTomType_EmptyType();
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomType_EmptyTypeImpl arg) {
    java.util.List args = new java.util.LinkedList();
    return make(patternTomType_EmptyType, args);
  }

  protected TomName_Name makeTomName_Name(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomName_Name) {
      protoTomName_Name.initHashCode(annos,fun,args);
      return (TomName_Name) build(protoTomName_Name);
    }
  }

  public TomName_Name makeTomName_Name(String _string) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeAppl(makeAFun(_string, 0, true))};
    return makeTomName_Name( funTomName_Name, args, empty);
  }

  public TomName TomName_NameFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomName_Name);

    if (children != null) {
      TomName tmp = makeTomName_Name((String) children.get(0));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomName_NameImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((aterm.ATermAppl)arg.getArgument(0)).getAFun().getName());
    return make(patternTomName_Name, args);
  }

  protected TomName_PositionName makeTomName_PositionName(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomName_PositionName) {
      protoTomName_PositionName.initHashCode(annos,fun,args);
      return (TomName_PositionName) build(protoTomName_PositionName);
    }
  }

  public TomName_PositionName makeTomName_PositionName(TomNumberList _numberList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_numberList};
    return makeTomName_PositionName( funTomName_PositionName, args, empty);
  }

  public TomName TomName_PositionNameFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomName_PositionName);

    if (children != null) {
      TomName tmp = makeTomName_PositionName(TomNumberListFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomName_PositionNameImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomNumberList)arg.getArgument(0)).toTerm());
    return make(patternTomName_PositionName, args);
  }

  protected TomName_EmptyName makeTomName_EmptyName(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomName_EmptyName) {
      protoTomName_EmptyName.initHashCode(annos,fun,args);
      return (TomName_EmptyName) build(protoTomName_EmptyName);
    }
  }

  public TomName_EmptyName makeTomName_EmptyName() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeTomName_EmptyName( funTomName_EmptyName, args, empty);
  }

  public TomName TomName_EmptyNameFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomName_EmptyName);

    if (children != null) {
      TomName tmp = makeTomName_EmptyName();
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomName_EmptyNameImpl arg) {
    java.util.List args = new java.util.LinkedList();
    return make(patternTomName_EmptyName, args);
  }

  protected TomTerm_TargetLanguageToTomTerm makeTomTerm_TargetLanguageToTomTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_TargetLanguageToTomTerm) {
      protoTomTerm_TargetLanguageToTomTerm.initHashCode(annos,fun,args);
      return (TomTerm_TargetLanguageToTomTerm) build(protoTomTerm_TargetLanguageToTomTerm);
    }
  }

  public TomTerm_TargetLanguageToTomTerm makeTomTerm_TargetLanguageToTomTerm(TargetLanguage _tl) {
    aterm.ATerm[] args = new aterm.ATerm[] {_tl};
    return makeTomTerm_TargetLanguageToTomTerm( funTomTerm_TargetLanguageToTomTerm, args, empty);
  }

  public TomTerm TomTerm_TargetLanguageToTomTermFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_TargetLanguageToTomTerm);

    if (children != null) {
      TomTerm tmp = makeTomTerm_TargetLanguageToTomTerm(TargetLanguageFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_TargetLanguageToTomTermImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TargetLanguage)arg.getArgument(0)).toTerm());
    return make(patternTomTerm_TargetLanguageToTomTerm, args);
  }

  protected TomTerm_TomTypeToTomTerm makeTomTerm_TomTypeToTomTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_TomTypeToTomTerm) {
      protoTomTerm_TomTypeToTomTerm.initHashCode(annos,fun,args);
      return (TomTerm_TomTypeToTomTerm) build(protoTomTerm_TomTypeToTomTerm);
    }
  }

  public TomTerm_TomTypeToTomTerm makeTomTerm_TomTypeToTomTerm(TomType _astType) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astType};
    return makeTomTerm_TomTypeToTomTerm( funTomTerm_TomTypeToTomTerm, args, empty);
  }

  public TomTerm TomTerm_TomTypeToTomTermFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_TomTypeToTomTerm);

    if (children != null) {
      TomTerm tmp = makeTomTerm_TomTypeToTomTerm(TomTypeFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_TomTypeToTomTermImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomType)arg.getArgument(0)).toTerm());
    return make(patternTomTerm_TomTypeToTomTerm, args);
  }

  protected TomTerm_TomNameToTomTerm makeTomTerm_TomNameToTomTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_TomNameToTomTerm) {
      protoTomTerm_TomNameToTomTerm.initHashCode(annos,fun,args);
      return (TomTerm_TomNameToTomTerm) build(protoTomTerm_TomNameToTomTerm);
    }
  }

  public TomTerm_TomNameToTomTerm makeTomTerm_TomNameToTomTerm(TomName _astName) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName};
    return makeTomTerm_TomNameToTomTerm( funTomTerm_TomNameToTomTerm, args, empty);
  }

  public TomTerm TomTerm_TomNameToTomTermFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_TomNameToTomTerm);

    if (children != null) {
      TomTerm tmp = makeTomTerm_TomNameToTomTerm(TomNameFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_TomNameToTomTermImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    return make(patternTomTerm_TomNameToTomTerm, args);
  }

  protected TomTerm_TomSymbolToTomTerm makeTomTerm_TomSymbolToTomTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_TomSymbolToTomTerm) {
      protoTomTerm_TomSymbolToTomTerm.initHashCode(annos,fun,args);
      return (TomTerm_TomSymbolToTomTerm) build(protoTomTerm_TomSymbolToTomTerm);
    }
  }

  public TomTerm_TomSymbolToTomTerm makeTomTerm_TomSymbolToTomTerm(TomSymbol _astSymbol) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astSymbol};
    return makeTomTerm_TomSymbolToTomTerm( funTomTerm_TomSymbolToTomTerm, args, empty);
  }

  public TomTerm TomTerm_TomSymbolToTomTermFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_TomSymbolToTomTerm);

    if (children != null) {
      TomTerm tmp = makeTomTerm_TomSymbolToTomTerm(TomSymbolFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_TomSymbolToTomTermImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomSymbol)arg.getArgument(0)).toTerm());
    return make(patternTomTerm_TomSymbolToTomTerm, args);
  }

  protected TomTerm_DeclarationToTomTerm makeTomTerm_DeclarationToTomTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_DeclarationToTomTerm) {
      protoTomTerm_DeclarationToTomTerm.initHashCode(annos,fun,args);
      return (TomTerm_DeclarationToTomTerm) build(protoTomTerm_DeclarationToTomTerm);
    }
  }

  public TomTerm_DeclarationToTomTerm makeTomTerm_DeclarationToTomTerm(Declaration _astDeclaration) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astDeclaration};
    return makeTomTerm_DeclarationToTomTerm( funTomTerm_DeclarationToTomTerm, args, empty);
  }

  public TomTerm TomTerm_DeclarationToTomTermFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_DeclarationToTomTerm);

    if (children != null) {
      TomTerm tmp = makeTomTerm_DeclarationToTomTerm(DeclarationFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_DeclarationToTomTermImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((Declaration)arg.getArgument(0)).toTerm());
    return make(patternTomTerm_DeclarationToTomTerm, args);
  }

  protected TomTerm_OptionToTomTerm makeTomTerm_OptionToTomTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_OptionToTomTerm) {
      protoTomTerm_OptionToTomTerm.initHashCode(annos,fun,args);
      return (TomTerm_OptionToTomTerm) build(protoTomTerm_OptionToTomTerm);
    }
  }

  public TomTerm_OptionToTomTerm makeTomTerm_OptionToTomTerm(Option _astOption) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astOption};
    return makeTomTerm_OptionToTomTerm( funTomTerm_OptionToTomTerm, args, empty);
  }

  public TomTerm TomTerm_OptionToTomTermFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_OptionToTomTerm);

    if (children != null) {
      TomTerm tmp = makeTomTerm_OptionToTomTerm(OptionFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_OptionToTomTermImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((Option)arg.getArgument(0)).toTerm());
    return make(patternTomTerm_OptionToTomTerm, args);
  }

  protected TomTerm_ExpressionToTomTerm makeTomTerm_ExpressionToTomTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_ExpressionToTomTerm) {
      protoTomTerm_ExpressionToTomTerm.initHashCode(annos,fun,args);
      return (TomTerm_ExpressionToTomTerm) build(protoTomTerm_ExpressionToTomTerm);
    }
  }

  public TomTerm_ExpressionToTomTerm makeTomTerm_ExpressionToTomTerm(Expression _astExpression) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astExpression};
    return makeTomTerm_ExpressionToTomTerm( funTomTerm_ExpressionToTomTerm, args, empty);
  }

  public TomTerm TomTerm_ExpressionToTomTermFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_ExpressionToTomTerm);

    if (children != null) {
      TomTerm tmp = makeTomTerm_ExpressionToTomTerm(ExpressionFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_ExpressionToTomTermImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((Expression)arg.getArgument(0)).toTerm());
    return make(patternTomTerm_ExpressionToTomTerm, args);
  }

  protected TomTerm_InstructionToTomTerm makeTomTerm_InstructionToTomTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_InstructionToTomTerm) {
      protoTomTerm_InstructionToTomTerm.initHashCode(annos,fun,args);
      return (TomTerm_InstructionToTomTerm) build(protoTomTerm_InstructionToTomTerm);
    }
  }

  public TomTerm_InstructionToTomTerm makeTomTerm_InstructionToTomTerm(Instruction _astInstruction) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astInstruction};
    return makeTomTerm_InstructionToTomTerm( funTomTerm_InstructionToTomTerm, args, empty);
  }

  public TomTerm TomTerm_InstructionToTomTermFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_InstructionToTomTerm);

    if (children != null) {
      TomTerm tmp = makeTomTerm_InstructionToTomTerm(InstructionFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_InstructionToTomTermImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((Instruction)arg.getArgument(0)).toTerm());
    return make(patternTomTerm_InstructionToTomTerm, args);
  }

  protected TomTerm_TomRuleToTomTerm makeTomTerm_TomRuleToTomTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_TomRuleToTomTerm) {
      protoTomTerm_TomRuleToTomTerm.initHashCode(annos,fun,args);
      return (TomTerm_TomRuleToTomTerm) build(protoTomTerm_TomRuleToTomTerm);
    }
  }

  public TomTerm_TomRuleToTomTerm makeTomTerm_TomRuleToTomTerm(TomRule _astTomRule) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astTomRule};
    return makeTomTerm_TomRuleToTomTerm( funTomTerm_TomRuleToTomTerm, args, empty);
  }

  public TomTerm TomTerm_TomRuleToTomTermFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_TomRuleToTomTerm);

    if (children != null) {
      TomTerm tmp = makeTomTerm_TomRuleToTomTerm(TomRuleFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_TomRuleToTomTermImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomRule)arg.getArgument(0)).toTerm());
    return make(patternTomTerm_TomRuleToTomTerm, args);
  }

  protected TomTerm_Tom makeTomTerm_Tom(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Tom) {
      protoTomTerm_Tom.initHashCode(annos,fun,args);
      return (TomTerm_Tom) build(protoTomTerm_Tom);
    }
  }

  public TomTerm_Tom makeTomTerm_Tom(TomList _tomList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_tomList};
    return makeTomTerm_Tom( funTomTerm_Tom, args, empty);
  }

  public TomTerm TomTerm_TomFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_Tom);

    if (children != null) {
      TomTerm tmp = makeTomTerm_Tom(TomListFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_TomImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomList)arg.getArgument(0)).toTerm());
    return make(patternTomTerm_Tom, args);
  }

  protected TomTerm_TomInclude makeTomTerm_TomInclude(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_TomInclude) {
      protoTomTerm_TomInclude.initHashCode(annos,fun,args);
      return (TomTerm_TomInclude) build(protoTomTerm_TomInclude);
    }
  }

  public TomTerm_TomInclude makeTomTerm_TomInclude(TomList _tomList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_tomList};
    return makeTomTerm_TomInclude( funTomTerm_TomInclude, args, empty);
  }

  public TomTerm TomTerm_TomIncludeFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_TomInclude);

    if (children != null) {
      TomTerm tmp = makeTomTerm_TomInclude(TomListFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_TomIncludeImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomList)arg.getArgument(0)).toTerm());
    return make(patternTomTerm_TomInclude, args);
  }

  protected TomTerm_MakeTerm makeTomTerm_MakeTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_MakeTerm) {
      protoTomTerm_MakeTerm.initHashCode(annos,fun,args);
      return (TomTerm_MakeTerm) build(protoTomTerm_MakeTerm);
    }
  }

  public TomTerm_MakeTerm makeTomTerm_MakeTerm(TomTerm _kid1) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1};
    return makeTomTerm_MakeTerm( funTomTerm_MakeTerm, args, empty);
  }

  public TomTerm TomTerm_MakeTermFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_MakeTerm);

    if (children != null) {
      TomTerm tmp = makeTomTerm_MakeTerm(TomTermFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_MakeTermImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    return make(patternTomTerm_MakeTerm, args);
  }

  protected TomTerm_BackQuoteTerm makeTomTerm_BackQuoteTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_BackQuoteTerm) {
      protoTomTerm_BackQuoteTerm.initHashCode(annos,fun,args);
      return (TomTerm_BackQuoteTerm) build(protoTomTerm_BackQuoteTerm);
    }
  }

  public TomTerm_BackQuoteTerm makeTomTerm_BackQuoteTerm(TomTerm _tomTerm, Option _option) {
    aterm.ATerm[] args = new aterm.ATerm[] {_tomTerm, _option};
    return makeTomTerm_BackQuoteTerm( funTomTerm_BackQuoteTerm, args, empty);
  }

  public TomTerm TomTerm_BackQuoteTermFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_BackQuoteTerm);

    if (children != null) {
      TomTerm tmp = makeTomTerm_BackQuoteTerm(TomTermFromTerm( (aterm.ATerm) children.get(0)), OptionFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_BackQuoteTermImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    args.add(((Option)arg.getArgument(1)).toTerm());
    return make(patternTomTerm_BackQuoteTerm, args);
  }

  protected TomTerm_FunctionCall makeTomTerm_FunctionCall(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_FunctionCall) {
      protoTomTerm_FunctionCall.initHashCode(annos,fun,args);
      return (TomTerm_FunctionCall) build(protoTomTerm_FunctionCall);
    }
  }

  public TomTerm_FunctionCall makeTomTerm_FunctionCall(TomName _astName, TomList _args) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _args};
    return makeTomTerm_FunctionCall( funTomTerm_FunctionCall, args, empty);
  }

  public TomTerm TomTerm_FunctionCallFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_FunctionCall);

    if (children != null) {
      TomTerm tmp = makeTomTerm_FunctionCall(TomNameFromTerm( (aterm.ATerm) children.get(0)), TomListFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_FunctionCallImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((TomList)arg.getArgument(1)).toTerm());
    return make(patternTomTerm_FunctionCall, args);
  }

  protected TomTerm_MakeFunctionBegin makeTomTerm_MakeFunctionBegin(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_MakeFunctionBegin) {
      protoTomTerm_MakeFunctionBegin.initHashCode(annos,fun,args);
      return (TomTerm_MakeFunctionBegin) build(protoTomTerm_MakeFunctionBegin);
    }
  }

  public TomTerm_MakeFunctionBegin makeTomTerm_MakeFunctionBegin(TomName _astName, TomTerm _subjectListAST) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _subjectListAST};
    return makeTomTerm_MakeFunctionBegin( funTomTerm_MakeFunctionBegin, args, empty);
  }

  public TomTerm TomTerm_MakeFunctionBeginFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_MakeFunctionBegin);

    if (children != null) {
      TomTerm tmp = makeTomTerm_MakeFunctionBegin(TomNameFromTerm( (aterm.ATerm) children.get(0)), TomTermFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_MakeFunctionBeginImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((TomTerm)arg.getArgument(1)).toTerm());
    return make(patternTomTerm_MakeFunctionBegin, args);
  }

  protected TomTerm_MakeFunctionEnd makeTomTerm_MakeFunctionEnd(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_MakeFunctionEnd) {
      protoTomTerm_MakeFunctionEnd.initHashCode(annos,fun,args);
      return (TomTerm_MakeFunctionEnd) build(protoTomTerm_MakeFunctionEnd);
    }
  }

  public TomTerm_MakeFunctionEnd makeTomTerm_MakeFunctionEnd() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeTomTerm_MakeFunctionEnd( funTomTerm_MakeFunctionEnd, args, empty);
  }

  public TomTerm TomTerm_MakeFunctionEndFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_MakeFunctionEnd);

    if (children != null) {
      TomTerm tmp = makeTomTerm_MakeFunctionEnd();
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_MakeFunctionEndImpl arg) {
    java.util.List args = new java.util.LinkedList();
    return make(patternTomTerm_MakeFunctionEnd, args);
  }

  protected TomTerm_Appl makeTomTerm_Appl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Appl) {
      protoTomTerm_Appl.initHashCode(annos,fun,args);
      return (TomTerm_Appl) build(protoTomTerm_Appl);
    }
  }

  public TomTerm_Appl makeTomTerm_Appl(Option _option, TomName _astName, TomList _args) {
    aterm.ATerm[] args = new aterm.ATerm[] {_option, _astName, _args};
    return makeTomTerm_Appl( funTomTerm_Appl, args, empty);
  }

  public TomTerm TomTerm_ApplFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_Appl);

    if (children != null) {
      TomTerm tmp = makeTomTerm_Appl(OptionFromTerm( (aterm.ATerm) children.get(0)), TomNameFromTerm( (aterm.ATerm) children.get(1)), TomListFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_ApplImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((Option)arg.getArgument(0)).toTerm());
    args.add(((TomName)arg.getArgument(1)).toTerm());
    args.add(((TomList)arg.getArgument(2)).toTerm());
    return make(patternTomTerm_Appl, args);
  }

  protected TomTerm_RecordAppl makeTomTerm_RecordAppl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_RecordAppl) {
      protoTomTerm_RecordAppl.initHashCode(annos,fun,args);
      return (TomTerm_RecordAppl) build(protoTomTerm_RecordAppl);
    }
  }

  public TomTerm_RecordAppl makeTomTerm_RecordAppl(Option _option, TomName _astName, TomList _args) {
    aterm.ATerm[] args = new aterm.ATerm[] {_option, _astName, _args};
    return makeTomTerm_RecordAppl( funTomTerm_RecordAppl, args, empty);
  }

  public TomTerm TomTerm_RecordApplFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_RecordAppl);

    if (children != null) {
      TomTerm tmp = makeTomTerm_RecordAppl(OptionFromTerm( (aterm.ATerm) children.get(0)), TomNameFromTerm( (aterm.ATerm) children.get(1)), TomListFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_RecordApplImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((Option)arg.getArgument(0)).toTerm());
    args.add(((TomName)arg.getArgument(1)).toTerm());
    args.add(((TomList)arg.getArgument(2)).toTerm());
    return make(patternTomTerm_RecordAppl, args);
  }

  protected TomTerm_PairSlotAppl makeTomTerm_PairSlotAppl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_PairSlotAppl) {
      protoTomTerm_PairSlotAppl.initHashCode(annos,fun,args);
      return (TomTerm_PairSlotAppl) build(protoTomTerm_PairSlotAppl);
    }
  }

  public TomTerm_PairSlotAppl makeTomTerm_PairSlotAppl(TomName _slotName, TomTerm _appl) {
    aterm.ATerm[] args = new aterm.ATerm[] {_slotName, _appl};
    return makeTomTerm_PairSlotAppl( funTomTerm_PairSlotAppl, args, empty);
  }

  public TomTerm TomTerm_PairSlotApplFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_PairSlotAppl);

    if (children != null) {
      TomTerm tmp = makeTomTerm_PairSlotAppl(TomNameFromTerm( (aterm.ATerm) children.get(0)), TomTermFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_PairSlotApplImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((TomTerm)arg.getArgument(1)).toTerm());
    return make(patternTomTerm_PairSlotAppl, args);
  }

  protected TomTerm_Match makeTomTerm_Match(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Match) {
      protoTomTerm_Match.initHashCode(annos,fun,args);
      return (TomTerm_Match) build(protoTomTerm_Match);
    }
  }

  public TomTerm_Match makeTomTerm_Match(TomTerm _subjectList, TomTerm _patternList, Option _option) {
    aterm.ATerm[] args = new aterm.ATerm[] {_subjectList, _patternList, _option};
    return makeTomTerm_Match( funTomTerm_Match, args, empty);
  }

  public TomTerm TomTerm_MatchFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_Match);

    if (children != null) {
      TomTerm tmp = makeTomTerm_Match(TomTermFromTerm( (aterm.ATerm) children.get(0)), TomTermFromTerm( (aterm.ATerm) children.get(1)), OptionFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_MatchImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    args.add(((TomTerm)arg.getArgument(1)).toTerm());
    args.add(((Option)arg.getArgument(2)).toTerm());
    return make(patternTomTerm_Match, args);
  }

  protected TomTerm_MatchingCondition makeTomTerm_MatchingCondition(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_MatchingCondition) {
      protoTomTerm_MatchingCondition.initHashCode(annos,fun,args);
      return (TomTerm_MatchingCondition) build(protoTomTerm_MatchingCondition);
    }
  }

  public TomTerm_MatchingCondition makeTomTerm_MatchingCondition(TomTerm _lhs, TomTerm _rhs) {
    aterm.ATerm[] args = new aterm.ATerm[] {_lhs, _rhs};
    return makeTomTerm_MatchingCondition( funTomTerm_MatchingCondition, args, empty);
  }

  public TomTerm TomTerm_MatchingConditionFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_MatchingCondition);

    if (children != null) {
      TomTerm tmp = makeTomTerm_MatchingCondition(TomTermFromTerm( (aterm.ATerm) children.get(0)), TomTermFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_MatchingConditionImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    args.add(((TomTerm)arg.getArgument(1)).toTerm());
    return make(patternTomTerm_MatchingCondition, args);
  }

  protected TomTerm_EqualityCondition makeTomTerm_EqualityCondition(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_EqualityCondition) {
      protoTomTerm_EqualityCondition.initHashCode(annos,fun,args);
      return (TomTerm_EqualityCondition) build(protoTomTerm_EqualityCondition);
    }
  }

  public TomTerm_EqualityCondition makeTomTerm_EqualityCondition(TomTerm _lhs, TomTerm _rhs) {
    aterm.ATerm[] args = new aterm.ATerm[] {_lhs, _rhs};
    return makeTomTerm_EqualityCondition( funTomTerm_EqualityCondition, args, empty);
  }

  public TomTerm TomTerm_EqualityConditionFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_EqualityCondition);

    if (children != null) {
      TomTerm tmp = makeTomTerm_EqualityCondition(TomTermFromTerm( (aterm.ATerm) children.get(0)), TomTermFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_EqualityConditionImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    args.add(((TomTerm)arg.getArgument(1)).toTerm());
    return make(patternTomTerm_EqualityCondition, args);
  }

  protected TomTerm_RuleSet makeTomTerm_RuleSet(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_RuleSet) {
      protoTomTerm_RuleSet.initHashCode(annos,fun,args);
      return (TomTerm_RuleSet) build(protoTomTerm_RuleSet);
    }
  }

  public TomTerm_RuleSet makeTomTerm_RuleSet(TomRuleList _ruleList, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_ruleList, _orgTrack};
    return makeTomTerm_RuleSet( funTomTerm_RuleSet, args, empty);
  }

  public TomTerm TomTerm_RuleSetFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_RuleSet);

    if (children != null) {
      TomTerm tmp = makeTomTerm_RuleSet(TomRuleListFromTerm( (aterm.ATerm) children.get(0)), OptionFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_RuleSetImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomRuleList)arg.getArgument(0)).toTerm());
    args.add(((Option)arg.getArgument(1)).toTerm());
    return make(patternTomTerm_RuleSet, args);
  }

  protected TomTerm_SubjectList makeTomTerm_SubjectList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_SubjectList) {
      protoTomTerm_SubjectList.initHashCode(annos,fun,args);
      return (TomTerm_SubjectList) build(protoTomTerm_SubjectList);
    }
  }

  public TomTerm_SubjectList makeTomTerm_SubjectList(TomList _tomList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_tomList};
    return makeTomTerm_SubjectList( funTomTerm_SubjectList, args, empty);
  }

  public TomTerm TomTerm_SubjectListFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_SubjectList);

    if (children != null) {
      TomTerm tmp = makeTomTerm_SubjectList(TomListFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_SubjectListImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomList)arg.getArgument(0)).toTerm());
    return make(patternTomTerm_SubjectList, args);
  }

  protected TomTerm_PatternList makeTomTerm_PatternList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_PatternList) {
      protoTomTerm_PatternList.initHashCode(annos,fun,args);
      return (TomTerm_PatternList) build(protoTomTerm_PatternList);
    }
  }

  public TomTerm_PatternList makeTomTerm_PatternList(TomList _tomList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_tomList};
    return makeTomTerm_PatternList( funTomTerm_PatternList, args, empty);
  }

  public TomTerm TomTerm_PatternListFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_PatternList);

    if (children != null) {
      TomTerm tmp = makeTomTerm_PatternList(TomListFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_PatternListImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomList)arg.getArgument(0)).toTerm());
    return make(patternTomTerm_PatternList, args);
  }

  protected TomTerm_TermList makeTomTerm_TermList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_TermList) {
      protoTomTerm_TermList.initHashCode(annos,fun,args);
      return (TomTerm_TermList) build(protoTomTerm_TermList);
    }
  }

  public TomTerm_TermList makeTomTerm_TermList(TomList _tomList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_tomList};
    return makeTomTerm_TermList( funTomTerm_TermList, args, empty);
  }

  public TomTerm TomTerm_TermListFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_TermList);

    if (children != null) {
      TomTerm tmp = makeTomTerm_TermList(TomListFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_TermListImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomList)arg.getArgument(0)).toTerm());
    return make(patternTomTerm_TermList, args);
  }

  protected TomTerm_Term makeTomTerm_Term(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Term) {
      protoTomTerm_Term.initHashCode(annos,fun,args);
      return (TomTerm_Term) build(protoTomTerm_Term);
    }
  }

  public TomTerm_Term makeTomTerm_Term(TomTerm _tomTerm) {
    aterm.ATerm[] args = new aterm.ATerm[] {_tomTerm};
    return makeTomTerm_Term( funTomTerm_Term, args, empty);
  }

  public TomTerm TomTerm_TermFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_Term);

    if (children != null) {
      TomTerm tmp = makeTomTerm_Term(TomTermFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_TermImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    return make(patternTomTerm_Term, args);
  }

  protected TomTerm_PatternAction makeTomTerm_PatternAction(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_PatternAction) {
      protoTomTerm_PatternAction.initHashCode(annos,fun,args);
      return (TomTerm_PatternAction) build(protoTomTerm_PatternAction);
    }
  }

  public TomTerm_PatternAction makeTomTerm_PatternAction(TomTerm _termList, TomTerm _tom, Option _option) {
    aterm.ATerm[] args = new aterm.ATerm[] {_termList, _tom, _option};
    return makeTomTerm_PatternAction( funTomTerm_PatternAction, args, empty);
  }

  public TomTerm TomTerm_PatternActionFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_PatternAction);

    if (children != null) {
      TomTerm tmp = makeTomTerm_PatternAction(TomTermFromTerm( (aterm.ATerm) children.get(0)), TomTermFromTerm( (aterm.ATerm) children.get(1)), OptionFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_PatternActionImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    args.add(((TomTerm)arg.getArgument(1)).toTerm());
    args.add(((Option)arg.getArgument(2)).toTerm());
    return make(patternTomTerm_PatternAction, args);
  }

  protected TomTerm_DefaultPatternAction makeTomTerm_DefaultPatternAction(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_DefaultPatternAction) {
      protoTomTerm_DefaultPatternAction.initHashCode(annos,fun,args);
      return (TomTerm_DefaultPatternAction) build(protoTomTerm_DefaultPatternAction);
    }
  }

  public TomTerm_DefaultPatternAction makeTomTerm_DefaultPatternAction(TomTerm _termList, TomTerm _tom, Option _option) {
    aterm.ATerm[] args = new aterm.ATerm[] {_termList, _tom, _option};
    return makeTomTerm_DefaultPatternAction( funTomTerm_DefaultPatternAction, args, empty);
  }

  public TomTerm TomTerm_DefaultPatternActionFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_DefaultPatternAction);

    if (children != null) {
      TomTerm tmp = makeTomTerm_DefaultPatternAction(TomTermFromTerm( (aterm.ATerm) children.get(0)), TomTermFromTerm( (aterm.ATerm) children.get(1)), OptionFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_DefaultPatternActionImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    args.add(((TomTerm)arg.getArgument(1)).toTerm());
    args.add(((Option)arg.getArgument(2)).toTerm());
    return make(patternTomTerm_DefaultPatternAction, args);
  }

  protected TomTerm_TLVar makeTomTerm_TLVar(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_TLVar) {
      protoTomTerm_TLVar.initHashCode(annos,fun,args);
      return (TomTerm_TLVar) build(protoTomTerm_TLVar);
    }
  }

  public TomTerm_TLVar makeTomTerm_TLVar(String _strName, TomType _astType) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeAppl(makeAFun(_strName, 0, true)), _astType};
    return makeTomTerm_TLVar( funTomTerm_TLVar, args, empty);
  }

  public TomTerm TomTerm_TLVarFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_TLVar);

    if (children != null) {
      TomTerm tmp = makeTomTerm_TLVar((String) children.get(0), TomTypeFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_TLVarImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((aterm.ATermAppl)arg.getArgument(0)).getAFun().getName());
    args.add(((TomType)arg.getArgument(1)).toTerm());
    return make(patternTomTerm_TLVar, args);
  }

  protected TomTerm_Declaration makeTomTerm_Declaration(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Declaration) {
      protoTomTerm_Declaration.initHashCode(annos,fun,args);
      return (TomTerm_Declaration) build(protoTomTerm_Declaration);
    }
  }

  public TomTerm_Declaration makeTomTerm_Declaration(TomTerm _kid1) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1};
    return makeTomTerm_Declaration( funTomTerm_Declaration, args, empty);
  }

  public TomTerm TomTerm_DeclarationFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_Declaration);

    if (children != null) {
      TomTerm tmp = makeTomTerm_Declaration(TomTermFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_DeclarationImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    return make(patternTomTerm_Declaration, args);
  }

  protected TomTerm_Variable makeTomTerm_Variable(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Variable) {
      protoTomTerm_Variable.initHashCode(annos,fun,args);
      return (TomTerm_Variable) build(protoTomTerm_Variable);
    }
  }

  public TomTerm_Variable makeTomTerm_Variable(Option _option, TomName _astName, TomType _astType) {
    aterm.ATerm[] args = new aterm.ATerm[] {_option, _astName, _astType};
    return makeTomTerm_Variable( funTomTerm_Variable, args, empty);
  }

  public TomTerm TomTerm_VariableFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_Variable);

    if (children != null) {
      TomTerm tmp = makeTomTerm_Variable(OptionFromTerm( (aterm.ATerm) children.get(0)), TomNameFromTerm( (aterm.ATerm) children.get(1)), TomTypeFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_VariableImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((Option)arg.getArgument(0)).toTerm());
    args.add(((TomName)arg.getArgument(1)).toTerm());
    args.add(((TomType)arg.getArgument(2)).toTerm());
    return make(patternTomTerm_Variable, args);
  }

  protected TomTerm_VariableStar makeTomTerm_VariableStar(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_VariableStar) {
      protoTomTerm_VariableStar.initHashCode(annos,fun,args);
      return (TomTerm_VariableStar) build(protoTomTerm_VariableStar);
    }
  }

  public TomTerm_VariableStar makeTomTerm_VariableStar(Option _option, TomName _astName, TomType _astType) {
    aterm.ATerm[] args = new aterm.ATerm[] {_option, _astName, _astType};
    return makeTomTerm_VariableStar( funTomTerm_VariableStar, args, empty);
  }

  public TomTerm TomTerm_VariableStarFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_VariableStar);

    if (children != null) {
      TomTerm tmp = makeTomTerm_VariableStar(OptionFromTerm( (aterm.ATerm) children.get(0)), TomNameFromTerm( (aterm.ATerm) children.get(1)), TomTypeFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_VariableStarImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((Option)arg.getArgument(0)).toTerm());
    args.add(((TomName)arg.getArgument(1)).toTerm());
    args.add(((TomType)arg.getArgument(2)).toTerm());
    return make(patternTomTerm_VariableStar, args);
  }

  protected TomTerm_Placeholder makeTomTerm_Placeholder(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Placeholder) {
      protoTomTerm_Placeholder.initHashCode(annos,fun,args);
      return (TomTerm_Placeholder) build(protoTomTerm_Placeholder);
    }
  }

  public TomTerm_Placeholder makeTomTerm_Placeholder(Option _option) {
    aterm.ATerm[] args = new aterm.ATerm[] {_option};
    return makeTomTerm_Placeholder( funTomTerm_Placeholder, args, empty);
  }

  public TomTerm TomTerm_PlaceholderFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_Placeholder);

    if (children != null) {
      TomTerm tmp = makeTomTerm_Placeholder(OptionFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_PlaceholderImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((Option)arg.getArgument(0)).toTerm());
    return make(patternTomTerm_Placeholder, args);
  }

  protected TomTerm_UnamedVariable makeTomTerm_UnamedVariable(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_UnamedVariable) {
      protoTomTerm_UnamedVariable.initHashCode(annos,fun,args);
      return (TomTerm_UnamedVariable) build(protoTomTerm_UnamedVariable);
    }
  }

  public TomTerm_UnamedVariable makeTomTerm_UnamedVariable(Option _option, TomType _astType) {
    aterm.ATerm[] args = new aterm.ATerm[] {_option, _astType};
    return makeTomTerm_UnamedVariable( funTomTerm_UnamedVariable, args, empty);
  }

  public TomTerm TomTerm_UnamedVariableFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_UnamedVariable);

    if (children != null) {
      TomTerm tmp = makeTomTerm_UnamedVariable(OptionFromTerm( (aterm.ATerm) children.get(0)), TomTypeFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_UnamedVariableImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((Option)arg.getArgument(0)).toTerm());
    args.add(((TomType)arg.getArgument(1)).toTerm());
    return make(patternTomTerm_UnamedVariable, args);
  }

  protected TomTerm_DotTerm makeTomTerm_DotTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_DotTerm) {
      protoTomTerm_DotTerm.initHashCode(annos,fun,args);
      return (TomTerm_DotTerm) build(protoTomTerm_DotTerm);
    }
  }

  public TomTerm_DotTerm makeTomTerm_DotTerm(TomTerm _kid1, TomTerm _kid2) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1, _kid2};
    return makeTomTerm_DotTerm( funTomTerm_DotTerm, args, empty);
  }

  public TomTerm TomTerm_DotTermFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_DotTerm);

    if (children != null) {
      TomTerm tmp = makeTomTerm_DotTerm(TomTermFromTerm( (aterm.ATerm) children.get(0)), TomTermFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_DotTermImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    args.add(((TomTerm)arg.getArgument(1)).toTerm());
    return make(patternTomTerm_DotTerm, args);
  }

  protected TomTerm_LocalVariable makeTomTerm_LocalVariable(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_LocalVariable) {
      protoTomTerm_LocalVariable.initHashCode(annos,fun,args);
      return (TomTerm_LocalVariable) build(protoTomTerm_LocalVariable);
    }
  }

  public TomTerm_LocalVariable makeTomTerm_LocalVariable() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeTomTerm_LocalVariable( funTomTerm_LocalVariable, args, empty);
  }

  public TomTerm TomTerm_LocalVariableFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_LocalVariable);

    if (children != null) {
      TomTerm tmp = makeTomTerm_LocalVariable();
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_LocalVariableImpl arg) {
    java.util.List args = new java.util.LinkedList();
    return make(patternTomTerm_LocalVariable, args);
  }

  protected TomTerm_EndLocalVariable makeTomTerm_EndLocalVariable(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_EndLocalVariable) {
      protoTomTerm_EndLocalVariable.initHashCode(annos,fun,args);
      return (TomTerm_EndLocalVariable) build(protoTomTerm_EndLocalVariable);
    }
  }

  public TomTerm_EndLocalVariable makeTomTerm_EndLocalVariable() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeTomTerm_EndLocalVariable( funTomTerm_EndLocalVariable, args, empty);
  }

  public TomTerm TomTerm_EndLocalVariableFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_EndLocalVariable);

    if (children != null) {
      TomTerm tmp = makeTomTerm_EndLocalVariable();
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_EndLocalVariableImpl arg) {
    java.util.List args = new java.util.LinkedList();
    return make(patternTomTerm_EndLocalVariable, args);
  }

  protected TomTerm_BuildVariable makeTomTerm_BuildVariable(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_BuildVariable) {
      protoTomTerm_BuildVariable.initHashCode(annos,fun,args);
      return (TomTerm_BuildVariable) build(protoTomTerm_BuildVariable);
    }
  }

  public TomTerm_BuildVariable makeTomTerm_BuildVariable(TomName _astName) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName};
    return makeTomTerm_BuildVariable( funTomTerm_BuildVariable, args, empty);
  }

  public TomTerm TomTerm_BuildVariableFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_BuildVariable);

    if (children != null) {
      TomTerm tmp = makeTomTerm_BuildVariable(TomNameFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_BuildVariableImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    return make(patternTomTerm_BuildVariable, args);
  }

  protected TomTerm_BuildTerm makeTomTerm_BuildTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_BuildTerm) {
      protoTomTerm_BuildTerm.initHashCode(annos,fun,args);
      return (TomTerm_BuildTerm) build(protoTomTerm_BuildTerm);
    }
  }

  public TomTerm_BuildTerm makeTomTerm_BuildTerm(TomName _astName, TomList _args) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _args};
    return makeTomTerm_BuildTerm( funTomTerm_BuildTerm, args, empty);
  }

  public TomTerm TomTerm_BuildTermFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_BuildTerm);

    if (children != null) {
      TomTerm tmp = makeTomTerm_BuildTerm(TomNameFromTerm( (aterm.ATerm) children.get(0)), TomListFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_BuildTermImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((TomList)arg.getArgument(1)).toTerm());
    return make(patternTomTerm_BuildTerm, args);
  }

  protected TomTerm_BuildList makeTomTerm_BuildList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_BuildList) {
      protoTomTerm_BuildList.initHashCode(annos,fun,args);
      return (TomTerm_BuildList) build(protoTomTerm_BuildList);
    }
  }

  public TomTerm_BuildList makeTomTerm_BuildList(TomName _astName, TomList _args) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _args};
    return makeTomTerm_BuildList( funTomTerm_BuildList, args, empty);
  }

  public TomTerm TomTerm_BuildListFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_BuildList);

    if (children != null) {
      TomTerm tmp = makeTomTerm_BuildList(TomNameFromTerm( (aterm.ATerm) children.get(0)), TomListFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_BuildListImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((TomList)arg.getArgument(1)).toTerm());
    return make(patternTomTerm_BuildList, args);
  }

  protected TomTerm_BuildArray makeTomTerm_BuildArray(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_BuildArray) {
      protoTomTerm_BuildArray.initHashCode(annos,fun,args);
      return (TomTerm_BuildArray) build(protoTomTerm_BuildArray);
    }
  }

  public TomTerm_BuildArray makeTomTerm_BuildArray(TomName _astName, TomList _args) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _args};
    return makeTomTerm_BuildArray( funTomTerm_BuildArray, args, empty);
  }

  public TomTerm TomTerm_BuildArrayFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_BuildArray);

    if (children != null) {
      TomTerm tmp = makeTomTerm_BuildArray(TomNameFromTerm( (aterm.ATerm) children.get(0)), TomListFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_BuildArrayImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((TomList)arg.getArgument(1)).toTerm());
    return make(patternTomTerm_BuildArray, args);
  }

  protected TomTerm_BuildBuiltin makeTomTerm_BuildBuiltin(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_BuildBuiltin) {
      protoTomTerm_BuildBuiltin.initHashCode(annos,fun,args);
      return (TomTerm_BuildBuiltin) build(protoTomTerm_BuildBuiltin);
    }
  }

  public TomTerm_BuildBuiltin makeTomTerm_BuildBuiltin(TomName _astName) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName};
    return makeTomTerm_BuildBuiltin( funTomTerm_BuildBuiltin, args, empty);
  }

  public TomTerm TomTerm_BuildBuiltinFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_BuildBuiltin);

    if (children != null) {
      TomTerm tmp = makeTomTerm_BuildBuiltin(TomNameFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_BuildBuiltinImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    return make(patternTomTerm_BuildBuiltin, args);
  }

  protected TomTerm_CompiledMatch makeTomTerm_CompiledMatch(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_CompiledMatch) {
      protoTomTerm_CompiledMatch.initHashCode(annos,fun,args);
      return (TomTerm_CompiledMatch) build(protoTomTerm_CompiledMatch);
    }
  }

  public TomTerm_CompiledMatch makeTomTerm_CompiledMatch(TomList _decls, TomList _automataList, Option _option) {
    aterm.ATerm[] args = new aterm.ATerm[] {_decls, _automataList, _option};
    return makeTomTerm_CompiledMatch( funTomTerm_CompiledMatch, args, empty);
  }

  public TomTerm TomTerm_CompiledMatchFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_CompiledMatch);

    if (children != null) {
      TomTerm tmp = makeTomTerm_CompiledMatch(TomListFromTerm( (aterm.ATerm) children.get(0)), TomListFromTerm( (aterm.ATerm) children.get(1)), OptionFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_CompiledMatchImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomList)arg.getArgument(0)).toTerm());
    args.add(((TomList)arg.getArgument(1)).toTerm());
    args.add(((Option)arg.getArgument(2)).toTerm());
    return make(patternTomTerm_CompiledMatch, args);
  }

  protected TomTerm_CompiledPattern makeTomTerm_CompiledPattern(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_CompiledPattern) {
      protoTomTerm_CompiledPattern.initHashCode(annos,fun,args);
      return (TomTerm_CompiledPattern) build(protoTomTerm_CompiledPattern);
    }
  }

  public TomTerm_CompiledPattern makeTomTerm_CompiledPattern(TomList _instList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_instList};
    return makeTomTerm_CompiledPattern( funTomTerm_CompiledPattern, args, empty);
  }

  public TomTerm TomTerm_CompiledPatternFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_CompiledPattern);

    if (children != null) {
      TomTerm tmp = makeTomTerm_CompiledPattern(TomListFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_CompiledPatternImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomList)arg.getArgument(0)).toTerm());
    return make(patternTomTerm_CompiledPattern, args);
  }

  protected TomTerm_AssignedVariable makeTomTerm_AssignedVariable(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_AssignedVariable) {
      protoTomTerm_AssignedVariable.initHashCode(annos,fun,args);
      return (TomTerm_AssignedVariable) build(protoTomTerm_AssignedVariable);
    }
  }

  public TomTerm_AssignedVariable makeTomTerm_AssignedVariable(String _varName, Expression _source, Integer _nbUse, Expression _usedInDoWhile, Expression _removable) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeAppl(makeAFun(_varName, 0, true)), _source, makeInt(_nbUse.intValue()), _usedInDoWhile, _removable};
    return makeTomTerm_AssignedVariable( funTomTerm_AssignedVariable, args, empty);
  }

  public TomTerm TomTerm_AssignedVariableFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_AssignedVariable);

    if (children != null) {
      TomTerm tmp = makeTomTerm_AssignedVariable((String) children.get(0), ExpressionFromTerm( (aterm.ATerm) children.get(1)), (Integer) children.get(2), ExpressionFromTerm( (aterm.ATerm) children.get(3)), ExpressionFromTerm( (aterm.ATerm) children.get(4)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_AssignedVariableImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((aterm.ATermAppl)arg.getArgument(0)).getAFun().getName());
    args.add(((Expression)arg.getArgument(1)).toTerm());
    args.add(new Integer(((aterm.ATermInt)arg.getArgument(2)).getInt()));
    args.add(((Expression)arg.getArgument(3)).toTerm());
    args.add(((Expression)arg.getArgument(4)).toTerm());
    return make(patternTomTerm_AssignedVariable, args);
  }

  protected TomTerm_Automata makeTomTerm_Automata(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Automata) {
      protoTomTerm_Automata.initHashCode(annos,fun,args);
      return (TomTerm_Automata) build(protoTomTerm_Automata);
    }
  }

  public TomTerm_Automata makeTomTerm_Automata(TomNumberList _numberList, TomList _instList, TomName _debugName) {
    aterm.ATerm[] args = new aterm.ATerm[] {_numberList, _instList, _debugName};
    return makeTomTerm_Automata( funTomTerm_Automata, args, empty);
  }

  public TomTerm TomTerm_AutomataFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_Automata);

    if (children != null) {
      TomTerm tmp = makeTomTerm_Automata(TomNumberListFromTerm( (aterm.ATerm) children.get(0)), TomListFromTerm( (aterm.ATerm) children.get(1)), TomNameFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_AutomataImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomNumberList)arg.getArgument(0)).toTerm());
    args.add(((TomList)arg.getArgument(1)).toTerm());
    args.add(((TomName)arg.getArgument(2)).toTerm());
    return make(patternTomTerm_Automata, args);
  }

  protected TomTerm_DefaultAutomata makeTomTerm_DefaultAutomata(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_DefaultAutomata) {
      protoTomTerm_DefaultAutomata.initHashCode(annos,fun,args);
      return (TomTerm_DefaultAutomata) build(protoTomTerm_DefaultAutomata);
    }
  }

  public TomTerm_DefaultAutomata makeTomTerm_DefaultAutomata(TomNumberList _numberList, TomList _instList, TomName _debugName) {
    aterm.ATerm[] args = new aterm.ATerm[] {_numberList, _instList, _debugName};
    return makeTomTerm_DefaultAutomata( funTomTerm_DefaultAutomata, args, empty);
  }

  public TomTerm TomTerm_DefaultAutomataFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_DefaultAutomata);

    if (children != null) {
      TomTerm tmp = makeTomTerm_DefaultAutomata(TomNumberListFromTerm( (aterm.ATerm) children.get(0)), TomListFromTerm( (aterm.ATerm) children.get(1)), TomNameFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_DefaultAutomataImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomNumberList)arg.getArgument(0)).toTerm());
    args.add(((TomList)arg.getArgument(1)).toTerm());
    args.add(((TomName)arg.getArgument(2)).toTerm());
    return make(patternTomTerm_DefaultAutomata, args);
  }

  protected TomTerm_MatchXML makeTomTerm_MatchXML(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_MatchXML) {
      protoTomTerm_MatchXML.initHashCode(annos,fun,args);
      return (TomTerm_MatchXML) build(protoTomTerm_MatchXML);
    }
  }

  public TomTerm_MatchXML makeTomTerm_MatchXML(String _docName, TomTerm _patternList, Option _option) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeAppl(makeAFun(_docName, 0, true)), _patternList, _option};
    return makeTomTerm_MatchXML( funTomTerm_MatchXML, args, empty);
  }

  public TomTerm TomTerm_MatchXMLFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_MatchXML);

    if (children != null) {
      TomTerm tmp = makeTomTerm_MatchXML((String) children.get(0), TomTermFromTerm( (aterm.ATerm) children.get(1)), OptionFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_MatchXMLImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((aterm.ATermAppl)arg.getArgument(0)).getAFun().getName());
    args.add(((TomTerm)arg.getArgument(1)).toTerm());
    args.add(((Option)arg.getArgument(2)).toTerm());
    return make(patternTomTerm_MatchXML, args);
  }

  protected TomTerm_BackQuoteXML makeTomTerm_BackQuoteXML(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_BackQuoteXML) {
      protoTomTerm_BackQuoteXML.initHashCode(annos,fun,args);
      return (TomTerm_BackQuoteXML) build(protoTomTerm_BackQuoteXML);
    }
  }

  public TomTerm_BackQuoteXML makeTomTerm_BackQuoteXML(XMLTerm _xmlTerm, Option _option) {
    aterm.ATerm[] args = new aterm.ATerm[] {_xmlTerm, _option};
    return makeTomTerm_BackQuoteXML( funTomTerm_BackQuoteXML, args, empty);
  }

  public TomTerm TomTerm_BackQuoteXMLFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_BackQuoteXML);

    if (children != null) {
      TomTerm tmp = makeTomTerm_BackQuoteXML(XMLTermFromTerm( (aterm.ATerm) children.get(0)), OptionFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_BackQuoteXMLImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((XMLTerm)arg.getArgument(0)).toTerm());
    args.add(((Option)arg.getArgument(1)).toTerm());
    return make(patternTomTerm_BackQuoteXML, args);
  }

  protected TomTerm_XMLTermToTomTerm makeTomTerm_XMLTermToTomTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_XMLTermToTomTerm) {
      protoTomTerm_XMLTermToTomTerm.initHashCode(annos,fun,args);
      return (TomTerm_XMLTermToTomTerm) build(protoTomTerm_XMLTermToTomTerm);
    }
  }

  public TomTerm_XMLTermToTomTerm makeTomTerm_XMLTermToTomTerm(XMLTerm _xmlTerm) {
    aterm.ATerm[] args = new aterm.ATerm[] {_xmlTerm};
    return makeTomTerm_XMLTermToTomTerm( funTomTerm_XMLTermToTomTerm, args, empty);
  }

  public TomTerm TomTerm_XMLTermToTomTermFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomTerm_XMLTermToTomTerm);

    if (children != null) {
      TomTerm tmp = makeTomTerm_XMLTermToTomTerm(XMLTermFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomTerm_XMLTermToTomTermImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((XMLTerm)arg.getArgument(0)).toTerm());
    return make(patternTomTerm_XMLTermToTomTerm, args);
  }

  protected TomNumber_MatchNumber makeTomNumber_MatchNumber(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomNumber_MatchNumber) {
      protoTomNumber_MatchNumber.initHashCode(annos,fun,args);
      return (TomNumber_MatchNumber) build(protoTomNumber_MatchNumber);
    }
  }

  public TomNumber_MatchNumber makeTomNumber_MatchNumber(TomNumber _number) {
    aterm.ATerm[] args = new aterm.ATerm[] {_number};
    return makeTomNumber_MatchNumber( funTomNumber_MatchNumber, args, empty);
  }

  public TomNumber TomNumber_MatchNumberFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomNumber_MatchNumber);

    if (children != null) {
      TomNumber tmp = makeTomNumber_MatchNumber(TomNumberFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomNumber_MatchNumberImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomNumber)arg.getArgument(0)).toTerm());
    return make(patternTomNumber_MatchNumber, args);
  }

  protected TomNumber_PatternNumber makeTomNumber_PatternNumber(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomNumber_PatternNumber) {
      protoTomNumber_PatternNumber.initHashCode(annos,fun,args);
      return (TomNumber_PatternNumber) build(protoTomNumber_PatternNumber);
    }
  }

  public TomNumber_PatternNumber makeTomNumber_PatternNumber(TomNumber _number) {
    aterm.ATerm[] args = new aterm.ATerm[] {_number};
    return makeTomNumber_PatternNumber( funTomNumber_PatternNumber, args, empty);
  }

  public TomNumber TomNumber_PatternNumberFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomNumber_PatternNumber);

    if (children != null) {
      TomNumber tmp = makeTomNumber_PatternNumber(TomNumberFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomNumber_PatternNumberImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomNumber)arg.getArgument(0)).toTerm());
    return make(patternTomNumber_PatternNumber, args);
  }

  protected TomNumber_ListNumber makeTomNumber_ListNumber(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomNumber_ListNumber) {
      protoTomNumber_ListNumber.initHashCode(annos,fun,args);
      return (TomNumber_ListNumber) build(protoTomNumber_ListNumber);
    }
  }

  public TomNumber_ListNumber makeTomNumber_ListNumber(TomNumber _number) {
    aterm.ATerm[] args = new aterm.ATerm[] {_number};
    return makeTomNumber_ListNumber( funTomNumber_ListNumber, args, empty);
  }

  public TomNumber TomNumber_ListNumberFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomNumber_ListNumber);

    if (children != null) {
      TomNumber tmp = makeTomNumber_ListNumber(TomNumberFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomNumber_ListNumberImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomNumber)arg.getArgument(0)).toTerm());
    return make(patternTomNumber_ListNumber, args);
  }

  protected TomNumber_IndexNumber makeTomNumber_IndexNumber(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomNumber_IndexNumber) {
      protoTomNumber_IndexNumber.initHashCode(annos,fun,args);
      return (TomNumber_IndexNumber) build(protoTomNumber_IndexNumber);
    }
  }

  public TomNumber_IndexNumber makeTomNumber_IndexNumber(TomNumber _number) {
    aterm.ATerm[] args = new aterm.ATerm[] {_number};
    return makeTomNumber_IndexNumber( funTomNumber_IndexNumber, args, empty);
  }

  public TomNumber TomNumber_IndexNumberFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomNumber_IndexNumber);

    if (children != null) {
      TomNumber tmp = makeTomNumber_IndexNumber(TomNumberFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomNumber_IndexNumberImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomNumber)arg.getArgument(0)).toTerm());
    return make(patternTomNumber_IndexNumber, args);
  }

  protected TomNumber_Begin makeTomNumber_Begin(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomNumber_Begin) {
      protoTomNumber_Begin.initHashCode(annos,fun,args);
      return (TomNumber_Begin) build(protoTomNumber_Begin);
    }
  }

  public TomNumber_Begin makeTomNumber_Begin(TomNumber _number) {
    aterm.ATerm[] args = new aterm.ATerm[] {_number};
    return makeTomNumber_Begin( funTomNumber_Begin, args, empty);
  }

  public TomNumber TomNumber_BeginFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomNumber_Begin);

    if (children != null) {
      TomNumber tmp = makeTomNumber_Begin(TomNumberFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomNumber_BeginImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomNumber)arg.getArgument(0)).toTerm());
    return make(patternTomNumber_Begin, args);
  }

  protected TomNumber_End makeTomNumber_End(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomNumber_End) {
      protoTomNumber_End.initHashCode(annos,fun,args);
      return (TomNumber_End) build(protoTomNumber_End);
    }
  }

  public TomNumber_End makeTomNumber_End(TomNumber _number) {
    aterm.ATerm[] args = new aterm.ATerm[] {_number};
    return makeTomNumber_End( funTomNumber_End, args, empty);
  }

  public TomNumber TomNumber_EndFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomNumber_End);

    if (children != null) {
      TomNumber tmp = makeTomNumber_End(TomNumberFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomNumber_EndImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomNumber)arg.getArgument(0)).toTerm());
    return make(patternTomNumber_End, args);
  }

  protected TomNumber_Number makeTomNumber_Number(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomNumber_Number) {
      protoTomNumber_Number.initHashCode(annos,fun,args);
      return (TomNumber_Number) build(protoTomNumber_Number);
    }
  }

  public TomNumber_Number makeTomNumber_Number(Integer _integer) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeInt(_integer.intValue())};
    return makeTomNumber_Number( funTomNumber_Number, args, empty);
  }

  public TomNumber TomNumber_NumberFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomNumber_Number);

    if (children != null) {
      TomNumber tmp = makeTomNumber_Number((Integer) children.get(0));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomNumber_NumberImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(new Integer(((aterm.ATermInt)arg.getArgument(0)).getInt()));
    return make(patternTomNumber_Number, args);
  }

  protected TomNumber_AbsVar makeTomNumber_AbsVar(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomNumber_AbsVar) {
      protoTomNumber_AbsVar.initHashCode(annos,fun,args);
      return (TomNumber_AbsVar) build(protoTomNumber_AbsVar);
    }
  }

  public TomNumber_AbsVar makeTomNumber_AbsVar(TomNumber _number) {
    aterm.ATerm[] args = new aterm.ATerm[] {_number};
    return makeTomNumber_AbsVar( funTomNumber_AbsVar, args, empty);
  }

  public TomNumber TomNumber_AbsVarFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomNumber_AbsVar);

    if (children != null) {
      TomNumber tmp = makeTomNumber_AbsVar(TomNumberFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomNumber_AbsVarImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomNumber)arg.getArgument(0)).toTerm());
    return make(patternTomNumber_AbsVar, args);
  }

  protected TomNumber_RenamedVar makeTomNumber_RenamedVar(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomNumber_RenamedVar) {
      protoTomNumber_RenamedVar.initHashCode(annos,fun,args);
      return (TomNumber_RenamedVar) build(protoTomNumber_RenamedVar);
    }
  }

  public TomNumber_RenamedVar makeTomNumber_RenamedVar(TomName _astName) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName};
    return makeTomNumber_RenamedVar( funTomNumber_RenamedVar, args, empty);
  }

  public TomNumber TomNumber_RenamedVarFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomNumber_RenamedVar);

    if (children != null) {
      TomNumber tmp = makeTomNumber_RenamedVar(TomNameFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomNumber_RenamedVarImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    return make(patternTomNumber_RenamedVar, args);
  }

  protected TomNumber_RuleVar makeTomNumber_RuleVar(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomNumber_RuleVar) {
      protoTomNumber_RuleVar.initHashCode(annos,fun,args);
      return (TomNumber_RuleVar) build(protoTomNumber_RuleVar);
    }
  }

  public TomNumber_RuleVar makeTomNumber_RuleVar() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeTomNumber_RuleVar( funTomNumber_RuleVar, args, empty);
  }

  public TomNumber TomNumber_RuleVarFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomNumber_RuleVar);

    if (children != null) {
      TomNumber tmp = makeTomNumber_RuleVar();
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomNumber_RuleVarImpl arg) {
    java.util.List args = new java.util.LinkedList();
    return make(patternTomNumber_RuleVar, args);
  }

  protected TomRule_RewriteRule makeTomRule_RewriteRule(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomRule_RewriteRule) {
      protoTomRule_RewriteRule.initHashCode(annos,fun,args);
      return (TomRule_RewriteRule) build(protoTomRule_RewriteRule);
    }
  }

  public TomRule_RewriteRule makeTomRule_RewriteRule(TomTerm _lhs, TomTerm _rhs, TomList _condList, Option _option) {
    aterm.ATerm[] args = new aterm.ATerm[] {_lhs, _rhs, _condList, _option};
    return makeTomRule_RewriteRule( funTomRule_RewriteRule, args, empty);
  }

  public TomRule TomRule_RewriteRuleFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomRule_RewriteRule);

    if (children != null) {
      TomRule tmp = makeTomRule_RewriteRule(TomTermFromTerm( (aterm.ATerm) children.get(0)), TomTermFromTerm( (aterm.ATerm) children.get(1)), TomListFromTerm( (aterm.ATerm) children.get(2)), OptionFromTerm( (aterm.ATerm) children.get(3)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomRule_RewriteRuleImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    args.add(((TomTerm)arg.getArgument(1)).toTerm());
    args.add(((TomList)arg.getArgument(2)).toTerm());
    args.add(((Option)arg.getArgument(3)).toTerm());
    return make(patternTomRule_RewriteRule, args);
  }

  protected Instruction_IfThenElse makeInstruction_IfThenElse(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoInstruction_IfThenElse) {
      protoInstruction_IfThenElse.initHashCode(annos,fun,args);
      return (Instruction_IfThenElse) build(protoInstruction_IfThenElse);
    }
  }

  public Instruction_IfThenElse makeInstruction_IfThenElse(Expression _condition, TomList _succesList, TomList _failureList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_condition, _succesList, _failureList};
    return makeInstruction_IfThenElse( funInstruction_IfThenElse, args, empty);
  }

  public Instruction Instruction_IfThenElseFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternInstruction_IfThenElse);

    if (children != null) {
      Instruction tmp = makeInstruction_IfThenElse(ExpressionFromTerm( (aterm.ATerm) children.get(0)), TomListFromTerm( (aterm.ATerm) children.get(1)), TomListFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Instruction_IfThenElseImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((Expression)arg.getArgument(0)).toTerm());
    args.add(((TomList)arg.getArgument(1)).toTerm());
    args.add(((TomList)arg.getArgument(2)).toTerm());
    return make(patternInstruction_IfThenElse, args);
  }

  protected Instruction_DoWhile makeInstruction_DoWhile(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoInstruction_DoWhile) {
      protoInstruction_DoWhile.initHashCode(annos,fun,args);
      return (Instruction_DoWhile) build(protoInstruction_DoWhile);
    }
  }

  public Instruction_DoWhile makeInstruction_DoWhile(TomList _instList, Expression _condition) {
    aterm.ATerm[] args = new aterm.ATerm[] {_instList, _condition};
    return makeInstruction_DoWhile( funInstruction_DoWhile, args, empty);
  }

  public Instruction Instruction_DoWhileFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternInstruction_DoWhile);

    if (children != null) {
      Instruction tmp = makeInstruction_DoWhile(TomListFromTerm( (aterm.ATerm) children.get(0)), ExpressionFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Instruction_DoWhileImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomList)arg.getArgument(0)).toTerm());
    args.add(((Expression)arg.getArgument(1)).toTerm());
    return make(patternInstruction_DoWhile, args);
  }

  protected Instruction_Assign makeInstruction_Assign(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoInstruction_Assign) {
      protoInstruction_Assign.initHashCode(annos,fun,args);
      return (Instruction_Assign) build(protoInstruction_Assign);
    }
  }

  public Instruction_Assign makeInstruction_Assign(TomTerm _kid1, Expression _source) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1, _source};
    return makeInstruction_Assign( funInstruction_Assign, args, empty);
  }

  public Instruction Instruction_AssignFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternInstruction_Assign);

    if (children != null) {
      Instruction tmp = makeInstruction_Assign(TomTermFromTerm( (aterm.ATerm) children.get(0)), ExpressionFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Instruction_AssignImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    args.add(((Expression)arg.getArgument(1)).toTerm());
    return make(patternInstruction_Assign, args);
  }

  protected Instruction_AssignMatchSubject makeInstruction_AssignMatchSubject(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoInstruction_AssignMatchSubject) {
      protoInstruction_AssignMatchSubject.initHashCode(annos,fun,args);
      return (Instruction_AssignMatchSubject) build(protoInstruction_AssignMatchSubject);
    }
  }

  public Instruction_AssignMatchSubject makeInstruction_AssignMatchSubject(TomTerm _kid1, Expression _source) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1, _source};
    return makeInstruction_AssignMatchSubject( funInstruction_AssignMatchSubject, args, empty);
  }

  public Instruction Instruction_AssignMatchSubjectFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternInstruction_AssignMatchSubject);

    if (children != null) {
      Instruction tmp = makeInstruction_AssignMatchSubject(TomTermFromTerm( (aterm.ATerm) children.get(0)), ExpressionFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Instruction_AssignMatchSubjectImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    args.add(((Expression)arg.getArgument(1)).toTerm());
    return make(patternInstruction_AssignMatchSubject, args);
  }

  protected Instruction_Increment makeInstruction_Increment(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoInstruction_Increment) {
      protoInstruction_Increment.initHashCode(annos,fun,args);
      return (Instruction_Increment) build(protoInstruction_Increment);
    }
  }

  public Instruction_Increment makeInstruction_Increment(TomTerm _kid1) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1};
    return makeInstruction_Increment( funInstruction_Increment, args, empty);
  }

  public Instruction Instruction_IncrementFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternInstruction_Increment);

    if (children != null) {
      Instruction tmp = makeInstruction_Increment(TomTermFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Instruction_IncrementImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    return make(patternInstruction_Increment, args);
  }

  protected Instruction_Action makeInstruction_Action(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoInstruction_Action) {
      protoInstruction_Action.initHashCode(annos,fun,args);
      return (Instruction_Action) build(protoInstruction_Action);
    }
  }

  public Instruction_Action makeInstruction_Action(TomList _instList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_instList};
    return makeInstruction_Action( funInstruction_Action, args, empty);
  }

  public Instruction Instruction_ActionFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternInstruction_Action);

    if (children != null) {
      Instruction tmp = makeInstruction_Action(TomListFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Instruction_ActionImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomList)arg.getArgument(0)).toTerm());
    return make(patternInstruction_Action, args);
  }

  protected Instruction_ExitAction makeInstruction_ExitAction(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoInstruction_ExitAction) {
      protoInstruction_ExitAction.initHashCode(annos,fun,args);
      return (Instruction_ExitAction) build(protoInstruction_ExitAction);
    }
  }

  public Instruction_ExitAction makeInstruction_ExitAction(TomNumberList _numberList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_numberList};
    return makeInstruction_ExitAction( funInstruction_ExitAction, args, empty);
  }

  public Instruction Instruction_ExitActionFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternInstruction_ExitAction);

    if (children != null) {
      Instruction tmp = makeInstruction_ExitAction(TomNumberListFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Instruction_ExitActionImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomNumberList)arg.getArgument(0)).toTerm());
    return make(patternInstruction_ExitAction, args);
  }

  protected Instruction_Return makeInstruction_Return(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoInstruction_Return) {
      protoInstruction_Return.initHashCode(annos,fun,args);
      return (Instruction_Return) build(protoInstruction_Return);
    }
  }

  public Instruction_Return makeInstruction_Return(TomTerm _kid1) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1};
    return makeInstruction_Return( funInstruction_Return, args, empty);
  }

  public Instruction Instruction_ReturnFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternInstruction_Return);

    if (children != null) {
      Instruction tmp = makeInstruction_Return(TomTermFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Instruction_ReturnImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomTerm)arg.getArgument(0)).toTerm());
    return make(patternInstruction_Return, args);
  }

  protected Instruction_OpenBlock makeInstruction_OpenBlock(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoInstruction_OpenBlock) {
      protoInstruction_OpenBlock.initHashCode(annos,fun,args);
      return (Instruction_OpenBlock) build(protoInstruction_OpenBlock);
    }
  }

  public Instruction_OpenBlock makeInstruction_OpenBlock() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeInstruction_OpenBlock( funInstruction_OpenBlock, args, empty);
  }

  public Instruction Instruction_OpenBlockFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternInstruction_OpenBlock);

    if (children != null) {
      Instruction tmp = makeInstruction_OpenBlock();
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Instruction_OpenBlockImpl arg) {
    java.util.List args = new java.util.LinkedList();
    return make(patternInstruction_OpenBlock, args);
  }

  protected Instruction_CloseBlock makeInstruction_CloseBlock(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoInstruction_CloseBlock) {
      protoInstruction_CloseBlock.initHashCode(annos,fun,args);
      return (Instruction_CloseBlock) build(protoInstruction_CloseBlock);
    }
  }

  public Instruction_CloseBlock makeInstruction_CloseBlock() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeInstruction_CloseBlock( funInstruction_CloseBlock, args, empty);
  }

  public Instruction Instruction_CloseBlockFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternInstruction_CloseBlock);

    if (children != null) {
      Instruction tmp = makeInstruction_CloseBlock();
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Instruction_CloseBlockImpl arg) {
    java.util.List args = new java.util.LinkedList();
    return make(patternInstruction_CloseBlock, args);
  }

  protected Instruction_NamedBlock makeInstruction_NamedBlock(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoInstruction_NamedBlock) {
      protoInstruction_NamedBlock.initHashCode(annos,fun,args);
      return (Instruction_NamedBlock) build(protoInstruction_NamedBlock);
    }
  }

  public Instruction_NamedBlock makeInstruction_NamedBlock(String _blockName, TomList _instList) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeAppl(makeAFun(_blockName, 0, true)), _instList};
    return makeInstruction_NamedBlock( funInstruction_NamedBlock, args, empty);
  }

  public Instruction Instruction_NamedBlockFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternInstruction_NamedBlock);

    if (children != null) {
      Instruction tmp = makeInstruction_NamedBlock((String) children.get(0), TomListFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(Instruction_NamedBlockImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((aterm.ATermAppl)arg.getArgument(0)).getAFun().getName());
    args.add(((TomList)arg.getArgument(1)).toTerm());
    return make(patternInstruction_NamedBlock, args);
  }

  protected TomSymbol_Symbol makeTomSymbol_Symbol(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomSymbol_Symbol) {
      protoTomSymbol_Symbol.initHashCode(annos,fun,args);
      return (TomSymbol_Symbol) build(protoTomSymbol_Symbol);
    }
  }

  public TomSymbol_Symbol makeTomSymbol_Symbol(TomName _astName, TomType _typesToType, SlotList _slotList, Option _option, TargetLanguage _tlCode) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _typesToType, _slotList, _option, _tlCode};
    return makeTomSymbol_Symbol( funTomSymbol_Symbol, args, empty);
  }

  public TomSymbol TomSymbol_SymbolFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomSymbol_Symbol);

    if (children != null) {
      TomSymbol tmp = makeTomSymbol_Symbol(TomNameFromTerm( (aterm.ATerm) children.get(0)), TomTypeFromTerm( (aterm.ATerm) children.get(1)), SlotListFromTerm( (aterm.ATerm) children.get(2)), OptionFromTerm( (aterm.ATerm) children.get(3)), TargetLanguageFromTerm( (aterm.ATerm) children.get(4)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomSymbol_SymbolImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((TomType)arg.getArgument(1)).toTerm());
    args.add(((SlotList)arg.getArgument(2)).toTerm());
    args.add(((Option)arg.getArgument(3)).toTerm());
    args.add(((TargetLanguage)arg.getArgument(4)).toTerm());
    return make(patternTomSymbol_Symbol, args);
  }

  protected PairNameDecl_Slot makePairNameDecl_Slot(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoPairNameDecl_Slot) {
      protoPairNameDecl_Slot.initHashCode(annos,fun,args);
      return (PairNameDecl_Slot) build(protoPairNameDecl_Slot);
    }
  }

  public PairNameDecl_Slot makePairNameDecl_Slot(TomName _slotName, Declaration _slotDecl) {
    aterm.ATerm[] args = new aterm.ATerm[] {_slotName, _slotDecl};
    return makePairNameDecl_Slot( funPairNameDecl_Slot, args, empty);
  }

  public PairNameDecl PairNameDecl_SlotFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternPairNameDecl_Slot);

    if (children != null) {
      PairNameDecl tmp = makePairNameDecl_Slot(TomNameFromTerm( (aterm.ATerm) children.get(0)), DeclarationFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(PairNameDecl_SlotImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((Declaration)arg.getArgument(1)).toTerm());
    return make(patternPairNameDecl_Slot, args);
  }

  protected TomSymbolTable_Table makeTomSymbolTable_Table(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomSymbolTable_Table) {
      protoTomSymbolTable_Table.initHashCode(annos,fun,args);
      return (TomSymbolTable_Table) build(protoTomSymbolTable_Table);
    }
  }

  public TomSymbolTable_Table makeTomSymbolTable_Table(TomEntryList _entryList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_entryList};
    return makeTomSymbolTable_Table( funTomSymbolTable_Table, args, empty);
  }

  public TomSymbolTable TomSymbolTable_TableFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomSymbolTable_Table);

    if (children != null) {
      TomSymbolTable tmp = makeTomSymbolTable_Table(TomEntryListFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomSymbolTable_TableImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomEntryList)arg.getArgument(0)).toTerm());
    return make(patternTomSymbolTable_Table, args);
  }

  protected TomEntry_Entry makeTomEntry_Entry(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomEntry_Entry) {
      protoTomEntry_Entry.initHashCode(annos,fun,args);
      return (TomEntry_Entry) build(protoTomEntry_Entry);
    }
  }

  public TomEntry_Entry makeTomEntry_Entry(String _strName, TomSymbol _astSymbol) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeAppl(makeAFun(_strName, 0, true)), _astSymbol};
    return makeTomEntry_Entry( funTomEntry_Entry, args, empty);
  }

  public TomEntry TomEntry_EntryFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomEntry_Entry);

    if (children != null) {
      TomEntry tmp = makeTomEntry_Entry((String) children.get(0), TomSymbolFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomEntry_EntryImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((aterm.ATermAppl)arg.getArgument(0)).getAFun().getName());
    args.add(((TomSymbol)arg.getArgument(1)).toTerm());
    return make(patternTomEntry_Entry, args);
  }

  protected TomStructureTable_StructTable makeTomStructureTable_StructTable(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomStructureTable_StructTable) {
      protoTomStructureTable_StructTable.initHashCode(annos,fun,args);
      return (TomStructureTable_StructTable) build(protoTomStructureTable_StructTable);
    }
  }

  public TomStructureTable_StructTable makeTomStructureTable_StructTable(TomList _structList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_structList};
    return makeTomStructureTable_StructTable( funTomStructureTable_StructTable, args, empty);
  }

  public TomStructureTable TomStructureTable_StructTableFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTomStructureTable_StructTable);

    if (children != null) {
      TomStructureTable tmp = makeTomStructureTable_StructTable(TomListFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TomStructureTable_StructTableImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomList)arg.getArgument(0)).toTerm());
    return make(patternTomStructureTable_StructTable, args);
  }

  protected XMLTerm_Element makeXMLTerm_Element(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoXMLTerm_Element) {
      protoXMLTerm_Element.initHashCode(annos,fun,args);
      return (XMLTerm_Element) build(protoXMLTerm_Element);
    }
  }

  public XMLTerm_Element makeXMLTerm_Element(TomName _astName, TomList _args, Option _option) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _args, _option};
    return makeXMLTerm_Element( funXMLTerm_Element, args, empty);
  }

  public XMLTerm XMLTerm_ElementFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternXMLTerm_Element);

    if (children != null) {
      XMLTerm tmp = makeXMLTerm_Element(TomNameFromTerm( (aterm.ATerm) children.get(0)), TomListFromTerm( (aterm.ATerm) children.get(1)), OptionFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(XMLTerm_ElementImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((TomList)arg.getArgument(1)).toTerm());
    args.add(((Option)arg.getArgument(2)).toTerm());
    return make(patternXMLTerm_Element, args);
  }

  protected XMLTerm_Attribute makeXMLTerm_Attribute(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoXMLTerm_Attribute) {
      protoXMLTerm_Attribute.initHashCode(annos,fun,args);
      return (XMLTerm_Attribute) build(protoXMLTerm_Attribute);
    }
  }

  public XMLTerm_Attribute makeXMLTerm_Attribute(TomName _astName, TomList _args, Option _option) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _args, _option};
    return makeXMLTerm_Attribute( funXMLTerm_Attribute, args, empty);
  }

  public XMLTerm XMLTerm_AttributeFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternXMLTerm_Attribute);

    if (children != null) {
      XMLTerm tmp = makeXMLTerm_Attribute(TomNameFromTerm( (aterm.ATerm) children.get(0)), TomListFromTerm( (aterm.ATerm) children.get(1)), OptionFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(XMLTerm_AttributeImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((TomList)arg.getArgument(1)).toTerm());
    args.add(((Option)arg.getArgument(2)).toTerm());
    return make(patternXMLTerm_Attribute, args);
  }

  protected XMLTerm_ReservedWord makeXMLTerm_ReservedWord(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoXMLTerm_ReservedWord) {
      protoXMLTerm_ReservedWord.initHashCode(annos,fun,args);
      return (XMLTerm_ReservedWord) build(protoXMLTerm_ReservedWord);
    }
  }

  public XMLTerm_ReservedWord makeXMLTerm_ReservedWord(TomName _astName, TomList _args, Option _option) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _args, _option};
    return makeXMLTerm_ReservedWord( funXMLTerm_ReservedWord, args, empty);
  }

  public XMLTerm XMLTerm_ReservedWordFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternXMLTerm_ReservedWord);

    if (children != null) {
      XMLTerm tmp = makeXMLTerm_ReservedWord(TomNameFromTerm( (aterm.ATerm) children.get(0)), TomListFromTerm( (aterm.ATerm) children.get(1)), OptionFromTerm( (aterm.ATerm) children.get(2)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(XMLTerm_ReservedWordImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((TomList)arg.getArgument(1)).toTerm());
    args.add(((Option)arg.getArgument(2)).toTerm());
    return make(patternXMLTerm_ReservedWord, args);
  }

  protected XMLTerm_XMLPlaceholder makeXMLTerm_XMLPlaceholder(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoXMLTerm_XMLPlaceholder) {
      protoXMLTerm_XMLPlaceholder.initHashCode(annos,fun,args);
      return (XMLTerm_XMLPlaceholder) build(protoXMLTerm_XMLPlaceholder);
    }
  }

  public XMLTerm_XMLPlaceholder makeXMLTerm_XMLPlaceholder(Option _option) {
    aterm.ATerm[] args = new aterm.ATerm[] {_option};
    return makeXMLTerm_XMLPlaceholder( funXMLTerm_XMLPlaceholder, args, empty);
  }

  public XMLTerm XMLTerm_XMLPlaceholderFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternXMLTerm_XMLPlaceholder);

    if (children != null) {
      XMLTerm tmp = makeXMLTerm_XMLPlaceholder(OptionFromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(XMLTerm_XMLPlaceholderImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((Option)arg.getArgument(0)).toTerm());
    return make(patternXMLTerm_XMLPlaceholder, args);
  }

  protected XMLTerm_XMLVariable makeXMLTerm_XMLVariable(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoXMLTerm_XMLVariable) {
      protoXMLTerm_XMLVariable.initHashCode(annos,fun,args);
      return (XMLTerm_XMLVariable) build(protoXMLTerm_XMLVariable);
    }
  }

  public XMLTerm_XMLVariable makeXMLTerm_XMLVariable(TomName _astName, Option _option) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _option};
    return makeXMLTerm_XMLVariable( funXMLTerm_XMLVariable, args, empty);
  }

  public XMLTerm XMLTerm_XMLVariableFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternXMLTerm_XMLVariable);

    if (children != null) {
      XMLTerm tmp = makeXMLTerm_XMLVariable(TomNameFromTerm( (aterm.ATerm) children.get(0)), OptionFromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(XMLTerm_XMLVariableImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((TomName)arg.getArgument(0)).toTerm());
    args.add(((Option)arg.getArgument(1)).toTerm());
    return make(patternXMLTerm_XMLVariable, args);
  }

  public TomList makeTomList() {
    return emptyTomList;
  }
  public TomList makeTomList(TomTerm elem ) {
    return (TomList) makeTomList(elem, emptyTomList);
  }
  public TomList makeTomList(TomTerm head, TomList tail) {
    return (TomList) makeTomList((aterm.ATerm) head, (aterm.ATermList) tail, empty);
  }
  protected TomList makeTomList(aterm.ATerm head, aterm.ATermList tail, aterm.ATermList annos) {
    synchronized (protoTomList) {
      protoTomList.initHashCode(annos,head,tail);
      return (TomList) build(protoTomList);
    }
  }
  public TomNumberList makeTomNumberList() {
    return emptyTomNumberList;
  }
  public TomNumberList makeTomNumberList(TomNumber elem ) {
    return (TomNumberList) makeTomNumberList(elem, emptyTomNumberList);
  }
  public TomNumberList makeTomNumberList(TomNumber head, TomNumberList tail) {
    return (TomNumberList) makeTomNumberList((aterm.ATerm) head, (aterm.ATermList) tail, empty);
  }
  protected TomNumberList makeTomNumberList(aterm.ATerm head, aterm.ATermList tail, aterm.ATermList annos) {
    synchronized (protoTomNumberList) {
      protoTomNumberList.initHashCode(annos,head,tail);
      return (TomNumberList) build(protoTomNumberList);
    }
  }
  public TomRuleList makeTomRuleList() {
    return emptyTomRuleList;
  }
  public TomRuleList makeTomRuleList(TomRule elem ) {
    return (TomRuleList) makeTomRuleList(elem, emptyTomRuleList);
  }
  public TomRuleList makeTomRuleList(TomRule head, TomRuleList tail) {
    return (TomRuleList) makeTomRuleList((aterm.ATerm) head, (aterm.ATermList) tail, empty);
  }
  protected TomRuleList makeTomRuleList(aterm.ATerm head, aterm.ATermList tail, aterm.ATermList annos) {
    synchronized (protoTomRuleList) {
      protoTomRuleList.initHashCode(annos,head,tail);
      return (TomRuleList) build(protoTomRuleList);
    }
  }
  public TomTypeList makeTomTypeList() {
    return emptyTomTypeList;
  }
  public TomTypeList makeTomTypeList(TomType elem ) {
    return (TomTypeList) makeTomTypeList(elem, emptyTomTypeList);
  }
  public TomTypeList makeTomTypeList(TomType head, TomTypeList tail) {
    return (TomTypeList) makeTomTypeList((aterm.ATerm) head, (aterm.ATermList) tail, empty);
  }
  protected TomTypeList makeTomTypeList(aterm.ATerm head, aterm.ATermList tail, aterm.ATermList annos) {
    synchronized (protoTomTypeList) {
      protoTomTypeList.initHashCode(annos,head,tail);
      return (TomTypeList) build(protoTomTypeList);
    }
  }
  public OptionList makeOptionList() {
    return emptyOptionList;
  }
  public OptionList makeOptionList(Option elem ) {
    return (OptionList) makeOptionList(elem, emptyOptionList);
  }
  public OptionList makeOptionList(Option head, OptionList tail) {
    return (OptionList) makeOptionList((aterm.ATerm) head, (aterm.ATermList) tail, empty);
  }
  protected OptionList makeOptionList(aterm.ATerm head, aterm.ATermList tail, aterm.ATermList annos) {
    synchronized (protoOptionList) {
      protoOptionList.initHashCode(annos,head,tail);
      return (OptionList) build(protoOptionList);
    }
  }
  public SlotList makeSlotList() {
    return emptySlotList;
  }
  public SlotList makeSlotList(PairNameDecl elem ) {
    return (SlotList) makeSlotList(elem, emptySlotList);
  }
  public SlotList makeSlotList(PairNameDecl head, SlotList tail) {
    return (SlotList) makeSlotList((aterm.ATerm) head, (aterm.ATermList) tail, empty);
  }
  protected SlotList makeSlotList(aterm.ATerm head, aterm.ATermList tail, aterm.ATermList annos) {
    synchronized (protoSlotList) {
      protoSlotList.initHashCode(annos,head,tail);
      return (SlotList) build(protoSlotList);
    }
  }
  public TomEntryList makeTomEntryList() {
    return emptyTomEntryList;
  }
  public TomEntryList makeTomEntryList(TomEntry elem ) {
    return (TomEntryList) makeTomEntryList(elem, emptyTomEntryList);
  }
  public TomEntryList makeTomEntryList(TomEntry head, TomEntryList tail) {
    return (TomEntryList) makeTomEntryList((aterm.ATerm) head, (aterm.ATermList) tail, empty);
  }
  protected TomEntryList makeTomEntryList(aterm.ATerm head, aterm.ATermList tail, aterm.ATermList annos) {
    synchronized (protoTomEntryList) {
      protoTomEntryList.initHashCode(annos,head,tail);
      return (TomEntryList) build(protoTomEntryList);
    }
  }
  public Declaration DeclarationFromTerm(aterm.ATerm trm)
  {
    Declaration tmp;
    tmp = Declaration_TypeTermDeclFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Declaration_TypeListDeclFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Declaration_TypeArrayDeclFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Declaration_GetFunctionSymbolDeclFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Declaration_GetSubtermDeclFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Declaration_IsFsymDeclFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Declaration_GetSlotDeclFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Declaration_CompareFunctionSymbolDeclFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Declaration_TermsEqualDeclFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Declaration_GetHeadDeclFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Declaration_GetTailDeclFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Declaration_IsEmptyDeclFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Declaration_MakeEmptyListFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Declaration_MakeAddListFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Declaration_GetElementDeclFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Declaration_GetSizeDeclFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Declaration_MakeEmptyArrayFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Declaration_MakeAddArrayFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Declaration_MakeDeclFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Declaration_SymbolDeclFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Declaration_ListSymbolDeclFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Declaration_ArraySymbolDeclFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Declaration_EmptyDeclarationFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a Declaration: " + trm);
  }
  public Option OptionFromTerm(aterm.ATerm trm)
  {
    Option tmp;
    tmp = Option_DeclarationToOptionFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Option_TomNameToOptionFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Option_TomTermToOptionFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Option_OptionFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Option_DefinedSymbolFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Option_GeneratedMatchFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Option_WithDefaultProductionFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Option_OriginTrackingFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Option_ConstructorFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Option_OriginalTextFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Option_XMLPositionFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a Option: " + trm);
  }
  public Expression ExpressionFromTerm(aterm.ATerm trm)
  {
    Expression tmp;
    tmp = Expression_TomTermToExpressionFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Expression_NotFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Expression_AndFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Expression_TrueTLFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Expression_FalseTLFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Expression_IsEmptyListFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Expression_IsEmptyArrayFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Expression_EqualFunctionSymbolFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Expression_EqualTermFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Expression_GetSubtermFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Expression_IsFsymFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Expression_GetSlotFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Expression_GetHeadFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Expression_GetTailFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Expression_GetSizeFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Expression_GetElementFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Expression_GetSliceListFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Expression_GetSliceArrayFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a Expression: " + trm);
  }
  public TargetLanguage TargetLanguageFromTerm(aterm.ATerm trm)
  {
    TargetLanguage tmp;
    tmp = TargetLanguage_TLFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TargetLanguage_ITLFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a TargetLanguage: " + trm);
  }
  public Position PositionFromTerm(aterm.ATerm trm)
  {
    Position tmp;
    tmp = Position_PositionFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a Position: " + trm);
  }
  public TomType TomTypeFromTerm(aterm.ATerm trm)
  {
    TomType tmp;
    tmp = TomType_TypeFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomType_TypesToTypeFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomType_TomTypeFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomType_TomTypeAloneFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomType_TLTypeFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomType_EmptyTypeFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a TomType: " + trm);
  }
  public TomName TomNameFromTerm(aterm.ATerm trm)
  {
    TomName tmp;
    tmp = TomName_NameFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomName_PositionNameFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomName_EmptyNameFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a TomName: " + trm);
  }
  public TomTerm TomTermFromTerm(aterm.ATerm trm)
  {
    TomTerm tmp;
    tmp = TomTerm_TargetLanguageToTomTermFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_TomTypeToTomTermFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_TomNameToTomTermFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_TomSymbolToTomTermFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_DeclarationToTomTermFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_OptionToTomTermFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_ExpressionToTomTermFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_InstructionToTomTermFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_TomRuleToTomTermFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_TomFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_TomIncludeFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_MakeTermFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_BackQuoteTermFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_FunctionCallFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_MakeFunctionBeginFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_MakeFunctionEndFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_ApplFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_RecordApplFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_PairSlotApplFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_MatchFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_MatchingConditionFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_EqualityConditionFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_RuleSetFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_SubjectListFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_PatternListFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_TermListFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_TermFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_PatternActionFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_DefaultPatternActionFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_TLVarFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_DeclarationFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_VariableFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_VariableStarFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_PlaceholderFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_UnamedVariableFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_DotTermFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_LocalVariableFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_EndLocalVariableFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_BuildVariableFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_BuildTermFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_BuildListFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_BuildArrayFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_BuildBuiltinFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_CompiledMatchFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_CompiledPatternFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_AssignedVariableFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_AutomataFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_DefaultAutomataFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_MatchXMLFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_BackQuoteXMLFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomTerm_XMLTermToTomTermFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a TomTerm: " + trm);
  }
  public TomNumber TomNumberFromTerm(aterm.ATerm trm)
  {
    TomNumber tmp;
    tmp = TomNumber_MatchNumberFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomNumber_PatternNumberFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomNumber_ListNumberFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomNumber_IndexNumberFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomNumber_BeginFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomNumber_EndFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomNumber_NumberFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomNumber_AbsVarFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomNumber_RenamedVarFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TomNumber_RuleVarFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a TomNumber: " + trm);
  }
  public TomRule TomRuleFromTerm(aterm.ATerm trm)
  {
    TomRule tmp;
    tmp = TomRule_RewriteRuleFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a TomRule: " + trm);
  }
  public Instruction InstructionFromTerm(aterm.ATerm trm)
  {
    Instruction tmp;
    tmp = Instruction_IfThenElseFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Instruction_DoWhileFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Instruction_AssignFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Instruction_AssignMatchSubjectFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Instruction_IncrementFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Instruction_ActionFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Instruction_ExitActionFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Instruction_ReturnFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Instruction_OpenBlockFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Instruction_CloseBlockFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Instruction_NamedBlockFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a Instruction: " + trm);
  }
  public TomSymbol TomSymbolFromTerm(aterm.ATerm trm)
  {
    TomSymbol tmp;
    tmp = TomSymbol_SymbolFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a TomSymbol: " + trm);
  }
  public PairNameDecl PairNameDeclFromTerm(aterm.ATerm trm)
  {
    PairNameDecl tmp;
    tmp = PairNameDecl_SlotFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a PairNameDecl: " + trm);
  }
  public TomSymbolTable TomSymbolTableFromTerm(aterm.ATerm trm)
  {
    TomSymbolTable tmp;
    tmp = TomSymbolTable_TableFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a TomSymbolTable: " + trm);
  }
  public TomEntry TomEntryFromTerm(aterm.ATerm trm)
  {
    TomEntry tmp;
    tmp = TomEntry_EntryFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a TomEntry: " + trm);
  }
  public TomStructureTable TomStructureTableFromTerm(aterm.ATerm trm)
  {
    TomStructureTable tmp;
    tmp = TomStructureTable_StructTableFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a TomStructureTable: " + trm);
  }
  public XMLTerm XMLTermFromTerm(aterm.ATerm trm)
  {
    XMLTerm tmp;
    tmp = XMLTerm_ElementFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = XMLTerm_AttributeFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = XMLTerm_ReservedWordFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = XMLTerm_XMLPlaceholderFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = XMLTerm_XMLVariableFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a XMLTerm: " + trm);
  }
  public TomList TomListFromTerm(aterm.ATerm trm)
  {
     if (trm instanceof aterm.ATermList) {
        aterm.ATermList list = ((aterm.ATermList) trm).reverse();
        TomList result = makeTomList();
        for (; !list.isEmpty(); list = list.getNext()) {
          TomTerm elem = TomTermFromTerm(list.getFirst());
           if (elem != null) {
             result = makeTomList(elem, result);
           }
           else {
             throw new RuntimeException("Invalid element in TomList: " + elem);
           }
        }
        return result;
     }
     else {
       throw new RuntimeException("This is not a TomList: " + trm);
     }
  }
  public TomNumberList TomNumberListFromTerm(aterm.ATerm trm)
  {
     if (trm instanceof aterm.ATermList) {
        aterm.ATermList list = ((aterm.ATermList) trm).reverse();
        TomNumberList result = makeTomNumberList();
        for (; !list.isEmpty(); list = list.getNext()) {
          TomNumber elem = TomNumberFromTerm(list.getFirst());
           if (elem != null) {
             result = makeTomNumberList(elem, result);
           }
           else {
             throw new RuntimeException("Invalid element in TomNumberList: " + elem);
           }
        }
        return result;
     }
     else {
       throw new RuntimeException("This is not a TomNumberList: " + trm);
     }
  }
  public TomRuleList TomRuleListFromTerm(aterm.ATerm trm)
  {
     if (trm instanceof aterm.ATermList) {
        aterm.ATermList list = ((aterm.ATermList) trm).reverse();
        TomRuleList result = makeTomRuleList();
        for (; !list.isEmpty(); list = list.getNext()) {
          TomRule elem = TomRuleFromTerm(list.getFirst());
           if (elem != null) {
             result = makeTomRuleList(elem, result);
           }
           else {
             throw new RuntimeException("Invalid element in TomRuleList: " + elem);
           }
        }
        return result;
     }
     else {
       throw new RuntimeException("This is not a TomRuleList: " + trm);
     }
  }
  public TomTypeList TomTypeListFromTerm(aterm.ATerm trm)
  {
     if (trm instanceof aterm.ATermList) {
        aterm.ATermList list = ((aterm.ATermList) trm).reverse();
        TomTypeList result = makeTomTypeList();
        for (; !list.isEmpty(); list = list.getNext()) {
          TomType elem = TomTypeFromTerm(list.getFirst());
           if (elem != null) {
             result = makeTomTypeList(elem, result);
           }
           else {
             throw new RuntimeException("Invalid element in TomTypeList: " + elem);
           }
        }
        return result;
     }
     else {
       throw new RuntimeException("This is not a TomTypeList: " + trm);
     }
  }
  public OptionList OptionListFromTerm(aterm.ATerm trm)
  {
     if (trm instanceof aterm.ATermList) {
        aterm.ATermList list = ((aterm.ATermList) trm).reverse();
        OptionList result = makeOptionList();
        for (; !list.isEmpty(); list = list.getNext()) {
          Option elem = OptionFromTerm(list.getFirst());
           if (elem != null) {
             result = makeOptionList(elem, result);
           }
           else {
             throw new RuntimeException("Invalid element in OptionList: " + elem);
           }
        }
        return result;
     }
     else {
       throw new RuntimeException("This is not a OptionList: " + trm);
     }
  }
  public SlotList SlotListFromTerm(aterm.ATerm trm)
  {
     if (trm instanceof aterm.ATermList) {
        aterm.ATermList list = ((aterm.ATermList) trm).reverse();
        SlotList result = makeSlotList();
        for (; !list.isEmpty(); list = list.getNext()) {
          PairNameDecl elem = PairNameDeclFromTerm(list.getFirst());
           if (elem != null) {
             result = makeSlotList(elem, result);
           }
           else {
             throw new RuntimeException("Invalid element in SlotList: " + elem);
           }
        }
        return result;
     }
     else {
       throw new RuntimeException("This is not a SlotList: " + trm);
     }
  }
  public TomEntryList TomEntryListFromTerm(aterm.ATerm trm)
  {
     if (trm instanceof aterm.ATermList) {
        aterm.ATermList list = ((aterm.ATermList) trm).reverse();
        TomEntryList result = makeTomEntryList();
        for (; !list.isEmpty(); list = list.getNext()) {
          TomEntry elem = TomEntryFromTerm(list.getFirst());
           if (elem != null) {
             result = makeTomEntryList(elem, result);
           }
           else {
             throw new RuntimeException("Invalid element in TomEntryList: " + elem);
           }
        }
        return result;
     }
     else {
       throw new RuntimeException("This is not a TomEntryList: " + trm);
     }
  }
  public Declaration DeclarationFromString(String str)
  {
    aterm.ATerm trm = parse(str);
    return DeclarationFromTerm(trm);
  }
  public Declaration DeclarationFromFile(java.io.InputStream stream) throws java.io.IOException {
    return DeclarationFromTerm(readFromFile(stream));
  }
  public Option OptionFromString(String str)
  {
    aterm.ATerm trm = parse(str);
    return OptionFromTerm(trm);
  }
  public Option OptionFromFile(java.io.InputStream stream) throws java.io.IOException {
    return OptionFromTerm(readFromFile(stream));
  }
  public Expression ExpressionFromString(String str)
  {
    aterm.ATerm trm = parse(str);
    return ExpressionFromTerm(trm);
  }
  public Expression ExpressionFromFile(java.io.InputStream stream) throws java.io.IOException {
    return ExpressionFromTerm(readFromFile(stream));
  }
  public TargetLanguage TargetLanguageFromString(String str)
  {
    aterm.ATerm trm = parse(str);
    return TargetLanguageFromTerm(trm);
  }
  public TargetLanguage TargetLanguageFromFile(java.io.InputStream stream) throws java.io.IOException {
    return TargetLanguageFromTerm(readFromFile(stream));
  }
  public Position PositionFromString(String str)
  {
    aterm.ATerm trm = parse(str);
    return PositionFromTerm(trm);
  }
  public Position PositionFromFile(java.io.InputStream stream) throws java.io.IOException {
    return PositionFromTerm(readFromFile(stream));
  }
  public TomType TomTypeFromString(String str)
  {
    aterm.ATerm trm = parse(str);
    return TomTypeFromTerm(trm);
  }
  public TomType TomTypeFromFile(java.io.InputStream stream) throws java.io.IOException {
    return TomTypeFromTerm(readFromFile(stream));
  }
  public TomName TomNameFromString(String str)
  {
    aterm.ATerm trm = parse(str);
    return TomNameFromTerm(trm);
  }
  public TomName TomNameFromFile(java.io.InputStream stream) throws java.io.IOException {
    return TomNameFromTerm(readFromFile(stream));
  }
  public TomTerm TomTermFromString(String str)
  {
    aterm.ATerm trm = parse(str);
    return TomTermFromTerm(trm);
  }
  public TomTerm TomTermFromFile(java.io.InputStream stream) throws java.io.IOException {
    return TomTermFromTerm(readFromFile(stream));
  }
  public TomNumber TomNumberFromString(String str)
  {
    aterm.ATerm trm = parse(str);
    return TomNumberFromTerm(trm);
  }
  public TomNumber TomNumberFromFile(java.io.InputStream stream) throws java.io.IOException {
    return TomNumberFromTerm(readFromFile(stream));
  }
  public TomRule TomRuleFromString(String str)
  {
    aterm.ATerm trm = parse(str);
    return TomRuleFromTerm(trm);
  }
  public TomRule TomRuleFromFile(java.io.InputStream stream) throws java.io.IOException {
    return TomRuleFromTerm(readFromFile(stream));
  }
  public Instruction InstructionFromString(String str)
  {
    aterm.ATerm trm = parse(str);
    return InstructionFromTerm(trm);
  }
  public Instruction InstructionFromFile(java.io.InputStream stream) throws java.io.IOException {
    return InstructionFromTerm(readFromFile(stream));
  }
  public TomSymbol TomSymbolFromString(String str)
  {
    aterm.ATerm trm = parse(str);
    return TomSymbolFromTerm(trm);
  }
  public TomSymbol TomSymbolFromFile(java.io.InputStream stream) throws java.io.IOException {
    return TomSymbolFromTerm(readFromFile(stream));
  }
  public PairNameDecl PairNameDeclFromString(String str)
  {
    aterm.ATerm trm = parse(str);
    return PairNameDeclFromTerm(trm);
  }
  public PairNameDecl PairNameDeclFromFile(java.io.InputStream stream) throws java.io.IOException {
    return PairNameDeclFromTerm(readFromFile(stream));
  }
  public TomSymbolTable TomSymbolTableFromString(String str)
  {
    aterm.ATerm trm = parse(str);
    return TomSymbolTableFromTerm(trm);
  }
  public TomSymbolTable TomSymbolTableFromFile(java.io.InputStream stream) throws java.io.IOException {
    return TomSymbolTableFromTerm(readFromFile(stream));
  }
  public TomEntry TomEntryFromString(String str)
  {
    aterm.ATerm trm = parse(str);
    return TomEntryFromTerm(trm);
  }
  public TomEntry TomEntryFromFile(java.io.InputStream stream) throws java.io.IOException {
    return TomEntryFromTerm(readFromFile(stream));
  }
  public TomStructureTable TomStructureTableFromString(String str)
  {
    aterm.ATerm trm = parse(str);
    return TomStructureTableFromTerm(trm);
  }
  public TomStructureTable TomStructureTableFromFile(java.io.InputStream stream) throws java.io.IOException {
    return TomStructureTableFromTerm(readFromFile(stream));
  }
  public XMLTerm XMLTermFromString(String str)
  {
    aterm.ATerm trm = parse(str);
    return XMLTermFromTerm(trm);
  }
  public XMLTerm XMLTermFromFile(java.io.InputStream stream) throws java.io.IOException {
    return XMLTermFromTerm(readFromFile(stream));
  }
  public TomList TomListFromString(String str)
  {
    aterm.ATerm trm = parse(str);
    return TomListFromTerm(trm);
  }
  public TomList TomListFromFile(java.io.InputStream stream) throws java.io.IOException {
    return TomListFromTerm(readFromFile(stream));
  }
  public TomNumberList TomNumberListFromString(String str)
  {
    aterm.ATerm trm = parse(str);
    return TomNumberListFromTerm(trm);
  }
  public TomNumberList TomNumberListFromFile(java.io.InputStream stream) throws java.io.IOException {
    return TomNumberListFromTerm(readFromFile(stream));
  }
  public TomRuleList TomRuleListFromString(String str)
  {
    aterm.ATerm trm = parse(str);
    return TomRuleListFromTerm(trm);
  }
  public TomRuleList TomRuleListFromFile(java.io.InputStream stream) throws java.io.IOException {
    return TomRuleListFromTerm(readFromFile(stream));
  }
  public TomTypeList TomTypeListFromString(String str)
  {
    aterm.ATerm trm = parse(str);
    return TomTypeListFromTerm(trm);
  }
  public TomTypeList TomTypeListFromFile(java.io.InputStream stream) throws java.io.IOException {
    return TomTypeListFromTerm(readFromFile(stream));
  }
  public OptionList OptionListFromString(String str)
  {
    aterm.ATerm trm = parse(str);
    return OptionListFromTerm(trm);
  }
  public OptionList OptionListFromFile(java.io.InputStream stream) throws java.io.IOException {
    return OptionListFromTerm(readFromFile(stream));
  }
  public SlotList SlotListFromString(String str)
  {
    aterm.ATerm trm = parse(str);
    return SlotListFromTerm(trm);
  }
  public SlotList SlotListFromFile(java.io.InputStream stream) throws java.io.IOException {
    return SlotListFromTerm(readFromFile(stream));
  }
  public TomEntryList TomEntryListFromString(String str)
  {
    aterm.ATerm trm = parse(str);
    return TomEntryListFromTerm(trm);
  }
  public TomEntryList TomEntryListFromFile(java.io.InputStream stream) throws java.io.IOException {
    return TomEntryListFromTerm(readFromFile(stream));
  }
}

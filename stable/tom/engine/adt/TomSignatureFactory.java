package jtom.adt;

import aterm.*;
import aterm.pure.PureFactory;
public class TomSignatureFactory extends PureFactory
{
  private aterm.AFun funDeclaration_TypeTermDecl;
  private Declaration protoDeclaration_TypeTermDecl;
  private aterm.AFun funDeclaration_TypeListDecl;
  private Declaration protoDeclaration_TypeListDecl;
  private aterm.AFun funDeclaration_TypeArrayDecl;
  private Declaration protoDeclaration_TypeArrayDecl;
  private aterm.AFun funDeclaration_GetFunctionSymbolDecl;
  private Declaration protoDeclaration_GetFunctionSymbolDecl;
  private aterm.AFun funDeclaration_GetSubtermDecl;
  private Declaration protoDeclaration_GetSubtermDecl;
  private aterm.AFun funDeclaration_IsFsymDecl;
  private Declaration protoDeclaration_IsFsymDecl;
  private aterm.AFun funDeclaration_GetSlotDecl;
  private Declaration protoDeclaration_GetSlotDecl;
  private aterm.AFun funDeclaration_CompareFunctionSymbolDecl;
  private Declaration protoDeclaration_CompareFunctionSymbolDecl;
  private aterm.AFun funDeclaration_TermsEqualDecl;
  private Declaration protoDeclaration_TermsEqualDecl;
  private aterm.AFun funDeclaration_GetHeadDecl;
  private Declaration protoDeclaration_GetHeadDecl;
  private aterm.AFun funDeclaration_GetTailDecl;
  private Declaration protoDeclaration_GetTailDecl;
  private aterm.AFun funDeclaration_IsEmptyDecl;
  private Declaration protoDeclaration_IsEmptyDecl;
  private aterm.AFun funDeclaration_MakeEmptyList;
  private Declaration protoDeclaration_MakeEmptyList;
  private aterm.AFun funDeclaration_MakeAddList;
  private Declaration protoDeclaration_MakeAddList;
  private aterm.AFun funDeclaration_GetElementDecl;
  private Declaration protoDeclaration_GetElementDecl;
  private aterm.AFun funDeclaration_GetSizeDecl;
  private Declaration protoDeclaration_GetSizeDecl;
  private aterm.AFun funDeclaration_MakeEmptyArray;
  private Declaration protoDeclaration_MakeEmptyArray;
  private aterm.AFun funDeclaration_MakeAddArray;
  private Declaration protoDeclaration_MakeAddArray;
  private aterm.AFun funDeclaration_MakeDecl;
  private Declaration protoDeclaration_MakeDecl;
  private aterm.AFun funDeclaration_SymbolDecl;
  private Declaration protoDeclaration_SymbolDecl;
  private aterm.AFun funDeclaration_ListSymbolDecl;
  private Declaration protoDeclaration_ListSymbolDecl;
  private aterm.AFun funDeclaration_ArraySymbolDecl;
  private Declaration protoDeclaration_ArraySymbolDecl;
  private aterm.AFun funDeclaration_EmptyDeclaration;
  private Declaration protoDeclaration_EmptyDeclaration;
  private aterm.AFun funOptionList_EmptyOptionList;
  private OptionList protoOptionList_EmptyOptionList;
  private aterm.AFun funOptionList_ConsOptionList;
  private OptionList protoOptionList_ConsOptionList;
  private aterm.AFun funOption_DeclarationToOption;
  private Option protoOption_DeclarationToOption;
  private aterm.AFun funOption_TomNameToOption;
  private Option protoOption_TomNameToOption;
  private aterm.AFun funOption_TomTermToOption;
  private Option protoOption_TomTermToOption;
  private aterm.AFun funOption_Option;
  private Option protoOption_Option;
  private aterm.AFun funOption_DefinedSymbol;
  private Option protoOption_DefinedSymbol;
  private aterm.AFun funOption_GeneratedMatch;
  private Option protoOption_GeneratedMatch;
  private aterm.AFun funOption_OriginTracking;
  private Option protoOption_OriginTracking;
  private aterm.AFun funOption_Constructor;
  private Option protoOption_Constructor;
  private aterm.AFun funOption_OriginalText;
  private Option protoOption_OriginalText;
  private aterm.AFun funExpression_TomTermToExpression;
  private Expression protoExpression_TomTermToExpression;
  private aterm.AFun funExpression_Not;
  private Expression protoExpression_Not;
  private aterm.AFun funExpression_And;
  private Expression protoExpression_And;
  private aterm.AFun funExpression_TrueTL;
  private Expression protoExpression_TrueTL;
  private aterm.AFun funExpression_FalseTL;
  private Expression protoExpression_FalseTL;
  private aterm.AFun funExpression_IsEmptyList;
  private Expression protoExpression_IsEmptyList;
  private aterm.AFun funExpression_IsEmptyArray;
  private Expression protoExpression_IsEmptyArray;
  private aterm.AFun funExpression_EqualFunctionSymbol;
  private Expression protoExpression_EqualFunctionSymbol;
  private aterm.AFun funExpression_EqualTerm;
  private Expression protoExpression_EqualTerm;
  private aterm.AFun funExpression_GetSubterm;
  private Expression protoExpression_GetSubterm;
  private aterm.AFun funExpression_IsFsym;
  private Expression protoExpression_IsFsym;
  private aterm.AFun funExpression_GetSlot;
  private Expression protoExpression_GetSlot;
  private aterm.AFun funExpression_GetHead;
  private Expression protoExpression_GetHead;
  private aterm.AFun funExpression_GetTail;
  private Expression protoExpression_GetTail;
  private aterm.AFun funExpression_GetSize;
  private Expression protoExpression_GetSize;
  private aterm.AFun funExpression_GetElement;
  private Expression protoExpression_GetElement;
  private aterm.AFun funExpression_GetSliceList;
  private Expression protoExpression_GetSliceList;
  private aterm.AFun funExpression_GetSliceArray;
  private Expression protoExpression_GetSliceArray;
  private aterm.AFun funTargetLanguage_TL;
  private TargetLanguage protoTargetLanguage_TL;
  private aterm.AFun funTargetLanguage_ITL;
  private TargetLanguage protoTargetLanguage_ITL;
  private aterm.AFun funPosition_Position;
  private Position protoPosition_Position;
  private aterm.AFun funTomType_Type;
  private TomType protoTomType_Type;
  private aterm.AFun funTomType_TypesToType;
  private TomType protoTomType_TypesToType;
  private aterm.AFun funTomType_TomType;
  private TomType protoTomType_TomType;
  private aterm.AFun funTomType_TomTypeAlone;
  private TomType protoTomType_TomTypeAlone;
  private aterm.AFun funTomType_TLType;
  private TomType protoTomType_TLType;
  private aterm.AFun funTomType_EmptyType;
  private TomType protoTomType_EmptyType;
  private aterm.AFun funTomName_Name;
  private TomName protoTomName_Name;
  private aterm.AFun funTomName_PositionName;
  private TomName protoTomName_PositionName;
  private aterm.AFun funTomName_EmptyName;
  private TomName protoTomName_EmptyName;
  private aterm.AFun funTomList_Empty;
  private TomList protoTomList_Empty;
  private aterm.AFun funTomList_Cons;
  private TomList protoTomList_Cons;
  private aterm.AFun funTomTerm_TargetLanguageToTomTerm;
  private TomTerm protoTomTerm_TargetLanguageToTomTerm;
  private aterm.AFun funTomTerm_TomTypeToTomTerm;
  private TomTerm protoTomTerm_TomTypeToTomTerm;
  private aterm.AFun funTomTerm_TomNameToTomTerm;
  private TomTerm protoTomTerm_TomNameToTomTerm;
  private aterm.AFun funTomTerm_TomSymbolToTomTerm;
  private TomTerm protoTomTerm_TomSymbolToTomTerm;
  private aterm.AFun funTomTerm_DeclarationToTomTerm;
  private TomTerm protoTomTerm_DeclarationToTomTerm;
  private aterm.AFun funTomTerm_OptionToTomTerm;
  private TomTerm protoTomTerm_OptionToTomTerm;
  private aterm.AFun funTomTerm_ExpressionToTomTerm;
  private TomTerm protoTomTerm_ExpressionToTomTerm;
  private aterm.AFun funTomTerm_InstructionToTomTerm;
  private TomTerm protoTomTerm_InstructionToTomTerm;
  private aterm.AFun funTomTerm_Tom;
  private TomTerm protoTomTerm_Tom;
  private aterm.AFun funTomTerm_TomInclude;
  private TomTerm protoTomTerm_TomInclude;
  private aterm.AFun funTomTerm_MakeTerm;
  private TomTerm protoTomTerm_MakeTerm;
  private aterm.AFun funTomTerm_BackQuoteTerm;
  private TomTerm protoTomTerm_BackQuoteTerm;
  private aterm.AFun funTomTerm_FunctionCall;
  private TomTerm protoTomTerm_FunctionCall;
  private aterm.AFun funTomTerm_MakeFunctionBegin;
  private TomTerm protoTomTerm_MakeFunctionBegin;
  private aterm.AFun funTomTerm_MakeFunctionEnd;
  private TomTerm protoTomTerm_MakeFunctionEnd;
  private aterm.AFun funTomTerm_Appl;
  private TomTerm protoTomTerm_Appl;
  private aterm.AFun funTomTerm_RecordAppl;
  private TomTerm protoTomTerm_RecordAppl;
  private aterm.AFun funTomTerm_PairSlotAppl;
  private TomTerm protoTomTerm_PairSlotAppl;
  private aterm.AFun funTomTerm_Match;
  private TomTerm protoTomTerm_Match;
  private aterm.AFun funTomTerm_MatchingCondition;
  private TomTerm protoTomTerm_MatchingCondition;
  private aterm.AFun funTomTerm_EqualityCondition;
  private TomTerm protoTomTerm_EqualityCondition;
  private aterm.AFun funTomTerm_RuleSet;
  private TomTerm protoTomTerm_RuleSet;
  private aterm.AFun funTomTerm_RewriteRule;
  private TomTerm protoTomTerm_RewriteRule;
  private aterm.AFun funTomTerm_SubjectList;
  private TomTerm protoTomTerm_SubjectList;
  private aterm.AFun funTomTerm_PatternList;
  private TomTerm protoTomTerm_PatternList;
  private aterm.AFun funTomTerm_TermList;
  private TomTerm protoTomTerm_TermList;
  private aterm.AFun funTomTerm_Term;
  private TomTerm protoTomTerm_Term;
  private aterm.AFun funTomTerm_PatternAction;
  private TomTerm protoTomTerm_PatternAction;
  private aterm.AFun funTomTerm_TLVar;
  private TomTerm protoTomTerm_TLVar;
  private aterm.AFun funTomTerm_Declaration;
  private TomTerm protoTomTerm_Declaration;
  private aterm.AFun funTomTerm_Variable;
  private TomTerm protoTomTerm_Variable;
  private aterm.AFun funTomTerm_VariableStar;
  private TomTerm protoTomTerm_VariableStar;
  private aterm.AFun funTomTerm_Placeholder;
  private TomTerm protoTomTerm_Placeholder;
  private aterm.AFun funTomTerm_UnamedVariable;
  private TomTerm protoTomTerm_UnamedVariable;
  private aterm.AFun funTomTerm_DotTerm;
  private TomTerm protoTomTerm_DotTerm;
  private aterm.AFun funTomTerm_LocalVariable;
  private TomTerm protoTomTerm_LocalVariable;
  private aterm.AFun funTomTerm_EndLocalVariable;
  private TomTerm protoTomTerm_EndLocalVariable;
  private aterm.AFun funTomTerm_BuildVariable;
  private TomTerm protoTomTerm_BuildVariable;
  private aterm.AFun funTomTerm_BuildTerm;
  private TomTerm protoTomTerm_BuildTerm;
  private aterm.AFun funTomTerm_BuildList;
  private TomTerm protoTomTerm_BuildList;
  private aterm.AFun funTomTerm_BuildArray;
  private TomTerm protoTomTerm_BuildArray;
  private aterm.AFun funTomTerm_BuildBuiltin;
  private TomTerm protoTomTerm_BuildBuiltin;
  private aterm.AFun funTomTerm_CompiledMatch;
  private TomTerm protoTomTerm_CompiledMatch;
  private aterm.AFun funTomTerm_CompiledPattern;
  private TomTerm protoTomTerm_CompiledPattern;
  private aterm.AFun funTomTerm_AssignedVariable;
  private TomTerm protoTomTerm_AssignedVariable;
  private aterm.AFun funTomTerm_Automata;
  private TomTerm protoTomTerm_Automata;
  private aterm.AFun funTomTerm_MatchNumber;
  private TomTerm protoTomTerm_MatchNumber;
  private aterm.AFun funTomTerm_PatternNumber;
  private TomTerm protoTomTerm_PatternNumber;
  private aterm.AFun funTomTerm_ListNumber;
  private TomTerm protoTomTerm_ListNumber;
  private aterm.AFun funTomTerm_IndexNumber;
  private TomTerm protoTomTerm_IndexNumber;
  private aterm.AFun funTomTerm_AbsVar;
  private TomTerm protoTomTerm_AbsVar;
  private aterm.AFun funTomTerm_RenamedVar;
  private TomTerm protoTomTerm_RenamedVar;
  private aterm.AFun funTomTerm_RuleVar;
  private TomTerm protoTomTerm_RuleVar;
  private aterm.AFun funTomTerm_Number;
  private TomTerm protoTomTerm_Number;
  private aterm.AFun funTomTerm_Begin;
  private TomTerm protoTomTerm_Begin;
  private aterm.AFun funTomTerm_End;
  private TomTerm protoTomTerm_End;
  private aterm.AFun funInstruction_IfThenElse;
  private Instruction protoInstruction_IfThenElse;
  private aterm.AFun funInstruction_DoWhile;
  private Instruction protoInstruction_DoWhile;
  private aterm.AFun funInstruction_Assign;
  private Instruction protoInstruction_Assign;
  private aterm.AFun funInstruction_AssignMatchSubject;
  private Instruction protoInstruction_AssignMatchSubject;
  private aterm.AFun funInstruction_Increment;
  private Instruction protoInstruction_Increment;
  private aterm.AFun funInstruction_Action;
  private Instruction protoInstruction_Action;
  private aterm.AFun funInstruction_ExitAction;
  private Instruction protoInstruction_ExitAction;
  private aterm.AFun funInstruction_Return;
  private Instruction protoInstruction_Return;
  private aterm.AFun funInstruction_OpenBlock;
  private Instruction protoInstruction_OpenBlock;
  private aterm.AFun funInstruction_CloseBlock;
  private Instruction protoInstruction_CloseBlock;
  private aterm.AFun funInstruction_NamedBlock;
  private Instruction protoInstruction_NamedBlock;
  private aterm.AFun funTomSymbol_Symbol;
  private TomSymbol protoTomSymbol_Symbol;
  private aterm.AFun funSlotList_EmptySlotList;
  private SlotList protoSlotList_EmptySlotList;
  private aterm.AFun funSlotList_ConsSlotList;
  private SlotList protoSlotList_ConsSlotList;
  private aterm.AFun funPairNameDecl_Slot;
  private PairNameDecl protoPairNameDecl_Slot;
  private aterm.AFun funTomSymbolTable_Table;
  private TomSymbolTable protoTomSymbolTable_Table;
  private aterm.AFun funTomEntryList_EmptyEntryList;
  private TomEntryList protoTomEntryList_EmptyEntryList;
  private aterm.AFun funTomEntryList_ConsEntryList;
  private TomEntryList protoTomEntryList_ConsEntryList;
  private aterm.AFun funTomEntry_Entry;
  private TomEntry protoTomEntry_Entry;
  private aterm.AFun funTomStructureTable_StructTable;
  private TomStructureTable protoTomStructureTable_StructTable;
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
    Declaration.initialize(this);

    Declaration_TypeTermDecl.initializePattern();
    funDeclaration_TypeTermDecl = makeAFun("_Declaration_TypeTermDecl", 3, false);
    protoDeclaration_TypeTermDecl = new Declaration_TypeTermDecl();

    Declaration_TypeListDecl.initializePattern();
    funDeclaration_TypeListDecl = makeAFun("_Declaration_TypeListDecl", 3, false);
    protoDeclaration_TypeListDecl = new Declaration_TypeListDecl();

    Declaration_TypeArrayDecl.initializePattern();
    funDeclaration_TypeArrayDecl = makeAFun("_Declaration_TypeArrayDecl", 3, false);
    protoDeclaration_TypeArrayDecl = new Declaration_TypeArrayDecl();

    Declaration_GetFunctionSymbolDecl.initializePattern();
    funDeclaration_GetFunctionSymbolDecl = makeAFun("_Declaration_GetFunctionSymbolDecl", 3, false);
    protoDeclaration_GetFunctionSymbolDecl = new Declaration_GetFunctionSymbolDecl();

    Declaration_GetSubtermDecl.initializePattern();
    funDeclaration_GetSubtermDecl = makeAFun("_Declaration_GetSubtermDecl", 4, false);
    protoDeclaration_GetSubtermDecl = new Declaration_GetSubtermDecl();

    Declaration_IsFsymDecl.initializePattern();
    funDeclaration_IsFsymDecl = makeAFun("_Declaration_IsFsymDecl", 4, false);
    protoDeclaration_IsFsymDecl = new Declaration_IsFsymDecl();

    Declaration_GetSlotDecl.initializePattern();
    funDeclaration_GetSlotDecl = makeAFun("_Declaration_GetSlotDecl", 5, false);
    protoDeclaration_GetSlotDecl = new Declaration_GetSlotDecl();

    Declaration_CompareFunctionSymbolDecl.initializePattern();
    funDeclaration_CompareFunctionSymbolDecl = makeAFun("_Declaration_CompareFunctionSymbolDecl", 4, false);
    protoDeclaration_CompareFunctionSymbolDecl = new Declaration_CompareFunctionSymbolDecl();

    Declaration_TermsEqualDecl.initializePattern();
    funDeclaration_TermsEqualDecl = makeAFun("_Declaration_TermsEqualDecl", 4, false);
    protoDeclaration_TermsEqualDecl = new Declaration_TermsEqualDecl();

    Declaration_GetHeadDecl.initializePattern();
    funDeclaration_GetHeadDecl = makeAFun("_Declaration_GetHeadDecl", 3, false);
    protoDeclaration_GetHeadDecl = new Declaration_GetHeadDecl();

    Declaration_GetTailDecl.initializePattern();
    funDeclaration_GetTailDecl = makeAFun("_Declaration_GetTailDecl", 3, false);
    protoDeclaration_GetTailDecl = new Declaration_GetTailDecl();

    Declaration_IsEmptyDecl.initializePattern();
    funDeclaration_IsEmptyDecl = makeAFun("_Declaration_IsEmptyDecl", 3, false);
    protoDeclaration_IsEmptyDecl = new Declaration_IsEmptyDecl();

    Declaration_MakeEmptyList.initializePattern();
    funDeclaration_MakeEmptyList = makeAFun("_Declaration_MakeEmptyList", 3, false);
    protoDeclaration_MakeEmptyList = new Declaration_MakeEmptyList();

    Declaration_MakeAddList.initializePattern();
    funDeclaration_MakeAddList = makeAFun("_Declaration_MakeAddList", 5, false);
    protoDeclaration_MakeAddList = new Declaration_MakeAddList();

    Declaration_GetElementDecl.initializePattern();
    funDeclaration_GetElementDecl = makeAFun("_Declaration_GetElementDecl", 4, false);
    protoDeclaration_GetElementDecl = new Declaration_GetElementDecl();

    Declaration_GetSizeDecl.initializePattern();
    funDeclaration_GetSizeDecl = makeAFun("_Declaration_GetSizeDecl", 3, false);
    protoDeclaration_GetSizeDecl = new Declaration_GetSizeDecl();

    Declaration_MakeEmptyArray.initializePattern();
    funDeclaration_MakeEmptyArray = makeAFun("_Declaration_MakeEmptyArray", 4, false);
    protoDeclaration_MakeEmptyArray = new Declaration_MakeEmptyArray();

    Declaration_MakeAddArray.initializePattern();
    funDeclaration_MakeAddArray = makeAFun("_Declaration_MakeAddArray", 5, false);
    protoDeclaration_MakeAddArray = new Declaration_MakeAddArray();

    Declaration_MakeDecl.initializePattern();
    funDeclaration_MakeDecl = makeAFun("_Declaration_MakeDecl", 5, false);
    protoDeclaration_MakeDecl = new Declaration_MakeDecl();

    Declaration_SymbolDecl.initializePattern();
    funDeclaration_SymbolDecl = makeAFun("_Declaration_SymbolDecl", 1, false);
    protoDeclaration_SymbolDecl = new Declaration_SymbolDecl();

    Declaration_ListSymbolDecl.initializePattern();
    funDeclaration_ListSymbolDecl = makeAFun("_Declaration_ListSymbolDecl", 1, false);
    protoDeclaration_ListSymbolDecl = new Declaration_ListSymbolDecl();

    Declaration_ArraySymbolDecl.initializePattern();
    funDeclaration_ArraySymbolDecl = makeAFun("_Declaration_ArraySymbolDecl", 1, false);
    protoDeclaration_ArraySymbolDecl = new Declaration_ArraySymbolDecl();

    Declaration_EmptyDeclaration.initializePattern();
    funDeclaration_EmptyDeclaration = makeAFun("_Declaration_EmptyDeclaration", 0, false);
    protoDeclaration_EmptyDeclaration = new Declaration_EmptyDeclaration();

    OptionList.initialize(this);

    OptionList_EmptyOptionList.initializePattern();
    funOptionList_EmptyOptionList = makeAFun("_OptionList_EmptyOptionList", 0, false);
    protoOptionList_EmptyOptionList = new OptionList_EmptyOptionList();

    OptionList_ConsOptionList.initializePattern();
    funOptionList_ConsOptionList = makeAFun("_OptionList_ConsOptionList", 2, false);
    protoOptionList_ConsOptionList = new OptionList_ConsOptionList();

    Option.initialize(this);

    Option_DeclarationToOption.initializePattern();
    funOption_DeclarationToOption = makeAFun("_Option_DeclarationToOption", 1, false);
    protoOption_DeclarationToOption = new Option_DeclarationToOption();

    Option_TomNameToOption.initializePattern();
    funOption_TomNameToOption = makeAFun("_Option_TomNameToOption", 1, false);
    protoOption_TomNameToOption = new Option_TomNameToOption();

    Option_TomTermToOption.initializePattern();
    funOption_TomTermToOption = makeAFun("_Option_TomTermToOption", 1, false);
    protoOption_TomTermToOption = new Option_TomTermToOption();

    Option_Option.initializePattern();
    funOption_Option = makeAFun("_Option_Option", 1, false);
    protoOption_Option = new Option_Option();

    Option_DefinedSymbol.initializePattern();
    funOption_DefinedSymbol = makeAFun("_Option_DefinedSymbol", 0, false);
    protoOption_DefinedSymbol = new Option_DefinedSymbol();

    Option_GeneratedMatch.initializePattern();
    funOption_GeneratedMatch = makeAFun("_Option_GeneratedMatch", 0, false);
    protoOption_GeneratedMatch = new Option_GeneratedMatch();

    Option_OriginTracking.initializePattern();
    funOption_OriginTracking = makeAFun("_Option_OriginTracking", 3, false);
    protoOption_OriginTracking = new Option_OriginTracking();

    Option_Constructor.initializePattern();
    funOption_Constructor = makeAFun("_Option_Constructor", 1, false);
    protoOption_Constructor = new Option_Constructor();

    Option_OriginalText.initializePattern();
    funOption_OriginalText = makeAFun("_Option_OriginalText", 1, false);
    protoOption_OriginalText = new Option_OriginalText();

    Expression.initialize(this);

    Expression_TomTermToExpression.initializePattern();
    funExpression_TomTermToExpression = makeAFun("_Expression_TomTermToExpression", 1, false);
    protoExpression_TomTermToExpression = new Expression_TomTermToExpression();

    Expression_Not.initializePattern();
    funExpression_Not = makeAFun("_Expression_Not", 1, false);
    protoExpression_Not = new Expression_Not();

    Expression_And.initializePattern();
    funExpression_And = makeAFun("_Expression_And", 2, false);
    protoExpression_And = new Expression_And();

    Expression_TrueTL.initializePattern();
    funExpression_TrueTL = makeAFun("_Expression_TrueTL", 0, false);
    protoExpression_TrueTL = new Expression_TrueTL();

    Expression_FalseTL.initializePattern();
    funExpression_FalseTL = makeAFun("_Expression_FalseTL", 0, false);
    protoExpression_FalseTL = new Expression_FalseTL();

    Expression_IsEmptyList.initializePattern();
    funExpression_IsEmptyList = makeAFun("_Expression_IsEmptyList", 1, false);
    protoExpression_IsEmptyList = new Expression_IsEmptyList();

    Expression_IsEmptyArray.initializePattern();
    funExpression_IsEmptyArray = makeAFun("_Expression_IsEmptyArray", 2, false);
    protoExpression_IsEmptyArray = new Expression_IsEmptyArray();

    Expression_EqualFunctionSymbol.initializePattern();
    funExpression_EqualFunctionSymbol = makeAFun("_Expression_EqualFunctionSymbol", 2, false);
    protoExpression_EqualFunctionSymbol = new Expression_EqualFunctionSymbol();

    Expression_EqualTerm.initializePattern();
    funExpression_EqualTerm = makeAFun("_Expression_EqualTerm", 2, false);
    protoExpression_EqualTerm = new Expression_EqualTerm();

    Expression_GetSubterm.initializePattern();
    funExpression_GetSubterm = makeAFun("_Expression_GetSubterm", 2, false);
    protoExpression_GetSubterm = new Expression_GetSubterm();

    Expression_IsFsym.initializePattern();
    funExpression_IsFsym = makeAFun("_Expression_IsFsym", 2, false);
    protoExpression_IsFsym = new Expression_IsFsym();

    Expression_GetSlot.initializePattern();
    funExpression_GetSlot = makeAFun("_Expression_GetSlot", 3, false);
    protoExpression_GetSlot = new Expression_GetSlot();

    Expression_GetHead.initializePattern();
    funExpression_GetHead = makeAFun("_Expression_GetHead", 1, false);
    protoExpression_GetHead = new Expression_GetHead();

    Expression_GetTail.initializePattern();
    funExpression_GetTail = makeAFun("_Expression_GetTail", 1, false);
    protoExpression_GetTail = new Expression_GetTail();

    Expression_GetSize.initializePattern();
    funExpression_GetSize = makeAFun("_Expression_GetSize", 1, false);
    protoExpression_GetSize = new Expression_GetSize();

    Expression_GetElement.initializePattern();
    funExpression_GetElement = makeAFun("_Expression_GetElement", 2, false);
    protoExpression_GetElement = new Expression_GetElement();

    Expression_GetSliceList.initializePattern();
    funExpression_GetSliceList = makeAFun("_Expression_GetSliceList", 3, false);
    protoExpression_GetSliceList = new Expression_GetSliceList();

    Expression_GetSliceArray.initializePattern();
    funExpression_GetSliceArray = makeAFun("_Expression_GetSliceArray", 4, false);
    protoExpression_GetSliceArray = new Expression_GetSliceArray();

    TargetLanguage.initialize(this);

    TargetLanguage_TL.initializePattern();
    funTargetLanguage_TL = makeAFun("_TargetLanguage_TL", 3, false);
    protoTargetLanguage_TL = new TargetLanguage_TL();

    TargetLanguage_ITL.initializePattern();
    funTargetLanguage_ITL = makeAFun("_TargetLanguage_ITL", 1, false);
    protoTargetLanguage_ITL = new TargetLanguage_ITL();

    Position.initialize(this);

    Position_Position.initializePattern();
    funPosition_Position = makeAFun("_Position_Position", 2, false);
    protoPosition_Position = new Position_Position();

    TomType.initialize(this);

    TomType_Type.initializePattern();
    funTomType_Type = makeAFun("_TomType_Type", 2, false);
    protoTomType_Type = new TomType_Type();

    TomType_TypesToType.initializePattern();
    funTomType_TypesToType = makeAFun("_TomType_TypesToType", 2, false);
    protoTomType_TypesToType = new TomType_TypesToType();

    TomType_TomType.initializePattern();
    funTomType_TomType = makeAFun("_TomType_TomType", 1, false);
    protoTomType_TomType = new TomType_TomType();

    TomType_TomTypeAlone.initializePattern();
    funTomType_TomTypeAlone = makeAFun("_TomType_TomTypeAlone", 1, false);
    protoTomType_TomTypeAlone = new TomType_TomTypeAlone();

    TomType_TLType.initializePattern();
    funTomType_TLType = makeAFun("_TomType_TLType", 1, false);
    protoTomType_TLType = new TomType_TLType();

    TomType_EmptyType.initializePattern();
    funTomType_EmptyType = makeAFun("_TomType_EmptyType", 0, false);
    protoTomType_EmptyType = new TomType_EmptyType();

    TomName.initialize(this);

    TomName_Name.initializePattern();
    funTomName_Name = makeAFun("_TomName_Name", 1, false);
    protoTomName_Name = new TomName_Name();

    TomName_PositionName.initializePattern();
    funTomName_PositionName = makeAFun("_TomName_PositionName", 1, false);
    protoTomName_PositionName = new TomName_PositionName();

    TomName_EmptyName.initializePattern();
    funTomName_EmptyName = makeAFun("_TomName_EmptyName", 0, false);
    protoTomName_EmptyName = new TomName_EmptyName();

    TomList.initialize(this);

    TomList_Empty.initializePattern();
    funTomList_Empty = makeAFun("_TomList_Empty", 0, false);
    protoTomList_Empty = new TomList_Empty();

    TomList_Cons.initializePattern();
    funTomList_Cons = makeAFun("_TomList_Cons", 2, false);
    protoTomList_Cons = new TomList_Cons();

    TomTerm.initialize(this);

    TomTerm_TargetLanguageToTomTerm.initializePattern();
    funTomTerm_TargetLanguageToTomTerm = makeAFun("_TomTerm_TargetLanguageToTomTerm", 1, false);
    protoTomTerm_TargetLanguageToTomTerm = new TomTerm_TargetLanguageToTomTerm();

    TomTerm_TomTypeToTomTerm.initializePattern();
    funTomTerm_TomTypeToTomTerm = makeAFun("_TomTerm_TomTypeToTomTerm", 1, false);
    protoTomTerm_TomTypeToTomTerm = new TomTerm_TomTypeToTomTerm();

    TomTerm_TomNameToTomTerm.initializePattern();
    funTomTerm_TomNameToTomTerm = makeAFun("_TomTerm_TomNameToTomTerm", 1, false);
    protoTomTerm_TomNameToTomTerm = new TomTerm_TomNameToTomTerm();

    TomTerm_TomSymbolToTomTerm.initializePattern();
    funTomTerm_TomSymbolToTomTerm = makeAFun("_TomTerm_TomSymbolToTomTerm", 1, false);
    protoTomTerm_TomSymbolToTomTerm = new TomTerm_TomSymbolToTomTerm();

    TomTerm_DeclarationToTomTerm.initializePattern();
    funTomTerm_DeclarationToTomTerm = makeAFun("_TomTerm_DeclarationToTomTerm", 1, false);
    protoTomTerm_DeclarationToTomTerm = new TomTerm_DeclarationToTomTerm();

    TomTerm_OptionToTomTerm.initializePattern();
    funTomTerm_OptionToTomTerm = makeAFun("_TomTerm_OptionToTomTerm", 1, false);
    protoTomTerm_OptionToTomTerm = new TomTerm_OptionToTomTerm();

    TomTerm_ExpressionToTomTerm.initializePattern();
    funTomTerm_ExpressionToTomTerm = makeAFun("_TomTerm_ExpressionToTomTerm", 1, false);
    protoTomTerm_ExpressionToTomTerm = new TomTerm_ExpressionToTomTerm();

    TomTerm_InstructionToTomTerm.initializePattern();
    funTomTerm_InstructionToTomTerm = makeAFun("_TomTerm_InstructionToTomTerm", 1, false);
    protoTomTerm_InstructionToTomTerm = new TomTerm_InstructionToTomTerm();

    TomTerm_Tom.initializePattern();
    funTomTerm_Tom = makeAFun("_TomTerm_Tom", 1, false);
    protoTomTerm_Tom = new TomTerm_Tom();

    TomTerm_TomInclude.initializePattern();
    funTomTerm_TomInclude = makeAFun("_TomTerm_TomInclude", 1, false);
    protoTomTerm_TomInclude = new TomTerm_TomInclude();

    TomTerm_MakeTerm.initializePattern();
    funTomTerm_MakeTerm = makeAFun("_TomTerm_MakeTerm", 1, false);
    protoTomTerm_MakeTerm = new TomTerm_MakeTerm();

    TomTerm_BackQuoteTerm.initializePattern();
    funTomTerm_BackQuoteTerm = makeAFun("_TomTerm_BackQuoteTerm", 2, false);
    protoTomTerm_BackQuoteTerm = new TomTerm_BackQuoteTerm();

    TomTerm_FunctionCall.initializePattern();
    funTomTerm_FunctionCall = makeAFun("_TomTerm_FunctionCall", 2, false);
    protoTomTerm_FunctionCall = new TomTerm_FunctionCall();

    TomTerm_MakeFunctionBegin.initializePattern();
    funTomTerm_MakeFunctionBegin = makeAFun("_TomTerm_MakeFunctionBegin", 2, false);
    protoTomTerm_MakeFunctionBegin = new TomTerm_MakeFunctionBegin();

    TomTerm_MakeFunctionEnd.initializePattern();
    funTomTerm_MakeFunctionEnd = makeAFun("_TomTerm_MakeFunctionEnd", 0, false);
    protoTomTerm_MakeFunctionEnd = new TomTerm_MakeFunctionEnd();

    TomTerm_Appl.initializePattern();
    funTomTerm_Appl = makeAFun("_TomTerm_Appl", 3, false);
    protoTomTerm_Appl = new TomTerm_Appl();

    TomTerm_RecordAppl.initializePattern();
    funTomTerm_RecordAppl = makeAFun("_TomTerm_RecordAppl", 3, false);
    protoTomTerm_RecordAppl = new TomTerm_RecordAppl();

    TomTerm_PairSlotAppl.initializePattern();
    funTomTerm_PairSlotAppl = makeAFun("_TomTerm_PairSlotAppl", 2, false);
    protoTomTerm_PairSlotAppl = new TomTerm_PairSlotAppl();

    TomTerm_Match.initializePattern();
    funTomTerm_Match = makeAFun("_TomTerm_Match", 3, false);
    protoTomTerm_Match = new TomTerm_Match();

    TomTerm_MatchingCondition.initializePattern();
    funTomTerm_MatchingCondition = makeAFun("_TomTerm_MatchingCondition", 2, false);
    protoTomTerm_MatchingCondition = new TomTerm_MatchingCondition();

    TomTerm_EqualityCondition.initializePattern();
    funTomTerm_EqualityCondition = makeAFun("_TomTerm_EqualityCondition", 2, false);
    protoTomTerm_EqualityCondition = new TomTerm_EqualityCondition();

    TomTerm_RuleSet.initializePattern();
    funTomTerm_RuleSet = makeAFun("_TomTerm_RuleSet", 2, false);
    protoTomTerm_RuleSet = new TomTerm_RuleSet();

    TomTerm_RewriteRule.initializePattern();
    funTomTerm_RewriteRule = makeAFun("_TomTerm_RewriteRule", 4, false);
    protoTomTerm_RewriteRule = new TomTerm_RewriteRule();

    TomTerm_SubjectList.initializePattern();
    funTomTerm_SubjectList = makeAFun("_TomTerm_SubjectList", 1, false);
    protoTomTerm_SubjectList = new TomTerm_SubjectList();

    TomTerm_PatternList.initializePattern();
    funTomTerm_PatternList = makeAFun("_TomTerm_PatternList", 1, false);
    protoTomTerm_PatternList = new TomTerm_PatternList();

    TomTerm_TermList.initializePattern();
    funTomTerm_TermList = makeAFun("_TomTerm_TermList", 1, false);
    protoTomTerm_TermList = new TomTerm_TermList();

    TomTerm_Term.initializePattern();
    funTomTerm_Term = makeAFun("_TomTerm_Term", 1, false);
    protoTomTerm_Term = new TomTerm_Term();

    TomTerm_PatternAction.initializePattern();
    funTomTerm_PatternAction = makeAFun("_TomTerm_PatternAction", 3, false);
    protoTomTerm_PatternAction = new TomTerm_PatternAction();

    TomTerm_TLVar.initializePattern();
    funTomTerm_TLVar = makeAFun("_TomTerm_TLVar", 2, false);
    protoTomTerm_TLVar = new TomTerm_TLVar();

    TomTerm_Declaration.initializePattern();
    funTomTerm_Declaration = makeAFun("_TomTerm_Declaration", 1, false);
    protoTomTerm_Declaration = new TomTerm_Declaration();

    TomTerm_Variable.initializePattern();
    funTomTerm_Variable = makeAFun("_TomTerm_Variable", 3, false);
    protoTomTerm_Variable = new TomTerm_Variable();

    TomTerm_VariableStar.initializePattern();
    funTomTerm_VariableStar = makeAFun("_TomTerm_VariableStar", 3, false);
    protoTomTerm_VariableStar = new TomTerm_VariableStar();

    TomTerm_Placeholder.initializePattern();
    funTomTerm_Placeholder = makeAFun("_TomTerm_Placeholder", 1, false);
    protoTomTerm_Placeholder = new TomTerm_Placeholder();

    TomTerm_UnamedVariable.initializePattern();
    funTomTerm_UnamedVariable = makeAFun("_TomTerm_UnamedVariable", 2, false);
    protoTomTerm_UnamedVariable = new TomTerm_UnamedVariable();

    TomTerm_DotTerm.initializePattern();
    funTomTerm_DotTerm = makeAFun("_TomTerm_DotTerm", 2, false);
    protoTomTerm_DotTerm = new TomTerm_DotTerm();

    TomTerm_LocalVariable.initializePattern();
    funTomTerm_LocalVariable = makeAFun("_TomTerm_LocalVariable", 0, false);
    protoTomTerm_LocalVariable = new TomTerm_LocalVariable();

    TomTerm_EndLocalVariable.initializePattern();
    funTomTerm_EndLocalVariable = makeAFun("_TomTerm_EndLocalVariable", 0, false);
    protoTomTerm_EndLocalVariable = new TomTerm_EndLocalVariable();

    TomTerm_BuildVariable.initializePattern();
    funTomTerm_BuildVariable = makeAFun("_TomTerm_BuildVariable", 1, false);
    protoTomTerm_BuildVariable = new TomTerm_BuildVariable();

    TomTerm_BuildTerm.initializePattern();
    funTomTerm_BuildTerm = makeAFun("_TomTerm_BuildTerm", 2, false);
    protoTomTerm_BuildTerm = new TomTerm_BuildTerm();

    TomTerm_BuildList.initializePattern();
    funTomTerm_BuildList = makeAFun("_TomTerm_BuildList", 2, false);
    protoTomTerm_BuildList = new TomTerm_BuildList();

    TomTerm_BuildArray.initializePattern();
    funTomTerm_BuildArray = makeAFun("_TomTerm_BuildArray", 2, false);
    protoTomTerm_BuildArray = new TomTerm_BuildArray();

    TomTerm_BuildBuiltin.initializePattern();
    funTomTerm_BuildBuiltin = makeAFun("_TomTerm_BuildBuiltin", 1, false);
    protoTomTerm_BuildBuiltin = new TomTerm_BuildBuiltin();

    TomTerm_CompiledMatch.initializePattern();
    funTomTerm_CompiledMatch = makeAFun("_TomTerm_CompiledMatch", 3, false);
    protoTomTerm_CompiledMatch = new TomTerm_CompiledMatch();

    TomTerm_CompiledPattern.initializePattern();
    funTomTerm_CompiledPattern = makeAFun("_TomTerm_CompiledPattern", 1, false);
    protoTomTerm_CompiledPattern = new TomTerm_CompiledPattern();

    TomTerm_AssignedVariable.initializePattern();
    funTomTerm_AssignedVariable = makeAFun("_TomTerm_AssignedVariable", 3, false);
    protoTomTerm_AssignedVariable = new TomTerm_AssignedVariable();

    TomTerm_Automata.initializePattern();
    funTomTerm_Automata = makeAFun("_TomTerm_Automata", 2, false);
    protoTomTerm_Automata = new TomTerm_Automata();

    TomTerm_MatchNumber.initializePattern();
    funTomTerm_MatchNumber = makeAFun("_TomTerm_MatchNumber", 1, false);
    protoTomTerm_MatchNumber = new TomTerm_MatchNumber();

    TomTerm_PatternNumber.initializePattern();
    funTomTerm_PatternNumber = makeAFun("_TomTerm_PatternNumber", 1, false);
    protoTomTerm_PatternNumber = new TomTerm_PatternNumber();

    TomTerm_ListNumber.initializePattern();
    funTomTerm_ListNumber = makeAFun("_TomTerm_ListNumber", 1, false);
    protoTomTerm_ListNumber = new TomTerm_ListNumber();

    TomTerm_IndexNumber.initializePattern();
    funTomTerm_IndexNumber = makeAFun("_TomTerm_IndexNumber", 1, false);
    protoTomTerm_IndexNumber = new TomTerm_IndexNumber();

    TomTerm_AbsVar.initializePattern();
    funTomTerm_AbsVar = makeAFun("_TomTerm_AbsVar", 1, false);
    protoTomTerm_AbsVar = new TomTerm_AbsVar();

    TomTerm_RenamedVar.initializePattern();
    funTomTerm_RenamedVar = makeAFun("_TomTerm_RenamedVar", 1, false);
    protoTomTerm_RenamedVar = new TomTerm_RenamedVar();

    TomTerm_RuleVar.initializePattern();
    funTomTerm_RuleVar = makeAFun("_TomTerm_RuleVar", 0, false);
    protoTomTerm_RuleVar = new TomTerm_RuleVar();

    TomTerm_Number.initializePattern();
    funTomTerm_Number = makeAFun("_TomTerm_Number", 1, false);
    protoTomTerm_Number = new TomTerm_Number();

    TomTerm_Begin.initializePattern();
    funTomTerm_Begin = makeAFun("_TomTerm_Begin", 1, false);
    protoTomTerm_Begin = new TomTerm_Begin();

    TomTerm_End.initializePattern();
    funTomTerm_End = makeAFun("_TomTerm_End", 1, false);
    protoTomTerm_End = new TomTerm_End();

    Instruction.initialize(this);

    Instruction_IfThenElse.initializePattern();
    funInstruction_IfThenElse = makeAFun("_Instruction_IfThenElse", 3, false);
    protoInstruction_IfThenElse = new Instruction_IfThenElse();

    Instruction_DoWhile.initializePattern();
    funInstruction_DoWhile = makeAFun("_Instruction_DoWhile", 2, false);
    protoInstruction_DoWhile = new Instruction_DoWhile();

    Instruction_Assign.initializePattern();
    funInstruction_Assign = makeAFun("_Instruction_Assign", 2, false);
    protoInstruction_Assign = new Instruction_Assign();

    Instruction_AssignMatchSubject.initializePattern();
    funInstruction_AssignMatchSubject = makeAFun("_Instruction_AssignMatchSubject", 2, false);
    protoInstruction_AssignMatchSubject = new Instruction_AssignMatchSubject();

    Instruction_Increment.initializePattern();
    funInstruction_Increment = makeAFun("_Instruction_Increment", 1, false);
    protoInstruction_Increment = new Instruction_Increment();

    Instruction_Action.initializePattern();
    funInstruction_Action = makeAFun("_Instruction_Action", 1, false);
    protoInstruction_Action = new Instruction_Action();

    Instruction_ExitAction.initializePattern();
    funInstruction_ExitAction = makeAFun("_Instruction_ExitAction", 1, false);
    protoInstruction_ExitAction = new Instruction_ExitAction();

    Instruction_Return.initializePattern();
    funInstruction_Return = makeAFun("_Instruction_Return", 1, false);
    protoInstruction_Return = new Instruction_Return();

    Instruction_OpenBlock.initializePattern();
    funInstruction_OpenBlock = makeAFun("_Instruction_OpenBlock", 0, false);
    protoInstruction_OpenBlock = new Instruction_OpenBlock();

    Instruction_CloseBlock.initializePattern();
    funInstruction_CloseBlock = makeAFun("_Instruction_CloseBlock", 0, false);
    protoInstruction_CloseBlock = new Instruction_CloseBlock();

    Instruction_NamedBlock.initializePattern();
    funInstruction_NamedBlock = makeAFun("_Instruction_NamedBlock", 2, false);
    protoInstruction_NamedBlock = new Instruction_NamedBlock();

    TomSymbol.initialize(this);

    TomSymbol_Symbol.initializePattern();
    funTomSymbol_Symbol = makeAFun("_TomSymbol_Symbol", 5, false);
    protoTomSymbol_Symbol = new TomSymbol_Symbol();

    SlotList.initialize(this);

    SlotList_EmptySlotList.initializePattern();
    funSlotList_EmptySlotList = makeAFun("_SlotList_EmptySlotList", 0, false);
    protoSlotList_EmptySlotList = new SlotList_EmptySlotList();

    SlotList_ConsSlotList.initializePattern();
    funSlotList_ConsSlotList = makeAFun("_SlotList_ConsSlotList", 2, false);
    protoSlotList_ConsSlotList = new SlotList_ConsSlotList();

    PairNameDecl.initialize(this);

    PairNameDecl_Slot.initializePattern();
    funPairNameDecl_Slot = makeAFun("_PairNameDecl_Slot", 2, false);
    protoPairNameDecl_Slot = new PairNameDecl_Slot();

    TomSymbolTable.initialize(this);

    TomSymbolTable_Table.initializePattern();
    funTomSymbolTable_Table = makeAFun("_TomSymbolTable_Table", 1, false);
    protoTomSymbolTable_Table = new TomSymbolTable_Table();

    TomEntryList.initialize(this);

    TomEntryList_EmptyEntryList.initializePattern();
    funTomEntryList_EmptyEntryList = makeAFun("_TomEntryList_EmptyEntryList", 0, false);
    protoTomEntryList_EmptyEntryList = new TomEntryList_EmptyEntryList();

    TomEntryList_ConsEntryList.initializePattern();
    funTomEntryList_ConsEntryList = makeAFun("_TomEntryList_ConsEntryList", 2, false);
    protoTomEntryList_ConsEntryList = new TomEntryList_ConsEntryList();

    TomEntry.initialize(this);

    TomEntry_Entry.initializePattern();
    funTomEntry_Entry = makeAFun("_TomEntry_Entry", 2, false);
    protoTomEntry_Entry = new TomEntry_Entry();

    TomStructureTable.initialize(this);

    TomStructureTable_StructTable.initializePattern();
    funTomStructureTable_StructTable = makeAFun("_TomStructureTable_StructTable", 1, false);
    protoTomStructureTable_StructTable = new TomStructureTable_StructTable();

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

  protected Declaration_GetSubtermDecl makeDeclaration_GetSubtermDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_GetSubtermDecl) {
      protoDeclaration_GetSubtermDecl.initHashCode(annos,fun,args);
      return (Declaration_GetSubtermDecl) build(protoDeclaration_GetSubtermDecl);
    }
  }

  public Declaration_GetSubtermDecl makeDeclaration_GetSubtermDecl(TomTerm _termArg, TomTerm _numberArg, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_termArg, _numberArg, _tlCode, _orgTrack};
    return makeDeclaration_GetSubtermDecl( funDeclaration_GetSubtermDecl, args, empty);
  }

  protected Declaration_IsFsymDecl makeDeclaration_IsFsymDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_IsFsymDecl) {
      protoDeclaration_IsFsymDecl.initHashCode(annos,fun,args);
      return (Declaration_IsFsymDecl) build(protoDeclaration_IsFsymDecl);
    }
  }

  public Declaration_IsFsymDecl makeDeclaration_IsFsymDecl(TomName _astName, TomTerm _term, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _term, _tlCode, _orgTrack};
    return makeDeclaration_IsFsymDecl( funDeclaration_IsFsymDecl, args, empty);
  }

  protected Declaration_GetSlotDecl makeDeclaration_GetSlotDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_GetSlotDecl) {
      protoDeclaration_GetSlotDecl.initHashCode(annos,fun,args);
      return (Declaration_GetSlotDecl) build(protoDeclaration_GetSlotDecl);
    }
  }

  public Declaration_GetSlotDecl makeDeclaration_GetSlotDecl(TomName _astName, TomName _slotName, TomTerm _term, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _slotName, _term, _tlCode, _orgTrack};
    return makeDeclaration_GetSlotDecl( funDeclaration_GetSlotDecl, args, empty);
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

  protected OptionList_EmptyOptionList makeOptionList_EmptyOptionList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoOptionList_EmptyOptionList) {
      protoOptionList_EmptyOptionList.initHashCode(annos,fun,args);
      return (OptionList_EmptyOptionList) build(protoOptionList_EmptyOptionList);
    }
  }

  public OptionList_EmptyOptionList makeOptionList_EmptyOptionList() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeOptionList_EmptyOptionList( funOptionList_EmptyOptionList, args, empty);
  }

  protected OptionList_ConsOptionList makeOptionList_ConsOptionList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoOptionList_ConsOptionList) {
      protoOptionList_ConsOptionList.initHashCode(annos,fun,args);
      return (OptionList_ConsOptionList) build(protoOptionList_ConsOptionList);
    }
  }

  public OptionList_ConsOptionList makeOptionList_ConsOptionList(Option _head, OptionList _tail) {
    aterm.ATerm[] args = new aterm.ATerm[] {_head, _tail};
    return makeOptionList_ConsOptionList( funOptionList_ConsOptionList, args, empty);
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

  protected Expression_GetSubterm makeExpression_GetSubterm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_GetSubterm) {
      protoExpression_GetSubterm.initHashCode(annos,fun,args);
      return (Expression_GetSubterm) build(protoExpression_GetSubterm);
    }
  }

  public Expression_GetSubterm makeExpression_GetSubterm(TomTerm _term, TomTerm _number) {
    aterm.ATerm[] args = new aterm.ATerm[] {_term, _number};
    return makeExpression_GetSubterm( funExpression_GetSubterm, args, empty);
  }

  protected Expression_IsFsym makeExpression_IsFsym(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_IsFsym) {
      protoExpression_IsFsym.initHashCode(annos,fun,args);
      return (Expression_IsFsym) build(protoExpression_IsFsym);
    }
  }

  public Expression_IsFsym makeExpression_IsFsym(TomName _astName, TomTerm _term) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _term};
    return makeExpression_IsFsym( funExpression_IsFsym, args, empty);
  }

  protected Expression_GetSlot makeExpression_GetSlot(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_GetSlot) {
      protoExpression_GetSlot.initHashCode(annos,fun,args);
      return (Expression_GetSlot) build(protoExpression_GetSlot);
    }
  }

  public Expression_GetSlot makeExpression_GetSlot(TomName _astName, String _slotNameString, TomTerm _term) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, makeAppl(makeAFun(_slotNameString, 0, true)), _term};
    return makeExpression_GetSlot( funExpression_GetSlot, args, empty);
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

  protected TomType_TypesToType makeTomType_TypesToType(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomType_TypesToType) {
      protoTomType_TypesToType.initHashCode(annos,fun,args);
      return (TomType_TypesToType) build(protoTomType_TypesToType);
    }
  }

  public TomType_TypesToType makeTomType_TypesToType(TomList _list, TomType _codomain) {
    aterm.ATerm[] args = new aterm.ATerm[] {_list, _codomain};
    return makeTomType_TypesToType( funTomType_TypesToType, args, empty);
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

  protected TomName_PositionName makeTomName_PositionName(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomName_PositionName) {
      protoTomName_PositionName.initHashCode(annos,fun,args);
      return (TomName_PositionName) build(protoTomName_PositionName);
    }
  }

  public TomName_PositionName makeTomName_PositionName(TomList _numberList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_numberList};
    return makeTomName_PositionName( funTomName_PositionName, args, empty);
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

  protected TomList_Empty makeTomList_Empty(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomList_Empty) {
      protoTomList_Empty.initHashCode(annos,fun,args);
      return (TomList_Empty) build(protoTomList_Empty);
    }
  }

  public TomList_Empty makeTomList_Empty() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeTomList_Empty( funTomList_Empty, args, empty);
  }

  protected TomList_Cons makeTomList_Cons(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomList_Cons) {
      protoTomList_Cons.initHashCode(annos,fun,args);
      return (TomList_Cons) build(protoTomList_Cons);
    }
  }

  public TomList_Cons makeTomList_Cons(TomTerm _head, TomList _tail) {
    aterm.ATerm[] args = new aterm.ATerm[] {_head, _tail};
    return makeTomList_Cons( funTomList_Cons, args, empty);
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

  protected TomTerm_Tom makeTomTerm_Tom(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Tom) {
      protoTomTerm_Tom.initHashCode(annos,fun,args);
      return (TomTerm_Tom) build(protoTomTerm_Tom);
    }
  }

  public TomTerm_Tom makeTomTerm_Tom(TomList _list) {
    aterm.ATerm[] args = new aterm.ATerm[] {_list};
    return makeTomTerm_Tom( funTomTerm_Tom, args, empty);
  }

  protected TomTerm_TomInclude makeTomTerm_TomInclude(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_TomInclude) {
      protoTomTerm_TomInclude.initHashCode(annos,fun,args);
      return (TomTerm_TomInclude) build(protoTomTerm_TomInclude);
    }
  }

  public TomTerm_TomInclude makeTomTerm_TomInclude(TomList _list) {
    aterm.ATerm[] args = new aterm.ATerm[] {_list};
    return makeTomTerm_TomInclude( funTomTerm_TomInclude, args, empty);
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

  protected TomTerm_BackQuoteTerm makeTomTerm_BackQuoteTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_BackQuoteTerm) {
      protoTomTerm_BackQuoteTerm.initHashCode(annos,fun,args);
      return (TomTerm_BackQuoteTerm) build(protoTomTerm_BackQuoteTerm);
    }
  }

  public TomTerm_BackQuoteTerm makeTomTerm_BackQuoteTerm(TomTerm _term, Option _option) {
    aterm.ATerm[] args = new aterm.ATerm[] {_term, _option};
    return makeTomTerm_BackQuoteTerm( funTomTerm_BackQuoteTerm, args, empty);
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

  protected TomTerm_RuleSet makeTomTerm_RuleSet(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_RuleSet) {
      protoTomTerm_RuleSet.initHashCode(annos,fun,args);
      return (TomTerm_RuleSet) build(protoTomTerm_RuleSet);
    }
  }

  public TomTerm_RuleSet makeTomTerm_RuleSet(TomList _list, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_list, _orgTrack};
    return makeTomTerm_RuleSet( funTomTerm_RuleSet, args, empty);
  }

  protected TomTerm_RewriteRule makeTomTerm_RewriteRule(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_RewriteRule) {
      protoTomTerm_RewriteRule.initHashCode(annos,fun,args);
      return (TomTerm_RewriteRule) build(protoTomTerm_RewriteRule);
    }
  }

  public TomTerm_RewriteRule makeTomTerm_RewriteRule(TomTerm _lhs, TomTerm _rhs, TomList _condList, Option _option) {
    aterm.ATerm[] args = new aterm.ATerm[] {_lhs, _rhs, _condList, _option};
    return makeTomTerm_RewriteRule( funTomTerm_RewriteRule, args, empty);
  }

  protected TomTerm_SubjectList makeTomTerm_SubjectList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_SubjectList) {
      protoTomTerm_SubjectList.initHashCode(annos,fun,args);
      return (TomTerm_SubjectList) build(protoTomTerm_SubjectList);
    }
  }

  public TomTerm_SubjectList makeTomTerm_SubjectList(TomList _list) {
    aterm.ATerm[] args = new aterm.ATerm[] {_list};
    return makeTomTerm_SubjectList( funTomTerm_SubjectList, args, empty);
  }

  protected TomTerm_PatternList makeTomTerm_PatternList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_PatternList) {
      protoTomTerm_PatternList.initHashCode(annos,fun,args);
      return (TomTerm_PatternList) build(protoTomTerm_PatternList);
    }
  }

  public TomTerm_PatternList makeTomTerm_PatternList(TomList _list) {
    aterm.ATerm[] args = new aterm.ATerm[] {_list};
    return makeTomTerm_PatternList( funTomTerm_PatternList, args, empty);
  }

  protected TomTerm_TermList makeTomTerm_TermList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_TermList) {
      protoTomTerm_TermList.initHashCode(annos,fun,args);
      return (TomTerm_TermList) build(protoTomTerm_TermList);
    }
  }

  public TomTerm_TermList makeTomTerm_TermList(TomList _list) {
    aterm.ATerm[] args = new aterm.ATerm[] {_list};
    return makeTomTerm_TermList( funTomTerm_TermList, args, empty);
  }

  protected TomTerm_Term makeTomTerm_Term(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Term) {
      protoTomTerm_Term.initHashCode(annos,fun,args);
      return (TomTerm_Term) build(protoTomTerm_Term);
    }
  }

  public TomTerm_Term makeTomTerm_Term(TomTerm _term) {
    aterm.ATerm[] args = new aterm.ATerm[] {_term};
    return makeTomTerm_Term( funTomTerm_Term, args, empty);
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

  protected TomTerm_AssignedVariable makeTomTerm_AssignedVariable(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_AssignedVariable) {
      protoTomTerm_AssignedVariable.initHashCode(annos,fun,args);
      return (TomTerm_AssignedVariable) build(protoTomTerm_AssignedVariable);
    }
  }

  public TomTerm_AssignedVariable makeTomTerm_AssignedVariable(String _varName, Expression _source, Integer _nbUse) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeAppl(makeAFun(_varName, 0, true)), _source, makeInt(_nbUse.intValue())};
    return makeTomTerm_AssignedVariable( funTomTerm_AssignedVariable, args, empty);
  }

  protected TomTerm_Automata makeTomTerm_Automata(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Automata) {
      protoTomTerm_Automata.initHashCode(annos,fun,args);
      return (TomTerm_Automata) build(protoTomTerm_Automata);
    }
  }

  public TomTerm_Automata makeTomTerm_Automata(TomList _numberList, TomList _instList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_numberList, _instList};
    return makeTomTerm_Automata( funTomTerm_Automata, args, empty);
  }

  protected TomTerm_MatchNumber makeTomTerm_MatchNumber(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_MatchNumber) {
      protoTomTerm_MatchNumber.initHashCode(annos,fun,args);
      return (TomTerm_MatchNumber) build(protoTomTerm_MatchNumber);
    }
  }

  public TomTerm_MatchNumber makeTomTerm_MatchNumber(TomTerm _number) {
    aterm.ATerm[] args = new aterm.ATerm[] {_number};
    return makeTomTerm_MatchNumber( funTomTerm_MatchNumber, args, empty);
  }

  protected TomTerm_PatternNumber makeTomTerm_PatternNumber(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_PatternNumber) {
      protoTomTerm_PatternNumber.initHashCode(annos,fun,args);
      return (TomTerm_PatternNumber) build(protoTomTerm_PatternNumber);
    }
  }

  public TomTerm_PatternNumber makeTomTerm_PatternNumber(TomTerm _number) {
    aterm.ATerm[] args = new aterm.ATerm[] {_number};
    return makeTomTerm_PatternNumber( funTomTerm_PatternNumber, args, empty);
  }

  protected TomTerm_ListNumber makeTomTerm_ListNumber(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_ListNumber) {
      protoTomTerm_ListNumber.initHashCode(annos,fun,args);
      return (TomTerm_ListNumber) build(protoTomTerm_ListNumber);
    }
  }

  public TomTerm_ListNumber makeTomTerm_ListNumber(TomTerm _number) {
    aterm.ATerm[] args = new aterm.ATerm[] {_number};
    return makeTomTerm_ListNumber( funTomTerm_ListNumber, args, empty);
  }

  protected TomTerm_IndexNumber makeTomTerm_IndexNumber(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_IndexNumber) {
      protoTomTerm_IndexNumber.initHashCode(annos,fun,args);
      return (TomTerm_IndexNumber) build(protoTomTerm_IndexNumber);
    }
  }

  public TomTerm_IndexNumber makeTomTerm_IndexNumber(TomTerm _number) {
    aterm.ATerm[] args = new aterm.ATerm[] {_number};
    return makeTomTerm_IndexNumber( funTomTerm_IndexNumber, args, empty);
  }

  protected TomTerm_AbsVar makeTomTerm_AbsVar(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_AbsVar) {
      protoTomTerm_AbsVar.initHashCode(annos,fun,args);
      return (TomTerm_AbsVar) build(protoTomTerm_AbsVar);
    }
  }

  public TomTerm_AbsVar makeTomTerm_AbsVar(TomTerm _number) {
    aterm.ATerm[] args = new aterm.ATerm[] {_number};
    return makeTomTerm_AbsVar( funTomTerm_AbsVar, args, empty);
  }

  protected TomTerm_RenamedVar makeTomTerm_RenamedVar(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_RenamedVar) {
      protoTomTerm_RenamedVar.initHashCode(annos,fun,args);
      return (TomTerm_RenamedVar) build(protoTomTerm_RenamedVar);
    }
  }

  public TomTerm_RenamedVar makeTomTerm_RenamedVar(TomName _astName) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName};
    return makeTomTerm_RenamedVar( funTomTerm_RenamedVar, args, empty);
  }

  protected TomTerm_RuleVar makeTomTerm_RuleVar(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_RuleVar) {
      protoTomTerm_RuleVar.initHashCode(annos,fun,args);
      return (TomTerm_RuleVar) build(protoTomTerm_RuleVar);
    }
  }

  public TomTerm_RuleVar makeTomTerm_RuleVar() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeTomTerm_RuleVar( funTomTerm_RuleVar, args, empty);
  }

  protected TomTerm_Number makeTomTerm_Number(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Number) {
      protoTomTerm_Number.initHashCode(annos,fun,args);
      return (TomTerm_Number) build(protoTomTerm_Number);
    }
  }

  public TomTerm_Number makeTomTerm_Number(Integer _integer) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeInt(_integer.intValue())};
    return makeTomTerm_Number( funTomTerm_Number, args, empty);
  }

  protected TomTerm_Begin makeTomTerm_Begin(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Begin) {
      protoTomTerm_Begin.initHashCode(annos,fun,args);
      return (TomTerm_Begin) build(protoTomTerm_Begin);
    }
  }

  public TomTerm_Begin makeTomTerm_Begin(TomTerm _number) {
    aterm.ATerm[] args = new aterm.ATerm[] {_number};
    return makeTomTerm_Begin( funTomTerm_Begin, args, empty);
  }

  protected TomTerm_End makeTomTerm_End(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_End) {
      protoTomTerm_End.initHashCode(annos,fun,args);
      return (TomTerm_End) build(protoTomTerm_End);
    }
  }

  public TomTerm_End makeTomTerm_End(TomTerm _number) {
    aterm.ATerm[] args = new aterm.ATerm[] {_number};
    return makeTomTerm_End( funTomTerm_End, args, empty);
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

  protected Instruction_ExitAction makeInstruction_ExitAction(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoInstruction_ExitAction) {
      protoInstruction_ExitAction.initHashCode(annos,fun,args);
      return (Instruction_ExitAction) build(protoInstruction_ExitAction);
    }
  }

  public Instruction_ExitAction makeInstruction_ExitAction(TomList _numberList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_numberList};
    return makeInstruction_ExitAction( funInstruction_ExitAction, args, empty);
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

  protected SlotList_EmptySlotList makeSlotList_EmptySlotList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoSlotList_EmptySlotList) {
      protoSlotList_EmptySlotList.initHashCode(annos,fun,args);
      return (SlotList_EmptySlotList) build(protoSlotList_EmptySlotList);
    }
  }

  public SlotList_EmptySlotList makeSlotList_EmptySlotList() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeSlotList_EmptySlotList( funSlotList_EmptySlotList, args, empty);
  }

  protected SlotList_ConsSlotList makeSlotList_ConsSlotList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoSlotList_ConsSlotList) {
      protoSlotList_ConsSlotList.initHashCode(annos,fun,args);
      return (SlotList_ConsSlotList) build(protoSlotList_ConsSlotList);
    }
  }

  public SlotList_ConsSlotList makeSlotList_ConsSlotList(PairNameDecl _headSlotList, SlotList _tailSlotList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_headSlotList, _tailSlotList};
    return makeSlotList_ConsSlotList( funSlotList_ConsSlotList, args, empty);
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

  protected TomEntryList_EmptyEntryList makeTomEntryList_EmptyEntryList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomEntryList_EmptyEntryList) {
      protoTomEntryList_EmptyEntryList.initHashCode(annos,fun,args);
      return (TomEntryList_EmptyEntryList) build(protoTomEntryList_EmptyEntryList);
    }
  }

  public TomEntryList_EmptyEntryList makeTomEntryList_EmptyEntryList() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeTomEntryList_EmptyEntryList( funTomEntryList_EmptyEntryList, args, empty);
  }

  protected TomEntryList_ConsEntryList makeTomEntryList_ConsEntryList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomEntryList_ConsEntryList) {
      protoTomEntryList_ConsEntryList.initHashCode(annos,fun,args);
      return (TomEntryList_ConsEntryList) build(protoTomEntryList_ConsEntryList);
    }
  }

  public TomEntryList_ConsEntryList makeTomEntryList_ConsEntryList(TomEntry _headEntryList, TomEntryList _tailEntryList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_headEntryList, _tailEntryList};
    return makeTomEntryList_ConsEntryList( funTomEntryList_ConsEntryList, args, empty);
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

}

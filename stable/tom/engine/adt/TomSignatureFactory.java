package jtom.adt;

import aterm.pure.PureFactory;
import aterm.*;

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
  private aterm.AFun funOption_OriginTracking;
  private Option protoOption_OriginTracking;
  private aterm.AFun funOption_LRParen;
  private Option protoOption_LRParen;
  private aterm.AFun funExpression_TomTermToExpression;
  private Expression protoExpression_TomTermToExpression;
  private aterm.AFun funExpression_Not;
  private Expression protoExpression_Not;
  private aterm.AFun funExpression_And;
  private Expression protoExpression_And;
  private aterm.AFun funExpression_TrueGL;
  private Expression protoExpression_TrueGL;
  private aterm.AFun funExpression_FalseGL;
  private Expression protoExpression_FalseGL;
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
  private aterm.AFun funTomTerm_Tom;
  private TomTerm protoTomTerm_Tom;
  private aterm.AFun funTomTerm_TomInclude;
  private TomTerm protoTomTerm_TomInclude;
  private aterm.AFun funTomTerm_MakeTerm;
  private TomTerm protoTomTerm_MakeTerm;
  private aterm.AFun funTomTerm_BackQuoteTerm;
  private TomTerm protoTomTerm_BackQuoteTerm;
  private aterm.AFun funTomTerm_LocalVariable;
  private TomTerm protoTomTerm_LocalVariable;
  private aterm.AFun funTomTerm_EndLocalVariable;
  private TomTerm protoTomTerm_EndLocalVariable;
  private aterm.AFun funTomTerm_BuildVariable;
  private TomTerm protoTomTerm_BuildVariable;
  private aterm.AFun funTomTerm_BuildVariableStar;
  private TomTerm protoTomTerm_BuildVariableStar;
  private aterm.AFun funTomTerm_BuildTerm;
  private TomTerm protoTomTerm_BuildTerm;
  private aterm.AFun funTomTerm_BuildList;
  private TomTerm protoTomTerm_BuildList;
  private aterm.AFun funTomTerm_BuildArray;
  private TomTerm protoTomTerm_BuildArray;
  private aterm.AFun funTomTerm_FunctionCall;
  private TomTerm protoTomTerm_FunctionCall;
  private aterm.AFun funTomTerm_Appl;
  private TomTerm protoTomTerm_Appl;
  private aterm.AFun funTomTerm_RecordAppl;
  private TomTerm protoTomTerm_RecordAppl;
  private aterm.AFun funTomTerm_PairSlotAppl;
  private TomTerm protoTomTerm_PairSlotAppl;
  private aterm.AFun funTomTerm_Match;
  private TomTerm protoTomTerm_Match;
  private aterm.AFun funTomTerm_MakeFunctionBegin;
  private TomTerm protoTomTerm_MakeFunctionBegin;
  private aterm.AFun funTomTerm_MakeFunctionEnd;
  private TomTerm protoTomTerm_MakeFunctionEnd;
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
  private aterm.AFun funTomTerm_GLVar;
  private TomTerm protoTomTerm_GLVar;
  private aterm.AFun funTomTerm_Variable;
  private TomTerm protoTomTerm_Variable;
  private aterm.AFun funTomTerm_VariableStar;
  private TomTerm protoTomTerm_VariableStar;
  private aterm.AFun funTomTerm_Placeholder;
  private TomTerm protoTomTerm_Placeholder;
  private aterm.AFun funTomTerm_UnamedVariable;
  private TomTerm protoTomTerm_UnamedVariable;
  private aterm.AFun funTomTerm_CompiledMatch;
  private TomTerm protoTomTerm_CompiledMatch;
  private aterm.AFun funTomTerm_Automata;
  private TomTerm protoTomTerm_Automata;
  private aterm.AFun funTomTerm_IfThenElse;
  private TomTerm protoTomTerm_IfThenElse;
  private aterm.AFun funTomTerm_DoWhile;
  private TomTerm protoTomTerm_DoWhile;
  private aterm.AFun funTomTerm_Assign;
  private TomTerm protoTomTerm_Assign;
  private aterm.AFun funTomTerm_Declaration;
  private TomTerm protoTomTerm_Declaration;
  private aterm.AFun funTomTerm_Begin;
  private TomTerm protoTomTerm_Begin;
  private aterm.AFun funTomTerm_End;
  private TomTerm protoTomTerm_End;
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
  private aterm.AFun funTomTerm_Increment;
  private TomTerm protoTomTerm_Increment;
  private aterm.AFun funTomTerm_Action;
  private TomTerm protoTomTerm_Action;
  private aterm.AFun funTomTerm_ExitAction;
  private TomTerm protoTomTerm_ExitAction;
  private aterm.AFun funTomTerm_Return;
  private TomTerm protoTomTerm_Return;
  private aterm.AFun funTomTerm_OpenBlock;
  private TomTerm protoTomTerm_OpenBlock;
  private aterm.AFun funTomTerm_CloseBlock;
  private TomTerm protoTomTerm_CloseBlock;
  private aterm.AFun funTomTerm_NamedBlock;
  private TomTerm protoTomTerm_NamedBlock;
  private aterm.AFun funTomTerm_Line;
  private TomTerm protoTomTerm_Line;
  private aterm.AFun funTomTerm_DotTerm;
  private TomTerm protoTomTerm_DotTerm;
  private aterm.AFun funTomName_Name;
  private TomName protoTomName_Name;
  private aterm.AFun funTomName_PositionName;
  private TomName protoTomName_PositionName;
  private aterm.AFun funTomName_EmptyName;
  private TomName protoTomName_EmptyName;
  private aterm.AFun funTomSymbol_Symbol;
  private TomSymbol protoTomSymbol_Symbol;
  private aterm.AFun funTomSymbolTable_Table;
  private TomSymbolTable protoTomSymbolTable_Table;
  private aterm.AFun funTomEntryList_EmptyEntryList;
  private TomEntryList protoTomEntryList_EmptyEntryList;
  private aterm.AFun funTomEntryList_ConsEntryList;
  private TomEntryList protoTomEntryList_ConsEntryList;
  private aterm.AFun funTomEntry_Entry;
  private TomEntry protoTomEntry_Entry;
  private aterm.AFun funSlotList_EmptySlotList;
  private SlotList protoSlotList_EmptySlotList;
  private aterm.AFun funSlotList_ConsSlotList;
  private SlotList protoSlotList_ConsSlotList;
  private aterm.AFun funPairNameDecl_Slot;
  private PairNameDecl protoPairNameDecl_Slot;

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
    funDeclaration_TypeTermDecl = makeAFun("Declaration_TypeTermDecl", 3, false);
    protoDeclaration_TypeTermDecl = new Declaration_TypeTermDecl();

    Declaration_TypeListDecl.initializePattern();
    funDeclaration_TypeListDecl = makeAFun("Declaration_TypeListDecl", 3, false);
    protoDeclaration_TypeListDecl = new Declaration_TypeListDecl();

    Declaration_TypeArrayDecl.initializePattern();
    funDeclaration_TypeArrayDecl = makeAFun("Declaration_TypeArrayDecl", 3, false);
    protoDeclaration_TypeArrayDecl = new Declaration_TypeArrayDecl();

    Declaration_GetFunctionSymbolDecl.initializePattern();
    funDeclaration_GetFunctionSymbolDecl = makeAFun("Declaration_GetFunctionSymbolDecl", 3, false);
    protoDeclaration_GetFunctionSymbolDecl = new Declaration_GetFunctionSymbolDecl();

    Declaration_GetSubtermDecl.initializePattern();
    funDeclaration_GetSubtermDecl = makeAFun("Declaration_GetSubtermDecl", 4, false);
    protoDeclaration_GetSubtermDecl = new Declaration_GetSubtermDecl();

    Declaration_IsFsymDecl.initializePattern();
    funDeclaration_IsFsymDecl = makeAFun("Declaration_IsFsymDecl", 4, false);
    protoDeclaration_IsFsymDecl = new Declaration_IsFsymDecl();

    Declaration_GetSlotDecl.initializePattern();
    funDeclaration_GetSlotDecl = makeAFun("Declaration_GetSlotDecl", 5, false);
    protoDeclaration_GetSlotDecl = new Declaration_GetSlotDecl();

    Declaration_CompareFunctionSymbolDecl.initializePattern();
    funDeclaration_CompareFunctionSymbolDecl = makeAFun("Declaration_CompareFunctionSymbolDecl", 4, false);
    protoDeclaration_CompareFunctionSymbolDecl = new Declaration_CompareFunctionSymbolDecl();

    Declaration_TermsEqualDecl.initializePattern();
    funDeclaration_TermsEqualDecl = makeAFun("Declaration_TermsEqualDecl", 4, false);
    protoDeclaration_TermsEqualDecl = new Declaration_TermsEqualDecl();

    Declaration_GetHeadDecl.initializePattern();
    funDeclaration_GetHeadDecl = makeAFun("Declaration_GetHeadDecl", 3, false);
    protoDeclaration_GetHeadDecl = new Declaration_GetHeadDecl();

    Declaration_GetTailDecl.initializePattern();
    funDeclaration_GetTailDecl = makeAFun("Declaration_GetTailDecl", 3, false);
    protoDeclaration_GetTailDecl = new Declaration_GetTailDecl();

    Declaration_IsEmptyDecl.initializePattern();
    funDeclaration_IsEmptyDecl = makeAFun("Declaration_IsEmptyDecl", 3, false);
    protoDeclaration_IsEmptyDecl = new Declaration_IsEmptyDecl();

    Declaration_MakeEmptyList.initializePattern();
    funDeclaration_MakeEmptyList = makeAFun("Declaration_MakeEmptyList", 3, false);
    protoDeclaration_MakeEmptyList = new Declaration_MakeEmptyList();

    Declaration_MakeAddList.initializePattern();
    funDeclaration_MakeAddList = makeAFun("Declaration_MakeAddList", 5, false);
    protoDeclaration_MakeAddList = new Declaration_MakeAddList();

    Declaration_GetElementDecl.initializePattern();
    funDeclaration_GetElementDecl = makeAFun("Declaration_GetElementDecl", 4, false);
    protoDeclaration_GetElementDecl = new Declaration_GetElementDecl();

    Declaration_GetSizeDecl.initializePattern();
    funDeclaration_GetSizeDecl = makeAFun("Declaration_GetSizeDecl", 3, false);
    protoDeclaration_GetSizeDecl = new Declaration_GetSizeDecl();

    Declaration_MakeEmptyArray.initializePattern();
    funDeclaration_MakeEmptyArray = makeAFun("Declaration_MakeEmptyArray", 4, false);
    protoDeclaration_MakeEmptyArray = new Declaration_MakeEmptyArray();

    Declaration_MakeAddArray.initializePattern();
    funDeclaration_MakeAddArray = makeAFun("Declaration_MakeAddArray", 5, false);
    protoDeclaration_MakeAddArray = new Declaration_MakeAddArray();

    Declaration_MakeDecl.initializePattern();
    funDeclaration_MakeDecl = makeAFun("Declaration_MakeDecl", 5, false);
    protoDeclaration_MakeDecl = new Declaration_MakeDecl();

    Declaration_SymbolDecl.initializePattern();
    funDeclaration_SymbolDecl = makeAFun("Declaration_SymbolDecl", 1, false);
    protoDeclaration_SymbolDecl = new Declaration_SymbolDecl();

    Declaration_ListSymbolDecl.initializePattern();
    funDeclaration_ListSymbolDecl = makeAFun("Declaration_ListSymbolDecl", 1, false);
    protoDeclaration_ListSymbolDecl = new Declaration_ListSymbolDecl();

    Declaration_ArraySymbolDecl.initializePattern();
    funDeclaration_ArraySymbolDecl = makeAFun("Declaration_ArraySymbolDecl", 1, false);
    protoDeclaration_ArraySymbolDecl = new Declaration_ArraySymbolDecl();

    Declaration_EmptyDeclaration.initializePattern();
    funDeclaration_EmptyDeclaration = makeAFun("Declaration_EmptyDeclaration", 0, false);
    protoDeclaration_EmptyDeclaration = new Declaration_EmptyDeclaration();

    OptionList.initialize(this);

    OptionList_EmptyOptionList.initializePattern();
    funOptionList_EmptyOptionList = makeAFun("OptionList_EmptyOptionList", 0, false);
    protoOptionList_EmptyOptionList = new OptionList_EmptyOptionList();

    OptionList_ConsOptionList.initializePattern();
    funOptionList_ConsOptionList = makeAFun("OptionList_ConsOptionList", 2, false);
    protoOptionList_ConsOptionList = new OptionList_ConsOptionList();

    Option.initialize(this);

    Option_DeclarationToOption.initializePattern();
    funOption_DeclarationToOption = makeAFun("Option_DeclarationToOption", 1, false);
    protoOption_DeclarationToOption = new Option_DeclarationToOption();

    Option_TomNameToOption.initializePattern();
    funOption_TomNameToOption = makeAFun("Option_TomNameToOption", 1, false);
    protoOption_TomNameToOption = new Option_TomNameToOption();

    Option_TomTermToOption.initializePattern();
    funOption_TomTermToOption = makeAFun("Option_TomTermToOption", 1, false);
    protoOption_TomTermToOption = new Option_TomTermToOption();

    Option_Option.initializePattern();
    funOption_Option = makeAFun("Option_Option", 1, false);
    protoOption_Option = new Option_Option();

    Option_DefinedSymbol.initializePattern();
    funOption_DefinedSymbol = makeAFun("Option_DefinedSymbol", 0, false);
    protoOption_DefinedSymbol = new Option_DefinedSymbol();

    Option_OriginTracking.initializePattern();
    funOption_OriginTracking = makeAFun("Option_OriginTracking", 2, false);
    protoOption_OriginTracking = new Option_OriginTracking();

    Option_LRParen.initializePattern();
    funOption_LRParen = makeAFun("Option_LRParen", 1, false);
    protoOption_LRParen = new Option_LRParen();

    Expression.initialize(this);

    Expression_TomTermToExpression.initializePattern();
    funExpression_TomTermToExpression = makeAFun("Expression_TomTermToExpression", 1, false);
    protoExpression_TomTermToExpression = new Expression_TomTermToExpression();

    Expression_Not.initializePattern();
    funExpression_Not = makeAFun("Expression_Not", 1, false);
    protoExpression_Not = new Expression_Not();

    Expression_And.initializePattern();
    funExpression_And = makeAFun("Expression_And", 2, false);
    protoExpression_And = new Expression_And();

    Expression_TrueGL.initializePattern();
    funExpression_TrueGL = makeAFun("Expression_TrueGL", 0, false);
    protoExpression_TrueGL = new Expression_TrueGL();

    Expression_FalseGL.initializePattern();
    funExpression_FalseGL = makeAFun("Expression_FalseGL", 0, false);
    protoExpression_FalseGL = new Expression_FalseGL();

    Expression_IsEmptyList.initializePattern();
    funExpression_IsEmptyList = makeAFun("Expression_IsEmptyList", 1, false);
    protoExpression_IsEmptyList = new Expression_IsEmptyList();

    Expression_IsEmptyArray.initializePattern();
    funExpression_IsEmptyArray = makeAFun("Expression_IsEmptyArray", 2, false);
    protoExpression_IsEmptyArray = new Expression_IsEmptyArray();

    Expression_EqualFunctionSymbol.initializePattern();
    funExpression_EqualFunctionSymbol = makeAFun("Expression_EqualFunctionSymbol", 2, false);
    protoExpression_EqualFunctionSymbol = new Expression_EqualFunctionSymbol();

    Expression_EqualTerm.initializePattern();
    funExpression_EqualTerm = makeAFun("Expression_EqualTerm", 2, false);
    protoExpression_EqualTerm = new Expression_EqualTerm();

    Expression_GetSubterm.initializePattern();
    funExpression_GetSubterm = makeAFun("Expression_GetSubterm", 2, false);
    protoExpression_GetSubterm = new Expression_GetSubterm();

    Expression_IsFsym.initializePattern();
    funExpression_IsFsym = makeAFun("Expression_IsFsym", 2, false);
    protoExpression_IsFsym = new Expression_IsFsym();

    Expression_GetSlot.initializePattern();
    funExpression_GetSlot = makeAFun("Expression_GetSlot", 3, false);
    protoExpression_GetSlot = new Expression_GetSlot();

    Expression_GetHead.initializePattern();
    funExpression_GetHead = makeAFun("Expression_GetHead", 1, false);
    protoExpression_GetHead = new Expression_GetHead();

    Expression_GetTail.initializePattern();
    funExpression_GetTail = makeAFun("Expression_GetTail", 1, false);
    protoExpression_GetTail = new Expression_GetTail();

    Expression_GetSize.initializePattern();
    funExpression_GetSize = makeAFun("Expression_GetSize", 1, false);
    protoExpression_GetSize = new Expression_GetSize();

    Expression_GetElement.initializePattern();
    funExpression_GetElement = makeAFun("Expression_GetElement", 2, false);
    protoExpression_GetElement = new Expression_GetElement();

    Expression_GetSliceList.initializePattern();
    funExpression_GetSliceList = makeAFun("Expression_GetSliceList", 3, false);
    protoExpression_GetSliceList = new Expression_GetSliceList();

    Expression_GetSliceArray.initializePattern();
    funExpression_GetSliceArray = makeAFun("Expression_GetSliceArray", 4, false);
    protoExpression_GetSliceArray = new Expression_GetSliceArray();

    TargetLanguage.initialize(this);

    TargetLanguage_TL.initializePattern();
    funTargetLanguage_TL = makeAFun("TargetLanguage_TL", 3, false);
    protoTargetLanguage_TL = new TargetLanguage_TL();

    TargetLanguage_ITL.initializePattern();
    funTargetLanguage_ITL = makeAFun("TargetLanguage_ITL", 1, false);
    protoTargetLanguage_ITL = new TargetLanguage_ITL();

    Position.initialize(this);

    Position_Position.initializePattern();
    funPosition_Position = makeAFun("Position_Position", 2, false);
    protoPosition_Position = new Position_Position();

    TomType.initialize(this);

    TomType_Type.initializePattern();
    funTomType_Type = makeAFun("TomType_Type", 2, false);
    protoTomType_Type = new TomType_Type();

    TomType_TypesToType.initializePattern();
    funTomType_TypesToType = makeAFun("TomType_TypesToType", 2, false);
    protoTomType_TypesToType = new TomType_TypesToType();

    TomType_TomType.initializePattern();
    funTomType_TomType = makeAFun("TomType_TomType", 1, false);
    protoTomType_TomType = new TomType_TomType();

    TomType_TomTypeAlone.initializePattern();
    funTomType_TomTypeAlone = makeAFun("TomType_TomTypeAlone", 1, false);
    protoTomType_TomTypeAlone = new TomType_TomTypeAlone();

    TomType_TLType.initializePattern();
    funTomType_TLType = makeAFun("TomType_TLType", 1, false);
    protoTomType_TLType = new TomType_TLType();

    TomType_EmptyType.initializePattern();
    funTomType_EmptyType = makeAFun("TomType_EmptyType", 0, false);
    protoTomType_EmptyType = new TomType_EmptyType();

    TomList.initialize(this);

    TomList_Empty.initializePattern();
    funTomList_Empty = makeAFun("TomList_Empty", 0, false);
    protoTomList_Empty = new TomList_Empty();

    TomList_Cons.initializePattern();
    funTomList_Cons = makeAFun("TomList_Cons", 2, false);
    protoTomList_Cons = new TomList_Cons();

    TomTerm.initialize(this);

    TomTerm_TargetLanguageToTomTerm.initializePattern();
    funTomTerm_TargetLanguageToTomTerm = makeAFun("TomTerm_TargetLanguageToTomTerm", 1, false);
    protoTomTerm_TargetLanguageToTomTerm = new TomTerm_TargetLanguageToTomTerm();

    TomTerm_TomTypeToTomTerm.initializePattern();
    funTomTerm_TomTypeToTomTerm = makeAFun("TomTerm_TomTypeToTomTerm", 1, false);
    protoTomTerm_TomTypeToTomTerm = new TomTerm_TomTypeToTomTerm();

    TomTerm_TomNameToTomTerm.initializePattern();
    funTomTerm_TomNameToTomTerm = makeAFun("TomTerm_TomNameToTomTerm", 1, false);
    protoTomTerm_TomNameToTomTerm = new TomTerm_TomNameToTomTerm();

    TomTerm_TomSymbolToTomTerm.initializePattern();
    funTomTerm_TomSymbolToTomTerm = makeAFun("TomTerm_TomSymbolToTomTerm", 1, false);
    protoTomTerm_TomSymbolToTomTerm = new TomTerm_TomSymbolToTomTerm();

    TomTerm_DeclarationToTomTerm.initializePattern();
    funTomTerm_DeclarationToTomTerm = makeAFun("TomTerm_DeclarationToTomTerm", 1, false);
    protoTomTerm_DeclarationToTomTerm = new TomTerm_DeclarationToTomTerm();

    TomTerm_OptionToTomTerm.initializePattern();
    funTomTerm_OptionToTomTerm = makeAFun("TomTerm_OptionToTomTerm", 1, false);
    protoTomTerm_OptionToTomTerm = new TomTerm_OptionToTomTerm();

    TomTerm_ExpressionToTomTerm.initializePattern();
    funTomTerm_ExpressionToTomTerm = makeAFun("TomTerm_ExpressionToTomTerm", 1, false);
    protoTomTerm_ExpressionToTomTerm = new TomTerm_ExpressionToTomTerm();

    TomTerm_Tom.initializePattern();
    funTomTerm_Tom = makeAFun("TomTerm_Tom", 1, false);
    protoTomTerm_Tom = new TomTerm_Tom();

    TomTerm_TomInclude.initializePattern();
    funTomTerm_TomInclude = makeAFun("TomTerm_TomInclude", 1, false);
    protoTomTerm_TomInclude = new TomTerm_TomInclude();

    TomTerm_MakeTerm.initializePattern();
    funTomTerm_MakeTerm = makeAFun("TomTerm_MakeTerm", 1, false);
    protoTomTerm_MakeTerm = new TomTerm_MakeTerm();

    TomTerm_BackQuoteTerm.initializePattern();
    funTomTerm_BackQuoteTerm = makeAFun("TomTerm_BackQuoteTerm", 1, false);
    protoTomTerm_BackQuoteTerm = new TomTerm_BackQuoteTerm();

    TomTerm_LocalVariable.initializePattern();
    funTomTerm_LocalVariable = makeAFun("TomTerm_LocalVariable", 0, false);
    protoTomTerm_LocalVariable = new TomTerm_LocalVariable();

    TomTerm_EndLocalVariable.initializePattern();
    funTomTerm_EndLocalVariable = makeAFun("TomTerm_EndLocalVariable", 0, false);
    protoTomTerm_EndLocalVariable = new TomTerm_EndLocalVariable();

    TomTerm_BuildVariable.initializePattern();
    funTomTerm_BuildVariable = makeAFun("TomTerm_BuildVariable", 1, false);
    protoTomTerm_BuildVariable = new TomTerm_BuildVariable();

    TomTerm_BuildVariableStar.initializePattern();
    funTomTerm_BuildVariableStar = makeAFun("TomTerm_BuildVariableStar", 1, false);
    protoTomTerm_BuildVariableStar = new TomTerm_BuildVariableStar();

    TomTerm_BuildTerm.initializePattern();
    funTomTerm_BuildTerm = makeAFun("TomTerm_BuildTerm", 2, false);
    protoTomTerm_BuildTerm = new TomTerm_BuildTerm();

    TomTerm_BuildList.initializePattern();
    funTomTerm_BuildList = makeAFun("TomTerm_BuildList", 2, false);
    protoTomTerm_BuildList = new TomTerm_BuildList();

    TomTerm_BuildArray.initializePattern();
    funTomTerm_BuildArray = makeAFun("TomTerm_BuildArray", 2, false);
    protoTomTerm_BuildArray = new TomTerm_BuildArray();

    TomTerm_FunctionCall.initializePattern();
    funTomTerm_FunctionCall = makeAFun("TomTerm_FunctionCall", 2, false);
    protoTomTerm_FunctionCall = new TomTerm_FunctionCall();

    TomTerm_Appl.initializePattern();
    funTomTerm_Appl = makeAFun("TomTerm_Appl", 3, false);
    protoTomTerm_Appl = new TomTerm_Appl();

    TomTerm_RecordAppl.initializePattern();
    funTomTerm_RecordAppl = makeAFun("TomTerm_RecordAppl", 3, false);
    protoTomTerm_RecordAppl = new TomTerm_RecordAppl();

    TomTerm_PairSlotAppl.initializePattern();
    funTomTerm_PairSlotAppl = makeAFun("TomTerm_PairSlotAppl", 2, false);
    protoTomTerm_PairSlotAppl = new TomTerm_PairSlotAppl();

    TomTerm_Match.initializePattern();
    funTomTerm_Match = makeAFun("TomTerm_Match", 3, false);
    protoTomTerm_Match = new TomTerm_Match();

    TomTerm_MakeFunctionBegin.initializePattern();
    funTomTerm_MakeFunctionBegin = makeAFun("TomTerm_MakeFunctionBegin", 2, false);
    protoTomTerm_MakeFunctionBegin = new TomTerm_MakeFunctionBegin();

    TomTerm_MakeFunctionEnd.initializePattern();
    funTomTerm_MakeFunctionEnd = makeAFun("TomTerm_MakeFunctionEnd", 0, false);
    protoTomTerm_MakeFunctionEnd = new TomTerm_MakeFunctionEnd();

    TomTerm_RuleSet.initializePattern();
    funTomTerm_RuleSet = makeAFun("TomTerm_RuleSet", 2, false);
    protoTomTerm_RuleSet = new TomTerm_RuleSet();

    TomTerm_RewriteRule.initializePattern();
    funTomTerm_RewriteRule = makeAFun("TomTerm_RewriteRule", 2, false);
    protoTomTerm_RewriteRule = new TomTerm_RewriteRule();

    TomTerm_SubjectList.initializePattern();
    funTomTerm_SubjectList = makeAFun("TomTerm_SubjectList", 1, false);
    protoTomTerm_SubjectList = new TomTerm_SubjectList();

    TomTerm_PatternList.initializePattern();
    funTomTerm_PatternList = makeAFun("TomTerm_PatternList", 1, false);
    protoTomTerm_PatternList = new TomTerm_PatternList();

    TomTerm_TermList.initializePattern();
    funTomTerm_TermList = makeAFun("TomTerm_TermList", 1, false);
    protoTomTerm_TermList = new TomTerm_TermList();

    TomTerm_Term.initializePattern();
    funTomTerm_Term = makeAFun("TomTerm_Term", 1, false);
    protoTomTerm_Term = new TomTerm_Term();

    TomTerm_PatternAction.initializePattern();
    funTomTerm_PatternAction = makeAFun("TomTerm_PatternAction", 2, false);
    protoTomTerm_PatternAction = new TomTerm_PatternAction();

    TomTerm_GLVar.initializePattern();
    funTomTerm_GLVar = makeAFun("TomTerm_GLVar", 2, false);
    protoTomTerm_GLVar = new TomTerm_GLVar();

    TomTerm_Variable.initializePattern();
    funTomTerm_Variable = makeAFun("TomTerm_Variable", 3, false);
    protoTomTerm_Variable = new TomTerm_Variable();

    TomTerm_VariableStar.initializePattern();
    funTomTerm_VariableStar = makeAFun("TomTerm_VariableStar", 3, false);
    protoTomTerm_VariableStar = new TomTerm_VariableStar();

    TomTerm_Placeholder.initializePattern();
    funTomTerm_Placeholder = makeAFun("TomTerm_Placeholder", 1, false);
    protoTomTerm_Placeholder = new TomTerm_Placeholder();

    TomTerm_UnamedVariable.initializePattern();
    funTomTerm_UnamedVariable = makeAFun("TomTerm_UnamedVariable", 2, false);
    protoTomTerm_UnamedVariable = new TomTerm_UnamedVariable();

    TomTerm_CompiledMatch.initializePattern();
    funTomTerm_CompiledMatch = makeAFun("TomTerm_CompiledMatch", 2, false);
    protoTomTerm_CompiledMatch = new TomTerm_CompiledMatch();

    TomTerm_Automata.initializePattern();
    funTomTerm_Automata = makeAFun("TomTerm_Automata", 2, false);
    protoTomTerm_Automata = new TomTerm_Automata();

    TomTerm_IfThenElse.initializePattern();
    funTomTerm_IfThenElse = makeAFun("TomTerm_IfThenElse", 3, false);
    protoTomTerm_IfThenElse = new TomTerm_IfThenElse();

    TomTerm_DoWhile.initializePattern();
    funTomTerm_DoWhile = makeAFun("TomTerm_DoWhile", 2, false);
    protoTomTerm_DoWhile = new TomTerm_DoWhile();

    TomTerm_Assign.initializePattern();
    funTomTerm_Assign = makeAFun("TomTerm_Assign", 2, false);
    protoTomTerm_Assign = new TomTerm_Assign();

    TomTerm_Declaration.initializePattern();
    funTomTerm_Declaration = makeAFun("TomTerm_Declaration", 1, false);
    protoTomTerm_Declaration = new TomTerm_Declaration();

    TomTerm_Begin.initializePattern();
    funTomTerm_Begin = makeAFun("TomTerm_Begin", 1, false);
    protoTomTerm_Begin = new TomTerm_Begin();

    TomTerm_End.initializePattern();
    funTomTerm_End = makeAFun("TomTerm_End", 1, false);
    protoTomTerm_End = new TomTerm_End();

    TomTerm_MatchNumber.initializePattern();
    funTomTerm_MatchNumber = makeAFun("TomTerm_MatchNumber", 1, false);
    protoTomTerm_MatchNumber = new TomTerm_MatchNumber();

    TomTerm_PatternNumber.initializePattern();
    funTomTerm_PatternNumber = makeAFun("TomTerm_PatternNumber", 1, false);
    protoTomTerm_PatternNumber = new TomTerm_PatternNumber();

    TomTerm_ListNumber.initializePattern();
    funTomTerm_ListNumber = makeAFun("TomTerm_ListNumber", 1, false);
    protoTomTerm_ListNumber = new TomTerm_ListNumber();

    TomTerm_IndexNumber.initializePattern();
    funTomTerm_IndexNumber = makeAFun("TomTerm_IndexNumber", 1, false);
    protoTomTerm_IndexNumber = new TomTerm_IndexNumber();

    TomTerm_AbsVar.initializePattern();
    funTomTerm_AbsVar = makeAFun("TomTerm_AbsVar", 1, false);
    protoTomTerm_AbsVar = new TomTerm_AbsVar();

    TomTerm_RenamedVar.initializePattern();
    funTomTerm_RenamedVar = makeAFun("TomTerm_RenamedVar", 1, false);
    protoTomTerm_RenamedVar = new TomTerm_RenamedVar();

    TomTerm_RuleVar.initializePattern();
    funTomTerm_RuleVar = makeAFun("TomTerm_RuleVar", 0, false);
    protoTomTerm_RuleVar = new TomTerm_RuleVar();

    TomTerm_Number.initializePattern();
    funTomTerm_Number = makeAFun("TomTerm_Number", 1, false);
    protoTomTerm_Number = new TomTerm_Number();

    TomTerm_Increment.initializePattern();
    funTomTerm_Increment = makeAFun("TomTerm_Increment", 1, false);
    protoTomTerm_Increment = new TomTerm_Increment();

    TomTerm_Action.initializePattern();
    funTomTerm_Action = makeAFun("TomTerm_Action", 1, false);
    protoTomTerm_Action = new TomTerm_Action();

    TomTerm_ExitAction.initializePattern();
    funTomTerm_ExitAction = makeAFun("TomTerm_ExitAction", 1, false);
    protoTomTerm_ExitAction = new TomTerm_ExitAction();

    TomTerm_Return.initializePattern();
    funTomTerm_Return = makeAFun("TomTerm_Return", 1, false);
    protoTomTerm_Return = new TomTerm_Return();

    TomTerm_OpenBlock.initializePattern();
    funTomTerm_OpenBlock = makeAFun("TomTerm_OpenBlock", 0, false);
    protoTomTerm_OpenBlock = new TomTerm_OpenBlock();

    TomTerm_CloseBlock.initializePattern();
    funTomTerm_CloseBlock = makeAFun("TomTerm_CloseBlock", 0, false);
    protoTomTerm_CloseBlock = new TomTerm_CloseBlock();

    TomTerm_NamedBlock.initializePattern();
    funTomTerm_NamedBlock = makeAFun("TomTerm_NamedBlock", 2, false);
    protoTomTerm_NamedBlock = new TomTerm_NamedBlock();

    TomTerm_Line.initializePattern();
    funTomTerm_Line = makeAFun("TomTerm_Line", 1, false);
    protoTomTerm_Line = new TomTerm_Line();

    TomTerm_DotTerm.initializePattern();
    funTomTerm_DotTerm = makeAFun("TomTerm_DotTerm", 2, false);
    protoTomTerm_DotTerm = new TomTerm_DotTerm();

    TomName.initialize(this);

    TomName_Name.initializePattern();
    funTomName_Name = makeAFun("TomName_Name", 1, false);
    protoTomName_Name = new TomName_Name();

    TomName_PositionName.initializePattern();
    funTomName_PositionName = makeAFun("TomName_PositionName", 1, false);
    protoTomName_PositionName = new TomName_PositionName();

    TomName_EmptyName.initializePattern();
    funTomName_EmptyName = makeAFun("TomName_EmptyName", 0, false);
    protoTomName_EmptyName = new TomName_EmptyName();

    TomSymbol.initialize(this);

    TomSymbol_Symbol.initializePattern();
    funTomSymbol_Symbol = makeAFun("TomSymbol_Symbol", 5, false);
    protoTomSymbol_Symbol = new TomSymbol_Symbol();

    TomSymbolTable.initialize(this);

    TomSymbolTable_Table.initializePattern();
    funTomSymbolTable_Table = makeAFun("TomSymbolTable_Table", 1, false);
    protoTomSymbolTable_Table = new TomSymbolTable_Table();

    TomEntryList.initialize(this);

    TomEntryList_EmptyEntryList.initializePattern();
    funTomEntryList_EmptyEntryList = makeAFun("TomEntryList_EmptyEntryList", 0, false);
    protoTomEntryList_EmptyEntryList = new TomEntryList_EmptyEntryList();

    TomEntryList_ConsEntryList.initializePattern();
    funTomEntryList_ConsEntryList = makeAFun("TomEntryList_ConsEntryList", 2, false);
    protoTomEntryList_ConsEntryList = new TomEntryList_ConsEntryList();

    TomEntry.initialize(this);

    TomEntry_Entry.initializePattern();
    funTomEntry_Entry = makeAFun("TomEntry_Entry", 2, false);
    protoTomEntry_Entry = new TomEntry_Entry();

    SlotList.initialize(this);

    SlotList_EmptySlotList.initializePattern();
    funSlotList_EmptySlotList = makeAFun("SlotList_EmptySlotList", 0, false);
    protoSlotList_EmptySlotList = new SlotList_EmptySlotList();

    SlotList_ConsSlotList.initializePattern();
    funSlotList_ConsSlotList = makeAFun("SlotList_ConsSlotList", 2, false);
    protoSlotList_ConsSlotList = new SlotList_ConsSlotList();

    PairNameDecl.initialize(this);

    PairNameDecl_Slot.initializePattern();
    funPairNameDecl_Slot = makeAFun("PairNameDecl_Slot", 2, false);
    protoPairNameDecl_Slot = new PairNameDecl_Slot();

  }

  protected Declaration makeDeclaration_TypeTermDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_TypeTermDecl) {
      protoDeclaration_TypeTermDecl.initHashCode(annos,fun,args);
      return (Declaration) build(protoDeclaration_TypeTermDecl);
    }
  }

  public Declaration makeDeclaration_TypeTermDecl(TomName _astName, TomList _keywordList, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _keywordList, _orgTrack};
    return makeDeclaration_TypeTermDecl( funDeclaration_TypeTermDecl, args, empty);
  }

  protected Declaration makeDeclaration_TypeListDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_TypeListDecl) {
      protoDeclaration_TypeListDecl.initHashCode(annos,fun,args);
      return (Declaration) build(protoDeclaration_TypeListDecl);
    }
  }

  public Declaration makeDeclaration_TypeListDecl(TomName _astName, TomList _keywordList, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _keywordList, _orgTrack};
    return makeDeclaration_TypeListDecl( funDeclaration_TypeListDecl, args, empty);
  }

  protected Declaration makeDeclaration_TypeArrayDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_TypeArrayDecl) {
      protoDeclaration_TypeArrayDecl.initHashCode(annos,fun,args);
      return (Declaration) build(protoDeclaration_TypeArrayDecl);
    }
  }

  public Declaration makeDeclaration_TypeArrayDecl(TomName _astName, TomList _keywordList, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _keywordList, _orgTrack};
    return makeDeclaration_TypeArrayDecl( funDeclaration_TypeArrayDecl, args, empty);
  }

  protected Declaration makeDeclaration_GetFunctionSymbolDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_GetFunctionSymbolDecl) {
      protoDeclaration_GetFunctionSymbolDecl.initHashCode(annos,fun,args);
      return (Declaration) build(protoDeclaration_GetFunctionSymbolDecl);
    }
  }

  public Declaration makeDeclaration_GetFunctionSymbolDecl(TomTerm _termArg, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_termArg, _tlCode, _orgTrack};
    return makeDeclaration_GetFunctionSymbolDecl( funDeclaration_GetFunctionSymbolDecl, args, empty);
  }

  protected Declaration makeDeclaration_GetSubtermDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_GetSubtermDecl) {
      protoDeclaration_GetSubtermDecl.initHashCode(annos,fun,args);
      return (Declaration) build(protoDeclaration_GetSubtermDecl);
    }
  }

  public Declaration makeDeclaration_GetSubtermDecl(TomTerm _termArg, TomTerm _numberArg, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_termArg, _numberArg, _tlCode, _orgTrack};
    return makeDeclaration_GetSubtermDecl( funDeclaration_GetSubtermDecl, args, empty);
  }

  protected Declaration makeDeclaration_IsFsymDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_IsFsymDecl) {
      protoDeclaration_IsFsymDecl.initHashCode(annos,fun,args);
      return (Declaration) build(protoDeclaration_IsFsymDecl);
    }
  }

  public Declaration makeDeclaration_IsFsymDecl(TomName _astName, TomTerm _term, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _term, _tlCode, _orgTrack};
    return makeDeclaration_IsFsymDecl( funDeclaration_IsFsymDecl, args, empty);
  }

  protected Declaration makeDeclaration_GetSlotDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_GetSlotDecl) {
      protoDeclaration_GetSlotDecl.initHashCode(annos,fun,args);
      return (Declaration) build(protoDeclaration_GetSlotDecl);
    }
  }

  public Declaration makeDeclaration_GetSlotDecl(TomName _astName, TomName _slotName, TomTerm _term, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _slotName, _term, _tlCode, _orgTrack};
    return makeDeclaration_GetSlotDecl( funDeclaration_GetSlotDecl, args, empty);
  }

  protected Declaration makeDeclaration_CompareFunctionSymbolDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_CompareFunctionSymbolDecl) {
      protoDeclaration_CompareFunctionSymbolDecl.initHashCode(annos,fun,args);
      return (Declaration) build(protoDeclaration_CompareFunctionSymbolDecl);
    }
  }

  public Declaration makeDeclaration_CompareFunctionSymbolDecl(TomTerm _symbolArg1, TomTerm _symbolArg2, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_symbolArg1, _symbolArg2, _tlCode, _orgTrack};
    return makeDeclaration_CompareFunctionSymbolDecl( funDeclaration_CompareFunctionSymbolDecl, args, empty);
  }

  protected Declaration makeDeclaration_TermsEqualDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_TermsEqualDecl) {
      protoDeclaration_TermsEqualDecl.initHashCode(annos,fun,args);
      return (Declaration) build(protoDeclaration_TermsEqualDecl);
    }
  }

  public Declaration makeDeclaration_TermsEqualDecl(TomTerm _termArg1, TomTerm _termArg2, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_termArg1, _termArg2, _tlCode, _orgTrack};
    return makeDeclaration_TermsEqualDecl( funDeclaration_TermsEqualDecl, args, empty);
  }

  protected Declaration makeDeclaration_GetHeadDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_GetHeadDecl) {
      protoDeclaration_GetHeadDecl.initHashCode(annos,fun,args);
      return (Declaration) build(protoDeclaration_GetHeadDecl);
    }
  }

  public Declaration makeDeclaration_GetHeadDecl(TomTerm _var, TargetLanguage _tlcode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_var, _tlcode, _orgTrack};
    return makeDeclaration_GetHeadDecl( funDeclaration_GetHeadDecl, args, empty);
  }

  protected Declaration makeDeclaration_GetTailDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_GetTailDecl) {
      protoDeclaration_GetTailDecl.initHashCode(annos,fun,args);
      return (Declaration) build(protoDeclaration_GetTailDecl);
    }
  }

  public Declaration makeDeclaration_GetTailDecl(TomTerm _var, TargetLanguage _tlcode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_var, _tlcode, _orgTrack};
    return makeDeclaration_GetTailDecl( funDeclaration_GetTailDecl, args, empty);
  }

  protected Declaration makeDeclaration_IsEmptyDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_IsEmptyDecl) {
      protoDeclaration_IsEmptyDecl.initHashCode(annos,fun,args);
      return (Declaration) build(protoDeclaration_IsEmptyDecl);
    }
  }

  public Declaration makeDeclaration_IsEmptyDecl(TomTerm _var, TargetLanguage _tlcode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_var, _tlcode, _orgTrack};
    return makeDeclaration_IsEmptyDecl( funDeclaration_IsEmptyDecl, args, empty);
  }

  protected Declaration makeDeclaration_MakeEmptyList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_MakeEmptyList) {
      protoDeclaration_MakeEmptyList.initHashCode(annos,fun,args);
      return (Declaration) build(protoDeclaration_MakeEmptyList);
    }
  }

  public Declaration makeDeclaration_MakeEmptyList(TomName _astName, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _tlCode, _orgTrack};
    return makeDeclaration_MakeEmptyList( funDeclaration_MakeEmptyList, args, empty);
  }

  protected Declaration makeDeclaration_MakeAddList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_MakeAddList) {
      protoDeclaration_MakeAddList.initHashCode(annos,fun,args);
      return (Declaration) build(protoDeclaration_MakeAddList);
    }
  }

  public Declaration makeDeclaration_MakeAddList(TomName _astName, TomTerm _varElt, TomTerm _varList, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _varElt, _varList, _tlCode, _orgTrack};
    return makeDeclaration_MakeAddList( funDeclaration_MakeAddList, args, empty);
  }

  protected Declaration makeDeclaration_GetElementDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_GetElementDecl) {
      protoDeclaration_GetElementDecl.initHashCode(annos,fun,args);
      return (Declaration) build(protoDeclaration_GetElementDecl);
    }
  }

  public Declaration makeDeclaration_GetElementDecl(TomTerm _kid1, TomTerm _kid2, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1, _kid2, _tlCode, _orgTrack};
    return makeDeclaration_GetElementDecl( funDeclaration_GetElementDecl, args, empty);
  }

  protected Declaration makeDeclaration_GetSizeDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_GetSizeDecl) {
      protoDeclaration_GetSizeDecl.initHashCode(annos,fun,args);
      return (Declaration) build(protoDeclaration_GetSizeDecl);
    }
  }

  public Declaration makeDeclaration_GetSizeDecl(TomTerm _kid1, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1, _tlCode, _orgTrack};
    return makeDeclaration_GetSizeDecl( funDeclaration_GetSizeDecl, args, empty);
  }

  protected Declaration makeDeclaration_MakeEmptyArray(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_MakeEmptyArray) {
      protoDeclaration_MakeEmptyArray.initHashCode(annos,fun,args);
      return (Declaration) build(protoDeclaration_MakeEmptyArray);
    }
  }

  public Declaration makeDeclaration_MakeEmptyArray(TomName _astName, TomTerm _varSize, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _varSize, _tlCode, _orgTrack};
    return makeDeclaration_MakeEmptyArray( funDeclaration_MakeEmptyArray, args, empty);
  }

  protected Declaration makeDeclaration_MakeAddArray(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_MakeAddArray) {
      protoDeclaration_MakeAddArray.initHashCode(annos,fun,args);
      return (Declaration) build(protoDeclaration_MakeAddArray);
    }
  }

  public Declaration makeDeclaration_MakeAddArray(TomName _astName, TomTerm _varElt, TomTerm _varList, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _varElt, _varList, _tlCode, _orgTrack};
    return makeDeclaration_MakeAddArray( funDeclaration_MakeAddArray, args, empty);
  }

  protected Declaration makeDeclaration_MakeDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_MakeDecl) {
      protoDeclaration_MakeDecl.initHashCode(annos,fun,args);
      return (Declaration) build(protoDeclaration_MakeDecl);
    }
  }

  public Declaration makeDeclaration_MakeDecl(TomName _astName, TomType _astType, TomList _args, TargetLanguage _tlCode, Option _orgTrack) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _astType, _args, _tlCode, _orgTrack};
    return makeDeclaration_MakeDecl( funDeclaration_MakeDecl, args, empty);
  }

  protected Declaration makeDeclaration_SymbolDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_SymbolDecl) {
      protoDeclaration_SymbolDecl.initHashCode(annos,fun,args);
      return (Declaration) build(protoDeclaration_SymbolDecl);
    }
  }

  public Declaration makeDeclaration_SymbolDecl(TomName _astName) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName};
    return makeDeclaration_SymbolDecl( funDeclaration_SymbolDecl, args, empty);
  }

  protected Declaration makeDeclaration_ListSymbolDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_ListSymbolDecl) {
      protoDeclaration_ListSymbolDecl.initHashCode(annos,fun,args);
      return (Declaration) build(protoDeclaration_ListSymbolDecl);
    }
  }

  public Declaration makeDeclaration_ListSymbolDecl(TomName _astName) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName};
    return makeDeclaration_ListSymbolDecl( funDeclaration_ListSymbolDecl, args, empty);
  }

  protected Declaration makeDeclaration_ArraySymbolDecl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_ArraySymbolDecl) {
      protoDeclaration_ArraySymbolDecl.initHashCode(annos,fun,args);
      return (Declaration) build(protoDeclaration_ArraySymbolDecl);
    }
  }

  public Declaration makeDeclaration_ArraySymbolDecl(TomName _astName) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName};
    return makeDeclaration_ArraySymbolDecl( funDeclaration_ArraySymbolDecl, args, empty);
  }

  protected Declaration makeDeclaration_EmptyDeclaration(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoDeclaration_EmptyDeclaration) {
      protoDeclaration_EmptyDeclaration.initHashCode(annos,fun,args);
      return (Declaration) build(protoDeclaration_EmptyDeclaration);
    }
  }

  public Declaration makeDeclaration_EmptyDeclaration() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeDeclaration_EmptyDeclaration( funDeclaration_EmptyDeclaration, args, empty);
  }

  protected OptionList makeOptionList_EmptyOptionList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoOptionList_EmptyOptionList) {
      protoOptionList_EmptyOptionList.initHashCode(annos,fun,args);
      return (OptionList) build(protoOptionList_EmptyOptionList);
    }
  }

  public OptionList makeOptionList_EmptyOptionList() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeOptionList_EmptyOptionList( funOptionList_EmptyOptionList, args, empty);
  }

  protected OptionList makeOptionList_ConsOptionList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoOptionList_ConsOptionList) {
      protoOptionList_ConsOptionList.initHashCode(annos,fun,args);
      return (OptionList) build(protoOptionList_ConsOptionList);
    }
  }

  public OptionList makeOptionList_ConsOptionList(Option _head, OptionList _tail) {
    aterm.ATerm[] args = new aterm.ATerm[] {_head, _tail};
    return makeOptionList_ConsOptionList( funOptionList_ConsOptionList, args, empty);
  }

  protected Option makeOption_DeclarationToOption(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoOption_DeclarationToOption) {
      protoOption_DeclarationToOption.initHashCode(annos,fun,args);
      return (Option) build(protoOption_DeclarationToOption);
    }
  }

  public Option makeOption_DeclarationToOption(Declaration _astDeclaration) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astDeclaration};
    return makeOption_DeclarationToOption( funOption_DeclarationToOption, args, empty);
  }

  protected Option makeOption_TomNameToOption(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoOption_TomNameToOption) {
      protoOption_TomNameToOption.initHashCode(annos,fun,args);
      return (Option) build(protoOption_TomNameToOption);
    }
  }

  public Option makeOption_TomNameToOption(TomName _astName) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName};
    return makeOption_TomNameToOption( funOption_TomNameToOption, args, empty);
  }

  protected Option makeOption_TomTermToOption(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoOption_TomTermToOption) {
      protoOption_TomTermToOption.initHashCode(annos,fun,args);
      return (Option) build(protoOption_TomTermToOption);
    }
  }

  public Option makeOption_TomTermToOption(TomTerm _astTerm) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astTerm};
    return makeOption_TomTermToOption( funOption_TomTermToOption, args, empty);
  }

  protected Option makeOption_Option(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoOption_Option) {
      protoOption_Option.initHashCode(annos,fun,args);
      return (Option) build(protoOption_Option);
    }
  }

  public Option makeOption_Option(OptionList _optionList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_optionList};
    return makeOption_Option( funOption_Option, args, empty);
  }

  protected Option makeOption_DefinedSymbol(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoOption_DefinedSymbol) {
      protoOption_DefinedSymbol.initHashCode(annos,fun,args);
      return (Option) build(protoOption_DefinedSymbol);
    }
  }

  public Option makeOption_DefinedSymbol() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeOption_DefinedSymbol( funOption_DefinedSymbol, args, empty);
  }

  protected Option makeOption_OriginTracking(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoOption_OriginTracking) {
      protoOption_OriginTracking.initHashCode(annos,fun,args);
      return (Option) build(protoOption_OriginTracking);
    }
  }

  public Option makeOption_OriginTracking(TomName _astName, TomTerm _line) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _line};
    return makeOption_OriginTracking( funOption_OriginTracking, args, empty);
  }

  protected Option makeOption_LRParen(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoOption_LRParen) {
      protoOption_LRParen.initHashCode(annos,fun,args);
      return (Option) build(protoOption_LRParen);
    }
  }

  public Option makeOption_LRParen(TomName _astName) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName};
    return makeOption_LRParen( funOption_LRParen, args, empty);
  }

  protected Expression makeExpression_TomTermToExpression(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_TomTermToExpression) {
      protoExpression_TomTermToExpression.initHashCode(annos,fun,args);
      return (Expression) build(protoExpression_TomTermToExpression);
    }
  }

  public Expression makeExpression_TomTermToExpression(TomTerm _astTerm) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astTerm};
    return makeExpression_TomTermToExpression( funExpression_TomTermToExpression, args, empty);
  }

  protected Expression makeExpression_Not(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_Not) {
      protoExpression_Not.initHashCode(annos,fun,args);
      return (Expression) build(protoExpression_Not);
    }
  }

  public Expression makeExpression_Not(Expression _arg) {
    aterm.ATerm[] args = new aterm.ATerm[] {_arg};
    return makeExpression_Not( funExpression_Not, args, empty);
  }

  protected Expression makeExpression_And(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_And) {
      protoExpression_And.initHashCode(annos,fun,args);
      return (Expression) build(protoExpression_And);
    }
  }

  public Expression makeExpression_And(Expression _arg1, Expression _arg2) {
    aterm.ATerm[] args = new aterm.ATerm[] {_arg1, _arg2};
    return makeExpression_And( funExpression_And, args, empty);
  }

  protected Expression makeExpression_TrueGL(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_TrueGL) {
      protoExpression_TrueGL.initHashCode(annos,fun,args);
      return (Expression) build(protoExpression_TrueGL);
    }
  }

  public Expression makeExpression_TrueGL() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeExpression_TrueGL( funExpression_TrueGL, args, empty);
  }

  protected Expression makeExpression_FalseGL(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_FalseGL) {
      protoExpression_FalseGL.initHashCode(annos,fun,args);
      return (Expression) build(protoExpression_FalseGL);
    }
  }

  public Expression makeExpression_FalseGL() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeExpression_FalseGL( funExpression_FalseGL, args, empty);
  }

  protected Expression makeExpression_IsEmptyList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_IsEmptyList) {
      protoExpression_IsEmptyList.initHashCode(annos,fun,args);
      return (Expression) build(protoExpression_IsEmptyList);
    }
  }

  public Expression makeExpression_IsEmptyList(TomTerm _kid1) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1};
    return makeExpression_IsEmptyList( funExpression_IsEmptyList, args, empty);
  }

  protected Expression makeExpression_IsEmptyArray(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_IsEmptyArray) {
      protoExpression_IsEmptyArray.initHashCode(annos,fun,args);
      return (Expression) build(protoExpression_IsEmptyArray);
    }
  }

  public Expression makeExpression_IsEmptyArray(TomTerm _kid1, TomTerm _kid2) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1, _kid2};
    return makeExpression_IsEmptyArray( funExpression_IsEmptyArray, args, empty);
  }

  protected Expression makeExpression_EqualFunctionSymbol(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_EqualFunctionSymbol) {
      protoExpression_EqualFunctionSymbol.initHashCode(annos,fun,args);
      return (Expression) build(protoExpression_EqualFunctionSymbol);
    }
  }

  public Expression makeExpression_EqualFunctionSymbol(TomTerm _kid1, TomTerm _kid2) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1, _kid2};
    return makeExpression_EqualFunctionSymbol( funExpression_EqualFunctionSymbol, args, empty);
  }

  protected Expression makeExpression_EqualTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_EqualTerm) {
      protoExpression_EqualTerm.initHashCode(annos,fun,args);
      return (Expression) build(protoExpression_EqualTerm);
    }
  }

  public Expression makeExpression_EqualTerm(TomTerm _kid1, TomTerm _kid2) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1, _kid2};
    return makeExpression_EqualTerm( funExpression_EqualTerm, args, empty);
  }

  protected Expression makeExpression_GetSubterm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_GetSubterm) {
      protoExpression_GetSubterm.initHashCode(annos,fun,args);
      return (Expression) build(protoExpression_GetSubterm);
    }
  }

  public Expression makeExpression_GetSubterm(TomTerm _term, TomTerm _number) {
    aterm.ATerm[] args = new aterm.ATerm[] {_term, _number};
    return makeExpression_GetSubterm( funExpression_GetSubterm, args, empty);
  }

  protected Expression makeExpression_IsFsym(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_IsFsym) {
      protoExpression_IsFsym.initHashCode(annos,fun,args);
      return (Expression) build(protoExpression_IsFsym);
    }
  }

  public Expression makeExpression_IsFsym(TomName _astName, TomTerm _term) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _term};
    return makeExpression_IsFsym( funExpression_IsFsym, args, empty);
  }

  protected Expression makeExpression_GetSlot(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_GetSlot) {
      protoExpression_GetSlot.initHashCode(annos,fun,args);
      return (Expression) build(protoExpression_GetSlot);
    }
  }

  public Expression makeExpression_GetSlot(TomName _astName, String _slotNameString, TomTerm _term) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, makeAppl(makeAFun(_slotNameString, 0, true)), _term};
    return makeExpression_GetSlot( funExpression_GetSlot, args, empty);
  }

  protected Expression makeExpression_GetHead(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_GetHead) {
      protoExpression_GetHead.initHashCode(annos,fun,args);
      return (Expression) build(protoExpression_GetHead);
    }
  }

  public Expression makeExpression_GetHead(TomTerm _kid1) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1};
    return makeExpression_GetHead( funExpression_GetHead, args, empty);
  }

  protected Expression makeExpression_GetTail(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_GetTail) {
      protoExpression_GetTail.initHashCode(annos,fun,args);
      return (Expression) build(protoExpression_GetTail);
    }
  }

  public Expression makeExpression_GetTail(TomTerm _kid1) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1};
    return makeExpression_GetTail( funExpression_GetTail, args, empty);
  }

  protected Expression makeExpression_GetSize(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_GetSize) {
      protoExpression_GetSize.initHashCode(annos,fun,args);
      return (Expression) build(protoExpression_GetSize);
    }
  }

  public Expression makeExpression_GetSize(TomTerm _kid1) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1};
    return makeExpression_GetSize( funExpression_GetSize, args, empty);
  }

  protected Expression makeExpression_GetElement(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_GetElement) {
      protoExpression_GetElement.initHashCode(annos,fun,args);
      return (Expression) build(protoExpression_GetElement);
    }
  }

  public Expression makeExpression_GetElement(TomTerm _kid1, TomTerm _kid2) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1, _kid2};
    return makeExpression_GetElement( funExpression_GetElement, args, empty);
  }

  protected Expression makeExpression_GetSliceList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_GetSliceList) {
      protoExpression_GetSliceList.initHashCode(annos,fun,args);
      return (Expression) build(protoExpression_GetSliceList);
    }
  }

  public Expression makeExpression_GetSliceList(TomName _astName, TomTerm _variableBeginAST, TomTerm _variableEndAST) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _variableBeginAST, _variableEndAST};
    return makeExpression_GetSliceList( funExpression_GetSliceList, args, empty);
  }

  protected Expression makeExpression_GetSliceArray(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoExpression_GetSliceArray) {
      protoExpression_GetSliceArray.initHashCode(annos,fun,args);
      return (Expression) build(protoExpression_GetSliceArray);
    }
  }

  public Expression makeExpression_GetSliceArray(TomName _astName, TomTerm _subjectListName, TomTerm _variableBeginAST, TomTerm _variableEndAST) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _subjectListName, _variableBeginAST, _variableEndAST};
    return makeExpression_GetSliceArray( funExpression_GetSliceArray, args, empty);
  }

  protected TargetLanguage makeTargetLanguage_TL(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTargetLanguage_TL) {
      protoTargetLanguage_TL.initHashCode(annos,fun,args);
      return (TargetLanguage) build(protoTargetLanguage_TL);
    }
  }

  public TargetLanguage makeTargetLanguage_TL(String _code, Position _start, Position _end) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeAppl(makeAFun(_code, 0, true)), _start, _end};
    return makeTargetLanguage_TL( funTargetLanguage_TL, args, empty);
  }

  protected TargetLanguage makeTargetLanguage_ITL(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTargetLanguage_ITL) {
      protoTargetLanguage_ITL.initHashCode(annos,fun,args);
      return (TargetLanguage) build(protoTargetLanguage_ITL);
    }
  }

  public TargetLanguage makeTargetLanguage_ITL(String _code) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeAppl(makeAFun(_code, 0, true))};
    return makeTargetLanguage_ITL( funTargetLanguage_ITL, args, empty);
  }

  protected Position makePosition_Position(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoPosition_Position) {
      protoPosition_Position.initHashCode(annos,fun,args);
      return (Position) build(protoPosition_Position);
    }
  }

  public Position makePosition_Position(Integer _line, Integer _column) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeInt(_line.intValue()), makeInt(_column.intValue())};
    return makePosition_Position( funPosition_Position, args, empty);
  }

  protected TomType makeTomType_Type(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomType_Type) {
      protoTomType_Type.initHashCode(annos,fun,args);
      return (TomType) build(protoTomType_Type);
    }
  }

  public TomType makeTomType_Type(TomType _tomType, TomType _tlType) {
    aterm.ATerm[] args = new aterm.ATerm[] {_tomType, _tlType};
    return makeTomType_Type( funTomType_Type, args, empty);
  }

  protected TomType makeTomType_TypesToType(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomType_TypesToType) {
      protoTomType_TypesToType.initHashCode(annos,fun,args);
      return (TomType) build(protoTomType_TypesToType);
    }
  }

  public TomType makeTomType_TypesToType(TomList _list, TomType _codomain) {
    aterm.ATerm[] args = new aterm.ATerm[] {_list, _codomain};
    return makeTomType_TypesToType( funTomType_TypesToType, args, empty);
  }

  protected TomType makeTomType_TomType(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomType_TomType) {
      protoTomType_TomType.initHashCode(annos,fun,args);
      return (TomType) build(protoTomType_TomType);
    }
  }

  public TomType makeTomType_TomType(String _string) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeAppl(makeAFun(_string, 0, true))};
    return makeTomType_TomType( funTomType_TomType, args, empty);
  }

  protected TomType makeTomType_TomTypeAlone(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomType_TomTypeAlone) {
      protoTomType_TomTypeAlone.initHashCode(annos,fun,args);
      return (TomType) build(protoTomType_TomTypeAlone);
    }
  }

  public TomType makeTomType_TomTypeAlone(String _string) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeAppl(makeAFun(_string, 0, true))};
    return makeTomType_TomTypeAlone( funTomType_TomTypeAlone, args, empty);
  }

  protected TomType makeTomType_TLType(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomType_TLType) {
      protoTomType_TLType.initHashCode(annos,fun,args);
      return (TomType) build(protoTomType_TLType);
    }
  }

  public TomType makeTomType_TLType(TargetLanguage _tl) {
    aterm.ATerm[] args = new aterm.ATerm[] {_tl};
    return makeTomType_TLType( funTomType_TLType, args, empty);
  }

  protected TomType makeTomType_EmptyType(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomType_EmptyType) {
      protoTomType_EmptyType.initHashCode(annos,fun,args);
      return (TomType) build(protoTomType_EmptyType);
    }
  }

  public TomType makeTomType_EmptyType() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeTomType_EmptyType( funTomType_EmptyType, args, empty);
  }

  protected TomList makeTomList_Empty(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomList_Empty) {
      protoTomList_Empty.initHashCode(annos,fun,args);
      return (TomList) build(protoTomList_Empty);
    }
  }

  public TomList makeTomList_Empty() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeTomList_Empty( funTomList_Empty, args, empty);
  }

  protected TomList makeTomList_Cons(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomList_Cons) {
      protoTomList_Cons.initHashCode(annos,fun,args);
      return (TomList) build(protoTomList_Cons);
    }
  }

  public TomList makeTomList_Cons(TomTerm _head, TomList _tail) {
    aterm.ATerm[] args = new aterm.ATerm[] {_head, _tail};
    return makeTomList_Cons( funTomList_Cons, args, empty);
  }

  protected TomTerm makeTomTerm_TargetLanguageToTomTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_TargetLanguageToTomTerm) {
      protoTomTerm_TargetLanguageToTomTerm.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_TargetLanguageToTomTerm);
    }
  }

  public TomTerm makeTomTerm_TargetLanguageToTomTerm(TargetLanguage _tl) {
    aterm.ATerm[] args = new aterm.ATerm[] {_tl};
    return makeTomTerm_TargetLanguageToTomTerm( funTomTerm_TargetLanguageToTomTerm, args, empty);
  }

  protected TomTerm makeTomTerm_TomTypeToTomTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_TomTypeToTomTerm) {
      protoTomTerm_TomTypeToTomTerm.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_TomTypeToTomTerm);
    }
  }

  public TomTerm makeTomTerm_TomTypeToTomTerm(TomType _astType) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astType};
    return makeTomTerm_TomTypeToTomTerm( funTomTerm_TomTypeToTomTerm, args, empty);
  }

  protected TomTerm makeTomTerm_TomNameToTomTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_TomNameToTomTerm) {
      protoTomTerm_TomNameToTomTerm.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_TomNameToTomTerm);
    }
  }

  public TomTerm makeTomTerm_TomNameToTomTerm(TomName _astName) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName};
    return makeTomTerm_TomNameToTomTerm( funTomTerm_TomNameToTomTerm, args, empty);
  }

  protected TomTerm makeTomTerm_TomSymbolToTomTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_TomSymbolToTomTerm) {
      protoTomTerm_TomSymbolToTomTerm.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_TomSymbolToTomTerm);
    }
  }

  public TomTerm makeTomTerm_TomSymbolToTomTerm(TomSymbol _astSymbol) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astSymbol};
    return makeTomTerm_TomSymbolToTomTerm( funTomTerm_TomSymbolToTomTerm, args, empty);
  }

  protected TomTerm makeTomTerm_DeclarationToTomTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_DeclarationToTomTerm) {
      protoTomTerm_DeclarationToTomTerm.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_DeclarationToTomTerm);
    }
  }

  public TomTerm makeTomTerm_DeclarationToTomTerm(Declaration _astDeclaration) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astDeclaration};
    return makeTomTerm_DeclarationToTomTerm( funTomTerm_DeclarationToTomTerm, args, empty);
  }

  protected TomTerm makeTomTerm_OptionToTomTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_OptionToTomTerm) {
      protoTomTerm_OptionToTomTerm.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_OptionToTomTerm);
    }
  }

  public TomTerm makeTomTerm_OptionToTomTerm(Option _astOption) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astOption};
    return makeTomTerm_OptionToTomTerm( funTomTerm_OptionToTomTerm, args, empty);
  }

  protected TomTerm makeTomTerm_ExpressionToTomTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_ExpressionToTomTerm) {
      protoTomTerm_ExpressionToTomTerm.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_ExpressionToTomTerm);
    }
  }

  public TomTerm makeTomTerm_ExpressionToTomTerm(Expression _astExpression) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astExpression};
    return makeTomTerm_ExpressionToTomTerm( funTomTerm_ExpressionToTomTerm, args, empty);
  }

  protected TomTerm makeTomTerm_Tom(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Tom) {
      protoTomTerm_Tom.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_Tom);
    }
  }

  public TomTerm makeTomTerm_Tom(TomList _list) {
    aterm.ATerm[] args = new aterm.ATerm[] {_list};
    return makeTomTerm_Tom( funTomTerm_Tom, args, empty);
  }

  protected TomTerm makeTomTerm_TomInclude(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_TomInclude) {
      protoTomTerm_TomInclude.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_TomInclude);
    }
  }

  public TomTerm makeTomTerm_TomInclude(TomList _list) {
    aterm.ATerm[] args = new aterm.ATerm[] {_list};
    return makeTomTerm_TomInclude( funTomTerm_TomInclude, args, empty);
  }

  protected TomTerm makeTomTerm_MakeTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_MakeTerm) {
      protoTomTerm_MakeTerm.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_MakeTerm);
    }
  }

  public TomTerm makeTomTerm_MakeTerm(TomTerm _kid1) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1};
    return makeTomTerm_MakeTerm( funTomTerm_MakeTerm, args, empty);
  }

  protected TomTerm makeTomTerm_BackQuoteTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_BackQuoteTerm) {
      protoTomTerm_BackQuoteTerm.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_BackQuoteTerm);
    }
  }

  public TomTerm makeTomTerm_BackQuoteTerm(TomTerm _term) {
    aterm.ATerm[] args = new aterm.ATerm[] {_term};
    return makeTomTerm_BackQuoteTerm( funTomTerm_BackQuoteTerm, args, empty);
  }

  protected TomTerm makeTomTerm_LocalVariable(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_LocalVariable) {
      protoTomTerm_LocalVariable.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_LocalVariable);
    }
  }

  public TomTerm makeTomTerm_LocalVariable() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeTomTerm_LocalVariable( funTomTerm_LocalVariable, args, empty);
  }

  protected TomTerm makeTomTerm_EndLocalVariable(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_EndLocalVariable) {
      protoTomTerm_EndLocalVariable.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_EndLocalVariable);
    }
  }

  public TomTerm makeTomTerm_EndLocalVariable() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeTomTerm_EndLocalVariable( funTomTerm_EndLocalVariable, args, empty);
  }

  protected TomTerm makeTomTerm_BuildVariable(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_BuildVariable) {
      protoTomTerm_BuildVariable.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_BuildVariable);
    }
  }

  public TomTerm makeTomTerm_BuildVariable(TomName _astName) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName};
    return makeTomTerm_BuildVariable( funTomTerm_BuildVariable, args, empty);
  }

  protected TomTerm makeTomTerm_BuildVariableStar(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_BuildVariableStar) {
      protoTomTerm_BuildVariableStar.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_BuildVariableStar);
    }
  }

  public TomTerm makeTomTerm_BuildVariableStar(TomName _astName) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName};
    return makeTomTerm_BuildVariableStar( funTomTerm_BuildVariableStar, args, empty);
  }

  protected TomTerm makeTomTerm_BuildTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_BuildTerm) {
      protoTomTerm_BuildTerm.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_BuildTerm);
    }
  }

  public TomTerm makeTomTerm_BuildTerm(TomName _astName, TomList _args) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _args};
    return makeTomTerm_BuildTerm( funTomTerm_BuildTerm, args, empty);
  }

  protected TomTerm makeTomTerm_BuildList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_BuildList) {
      protoTomTerm_BuildList.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_BuildList);
    }
  }

  public TomTerm makeTomTerm_BuildList(TomName _astName, TomList _args) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _args};
    return makeTomTerm_BuildList( funTomTerm_BuildList, args, empty);
  }

  protected TomTerm makeTomTerm_BuildArray(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_BuildArray) {
      protoTomTerm_BuildArray.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_BuildArray);
    }
  }

  public TomTerm makeTomTerm_BuildArray(TomName _astName, TomList _args) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _args};
    return makeTomTerm_BuildArray( funTomTerm_BuildArray, args, empty);
  }

  protected TomTerm makeTomTerm_FunctionCall(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_FunctionCall) {
      protoTomTerm_FunctionCall.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_FunctionCall);
    }
  }

  public TomTerm makeTomTerm_FunctionCall(TomName _astName, TomList _args) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _args};
    return makeTomTerm_FunctionCall( funTomTerm_FunctionCall, args, empty);
  }

  protected TomTerm makeTomTerm_Appl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Appl) {
      protoTomTerm_Appl.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_Appl);
    }
  }

  public TomTerm makeTomTerm_Appl(Option _option, TomName _astName, TomList _args) {
    aterm.ATerm[] args = new aterm.ATerm[] {_option, _astName, _args};
    return makeTomTerm_Appl( funTomTerm_Appl, args, empty);
  }

  protected TomTerm makeTomTerm_RecordAppl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_RecordAppl) {
      protoTomTerm_RecordAppl.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_RecordAppl);
    }
  }

  public TomTerm makeTomTerm_RecordAppl(Option _option, TomName _astName, TomList _args) {
    aterm.ATerm[] args = new aterm.ATerm[] {_option, _astName, _args};
    return makeTomTerm_RecordAppl( funTomTerm_RecordAppl, args, empty);
  }

  protected TomTerm makeTomTerm_PairSlotAppl(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_PairSlotAppl) {
      protoTomTerm_PairSlotAppl.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_PairSlotAppl);
    }
  }

  public TomTerm makeTomTerm_PairSlotAppl(TomName _slotName, TomTerm _appl) {
    aterm.ATerm[] args = new aterm.ATerm[] {_slotName, _appl};
    return makeTomTerm_PairSlotAppl( funTomTerm_PairSlotAppl, args, empty);
  }

  protected TomTerm makeTomTerm_Match(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Match) {
      protoTomTerm_Match.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_Match);
    }
  }

  public TomTerm makeTomTerm_Match(Option _option, TomTerm _subjectList, TomTerm _patternList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_option, _subjectList, _patternList};
    return makeTomTerm_Match( funTomTerm_Match, args, empty);
  }

  protected TomTerm makeTomTerm_MakeFunctionBegin(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_MakeFunctionBegin) {
      protoTomTerm_MakeFunctionBegin.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_MakeFunctionBegin);
    }
  }

  public TomTerm makeTomTerm_MakeFunctionBegin(TomName _astName, TomTerm _subjectListAST) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _subjectListAST};
    return makeTomTerm_MakeFunctionBegin( funTomTerm_MakeFunctionBegin, args, empty);
  }

  protected TomTerm makeTomTerm_MakeFunctionEnd(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_MakeFunctionEnd) {
      protoTomTerm_MakeFunctionEnd.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_MakeFunctionEnd);
    }
  }

  public TomTerm makeTomTerm_MakeFunctionEnd() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeTomTerm_MakeFunctionEnd( funTomTerm_MakeFunctionEnd, args, empty);
  }

  protected TomTerm makeTomTerm_RuleSet(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_RuleSet) {
      protoTomTerm_RuleSet.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_RuleSet);
    }
  }

  public TomTerm makeTomTerm_RuleSet(Option _option, TomList _list) {
    aterm.ATerm[] args = new aterm.ATerm[] {_option, _list};
    return makeTomTerm_RuleSet( funTomTerm_RuleSet, args, empty);
  }

  protected TomTerm makeTomTerm_RewriteRule(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_RewriteRule) {
      protoTomTerm_RewriteRule.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_RewriteRule);
    }
  }

  public TomTerm makeTomTerm_RewriteRule(TomTerm _lhs, TomTerm _rhs) {
    aterm.ATerm[] args = new aterm.ATerm[] {_lhs, _rhs};
    return makeTomTerm_RewriteRule( funTomTerm_RewriteRule, args, empty);
  }

  protected TomTerm makeTomTerm_SubjectList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_SubjectList) {
      protoTomTerm_SubjectList.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_SubjectList);
    }
  }

  public TomTerm makeTomTerm_SubjectList(TomList _list) {
    aterm.ATerm[] args = new aterm.ATerm[] {_list};
    return makeTomTerm_SubjectList( funTomTerm_SubjectList, args, empty);
  }

  protected TomTerm makeTomTerm_PatternList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_PatternList) {
      protoTomTerm_PatternList.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_PatternList);
    }
  }

  public TomTerm makeTomTerm_PatternList(TomList _list) {
    aterm.ATerm[] args = new aterm.ATerm[] {_list};
    return makeTomTerm_PatternList( funTomTerm_PatternList, args, empty);
  }

  protected TomTerm makeTomTerm_TermList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_TermList) {
      protoTomTerm_TermList.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_TermList);
    }
  }

  public TomTerm makeTomTerm_TermList(TomList _list) {
    aterm.ATerm[] args = new aterm.ATerm[] {_list};
    return makeTomTerm_TermList( funTomTerm_TermList, args, empty);
  }

  protected TomTerm makeTomTerm_Term(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Term) {
      protoTomTerm_Term.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_Term);
    }
  }

  public TomTerm makeTomTerm_Term(TomTerm _kid1) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1};
    return makeTomTerm_Term( funTomTerm_Term, args, empty);
  }

  protected TomTerm makeTomTerm_PatternAction(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_PatternAction) {
      protoTomTerm_PatternAction.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_PatternAction);
    }
  }

  public TomTerm makeTomTerm_PatternAction(TomTerm _termList, TomTerm _tom) {
    aterm.ATerm[] args = new aterm.ATerm[] {_termList, _tom};
    return makeTomTerm_PatternAction( funTomTerm_PatternAction, args, empty);
  }

  protected TomTerm makeTomTerm_GLVar(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_GLVar) {
      protoTomTerm_GLVar.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_GLVar);
    }
  }

  public TomTerm makeTomTerm_GLVar(String _strName, TomType _astType) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeAppl(makeAFun(_strName, 0, true)), _astType};
    return makeTomTerm_GLVar( funTomTerm_GLVar, args, empty);
  }

  protected TomTerm makeTomTerm_Variable(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Variable) {
      protoTomTerm_Variable.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_Variable);
    }
  }

  public TomTerm makeTomTerm_Variable(Option _option, TomName _astName, TomType _astType) {
    aterm.ATerm[] args = new aterm.ATerm[] {_option, _astName, _astType};
    return makeTomTerm_Variable( funTomTerm_Variable, args, empty);
  }

  protected TomTerm makeTomTerm_VariableStar(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_VariableStar) {
      protoTomTerm_VariableStar.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_VariableStar);
    }
  }

  public TomTerm makeTomTerm_VariableStar(Option _option, TomName _astName, TomType _astType) {
    aterm.ATerm[] args = new aterm.ATerm[] {_option, _astName, _astType};
    return makeTomTerm_VariableStar( funTomTerm_VariableStar, args, empty);
  }

  protected TomTerm makeTomTerm_Placeholder(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Placeholder) {
      protoTomTerm_Placeholder.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_Placeholder);
    }
  }

  public TomTerm makeTomTerm_Placeholder(Option _option) {
    aterm.ATerm[] args = new aterm.ATerm[] {_option};
    return makeTomTerm_Placeholder( funTomTerm_Placeholder, args, empty);
  }

  protected TomTerm makeTomTerm_UnamedVariable(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_UnamedVariable) {
      protoTomTerm_UnamedVariable.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_UnamedVariable);
    }
  }

  public TomTerm makeTomTerm_UnamedVariable(Option _option, TomType _astType) {
    aterm.ATerm[] args = new aterm.ATerm[] {_option, _astType};
    return makeTomTerm_UnamedVariable( funTomTerm_UnamedVariable, args, empty);
  }

  protected TomTerm makeTomTerm_CompiledMatch(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_CompiledMatch) {
      protoTomTerm_CompiledMatch.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_CompiledMatch);
    }
  }

  public TomTerm makeTomTerm_CompiledMatch(TomList _decls, TomList _automataList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_decls, _automataList};
    return makeTomTerm_CompiledMatch( funTomTerm_CompiledMatch, args, empty);
  }

  protected TomTerm makeTomTerm_Automata(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Automata) {
      protoTomTerm_Automata.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_Automata);
    }
  }

  public TomTerm makeTomTerm_Automata(TomList _numberList, TomList _instList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_numberList, _instList};
    return makeTomTerm_Automata( funTomTerm_Automata, args, empty);
  }

  protected TomTerm makeTomTerm_IfThenElse(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_IfThenElse) {
      protoTomTerm_IfThenElse.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_IfThenElse);
    }
  }

  public TomTerm makeTomTerm_IfThenElse(Expression _condition, TomList _succesList, TomList _failureList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_condition, _succesList, _failureList};
    return makeTomTerm_IfThenElse( funTomTerm_IfThenElse, args, empty);
  }

  protected TomTerm makeTomTerm_DoWhile(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_DoWhile) {
      protoTomTerm_DoWhile.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_DoWhile);
    }
  }

  public TomTerm makeTomTerm_DoWhile(TomList _instList, Expression _condition) {
    aterm.ATerm[] args = new aterm.ATerm[] {_instList, _condition};
    return makeTomTerm_DoWhile( funTomTerm_DoWhile, args, empty);
  }

  protected TomTerm makeTomTerm_Assign(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Assign) {
      protoTomTerm_Assign.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_Assign);
    }
  }

  public TomTerm makeTomTerm_Assign(TomTerm _kid1, Expression _source) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1, _source};
    return makeTomTerm_Assign( funTomTerm_Assign, args, empty);
  }

  protected TomTerm makeTomTerm_Declaration(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Declaration) {
      protoTomTerm_Declaration.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_Declaration);
    }
  }

  public TomTerm makeTomTerm_Declaration(TomTerm _kid1) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1};
    return makeTomTerm_Declaration( funTomTerm_Declaration, args, empty);
  }

  protected TomTerm makeTomTerm_Begin(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Begin) {
      protoTomTerm_Begin.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_Begin);
    }
  }

  public TomTerm makeTomTerm_Begin(TomTerm _number) {
    aterm.ATerm[] args = new aterm.ATerm[] {_number};
    return makeTomTerm_Begin( funTomTerm_Begin, args, empty);
  }

  protected TomTerm makeTomTerm_End(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_End) {
      protoTomTerm_End.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_End);
    }
  }

  public TomTerm makeTomTerm_End(TomTerm _number) {
    aterm.ATerm[] args = new aterm.ATerm[] {_number};
    return makeTomTerm_End( funTomTerm_End, args, empty);
  }

  protected TomTerm makeTomTerm_MatchNumber(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_MatchNumber) {
      protoTomTerm_MatchNumber.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_MatchNumber);
    }
  }

  public TomTerm makeTomTerm_MatchNumber(TomTerm _number) {
    aterm.ATerm[] args = new aterm.ATerm[] {_number};
    return makeTomTerm_MatchNumber( funTomTerm_MatchNumber, args, empty);
  }

  protected TomTerm makeTomTerm_PatternNumber(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_PatternNumber) {
      protoTomTerm_PatternNumber.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_PatternNumber);
    }
  }

  public TomTerm makeTomTerm_PatternNumber(TomTerm _number) {
    aterm.ATerm[] args = new aterm.ATerm[] {_number};
    return makeTomTerm_PatternNumber( funTomTerm_PatternNumber, args, empty);
  }

  protected TomTerm makeTomTerm_ListNumber(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_ListNumber) {
      protoTomTerm_ListNumber.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_ListNumber);
    }
  }

  public TomTerm makeTomTerm_ListNumber(TomTerm _number) {
    aterm.ATerm[] args = new aterm.ATerm[] {_number};
    return makeTomTerm_ListNumber( funTomTerm_ListNumber, args, empty);
  }

  protected TomTerm makeTomTerm_IndexNumber(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_IndexNumber) {
      protoTomTerm_IndexNumber.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_IndexNumber);
    }
  }

  public TomTerm makeTomTerm_IndexNumber(TomTerm _number) {
    aterm.ATerm[] args = new aterm.ATerm[] {_number};
    return makeTomTerm_IndexNumber( funTomTerm_IndexNumber, args, empty);
  }

  protected TomTerm makeTomTerm_AbsVar(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_AbsVar) {
      protoTomTerm_AbsVar.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_AbsVar);
    }
  }

  public TomTerm makeTomTerm_AbsVar(TomTerm _number) {
    aterm.ATerm[] args = new aterm.ATerm[] {_number};
    return makeTomTerm_AbsVar( funTomTerm_AbsVar, args, empty);
  }

  protected TomTerm makeTomTerm_RenamedVar(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_RenamedVar) {
      protoTomTerm_RenamedVar.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_RenamedVar);
    }
  }

  public TomTerm makeTomTerm_RenamedVar(TomName _astName) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName};
    return makeTomTerm_RenamedVar( funTomTerm_RenamedVar, args, empty);
  }

  protected TomTerm makeTomTerm_RuleVar(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_RuleVar) {
      protoTomTerm_RuleVar.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_RuleVar);
    }
  }

  public TomTerm makeTomTerm_RuleVar() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeTomTerm_RuleVar( funTomTerm_RuleVar, args, empty);
  }

  protected TomTerm makeTomTerm_Number(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Number) {
      protoTomTerm_Number.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_Number);
    }
  }

  public TomTerm makeTomTerm_Number(Integer _integer) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeInt(_integer.intValue())};
    return makeTomTerm_Number( funTomTerm_Number, args, empty);
  }

  protected TomTerm makeTomTerm_Increment(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Increment) {
      protoTomTerm_Increment.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_Increment);
    }
  }

  public TomTerm makeTomTerm_Increment(TomTerm _kid1) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1};
    return makeTomTerm_Increment( funTomTerm_Increment, args, empty);
  }

  protected TomTerm makeTomTerm_Action(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Action) {
      protoTomTerm_Action.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_Action);
    }
  }

  public TomTerm makeTomTerm_Action(TomList _instList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_instList};
    return makeTomTerm_Action( funTomTerm_Action, args, empty);
  }

  protected TomTerm makeTomTerm_ExitAction(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_ExitAction) {
      protoTomTerm_ExitAction.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_ExitAction);
    }
  }

  public TomTerm makeTomTerm_ExitAction(TomList _numberList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_numberList};
    return makeTomTerm_ExitAction( funTomTerm_ExitAction, args, empty);
  }

  protected TomTerm makeTomTerm_Return(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Return) {
      protoTomTerm_Return.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_Return);
    }
  }

  public TomTerm makeTomTerm_Return(TomTerm _kid1) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1};
    return makeTomTerm_Return( funTomTerm_Return, args, empty);
  }

  protected TomTerm makeTomTerm_OpenBlock(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_OpenBlock) {
      protoTomTerm_OpenBlock.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_OpenBlock);
    }
  }

  public TomTerm makeTomTerm_OpenBlock() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeTomTerm_OpenBlock( funTomTerm_OpenBlock, args, empty);
  }

  protected TomTerm makeTomTerm_CloseBlock(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_CloseBlock) {
      protoTomTerm_CloseBlock.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_CloseBlock);
    }
  }

  public TomTerm makeTomTerm_CloseBlock() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeTomTerm_CloseBlock( funTomTerm_CloseBlock, args, empty);
  }

  protected TomTerm makeTomTerm_NamedBlock(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_NamedBlock) {
      protoTomTerm_NamedBlock.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_NamedBlock);
    }
  }

  public TomTerm makeTomTerm_NamedBlock(String _blockName, TomList _instList) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeAppl(makeAFun(_blockName, 0, true)), _instList};
    return makeTomTerm_NamedBlock( funTomTerm_NamedBlock, args, empty);
  }

  protected TomTerm makeTomTerm_Line(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_Line) {
      protoTomTerm_Line.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_Line);
    }
  }

  public TomTerm makeTomTerm_Line(String _string) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeAppl(makeAFun(_string, 0, true))};
    return makeTomTerm_Line( funTomTerm_Line, args, empty);
  }

  protected TomTerm makeTomTerm_DotTerm(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomTerm_DotTerm) {
      protoTomTerm_DotTerm.initHashCode(annos,fun,args);
      return (TomTerm) build(protoTomTerm_DotTerm);
    }
  }

  public TomTerm makeTomTerm_DotTerm(TomTerm _kid1, TomTerm _kid2) {
    aterm.ATerm[] args = new aterm.ATerm[] {_kid1, _kid2};
    return makeTomTerm_DotTerm( funTomTerm_DotTerm, args, empty);
  }

  protected TomName makeTomName_Name(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomName_Name) {
      protoTomName_Name.initHashCode(annos,fun,args);
      return (TomName) build(protoTomName_Name);
    }
  }

  public TomName makeTomName_Name(String _string) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeAppl(makeAFun(_string, 0, true))};
    return makeTomName_Name( funTomName_Name, args, empty);
  }

  protected TomName makeTomName_PositionName(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomName_PositionName) {
      protoTomName_PositionName.initHashCode(annos,fun,args);
      return (TomName) build(protoTomName_PositionName);
    }
  }

  public TomName makeTomName_PositionName(TomList _numberList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_numberList};
    return makeTomName_PositionName( funTomName_PositionName, args, empty);
  }

  protected TomName makeTomName_EmptyName(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomName_EmptyName) {
      protoTomName_EmptyName.initHashCode(annos,fun,args);
      return (TomName) build(protoTomName_EmptyName);
    }
  }

  public TomName makeTomName_EmptyName() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeTomName_EmptyName( funTomName_EmptyName, args, empty);
  }

  protected TomSymbol makeTomSymbol_Symbol(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomSymbol_Symbol) {
      protoTomSymbol_Symbol.initHashCode(annos,fun,args);
      return (TomSymbol) build(protoTomSymbol_Symbol);
    }
  }

  public TomSymbol makeTomSymbol_Symbol(TomName _astName, TomType _typesToType, SlotList _slotList, Option _option, TargetLanguage _tlCode) {
    aterm.ATerm[] args = new aterm.ATerm[] {_astName, _typesToType, _slotList, _option, _tlCode};
    return makeTomSymbol_Symbol( funTomSymbol_Symbol, args, empty);
  }

  protected TomSymbolTable makeTomSymbolTable_Table(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomSymbolTable_Table) {
      protoTomSymbolTable_Table.initHashCode(annos,fun,args);
      return (TomSymbolTable) build(protoTomSymbolTable_Table);
    }
  }

  public TomSymbolTable makeTomSymbolTable_Table(TomEntryList _entryList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_entryList};
    return makeTomSymbolTable_Table( funTomSymbolTable_Table, args, empty);
  }

  protected TomEntryList makeTomEntryList_EmptyEntryList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomEntryList_EmptyEntryList) {
      protoTomEntryList_EmptyEntryList.initHashCode(annos,fun,args);
      return (TomEntryList) build(protoTomEntryList_EmptyEntryList);
    }
  }

  public TomEntryList makeTomEntryList_EmptyEntryList() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeTomEntryList_EmptyEntryList( funTomEntryList_EmptyEntryList, args, empty);
  }

  protected TomEntryList makeTomEntryList_ConsEntryList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomEntryList_ConsEntryList) {
      protoTomEntryList_ConsEntryList.initHashCode(annos,fun,args);
      return (TomEntryList) build(protoTomEntryList_ConsEntryList);
    }
  }

  public TomEntryList makeTomEntryList_ConsEntryList(TomEntry _headEntryList, TomEntryList _tailEntryList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_headEntryList, _tailEntryList};
    return makeTomEntryList_ConsEntryList( funTomEntryList_ConsEntryList, args, empty);
  }

  protected TomEntry makeTomEntry_Entry(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTomEntry_Entry) {
      protoTomEntry_Entry.initHashCode(annos,fun,args);
      return (TomEntry) build(protoTomEntry_Entry);
    }
  }

  public TomEntry makeTomEntry_Entry(String _strName, TomSymbol _astSymbol) {
    aterm.ATerm[] args = new aterm.ATerm[] {makeAppl(makeAFun(_strName, 0, true)), _astSymbol};
    return makeTomEntry_Entry( funTomEntry_Entry, args, empty);
  }

  protected SlotList makeSlotList_EmptySlotList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoSlotList_EmptySlotList) {
      protoSlotList_EmptySlotList.initHashCode(annos,fun,args);
      return (SlotList) build(protoSlotList_EmptySlotList);
    }
  }

  public SlotList makeSlotList_EmptySlotList() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeSlotList_EmptySlotList( funSlotList_EmptySlotList, args, empty);
  }

  protected SlotList makeSlotList_ConsSlotList(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoSlotList_ConsSlotList) {
      protoSlotList_ConsSlotList.initHashCode(annos,fun,args);
      return (SlotList) build(protoSlotList_ConsSlotList);
    }
  }

  public SlotList makeSlotList_ConsSlotList(PairNameDecl _headSlotList, SlotList _tailSlotList) {
    aterm.ATerm[] args = new aterm.ATerm[] {_headSlotList, _tailSlotList};
    return makeSlotList_ConsSlotList( funSlotList_ConsSlotList, args, empty);
  }

  protected PairNameDecl makePairNameDecl_Slot(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoPairNameDecl_Slot) {
      protoPairNameDecl_Slot.initHashCode(annos,fun,args);
      return (PairNameDecl) build(protoPairNameDecl_Slot);
    }
  }

  public PairNameDecl makePairNameDecl_Slot(TomName _slotName, Declaration _slotDecl) {
    aterm.ATerm[] args = new aterm.ATerm[] {_slotName, _slotDecl};
    return makePairNameDecl_Slot( funPairNameDecl_Slot, args, empty);
  }

}

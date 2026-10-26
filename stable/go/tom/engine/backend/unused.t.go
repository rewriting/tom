// tom-source-sha256: 5320ac81f91c518afbd2bc565288b7337a6679eac683d353d17ee074b4d5fed2

package backend









import (
	"strings"

	. "tom/engine/adt"
	"tom/library/aterm"
)

func tom_unused_equal_term_char(t1 rune, t2 rune) bool { return t1 == t2;  }; func tom_unused_is_sort_char(t rune) bool { return true;  }; func tom_unused_equal_term_String(t1 string, t2 string) bool { return t1 == t2;  }; func tom_unused_is_sort_String(t string) bool { return true;  }; func tom_unused_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_unused_is_sort_int(t int64) bool { return true;  }; func tom_unused_equal_term_boolean(t1 bool, t2 bool) bool { return t1 == t2;  }; func tom_unused_is_sort_boolean(t bool) bool { return true;  }; func tom_unused_equal_term_ATerm(t1  aterm.ATerm , t2  aterm.ATerm ) bool { return t1 == t2;  }; func tom_unused_is_sort_ATerm(t any) bool { return func() bool { _, ok := any(t).(aterm.ATerm); return ok }();  }; func tom_unused_equal_term_ATermList(l1  *aterm.ATermList , l2  *aterm.ATermList ) bool { return l1 == l2;  }; func tom_unused_is_sort_ATermList(t any) bool { return func() bool { _, ok := any(t).(*aterm.ATermList); return ok }();  }; func tom_unused_equal_term_AFun(t1  *aterm.AFun , t2  *aterm.AFun ) bool { return t1 == t2;  }; func tom_unused_is_sort_AFun(t any) bool { return func() bool { _, ok := any(t).(*aterm.AFun); return ok }();  }; func tom_unused_equal_term_CstProgram(t1  CstProgram , t2  CstProgram ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstProgram(t any) bool { return func() bool { _, ok := (t).(CstProgram); return ok }();  }; func tom_unused_equal_term_CstBlockList(t1  CstBlockList , t2  CstBlockList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstBlockList(t any) bool { return func() bool { _, ok := (t).(CstBlockList); return ok }();  }; func tom_unused_equal_term_CstBlock(t1  CstBlock , t2  CstBlock ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstBlock(t any) bool { return func() bool { _, ok := (t).(CstBlock); return ok }();  }; func tom_unused_equal_term_CstConstraintAction(t1  CstConstraintAction , t2  CstConstraintAction ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstConstraintAction(t any) bool { return func() bool { _, ok := (t).(CstConstraintAction); return ok }();  }; func tom_unused_equal_term_CstConstraintActionList(t1  CstConstraintActionList , t2  CstConstraintActionList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstConstraintActionList(t any) bool { return func() bool { _, ok := (t).(CstConstraintActionList); return ok }();  }; func tom_unused_equal_term_CstConstraint(t1  CstConstraint , t2  CstConstraint ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstConstraint(t any) bool { return func() bool { _, ok := (t).(CstConstraint); return ok }();  }; func tom_unused_equal_term_CstConstraintList(t1  CstConstraintList , t2  CstConstraintList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstConstraintList(t any) bool { return func() bool { _, ok := (t).(CstConstraintList); return ok }();  }; func tom_unused_equal_term_CstTerm(t1  CstTerm , t2  CstTerm ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstTerm(t any) bool { return func() bool { _, ok := (t).(CstTerm); return ok }();  }; func tom_unused_equal_term_CstTermList(t1  CstTermList , t2  CstTermList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstTermList(t any) bool { return func() bool { _, ok := (t).(CstTermList); return ok }();  }; func tom_unused_equal_term_CstType(t1  CstType , t2  CstType ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstType(t any) bool { return func() bool { _, ok := (t).(CstType); return ok }();  }; func tom_unused_equal_term_CstPattern(t1  CstPattern , t2  CstPattern ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstPattern(t any) bool { return func() bool { _, ok := (t).(CstPattern); return ok }();  }; func tom_unused_equal_term_CstSymbolList(t1  CstSymbolList , t2  CstSymbolList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstSymbolList(t any) bool { return func() bool { _, ok := (t).(CstSymbolList); return ok }();  }; func tom_unused_equal_term_CstSymbol(t1  CstSymbol , t2  CstSymbol ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstSymbol(t any) bool { return func() bool { _, ok := (t).(CstSymbol); return ok }();  }; func tom_unused_equal_term_CstTheory(t1  CstTheory , t2  CstTheory ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstTheory(t any) bool { return func() bool { _, ok := (t).(CstTheory); return ok }();  }; func tom_unused_equal_term_CstPatternList(t1  CstPatternList , t2  CstPatternList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstPatternList(t any) bool { return func() bool { _, ok := (t).(CstPatternList); return ok }();  }; func tom_unused_equal_term_CstPairPatternList(t1  CstPairPatternList , t2  CstPairPatternList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstPairPatternList(t any) bool { return func() bool { _, ok := (t).(CstPairPatternList); return ok }();  }; func tom_unused_equal_term_CstPairPattern(t1  CstPairPattern , t2  CstPairPattern ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstPairPattern(t any) bool { return func() bool { _, ok := (t).(CstPairPattern); return ok }();  }; func tom_unused_equal_term_CstOperatorList(t1  CstOperatorList , t2  CstOperatorList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstOperatorList(t any) bool { return func() bool { _, ok := (t).(CstOperatorList); return ok }();  }; func tom_unused_equal_term_CstOperator(t1  CstOperator , t2  CstOperator ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstOperator(t any) bool { return func() bool { _, ok := (t).(CstOperator); return ok }();  }; func tom_unused_equal_term_CstSlotList(t1  CstSlotList , t2  CstSlotList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstSlotList(t any) bool { return func() bool { _, ok := (t).(CstSlotList); return ok }();  }; func tom_unused_equal_term_CstSlot(t1  CstSlot , t2  CstSlot ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstSlot(t any) bool { return func() bool { _, ok := (t).(CstSlot); return ok }();  }; func tom_unused_equal_term_CstBQTerm(t1  CstBQTerm , t2  CstBQTerm ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstBQTerm(t any) bool { return func() bool { _, ok := (t).(CstBQTerm); return ok }();  }; func tom_unused_equal_term_CstPairSlotBQTerm(t1  CstPairSlotBQTerm , t2  CstPairSlotBQTerm ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstPairSlotBQTerm(t any) bool { return func() bool { _, ok := (t).(CstPairSlotBQTerm); return ok }();  }; func tom_unused_equal_term_CstBQTermList(t1  CstBQTermList , t2  CstBQTermList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstBQTermList(t any) bool { return func() bool { _, ok := (t).(CstBQTermList); return ok }();  }; func tom_unused_equal_term_CstPairSlotBQTermList(t1  CstPairSlotBQTermList , t2  CstPairSlotBQTermList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstPairSlotBQTermList(t any) bool { return func() bool { _, ok := (t).(CstPairSlotBQTermList); return ok }();  }; func tom_unused_equal_term_CstName(t1  CstName , t2  CstName ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstName(t any) bool { return func() bool { _, ok := (t).(CstName); return ok }();  }; func tom_unused_equal_term_CstNameList(t1  CstNameList , t2  CstNameList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstNameList(t any) bool { return func() bool { _, ok := (t).(CstNameList); return ok }();  }; func tom_unused_equal_term_CstOptionList(t1  CstOptionList , t2  CstOptionList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstOptionList(t any) bool { return func() bool { _, ok := (t).(CstOptionList); return ok }();  }; func tom_unused_equal_term_CstOption(t1  CstOption , t2  CstOption ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstOption(t any) bool { return func() bool { _, ok := (t).(CstOption); return ok }();  }; func tom_unused_equal_term_CstVisit(t1  CstVisit , t2  CstVisit ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstVisit(t any) bool { return func() bool { _, ok := (t).(CstVisit); return ok }();  }; func tom_unused_equal_term_CstVisitList(t1  CstVisitList , t2  CstVisitList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CstVisitList(t any) bool { return func() bool { _, ok := (t).(CstVisitList); return ok }();  }; func tom_unused_equal_term_TargetLanguage(t1  TargetLanguage , t2  TargetLanguage ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_TargetLanguage(t any) bool { return func() bool { _, ok := (t).(TargetLanguage); return ok }();  }; func tom_unused_equal_term_CodeList(t1  CodeList , t2  CodeList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CodeList(t any) bool { return func() bool { _, ok := (t).(CodeList); return ok }();  }; func tom_unused_equal_term_Code(t1  Code , t2  Code ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_Code(t any) bool { return func() bool { _, ok := (t).(Code); return ok }();  }; func tom_unused_equal_term_BQTermList(t1  BQTermList , t2  BQTermList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_BQTermList(t any) bool { return func() bool { _, ok := (t).(BQTermList); return ok }();  }; func tom_unused_equal_term_CompositeMember(t1  CompositeMember , t2  CompositeMember ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_CompositeMember(t any) bool { return func() bool { _, ok := (t).(CompositeMember); return ok }();  }; func tom_unused_equal_term_BQTerm(t1  BQTerm , t2  BQTerm ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_BQTerm(t any) bool { return func() bool { _, ok := (t).(BQTerm); return ok }();  }; func tom_unused_is_fun_sym_TL(t  TargetLanguage ) bool { return func() bool { _, ok := (t).(*TLTargetLanguage); return ok }();  }; func tom_unused_get_slot_TL_Code(t  TargetLanguage )  string  { return (t).(*TLTargetLanguage).Code;  }; func tom_unused_get_slot_TL_Start(t  TargetLanguage )  TextPosition  { return (t).(*TLTargetLanguage).Start;  }; func tom_unused_get_slot_TL_End(t  TargetLanguage )  TextPosition  { return (t).(*TLTargetLanguage).End;  }; func tom_unused_is_fun_sym_ITL(t  TargetLanguage ) bool { return func() bool { _, ok := (t).(*ITLTargetLanguage); return ok }();  }; func tom_unused_get_slot_ITL_Code(t  TargetLanguage )  string  { return (t).(*ITLTargetLanguage).Code;  }; func tom_unused_is_fun_sym_concCode(t  CodeList ) bool { return func() bool { _, ok := (t).(*ConcCodeCodeList); return ok }();  }; func tom_unused_empty_list_concCode()  CodeList  { return (EmptyConcCode()) }; func tom_unused_cons_list_concCode(e  Code , l  CodeList )  CodeList  { return (ConsConcCode(e,l)) }; func tom_unused_get_head_concCode_CodeList(l  CodeList )  Code  { return (l).GetHeadconcCode();  }; func tom_unused_get_tail_concCode_CodeList(l  CodeList )  CodeList  { return (l).GetTailconcCode();  }; func tom_unused_is_empty_concCode_CodeList(l  CodeList ) bool { return (l).IsEmptyconcCode();  }; func tom_unused_append_list_concCode(l1  CodeList , l2  CodeList )  CodeList  { if tom_unused_is_empty_concCode_CodeList(l1) { return l2 } else if tom_unused_is_empty_concCode_CodeList(l2) { return l1 } else if tom_unused_is_empty_concCode_CodeList(tom_unused_get_tail_concCode_CodeList(l1)) { return tom_unused_cons_list_concCode(tom_unused_get_head_concCode_CodeList(l1), l2) } else { return tom_unused_cons_list_concCode(tom_unused_get_head_concCode_CodeList(l1), tom_unused_append_list_concCode(tom_unused_get_tail_concCode_CodeList(l1), l2)) }; }; func tom_unused_get_slice_concCode(begin  CodeList , end  CodeList , tail  CodeList )  CodeList  { if tom_unused_equal_term_CodeList(begin, end) { return tail } else if tom_unused_equal_term_CodeList(end, tail) && (tom_unused_is_empty_concCode_CodeList(end) || tom_unused_equal_term_CodeList(end, tom_unused_empty_list_concCode())) { return begin }; return tom_unused_cons_list_concCode(tom_unused_get_head_concCode_CodeList(begin), tom_unused_get_slice_concCode(tom_unused_get_tail_concCode_CodeList(begin), end, tail)) }; func tom_unused_is_fun_sym_TargetLanguageToCode(t  Code ) bool { return func() bool { _, ok := (t).(*TargetLanguageToCodeCode); return ok }();  }; func tom_unused_get_slot_TargetLanguageToCode_Tl(t  Code )  TargetLanguage  { return (t).(*TargetLanguageToCodeCode).Tl;  }; func tom_unused_is_fun_sym_InstructionToCode(t  Code ) bool { return func() bool { _, ok := (t).(*InstructionToCodeCode); return ok }();  }; func tom_unused_get_slot_InstructionToCode_AstInstruction(t  Code )  Instruction  { return (t).(*InstructionToCodeCode).AstInstruction;  }; func tom_unused_is_fun_sym_BQTermToCode(t  Code ) bool { return func() bool { _, ok := (t).(*BQTermToCodeCode); return ok }();  }; func tom_unused_get_slot_BQTermToCode_Bq(t  Code )  BQTerm  { return (t).(*BQTermToCodeCode).Bq;  }; func tom_unused_is_fun_sym_Tom(t  Code ) bool { return func() bool { _, ok := (t).(*TomCode); return ok }();  }; func tom_unused_get_slot_Tom_CodeList(t  Code )  CodeList  { return (t).(*TomCode).CodeList;  }; func tom_unused_is_fun_sym_TomInclude(t  Code ) bool { return func() bool { _, ok := (t).(*TomIncludeCode); return ok }();  }; func tom_unused_get_slot_TomInclude_CodeList(t  Code )  CodeList  { return (t).(*TomIncludeCode).CodeList;  }; func tom_unused_is_fun_sym_concBQTerm(t  BQTermList ) bool { return func() bool { _, ok := (t).(*ConcBQTermBQTermList); return ok }();  }; func tom_unused_empty_list_concBQTerm()  BQTermList  { return (EmptyConcBQTerm()) }; func tom_unused_cons_list_concBQTerm(e  BQTerm , l  BQTermList )  BQTermList  { return (ConsConcBQTerm(e,l)) }; func tom_unused_get_head_concBQTerm_BQTermList(l  BQTermList )  BQTerm  { return (l).GetHeadconcBQTerm();  }; func tom_unused_get_tail_concBQTerm_BQTermList(l  BQTermList )  BQTermList  { return (l).GetTailconcBQTerm();  }; func tom_unused_is_empty_concBQTerm_BQTermList(l  BQTermList ) bool { return (l).IsEmptyconcBQTerm();  }; func tom_unused_append_list_concBQTerm(l1  BQTermList , l2  BQTermList )  BQTermList  { if tom_unused_is_empty_concBQTerm_BQTermList(l1) { return l2 } else if tom_unused_is_empty_concBQTerm_BQTermList(l2) { return l1 } else if tom_unused_is_empty_concBQTerm_BQTermList(tom_unused_get_tail_concBQTerm_BQTermList(l1)) { return tom_unused_cons_list_concBQTerm(tom_unused_get_head_concBQTerm_BQTermList(l1), l2) } else { return tom_unused_cons_list_concBQTerm(tom_unused_get_head_concBQTerm_BQTermList(l1), tom_unused_append_list_concBQTerm(tom_unused_get_tail_concBQTerm_BQTermList(l1), l2)) }; }; func tom_unused_get_slice_concBQTerm(begin  BQTermList , end  BQTermList , tail  BQTermList )  BQTermList  { if tom_unused_equal_term_BQTermList(begin, end) { return tail } else if tom_unused_equal_term_BQTermList(end, tail) && (tom_unused_is_empty_concBQTerm_BQTermList(end) || tom_unused_equal_term_BQTermList(end, tom_unused_empty_list_concBQTerm())) { return begin }; return tom_unused_cons_list_concBQTerm(tom_unused_get_head_concBQTerm_BQTermList(begin), tom_unused_get_slice_concBQTerm(tom_unused_get_tail_concBQTerm_BQTermList(begin), end, tail)) }; func tom_unused_is_fun_sym_CompositeBQTerm(t  CompositeMember ) bool { return func() bool { _, ok := (t).(*CompositeBQTermCompositeMember); return ok }();  }; func tom_unused_get_slot_CompositeBQTerm_term(t  CompositeMember )  BQTerm  { return (t).(*CompositeBQTermCompositeMember).Term;  }; func tom_unused_is_fun_sym_CompositeTL(t  CompositeMember ) bool { return func() bool { _, ok := (t).(*CompositeTLCompositeMember); return ok }();  }; func tom_unused_get_slot_CompositeTL_Tl(t  CompositeMember )  TargetLanguage  { return (t).(*CompositeTLCompositeMember).Tl;  }; func tom_unused_is_fun_sym_BQVariable(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BQVariableBQTerm); return ok }();  }; func tom_unused_get_slot_BQVariable_Options(t  BQTerm )  OptionList  { return (t).(*BQVariableBQTerm).Options;  }; func tom_unused_get_slot_BQVariable_AstName(t  BQTerm )  TomName  { return (t).(*BQVariableBQTerm).AstName;  }; func tom_unused_get_slot_BQVariable_AstType(t  BQTerm )  TomType  { return (t).(*BQVariableBQTerm).AstType;  }; func tom_unused_is_fun_sym_BQVariableStar(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BQVariableStarBQTerm); return ok }();  }; func tom_unused_get_slot_BQVariableStar_Options(t  BQTerm )  OptionList  { return (t).(*BQVariableStarBQTerm).Options;  }; func tom_unused_get_slot_BQVariableStar_AstName(t  BQTerm )  TomName  { return (t).(*BQVariableStarBQTerm).AstName;  }; func tom_unused_get_slot_BQVariableStar_AstType(t  BQTerm )  TomType  { return (t).(*BQVariableStarBQTerm).AstType;  }; func tom_unused_is_fun_sym_Composite(t  BQTerm ) bool { return func() bool { _, ok := (t).(*CompositeBQTerm); return ok }();  }; func tom_unused_empty_list_Composite()  BQTerm  { return (EmptyComposite()) }; func tom_unused_cons_list_Composite(e  CompositeMember , l  BQTerm )  BQTerm  { return (ConsComposite(e,l)) }; func tom_unused_get_head_Composite_BQTerm(l  BQTerm )  CompositeMember  { return (l).GetHeadComposite();  }; func tom_unused_get_tail_Composite_BQTerm(l  BQTerm )  BQTerm  { return (l).GetTailComposite();  }; func tom_unused_is_empty_Composite_BQTerm(l  BQTerm ) bool { return (l).IsEmptyComposite();  }; func tom_unused_append_list_Composite(l1  BQTerm , l2  BQTerm )  BQTerm  { if tom_unused_is_empty_Composite_BQTerm(l1) { return l2 } else if tom_unused_is_empty_Composite_BQTerm(l2) { return l1 } else if tom_unused_is_empty_Composite_BQTerm(tom_unused_get_tail_Composite_BQTerm(l1)) { return tom_unused_cons_list_Composite(tom_unused_get_head_Composite_BQTerm(l1), l2) } else { return tom_unused_cons_list_Composite(tom_unused_get_head_Composite_BQTerm(l1), tom_unused_append_list_Composite(tom_unused_get_tail_Composite_BQTerm(l1), l2)) }; }; func tom_unused_get_slice_Composite(begin  BQTerm , end  BQTerm , tail  BQTerm )  BQTerm  { if tom_unused_equal_term_BQTerm(begin, end) { return tail } else if tom_unused_equal_term_BQTerm(end, tail) && (tom_unused_is_empty_Composite_BQTerm(end) || tom_unused_equal_term_BQTerm(end, tom_unused_empty_list_Composite())) { return begin }; return tom_unused_cons_list_Composite(tom_unused_get_head_Composite_BQTerm(begin), tom_unused_get_slice_Composite(tom_unused_get_tail_Composite_BQTerm(begin), end, tail)) }; func tom_unused_is_fun_sym_FunctionCall(t  BQTerm ) bool { return func() bool { _, ok := (t).(*FunctionCallBQTerm); return ok }();  }; func tom_unused_get_slot_FunctionCall_AstName(t  BQTerm )  TomName  { return (t).(*FunctionCallBQTerm).AstName;  }; func tom_unused_get_slot_FunctionCall_AstType(t  BQTerm )  TomType  { return (t).(*FunctionCallBQTerm).AstType;  }; func tom_unused_get_slot_FunctionCall_Args(t  BQTerm )  BQTermList  { return (t).(*FunctionCallBQTerm).Args;  }; func tom_unused_is_fun_sym_BuildTerm(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BuildTermBQTerm); return ok }();  }; func tom_unused_get_slot_BuildTerm_AstName(t  BQTerm )  TomName  { return (t).(*BuildTermBQTerm).AstName;  }; func tom_unused_get_slot_BuildTerm_Args(t  BQTerm )  BQTermList  { return (t).(*BuildTermBQTerm).Args;  }; func tom_unused_get_slot_BuildTerm_ModuleName(t  BQTerm )  string  { return (t).(*BuildTermBQTerm).ModuleName;  }; func tom_unused_is_fun_sym_BuildConsList(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BuildConsListBQTerm); return ok }();  }; func tom_unused_get_slot_BuildConsList_AstName(t  BQTerm )  TomName  { return (t).(*BuildConsListBQTerm).AstName;  }; func tom_unused_get_slot_BuildConsList_HeadTerm(t  BQTerm )  BQTerm  { return (t).(*BuildConsListBQTerm).HeadTerm;  }; func tom_unused_get_slot_BuildConsList_TailTerm(t  BQTerm )  BQTerm  { return (t).(*BuildConsListBQTerm).TailTerm;  }; func tom_unused_is_fun_sym_BuildAppendList(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BuildAppendListBQTerm); return ok }();  }; func tom_unused_get_slot_BuildAppendList_AstName(t  BQTerm )  TomName  { return (t).(*BuildAppendListBQTerm).AstName;  }; func tom_unused_get_slot_BuildAppendList_HeadTerm(t  BQTerm )  BQTerm  { return (t).(*BuildAppendListBQTerm).HeadTerm;  }; func tom_unused_get_slot_BuildAppendList_TailTerm(t  BQTerm )  BQTerm  { return (t).(*BuildAppendListBQTerm).TailTerm;  }; func tom_unused_is_fun_sym_BuildEmptyArray(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BuildEmptyArrayBQTerm); return ok }();  }; func tom_unused_get_slot_BuildEmptyArray_AstName(t  BQTerm )  TomName  { return (t).(*BuildEmptyArrayBQTerm).AstName;  }; func tom_unused_get_slot_BuildEmptyArray_Size(t  BQTerm )  BQTerm  { return (t).(*BuildEmptyArrayBQTerm).Size;  }; func tom_unused_is_fun_sym_BuildConsArray(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BuildConsArrayBQTerm); return ok }();  }; func tom_unused_get_slot_BuildConsArray_AstName(t  BQTerm )  TomName  { return (t).(*BuildConsArrayBQTerm).AstName;  }; func tom_unused_get_slot_BuildConsArray_HeadTerm(t  BQTerm )  BQTerm  { return (t).(*BuildConsArrayBQTerm).HeadTerm;  }; func tom_unused_get_slot_BuildConsArray_TailTerm(t  BQTerm )  BQTerm  { return (t).(*BuildConsArrayBQTerm).TailTerm;  }; func tom_unused_is_fun_sym_BuildAppendArray(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BuildAppendArrayBQTerm); return ok }();  }; func tom_unused_get_slot_BuildAppendArray_AstName(t  BQTerm )  TomName  { return (t).(*BuildAppendArrayBQTerm).AstName;  }; func tom_unused_get_slot_BuildAppendArray_HeadTerm(t  BQTerm )  BQTerm  { return (t).(*BuildAppendArrayBQTerm).HeadTerm;  }; func tom_unused_get_slot_BuildAppendArray_TailTerm(t  BQTerm )  BQTerm  { return (t).(*BuildAppendArrayBQTerm).TailTerm;  }; func tom_unused_is_fun_sym_ExpressionToBQTerm(t  BQTerm ) bool { return func() bool { _, ok := (t).(*ExpressionToBQTermBQTerm); return ok }();  }; func tom_unused_get_slot_ExpressionToBQTerm_Exp(t  BQTerm )  Expression  { return (t).(*ExpressionToBQTermBQTerm).Exp;  }; func tom_unused_equal_term_Symbol(t1  Symbol , t2  Symbol ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_Symbol(t any) bool { return func() bool { _, ok := (t).(Symbol); return ok }();  }; func tom_unused_equal_term_Variable(t1  Variable , t2  Variable ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_Variable(t any) bool { return func() bool { _, ok := (t).(Variable); return ok }();  }; func tom_unused_equal_term_AbsTerm(t1  AbsTerm , t2  AbsTerm ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_AbsTerm(t any) bool { return func() bool { _, ok := (t).(AbsTerm); return ok }();  }; func tom_unused_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_unused_equal_term_Expr(t1  Expr , t2  Expr ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_Expr(t any) bool { return func() bool { _, ok := (t).(Expr); return ok }();  }; func tom_unused_equal_term_Instr(t1  Instr , t2  Instr ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_Instr(t any) bool { return func() bool { _, ok := (t).(Instr); return ok }();  }; func tom_unused_equal_term_InstrList(t1  InstrList , t2  InstrList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_InstrList(t any) bool { return func() bool { _, ok := (t).(InstrList); return ok }();  }; func tom_unused_equal_term_Substitution(t1  Substitution , t2  Substitution ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_Substitution(t any) bool { return func() bool { _, ok := (t).(Substitution); return ok }();  }; func tom_unused_equal_term_SubstitutionList(t1  SubstitutionList , t2  SubstitutionList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_SubstitutionList(t any) bool { return func() bool { _, ok := (t).(SubstitutionList); return ok }();  }; func tom_unused_equal_term_Environment(t1  Environment , t2  Environment ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_Environment(t any) bool { return func() bool { _, ok := (t).(Environment); return ok }();  }; func tom_unused_equal_term_Seq(t1  Seq , t2  Seq ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_Seq(t any) bool { return func() bool { _, ok := (t).(Seq); return ok }();  }; func tom_unused_equal_term_TermList(t1  TermList , t2  TermList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_TermList(t any) bool { return func() bool { _, ok := (t).(TermList); return ok }();  }; func tom_unused_equal_term_ExprList(t1  ExprList , t2  ExprList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_ExprList(t any) bool { return func() bool { _, ok := (t).(ExprList); return ok }();  }; func tom_unused_equal_term_Deriv(t1  Deriv , t2  Deriv ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_Deriv(t any) bool { return func() bool { _, ok := (t).(Deriv); return ok }();  }; func tom_unused_equal_term_DerivTree(t1  DerivTree , t2  DerivTree ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_DerivTree(t any) bool { return func() bool { _, ok := (t).(DerivTree); return ok }();  }; func tom_unused_equal_term_ElementaryTheory(t1  ElementaryTheory , t2  ElementaryTheory ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_ElementaryTheory(t any) bool { return func() bool { _, ok := (t).(ElementaryTheory); return ok }();  }; func tom_unused_equal_term_Theory(t1  Theory , t2  Theory ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_Theory(t any) bool { return func() bool { _, ok := (t).(Theory); return ok }();  }; func tom_unused_equal_term_Constraint(t1  Constraint , t2  Constraint ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_Constraint(t any) bool { return func() bool { _, ok := (t).(Constraint); return ok }();  }; func tom_unused_equal_term_NumericConstraintType(t1  NumericConstraintType , t2  NumericConstraintType ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_NumericConstraintType(t any) bool { return func() bool { _, ok := (t).(NumericConstraintType); return ok }();  }; func tom_unused_equal_term_ConstraintList(t1  ConstraintList , t2  ConstraintList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_ConstraintList(t any) bool { return func() bool { _, ok := (t).(ConstraintList); return ok }();  }; func tom_unused_equal_term_Declaration(t1  Declaration , t2  Declaration ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_Declaration(t any) bool { return func() bool { _, ok := (t).(Declaration); return ok }();  }; func tom_unused_equal_term_DeclarationList(t1  DeclarationList , t2  DeclarationList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_DeclarationList(t any) bool { return func() bool { _, ok := (t).(DeclarationList); return ok }();  }; func tom_unused_equal_term_Expression(t1  Expression , t2  Expression ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_Expression(t any) bool { return func() bool { _, ok := (t).(Expression); return ok }();  }; func tom_unused_is_fun_sym_BQTermToExpression(t  Expression ) bool { return func() bool { _, ok := (t).(*BQTermToExpressionExpression); return ok }();  }; func tom_unused_get_slot_BQTermToExpression_AstTerm(t  Expression )  BQTerm  { return (t).(*BQTermToExpressionExpression).AstTerm;  }; func tom_unused_is_fun_sym_TomInstructionToExpression(t  Expression ) bool { return func() bool { _, ok := (t).(*TomInstructionToExpressionExpression); return ok }();  }; func tom_unused_get_slot_TomInstructionToExpression_Instruction(t  Expression )  Instruction  { return (t).(*TomInstructionToExpressionExpression).Instruction;  }; func tom_unused_is_fun_sym_Negation(t  Expression ) bool { return func() bool { _, ok := (t).(*NegationExpression); return ok }();  }; func tom_unused_get_slot_Negation_Arg(t  Expression )  Expression  { return (t).(*NegationExpression).Arg;  }; func tom_unused_is_fun_sym_And(t  Expression ) bool { return func() bool { _, ok := (t).(*AndExpression); return ok }();  }; func tom_unused_get_slot_And_Arg1(t  Expression )  Expression  { return (t).(*AndExpression).Arg1;  }; func tom_unused_get_slot_And_Arg2(t  Expression )  Expression  { return (t).(*AndExpression).Arg2;  }; func tom_unused_is_fun_sym_Or(t  Expression ) bool { return func() bool { _, ok := (t).(*OrExpression); return ok }();  }; func tom_unused_get_slot_Or_Arg1(t  Expression )  Expression  { return (t).(*OrExpression).Arg1;  }; func tom_unused_get_slot_Or_Arg2(t  Expression )  Expression  { return (t).(*OrExpression).Arg2;  }; func tom_unused_is_fun_sym_GreaterThan(t  Expression ) bool { return func() bool { _, ok := (t).(*GreaterThanExpression); return ok }();  }; func tom_unused_get_slot_GreaterThan_Arg1(t  Expression )  Expression  { return (t).(*GreaterThanExpression).Arg1;  }; func tom_unused_get_slot_GreaterThan_Arg2(t  Expression )  Expression  { return (t).(*GreaterThanExpression).Arg2;  }; func tom_unused_is_fun_sym_GreaterOrEqualThan(t  Expression ) bool { return func() bool { _, ok := (t).(*GreaterOrEqualThanExpression); return ok }();  }; func tom_unused_get_slot_GreaterOrEqualThan_Arg1(t  Expression )  Expression  { return (t).(*GreaterOrEqualThanExpression).Arg1;  }; func tom_unused_get_slot_GreaterOrEqualThan_Arg2(t  Expression )  Expression  { return (t).(*GreaterOrEqualThanExpression).Arg2;  }; func tom_unused_is_fun_sym_LessThan(t  Expression ) bool { return func() bool { _, ok := (t).(*LessThanExpression); return ok }();  }; func tom_unused_get_slot_LessThan_Arg1(t  Expression )  Expression  { return (t).(*LessThanExpression).Arg1;  }; func tom_unused_get_slot_LessThan_Arg2(t  Expression )  Expression  { return (t).(*LessThanExpression).Arg2;  }; func tom_unused_is_fun_sym_LessOrEqualThan(t  Expression ) bool { return func() bool { _, ok := (t).(*LessOrEqualThanExpression); return ok }();  }; func tom_unused_get_slot_LessOrEqualThan_Arg1(t  Expression )  Expression  { return (t).(*LessOrEqualThanExpression).Arg1;  }; func tom_unused_get_slot_LessOrEqualThan_Arg2(t  Expression )  Expression  { return (t).(*LessOrEqualThanExpression).Arg2;  }; func tom_unused_is_fun_sym_Conditional(t  Expression ) bool { return func() bool { _, ok := (t).(*ConditionalExpression); return ok }();  }; func tom_unused_get_slot_Conditional_Cond(t  Expression )  Expression  { return (t).(*ConditionalExpression).Cond;  }; func tom_unused_get_slot_Conditional_Then(t  Expression )  Expression  { return (t).(*ConditionalExpression).Then;  }; func tom_unused_get_slot_Conditional_Else(t  Expression )  Expression  { return (t).(*ConditionalExpression).Else;  }; func tom_unused_is_fun_sym_EqualTerm(t  Expression ) bool { return func() bool { _, ok := (t).(*EqualTermExpression); return ok }();  }; func tom_unused_get_slot_EqualTerm_TomType(t  Expression )  TomType  { return (t).(*EqualTermExpression).TomType;  }; func tom_unused_get_slot_EqualTerm_Kid1(t  Expression )  BQTerm  { return (t).(*EqualTermExpression).Kid1;  }; func tom_unused_get_slot_EqualTerm_Kid2(t  Expression )  TomTerm  { return (t).(*EqualTermExpression).Kid2;  }; func tom_unused_is_fun_sym_EqualBQTerm(t  Expression ) bool { return func() bool { _, ok := (t).(*EqualBQTermExpression); return ok }();  }; func tom_unused_get_slot_EqualBQTerm_TomType(t  Expression )  TomType  { return (t).(*EqualBQTermExpression).TomType;  }; func tom_unused_get_slot_EqualBQTerm_t1(t  Expression )  BQTerm  { return (t).(*EqualBQTermExpression).T1;  }; func tom_unused_get_slot_EqualBQTerm_t2(t  Expression )  BQTerm  { return (t).(*EqualBQTermExpression).T2;  }; func tom_unused_is_fun_sym_Cast(t  Expression ) bool { return func() bool { _, ok := (t).(*CastExpression); return ok }();  }; func tom_unused_get_slot_Cast_AstType(t  Expression )  TomType  { return (t).(*CastExpression).AstType;  }; func tom_unused_get_slot_Cast_Source(t  Expression )  Expression  { return (t).(*CastExpression).Source;  }; func tom_unused_is_fun_sym_GetSlot(t  Expression ) bool { return func() bool { _, ok := (t).(*GetSlotExpression); return ok }();  }; func tom_unused_get_slot_GetSlot_Codomain(t  Expression )  TomType  { return (t).(*GetSlotExpression).Codomain;  }; func tom_unused_get_slot_GetSlot_AstName(t  Expression )  TomName  { return (t).(*GetSlotExpression).AstName;  }; func tom_unused_get_slot_GetSlot_SlotNameString(t  Expression )  string  { return (t).(*GetSlotExpression).SlotNameString;  }; func tom_unused_get_slot_GetSlot_Variable(t  Expression )  BQTerm  { return (t).(*GetSlotExpression).Variable;  }; func tom_unused_is_fun_sym_IsFsym(t  Expression ) bool { return func() bool { _, ok := (t).(*IsFsymExpression); return ok }();  }; func tom_unused_get_slot_IsFsym_AstName(t  Expression )  TomName  { return (t).(*IsFsymExpression).AstName;  }; func tom_unused_get_slot_IsFsym_Variable(t  Expression )  BQTerm  { return (t).(*IsFsymExpression).Variable;  }; func tom_unused_is_fun_sym_GetHead(t  Expression ) bool { return func() bool { _, ok := (t).(*GetHeadExpression); return ok }();  }; func tom_unused_get_slot_GetHead_Opname(t  Expression )  TomName  { return (t).(*GetHeadExpression).Opname;  }; func tom_unused_get_slot_GetHead_Codomain(t  Expression )  TomType  { return (t).(*GetHeadExpression).Codomain;  }; func tom_unused_get_slot_GetHead_Variable(t  Expression )  BQTerm  { return (t).(*GetHeadExpression).Variable;  }; func tom_unused_is_fun_sym_GetTail(t  Expression ) bool { return func() bool { _, ok := (t).(*GetTailExpression); return ok }();  }; func tom_unused_get_slot_GetTail_Opname(t  Expression )  TomName  { return (t).(*GetTailExpression).Opname;  }; func tom_unused_get_slot_GetTail_Variable(t  Expression )  BQTerm  { return (t).(*GetTailExpression).Variable;  }; func tom_unused_is_fun_sym_IsEmptyList(t  Expression ) bool { return func() bool { _, ok := (t).(*IsEmptyListExpression); return ok }();  }; func tom_unused_get_slot_IsEmptyList_Opname(t  Expression )  TomName  { return (t).(*IsEmptyListExpression).Opname;  }; func tom_unused_get_slot_IsEmptyList_Variable(t  Expression )  BQTerm  { return (t).(*IsEmptyListExpression).Variable;  }; func tom_unused_is_fun_sym_IsEmptyArray(t  Expression ) bool { return func() bool { _, ok := (t).(*IsEmptyArrayExpression); return ok }();  }; func tom_unused_get_slot_IsEmptyArray_Opname(t  Expression )  TomName  { return (t).(*IsEmptyArrayExpression).Opname;  }; func tom_unused_get_slot_IsEmptyArray_Variable(t  Expression )  BQTerm  { return (t).(*IsEmptyArrayExpression).Variable;  }; func tom_unused_get_slot_IsEmptyArray_Index(t  Expression )  BQTerm  { return (t).(*IsEmptyArrayExpression).Index;  }; func tom_unused_is_fun_sym_AddOne(t  Expression ) bool { return func() bool { _, ok := (t).(*AddOneExpression); return ok }();  }; func tom_unused_get_slot_AddOne_Variable(t  Expression )  BQTerm  { return (t).(*AddOneExpression).Variable;  }; func tom_unused_is_fun_sym_SubstractOne(t  Expression ) bool { return func() bool { _, ok := (t).(*SubstractOneExpression); return ok }();  }; func tom_unused_get_slot_SubstractOne_Variable(t  Expression )  BQTerm  { return (t).(*SubstractOneExpression).Variable;  }; func tom_unused_is_fun_sym_Substract(t  Expression ) bool { return func() bool { _, ok := (t).(*SubstractExpression); return ok }();  }; func tom_unused_get_slot_Substract_Term1(t  Expression )  BQTerm  { return (t).(*SubstractExpression).Term1;  }; func tom_unused_get_slot_Substract_Term2(t  Expression )  BQTerm  { return (t).(*SubstractExpression).Term2;  }; func tom_unused_is_fun_sym_GetSize(t  Expression ) bool { return func() bool { _, ok := (t).(*GetSizeExpression); return ok }();  }; func tom_unused_get_slot_GetSize_Opname(t  Expression )  TomName  { return (t).(*GetSizeExpression).Opname;  }; func tom_unused_get_slot_GetSize_Variable(t  Expression )  BQTerm  { return (t).(*GetSizeExpression).Variable;  }; func tom_unused_is_fun_sym_GetElement(t  Expression ) bool { return func() bool { _, ok := (t).(*GetElementExpression); return ok }();  }; func tom_unused_get_slot_GetElement_Opname(t  Expression )  TomName  { return (t).(*GetElementExpression).Opname;  }; func tom_unused_get_slot_GetElement_Codomain(t  Expression )  TomType  { return (t).(*GetElementExpression).Codomain;  }; func tom_unused_get_slot_GetElement_Variable(t  Expression )  BQTerm  { return (t).(*GetElementExpression).Variable;  }; func tom_unused_get_slot_GetElement_Index(t  Expression )  BQTerm  { return (t).(*GetElementExpression).Index;  }; func tom_unused_is_fun_sym_GetSliceList(t  Expression ) bool { return func() bool { _, ok := (t).(*GetSliceListExpression); return ok }();  }; func tom_unused_get_slot_GetSliceList_AstName(t  Expression )  TomName  { return (t).(*GetSliceListExpression).AstName;  }; func tom_unused_get_slot_GetSliceList_VariableBeginAST(t  Expression )  BQTerm  { return (t).(*GetSliceListExpression).VariableBeginAST;  }; func tom_unused_get_slot_GetSliceList_VariableEndAST(t  Expression )  BQTerm  { return (t).(*GetSliceListExpression).VariableEndAST;  }; func tom_unused_get_slot_GetSliceList_Tail(t  Expression )  BQTerm  { return (t).(*GetSliceListExpression).Tail;  }; func tom_unused_is_fun_sym_GetSliceArray(t  Expression ) bool { return func() bool { _, ok := (t).(*GetSliceArrayExpression); return ok }();  }; func tom_unused_get_slot_GetSliceArray_AstName(t  Expression )  TomName  { return (t).(*GetSliceArrayExpression).AstName;  }; func tom_unused_get_slot_GetSliceArray_SubjectListName(t  Expression )  BQTerm  { return (t).(*GetSliceArrayExpression).SubjectListName;  }; func tom_unused_get_slot_GetSliceArray_VariableBeginAST(t  Expression )  BQTerm  { return (t).(*GetSliceArrayExpression).VariableBeginAST;  }; func tom_unused_get_slot_GetSliceArray_VariableEndAST(t  Expression )  BQTerm  { return (t).(*GetSliceArrayExpression).VariableEndAST;  }; func tom_unused_is_fun_sym_Code(t  Expression ) bool { return func() bool { _, ok := (t).(*CodeExpression); return ok }();  }; func tom_unused_get_slot_Code_Code(t  Expression )  string  { return (t).(*CodeExpression).Code;  }; func tom_unused_equal_term_Instruction(t1  Instruction , t2  Instruction ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_Instruction(t any) bool { return func() bool { _, ok := (t).(Instruction); return ok }();  }; func tom_unused_equal_term_RefClassTracelinkInstruction(t1  RefClassTracelinkInstruction , t2  RefClassTracelinkInstruction ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_RefClassTracelinkInstruction(t any) bool { return func() bool { _, ok := (t).(RefClassTracelinkInstruction); return ok }();  }; func tom_unused_equal_term_RefClassTracelinkInstructionList(t1  RefClassTracelinkInstructionList , t2  RefClassTracelinkInstructionList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_RefClassTracelinkInstructionList(t any) bool { return func() bool { _, ok := (t).(RefClassTracelinkInstructionList); return ok }();  }; func tom_unused_equal_term_ConstraintInstruction(t1  ConstraintInstruction , t2  ConstraintInstruction ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_ConstraintInstruction(t any) bool { return func() bool { _, ok := (t).(ConstraintInstruction); return ok }();  }; func tom_unused_equal_term_RuleInstruction(t1  RuleInstruction , t2  RuleInstruction ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_RuleInstruction(t any) bool { return func() bool { _, ok := (t).(RuleInstruction); return ok }();  }; func tom_unused_equal_term_RuleInstructionList(t1  RuleInstructionList , t2  RuleInstructionList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_RuleInstructionList(t any) bool { return func() bool { _, ok := (t).(RuleInstructionList); return ok }();  }; func tom_unused_equal_term_ConstraintInstructionList(t1  ConstraintInstructionList , t2  ConstraintInstructionList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_ConstraintInstructionList(t any) bool { return func() bool { _, ok := (t).(ConstraintInstructionList); return ok }();  }; func tom_unused_equal_term_InstructionList(t1  InstructionList , t2  InstructionList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_InstructionList(t any) bool { return func() bool { _, ok := (t).(InstructionList); return ok }();  }; func tom_unused_is_fun_sym_BQTermToInstruction(t  Instruction ) bool { return func() bool { _, ok := (t).(*BQTermToInstructionInstruction); return ok }();  }; func tom_unused_get_slot_BQTermToInstruction_Tom(t  Instruction )  BQTerm  { return (t).(*BQTermToInstructionInstruction).Tom;  }; func tom_unused_is_fun_sym_ExpressionToInstruction(t  Instruction ) bool { return func() bool { _, ok := (t).(*ExpressionToInstructionInstruction); return ok }();  }; func tom_unused_get_slot_ExpressionToInstruction_Expr(t  Instruction )  Expression  { return (t).(*ExpressionToInstructionInstruction).Expr;  }; func tom_unused_is_fun_sym_CodeToInstruction(t  Instruction ) bool { return func() bool { _, ok := (t).(*CodeToInstructionInstruction); return ok }();  }; func tom_unused_get_slot_CodeToInstruction_Code(t  Instruction )  Code  { return (t).(*CodeToInstructionInstruction).Code;  }; func tom_unused_is_fun_sym_If(t  Instruction ) bool { return func() bool { _, ok := (t).(*IfInstruction); return ok }();  }; func tom_unused_get_slot_If_Condition(t  Instruction )  Expression  { return (t).(*IfInstruction).Condition;  }; func tom_unused_get_slot_If_SuccesInst(t  Instruction )  Instruction  { return (t).(*IfInstruction).SuccesInst;  }; func tom_unused_get_slot_If_FailureInst(t  Instruction )  Instruction  { return (t).(*IfInstruction).FailureInst;  }; func tom_unused_is_fun_sym_DoWhile(t  Instruction ) bool { return func() bool { _, ok := (t).(*DoWhileInstruction); return ok }();  }; func tom_unused_get_slot_DoWhile_DoInst(t  Instruction )  Instruction  { return (t).(*DoWhileInstruction).DoInst;  }; func tom_unused_get_slot_DoWhile_Condition(t  Instruction )  Expression  { return (t).(*DoWhileInstruction).Condition;  }; func tom_unused_is_fun_sym_WhileDo(t  Instruction ) bool { return func() bool { _, ok := (t).(*WhileDoInstruction); return ok }();  }; func tom_unused_get_slot_WhileDo_Condition(t  Instruction )  Expression  { return (t).(*WhileDoInstruction).Condition;  }; func tom_unused_get_slot_WhileDo_DoInst(t  Instruction )  Instruction  { return (t).(*WhileDoInstruction).DoInst;  }; func tom_unused_is_fun_sym_Let(t  Instruction ) bool { return func() bool { _, ok := (t).(*LetInstruction); return ok }();  }; func tom_unused_get_slot_Let_Variable(t  Instruction )  BQTerm  { return (t).(*LetInstruction).Variable;  }; func tom_unused_get_slot_Let_Source(t  Instruction )  Expression  { return (t).(*LetInstruction).Source;  }; func tom_unused_get_slot_Let_AstInstruction(t  Instruction )  Instruction  { return (t).(*LetInstruction).AstInstruction;  }; func tom_unused_is_fun_sym_LetRef(t  Instruction ) bool { return func() bool { _, ok := (t).(*LetRefInstruction); return ok }();  }; func tom_unused_get_slot_LetRef_Variable(t  Instruction )  BQTerm  { return (t).(*LetRefInstruction).Variable;  }; func tom_unused_get_slot_LetRef_Source(t  Instruction )  Expression  { return (t).(*LetRefInstruction).Source;  }; func tom_unused_get_slot_LetRef_AstInstruction(t  Instruction )  Instruction  { return (t).(*LetRefInstruction).AstInstruction;  }; func tom_unused_is_fun_sym_Assign(t  Instruction ) bool { return func() bool { _, ok := (t).(*AssignInstruction); return ok }();  }; func tom_unused_get_slot_Assign_Variable(t  Instruction )  BQTerm  { return (t).(*AssignInstruction).Variable;  }; func tom_unused_get_slot_Assign_Source(t  Instruction )  Expression  { return (t).(*AssignInstruction).Source;  }; func tom_unused_is_fun_sym_AssignArray(t  Instruction ) bool { return func() bool { _, ok := (t).(*AssignArrayInstruction); return ok }();  }; func tom_unused_get_slot_AssignArray_Variable(t  Instruction )  BQTerm  { return (t).(*AssignArrayInstruction).Variable;  }; func tom_unused_get_slot_AssignArray_Index(t  Instruction )  BQTerm  { return (t).(*AssignArrayInstruction).Index;  }; func tom_unused_get_slot_AssignArray_Source(t  Instruction )  Expression  { return (t).(*AssignArrayInstruction).Source;  }; func tom_unused_is_fun_sym_Return(t  Instruction ) bool { return func() bool { _, ok := (t).(*ReturnInstruction); return ok }();  }; func tom_unused_get_slot_Return_Kid1(t  Instruction )  BQTerm  { return (t).(*ReturnInstruction).Kid1;  }; func tom_unused_is_fun_sym_AbstractBlock(t  Instruction ) bool { return func() bool { _, ok := (t).(*AbstractBlockInstruction); return ok }();  }; func tom_unused_get_slot_AbstractBlock_InstList(t  Instruction )  InstructionList  { return (t).(*AbstractBlockInstruction).InstList;  }; func tom_unused_is_fun_sym_UnamedBlock(t  Instruction ) bool { return func() bool { _, ok := (t).(*UnamedBlockInstruction); return ok }();  }; func tom_unused_get_slot_UnamedBlock_InstList(t  Instruction )  InstructionList  { return (t).(*UnamedBlockInstruction).InstList;  }; func tom_unused_is_fun_sym_NamedBlock(t  Instruction ) bool { return func() bool { _, ok := (t).(*NamedBlockInstruction); return ok }();  }; func tom_unused_get_slot_NamedBlock_BlockName(t  Instruction )  string  { return (t).(*NamedBlockInstruction).BlockName;  }; func tom_unused_get_slot_NamedBlock_InstList(t  Instruction )  InstructionList  { return (t).(*NamedBlockInstruction).InstList;  }; func tom_unused_is_fun_sym_CompiledMatch(t  Instruction ) bool { return func() bool { _, ok := (t).(*CompiledMatchInstruction); return ok }();  }; func tom_unused_get_slot_CompiledMatch_AutomataInst(t  Instruction )  Instruction  { return (t).(*CompiledMatchInstruction).AutomataInst;  }; func tom_unused_get_slot_CompiledMatch_Options(t  Instruction )  OptionList  { return (t).(*CompiledMatchInstruction).Options;  }; func tom_unused_is_fun_sym_CompiledPattern(t  Instruction ) bool { return func() bool { _, ok := (t).(*CompiledPatternInstruction); return ok }();  }; func tom_unused_get_slot_CompiledPattern_Contraint(t  Instruction )  Constraint  { return (t).(*CompiledPatternInstruction).Contraint;  }; func tom_unused_get_slot_CompiledPattern_AutomataInst(t  Instruction )  Instruction  { return (t).(*CompiledPatternInstruction).AutomataInst;  }; func tom_unused_is_fun_sym_RawAction(t  Instruction ) bool { return func() bool { _, ok := (t).(*RawActionInstruction); return ok }();  }; func tom_unused_get_slot_RawAction_AstInstruction(t  Instruction )  Instruction  { return (t).(*RawActionInstruction).AstInstruction;  }; func tom_unused_is_fun_sym_concInstruction(t  InstructionList ) bool { return func() bool { _, ok := (t).(*ConcInstructionInstructionList); return ok }();  }; func tom_unused_empty_list_concInstruction()  InstructionList  { return (EmptyConcInstruction()) }; func tom_unused_cons_list_concInstruction(e  Instruction , l  InstructionList )  InstructionList  { return (ConsConcInstruction(e,l)) }; func tom_unused_get_head_concInstruction_InstructionList(l  InstructionList )  Instruction  { return (l).GetHeadconcInstruction();  }; func tom_unused_get_tail_concInstruction_InstructionList(l  InstructionList )  InstructionList  { return (l).GetTailconcInstruction();  }; func tom_unused_is_empty_concInstruction_InstructionList(l  InstructionList ) bool { return (l).IsEmptyconcInstruction();  }; func tom_unused_append_list_concInstruction(l1  InstructionList , l2  InstructionList )  InstructionList  { if tom_unused_is_empty_concInstruction_InstructionList(l1) { return l2 } else if tom_unused_is_empty_concInstruction_InstructionList(l2) { return l1 } else if tom_unused_is_empty_concInstruction_InstructionList(tom_unused_get_tail_concInstruction_InstructionList(l1)) { return tom_unused_cons_list_concInstruction(tom_unused_get_head_concInstruction_InstructionList(l1), l2) } else { return tom_unused_cons_list_concInstruction(tom_unused_get_head_concInstruction_InstructionList(l1), tom_unused_append_list_concInstruction(tom_unused_get_tail_concInstruction_InstructionList(l1), l2)) }; }; func tom_unused_get_slice_concInstruction(begin  InstructionList , end  InstructionList , tail  InstructionList )  InstructionList  { if tom_unused_equal_term_InstructionList(begin, end) { return tail } else if tom_unused_equal_term_InstructionList(end, tail) && (tom_unused_is_empty_concInstruction_InstructionList(end) || tom_unused_equal_term_InstructionList(end, tom_unused_empty_list_concInstruction())) { return begin }; return tom_unused_cons_list_concInstruction(tom_unused_get_head_concInstruction_InstructionList(begin), tom_unused_get_slice_concInstruction(tom_unused_get_tail_concInstruction_InstructionList(begin), end, tail)) }; func tom_unused_equal_term_TomName(t1  TomName , t2  TomName ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_TomName(t any) bool { return func() bool { _, ok := (t).(TomName); return ok }();  }; func tom_unused_equal_term_TomNumber(t1  TomNumber , t2  TomNumber ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_TomNumber(t any) bool { return func() bool { _, ok := (t).(TomNumber); return ok }();  }; func tom_unused_equal_term_TomNumberList(t1  TomNumberList , t2  TomNumberList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_TomNumberList(t any) bool { return func() bool { _, ok := (t).(TomNumberList); return ok }();  }; func tom_unused_equal_term_TomNameList(t1  TomNameList , t2  TomNameList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_TomNameList(t any) bool { return func() bool { _, ok := (t).(TomNameList); return ok }();  }; func tom_unused_equal_term_Option(t1  Option , t2  Option ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_Option(t any) bool { return func() bool { _, ok := (t).(Option); return ok }();  }; func tom_unused_equal_term_OptionList(t1  OptionList , t2  OptionList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_OptionList(t any) bool { return func() bool { _, ok := (t).(OptionList); return ok }();  }; func tom_unused_equal_term_TextPosition(t1  TextPosition , t2  TextPosition ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_TextPosition(t any) bool { return func() bool { _, ok := (t).(TextPosition); return ok }();  }; func tom_unused_equal_term_TomVisit(t1  TomVisit , t2  TomVisit ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_TomVisit(t any) bool { return func() bool { _, ok := (t).(TomVisit); return ok }();  }; func tom_unused_equal_term_ResolveStratElement(t1  ResolveStratElement , t2  ResolveStratElement ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_ResolveStratElement(t any) bool { return func() bool { _, ok := (t).(ResolveStratElement); return ok }();  }; func tom_unused_equal_term_ResolveStratElementList(t1  ResolveStratElementList , t2  ResolveStratElementList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_ResolveStratElementList(t any) bool { return func() bool { _, ok := (t).(ResolveStratElementList); return ok }();  }; func tom_unused_equal_term_ResolveStratBlock(t1  ResolveStratBlock , t2  ResolveStratBlock ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_ResolveStratBlock(t any) bool { return func() bool { _, ok := (t).(ResolveStratBlock); return ok }();  }; func tom_unused_equal_term_ResolveStratBlockList(t1  ResolveStratBlockList , t2  ResolveStratBlockList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_ResolveStratBlockList(t any) bool { return func() bool { _, ok := (t).(ResolveStratBlockList); return ok }();  }; func tom_unused_equal_term_ElementaryTransformation(t1  ElementaryTransformation , t2  ElementaryTransformation ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_ElementaryTransformation(t any) bool { return func() bool { _, ok := (t).(ElementaryTransformation); return ok }();  }; func tom_unused_equal_term_ElementaryTransformationList(t1  ElementaryTransformationList , t2  ElementaryTransformationList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_ElementaryTransformationList(t any) bool { return func() bool { _, ok := (t).(ElementaryTransformationList); return ok }();  }; func tom_unused_equal_term_TomSymbol(t1  TomSymbol , t2  TomSymbol ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_TomSymbol(t any) bool { return func() bool { _, ok := (t).(TomSymbol); return ok }();  }; func tom_unused_equal_term_TomSymbolTable(t1  TomSymbolTable , t2  TomSymbolTable ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_TomSymbolTable(t any) bool { return func() bool { _, ok := (t).(TomSymbolTable); return ok }();  }; func tom_unused_equal_term_TomEntry(t1  TomEntry , t2  TomEntry ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_TomEntry(t any) bool { return func() bool { _, ok := (t).(TomEntry); return ok }();  }; func tom_unused_equal_term_TomStructureTable(t1  TomStructureTable , t2  TomStructureTable ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_TomStructureTable(t any) bool { return func() bool { _, ok := (t).(TomStructureTable); return ok }();  }; func tom_unused_equal_term_TomVisitList(t1  TomVisitList , t2  TomVisitList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_TomVisitList(t any) bool { return func() bool { _, ok := (t).(TomVisitList); return ok }();  }; func tom_unused_equal_term_TomEntryList(t1  TomEntryList , t2  TomEntryList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_TomEntryList(t any) bool { return func() bool { _, ok := (t).(TomEntryList); return ok }();  }; func tom_unused_equal_term_TomSymbolList(t1  TomSymbolList , t2  TomSymbolList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_TomSymbolList(t any) bool { return func() bool { _, ok := (t).(TomSymbolList); return ok }();  }; func tom_unused_equal_term_KeyEntry(t1  KeyEntry , t2  KeyEntry ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_KeyEntry(t any) bool { return func() bool { _, ok := (t).(KeyEntry); return ok }();  }; func tom_unused_equal_term_TransfoStratInfo(t1  TransfoStratInfo , t2  TransfoStratInfo ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_TransfoStratInfo(t any) bool { return func() bool { _, ok := (t).(TransfoStratInfo); return ok }();  }; func tom_unused_equal_term_Slot(t1  Slot , t2  Slot ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_Slot(t any) bool { return func() bool { _, ok := (t).(Slot); return ok }();  }; func tom_unused_equal_term_SlotList(t1  SlotList , t2  SlotList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_SlotList(t any) bool { return func() bool { _, ok := (t).(SlotList); return ok }();  }; func tom_unused_equal_term_BQSlot(t1  BQSlot , t2  BQSlot ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_BQSlot(t any) bool { return func() bool { _, ok := (t).(BQSlot); return ok }();  }; func tom_unused_equal_term_BQSlotList(t1  BQSlotList , t2  BQSlotList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_BQSlotList(t any) bool { return func() bool { _, ok := (t).(BQSlotList); return ok }();  }; func tom_unused_equal_term_PairNameDecl(t1  PairNameDecl , t2  PairNameDecl ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_PairNameDecl(t any) bool { return func() bool { _, ok := (t).(PairNameDecl); return ok }();  }; func tom_unused_equal_term_PairNameDeclList(t1  PairNameDeclList , t2  PairNameDeclList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_PairNameDeclList(t any) bool { return func() bool { _, ok := (t).(PairNameDeclList); return ok }();  }; func tom_unused_equal_term_TomTerm(t1  TomTerm , t2  TomTerm ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_TomTerm(t any) bool { return func() bool { _, ok := (t).(TomTerm); return ok }();  }; func tom_unused_equal_term_TomList(t1  TomList , t2  TomList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_TomList(t any) bool { return func() bool { _, ok := (t).(TomList); return ok }();  }; func tom_unused_is_fun_sym_Variable(t  TomTerm ) bool { return func() bool { _, ok := (t).(*VariableTomTerm); return ok }();  }; func tom_unused_get_slot_Variable_Options(t  TomTerm )  OptionList  { return (t).(*VariableTomTerm).Options;  }; func tom_unused_get_slot_Variable_AstName(t  TomTerm )  TomName  { return (t).(*VariableTomTerm).AstName;  }; func tom_unused_get_slot_Variable_AstType(t  TomTerm )  TomType  { return (t).(*VariableTomTerm).AstType;  }; func tom_unused_get_slot_Variable_Constraints(t  TomTerm )  ConstraintList  { return (t).(*VariableTomTerm).Constraints;  }; func tom_unused_is_fun_sym_VariableStar(t  TomTerm ) bool { return func() bool { _, ok := (t).(*VariableStarTomTerm); return ok }();  }; func tom_unused_get_slot_VariableStar_Options(t  TomTerm )  OptionList  { return (t).(*VariableStarTomTerm).Options;  }; func tom_unused_get_slot_VariableStar_AstName(t  TomTerm )  TomName  { return (t).(*VariableStarTomTerm).AstName;  }; func tom_unused_get_slot_VariableStar_AstType(t  TomTerm )  TomType  { return (t).(*VariableStarTomTerm).AstType;  }; func tom_unused_get_slot_VariableStar_Constraints(t  TomTerm )  ConstraintList  { return (t).(*VariableStarTomTerm).Constraints;  }; func tom_unused_equal_term_TomType(t1  TomType , t2  TomType ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_TomType(t any) bool { return func() bool { _, ok := (t).(TomType); return ok }();  }; func tom_unused_equal_term_TypeOption(t1  TypeOption , t2  TypeOption ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_TypeOption(t any) bool { return func() bool { _, ok := (t).(TypeOption); return ok }();  }; func tom_unused_equal_term_TypeOptionList(t1  TypeOptionList , t2  TypeOptionList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_TypeOptionList(t any) bool { return func() bool { _, ok := (t).(TypeOptionList); return ok }();  }; func tom_unused_equal_term_TargetLanguageType(t1  TargetLanguageType , t2  TargetLanguageType ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_TargetLanguageType(t any) bool { return func() bool { _, ok := (t).(TargetLanguageType); return ok }();  }; func tom_unused_equal_term_TomTypeList(t1  TomTypeList , t2  TomTypeList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_TomTypeList(t any) bool { return func() bool { _, ok := (t).(TomTypeList); return ok }();  }; func tom_unused_equal_term_Info(t1  Info , t2  Info ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_Info(t any) bool { return func() bool { _, ok := (t).(Info); return ok }();  }; func tom_unused_equal_term_TypeConstraint(t1  TypeConstraint , t2  TypeConstraint ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_TypeConstraint(t any) bool { return func() bool { _, ok := (t).(TypeConstraint); return ok }();  }; func tom_unused_equal_term_TypeConstraintList(t1  TypeConstraintList , t2  TypeConstraintList ) bool { return (t1).Equivalent(t2);  }; func tom_unused_is_sort_TypeConstraintList(t any) bool { return func() bool { _, ok := (t).(TypeConstraintList); return ok }();  }; 




const (
	readNone   = 0
	readRead   = 1
	readUnsure = 2
)








func (g *goGenerator) buildUnreadSubjects(automata Instruction) {
	for _, name := range g.hostSubjects(automata) {
		if !g.classFields[name] && g.readsInstruction(automata, name) != readRead {
			g.output.Write("_ = " + name + "; ")
		}
	}
}








func (g *goGenerator) hostSubjects(automata Instruction) []string {
	var names []string
	g.collectHostSubjects(automata, map[string]bool{}, &names)
	return names
}




type children interface {
	ChildCount() int
	ChildAt(i int) any
}








func (g *goGenerator) collectHostSubjects(t any, declared map[string]bool, names *[]string) {
	switch term := t.(type) {
	case Instruction:
		; { { if tom_unused_is_sort_Instruction(term) { if tom_unused_is_fun_sym_RawAction(any(term).( Instruction )) { 
 return ;  };  };  }; { if tom_unused_is_sort_Instruction(term) { var tomMatch1_10 bool =  false ; var tomMatch1_8  Instruction  =  *new(Instruction) ; var tomMatch1_4  BQTerm  =  *new(BQTerm) ; var tomMatch1_6  Instruction  =  *new(Instruction) ; var tomMatch1_5  Expression  =  *new(Expression) ; var tomMatch1_9  Instruction  =  *new(Instruction) ; if tom_unused_is_fun_sym_Let(any(term).( Instruction )) { { tomMatch1_10 =  true ; tomMatch1_8 = any(term).( Instruction ); tomMatch1_4 = tom_unused_get_slot_Let_Variable(tomMatch1_8); tomMatch1_5 = tom_unused_get_slot_Let_Source(tomMatch1_8); tomMatch1_6 = tom_unused_get_slot_Let_AstInstruction(tomMatch1_8);  };  } else { if tom_unused_is_fun_sym_LetRef(any(term).( Instruction )) { { tomMatch1_10 =  true ; tomMatch1_9 = any(term).( Instruction ); tomMatch1_4 = tom_unused_get_slot_LetRef_Variable(tomMatch1_9); tomMatch1_5 = tom_unused_get_slot_LetRef_Source(tomMatch1_9); tomMatch1_6 = tom_unused_get_slot_LetRef_AstInstruction(tomMatch1_9);  };  };  }; if tomMatch1_10 { 

				g.collectHostSubjects(tomMatch1_5, declared, names)
				inner := make(map[string]bool, len(declared)+1)
				for k := range declared {
					inner[k] = true
				}
				inner[g.localName(tomMatch1_4)] = true
				g.collectHostSubjects(tomMatch1_6, inner, names)
				return
			;  };  };  };  }; 

	case BQTerm:
		; { { if tom_unused_is_sort_BQTerm(term) { var tomMatch2_4 bool =  false ; var tomMatch2_2  BQTerm  =  *new(BQTerm) ; _ = tomMatch2_2; var tomMatch2_3  BQTerm  =  *new(BQTerm) ; _ = tomMatch2_3; if tom_unused_is_fun_sym_BQVariable(any(term).( BQTerm )) { { tomMatch2_4 =  true ; tomMatch2_2 = any(term).( BQTerm );  };  } else { if tom_unused_is_fun_sym_BQVariableStar(any(term).( BQTerm )) { { tomMatch2_4 =  true ; tomMatch2_3 = any(term).( BQTerm );  };  };  }; if tomMatch2_4 { 

				name := g.localName(any(term).( BQTerm ))
				if !declared[name] && !containsString(*names, name) {
					*names = append(*names, name)
				}
				return
			;  };  };  };  }; 

	case InstructionList, Expression, BQTermList, Code, CodeList, CompositeMember:
	default:
		return
	}
	c, ok := t.(children)
	if !ok {
		return
	}
	for i := 0; i < c.ChildCount(); i++ {
		g.collectHostSubjects(c.ChildAt(i), declared, names)
	}
}



func containsString(l []string, s string) bool {
	for _, e := range l {
		if e == s {
			return true
		}
	}
	return false
}




func (g *goGenerator) readsInstruction(inst Instruction, name string) int {
	; { { if tom_unused_is_sort_Instruction(inst) { if tom_unused_is_fun_sym_CodeToInstruction(any(inst).( Instruction )) { 
 return g.readsCode(tom_unused_get_slot_CodeToInstruction_Code(any(inst).( Instruction )), name) ;  };  };  }; { if tom_unused_is_sort_Instruction(inst) { if tom_unused_is_fun_sym_BQTermToInstruction(any(inst).( Instruction )) { 
 return g.readsBQTerm(tom_unused_get_slot_BQTermToInstruction_Tom(any(inst).( Instruction )), name) ;  };  };  }; { if tom_unused_is_sort_Instruction(inst) { if tom_unused_is_fun_sym_ExpressionToInstruction(any(inst).( Instruction )) { 
 return g.readsExpression(tom_unused_get_slot_ExpressionToInstruction_Expr(any(inst).( Instruction )), name) ;  };  };  }; { if tom_unused_is_sort_Instruction(inst) { if tom_unused_is_fun_sym_If(any(inst).( Instruction )) { 

			return max(g.readsExpression(tom_unused_get_slot_If_Condition(any(inst).( Instruction )), name), g.readsInstruction(tom_unused_get_slot_If_SuccesInst(any(inst).( Instruction )), name), g.readsInstruction(tom_unused_get_slot_If_FailureInst(any(inst).( Instruction )), name))
		;  };  };  }; { if tom_unused_is_sort_Instruction(inst) { if tom_unused_is_fun_sym_DoWhile(any(inst).( Instruction )) { 
 return max(g.readsInstruction(tom_unused_get_slot_DoWhile_DoInst(any(inst).( Instruction )), name), g.readsExpression(tom_unused_get_slot_DoWhile_Condition(any(inst).( Instruction )), name)) ;  };  };  }; { if tom_unused_is_sort_Instruction(inst) { if tom_unused_is_fun_sym_WhileDo(any(inst).( Instruction )) { 
 return max(g.readsExpression(tom_unused_get_slot_WhileDo_Condition(any(inst).( Instruction )), name), g.readsInstruction(tom_unused_get_slot_WhileDo_DoInst(any(inst).( Instruction )), name)) ;  };  };  }; { if tom_unused_is_sort_Instruction(inst) { var tomMatch3_35 bool =  false ; var tomMatch3_33  Instruction  =  *new(Instruction) ; var tomMatch3_34  Instruction  =  *new(Instruction) ; var tomMatch3_30  Expression  =  *new(Expression) ; var tomMatch3_31  Instruction  =  *new(Instruction) ; var tomMatch3_29  BQTerm  =  *new(BQTerm) ; if tom_unused_is_fun_sym_Let(any(inst).( Instruction )) { { tomMatch3_35 =  true ; tomMatch3_33 = any(inst).( Instruction ); tomMatch3_29 = tom_unused_get_slot_Let_Variable(tomMatch3_33); tomMatch3_30 = tom_unused_get_slot_Let_Source(tomMatch3_33); tomMatch3_31 = tom_unused_get_slot_Let_AstInstruction(tomMatch3_33);  };  } else { if tom_unused_is_fun_sym_LetRef(any(inst).( Instruction )) { { tomMatch3_35 =  true ; tomMatch3_34 = any(inst).( Instruction ); tomMatch3_29 = tom_unused_get_slot_LetRef_Variable(tomMatch3_34); tomMatch3_30 = tom_unused_get_slot_LetRef_Source(tomMatch3_34); tomMatch3_31 = tom_unused_get_slot_LetRef_AstInstruction(tomMatch3_34);  };  };  }; if tomMatch3_35 { 

			r := g.readsExpression(tomMatch3_30, name)
			if name == g.localName(tomMatch3_29) {
				return r
			}
			return max(r, g.readsInstruction(tomMatch3_31, name))
		;  };  };  }; { if tom_unused_is_sort_Instruction(inst) { if tom_unused_is_fun_sym_Assign(any(inst).( Instruction )) { 
 return g.readsExpression(tom_unused_get_slot_Assign_Source(any(inst).( Instruction )), name) ;  };  };  }; { if tom_unused_is_sort_Instruction(inst) { if tom_unused_is_fun_sym_AssignArray(any(inst).( Instruction )) { 
 return g.readsExpression(tom_unused_get_slot_AssignArray_Source(any(inst).( Instruction )), name) ;  };  };  }; { if tom_unused_is_sort_Instruction(inst) { if tom_unused_is_fun_sym_Return(any(inst).( Instruction )) { 
 return g.readsBQTerm(tom_unused_get_slot_Return_Kid1(any(inst).( Instruction )), name) ;  };  };  }; { if tom_unused_is_sort_Instruction(inst) { var tomMatch3_56 bool =  false ; var tomMatch3_54  Instruction  =  *new(Instruction) ; var tomMatch3_55  Instruction  =  *new(Instruction) ; var tomMatch3_52  InstructionList  =  *new(InstructionList) ; if tom_unused_is_fun_sym_AbstractBlock(any(inst).( Instruction )) { { tomMatch3_56 =  true ; tomMatch3_54 = any(inst).( Instruction ); tomMatch3_52 = tom_unused_get_slot_AbstractBlock_InstList(tomMatch3_54);  };  } else { if tom_unused_is_fun_sym_UnamedBlock(any(inst).( Instruction )) { { tomMatch3_56 =  true ; tomMatch3_55 = any(inst).( Instruction ); tomMatch3_52 = tom_unused_get_slot_UnamedBlock_InstList(tomMatch3_55);  };  };  }; if tomMatch3_56 { 
 return g.readsInstructionList(tomMatch3_52, name) ;  };  };  }; { if tom_unused_is_sort_Instruction(inst) { if tom_unused_is_fun_sym_NamedBlock(any(inst).( Instruction )) { 
 return g.readsInstructionList(tom_unused_get_slot_NamedBlock_InstList(any(inst).( Instruction )), name) ;  };  };  }; { if tom_unused_is_sort_Instruction(inst) { if tom_unused_is_fun_sym_RawAction(any(inst).( Instruction )) { 
 return g.readsInstruction(tom_unused_get_slot_RawAction_AstInstruction(any(inst).( Instruction )), name) ;  };  };  }; { if tom_unused_is_sort_Instruction(inst) { if tom_unused_is_fun_sym_CompiledMatch(any(inst).( Instruction )) { var tom___i  Instruction  = tom_unused_get_slot_CompiledMatch_AutomataInst(any(inst).( Instruction )); 

			r := g.readsInstruction(tom___i, name)
			if !g.classFields[name] && containsString(g.hostSubjects(tom___i), name) {
				r = max(r, readRead)
			}
			return r
		;  };  };  }; { if tom_unused_is_sort_Instruction(inst) { if tom_unused_is_fun_sym_CompiledPattern(any(inst).( Instruction )) { 
 return g.readsInstruction(tom_unused_get_slot_CompiledPattern_AutomataInst(any(inst).( Instruction )), name) ;  };  };  };  }; 

	return readNone
}




func (g *goGenerator) readsInstructionList(l InstructionList, name string) int {
	r := readNone
	; { { if tom_unused_is_sort_InstructionList(l) { if tom_unused_is_fun_sym_concInstruction(any(l).( InstructionList )) { var tomMatch4_end_4  InstructionList  = any(l).( InstructionList ); for { { if !(tom_unused_is_empty_concInstruction_InstructionList(tomMatch4_end_4)) { 
 r = max(r, g.readsInstruction(tom_unused_get_head_concInstruction_InstructionList(tomMatch4_end_4), name)) ;  }; if tom_unused_is_empty_concInstruction_InstructionList(tomMatch4_end_4) { tomMatch4_end_4 = any(l).( InstructionList );  } else { tomMatch4_end_4 = tom_unused_get_tail_concInstruction_InstructionList(tomMatch4_end_4);  };  };  if !(!(tom_unused_equal_term_InstructionList(tomMatch4_end_4, any(l).( InstructionList )))) { break; }; };  };  };  };  }; 

	return r
}




func (g *goGenerator) readsCode(code Code, name string) int {
	r := readNone
	; { { if tom_unused_is_sort_Code(code) { var tomMatch5_11 bool =  false ; var tomMatch5_1  CodeList  =  *new(CodeList) ; var tomMatch5_4  Code  =  *new(Code) ; var tomMatch5_3  Code  =  *new(Code) ; if tom_unused_is_fun_sym_Tom(any(code).( Code )) { { tomMatch5_11 =  true ; tomMatch5_3 = any(code).( Code ); tomMatch5_1 = tom_unused_get_slot_Tom_CodeList(tomMatch5_3);  };  } else { if tom_unused_is_fun_sym_TomInclude(any(code).( Code )) { { tomMatch5_11 =  true ; tomMatch5_4 = any(code).( Code ); tomMatch5_1 = tom_unused_get_slot_TomInclude_CodeList(tomMatch5_4);  };  };  }; if tomMatch5_11 { if tom_unused_is_fun_sym_concCode(any(tomMatch5_1).( CodeList )) { var tomMatch5_end_8  CodeList  = tomMatch5_1; for { { if !(tom_unused_is_empty_concCode_CodeList(tomMatch5_end_8)) { 
 r = max(r, g.readsCode(tom_unused_get_head_concCode_CodeList(tomMatch5_end_8), name)) ;  }; if tom_unused_is_empty_concCode_CodeList(tomMatch5_end_8) { tomMatch5_end_8 = tomMatch5_1;  } else { tomMatch5_end_8 = tom_unused_get_tail_concCode_CodeList(tomMatch5_end_8);  };  };  if !(!(tom_unused_equal_term_CodeList(tomMatch5_end_8, tomMatch5_1))) { break; }; };  };  };  };  }; { if tom_unused_is_sort_Code(code) { if tom_unused_is_fun_sym_TargetLanguageToCode(any(code).( Code )) { 
 r = g.readsTargetLanguage(tom_unused_get_slot_TargetLanguageToCode_Tl(any(code).( Code )), name) ;  };  };  }; { if tom_unused_is_sort_Code(code) { if tom_unused_is_fun_sym_InstructionToCode(any(code).( Code )) { 
 r = g.readsInstruction(tom_unused_get_slot_InstructionToCode_AstInstruction(any(code).( Code )), name) ;  };  };  }; { if tom_unused_is_sort_Code(code) { if tom_unused_is_fun_sym_BQTermToCode(any(code).( Code )) { 
 r = g.readsBQTerm(tom_unused_get_slot_BQTermToCode_Bq(any(code).( Code )), name) ;  };  };  };  }; 

	return r
}




func (g *goGenerator) readsTargetLanguage(tl TargetLanguage, name string) int {
	; { { if tom_unused_is_sort_TargetLanguage(tl) { if tom_unused_is_fun_sym_TL(any(tl).( TargetLanguage )) { 
 return scanHostText(tom_unused_get_slot_TL_Code(any(tl).( TargetLanguage )), name) ;  };  };  }; { if tom_unused_is_sort_TargetLanguage(tl) { if tom_unused_is_fun_sym_ITL(any(tl).( TargetLanguage )) { 
 return scanHostText(tom_unused_get_slot_ITL_Code(any(tl).( TargetLanguage )), name) ;  };  };  };  }; 

	return readNone
}




func (g *goGenerator) readsBQTermList(l BQTermList, name string) int {
	r := readNone
	; { { if tom_unused_is_sort_BQTermList(l) { if tom_unused_is_fun_sym_concBQTerm(any(l).( BQTermList )) { var tomMatch7_end_4  BQTermList  = any(l).( BQTermList ); for { { if !(tom_unused_is_empty_concBQTerm_BQTermList(tomMatch7_end_4)) { 
 r = max(r, g.readsBQTerm(tom_unused_get_head_concBQTerm_BQTermList(tomMatch7_end_4), name)) ;  }; if tom_unused_is_empty_concBQTerm_BQTermList(tomMatch7_end_4) { tomMatch7_end_4 = any(l).( BQTermList );  } else { tomMatch7_end_4 = tom_unused_get_tail_concBQTerm_BQTermList(tomMatch7_end_4);  };  };  if !(!(tom_unused_equal_term_BQTermList(tomMatch7_end_4, any(l).( BQTermList )))) { break; }; };  };  };  };  }; 

	return r
}




func (g *goGenerator) readsBQTerm(t BQTerm, name string) int {
	r := readNone
	; { { if tom_unused_is_sort_BQTerm(t) { var tomMatch8_4 bool =  false ; var tomMatch8_2  BQTerm  =  *new(BQTerm) ; _ = tomMatch8_2; var tomMatch8_3  BQTerm  =  *new(BQTerm) ; _ = tomMatch8_3; if tom_unused_is_fun_sym_BQVariable(any(t).( BQTerm )) { { tomMatch8_4 =  true ; tomMatch8_2 = any(t).( BQTerm );  };  } else { if tom_unused_is_fun_sym_BQVariableStar(any(t).( BQTerm )) { { tomMatch8_4 =  true ; tomMatch8_3 = any(t).( BQTerm );  };  };  }; if tomMatch8_4 { 

			if name == g.localName(any(t).( BQTerm )) {
				return readRead
			}
			return readNone
		;  };  };  }; { if tom_unused_is_sort_BQTerm(t) { if tom_unused_is_fun_sym_FunctionCall(any(t).( BQTerm )) { 
 return g.readsBQTermList(tom_unused_get_slot_FunctionCall_Args(any(t).( BQTerm )), name) ;  };  };  }; { if tom_unused_is_sort_BQTerm(t) { var tomMatch8_18 bool =  false ; var tomMatch8_13  BQTerm  =  *new(BQTerm) ; var tomMatch8_14  BQTerm  =  *new(BQTerm) ; var tomMatch8_17  BQTerm  =  *new(BQTerm) ; var tomMatch8_16  BQTerm  =  *new(BQTerm) ; var tomMatch8_12  TomName  =  *new(TomName) ; _ = tomMatch8_12; if tom_unused_is_fun_sym_BuildAppendList(any(t).( BQTerm )) { { tomMatch8_18 =  true ; tomMatch8_16 = any(t).( BQTerm ); tomMatch8_12 = tom_unused_get_slot_BuildAppendList_AstName(tomMatch8_16); tomMatch8_13 = tom_unused_get_slot_BuildAppendList_HeadTerm(tomMatch8_16); tomMatch8_14 = tom_unused_get_slot_BuildAppendList_TailTerm(tomMatch8_16);  };  } else { if tom_unused_is_fun_sym_BuildAppendArray(any(t).( BQTerm )) { { tomMatch8_18 =  true ; tomMatch8_17 = any(t).( BQTerm ); tomMatch8_12 = tom_unused_get_slot_BuildAppendArray_AstName(tomMatch8_17); tomMatch8_13 = tom_unused_get_slot_BuildAppendArray_HeadTerm(tomMatch8_17); tomMatch8_14 = tom_unused_get_slot_BuildAppendArray_TailTerm(tomMatch8_17);  };  };  }; if tomMatch8_18 { 

			return max(g.readsBQTerm(tomMatch8_13, name), g.readsBQTerm(tomMatch8_14, name))
		;  };  };  }; { if tom_unused_is_sort_BQTerm(t) { if tom_unused_is_fun_sym_ExpressionToBQTerm(any(t).( BQTerm )) { 
 return g.readsExpression(tom_unused_get_slot_ExpressionToBQTerm_Exp(any(t).( BQTerm )), name) ;  };  };  }; { if tom_unused_is_sort_BQTerm(t) { if tom_unused_is_fun_sym_Composite(any(t).( BQTerm )) { var tomMatch8_end_27  BQTerm  = any(t).( BQTerm ); for { { if !(tom_unused_is_empty_Composite_BQTerm(tomMatch8_end_27)) { var tomMatch8_31  CompositeMember  = tom_unused_get_head_Composite_BQTerm(tomMatch8_end_27); if tom_unused_is_fun_sym_CompositeBQTerm(any(tomMatch8_31).( CompositeMember )) { 
 r = max(r, g.readsBQTerm(tom_unused_get_slot_CompositeBQTerm_term(tomMatch8_31), name)) ;  };  }; if tom_unused_is_empty_Composite_BQTerm(tomMatch8_end_27) { tomMatch8_end_27 = any(t).( BQTerm );  } else { tomMatch8_end_27 = tom_unused_get_tail_Composite_BQTerm(tomMatch8_end_27);  };  };  if !(!(tom_unused_equal_term_BQTerm(tomMatch8_end_27, any(t).( BQTerm )))) { break; }; };  };  };  }; { if tom_unused_is_sort_BQTerm(t) { if tom_unused_is_fun_sym_Composite(any(t).( BQTerm )) { var tomMatch8_end_37  BQTerm  = any(t).( BQTerm ); for { { if !(tom_unused_is_empty_Composite_BQTerm(tomMatch8_end_37)) { var tomMatch8_41  CompositeMember  = tom_unused_get_head_Composite_BQTerm(tomMatch8_end_37); if tom_unused_is_fun_sym_CompositeTL(any(tomMatch8_41).( CompositeMember )) { 
 r = max(r, g.readsTargetLanguage(tom_unused_get_slot_CompositeTL_Tl(tomMatch8_41), name)) ;  };  }; if tom_unused_is_empty_Composite_BQTerm(tomMatch8_end_37) { tomMatch8_end_37 = any(t).( BQTerm );  } else { tomMatch8_end_37 = tom_unused_get_tail_Composite_BQTerm(tomMatch8_end_37);  };  };  if !(!(tom_unused_equal_term_BQTerm(tomMatch8_end_37, any(t).( BQTerm )))) { break; }; };  };  };  };  }; 

	if !g.inline {
		; { { if tom_unused_is_sort_BQTerm(t) { if tom_unused_is_fun_sym_BuildTerm(any(t).( BQTerm )) { 
 return g.readsBQTermList(tom_unused_get_slot_BuildTerm_Args(any(t).( BQTerm )), name) ;  };  };  }; { if tom_unused_is_sort_BQTerm(t) { var tomMatch9_13 bool =  false ; var tomMatch9_11  BQTerm  =  *new(BQTerm) ; var tomMatch9_8  BQTerm  =  *new(BQTerm) ; var tomMatch9_9  BQTerm  =  *new(BQTerm) ; var tomMatch9_7  TomName  =  *new(TomName) ; _ = tomMatch9_7; var tomMatch9_12  BQTerm  =  *new(BQTerm) ; if tom_unused_is_fun_sym_BuildConsList(any(t).( BQTerm )) { { tomMatch9_13 =  true ; tomMatch9_11 = any(t).( BQTerm ); tomMatch9_7 = tom_unused_get_slot_BuildConsList_AstName(tomMatch9_11); tomMatch9_8 = tom_unused_get_slot_BuildConsList_HeadTerm(tomMatch9_11); tomMatch9_9 = tom_unused_get_slot_BuildConsList_TailTerm(tomMatch9_11);  };  } else { if tom_unused_is_fun_sym_BuildConsArray(any(t).( BQTerm )) { { tomMatch9_13 =  true ; tomMatch9_12 = any(t).( BQTerm ); tomMatch9_7 = tom_unused_get_slot_BuildConsArray_AstName(tomMatch9_12); tomMatch9_8 = tom_unused_get_slot_BuildConsArray_HeadTerm(tomMatch9_12); tomMatch9_9 = tom_unused_get_slot_BuildConsArray_TailTerm(tomMatch9_12);  };  };  }; if tomMatch9_13 { 

				return max(g.readsBQTerm(tomMatch9_8, name), g.readsBQTerm(tomMatch9_9, name))
			;  };  };  }; { if tom_unused_is_sort_BQTerm(t) { if tom_unused_is_fun_sym_BuildEmptyArray(any(t).( BQTerm )) { 
 return g.readsBQTerm(tom_unused_get_slot_BuildEmptyArray_Size(any(t).( BQTerm )), name) ;  };  };  };  }; 

	}
	return r
}




func (g *goGenerator) readsExpression(e Expression, name string) int {
	; { { if tom_unused_is_sort_Expression(e) { if tom_unused_is_fun_sym_Code(any(e).( Expression )) { 
 return scanHostText(tom_unused_get_slot_Code_Code(any(e).( Expression )), name) ;  };  };  }; { if tom_unused_is_sort_Expression(e) { if tom_unused_is_fun_sym_BQTermToExpression(any(e).( Expression )) { 
 return g.readsBQTerm(tom_unused_get_slot_BQTermToExpression_AstTerm(any(e).( Expression )), name) ;  };  };  }; { if tom_unused_is_sort_Expression(e) { if tom_unused_is_fun_sym_TomInstructionToExpression(any(e).( Expression )) { 
 return g.readsInstruction(tom_unused_get_slot_TomInstructionToExpression_Instruction(any(e).( Expression )), name) ;  };  };  }; { if tom_unused_is_sort_Expression(e) { if tom_unused_is_fun_sym_Negation(any(e).( Expression )) { 
 return g.readsExpression(tom_unused_get_slot_Negation_Arg(any(e).( Expression )), name) ;  };  };  }; { if tom_unused_is_sort_Expression(e) { var tomMatch10_26 bool =  false ; var tomMatch10_25  Expression  =  *new(Expression) ; var tomMatch10_24  Expression  =  *new(Expression) ; var tomMatch10_17  Expression  =  *new(Expression) ; var tomMatch10_18  Expression  =  *new(Expression) ; var tomMatch10_21  Expression  =  *new(Expression) ; var tomMatch10_20  Expression  =  *new(Expression) ; var tomMatch10_22  Expression  =  *new(Expression) ; var tomMatch10_23  Expression  =  *new(Expression) ; if tom_unused_is_fun_sym_And(any(e).( Expression )) { { tomMatch10_26 =  true ; tomMatch10_20 = any(e).( Expression ); tomMatch10_17 = tom_unused_get_slot_And_Arg1(tomMatch10_20); tomMatch10_18 = tom_unused_get_slot_And_Arg2(tomMatch10_20);  };  } else { if tom_unused_is_fun_sym_Or(any(e).( Expression )) { { tomMatch10_26 =  true ; tomMatch10_21 = any(e).( Expression ); tomMatch10_17 = tom_unused_get_slot_Or_Arg1(tomMatch10_21); tomMatch10_18 = tom_unused_get_slot_Or_Arg2(tomMatch10_21);  };  } else { if tom_unused_is_fun_sym_GreaterThan(any(e).( Expression )) { { tomMatch10_26 =  true ; tomMatch10_22 = any(e).( Expression ); tomMatch10_17 = tom_unused_get_slot_GreaterThan_Arg1(tomMatch10_22); tomMatch10_18 = tom_unused_get_slot_GreaterThan_Arg2(tomMatch10_22);  };  } else { if tom_unused_is_fun_sym_GreaterOrEqualThan(any(e).( Expression )) { { tomMatch10_26 =  true ; tomMatch10_23 = any(e).( Expression ); tomMatch10_17 = tom_unused_get_slot_GreaterOrEqualThan_Arg1(tomMatch10_23); tomMatch10_18 = tom_unused_get_slot_GreaterOrEqualThan_Arg2(tomMatch10_23);  };  } else { if tom_unused_is_fun_sym_LessThan(any(e).( Expression )) { { tomMatch10_26 =  true ; tomMatch10_24 = any(e).( Expression ); tomMatch10_17 = tom_unused_get_slot_LessThan_Arg1(tomMatch10_24); tomMatch10_18 = tom_unused_get_slot_LessThan_Arg2(tomMatch10_24);  };  } else { if tom_unused_is_fun_sym_LessOrEqualThan(any(e).( Expression )) { { tomMatch10_26 =  true ; tomMatch10_25 = any(e).( Expression ); tomMatch10_17 = tom_unused_get_slot_LessOrEqualThan_Arg1(tomMatch10_25); tomMatch10_18 = tom_unused_get_slot_LessOrEqualThan_Arg2(tomMatch10_25);  };  };  };  };  };  };  }; if tomMatch10_26 { 

			return max(g.readsExpression(tomMatch10_17, name), g.readsExpression(tomMatch10_18, name))
		;  };  };  }; { if tom_unused_is_sort_Expression(e) { if tom_unused_is_fun_sym_Conditional(any(e).( Expression )) { 

			return max(g.readsExpression(tom_unused_get_slot_Conditional_Cond(any(e).( Expression )), name), g.readsExpression(tom_unused_get_slot_Conditional_Then(any(e).( Expression )), name), g.readsExpression(tom_unused_get_slot_Conditional_Else(any(e).( Expression )), name))
		;  };  };  }; { if tom_unused_is_sort_Expression(e) { if tom_unused_is_fun_sym_Cast(any(e).( Expression )) { 
 return g.readsExpression(tom_unused_get_slot_Cast_Source(any(e).( Expression )), name) ;  };  };  }; { if tom_unused_is_sort_Expression(e) { if tom_unused_is_fun_sym_EqualTerm(any(e).( Expression )) { var tomMatch10_41  TomTerm  = tom_unused_get_slot_EqualTerm_Kid2(any(e).( Expression )); var tomMatch10_47 bool =  false ; var tomMatch10_46  TomTerm  =  *new(TomTerm) ; _ = tomMatch10_46; var tomMatch10_45  TomTerm  =  *new(TomTerm) ; _ = tomMatch10_45; if tom_unused_is_fun_sym_Variable(any(tomMatch10_41).( TomTerm )) { { tomMatch10_47 =  true ; tomMatch10_45 = tomMatch10_41;  };  } else { if tom_unused_is_fun_sym_VariableStar(any(tomMatch10_41).( TomTerm )) { { tomMatch10_47 =  true ; tomMatch10_46 = tomMatch10_41;  };  };  }; if tomMatch10_47 { 

			r := g.readsBQTerm(tom_unused_get_slot_EqualTerm_Kid1(any(e).( Expression )), name)
			if name == g.getVariableNameAbstract(tomMatch10_41) {
				r = max(r, readRead)
			}
			return r
		;  };  };  };  }; { if tom_unused_is_sort_Expression(e) { if tom_unused_is_fun_sym_EqualTerm(any(e).( Expression )) { 
 return g.readsBQTerm(tom_unused_get_slot_EqualTerm_Kid1(any(e).( Expression )), name) ;  };  };  }; { if tom_unused_is_sort_Expression(e) { if tom_unused_is_fun_sym_EqualBQTerm(any(e).( Expression )) { 
 return max(g.readsBQTerm(tom_unused_get_slot_EqualBQTerm_t1(any(e).( Expression )), name), g.readsBQTerm(tom_unused_get_slot_EqualBQTerm_t2(any(e).( Expression )), name)) ;  };  };  }; { if tom_unused_is_sort_Expression(e) { var tomMatch10_65 bool =  false ; var tomMatch10_63  Expression  =  *new(Expression) ; var tomMatch10_61  BQTerm  =  *new(BQTerm) ; var tomMatch10_64  Expression  =  *new(Expression) ; if tom_unused_is_fun_sym_AddOne(any(e).( Expression )) { { tomMatch10_65 =  true ; tomMatch10_63 = any(e).( Expression ); tomMatch10_61 = tom_unused_get_slot_AddOne_Variable(tomMatch10_63);  };  } else { if tom_unused_is_fun_sym_SubstractOne(any(e).( Expression )) { { tomMatch10_65 =  true ; tomMatch10_64 = any(e).( Expression ); tomMatch10_61 = tom_unused_get_slot_SubstractOne_Variable(tomMatch10_64);  };  };  }; if tomMatch10_65 { 
 return g.readsBQTerm(tomMatch10_61, name) ;  };  };  }; { if tom_unused_is_sort_Expression(e) { if tom_unused_is_fun_sym_Substract(any(e).( Expression )) { 
 return max(g.readsBQTerm(tom_unused_get_slot_Substract_Term1(any(e).( Expression )), name), g.readsBQTerm(tom_unused_get_slot_Substract_Term2(any(e).( Expression )), name)) ;  };  };  }; { if tom_unused_is_sort_Expression(e) { if tom_unused_is_fun_sym_GetSliceList(any(e).( Expression )) { 

			return max(g.readsBQTerm(tom_unused_get_slot_GetSliceList_VariableBeginAST(any(e).( Expression )), name), g.readsBQTerm(tom_unused_get_slot_GetSliceList_VariableEndAST(any(e).( Expression )), name), g.readsBQTerm(tom_unused_get_slot_GetSliceList_Tail(any(e).( Expression )), name))
		;  };  };  }; { if tom_unused_is_sort_Expression(e) { if tom_unused_is_fun_sym_GetSliceArray(any(e).( Expression )) { 

			return max(g.readsBQTerm(tom_unused_get_slot_GetSliceArray_SubjectListName(any(e).( Expression )), name), g.readsBQTerm(tom_unused_get_slot_GetSliceArray_VariableBeginAST(any(e).( Expression )), name), g.readsBQTerm(tom_unused_get_slot_GetSliceArray_VariableEndAST(any(e).( Expression )), name))
		;  };  };  };  }; 

	if !g.inline {
		; { { if tom_unused_is_sort_Expression(e) { if tom_unused_is_fun_sym_GetSlot(any(e).( Expression )) { 
 return g.readsBQTerm(tom_unused_get_slot_GetSlot_Variable(any(e).( Expression )), name) ;  };  };  }; { if tom_unused_is_sort_Expression(e) { if tom_unused_is_fun_sym_IsFsym(any(e).( Expression )) { 
 return g.readsBQTerm(tom_unused_get_slot_IsFsym_Variable(any(e).( Expression )), name) ;  };  };  }; { if tom_unused_is_sort_Expression(e) { if tom_unused_is_fun_sym_GetHead(any(e).( Expression )) { 
 return g.readsBQTerm(tom_unused_get_slot_GetHead_Variable(any(e).( Expression )), name) ;  };  };  }; { if tom_unused_is_sort_Expression(e) { if tom_unused_is_fun_sym_GetTail(any(e).( Expression )) { 
 return g.readsBQTerm(tom_unused_get_slot_GetTail_Variable(any(e).( Expression )), name) ;  };  };  }; { if tom_unused_is_sort_Expression(e) { if tom_unused_is_fun_sym_IsEmptyList(any(e).( Expression )) { 
 return g.readsBQTerm(tom_unused_get_slot_IsEmptyList_Variable(any(e).( Expression )), name) ;  };  };  }; { if tom_unused_is_sort_Expression(e) { if tom_unused_is_fun_sym_GetSize(any(e).( Expression )) { 
 return g.readsBQTerm(tom_unused_get_slot_GetSize_Variable(any(e).( Expression )), name) ;  };  };  }; { if tom_unused_is_sort_Expression(e) { if tom_unused_is_fun_sym_IsEmptyArray(any(e).( Expression )) { 
 return max(g.readsBQTerm(tom_unused_get_slot_IsEmptyArray_Variable(any(e).( Expression )), name), g.readsBQTerm(tom_unused_get_slot_IsEmptyArray_Index(any(e).( Expression )), name)) ;  };  };  }; { if tom_unused_is_sort_Expression(e) { if tom_unused_is_fun_sym_GetElement(any(e).( Expression )) { 
 return max(g.readsBQTerm(tom_unused_get_slot_GetElement_Variable(any(e).( Expression )), name), g.readsBQTerm(tom_unused_get_slot_GetElement_Index(any(e).( Expression )), name)) ;  };  };  };  }; 

	}
	return readNone
}








func (g *goGenerator) localName(v BQTerm) string {
	; { { if tom_unused_is_sort_BQTerm(v) { var tomMatch12_4 bool =  false ; var tomMatch12_2  BQTerm  =  *new(BQTerm) ; _ = tomMatch12_2; var tomMatch12_3  BQTerm  =  *new(BQTerm) ; _ = tomMatch12_3; if tom_unused_is_fun_sym_BQVariable(any(v).( BQTerm )) { { tomMatch12_4 =  true ; tomMatch12_2 = any(v).( BQTerm );  };  } else { if tom_unused_is_fun_sym_BQVariableStar(any(v).( BQTerm )) { { tomMatch12_4 =  true ; tomMatch12_3 = any(v).( BQTerm );  };  };  }; if tomMatch12_4 { 
 return g.getVariableNameBQAbstract(v) ;  };  };  };  }; 

	return ""
}






func scanHostText(text, name string) int {
	if name == "" || !strings.Contains(text, name) {
		return readNone
	}
	toks, ok := goTokens(text)
	if !ok {
		return readUnsure
	}
	result := readNone
	var stack []string
	prevClosed := ""
	for i, tok := range toks {
		closed := ""
		switch {
		case tok == "(":
			prev := ""
			if i > 0 {
				prev = toks[i-1]
			}
			if prev == "func" || (prev == ")" && prevClosed == "f(") {
				stack = append(stack, "f(")
			} else {
				stack = append(stack, "(")
			}
		case tok == "[" || tok == "{":
			stack = append(stack, tok)
		case tok == ")" || tok == "]" || tok == "}":
			if len(stack) > 0 {
				closed = stack[len(stack)-1]
				stack = stack[:len(stack)-1]
			}
		case tok == name:
			result = max(result, hostOccurrence(toks, i, stack))
			if result == readUnsure {
				return readUnsure
			}
		}
		prevClosed = closed
	}
	return result
}




func hostOccurrence(toks []string, i int, stack []string) int {
	prev, next := "", ""
	hasNext := i+1 < len(toks)
	if i > 0 {
		prev = toks[i-1]
	}
	if hasNext {
		next = toks[i+1]
	}
	if containsString(stack, "f(") {
		return readUnsure
	}
	if prev == "." || prev == "goto" || prev == "break" || prev == "continue" {
		return readNone
	}
	if prev == "var" || prev == "const" || prev == "type" || prev == "func" {
		return readUnsure
	}
	start := i
	for start > 0 && !isStatementEnd(toks[start-1]) {
		start--
	}
	if first := toks[start]; first == "var" || first == "const" || first == "type" {
		return readUnsure
	}
	if !hasNext {
		return readRead
	}
	if next == ":=" || isGoIdentStart(next[0]) {
		return readUnsure
	}
	if next == ":" || next == "=" {
		return readNone
	}
	if next == "," {
		if top := stack; len(top) > 0 && (top[len(top)-1] == "(" || top[len(top)-1] == "[") {
			return readRead
		}
		depth := 0
		for j := i + 1; j < len(toks); j++ {
			t := toks[j]
			switch {
			case t == "(" || t == "[" || t == "{":
				depth++
			case t == ")" || t == "]" || t == "}":
				if depth == 0 {
					return readRead
				}
				depth--
			case depth == 0:
				if t == ":=" {
					return readUnsure
				} else if t == "=" {
					return readNone
				} else if t == ";" || (t == "\n" && toks[j-1] != ",") {
					return readRead
				}
			}
		}
		return readUnsure
	}
	return readRead
}




func isStatementEnd(tok string) bool {
	return tok == ";" || tok == "\n" || tok == "{" || tok == "}"
}






func isGoIdentStart(c byte) bool {
	return c == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c >= 0x80
}




func isGoIdentPart(c byte) bool {
	return isGoIdentStart(c) || (c >= '0' && c <= '9')
}






const backquote = byte(96)




var goOperators = []string{
	"<<=", ">>=", "&^=", "...",
	":=", "==", "!=", "<=", ">=", "&&", "||", "<-", "++", "--",
	"+=", "-=", "*=", "/=", "%=", "&=", "|=", "^=", "<<", ">>", "&^",
}






func goTokens(s string) (toks []string, ok bool) {
	n := len(s)
	i := 0
	for i < n {
		c := s[i]
		switch {
		case c == '\n':
			toks = append(toks, "\n")
			i++
		case c == ' ' || c == '\t' || c == '\r' || c == '\f':
			i++
		case c == '/' && i+1 < n && s[i+1] == '/':
			for i < n && s[i] != '\n' {
				i++
			}
		case c == '/' && i+1 < n && s[i+1] == '*':
			end := strings.Index(s[i+2:], "*/")
			if end < 0 {
				return nil, false
			}
			end += i + 2
			if strings.IndexByte(s[i+2:end], '\n') >= 0 {
				toks = append(toks, "\n")
			}
			i = end + 2
		case c == '"' || c == '\'':
			j := i + 1
			for j < n && s[j] != c && s[j] != '\n' {
				if s[j] == '\\' {
					j++
				}
				j++
			}
			if j >= n || s[j] != c {
				return nil, false
			}
			toks = append(toks, "0")
			i = j + 1
		case c == backquote:
			end := strings.IndexByte(s[i+1:], backquote)
			if end < 0 {
				return nil, false
			}
			toks = append(toks, "0")
			i += end + 2
		case isGoIdentStart(c):
			j := i
			for j < n && isGoIdentPart(s[j]) {
				j++
			}
			toks = append(toks, s[i:j])
			i = j
		case c >= '0' && c <= '9':
			for i < n && (isGoIdentPart(s[i]) || s[i] == '.') {
				i++
			}
			toks = append(toks, "0")
		default:
			op := s[i : i+1]
			for _, o := range goOperators {
				if strings.HasPrefix(s[i:], o) {
					op = o
					break
				}
			}
			toks = append(toks, op)
			i += len(op)
		}
	}
	return toks, true
}




var _ = aterm.GetInstance

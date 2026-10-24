// tom-source-sha256: 7a2ce4986d3ef7f9b7c189f4792022e4877ef92acd0af4fda332e1556f05fb78

package backend











import (
	"regexp"
	"strings"

	. "tom/engine/adt"
	"tom/engine/platform"
	"tom/engine/tools"
	"tom/library/aterm"
)

func tom_generate_equal_term_char(t1 rune, t2 rune) bool { return t1 == t2;  }; func tom_generate_is_sort_char(t rune) bool { return true;  }; func tom_generate_equal_term_String(t1 string, t2 string) bool { return t1 == t2;  }; func tom_generate_is_sort_String(t string) bool { return true;  }; func tom_generate_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_generate_is_sort_int(t int64) bool { return true;  }; func tom_generate_equal_term_boolean(t1 bool, t2 bool) bool { return t1 == t2;  }; func tom_generate_is_sort_boolean(t bool) bool { return true;  }; func tom_generate_equal_term_ATerm(t1  aterm.ATerm , t2  aterm.ATerm ) bool { return t1 == t2;  }; func tom_generate_is_sort_ATerm(t any) bool { return func() bool { _, ok := any(t).(aterm.ATerm); return ok }();  }; func tom_generate_equal_term_ATermList(l1  *aterm.ATermList , l2  *aterm.ATermList ) bool { return l1 == l2;  }; func tom_generate_is_sort_ATermList(t any) bool { return func() bool { _, ok := any(t).(*aterm.ATermList); return ok }();  }; func tom_generate_equal_term_AFun(t1  *aterm.AFun , t2  *aterm.AFun ) bool { return t1 == t2;  }; func tom_generate_is_sort_AFun(t any) bool { return func() bool { _, ok := any(t).(*aterm.AFun); return ok }();  }; func tom_generate_equal_term_CstProgram(t1  CstProgram , t2  CstProgram ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstProgram(t any) bool { return func() bool { _, ok := (t).(CstProgram); return ok }();  }; func tom_generate_equal_term_CstBlockList(t1  CstBlockList , t2  CstBlockList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstBlockList(t any) bool { return func() bool { _, ok := (t).(CstBlockList); return ok }();  }; func tom_generate_equal_term_CstBlock(t1  CstBlock , t2  CstBlock ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstBlock(t any) bool { return func() bool { _, ok := (t).(CstBlock); return ok }();  }; func tom_generate_equal_term_CstConstraintAction(t1  CstConstraintAction , t2  CstConstraintAction ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstConstraintAction(t any) bool { return func() bool { _, ok := (t).(CstConstraintAction); return ok }();  }; func tom_generate_equal_term_CstConstraintActionList(t1  CstConstraintActionList , t2  CstConstraintActionList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstConstraintActionList(t any) bool { return func() bool { _, ok := (t).(CstConstraintActionList); return ok }();  }; func tom_generate_equal_term_CstConstraint(t1  CstConstraint , t2  CstConstraint ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstConstraint(t any) bool { return func() bool { _, ok := (t).(CstConstraint); return ok }();  }; func tom_generate_equal_term_CstConstraintList(t1  CstConstraintList , t2  CstConstraintList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstConstraintList(t any) bool { return func() bool { _, ok := (t).(CstConstraintList); return ok }();  }; func tom_generate_equal_term_CstTerm(t1  CstTerm , t2  CstTerm ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstTerm(t any) bool { return func() bool { _, ok := (t).(CstTerm); return ok }();  }; func tom_generate_equal_term_CstTermList(t1  CstTermList , t2  CstTermList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstTermList(t any) bool { return func() bool { _, ok := (t).(CstTermList); return ok }();  }; func tom_generate_equal_term_CstType(t1  CstType , t2  CstType ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstType(t any) bool { return func() bool { _, ok := (t).(CstType); return ok }();  }; func tom_generate_equal_term_CstPattern(t1  CstPattern , t2  CstPattern ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstPattern(t any) bool { return func() bool { _, ok := (t).(CstPattern); return ok }();  }; func tom_generate_equal_term_CstSymbolList(t1  CstSymbolList , t2  CstSymbolList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstSymbolList(t any) bool { return func() bool { _, ok := (t).(CstSymbolList); return ok }();  }; func tom_generate_equal_term_CstSymbol(t1  CstSymbol , t2  CstSymbol ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstSymbol(t any) bool { return func() bool { _, ok := (t).(CstSymbol); return ok }();  }; func tom_generate_equal_term_CstTheory(t1  CstTheory , t2  CstTheory ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstTheory(t any) bool { return func() bool { _, ok := (t).(CstTheory); return ok }();  }; func tom_generate_equal_term_CstPatternList(t1  CstPatternList , t2  CstPatternList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstPatternList(t any) bool { return func() bool { _, ok := (t).(CstPatternList); return ok }();  }; func tom_generate_equal_term_CstPairPatternList(t1  CstPairPatternList , t2  CstPairPatternList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstPairPatternList(t any) bool { return func() bool { _, ok := (t).(CstPairPatternList); return ok }();  }; func tom_generate_equal_term_CstPairPattern(t1  CstPairPattern , t2  CstPairPattern ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstPairPattern(t any) bool { return func() bool { _, ok := (t).(CstPairPattern); return ok }();  }; func tom_generate_equal_term_CstOperatorList(t1  CstOperatorList , t2  CstOperatorList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstOperatorList(t any) bool { return func() bool { _, ok := (t).(CstOperatorList); return ok }();  }; func tom_generate_equal_term_CstOperator(t1  CstOperator , t2  CstOperator ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstOperator(t any) bool { return func() bool { _, ok := (t).(CstOperator); return ok }();  }; func tom_generate_equal_term_CstSlotList(t1  CstSlotList , t2  CstSlotList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstSlotList(t any) bool { return func() bool { _, ok := (t).(CstSlotList); return ok }();  }; func tom_generate_equal_term_CstSlot(t1  CstSlot , t2  CstSlot ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstSlot(t any) bool { return func() bool { _, ok := (t).(CstSlot); return ok }();  }; func tom_generate_equal_term_CstBQTerm(t1  CstBQTerm , t2  CstBQTerm ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstBQTerm(t any) bool { return func() bool { _, ok := (t).(CstBQTerm); return ok }();  }; func tom_generate_equal_term_CstPairSlotBQTerm(t1  CstPairSlotBQTerm , t2  CstPairSlotBQTerm ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstPairSlotBQTerm(t any) bool { return func() bool { _, ok := (t).(CstPairSlotBQTerm); return ok }();  }; func tom_generate_equal_term_CstBQTermList(t1  CstBQTermList , t2  CstBQTermList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstBQTermList(t any) bool { return func() bool { _, ok := (t).(CstBQTermList); return ok }();  }; func tom_generate_equal_term_CstPairSlotBQTermList(t1  CstPairSlotBQTermList , t2  CstPairSlotBQTermList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstPairSlotBQTermList(t any) bool { return func() bool { _, ok := (t).(CstPairSlotBQTermList); return ok }();  }; func tom_generate_equal_term_CstName(t1  CstName , t2  CstName ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstName(t any) bool { return func() bool { _, ok := (t).(CstName); return ok }();  }; func tom_generate_equal_term_CstNameList(t1  CstNameList , t2  CstNameList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstNameList(t any) bool { return func() bool { _, ok := (t).(CstNameList); return ok }();  }; func tom_generate_equal_term_CstOptionList(t1  CstOptionList , t2  CstOptionList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstOptionList(t any) bool { return func() bool { _, ok := (t).(CstOptionList); return ok }();  }; func tom_generate_equal_term_CstOption(t1  CstOption , t2  CstOption ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstOption(t any) bool { return func() bool { _, ok := (t).(CstOption); return ok }();  }; func tom_generate_equal_term_CstVisit(t1  CstVisit , t2  CstVisit ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstVisit(t any) bool { return func() bool { _, ok := (t).(CstVisit); return ok }();  }; func tom_generate_equal_term_CstVisitList(t1  CstVisitList , t2  CstVisitList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CstVisitList(t any) bool { return func() bool { _, ok := (t).(CstVisitList); return ok }();  }; func tom_generate_equal_term_TargetLanguage(t1  TargetLanguage , t2  TargetLanguage ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_TargetLanguage(t any) bool { return func() bool { _, ok := (t).(TargetLanguage); return ok }();  }; func tom_generate_equal_term_CodeList(t1  CodeList , t2  CodeList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CodeList(t any) bool { return func() bool { _, ok := (t).(CodeList); return ok }();  }; func tom_generate_equal_term_Code(t1  Code , t2  Code ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_Code(t any) bool { return func() bool { _, ok := (t).(Code); return ok }();  }; func tom_generate_equal_term_BQTermList(t1  BQTermList , t2  BQTermList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_BQTermList(t any) bool { return func() bool { _, ok := (t).(BQTermList); return ok }();  }; func tom_generate_equal_term_CompositeMember(t1  CompositeMember , t2  CompositeMember ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_CompositeMember(t any) bool { return func() bool { _, ok := (t).(CompositeMember); return ok }();  }; func tom_generate_equal_term_BQTerm(t1  BQTerm , t2  BQTerm ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_BQTerm(t any) bool { return func() bool { _, ok := (t).(BQTerm); return ok }();  }; func tom_generate_is_fun_sym_TL(t  TargetLanguage ) bool { return func() bool { _, ok := (t).(*TLTargetLanguage); return ok }();  }; func tom_generate_get_slot_TL_Code(t  TargetLanguage )  string  { return (t).(*TLTargetLanguage).Code;  }; func tom_generate_get_slot_TL_Start(t  TargetLanguage )  TextPosition  { return (t).(*TLTargetLanguage).Start;  }; func tom_generate_get_slot_TL_End(t  TargetLanguage )  TextPosition  { return (t).(*TLTargetLanguage).End;  }; func tom_generate_is_fun_sym_ITL(t  TargetLanguage ) bool { return func() bool { _, ok := (t).(*ITLTargetLanguage); return ok }();  }; func tom_generate_get_slot_ITL_Code(t  TargetLanguage )  string  { return (t).(*ITLTargetLanguage).Code;  }; func tom_generate_is_fun_sym_Comment(t  TargetLanguage ) bool { return func() bool { _, ok := (t).(*CommentTargetLanguage); return ok }();  }; func tom_generate_get_slot_Comment_Code(t  TargetLanguage )  string  { return (t).(*CommentTargetLanguage).Code;  }; func tom_generate_is_fun_sym_TargetLanguageToCode(t  Code ) bool { return func() bool { _, ok := (t).(*TargetLanguageToCodeCode); return ok }();  }; func tom_generate_get_slot_TargetLanguageToCode_Tl(t  Code )  TargetLanguage  { return (t).(*TargetLanguageToCodeCode).Tl;  }; func tom_generate_is_fun_sym_InstructionToCode(t  Code ) bool { return func() bool { _, ok := (t).(*InstructionToCodeCode); return ok }();  }; func tom_generate_get_slot_InstructionToCode_AstInstruction(t  Code )  Instruction  { return (t).(*InstructionToCodeCode).AstInstruction;  }; func tom_generate_is_fun_sym_DeclarationToCode(t  Code ) bool { return func() bool { _, ok := (t).(*DeclarationToCodeCode); return ok }();  }; func tom_generate_get_slot_DeclarationToCode_AstDeclaration(t  Code )  Declaration  { return (t).(*DeclarationToCodeCode).AstDeclaration;  }; func tom_generate_is_fun_sym_BQTermToCode(t  Code ) bool { return func() bool { _, ok := (t).(*BQTermToCodeCode); return ok }();  }; func tom_generate_get_slot_BQTermToCode_Bq(t  Code )  BQTerm  { return (t).(*BQTermToCodeCode).Bq;  }; func tom_generate_is_fun_sym_Tom(t  Code ) bool { return func() bool { _, ok := (t).(*TomCode); return ok }();  }; func tom_generate_get_slot_Tom_CodeList(t  Code )  CodeList  { return (t).(*TomCode).CodeList;  }; func tom_generate_is_fun_sym_TomInclude(t  Code ) bool { return func() bool { _, ok := (t).(*TomIncludeCode); return ok }();  }; func tom_generate_get_slot_TomInclude_CodeList(t  Code )  CodeList  { return (t).(*TomIncludeCode).CodeList;  }; func tom_generate_is_fun_sym_concBQTerm(t  BQTermList ) bool { return func() bool { _, ok := (t).(*ConcBQTermBQTermList); return ok }();  }; func tom_generate_empty_list_concBQTerm()  BQTermList  { return (EmptyConcBQTerm()) }; func tom_generate_cons_list_concBQTerm(e  BQTerm , l  BQTermList )  BQTermList  { return (ConsConcBQTerm(e,l)) }; func tom_generate_get_head_concBQTerm_BQTermList(l  BQTermList )  BQTerm  { return (l).GetHeadconcBQTerm();  }; func tom_generate_get_tail_concBQTerm_BQTermList(l  BQTermList )  BQTermList  { return (l).GetTailconcBQTerm();  }; func tom_generate_is_empty_concBQTerm_BQTermList(l  BQTermList ) bool { return (l).IsEmptyconcBQTerm();  }; func tom_generate_append_list_concBQTerm(l1  BQTermList , l2  BQTermList )  BQTermList  { if tom_generate_is_empty_concBQTerm_BQTermList(l1) { return l2 } else if tom_generate_is_empty_concBQTerm_BQTermList(l2) { return l1 } else if tom_generate_is_empty_concBQTerm_BQTermList(tom_generate_get_tail_concBQTerm_BQTermList(l1)) { return tom_generate_cons_list_concBQTerm(tom_generate_get_head_concBQTerm_BQTermList(l1), l2) } else { return tom_generate_cons_list_concBQTerm(tom_generate_get_head_concBQTerm_BQTermList(l1), tom_generate_append_list_concBQTerm(tom_generate_get_tail_concBQTerm_BQTermList(l1), l2)) }; }; func tom_generate_get_slice_concBQTerm(begin  BQTermList , end  BQTermList , tail  BQTermList )  BQTermList  { if tom_generate_equal_term_BQTermList(begin, end) { return tail } else if tom_generate_equal_term_BQTermList(end, tail) && (tom_generate_is_empty_concBQTerm_BQTermList(end) || tom_generate_equal_term_BQTermList(end, tom_generate_empty_list_concBQTerm())) { return begin }; return tom_generate_cons_list_concBQTerm(tom_generate_get_head_concBQTerm_BQTermList(begin), tom_generate_get_slice_concBQTerm(tom_generate_get_tail_concBQTerm_BQTermList(begin), end, tail)) }; func tom_generate_is_fun_sym_CompositeBQTerm(t  CompositeMember ) bool { return func() bool { _, ok := (t).(*CompositeBQTermCompositeMember); return ok }();  }; func tom_generate_get_slot_CompositeBQTerm_term(t  CompositeMember )  BQTerm  { return (t).(*CompositeBQTermCompositeMember).Term;  }; func tom_generate_is_fun_sym_CompositeTL(t  CompositeMember ) bool { return func() bool { _, ok := (t).(*CompositeTLCompositeMember); return ok }();  }; func tom_generate_get_slot_CompositeTL_Tl(t  CompositeMember )  TargetLanguage  { return (t).(*CompositeTLCompositeMember).Tl;  }; func tom_generate_is_fun_sym_BQVariable(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BQVariableBQTerm); return ok }();  }; func tom_generate_make_BQVariable(Options  OptionList , AstName  TomName , AstType  TomType )  BQTerm  { return (MakeBQVariable(Options,AstName,AstType)) }; func tom_generate_get_slot_BQVariable_Options(t  BQTerm )  OptionList  { return (t).(*BQVariableBQTerm).Options;  }; func tom_generate_get_slot_BQVariable_AstName(t  BQTerm )  TomName  { return (t).(*BQVariableBQTerm).AstName;  }; func tom_generate_get_slot_BQVariable_AstType(t  BQTerm )  TomType  { return (t).(*BQVariableBQTerm).AstType;  }; func tom_generate_is_fun_sym_BQVariableStar(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BQVariableStarBQTerm); return ok }();  }; func tom_generate_get_slot_BQVariableStar_Options(t  BQTerm )  OptionList  { return (t).(*BQVariableStarBQTerm).Options;  }; func tom_generate_get_slot_BQVariableStar_AstName(t  BQTerm )  TomName  { return (t).(*BQVariableStarBQTerm).AstName;  }; func tom_generate_get_slot_BQVariableStar_AstType(t  BQTerm )  TomType  { return (t).(*BQVariableStarBQTerm).AstType;  }; func tom_generate_is_fun_sym_Composite(t  BQTerm ) bool { return func() bool { _, ok := (t).(*CompositeBQTerm); return ok }();  }; func tom_generate_empty_list_Composite()  BQTerm  { return (EmptyComposite()) }; func tom_generate_cons_list_Composite(e  CompositeMember , l  BQTerm )  BQTerm  { return (ConsComposite(e,l)) }; func tom_generate_get_head_Composite_BQTerm(l  BQTerm )  CompositeMember  { return (l).GetHeadComposite();  }; func tom_generate_get_tail_Composite_BQTerm(l  BQTerm )  BQTerm  { return (l).GetTailComposite();  }; func tom_generate_is_empty_Composite_BQTerm(l  BQTerm ) bool { return (l).IsEmptyComposite();  }; func tom_generate_append_list_Composite(l1  BQTerm , l2  BQTerm )  BQTerm  { if tom_generate_is_empty_Composite_BQTerm(l1) { return l2 } else if tom_generate_is_empty_Composite_BQTerm(l2) { return l1 } else if tom_generate_is_empty_Composite_BQTerm(tom_generate_get_tail_Composite_BQTerm(l1)) { return tom_generate_cons_list_Composite(tom_generate_get_head_Composite_BQTerm(l1), l2) } else { return tom_generate_cons_list_Composite(tom_generate_get_head_Composite_BQTerm(l1), tom_generate_append_list_Composite(tom_generate_get_tail_Composite_BQTerm(l1), l2)) }; }; func tom_generate_get_slice_Composite(begin  BQTerm , end  BQTerm , tail  BQTerm )  BQTerm  { if tom_generate_equal_term_BQTerm(begin, end) { return tail } else if tom_generate_equal_term_BQTerm(end, tail) && (tom_generate_is_empty_Composite_BQTerm(end) || tom_generate_equal_term_BQTerm(end, tom_generate_empty_list_Composite())) { return begin }; return tom_generate_cons_list_Composite(tom_generate_get_head_Composite_BQTerm(begin), tom_generate_get_slice_Composite(tom_generate_get_tail_Composite_BQTerm(begin), end, tail)) }; func tom_generate_is_fun_sym_FunctionCall(t  BQTerm ) bool { return func() bool { _, ok := (t).(*FunctionCallBQTerm); return ok }();  }; func tom_generate_get_slot_FunctionCall_AstName(t  BQTerm )  TomName  { return (t).(*FunctionCallBQTerm).AstName;  }; func tom_generate_get_slot_FunctionCall_AstType(t  BQTerm )  TomType  { return (t).(*FunctionCallBQTerm).AstType;  }; func tom_generate_get_slot_FunctionCall_Args(t  BQTerm )  BQTermList  { return (t).(*FunctionCallBQTerm).Args;  }; func tom_generate_is_fun_sym_BuildConstant(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BuildConstantBQTerm); return ok }();  }; func tom_generate_get_slot_BuildConstant_AstName(t  BQTerm )  TomName  { return (t).(*BuildConstantBQTerm).AstName;  }; func tom_generate_is_fun_sym_BuildTerm(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BuildTermBQTerm); return ok }();  }; func tom_generate_get_slot_BuildTerm_AstName(t  BQTerm )  TomName  { return (t).(*BuildTermBQTerm).AstName;  }; func tom_generate_get_slot_BuildTerm_Args(t  BQTerm )  BQTermList  { return (t).(*BuildTermBQTerm).Args;  }; func tom_generate_get_slot_BuildTerm_ModuleName(t  BQTerm )  string  { return (t).(*BuildTermBQTerm).ModuleName;  }; func tom_generate_is_fun_sym_BuildEmptyList(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BuildEmptyListBQTerm); return ok }();  }; func tom_generate_get_slot_BuildEmptyList_AstName(t  BQTerm )  TomName  { return (t).(*BuildEmptyListBQTerm).AstName;  }; func tom_generate_is_fun_sym_BuildConsList(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BuildConsListBQTerm); return ok }();  }; func tom_generate_get_slot_BuildConsList_AstName(t  BQTerm )  TomName  { return (t).(*BuildConsListBQTerm).AstName;  }; func tom_generate_get_slot_BuildConsList_HeadTerm(t  BQTerm )  BQTerm  { return (t).(*BuildConsListBQTerm).HeadTerm;  }; func tom_generate_get_slot_BuildConsList_TailTerm(t  BQTerm )  BQTerm  { return (t).(*BuildConsListBQTerm).TailTerm;  }; func tom_generate_is_fun_sym_BuildAppendList(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BuildAppendListBQTerm); return ok }();  }; func tom_generate_get_slot_BuildAppendList_AstName(t  BQTerm )  TomName  { return (t).(*BuildAppendListBQTerm).AstName;  }; func tom_generate_get_slot_BuildAppendList_HeadTerm(t  BQTerm )  BQTerm  { return (t).(*BuildAppendListBQTerm).HeadTerm;  }; func tom_generate_get_slot_BuildAppendList_TailTerm(t  BQTerm )  BQTerm  { return (t).(*BuildAppendListBQTerm).TailTerm;  }; func tom_generate_is_fun_sym_BuildEmptyArray(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BuildEmptyArrayBQTerm); return ok }();  }; func tom_generate_get_slot_BuildEmptyArray_AstName(t  BQTerm )  TomName  { return (t).(*BuildEmptyArrayBQTerm).AstName;  }; func tom_generate_get_slot_BuildEmptyArray_Size(t  BQTerm )  BQTerm  { return (t).(*BuildEmptyArrayBQTerm).Size;  }; func tom_generate_is_fun_sym_BuildConsArray(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BuildConsArrayBQTerm); return ok }();  }; func tom_generate_get_slot_BuildConsArray_AstName(t  BQTerm )  TomName  { return (t).(*BuildConsArrayBQTerm).AstName;  }; func tom_generate_get_slot_BuildConsArray_HeadTerm(t  BQTerm )  BQTerm  { return (t).(*BuildConsArrayBQTerm).HeadTerm;  }; func tom_generate_get_slot_BuildConsArray_TailTerm(t  BQTerm )  BQTerm  { return (t).(*BuildConsArrayBQTerm).TailTerm;  }; func tom_generate_is_fun_sym_BuildAppendArray(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BuildAppendArrayBQTerm); return ok }();  }; func tom_generate_get_slot_BuildAppendArray_AstName(t  BQTerm )  TomName  { return (t).(*BuildAppendArrayBQTerm).AstName;  }; func tom_generate_get_slot_BuildAppendArray_HeadTerm(t  BQTerm )  BQTerm  { return (t).(*BuildAppendArrayBQTerm).HeadTerm;  }; func tom_generate_get_slot_BuildAppendArray_TailTerm(t  BQTerm )  BQTerm  { return (t).(*BuildAppendArrayBQTerm).TailTerm;  }; func tom_generate_is_fun_sym_ExpressionToBQTerm(t  BQTerm ) bool { return func() bool { _, ok := (t).(*ExpressionToBQTermBQTerm); return ok }();  }; func tom_generate_make_ExpressionToBQTerm(Exp  Expression )  BQTerm  { return (MakeExpressionToBQTerm(Exp)) }; func tom_generate_get_slot_ExpressionToBQTerm_Exp(t  BQTerm )  Expression  { return (t).(*ExpressionToBQTermBQTerm).Exp;  }; func tom_generate_equal_term_Symbol(t1  Symbol , t2  Symbol ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_Symbol(t any) bool { return func() bool { _, ok := (t).(Symbol); return ok }();  }; func tom_generate_equal_term_Variable(t1  Variable , t2  Variable ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_Variable(t any) bool { return func() bool { _, ok := (t).(Variable); return ok }();  }; func tom_generate_equal_term_AbsTerm(t1  AbsTerm , t2  AbsTerm ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_AbsTerm(t any) bool { return func() bool { _, ok := (t).(AbsTerm); return ok }();  }; func tom_generate_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_generate_equal_term_Expr(t1  Expr , t2  Expr ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_Expr(t any) bool { return func() bool { _, ok := (t).(Expr); return ok }();  }; func tom_generate_equal_term_Instr(t1  Instr , t2  Instr ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_Instr(t any) bool { return func() bool { _, ok := (t).(Instr); return ok }();  }; func tom_generate_equal_term_InstrList(t1  InstrList , t2  InstrList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_InstrList(t any) bool { return func() bool { _, ok := (t).(InstrList); return ok }();  }; func tom_generate_equal_term_Substitution(t1  Substitution , t2  Substitution ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_Substitution(t any) bool { return func() bool { _, ok := (t).(Substitution); return ok }();  }; func tom_generate_equal_term_SubstitutionList(t1  SubstitutionList , t2  SubstitutionList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_SubstitutionList(t any) bool { return func() bool { _, ok := (t).(SubstitutionList); return ok }();  }; func tom_generate_equal_term_Environment(t1  Environment , t2  Environment ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_Environment(t any) bool { return func() bool { _, ok := (t).(Environment); return ok }();  }; func tom_generate_equal_term_Seq(t1  Seq , t2  Seq ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_Seq(t any) bool { return func() bool { _, ok := (t).(Seq); return ok }();  }; func tom_generate_equal_term_TermList(t1  TermList , t2  TermList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_TermList(t any) bool { return func() bool { _, ok := (t).(TermList); return ok }();  }; func tom_generate_equal_term_ExprList(t1  ExprList , t2  ExprList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_ExprList(t any) bool { return func() bool { _, ok := (t).(ExprList); return ok }();  }; func tom_generate_equal_term_Deriv(t1  Deriv , t2  Deriv ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_Deriv(t any) bool { return func() bool { _, ok := (t).(Deriv); return ok }();  }; func tom_generate_equal_term_DerivTree(t1  DerivTree , t2  DerivTree ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_DerivTree(t any) bool { return func() bool { _, ok := (t).(DerivTree); return ok }();  }; func tom_generate_equal_term_ElementaryTheory(t1  ElementaryTheory , t2  ElementaryTheory ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_ElementaryTheory(t any) bool { return func() bool { _, ok := (t).(ElementaryTheory); return ok }();  }; func tom_generate_equal_term_Theory(t1  Theory , t2  Theory ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_Theory(t any) bool { return func() bool { _, ok := (t).(Theory); return ok }();  }; func tom_generate_equal_term_Constraint(t1  Constraint , t2  Constraint ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_Constraint(t any) bool { return func() bool { _, ok := (t).(Constraint); return ok }();  }; func tom_generate_equal_term_NumericConstraintType(t1  NumericConstraintType , t2  NumericConstraintType ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_NumericConstraintType(t any) bool { return func() bool { _, ok := (t).(NumericConstraintType); return ok }();  }; func tom_generate_equal_term_ConstraintList(t1  ConstraintList , t2  ConstraintList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_ConstraintList(t any) bool { return func() bool { _, ok := (t).(ConstraintList); return ok }();  }; func tom_generate_equal_term_Declaration(t1  Declaration , t2  Declaration ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_Declaration(t any) bool { return func() bool { _, ok := (t).(Declaration); return ok }();  }; func tom_generate_equal_term_DeclarationList(t1  DeclarationList , t2  DeclarationList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_DeclarationList(t any) bool { return func() bool { _, ok := (t).(DeclarationList); return ok }();  }; func tom_generate_is_fun_sym_TypeTermDecl(t  Declaration ) bool { return func() bool { _, ok := (t).(*TypeTermDeclDeclaration); return ok }();  }; func tom_generate_get_slot_TypeTermDecl_AstName(t  Declaration )  TomName  { return (t).(*TypeTermDeclDeclaration).AstName;  }; func tom_generate_get_slot_TypeTermDecl_Declarations(t  Declaration )  DeclarationList  { return (t).(*TypeTermDeclDeclaration).Declarations;  }; func tom_generate_get_slot_TypeTermDecl_OrgTrack(t  Declaration )  Option  { return (t).(*TypeTermDeclDeclaration).OrgTrack;  }; func tom_generate_is_fun_sym_IsFsymDecl(t  Declaration ) bool { return func() bool { _, ok := (t).(*IsFsymDeclDeclaration); return ok }();  }; func tom_generate_get_slot_IsFsymDecl_AstName(t  Declaration )  TomName  { return (t).(*IsFsymDeclDeclaration).AstName;  }; func tom_generate_get_slot_IsFsymDecl_Variable(t  Declaration )  BQTerm  { return (t).(*IsFsymDeclDeclaration).Variable;  }; func tom_generate_get_slot_IsFsymDecl_Expr(t  Declaration )  Expression  { return (t).(*IsFsymDeclDeclaration).Expr;  }; func tom_generate_get_slot_IsFsymDecl_OrgTrack(t  Declaration )  Option  { return (t).(*IsFsymDeclDeclaration).OrgTrack;  }; func tom_generate_is_fun_sym_GetSlotDecl(t  Declaration ) bool { return func() bool { _, ok := (t).(*GetSlotDeclDeclaration); return ok }();  }; func tom_generate_get_slot_GetSlotDecl_AstName(t  Declaration )  TomName  { return (t).(*GetSlotDeclDeclaration).AstName;  }; func tom_generate_get_slot_GetSlotDecl_SlotName(t  Declaration )  TomName  { return (t).(*GetSlotDeclDeclaration).SlotName;  }; func tom_generate_get_slot_GetSlotDecl_Variable(t  Declaration )  BQTerm  { return (t).(*GetSlotDeclDeclaration).Variable;  }; func tom_generate_get_slot_GetSlotDecl_Expr(t  Declaration )  Expression  { return (t).(*GetSlotDeclDeclaration).Expr;  }; func tom_generate_get_slot_GetSlotDecl_OrgTrack(t  Declaration )  Option  { return (t).(*GetSlotDeclDeclaration).OrgTrack;  }; func tom_generate_is_fun_sym_GetDefaultDecl(t  Declaration ) bool { return func() bool { _, ok := (t).(*GetDefaultDeclDeclaration); return ok }();  }; func tom_generate_get_slot_GetDefaultDecl_AstName(t  Declaration )  TomName  { return (t).(*GetDefaultDeclDeclaration).AstName;  }; func tom_generate_get_slot_GetDefaultDecl_SlotName(t  Declaration )  TomName  { return (t).(*GetDefaultDeclDeclaration).SlotName;  }; func tom_generate_get_slot_GetDefaultDecl_Expr(t  Declaration )  Expression  { return (t).(*GetDefaultDeclDeclaration).Expr;  }; func tom_generate_get_slot_GetDefaultDecl_OrgTrack(t  Declaration )  Option  { return (t).(*GetDefaultDeclDeclaration).OrgTrack;  }; func tom_generate_is_fun_sym_ImplementDecl(t  Declaration ) bool { return func() bool { _, ok := (t).(*ImplementDeclDeclaration); return ok }();  }; func tom_generate_get_slot_ImplementDecl_AstName(t  Declaration )  TomName  { return (t).(*ImplementDeclDeclaration).AstName;  }; func tom_generate_get_slot_ImplementDecl_Expr(t  Declaration )  Expression  { return (t).(*ImplementDeclDeclaration).Expr;  }; func tom_generate_get_slot_ImplementDecl_orgTrack(t  Declaration )  Option  { return (t).(*ImplementDeclDeclaration).OrgTrack;  }; func tom_generate_is_fun_sym_EqualTermDecl(t  Declaration ) bool { return func() bool { _, ok := (t).(*EqualTermDeclDeclaration); return ok }();  }; func tom_generate_get_slot_EqualTermDecl_TermArg1(t  Declaration )  BQTerm  { return (t).(*EqualTermDeclDeclaration).TermArg1;  }; func tom_generate_get_slot_EqualTermDecl_TermArg2(t  Declaration )  BQTerm  { return (t).(*EqualTermDeclDeclaration).TermArg2;  }; func tom_generate_get_slot_EqualTermDecl_Expr(t  Declaration )  Expression  { return (t).(*EqualTermDeclDeclaration).Expr;  }; func tom_generate_get_slot_EqualTermDecl_OrgTrack(t  Declaration )  Option  { return (t).(*EqualTermDeclDeclaration).OrgTrack;  }; func tom_generate_is_fun_sym_IsSortDecl(t  Declaration ) bool { return func() bool { _, ok := (t).(*IsSortDeclDeclaration); return ok }();  }; func tom_generate_get_slot_IsSortDecl_TermArg(t  Declaration )  BQTerm  { return (t).(*IsSortDeclDeclaration).TermArg;  }; func tom_generate_get_slot_IsSortDecl_Expr(t  Declaration )  Expression  { return (t).(*IsSortDeclDeclaration).Expr;  }; func tom_generate_get_slot_IsSortDecl_OrgTrack(t  Declaration )  Option  { return (t).(*IsSortDeclDeclaration).OrgTrack;  }; func tom_generate_is_fun_sym_GetHeadDecl(t  Declaration ) bool { return func() bool { _, ok := (t).(*GetHeadDeclDeclaration); return ok }();  }; func tom_generate_get_slot_GetHeadDecl_Opname(t  Declaration )  TomName  { return (t).(*GetHeadDeclDeclaration).Opname;  }; func tom_generate_get_slot_GetHeadDecl_Codomain(t  Declaration )  TomType  { return (t).(*GetHeadDeclDeclaration).Codomain;  }; func tom_generate_get_slot_GetHeadDecl_Variable(t  Declaration )  BQTerm  { return (t).(*GetHeadDeclDeclaration).Variable;  }; func tom_generate_get_slot_GetHeadDecl_Expr(t  Declaration )  Expression  { return (t).(*GetHeadDeclDeclaration).Expr;  }; func tom_generate_get_slot_GetHeadDecl_OrgTrack(t  Declaration )  Option  { return (t).(*GetHeadDeclDeclaration).OrgTrack;  }; func tom_generate_is_fun_sym_GetTailDecl(t  Declaration ) bool { return func() bool { _, ok := (t).(*GetTailDeclDeclaration); return ok }();  }; func tom_generate_get_slot_GetTailDecl_Opname(t  Declaration )  TomName  { return (t).(*GetTailDeclDeclaration).Opname;  }; func tom_generate_get_slot_GetTailDecl_Variable(t  Declaration )  BQTerm  { return (t).(*GetTailDeclDeclaration).Variable;  }; func tom_generate_get_slot_GetTailDecl_Expr(t  Declaration )  Expression  { return (t).(*GetTailDeclDeclaration).Expr;  }; func tom_generate_get_slot_GetTailDecl_OrgTrack(t  Declaration )  Option  { return (t).(*GetTailDeclDeclaration).OrgTrack;  }; func tom_generate_is_fun_sym_IsEmptyDecl(t  Declaration ) bool { return func() bool { _, ok := (t).(*IsEmptyDeclDeclaration); return ok }();  }; func tom_generate_get_slot_IsEmptyDecl_Opname(t  Declaration )  TomName  { return (t).(*IsEmptyDeclDeclaration).Opname;  }; func tom_generate_get_slot_IsEmptyDecl_Variable(t  Declaration )  BQTerm  { return (t).(*IsEmptyDeclDeclaration).Variable;  }; func tom_generate_get_slot_IsEmptyDecl_Expr(t  Declaration )  Expression  { return (t).(*IsEmptyDeclDeclaration).Expr;  }; func tom_generate_get_slot_IsEmptyDecl_OrgTrack(t  Declaration )  Option  { return (t).(*IsEmptyDeclDeclaration).OrgTrack;  }; func tom_generate_is_fun_sym_MakeEmptyList(t  Declaration ) bool { return func() bool { _, ok := (t).(*MakeEmptyListDeclaration); return ok }();  }; func tom_generate_get_slot_MakeEmptyList_AstName(t  Declaration )  TomName  { return (t).(*MakeEmptyListDeclaration).AstName;  }; func tom_generate_get_slot_MakeEmptyList_Instr(t  Declaration )  Instruction  { return (t).(*MakeEmptyListDeclaration).Instr;  }; func tom_generate_get_slot_MakeEmptyList_OrgTrack(t  Declaration )  Option  { return (t).(*MakeEmptyListDeclaration).OrgTrack;  }; func tom_generate_is_fun_sym_MakeAddList(t  Declaration ) bool { return func() bool { _, ok := (t).(*MakeAddListDeclaration); return ok }();  }; func tom_generate_get_slot_MakeAddList_AstName(t  Declaration )  TomName  { return (t).(*MakeAddListDeclaration).AstName;  }; func tom_generate_get_slot_MakeAddList_VarElt(t  Declaration )  BQTerm  { return (t).(*MakeAddListDeclaration).VarElt;  }; func tom_generate_get_slot_MakeAddList_VarList(t  Declaration )  BQTerm  { return (t).(*MakeAddListDeclaration).VarList;  }; func tom_generate_get_slot_MakeAddList_Instr(t  Declaration )  Instruction  { return (t).(*MakeAddListDeclaration).Instr;  }; func tom_generate_get_slot_MakeAddList_OrgTrack(t  Declaration )  Option  { return (t).(*MakeAddListDeclaration).OrgTrack;  }; func tom_generate_is_fun_sym_GetElementDecl(t  Declaration ) bool { return func() bool { _, ok := (t).(*GetElementDeclDeclaration); return ok }();  }; func tom_generate_get_slot_GetElementDecl_Opname(t  Declaration )  TomName  { return (t).(*GetElementDeclDeclaration).Opname;  }; func tom_generate_get_slot_GetElementDecl_Variable(t  Declaration )  BQTerm  { return (t).(*GetElementDeclDeclaration).Variable;  }; func tom_generate_get_slot_GetElementDecl_Index(t  Declaration )  BQTerm  { return (t).(*GetElementDeclDeclaration).Index;  }; func tom_generate_get_slot_GetElementDecl_Expr(t  Declaration )  Expression  { return (t).(*GetElementDeclDeclaration).Expr;  }; func tom_generate_get_slot_GetElementDecl_OrgTrack(t  Declaration )  Option  { return (t).(*GetElementDeclDeclaration).OrgTrack;  }; func tom_generate_is_fun_sym_GetSizeDecl(t  Declaration ) bool { return func() bool { _, ok := (t).(*GetSizeDeclDeclaration); return ok }();  }; func tom_generate_get_slot_GetSizeDecl_Opname(t  Declaration )  TomName  { return (t).(*GetSizeDeclDeclaration).Opname;  }; func tom_generate_get_slot_GetSizeDecl_Variable(t  Declaration )  BQTerm  { return (t).(*GetSizeDeclDeclaration).Variable;  }; func tom_generate_get_slot_GetSizeDecl_Expr(t  Declaration )  Expression  { return (t).(*GetSizeDeclDeclaration).Expr;  }; func tom_generate_get_slot_GetSizeDecl_OrgTrack(t  Declaration )  Option  { return (t).(*GetSizeDeclDeclaration).OrgTrack;  }; func tom_generate_is_fun_sym_MakeEmptyArray(t  Declaration ) bool { return func() bool { _, ok := (t).(*MakeEmptyArrayDeclaration); return ok }();  }; func tom_generate_get_slot_MakeEmptyArray_AstName(t  Declaration )  TomName  { return (t).(*MakeEmptyArrayDeclaration).AstName;  }; func tom_generate_get_slot_MakeEmptyArray_VarSize(t  Declaration )  BQTerm  { return (t).(*MakeEmptyArrayDeclaration).VarSize;  }; func tom_generate_get_slot_MakeEmptyArray_Instr(t  Declaration )  Instruction  { return (t).(*MakeEmptyArrayDeclaration).Instr;  }; func tom_generate_get_slot_MakeEmptyArray_OrgTrack(t  Declaration )  Option  { return (t).(*MakeEmptyArrayDeclaration).OrgTrack;  }; func tom_generate_is_fun_sym_MakeAddArray(t  Declaration ) bool { return func() bool { _, ok := (t).(*MakeAddArrayDeclaration); return ok }();  }; func tom_generate_get_slot_MakeAddArray_AstName(t  Declaration )  TomName  { return (t).(*MakeAddArrayDeclaration).AstName;  }; func tom_generate_get_slot_MakeAddArray_VarElt(t  Declaration )  BQTerm  { return (t).(*MakeAddArrayDeclaration).VarElt;  }; func tom_generate_get_slot_MakeAddArray_VarList(t  Declaration )  BQTerm  { return (t).(*MakeAddArrayDeclaration).VarList;  }; func tom_generate_get_slot_MakeAddArray_Instr(t  Declaration )  Instruction  { return (t).(*MakeAddArrayDeclaration).Instr;  }; func tom_generate_get_slot_MakeAddArray_OrgTrack(t  Declaration )  Option  { return (t).(*MakeAddArrayDeclaration).OrgTrack;  }; func tom_generate_is_fun_sym_MakeDecl(t  Declaration ) bool { return func() bool { _, ok := (t).(*MakeDeclDeclaration); return ok }();  }; func tom_generate_get_slot_MakeDecl_AstName(t  Declaration )  TomName  { return (t).(*MakeDeclDeclaration).AstName;  }; func tom_generate_get_slot_MakeDecl_AstType(t  Declaration )  TomType  { return (t).(*MakeDeclDeclaration).AstType;  }; func tom_generate_get_slot_MakeDecl_Args(t  Declaration )  BQTermList  { return (t).(*MakeDeclDeclaration).Args;  }; func tom_generate_get_slot_MakeDecl_Instr(t  Declaration )  Instruction  { return (t).(*MakeDeclDeclaration).Instr;  }; func tom_generate_get_slot_MakeDecl_OrgTrack(t  Declaration )  Option  { return (t).(*MakeDeclDeclaration).OrgTrack;  }; func tom_generate_is_fun_sym_ResolveInverseLinksDecl(t  Declaration ) bool { return func() bool { _, ok := (t).(*ResolveInverseLinksDeclDeclaration); return ok }();  }; func tom_generate_get_slot_ResolveInverseLinksDecl_ResolveNameList(t  Declaration )  TomNameList  { return (t).(*ResolveInverseLinksDeclDeclaration).ResolveNameList;  }; func tom_generate_get_slot_ResolveInverseLinksDecl_FileFrom(t  Declaration )  string  { return (t).(*ResolveInverseLinksDeclDeclaration).FileFrom;  }; func tom_generate_get_slot_ResolveInverseLinksDecl_FileTo(t  Declaration )  string  { return (t).(*ResolveInverseLinksDeclDeclaration).FileTo;  }; func tom_generate_is_fun_sym_ResolveClassDecl(t  Declaration ) bool { return func() bool { _, ok := (t).(*ResolveClassDeclDeclaration); return ok }();  }; func tom_generate_get_slot_ResolveClassDecl_AstName(t  Declaration )  TomName  { return (t).(*ResolveClassDeclDeclaration).AstName;  }; func tom_generate_get_slot_ResolveClassDecl_WithName(t  Declaration )  string  { return (t).(*ResolveClassDeclDeclaration).WithName;  }; func tom_generate_get_slot_ResolveClassDecl_ToName(t  Declaration )  string  { return (t).(*ResolveClassDeclDeclaration).ToName;  }; func tom_generate_get_slot_ResolveClassDecl_Extends(t  Declaration )  string  { return (t).(*ResolveClassDeclDeclaration).Extends;  }; func tom_generate_is_fun_sym_ResolveIsFsymDecl(t  Declaration ) bool { return func() bool { _, ok := (t).(*ResolveIsFsymDeclDeclaration); return ok }();  }; func tom_generate_get_slot_ResolveIsFsymDecl_AstName(t  Declaration )  TomName  { return (t).(*ResolveIsFsymDeclDeclaration).AstName;  }; func tom_generate_get_slot_ResolveIsFsymDecl_Variable(t  Declaration )  BQTerm  { return (t).(*ResolveIsFsymDeclDeclaration).Variable;  }; func tom_generate_get_slot_ResolveIsFsymDecl_OrgTrack(t  Declaration )  Option  { return (t).(*ResolveIsFsymDeclDeclaration).OrgTrack;  }; func tom_generate_is_fun_sym_ResolveGetSlotDecl(t  Declaration ) bool { return func() bool { _, ok := (t).(*ResolveGetSlotDeclDeclaration); return ok }();  }; func tom_generate_get_slot_ResolveGetSlotDecl_AstName(t  Declaration )  TomName  { return (t).(*ResolveGetSlotDeclDeclaration).AstName;  }; func tom_generate_get_slot_ResolveGetSlotDecl_SlotName(t  Declaration )  TomName  { return (t).(*ResolveGetSlotDeclDeclaration).SlotName;  }; func tom_generate_get_slot_ResolveGetSlotDecl_Variable(t  Declaration )  BQTerm  { return (t).(*ResolveGetSlotDeclDeclaration).Variable;  }; func tom_generate_get_slot_ResolveGetSlotDecl_OrgTrack(t  Declaration )  Option  { return (t).(*ResolveGetSlotDeclDeclaration).OrgTrack;  }; func tom_generate_is_fun_sym_ResolveMakeDecl(t  Declaration ) bool { return func() bool { _, ok := (t).(*ResolveMakeDeclDeclaration); return ok }();  }; func tom_generate_get_slot_ResolveMakeDecl_AstName(t  Declaration )  TomName  { return (t).(*ResolveMakeDeclDeclaration).AstName;  }; func tom_generate_get_slot_ResolveMakeDecl_AstType(t  Declaration )  TomType  { return (t).(*ResolveMakeDeclDeclaration).AstType;  }; func tom_generate_get_slot_ResolveMakeDecl_Args(t  Declaration )  BQTermList  { return (t).(*ResolveMakeDeclDeclaration).Args;  }; func tom_generate_get_slot_ResolveMakeDecl_OrgTrack(t  Declaration )  Option  { return (t).(*ResolveMakeDeclDeclaration).OrgTrack;  }; func tom_generate_is_fun_sym_ReferenceClass(t  Declaration ) bool { return func() bool { _, ok := (t).(*ReferenceClassDeclaration); return ok }();  }; func tom_generate_get_slot_ReferenceClass_RefName(t  Declaration )  TomName  { return (t).(*ReferenceClassDeclaration).RefName;  }; func tom_generate_get_slot_ReferenceClass_Fields(t  Declaration )  RefClassTracelinkInstructionList  { return (t).(*ReferenceClassDeclaration).Fields;  }; func tom_generate_is_fun_sym_FunctionDef(t  Declaration ) bool { return func() bool { _, ok := (t).(*FunctionDefDeclaration); return ok }();  }; func tom_generate_get_slot_FunctionDef_AstName(t  Declaration )  TomName  { return (t).(*FunctionDefDeclaration).AstName;  }; func tom_generate_get_slot_FunctionDef_ArgumentList(t  Declaration )  BQTermList  { return (t).(*FunctionDefDeclaration).ArgumentList;  }; func tom_generate_get_slot_FunctionDef_Codomain(t  Declaration )  TomType  { return (t).(*FunctionDefDeclaration).Codomain;  }; func tom_generate_get_slot_FunctionDef_ThrowsType(t  Declaration )  TomType  { return (t).(*FunctionDefDeclaration).ThrowsType;  }; func tom_generate_get_slot_FunctionDef_Instruction(t  Declaration )  Instruction  { return (t).(*FunctionDefDeclaration).Instruction;  }; func tom_generate_is_fun_sym_MethodDef(t  Declaration ) bool { return func() bool { _, ok := (t).(*MethodDefDeclaration); return ok }();  }; func tom_generate_get_slot_MethodDef_AstName(t  Declaration )  TomName  { return (t).(*MethodDefDeclaration).AstName;  }; func tom_generate_get_slot_MethodDef_ArgumentList(t  Declaration )  BQTermList  { return (t).(*MethodDefDeclaration).ArgumentList;  }; func tom_generate_get_slot_MethodDef_Codomain(t  Declaration )  TomType  { return (t).(*MethodDefDeclaration).Codomain;  }; func tom_generate_get_slot_MethodDef_ThrowsType(t  Declaration )  TomType  { return (t).(*MethodDefDeclaration).ThrowsType;  }; func tom_generate_get_slot_MethodDef_Instruction(t  Declaration )  Instruction  { return (t).(*MethodDefDeclaration).Instruction;  }; func tom_generate_is_fun_sym_Class(t  Declaration ) bool { return func() bool { _, ok := (t).(*ClassDeclaration); return ok }();  }; func tom_generate_get_slot_Class_AstName(t  Declaration )  TomName  { return (t).(*ClassDeclaration).AstName;  }; func tom_generate_get_slot_Class_ExtendsType(t  Declaration )  TomType  { return (t).(*ClassDeclaration).ExtendsType;  }; func tom_generate_get_slot_Class_SuperTerm(t  Declaration )  BQTerm  { return (t).(*ClassDeclaration).SuperTerm;  }; func tom_generate_get_slot_Class_Declaration(t  Declaration )  Declaration  { return (t).(*ClassDeclaration).Declaration;  }; func tom_generate_is_fun_sym_IntrospectorClass(t  Declaration ) bool { return func() bool { _, ok := (t).(*IntrospectorClassDeclaration); return ok }();  }; func tom_generate_get_slot_IntrospectorClass_AstName(t  Declaration )  TomName  { return (t).(*IntrospectorClassDeclaration).AstName;  }; func tom_generate_get_slot_IntrospectorClass_Declaration(t  Declaration )  Declaration  { return (t).(*IntrospectorClassDeclaration).Declaration;  }; func tom_generate_is_fun_sym_SymbolDecl(t  Declaration ) bool { return func() bool { _, ok := (t).(*SymbolDeclDeclaration); return ok }();  }; func tom_generate_get_slot_SymbolDecl_AstName(t  Declaration )  TomName  { return (t).(*SymbolDeclDeclaration).AstName;  }; func tom_generate_is_fun_sym_ListSymbolDecl(t  Declaration ) bool { return func() bool { _, ok := (t).(*ListSymbolDeclDeclaration); return ok }();  }; func tom_generate_get_slot_ListSymbolDecl_AstName(t  Declaration )  TomName  { return (t).(*ListSymbolDeclDeclaration).AstName;  }; func tom_generate_is_fun_sym_ArraySymbolDecl(t  Declaration ) bool { return func() bool { _, ok := (t).(*ArraySymbolDeclDeclaration); return ok }();  }; func tom_generate_get_slot_ArraySymbolDecl_AstName(t  Declaration )  TomName  { return (t).(*ArraySymbolDeclDeclaration).AstName;  }; func tom_generate_is_fun_sym_EmptyDeclaration(t  Declaration ) bool { return func() bool { _, ok := (t).(*EmptyDeclarationDeclaration); return ok }();  }; func tom_generate_is_fun_sym_AbstractDecl(t  Declaration ) bool { return func() bool { _, ok := (t).(*AbstractDeclDeclaration); return ok }();  }; func tom_generate_get_slot_AbstractDecl_DeclList(t  Declaration )  DeclarationList  { return (t).(*AbstractDeclDeclaration).DeclList;  }; func tom_generate_is_fun_sym_ACSymbolDecl(t  Declaration ) bool { return func() bool { _, ok := (t).(*ACSymbolDeclDeclaration); return ok }();  }; func tom_generate_get_slot_ACSymbolDecl_AstName(t  Declaration )  TomName  { return (t).(*ACSymbolDeclDeclaration).AstName;  }; func tom_generate_is_fun_sym_BQTermToDeclaration(t  Declaration ) bool { return func() bool { _, ok := (t).(*BQTermToDeclarationDeclaration); return ok }();  }; func tom_generate_get_slot_BQTermToDeclaration_BqTerm(t  Declaration )  BQTerm  { return (t).(*BQTermToDeclarationDeclaration).BqTerm;  }; func tom_generate_equal_term_Expression(t1  Expression , t2  Expression ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_Expression(t any) bool { return func() bool { _, ok := (t).(Expression); return ok }();  }; func tom_generate_is_fun_sym_BQTermToExpression(t  Expression ) bool { return func() bool { _, ok := (t).(*BQTermToExpressionExpression); return ok }();  }; func tom_generate_get_slot_BQTermToExpression_AstTerm(t  Expression )  BQTerm  { return (t).(*BQTermToExpressionExpression).AstTerm;  }; func tom_generate_is_fun_sym_TomInstructionToExpression(t  Expression ) bool { return func() bool { _, ok := (t).(*TomInstructionToExpressionExpression); return ok }();  }; func tom_generate_get_slot_TomInstructionToExpression_Instruction(t  Expression )  Instruction  { return (t).(*TomInstructionToExpressionExpression).Instruction;  }; func tom_generate_is_fun_sym_Bottom(t  Expression ) bool { return func() bool { _, ok := (t).(*BottomExpression); return ok }();  }; func tom_generate_get_slot_Bottom_TomType(t  Expression )  TomType  { return (t).(*BottomExpression).TomType;  }; func tom_generate_is_fun_sym_Negation(t  Expression ) bool { return func() bool { _, ok := (t).(*NegationExpression); return ok }();  }; func tom_generate_make_Negation(Arg  Expression )  Expression  { return (MakeNegation(Arg)) }; func tom_generate_get_slot_Negation_Arg(t  Expression )  Expression  { return (t).(*NegationExpression).Arg;  }; func tom_generate_is_fun_sym_And(t  Expression ) bool { return func() bool { _, ok := (t).(*AndExpression); return ok }();  }; func tom_generate_get_slot_And_Arg1(t  Expression )  Expression  { return (t).(*AndExpression).Arg1;  }; func tom_generate_get_slot_And_Arg2(t  Expression )  Expression  { return (t).(*AndExpression).Arg2;  }; func tom_generate_is_fun_sym_Or(t  Expression ) bool { return func() bool { _, ok := (t).(*OrExpression); return ok }();  }; func tom_generate_get_slot_Or_Arg1(t  Expression )  Expression  { return (t).(*OrExpression).Arg1;  }; func tom_generate_get_slot_Or_Arg2(t  Expression )  Expression  { return (t).(*OrExpression).Arg2;  }; func tom_generate_is_fun_sym_GreaterThan(t  Expression ) bool { return func() bool { _, ok := (t).(*GreaterThanExpression); return ok }();  }; func tom_generate_get_slot_GreaterThan_Arg1(t  Expression )  Expression  { return (t).(*GreaterThanExpression).Arg1;  }; func tom_generate_get_slot_GreaterThan_Arg2(t  Expression )  Expression  { return (t).(*GreaterThanExpression).Arg2;  }; func tom_generate_is_fun_sym_GreaterOrEqualThan(t  Expression ) bool { return func() bool { _, ok := (t).(*GreaterOrEqualThanExpression); return ok }();  }; func tom_generate_get_slot_GreaterOrEqualThan_Arg1(t  Expression )  Expression  { return (t).(*GreaterOrEqualThanExpression).Arg1;  }; func tom_generate_get_slot_GreaterOrEqualThan_Arg2(t  Expression )  Expression  { return (t).(*GreaterOrEqualThanExpression).Arg2;  }; func tom_generate_is_fun_sym_LessThan(t  Expression ) bool { return func() bool { _, ok := (t).(*LessThanExpression); return ok }();  }; func tom_generate_get_slot_LessThan_Arg1(t  Expression )  Expression  { return (t).(*LessThanExpression).Arg1;  }; func tom_generate_get_slot_LessThan_Arg2(t  Expression )  Expression  { return (t).(*LessThanExpression).Arg2;  }; func tom_generate_is_fun_sym_LessOrEqualThan(t  Expression ) bool { return func() bool { _, ok := (t).(*LessOrEqualThanExpression); return ok }();  }; func tom_generate_get_slot_LessOrEqualThan_Arg1(t  Expression )  Expression  { return (t).(*LessOrEqualThanExpression).Arg1;  }; func tom_generate_get_slot_LessOrEqualThan_Arg2(t  Expression )  Expression  { return (t).(*LessOrEqualThanExpression).Arg2;  }; func tom_generate_is_fun_sym_Conditional(t  Expression ) bool { return func() bool { _, ok := (t).(*ConditionalExpression); return ok }();  }; func tom_generate_get_slot_Conditional_Cond(t  Expression )  Expression  { return (t).(*ConditionalExpression).Cond;  }; func tom_generate_get_slot_Conditional_Then(t  Expression )  Expression  { return (t).(*ConditionalExpression).Then;  }; func tom_generate_get_slot_Conditional_Else(t  Expression )  Expression  { return (t).(*ConditionalExpression).Else;  }; func tom_generate_is_fun_sym_TrueTL(t  Expression ) bool { return func() bool { _, ok := (t).(*TrueTLExpression); return ok }();  }; func tom_generate_is_fun_sym_FalseTL(t  Expression ) bool { return func() bool { _, ok := (t).(*FalseTLExpression); return ok }();  }; func tom_generate_is_fun_sym_Integer(t  Expression ) bool { return func() bool { _, ok := (t).(*IntegerExpression); return ok }();  }; func tom_generate_get_slot_Integer_value(t  Expression )  int64  { return (t).(*IntegerExpression).Value;  }; func tom_generate_is_fun_sym_EqualTerm(t  Expression ) bool { return func() bool { _, ok := (t).(*EqualTermExpression); return ok }();  }; func tom_generate_get_slot_EqualTerm_TomType(t  Expression )  TomType  { return (t).(*EqualTermExpression).TomType;  }; func tom_generate_get_slot_EqualTerm_Kid1(t  Expression )  BQTerm  { return (t).(*EqualTermExpression).Kid1;  }; func tom_generate_get_slot_EqualTerm_Kid2(t  Expression )  TomTerm  { return (t).(*EqualTermExpression).Kid2;  }; func tom_generate_is_fun_sym_EqualBQTerm(t  Expression ) bool { return func() bool { _, ok := (t).(*EqualBQTermExpression); return ok }();  }; func tom_generate_get_slot_EqualBQTerm_TomType(t  Expression )  TomType  { return (t).(*EqualBQTermExpression).TomType;  }; func tom_generate_get_slot_EqualBQTerm_t1(t  Expression )  BQTerm  { return (t).(*EqualBQTermExpression).T1;  }; func tom_generate_get_slot_EqualBQTerm_t2(t  Expression )  BQTerm  { return (t).(*EqualBQTermExpression).T2;  }; func tom_generate_is_fun_sym_Cast(t  Expression ) bool { return func() bool { _, ok := (t).(*CastExpression); return ok }();  }; func tom_generate_get_slot_Cast_AstType(t  Expression )  TomType  { return (t).(*CastExpression).AstType;  }; func tom_generate_get_slot_Cast_Source(t  Expression )  Expression  { return (t).(*CastExpression).Source;  }; func tom_generate_is_fun_sym_GetSlot(t  Expression ) bool { return func() bool { _, ok := (t).(*GetSlotExpression); return ok }();  }; func tom_generate_get_slot_GetSlot_Codomain(t  Expression )  TomType  { return (t).(*GetSlotExpression).Codomain;  }; func tom_generate_get_slot_GetSlot_AstName(t  Expression )  TomName  { return (t).(*GetSlotExpression).AstName;  }; func tom_generate_get_slot_GetSlot_SlotNameString(t  Expression )  string  { return (t).(*GetSlotExpression).SlotNameString;  }; func tom_generate_get_slot_GetSlot_Variable(t  Expression )  BQTerm  { return (t).(*GetSlotExpression).Variable;  }; func tom_generate_is_fun_sym_IsFsym(t  Expression ) bool { return func() bool { _, ok := (t).(*IsFsymExpression); return ok }();  }; func tom_generate_get_slot_IsFsym_AstName(t  Expression )  TomName  { return (t).(*IsFsymExpression).AstName;  }; func tom_generate_get_slot_IsFsym_Variable(t  Expression )  BQTerm  { return (t).(*IsFsymExpression).Variable;  }; func tom_generate_is_fun_sym_GetHead(t  Expression ) bool { return func() bool { _, ok := (t).(*GetHeadExpression); return ok }();  }; func tom_generate_get_slot_GetHead_Opname(t  Expression )  TomName  { return (t).(*GetHeadExpression).Opname;  }; func tom_generate_get_slot_GetHead_Codomain(t  Expression )  TomType  { return (t).(*GetHeadExpression).Codomain;  }; func tom_generate_get_slot_GetHead_Variable(t  Expression )  BQTerm  { return (t).(*GetHeadExpression).Variable;  }; func tom_generate_is_fun_sym_GetTail(t  Expression ) bool { return func() bool { _, ok := (t).(*GetTailExpression); return ok }();  }; func tom_generate_get_slot_GetTail_Opname(t  Expression )  TomName  { return (t).(*GetTailExpression).Opname;  }; func tom_generate_get_slot_GetTail_Variable(t  Expression )  BQTerm  { return (t).(*GetTailExpression).Variable;  }; func tom_generate_is_fun_sym_IsEmptyList(t  Expression ) bool { return func() bool { _, ok := (t).(*IsEmptyListExpression); return ok }();  }; func tom_generate_get_slot_IsEmptyList_Opname(t  Expression )  TomName  { return (t).(*IsEmptyListExpression).Opname;  }; func tom_generate_get_slot_IsEmptyList_Variable(t  Expression )  BQTerm  { return (t).(*IsEmptyListExpression).Variable;  }; func tom_generate_is_fun_sym_IsEmptyArray(t  Expression ) bool { return func() bool { _, ok := (t).(*IsEmptyArrayExpression); return ok }();  }; func tom_generate_get_slot_IsEmptyArray_Opname(t  Expression )  TomName  { return (t).(*IsEmptyArrayExpression).Opname;  }; func tom_generate_get_slot_IsEmptyArray_Variable(t  Expression )  BQTerm  { return (t).(*IsEmptyArrayExpression).Variable;  }; func tom_generate_get_slot_IsEmptyArray_Index(t  Expression )  BQTerm  { return (t).(*IsEmptyArrayExpression).Index;  }; func tom_generate_is_fun_sym_AddOne(t  Expression ) bool { return func() bool { _, ok := (t).(*AddOneExpression); return ok }();  }; func tom_generate_get_slot_AddOne_Variable(t  Expression )  BQTerm  { return (t).(*AddOneExpression).Variable;  }; func tom_generate_is_fun_sym_SubstractOne(t  Expression ) bool { return func() bool { _, ok := (t).(*SubstractOneExpression); return ok }();  }; func tom_generate_get_slot_SubstractOne_Variable(t  Expression )  BQTerm  { return (t).(*SubstractOneExpression).Variable;  }; func tom_generate_is_fun_sym_Substract(t  Expression ) bool { return func() bool { _, ok := (t).(*SubstractExpression); return ok }();  }; func tom_generate_get_slot_Substract_Term1(t  Expression )  BQTerm  { return (t).(*SubstractExpression).Term1;  }; func tom_generate_get_slot_Substract_Term2(t  Expression )  BQTerm  { return (t).(*SubstractExpression).Term2;  }; func tom_generate_is_fun_sym_GetSize(t  Expression ) bool { return func() bool { _, ok := (t).(*GetSizeExpression); return ok }();  }; func tom_generate_get_slot_GetSize_Opname(t  Expression )  TomName  { return (t).(*GetSizeExpression).Opname;  }; func tom_generate_get_slot_GetSize_Variable(t  Expression )  BQTerm  { return (t).(*GetSizeExpression).Variable;  }; func tom_generate_is_fun_sym_GetElement(t  Expression ) bool { return func() bool { _, ok := (t).(*GetElementExpression); return ok }();  }; func tom_generate_get_slot_GetElement_Opname(t  Expression )  TomName  { return (t).(*GetElementExpression).Opname;  }; func tom_generate_get_slot_GetElement_Codomain(t  Expression )  TomType  { return (t).(*GetElementExpression).Codomain;  }; func tom_generate_get_slot_GetElement_Variable(t  Expression )  BQTerm  { return (t).(*GetElementExpression).Variable;  }; func tom_generate_get_slot_GetElement_Index(t  Expression )  BQTerm  { return (t).(*GetElementExpression).Index;  }; func tom_generate_is_fun_sym_GetSliceList(t  Expression ) bool { return func() bool { _, ok := (t).(*GetSliceListExpression); return ok }();  }; func tom_generate_get_slot_GetSliceList_AstName(t  Expression )  TomName  { return (t).(*GetSliceListExpression).AstName;  }; func tom_generate_get_slot_GetSliceList_VariableBeginAST(t  Expression )  BQTerm  { return (t).(*GetSliceListExpression).VariableBeginAST;  }; func tom_generate_get_slot_GetSliceList_VariableEndAST(t  Expression )  BQTerm  { return (t).(*GetSliceListExpression).VariableEndAST;  }; func tom_generate_get_slot_GetSliceList_Tail(t  Expression )  BQTerm  { return (t).(*GetSliceListExpression).Tail;  }; func tom_generate_is_fun_sym_GetSliceArray(t  Expression ) bool { return func() bool { _, ok := (t).(*GetSliceArrayExpression); return ok }();  }; func tom_generate_get_slot_GetSliceArray_AstName(t  Expression )  TomName  { return (t).(*GetSliceArrayExpression).AstName;  }; func tom_generate_get_slot_GetSliceArray_SubjectListName(t  Expression )  BQTerm  { return (t).(*GetSliceArrayExpression).SubjectListName;  }; func tom_generate_get_slot_GetSliceArray_VariableBeginAST(t  Expression )  BQTerm  { return (t).(*GetSliceArrayExpression).VariableBeginAST;  }; func tom_generate_get_slot_GetSliceArray_VariableEndAST(t  Expression )  BQTerm  { return (t).(*GetSliceArrayExpression).VariableEndAST;  }; func tom_generate_is_fun_sym_IsSort(t  Expression ) bool { return func() bool { _, ok := (t).(*IsSortExpression); return ok }();  }; func tom_generate_get_slot_IsSort_AstType(t  Expression )  TomType  { return (t).(*IsSortExpression).AstType;  }; func tom_generate_get_slot_IsSort_Variable(t  Expression )  BQTerm  { return (t).(*IsSortExpression).Variable;  }; func tom_generate_is_fun_sym_Code(t  Expression ) bool { return func() bool { _, ok := (t).(*CodeExpression); return ok }();  }; func tom_generate_make_Code(Code  string )  Expression  { return (MakeCode(Code)) }; func tom_generate_get_slot_Code_Code(t  Expression )  string  { return (t).(*CodeExpression).Code;  }; func tom_generate_equal_term_Instruction(t1  Instruction , t2  Instruction ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_Instruction(t any) bool { return func() bool { _, ok := (t).(Instruction); return ok }();  }; func tom_generate_equal_term_RefClassTracelinkInstruction(t1  RefClassTracelinkInstruction , t2  RefClassTracelinkInstruction ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_RefClassTracelinkInstruction(t any) bool { return func() bool { _, ok := (t).(RefClassTracelinkInstruction); return ok }();  }; func tom_generate_equal_term_RefClassTracelinkInstructionList(t1  RefClassTracelinkInstructionList , t2  RefClassTracelinkInstructionList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_RefClassTracelinkInstructionList(t any) bool { return func() bool { _, ok := (t).(RefClassTracelinkInstructionList); return ok }();  }; func tom_generate_equal_term_ConstraintInstruction(t1  ConstraintInstruction , t2  ConstraintInstruction ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_ConstraintInstruction(t any) bool { return func() bool { _, ok := (t).(ConstraintInstruction); return ok }();  }; func tom_generate_equal_term_RuleInstruction(t1  RuleInstruction , t2  RuleInstruction ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_RuleInstruction(t any) bool { return func() bool { _, ok := (t).(RuleInstruction); return ok }();  }; func tom_generate_equal_term_RuleInstructionList(t1  RuleInstructionList , t2  RuleInstructionList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_RuleInstructionList(t any) bool { return func() bool { _, ok := (t).(RuleInstructionList); return ok }();  }; func tom_generate_equal_term_ConstraintInstructionList(t1  ConstraintInstructionList , t2  ConstraintInstructionList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_ConstraintInstructionList(t any) bool { return func() bool { _, ok := (t).(ConstraintInstructionList); return ok }();  }; func tom_generate_equal_term_InstructionList(t1  InstructionList , t2  InstructionList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_InstructionList(t any) bool { return func() bool { _, ok := (t).(InstructionList); return ok }();  }; func tom_generate_is_fun_sym_BQTermToInstruction(t  Instruction ) bool { return func() bool { _, ok := (t).(*BQTermToInstructionInstruction); return ok }();  }; func tom_generate_get_slot_BQTermToInstruction_Tom(t  Instruction )  BQTerm  { return (t).(*BQTermToInstructionInstruction).Tom;  }; func tom_generate_is_fun_sym_ExpressionToInstruction(t  Instruction ) bool { return func() bool { _, ok := (t).(*ExpressionToInstructionInstruction); return ok }();  }; func tom_generate_get_slot_ExpressionToInstruction_Expr(t  Instruction )  Expression  { return (t).(*ExpressionToInstructionInstruction).Expr;  }; func tom_generate_is_fun_sym_CodeToInstruction(t  Instruction ) bool { return func() bool { _, ok := (t).(*CodeToInstructionInstruction); return ok }();  }; func tom_generate_get_slot_CodeToInstruction_Code(t  Instruction )  Code  { return (t).(*CodeToInstructionInstruction).Code;  }; func tom_generate_is_fun_sym_If(t  Instruction ) bool { return func() bool { _, ok := (t).(*IfInstruction); return ok }();  }; func tom_generate_get_slot_If_Condition(t  Instruction )  Expression  { return (t).(*IfInstruction).Condition;  }; func tom_generate_get_slot_If_SuccesInst(t  Instruction )  Instruction  { return (t).(*IfInstruction).SuccesInst;  }; func tom_generate_get_slot_If_FailureInst(t  Instruction )  Instruction  { return (t).(*IfInstruction).FailureInst;  }; func tom_generate_is_fun_sym_DoWhile(t  Instruction ) bool { return func() bool { _, ok := (t).(*DoWhileInstruction); return ok }();  }; func tom_generate_get_slot_DoWhile_DoInst(t  Instruction )  Instruction  { return (t).(*DoWhileInstruction).DoInst;  }; func tom_generate_get_slot_DoWhile_Condition(t  Instruction )  Expression  { return (t).(*DoWhileInstruction).Condition;  }; func tom_generate_is_fun_sym_WhileDo(t  Instruction ) bool { return func() bool { _, ok := (t).(*WhileDoInstruction); return ok }();  }; func tom_generate_get_slot_WhileDo_Condition(t  Instruction )  Expression  { return (t).(*WhileDoInstruction).Condition;  }; func tom_generate_get_slot_WhileDo_DoInst(t  Instruction )  Instruction  { return (t).(*WhileDoInstruction).DoInst;  }; func tom_generate_is_fun_sym_Let(t  Instruction ) bool { return func() bool { _, ok := (t).(*LetInstruction); return ok }();  }; func tom_generate_get_slot_Let_Variable(t  Instruction )  BQTerm  { return (t).(*LetInstruction).Variable;  }; func tom_generate_get_slot_Let_Source(t  Instruction )  Expression  { return (t).(*LetInstruction).Source;  }; func tom_generate_get_slot_Let_AstInstruction(t  Instruction )  Instruction  { return (t).(*LetInstruction).AstInstruction;  }; func tom_generate_is_fun_sym_LetRef(t  Instruction ) bool { return func() bool { _, ok := (t).(*LetRefInstruction); return ok }();  }; func tom_generate_get_slot_LetRef_Variable(t  Instruction )  BQTerm  { return (t).(*LetRefInstruction).Variable;  }; func tom_generate_get_slot_LetRef_Source(t  Instruction )  Expression  { return (t).(*LetRefInstruction).Source;  }; func tom_generate_get_slot_LetRef_AstInstruction(t  Instruction )  Instruction  { return (t).(*LetRefInstruction).AstInstruction;  }; func tom_generate_is_fun_sym_Assign(t  Instruction ) bool { return func() bool { _, ok := (t).(*AssignInstruction); return ok }();  }; func tom_generate_get_slot_Assign_Variable(t  Instruction )  BQTerm  { return (t).(*AssignInstruction).Variable;  }; func tom_generate_get_slot_Assign_Source(t  Instruction )  Expression  { return (t).(*AssignInstruction).Source;  }; func tom_generate_is_fun_sym_AssignArray(t  Instruction ) bool { return func() bool { _, ok := (t).(*AssignArrayInstruction); return ok }();  }; func tom_generate_get_slot_AssignArray_Variable(t  Instruction )  BQTerm  { return (t).(*AssignArrayInstruction).Variable;  }; func tom_generate_get_slot_AssignArray_Index(t  Instruction )  BQTerm  { return (t).(*AssignArrayInstruction).Index;  }; func tom_generate_get_slot_AssignArray_Source(t  Instruction )  Expression  { return (t).(*AssignArrayInstruction).Source;  }; func tom_generate_is_fun_sym_Return(t  Instruction ) bool { return func() bool { _, ok := (t).(*ReturnInstruction); return ok }();  }; func tom_generate_make_Return(Kid1  BQTerm )  Instruction  { return (MakeReturn(Kid1)) }; func tom_generate_get_slot_Return_Kid1(t  Instruction )  BQTerm  { return (t).(*ReturnInstruction).Kid1;  }; func tom_generate_is_fun_sym_Nop(t  Instruction ) bool { return func() bool { _, ok := (t).(*NopInstruction); return ok }();  }; func tom_generate_is_fun_sym_AbstractBlock(t  Instruction ) bool { return func() bool { _, ok := (t).(*AbstractBlockInstruction); return ok }();  }; func tom_generate_get_slot_AbstractBlock_InstList(t  Instruction )  InstructionList  { return (t).(*AbstractBlockInstruction).InstList;  }; func tom_generate_is_fun_sym_UnamedBlock(t  Instruction ) bool { return func() bool { _, ok := (t).(*UnamedBlockInstruction); return ok }();  }; func tom_generate_get_slot_UnamedBlock_InstList(t  Instruction )  InstructionList  { return (t).(*UnamedBlockInstruction).InstList;  }; func tom_generate_is_fun_sym_NamedBlock(t  Instruction ) bool { return func() bool { _, ok := (t).(*NamedBlockInstruction); return ok }();  }; func tom_generate_get_slot_NamedBlock_BlockName(t  Instruction )  string  { return (t).(*NamedBlockInstruction).BlockName;  }; func tom_generate_get_slot_NamedBlock_InstList(t  Instruction )  InstructionList  { return (t).(*NamedBlockInstruction).InstList;  }; func tom_generate_is_fun_sym_CompiledMatch(t  Instruction ) bool { return func() bool { _, ok := (t).(*CompiledMatchInstruction); return ok }();  }; func tom_generate_get_slot_CompiledMatch_AutomataInst(t  Instruction )  Instruction  { return (t).(*CompiledMatchInstruction).AutomataInst;  }; func tom_generate_get_slot_CompiledMatch_Options(t  Instruction )  OptionList  { return (t).(*CompiledMatchInstruction).Options;  }; func tom_generate_is_fun_sym_CompiledPattern(t  Instruction ) bool { return func() bool { _, ok := (t).(*CompiledPatternInstruction); return ok }();  }; func tom_generate_get_slot_CompiledPattern_Contraint(t  Instruction )  Constraint  { return (t).(*CompiledPatternInstruction).Contraint;  }; func tom_generate_get_slot_CompiledPattern_AutomataInst(t  Instruction )  Instruction  { return (t).(*CompiledPatternInstruction).AutomataInst;  }; func tom_generate_is_fun_sym_RawAction(t  Instruction ) bool { return func() bool { _, ok := (t).(*RawActionInstruction); return ok }();  }; func tom_generate_get_slot_RawAction_AstInstruction(t  Instruction )  Instruction  { return (t).(*RawActionInstruction).AstInstruction;  }; func tom_generate_is_fun_sym_Tracelink(t  Instruction ) bool { return func() bool { _, ok := (t).(*TracelinkInstruction); return ok }();  }; func tom_generate_get_slot_Tracelink_Type(t  Instruction )  TomName  { return (t).(*TracelinkInstruction).Type;  }; func tom_generate_get_slot_Tracelink_Name(t  Instruction )  TomName  { return (t).(*TracelinkInstruction).Name;  }; func tom_generate_get_slot_Tracelink_ElementaryTransfoName(t  Instruction )  TomName  { return (t).(*TracelinkInstruction).ElementaryTransfoName;  }; func tom_generate_get_slot_Tracelink_Expr(t  Instruction )  Expression  { return (t).(*TracelinkInstruction).Expr;  }; func tom_generate_get_slot_Tracelink_OrgTrack(t  Instruction )  Option  { return (t).(*TracelinkInstruction).OrgTrack;  }; func tom_generate_is_fun_sym_TracelinkPopulateResolve(t  Instruction ) bool { return func() bool { _, ok := (t).(*TracelinkPopulateResolveInstruction); return ok }();  }; func tom_generate_get_slot_TracelinkPopulateResolve_RefClassName(t  Instruction )  TomName  { return (t).(*TracelinkPopulateResolveInstruction).RefClassName;  }; func tom_generate_get_slot_TracelinkPopulateResolve_TracedLinks(t  Instruction )  TomNameList  { return (t).(*TracelinkPopulateResolveInstruction).TracedLinks;  }; func tom_generate_get_slot_TracelinkPopulateResolve_Current(t  Instruction )  BQTerm  { return (t).(*TracelinkPopulateResolveInstruction).Current;  }; func tom_generate_get_slot_TracelinkPopulateResolve_Link(t  Instruction )  BQTerm  { return (t).(*TracelinkPopulateResolveInstruction).Link;  }; func tom_generate_is_fun_sym_Resolve(t  Instruction ) bool { return func() bool { _, ok := (t).(*ResolveInstruction); return ok }();  }; func tom_generate_get_slot_Resolve_ResolveBQTerm(t  Instruction )  BQTerm  { return (t).(*ResolveInstruction).ResolveBQTerm;  }; func tom_generate_get_slot_Resolve_Src(t  Instruction )  string  { return (t).(*ResolveInstruction).Src;  }; func tom_generate_get_slot_Resolve_SType(t  Instruction )  string  { return (t).(*ResolveInstruction).SType;  }; func tom_generate_get_slot_Resolve_Target(t  Instruction )  string  { return (t).(*ResolveInstruction).Target;  }; func tom_generate_get_slot_Resolve_TType(t  Instruction )  string  { return (t).(*ResolveInstruction).TType;  }; func tom_generate_get_slot_Resolve_OrgTrack(t  Instruction )  Option  { return (t).(*ResolveInstruction).OrgTrack;  }; func tom_generate_is_fun_sym_concInstruction(t  InstructionList ) bool { return func() bool { _, ok := (t).(*ConcInstructionInstructionList); return ok }();  }; func tom_generate_empty_list_concInstruction()  InstructionList  { return (EmptyConcInstruction()) }; func tom_generate_cons_list_concInstruction(e  Instruction , l  InstructionList )  InstructionList  { return (ConsConcInstruction(e,l)) }; func tom_generate_get_head_concInstruction_InstructionList(l  InstructionList )  Instruction  { return (l).GetHeadconcInstruction();  }; func tom_generate_get_tail_concInstruction_InstructionList(l  InstructionList )  InstructionList  { return (l).GetTailconcInstruction();  }; func tom_generate_is_empty_concInstruction_InstructionList(l  InstructionList ) bool { return (l).IsEmptyconcInstruction();  }; func tom_generate_append_list_concInstruction(l1  InstructionList , l2  InstructionList )  InstructionList  { if tom_generate_is_empty_concInstruction_InstructionList(l1) { return l2 } else if tom_generate_is_empty_concInstruction_InstructionList(l2) { return l1 } else if tom_generate_is_empty_concInstruction_InstructionList(tom_generate_get_tail_concInstruction_InstructionList(l1)) { return tom_generate_cons_list_concInstruction(tom_generate_get_head_concInstruction_InstructionList(l1), l2) } else { return tom_generate_cons_list_concInstruction(tom_generate_get_head_concInstruction_InstructionList(l1), tom_generate_append_list_concInstruction(tom_generate_get_tail_concInstruction_InstructionList(l1), l2)) }; }; func tom_generate_get_slice_concInstruction(begin  InstructionList , end  InstructionList , tail  InstructionList )  InstructionList  { if tom_generate_equal_term_InstructionList(begin, end) { return tail } else if tom_generate_equal_term_InstructionList(end, tail) && (tom_generate_is_empty_concInstruction_InstructionList(end) || tom_generate_equal_term_InstructionList(end, tom_generate_empty_list_concInstruction())) { return begin }; return tom_generate_cons_list_concInstruction(tom_generate_get_head_concInstruction_InstructionList(begin), tom_generate_get_slice_concInstruction(tom_generate_get_tail_concInstruction_InstructionList(begin), end, tail)) }; func tom_generate_equal_term_TomName(t1  TomName , t2  TomName ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_TomName(t any) bool { return func() bool { _, ok := (t).(TomName); return ok }();  }; func tom_generate_equal_term_TomNumber(t1  TomNumber , t2  TomNumber ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_TomNumber(t any) bool { return func() bool { _, ok := (t).(TomNumber); return ok }();  }; func tom_generate_equal_term_TomNumberList(t1  TomNumberList , t2  TomNumberList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_TomNumberList(t any) bool { return func() bool { _, ok := (t).(TomNumberList); return ok }();  }; func tom_generate_equal_term_TomNameList(t1  TomNameList , t2  TomNameList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_TomNameList(t any) bool { return func() bool { _, ok := (t).(TomNameList); return ok }();  }; func tom_generate_is_fun_sym_Name(t  TomName ) bool { return func() bool { _, ok := (t).(*NameTomName); return ok }();  }; func tom_generate_get_slot_Name_String(t  TomName )  string  { return (t).(*NameTomName).String_;  }; func tom_generate_is_fun_sym_PositionName(t  TomName ) bool { return func() bool { _, ok := (t).(*PositionNameTomName); return ok }();  }; func tom_generate_get_slot_PositionName_NumberList(t  TomName )  TomNumberList  { return (t).(*PositionNameTomName).NumberList;  }; func tom_generate_equal_term_Option(t1  Option , t2  Option ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_Option(t any) bool { return func() bool { _, ok := (t).(Option); return ok }();  }; func tom_generate_equal_term_OptionList(t1  OptionList , t2  OptionList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_OptionList(t any) bool { return func() bool { _, ok := (t).(OptionList); return ok }();  }; func tom_generate_is_fun_sym_DeclarationToOption(t  Option ) bool { return func() bool { _, ok := (t).(*DeclarationToOptionOption); return ok }();  }; func tom_generate_get_slot_DeclarationToOption_AstDeclaration(t  Option )  Declaration  { return (t).(*DeclarationToOptionOption).AstDeclaration;  }; func tom_generate_is_fun_sym_OriginTracking(t  Option ) bool { return func() bool { _, ok := (t).(*OriginTrackingOption); return ok }();  }; func tom_generate_get_slot_OriginTracking_AstName(t  Option )  TomName  { return (t).(*OriginTrackingOption).AstName;  }; func tom_generate_get_slot_OriginTracking_Line(t  Option )  int64  { return (t).(*OriginTrackingOption).Line;  }; func tom_generate_get_slot_OriginTracking_FileName(t  Option )  string  { return (t).(*OriginTrackingOption).FileName;  }; func tom_generate_is_fun_sym_ACSymbol(t  Option ) bool { return func() bool { _, ok := (t).(*ACSymbolOption); return ok }();  }; func tom_generate_equal_term_TextPosition(t1  TextPosition , t2  TextPosition ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_TextPosition(t any) bool { return func() bool { _, ok := (t).(TextPosition); return ok }();  }; func tom_generate_equal_term_TomVisit(t1  TomVisit , t2  TomVisit ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_TomVisit(t any) bool { return func() bool { _, ok := (t).(TomVisit); return ok }();  }; func tom_generate_equal_term_ResolveStratElement(t1  ResolveStratElement , t2  ResolveStratElement ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_ResolveStratElement(t any) bool { return func() bool { _, ok := (t).(ResolveStratElement); return ok }();  }; func tom_generate_equal_term_ResolveStratElementList(t1  ResolveStratElementList , t2  ResolveStratElementList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_ResolveStratElementList(t any) bool { return func() bool { _, ok := (t).(ResolveStratElementList); return ok }();  }; func tom_generate_equal_term_ResolveStratBlock(t1  ResolveStratBlock , t2  ResolveStratBlock ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_ResolveStratBlock(t any) bool { return func() bool { _, ok := (t).(ResolveStratBlock); return ok }();  }; func tom_generate_equal_term_ResolveStratBlockList(t1  ResolveStratBlockList , t2  ResolveStratBlockList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_ResolveStratBlockList(t any) bool { return func() bool { _, ok := (t).(ResolveStratBlockList); return ok }();  }; func tom_generate_equal_term_ElementaryTransformation(t1  ElementaryTransformation , t2  ElementaryTransformation ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_ElementaryTransformation(t any) bool { return func() bool { _, ok := (t).(ElementaryTransformation); return ok }();  }; func tom_generate_equal_term_ElementaryTransformationList(t1  ElementaryTransformationList , t2  ElementaryTransformationList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_ElementaryTransformationList(t any) bool { return func() bool { _, ok := (t).(ElementaryTransformationList); return ok }();  }; func tom_generate_equal_term_TomSymbol(t1  TomSymbol , t2  TomSymbol ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_TomSymbol(t any) bool { return func() bool { _, ok := (t).(TomSymbol); return ok }();  }; func tom_generate_equal_term_TomSymbolTable(t1  TomSymbolTable , t2  TomSymbolTable ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_TomSymbolTable(t any) bool { return func() bool { _, ok := (t).(TomSymbolTable); return ok }();  }; func tom_generate_equal_term_TomEntry(t1  TomEntry , t2  TomEntry ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_TomEntry(t any) bool { return func() bool { _, ok := (t).(TomEntry); return ok }();  }; func tom_generate_equal_term_TomStructureTable(t1  TomStructureTable , t2  TomStructureTable ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_TomStructureTable(t any) bool { return func() bool { _, ok := (t).(TomStructureTable); return ok }();  }; func tom_generate_equal_term_TomVisitList(t1  TomVisitList , t2  TomVisitList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_TomVisitList(t any) bool { return func() bool { _, ok := (t).(TomVisitList); return ok }();  }; func tom_generate_equal_term_TomEntryList(t1  TomEntryList , t2  TomEntryList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_TomEntryList(t any) bool { return func() bool { _, ok := (t).(TomEntryList); return ok }();  }; func tom_generate_equal_term_TomSymbolList(t1  TomSymbolList , t2  TomSymbolList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_TomSymbolList(t any) bool { return func() bool { _, ok := (t).(TomSymbolList); return ok }();  }; func tom_generate_equal_term_KeyEntry(t1  KeyEntry , t2  KeyEntry ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_KeyEntry(t any) bool { return func() bool { _, ok := (t).(KeyEntry); return ok }();  }; func tom_generate_equal_term_TransfoStratInfo(t1  TransfoStratInfo , t2  TransfoStratInfo ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_TransfoStratInfo(t any) bool { return func() bool { _, ok := (t).(TransfoStratInfo); return ok }();  }; func tom_generate_is_fun_sym_TextPosition(t  TextPosition ) bool { return func() bool { _, ok := (t).(*TextPositionTextPosition); return ok }();  }; func tom_generate_get_slot_TextPosition_Line(t  TextPosition )  int64  { return (t).(*TextPositionTextPosition).Line;  }; func tom_generate_get_slot_TextPosition_Column(t  TextPosition )  int64  { return (t).(*TextPositionTextPosition).Column;  }; func tom_generate_equal_term_Slot(t1  Slot , t2  Slot ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_Slot(t any) bool { return func() bool { _, ok := (t).(Slot); return ok }();  }; func tom_generate_equal_term_SlotList(t1  SlotList , t2  SlotList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_SlotList(t any) bool { return func() bool { _, ok := (t).(SlotList); return ok }();  }; func tom_generate_equal_term_BQSlot(t1  BQSlot , t2  BQSlot ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_BQSlot(t any) bool { return func() bool { _, ok := (t).(BQSlot); return ok }();  }; func tom_generate_equal_term_BQSlotList(t1  BQSlotList , t2  BQSlotList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_BQSlotList(t any) bool { return func() bool { _, ok := (t).(BQSlotList); return ok }();  }; func tom_generate_equal_term_PairNameDecl(t1  PairNameDecl , t2  PairNameDecl ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_PairNameDecl(t any) bool { return func() bool { _, ok := (t).(PairNameDecl); return ok }();  }; func tom_generate_equal_term_PairNameDeclList(t1  PairNameDeclList , t2  PairNameDeclList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_PairNameDeclList(t any) bool { return func() bool { _, ok := (t).(PairNameDeclList); return ok }();  }; func tom_generate_equal_term_TomTerm(t1  TomTerm , t2  TomTerm ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_TomTerm(t any) bool { return func() bool { _, ok := (t).(TomTerm); return ok }();  }; func tom_generate_equal_term_TomList(t1  TomList , t2  TomList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_TomList(t any) bool { return func() bool { _, ok := (t).(TomList); return ok }();  }; func tom_generate_is_fun_sym_Variable(t  TomTerm ) bool { return func() bool { _, ok := (t).(*VariableTomTerm); return ok }();  }; func tom_generate_get_slot_Variable_Options(t  TomTerm )  OptionList  { return (t).(*VariableTomTerm).Options;  }; func tom_generate_get_slot_Variable_AstName(t  TomTerm )  TomName  { return (t).(*VariableTomTerm).AstName;  }; func tom_generate_get_slot_Variable_AstType(t  TomTerm )  TomType  { return (t).(*VariableTomTerm).AstType;  }; func tom_generate_get_slot_Variable_Constraints(t  TomTerm )  ConstraintList  { return (t).(*VariableTomTerm).Constraints;  }; func tom_generate_is_fun_sym_VariableStar(t  TomTerm ) bool { return func() bool { _, ok := (t).(*VariableStarTomTerm); return ok }();  }; func tom_generate_get_slot_VariableStar_Options(t  TomTerm )  OptionList  { return (t).(*VariableStarTomTerm).Options;  }; func tom_generate_get_slot_VariableStar_AstName(t  TomTerm )  TomName  { return (t).(*VariableStarTomTerm).AstName;  }; func tom_generate_get_slot_VariableStar_AstType(t  TomTerm )  TomType  { return (t).(*VariableStarTomTerm).AstType;  }; func tom_generate_get_slot_VariableStar_Constraints(t  TomTerm )  ConstraintList  { return (t).(*VariableStarTomTerm).Constraints;  }; func tom_generate_equal_term_TomType(t1  TomType , t2  TomType ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_TomType(t any) bool { return func() bool { _, ok := (t).(TomType); return ok }();  }; func tom_generate_equal_term_TypeOption(t1  TypeOption , t2  TypeOption ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_TypeOption(t any) bool { return func() bool { _, ok := (t).(TypeOption); return ok }();  }; func tom_generate_equal_term_TypeOptionList(t1  TypeOptionList , t2  TypeOptionList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_TypeOptionList(t any) bool { return func() bool { _, ok := (t).(TypeOptionList); return ok }();  }; func tom_generate_equal_term_TargetLanguageType(t1  TargetLanguageType , t2  TargetLanguageType ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_TargetLanguageType(t any) bool { return func() bool { _, ok := (t).(TargetLanguageType); return ok }();  }; func tom_generate_equal_term_TomTypeList(t1  TomTypeList , t2  TomTypeList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_TomTypeList(t any) bool { return func() bool { _, ok := (t).(TomTypeList); return ok }();  }; func tom_generate_is_fun_sym_Type(t  TomType ) bool { return func() bool { _, ok := (t).(*TypeTomType); return ok }();  }; func tom_generate_get_slot_Type_TypeOptions(t  TomType )  TypeOptionList  { return (t).(*TypeTomType).TypeOptions;  }; func tom_generate_get_slot_Type_TomType(t  TomType )  string  { return (t).(*TypeTomType).TomType;  }; func tom_generate_get_slot_Type_TlType(t  TomType )  TargetLanguageType  { return (t).(*TypeTomType).TlType;  }; func tom_generate_is_fun_sym_TLType(t  TargetLanguageType ) bool { return func() bool { _, ok := (t).(*TLTypeTargetLanguageType); return ok }();  }; func tom_generate_get_slot_TLType_String(t  TargetLanguageType )  string  { return (t).(*TLTypeTargetLanguageType).String_;  }; func tom_generate_equal_term_Info(t1  Info , t2  Info ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_Info(t any) bool { return func() bool { _, ok := (t).(Info); return ok }();  }; func tom_generate_equal_term_TypeConstraint(t1  TypeConstraint , t2  TypeConstraint ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_TypeConstraint(t any) bool { return func() bool { _, ok := (t).(TypeConstraint); return ok }();  }; func tom_generate_equal_term_TypeConstraintList(t1  TypeConstraintList , t2  TypeConstraintList ) bool { return (t1).Equivalent(t2);  }; func tom_generate_is_sort_TypeConstraintList(t any) bool { return func() bool { _, ok := (t).(TypeConstraintList); return ok }();  }; 




func (g *goGenerator) generate(deep int, subject Code, moduleName string) {
	; { _ = subject; { if tom_generate_is_sort_Code(subject) { if tom_generate_is_fun_sym_Tom(any(subject).( Code )) { 
 g.generateList(deep, tom_generate_get_slot_Tom_CodeList(any(subject).( Code )), moduleName); return ;  };  };  }; { if tom_generate_is_sort_Code(subject) { if tom_generate_is_fun_sym_TomInclude(any(subject).( Code )) { 
 g.generateListInclude(deep, tom_generate_get_slot_TomInclude_CodeList(any(subject).( Code )), moduleName); return ;  };  };  }; { if tom_generate_is_sort_Code(subject) { if tom_generate_is_fun_sym_BQTermToCode(any(subject).( Code )) { 
 g.generateBQTerm(deep, tom_generate_get_slot_BQTermToCode_Bq(any(subject).( Code )), moduleName); return ;  };  };  }; { if tom_generate_is_sort_Code(subject) { if tom_generate_is_fun_sym_TargetLanguageToCode(any(subject).( Code )) { 
 g.generateTargetLanguage(deep, tom_generate_get_slot_TargetLanguageToCode_Tl(any(subject).( Code )), moduleName); return ;  };  };  }; { if tom_generate_is_sort_Code(subject) { if tom_generate_is_fun_sym_InstructionToCode(any(subject).( Code )) { 
 g.generateInstruction(deep, tom_generate_get_slot_InstructionToCode_AstInstruction(any(subject).( Code )), moduleName); return ;  };  };  }; { if tom_generate_is_sort_Code(subject) { if tom_generate_is_fun_sym_DeclarationToCode(any(subject).( Code )) { 
 g.generateDeclaration(deep, tom_generate_get_slot_DeclarationToCode_AstDeclaration(any(subject).( Code )), moduleName); return ;  };  };  };  }; 

	
	panic(&platform.RuntimeError{Msg: "Cannot generate code for: " + platform.JavaString(subject)})
}






func (g *goGenerator) generateTomTerm(deep int, subject TomTerm, moduleName string) {
	; { _ = subject; { if tom_generate_is_sort_TomTerm(subject) { var tomMatch2_4 bool =  false ; _ = tomMatch2_4; var tomMatch2_2  TomTerm  =  *new(TomTerm) ; _ = tomMatch2_2; var tomMatch2_3  TomTerm  =  *new(TomTerm) ; _ = tomMatch2_3; if tom_generate_is_fun_sym_Variable(any(subject).( TomTerm )) { { tomMatch2_4 =  true ; tomMatch2_2 = any(subject).( TomTerm );  };  } else { if tom_generate_is_fun_sym_VariableStar(any(subject).( TomTerm )) { { tomMatch2_4 =  true ; tomMatch2_3 = any(subject).( TomTerm );  };  };  }; if tomMatch2_4 { 

			g.output.WriteDeep(deep, g.getVariableName(any(subject).( TomTerm )))
			return
		;  };  };  };  }; 

}









func (g *goGenerator) generateBQTerm(deep int, subject BQTerm, moduleName string) {
	; { _ = subject; { if tom_generate_is_sort_BQTerm(subject) { if tom_generate_is_fun_sym_BuildConstant(any(subject).( BQTerm )) { var tomMatch3_1  TomName  = tom_generate_get_slot_BuildConstant_AstName(any(subject).( BQTerm )); _ = tomMatch3_1; if tom_generate_is_fun_sym_Name(any(tomMatch3_1).( TomName )) { var tom___name  string  = tom_generate_get_slot_Name_String(tomMatch3_1); _ = tom___name; 

			if longLiteral.MatchString(tom___name) {
				g.output.Write(tom___name[:len(tom___name)-1])
				return
			};  };  };  };  };  }; 


	g.generateBQTermAbstract(deep, subject, moduleName)
}






var longLiteral = regexp.MustCompile("^-?[0-9]+[lL]$")




func (g *goGenerator) generateBQTermAbstract(deep int, subject BQTerm, moduleName string) {
	; { _ = subject; { if tom_generate_is_sort_BQTerm(subject) { if tom_generate_is_fun_sym_BuildConstant(any(subject).( BQTerm )) { var tomMatch4_1  TomName  = tom_generate_get_slot_BuildConstant_AstName(any(subject).( BQTerm )); _ = tomMatch4_1; if tom_generate_is_fun_sym_Name(any(tomMatch4_1).( TomName )) { 

			g.output.Write(tom_generate_get_slot_Name_String(tomMatch4_1))
			return
		;  };  };  };  }; { if tom_generate_is_sort_BQTerm(subject) { if tom_generate_is_fun_sym_BuildTerm(any(subject).( BQTerm )) { var tomMatch4_8  TomName  = tom_generate_get_slot_BuildTerm_AstName(any(subject).( BQTerm )); _ = tomMatch4_8; if tom_generate_is_fun_sym_Name(any(tomMatch4_8).( TomName )) { 

			g.buildTerm(deep, tom_generate_get_slot_Name_String(tomMatch4_8), tom_generate_get_slot_BuildTerm_Args(any(subject).( BQTerm )), tom_generate_get_slot_BuildTerm_ModuleName(any(subject).( BQTerm )))
			return
		;  };  };  };  }; { if tom_generate_is_sort_BQTerm(subject) { var tomMatch4_24 bool =  false ; _ = tomMatch4_24; var tomMatch4_18  BQTerm  =  *new(BQTerm) ; _ = tomMatch4_18; var tomMatch4_21  BQTerm  =  *new(BQTerm) ; _ = tomMatch4_21; var tomMatch4_19  BQTerm  =  *new(BQTerm) ; _ = tomMatch4_19; var tomMatch4_22  BQTerm  =  *new(BQTerm) ; _ = tomMatch4_22; var tomMatch4_23  BQTerm  =  *new(BQTerm) ; _ = tomMatch4_23; var tomMatch4_20  BQTerm  =  *new(BQTerm) ; _ = tomMatch4_20; if tom_generate_is_fun_sym_BuildEmptyList(any(subject).( BQTerm )) { { tomMatch4_24 =  true ; tomMatch4_18 = any(subject).( BQTerm );  };  } else { if tom_generate_is_fun_sym_BuildEmptyArray(any(subject).( BQTerm )) { { tomMatch4_24 =  true ; tomMatch4_19 = any(subject).( BQTerm );  };  } else { if tom_generate_is_fun_sym_BuildConsList(any(subject).( BQTerm )) { { tomMatch4_24 =  true ; tomMatch4_20 = any(subject).( BQTerm );  };  } else { if tom_generate_is_fun_sym_BuildAppendList(any(subject).( BQTerm )) { { tomMatch4_24 =  true ; tomMatch4_21 = any(subject).( BQTerm );  };  } else { if tom_generate_is_fun_sym_BuildConsArray(any(subject).( BQTerm )) { { tomMatch4_24 =  true ; tomMatch4_22 = any(subject).( BQTerm );  };  } else { if tom_generate_is_fun_sym_BuildAppendArray(any(subject).( BQTerm )) { { tomMatch4_24 =  true ; tomMatch4_23 = any(subject).( BQTerm );  };  };  };  };  };  };  }; if tomMatch4_24 { 

			g.buildListOrArray(deep, any(subject).( BQTerm ), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_BQTerm(subject) { if tom_generate_is_fun_sym_FunctionCall(any(subject).( BQTerm )) { var tomMatch4_26  TomName  = tom_generate_get_slot_FunctionCall_AstName(any(subject).( BQTerm )); _ = tomMatch4_26; if tom_generate_is_fun_sym_Name(any(tomMatch4_26).( TomName )) { 

			g.buildFunctionCall(deep, tom_generate_get_slot_Name_String(tomMatch4_26), tom_generate_get_slot_FunctionCall_Args(any(subject).( BQTerm )), moduleName)
			return
		;  };  };  };  }; { if tom_generate_is_sort_BQTerm(subject) { var tomMatch4_37 bool =  false ; _ = tomMatch4_37; var tomMatch4_35  BQTerm  =  *new(BQTerm) ; _ = tomMatch4_35; var tomMatch4_36  BQTerm  =  *new(BQTerm) ; _ = tomMatch4_36; if tom_generate_is_fun_sym_BQVariable(any(subject).( BQTerm )) { { tomMatch4_37 =  true ; tomMatch4_35 = any(subject).( BQTerm );  };  } else { if tom_generate_is_fun_sym_BQVariableStar(any(subject).( BQTerm )) { { tomMatch4_37 =  true ; tomMatch4_36 = any(subject).( BQTerm );  };  };  }; if tomMatch4_37 { 

			g.output.WriteDeep(deep, g.getVariableNameBQ(any(subject).( BQTerm )))
			return
		;  };  };  }; { if tom_generate_is_sort_BQTerm(subject) { if tom_generate_is_fun_sym_ExpressionToBQTerm(any(subject).( BQTerm )) { 

			g.generateExpression(deep, tom_generate_get_slot_ExpressionToBQTerm_Exp(any(subject).( BQTerm )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_BQTerm(subject) { if tom_generate_is_fun_sym_Composite(any(subject).( BQTerm )) { var tomMatch4_end_46  BQTerm  = any(subject).( BQTerm ); _ = tomMatch4_end_46; for { { if !(tom_generate_is_empty_Composite_BQTerm(tomMatch4_end_46)) { var tom___t  CompositeMember  = tom_generate_get_head_Composite_BQTerm(tomMatch4_end_46); _ = tom___t; ; { _ = tom___t; { if tom_generate_is_sort_CompositeMember(tom___t) { if tom_generate_is_fun_sym_CompositeTL(any(tom___t).( CompositeMember )) { 



					g.generateTargetLanguage(deep, tom_generate_get_slot_CompositeTL_Tl(any(tom___t).( CompositeMember )), moduleName)
				;  };  };  }; { if tom_generate_is_sort_CompositeMember(tom___t) { if tom_generate_is_fun_sym_CompositeBQTerm(any(tom___t).( CompositeMember )) { 

					g.generateBQTerm(deep, tom_generate_get_slot_CompositeBQTerm_term(any(tom___t).( CompositeMember )), moduleName)
				;  };  };  };  }; ;  }; if tom_generate_is_empty_Composite_BQTerm(tomMatch4_end_46) { tomMatch4_end_46 = any(subject).( BQTerm );  } else { tomMatch4_end_46 = tom_generate_get_tail_Composite_BQTerm(tomMatch4_end_46);  };  };  if !(!(tom_generate_equal_term_BQTerm(tomMatch4_end_46, any(subject).( BQTerm )))) { break; }; };  };  };  }; { if tom_generate_is_sort_BQTerm(subject) { var tom___t  BQTerm  = any(subject).( BQTerm ); _ = tom___t; var tomMatch4_53 bool =  false ; _ = tomMatch4_53; if tom_generate_is_fun_sym_Composite(any(subject).( BQTerm )) { if tom_generate_equal_term_BQTerm(tom___t, any(subject).( BQTerm )) { tomMatch4_53 =  true ;  };  }; if !(tomMatch4_53) { 



			panic(&platform.RuntimeError{Msg: "Cannot generate code for bqterm " + platform.JavaString(tom___t)})
		;  };  };  };  }; 

}









func (g *goGenerator) getVariableNameBQ(v BQTerm) string {
	name := g.getVariableNameBQAbstract(v)
	if g.classFields[name] {
		return receiver + "." + name
	}
	return name
}






func (g *goGenerator) getVariableNameBQAbstract(v BQTerm) string {
	; { _ = v; { if tom_generate_is_sort_BQTerm(v) { if tom_generate_is_fun_sym_BQVariable(any(v).( BQTerm )) { var tomMatch6_1  TomName  = tom_generate_get_slot_BQVariable_AstName(any(v).( BQTerm )); _ = tomMatch6_1; if tom_generate_is_fun_sym_PositionName(any(tomMatch6_1).( TomName )) { 
 return "tom" + tools.TomNumberListToString(tom_generate_get_slot_PositionName_NumberList(tomMatch6_1)) ;  };  };  };  }; { if tom_generate_is_sort_BQTerm(v) { if tom_generate_is_fun_sym_BQVariable(any(v).( BQTerm )) { var tomMatch6_8  TomName  = tom_generate_get_slot_BQVariable_AstName(any(v).( BQTerm )); _ = tomMatch6_8; if tom_generate_is_fun_sym_Name(any(tomMatch6_8).( TomName )) { 
 return tom_generate_get_slot_Name_String(tomMatch6_8);  };  };  };  }; { if tom_generate_is_sort_BQTerm(v) { if tom_generate_is_fun_sym_BQVariableStar(any(v).( BQTerm )) { var tomMatch6_15  TomName  = tom_generate_get_slot_BQVariableStar_AstName(any(v).( BQTerm )); _ = tomMatch6_15; if tom_generate_is_fun_sym_PositionName(any(tomMatch6_15).( TomName )) { 
 return "tom" + tools.TomNumberListToString(tom_generate_get_slot_PositionName_NumberList(tomMatch6_15)) ;  };  };  };  }; { if tom_generate_is_sort_BQTerm(v) { if tom_generate_is_fun_sym_BQVariableStar(any(v).( BQTerm )) { var tomMatch6_22  TomName  = tom_generate_get_slot_BQVariableStar_AstName(any(v).( BQTerm )); _ = tomMatch6_22; if tom_generate_is_fun_sym_Name(any(tomMatch6_22).( TomName )) { 
 return tom_generate_get_slot_Name_String(tomMatch6_22);  };  };  };  };  }; 

	panic(&platform.RuntimeError{Msg: "cannot generate the name of the variable " + platform.JavaString(v)})
}






func (g *goGenerator) getVariableName(v TomTerm) string {
	name := g.getVariableNameAbstract(v)
	if g.classFields[name] {
		return receiver + "." + name
	}
	return name
}






func (g *goGenerator) getVariableNameAbstract(v TomTerm) string {
	; { _ = v; { if tom_generate_is_sort_TomTerm(v) { if tom_generate_is_fun_sym_Variable(any(v).( TomTerm )) { var tomMatch7_1  TomName  = tom_generate_get_slot_Variable_AstName(any(v).( TomTerm )); _ = tomMatch7_1; if tom_generate_is_fun_sym_PositionName(any(tomMatch7_1).( TomName )) { 
 return "tom" + tools.TomNumberListToString(tom_generate_get_slot_PositionName_NumberList(tomMatch7_1)) ;  };  };  };  }; { if tom_generate_is_sort_TomTerm(v) { if tom_generate_is_fun_sym_Variable(any(v).( TomTerm )) { var tomMatch7_8  TomName  = tom_generate_get_slot_Variable_AstName(any(v).( TomTerm )); _ = tomMatch7_8; if tom_generate_is_fun_sym_Name(any(tomMatch7_8).( TomName )) { 
 return tom_generate_get_slot_Name_String(tomMatch7_8);  };  };  };  }; { if tom_generate_is_sort_TomTerm(v) { if tom_generate_is_fun_sym_VariableStar(any(v).( TomTerm )) { var tomMatch7_15  TomName  = tom_generate_get_slot_VariableStar_AstName(any(v).( TomTerm )); _ = tomMatch7_15; if tom_generate_is_fun_sym_PositionName(any(tomMatch7_15).( TomName )) { 
 return "tom" + tools.TomNumberListToString(tom_generate_get_slot_PositionName_NumberList(tomMatch7_15)) ;  };  };  };  }; { if tom_generate_is_sort_TomTerm(v) { if tom_generate_is_fun_sym_VariableStar(any(v).( TomTerm )) { var tomMatch7_22  TomName  = tom_generate_get_slot_VariableStar_AstName(any(v).( TomTerm )); _ = tomMatch7_22; if tom_generate_is_fun_sym_Name(any(tomMatch7_22).( TomName )) { 
 return tom_generate_get_slot_Name_String(tomMatch7_22);  };  };  };  };  }; 

	panic(&platform.RuntimeError{Msg: "cannot generate the name of the variable " + platform.JavaString(v)})
}








func (g *goGenerator) generateExpression(deep int, subject Expression, moduleName string) {
	if subject.IsCode() {
		g.output.Write(javaTrim(subject.GetCode()))
		return
	}
	g.generateExpressionAbstract(deep, subject, moduleName)
}








func javaTrim(s string) string {
	return strings.TrimFunc(s, func(r rune) bool { return r <= ' ' })
}






func (g *goGenerator) generateExpressionAbstract(deep int, subject Expression, moduleName string) {
	; { _ = subject; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_Code(any(subject).( Expression )) { 

			g.output.Write(tom_generate_get_slot_Code_Code(any(subject).( Expression )))
			return
		;  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_Integer(any(subject).( Expression )) { 

			g.output.WriteInt(tom_generate_get_slot_Integer_value(any(subject).( Expression )))
			return
		;  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_Negation(any(subject).( Expression )) { 

			g.buildExpNegation(deep, tom_generate_get_slot_Negation_Arg(any(subject).( Expression )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_Conditional(any(subject).( Expression )) { 

			g.buildExpConditional(deep, tom_generate_get_slot_Conditional_Cond(any(subject).( Expression )), tom_generate_get_slot_Conditional_Then(any(subject).( Expression )), tom_generate_get_slot_Conditional_Else(any(subject).( Expression )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_And(any(subject).( Expression )) { 

			g.buildExpAnd(deep, tom_generate_get_slot_And_Arg1(any(subject).( Expression )), tom_generate_get_slot_And_Arg2(any(subject).( Expression )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_Or(any(subject).( Expression )) { 

			g.buildExpOr(deep, tom_generate_get_slot_Or_Arg1(any(subject).( Expression )), tom_generate_get_slot_Or_Arg2(any(subject).( Expression )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_GreaterThan(any(subject).( Expression )) { 

			g.buildExpGreaterThan(deep, tom_generate_get_slot_GreaterThan_Arg1(any(subject).( Expression )), tom_generate_get_slot_GreaterThan_Arg2(any(subject).( Expression )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_GreaterOrEqualThan(any(subject).( Expression )) { 

			g.buildExpGreaterOrEqualThan(deep, tom_generate_get_slot_GreaterOrEqualThan_Arg1(any(subject).( Expression )), tom_generate_get_slot_GreaterOrEqualThan_Arg2(any(subject).( Expression )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_LessThan(any(subject).( Expression )) { 

			g.buildExpLessThan(deep, tom_generate_get_slot_LessThan_Arg1(any(subject).( Expression )), tom_generate_get_slot_LessThan_Arg2(any(subject).( Expression )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_LessOrEqualThan(any(subject).( Expression )) { 

			g.buildExpLessOrEqualThan(deep, tom_generate_get_slot_LessOrEqualThan_Arg1(any(subject).( Expression )), tom_generate_get_slot_LessOrEqualThan_Arg2(any(subject).( Expression )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_Bottom(any(subject).( Expression )) { 

			g.buildExpBottom(deep, tom_generate_get_slot_Bottom_TomType(any(subject).( Expression )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_TrueTL(any(subject).( Expression )) { 

			g.buildExpTrue(deep)
			return
		;  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_FalseTL(any(subject).( Expression )) { 

			g.buildExpFalse(deep)
			return
		;  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_IsEmptyList(any(subject).( Expression )) { var tomMatch8_59  TomName  = tom_generate_get_slot_IsEmptyList_Opname(any(subject).( Expression )); _ = tomMatch8_59; if tom_generate_is_fun_sym_Name(any(tomMatch8_59).( TomName )) { var tom___expList  BQTerm  = tom_generate_get_slot_IsEmptyList_Variable(any(subject).( Expression )); _ = tom___expList; 

			g.buildExpIsEmptyList(deep, tom_generate_get_slot_Name_String(tomMatch8_59), g.getTermTypeBQ(tom___expList), tom___expList, moduleName)
			return
		;  };  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_IsEmptyArray(any(subject).( Expression )) { var tom___expArray  BQTerm  = tom_generate_get_slot_IsEmptyArray_Variable(any(subject).( Expression )); _ = tom___expArray; 

			g.buildExpIsEmptyArray(deep, tom_generate_get_slot_IsEmptyArray_Opname(any(subject).( Expression )), g.getTermTypeBQ(tom___expArray), tom_generate_get_slot_IsEmptyArray_Index(any(subject).( Expression )), tom___expArray, moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_EqualTerm(any(subject).( Expression )) { 

			g.buildExpEqualTerm(deep, tom_generate_get_slot_EqualTerm_TomType(any(subject).( Expression )), tom_generate_get_slot_EqualTerm_Kid1(any(subject).( Expression )), tom_generate_get_slot_EqualTerm_Kid2(any(subject).( Expression )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_EqualBQTerm(any(subject).( Expression )) { 

			g.buildExpEqualBQTerm(deep, tom_generate_get_slot_EqualBQTerm_TomType(any(subject).( Expression )), tom_generate_get_slot_EqualBQTerm_t1(any(subject).( Expression )), tom_generate_get_slot_EqualBQTerm_t2(any(subject).( Expression )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_IsSort(any(subject).( Expression )) { var tomMatch8_85  TomType  = tom_generate_get_slot_IsSort_AstType(any(subject).( Expression )); _ = tomMatch8_85; if tom_generate_is_fun_sym_Type(any(tomMatch8_85).( TomType )) { 

			g.buildExpIsSort(deep, tom_generate_get_slot_Type_TomType(tomMatch8_85), tom_generate_get_slot_IsSort_Variable(any(subject).( Expression )), moduleName)
			return
		;  };  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_IsFsym(any(subject).( Expression )) { var tomMatch8_93  TomName  = tom_generate_get_slot_IsFsym_AstName(any(subject).( Expression )); _ = tomMatch8_93; if tom_generate_is_fun_sym_Name(any(tomMatch8_93).( TomName )) { 

			g.buildExpIsFsym(deep, tom_generate_get_slot_Name_String(tomMatch8_93), tom_generate_get_slot_IsFsym_Variable(any(subject).( Expression )), moduleName)
			return
		;  };  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_Cast(any(subject).( Expression )) { var tomMatch8_101  TomType  = tom_generate_get_slot_Cast_AstType(any(subject).( Expression )); _ = tomMatch8_101; if tom_generate_is_fun_sym_Type(any(tomMatch8_101).( TomType )) { var tomMatch8_105  TargetLanguageType  = tom_generate_get_slot_Type_TlType(tomMatch8_101); _ = tomMatch8_105; if tom_generate_is_fun_sym_TLType(any(tomMatch8_105).( TargetLanguageType )) { 

			g.buildExpCast(deep, tomMatch8_105, tom_generate_get_slot_Cast_Source(any(subject).( Expression )), moduleName)
			return
		;  };  };  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_GetSlot(any(subject).( Expression )) { var tomMatch8_112  TomName  = tom_generate_get_slot_GetSlot_AstName(any(subject).( Expression )); _ = tomMatch8_112; var tomMatch8_114  BQTerm  = tom_generate_get_slot_GetSlot_Variable(any(subject).( Expression )); _ = tomMatch8_114; if tom_generate_is_fun_sym_Name(any(tomMatch8_112).( TomName )) { var tomMatch8_124 bool =  false ; _ = tomMatch8_124; var tomMatch8_123  BQTerm  =  *new(BQTerm) ; _ = tomMatch8_123; var tomMatch8_122  BQTerm  =  *new(BQTerm) ; _ = tomMatch8_122; var tomMatch8_121  BQTerm  =  *new(BQTerm) ; _ = tomMatch8_121; if tom_generate_is_fun_sym_BQVariable(any(tomMatch8_114).( BQTerm )) { { tomMatch8_124 =  true ; tomMatch8_121 = tomMatch8_114;  };  } else { if tom_generate_is_fun_sym_BuildTerm(any(tomMatch8_114).( BQTerm )) { { tomMatch8_124 =  true ; tomMatch8_122 = tomMatch8_114;  };  } else { if tom_generate_is_fun_sym_ExpressionToBQTerm(any(tomMatch8_114).( BQTerm )) { { tomMatch8_124 =  true ; tomMatch8_123 = tomMatch8_114;  };  };  };  }; if tomMatch8_124 { 

			g.buildExpGetSlot(deep, tom_generate_get_slot_Name_String(tomMatch8_112), tom_generate_get_slot_GetSlot_SlotNameString(any(subject).( Expression )), tomMatch8_114, moduleName)
			return
		;  };  };  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_GetSlot(any(subject).( Expression )) { var tomMatch8_127  TomName  = tom_generate_get_slot_GetSlot_AstName(any(subject).( Expression )); _ = tomMatch8_127; var tomMatch8_129  BQTerm  = tom_generate_get_slot_GetSlot_Variable(any(subject).( Expression )); _ = tomMatch8_129; if tom_generate_is_fun_sym_Name(any(tomMatch8_127).( TomName )) { if tom_generate_is_fun_sym_ExpressionToBQTerm(any(tomMatch8_129).( BQTerm )) { if tom_generate_is_fun_sym_GetSlot(any(tom_generate_get_slot_ExpressionToBQTerm_Exp(tomMatch8_129)).( Expression )) { 

			g.buildExpGetSlot(deep, tom_generate_get_slot_Name_String(tomMatch8_127), tom_generate_get_slot_GetSlot_SlotNameString(any(subject).( Expression )), tomMatch8_129, moduleName)
			return
		;  };  };  };  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_GetHead(any(subject).( Expression )) { var tomMatch8_141  TomName  = tom_generate_get_slot_GetHead_Opname(any(subject).( Expression )); _ = tomMatch8_141; if tom_generate_is_fun_sym_Name(any(tomMatch8_141).( TomName )) { var tom___exp  BQTerm  = tom_generate_get_slot_GetHead_Variable(any(subject).( Expression )); _ = tom___exp; 

			g.buildExpGetHead(deep, tom_generate_get_slot_Name_String(tomMatch8_141), g.getTermTypeBQ(tom___exp), tom_generate_get_slot_GetHead_Codomain(any(subject).( Expression )), tom___exp, moduleName)
			return
		;  };  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_GetTail(any(subject).( Expression )) { var tomMatch8_150  TomName  = tom_generate_get_slot_GetTail_Opname(any(subject).( Expression )); _ = tomMatch8_150; if tom_generate_is_fun_sym_Name(any(tomMatch8_150).( TomName )) { var tom___exp  BQTerm  = tom_generate_get_slot_GetTail_Variable(any(subject).( Expression )); _ = tom___exp; 

			g.buildExpGetTail(deep, tom_generate_get_slot_Name_String(tomMatch8_150), g.getTermTypeBQ(tom___exp), tom___exp, moduleName)
			return
		;  };  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_AddOne(any(subject).( Expression )) { 

			g.buildAddOne(deep, tom_generate_get_slot_AddOne_Variable(any(subject).( Expression )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_SubstractOne(any(subject).( Expression )) { 

			g.buildSubstractOne(deep, tom_generate_get_slot_SubstractOne_Variable(any(subject).( Expression )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_Substract(any(subject).( Expression )) { 

			g.buildSubstract(deep, tom_generate_get_slot_Substract_Term1(any(subject).( Expression )), tom_generate_get_slot_Substract_Term2(any(subject).( Expression )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_GetSize(any(subject).( Expression )) { var tom___exp  BQTerm  = tom_generate_get_slot_GetSize_Variable(any(subject).( Expression )); _ = tom___exp; 

			g.buildExpGetSize(deep, tom_generate_get_slot_GetSize_Opname(any(subject).( Expression )), g.getTermTypeBQ(tom___exp), tom___exp, moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_GetElement(any(subject).( Expression )) { var tom___varName  BQTerm  = tom_generate_get_slot_GetElement_Variable(any(subject).( Expression )); _ = tom___varName; 

			g.buildExpGetElement(deep, tom_generate_get_slot_GetElement_Opname(any(subject).( Expression )), g.getTermTypeBQ(tom___varName), tom___varName, tom_generate_get_slot_GetElement_Index(any(subject).( Expression )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_GetSliceList(any(subject).( Expression )) { var tomMatch8_183  TomName  = tom_generate_get_slot_GetSliceList_AstName(any(subject).( Expression )); _ = tomMatch8_183; if tom_generate_is_fun_sym_Name(any(tomMatch8_183).( TomName )) { 

			g.buildExpGetSliceList(deep, tom_generate_get_slot_Name_String(tomMatch8_183), tom_generate_get_slot_GetSliceList_VariableBeginAST(any(subject).( Expression )), tom_generate_get_slot_GetSliceList_VariableEndAST(any(subject).( Expression )), tom_generate_get_slot_GetSliceList_Tail(any(subject).( Expression )), moduleName)
			return
		;  };  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_GetSliceArray(any(subject).( Expression )) { var tomMatch8_193  TomName  = tom_generate_get_slot_GetSliceArray_AstName(any(subject).( Expression )); _ = tomMatch8_193; if tom_generate_is_fun_sym_Name(any(tomMatch8_193).( TomName )) { 

			g.buildExpGetSliceArray(deep, tom_generate_get_slot_Name_String(tomMatch8_193), tom_generate_get_slot_GetSliceArray_SubjectListName(any(subject).( Expression )), tom_generate_get_slot_GetSliceArray_VariableBeginAST(any(subject).( Expression )), tom_generate_get_slot_GetSliceArray_VariableEndAST(any(subject).( Expression )), moduleName)
			return
		;  };  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_BQTermToExpression(any(subject).( Expression )) { 

			g.generateBQTerm(deep, tom_generate_get_slot_BQTermToExpression_AstTerm(any(subject).( Expression )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Expression(subject) { if tom_generate_is_fun_sym_TomInstructionToExpression(any(subject).( Expression )) { 

			g.generateInstruction(deep, tom_generate_get_slot_TomInstructionToExpression_Instruction(any(subject).( Expression )), moduleName)
			return
		;  };  };  };  }; 

	
	panic(&platform.RuntimeError{Msg: "Cannot generate code for expression: " + platform.JavaString(subject)})
}






func (g *goGenerator) generateArray(deep int, subject BQTerm, index BQTerm, moduleName string) {
	; { _ = subject; { if tom_generate_is_sort_BQTerm(subject) { if tom_generate_is_fun_sym_BQVariable(any(subject).( BQTerm )) { var tomMatch9_1  TomName  = tom_generate_get_slot_BQVariable_AstName(any(subject).( BQTerm )); _ = tomMatch9_1; if tom_generate_is_fun_sym_PositionName(any(tomMatch9_1).( TomName )) { 

			g.output.Write("tom" + tools.TomNumberListToString(tom_generate_get_slot_PositionName_NumberList(tomMatch9_1)))
		;  };  };  };  }; { if tom_generate_is_sort_BQTerm(subject) { if tom_generate_is_fun_sym_BQVariable(any(subject).( BQTerm )) { var tomMatch9_8  TomName  = tom_generate_get_slot_BQVariable_AstName(any(subject).( BQTerm )); _ = tomMatch9_8; if tom_generate_is_fun_sym_Name(any(tomMatch9_8).( TomName )) { 

			g.output.Write(tom_generate_get_slot_Name_String(tomMatch9_8))
		;  };  };  };  };  }; ; { _ = index; { if tom_generate_is_sort_BQTerm(index) { if tom_generate_is_fun_sym_BQVariable(any(index).( BQTerm )) { var tomMatch10_1  TomName  = tom_generate_get_slot_BQVariable_AstName(any(index).( BQTerm )); _ = tomMatch10_1; if tom_generate_is_fun_sym_PositionName(any(tomMatch10_1).( TomName )) { 



			g.output.Write("[")
			g.output.Write("tom" + tools.TomNumberListToString(tom_generate_get_slot_PositionName_NumberList(tomMatch10_1)))
			g.output.Write("]")
		;  };  };  };  }; { if tom_generate_is_sort_BQTerm(index) { if tom_generate_is_fun_sym_BQVariable(any(index).( BQTerm )) { var tomMatch10_8  TomName  = tom_generate_get_slot_BQVariable_AstName(any(index).( BQTerm )); _ = tomMatch10_8; if tom_generate_is_fun_sym_Name(any(tomMatch10_8).( TomName )) { 

			g.output.Write("[")
			g.output.Write(tom_generate_get_slot_Name_String(tomMatch10_8))
			g.output.Write("]")
		;  };  };  };  }; { if tom_generate_is_sort_BQTerm(index) { if tom_generate_is_fun_sym_ExpressionToBQTerm(any(index).( BQTerm )) { var tomMatch10_15  Expression  = tom_generate_get_slot_ExpressionToBQTerm_Exp(any(index).( BQTerm )); _ = tomMatch10_15; if tom_generate_is_fun_sym_Integer(any(tomMatch10_15).( Expression )) { 

			g.output.Write("[")
			g.output.WriteInt(tom_generate_get_slot_Integer_value(tomMatch10_15))
			g.output.Write("]")
		;  };  };  };  };  }; 

}









func (g *goGenerator) generateInstruction(deep int, subject Instruction, moduleName string) {
	; { _ = subject; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_UnamedBlock(any(subject).( Instruction )) { var tomMatch11_1  InstructionList  = tom_generate_get_slot_UnamedBlock_InstList(any(subject).( Instruction )); _ = tomMatch11_1; if tom_generate_is_fun_sym_concInstruction(any(tomMatch11_1).( InstructionList )) { if !(tom_generate_is_empty_concInstruction_InstructionList(tomMatch11_1)) { if tom_generate_is_fun_sym_CompiledMatch(any(tom_generate_get_head_concInstruction_InstructionList(tomMatch11_1)).( Instruction )) { 
 g.output.Write("; ") ;  };  };  };  };  };  };  }; 

	g.generateInstructionAbstract(deep, subject, moduleName)
	; { _ = subject; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_RawAction(any(subject).( Instruction )) { 
 g.output.Write("; ") ;  };  };  };  }; 

}







func (g *goGenerator) generateInstructionAbstract(deep int, subject Instruction, moduleName string) {
	; { _ = subject; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_CodeToInstruction(any(subject).( Instruction )) { 

			g.generate(deep, tom_generate_get_slot_CodeToInstruction_Code(any(subject).( Instruction )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_BQTermToInstruction(any(subject).( Instruction )) { 

			g.generateBQTerm(deep, tom_generate_get_slot_BQTermToInstruction_Tom(any(subject).( Instruction )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_ExpressionToInstruction(any(subject).( Instruction )) { 

			g.generateExpression(deep, tom_generate_get_slot_ExpressionToInstruction_Expr(any(subject).( Instruction )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_Nop(any(subject).( Instruction )) { 

			return
		;  };  };  }; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_Assign(any(subject).( Instruction )) { var tomMatch13_16  BQTerm  = tom_generate_get_slot_Assign_Variable(any(subject).( Instruction )); _ = tomMatch13_16; var tomMatch13_24 bool =  false ; _ = tomMatch13_24; var tomMatch13_22  BQTerm  =  *new(BQTerm) ; _ = tomMatch13_22; var tomMatch13_20  OptionList  =  *new(OptionList) ; _ = tomMatch13_20; var tomMatch13_23  BQTerm  =  *new(BQTerm) ; _ = tomMatch13_23; if tom_generate_is_fun_sym_BQVariable(any(tomMatch13_16).( BQTerm )) { { tomMatch13_24 =  true ; tomMatch13_22 = tomMatch13_16; tomMatch13_20 = tom_generate_get_slot_BQVariable_Options(tomMatch13_22);  };  } else { if tom_generate_is_fun_sym_BQVariableStar(any(tomMatch13_16).( BQTerm )) { { tomMatch13_24 =  true ; tomMatch13_23 = tomMatch13_16; tomMatch13_20 = tom_generate_get_slot_BQVariableStar_Options(tomMatch13_23);  };  };  }; if tomMatch13_24 { 

			g.buildAssign(deep, tomMatch13_16, tomMatch13_20, tom_generate_get_slot_Assign_Source(any(subject).( Instruction )), moduleName)
			return
		;  };  };  };  }; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_AssignArray(any(subject).( Instruction )) { var tomMatch13_26  BQTerm  = tom_generate_get_slot_AssignArray_Variable(any(subject).( Instruction )); _ = tomMatch13_26; if tom_generate_is_fun_sym_BQVariable(any(tomMatch13_26).( BQTerm )) { 

			g.buildAssignArray(deep, tomMatch13_26, tom_generate_get_slot_BQVariable_Options(tomMatch13_26), tom_generate_get_slot_AssignArray_Index(any(subject).( Instruction )), tom_generate_get_slot_AssignArray_Source(any(subject).( Instruction )), moduleName)
			return
		;  };  };  };  }; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_Let(any(subject).( Instruction )) { var tomMatch13_35  BQTerm  = tom_generate_get_slot_Let_Variable(any(subject).( Instruction )); _ = tomMatch13_35; var tomMatch13_48 bool =  false ; _ = tomMatch13_48; var tomMatch13_44  BQTerm  =  *new(BQTerm) ; _ = tomMatch13_44; var tomMatch13_43  BQTerm  =  *new(BQTerm) ; _ = tomMatch13_43; var tomMatch13_41  TomType  =  *new(TomType) ; _ = tomMatch13_41; var tomMatch13_40  OptionList  =  *new(OptionList) ; _ = tomMatch13_40; if tom_generate_is_fun_sym_BQVariable(any(tomMatch13_35).( BQTerm )) { { tomMatch13_48 =  true ; tomMatch13_43 = tomMatch13_35; tomMatch13_40 = tom_generate_get_slot_BQVariable_Options(tomMatch13_43); tomMatch13_41 = tom_generate_get_slot_BQVariable_AstType(tomMatch13_43);  };  } else { if tom_generate_is_fun_sym_BQVariableStar(any(tomMatch13_35).( BQTerm )) { { tomMatch13_48 =  true ; tomMatch13_44 = tomMatch13_35; tomMatch13_40 = tom_generate_get_slot_BQVariableStar_Options(tomMatch13_44); tomMatch13_41 = tom_generate_get_slot_BQVariableStar_AstType(tomMatch13_44);  };  };  }; if tomMatch13_48 { if tom_generate_is_fun_sym_Type(any(tomMatch13_41).( TomType )) { 

			g.buildLet(deep, tomMatch13_35, tomMatch13_40, tom_generate_get_slot_Type_TlType(tomMatch13_41), tom_generate_get_slot_Let_Source(any(subject).( Instruction )), tom_generate_get_slot_Let_AstInstruction(any(subject).( Instruction )), moduleName)
			return
		;  };  };  };  };  }; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_LetRef(any(subject).( Instruction )) { var tomMatch13_50  BQTerm  = tom_generate_get_slot_LetRef_Variable(any(subject).( Instruction )); _ = tomMatch13_50; var tomMatch13_63 bool =  false ; _ = tomMatch13_63; var tomMatch13_59  BQTerm  =  *new(BQTerm) ; _ = tomMatch13_59; var tomMatch13_56  TomType  =  *new(TomType) ; _ = tomMatch13_56; var tomMatch13_58  BQTerm  =  *new(BQTerm) ; _ = tomMatch13_58; var tomMatch13_55  OptionList  =  *new(OptionList) ; _ = tomMatch13_55; if tom_generate_is_fun_sym_BQVariable(any(tomMatch13_50).( BQTerm )) { { tomMatch13_63 =  true ; tomMatch13_58 = tomMatch13_50; tomMatch13_55 = tom_generate_get_slot_BQVariable_Options(tomMatch13_58); tomMatch13_56 = tom_generate_get_slot_BQVariable_AstType(tomMatch13_58);  };  } else { if tom_generate_is_fun_sym_BQVariableStar(any(tomMatch13_50).( BQTerm )) { { tomMatch13_63 =  true ; tomMatch13_59 = tomMatch13_50; tomMatch13_55 = tom_generate_get_slot_BQVariableStar_Options(tomMatch13_59); tomMatch13_56 = tom_generate_get_slot_BQVariableStar_AstType(tomMatch13_59);  };  };  }; if tomMatch13_63 { if tom_generate_is_fun_sym_Type(any(tomMatch13_56).( TomType )) { 

			g.buildLetRef(deep, tomMatch13_50, tomMatch13_55, tom_generate_get_slot_Type_TlType(tomMatch13_56), tom_generate_get_slot_LetRef_Source(any(subject).( Instruction )), tom_generate_get_slot_LetRef_AstInstruction(any(subject).( Instruction )), moduleName)
			return
		;  };  };  };  };  }; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_AbstractBlock(any(subject).( Instruction )) { 

			g.buildInstructionSequence(deep, tom_generate_get_slot_AbstractBlock_InstList(any(subject).( Instruction )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_UnamedBlock(any(subject).( Instruction )) { 

			g.buildUnamedBlock(deep, tom_generate_get_slot_UnamedBlock_InstList(any(subject).( Instruction )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_NamedBlock(any(subject).( Instruction )) { 

			g.buildNamedBlock(deep, tom_generate_get_slot_NamedBlock_BlockName(any(subject).( Instruction )), tom_generate_get_slot_NamedBlock_InstList(any(subject).( Instruction )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_If(any(subject).( Instruction )) { if tom_generate_is_fun_sym_Nop(any(tom_generate_get_slot_If_FailureInst(any(subject).( Instruction ))).( Instruction )) { 

			g.buildIf(deep, tom_generate_get_slot_If_Condition(any(subject).( Instruction )), tom_generate_get_slot_If_SuccesInst(any(subject).( Instruction )), moduleName)
			return
		;  };  };  };  }; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_If(any(subject).( Instruction )) { if tom_generate_is_fun_sym_Nop(any(tom_generate_get_slot_If_SuccesInst(any(subject).( Instruction ))).( Instruction )) { 

			g.buildIf(deep, tom_generate_make_Negation(tom_generate_get_slot_If_Condition(any(subject).( Instruction ))), tom_generate_get_slot_If_FailureInst(any(subject).( Instruction )), moduleName)
			return
		;  };  };  };  }; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_If(any(subject).( Instruction )) { 

			g.buildIfWithFailure(deep, tom_generate_get_slot_If_Condition(any(subject).( Instruction )), tom_generate_get_slot_If_SuccesInst(any(subject).( Instruction )), tom_generate_get_slot_If_FailureInst(any(subject).( Instruction )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_DoWhile(any(subject).( Instruction )) { 

			g.buildDoWhile(deep, tom_generate_get_slot_DoWhile_DoInst(any(subject).( Instruction )), tom_generate_get_slot_DoWhile_Condition(any(subject).( Instruction )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_WhileDo(any(subject).( Instruction )) { 

			g.buildWhileDo(deep, tom_generate_get_slot_WhileDo_Condition(any(subject).( Instruction )), tom_generate_get_slot_WhileDo_DoInst(any(subject).( Instruction )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_RawAction(any(subject).( Instruction )) { var tomMatch13_110  Instruction  = tom_generate_get_slot_RawAction_AstInstruction(any(subject).( Instruction )); _ = tomMatch13_110; if tom_generate_is_fun_sym_AbstractBlock(any(tomMatch13_110).( Instruction )) { 

			g.generateInstructionList(deep, tom_generate_get_slot_AbstractBlock_InstList(tomMatch13_110), moduleName)
			return
		;  };  };  };  }; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_RawAction(any(subject).( Instruction )) { 

			g.generateInstruction(deep, tom_generate_get_slot_RawAction_AstInstruction(any(subject).( Instruction )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_Return(any(subject).( Instruction )) { 

			g.buildReturn(deep, tom_generate_get_slot_Return_Kid1(any(subject).( Instruction )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_CompiledMatch(any(subject).( Instruction )) { 

			g.generateInstruction(deep, tom_generate_get_slot_CompiledMatch_AutomataInst(any(subject).( Instruction )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_CompiledPattern(any(subject).( Instruction )) { 

			g.generateInstruction(deep, tom_generate_get_slot_CompiledPattern_AutomataInst(any(subject).( Instruction )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_Tracelink(any(subject).( Instruction )) { var tomMatch13_135  TomName  = tom_generate_get_slot_Tracelink_Type(any(subject).( Instruction )); _ = tomMatch13_135; var tomMatch13_136  TomName  = tom_generate_get_slot_Tracelink_Name(any(subject).( Instruction )); _ = tomMatch13_136; if tom_generate_is_fun_sym_Name(any(tomMatch13_135).( TomName )) { if tom_generate_is_fun_sym_Name(any(tomMatch13_136).( TomName )) { 

			g.buildTracelink(deep, tom_generate_get_slot_Name_String(tomMatch13_135), tom_generate_get_slot_Name_String(tomMatch13_136), tom_generate_get_slot_Tracelink_Expr(any(subject).( Instruction )), moduleName)
			return
		;  };  };  };  };  }; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_TracelinkPopulateResolve(any(subject).( Instruction )) { var tomMatch13_149  TomName  = tom_generate_get_slot_TracelinkPopulateResolve_RefClassName(any(subject).( Instruction )); _ = tomMatch13_149; if tom_generate_is_fun_sym_Name(any(tomMatch13_149).( TomName )) { 

			g.buildTracelinkPopulateResolve(deep, tom_generate_get_slot_Name_String(tomMatch13_149), tom_generate_get_slot_TracelinkPopulateResolve_TracedLinks(any(subject).( Instruction )), tom_generate_get_slot_TracelinkPopulateResolve_Current(any(subject).( Instruction )), tom_generate_get_slot_TracelinkPopulateResolve_Link(any(subject).( Instruction )), moduleName)
			return
		;  };  };  };  }; { if tom_generate_is_sort_Instruction(subject) { if tom_generate_is_fun_sym_Resolve(any(subject).( Instruction )) { 

			g.generateBQTerm(deep, tom_generate_get_slot_Resolve_ResolveBQTerm(any(subject).( Instruction )), moduleName)
			return
		;  };  };  };  }; 

	
	panic(&platform.RuntimeError{Msg: "Cannot generate code for instruction: " + platform.JavaString(subject)})
}






func (g *goGenerator) generateTargetLanguage(deep int, subject TargetLanguage, moduleName string) {
	; { _ = subject; { if tom_generate_is_sort_TargetLanguage(subject) { if tom_generate_is_fun_sym_TL(any(subject).( TargetLanguage )) { var tomMatch14_2  TextPosition  = tom_generate_get_slot_TL_Start(any(subject).( TargetLanguage )); _ = tomMatch14_2; var tomMatch14_3  TextPosition  = tom_generate_get_slot_TL_End(any(subject).( TargetLanguage )); _ = tomMatch14_3; if tom_generate_is_fun_sym_TextPosition(any(tomMatch14_2).( TextPosition )) { var tom___startLine  int64  = tom_generate_get_slot_TextPosition_Line(tomMatch14_2); _ = tom___startLine; if tom_generate_is_fun_sym_TextPosition(any(tomMatch14_3).( TextPosition )) { 

			length := tom_generate_get_slot_TextPosition_Line(tomMatch14_3) - tom___startLine
			g.output.WriteTL(deep, tom_generate_get_slot_TL_Code(any(subject).( TargetLanguage )), tom___startLine, length)
			return
		;  };  };  };  };  }; { if tom_generate_is_sort_TargetLanguage(subject) { if tom_generate_is_fun_sym_ITL(any(subject).( TargetLanguage )) { 

			g.output.Write(tom_generate_get_slot_ITL_Code(any(subject).( TargetLanguage )))
			return
		;  };  };  }; { if tom_generate_is_sort_TargetLanguage(subject) { if tom_generate_is_fun_sym_Comment(any(subject).( TargetLanguage )) { 

			g.buildComment(deep, tom_generate_get_slot_Comment_Code(any(subject).( TargetLanguage )))
			return
		;  };  };  };  }; 

	
	panic(&platform.RuntimeError{Msg: "Cannot generate code for TL: " + platform.JavaString(subject)})
}




func (g *goGenerator) generateOption(deep int, subject Option, moduleName string) {
	; { _ = subject; { if tom_generate_is_sort_Option(subject) { if tom_generate_is_fun_sym_DeclarationToOption(any(subject).( Option )) { 

			g.generateDeclaration(deep, tom_generate_get_slot_DeclarationToOption_AstDeclaration(any(subject).( Option )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Option(subject) { if tom_generate_is_fun_sym_OriginTracking(any(subject).( Option )) { 
 return ;  };  };  }; { if tom_generate_is_sort_Option(subject) { if tom_generate_is_fun_sym_ACSymbol(any(subject).( Option )) { 
 return ;  };  };  };  }; 

	panic(&platform.RuntimeError{Msg: "Cannot generate code for option: " + platform.JavaString(subject)})
}








func (g *goGenerator) generateDeclaration(deep int, subject Declaration, moduleName string) {
	; { _ = subject; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_EmptyDeclaration(any(subject).( Declaration )) { 

			return
		;  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_AbstractDecl(any(subject).( Declaration )) { 

			g.generateDeclarationList(deep, tom_generate_get_slot_AbstractDecl_DeclList(any(subject).( Declaration )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_FunctionDef(any(subject).( Declaration )) { var tomMatch16_8  TomName  = tom_generate_get_slot_FunctionDef_AstName(any(subject).( Declaration )); _ = tomMatch16_8; if tom_generate_is_fun_sym_Name(any(tomMatch16_8).( TomName )) { 

			g.buildFunctionDef(deep, tom_generate_get_slot_Name_String(tomMatch16_8), tom_generate_get_slot_FunctionDef_ArgumentList(any(subject).( Declaration )), tom_generate_get_slot_FunctionDef_Codomain(any(subject).( Declaration )), tom_generate_get_slot_FunctionDef_ThrowsType(any(subject).( Declaration )), tom_generate_get_slot_FunctionDef_Instruction(any(subject).( Declaration )), moduleName)
			return
		;  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_MethodDef(any(subject).( Declaration )) { var tomMatch16_19  TomName  = tom_generate_get_slot_MethodDef_AstName(any(subject).( Declaration )); _ = tomMatch16_19; if tom_generate_is_fun_sym_Name(any(tomMatch16_19).( TomName )) { 

			g.buildMethodDef(deep, tom_generate_get_slot_Name_String(tomMatch16_19), tom_generate_get_slot_MethodDef_ArgumentList(any(subject).( Declaration )), tom_generate_get_slot_MethodDef_Codomain(any(subject).( Declaration )), tom_generate_get_slot_MethodDef_ThrowsType(any(subject).( Declaration )), tom_generate_get_slot_MethodDef_Instruction(any(subject).( Declaration )), moduleName)
			return
		;  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_Class(any(subject).( Declaration )) { var tomMatch16_30  TomName  = tom_generate_get_slot_Class_AstName(any(subject).( Declaration )); _ = tomMatch16_30; if tom_generate_is_fun_sym_Name(any(tomMatch16_30).( TomName )) { 

			g.buildClass(deep, tom_generate_get_slot_Name_String(tomMatch16_30), tom_generate_get_slot_Class_ExtendsType(any(subject).( Declaration )), tom_generate_get_slot_Class_SuperTerm(any(subject).( Declaration )), tom_generate_get_slot_Class_Declaration(any(subject).( Declaration )), moduleName)
			return
		;  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_IntrospectorClass(any(subject).( Declaration )) { var tomMatch16_40  TomName  = tom_generate_get_slot_IntrospectorClass_AstName(any(subject).( Declaration )); _ = tomMatch16_40; if tom_generate_is_fun_sym_Name(any(tomMatch16_40).( TomName )) { 

			g.buildIntrospectorClass(deep, tom_generate_get_slot_Name_String(tomMatch16_40), tom_generate_get_slot_IntrospectorClass_Declaration(any(subject).( Declaration )), moduleName)
			return
		;  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_SymbolDecl(any(subject).( Declaration )) { var tomMatch16_48  TomName  = tom_generate_get_slot_SymbolDecl_AstName(any(subject).( Declaration )); _ = tomMatch16_48; if tom_generate_is_fun_sym_Name(any(tomMatch16_48).( TomName )) { 

			g.buildSymbolDecl(deep, tom_generate_get_slot_Name_String(tomMatch16_48), moduleName)
			return
		;  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_ArraySymbolDecl(any(subject).( Declaration )) { var tomMatch16_55  TomName  = tom_generate_get_slot_ArraySymbolDecl_AstName(any(subject).( Declaration )); _ = tomMatch16_55; if tom_generate_is_fun_sym_Name(any(tomMatch16_55).( TomName )) { var tom___tomName  string  = tom_generate_get_slot_Name_String(tomMatch16_55); _ = tom___tomName; 

			if g.getSymbolTable(moduleName).IsUsedSymbolConstructorName(tom___tomName) ||
				g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(tom___tomName) {
				g.buildSymbolDecl(deep, tom___tomName, moduleName)
				g.genDeclArray(tom___tomName, moduleName)
			}
			return
		;  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { var tomMatch16_69 bool =  false ; _ = tomMatch16_69; var tomMatch16_64  Declaration  =  *new(Declaration) ; _ = tomMatch16_64; var tomMatch16_62  TomName  =  *new(TomName) ; _ = tomMatch16_62; var tomMatch16_65  Declaration  =  *new(Declaration) ; _ = tomMatch16_65; if tom_generate_is_fun_sym_ListSymbolDecl(any(subject).( Declaration )) { { tomMatch16_69 =  true ; tomMatch16_64 = any(subject).( Declaration ); tomMatch16_62 = tom_generate_get_slot_ListSymbolDecl_AstName(tomMatch16_64);  };  } else { if tom_generate_is_fun_sym_ACSymbolDecl(any(subject).( Declaration )) { { tomMatch16_69 =  true ; tomMatch16_65 = any(subject).( Declaration ); tomMatch16_62 = tom_generate_get_slot_ACSymbolDecl_AstName(tomMatch16_65);  };  };  }; if tomMatch16_69 { if tom_generate_is_fun_sym_Name(any(tomMatch16_62).( TomName )) { var tom___tomName  string  = tom_generate_get_slot_Name_String(tomMatch16_62); _ = tom___tomName; 

			if g.getSymbolTable(moduleName).IsUsedSymbolConstructorName(tom___tomName) ||
				g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(tom___tomName) {
				g.buildSymbolDecl(deep, tom___tomName, moduleName)
				g.genDeclList(tom___tomName, moduleName)
			}
			return
		;  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_IsFsymDecl(any(subject).( Declaration )) { var tomMatch16_71  TomName  = tom_generate_get_slot_IsFsymDecl_AstName(any(subject).( Declaration )); _ = tomMatch16_71; var tomMatch16_72  BQTerm  = tom_generate_get_slot_IsFsymDecl_Variable(any(subject).( Declaration )); _ = tomMatch16_72; if tom_generate_is_fun_sym_Name(any(tomMatch16_71).( TomName )) { var tom___tomName  string  = tom_generate_get_slot_Name_String(tomMatch16_71); _ = tom___tomName; if tom_generate_is_fun_sym_BQVariable(any(tomMatch16_72).( BQTerm )) { var tomMatch16_80  TomName  = tom_generate_get_slot_BQVariable_AstName(tomMatch16_72); _ = tomMatch16_80; var tomMatch16_81  TomType  = tom_generate_get_slot_BQVariable_AstType(tomMatch16_72); _ = tomMatch16_81; if tom_generate_is_fun_sym_Name(any(tomMatch16_80).( TomName )) { if tom_generate_is_fun_sym_Type(any(tomMatch16_81).( TomType )) { var tomMatch16_87  TargetLanguageType  = tom_generate_get_slot_Type_TlType(tomMatch16_81); _ = tomMatch16_87; if tom_generate_is_fun_sym_TLType(any(tomMatch16_87).( TargetLanguageType )) { 

			if g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(tom___tomName) {
				g.buildIsFsymDecl(deep, tom___tomName, tom_generate_get_slot_Name_String(tomMatch16_80), tomMatch16_87, tom_generate_get_slot_IsFsymDecl_Expr(any(subject).( Declaration )), moduleName)
			}
			return
		;  };  };  };  };  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_GetSlotDecl(any(subject).( Declaration )) { var tomMatch16_93  TomName  = tom_generate_get_slot_GetSlotDecl_AstName(any(subject).( Declaration )); _ = tomMatch16_93; var tomMatch16_95  BQTerm  = tom_generate_get_slot_GetSlotDecl_Variable(any(subject).( Declaration )); _ = tomMatch16_95; if tom_generate_is_fun_sym_Name(any(tomMatch16_93).( TomName )) { var tom___tomName  string  = tom_generate_get_slot_Name_String(tomMatch16_93); _ = tom___tomName; if tom_generate_is_fun_sym_BQVariable(any(tomMatch16_95).( BQTerm )) { var tomMatch16_102  TomName  = tom_generate_get_slot_BQVariable_AstName(tomMatch16_95); _ = tomMatch16_102; var tomMatch16_103  TomType  = tom_generate_get_slot_BQVariable_AstType(tomMatch16_95); _ = tomMatch16_103; if tom_generate_is_fun_sym_Name(any(tomMatch16_102).( TomName )) { if tom_generate_is_fun_sym_Type(any(tomMatch16_103).( TomType )) { var tomMatch16_109  TargetLanguageType  = tom_generate_get_slot_Type_TlType(tomMatch16_103); _ = tomMatch16_109; if tom_generate_is_fun_sym_TLType(any(tomMatch16_109).( TargetLanguageType )) { 

			if g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(tom___tomName) {
				g.buildGetSlotDecl(deep, tom___tomName, tom_generate_get_slot_Name_String(tomMatch16_102), tomMatch16_109, tom_generate_get_slot_GetSlotDecl_Expr(any(subject).( Declaration )), tom_generate_get_slot_GetSlotDecl_SlotName(any(subject).( Declaration )), moduleName)
			}
			return
		;  };  };  };  };  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_ImplementDecl(any(subject).( Declaration )) { if tom_generate_is_fun_sym_Name(any(tom_generate_get_slot_ImplementDecl_AstName(any(subject).( Declaration ))).( TomName )) { 

			
			return
		;  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_GetDefaultDecl(any(subject).( Declaration )) { var tomMatch16_122  TomName  = tom_generate_get_slot_GetDefaultDecl_AstName(any(subject).( Declaration )); _ = tomMatch16_122; if tom_generate_is_fun_sym_Name(any(tomMatch16_122).( TomName )) { var tom___tomName  string  = tom_generate_get_slot_Name_String(tomMatch16_122); _ = tom___tomName; 

			if g.getSymbolTable(moduleName).IsUsedSymbolConstructorName(tom___tomName) {
				g.buildGetDefaultDecl(deep, tom___tomName, tom_generate_get_slot_GetDefaultDecl_Expr(any(subject).( Declaration )), tom_generate_get_slot_GetDefaultDecl_SlotName(any(subject).( Declaration )), moduleName)
			}
			return
		;  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_EqualTermDecl(any(subject).( Declaration )) { var tomMatch16_131  BQTerm  = tom_generate_get_slot_EqualTermDecl_TermArg1(any(subject).( Declaration )); _ = tomMatch16_131; var tomMatch16_132  BQTerm  = tom_generate_get_slot_EqualTermDecl_TermArg2(any(subject).( Declaration )); _ = tomMatch16_132; if tom_generate_is_fun_sym_BQVariable(any(tomMatch16_131).( BQTerm )) { var tomMatch16_137  TomName  = tom_generate_get_slot_BQVariable_AstName(tomMatch16_131); _ = tomMatch16_137; var tomMatch16_138  TomType  = tom_generate_get_slot_BQVariable_AstType(tomMatch16_131); _ = tomMatch16_138; if tom_generate_is_fun_sym_Name(any(tomMatch16_137).( TomName )) { if tom_generate_is_fun_sym_Type(any(tomMatch16_138).( TomType )) { var tom___type1  string  = tom_generate_get_slot_Type_TomType(tomMatch16_138); _ = tom___type1; if tom_generate_is_fun_sym_BQVariable(any(tomMatch16_132).( BQTerm )) { var tomMatch16_147  TomName  = tom_generate_get_slot_BQVariable_AstName(tomMatch16_132); _ = tomMatch16_147; var tomMatch16_148  TomType  = tom_generate_get_slot_BQVariable_AstType(tomMatch16_132); _ = tomMatch16_148; if tom_generate_is_fun_sym_Name(any(tomMatch16_147).( TomName )) { if tom_generate_is_fun_sym_Type(any(tomMatch16_148).( TomType )) { 

			if g.getSymbolTable(moduleName).IsUsedTypeName(tom___type1) {
				g.buildEqualTermDecl(deep, tom_generate_get_slot_Name_String(tomMatch16_137), tom_generate_get_slot_Name_String(tomMatch16_147), tom___type1, tom_generate_get_slot_Type_TomType(tomMatch16_148), tom_generate_get_slot_EqualTermDecl_Expr(any(subject).( Declaration )), moduleName)
			}
			return
		;  };  };  };  };  };  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_IsSortDecl(any(subject).( Declaration )) { var tomMatch16_158  BQTerm  = tom_generate_get_slot_IsSortDecl_TermArg(any(subject).( Declaration )); _ = tomMatch16_158; if tom_generate_is_fun_sym_BQVariable(any(tomMatch16_158).( BQTerm )) { var tomMatch16_163  TomName  = tom_generate_get_slot_BQVariable_AstName(tomMatch16_158); _ = tomMatch16_163; var tomMatch16_164  TomType  = tom_generate_get_slot_BQVariable_AstType(tomMatch16_158); _ = tomMatch16_164; if tom_generate_is_fun_sym_Name(any(tomMatch16_163).( TomName )) { if tom_generate_is_fun_sym_Type(any(tomMatch16_164).( TomType )) { var tom___ty  string  = tom_generate_get_slot_Type_TomType(tomMatch16_164); _ = tom___ty; 

			if g.getSymbolTable(moduleName).IsUsedTypeName(tom___ty) {
				g.buildIsSortDecl(deep, tom_generate_get_slot_Name_String(tomMatch16_163), tom___ty, tom_generate_get_slot_IsSortDecl_Expr(any(subject).( Declaration )), moduleName)
			}
			return
		;  };  };  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_BQTermToDeclaration(any(subject).( Declaration )) { 

			g.generateBQTerm(deep, tom_generate_get_slot_BQTermToDeclaration_BqTerm(any(subject).( Declaration )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_ResolveIsFsymDecl(any(subject).( Declaration )) { var tomMatch16_178  TomName  = tom_generate_get_slot_ResolveIsFsymDecl_AstName(any(subject).( Declaration )); _ = tomMatch16_178; var tomMatch16_179  BQTerm  = tom_generate_get_slot_ResolveIsFsymDecl_Variable(any(subject).( Declaration )); _ = tomMatch16_179; if tom_generate_is_fun_sym_Name(any(tomMatch16_178).( TomName )) { var tom___tomName  string  = tom_generate_get_slot_Name_String(tomMatch16_178); _ = tom___tomName; if tom_generate_is_fun_sym_BQVariable(any(tomMatch16_179).( BQTerm )) { var tomMatch16_186  TomName  = tom_generate_get_slot_BQVariable_AstName(tomMatch16_179); _ = tomMatch16_186; var tomMatch16_187  TomType  = tom_generate_get_slot_BQVariable_AstType(tomMatch16_179); _ = tomMatch16_187; if tom_generate_is_fun_sym_Name(any(tomMatch16_186).( TomName )) { if tom_generate_is_fun_sym_Type(any(tomMatch16_187).( TomType )) { var tomMatch16_193  TargetLanguageType  = tom_generate_get_slot_Type_TlType(tomMatch16_187); _ = tomMatch16_193; if tom_generate_is_fun_sym_TLType(any(tomMatch16_193).( TargetLanguageType )) { 

			if g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(tom___tomName) {
				g.buildResolveIsFsymDecl(deep, tom___tomName, tom_generate_get_slot_Name_String(tomMatch16_186), tomMatch16_193, moduleName)
			}
			return
		;  };  };  };  };  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_ResolveGetSlotDecl(any(subject).( Declaration )) { var tomMatch16_199  TomName  = tom_generate_get_slot_ResolveGetSlotDecl_AstName(any(subject).( Declaration )); _ = tomMatch16_199; var tomMatch16_201  BQTerm  = tom_generate_get_slot_ResolveGetSlotDecl_Variable(any(subject).( Declaration )); _ = tomMatch16_201; if tom_generate_is_fun_sym_Name(any(tomMatch16_199).( TomName )) { var tom___tomName  string  = tom_generate_get_slot_Name_String(tomMatch16_199); _ = tom___tomName; if tom_generate_is_fun_sym_BQVariable(any(tomMatch16_201).( BQTerm )) { var tomMatch16_207  TomName  = tom_generate_get_slot_BQVariable_AstName(tomMatch16_201); _ = tomMatch16_207; var tomMatch16_208  TomType  = tom_generate_get_slot_BQVariable_AstType(tomMatch16_201); _ = tomMatch16_208; if tom_generate_is_fun_sym_Name(any(tomMatch16_207).( TomName )) { if tom_generate_is_fun_sym_Type(any(tomMatch16_208).( TomType )) { var tomMatch16_214  TargetLanguageType  = tom_generate_get_slot_Type_TlType(tomMatch16_208); _ = tomMatch16_214; if tom_generate_is_fun_sym_TLType(any(tomMatch16_214).( TargetLanguageType )) { 

			if g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(tom___tomName) {
				g.buildResolveGetSlotDecl(deep, tom___tomName, tom_generate_get_slot_Name_String(tomMatch16_207), tomMatch16_214, tom_generate_get_slot_ResolveGetSlotDecl_SlotName(any(subject).( Declaration )), moduleName)
			}
			return
		;  };  };  };  };  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_ResolveMakeDecl(any(subject).( Declaration )) { var tomMatch16_220  TomName  = tom_generate_get_slot_ResolveMakeDecl_AstName(any(subject).( Declaration )); _ = tomMatch16_220; if tom_generate_is_fun_sym_Name(any(tomMatch16_220).( TomName )) { var tom___opname  string  = tom_generate_get_slot_Name_String(tomMatch16_220); _ = tom___opname; 

			if g.getSymbolTable(moduleName).IsUsedSymbolConstructorName(tom___opname) {
				g.genResolveDeclMake(g.declName("tom_make_"), tom___opname, tom_generate_get_slot_ResolveMakeDecl_AstType(any(subject).( Declaration )), tom_generate_get_slot_ResolveMakeDecl_Args(any(subject).( Declaration )), moduleName)
			}
			return
		;  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_ResolveClassDecl(any(subject).( Declaration )) { 

			g.buildResolveClass(tom_generate_get_slot_ResolveClassDecl_WithName(any(subject).( Declaration )), tom_generate_get_slot_ResolveClassDecl_ToName(any(subject).( Declaration )), tom_generate_get_slot_ResolveClassDecl_Extends(any(subject).( Declaration )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_ResolveInverseLinksDecl(any(subject).( Declaration )) { 

			g.buildResolveInverseLinks(deep, tom_generate_get_slot_ResolveInverseLinksDecl_FileFrom(any(subject).( Declaration )), tom_generate_get_slot_ResolveInverseLinksDecl_FileTo(any(subject).( Declaration )), tom_generate_get_slot_ResolveInverseLinksDecl_ResolveNameList(any(subject).( Declaration )), moduleName)
			return
		;  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_ReferenceClass(any(subject).( Declaration )) { var tomMatch16_242  TomName  = tom_generate_get_slot_ReferenceClass_RefName(any(subject).( Declaration )); _ = tomMatch16_242; if tom_generate_is_fun_sym_Name(any(tomMatch16_242).( TomName )) { 

			g.buildReferenceClass(deep, tom_generate_get_slot_Name_String(tomMatch16_242), tom_generate_get_slot_ReferenceClass_Fields(any(subject).( Declaration )), moduleName)
			return
		;  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_GetHeadDecl(any(subject).( Declaration )) { var tomMatch16_250  TomName  = tom_generate_get_slot_GetHeadDecl_Opname(any(subject).( Declaration )); _ = tomMatch16_250; var tomMatch16_251  TomType  = tom_generate_get_slot_GetHeadDecl_Codomain(any(subject).( Declaration )); _ = tomMatch16_251; var tomMatch16_252  BQTerm  = tom_generate_get_slot_GetHeadDecl_Variable(any(subject).( Declaration )); _ = tomMatch16_252; if tom_generate_is_fun_sym_Name(any(tomMatch16_250).( TomName )) { var tom___opname  string  = tom_generate_get_slot_Name_String(tomMatch16_250); _ = tom___opname; if tom_generate_is_fun_sym_Type(any(tomMatch16_251).( TomType )) { if tom_generate_is_fun_sym_BQVariable(any(tomMatch16_252).( BQTerm )) { var tomMatch16_262  TomName  = tom_generate_get_slot_BQVariable_AstName(tomMatch16_252); _ = tomMatch16_262; var tomMatch16_263  TomType  = tom_generate_get_slot_BQVariable_AstType(tomMatch16_252); _ = tomMatch16_263; if tom_generate_is_fun_sym_Name(any(tomMatch16_262).( TomName )) { if tom_generate_is_fun_sym_Type(any(tomMatch16_263).( TomType )) { var tomMatch16_270  TargetLanguageType  = tom_generate_get_slot_Type_TlType(tomMatch16_263); _ = tomMatch16_270; if tom_generate_is_fun_sym_TLType(any(tomMatch16_270).( TargetLanguageType )) { 

			if g.getSymbolTable(moduleName).IsUsedSymbolConstructorName(tom___opname) ||
				g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(tom___opname) {
				g.buildGetHeadDecl(deep, tomMatch16_250, tom_generate_get_slot_Name_String(tomMatch16_262), tom_generate_get_slot_Type_TomType(tomMatch16_263), tomMatch16_270, tom_generate_get_slot_Type_TlType(tomMatch16_251), tom_generate_get_slot_GetHeadDecl_Expr(any(subject).( Declaration )), moduleName)
			}
			return
		;  };  };  };  };  };  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_GetTailDecl(any(subject).( Declaration )) { var tomMatch16_276  TomName  = tom_generate_get_slot_GetTailDecl_Opname(any(subject).( Declaration )); _ = tomMatch16_276; var tomMatch16_277  BQTerm  = tom_generate_get_slot_GetTailDecl_Variable(any(subject).( Declaration )); _ = tomMatch16_277; if tom_generate_is_fun_sym_Name(any(tomMatch16_276).( TomName )) { var tom___opname  string  = tom_generate_get_slot_Name_String(tomMatch16_276); _ = tom___opname; if tom_generate_is_fun_sym_BQVariable(any(tomMatch16_277).( BQTerm )) { var tomMatch16_284  TomName  = tom_generate_get_slot_BQVariable_AstName(tomMatch16_277); _ = tomMatch16_284; var tomMatch16_285  TomType  = tom_generate_get_slot_BQVariable_AstType(tomMatch16_277); _ = tomMatch16_285; if tom_generate_is_fun_sym_Name(any(tomMatch16_284).( TomName )) { if tom_generate_is_fun_sym_Type(any(tomMatch16_285).( TomType )) { var tomMatch16_292  TargetLanguageType  = tom_generate_get_slot_Type_TlType(tomMatch16_285); _ = tomMatch16_292; if tom_generate_is_fun_sym_TLType(any(tomMatch16_292).( TargetLanguageType )) { 

			if g.getSymbolTable(moduleName).IsUsedSymbolConstructorName(tom___opname) ||
				g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(tom___opname) {
				g.buildGetTailDecl(deep, tomMatch16_276, tom_generate_get_slot_Name_String(tomMatch16_284), tom_generate_get_slot_Type_TomType(tomMatch16_285), tomMatch16_292, tom_generate_get_slot_GetTailDecl_Expr(any(subject).( Declaration )), moduleName)
			}
			return
		;  };  };  };  };  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_IsEmptyDecl(any(subject).( Declaration )) { var tomMatch16_298  TomName  = tom_generate_get_slot_IsEmptyDecl_Opname(any(subject).( Declaration )); _ = tomMatch16_298; var tomMatch16_299  BQTerm  = tom_generate_get_slot_IsEmptyDecl_Variable(any(subject).( Declaration )); _ = tomMatch16_299; if tom_generate_is_fun_sym_Name(any(tomMatch16_298).( TomName )) { var tom___opname  string  = tom_generate_get_slot_Name_String(tomMatch16_298); _ = tom___opname; if tom_generate_is_fun_sym_BQVariable(any(tomMatch16_299).( BQTerm )) { var tomMatch16_306  TomName  = tom_generate_get_slot_BQVariable_AstName(tomMatch16_299); _ = tomMatch16_306; var tomMatch16_307  TomType  = tom_generate_get_slot_BQVariable_AstType(tomMatch16_299); _ = tomMatch16_307; if tom_generate_is_fun_sym_Name(any(tomMatch16_306).( TomName )) { if tom_generate_is_fun_sym_Type(any(tomMatch16_307).( TomType )) { var tomMatch16_314  TargetLanguageType  = tom_generate_get_slot_Type_TlType(tomMatch16_307); _ = tomMatch16_314; if tom_generate_is_fun_sym_TLType(any(tomMatch16_314).( TargetLanguageType )) { 

			if g.getSymbolTable(moduleName).IsUsedSymbolConstructorName(tom___opname) ||
				g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(tom___opname) {
				g.buildIsEmptyDecl(deep, tomMatch16_298, tom_generate_get_slot_Name_String(tomMatch16_306), tom_generate_get_slot_Type_TomType(tomMatch16_307), tomMatch16_314, tom_generate_get_slot_IsEmptyDecl_Expr(any(subject).( Declaration )), moduleName)
			}
			return
		;  };  };  };  };  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_MakeEmptyList(any(subject).( Declaration )) { var tomMatch16_320  TomName  = tom_generate_get_slot_MakeEmptyList_AstName(any(subject).( Declaration )); _ = tomMatch16_320; if tom_generate_is_fun_sym_Name(any(tomMatch16_320).( TomName )) { var tom___opname  string  = tom_generate_get_slot_Name_String(tomMatch16_320); _ = tom___opname; 

			returnType := tools.GetSymbolCodomain(g.getSymbolFromName(tom___opname))
			if g.getSymbolTable(moduleName).IsUsedSymbolConstructorName(tom___opname) ||
				g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(tom___opname) {
				g.genDeclMake(g.declName("tom_empty_list_"), tom___opname, returnType, tom_generate_empty_list_concBQTerm(), tom_generate_get_slot_MakeEmptyList_Instr(any(subject).( Declaration )), moduleName)
			}
			return
		;  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_MakeAddList(any(subject).( Declaration )) { var tomMatch16_329  TomName  = tom_generate_get_slot_MakeAddList_AstName(any(subject).( Declaration )); _ = tomMatch16_329; var tomMatch16_330  BQTerm  = tom_generate_get_slot_MakeAddList_VarElt(any(subject).( Declaration )); _ = tomMatch16_330; var tomMatch16_331  BQTerm  = tom_generate_get_slot_MakeAddList_VarList(any(subject).( Declaration )); _ = tomMatch16_331; if tom_generate_is_fun_sym_Name(any(tomMatch16_329).( TomName )) { var tom___opname  string  = tom_generate_get_slot_Name_String(tomMatch16_329); _ = tom___opname; if tom_generate_is_fun_sym_BQVariable(any(tomMatch16_330).( BQTerm )) { var tomMatch16_339  TomType  = tom_generate_get_slot_BQVariable_AstType(tomMatch16_330); _ = tomMatch16_339; if tom_generate_is_fun_sym_Type(any(tomMatch16_339).( TomType )) { if tom_generate_is_fun_sym_TLType(any(tom_generate_get_slot_Type_TlType(tomMatch16_339)).( TargetLanguageType )) { if tom_generate_is_fun_sym_BQVariable(any(tomMatch16_331).( BQTerm )) { var tomMatch16_347  TomType  = tom_generate_get_slot_BQVariable_AstType(tomMatch16_331); _ = tomMatch16_347; if tom_generate_is_fun_sym_Type(any(tomMatch16_347).( TomType )) { if tom_generate_is_fun_sym_TLType(any(tom_generate_get_slot_Type_TlType(tomMatch16_347)).( TargetLanguageType )) { 

			returnType := tomMatch16_347
			if g.getSymbolTable(moduleName).IsUsedSymbolConstructorName(tom___opname) ||
				g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(tom___opname) {
				g.genDeclMake(g.declName("tom_cons_list_"), tom___opname, returnType, tom_generate_cons_list_concBQTerm(tomMatch16_330,tom_generate_cons_list_concBQTerm(tomMatch16_331,tom_generate_empty_list_concBQTerm())), tom_generate_get_slot_MakeAddList_Instr(any(subject).( Declaration )), moduleName)
			}
			return
		;  };  };  };  };  };  };  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_GetElementDecl(any(subject).( Declaration )) { var tomMatch16_356  TomName  = tom_generate_get_slot_GetElementDecl_Opname(any(subject).( Declaration )); _ = tomMatch16_356; var tomMatch16_357  BQTerm  = tom_generate_get_slot_GetElementDecl_Variable(any(subject).( Declaration )); _ = tomMatch16_357; var tomMatch16_358  BQTerm  = tom_generate_get_slot_GetElementDecl_Index(any(subject).( Declaration )); _ = tomMatch16_358; if tom_generate_is_fun_sym_Name(any(tomMatch16_356).( TomName )) { if tom_generate_is_fun_sym_BQVariable(any(tomMatch16_357).( BQTerm )) { var tomMatch16_365  TomName  = tom_generate_get_slot_BQVariable_AstName(tomMatch16_357); _ = tomMatch16_365; var tomMatch16_366  TomType  = tom_generate_get_slot_BQVariable_AstType(tomMatch16_357); _ = tomMatch16_366; if tom_generate_is_fun_sym_Name(any(tomMatch16_365).( TomName )) { if tom_generate_is_fun_sym_Type(any(tomMatch16_366).( TomType )) { if tom_generate_is_fun_sym_TLType(any(tom_generate_get_slot_Type_TlType(tomMatch16_366)).( TargetLanguageType )) { if tom_generate_is_fun_sym_BQVariable(any(tomMatch16_358).( BQTerm )) { var tomMatch16_378  TomName  = tom_generate_get_slot_BQVariable_AstName(tomMatch16_358); _ = tomMatch16_378; if tom_generate_is_fun_sym_Name(any(tomMatch16_378).( TomName )) { 

			if g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(tom_generate_get_slot_Name_String(tomMatch16_356)) {
				g.buildGetElementDecl(deep, tomMatch16_356, tom_generate_get_slot_Name_String(tomMatch16_365), tom_generate_get_slot_Name_String(tomMatch16_378), tom_generate_get_slot_Type_TomType(tomMatch16_366), tom_generate_get_slot_GetElementDecl_Expr(any(subject).( Declaration )), moduleName)
			}
			return
		;  };  };  };  };  };  };  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_GetSizeDecl(any(subject).( Declaration )) { var tomMatch16_385  TomName  = tom_generate_get_slot_GetSizeDecl_Opname(any(subject).( Declaration )); _ = tomMatch16_385; var tomMatch16_386  BQTerm  = tom_generate_get_slot_GetSizeDecl_Variable(any(subject).( Declaration )); _ = tomMatch16_386; if tom_generate_is_fun_sym_Name(any(tomMatch16_385).( TomName )) { if tom_generate_is_fun_sym_BQVariable(any(tomMatch16_386).( BQTerm )) { var tomMatch16_393  TomName  = tom_generate_get_slot_BQVariable_AstName(tomMatch16_386); _ = tomMatch16_393; var tomMatch16_394  TomType  = tom_generate_get_slot_BQVariable_AstType(tomMatch16_386); _ = tomMatch16_394; if tom_generate_is_fun_sym_Name(any(tomMatch16_393).( TomName )) { if tom_generate_is_fun_sym_Type(any(tomMatch16_394).( TomType )) { if tom_generate_is_fun_sym_TLType(any(tom_generate_get_slot_Type_TlType(tomMatch16_394)).( TargetLanguageType )) { 

			if g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(tom_generate_get_slot_Name_String(tomMatch16_385)) {
				g.buildGetSizeDecl(deep, tomMatch16_385, tom_generate_get_slot_Name_String(tomMatch16_393), tom_generate_get_slot_Type_TomType(tomMatch16_394), tom_generate_get_slot_GetSizeDecl_Expr(any(subject).( Declaration )), moduleName)
			}
			return
		;  };  };  };  };  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_MakeEmptyArray(any(subject).( Declaration )) { var tomMatch16_407  TomName  = tom_generate_get_slot_MakeEmptyArray_AstName(any(subject).( Declaration )); _ = tomMatch16_407; var tomMatch16_408  BQTerm  = tom_generate_get_slot_MakeEmptyArray_VarSize(any(subject).( Declaration )); _ = tomMatch16_408; if tom_generate_is_fun_sym_Name(any(tomMatch16_407).( TomName )) { var tom___opname  string  = tom_generate_get_slot_Name_String(tomMatch16_407); _ = tom___opname; if tom_generate_is_fun_sym_BQVariable(any(tomMatch16_408).( BQTerm )) { 

			returnType := tools.GetSymbolCodomain(g.getSymbolFromName(tom___opname))
			intType := g.getSymbolTable(moduleName).GetIntType()
			newVar := tom_generate_make_BQVariable(tom_generate_get_slot_BQVariable_Options(tomMatch16_408),tom_generate_get_slot_BQVariable_AstName(tomMatch16_408),intType)
			if g.getSymbolTable(moduleName).IsUsedSymbolConstructorName(tom___opname) {
				g.genDeclMake(g.declName("tom_empty_array_"), tom___opname, returnType, tom_generate_cons_list_concBQTerm(newVar,tom_generate_empty_list_concBQTerm()), tom_generate_get_slot_MakeEmptyArray_Instr(any(subject).( Declaration )), moduleName)
			}
			return
		;  };  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_MakeAddArray(any(subject).( Declaration )) { var tomMatch16_421  TomName  = tom_generate_get_slot_MakeAddArray_AstName(any(subject).( Declaration )); _ = tomMatch16_421; var tomMatch16_422  BQTerm  = tom_generate_get_slot_MakeAddArray_VarElt(any(subject).( Declaration )); _ = tomMatch16_422; var tomMatch16_423  BQTerm  = tom_generate_get_slot_MakeAddArray_VarList(any(subject).( Declaration )); _ = tomMatch16_423; if tom_generate_is_fun_sym_Name(any(tomMatch16_421).( TomName )) { var tom___opname  string  = tom_generate_get_slot_Name_String(tomMatch16_421); _ = tom___opname; if tom_generate_is_fun_sym_BQVariable(any(tomMatch16_422).( BQTerm )) { var tomMatch16_431  TomType  = tom_generate_get_slot_BQVariable_AstType(tomMatch16_422); _ = tomMatch16_431; if tom_generate_is_fun_sym_Type(any(tomMatch16_431).( TomType )) { if tom_generate_is_fun_sym_TLType(any(tom_generate_get_slot_Type_TlType(tomMatch16_431)).( TargetLanguageType )) { if tom_generate_is_fun_sym_BQVariable(any(tomMatch16_423).( BQTerm )) { var tomMatch16_439  TomType  = tom_generate_get_slot_BQVariable_AstType(tomMatch16_423); _ = tomMatch16_439; if tom_generate_is_fun_sym_Type(any(tomMatch16_439).( TomType )) { if tom_generate_is_fun_sym_TLType(any(tom_generate_get_slot_Type_TlType(tomMatch16_439)).( TargetLanguageType )) { 

			returnType := tomMatch16_439
			if g.getSymbolTable(moduleName).IsUsedSymbolConstructorName(tom___opname) {
				g.genDeclMake(g.declName("tom_cons_array_"), tom___opname, returnType, tom_generate_cons_list_concBQTerm(tomMatch16_422,tom_generate_cons_list_concBQTerm(tomMatch16_423,tom_generate_empty_list_concBQTerm())), tom_generate_get_slot_MakeAddArray_Instr(any(subject).( Declaration )), moduleName)
			}
			return
		;  };  };  };  };  };  };  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_MakeDecl(any(subject).( Declaration )) { var tomMatch16_448  TomName  = tom_generate_get_slot_MakeDecl_AstName(any(subject).( Declaration )); _ = tomMatch16_448; if tom_generate_is_fun_sym_Name(any(tomMatch16_448).( TomName )) { var tom___opname  string  = tom_generate_get_slot_Name_String(tomMatch16_448); _ = tom___opname; 

			if g.getSymbolTable(moduleName).IsUsedSymbolConstructorName(tom___opname) {
				g.genDeclMake(g.declName("tom_make_"), tom___opname, tom_generate_get_slot_MakeDecl_AstType(any(subject).( Declaration )), tom_generate_get_slot_MakeDecl_Args(any(subject).( Declaration )), tom_generate_get_slot_MakeDecl_Instr(any(subject).( Declaration )), moduleName)
			}
			return
		;  };  };  };  }; { if tom_generate_is_sort_Declaration(subject) { if tom_generate_is_fun_sym_TypeTermDecl(any(subject).( Declaration )) { 

			g.generateDeclarationList(deep, tom_generate_get_slot_TypeTermDecl_Declarations(any(subject).( Declaration )), moduleName)
			return
		;  };  };  };  }; 

	
	panic(&platform.RuntimeError{Msg: "Cannot generate code for declaration: " + platform.JavaString(subject)})
}






func (g *goGenerator) generateListInclude(deep int, subject CodeList, moduleName string) {
	g.output.SetSingleLine()
	g.generateList(deep, subject, moduleName)
	g.output.UnsetSingleLine()
}




func (g *goGenerator) generateList(deep int, subject CodeList, moduleName string) {
	for !subject.IsEmptyconcCode() {
		g.generate(deep, subject.GetHeadconcCode(), moduleName)
		subject = subject.GetTailconcCode()
	}
}




func (g *goGenerator) generateOptionList(deep int, subject OptionList, moduleName string) {
	for !subject.IsEmptyconcOption() {
		g.generateOption(deep, subject.GetHeadconcOption(), moduleName)
		subject = subject.GetTailconcOption()
	}
}






func (g *goGenerator) generateInstructionList(deep int, subject InstructionList, moduleName string) {
	for !subject.IsEmptyconcInstruction() {
		g.generateInstruction(deep, subject.GetHeadconcInstruction(), moduleName)
		subject = subject.GetTailconcInstruction()
	}
}






func (g *goGenerator) generateDeclarationList(deep int, subject DeclarationList, moduleName string) {
	for !subject.IsEmptyconcDeclaration() {
		g.generateDeclaration(deep, subject.GetHeadconcDeclaration(), moduleName)
		subject = subject.GetTailconcDeclaration()
	}
}






func (g *goGenerator) generatePairNameDeclList(deep int, pairNameDeclList PairNameDeclList, moduleName string) {
	for !pairNameDeclList.IsEmptyconcPairNameDecl() {
		g.generateDeclaration(deep, pairNameDeclList.GetHeadconcPairNameDecl().GetSlotDecl(), moduleName)
		pairNameDeclList = pairNameDeclList.GetTailconcPairNameDecl()
	}
}




func (g *goGenerator) buildComment(deep int, text string) {
	g.output.Write("/* " + strings.ReplaceAll(text, "*/", "* /") + " */ ")
}












func (g *goGenerator) buildInstructionSequence(deep int, instructionList InstructionList, moduleName string) {
	g.generateInstructionList(deep, instructionList, moduleName)
}




func (g *goGenerator) buildReturn(deep int, exp BQTerm, moduleName string) {
	g.output.Write("return ")
	g.generateBQTerm(deep, exp, moduleName)
	g.output.Write("; ")
}










func (g *goGenerator) buildSymbolDecl(deep int, tomName string, moduleName string) {
	tomSymbol := g.getSymbolTable(moduleName).GetSymbolFromName(tomName)
	optionList := tomSymbol.GetOptions()
	pairNameDeclList := tomSymbol.GetPairNameDeclList()
	
	g.generateOptionList(deep, optionList, moduleName)
	
	g.generatePairNameDeclList(deep, pairNameDeclList, moduleName)
}






func (g *goGenerator) buildIsSortDecl(deep int, varName string, ty string, code Expression, moduleName string) {
	inlined := g.inlineplus
	if code.IsCode() {
		
		ocode := code.GetCode()
		ncode := strings.ReplaceAll(ocode, "{0}", varName)
		if ncode != ocode {
			inlined = true
			code = tom_generate_make_Code(ncode)
		}
	}
	if !g.inline || !code.IsCode() || !inlined {
		argType := g.getUniversalType()
		if g.getSymbolTable(moduleName).IsBuiltinType(ty) {
			argType = g.getSymbolTable(moduleName).GetBuiltinType(ty)
		}
		g.genDeclInstr(tools.GetTLType(g.getSymbolTable(moduleName).GetBooleanType()), g.declName("tom_is_sort"), ty,
			[]string{tools.GetTLType(argType), varName},
			tom_generate_make_Return(tom_generate_make_ExpressionToBQTerm(code)), deep, moduleName)
	}
}








func (g *goGenerator) buildEqualTermDecl(deep int, varname1, varname2, type1, type2 string, code Expression, moduleName string) {
	if code.IsCode() {
		ncode := strings.ReplaceAll(strings.ReplaceAll(code.GetCode(), "{0}", varname1), "{1}", varname2)
		code = tom_generate_make_Code(ncode)
	}
	g.genDeclInstr(g.goType(g.getSymbolTable(moduleName).GetBooleanType()), g.declName("tom_equal_term"), type1,
		[]string{g.sortGoType(type1, moduleName), varname1, g.sortGoType(type2, moduleName), varname2},
		tom_generate_make_Return(tom_generate_make_ExpressionToBQTerm(code)), deep, moduleName)
}








func (g *goGenerator) genDeclInstr(returnType, declName, suffix string, args []string, instr Instruction, deep int, moduleName string) {
	if g.nodeclMode {
		return
	}
	var s strings.Builder
	s.WriteString("func " + declName + "_" + suffix + "(")
	for i := 0; i < len(args); {
		s.WriteString(args[i+1] + " " + args[i])
		i += 2
		if i < len(args) {
			s.WriteString(", ")
		}
	}
	s.WriteString(") " + returnType + " { ")
	g.output.Write(s.String())
	g.generateInstruction(deep, instr, moduleName)
	g.output.Write(" }; ")
}




func (g *goGenerator) genDecl(returnType, declName, suffix string, args []string, tlCode TargetLanguage, moduleName string) {
	panic(&platform.RuntimeError{Msg: "GoGenerator.genDecl is not used"})
}




var _ = aterm.GetInstance

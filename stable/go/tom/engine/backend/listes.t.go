// tom-source-sha256: 6d9a5839d1328bddf50bda3e29ebb362a81d73e55c93c22d8a03cd4ba3e9af65

package backend









import (
	"strings"

	. "tom/engine/adt"
	"tom/engine/platform"
	"tom/engine/tools"
	"tom/library/aterm"
)

func tom_listes_equal_term_char(t1 rune, t2 rune) bool { return t1 == t2;  }; func tom_listes_is_sort_char(t rune) bool { return true;  }; func tom_listes_equal_term_String(t1 string, t2 string) bool { return t1 == t2;  }; func tom_listes_is_sort_String(t string) bool { return true;  }; func tom_listes_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_listes_is_sort_int(t int64) bool { return true;  }; func tom_listes_equal_term_boolean(t1 bool, t2 bool) bool { return t1 == t2;  }; func tom_listes_is_sort_boolean(t bool) bool { return true;  }; func tom_listes_equal_term_ATerm(t1  aterm.ATerm , t2  aterm.ATerm ) bool { return t1 == t2;  }; func tom_listes_is_sort_ATerm(t any) bool { return func() bool { _, ok := any(t).(aterm.ATerm); return ok }();  }; func tom_listes_equal_term_ATermList(l1  *aterm.ATermList , l2  *aterm.ATermList ) bool { return l1 == l2;  }; func tom_listes_is_sort_ATermList(t any) bool { return func() bool { _, ok := any(t).(*aterm.ATermList); return ok }();  }; func tom_listes_equal_term_AFun(t1  *aterm.AFun , t2  *aterm.AFun ) bool { return t1 == t2;  }; func tom_listes_is_sort_AFun(t any) bool { return func() bool { _, ok := any(t).(*aterm.AFun); return ok }();  }; func tom_listes_equal_term_CstProgram(t1  CstProgram , t2  CstProgram ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstProgram(t any) bool { return func() bool { _, ok := (t).(CstProgram); return ok }();  }; func tom_listes_equal_term_CstBlockList(t1  CstBlockList , t2  CstBlockList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstBlockList(t any) bool { return func() bool { _, ok := (t).(CstBlockList); return ok }();  }; func tom_listes_equal_term_CstBlock(t1  CstBlock , t2  CstBlock ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstBlock(t any) bool { return func() bool { _, ok := (t).(CstBlock); return ok }();  }; func tom_listes_equal_term_CstConstraintAction(t1  CstConstraintAction , t2  CstConstraintAction ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstConstraintAction(t any) bool { return func() bool { _, ok := (t).(CstConstraintAction); return ok }();  }; func tom_listes_equal_term_CstConstraintActionList(t1  CstConstraintActionList , t2  CstConstraintActionList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstConstraintActionList(t any) bool { return func() bool { _, ok := (t).(CstConstraintActionList); return ok }();  }; func tom_listes_equal_term_CstConstraint(t1  CstConstraint , t2  CstConstraint ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstConstraint(t any) bool { return func() bool { _, ok := (t).(CstConstraint); return ok }();  }; func tom_listes_equal_term_CstConstraintList(t1  CstConstraintList , t2  CstConstraintList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstConstraintList(t any) bool { return func() bool { _, ok := (t).(CstConstraintList); return ok }();  }; func tom_listes_equal_term_CstTerm(t1  CstTerm , t2  CstTerm ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstTerm(t any) bool { return func() bool { _, ok := (t).(CstTerm); return ok }();  }; func tom_listes_equal_term_CstTermList(t1  CstTermList , t2  CstTermList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstTermList(t any) bool { return func() bool { _, ok := (t).(CstTermList); return ok }();  }; func tom_listes_equal_term_CstType(t1  CstType , t2  CstType ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstType(t any) bool { return func() bool { _, ok := (t).(CstType); return ok }();  }; func tom_listes_equal_term_CstPattern(t1  CstPattern , t2  CstPattern ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstPattern(t any) bool { return func() bool { _, ok := (t).(CstPattern); return ok }();  }; func tom_listes_equal_term_CstSymbolList(t1  CstSymbolList , t2  CstSymbolList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstSymbolList(t any) bool { return func() bool { _, ok := (t).(CstSymbolList); return ok }();  }; func tom_listes_equal_term_CstSymbol(t1  CstSymbol , t2  CstSymbol ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstSymbol(t any) bool { return func() bool { _, ok := (t).(CstSymbol); return ok }();  }; func tom_listes_equal_term_CstTheory(t1  CstTheory , t2  CstTheory ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstTheory(t any) bool { return func() bool { _, ok := (t).(CstTheory); return ok }();  }; func tom_listes_equal_term_CstPatternList(t1  CstPatternList , t2  CstPatternList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstPatternList(t any) bool { return func() bool { _, ok := (t).(CstPatternList); return ok }();  }; func tom_listes_equal_term_CstPairPatternList(t1  CstPairPatternList , t2  CstPairPatternList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstPairPatternList(t any) bool { return func() bool { _, ok := (t).(CstPairPatternList); return ok }();  }; func tom_listes_equal_term_CstPairPattern(t1  CstPairPattern , t2  CstPairPattern ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstPairPattern(t any) bool { return func() bool { _, ok := (t).(CstPairPattern); return ok }();  }; func tom_listes_equal_term_CstOperatorList(t1  CstOperatorList , t2  CstOperatorList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstOperatorList(t any) bool { return func() bool { _, ok := (t).(CstOperatorList); return ok }();  }; func tom_listes_equal_term_CstOperator(t1  CstOperator , t2  CstOperator ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstOperator(t any) bool { return func() bool { _, ok := (t).(CstOperator); return ok }();  }; func tom_listes_equal_term_CstSlotList(t1  CstSlotList , t2  CstSlotList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstSlotList(t any) bool { return func() bool { _, ok := (t).(CstSlotList); return ok }();  }; func tom_listes_equal_term_CstSlot(t1  CstSlot , t2  CstSlot ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstSlot(t any) bool { return func() bool { _, ok := (t).(CstSlot); return ok }();  }; func tom_listes_equal_term_CstBQTerm(t1  CstBQTerm , t2  CstBQTerm ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstBQTerm(t any) bool { return func() bool { _, ok := (t).(CstBQTerm); return ok }();  }; func tom_listes_equal_term_CstPairSlotBQTerm(t1  CstPairSlotBQTerm , t2  CstPairSlotBQTerm ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstPairSlotBQTerm(t any) bool { return func() bool { _, ok := (t).(CstPairSlotBQTerm); return ok }();  }; func tom_listes_equal_term_CstBQTermList(t1  CstBQTermList , t2  CstBQTermList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstBQTermList(t any) bool { return func() bool { _, ok := (t).(CstBQTermList); return ok }();  }; func tom_listes_equal_term_CstPairSlotBQTermList(t1  CstPairSlotBQTermList , t2  CstPairSlotBQTermList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstPairSlotBQTermList(t any) bool { return func() bool { _, ok := (t).(CstPairSlotBQTermList); return ok }();  }; func tom_listes_equal_term_CstName(t1  CstName , t2  CstName ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstName(t any) bool { return func() bool { _, ok := (t).(CstName); return ok }();  }; func tom_listes_equal_term_CstNameList(t1  CstNameList , t2  CstNameList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstNameList(t any) bool { return func() bool { _, ok := (t).(CstNameList); return ok }();  }; func tom_listes_equal_term_CstOptionList(t1  CstOptionList , t2  CstOptionList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstOptionList(t any) bool { return func() bool { _, ok := (t).(CstOptionList); return ok }();  }; func tom_listes_equal_term_CstOption(t1  CstOption , t2  CstOption ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstOption(t any) bool { return func() bool { _, ok := (t).(CstOption); return ok }();  }; func tom_listes_equal_term_CstVisit(t1  CstVisit , t2  CstVisit ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstVisit(t any) bool { return func() bool { _, ok := (t).(CstVisit); return ok }();  }; func tom_listes_equal_term_CstVisitList(t1  CstVisitList , t2  CstVisitList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CstVisitList(t any) bool { return func() bool { _, ok := (t).(CstVisitList); return ok }();  }; func tom_listes_equal_term_TargetLanguage(t1  TargetLanguage , t2  TargetLanguage ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_TargetLanguage(t any) bool { return func() bool { _, ok := (t).(TargetLanguage); return ok }();  }; func tom_listes_equal_term_CodeList(t1  CodeList , t2  CodeList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CodeList(t any) bool { return func() bool { _, ok := (t).(CodeList); return ok }();  }; func tom_listes_equal_term_Code(t1  Code , t2  Code ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_Code(t any) bool { return func() bool { _, ok := (t).(Code); return ok }();  }; func tom_listes_equal_term_BQTermList(t1  BQTermList , t2  BQTermList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_BQTermList(t any) bool { return func() bool { _, ok := (t).(BQTermList); return ok }();  }; func tom_listes_equal_term_CompositeMember(t1  CompositeMember , t2  CompositeMember ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_CompositeMember(t any) bool { return func() bool { _, ok := (t).(CompositeMember); return ok }();  }; func tom_listes_equal_term_BQTerm(t1  BQTerm , t2  BQTerm ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_BQTerm(t any) bool { return func() bool { _, ok := (t).(BQTerm); return ok }();  }; func tom_listes_is_fun_sym_concBQTerm(t  BQTermList ) bool { return func() bool { _, ok := (t).(*ConcBQTermBQTermList); return ok }();  }; func tom_listes_empty_list_concBQTerm()  BQTermList  { return (EmptyConcBQTerm()) }; func tom_listes_cons_list_concBQTerm(e  BQTerm , l  BQTermList )  BQTermList  { return (ConsConcBQTerm(e,l)) }; func tom_listes_get_head_concBQTerm_BQTermList(l  BQTermList )  BQTerm  { return (l).GetHeadconcBQTerm();  }; func tom_listes_get_tail_concBQTerm_BQTermList(l  BQTermList )  BQTermList  { return (l).GetTailconcBQTerm();  }; func tom_listes_is_empty_concBQTerm_BQTermList(l  BQTermList ) bool { return (l).IsEmptyconcBQTerm();  }; func tom_listes_append_list_concBQTerm(l1  BQTermList , l2  BQTermList )  BQTermList  { if tom_listes_is_empty_concBQTerm_BQTermList(l1) { return l2 } else if tom_listes_is_empty_concBQTerm_BQTermList(l2) { return l1 } else if tom_listes_is_empty_concBQTerm_BQTermList(tom_listes_get_tail_concBQTerm_BQTermList(l1)) { return tom_listes_cons_list_concBQTerm(tom_listes_get_head_concBQTerm_BQTermList(l1), l2) } else { return tom_listes_cons_list_concBQTerm(tom_listes_get_head_concBQTerm_BQTermList(l1), tom_listes_append_list_concBQTerm(tom_listes_get_tail_concBQTerm_BQTermList(l1), l2)) }; }; func tom_listes_get_slice_concBQTerm(begin  BQTermList , end  BQTermList , tail  BQTermList )  BQTermList  { if tom_listes_equal_term_BQTermList(begin, end) { return tail } else if tom_listes_equal_term_BQTermList(end, tail) && (tom_listes_is_empty_concBQTerm_BQTermList(end) || tom_listes_equal_term_BQTermList(end, tom_listes_empty_list_concBQTerm())) { return begin }; return tom_listes_cons_list_concBQTerm(tom_listes_get_head_concBQTerm_BQTermList(begin), tom_listes_get_slice_concBQTerm(tom_listes_get_tail_concBQTerm_BQTermList(begin), end, tail)) }; func tom_listes_is_fun_sym_BuildEmptyList(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BuildEmptyListBQTerm); return ok }();  }; func tom_listes_get_slot_BuildEmptyList_AstName(t  BQTerm )  TomName  { return (t).(*BuildEmptyListBQTerm).AstName;  }; func tom_listes_is_fun_sym_BuildConsList(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BuildConsListBQTerm); return ok }();  }; func tom_listes_get_slot_BuildConsList_AstName(t  BQTerm )  TomName  { return (t).(*BuildConsListBQTerm).AstName;  }; func tom_listes_get_slot_BuildConsList_HeadTerm(t  BQTerm )  BQTerm  { return (t).(*BuildConsListBQTerm).HeadTerm;  }; func tom_listes_get_slot_BuildConsList_TailTerm(t  BQTerm )  BQTerm  { return (t).(*BuildConsListBQTerm).TailTerm;  }; func tom_listes_is_fun_sym_BuildAppendList(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BuildAppendListBQTerm); return ok }();  }; func tom_listes_get_slot_BuildAppendList_AstName(t  BQTerm )  TomName  { return (t).(*BuildAppendListBQTerm).AstName;  }; func tom_listes_get_slot_BuildAppendList_HeadTerm(t  BQTerm )  BQTerm  { return (t).(*BuildAppendListBQTerm).HeadTerm;  }; func tom_listes_get_slot_BuildAppendList_TailTerm(t  BQTerm )  BQTerm  { return (t).(*BuildAppendListBQTerm).TailTerm;  }; func tom_listes_is_fun_sym_BuildEmptyArray(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BuildEmptyArrayBQTerm); return ok }();  }; func tom_listes_get_slot_BuildEmptyArray_AstName(t  BQTerm )  TomName  { return (t).(*BuildEmptyArrayBQTerm).AstName;  }; func tom_listes_get_slot_BuildEmptyArray_Size(t  BQTerm )  BQTerm  { return (t).(*BuildEmptyArrayBQTerm).Size;  }; func tom_listes_is_fun_sym_BuildConsArray(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BuildConsArrayBQTerm); return ok }();  }; func tom_listes_get_slot_BuildConsArray_AstName(t  BQTerm )  TomName  { return (t).(*BuildConsArrayBQTerm).AstName;  }; func tom_listes_get_slot_BuildConsArray_HeadTerm(t  BQTerm )  BQTerm  { return (t).(*BuildConsArrayBQTerm).HeadTerm;  }; func tom_listes_get_slot_BuildConsArray_TailTerm(t  BQTerm )  BQTerm  { return (t).(*BuildConsArrayBQTerm).TailTerm;  }; func tom_listes_is_fun_sym_BuildAppendArray(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BuildAppendArrayBQTerm); return ok }();  }; func tom_listes_get_slot_BuildAppendArray_AstName(t  BQTerm )  TomName  { return (t).(*BuildAppendArrayBQTerm).AstName;  }; func tom_listes_get_slot_BuildAppendArray_HeadTerm(t  BQTerm )  BQTerm  { return (t).(*BuildAppendArrayBQTerm).HeadTerm;  }; func tom_listes_get_slot_BuildAppendArray_TailTerm(t  BQTerm )  BQTerm  { return (t).(*BuildAppendArrayBQTerm).TailTerm;  }; func tom_listes_make_ExpressionToBQTerm(Exp  Expression )  BQTerm  { return (MakeExpressionToBQTerm(Exp)) }; func tom_listes_equal_term_Symbol(t1  Symbol , t2  Symbol ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_Symbol(t any) bool { return func() bool { _, ok := (t).(Symbol); return ok }();  }; func tom_listes_equal_term_Variable(t1  Variable , t2  Variable ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_Variable(t any) bool { return func() bool { _, ok := (t).(Variable); return ok }();  }; func tom_listes_equal_term_AbsTerm(t1  AbsTerm , t2  AbsTerm ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_AbsTerm(t any) bool { return func() bool { _, ok := (t).(AbsTerm); return ok }();  }; func tom_listes_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_listes_equal_term_Expr(t1  Expr , t2  Expr ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_Expr(t any) bool { return func() bool { _, ok := (t).(Expr); return ok }();  }; func tom_listes_equal_term_Instr(t1  Instr , t2  Instr ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_Instr(t any) bool { return func() bool { _, ok := (t).(Instr); return ok }();  }; func tom_listes_equal_term_InstrList(t1  InstrList , t2  InstrList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_InstrList(t any) bool { return func() bool { _, ok := (t).(InstrList); return ok }();  }; func tom_listes_equal_term_Substitution(t1  Substitution , t2  Substitution ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_Substitution(t any) bool { return func() bool { _, ok := (t).(Substitution); return ok }();  }; func tom_listes_equal_term_SubstitutionList(t1  SubstitutionList , t2  SubstitutionList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_SubstitutionList(t any) bool { return func() bool { _, ok := (t).(SubstitutionList); return ok }();  }; func tom_listes_equal_term_Environment(t1  Environment , t2  Environment ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_Environment(t any) bool { return func() bool { _, ok := (t).(Environment); return ok }();  }; func tom_listes_equal_term_Seq(t1  Seq , t2  Seq ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_Seq(t any) bool { return func() bool { _, ok := (t).(Seq); return ok }();  }; func tom_listes_equal_term_TermList(t1  TermList , t2  TermList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_TermList(t any) bool { return func() bool { _, ok := (t).(TermList); return ok }();  }; func tom_listes_equal_term_ExprList(t1  ExprList , t2  ExprList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_ExprList(t any) bool { return func() bool { _, ok := (t).(ExprList); return ok }();  }; func tom_listes_equal_term_Deriv(t1  Deriv , t2  Deriv ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_Deriv(t any) bool { return func() bool { _, ok := (t).(Deriv); return ok }();  }; func tom_listes_equal_term_DerivTree(t1  DerivTree , t2  DerivTree ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_DerivTree(t any) bool { return func() bool { _, ok := (t).(DerivTree); return ok }();  }; func tom_listes_equal_term_ElementaryTheory(t1  ElementaryTheory , t2  ElementaryTheory ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_ElementaryTheory(t any) bool { return func() bool { _, ok := (t).(ElementaryTheory); return ok }();  }; func tom_listes_equal_term_Theory(t1  Theory , t2  Theory ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_Theory(t any) bool { return func() bool { _, ok := (t).(Theory); return ok }();  }; func tom_listes_equal_term_Constraint(t1  Constraint , t2  Constraint ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_Constraint(t any) bool { return func() bool { _, ok := (t).(Constraint); return ok }();  }; func tom_listes_equal_term_NumericConstraintType(t1  NumericConstraintType , t2  NumericConstraintType ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_NumericConstraintType(t any) bool { return func() bool { _, ok := (t).(NumericConstraintType); return ok }();  }; func tom_listes_equal_term_ConstraintList(t1  ConstraintList , t2  ConstraintList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_ConstraintList(t any) bool { return func() bool { _, ok := (t).(ConstraintList); return ok }();  }; func tom_listes_equal_term_Declaration(t1  Declaration , t2  Declaration ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_Declaration(t any) bool { return func() bool { _, ok := (t).(Declaration); return ok }();  }; func tom_listes_equal_term_DeclarationList(t1  DeclarationList , t2  DeclarationList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_DeclarationList(t any) bool { return func() bool { _, ok := (t).(DeclarationList); return ok }();  }; func tom_listes_equal_term_Expression(t1  Expression , t2  Expression ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_Expression(t any) bool { return func() bool { _, ok := (t).(Expression); return ok }();  }; func tom_listes_make_Code(Code  string )  Expression  { return (MakeCode(Code)) }; func tom_listes_equal_term_Instruction(t1  Instruction , t2  Instruction ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_Instruction(t any) bool { return func() bool { _, ok := (t).(Instruction); return ok }();  }; func tom_listes_equal_term_RefClassTracelinkInstruction(t1  RefClassTracelinkInstruction , t2  RefClassTracelinkInstruction ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_RefClassTracelinkInstruction(t any) bool { return func() bool { _, ok := (t).(RefClassTracelinkInstruction); return ok }();  }; func tom_listes_equal_term_RefClassTracelinkInstructionList(t1  RefClassTracelinkInstructionList , t2  RefClassTracelinkInstructionList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_RefClassTracelinkInstructionList(t any) bool { return func() bool { _, ok := (t).(RefClassTracelinkInstructionList); return ok }();  }; func tom_listes_equal_term_ConstraintInstruction(t1  ConstraintInstruction , t2  ConstraintInstruction ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_ConstraintInstruction(t any) bool { return func() bool { _, ok := (t).(ConstraintInstruction); return ok }();  }; func tom_listes_equal_term_RuleInstruction(t1  RuleInstruction , t2  RuleInstruction ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_RuleInstruction(t any) bool { return func() bool { _, ok := (t).(RuleInstruction); return ok }();  }; func tom_listes_equal_term_RuleInstructionList(t1  RuleInstructionList , t2  RuleInstructionList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_RuleInstructionList(t any) bool { return func() bool { _, ok := (t).(RuleInstructionList); return ok }();  }; func tom_listes_equal_term_ConstraintInstructionList(t1  ConstraintInstructionList , t2  ConstraintInstructionList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_ConstraintInstructionList(t any) bool { return func() bool { _, ok := (t).(ConstraintInstructionList); return ok }();  }; func tom_listes_equal_term_InstructionList(t1  InstructionList , t2  InstructionList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_InstructionList(t any) bool { return func() bool { _, ok := (t).(InstructionList); return ok }();  }; func tom_listes_make_Return(Kid1  BQTerm )  Instruction  { return (MakeReturn(Kid1)) }; func tom_listes_equal_term_TomName(t1  TomName , t2  TomName ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_TomName(t any) bool { return func() bool { _, ok := (t).(TomName); return ok }();  }; func tom_listes_equal_term_TomNumber(t1  TomNumber , t2  TomNumber ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_TomNumber(t any) bool { return func() bool { _, ok := (t).(TomNumber); return ok }();  }; func tom_listes_equal_term_TomNumberList(t1  TomNumberList , t2  TomNumberList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_TomNumberList(t any) bool { return func() bool { _, ok := (t).(TomNumberList); return ok }();  }; func tom_listes_equal_term_TomNameList(t1  TomNameList , t2  TomNameList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_TomNameList(t any) bool { return func() bool { _, ok := (t).(TomNameList); return ok }();  }; func tom_listes_is_fun_sym_Name(t  TomName ) bool { return func() bool { _, ok := (t).(*NameTomName); return ok }();  }; func tom_listes_get_slot_Name_String(t  TomName )  string  { return (t).(*NameTomName).String_;  }; func tom_listes_is_fun_sym_EmptyName(t  TomName ) bool { return func() bool { _, ok := (t).(*EmptyNameTomName); return ok }();  }; func tom_listes_equal_term_Option(t1  Option , t2  Option ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_Option(t any) bool { return func() bool { _, ok := (t).(Option); return ok }();  }; func tom_listes_equal_term_OptionList(t1  OptionList , t2  OptionList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_OptionList(t any) bool { return func() bool { _, ok := (t).(OptionList); return ok }();  }; func tom_listes_equal_term_TextPosition(t1  TextPosition , t2  TextPosition ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_TextPosition(t any) bool { return func() bool { _, ok := (t).(TextPosition); return ok }();  }; func tom_listes_equal_term_TomVisit(t1  TomVisit , t2  TomVisit ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_TomVisit(t any) bool { return func() bool { _, ok := (t).(TomVisit); return ok }();  }; func tom_listes_equal_term_ResolveStratElement(t1  ResolveStratElement , t2  ResolveStratElement ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_ResolveStratElement(t any) bool { return func() bool { _, ok := (t).(ResolveStratElement); return ok }();  }; func tom_listes_equal_term_ResolveStratElementList(t1  ResolveStratElementList , t2  ResolveStratElementList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_ResolveStratElementList(t any) bool { return func() bool { _, ok := (t).(ResolveStratElementList); return ok }();  }; func tom_listes_equal_term_ResolveStratBlock(t1  ResolveStratBlock , t2  ResolveStratBlock ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_ResolveStratBlock(t any) bool { return func() bool { _, ok := (t).(ResolveStratBlock); return ok }();  }; func tom_listes_equal_term_ResolveStratBlockList(t1  ResolveStratBlockList , t2  ResolveStratBlockList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_ResolveStratBlockList(t any) bool { return func() bool { _, ok := (t).(ResolveStratBlockList); return ok }();  }; func tom_listes_equal_term_ElementaryTransformation(t1  ElementaryTransformation , t2  ElementaryTransformation ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_ElementaryTransformation(t any) bool { return func() bool { _, ok := (t).(ElementaryTransformation); return ok }();  }; func tom_listes_equal_term_ElementaryTransformationList(t1  ElementaryTransformationList , t2  ElementaryTransformationList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_ElementaryTransformationList(t any) bool { return func() bool { _, ok := (t).(ElementaryTransformationList); return ok }();  }; func tom_listes_equal_term_TomSymbol(t1  TomSymbol , t2  TomSymbol ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_TomSymbol(t any) bool { return func() bool { _, ok := (t).(TomSymbol); return ok }();  }; func tom_listes_equal_term_TomSymbolTable(t1  TomSymbolTable , t2  TomSymbolTable ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_TomSymbolTable(t any) bool { return func() bool { _, ok := (t).(TomSymbolTable); return ok }();  }; func tom_listes_equal_term_TomEntry(t1  TomEntry , t2  TomEntry ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_TomEntry(t any) bool { return func() bool { _, ok := (t).(TomEntry); return ok }();  }; func tom_listes_equal_term_TomStructureTable(t1  TomStructureTable , t2  TomStructureTable ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_TomStructureTable(t any) bool { return func() bool { _, ok := (t).(TomStructureTable); return ok }();  }; func tom_listes_equal_term_TomVisitList(t1  TomVisitList , t2  TomVisitList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_TomVisitList(t any) bool { return func() bool { _, ok := (t).(TomVisitList); return ok }();  }; func tom_listes_equal_term_TomEntryList(t1  TomEntryList , t2  TomEntryList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_TomEntryList(t any) bool { return func() bool { _, ok := (t).(TomEntryList); return ok }();  }; func tom_listes_equal_term_TomSymbolList(t1  TomSymbolList , t2  TomSymbolList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_TomSymbolList(t any) bool { return func() bool { _, ok := (t).(TomSymbolList); return ok }();  }; func tom_listes_equal_term_KeyEntry(t1  KeyEntry , t2  KeyEntry ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_KeyEntry(t any) bool { return func() bool { _, ok := (t).(KeyEntry); return ok }();  }; func tom_listes_equal_term_TransfoStratInfo(t1  TransfoStratInfo , t2  TransfoStratInfo ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_TransfoStratInfo(t any) bool { return func() bool { _, ok := (t).(TransfoStratInfo); return ok }();  }; func tom_listes_equal_term_Slot(t1  Slot , t2  Slot ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_Slot(t any) bool { return func() bool { _, ok := (t).(Slot); return ok }();  }; func tom_listes_equal_term_SlotList(t1  SlotList , t2  SlotList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_SlotList(t any) bool { return func() bool { _, ok := (t).(SlotList); return ok }();  }; func tom_listes_equal_term_BQSlot(t1  BQSlot , t2  BQSlot ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_BQSlot(t any) bool { return func() bool { _, ok := (t).(BQSlot); return ok }();  }; func tom_listes_equal_term_BQSlotList(t1  BQSlotList , t2  BQSlotList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_BQSlotList(t any) bool { return func() bool { _, ok := (t).(BQSlotList); return ok }();  }; func tom_listes_equal_term_PairNameDecl(t1  PairNameDecl , t2  PairNameDecl ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_PairNameDecl(t any) bool { return func() bool { _, ok := (t).(PairNameDecl); return ok }();  }; func tom_listes_equal_term_PairNameDeclList(t1  PairNameDeclList , t2  PairNameDeclList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_PairNameDeclList(t any) bool { return func() bool { _, ok := (t).(PairNameDeclList); return ok }();  }; func tom_listes_equal_term_TomTerm(t1  TomTerm , t2  TomTerm ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_TomTerm(t any) bool { return func() bool { _, ok := (t).(TomTerm); return ok }();  }; func tom_listes_equal_term_TomList(t1  TomList , t2  TomList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_TomList(t any) bool { return func() bool { _, ok := (t).(TomList); return ok }();  }; func tom_listes_equal_term_TomType(t1  TomType , t2  TomType ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_TomType(t any) bool { return func() bool { _, ok := (t).(TomType); return ok }();  }; func tom_listes_equal_term_TypeOption(t1  TypeOption , t2  TypeOption ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_TypeOption(t any) bool { return func() bool { _, ok := (t).(TypeOption); return ok }();  }; func tom_listes_equal_term_TypeOptionList(t1  TypeOptionList , t2  TypeOptionList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_TypeOptionList(t any) bool { return func() bool { _, ok := (t).(TypeOptionList); return ok }();  }; func tom_listes_equal_term_TargetLanguageType(t1  TargetLanguageType , t2  TargetLanguageType ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_TargetLanguageType(t any) bool { return func() bool { _, ok := (t).(TargetLanguageType); return ok }();  }; func tom_listes_equal_term_TomTypeList(t1  TomTypeList , t2  TomTypeList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_TomTypeList(t any) bool { return func() bool { _, ok := (t).(TomTypeList); return ok }();  }; func tom_listes_equal_term_Info(t1  Info , t2  Info ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_Info(t any) bool { return func() bool { _, ok := (t).(Info); return ok }();  }; func tom_listes_equal_term_TypeConstraint(t1  TypeConstraint , t2  TypeConstraint ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_TypeConstraint(t any) bool { return func() bool { _, ok := (t).(TypeConstraint); return ok }();  }; func tom_listes_equal_term_TypeConstraintList(t1  TypeConstraintList , t2  TypeConstraintList ) bool { return (t1).Equivalent(t2);  }; func tom_listes_is_sort_TypeConstraintList(t any) bool { return func() bool { _, ok := (t).(TypeConstraintList); return ok }();  }; 





const genericBadCase = "GenericGenerator: bad case: "






func javaTomType(ty TomType) string {
	s, ok := tools.GetTomType(ty)
	if !ok {
		return "null"
	}
	return s
}




func (g *goGenerator) buildExpIsEmptyArray(deep int, opNameAST TomName, ty TomType, expIndex, expArray BQTerm, moduleName string) {
	deep = 0 
	g.generateBQTerm(deep, expIndex, moduleName)
	g.output.Write(" >= ")
	; { _ = opNameAST; { if tom_listes_is_sort_TomName(opNameAST) { if tom_listes_is_fun_sym_EmptyName(any(opNameAST).( TomName )) { 

			panic(&platform.RuntimeError{Msg: genericBadCase + platform.JavaString(opNameAST)})
		;  };  };  };  }; 

	opName := opNameAST.GetString()
	sType := javaTomType(ty)
	template, hasTemplate := g.getSymbolTable(moduleName).GetGetSizeArray(opName)
	if !g.instantiateTemplate(deep, template, hasTemplate, opName, tom_listes_cons_list_concBQTerm(expArray,tom_listes_empty_list_concBQTerm()), moduleName) {
		g.output.Write(g.declName("tom_get_size_"+opName+"_"+sType) + "(")
		g.generateBQTerm(deep, expArray, moduleName)
		g.output.Write(")")
	}
}




func (g *goGenerator) buildExpGetHead(deep int, opName string, domain, codomain TomType, v BQTerm, moduleName string) {
	deep = 0 
	template, hasTemplate := g.getSymbolTable(moduleName).GetGetHead(opName)
	if !g.instantiateTemplate(deep, template, hasTemplate, opName, tom_listes_cons_list_concBQTerm(v,tom_listes_empty_list_concBQTerm()), moduleName) {
		g.output.Write(g.declName("tom_get_head_"+opName+"_"+javaTomType(domain)) + "(")
		g.generateBQTerm(deep, v, moduleName)
		g.output.Write(")")
	}
}




func (g *goGenerator) buildExpGetTail(deep int, opName string, ty TomType, v BQTerm, moduleName string) {
	deep = 0 
	template, hasTemplate := g.getSymbolTable(moduleName).GetGetTail(opName)
	if !g.instantiateTemplate(deep, template, hasTemplate, opName, tom_listes_cons_list_concBQTerm(v,tom_listes_empty_list_concBQTerm()), moduleName) {
		g.output.Write(g.declName("tom_get_tail_"+opName+"_"+javaTomType(ty)) + "(")
		g.generateBQTerm(deep, v, moduleName)
		g.output.Write(")")
	}
}




func (g *goGenerator) buildExpIsEmptyList(deep int, opName string, ty TomType, v BQTerm, moduleName string) {
	deep = 0 
	template, hasTemplate := g.getSymbolTable(moduleName).GetIsEmptyList(opName)
	if !g.instantiateTemplate(deep, template, hasTemplate, opName, tom_listes_cons_list_concBQTerm(v,tom_listes_empty_list_concBQTerm()), moduleName) {
		g.output.Write(g.declName("tom_is_empty_"+opName+"_"+javaTomType(ty)) + "(")
		g.generateBQTerm(deep, v, moduleName)
		g.output.Write(")")
	}
}




func (g *goGenerator) buildExpGetSize(deep int, opNameAST TomName, ty TomType, v BQTerm, moduleName string) {
	deep = 0 
	; { _ = opNameAST; { if tom_listes_is_sort_TomName(opNameAST) { if tom_listes_is_fun_sym_EmptyName(any(opNameAST).( TomName )) { 

			panic(&platform.RuntimeError{Msg: genericBadCase + platform.JavaString(opNameAST)})
		;  };  };  };  }; 

	opName := opNameAST.GetString()
	sType := javaTomType(ty)
	template, hasTemplate := g.getSymbolTable(moduleName).GetGetSizeArray(opName)
	if !g.instantiateTemplate(deep, template, hasTemplate, opName, tom_listes_cons_list_concBQTerm(v,tom_listes_empty_list_concBQTerm()), moduleName) {
		g.output.Write(g.declName("tom_get_size_"+opName+"_"+sType) + "(")
		g.generateBQTerm(deep, v, moduleName)
		g.output.Write(")")
	}
}




func (g *goGenerator) buildExpGetSliceList(deep int, name string, varBegin, varEnd, tail BQTerm, moduleName string) {
	deep = 0 
	g.output.Write(g.declName("tom_get_slice_"+name) + "(")
	g.generateBQTerm(deep, varBegin, moduleName)
	g.output.Write(",")
	g.generateBQTerm(deep, varEnd, moduleName)
	g.output.Write(",")
	g.generateBQTerm(deep, tail, moduleName)
	g.output.Write(")")
}




func (g *goGenerator) buildExpGetSliceArray(deep int, name string, varArray, varBegin, expEnd BQTerm, moduleName string) {
	deep = 0 
	g.output.Write(g.declName("tom_get_slice_"+name) + "(")
	g.generateBQTerm(deep, varArray, moduleName)
	g.output.Write(",")
	g.generateBQTerm(deep, varBegin, moduleName)
	g.output.Write(",")
	g.generateBQTerm(deep, expEnd, moduleName)
	g.output.Write(")")
}




func (g *goGenerator) buildAssignArray(deep int, v BQTerm, optionList OptionList, index BQTerm, exp Expression, moduleName string) {
	g.generateArray(deep, v, index, moduleName)
	g.output.Write(" = ")
	g.generateExpression(deep, exp, moduleName)
	g.output.Write("; ")
}




func (g *goGenerator) buildGetHeadDecl(deep int, opNameAST TomName, varName, suffix string, domain, codomain TargetLanguageType, code Expression, moduleName string) {
	opname := opNameAST.GetString()
	inlined := g.inlineplus
	if code.IsCode() {
		
		ocode := code.GetCode()
		ncode := strings.ReplaceAll(ocode, "{0}", varName)
		if ncode != ocode {
			inlined = true
			code = tom_listes_make_Code(ncode)
		}
	}
	if !g.inline || !code.IsCode() || !inlined {
		returnType := ""
		argType := ""
		functionName := g.declName("tom_get_head_" + opname)
		; { _ = opNameAST; { if tom_listes_is_sort_TomName(opNameAST) { if tom_listes_is_fun_sym_EmptyName(any(opNameAST).( TomName )) { 

				panic(&platform.RuntimeError{Msg: genericBadCase + platform.JavaString(opNameAST)})
			;  };  };  }; { if tom_listes_is_sort_TomName(opNameAST) { if tom_listes_is_fun_sym_Name(any(opNameAST).( TomName )) { 

				tomSymbol := g.getSymbolFromName(tom_listes_get_slot_Name_String(any(opNameAST).( TomName )))
				argType = tools.GetTLType(tools.GetSymbolCodomain(tomSymbol))
				returnType = tools.GetTLType(tools.GetSymbolDomain(tomSymbol).GetHeadconcTomType())
			;  };  };  };  }; 

		g.genDeclInstr(returnType, functionName, suffix,
			[]string{argType, varName},
			tom_listes_make_Return(tom_listes_make_ExpressionToBQTerm(code)), deep, moduleName)
	}
}




func (g *goGenerator) buildGetTailDecl(deep int, opNameAST TomName, varName, ty string, tlType TargetLanguageType, code Expression, moduleName string) {
	opname := opNameAST.GetString()
	inlined := g.inlineplus
	if code.IsCode() {
		
		ocode := code.GetCode()
		ncode := strings.ReplaceAll(ocode, "{0}", varName)
		if ncode != ocode {
			inlined = true
			code = tom_listes_make_Code(ncode)
		}
	}
	if !g.inline || !code.IsCode() || !inlined {
		returnType := ""
		argType := ""
		functionName := g.declName("tom_get_tail_" + opname)
		; { _ = opNameAST; { if tom_listes_is_sort_TomName(opNameAST) { if tom_listes_is_fun_sym_EmptyName(any(opNameAST).( TomName )) { 

				panic(&platform.RuntimeError{Msg: genericBadCase + platform.JavaString(opNameAST)})
			;  };  };  }; { if tom_listes_is_sort_TomName(opNameAST) { if tom_listes_is_fun_sym_Name(any(opNameAST).( TomName )) { 

				tomSymbol := g.getSymbolFromName(tom_listes_get_slot_Name_String(any(opNameAST).( TomName )))
				returnType = tools.GetTLType(tools.GetSymbolCodomain(tomSymbol))
				argType = returnType
			;  };  };  };  }; 

		g.genDeclInstr(returnType, functionName, ty,
			[]string{argType, varName},
			tom_listes_make_Return(tom_listes_make_ExpressionToBQTerm(code)), deep, moduleName)
	}
}




func (g *goGenerator) buildIsEmptyDecl(deep int, opNameAST TomName, varName, ty string, tlType TargetLanguageType, code Expression, moduleName string) {
	opname := opNameAST.GetString()
	inlined := g.inlineplus
	if code.IsCode() {
		
		ocode := code.GetCode()
		ncode := strings.ReplaceAll(ocode, "{0}", varName)
		if ncode != ocode {
			inlined = true
			code = tom_listes_make_Code(ncode)
		}
	}
	if !g.inline || !code.IsCode() || !inlined {
		argType := ""
		functionName := g.declName("tom_is_empty_" + opname)
		; { _ = opNameAST; { if tom_listes_is_sort_TomName(opNameAST) { if tom_listes_is_fun_sym_EmptyName(any(opNameAST).( TomName )) { 

				panic(&platform.RuntimeError{Msg: genericBadCase + platform.JavaString(opNameAST)})
			;  };  };  }; { if tom_listes_is_sort_TomName(opNameAST) { if tom_listes_is_fun_sym_Name(any(opNameAST).( TomName )) { 

				tomSymbol := g.getSymbolFromName(tom_listes_get_slot_Name_String(any(opNameAST).( TomName )))
				argType = tools.GetTLType(tools.GetSymbolCodomain(tomSymbol))
			;  };  };  };  }; 

		g.genDeclInstr(tools.GetTLType(g.getSymbolTable(moduleName).GetBooleanType()),
			functionName, ty,
			[]string{argType, varName},
			tom_listes_make_Return(tom_listes_make_ExpressionToBQTerm(code)), deep, moduleName)
	}
}




func (g *goGenerator) buildGetElementDecl(deep int, opNameAST TomName, name1, name2, type1 string, code Expression, moduleName string) {
	opname := opNameAST.GetString()
	inlined := g.inlineplus
	if code.IsCode() {
		
		ocode := code.GetCode()
		ncode := strings.ReplaceAll(strings.ReplaceAll(ocode, "{0}", name1), "{1}", name2)
		if ncode != ocode {
			inlined = true
			code = tom_listes_make_Code(ncode)
		}
	}
	if !g.inline || !code.IsCode() || !inlined {
		functionName := g.declName("tom_get_element")
		; { _ = opNameAST; { if tom_listes_is_sort_TomName(opNameAST) { if tom_listes_is_fun_sym_EmptyName(any(opNameAST).( TomName )) { 

				panic(&platform.RuntimeError{Msg: genericBadCase + platform.JavaString(opNameAST)})
			;  };  };  };  }; 

		tomSymbol := g.getSymbolFromName(opname)
		argType := tools.GetTLType(tools.GetSymbolCodomain(tomSymbol))
		returnType := tools.GetTLType(tools.GetSymbolDomain(tomSymbol).GetHeadconcTomType())
		g.genDeclInstr(returnType,
			functionName+"_"+opname, type1,
			[]string{argType, name1,
				tools.GetTLType(g.getSymbolTable(moduleName).GetIntType()), name2},
			tom_listes_make_Return(tom_listes_make_ExpressionToBQTerm(code)), deep, moduleName)
	}
}










func (g *goGenerator) buildGetSizeDecl(deep int, opNameAST TomName, name1, ty string, code Expression, moduleName string) {
	if code.IsCode() {
		it := g.goType(g.getSymbolTable(moduleName).GetIntType())
		body := javaTrim(code.GetCode())
		
		for strings.HasSuffix(body, ";") {
			body = javaTrim(body[:len(body)-1])
		}
		code = tom_listes_make_Code(it+"("+body+")")
	}
	g.buildGetSizeDeclGeneric(deep, opNameAST, name1, ty, code, moduleName)
}






func (g *goGenerator) buildGetSizeDeclGeneric(deep int, opNameAST TomName, name1, ty string, code Expression, moduleName string) {
	opname := opNameAST.GetString()
	inlined := g.inlineplus
	if code.IsCode() {
		
		ocode := code.GetCode()
		ncode := strings.ReplaceAll(ocode, "{0}", name1)
		if ncode != ocode {
			inlined = true
			code = tom_listes_make_Code(ncode)
		}
	}
	if !g.inline || !code.IsCode() || !inlined {
		argType := ""
		functionName := g.declName("tom_get_size")
		; { _ = opNameAST; { if tom_listes_is_sort_TomName(opNameAST) { if tom_listes_is_fun_sym_EmptyName(any(opNameAST).( TomName )) { 

				panic(&platform.RuntimeError{Msg: genericBadCase + platform.JavaString(opNameAST)})
			;  };  };  }; { if tom_listes_is_sort_TomName(opNameAST) { if tom_listes_is_fun_sym_Name(any(opNameAST).( TomName )) { 

				tomSymbol := g.getSymbolFromName(tom_listes_get_slot_Name_String(any(opNameAST).( TomName )))
				argType = tools.GetTLType(tools.GetSymbolCodomain(tomSymbol))
			;  };  };  };  }; 

		g.genDeclInstr(tools.GetTLType(g.getSymbolTable(moduleName).GetIntType()),
			functionName+"_"+opname, ty,
			[]string{argType, name1},
			tom_listes_make_Return(tom_listes_make_ExpressionToBQTerm(code)), deep, moduleName)
	}
}




func (g *goGenerator) buildExpGetElement(deep int, opNameAST TomName, domain TomType, varName, varIndex BQTerm, moduleName string) {
	deep = 0 
	; { _ = opNameAST; { if tom_listes_is_sort_TomName(opNameAST) { if tom_listes_is_fun_sym_EmptyName(any(opNameAST).( TomName )) { 

			panic(&platform.RuntimeError{Msg: genericBadCase + platform.JavaString(opNameAST)})
		;  };  };  };  }; 

	opName := opNameAST.GetString()
	sType := javaTomType(domain)
	template, hasTemplate := g.getSymbolTable(moduleName).GetGetElementArray(opName)
	if !g.instantiateTemplate(deep, template, hasTemplate, opName, tom_listes_cons_list_concBQTerm(varName,tom_listes_cons_list_concBQTerm(varIndex,tom_listes_empty_list_concBQTerm())), moduleName) {
		g.output.Write(g.declName("tom_get_element_"+opName+"_"+sType) + "(")
		g.generateBQTerm(deep, varName, moduleName)
		g.output.Write(",")
		g.generateBQTerm(deep, varIndex, moduleName)
		g.output.Write(")")
	}
}




func (g *goGenerator) buildListOrArray(deep int, list BQTerm, moduleName string) {
	deep = 0 
	; { _ = list; { if tom_listes_is_sort_BQTerm(list) { if tom_listes_is_fun_sym_BuildEmptyList(any(list).( BQTerm )) { var tomMatch9_1  TomName  = tom_listes_get_slot_BuildEmptyList_AstName(any(list).( BQTerm )); _ = tomMatch9_1; if tom_listes_is_fun_sym_Name(any(tomMatch9_1).( TomName )) { var tom___name  string  = tom_listes_get_slot_Name_String(tomMatch9_1); _ = tom___name; 

			prefix := g.declName("tom_empty_list_")
			template, hasTemplate := g.getSymbolTable(moduleName).GetMakeEmptyList(tom___name)
			if !g.instantiateTemplate(deep, template, hasTemplate, tom___name, tom_listes_empty_list_concBQTerm(), moduleName) {
				g.output.Write(prefix + tom___name + "()")
			}
			return
		;  };  };  };  }; { if tom_listes_is_sort_BQTerm(list) { if tom_listes_is_fun_sym_BuildConsList(any(list).( BQTerm )) { var tomMatch9_8  TomName  = tom_listes_get_slot_BuildConsList_AstName(any(list).( BQTerm )); _ = tomMatch9_8; if tom_listes_is_fun_sym_Name(any(tomMatch9_8).( TomName )) { var tom___name  string  = tom_listes_get_slot_Name_String(tomMatch9_8); _ = tom___name; var tom___headTerm  BQTerm  = tom_listes_get_slot_BuildConsList_HeadTerm(any(list).( BQTerm )); _ = tom___headTerm; var tom___tailTerm  BQTerm  = tom_listes_get_slot_BuildConsList_TailTerm(any(list).( BQTerm )); _ = tom___tailTerm; 

			prefix := g.declName("tom_cons_list_")
			template, hasTemplate := g.getSymbolTable(moduleName).GetMakeAddList(tom___name)
			if !g.instantiateTemplate(deep, template, hasTemplate, tom___name, tom_listes_cons_list_concBQTerm(tom___headTerm,tom_listes_cons_list_concBQTerm(tom___tailTerm,tom_listes_empty_list_concBQTerm())), moduleName) {
				g.output.Write(prefix + tom___name + "(")
				g.generateBQTerm(deep, tom___headTerm, moduleName)
				g.output.Write(",")
				g.generateBQTerm(deep, tom___tailTerm, moduleName)
				g.output.Write(")")
			}
			return
		;  };  };  };  }; { if tom_listes_is_sort_BQTerm(list) { if tom_listes_is_fun_sym_BuildAppendList(any(list).( BQTerm )) { var tomMatch9_17  TomName  = tom_listes_get_slot_BuildAppendList_AstName(any(list).( BQTerm )); _ = tomMatch9_17; if tom_listes_is_fun_sym_Name(any(tomMatch9_17).( TomName )) { 

			g.output.Write(g.declName("tom_append_list_"+tom_listes_get_slot_Name_String(tomMatch9_17)) + "(")
			g.generateBQTerm(deep, tom_listes_get_slot_BuildAppendList_HeadTerm(any(list).( BQTerm )), moduleName)
			g.output.Write(",")
			g.generateBQTerm(deep, tom_listes_get_slot_BuildAppendList_TailTerm(any(list).( BQTerm )), moduleName)
			g.output.Write(")")
			return
		;  };  };  };  }; { if tom_listes_is_sort_BQTerm(list) { if tom_listes_is_fun_sym_BuildEmptyArray(any(list).( BQTerm )) { var tomMatch9_26  TomName  = tom_listes_get_slot_BuildEmptyArray_AstName(any(list).( BQTerm )); _ = tomMatch9_26; if tom_listes_is_fun_sym_Name(any(tomMatch9_26).( TomName )) { var tom___name  string  = tom_listes_get_slot_Name_String(tomMatch9_26); _ = tom___name; var tom___size  BQTerm  = tom_listes_get_slot_BuildEmptyArray_Size(any(list).( BQTerm )); _ = tom___size; 

			prefix := g.declName("tom_empty_array_")
			template, hasTemplate := g.getSymbolTable(moduleName).GetMakeEmptyArray(tom___name)
			if !g.instantiateTemplate(deep, template, hasTemplate, tom___name, tom_listes_cons_list_concBQTerm(tom___size,tom_listes_empty_list_concBQTerm()), moduleName) {
				g.output.Write(prefix + tom___name + "(")
				g.generateBQTerm(deep, tom___size, moduleName)
				g.output.Write(")")
			}
			return
		;  };  };  };  }; { if tom_listes_is_sort_BQTerm(list) { if tom_listes_is_fun_sym_BuildConsArray(any(list).( BQTerm )) { var tomMatch9_34  TomName  = tom_listes_get_slot_BuildConsArray_AstName(any(list).( BQTerm )); _ = tomMatch9_34; if tom_listes_is_fun_sym_Name(any(tomMatch9_34).( TomName )) { var tom___name  string  = tom_listes_get_slot_Name_String(tomMatch9_34); _ = tom___name; var tom___headTerm  BQTerm  = tom_listes_get_slot_BuildConsArray_HeadTerm(any(list).( BQTerm )); _ = tom___headTerm; var tom___tailTerm  BQTerm  = tom_listes_get_slot_BuildConsArray_TailTerm(any(list).( BQTerm )); _ = tom___tailTerm; 

			template, hasTemplate := g.getSymbolTable(moduleName).GetMakeAddArray(tom___name)
			if !g.instantiateTemplate(deep, template, hasTemplate, tom___name, tom_listes_cons_list_concBQTerm(tom___headTerm,tom_listes_cons_list_concBQTerm(tom___tailTerm,tom_listes_empty_list_concBQTerm())), moduleName) {
				prefix := g.declName("tom_cons_array_")
				g.output.Write(prefix + tom___name + "(")
				g.generateBQTerm(deep, tom___headTerm, moduleName)
				g.output.Write(",")
				g.generateBQTerm(deep, tom___tailTerm, moduleName)
				g.output.Write(")")
			}
			return
		;  };  };  };  }; { if tom_listes_is_sort_BQTerm(list) { if tom_listes_is_fun_sym_BuildAppendArray(any(list).( BQTerm )) { var tomMatch9_43  TomName  = tom_listes_get_slot_BuildAppendArray_AstName(any(list).( BQTerm )); _ = tomMatch9_43; if tom_listes_is_fun_sym_Name(any(tomMatch9_43).( TomName )) { 

			g.output.Write(g.declName("tom_append_array_"+tom_listes_get_slot_Name_String(tomMatch9_43)) + "(")
			g.generateBQTerm(deep, tom_listes_get_slot_BuildAppendArray_HeadTerm(any(list).( BQTerm )), moduleName)
			g.output.Write(",")
			g.generateBQTerm(deep, tom_listes_get_slot_BuildAppendArray_TailTerm(any(list).( BQTerm )), moduleName)
			g.output.Write(")")
			return
		;  };  };  };  };  }; 

}







func (g *goGenerator) genDeclList(name, moduleName string) {
	if g.nodeclMode {
		return
	}
	tomSymbol := g.getSymbolTable(moduleName).GetSymbolFromName(name)
	listType := tools.GetSymbolCodomain(tomSymbol)
	eltType := tools.GetSymbolDomain(tomSymbol).GetHeadconcTomType()
	tomType := javaTomType(listType)
	t := g.goType(listType)
	isEmpty := g.declName("tom_is_empty_" + name + "_" + tomType)
	head := g.declName("tom_get_head_" + name + "_" + tomType)
	tail := g.declName("tom_get_tail_" + name + "_" + tomType)
	cons := g.declName("tom_cons_list_" + name)
	empty := g.declName("tom_empty_list_"+name) + "()"
	equal := g.declName("tom_equal_term_" + tomType)
	isConc := g.declName("tom_is_fun_sym_" + name)
	appendF := g.declName("tom_append_list_" + name)
	slice := g.declName("tom_get_slice_" + name)
	var s strings.Builder
	s.WriteString("func " + appendF + "(l1 " + t + ", l2 " + t + ") " + t + " { ")
	s.WriteString("if " + isEmpty + "(l1) { return l2 } else if " + isEmpty + "(l2) { return l1 } ")
	if listType == eltType { 
		s.WriteString("else if " + isConc + "(l1) { if " + isEmpty + "(" + tail + "(l1)) { return " + cons + "(" + head + "(l1), l2) } else { return " +
			cons + "(" + head + "(l1), " + appendF + "(" + tail + "(l1), l2)) } } else { return " + cons + "(l1, l2) }; }; ")
	} else {
		s.WriteString("else if " + isEmpty + "(" + tail + "(l1)) { return " + cons + "(" + head + "(l1), l2) } else { return " +
			cons + "(" + head + "(l1), " + appendF + "(" + tail + "(l1), l2)) }; }; ")
	}
	headIn := head + "(begin)"
	tailIn := tail + "(begin)"
	if listType == eltType {
		headIn = "func() " + t + " { if " + isConc + "(begin) { return " + head + "(begin) }; return begin }()"
		tailIn = "func() " + t + " { if " + isConc + "(begin) { return " + tail + "(begin) }; return " + empty + " }()"
	}
	s.WriteString("func " + slice + "(begin " + t + ", end " + t + ", tail " + t + ") " + t + " { ")
	s.WriteString("if " + equal + "(begin, end) { return tail } else if " + equal + "(end, tail) && (" + isEmpty + "(end) || " +
		equal + "(end, " + empty + ")) { return begin }; ")
	s.WriteString("return " + cons + "(" + headIn + ", " + slice + "(" + tailIn + ", end, tail)) }; ")
	g.output.Write(s.String())
}










func (g *goGenerator) genDeclArray(name, moduleName string) {
	if g.nodeclMode || !g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(name) {
		return
	}
	tomSymbol := g.getSymbolTable(moduleName).GetSymbolFromName(name)
	listType := tools.GetSymbolCodomain(tomSymbol)
	tomType := javaTomType(listType)
	t := g.goType(listType)
	it := g.goType(g.getSymbolTable(moduleName).GetIntType())
	empty := g.declName("tom_empty_array_" + name)
	cons := g.declName("tom_cons_array_" + name)
	elt := g.declName("tom_get_element_" + name + "_" + tomType)
	size := g.declName("tom_get_size_" + name + "_" + tomType)
	var s strings.Builder
	s.WriteString("func " + g.declName("tom_get_slice_"+name) + "(subject " + t + ", begin " + it + ", end " + it + ") " + t + " { ")
	s.WriteString("var result " + t + " = " + empty + "(end-begin); for begin != end { result = " + cons + "(" + elt + "(subject, begin), result); begin++ }; return result }; ")
	s.WriteString("func " + g.declName("tom_append_array_"+name) + "(l2 " + t + ", l1 " + t + ") " + t + " { ")
	s.WriteString("var size1 " + it + " = " + size + "(l1); var size2 " + it + " = " + size + "(l2); var index " + it + "; ")
	s.WriteString("var result " + t + " = " + empty + "(size1+size2); index = size1; ")
	s.WriteString("for index > 0 { result = " + cons + "(" + elt + "(l1, size1-index), result); index-- }; index = size2; ")
	s.WriteString("for index > 0 { result = " + cons + "(" + elt + "(l2, size2-index), result); index-- }; return result }; ")
	g.output.Write(s.String())
}




var _ = aterm.GetInstance

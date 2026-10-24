// tom-source-sha256: 3e4258b80b123d770535322a95585cc5b13aad80993a63b10aca27d28fc4fc24

package backend










import (
	"strconv"
	"strings"

	. "tom/engine/adt"
	"tom/engine/platform"
	"tom/engine/tools"
	"tom/library/aterm"
)

func tom_declarations_equal_term_char(t1 rune, t2 rune) bool { return t1 == t2;  }; func tom_declarations_is_sort_char(t rune) bool { return true;  }; func tom_declarations_equal_term_String(t1 string, t2 string) bool { return t1 == t2;  }; func tom_declarations_is_sort_String(t string) bool { return true;  }; func tom_declarations_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_declarations_is_sort_int(t int64) bool { return true;  }; func tom_declarations_equal_term_boolean(t1 bool, t2 bool) bool { return t1 == t2;  }; func tom_declarations_is_sort_boolean(t bool) bool { return true;  }; func tom_declarations_equal_term_ATerm(t1  aterm.ATerm , t2  aterm.ATerm ) bool { return t1 == t2;  }; func tom_declarations_is_sort_ATerm(t any) bool { return func() bool { _, ok := any(t).(aterm.ATerm); return ok }();  }; func tom_declarations_equal_term_ATermList(l1  *aterm.ATermList , l2  *aterm.ATermList ) bool { return l1 == l2;  }; func tom_declarations_is_sort_ATermList(t any) bool { return func() bool { _, ok := any(t).(*aterm.ATermList); return ok }();  }; func tom_declarations_equal_term_AFun(t1  *aterm.AFun , t2  *aterm.AFun ) bool { return t1 == t2;  }; func tom_declarations_is_sort_AFun(t any) bool { return func() bool { _, ok := any(t).(*aterm.AFun); return ok }();  }; func tom_declarations_equal_term_CstProgram(t1  CstProgram , t2  CstProgram ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstProgram(t any) bool { return func() bool { _, ok := (t).(CstProgram); return ok }();  }; func tom_declarations_equal_term_CstBlockList(t1  CstBlockList , t2  CstBlockList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstBlockList(t any) bool { return func() bool { _, ok := (t).(CstBlockList); return ok }();  }; func tom_declarations_equal_term_CstBlock(t1  CstBlock , t2  CstBlock ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstBlock(t any) bool { return func() bool { _, ok := (t).(CstBlock); return ok }();  }; func tom_declarations_equal_term_CstConstraintAction(t1  CstConstraintAction , t2  CstConstraintAction ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstConstraintAction(t any) bool { return func() bool { _, ok := (t).(CstConstraintAction); return ok }();  }; func tom_declarations_equal_term_CstConstraintActionList(t1  CstConstraintActionList , t2  CstConstraintActionList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstConstraintActionList(t any) bool { return func() bool { _, ok := (t).(CstConstraintActionList); return ok }();  }; func tom_declarations_equal_term_CstConstraint(t1  CstConstraint , t2  CstConstraint ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstConstraint(t any) bool { return func() bool { _, ok := (t).(CstConstraint); return ok }();  }; func tom_declarations_equal_term_CstConstraintList(t1  CstConstraintList , t2  CstConstraintList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstConstraintList(t any) bool { return func() bool { _, ok := (t).(CstConstraintList); return ok }();  }; func tom_declarations_equal_term_CstTerm(t1  CstTerm , t2  CstTerm ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstTerm(t any) bool { return func() bool { _, ok := (t).(CstTerm); return ok }();  }; func tom_declarations_equal_term_CstTermList(t1  CstTermList , t2  CstTermList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstTermList(t any) bool { return func() bool { _, ok := (t).(CstTermList); return ok }();  }; func tom_declarations_equal_term_CstType(t1  CstType , t2  CstType ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstType(t any) bool { return func() bool { _, ok := (t).(CstType); return ok }();  }; func tom_declarations_equal_term_CstPattern(t1  CstPattern , t2  CstPattern ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstPattern(t any) bool { return func() bool { _, ok := (t).(CstPattern); return ok }();  }; func tom_declarations_equal_term_CstSymbolList(t1  CstSymbolList , t2  CstSymbolList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstSymbolList(t any) bool { return func() bool { _, ok := (t).(CstSymbolList); return ok }();  }; func tom_declarations_equal_term_CstSymbol(t1  CstSymbol , t2  CstSymbol ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstSymbol(t any) bool { return func() bool { _, ok := (t).(CstSymbol); return ok }();  }; func tom_declarations_equal_term_CstTheory(t1  CstTheory , t2  CstTheory ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstTheory(t any) bool { return func() bool { _, ok := (t).(CstTheory); return ok }();  }; func tom_declarations_equal_term_CstPatternList(t1  CstPatternList , t2  CstPatternList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstPatternList(t any) bool { return func() bool { _, ok := (t).(CstPatternList); return ok }();  }; func tom_declarations_equal_term_CstPairPatternList(t1  CstPairPatternList , t2  CstPairPatternList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstPairPatternList(t any) bool { return func() bool { _, ok := (t).(CstPairPatternList); return ok }();  }; func tom_declarations_equal_term_CstPairPattern(t1  CstPairPattern , t2  CstPairPattern ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstPairPattern(t any) bool { return func() bool { _, ok := (t).(CstPairPattern); return ok }();  }; func tom_declarations_equal_term_CstOperatorList(t1  CstOperatorList , t2  CstOperatorList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstOperatorList(t any) bool { return func() bool { _, ok := (t).(CstOperatorList); return ok }();  }; func tom_declarations_equal_term_CstOperator(t1  CstOperator , t2  CstOperator ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstOperator(t any) bool { return func() bool { _, ok := (t).(CstOperator); return ok }();  }; func tom_declarations_equal_term_CstSlotList(t1  CstSlotList , t2  CstSlotList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstSlotList(t any) bool { return func() bool { _, ok := (t).(CstSlotList); return ok }();  }; func tom_declarations_equal_term_CstSlot(t1  CstSlot , t2  CstSlot ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstSlot(t any) bool { return func() bool { _, ok := (t).(CstSlot); return ok }();  }; func tom_declarations_equal_term_CstBQTerm(t1  CstBQTerm , t2  CstBQTerm ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstBQTerm(t any) bool { return func() bool { _, ok := (t).(CstBQTerm); return ok }();  }; func tom_declarations_equal_term_CstPairSlotBQTerm(t1  CstPairSlotBQTerm , t2  CstPairSlotBQTerm ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstPairSlotBQTerm(t any) bool { return func() bool { _, ok := (t).(CstPairSlotBQTerm); return ok }();  }; func tom_declarations_equal_term_CstBQTermList(t1  CstBQTermList , t2  CstBQTermList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstBQTermList(t any) bool { return func() bool { _, ok := (t).(CstBQTermList); return ok }();  }; func tom_declarations_equal_term_CstPairSlotBQTermList(t1  CstPairSlotBQTermList , t2  CstPairSlotBQTermList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstPairSlotBQTermList(t any) bool { return func() bool { _, ok := (t).(CstPairSlotBQTermList); return ok }();  }; func tom_declarations_equal_term_CstName(t1  CstName , t2  CstName ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstName(t any) bool { return func() bool { _, ok := (t).(CstName); return ok }();  }; func tom_declarations_equal_term_CstNameList(t1  CstNameList , t2  CstNameList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstNameList(t any) bool { return func() bool { _, ok := (t).(CstNameList); return ok }();  }; func tom_declarations_equal_term_CstOptionList(t1  CstOptionList , t2  CstOptionList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstOptionList(t any) bool { return func() bool { _, ok := (t).(CstOptionList); return ok }();  }; func tom_declarations_equal_term_CstOption(t1  CstOption , t2  CstOption ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstOption(t any) bool { return func() bool { _, ok := (t).(CstOption); return ok }();  }; func tom_declarations_equal_term_CstVisit(t1  CstVisit , t2  CstVisit ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstVisit(t any) bool { return func() bool { _, ok := (t).(CstVisit); return ok }();  }; func tom_declarations_equal_term_CstVisitList(t1  CstVisitList , t2  CstVisitList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CstVisitList(t any) bool { return func() bool { _, ok := (t).(CstVisitList); return ok }();  }; func tom_declarations_equal_term_TargetLanguage(t1  TargetLanguage , t2  TargetLanguage ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_TargetLanguage(t any) bool { return func() bool { _, ok := (t).(TargetLanguage); return ok }();  }; func tom_declarations_equal_term_CodeList(t1  CodeList , t2  CodeList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CodeList(t any) bool { return func() bool { _, ok := (t).(CodeList); return ok }();  }; func tom_declarations_equal_term_Code(t1  Code , t2  Code ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_Code(t any) bool { return func() bool { _, ok := (t).(Code); return ok }();  }; func tom_declarations_equal_term_BQTermList(t1  BQTermList , t2  BQTermList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_BQTermList(t any) bool { return func() bool { _, ok := (t).(BQTermList); return ok }();  }; func tom_declarations_equal_term_CompositeMember(t1  CompositeMember , t2  CompositeMember ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_CompositeMember(t any) bool { return func() bool { _, ok := (t).(CompositeMember); return ok }();  }; func tom_declarations_equal_term_BQTerm(t1  BQTerm , t2  BQTerm ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_BQTerm(t any) bool { return func() bool { _, ok := (t).(BQTerm); return ok }();  }; func tom_declarations_is_fun_sym_concBQTerm(t  BQTermList ) bool { return func() bool { _, ok := (t).(*ConcBQTermBQTermList); return ok }();  }; func tom_declarations_empty_list_concBQTerm()  BQTermList  { return (EmptyConcBQTerm()) }; func tom_declarations_cons_list_concBQTerm(e  BQTerm , l  BQTermList )  BQTermList  { return (ConsConcBQTerm(e,l)) }; func tom_declarations_get_head_concBQTerm_BQTermList(l  BQTermList )  BQTerm  { return (l).GetHeadconcBQTerm();  }; func tom_declarations_get_tail_concBQTerm_BQTermList(l  BQTermList )  BQTermList  { return (l).GetTailconcBQTerm();  }; func tom_declarations_is_empty_concBQTerm_BQTermList(l  BQTermList ) bool { return (l).IsEmptyconcBQTerm();  }; func tom_declarations_append_list_concBQTerm(l1  BQTermList , l2  BQTermList )  BQTermList  { if tom_declarations_is_empty_concBQTerm_BQTermList(l1) { return l2 } else if tom_declarations_is_empty_concBQTerm_BQTermList(l2) { return l1 } else if tom_declarations_is_empty_concBQTerm_BQTermList(tom_declarations_get_tail_concBQTerm_BQTermList(l1)) { return tom_declarations_cons_list_concBQTerm(tom_declarations_get_head_concBQTerm_BQTermList(l1), l2) } else { return tom_declarations_cons_list_concBQTerm(tom_declarations_get_head_concBQTerm_BQTermList(l1), tom_declarations_append_list_concBQTerm(tom_declarations_get_tail_concBQTerm_BQTermList(l1), l2)) }; }; func tom_declarations_get_slice_concBQTerm(begin  BQTermList , end  BQTermList , tail  BQTermList )  BQTermList  { if tom_declarations_equal_term_BQTermList(begin, end) { return tail } else if tom_declarations_equal_term_BQTermList(end, tail) && (tom_declarations_is_empty_concBQTerm_BQTermList(end) || tom_declarations_equal_term_BQTermList(end, tom_declarations_empty_list_concBQTerm())) { return begin }; return tom_declarations_cons_list_concBQTerm(tom_declarations_get_head_concBQTerm_BQTermList(begin), tom_declarations_get_slice_concBQTerm(tom_declarations_get_tail_concBQTerm_BQTermList(begin), end, tail)) }; func tom_declarations_is_fun_sym_CompositeBQTerm(t  CompositeMember ) bool { return func() bool { _, ok := (t).(*CompositeBQTermCompositeMember); return ok }();  }; func tom_declarations_get_slot_CompositeBQTerm_term(t  CompositeMember )  BQTerm  { return (t).(*CompositeBQTermCompositeMember).Term;  }; func tom_declarations_is_fun_sym_BQVariable(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BQVariableBQTerm); return ok }();  }; func tom_declarations_get_slot_BQVariable_Options(t  BQTerm )  OptionList  { return (t).(*BQVariableBQTerm).Options;  }; func tom_declarations_get_slot_BQVariable_AstName(t  BQTerm )  TomName  { return (t).(*BQVariableBQTerm).AstName;  }; func tom_declarations_get_slot_BQVariable_AstType(t  BQTerm )  TomType  { return (t).(*BQVariableBQTerm).AstType;  }; func tom_declarations_is_fun_sym_BQDefault(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BQDefaultBQTerm); return ok }();  }; func tom_declarations_is_fun_sym_Composite(t  BQTerm ) bool { return func() bool { _, ok := (t).(*CompositeBQTerm); return ok }();  }; func tom_declarations_empty_list_Composite()  BQTerm  { return (EmptyComposite()) }; func tom_declarations_cons_list_Composite(e  CompositeMember , l  BQTerm )  BQTerm  { return (ConsComposite(e,l)) }; func tom_declarations_get_head_Composite_BQTerm(l  BQTerm )  CompositeMember  { return (l).GetHeadComposite();  }; func tom_declarations_get_tail_Composite_BQTerm(l  BQTerm )  BQTerm  { return (l).GetTailComposite();  }; func tom_declarations_is_empty_Composite_BQTerm(l  BQTerm ) bool { return (l).IsEmptyComposite();  }; func tom_declarations_append_list_Composite(l1  BQTerm , l2  BQTerm )  BQTerm  { if tom_declarations_is_empty_Composite_BQTerm(l1) { return l2 } else if tom_declarations_is_empty_Composite_BQTerm(l2) { return l1 } else if tom_declarations_is_empty_Composite_BQTerm(tom_declarations_get_tail_Composite_BQTerm(l1)) { return tom_declarations_cons_list_Composite(tom_declarations_get_head_Composite_BQTerm(l1), l2) } else { return tom_declarations_cons_list_Composite(tom_declarations_get_head_Composite_BQTerm(l1), tom_declarations_append_list_Composite(tom_declarations_get_tail_Composite_BQTerm(l1), l2)) }; }; func tom_declarations_get_slice_Composite(begin  BQTerm , end  BQTerm , tail  BQTerm )  BQTerm  { if tom_declarations_equal_term_BQTerm(begin, end) { return tail } else if tom_declarations_equal_term_BQTerm(end, tail) && (tom_declarations_is_empty_Composite_BQTerm(end) || tom_declarations_equal_term_BQTerm(end, tom_declarations_empty_list_Composite())) { return begin }; return tom_declarations_cons_list_Composite(tom_declarations_get_head_Composite_BQTerm(begin), tom_declarations_get_slice_Composite(tom_declarations_get_tail_Composite_BQTerm(begin), end, tail)) }; func tom_declarations_make_ExpressionToBQTerm(Exp  Expression )  BQTerm  { return (MakeExpressionToBQTerm(Exp)) }; func tom_declarations_equal_term_Symbol(t1  Symbol , t2  Symbol ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_Symbol(t any) bool { return func() bool { _, ok := (t).(Symbol); return ok }();  }; func tom_declarations_equal_term_Variable(t1  Variable , t2  Variable ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_Variable(t any) bool { return func() bool { _, ok := (t).(Variable); return ok }();  }; func tom_declarations_equal_term_AbsTerm(t1  AbsTerm , t2  AbsTerm ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_AbsTerm(t any) bool { return func() bool { _, ok := (t).(AbsTerm); return ok }();  }; func tom_declarations_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_declarations_equal_term_Expr(t1  Expr , t2  Expr ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_Expr(t any) bool { return func() bool { _, ok := (t).(Expr); return ok }();  }; func tom_declarations_equal_term_Instr(t1  Instr , t2  Instr ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_Instr(t any) bool { return func() bool { _, ok := (t).(Instr); return ok }();  }; func tom_declarations_equal_term_InstrList(t1  InstrList , t2  InstrList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_InstrList(t any) bool { return func() bool { _, ok := (t).(InstrList); return ok }();  }; func tom_declarations_equal_term_Substitution(t1  Substitution , t2  Substitution ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_Substitution(t any) bool { return func() bool { _, ok := (t).(Substitution); return ok }();  }; func tom_declarations_equal_term_SubstitutionList(t1  SubstitutionList , t2  SubstitutionList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_SubstitutionList(t any) bool { return func() bool { _, ok := (t).(SubstitutionList); return ok }();  }; func tom_declarations_equal_term_Environment(t1  Environment , t2  Environment ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_Environment(t any) bool { return func() bool { _, ok := (t).(Environment); return ok }();  }; func tom_declarations_equal_term_Seq(t1  Seq , t2  Seq ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_Seq(t any) bool { return func() bool { _, ok := (t).(Seq); return ok }();  }; func tom_declarations_equal_term_TermList(t1  TermList , t2  TermList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_TermList(t any) bool { return func() bool { _, ok := (t).(TermList); return ok }();  }; func tom_declarations_equal_term_ExprList(t1  ExprList , t2  ExprList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_ExprList(t any) bool { return func() bool { _, ok := (t).(ExprList); return ok }();  }; func tom_declarations_equal_term_Deriv(t1  Deriv , t2  Deriv ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_Deriv(t any) bool { return func() bool { _, ok := (t).(Deriv); return ok }();  }; func tom_declarations_equal_term_DerivTree(t1  DerivTree , t2  DerivTree ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_DerivTree(t any) bool { return func() bool { _, ok := (t).(DerivTree); return ok }();  }; func tom_declarations_equal_term_ElementaryTheory(t1  ElementaryTheory , t2  ElementaryTheory ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_ElementaryTheory(t any) bool { return func() bool { _, ok := (t).(ElementaryTheory); return ok }();  }; func tom_declarations_equal_term_Theory(t1  Theory , t2  Theory ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_Theory(t any) bool { return func() bool { _, ok := (t).(Theory); return ok }();  }; func tom_declarations_equal_term_Constraint(t1  Constraint , t2  Constraint ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_Constraint(t any) bool { return func() bool { _, ok := (t).(Constraint); return ok }();  }; func tom_declarations_equal_term_NumericConstraintType(t1  NumericConstraintType , t2  NumericConstraintType ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_NumericConstraintType(t any) bool { return func() bool { _, ok := (t).(NumericConstraintType); return ok }();  }; func tom_declarations_equal_term_ConstraintList(t1  ConstraintList , t2  ConstraintList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_ConstraintList(t any) bool { return func() bool { _, ok := (t).(ConstraintList); return ok }();  }; func tom_declarations_equal_term_Declaration(t1  Declaration , t2  Declaration ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_Declaration(t any) bool { return func() bool { _, ok := (t).(Declaration); return ok }();  }; func tom_declarations_equal_term_DeclarationList(t1  DeclarationList , t2  DeclarationList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_DeclarationList(t any) bool { return func() bool { _, ok := (t).(DeclarationList); return ok }();  }; func tom_declarations_equal_term_Expression(t1  Expression , t2  Expression ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_Expression(t any) bool { return func() bool { _, ok := (t).(Expression); return ok }();  }; func tom_declarations_make_TrueTL()  Expression  { return (MakeTrueTL()) }; func tom_declarations_is_fun_sym_Code(t  Expression ) bool { return func() bool { _, ok := (t).(*CodeExpression); return ok }();  }; func tom_declarations_make_Code(Code  string )  Expression  { return (MakeCode(Code)) }; func tom_declarations_get_slot_Code_Code(t  Expression )  string  { return (t).(*CodeExpression).Code;  }; func tom_declarations_equal_term_Instruction(t1  Instruction , t2  Instruction ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_Instruction(t any) bool { return func() bool { _, ok := (t).(Instruction); return ok }();  }; func tom_declarations_equal_term_RefClassTracelinkInstruction(t1  RefClassTracelinkInstruction , t2  RefClassTracelinkInstruction ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_RefClassTracelinkInstruction(t any) bool { return func() bool { _, ok := (t).(RefClassTracelinkInstruction); return ok }();  }; func tom_declarations_equal_term_RefClassTracelinkInstructionList(t1  RefClassTracelinkInstructionList , t2  RefClassTracelinkInstructionList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_RefClassTracelinkInstructionList(t any) bool { return func() bool { _, ok := (t).(RefClassTracelinkInstructionList); return ok }();  }; func tom_declarations_equal_term_ConstraintInstruction(t1  ConstraintInstruction , t2  ConstraintInstruction ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_ConstraintInstruction(t any) bool { return func() bool { _, ok := (t).(ConstraintInstruction); return ok }();  }; func tom_declarations_equal_term_RuleInstruction(t1  RuleInstruction , t2  RuleInstruction ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_RuleInstruction(t any) bool { return func() bool { _, ok := (t).(RuleInstruction); return ok }();  }; func tom_declarations_equal_term_RuleInstructionList(t1  RuleInstructionList , t2  RuleInstructionList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_RuleInstructionList(t any) bool { return func() bool { _, ok := (t).(RuleInstructionList); return ok }();  }; func tom_declarations_equal_term_ConstraintInstructionList(t1  ConstraintInstructionList , t2  ConstraintInstructionList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_ConstraintInstructionList(t any) bool { return func() bool { _, ok := (t).(ConstraintInstructionList); return ok }();  }; func tom_declarations_equal_term_InstructionList(t1  InstructionList , t2  InstructionList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_InstructionList(t any) bool { return func() bool { _, ok := (t).(InstructionList); return ok }();  }; func tom_declarations_is_fun_sym_ExpressionToInstruction(t  Instruction ) bool { return func() bool { _, ok := (t).(*ExpressionToInstructionInstruction); return ok }();  }; func tom_declarations_make_ExpressionToInstruction(Expr  Expression )  Instruction  { return (MakeExpressionToInstruction(Expr)) }; func tom_declarations_get_slot_ExpressionToInstruction_Expr(t  Instruction )  Expression  { return (t).(*ExpressionToInstructionInstruction).Expr;  }; func tom_declarations_make_Return(Kid1  BQTerm )  Instruction  { return (MakeReturn(Kid1)) }; func tom_declarations_equal_term_TomName(t1  TomName , t2  TomName ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_TomName(t any) bool { return func() bool { _, ok := (t).(TomName); return ok }();  }; func tom_declarations_equal_term_TomNumber(t1  TomNumber , t2  TomNumber ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_TomNumber(t any) bool { return func() bool { _, ok := (t).(TomNumber); return ok }();  }; func tom_declarations_equal_term_TomNumberList(t1  TomNumberList , t2  TomNumberList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_TomNumberList(t any) bool { return func() bool { _, ok := (t).(TomNumberList); return ok }();  }; func tom_declarations_equal_term_TomNameList(t1  TomNameList , t2  TomNameList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_TomNameList(t any) bool { return func() bool { _, ok := (t).(TomNameList); return ok }();  }; func tom_declarations_is_fun_sym_Name(t  TomName ) bool { return func() bool { _, ok := (t).(*NameTomName); return ok }();  }; func tom_declarations_get_slot_Name_String(t  TomName )  string  { return (t).(*NameTomName).String_;  }; func tom_declarations_equal_term_Option(t1  Option , t2  Option ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_Option(t any) bool { return func() bool { _, ok := (t).(Option); return ok }();  }; func tom_declarations_equal_term_OptionList(t1  OptionList , t2  OptionList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_OptionList(t any) bool { return func() bool { _, ok := (t).(OptionList); return ok }();  }; func tom_declarations_equal_term_TextPosition(t1  TextPosition , t2  TextPosition ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_TextPosition(t any) bool { return func() bool { _, ok := (t).(TextPosition); return ok }();  }; func tom_declarations_equal_term_TomVisit(t1  TomVisit , t2  TomVisit ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_TomVisit(t any) bool { return func() bool { _, ok := (t).(TomVisit); return ok }();  }; func tom_declarations_equal_term_ResolveStratElement(t1  ResolveStratElement , t2  ResolveStratElement ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_ResolveStratElement(t any) bool { return func() bool { _, ok := (t).(ResolveStratElement); return ok }();  }; func tom_declarations_equal_term_ResolveStratElementList(t1  ResolveStratElementList , t2  ResolveStratElementList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_ResolveStratElementList(t any) bool { return func() bool { _, ok := (t).(ResolveStratElementList); return ok }();  }; func tom_declarations_equal_term_ResolveStratBlock(t1  ResolveStratBlock , t2  ResolveStratBlock ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_ResolveStratBlock(t any) bool { return func() bool { _, ok := (t).(ResolveStratBlock); return ok }();  }; func tom_declarations_equal_term_ResolveStratBlockList(t1  ResolveStratBlockList , t2  ResolveStratBlockList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_ResolveStratBlockList(t any) bool { return func() bool { _, ok := (t).(ResolveStratBlockList); return ok }();  }; func tom_declarations_equal_term_ElementaryTransformation(t1  ElementaryTransformation , t2  ElementaryTransformation ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_ElementaryTransformation(t any) bool { return func() bool { _, ok := (t).(ElementaryTransformation); return ok }();  }; func tom_declarations_equal_term_ElementaryTransformationList(t1  ElementaryTransformationList , t2  ElementaryTransformationList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_ElementaryTransformationList(t any) bool { return func() bool { _, ok := (t).(ElementaryTransformationList); return ok }();  }; func tom_declarations_equal_term_TomSymbol(t1  TomSymbol , t2  TomSymbol ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_TomSymbol(t any) bool { return func() bool { _, ok := (t).(TomSymbol); return ok }();  }; func tom_declarations_equal_term_TomSymbolTable(t1  TomSymbolTable , t2  TomSymbolTable ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_TomSymbolTable(t any) bool { return func() bool { _, ok := (t).(TomSymbolTable); return ok }();  }; func tom_declarations_equal_term_TomEntry(t1  TomEntry , t2  TomEntry ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_TomEntry(t any) bool { return func() bool { _, ok := (t).(TomEntry); return ok }();  }; func tom_declarations_equal_term_TomStructureTable(t1  TomStructureTable , t2  TomStructureTable ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_TomStructureTable(t any) bool { return func() bool { _, ok := (t).(TomStructureTable); return ok }();  }; func tom_declarations_equal_term_TomVisitList(t1  TomVisitList , t2  TomVisitList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_TomVisitList(t any) bool { return func() bool { _, ok := (t).(TomVisitList); return ok }();  }; func tom_declarations_equal_term_TomEntryList(t1  TomEntryList , t2  TomEntryList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_TomEntryList(t any) bool { return func() bool { _, ok := (t).(TomEntryList); return ok }();  }; func tom_declarations_equal_term_TomSymbolList(t1  TomSymbolList , t2  TomSymbolList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_TomSymbolList(t any) bool { return func() bool { _, ok := (t).(TomSymbolList); return ok }();  }; func tom_declarations_equal_term_KeyEntry(t1  KeyEntry , t2  KeyEntry ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_KeyEntry(t any) bool { return func() bool { _, ok := (t).(KeyEntry); return ok }();  }; func tom_declarations_equal_term_TransfoStratInfo(t1  TransfoStratInfo , t2  TransfoStratInfo ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_TransfoStratInfo(t any) bool { return func() bool { _, ok := (t).(TransfoStratInfo); return ok }();  }; func tom_declarations_equal_term_Slot(t1  Slot , t2  Slot ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_Slot(t any) bool { return func() bool { _, ok := (t).(Slot); return ok }();  }; func tom_declarations_equal_term_SlotList(t1  SlotList , t2  SlotList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_SlotList(t any) bool { return func() bool { _, ok := (t).(SlotList); return ok }();  }; func tom_declarations_equal_term_BQSlot(t1  BQSlot , t2  BQSlot ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_BQSlot(t any) bool { return func() bool { _, ok := (t).(BQSlot); return ok }();  }; func tom_declarations_equal_term_BQSlotList(t1  BQSlotList , t2  BQSlotList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_BQSlotList(t any) bool { return func() bool { _, ok := (t).(BQSlotList); return ok }();  }; func tom_declarations_equal_term_PairNameDecl(t1  PairNameDecl , t2  PairNameDecl ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_PairNameDecl(t any) bool { return func() bool { _, ok := (t).(PairNameDecl); return ok }();  }; func tom_declarations_equal_term_PairNameDeclList(t1  PairNameDeclList , t2  PairNameDeclList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_PairNameDeclList(t any) bool { return func() bool { _, ok := (t).(PairNameDeclList); return ok }();  }; func tom_declarations_equal_term_TomTerm(t1  TomTerm , t2  TomTerm ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_TomTerm(t any) bool { return func() bool { _, ok := (t).(TomTerm); return ok }();  }; func tom_declarations_equal_term_TomList(t1  TomList , t2  TomList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_TomList(t any) bool { return func() bool { _, ok := (t).(TomList); return ok }();  }; func tom_declarations_equal_term_TomType(t1  TomType , t2  TomType ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_TomType(t any) bool { return func() bool { _, ok := (t).(TomType); return ok }();  }; func tom_declarations_equal_term_TypeOption(t1  TypeOption , t2  TypeOption ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_TypeOption(t any) bool { return func() bool { _, ok := (t).(TypeOption); return ok }();  }; func tom_declarations_equal_term_TypeOptionList(t1  TypeOptionList , t2  TypeOptionList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_TypeOptionList(t any) bool { return func() bool { _, ok := (t).(TypeOptionList); return ok }();  }; func tom_declarations_equal_term_TargetLanguageType(t1  TargetLanguageType , t2  TargetLanguageType ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_TargetLanguageType(t any) bool { return func() bool { _, ok := (t).(TargetLanguageType); return ok }();  }; func tom_declarations_equal_term_TomTypeList(t1  TomTypeList , t2  TomTypeList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_TomTypeList(t any) bool { return func() bool { _, ok := (t).(TomTypeList); return ok }();  }; func tom_declarations_is_fun_sym_Type(t  TomType ) bool { return func() bool { _, ok := (t).(*TypeTomType); return ok }();  }; func tom_declarations_get_slot_Type_TypeOptions(t  TomType )  TypeOptionList  { return (t).(*TypeTomType).TypeOptions;  }; func tom_declarations_get_slot_Type_TomType(t  TomType )  string  { return (t).(*TypeTomType).TomType;  }; func tom_declarations_get_slot_Type_TlType(t  TomType )  TargetLanguageType  { return (t).(*TypeTomType).TlType;  }; func tom_declarations_is_fun_sym_TLType(t  TargetLanguageType ) bool { return func() bool { _, ok := (t).(*TLTypeTargetLanguageType); return ok }();  }; func tom_declarations_get_slot_TLType_String(t  TargetLanguageType )  string  { return (t).(*TLTypeTargetLanguageType).String_;  }; func tom_declarations_equal_term_Info(t1  Info , t2  Info ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_Info(t any) bool { return func() bool { _, ok := (t).(Info); return ok }();  }; func tom_declarations_equal_term_TypeConstraint(t1  TypeConstraint , t2  TypeConstraint ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_TypeConstraint(t any) bool { return func() bool { _, ok := (t).(TypeConstraint); return ok }();  }; func tom_declarations_equal_term_TypeConstraintList(t1  TypeConstraintList , t2  TypeConstraintList ) bool { return (t1).Equivalent(t2);  }; func tom_declarations_is_sort_TypeConstraintList(t any) bool { return func() bool { _, ok := (t).(TypeConstraintList); return ok }();  }; 





func (g *goGenerator) buildTerm(deep int, opname string, argList BQTermList, moduleName string) {
	deep = 0 
	template, hasTemplate := g.getSymbolTable(moduleName).GetMake(opname)
	if !g.instantiateTemplate(deep, template, hasTemplate, opname, argList, moduleName) {
		prefix := g.declName("tom_make_")
		g.output.Write(prefix + opname)
		g.output.WriteOpenBrace()
		index := 0
		for !argList.IsEmptyconcBQTerm() {
			bqt := argList.GetHeadconcBQTerm()
			generatebqt := true
			; { _ = bqt; { if tom_declarations_is_sort_BQTerm(bqt) { if tom_declarations_is_fun_sym_BQDefault(any(bqt).( BQTerm )) { 

					
					tomSymbol := g.getSymbolTable(moduleName).GetSymbolFromName(opname)
					slotName := tools.GetSlotName(tomSymbol, index)
					g.buildExpGetDefault(deep, opname, slotName.GetString(), moduleName)
					generatebqt = false
				;  };  };  }; { if tom_declarations_is_sort_BQTerm(bqt) { if tom_declarations_is_fun_sym_Composite(any(bqt).( BQTerm )) { if !(tom_declarations_is_empty_Composite_BQTerm(any(bqt).( BQTerm ))) { var tomMatch1_8  CompositeMember  = tom_declarations_get_head_Composite_BQTerm(any(bqt).( BQTerm )); _ = tomMatch1_8; if tom_declarations_is_fun_sym_CompositeBQTerm(any(tomMatch1_8).( CompositeMember )) { if tom_declarations_is_fun_sym_BQDefault(any(tom_declarations_get_slot_CompositeBQTerm_term(tomMatch1_8)).( BQTerm )) { 

					
					tomSymbol := g.getSymbolTable(moduleName).GetSymbolFromName(opname)
					slotName := tools.GetSlotName(tomSymbol, index)
					g.buildExpGetDefault(deep, opname, slotName.GetString(), moduleName)
					generatebqt = false
				;  };  };  };  };  };  };  }; 

			if generatebqt {
				g.generateBQTerm(deep, bqt, moduleName)
			}
			argList = argList.GetTailconcBQTerm()
			if !argList.IsEmptyconcBQTerm() {
				g.output.WriteComa()
			}
			index++
		}
		g.output.WriteCloseBrace()
	}
}




func (g *goGenerator) isResolveOp(opname string, moduleName string) bool {
	g.getSymbolTable(moduleName).GetSymbolFromName(opname)
	return true
}
















func (g *goGenerator) instantiateTemplate(deep int, template string, hasTemplate bool, opname string, termList BQTermList, moduleName string) bool {
	if g.inline && hasTemplate {
		oldOutput := g.output
		instance := template
		index := 0
		for !termList.IsEmptyconcBQTerm() {
			g.output = tools.NewStringOutputCode()
			bqt := termList.GetHeadconcBQTerm()
			generatebqt := true
			; { _ = bqt; { if tom_declarations_is_sort_BQTerm(bqt) { if tom_declarations_is_fun_sym_BQDefault(any(bqt).( BQTerm )) { 

					
					tomSymbol := g.getSymbolTable(moduleName).GetSymbolFromName(opname)
					slotName := tools.GetSlotName(tomSymbol, index)
					g.buildExpGetDefault(deep, opname, slotName.GetString(), moduleName)
					generatebqt = false
				;  };  };  }; { if tom_declarations_is_sort_BQTerm(bqt) { if tom_declarations_is_fun_sym_Composite(any(bqt).( BQTerm )) { if !(tom_declarations_is_empty_Composite_BQTerm(any(bqt).( BQTerm ))) { var tomMatch2_8  CompositeMember  = tom_declarations_get_head_Composite_BQTerm(any(bqt).( BQTerm )); _ = tomMatch2_8; if tom_declarations_is_fun_sym_CompositeBQTerm(any(tomMatch2_8).( CompositeMember )) { if tom_declarations_is_fun_sym_BQDefault(any(tom_declarations_get_slot_CompositeBQTerm_term(tomMatch2_8)).( BQTerm )) { 

					tomSymbol := g.getSymbolTable(moduleName).GetSymbolFromName(opname)
					slotName := tools.GetSlotName(tomSymbol, index)
					g.buildExpGetDefault(deep, opname, slotName.GetString(), moduleName)
					generatebqt = false
				;  };  };  };  };  };  };  }; 

			if generatebqt {
				g.generateBQTerm(deep, bqt, moduleName)
			}
			dump := g.output.StringDump()
			instance = strings.ReplaceAll(instance, "{"+strconv.Itoa(index)+"}", dump)
			termList = termList.GetTailconcBQTerm()
			index++
		}
		g.output = oldOutput
		if g.inlineplus || instance != template {
			
			g.output.Write(instance)
			return true
		}
	}
	return false
}






func (g *goGenerator) instantiateTemplateSubject(template string, hasTemplate bool, subject string) (string, bool) {
	if hasTemplate {
		return strings.ReplaceAll(template, "{0}", subject), true
	}
	return "", false
}






func (g *goGenerator) instantiateTemplateHeadTail(template string, hasTemplate bool, head, tail string) (string, bool) {
	if hasTemplate {
		return strings.ReplaceAll(strings.ReplaceAll(template, "{0}", head), "{1}", tail), true
	}
	return "", false
}




func (g *goGenerator) buildExpIsSort(deep int, ty string, exp BQTerm, moduleName string) {
	deep = 0 
	if g.getSymbolTable(moduleName).IsBuiltinType(ty) {
		g.generateExpression(deep, tom_declarations_make_TrueTL(), moduleName)
		return
	}
	template, hasTemplate := g.getSymbolTable(moduleName).GetIsSort(ty)
	opname := ""
	if !g.instantiateTemplate(deep, template, hasTemplate, opname, tom_declarations_cons_list_concBQTerm(exp,tom_declarations_empty_list_concBQTerm()), moduleName) {
		g.output.Write(g.declName("tom_is_sort_"+ty) + "(")
		g.generateBQTerm(deep, exp, moduleName)
		g.output.Write(")")
	}
}




func (g *goGenerator) buildExpIsFsym(deep int, opname string, exp BQTerm, moduleName string) {
	deep = 0 
	template, hasTemplate := g.getSymbolTable(moduleName).GetIsFsym(opname)
	if !g.instantiateTemplate(deep, template, hasTemplate, opname, tom_declarations_cons_list_concBQTerm(exp,tom_declarations_empty_list_concBQTerm()), moduleName) {
		s, ok := g.isFsymMap[opname] 
		if !ok {
			s = g.declName("tom_is_fun_sym_"+opname) + "("
		}
		g.output.Write(s)
		g.generateBQTerm(deep, exp, moduleName)
		g.output.Write(")")
	}
}




func (g *goGenerator) buildExpGetSlot(deep int, opname, slotName string, v BQTerm, moduleName string) {
	deep = 0 
	template, hasTemplate := g.getSymbolTable(moduleName).GetGetSlot(opname, slotName)
	if !g.instantiateTemplate(deep, template, hasTemplate, opname, tom_declarations_cons_list_concBQTerm(v,tom_declarations_empty_list_concBQTerm()), moduleName) {
		g.output.Write(g.declName("tom_get_slot_" + opname + "_" + slotName))
		g.output.WriteOpenBrace()
		g.generateBQTerm(deep, v, moduleName)
		g.output.WriteCloseBrace()
	}
}




func (g *goGenerator) buildExpGetDefault(deep int, opname string, slotName string, moduleName string) {
	deep = 0 
	g.output.Write(g.declName("tom_get_default_" + opname + "_" + slotName))
	g.output.WriteOpenBrace()
	g.output.WriteCloseBrace()
}






func (g *goGenerator) buildIsFsymDecl(deep int, tomName, varname string, tlType TargetLanguageType, code Expression, moduleName string) {
	tomSymbol := g.getSymbolTable(moduleName).GetSymbolFromName(tomName)
	opname := tomSymbol.GetAstName().GetString()
	inlined := g.inlineplus
	if code.IsCode() {
		
		ocode := code.GetCode()
		ncode := strings.ReplaceAll(ocode, "{0}", varname)
		if ncode != ocode {
			inlined = true
			code = tom_declarations_make_Code(ncode)
		}
	}
	if !g.inline || !code.IsCode() || !inlined {
		returnType := g.getSymbolTable(moduleName).GetBooleanType()
		argType := tools.GetTLCode(tlType)
		g.genDeclInstr(tools.GetTLType(returnType), g.declName("tom_is_fun_sym"), opname,
			[]string{argType, varname}, tom_declarations_make_Return(tom_declarations_make_ExpressionToBQTerm(code)), deep, moduleName)
	}
}




func (g *goGenerator) buildGetSlotDecl(deep int, tomName, varname string, tlType TargetLanguageType, code Expression, slotName TomName, moduleName string) {
	tomSymbol := g.getSymbolTable(moduleName).GetSymbolFromName(tomName)
	opname := tomSymbol.GetAstName().GetString()
	typesList := tomSymbol.GetTypesToType().GetDomain()
	inlined := g.inlineplus
	if code.IsCode() {
		
		ocode := code.GetCode()
		ncode := strings.ReplaceAll(ocode, "{0}", varname)
		if ncode != ocode {
			inlined = true
			code = tom_declarations_make_Code(ncode)
		}
	}
	if !g.inline || !code.IsCode() || !inlined {
		slotIndex := tools.GetSlotIndex(tomSymbol, slotName)
		l := typesList
		for index := 0; !l.IsEmptyconcTomType() && index < slotIndex; index++ {
			l = l.GetTailconcTomType()
		}
		returnType := l.GetHeadconcTomType()
		argType := tools.GetTLCode(tlType)
		g.genDeclInstr(tools.GetTLType(returnType),
			g.declName("tom_get_slot"), opname+"_"+slotName.GetString(),
			[]string{argType, varname},
			tom_declarations_make_Return(tom_declarations_make_ExpressionToBQTerm(code)), deep, moduleName)
	}
}




func (g *goGenerator) buildGetDefaultDecl(deep int, tomName string, code Expression, slotName TomName, moduleName string) {
	tomSymbol := g.getSymbolTable(moduleName).GetSymbolFromName(tomName)
	opname := tomSymbol.GetAstName().GetString()
	typesList := tomSymbol.GetTypesToType().GetDomain()
	slotIndex := tools.GetSlotIndex(tomSymbol, slotName)
	l := typesList
	for index := 0; !l.IsEmptyconcTomType() && index < slotIndex; index++ {
		l = l.GetTailconcTomType()
	}
	returnType := l.GetHeadconcTomType()
	g.genDeclInstr(tools.GetTLType(returnType),
		g.declName("tom_get_default"), opname+"_"+slotName.GetString(),
		[]string{},
		tom_declarations_make_Return(tom_declarations_make_ExpressionToBQTerm(code)), deep, moduleName)
}




func (g *goGenerator) buildFunctionCall(deep int, name string, argList BQTermList, moduleName string) {
	g.output.WriteDeep(deep, name)
	g.output.WriteOpenBrace()
	for !argList.IsEmptyconcBQTerm() {
		bqt := argList.GetHeadconcBQTerm()
		g.generateBQTerm(0, bqt, moduleName)
		argList = argList.GetTailconcBQTerm()
		if !argList.IsEmptyconcBQTerm() {
			g.output.WriteComa()
		}
	}
	g.output.WriteCloseBrace()
}










func (g *goGenerator) genDeclMake(prefix, funName string, returnType TomType, argList BQTermList, instr Instruction, moduleName string) {
	if g.nodeclMode {
		return
	}
	; { _ = instr; { if tom_declarations_is_sort_Instruction(instr) { if tom_declarations_is_fun_sym_ExpressionToInstruction(any(instr).( Instruction )) { var tomMatch3_1  Expression  = tom_declarations_get_slot_ExpressionToInstruction_Expr(any(instr).( Instruction )); _ = tomMatch3_1; if tom_declarations_is_fun_sym_Code(any(tomMatch3_1).( Expression )) { 

			ncode := tom_declarations_get_slot_Code_Code(tomMatch3_1)
			index := 0
			; { _ = argList; { if tom_declarations_is_sort_BQTermList(argList) { if tom_declarations_is_fun_sym_concBQTerm(any(argList).( BQTermList )) { var tomMatch4_end_4  BQTermList  = any(argList).( BQTermList ); _ = tomMatch4_end_4; for { { if !(tom_declarations_is_empty_concBQTerm_BQTermList(tomMatch4_end_4)) { var tomMatch4_8  BQTerm  = tom_declarations_get_head_concBQTerm_BQTermList(tomMatch4_end_4); _ = tomMatch4_8; if tom_declarations_is_fun_sym_BQVariable(any(tomMatch4_8).( BQTerm )) { var tomMatch4_7  TomName  = tom_declarations_get_slot_BQVariable_AstName(tomMatch4_8); _ = tomMatch4_7; if tom_declarations_is_fun_sym_Name(any(tomMatch4_7).( TomName )) { 

					ncode = strings.ReplaceAll(ncode, "{"+strconv.Itoa(index)+"}", tom_declarations_get_slot_Name_String(tomMatch4_7))
					index++
				;  };  };  }; if tom_declarations_is_empty_concBQTerm_BQTermList(tomMatch4_end_4) { tomMatch4_end_4 = any(argList).( BQTermList );  } else { tomMatch4_end_4 = tom_declarations_get_tail_concBQTerm_BQTermList(tomMatch4_end_4);  };  };  if !(!(tom_declarations_equal_term_BQTermList(tomMatch4_end_4, any(argList).( BQTermList )))) { break; }; };  };  };  };  }; 

			instr = tom_declarations_make_ExpressionToInstruction(tom_declarations_make_Code(ncode));  };  };  };  };  }; 


	var s strings.Builder
	s.WriteString("func " + prefix + funName + "(")
	for !argList.IsEmptyconcBQTerm() {
		arg := argList.GetHeadconcBQTerm()
		; { _ = arg; { if tom_declarations_is_sort_BQTerm(arg) { if tom_declarations_is_fun_sym_BQVariable(any(arg).( BQTerm )) { var tomMatch5_1  TomName  = tom_declarations_get_slot_BQVariable_AstName(any(arg).( BQTerm )); _ = tomMatch5_1; var tomMatch5_2  TomType  = tom_declarations_get_slot_BQVariable_AstType(any(arg).( BQTerm )); _ = tomMatch5_2; if tom_declarations_is_fun_sym_Name(any(tomMatch5_1).( TomName )) { if tom_declarations_is_fun_sym_Type(any(tomMatch5_2).( TomType )) { var tomMatch5_8  TargetLanguageType  = tom_declarations_get_slot_Type_TlType(tomMatch5_2); _ = tomMatch5_8; if tom_declarations_is_fun_sym_TLType(any(tomMatch5_8).( TargetLanguageType )) { 

				s.WriteString(tom_declarations_get_slot_Name_String(tomMatch5_1) + " " + tools.GetTLCode(tomMatch5_8))
				argList = argList.GetTailconcBQTerm()
				if !argList.IsEmptyconcBQTerm() {
					s.WriteString(", ")
				}
				continue
			;  };  };  };  };  };  }; { if tom_declarations_is_sort_BQTerm(arg) { 

				panic(&platform.RuntimeError{Msg: "genDeclMake: strange term: " + platform.JavaString(arg)})
			;  };  };  }; 

	}
	
	s.WriteString(") " + g.goType(returnType) + " { return (")
	g.output.Write(s.String())
	g.generateInstruction(0, instr, moduleName)
	g.output.Write(") }; ")
}




var _ = aterm.GetInstance

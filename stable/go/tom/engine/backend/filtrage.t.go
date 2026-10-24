// tom-source-sha256: 22902cfdbad4277310daa9783317e8a444d1d49cc783364fa31417ad421409ac

package backend









import (
	. "tom/engine/adt"
	"tom/engine/platform"
	"tom/engine/tools"
	"tom/library/aterm"
)

func tom_filtrage_equal_term_char(t1 rune, t2 rune) bool { return t1 == t2;  }; func tom_filtrage_is_sort_char(t rune) bool { return true;  }; func tom_filtrage_equal_term_String(t1 string, t2 string) bool { return t1 == t2;  }; func tom_filtrage_is_sort_String(t string) bool { return true;  }; func tom_filtrage_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_filtrage_is_sort_int(t int64) bool { return true;  }; func tom_filtrage_equal_term_boolean(t1 bool, t2 bool) bool { return t1 == t2;  }; func tom_filtrage_is_sort_boolean(t bool) bool { return true;  }; func tom_filtrage_equal_term_ATerm(t1  aterm.ATerm , t2  aterm.ATerm ) bool { return t1 == t2;  }; func tom_filtrage_is_sort_ATerm(t any) bool { return func() bool { _, ok := any(t).(aterm.ATerm); return ok }();  }; func tom_filtrage_equal_term_ATermList(l1  *aterm.ATermList , l2  *aterm.ATermList ) bool { return l1 == l2;  }; func tom_filtrage_is_sort_ATermList(t any) bool { return func() bool { _, ok := any(t).(*aterm.ATermList); return ok }();  }; func tom_filtrage_equal_term_AFun(t1  *aterm.AFun , t2  *aterm.AFun ) bool { return t1 == t2;  }; func tom_filtrage_is_sort_AFun(t any) bool { return func() bool { _, ok := any(t).(*aterm.AFun); return ok }();  }; func tom_filtrage_equal_term_CstProgram(t1  CstProgram , t2  CstProgram ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstProgram(t any) bool { return func() bool { _, ok := (t).(CstProgram); return ok }();  }; func tom_filtrage_equal_term_CstBlockList(t1  CstBlockList , t2  CstBlockList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstBlockList(t any) bool { return func() bool { _, ok := (t).(CstBlockList); return ok }();  }; func tom_filtrage_equal_term_CstBlock(t1  CstBlock , t2  CstBlock ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstBlock(t any) bool { return func() bool { _, ok := (t).(CstBlock); return ok }();  }; func tom_filtrage_equal_term_CstConstraintAction(t1  CstConstraintAction , t2  CstConstraintAction ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstConstraintAction(t any) bool { return func() bool { _, ok := (t).(CstConstraintAction); return ok }();  }; func tom_filtrage_equal_term_CstConstraintActionList(t1  CstConstraintActionList , t2  CstConstraintActionList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstConstraintActionList(t any) bool { return func() bool { _, ok := (t).(CstConstraintActionList); return ok }();  }; func tom_filtrage_equal_term_CstConstraint(t1  CstConstraint , t2  CstConstraint ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstConstraint(t any) bool { return func() bool { _, ok := (t).(CstConstraint); return ok }();  }; func tom_filtrage_equal_term_CstConstraintList(t1  CstConstraintList , t2  CstConstraintList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstConstraintList(t any) bool { return func() bool { _, ok := (t).(CstConstraintList); return ok }();  }; func tom_filtrage_equal_term_CstTerm(t1  CstTerm , t2  CstTerm ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstTerm(t any) bool { return func() bool { _, ok := (t).(CstTerm); return ok }();  }; func tom_filtrage_equal_term_CstTermList(t1  CstTermList , t2  CstTermList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstTermList(t any) bool { return func() bool { _, ok := (t).(CstTermList); return ok }();  }; func tom_filtrage_equal_term_CstType(t1  CstType , t2  CstType ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstType(t any) bool { return func() bool { _, ok := (t).(CstType); return ok }();  }; func tom_filtrage_equal_term_CstPattern(t1  CstPattern , t2  CstPattern ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstPattern(t any) bool { return func() bool { _, ok := (t).(CstPattern); return ok }();  }; func tom_filtrage_equal_term_CstSymbolList(t1  CstSymbolList , t2  CstSymbolList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstSymbolList(t any) bool { return func() bool { _, ok := (t).(CstSymbolList); return ok }();  }; func tom_filtrage_equal_term_CstSymbol(t1  CstSymbol , t2  CstSymbol ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstSymbol(t any) bool { return func() bool { _, ok := (t).(CstSymbol); return ok }();  }; func tom_filtrage_equal_term_CstTheory(t1  CstTheory , t2  CstTheory ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstTheory(t any) bool { return func() bool { _, ok := (t).(CstTheory); return ok }();  }; func tom_filtrage_equal_term_CstPatternList(t1  CstPatternList , t2  CstPatternList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstPatternList(t any) bool { return func() bool { _, ok := (t).(CstPatternList); return ok }();  }; func tom_filtrage_equal_term_CstPairPatternList(t1  CstPairPatternList , t2  CstPairPatternList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstPairPatternList(t any) bool { return func() bool { _, ok := (t).(CstPairPatternList); return ok }();  }; func tom_filtrage_equal_term_CstPairPattern(t1  CstPairPattern , t2  CstPairPattern ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstPairPattern(t any) bool { return func() bool { _, ok := (t).(CstPairPattern); return ok }();  }; func tom_filtrage_equal_term_CstOperatorList(t1  CstOperatorList , t2  CstOperatorList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstOperatorList(t any) bool { return func() bool { _, ok := (t).(CstOperatorList); return ok }();  }; func tom_filtrage_equal_term_CstOperator(t1  CstOperator , t2  CstOperator ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstOperator(t any) bool { return func() bool { _, ok := (t).(CstOperator); return ok }();  }; func tom_filtrage_equal_term_CstSlotList(t1  CstSlotList , t2  CstSlotList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstSlotList(t any) bool { return func() bool { _, ok := (t).(CstSlotList); return ok }();  }; func tom_filtrage_equal_term_CstSlot(t1  CstSlot , t2  CstSlot ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstSlot(t any) bool { return func() bool { _, ok := (t).(CstSlot); return ok }();  }; func tom_filtrage_equal_term_CstBQTerm(t1  CstBQTerm , t2  CstBQTerm ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstBQTerm(t any) bool { return func() bool { _, ok := (t).(CstBQTerm); return ok }();  }; func tom_filtrage_equal_term_CstPairSlotBQTerm(t1  CstPairSlotBQTerm , t2  CstPairSlotBQTerm ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstPairSlotBQTerm(t any) bool { return func() bool { _, ok := (t).(CstPairSlotBQTerm); return ok }();  }; func tom_filtrage_equal_term_CstBQTermList(t1  CstBQTermList , t2  CstBQTermList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstBQTermList(t any) bool { return func() bool { _, ok := (t).(CstBQTermList); return ok }();  }; func tom_filtrage_equal_term_CstPairSlotBQTermList(t1  CstPairSlotBQTermList , t2  CstPairSlotBQTermList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstPairSlotBQTermList(t any) bool { return func() bool { _, ok := (t).(CstPairSlotBQTermList); return ok }();  }; func tom_filtrage_equal_term_CstName(t1  CstName , t2  CstName ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstName(t any) bool { return func() bool { _, ok := (t).(CstName); return ok }();  }; func tom_filtrage_equal_term_CstNameList(t1  CstNameList , t2  CstNameList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstNameList(t any) bool { return func() bool { _, ok := (t).(CstNameList); return ok }();  }; func tom_filtrage_equal_term_CstOptionList(t1  CstOptionList , t2  CstOptionList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstOptionList(t any) bool { return func() bool { _, ok := (t).(CstOptionList); return ok }();  }; func tom_filtrage_equal_term_CstOption(t1  CstOption , t2  CstOption ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstOption(t any) bool { return func() bool { _, ok := (t).(CstOption); return ok }();  }; func tom_filtrage_equal_term_CstVisit(t1  CstVisit , t2  CstVisit ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstVisit(t any) bool { return func() bool { _, ok := (t).(CstVisit); return ok }();  }; func tom_filtrage_equal_term_CstVisitList(t1  CstVisitList , t2  CstVisitList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CstVisitList(t any) bool { return func() bool { _, ok := (t).(CstVisitList); return ok }();  }; func tom_filtrage_equal_term_TargetLanguage(t1  TargetLanguage , t2  TargetLanguage ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_TargetLanguage(t any) bool { return func() bool { _, ok := (t).(TargetLanguage); return ok }();  }; func tom_filtrage_equal_term_CodeList(t1  CodeList , t2  CodeList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CodeList(t any) bool { return func() bool { _, ok := (t).(CodeList); return ok }();  }; func tom_filtrage_equal_term_Code(t1  Code , t2  Code ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_Code(t any) bool { return func() bool { _, ok := (t).(Code); return ok }();  }; func tom_filtrage_equal_term_BQTermList(t1  BQTermList , t2  BQTermList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_BQTermList(t any) bool { return func() bool { _, ok := (t).(BQTermList); return ok }();  }; func tom_filtrage_equal_term_CompositeMember(t1  CompositeMember , t2  CompositeMember ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_CompositeMember(t any) bool { return func() bool { _, ok := (t).(CompositeMember); return ok }();  }; func tom_filtrage_equal_term_BQTerm(t1  BQTerm , t2  BQTerm ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_BQTerm(t any) bool { return func() bool { _, ok := (t).(BQTerm); return ok }();  }; func tom_filtrage_is_fun_sym_BQVariable(t  BQTerm ) bool { return func() bool { _, ok := (t).(*BQVariableBQTerm); return ok }();  }; func tom_filtrage_get_slot_BQVariable_Options(t  BQTerm )  OptionList  { return (t).(*BQVariableBQTerm).Options;  }; func tom_filtrage_get_slot_BQVariable_AstName(t  BQTerm )  TomName  { return (t).(*BQVariableBQTerm).AstName;  }; func tom_filtrage_get_slot_BQVariable_AstType(t  BQTerm )  TomType  { return (t).(*BQVariableBQTerm).AstType;  }; func tom_filtrage_equal_term_Symbol(t1  Symbol , t2  Symbol ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_Symbol(t any) bool { return func() bool { _, ok := (t).(Symbol); return ok }();  }; func tom_filtrage_equal_term_Variable(t1  Variable , t2  Variable ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_Variable(t any) bool { return func() bool { _, ok := (t).(Variable); return ok }();  }; func tom_filtrage_equal_term_AbsTerm(t1  AbsTerm , t2  AbsTerm ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_AbsTerm(t any) bool { return func() bool { _, ok := (t).(AbsTerm); return ok }();  }; func tom_filtrage_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_filtrage_equal_term_Expr(t1  Expr , t2  Expr ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_Expr(t any) bool { return func() bool { _, ok := (t).(Expr); return ok }();  }; func tom_filtrage_equal_term_Instr(t1  Instr , t2  Instr ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_Instr(t any) bool { return func() bool { _, ok := (t).(Instr); return ok }();  }; func tom_filtrage_equal_term_InstrList(t1  InstrList , t2  InstrList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_InstrList(t any) bool { return func() bool { _, ok := (t).(InstrList); return ok }();  }; func tom_filtrage_equal_term_Substitution(t1  Substitution , t2  Substitution ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_Substitution(t any) bool { return func() bool { _, ok := (t).(Substitution); return ok }();  }; func tom_filtrage_equal_term_SubstitutionList(t1  SubstitutionList , t2  SubstitutionList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_SubstitutionList(t any) bool { return func() bool { _, ok := (t).(SubstitutionList); return ok }();  }; func tom_filtrage_equal_term_Environment(t1  Environment , t2  Environment ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_Environment(t any) bool { return func() bool { _, ok := (t).(Environment); return ok }();  }; func tom_filtrage_equal_term_Seq(t1  Seq , t2  Seq ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_Seq(t any) bool { return func() bool { _, ok := (t).(Seq); return ok }();  }; func tom_filtrage_equal_term_TermList(t1  TermList , t2  TermList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_TermList(t any) bool { return func() bool { _, ok := (t).(TermList); return ok }();  }; func tom_filtrage_equal_term_ExprList(t1  ExprList , t2  ExprList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_ExprList(t any) bool { return func() bool { _, ok := (t).(ExprList); return ok }();  }; func tom_filtrage_equal_term_Deriv(t1  Deriv , t2  Deriv ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_Deriv(t any) bool { return func() bool { _, ok := (t).(Deriv); return ok }();  }; func tom_filtrage_equal_term_DerivTree(t1  DerivTree , t2  DerivTree ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_DerivTree(t any) bool { return func() bool { _, ok := (t).(DerivTree); return ok }();  }; func tom_filtrage_equal_term_ElementaryTheory(t1  ElementaryTheory , t2  ElementaryTheory ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_ElementaryTheory(t any) bool { return func() bool { _, ok := (t).(ElementaryTheory); return ok }();  }; func tom_filtrage_equal_term_Theory(t1  Theory , t2  Theory ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_Theory(t any) bool { return func() bool { _, ok := (t).(Theory); return ok }();  }; func tom_filtrage_equal_term_Constraint(t1  Constraint , t2  Constraint ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_Constraint(t any) bool { return func() bool { _, ok := (t).(Constraint); return ok }();  }; func tom_filtrage_equal_term_NumericConstraintType(t1  NumericConstraintType , t2  NumericConstraintType ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_NumericConstraintType(t any) bool { return func() bool { _, ok := (t).(NumericConstraintType); return ok }();  }; func tom_filtrage_equal_term_ConstraintList(t1  ConstraintList , t2  ConstraintList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_ConstraintList(t any) bool { return func() bool { _, ok := (t).(ConstraintList); return ok }();  }; func tom_filtrage_equal_term_Declaration(t1  Declaration , t2  Declaration ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_Declaration(t any) bool { return func() bool { _, ok := (t).(Declaration); return ok }();  }; func tom_filtrage_equal_term_DeclarationList(t1  DeclarationList , t2  DeclarationList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_DeclarationList(t any) bool { return func() bool { _, ok := (t).(DeclarationList); return ok }();  }; func tom_filtrage_equal_term_Expression(t1  Expression , t2  Expression ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_Expression(t any) bool { return func() bool { _, ok := (t).(Expression); return ok }();  }; func tom_filtrage_equal_term_Instruction(t1  Instruction , t2  Instruction ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_Instruction(t any) bool { return func() bool { _, ok := (t).(Instruction); return ok }();  }; func tom_filtrage_equal_term_RefClassTracelinkInstruction(t1  RefClassTracelinkInstruction , t2  RefClassTracelinkInstruction ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_RefClassTracelinkInstruction(t any) bool { return func() bool { _, ok := (t).(RefClassTracelinkInstruction); return ok }();  }; func tom_filtrage_equal_term_RefClassTracelinkInstructionList(t1  RefClassTracelinkInstructionList , t2  RefClassTracelinkInstructionList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_RefClassTracelinkInstructionList(t any) bool { return func() bool { _, ok := (t).(RefClassTracelinkInstructionList); return ok }();  }; func tom_filtrage_equal_term_ConstraintInstruction(t1  ConstraintInstruction , t2  ConstraintInstruction ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_ConstraintInstruction(t any) bool { return func() bool { _, ok := (t).(ConstraintInstruction); return ok }();  }; func tom_filtrage_equal_term_RuleInstruction(t1  RuleInstruction , t2  RuleInstruction ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_RuleInstruction(t any) bool { return func() bool { _, ok := (t).(RuleInstruction); return ok }();  }; func tom_filtrage_equal_term_RuleInstructionList(t1  RuleInstructionList , t2  RuleInstructionList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_RuleInstructionList(t any) bool { return func() bool { _, ok := (t).(RuleInstructionList); return ok }();  }; func tom_filtrage_equal_term_ConstraintInstructionList(t1  ConstraintInstructionList , t2  ConstraintInstructionList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_ConstraintInstructionList(t any) bool { return func() bool { _, ok := (t).(ConstraintInstructionList); return ok }();  }; func tom_filtrage_equal_term_InstructionList(t1  InstructionList , t2  InstructionList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_InstructionList(t any) bool { return func() bool { _, ok := (t).(InstructionList); return ok }();  }; func tom_filtrage_equal_term_TomName(t1  TomName , t2  TomName ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_TomName(t any) bool { return func() bool { _, ok := (t).(TomName); return ok }();  }; func tom_filtrage_equal_term_TomNumber(t1  TomNumber , t2  TomNumber ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_TomNumber(t any) bool { return func() bool { _, ok := (t).(TomNumber); return ok }();  }; func tom_filtrage_equal_term_TomNumberList(t1  TomNumberList , t2  TomNumberList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_TomNumberList(t any) bool { return func() bool { _, ok := (t).(TomNumberList); return ok }();  }; func tom_filtrage_equal_term_TomNameList(t1  TomNameList , t2  TomNameList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_TomNameList(t any) bool { return func() bool { _, ok := (t).(TomNameList); return ok }();  }; func tom_filtrage_equal_term_Option(t1  Option , t2  Option ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_Option(t any) bool { return func() bool { _, ok := (t).(Option); return ok }();  }; func tom_filtrage_equal_term_OptionList(t1  OptionList , t2  OptionList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_OptionList(t any) bool { return func() bool { _, ok := (t).(OptionList); return ok }();  }; func tom_filtrage_equal_term_TextPosition(t1  TextPosition , t2  TextPosition ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_TextPosition(t any) bool { return func() bool { _, ok := (t).(TextPosition); return ok }();  }; func tom_filtrage_equal_term_TomVisit(t1  TomVisit , t2  TomVisit ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_TomVisit(t any) bool { return func() bool { _, ok := (t).(TomVisit); return ok }();  }; func tom_filtrage_equal_term_ResolveStratElement(t1  ResolveStratElement , t2  ResolveStratElement ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_ResolveStratElement(t any) bool { return func() bool { _, ok := (t).(ResolveStratElement); return ok }();  }; func tom_filtrage_equal_term_ResolveStratElementList(t1  ResolveStratElementList , t2  ResolveStratElementList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_ResolveStratElementList(t any) bool { return func() bool { _, ok := (t).(ResolveStratElementList); return ok }();  }; func tom_filtrage_equal_term_ResolveStratBlock(t1  ResolveStratBlock , t2  ResolveStratBlock ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_ResolveStratBlock(t any) bool { return func() bool { _, ok := (t).(ResolveStratBlock); return ok }();  }; func tom_filtrage_equal_term_ResolveStratBlockList(t1  ResolveStratBlockList , t2  ResolveStratBlockList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_ResolveStratBlockList(t any) bool { return func() bool { _, ok := (t).(ResolveStratBlockList); return ok }();  }; func tom_filtrage_equal_term_ElementaryTransformation(t1  ElementaryTransformation , t2  ElementaryTransformation ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_ElementaryTransformation(t any) bool { return func() bool { _, ok := (t).(ElementaryTransformation); return ok }();  }; func tom_filtrage_equal_term_ElementaryTransformationList(t1  ElementaryTransformationList , t2  ElementaryTransformationList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_ElementaryTransformationList(t any) bool { return func() bool { _, ok := (t).(ElementaryTransformationList); return ok }();  }; func tom_filtrage_equal_term_TomSymbol(t1  TomSymbol , t2  TomSymbol ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_TomSymbol(t any) bool { return func() bool { _, ok := (t).(TomSymbol); return ok }();  }; func tom_filtrage_equal_term_TomSymbolTable(t1  TomSymbolTable , t2  TomSymbolTable ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_TomSymbolTable(t any) bool { return func() bool { _, ok := (t).(TomSymbolTable); return ok }();  }; func tom_filtrage_equal_term_TomEntry(t1  TomEntry , t2  TomEntry ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_TomEntry(t any) bool { return func() bool { _, ok := (t).(TomEntry); return ok }();  }; func tom_filtrage_equal_term_TomStructureTable(t1  TomStructureTable , t2  TomStructureTable ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_TomStructureTable(t any) bool { return func() bool { _, ok := (t).(TomStructureTable); return ok }();  }; func tom_filtrage_equal_term_TomVisitList(t1  TomVisitList , t2  TomVisitList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_TomVisitList(t any) bool { return func() bool { _, ok := (t).(TomVisitList); return ok }();  }; func tom_filtrage_equal_term_TomEntryList(t1  TomEntryList , t2  TomEntryList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_TomEntryList(t any) bool { return func() bool { _, ok := (t).(TomEntryList); return ok }();  }; func tom_filtrage_equal_term_TomSymbolList(t1  TomSymbolList , t2  TomSymbolList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_TomSymbolList(t any) bool { return func() bool { _, ok := (t).(TomSymbolList); return ok }();  }; func tom_filtrage_equal_term_KeyEntry(t1  KeyEntry , t2  KeyEntry ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_KeyEntry(t any) bool { return func() bool { _, ok := (t).(KeyEntry); return ok }();  }; func tom_filtrage_equal_term_TransfoStratInfo(t1  TransfoStratInfo , t2  TransfoStratInfo ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_TransfoStratInfo(t any) bool { return func() bool { _, ok := (t).(TransfoStratInfo); return ok }();  }; func tom_filtrage_equal_term_Slot(t1  Slot , t2  Slot ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_Slot(t any) bool { return func() bool { _, ok := (t).(Slot); return ok }();  }; func tom_filtrage_equal_term_SlotList(t1  SlotList , t2  SlotList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_SlotList(t any) bool { return func() bool { _, ok := (t).(SlotList); return ok }();  }; func tom_filtrage_equal_term_BQSlot(t1  BQSlot , t2  BQSlot ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_BQSlot(t any) bool { return func() bool { _, ok := (t).(BQSlot); return ok }();  }; func tom_filtrage_equal_term_BQSlotList(t1  BQSlotList , t2  BQSlotList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_BQSlotList(t any) bool { return func() bool { _, ok := (t).(BQSlotList); return ok }();  }; func tom_filtrage_equal_term_PairNameDecl(t1  PairNameDecl , t2  PairNameDecl ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_PairNameDecl(t any) bool { return func() bool { _, ok := (t).(PairNameDecl); return ok }();  }; func tom_filtrage_equal_term_PairNameDeclList(t1  PairNameDeclList , t2  PairNameDeclList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_PairNameDeclList(t any) bool { return func() bool { _, ok := (t).(PairNameDeclList); return ok }();  }; func tom_filtrage_equal_term_TomTerm(t1  TomTerm , t2  TomTerm ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_TomTerm(t any) bool { return func() bool { _, ok := (t).(TomTerm); return ok }();  }; func tom_filtrage_equal_term_TomList(t1  TomList , t2  TomList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_TomList(t any) bool { return func() bool { _, ok := (t).(TomList); return ok }();  }; func tom_filtrage_equal_term_TomType(t1  TomType , t2  TomType ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_TomType(t any) bool { return func() bool { _, ok := (t).(TomType); return ok }();  }; func tom_filtrage_equal_term_TypeOption(t1  TypeOption , t2  TypeOption ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_TypeOption(t any) bool { return func() bool { _, ok := (t).(TypeOption); return ok }();  }; func tom_filtrage_equal_term_TypeOptionList(t1  TypeOptionList , t2  TypeOptionList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_TypeOptionList(t any) bool { return func() bool { _, ok := (t).(TypeOptionList); return ok }();  }; func tom_filtrage_equal_term_TargetLanguageType(t1  TargetLanguageType , t2  TargetLanguageType ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_TargetLanguageType(t any) bool { return func() bool { _, ok := (t).(TargetLanguageType); return ok }();  }; func tom_filtrage_equal_term_TomTypeList(t1  TomTypeList , t2  TomTypeList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_TomTypeList(t any) bool { return func() bool { _, ok := (t).(TomTypeList); return ok }();  }; func tom_filtrage_is_fun_sym_TLType(t  TargetLanguageType ) bool { return func() bool { _, ok := (t).(*TLTypeTargetLanguageType); return ok }();  }; func tom_filtrage_get_slot_TLType_String(t  TargetLanguageType )  string  { return (t).(*TLTypeTargetLanguageType).String_;  }; func tom_filtrage_equal_term_Info(t1  Info , t2  Info ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_Info(t any) bool { return func() bool { _, ok := (t).(Info); return ok }();  }; func tom_filtrage_equal_term_TypeConstraint(t1  TypeConstraint , t2  TypeConstraint ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_TypeConstraint(t any) bool { return func() bool { _, ok := (t).(TypeConstraint); return ok }();  }; func tom_filtrage_equal_term_TypeConstraintList(t1  TypeConstraintList , t2  TypeConstraintList ) bool { return (t1).Equivalent(t2);  }; func tom_filtrage_is_sort_TypeConstraintList(t any) bool { return func() bool { _, ok := (t).(TypeConstraintList); return ok }();  }; 




func (g *goGenerator) buildExpGreaterThan(deep int, exp1, exp2 Expression, moduleName string) {
	deep = 0 
	g.generateExpression(deep, exp1, moduleName)
	g.output.Write(" > ")
	g.generateExpression(deep, exp2, moduleName)
}




func (g *goGenerator) buildExpGreaterOrEqualThan(deep int, exp1, exp2 Expression, moduleName string) {
	deep = 0 
	g.generateExpression(deep, exp1, moduleName)
	g.output.Write(" >= ")
	g.generateExpression(deep, exp2, moduleName)
}




func (g *goGenerator) buildExpLessThan(deep int, exp1, exp2 Expression, moduleName string) {
	deep = 0 
	g.generateExpression(deep, exp1, moduleName)
	g.output.Write(" < ")
	g.generateExpression(deep, exp2, moduleName)
}




func (g *goGenerator) buildExpLessOrEqualThan(deep int, exp1, exp2 Expression, moduleName string) {
	deep = 0 
	g.generateExpression(deep, exp1, moduleName)
	g.output.Write(" <= ")
	g.generateExpression(deep, exp2, moduleName)
}




func (g *goGenerator) buildAddOne(deep int, v BQTerm, moduleName string) {
	g.generateBQTerm(deep, v, moduleName)
	g.output.Write(" + 1")
}




func (g *goGenerator) buildSubstractOne(deep int, v BQTerm, moduleName string) {
	g.generateBQTerm(0, v, moduleName)
	g.output.Write(" - 1")
}




func (g *goGenerator) buildSubstract(deep int, v1, v2 BQTerm, moduleName string) {
	g.generateBQTerm(0, v1, moduleName)
	g.output.Write(" - ")
	g.generateBQTerm(0, v2, moduleName)
}






func (g *goGenerator) buildAssign(deep int, v BQTerm, optionList OptionList, exp Expression, moduleName string) {
	g.generateBQTerm(deep, v, moduleName)
	g.output.Write(" = ")
	var varType *string
	; { _ = v; { if tom_filtrage_is_sort_BQTerm(v) { if tom_filtrage_is_fun_sym_BQVariable(any(v).( BQTerm )) { 

			s, ok := g.knownGoType(tom_filtrage_get_slot_BQVariable_AstType(any(v).( BQTerm )), moduleName)
			varType = optString(s, ok)
		;  };  };  };  }; 

	g.generateTypedExpression(deep, varType, exp, moduleName)
	g.output.Write("; ")
}




func (g *goGenerator) buildDoWhile(deep int, succes Instruction, exp Expression, moduleName string) {
	g.output.Write("for { ")
	g.generateInstruction(deep+1, succes, moduleName)
	g.output.Write(" if !(")
	g.generateExpression(deep, exp, moduleName)
	g.output.Write(") { break; }; }; ")
}




func (g *goGenerator) buildWhileDo(deep int, exp Expression, succes Instruction, moduleName string) {
	g.output.Write("for ")
	g.generateExpression(deep, exp, moduleName)
	g.output.Write(" { ")
	g.generateInstruction(deep+1, succes, moduleName)
	g.output.Write(" }; ")
}




func (g *goGenerator) buildIf(deep int, exp Expression, succes Instruction, moduleName string) {
	g.output.Write("if ")
	g.generateExpression(deep, exp, moduleName)
	g.output.Write(" { ")
	g.generateInstruction(deep+1, succes, moduleName)
	g.output.Write(" }; ")
}




func (g *goGenerator) buildIfWithFailure(deep int, exp Expression, succes, failure Instruction, moduleName string) {
	g.output.Write("if ")
	g.generateExpression(deep, exp, moduleName)
	g.output.Write(" { ")
	g.generateInstruction(deep+1, succes, moduleName)
	g.output.Write(" } else { ")
	g.generateInstruction(deep+1, failure, moduleName)
	g.output.Write(" }; ")
}










func (g *goGenerator) buildLet(deep int, v BQTerm, optionList OptionList, tlType TargetLanguageType, exp Expression, body Instruction, moduleName string) {
	g.output.Write("var ")
	name := g.getVariableNameBQAbstract(v)
	g.output.WriteDeep(deep, name)
	letType := g.goTLCode(tlType)
	g.output.Write(" " + letType + " = ")
	g.generateTypedExpression(deep, &letType, exp, moduleName)
	g.output.Write("; _ = " + name + "; ")
	shadows := g.classFields[name]
	delete(g.classFields, name)
	g.generateInstruction(deep, body, moduleName)
	if shadows {
		g.classFields[name] = true
	}
}




func (g *goGenerator) buildLetRef(deep int, v BQTerm, optionList OptionList, tlType TargetLanguageType, exp Expression, body Instruction, moduleName string) {
	g.buildLet(deep, v, optionList, tlType, exp, body, moduleName)
}






func (g *goGenerator) buildNamedBlock(deep int, blockName string, instList InstructionList, moduleName string) {
	g.output.Write(blockName + ": for { ")
	g.generateInstructionList(deep+1, instList, moduleName)
	g.output.Write(" break " + blockName + "; }; ")
}






func (g *goGenerator) buildUnamedBlock(deep int, instList InstructionList, moduleName string) {
	g.output.Write("{ ")
	g.generateInstructionList(deep+1, instList, moduleName)
	g.output.Write(" }; ")
}




func (g *goGenerator) buildExpTrue(deep int) {
	g.output.Write(" true ")
}




func (g *goGenerator) buildExpFalse(deep int) {
	g.output.Write(" false ")
}






func (g *goGenerator) buildExpBottom(deep int, ty TomType, moduleName string) {
	st := g.getSymbolTable(moduleName)
	sType, ok := tools.GetTomType(ty)
	if ok && (st.IsIntType(sType) || st.IsLongType(sType) || st.IsCharType(sType) ||
		st.IsFloatType(sType) || st.IsDoubleType(sType)) {
		g.output.Write(" 0 ")
	} else if ok && st.IsBooleanType(sType) {
		g.output.Write(" false ")
	} else if ok && st.IsStringType(sType) {
		g.output.Write(" \"\" ")
	} else {
		
		goT, known := g.knownGoType(ty, moduleName)
		if !known || javaTrim(goT) == "" {
			g.output.Write(" nil ")
		} else {
			g.output.Write(" *new(" + javaTrim(goT) + ") ")
		}
	}
}




func (g *goGenerator) buildExpNegation(deep int, exp Expression, moduleName string) {
	g.output.Write("!(")
	g.generateExpression(deep, exp, moduleName)
	g.output.Write(")")
}




func (g *goGenerator) buildExpAnd(deep int, exp1, exp2 Expression, moduleName string) {
	g.output.Write(" (")
	g.generateExpression(deep, exp1, moduleName)
	g.output.Write(" && ")
	g.generateExpression(deep, exp2, moduleName)
	g.output.Write(") ")
}




func (g *goGenerator) buildExpOr(deep int, exp1, exp2 Expression, moduleName string) {
	g.output.Write(" (")
	g.generateExpression(deep, exp1, moduleName)
	g.output.Write(" || ")
	g.generateExpression(deep, exp2, moduleName)
	g.output.Write(") ")
}










func (g *goGenerator) buildExpConditional(deep int, cond, exp1, exp2 Expression, moduleName string) {
	ty, ok := g.expressionGoType(exp2, moduleName)
	if !ok {
		ty, ok = g.expressionGoType(exp1, moduleName)
	}
	if !ok && g.conditionalType != nil {
		ty, ok = *g.conditionalType, true
	}
	if !ok || javaTrim(ty) == "" {
		ty = "any"
	}
	g.output.Write("func() " + ty + " { if ")
	g.generateExpression(deep, cond, moduleName)
	g.output.Write(" { return ")
	g.generateExpression(deep, exp1, moduleName)
	g.output.Write(" }; return ")
	g.generateExpression(deep, exp2, moduleName)
	g.output.Write(" }()")
}






func (g *goGenerator) buildExpCast(deep int, tlType TargetLanguageType, exp Expression, moduleName string) {
	; { _ = tlType; { if tom_filtrage_is_sort_TargetLanguageType(tlType) { if tom_filtrage_is_fun_sym_TLType(any(tlType).( TargetLanguageType )) { 

			g.output.Write("any(")
			g.generateExpression(deep, exp, moduleName)
			g.output.Write(").(" + tom_filtrage_get_slot_TLType_String(any(tlType).( TargetLanguageType )) + ")")
			return
		;  };  };  };  }; 

	g.generateExpression(deep, exp, moduleName)
}






func (g *goGenerator) useNativeEquality(sType string, ok bool, moduleName string) bool {
	st := g.getSymbolTable(moduleName)
	return !ok || st.IsUnknownType(sType) || st.IsBooleanType(sType)
}




func (g *goGenerator) buildExpEqualTerm(deep int, ty TomType, exp1 BQTerm, exp2 TomTerm, moduleName string) {
	sType, ok := tools.GetTomType(ty)
	if g.useNativeEquality(sType, ok, moduleName) {
		g.output.Write("(")
		g.generateBQTerm(deep, exp1, moduleName)
		g.output.Write(" == ")
		g.generateTomTerm(deep, exp2, moduleName)
		g.output.Write(")")
		return
	}
	t1, ok1 := g.termTypeBQ(exp1, moduleName)
	other := g.otherSort(sType, t1, ok1, moduleName)
	if !other {
		t2, ok2 := g.termType(exp2, moduleName)
		other = g.otherSort(sType, t2, ok2, moduleName)
	}
	if other {
		goSort := g.sortGoType(sType, moduleName)
		g.output.Write("func() bool { tom_a, tom_ok1 := any(")
		g.generateBQTerm(deep, exp1, moduleName)
		g.output.Write(").(" + goSort + "); tom_b, tom_ok2 := any(")
		g.generateTomTerm(deep, exp2, moduleName)
		g.output.Write(").(" + goSort + "); return tom_ok1 && tom_ok2 && " + g.declName("tom_equal_term_"+sType) + "(tom_a, tom_b) }()")
	} else {
		g.output.Write(g.declName("tom_equal_term_"+sType) + "(")
		g.generateBQTerm(deep, exp1, moduleName)
		g.output.Write(", ")
		g.generateTomTerm(deep, exp2, moduleName)
		g.output.Write(")")
	}
}




func (g *goGenerator) buildExpEqualBQTerm(deep int, ty TomType, exp1, exp2 BQTerm, moduleName string) {
	sType, ok := tools.GetTomType(ty)
	if g.useNativeEquality(sType, ok, moduleName) {
		g.output.Write("(")
		g.generateBQTerm(deep, exp1, moduleName)
		g.output.Write(" == ")
		g.generateBQTerm(deep, exp2, moduleName)
		g.output.Write(")")
		return
	}
	t1, ok1 := g.termTypeBQ(exp1, moduleName)
	other := g.otherSort(sType, t1, ok1, moduleName)
	if !other {
		t2, ok2 := g.termTypeBQ(exp2, moduleName)
		other = g.otherSort(sType, t2, ok2, moduleName)
	}
	if other {
		goSort := g.sortGoType(sType, moduleName)
		g.output.Write("func() bool { tom_a, tom_ok1 := any(")
		g.generateBQTerm(deep, exp1, moduleName)
		g.output.Write(").(" + goSort + "); tom_b, tom_ok2 := any(")
		g.generateBQTerm(deep, exp2, moduleName)
		g.output.Write(").(" + goSort + "); return tom_ok1 && tom_ok2 && " + g.declName("tom_equal_term_"+sType) + "(tom_a, tom_b) }()")
	} else {
		g.output.Write(g.declName("tom_equal_term_"+sType) + "(")
		g.generateBQTerm(deep, exp1, moduleName)
		g.output.Write(", ")
		g.generateBQTerm(deep, exp2, moduleName)
		g.output.Write(")")
	}
}








func (g *goGenerator) termTypeBQ(t BQTerm, moduleName string) (sort string, ok bool) {
	normal := platform.CatchRuntime(func() {
		sort, ok = tools.GetTomType(tools.GetTermTypeBQ(t, g.getSymbolTable(moduleName)))
	})
	if !normal {
		return "", false
	}
	return sort, ok
}




func (g *goGenerator) termType(t TomTerm, moduleName string) (sort string, ok bool) {
	normal := platform.CatchRuntime(func() {
		sort, ok = tools.GetTomType(tools.GetTermType(t, g.getSymbolTable(moduleName)))
	})
	if !normal {
		return "", false
	}
	return sort, ok
}








func (g *goGenerator) otherSort(sType string, argSort string, hasArgSort bool, moduleName string) bool {
	st := g.getSymbolTable(moduleName)
	return hasArgSort && argSort != sType && !st.IsUnknownType(argSort) &&
		!st.IsBuiltinType(sType) && st.GetType(sType) != nil && st.GetType(argSort) != nil
}




var _ = aterm.GetInstance

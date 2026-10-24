// tom-source-sha256: a270f6635ff53780f05551388a34722440746a6326a85819034e8166edbd88eb

package tools










import (
	"fmt"
	"sort"

	. "tom/engine/adt"
	"tom/engine/bridge"
	"tom/engine/platform"
	"tom/library/aterm"
)

func tom_symboltable_equal_term_char(t1 rune, t2 rune) bool { return t1 == t2;  }; func tom_symboltable_is_sort_char(t rune) bool { return true;  }; func tom_symboltable_equal_term_String(t1 string, t2 string) bool { return t1 == t2;  }; func tom_symboltable_is_sort_String(t string) bool { return true;  }; func tom_symboltable_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_symboltable_is_sort_int(t int64) bool { return true;  }; func tom_symboltable_equal_term_boolean(t1 bool, t2 bool) bool { return t1 == t2;  }; func tom_symboltable_is_sort_boolean(t bool) bool { return true;  }; func tom_symboltable_equal_term_ATerm(t1  aterm.ATerm , t2  aterm.ATerm ) bool { return t1 == t2;  }; func tom_symboltable_is_sort_ATerm(t any) bool { return func() bool { _, ok := any(t).(aterm.ATerm); return ok }();  }; func tom_symboltable_equal_term_ATermList(l1  *aterm.ATermList , l2  *aterm.ATermList ) bool { return l1 == l2;  }; func tom_symboltable_is_sort_ATermList(t any) bool { return func() bool { _, ok := any(t).(*aterm.ATermList); return ok }();  }; func tom_symboltable_equal_term_AFun(t1  *aterm.AFun , t2  *aterm.AFun ) bool { return t1 == t2;  }; func tom_symboltable_is_sort_AFun(t any) bool { return func() bool { _, ok := any(t).(*aterm.AFun); return ok }();  }; func tom_symboltable_equal_term_CstProgram(t1  CstProgram , t2  CstProgram ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstProgram(t any) bool { return func() bool { _, ok := (t).(CstProgram); return ok }();  }; func tom_symboltable_equal_term_CstBlockList(t1  CstBlockList , t2  CstBlockList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstBlockList(t any) bool { return func() bool { _, ok := (t).(CstBlockList); return ok }();  }; func tom_symboltable_equal_term_CstBlock(t1  CstBlock , t2  CstBlock ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstBlock(t any) bool { return func() bool { _, ok := (t).(CstBlock); return ok }();  }; func tom_symboltable_equal_term_CstConstraintAction(t1  CstConstraintAction , t2  CstConstraintAction ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstConstraintAction(t any) bool { return func() bool { _, ok := (t).(CstConstraintAction); return ok }();  }; func tom_symboltable_equal_term_CstConstraintActionList(t1  CstConstraintActionList , t2  CstConstraintActionList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstConstraintActionList(t any) bool { return func() bool { _, ok := (t).(CstConstraintActionList); return ok }();  }; func tom_symboltable_equal_term_CstConstraint(t1  CstConstraint , t2  CstConstraint ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstConstraint(t any) bool { return func() bool { _, ok := (t).(CstConstraint); return ok }();  }; func tom_symboltable_equal_term_CstConstraintList(t1  CstConstraintList , t2  CstConstraintList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstConstraintList(t any) bool { return func() bool { _, ok := (t).(CstConstraintList); return ok }();  }; func tom_symboltable_equal_term_CstTerm(t1  CstTerm , t2  CstTerm ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstTerm(t any) bool { return func() bool { _, ok := (t).(CstTerm); return ok }();  }; func tom_symboltable_equal_term_CstTermList(t1  CstTermList , t2  CstTermList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstTermList(t any) bool { return func() bool { _, ok := (t).(CstTermList); return ok }();  }; func tom_symboltable_equal_term_CstType(t1  CstType , t2  CstType ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstType(t any) bool { return func() bool { _, ok := (t).(CstType); return ok }();  }; func tom_symboltable_equal_term_CstPattern(t1  CstPattern , t2  CstPattern ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstPattern(t any) bool { return func() bool { _, ok := (t).(CstPattern); return ok }();  }; func tom_symboltable_equal_term_CstSymbolList(t1  CstSymbolList , t2  CstSymbolList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstSymbolList(t any) bool { return func() bool { _, ok := (t).(CstSymbolList); return ok }();  }; func tom_symboltable_equal_term_CstSymbol(t1  CstSymbol , t2  CstSymbol ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstSymbol(t any) bool { return func() bool { _, ok := (t).(CstSymbol); return ok }();  }; func tom_symboltable_equal_term_CstTheory(t1  CstTheory , t2  CstTheory ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstTheory(t any) bool { return func() bool { _, ok := (t).(CstTheory); return ok }();  }; func tom_symboltable_equal_term_CstPatternList(t1  CstPatternList , t2  CstPatternList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstPatternList(t any) bool { return func() bool { _, ok := (t).(CstPatternList); return ok }();  }; func tom_symboltable_equal_term_CstPairPatternList(t1  CstPairPatternList , t2  CstPairPatternList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstPairPatternList(t any) bool { return func() bool { _, ok := (t).(CstPairPatternList); return ok }();  }; func tom_symboltable_equal_term_CstPairPattern(t1  CstPairPattern , t2  CstPairPattern ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstPairPattern(t any) bool { return func() bool { _, ok := (t).(CstPairPattern); return ok }();  }; func tom_symboltable_equal_term_CstOperatorList(t1  CstOperatorList , t2  CstOperatorList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstOperatorList(t any) bool { return func() bool { _, ok := (t).(CstOperatorList); return ok }();  }; func tom_symboltable_equal_term_CstOperator(t1  CstOperator , t2  CstOperator ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstOperator(t any) bool { return func() bool { _, ok := (t).(CstOperator); return ok }();  }; func tom_symboltable_equal_term_CstSlotList(t1  CstSlotList , t2  CstSlotList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstSlotList(t any) bool { return func() bool { _, ok := (t).(CstSlotList); return ok }();  }; func tom_symboltable_equal_term_CstSlot(t1  CstSlot , t2  CstSlot ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstSlot(t any) bool { return func() bool { _, ok := (t).(CstSlot); return ok }();  }; func tom_symboltable_equal_term_CstBQTerm(t1  CstBQTerm , t2  CstBQTerm ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstBQTerm(t any) bool { return func() bool { _, ok := (t).(CstBQTerm); return ok }();  }; func tom_symboltable_equal_term_CstPairSlotBQTerm(t1  CstPairSlotBQTerm , t2  CstPairSlotBQTerm ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstPairSlotBQTerm(t any) bool { return func() bool { _, ok := (t).(CstPairSlotBQTerm); return ok }();  }; func tom_symboltable_equal_term_CstBQTermList(t1  CstBQTermList , t2  CstBQTermList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstBQTermList(t any) bool { return func() bool { _, ok := (t).(CstBQTermList); return ok }();  }; func tom_symboltable_equal_term_CstPairSlotBQTermList(t1  CstPairSlotBQTermList , t2  CstPairSlotBQTermList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstPairSlotBQTermList(t any) bool { return func() bool { _, ok := (t).(CstPairSlotBQTermList); return ok }();  }; func tom_symboltable_equal_term_CstName(t1  CstName , t2  CstName ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstName(t any) bool { return func() bool { _, ok := (t).(CstName); return ok }();  }; func tom_symboltable_equal_term_CstNameList(t1  CstNameList , t2  CstNameList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstNameList(t any) bool { return func() bool { _, ok := (t).(CstNameList); return ok }();  }; func tom_symboltable_equal_term_CstOptionList(t1  CstOptionList , t2  CstOptionList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstOptionList(t any) bool { return func() bool { _, ok := (t).(CstOptionList); return ok }();  }; func tom_symboltable_equal_term_CstOption(t1  CstOption , t2  CstOption ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstOption(t any) bool { return func() bool { _, ok := (t).(CstOption); return ok }();  }; func tom_symboltable_equal_term_CstVisit(t1  CstVisit , t2  CstVisit ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstVisit(t any) bool { return func() bool { _, ok := (t).(CstVisit); return ok }();  }; func tom_symboltable_equal_term_CstVisitList(t1  CstVisitList , t2  CstVisitList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CstVisitList(t any) bool { return func() bool { _, ok := (t).(CstVisitList); return ok }();  }; func tom_symboltable_equal_term_TargetLanguage(t1  TargetLanguage , t2  TargetLanguage ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_TargetLanguage(t any) bool { return func() bool { _, ok := (t).(TargetLanguage); return ok }();  }; func tom_symboltable_equal_term_CodeList(t1  CodeList , t2  CodeList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CodeList(t any) bool { return func() bool { _, ok := (t).(CodeList); return ok }();  }; func tom_symboltable_equal_term_Code(t1  Code , t2  Code ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_Code(t any) bool { return func() bool { _, ok := (t).(Code); return ok }();  }; func tom_symboltable_equal_term_BQTermList(t1  BQTermList , t2  BQTermList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_BQTermList(t any) bool { return func() bool { _, ok := (t).(BQTermList); return ok }();  }; func tom_symboltable_equal_term_CompositeMember(t1  CompositeMember , t2  CompositeMember ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_CompositeMember(t any) bool { return func() bool { _, ok := (t).(CompositeMember); return ok }();  }; func tom_symboltable_equal_term_BQTerm(t1  BQTerm , t2  BQTerm ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_BQTerm(t any) bool { return func() bool { _, ok := (t).(BQTerm); return ok }();  }; func tom_symboltable_equal_term_Symbol(t1  Symbol , t2  Symbol ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_Symbol(t any) bool { return func() bool { _, ok := (t).(Symbol); return ok }();  }; func tom_symboltable_equal_term_Variable(t1  Variable , t2  Variable ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_Variable(t any) bool { return func() bool { _, ok := (t).(Variable); return ok }();  }; func tom_symboltable_equal_term_AbsTerm(t1  AbsTerm , t2  AbsTerm ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_AbsTerm(t any) bool { return func() bool { _, ok := (t).(AbsTerm); return ok }();  }; func tom_symboltable_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_symboltable_equal_term_Expr(t1  Expr , t2  Expr ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_Expr(t any) bool { return func() bool { _, ok := (t).(Expr); return ok }();  }; func tom_symboltable_equal_term_Instr(t1  Instr , t2  Instr ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_Instr(t any) bool { return func() bool { _, ok := (t).(Instr); return ok }();  }; func tom_symboltable_equal_term_InstrList(t1  InstrList , t2  InstrList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_InstrList(t any) bool { return func() bool { _, ok := (t).(InstrList); return ok }();  }; func tom_symboltable_equal_term_Substitution(t1  Substitution , t2  Substitution ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_Substitution(t any) bool { return func() bool { _, ok := (t).(Substitution); return ok }();  }; func tom_symboltable_equal_term_SubstitutionList(t1  SubstitutionList , t2  SubstitutionList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_SubstitutionList(t any) bool { return func() bool { _, ok := (t).(SubstitutionList); return ok }();  }; func tom_symboltable_equal_term_Environment(t1  Environment , t2  Environment ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_Environment(t any) bool { return func() bool { _, ok := (t).(Environment); return ok }();  }; func tom_symboltable_equal_term_Seq(t1  Seq , t2  Seq ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_Seq(t any) bool { return func() bool { _, ok := (t).(Seq); return ok }();  }; func tom_symboltable_equal_term_TermList(t1  TermList , t2  TermList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_TermList(t any) bool { return func() bool { _, ok := (t).(TermList); return ok }();  }; func tom_symboltable_equal_term_ExprList(t1  ExprList , t2  ExprList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_ExprList(t any) bool { return func() bool { _, ok := (t).(ExprList); return ok }();  }; func tom_symboltable_equal_term_Deriv(t1  Deriv , t2  Deriv ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_Deriv(t any) bool { return func() bool { _, ok := (t).(Deriv); return ok }();  }; func tom_symboltable_equal_term_DerivTree(t1  DerivTree , t2  DerivTree ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_DerivTree(t any) bool { return func() bool { _, ok := (t).(DerivTree); return ok }();  }; func tom_symboltable_equal_term_ElementaryTheory(t1  ElementaryTheory , t2  ElementaryTheory ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_ElementaryTheory(t any) bool { return func() bool { _, ok := (t).(ElementaryTheory); return ok }();  }; func tom_symboltable_equal_term_Theory(t1  Theory , t2  Theory ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_Theory(t any) bool { return func() bool { _, ok := (t).(Theory); return ok }();  }; func tom_symboltable_equal_term_Constraint(t1  Constraint , t2  Constraint ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_Constraint(t any) bool { return func() bool { _, ok := (t).(Constraint); return ok }();  }; func tom_symboltable_equal_term_NumericConstraintType(t1  NumericConstraintType , t2  NumericConstraintType ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_NumericConstraintType(t any) bool { return func() bool { _, ok := (t).(NumericConstraintType); return ok }();  }; func tom_symboltable_equal_term_ConstraintList(t1  ConstraintList , t2  ConstraintList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_ConstraintList(t any) bool { return func() bool { _, ok := (t).(ConstraintList); return ok }();  }; func tom_symboltable_equal_term_Declaration(t1  Declaration , t2  Declaration ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_Declaration(t any) bool { return func() bool { _, ok := (t).(Declaration); return ok }();  }; func tom_symboltable_equal_term_DeclarationList(t1  DeclarationList , t2  DeclarationList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_DeclarationList(t any) bool { return func() bool { _, ok := (t).(DeclarationList); return ok }();  }; func tom_symboltable_equal_term_Expression(t1  Expression , t2  Expression ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_Expression(t any) bool { return func() bool { _, ok := (t).(Expression); return ok }();  }; func tom_symboltable_equal_term_Instruction(t1  Instruction , t2  Instruction ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_Instruction(t any) bool { return func() bool { _, ok := (t).(Instruction); return ok }();  }; func tom_symboltable_equal_term_RefClassTracelinkInstruction(t1  RefClassTracelinkInstruction , t2  RefClassTracelinkInstruction ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_RefClassTracelinkInstruction(t any) bool { return func() bool { _, ok := (t).(RefClassTracelinkInstruction); return ok }();  }; func tom_symboltable_equal_term_RefClassTracelinkInstructionList(t1  RefClassTracelinkInstructionList , t2  RefClassTracelinkInstructionList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_RefClassTracelinkInstructionList(t any) bool { return func() bool { _, ok := (t).(RefClassTracelinkInstructionList); return ok }();  }; func tom_symboltable_equal_term_ConstraintInstruction(t1  ConstraintInstruction , t2  ConstraintInstruction ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_ConstraintInstruction(t any) bool { return func() bool { _, ok := (t).(ConstraintInstruction); return ok }();  }; func tom_symboltable_equal_term_RuleInstruction(t1  RuleInstruction , t2  RuleInstruction ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_RuleInstruction(t any) bool { return func() bool { _, ok := (t).(RuleInstruction); return ok }();  }; func tom_symboltable_equal_term_RuleInstructionList(t1  RuleInstructionList , t2  RuleInstructionList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_RuleInstructionList(t any) bool { return func() bool { _, ok := (t).(RuleInstructionList); return ok }();  }; func tom_symboltable_equal_term_ConstraintInstructionList(t1  ConstraintInstructionList , t2  ConstraintInstructionList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_ConstraintInstructionList(t any) bool { return func() bool { _, ok := (t).(ConstraintInstructionList); return ok }();  }; func tom_symboltable_equal_term_InstructionList(t1  InstructionList , t2  InstructionList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_InstructionList(t any) bool { return func() bool { _, ok := (t).(InstructionList); return ok }();  }; func tom_symboltable_equal_term_TomName(t1  TomName , t2  TomName ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_TomName(t any) bool { return func() bool { _, ok := (t).(TomName); return ok }();  }; func tom_symboltable_equal_term_TomNumber(t1  TomNumber , t2  TomNumber ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_TomNumber(t any) bool { return func() bool { _, ok := (t).(TomNumber); return ok }();  }; func tom_symboltable_equal_term_TomNumberList(t1  TomNumberList , t2  TomNumberList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_TomNumberList(t any) bool { return func() bool { _, ok := (t).(TomNumberList); return ok }();  }; func tom_symboltable_equal_term_TomNameList(t1  TomNameList , t2  TomNameList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_TomNameList(t any) bool { return func() bool { _, ok := (t).(TomNameList); return ok }();  }; func tom_symboltable_equal_term_Option(t1  Option , t2  Option ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_Option(t any) bool { return func() bool { _, ok := (t).(Option); return ok }();  }; func tom_symboltable_equal_term_OptionList(t1  OptionList , t2  OptionList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_OptionList(t any) bool { return func() bool { _, ok := (t).(OptionList); return ok }();  }; func tom_symboltable_equal_term_TextPosition(t1  TextPosition , t2  TextPosition ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_TextPosition(t any) bool { return func() bool { _, ok := (t).(TextPosition); return ok }();  }; func tom_symboltable_equal_term_TomVisit(t1  TomVisit , t2  TomVisit ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_TomVisit(t any) bool { return func() bool { _, ok := (t).(TomVisit); return ok }();  }; func tom_symboltable_equal_term_ResolveStratElement(t1  ResolveStratElement , t2  ResolveStratElement ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_ResolveStratElement(t any) bool { return func() bool { _, ok := (t).(ResolveStratElement); return ok }();  }; func tom_symboltable_equal_term_ResolveStratElementList(t1  ResolveStratElementList , t2  ResolveStratElementList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_ResolveStratElementList(t any) bool { return func() bool { _, ok := (t).(ResolveStratElementList); return ok }();  }; func tom_symboltable_equal_term_ResolveStratBlock(t1  ResolveStratBlock , t2  ResolveStratBlock ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_ResolveStratBlock(t any) bool { return func() bool { _, ok := (t).(ResolveStratBlock); return ok }();  }; func tom_symboltable_equal_term_ResolveStratBlockList(t1  ResolveStratBlockList , t2  ResolveStratBlockList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_ResolveStratBlockList(t any) bool { return func() bool { _, ok := (t).(ResolveStratBlockList); return ok }();  }; func tom_symboltable_equal_term_ElementaryTransformation(t1  ElementaryTransformation , t2  ElementaryTransformation ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_ElementaryTransformation(t any) bool { return func() bool { _, ok := (t).(ElementaryTransformation); return ok }();  }; func tom_symboltable_equal_term_ElementaryTransformationList(t1  ElementaryTransformationList , t2  ElementaryTransformationList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_ElementaryTransformationList(t any) bool { return func() bool { _, ok := (t).(ElementaryTransformationList); return ok }();  }; func tom_symboltable_equal_term_TomSymbol(t1  TomSymbol , t2  TomSymbol ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_TomSymbol(t any) bool { return func() bool { _, ok := (t).(TomSymbol); return ok }();  }; func tom_symboltable_equal_term_TomSymbolTable(t1  TomSymbolTable , t2  TomSymbolTable ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_TomSymbolTable(t any) bool { return func() bool { _, ok := (t).(TomSymbolTable); return ok }();  }; func tom_symboltable_equal_term_TomEntry(t1  TomEntry , t2  TomEntry ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_TomEntry(t any) bool { return func() bool { _, ok := (t).(TomEntry); return ok }();  }; func tom_symboltable_equal_term_TomStructureTable(t1  TomStructureTable , t2  TomStructureTable ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_TomStructureTable(t any) bool { return func() bool { _, ok := (t).(TomStructureTable); return ok }();  }; func tom_symboltable_equal_term_TomVisitList(t1  TomVisitList , t2  TomVisitList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_TomVisitList(t any) bool { return func() bool { _, ok := (t).(TomVisitList); return ok }();  }; func tom_symboltable_equal_term_TomEntryList(t1  TomEntryList , t2  TomEntryList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_TomEntryList(t any) bool { return func() bool { _, ok := (t).(TomEntryList); return ok }();  }; func tom_symboltable_equal_term_TomSymbolList(t1  TomSymbolList , t2  TomSymbolList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_TomSymbolList(t any) bool { return func() bool { _, ok := (t).(TomSymbolList); return ok }();  }; func tom_symboltable_equal_term_KeyEntry(t1  KeyEntry , t2  KeyEntry ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_KeyEntry(t any) bool { return func() bool { _, ok := (t).(KeyEntry); return ok }();  }; func tom_symboltable_equal_term_TransfoStratInfo(t1  TransfoStratInfo , t2  TransfoStratInfo ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_TransfoStratInfo(t any) bool { return func() bool { _, ok := (t).(TransfoStratInfo); return ok }();  }; func tom_symboltable_make_Table(EntryList  TomEntryList )  TomSymbolTable  { return (MakeTable(EntryList)) }; func tom_symboltable_make_Entry(StrName  string , AstSymbol  TomSymbol )  TomEntry  { return (MakeEntry(StrName,AstSymbol)) }; func tom_symboltable_is_fun_sym_concTomEntry(t  TomEntryList ) bool { return func() bool { _, ok := (t).(*ConcTomEntryTomEntryList); return ok }();  }; func tom_symboltable_empty_list_concTomEntry()  TomEntryList  { return (EmptyConcTomEntry()) }; func tom_symboltable_cons_list_concTomEntry(e  TomEntry , l  TomEntryList )  TomEntryList  { return (ConsConcTomEntry(e,l)) }; func tom_symboltable_get_head_concTomEntry_TomEntryList(l  TomEntryList )  TomEntry  { return (l).GetHeadconcTomEntry();  }; func tom_symboltable_get_tail_concTomEntry_TomEntryList(l  TomEntryList )  TomEntryList  { return (l).GetTailconcTomEntry();  }; func tom_symboltable_is_empty_concTomEntry_TomEntryList(l  TomEntryList ) bool { return (l).IsEmptyconcTomEntry();  }; func tom_symboltable_append_list_concTomEntry(l1  TomEntryList , l2  TomEntryList )  TomEntryList  { if tom_symboltable_is_empty_concTomEntry_TomEntryList(l1) { return l2 } else if tom_symboltable_is_empty_concTomEntry_TomEntryList(l2) { return l1 } else if tom_symboltable_is_empty_concTomEntry_TomEntryList(tom_symboltable_get_tail_concTomEntry_TomEntryList(l1)) { return tom_symboltable_cons_list_concTomEntry(tom_symboltable_get_head_concTomEntry_TomEntryList(l1), l2) } else { return tom_symboltable_cons_list_concTomEntry(tom_symboltable_get_head_concTomEntry_TomEntryList(l1), tom_symboltable_append_list_concTomEntry(tom_symboltable_get_tail_concTomEntry_TomEntryList(l1), l2)) }; }; func tom_symboltable_get_slice_concTomEntry(begin  TomEntryList , end  TomEntryList , tail  TomEntryList )  TomEntryList  { if tom_symboltable_equal_term_TomEntryList(begin, end) { return tail } else if tom_symboltable_equal_term_TomEntryList(end, tail) && (tom_symboltable_is_empty_concTomEntry_TomEntryList(end) || tom_symboltable_equal_term_TomEntryList(end, tom_symboltable_empty_list_concTomEntry())) { return begin }; return tom_symboltable_cons_list_concTomEntry(tom_symboltable_get_head_concTomEntry_TomEntryList(begin), tom_symboltable_get_slice_concTomEntry(tom_symboltable_get_tail_concTomEntry_TomEntryList(begin), end, tail)) }; func tom_symboltable_make_UsedSymbolConstructor(Symbol  TomSymbol )  KeyEntry  { return (MakeUsedSymbolConstructor(Symbol)) }; func tom_symboltable_make_UsedSymbolDestructor(Symbol  TomSymbol )  KeyEntry  { return (MakeUsedSymbolDestructor(Symbol)) }; func tom_symboltable_make_UsedSymbolAC(Symbol  TomSymbol )  KeyEntry  { return (MakeUsedSymbolAC(Symbol)) }; func tom_symboltable_make_UsedType(Typedef  TomType )  KeyEntry  { return (MakeUsedType(Typedef)) }; func tom_symboltable_equal_term_Slot(t1  Slot , t2  Slot ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_Slot(t any) bool { return func() bool { _, ok := (t).(Slot); return ok }();  }; func tom_symboltable_equal_term_SlotList(t1  SlotList , t2  SlotList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_SlotList(t any) bool { return func() bool { _, ok := (t).(SlotList); return ok }();  }; func tom_symboltable_equal_term_BQSlot(t1  BQSlot , t2  BQSlot ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_BQSlot(t any) bool { return func() bool { _, ok := (t).(BQSlot); return ok }();  }; func tom_symboltable_equal_term_BQSlotList(t1  BQSlotList , t2  BQSlotList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_BQSlotList(t any) bool { return func() bool { _, ok := (t).(BQSlotList); return ok }();  }; func tom_symboltable_equal_term_PairNameDecl(t1  PairNameDecl , t2  PairNameDecl ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_PairNameDecl(t any) bool { return func() bool { _, ok := (t).(PairNameDecl); return ok }();  }; func tom_symboltable_equal_term_PairNameDeclList(t1  PairNameDeclList , t2  PairNameDeclList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_PairNameDeclList(t any) bool { return func() bool { _, ok := (t).(PairNameDeclList); return ok }();  }; func tom_symboltable_equal_term_TomTerm(t1  TomTerm , t2  TomTerm ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_TomTerm(t any) bool { return func() bool { _, ok := (t).(TomTerm); return ok }();  }; func tom_symboltable_equal_term_TomList(t1  TomList , t2  TomList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_TomList(t any) bool { return func() bool { _, ok := (t).(TomList); return ok }();  }; func tom_symboltable_equal_term_TomType(t1  TomType , t2  TomType ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_TomType(t any) bool { return func() bool { _, ok := (t).(TomType); return ok }();  }; func tom_symboltable_equal_term_TypeOption(t1  TypeOption , t2  TypeOption ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_TypeOption(t any) bool { return func() bool { _, ok := (t).(TypeOption); return ok }();  }; func tom_symboltable_equal_term_TypeOptionList(t1  TypeOptionList , t2  TypeOptionList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_TypeOptionList(t any) bool { return func() bool { _, ok := (t).(TypeOptionList); return ok }();  }; func tom_symboltable_equal_term_TargetLanguageType(t1  TargetLanguageType , t2  TargetLanguageType ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_TargetLanguageType(t any) bool { return func() bool { _, ok := (t).(TargetLanguageType); return ok }();  }; func tom_symboltable_equal_term_TomTypeList(t1  TomTypeList , t2  TomTypeList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_TomTypeList(t any) bool { return func() bool { _, ok := (t).(TomTypeList); return ok }();  }; func tom_symboltable_is_fun_sym_Type(t  TomType ) bool { return func() bool { _, ok := (t).(*TypeTomType); return ok }();  }; func tom_symboltable_make_Type(TypeOptions  TypeOptionList , TomType  string , TlType  TargetLanguageType )  TomType  { return (MakeType(TypeOptions,TomType,TlType)) }; func tom_symboltable_get_slot_Type_TypeOptions(t  TomType )  TypeOptionList  { return (t).(*TypeTomType).TypeOptions;  }; func tom_symboltable_get_slot_Type_TomType(t  TomType )  string  { return (t).(*TypeTomType).TomType;  }; func tom_symboltable_get_slot_Type_TlType(t  TomType )  TargetLanguageType  { return (t).(*TypeTomType).TlType;  }; func tom_symboltable_is_fun_sym_concTypeOption(t  TypeOptionList ) bool { return func() bool { _, ok := (t).(*ConcTypeOptionTypeOptionList); return ok }();  }; func tom_symboltable_empty_list_concTypeOption()  TypeOptionList  { return (EmptyConcTypeOption()) }; func tom_symboltable_cons_list_concTypeOption(e  TypeOption , l  TypeOptionList )  TypeOptionList  { return (ConsConcTypeOption(e,l)) }; func tom_symboltable_get_head_concTypeOption_TypeOptionList(l  TypeOptionList )  TypeOption  { return (l).GetHeadconcTypeOption();  }; func tom_symboltable_get_tail_concTypeOption_TypeOptionList(l  TypeOptionList )  TypeOptionList  { return (l).GetTailconcTypeOption();  }; func tom_symboltable_is_empty_concTypeOption_TypeOptionList(l  TypeOptionList ) bool { return (l).IsEmptyconcTypeOption();  }; func tom_symboltable_append_list_concTypeOption(l1  TypeOptionList , l2  TypeOptionList )  TypeOptionList  { if tom_symboltable_is_empty_concTypeOption_TypeOptionList(l1) { return l2 } else if tom_symboltable_is_empty_concTypeOption_TypeOptionList(l2) { return l1 } else if tom_symboltable_is_empty_concTypeOption_TypeOptionList(tom_symboltable_get_tail_concTypeOption_TypeOptionList(l1)) { return tom_symboltable_cons_list_concTypeOption(tom_symboltable_get_head_concTypeOption_TypeOptionList(l1), l2) } else { return tom_symboltable_cons_list_concTypeOption(tom_symboltable_get_head_concTypeOption_TypeOptionList(l1), tom_symboltable_append_list_concTypeOption(tom_symboltable_get_tail_concTypeOption_TypeOptionList(l1), l2)) }; }; func tom_symboltable_get_slice_concTypeOption(begin  TypeOptionList , end  TypeOptionList , tail  TypeOptionList )  TypeOptionList  { if tom_symboltable_equal_term_TypeOptionList(begin, end) { return tail } else if tom_symboltable_equal_term_TypeOptionList(end, tail) && (tom_symboltable_is_empty_concTypeOption_TypeOptionList(end) || tom_symboltable_equal_term_TypeOptionList(end, tom_symboltable_empty_list_concTypeOption())) { return begin }; return tom_symboltable_cons_list_concTypeOption(tom_symboltable_get_head_concTypeOption_TypeOptionList(begin), tom_symboltable_get_slice_concTypeOption(tom_symboltable_get_tail_concTypeOption_TypeOptionList(begin), end, tail)) }; func tom_symboltable_make_EmptyTargetLanguageType()  TargetLanguageType  { return (MakeEmptyTargetLanguageType()) }; func tom_symboltable_equal_term_Info(t1  Info , t2  Info ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_Info(t any) bool { return func() bool { _, ok := (t).(Info); return ok }();  }; func tom_symboltable_equal_term_TypeConstraint(t1  TypeConstraint , t2  TypeConstraint ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_TypeConstraint(t any) bool { return func() bool { _, ok := (t).(TypeConstraint); return ok }();  }; func tom_symboltable_equal_term_TypeConstraintList(t1  TypeConstraintList , t2  TypeConstraintList ) bool { return (t1).Equivalent(t2);  }; func tom_symboltable_is_sort_TypeConstraintList(t any) bool { return func() bool { _, ok := (t).(TypeConstraintList); return ok }();  }; 




const (
	typeInt       = "int"
	typeLong      = "long"
	typeFloat     = "float"
	typeChar      = "char"
	typeDouble    = "double"
	typeString    = "String"
	typeBoolean   = "boolean"
	typeUniversal = "universal"
	typeVoid      = "void"
	typeIntArray  = "intarray"
	intArrayOp    = "concInt"
)




func typeUnknown() TomType {
  return tom_symboltable_make_Type(tom_symboltable_empty_list_concTypeOption(),"unknown type",tom_symboltable_make_EmptyTargetLanguageType())
}







type SymbolTable struct {
	mapSymbolName map[string]TomSymbol 
	mapTypeName   map[string]TomType   
	usedKeyEntry  map[KeyEntry]bool    
	mapInliner    map[string]string    
}






func NewSymbolTable() *SymbolTable {
	return &SymbolTable{
		mapSymbolName: map[string]TomSymbol{},
		mapTypeName:   map[string]TomType{},
		usedKeyEntry:  map[KeyEntry]bool{},
		mapInliner:    map[string]string{},
	}
}










func FromBundle(b *bridge.Bundle) *SymbolTable {
	st := NewSymbolTable()
	st.FromTerm(b.Table)
	for _, ty := range b.Types {
		; { _ = ty; { if tom_symboltable_is_sort_TomType(ty) { if tom_symboltable_is_fun_sym_Type(any(ty).( TomType )) { 
 st.PutType(tom_symboltable_get_slot_Type_TomType(any(ty).( TomType )), ty) ;  };  };  };  }; 

	}
	for _, ke := range b.Used {
		st.usedKeyEntry[ke] = true
	}
	return st
}




func (st *SymbolTable) PutSymbol(name string, astSymbol TomSymbol) {
	st.mapSymbolName[name] = astSymbol
}




func (st *SymbolTable) GetSymbolFromName(name string) TomSymbol {
	return st.mapSymbolName[name]
}




func (st *SymbolTable) PutType(name string, astType TomType) {
	st.mapTypeName[name] = astType
}








func (st *SymbolTable) GetUsedKeyEntries() []KeyEntry {
	res := make([]KeyEntry, 0, len(st.usedKeyEntry))
	for ke := range st.usedKeyEntry {
		res = append(res, ke)
	}
	sort.Slice(res, func(i, j int) bool { return fmt.Sprint(res[i]) < fmt.Sprint(res[j]) })
	return res
}






func (st *SymbolTable) GetTypeEntries() []TomType {
	names := make([]string, 0, len(st.mapTypeName))
	for n := range st.mapTypeName {
		names = append(names, n)
	}
	sort.Strings(names)
	res := make([]TomType, len(names))
	for i, n := range names {
		res[i] = st.mapTypeName[n]
	}
	return res
}






func (st *SymbolTable) GetType(name string) TomType {
	if ty, ok := st.mapTypeName[name]; ok {
		return ty
	}
	if name == typeInt {
		return st.GetIntType()
	}
	return nil
}




func (st *SymbolTable) IsUsedSymbolConstructor(symbol TomSymbol) bool {
	ke := tom_symboltable_make_UsedSymbolConstructor(symbol)
	return st.usedKeyEntry[ke]
}




func (st *SymbolTable) IsUsedSymbolDestructor(symbol TomSymbol) bool {
	ke := tom_symboltable_make_UsedSymbolDestructor(symbol)
	return st.usedKeyEntry[ke]
}




func (st *SymbolTable) IsUsedSymbolAC(symbol TomSymbol) bool {
	ke := tom_symboltable_make_UsedSymbolAC(symbol)
	return st.usedKeyEntry[ke]
}




func (st *SymbolTable) IsUsedType(ty TomType) bool {
	ke := tom_symboltable_make_UsedType(ty)
	return st.usedKeyEntry[ke]
}




func (st *SymbolTable) SetUsedSymbolConstructor(symbol TomSymbol) {
	ke := tom_symboltable_make_UsedSymbolConstructor(symbol)
	st.usedKeyEntry[ke] = true
}




func (st *SymbolTable) SetUsedSymbolDestructor(symbol TomSymbol) {
	ke := tom_symboltable_make_UsedSymbolDestructor(symbol)
	st.usedKeyEntry[ke] = true
}




func (st *SymbolTable) SetUsedType(ty TomType) {
	ke := tom_symboltable_make_UsedType(ty)
	st.usedKeyEntry[ke] = true
}




func (st *SymbolTable) SetUsedSymbolConstructorName(name string) {
	if symbol := st.GetSymbolFromName(name); symbol != nil {
		st.SetUsedSymbolConstructor(symbol)
	}
}




func (st *SymbolTable) SetUsedSymbolDestructorName(name string) {
	if symbol := st.GetSymbolFromName(name); symbol != nil {
		st.SetUsedSymbolDestructor(symbol)
	}
}




func (st *SymbolTable) SetUsedTypeName(name string) {
	if ty := st.GetType(name); ty != nil {
		st.SetUsedType(ty)
	}
}




func (st *SymbolTable) IsUsedSymbolConstructorName(name string) bool {
	if symbol := st.GetSymbolFromName(name); symbol != nil {
		return st.IsUsedSymbolConstructor(symbol)
	}
	return false
}




func (st *SymbolTable) IsUsedSymbolDestructorName(name string) bool {
	if symbol := st.GetSymbolFromName(name); symbol != nil {
		return st.IsUsedSymbolDestructor(symbol)
	}
	return false
}




func (st *SymbolTable) IsUsedTypeName(name string) bool {
	if ty := st.GetType(name); ty != nil {
		return st.IsUsedType(ty)
	}
	return false
}




func (st *SymbolTable) GetIntType() TomType { return NewType(tom_symboltable_empty_list_concTypeOption(), typeInt, "int64") }




func (st *SymbolTable) GetIntArrayType() TomType { return NewType(tom_symboltable_empty_list_concTypeOption(), typeIntArray, "[]int64") }




func (st *SymbolTable) GetLongType() TomType { return NewType(tom_symboltable_empty_list_concTypeOption(), typeLong, "int64") }




func (st *SymbolTable) GetFloatType() TomType { return NewType(tom_symboltable_empty_list_concTypeOption(), typeFloat, "float32") }




func (st *SymbolTable) GetCharType() TomType { return NewType(tom_symboltable_empty_list_concTypeOption(), typeChar, "rune") }




func (st *SymbolTable) GetDoubleType() TomType { return NewType(tom_symboltable_empty_list_concTypeOption(), typeDouble, "float64") }




func (st *SymbolTable) GetBooleanType() TomType { return NewType(tom_symboltable_empty_list_concTypeOption(), typeBoolean, "bool") }




func (st *SymbolTable) GetStringType() TomType { return NewType(tom_symboltable_empty_list_concTypeOption(), typeString, "string") }




func (st *SymbolTable) GetUniversalType() TomType { return NewType(tom_symboltable_empty_list_concTypeOption(), typeUniversal, "any") }




func (st *SymbolTable) GetVoidType() TomType { return NewType(tom_symboltable_empty_list_concTypeOption(), typeVoid, "") }




func (st *SymbolTable) IsIntType(ty string) bool { return ty == typeInt }




func (st *SymbolTable) IsIntArrayType(ty string) bool { return ty == typeIntArray }




func (st *SymbolTable) IsLongType(ty string) bool { return ty == typeLong }




func (st *SymbolTable) IsFloatType(ty string) bool { return ty == typeFloat }




func (st *SymbolTable) IsCharType(ty string) bool { return ty == typeChar }




func (st *SymbolTable) IsStringType(ty string) bool { return ty == typeString }




func (st *SymbolTable) IsBooleanType(ty string) bool { return ty == typeBoolean }




func (st *SymbolTable) IsDoubleType(ty string) bool { return ty == typeDouble }




func (st *SymbolTable) IsVoidType(ty string) bool { return ty == typeVoid }




func (st *SymbolTable) IsUnknownType(ty string) bool {
	t := tom_symboltable_make_Type(tom_symboltable_empty_list_concTypeOption(),ty,tom_symboltable_make_EmptyTargetLanguageType())
	return t == typeUnknown()
}






func (st *SymbolTable) IsBuiltinType(ty string) bool {
	return st.IsIntType(ty) || st.IsLongType(ty) || st.IsCharType(ty) ||
		st.IsStringType(ty) || st.IsBooleanType(ty) || st.IsDoubleType(ty) ||
		st.IsFloatType(ty)
}








func (st *SymbolTable) GetBuiltinType(ty string) TomType {
	switch {
	case st.IsIntType(ty):
		return st.GetIntType()
	case st.IsLongType(ty):
		return st.GetLongType()
	case st.IsCharType(ty):
		return st.GetCharType()
	case st.IsStringType(ty):
		return st.GetStringType()
	case st.IsBooleanType(ty):
		return st.GetBooleanType()
	case st.IsDoubleType(ty):
		return st.GetDoubleType()
	case st.IsFloatType(ty):
		return st.GetFloatType()
	}
	panic(&platform.RuntimeError{Msg: "getBuiltinType error on term: " + ty})
}






func (st *SymbolTable) KeySymbols() []string {
	names := make([]string, 0, len(st.mapSymbolName))
	for n := range st.mapSymbolName {
		names = append(names, n)
	}
	sort.Strings(names)
	return names
}




func (st *SymbolTable) FromTerm(table TomSymbolTable) {
	list := table.GetEntryList()
	for !list.IsEmptyconcTomEntry() {
		entry := list.GetHeadconcTomEntry()
		st.PutSymbol(entry.GetStrName(), entry.GetAstSymbol())
		list = list.GetTailconcTomEntry()
	}
}






func (st *SymbolTable) ToTerm() TomSymbolTable {
	list := tom_symboltable_empty_list_concTomEntry()
	for _, name := range st.KeySymbols() {
		symbol := st.GetSymbolFromName(name)
		entry := tom_symboltable_make_Entry(name,symbol)
		list = tom_symboltable_cons_list_concTomEntry(entry,tom_symboltable_append_list_concTomEntry(list,tom_symboltable_empty_list_concTomEntry()))
	}
	return tom_symboltable_make_Table(list)
}







const (
	prefixIsFsym          = "is_fsym_"
	prefixGetSlot         = "get_slot_"
	prefixGetHead         = "get_head_"
	prefixGetTail         = "get_tail_"
	prefixGetElementArray = "get_element_array_"
	prefixGetSizeArray    = "get_size_array_"
	prefixIsEmptyList     = "is_empty_list_"
	prefixIsSort          = "is_sort_"
	prefixMake            = "make_"
	prefixMakeEmptyArray  = "make_empty_array_"
	prefixMakeEmptyList   = "make_empty_list_"
	prefixMakeAddArray    = "make_append_"
	prefixMakeAddList     = "make_insert_"
	prefixEqualTerm       = "equal_"
)




func (st *SymbolTable) putInliner(prefix, opname, code string) {
	st.mapInliner[prefix+opname] = code
}






func (st *SymbolTable) getInliner(prefix, opname string) (code string, ok bool) {
	code, ok = st.mapInliner[prefix+opname]
	return code, ok
}




func (st *SymbolTable) PutIsFsym(opname, code string) { st.putInliner(prefixIsFsym, opname, code) }




func (st *SymbolTable) GetIsFsym(opname string) (string, bool) { return st.getInliner(prefixIsFsym, opname) }




func (st *SymbolTable) PutIsSort(ty, code string) { st.putInliner(prefixIsSort, ty, code) }




func (st *SymbolTable) GetIsSort(ty string) (string, bool) { return st.getInliner(prefixIsSort, ty) }






func (st *SymbolTable) PutResolveGetSlot(opname, slotname string) {
	st.putInliner(prefixGetSlot, opname+slotname, "")
}




func (st *SymbolTable) GetResolveGetSlot(opname, slotname string) (string, bool) {
	return st.getInliner(prefixGetSlot, opname+slotname)
}




func (st *SymbolTable) PutGetSlot(opname, slotname, code string) {
	st.putInliner(prefixGetSlot, opname+slotname, code)
}




func (st *SymbolTable) GetGetSlot(opname, slotname string) (string, bool) {
	return st.getInliner(prefixGetSlot, opname+slotname)
}




func (st *SymbolTable) PutGetHead(opname, code string) { st.putInliner(prefixGetHead, opname, code) }




func (st *SymbolTable) GetGetHead(opname string) (string, bool) { return st.getInliner(prefixGetHead, opname) }




func (st *SymbolTable) PutGetTail(opname, code string) { st.putInliner(prefixGetTail, opname, code) }




func (st *SymbolTable) GetGetTail(opname string) (string, bool) { return st.getInliner(prefixGetTail, opname) }




func (st *SymbolTable) PutGetElementArray(opname, code string) {
	st.putInliner(prefixGetElementArray, opname, code)
}




func (st *SymbolTable) GetGetElementArray(opname string) (string, bool) {
	return st.getInliner(prefixGetElementArray, opname)
}




func (st *SymbolTable) PutGetSizeArray(opname, code string) {
	st.putInliner(prefixGetSizeArray, opname, code)
}




func (st *SymbolTable) GetGetSizeArray(opname string) (string, bool) {
	return st.getInliner(prefixGetSizeArray, opname)
}




func (st *SymbolTable) PutIsEmptyList(opname, code string) { st.putInliner(prefixIsEmptyList, opname, code) }




func (st *SymbolTable) GetIsEmptyList(opname string) (string, bool) {
	return st.getInliner(prefixIsEmptyList, opname)
}




func (st *SymbolTable) PutMake(opname, code string) { st.putInliner(prefixMake, opname, code) }




func (st *SymbolTable) GetMake(opname string) (string, bool) { return st.getInliner(prefixMake, opname) }




func (st *SymbolTable) PutMakeEmptyArray(opname, code string) {
	st.putInliner(prefixMakeEmptyArray, opname, code)
}




func (st *SymbolTable) GetMakeEmptyArray(opname string) (string, bool) {
	return st.getInliner(prefixMakeEmptyArray, opname)
}




func (st *SymbolTable) PutMakeEmptyList(opname, code string) {
	st.putInliner(prefixMakeEmptyList, opname, code)
}




func (st *SymbolTable) GetMakeEmptyList(opname string) (string, bool) {
	return st.getInliner(prefixMakeEmptyList, opname)
}




func (st *SymbolTable) PutMakeAddArray(opname, code string) {
	st.putInliner(prefixMakeAddArray, opname, code)
}




func (st *SymbolTable) GetMakeAddArray(opname string) (string, bool) {
	return st.getInliner(prefixMakeAddArray, opname)
}




func (st *SymbolTable) PutMakeAddList(opname, code string) {
	st.putInliner(prefixMakeAddList, opname, code)
}




func (st *SymbolTable) GetMakeAddList(opname string) (string, bool) {
	return st.getInliner(prefixMakeAddList, opname)
}




func (st *SymbolTable) PutEqualTerm(ty, code string) { st.putInliner(prefixEqualTerm, ty, code) }




func (st *SymbolTable) GetEqualTerm(ty string) (string, bool) { return st.getInliner(prefixEqualTerm, ty) }




func (st *SymbolTable) GetIntArrayOp() string { return intArrayOp }



var _ = aterm.GetInstance

package backend

// Family F2 (plan J4.1b, Task 4), second part: the instructions and the
// expressions of the matching code. First the methods of GenericGenerator
// (GenericGenerator.t: comparisons, buildAddOne, buildSubstractOne,
// buildSubstract), then those of GoGenerator (GoGenerator.t:227-529) in
// the order of the Java source, buildAssignArray excepted (family F3).

import (
	. "tom/engine/adt"
	"tom/engine/platform"
	"tom/engine/tools"
	"tom/library/aterm"
)

%include { adt.tom }

// buildExpGreaterThan is GenericGenerator.buildExpGreaterThan (GenericGenerator.t:154).
func (g *goGenerator) buildExpGreaterThan(deep int, exp1, exp2 Expression, moduleName string) {
	deep = 0 // Java: to avoid useless spaces inside the code
	g.generateExpression(deep, exp1, moduleName)
	g.output.Write(" > ")
	g.generateExpression(deep, exp2, moduleName)
}

// buildExpGreaterOrEqualThan is GenericGenerator.buildExpGreaterOrEqualThan (GenericGenerator.t:161).
func (g *goGenerator) buildExpGreaterOrEqualThan(deep int, exp1, exp2 Expression, moduleName string) {
	deep = 0 // Java: to avoid useless spaces inside the code
	g.generateExpression(deep, exp1, moduleName)
	g.output.Write(" >= ")
	g.generateExpression(deep, exp2, moduleName)
}

// buildExpLessThan is GenericGenerator.buildExpLessThan (GenericGenerator.t:168).
func (g *goGenerator) buildExpLessThan(deep int, exp1, exp2 Expression, moduleName string) {
	deep = 0 // Java: to avoid useless spaces inside the code
	g.generateExpression(deep, exp1, moduleName)
	g.output.Write(" < ")
	g.generateExpression(deep, exp2, moduleName)
}

// buildExpLessOrEqualThan is GenericGenerator.buildExpLessOrEqualThan (GenericGenerator.t:175).
func (g *goGenerator) buildExpLessOrEqualThan(deep int, exp1, exp2 Expression, moduleName string) {
	deep = 0 // Java: to avoid useless spaces inside the code
	g.generateExpression(deep, exp1, moduleName)
	g.output.Write(" <= ")
	g.generateExpression(deep, exp2, moduleName)
}

// buildAddOne is GenericGenerator.buildAddOne (GenericGenerator.t:409).
func (g *goGenerator) buildAddOne(deep int, v BQTerm, moduleName string) {
	g.generateBQTerm(deep, v, moduleName)
	g.output.Write(" + 1")
}

// buildSubstractOne is GenericGenerator.buildSubstractOne (GenericGenerator.t:990).
func (g *goGenerator) buildSubstractOne(deep int, v BQTerm, moduleName string) {
	g.generateBQTerm(0, v, moduleName)
	g.output.Write(" - 1")
}

// buildSubstract is GenericGenerator.buildSubstract (GenericGenerator.t:995).
func (g *goGenerator) buildSubstract(deep int, v1, v2 BQTerm, moduleName string) {
	g.generateBQTerm(0, v1, moduleName)
	g.output.Write(" - ")
	g.generateBQTerm(0, v2, moduleName)
}

// buildAssign is GoGenerator.buildAssign (GoGenerator.t:227): the type of
// the variable tells buildExpConditional the type of its closure.
func (g *goGenerator) buildAssign(deep int, v BQTerm, optionList OptionList, exp Expression, moduleName string) {
	g.generateBQTerm(deep, v, moduleName)
	g.output.Write(" = ")
	var varType *string
	%match(v) {
		BQVariable[AstType=ty] -> {
			s, ok := g.knownGoType(`ty, moduleName)
			varType = optString(s, ok)
		}
	}
	g.generateTypedExpression(deep, varType, exp, moduleName)
	g.output.Write("; ")
}

// buildDoWhile is GoGenerator.buildDoWhile (GoGenerator.t:250).
func (g *goGenerator) buildDoWhile(deep int, succes Instruction, exp Expression, moduleName string) {
	g.output.Write("for { ")
	g.generateInstruction(deep+1, succes, moduleName)
	g.output.Write(" if !(")
	g.generateExpression(deep, exp, moduleName)
	g.output.Write(") { break; }; }; ")
}

// buildWhileDo is GoGenerator.buildWhileDo (GoGenerator.t:258).
func (g *goGenerator) buildWhileDo(deep int, exp Expression, succes Instruction, moduleName string) {
	g.output.Write("for ")
	g.generateExpression(deep, exp, moduleName)
	g.output.Write(" { ")
	g.generateInstruction(deep+1, succes, moduleName)
	g.output.Write(" }; ")
}

// buildIf is GoGenerator.buildIf (GoGenerator.t:266).
func (g *goGenerator) buildIf(deep int, exp Expression, succes Instruction, moduleName string) {
	g.output.Write("if ")
	g.generateExpression(deep, exp, moduleName)
	g.output.Write(" { ")
	g.generateInstruction(deep+1, succes, moduleName)
	g.output.Write(" }; ")
}

// buildIfWithFailure is GoGenerator.buildIfWithFailure (GoGenerator.t:274).
func (g *goGenerator) buildIfWithFailure(deep int, exp Expression, succes, failure Instruction, moduleName string) {
	g.output.Write("if ")
	g.generateExpression(deep, exp, moduleName)
	g.output.Write(" { ")
	g.generateInstruction(deep+1, succes, moduleName)
	g.output.Write(" } else { ")
	g.generateInstruction(deep+1, failure, moduleName)
	g.output.Write(" }; ")
}

// buildLet is GoGenerator.buildLet (GoGenerator.t:313): Go rejects unused
// variables, so every Let is followed by "_ = x". super.getVariableName is
// getVariableNameBQAbstract (R4); a Let shadows a %strategy field of the
// same name in its body.
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

// buildLetRef is GoGenerator.buildLetRef (GoGenerator.t:326).
func (g *goGenerator) buildLetRef(deep int, v BQTerm, optionList OptionList, tlType TargetLanguageType, exp Expression, body Instruction, moduleName string) {
	g.buildLet(deep, v, optionList, tlType, exp, body, moduleName)
}

// buildNamedBlock is GoGenerator.buildNamedBlock (GoGenerator.t:332): a
// labelled block is a one-shot labelled loop, "break label" leaves it.
func (g *goGenerator) buildNamedBlock(deep int, blockName string, instList InstructionList, moduleName string) {
	g.output.Write(blockName + ": for { ")
	g.generateInstructionList(deep+1, instList, moduleName)
	g.output.Write(" break " + blockName + "; }; ")
}

// buildUnamedBlock is GoGenerator.buildUnamedBlock (GoGenerator.t:340): a
// block the compiler builds; "};" ends it as a statement.
func (g *goGenerator) buildUnamedBlock(deep int, instList InstructionList, moduleName string) {
	g.output.Write("{ ")
	g.generateInstructionList(deep+1, instList, moduleName)
	g.output.Write(" }; ")
}

// buildExpTrue is GoGenerator.buildExpTrue (GoGenerator.t:354).
func (g *goGenerator) buildExpTrue(deep int) {
	g.output.Write(" true ")
}

// buildExpFalse is GoGenerator.buildExpFalse (GoGenerator.t:358).
func (g *goGenerator) buildExpFalse(deep int) {
	g.output.Write(" false ")
}

// buildExpBottom is GoGenerator.buildExpBottom (GoGenerator.t:362): the
// zero value of the type.
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
		// zero value of the sort's Go type, whatever it is (pointer, struct, …)
		goT, known := g.knownGoType(ty, moduleName)
		if !known || javaTrim(goT) == "" {
			g.output.Write(" nil ")
		} else {
			g.output.Write(" *new(" + javaTrim(goT) + ") ")
		}
	}
}

// buildExpNegation is GoGenerator.buildExpNegation (GoGenerator.t:383).
func (g *goGenerator) buildExpNegation(deep int, exp Expression, moduleName string) {
	g.output.Write("!(")
	g.generateExpression(deep, exp, moduleName)
	g.output.Write(")")
}

// buildExpAnd is GoGenerator.buildExpAnd (GoGenerator.t:389).
func (g *goGenerator) buildExpAnd(deep int, exp1, exp2 Expression, moduleName string) {
	g.output.Write(" (")
	g.generateExpression(deep, exp1, moduleName)
	g.output.Write(" && ")
	g.generateExpression(deep, exp2, moduleName)
	g.output.Write(") ")
}

// buildExpOr is GoGenerator.buildExpOr (GoGenerator.t:397).
func (g *goGenerator) buildExpOr(deep int, exp1, exp2 Expression, moduleName string) {
	g.output.Write(" (")
	g.generateExpression(deep, exp1, moduleName)
	g.output.Write(" || ")
	g.generateExpression(deep, exp2, moduleName)
	g.output.Write(") ")
}

// buildExpConditional is GoGenerator.buildExpConditional (GoGenerator.t:411):
// Go has no ternary operator, so an immediately-called closure. Its result
// type is read from the branches (exp2 first), else it is the type of the
// variable being assigned, else any (R7: the null of Java is ok == false).
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

// buildExpCast is GoGenerator.buildExpCast (GoGenerator.t:432): any(e).(T)
// is valid whatever the static type of e.
func (g *goGenerator) buildExpCast(deep int, tlType TargetLanguageType, exp Expression, moduleName string) {
	%match(tlType) {
		TLType(str) -> {
			g.output.Write("any(")
			g.generateExpression(deep, exp, moduleName)
			g.output.Write(").(" + `str + ")")
			return
		}
	}
	g.generateExpression(deep, exp, moduleName)
}

// useNativeEquality is GoGenerator.useNativeEquality (GoGenerator.t:444);
// ok is false where Java's sType is null.
func (g *goGenerator) useNativeEquality(sType string, ok bool, moduleName string) bool {
	st := g.getSymbolTable(moduleName)
	return !ok || st.IsUnknownType(sType) || st.IsBooleanType(sType)
}

// buildExpEqualTerm is GoGenerator.buildExpEqualTerm (GoGenerator.t:449).
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

// buildExpEqualBQTerm is GoGenerator.buildExpEqualBQTerm (GoGenerator.t:473).
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

// termTypeBQ is GoGenerator.termType(BQTerm) (GoGenerator.t:512): the sort
// of a term; ok is false where Java returns null, when the backend cannot
// tell (a runtime exception is caught, R8).
func (g *goGenerator) termTypeBQ(t BQTerm, moduleName string) (sort string, ok bool) {
	normal := platform.CatchRuntime(func() {
		sort, ok = tools.GetTomType(tools.GetTermTypeBQ(t, g.getSymbolTable(moduleName)))
	})
	if !normal {
		return "", false
	}
	return sort, ok
}

// termType is GoGenerator.termType(TomTerm) (GoGenerator.t:520).
func (g *goGenerator) termType(t TomTerm, moduleName string) (sort string, ok bool) {
	normal := platform.CatchRuntime(func() {
		sort, ok = tools.GetTomType(tools.GetTermType(t, g.getSymbolTable(moduleName)))
	})
	if !normal {
		return "", false
	}
	return sort, ok
}

// otherSort is GoGenerator.otherSort (GoGenerator.t:529): argSort is a
// known sort, other than sType, of a non-builtin sType; hasArgSort is
// false where Java's argSort is null.
func (g *goGenerator) otherSort(sType string, argSort string, hasArgSort bool, moduleName string) bool {
	st := g.getSymbolTable(moduleName)
	return hasArgSort && argSort != sType && !st.IsUnknownType(argSort) &&
		!st.IsBuiltinType(sType) && st.GetType(sType) != nil && st.GetType(argSort) != nil
}

// The generated mapping code (adt.tom) names aterm types; this keeps the import used.
var _ = aterm.GetInstance

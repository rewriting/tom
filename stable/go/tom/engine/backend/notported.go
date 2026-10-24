package backend

// Stubs of the generator methods not ported yet (plan J4.1b): each family
// task moves the methods it ports into its .t file and deletes their
// lines here; Task 7 deletes the file. A stub panics with the name of the
// method: the P2 counter (test/tools/j4) reports it.

import (
	. "tom/engine/adt"
	"tom/engine/platform"
)

// notPorted is the panic of a method not ported yet.
func notPorted(method string) {
	panic(&platform.RuntimeError{Msg: "backend: " + method + " is not ported yet (J4.1b)"})
}

// family F2 (Task 4)
func (g *goGenerator) buildTerm(deep int, opname string, argList BQTermList, moduleName string) {
	notPorted("buildTerm")
}
func (g *goGenerator) buildFunctionCall(deep int, name string, argList BQTermList, moduleName string) {
	notPorted("buildFunctionCall")
}
func (g *goGenerator) buildExpGreaterThan(deep int, exp1, exp2 Expression, moduleName string) {
	notPorted("buildExpGreaterThan")
}
func (g *goGenerator) buildExpGreaterOrEqualThan(deep int, exp1, exp2 Expression, moduleName string) {
	notPorted("buildExpGreaterOrEqualThan")
}
func (g *goGenerator) buildExpLessThan(deep int, exp1, exp2 Expression, moduleName string) {
	notPorted("buildExpLessThan")
}
func (g *goGenerator) buildExpLessOrEqualThan(deep int, exp1, exp2 Expression, moduleName string) {
	notPorted("buildExpLessOrEqualThan")
}
func (g *goGenerator) buildExpIsSort(deep int, ty string, exp BQTerm, moduleName string) {
	notPorted("buildExpIsSort")
}
func (g *goGenerator) buildExpIsFsym(deep int, opname string, exp BQTerm, moduleName string) {
	notPorted("buildExpIsFsym")
}
func (g *goGenerator) buildExpGetSlot(deep int, opname, slotName string, v BQTerm, moduleName string) {
	notPorted("buildExpGetSlot")
}
func (g *goGenerator) buildAddOne(deep int, v BQTerm, moduleName string) { notPorted("buildAddOne") }
func (g *goGenerator) buildSubstractOne(deep int, v BQTerm, moduleName string) {
	notPorted("buildSubstractOne")
}
func (g *goGenerator) buildSubstract(deep int, v1, v2 BQTerm, moduleName string) {
	notPorted("buildSubstract")
}
func (g *goGenerator) buildIsFsymDecl(deep int, tomName, varname string, tlType TargetLanguageType, code Expression, moduleName string) {
	notPorted("buildIsFsymDecl")
}
func (g *goGenerator) buildGetSlotDecl(deep int, tomName, varname string, tlType TargetLanguageType, code Expression, slotName TomName, moduleName string) {
	notPorted("buildGetSlotDecl")
}
func (g *goGenerator) buildGetDefaultDecl(deep int, tomName string, code Expression, slotName TomName, moduleName string) {
	notPorted("buildGetDefaultDecl")
}
func (g *goGenerator) buildAssign(deep int, v BQTerm, optionList OptionList, exp Expression, moduleName string) {
	notPorted("buildAssign")
}
func (g *goGenerator) buildDoWhile(deep int, succes Instruction, exp Expression, moduleName string) {
	notPorted("buildDoWhile")
}
func (g *goGenerator) buildWhileDo(deep int, exp Expression, succes Instruction, moduleName string) {
	notPorted("buildWhileDo")
}
func (g *goGenerator) buildIf(deep int, exp Expression, succes Instruction, moduleName string) {
	notPorted("buildIf")
}
func (g *goGenerator) buildIfWithFailure(deep int, exp Expression, succes, failure Instruction, moduleName string) {
	notPorted("buildIfWithFailure")
}
func (g *goGenerator) buildLet(deep int, v BQTerm, optionList OptionList, tlType TargetLanguageType, exp Expression, body Instruction, moduleName string) {
	notPorted("buildLet")
}
func (g *goGenerator) buildLetRef(deep int, v BQTerm, optionList OptionList, tlType TargetLanguageType, exp Expression, body Instruction, moduleName string) {
	notPorted("buildLetRef")
}
func (g *goGenerator) buildNamedBlock(deep int, blockName string, instList InstructionList, moduleName string) {
	notPorted("buildNamedBlock")
}
func (g *goGenerator) buildUnamedBlock(deep int, instList InstructionList, moduleName string) {
	notPorted("buildUnamedBlock")
}
func (g *goGenerator) buildExpTrue(deep int)  { notPorted("buildExpTrue") }
func (g *goGenerator) buildExpFalse(deep int) { notPorted("buildExpFalse") }
func (g *goGenerator) buildExpBottom(deep int, ty TomType, moduleName string) {
	notPorted("buildExpBottom")
}
func (g *goGenerator) buildExpNegation(deep int, exp Expression, moduleName string) {
	notPorted("buildExpNegation")
}
func (g *goGenerator) buildExpAnd(deep int, exp1, exp2 Expression, moduleName string) {
	notPorted("buildExpAnd")
}
func (g *goGenerator) buildExpOr(deep int, exp1, exp2 Expression, moduleName string) {
	notPorted("buildExpOr")
}
func (g *goGenerator) buildExpConditional(deep int, cond, exp1, exp2 Expression, moduleName string) {
	notPorted("buildExpConditional")
}
func (g *goGenerator) buildExpCast(deep int, tlType TargetLanguageType, exp Expression, moduleName string) {
	notPorted("buildExpCast")
}
func (g *goGenerator) buildExpEqualTerm(deep int, ty TomType, exp1 BQTerm, exp2 TomTerm, moduleName string) {
	notPorted("buildExpEqualTerm")
}
func (g *goGenerator) buildExpEqualBQTerm(deep int, ty TomType, exp1, exp2 BQTerm, moduleName string) {
	notPorted("buildExpEqualBQTerm")
}
func (g *goGenerator) genDeclMake(prefix, funName string, returnType TomType, argList BQTermList, instr Instruction, moduleName string) {
	notPorted("genDeclMake")
}

// family F3 (Task 5)
func (g *goGenerator) buildExpIsEmptyArray(deep int, opNameAST TomName, ty TomType, expIndex, expArray BQTerm, moduleName string) {
	notPorted("buildExpIsEmptyArray")
}
func (g *goGenerator) buildExpGetHead(deep int, opName string, domain, codomain TomType, v BQTerm, moduleName string) {
	notPorted("buildExpGetHead")
}
func (g *goGenerator) buildExpGetTail(deep int, opName string, ty TomType, v BQTerm, moduleName string) {
	notPorted("buildExpGetTail")
}
func (g *goGenerator) buildExpIsEmptyList(deep int, opName string, ty TomType, v BQTerm, moduleName string) {
	notPorted("buildExpIsEmptyList")
}
func (g *goGenerator) buildExpGetSize(deep int, opNameAST TomName, ty TomType, v BQTerm, moduleName string) {
	notPorted("buildExpGetSize")
}
func (g *goGenerator) buildExpGetSliceList(deep int, name string, varBegin, varEnd, tail BQTerm, moduleName string) {
	notPorted("buildExpGetSliceList")
}
func (g *goGenerator) buildExpGetSliceArray(deep int, name string, varArray, varBegin, expEnd BQTerm, moduleName string) {
	notPorted("buildExpGetSliceArray")
}
func (g *goGenerator) buildExpGetElement(deep int, opNameAST TomName, domain TomType, varName, varIndex BQTerm, moduleName string) {
	notPorted("buildExpGetElement")
}
func (g *goGenerator) buildListOrArray(deep int, list BQTerm, moduleName string) {
	notPorted("buildListOrArray")
}
func (g *goGenerator) buildAssignArray(deep int, v BQTerm, optionList OptionList, index BQTerm, exp Expression, moduleName string) {
	notPorted("buildAssignArray")
}
func (g *goGenerator) buildGetHeadDecl(deep int, opNameAST TomName, varName, suffix string, domain, codomain TargetLanguageType, code Expression, moduleName string) {
	notPorted("buildGetHeadDecl")
}
func (g *goGenerator) buildGetTailDecl(deep int, opNameAST TomName, varName, ty string, tlType TargetLanguageType, code Expression, moduleName string) {
	notPorted("buildGetTailDecl")
}
func (g *goGenerator) buildIsEmptyDecl(deep int, opNameAST TomName, varName, ty string, tlType TargetLanguageType, code Expression, moduleName string) {
	notPorted("buildIsEmptyDecl")
}
func (g *goGenerator) buildGetElementDecl(deep int, opNameAST TomName, name1, name2, type1 string, code Expression, moduleName string) {
	notPorted("buildGetElementDecl")
}
func (g *goGenerator) buildGetSizeDecl(deep int, opNameAST TomName, name1, ty string, code Expression, moduleName string) {
	notPorted("buildGetSizeDecl")
}
func (g *goGenerator) genDeclList(name, moduleName string)  { notPorted("genDeclList") }
func (g *goGenerator) genDeclArray(name, moduleName string) { notPorted("genDeclArray") }

// family F4 (Task 6)
func (g *goGenerator) buildFunctionDef(deep int, tomName string, argList BQTermList, codomain, throwsType TomType, instruction Instruction, moduleName string) {
	notPorted("buildFunctionDef")
}
func (g *goGenerator) buildMethodDef(deep int, tomName string, argList BQTermList, codomain, throwsType TomType, instruction Instruction, moduleName string) {
	notPorted("buildMethodDef")
}
func (g *goGenerator) buildClass(deep int, tomName string, extendsType TomType, superTerm BQTerm, declaration Declaration, moduleName string) {
	notPorted("buildClass")
}
func (g *goGenerator) buildIntrospectorClass(deep int, tomName string, declaration Declaration, moduleName string) {
	notPorted("buildIntrospectorClass")
}
func (g *goGenerator) buildResolveIsFsymDecl(deep int, tomName, varname string, tlType TargetLanguageType, moduleName string) {
	notPorted("buildResolveIsFsymDecl")
}
func (g *goGenerator) buildResolveGetSlotDecl(deep int, tomName, varname string, tlType TargetLanguageType, slotName TomName, moduleName string) {
	notPorted("buildResolveGetSlotDecl")
}
func (g *goGenerator) genResolveDeclMake(prefix, funName string, returnType TomType, argList BQTermList, moduleName string) {
	notPorted("genResolveDeclMake")
}
func (g *goGenerator) buildResolveClass(wName, tName, extendsName, moduleName string) {
	notPorted("buildResolveClass")
}
func (g *goGenerator) buildResolveInverseLinks(deep int, fileFrom, fileTo string, resolveNameList TomNameList, moduleName string) {
	notPorted("buildResolveInverseLinks")
}
func (g *goGenerator) buildReferenceClass(deep int, refname string, refclassTInstructions RefClassTracelinkInstructionList, moduleName string) {
	notPorted("buildReferenceClass")
}
func (g *goGenerator) buildTracelink(deep int, ty, name string, expr Expression, moduleName string) {
	notPorted("buildTracelink")
}
func (g *goGenerator) buildTracelinkPopulateResolve(deep int, refClassName string, tracedLinks TomNameList, current, link BQTerm, moduleName string) {
	notPorted("buildTracelinkPopulateResolve")
}

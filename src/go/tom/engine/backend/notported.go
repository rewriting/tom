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

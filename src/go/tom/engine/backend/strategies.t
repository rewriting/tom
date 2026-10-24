package backend

// Family F4 (plan J4.1b, Task 6): functions, %strategy and introspector
// classes (GoGenerator.t), then the methods of %transformation, which -go
// refuses (GoGenerator.t:860-903; GenericGenerator.buildResolveIsFsymDecl
// and buildResolveGetSlotDecl, which call the refusing genResolve…Code).

import (
	"strconv"
	"strings"

	. "tom/engine/adt"
	"tom/engine/platform"
	"tom/engine/tools"
	"tom/library/aterm"
)

%include { adt.tom }

// transformationRefused is the panic of every %transformation method
// (GoGenerator.t:860-903).
func transformationRefused() {
	panic(&platform.RuntimeError{Msg: "%transformation is not supported with -go"})
}

// copyFields is new HashSet<String>(classFields) (GoGenerator.t:719).
func copyFields(m map[string]bool) map[string]bool {
	c := make(map[string]bool, len(m))
	for k, v := range m {
		c[k] = v
	}
	return c
}

// buildFunctionDef is GoGenerator.buildFunctionDef (GoGenerator.t:703),
// which masks the abstract AbstractGenerator.buildFunctionDef (AbstractGenerator.t:1000).
func (g *goGenerator) buildFunctionDef(deep int, tomName string, argList BQTermList, codomain, throwsType TomType, instruction Instruction, moduleName string) {
	g.buildMethod(deep, tomName, argList, codomain, instruction, moduleName, false)
}

// buildMethodDef is GoGenerator.buildMethodDef (GoGenerator.t:707), which
// masks AbstractGenerator.buildMethodDef (AbstractGenerator.t:1001).
func (g *goGenerator) buildMethodDef(deep int, tomName string, argList BQTermList, codomain, throwsType TomType, instruction Instruction, moduleName string) {
	g.buildMethod(deep, tomName, argList, codomain, instruction, moduleName, true)
}

// buildMethod is GoGenerator.buildMethod (GoGenerator.t:711).
func (g *goGenerator) buildMethod(deep int, tomName string, varList BQTermList, codomain TomType, instruction Instruction, moduleName string, isMethod bool) {
	if isMethod && g.currentClass != "" {
		g.output.Write("func (" + receiver + " *" + g.currentClass + ") " + tomName + "(")
	} else {
		g.output.Write("func " + tomName + "(")
	}
	savedFields := g.classFields
	g.classFields = copyFields(g.classFields)
	for !varList.IsEmptyconcBQTerm() {
		localVar := varList.GetHeadconcBQTerm()
		matched := false // Java: break matchBlock (a Tom rule falls through to the next one)
		%match(localVar) {
			v@BQVariable[AstType=type2] -> {
				name := g.getVariableNameBQAbstract(`v) // Java: super.getVariableName(v)
				g.output.WriteDeep(deep, name)
				delete(g.classFields, name)
				g.output.Write(" " + g.goType(`type2))
				matched = true
			}
			_ -> {
				if !matched {
					panic(&platform.RuntimeError{Msg: "GoGenerator.buildMethod: strange term: " + platform.JavaString(localVar)})
				}
			}
		}
		varList = varList.GetTailconcBQTerm()
		if !varList.IsEmptyconcBQTerm() {
			g.output.Write(", ")
		}
	}
	ret := g.goType(codomain)
	g.output.Write(") " + ret + " { ")
	g.generateInstruction(deep, instruction, moduleName)
	g.output.Write(" }; ")
	g.classFields = savedFields
}

// buildClass is GoGenerator.buildClass (GoGenerator.t:756), which masks
// AbstractGenerator.buildClass (AbstractGenerator.t:1008): a %strategy.
func (g *goGenerator) buildClass(deep int, tomName string, extendsType TomType, superTerm BQTerm, declaration Declaration, moduleName string) {
	tomSymbol := g.getSymbolTable(moduleName).GetSymbolFromName(tomName)
	tomTypes := tools.GetSymbolDomain(tomSymbol)
	var names, types []string
	// names of the parameters of sort Strategy (as JavaGenerator.buildClass)
	var stratParams []string
	index := 0
	for !tomTypes.IsEmptyconcTomType() {
		ty := tomTypes.GetHeadconcTomType()
		types = append(types, g.goType(ty))
		name := tools.GetSlotName(tomSymbol, index).GetString()
		names = append(names, name)
		%match(ty) {
			Type[TomType="Strategy"] -> {
				stratParams = append(stratParams, name)
			}
		}
		tomTypes = tomTypes.GetTailconcTomType()
		index++
	}
	var s strings.Builder
	s.WriteString("type " + tomName + " struct { *sl.AbstractStrategyBasic; ")
	for i := 0; i < len(names); i++ {
		s.WriteString(names[i] + " " + types[i] + "; ")
	}
	s.WriteString("}; func tom_new_" + tomName + "(")
	for i := 0; i < len(names); i++ {
		sep := ""
		if i+1 < len(names) {
			sep = ", "
		}
		s.WriteString(names[i] + " " + types[i] + sep)
	}
	s.WriteString(") *" + tomName + " { " + receiver + " := &" + tomName + "{")
	for i := 0; i < len(names); i++ {
		s.WriteString(names[i] + ": " + names[i] + ", ")
	}
	s.WriteString("}; " + receiver + ".AbstractStrategyBasic = sl.NewAbstractStrategyBasic(")
	g.output.Write(s.String())
	g.generateBQTerm(deep, superTerm, moduleName)
	g.output.Write("); return " + receiver + " }; ")
	for i := 0; i < len(names); i++ {
		g.output.Write("func (" + receiver + " *" + tomName + ") get" + names[i] + "() " + types[i] +
			" { return " + receiver + "." + names[i] + " }; ")
	}
	// as the Java backend: the children of the strategy are its default
	// strategy (child 0, from AbstractStrategyBasic) followed by its
	// Strategy-typed parameters
	if len(stratParams) > 0 {
		var c strings.Builder
		c.WriteString("func (" + receiver + " *" + tomName + ") ChildCount() int { return " + strconv.Itoa(1+len(stratParams)) + " }; ")
		c.WriteString("func (" + receiver + " *" + tomName + ") ChildAt(i int) sl.Strategy { switch i { case 0: return " + receiver + ".AbstractStrategyBasic.ChildAt(0); ")
		for i := 0; i < len(stratParams); i++ {
			c.WriteString("case " + strconv.Itoa(i+1) + ": return " + receiver + "." + stratParams[i] + "; ")
		}
		c.WriteString("}; panic(\"" + tomName + ": no such child\") }; ")
		c.WriteString("func (" + receiver + " *" + tomName + ") SetChildAt(i int, child sl.Strategy) { switch i { case 0: " + receiver + ".AbstractStrategyBasic.SetChildAt(0, child); return; ")
		for i := 0; i < len(stratParams); i++ {
			c.WriteString("case " + strconv.Itoa(i+1) + ": " + receiver + "." + stratParams[i] + " = child; return; ")
		}
		c.WriteString("}; panic(\"" + tomName + ": no such child\") }; ")
		g.output.Write(c.String())
	}
	g.output.Write("func (" + receiver + " *" + tomName + ") VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { " +
		"defer sl.RecoverVisitFailure(&tom__err); return " + receiver + ".visitLight(v, introspector), nil }; ")
	// environment-based visit (Java AbstractStrategyBasic.visit(Introspector)
	// and AbstractStrategy.visit(T,Introspector) / visit(Environment,Introspector)):
	// Go has no virtual dispatch, so each strategy defines them on itself
	g.output.Write("func (" + receiver + " *" + tomName + ") VisitEnv(introspector sl.Introspector) int { " +
		"return sl.VisitBasic(" + receiver + ", introspector) }; ")
	g.output.Write("func (" + receiver + " *" + tomName + ") Visit(v any, introspector sl.Introspector) (any, error) { " +
		"return sl.VisitStrategy(" + receiver + ", v, introspector) }; ")
	g.output.Write("func (" + receiver + " *" + tomName + ") VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { " +
		"return sl.VisitStrategyWith(" + receiver + ", env, introspector) }; ")
	g.currentClass = tomName
	g.classFields = map[string]bool{}
	for _, n := range names {
		g.classFields[n] = true
	}
	g.generateDeclaration(deep, declaration, moduleName)
	g.classFields = map[string]bool{}
	g.currentClass = ""
}

// buildIntrospectorClass is GoGenerator.buildIntrospectorClass
// (GoGenerator.t:850), which masks AbstractGenerator.buildIntrospectorClass
// (AbstractGenerator.t:1015): --genIntrospector.
func (g *goGenerator) buildIntrospectorClass(deep int, tomName string, declaration Declaration, moduleName string) {
	g.output.Write("type " + tomName + " struct{}; var _ sl.Introspector = (*" + tomName + ")(nil); ")
	savedClass := g.currentClass
	g.currentClass = tomName
	g.generateDeclaration(deep, declaration, moduleName)
	g.currentClass = savedClass
}

// buildResolveIsFsymDecl is GenericGenerator.buildResolveIsFsymDecl
// (GenericGenerator.t:447): its language specific part, genResolveIsFsymCode,
// refuses %transformation.
func (g *goGenerator) buildResolveIsFsymDecl(deep int, tomName, varname string, tlType TargetLanguageType, moduleName string) {
	tomSymbol := g.getSymbolTable(moduleName).GetSymbolFromName(tomName)
	opname := tomSymbol.GetAstName().GetString()
	code := `Code(g.genResolveIsFsymCode(tomName, varname))
	if !g.inline || !code.IsCode() {
		returnType := g.getSymbolTable(moduleName).GetBooleanType()
		argType := tools.GetTLCode(tlType)
		g.genDeclInstr(tools.GetTLType(returnType), g.declName("tom_is_fun_sym"), opname,
			[]string{argType, varname}, `Return(ExpressionToBQTerm(code)), deep, moduleName)
	}
}

// buildResolveGetSlotDecl is GenericGenerator.buildResolveGetSlotDecl
// (GenericGenerator.t:508): genResolveGetSlotCode refuses %transformation.
func (g *goGenerator) buildResolveGetSlotDecl(deep int, tomName, varname string, tlType TargetLanguageType, slotName TomName, moduleName string) {
	tomSymbol := g.getSymbolTable(moduleName).GetSymbolFromName(tomName)
	opname := tomSymbol.GetAstName().GetString()
	typesList := tomSymbol.GetTypesToType().GetDomain()
	code := `Code(g.genResolveGetSlotCode(tomName, varname, slotName.GetString()))
	if !g.inline || !code.IsCode() {
		slotIndex := tools.GetSlotIndex(tomSymbol, slotName)
		l := typesList
		for index := 0; !l.IsEmptyconcTomType() && index < slotIndex; index++ {
			l = l.GetTailconcTomType()
		}
		returnType := l.GetHeadconcTomType()
		argType := tools.GetTLCode(tlType)
		g.genDeclInstr(tools.GetTLType(returnType), g.declName("tom_get_slot"), opname+"_"+slotName.GetString(),
			[]string{argType, varname}, `Return(ExpressionToBQTerm(code)), deep, moduleName)
	}
}

// genResolveIsSortCode is GoGenerator.genResolveIsSortCode (GoGenerator.t:860).
func (g *goGenerator) genResolveIsSortCode(varName, resolveStringName string) string {
	transformationRefused()
	return ""
}

// genResolveIsFsymCode is GoGenerator.genResolveIsFsymCode (GoGenerator.t:864).
func (g *goGenerator) genResolveIsFsymCode(tomName, varname string) string {
	transformationRefused()
	return ""
}

// genResolveGetSlotCode is GoGenerator.genResolveGetSlotCode (GoGenerator.t:868).
func (g *goGenerator) genResolveGetSlotCode(tomName, varname, slotName string) string {
	transformationRefused()
	return ""
}

// buildResolveClass is GoGenerator.buildResolveClass (GoGenerator.t:872).
func (g *goGenerator) buildResolveClass(wName, tName, extendsName, moduleName string) {
	transformationRefused()
}

// buildResolveInverseLinks is GoGenerator.buildResolveInverseLinks (GoGenerator.t:876).
func (g *goGenerator) buildResolveInverseLinks(deep int, fileFrom, fileTo string, resolveNameList TomNameList, moduleName string) {
	transformationRefused()
}

// genResolveDeclMake is GoGenerator.genResolveDeclMake (GoGenerator.t:880).
func (g *goGenerator) genResolveDeclMake(prefix, funName string, returnType TomType, argList BQTermList, moduleName string) {
	transformationRefused()
}

// genResolveMakeCode is GoGenerator.genResolveMakeCode (GoGenerator.t:884).
func (g *goGenerator) genResolveMakeCode(funName string, argList BQTermList) string {
	transformationRefused()
	return ""
}

// buildReferenceClass is GoGenerator.buildReferenceClass (GoGenerator.t:888).
func (g *goGenerator) buildReferenceClass(deep int, refname string, refclassTInstructions RefClassTracelinkInstructionList, moduleName string) {
	transformationRefused()
}

// buildTracelink is GoGenerator.buildTracelink (GoGenerator.t:892).
func (g *goGenerator) buildTracelink(deep int, ty, name string, expr Expression, moduleName string) {
	transformationRefused()
}

// buildTracelinkPopulateResolve is GoGenerator.buildTracelinkPopulateResolve (GoGenerator.t:896).
func (g *goGenerator) buildTracelinkPopulateResolve(deep int, refClassName string, tracedLinks TomNameList, current, link BQTerm, moduleName string) {
	transformationRefused()
}

// buildResolve is GoGenerator.buildResolve (GoGenerator.t:900).
func (g *goGenerator) buildResolve(deep int, bqterm BQTerm, moduleName string) {
	transformationRefused()
}

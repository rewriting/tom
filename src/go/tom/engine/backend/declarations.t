package backend

// Family F2 (plan J4.1b, Task 4), first part: the mapping functions that do
// not depend on lists or arrays, and the terms (GenericGenerator.t and
// GoGenerator.t, in the order of the Java sources): buildTerm,
// isResolveOp, instantiateTemplate and its two variants, buildExpIsSort,
// buildExpIsFsym, buildExpGetSlot, buildExpGetDefault, buildIsFsymDecl,
// buildGetSlotDecl, buildGetDefaultDecl, buildFunctionCall, genDeclMake.

import (
	"strconv"
	"strings"

	. "tom/engine/adt"
	"tom/engine/platform"
	"tom/engine/tools"
	"tom/library/aterm"
)

%include { adt.tom }

// buildTerm is GenericGenerator.buildTerm (GenericGenerator.t:85): the
// call of the make function of an operator, or its inlined template.
func (g *goGenerator) buildTerm(deep int, opname string, argList BQTermList, moduleName string) {
	deep = 0 // Java: to avoid useless spaces inside the code
	template, hasTemplate := g.getSymbolTable(moduleName).GetMake(opname)
	if !g.instantiateTemplate(deep, template, hasTemplate, opname, argList, moduleName) {
		prefix := g.declName("tom_make_")
		g.output.Write(prefix + opname)
		g.output.WriteOpenBrace()
		index := 0
		for !argList.IsEmptyconcBQTerm() {
			bqt := argList.GetHeadconcBQTerm()
			generatebqt := true
			%match(bqt) {
				BQDefault() -> {
					// code for ast produced by antlr4 parser
					tomSymbol := g.getSymbolTable(moduleName).GetSymbolFromName(opname)
					slotName := tools.GetSlotName(tomSymbol, index)
					g.buildExpGetDefault(deep, opname, slotName.GetString(), moduleName)
					generatebqt = false
				}
				Composite(CompositeBQTerm(BQDefault()), _*) -> {
					// code for ast produced by antlr2 parser
					tomSymbol := g.getSymbolTable(moduleName).GetSymbolFromName(opname)
					slotName := tools.GetSlotName(tomSymbol, index)
					g.buildExpGetDefault(deep, opname, slotName.GetString(), moduleName)
					generatebqt = false
				}
			}
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

// isResolveOp is GenericGenerator.isResolveOp (GenericGenerator.t:135).
func (g *goGenerator) isResolveOp(opname string, moduleName string) bool {
	g.getSymbolTable(moduleName).GetSymbolFromName(opname)
	return true
}

// instantiateTemplate is GenericGenerator.instantiateTemplate(int, String,
// String, BQTermList, String) (GenericGenerator.t:206): given a template,
// computes its instance, writes it and returns true if a substitution has
// been done; does nothing and returns false otherwise. hasTemplate is
// false where Java's template is null (R7). Dead code with -go, inline
// being false (R10). The output is replaced by a string output for each
// argument, as in Java (R6).
func (g *goGenerator) instantiateTemplate(deep int, template string, hasTemplate bool, opname string, termList BQTermList, moduleName string) bool {
	if g.inline && hasTemplate {
		oldOutput := g.output
		instance := template
		index := 0
		for !termList.IsEmptyconcBQTerm() {
			g.output = tools.NewStringOutputCode()
			bqt := termList.GetHeadconcBQTerm()
			generatebqt := true
			%match(bqt) {
				BQDefault() -> {
					// code for ast produced by antlr4 parser
					tomSymbol := g.getSymbolTable(moduleName).GetSymbolFromName(opname)
					slotName := tools.GetSlotName(tomSymbol, index)
					g.buildExpGetDefault(deep, opname, slotName.GetString(), moduleName)
					generatebqt = false
				}
				Composite(CompositeBQTerm(BQDefault()), _*) -> {
					tomSymbol := g.getSymbolTable(moduleName).GetSymbolFromName(opname)
					slotName := tools.GetSlotName(tomSymbol, index)
					g.buildExpGetDefault(deep, opname, slotName.GetString(), moduleName)
					generatebqt = false
				}
			}
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
			// inline only if a substitution has been done
			g.output.Write(instance)
			return true
		}
	}
	return false
}

// instantiateTemplateSubject is GenericGenerator.instantiateTemplate(String,
// String) (GenericGenerator.t:270): ok is false where Java returns null.
func (g *goGenerator) instantiateTemplateSubject(template string, hasTemplate bool, subject string) (string, bool) {
	if hasTemplate {
		return strings.ReplaceAll(template, "{0}", subject), true
	}
	return "", false
}

// instantiateTemplateHeadTail is GenericGenerator.instantiateTemplate(String,
// String, String) (GenericGenerator.t:277): ok is false where Java returns null.
func (g *goGenerator) instantiateTemplateHeadTail(template string, hasTemplate bool, head, tail string) (string, bool) {
	if hasTemplate {
		return strings.ReplaceAll(strings.ReplaceAll(template, "{0}", head), "{1}", tail), true
	}
	return "", false
}

// buildExpIsSort is GenericGenerator.buildExpIsSort (GenericGenerator.t:284).
func (g *goGenerator) buildExpIsSort(deep int, ty string, exp BQTerm, moduleName string) {
	deep = 0 // Java: to avoid useless spaces inside the code
	if g.getSymbolTable(moduleName).IsBuiltinType(ty) {
		g.generateExpression(deep, `TrueTL(), moduleName)
		return
	}
	template, hasTemplate := g.getSymbolTable(moduleName).GetIsSort(ty)
	opname := ""
	if !g.instantiateTemplate(deep, template, hasTemplate, opname, `concBQTerm(exp), moduleName) {
		g.output.Write(g.declName("tom_is_sort_"+ty) + "(")
		g.generateBQTerm(deep, exp, moduleName)
		g.output.Write(")")
	}
}

// buildExpIsFsym is GenericGenerator.buildExpIsFsym (GenericGenerator.t:300).
func (g *goGenerator) buildExpIsFsym(deep int, opname string, exp BQTerm, moduleName string) {
	deep = 0 // Java: to avoid useless spaces inside the code
	template, hasTemplate := g.getSymbolTable(moduleName).GetIsFsym(opname)
	if !g.instantiateTemplate(deep, template, hasTemplate, opname, `concBQTerm(exp), moduleName) {
		s, ok := g.isFsymMap[opname] // Java: HashMap.get, null when absent
		if !ok {
			s = g.declName("tom_is_fun_sym_"+opname) + "("
		}
		g.output.Write(s)
		g.generateBQTerm(deep, exp, moduleName)
		g.output.Write(")")
	}
}

// buildExpGetSlot is GenericGenerator.buildExpGetSlot (GenericGenerator.t:315).
func (g *goGenerator) buildExpGetSlot(deep int, opname, slotName string, v BQTerm, moduleName string) {
	deep = 0 // Java: to avoid useless spaces inside the code
	template, hasTemplate := g.getSymbolTable(moduleName).GetGetSlot(opname, slotName)
	if !g.instantiateTemplate(deep, template, hasTemplate, opname, `concBQTerm(v), moduleName) {
		g.output.Write(g.declName("tom_get_slot_" + opname + "_" + slotName))
		g.output.WriteOpenBrace()
		g.generateBQTerm(deep, v, moduleName)
		g.output.WriteCloseBrace()
	}
}

// buildExpGetDefault is GenericGenerator.buildExpGetDefault (GenericGenerator.t:333).
func (g *goGenerator) buildExpGetDefault(deep int, opname string, slotName string, moduleName string) {
	deep = 0 // Java: to avoid useless spaces inside the code
	g.output.Write(g.declName("tom_get_default_" + opname + "_" + slotName))
	g.output.WriteOpenBrace()
	g.output.WriteCloseBrace()
}

// buildIsFsymDecl is GenericGenerator.buildIsFsymDecl (GenericGenerator.t:417):
// the function declaration when no substitution has been done.
func (g *goGenerator) buildIsFsymDecl(deep int, tomName, varname string, tlType TargetLanguageType, code Expression, moduleName string) {
	tomSymbol := g.getSymbolTable(moduleName).GetSymbolFromName(tomName)
	opname := tomSymbol.GetAstName().GetString()
	inlined := g.inlineplus
	if code.IsCode() {
		// perform the instantiation
		ocode := code.GetCode()
		ncode := strings.ReplaceAll(ocode, "{0}", varname)
		if ncode != ocode {
			inlined = true
			code = `Code(ncode)
		}
	}
	if !g.inline || !code.IsCode() || !inlined {
		returnType := g.getSymbolTable(moduleName).GetBooleanType()
		argType := tools.GetTLCode(tlType)
		g.genDeclInstr(tools.GetTLType(returnType), g.declName("tom_is_fun_sym"), opname,
			[]string{argType, varname}, `Return(ExpressionToBQTerm(code)), deep, moduleName)
	}
}

// buildGetSlotDecl is GenericGenerator.buildGetSlotDecl (GenericGenerator.t:467).
func (g *goGenerator) buildGetSlotDecl(deep int, tomName, varname string, tlType TargetLanguageType, code Expression, slotName TomName, moduleName string) {
	tomSymbol := g.getSymbolTable(moduleName).GetSymbolFromName(tomName)
	opname := tomSymbol.GetAstName().GetString()
	typesList := tomSymbol.GetTypesToType().GetDomain()
	inlined := g.inlineplus
	if code.IsCode() {
		// perform the instantiation
		ocode := code.GetCode()
		ncode := strings.ReplaceAll(ocode, "{0}", varname)
		if ncode != ocode {
			inlined = true
			code = `Code(ncode)
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
			`Return(ExpressionToBQTerm(code)), deep, moduleName)
	}
}

// buildGetDefaultDecl is GenericGenerator.buildGetDefaultDecl (GenericGenerator.t:537).
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
		`Return(ExpressionToBQTerm(code)), deep, moduleName)
}

// buildFunctionCall is GenericGenerator.buildFunctionCall (GenericGenerator.t:886).
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

// genDeclMake is GoGenerator.genDeclMake (GoGenerator.t:573): the make
// function of an operator; {i} in the hook code stands for the i-th
// argument. The Java matchBlock/break is a %match whose default branch
// panics, the other branch going on with the next argument.
func (g *goGenerator) genDeclMake(prefix, funName string, returnType TomType, argList BQTermList, instr Instruction, moduleName string) {
	if g.nodeclMode {
		return
	}
	%match(instr) {
		ExpressionToInstruction(Code(code)) -> {
			ncode := `code
			index := 0
			%match(argList) {
				concBQTerm(_*, BQVariable[AstName=Name(varname)], _*) -> {
					ncode = strings.ReplaceAll(ncode, "{"+strconv.Itoa(index)+"}", `varname)
					index++
				}
			}
			instr = `ExpressionToInstruction(Code(ncode))
		}
	}
	var s strings.Builder
	s.WriteString("func " + prefix + funName + "(")
	for !argList.IsEmptyconcBQTerm() {
		arg := argList.GetHeadconcBQTerm()
		%match(arg) {
			BQVariable[AstName=Name(name), AstType=Type[TlType=tlType@TLType[]]] -> {
				s.WriteString(`name + " " + tools.GetTLCode(`tlType))
				argList = argList.GetTailconcBQTerm()
				if !argList.IsEmptyconcBQTerm() {
					s.WriteString(", ")
				}
				continue
			}
			_ -> {
				panic(&platform.RuntimeError{Msg: "genDeclMake: strange term: " + platform.JavaString(arg)})
			}
		}
	}
	// parenthesised: the hook code may start with newlines (line sync), Go would insert ';' after return
	s.WriteString(") " + g.goType(returnType) + " { return (")
	g.output.Write(s.String())
	g.generateInstruction(0, instr, moduleName)
	g.output.Write(") }; ")
}

// The generated mapping code (adt.tom) names aterm types; this keeps the import used.
var _ = aterm.GetInstance

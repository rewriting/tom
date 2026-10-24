package backend

// Family F3 (plan J4.1b, Task 5): lists and arrays (GenericGenerator.t and
// GoGenerator.t, in the order of the Java sources): the expressions on
// lists and arrays, the declarations of the get_head, get_tail, is_empty,
// get_element and get_size hooks, buildExpGetElement, buildListOrArray,
// buildAssignArray, genDeclList and genDeclArray.

import (
	"strings"

	. "tom/engine/adt"
	"tom/engine/platform"
	"tom/engine/tools"
	"tom/library/aterm"
)

%include { adt.tom }

// genericBadCase is GenericGenerator.GENERIC_GENERATOR_BAD_CASE
// (GenericGenerator.t:55).
const genericBadCase = "GenericGenerator: bad case: "

// javaTomType is TomBase.getTomType(type) as Java concatenates it into a
// string: null, spelled "null", for a type without a name (R7).
func javaTomType(ty TomType) string {
	s, ok := tools.GetTomType(ty)
	if !ok {
		return "null"
	}
	return s
}

// buildExpIsEmptyArray is GenericGenerator.buildExpIsEmptyArray (GenericGenerator.t:182).
func (g *goGenerator) buildExpIsEmptyArray(deep int, opNameAST TomName, ty TomType, expIndex, expArray BQTerm, moduleName string) {
	deep = 0 // Java: to avoid useless spaces inside the code
	g.generateBQTerm(deep, expIndex, moduleName)
	g.output.Write(" >= ")
	%match(opNameAST) {
		EmptyName() -> {
			panic(&platform.RuntimeError{Msg: genericBadCase + platform.JavaString(opNameAST)})
		}
	}
	opName := opNameAST.GetString()
	sType := javaTomType(ty)
	template, hasTemplate := g.getSymbolTable(moduleName).GetGetSizeArray(opName)
	if !g.instantiateTemplate(deep, template, hasTemplate, opName, `concBQTerm(expArray), moduleName) {
		g.output.Write(g.declName("tom_get_size_"+opName+"_"+sType) + "(")
		g.generateBQTerm(deep, expArray, moduleName)
		g.output.Write(")")
	}
}

// buildExpGetHead is GenericGenerator.buildExpGetHead (GenericGenerator.t:340).
func (g *goGenerator) buildExpGetHead(deep int, opName string, domain, codomain TomType, v BQTerm, moduleName string) {
	deep = 0 // Java: to avoid useless spaces inside the code
	template, hasTemplate := g.getSymbolTable(moduleName).GetGetHead(opName)
	if !g.instantiateTemplate(deep, template, hasTemplate, opName, `concBQTerm(v), moduleName) {
		g.output.Write(g.declName("tom_get_head_"+opName+"_"+javaTomType(domain)) + "(")
		g.generateBQTerm(deep, v, moduleName)
		g.output.Write(")")
	}
}

// buildExpGetTail is GenericGenerator.buildExpGetTail (GenericGenerator.t:350).
func (g *goGenerator) buildExpGetTail(deep int, opName string, ty TomType, v BQTerm, moduleName string) {
	deep = 0 // Java: to avoid useless spaces inside the code
	template, hasTemplate := g.getSymbolTable(moduleName).GetGetTail(opName)
	if !g.instantiateTemplate(deep, template, hasTemplate, opName, `concBQTerm(v), moduleName) {
		g.output.Write(g.declName("tom_get_tail_"+opName+"_"+javaTomType(ty)) + "(")
		g.generateBQTerm(deep, v, moduleName)
		g.output.Write(")")
	}
}

// buildExpIsEmptyList is GenericGenerator.buildExpIsEmptyList (GenericGenerator.t:360).
func (g *goGenerator) buildExpIsEmptyList(deep int, opName string, ty TomType, v BQTerm, moduleName string) {
	deep = 0 // Java: to avoid useless spaces inside the code
	template, hasTemplate := g.getSymbolTable(moduleName).GetIsEmptyList(opName)
	if !g.instantiateTemplate(deep, template, hasTemplate, opName, `concBQTerm(v), moduleName) {
		g.output.Write(g.declName("tom_is_empty_"+opName+"_"+javaTomType(ty)) + "(")
		g.generateBQTerm(deep, v, moduleName)
		g.output.Write(")")
	}
}

// buildExpGetSize is GenericGenerator.buildExpGetSize (GenericGenerator.t:370).
func (g *goGenerator) buildExpGetSize(deep int, opNameAST TomName, ty TomType, v BQTerm, moduleName string) {
	deep = 0 // Java: to avoid useless spaces inside the code
	%match(opNameAST) {
		EmptyName() -> {
			panic(&platform.RuntimeError{Msg: genericBadCase + platform.JavaString(opNameAST)})
		}
	}
	opName := opNameAST.GetString()
	sType := javaTomType(ty)
	template, hasTemplate := g.getSymbolTable(moduleName).GetGetSizeArray(opName)
	if !g.instantiateTemplate(deep, template, hasTemplate, opName, `concBQTerm(v), moduleName) {
		g.output.Write(g.declName("tom_get_size_"+opName+"_"+sType) + "(")
		g.generateBQTerm(deep, v, moduleName)
		g.output.Write(")")
	}
}

// buildExpGetSliceList is GenericGenerator.buildExpGetSliceList (GenericGenerator.t:387).
func (g *goGenerator) buildExpGetSliceList(deep int, name string, varBegin, varEnd, tail BQTerm, moduleName string) {
	deep = 0 // Java: to avoid useless spaces inside the code
	g.output.Write(g.declName("tom_get_slice_"+name) + "(")
	g.generateBQTerm(deep, varBegin, moduleName)
	g.output.Write(",")
	g.generateBQTerm(deep, varEnd, moduleName)
	g.output.Write(",")
	g.generateBQTerm(deep, tail, moduleName)
	g.output.Write(")")
}

// buildExpGetSliceArray is GenericGenerator.buildExpGetSliceArray (GenericGenerator.t:398).
func (g *goGenerator) buildExpGetSliceArray(deep int, name string, varArray, varBegin, expEnd BQTerm, moduleName string) {
	deep = 0 // Java: to avoid useless spaces inside the code
	g.output.Write(g.declName("tom_get_slice_"+name) + "(")
	g.generateBQTerm(deep, varArray, moduleName)
	g.output.Write(",")
	g.generateBQTerm(deep, varBegin, moduleName)
	g.output.Write(",")
	g.generateBQTerm(deep, expEnd, moduleName)
	g.output.Write(")")
}

// buildAssignArray is GoGenerator.buildAssignArray (GoGenerator.t:238).
func (g *goGenerator) buildAssignArray(deep int, v BQTerm, optionList OptionList, index BQTerm, exp Expression, moduleName string) {
	g.generateArray(deep, v, index, moduleName)
	g.output.Write(" = ")
	g.generateExpression(deep, exp, moduleName)
	g.output.Write("; ")
}

// buildGetHeadDecl is GenericGenerator.buildGetHeadDecl (GenericGenerator.t:609).
func (g *goGenerator) buildGetHeadDecl(deep int, opNameAST TomName, varName, suffix string, domain, codomain TargetLanguageType, code Expression, moduleName string) {
	opname := opNameAST.GetString()
	inlined := g.inlineplus
	if code.IsCode() {
		// perform the instantiation
		ocode := code.GetCode()
		ncode := strings.ReplaceAll(ocode, "{0}", varName)
		if ncode != ocode {
			inlined = true
			code = `Code(ncode)
		}
	}
	if !g.inline || !code.IsCode() || !inlined {
		returnType := ""
		argType := ""
		functionName := g.declName("tom_get_head_" + opname)
		%match(opNameAST) {
			EmptyName() -> {
				panic(&platform.RuntimeError{Msg: genericBadCase + platform.JavaString(opNameAST)})
			}
			Name(opName) -> {
				tomSymbol := g.getSymbolFromName(`opName)
				argType = tools.GetTLType(tools.GetSymbolCodomain(tomSymbol))
				returnType = tools.GetTLType(tools.GetSymbolDomain(tomSymbol).GetHeadconcTomType())
			}
		}
		g.genDeclInstr(returnType, functionName, suffix,
			[]string{argType, varName},
			`Return(ExpressionToBQTerm(code)), deep, moduleName)
	}
}

// buildGetTailDecl is GenericGenerator.buildGetTailDecl (GenericGenerator.t:646).
func (g *goGenerator) buildGetTailDecl(deep int, opNameAST TomName, varName, ty string, tlType TargetLanguageType, code Expression, moduleName string) {
	opname := opNameAST.GetString()
	inlined := g.inlineplus
	if code.IsCode() {
		// perform the instantiation
		ocode := code.GetCode()
		ncode := strings.ReplaceAll(ocode, "{0}", varName)
		if ncode != ocode {
			inlined = true
			code = `Code(ncode)
		}
	}
	if !g.inline || !code.IsCode() || !inlined {
		returnType := ""
		argType := ""
		functionName := g.declName("tom_get_tail_" + opname)
		%match(opNameAST) {
			EmptyName() -> {
				panic(&platform.RuntimeError{Msg: genericBadCase + platform.JavaString(opNameAST)})
			}
			Name(opName) -> {
				tomSymbol := g.getSymbolFromName(`opName)
				returnType = tools.GetTLType(tools.GetSymbolCodomain(tomSymbol))
				argType = returnType
			}
		}
		g.genDeclInstr(returnType, functionName, ty,
			[]string{argType, varName},
			`Return(ExpressionToBQTerm(code)), deep, moduleName)
	}
}

// buildIsEmptyDecl is GenericGenerator.buildIsEmptyDecl (GenericGenerator.t:685).
func (g *goGenerator) buildIsEmptyDecl(deep int, opNameAST TomName, varName, ty string, tlType TargetLanguageType, code Expression, moduleName string) {
	opname := opNameAST.GetString()
	inlined := g.inlineplus
	if code.IsCode() {
		// perform the instantiation
		ocode := code.GetCode()
		ncode := strings.ReplaceAll(ocode, "{0}", varName)
		if ncode != ocode {
			inlined = true
			code = `Code(ncode)
		}
	}
	if !g.inline || !code.IsCode() || !inlined {
		argType := ""
		functionName := g.declName("tom_is_empty_" + opname)
		%match(opNameAST) {
			EmptyName() -> {
				panic(&platform.RuntimeError{Msg: genericBadCase + platform.JavaString(opNameAST)})
			}
			Name(opName) -> {
				tomSymbol := g.getSymbolFromName(`opName)
				argType = tools.GetTLType(tools.GetSymbolCodomain(tomSymbol))
			}
		}
		g.genDeclInstr(tools.GetTLType(g.getSymbolTable(moduleName).GetBooleanType()),
			functionName, ty,
			[]string{argType, varName},
			`Return(ExpressionToBQTerm(code)), deep, moduleName)
	}
}

// buildGetElementDecl is GenericGenerator.buildGetElementDecl (GenericGenerator.t:721).
func (g *goGenerator) buildGetElementDecl(deep int, opNameAST TomName, name1, name2, type1 string, code Expression, moduleName string) {
	opname := opNameAST.GetString()
	inlined := g.inlineplus
	if code.IsCode() {
		// perform the instantiation
		ocode := code.GetCode()
		ncode := strings.ReplaceAll(strings.ReplaceAll(ocode, "{0}", name1), "{1}", name2)
		if ncode != ocode {
			inlined = true
			code = `Code(ncode)
		}
	}
	if !g.inline || !code.IsCode() || !inlined {
		functionName := g.declName("tom_get_element")
		%match(opNameAST) {
			EmptyName() -> {
				panic(&platform.RuntimeError{Msg: genericBadCase + platform.JavaString(opNameAST)})
			}
		}
		tomSymbol := g.getSymbolFromName(opname)
		argType := tools.GetTLType(tools.GetSymbolCodomain(tomSymbol))
		returnType := tools.GetTLType(tools.GetSymbolDomain(tomSymbol).GetHeadconcTomType())
		g.genDeclInstr(returnType,
			functionName+"_"+opname, type1,
			[]string{argType, name1,
				tools.GetTLType(g.getSymbolTable(moduleName).GetIntType()), name2},
			`Return(ExpressionToBQTerm(code)), deep, moduleName)
	}
}

// buildGetSizeDecl is GoGenerator.buildGetSizeDecl (GoGenerator.t:663),
// which masks GenericGenerator.buildGetSizeDecl (GenericGenerator.t:759).
// get_size returns the Tom int type (int64), but the natural Go hook body
// is len($l), of type int: convert explicitly.
func (g *goGenerator) buildGetSizeDecl(deep int, opNameAST TomName, name1, ty string, code Expression, moduleName string) {
	if code.IsCode() {
		it := g.goType(g.getSymbolTable(moduleName).GetIntType())
		body := javaTrim(code.GetCode())
		// a statement-style body `len($l);` cannot go inside int64(...)
		for strings.HasSuffix(body, ";") {
			body = javaTrim(body[:len(body)-1])
		}
		code = `Code(it + "(" + body + ")")
	}
	g.buildGetSizeDeclGeneric(deep, opNameAST, name1, ty, code, moduleName)
}

// buildGetSizeDeclGeneric is GenericGenerator.buildGetSizeDecl
// (GenericGenerator.t:759), called by super (R4).
func (g *goGenerator) buildGetSizeDeclGeneric(deep int, opNameAST TomName, name1, ty string, code Expression, moduleName string) {
	opname := opNameAST.GetString()
	inlined := g.inlineplus
	if code.IsCode() {
		// perform the instantiation
		ocode := code.GetCode()
		ncode := strings.ReplaceAll(ocode, "{0}", name1)
		if ncode != ocode {
			inlined = true
			code = `Code(ncode)
		}
	}
	if !g.inline || !code.IsCode() || !inlined {
		argType := ""
		functionName := g.declName("tom_get_size")
		%match(opNameAST) {
			EmptyName() -> {
				panic(&platform.RuntimeError{Msg: genericBadCase + platform.JavaString(opNameAST)})
			}
			Name(opName) -> {
				tomSymbol := g.getSymbolFromName(`opName)
				argType = tools.GetTLType(tools.GetSymbolCodomain(tomSymbol))
			}
		}
		g.genDeclInstr(tools.GetTLType(g.getSymbolTable(moduleName).GetIntType()),
			functionName+"_"+opname, ty,
			[]string{argType, name1},
			`Return(ExpressionToBQTerm(code)), deep, moduleName)
	}
}

// buildExpGetElement is GenericGenerator.buildExpGetElement (GenericGenerator.t:797).
func (g *goGenerator) buildExpGetElement(deep int, opNameAST TomName, domain TomType, varName, varIndex BQTerm, moduleName string) {
	deep = 0 // Java: to avoid useless spaces inside the code
	%match(opNameAST) {
		EmptyName() -> {
			panic(&platform.RuntimeError{Msg: genericBadCase + platform.JavaString(opNameAST)})
		}
	}
	opName := opNameAST.GetString()
	sType := javaTomType(domain)
	template, hasTemplate := g.getSymbolTable(moduleName).GetGetElementArray(opName)
	if !g.instantiateTemplate(deep, template, hasTemplate, opName, `concBQTerm(varName, varIndex), moduleName) {
		g.output.Write(g.declName("tom_get_element_"+opName+"_"+sType) + "(")
		g.generateBQTerm(deep, varName, moduleName)
		g.output.Write(",")
		g.generateBQTerm(deep, varIndex, moduleName)
		g.output.Write(")")
	}
}

// buildListOrArray is GenericGenerator.buildListOrArray (GenericGenerator.t:817).
func (g *goGenerator) buildListOrArray(deep int, list BQTerm, moduleName string) {
	deep = 0 // Java: to avoid useless spaces inside the code
	%match(list) {
		BuildEmptyList(Name(name)) -> {
			prefix := g.declName("tom_empty_list_")
			template, hasTemplate := g.getSymbolTable(moduleName).GetMakeEmptyList(`name)
			if !g.instantiateTemplate(deep, template, hasTemplate, `name, `concBQTerm(), moduleName) {
				g.output.Write(prefix + `name + "()")
			}
			return
		}
		BuildConsList(Name(name), headTerm, tailTerm) -> {
			prefix := g.declName("tom_cons_list_")
			template, hasTemplate := g.getSymbolTable(moduleName).GetMakeAddList(`name)
			if !g.instantiateTemplate(deep, template, hasTemplate, `name, `concBQTerm(headTerm, tailTerm), moduleName) {
				g.output.Write(prefix + `name + "(")
				g.generateBQTerm(deep, `headTerm, moduleName)
				g.output.Write(",")
				g.generateBQTerm(deep, `tailTerm, moduleName)
				g.output.Write(")")
			}
			return
		}
		BuildAppendList(Name(name), headTerm, tailTerm) -> {
			g.output.Write(g.declName("tom_append_list_"+`name) + "(")
			g.generateBQTerm(deep, `headTerm, moduleName)
			g.output.Write(",")
			g.generateBQTerm(deep, `tailTerm, moduleName)
			g.output.Write(")")
			return
		}
		BuildEmptyArray(Name(name), size) -> {
			prefix := g.declName("tom_empty_array_")
			template, hasTemplate := g.getSymbolTable(moduleName).GetMakeEmptyArray(`name)
			if !g.instantiateTemplate(deep, template, hasTemplate, `name, `concBQTerm(size), moduleName) {
				g.output.Write(prefix + `name + "(")
				g.generateBQTerm(deep, `size, moduleName)
				g.output.Write(")")
			}
			return
		}
		BuildConsArray(Name(name), headTerm, tailTerm) -> {
			template, hasTemplate := g.getSymbolTable(moduleName).GetMakeAddArray(`name)
			if !g.instantiateTemplate(deep, template, hasTemplate, `name, `concBQTerm(headTerm, tailTerm), moduleName) {
				prefix := g.declName("tom_cons_array_")
				g.output.Write(prefix + `name + "(")
				g.generateBQTerm(deep, `headTerm, moduleName)
				g.output.Write(",")
				g.generateBQTerm(deep, `tailTerm, moduleName)
				g.output.Write(")")
			}
			return
		}
		BuildAppendArray(Name(name), headTerm, tailTerm) -> {
			g.output.Write(g.declName("tom_append_array_"+`name) + "(")
			g.generateBQTerm(deep, `headTerm, moduleName)
			g.output.Write(",")
			g.generateBQTerm(deep, `tailTerm, moduleName)
			g.output.Write(")")
			return
		}
	}
}

// genDeclList is GoGenerator.genDeclList (GoGenerator.t:618): the append
// and slice functions of a list operator.
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
	if listType == eltType { // Java: == on shared terms
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

// genDeclArray is GoGenerator.genDeclArray (GoGenerator.t:677), which
// masks GenericGenerator.genDeclArray (GenericGenerator.t:900): the slice
// and append functions of an array operator, only when its destructors are
// used.
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

// The generated mapping code (adt.tom) names aterm types; this keeps the import used.
var _ = aterm.GetInstance

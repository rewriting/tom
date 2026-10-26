package backend

// The traversal of AbstractGenerator (src/tom/engine/backend/AbstractGenerator.t:104-956),
// with the GoGenerator versions that mask its methods (R4: the masked
// method is kept as <name>Abstract for the super. call), and the methods of
// the skeleton family F1 (plan J4.1b, Task 2): buildComment, buildReturn,
// buildSymbolDecl, buildIsSortDecl, buildEqualTermDecl, genDeclInstr,
// genDecl, and buildInstructionSequence (see its comment). The methods of the other families are stubs (notported.go)
// until their task ports them.

import (
	"regexp"
	"strings"

	. "tom/engine/adt"
	"tom/engine/platform"
	"tom/engine/tools"
	"tom/library/aterm"
)

%include { adt.tom }

// generate is AbstractGenerator.generate (AbstractGenerator.t:104).
func (g *goGenerator) generate(deep int, subject Code, moduleName string) {
	%match(subject) {
		Tom(l) -> { g.generateList(deep, `l, moduleName); return }
		TomInclude(l) -> { g.generateListInclude(deep, `l, moduleName); return }
		BQTermToCode(t) -> { g.generateBQTerm(deep, `t, moduleName); return }
		TargetLanguageToCode(t) -> { g.generateTargetLanguage(deep, `t, moduleName); return }
		InstructionToCode(t) -> { g.generateInstruction(deep, `t, moduleName); return }
		DeclarationToCode(t) -> { g.generateDeclaration(deep, `t, moduleName); return }
	}
	// port: Java also prints the message on stdout (R8)
	panic(&platform.RuntimeError{Msg: "Cannot generate code for: " + platform.JavaString(subject)})
}

// generateTomTerm is AbstractGenerator.generateTomTerm (AbstractGenerator.t:150):
// only variables; any other term writes nothing, as in Java.
func (g *goGenerator) generateTomTerm(deep int, subject TomTerm, moduleName string) {
	%match(subject) {
		v@(Variable|VariableStar)[] -> {
			g.output.WriteDeep(deep, g.getVariableName(`v))
			return
		}
	}
}

// generateBQTerm is GoGenerator.generateBQTerm (GoGenerator.t:213), which
// masks AbstractGenerator.generateBQTerm (AbstractGenerator.t:166): a Java
// long literal (32l, 7L) loses its suffix.
func (g *goGenerator) generateBQTerm(deep int, subject BQTerm, moduleName string) {
	%match(subject) {
		BuildConstant[AstName=Name(name)] -> {
			if longLiteral.MatchString(`name) {
				g.output.Write(`name[:len(`name)-1])
				return
			}
		}
	}
	g.generateBQTermAbstract(deep, subject, moduleName)
}

// longLiteral is the regular expression of GoGenerator.t:216
// (String.matches: the whole string).
var longLiteral = regexp.MustCompile("^-?[0-9]+[lL]$")

// generateBQTermAbstract is AbstractGenerator.generateBQTerm (AbstractGenerator.t:166).
func (g *goGenerator) generateBQTermAbstract(deep int, subject BQTerm, moduleName string) {
	%match(subject) {
		BuildConstant[AstName=Name(name)] -> {
			g.output.Write(`name)
			return
		}
		BuildTerm(Name(name), argList, myModuleName) -> {
			g.buildTerm(deep, `name, `argList, `myModuleName)
			return
		}
		l@(BuildEmptyList|BuildEmptyArray|BuildConsList|BuildAppendList|BuildConsArray|BuildAppendArray)[] -> {
			g.buildListOrArray(deep, `l, moduleName)
			return
		}
		FunctionCall[AstName=Name(name), Args=argList] -> {
			g.buildFunctionCall(deep, `name, `argList, moduleName)
			return
		}
		v@(BQVariable|BQVariableStar)[] -> {
			g.output.WriteDeep(deep, g.getVariableNameBQ(`v))
			return
		}
		ExpressionToBQTerm(t) -> {
			g.generateExpression(deep, `t, moduleName)
			return
		}
		Composite(_*, t, _*) -> {
			%match(t) {
				CompositeTL(target) -> {
					g.generateTargetLanguage(deep, `target, moduleName)
				}
				CompositeBQTerm(term) -> {
					g.generateBQTerm(deep, `term, moduleName)
				}
			}
		}
		t@!Composite(_*) -> {
			panic(&platform.RuntimeError{Msg: "Cannot generate code for bqterm " + platform.JavaString(`t)})
		}
	}
}

// getVariableNameBQ is GoGenerator.getVariableName(BQTerm) (GoGenerator.t:835),
// which masks AbstractGenerator.getVariableName(BQTerm) (AbstractGenerator.t:216):
// a %strategy parameter is a field of the receiver.
func (g *goGenerator) getVariableNameBQ(v BQTerm) string {
	name := g.getVariableNameBQAbstract(v)
	if g.classFields[name] {
		return receiver + "." + name
	}
	return name
}

// getVariableNameBQAbstract is AbstractGenerator.getVariableName(BQTerm)
// (AbstractGenerator.t:216).
func (g *goGenerator) getVariableNameBQAbstract(v BQTerm) string {
	%match(v) {
		BQVariable[AstName=PositionName(l)] -> { return "tom" + tools.TomNumberListToString(`l) }
		BQVariable[AstName=Name(name)] -> { return `name }
		BQVariableStar[AstName=PositionName(l)] -> { return "tom" + tools.TomNumberListToString(`l) }
		BQVariableStar[AstName=Name(name)] -> { return `name }
	}
	panic(&platform.RuntimeError{Msg: "cannot generate the name of the variable " + platform.JavaString(v)})
}

// getVariableName is GoGenerator.getVariableName(TomTerm) (GoGenerator.t:840),
// which masks AbstractGenerator.getVariableName(TomTerm) (AbstractGenerator.t:237).
func (g *goGenerator) getVariableName(v TomTerm) string {
	name := g.getVariableNameAbstract(v)
	if g.classFields[name] {
		return receiver + "." + name
	}
	return name
}

// getVariableNameAbstract is AbstractGenerator.getVariableName(TomTerm)
// (AbstractGenerator.t:237).
func (g *goGenerator) getVariableNameAbstract(v TomTerm) string {
	%match(v) {
		Variable[AstName=PositionName(l)] -> { return "tom" + tools.TomNumberListToString(`l) }
		Variable[AstName=Name(name)] -> { return `name }
		VariableStar[AstName=PositionName(l)] -> { return "tom" + tools.TomNumberListToString(`l) }
		VariableStar[AstName=Name(name)] -> { return `name }
	}
	panic(&platform.RuntimeError{Msg: "cannot generate the name of the variable " + platform.JavaString(v)})
}

// generateExpression is GoGenerator.generateExpression (GoGenerator.t:198),
// which masks AbstractGenerator.generateExpression (AbstractGenerator.t:258):
// hook code (Code) is written without its surrounding whitespace.
func (g *goGenerator) generateExpression(deep int, subject Expression, moduleName string) {
	if subject.IsCode() {
		g.output.Write(javaTrim(subject.GetCode()))
		return
	}
	g.generateExpressionAbstract(deep, subject, moduleName)
}

// javaTrim is String.trim: the characters up to U+0020 are removed at both
// ends (strings.TrimSpace would also remove U+0085, U+00A0… and keep
// the other control characters).
func javaTrim(s string) string {
	return strings.TrimFunc(s, func(r rune) bool { return r <= ' ' })
}

// generateExpressionAbstract is AbstractGenerator.generateExpression
// (AbstractGenerator.t:258).
func (g *goGenerator) generateExpressionAbstract(deep int, subject Expression, moduleName string) {
	%match(subject) {
		Code(t) -> {
			g.output.Write(`t)
			return
		}
		Integer(n) -> {
			g.output.WriteInt(`n)
			return
		}
		Negation(exp) -> {
			g.buildExpNegation(deep, `exp, moduleName)
			return
		}
		Conditional(cond, exp1, exp2) -> {
			g.buildExpConditional(deep, `cond, `exp1, `exp2, moduleName)
			return
		}
		And(exp1, exp2) -> {
			g.buildExpAnd(deep, `exp1, `exp2, moduleName)
			return
		}
		Or(exp1, exp2) -> {
			g.buildExpOr(deep, `exp1, `exp2, moduleName)
			return
		}
		GreaterThan(exp1, exp2) -> {
			g.buildExpGreaterThan(deep, `exp1, `exp2, moduleName)
			return
		}
		GreaterOrEqualThan(exp1, exp2) -> {
			g.buildExpGreaterOrEqualThan(deep, `exp1, `exp2, moduleName)
			return
		}
		LessThan(exp1, exp2) -> {
			g.buildExpLessThan(deep, `exp1, `exp2, moduleName)
			return
		}
		LessOrEqualThan(exp1, exp2) -> {
			g.buildExpLessOrEqualThan(deep, `exp1, `exp2, moduleName)
			return
		}
		Bottom(tomType) -> {
			g.buildExpBottom(deep, `tomType, moduleName)
			return
		}
		TrueTL() -> {
			g.buildExpTrue(deep)
			return
		}
		FalseTL() -> {
			g.buildExpFalse(deep)
			return
		}
		IsEmptyList(Name(opName), expList) -> {
			g.buildExpIsEmptyList(deep, `opName, g.getTermTypeBQ(`expList), `expList, moduleName)
			return
		}
		IsEmptyArray(opNameAST, expArray, expIndex) -> {
			g.buildExpIsEmptyArray(deep, `opNameAST, g.getTermTypeBQ(`expArray), `expIndex, `expArray, moduleName)
			return
		}
		EqualTerm(ty, exp1, exp2) -> {
			g.buildExpEqualTerm(deep, `ty, `exp1, `exp2, moduleName)
			return
		}
		EqualBQTerm(ty, exp1, exp2) -> {
			g.buildExpEqualBQTerm(deep, `ty, `exp1, `exp2, moduleName)
			return
		}
		IsSort(Type[TomType=ty], exp) -> {
			g.buildExpIsSort(deep, `ty, `exp, moduleName)
			return
		}
		IsFsym(Name(opname), exp) -> {
			g.buildExpIsFsym(deep, `opname, `exp, moduleName)
			return
		}
		Cast(Type[TlType=tlType@TLType[]], exp) -> {
			g.buildExpCast(deep, `tlType, `exp, moduleName)
			return
		}
		GetSlot(_, Name(opname), slotName, v@(BQVariable|BuildTerm|ExpressionToBQTerm)[]) -> {
			g.buildExpGetSlot(deep, `opname, `slotName, `v, moduleName)
			return
		}
		GetSlot(_, Name(opname), slotName, v@ExpressionToBQTerm(GetSlot[])) -> {
			g.buildExpGetSlot(deep, `opname, `slotName, `v, moduleName)
			return
		}
		GetHead(Name(opName), codomain, exp) -> {
			g.buildExpGetHead(deep, `opName, g.getTermTypeBQ(`exp), `codomain, `exp, moduleName)
			return
		}
		GetTail(Name(opName), exp) -> {
			g.buildExpGetTail(deep, `opName, g.getTermTypeBQ(`exp), `exp, moduleName)
			return
		}
		AddOne(exp) -> {
			g.buildAddOne(deep, `exp, moduleName)
			return
		}
		SubstractOne(exp) -> {
			g.buildSubstractOne(deep, `exp, moduleName)
			return
		}
		Substract(exp1, exp2) -> {
			g.buildSubstract(deep, `exp1, `exp2, moduleName)
			return
		}
		GetSize(opNameAST, exp) -> {
			g.buildExpGetSize(deep, `opNameAST, g.getTermTypeBQ(`exp), `exp, moduleName)
			return
		}
		GetElement(opNameAST, _, varName, varIndex) -> {
			g.buildExpGetElement(deep, `opNameAST, g.getTermTypeBQ(`varName), `varName, `varIndex, moduleName)
			return
		}
		GetSliceList(Name(name), varBegin, varEnd, tailSlice) -> {
			g.buildExpGetSliceList(deep, `name, `varBegin, `varEnd, `tailSlice, moduleName)
			return
		}
		GetSliceArray(Name(name), varArray, varBegin, expEnd) -> {
			g.buildExpGetSliceArray(deep, `name, `varArray, `varBegin, `expEnd, moduleName)
			return
		}
		BQTermToExpression(t) -> {
			g.generateBQTerm(deep, `t, moduleName)
			return
		}
		TomInstructionToExpression(t) -> {
			g.generateInstruction(deep, `t, moduleName)
			return
		}
	}
	// port: Java also prints the message on stdout (R8)
	panic(&platform.RuntimeError{Msg: "Cannot generate code for expression: " + platform.JavaString(subject)})
}

// generateArray is AbstractGenerator.generateArray (AbstractGenerator.t:433):
// var[index].
func (g *goGenerator) generateArray(deep int, subject BQTerm, index BQTerm, moduleName string) {
	%match(subject) {
		BQVariable[AstName=PositionName(l)] -> {
			g.output.Write("tom" + tools.TomNumberListToString(`l))
		}
		BQVariable[AstName=Name(name)] -> {
			g.output.Write(`name)
		}
	}
	%match(index) {
		BQVariable[AstName=PositionName(l)] -> {
			g.output.Write("[")
			g.output.Write("tom" + tools.TomNumberListToString(`l))
			g.output.Write("]")
		}
		BQVariable[AstName=Name(name)] -> {
			g.output.Write("[")
			g.output.Write(`name)
			g.output.Write("]")
		}
		ExpressionToBQTerm(Integer(x)) -> {
			g.output.Write("[")
			g.output.WriteInt(`x)
			g.output.Write("]")
		}
	}
}

// generateInstruction is GoGenerator.generateInstruction (GoGenerator.t:291),
// which masks AbstractGenerator.generateInstruction (AbstractGenerator.t:461):
// "; " before a block that starts with a CompiledMatch, "_ = s" for the
// host subjects of a CompiledMatch (unused.t), "; " after a RawAction.
func (g *goGenerator) generateInstruction(deep int, subject Instruction, moduleName string) {
	%match(subject) {
		UnamedBlock(concInstruction(CompiledMatch[], _*)) -> { g.output.Write("; ") }
		CompiledMatch(automata, _) -> { g.buildUnreadSubjects(`automata) }
	}
	g.generateInstructionAbstract(deep, subject, moduleName)
	%match(subject) {
		RawAction[] -> { g.output.Write("; ") }
	}
}

// generateInstructionAbstract is AbstractGenerator.generateInstruction
// (AbstractGenerator.t:461).
func (g *goGenerator) generateInstructionAbstract(deep int, subject Instruction, moduleName string) {
	%match(subject) {
		CodeToInstruction(c) -> {
			g.generate(deep, `c, moduleName)
			return
		}
		BQTermToInstruction(t) -> {
			g.generateBQTerm(deep, `t, moduleName)
			return
		}
		ExpressionToInstruction(t) -> {
			g.generateExpression(deep, `t, moduleName)
			return
		}
		Nop() -> {
			return
		}
		Assign(v@(BQVariable|BQVariableStar)[Options=optionList], exp) -> {
			g.buildAssign(deep, `v, `optionList, `exp, moduleName)
			return
		}
		AssignArray(v@BQVariable[Options=optionList], index, exp) -> {
			g.buildAssignArray(deep, `v, `optionList, `index, `exp, moduleName)
			return
		}
		Let(v@(BQVariable|BQVariableStar)[Options=optionList, AstType=Type[TlType=tlType]], exp, body) -> {
			g.buildLet(deep, `v, `optionList, `tlType, `exp, `body, moduleName)
			return
		}
		LetRef(v@(BQVariable|BQVariableStar)[Options=optionList, AstType=Type[TlType=tlType]], exp, body) -> {
			g.buildLetRef(deep, `v, `optionList, `tlType, `exp, `body, moduleName)
			return
		}
		AbstractBlock(instList) -> {
			g.buildInstructionSequence(deep, `instList, moduleName)
			return
		}
		UnamedBlock(instList) -> {
			g.buildUnamedBlock(deep, `instList, moduleName)
			return
		}
		NamedBlock(blockName, instList) -> {
			g.buildNamedBlock(deep, `blockName, `instList, moduleName)
			return
		}
		If(exp, succesList, Nop()) -> {
			g.buildIf(deep, `exp, `succesList, moduleName)
			return
		}
		If(exp, Nop(), failureList) -> {
			g.buildIf(deep, `Negation(exp), `failureList, moduleName)
			return
		}
		If(exp, succesList, failureList) -> {
			g.buildIfWithFailure(deep, `exp, `succesList, `failureList, moduleName)
			return
		}
		DoWhile(succes, exp) -> {
			g.buildDoWhile(deep, `succes, `exp, moduleName)
			return
		}
		WhileDo(exp, succes) -> {
			g.buildWhileDo(deep, `exp, `succes, moduleName)
			return
		}
		RawAction[AstInstruction=AbstractBlock(instructionList)] -> {
			g.generateInstructionList(deep, `instructionList, moduleName)
			return
		}
		RawAction[AstInstruction=inst] -> {
			g.generateInstruction(deep, `inst, moduleName)
			return
		}
		Return(exp) -> {
			g.buildReturn(deep, `exp, moduleName)
			return
		}
		CompiledMatch(instruction, _) -> {
			g.generateInstruction(deep, `instruction, moduleName)
			return
		}
		CompiledPattern(_, instruction) -> {
			g.generateInstruction(deep, `instruction, moduleName)
			return
		}
		Tracelink(Name(ty), Name(name), _, expr, _) -> {
			g.buildTracelink(deep, `ty, `name, `expr, moduleName)
			return
		}
		TracelinkPopulateResolve[RefClassName=Name(refClassName), TracedLinks=tracedLinks, Current=current, Link=link] -> {
			g.buildTracelinkPopulateResolve(deep, `refClassName, `tracedLinks, `current, `link, moduleName)
			return
		}
		Resolve[ResolveBQTerm=resolveBQTerm] -> {
			g.generateBQTerm(deep, `resolveBQTerm, moduleName)
			return
		}
	}
	// port: Java also prints the message on stdout (R8)
	panic(&platform.RuntimeError{Msg: "Cannot generate code for instruction: " + platform.JavaString(subject)})
}

// generateTargetLanguage is AbstractGenerator.generateTargetLanguage
// (AbstractGenerator.t:596).
func (g *goGenerator) generateTargetLanguage(deep int, subject TargetLanguage, moduleName string) {
	%match(subject) {
		TL(t, TextPosition[Line=startLine], TextPosition[Line=endLine]) -> {
			length := `endLine - `startLine
			g.output.WriteTL(deep, `t, `startLine, length)
			return
		}
		ITL(t) -> {
			g.output.Write(`t)
			return
		}
		Comment(t) -> {
			g.buildComment(deep, `t)
			return
		}
	}
	// port: Java also prints the message on stdout (R8)
	panic(&platform.RuntimeError{Msg: "Cannot generate code for TL: " + platform.JavaString(subject)})
}

// generateOption is AbstractGenerator.generateOption (AbstractGenerator.t:618).
func (g *goGenerator) generateOption(deep int, subject Option, moduleName string) {
	%match(subject) {
		DeclarationToOption(decl) -> {
			g.generateDeclaration(deep, `decl, moduleName)
			return
		}
		OriginTracking[] -> { return }
		ACSymbol() -> { return }
	}
	panic(&platform.RuntimeError{Msg: "Cannot generate code for option: " + platform.JavaString(subject)})
}

// generateDeclaration is AbstractGenerator.generateDeclaration
// (AbstractGenerator.t:635): a mapping function is generated only for the
// symbols and types the Collector marked used.
func (g *goGenerator) generateDeclaration(deep int, subject Declaration, moduleName string) {
	%match(subject) {
		EmptyDeclaration() -> {
			return
		}
		AbstractDecl(declList) -> {
			g.generateDeclarationList(deep, `declList, moduleName)
			return
		}
		FunctionDef(Name(tomName), argList, codomain, throwsType, instruction) -> {
			g.buildFunctionDef(deep, `tomName, `argList, `codomain, `throwsType, `instruction, moduleName)
			return
		}
		MethodDef(Name(tomName), argList, codomain, throwsType, instruction) -> {
			g.buildMethodDef(deep, `tomName, `argList, `codomain, `throwsType, `instruction, moduleName)
			return
		}
		Class(Name(tomName), extendsType, superTerm, declaration) -> {
			g.buildClass(deep, `tomName, `extendsType, `superTerm, `declaration, moduleName)
			return
		}
		IntrospectorClass(Name(tomName), declaration) -> {
			g.buildIntrospectorClass(deep, `tomName, `declaration, moduleName)
			return
		}
		SymbolDecl(Name(tomName)) -> {
			g.buildSymbolDecl(deep, `tomName, moduleName)
			return
		}
		ArraySymbolDecl(Name(tomName)) -> {
			if g.getSymbolTable(moduleName).IsUsedSymbolConstructorName(`tomName) ||
				g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(`tomName) {
				g.buildSymbolDecl(deep, `tomName, moduleName)
				g.genDeclArray(`tomName, moduleName)
			}
			return
		}
		(ListSymbolDecl|ACSymbolDecl)(Name(tomName)) -> {
			if g.getSymbolTable(moduleName).IsUsedSymbolConstructorName(`tomName) ||
				g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(`tomName) {
				g.buildSymbolDecl(deep, `tomName, moduleName)
				g.genDeclList(`tomName, moduleName)
			}
			return
		}
		IsFsymDecl(Name(tomName), BQVariable[AstName=Name(varname), AstType=Type[TlType=tlType@TLType[]]], code, _) -> {
			if g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(`tomName) {
				g.buildIsFsymDecl(deep, `tomName, `varname, `tlType, `code, moduleName)
			}
			return
		}
		GetSlotDecl[AstName=Name(tomName), SlotName=slotName, Variable=BQVariable[AstName=Name(name), AstType=Type[TlType=tlType@TLType[]]], Expr=code] -> {
			if g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(`tomName) {
				g.buildGetSlotDecl(deep, `tomName, `name, `tlType, `code, `slotName, moduleName)
			}
			return
		}
		ImplementDecl[AstName=Name(_)] -> {
			// Java: nothing for the moment
			return
		}
		GetDefaultDecl[AstName=Name(tomName), SlotName=slotName, Expr=code] -> {
			if g.getSymbolTable(moduleName).IsUsedSymbolConstructorName(`tomName) {
				g.buildGetDefaultDecl(deep, `tomName, `code, `slotName, moduleName)
			}
			return
		}
		EqualTermDecl(BQVariable[AstName=Name(name1), AstType=Type[TomType=type1]], BQVariable[AstName=Name(name2), AstType=Type[TomType=type2]], code, _) -> {
			if g.getSymbolTable(moduleName).IsUsedTypeName(`type1) {
				g.buildEqualTermDecl(deep, `name1, `name2, `type1, `type2, `code, moduleName)
			}
			return
		}
		IsSortDecl(BQVariable[AstName=Name(varName), AstType=Type[TomType=ty]], expr, _) -> {
			if g.getSymbolTable(moduleName).IsUsedTypeName(`ty) {
				g.buildIsSortDecl(deep, `varName, `ty, `expr, moduleName)
			}
			return
		}
		BQTermToDeclaration(bqterm) -> {
			g.generateBQTerm(deep, `bqterm, moduleName)
			return
		}
		ResolveIsFsymDecl(Name(tomName), BQVariable[AstName=Name(varname), AstType=Type[TlType=tlType@TLType[]]], _) -> {
			if g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(`tomName) {
				g.buildResolveIsFsymDecl(deep, `tomName, `varname, `tlType, moduleName)
			}
			return
		}
		ResolveGetSlotDecl[AstName=Name(tomName), SlotName=slotName, Variable=BQVariable[AstName=Name(name), AstType=Type[TlType=tlType@TLType[]]]] -> {
			if g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(`tomName) {
				g.buildResolveGetSlotDecl(deep, `tomName, `name, `tlType, `slotName, moduleName)
			}
			return
		}
		ResolveMakeDecl(Name(opname), returnType, argList, _) -> {
			if g.getSymbolTable(moduleName).IsUsedSymbolConstructorName(`opname) {
				g.genResolveDeclMake(g.declName("tom_make_"), `opname, `returnType, `argList, moduleName)
			}
			return
		}
		ResolveClassDecl[WithName=wName, ToName=tName, Extends=eName] -> {
			g.buildResolveClass(`wName, `tName, `eName, moduleName)
			return
		}
		ResolveInverseLinksDecl[FileFrom=fileFrom, FileTo=fileTo, ResolveNameList=resolveNameList] -> {
			g.buildResolveInverseLinks(deep, `fileFrom, `fileTo, `resolveNameList, moduleName)
			return
		}
		ReferenceClass[RefName=Name(refname), Fields=refclassTInstructions] -> {
			g.buildReferenceClass(deep, `refname, `refclassTInstructions, moduleName)
			return
		}
		GetHeadDecl[Opname=opNameAST@Name(opname), Codomain=Type[TlType=codomain], Variable=BQVariable[AstName=Name(varName), AstType=Type[TomType=suffix, TlType=domain@TLType[]]], Expr=expr] -> {
			if g.getSymbolTable(moduleName).IsUsedSymbolConstructorName(`opname) ||
				g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(`opname) {
				g.buildGetHeadDecl(deep, `opNameAST, `varName, `suffix, `domain, `codomain, `expr, moduleName)
			}
			return
		}
		GetTailDecl[Opname=opNameAST@Name(opname), Variable=BQVariable[AstName=Name(varName), AstType=Type[TomType=ty, TlType=tlType@TLType[]]], Expr=expr] -> {
			if g.getSymbolTable(moduleName).IsUsedSymbolConstructorName(`opname) ||
				g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(`opname) {
				g.buildGetTailDecl(deep, `opNameAST, `varName, `ty, `tlType, `expr, moduleName)
			}
			return
		}
		IsEmptyDecl[Opname=opNameAST@Name(opname), Variable=BQVariable[AstName=Name(varName), AstType=Type[TomType=ty, TlType=tlType@TLType[]]], Expr=expr] -> {
			if g.getSymbolTable(moduleName).IsUsedSymbolConstructorName(`opname) ||
				g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(`opname) {
				g.buildIsEmptyDecl(deep, `opNameAST, `varName, `ty, `tlType, `expr, moduleName)
			}
			return
		}
		MakeEmptyList(Name(opname), instr, _) -> {
			returnType := tools.GetSymbolCodomain(g.getSymbolFromName(`opname))
			if g.getSymbolTable(moduleName).IsUsedSymbolConstructorName(`opname) ||
				g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(`opname) {
				g.genDeclMake(g.declName("tom_empty_list_"), `opname, returnType, `concBQTerm(), `instr, moduleName)
			}
			return
		}
		MakeAddList(Name(opname), elt@BQVariable[AstType=Type[TlType=TLType[]]], list@BQVariable[AstType=fullListType@Type[TlType=TLType[]]], instr, _) -> {
			returnType := `fullListType
			if g.getSymbolTable(moduleName).IsUsedSymbolConstructorName(`opname) ||
				g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(`opname) {
				g.genDeclMake(g.declName("tom_cons_list_"), `opname, returnType, `concBQTerm(elt, list), `instr, moduleName)
			}
			return
		}
		GetElementDecl[Opname=opNameAST@Name(opname), Variable=BQVariable[AstName=Name(name1), AstType=Type[TomType=type1, TlType=TLType[]]], Index=BQVariable[AstName=Name(name2)], Expr=code] -> {
			if g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(`opname) {
				g.buildGetElementDecl(deep, `opNameAST, `name1, `name2, `type1, `code, moduleName)
			}
			return
		}
		GetSizeDecl[Opname=opNameAST@Name(opname), Variable=BQVariable[AstName=Name(name), AstType=Type[TomType=ty, TlType=TLType[]]], Expr=code] -> {
			if g.getSymbolTable(moduleName).IsUsedSymbolDestructorName(`opname) {
				g.buildGetSizeDecl(deep, `opNameAST, `name, `ty, `code, moduleName)
			}
			return
		}
		MakeEmptyArray(Name(opname), BQVariable[Options=optionList, AstName=name], instr, _) -> {
			returnType := tools.GetSymbolCodomain(g.getSymbolFromName(`opname))
			intType := g.getSymbolTable(moduleName).GetIntType()
			newVar := `BQVariable(optionList, name, intType)
			if g.getSymbolTable(moduleName).IsUsedSymbolConstructorName(`opname) {
				g.genDeclMake(g.declName("tom_empty_array_"), `opname, returnType, `concBQTerm(newVar), `instr, moduleName)
			}
			return
		}
		MakeAddArray(Name(opname), elt@BQVariable[AstType=Type[TlType=TLType[]]], list@BQVariable[AstType=fullArrayType@Type[TlType=TLType[]]], instr, _) -> {
			returnType := `fullArrayType
			if g.getSymbolTable(moduleName).IsUsedSymbolConstructorName(`opname) {
				g.genDeclMake(g.declName("tom_cons_array_"), `opname, returnType, `concBQTerm(elt, list), `instr, moduleName)
			}
			return
		}
		MakeDecl(Name(opname), returnType, argList, instr, _) -> {
			if g.getSymbolTable(moduleName).IsUsedSymbolConstructorName(`opname) {
				g.genDeclMake(g.declName("tom_make_"), `opname, `returnType, `argList, `instr, moduleName)
			}
			return
		}
		TypeTermDecl[Declarations=declList] -> {
			g.generateDeclarationList(deep, `declList, moduleName)
			return
		}
	}
	// port: Java also prints the message on stdout (R8)
	panic(&platform.RuntimeError{Msg: "Cannot generate code for declaration: " + platform.JavaString(subject)})
}

// generateListInclude is AbstractGenerator.generateListInclude
// (AbstractGenerator.t:908): the code of an included file on one line.
func (g *goGenerator) generateListInclude(deep int, subject CodeList, moduleName string) {
	g.output.SetSingleLine()
	g.generateList(deep, subject, moduleName)
	g.output.UnsetSingleLine()
}

// generateList is AbstractGenerator.generateList (AbstractGenerator.t:914).
func (g *goGenerator) generateList(deep int, subject CodeList, moduleName string) {
	for !subject.IsEmptyconcCode() {
		g.generate(deep, subject.GetHeadconcCode(), moduleName)
		subject = subject.GetTailconcCode()
	}
}

// generateOptionList is AbstractGenerator.generateOptionList (AbstractGenerator.t:922).
func (g *goGenerator) generateOptionList(deep int, subject OptionList, moduleName string) {
	for !subject.IsEmptyconcOption() {
		g.generateOption(deep, subject.GetHeadconcOption(), moduleName)
		subject = subject.GetTailconcOption()
	}
}

// generateInstructionList is AbstractGenerator.generateInstructionList
// (AbstractGenerator.t:930).
func (g *goGenerator) generateInstructionList(deep int, subject InstructionList, moduleName string) {
	for !subject.IsEmptyconcInstruction() {
		g.generateInstruction(deep, subject.GetHeadconcInstruction(), moduleName)
		subject = subject.GetTailconcInstruction()
	}
}

// generateDeclarationList is AbstractGenerator.generateDeclarationList
// (AbstractGenerator.t:942).
func (g *goGenerator) generateDeclarationList(deep int, subject DeclarationList, moduleName string) {
	for !subject.IsEmptyconcDeclaration() {
		g.generateDeclaration(deep, subject.GetHeadconcDeclaration(), moduleName)
		subject = subject.GetTailconcDeclaration()
	}
}

// generatePairNameDeclList is AbstractGenerator.generatePairNameDeclList
// (AbstractGenerator.t:950).
func (g *goGenerator) generatePairNameDeclList(deep int, pairNameDeclList PairNameDeclList, moduleName string) {
	for !pairNameDeclList.IsEmptyconcPairNameDecl() {
		g.generateDeclaration(deep, pairNameDeclList.GetHeadconcPairNameDecl().GetSlotDecl(), moduleName)
		pairNameDeclList = pairNameDeclList.GetTailconcPairNameDecl()
	}
}

// buildComment is GoGenerator.buildComment (GoGenerator.t:246).
func (g *goGenerator) buildComment(deep int, text string) {
	g.output.Write("/* " + strings.ReplaceAll(text, "*/", "* /") + " */ ")
}

// buildInstructionSequence is GoGenerator.buildInstructionSequence
// (GoGenerator.t:308): an AbstractBlock is its instructions in sequence.
// port: listed in family F2 (Task 4) by the plan's inventory, ported with
// F1 because the %gom blocks of F1 sources (gom/TestFrom.t,
// gom/TestTermgraph.t) are an InstructionToCode(AbstractBlock(…)).
func (g *goGenerator) buildInstructionSequence(deep int, instructionList InstructionList, moduleName string) {
	g.generateInstructionList(deep, instructionList, moduleName)
}

// buildReturn is GoGenerator.buildReturn (GoGenerator.t:346).
func (g *goGenerator) buildReturn(deep int, exp BQTerm, moduleName string) {
	g.output.Write("return ")
	g.generateBQTerm(deep, exp, moduleName)
	g.output.Write("; ")
}

// buildSymbolDecl is GenericGenerator.buildSymbolDecl (GenericGenerator.t:141):
// the declarations of the options, then of the slots, of a symbol. Java: a
// NullPointerException for an unknown symbol; here the nil dereference is
// a runtime error too (platform.IsRuntime).
func (g *goGenerator) buildSymbolDecl(deep int, tomName string, moduleName string) {
	tomSymbol := g.getSymbolTable(moduleName).GetSymbolFromName(tomName)
	optionList := tomSymbol.GetOptions()
	pairNameDeclList := tomSymbol.GetPairNameDeclList()
	// inspect the optionList
	g.generateOptionList(deep, optionList, moduleName)
	// inspect the slotlist
	g.generatePairNameDeclList(deep, pairNameDeclList, moduleName)
}

// buildIsSortDecl is GenericGenerator.buildIsSortDecl (GenericGenerator.t:587).
// The inlined test is ported as in Java (R10): inline is false with -go.
func (g *goGenerator) buildIsSortDecl(deep int, varName string, ty string, code Expression, moduleName string) {
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
		argType := g.getUniversalType()
		if g.getSymbolTable(moduleName).IsBuiltinType(ty) {
			argType = g.getSymbolTable(moduleName).GetBuiltinType(ty)
		}
		g.genDeclInstr(tools.GetTLType(g.getSymbolTable(moduleName).GetBooleanType()), g.declName("tom_is_sort"), ty,
			[]string{tools.GetTLType(argType), varName},
			`Return(ExpressionToBQTerm(code)), deep, moduleName)
	}
}

// buildEqualTermDecl is GoGenerator.buildEqualTermDecl (GoGenerator.t:563),
// which masks GenericGenerator.buildEqualTermDecl (GenericGenerator.t:557):
// equals receives the sort's Go type (mappings call methods on it).
func (g *goGenerator) buildEqualTermDecl(deep int, varname1, varname2, type1, type2 string, code Expression, moduleName string) {
	if code.IsCode() {
		ncode := strings.ReplaceAll(strings.ReplaceAll(code.GetCode(), "{0}", varname1), "{1}", varname2)
		code = `Code(ncode)
	}
	g.genDeclInstr(g.goType(g.getSymbolTable(moduleName).GetBooleanType()), g.declName("tom_equal_term"), type1,
		[]string{g.sortGoType(type1, moduleName), varname1, g.sortGoType(type2, moduleName), varname2},
		`Return(ExpressionToBQTerm(code)), deep, moduleName)
}

// genDeclInstr is GoGenerator.genDeclInstr (GoGenerator.t:537): func
// <declName>_<suffix>(<name> <type>, …) <returnType> { <instr> }; args
// holds type, name pairs.
func (g *goGenerator) genDeclInstr(returnType, declName, suffix string, args []string, instr Instruction, deep int, moduleName string) {
	if g.nodeclMode {
		return
	}
	var s strings.Builder
	s.WriteString("func " + declName + "_" + suffix + "(")
	for i := 0; i < len(args); {
		s.WriteString(args[i+1] + " " + args[i])
		i += 2
		if i < len(args) {
			s.WriteString(", ")
		}
	}
	s.WriteString(") " + returnType + " { ")
	g.output.Write(s.String())
	g.generateInstruction(deep, instr, moduleName)
	g.output.Write(" }; ")
}

// genDecl is GoGenerator.genDecl (GoGenerator.t:557): never used with -go.
func (g *goGenerator) genDecl(returnType, declName, suffix string, args []string, tlCode TargetLanguage, moduleName string) {
	panic(&platform.RuntimeError{Msg: "GoGenerator.genDecl is not used"})
}

// The generated mapping code (adt.tom) names aterm types; this keeps the import used.
var _ = aterm.GetInstance

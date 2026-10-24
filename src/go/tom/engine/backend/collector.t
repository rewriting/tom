package backend

// The Collector pass of BackendPlugin (src/tom/engine/backend/BackendPlugin.t:246-598):
// before the generator runs, it marks the symbols and types the code uses
// and fills the inliners of the symbol table (plan J4.1b, Task 1).

import (
	"fmt"
	"strings"

	. "tom/engine/adt"
	"tom/engine/bridge"
	"tom/engine/tools"
	"tom/library/aterm"
	"tom/library/sl"
)

%include { adt.tom }
%include { sl.tom }

// backendPlugin is the state of BackendPlugin the Collector uses: the
// symbol table of the compilation.
type backendPlugin struct {
	st *tools.SymbolTable
}

// getSymbolTable is BackendPlugin.getSymbolTable (BackendPlugin.t:237): a
// single table for every module, the argument is ignored.
func (bp *backendPlugin) getSymbolTable(moduleName string) *tools.SymbolTable {
	return bp.st
}

// stringStack is the Stack<String> of module names (BackendPlugin.t:265).
type stringStack struct{ items []string }

func (s *stringStack) push(x string) { s.items = append(s.items, x) }

// peek is Stack.peek; ok is false where Java throws EmptyStackException.
func (s *stringStack) peek() (string, bool) {
	if len(s.items) == 0 {
		return "", false
	}
	return s.items[len(s.items)-1], true
}

%typeterm BackendPlugin {
	implement { *backendPlugin }
}

%typeterm StringStack {
	implement { *stringStack }
}

// markUsedConstructorDestructor is BackendPlugin.markUsedConstructorDestructor
// (BackendPlugin.t:264).
func (bp *backendPlugin) markUsedConstructorDestructor(pilCode Code) {
	stack := &stringStack{}
	stack.push(tools.DefaultModuleName)
	// Java: catch(VisitFailure e) { /* Ignored */ }
	_, _ = `mu(MuVar("markStrategy"),TopDownCollect(Collector(MuVar("markStrategy"),bp,stack))).VisitLight(pilCode, sl.VisitableIntrospector{})
}

// setUsedSymbolConstructor is BackendPlugin.setUsedSymbolConstructor
// (BackendPlugin.t:272).
func (bp *backendPlugin) setUsedSymbolConstructor(moduleName string, tomSymbol TomSymbol, markStrategy sl.Strategy) {
	st := bp.getSymbolTable(moduleName)
	if !st.IsUsedSymbolConstructor(tomSymbol) && !st.IsUsedSymbolDestructor(tomSymbol) {
		_, _ = markStrategy.VisitLight(tomSymbol, sl.VisitableIntrospector{}) // Java: catch(VisitFailure) ignored
	}
	bp.getSymbolTable(moduleName).SetUsedSymbolConstructor(tomSymbol)
}

// setUsedSymbolDestructor is BackendPlugin.setUsedSymbolDestructor
// (BackendPlugin.t:282).
func (bp *backendPlugin) setUsedSymbolDestructor(moduleName string, tomSymbol TomSymbol, markStrategy sl.Strategy) {
	st := bp.getSymbolTable(moduleName)
	if !st.IsUsedSymbolConstructor(tomSymbol) && !st.IsUsedSymbolDestructor(tomSymbol) {
		_, _ = markStrategy.VisitLight(tomSymbol, sl.VisitableIntrospector{}) // Java: catch(VisitFailure) ignored
	}
	bp.getSymbolTable(moduleName).SetUsedSymbolDestructor(tomSymbol)
}

// setUsedType is BackendPlugin.setUsedType (BackendPlugin.t:292).
func (bp *backendPlugin) setUsedType(moduleName, tomTypeName string, markStrategy sl.Strategy) {
	bp.getSymbolTable(moduleName).SetUsedTypeName(tomTypeName)
}

// Collector is the %strategy Collector of BackendPlugin (BackendPlugin.t:301).
// port: where Java catches EmptyStackException and prints "No moduleName in
// stack" on stdout (which carries the generated code here), the rule does
// nothing; the stack always holds the default module name, as in Java.
// The module names pushed for a CompiledMatch are never popped, as in
// Java (the pop is commented out, BackendPlugin.t:326).
%strategy Collector(markStrategy:Strategy, bp:BackendPlugin, stack:StringStack) extends `Identity() {
	visit Instruction {
		CompiledMatch[AutomataInst=inst, Options=optionList] -> {
			moduleName, ok := tools.GetModuleName(`optionList)
			if !ok {
				if m, ok := tom__strat.stack.peek(); ok {
					moduleName = m
					tom__strat.stack.push(moduleName)
				}
			} else {
				tom__strat.stack.push(moduleName)
			}
			sl.MustVisit(tom__strat.markStrategy.VisitLight(`inst, sl.VisitableIntrospector{}))
			sl.ThrowVisitFailure()
		}
		RawAction[AstInstruction=inst] -> {
			sl.MustVisit(tom__strat.markStrategy.VisitLight(`inst, sl.VisitableIntrospector{}))
			sl.ThrowVisitFailure()
		}
	}

	visit Expression {
		(IsEmptyList|IsEmptyArray|GetHead|GetTail|GetSize|GetElement)[Opname=Name(name)] -> {
			if moduleName, ok := tom__strat.stack.peek(); ok {
				tomSymbol := tools.GetSymbolFromName(`name, tom__strat.bp.getSymbolTable(moduleName))
				tom__strat.bp.setUsedSymbolConstructor(moduleName, tomSymbol, tom__strat.markStrategy)
			}
		}
		IsFsym[AstName=Name(name)] -> {
			if moduleName, ok := tom__strat.stack.peek(); ok {
				tomSymbol := tools.GetSymbolFromName(`name, tom__strat.bp.getSymbolTable(moduleName))
				tom__strat.bp.setUsedSymbolDestructor(moduleName, tomSymbol, tom__strat.markStrategy)
			}
		}
	}

	visit TomType {
		Type[TomType=ty] -> {
			if moduleName, ok := tom__strat.stack.peek(); ok {
				tom__strat.bp.setUsedType(moduleName, `ty, tom__strat.markStrategy)
			}
		}
	}

	visit TomTerm {
		RecordAppl[NameList=nameList] -> {
			for l := `nameList; !l.IsEmptyconcTomName(); l = l.GetTailconcTomName() {
				if moduleName, ok := tom__strat.stack.peek(); ok {
					tomSymbol := tools.GetSymbolFromName(l.GetHeadconcTomName().GetString(), tom__strat.bp.getSymbolTable(moduleName))
					if tomSymbol != nil { // Java: "if it comes from java"
						tom__strat.bp.setUsedSymbolDestructor(moduleName, tomSymbol, tom__strat.markStrategy)
					}
				}
			}
			sl.ThrowVisitFailure()
		}
	}

	visit BQTerm {
		(BuildTerm|BuildEmptyArray)[AstName=Name(name)] -> {
			if moduleName, ok := tom__strat.stack.peek(); ok {
				tomSymbol := tools.GetSymbolFromName(`name, tom__strat.bp.getSymbolTable(moduleName))
				tom__strat.bp.setUsedSymbolConstructor(moduleName, tomSymbol, tom__strat.markStrategy)
			}
		}
		(BuildConsList|BuildEmptyList|BuildAppendList|BuildConsArray|BuildAppendArray)[AstName=Name(name)] -> {
			if moduleName, ok := tom__strat.stack.peek(); ok {
				tomSymbol := tools.GetSymbolFromName(`name, tom__strat.bp.getSymbolTable(moduleName))
				tom__strat.bp.setUsedSymbolConstructor(moduleName, tomSymbol, tom__strat.markStrategy)
				// Java: also mark the destructors, some generated functions use them
				tom__strat.bp.setUsedSymbolDestructor(moduleName, tomSymbol, tom__strat.markStrategy)
			}
		}
	}

	visit Declaration {
		IsFsymDecl[AstName=Name(opname), Expr=Code(code)] -> {
			if moduleName, ok := tom__strat.stack.peek(); ok {
				tom__strat.bp.getSymbolTable(moduleName).PutIsFsym(`opname, `code)
			}
		}
		IsSortDecl[TermArg=BQVariable[AstType=Type[TomType=ty]], Expr=Code(code)] -> {
			if moduleName, ok := tom__strat.stack.peek(); ok {
				tom__strat.bp.getSymbolTable(moduleName).PutIsSort(`ty, `code)
			}
		}
		EqualTermDecl[TermArg1=BQVariable[AstType=Type[TomType=ty]], Expr=Code(code)] -> {
			if moduleName, ok := tom__strat.stack.peek(); ok {
				tom__strat.bp.getSymbolTable(moduleName).PutEqualTerm(`ty, `code)
			}
		}
		GetSlotDecl[AstName=Name(opname), SlotName=Name(slotName), Expr=Code(code)] -> {
			if moduleName, ok := tom__strat.stack.peek(); ok {
				tom__strat.bp.getSymbolTable(moduleName).PutGetSlot(`opname, `slotName, `code)
			}
		}
		GetHeadDecl[Opname=Name(opname), Expr=Code(code)] -> {
			if moduleName, ok := tom__strat.stack.peek(); ok {
				tom__strat.bp.getSymbolTable(moduleName).PutGetHead(`opname, `code)
			}
		}
		GetTailDecl[Opname=Name(opname), Expr=Code(code)] -> {
			if moduleName, ok := tom__strat.stack.peek(); ok {
				tom__strat.bp.getSymbolTable(moduleName).PutGetTail(`opname, `code)
			}
		}
		MakeDecl[AstName=Name(opname), Instr=ExpressionToInstruction(Code(code))] -> {
			if moduleName, ok := tom__strat.stack.peek(); ok {
				tom__strat.bp.getSymbolTable(moduleName).PutMake(`opname, `code)
			}
		}
		IsEmptyDecl[Opname=Name(opname), Expr=Code(code)] -> {
			if moduleName, ok := tom__strat.stack.peek(); ok {
				tom__strat.bp.getSymbolTable(moduleName).PutIsEmptyList(`opname, `code)
			}
		}
		MakeEmptyList[AstName=Name(opname), Instr=ExpressionToInstruction(Code(code))] -> {
			if moduleName, ok := tom__strat.stack.peek(); ok {
				tom__strat.bp.getSymbolTable(moduleName).PutMakeEmptyList(`opname, `code)
			}
		}
		MakeAddList[AstName=Name(opname), Instr=ExpressionToInstruction(Code(code))] -> {
			if moduleName, ok := tom__strat.stack.peek(); ok {
				tom__strat.bp.getSymbolTable(moduleName).PutMakeAddList(`opname, `code)
			}
		}
		MakeEmptyArray[AstName=Name(opname), Instr=ExpressionToInstruction(Code(code))] -> {
			if moduleName, ok := tom__strat.stack.peek(); ok {
				tom__strat.bp.getSymbolTable(moduleName).PutMakeEmptyArray(`opname, `code)
			}
		}
		MakeAddArray[AstName=Name(opname), Instr=ExpressionToInstruction(Code(code))] -> {
			if moduleName, ok := tom__strat.stack.peek(); ok {
				tom__strat.bp.getSymbolTable(moduleName).PutMakeAddArray(`opname, `code)
			}
		}
		GetElementDecl[Opname=Name(opname), Expr=Code(code)] -> {
			if moduleName, ok := tom__strat.stack.peek(); ok {
				tom__strat.bp.getSymbolTable(moduleName).PutGetElementArray(`opname, `code)
			}
		}
		GetSizeDecl[Opname=Name(opname), Expr=Code(code)] -> {
			if moduleName, ok := tom__strat.stack.peek(); ok {
				tom__strat.bp.getSymbolTable(moduleName).PutGetSizeArray(`opname, `code)
			}
		}
		(SymbolDecl|ListSymbolDecl|ArraySymbolDecl)[AstName=Name(opname)] -> {
			if moduleName, ok := tom__strat.stack.peek(); ok {
				tomSymbol := tools.GetSymbolFromName(`opname, tom__strat.bp.getSymbolTable(moduleName))
				// Java: not inside try/catch(VisitFailure): a failure cuts the descent
				sl.MustVisit(tom__strat.markStrategy.VisitLight(tomSymbol, sl.VisitableIntrospector{}))
			}
		}
		ResolveGetSlotDecl[AstName=Name(opname), SlotName=Name(slotName)] -> {
			if moduleName, ok := tom__strat.stack.peek(); ok {
				tom__strat.bp.getSymbolTable(moduleName).PutResolveGetSlot(`opname, `slotName)
			}
		}
	}
}

// CollectorDump runs the Collector on the code of a bundle, from the
// symbol table and used marks the bundle carries (the marks of the earlier
// phases, such as concInt for AC matching), and returns the state it
// leaves: the used marks (GetUsedKeyEntries), then the inliners, one per
// line, as CollectorState.java prints them (plan J4.1b, Task 1).
func CollectorDump(b *bridge.Bundle) string {
	st := tools.FromBundle(b)
	(&backendPlugin{st: st}).markUsedConstructorDestructor(b.Code)
	var sb strings.Builder
	for _, ke := range st.GetUsedKeyEntries() {
		fmt.Fprintf(&sb, "used %s\n", ke)
	}
	for _, e := range st.InlinerEntries() {
		fmt.Fprintf(&sb, "inliner %s %s\n", bridge.JavaQuote(e.Key), bridge.JavaQuote(e.Code))
	}
	return sb.String()
}

// The generated mapping code (adt.tom) names aterm types; this keeps the import used.
var _ = aterm.GetInstance

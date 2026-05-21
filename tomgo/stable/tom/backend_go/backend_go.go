// Package backend_go is the Go-target counterpart of stable/tom/backend.
//
// The compiler pipeline (parser → optimizer) is language-agnostic.
// Only this final phase translates the post-Compiler IR (Let / If /
// Assign / Return / Cast / IsSort / IsFsym / GetSlot / BuildTerm /
// …) to host-language source — Java in `stable/tom/backend/`, Go
// here.
//
// The output is a single `.go` source file: TL chunks pass through
// verbatim (they already contain valid Go from the `.t` file) and
// the Tom-emitted helpers + compiled matches are rendered as Go
// `func` declarations and statements.
//
// This first cut covers exactly what Peano needs:
//   - `%typeterm`        → `tom_is_sort_X` / `tom_equal_term_X`
//   - `%op`              → `tom_is_fun_sym_X` / `tom_make_X` /
//                          `tom_get_slot_X_<slot>`
//   - `%match`           → Let-chained `if` blocks (via the
//                          compiler's CompiledMatch shape)
//   - BuildTerm / BQVariable / Return — `%match` action bodies
//
// Variadic-list/array, %strategy, %gom, etc. are NOT yet wired up
// — Peano doesn't need them.
package backend_go

import (
	"fmt"
	"strings"

	"tom/tomgo/stable/library/tomast"
	"tom/tomgo/stable/tom"
)

// Run is the public phase entry point. Drops the generated Go
// source into `state.Source`, leaving `state.Code` untouched (the
// other phases don't run after this).
func Run(in tom.State) (tom.State, error) {
	if in.Code == nil {
		return in, nil
	}
	code, ok := in.Code.(*tomast.TomCode)
	if !ok {
		return in, fmt.Errorf("backend_go: expected *TomCode, got %T", in.Code)
	}
	list, ok := code.CodeList.(*tomast.ConcCodeCodeList)
	if !ok {
		return in, fmt.Errorf("backend_go: expected ConcCode list")
	}
	used := collectUsedSymbols(list)
	e := &emitter{symbols: in.Symbols, usedSymbols: used}
	for _, c := range list.Slots {
		if err := e.emitCode(c); err != nil {
			return in, err
		}
	}
	out := in
	out.Source = []byte(e.buf.String())
	return out, nil
}

type emitter struct {
	buf         strings.Builder
	symbols     *tom.SymbolTable
	usedSymbols map[string]bool
}

// emitCode dispatches on the AST shape of one top-level code entry.
func (e *emitter) emitCode(c tomast.Code) error {
	switch x := c.(type) {
	case *tomast.TargetLanguageToCodeCode:
		return e.emitTL(x.Tl)
	case *tomast.DeclarationToCodeCode:
		return e.emitDecl(x.AstDeclaration)
	case *tomast.InstructionToCodeCode:
		return e.emitInstruction(x.AstInstruction)
	case *tomast.TomIncludeCode:
		if cl, ok := x.CodeList.(*tomast.ConcCodeCodeList); ok {
			for _, inner := range cl.Slots {
				if err := e.emitCode(inner); err != nil {
					return err
				}
			}
		}
	}
	return nil
}

func (e *emitter) emitTL(t tomast.TargetLanguage) error {
	switch x := t.(type) {
	case *tomast.TLTargetLanguage:
		e.buf.WriteString(x.Code)
	case *tomast.ITLTargetLanguage:
		e.buf.WriteString(x.Code)
	}
	return nil
}

func (e *emitter) emitDecl(d tomast.Declaration) error {
	switch x := d.(type) {
	case *tomast.TypeTermDeclDeclaration:
		return e.emitTypeTermHelpers(x)
	case *tomast.SymbolDeclDeclaration:
		return e.emitSymbolHelpers(x.AstName)
	case *tomast.ListSymbolDeclDeclaration:
		return e.emitSymbolHelpers(x.AstName)
	case *tomast.ArraySymbolDeclDeclaration:
		return e.emitSymbolHelpers(x.AstName)
	}
	return nil
}

// emitTypeTermHelpers writes `tom_is_sort_X` / `tom_equal_term_X`
// from the `is_sort(t)` / `equals(t1, t2)` hooks. Unlike Java where
// Object is the natural universal type, Go uses the typeterm's
// `implement` TL (e.g. `*Term`) so user-written bodies like
// `$t != nil` or `$t1.Equals($t2)` type-check.
func (e *emitter) emitTypeTermHelpers(t *tomast.TypeTermDeclDeclaration) error {
	sortName := nameString(t.AstName)
	sortTL := sortName
	if e.symbols != nil {
		if body, ok := e.symbols.Sorts[sortName]; ok && strings.TrimSpace(body) != "" {
			sortTL = strings.TrimSpace(body)
		}
	}
	for _, d := range flattenDeclList(t.Declarations) {
		switch decl := d.(type) {
		case *tomast.EqualTermDeclDeclaration:
			body := substituteDollarVars(codeBodyFromExpr(decl.Expr), []string{"t1", "t2"})
			fmt.Fprintf(&e.buf, "\nfunc tom_equal_term_%s(t1, t2 %s) bool { return %s }\n",
				sortName, sortTL, body)
		case *tomast.IsSortDeclDeclaration:
			body := substituteDollarVars(codeBodyFromExpr(decl.Expr), []string{"t"})
			fmt.Fprintf(&e.buf, "\nfunc tom_is_sort_%s(t %s) bool { return %s }\n",
				sortName, sortTL, body)
		}
	}
	return nil
}

// emitSymbolHelpers writes per-operator helpers (filtered through
// `usedSymbols` — dead-code elimination, same convention as Java).
func (e *emitter) emitSymbolHelpers(name tomast.TomName) error {
	if e.symbols == nil {
		return nil
	}
	opName := nameString(name)
	if !e.usedSymbols[opName] {
		return nil
	}
	sym, ok := e.symbols.Symbols[opName]
	if !ok {
		return nil
	}
	s, ok := sym.(*tomast.SymbolTomSymbol)
	if !ok {
		return nil
	}
	opts, ok := s.Options.(*tomast.ConcOptionOptionList)
	if !ok {
		return nil
	}
	codomainTL := tlTypeFor(s, e.symbols)
	for _, opt := range opts.Slots {
		declOpt, ok := opt.(*tomast.DeclarationToOptionOption)
		if !ok {
			continue
		}
		switch decl := declOpt.AstDeclaration.(type) {
		case *tomast.IsFsymDeclDeclaration:
			body := substituteDollarVars(codeBodyFromExpr(decl.Expr), []string{"t"})
			fmt.Fprintf(&e.buf, "\nfunc tom_is_fun_sym_%s(t %s) bool { return %s }\n",
				opName, codomainTL, body)
		case *tomast.MakeDeclDeclaration:
			params, names := makeDeclParams(decl, e.symbols)
			fmt.Fprintf(&e.buf, "\nfunc tom_make_%s(%s) %s { return ",
				opName, params, codomainTL)
			if body := codeBodyFromInstruction(decl.Instr); body != "" {
				// Body kept as raw text by the parser (no backquote
				// inside). Substitute `$<argname>` → `<argname>`.
				e.buf.WriteString(substituteDollarVars(body, names))
			} else {
				e.emitMakeBody(decl.Instr, names)
			}
			e.buf.WriteString(" }\n")
		case *tomast.GetSlotDeclDeclaration:
			body := substituteDollarVars(codeBodyFromExpr(decl.Expr), []string{"t"})
			slotName := nameString(decl.SlotName)
			slotTL := slotTLTypeFor(s, slotName, e.symbols)
			if slotTL == "" {
				slotTL = codomainTL
			}
			fmt.Fprintf(&e.buf, "\nfunc tom_get_slot_%s_%s(t %s) %s { return %s }\n",
				opName, slotName, codomainTL, slotTL, body)
		}
	}
	return nil
}

// emitInstruction dispatches on the post-compile instruction shapes.
func (e *emitter) emitInstruction(i tomast.Instruction) error {
	if handled, err := e.emitIRInstruction(i); handled {
		return err
	}
	switch x := i.(type) {
	case *tomast.AbstractBlockInstruction:
		if list, ok := x.InstList.(*tomast.ConcInstructionInstructionList); ok {
			for _, inner := range list.Slots {
				if err := e.emitInstruction(inner); err != nil {
					return err
				}
			}
		}
	case *tomast.UnamedBlockInstruction:
		// Host-language braces inside an action. In Go this becomes
		// a plain `{ ... }` scope. The trailing `\n` triggers Go's
		// auto-semicolon insertion so two sibling blocks `}{` don't
		// run together as one syntactically invalid statement.
		e.buf.WriteString("{")
		if list, ok := x.InstList.(*tomast.ConcInstructionInstructionList); ok {
			for _, inner := range list.Slots {
				if err := e.emitInstruction(inner); err != nil {
					return err
				}
			}
		}
		e.buf.WriteString("}\n")
	case *tomast.CodeToInstructionInstruction:
		return e.emitCode(x.Code)
	case *tomast.IfInstruction:
		// Go's `if cond { … }` — no parens around the condition.
		e.buf.WriteString("if ")
		e.emitExpression(x.Condition)
		e.buf.WriteString(" {")
		if err := e.emitInstruction(x.SuccesInst); err != nil {
			return err
		}
		e.buf.WriteString("}")
		if _, nop := x.FailureInst.(*tomast.NopInstruction); !nop {
			e.buf.WriteString(" else {")
			if err := e.emitInstruction(x.FailureInst); err != nil {
				return err
			}
			e.buf.WriteString("}")
		}
	case *tomast.NopInstruction:
		// Nothing.
	default:
		fmt.Fprintf(&e.buf, "/* backend_go: unsupported instruction %T */", x)
	}
	return nil
}

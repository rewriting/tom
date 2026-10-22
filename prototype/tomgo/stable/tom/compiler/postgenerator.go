// Package compiler — PostGenerator.
//
// Java reference: stable/tom/engine/compiler/PostGenerator.t (136 LOC).
//
// PostGenerator runs a single bottom-up pass over the freshly
// generated automaton and does two related rewrites on every
// `LetRef(var, exp, body)`:
//
//  1. If `var` has already been declared by a LetRef above (somewhere
//     between root and the current node), the LetRef is downgraded
//     to `AbstractBlock([Assign(var, exp), body])`. This avoids
//     shadowing the previous binding.
//
//  2. Otherwise, if `body` never reassigns or re-declares `var`, the
//     LetRef is upgraded to `Let(var, exp, body)` — a mutability hint
//     that Java code generators consume.
//
// Functionally the matching automaton compiles to working host code
// without either rewrite (Java `final` and Java rebinds both compile),
// so a pure-identity PostGenerator is correct but loses the
// immutability annotation Java uses to refine emitted code. The
// minimal Go port performs rewrite (2) only — single bottom-up walk,
// no Omega root-path. Rewrite (1) is irrelevant for Peano-class
// fixtures (Compiler's renameSubjects already produces unique names).

package compiler

import (
	"strings"

	"tom/tomgo/stable/library/tomast"
)

// performPostGenerationTreatment runs the LetRef→Let upgrade
// bottom-up. Java's TopDown variant uses Omega paths to look at
// ancestors; the Go port instead does a single recursive walk that
// inspects only descendants — sufficient for rewrite (2).
func (e *compilerEnv) performPostGenerationTreatment(i tomast.Instruction) tomast.Instruction {
	// First pass: top-down LetRef → Assign for names already
	// declared higher in the path. Mirrors Java
	// PostGenerator.ChangeVarDeclarations using Omega root-paths.
	i = downgradeReLetRefs(i, map[string]bool{})
	// Second pass: bottom-up LetRef → Let when the body never
	// re-assigns / re-declares the variable.
	return e.postRewriteInstr(i)
}

// downgradeReLetRefs is the top-down LetRef→Assign rewrite for
// re-declarations along the path from root. `declared` is the set
// of variable names declared by enclosing LetRef / Let / Assign
// nodes; when a nested LetRef re-declares one of those, we replace
// it with `AbstractBlock([Assign(var, src), body])`.
func downgradeReLetRefs(i tomast.Instruction, declared map[string]bool) tomast.Instruction {
	switch x := i.(type) {
	case *tomast.LetRefInstruction:
		name := bqVarName(x.Variable)
		if declared[name] {
			// Re-declaration: downgrade to AbstractBlock with Assign.
			newBody := downgradeReLetRefs(x.AstInstruction, declared)
			return tomast.MakeAbstractBlock(tomast.MakeConcInstruction(
				tomast.MakeAssign(x.Variable, x.Source),
				newBody,
			))
		}
		declared[name] = true
		newBody := downgradeReLetRefs(x.AstInstruction, declared)
		delete(declared, name)
		if newBody != x.AstInstruction {
			return tomast.MakeLetRef(x.Variable, x.Source, newBody)
		}
	case *tomast.LetInstruction:
		name := bqVarName(x.Variable)
		declared[name] = true
		newBody := downgradeReLetRefs(x.AstInstruction, declared)
		delete(declared, name)
		if newBody != x.AstInstruction {
			return tomast.MakeLet(x.Variable, x.Source, newBody)
		}
	case *tomast.IfInstruction:
		t := downgradeReLetRefs(x.SuccesInst, declared)
		f := downgradeReLetRefs(x.FailureInst, declared)
		if t != x.SuccesInst || f != x.FailureInst {
			return tomast.MakeIf(x.Condition, t, f)
		}
	case *tomast.AbstractBlockInstruction:
		list, ok := x.InstList.(*tomast.ConcInstructionInstructionList)
		if !ok {
			return i
		}
		newSlots := make([]tomast.Instruction, len(list.Slots))
		changed := false
		for k, s := range list.Slots {
			r := downgradeReLetRefs(s, declared)
			if r != s {
				changed = true
			}
			newSlots[k] = r
		}
		if changed {
			return tomast.MakeAbstractBlock(tomast.MakeConcInstruction(newSlots...))
		}
	case *tomast.UnamedBlockInstruction:
		list, ok := x.InstList.(*tomast.ConcInstructionInstructionList)
		if !ok {
			return i
		}
		newSlots := make([]tomast.Instruction, len(list.Slots))
		changed := false
		for k, s := range list.Slots {
			r := downgradeReLetRefs(s, declared)
			if r != s {
				changed = true
			}
			newSlots[k] = r
		}
		if changed {
			return tomast.MakeUnamedBlock(tomast.MakeConcInstruction(newSlots...))
		}
	case *tomast.DoWhileInstruction:
		body := downgradeReLetRefs(x.DoInst, declared)
		if body != x.DoInst {
			return tomast.MakeDoWhile(body, x.Condition)
		}
	case *tomast.WhileDoInstruction:
		body := downgradeReLetRefs(x.DoInst, declared)
		if body != x.DoInst {
			return tomast.MakeWhileDo(x.Condition, body)
		}
	case *tomast.CompiledMatchInstruction:
		body := downgradeReLetRefs(x.AutomataInst, declared)
		if body != x.AutomataInst {
			return tomast.MakeCompiledMatch(body, x.Options)
		}
	case *tomast.CompiledPatternInstruction:
		body := downgradeReLetRefs(x.AutomataInst, declared)
		if body != x.AutomataInst {
			return tomast.MakeCompiledPattern(x.Contraint, body)
		}
	}
	return i
}

func (e *compilerEnv) postRewriteInstr(i tomast.Instruction) tomast.Instruction {
	switch x := i.(type) {
	case *tomast.LetRefInstruction:
		newBody := e.postRewriteInstr(x.AstInstruction)
		name := bqVarName(x.Variable)
		if !instrAssignsOrReLetRefs(newBody, name) {
			return tomast.MakeLet(x.Variable, x.Source, newBody)
		}
		if newBody != x.AstInstruction {
			return tomast.MakeLetRef(x.Variable, x.Source, newBody)
		}
	case *tomast.LetInstruction:
		newBody := e.postRewriteInstr(x.AstInstruction)
		if newBody != x.AstInstruction {
			return tomast.MakeLet(x.Variable, x.Source, newBody)
		}
	case *tomast.IfInstruction:
		t := e.postRewriteInstr(x.SuccesInst)
		f := e.postRewriteInstr(x.FailureInst)
		if t != x.SuccesInst || f != x.FailureInst {
			return tomast.MakeIf(x.Condition, t, f)
		}
	case *tomast.AbstractBlockInstruction:
		list, ok := x.InstList.(*tomast.ConcInstructionInstructionList)
		if !ok {
			return i
		}
		newSlots := make([]tomast.Instruction, len(list.Slots))
		changed := false
		for k, s := range list.Slots {
			r := e.postRewriteInstr(s)
			if r != s {
				changed = true
			}
			newSlots[k] = r
		}
		if changed {
			return tomast.MakeAbstractBlock(tomast.MakeConcInstruction(newSlots...))
		}
	case *tomast.UnamedBlockInstruction:
		list, ok := x.InstList.(*tomast.ConcInstructionInstructionList)
		if !ok {
			return i
		}
		newSlots := make([]tomast.Instruction, len(list.Slots))
		changed := false
		for k, s := range list.Slots {
			r := e.postRewriteInstr(s)
			if r != s {
				changed = true
			}
			newSlots[k] = r
		}
		if changed {
			return tomast.MakeUnamedBlock(tomast.MakeConcInstruction(newSlots...))
		}
	case *tomast.DoWhileInstruction:
		body := e.postRewriteInstr(x.DoInst)
		if body != x.DoInst {
			return tomast.MakeDoWhile(body, x.Condition)
		}
	case *tomast.CompiledMatchInstruction:
		body := e.postRewriteInstr(x.AutomataInst)
		if body != x.AutomataInst {
			return tomast.MakeCompiledMatch(body, x.Options)
		}
	case *tomast.CompiledPatternInstruction:
		body := e.postRewriteInstr(x.AutomataInst)
		if body != x.AutomataInst {
			return tomast.MakeCompiledPattern(x.Contraint, body)
		}
	}
	return i
}

// instrAssignsOrReLetRefs returns true when `body` contains either
// an `Assign`/`AssignArray` of `name` or a nested `LetRef`/`Let` that
// re-declares it. That's the signal Java uses to decide whether to
// keep LetRef vs. downgrade to Let.
func instrAssignsOrReLetRefs(body tomast.Instruction, name string) bool {
	var hit bool
	var walk func(tomast.Instruction)
	walk = func(i tomast.Instruction) {
		if hit {
			return
		}
		switch x := i.(type) {
		case *tomast.AssignInstruction:
			if bqVarName(x.Variable) == name {
				hit = true
			}
		case *tomast.AssignArrayInstruction:
			if bqVarName(x.Variable) == name {
				hit = true
			}
		case *tomast.LetRefInstruction:
			if bqVarName(x.Variable) == name {
				hit = true
				return
			}
			walk(x.AstInstruction)
		case *tomast.LetInstruction:
			walk(x.AstInstruction)
		case *tomast.IfInstruction:
			walk(x.SuccesInst)
			walk(x.FailureInst)
		case *tomast.AbstractBlockInstruction:
			if l, ok := x.InstList.(*tomast.ConcInstructionInstructionList); ok {
				for _, s := range l.Slots {
					walk(s)
				}
			}
		case *tomast.UnamedBlockInstruction:
			if l, ok := x.InstList.(*tomast.ConcInstructionInstructionList); ok {
				for _, s := range l.Slots {
					walk(s)
				}
			}
		case *tomast.DoWhileInstruction:
			walk(x.DoInst)
		case *tomast.CompiledMatchInstruction:
			walk(x.AutomataInst)
		case *tomast.CompiledPatternInstruction:
			walk(x.AutomataInst)
		}
	}
	walk(body)
	return hit
}

// bqVarName returns a stable string identifier for the AstName of a
// BQVariable / BQVariableStar. For `Name(s)` it returns `s`. For
// `PositionName(concTomNumber(MatchNumber(N), NameNumber(Name(s))))`
// it returns a deterministic string built from the components, so
// post-gen passes can compare names by string equality. Mirrors the
// rendering convention in the Backend's `renderTomName`.
func bqVarName(b tomast.BQTerm) string {
	switch x := b.(type) {
	case *tomast.BQVariableBQTerm:
		return tomNameKey(x.AstName)
	case *tomast.BQVariableStarBQTerm:
		return tomNameKey(x.AstName)
	}
	return ""
}

// tomNameKey serialises a TomName into a stable key — Name(s) → s;
// PositionName(parts) → joined parts (MatchNumber(N) → "tomMatchN";
// NameNumber(Name(s)) → s).
func tomNameKey(n tomast.TomName) string {
	switch x := n.(type) {
	case *tomast.NameTomName:
		return x.String_
	case *tomast.PositionNameTomName:
		var sb stringsBuilder
		if list, ok := x.NumberList.(*tomast.ConcTomNumberTomNumberList); ok {
			for _, num := range list.Slots {
				sb.WriteString(tomNumberKey(num))
			}
		}
		return sb.String()
	}
	return ""
}

// tomNumberKey is the compiler-side mirror of the Backend's
// renderTomNumber — produces the same string fragments.
func tomNumberKey(n tomast.TomNumber) string {
	switch x := n.(type) {
	case *tomast.MatchNumberTomNumber:
		return fmtInt("tomMatch", x.Integer)
	case *tomast.PatternNumberTomNumber:
		return fmtInt("_pattern", x.Integer)
	case *tomast.NameNumberTomNumber:
		return tomNameKey(x.AstName)
	}
	return ""
}

// stringsBuilder is a thin wrapper around strings.Builder that lets
// us avoid an import cycle if the compiler package needs more text
// helpers later.
type stringsBuilder struct{ sb strings.Builder }

func (s *stringsBuilder) WriteString(v string) (int, error) { return s.sb.WriteString(v) }
func (s *stringsBuilder) String() string                    { return s.sb.String() }

func fmtInt(prefix string, n int64) string {
	return prefix + fmtIntStr(n)
}

func fmtIntStr(n int64) string {
	if n == 0 {
		return "0"
	}
	neg := n < 0
	if neg {
		n = -n
	}
	var buf [20]byte
	i := len(buf)
	for n > 0 {
		i--
		buf[i] = byte('0' + n%10)
		n /= 10
	}
	if neg {
		i--
		buf[i] = '-'
	}
	return string(buf[i:])
}

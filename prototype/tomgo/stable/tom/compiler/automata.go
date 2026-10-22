// Package compiler — automata-list assembly.
//
// Java reference: Compiler.t:363-381 (automataListCompileMatchingList).
//
// After CompileMatch has built one `Automata(opts, constraint, path,
// instr)` per ConstraintInstruction, this helper turns the list into
// a sequence of `UnamedBlock(CompiledPattern(constraint, instr))`
// (or `UnamedBlock(CompiledPattern(constraint, NamedBlock(name,
// instr)))` if the rule carried a `Label(Name(name))` option).
//
// The UnamedBlock around each pattern gives the generated code a
// fresh scope so the per-pattern Let/LetRef bindings don't leak.

package compiler

import (
	"tom/tomgo/stable/library/tomast"
)

// automataListCompileMatchingList — Go port of the Java
// static helper of the same name in Compiler.t. Returns the list of
// per-pattern instructions ready to be wrapped in CompiledMatch's
// AbstractBlock.
func automataListCompileMatchingList(automataList tomast.TomList) []tomast.Instruction {
	conc, ok := automataList.(*tomast.ConcTomTermTomList)
	if !ok {
		return nil
	}
	out := make([]tomast.Instruction, 0, len(conc.Slots))
	for _, item := range conc.Slots {
		aut, ok := item.(*tomast.AutomataTomTerm)
		if !ok {
			continue
		}
		instr := aut.Inst
		if labelName, ok := findLabelOption(aut.Options); ok {
			instr = tomast.MakeNamedBlock(labelName,
				tomast.MakeConcInstruction(instr))
		}
		out = append(out,
			tomast.MakeUnamedBlock(tomast.MakeConcInstruction(
				tomast.MakeCompiledPattern(aut.Constraint, instr),
			)),
		)
	}
	return out
}

// findLabelOption walks the OptionList looking for a `Label(Name(s))`
// entry. Returns the string and `true` when found.
func findLabelOption(opts tomast.OptionList) (string, bool) {
	conc, ok := opts.(*tomast.ConcOptionOptionList)
	if !ok {
		return "", false
	}
	for _, o := range conc.Slots {
		lbl, ok := o.(*tomast.LabelOption)
		if !ok {
			continue
		}
		if n, ok := lbl.AstName.(*tomast.NameTomName); ok {
			return n.String_, true
		}
	}
	return "", false
}

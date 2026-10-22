// CompileMatch — Go port of tom.engine.compiler.Compiler.t's
// `CompileMatch` strategy. Top-level driver for the new pipeline.
//
// Java reference: stable/tom/engine/compiler/Compiler.t  (~893 LOC)
//
// Status: SKELETON. The Run-equivalent walks the Code top-down,
// detects each MatchInstruction, and currently delegates to the
// legacy compiler's per-instruction transform. As the propagator /
// pre-generator / generator / post-generator land (tasks #59–#62),
// this file replaces the delegate with the faithful pipeline step
// by step.

package compiler

import (
	"fmt"

	"tom/tomgo/stable/library/tomast"
	"tom/tomgo/stable/tom"
)

// runNew is the new pipeline entry. Currently it just walks the AST
// and falls back to the legacy compilation for each MatchInstruction
// it finds. Subsequent commits will replace the delegate body with
// the propagator → pre-gen → generator → post-gen → wrap-in-
// CompiledMatch chain.
func runNew(in tom.State) (tom.State, error) {
	if in.Code == nil {
		return in, nil
	}
	env := newCompilerEnv(in.Symbols)
	code, ok := in.Code.(*tomast.TomCode)
	if !ok {
		return in, fmt.Errorf("compiler: expected *TomCode, got %T", in.Code)
	}
	list, ok := code.CodeList.(*tomast.ConcCodeCodeList)
	if !ok {
		return in, fmt.Errorf("compiler: expected ConcCode list")
	}
	newSlots := make([]tomast.Code, len(list.Slots))
	for i, item := range list.Slots {
		newSlots[i] = env.compileCode(item)
	}
	out := in
	out.Code = tomast.MakeTom(tomast.MakeConcCode(newSlots...))
	return out, nil
}

// compileCode mirrors the TopDown walk of CompileMatch — when we
// reach an Instruction we recurse; everything else is passed through.
func (e *compilerEnv) compileCode(x tomast.Code) tomast.Code {
	switch v := x.(type) {
	case *tomast.InstructionToCodeCode:
		return tomast.MakeInstructionToCode(e.compileInstr(v.AstInstruction))
	case *tomast.TomIncludeCode:
		if cl, ok := v.CodeList.(*tomast.ConcCodeCodeList); ok {
			newSlots := make([]tomast.Code, len(cl.Slots))
			for i, item := range cl.Slots {
				newSlots[i] = e.compileCode(item)
			}
			return tomast.MakeTomInclude(tomast.MakeConcCode(newSlots...))
		}
	}
	return x
}

// compileInstr is the workhorse. When it finds a MatchInstruction it
// runs the (eventual) pipeline and replaces it with
// UnamedBlock(CompiledMatch(AbstractBlock(automataList), opts)).
// Otherwise it recurses into structural shapes.
func (e *compilerEnv) compileInstr(i tomast.Instruction) tomast.Instruction {
	switch v := i.(type) {
	case *tomast.MatchInstruction:
		return e.compileMatch(v)
	case *tomast.AbstractBlockInstruction:
		if list, ok := v.InstList.(*tomast.ConcInstructionInstructionList); ok {
			newSlots := make([]tomast.Instruction, len(list.Slots))
			for j, item := range list.Slots {
				newSlots[j] = e.compileInstr(item)
			}
			return tomast.MakeAbstractBlock(tomast.MakeConcInstruction(newSlots...))
		}
	case *tomast.UnamedBlockInstruction:
		if list, ok := v.InstList.(*tomast.ConcInstructionInstructionList); ok {
			newSlots := make([]tomast.Instruction, len(list.Slots))
			for j, item := range list.Slots {
				newSlots[j] = e.compileInstr(item)
			}
			return tomast.MakeUnamedBlock(tomast.MakeConcInstruction(newSlots...))
		}
	case *tomast.IfInstruction:
		return tomast.MakeIf(v.Condition,
			e.compileInstr(v.SuccesInst),
			e.compileInstr(v.FailureInst))
	case *tomast.CodeToInstructionInstruction:
		return tomast.MakeCodeToInstruction(e.compileCode(v.Code))
	}
	return i
}

// compileMatch is the heart of CompileMatch. Java code path (line
// 222-264 of Compiler.t):
//
//	for each ConstraintInstruction(c, action, opts) in the Match:
//	  c   = renameSubjects(c)
//	  c   = ConstraintPropagator.performPropagations(c)
//	  e   = PreGenerator.performPreGenerationTreatment(c)
//	  ins = ConstraintGenerator.performGenerations(e, action)
//	  ins = PostGenerator.performPostGenerationTreatment(ins)
//	  push Automata(opts, c, path, ins) to automataList
//	assemble UnamedBlock(CompiledMatch(AbstractBlock(automataList), matchOpts))
//
// The assembly + post-pipeline wrapping is in place. The pipeline
// steps themselves remain stubs (see propagator.go / pregenerator.go
// / generator.go / postgenerator.go) — they will be filled in as
// tasks #59…#62 land. With stubs, the Match expands into a no-op
// CompiledMatch whose body is the action wrapped in UnamedBlock —
// enough to confirm the pipeline plumbing without crashing the
// Backend.
func (e *compilerEnv) compileMatch(m *tomast.MatchInstruction) tomast.Instruction {
	e.nextMatch()

	cilist, ok := m.ConstraintInstructionList.(*tomast.ConcConstraintInstructionConstraintInstructionList)
	if !ok {
		return tomast.MakeNop()
	}

	automata := make([]tomast.TomTerm, 0, len(cilist.Slots))
	subjectList := []tomast.BQTerm{}
	renamedSubjects := []tomast.TomTerm{}
	actionNumber := 0
	for _, item := range cilist.Slots {
		ci, ok := item.(*tomast.ConstraintInstructionConstraintInstruction)
		if !ok {
			continue
		}
		actionNumber++

		// Step 1 — rename subjects (Compiler.t:239).
		newConstraint := e.renameSubjects(ci.Constraint, &subjectList, &renamedSubjects)

		// Step 2 — propagate (constraint-shape dispatcher).
		propagated := e.performPropagations(newConstraint)

		// Step 3 — pre-generate (constraint → Expression).
		preExpr := e.performPreGenerationTreatment(propagated)

		// Step 4 — generate (Expression + action → Instruction).
		generated := e.performGenerations(preExpr, ci.Action)

		// Step 5 — post-generate cleanup.
		postGen := e.performPostGenerationTreatment(generated)

		// Path = rootpath ++ PatternNumber(actionNumber).
		var path tomast.TomNumberList
		if rp, ok := e.rootpath.(*tomast.ConcTomNumberTomNumberList); ok {
			extended := make([]tomast.TomNumber, 0, len(rp.Slots)+1)
			extended = append(extended, rp.Slots...)
			extended = append(extended, tomast.MakePatternNumber(int64(actionNumber)))
			path = tomast.MakeConcTomNumber(extended...)
		} else {
			path = tomast.MakeConcTomNumber(tomast.MakePatternNumber(int64(actionNumber)))
		}

		automata = append(automata,
			tomast.MakeAutomata(ci.Options, newConstraint, path, postGen),
		)
	}

	autList := tomast.MakeConcTomTerm(automata...)
	patternInstructions := automataListCompileMatchingList(autList)

	return tomast.MakeUnamedBlock(tomast.MakeConcInstruction(
		tomast.MakeCompiledMatch(
			tomast.MakeAbstractBlock(tomast.MakeConcInstruction(patternInstructions...)),
			m.Options,
		),
	))
}
